package springbook.sug.domain;

import javax.validation.constraints.Size;

public class Login {
	@Size(min=4, max=12)
	String username;
	@Size(min=4, max=12)
	String password;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
