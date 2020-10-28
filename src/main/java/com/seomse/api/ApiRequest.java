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

import com.seomse.api.communication.SendToReceive;
import com.seomse.commons.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
/**
 * api 요청 클라이언트
 *
 * @author macle
 */
public class ApiRequest {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiRequest.class);
	
	public static final String CONNECT_FAIL = "CONNECT_FAIL";
	
	public static final String TIME_OVER = "TIME_OVER";
	
	private final SendToReceive sendToReceive;
	
	private String host;
	private int port;
	
	private Long waitingTime =null;
	
	private int connectTimeOut = Config.getInteger("api.connect.time.out", 30000);
	
	private String packageName = null;  
	
	private int maxLogLength = 150;

	/**
	 * 생성자 
	 * @param host String 서버 아이피 주소, 또는 도메인 주소
	 * @param port int 서버포트
	 */
	public ApiRequest(String host, int  port) {
		this.host = host;
		this.port = port;
		
		sendToReceive = new SendToReceive();

	}
	
	/**
	 * 생성자
	 * @param socket Socket
	 */
	public ApiRequest(Socket socket) throws  IOException{
		sendToReceive = new SendToReceive(socket);
	}
	
	

	/**
	 * 연결 오류 여부 설정
	 * false 이면 연결오류를 표시하지 않음
	 * ping 테스트에 에러를 표시하고 싶지 않을 경우 사용
	 * @param isConnectErrorLog boolean isConnectErrorLog
	 */
	public void setConnectErrorLog(boolean isConnectErrorLog) {
		sendToReceive.setConnectErrorLog(isConnectErrorLog);
	}
	
	/**
	 * 연결정보 다시설정
	 * @param host String
	 * @param port int 포트번호
	 */
	public void setConnect(String host, int port){
		if(!this.host.equals(host) || this.port != port ){
			sendToReceive.disConnect();
			this.host = host;
			this.port = port;
		}
	}
	
	/**
	 * 로그 최대문자 길이 설정
	 * @param maxLogLength String MaxLogLength
	 */
	public void setMaxLogLength(int maxLogLength) {
		this.maxLogLength = maxLogLength;
	}
	
	/**
	 * 패키지명 설정
	 * @param packageName String
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * 접속 대기시간 설정
	 * @param connectTimeOut Integer
	 */
	public void setConnectTimeOut(int connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
	}

	/**
	 * 대기시간을 설정한다.
	 * @param time Long
	 */
	public void setWaitingTime(Long time){
		waitingTime = time;
	}
	
	/**
	 * 연결
	 * @return 연결 성공 여부
	 */
	public boolean connect(){
		sendToReceive.setConnectTimeOut(connectTimeOut);
		return sendToReceive.connect(host, port);
	}
	
	private boolean isSendMessage = false;
	private boolean isWaitingTimeOver =false;
	/**
	 * 메시지를 요청하고 전달받은 메시지를 돌려준다
	 * 전달받아야할 메시지가 있을때사용
	 * @param code String code
	 * @param sendMessage String sendMessage
	 * @return String ReceiveMessage
	 */
	public String sendToReceiveMessage(String code, String sendMessage){
		isWaitingTimeOver = false;
		isSendMessage= false;
		
		if(!sendToReceive.isConnect()){
			return CONNECT_FAIL;
		}
		
		if(sendMessage == null) {
			sendMessage = "";
		}

		logger.debug(ApiCommunication.getSendMessageLog(sendMessage, maxLogLength));

		if(packageName == null){
			sendToReceive.send(ApiCommunication.DEFAULT_PACKAGE +code +"," + sendMessage);
		}else{
			sendToReceive.send(ApiCommunication.CUSTOM_PACKAGE + packageName+ "," + code + "," + sendMessage);
					
		}

		if(waitingTime != null){
			new Thread(() -> {
				try{
					Thread.sleep(waitingTime);
				}catch(Exception e){
					return;
				}
				if(!isSendMessage){
					isWaitingTimeOver = true;
					logger.error("waitingTimeOut disconnect");
					disConnect();
				}
			}).start();
		}
		
		
		
		String receiveMessage = sendToReceive.receive() ;
		
		if(receiveMessage != null){
			if(receiveMessage.length() > 100){
				logger.debug("receiveMessage: " + receiveMessage.substring(0 , 100) + ".. +" + receiveMessage.length() + "characters.");
			} else {
				logger.debug("receiveMessage: " + receiveMessage);
			}
				
		}
		isSendMessage = true;
		if(isWaitingTimeOver){
			receiveMessage = TIME_OVER;
		}
		
		if(receiveMessage == null)
			receiveMessage = CONNECT_FAIL;
		return receiveMessage;
	}
	
	/**
	 * 메시지를 전달한다
	 * 전달받아야할 메시지가 없을때에만 사용해야 한다.
	 * @param code code
	 * @param sendMessage sendMessage
	 */
	public void sendMessage(String code, String sendMessage){
		if(packageName == null){
			sendToReceive.send(ApiCommunication.DEFAULT_PACKAGE +code +"," + sendMessage);
		}else{
			sendToReceive.send(ApiCommunication.CUSTOM_PACKAGE + packageName+ "," + code + "," + sendMessage);
					
		}
		
	}
	
	/**
	 * 연결해제
	 */
	public void disConnect(){
		sendToReceive.disConnect();
	}

	/**
	 * 연결여부
	 * @return boolean isConnect
	 */
	public boolean isConnect(){
		return sendToReceive.isConnect();
	}
	
	/**
	 * socket 얻기
	 * @return Socket
	 */
	public Socket getSocket() {
		return sendToReceive.getSocket();
	}
}