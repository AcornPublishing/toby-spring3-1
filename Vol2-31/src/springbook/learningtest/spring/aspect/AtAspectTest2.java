package springbook.learningtest.spring.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

public class AtAspectTest2 {
	@Test
	public void simple() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
		ac.register(HelloBean.class, HiBean.class, SimpleMornitoringAspect.class);
		ac.register(Client.class, HiBean.class);
		AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(ac);
		AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(ac);
		ac.refresh();
		
		for(String name : ac.getBeanDefinitionNames()) {
			System.out.println(name + "\t\t" + ac.getBean(name).getClass());
		}
		
		ac.getBean(Client.class).dohello();
		ac.getBean(Client.class).dohibean();
			
		ac.getBean("hello", HelloBean.class).sayHello("Spring");
		ac.getBean("hello", HelloBean.class).add(1, 2);
		ac.getBean("hi", Hello.class).sayHello("Spring");
	}
	interface Hello { String sayHello(String name);	}
	@Component("hello") static class HelloBean {
		public String sayHello(String name) {
			return "Hello " + name;
		}
		public int add(int a, int b) {
			return a+b;
		}
	}
	@Component("client") static class Client {
		@Autowired HiBean hiBean;
		@Autowired Hello hello;
		
		public void dohibean() {
			System.out.println(hiBean.sayHello("HiBean"));
		}
		public void dohello() {
			System.out.println(hiBean.sayHello("Hello"));
		}
	}
	@Component("hi") static class HiBean implements Hello {
		public String sayHello(String name) {
			return "Hi " + name;
		}
	}
	
	@Aspect 
	public static class SimpleMornitoringAspect {
//		@Pointcut("this(springbook.learningtest.spring.aspect.AtAspectTest.HelloBean)")
//		public void pointcutHb() {}
//		
//		@Before("pointcutHb()")
//		public void hello(JoinPoint jp) {
//			System.out.println(jp.getTarget().getClass());
//		}
		@Pointcut("execution(* *(..))")
		private void all() {}
		
		@Around("all()")
		public Object printParametersAndReturnVal(ProceedingJoinPoint pjp) throws Throwable {
			System.out.println("Class : " + pjp.getTarget().getClass());
			System.out.println("Method : " + pjp.getSignature().getName());
			System.out.print("Args : ");
			for(Object arg : pjp.getArgs()) System.out.print(arg + " / "); 
			
			Object ret = pjp.proceed();
			
			System.out.println("\nReturn : " + ret);
			
			return ret;
		}
	}
}
