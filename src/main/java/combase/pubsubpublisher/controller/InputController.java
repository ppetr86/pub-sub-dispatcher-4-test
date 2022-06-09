package combase.pubsubpublisher.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import combase.pubsubpublisher.service.GooglePubSubCreateSubscriptionService;
import combase.pubsubpublisher.service.GooglePubSubPublisherService;
import combase.pubsubpublisher.util.MyObjectMapper;
import combase.pubsubpublisher.util.PosNumberDTO;
import combase.pubsubpublisher.util.ReceiptNumberDTO;
import combase.pubsubpublisher.util.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.springframework.http.HttpStatus.OK;


@RestController
public class InputController {
    private static final Logger LOG = LoggerFactory.getLogger(InputController.class);
    private static final String NA = "NA";
    private final GooglePubSubPublisherService publisherService;
    private final MyObjectMapper myObjectMapper;
    private final GooglePubSubCreateSubscriptionService subscriptionService;

    public InputController(GooglePubSubPublisherService publisherService, MyObjectMapper myObjectMapper, GooglePubSubCreateSubscriptionService subscriptionService) {
        this.publisherService = publisherService;
        this.myObjectMapper = myObjectMapper;
        this.subscriptionService = subscriptionService;
    }

    /**
     * closes connection to pub sub and nulls the instance of publisher
     */
    @GetMapping(path = "/connection/close")
    public ResponseEntity<String> connectionClose() {
        boolean isClosed = publisherService.publisherCloseConnection();
        String response = myObjectMapper.stringify(new ResponseDTO(!isClosed, null, null));
        return ResponseEntity.status(OK).body(response);
    }

    /**
     * creates connection to pub sub. If successfull returns true
     */
    @GetMapping(path = "/connection/establish")
    public ResponseEntity<String> connectionEstablish() {
        boolean isConnected = publisherService.publisherConnectionCreate();
        String response = myObjectMapper.stringify(new ResponseDTO(isConnected, null));
        return ResponseEntity.status(OK).body(response);
    }

    /**
     * returns whether publisher instance is connected to pub sub
     */
    @GetMapping(path = "/connection/info")
    public ResponseEntity<String> connectionIsConnected() {
        boolean isConnected = publisherService.isInitialized();
        String response = myObjectMapper.stringify(new ResponseDTO(isConnected, null));
        return ResponseEntity.status(OK).body(response);
    }

    /**
     * creates a default subscription structure
     * returns the filters of each subscription
     */
    @PostMapping(path = "/subscription-structure")
    public ResponseEntity<String> createSubscriptionStructure() throws IOException {

        String response = subscriptionService.createSubscriptionWithFilteringExample();
        return ResponseEntity.status(OK).body(response);
    }

    /**
     * returns the name of current topic
     */
    @GetMapping(path = "/connection/topic")
    public ResponseEntity<String> getPublisherTopic() {
        String topic = publisherService.getPublisherTopic();
        String response = myObjectMapper.stringify(new ResponseDTO(null, null, topic));
        return ResponseEntity.status(OK).body(response);
    }

    @PostMapping(path = "/publish-with-filters-test")
    public ResponseEntity<String> publishWithFiltersTest(@RequestBody String input,
                                                         @RequestHeader Map<String, String> headers)
            throws ExecutionException, InterruptedException, TimeoutException {


        String messageId = publisherService.publishMessageWithAttributes(input, headers);

        LOG.info(messageId);
        return ResponseEntity.status(OK).body(messageId);
    }

    /**
     * publishes a payload to pub sub
     *
     * @param input String
     */
    @PostMapping(path = "/publish")
    public ResponseEntity<String> receiptPublish(@RequestBody String input) throws ExecutionException, InterruptedException, TimeoutException {
        ReceiptNumberDTO receiptNumberDTO = null;
        PosNumberDTO posNumberDTO = null;

        try {
            receiptNumberDTO = myObjectMapper.getCustomObjectMapper()
                    .readerFor(ReceiptNumberDTO.class)
                    .readValue(input);
            posNumberDTO = myObjectMapper.getCustomObjectMapper()
                    .readerFor(PosNumberDTO.class)
                    .readValue(input);
        } catch (JsonProcessingException e) {
            LOG.error("DID NOT PARSE RECEIPT NUMBER OR POS NUMBER");
            LOG.error(e.toString(), e);
        }

        String messageId = publisherService.publishMessage(input);
        String response = myObjectMapper.stringify(new ResponseDTO(publisherService.isInitialized(), //
                messageId != null && !messageId.isEmpty(),  //
                null, //
                messageId,  //
                posNumberDTO == null ? NA : posNumberDTO.getPosNumber(), //
                receiptNumberDTO == null ? NA : receiptNumberDTO.getReceiptNumber()));

        LOG.info(response);
        return ResponseEntity.status(OK).body(response);
    }

    @PostMapping(path = "/publish-with-custom-attributes")
    public ResponseEntity<String> receiptPublishWithCustomAttributes(@RequestBody String input,
                                                                     @RequestHeader Map<String, String> headers)
            throws ExecutionException, InterruptedException, TimeoutException {
        ReceiptNumberDTO receiptNumberDTO = null;
        PosNumberDTO posNumberDTO = null;

        try {
            receiptNumberDTO = myObjectMapper.getCustomObjectMapper()
                    .readerFor(ReceiptNumberDTO.class)
                    .readValue(input);
            posNumberDTO = myObjectMapper.getCustomObjectMapper()
                    .readerFor(PosNumberDTO.class)
                    .readValue(input);
        } catch (JsonProcessingException e) {
            LOG.error("DID NOT PARSE RECEIPT NUMBER OR POS NUMBER");
            LOG.error(e.toString(), e);
        }

        String messageId = publisherService.publishMessageWithAttributes(input, headers);
        String response = myObjectMapper.stringify(new ResponseDTO(publisherService.isInitialized(), //
                messageId != null && !messageId.isEmpty(),  //
                null, //
                messageId,  //
                posNumberDTO == null ? NA : posNumberDTO.getPosNumber(), //
                receiptNumberDTO == null ? NA : receiptNumberDTO.getReceiptNumber()));

        LOG.info(response);
        return ResponseEntity.status(OK).body(response);
    }


}
