package springbook.learningtest.spring31.ioc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.stereotype.Component;

public class EnvironmentTest {
	@Test
	public void defaultProfile() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SimpleEnvConfig.class);
		assertThat(ac.getEnvironment().getDefaultProfiles().length, is(1));
		assertThat(ac.getEnvironment().getDefaultProfiles()[0], is("default"));
	}
	
	static List<String> applicationBeans(GenericApplicationContext ac) {
		List<String> beans = new ArrayList<String>();
		for(String name : ac.getBeanDefinitionNames()) {
			if (ac.getBeanDefinition(name).getRole() == BeanDefinition.ROLE_APPLICATION && name.indexOf("ImportAwareBeanPostProcessor") < 0) {
				beans.add(name);
			}
		}
		return beans;
	}
	
	@Test
	public void activeProfiles() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
		ac.register(ActiveProfileConfig.class);
		ac.refresh();
		assertThat(applicationBeans(ac).size(), is(1));
		assertThat(applicationBeans(ac).contains("environmentTest.ActiveProfileConfig"), is(true));
		
		ac = new AnnotationConfigApplicationContext();
		ac.register(ActiveProfileConfig.class);
		ac.getEnvironment().setActiveProfiles("p1");
		ac.refresh();
		assertThat(applicationBeans(ac).size(), is(3));
		
		ac = new AnnotationConfigApplicationContext();
		ac.register(ActiveProfileConfig.class);
		ac.getEnvironment().setActiveProfiles("p2");
		ac.refresh();
		assertThat(applicationBeans(ac).size(), is(2));
		assertThat(applicationBeans(ac).contains("environmentTest.ProfileConfig"), is(true));
		assertThat(applicationBeans(ac).contains("environmentTest.ProfileBean"), is(false));
	}
	
	@Configuration
	@Import({ProfileBean.class, ProfileConfig.class})
	static class ActiveProfileConfig {
	}
	
	@Component @Profile("p1")
	static class ProfileBean {
	}
	
	@Configuration @Profile({"p1", "p2"})	
	static class ProfileConfig {		
	}
	
	@Configuration
	static class SimpleEnvConfig {}
	
	@Test
	public void propertySourcePlaceholderConfigurer() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SimplePropertySource.class);
		SimplePropertySource sps = ac.getBean(SimplePropertySource.class);
		assertThat(sps.name, is("Toby"));
		
		Environment env = ac.getBean(Environment.class);
		assertThat(env.getProperty("name"), is("Toby"));
	}
	
	@Configuration
	@PropertySource("classpath:/springbook/learningtest/spring31/ioc/settings.properties")
	static class SimplePropertySource {
		@Autowired Environment env;
		@Value("${name}") String name;
		
		@Bean static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
			return new PropertySourcesPlaceholderConfigurer();
		}
	}
	
	public static class BeanA {
		String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	@Test
	public void embeddedDb() {
		GenericXmlApplicationContext gac = new GenericXmlApplicationContext(getClass(), "edb.xml");
		gac.getBean(DataSource.class);
	}
	
	@Test
	public void profiles() throws IllegalStateException, NamingException {
		// dev profile
		GenericXmlApplicationContext ac2 = new GenericXmlApplicationContext();
		ac2.getEnvironment().setActiveProfiles("dev");
		ac2.load(getClass(), "profile.xml");
		ac2.refresh();
		assertThat(ac2.getBean(DataSource.class), is(BasicDataSource.class));
		ac2.close();
		
		// production profile
		SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
		DriverManagerDataSource ds = new DriverManagerDataSource();
		builder.bind("jdbc/DefaultDS", ds);
		builder.activate();
		
		GenericXmlApplicationContext ac3 = new GenericXmlApplicationContext();
		ac3.getEnvironment().setActiveProfiles("production");
		ac3.load(getClass(), "profile.xml");
		ac3.refresh();
		assertThat(ac3.getBean(DataSource.class), is(DriverManagerDataSource.class));
		ac3.close();
	}
	
	@Test
	public void activeProfile() {
		// system property 
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "dev");
		
		GenericXmlApplicationContext ac2 = new GenericXmlApplicationContext();
		ac2.load(getClass(), "profile.xml");
		ac2.refresh();
		assertThat(ac2.getBean(DataSource.class), is(BasicDataSource.class));
		ac2.close();
	}
	
	// 이 테스트는 테스트 실행 환경에 VM 파라미터로 -Dspring.profiles.active=dev를 지정하고 수행해야 한다.
//	@Test
//	public void activeProfileCommandLineParameter() {
//		GenericXmlApplicationContext ac2 = new GenericXmlApplicationContext();
//		ac2.load(getClass(), "profile.xml");
//		ac2.refresh();
//		assertThat(ac2.getBean(DataSource.class), is(BasicDataSource.class));
//		ac2.close();
//	}
	
}
