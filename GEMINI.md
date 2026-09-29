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

## 7. Arquitectura de Integración y Reglas de Negocio (Pintuco)
> **LECTURA OBLIGATORIA:** Consulta el documento maestro [FLUJO_DATOS_PINTUCO.md](file:///c:/Users/DiegoAntonioConstant/Desktop/proyectos%20y%20obras/FLUJO_DATOS_PINTUCO.md) para detalles exhaustivos de funciones, tablas, estados y endpoints del ecosistema (`PintucoApp` móvil ↔ `Proyectos2` web maestra ↔ `TecnicosProyectos` web técnicos).

### Reglas Clave de Integración:
1. **El Ciclo de Vida de 6 Fases:**
   - Fase 1 (Contacto / Prospecto) -> Fase 2 (Agendamiento y Cita) -> Fase 3 (Visita Técnica en Terreno) -> Fase 4 (Auditoría / Validación Presupuesto) -> Fase 5 (Facturación Directa o Plazos) -> Fase 6 (Liquidación de Cuotas y Cierre).
2. **Regla de Puntos de Venta (PDVs):** La limitación que tiene la app móvil (que solo muestra locales de `COMERCIAL KYWI S.A.` por región del promotor) **NO APLICA a `TecnicosProyectos`**. Los técnicos cubren obras de **todos los puntos de venta** (`WHERE activar = 'SI'` en `lvi_rutero`).
3. **Formulario de Contacto Técnico:** En `TecnicosProyectos`, **NO pedir campo de técnico** en la UI. El técnico es el usuario logueado en sesión. El backend autoasigna `$tecnico = $usuario` para que la visita nazca confirmada.
4. **Relación `usuario` vs `tecnico` y Permisos Cruzados:** En `TecnicosProyectos`, toda consulta de lectura y autorización debe contemplar visitas asignadas por terceros:
   - Para agendamientos: `WHERE activar = 'SI' AND (tecnico = ? OR usuario = ?)`
   - Para proformas y facturas: `WHERE p.id = ? AND (p.usuario = ? OR c.tecnico = ? OR c.usuario = ?)`
5. **Recálculo de `estado_pago` en Facturación a Plazos:**
   - Cada vez que se inserte un pago en `insert_pago_factura`, se DEBE sumar las cuotas pagadas y actualizar `insert_proforma.estado_pago` a `'en_proceso'` o `'completado'` si iguala/supera `monto_total_factura`.
6. **Switch de Canales en la Web Maestra (`Proyectos2`):**
   - Para no contaminar los KPIs y rankings de promotores Kywi (`get_dashboard.php`, `get_avance.php`), las consultas de `Proyectos2` soportan el switch `&canal=promotores|tecnicos|todos` discriminando mediante `repositorio_usuario_tecnicos`.
7. **Seguridad de Ambientes y Tablas de Prueba Aisladas (`_test`):**
   - La base de datos en Azure (`luckyec_pintuco`) está en **PRODUCCIÓN REAL**.
   - Aunque se ejecute en local, `config.php` se conecta a Azure en vivo. Como `Proyectos2` en producción aún no filtra canal, **cualquier dato de prueba aparecería de inmediato en la pantalla de la analista**.
   - Para probar con 100% de seguridad sin afectar a producción, crear en HeidiSQL:
     ```sql
     CREATE TABLE IF NOT EXISTS insert_proyectos_contacto_test LIKE insert_proyectos_contacto;
     CREATE TABLE IF NOT EXISTS insert_proforma_test LIKE insert_proforma;
     CREATE TABLE IF NOT EXISTS insert_pago_factura_test LIKE insert_pago_factura;
     ```
   - En `config.php` local, apuntar a estas tablas vía constantes (`TABLA_CONTACTO`, `TABLA_PROFORMA`, `TABLA_PAGOS`). Así producción queda 100% aislada e intacta.
