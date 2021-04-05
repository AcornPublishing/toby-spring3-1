package springbook.learningtest.spring.jdbc;


public class Member {
	int id;
	String name;
	double point;

	public Member() {
	}
	public Member(int id, String name, double point) {
		super();
		this.id = id;
		this.name = name;
		this.point = point;
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
	public double getPoint() {
		return point;
	}
	public void setPoint(double point) {
		this.point = point;
	}
	@Override
	public String toString() {
		return "Member [id=" + id + ", name=" + name + ", point=" + point + "]";
	}
	
	
}
