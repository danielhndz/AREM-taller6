# :hammer_and_wrench: Log Service

## Arquitecturas Empresariales

### :pushpin: Daniel Felipe Hernández Mancipe

<br/>

[LogService](/log-service/src/main/java/edu/escuelaing/arem/App.java) es un servicio REST que recibe una cadena, la almacena en la base de datos y responde en un objeto JSON con las 10 ultimas cadenas almacenadas en la base de datos y la fecha en que fueron almacenadas.

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
cd <project-folder>/log-service
mvn clean install
```

Si salió bien, debería tener una salida similar a esta:

![compile output](../../media/log_service_mvn_compile.png?raw=true)

El ejemplo muestra el caso de `compile` pero la salida final para `install` debe ser la misma, un `BUILD SUCCESS` en verde.

### Using

Debe estar en la carpeta raíz del proyecto para ejecutarlo correctamente.

```bash
mvn exec:java -Dexec.mainClass="edu.escuelaing.arem.App"
```

![output for first use](../../media/log_service_using1.png?raw=true)

## Built With

- [Maven](https://maven.apache.org/) - Dependency Management
- [Git](https://git-scm.com/) - Version Management
- [JUnit4](https://junit.org/junit4/) - Unit testing framework for Java

## Design Metaphor

- Autor: Daniel Hernández
- Última modificación: 17/03/2023

### Backend Class Diagram

- [Diagrama de paquetes](/log-service/src/main/java/edu/escuelaing/arem/)

![Diagrama de paquetes con clases](../../media/log_service_pkgs_simple.png?raw=true)

Los nombres de los paquetes intentan ser representativos en términos de la funcionalidad que está implementada en dicho paquete.

- La clase [App](/log-service/src/main/java/edu/escuelaing/arem/App.java) modela la lógica y arranca el servicio.

- La clase [MongoConnection](/log-service/src/main/java/edu/escuelaing/arem/persistence/MongoConnection.java) modela la conexión a la base de datos Mongo.

## Authors

- **Daniel Hernández** - _Initial work_ - [danielhndz](https://github.com/danielhndz)

## License

This project is licensed under the GPLv3 License - see the [LICENSE.md](LICENSE.md) file for details

## Javadoc

Para generar Javadocs independientes para el proyecto en la carpeta `/target/site/apidocs` ejecute:

```bash
mvn javadoc:javadoc
```
