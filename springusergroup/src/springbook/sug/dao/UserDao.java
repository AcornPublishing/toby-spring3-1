package springbook.sug.dao;

import springbook.sug.domain.User;

public interface UserDao extends GenericDao<User> {
	User findUser(String username);
}