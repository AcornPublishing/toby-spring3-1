package springbook.learningtest.spring.web.atmvc;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CharsetEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;
import springbook.user.domain.Level;
public class BindingTest extends AbstractDispatcherServletTest {
	@Test
	public void defaultPropertyEditor() throws ServletException, IOException {
		setClasses(DefaultPEController.class);
		initRequest("/hello.do").addParameter("charset", "UTF-8");
		runService();
		assertModel("charset", Charset.forName("UTF-8"));
	}
	@Controller static class DefaultPEController {
		@RequestMapping("/hello") public void hello(@RequestParam Charset charset, Model model) {
			model.addAttribute("charset", charset);
		}
	}
	
	@Test
	public void charsetEditor() {
		CharsetEditor charsetEditor = new CharsetEditor();
		charsetEditor.setAsText("UTF-8");
		assertThat(charsetEditor.getValue(), is(instanceOf(Charset.class)));
		assertThat((Charset)charsetEditor.getValue(), is(Charset.forName("UTF-8")));
	}
	
	@Test
	public void levelPropertyEditor() {
		LevelPropertyEditor levelEditor = new LevelPropertyEditor();
		
		levelEditor.setValue(Level.BASIC);
		assertThat(levelEditor.getAsText(), is("1"));
		
		levelEditor.setAsText("3");
		assertThat((Level)levelEditor.getValue(), is(Level.GOLD));
	}
	@Test
	public void levelTypeParameter() throws ServletException, IOException {
		setClasses(SearchController.class);
		initRequest("/user/search.do").addParameter("level", "1");
		runService();
		assertModel("level", Level.BASIC);
	}
	@Controller static class SearchController {
		@InitBinder
		public void initBinder(WebDataBinder dataBinder) {
			dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
		}
		
		@RequestMapping("/user/search") public void search(@RequestParam Level level, Model model) {
			model.addAttribute("level", level);
		}
	}
	static class LevelPropertyEditor extends PropertyEditorSupport {
		public String getAsText() {
			return String.valueOf(((Level)this.getValue()).intValue());
		}

		public void setAsText(String text) throws IllegalArgumentException {
			this.setValue(Level.valueOf(Integer.parseInt(text.trim())));
		}
	}
	
	@Test
	public void webBindingInitializer() throws ServletException, IOException {
		setClasses(SearchController2.class, ConfigForWebBinidngInitializer.class);
		initRequest("/user/search").addParameter("level", "2");
		runService();
		assertModel("level", Level.SILVER);
	}
	@Controller static class SearchController2 {
		@RequestMapping("/user/search") public void search(@RequestParam Level level, Model model) {
			model.addAttribute("level", level);
		}
	}
	@Configuration static class ConfigForWebBinidngInitializer {
		@Bean public AnnotationMethodHandlerAdapter annotationMethodHandlerAdaptor() {
			return new AnnotationMethodHandlerAdapter() {{
				setWebBindingInitializer(webBindingInitializer());
			}};
		}
		
		@Bean public WebBindingInitializer webBindingInitializer() {
			return new WebBindingInitializer() {
				public void initBinder(WebDataBinder binder, WebRequest request) {
					binder.registerCustomEditor(Level.class,new LevelPropertyEditor());
				}
			};
		}
	}
	/*static class MyWebBindingInitializer implements WebBindingInitializer {
		public void initBinder(WebDataBinder binder, WebRequest request) {
			binder.registerCustomEditor(Level.class,new LevelPropertyEditor());
		}
	}*/
	
	@Test
	public void dataBinder() {
		WebDataBinder dataBinder = new WebDataBinder(null);
		dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
		assertThat(dataBinder.convertIfNecessary("1", Level.class), is(Level.BASIC));
	}
	
	@Test
	public void namedPropertyEditor() throws ServletException, IOException {
		setClasses(MemberController.class);
		initRequest("/add.do").addParameter("id", "10000").addParameter("age", "10000");
		runService();
		System.out.println(getModelAndView().getModel().get("member"));
	}
	@Controller static class MemberController {
		@InitBinder
		public void initBinder(WebDataBinder dataBinder) {
			dataBinder.registerCustomEditor(int.class, "age", new MinMaxPropertyEditor(0, 200));
		}
		@RequestMapping("/add") public void add(@ModelAttribute Member member) {
		}
	}
	static class Member {
		int id;
		int age;
		public int getId() { return id; }
		public void setId(int id) { this.id = id; }
		public int getAge() { return age; }
		public void setAge(int age) { this.age = age; }
		public String toString() { return "Member [age=" + age + ", id=" + id + "]"; }
		
	}
	static class MinMaxPropertyEditor extends PropertyEditorSupport {
		int min = Integer.MIN_VALUE;
		int max = Integer.MAX_VALUE;
		
		public MinMaxPropertyEditor(int min, int max) {
			this.min = min;
			this.max = max;
		}
		
		public MinMaxPropertyEditor() { }

		public String getAsText() {
			return String.valueOf((Integer)this.getValue());
		}

		public void setAsText(String text) throws IllegalArgumentException {
			Integer val = Integer.parseInt(text);
			if (val < min) val = min;
			else if (val > max) val = max;
			
			setValue(val);
		}
	}
	
	@Test
	public void webBindingInit() throws ServletException, IOException {
		setClasses(Config.class, UserController.class);
		initRequest("/add.do", "POST");
		addParameter("id", "1").addParameter("name", "Spring").addParameter("date", "02/03/01");
		addParameter("level", "3");
		runService();
	}
	@Controller static class UserController {
		@RequestMapping("/add") public void add(@ModelAttribute User user) {
			System.out.println(user);
		}
	}
	@Configuration static class Config {
		@Autowired FormattingConversionService conversionService;
		
		@Bean public AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter() {
			return new AnnotationMethodHandlerAdapter() {{
				setWebBindingInitializer(webBindingInit());
			}};
		}
		
		@Bean public WebBindingInitializer webBindingInit() {
			return new ConfigurableWebBindingInitializer() {{
				setConversionService(Config.this.conversionService);
			}};
		}
		
		@Bean public FormattingConversionServiceFactoryBean formattingConversionServiceFactoryBean() {
			return new FormattingConversionServiceFactoryBean() {{
//				setConverters(new LinkedHashSet(Arrays.asList(new Converter[] {new LabelToStringConverter(), new StringToLabelConverter()}))); // convert Àû¿ë
				}
				protected void installFormatters(FormatterRegistry registry) {
					super.installFormatters(registry);
					registry.addFormatterForFieldType(Level.class, new LabelStringFormatter());
				}
			};
		}
		
		// formatter
		static class LabelStringFormatter implements Formatter<Level> {
			public String print(Level level, Locale locale) {
				return String.valueOf(level.intValue());
			}

			public Level parse(String text, Locale locale) throws ParseException {
				return Level.valueOf(Integer.parseInt(text));
			}
		}
		
		// converter
		static class LevelToStringConverter implements Converter<Level, String> {
			public String convert(Level level) {
				return String.valueOf(level.intValue());
			}
		}
		
		static class StringToLevelConverter implements Converter<String, Level> {
			public Level convert(String text) {
				return Level.valueOf(Integer.parseInt(text));
			}
		}
	}
	static class User {
		int id;
		String name;
		Level level;
		@DateTimeFormat(pattern="dd/yy/MM")
		Date date;
		
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Level getLevel() {
			return level;
		}
		public void setLevel(Level level) {
			this.level = level;
		}
		@Override
		public String toString() {
			return "User [date=" + date + ", id=" + id + ", level=" + level + ", name=" + name + "]";
		}
	}
}
