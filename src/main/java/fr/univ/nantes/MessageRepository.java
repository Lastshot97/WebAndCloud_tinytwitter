package fr.univ.nantes;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;

import fr.univ.nantes.model.Message;
import fr.univ.nantes.model.MessageReceivers;
import fr.univ.nantes.model.MessageTags;
import fr.univ.nantes.model.User;

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
		
		new CreateMessageIndexes(sender, message).run();
		
		return message;
	}
	
	public void deleteMessages() {
		List<Message> messages = ofy().load().type(Message.class).list();
		ofy().delete().entities(messages).now();
		List<MessageReceivers> indexMR = ofy().load().type(MessageReceivers.class).list();
		ofy().delete().entities(indexMR).now();
		List<MessageTags> indexMT = ofy().load().type(MessageTags.class).list();
		ofy().delete().entities(indexMT).now();
	}
	
	public Message findMessage(Long id) {
		Message message = ofy().load().type(Message.class).id(id).now();
		return message;
	}
	
	public List<Message> findMessageByReceiver(User receiver, int limit) {
		List<MessageReceivers> receiversIndex = ofy().load().type(MessageReceivers.class).filter("receivers", receiver).order("-publicationDate").limit(limit).list();
		
		List<Key<Message>> keys = new ArrayList<Key<Message>>();
		receiversIndex.forEach( (item) -> keys.add(item.getParent()));
		
		List<Message> messages = new ArrayList<Message>();
		messages.addAll(ofy().load().keys(keys).values());
		return messages;
	}
	
	public void addTag(Message message, String tag) {
		ofy().transact(() -> {
			MessageTags tagsIndex = ofy().load().type(MessageTags.class).ancestor(message).first().now();
			tagsIndex.addTag(tag);
			ofy().save().entity(tagsIndex).now();
		});
	}
	
	public List<Message> findMessageByTag(String tag, int limit) {
		List<MessageTags> tagsIndex = ofy().load().type(MessageTags.class).filter("tags", tag).order("-publicationDate").limit(limit).list();
		
		List<Key<Message>> keys = new ArrayList<Key<Message>>();
		tagsIndex.forEach( (item) -> keys.add(item.getParent()));
		
		List<Message> messages = new ArrayList<Message>();
		messages.addAll(ofy().load().keys(keys).values());
		return messages;
	}
}
