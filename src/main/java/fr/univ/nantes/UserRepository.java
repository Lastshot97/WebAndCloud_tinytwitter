package fr.univ.nantes;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;

import fr.univ.nantes.model.User;
import fr.univ.nantes.model.UserFollowees;
import fr.univ.nantes.model.UserFollowers;

public class UserRepository {

	private static UserRepository instance = null;
	
	private UserRepository() {}
	
	public static UserRepository getInstance() {
		if(instance == null) {
			instance = new UserRepository();
		}
		return instance;
	}
	
	public User create(String pseudo) {
		User user = ofy().transact(() -> {
			User u = new User(pseudo);
			ofy().save().entity(u).now();
			UserFollowers userFollowers = new UserFollowers(Key.create(User.class, u.getPseudo()));
			ofy().save().entity(userFollowers).now();
			UserFollowees userFollowees = new UserFollowees(Key.create(User.class, u.getPseudo()));
			ofy().save().entity(userFollowees).now();
			return u;
		});
		return user;
	}
	
	public void deleteUsers() {
		List<User> users = ofy().load().type(User.class).list();
		ofy().delete().entities(users).now();
		List<UserFollowers> indexUserFollowers = ofy().load().type(UserFollowers.class).list();
		ofy().delete().entities(indexUserFollowers).now();
		List<UserFollowees> indexUserFollowees = ofy().load().type(UserFollowees.class).list();
		ofy().delete().entities(indexUserFollowees).now();
	}
	
	public List<User> findUsers(int limit) {
		List<User> users = ofy().load().type(User.class).limit(limit).list();
		return users;
	}
	
	public User findUserByPseudo(String pseudo) {
		User user = ofy().load().type(User.class).id(pseudo).now();
		return user;
	}
	
	public List<User> getFollowers(User user) {
		UserFollowers userFollowers = ofy().load().type(UserFollowers.class).ancestor(user).first().now();
		List<Key<User>> followers = userFollowers.getFollowers();
		List<User> users = new ArrayList<User>();
		users.addAll(ofy().load().keys(followers).values());
		return users;
	}

	public void addFollower(User user, User follower) {
		ofy().transact(() -> {
			UserFollowers userFollowers = ofy().load().type(UserFollowers.class).ancestor(user).first().now();
			userFollowers.addFollower(Key.create(follower));
			ofy().save().entity(userFollowers).now();
		});
	}
	
	public List<User> getFollowees(User user) {
		UserFollowees userFollowees = ofy().load().type(UserFollowees.class).ancestor(user).first().now();
		List<Key<User>> followees = userFollowees.getFollowees();
		List<User> users = new ArrayList<User>();
		users.addAll(ofy().load().keys(followees).values());
		return users;
	}
	
	public void addFollowee(User user, User followee) {
		ofy().transact(() -> {
			UserFollowees userFollowees = ofy().load().type(UserFollowees.class).ancestor(user).first().now();
			userFollowees.addFollowee(Key.create(followee));
			ofy().save().entity(userFollowees).now();
		});
	}
}
