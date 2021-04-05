package springbook.learningtest.spring.ioc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import javax.inject.Named;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import springbook.learningtest.spring.ioc.bean.Hello;
import springbook.learningtest.spring.ioc.bean.Printer;
import springbook.learningtest.spring.ioc.bean.StringPrinter;

public class SimpleAtBeanTest {
	@Test
	public void simpleAtBean() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(HelloService.class);
		Hello h1 = ac.getBean("hello", Hello.class);
		Hello h2 = ac.getBean("hello2", Hello.class);
		assertThat(h1.getPrinter(), is(notNullValue()));
		assertThat(h1.getPrinter(), is(sameInstance(h2.getPrinter())));
		
		HelloService hs = ac.getBean("service", HelloService.class);
		assertThat(hs, is(notNullValue()));
	}
	
	@Named("service")
	static class HelloService {
		private Printer printer;
		
		@Autowired
		public void setPrinter(Printer printer) {
			this.printer = printer;
		}

		@Bean
		private Hello hello() {
			Hello hello = new Hello();
			hello.setName("Spring");
			hello.setPrinter(this.printer);
			return hello;
		}
		
		@Bean		
		private Hello hello2() {
			Hello hello = new Hello();
			hello.setName("Spring2");
			hello.setPrinter(this.printer);
			return hello;
		}
		
		@Bean
		private Printer printer() {
			return new StringPrinter();
		}
	}
}
