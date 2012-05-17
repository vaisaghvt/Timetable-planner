package timetable.generators.gui;

import java.io.File;

import timetable.core.TimeTable;
import timetable.database.SubjectDatabase;
import timetable.generators.Generator;

public class RegistrationDetails {

	private String userName;
	private SubjectDatabase subjectDatabase;
	private Generator generator;
	private TimeTable chosenTimeTable;

	public RegistrationDetails(String userName,
			SubjectDatabase subjectDatabase, Generator generator) {
		this.userName = userName;
		this.subjectDatabase = subjectDatabase;
		this.generator = generator;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the subjectDatabase
	 */
	public SubjectDatabase getSubjectDatabase() {
		return subjectDatabase;
	}

	/**
	 * @param subjectDatabase
	 *            the subjectDatabase to set
	 */
	public void setSubjectDatabase(SubjectDatabase subjectDatabase) {
		this.subjectDatabase = subjectDatabase;
	}

	/**
	 * @return the generator
	 */
	public Generator getGenerator() {
		return generator;
	}

	/**
	 * @param generator
	 *            the generator to set
	 */
	public void setGenerator(Generator generator) {
		this.generator = generator;
	}

	public void setChosenTimeTable(TimeTable currentTimeTable) {
		this.chosenTimeTable = currentTimeTable;

	}

	public TimeTable getChosenTimeTable() {
		return this.chosenTimeTable;
	}

	public String timeTableExistsAlready(TimeTable currentTimeTable) {
		File directory = new File(userName);
		if (!directory.exists()) {
			return new String();
		} else {

			for (String file : directory.list()) {
				File tempFile = new File(userName + File.separator + file);
				System.out.println(file);
				if (tempFile.exists() && !tempFile.isHidden()) {

					TimeTable tempTimeTable = new TimeTable(userName
							+ File.separator + file, subjectDatabase);
					System.out.println("time table :" + tempTimeTable);
					if (currentTimeTable.equals(tempTimeTable)) {

						return "Time Table has already been stored as " + file;

					}
				}
			}
		}
		return new String();
	}

}
