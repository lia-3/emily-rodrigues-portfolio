import time
from db import get_connection, insert_system_info, insert_system_metric, insert_processes
from metrics import get_system_info, get_metrics, get_processes

INTERVAL_SECONDS = 60 

def main():
    conn = get_connection()
    print("Conectado a MariaDB")

    sys_info = get_system_info()
    system_info_id = insert_system_info(conn, **sys_info)

    print("Recogida de métricas iniciada. Pulsa Ctrl+C para detener.")

    try:
        while True:
            data = get_metrics()
            metric_id = insert_system_metric(conn, data, system_info_id)
            processes = get_processes(limit=15)
            insert_processes(conn, processes, metric_id)
            print(f"Métricas registradas - ID {metric_id}")
            time.sleep(INTERVAL_SECONDS)
    except KeyboardInterrupt:
        print("\nFinalizando...")
    finally:
        conn.close()

if __name__ == '__main__':
    main()

