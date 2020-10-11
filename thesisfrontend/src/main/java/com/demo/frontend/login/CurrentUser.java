package com.demo.frontend.login;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;

import shared.thesiscommon.bean.User;

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
	/**
	 * The attribute key used to store the username in the session.
	 */
	public static final String CURRENT_USER_SESSION_ATTRIBUTE_KEY = CurrentUser.class.getCanonicalName();
	public static final String CURRENT_CODE_SESSION_ATTRIBUTE_KEY = "CurrentUserCode";

	/*public static boolean checkFunctionalities(final SystemFunctionality... sfs)
	{
		return get().checkFunctionalities(sfs);
	}*/

	/**
	 * Returns the name of the current user stored in the current session, or an
	 * empty string if no user name is stored.
	 *
	 * @throws IllegalStateException
	 *             if the current session cannot be accessed.
	 */
	public static User get(){
		return (User) UI.getCurrent().getSession().getAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY);
	}

	public static User get(final UI ui){
		return (User) ui.getSession().getAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY);
	}

	/**
	 *
	 * @param name
	 * @return it returns the value of the given cookie name
	 */
	public static String getCookieValue(final String name)
	{
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		for (Cookie c : cookies)
		{
			if (c.getName().equals(name))
			{
				return c.getValue();
			}
		}
		return "";
	}

	/**
	 *
	 * @return true if cookie is saved
	 */
	public static boolean LoginCookiesArePresent()
	{
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		if (cookies != null)
			for (Cookie c : cookies){
				if ((c.getName().equals("username") || c.getName().equals("password")) && !c.getValue().isEmpty())
					return true;
			}
		return false;
	}

	/**
	 * Sets the name of the current user and stores it in the current session.
	 * Using a {@code null} username will remove the username from the session.
	 *
	 * @throws IllegalStateException
	 *             if the current session cannot be accessed.
	 */
	public static void set(final User login){
		set(login, false);
	}

	/**
	 * @param login
	 *            same of the previous
	 * @param rememberMe
	 *            set cookies
	 */
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
			//handleCookie("password", TextUtils.decode(login.getPassword()));
			handleCookie("password", login.getPassword());
		}
	}

	/**
	 * Sets the code of the current user's device (or the code of the device
	 * selected if the current user has more than one device installed) and
	 * stores it in the current session.
	 */
	public static void setCode(String code)
	{
		logger.info("Set code:" + code);
		if (code == null)
		{
			UI.getCurrent().getSession().setAttribute(CURRENT_CODE_SESSION_ATTRIBUTE_KEY, null);
			handleCookie("username", "");
			handleCookie("password", "");
		} else
		{
			UI.getCurrent().getSession().setAttribute(CURRENT_CODE_SESSION_ATTRIBUTE_KEY, code);
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
