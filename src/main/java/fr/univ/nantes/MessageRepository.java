package fr.univ.nantes;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;

import fr.univ.nantes.model.Message;
import fr.univ.nantes.model.MessageReceivers;
import fr.univ.nantes.model.MessageTags;
import fr.univ.nantes.model.User;
import fr.univ.nantes.model.UserFollowers;

public class MessageRepository {

	private static MessageRepository instance = null;
	
	private MessageRepository() {}
	
	public static MessageRepository getInstance() {
		if(instance == null) {
			instance = new MessageRepository();
		}
		return instance;
	}
	
	public Message create(User sender, String body) {
		Message message = new Message(sender, body);
		ofy().save().entity(message).now();
		
		Key<Message> messageKey = Key.create(Message.class, message.getId());
		
		UserFollowers userFollowers = ofy().load().type(UserFollowers.class).ancestor(sender).first().now();
		List<Key<User>> followers = userFollowers.getFollowers();
		followers.add(Key.create(User.class, sender.getPseudo()));
		
		MessageReceivers messageReceivers = new MessageReceivers(messageKey, message.getPublicationDate(), followers);
		ofy().save().entity(messageReceivers).now();
		
		MessageTags messageTags = new MessageTags(messageKey, message.getPublicationDate());
		ofy().save().entity(messageTags).now();
		
		return message;
	}
	
	public List<Message> findMessages() {
		List<Message> messages = ofy().load().type(Message.class).list();
		return messages;
	}
	
	public void deleteMessages() {
		List<Message> messages = this.findMessages();
		ofy().delete().entities(messages).now();
	}
	
	public Message findMessage(Long id) {
		Message message = ofy().load().type(Message.class).id(id).now();
		return message;
	}
	
	public List<Message> findMessageByReceiver(User receiver, int limit) {
		List<MessageReceivers> receiversIndex = ofy().load().type(MessageReceivers.class).filter("receivers", receiver).order("-publicationDate").limit(limit).list();
		
		List<Key<Message>> keys = new ArrayList<Key<Message>>();
		receiversIndex.forEach( (item) -> keys.add(item.getParent()) );
				
		List<Message> messages = new ArrayList<Message>();
		messages.addAll(ofy().load().keys(keys).values());
		return messages;
	}
	
	public void addTag(Message message, String tag) {
		MessageTags tagsIndex = ofy().load().type(MessageTags.class).ancestor(message).first().now();
		tagsIndex.addTag(tag);
		ofy().save().entity(tagsIndex).now();
	}
	
	public List<Message> findMessageByTag(String tag) {
		List<MessageTags> tagsIndex = ofy().load().type(MessageTags.class).filter("tags", tag).list();
		
		List<Key<Message>> keys = new ArrayList<Key<Message>>();
		tagsIndex.forEach( (item) -> keys.add(item.getParent()) );
		
		List<Message> messages = new ArrayList<Message>();
		messages.addAll(ofy().load().keys(keys).values());
		return messages;
	}
}
