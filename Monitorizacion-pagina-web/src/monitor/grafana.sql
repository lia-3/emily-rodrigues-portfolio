-- Borrar tablas si ya existen
DROP TABLE IF EXISTS process;
DROP TABLE IF EXISTS system_metric;
DROP TABLE IF EXISTS system_info;

-- Crear tabla system_info
CREATE TABLE system_info (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ram_total BIGINT NOT NULL,
    swap_total BIGINT NOT NULL,
    disk_total BIGINT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Crear tabla system_metric
CREATE TABLE system_metric (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    system_info_id INT NOT NULL,
    timestamp DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    cpu_load DOUBLE NOT NULL,
    cpu_freq DOUBLE NOT NULL,
    ram_used BIGINT NOT NULL,
    swap_used BIGINT NOT NULL,
    disk_used BIGINT NOT NULL,
    io_reads BIGINT NOT NULL,
    io_writes BIGINT NOT NULL,
    net_bytes_sent BIGINT NOT NULL,
    net_bytes_recv BIGINT NOT NULL,
    active_connections INT NOT NULL,
    process_count INT NOT NULL,
    CONSTRAINT fk_system_metric_system_info
        FOREIGN KEY (system_info_id)
        REFERENCES system_info(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Crear tabla process
CREATE TABLE process (
    pid INT NOT NULL,
    system_metric_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    cpu_percent DOUBLE NOT NULL,
    mem_percent DOUBLE NOT NULL,
    PRIMARY KEY (pid, system_metric_id),
    CONSTRAINT fk_process_system_metric 
        FOREIGN KEY (system_metric_id) 
        REFERENCES system_metric(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Índices para rendimiento
CREATE INDEX idx_system_metric_timestamp ON system_metric(timestamp);
CREATE INDEX idx_system_metric_system_info_id ON system_metric(system_info_id);
CREATE INDEX idx_process_system_metric_id ON process(system_metric_id);

