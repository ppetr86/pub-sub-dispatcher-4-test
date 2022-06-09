package combase.pubsubpublisher.service;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.Subscription;
import combase.pubsubpublisher.globalexceptionhandling.ConnectionNotClosedException;
import combase.pubsubpublisher.globalexceptionhandling.ConnectionNotEstablishedException;
import combase.pubsubpublisher.globalexceptionhandling.MessageNotSentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;


@Service
public class GooglePubSubPublisherService {

    private static final Logger LOG = LoggerFactory.getLogger(GooglePubSubPublisherService.class);


    private Publisher publisher;

    @Value("${pubSub_topics_bon}")
    private String topicBon;


    @PreDestroy
    public void destroy() {
        if (isInitialized()) {
            LOG.info("PRE-DESTROY CALLBACK TRIGGERED PUBLISHER");
            publisher.shutdown();
        }
    }

    public String getPublisherTopic() {
        return !isInitialized() ? null : publisher.getTopicNameString();
    }

    public boolean isInitialized() {
        return publisher != null;
    }

    public String publishMessage(String input) throws ExecutionException, InterruptedException, TimeoutException {
        if (!isInitialized()) {
            publisherConnectionCreate();
        }
        ByteString data = ByteString.copyFromUtf8(input);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(data)
                .build();
        ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
        ApiFutures.addCallback(messageIdFuture, new ApiFutureCallback<>() {
            public void onFailure(Throwable t) {
                throw new MessageNotSentException(t.toString());
            }

            public void onSuccess(String messageId) {
                LOG.info("PUBLISHED MESSAGE ID: " + messageId);
            }
        }, MoreExecutors.directExecutor());
        return messageIdFuture.get(10000L,TimeUnit.MILLISECONDS);
    }

    public String publishMessageWithAttributes(String input, Map<String, String> headers) throws ExecutionException, InterruptedException, TimeoutException {
        if (!isInitialized()) {
            publisherConnectionCreate();
        }

        headers = headers.entrySet().stream()
                .filter(kv -> kv.getKey().contains("attributes."))
                .collect(Collectors.toMap((entry) -> entry.getKey().substring("attributes.".length()), Map.Entry::getValue));

        ByteString data = ByteString.copyFromUtf8(input);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(data)
                .putAllAttributes(headers)
                .build();

        ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);

        ApiFutures.addCallback(messageIdFuture, new ApiFutureCallback<>() {
            public void onFailure(Throwable t) {
                throw new MessageNotSentException(t.toString());
            }

            public void onSuccess(String messageId) {
                LOG.info("PUBLISHED MESSAGE ID: " + messageId);
            }
        }, MoreExecutors.directExecutor());
        return messageIdFuture.get(10000L,TimeUnit.MILLISECONDS);
    }

    public boolean publisherCloseConnection() {
        if (isInitialized()) {
            publisher.shutdown();
            LOG.info("WILL CLOSE CONNECTION TO PUB SUB PUBLISHER");
            long duration = 1;
            TimeUnit unit = TimeUnit.MINUTES;
            try {
                LOG.info(String.format("WAITING %d %s TO FINISH ANY TASKS BEFORE CLOSING", duration, unit));
                publisher.awaitTermination(duration, unit);
                Thread.sleep(unit.toMillis(duration));
            } catch (InterruptedException e) {
                throw new ConnectionNotClosedException(e.toString());
            }
            publisher = null;
            LOG.info("CONNECTION TO PUB SUB PUBLISHER CLOSED.");
            return true;
        }
        LOG.info("CONNECTION TO PUB SUB PUBLISHER WAS NOT OPEN BEFORE SENDING CLOSE REQUEST. NO NEED TO CLOSE.");
        return true;
    }

    @PostConstruct
    public boolean publisherConnectionCreate() {
        if (!isInitialized()) {
            try {
                LOG.info("CREATING PUBLISHER CONNECTION");
                publisher = Publisher.newBuilder(topicBon).build();
                LOG.info("CREATED PUBLISHER CONNECTION");
            } catch (IOException e) {
                publisher = null;
                throw new ConnectionNotEstablishedException(e.toString());
            }
        }
        LOG.info("CONNECTION TO PUB SUB PUBLISHER ESTABLISHED");
        return true;
    }
}
