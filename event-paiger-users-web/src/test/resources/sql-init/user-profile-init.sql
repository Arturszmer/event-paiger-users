INSERT INTO user_profile (
    creation_timestamp, modification_timestamp, user_address_id, event_organizer_id,
                        created_by, email, first_name, last_modified, last_name, password, username
) VALUES (
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, null, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
    'admin', 'admin@example.com', 'Admin', CURRENT_TIMESTAMP, 'Admin', 'password', 'admin'
    );
