--Criação do banco de dados
CREATE DATABASE senior
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;
	
--Instalação do módulo uuid-ossp caso não exista
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
 
--Criação das tabelas
CREATE TABLE public.produtos_servicos (
    id uuid DEFAULT uuid_generate_v4 (),
    nome CHARACTER VARYING(255) NOT NULL,
    tipo INTEGER NOT NULL,
    valor DOUBLE PRECISION NOT NULL,
	ativo BOOLEAN NOT NULL,
    PRIMARY KEY (id)
);
    
CREATE TABLE public.pedidos (
    id uuid DEFAULT uuid_generate_v4 (),
    percentual_desconto DOUBLE PRECISION NULL,
    valor DOUBLE PRECISION NOT NULL,
	situacao INTEGER NOT NULL,
	entrada TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE public.pedidos_produtos_servicos (
    id uuid DEFAULT uuid_generate_v4 (),
    i_pedidos uuid NOT NULL,
	i_produtos_servicos uuid NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.pedidos_produtos_servicos ADD CONSTRAINT fk_pedidos_produtos_servicos_pedidos FOREIGN KEY (i_pedidos) REFERENCES public.pedidos(id);
ALTER TABLE public.pedidos_produtos_servicos ADD CONSTRAINT fk_pedidos_produtos_servicos_produtos_servicos FOREIGN KEY (i_produtos_servicos) REFERENCES public.produtos_servicos(id);