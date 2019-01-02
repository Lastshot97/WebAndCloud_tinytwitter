package fr.univ.nantes;

import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;

import fr.univ.nantes.model.Message;
import fr.univ.nantes.model.User;

/**
  * Add your first API methods in this class, or you may create another class. In that case, please
  * update your web.xml accordingly.
 **/
@Api(name = "tinytwitter",
     version = "v1")
public class YourFirstAPI {
	
	@ApiMethod(httpMethod = HttpMethod.PUT, path = "users/{pseudo}")
	public User addUser(@Named("pseudo") String pseudo) throws NotFoundException {
		if(UserRepository.getInstance().findUserByPseudo(pseudo) != null) {
			throw new NotFoundException(String.format("User %s already exists !", pseudo));
		}
		return UserRepository.getInstance().create(pseudo);
	}
	
	@ApiMethod(httpMethod = HttpMethod.GET, path = "users")
	public List<User> getUsers() {
		return UserRepository.getInstance().findUsers();
	}
	
	@ApiMethod(httpMethod = HttpMethod.DELETE, path = "users")
	public void deleteUsers() {
		UserRepository.getInstance().deleteUsers();
		MessageRepository.getInstance().deleteMessages();
	}
	
	@ApiMethod(httpMethod = HttpMethod.GET, path = "users/{pseudo}")
	public User getUser(@Named("pseudo") String pseudo) throws NotFoundException {
		User user = UserRepository.getInstance().findUserByPseudo(pseudo);
		if(user == null) {
			throw new NotFoundException(String.format("User %s doesn't exist !", pseudo));
		}
		return user;
	}
	
	@ApiMethod(httpMethod = HttpMethod.POST, path = "users/{pseudo}/timeline")
	public Message postMessage(@Named("pseudo") String pseudo, @Named("message") String messageBody) throws NotFoundException {
		User user = UserRepository.getInstance().findUserByPseudo(pseudo);
		if(user == null) {
			throw new NotFoundException(String.format("User %s doesn't exist !", pseudo));
		}
		return MessageRepository.getInstance().create(user, messageBody);
	}
	
	@ApiMethod(httpMethod = HttpMethod.GET, path = "users/{pseudo}/timeline")
	public List<Message> getTimeline(@Named("pseudo") String pseudo, @Named("limit") int limit) throws NotFoundException {
		User user = UserRepository.getInstance().findUserByPseudo(pseudo);
		if(user == null) {
			throw new NotFoundException(String.format("User %s doesn't exist !", pseudo));
		}
		return MessageRepository.getInstance().findMessageByReceiver(user, limit);
	}
	
	@ApiMethod(httpMethod = HttpMethod.GET, path = "users/{pseudo}/followers")
	public List<User> getFollowers(@Named("pseudo") String pseudo) throws NotFoundException {
		User user = UserRepository.getInstance().findUserByPseudo(pseudo);
		if(user == null) {
			throw new NotFoundException(String.format("User %s doesn't exist !", pseudo));
		}
		return UserRepository.getInstance().getFollowers(user);
	}
	
	@ApiMethod(httpMethod = HttpMethod.GET, path = "users/{pseudo}/followees")
	public List<User> getFollowees(@Named("pseudo") String pseudo) throws NotFoundException {
		User user = UserRepository.getInstance().findUserByPseudo(pseudo);
		if(user == null) {
			throw new NotFoundException(String.format("User %s doesn't exist !", pseudo));
		}
		return UserRepository.getInstance().getFollowees(user);
	}
	
	@ApiMethod(httpMethod = HttpMethod.PUT, path = "users/{follower}/followees/{followee}")
	public void addFollower(@Named("follower") String pseudo1, @Named("followee") String pseudo2) throws NotFoundException {
		User follower = UserRepository.getInstance().findUserByPseudo(pseudo1);
		User followee = UserRepository.getInstance().findUserByPseudo(pseudo2);
		if(follower == null) {
			throw new NotFoundException(String.format("User %s doesn't exist !", pseudo1));
		} else if(followee == null) {
			throw new NotFoundException(String.format("User %s doesn't exist !", pseudo2));
		}
		UserRepository.getInstance().addFollowee(follower, followee);
		UserRepository.getInstance().addFollower(followee, follower);
	}
	
	@ApiMethod(httpMethod = HttpMethod.GET, path = "messages")
	public List<Message> getMessages(@Named("tag") String tag) {
		return MessageRepository.getInstance().findMessageByTag(tag);
	}
	
	@ApiMethod(httpMethod = "patch", path = "messages/{id}")
	public void addTag(@Named("id") Long id, @Named("tag") String tag) throws NotFoundException {
		Message message = MessageRepository.getInstance().findMessage(id);
		if(message == null) {
			throw new NotFoundException("This message doesn't exist");
		}
		MessageRepository.getInstance().addTag(message, tag);
	}
}
