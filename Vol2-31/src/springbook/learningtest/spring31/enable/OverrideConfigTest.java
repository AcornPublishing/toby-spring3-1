package springbook.learningtest.spring31.enable;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

public class OverrideConfigTest {
	@Test
	public void simple() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		Hello h = ac.getBean(Hello.class);
		h.sayHello();
		assertThat(h.getHello(), is("Hello Toby"));
	}
	
	@Configuration
	public static class AppConfig extends HelloConfig {
		@Override
		Hello hello() {
			Hello hello = super.hello();
			hello.setName("Toby");
			return hello;
		}
		
	}
	
	@Configuration
	public static class HelloConfig {
		@Bean 
		Hello hello() {
			Hello h = new Hello();
			h.setName("Spring");
			return h;
		}
	}
}
