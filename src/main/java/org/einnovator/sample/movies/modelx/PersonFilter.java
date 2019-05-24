package org.einnovator.sample.movies.modelx;

import org.einnovator.sample.movies.model.PersonRole;
import org.einnovator.util.model.ToStringCreator;

public class PersonFilter extends PersonOptions {
	
	private String q;
	
	private PersonRole role;
	
	public PersonFilter() {
	}
	
	/**
	 * Get the value of property {@code q}.
	 *
	 * @return the q
	 */
	public String getQ() {
		return q;
	}

	/**
	 * Set the value of property {@code q}.
	 *
	 * @param q the q to set
	 */
	public void setQ(String q) {
		this.q = q;
	}


	/**
	 * Get the value of property {@code role}.
	 *
	 * @return the role
	 */
	public PersonRole getRole() {
		return role;
	}

	/**
	 * Set the value of property {@code role}.
	 *
	 * @param role the role to set
	 */
	public void setRole(PersonRole role) {
		this.role = role;
	}

	@Override
	public ToStringCreator toString(ToStringCreator creator) {
		return creator
			.append("q", q)
			.append("role", role)
			;
	}
	
}
