/*
 * Copyright (C) 2020 Seomse Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seomse.api.communication;
/**
 * host address and port
 * connect default info
 * @author macle
 */
public class HostAddrPort {
    private String hostAddress;
    private int port;


    /**
     * 생성자
     */
    public HostAddrPort(){

    }

    /**
     * 생성자
     * @param hostAddress string host address
     * @param port int port
     */
    public HostAddrPort(String hostAddress, int port){
        this.hostAddress = hostAddress;
        this.port = port;
    }

    /**
     * @return host address
     */
    public String getHostAddress() {
        return hostAddress;
    }

    /**
     * host address setting
     * @param hostAddress string
     */
    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    /**
     * @return int port number
     */
    public int getPort() {
        return port;
    }

    /**
     * port setting
     * @param port int
     */
    public void setPort(int port) {
        this.port = port;
    }
}
