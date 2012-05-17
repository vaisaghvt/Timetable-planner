package timetable.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.joda.time.LocalTime;

import timetable.core.TimeSlot.Day;
import timetable.core.TimeSlot.SessionType;
import timetable.database.SubjectDatabase;

public class TimeTable {

	private Color TUTORIAL_COLOUR = new Color(205,92,92);
	private Color LECTURE_COLOUR = new Color(49, 79, 79);
	private Color LAB_COLOUR = new Color(49, 79, 79);
	private Color TUTORIAL_TEXT_COLOUR =Color.BLACK;
	private Color LECTURE_TEXT_COLOUR = new Color(245, 255, 250);
	private Color LAB_SLOT_COLOUR = new Color(245, 255, 250);
	private Color BACKGROUND_COLOUR = new Color(255, 245, 238);
	private Color LINE_COLOUR = new Color(49, 79, 79);
	
	
	
	Set<Index> indices;
	Set<Subject> subjects;
	Set<TimeSlot> timeSlots;
	String imageFile;
	private int width;
	private int timeSlotWidth;
	private int height;
	private int startY;
	private int startX;
	private LocalTime lastStartingSession;
	private LocalTime firstStartingSession;
	private double fiveMinuteHeight;
	private int thirtyMinuteHeight;

	public TimeTable() {
		indices = new HashSet<Index>();
		subjects = new HashSet<Subject>();
		timeSlots = new TreeSet<TimeSlot>();
	}

	public TimeTable(TimeTable currentTimeTable) {
		this.indices = new HashSet<Index>(currentTimeTable.getIndices());
		this.subjects = new HashSet<Subject>(currentTimeTable.getSubjects());
		this.timeSlots = new TreeSet<TimeSlot>(currentTimeTable.getTimeSlots());
	}

	public TimeTable(String fileName, SubjectDatabase database) {
		indices = new HashSet<Index>();
		subjects = new HashSet<Subject>();
		timeSlots = new TreeSet<TimeSlot>();
		File timeTableFile = new File(fileName);
		assert timeTableFile.exists();
		
		try {
			//TODO : better exception handling
			Scanner sc = new Scanner(timeTableFile);
			int numberOfIndices = sc.nextInt();
			for(int i=0;i<numberOfIndices; i++){
				String indexCode = sc.next();
				String subjectCode = sc.next();
				subjectCode.trim();
				Index index = database.getIndexDetailsForCode(indexCode);
				if(index == null){
					System.out.println("Invalid database or file. Contact Software Vendor");					
				}else if(!index.getSubject().equals(database.getSubjectForCode(subjectCode))){
					System.out.println("Invalid index code subject mismatch. Index"+ index.getIndexCode()+" Subject "+subjectCode);
				}else{
					indices.add(index);
					subjects.add(index.getSubject());
					System.out.println(index.getSubject().getCode());
					System.out.println(index.getTimeSlots());
					timeSlots.addAll(index.getTimeSlots());
					
				}
			}
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		System.out.println(this.timeSlots);
		
	}

	public Collection<? extends Subject> getSubjects() {

		return this.subjects;
	}

	Collection<? extends Index> getIndices() {
		return this.indices;
	}

	/**
	 * Adds an index to the current timetable. Checks should actually be done
	 * earlier. This is only a sanity check. Change later to throw an exceptionn
	 * 
	 * @param indices
	 *            to be added to list
	 * @return true :if successfully added. Also, internally the list has added
	 *         index false :if any one couldn't be added. Also, internally none
	 *         of these elements are added.
	 */
	public Subject addIndex(Index index) {
		Subject clashingSubject = addIndexToList(index, indices, this.subjects);
		if (clashingSubject == null) {

			timeSlots.addAll(index.getTimeSlots());

			return null;
		}
		return clashingSubject;
	}

	private static final Subject addIndexToList(Index indexToBeAdded,
			Set<Index> passedIndices, Set<Subject> passedSubjects) {
		if (passedSubjects.contains(indexToBeAdded.getSubject())) {
			System.out.println(indexToBeAdded.getSubject() + "already added");
			return indexToBeAdded.getSubject();
		}
		for (Index tempIndex : passedIndices) {
			if (tempIndex.clashesWith(indexToBeAdded)) {
				return tempIndex.getSubject();
			}
		}
		passedIndices.add(indexToBeAdded);
		passedSubjects.add(indexToBeAdded.getSubject());
		return null;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((indices == null) ? 0 : indices.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeTable other = (TimeTable) obj;
		if (indices == null) {
			if (other.indices != null)
				return false;
		} else if (!indices.equals(other.indices)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "TimeTable [possibleIndices=" + indices + "]";
		// return "TimeTable [timeSlots=" + this.timeSlots + "]";
	}

	/**
	 * @return the timeSlots
	 */
	public Set<TimeSlot> getTimeSlots() {
		return timeSlots;
	}

	public void removeIndex(Index index) {
		this.timeSlots.removeAll(index.getTimeSlots());
		this.indices.remove(index);
		this.subjects.remove(index.getSubject());

	}

	/**
	 * This function should return an image file.
	 * 
	 * @return
	 */
	public String getImageFile() {
		return this.imageFile;
	}

	public void createImage(String fileLocation) {
		
		BufferedImage image = new BufferedImage(1080, 720,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();

		createImageForTimeTable(image, graphics);

		
		try {
			File outputfile = new File(fileLocation);
			this.imageFile = fileLocation;
			ImageIO.write(image, "png", outputfile);
			outputfile.deleteOnExit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void createImageForTimeTable(BufferedImage image, Graphics2D graphics) {

		lastStartingSession = new LocalTime(18, 0);
		firstStartingSession = new LocalTime(8, 0);

		for (Index index : this.indices) {
			for (TimeSlot timeSlot : index.getTimeSlots()) {
				if (timeSlot.getStartTime().isBefore(firstStartingSession.plusMinutes(30))) {
					firstStartingSession = timeSlot.getStartTime().minusMinutes(30);
				}
				if (timeSlot.getEndTime().isAfter(
						lastStartingSession.plusMinutes(30))) {
					lastStartingSession = timeSlot.getEndTime()
							.minusMinutes(30);
				}
			}
		}

		width = image.getWidth();
		height = image.getHeight() - 20;
		timeSlotWidth = ((width - 15) / (Day.values().length + 1));
		startX = 5;
		startY = 5;

		int currentStartX = startX;
		int currentStartY = startY;

		int totalFiveMinuteDifferences = this
				.findNumberOfFiveMinuteDifferences(
						lastStartingSession.plusMinutes(30),
						firstStartingSession);
		fiveMinuteHeight = (double) (height)
				/ (double) (totalFiveMinuteDifferences);

		graphics.setStroke(new BasicStroke(5));
		graphics.setColor(BACKGROUND_COLOUR);
		graphics.fillRect(0, 0, width, height);
		graphics.setColor(LINE_COLOUR);
		thirtyMinuteHeight = (int) Math.round(fiveMinuteHeight * 6);
		graphics.drawRect(startX, startY,
				startX + ((timeSlotWidth - 2) * (Day.values().length + 1)),
				height *thirtyMinuteHeight/thirtyMinuteHeight);

		for (int i = 1; i < Day.values().length + 1; i++) {
			graphics.drawLine(startX + i * (timeSlotWidth - 1), startY, startX
					+ i * (timeSlotWidth - 1), height *thirtyMinuteHeight/thirtyMinuteHeight);
			graphics.drawString(Day.values()[i - 1].toString(), startX + i
					* (timeSlotWidth - 1) + timeSlotWidth / 2 - 2, startY
					+ Math.round(fiveMinuteHeight * 5));
		}

		// for(TimeSlot timeSlot: this.timeSlots){
		// System.out.println(timeSlot.getFormattedDetails());
		// }

		totalFiveMinuteDifferences = this.findNumberOfFiveMinuteDifferences(
				lastStartingSession.plusMinutes(30), firstStartingSession);
		graphics.setColor(Color.black);
		// System.out.println(fiveMinuteSize);
		int currentEndX, currentEndY;
		// graphics.setColor(new Color(245,255,250));
		LocalTime currentTime = new LocalTime(firstStartingSession);
		graphics.setStroke(new BasicStroke(1));
		
		for (int i = startY; i < height*thirtyMinuteHeight/thirtyMinuteHeight ; i += thirtyMinuteHeight ) {
			graphics.drawLine(startX, i, width - 20, i);
			if (i != startY) {
				graphics.drawString(currentTime.toString("HH:mm") + " - "
						+ currentTime.plusMinutes(30).toString("HH:mm"),
						startX + 20,
						(int) ((((2 * i) + thirtyMinuteHeight) / 2) + 5));
			}
			currentTime = currentTime.plusMinutes(30);
			if (currentTime.isAfter(lastStartingSession.plusMinutes(30))) {
				break;
			}
//			System.out.println("y value=" + i);
		}
		graphics.setStroke(new BasicStroke(2));
		for (TimeSlot timeSlot : this.getTimeSlots()) {
System.out.println(timeSlot);
			currentStartX = startX + (timeSlot.getDay().ordinal() + 1)
					* (timeSlotWidth - 1);
			currentEndX = currentStartX + timeSlotWidth - 1;

			totalFiveMinuteDifferences = this
					.findNumberOfFiveMinuteDifferences(timeSlot.getStartTime(),
							firstStartingSession);
			currentStartY = startY
					+ ((int) Math.round(totalFiveMinuteDifferences
							* fiveMinuteHeight)  * thirtyMinuteHeight/ thirtyMinuteHeight);

			totalFiveMinuteDifferences = this
					.findNumberOfFiveMinuteDifferences(timeSlot.getEndTime(),
							timeSlot.getStartTime());
			currentEndY = currentStartY
					+ (int) (Math.round(totalFiveMinuteDifferences
							* fiveMinuteHeight));
//			System.out.println("starty" + currentStartY + ", endy"
//					+ currentEndY);
			if(timeSlot.getType() == SessionType.TUTORIAL){
				graphics.setColor(TUTORIAL_COLOUR);
			}else if(timeSlot.getType() == SessionType.LECTURE){
				graphics.setColor(LECTURE_COLOUR);
			}else if(timeSlot.getType() == SessionType.LAB){
				graphics.setColor(LAB_COLOUR);
			}
			

			graphics.fillRect(currentStartX + 4, currentStartY, currentEndX
					- currentStartX - 8, currentEndY - currentStartY);

			graphics.setColor(BACKGROUND_COLOUR);
			graphics.drawRect(currentStartX + 4, currentStartY, currentEndX
					- currentStartX - 8, currentEndY - currentStartY);
			
			if(timeSlot.getType() == SessionType.TUTORIAL){
				graphics.setColor(TUTORIAL_TEXT_COLOUR);
			}else if(timeSlot.getType() == SessionType.LECTURE){
				graphics.setColor(LECTURE_TEXT_COLOUR);
			}else if(timeSlot.getType() == SessionType.LAB){
				graphics.setColor(LAB_SLOT_COLOUR);
			}
			// graphics.setColor(Color.black);
			
			graphics.drawString(timeSlot.getIndex().getSubject().getCode(),
					(int) (currentStartX + 75-timeSlot.getIndex().getSubject().getCode().length()*4),
					(int) ((currentStartY + currentEndY) / 2 -8));
			graphics.drawString(timeSlot.getLocation(),
					(int) (currentStartX + 75-timeSlot.getLocation().length()*4),
					(int) ((currentStartY + currentEndY) / 2 + 15));

		}

	}

	public boolean hasImage() {
		return this.imageFile != null;
	}

	private int findNumberOfFiveMinuteDifferences(LocalTime endTime,
			LocalTime startTime) {

		final int hourDifference = endTime.getHourOfDay()
				- startTime.getHourOfDay();
		final int minuteDifference = endTime.getMinuteOfHour()
				- startTime.getMinuteOfHour();
		final int totalFiveMinuteDifferences = (hourDifference * 60 + minuteDifference) / 5;
		// System.out.println("From"+startTime+"To"+endTime+"the total gaps ="+totalFiveMinuteDifferences);
		return totalFiveMinuteDifferences;

	}

	public String getFormattedExams() {
		StringBuilder result = new StringBuilder();
		for (Subject subject : this.subjects) {
			result.append(subject.getName() + ":\t");
			if (subject.hasExam())
				result.append(subject.getExaminationDateTime()
						.getFormattedDetails());
			else
				result.append(" - No Exam - \n");

		}

		return result.toString();
	}

	public Subject findSubjectForLocation(int x, int y) {
		// System.out.println(x+","+y);
		for (Index index : this.indices) {
			for (TimeSlot timeSlot : index.getTimeSlots()) {

				int currentStartX = startX + (timeSlot.getDay().ordinal() + 1)
						* (timeSlotWidth - 1);
				if (x < currentStartX) {
					continue;
				}

				int currentEndX = currentStartX + timeSlotWidth - 1;
				if (x > currentEndX) {
					continue;
				}

				int totalFiveMinuteDifferences = this
						.findNumberOfFiveMinuteDifferences(
								timeSlot.getStartTime(), firstStartingSession);
				int currentStartY = startY
						+ ((int) Math.round(totalFiveMinuteDifferences
								* fiveMinuteHeight) / thirtyMinuteHeight * thirtyMinuteHeight);
				if (y < currentStartY) {
					continue;
				}

				totalFiveMinuteDifferences = this
						.findNumberOfFiveMinuteDifferences(
								timeSlot.getEndTime(), timeSlot.getStartTime());
				int currentEndY = currentStartY
						+ (int) (Math.round(totalFiveMinuteDifferences
								* fiveMinuteHeight));
				if (y > currentEndY) {
					continue;
				}

				return index.getSubject();
			}
		}
		return null;
	}
	
	public boolean saveTimeTableToFile(String directoryName, String fileName) {
		File directory = new File(directoryName);
		if (!directory.exists()) {
			if (!directory.mkdir()) {
				System.out.println("Type Directory could not be created for "
						+ directory);
				return false;
			}
		}
		File outputFile = new File(directoryName+File.separator+fileName);
		PrintWriter writing = null;
		try {
			
			writing = new PrintWriter(new BufferedWriter(new FileWriter(
					outputFile)));
			writing.println(this.indices.size());
			for(Index index :this.indices){
				writing.println(index.getIndexCode() + "\t" + index.getSubject().getCode());
			}
			writing.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
