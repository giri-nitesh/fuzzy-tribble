package com.practice.familymodel.services;

import java.util.HashMap;
import java.util.Map;

import com.practice.familymodel.exceptions.InsufficientArguementsException;
import com.practice.familymodel.exceptions.InvalidGenderException;
import com.practice.familymodel.exceptions.PersonNotFoundException;
import com.practice.familymodel.exceptions.UndefinedRelationshipException;
import com.practice.familymodel.models.Person;
import com.practice.familymodel.utils.GenderCostants.Gender;
import com.practice.familymodel.utils.RelationshipConstants;
import com.practice.familymodel.utils.RelationshipConstants.RelationshipType;

/**
 * 
 * @author Nitesh This class contains helper methods that are used in the
 *         application
 */
public class FamilyUtils {

	/**
	 * Get a person object specified from input file separated by commas
	 * 
	 * @param id     person id
	 * @param string
	 * @return person object of the Person created
	 * @throws InsufficientArguementsException if there are not sufficient params
	 */
	public static Person getPerson(int id, String string) throws InsufficientArguementsException {
		String[] kingDetails = string.split(",");
		if (kingDetails.length < 2) {
			InsufficientArguementsException exception = new InsufficientArguementsException(
					"Insufficient parameters to define a person.");
			throw exception;
		}
		String name = kingDetails[0].trim();
		Gender sex = kingDetails[1].trim().equals("M") ? Gender.MALE : Gender.FEMALE;
		Person person = new Person(id, name, sex);
		return person;
	}

	/**
	 * Get the id of the person specified through a string
	 * 
	 * @param param
	 * @param familyMemberDetail
	 * @param familyMemberId
	 * @return
	 * @throws PersonNotFoundException If the person does not exist in family
	 */
	public static Person getPerson(String param, Map<Integer, Person> familyMemberDetail,
			Map<String, Integer> familyMemberId) throws PersonNotFoundException {
		String name = param.trim();
		Integer personId = null;
		if (!familyMemberId.containsKey(name)) {
			PersonNotFoundException exception = new PersonNotFoundException(
					"Person: " + name + " does not exist in" + "the family tree");
			throw exception;
		} else {
			personId = familyMemberId.get(name);
		}
		if (!familyMemberDetail.containsKey(personId)) {
			PersonNotFoundException exception = new PersonNotFoundException(
					"Person with name: " + name + " and " + "id: " + personId + " does not exist in the family tree");
			throw exception;
		}
		return familyMemberDetail.get(personId);

	}

	/**
	 * Get the predefined relationship type in application
	 * 
	 * @param string
	 * @return relationship type specified through integer
	 * @throws UndefinedRelationshipException if no such relationship exists in the
	 *                                        application
	 * @throws NumberFormatException
	 */
	public static RelationshipType getRelationshipType(String string)
			throws UndefinedRelationshipException, NumberFormatException {
		int relation = Integer.parseInt(string.trim());
		HashMap<Integer, RelationshipType> relationshipMap = RelationshipConstants.RELATION;
		if (relationshipMap.get(relation) == null) {
			UndefinedRelationshipException exception = new UndefinedRelationshipException(
					"Input relationship: " + relation + " is not defined for this kingdom");
			throw exception;
		} else {
			return relationshipMap.get(relation);
		}
	}

	/**
	 * 
	 * @param gender string representation of gender
	 * @return gender type as specified through string parameter
	 * @throws InvalidGenderException if the gender specified cannot be casted to
	 *                                one of the predefined genders in the
	 *                                application
	 */
	public static Gender getGender(String gender) throws InvalidGenderException {
		String parseGender = gender.toLowerCase().substring(0, 1);
		if (parseGender.equals("m"))
			return Gender.MALE;
		else if (parseGender.equals("f"))
			return Gender.FEMALE;
		else
			throw new InvalidGenderException("Gender " + gender + "specified is invalid.");
	}

}
