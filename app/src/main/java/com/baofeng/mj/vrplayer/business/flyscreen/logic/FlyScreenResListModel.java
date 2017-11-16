package com.baofeng.mj.vrplayer.business.flyscreen.logic;

import com.baofeng.mj.vrplayer.bean.BaseMessage;
import com.baofeng.mj.vrplayer.bean.Resource;
import com.baofeng.mj.vrplayer.business.flyscreen.util.FlyScreenUtil;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static com.baofeng.mj.vrplayer.business.flyscreen.util.FlyScreenUtil.FlyResType.picture;

/**
 * 资源列表
 *
 * @author yanzw
 * @date 2014-7-4 下午12:05:58
 */
public class FlyScreenResListModel {

    private static final int rowNumPrePage = 1000;

    public FlyScreenResListModel() {
    }

    /**
     * 创建获取资源服务器端口请求数据包
     *
     * @return
     */
    public byte[] createServerPortRequest() {
        return RequestModel.createtServerPortRequestDataPacket();
    }

    /**
     * 创建特定目录分页请求数据
     *
     * @param resType 资源类型
     * @param dirPath 目录路径
     * @param start   起始位置
     * @return
     */
    public byte[] createtDirPageRequest(FlyScreenUtil.FlyResType resType, String dirPath, int start) {
        return RequestModel.createtDirPageRequestDataPacket(resType, dirPath, start);
    }

    /**
     * 创建目录下节点统计的请求
     *
     * @param resType
     * @param dirPath
     * @return
     */
    public byte[] createtDirCountRequest(FlyScreenUtil.FlyResType resType, String dirPath) {
        return RequestModel.createtDirCountRequestDataPacket(resType, dirPath);
    }

    /**
     * 处理接收数据
     *
     * @param resp
     */
    public void dealReceiveData(BaseMessage.MessageType msgType, ByteString resp) {
        FlyScreenUtil.FlyResType resType = null;
        switch (msgType) {
            case MessageType_Video:        //视频业务
                resType = FlyScreenUtil.FlyResType.video;
                break;
            case MessageType_Music:        //音乐业务
                resType = FlyScreenUtil.FlyResType.music;
                break;
            case MessageType_Game:        //游戏业务
                resType = FlyScreenUtil.FlyResType.game;
                break;
            case MessageType_Picture:    //图片业务
                resType = picture;
                break;
            default:
                return;
        }

        ReceiveModel.dealReceiveData(resType, resp);
    }

    /**
     * 处理请求数据模型
     *
     * @author yanzw
     * @date 2014-7-4 下午5:55:34
     */
    private static class RequestModel {

        /**
         * 创建资源服务器地�?��求数据包
         */
        public static byte[] createtServerPortRequestDataPacket() {
            //创建业务数据�?
            byte[] busMsg = createtServerPortRequestBusinessDataPacket();
            byte[] basicMsg = createtBasicResourceMessage(FlyScreenUtil.FlyResType.video, busMsg);

            //tag�?
            byte[] tag = FlyScreenUtil.getItagAppToPc_Message();
            //发�?总包
            byte[] totalMsg = new byte[8 + basicMsg.length];
            //消息实体总长
            byte[] msgLen = FlyScreenUtil.intToByteArray(basicMsg.length);

            System.arraycopy(tag, 0, totalMsg, 0, 4);
            System.arraycopy(msgLen, 0, totalMsg, 4, 4);
            System.arraycopy(basicMsg, 0, totalMsg, 8, basicMsg.length);

            return totalMsg;
        }

        /**
         * 创建请求资源服务器端口的数据�?
         *
         * @return
         */
        private static byte[] createtServerPortRequestBusinessDataPacket() {
            Resource.BasicResourceMessage.Builder baseBuilder = Resource.BasicResourceMessage.newBuilder();
            baseBuilder.setMt(Resource.ResourceMessageType.ResourceMessageType_RequestHttpServertPort);

            Resource.RequestServerPort.Builder detailBuilder = Resource.RequestServerPort.newBuilder();
            detailBuilder.setSessionID(FlyScreenLoginModel.getSessionId());

            Resource.RequestServerPort detailData = detailBuilder.build();

            byte[] detailMsgByteAry = detailData.toByteArray();
            if (detailMsgByteAry != null) {
                String detailMsgStr = "";
                try {
                    detailMsgStr = new String(detailMsgByteAry, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                baseBuilder.setDetailMsg(detailMsgStr);
            }

            Resource.BasicResourceMessage questMsg = baseBuilder.build();
            byte[] byteMsg = questMsg.toByteArray();
            return byteMsg;
        }

        /**
         * 创建目录分页数据请求数据�?
         *
         * @param resType
         * @param dirPath
         * @param start
         * @return
         */
        public static byte[] createtDirPageRequestDataPacket(FlyScreenUtil.FlyResType resType, String dirPath, int start) {
            //创建业务数据�?
            ByteString busMsg = createtDirPageRequestBusinessDataPacket(resType, dirPath, start);
            byte[] basicMsg = createtBasicResourceMessage2(resType, busMsg);
            //tag�?
            byte[] tag = FlyScreenUtil.getItagAppToPc_Message();
            //发�?总包
            byte[] totalMsg = new byte[8 + basicMsg.length];
            //消息实体总长
            byte[] msgLen = FlyScreenUtil.intToByteArray(basicMsg.length);

            System.arraycopy(tag, 0, totalMsg, 0, 4);
            System.arraycopy(msgLen, 0, totalMsg, 4, 4);
            System.arraycopy(basicMsg, 0, totalMsg, 8, basicMsg.length);

            return totalMsg;
        }

        /**
         * 创建请求资源统计的数据包
         *
         * @return
         */
        private static ByteString createtDirPageRequestBusinessDataPacket(FlyScreenUtil.FlyResType resType, String dirPath, int start) {
            Resource.BasicResourceMessage.Builder baseBuilder = Resource.BasicResourceMessage.newBuilder();
            baseBuilder.setMt(Resource.ResourceMessageType.ResourceMessageType_RequestDirPageData);

            Resource.RequestPageData.Builder detailBuilder = Resource.RequestPageData.newBuilder();
            detailBuilder.setSessionID(FlyScreenLoginModel.getSessionId());
            detailBuilder.setStartIndex(start);
            detailBuilder.setEndIndex(start + rowNumPrePage);
            detailBuilder.setUri(dirPath);
            Resource.RequestPageData pageData = detailBuilder.build();
            baseBuilder.setDetailMsgBytes(pageData.toByteString());

            Resource.BasicResourceMessage questMsg = baseBuilder.build();

            return questMsg.toByteString();
        }

        /**
         * 创建目录分页数据请求数据�?
         *
         * @param resType
         * @param dirPath
         * @return
         */
        public static byte[] createtDirCountRequestDataPacket(FlyScreenUtil.FlyResType resType, String dirPath) {
            //创建业务数据�?
            ByteString busMsg = createtDirCountRequestBusinessDataPacket(resType, dirPath);
            byte[] basicMsg = createtBasicResourceMessage2(resType, busMsg);
            //tag�?
            byte[] tag = FlyScreenUtil.getItagAppToPc_Message();
            //发�?总包
            byte[] totalMsg = new byte[8 + basicMsg.length];
            //消息实体总长
            byte[] msgLen = FlyScreenUtil.intToByteArray(basicMsg.length);

            System.arraycopy(tag, 0, totalMsg, 0, 4);
            System.arraycopy(msgLen, 0, totalMsg, 4, 4);
            System.arraycopy(basicMsg, 0, totalMsg, 8, basicMsg.length);

            return totalMsg;
        }

        /**
         * 创建请求资源统计的数据包
         *
         * @return
         */
        private static ByteString createtDirCountRequestBusinessDataPacket(FlyScreenUtil.FlyResType resType, String dirPath) {
            Resource.BasicResourceMessage.Builder baseBuilder = Resource.BasicResourceMessage.newBuilder();
            baseBuilder.setMt(Resource.ResourceMessageType.ResourceMessageType_RequestDirCount);

            Resource.RequestDirCount.Builder detailBuilder = Resource.RequestDirCount.newBuilder();
            detailBuilder.setSessionID(FlyScreenLoginModel.getSessionId());
            detailBuilder.setDirectory(dirPath);
            Resource.RequestDirCount dirCount = detailBuilder.build();
            baseBuilder.setDetailMsgBytes(dirCount.toByteString());

            Resource.BasicResourceMessage questMsg = baseBuilder.build();

            return questMsg.toByteString();
        }

        /**
         * 创建资源请求的�?�?
         *
         * @param resType
         * @return
         */
        private static byte[] createtBasicResourceMessage2(FlyScreenUtil.FlyResType resType, ByteString busMsg) {
            ByteString basicMsg = null;
            switch (resType) {
                case video:
                    basicMsg = FlyScreenBaseModel.createBasicMassage(BaseMessage.MessageType.MessageType_Video, busMsg);
                    break;
                case music:
                    basicMsg = FlyScreenBaseModel.createBasicMassage(BaseMessage.MessageType.MessageType_Music, busMsg);
                    break;
                case game:
                    basicMsg = FlyScreenBaseModel.createBasicMassage(BaseMessage.MessageType.MessageType_Game, busMsg);
                    break;
                case picture:
                    basicMsg = FlyScreenBaseModel.createBasicMassage(BaseMessage.MessageType.MessageType_Picture, busMsg);
                    break;
            }
            return basicMsg.toByteArray();
        }

        /**
         * 创建资源请求的�?�?
         *
         * @param resType
         * @return
         */
        private static byte[] createtBasicResourceMessage(FlyScreenUtil.FlyResType resType, byte[] busMsg) {
            byte[] basicMsg = null;
            switch (resType) {
                case video:
                    basicMsg = FlyScreenBaseModel.createBasicMassage(BaseMessage.MessageType.MessageType_Video, busMsg);
                    break;
                case music:
                    basicMsg = FlyScreenBaseModel.createBasicMassage(BaseMessage.MessageType.MessageType_Music, busMsg);
                    break;
                case game:
                    basicMsg = FlyScreenBaseModel.createBasicMassage(BaseMessage.MessageType.MessageType_Game, busMsg);
                    break;
                case picture:
                    basicMsg = FlyScreenBaseModel.createBasicMassage(BaseMessage.MessageType.MessageType_Picture, busMsg);
                    break;
            }
            return basicMsg;
        }
    }

    /**
     * 处理接收数据模型
     *
     * @author yanzw
     * @date 2014-7-4 下午5:56:00
     */
    private static class ReceiveModel {

        // 处理接收数据
        public static void dealReceiveData(FlyScreenUtil.FlyResType resType, ByteString resp) {
            try {

                Resource.BasicResourceMessage baseResourceMsg = Resource.BasicResourceMessage.parseFrom(resp);
                if (baseResourceMsg.getMt() == Resource.ResourceMessageType.ResourceMessageType_ResponseHttpServerPort) {
                } else if (baseResourceMsg.getMt() == Resource.ResourceMessageType.ResourceMessageType_ResponseDirCount) {
                    //FlyScreenInterface.updateResList(resType, baseResourceMsg);
                } else {
                    //FlyScreenInterface.updateResList(resType, baseResourceMsg);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }


}