package springbook.sug.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.inject.Provider;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.support.SessionStatus;

import springbook.sug.domain.Login;
import springbook.sug.service.UserService;
import springbook.sug.web.security.LoginInfo;
import springbook.sug.web.validator.LoginValidator;

/**
 * 컨트롤러 단위 테스트
 */
public class LoginControllerTest {
	LoginController loginController;
	LoginValidator loginValidator;
	UserService userService;
	BindingResult result;
	SessionStatus status;
	Login login;
	
	@Before
	public void before() {
		loginController = new LoginController();
		loginValidator = mock(LoginValidator.class);
		userService = mock(UserService.class);
		loginController.init(loginValidator, userService);
		result = mock(BindingResult.class);
		status = mock(SessionStatus.class);
		login = new Login();
	}
	
	@Test
	public void resultHasErrors() throws HttpSessionRequiredException {
		when(result.hasErrors()).thenReturn(true);
		String viewName = loginController.login(login, result, status);
		assertThat(viewName, is("login"));
		verify(loginValidator, times(0)).validate(login, result);
	}
	
	@Test
	public void loginValidationFail() throws HttpSessionRequiredException {
		when(result.hasErrors()).thenReturn(false, true);
		String viewName = loginController.login(login, result, status);
		assertThat(viewName, is("login"));
		verify(loginValidator, times(1)).validate(login, result);
	}
	
	@Test
	public void formSuccess() {
		when(result.hasErrors()).thenReturn(false);
		Provider<LoginInfo> provider = mock(Provider.class);
		LoginInfo loginInfo = mock(LoginInfo.class); 
		when(provider.get()).thenReturn(loginInfo);
		loginController.setLoginInfoProvider(provider);
		
		String viewName = loginController.login(login, result, status);
		assertThat(viewName, is("redirect:user/list"));
		
		verify(status).setComplete();
		verify(loginInfo).currentUser();
		verify(userService).login(loginInfo.currentUser());
	}
}
