package springbook.sug.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

public class FieldInjectUtilsTest {
	@Test
	public void fieldInjectByType() {
		Bean b = new Bean();
		FieldInjectUtils.inject(b, String.class, "Spring");
		assertThat(b.getName(), is("Spring"));
		
		Bean bb = new Bean();
		FieldInjectUtils.inject(b, Bean.class, bb);
		assertThat(b.getBean(), is(bb));
	}
	
	// 독자를 위한 TODO
	// 타입에 의한 inject()의 경우 같은 타입이 두 개 이상 있을 때는 예외가 발생하도록 만들어 봅시다.
	
	@Test
	public void FieldInjectByName() {
		Bean2 b2 = new Bean2();
		FieldInjectUtils.inject(b2, String.class, "name", "Spring");
		assertThat(b2.getName(), is("Spring"));
		FieldInjectUtils.inject(b2, String.class, "id", "id1");
		assertThat(b2.getId(), is("id1"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void fieldNotFound() {
		Bean b = new Bean();
		FieldInjectUtils.inject(b, ApplicationContext.class, null);
	}
	
	private static class Bean {
		private String name;
		private Bean bean;

		public String getName() {
			return name;
		}
		public Bean getBean() {
			return bean;
		}
	}
	
	private static class Bean2 extends Bean {
		private String id;
		
		public String getId() {
			return id;
		}
	}
}
