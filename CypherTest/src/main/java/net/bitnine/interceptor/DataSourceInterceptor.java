package net.bitnine.interceptor;

//import static net.bitnine.controller.DataSourceController.DATASOURCE;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class DataSourceInterceptor extends HandlerInterceptorAdapter {

	public static final String DATASOURCE = "dataSource";
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		HttpSession session = request.getSession();
		
		ModelMap modelMap = modelAndView.getModelMap();
		Object dataSource = modelMap.get("dataSource");
		
		if (dataSource != null) {
			session.setAttribute(DATASOURCE, dataSource);
			response.sendRedirect("/");
		}

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		return super.preHandle(request, response, handler);
	}

	
}
