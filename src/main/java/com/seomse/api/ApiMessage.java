


package com.seomse.api;

/**
 * <pre>
 *  파 일 명 : ApiCommunication.java
 *  설    명 : API메시지 추상체
 *
 *  작 성 자 : macle
 *  작 성 일 : 2018.04
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜섬세한사람들. All right reserved.
 */

public abstract class ApiMessage {
	
	protected ApiCommunication communication;
	
	/**
	 * ApiCommunication 설정
	 * @param apiCommunication ApiCommunication
	 */
	public void setCommunication(ApiCommunication apiCommunication){
		this.communication = apiCommunication;
	}
		
	/**
	 * 메시지 전달
	 * @param message sendMessage
	 */
	public void sendMessage(String message) {
		communication.sendMessage(message);
	}
	
	/**
	 * 메시지 받기
	 * @param message receiveMessage
	 */
	public abstract void receive(String message);
	
	
	
}