package springbook.learningtest.spring.ioc.autowired;

import org.springframework.stereotype.Component;

@Component
public class StringPrinter implements Printer {
	private StringBuffer buffer = new StringBuffer();

	public void print(String message) {
		this.buffer.append(message);
	}

	public String toString() {
		return this.buffer.toString();
	}
}

