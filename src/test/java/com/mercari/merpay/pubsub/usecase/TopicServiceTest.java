package com.mercari.merpay.pubsub.usecase;

import com.mercari.merpay.pubsub.entity.Topic;
import com.mercari.merpay.pubsub.repository.TopicRepository;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicService topicService;

    @Test
    public void register_OK() {
        long publisherId = 1L;
        String topicName = "New Topic";
        when(topicRepository.findByPublisherIdAndTopicName(publisherId, topicName)).thenReturn(Optional.empty());

        Topic result = topicService.register(publisherId, topicName);

        assertNotNull(result);
        assertEquals(topicName, result.getTopicName());
        assertEquals(publisherId, result.getOwnerPublisherId());
        verify(topicRepository).insert(any(Topic.class));
    }

    @Test
    public void register_DuplicateTopicException() {
        long publisherId = 1L;
        String topicName = "Existing Topic";
        Topic existingTopic = new Topic();
        existingTopic.setOwnerPublisherId(publisherId);
        existingTopic.setTopicName(topicName);

        when(topicRepository.findByPublisherIdAndTopicName(publisherId, topicName)).thenReturn(Optional.of(existingTopic));

        assertThrows(TopicService.DuplicateTopicException.class, () -> topicService.register(publisherId, topicName));
    }

    @Test
    public void subscribe_TopicNotExistException() {
        long ownerId = 1L;
        String topicName = "Nonexistent Topic";
        long subscriberId = 2L;

        when(topicRepository.findByPublisherIdAndTopicName(ownerId, topicName)).thenReturn(Optional.empty());

        assertThrows(TopicService.TopicNotExistException.class, () -> topicService.subscribe(ownerId, topicName, subscriberId));
    }

    @Test
    public void subscribe_OK() {
        long ownerId = 1L;
        String topicName = "Existing Topic";
        long subscriberId = 2L;
        Topic topic = new Topic();
        topic.setTopicId(1L);
        topic.setOwnerPublisherId(ownerId);
        topic.setTopicName(topicName);

        when(topicRepository.findByPublisherIdAndTopicName(ownerId, topicName)).thenReturn(Optional.of(topic));
        when(topicRepository.existsTopicSubscription(1L, ownerId, subscriberId)).thenReturn(false);

        Topic result = topicService.subscribe(ownerId, topicName, subscriberId);

        assertNotNull(result);
        verify(topicRepository).insertSubscription(1L, ownerId, subscriberId);
    }

    @Test
    public void subscribe_DuplicateSubscriptionException() {
        long ownerId = 1L;
        String topicName = "Topic";
        long subscriberId = 2L;
        Topic topic = new Topic();
        topic.setTopicId(1L);
        topic.setOwnerPublisherId(ownerId);
        topic.setTopicName(topicName);

        when(topicRepository.findByPublisherIdAndTopicName(ownerId, topicName)).thenReturn(Optional.of(topic));
        when(topicRepository.existsTopicSubscription(1L, ownerId, subscriberId)).thenReturn(true);

        assertThrows(TopicService.DuplicateSubscriptionException.class, () -> topicService.subscribe(ownerId, topicName, subscriberId));
    }
}

