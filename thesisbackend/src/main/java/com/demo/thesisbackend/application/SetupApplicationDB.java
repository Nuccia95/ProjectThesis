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
		setupFunctionalities();
		final Profile adminProfile = setupAdminProfile();
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
			adminUser.setFirstName("Nuccia");
			adminUser.setLastName("Oliverio");
			adminUser.setEmail("nucciaoliverio95@gmail.com");
			adminUser.setAdmin(Boolean.TRUE);
			adminUser.setPassword(PasswordEncoder.encode("admin"));
			adminUser.setUsername(User.ADMIN_USERNAME);
			adminUser.setProfile(adminProfile);

			userDAO.save(adminUser);
			LOGGER.info("Creazione dell'utente admin"); //$NON-NLS-1$
		}
	}

	private Profile setupAdminProfile() {
		Profile adminProfile = profileDAO.findByName(User.ADMIN_USERNAME);
		if (adminProfile == null) {
			adminProfile = new Profile();
			adminProfile.setAdmin(Boolean.TRUE);
			adminProfile.setName(User.ADMIN_USERNAME);
			
			Set<Functionality> functionalities = new HashSet<>();
			Iterable<Functionality> all = functionalityDAO.findAll();
			for (Functionality functionality : all)
				functionalities.add(functionality);
			
			/* admin has all the functionalities */
			adminProfile.setFunctionalities(functionalities);
			
			adminProfile = profileDAO.save(adminProfile);
			LOGGER.info("Creazione del profilo admin"); //$NON-NLS-1$
		}
		
		return adminProfile;
	}

	private void setupFunctionalities() {
		for (final SystemFunctionality axaFunctionalArea : SystemFunctionality.values()) {
			final String name = axaFunctionalArea.toString();
			Functionality functionality = functionalityDAO.findByName(name);
			if (functionality == null) {
				functionality = new Functionality();
				functionality.setName(name);
				functionalityDAO.save(functionality);
				LOGGER.info("Aggiunta una nuova area funzionale: {}", name); //$NON-NLS-1$
			}
		}
	}

	private Profile setupViewerProfile() {
		Profile profile = profileDAO.findByName(User.VIEWER_PROFILE);
		if (profile == null) {
			profile = new Profile();
			profile.setAdmin(Boolean.FALSE);
			profile.setName(User.VIEWER_PROFILE);
			final Functionality functionality = functionalityDAO.findByName(SystemFunctionality.VIEWER.toString());
			profile.setFunctionalities(Collections.singleton(functionality));
			profile = profileDAO.save(profile);
			LOGGER.info("Creazione del profilo viewer"); //$NON-NLS-1$
		}
		return profile;
	}

	public Profile setupManagerProfile() {
		Profile profile = profileDAO.findByName(User.USER_PROFILE);
		if (profile == null) {
			profile = new Profile();
			profile.setAdmin(Boolean.FALSE);
			profile.setName(User.USER_PROFILE);
			final Functionality functionality = functionalityDAO
					.findByName(SystemFunctionality.EVENTS_MANAGEMENT.toString());
			profile.setFunctionalities(Collections.singleton(functionality));
			profile = profileDAO.save(profile);
			LOGGER.info("Creazione del profilo viewer"); //$NON-NLS-1$
		}
		return profile;
	}

}
