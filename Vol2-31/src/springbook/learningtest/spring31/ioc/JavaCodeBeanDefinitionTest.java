package springbook.learningtest.spring31.ioc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Repository;

import springbook.learningtest.spring31.ioc.scanner.dao.MyDao;
import springbook.learningtest.spring31.ioc.scanner.service.MyService;
import springbook.learningtest.spring31.ioc.scanner.service.ServiceMarker;

public class JavaCodeBeanDefinitionTest {
	@Test
	public void componentScan() {
		// basePackages
		AnnotationConfigApplicationContext ac1 = new AnnotationConfigApplicationContext(C1.class);
		assertThat(ac1.getBean(MyDao.class), is(notNullValue()));
		assertThat(ac1.getBean(MyService.class), is(notNullValue()));
		
		// excludes
		AnnotationConfigApplicationContext ac2 = new AnnotationConfigApplicationContext(C2.class);
		try {
			ac2.getBean(MyDao.class);
			fail();
		}
		catch(NoSuchBeanDefinitionException e) {}
		assertThat(ac2.getBean(MyService.class), is(notNullValue()));
		
		// excludes
		AnnotationConfigApplicationContext ac3 = new AnnotationConfigApplicationContext(C3.class);
		try {
			ac3.getBean(MyDao.class);
			fail();
		}
		catch(NoSuchBeanDefinitionException e) {}
		assertThat(ac3.getBean(MyService.class), is(notNullValue()));
		
		// basePackageClasses
		AnnotationConfigApplicationContext ac4 = new AnnotationConfigApplicationContext(C4.class);
		try {
			ac4.getBean(MyDao.class);
			fail();
		}
		catch(NoSuchBeanDefinitionException e) {}
		assertThat(ac4.getBean(MyService.class), is(notNullValue()));
		
	}

	@Configuration
	@ComponentScan("springbook.learningtest.spring31.ioc.scanner")
	static class C1 {}
	
	@Configuration
	@ComponentScan(basePackages={"springbook.learningtest.spring31.ioc.scanner"},
		excludeFilters=@Filter(Repository.class)
	)
	static class C2 {}
	
	@Configuration
	@ComponentScan(basePackages={"springbook.learningtest.spring31.ioc.scanner"},
		excludeFilters=@Filter(type=FilterType.ASSIGNABLE_TYPE, value=MyDao.class)
	)
	static class C3 {} 
	
	
	@Configuration
	@ComponentScan(basePackageClasses={ServiceMarker.class})
	static class C4 {} 
	
	@Test
	public void atImport() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		ac.getBean(DataConfig.class);
		ac.getBean(AppConfig.class);
	}
	
	@Configuration
	@Import(DataConfig.class)
	static class AppConfig {
	}
	
	@Configuration
	static class DataConfig {
	}
	
	@Test
	public void atImportResource() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(MainConfig.class);
		assertThat((String)ac.getBean("name"), is("Toby"));
	}
	
	@Configuration
	@ImportResource("springbook/learningtest/spring31/ioc/extra.xml")
	static class MainConfig {
	}
	
}
