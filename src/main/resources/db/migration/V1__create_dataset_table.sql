-- Table create kar rahe hain jisme json data aur dataset ka naam store hoga
CREATE TABLE dataset_records (
                                 id BIGSERIAL PRIMARY KEY,
                                 dataset_name VARCHAR(255) NOT NULL,

    -- JSONB type is the secret weapon here. Ye JSON ko simple text ki tarah nahi,
    -- balki optimized binary format mein store karta hai jisse querying bohot fast hoti hai.
                                 record_data JSONB NOT NULL,

                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Dataset name pe standard index bana rahe hain taaki specific dataset ko filter karna fast ho
CREATE INDEX idx_dataset_name ON dataset_records (dataset_name);

-- JSONB column pe GIN (Generalized Inverted Index) bana rahe hain.
-- Interviewer ko ye batana zaroori hai ki JSONB + GIN index se performance scale hoti hai.
CREATE INDEX idx_record_data ON dataset_records USING GIN (record_data);