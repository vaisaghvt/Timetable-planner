package timetable.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Subject {
	/**
	 * Subject name
	 */
	private final String name;

	/**
	 * Set of subject codes. Same subject is sometimes offered by multiple codes
	 * with different codes
	 */
	private final String code;

	/**
	 * Description of the subject
	 */
	private final String description;

	/**
	 * Set of subjects that have to completed as prerequites to take this
	 * subject.
	 */
	private final Set<String> preRequisites;

	/**
	 * Set of indices for each subject.
	 */
	private Set<Index> indices;

	/**
	 * The date of examination. This is actually the only field that can have an
	 * effect on the time table plan. The rest of the fields are for checks to
	 * insure that subject is registerable or to give more details about the
	 * subject
	 * 
	 */
	private final TimeSlot examinationDateTime;

	private final int hashCode;

	private String category;

	public Subject(String name, String description, String category, String code,
			Collection<String> preRequisites, Collection<Index> indices,
			TimeSlot date) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.code = code;
		this.preRequisites = new HashSet<String>(preRequisites);
		this.indices = new HashSet<Index>(indices);
		examinationDateTime = date;
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.replace(" ", "").hashCode());
		hashCode = result;
	}

	public Subject(String name, String description,String category, String code,
			Collection<String> preRequisites, TimeSlot date) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.code = new String(code);
		this.preRequisites = new HashSet<String>(preRequisites);
		examinationDateTime = date;
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		hashCode = result;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * @return the preRequisites
	 */
	public final Set<String> getPreRequisites() {
		return preRequisites;
	}

	/**
	 * @return the indices
	 */
	public final Set<Index> getIndices() {
		return indices;
	}

	/**
	 * @return true if ther eis an examination.
	 */
	public final boolean hasExam() {
		return (examinationDateTime == null) ? false : true;
	}

	/**
	 * @return the examination date. Returns null if no exam. Use hasExam() to
	 *         avoid this.
	 */
	public final TimeSlot getExaminationDateTime() {
		return examinationDateTime;
	}

	/**
	 * @return the codes
	 */
	public String getCode() {
		return code;
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subject other = (Subject) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// return "Subject [name=" + name + ", codes=" + codes +
		// ", description="
		// + description + ", preRequisites=" + preRequisites
		// + ", examinationDate=" + examinationDateTime + "]";
		return code +" : " +name;
	}

	public boolean clashesWith(Subject other) {
		if (this.hasExam() && other.hasExam()) {
			if (this.examinationDateTime.clashesWith(other
					.getExaminationDateTime())) {
				return true;
			}
		}
		return false;
	}

	public void initializeIndices(Set<Index> indices) {

		this.indices = indices;
	}

	public String getFormattedDetails() {
		StringBuilder result = new StringBuilder();
		result.append("Name\t: ");
		result.append(this.name);
		result.append("\nCodes\t: ");
		result.append(code);
	
		result.append("\n");
		result.append(this.description + "\n");
		if(preRequisites.size()>0)
			result.append("Prerequisites\t: ");
		int i=0;
		for (String prereq : preRequisites) {
			result.append(prereq);
			if (i < preRequisites.size() - 1)
				result.append(", ");
			else
				result.append("\n");
			i++;
		}
		
		if (this.hasExam()) {
			result.append("Exam date\t: ");
			result.append(this.examinationDateTime.getFormattedDetails());
		}
		for (Index index : this.indices) {
			result.append(index.getFormattedDetails());
		}
		return result.toString();
	}

	public String getCategory() {
		
		return category;
	}

}
