# Literatura - Challenge Literatura 📚

¡Bienvenido a **Literatura**! Una aplicación de consola desarrollada en Java utilizando **Spring Boot** para la gestión y consulta de libros y autores. El proyecto consume la API de [Gutendex](https://gutendex.com/) para obtener datos reales de obras literarias y almacenarlos en una base de datos relacional.

## 🚀 Funcionalidades

La aplicación ofrece un menú interactivo con las siguientes opciones:

1.  **Buscar libro por título:** Busca un libro en la API de Gutendex, lo muestra en consola y lo guarda automáticamente en la base de datos (evitando duplicados).
2.  **Listar libros registrados:** Muestra todos los libros que han sido guardados previamente en la base de datos.
3.  **Listar autores registrados:** Lista todos los autores almacenados, incluyendo sus fechas de nacimiento/fallecimiento y las obras registradas.
4.  **Listar autores vivos en un determinado año:** Filtra autores de la base de datos que estaban vivos en el año ingresado por el usuario.
5.  **Listar libros por idioma:** Filtra las obras registradas según su código de idioma (es, en, fr, pt).

## 🛠️ Tecnologías Utilizadas

* **Java 17** (o superior)
* **Spring Boot 3.x**
* **Spring Data JPA:** Para la persistencia de datos.
* **PostgreSQL:** Como base de datos relacional.
* **Jackson:** Para el mapeo de JSON a objetos Java (Records).
* **HttpClient:** Para el consumo de la API externa.

## 📋 Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener:
1. Una instancia de **PostgreSQL** en funcionamiento.
2. Una base de datos creada (ej: `literatura_db`).
3. Variable de entorno configurada para la URL de la API: `URL_BASE=https://gutendex.com/books/`.

## ⚙️ Configuración del Proyecto

Edita tu archivo `src/main/resources/application.properties` con tus credenciales de base de datos:

```properties
spring.application.name=literatura

# URL de la conexión
spring.datasource.url=jdbc:postgresql://${DB_HOST}/${DB_NAME}

# Credenciales
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

# Driver (Opcional en versiones modernas de Spring Boot, ya que lo autodetecta)
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuración de Hibernate (JPA)
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.format-sql=true
