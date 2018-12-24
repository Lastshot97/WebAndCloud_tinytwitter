package fr.univ.nantes.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class User {
	
	@Id private String pseudo;
	
	protected User() {}
	
	public User(String pseudo) {
		this.pseudo = pseudo;
	}
	
	public String getPseudo() {
		return this.pseudo;
	}
}
