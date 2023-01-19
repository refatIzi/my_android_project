package com.example.testedit.connect;

public class Connect {
    private String ipAddress;
    private String userName;
    private String password;
    private String port;
    private Protocol protocol;
    private String projectDirHost;

    public Connect() {
        this.ipAddress = "";
        this.userName = "";
        this.password = "";
        this.port = "";
        /**Default protocol the SSH*/
        this.protocol = Protocol.SSH;
        this.projectDirHost = "/";
    }

    public Connect(String ipAddress, String userName, String password, String port, Protocol protocol, String projectDirHost) {
        this.ipAddress = ipAddress;
        this.userName = userName;
        this.password = password;
        this.port = port;
        this.protocol = protocol;
        this.projectDirHost = projectDirHost;
    }

    public void setIPAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public void setProjectDirHost(String projectDirHost) {
        this.projectDirHost = projectDirHost;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getPort() {
        return port;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public String getProjectDirHost() {
        return projectDirHost;
    }

    public static Host newHost() {
        return new Connect().new Host();
    }

    public class Host {
        private Host() {
        }

        public Host ipAddress(String ipAddress) {
            Connect.this.ipAddress = ipAddress;
            return this;
        }

        public Host setUserName(String userName) {
            Connect.this.userName = userName;
            return this;
        }

        public Host setPassword(String password) {
            Connect.this.password = password;
            return this;
        }

        public Host setPort(String port) {
            Connect.this.port = port;
            return this;
        }

        public Host setProtocol(Protocol protocol) {
            Connect.this.protocol = protocol;
            return this;
        }

        public Host setProjectDirHost(String projectDirHost) {
            Connect.this.projectDirHost = projectDirHost;
            return this;
        }

        public Connect accept() {
            return Connect.this;
        }
    }

    @Override
    public String toString() {
        return ipAddress + ":" + userName + ":" + password + ":" + port + ":" + protocol;
    }

}
