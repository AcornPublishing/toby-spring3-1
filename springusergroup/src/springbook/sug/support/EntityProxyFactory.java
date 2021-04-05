package springbook.sug.support;

import springbook.sug.dao.GenericDao;


public interface EntityProxyFactory {
	<T> T createProxy(Class<T> clazz, GenericDao<T> dao, int id);
}
