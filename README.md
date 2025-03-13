# API de Gestión Bancaria - CDT Bancolombia

API REST para la gestión de productos bancarios, incluyendo cuentas de ahorro, CDTs y tarjetas de crédito.

## Características

- Gestión de usuarios y cuentas bancarias
- Creación y administración de CDTs
- Manejo de tarjetas de crédito
- Transferencias entre cuentas
- Simulación de CDTs con diferentes plazos

## Tecnologías

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database (para desarrollo)
- Maven

## Requisitos

- Java JDK 17 o superior
- Maven 3.8.x o superior
- IDE compatible con Spring Boot (recomendado: IntelliJ IDEA o Spring Tool Suite)

## Instalación

1. Clonar el repositorio:
```bash
git clone https://github.com/tuusuario/BancolombiaCDT.git
```

2. Navegar al directorio del proyecto:
```bash
cd BancolombiaCDT
```

3. Compilar el proyecto:
```bash
mvn clean install
```

4. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Endpoints Principales

### Usuarios
- POST /api/users - Crear usuario
- PUT /api/users/{id} - Actualizar usuario
- GET /api/users/{id} - Obtener usuario
- DELETE /api/users/{id} - Eliminar usuario

### Cuentas
- POST /api/accounts - Crear cuenta
- PUT /api/accounts/{id} - Actualizar cuenta
- GET /api/accounts/{id} - Obtener cuenta
- DELETE /api/accounts/{id} - Desactivar cuenta

### CDTs
- POST /api/cdt - Crear CDT
- PUT /api/cdt/{id} - Actualizar CDT
- DELETE /api/cdt/{id} - Eliminar CDT
- POST /api/cdt/simular - Simular CDT

### Transferencias
- POST /api/transfer - Realizar transferencia

### Tarjetas de Crédito
- POST /api/tarjetas - Crear tarjeta
- PUT /api/tarjetas/{id} - Actualizar tarjeta
- DELETE /api/tarjetas/{id} - Eliminar tarjeta

## Seguridad y Validaciones

- Validación de datos de entrada
- Verificación de saldos para transferencias
- Validación de cuentas activas
- Protección contra operaciones inválidas

