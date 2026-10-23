import psutil
import shutil
import socket

def get_system_info():
    return {
        'ram_total': psutil.virtual_memory().total,
        'swap_total': psutil.swap_memory().total,
        'disk_total': shutil.disk_usage('/').total
    }

def get_metrics():
    net = psutil.net_io_counters()
    io = psutil.disk_io_counters()
    connections = len(psutil.net_connections(kind='inet'))

    return {
        'cpu_load': psutil.getloadavg()[0],  # 1 min avg
        'cpu_freq': psutil.cpu_freq().current,
        'ram_used': psutil.virtual_memory().used,
        'swap_used': psutil.swap_memory().used,
        'disk_used': shutil.disk_usage('/').used,
        'io_reads': io.read_count,
        'io_writes': io.write_count,
        'net_sent': net.bytes_sent,
        'net_recv': net.bytes_recv,
        'connections': connections,
        'proc_count': len(psutil.pids())
    }

def get_processes(limit=10):
    processes = []
    for proc in psutil.process_iter(['pid', 'name', 'cpu_percent', 'memory_percent']):
        try:
            info = proc.info
            processes.append({
                'pid': info['pid'],
                'name': info['name'][:255],
                'cpu': info['cpu_percent'],
                'mem': info['memory_percent']
            })
        except (psutil.NoSuchProcess, psutil.AccessDenied):
            continue
    # Ordenamos por CPU y cogemos los más activos
    return sorted(processes, key=lambda p: p['cpu'], reverse=True)[:limit]

