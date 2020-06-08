/**
 * 
 */
package org.einnovator.sample.movies.model;

/**
 * A {@code WorkerType}.
 *
 * @author support@einnovator.org
 */
public enum MovieRole {
	ACTOR("Actor"),
	DIRECTOR("Director"),
	PRODUCER("Producer");

	private final String displayValue;
	
	MovieRole(String displayValue) {
		this.displayValue = displayValue;
	}


	public String getDisplayValue() {
		return displayValue;
	}

	public String getLabel() {
		return displayValue;
	}

	public static MovieRole parse(String s) {
		for (MovieRole e: MovieRole.class.getEnumConstants()) {
			if (e.toString().equalsIgnoreCase(s)) {
				return e;
			}
		}
		return null;
	}
	
	public static MovieRole parse(String s, MovieRole defaultValue) {
		MovieRole value = parse(s);
		return value!=null ? value: defaultValue;
	}

	/**
	 * @param requestURI
	 * @return
	 */
	public static MovieRole parseURLtail(String uri) {
		if (uri==null) {
			return null;
		}
		for (MovieRole e: MovieRole.class.getEnumConstants()) {
			if (uri.endsWith(e.toString().toLowerCase())) {
				return e;
			}
		}
		return null;
	}

	public static MovieRole parseURLtail(String uri, MovieRole defaultValue) {
		MovieRole value = parseURLtail(uri);
		return value!=null ? value: defaultValue;
	}
	
	public static MovieRole parseURLhead(String uri) {
		if (uri==null) {
			return null;
		}
		for (MovieRole e: MovieRole.class.getEnumConstants()) {
			if (uri.endsWith(e.toString().toLowerCase())) {
				return e;
			}
		}
		return null;
	}

	public static MovieRole parseURLhead(String uri, MovieRole defaultValue) {
		MovieRole value = parseURLhead(uri);
		return value!=null ? value: defaultValue;
	}
}
