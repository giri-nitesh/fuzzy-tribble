package com.practice.familymodel.utils;

import java.util.HashMap;
import java.util.Map;

import com.practice.familymodel.utils.RelationshipConstants.RelationshipType;

public class AppConstant {

	public static final String FAMILY_FILE_PATH = "../familytree/src/main/resources/shan_family.txt";

	public static final String INPUT_FILE_PATH = "../familytree/src/main/resources/input.txt";

	public static final int FAMILY_SIZE = 1000;

	public static final String PATTERN = "\\<(.*?)\\>";

	public static final int ARGS_SIZE = 3;

	public static final Map<String, RelationshipType> RELATION_MAPPER = new HashMap<String, RelationshipConstants.RelationshipType>();

	static {
		RELATION_MAPPER.put("Paternal-Uncle", RelationshipType.PATERNAL_UNCLE);
		RELATION_MAPPER.put("Maternal-Uncle", RelationshipType.MATERNAL_UNCLE);
		RELATION_MAPPER.put("Paternal-Aunt", RelationshipType.PATERNAL_AUNT);
		RELATION_MAPPER.put("Maternal-Aunt", RelationshipType.MATERNAL_AUNT);
		RELATION_MAPPER.put("Sister-In-Law", RelationshipType.SISTER_IN_LAW);
		RELATION_MAPPER.put("Brother-In-Law", RelationshipType.BROTER_IN_LAW);
		RELATION_MAPPER.put("Son", RelationshipType.SON);
		RELATION_MAPPER.put("Daughter", RelationshipType.DAUGHTER);
		RELATION_MAPPER.put("Siblings", RelationshipType.SIBLING);
	}
}
