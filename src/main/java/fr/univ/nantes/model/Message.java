package fr.univ.nantes.model;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

@Entity
public class Message {

	@Id private Long id;
	@Load private Ref<User> sender;
	private String body;
	private long publicationDate;
	
	protected Message() {}
	
	public Message(User sender, String body) {
		this.sender = Ref.create(sender);
		this.body = body;
		this.publicationDate = System.currentTimeMillis();
	}
	
	public Long getId() {
		return this.id;
	}
	
	public User getSender() {
		return this.sender.get();
	}
	
	public String getBody() {
		return this.body;
	}
	
	public long getPublicationDate() {
		return this.publicationDate;
	}
}
