package com.mercari.merpay.pubsub.usecase;

import com.google.gson.Gson;
import com.mercari.merpay.pubsub.entity.Message;
import com.mercari.merpay.pubsub.entity.Topic;
import com.mercari.merpay.pubsub.repository.MessageRepository;
import com.mercari.merpay.pubsub.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class MessageService {
    private final TopicRepository topicRepository;
    private final MessageRepository messageRepository;
    private final Gson gson;
    public MessageService(TopicRepository topicRepository, MessageRepository messageRepository, Gson gson) {
        this.topicRepository = topicRepository;
        this.messageRepository = messageRepository;
        this.gson = gson;
    }

    private static final long MAX_SIZE = 128;

    public Message publish(long publisherId, String topicName, Map<String, Object> messageContent) {
        long contentLength = gson.toJson(messageContent).length();
        if (contentLength > MAX_SIZE) {
            throw new MessageSizeExceededException("Request size exceeds the configured maximum: " + MAX_SIZE + "bytes");
        }
        List<Topic> targetTopics = topicRepository.findByTopicName(topicName);
        if (targetTopics.isEmpty()) {
            throw new TopicService.TopicNotExistException("There is no topic with the specified name. topicName: " + topicName);
        }
        Topic targetTopic = targetTopics.stream()
                .filter(topic -> topic.getOwnerPublisherId() == publisherId)
                .findFirst()
                .orElse(null);
        if (Objects.isNull(targetTopic)) {
            throw new TopicService.UnauthorizedTopicException("Unauthorized to publish to target topic which owned by another publisher. topicName: " + topicName);
        }

        Message message = new Message(){{
            setTopicId(targetTopic.getTopicId());
            setContent(messageContent);
        }};
        messageRepository.insert(message);
        return message;
    }

    public void ack(long messageId, long subscriberId) {
        messageRepository.insertAck(messageId, subscriberId);
    }

    public Message get(String topicName, long ownerPublisherId, long subscriberId) {
        Topic targetTopic = topicRepository.findByPublisherIdAndTopicName(ownerPublisherId, topicName)
            .orElseThrow(() -> new TopicService.TopicNotExistException("The target topic does not exist."));
        boolean isExist = topicRepository.existsTopicSubscription(targetTopic.getTopicId(), targetTopic.getOwnerPublisherId(), subscriberId);
        if (!isExist) {
            throw new TopicService.NotSubscribeTopicException("Not subscribed to target topic.");
        }
        Optional<Message> message = messageRepository.getLatestMessage(targetTopic.getTopicId(), ownerPublisherId, subscriberId);
        if (message.isEmpty()) {
            throw new NotExistLatestMessageException("There are no latest messages.");
        }
        return message.get();
    }

    public static class NotExistLatestMessageException extends RuntimeException {
        public NotExistLatestMessageException(String message) {
            super(message);
        }
    }

    public static class MessageSizeExceededException extends RuntimeException {
        public MessageSizeExceededException(String message) {
            super(message);
        }
    }
}
