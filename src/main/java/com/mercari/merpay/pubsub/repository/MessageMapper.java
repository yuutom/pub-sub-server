package com.mercari.merpay.pubsub.repository;

import com.mercari.merpay.pubsub.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MessageMapper {
    void insert(Message message, String contentString);
    void insertAck(long messageId, long subscriberId);
    Optional<Message> selectMessage(long topicId, long ownerPublisherId, long subscriberId);
}
