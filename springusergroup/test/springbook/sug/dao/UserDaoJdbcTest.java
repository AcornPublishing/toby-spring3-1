package springbook.sug.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import springbook.sug.domain.Group;
import springbook.sug.domain.Type;
import springbook.sug.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
@Transactional
public class UserDaoJdbcTest {
	@PersistenceContext EntityManager em;
	@Autowired UserDao userDao;
	@Autowired GroupDao groupDao;
	Group group1;
	Group group2;
	Date day1;
	Date day2;
	Date day3;
	User user1;
	User user2;
	User user3;
	
	@Before
	public void before() {
		group1 = new Group(0, "group1");
		group2 = new Group(0, "group2");
		day1 = new GregorianCalendar(2010, 1-1, 1).getTime();
		day2 = new GregorianCalendar(2010, 2-1, 1).getTime();
		day3 = new GregorianCalendar(2010, 3-1, 1).getTime();		
		user1 = new User(0, "name1", "username1", "password1", Type.ADMIN, group1, day1, day2, 1); 
		user2 = new User(0, "name2", "username2", "password2", Type.GUEST, group2, day2, day3, 10); 
		user3 = new User(0, "name3", "username3", "password3", Type.MEMBER, group2, day1, day3, 100);
	}
	
	private void init() {
		userDao.deleteAll();
		groupDao.deleteAll();
		groupDao.add(group1);
		groupDao.add(group2);
		em.flush();
		em.clear();
	}
	
	@Test
	public void crud() {
		init();
		
		// C
		assertThat(userDao.count(), is(0L));
		userDao.add(user1);
		assertThat(user1.getId() > 0, is(true));
		assertThat(userDao.count(), is(1L));		
		userDao.add(user2);
		assertThat(userDao.count(), is(2L));		
		userDao.add(user3);
		assertThat(userDao.count(), is(3L));
		
		// R
		User u1 = userDao.get(user1.getId());
		compareUserProperties(user1, u1);		
		User u2 = userDao.get(user2.getId());
		compareUserProperties(user2, u2);
		
		// U
		user1.setName("mName");
		user1.setUsername("mPassword");
		user1.setPassword("mPassword");
		user1.setCreated(new GregorianCalendar(1999,9-1,9).getTime());
		user1.setModified(new GregorianCalendar(1999,9-1,9).getTime());
		user1.setLogins(999);
		user1.setType(Type.GUEST);
		user1.setGroup(group2);		
		userDao.update(user1);
		User u3 = userDao.get(user1.getId());
		compareUserProperties(user1, u3);
		User u4 = userDao.get(user2.getId());
		compareUserProperties(user2, u4);
		
		// D
		userDao.delete(user1.getId());
		assertThat(userDao.count(), is(2L));
		assertThat(userDao.get(user1.getId()), is(nullValue()));
	}
	
	@Test
	public void findUser() {
		init();
		userDao.add(user1);
		userDao.add(user2);
		assertThat(userDao.findUser(user1.getUsername()), is(user1));
		assertThat(userDao.findUser(user2.getUsername()), is(user2));
		assertThat(userDao.findUser("asdf1234"), is(nullValue()));
	}

	private void compareUserProperties(User u1, User u2) {
		assertThat(u1.getId(), is(u2.getId()));
		assertThat(u1.getName(), is(u2.getName()));
		assertThat(u1.getUsername(), is(u2.getUsername()));
		assertThat(u1.getPassword(), is(u2.getPassword()));
		assertThat(u1.getType(), is(u2.getType()));
		assertThat(u1.getGroup(), is(u2.getGroup()));  
		assertThat(u1.getCreated(), is(u2.getCreated()));
		assertThat(u1.getModified(), is(u2.getModified()));
	}
}
