package springbook.sug.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

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
public class DataSourceTest {
	@Autowired DataSource dataSource;
	@Autowired PlatformTransactionManager transactionManager;

	@Test
	public void connection() throws SQLException {
		Connection c = null;
		try {
			c = dataSource.getConnection();
			assertThat(c.isClosed(), is(false));
		}
		finally {
			if (c != null) c.close();
		}
	}
	
	@Test
	public void transaction() throws Exception {
		TransactionStatus tx = transactionManager.getTransaction(new DefaultTransactionDefinition());
		transactionManager.commit(tx);
		assertThat(tx.isCompleted(), is(true));
	}
}
