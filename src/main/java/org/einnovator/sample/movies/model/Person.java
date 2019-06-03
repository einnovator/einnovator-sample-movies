package org.einnovator.sample.movies.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.einnovator.jpa.model.EntityBase2;
import org.einnovator.social.client.model.Channel;
import org.einnovator.social.client.model.ChannelBuilder;
import org.einnovator.social.client.model.ChannelType;
import org.einnovator.util.model.Ref;
import org.einnovator.util.model.RefBuilder;
import org.einnovator.util.model.ToStringCreator;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Person extends EntityBase2<Long> {

	@JsonProperty("Name")
	@Column(length=1024)
	private String name;

	@JsonProperty("Surname")
	@Column(length=1024)
	private String surname;

	@Temporal(TemporalType.DATE)
	@JsonProperty("DateOfBirth")
	private Date birthDate;

	@Temporal(TemporalType.DATE)
	private Date deathDate;

	
	@Column(length=256)
	protected String avatar;

	@Lob
	@JsonProperty("Description")
	protected String description;
	
	@Column(length=128)
	private String channelId;

	public Person() {
	}
	

	/**
	 * Get the value of property {@code name}.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the value of property {@code name}.
	 *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the value of property {@code surname}.
	 *
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Set the value of property {@code surname}.
	 *
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * Get the value of property {@code birthDate}.
	 *
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * Set the value of property {@code birthDate}.
	 *
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * Get the value of property {@code deathDate}.
	 *
	 * @return the deathDate
	 */
	public Date getDeathDate() {
		return deathDate;
	}

	/**
	 * Set the value of property {@code deathDate}.
	 *
	 * @param deathDate the deathDate to set
	 */
	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	/**
	 * Get the value of property {@code avatar}.
	 *
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * Set the value of property {@code avatar}.
	 *
	 * @param avatar the avatar to set
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
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


	@Override
	public ToStringCreator toString1(ToStringCreator creator) {
		return super.toString1(creator)
				.append("name", name)
				.append("birthDate", birthDate)
				.append("deathDate", deathDate)
				.append("avatar", avatar)				
				;
	}
	
	public String getDisplayName() {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.hasText(name)) {
			sb.append(name.trim());
		}
		if (StringUtils.hasText(surname)) {
			if (sb.length()>0) {
				sb.append(" ");
			}
			sb.append(surname.trim());
		}
		return sb.length()>0 ? sb.toString() : null;
	}
	/**
	 * @return
	 */
	public Channel makeChannel(String baseUri) {
		return new ChannelBuilder()
				.uuid(channelId)
				.name(getDisplayName())
				.purpose(null)
				.img(avatar)
				.thumbnail(avatar)
				.type(ChannelType.COMMENTS)
				.ref(makeRef(baseUri))
				.build();
	}
	
	public Ref makeRef(String baseUri) {
		return new RefBuilder()
				.id(uuid)
				.type(getClass().getSimpleName())
				.name(getDisplayName())
				.img(avatar)
				.thumbnail(avatar)
				.redirectUri(baseUri + "/person/" + uuid)
				.pingUri(baseUri + "/api/person/" + uuid)
				.build();
	}

	
}
