package shared.thesiscommon.bean;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = Resource.TABLE_NAME)
public class Resource extends AbstractIdentifiedBean {
	
	private static final long serialVersionUID = 1L;
	
	public static final String TABLE_NAME = "resources"; //$NON-NLS-1$
	
	@Column(unique = true, nullable = false)
	private String name;
	private String description;
	private Integer seatsAvailable;
	private Boolean enable;
	private LocalDate disabledUntil;
	
	public Resource() {}
	
	public Resource(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSeatsAvailable() {
		return seatsAvailable;
	}

	public void setSeatsAvailable(Integer seatsAvailable) {
		this.seatsAvailable = seatsAvailable;
	}
	
	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public LocalDate getDisabledUntil() {
		return disabledUntil;
	}

	public void setDisabledUntil(LocalDate disabledUntil) {
		this.disabledUntil = disabledUntil;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((disabledUntil == null) ? 0 : disabledUntil.hashCode());
		result = prime * result + ((enable == null) ? 0 : enable.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((seatsAvailable == null) ? 0 : seatsAvailable.hashCode());
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
		Resource other = (Resource) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (disabledUntil == null) {
			if (other.disabledUntil != null)
				return false;
		} else if (!disabledUntil.equals(other.disabledUntil))
			return false;
		if (enable == null) {
			if (other.enable != null)
				return false;
		} else if (!enable.equals(other.enable))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (seatsAvailable == null) {
			if (other.seatsAvailable != null)
				return false;
		} else if (!seatsAvailable.equals(other.seatsAvailable))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Resource [name=" + name + ", description=" + description + ", seatsAvailable=" + seatsAvailable
				+ ", enable=" + enable + ", disabledUntil=" + disabledUntil + "]";
	}
}
