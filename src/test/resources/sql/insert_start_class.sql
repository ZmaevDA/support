INSERT INTO attribute (id, vigor, mind, endurance, strength, dexterity, intelligence, faith, arcana)
VALUES (1, 9, 15, 9, 8, 12, 16, 7, 9);
INSERT INTO start_class (id, attribute_id, name, level, health, mana, stamina)
VALUES (1, 1, 'Astrologer', 6, 396, 95, 94);

INSERT INTO attribute (id, vigor, mind, endurance, strength, dexterity, intelligence, faith, arcana)
VALUES (2, 10, 11, 10, 9, 13, 9, 8, 14);
INSERT INTO start_class (id, attribute_id, name, level, health, mana, stamina)
VALUES (2, 2, 'Bandit', 5, 414, 82, 96);

INSERT INTO attribute (id, vigor, mind, endurance, strength, dexterity, intelligence, faith, arcana)
VALUES (3, 10, 13, 10, 12, 12, 9, 14, 9);
INSERT INTO start_class (id, attribute_id, name, level, health, mana, stamina)
VALUES (3, 3, 'Confessor', 10, 414, 88, 96);

INSERT INTO attribute (id, vigor, mind, endurance, strength, dexterity, intelligence, faith, arcana)
VALUES (4, 14, 9, 12, 16, 9, 7, 8, 11);
INSERT INTO start_class (id, attribute_id, name, level, health, mana, stamina)
VALUES (4, 4, 'Hero', 7, 499, 88, 96);

INSERT INTO attribute (id, vigor, mind, endurance, strength, dexterity, intelligence, faith, arcana)
VALUES (5, 11, 12, 11, 11, 14, 14, 6, 9);
INSERT INTO start_class (id, attribute_id, name, level, health, mana, stamina)
VALUES (5, 5, 'Prisoner', 9, 434, 85, 97);

INSERT INTO attribute (id, vigor, mind, endurance, strength, dexterity, intelligence, faith, arcana)
VALUES (6, 10, 14, 8, 11, 10, 7, 16, 11);
INSERT INTO start_class (id, attribute_id, name, level, health, mana, stamina)
VALUES (6, 6, 'Prophet', 7, 414, 91, 92);

INSERT INTO attribute (id, vigor, mind, endurance, strength, dexterity, intelligence, faith, arcana)
VALUES (7, 12, 11, 13, 12, 15, 9, 8, 8);
INSERT INTO start_class (id, attribute_id, name, level, health, mana, stamina)
VALUES (7, 7, 'Samurai', 9, 455, 82, 101);

INSERT INTO attribute (id, vigor, mind, endurance, strength, dexterity, intelligence, faith, arcana)
VALUES (8, 15, 10, 11, 14, 13, 9, 9, 7);
INSERT INTO start_class (id, attribute_id, name, level, health, mana, stamina)
VALUES (8, 8, 'Vagabond', 9, 522, 78, 97);

INSERT INTO attribute (id, vigor, mind, endurance, strength, dexterity, intelligence, faith, arcana)
VALUES (9, 11, 12, 11, 10, 16, 10, 8, 9);
INSERT INTO start_class (id, attribute_id, name, level, health, mana, stamina)
VALUES (9, 9, 'Warrior', 8, 434, 85, 97);

INSERT INTO attribute (id, vigor, mind, endurance, strength, dexterity, intelligence, faith, arcana)
VALUES (10, 10, 10, 10, 10, 10, 10, 10, 10);
INSERT INTO start_class (id, attribute_id, name, level, health, mana, stamina)
VALUES (10, 10, 'Wretch', 1, 414, 78, 96);

ALTER SEQUENCE attribute_id_seq RESTART WITH 11;
ALTER SEQUENCE start_class_id_seq RESTART WITH 11;