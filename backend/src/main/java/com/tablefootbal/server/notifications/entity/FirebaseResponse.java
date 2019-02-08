package com.tablefootbal.server.notifications.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class FirebaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("multicast_id")
    private Long multicastId;

    @JsonProperty("success")
    private Integer success;

    @JsonProperty("failure")
    private Integer failure;

    @JsonProperty("canonical_ids")
    private Integer canonicalIds;

    @JsonProperty("results")
    private List<Object> results;

    public FirebaseResponse() {}
}
