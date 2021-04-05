package springbook.learningtest.spring31.web;

import java.net.URI;

import org.junit.Test;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class UriComponentsBuilderTest {
	@Test
	public void uriCB() {
		UriComponents uriComponents = 
		        UriComponentsBuilder.fromUriString("http://example.com/hotels/{hotel}/bookings/{booking}").build();
		        
		System.out.println(uriComponents.expand("42", "21").encode().toString());
		
//		String uri = ¡°http://www.myshop.com/user/¡± + userId ¡°/order/¡± + orderId;
			
		int userId = 10;
		int orderId = 20;
		UriComponents uc = 
        	UriComponentsBuilder.fromUriString("http://www.myshop.com/users/{user}/orders/{order}").build();
		System.out.println(uc.expand(10, 20).encode().toUriString());
		
		UriComponents uc2 = UriComponentsBuilder.newInstance()
			.scheme("http")
			.host("www.myshop.com").
			path("/users/{user}/orders/{order}").build();
	}
}




