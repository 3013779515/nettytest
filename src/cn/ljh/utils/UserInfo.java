package cn.ljh.utils;

import org.msgpack.annotation.Message;

/*�������@messageע�⣬�����޷��������*/
@Message
public class UserInfo {
	
	int age;
	
	String name;
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "UserInfo [age=" + age + ", name=" + name + "]";
	}
}
