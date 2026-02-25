# API Docs - Fondos API

## Estandar Aplicado

- Especificacion OpenAPI 3.0+.
- Contratos versionados por path (`/api/v1`).
- Respuestas HTTP coherentes por operacion CRUD.
- Validacion de entrada con Jakarta Validation.

## Base URL

`http://localhost:8020`

## Recursos

### Product

```json
{
  "id": 1,
  "code": "FND001",
  "name": "Fondo Conservador",
  "description": "Fondo orientado a bajo riesgo",
  "price": 1500.50,
  "category": "Renta Fija",
  "regDate": "2026-02-25T10:00:00",
  "modDate": "2026-02-25T10:00:00",
  "state": "ACTIVE"
}
```

## Endpoints

### Crear Producto

- Metodo: `POST`
- Ruta: `/api/v1/products`
- Request Body:

```json
{
  "code": "FND001",
  "name": "Fondo Conservador",
  "description": "Fondo orientado a bajo riesgo",
  "price": 1500.50,
  "category": "Renta Fija"
}
```

- Respuestas:
- `201 Created`: producto creado.
- `400 Bad Request`: datos invalidos.

### Listar Productos

- Metodo: `GET`
- Ruta: `/api/v1/products`
- Respuestas:
- `200 OK`: lista de productos.

### Obtener Producto por ID

- Metodo: `GET`
- Ruta: `/api/v1/products/{id}`
- Respuestas:
- `200 OK`: producto encontrado.
- `404 Not Found`: no existe el producto.

### Actualizar Producto

- Metodo: `PUT`
- Ruta: `/api/v1/products/{id}`
- Request Body: mismo esquema que creacion.
- Respuestas:
- `200 OK`: producto actualizado.
- `400 Bad Request`: datos invalidos.
- `404 Not Found`: no existe el producto.

### Eliminar Producto

- Metodo: `DELETE`
- Ruta: `/api/v1/products/{id}`
- Respuestas:
- `204 No Content`: eliminado correctamente.

## Swagger y OpenAPI

- UI interactiva: `http://localhost:8020/swagger-ui.html`
- Contrato JSON: `http://localhost:8020/v3/api-docs`
- Contrato YAML: `http://localhost:8020/v3/api-docs.yaml`

## Recomendaciones de Gobierno API

- Mantener convencion semantica de versiones (`v1`, `v2`, ...).
- Documentar cambios breaking en CHANGELOG.
- Estandarizar errores con payload uniforme en futuras iteraciones.
