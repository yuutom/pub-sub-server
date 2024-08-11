CREATE TABLE topic (
    topic_id                 BIGINT NOT NULL AUTO_INCREMENT,
    topic_name               VARCHAR(64) NOT NULL,
    owner_publisher_id       BIGINT NOT NULL,
    register_date            DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (topic_id)
);

CREATE TABLE message (
    message_id              BIGINT NOT NULL AUTO_INCREMENT,
    topic_id                BIGINT NOT NULL,
    content                 json NOT NULL,
    publish_date            DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (message_id)
);

CREATE TABLE subscription (
    topic_id                 BIGINT NOT NULL,
    owner_publisher_id       BIGINT NOT NULL,
    subscriber_id            BIGINT NOT NULL,
    subscribe_date            DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (topic_id, owner_publisher_id, subscriber_id)
);

CREATE TABLE ack (
    message_id               BIGINT NOT NULL,
    subscriber_id            BIGINT NOT NULL,
    acked                    BOOLEAN NOT NULL,
    PRIMARY KEY (message_id, subscriber_id)
);