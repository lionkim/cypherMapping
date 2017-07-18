package net.bitnine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import net.bitnine.interceptor.DataSourceInterceptor;
import net.bitnine.interceptor.DataSourceOutInterceptor;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Autowired DataSourceInterceptor dataSourceInterceptor;
	@Autowired DataSourceOutInterceptor dataSourceOutInterceptor;
	
	

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(dataSourceInterceptor).addPathPatterns("/dbConnect");
//		registry.addInterceptor(dataSourceOutInterceptor).addPathPatterns("/dbConnectOut");
	}
}
