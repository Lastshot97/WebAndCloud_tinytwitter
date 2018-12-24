package fr.univ.nantes.model;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class MessageReceivers {
	
	@Id private Long id;
	@Parent private Key<Message> message;
	@Index private long publicationDate;
	@Index private List<Key<User>> receivers = new ArrayList<Key<User>>();

	protected MessageReceivers() {}
	
	public MessageReceivers(Key<Message> message, long publicationDate, List<Key<User>> receivers) {
		this.message = message;
		this.publicationDate = publicationDate;
		this.receivers.addAll(receivers);
	}
	
	public Key<Message> getParent() {
		return this.message;
	}
	
	public long getPublicationDate() {
		return this.publicationDate;
	}
}
