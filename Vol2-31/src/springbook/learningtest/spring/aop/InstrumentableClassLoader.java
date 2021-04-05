package springbook.learningtest.spring.aop;

import java.lang.instrument.ClassFileTransformer;

import org.springframework.instrument.classloading.SimpleInstrumentableClassLoader;

public class InstrumentableClassLoader  extends SimpleInstrumentableClassLoader {

	public InstrumentableClassLoader(ClassLoader parent) {
		super(parent);
		excludePackage("com.sun");
		excludePackage("org.apache");
	}
	
	@Override
	public void addTransformer(ClassFileTransformer transformer) {
		System.out.println("*** Transformer : " + transformer);
		super.addTransformer(transformer);
	}

	@Override
	protected byte[] transformIfNecessary(String name, byte[] bytes) {
		return super.transformIfNecessary(name, bytes);
	}
	
	
}