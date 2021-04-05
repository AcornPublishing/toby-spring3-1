package springbook.learningtest.spring31.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class RequestConditionTest extends AbstractDispatcherServlet31Test {
	@Test
	public void patternRC() throws ServletException, SecurityException, NoSuchMethodException, IOException {
		setClasses(AppConfig.class, PatternController.class, ParamsController.class, 
					HeadersController.class, ConsumesController.class, ProducesController.class,
					RequestMethodController.class, Quiz4.class, Quiz5.class, Quiz6.class);
		buildDispatcherServlet();
		
		RequestMappingHandlerMapping rmhm = getBean(AppConfig.class).rhhm;
		
		for(Map.Entry<RequestMappingInfo, HandlerMethod> e : rmhm.getHandlerMethods().entrySet()) {
			System.out.println(e.getKey() + "\t" + e.getValue());
		}
		
		runService("/a/c");
		assertThat(response.getStatus(), is(HttpServletResponse.SC_OK));
		
		runService("/a/c.html");
		assertThat(response.getStatus(), is(HttpServletResponse.SC_OK));
		
		runService("/a/c/");
		assertThat(response.getStatus(), is(HttpServletResponse.SC_OK));
		
		runService("/a/d");
		assertThat(response.getStatus(), is(HttpServletResponse.SC_OK));
		
		runService("/b/c");
		assertThat(response.getStatus(), is(HttpServletResponse.SC_OK));
		
		runService("/b/d");
		assertThat(response.getStatus(), is(HttpServletResponse.SC_OK));
		
		runService("/");
		assertThat(response.getStatus(), is(HttpServletResponse.SC_OK));
		
		initRequest("/").addParameter("p1", "").runService();
		assertThat(response.getStatus(), is(HttpServletResponse.SC_OK));
		
		initRequest("/").addParameter("p1", "").addParameter("p2", "").runService();
		assertThat(response.getStatus(), is(HttpServletResponse.SC_OK));
		
		initRequest("/").addParameter("p1", "").addParameter("p2", "").addParameter("p3", "").addParameter("p4", "").runService();
		assertThat(response.getStatus(), is(HttpServletResponse.SC_OK));
	}
	
	@Configuration
	static class AppConfig extends WebMvcConfigurationSupport {
		@Autowired RequestMappingHandlerMapping rhhm;

		@Override
		public RequestMappingHandlerMapping requestMappingHandlerMapping() {
			RequestMappingHandlerMapping rmhm = new RequestMappingHandlerMapping();
//			rmhm.setUseSuffixPatternMatch(false);
//			rmhm.setUseTrailingSlashMatch(false);
			return rmhm;
		}
	}
	
	@RequestMapping({"/s1", "/s2"})
	static interface Super {
		public String hello();
	}
	
	@Controller
	@RequestMapping({"/a", "/b"})
	static class PatternController  { // implements Super {
		@RequestMapping({"/c", "/d"})
		public String hello() {
			return "hello.jsp";
		}
	}
	
	@Controller
	@RequestMapping(params={"p1", "p2"})
	static class ParamsController {
		@RequestMapping(params={"p3", "p4"})
		public String hello2() {
			return "hello.jsp";
		}
	}
	
	@Controller
	@RequestMapping(headers={"h1", "h2"})
	static class HeadersController {
		@RequestMapping(value="/f", headers={"h3", "h4"})
		public String hello3() {
			return "hello.jsp";
		}
	}
	
	@Controller
	@RequestMapping
	static class ConsumesController {
		@RequestMapping(value="/g", consumes={"c3/text", "c4/text"})
		public String hello4() {
			return "hello.jsp";
		}
	}
	
	@Controller
	@RequestMapping(produces={"r1/text", "r2/text"})
	static class ProducesController {
		@RequestMapping(value="/g", produces={"r3/text", "r4/text"})
		public String hello5() {
			return "hello.jsp";
		}
	}
	
	@Controller
	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
	static class RequestMethodController {
		@RequestMapping(method={RequestMethod.PUT, RequestMethod.DELETE})
		public String hello6() {
			return "hello.jsp";
		}
	}
	
	@Controller
	@RequestMapping(headers={"a", "Content-Type=application/json", "Content-Type=multipart/form-data"})
	public static class Quiz4 {
		@RequestMapping(headers={"c", "d"})
		public void hello() {}
	}
	
	@Controller
	@RequestMapping(headers={"a", "b"})
	public static class Quiz5 {
		@RequestMapping(headers={"c", "d", "Content-Type=application/json"}, consumes="multipart/form-data")
		public void hello() {}
	}
	
	@Controller
	@RequestMapping(consumes={"application/xml", "application/x-www-form-urlencoded"})
	public static class Quiz6 {
		@RequestMapping(consumes={"multipart/form-data", "application/json"})
		public void hello() {}
	}
}




