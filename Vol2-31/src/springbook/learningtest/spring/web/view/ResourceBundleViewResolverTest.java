package springbook.learningtest.spring.web.view;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;
public class ResourceBundleViewResolverTest extends AbstractDispatcherServletTest {
	@Test 
	public void rbvr() throws ServletException, IOException {
		setClasses(RBVR.class, InternalResourceViewResolver.class, HelloController.class, MainController.class);
		runService("/hello");
		assertThat(response.getForwardedUrl(), is("/WEB-INF/view/hello.jsp"));
		
		runService("/main");
		System.out.println(response.getForwardedUrl());
	}
	static class RBVR extends ResourceBundleViewResolver {{
		setOrder(1);
	}}
	@RequestMapping("/hello")
	public static class HelloController implements Controller {
		public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
			return new ModelAndView("hello");
		}
	}
	@RequestMapping("/main")
	public static class MainController implements Controller {
		public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
			return new ModelAndView("main.jsp");
		}
	}
}
