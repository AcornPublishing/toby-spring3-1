package springbook.learningtest.spring.web.atmvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class FormatterTest extends AbstractDispatcherServletTest {
	@Test
	public void numberFormat() throws ServletException, IOException {
		setRelativeLocations("mvc-annotation.xml");
		setClasses(UserController.class);
		initRequest("/hello.do").addParameter("money", "$1,234.56");
		runService();
	}
	
	@Test
	public void dateFormat() throws ServletException, IOException {
		setRelativeLocations("mvc-annotation.xml");
		setClasses(UserController.class);
		initRequest("/hello.do").addParameter("date", "01/02/1999");
		runService();
	}
	
	static class User {
		@DateTimeFormat(pattern="dd/MM/yyyy")
		Date date;
		public Date getDate() { return date; }
		public void setDate(Date date) { this.date = date; }
		
		@NumberFormat(pattern="$###,##0.00")
		BigDecimal money;
		public BigDecimal getMoney() { return money; }
		public void setMoney(BigDecimal money) { this.money = money; }
		
	}
	@Controller static class UserController {
		@RequestMapping("/hello") void hello(User user) {
			System.out.println(user.date);
			System.out.println(user.money);
		}
	}
	
	@Test
	public void dateTimeFormat() {
		System.out.println(org.joda.time.format.DateTimeFormat.patternForStyle("SS", Locale.US));
	}
}
