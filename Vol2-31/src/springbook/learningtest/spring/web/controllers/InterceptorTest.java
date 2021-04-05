package springbook.learningtest.spring.web.controllers;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.Controller;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;
public class InterceptorTest extends AbstractDispatcherServletTest {
	@Test
	public void preHandleReturnValue() throws ServletException, IOException {
		setClasses(InterceptorConfig.class, Controller1.class);
		runService("/hello").assertViewName("controller1.jsp");
		assertThat((Controller1)getBean(Interceptor1.class).handler, is(getBean(Controller1.class)));
		
		getBean(Interceptor1.class).ret = false;
		assertThat(runService("/hello").getModelAndView(), is(nullValue()));
	}
	@Component("/hello") static class Controller1 implements Controller {
		public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
			return new ModelAndView("controller1.jsp");
		}
	}
	static class Interceptor1 extends HandlerInterceptorAdapter {
		Object handler; boolean ret = true;
		public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
			this.handler = handler;
			return ret;
		}
	}
	@Configuration static class InterceptorConfig {
		@Bean public HandlerMapping beanNameUrlHM() {
			BeanNameUrlHandlerMapping hm = new BeanNameUrlHandlerMapping();
			hm.setInterceptors(new Object[] {interceptor1()});
			return hm;
		}
		@Bean public HandlerInterceptor interceptor1() {
			return new Interceptor1();
		}
	}
	
	@Test
	public void postHandle() throws ServletException, IOException {
		setClasses(InterceptorConfig2.class, Controller1.class);
		runService("/hello").assertViewName("controller1.jsp");
		assertThat(getBean(Interceptor2.class).post, is(true)); 
		
		getBean(Interceptor2.class).ret = false;
		getBean(Interceptor2.class).post = false;
		assertThat(getBean(Interceptor2.class).post, is(false)); 
	}
	@Configuration static class InterceptorConfig2 {
		@Bean public HandlerMapping handlerMapping() {
			return new BeanNameUrlHandlerMapping() {
				{this.setInterceptors(new Object[] {interceptor2()});}};
		}
		@Bean public HandlerInterceptor interceptor2() {
			return new Interceptor2();
		}
	}
	static class Interceptor2 extends HandlerInterceptorAdapter {
		boolean post, ret = true;
		public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
			post = true;
		}
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
			return ret;
		}
	}
}
