package springbook.learningtest.jdk.properites;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.junit.*;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class PropertiesTest {
	@Test
	public void propertiesFile() throws IOException {
		Properties p = new Properties();
		p.load(new ClassPathResource("test.properties", getClass()).getInputStream());
		
		assertThat(p.getProperty("name"), is("토비"));
		assertThat(p.getProperty("age"), is("17"));
	}
	
	@Test
	public void propertiesXml() throws IOException {
		Properties p = new Properties();
		p.loadFromXML(new ClassPathResource("test.xml", getClass()).getInputStream());
		
		assertThat(p.getProperty("name"), is("토비"));
		assertThat(p.getProperty("age"), is("17"));
	}
	
	@Test
	public void utilProperties() {
		GenericXmlApplicationContext ac = new GenericXmlApplicationContext(getClass(), "context.xml");
		Properties p = ac.getBean("dbProperties", Properties.class);
		Properties p2 = ac.getBean("dbPropertiesXml", Properties.class);

		assertThat(p.getProperty("name"), is("토비"));
		assertThat(p.getProperty("age"), is("17"));
		assertThat(p2.getProperty("name"), is("토비"));
		assertThat(p2.getProperty("age"), is("17"));
	}
	
	@Test
	public void environmentVar() {
		Properties a = System.getProperties();
		for(Object key : a.keySet()) {
			System.out.println(key);
		}
	}
}
