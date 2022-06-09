# pub-sub-dispatcher

This projects endpoints serves as a communication hub to google pub sub.
Endpoints:
GET:
/connection/close
/connection/establish
/connection/info
/connection/topic
POST:
/publish
    @RequestBody String input

Dont forgte to set environment variables, especially path to the pub sub json key
GOOGLE_APPLICATION_CREDENTIALS
dispatcher.password
dispatcher.username
pubSub_subscriptions_bonDwh
pubSub_subscriptions_bonStm
pubSub_subscriptions_bonSub2
pubSub_topics_bon
pubSub_topics_bonDeadLetter

## Return entities

All return values are stringified objects with hidden null fields

