package com.mercari.merpay.pubsub.usecase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.mercari.merpay.pubsub.entity.Message;
import com.mercari.merpay.pubsub.entity.Topic;
import com.mercari.merpay.pubsub.repository.MessageRepository;
import com.mercari.merpay.pubsub.repository.TopicRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private Gson gson;

    @InjectMocks
    private MessageService messageService;

    Map<String, Object> messageContent = new HashMap<>(){{
        put("key", "value");
    }};

    @Test
    void publish_ok() {
        long publisherId = 1L;
        String topicName = "Valid Topic";

        List<Topic> topics = new ArrayList<>();
        Topic topic = new Topic();
        topic.setTopicId(1L);
        topic.setOwnerPublisherId(publisherId);
        topic.setTopicName(topicName);
        topics.add(topic);

        when(topicRepository.findByTopicName(topicName)).thenReturn(topics);
        when(gson.toJson(messageContent)).thenReturn("serialized message content");

        Message result = messageService.publish(publisherId, topicName, messageContent);

        assertNotNull(result);
        assertEquals(1L, result.getTopicId());
        assertEquals(messageContent, result.getContent());
        verify(messageRepository).insert(any(Message.class));
    }

    @Test
    void publish_UnauthorizedTopicException() {
        long publisherId = 1L;
        long otherPublisherId = 2L;
        String topicName = "Valid Topic";
        Map<String, Object> messageContent = new HashMap<>();

        List<Topic> topics = new ArrayList<>();
        Topic topic = new Topic();
        topic.setTopicId(1L);
        topic.setOwnerPublisherId(otherPublisherId);  // Owned by another publisher
        topic.setTopicName(topicName);
        topics.add(topic);

        when(topicRepository.findByTopicName(topicName)).thenReturn(topics);
        when(gson.toJson(messageContent)).thenReturn("serialized message content");

        assertThrows(TopicService.UnauthorizedTopicException.class, () -> {
            messageService.publish(publisherId, topicName, messageContent);
        });
    }

    @Test
    void get_TopicNotExistException() {
        String topicName = "Unknown Topic";
        long ownerId = 1L;
        long subscriberId = 2L;

        when(topicRepository.findByPublisherIdAndTopicName(ownerId, topicName)).thenReturn(Optional.empty());

        assertThrows(TopicService.TopicNotExistException.class, () -> {
            messageService.get(topicName, ownerId, subscriberId);
        });
    }

    @Test
    void get_NotSubscribeTopicException() {
        String topicName = "Existing Topic";
        long ownerId = 1L;
        long subscriberId = 2L;
        Topic foundTopic = new Topic();
        foundTopic.setTopicId(3L);
        foundTopic.setOwnerPublisherId(ownerId);
        foundTopic.setTopicName(topicName);

        when(topicRepository.findByPublisherIdAndTopicName(ownerId, topicName)).thenReturn(Optional.of(foundTopic));
        when(topicRepository.existsTopicSubscription(3L, ownerId, subscriberId)).thenReturn(false);

        assertThrows(TopicService.NotSubscribeTopicException.class, () -> {
            messageService.get(topicName, ownerId, subscriberId);
        });
    }

    @Test
    void get_NotExistLatestMessageException() {
        String topicName = "Active Topic";
        long ownerId = 1L;
        long subscriberId = 2L;
        Topic foundTopic = new Topic();
        foundTopic.setTopicId(3L);
        foundTopic.setOwnerPublisherId(ownerId);
        foundTopic.setTopicName(topicName);

        when(topicRepository.findByPublisherIdAndTopicName(ownerId, topicName)).thenReturn(Optional.of(foundTopic));
        when(topicRepository.existsTopicSubscription(3L, ownerId, subscriberId)).thenReturn(true);
        when(messageRepository.getLatestMessage(3L, ownerId, subscriberId)).thenReturn(Optional.empty());

        assertThrows(MessageService.NotExistLatestMessageException.class, () -> {
            messageService.get(topicName, ownerId, subscriberId);
        });
    }

    @Test
    void get_WhenMessageExists_ShouldReturnMessage() {
        // Given
        String topicName = "Active Topic";
        long ownerId = 1L;
        long subscriberId = 2L;
        Topic foundTopic = new Topic();
        foundTopic.setTopicId(3L);
        foundTopic.setOwnerPublisherId(ownerId);
        foundTopic.setTopicName(topicName);
        Message latestMessage = new Message();
        latestMessage.setTopicId(3L);
        latestMessage.setContent(Map.of("data", "value"));

        when(topicRepository.findByPublisherIdAndTopicName(ownerId, topicName)).thenReturn(Optional.of(foundTopic));
        when(topicRepository.existsTopicSubscription(3L, ownerId, subscriberId)).thenReturn(true);
        when(messageRepository.getLatestMessage(3L, ownerId, subscriberId)).thenReturn(Optional.of(latestMessage));

        Message result = messageService.get(topicName, ownerId, subscriberId);

        assertNotNull(result);
        assertEquals(3L, result.getTopicId());
        assertEquals("value", result.getContent().get("data"));
    }
}

