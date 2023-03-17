# :hammer_and_wrench: Patrones Arquitecturales

## Arquitecturas Empresariales

### :pushpin: Daniel Felipe Hernández Mancipe

<br/>

En este proyecto se profundizan los conceptos de modulación por medio de virtualización usando Docker y AWS. Se construye una aplicación con la arquitectura propuesta y se despliega en AWS usando EC2, Docker, **ELB y autoescalado**.

![statement](../media/statement.png?raw=true)

1. El servicio MongoDB es una instancia de MongoDB corriendo en un container de docker en una máquina virtual de EC2.
2. [LogService](/log-service/src/main/java/edu/escuelaing/arem/Launcher.java) es un servicio REST que recibe una cadena, la almacena en la base de datos y responde en un objeto JSON con las 10 ultimas cadenas almacenadas en la base de datos y la fecha en que fueron almacenadas.
3. La aplicación web [APP-LB-RoundRobin](/round-robin-load-balancer/src/main/java/edu/escuelaing/arem/Launcher.java.java) está compuesta por un cliente web y al menos un servicio REST. El cliente web tiene un campo y un botón y cada vez que el usuario envía un mensaje, este se lo envía al servicio REST y actualiza la pantalla con la información que este le regresa en formato JSON. El servicio REST recibe la cadena e implementa un algoritmo de balanceo de cargas de Round Robin, delegando el procesamiento del mensaje y el retorno de la respuesta a cada una de las tres instancias del servicio [LogService](/log-service/src/main/java/edu/escuelaing/arem/Launcher.java).

## Getting Started

### Prerequisites

- Java >= 11.x
- Maven >= 3.x
- Git >= 2.x
- JUnit 4.x

### Amazon Web Services - Parte 1

Primero se crea una instancia EC2:

![instancia EC2](../media/aws1.png?raw=true)

Luego, se crean un par de llaves de seguridad:

![instancia EC2 keys 1](../media/aws2.png?raw=true)

![instancia EC2 keys 2](../media/aws3.png?raw=true)

Luego, se crea un subgrupo de seguridad:

![instancia EC2 subgrupo](../media/aws4.png?raw=true)

Si la máquina se creó correctamente:

![instancia EC2 correcta](../media/aws5.png?raw=true)

Los datos que nos interesan de la instancia son:

![instancia EC2 correcta](../media/aws6.png?raw=true)

Se ajusta la clase [MongoConnection](/log-service/src/main/java/edu/escuelaing/arem/persistence/MongoConnection.java) y el [LoadBalancer](/round-robin- load-balancer/src/main/java/edu/escuelaing/arem/App.java):

```java
private static final String URL_STRING = "ec2-100-24-59-5.compute-1.amazonaws.com";
```

### Installing

Simplemente clone el repositorio:

```bash
git clone https://github.com/danielhndz/AREM-taller5.git
```

Luego compile los dos subproyectos con maven, `Log Service` y `Round-Robin Load Balancer`:

```bash
cd <project-folder>/<subproject-folder>
mvn clean install
```

![compile output](../media/log_service_mvn_compile.png?raw=true)

![compile output](../media/load_balancer_mvn_compile.png?raw=true)

Los ejemplos se muestran para el caso de `compile` pero la salida final para `install` debe ser la misma, un `BUILD SUCCESS` en verde.

### Using

Primero se construye toda la arquitectura de servicios mediante una configuración Docker con el comando `docker-compose`:

```bash
sudo docker-compose up -d --build --scale web=3
```

![docker-compose 1](../media/docker1.png?raw=true)

![docker-compose 2](../media/docker2.png?raw=true)

Se verifican las imágenes:

![docker imgs](../media/docker3.png?raw=true)

Para subir las imágenes a DockerHub, en el motor de Docker local, se crea una referencia a la imagen con el nombre del repositorio a donde se desea subir:

![docker web](../media/docker4.png?raw=true)

![docker balancer](../media/docker5.png?raw=true)

Luego se suben la imágenes:

![dockerhub 1](../media/docker6.png?raw=true)

![dockerhub 2](../media/docker7.png?raw=true)

### Amazon Web Services - Parte 2

Se accede a la instancia EC2 por `ssh`:

```bash
ssh -i "AREM-lab5.pem" ec2-user@ec2-100-24-59-5.compute-1.amazonaws.com
```

![instancia EC2 ssh](../media/aws7.png?raw=true)

Se instala `Docker`:

```bash
sudo yum install docker
```

Se configura el servicio y se vuelve a conectar a la instancia:

![instancia EC2 setup](../media/aws8.png?raw=true)

Se descarga la imagen de Docker de Mongo:

![aws img mongo](../media/aws9.png?raw=true)

Se crea el docker para la imagen de Mongo:

![aws docker mongo](../media/aws10.png?raw=true)

Una vez el docker con la instancia de la base de datos MongoDB esté ejecutando, entramos al cliente Mongo para crear un registro y habilitar el acceso remoto

![aws mongo setup 1](../media/aws10.1.png?raw=true)

![aws mongo setup 2](../media/aws10.2.png?raw=true)

![aws mongo setup 3](../media/aws10.3.png?raw=true)

Se despliegan el `Log Service` (3 instancias) y el `Round-Robin Load Balancer` cada uno en su contenedor Docker respectivo:

![aws services setup](../media/aws11.png?raw=true)

Se verifica que los contenedores estén corriendo:

![aws docker ps](../media/aws12.png?raw=true)

Se abren los puertos para acceder a los servicios:

![aws ports](../media/aws13.png?raw=true)

Finalmente, se puede acceder al servidor desde [AWS](http://ec2-100-24-59-5.compute-1.amazonaws.com:9000/):

![instancia EC2 using 1](../media/aws14.png?raw=true)

Al entrar podemos ver que hay un log en la base de datos, así que vamos a insertar logs hasta llegar a la inserción del log numero 11, así se vería la tabla de logs:

![instancia EC2 using 2](../media/aws15.png?raw=true)

![instancia EC2 using 3](../media/aws16.png?raw=true)

![instancia EC2 using 4](../media/aws17.png?raw=true)

Ahora al insertar el log 11 se verá que éste será agregado al final de la tabla y el primer log desparecera, ya que los `Log Service` están programados solo para retornar los 10 últimos logs registrados en la base de datos, como se ve a continuación:

![instancia EC2 using 5](../media/aws18.png?raw=true)

![instancia EC2 using 6](../media/aws19.png?raw=true)

Podemos tambien acceder a un `Log Service` con la IP de la instancia EC2 y el puerto 8001 que corresponde al puerto del `Log Service 1` y vemos que este lo que le retorna al balanceador es un archivo json con string separados por comas que corresponden a los menajes y fechas de los 10 ultimos log registrados en la base de datos:

![instancia EC2 using 7](../media/aws20.png?raw=true)

## Built With

- [Maven](https://maven.apache.org/) - Dependency Management
- [Git](https://git-scm.com/) - Version Management
- [JUnit4](https://junit.org/junit4/) - Unit testing framework for Java

## Design Metaphor

- Autor: Daniel Hernández
- Última modificación: 17/03/2023

### Backend Class Diagram

- [Log Service](/log-service/src/main/java/edu/escuelaing/arem/)

![Diagrama de paquetes con clases](../media/log_service_pkgs_simple.png?raw=true)

Los nombres de los paquetes intentan ser representativos en términos de la funcionalidad que está implementada en dicho paquete.

- La clase [App](/log-service/src/main/java/edu/escuelaing/arem/App.java) modela la lógica y arranca el servicio.

- La clase [MongoConnection](/log-service/src/main/java/edu/escuelaing/arem/persistence/MongoConnection.java) modela la conexión a la base de datos Mongo.

- [Round Robin Load Balancer](/round-robin-load-balancer/src/main/java/edu/escuelaing/arem/)

Los nombres de los paquetes intentan ser representativos en términos de la funcionalidad que está implementada en dicho paquete.

![Diagrama de paquetes con clases](../media/load_balancer_pkgs_simple.png?raw=true)

- La clase [App](/log-service/src/main/java/edu/escuelaing/arem/App.java) modela la lógica y arranca el servicio.

## Authors

- **Daniel Hernández** - _Initial work_ - [danielhndz](https://github.com/danielhndz)

## License

This project is licensed under the GPLv3 License - see the [LICENSE.md](LICENSE.md) file for details

## Javadoc

Para generar Javadocs independientes para el proyecto en la carpeta `/target/site/apidocs` ejecute:

```bash
mvn javadoc:javadoc
```
