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
    sender     text CHECK ( length(sender) <= 50),
    content    text,
    status     text CHECK ( length(status) <= 20 ),
    created_at timestamp default now(),
    updated_at timestamp,
    deleted_at timestamp
);

CREATE TABLE IF NOT EXISTS m2m_users_chats
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users (id),
    chat_id BIGINT REFERENCES chats (id)
);