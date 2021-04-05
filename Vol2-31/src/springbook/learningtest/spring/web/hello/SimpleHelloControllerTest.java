package springbook.learningtest.spring.web.hello;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class SimpleHelloControllerTest extends AbstractDispatcherServletTest {
	@Test
	public void helloController() throws ServletException, IOException {
		ModelAndView mav = setRelativeLocations("spring-servlet.xml")
						  .setClasses(HelloSpring.class)
						  .initRequest("/hello", RequestMethod.GET).addParameter("name", "Spring")
						  .runService()
						  .getModelAndView();
		
		assertThat(mav.getViewName(), is("/WEB-INF/view/hello.jsp"));
		assertThat((String)mav.getModel().get("message"), is("Hello Spring"));		
	}
	
	@Test
	public void helloControllerWithAssertMethods() throws ServletException, IOException {
		this.setRelativeLocations("spring-servlet.xml")
			.setClasses(HelloSpring.class)
			.initRequest("/hello", RequestMethod.GET).addParameter("name", "Spring")
			.runService()
			.assertModel("message", "Hello Spring")
			.assertViewName("/WEB-INF/view/hello.jsp");
	}
	
	@Test
	public void helloControllerWithServletPath() throws ServletException, IOException {
		this.setRelativeLocations("spring-servlet.xml")
			.setClasses(HelloSpring.class)
			.setServletPath("/app")
			.initRequest("/app/hello", RequestMethod.GET).addParameter("name", "Spring")
			.runService()
			.assertModel("message", "Hello Spring")
			.assertViewName("/WEB-INF/view/hello.jsp");
	}
}
