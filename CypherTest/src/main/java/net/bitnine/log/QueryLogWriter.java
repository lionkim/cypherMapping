package net.bitnine.log;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 사용자의 접속정보, 쿼리 횟수의 로그 생성 클래스.
 * 
 * @author  김형우
 *
 */
@Aspect
@Component
public class QueryLogWriter {

    public static final String Token = "접속자토큰";
	private static final String ConnectTime = "접속시간";
	private static final String QueryTimes = "쿼리횟수";
	
	// 포인트 컷. 로직을 적용할 지점을 설정.
	@Pointcut("execution(* net.bitnine.controller.JsonObjectController.getJson(..))") 
	public void queryLog() { }
	
	
	// JsonObjectController의 getJson 메소드 호출 후 실행 되는 어드바이스.
	@Around("queryLog()")
	public Object createAuthorityLogging(ProceedingJoinPoint pjp) throws Throwable {	
		Object ret = pjp.proceed();
				
		commonAdminLogging(LoggingTypeCreate, pjp);		
		System.out.println("Around createLog 생성!");
		
		return ret;
	}
	
	/**
	 * 생성, 수정 로그를 기록하는 공통 모듈.
	 * @param loggingType
	 */
	private void commonAdminLogging(String loggingType, ProceedingJoinPoint pjp) {
		
		Admin admin = Admin.getAdminbyAuthentication();	// 현재 로그인한 Admin. 권한 부여자.
        
		HttpServletRequest request = HttpServletRequestUtil.getHttpServletRequest();	// 접속자의 IP 주소를 설정을 위한 HttpServletRequest 가져옴.
		
        // 생성된 Authorities 객체를 가져옴.
		Object[] objects = pjp.getArgs();		
		Authorities authorities = (Authorities) objects[0];
		
        // 새로운 AdminLogging를 생성하고 필드값들을 설정한 후 persist
		persistAuthorityLogging(loggingType, request,  admin, authorities);
	}

	/**
	 * 새로운 AdminLogging를 생성하여 persist
	 * @param loggingType
	 * @param request
	 * @param admin
	 */
	private void persistAuthorityLogging(String loggingType, HttpServletRequest request, Admin admin, Authorities authorities) {
        
		// AdminAuthorityLogging 세팅.
        AdminAuthorityLogging adminAuthorityLogging = getAdminAutthorityLogging(loggingType, request, admin,
				authorities);
        
        adminAuthorityLogging.persist();
	}

	// AdminAuthorityLogging를 파라미터값으로 설정하고 생성된 AdminAuthorityLogging를 리턴.
	public AdminAuthorityLogging getAdminAutthorityLogging(String loggingType, HttpServletRequest request,
			Admin admin, Authorities authorities) {
		AdminAuthorityLogging adminAuthorityLogging = new AdminAuthorityLogging();
        adminAuthorityLogging.setLogAdminId(authorities.getAdmin().getAdId());				// 권한 획득자 아이디
        adminAuthorityLogging.setLogAdminName(authorities.getAdmin().getAdName());	// 권한 획득자 이름
        adminAuthorityLogging.setLogAdminRole(authorities.getAuthRole());					// 권한 획득자 권한 
        adminAuthorityLogging.setLogAdminIp(request.getRemoteAddr());		// 아이피 주소
        adminAuthorityLogging.setLogTime(TimeUtils.getTime());	// 시간
        adminAuthorityLogging.setAuthorizer(admin.getAdId());	// 권한 부여자 아이디
        adminAuthorityLogging.setLogType(loggingType);			// 권한 부여 타입. (생성, 수정, 삭제)
		return adminAuthorityLogging;
	}
	
	protected String getUserIPAddress(HttpServletRequest request) {
		return request.getRemoteAddr();
	}

}
