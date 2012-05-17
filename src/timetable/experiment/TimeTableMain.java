package timetable.experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import timetable.core.Subject;
import timetable.core.TimeTable;
import timetable.database.SubjectDatabase;
import timetable.generators.SimpleGenerator;

public class TimeTableMain {
	private static final String USER_INPUT_FILE ="userInput.txt";
	private static final String SUBJECT_DATABASE_FILE ="subjectDatabase2.txt";

	public static void main(String[] args) {
		
			File inputFile = new File(SUBJECT_DATABASE_FILE);
			if (!inputFile.exists()) {
				System.out.println("Argument is not a file");
			} else {
			

				SubjectDatabase subjectDatabase = new SubjectDatabase(
						inputFile);

				SimpleGenerator currentGenerator = new SimpleGenerator(
						subjectDatabase);

				File individualInput = new File(USER_INPUT_FILE);

				Scanner user = null;
				try {
					user = new Scanner(individualInput);
				} catch (FileNotFoundException e) {
					// Exception can't be thrown since already checked...
					e.printStackTrace();
				}

				while (user.hasNext()) {
					String subjectCode = user.next();
					Subject subject = subjectDatabase.getSubjectForCode(subjectCode);
					if(subject == null){
						System.out.println(subjectCode + " is not in database ");
						continue;
					}
					currentGenerator.addIntrestedSubject(subject);

				}
				System.out.println("Number of timeTables generated = "+currentGenerator.generateTimeTables());
				
				Set<TimeTable> generatedTables = new HashSet<TimeTable>(currentGenerator.getPossibleTimeTableList());
				
				for(TimeTable table:generatedTables){
					System.out.println(table);
				}
			}

		}
	
}
