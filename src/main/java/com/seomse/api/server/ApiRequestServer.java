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

import com.seomse.api.ApiRequest;
import com.seomse.commons.handler.ExceptionHandler;
import com.seomse.commons.utils.ExceptionUtil;
/**
 * api 요청용 서버
 * 클라이언트가 서버의 api를 이용하는것이 아닌
 * 서버가 클라이언트의 api를 이용할떄 사용 한다.
 * api 요청이 들어왔을때 전달받는 핸들러
 *  클라이언트가 서버에 나를 컨트롤 해달라고 하는 경우에 사용
 *
 * @author macle
 */
public class ApiRequestServer extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(ApiRequestServer.class);

	private ServerSocket serverSocket = null;
	
	private ExceptionHandler exceptionHandler;
	
	private final int port;
	
	private boolean isRun = true; 
	

	private final ApiRequestConnectHandler connectHandler;


	/**
	 * 생성자
	 * @param port int
	 * @param connectHandler ApiRequestConnectHandler
	 */
	public ApiRequestServer(int port, ApiRequestConnectHandler connectHandler){
		this.port = port;
		this.connectHandler = connectHandler;
	
	}
	
	private InetAddress inetAddress = null;

	/**
	 * InetAddress 설정
	 * 네트워크 카디를 지정 하는 경우
	 * @param inetAddress InetAddress
	 */
	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}

	@Override
	public void run(){
		//noinspection TryWithIdenticalCatches
		try{
			logger.debug("request server start");
				
			
			if(serverSocket == null){
				
				if(inetAddress == null){
					serverSocket = new ServerSocket(port);
				
				}else{
					serverSocket = new ServerSocket(port, 50, inetAddress );
					
				}
			}
			
			
			logger.debug("request server start port: " + port);
			
			while(isRun){								
				Socket socket = serverSocket.accept();	
				ApiRequest apiRequest = new ApiRequest(socket);
				connectHandler.connect(apiRequest);
			}
		}catch(java.net.BindException e){
			ExceptionUtil.exception(e, logger, exceptionHandler);
		}catch(Exception e){
			ExceptionUtil.exception(e, logger, exceptionHandler);
		}
		
		logger.debug("request server stop port: " + port);
	}
	
	
	
	/**
	 * api데몬 서버를 종료한다.
	 */
	public void stopServer(){
		isRun= false;
		//noinspection CatchMayIgnoreException
		try{
			serverSocket.close();
			serverSocket = null;
			
		}catch(Exception e){}
	}
}
