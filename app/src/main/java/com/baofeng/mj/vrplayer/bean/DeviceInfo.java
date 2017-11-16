package com.baofeng.mj.vrplayer.bean;


public class DeviceInfo {

    private String id;

    private String name;
    private String ip;
    private int port;
    private String lastLoginDate;
    private boolean icheck;
    private boolean isLive = false;


    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean isLive) {
        this.isLive = isLive;
    }

    public boolean getIcheck() {
        return icheck;
    }

    public void setIcheck(boolean icheck) {
        this.icheck = icheck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", lastLoginDate='" + lastLoginDate + '\'' +
                ", icheck=" + icheck +
                ", isLive=" + isLive +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (!(other instanceof DeviceInfo))
            return false;
        final DeviceInfo deviceInfo = (DeviceInfo) other;
        if (!getId().equals(deviceInfo.getId()))
            return false;
        return true;
    }
}
