package com.jsvana.cayenne;

import java.io.Serializable;

public class Comic implements Serializable {
	private static final long serialVersionUID = -3650023943110206225L;
	
	public int id;
	public String name;
	public int totalPages;
	public int newPages;
	public String homePage;
	public String urlBase;
	public String urlTail;
	public String mostRecent;
	
	public Comic(int id, String name, int totalPages, int newPages) {
		this.id = id;
		this.name = name;
		this.totalPages = totalPages;
		this.newPages = newPages;
	}
	
	@Override
	public String toString() {
		return this.name + " (" + this.newPages + " new)";
	}
}
