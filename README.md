# Consigna
Existe un call center donde hay 3 tipos de empleados: operador, supervisor y director. El proceso de la atención de una llamada telefónica en primera instancia debe ser atendida por un operador, si no hay ninguno libre debe ser atendida por un supervisor, y de no haber tampoco supervisores libres debe ser atendida por un director.

# Requerimientos
1.- Debe existir una clase Dispatcher encargada de manejar las llamadas, y debe contener el método dispatchCall para que las asigne a los empleados disponibles.
2.- El método dispatchCall puede invocarse por varios hilos al mismo tiempo.
3.- La clase Dispatcher debe tener la capacidad de poder procesar 10 llamadas al mismo tiempo (de modo concurrente).
4.- Cada llamada puede durar un tiempo aleatorio entre 5 y 10 segundos.
5.- Debe tener un test unitario donde lleguen 10 llamadas.

# Extras/Plus
1.- Dar alguna solución sobre qué pasa con una llamada cuando no hay ningún empleado libre.
2.- Dar alguna solución sobre qué pasa con una llamada cuando entran más de 10 llamadas concurrentes.
3.- Agregar los tests unitarios que se crean convenientes.
4.- Agregar documentación de código.

# Resolución
Se creó la clase CallDispacherService (Para la logica) y CallDispacherController (Para disponibilizarlo como API),  CallDispacherService recibe llamadas y lo agrega a una cola de tareas para que la llamada sea atendida, va agregando al pool de threads cada llamada, los cuales asincrónicamente va asignando los empleados disponibles quienes van a atender dichos llamados. Cuando un empleado termina de atender una llamada vuelve quedar disponible y la llamada se marca como finalizada.

Para saber si tu llamada a sido atendida o finalizada puedes usar la url http://localhost:8080/call/{idCall}

Para la cola de llamadas se utiliza java.util.concurrent.LinkedBlockingQueue.LinkedBlockingQueue que es una implementación de BlockingQueue y se caracteriza por mantener el hilo a la espera cuando se quiere sacar un elemento de una cola vacía.

Para el control de los empleados y llamadas uso los collections del paquete concurrent que permite el acceso simuktanea a los datos de dichas listas o mapas desde distintos subprocesos.

Los puntos extras los solucione creando un poll Thread que controle el maximo de hilos simultaneos que corren (No necesariamente mas hilos en paralelo significa mejor performance), tambien dentro de cada subproceso cree, un ciclo de reintentos para cuando una llamada entra y no hay empleados disponibles, tiene un break para que no quede en un loop infinito.
