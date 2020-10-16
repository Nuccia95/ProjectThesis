package com.demo.frontend.view.login;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;

import shared.thesiscommon.bean.User;
import shared.thesiscommon.utils.TextUtils;

/**
 * Class for retrieving and setting the name of the current user of the current
 * session (without using JAAS). All methods of this class require that a
 * {@link VaadinRequest} is bound to the current thread.
 *
 *
 * @see com.vaadin.server.VaadinService#getCurrentRequest()
 */
public final class CurrentUser
{

	private static Logger logger = LoggerFactory.getLogger(CurrentUser.class);

	public static final String CURRENT_USER_SESSION_ATTRIBUTE_KEY = CurrentUser.class.getCanonicalName();


	public static User get(){
		return (User) UI.getCurrent().getSession().getAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY);
	}

	public static User get(final UI ui){
		return (User) ui.getSession().getAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY);
	}
	
	public static boolean isAdmin() {
		return get().getAdmin();
	}

	public static String getCookieValue(final String name){
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		for (Cookie c : cookies)
			if (c.getName().equals(name))
				return c.getValue();
		return "";
	}

	public static boolean LoginCookiesArePresent(){
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		if (cookies != null)
			for (Cookie c : cookies){
				if ((c.getName().equals("username") || c.getName().equals("password")) && !c.getValue().isEmpty())
					return true;
			}
		return false;
	}

	public static void set(final User login){
		set(login, false);
	}

	public static void set(final User login, final boolean rememberMe)
	{
		logger.info("Set user:" + login);
		if (login == null){
			UI.getCurrent().getSession().setAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY, null);
			handleCookie("username", "");
			handleCookie("password", "");
		} else
			UI.getCurrent().getSession().setAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY, login);

		if (rememberMe){
			handleCookie("username", login.getEmail());
			handleCookie("password", TextUtils.decode(login.getPassword()));
		}
	}

	
	private static void handleCookie(final String cookie_name, final String cookie_value)
	{
		// Create a new cookie
		Cookie myCookie = new Cookie(cookie_name, cookie_value);
		// Make cookie expire in 7 days
		myCookie.setMaxAge(60 * 60 * 24 * 7);
		// Set the cookie path.
		myCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
		// Save cookie
		VaadinService.getCurrentResponse().addCookie(myCookie);
	}

	private CurrentUser(){}

}
