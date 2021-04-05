package springbook.sug.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springbook.sug.dao.UserDao;
import springbook.sug.domain.User;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	private UserDao userDao;

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public User add(User user) {
		user.initDates();
		return this.userDao.add(user);
	}

	public void delete(int id) {
		this.userDao.delete(id);
	}

	public User get(int id) {
		return this.userDao.get(id);
	}

	public User update(User user) {
		user.setModified(new Date());
		return this.userDao.update(user);
	}

	public User findUser(String username) {
		return this.userDao.findUser(username);
	}

	public List<User> getAll() {
		return this.userDao.getAll();
	}

	public void login(User user) {
		user.logIn();
		this.userDao.update(user);
	}
}
