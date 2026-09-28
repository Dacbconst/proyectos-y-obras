# Directrices de Desarrollo y Arquitectura

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
- Si el cambio toca un endpoint AJAX o una respuesta JSON, verifica mentalmente el flujo completo cliente↔servidor (qué campo espera el JS, qué campo realmente manda el PHP) antes de continuar.
- No dejes código muerto (CSS, funciones o imports que ya nadie usa) tras un refactor; elimínalo en el mismo cambio.

## 6. Coherencia visual
- Este proyecto (`TecnicosProyectos`) tiene una paleta morada de marca definida en `assets/css/login.css` (`--brand-purple-dark`, `--brand-purple`, `--brand-purple-accent`, `--brand-lavender`) y replicada como variables en `assets/css/base.css` (`--color-header-bg`, `--color-accent`, `--color-accent-strong`, `--color-accent-bg`). Cualquier color nuevo debe alinearse a esa paleta, no inventar tonos sueltos.
- Antes de rediseñar una pantalla, si el usuario lo pide, propone primero una maqueta (no vayas directo a producción con cambios visuales grandes sin validación).
