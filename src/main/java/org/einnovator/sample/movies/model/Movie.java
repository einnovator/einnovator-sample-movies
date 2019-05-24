/**
 * 
 */
package org.einnovator.sample.movies.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.einnovator.jpa.model.EntityBase2;
import org.einnovator.social.client.model.Channel;
import org.einnovator.social.client.model.ChannelBuilder;
import org.einnovator.social.client.model.ChannelType;
import org.einnovator.util.model.Ref;
import org.einnovator.util.model.RefBuilder;
import org.einnovator.util.model.ToStringCreator;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * A {@code Movie}.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Movie extends EntityBase2<Long> {

	@Column(length=1024)
	@NotEmpty
	@JsonProperty("Title")
	protected String title;
	
	@Lob
	@JsonProperty("PlotSummary")
	protected String description;
	
	@Column(length=256)
	protected String img;

	@Enumerated(EnumType.STRING)
	private MovieGenre genre;

	@JsonProperty("Year")
	private Integer year;

	@JsonProperty("GrossTakingsAmount")
	private BigDecimal /*Currency*/ amount;

	@JsonProperty("IsAvailableOnDVD")	
	private Boolean dvd;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
	@OrderBy("corder")
	@JoinColumn(name="movie_id")
	protected List<Person> persons;
	
	private String metaId;

	@Column(length=128)
	private String channelId;
	
	
	/**
	 * Create instance of {@code Movie}.
	 *
	 */
	public Movie() {
	}	
	
	/**
	 * Create instance of {@code Movie}.
	 *
	 */
	public Movie(Object obj) {
		super(obj);
	}	
	

	//
	// Getters and setters
	//


	/**
	 * Get the value of property {@code title}.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the value of property {@code title}.
	 *
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the value of property {@code genre}.
	 *
	 * @return the genre
	 */
	public MovieGenre getGenre() {
		return genre;
	}

	/**
	 * Set the value of property {@code genre}.
	 *
	 * @param genre the genre to set
	 */
	public void setGenre(MovieGenre genre) {
		this.genre = genre;
	}

	/**
	 * Get the value of property {@code img}.
	 *
	 * @return the img
	 */
	public String getImg() {
		return img;
	}

	/**
	 * Set the value of property {@code img}.
	 *
	 * @param img the img to set
	 */
	public void setImg(String img) {
		this.img = img;
	}


	/**
	 * Get the value of property {@code description}.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the value of property {@code description}.
	 *
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the value of property {@code year}.
	 *
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}



	/**
	 * Set the value of property {@code year}.
	 *
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}



	/**
	 * Get the value of property {@code amount}.
	 *
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}



	/**
	 * Set the value of property {@code amount}.
	 *
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}



	/**
	 * Get the value of property {@code dvd}.
	 *
	 * @return the dvd
	 */
	public Boolean getDvd() {
		return dvd;
	}



	/**
	 * Set the value of property {@code dvd}.
	 *
	 * @param dvd the dvd to set
	 */
	public void setDvd(Boolean dvd) {
		this.dvd = dvd;
	}



	/**
	 * Get the value of property {@code metaId}.
	 *
	 * @return the metaId
	 */
	public String getMetaId() {
		return metaId;
	}



	/**
	 * Set the value of property {@code metaId}.
	 *
	 * @param metaId the metaId to set
	 */
	public void setMetaId(String metaId) {
		this.metaId = metaId;
	}



	/**
	 * Get the value of property {@code channelId}.
	 *
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}



	/**
	 * Set the value of property {@code channelId}.
	 *
	 * @param channelId the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}



	/**
	 * Set the value of property {@code persons}.
	 *
	 * @param persons the persons to set
	 */
	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}


	/**
	 * Get the value of property {@code persons}.
	 *
	 * @return the persons
	 */
	public List<Person> getPersons() {
		return persons;
	}


	public Person getPerson(int i) {
		return persons!=null && i>=0 && i<persons.size() ? persons.get(i) : null;
	}

	public Person addPerson(Person person) {
		if (persons==null) {
			persons = new ArrayList<>();
		}
		persons.add(person);
		return person;
	}


	public Person findPerson(Person person) {
		if (person!=null && persons!=null) {
			for (Person person2: persons) {
				if (person2.getId().equals(person.getId()) || (person2.getOrder()!=null && person2.getOrder().equals(person.getOrder()))) {
					return person;
				}
			}
		}
		return null;
	}

	public Person findPerson(String id) {
		if (id!=null && persons!=null) {
			for (Person person: persons) {
				if (id.equals(person.getUuid())) {
					return person;
				}
			}
		}
		return null;
	}

	public Person removePerson(Person person) {
		if (person!=null && persons!=null) {
			for (int i=0; i<persons.size(); i++) {
				Person person2 = persons.get(i);
				if (person2.getId().equals(person.getId()) || (person2.getOrder()!=null && person2.getOrder().equals(person.getOrder()))) {
					return persons.remove(i);
				}
			}
		}
		return null;
	}

	public List<Person> getPersonOfType(PersonRole type) {
		return filter(persons, type);
	}

	public List<Person> getActors() {
		return filter(persons, PersonRole.ACTOR);
	}



	public static List<Person> filter(List<Person> persons, PersonRole... roles) {
		if (persons==null) {
			return null;
		}
		List<Person> persons2 = new ArrayList<>();
		for (Person person: persons) {
			for (PersonRole role: roles) {
				if (role==person.getRole()) {
					persons2.add(person);
					break;
				}
			}
		}
		return persons2;
	}

	
	/**
	 * @param person2
	 */
	public Person moveupPerson(Person person) {
		if (persons!=null || persons.size()<=1) {
			for (int i=1; i<persons.size(); i++) {
				Person person2 = persons.get(i);
				if (person2.getId().equals(person.getId())) {
					person2.setOrder(i-1);
					persons.get(i-1).setOrder(i);
					//persons.remove(i);
					//persons.add(i-1, person2);
					return person2;
				}
			}
		}
		return null;		
	}


	/**
	 * @param person
	 */
	public Person movedownPerson(Person person) {
		if (persons!=null) {
			for (int i=0; i<persons.size()-1; i++) {
				Person person2 = persons.get(i);
				if (person2.getId().equals(person.getId())) {
					person2.setOrder(i+1);
					persons.get(i+1).setOrder(i);
					//persons.remove(i);
					//persons.add(i-1, person2);
					return person2;
				}
			}
		}	
		return null;
	}


	@Override
	public ToStringCreator toString1(ToStringCreator creator) {
		return super.toString1(creator)
				.append("title", title)
				.append("year", year)
				.append("dvd", dvd)
				.append("amount", amount)
				.append("img", img)
				.append("description", description)
				.append("persons", persons)
				;
	}



	/**
	 * @return
	 */
	public Channel makeChannel(String baseUri) {
		return new ChannelBuilder()
				.uuid(channelId)
				.name(title)
				.purpose(null)
				.img(img)
				.thumbnail(img)
				.type(ChannelType.COMMENTS)
				.ref(makeRef(baseUri))
				.build();
	}
	
	public Ref makeRef(String baseUri) {
		return new RefBuilder()
				.id(uuid)
				.type(getClass().getSimpleName())
				.name(title)
				.img(img)
				.thumbnail(img)
				.redirectUri(baseUri + "/movie/" + uuid)
				.pingUri(baseUri + "/api/movie/" + uuid)
				.build();
	}

	
}
