package springbook.sug.web.security;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import springbook.sug.domain.User;

public class SessionLoginInfoTest {
	@Test
	public void loginLogout() {
		SessionLoginInfo sli = new SessionLoginInfo();
		assertThat(sli.currentUser(), is(nullValue()));
		assertThat(sli.isLoggedIn(), is(false));
		assertThat(sli.getLoginTime(), is(nullValue()));
		
		User user = new User();
		sli.save(user);
		assertThat(sli.isLoggedIn(), is(true));
		assertThat(sli.currentUser(), is(user));
		assertThat(sli.getLoginTime(), is(notNullValue()));

		sli.remove();
		assertThat(sli.isLoggedIn(), is(false));
		assertThat(sli.currentUser(), is(nullValue()));
		assertThat(sli.getLoginTime(), is(nullValue()));
	}
	
	@Test(expected=IllegalStateException.class)
	public void illegalRemove() {
		SessionLoginInfo sli = new SessionLoginInfo();
		sli.remove();
	}
	
}
