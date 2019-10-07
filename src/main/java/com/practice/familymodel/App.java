package com.practice.familymodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.practice.familymodel.exceptions.InvalidGenderException;
import com.practice.familymodel.exceptions.PersonNotFoundException;
import com.practice.familymodel.exceptions.RelationshipNotFoundException;
import com.practice.familymodel.exceptions.UndefinedRelationshipException;
import com.practice.familymodel.services.FamilyTree;
import com.practice.familymodel.services.RelationshipFinder;
import com.practice.familymodel.utils.AppConstant;

/**
 * Hello world!
 *
 */
public class App {
	/**
	 * Main driver of the application
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		/*
		 * Get the King Shan Family from file. We can initialize any family given the
		 * family in file provided by filePath
		 */
		final Logger LOGGER = Logger.getLogger(App.class);
		String filePath = AppConstant.FAMILY_FILE_PATH;
		File file = new File(filePath);
		FamilyTree shanFamily = new FamilyTree();
		LOGGER.info("Initiazing family tree...");
		shanFamily.initializeTree(file);
		LOGGER.info("Family tree initialised.");

		RelationshipFinder relationshipFinder = shanFamily.getRelationshipFinder();
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(new File(AppConstant.INPUT_FILE_PATH)));
		String line = reader.readLine();
		while (line != null) {
			if (line.equals("q") || line.equals("Q")) {
				reader.close();
				return;
			}
			String[] params = line.split(" ");
			if (params[0].trim().equals("ADD_CHILD")) {
				String motherName = params[1].trim();
				String name = params[2].trim();
				String gender = params[3].trim();
				try {
					LOGGER.info("Adding child with following params: " + motherName + " " + name + " " + gender);
					shanFamily.addChild(motherName, name, gender);
					System.out.println("CHILD_ADDITION_SUCCEEDED");
					LOGGER.info("Child addition succesful");
				} catch (InvalidGenderException exception) {
					LOGGER.error("Invalid specified gender:", exception);
					System.out.println("CHILD_ADDITION_FAILED");
				} catch (PersonNotFoundException exception) {
					System.out.println("PERSON_NOT_FOUND");
					LOGGER.error("Invalid specified person:", exception);
				} catch (UndefinedRelationshipException exception) {
					LOGGER.error("Invalid specified relationship:", exception);
					System.out.println("CHILD_ADDITION_FAILED");
				} catch (RelationshipNotFoundException exception) {
					LOGGER.error("Invalid specified relationship:", exception);
					System.out.println("CHILD_ADDITION_FAILED");
				} catch (IndexOutOfBoundsException exception) {
					LOGGER.error("Insufficient arguements specified while Addition of child.", exception);
					System.out.println("PERSON_NOT_FOUND");
				}
				LOGGER.info("");

			} else if (params[0].trim().equals("GET_RELATIONSHIP")) {
				String name = params[1];
				String relation = params[2];
				String[] response = {};
				try {
					response = relationshipFinder.findRelative(name, relation);
					for (String item : response) {
						sb.append(item + " ");
					}
					System.out.println(sb);
				} catch (PersonNotFoundException exception) {
					LOGGER.error("Invalid person relationship:", exception);
					System.out.println("NONE");
				} catch (RelationshipNotFoundException exception) {
					LOGGER.error("Invalid specified relationship:", exception);
					System.out.println("NONE");
				}

			}
			line = reader.readLine();
		}
		reader.close();
	}
}
