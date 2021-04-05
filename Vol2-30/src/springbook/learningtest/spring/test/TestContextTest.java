package springbook.learningtest.spring.test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("context.xml")

public class TestContextTest {
	@Autowired BeanA a;
	@Resource BeanA beanA;
	BeanA b;
	BeanA c;
	
	@Autowired 
	public void setBeanA(BeanA b) {
		this.b = b;
	}
	
	@Autowired 
	public void init(BeanA c) {
		this.c = c;
	}
	
	@Test
	@DirtiesContext
	public void test1() {
		assertThat(a, is(notNullValue()));
		assertThat(beanA, is(notNullValue()));
		assertThat(b, is(notNullValue()));
		assertThat(c, is(notNullValue()));
	}
	
	@Test
	public void test2() {
		
	}
	
	static class BeanA {
		@PostConstruct public void init() {
			System.out.println("A");
		}
	}
}
