package combase.pubsubpublisher.globalexceptionhandling;

public class ConnectionNotClosedException extends RuntimeException {

    public ConnectionNotClosedException(String stackTrace) {
        super("CONNECTION TO PUB SUB WAS NOT CLOSED\n" + stackTrace);
    }
}