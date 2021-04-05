package springbook.sug.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

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
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;

import springbook.sug.dao.GroupDao;
import springbook.sug.dao.UserDao;
import springbook.sug.domain.Group;
import springbook.sug.domain.Type;
import springbook.sug.domain.User;


/**
 * 사용자  수정 통합 테스트 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/test-applicationContext.xml", "/test-springusergroup-servlet.xml"})
@Transactional
public class UserEditControllerTest {
	@PersistenceContext EntityManager em;
	@Autowired UserDao userDao;
	@Autowired GroupDao groupDao;
	@Autowired UserEditController userEditController;
	Group group1, group2;
	User user1, user2, user3;
	
	@Before
	public void before() {
		group1 = new Group(0, "group1");
		group2 = new Group(0, "group2");
		Date day1 = new GregorianCalendar(2010, 1-1, 1).getTime();
		Date day2 = new GregorianCalendar(2010, 2-1, 1).getTime();
		Date day3 = new GregorianCalendar(2010, 3-1, 1).getTime();
		user1 = new User(0, "name1", "username1", "password1", Type.ADMIN, group1, day1, day2, 1); 
		user2 = new User(0, "name2", "username2", "password2", Type.GUEST, group2, day2, day3, 10); 
		user3 = new User(0, "name3", "username3", "password3", Type.MEMBER, group2, day1, day3, 100);
	}
	
	private void init() {
		groupDao.deleteAll();
		userDao.deleteAll();
		groupDao.add(group1);
		groupDao.add(group2);
		em.flush();
		userDao.add(user1);
		userDao.add(user2);
		userDao.add(user3);
	}
	
	@Test
	public void showForm() {
		init();
		ModelMap model = new ExtendedModelMap();
		assertThat(userEditController.showform(user1.getId(), model), is("user/edit"));
		assertThat((User)model.get("user"), is(user1));
		assertThat(userEditController.showform(user2.getId(), model), is("user/edit"));
		assertThat((User)model.get("user"), is(user2));
	}
	
	@Test
	public void duplicateUsername() {
		init();
		user1.setUsername(user2.getUsername());
		BindingResult result = new BeanPropertyBindingResult(user1, "user");
		SessionStatus status = mock(SessionStatus.class);
		assertThat(userEditController.edit(user1, result, status), is("user/edit"));
		assertThat(result.hasFieldErrors("username"), is(true));
	}
	
	@Test
	public void editSuccess() {
		init();
		user1.setUsername("modified");
		BindingResult result = new BeanPropertyBindingResult(user1, "user");
		SessionStatus status = mock(SessionStatus.class);
		assertThat(userEditController.edit(user1, result, status), is("redirect:../list"));
		assertThat(result.hasErrors(), is(false));
		
		User user = userDao.get(user1.getId());
		assertThat(user.getUsername(), is(user1.getUsername()));
	}
}
