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

	long deleteReservation(long id);

	void deleteRecurringReservations(HttpEntity<Reservation> reservation);

	Set<Reservation> getByOwner(long id);

	Resource createResource(HttpEntity<Resource> resource);

	void deleteRelatedReservations(HttpEntity<Resource> resource);

	List<Resource> getAllResources();

	List<String> getResourcesNames();

	List<String> getAllEmails();
	
	Set<Reservation> getAllReservations();
	
	Set<Reservation> getReservationsByResource(long id);
	
	boolean checkOldPassword(HttpEntity<User> user);

	boolean updatePassword(HttpEntity<User> user);
	
	boolean updateResource(HttpEntity<Resource> resource);
	
	Resource getRelatedResource(long id);
	
	boolean checkAvailableResource(HttpEntity<Reservation> res);
	
	Reservation getReservationById(long id);
	
	Reservation gerReservationByTitle(String title);
	
	void notifyEnabledResource(HttpEntity<Resource> resource);
}