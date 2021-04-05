package springbook.sug.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
@Transactional
public class EntityManagerTest {
	@PersistenceContext EntityManager em;
	@Autowired PlatformTransactionManager transactionManager;
	
	@Test
	public void em() {
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		assertThat(em.isOpen(), is(true));
		transactionManager.commit(status);
	}
}
