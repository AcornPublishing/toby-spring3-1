package springbook.sug.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import springbook.sug.domain.Group;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
@Transactional
public class GroupDaoJpaTest {
	@PersistenceContext EntityManager em; 
	@Autowired GroupDao groupDao;
	Group group1;
	Group group2;
	Group group3;
	
	@Before
	public void before() {
		group1 = new Group(0, "group1");
		group2 = new Group(0, "group2");
		group3 = new Group(0, "group2-1");
	}
	
	@Test
	public void crud() {
		groupDao.deleteAll();
		assertThat(groupDao.count(), is(0L));
		
		// C
		groupDao.add(group1);
		assertThat(groupDao.count(), is(1L)); 
		assertThat(group1.getId() > 0, is(true));
		
		groupDao.add(group2);
		groupDao.add(group3);
		assertThat(groupDao.count(), is(3L)); 
		
		// R
		assertThat(groupDao.get(group1.getId()), is(group1));
		assertThat(groupDao.get(group2.getId()), is(group2));
		assertThat(groupDao.get(group3.getId()), is(group3));
		
		// U
		group1.setName("modified1");
		group1 = groupDao.update(group1);
		em.flush();
		Group updatedGroup1 = groupDao.get(group1.getId());
		assertThat(updatedGroup1.getName(), is("modified1"));
	
		// D
		groupDao.delete(group1.getId());
		assertThat(groupDao.count(), is(2L));
		assertThat(groupDao.get(group1.getId()), is(nullValue()));
	}
	
	@Test
	public void search() {
		groupDao.deleteAll();
		groupDao.add(group1);
		groupDao.add(group2);
		groupDao.add(group3);
		 
		assertThat(groupDao.search("abc").size(), is(0));
		assertThat(groupDao.search("oup1").size(), is(1));
		assertThat(groupDao.search("up2").size(), is(2));
		assertThat(groupDao.search("group").size(), is(3));
	}
}
