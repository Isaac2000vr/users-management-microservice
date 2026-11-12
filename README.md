# Users Management — Spring Boot, Arquitectura Hexagonal y DDD

Aplicación de gestión de usuarios y reservas de hotel construida con Java 17 y Spring Boot. La API REST es el punto de entrada activo. El código de la antigua CLI se conserva como adaptador inactivo y no posee un contenedor de dependencias independiente.

Spring es el único *composition root*: `Main` inicia el contexto y las dependencias se resuelven mediante configuración y component scanning de Spring.

## Demo en línea

La API está desplegada en Render y usa TiDB Cloud Serverless como base de datos:

- Servicio: https://users-management-api-70w5.onrender.com
- Estado: https://users-management-api-70w5.onrender.com/health
- Swagger UI: https://users-management-api-70w5.onrender.com/swagger-ui.html

El plan gratuito de Render suspende el servicio tras unos minutos sin tráfico, así que la primera petición puede tardar cerca de un minuto.

## Verificación

`GET /health` es público y responde HTTP 200 con los metadatos generados desde
`pom.xml` durante el build:

```json
{"status":"UP","name":"users-management","version":"2.2.0"}
```

Indica que la API responde; no comprueba MySQL ni el proveedor de correo.

```bash
./mvnw clean test
./mvnw clean package
```

En Windows se puede utilizar `mvnw.cmd`.

## Endpoints

Salvo las rutas marcadas como públicas, todas las peticiones necesitan el token JWT
en el header `Authorization: Bearer <token>`. El token se obtiene con el login y
dura 15 minutos.

### Salud y autenticación

| Método | Ruta | Descripción | Permiso |
|--------|------|-------------|---------|
| GET | `/health` | Estado de la API | Público |
| POST | `/api/auth/login` | Autentica y devuelve un JWT | Público |

**Login**

```json
{
  "email": "admin@example.com",
  "password": "Admin1234!"
}
```

Respuesta `200`:

```json
{
  "accessToken": "<jwt>",
  "tokenType": "Bearer",
  "expiresIn": 900
}
```

### Usuarios

| Método | Ruta | Descripción | Permiso |
|--------|------|-------------|---------|
| POST | `/api/users` | Crea un usuario | Público |
| GET | `/api/users` | Lista todos los usuarios | ADMIN o REVIEWER |
| GET | `/api/users/{id}` | Consulta un usuario por su id | ADMIN o REVIEWER |
| PUT | `/api/users/{id}` | Actualiza un usuario | ADMIN |
| DELETE | `/api/users/{id}` | Elimina un usuario | ADMIN |

**Crear usuario**

```json
{
  "id": "usr-001",
  "name": "Isaac Vasquez",
  "email": "isaac@example.com",
  "password": "Password123!",
  "role": "MEMBER"
}
```

Respuesta `201`:

```json
{
  "id": "usr-001",
  "name": "Isaac Vasquez",
  "email": "isaac@example.com",
  "role": "MEMBER",
  "status": "PENDING"
}
```

Los usuarios se crean en estado `PENDING`. Un administrador los activa con un `PUT`
enviando `"status": "ACTIVE"`.

**Actualizar usuario**

```json
{
  "name": "Isaac Vasquez",
  "email": "isaac@example.com",
  "password": "",
  "role": "MEMBER",
  "status": "ACTIVE"
}
```

Si `password` viene vacío se conserva la contraseña actual.

### Reservas de hotel

| Método | Ruta | Descripción | Permiso |
|--------|------|-------------|---------|
| POST | `/api/reservas-hotel` | Crea una reserva | Público |
| GET | `/api/reservas-hotel` | Lista todas las reservas | ADMIN o REVIEWER |
| GET | `/api/reservas-hotel/{id}` | Consulta una reserva por su id | ADMIN o REVIEWER |
| PUT | `/api/reservas-hotel/{id}` | Actualiza una reserva | ADMIN |
| DELETE | `/api/reservas-hotel/{id}` | Elimina una reserva | ADMIN |

**Crear reserva**

```json
{
  "id": "res-demo-001",
  "fecha": "2026-11-12",
  "hotel": "Hotel Cartagena",
  "huesped": "Juan Perez",
  "fechaInicio": "2026-12-01",
  "fechaFin": "2026-12-05",
  "valor": 450000.0,
  "habitacion": "301",
  "numAcompanantes": 2,
  "pais": "Colombia",
  "departamento": "Bolivar",
  "ciudad": "Cartagena",
  "horaCheckin": "15:00",
  "horaCheckout": "12:00",
  "empleadoAtiende": "Maria Lopez",
  "empleadoDespide": "Carlos Ruiz",
  "descripcion": "Vista al mar"
}
```

Respuesta `201`: la reserva creada con `"status": "PENDING"`.

**Actualizar reserva**

```json
{
  "fecha": "2026-11-12",
  "hotel": "Hotel Cartagena",
  "huesped": "Juan Perez",
  "fechaInicio": "2026-12-01",
  "fechaFin": "2026-12-05",
  "valor": 500000.0,
  "habitacion": "302",
  "numAcompanantes": 3,
  "pais": "Colombia",
  "departamento": "Bolivar",
  "ciudad": "Cartagena",
  "horaCheckin": "15:00",
  "horaCheckout": "12:00",
  "empleadoAtiende": "Maria Lopez",
  "empleadoDespide": "Carlos Ruiz",
  "descripcion": "Cambio de habitacion",
  "status": "CONFIRMED"
}
```

El `PUT` reemplaza todos los campos de la reserva, incluido el estado. El `DELETE`
responde `204` sin cuerpo.

### Códigos de respuesta

| Código | Cuándo ocurre |
|--------|---------------|
| 200 | Consulta o actualización exitosa |
| 201 | Creación exitosa |
| 204 | Eliminación exitosa |
| 400 | Datos inválidos o estado no permitido |
| 401 | Falta el token o las credenciales son incorrectas |
| 403 | El rol del usuario no tiene permiso |
| 404 | El recurso no existe |
| 409 | El usuario ya existe |
| 500 | Error inesperado del servidor |

## Modelo de reservas de hotel

El servicio también permite llevar el registro de las reservas de hotel. Cada reserva guarda la fecha en que se hizo, el hotel, el huésped, la fecha de inicio y de fin de la estadía, el valor pagado, la habitación, el número de acompañantes, el país, el departamento y la ciudad donde queda el hotel, la hora de check-in y de check-out, el empleado que atiende y el que despide al huésped, y una descripción opcional con observaciones adicionales.

Las reservas pasan por cuatro estados: `PENDING` cuando se crean, y luego se pueden actualizar a `CONFIRMED`, `CANCELLED` o `COMPLETED` mediante el `PUT`. El estado se valida contra el enum, así que un valor desconocido devuelve `400`.

Los endpoints y ejemplos de request están documentados en la sección
[Reservas de hotel](#reservas-de-hotel) de arriba, y también en Swagger UI.

Las reservas se guardan en la tabla `reservas_hotel` (ver `src/main/resources/schema.sql`), siguiendo el mismo flujo que los usuarios: el caso de uso valida los datos, el dominio aplica las reglas y el repositorio en MySQL se encarga de la persistencia.

## Pruebas con Postman

La colección `postman/users-management.postman_collection.json` agrupa las peticiones de salud, autenticación, usuarios y reservas. Para usarla:

1. Importar el archivo en Postman.
2. Ajustar `baseUrl` si la API no está en `http://localhost:8080`.
3. Ejecutar **Login admin**: el script guarda el `accessToken` en la variable `token`.
4. Correr las demás peticiones o la colección completa con el Collection Runner.

Cada petición incluye pruebas básicas del código de respuesta y del estado resultante.

## Integración y despliegue continuo

El flujo `.github/workflows/ci.yml` se ejecuta en cada push a `main` o a una rama `feature/**` y en los pull requests hacia `main`:

1. Compila y ejecuta la suite con Maven sobre JDK 17.
2. Construye la imagen Docker con el mismo `Dockerfile` que se usa en local.
3. En pushes a `main`, dispara el deploy hook de Render si existe el secreto `RENDER_DEPLOY_HOOK_URL`.

Para habilitar el último paso hay que crear ese secreto en GitHub con la URL del deploy hook del servicio en Render.

## Despliegue en Render

El archivo `render.yaml` define el servicio web, el build con Docker y el despliegue
automático de cada commit que llegue a la rama `main`.

1. Crear un Blueprint en Render y seleccionar este repositorio.
2. Completar en el panel los secretos marcados como requeridos: `DB_HOST`,
   `DB_USERNAME`, `DB_PASSWORD`, `SMTP_USERNAME`, `SMTP_PASSWORD` y
   `SMTP_FROM_ADDRESS`.
3. Usar una instancia MySQL accesible desde Internet o desde la red privada de
   Render y ejecutar `src/main/resources/schema.sql` una vez para crear el esquema.
4. Desplegar el Blueprint. La API quedará disponible en el subdominio
   `onrender.com` asignado por Render y Swagger UI en `/swagger-ui.html`.

Las credenciales nunca deben guardarse en `application.properties` ni en
`render.yaml`. Para desarrollo local, deben proporcionarse como variables de entorno.
