package com.practice.familymodel.utils;

import java.util.HashMap;

/**
 * 
 * @author Nitesh
 * Application constants are loaded from this file
 */
public class RelationshipConstants {
	
	/**
	 * 
	 * @author Nitesh
	 * Predefined types of relationship which has been handled in this application
	 */
	public enum RelationshipType{
		HUSBAND,
		WIFE,
		SON,
		DAUGHTER,
		BROTHER,
		SISTER,
		PATERNAL_UNCLE,
		MATERNAL_UNCLE,
		PATERNAL_AUNT,
		MATERNAL_AUNT,
		SISTER_IN_LAW,
		BROTER_IN_LAW,
		FATHER,
		MOTHER,
		SIBLING
	}
	
	/**
	 * Stores the complimentary relationship
	 * For Ex: complimentary relationship of mother is son, while that of husband is wife
	 */
	public static final HashMap<RelationshipType, RelationshipType> COMPLIMENTARY_RELATION = new 
			HashMap<RelationshipType, RelationshipType>();
	
	/**
	 * Stores all the relationships in integer format so that we can parse the 
	 * relationships from input
	 */
	public static final HashMap<Integer, RelationshipType> RELATION = new 
			HashMap<Integer, RelationshipType>();
	
	/*
	 * Initialize Relation map. It's sole purpose is for creating family
	 * tree at first. We take input relation as integer.
	 * It gets initialized whenever we access any property of this class.
	 */
	static {
		RELATION.put(1, RelationshipType.HUSBAND);
		RELATION.put(2, RelationshipType.WIFE);
		RELATION.put(3, RelationshipType.SON);
		RELATION.put(4, RelationshipType.DAUGHTER);
	}
	
	static {
		COMPLIMENTARY_RELATION.put(RelationshipType.HUSBAND, RelationshipType.WIFE);
		COMPLIMENTARY_RELATION.put(RelationshipType.WIFE, RelationshipType.HUSBAND);
		COMPLIMENTARY_RELATION.put(RelationshipType.SON, RelationshipType.MOTHER);
		COMPLIMENTARY_RELATION.put(RelationshipType.MOTHER, RelationshipType.SON);
		COMPLIMENTARY_RELATION.put(RelationshipType.DAUGHTER, RelationshipType.MOTHER);
		COMPLIMENTARY_RELATION.put(RelationshipType.MOTHER, RelationshipType.DAUGHTER);
	}

}
