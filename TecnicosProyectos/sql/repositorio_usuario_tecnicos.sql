CREATE TABLE repositorio_usuario_tecnicos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_tecnico VARCHAR(100) NOT NULL UNIQUE,
  nombre_completo VARCHAR(150) NOT NULL,
  password VARCHAR(255) NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ultimo_login DATETIME NULL
);
