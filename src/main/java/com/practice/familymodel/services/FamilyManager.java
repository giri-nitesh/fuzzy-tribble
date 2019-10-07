package com.practice.familymodel.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.practice.familymodel.exceptions.InsufficientArguementsException;
import com.practice.familymodel.exceptions.InvalidGenderException;
import com.practice.familymodel.exceptions.PersonNotFoundException;
import com.practice.familymodel.exceptions.RelationshipNotFoundException;
import com.practice.familymodel.exceptions.TooManyArguementsException;
import com.practice.familymodel.exceptions.UndefinedRelationshipException;
import com.practice.familymodel.models.Parent;
import com.practice.familymodel.models.Person;
import com.practice.familymodel.models.Relationship;
import com.practice.familymodel.utils.AppConstant;
import com.practice.familymodel.utils.GenderCostants.Gender;
import com.practice.familymodel.utils.RelationshipConstants;
import com.practice.familymodel.utils.RelationshipConstants.RelationshipType;

/**
 * 
 * @author Nitesh Family Manager, is managing the entire family structure by
 *         initializing the hierarchy. It can add a member to family or delete a
 *         member from family. It can also give us the primary relationship
 *         between any two member such as siblings or parents.
 *
 */
public class FamilyManager {

	private static final Logger LOGGER = Logger.getLogger(FamilyManager.class);
	private static int sizeOfFamily = 0;

	/**
	 * 
	 * @param file               family hierarchy to be loaded
	 * @param familyTree         graph representation of family hierarchy
	 * @param familyMemberId     holds the information about a person
	 * @param familyMemberDetail
	 * @param parentMapper
	 * @throws TooManyArguementsException
	 * @throws InsufficientArguementsException
	 * @throws FileNotFoundException
	 */
	protected static void InitialiseTree(File file, List<ArrayList<Relationship>> familyTree,
			Map<String, Integer> familyMemberId, Map<Integer, Person> familyMemberDetail,
			Map<Integer, Parent> parentMapper)
			throws TooManyArguementsException, InsufficientArguementsException, FileNotFoundException {

		// Checks if line is the root of tree
		boolean isFirst = true;
		Pattern pattern = Pattern.compile(AppConstant.PATTERN);
		Scanner sc = new Scanner(file);
		while (sc.hasNext()) {
			sizeOfFamily++;
			String line = sc.nextLine();
			Matcher matcher = pattern.matcher(line);

			// param1=> args[0]: Person1
			// param2=> args[1]: Person2
			// param3=> args[2]: Relationship of Person2 with Person1
			String[] args = new String[AppConstant.ARGS_SIZE];

			int i = 0;
			while (matcher.find()) {
				args[i] = matcher.group(1);
				i++;
			}
			if (i > AppConstant.ARGS_SIZE) {
				throw new TooManyArguementsException(
						"No of input arguements" + "exceed than the expected Length : " + AppConstant.ARGS_SIZE);
			}

			if (!isFirst && i < AppConstant.ARGS_SIZE) {
				throw new InsufficientArguementsException(
						"No of input arguements" + "are less than defined one: " + AppConstant.ARGS_SIZE);
			}
			/*
			 * Case 1: When we are adding the root of family, we do not have to define any
			 * relationship. So we need just one param param1=> Person to be added
			 */
			if (isFirst) {
				try {
					Person person = FamilyUtils.getPerson(sizeOfFamily, args[0]);
					familyMemberDetail.put(person.getId(), person);
					familyMemberId.put(person.getName(), person.getId());
					parentMapper.put(person.getId(), null);
					LOGGER.info("Added King to the family tree with details: " + person);
				} catch (InsufficientArguementsException exception) {
					LOGGER.error("Error while creating an object of Person: ", exception);
				}
				isFirst = false;
			}
			/*
			 * Case 2: When we are adding other members of the family, we need 3 param:
			 * param1: firstPerson => We are adding this person to the tree param2:
			 * secondPerson => We are adding first person by defining relationship with
			 * param1 param3: relationship => Associating param1 with param2 as defined
			 * param3
			 */
			else {
				try {
					Person firstPerson = FamilyUtils.getPerson(sizeOfFamily, args[0]);
					RelationshipType type = FamilyUtils.getRelationshipType(args[2]);
					addMemberToFamilyTree(firstPerson, args[1], type, familyMemberDetail, familyMemberId, familyTree,
							parentMapper);
				} catch (InsufficientArguementsException exception) {
					LOGGER.error("Error while creating an object of Person: ", exception);
				} catch (PersonNotFoundException exception) {
					LOGGER.error("Person not found in the family ", exception);
				} catch (UndefinedRelationshipException exception) {
					LOGGER.error("Relationship is invalid", exception);
				} catch (Exception exception) {
					LOGGER.error("Some exception occured", exception);
				}
			}
		}
		sc.close();
	}

	/**
	 * 
	 * @param firstPerson
	 * @param nameOfSecondPerson
	 * @param type
	 * @param familyMemberDetail
	 * @param familyMemberId
	 * @param familyTree
	 * @param parentMapper
	 * @throws PersonNotFoundException
	 * @throws UndefinedRelationshipException
	 * @throws RelationshipNotFoundException
	 * @throws InvalidGenderException         if child is not being added through
	 *                                        mother
	 */
	public static void addMemberToFamilyTree(Person firstPerson, String nameOfSecondPerson, RelationshipType type,
			Map<Integer, Person> familyMemberDetail, Map<String, Integer> familyMemberId,
			List<ArrayList<Relationship>> familyTree, Map<Integer, Parent> parentMapper) throws PersonNotFoundException,
			UndefinedRelationshipException, RelationshipNotFoundException, InvalidGenderException {
		familyMemberDetail.put(firstPerson.getId(), firstPerson);
		familyMemberId.put(firstPerson.getName(), firstPerson.getId());
		Person secondPerson = FamilyUtils.getPerson(nameOfSecondPerson, familyMemberDetail, familyMemberId);
		if (secondPerson.getSex() == Gender.MALE
				&& (type == RelationshipType.SON || type == RelationshipType.DAUGHTER)) {
			throw new InvalidGenderException("Cannot add a child with father's name");
		}
		if (type == RelationshipType.SON || type == RelationshipType.DAUGHTER) {
			int husbandId = getHusband(familyTree, secondPerson.getId());
			Person father = familyMemberDetail.get(husbandId);
			Parent parent = new Parent();
			parent.setFather(father);
			parent.setMother(secondPerson);
			parentMapper.put(firstPerson.getId(), parent);
		}
		addRelationshipInFamilyTree(firstPerson, secondPerson, type, familyTree);
	}

	/**
	 * 
	 * @param firstPerson  Person to be added in the family
	 * @param secondPerson Person through which {@link firstPerson} is added to the
	 *                     family. It is assumed that this person already exists in
	 *                     the family tree
	 * @param type         Relationship of the secondPerson with firstPerson
	 * @param familyTree   Family Tree
	 */
	/*
	 * When we add relationship between two member it is always a two way.
	 * Relationship of son with mother is MOTHER relationship while relationship of
	 * mother with son is SON relationship. When we add a son, it is always added
	 * through mother, we can add other case as well when we want to add the child
	 * through father
	 */
	private static void addRelationshipInFamilyTree(Person firstPerson, Person secondPerson, RelationshipType type,
			List<ArrayList<Relationship>> familyTree) {

		ArrayList<Relationship> list;

		// Add the first relationship
		list = familyTree.get(secondPerson.getId());
		Relationship relationShip = new Relationship(secondPerson.getId(), firstPerson.getId(), type);
		list.add(relationShip);
		LOGGER.info("Added relation of {" + type + "->" + RelationshipConstants.COMPLIMENTARY_RELATION.get(type)
				+ "} between {<" + secondPerson.getId() + ":" + secondPerson.getName() + "> & <" + firstPerson.getId()
				+ ":" + firstPerson.getName() + ">}.");

		// Add the second relationship
		list = familyTree.get(firstPerson.getId());
		relationShip = new Relationship(firstPerson.getId(), secondPerson.getId(),
				RelationshipConstants.COMPLIMENTARY_RELATION.get(type));
		list.add(relationShip);
		LOGGER.info("Added relation of {" + RelationshipConstants.COMPLIMENTARY_RELATION.get(type) + "->" + type
				+ "} between {<" + firstPerson.getId() + ":" + firstPerson.getName() + "> & <" + secondPerson.getId()
				+ ":" + secondPerson.getName() + ">}.");

	}

	/**
	 * 
	 * @param familyTree Family tree
	 * @param wifeId     Id of the wife for which we are looking for husband
	 * @return Id of the husband
	 * @throws RelationshipNotFoundException If husband is not found in family tree
	 */
	public static int getHusband(List<ArrayList<Relationship>> familyTree, int wifeId)
			throws RelationshipNotFoundException {
		List<Relationship> wifeRelationshipList = familyTree.get(wifeId);
		for (Relationship relationship : wifeRelationshipList) {
			if (relationship.getRelation() == RelationshipType.WIFE) {
				return relationship.getSecondPerson();
			}
		}
		throw new RelationshipNotFoundException("Person with id: " + wifeId + " is not married.");
	}

	/**
	 * 
	 * @param familyTree Family tree
	 * @param husbandId  Id of the husband for whom we are looking for wife
	 * @return Id of the wife
	 * @throws RelationshipNotFoundException If wife is not found in the family tree
	 */
	public static int getWife(List<ArrayList<Relationship>> familyTree, int husbandId)
			throws RelationshipNotFoundException {
		List<Relationship> husbandRelationshipList = familyTree.get(husbandId);
		for (Relationship relationship : husbandRelationshipList) {
			if (relationship.getRelation() == RelationshipType.HUSBAND) {
				return relationship.getSecondPerson();
			}
		}
		throw new RelationshipNotFoundException("Person with id: " + husbandId + "is not married.");
	}

	@SuppressWarnings("unused")
	private void deleteMember() {

	}

	/**
	 * 
	 * @return the size of family
	 */
	public static int getSizeOfFamily() {
		return sizeOfFamily;
	}

	/**
	 * 
	 * @param sizeOfFamily sets the size of family to specified params
	 */
	public static void setSizeOfFamily(int sizeOfFamily) {
		FamilyManager.sizeOfFamily = sizeOfFamily;
	}
}
