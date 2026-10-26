# Registro de productos — Android

Aplicación Android desarrollada con **Kotlin y Jetpack Compose** para registrar y consultar productos.

## Funcionalidades

- Menú principal para acceder al registro y al listado de productos.
- Formulario con código de barras, nombre, precio, categoría y URL.
- Validación de campos obligatorios y del precio.
- Consulta de productos almacenados en Firebase Firestore.

## Tecnologías

- Kotlin
- Jetpack Compose y Material 3
- Firebase Cloud Firestore
- Android Studio y Gradle

## Estructura

La interfaz y los flujos principales están en `app/src/main/java/com/example/myapplication/MainActivity.kt`. El modelo de datos se encuentra en `Producto.kt`.

## Ejecución local

1. Abrir el proyecto con Android Studio.
2. Sincronizar Gradle.
3. Configurar un proyecto Firebase propio con Cloud Firestore y el archivo `google-services.json` correspondiente.
4. Ejecutar la aplicación en un emulador o dispositivo Android compatible (Android 7.0 o posterior, según `minSdk = 24`).

El acceso a Firestore depende de la configuración y las reglas de seguridad del proyecto Firebase utilizado. No incluyo credenciales ni configuraciones privadas en estas instrucciones.

Proyecto desarrollado para practicar interfaces Android y persistencia de datos. No se presenta como una aplicación publicada en Google Play.
