/**
 * 
 */
package org.einnovator.sample.movies.model;

/**
 * A {@code PersonRole}.
 *
 * @author Jorge Simao {@code {jorge.simao@einnovator.org}}
 */
public enum MovieGenre {
	ACTION("Action", 16),
	COMEDY("Comedy", 16),
	SCIFI("SciFi", 12),
	ANIMATION("Animation", 2),
	;
	

	private final String displayValue;
	
	private final Integer minAge;

	MovieGenre(String displayValue, Integer minAge) {
		this.displayValue = displayValue;
		this.minAge = minAge;
	}

	
	/**
	 * Get the value of property {@code minAge}.
	 *
	 * @return the minAge
	 */
	public Integer getMinAge() {
		return minAge;
	}


	public String getDisplayValue() {
		return displayValue;
	}

	public String getLabel() {
		return displayValue;
	}

	public static MovieGenre parse(String s) {
		for (MovieGenre e: MovieGenre.class.getEnumConstants()) {
			if (e.toString().equalsIgnoreCase(s)) {
				return e;
			}
		}
		return null;
	}
	
	public static MovieGenre parse(String s, MovieGenre defaultValue) {
		MovieGenre value = parse(s);
		return value!=null ? value: defaultValue;
	}

	/**
	 * @param requestURI
	 * @return
	 */
	public static MovieGenre parseURLtail(String uri) {
		if (uri==null) {
			return null;
		}
		for (MovieGenre e: MovieGenre.class.getEnumConstants()) {
			if (uri.endsWith(e.toString().toLowerCase())) {
				return e;
			}
		}
		return null;
	}

	public static MovieGenre parseURLtail(String uri, MovieGenre defaultValue) {
		MovieGenre value = parseURLtail(uri);
		return value!=null ? value: defaultValue;
	}
	
	public static MovieGenre parseURLhead(String uri) {
		if (uri==null) {
			return null;
		}
		for (MovieGenre e: MovieGenre.class.getEnumConstants()) {
			if (uri.endsWith(e.toString().toLowerCase())) {
				return e;
			}
		}
		return null;
	}

	public static MovieGenre parseURLhead(String uri, MovieGenre defaultValue) {
		MovieGenre value = parseURLhead(uri);
		return value!=null ? value: defaultValue;
	}

}
