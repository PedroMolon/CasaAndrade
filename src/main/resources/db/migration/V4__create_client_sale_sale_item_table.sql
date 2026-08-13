CREATE TABLE tb_client (
    id BIGSERIAL PRIMARY KEY,
    persons_type VARCHAR(10) NOT NULL,
    name VARCHAR(150) NOT NULL,
    document VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(150),
    phone VARCHAR(20),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE tb_sale(
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL REFERENCES tb_client(id),
    seller_id BIGINT NOT NULL REFERENCES tb_user(id),
    total NUMERIC(10, 2) NOT NULL,
    sale_date TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE tb_sale_item(
    id BIGSERIAL PRIMARY KEY,
    sale_id BIGINT NOT NULL REFERENCES tb_sale(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES tb_product(id),
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(10, 2) NOT NULL,
    subtotal NUMERIC(10, 2) NOT NULL
);