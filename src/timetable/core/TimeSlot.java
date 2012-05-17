package timetable.core;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

public class TimeSlot implements Comparable<TimeSlot> {
	public static enum SessionType {
		LECTURE, TUTORIAL, SEMINAR, LAB, EXAM, SIMPLE
	}

	public static enum Day {
		MON, TUE, WED, THUR, FRI ;
	}

	private LocalDate date;
	private Day day;
	private final LocalTime startTime;
	private final LocalTime endTime;
	private SessionType type;
	private Index index;
	private final String location;
	
	public TimeSlot(String day, LocalTime startTime, LocalTime endTime,
			SessionType type, String location) {

		this.day = Day.valueOf(day);
		this.startTime = startTime;
		this.endTime = endTime;
		this.type = type;
		this.location = location;
	}

	public TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime,
			SessionType type, String location) {
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.type = type;
		this.location = location;
	}

	/**
	 * @return the type
	 */
	public SessionType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(SessionType type) {
		this.type = type;
	}

	public Day getDay() {
		return day;
	}

	/**
	 * @return the startTime
	 */
	public LocalTime getStartTime() {
		return startTime;
	}

	/**
	 * @return the endTime
	 */
	public LocalTime getEndTime() {
		return endTime;
	}

	public Index getIndex() {
		return index;
	}
	
	public String getLocation(){
		return location;
	}

	/**
	 * Violates compareTo contract. Returns 0 if clashing. -1 if this ends
	 * before other startas and +1 if this starts after the other one ends
	 */

	@Override
	public int compareTo(TimeSlot timeSlot) {
		if (day != null) {
			if (this.day.compareTo(timeSlot.getDay()) == 0) {
				if (this.endTime.isBefore(timeSlot.getStartTime())||this.endTime.equals(timeSlot.getStartTime())) {
					return -1;
				} else if (this.startTime.isAfter(timeSlot.getEndTime())||this.startTime.equals(timeSlot.getEndTime())) {
					return +1;
				} else {
					return 0;
				}
			} else {
				return this.day.compareTo(timeSlot.day);
			}
		} else {
			if (this.date.isBefore(timeSlot.getDate())) {
				return -1;
			} else if (this.date.isAfter(timeSlot.getDate())) {
				return +1;
			} else {
				if (this.endTime.isBefore(timeSlot.getStartTime())) {
					return -1;
				} else if (this.startTime.isAfter(timeSlot.getEndTime())) {
					return +1;
				} else {
					return 0;
				}
			}
		}

	}

	public boolean clashesWith(TimeSlot timeSlot) {
		return (this.compareTo(timeSlot)) == 0 ? true : false;
	}

	public void setIndex(Index index) {
		this.index = index;
	}

	public LocalDate getDate() {
		return date;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (this.type == SessionType.EXAM)
			return "TimeSlot [date=" + date.toString(DateTimeFormat.forPattern("dd-MM-yyyy")) + ", startTime=" + startTime.toString(DateTimeFormat.forPattern("HH:mm"))
					+ ", endTime=" + endTime.toString(DateTimeFormat.forPattern("HH:mm")) + ", type=" + type + "]";
		else
			return "TimeSlot [day=" + day + ", startTime=" + startTime.toString(DateTimeFormat.forPattern("HH:mm"))
					+ ", endTime=" + endTime.toString(DateTimeFormat.forPattern("HH:mm")) + ", type=" + type + "]";

	}

	public String getFormattedDetails() {
		
		if (this.type == SessionType.EXAM)
			return date.toString(DateTimeFormat.forPattern("dd-MM-yyyy")) + "\t:\t" + startTime.toString(DateTimeFormat.forPattern("HH:mm"))
					+ " -" + endTime.toString(DateTimeFormat.forPattern("HH:mm"))+"\n";
		else
			return type+"\t" + day + "\t" + startTime.toString(DateTimeFormat.forPattern("HH:mm"))
					+ " -" + endTime.toString(DateTimeFormat.forPattern("HH:mm"))+"\n";
		
	}

}
