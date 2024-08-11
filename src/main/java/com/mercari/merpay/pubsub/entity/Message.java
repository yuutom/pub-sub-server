package com.mercari.merpay.pubsub.entity;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class Message {
    private long messageId;
    private long topicId;
    private Map<String, Object> content;
    private Date publishDate;
}
