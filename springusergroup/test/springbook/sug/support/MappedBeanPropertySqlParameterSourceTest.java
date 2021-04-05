package springbook.sug.support;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import springbook.sug.domain.Group;
import springbook.sug.domain.Type;
import springbook.sug.domain.User;

public class MappedBeanPropertySqlParameterSourceTest {
	private User user;
	
	@Before
	public void before() {
		user = new User(1, "name", "username", "password", Type.GUEST, new Group(1, "group"),
				new GregorianCalendar(2000, 1-1, 10).getTime(),
				new GregorianCalendar(2000, 2-1, 20).getTime(), 100);	
	}
	
	@Test
	public void beanPropertySqlParameterSource() {
		SqlParameterSource sps = new BeanPropertySqlParameterSource(user);
		assertThat((String)sps.getValue("name"), is("name"));
		assertThat((Integer)sps.getValue("id"), is(1));
		assertThat((Integer)sps.getValue("type.value"), is(3));
		assertThat((String)sps.getValue("group.name"), is("group"));
	}
	
	@Test
	public void mapMethod() {
		SqlParameterSource sps = new MappedBeanPropertySqlParameterSource(user)
									.map("type","type.value").map("group", "group.name");
		checkSource(sps);
	}
	
	@Test
	public void propertyMapConstructorParam() {
		Map<String, String> propertyMap = new HashMap<String, String>();
		propertyMap.put("type","type.value");
		propertyMap.put("group", "group.name");
		SqlParameterSource sps = new MappedBeanPropertySqlParameterSource(user, propertyMap);
		checkSource(sps);
	}
	
	@Test
	public void getReadablePropertyNames() {
		BeanPropertySqlParameterSource sps = new MappedBeanPropertySqlParameterSource(user);

		Map<String, String> propertyMap = new HashMap<String, String>();
		propertyMap.put("type","type.value");
		propertyMap.put("group", "group.name");
		assertThat(sps.getReadablePropertyNames().length, is(10));
		BeanPropertySqlParameterSource mappedSps = new MappedBeanPropertySqlParameterSource(user, propertyMap);
		
		assertThat(mappedSps.getReadablePropertyNames().length, is(12));
		assertThat(mappedSps.getReadablePropertyNames()[10], is("group"));
		assertThat(mappedSps.getReadablePropertyNames()[11], is("type"));
	}

	private void checkSource(SqlParameterSource sps) {
		assertThat((String)sps.getValue("name"), is("name"));
		assertThat((Integer)sps.getValue("id"), is(1));
		assertThat((Integer)sps.getValue("type"), is(3));
		assertThat((String)sps.getValue("group"), is("group"));
	}
}
