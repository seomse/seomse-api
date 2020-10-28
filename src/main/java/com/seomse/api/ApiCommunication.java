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
import com.seomse.commons.callback.ObjCallback;
import com.seomse.commons.handler.ExceptionHandler;
import com.seomse.commons.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
/**
 * api 통신
 *
 * @author macle
 */
public class ApiCommunication extends Thread{

	private static final Logger logger = LoggerFactory.getLogger(ApiCommunication.class);
	public static final char DEFAULT_PACKAGE = 'D';
	public static final char CUSTOM_PACKAGE = 'C';

	private final SendToReceive sendToReceive;
	private boolean flag = true;
	
	private final String defaultPackageName;
	
	private final long createTime;
	
	private ObjCallback endCallback = null;
	
	private ExceptionHandler exceptionHandler = null;

	private int maxLogLength = 150;


	/**
	 * 생성자
	 * @param defaultPackageName string default package name
	 * @param socket Socket
	 * @throws IOException IOException
	 */
	public ApiCommunication(String defaultPackageName, Socket socket) throws  IOException {
		sendToReceive = new SendToReceive(socket);
		createTime = System.currentTimeMillis();
		
		this.defaultPackageName = defaultPackageName;
	
	}
	
	/**
	 * 로그 최대문자 길이 설정
	 * @param maxLogLength int
	 */
	public void setMaxLogLength(int maxLogLength) {
		this.maxLogLength = maxLogLength;
	}



	/**
	 * 생성 time 얻기
	 * @return long time
	 */
	public long getCreateTime() {
		return createTime;
	}
	
	
	/**
	 * 종료 핸들러
	 * @param endCallback ObjCallback
	 */
	public void setEndCallback(ObjCallback endCallback) {
		this.endCallback = endCallback;
	}

	/**
	 * 예외 핸들러설정
	 * @param exceptionHandler ExceptionHandler
	 */
	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
	
	@Override
	public void run() {
		
		try {
			while(flag){
				
				String message  = sendToReceive.receive();
				if(message == null){
					flag = false;
					disConnect();
					break;
				}
				
				readMessage(message);
				
			}
		}catch(Exception e) {
			ExceptionUtil.exception(e, logger, exceptionHandler);
		}
		try{
			if(endCallback != null){
				endCallback.callback(this);
			}
		}catch(Exception e){
			ExceptionUtil.exception(e, logger, exceptionHandler);
		}
		
	}

	/**
	 * message read
	 * @param message String message
	 */
	private void readMessage(String message) {

		if(message.length() > maxLogLength){
			logger.debug("readMessage: " + message.substring(0 , maxLogLength) + ".. +" + message.length() + "characters.");
		} else {
			logger.debug("readMessage: " + message);
		}


		char packageType = message.charAt(0);
		message = message.substring(1);
		
		String className ;
		String messageCode ;
		
		if(packageType == DEFAULT_PACKAGE){
			
			int idx = message.indexOf(",");
			messageCode = message.substring(0, idx);
			className =  defaultPackageName +"."+messageCode;
			message = message.substring(idx+1);
		}else{
			
			int idx = message.indexOf(",");
			String packageName = message.substring(0, idx);
			
			int next = idx+1;
			
			idx = message.indexOf(",", next);
			messageCode = message.substring(next, idx);
			className =  packageName +"."+messageCode;
			message = message.substring(idx+1);
		}	
			
		try {
			Class<?> apiMessageClass = Class.forName(className);
			ApiMessage apiMessage = (ApiMessage)apiMessageClass.newInstance();
			apiMessage.setCommunication(this);
			apiMessage.receive(message);
		} catch (Exception e1) {
			sendMessage(ExceptionUtil.getStackTrace(e1));
			ExceptionUtil.exception(e1, logger, exceptionHandler);

		}

	}
	
	/**
	 * send message
	 * null 이나 빈값이 들어오면 전달하지 않는다.
	 * @param message String sendMessage
	 * @return boolean message send success fail
	 */
	public boolean sendMessage(String message){
		if(message == null){
			return false;
		}

		if(!sendToReceive.isConnect()){
			logger.error("message send Fail(Not Connected) : " + message);
			return false;
		}

		logger.debug(getSendMessageLog(message, maxLogLength));
		return sendToReceive.send(message);
	}

	/**
	 * send message log 얻기
	 * @param message String sendMessage
	 * @param maxLogLength int maxLogLength
	 * @return String SendMessageLog
	 */
	public static String getSendMessageLog(String message, int maxLogLength){
		String log ;
		if(message.length() > maxLogLength){
			log = "sendMessage: " + message.substring(0 , maxLogLength) + ".. +" + message.length() + "characters.";
		}else{
			log = message;
		}

		return log;
	}

	/**
	 * connect check
	 * @return boolean connect check flag
	 */
	public boolean isConnect(){
		return sendToReceive.isConnect();
	}

	/**
	 * 연결종료
	 */
	public void disConnect() {
		flag = false;
		sendToReceive.disConnect();
	}

	/**
	 * 마지막 연결 시간 얻기
	 * @return long (time)
	 */
	public long getLastConnectTime() {
		return sendToReceive.getLastConnectTime();
	}

}