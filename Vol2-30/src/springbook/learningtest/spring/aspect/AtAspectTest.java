package springbook.learningtest.spring.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AtAspectTest {
	@Test
	public void simple() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
		ac.register(HelloImpl.class, HelloWorld.class, HelloAspect.class);
		AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(ac);
//		AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(ac);
		ac.refresh();
		
		Hello hello = ac.getBean(Hello.class);
		hello.makeHello("Spring");
		hello.makeHello("Spring",2);
		hello.add(1, 2);
		
		HelloWorld helloWorld = ac.getBean(HelloWorld.class);
		helloWorld.hello();
		User u = new User(); u.id = 1; u.name = "Spring";
		helloWorld.printUser(u);
		
		HelloAspect aspect = ac.getBean(HelloAspect.class);
		System.out.println(aspect.resultCombine);
		System.out.println(aspect.resultReturnString);
		System.out.println(aspect.resultHelloClass);
		System.out.println(aspect.resultCommon);
	}
	@Aspect static class HelloAspect {
		List resultCombine = new ArrayList();		
		List resultReturnString = new ArrayList();		
		List resultHelloClass = new ArrayList();
		List resultCommon = new ArrayList();
		
		public void clear() { resultCombine.clear(); resultReturnString.clear(); resultHelloClass.clear(); 
		resultCommon.clear(); }
		
		@Pointcut("execution(String *(..))") private void returnString() {}		
//		@Pointcut("@args(springbook.learningtest.spring.aspect.AtAspectTest.Anno)") private void returnString() {}		
//		@Pointcut("within(springbook.*)") private void helloClass() {}
//		@Pointcut("returnString() || helloClass()") private void combine() {}
//		@Pointcut("returnString() && args(String)") private void common() {}
		
//		@Before("returnString()") public void callLogReturnString(JoinPoint jp) {
//			System.out.println("\t" + jp.getSignature().getDeclaringTypeName());
//			System.out.println("\t" + jp.getSignature().getName());			
//			for(Object arg : jp.getArgs()) {
//				System.out.println("\t" + arg);
//			}
//		
//			resultReturnString.add(jp.getSignature().getDeclaringType().getSimpleName() + "/" + jp.getSignature().getName());
//		}
//		@Before("helloClass()") public void callLogHelloClass(JoinPoint jp) {
//			resultHelloClass.add(jp.getSignature().getDeclaringType().getSimpleName() + "/" + jp.getSignature().getName());
//		}
//		@Before("combine()") public void callLogCombine(JoinPoint jp) {
//			resultCombine.add(jp.getSignature().getDeclaringType().getSimpleName() + "/" + jp.getSignature().getName());
//		}
//		@Before("common()") public void callLogCommon(JoinPoint jp) {
//			resultCommon.add(jp.getSignature().getDeclaringType().getSimpleName() + "/" + jp.getSignature().getName());
//		}

//		@Before("args(springbook.learningtest.spring.aspect.AtAspectTest.User)") 
//		public void user(JoinPoint jp) {
//			((User)(jp.getArgs()[0])).name = "Toby";
//		}		
//		@AfterReturning(pointcut="execution(* makeHello(..))", returning="ret") 
//		public void ret(JoinPoint jp, String ret) {
//			System.out.println("RET = " + ret);
//		}		
		@Before("@target(an)") 
		public void an(JoinPoint jp, Anno an) {
			System.out.println(an);
		}		
	}
	@Target({ElementType.TYPE, ElementType.PARAMETER})	@Retention(RetentionPolicy.RUNTIME) 
	public @interface Anno { String value() default "a"; }
	interface Hello {
		int add(int a, int b);
		String makeHello(String name);
		String makeHello(String name, int repeat);
	}
	@Anno("b") static class HelloImpl implements Hello {
		public String makeHello(String name) { return "Hello " + name; } 
		public String makeHello(String name, int repeat) {
			String names = "";
			while(repeat > 0) { names += name; repeat--; }
			return "Hello " + names;
		}
		public int add(int a, int b) { return a+b; }		
		public String convert(int a) { return String.valueOf(a); }		
		protected int increase(int a) { return a++; }
	}
	@Anno("c") static class HelloWorld {
		public void hello() { System.out.println("Hello World"); }
		public void printUser(User u) { System.out.println(u); }
	}
	static public class User {
		int id; String name;
		public String toString() { return "User [id=" + id + ", name=" + name + "]"; }
	}
}
