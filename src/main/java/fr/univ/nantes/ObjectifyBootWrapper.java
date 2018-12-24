package fr.univ.nantes;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.google.cloud.datastore.DatastoreOptions;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import fr.univ.nantes.model.Message;
import fr.univ.nantes.model.MessageReceivers;
import fr.univ.nantes.model.MessageTags;
import fr.univ.nantes.model.User;
import fr.univ.nantes.model.UserFollowees;
import fr.univ.nantes.model.UserFollowers;

public class ObjectifyBootWrapper implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ObjectifyService.init(new ObjectifyFactory(
			    DatastoreOptions.newBuilder()
			        .setHost("http://localhost:8484")
			        .setProjectId("tinytwittertp")
			        .build()
			        .getService()
			));
		//ObjectifyService.init();
		ObjectifyService.register(User.class);
		ObjectifyService.register(Message.class);
		ObjectifyService.register(UserFollowees.class);
		ObjectifyService.register(UserFollowers.class);
		ObjectifyService.register(MessageReceivers.class);
		ObjectifyService.register(MessageTags.class);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}

}
