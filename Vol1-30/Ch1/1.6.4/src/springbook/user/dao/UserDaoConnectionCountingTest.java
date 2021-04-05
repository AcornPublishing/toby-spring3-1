package springbook.user.dao;

import java.sql.SQLException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.domain.User;

public class UserDaoConnectionCountingTest {
	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		AnnotationConfigApplicationContext context = 
			new AnnotationConfigApplicationContext(CountingDaoFactory.class);		
		UserDao dao = context.getBean("userDao", UserDao.class);

		for(int i=0; i<10; i++) {
			User user = new User();
			user.setId(""+i);
			user.setName(""+i);
			user.setPassword(""+i);
			dao.add(user);
		}

		CountingConnectionMaker ccm =  context.getBean("connectionMaker", CountingConnectionMaker.class);
		System.out.println("Connection counter : " + ccm.getCounter());		
	}
}
