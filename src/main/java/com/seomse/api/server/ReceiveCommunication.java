


package  com.seomse.api.server;

import com.seomse.commons.communication.StringReceive;
import com.seomse.commons.handler.ExceptionHandler;
import com.seomse.commons.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
/**
 * <pre>
 *  파 일 명 : ReceiveCommunication.java
 *  설    명 : 문자열 받는 통신
 *
 *  작 성 자 : macle
 *  작 성 일 : 2018.04
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜섬세한사람들. All right reserved.
 */
public class ReceiveCommunication extends Thread{
	
	private static final Logger logger = LoggerFactory.getLogger(ReceiveCommunication.class);
	
	private StringReceive stringReceive;
	
	private ExceptionHandler exceptionHandler;
	
	
	ReceiveCommunication(Socket socket, int bufSize) throws  IOException{
		stringReceive = new StringReceive(socket, bufSize);
	}
	

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}


	private MessageReceiver messageReceiver = null;
	
	@Override
	public void run(){
		
		StringBuilder sb = new StringBuilder();
		while(true){
			
			try{
				String message = stringReceive.receive();
				if(message == null){
					break;
				}
				if(messageReceiver == null){
					sb.append(message);
					String classSearch = sb.toString();
					int index = classSearch.indexOf(',');
					
					if(index ==-1){
						continue;
					}
					//noinspection deprecation
					messageReceiver = (MessageReceiver)Class.forName(classSearch.substring(0, index)).newInstance();
					
					if(index != classSearch.length()-1){
						messageReceiver.receive(classSearch.substring(index+1));
					}					
					sb.setLength(0);

				}else{
					messageReceiver.receive(message);	
				}
				
				
			
			}catch(Exception e){
				ExceptionUtil.exception(e, logger, exceptionHandler);
				break;
			}
		}
		try{
			if(messageReceiver != null){
				messageReceiver.end();
			}
		}catch(Exception e){
			ExceptionUtil.exception(e, logger, exceptionHandler);
		}
		stringReceive.disConnect();
	}
	
}
