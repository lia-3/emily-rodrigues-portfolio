import mysql.connector
from config import DB_CONFIG

def get_connection():
    return mysql.connector.connect(**DB_CONFIG)

def insert_system_info(conn, ram_total, swap_total, disk_total):
    cursor = conn.cursor()
    cursor.execute("""
        INSERT INTO system_info (ram_total, swap_total, disk_total)
        VALUES (%s, %s, %s)
    """, (ram_total, swap_total, disk_total))
    conn.commit()
    return cursor.lastrowid

def insert_system_metric(conn, data, system_info_id):
    cursor = conn.cursor()
    cursor.execute("""
        INSERT INTO system_metric (
            system_info_id, cpu_load, cpu_freq, ram_used, swap_used,
            disk_used, io_reads, io_writes, net_bytes_sent,
            net_bytes_recv, active_connections, process_count
        ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    """, (
        system_info_id,
        data['cpu_load'], data['cpu_freq'],
        data['ram_used'], data['swap_used'],
        data['disk_used'], data['io_reads'], data['io_writes'],
        data['net_sent'], data['net_recv'],
        data['connections'], data['proc_count']
    ))
    conn.commit()
    return cursor.lastrowid

def insert_processes(conn, processes, system_metric_id):
    cursor = conn.cursor()
    for p in processes:
        cursor.execute("""
            INSERT INTO process (pid, system_metric_id, name, cpu_percent, mem_percent)
            VALUES (%s, %s, %s, %s, %s)
        """, (p['pid'], system_metric_id, p['name'], p['cpu'], p['mem']))
    conn.commit()

