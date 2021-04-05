package springbook.learningtest.spring.web.atmvc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class RequestResponseBodyTest extends AbstractDispatcherServletTest {
	@Test
	public void requestBody() throws ServletException, IOException {
		setClasses(Ctrl1.class);
		initRequest("/hello.do", "POST");
		request.addHeader("content-type", "text/html");
		request.setContent("name=Spring".getBytes());
		runService();
		assertThat(response.getStatus(), is(200));
	}
	
	@Controller static class Ctrl1 {
		@RequestMapping("/hello") public void form(@RequestBody String body) {
			assertThat(body, is("name=Spring"));
		}
	}
}
