package springbook.learningtest.spring31.enable;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

public class ImportAwareTest {
	@Test
	public void simple() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		Hello h = ac.getBean(Hello.class);
		h.sayHello();
		assertThat(h.getHello(), is("Hello Toby"));
	}
	
	@Configuration
	@EnableHello(name="Toby")
	public static class AppConfig {
	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Import(HelloConfig.class)
	public static @interface EnableHello {
		String name();
	}
	
	@Configuration
	public static class HelloConfig implements ImportAware {
		@Bean 
		Hello hello() {
			Hello h = new Hello();
			h.setName("Spring");
			return h;
		}
		
		@Override
		public void setImportMetadata(AnnotationMetadata importMetadata) {
			Map<String, Object> elements = importMetadata.getAnnotationAttributes(EnableHello.class.getName());
			String name = (String) elements.get("name");
			hello().setName(name);
		}
	}
}
