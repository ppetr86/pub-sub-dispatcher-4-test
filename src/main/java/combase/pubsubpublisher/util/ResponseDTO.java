package combase.pubsubpublisher.util;

import java.io.Serializable;

public class ResponseDTO implements Serializable {

    private Boolean isConnectedToPubSub;
    private Boolean isPublishedToPubSub;
    private String topicNamePubSub;
    private String pubSubMessageId;
    private String posNumber;
    private String receiptNumber;

    public ResponseDTO(Boolean isConnectedToPubSub, Boolean isPublishedToPubSub, String topicNamePubSub, String pubSubMessageId, String posNumber, String receiptNumber) {
        this.isConnectedToPubSub = isConnectedToPubSub;
        this.isPublishedToPubSub = isPublishedToPubSub;
        this.topicNamePubSub = topicNamePubSub;
        this.pubSubMessageId = pubSubMessageId;
        this.posNumber = posNumber;
        this.receiptNumber = receiptNumber;
    }

    public ResponseDTO(Boolean isConnectedToPubSub, Boolean isPublishedToPubSub, String topicNamePubSub, String pubSubMessageId) {
        this.isConnectedToPubSub = isConnectedToPubSub;
        this.isPublishedToPubSub = isPublishedToPubSub;
        this.topicNamePubSub = topicNamePubSub;
        this.pubSubMessageId = pubSubMessageId;
    }

    public ResponseDTO(Boolean isConnectedToPubSub, Boolean isPublishedToPubSub, String topicNamePubSub) {
        this.isConnectedToPubSub = isConnectedToPubSub;
        this.isPublishedToPubSub = isPublishedToPubSub;
        this.topicNamePubSub = topicNamePubSub;
    }

    public ResponseDTO(Boolean isConnectedToPubSub, Boolean isPublishedToPubSub) {
        this.isConnectedToPubSub = isConnectedToPubSub;
        this.isPublishedToPubSub = isPublishedToPubSub;
        this.topicNamePubSub = null;
    }

    public ResponseDTO() {
    }

    public Boolean getConnectedToPubSub() {
        return isConnectedToPubSub;
    }

    public void setConnectedToPubSub(Boolean connectedToPubSub) {
        isConnectedToPubSub = connectedToPubSub;
    }

    public String getPosNumber() {
        return posNumber;
    }

    public void setPosNumber(String posNumber) {
        this.posNumber = posNumber;
    }

    public String getPubSubMessageId() {
        return pubSubMessageId;
    }

    public void setPubSubMessageId(String pubSubMessageId) {
        this.pubSubMessageId = pubSubMessageId;
    }

    public Boolean getPublishedToPubSub() {
        return isPublishedToPubSub;
    }

    public void setPublishedToPubSub(Boolean publishedToPubSub) {
        isPublishedToPubSub = publishedToPubSub;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getTopicNamePubSub() {
        return topicNamePubSub;
    }

    public void setTopicNamePubSub(String topicNamePubSub) {
        this.topicNamePubSub = topicNamePubSub;
    }

    @Override
    public String toString() {
        String str = "\n{\n";
        if (this.isConnectedToPubSub != null)
            str += "isConnectedToPubSub=" + isConnectedToPubSub + ",\n";
        if (this.isPublishedToPubSub != null)
            str += "isPublishedToPubSub=" + isPublishedToPubSub + ",\n";
        if (this.topicNamePubSub != null)
            str += "topicNamePubSub='" + topicNamePubSub + ",\n";
        if (this.pubSubMessageId != null)
            str += "pubSubMessageId='" + pubSubMessageId + ",\n";
        if (this.posNumber != null)
            str += "posNumber='" + posNumber + ",\n";
        if (this.receiptNumber != null)
            str += "receiptNumber='" + receiptNumber + "\n}";
        return str;
    }
}
