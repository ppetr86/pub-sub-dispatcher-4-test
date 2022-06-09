package combase.pubsubpublisher.service;

import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.RetryPolicy;
import com.google.pubsub.v1.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GooglePubSubCreateSubscriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(GooglePubSubCreateSubscriptionService.class);

    public String createSubscriptionWithFilteringExample() throws IOException {

        String projectId = "cag-test-233015";
        String topicId = "cag-test-2022";

        String subscriptionId;
        String or = " OR ";

        String filter1;
        String filter2;
        String filter3;
        String filter4;
        String filter5;
        String filter6;
        String filter7;
        String filter8;
        String filterTotal;

        String result = "";

        //loop over accounts
        for (int accountNr = 1; accountNr <= 2; accountNr++) {
            //loop over subsidiaries
            for (int storeNr = 1; storeNr <= 2; storeNr++) {
                //loop over kassen
                for (int kasseNr = 1; kasseNr <= 3; kasseNr++) {
                    subscriptionId = String.format("eu.account%s.store%s.kasse%s", accountNr, storeNr, kasseNr);
                    //filter1 = "hasPrefix(attributes.instanz,\"eu\")"; // instanz filter
                    //filter2 = String.format("hasPrefix(attributes.instanz_account,\"eu.account%s\")", accountNr); // account filter
                    //filter3 = String.format("hasPrefix(attributes.instanz_account_store,\"eu.account%s.store%s\")", accountNr, storeNr); // store filter
                    //filter4 = String.format("hasPrefix(attributes.instanz_account_store_kasse,\"eu.account%s.store%s.kasse%s\")", accountNr, storeNr, kasseNr); // kasse filter

                    //individual filters from here
                    filter5 = "hasPrefix(attributes.eu,\"true\")"; //instanz filter
                    filter6 = String.format("hasPrefix(attributes.eu_account%s,\"true\")", accountNr); //account filter
                    filter7 = String.format("hasPrefix(attributes.eu_account%s_store%s,\"true\")", accountNr, storeNr); //store filter
                    filter8 = String.format("hasPrefix(attributes.eu_account%s_store%s_kasse%s,\"true\")", accountNr, storeNr, kasseNr); //kasse filter
                    filterTotal = /*filter1 + or + filter2 + or + filter3 + or +  filter4 + or +*/ filter5 + or + filter6 + or + filter7 + or + filter8;
                    try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {

                        ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);
                        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);

                        Subscription subscription = subscriptionAdminClient.createSubscription(
                                Subscription.newBuilder()
                                        .setAckDeadlineSeconds(600)
                                        .setEnableExactlyOnceDelivery(false)
                                        .setFilter(filterTotal)
                                        //one week
                                        .setMessageRetentionDuration(com.google.protobuf.Duration.newBuilder().setSeconds(604800).build())
                                        .setName(subscriptionName.toString())
                                        .setRetryPolicy(RetryPolicy.newBuilder()
                                                .setMinimumBackoff(com.google.protobuf.Duration.newBuilder().setSeconds(10).build())
                                                .setMaximumBackoff(com.google.protobuf.Duration.newBuilder().setSeconds(300).build())
                                                .build())
                                        .setRetainAckedMessages(false)
                                        .setTopic(topicName.toString())
                                        .build());

                        result += subscription.getAllFields() + "\n";
                        LOG.info("Created a subscription with filtering enabled: " + subscription.getAllFields());
                    }
                }
            }
        }
        return result;
    }

}
