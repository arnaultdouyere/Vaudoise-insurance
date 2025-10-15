CREATE TYPE client_type AS ENUM ('PERSON', 'COMPANY');

CREATE TABLE clients (
  id BIGSERIAL PRIMARY KEY,
  type client_type NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  phone VARCHAR(32) NOT NULL,
  birth_date DATE,
  company_identifier VARCHAR(64),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT email_format CHECK (position('@' in email) > 1),
  CONSTRAINT birth_or_company CHECK (
    (type='PERSON' AND birth_date IS NOT NULL AND company_identifier IS NULL)
    OR
    (type='COMPANY' AND company_identifier IS NOT NULL AND birth_date IS NULL)
  )
);

CREATE TABLE contracts (
  id BIGSERIAL PRIMARY KEY,
  client_id BIGINT NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
  start_date DATE NOT NULL,
  end_date DATE NULL,
  cost_amount NUMERIC(12,2) NOT NULL CHECK (cost_amount >= 0),
  update_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

-- Indexes for performance (sum endpoint + filtering)
CREATE INDEX idx_contract_client_active ON contracts(client_id, end_date);
CREATE INDEX idx_contract_update_date ON contracts(update_date);