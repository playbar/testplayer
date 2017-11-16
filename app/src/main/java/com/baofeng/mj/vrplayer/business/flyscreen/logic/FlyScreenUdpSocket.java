package com.baofeng.mj.vrplayer.business.flyscreen.logic;

import android.content.Context;
import android.os.Message;

import com.baofeng.mj.vrplayer.bean.DeviceInfo;
import com.baofeng.mj.vrplayer.business.flyscreen.util.FlyScreenUtil;
import  com.baofeng.mj.util.publicutil.NetworkUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;


/**
 * ClassName: FlyScreenUdpSocket
 *
 * @author zhangxin
 * @date: 2015-1-19 下午3:42:08
 * description: udp设备搜索类
 */
public class FlyScreenUdpSocket {

    private static FlyScreenUdpSocket instance;
    private DatagramSocket udpSocket;

    private static final int MSG_TCP_DISCONNECTED = 0x1003;
    private final static int Udp_Port_Listen = 8999;
    private final static int Udp_Port_ToPc = 8989;

    private FlyScreenTcpSocket tcpClient;
    private UdpReceiveThread mr;
    private Context mContext;
    public static boolean isClose = false;
    // 设备列表
    private ArrayList<DeviceInfo> mDevList = new ArrayList<DeviceInfo>();

    private FlyScreenUdpSocket() {

    }


    /**
     * @return
     * @author zhangxin  @Date 2015-1-19 下午3:42:46
     * description:单例
     * @param pContext:上下文
     */
    public static FlyScreenUdpSocket getSingleInstance(Context pContext) {
        if (instance == null) {
            instance = new FlyScreenUdpSocket();
        }

        if (instance.mContext == null && pContext != null) {
            instance.mContext = pContext;
        }
        isClose = false;
        return instance;
    }

    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-19 下午3:43:36
     * description:devlistActivity oncreate中调用，发现设备
     * @param 无
     */
    public void udpDeviceList() {
        tcpClient = FlyScreenTcpSocket.getSingleInstance();
        ExecutorService threadPoolService = tcpClient.getThreadPoolService();
        // 启动组播发送
        mDevList.clear();
        UdpSendThread mc = new UdpSendThread();
        threadPoolService.execute(mc);
    }

    /**
     * ClassName: UdpSendThread
     *
     * @author zhangxin
     * @date: 2015-1-19 下午3:44:37
     * description: udp发送消息线程
     */
    class UdpSendThread implements Runnable {

        @Override
        public void run() {
            initUdp();
            scanIP();
        }

        /**
         * @return 无
         * @author zhangxin  @Date 2015-1-19 下午3:45:07
         * description: 初始哈socket
         * @param 无
         */
        private void initUdp() {
            try {
                if (udpSocket == null) {
                    udpSocket = new DatagramSocket(Udp_Port_Listen);
                }

                // 启动接收线程，持续接收，阻塞
                ExecutorService threadPoolService = tcpClient
                        .getThreadPoolService();

                if (mr == null) {
                    mr = new UdpReceiveThread();
                    threadPoolService.execute(mr);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * @return 无
         * @author zhangxin  @Date 2015-1-19 下午3:46:07
         * description: 向内网的192.168.xx.255 发广播，发3遍；
         * @param 无
         */
        private void scanIP() {
            String[] localip = new String[2];
            localip[0] = NetworkUtil.getLocalIpAddress();
            localip[1] = NetworkUtil.getWifiIpAddress(mContext);
            for (int j = 0; j < localip.length; j++) {
                if (localip[j] == null)
                    continue;
                if (j == 1 && localip[j] == localip[0])
                    continue;

                String[] localip_arr = localip[j].split("\\.");
                if (localip_arr.length < 4)
                    continue;

                StringBuilder ip3 = new StringBuilder();
                ip3.append(localip_arr[0]).append(".").append(localip_arr[1])
                        .append(".").append(localip_arr[2]).append(".");

                StringBuilder ip4 = new StringBuilder();

                for (int i = 0; i <= 2; i++) {
                    ip4.delete(0, ip4.length());
                    ip4.append(ip3).append(255);
                    String ipaddr = ip4.toString();
                    udpToPc(ipaddr);
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

        /**
         * @return 无
         * @author zhangxin  @Date 2015-1-19 下午3:48:29
         * description: 向指定pc发送udp包，询问是否在线
         * @param mIp:pc的ip
         */
        private void udpToPc(String mIp) {

            byte[] itag = FlyScreenUtil.getItagAppToPc_Inner();
            String data = "LookingForFeipingSever";
            byte[] bdata = data.getBytes();
            int length = bdata.length;
            byte[] blen = FlyScreenUtil.intToByteArray(length);
            byte[] msg = new byte[8 + length];
            System.arraycopy(itag, 0, msg, 0, 4);
            System.arraycopy(blen, 0, msg, 4, 4);
            System.arraycopy(bdata, 0, msg, 8, length);
            udpSend(mIp, msg);
        }

        /**
         * @return 无
         * @author zhangxin  @Date 2015-1-19 下午3:49:36
         * description: 发送消息socket封装
         * @param mIp:pc的ip地址；msg:发送的消息
         */
        private void udpSend(String mIp, byte[] msg) {
            try {
                InetAddress host = InetAddress.getByName(mIp);
                if (udpSocket != null) {
                    DatagramPacket dp = new DatagramPacket(msg, msg.length,
                            host, Udp_Port_ToPc);
                    udpSocket.send(dp);
                }
            } catch (IOException e) {
                UdpClose();
                e.printStackTrace();
            }
        }

    }

    /**
     * ClassName: UdpReceiveThread
     *
     * @author zhangxin
     * @date: 2015-1-19 下午3:52:38
     * description:udp接收线程类
     */
    class UdpReceiveThread extends Thread {
        private String ipaddr;

        @Override
        public void run() {
            while (true) {
                if (isClose)
                    break;
                byte[] receBuffer = udpReceive();
                boolean valid = udpCheck(receBuffer);
                if (valid) {
                    udpGetAddDevList(receBuffer);
                    checkTcpDevice_SendOne(ipaddr);
                }
            }
        }

        /**
         * @return 返回接收的数据
         * @author zhangxin  @Date 2015-1-19 下午3:57:26
         * description: 接收udp数据
         * @param 无
         */
        private byte[] udpReceive() {
            try {
                byte[] recebuffer = new byte[128];
                DatagramPacket receDp = new DatagramPacket(recebuffer,
                        recebuffer.length);

                if (udpSocket == null||udpSocket.isClosed()) {
                    return null;
                }
                udpSocket.receive(receDp);

                InetAddress address = receDp.getAddress();
                ipaddr = address.getHostAddress();
                return recebuffer;
            } catch (IOException ioe) {
                UdpClose();
                ioe.printStackTrace();
                return null;
            }
        }

        /**
         * @return 是否是合法数据
         * @author zhangxin  @Date 2015-1-19 下午4:00:33
         * description: 检查接收到的数据是否符合协议要求的合法数据
         * @param receBuffer:接收到的数据
         */
        private boolean udpCheck(byte[] receBuffer) {
            if (receBuffer == null) return false;

            byte[] blen = new byte[4];
            System.arraycopy(receBuffer, 4, blen, 0, 4);

            int len = FlyScreenUtil.byteArrayToInt(blen);
            byte[] bdata = new byte[len];
            System.arraycopy(receBuffer, 8, bdata, 0, len);

            byte[] bpc = new byte[16];
            System.arraycopy(bdata, 0, bpc, 0, 16);
            String s = new String(bpc);

            return s.trim().equalsIgnoreCase("IAMFeipingSever");
        }

        /**
         * @return 无
         * @author zhangxin  @Date 2015-1-19 下午4:01:30
         * description: 从upd数据中取得设备信息并添加到设备列表list中
         * @param receBuffer：接收到的数据
         */
        private void udpGetAddDevList(byte[] receBuffer) {
            DeviceInfo info = new DeviceInfo();
            info.setLive(true);
            info.setIcheck(false);

            // 取端口号
            byte[] bport = new byte[4];
            System.arraycopy(receBuffer, 8 + 16, bport, 0, 4);
            int iport = FlyScreenUtil.byteArrayToInt(bport);
            info.setPort(iport);
            // 取设备id
            byte[] bdevidlen = new byte[4];
            System.arraycopy(receBuffer, 8 + 16 + 4, bdevidlen, 0, 4);
            int idevidlen = FlyScreenUtil.byteArrayToInt(bdevidlen);
            byte[] bdevid = new byte[idevidlen];
            System.arraycopy(receBuffer, 8 + 16 + 8, bdevid, 0, idevidlen);

            // 取设备名
            byte[] bdevlen = new byte[4];
            System.arraycopy(receBuffer, 8 + 16 + 8 + idevidlen, bdevlen, 0, 4);
            int idevlen = FlyScreenUtil.byteArrayToInt(bdevlen);
            byte[] bdevname = new byte[idevlen];
            System.arraycopy(receBuffer, 8 + 16 + 8 + idevidlen + 4, bdevname,
                    0, idevlen);

            try {
                String devid = new String(bdevid, "UTF-8");
                info.setId(devid.trim());
                info.setIp(ipaddr.trim());
                info.setName(new String(bdevname, "UTF-8").trim());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            addDevList(info);
        }
    }

    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-19 下午4:13:05
     * description: udpsocket关闭
     * @param 无
     */
    public void UdpClose() {
        try {
            if (udpSocket != null) {
                udpSocket.close();
                udpSocket = null;
            }

        } catch (Exception e) {

        }
    }


    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-19 下午4:16:06
     * description:tcp检测设备 如设备验证错误，从设备列表删除 如设备全部检查完毕，通知ui展示设备列表 否则继续找一个设备检查
     * @param bufferData：协议中的校验串；ip：该设备ip
     */
    public void checkTcpDevice_Receive(byte[] bufferData, String ip) {
        String s = "";
        try {
            s = new String(bufferData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (s.trim().equalsIgnoreCase("IAMFeipingSever")) {
            setDevChecked(ip); // 设置校验完成

            FlyScreenTcpSocket.HandlerCallBack handler = tcpClient.getDevListHandler();
            Message msg = new Message();
            msg.what = 4;
            //			handler.sendMessage(msg);
            handler.handlerMessage(msg);
        } else if (s.trim().equalsIgnoreCase("SeverIsClosing")) // pc 关闭 连接
        {
            tcpClient.close();
            Message msg = new Message();
            msg.what = MSG_TCP_DISCONNECTED;
            tcpClient.getDevListHandler().handlerMessage(msg);
//            FlyScreenTcpSocket.ActivityType atype = tcpClient.getCurrActivityType();
//            if (atype == null) return;
//
//            switch (atype) {
//                case devlist_reshome:
//                    Message msg = new Message();
//                    msg.what = MSG_TCP_DISCONNECTED;
//                    //				tcpClient.getDevListHandler().sendMessage(msg);
//                    tcpClient.getDevListHandler().handlerMessage(msg);
//                    break;
//                case reslist:
//                    //ResList.tcpDisconnect();
//                    break;
//                default:
//                    break;
//            }
        }
    }

    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-19 下午4:35:50
     * description: 按照协议tcp校验设备
     * @param ip：校验设备ip
     */
    public void checkTcpDevice_SendOne(String ip) {

        if (mDevList.size() == 0) {
            return;
        }

        DeviceInfo dInfo = getDev(ip);
        if (dInfo == null)
            return;

        String host = dInfo.getIp();
        int port = dInfo.getPort();

        tcpClient.tcpConnect(mContext, host, port);

        byte[] btag = FlyScreenUtil.getItagAppToPc_Inner();
        String s = "AreyouFeipingSever";
        byte[] bmsg = null;
        try {
            bmsg = s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] blen = FlyScreenUtil.intToByteArray(bmsg.length);

        byte[] bdata = new byte[8 + bmsg.length];
        System.arraycopy(btag, 0, bdata, 0, 4);
        System.arraycopy(blen, 0, bdata, 4, 4);
        System.arraycopy(bmsg, 0, bdata, 8, bmsg.length);

        int i = 0;
        while (!tcpClient.isTcpConnect(host)) {
            i++;
            if (i > 10)
                break;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        tcpClient.send(mContext, host, bdata);

    }

    /**
     * @return 设备对象
     * @author zhangxin  @Date 2015-1-19 下午4:37:33
     * description: 根据ip取得设备对象
     * @param ip：设备ip
     */
    public DeviceInfo getDev(String ip) {
        for (int i = 0; i < mDevList.size(); i++) {
            DeviceInfo info = mDevList.get(i);
            if (info.getIp().equalsIgnoreCase(ip)) {
                return info;
            }
        }
        return null;
    }

    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-19 下午4:38:07
     * description: 设置设备校验状态为true
     * @param ip：设备ip
     */
    private void setDevChecked(String ip) {
        for (int i = 0; i < mDevList.size(); i++) {
            DeviceInfo info = mDevList.get(i);
            if (info.getIp().equalsIgnoreCase(ip)) {
                info.setIcheck(true);
            }
        }
    }

    /**
     * @return 设备列表list
     * @author zhangxin  @Date 2015-1-19 下午4:39:13
     * description:取得校验为true的设备列表
     * @param 无
     */
    public ArrayList<DeviceInfo> getCheckedDevList() {
        ArrayList<DeviceInfo> list = new ArrayList<DeviceInfo>();

        for (int i = 0; i < mDevList.size(); i++) {
            DeviceInfo info = mDevList.get(i);
            if (info.getIcheck()) {
                list.add(info);
            }
        }
        return list;
    }

    /**
     * @return 设备列表
     * @author zhangxin  @Date 2015-1-19 下午4:40:02
     * description: 取得所有设备列表
     * @param 无
     */
    public ArrayList<DeviceInfo> getDevList() {
        return mDevList;
    }

    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-19 下午4:40:31
     * description:把一个设备加入设备列表中
     * @param info：设备对象
     */
    public void addDevList(DeviceInfo info) {
        DeviceInfo dev = getDev(info.getIp());
        if (dev == null) {
            mDevList.add(info);
        }
    }

    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-19 下午4:40:58
     * description: 关闭中断线程
     * @param 无
     */
    private void closeThread() {

        try {
            if (null != mr && mr.isAlive()) {
                mr.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mr = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {

        super.finalize();
    }


    public static void closeUdp() {
        if (instance != null) {
            isClose = true;
            instance.UdpClose();
            instance.closeThread();
            instance.mContext = null;

        }
    }
}
