package springbook.learningtest.spring.web.controllers;

import java.io.IOException;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class HandlerAdapterTest extends AbstractDispatcherServletTest {
	@Test
	public void simpleHandlerAdapter() throws ServletException, IOException {
		setClasses(SimpleHandlerAdapter.class, HelloController.class);
		initRequest("/hello").addParameter("name", "Spring").runService();
		assertViewName("/WEB-INF/view/hello.jsp");
		assertModel("message", "Hello Spring");
	}
	@Component("/hello")
	static class HelloController implements SimpleController {
		@ViewName("/WEB-INF/view/hello.jsp")
		@RequiredParams({"name"})
		public void control(Map<String, String> params, Map<String, Object> model) {
			model.put("message", "Hello " + params.get("name"));
		}
	}
	static class SimpleHandlerAdapter implements HandlerAdapter {
		public boolean supports(Object handler) {
			return (handler instanceof SimpleController);
		}

		public ModelAndView handle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
			Method m =  ReflectionUtils.findMethod(handler.getClass(), "control", Map.class, Map.class);
			ViewName viewName = AnnotationUtils.getAnnotation(m, ViewName.class);
			RequiredParams requiredParams = AnnotationUtils.getAnnotation(m, RequiredParams.class);
						
			Map<String, String> params = new HashMap<String, String>();
			for(String param : requiredParams.value()) {
				String value = req.getParameter(param);
				if (value == null) throw new IllegalStateException();
				params.put(param, value);
			}
			
			Map<String, Object> model = new HashMap<String, Object>();
			
			((SimpleController)handler).control(params, model);
				
			return new ModelAndView(viewName.value(), model);
		}
		
		public long getLastModified(HttpServletRequest request, Object handler) {
			return -1;
		}
	}
	public interface SimpleController {
		void control(Map<String, String> params, Map<String, Object> model);
	}
	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	public @interface ViewName {
		String value();
	}
	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	public @interface RequiredParams {
		String[] value();
	}
}
