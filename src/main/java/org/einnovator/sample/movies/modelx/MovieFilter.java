package org.einnovator.sample.movies.modelx;

import org.einnovator.sample.movies.model.MovieGenre;
import org.einnovator.util.model.ToStringCreator;

public class MovieFilter extends MovieOptions {
	
	private String q;
	
	private MovieGenre genre;
	
	public MovieFilter() {
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

	@Override
	public ToStringCreator toString(ToStringCreator creator) {
		return creator
				.append("q", q)
				.append("genre", genre)
			;
	}
	
}
