package springbook.sug.support;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import springbook.sug.dao.GroupDao;
import springbook.sug.domain.Group;

public class EntityProxyFactoryTest {
	@Test
	public void createCglibProxy() {
		EntityProxyFactory ep = new CglibEntityProxyFactory(); 
		GroupDaoImpl mockGroupDao = new GroupDaoImpl();
		Group group = ep.createProxy(Group.class, mockGroupDao, 1);
		assertThat(group.getId(), is(1));
		assertThat(mockGroupDao.called, is(false));
		assertThat(group.getName(), is("name1"));
		assertThat(mockGroupDao.called, is(true));
	}
	
	static class GroupDaoImpl implements GroupDao {
		boolean called;
		
		public Group add(Group user) {
			throw new UnsupportedOperationException();
		}

		public long count() {
			throw new UnsupportedOperationException();
		}

		public void delete(int id) {
			throw new UnsupportedOperationException();
		}

		public int deleteAll() {
			throw new UnsupportedOperationException();
		}

		public Group get(int id) {
			called = true;
			return new Group(id, "name" + id);
		}

		public List<Group> getAll() {
			throw new UnsupportedOperationException();
		}

		public List<Group> search(String name) {
			throw new UnsupportedOperationException();
		}

		public Group update(Group user) {
			throw new UnsupportedOperationException();
		}
	}
}
