package com.demo.thesisbackend.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * Classe base per tutti i bean che prevedono un identificativo.
 */
@MappedSuperclass
public abstract class AbstractIdentifiedBean extends AbstractBean {
	private static final long serialVersionUID = 1L;

	/**
	 * Identificativo del bean.
	 */
	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "increment")
	private Long id;

	/**
	 * Restituisce {@link #id}.
	 *
	 * @return {@link #id}.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Imposta il valore per il campo {@link #id}.
	 *
	 * @param id Il valore per il campo {@link #id} da impostare.
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	protected int getHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}

	protected boolean isEquals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractIdentifiedBean)) {
			return false;
		}
		final AbstractIdentifiedBean other = (AbstractIdentifiedBean) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
}
