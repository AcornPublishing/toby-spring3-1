package springbook.sug.web.security;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UserSecurityInterceptor extends HandlerInterceptorAdapter {
	@Inject
	private Provider<LoginInfo> loginInfoProvider; 

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if (loginInfoProvider.get().isLoggedIn()) {
			return true;
		} 
		else {
			response.sendRedirect(request.getContextPath() + "/accessdenied");
			return false;
		}
	}
}
