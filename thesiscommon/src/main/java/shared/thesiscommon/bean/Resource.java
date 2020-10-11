package shared.thesiscommon.bean;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Resources")
public class Resource extends AbstractIdentifiedBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(unique = true)
	private String name;
	private String description;
	private Integer seatsAvailable;
	
	@OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Reservation> reservations;
	
	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	public final Integer getSeatsAvailable() {
		return seatsAvailable;
	}

	public final void setSeatsAvailable(Integer seatsAvailable) {
		this.seatsAvailable = seatsAvailable;
	}
	
	public final Set<Reservation> getReservations() {
		return reservations;
	}

	public final void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
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
