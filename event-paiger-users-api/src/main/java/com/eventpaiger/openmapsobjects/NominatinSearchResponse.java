package com.eventpaiger.openmapsobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NominatinSearchResponse {

    @JsonProperty("place_id")
    private Long placeId;
    @JsonProperty("licence")
    private String licence;
    @JsonProperty("osm_type")
    private String osmType;
    @JsonProperty("osm_id")
    private Long osmId;
    @JsonProperty("lat")
    private String latitude;
    @JsonProperty("lon")
    private String longitude;
    @JsonProperty("class")
    private String clazz;
    @JsonProperty("type")
    private String type;
    @JsonProperty("place_rank")
    private Integer placeRank;
    @JsonProperty("importance")
    private Double importance;
    @JsonProperty("addresstype")
    private String addressType;
    @JsonProperty("name")
    private String name;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("boundingbox")
    private List<String> boundingBox;

}
