package springbook.learningtest.spring31.temp;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Keesun Baik
 */
public class PropertiesTest {

	@Test
	public void keesun(){
		GenericXmlApplicationContext parentContext = new GenericXmlApplicationContext(getClass(), "parent.xml");
		
		System.out.println(parentContext.getEnvironment().getProperty("name"));
		
//		GenericXmlApplicationContext childContext = new GenericXmlApplicationContext();
//		childContext.setParent(parentContext);
//		childContext.load(getClass(), "child.xml");
//		childContext.refresh();

//		String name1 = childContext.getBean("name1", String.class);
//		String name2 = childContext.getBean("name2", String.class);
//		
//		assertThat(name1, is("keesun"));
//		assertThat(name2, is("keesun"));
		
		System.out.println(parentContext.getEnvironment().getProperty("name"));
	}
	
//	@Test 
//	public void toby() {
//		AnnotationConfigApplicationContext p = new AnnotationConfigApplicationContext();
//		p.register(Parent.class);		
//		p.refresh();
//		
////		AnnotationConfigApplicationContext c = new AnnotationConfigApplicationContext();
////		c.register(Child.class);
////		c.setParent(p);
////		c.refresh();
//		
//		GenericXmlApplicationContext childContext = new GenericXmlApplicationContext();
//		childContext.setParent(p);
//		childContext.load(getClass(), "child.xml");
//		childContext.refresh();
//	}
//	
//	@Configuration
//	@PropertySource("/springbook/learningtest/spring31/temp/env.properties")
//	//@ImportResource("/springbook/learningtest/spring31/temp/parent.xml")
//	static class Parent {
//		
//	}
//	
//	@Configuration
//	@ImportResource("/springbook/learningtest/spring31/temp/child.xml")
//	static class Child {
//		@Autowired Environment env;
//		
//		@PostConstruct
//		public void init() {
//			System.out.println(env.getProperty("name"));
//		}
//	}
//	
//	@Test
//	public void toby2() {
//		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig2.class);
//		System.out.println(ac.getEnvironment().getProperty("name"));
//		
//		for(org.springframework.core.env.PropertySource<?> mps : ac.getEnvironment().getPropertySources()) {
//			System.out.println(mps.getName());
//		}
//		
//		System.out.println(ac.getBean(AppConfig2.class).name);
//	}
//	
//	@Configuration
//	static class AppConfig2 {
//		@Value("${name}") String name;
//		
//		@Bean
//		public static PropertySourcesPlaceholderConfigurer configurer() {
//			PropertySourcesPlaceholderConfigurer p = new PropertySourcesPlaceholderConfigurer();
//			p.setLocation(new ClassPathResource("env.properties", PropertiesTest.class));
//			return p;
//		}
//	}
	
}

