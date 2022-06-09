/*
package combase.pubsubpublisher.service;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.PubsubMessage;
import combase.pubsubpublisher.globalexceptionhandling.ConnectionNotClosedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@Service
public class GooglePubSubSubscriberServiceKasseAlle {

    private static final Logger LOG = LoggerFactory.getLogger(GooglePubSubSubscriberServiceKasseAlle.class);


    private Subscriber subscriber;

    @Value("${topicIdSubscriber}")
    private String topicIdSubscriber;

    @Value("${projectId}")
    private String projectId;

    @Value("${subscriptionId}")
    private String subscriptionId;

    private String subscriptionName = "projects/cag-test-233015/subscriptions/cag-test-2022-sub-kasse-alle";

    private String topicName = "projects/cag-test-233015/topics/cag-test-2022";

    private final String filter = "attributes:pos";

    @PostConstruct
    public void initializeSubscriber() throws IOException {
        if (!isInitialized()) {

            MessageReceiver receiver =
                    (PubsubMessage message, AckReplyConsumer consumer) -> {
                        // Handle incoming message, then ack the received message.
                        LOG.info("SUBSCRIBER KASSE ALLE RECEIVED MESSAGE ID: " + message.getMessageId());
                        LOG.info("SUBSCRIBER KASSE ALLE RECEIVED DATA: " + message.getData().toStringUtf8());
                        consumer.ack();
                    };

            subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
            // Start the subscriber.
            subscriber.startAsync().awaitRunning();
        }

        LOG.info("CONNECTION TO PUB SUB SUBSCRIBER KASSE ALLE ESTABLISHED");
    }

    @PreDestroy
    public void destroy() throws TimeoutException {
        if (isInitialized()) {
            LOG.info("PRE-DESTROY CALLBACK TRIGGERED SUBSCRIBER KASSE ALLE");
            subscriber.stopAsync();
        }
    }

    public String getSubscriptionName() {
        return !isInitialized() ? null : subscriber.getSubscriptionNameString();
    }

    public boolean isInitialized() {
        return subscriber != null && subscriber.isRunning();
    }

    public boolean subscriberCloseConnection() {
        if (isInitialized()) {
            subscriber.awaitTerminated();
            LOG.info("WILL CLOSE CONNECTION TO PUB SUB SUBSCRIBER KASSE ALLE");
            long duration = 1;
            TimeUnit unit = TimeUnit.MINUTES;
            try {
                LOG.info(String.format("WAITING %d %s TO FINISH ANY TASKS BEFORE CLOSING SUBSCRIBER KASSE ALLE", duration, unit));
                subscriber.awaitTerminated(duration, unit);
                Thread.sleep(unit.toMillis(duration));
            } catch (TimeoutException e) {
                throw new ConnectionNotClosedException(e.toString());
            } catch (InterruptedException e) {
                throw new RuntimeException(e.toString());
            }
            subscriber = null;
            LOG.info("CONNECTION TO PUB SUB SUBSCRIBER KASSE ALLE CLOSED.");
            return true;
        }
        LOG.info("CONNECTION TO PUB SUBSCRIBER KASSE ALLE SUB WAS NOT OPEN BEFORE SENDING CLOSE REQUEST. NO NEED TO CLOSE.");
        return true;
    }
}
*/
