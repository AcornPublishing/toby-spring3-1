package springbook.learningtest.spring31.cache;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CacheTest {
	@Autowired SimpleService s;
	
	@Test @DirtiesContext
	public void cacheWithoutParameters() {
		assertThat(s.getCnt(), is(0));
		s.read();
		assertThat(s.getCnt(), is(1));
		s.read();
		assertThat(s.getCnt(), is(2));
		
		s.setCnt(0);
		assertThat(s.getCnt(), is(0));
		s.readCacheable();
		assertThat(s.getCnt(), is(1));
		s.readCacheable();
		assertThat(s.getCnt(), is(1));
		s.readCacheable();
		assertThat(s.getCnt(), is(1));
		
		s.readEvict();
		s.readCacheable();
		assertThat(s.getCnt(), is(2));
		s.readCacheable();
		assertThat(s.getCnt(), is(2));
	}
	
	@Test @DirtiesContext
	public void cacheWithSingleParameter() {
		assertThat(s.map().get("A"), is(0));
		assertThat(s.map().get("B"), is(0));
		assertThat(s.map().get("C"), is(0));

		s.readCacheable("A");
		assertThat(s.map().get("A"), is(1));
		assertThat(s.map().get("B"), is(0));
		assertThat(s.map().get("C"), is(0));
		
		s.readCacheable("A");
		assertThat(s.map().get("A"), is(1));
		assertThat(s.map().get("B"), is(0));
		assertThat(s.map().get("C"), is(0));
		
		s.readCacheable("A");
		assertThat(s.map().get("A"), is(1));
		assertThat(s.map().get("B"), is(0));
		assertThat(s.map().get("C"), is(0));
		
		s.readCacheable("B");
		assertThat(s.map().get("A"), is(1));
		assertThat(s.map().get("B"), is(1));
		assertThat(s.map().get("C"), is(0));
		
		s.readCacheable("B");
		assertThat(s.map().get("A"), is(1));
		assertThat(s.map().get("B"), is(1));
		assertThat(s.map().get("C"), is(0));
		
		s.readCacheable("C");
		assertThat(s.map().get("A"), is(1));
		assertThat(s.map().get("B"), is(1));
		assertThat(s.map().get("C"), is(1));
		
		s.readCacheable("C");
		assertThat(s.map().get("A"), is(1));
		assertThat(s.map().get("B"), is(1));
		assertThat(s.map().get("C"), is(1));
		
		s.readEvict("A");
		s.readCacheable("A");
		s.readCacheable("B");
		s.readCacheable("C");
		assertThat(s.map().get("A"), is(2));
		assertThat(s.map().get("B"), is(1));
		assertThat(s.map().get("C"), is(1));
		
		s.readEvict("B");
		s.readCacheable("A");
		s.readCacheable("B");
		s.readCacheable("C");
		assertThat(s.map().get("A"), is(2));
		assertThat(s.map().get("B"), is(2));
		assertThat(s.map().get("C"), is(1));
	}
	
	@Test @DirtiesContext
	public void cacheWithComplexObject() {
		User a = new User("A", "UserA");
		User a2 = new User("A", "UserA2");
		User b = new User("B", "UserB");
		User c = new User("C", "UserC");
		
		assertThat(s.map().get("A"), is(0));
		assertThat(s.map().get("B"), is(0));
		assertThat(s.map().get("C"), is(0));

		assertThat(s.readCacheable(a), is(a));
		assertThat(s.map().get("A"), is(1));
		assertThat(s.map().get("B"), is(0));
		assertThat(s.map().get("C"), is(0));
		
		assertThat(s.readCacheable(a2), is(a));	// same key
		assertThat(s.map().get("A"), is(1));
		assertThat(s.map().get("B"), is(0));
		assertThat(s.map().get("C"), is(0));
		
		assertThat(s.readCacheable(b), is(b));
		assertThat(s.map().get("A"), is(1));
		assertThat(s.map().get("B"), is(1));
		assertThat(s.map().get("C"), is(0));
		
		assertThat(s.readCacheable(c), is(c));
		assertThat(s.map().get("A"), is(1));
		assertThat(s.map().get("B"), is(1));
		assertThat(s.map().get("C"), is(1));
		
		assertThat(s.readCacheable(a), is(a));
		assertThat(s.readCacheable(b), is(b));
		assertThat(s.readCacheable(c), is(c));
		assertThat(s.map().get("A"), is(1));
		assertThat(s.map().get("B"), is(1));
		assertThat(s.map().get("C"), is(1));
		
		s.readEvictAll();
		assertThat(s.readCacheable(a), is(a));
		assertThat(s.readCacheable(b), is(b));
		assertThat(s.readCacheable(c), is(c));
		assertThat(s.map().get("A"), is(2));
		assertThat(s.map().get("B"), is(2));
		assertThat(s.map().get("C"), is(2));

	}
	
	@Test @DirtiesContext
	public void cacheWithConditional() {
		User a = new User("A", "ADMIN");
		User b = new User("B", "ADMIN");
		User c = new User("C", "USER");
		
		assertThat(s.map().get("A"), is(0));
		assertThat(s.map().get("B"), is(0));
		assertThat(s.map().get("C"), is(0));
		
		assertThat(s.readConditionalCacheable(a), is(a));
		assertThat(s.readConditionalCacheable(b), is(b));
		assertThat(s.readConditionalCacheable(c), is(c));
		assertThat(s.map().get("A"), is(1));
		assertThat(s.map().get("B"), is(1));
		assertThat(s.map().get("C"), is(1));
		
		assertThat(s.readConditionalCacheable(a), is(a));
		assertThat(s.readConditionalCacheable(b), is(b));
		assertThat(s.readConditionalCacheable(c), is(c));
		assertThat(s.map().get("A"), is(1));
		assertThat(s.map().get("B"), is(1));
		assertThat(s.map().get("C"), is(2));
	}

	@Component 
	public static class SimpleService {
		private int cnt = 0;
		private Map<String, Integer> map = new HashMap<String, Integer>();
		
		public SimpleService() {
			init();
		}
		
		public void init() {
			map.put("A", 0);
			map.put("B", 0);
			map.put("C", 0);
		}

		public Map<String, Integer> map() {
			return map;
		}

		public int getCnt() {
			return cnt;
		}
		
		public void setCnt(int cnt) {
			this.cnt = cnt;
		}

		public String read() {
			this.cnt++;
			return "Data";
		}
		
		@Cacheable("read")
		public String readCacheable() {
			this.cnt++;
			return "Data";
		}
		
		@CacheEvict("read")
		public void readEvict() {
		}
		
		@CacheEvict("read")
		public void readEvict(String id) {
		}
		
		@CacheEvict(value="read", allEntries=true)
		public void readEvictAll() {
		}
		
		@Cacheable("read")
		public String readCacheable(String id) {
			map.put(id, map.get(id)+1);
			return id;
		}
		
		@Cacheable(value="read", key="#user.userId")
		public User readCacheable(User user) {
			map.put(user.getUserId(), map.get(user.getUserId())+1);
			return user;
		}
		
		@Cacheable(value="read", condition="#user.type == 'ADMIN'")
		public User readConditionalCacheable(User user) {
			map.put(user.getUserId(), map.get(user.getUserId())+1);
			return user;
		}
	}
	
	public static class User {
		String userId;
		String type;
		
		public User(String userId, String type) {
			this.userId = userId;
			this.type = type;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}
	
	@Configuration
	@EnableCaching
	public static class AppConfig {
		@Bean public SimpleService s() { return new SimpleService(); }
		
		@Autowired
		@Bean public CacheManager cm(net.sf.ehcache.CacheManager cacheManager) {
			EhCacheCacheManager eccm = new EhCacheCacheManager();
			eccm.setCacheManager(cacheManager);
			return eccm;
		}
		
		@Bean public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
			EhCacheManagerFactoryBean ecmfb = new EhCacheManagerFactoryBean();
			ecmfb.setConfigLocation(new ClassPathResource("ehcache.xml", getClass()));
			return ecmfb;
		}
		
//		@Bean public CacheManager cacheManager() {
//			return new ConcurrentMapCacheManager();
//		}
	}
}
