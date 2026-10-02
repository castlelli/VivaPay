CREATE TABLE Transacoes 
( 
 id_transacao SERIAL PRIMARY KEY,  
 valor INT,  
 tipo VARCHAR(1) NOT NULL,  
 criado_em DATE DEFAULT GETDATE(),  
 id_cliente_evento INT NOT NULL,  
 id_vendedor INT NOT NULL,  
); 

CREATE TABLE Clientes 
( 
 id_cliente SERIAL PRIMARY KEY,  
 voucher VARCHAR(255),
 id_usuario INT NOT NULL,  
 criado_em DATE DEFAULT GETDATE()
); 

CREATE TABLE Usuarios 
( 
 id_usuario SERIAL PRIMARY KEY, 
 id_instituicao INT NOT NULL,   
 login VARCHAR(255) NOT NULL,  
 senha VARCHAR(255) NOT NULL,  
 criado_em DATE NOT NULL DEFAULT GETDATE(),  
 nome_usuario VARCHAR(255) NOT NULL,  
 unique(login)
); 

CREATE TABLE Vendedores 
( 
    id_vendedor SERIAL PRIMARY KEY,  
    loja VARCHAR(255) NOT NULL,
    id_usuario INT NOT NULL,
    criado_por INT NOT NULL,
    cancelado_por INT,
    criado_em DATE NOT NULL DEFAULT GETDATE(),
    cancelado_em DATE 
); 

CREATE TABLE Instituicoes
{
    id_instituicao SERIAL PRIMARY KEY,
    nome_instituicao VARCHAR(255) NOT NULL,
    abreviacao_instituicao VARCHAR(255),
    cidade_estado VARCHAR(255),
    cnpj VARCHAR(255),
    criado_em DATE NOT NULL DEFAULT GETDATE()
    cancelado_em DATE 
}

CREATE TABLE Eventos
{
    id_evento SERIAL PRIMARY KEY,
    id_instituicao INT NOT NULL,
    criado_por INT NOT NULL,
    cancelado_por INT,
    nome_evento VARCHAR(255) NOT NULL,
    abreviacao_evento VARCHAR(255),
    hora_inicio DATE NOT NULL,
    hora_final DATE NOT NULL,
    criado_em DATE NOT NULL DEFAULT GETDATE(),
    cancelado_em DATE,
    finalizado_em DATE
}

CREATE TABLE ClienteEvento
{
    id_cliente_evento SERIAL PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_evento INT NOT NULL,
    criado_em DATE NOT NULL DEFAULT GETDATE()
}

CREATE TABLE VendedorEvento
{
    id_vendedor_evento SERIAL PRIMARY KEY,
    id_vendedor INT NOT NULL,
    id_evento INT NOT NULL,
    criado_por INT NOT NULL,
    cancelado_por INT,
    criado_em DATE NOT NULL DEFAULT GETDATE()
}
CREATE TABLE Administrador
{
    id_administrador SERIAL PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_instituicao INT NOT NULL,
    criado_em DATE NOT NULL,
    cancelado_em DATE
}

ALTER TABLE Transacoes ADD FOREIGN KEY (id_cliente_evento) REFERENCES ClientesEvento (id_cliente_evento)
ALTER TABLE Transacoes ADD FOREIGN KEY (id_vendedor) REFERENCES Vendedores (id_vendedor)
ALTER TABLE Clientes ADD FOREIGN KEY (id_usuario) REFERENCES Usuarios (id_usuario)
ALTER TABLE Usuarios ADD FOREIGN KEY (id_instituicao) REFERENCES Instituicoes (id_instituicao)
ALTER TABLE Vendedores ADD FOREIGN KEY (criado_por) REFERENCES Administrador(id_administrador)
ALTER TABLE Vendedores ADD FOREIGN KEY (cancelado_por) REFERENCES Administrador(id_administrador)
ALTER TABLE Eventos ADD FOREIGN KEY (id_instituicao) REFERENCES Instituicoes (id_instituicao)
ALTER TABLE Eventos ADD FOREIGN KEY (criado_por) REFERENCES Administrador(id_administrador)
ALTER TABLE Eventos ADD FOREIGN KEY (cancelado_por) REFERENCES Administrador(id_administrador)
ALTER TABLE ClienteEvento ADD FOREIGN KEY (id_cliente) REFERENCES Clientes (id_cliente)
ALTER TABLE ClienteEvento ADD FOREIGN KEY (id_evento) REFERENCES Eventos (id_evento)
ALTER TABLE VendedorEvento ADD FOREIGN KEY (id_evento) REFERENCES Eventos (id_evento)
ALTER TABLE VendedorEvento ADD FOREIGN KEY (id_vendedor) REFERENCES Vendedores (id_vendedor)
ALTER TABLE VendedorEvento ADD FOREIGN KEY (criado_por) REFERENCES Administrador(id_administrador)
ALTER TABLE VendedorEvento ADD FOREIGN KEY (cancelado_por) REFERENCES Administrador(id_administrador)
ALTER TABLE Administrador ADD FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
ALTER TABLE Instituicoes ADD FOREIGN KEY (id_instituicao) REFERENCES Instituicoes(id_instituicao)