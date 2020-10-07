package shared.thesiscommon;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Reservations")
public class Reservation extends AbstractIdentifiedBean{
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JsonIgnore
	private User owner;
	
	private String resourceId;
	
	private String dayOfWeek;
	private String color;
	private LocalDate startDate;
	private LocalTime startTime;
	private LocalTime endTime;
	/* recurring event */
	private boolean firstOfRecurrences;
	private boolean recurring;
	private LocalDate endDate;
	private boolean editable;
	
	public Reservation() {}

	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public boolean isFirstOfRecurrences() {
		return firstOfRecurrences;
	}

	public void setFirstOfRecurrences(boolean firstOfRecurrences) {
		this.firstOfRecurrences = firstOfRecurrences;
	}

	public boolean isRecurring() {
		return recurring;
	}

	public void setRecurring(boolean recurring) {
		this.recurring = recurring;
	}
	
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public boolean equals(Object obj) {
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return "Reservation [ resourceId=" + resourceId + ", recurring=" + recurring
				+ "]";
	}
}
