package springbook.sug.dao;

import java.util.List;

public interface GenericDao<T> {
	T add(T entity);

	T update(T entity);

	void delete(int id);

	T get(int id);

	List<T> search(String name);
	
	int deleteAll();

	List<T> getAll();
	
	long count();
}
