package com.practice.familymodel.models;

import com.practice.familymodel.utils.GenderCostants.Gender;

/**
 * 
 * @author Nitesh Represents the person entity of our application
 */
public class Person {
	private int id;
	private String name;
	private Gender sex;

	public Person(int id, String name, Gender sex) {
		this.id = id;
		this.name = name;
		this.sex = sex;
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

	public Gender getSex() {
		return sex;
	}

	public void setSex(Gender sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "[" + name + " : {Id:" + id + " Gender:" + sex + "}]";
	}

}
