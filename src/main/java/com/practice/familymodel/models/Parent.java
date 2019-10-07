package com.practice.familymodel.models;

/**
 * 
 * @author Nitesh
 * Represent a parent of the person
 */
public class Parent {
	
	private Person mother;
	private Person father;
	
	public Person getMother() {
		return mother;
	}

	public void setMother(Person mother) {
		this.mother = mother;
	}

	public Person getFather() {
		return father;
	}

	public void setFather(Person father) {
		this.father = father;
	}

}
