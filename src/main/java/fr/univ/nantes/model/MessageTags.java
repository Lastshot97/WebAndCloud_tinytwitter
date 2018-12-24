package fr.univ.nantes.model;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class MessageTags {

	@Id private Long id;
	@Parent private Key<Message> message;
	@Index private long publicationDate;
	@Index private List<String> tags = new ArrayList<String>();

	protected MessageTags() {}
	
	public MessageTags(Key<Message> message, long publicationDate) {
		this.message = message;
		this.publicationDate = publicationDate;
	}
	
	public Key<Message> getParent() {
		return this.message;
	}
	
	public long getPublicationDate() {
		return this.publicationDate;
	}
	
	public void addTag(String tag) {
		this.tags.add(tag);
	}
}
