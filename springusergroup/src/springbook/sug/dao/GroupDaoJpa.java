package springbook.sug.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import springbook.sug.domain.Group;

@Repository
public class GroupDaoJpa implements GroupDao {
	@PersistenceContext EntityManager em;
	
	public Group add(Group user) {
		em.persist(user);
		return user;
	}

	public long count() {
		return (Long) em.createQuery("select count(g) from Group g").getSingleResult();
	}

	public void delete(int id) {
		em.remove(get(id));
	}

	public int deleteAll() {
		return em.createQuery("delete from Group").executeUpdate();
	}

	public Group get(int id) {
		return em.find(Group.class, id);
	}

	public List<Group> getAll() {
		return em.createQuery("select g from Group g", Group.class).getResultList();
	}

	public List<Group> search(String name) {
		Query query = em.createQuery("select g from Group g where g.name like :name", Group.class);
		query.setParameter("name", "%" + name + "%");
		return query.getResultList();
	}

	public Group update(Group user) {
		return em.merge(user);
	}
}
