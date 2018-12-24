package fr.univ.nantes.model;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class UserFollowers {

	@Id private Long id;
	@Parent private Key<User> user;
	private List<Key<User>> followers = new ArrayList<Key<User>>();
	
	protected UserFollowers() {}
	
	public UserFollowers(Key<User> user) {
		this.user = user;
	}
	
	public List<Key<User>> getFollowers() {
		return this.followers;
	}
	
	public void addFollower(Key<User> follower) {
		this.followers.add(follower);
	}
} 
