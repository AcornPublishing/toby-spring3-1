package springbook.learningtest.spring31.ioc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

public class PropertySourceTest {
	@Test
	public void standardEnviroment() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
		ac.refresh();
		
		for(PropertySource<?> ps : ac.getEnvironment().getPropertySources()) {
			System.out.println(ps.getName());
		}
		
		System.out.println(ac.getEnvironment().getProperty("os.name"));
		System.out.println(ac.getEnvironment().getProperty("Path"));
	}
	
	@Test
	public void addPropertySource() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		
		Properties p = new Properties();
		p.put("db.username", "spring");		
		PropertySource<?> ps = new PropertiesPropertySource("customPropertySource", p);
		
		ac.getEnvironment().getPropertySources().addFirst(ps);
		
		assertThat(ac.getEnvironment().getProperty("db.username"), is("spring"));
	}
	
	@Configuration
	public static class AppConfig {
	}
	
	@Test
	public void pspc() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
		ac.register(AppConfig2.class);
		
		Properties p = new Properties();
		p.put("db.username", "spring");		
		PropertySource<?> ps = new PropertiesPropertySource("customPropertySource", p);
		
		ac.getEnvironment().getPropertySources().addFirst(ps);		
		ac.refresh();
		
		assertThat(ac.getBean(AppConfig2.class).username, is("spring"));
		assertThat(ac.getBean(SimpleDS.class).username, is("spring"));
		
		assertThat(ac.getBean(AppConfig2.class).password, is("book"));
	}
	
	@Configuration
	@ImportResource("springbook/learningtest/spring31/ioc/appconfig2.xml")
	@org.springframework.context.annotation.PropertySource(name="DB Settings", value={"springbook/learningtest/spring31/ioc/appconfig2.properties"})
	public static class AppConfig2 {
		@Value("${db.username}") String username;
		@Value("${db.password}") String password;
		
		@Bean
		public static PropertySourcesPlaceholderConfigurer pspc() {
			return new PropertySourcesPlaceholderConfigurer();
		}
		
	}
	
	public static class SimpleDS {
		String username;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
	}
	
}
