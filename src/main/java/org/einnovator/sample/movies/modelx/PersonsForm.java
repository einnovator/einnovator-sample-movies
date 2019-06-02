/**
 * 
 */
package org.einnovator.sample.movies.modelx;

import java.util.List;

import org.einnovator.sample.movies.model.MovieRole;
import org.einnovator.sample.movies.model.PersonInMovie;
import org.einnovator.util.model.ObjectBase;
import org.einnovator.util.model.ToStringCreator;

/**
 *
 */
public class PersonsForm extends ObjectBase {

	private MovieRole role;

	private List<PersonInMovie> persons;
	
	/**
	 * Create instance of {@code PersonsForm}.
	 *
	 */
	public PersonsForm() {
	}

	/**
	 * Get the value of property {@code role}.
	 *
	 * @return the role
	 */
	public MovieRole getRole() {
		return role;
	}

	/**
	 * Set the value of property {@code role}.
	 *
	 * @param role the role to set
	 */
	public void setRole(MovieRole role) {
		this.role = role;
	}

	/**
	 * Get the value of property {@code persons}.
	 *
	 * @return the persons
	 */
	public List<PersonInMovie> getPersons() {
		return persons;
	}

	/**
	 * Set the value of property {@code persons}.
	 *
	 * @param persons the persons to set
	 */
	public void setPersons(List<PersonInMovie> persons) {
		this.persons = persons;
	}
	
	
	@Override
	public ToStringCreator toString(ToStringCreator creator) {
		return creator
			.append("role", role)
			.append("persons", persons)
			;
	}
	
}
