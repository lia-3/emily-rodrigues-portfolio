# test-pdf-csv

## Pasos
1. generar pdf con las preguntas y respuestas, en negrita y cursiva la respuesta correcta
    a. Las preguntas van enumeradas 1..n
    b. las respuestas del a..d
2. generar csv con las respuesta correcta de la siguiente manera
        pregunta,respuesta
        1,b
        2,d
4. source test_env/bin/activate para entrar en el entorno
5. python3 pdf_tester.py para poner en marcha el programa (ya él se encarga de pedirte los ficheros)
6. deactivate para salir del entorno

## Notas
* si llega a fallar el paso 4 hacer
    * eliminar el test_env
    * ejecutar python3 -m venv test_env
    * ejecutar 
        * source test_env/bin/activate (linux)
        * source test_env/Scripts/activate (w11)
    * ejecutar pip install pymupdf