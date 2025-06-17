# Proyecto Base Implementando Clean Architecture 
# ms-nequi-franchises-hub
Este microservicio es encargado de orquestar todo el nucleo para las franquicias, de acuerdo a los requerimientos estipulados en la prueba técnica. 

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por último el inicio y configuración de la aplicación.

Lee el artículo [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de entities.
Los casos de uso son:

| Significado en el Negocio                                                                                                                                  |
|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Franquicias `: Una franquicia puede tener una o muchas sucursales. En el proyecto son las Franchises.                                                     |
| `Sucursales`: Una sucursal pertenece a una franquicia y puede tener uno o muchos productos para su gestión de inventario. En el proyecto son los Branches. |
| `Productos`: Un producto pertenece a una franquicia, ya que los productos se crean por franquicia. En el proyecto son los products.                        |

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

## Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos módulos, resolver las dependencias y crear los beans de los casos de use (UseCases) de forma automática, inyectando en éstos instancias concretas de las dependencias declaradas. Además inicia la aplicación (es el único módulo del proyecto donde encontraremos la función “public static void main(String[] args)”.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**

## 📝 REQUISITOS PREVIOS

### 🎯 Gradle 8.14.2 
### 🎯 Java JDK 21
### 🎯 Docker Desktop 4.41.2


## 💯 Pasos para ejecutar el proyecto localmente

### ✅ PASO 1: Clona el repositorio

```bash

git clone https://github.com/CRodriguez2017/ms-nequi-franchises-hub.git
cd ms-nequi-franchises-hub
```

### ✅ PASO 2: Crea y levanta la base de datos con Docker

Asegúrate de tener Docker instalado. <br>
Luego ejecuta el siguiente comando para iniciar un contenedor de MySQL:
```bash

docker run --name nequi-franchises-hub-rds \
  -e MYSQL_ROOT_PASSWORD=N3qu1.@1234 \
  -v /var/lib/mysql \
  -p 3306:3306 \
  -d mysql
```

Esto levanta una instancia de MySQL accesible en localhost:3306.

#### ⚙️ Ejecuta el script de creación de la base de datos <br>

Una vez el contenedor esté corriendo, correr el comando ubicandose sobre la raiz del proyecto, ejecuta el script `nequi_franchises_hub_db.sql` para crear el schema `nequi_franchises_hub_db` y sus respectivas tablas:

```bash

docker exec -i nequi-franchises-hub-rds \
mysql -u root -pN3qu1.@1234 < ./nequi_franchises_hub_db.sql
```

Otra opción es abrir el archivo nequi_franchises_hub_db.sql y ejecutarlo directamente en un IDE que permita establecer conexión a la base datos sobre la instancia que se creo en el docker.  
---

### ✅ PASO 3: Variables de entorno

Puedes usar variables en tiempo de ejecución o definir un archivo `.env`. Las propiedades necesarias están en `application.yml` y usan placeholders con valores por defecto:

```yaml
spring:
  datasource:
    url: ${MYSQL_URL:jdbc:mysql://localhost:3306/nequi_franchises_hub_db?allowPublicKeyRetrieval=true&useSSL=false}
    username: ${MYSQL_USERNAME:root}
    password: ${MYSQL_PASSWORD:N3qu1.@1234}
```

#### Si deseas sobrescribirlas:

```bash

export MYSQL_URL=jdbc:mysql://localhost:3306/nequi_franchises_hub_db
export MYSQL_USERNAME=root
export MYSQL_PASSWORD=N3qu1.@1234 
```

---

### ✅ PASO 4: Compilar y ejecutar el proyecto localmente

```bash

./gradlew clean build
./gradlew :app-service:bootRun
```
El servicio esta expuesto por defecto en el puerto `8080`. Si lo cambian no olviden cambiarlo en las colecciones de postman para que apunte a su repo. 

---


### ✅ PASO 5(Opcional): Generar imagen docker y compilar el proyecto

El siguiente comando genera la imagen docker. 
```bash

docker build -f deployment/Dockerfile -t ms-nequi-franchises-hub .
```
El siguiente comando corre la imagen docker.
```bash

docker run -d --name ms-nequi-franchises-hub \
  -p 8080:8080 \
  -e SERVER_PORT=8080 \
  -e MYSQL_URL='jdbc:mysql://host.docker.internal:3306/nequi_franchises_hub_db?allowPublicKeyRetrieval=true&useSSL=false' \
  -e MYSQL_USERNAME='root' \
  -e MYSQL_PASSWORD='N3qu1.@1234' \
  ms-nequi-franchises-hub
```
---

### ✅ PASO 6(Opcional): Descargar imagen de DockerHub y compilar el proyecto

El siguiente comando descarga la imagen docker desde: https://hub.docker.com/r/crodriguez2017/ms-nequi-franchises-hub.
```bash

docker docker pull crodriguez2017/ms-nequi-franchises-hub
```
El siguiente comando corre la imagen docker.
```bash

docker run -d --name ms-nequi-franchises-hub-test \
  -p 8080:8080 \
  -e SERVER_PORT=8080 \
  -e MYSQL_URL='jdbc:mysql://host.docker.internal:3306/nequi_franchises_hub_db?allowPublicKeyRetrieval=true&useSSL=false' \
  -e MYSQL_USERNAME='root' \
  -e MYSQL_PASSWORD='N3qu1.@1234' \
  crodriguez2017/ms-nequi-franchises-hub
```
---

### ✅ PASO 6(Opcional): Descargar imagen de DockerHub y compilar el proyecto

El siguiente comando ejecuta las pruebas unitarias:
```bash

./gradlew test
```
---

## 🧪 Pruebas usando POSTMAN

Se crearon las colecciones de Postman para que sean importadas en el IDE de Postman, se encuentran en la carpeta /postman sobre la raiz del proyecto. 

En este caso son: 

- `ms-nequi-franchises-hub.postman_collection` Aqui se encontraran los EndPoints con ejemplos para peticionar.
- `ms-nequi-franchises-hub.postman_environment`. Aqui hay varias variables importantes para su uso correcto: 

        -basePath -> Esta tiene el HOST que queremos usar, se puede asignar el de local o el de AWS.
        -envLocal -> Esta variable se usa cuando se quiere peticionar en ambiente local. 
        -envAWS -> Esta variable se usa cuando se quiere peticionar al entorno desplegado en AWS.
        -version -> contiene la version del API ya que se encuentra versionado. Por defecto usar la v1.
        -franchiseId -> El id de la Franquicia con la que queremos trabajar. 
        -branchId -> El id de la Sucursal con la que queremos trabajar. 
        -productId -> El id del Producto con el que queremos trabajar. 




---
## 🌩️ Tecnologías usadas

- Programación Reactiva
- Java JDK 21
- Gradle 8.14.2
- AWS EC2
- MySql
- Spring Boot 3 (WebFlux, Data JPA)
- JUnit 5 + Mockito para pruebas unitarias
- Docker
- GitHub para versionado del código 


---

## ©️ Propuesta desarrollada por:

Ing. Christian David Rodriguez Gonzalez: Mis palabras de agradecimiento por diseñar una prueba que fuera
interesante y atractiva, termino esta prueba con animo moral muy importante para continuar el proceso.