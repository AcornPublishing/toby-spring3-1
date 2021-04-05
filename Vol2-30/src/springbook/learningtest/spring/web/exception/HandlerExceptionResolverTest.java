package springbook.learningtest.spring.web.exception;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class HandlerExceptionResolverTest extends AbstractDispatcherServletTest  {
	@Test
	public void annotationMethod() throws ServletException, IOException {
		setClasses(HelloCon.class);
		runService("/hello");
		assertViewName("dataexception");
		System.out.println(getModelAndView().getModel().get("msg"));
	}
	@RequestMapping
	static class HelloCon {
		@RequestMapping("/hello")
		public void hello() {
			if (1==1) throw new DataRetrievalFailureException("hi");
		}
		
		@ExceptionHandler(DataAccessException.class)
		public ModelAndView dataAccessExceptionHandler(DataAccessException ex) {
			return new ModelAndView("dataexception").addObject("msg", ex.getMessage());
		}
	}
	
	@Test
	public void responseStatus() throws ServletException, IOException {
		setClasses(HelloCon2.class);
		runService("/hello");
		System.out.println(response.getStatus());
		System.out.println(response.getErrorMessage());
	}
	@RequestMapping
	static class HelloCon2 {
		@RequestMapping("/hello")
		public void hello() {
			if (1==1) throw new NotInServiceException();
		}
	}
	
	@ResponseStatus(value=HttpStatus.SERVICE_UNAVAILABLE, reason="서비스 긴급 점검중")
	static class NotInServiceException extends RuntimeException {
	}
	
}
