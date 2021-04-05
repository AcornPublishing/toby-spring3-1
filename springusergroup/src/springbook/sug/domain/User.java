package springbook.sug.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

public class User {
	int id;
	
	@Size(min=2, max=20) 
	String name;
	
	@Size(min=4, max=12)
	String username;
	
	@Size(min=4, max=12)
	String password;
	
	@NotNull
	Type type;
	
	@NotNull
	Group group;
	 
	@DateTimeFormat(style="M-")
	Date created;
	
	@DateTimeFormat(style="M-")
	Date modified;
	
	@NumberFormat(style=Style.NUMBER)
	int logins;
	
	public User() {
	}
	
	public User(int id, String name, String username, String password,
			Type type, Group group, Date created, Date modified, int logins) {
		super();
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.type = type;
		this.group = group;
		this.created = created;
		this.modified = modified;
		this.logins = logins;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public int getLogins() {
		return logins;
	}
	public void setLogins(int logins) {
		this.logins = logins;
	}
	
	public String toString() {
		return "User [created=" + created + ", group=" + group + ", id=" + id
				+ ", logins=" + logins + ", modified=" + modified + ", name="
				+ name + ", password=" + password + ", type=" + type
				+ ", username=" + username + "]";
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		User other = (User) obj;
		if (id != other.id) return false;
		return true;
	}

	public void logIn() {
		this.logins++;
	}
	
	public void initDates() {
		Date now = new Date();
		if (this.created == null) this.created = now;
		if (this.modified == null) this.modified = now;	
	}
	
}
