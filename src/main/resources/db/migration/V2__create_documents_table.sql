CREATE TABLE documents(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    file_name VARCHAR(255) NOT NULL,
    file_size BIGINT,
    status VARCHAR(50) NOT NULL CHECK (status IN ('UPLOADED', 'PROCESSING', 'PROCESSED', 'FAILED')),
    uploaded_at TIMESTAMP NOT NULL DEFAULT now(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE


);