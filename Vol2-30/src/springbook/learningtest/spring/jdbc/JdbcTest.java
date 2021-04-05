package springbook.learningtest.spring.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JdbcTest {
	@Autowired DataSource dataSource;
	
	SimpleJdbcInsert sji;
	SimpleJdbcTemplate sjt;
	
	@BeforeClass
	public static void init() throws IllegalStateException, NamingException, SQLException {
		SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
		SimpleDriverDataSource ds = new SimpleDriverDataSource(new com.mysql.jdbc.Driver(), "jdbc:mysql://localhost/springbook?characterEncoding=UTF-8", "spring", "book");
		builder.bind("jdbc/DefaultDS", ds);
		builder.activate();
	}
	
	@Before
	public void before() {
		sjt = new SimpleJdbcTemplate(dataSource);		
		sji = new SimpleJdbcInsert(dataSource).withTableName("member");
	}
	
	@Test
	public void simeJdbcTemplate() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(SimpleDao.class);
		SimpleDao dao = ac.getBean(SimpleDao.class);
		dao.deleteAll();
		
		// update()
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id", 1);
		m.put("name", "Spring");
		m.put("point", 3.5);
		dao.insert(m);
		dao.insert(new MapSqlParameterSource().addValue("id", 2).addValue("name", "Book").addValue("point", 10.1));
		dao.insert(new Member(3, "Jdbc", 20.5));
		
		// queryForInt()
		assertThat(dao.rowCount(), is(3));
		assertThat(dao.rowCount(5), is(2));
		assertThat(dao.rowCount(1), is(3));
		
		// queryForObject(Class)
		assertThat(dao.name(1), is("Spring"));
		assertThat(dao.point(1), is(3.5));
		
		// queryForObject(RowMapper)
		Member mret = dao.get(1);
		assertThat(mret.id, is(1));
		assertThat(mret.name, is("Spring"));
		assertThat(mret.point, is(3.5));
		
		// query(RowMapper)
		assertThat(dao.find(1).size(), is(3));
		assertThat(dao.find(5).size(), is(2));
		assertThat(dao.find(100).size(), is(0));
		
		// queryForMap
		Map<String, Object> mmap = dao.getMap(1);
		assertThat((Integer)mmap.get("id"), is(1));
		assertThat((String)mmap.get("name"), is("Spring"));
		assertThat((Double)mmap.get("point"), is(3.5));
	
		// batchUpdates()
		Map<String, Object>[] paramMaps = new HashMap[2];
		paramMaps[0] = new HashMap<String, Object>();
		paramMaps[0].put("id", 1);
		paramMaps[0].put("name", "Spring2");
		paramMaps[1] = new HashMap<String, Object>();
		paramMaps[1].put("id", 2);
		paramMaps[1].put("name", "Book2");
		dao.updates(paramMaps);		
		
		assertThat(dao.name(1), is("Spring2"));
		assertThat(dao.name(2), is("Book2"));
		
		dao.simpleJdbcTemplate.batchUpdate("update member set name = :name where id = :id",
			new SqlParameterSource[] {
				new MapSqlParameterSource().addValue("id", 1).addValue("name", "Spring3"),
				new BeanPropertySqlParameterSource(new Member(2, "Book3", 0))
			}
		);
		assertThat(dao.name(1), is("Spring3"));
		assertThat(dao.name(2), is("Book3"));
	}
	static class SimpleDao {
		SimpleJdbcTemplate simpleJdbcTemplate;
		
		@Autowired public void init(DataSource dataSource) {
			this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
		}

		public void updates(Map<String,Object>[] maps) { this.simpleJdbcTemplate.batchUpdate("update member set name = :name where id = :id", maps); }
		public Map<String, Object> getMap(int id) { return this.simpleJdbcTemplate.queryForMap("select * from member where id = ?", id); }
		public List<Member> find(double point) {
			return this.simpleJdbcTemplate.query("select * from member where point > ?", new BeanPropertyRowMapper<Member>(Member.class), point);
		}
		public Member get(int id) { 
			return this.simpleJdbcTemplate.queryForObject("select * from member where id = ?", new BeanPropertyRowMapper<Member>(Member.class), id);
		}
		public String name(int id) { return this.simpleJdbcTemplate.queryForObject("select name from member where id = ?", String.class, id); }
		public double point(int id) { return this.simpleJdbcTemplate.queryForObject("select point from member where id = ?", Double.class, id); }
		public int rowCount() { return this.simpleJdbcTemplate.queryForInt("select count(*) from member"); }
		public int rowCount(double min) { 
			return this.simpleJdbcTemplate.queryForInt("select count(*) from member where point > :min", new MapSqlParameterSource("min", min)); }
		
		public void deleteAll() { this.simpleJdbcTemplate.update("delete from member"); 	}		
		public void insert(Map map) { this.simpleJdbcTemplate.update("INSERT INTO MEMBER(ID, NAME, POINT) VALUES(:id, :name, :point)", map); }
		public void insert(Member m) {
			this.simpleJdbcTemplate.update("INSERT INTO MEMBER(ID, NAME, POINT) VALUES(:id, :name, :point)", new BeanPropertySqlParameterSource(m));
		}
		public void insert(SqlParameterSource m) {
			this.simpleJdbcTemplate.update("INSERT INTO MEMBER(ID, NAME, POINT) VALUES(:id, :name, :point)", m);
		}
		
		@Bean public DataSource dataSource() {
			try {
				return new SimpleDriverDataSource(new com.mysql.jdbc.Driver(), "jdbc:mysql://localhost/springbook?characterEncoding=UTF-8", "spring", "book");
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	@Test
	public void simpleJdbcInsert() {
		sjt.update("delete from member");
		
		SimpleJdbcInsert memberInsert = new SimpleJdbcInsert(dataSource).withTableName("member");
		Member member = new Member(1, "Spring", 3.5);
		memberInsert.execute(new BeanPropertySqlParameterSource(member));
	}
	
	@Test
	public void simpleJdbcInsertWithGeneratedKey() {
		sjt.update("delete from register");
		
		SimpleJdbcInsert registerInsert = new SimpleJdbcInsert(dataSource).withTableName("register").usingGeneratedKeyColumns("id");
		int key = registerInsert.executeAndReturnKey(new MapSqlParameterSource("name", "Spring")).intValue();
		System.out.println(key);
	}
	
	@Test 
	public void simpleJdbcInsertWithSqlParamSource() {
		sjt.update("delete from member");
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("id", 1)
				.addValue("name", "Spring")
				.addValue("point", 10.5);
		sji.execute(paramSource);
		
		Member m = new Member(2, "Jdbc", 3.3);
		sji.execute(new BeanPropertySqlParameterSource(m));
		
		SimpleJdbcTemplate sjt = new SimpleJdbcTemplate(dataSource);
		List<Map<String, Object>> list = sjt.queryForList("select * from member order by id");
		
		assertThat(list.size(), is(2));		
		
		assertThat((Integer)list.get(0).get("id"), is(1));
		assertThat((String)list.get(0).get("name"), is("Spring"));
		assertThat((Double)list.get(0).get("point"), is(10.5));
		
		assertThat((Integer)list.get(1).get("id"), is(2));
		assertThat((String)list.get(1).get("name"), is("Jdbc"));
		assertThat((Double)list.get(1).get("point"), is(3.3));
	}
	
	@Test  
	public void simpleJdbcCall() {
		sjt.update("delete from member");
		sjt.update("insert into member(id, name, point) values(1, 'Spring', 0)");
		
		SimpleJdbcCall sjc = new SimpleJdbcCall(dataSource).withFunctionName("find_name");
		String ret = sjc.executeFunction(String.class, 1);
		assertThat(ret, is("Spring"));
	}
}






