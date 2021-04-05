package springbook.learningtest.spring.ioc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ResourceLoader;

public class AutoRegisteredBeansTest {
	@Test
	public void autoRegisteredBean() {
		System.getProperties().put("os.name", "Hi");
		
		ApplicationContext ac = new AnnotationConfigApplicationContext(SystemBean.class);
		SystemBean bean = ac.getBean(SystemBean.class);
		assertThat(bean.applicationContext, is(ac));
		
		System.out.println(bean.osname);
		System.out.println(bean.path);
		
		System.out.println("### " + bean.systemProperties);
		System.out.println("$$$ " + bean.systemEnvironment);
		
	}
	static class SystemBean {
		@Resource ApplicationContext applicationContext;
		@Autowired BeanFactory beanFactory;
		@Autowired ResourceLoader resourceLoader;
		@Autowired ApplicationEventPublisher applicationEventPublisher;
		
		@Value("#{systemProperties['os.name']}") String osname;
		@Value("#{systemEnvironment['Path']}") String path;
		
		@Resource Properties systemProperties;
		@Resource Map systemEnvironment;
	}
	
}
