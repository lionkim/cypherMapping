package net.bitnine.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import net.bitnine.domain.ConnectInfo;
import net.bitnine.domain.ConnectInfos;
import net.bitnine.domain.State;

/**
 * 사용자의 접속정보, 쿼리 횟수의 로그 생성 클래스.
 * 
 * @author  김형우
 *
 */
@Aspect
@Component
public class QueryLogWriter {

    @Autowired private ConnectInfos connectInfos;
    
    private static final String CONNECT_SUCCESS = "Database Connect Success";
    
    public static final String Token = "접속자토큰";
	private static final String ConnectTime = "접속시간";
	private static final String QueryTimes = "쿼리횟수";

    // 포인트 컷. 로직을 적용할 지점을 설정.
    @Pointcut("execution(* net.bitnine.controller.DataSourceController.connect(..))") 
    public void connectInfo() { }
    
    // 포인트 컷. 로직을 적용할 지점을 설정.
    @Pointcut("execution(* net.bitnine.controller.JsonObjectController.getJson(..))") 
    public void queryLog() { }
	
	
	/**
	 * DB Connect 로그를 남기는 어드바이스.
	 * DataSourceController의 connect 메소드 호출 후 실행 됨.
	 * @param JoinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("connectInfo()")
	public Object connectAdvice (ProceedingJoinPoint JoinPoint) throws Throwable {	
		Object ret = JoinPoint.proceed();      // 프록시 대상 객체의 실제 메소드를 호출.

	    ConnectInfo connectInfo = new ConnectInfo();       // 새로운 ConnectInfo 객체를 생성.
		
		JSONObject jsonObject = (JSONObject) ret;
		if (jsonObject.get("message") == CONNECT_SUCCESS) {  // Db Connect가 성공했을 때 실행
		    connectInfo.setToken ((String) jsonObject.get("token"));	        
	        connectInfo.setConnetTime (stringCurrentTime());       // 현재 시간을 저장. 
	        connectInfo.setQueryTimes(0);
	        connectInfo.setState(State.VALID);
		}
        
        connectInfos.getConnectInfoList().add (connectInfo);     // connectInfos의 connectInfoList에 ConnectInfo 객체를 저장.
        
        System.out.println("Around connectInfo 생성!");
		return ret;
	}

	// 현재 시간을 String형으로 반환.
    private String stringCurrentTime() {
        String timeFormat = "yyyy-MM-dd HH:mm:ss";        
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(timeFormat));
    }

    /**
     * 사용자가 쿼리를 실행한 후 로그를 생성하는 어드바이스.
     *  JsonObjectController의 getJson 메소드 호출 후 실행 됨.
     * @param JoinPoint
     * @return
     * @throws Throwable
     */
    @Around("queryLog()")
    public Object queryLog (ProceedingJoinPoint JoinPoint) throws Throwable { 
        
        String Authorization = (String) JoinPoint.getArgs()[1];     // 대상 메소드의 2번째 인자 [ getJson(String query, @RequestHeader(value="Authorization") String Authorization) ]를 가져옴.
        
        Object ret = JoinPoint.proceed();      // 프록시 대상 객체의 실제 메소드를 호출.  

//        ConnectInfo connectInfo =  getConnectInfoByToken(Authorization);
        setConnectInfoByToken(Authorization);
        
       /* int times = connectInfo.getQueryTimes();
        connectInfo.setQueryTimes(++times);
        
        connectInfos.getConnectInfoList().add(connectInfo);*/
        
        System.out.println("Around queryLog 생성!");
        
        return ret;
    }

    /**
     * connectInfos의 connectInfoList를 순환하며 connectInfo.getToken() 과 인자로 받은 token을 비교해서
     * 동일했을경우 해당 connectInfo 객체를 반환.
     * ConnectInfos connectInfos :  객체 Scope("application"). 애플리케이션에 하나만 생성됨.
     * @param Authorization
     * @return
     */
    private void setConnectInfoByToken(String Authorization) {
        
        ConnectInfo connectInfo = connectInfos.getConnectInfoList().stream()                        // Convert to steam
                .filter(x -> Authorization.equals(x.getToken()))        // we want Authorization only
                .findAny()                                      // If 'findAny' then return found
                .orElse(null); 

        int times = connectInfo.getQueryTimes();
        connectInfo.setQueryTimes(++times);
    }
    
    
	
	protected String getUserIPAddress(HttpServletRequest request) {
		return request.getRemoteAddr();
	}

}
