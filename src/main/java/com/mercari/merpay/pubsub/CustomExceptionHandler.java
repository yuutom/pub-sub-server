package com.mercari.merpay.pubsub;

import com.mercari.merpay.pubsub.model.ErrorResponse;
import com.mercari.merpay.pubsub.model.InvalidParam;
import com.mercari.merpay.pubsub.usecase.MessageService;
import com.mercari.merpay.pubsub.usecase.TopicService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TopicService.DuplicateTopicException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateTopicException(TopicService.DuplicateTopicException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponse(){{
                setTitle("topic conflict");
                setDetail(ex.getMessage());
            }});
    }

    @ExceptionHandler(TopicService.TopicNotExistException.class)
    public ResponseEntity<ErrorResponse> handleTopicNotExistException(TopicService.TopicNotExistException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(){{
                setTitle("topic not found");
                setDetail(ex.getMessage());
            }});
    }

    @ExceptionHandler(TopicService.DuplicateSubscriptionException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateSubscriptionException(TopicService.DuplicateSubscriptionException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponse(){{
                setTitle("subscription conflict");
                setDetail(ex.getMessage());
            }});
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(){{
                setTitle("bad request");
                setDetail("parameters are invalid.");
                setParams(ex.getFieldErrors()
                    .stream()
                    .map(fieldError -> new InvalidParam(){{
                        setName(fieldError.getField());
                        setReason(fieldError.getDefaultMessage());
                    }})
                    .collect(Collectors.toList()));
            }});
    }

    @ExceptionHandler(TopicService.UnauthorizedTopicException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedTopicException(TopicService.UnauthorizedTopicException ex) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse(){{
                setTitle("unauthorized publish");
                setDetail(ex.getMessage());
            }});
    }

    @ExceptionHandler(MessageService.NotExistLatestMessageException.class)
    public ResponseEntity<ErrorResponse> handleNotExistLatestMessageException(MessageService.NotExistLatestMessageException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(){{
                setTitle("subscription not found");
                setDetail(ex.getMessage());
            }});
    }

    @ExceptionHandler(TopicService.NotSubscribeTopicException.class)
    public ResponseEntity<ErrorResponse> handleNotSubscribeTopicException(TopicService.NotSubscribeTopicException ex) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse(){{
                setTitle("not exist subscription to target topic");
                setDetail(ex.getMessage());
            }});
    }

    @ExceptionHandler(MessageService.MessageSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMessageSizeExceededException(MessageService.MessageSizeExceededException ex) {
        return ResponseEntity
            .status(HttpStatus.PAYLOAD_TOO_LARGE)
            .body(new ErrorResponse(){{
                setTitle("message size is over");
                setDetail(ex.getMessage());
            }});
    }
}
