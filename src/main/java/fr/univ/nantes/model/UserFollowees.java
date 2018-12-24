package fr.univ.nantes.model;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class UserFollowees {

	@Id private Long id;
	@Parent private Key<User> user;
	private List<Key<User>> followees = new ArrayList<Key<User>>();
	
	protected UserFollowees() {}
	
	public UserFollowees(Key<User> user) {
		this.user = user;
	}
	
	public List<Key<User>> getFollowees() {
		return this.followees;
	}
	
	public void addFollowee(Key<User> followee) {
		this.followees.add(followee);
	}
} 
