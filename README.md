# PracticaAutenticacionGutierrezR

Práctica de autenticación con Firebase en Android, adaptada del capítulo 2 del curso de AristiDevs a un proyecto de plantilla **Empty Views** en Kotlin.

## Funcionalidades

- Pantalla inicial con registro, login y botones decorativos de Google/Facebook.
- Registro de usuarios con `createUserWithEmailAndPassword`.
- Inicio de sesión con `signInWithEmailAndPassword`.
- Pantalla principal que muestra el correo del usuario autenticado.
- Cierre de sesión con `FirebaseAuth.signOut()`.
- Validación de correo y contraseña.
- Mensajes de error de Firebase mediante `Toast`.

## Configuración

- Paquete: `com.example.practicaautenticaciongutierrezr`
- Firebase Authentication: **Correo electrónico/contraseña** habilitado.
- El archivo `app/google-services.json` corresponde al proyecto Firebase de la práctica.

## Compilación

Requiere JDK 21 y Android SDK con API 37 instalada.

```powershell
.\gradlew.bat assembleDebug --no-daemon
```

El APK debug se genera en:

```text
app\build\outputs\apk\debug\app-debug.apk
```
