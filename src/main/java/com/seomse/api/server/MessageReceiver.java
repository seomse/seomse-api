


package com.seomse.api.server;
/**
 * <pre>
 *  파 일 명 : MessageReceiver.java
 *  설    명 : 패키지검색
 *
 *  작 성 자 : macle
 *  작 성 일 : 2018.04
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜섬세한사람들. All right reserved.
 */
public interface MessageReceiver {
	

	/**
	 * 메시지 받기
	 * @param message
	 */
	void receive(String message);
	
	/**
	 * 메시지받기 종료
	 */
	void end();
}
