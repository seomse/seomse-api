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
/**
 * 메시지 전달 받기.
 * 메시지를 정해진 크기 만큼 받고
 * 메시지 종료 여부를 전달 받을 떄 활용
 *
 * 대량 메시지를 전달 받기 위해 개발됨
 * @author macle
 */
public interface MessageReceiver {
	

	/**
	 * 메시지 받기
	 * @param message String receive message
	 */
	void receive(String message);
	
	/**
	 * 메시지받기 종료
	 */
	void end();
}
