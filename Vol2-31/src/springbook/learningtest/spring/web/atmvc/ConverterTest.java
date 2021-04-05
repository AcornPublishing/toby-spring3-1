package springbook.learningtest.spring.web.atmvc;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;
import springbook.user.domain.Level;

public class ConverterTest extends AbstractDispatcherServletTest {
	@Test
	public void inheritedGenericConversionService() throws ServletException, IOException {
		setClasses(SearchController.class, MyConvertsionService.class);
		initRequest("/user/search.do").addParameter("level", "1");
		runService();
		assertModel("level", Level.BASIC);
	}
	@Controller public static class SearchController {
		@Autowired ConversionService conversionService;
		
		@InitBinder
		public void initBinder(WebDataBinder dataBinder) {
			dataBinder.setConversionService(this.conversionService);
		}
		
		@RequestMapping("/user/search") public void search(@RequestParam Level level, Model model) {
			model.addAttribute("level", level);
		}
	}
	static class MyConvertsionService extends GenericConversionService {{
		this.addConverter(new LevelToStringConverter());
		this.addConverter(new StringToLevelConverter());
	}}
	@SuppressWarnings("unchecked")
	static class MyConversionServiceFactoryBean extends ConversionServiceFactoryBean {{
		this.setConverters(new LinkedHashSet(Arrays.asList(
				new Converter[] { new LevelToStringConverter(), new StringToLevelConverter()} )));
	}}
	public static class LevelToStringConverter implements Converter<Level, String> {
		public String convert(Level level) {
			return String.valueOf(level.intValue());
		}
	}
	public static class StringToLevelConverter implements Converter<String, Level> {
		public Level convert(String text) {
			return Level.valueOf(Integer.parseInt(text));
		}
	}
	
	@Test 
	public void compositeGenericConversionService() throws ServletException, IOException {
		setRelativeLocations("conversionservice.xml");
		setClasses(SearchController.class);
		initRequest("/user/search.do").addParameter("level", "1");
		runService();
		assertModel("level", Level.BASIC);
	}
}
