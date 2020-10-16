package shared.thesiscommon.bean;

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
	
	public Resource() {}
	
	public Resource(String name, String description, Integer seatsAvailable) {
		this.name = name;
		this.description = description;
		this.seatsAvailable = seatsAvailable;
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

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
