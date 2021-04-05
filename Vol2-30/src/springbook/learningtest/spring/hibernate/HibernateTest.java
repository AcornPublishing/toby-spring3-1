package springbook.learningtest.spring.hibernate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class HibernateTest {
	@Autowired MemberDao dao;
	@Autowired MemberTemplateDao templateDao;
	
	@Test @Transactional
	public void hibernateTemplate() {
		templateDao.hibernateTemplate.bulkUpdate("delete from Member");
		Member m = new Member(1, "Hibernate", 1.2);
		templateDao.hibernateTemplate.save(m);
		long count = templateDao.hibernateTemplate.execute(new HibernateCallback<Long>() {
			public Long doInHibernate(Session s) throws HibernateException, SQLException {
				return (Long) s.createQuery("select count(m) from Member m").uniqueResult();
			}}); 
		assertThat(count, is(1L));
	}
	
	@Test @Transactional
	public void hiberateApi(){
		Session s = dao.sessionFactory.getCurrentSession();		
		s.createQuery("delete from Member").executeUpdate();
		Member m = new Member(1, "Hibernate", 1.2);
		s.save(m);
		long count = (Long) s.createQuery("select count(m) from Member m").uniqueResult();
		assertThat(count, is(1L));
	}
	
	public static class MemberTemplateDao {
		private HibernateTemplate hibernateTemplate;

		public void setSessionFactory(SessionFactory sessionFactory) {
			hibernateTemplate = new HibernateTemplate(sessionFactory);
		}
	}
	
	public static class MemberDao {
		@Autowired
		SessionFactory sessionFactory;
	}
	
	@Test
	public void localSessionFactoryBean() {
		ApplicationContext ac = new GenericXmlApplicationContext(new ClassPathResource("localsessionfactorybean-context.xml", getClass()));
		SessionFactory sf = ac.getBean(SessionFactory.class);
		Session s = sf.openSession();
		Transaction tx = s.beginTransaction();
		
		s.createQuery("delete from Member").executeUpdate();
		Member m = new Member(1, "Hibernate", 1.2);
		s.save(m);
		long count = (Long) s.createQuery("select count(m) from Member m").uniqueResult();
		assertThat(count, is(1L));
		
		tx.commit();
		s.close();
		sf.close();
	}
}
