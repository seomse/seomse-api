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
package com.seomse.api.communication;

import com.seomse.commons.handler.ExceptionHandler;
import com.seomse.commons.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * 메시지를 전송하고 받는 기본형
 * @author macle
 */
public class SendToReceive {
	
	private static final Logger logger = LoggerFactory.getLogger(SendToReceive.class);
	
	private static final char START =(char)0;
	private static final char END =(char)1;
	
	
	
	private boolean isConnectErrorLog = true;
	
	private ExceptionHandler exceptionHandler;
	
	
	private Socket socket;
	private OutputStreamWriter writer;
	private InputStreamReader reader;
	private boolean readMessageFlag ;
	private Integer connectTimeOut = null;
	
	/**
	 * 생성자
	 * @param socket 접속정보
	 */
	public SendToReceive(Socket socket) throws IOException{
	
		readMessageFlag = true;
		this.socket = socket;
			
		reader  = new InputStreamReader(socket.getInputStream(), CommunicationDefault.CHAR_SET);
		writer = new OutputStreamWriter(socket.getOutputStream(), CommunicationDefault.CHAR_SET);
	
	}
	
	
	

	/**
	 * 예외 핸들링설정
	 * @param exceptionHandler 예외 핸들러
	 */
	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}



	/**
	 * 연결에러로그 설정
	 * @param isConnectErrorLog 에러로그 츌력 여부
	 */
	public void setConnectErrorLog(boolean isConnectErrorLog){
		this.isConnectErrorLog = isConnectErrorLog;
	}
	
	
	/**
	 * 연결제한시간을 돌려준다.
	 * @return 연결제한시간
	 */
	public Integer getConnectTimeOut() {
		return connectTimeOut;
	}


	/**
	 * 연결 제한시간을 설정한다.
	 * @param connectTimeOut 연결제한시간
	 */
	public void setConnectTimeOut(Integer connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
	}


	/**
	 * 생성자
	 */
	public SendToReceive() {
		
	}
	
	
	/**
	 * 연결이끊어졌을경우 제연결한다.
	 */
	public boolean connect(String host, int port){
		if(socket == null || socket.isClosed()){


			readMessageFlag = true;
			try{
				
				if(connectTimeOut == null){
					socket = new Socket(host, port);
				}else{
					SocketAddress socketAddress = new InetSocketAddress(host, port);
					socket = new Socket();
					socket.setSoTimeout(connectTimeOut);
					socket.connect(socketAddress, connectTimeOut);
				}
				reader  = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
				writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
				

				return true;
			}catch(Exception e){
				if(isConnectErrorLog){
					logger.error(host +", " + port + " : connect fail");
					ExceptionUtil.exception(e, logger, exceptionHandler);
				}
				disConnect();
				return false;
			}
			
		}
		
		
		readMessageFlag = true;
		return true;
		
	}
	/**
	 * 연결중인지 체크한다.
	 * @return 연결여부
	 */
	public boolean isConnect(){
		if(!readMessageFlag){
			return false;
		}

		return socket != null && !socket.isClosed() && socket.isConnected();
	}
	
	/**
	 * 메시지를 돌려받는다
	 * @return 메시지
	 */
	public String receive(){
		int readData ;
		
		StringBuilder messageBuilder = new StringBuilder();
		while(readMessageFlag){
			try {
				
				try{
					readData = reader.read();
				}catch(java.net.SocketException se){
					readMessageFlag = false;
					return null;
				}
				
				
				if(readData == -1){
					readMessageFlag = false;
					return null;
				}
				
				char message = (char)readData;
				switch(message){
					case START: 
						messageBuilder.setLength(0);
						break;
					case END:
						return messageBuilder.toString();
						
					default:
						messageBuilder.append(message);
						break;
					
				}
				
				
			} catch (IOException e) {
				readMessageFlag = false;
				return null;
			}
		}
	
		return null;
	}
	
	
	/**
	 * 메시지전달
	 * null 이나 빈값이 들어오면 전달하지 않는다.
	 * @param message 메시지를 전달한
	 */
	public boolean send(String message){
		if(message == null || message.equals(""))
			return false;
		
		//특수기호 제거
		message = message.replace(START, ' ');
		message = message.replace(END, ' ');
		
		try {
			writer.write(START+message+END);
			writer.flush();
		} catch (IOException e) {
			return false;
		}
		
		return true;
	
	}
	
	/**
	 * 연결을 해제한다. 
	 */
	public void disConnect(){
		readMessageFlag = false;

		//noinspection CatchMayIgnoreException
		try {if(socket!=null) socket.close(); } catch(Exception e){}
		socket=null;
		//noinspection CatchMayIgnoreException
		try {if(writer!=null) writer.close(); } catch(Exception e){}
		writer=null;
		//noinspection CatchMayIgnoreException
		try {if(reader!=null) reader.close(); } catch(Exception e){}
		reader=null;

	}
	
	/**
	 * socket 얻기
	 * @return socket
	 */
	public Socket getSocket() {
		return socket;
	}
	
}
	