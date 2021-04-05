package springbook.learningtest.spring.web.atmvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class WebDataBinderTest extends AbstractDispatcherServletTest {
	@Test
	public void allowed() throws ServletException, IOException {
		setClasses(UserController.class);
		initRequest("/add.do", "POST").addParameter("id", "1").addParameter("name", "name");
		runService();
		User user = (User) getModelAndView().getModel().get("user");
		assertThat(user.getId(), is(1));
		assertThat(user.getName(), is(nullValue()));
	}
	@Controller static class UserController {
		@InitBinder
		public void initBinder(WebDataBinder dataBinder) {
			dataBinder.setAllowedFields("id");
		}		
		@RequestMapping("/add") public void add(@ModelAttribute User user) {
		}
	}
	
	@Test
	public void prefix() throws ServletException, IOException {
		setClasses(UserController2.class);
		initRequest("/add.do").addParameter("_flag", "").addParameter("!type", "member");
		runService();
		User user = (User) getModelAndView().getModel().get("user");
		assertThat(user.isFlag(), is(false));
		assertThat(user.getType(), is("member"));
	}
	@Controller static class UserController2 {
		@RequestMapping("/add") public void add(@ModelAttribute User user) {
		}
	}
	static class User {
		int id;
		String name;
		boolean flag = true;
		String type;
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean isFlag() {
			return flag;
		}
		public void setFlag(boolean flag) {
			this.flag = flag;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		@Override
		public String toString() {
			return "User [flag=" + flag + ", id=" + id + ", name=" + name
					+ ", type=" + type + "]";
		}
	}
}
