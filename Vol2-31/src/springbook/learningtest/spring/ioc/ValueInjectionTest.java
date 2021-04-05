package springbook.learningtest.spring.ioc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;


public class ValueInjectionTest {
	@Test @Ignore
	public void valueInjection() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BeanSP.class, ConfigSP.class, DatabasePropertyPlaceHolder.class);
		BeanSP bean = ac.getBean(BeanSP.class); 
		assertThat(bean.name, is("Windows XP")); // OS에 맞게 수정해야 합니다.
		assertThat(bean.username, is("Spring"));
		
		assertThat(bean.hello.name, is("Spring"));
	}
	static class BeanSP {
		@Value("#{systemProperties['os.name']}") String name;
		@Value("${database.username}") String username;
		@Value("${os.name}") String osname;
		@Autowired Hello hello;
	}
	static class ConfigSP {
		@Bean public Hello hello(@Value("${database.username}") String username) {
			Hello hello = new Hello();
			hello.name = username;
			return hello;
		}
	}
	static class Hello { String name; }
	static class DatabasePropertyPlaceHolder extends PropertyPlaceholderConfigurer {
		public DatabasePropertyPlaceHolder() {
			this.setLocation(new ClassPathResource("database.properties", getClass()));
		}
	}
	
	@Test @Ignore
	public void importResource() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ConfigIR.class);
		BeanSP bean = ac.getBean(BeanSP.class);
		
		assertThat(bean.name, is("Windows XP"));
		assertThat(bean.username, is("Spring"));
	}
	@ImportResource("/springbook/learningtest/spring/ioc/properties2.xml")
	@Configuration
	static class ConfigIR {
		@Bean public BeanSP beanSp() {
			return new BeanSP();
		}
		@Bean public Hello hello() {
			return new Hello();
		}
	}
	
	@Test
	public void propertyEditor() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BeanPE.class);
		BeanPE bean = ac.getBean(BeanPE.class);
		assertThat(bean.charset, is(Charset.forName("UTF-8")));
		assertThat(bean.intarr, is(new int[] {1,2,3}));
		assertThat(bean.flag, is(true));
		assertThat(bean.rate, is(1.2));
		assertThat(bean.file.exists(), is(true));
	}
	static class BeanPE {
		@Value("UTF-8") Charset charset;
		@Value("1,2,3") int[] intarr;
		@Value("true") boolean flag;
		@Value("1.2") double rate;
		@Value("classpath:test-applicationContext.xml") File file;
	}
	
	@Test
	public void collectionInject() {
		ApplicationContext ac = new GenericXmlApplicationContext(new ClassPathResource("collection.xml", getClass()));
		BeanC bean = ac.getBean(BeanC.class);
		
		assertThat(bean.nameList.size(), is(3));
		assertThat(bean.nameList.get(0), is("Spring"));
		assertThat(bean.nameList.get(1), is("IoC"));
		assertThat(bean.nameList.get(2), is("DI"));
		
		assertThat(bean.nameSet.size(), is(3));
		
		assertThat(bean.ages.get("Kim"), is(30));
		assertThat(bean.ages.get("Lee"), is(35));
		assertThat(bean.ages.get("Ahn"), is(40));
		
		assertThat((String)bean.settings.get("username"), is("Spring"));
		assertThat((String)bean.settings.get("password"), is("Book"));
		
		assertThat(bean.beans.size(), is(2));
	}
	static class BeanC {
		List<String> nameList;		public void setNameList(List<String> names) {	this.nameList = names;	}
		Set<String> nameSet;		public void setNameSet(Set<String> nameSet) {	this.nameSet = nameSet;	}  
		Map<String, Integer> ages;	public void setAges(Map<String, Integer> ages) {this.ages = ages;	}
		Properties settings;		public void setSettings(Properties settings) {	this.settings = settings;	}
		List beans;					public void setBeans(List beans) {	this.beans = beans; }					
	}
}

