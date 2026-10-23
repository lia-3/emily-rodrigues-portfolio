#!/bin/bash
# Reinicialización de la base de datos ejecutando el script SQL

# Datos de conexión a MySQL
MYSQL_USER="G14"
MYSQL_PASSWORD="G14@2025"
MYSQL_DB="grafana_db"

# Función para escapar cadenas (evitar problemas con apóstrofos)
escape_string() {
    echo "$1" | sed "s/'/\\'/g"
}

# Recopilación de datos de métricas

# CPU: se calcula el porcentaje de uso (100 - idle)
cpu_load=$(top -bn1 | grep "Cpu(s)" | sed "s/.*, *\([0-9.]*\)%* id.*/\1/" | awk '{print 100 - $1}')
cpu_load=${cpu_load:-0}

cpu_freq=$(cat /proc/cpuinfo | grep "cpu MHz" | head -n 1 | awk '{print $4}')
cpu_freq=${cpu_freq:-0}

# Memoria y Swap
ram_total=$(free -b | grep Mem | awk '{print $2}')
ram_total=${ram_total:-0}
ram_used=$(free -b | grep Mem | awk '{print $3}')
ram_used=${ram_used:-0}

swap_total=$(free -b | grep Swap | awk '{print $2}')
swap_total=${swap_total:-0}
swap_used=$(free -b | grep Swap | awk '{print $3}')
swap_used=${swap_used:-0}

# Disco
disk_total=$(df --total | grep total | awk '{print $2}')
disk_total=${disk_total:-0}
disk_used=$(df --total | grep total | awk '{print $3}')
disk_used=${disk_used:-0}

# I/O
io_stats=$(iostat -d 1 1 | tail -n 1)
io_reads=$(echo "$io_stats" | awk '{print $3}')
io_reads=${io_reads:-0}
io_writes=$(echo "$io_stats" | awk '{print $4}')
io_writes=${io_writes:-0}

# Red: obtener los contadores de bytes usando las estadísticas del sistema
net_bytes_sent=$(cat /sys/class/net/ens18/statistics/tx_bytes 2>/dev/null)
net_bytes_sent=${net_bytes_sent:-0}
net_bytes_recv=$(cat /sys/class/net/ens18/statistics/rx_bytes 2>/dev/null)
net_bytes_recv=${net_bytes_recv:-0}

active_connections=$(netstat -tn | wc -l)
active_connections=${active_connections:-0}

process_count=$(ps -e --no-headers | wc -l)
process_count=${process_count:-0}

# Inserción en system_info y obtención del ID en la misma conexión
system_info_id=$(mysql -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DB" -se "
INSERT INTO system_info (ram_total, swap_total, disk_total)
VALUES ($ram_total, $swap_total, $disk_total);
SELECT LAST_INSERT_ID();")
if [ -z "$system_info_id" ] || [ "$system_info_id" -eq 0 ]; then
    echo "No se obtuvo un ID válido de system_info" >&2
    exit 1
fi
echo "system_info_id: $system_info_id"

# Inserción en system_metric y obtención del ID en la misma conexión
timestamp=$(date +"%Y-%m-%d %H:%M:%S")
system_metric_id=$(mysql -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DB" -se "
INSERT INTO system_metric (system_info_id, timestamp, cpu_load, cpu_freq,
      ram_used, swap_used, disk_used, io_reads, io_writes, net_bytes_sent, net_bytes_recv,
      active_connections, process_count)
VALUES ($system_info_id, '$timestamp', $cpu_load, $cpu_freq, $ram_used, $swap_used,
        $disk_used, $io_reads, $io_writes, $net_bytes_sent, $net_bytes_recv,
        $active_connections, $process_count);
SELECT LAST_INSERT_ID();")
if [ -z "$system_metric_id" ] || [ "$system_metric_id" -eq 0 ]; then
    echo "No se obtuvo un ID válido de system_metric" >&2
    exit 1
fi
echo "system_metric_id: $system_metric_id"

# Inserción de procesos asociados al system_metric
ps aux --sort=-%cpu | awk 'NR>1 {print $2, $11, $3, $4}' | while read pid name cpu_percent mem_percent
do
    # Escapar el nombre del proceso para evitar problemas con las comillas
    escaped_name=$(escape_string "$name")
    mysql -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DB" -e "
    INSERT INTO process (pid, system_metric_id, name, cpu_percent, mem_percent)
    VALUES ($pid, $system_metric_id, '$escaped_name', $cpu_percent, $mem_percent);"
    if [ $? -ne 0 ]; then
        echo "Error insertando el proceso con PID: $pid" >&2
    fi
done

# Eliminar registros de procesos de mediciones anteriores, quedándonos
# sólo con los procesos de la medición actual (system_metric_id)
mysql -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DB" -e "
DELETE FROM process
WHERE system_metric_id <> $system_metric_id;
"

echo "Datos insertados exitosamente."

