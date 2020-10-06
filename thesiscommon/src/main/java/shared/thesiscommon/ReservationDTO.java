package shared.thesiscommon;

public class ReservationDTO {
	
	private Reservation reservation;
	private Long ownerId;
	
	public ReservationDTO(){}
	
	public ReservationDTO(Reservation book, Long id) {
		this.reservation = book;
		this.ownerId = id;
	}
	
	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	
	

}
