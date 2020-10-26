package shared.thesiscommon.webservicesinterface;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpEntity;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.Resource;
import shared.thesiscommon.bean.User;

public interface WebServicesInterface {

	User login(HttpEntity<User> user);

	User registration(HttpEntity<User> user);

	Reservation createReservation(HttpEntity<Reservation> reservation);

	Reservation updateSingleReservation(HttpEntity<Reservation> reservation);

	void deleteReservation(HttpEntity<Reservation> reservation);

	void deleteRecurringReservations(HttpEntity<Reservation> reservation);

	Set<Reservation> getByOwner(long id);

	Resource createResource(HttpEntity<Resource> resource);

	void deleteResource(HttpEntity<Resource> resource);

	List<Resource> getAllResources();

	List<String> getResourcesNames();

	List<String> getAllEmails();
	
	Set<Reservation> getAllReservations();
	
	int getReservationsByResource(long id);
	
	String getUserName(long id);
	
	boolean checkOldPassword(HttpEntity<User> user);

	boolean updatePassword(HttpEntity<User> user);
	
	
}