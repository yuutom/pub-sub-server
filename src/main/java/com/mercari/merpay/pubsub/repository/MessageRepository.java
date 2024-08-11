package com.mercari.merpay.pubsub.repository;

import com.google.gson.Gson;
import com.mercari.merpay.pubsub.entity.Message;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MessageRepository {
    private final SqlSessionTemplate sqlSessionTemplate;
    private final Gson gson;
    public MessageRepository(SqlSessionTemplate sqlSessionTemplate, Gson gson) {
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.gson = gson;
    }
    public void insert(Message message) {
        getMapper().insert(message, gson.toJson(message.getContent()));
    }
    public void insertAck(long messageId, long subscriberId) {
        getMapper().insertAck(messageId, subscriberId);
    }
    public Optional<Message> getLatestMessage(long topicId, long ownerPublisherId, long subscriberId){
        return getMapper().selectMessage(topicId, ownerPublisherId, subscriberId);
    }
    private MessageMapper getMapper() {
        return this.sqlSessionTemplate.getMapper(MessageMapper.class);
    }
}
