package springbook.learningtest.spring.web.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.RequestToViewNameTranslator;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class RequestViewNameTranslatorTest extends AbstractDispatcherServletTest {
	@Test
	public void defaultRequestToViewNameTranslatorTest() throws ServletException, IOException {
		setClasses(Config1.class);
		runService("/hello").assertViewName("hello.jsp");
		runService("/hello/world").assertViewName("hello/world.jsp");
		runService("/hi").assertViewName("hi.jsp");
	}
	@Configuration static class Config1 {
		@Bean public HandlerMapping handlerMapping() {
			return new BeanNameUrlHandlerMapping() {{
				this.setDefaultHandler(defaultHandler());
			}};
		}
		@Bean public RequestToViewNameTranslator viewNameTranslator() {
			return new DefaultRequestToViewNameTranslator() {{
				this.setSuffix(".jsp");
			}};
		}
		@Bean public Controller defaultHandler() {
			return new Controller() {
				public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
					return new ModelAndView();
				}
			};
		}
	}
}
