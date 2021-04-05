package springbook.learningtest.spring.aop;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.support.GenericXmlApplicationContext;

public class ConfigurableTest {
	@Test @Ignore 
	public void configurable() {
		GenericXmlApplicationContext ac = new GenericXmlApplicationContext("springbook/learningtest/spring/aop/ConfigurableTest-context.xml");
		
		Client c = new Client();
		assertThat(c.service, is(notNullValue()));
	}
	
	@Configurable(autowire=Autowire.BY_NAME)
	public static class Client {
		@Autowired Service service;
		
		public void setService(Service service) {
			this.service = service;
		}
	}
	public static class Service {
	}
}
