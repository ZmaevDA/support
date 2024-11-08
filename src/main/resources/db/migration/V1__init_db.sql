CREATE TABLE country
(
    id   BIGSERIAL    NOT NULL PRIMARY KEY,
    iso  VARCHAR(255) NOT NULL,
    iso3 VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE principal
(
    id            VARCHAR(255) NOT NULL PRIMARY KEY,
    country_id    BIGINT,
    username      VARCHAR(255) NOT NULL,
    description   VARCHAR(255),
    email         VARCHAR(255),
    age           INT,
    in_game_hours INT,
    __deleted     BOOLEAN      NOT NULL DEFAULT false,
    CONSTRAINT fk_principal_country FOREIGN KEY (country_id) REFERENCES country (id)
);

CREATE TABLE build
(
    id          BIGSERIAL    NOT NULL PRIMARY KEY,
    user_id     VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    rating FLOAT NOT NULL DEFAULT 0,
    is_private  BOOLEAN      NOT NULL DEFAULT false
);

CREATE TABLE comment
(
    id           BIGSERIAL    NOT NULL PRIMARY KEY,
    user_id      VARCHAR(255) NOT NULL,
    build_id     BIGINT       NOT NULL,
    content      VARCHAR      NOT NULL,
    commented_at TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP DEFAULT null,
    CONSTRAINT fk_comment_principal FOREIGN KEY (user_id) REFERENCES principal (id),
    CONSTRAINT fk_comment_build FOREIGN KEY (build_id) REFERENCES build (id)
);

CREATE TABLE view
(
    id        BIGSERIAL    NOT NULL PRIMARY KEY,
    user_id   VARCHAR(255) NOT NULL,
    build_id  BIGINT       NOT NULL,
    viewed_at TIMESTAMP    NOT NULL,
    CONSTRAINT fk_view_principal FOREIGN KEY (user_id) REFERENCES principal (id),
    CONSTRAINT fk_view_build FOREIGN KEY (build_id) REFERENCES build (id)
);

CREATE TABLE reaction
(
    id            BIGSERIAL    NOT NULL PRIMARY KEY,
    user_id       VARCHAR(255) NOT NULL,
    build_id      BIGINT       NOT NULL,
    reaction_type VARCHAR(255) NOT NULL,
    reacted_at    TIMESTAMP    NOT NULL,
    updated_at    TIMESTAMP    NOT NULL,
    CONSTRAINT fk_reaction_principal FOREIGN KEY (user_id) REFERENCES principal (id),
    CONSTRAINT fk_reaction_build FOREIGN KEY (build_id) REFERENCES build (id)
);

CREATE TABLE attribute
(
    id           BIGSERIAL NOT NULL PRIMARY KEY,
    vigor        INTEGER   NOT NULL,
    mind         INTEGER   NOT NULL,
    endurance    INTEGER   NOT NULL,
    strength     INTEGER   NOT NULL,
    dexterity    INTEGER   NOT NULL,
    intelligence INTEGER   NOT NULL,
    faith        INTEGER   NOT NULL,
    arcana       INTEGER   NOT NULL
);

CREATE TABLE resist
(
    id         BIGSERIAL NOT NULL PRIMARY KEY,
    vitality   INTEGER   NOT NULL,
    focus      INTEGER   NOT NULL,
    robustness INTEGER   NOT NULL,
    immunity   INTEGER   NOT NULL,
    holy       INTEGER   NOT NULL,
    lightning  INTEGER   NOT NULL,
    fire       INTEGER   NOT NULL,
    magic      INTEGER   NOT NULL,
    pierce     INTEGER   NOT NULL,
    splash     INTEGER   NOT NULL,
    strike     INTEGER   NOT NULL,
    physical   INTEGER   NOT NULL
);

CREATE TABLE ashes_of_war
(
    id          BIGSERIAL    NOT NULL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(1000)
);

CREATE TABLE ashes_of_war_weapon
(
    id              BIGSERIAL NOT NULL PRIMARY KEY,
    ashes_of_war_id BIGINT    NOT NULL,
    weapon_id       BIGINT    NOT NULL,
    CONSTRAINT fk_ashes_of_war_weapon_ashes_of_war FOREIGN KEY (ashes_of_war_id) REFERENCES ashes_of_war (id)
);

CREATE TABLE weapon
(
    id           BIGSERIAL    NOT NULL PRIMARY KEY,
    attribute_id BIGINT       NOT NULL,
    name         VARCHAR(255) NOT NULL,
    description  VARCHAR(1000),
    weight       FLOAT        NOT NULL
);

CREATE TABLE build_weapon
(
    id                     BIGSERIAL NOT NULL PRIMARY KEY,
    build_id               BIGINT    NOT NULL,
    ashes_of_war_weapon_id BIGINT    NOT NULL,
    CONSTRAINT fk_build_weapon_build FOREIGN KEY (build_id) REFERENCES build (id),
    CONSTRAINT fk_build_weapon_ashes_of_war_weapon FOREIGN KEY (ashes_of_war_weapon_id) REFERENCES ashes_of_war_weapon (id)
);

CREATE TABLE start_class
(
    id           BIGSERIAL    NOT NULL PRIMARY KEY,
    attribute_id BIGINT       NOT NULL,
    name         VARCHAR(255) NOT NULL,
    level        INTEGER      NOT NULL,
    health       FLOAT        NOT NULL,
    mana         FLOAT        NOT NULL,
    stamina      FLOAT        NOT NULL,
    CONSTRAINT fk_start_class_attribute FOREIGN KEY (attribute_id) REFERENCES attribute (id)
);

CREATE TABLE inventory_item
(
    id          BIGSERIAL    NOT NULL PRIMARY KEY,
    resist_id   BIGINT       NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    item_type   VARCHAR(255) NOT NULL,
    weight      FLOAT        NOT NULL,
    image_url   VARCHAR(255),
    CONSTRAINT fk_inventory_item_resist FOREIGN KEY (resist_id) REFERENCES resist (id)
);

CREATE TABLE build_inventory_item
(
    id                BIGSERIAL NOT NULL PRIMARY KEY,
    build_id          BIGINT,
    inventory_item_id BIGINT,
    CONSTRAINT fk_build_inventory_item_build FOREIGN KEY (build_id) REFERENCES build (id),
    CONSTRAINT fk_build_inventory_item_inventory_item FOREIGN KEY (inventory_item_id) REFERENCES inventory_item (id)
);

CREATE TABLE character
(
    id             BIGSERIAL    NOT NULL PRIMARY KEY,
    start_class_id BIGINT       NOT NULL,
    attribute_id   BIGINT       NOT NULL,
    build_id       BIGINT       NOT NULL,
    name           VARCHAR(255) NOT NULL,
    CONSTRAINT fk_character_start_class_id FOREIGN KEY (start_class_id) REFERENCES start_class (id),
    CONSTRAINT fk_character_attribute FOREIGN KEY (attribute_id) REFERENCES attribute (id),
    CONSTRAINT fk_character_build FOREIGN KEY (build_id) REFERENCES build (id)
);
