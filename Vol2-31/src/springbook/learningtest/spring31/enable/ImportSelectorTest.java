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
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class ImportSelectorTest {
	@Test
	public void simple() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		Hello h = ac.getBean(Hello.class);
		h.sayHello();
		assertThat(h.getHello(), is("Hello Spring1"));
	}
	
	@Configuration
	@EnableHello(mode="mode1")
	public static class AppConfig {
	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Import(HelloSelector.class)
	public static @interface EnableHello {
		String mode();
	}
	
	public static class HelloSelector implements ImportSelector {
		@Override
		public String[] selectImports(AnnotationMetadata importingClassMetadata) {
			String mode = 
				(String) importingClassMetadata.getAnnotationAttributes(EnableHello.class.getName())
				.get("mode");
			if ("mode1".equals(mode)) {
				return new String[] { HelloConfig1.class.getName() };
			}
			else {
				return new String[] { HelloConfig2.class.getName() };
			}
		}
	}
	
	@Configuration
	public static class HelloConfig1 {
		@Bean 
		Hello hello() {
			Hello h = new Hello();
			h.setName("Spring1");
			return h;
		}
	}
	
	@Configuration
	public static class HelloConfig2 {
		@Bean 
		Hello hello() {
			Hello h = new Hello();
			h.setName("Spring2");
			return h;
		}
	}
}
