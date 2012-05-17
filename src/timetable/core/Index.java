package timetable.core;

import java.util.Set;
import java.util.TreeSet;

public class Index {

	private final Set<TimeSlot> sessions;
	private Subject subject;
	private final String indexCode;
	private final int hashCode;

	public Index(Set<TimeSlot> sessions, Subject subject, String indexCode) {
		this.sessions = new TreeSet<TimeSlot>(sessions);
		for(TimeSlot session: sessions){
			session.setIndex(this);
		}
		this.subject = subject;
		this.indexCode = indexCode;
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((indexCode == null) ? 0 : indexCode.hashCode());
		hashCode = result;
	}
	
	public Index(Set<TimeSlot> sessions, String indexCode) {
		this.sessions = new TreeSet<TimeSlot>(sessions);
		for(TimeSlot session: sessions){
			session.setIndex(this);
		}
		
		this.indexCode = indexCode;
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((indexCode == null) ? 0 : indexCode.hashCode());
		hashCode = result;
	}
	
	public void setSubject(Subject subject){
		this.subject = subject;
	}

	public Set<TimeSlot> getTimeSlots() {
		return sessions;
	}

	public Subject getSubject() {
		return subject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return hashCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
	System.out.println("comparing indices");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Index other = (Index) obj;
		if (indexCode == null) {
			if (other.indexCode != null){
				System.out.println(indexCode + "not equals"+ other.indexCode);
				return false;
			}
		} else if (!indexCode.equals(other.indexCode)){
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
//		return "Index [sessions=" + sessions + ", subject=" + subject
//				+ ", indexCode=" + indexCode + "]";
		return "Index [index Code="+ indexCode + "]";
	}

	public boolean clashesWith(Index otherIndex) {
		for (TimeSlot myTimeSlot : this.getTimeSlots()) {
			for (TimeSlot hisTimeSlot : otherIndex.getTimeSlots())
				if (myTimeSlot.clashesWith(hisTimeSlot)) {
	
					return true;
				}
		}
		return false;
	}

	public String getIndexCode() {
		return this.indexCode;
	}

	public String getFormattedDetails() {
		StringBuilder result =new StringBuilder("Index\t:");
		result.append(this.indexCode);
		result.append("\nTimeSlots:\n");
		for(TimeSlot timeSlot : this.sessions){
			result.append(timeSlot.getFormattedDetails());
		}
		return result.toString();
	}

}
