# pub-sub-dispatcher

The endpoints serve as a communication hub to google pub sub.\n
Endpoints: <br />
GET:<br />
/connection/close<br />
/connection/establish<br />
/connection/info<br />
/connection/topic<br />
POST:<br />
/publish<br />
@RequestBody String input<br />
/publish-with-custom-attributes<br />
@RequestBody String input<br />

Set environment variables:<br />
GOOGLE_APPLICATION_CREDENTIALS<br />
dispatcher_password<br />
dispatcher_username<br />
pubSub_topics_bon<br />

## Return entities<br />

All return values are stringified objects with hidden null fields<br />

