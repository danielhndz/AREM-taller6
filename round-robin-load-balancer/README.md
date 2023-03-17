# :hammer_and_wrench: Round-Robin Load Balancer

## Arquitecturas Empresariales

### :pushpin: Daniel Felipe Hernández Mancipe

<br/>

La aplicación web [Round-Robin Load Balancer](/round-robin-load-balancer/src/main/java/edu/escuelaing/arem/App.java) está compuesta por un cliente web y al menos un servicio REST. El cliente web tiene un campo y un botón y cada vez que el usuario envía un mensaje, este se lo envía al servicio REST y actualiza la pantalla con la información que este le regresa en formato JSON. El servicio REST recibe la cadena e implementa un algoritmo de balanceo de cargas de Round Robin, delegando el procesamiento del mensaje y el retorno de la respuesta a cada una de las tres instancias del servicio [LogService](/log-service/src/main/java/edu/escuelaing/arem/App.java).

## Getting Started

### Prerequisites

- Java >= 11.x
- Maven >= 3.x
- Git >= 2.x
- JUnit 4.x

### Installing

Simplemente clone el repositorio:

```bash
git clone https://github.com/danielhndz/AREM-taller5.git
```

Luego compile el proyecto con maven:

```bash
cd <project-folder>/round-robin-load-balancer
mvn clean install
```

Si salió bien, debería tener una salida similar a esta:

![compile output](../../media/load_balancer_mvn_compile.png?raw=true)

El ejemplo muestra el caso de `compile` pero la salida final para `install` debe ser la misma, un `BUILD SUCCESS` en verde.

### Using

Debe estar en la carpeta raíz del proyecto para ejecutarlo correctamente.

```bash
mvn exec:java -Dexec.mainClass="edu.escuelaing.arem.App"
```

![output for first use](../../media/load_balancer_using1.png?raw=true)

## Built With

- [Maven](https://maven.apache.org/) - Dependency Management
- [Git](https://git-scm.com/) - Version Management
- [JUnit4](https://junit.org/junit4/) - Unit testing framework for Java

## Design Metaphor

- Autor: Daniel Hernández
- Última modificación: 17/03/2023

### Backend Class Diagram

- [Diagrama de paquetes](/round-robin-load-balancer/src/main/java/edu/escuelaing/arem/)

Los nombres de los paquetes intentan ser representativos en términos de la funcionalidad que está implementada en dicho paquete.

![Diagrama de paquetes con clases](../../media/load_balancer_pkgs_simple.png?raw=true)

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
