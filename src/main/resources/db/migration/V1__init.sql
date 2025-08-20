-- src/main/resources/db/migration/V1__init.sql
CREATE TABLE relatorio (
                           id BIGSERIAL PRIMARY KEY,
                           data DATE NOT NULL,
                           hora TIME NOT NULL,
                           motorista VARCHAR(100) NOT NULL,
                           placa_veiculo VARCHAR(10) NOT NULL,
                           valor DECIMAL(10,2) NOT NULL
);
