package springbook.learningtest.spring.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class SimpleMVCTest extends AbstractDispatcherServletTest {
	@Test
	public void simpleHandler() throws ServletException, IOException {
		this.setClasses(SimpleHandler.class, SimpleViewHandler.class)
			.runService("/hi");
		assertThat(this.response.getContentAsString(), is("hi"));
		
		this.runService("/view");
		assertThat(this.getModelAndView().getViewName(), is("view.jsp"));
	}

	@Controller	static class SimpleHandler {
		@RequestMapping("/hi") @ResponseBody 
		public String hi() { return "hi"; }
	}	
	@Controller	static class SimpleViewHandler {
		@RequestMapping("/view")		
		public String view() { return "view.jsp"; }
	}
	
	@Test
	public void contentNego() throws ServletException, IOException {
		this.setLocations("springbook/learningtest/spring/web/contentnegotiatingviewresolver.xml")
			.setClasses(ContentHandler.class)
			.runService("/content.json");
		
		assertThat(this.response.getContentType(), is("application/json"));
		assertThat(this.response.getContentAsString(), is("{\"name\":\"Toby\"}"));
	}
	@Controller static class ContentHandler {
		@RequestMapping("/content")
		public String content(ModelMap model) {
			model.put("name", "Toby");
			return "content";
		}
	}
	
}
