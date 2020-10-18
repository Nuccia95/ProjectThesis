package shared.thesiscommon.bean;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity(name = Profile.TABLE_NAME)
public class Profile extends AbstractIdentifiedBean {
	
	private static final long serialVersionUID = 1L;
	
	public static final String TABLE_NAME = "userProfile"; //$NON-NLS-1$

	@Column(nullable = false)
	private Boolean admin;

	@ManyToMany
	@JoinTable(name = "profile_functionality", joinColumns = { @JoinColumn(name = "profile") }, inverseJoinColumns = {
			@JoinColumn(name = "functionality") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Functionality> functionalities;

	@Column(unique = true, nullable = false)
	private String name;

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Set<Functionality> getFunctionalities() {
		return functionalities;
	}

	public void setFunctionalities(Set<Functionality> functionalities) {
		this.functionalities = functionalities;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((admin == null) ? 0 : admin.hashCode());
		result = prime * result + ((functionalities == null) ? 0 : functionalities.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Profile other = (Profile) obj;
		if (admin == null) {
			if (other.admin != null)
				return false;
		} else if (!admin.equals(other.admin))
			return false;
		if (functionalities == null) {
			if (other.functionalities != null)
				return false;
		} else if (!functionalities.equals(other.functionalities))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Profile [admin=" + admin + ", functionalities=" + functionalities + ", name=" + name + "]";
	}
}
