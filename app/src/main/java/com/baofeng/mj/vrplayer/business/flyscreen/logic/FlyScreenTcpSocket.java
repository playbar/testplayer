package com.baofeng.mj.vrplayer.business.flyscreen.logic;

import android.content.Context;
import android.os.Message;

import com.baofeng.mj.vrplayer.bean.BaseMessage;
import com.baofeng.mj.vrplayer.bean.DeviceInfo;
import com.baofeng.mj.vrplayer.business.flyscreen.FlyScreenBusiness;
import com.baofeng.mj.vrplayer.business.flyscreen.util.FlyScreenUtil;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: FlyScreenTcpSocket
 *
 * @author zhangxin
 * @date: 2015-1-16 上午11:26:39
 * description:飞屏设备与pc的tcp逻辑
 */
public class FlyScreenTcpSocket {
    private static FlyScreenTcpSocket instance;

    // 所有udp、tcp使用线程池执行
    private ExecutorService threadPoolService = Executors.newCachedThreadPool();

    private String mCurrHost;
    private int mCurrPort;
    private DeviceInfo mCurrDevInfo;

    //线程池
    private ArrayList<HashMap<String, Object>> mSocketThread = new ArrayList<HashMap<String, Object>>();

    private HandlerCallBack devListHandler;
    private ScheduledExecutorService scheduExec;
    private resourceDealRecieve receiveData = null;

    public enum ActivityType {
        devlist_reshome, reslist, gamelist, fckeyset
    }

    private ActivityType currActivityType;
    private TcpConnCallback mCallObject;

    private FlyScreenTcpSocket() {
    }


    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-16 上午11:27:22
     * description:该类单例
     * @param 无
     */
    public static FlyScreenTcpSocket getSingleInstance() {
        if (instance == null) {
            instance = new FlyScreenTcpSocket();
        }
        return instance;
    }

    public static FlyScreenTcpSocket getFlyScreenTcpSocket() {
        return instance;
    }

    /**
     *
     * */
    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-16 上午11:28:00
     * description: tcp连接对外接口
     * @param ip:pc地址；port:pc端口号
     */
    public void tcpConnect(Context mContext, String ip, int port) {
        mCurrHost = ip;
        mCurrPort = port;

        HashMap<String, Object> hsMap = getSocketAndThread(ip);
        if (hsMap == null) {
            Thread th = createThread(mContext, ip, port, mCallObject);
            threadPoolService.execute(th);
        }
    }

    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-16 上午11:28:48
     * description:tcp连接对外接口,支持回调
     * @param ip:pc地址；port:pc端口号; callObj:回调对象
     */
    public void tcpConnectAndCallback(Context mContext, String ip, int port,
                                      TcpConnCallback callObj) {
        mCurrHost = ip;
        mCurrPort = port;

        HashMap<String, Object> hsMap = getSocketAndThread(ip);
        if (hsMap == null) {
            Thread th = createThread(mContext, ip, port, callObj);
            threadPoolService.execute(th);
        } else {
            if (callObj != null) {
                callObj.onTcpConnSuccess(mCurrDevInfo);
            }
        }
    }


//	public void tcpConnect_devScan(String ip, int port, TcpConnCallback callback) {
//		mCurrHost = ip;
//		mCurrPort = port;
//
//		close();
//		Thread th = createThread(ip, port, callback);
//		threadPoolService.execute(th);
//	}

//	private void createSocketAddArray(HashMap<String, Object> hsMap, String ip,
//			int port) {
//		Socket hsSocket = (Socket) hsMap.get("socket");
//		OutputStream hsOutputStream = (OutputStream) hsMap.get("output");
//		InputStream hsInputStream = (InputStream) hsMap.get("input");
//
//		closeSocket(hsSocket);
//		closeStream(hsOutputStream, hsInputStream);
//
//		try {
//			InetAddress hostIP = InetAddress.getByName(ip);
//			Socket tSocket = new Socket(hostIP, port);
//			OutputStream tOutputStream = tSocket.getOutputStream();
//			InputStream tInputStream = tSocket.getInputStream();
//
//			hsMap.put("output", tOutputStream);
//			hsMap.put("input", tInputStream);
//			hsMap.put("socket", tSocket);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

    /**
     * @return 返回创建的线程对象
     * @author zhangxin  @Date 2015-1-16 上午11:33:14
     * description: 创建设备接收线程，并加入线程列表
     * @param ip:pc地址；port:pc端口号; callObj:回调对象
     */
    private Thread createThread(Context mContext, String ip, int port, TcpConnCallback callObject) {
        HashMap<String, Object> hsMap = new HashMap<String, Object>();
        hsMap.put("ip", ip);

        Thread th = new TcpConnAndReceiveThread(mContext, ip, port, callObject);
        hsMap.put("thread", th);

        mSocketThread.add(hsMap);
        return th;
    }

    /**
     * @return 线程hashmap
     * @author zhangxin  @Date 2015-1-16 上午11:35:07
     * description:从线程列表中根据参数ip 取得线程hashmap
     * @param pip:ip地址
     */
    private HashMap<String, Object> getSocketAndThread(String pip) {
        for (int i = 0; i < mSocketThread.size(); i++) {
            try {
                HashMap<String, Object> hsMap = mSocketThread.get(i);
                if (hsMap == null) {
                    continue;
                }
                String ip = (String) hsMap.get("ip");
                if (ip == null) {
                    continue;
                }
                if (ip.equalsIgnoreCase(pip)) {
                    return hsMap;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    /**
     * @return
     * @author zhangxin  @Date 2015-1-16 上午11:36:10
     * description:
     * @param 
     */
    private void delAllSocketAndThead() {
        mSocketThread.clear();
    }

    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-16 上午11:36:42
     * description:所有向服务器发送消息公共方法
     * @param ip:发送目标pc的ip地址； msg：发送的消息
     */
    public void send(Context mContext, String ip, byte[] msg) {
        Thread ts = new TcpSendThread(mContext, ip, msg);
        threadPoolService.execute(ts);
    }

    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-16 上午11:36:42
     * description:所有向当前连接pc服务器发送消息公共方法
     * @param msg：发送的消息
     */
    public void send(Context mContext, byte[] msg) {

        Thread ts = new TcpSendThread(mContext, mCurrHost, msg);
        threadPoolService.execute(ts);
    }

    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-16 上午11:38:18
     * description:每10秒发送心跳包 总包 只执行一次，用于pc端校验socket连接
     * @param 无
     */
    public void heartBeat(final Context mContext) {

        if (scheduExec == null) {
            scheduExec = Executors.newSingleThreadScheduledExecutor();

            Runnable task = new Runnable() {
                int times = 0;
                public void run() {
                    BaseMessage.BasicMessage.Builder builder = BaseMessage.BasicMessage
                            .newBuilder();
                    builder.setMt(BaseMessage.MessageType.MessageType_HeartBeat);

                    String sessid = FlyScreenLoginModel.getSessionId();
                    if (!sessid.equals("")) {
                        builder.setDetailMsg(FlyScreenLoginModel.getSessionId());
                    }

                    BaseMessage.BasicMessage message = builder.build();
                    byte[] bmsg = message.toByteArray();

                    byte[] btag = FlyScreenUtil.getItagAppToPc_Message();
                    byte[] blen = FlyScreenUtil.intToByteArray(bmsg.length);
                    byte[] bdst = new byte[8 + bmsg.length];

                    System.arraycopy(btag, 0, bdst, 0, 4);
                    System.arraycopy(blen, 0, bdst, 4, 4);
                    System.arraycopy(bmsg, 0, bdst, 8, bmsg.length);
                    send(mContext, mCurrHost, bdst);
                    times++;
                }
            };

            scheduExec.scheduleWithFixedDelay(task, 1, 10, TimeUnit.SECONDS);
        }

    }

    /**
     * ClassName: TcpSendThread
     *
     * @author zhangxin
     * @date: 2015-1-16 上午11:39:48
     * description: tcp发送消息线程
     */
    class TcpSendThread extends Thread {
        private byte[] sendMsg;
        private String mIp;
        private Socket tSocket;
        private OutputStream tOutputStream;
        private Context mContext;

        public TcpSendThread(Context context, String ip, byte[] msg) {
            mContext = context;
            sendMsg = msg;
            mIp = ip;
        }

        @Override
        public void run() {
            getSocketByIp();
            if (tSocket == null) {
                reconn(mContext);
                getSocketByIp();
                if (tSocket == null) {
                    return;
                }
            }

            synchronized (tSocket) {
                send();
            }
        }

        /**
         * @return 无
         * @author zhangxin  @Date 2015-1-16 上午11:40:24
         * description:根据创建线程时的ip取得socket
         * @param 无
         */
        private void getSocketByIp() {
            HashMap<String, Object> hsMap = getSocketAndThread(mIp);
            if (hsMap == null)
                return;

            tSocket = (Socket) hsMap.get("socket");
            tOutputStream = (OutputStream) hsMap.get("output");
        }

        /**
         * @return 无
         * @author zhangxin  @Date 2015-1-16 上午11:40:24
         * description: 发送消息给pc端
         * @param 无
         */
        public void send() {

            try {
                if (!isTcpConnect(mIp)) {
                    reconn(mContext);
                    getSocketByIp();
                    if (tSocket == null)
                        return;
                }

                if (tSocket.isOutputShutdown()) {
                    tOutputStream = tSocket.getOutputStream();
                }

                tOutputStream.write(sendMsg);
                tOutputStream.flush();

            } catch (SocketException sk) {
                reconn(mContext);
                sk.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * ClassName: TcpConnAndReceiveThread
     *
     * @author zhangxin
     * @date: 2015-1-16 上午11:41:50
     * description:接收tcp线程
     */
    class TcpConnAndReceiveThread extends Thread {
        private byte[] bufferData;
        private String mIp;
        private int mPort;
        private Socket tSocket;
        private OutputStream tOutputStream;
        private InputStream tInputStream;
        //private TcpConnCallback mCallObject;
        private Context mContext;

        private TcpConnAndReceiveThread(Context context, String pip, int pport,
                                        TcpConnCallback callObject) {
            mContext = context;
            mIp = pip;
            mPort = pport;
            mCallObject = callObject;
        }

        @Override
        public void run() {
            try {
                createTcpConnect();
                if (mCallObject != null) {
                    mCallObject.onTcpConnSuccess(mCurrDevInfo);
                }

                checkTcpCommunicateOrControlMessage(mContext);
            } catch (Exception e) {
                e.printStackTrace();
                sendException();
            }

        }


        private void sendException(){
            if (mCallObject != null) {
                delAllSocketAndThead();
                if (mCallObject != null) {
                    mCallObject.onTcpConnError();
                }
            }
            if(null != devListHandler){
                Message msg = Message.obtain();
                msg.what = FlyScreenBusiness.EXCEPTION;
                devListHandler.handlerMessage(msg);
            }

        }

        /**
         * @return 无
         * @author zhangxin  @Date 2015-1-16 上午11:42:48
         * description:取出或者创建socket
         * @param 无
         */
        private void createTcpConnect() throws Exception {
            HashMap<String, Object> hsMap = getSocketAndThread(mIp);

//			try {
            Socket sok = (Socket) hsMap.get("socket");
            if (sok == null) {
                InetAddress hostIP = InetAddress.getByName(mIp);
                //tSocket = new Socket(hostIP, mPort);
                tSocket = new Socket();
                SocketAddress socAddress = new InetSocketAddress(hostIP, mPort);
                tSocket.connect(socAddress, 5000);
                tOutputStream = tSocket.getOutputStream();
                tInputStream = tSocket.getInputStream();

                hsMap.put("output", tOutputStream);
                hsMap.put("input", tInputStream);
                hsMap.put("socket", tSocket);
            } else {
                tSocket = (Socket) hsMap.get("socket");

                tOutputStream = (OutputStream) hsMap.get("output");
                tInputStream = (InputStream) hsMap.get("input");
            }

//			} catch (Exception e) {
//				//e.printStackTrace();
//				
//			}
        }

        /**
         * @return 无
         * @author zhangxin  @Date 2015-1-16 上午11:43:23
         * description:检测是通信验证还是控制层message
         * @param 无
         */
        private void checkTcpCommunicateOrControlMessage(Context mContext) {

            byte[] bufferLen = new byte[4];
            byte[] bufferTag = new byte[4];
            byte[] bufferHead = new byte[8];
            int tag = 0;

            try {

                FlyScreenUdpSocket udpSocket = FlyScreenUdpSocket
                        .getSingleInstance(null);

                if (tSocket == null)
                    return;

                if (tSocket.isInputShutdown()) {
                    tInputStream = tSocket.getInputStream();
                }

                byte[] oneBufferHead = new byte[8];
                int readOneHead;
                while ((readOneHead = tInputStream.read(oneBufferHead, 0, 8)) != -1) {
                    System.arraycopy(oneBufferHead, 0, bufferHead, 0,
                            readOneHead);
                    int hasReadHead = readOneHead;
                    int leftReadHead = 8 - hasReadHead;

                    while (hasReadHead != 8) {
                        readOneHead = tInputStream.read(oneBufferHead, 0,
                                leftReadHead);
                        System.arraycopy(oneBufferHead, 0, bufferHead,
                                hasReadHead, readOneHead);
                        hasReadHead += readOneHead;
                        leftReadHead = 8 - hasReadHead;
                    }

                    // 数据tag
                    System.arraycopy(bufferHead, 0, bufferTag, 0, 4);

                    // 数据length
                    System.arraycopy(bufferHead, 4, bufferLen, 0, 4);
                    int len = FlyScreenUtil.byteArrayToInt(bufferLen);

                    // + hasReadHead + " bytes.");

                    // 数据protobuf
                    bufferData = new byte[len];
                    byte[] oneBuffer = new byte[len];
                    int hasReadBody = 0;
                    int leftReadBody = len - hasReadBody;

                    while (hasReadBody != len) {
                        int readOneBody = tInputStream.read(oneBuffer, 0,
                                leftReadBody);
                        System.arraycopy(oneBuffer, 0, bufferData, hasReadBody,
                                readOneBody);
                        hasReadBody += readOneBody;
                        leftReadBody = len - hasReadBody;
                    }

                    tag = FlyScreenUtil.byteArrayToInt(bufferTag);

                    if (tag == FlyScreenUtil.getPcToAppItagInt_Inner()) // tcp设备回包验证
                    {
                        String ipsString = tSocket.getInetAddress()
                                .getHostAddress();
                        udpSocket.checkTcpDevice_Receive(bufferData, ipsString);
                    } else if (tag == FlyScreenUtil.getItagPcToAppInt_Message()) // tcp
                    // message业务回包
                    {
                        dealReceiveData(mContext);
                    }
                }

            } catch (SocketException sk) {
                sk.printStackTrace();
                sendException();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                sendException();
            }

        }

        /**
         * @return 无
         * @author zhangxin  @Date 2015-1-16 上午11:44:08
         * description:根据总包分类分发处理
         * @param 无
         */
        private void dealReceiveData(Context mContext) {
            BaseMessage.BasicMessage basicMessage = null;
            try {
                basicMessage = BaseMessage.BasicMessage.parseFrom(bufferData);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }

            BaseMessage.MessageType mt = basicMessage.getMt();
            String s = basicMessage.getDetailMsg();
            ByteString byteStr = basicMessage.getDetailMsgBytes();

            switch (mt) {
                case MessageType_Login:
                    FlyScreenLoginModel loginModel = FlyScreenLoginModel
                            .getSingleInstance(null);
                    loginModel.dealReceiveLoginModelData(mContext, byteStr);
                    break;
                case MessageType_Stat: // 数据统计
                    //FlyScreenResCountModel statModel = new FlyScreenResCountModel();
                    //statModel.dealReceiveData(byteStr);
                    break;
                case MessageType_Video: // 视频业务
                    if (receiveData != null) {
                        receiveData.handleVideoData(mt, byteStr);
                    } else {
                        FlyScreenResListModel resListModel = new FlyScreenResListModel();
                        resListModel.dealReceiveData(mt, byteStr);
                    }
                    break;

                default:
                    break;
            }
        }

    }

    /**
     * @return 验证结果
     * @author zhangxin  @Date 2015-1-16 上午11:44:48
     * description: 验证tcp连接是否正常
     * @param 待验证ip
     */
    public boolean isTcpConnect(String ip) {
        HashMap<String, Object> hsMap = getSocketAndThread(ip);
        if (hsMap == null)
            return false;

        Socket pSocket = (Socket) hsMap.get("socket");
        return pSocket != null && pSocket.isConnected();
    }

    public void setDevListHandler(HandlerCallBack h) {
        this.devListHandler = h;
    }

    public HandlerCallBack getDevListHandler() {
        return this.devListHandler;
    }

    public ExecutorService getThreadPoolService() {
        return threadPoolService;
    }

    public String getmCurrHost() {
        return mCurrHost;
    }

    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-16 上午11:44:48
     * description: 关闭流
     * @param tOutputStream，tinInputStream：待关闭流
     */
    private void closeStream(OutputStream tOutputStream,
                             InputStream tinInputStream) {
        if (tOutputStream != null) {
            try {
                tOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                tOutputStream = null;
            }
        }

        if (tinInputStream != null) {
            try {
                tinInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                tinInputStream = null;
            }
        }
    }

    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-16 上午11:44:48
     * description: 关闭线程池中所有流和socket，并中断线程
     * @param 无
     */
    public void close() {
        try {
            for (int i = 0; i < mSocketThread.size(); i++) {
                HashMap<String, Object> hsMap = mSocketThread.get(i);

                Thread th = (Thread) hsMap.get("thread");
                Socket tSocket = (Socket) hsMap.get("socket");

                OutputStream tOutputStream = (OutputStream) hsMap.get("output");
                InputStream tInputStream = (InputStream) hsMap.get("input");

                closeSocket(tSocket);
                closeStream(tOutputStream, tInputStream);
                closeThread(th);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        delAllSocketAndThead();
        // if (scheduExec != null)
        // {
        // scheduExec.shutdown();
        // scheduExec = null;
        // }
    }

    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-16 上午11:44:48
     * description: 关闭socket
     * @param tSocket：待关闭socket
     */
    private void closeSocket(Socket tSocket) {
        if (tSocket != null) {
            try {
                tSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                tSocket = null;
            }
        }
    }

    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-16 上午11:44:48
     * description: 中断线程
     * @param tSocket：待中断线程
     */
    private void closeThread(Thread th) {
        try {
            if (null != th && th.isAlive()) {
                th.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            th = null;
        }

    }

    /**
     * @return 无
     * @author zhangxin  @Date 2015-1-16 上午11:44:48
     * description: 重连当前设备
     * @param 无
     */
    public void reconn(Context mContext) {
        close();
        tcpConnect(mContext, mCurrHost, mCurrPort);
    }

    public DeviceInfo getmCurrDevInfo() {
        return mCurrDevInfo;
    }

    public void setmCurrDevInfo(DeviceInfo mCurrDevInfo) {
        this.mCurrDevInfo = mCurrDevInfo;
    }

    public void setCurrActivityType(ActivityType currActivityType) {
        this.currActivityType = currActivityType;
    }

    public ActivityType getCurrActivityType() {
        return currActivityType;
    }

    public void setmCurrHost(String mCurrHost) {
        this.mCurrHost = mCurrHost;
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }


    public interface HandlerCallBack {
        void handlerMessage(Message msg);

    }
    public HandlerCallBack mHandlerCallBack;


    public void setHandlerCallBack(HandlerCallBack mHandlerCallBack) {
        this.mHandlerCallBack = mHandlerCallBack;
    }

    public interface resourceDealRecieve {
        void handleVideoData(BaseMessage.MessageType messageType, ByteString bytes);
    }

    public void registerVideoReceiver(resourceDealRecieve resourceDealRecieve) {
        this.receiveData = resourceDealRecieve;
    }

    public void unRegistrs() {
        this.receiveData = null;
    }
    public  static  void closeClient(){
        if (instance!=null){
            instance.close();
        }
    }


}