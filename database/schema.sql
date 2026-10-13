CREATE DATABASE IF NOT EXISTS medisoft
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE medisoft;

CREATE TABLE IF NOT EXISTS citas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    documento_paciente VARCHAR(30) NOT NULL,
    nombre_paciente VARCHAR(120) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    medico VARCHAR(120) NOT NULL,
    fecha_hora DATETIME NOT NULL,
    estado ENUM('PROGRAMADA', 'ATENDIDA', 'CANCELADA') NOT NULL DEFAULT 'PROGRAMADA',
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_citas_documento (documento_paciente),
    INDEX idx_citas_fecha_hora (fecha_hora)
);
