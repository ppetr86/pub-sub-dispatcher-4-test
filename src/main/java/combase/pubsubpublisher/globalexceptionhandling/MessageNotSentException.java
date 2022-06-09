package combase.pubsubpublisher.globalexceptionhandling;

public class MessageNotSentException extends RuntimeException {

    public MessageNotSentException(String stackTrace) {
        super("MESSAGE NOT SENT\n" + stackTrace);
    }
}
