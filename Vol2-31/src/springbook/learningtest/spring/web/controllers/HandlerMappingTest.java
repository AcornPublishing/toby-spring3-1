package springbook.learningtest.spring.web.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;
import org.springframework.web.servlet.mvc.support.ControllerBeanNameHandlerMapping;
import org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class HandlerMappingTest extends AbstractDispatcherServletTest {
	@Test
	public void beanNameUrlHM() throws ServletException, IOException {
		setRelativeLocations("beannameurlhm.xml");
		runService("/hello").assertViewName("/hello.jsp");
		runService("/hello/world").assertViewName("/hello/world.jsp");
		runService("/multi/").assertViewName("/multi/*.jsp");
		runService("/multi/a").assertViewName("/multi/*.jsp");
		runService("/root/sub").assertViewName("/root/**/sub.jsp");
		runService("/root/a/b/c/sub").assertViewName("/root/**/sub.jsp");
		runService("/s").assertViewName("/s*.jsp");
		runService("/s1234").assertViewName("/s*.jsp");
	}
	static class Controller1 extends AbstractController {
		private String url; 
		public void setUrl(String url) { this.url = url; }
		protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse res) throws Exception {
			return new ModelAndView(url+".jsp");
		}
	}
	
	@Test
	public void controllerBeanNameHM() throws ServletException, IOException {
		setClasses(ControllerBeanNameHandlerMapping.class, Controller2.class);
		runService("/hello").assertViewName("controller2.jsp");
	}	
	@Component("hello")
	static class Controller2 implements Controller {
		public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
			return new ModelAndView("controller2.jsp");
		}
	}
	
	@Test
	public void controllerClassNameHM() throws ServletException, IOException {
		setClasses(ControllerClassNameHandlerMapping.class, Controller3Controller.class);
		runService("/handlermappingtest.controller3").assertViewName("controller3.jsp");
	}	
	static class Controller3Controller implements Controller {
		public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
			return new ModelAndView("controller3.jsp");
		}
	}
	
	@Test
	public void simpleUrlHM() throws ServletException, IOException {
		setRelativeLocations("simpleurlhm.xml");
		runService("/hello").assertViewName("c1.jsp");
		runService("/multi/a").assertViewName("c2.jsp");
		runService("/deep/a/b/c/sub").assertViewName("c3.jsp");
	}
		
	@Test
	public void orderOfHM() throws ServletException, IOException {
		setClasses(Controller4.class, Controller5.class);
		runService("/hello").assertViewName("controller5.jsp");
		
		setClasses(BeanNameHM.class, AnnotationHM.class, Controller4.class, Controller5.class);
		buildDispatcherServlet();
		runService("/hello").assertViewName("controller4.jsp");
	}
	static class BeanNameHM extends BeanNameUrlHandlerMapping {
		public BeanNameHM() { setOrder(2); }
	}
	static class AnnotationHM extends DefaultAnnotationHandlerMapping {
		public AnnotationHM() { setOrder(1); }
	}
	@RequestMapping	static class Controller4 {
		@RequestMapping("/hello") public String hello() { return "controller4.jsp"; }
	}
	@Component("/hello") static class Controller5 implements Controller {
		public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
			return new ModelAndView("controller5.jsp");
		}
	}
	
	@Test
	public void defaultHandler() throws ServletException, IOException {
		setRelativeLocations("defaulthandler.xml");
		setClasses(DefaultHandler.class);
		runService("/dsalkfjalk").assertViewName("defaulthandler.jsp");
	}
	@Component("defaultHandler")
	static class DefaultHandler implements Controller {
		public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
			return new ModelAndView("defaulthandler.jsp");
		}
	}
	
	@Test
	public void temp() throws ServletException, IOException {
		setClasses(AbcController.class);
		runService("/hello");
	}
	@RequestMapping("/hello")
	static class AbcController implements Controller {
		public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
			return new ModelAndView("hello.jsp");
		}
	}
	static class BH extends BeanNameUrlHandlerMapping {
		
	}
	
	
}
