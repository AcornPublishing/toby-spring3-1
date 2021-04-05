package springbook.learningtest.spring31.ioc;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.*;

import org.junit.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;


public class BeanRoleTest {
	@Test
	public void simpleConfig() {
		GenericApplicationContext ac = new GenericXmlApplicationContext(BeanRoleTest.class, "beanrole.xml");
		BeanDefinitionUtils.printBeanDefinitions(ac);
		
		SimpleConfig sc = ac.getBean(SimpleConfig.class);
		assertThat(sc.hello, is(notNullValue()));
		sc.hello.sayHello(); // 예외 발생하지 않음.
	}
}
