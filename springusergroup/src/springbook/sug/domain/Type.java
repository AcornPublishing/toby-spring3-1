package springbook.sug.domain;
 
public enum Type {
	ADMIN(1, "���"), MEMBER(2, "ȸ��"), GUEST(3, "�մ�");
	
	private int value;
	private String name;

	private Type(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public static Type valueOf(int value) {
		switch(value) {
		case 1 : return ADMIN;
		case 2 : return MEMBER;
		case 3: return GUEST;
		}
		throw new IllegalArgumentException("�߸��� id ���Դϴ�");
	}
}
