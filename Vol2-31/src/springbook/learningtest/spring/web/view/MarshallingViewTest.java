package springbook.learningtest.spring.web.view;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.xml.MarshallingView;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;
public class MarshallingViewTest extends AbstractDispatcherServletTest {
	@Test
	public void marshallingView() throws ServletException, IOException {
		setRelativeLocations("marshallingview.xml");
		initRequest("/hello").addParameter("name", "Spring");
		runService();
		assertThat(getContentAsString().indexOf("<info><message>Hello Spring</message></info>") >= 0, is(true));
	}
	@RequestMapping("/hello")
	public static class HelloController implements Controller {
		@Resource MarshallingView helloMarshallingView;
		
		public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("info", new Info("Hello " +req.getParameter("name")));
			
			return new ModelAndView(helloMarshallingView, model);
		}
	}
}
