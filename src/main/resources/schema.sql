-- =============================================
-- Script de creación de la base de datos
-- Gestión de Usuarios - Arquitectura Hexagonal
-- =============================================

CREATE DATABASE IF NOT EXISTS crud_usuarios
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE crud_usuarios;

CREATE TABLE IF NOT EXISTS users (
    id          VARCHAR(36)  NOT NULL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        ENUM('ADMIN', 'MEMBER', 'REVIEWER') NOT NULL,
    status      ENUM('ACTIVE', 'INACTIVE', 'PENDING', 'BLOCKED') NOT NULL DEFAULT 'PENDING',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Usuario administrador inicial (password: Admin1234!)
INSERT INTO users (id, name, email, password, role, status)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'Administrador',
    'admin@example.com',
    '$2a$12$sfwkajsls.fktQb0Ngw5cOiSiV3z4LIwbucSAy4sw2wyNlxbUBt4q',
    'ADMIN',
    'ACTIVE'
);

CREATE TABLE IF NOT EXISTS reservas_hotel (
    id                VARCHAR(36)  NOT NULL PRIMARY KEY,
    fecha             VARCHAR(20)  NOT NULL,
    hotel             VARCHAR(150) NOT NULL,
    huesped           VARCHAR(100) NOT NULL,
    fecha_inicio      VARCHAR(20)  NOT NULL,
    fecha_fin         VARCHAR(20)  NOT NULL,
    valor             DOUBLE       NOT NULL,
    habitacion        VARCHAR(20)  NOT NULL,
    num_acompanantes  INT          NOT NULL,
    pais              VARCHAR(100) NOT NULL,
    departamento      VARCHAR(100) NOT NULL,
    ciudad            VARCHAR(100) NOT NULL,
    hora_checkin      VARCHAR(10)  NOT NULL,
    hora_checkout     VARCHAR(10)  NOT NULL,
    empleado_atiende  VARCHAR(100) NOT NULL,
    empleado_despide  VARCHAR(100) NOT NULL,
    descripcion       TEXT         NULL,
    status            ENUM('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED') NOT NULL DEFAULT 'PENDING',
    created_at        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

