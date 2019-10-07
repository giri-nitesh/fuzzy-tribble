package com.practice.familymodel.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.practice.familymodel.utils.RelationshipConstants.RelationshipType;

/**
 * 
 * @author Nitesh Represents a family tree. Holds all the relationships and
 *         details about each member
 */

public class FamilyTree {
	private static final Logger LOGGER = Logger.getLogger(FamilyTree.class);

	private final List<ArrayList<Relationship>> familyTree = new ArrayList<ArrayList<Relationship>>(
			AppConstant.FAMILY_SIZE);
	private final Map<String, Integer> familyMemberId = new HashMap<String, Integer>();
	private final Map<Integer, Person> familyMemberDetail = new HashMap<Integer, Person>();
	private final Map<Integer, Parent> parentMapper = new HashMap<Integer, Parent>();

	private RelationshipFinder relationshipFinder;

	public List<ArrayList<Relationship>> getFamilyTree() {
		return familyTree;
	}

	public Map<String, Integer> getFamilyMemberId() {
		return familyMemberId;
	}

	public RelationshipFinder getRelationshipFinder() {
		return relationshipFinder;
	}

	public FamilyTree() {
		for (int index = 0; index <= AppConstant.FAMILY_SIZE; index++) {
			ArrayList<Relationship> list = new ArrayList<Relationship>();
			familyTree.add(list);
		}
		relationshipFinder = new RelationshipFinder(familyTree, familyMemberId, familyMemberDetail, parentMapper);
	}

	/**
	 * Load the family tree as specified from the file
	 * 
	 * @param file
	 */
	public void initializeTree(File file) {

		try {
			FamilyManager.InitialiseTree(file, familyTree, familyMemberId, familyMemberDetail, parentMapper);
		} catch (TooManyArguementsException exception) {
			LOGGER.error("Input arguements exceed the specified length.", exception);
		} catch (InsufficientArguementsException exception) {
			LOGGER.error("One or more arguements missing in the input line.", exception);
		} catch (FileNotFoundException exception) {
			LOGGER.error("File Not Found.", exception);
		}
		printFamilyTree();
	}

	/**
	 * Prints the family tree. Family tree is represented as graph, so it would just
	 * print the graph in adjacency list representation
	 */
	public void printFamilyTree() {
		LOGGER.info("=================Starting Printing Family Tree=================");
		for (int index = 1; index <= FamilyManager.getSizeOfFamily(); index++) {
			StringBuffer sb = new StringBuffer();
			String line = "[" + familyMemberDetail.get(index).getId() + " " + familyMemberDetail.get(index).getName()
					+ "] => ";
			sb.append(line);
			ArrayList<Relationship> list = familyTree.get(index);
			for (Relationship rel : list) {
				int pId = rel.getSecondPerson();
				String name = familyMemberDetail.get(pId).getName();
				sb.append("[" + pId + " " + name + " " + rel.getRelation() + "] ");
			}
			LOGGER.info(sb.toString());
		}
		LOGGER.info("=================Finished Printing Family Tree=================");
	}

	/**
	 * Adds a child to the family through mothers name
	 * 
	 * @param motherName mother of the child to be added
	 * @param name       name of the child
	 * @param gender     gender of the child
	 * @throws InvalidGenderException         if the gender of the child does not
	 *                                        matches to one of the predefined
	 *                                        gender type in application
	 * @throws PersonNotFoundException        if the mother is not found in the
	 *                                        family tree
	 * @throws UndefinedRelationshipException if the relationship specified from
	 *                                        argument does not matches to one of
	 *                                        the predefined relationships in
	 *                                        application
	 * @throws RelationshipNotFoundException
	 */
	public void addChild(String motherName, String name, String gender) throws InvalidGenderException,
			PersonNotFoundException, UndefinedRelationshipException, RelationshipNotFoundException {
		Gender sex = FamilyUtils.getGender(gender);
		int size = FamilyManager.getSizeOfFamily();
		size++;
		FamilyManager.setSizeOfFamily(size);
		Person person = new Person(size, name, sex);
		RelationshipType type = sex == Gender.FEMALE ? RelationshipType.DAUGHTER : RelationshipType.SON;
		FamilyManager.addMemberToFamilyTree(person, motherName, type, familyMemberDetail, familyMemberId, familyTree,
				parentMapper);
	}

}
