package springbook.learningtest.spring.ibatis;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.learningtest.spring.jdbc.Member;

import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class IbatisTest {
	@Autowired IbatisDao dao;
	
	@Test
	public void ibatis() {
		dao.deleteAll();
		dao.insert(new Member(5, "iBatis", 1.2));
		dao.insert(new Member(6, "sqlMap", 3.3));
		
		Member m = dao.select(5);
		assertThat(m.getId(), is(5));
		assertThat(m.getName(), is("iBatis"));
		assertThat(m.getPoint(), is(1.2));
		
		List<Member> ms = dao.selectAll();
		assertThat(ms.size(), is(2));
		
	}
	public static class IbatisDao {
		private SqlMapClientTemplate sqlMapClientTemplate;
		
		public void setSqlMapClient(SqlMapClient sqlMapClient) {
			sqlMapClientTemplate = new SqlMapClientTemplate(sqlMapClient);
		}
		
		public void insert(Member m) { sqlMapClientTemplate.insert("insertMember", m); }		
		public void deleteAll() { sqlMapClientTemplate.delete("deleteMemberAll"); }
		public Member select(int id) { return (Member)sqlMapClientTemplate.queryForObject("findMemberById", id); }
		public List<Member> selectAll() { return sqlMapClientTemplate.queryForList("findMembers"); }
	}
}
