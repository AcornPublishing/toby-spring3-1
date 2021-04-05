package springbook.learningtest.spring.tx;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import springbook.learningtest.spring.jpa.Member;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JtaTxTest {
	private static final String LONG_STR = "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"; 

	@Resource MemberJdbcDao jdbcDao1;
	@Resource MemberJdbcDao jdbcDao2;
	@Autowired MemberHibernateDao hibernateDao;
	@Autowired PlatformTransactionManager transactionManager;
	
	@Test
	public void jtaTx() {
		jdbcDao1.deleteAll();
		jdbcDao2.deleteAll();
		hibernateDao.deleteAll();
		
		assertThat(jdbcDao1.count(), is(0L));
		assertThat(jdbcDao2.count(), is(0L));
		assertThat(hibernateDao.count(), is(0L));
		
		try {
			new TransactionTemplate(transactionManager).execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(TransactionStatus arg0) {
					jdbcDao1.add(new Member(1, "Spring", 1.2));		
					jdbcDao2.add(new Member(1, "Spring", 1.2));		
					hibernateDao.add(new Member(1, "Spring", 1.2));		
					assertThat(jdbcDao1.count(), is(1L));
					assertThat(jdbcDao2.count(), is(1L));
					assertThat(hibernateDao.count(), is(1L));
					
					jdbcDao1.add(new Member(2, LONG_STR, 1.2)); // 강제 예외 발생
				}
			});
			fail("DataAccessException expected");
		}
		catch(DataAccessException e) {
		}
		
		assertThat(jdbcDao1.count(), is(0L));
		assertThat(jdbcDao2.count(), is(0L));
		assertThat(hibernateDao.count(), is(0L));
	}
	
	public static class MemberJdbcDao extends JdbcDaoSupport {
		SimpleJdbcInsert insert;
		protected void initTemplateConfig() {
			insert = new SimpleJdbcInsert(getDataSource()).withTableName("member");
		}

		public void add(Member m) {
			insert.execute(new BeanPropertySqlParameterSource(m));
		}
		
		public void deleteAll() {
			getJdbcTemplate().execute("delete from member");
		}
		
		public long count() {
			return getJdbcTemplate().queryForObject("select count(*) from member", Long.class).longValue();
		}
	}
	
	@Repository
	public static class MemberJpaDao {
		@PersistenceContext
		EntityManager entityManager;
		
		public void add(Member m) {
			entityManager.persist(m);
			entityManager.flush();
		}
		
		public void deleteAll() {
			entityManager.createQuery("delete from Member").executeUpdate();
		}
		
		public long count() {
			return entityManager.createQuery("select count(m) from Member m", Long.class).getSingleResult();
		}
	}
	
	public static class MemberHibernateDao extends HibernateDaoSupport {
		public void add(Member m) {
			getHibernateTemplate().persist(m);
			getHibernateTemplate().flush();
		}
		
		public void deleteAll() {
			getHibernateTemplate().bulkUpdate("delete from Member");
		}
		
		public long count() {
			return (Long) getHibernateTemplate().execute(new HibernateCallback<Long>() {
				public Long doInHibernate(Session s) throws HibernateException, SQLException {
					return (Long) s.createQuery("select count(m) from Member m").uniqueResult();
				}
			});
		}
	}
}
