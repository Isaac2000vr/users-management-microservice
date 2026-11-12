# Users Management — Spring Boot, Arquitectura Hexagonal y DDD

Aplicación de gestión de usuarios y reservas de hotel construida con Java 17 y Spring Boot. La API REST es el punto de entrada activo. El código de la antigua CLI se conserva como adaptador inactivo y no posee un contenedor de dependencias independiente.

Spring es el único *composition root*: `Main` inicia el contexto y las dependencias se resuelven mediante configuración y component scanning de Spring.

## Verificación

`GET /health` es publico y responde HTTP 200 con los metadatos generados desde
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

## Reservas de hotel

El servicio también permite llevar el registro de las reservas de hotel. Cada reserva guarda la fecha en que se hizo, el hotel, el huésped, la fecha de inicio y de fin de la estadía, el valor pagado, la habitación, el número de acompañantes, el país, el departamento y la ciudad donde queda el hotel, la hora de check-in y de check-out, el empleado que atiende y el que despide al huésped, y una descripción opcional con observaciones adicionales.

Las reservas pasan por cuatro estados: `PENDING` cuando se crean, y luego pueden pasar a `CONFIRMED`, `CANCELLED` o `COMPLETED` al actualizarlas.

Los endpoints son los mismos de siempre, sobre `/api/reservas-hotel`:

| Método | Ruta | Descripción | Permiso |
|--------|------|-------------|---------|
| POST | `/api/reservas-hotel` | Crea una reserva | Público |
| GET | `/api/reservas-hotel` | Lista todas las reservas | ADMIN o REVIEWER |
| GET | `/api/reservas-hotel/{id}` | Consulta una reserva por su id | ADMIN o REVIEWER |
| PUT | `/api/reservas-hotel/{id}` | Actualiza una reserva | ADMIN |
| DELETE | `/api/reservas-hotel/{id}` | Elimina una reserva | ADMIN |

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
