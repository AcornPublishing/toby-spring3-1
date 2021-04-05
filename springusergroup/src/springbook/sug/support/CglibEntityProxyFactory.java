package springbook.sug.support;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import springbook.sug.dao.GenericDao;

public class CglibEntityProxyFactory implements EntityProxyFactory {
	@SuppressWarnings("unchecked")
	public <T> T createProxy(Class<T> clazz, final GenericDao<T> dao, final int id) {
		Enhancer e = new Enhancer();
		e.setSuperclass(clazz);
		e.setCallback(new MethodInterceptor() {
			private T entity;
			public Object intercept(Object obj, Method method, Object[] args,
					MethodProxy proxy) throws Throwable {
				if (method.getName().equals("getId")) {
					return id;
				}
				else {
					if (entity == null) {
						entity = dao.get(id);
					}
					return proxy.invoke(entity, args);
				}
			}
		});
		
		return (T)e.create();
	}
}
