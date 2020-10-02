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
 * api 메시지
 * 이클래스의 구현체로 api 이벤트를 작성
 *
 * @author macle
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
	 * send message
	 * @param message String sendMessage
	 */
	public void sendMessage(String message) {
		communication.sendMessage(message);
	}
	
	/**
	 * receive message
	 * @param message String receiveMessage
	 */
	public abstract void receive(String message);
	
	
	
}