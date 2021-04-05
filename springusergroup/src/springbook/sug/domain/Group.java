package springbook.sug.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="groups")
public class Group {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	
	@Column(length=50)
	String name;
	
	public Group(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public Group() {
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
	
	public String toString() {
		return "Group [id=" + id + ", name=" + name + "]";
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
		Group other = (Group) obj;
		if (id != other.id)	return false;
		return true;
	}
}
