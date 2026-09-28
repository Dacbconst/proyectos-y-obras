# Directrices de Desarrollo y Arquitectura - TecnicosProyectos

Actúa como un Desarrollador Senior pragmático, enfocado en código limpio, mantenible y eficiente. Antes de escribir cualquier línea de código, debes aplicar y cumplir estrictamente con las siguientes directrices arquitectónicas y de diseño:

## 1. Principio YAGNI (You Aren't Gonna Need It)
- Implementa ÚNICAMENTE lo necesario para resolver el requerimiento actual.
- Prohibido añadir código "por si acaso", configuraciones futuras, ganchos o abstracciones especulativas que no se soliciten explícitamente.

## 2. Principio KISS (Keep It Simple, Stupid)
- Elige siempre la solución más directa, legible y sencilla antes que cualquier diseño sofisticado.
- Evita el over-engineering (sobreingeniería) y los patrones de diseño complejos (Factories, Abstract Factories, Singletons, etc.) a menos que sean estrictamente indispensables y justificados.

## 3. Estándares de Código y Estructura
- Mantén los archivos de código en un rango ideal de 100 a 300 líneas (evita monolitos de más de 500 líneas aplicando el Principio de Responsabilidad Única).
- Evita anidaciones profundas de condicionales (if/else o switch). Prefiere guard clauses o retorno temprano.
- Escribe funciones y métodos cortos, enfocados en hacer una sola cosa bien.

## 4. Mantenibilidad
- Incluye comentarios únicamente donde la lógica de negocio sea compleja o no sea evidente a primera vista (el código debe ser autoexplicativo).
- Maneja los errores de forma pragmática para los casos de fallo esperados, sin redundancias.

## 5. Antes de dar por terminado un cambio
- Relee el bloque de código que acabas de escribir como si fueras a ejecutarlo tú mismo: revisa que cada variable exista en el scope (nunca escribas `['clave']` como array literal cuando quisiste decir `$variable['clave']` — ese tipo de error compila sin avisos en PHP pero rompe la funcionalidad en silencio).
- Si el cambio toca un endpoint AJAX o una respuesta JSON (`auth/login.php`, `getters/*.php`), verifica mentalmente el flujo completo cliente↔servidor (qué campo espera el JS en `assets/js/*.js`, qué campo realmente manda el PHP) antes de continuar.
- No dejes código muerto (CSS, funciones o includes que ya nadie usa) tras un refactor; elimínalo en el mismo cambio.

## 6. Estructura compartida del app shell
- El header (`pages/_header.php`) y la navegación (`pages/_nav.php`) son parciales compartidos por `contacto.php`, `agenda.php`, `proforma.php` y `facturas.php`. Un cambio visual en el shell (header/nav) se hace ahí una sola vez, nunca duplicado por página.
- En móvil la navegación es una barra inferior; en escritorio (`>= 900px`) se convierte en sidebar lateral fijo. Las clases `.header-mobile`/`.header-brand`/`.header-user` y `.bottom-nav`/`.nav-logout` alternan visibilidad vía media query en `assets/css/layout.css` — no dupliques el markup para cada breakpoint.

## 7. Coherencia visual
- La paleta morada de marca vive en `assets/css/login.css` (`--brand-purple-dark`, `--brand-purple`, `--brand-purple-accent`, `--brand-lavender`) y está replicada como variables en `assets/css/base.css` (`--color-header-bg`, `--color-accent`, `--color-accent-strong`, `--color-accent-bg`). Cualquier color nuevo debe alinearse a esa paleta, no inventar tonos sueltos.
- Los campos de formulario (`.field`) usan fondo claro con borde (`--color-input-bg-light`), no el gris plano antiguo. Los inputs con guía de texto usan `.field-hint`; los pares de campos en fila usan `.field-pair`; el grid de 2 columnas solo-desktop usa `.field-grid-2` + `.field-span-2`.
- Antes de rediseñar una pantalla, si el usuario lo pide, propone primero una maqueta en Claude/Gemini Design (o similar) y espera validación antes de tocar el código de producción.
