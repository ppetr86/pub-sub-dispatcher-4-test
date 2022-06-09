package combase.pubsubpublisher.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class PosNumberDTO {

    private String posNumber;


    public PosNumberDTO() {
    }

    public PosNumberDTO(String posNumber) {
        this.posNumber = posNumber;
    }

    public String getPosNumber() {
        return posNumber;
    }

    public void setPosNumber(String posNumber) {
        this.posNumber = posNumber;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonProperty("pos")
    private void unpackNested(Map<String, Object> payload) {
        this.posNumber = (String) payload.get("number");
    }

}
