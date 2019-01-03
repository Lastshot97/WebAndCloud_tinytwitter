---------------------------------------------------------------------------------------------------------------
How to test Objectify in a local server ?
---------------------------------------------------------------------------------------------------------------

1. execute : gcloud components install cloud-datastore-emulator
2. execute : gcloud beta emulators datastore start --host-port=localhost:8484
3. set environment variables :
	- set DATASTORE_EMULATOR_HOST=localhost:8484
	- set DATASTORE_PROJECT_ID=my-project-id
	- set DATASTORE_USE_PROJECT_ID_AS_APP_ID=true
	
---------------------------------------------------------------------------------------------------------------
How to use this API :
---------------------------------------------------------------------------------------------------------------

local : http://localhost:8080/_ah/api ...
cloud : https://tinytwittertp.appspot.com/_ah/api ...

- (get)   	/tinytwitter/v1/users (retrieves all users)
- (delete)	/tinytwitter/v1/users (deletes all users and messages)
- (put)   	/tinytwitter/v1/users/{pseudo} (creates a new user with the given pseudo)
- (get)   	/tinytwitter/v1/users/{pseudo} (retrieves the user with the given pseudo)
- (post)  	/tinytwitter/v1/users/{pseudo}/timeline?message=... (adds a new message on the user's timeline)
- (get)   	/tinytwitter/v1/users/{pseudo}/timeline (retrieves all messages of the user's timeline)
- (get)   	/tinytwitter/v1/users/{pseudo}/followers (retrieves user's followers)
- (get)   	/tinytwitter/v1/users/{pseudo}/followees (retrieves user's followees)
- (put)   	/tinytwitter/v1/users/{pseudo1}/followees/{pseudo2} (makes a user {pseudo1} to follow another one {pseudo2})
- (get)   	/tinytwitter/v1/messages?tag=... (retrieves all messages which have the given tag)
- (patch) 	/tinytwitter/v1/messages/{id}?tag=... (adds a new tag to the message with the given id)

---------------------------------------------------------------------------------------------------------------