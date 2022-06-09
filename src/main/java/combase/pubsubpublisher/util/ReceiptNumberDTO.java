package combase.pubsubpublisher.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ReceiptNumberDTO {

    private String receiptNumber;


    public ReceiptNumberDTO() {
    }

    public ReceiptNumberDTO(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonProperty("receipt")
    private void unpackNested(Map<String, Object> payload) {
        this.receiptNumber = (String) payload.get("number");
    }
}
