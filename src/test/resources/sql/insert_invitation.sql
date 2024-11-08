INSERT INTO invitation(id, token, build_id)
VALUES (1, 'token1', 1),
       (2, 'token2', 2),
       (3, 'token3', 3);
INSERT INTO invitation_principal(invited_user_id, invitation_id)
VALUES ('uuid2', 1),
       ('uuid3', 2);