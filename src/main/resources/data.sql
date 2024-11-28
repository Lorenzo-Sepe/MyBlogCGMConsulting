INSERT IGNORE INTO authority (created_at, updated_at, authority_name, default_authority, visible) VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'GUEST', 1, 1);
INSERT IGNORE INTO authority (created_at, updated_at, authority_name, default_authority, visible) VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'MEMBER', 0, 1);
INSERT IGNORE INTO authority (created_at, updated_at, authority_name, default_authority, visible) VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'AUTHOR', 0, 1);
INSERT IGNORE INTO authority (created_at, updated_at, authority_name, default_authority, visible) VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'MODERATOR', 0, 1);
INSERT IGNORE INTO authority (created_at, updated_at, authority_name, default_authority, visible) VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ADMIN', 0, 1);

