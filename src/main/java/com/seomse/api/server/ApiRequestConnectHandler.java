
package com.seomse.api.server;

import com.seomse.api.ApiRequest;
/**
 * <pre>
 *  파 일 명 : ApiRequestConnectHandler.java
 *  설    명 : api 요청용 핸들러
 *            클라이언트가 서버의 api를 이용하는것이 아닌
 *            서버가 클라이언트의 api를 이용할떄 사용 한다.
 *            api 요청이 들어왔을때 전달받는 핸들러
 *
 *  작 성 자 : macle
 *  작 성 일 : 2018.04
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜섬세한사람들. All right reserved.
 */
public interface ApiRequestConnectHandler {
	/**
	 * 연결
	 * @param apiRequest
	 */
	void connect(ApiRequest apiRequest);
}
