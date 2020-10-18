package shared.thesiscommon.bean;

import java.util.Arrays;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = User.TABLE_NAME)
public class User extends AbstractIdentifiedBean {

	
	public static final String ADMIN_USERNAME = "ADMIN"; //$NON-NLS-1$	
	
	public static final String ADMIN_PROFILE = "ADMIN"; //$NON-NLS-1$	
	public static final String USER_PROFILE = "USER"; //$NON-NLS-1$
	public static final String VIEWER_PROFILE = "VIEWER"; //$NON-NLS-1$
	
	public static final String TABLE_NAME = "users";


	private static final long serialVersionUID = 1L;

	@Column(unique = true, nullable = false)
	private String email;

	private String username;
	private String firstName;
	private String lastName;

	@Column(nullable = false)
	private Boolean admin;
	
	@Basic(fetch = FetchType.LAZY, optional = false)
	private byte[] password;

	@ManyToOne
	@JoinColumn(name = "profile")
	private Profile profile;

	public Set<Functionality> computeAllGrantedFunctionalities() {
		return getProfile().getFunctionalities();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public boolean checkFunctionalities(final String... sfs) {
		Set<Functionality> functionalities = computeAllGrantedFunctionalities();
		for (String sf : sfs) {
			boolean found = false;
			for (Functionality f : functionalities) {
				if (f.getName().equals(sf)) {
					found = true;
					break;
				}
			}
			if (!found)
				return false;
		}
		return true;
	}

	public boolean checkFunctionalities(final SystemFunctionality... sfs) {
		Set<Functionality> functionalities = computeAllGrantedFunctionalities();
		for (SystemFunctionality sf : sfs) {
			boolean found = false;
			for (Functionality f : functionalities) {
				if (f.getName().equals(sf.toString())) {
					found = true;
					break;
				}
			}
			if (!found)
				return false;
		}
		return true;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (admin == null) {
			if (other.admin != null)
				return false;
		} else if (!admin.equals(other.admin))
			return false;
		if (!Arrays.equals(password, other.password))
			return false;
		if (profile == null) {
			if (other.profile != null)
				return false;
		} else if (!profile.equals(other.profile))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((admin == null) ? 0 : admin.hashCode());
		result = prime * result + Arrays.hashCode(password);
		result = prime * result + ((profile == null) ? 0 : profile.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + ", profile="
				+ profile + "]";
	}

}
