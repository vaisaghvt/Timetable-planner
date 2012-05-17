package timetable.generators.gui.main;

import java.io.File;

import timetable.core.Subject;
import timetable.database.SubjectDatabase;
import timetable.generators.SimpleGenerator;

public class GeneratorMain {

	private static final String SUBJECT_DATABASE_FILE ="subjectDatabase3.txt";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File inputFile = new File(SUBJECT_DATABASE_FILE);
		if (!inputFile.exists()) {
			System.out.println("Argument is not a file");
		} else {

			SubjectDatabase subjectDatabase = new SubjectDatabase(inputFile);

			SimpleGenerator currentGenerator = new SimpleGenerator(
					subjectDatabase);
			for(Subject subject: subjectDatabase.getAllSubjects()){
				System.out.println(subject.getCode());
			}
			new UserApp(subjectDatabase, currentGenerator);
		}
	}

}
