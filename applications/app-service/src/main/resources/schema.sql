-- Borramos las tablas si ya existen para un inicio limpio
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

-- Creamos la tabla de Roles primero porque 'users' depende de ella
CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL UNIQUE
);

-- Creamos la tabla de Usuarios
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       nombres VARCHAR(255) NOT NULL,
                       apellidos VARCHAR(255) NOT NULL,
                       fecha_nacimiento DATE NOT NULL,
                       direccion VARCHAR(255),
                       telefono VARCHAR(20),
                       correo_electronico VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       salario_base NUMERIC(12, 2) NOT NULL,
                       role_id BIGINT NOT NULL,
                       CONSTRAINT fk_role FOREIGN KEY(role_id) REFERENCES roles(id)
);

-- (Opcional pero recomendado) Insertamos los roles básicos
INSERT INTO roles (name) VALUES ('SOLICITANTE'), ('ADMINISTRADOR');