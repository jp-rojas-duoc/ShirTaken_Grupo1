# 🛒👕 ShirTaken — Tienda Android Jetpack Compose

**ShirTaken** es una aplicación demo de **e-commerce** para la venta de poleras, creada en **Android** con **Jetpack Compose**, arquitectura **MVVM**, y **Room**.  
Diseñada como una solución profesional para la gestión de catálogo, carrito de compras y pedidos, con validaciones en tiempo real y experiencia **Material 3**.

---

## 📝 Características principales

-  **Material Design 3 completo:** Interfaz moderna, responsiva y amigable.  
-  **Navegación funcional (Navigation Compose):** Flujo desde **catálogo → detalle → carrito → checkout → historial**.  
-  **Gestión de estado vía ViewModel:** Arquitectura **MVVM** desacoplada y reactiva con `StateFlow`/`MutableState`.  
-  **Persistencia local con Room:** Catálogo, pedidos y stock almacenados en SQLite.  
-  **Validaciones en tiempo real:** Formularios de compra robustos y consistentes.  
-  **Animaciones suaves:** Transiciones en carrito y secciones plegables del checkout.  
-  **Feedback inmediato:** Snackbars de éxito/error y estados claros en toda la app.  
-  **Historial de compras persistente:** Consulta todos los pedidos realizados.  
-  **Control de stock inteligente:** Descuento automático y bloqueo ante falta de stock.  
-  **Datos demo automáticos:** Poblado inicial al primer arranque.

---

## 📂 Estructura del proyecto
├── model/        ← Modelos básicos como Polera, ItemCarrito, PedidoEntity
├── repository/   ← Acceso a datos (Room) y lógica de negocio (repositorios)
├── ui/           ← Pantallas (Compose), navegación, temas y utilidades
│   ├── pantallas/   ← Pantallas: Catálogo, Detalle, Carrito, Checkout, Historial, Inicio
│   ├── navegacion/  ← AppNavegacion y rutas
│   ├── theme/       ← Personalización de colores / tipografía Material 3
│   └── utils/       ← Validadores y utilidades
└── viewmodel/    ← ViewModels para cada feature (Carrito, Poleras, Historial)

---

## 🚀 ¿Cómo funciona la app?

1. **Explora el catálogo:** Visualiza todas las poleras disponibles, con stock visible y búsqueda rápida.  
2. **Detalle del producto:** Toca una polera para ver sus detalles, foto y stock actual.  
3. **Agrega al carrito:** Solo puedes sumar productos si hay stock disponible. El botón “Agregar al carrito” se desactiva automáticamente.  
4. **Gestiona tu carrito:** Suma, resta o elimina productos; si superas el stock, se mostrará un aviso por `Snackbar`.  
5. **Completa el pago:** Formulario validado en tiempo real (nombre, email, teléfono, dirección) con totales, despacho y cupones.  
6. **Confirma tu compra:** Se descuenta el stock, se limpia el carrito y se registra el pedido en el historial.  
7. **Revisa tus pedidos:** Consulta el historial con los pedidos previos y su detalle completo.

---

## 🛠️ Tecnologías usadas

-  **Kotlin + Jetpack Compose**  
-  **Room (persistencia local)**  
-  **ViewModel + StateFlow (arquitectura MVVM reactiva)**  
-  **Material 3 (UI moderna)**  
-  **Coil (carga de imágenes)**  
-  **Snackbars, AnimatedVisibility (UX moderna)**

---
## 🚀 Cómo ejecutar

1. **Clona el repositorio** y ábrelo en **Android Studio**.  
2. **Sincroniza Gradle** y ejecuta el proyecto en un **emulador o dispositivo físico**.  
3. En el **primer arranque**, la app cargará productos demo automáticamente.  
4. **Prueba el flujo completo:** agrega productos al carrito, realiza una compra y revisa el historial.


