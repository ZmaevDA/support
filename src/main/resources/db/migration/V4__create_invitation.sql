CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS invitation
(
    id              BIGSERIAL    NOT NULL PRIMARY KEY,
    token           VARCHAR(255) NOT NULL DEFAULT gen_random_uuid(),
    build_id        BIGINT       NOT NULL,
    CONSTRAINT fk_invitation_build FOREIGN KEY (build_id) REFERENCES build (id)
);

CREATE TABLE IF NOT EXISTS invitation_principal
(
    id              BIGSERIAL    NOT NULL PRIMARY KEY,
    invited_user_id VARCHAR(255) NOT NULL,
    invitation_id   BIGINT       NOT NULL,
    CONSTRAINT fk_invitation_principal_invitation FOREIGN KEY (invitation_id) REFERENCES invitation (id),
    CONSTRAINT fk_invitation_principal_principal FOREIGN KEY (invited_user_id) REFERENCES principal (id)
);