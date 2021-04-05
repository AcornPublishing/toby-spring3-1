package springbook.sug.service;

import java.util.List;

import springbook.sug.domain.User;

public interface UserService extends GenericService<User> {
	User findUser(String username);
	List<User> getAll();
	void login(User user);
}
