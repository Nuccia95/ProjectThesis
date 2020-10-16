package com.demo.thesisbackend.application;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.thesisbackend.dao.FunctionalityDAO;
import com.demo.thesisbackend.dao.ProfileDAO;
import com.demo.thesisbackend.dao.UserDAO;

import shared.thesiscommon.bean.Functionality;
import shared.thesiscommon.bean.Profile;
import shared.thesiscommon.bean.SystemFunctionality;
import shared.thesiscommon.bean.User;
import shared.thesiscommon.utils.PasswordEncoder;

@Component
public class SetupApplicationDB {

	public static final Logger LOGGER = LoggerFactory.getLogger(SetupApplicationDB.class);

	@Autowired
	private FunctionalityDAO functionalityDAO;

	@Autowired
	private ProfileDAO profileDAO;

	@Autowired
	private UserDAO userDAO;

	@PostConstruct
	public void initDB() {
		LOGGER.info("Setup DB in corso..."); //$NON-NLS-1$
		final Set<String> removed = setupFunctionalities();
		final Profile adminProfile = setupAdminProfile(removed);
		setupViewerProfile();
		setupManagerProfile();
		createAdminUser(adminProfile);
		LOGGER.info("Setup DB completato"); // $NON-NLS-1
	}

	/* create ADMIN */
	private void createAdminUser(final Profile adminProfile) {
		User adminUser = userDAO.findByUsername(User.ADMIN_USERNAME);
		if (adminUser == null) {
			adminUser = new User();
			adminUser.setFirstName("Admin");
			adminUser.setLastName("Admin");
			adminUser.setEmail("nucciaoliverio95@gmail.com");
			adminUser.setAdmin(Boolean.TRUE);
			adminUser.setPassword(PasswordEncoder.encode("admin"));
			adminUser.setUsername(User.ADMIN_USERNAME);
			adminUser.setProfile(adminProfile);

			userDAO.save(adminUser);
			LOGGER.info("Creazione dell'utente admin"); //$NON-NLS-1$
		}
		// User user = userDAO.findByUsername("Sales");
		// user.setPassword(PasswordEncoder.encode("RiDuCoSales"));
		// userDAO.save(user);
		// LOGGER.info("Password Cambiata"); //$NON-NLS-1$
	}

	/* Create profile admin, if it dosn't exist */
	private Profile setupAdminProfile(final Set<String> removedFunctionalityNames) {
		Profile adminProfile = profileDAO.findByName(User.ADMIN_USERNAME);
		if (adminProfile == null) {
			adminProfile = new Profile();
			adminProfile.setAdmin(Boolean.TRUE);
			adminProfile.setName(User.ADMIN_USERNAME);
			adminProfile = profileDAO.save(adminProfile);
			LOGGER.info("Creazione del profilo admin"); //$NON-NLS-1$
		}

		// Aggiunta delle funzionalità mancanti al profilo admin
		Set<Functionality> functionalities = adminProfile.getFunctionalities();
		if (functionalities == null)
			functionalities = new HashSet<>();

		final Set<String> functionalityNames = new HashSet<>();
		for (final Functionality functionality : functionalities)
			functionalityNames.add(functionality.getName());

		boolean someNews = false;
		
		for (final SystemFunctionality axaFunctionalArea : SystemFunctionality.values()) {
		    final String name = axaFunctionalArea.name();
		    removedFunctionalityNames.remove(name);
		    final Functionality functionality = functionalityDAO.findByName(name);
		    if (functionality != null) 
		    	if (!functionalityNames.contains(name)) {
		    		functionalities.add(functionality);
		    		LOGGER.info("Aggiunta una nuova area funzionale al profilo admin: {}", name); //$NON-NLS-1$
		    		someNews = true;
		    	}
		}

		final Set<Functionality> toDelete = new HashSet<>();
		for (final String name : removedFunctionalityNames) {
			final Functionality functionality = functionalityDAO.findByName(name);
		    	if (functionalityNames.contains(name)) {
		    		functionalities.remove(functionality);
		    		toDelete.add(functionality);
		    		someNews = true;
		    	}
		}

		return adminProfile;
	}

	private Set<String> setupFunctionalities() {
		final Set<String> installed = new HashSet<>();
		for (final SystemFunctionality axaFunctionalArea : SystemFunctionality.values()) {
			final String name = axaFunctionalArea.toString();
			installed.add(name);
			Functionality functionality = functionalityDAO.findByName(name);
			if (functionality == null) {
				functionality = new Functionality();
				functionality.setName(name);
				functionalityDAO.save(functionality);
				LOGGER.info("Aggiunta una nuova area funzionale: {}", name); //$NON-NLS-1$
			}
		}

		final Set<String> removed = new HashSet<>();
		functionalityDAO.findAll().forEach(functionality -> {
			final String name = functionality.getName();
			if (!installed.contains(name)) {
				removed.add(name);
				LOGGER.info("L'area funzionale {} non e' piu' utilizzata e verra' eliminata", name); //$NON-NLS-1$
			}
		});

		return removed;
	}

	private Profile setupViewerProfile() {
		Profile profile = profileDAO.findByName(User.VIEWER_USERNAME);
		if (profile == null) {
			profile = new Profile();
			profile.setAdmin(Boolean.FALSE);
			profile.setName(User.VIEWER_USERNAME);
			final Functionality functionality = functionalityDAO.findByName(SystemFunctionality.VIEWER.toString());
			profile.setFunctionalities(Collections.singleton(functionality));
			profile = profileDAO.save(profile);
			LOGGER.info("Creazione del profilo viewer"); //$NON-NLS-1$
		}
		return profile;
	}
	
	public Profile setupManagerProfile() {
		Profile profile = profileDAO.findByName(User.MANAGER_USERNAME);
		if (profile == null) {
			profile = new Profile();
			profile.setAdmin(Boolean.FALSE);
			profile.setName(User.MANAGER_USERNAME);
			final Functionality functionality = functionalityDAO.findByName(SystemFunctionality.EVENTS_MANAGEMENT.toString());
			profile.setFunctionalities(Collections.singleton(functionality));
			profile = profileDAO.save(profile);
			LOGGER.info("Creazione del profilo viewer"); //$NON-NLS-1$
		}
		return profile;
	}
	

}
