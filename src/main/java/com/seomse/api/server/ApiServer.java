
package com.seomse.api.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seomse.api.ApiCommunication;
import com.seomse.commons.handler.EndHandler;
import com.seomse.commons.handler.ExceptionHandler;
import com.seomse.commons.utils.ExceptionUtil;

/**
 * <pre>
 *  파 일 명 : ApiServer.java
 *  설    명 : api 통신용 서버
 *
 *  작 성 자 : macle
 *  작 성 일 : 2018.04
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜섬세한사람들. All right reserved.
 */
public class ApiServer extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiServer.class);

	private ServerSocket serverSocket = null;
	
	private ExceptionHandler exceptionHandler;
	
	private int port;
	
	private boolean isRun = true; 
	
	private String packageName;
	
	private List<ApiCommunication> apiCommunicationList = new LinkedList<>();
	
	private Object lock = new Object();
	
	private EndHandler endHandler = new EndHandler() {
		
		@Override
		public void end(Object obj) {
			ApiCommunication apiCommunication = (ApiCommunication) obj;
			synchronized (lock) {
				apiCommunicationList.remove(apiCommunication);
			}
		}
	};
	
	/**
	 * 생성자
	 * @param port
	 * @param packageName
	 */
	public ApiServer(int port, String packageName){
		this.port = port;
		this.packageName = packageName;
	
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
				apiCommunication.setEndHandler(endHandler);
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
	 * @return
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
		try{ 
			serverSocket.close();
			serverSocket = null;
			
		}catch(Exception e){}
	}
}