package springbook.learningtest.spring.resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.*;
import org.springframework.web.servlet.DispatcherServlet;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class ResourceTest extends AbstractDispatcherServletTest {
	@Test
	public void resources() throws ServletException, IOException {
		setRelativeLocations("resources.xml");
		runService("/resources/test.js");
		
		System.out.println(getContentAsString());
		System.out.println(response.getStatus());
		
		runService("/resources/test.js");
		System.out.println(response.getStatus());
	}
}