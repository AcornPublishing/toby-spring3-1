package springbook.learningtest.spring.aop;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class InstrumentableSpringJUnit4ClassRunner extends SpringJUnit4ClassRunner {

	public InstrumentableSpringJUnit4ClassRunner(Class<?> clazz)
			throws InitializationError {
		super(clazz);
	}

	@Override
	protected TestContextManager createTestContextManager(Class<?> clazz) {
		Thread.currentThread().setContextClassLoader(
			new InstrumentableClassLoader(Thread.currentThread().getContextClassLoader())
		);
		
		return super.createTestContextManager(clazz);
	}

	
}
