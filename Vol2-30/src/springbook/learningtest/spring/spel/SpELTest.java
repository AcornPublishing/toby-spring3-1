package springbook.learningtest.spring.spel;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class SpELTest {
	@Test
	public void staticEx() {
		ExpressionParser parser = new SpelExpressionParser();
		Expression ex = parser.parseExpression("1+2");
		int result = (Integer)ex.getValue();
		assertThat(result, is(3));
	}
	
	@Test
	public void objectBinding() {
		User user = new User(1, "Spring", new Addr("Seoul", "Jongro"));
		ExpressionParser parser = new SpelExpressionParser();
		Expression nameEx = parser.parseExpression("name");
		assertThat(nameEx.getValue(user, String.class), is("Spring"));
		assertThat(parser.parseExpression("addr.city").getValue(user, String.class), is("Seoul"));
	}
	
	static class User {
		int id; String name;
		Addr addr;
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
		public Addr getAddr() {
			return addr;
		}
		public void setAddr(Addr addr) {
			this.addr = addr;
		}
		public User(int id, String name, Addr addr) {
			super();
			this.id = id;
			this.name = name;
			this.addr = addr;
		}
		
	}
	static class Addr {
		String city;
		String street;
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getStreet() {
			return street;
		}
		public void setStreet(String street) {
			this.street = street;
		}
		public Addr() {
			// TODO Auto-generated constructor stub
		}
		public Addr(String city, String street) {
			super();
			this.city = city;
			this.street = street;
		}
		 
	}
}
