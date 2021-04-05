package springbook.learningtest.spring.remote;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.*;


public class RestTemplateTest {
	@Test @Ignore
	public void openApi() {
		RestTemplate template = new RestTemplate();
		
		String key = "�׽�Ʈ�� �����Ϸ��� DAUM API KEY�� ���� ��������";
		
		String result = 
			template.getForObject("http://apis.daum.net/search/blog?apikey=" + key +"&q={key}&output={output}", 
					String.class, "SpringFramework", "json");
		System.out.println(result);
		assertThat(result.contains("\"result\":\"10\""), is(true));
	}
}
