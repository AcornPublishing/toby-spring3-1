package springbook.learningtest.spring.jpa;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JpaTest {
	@Autowired PlatformTransactionManager transactionManager;
	@Autowired MemberDao dao;
	@Autowired MemberTemplateDao templateDao;
	@Autowired MemberRepositoryDao repositoryDao;
	
	@Test @Transactional 
	public void sharedEntityManager() {
		
		
		dao.em.createQuery("delete from Member").executeUpdate();
		Member m = new Member(10, "Spring", 7.8);
		dao.em.persist(m);
		Long count = dao.em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
		assertThat(count, is(1L));
	}
	
	@Test @Transactional
	public void jpaTemplate(){
		templateDao.jpaTemplate.execute(new JpaCallback<Object>() {
			public Object doInJpa(EntityManager entityManager) throws PersistenceException {
				entityManager.createQuery("delete from Member").executeUpdate();
				return null;
			}
		});
		
		Member m = new Member(11, "Spring", 8.9);
		templateDao.jpaTemplate.persist(m);
		Member m2 = templateDao.jpaTemplate.find(Member.class, 11);
		assertThat(m.id, is(m2.id));
		assertThat(m.name, is(m2.name));
		assertThat(m.point, is(m2.point));
		
		List<Member> ms = templateDao.jpaTemplate.execute(new JpaCallback<List<Member>>() {
			public List<Member> doInJpa(EntityManager entityManager) throws PersistenceException {
				return entityManager.createQuery("select m from Member m").getResultList();
			}
		});
		System.out.println(ms.size());
	}
	
	@Test 
	public void entityManagerFactory() {
		EntityManager em = dao.emf.createEntityManager();
		em.getTransaction().begin();
		em.createQuery("delete from Member").executeUpdate();
		Member m = new Member(10, "Spring", 7.8);
		em.persist(m);
		Long count = em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
		assertThat(count, is(1L));
		em.getTransaction().commit();
	}
	
	@Test(expected=PersistenceException.class) @Transactional
	public void jpaApiException() {
		dao.addDuplicatedId();
	}
	
	@Test(expected=DataAccessException.class) @Transactional 
	public void jpaTemplateException() {
		templateDao.addDuplicatedId();
	}
	
	@Test(expected=DataAccessException.class) @Transactional 
	public void jpaApiRepositoryException() {
		repositoryDao.addDuplicatedId();
	}
	
	public static class MemberDao {
		@PersistenceContext(type=PersistenceContextType.EXTENDED)
		EntityManager em;
		
		@PersistenceUnit
		EntityManagerFactory emf;
		
		public void addDuplicatedId() {
			em.persist(new Member(10, "Spring", 7.8));
			em.persist(new Member(10, "Spring", 7.8));
			em.flush();
		}
	}
	
	@Repository
	public static class MemberRepositoryDao {
		@PersistenceContext
		EntityManager em;
		
		public void addDuplicatedId() {
			em.persist(new Member(10, "Spring", 7.8));
			em.persist(new Member(10, "Spring", 7.8));
			em.flush();
		}
	}
	
	public static class MemberTemplateDao {
		JpaTemplate jpaTemplate;

		@Autowired
		public void init(EntityManagerFactory emf) {
			jpaTemplate = new JpaTemplate(emf);
		}
		
		public void addDuplicatedId() {
			jpaTemplate.persist(new Member(10, "Spring", 7.8));
			jpaTemplate.persist(new Member(10, "Spring", 7.8));
			jpaTemplate.flush();
		}
	}
}
