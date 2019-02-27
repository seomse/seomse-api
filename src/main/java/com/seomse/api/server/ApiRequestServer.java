
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
 * <pre>
 *  파 일 명 : ApiRequestServer.java
 *  설    명 : api 요청용서버 서버
 *            클라이언트가 서버의 api를 이용하는것이 아닌
 *            서버가 클라이언트의 api를 이용할떄 사용 한다.
 *  작 성 자 : macle
 *  작 성 일 : 2018.04
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜섬세한사람들. All right reserved.
 */

public class ApiRequestServer extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(ApiRequestServer.class);

	private ServerSocket serverSocket = null;
	
	private ExceptionHandler exceptionHandler;
	
	private int port;
	
	private boolean isRun = true; 
	

	private ApiRequestConnectHandler connectHandler;


	/**
	 *
	 * @param port
	 * @param connectHandler
	 */
	public ApiRequestServer(int port, ApiRequestConnectHandler connectHandler){
		this.port = port;
		this.connectHandler = connectHandler;
	
	}
	
	private InetAddress inetAddress = null;
	
	
	/**
	 * inetAddress 설정
	 * @param inetAddress
	 */
	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}

	@Override
	public void run(){
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
		try{ 
			serverSocket.close();
			serverSocket = null;
			
		}catch(Exception e){}
	}
}
