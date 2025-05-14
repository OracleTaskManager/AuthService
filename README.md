
# Oracle Task Manager - Auth Service

Este repositorio contiene el microservicio de autenticación para el sistema Oracle Task Manager. Proporciona funcionalidades de registro, inicio de sesión y gestión de usuarios y equipos.

## Requisitos previos

- Java JDK 23
- Maven
- Oracle Wallet (proporcionado separadamente)
- Git

## Configuración del entorno local

### 1. Clonar el repositorio

```bash
git clone https://github.com/tuOrganizacion/AuthService.git
cd AuthService
```

### 2. Configurar Oracle Wallet
### 2.1 Configurar `sqlnet.ora`

Asegúrate de que el archivo `sqlnet.ora` ubicado dentro del directorio del wallet contenga la ruta correcta. Debe verse así:

```ora
WALLET_LOCATION = (SOURCE = (METHOD = file) (METHOD_DATA = (DIRECTORY="C:\Users\cesar\Wallet_TelegramBotDatabase")))
SSL_SERVER_DN_MATCH=yes
```

### 2.2 Configurar `application.properties`

1. Coloca los archivos del wallet en una ubicación accesible (p. ej., `C:/Users/your-username/Wallet_TelegramBotDatabase`)
2. Asegúrate de que el archivo `application.properties` tenga la configuración correcta para tu entorno local:

```properties
spring.datasource.url=jdbc:oracle:thin:@TelegramBotDatabase_medium?TNS_ADMIN=C:/Users/your-username/Wallet_TelegramBotDatabase
spring.datasource.username=ADMIN
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
spring.jpa.hibernate.ddl-auto=none
spring.jpa.open-in-view=false
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
jwt.secret.oracle=${JWT_SECRET_ORACLE}
server.port=8080
telegram.bot.secret=${TELEGRAM_BOT_SECRET}

spring.datasource.hikari.maximum-pool-size=3
spring.datasource.hikari.minimum-idle=1
spring.datasource.hikari.idle-timeout=30000
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.max-lifetime=600000

# Configuración básica para Swagger en entorno local
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

# Comentar/eliminar estas líneas para desarrollo local
springdoc.swagger-ui.configUrl=/swagger-auth/v3/api-docs/swagger-config
springdoc.swagger-ui.url=/swagger-auth/v3/api-docs
springdoc.swagger-ui.oauth2RedirectUrl=/swagger-auth/swagger-ui/oauth2-redirect.html
springdoc.swagger-ui.disable-swagger-default-url=true
```

3. Configura las variables de entorno para las credenciales:
   - `DB_PASSWORD`: Contraseña de tu base de datos
   - `JWT_SECRET_ORACLE`: Clave secreta para generar tokens JWT
   - `TELEGRAM_BOT_SECRET`: Clave del bot de Telegram

### 3. Compilar y ejecutar la aplicación

```bash
mvn clean package
java -jar target/AuthService-0.0.1-SNAPSHOT.jar
```

Alternativamente, puedes ejecutar la aplicación directamente con Maven:

```bash
mvn spring-boot:run
```

### 4. Verificar la instalación

- La API estará disponible en: http://localhost:8080
- La documentación Swagger UI: http://localhost:8080/swagger-ui.html

## Diferencias entre entorno local y producción

### Configuración del Wallet

| Local | Producción |
|-------|------------|
| Ruta local al wallet (`C:/Users/your-username/Wallet_TelegramBotDatabase`) | Montado en contenedor como volumen (`/wallet`) |

### Configuración de Swagger

| Local | Producción |
|-------|------------|
| Acceso directo: `/swagger-ui.html` | Acceso a través de Ingress: `http://140.84.189.81/swagger-auth/swagger-ui.html` |
| Sin prefijo en URLs | URLs con prefijo `/api/auth` |
| No requiere configUrl | Requiere `springdoc.swagger-ui.configUrl=/v3/api-docs/swagger-config` |

### Configuración de endpoints

| Local | Producción |
|-------|------------|
| Acceso directo: `http://localhost:8080/users/login` | Acceso con prefijo: `http://140.84.189.81/api/auth/users/login` |
| Sin proxy reverso | Utiliza Ingress y reescritura de rutas |

### Despliegue con GitHub Actions

Para el despliegue en producción, utilizamos GitHub Actions que automatiza:

1. Modificación de `application.properties` para usar la ruta `/wallet`
2. Compilación del proyecto con Maven
3. Creación de una imagen Docker
4. Subida de la imagen al Oracle Container Registry
5. Reconstrucción de los archivos del wallet desde secretos almacenados
6. Actualización del despliegue Kubernetes

El pipeline en `.github/workflows/build-push-auth.yml` maneja todo este proceso automáticamente cuando se hace push a la rama principal.

## Ejemplos de peticiones

### Local

```http
POST http://localhost:8080/users/login
Content-Type: application/json

{
  "email": "usuario@ejemplo.com",
  "password": "contraseña"
}
```

### Producción

```http
POST http://140.84.189.81/api/auth/users/login
Content-Type: application/json

{
  "emsil": "usuario@ejemplo.com",
  "password": "contraseña"
}
```

## Documentación API

- **Local**: http://localhost:8080/swagger-ui.html
- **Producción**: http://140.84.189.81/swagger-auth/swagger-ui.html

## Solución de problemas

### Error de conexión a la base de datos

Verifica:
- Que todos los archivos del wallet estén presentes
- Que la ruta en `TNS_ADMIN` sea correcta
- Que las credenciales de la base de datos sean correctas

### Problemas con Swagger

Para entorno local:
- Asegúrate de que `springdoc.swagger-ui.configUrl` esté comentado
- Verifica que la configuración básica de SpringDoc esté habilitada
