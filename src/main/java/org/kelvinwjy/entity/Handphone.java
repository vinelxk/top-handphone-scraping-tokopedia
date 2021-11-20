package org.kelvinwjy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
@JsonPropertyOrder({"name", "description", "imageLink", "price", "rating", "merchantName", "link"})
public class Handphone {
    public static final String SEPARATOR = ",";
    public static final String DOUBLE_QUOTES = "\"";

    @JsonIgnore
    private String type;
    private String name;
    private String description;
    private String imageLink;
    private double price;
    private double rating;
    private String merchantName;
    private String link;

    public abstract static class HandphoneFormat {

        @JsonProperty("Product Name")
        abstract String getName();

        @JsonProperty("Description")
        abstract String getDescription();

        @JsonProperty("Image Link")
        abstract String getImageLink();

        @JsonProperty("Price")
        abstract String getPrice();

        @JsonProperty("Rating")
        abstract String getRating();

        @JsonProperty("Merchant Name")
        abstract String getMerchantName();

        @JsonProperty("Product Link")
        abstract String getLink();
    }
}
