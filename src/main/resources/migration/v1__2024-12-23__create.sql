CREATE TABLE IF NOT EXISTS chats
(
    id         BIGSERIAL PRIMARY KEY,
    name       text CHECK ( length(name) <= 50 ),
    type       text CHECK ( length(type) <= 50 ),
    created_at timestamp default now(),
    updated_at timestamp,
    deleted_at timestamp
);

CREATE TABLE IF NOT EXISTS chat_messages
(
    id         BIGSERIAL PRIMARY KEY,
    chat_id    BIGINT REFERENCES chats (id),
    sender_id  BIGINT REFERENCES users (id),
    content    text,
    status     text CHECK ( length(status) <= 20 ),
    created_at timestamp default now(),
    updated_at timestamp,
    deleted_at timestamp
);

CREATE TABLE IF NOT EXISTS chat_notifications
(
    id          BIGSERIAL PRIMARY KEY,
    sender_id   BIGINT REFERENCES users (id),
    sender_name text
);

CREATE TABLE IF NOT EXISTS m2m_users_chats
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users (id),
    chat_id BIGINT REFERENCES chats (id)
);