package com.seomse.api;

/**
 * <pre>
 *  파 일 명 : SocketAddress.java
 *  설    명 : 소켓 주소 정보 객체
 *  작 성 자 : monds
 *  작 성 일 : 2019.06
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class SocketAddress {

    private String hostAddress;
    private int port;

    public SocketAddress(String hostAddress, int port) {
        this.hostAddress = hostAddress;
        this.port = port;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
