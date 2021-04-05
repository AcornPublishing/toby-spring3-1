package springbook.learningtest.spring31.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;
import springbook.learningtest.spring31.ioc.BeanDefinitionUtils;

public class MvcArchitectureTest extends AbstractDispatcherServletTest {
	@Test
	public void oldArchitecture() throws ServletException, IOException {
		setClasses(SimpleController.class, OldArchitecture.class).buildDispatcherServlet();
		runService("/hello");
		
		assertThat(getBean(SimpleInterceptor.class).handler, is(SimpleController.class));
	}
	
	@Test
	public void newArchitecture() throws ServletException, IOException {
		setClasses(SimpleController.class, WebConfig.class).buildDispatcherServlet();
		runService("/hello");
		
		assertThat(getBean(SimpleInterceptor.class).handler, is(HandlerMethod.class));
	}
	
	@Configuration
	public static class WebConfig extends WebMvcConfigurationSupport {
		@Override
		protected void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(handlerInterceptor());
		}
		
		@Bean HandlerInterceptor handlerInterceptor() {
			return new SimpleInterceptor();
		}
	}
	
	@Configuration
	static class OldArchitecture {
		@Bean HandlerMapping handlerMapping() {
			DefaultAnnotationHandlerMapping hm = new DefaultAnnotationHandlerMapping();
			hm.setInterceptors(new Object[] { handlerInterceptor() });
			return hm;
		}
		
		@Bean HandlerInterceptor handlerInterceptor() {
			return new SimpleInterceptor();
		}
	}
	
	@Controller
	static class SimpleController {
		@RequestMapping("/hello")
		public String hello() {
			return "hello.jsp";
		}
	}
	
	static class SimpleInterceptor extends HandlerInterceptorAdapter {
		private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
		
		Object handler;
		
		@Override
		public boolean preHandle(HttpServletRequest request,
				HttpServletResponse response, Object handler) throws Exception {
			System.out.println(handler.getClass().getName());
			this.handler = handler;
			
			if (handler instanceof HandlerMethod)
				System.out.println(asString((HandlerMethod)handler));
			
			return true;
		}
		
		private String asString(HandlerMethod handlerMethod) {
			StringBuilder sb = new StringBuilder();
			sb.append("\nController:\n").append(handlerMethod.getBeanType().getSimpleName());
			sb.append("\nMethod:\n");
			sb.append(handlerMethod.getMethod().getReturnType().getSimpleName()).append(" ");
			sb.append(handlerMethod.getMethod().getName()).append("(");
			for (MethodParameter param : handlerMethod.getMethodParameters()) {
				param.initParameterNameDiscovery(this.parameterNameDiscoverer);
				for (Annotation annotation : param.getParameterAnnotations()) {
					sb.append(annotation).append(" ");
				}
				sb.append(param.getParameterType().getSimpleName()).append(" ");
				sb.append(param.getParameterName());
				if (param.getParameterIndex() < handlerMethod.getMethodParameters().length - 1) {
					sb.append(" ");
				}
			}
			sb.append(")\n");
			return sb.toString();
		}
	}
	
	@Test
	public void handlerMethodInterceptor() throws ServletException, IOException {
		setClasses(JobController.class, JobWebConfig.class).buildDispatcherServlet();
		runService("/hello");
		runService("/specialjob");
		
	}
	
	@Configuration
	public static class JobWebConfig extends WebMvcConfigurationSupport {
		@Override
		protected void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(handlerInterceptor());
		}
		
		@Bean HandlerInterceptor handlerInterceptor() {
			return new AuditInterceptor();
		}
	}
	
	static class AuditInterceptor extends HandlerInterceptorAdapter {
		@Override
		public boolean preHandle(HttpServletRequest request,
				HttpServletResponse response, Object handler) throws Exception {
			
			HandlerMethod hm = (HandlerMethod)handler;
			if (hm.getMethodAnnotation(Audit.class) != null) {
				saveAuditInfo(request, response, handler);
			}
			
			return super.preHandle(request, response, handler);
		}

		private void saveAuditInfo(HttpServletRequest request,
				HttpServletResponse response, Object handler) {
			System.out.println(request.getRequestURI());
		}
	}
	
	@Controller
	static class JobController {
		@RequestMapping("/hello")
		public String hello() { return "hello.jsp"; }
		
		@RequestMapping("/specialjob")
		@Audit
		public String specialjob() { return "specialjob.jsp"; }
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@interface Audit {}
	
	@Test
	public void defaultStrategies() throws ServletException {
		setRelativeLocations("servlet.xml");
		buildDispatcherServlet();
		BeanDefinitionUtils.printBeanDefinitions(getContext());
	}
}
