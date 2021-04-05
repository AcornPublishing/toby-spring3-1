package springbook.learningtest.spring.web.controllers;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class SimpleAnnotationControllerTest extends AbstractDispatcherServletTest {
	@Test
	public void annotationHello() throws ServletException, IOException {
		setClasses(HelloController.class);
		initRequest("/hello").addParameter("name", "Spring");
		runService();
		assertModel("message", "Hello Spring");
		assertViewName("/WEB-INF/view/hello.jsp");
	}

	@Controller	
	static class HelloController {
		@RequestMapping("/hello")
		public String hello(@RequestParam("name") String name, ModelMap map) {
			map.put("message", "Hello " + name);
			return "/WEB-INF/view/hello.jsp";
		}
	}
}
