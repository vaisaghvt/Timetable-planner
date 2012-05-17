package timetable.generators;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import timetable.core.Subject;
import timetable.core.TimeTable;
import timetable.database.SubjectDatabase;

public abstract class Generator {

	protected Collection<TimeTable> possibleTimeTableList;
	protected final Collection<Subject> availableSubjects;
	protected Collection<Subject> interestedSubjects;
	private Collection<String> completedSubjects;

	public Generator(SubjectDatabase subjectDatabase,
			Collection<String> completedSubjects) {
		if (completedSubjects != null) {
			this.completedSubjects = new HashSet<String>(completedSubjects);
		} else {
			this.completedSubjects = new HashSet<String>();
		}
		availableSubjects = new HashSet<Subject>(
				subjectDatabase.getAllSubjects());
		interestedSubjects = new HashSet<Subject>();
		possibleTimeTableList = new HashSet<TimeTable>();
	}

	public String addIntrestedSubject(Subject subject) {
		if (availableSubjects.contains(subject)) {

			for (Subject existingInterestedSubject : this.interestedSubjects) {
				if(existingInterestedSubject.getName().equals(subject.getName())){
					return existingInterestedSubject.getName()+ " has already been added to list";
				}
				
				if (existingInterestedSubject.clashesWith(subject)) {

					return subject.getName() + "'s exam clashes with "
							+ existingInterestedSubject.getName();
				}
			}
			for (String preRequisite : subject.getPreRequisites()) {
				if (!this.completedSubjects.contains(preRequisite)) {
					return "You haven't completed the pre-Requisite: "
							+ preRequisite;
				}
			}

			interestedSubjects.add(subject);
			return "";
		}
		
		return subject + " is not available for you";
	}

	/**
	 * Adds all interested subjects to list of subjects. Outputs an error to
	 * console if subject could not be added.
	 * 
	 * @param subjects
	 *            to be added
	 */

	public boolean addAllInterestedSubjects(Collection<Subject> subjects) {
		if (!availableSubjects.containsAll(subjects)) {
			Set<Subject> tempAddingList = new HashSet<Subject>(subjects);
			tempAddingList.removeAll(availableSubjects);
			System.out
					.println("Some/All of these subjects aren't available for you :"
							+ tempAddingList);
			return false;
		}
		for (Subject subjectToBeAdded : subjects) {
			for (Subject existingInterestedSubject : this.interestedSubjects) {
				if (existingInterestedSubject.clashesWith(subjectToBeAdded)) {
					System.out.println(subjectToBeAdded
							+ "'s exam clashes with "
							+ existingInterestedSubject);
					return false;
				}
				for (String preRequisite : subjectToBeAdded.getPreRequisites()) {
					if (!this.completedSubjects.contains(preRequisite)) {
						System.out
								.println("You haven't completed the pre-Requisite+"
										+ preRequisite);
						return false;
					}
				}
			}
		}
		this.interestedSubjects.addAll(subjects);
		return true;
	}

	public void removeInterestedSubject(Subject subject) {
		interestedSubjects.remove(subject);
	}

	public void removeAllInterestedSubjects(Collection<Subject> subjects) {
		interestedSubjects.removeAll(subjects);
	}

	public void clearInterestedSubjects() {
		interestedSubjects.clear();
	}

	/**
	 * generates the set of timeTables
	 * 
	 * @return numberOfTimetables generated
	 */
	public abstract int generateTimeTables();

	/**
	 * @return the possibleTimeTableList
	 */
	public abstract Collection<TimeTable> getPossibleTimeTableList();

	public Collection<? extends Subject> getInterestedSubjects() {
	
		return this.interestedSubjects;
	}
}
