package fr.univ.nantes;

import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.NotFoundException;

import fr.univ.nantes.model.Message;
import fr.univ.nantes.model.User;

/**
  * Add your first API methods in this class, or you may create another class. In that case, please
  * update your web.xml accordingly.
 **/
@Api(name = "tinytwitter",
     version = "v1")
public class TinytwitterEndpoint {
	
	@ApiMethod(httpMethod = HttpMethod.PUT, path = "users/{pseudo}")
	public User addUser(@Named("pseudo") String pseudo) throws BadRequestException {
		if(UserRepository.getInstance().findUserByPseudo(pseudo) != null) {
			throw new BadRequestException(String.format("User %s already exists !", pseudo));
		}
		return UserRepository.getInstance().create(pseudo);
	}
	
	@ApiMethod(httpMethod = HttpMethod.GET, path = "users")
	public List<User> getUsers(@Named("limit") @DefaultValue("10") int limit) {
		return UserRepository.getInstance().findUsers(limit);
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
	public Message postMessage(@Named("pseudo") String pseudo, @Named("message") String messageBody) 
			throws NotFoundException {
		User user = UserRepository.getInstance().findUserByPseudo(pseudo);
		if(user == null) {
			throw new NotFoundException(String.format("User %s doesn't exist !", pseudo));
		}
		return MessageRepository.getInstance().create(user, messageBody);
	}
	
	@ApiMethod(httpMethod = HttpMethod.GET, path = "users/{pseudo}/timeline")
	public List<Message> getTimeline(@Named("pseudo") String pseudo, @Named("limit")  @DefaultValue("10") int limit) 
			throws NotFoundException {
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
	
	@ApiMethod(httpMethod = HttpMethod.PUT, path = "users/{user}/followees/{followee}")
	public void addFollowee(@Named("user") String userPseudo, @Named("followee") String followeePseudo) 
			throws NotFoundException {
		User user = UserRepository.getInstance().findUserByPseudo(userPseudo);
		User followee = UserRepository.getInstance().findUserByPseudo(followeePseudo);
		if(user == null) {
			throw new NotFoundException(String.format("User %s doesn't exist !", userPseudo));
		} else if(followee == null) {
			throw new NotFoundException(String.format("User %s doesn't exist !", followeePseudo));
		}
		UserRepository.getInstance().addFollowee(user, followee);
		UserRepository.getInstance().addFollower(followee, user);
	}
	
	@ApiMethod(httpMethod = HttpMethod.PUT, path = "users/{user}/followers/{follower}")
	public void addFollower(@Named("user") String userPseudo, @Named("follower") String followerPseudo) 
			throws NotFoundException {
		User user = UserRepository.getInstance().findUserByPseudo(userPseudo);
		User follower = UserRepository.getInstance().findUserByPseudo(followerPseudo);
		if(user == null) {
			throw new NotFoundException(String.format("User %s doesn't exist !", userPseudo));
		} else if(follower == null) {
			throw new NotFoundException(String.format("User %s doesn't exist !", followerPseudo));
		}
		UserRepository.getInstance().addFollower(user, follower);
		UserRepository.getInstance().addFollowee(follower, user);
	}
	
	@ApiMethod(httpMethod = HttpMethod.GET, path = "messages")
	public List<Message> getMessages(@Named("tag") String tag, @Named("limit") @DefaultValue("10") int limit) {
		return MessageRepository.getInstance().findMessageByTag(tag, limit);
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
