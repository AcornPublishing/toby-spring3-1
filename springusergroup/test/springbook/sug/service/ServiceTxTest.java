package springbook.sug.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import springbook.sug.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
@Transactional
public class ServiceTxTest {
	@Autowired UserService userService;
	@Autowired GroupService groupService;
	@Autowired UserDao userDao;
	
	@Test 
	public void proxy() {
		assertThat(AopUtils.isAopProxy(userService), is(true));
		assertThat(AopUtils.isAopProxy(groupService), is(true));
		assertThat(AopUtils.isAopProxy(userDao), is(false));
	}
}
