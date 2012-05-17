package timetable.database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import timetable.core.Index;
import timetable.core.Subject;
import timetable.core.TimeSlot;
import timetable.core.TimeSlot.SessionType;

public class SubjectDatabase {
	Set<Subject> subjectDatabase;
	HashMap<Subject, String> categoryForSubject;
	HashMap<String, HashSet<Subject>> subjectsForCategory;
	
	public SubjectDatabase(File inputFile) {
		
		Scanner sc = null;
		
		try {
			sc = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int numberOfSubjects = sc.nextInt();

		subjectDatabase = new HashSet<Subject>();
		categoryForSubject = new HashMap<Subject, String>();
		
		subjectsForCategory = new HashMap<String, HashSet<Subject>>();
		sc.nextLine();
		for (int i = 0; i < numberOfSubjects; i++) {
		
			String subjectName = sc.nextLine();
			System.out.println(subjectName);

			// TODO : realistically more than one line
			String description = sc.nextLine();

			String category = sc.nextLine();
			
			String code = sc.next();

			int numberOfPreReqs = sc.nextInt();

			List<String> preReqStrings = new ArrayList<String>();
			for (int j = 0; j < numberOfPreReqs; j++) {
				preReqStrings.add(sc.next());
			}

			Boolean hasExamination = sc.nextBoolean();
			TimeSlot timeSlot = null;
			LocalTime startTime = null;
			LocalTime endTime = null;
			if (hasExamination) {
				int day = sc.nextInt();
				int month = sc.nextInt();
				int year = sc.nextInt();

				LocalDate date = new LocalDate(year, month, day);
				startTime = constructNewLocalTime(sc);
				endTime = constructNewLocalTime(sc);// TODO : DO SANITY CHECK
				
				String location = sc.nextLine();
				timeSlot = new TimeSlot(date, startTime, endTime,
						SessionType.EXAM, location);
			}

			Subject currentSubject = new Subject(subjectName, description, category, 
					code, preReqStrings, timeSlot);

			startTime = null;
			endTime = null;
			int numberOfIndices = sc.nextInt();
			Set<Index> indices = new HashSet<Index>();
			for (int j = 0; j < numberOfIndices; j++) {
				String indexCode = sc.next();
				int numberOfTimeSlots = sc.nextInt();
				Set<TimeSlot> timeSlots = new HashSet<TimeSlot>();
				for (int k = 0; k < numberOfTimeSlots; k++) {
					SessionType sessionType = SessionType.valueOf(sc.next());
					String day = sc.next();
					startTime = constructNewLocalTime(sc);
					endTime = constructNewLocalTime(sc);
					String location = sc.nextLine();
					timeSlots.add(new TimeSlot(day, startTime, endTime,
							sessionType,location));
				}
				indices.add(new Index(timeSlots, currentSubject, indexCode));
			}
			currentSubject.initializeIndices(indices);

			this.subjectDatabase.add(currentSubject);
			this.categoryForSubject.put(currentSubject, category);
		
			if(this.subjectsForCategory.get(category) == null){
				this.subjectsForCategory.put(category, new HashSet<Subject>());
			}
			this.subjectsForCategory.get(category).add(currentSubject);
			
		}
		sc.close();
	}

	public SubjectDatabase() {
		subjectDatabase = new HashSet<Subject>();
	}

	private LocalTime constructNewLocalTime(Scanner sc) {

		int hour = sc.nextInt();
		int minute = sc.nextInt();

		return new LocalTime(hour, minute);
	}

	public Collection<? extends Subject> getAllSubjects() {

		return this.subjectDatabase;
	}

	public String getSubjectNameForCode(String code) {
		for (Subject subject : this.subjectDatabase) {
			if (subject.getCode().equals(code)) {
				return subject.getName();
			}

		}
		return null;
	}

	public Subject getSubjectForCode(String code) {
		for (Subject subject : this.subjectDatabase) {
			if (subject.getCode().equals(code)) {
				return subject;
			}
		}
		return null;
	}

	public Subject getSubjectForName(String name) {
		for (Subject subject : this.subjectDatabase) {
			if (subject.getName().equals(name)) {
				return subject;
			}

		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SubjectDatabase [subjectDatabase=" + subjectDatabase + "]";
	}

	public void saveToFile(File file) {
		PrintWriter output = null;
		try {
			output = new PrintWriter(new BufferedWriter(new FileWriter(file,
					false)));
		} catch (IOException e) {

			e.printStackTrace();
		}

		output.println(this.subjectDatabase.size());

		for (Subject subject : this.subjectDatabase) {

			output.println(subject.getName());

			output.println(subject.getDescription());
			
			output.println(subject.getCategory());

			output.println(subject.getCode());

			output.println(subject.getPreRequisites().size());

			if (!subject.getPreRequisites().isEmpty()) {
				for (String preReq : subject.getPreRequisites()) {
					output.print(preReq + "\t");
				}
				output.println();
			}
			output.println(subject.hasExam());

			if (subject.hasExam()) {
				output.print(subject.getExaminationDateTime().getDate()
						.getDayOfMonth()
						+ " ");
				output.print(subject.getExaminationDateTime().getDate()
						.getMonthOfYear()
						+ " ");
				output.print(subject.getExaminationDateTime().getDate()
						.getYear()
						+ "\t");

				output.print(subject.getExaminationDateTime().getStartTime()
						.getHourOfDay()
						+ " ");
				output.print(subject.getExaminationDateTime().getStartTime()
						.getMinuteOfHour()
						+ "\t");

				output.print(subject.getExaminationDateTime().getEndTime()
						.getHourOfDay()
						+ " ");
				output.print(subject.getExaminationDateTime().getEndTime()
						.getMinuteOfHour()
						+ "\n");
			}

			output.println(subject.getIndices().size());
			for (Index index : subject.getIndices()) {
				output.println(index.getIndexCode());
				output.println(index.getTimeSlots().size());
				for (TimeSlot timeSlot : index.getTimeSlots()) {
					output.print(timeSlot.getType() + "\t");
					output.print(timeSlot.getDay() + "\t");

					output.print(timeSlot.getStartTime().getHourOfDay() + " ");
					output.print(timeSlot.getStartTime().getMinuteOfHour()
							+ "\t");

					output.print(timeSlot.getEndTime().getHourOfDay() + " ");
					output.print(timeSlot.getEndTime().getMinuteOfHour() + "\n");

				}
			}
		}
		output.close();
	}

	public List<String> getAllSubjectNames() {
		List<String> names = new ArrayList<String>();

		for (Subject subject : subjectDatabase) {
			names.add(subject.getName());
		}
		return names;
	}

	public void remove(Subject currentSubject) {

		this.subjectDatabase.remove(currentSubject);

	}

	public void addSubject(Subject currentSubject) {
		this.subjectDatabase.add(currentSubject);

	}

	public Collection<? extends String> getAllSubjectNamesWithCodes() {
		List<String> namesWithCodes = new ArrayList<String>();

		for (Subject subject : subjectDatabase) {
			namesWithCodes.add(subject.getCode()+" :\t "+subject.getName());
		}
//		System.out.println(namesWithCodes.size());
		return namesWithCodes;
	}
	
	public Set<String> getCategories(){
		return this.subjectsForCategory.keySet();
	}
	
	public String getCategoryForSubject(Subject subject){
		return this.categoryForSubject.get(subject);
	}
	
	public String getCategoryForSubjectName(String subjectName){
		return this.categoryForSubject.get(this.getSubjectForName(subjectName));
	}
	
	public String getCategoryForSubjectCode(String subjectCode){
		return this.categoryForSubject.get(this.getSubjectForCode(subjectCode));
	}

	public Collection<? extends String> getAllSubjectNamesWithCodesForCategory(
			String currentCategory) {
		List<String> namesWithCodes = new ArrayList<String>();

		for (Subject subject : subjectsForCategory.get(currentCategory)) {
			namesWithCodes.add(subject.getCode()+" :\t "+subject.getName());
		}
//		System.out.println(namesWithCodes.size());
		return namesWithCodes;
	}

	public Index getIndexDetailsForCode(String indexCode) {
		for(Subject subject:this.subjectDatabase){
			for(Index index :subject.getIndices()){
				if(index.getIndexCode().equalsIgnoreCase(indexCode)){
					return index;
				}
			}
		}
		return null;
		
	}
}
