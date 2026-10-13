# MediSoft | Módulo CRUD de citas con JDBC

Implementación Java del módulo de gestión de citas médicas de MediSoft. Se relaciona con los flujos de agendamiento, consulta y cancelación del prototipo front-end del proyecto.

## Criterios de la evidencia

1. **Conexión JDBC:** `database/ConexionBD.java` abre conexiones MySQL mediante `DriverManager`.
2. **CRUD:** `dao/CitaDao.java` implementa insertar, listar, buscar por ID, actualizar y eliminar.
3. **Versionamiento:** el proyecto se administra con Git; el historial local comienza con el commit `Implementa módulo CRUD de citas con JDBC`.
4. **Estándar:** convenciones Java: paquetes en minúscula, clases y tipos en `UpperCamelCase`, métodos y variables en `lowerCamelCase`, constantes en `UPPER_SNAKE_CASE`; consultas parametrizadas, cierre de recursos con try-with-resources y separación por capas.

## Requisitos

- JDK 17 o superior.
- Maven 3.8 o superior.
- MySQL 8.0 o superior.

Maven descarga MySQL Connector/J, el driver JDBC oficial para MySQL, declarado en `pom.xml`.

## Preparar la base de datos

Ejecuta `database/schema.sql` desde MySQL Workbench o desde el cliente `mysql`. El script crea la base `medisoft` y la tabla `citas`.

## Configurar conexión

La URL predeterminada es `jdbc:mysql://localhost:3306/medisoft?serverTimezone=UTC` y el usuario predeterminado es `root`. Para establecer las credenciales localmente, configura estas variables de entorno antes de iniciar el programa:

```powershell
$env:MEDISOFT_DB_URL = "jdbc:mysql://localhost:3306/medisoft?serverTimezone=UTC"
$env:MEDISOFT_DB_USER = "root"
$env:MEDISOFT_DB_PASSWORD = "tu_clave_local"
```

No guardes contraseñas personales en el código ni en Git.

## Ejecutar

Desde la carpeta del proyecto:

```powershell
mvn compile exec:java
```

El menú permite registrar, listar, buscar por ID, actualizar y eliminar citas. La fecha se ingresa con formato `AAAA-MM-DD HH:MM`; el estado permitido es `PROGRAMADA`, `ATENDIDA` o `CANCELADA`.

## Estructura

```text
src/main/java/co/medisoft/citas/
├── Main.java
├── dao/CitaDao.java
├── database/ConexionBD.java
└── model/Cita.java
database/schema.sql
```

## Repositorio remoto

Este paquete contiene el repositorio Git local del módulo. Para publicarlo en GitHub/GitLab, crea un repositorio remoto vacío y configura su URL:

```bash
git remote add origin URL_DEL_REPOSITORIO
git push -u origin main
```

Registra la URL real del repositorio remoto en `repositorio.txt` cuando se publique.
