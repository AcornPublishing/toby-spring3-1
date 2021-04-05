package springbook.learningtest.spring31.enable;

public class Hello {
	String name;
	
	public void setName(String name) {
		this.name = name;
	}

	public void sayHello() {
		System.out.println("Hello " + name);
	}
	
	public String getHello() {
		return "Hello " + name;
	}
}
