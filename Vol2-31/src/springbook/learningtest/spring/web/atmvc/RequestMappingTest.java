package springbook.learningtest.spring.web.atmvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class RequestMappingTest extends AbstractDispatcherServletTest {
	@Test
	public void requestmapping() throws ServletException, IOException {
		setClasses(Ctrl1.class);
		// default suffix pattern
		runService("/hello").assertViewName("hello.jsp");
		runService("/hello/").assertViewName("hello.jsp");
		runService("/hello.a").assertViewName("hello.jsp");
		runService("/hello.html").assertViewName("hello.jsp");
		
		// method
		initRequest("/user/add", "GET").runService().assertViewName("get.jsp");
		initRequest("/user/add", "POST").runService().assertViewName("post.jsp");
		assertThat(initRequest("/user/add", "PUT").runService().getModelAndView(), is(nullValue()));
		assertThat(this.response.getStatus(), is(405));	// method not allowed
		
		// param
		initRequest("/user/edit", "POST").addParameter("type", "admin").runService().assertViewName("admin.jsp");
		initRequest("/user/edit", "GET").addParameter("type", "member").runService().assertViewName("member.jsp");
		initRequest("/user/edit").addParameter("type", "xxx").runService().assertViewName("edit.jsp");
		initRequest("/user/edit").runService().assertViewName("not.jsp");
	}
	@RequestMapping
	static class Ctrl1 {
		@RequestMapping("/hello") public String suffixpattern() { return "hello.jsp"; }
		@RequestMapping(value="/user/add", method=RequestMethod.GET) public String get() { return "get.jsp"; }
		@RequestMapping(value="/user/add", method=RequestMethod.POST) public String post() { return "post.jsp"; }
		@RequestMapping(value="/user/edit", params="type=admin") public String paramadmin() { return "admin.jsp"; }
		@RequestMapping(value="/user/edit", params="type=member") public String paraqmmember() { return "member.jsp"; }
		@RequestMapping(value="/user/edit", params="!type") public String nottype() { return "not.jsp"; }
		@RequestMapping(value="/user/edit") public String noparam() { return "edit.jsp"; }
	}
	
	@Test
	public void combine() throws ServletException, IOException {
		setClasses(MemberController.class);
		runService("/member/add").assertViewName("add.jsp");
		runService("/member/edit").assertViewName("edit.jsp");
		runService("/member/delete").assertViewName("delete.jsp");
	}
	@RequestMapping("/member")
	static class MemberController {
		@RequestMapping("/add") public String add() { return "add.jsp"; }
		@RequestMapping("/edit") public String edit() { return "edit.jsp"; }
		@RequestMapping("/delete") public String delete() { return "delete.jsp"; }
	}
	
	@Test
	public void classonly() throws ServletException, IOException {
		setClasses(Ctrl2.class);
		runService("/member/add").assertViewName("add.jsp");
		runService("/member/edit").assertViewName("edit.jsp");
		runService("/member/delete").assertViewName("delete.jsp");
	}
	@RequestMapping("/member/*")
	static class Ctrl2 {
		@RequestMapping public String add() { return "add.jsp"; }
		@RequestMapping public String edit() { return "edit.jsp"; }
		@RequestMapping public String delete() { return "delete.jsp"; }
	}
	
	@Test
	public void methodonly() throws ServletException, IOException {
		setClasses(Ctrl3.class, Ctrl4.class);
		runService("/member/add").assertViewName("add.jsp");
		runService("/member/edit").assertViewName("edit.jsp");
		runService("/member/delete").assertViewName("delete.jsp");
		runService("/user/add").assertViewName("add.jsp");
		runService("/user/edit").assertViewName("edit.jsp");
		runService("/user/delete").assertViewName("delete.jsp");
	}
	@RequestMapping
	static class Ctrl3 {
		@RequestMapping("/member/add") public String add() { return "add.jsp"; }
		@RequestMapping("/member/edit") public String edit() { return "edit.jsp"; }
		@RequestMapping("/member/delete") public String delete() { return "delete.jsp"; }
	}
	@Controller
	static class Ctrl4 {
		@RequestMapping("/user/add") public String add() { return "add.jsp"; }
		@RequestMapping("/user/edit") public String edit() { return "edit.jsp"; }
		@RequestMapping("/user/delete") public String delete() { return "delete.jsp"; }
	}
	
	@Test
	public void inheritance() throws ServletException, IOException {
		setClasses(Sub1.class, Sub2.class, Sub2a.class, Sub3.class, Sub4.class, Sub6.class, Sub6a.class);
		runService("/member/add").assertViewName("add.jsp");
		runService("/user/edit").assertViewName("edit.jsp");
		runService("/user2a/edit").assertViewName("edit2a.jsp");
		runService("/admin/sub").assertViewName("subview.jsp");
		assertThat(runService("/admin/super").getModelAndView(), is(nullValue()));
		// runService("/sys/super").assertViewName("subview.jsp"); // 클래스 상속과 인터페이스 구현의 다른 특징! 3.0.4부터 바뀜.
		runService("/super/add").assertViewName("add.jsp");
		runService("/super2/add").assertViewName("add2.jsp");
	}
	@RequestMapping("/member") static class Super1 { }
	static class Sub1 extends Super1 { 
		@RequestMapping("/add") public String add() { return "add.jsp"; } 
	}	
	
	static class Super2 { 
		@RequestMapping("/edit") public String add() { return "edit.jsp"; } }	
	@RequestMapping("/user") static class Sub2 extends Super2 {  
	}
	
	static class Super2a { 
		@RequestMapping("/edit") public String add() { return "edit.jsp"; } }	
	@RequestMapping("/user2a") static class Sub2a extends Super2a {  
		public String add() { return "edit2a.jsp"; }
	}

	@RequestMapping("/adming") static class Super3  { 
		@RequestMapping(value="/super", method=RequestMethod.POST) public String add() { return "superview.jsp"; }	}
	@RequestMapping("/admin") static class Sub3 extends Super3 {  
		@RequestMapping("/sub") public String add() { return "subview.jsp"; } }
	
	static class Super4  { 
		@RequestMapping(value="/super") public String add() { return "superview.jsp"; }	}
	@RequestMapping("/sys") static class Sub4 extends Super4 {  
		@RequestMapping public String add() { return "subview.jsp"; } }
	
	@RequestMapping("/super") static class Super6 { @RequestMapping("/add") public String add() { return "add.jsp"; } }
	static class Sub6 extends Super6 {		
	}
	
	@RequestMapping("/super2") static class Super6a { @RequestMapping("/add") public String add() { return "add.jsp"; } }
	static class Sub6a extends Super6a {		
		public String add() { return "add2.jsp"; }
	}	
	
	@Test
	public void interfaces() throws ServletException, IOException {
		setClasses(Cls1.class, Cls2.class, Cls3.class,Cls4.class, Cls6.class);
		runService("/member/add").assertViewName("add.jsp");
		runService("/user/edit").assertViewName("edit.jsp");
		runService("/admin/sub").assertViewName("subview.jsp");
		assertThat(runService("/admin/super").getModelAndView(), is(nullValue()));
		assertThat(runService("/sys/super").getModelAndView(), is(nullValue())); // 클래스 상속과 인터페이스 구현의 다른 특징!
		runService("/sub/add").assertViewName("add.jsp");
	}
	@RequestMapping("/member") interface Inf1 { public String add(); }
	static class Cls1 implements Inf1 { 
		@RequestMapping("/add") public String add() { return "add.jsp"; } 
	}	
		
	static interface Inf2 {  @RequestMapping("/edit") public String add(); }	
	 @RequestMapping("/user") static class Cls2 implements Inf2 {
		public String add() { return "edit.jsp";	}  }

	@RequestMapping("/adming") static interface Inf3  { 
		@RequestMapping(value="/super", method=RequestMethod.POST) public String add();	}
	@RequestMapping("/admin") static class Cls3 implements Inf3 {  
		@RequestMapping("/sub") public String add() { return "subview.jsp"; } }

	static interface Inf4  { @RequestMapping(value="/super") public String add();	}
	@RequestMapping("/sys") static class Cls4 implements Inf4 {  
		@RequestMapping public String add() { return "subview.jsp"; } }

	@RequestMapping("/sub") interface Inf6 { @RequestMapping("/add") public String add(); }
	static class Cls6 implements Inf6 { 
		 public String add() { return "add.jsp"; } 
	}	
}
