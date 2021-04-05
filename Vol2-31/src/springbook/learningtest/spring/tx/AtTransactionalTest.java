package springbook.learningtest.spring.tx;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import springbook.learningtest.spring.jpa.Member;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AtTransactionalTest {
	private static final String LONG_STR = "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
	
	MemberDao dao;
	@Autowired 
	private void init(MemberDao memberDao) {
		this.dao = memberDao;
	}
	
	@Test
	public void tx() {
		dao.deleteAll();
		assertThat(dao.count(), is(0L));

		dao.add( Arrays.asList(new Member[] {
				new Member(1, "Spring", 1.2),		
				new Member(2, "Spring", 1.2)
		}));
		
		assertThat(dao.count(), is(2L));

		try {
			dao.add( Arrays.asList(new Member[] {
					new Member(3, "Spring", 1.2),		
					new Member(4, LONG_STR, 1.2)
			}));
			fail();
		}
		catch(DataAccessException e) {}
		
		assertThat(dao.count(), is(2L));
	}
	
	public interface MemberDao {
		public void add(Member m);
		public void add(List<Member> members);
		public void deleteAll();
		public long count();
	}
	
	@Transactional
	public static class MemberDaoImpl extends JdbcDaoSupport implements MemberDao {
		SimpleJdbcInsert insert;
		public void initTemplateConfig() {
			insert = new SimpleJdbcInsert(getDataSource()).withTableName("member");
		}
		
		public void add(Member m) {
			insert.execute(new BeanPropertySqlParameterSource(m));
		}
		
		public void add(List<Member> members) {
			for(Member m : members) add(m);
		}
		
		public void deleteAll() {
			getJdbcTemplate().execute("delete from member");
		}
		
		@Transactional(readOnly=true)
		public long count() {
			return getJdbcTemplate().queryForObject("select count(*) from member", Long.class).longValue();
		}
	}
}
