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
	@Resource Hello 하이;
	@Resource ApplicationContext ac;
	@Test public void id() {
		assertThat(hello, is(notNullValue()));
		assertThat(하이, is(notNullValue()));
		assertThat(hello, is(하이));
		assertThat(hello, is(ac.getBean("1234")));
		assertThat(hello, is(ac.getBean("/hello")));
		assertThat(hello, is(ac.getBean("헬로우")));
		System.out.println("OK5");
	}
	
	@Component("하이") static class Hi {	}
	@Component @Named("하우디") static class Howdy { @Resource Hi 하이; }
	@Configuration static class Config {
		@Bean(name={"울랄라", "흠흠"}) 
		public Howdy lala(Hi 하이) {
			Howdy h = new Howdy();
			h.하이 = 하이;
			return h;
		}
	}
	
	@Test public void hi() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Hi.class, Howdy.class, Config.class);
		Hi 하이 = ac.getBean("하이", Hi.class);
		assertThat(하이, is(notNullValue()));
		
		Howdy h = ac.getBean("하우디", Howdy.class);
		assertThat(h.하이, is(하이));
		
		Howdy h2 = ac.getBean("울랄라", Howdy.class);
		assertThat(h2.하이, is(하이));
		
		Howdy h3 = ac.getBean("흠흠", Howdy.class);
		assertThat(h3.하이, is(하이));
	}
	
	
}
