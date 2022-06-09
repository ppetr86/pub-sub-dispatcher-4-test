package combase.pubsubpublisher.globalexceptionhandling;

public class ConnectionNotEstablishedException extends RuntimeException {

    public ConnectionNotEstablishedException(String stackTrace) {
        super("CONNECTION TO PUB SUB NOT ESTABLISHED\n" + stackTrace);
    }
}