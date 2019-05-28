/**
 * 
 */
package org.einnovator.sample.movies.model;

/**
 * A {@code WorkerType}.
 *
 * @author Jorge Simao {@code {jorge.simao@einnovator.org}}
 */
public enum PersonRole {
	ACTOR("Actor"),
	DIRECTOR("Director"),
	PRODUCER("Producer");

	private final String displayValue;
	
	PersonRole(String displayValue) {
		this.displayValue = displayValue;
	}


	public String getDisplayValue() {
		return displayValue;
	}

	public String getLabel() {
		return displayValue;
	}

	public static PersonRole parse(String s) {
		for (PersonRole e: PersonRole.class.getEnumConstants()) {
			if (e.toString().equalsIgnoreCase(s)) {
				return e;
			}
		}
		return null;
	}
	
	public static PersonRole parse(String s, PersonRole defaultValue) {
		PersonRole value = parse(s);
		return value!=null ? value: defaultValue;
	}

	/**
	 * @param requestURI
	 * @return
	 */
	public static PersonRole parseURLtail(String uri) {
		if (uri==null) {
			return null;
		}
		for (PersonRole e: PersonRole.class.getEnumConstants()) {
			if (uri.endsWith(e.toString().toLowerCase())) {
				return e;
			}
		}
		return null;
	}

	public static PersonRole parseURLtail(String uri, PersonRole defaultValue) {
		PersonRole value = parseURLtail(uri);
		return value!=null ? value: defaultValue;
	}
	
	public static PersonRole parseURLhead(String uri) {
		if (uri==null) {
			return null;
		}
		for (PersonRole e: PersonRole.class.getEnumConstants()) {
			if (uri.endsWith(e.toString().toLowerCase())) {
				return e;
			}
		}
		return null;
	}

	public static PersonRole parseURLhead(String uri, PersonRole defaultValue) {
		PersonRole value = parseURLhead(uri);
		return value!=null ? value: defaultValue;
	}
}
