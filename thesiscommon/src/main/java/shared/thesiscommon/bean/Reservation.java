package shared.thesiscommon.bean;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "Reservations")
public class Reservation extends AbstractIdentifiedBean {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JsonIgnore
	private User owner;

	@ManyToOne
	@JsonIgnore
	private Resource resource;

	private String dayOfWeek;
	private String color;
	private LocalDate startDate;
	private LocalTime startTime;
	private LocalTime endTime;
	/* recurring event */
	private long groupId;
	private boolean recurring;
	private LocalDate endDate;
	private boolean editable;
	
	@Transient
	@JsonSerialize
	private Long ownerId;
	@Transient
	@JsonSerialize
	private String resourceName;

	public Reservation() {
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

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public final Resource getResource() {
		return resource;
	}

	public final void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public final Long getOwnerId() {
		return ownerId;
	}

	public final void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public final String getResourceName() {
		return resourceName;
	}

	public final void setResourceName(String resourceName) {
		this.resourceName = resourceName;
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
		return null;
		/*return "Reservation [ dayOfWeek=" + dayOfWeek + ",recurring=" + recurring + ", color=" + color + ", startDate="
				+ startDate + ", startTime=" + startTime + ", endTime=" + endTime + ", endDate=" + endDate
				+ ", editable=" + editable + ", groupId=" + groupId + "]";*/
	}
}