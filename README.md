# Examen Transversal. ShirTaken - E-commerce de Póleras 👕

[![Android](https://img.shields.io/badge/Android-Kotlin-brightgreen)](https://www.android.com)
[![Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot-green)](https://spring.io)
[![Database](https://img.shields.io/badge/Database-MySQL-blue)](https://www.mysql.com)

Aplicación Android de e-commerce integrada con microservicios Spring Boot para la venta de póleras personalizadas.

## 🎯 Descripción

ShirTaken es una aplicación full-stack que demuestra:
- ✅ **Consumo de APIs REST** con Retrofit
- ✅ **Arquitectura MVVM** con Repository Pattern
- ✅ **Persistencia local** con Room (SQLite)
- ✅ **Microservicios** Spring Boot
- ✅ **Pruebas unitarias** (5/5 tests pasando)
- ✅ **APK firmado** en modo release

## 🏗️ Arquitectura

### Frontend (Android)
- **Lenguaje**: Kotlin
- **UI Framework**: Jetpack Compose
- **Arquitectura**: MVVM + Repository Pattern
- **Base datos local**: Room
- **HTTP Client**: Retrofit + OkHttp

### Backend (Spring Boot)
- **Lenguaje**: Java
- **Framework**: Spring Boot
- **Base datos**: MySQL

```
┌─────────────┐
│   Android   │ (UI + Local DB - Room)
└──────┬──────┘
       │ HTTP REST (Retrofit)
       ▼
┌──────────────────┐
│  Spring Boot     │ (API REST)
└──────┬───────────┘
       │ JDBC
       ▼
┌──────────────┐
│    MySQL     │
└──────────────┘
```

## ✨ Características Principales

### 📱 Pantallas Implementadas

1. **Inicio**: Navegación principal a catálogo e historial
2. **Catálogo**: Listado dinámico de póleras desde backend
3. **Detalle**: Información completa del producto (nombre, precio, stock)
4. **Carrito**: Gestión de items (agregar, quitar, ver total)
5. **Checkout**: Formulario de compra con datos del cliente
6. **Historial**: Compras guardadas localmente en Room

### 🔄 Flujo de Compra Completo

1. **Catálogo**: Carga de `GET /api/poleras` en tiempo real
2. **Selección**: Usuario elige producto y ve detalles
3. **Carrito**: Agrega items (almacenamiento local)
4. **Checkout**: Completa formulario de compra
5. **Backend**: `POST /api/pedidos` crea el pedido
6. **Stock**: Se descuenta automáticamente con `PUT /api/poleras/{id}/stock`
7. **Historial**: Pedido se sincroniza a Room y aparece en historial

## 🗄️ Base de Datos

### Local (Room - SQLite)
```sql
-- Tabla de pedidos
CREATE TABLE pedidos (
    id INTEGER PRIMARY KEY,
    numero_pedido TEXT UNIQUE,
    fecha TEXT,
    cliente_nombre TEXT,
    cliente_email TEXT,
    total INTEGER,
    estado TEXT
);

-- Tabla de items del pedido
CREATE TABLE pedido_items (
    id INTEGER PRIMARY KEY,
    pedido_id INTEGER,
    polera_id INTEGER,
    nombre_polera TEXT,
    cantidad INTEGER,
    precio_unitario INTEGER
);
```

### Remota (MySQL - Backend)
- **poleras**: id, nombre, marca, precio, talla, color, urlImagen, stock
- **pedidos**: id, numero_pedido, cliente_nombre, cliente_email, fecha, total, estado
- **detalles_pedido**: id, pedido_id, polera_id, cantidad, precio_unitario

## 🚀 Instalación & Ejecución

### Requisitos Previos

- Android Studio Flamingo o superior
- JDK 11+
- MySQL 8.0+
- Spring Boot backend ejecutándose en `http://192.168.1.136:8080`

### Pasos

1. **Clonar repositorio**
   ```bash
   git clone https://github.com/tu-usuario/ShirTaken.git
   cd ShirTaken
   ```

2. **Sincronizar Gradle**
   ```bash
   ./gradlew sync
   ```

3. **Configurar IP del backend** (si es diferente)
   - Archivo: `app/src/main/java/cl/shirtaken/shirtaken_grupo1/data/remote/PolerasApi.kt`
   - Cambiar: `.baseUrl("http://192.168.1.136:8080/")`

4. **Ejecutar en dispositivo/emulador**
   ```bash
   ./gradlew installDebug
   ```

5. **Generar APK Release (Firmado)**
   ```bash
   ./gradlew assembleRelease
   # Ubicación: app/release/app-release.apk
   ```

## 🧪 Testing

### Ejecutar todos los tests

```bash
./gradlew test
```

### Tests Implementados

**PolerasViewModelTest**
- ✅ testPoleraModeloCreation - Valida creación de modelo
- ✅ testPoleraStockValidation - Valida disponibilidad de stock

**CarritoViewModelTest**
- ✅ testItemCarritoCreation - Valida creación de items
- ✅ testCalcularTotalCarrito - Valida suma total correcta
- ✅ testCarritoVacioTieneZeroTotal - Valida carrito vacío

**Resultado**: ✅ 5/5 tests pasando

## 📦 Build & Versioning

### Debug APK
```bash
./gradlew installDebug
# Tamaño: ~15MB
# Ubicación: app/build/outputs/apk/debug/app-debug.apk
```

### Release APK (Firmado)
```bash
./gradlew assembleRelease
# Tamaño: ~8MB (minificado)
# Ubicación: app/release/app-release.apk
```

**Credenciales Keystore:**
- Alias: `shirtaken-key`
- Validez: 25 años

## 🔗 Endpoints API Backend

**Base URL**: `http://192.168.1.136:8080`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/poleras` | Obtener catálogo completo |
| GET | `/api/poleras/{id}` | Obtener polera por ID |
| GET | `/api/poleras/{id}/stock` | Consultar stock disponible |
| PUT | `/api/poleras/{id}/stock` | Descontar stock por cantidad |
| POST | `/api/pedidos` | Crear nuevo pedido |

## 📊 Stack Tecnológico

### Android
- Kotlin 1.9+
- Jetpack Compose (UI)
- Room (Persistencia local)
- Retrofit 2.x + OkHttp 4.x (Networking)
- Coil (Carga de imágenes)
- JUnit 4 (Testing)
- Coroutines (Async)

### Backend
- Spring Boot 3.x
- Spring Data JPA
- MySQL 8.0
- Lombok

## 📋 Estructura del Proyecto

```
ShirTaken/
├── app/src/
│   ├── main/
│   │   ├── java/cl/shirtaken/shirtaken_grupo1/
│   │   │   ├── ui/pantallas/
│   │   │   │   ├── PantallaInicio.kt
│   │   │   │   ├── PantallaCatalogo.kt
│   │   │   │   ├── PantallaDetalle.kt
│   │   │   │   ├── PantallaCarrito.kt
│   │   │   │   ├── PantallaCheckout.kt
│   │   │   │   └── PantallaHistorial.kt
│   │   │   ├── viewmodel/
│   │   │   │   ├── PolerasViewModel.kt
│   │   │   │   ├── CarritoViewModel.kt
│   │   │   │   └── HistorialViewModel.kt
│   │   │   ├── repository/
│   │   │   │   ├── RepositorioPolerasRemoto.kt
│   │   │   │   ├── RepositorioPolerasRoom.kt
│   │   │   │   └── RepositorioPedidos.kt
│   │   │   ├── data/
│   │   │   │   ├── remote/
│   │   │   │   │   └── PolerasApi.kt
│   │   │   │   └── local/
│   │   │   │       ├── AppDb.kt
│   │   │   │       └── PedidoDao.kt
│   │   │   ├── model/
│   │   │   │   ├── Polera.kt
│   │   │   │   ├── Pedido.kt
│   │   │   │   └── ItemCarrito.kt
│   │   │   └── ui/navegacion/
│   │   │       └── AppNavegacion.kt
│   │   └── res/
│   └── test/
│       └── java/cl/shirtaken/shirtaken_grupo1/viewmodel/
│           ├── PolerasViewModelTest.kt
│           └── CarritoViewModelTest.kt
├── build.gradle (app)
├── README.md (este archivo)
└── DOCUMENTACION_TECNICA.md
```

## ✅ Checklist de Entrega - Parcial #4

- [x] ✅ Consumo de APIs externas (Retrofit)
- [x] ✅ Conexión con microservicios Spring Boot
- [x] ✅ Pruebas unitarias (5/5 tests pasando)
- [x] ✅ APK generado en modo release (firmado)
- [x] ✅ Documentación técnica del proyecto
- [x] ✅ README.md completo
- [x] ✅ Contexto alineado con parámetros del EFT (e-commerce)

## 👥 Equipo

- [Jose Parra ,Javier Reyes , Juan Pablo Rojas/Grupo1]

## 📅 Fecha de Entrega

**18 de Noviembre de 2025**

## 📝 Notas Importantes

- La aplicación requiere conexión de red activa
- Backend Spring Boot debe estar ejecutándose
- Base de datos MySQL debe estar accesible
- Compatible con Android 8.0 (API 26) o superior
- APK firmado en modo release lista para distribuir


---

**Parcial #4 - Evaluación de Competencias Digitales DSY1105**
**Instituto Profesional - 2025**
