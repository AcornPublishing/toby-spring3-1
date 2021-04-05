package springbook.learningtest.spring.ioc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import springbook.learningtest.spring.ioc.resource.Hello;

public class AnnotationConfigurationTest {
	private String basePath = StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())) + "/";
	private String beanBasePath = basePath + "annotation/";

	@Test
	public void atResource() {
		ApplicationContext ac = new GenericXmlApplicationContext(basePath + "resource.xml");

		Hello hello = ac.getBean("hello", Hello.class);
		
		hello.print();
		
		assertThat(ac.getBean("myprinter").toString(), is("Hello Spring"));
	}
	
	@Test
	public void atAutowiredCollection() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(Client.class, ServiceA.class, ServiceB.class);
		Client client = ac.getBean(Client.class);
		assertThat(client.beanBArray.length, is(2));
		assertThat(client.beanBSet.size(), is(2));
		assertThat(client.beanBMap.entrySet().size(), is(2));
		assertThat(client.beanBList.size(), is(2));
		assertThat(client.beanBCollection.size(), is(2));
	}
	
	// atAutowiredCollection test 
	static class Client {
		@Autowired Set<Service> beanBSet;
		@Autowired Service[] beanBArray;		
		@Autowired Map<String, Service> beanBMap;		
		@Autowired List<Service> beanBList;		
		@Autowired Collection<Service> beanBCollection;		
	}	
	interface Service {}	
	static class ServiceA implements Service {}	
	static class ServiceB implements Service {}
	
	@Test
	public void atQualifier() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(QClient.class, QServiceA.class, QServiceB.class);
		QClient qclient = ac.getBean(QClient.class);
		assertThat(qclient.service, is(QServiceA.class));
	}
	
	static class QClient {
		@Autowired @Qualifier("main") Service service;
	}
	
	@Qualifier("main")
	static class QServiceA implements Service {}
	static class QServiceB implements Service {}
	
	
	@Test
	public void atInject() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(IClient.class, IServiceA.class, IServiceB.class);
		IClient iclient = ac.getBean(IClient.class);
		assertThat(iclient.service, is(IServiceA.class));
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Qualifier	// DIJ qualifier	
	@interface Main {}
	
	static class IClient {
		@Inject @Main Service service;
	} 
	
	@Main
	static class IServiceA implements Service {}
	static class IServiceB implements Service {}
	
}
