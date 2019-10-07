package com.practice.familymodel.models;

import com.practice.familymodel.utils.RelationshipConstants.RelationshipType;

/**
 * 
 * @author Nitesh Represents the relationship entity of the application
 */
public class Relationship {
	private int firstPerson;
	private int secondPerson;
	private RelationshipType relation;

	public Relationship(int firstPerson, int secondPerson, RelationshipType relation) {
		this.firstPerson = firstPerson;
		this.secondPerson = secondPerson;
		this.relation = relation;
	}

	public int getFirstPerson() {
		return firstPerson;
	}

	public void setFirstPerson(int firstPerson) {
		this.firstPerson = firstPerson;
	}

	public int getSecondPerson() {
		return secondPerson;
	}

	public void setSecondPerson(int secondPerson) {
		this.secondPerson = secondPerson;
	}

	public RelationshipType getRelation() {
		return relation;
	}

	public void setRelation(RelationshipType relation) {
		this.relation = relation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + firstPerson;
		result = prime * result + ((relation == null) ? 0 : relation.hashCode());
		result = prime * result + secondPerson;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Relationship other = (Relationship) obj;
		if (firstPerson != other.firstPerson)
			return false;
		if (relation != other.relation)
			return false;
		if (secondPerson != other.secondPerson)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[Id:" + secondPerson + " Relation:" + relation + "]";
	}

}
