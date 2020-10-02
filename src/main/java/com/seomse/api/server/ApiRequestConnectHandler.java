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
package com.seomse.api.server;

import com.seomse.api.ApiRequest;


/**
 * api 요청용 핸들러
 * 클라이언트가 서버의 api를 이용하는것이 아닌
 * 서버가 클라이언트의 api를 이용할떄 사용 한다.
 * api 요청이 들어왔을때 전달받는 핸들러
 *  클라이언트가 서버에 나를 컨트롤 해달라고 하는 경우에 사용
 *
 * @author macle
 */
public interface ApiRequestConnectHandler {
	/**
	 * 연결
	 * @param apiRequest ApiRequest
	 */
	void connect(ApiRequest apiRequest);
}
