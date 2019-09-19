package com.seomse.api;
/**
 * <pre>
 *  파 일 명 : ApiRequests.java
 *  설    명 : API 요청
 *              ApiRequest를 활용한 1회성 메소드
 *  작 성 자 : macle
 *  작 성 일 : 2019.09.19
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class ApiRequests {

    /**
     * 1회성 메시지 클래스
     * @param hostAddress
     * @param port
     * @param packageName
     * @param code
     * @return
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
