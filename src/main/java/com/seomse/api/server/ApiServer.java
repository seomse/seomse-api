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

import com.seomse.api.ApiCommunication;
import com.seomse.commons.callback.ObjCallback;
import com.seomse.commons.handler.ExceptionHandler;
import com.seomse.commons.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * api 통신용 서버
 *
 * @author macle
 */
public class ApiServer extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiServer.class);

	private ServerSocket serverSocket = null;
	
	private ExceptionHandler exceptionHandler;
	
	private final int port;
	
	private boolean isRun = true; 
	
	private String packageName;
	
	private final List<ApiCommunication> apiCommunicationList = new LinkedList<>();
	
	private final Object lock = new Object();

	private final ObjCallback endCallback = obj -> {
		ApiCommunication apiCommunication = (ApiCommunication) obj;
		synchronized (lock) {
			apiCommunicationList.remove(apiCommunication);
		}
	};
	
	/**
	 * 생성자
	 * @param port int service port
	 * @param packageName string default packageName
	 */
	public ApiServer(int port, String packageName){
		this.port = port;
		this.packageName = packageName;
	
	}
	
	private InetAddress inetAddress = null;


	/**
	 * inet address 설정 
	 * 네트워크를 지정할 떄
	 * @param inetAddress InetAddress
	 */
	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}

	@Override
	public void run(){
		//noinspection TryWithIdenticalCatches
		try{
			logger.debug("api server start");
				
			
			if(serverSocket == null){
				
				if(inetAddress == null){
					serverSocket = new ServerSocket(port);
				
				}else{
					serverSocket = new ServerSocket(port, 50, inetAddress );
					
				}
			}
			
			
			logger.debug("api server start port: " + port);
			
			while(isRun){								
				Socket communication_socket = serverSocket.accept();	
				ApiCommunication apiCommunication = new ApiCommunication(packageName, communication_socket);
				apiCommunication.setEndCallback(endCallback);
				synchronized (lock) {
					apiCommunicationList.add(apiCommunication);	
				}
				
				
				
					
				apiCommunication.start();		
			}
		}catch(java.net.BindException e){
			ExceptionUtil.exception(e, logger, exceptionHandler);
		}catch(Exception e){
			ExceptionUtil.exception(e, logger, exceptionHandler);
		}
		
		logger.debug("api server stop port: " + port);
	}
	
	/**
	 * 연결개수 얻기
	 * @return int apiCommunicationList size
	 */
	public int size() {
		return apiCommunicationList.size();
	}
	
	/**
	 * api데몬 서버를 종료한다.
	 */
	public void stopServer(){
		isRun= false;
		packageName= null;
		//noinspection CatchMayIgnoreException
		try{
			serverSocket.close();
			serverSocket = null;
			
		}catch(Exception e){}
	}
}