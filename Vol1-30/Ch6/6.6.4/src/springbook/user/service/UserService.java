package springbook.user.service;

import java.util.List;

import springbook.user.domain.User;

public interface UserService {
	void add(User user);
	void deleteAll();
	void update(User user);	
	User get(String id);
	List<User> getAll();
	
	void upgradeLevels();
}
