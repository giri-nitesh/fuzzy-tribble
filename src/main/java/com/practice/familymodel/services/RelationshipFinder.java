package com.practice.familymodel.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.practice.familymodel.exceptions.PersonNotFoundException;
import com.practice.familymodel.exceptions.RelationshipNotFoundException;
import com.practice.familymodel.models.Parent;
import com.practice.familymodel.models.Person;
import com.practice.familymodel.models.Relationship;
import com.practice.familymodel.utils.AppConstant;
import com.practice.familymodel.utils.GenderCostants.Gender;
import com.practice.familymodel.utils.RelationshipConstants.RelationshipType;

/**
 * 
 * @author Nitesh Relationship Finder class is responsible for processing the
 *         queries related to any relationship from user interface.
 *
 */
public class RelationshipFinder {

	private static final Logger LOGGER = Logger.getLogger(RelationshipFinder.class);
	private List<ArrayList<Relationship>> familyTree = new ArrayList<ArrayList<Relationship>>(AppConstant.FAMILY_SIZE);
	private Map<String, Integer> familyMemberId = new HashMap<String, Integer>();
	private Map<Integer, Person> familyMemberDetail = new HashMap<Integer, Person>();
	private Map<Integer, Parent> parentMapper = new HashMap<Integer, Parent>();

	public RelationshipFinder(List<ArrayList<Relationship>> familyTree, Map<String, Integer> familyMemberId,
			Map<Integer, Person> familyMemberDetail, Map<Integer, Parent> parentMapper) {
		this.familyTree = familyTree;
		this.familyMemberId = familyMemberId;
		this.familyMemberDetail = familyMemberDetail;
		this.parentMapper = parentMapper;
	}

	/**
	 * 
	 * @param name
	 * @param relation
	 * @return
	 * @throws PersonNotFoundException
	 * @throws RelationshipNotFoundException
	 */
	public String[] findRelative(String name, String relation)
			throws PersonNotFoundException, RelationshipNotFoundException {
		RelationshipType relationship = AppConstant.RELATION_MAPPER.get(relation.trim());
		Integer id = familyMemberId.get(name.trim());
		if (id == null) {
			throw new PersonNotFoundException(
					"Person with the name: " + name + "does not exist" + "in the family tree");
		}
		List<String> response = findRelative(id, relationship);
		String[] responseArray = new String[response.size()];
		int index = 0;
		for (String item : response) {
			responseArray[index++] = item;
		}
		return responseArray;
	}

	/**
	 * Router to the finding relationship query
	 * 
	 * @param id
	 * @param relationship
	 * @return
	 * @throws RelationshipNotFoundException
	 */

	private List<String> findRelative(Integer id, RelationshipType relationship) throws RelationshipNotFoundException {
		List<String> response;
		switch (relationship) {
		case PATERNAL_UNCLE:
			response = findPaternalUncle(id);
			return response;

		case PATERNAL_AUNT:
			response = findPaternalAunt(id);
			return response;

		case MATERNAL_UNCLE:
			response = findMaternalUncle(id);
			return response;

		case MATERNAL_AUNT:
			response = findMaternalAunt(id);
			return response;

		case SISTER_IN_LAW:
			response = findSisterInLaw(id);
			return response;

		case BROTER_IN_LAW:
			response = findBrotherInLaw(id);
			return response;

		case SON:
			response = findSon(id);
			return response;

		case DAUGHTER:
			response = findDaughter(id);
			return response;

		case SIBLING:
			response = findSibling(id);
			return response;

		default:
			break;
		}
		return null;
	}

	/**
	 * Finds the paternal uncle of the person as specified in id param
	 * 
	 * @param id
	 * @return list of the paternal uncles found in the family
	 * @throws RelationshipNotFoundException if no such relationship is found in the
	 *                                       aplication
	 */
	/*
	 * To get the Paternal Uncle, we have to print the male sibling of father Print
	 * all the sons of father's father
	 */
	private List<String> findPaternalUncle(Integer id) throws RelationshipNotFoundException {
		Parent parent = parentMapper.get(id);
		Person father = parent.getFather();
		if (father == null) {
			throw new RelationshipNotFoundException("Relationship not found in the family tree");
		}
		Parent fathersParent = parentMapper.get(father.getId());
		if (fathersParent == null) {
			throw new RelationshipNotFoundException("Relationship not found in the family tree");
		}
		// print all the male child of fathersParent
		ArrayList<Relationship> list = familyTree.get(fathersParent.getMother().getId());
		List<String> response = new ArrayList<String>();
		for (Relationship relationship : list) {
			if (relationship.getRelation() == RelationshipType.SON
					&& relationship.getSecondPerson() != father.getId()) {
				response.add(familyMemberDetail.get(relationship.getSecondPerson()).getName());
			}
		}
		return response;

	}

	/**
	 * Finds the paternal aunt of the person as specified in id param
	 * 
	 * @param id
	 * @return list of the paternal aunts found in the family
	 * @throws RelationshipNotFoundException if no such relationship is found in the
	 *                                       aplication
	 */
	private List<String> findPaternalAunt(Integer id) throws RelationshipNotFoundException {
		Parent parent = parentMapper.get(id);
		Person father = parent.getFather();
		if (father == null) {
			throw new RelationshipNotFoundException("Relationship not found in the family tree");
		}
		Parent fathersParent = parentMapper.get(father.getId());
		if (fathersParent == null) {
			throw new RelationshipNotFoundException("Relationship not found in the family tree");
		}
		// print all the male child of fathersParent
		ArrayList<Relationship> list = familyTree.get(fathersParent.getMother().getId());
		List<String> response = new ArrayList<String>();
		for (Relationship relationship : list) {
			if (relationship.getRelation() == RelationshipType.DAUGHTER
					&& relationship.getSecondPerson() != father.getId()) {
				response.add(familyMemberDetail.get(relationship.getSecondPerson()).getName());
			}
		}
		return response;

	}

	private List<String> findMaternalUncle(Integer id) throws RelationshipNotFoundException {
		Parent parent = parentMapper.get(id);
		Person mother = parent.getMother();
		if (mother == null) {
			throw new RelationshipNotFoundException("Relationship not found in the family tree");
		}
		Parent mothersParent = parentMapper.get(mother.getId());
		if (mothersParent == null) {
			throw new RelationshipNotFoundException("Relationship not found in the family tree");
		}
		// print all the male child of fathersParent
		ArrayList<Relationship> list = familyTree.get(mothersParent.getMother().getId());
		List<String> response = new ArrayList<String>();
		for (Relationship relationship : list) {
			if (relationship.getRelation() == RelationshipType.SON
					&& relationship.getSecondPerson() != mother.getId()) {
				response.add(familyMemberDetail.get(relationship.getSecondPerson()).getName());
			}
		}
		return response;

	}

	private List<String> findMaternalAunt(Integer id) throws RelationshipNotFoundException {
		Parent parent = parentMapper.get(id);
		Person mother = parent.getMother();
		if (mother == null) {
			throw new RelationshipNotFoundException("Relationship not found in the family tree");
		}
		Parent mothersParent = parentMapper.get(mother.getId());
		if (mothersParent == null) {
			throw new RelationshipNotFoundException("Relationship not found in the family tree");
		}
		// print all the male child of fathersParent
		ArrayList<Relationship> list = familyTree.get(mothersParent.getMother().getId());
		List<String> response = new ArrayList<String>();
		for (Relationship relationship : list) {
			if (relationship.getRelation() == RelationshipType.DAUGHTER
					&& relationship.getSecondPerson() != mother.getId()) {
				response.add(familyMemberDetail.get(relationship.getSecondPerson()).getName());
			}
		}
		return response;

	}

	private List<String> findBrotherInLaw(Integer id) throws RelationshipNotFoundException {
		Person person = familyMemberDetail.get(id);
		Integer spouseId = null;
		if (person.getSex() == Gender.MALE) {
			spouseId = FamilyManager.getWife(familyTree, person.getId());
		} else {
			spouseId = FamilyManager.getHusband(familyTree, person.getId());
		}
		Parent parent = parentMapper.get(spouseId);
		if (parent == null) {
			throw new RelationshipNotFoundException("No parent found for the person with id: " + spouseId);
		}
		Person mother = parent.getMother();
		ArrayList<Relationship> list = familyTree.get(mother.getId());
		List<String> response = new ArrayList<String>();
		for (Relationship relationship : list) {
			if (relationship.getRelation() == RelationshipType.SON && relationship.getSecondPerson() != spouseId) {
				response.add(familyMemberDetail.get(relationship.getSecondPerson()).getName());
			}
		}
		return response;

	}

	private List<String> findSisterInLaw(Integer id) throws RelationshipNotFoundException {
		Person person = familyMemberDetail.get(id);
		Integer spouseId = null;
		if (person.getSex() == Gender.MALE) {
			spouseId = FamilyManager.getWife(familyTree, person.getId());
		} else {
			spouseId = FamilyManager.getHusband(familyTree, person.getId());
		}
		Parent parent = parentMapper.get(spouseId);
		if (parent == null) {
			throw new RelationshipNotFoundException("No parent found for the person with id: " + spouseId);
		}
		Person mother = parent.getMother();
		ArrayList<Relationship> list = familyTree.get(mother.getId());
		List<String> response = new ArrayList<String>();
		for (Relationship relationship : list) {
			if (relationship.getRelation() == RelationshipType.DAUGHTER && relationship.getSecondPerson() != spouseId) {
				response.add(familyMemberDetail.get(relationship.getSecondPerson()).getName());
			}
		}
		return response;
	}

	private List<String> findSon(Integer id) throws RelationshipNotFoundException {
		Person person = familyMemberDetail.get(id);
		if (person == null) {
			throw new RelationshipNotFoundException("No such person exists in the family tree with id: " + id);
		}
		Integer idToGetSon = id;
		if (person.getSex() == Gender.MALE) {
			idToGetSon = FamilyManager.getWife(familyTree, id);
		}
		ArrayList<Relationship> list = familyTree.get(idToGetSon);
		List<String> response = new ArrayList<String>();
		for (Relationship relationship : list) {
			if (relationship.getRelation() == RelationshipType.SON) {
				response.add(familyMemberDetail.get(relationship.getSecondPerson()).getName());
			}
		}
		return response;
	}

	private List<String> findDaughter(Integer id) throws RelationshipNotFoundException {
		Person person = familyMemberDetail.get(id);
		if (person == null) {
			throw new RelationshipNotFoundException("No such person exists in the family tree with id: " + id);
		}
		Integer idToGetDaughter = id;
		if (person.getSex() == Gender.MALE) {
			idToGetDaughter = FamilyManager.getWife(familyTree, id);
		}
		ArrayList<Relationship> list = familyTree.get(idToGetDaughter);
		List<String> response = new ArrayList<String>();
		for (Relationship relationship : list) {
			if (relationship.getRelation() == RelationshipType.DAUGHTER) {
				response.add(familyMemberDetail.get(relationship.getSecondPerson()).getName());
			}
		}
		return response;
	}

	/**
	 * Finds the sibling
	 * 
	 * @param id
	 * @return
	 * @throws RelationshipNotFoundException
	 */
	private List<String> findSibling(Integer id) throws RelationshipNotFoundException {
		Parent parent = parentMapper.get(id);
		if (parent == null) {
			throw new RelationshipNotFoundException("No parent found for the person with id: " + id);
		}
		Person mother = parent.getMother();
		ArrayList<Relationship> list = familyTree.get(mother.getId());
		List<String> response = new ArrayList<String>();
		for (Relationship relationship : list) {
			if ((relationship.getRelation() == RelationshipType.SON
					|| relationship.getRelation() == RelationshipType.DAUGHTER)
					&& relationship.getSecondPerson() != id) {
				response.add(familyMemberDetail.get(relationship.getSecondPerson()).getName());
			}
		}
		return response;
	}
}
