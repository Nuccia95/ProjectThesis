package shared.thesiscommon.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = Functionality.TABLE_NAME)
public class Functionality extends AbstractIdentifiedBean {

	private static final long serialVersionUID = 1L;
	
	public static final String TABLE_NAME = "functionality"; //$NON-NLS-1$

	@Column(unique = true, nullable = false)
	private String name;

	public void setName(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Functionality other = (Functionality) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Functionality [" + name + "]";
	}
}
