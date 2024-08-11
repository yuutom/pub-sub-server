package com.mercari.merpay.pubsub.usecase;

import com.mercari.merpay.pubsub.entity.Topic;
import com.mercari.merpay.pubsub.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopicService {
    private final TopicRepository topicRepository;
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Topic register(long publisherId, String topicName) {
        Optional<Topic> existingTopic = topicRepository.findByPublisherIdAndTopicName(publisherId, topicName);
        if (existingTopic.isPresent()) {
            throw new DuplicateTopicException("A topic with the same name and publisher already exists. Topic: " + existingTopic.get().toString());
        }
        Topic topic = new Topic(){{
            setOwnerPublisherId(publisherId);
            setTopicName(topicName);
        }};
        topicRepository.insert(topic);
        return topic;
    }

    public Topic subscribe(long ownerPublisherId, String topicName, long subscriberId) {
        Topic targetTopic = topicRepository.findByPublisherIdAndTopicName(ownerPublisherId, topicName)
            .orElseThrow(() -> new TopicNotExistException("The target topic does not exist."));

        boolean hasAlreadyExists = topicRepository.existsTopicSubscription(targetTopic.getTopicId(), targetTopic.getOwnerPublisherId(), subscriberId);
        if (hasAlreadyExists) {
            throw new DuplicateSubscriptionException("Subscription already exists");
        }

        topicRepository.insertSubscription(targetTopic.getTopicId(), targetTopic.getOwnerPublisherId(), subscriberId);
        return targetTopic;
    }

    public static class DuplicateTopicException extends RuntimeException {
        public DuplicateTopicException(String message) {
            super(message);
        }
    }

    public static class TopicNotExistException extends RuntimeException {
        public TopicNotExistException(String message) {
            super(message);
        }
    }

    public static class DuplicateSubscriptionException extends RuntimeException {
        public DuplicateSubscriptionException(String message) {
            super(message);
        }
    }

    public static class UnauthorizedTopicException extends RuntimeException {
        public UnauthorizedTopicException(String message) {
            super(message);
        }
    }

    public static class NotSubscribeTopicException extends RuntimeException {
        public NotSubscribeTopicException(String message) {
            super(message);
        }
    }
}
