package shared.thesiscommon.bean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = Reservation.TABLE_NAME)
public class Reservation extends AbstractIdentifiedBean {

	private static final long serialVersionUID = 1L;
	
	public static final String TABLE_NAME = "reservations"; //$NON-NLS-1$
	
	@ManyToOne
	private User owner;

	@ManyToOne
	private Resource resource;

	private String dayOfWeek;
	private String title;
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
	private List<String> receivers;
	@Transient
	private List<String> days;
	@Transient
	private LocalDate endRecurring;
	
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

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public List<String> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<String> receivers) {
		this.receivers = receivers;
	}
	
	public List<String> getDays() {
		return days;
	}

	public void setDays(List<String> days) {
		this.days = days;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getEndRecurring() {
		return endRecurring;
	}

	public void setEndRecurring(LocalDate endRecurring) {
		this.endRecurring = endRecurring;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
		result = prime * result + ((days == null) ? 0 : days.hashCode());
		result = prime * result + (editable ? 1231 : 1237);
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((endRecurring == null) ? 0 : endRecurring.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + (int) (groupId ^ (groupId >>> 32));
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((receivers == null) ? 0 : receivers.hashCode());
		result = prime * result + (recurring ? 1231 : 1237);
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Reservation other = (Reservation) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (dayOfWeek == null) {
			if (other.dayOfWeek != null)
				return false;
		} else if (!dayOfWeek.equals(other.dayOfWeek))
			return false;
		if (days == null) {
			if (other.days != null)
				return false;
		} else if (!days.equals(other.days))
			return false;
		if (editable != other.editable)
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (endRecurring == null) {
			if (other.endRecurring != null)
				return false;
		} else if (!endRecurring.equals(other.endRecurring))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (groupId != other.groupId)
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (receivers == null) {
			if (other.receivers != null)
				return false;
		} else if (!receivers.equals(other.receivers))
			return false;
		if (recurring != other.recurring)
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Reservation [owner=" + owner + ", resource=" + resource + ", dayOfWeek=" + dayOfWeek + ", title="
				+ title + ", color=" + color + ", startDate=" + startDate + ", startTime=" + startTime + ", endTime="
				+ endTime + ", groupId=" + groupId + ", recurring=" + recurring + ", endDate=" + endDate + ", editable="
				+ editable + ", receivers=" + receivers + ", days=" + days + ", endRecurring=" + endRecurring + "]";
	}

}
