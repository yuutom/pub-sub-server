package com.mercari.merpay.pubsub.controller;

import com.mercari.merpay.pubsub.entity.Topic;
import com.mercari.merpay.pubsub.model.RegisterTopicRequest;
import com.mercari.merpay.pubsub.model.SubscribeTopicRequest;
import com.mercari.merpay.pubsub.model.TopicRegisterResponse;
import com.mercari.merpay.pubsub.model.TopicSubscribeResponse;
import com.mercari.merpay.pubsub.usecase.TopicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicController implements TopicApi{
    private final TopicService topicService;
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @Override
    public ResponseEntity<TopicRegisterResponse> registerTopic(RegisterTopicRequest request) {
        Topic registeredTopic = topicService.register(request.getPublisherId(), request.getTopicName());
        var response = new TopicRegisterResponse();
        response.setTopicId(registeredTopic.getTopicId());
        response.setTopicName(registeredTopic.getTopicName());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<TopicSubscribeResponse> subscribeTopic(SubscribeTopicRequest request) {
        Topic subscribedTopic = topicService.subscribe(request.getOwnerPublisherId(), request.getTopicName(), request.getSubscriberId());
        var response = new TopicSubscribeResponse();
        response.setTopicId(subscribedTopic.getTopicId());
        response.setTopicName(subscribedTopic.getTopicName());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
