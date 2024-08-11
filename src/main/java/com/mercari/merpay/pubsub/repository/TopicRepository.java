package com.mercari.merpay.pubsub.repository;

import com.mercari.merpay.pubsub.entity.Topic;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TopicRepository {
    private final SqlSessionTemplate sqlSessionTemplate;
    public TopicRepository(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public void insert(Topic topic) {
        getMapper().insert(topic);
    }

    public Optional<Topic> findByPublisherIdAndTopicName(long ownerPublisherId, String topicName){
        return getMapper().findByPublisherIdAndTopicName(ownerPublisherId, topicName);
    }

    public List<Topic> findByTopicName(String topicName) {
        return getMapper().findByTopicName(topicName);
    }

    public boolean existsTopicSubscription(long topicId, long ownerPublisherId, long subscriberId){
        return getMapper().existsByTopicIdAndSubscriberId(topicId, ownerPublisherId, subscriberId);
    }

    public void insertSubscription(long topicId, long ownerPublisherId, long subscriberId){
        getMapper().insertSubscription(topicId, ownerPublisherId, subscriberId);
    }

    public boolean isExistTopicSubscription(String topicName, long ownerPublisherId, long subscriberId) {
        return getMapper().isExistTopicSubscription(topicName, ownerPublisherId, subscriberId);
    }

    private TopicMapper getMapper() {
        return this.sqlSessionTemplate.getMapper(TopicMapper.class);
    }
}
