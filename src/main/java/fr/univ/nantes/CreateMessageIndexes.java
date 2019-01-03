package fr.univ.nantes;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.googlecode.objectify.Key;

import fr.univ.nantes.model.Message;
import fr.univ.nantes.model.MessageReceivers;
import fr.univ.nantes.model.MessageTags;
import fr.univ.nantes.model.User;
import fr.univ.nantes.model.UserFollowers;

public class CreateMessageIndexes extends Thread {

	private User sender;
	private Message message;
	
	public CreateMessageIndexes(User sender, Message message) {
		this.sender = sender;
		this.message = message;
	}
	
	@Override
	public void run() {
		Key<Message> messageKey = Key.create(Message.class, this.message.getId());
		
		UserFollowers userFollowers = ofy().load().type(UserFollowers.class).ancestor(this.sender).first().now();
		List<Key<User>> followers = userFollowers.getFollowers();
		followers.add(Key.create(User.class, this.sender.getPseudo()));
		
		MessageReceivers messageReceivers = new MessageReceivers(messageKey, this.message.getPublicationDate(), followers);
		ofy().save().entity(messageReceivers);
		
		MessageTags messageTags = new MessageTags(messageKey, this.message.getPublicationDate());
		ofy().save().entity(messageTags);
	}
}
