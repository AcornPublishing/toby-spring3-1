package springbook.learningtest.spring.ioc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;
import javax.inject.Named;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.learningtest.spring.ioc.bean.Hello;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("identifier.xml")
public class BeanIdentifierTest {
	@Autowired Hello hello;
	@Resource Hello ����;
	@Resource ApplicationContext ac;
	@Test public void id() {
		assertThat(hello, is(notNullValue()));
		assertThat(����, is(notNullValue()));
		assertThat(hello, is(����));
		assertThat(hello, is(ac.getBean("1234")));
		assertThat(hello, is(ac.getBean("/hello")));
		assertThat(hello, is(ac.getBean("��ο�")));
		System.out.println("OK5");
	}
	
	@Component("����") static class Hi {	}
	@Component @Named("�Ͽ��") static class Howdy { @Resource Hi ����; }
	@Configuration static class Config {
		@Bean(name={"�����", "����"}) 
		public Howdy lala(Hi ����) {
			Howdy h = new Howdy();
			h.���� = ����;
			return h;
		}
	}
	
	@Test public void hi() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Hi.class, Howdy.class, Config.class);
		Hi ���� = ac.getBean("����", Hi.class);
		assertThat(����, is(notNullValue()));
		
		Howdy h = ac.getBean("�Ͽ��", Howdy.class);
		assertThat(h.����, is(����));
		
		Howdy h2 = ac.getBean("�����", Howdy.class);
		assertThat(h2.����, is(����));
		
		Howdy h3 = ac.getBean("����", Howdy.class);
		assertThat(h3.����, is(����));
	}
	
	
}
