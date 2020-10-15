package shared.thesiscommon.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class User extends AbstractIdentifiedBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(unique = true)
	private String email;

	private String firstName;
	private String lastName;
	private String password;
	private Boolean admin;

	public User() {}

	public User(String email) {
		this.email = email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
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
		return "User [" + "id=" + getId() + ",email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", password=" + password + ", admin=" + admin + "]";
	}

}
