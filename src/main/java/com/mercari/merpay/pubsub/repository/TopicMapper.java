package com.mercari.merpay.pubsub.repository;

import com.mercari.merpay.pubsub.entity.Topic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TopicMapper {
    void insert(Topic topic);
    void insertSubscription(long topicId, long ownerPublisherId, long subscriberId);
    Optional<Topic> findByPublisherIdAndTopicName(long ownerPublisherId, String topicName);
    List<Topic> findByTopicName(String topicName);
    boolean existsByTopicIdAndSubscriberId(long topicId, long ownerPublisherId, long subscriberId);
    boolean isExistTopicSubscription(String topicName, long ownerPublisherId, long subscriberId);
}
