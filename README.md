## Fondos API

Servicio reactivo para la gestion de productos de fondos mutuos, construido con Spring Boot WebFlux y R2DBC.

## Tabla de Contenidos

- [Arquitectura](#arquitectura)
- [Tecnologias](#tecnologias)
- [Requisitos](#requisitos)
- [Ejecucion Local](#ejecucion-local)
- [Documentacion API](#documentacion-api)
- [Endpoints Principales](#endpoints-principales)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Pruebas](#pruebas)

## Arquitectura

El proyecto sigue una estructura orientada a puertos y adaptadores (hexagonal):

- `domain`: modelos de negocio y puertos.
- `application`: casos de uso.
- `infrastructure`: adaptadores de entrada/salida y configuracion tecnica.

## Tecnologias

- Java 17
- Spring Boot 3.4.2
- Spring WebFlux
- Spring Data R2DBC
- H2 + R2DBC H2
- Jakarta Validation
- Springdoc OpenAPI (Swagger UI)
- Gradle

## Requisitos

- JDK 17+
- Gradle Wrapper (incluido)

## Ejecucion Local

1. Compilar:

```bash
./gradlew clean build
```

En Windows PowerShell:

```powershell
.\gradlew.bat clean build
```

2. Iniciar servicio:

```bash
./gradlew bootRun
```

En Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

La aplicacion corre por defecto en `http://localhost:8020`.

## Documentacion API

- Swagger UI: `http://localhost:8020/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8020/v3/api-docs`
- OpenAPI YAML: `http://localhost:8020/v3/api-docs.yaml`

Guia detallada de API en [docs/APIDOCS.md](docs/APIDOCS.md).

## Endpoints Principales

- `POST /api/v1/products`
- `GET /api/v1/products`
- `GET /api/v1/products/{id}`
- `PUT /api/v1/products/{id}`
- `DELETE /api/v1/products/{id}`

## Estructura del Proyecto

```text
src/main/java/com/scotiabank/fondos
|-- application
|-- domain
|-- infrastructure
|   |-- adapters
|   |-- config
```

## Pruebas

```bash
./gradlew test
```

En Windows PowerShell:

```powershell
.\gradlew.bat test
```
