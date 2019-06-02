/**
 * 
 */
package org.einnovator.sample.movies.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.einnovator.jpa.model.EntityBase2;
import org.einnovator.util.model.ToStringCreator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * A {@code PersonInMovie}.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class PersonInMovie extends EntityBase2<Long> {

	@Enumerated(EnumType.STRING)
	private MovieRole role;
	
	@ManyToOne(cascade= { CascadeType.MERGE, CascadeType.PERSIST})
	private Person person;
	
	@ManyToOne(cascade= { CascadeType.MERGE, CascadeType.PERSIST})
	@JsonIgnore
	private Movie movie;

	@Column(name="corder")
	private Integer order;

	
	/**
	 * Create instance of {@code PersonInMovie}.
	 *
	 */
	public PersonInMovie() {
	}	
	
	/**
	 * Create instance of {@code PersonInMovie}.
	 *
	 */
	public PersonInMovie(Object obj) {
		super(obj);
	}	
	
	
	//
	// Getters and setters
	//


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
	 * Get the value of property {@code person}.
	 *
	 * @return the person
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * Set the value of property {@code person}.
	 *
	 * @param person the person to set
	 */
	public void setPerson(Person person) {
		this.person = person;
	}
	
	/**
	 * Get the value of property {@code order}.
	 *
	 * @return the order
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * Set the value of property {@code order}.
	 *
	 * @param order the order to set
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}
	

	/**
	 * Get the value of property {@code movie}.
	 *
	 * @return the movie
	 */
	public Movie getMovie() {
		return movie;
	}

	/**
	 * Set the value of property {@code movie}.
	 *
	 * @param movie the movie to set
	 */
	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	@Override
	public ToStringCreator toString1(ToStringCreator creator) {
		return super.toString1(creator)
				.append("role", role)
				.append("person", person)
				.append("order", order)
				;
	}

	
}
