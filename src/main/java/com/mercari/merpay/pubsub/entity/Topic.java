package com.mercari.merpay.pubsub.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Topic {
    private long topicId;
    private String topicName;
    private long ownerPublisherId;
    Date registerDate;
}
