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

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seomse.commons.handler.ExceptionHandler;
import com.seomse.commons.utils.ExceptionUtil;
/**
 * 메시지 전달 통신 서버.
 * 메시지를 정해진 크기 만큼 받고
 * 메시지 종료 여부를 전달 받을 떄 활용
 *
 * 대량 메시지를 전달 받기 위해 개발됨
 * @author macle
 */
public class ReceiveServer extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(ReceiveServer.class);
	
	private ServerSocket serverSocket = null;
	private boolean isService = true;
	
	private boolean isEnd = false;
	
	private final int port;
	private InetAddress inetAddress = null;
	
	private int bufferSize = 10240;
	
	private ExceptionHandler exceptionHandler;
	

	/**
	 * 생성자
	 * @param port int
	 */
	public ReceiveServer(int port){
		this.port = port;
	}

	/**
	 * 예외 핸들러설정
	 * @param  exceptionHandler exceptionHandler
	 */
	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
	
	/**
	 * bufferSize 얻기
	 * @return int bufferSize
	 */
	public int getBufferSize() {
		return bufferSize;
	}


	/**
	 * bufferSize 설정 기본 10240
	 * @param bufferSize int
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * inetAddress 설정
	 * @param inetAddress InetAddress
	 */
	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}
	
	
	@Override
	public void run(){
	
		try{
			if(serverSocket == null){
				
				if(inetAddress == null){
					serverSocket = new ServerSocket(port);
				
				}else{
					serverSocket = new ServerSocket(port, 50, inetAddress );
					
				}
			}
			
			while(isService){
				
				try{
					Socket socket = serverSocket.accept();	
					ReceiveCommunication receiveCommunication = new ReceiveCommunication(socket, bufferSize);
					receiveCommunication.setExceptionHandler(exceptionHandler);
					receiveCommunication.start();
				}catch(Exception e){
					ExceptionUtil.exception(e, logger, exceptionHandler);
				}
			}
			
		}catch(Exception e){
			ExceptionUtil.exception(e, logger, exceptionHandler);
		}
		
		isEnd = true;
		
	}
	
	/**
	 * 서비스 종료 
	 */
	public void stopService(){
		isService = false;
		//noinspection CatchMayIgnoreException
		try{
			if(serverSocket != null){
				serverSocket.close();
				serverSocket = null;
			}
		}catch(Exception e){}
	}

	
	/**
	 * 종료 여부 얻기
	 * @return boolean isEnd
	 */
	public boolean isEnd() {
		return isEnd;
	}
}
