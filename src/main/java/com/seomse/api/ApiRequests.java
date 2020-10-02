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

package com.seomse.api;
/**
 * api 요청 전역 메소드 모음
 *
 * @author macle
 */
public class ApiRequests {

    /**
     * 1회성 메시지 클래스
     * @param hostAddress String address
     * @param port int port
     * @param packageName String package name
     * @param code String code = run class name
     * @param message String request message
     * @return String response message
     */
    public static String sendToReceiveMessage(String hostAddress, int port, String packageName, String code, String message){

        ApiRequest apiRequest = new ApiRequest(hostAddress , port);
        if(packageName != null){
            apiRequest.setPackageName(packageName);
        }
        apiRequest.connect();

        String receiveMessage = apiRequest.sendToReceiveMessage(code,message);
        apiRequest.disConnect();
        return receiveMessage;

    }

}
