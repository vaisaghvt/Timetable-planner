package timetable.generators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import timetable.core.Index;
import timetable.core.Subject;
import timetable.core.TimeTable;
import timetable.database.SubjectDatabase;

/**
 * This class generates a timeTable. A scanner is passed to it to initialise the
 * available subjects. Each intereted subject is then added to it. It is added
 * to generated Subject List if it is valid. On calling generateTimeTable, the
 * required TimeTables are generated and stored in the possibleTimeTable List
 * 
 * Assumes all available subjects can be added.
 * 
 * @author vaisaghvt
 * 
 */
public class SimpleGenerator extends Generator {

	private TimeTable currentTimeTable;
	private boolean noChance = false;

	public SimpleGenerator(SubjectDatabase subjectDatabase) {

		super(subjectDatabase, null);
	}

	public SimpleGenerator(SubjectDatabase subjectDatabase,
			Collection<String> completedSubjects) {

		super(subjectDatabase, completedSubjects);

	}

	
	/**
	 * generates the set of timeTables
	 * 
	 * @return numberOfTimetables generated
	 */
	@Override
	public int generateTimeTables() {
		possibleTimeTableList = new HashSet<TimeTable>();
		if (availableSubjects.isEmpty()) {
			System.out.println("Available Subjects not initialized!");
			return 0;
		} else if (interestedSubjects.isEmpty()) {
			System.out.println("No Interested Sujects selected");
			return 0;
		}
		List<Subject> subjectList = new ArrayList<Subject>(interestedSubjects);

		return generateTimeTables(subjectList);
	}

	private int generateTimeTables(List<Subject> subjectList) {
		currentTimeTable = new TimeTable();
		noChance = false;
		recursiveGenerate(subjectList);
		return possibleTimeTableList.size();
	}

	private void recursiveGenerate(List<Subject> subjectList) {

		if (subjectList.isEmpty()) {
			// Successfully created Time table
			this.possibleTimeTableList.add(new TimeTable(currentTimeTable));
			return;
		}

		Set<Subject> blackList = new HashSet<Subject>();

		Subject currentSubject = subjectList.get(0);

		boolean someIndexAdded = false;
		for (Index index : currentSubject.getIndices()) {
			Subject clashingSubject = currentTimeTable.addIndex(index);
			if (clashingSubject == null) {
				subjectList.remove(0);
				recursiveGenerate(subjectList);

				if (noChance) {
					return;
				}
				currentTimeTable.removeIndex(index);
				subjectList.add(0, currentSubject);
				someIndexAdded = true;

			} else {
				blackList.add(clashingSubject);
				continue;
			}
		}

		if (!someIndexAdded) {
			// No index added for some subject
			System.out.println(currentSubject
					+ "could not be added because of " + blackList);
			noChance = true;
		}
	}

	/**
	 * @return the possibleTimeTableList
	 */
	@Override
	public Collection<TimeTable> getPossibleTimeTableList() {
		return possibleTimeTableList;
	}

}
