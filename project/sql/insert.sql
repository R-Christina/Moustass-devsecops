-- insert created admin
-- id admin : 2

insert into User (
    user_name, 
    email, 
    password_hash, 
    post, 
    public_key, 
    is_admin, 
    force_change, 
    created_at, 
    updated_at
)values(
    'admin',
    'admin@gmail.com',
    '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',
    'admin',
    '-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArjT9in5I/ZkIa1Zwm3XkL4/HpJJIxw1MHWoAH1xAmluj/vTDpsfDCi7E1s1Q87R1hq0SqQJ+8NAy86eHTU5h97DFfDHH/IYNAldNgz7UC/oe/0O2nPEhlh77xG3flvrWTEuyN4yIS07FW9I/1uOYMEAOLarx429t3jWeWE1yhyfiQbIQJUpSd9s9QEJHr7LcSniL/feu8Qjuxt4dnUh07Ri6XxcxSazW8jEfE8bzttWm+75gAjvqHC4jQjFXF/FEOKjdXXzGkCe+hj+3eRDoR+kH/KSbb2ZwPpSKvwKf/4WwirnNAYXI8ZlsaZP7dPS8XGei47Bke+A2LErkUXNpWQIDAQAB\n-----END PUBLIC KEY-----',
    0,
    0,
    NOW(),
    NOW()
);



