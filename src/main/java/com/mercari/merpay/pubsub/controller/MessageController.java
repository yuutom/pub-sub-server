package com.mercari.merpay.pubsub.controller;

import com.mercari.merpay.pubsub.entity.Message;
import com.mercari.merpay.pubsub.model.AckMessageRequest;
import com.mercari.merpay.pubsub.model.MessageGetResponse;
import com.mercari.merpay.pubsub.model.MessagePublishResponse;
import com.mercari.merpay.pubsub.model.PublishMessageRequest;
import com.mercari.merpay.pubsub.usecase.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController implements MessageApi{
    private final MessageService messageService;
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    @Override
    public ResponseEntity<MessagePublishResponse> publishMessage(PublishMessageRequest request) {
        Message publishedMessage = messageService.publish(request.getPublisherId(), request.getTopicName(), request.getMessageContent());
        var response = new MessagePublishResponse();
        response.setMessageId(publishedMessage.getMessageId());
        response.setMessageContent(publishedMessage.getContent());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<Void> ackMessage(AckMessageRequest request) {
        messageService.ack(request.getMessageId(), request.getSubscriberId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<MessageGetResponse> getMessage(@RequestParam String topicName, @RequestParam Long ownerPublisherId, @RequestParam Long subscriberId) {
        Message publishedMessage = messageService.get(topicName, ownerPublisherId, subscriberId);
        var response = new MessageGetResponse();
        response.setMessageId(publishedMessage.getMessageId());
        response.setMessageContent(publishedMessage.getContent());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
