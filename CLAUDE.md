# Directrices de Desarrollo, Arquitectura y Flujo Maestro

> **MANUAL MAESTRO PARA CLAUDE Y AGENTES DE IA:**  
> Este archivo consolida y detalla TODO el funcionamiento técnico, arquitectura de integración, ciclo de vida de 6 fases, reglas de negocio, aislamiento de ambientes y el mapeo exhaustivo archivo por archivo del ecosistema Pintuco:
> **PintucoApp (Móvil Android)** ↔ **Proyectos2 (Web Maestra)** ↔ **TecnicosProyectos (Web Técnicos)**.
> Documento complementario exhaustivo: [FLUJO_DATOS_PINTUCO.md](file:///c:/Users/DiegoAntonioConstant/Desktop/proyectos%20y%20obras/FLUJO_DATOS_PINTUCO.md).

---

## 1. Principios Fundamentales de Código

### A. YAGNI (You Aren't Gonna Need It)
- Implementa ÚNICAMENTE lo necesario para resolver el requerimiento actual.
- Prohibido añadir código "por si acaso", abstracciones especulativas o configuraciones futuras no solicitadas.

### B. KISS (Keep It Simple, Stupid)
- Elige siempre la solución más directa, legible y sencilla.
- Evita sobreingeniería (over-engineering) y patrones de diseño pesados si un procedimiento limpio y modular lo resuelve mejor.

### C. Estándares y Mantenibilidad
- Mantén los archivos en un rango ideal de 100 a 300 líneas (máximo 500 líneas).
- Evita anidaciones profundas de `if/else`. Usa retorno temprano (guard clauses).
- Escribe funciones concisas con responsabilidad única.
- Comentarios solo donde la regla de negocio no sea evidente a primera vista.
- **REGLA ABSOLUTA DE COMENTARIOS (2026-10-08, pedido explícito del usuario, aplica a TODO el repo sin excepción): un comentario es SIEMPRE una sola línea.** Nunca un bloque de varias líneas (ni `//`/`#` repetido línea tras línea, ni `/* ... */` multilínea, ni separadores decorativos tipo `// ----`). Si la explicación no entra en una línea, es señal de que sobra detalle — recórtala, no la partas en varias líneas.
- Manejo pragmático de errores sin redundancias innecesarias.

### D. Reglas de Calidad antes de Confirmar Código
- **Scope de variables en PHP:** Revisa mentalmente cada línea. Nunca dejes arrays sueltos sin variable (`['clave']` en lugar de `$variable['clave']`).
- **Contrato Cliente ↔ Servidor (AJAX / JSON):** Verifica que los nombres de campos que devuelve PHP coincidan exactamente con lo que espera JavaScript.
- **Cero código muerto:** No dejes imports, estilos CSS ni funciones obsoletas tras un refactor.

### E. Coherencia Visual (Paleta Morada de Marca)
- `TecnicosProyectos` utiliza la paleta morada definida en [assets/css/login.css](file:///c:/Users/DiegoAntonioConstant/Desktop/proyectos%20y%20obras/TecnicosProyectos/assets/css/login.css) y [assets/css/base.css](file:///c:/Users/DiegoAntonioConstant/Desktop/proyectos%20y%20obras/TecnicosProyectos/assets/css/base.css):
  * Primario / Header: `--brand-purple-dark` (`#3d1854`) / `--color-header-bg`
  * Acento / Botones: `--brand-purple` (`#582774`) / `--color-accent`
  * Hover / Focus: `--brand-purple-accent` (`#70388f`) / `--color-accent-strong`
  * Fondos tenues / Badges: `--brand-lavender` (`#e8daf5`) / `--color-accent-bg`
- Cualquier elemento visual nuevo DEBE alinearse con estas variables CSS, sin tonos aislados.

---

## 2. Aclaración Crítica de Ambientes: Desarrollo vs. Producción Real

> [!CAUTION]
> ### ¿Por qué el entorno local afectaba a Producción?
> 1. Tanto `TecnicosProyectos/config.php` como `Proyectos2/Pintuco/config.php` conectan directamente al servidor Azure MySQL:
>    * **Host:** `mysqlecuadorsf.mysql.database.azure.com`
>    * **Base de Datos:** `luckyec_pintuco` (**PRODUCCIÓN REAL EN LA NUBE**)
> 2. **NO EXISTE una base de datos staging o de pruebas separada en Azure.**
> 3. Aunque ejecutes `localhost/TecnicosProyectos` o `localhost/Proyectos2` desde tu carpeta local de desarrollo, **cualquier `INSERT`, `UPDATE` o `DELETE` impacta la base de datos de producción**.
> 4. Como la versión de producción de `Proyectos2` (la que usa la analista en su oficina) **aún no tiene el switch de canales**, cualquier agendamiento o visita que un técnico o desarrollador inserte de prueba **aparece inmediatamente en vivo en la pantalla de la analista y en los reportes de ventas de Kywi**.

---

### Solución Definitiva: Tablas de Prueba Aisladas (`_test`)

Para probar con 100% de libertad y seguridad sin afectar a la analista ni a los promotores:

#### 1. Estructura en HeidiSQL (Base `luckyec_pintuco`):
Ejecutar estas 3 sentencias una sola vez para clonar exactamente las tablas transaccionales:
```sql
CREATE TABLE IF NOT EXISTS insert_proyectos_contacto_test LIKE insert_proyectos_contacto;
CREATE TABLE IF NOT EXISTS insert_proforma_test LIKE insert_proforma;
CREATE TABLE IF NOT EXISTS insert_pago_factura_test LIKE insert_pago_factura;
```
*(Las tablas de catálogo `lvi_rutero` y `repositorio_usuario_tecnicos` NO se clonan porque son de solo lectura y se leen directo).*

#### 2. Configuración en Entorno Local (`config.php`):
Definir constantes en `TecnicosProyectos/config.php` y `Proyectos2/Pintuco/config.php`:
```php
// Entorno: 'local' (desarrollo con tablas test) o 'production' (producción real)
define('APP_ENV', 'local');

if (APP_ENV === 'local') {
    define('TABLA_CONTACTO', 'insert_proyectos_contacto_test');
    define('TABLA_PROFORMA',  'insert_proforma_test');
    define('TABLA_PAGOS',     'insert_pago_factura_test');
} else {
    define('TABLA_CONTACTO', 'insert_proyectos_contacto');
    define('TABLA_PROFORMA',  'insert_proforma');
    define('TABLA_PAGOS',     'insert_pago_factura');
}
```

#### 3. Aislamiento de Azure Blob Storage:
En modo `local`, [blob_upload.php](file:///c:/Users/DiegoAntonioConstant/Desktop/proyectos%20y%20obras/TecnicosProyectos/blob_upload.php) debe enviar las fotos a carpetas aisladas:
* `Proforma_Test/` en lugar de `Proforma/`
* `Factura_Test/` en lugar de `Factura/`
* `PagoFactura_Test/` en lugar de `PagoFactura/`

#### 4. Por qué esta arquitectura garantiza CERO afectación:
* **Producción:** La web maestra en el servidor oficial de Pintuco y las apps Android de los promotores en las tiendas siguen apuntando a las tablas estándar (`insert_proyectos_contacto`, etc.).
* **Desarrollo:** Tu servidor local de `TecnicosProyectos` y tu copia local de `Proyectos2` operan exclusivamente sobre las tablas `_test`.
* Puedes crear obras de prueba, agendar visitas ficticias, subir fotos dummy y facturar a plazos: **la analista real en producción jamás verá ninguno de estos registros**.
* Cuando todo esté validado, pasar a producción consiste simplemente en cambiar `APP_ENV` a `'production'`.

---

## 3. El Ciclo de Vida de 6 Fases (Pipeline de Obras)

Toda obra transita por 6 fases estrictas y secuenciales:

```
[Fase 1: Contacto] ──> [Fase 2: Agenda] ──> [Fase 3: Visita en Terreno]
 (Cliente y PDV)       (Cita y Técnico)     (Foto Evidencia en Blob)
                                                        │
[Fase 6: Liquidación] <── [Fase 5: Factura] <── [Fase 4: Auditoría Web]
 (Cuotas cubiertas)     (Directa o Plazos)    (Sello de Presupuesto)
```

### Fase 1: Contacto / Prospecto (Captación Comercial)
- **Objetivo:** Registrar datos del cliente (contacto, empresa, teléfono, dirección, ubicación GPS) vinculado a un Punto de Venta (`codigo_pdv`, `pdv`, `ciudad_pdv`).
- **Decisión de Visita:**
  * Si `no_requiere_visita = 'SI'`: No agenda cita física; la obra salta directamente a Fase 3 en espera de cotización/proforma.
  * Si requiere visita: Se propone fecha y hora tentativa.

### Fase 2: Agendamiento y Asignación de Cita
- **Control de Choques:** Valida que ningún técnico tenga dos visitas con menos de 45 minutos de separación.
- **Máquina de Estados de `estado_agenda`:**
  * `pendiente`: Cita sin técnico formal o fecha tentativa (origen móvil promotor).
  * `confirmado`: Cita con técnico formalmente asignado, fecha y hora (06:00 a 23:00).
  * `vencida`: La fecha de la visita ya pasó (`< CURDATE()`) y no tiene evidencia subida.
  * `asistio_pendiente_proforma`: Prórroga otorgada por la analista en `Proyectos2` ("Sí asistió") a una visita vencida para que el técnico pueda subir la foto sin alterar la fecha original.
  * `reagendada`: Estado temporal al asignar nueva fecha a una cita vencida.
  * `completada`: Transición automática inmediata al insertar la primera foto en `insert_proforma`.
  * `cancelada`: Cancelación formal.

### Fase 3: Visita Técnica en Terreno y Levantamiento
- **Objetivo:** El técnico inspecciona la obra física (sustrato, humedad, metros cuadrados, fases constructivas: obra gris, enlucido, pintura).
- **Multi-ronda:** Una misma obra (`id_agendamiento`) soporta múltiples rondas de visita en `insert_proforma`.
- **Evidencia obligatoria:** Foto tomada en terreno subida a Azure Blob (`app/AppPintuco/Inserts/Proforma/<unique>.png`).
- **Disparador:** La subida de la foto conmuta en automático `insert_proyectos_contacto.estado_agenda = 'completada'`.

### Fase 4: Auditoría Web y Negociación (`Proyectos2`)
- **Objetivo:** La analista en oficina audita el presupuesto técnico y la cotización de galones.
- **Acciones posibles en `update_proforma.php`:**
  * `guardar` (Aprobación): Sella `monto_validado`, `observaciones_auditoria` y `fecha_auditoria = NOW()`.
  * `rechazar_calidad`: Sella `estado_proforma = 'correccion_solicitada'`. La foto no se borra; el móvil o web muestra alerta al técnico para que repita la fotografía.
  * `cancelar_correccion`: Regresa el estado a `'en_proceso'`.
  * `rechazar`: Cierre irrevocable por descarte de la obra. Exige `motivo_cierre` obligatorio.

### Fase 5: Cierre Comercial y Facturación
- **Objetivo:** El cliente adquiere los productos de pintura en el PDV. Se sube foto de la factura a Azure Blob (`app/AppPintuco/Inserts/Factura/<unique>.png`).
- **Contrato de Cobro Híbrido:**
  * **Modalidad A: Pago Directo (Contado):** `plazo_meses <= 1` o vacío. Factura única que cubre el 100% de la cotización. `monto_total_factura` = total. `estado_pago = 'completado'`.
  * **Modalidad B: A Plazos (Crédito):** `plazo_meses > 1`. `monto_total_factura` = meta total fija (invariable). La primera entrega física se registra como cuota #1 en `insert_pago_factura` con `estado_pago = 'pendiente'`.

### Fase 6: Liquidación de Cuotas y Cierre
- **Registro de Abonos:** Cada pago se inserta en `insert_pago_factura` con recibo en Azure Blob (`app/AppPintuco/Inserts/PagoFactura/<unique>.png`).
- **Recálculo de Estado en `insert_proforma.estado_pago`:**
  * Se totaliza `SUM(monto_pago)` contra `monto_total_factura`.
  * Si la suma iguala o supera el total: `estado_pago = 'completado'`.
  * Si la suma es parcial: `estado_pago = 'en_proceso'`.
  * Si se interrumpe el plan: Acción `cerrar_plan_pago` sella `estado_pago = 'cerrado'` con `motivo_cierre_pago`.

---

## 4. Reglas de Negocio Específicas para `TecnicosProyectos`

1. **Sin Campo "Técnico" en Formulario de Contacto:**
   * En la UI de [contacto.php](file:///c:/Users/DiegoAntonioConstant/Desktop/proyectos%20y%20obras/TecnicosProyectos/pages/contacto.php) **NO se debe mostrar un desplegable para seleccionar técnico**.
   * El técnico es quien tiene la sesión iniciada. El backend asigna automáticamente:
     `$usuario = $_SESSION['usuario'];` y `$tecnico = $_SESSION['usuario'];`.
2. **Nacimiento de Visita Confirmada:**
   * Al auto-asignarse el técnico desde la sesión, si se ingresa fecha y hora la visita **nace como `'confirmado'`**, nunca `'pendiente'`.
3. **Catálogo de Puntos de Venta (PDVs):**
   * A diferencia del promotor móvil (limitado a locales de Kywi de su región), los técnicos cubren obras de **todos los puntos de venta** (`WHERE activar = 'SI'` en `lvi_rutero`).
4. **Permisos Cruzados de Autorización y Lectura:**
   * Un promotor puede registrar un contacto y la analista asignar la visita al técnico `DIEGO`.
   * Por tanto, los endpoints de `TecnicosProyectos` (`agenda.php`, `proforma.php`, `facturas.php`, `confirmar_factura.php`, `update_proforma.php`, `get_pagos_factura.php`) **NUNCA deben filtrar solo por `usuario = ?`**.
   * DEBEN USAR: `(p.usuario = ? OR c.tecnico = ? OR c.usuario = ?)`.
5. **Recálculo Obligatorio de `estado_pago` tras Registrar Cuotas:**
   * Al insertar una cuota en `insert_pago_factura.php`, se DEBE sumar `SUM(monto_pago)` y actualizar `insert_proforma.estado_pago` a `'en_proceso'` o `'completado'`.

---

## 5. Estrategia del Switch de Canales en la Web Maestra (`Proyectos2`)

### El Problema de Mezcla de Datos
* Promotores de Kywi miden ventas en mostrador retail.
* Técnicos de obra miden prospección técnica y especificación de producto en obras de gran escala.
* Si se mezclan en la misma tabla sin discriminar, los técnicos aparecerían en el ranking de tiendas de `principal.js` y `get_avance.php`, corrompiendo la analítica histórica de ventas de Kywi.

### Implementación Arquitectónica (Cero DDL):
Los endpoints de `Proyectos2` (`get_dashboard.php`, `get_avance.php`, `get_agenda.php`, `get_contactados.php`, `proformas_listar.php`) reciben el parámetro `$_GET['canal']`:

```php
$canal = isset($_GET['canal']) ? $_GET['canal'] : 'promotores';

$whereCanal = "";
if ($canal === 'tecnicos') {
    // Solo obras y visitas generadas por técnicos
    $whereCanal = " AND c.usuario IN (SELECT usuario_tecnico FROM repositorio_usuario_tecnicos WHERE activo = 1)";
} elseif ($canal === 'promotores') {
    // Solo obras de promotores de tienda Kywi
    $whereCanal = " AND c.usuario NOT IN (SELECT usuario_tecnico FROM repositorio_usuario_tecnicos WHERE activo = 1)";
}
// Si $canal === 'todos', se omite la condición para auditoría gerencial completa
```

En la UI de `Proyectos2`:
Selector de pastillas en cabecera:
`[ 🏪 Tiendas (Promotores) ]` (por defecto) · `[ 🛠️ Obras (Técnicos) ]` · `[ 🌐 Todo ]`.

---

## 6. Mapeo Exhaustivo Archivo por Archivo y Función por Función

### Módulo 1: Contacto / Prospecto
| Sistema | Archivo | Funciones Clave | Comportamiento |
|---|---|---|---|
| **Android** | `ContactoFragment.java` | `guardarContacto()`, `validarFormulario()` | Solo locales Kywi. Inserta en SQLite con `tecnico = NULL`. |
| **Backend Móvil** | `AppPintuco/insert_contacto.php` | `FuncionesSamsung::insertProyectosContacto()` | Guarda en BD con `estado_agenda = 'pendiente'`. |
| **Web Maestra** | `Proyectos2/.../agenda-crear.js` | `guardarVisita()`, `actualizarMinHora()` | Analista asigna promotor y técnico manualmente. |
| **Web Maestra** | `Proyectos2/getters/insert_contacto.php` | Inserción + validación choques | Verifica que el técnico no tenga cita <45 min. |
| **Web Técnicos** | `TecnicosProyectos/pages/contacto.php` | Formulario `pdv-combo` | Todos los PDVs activos. No pide técnico. |
| **Web Técnicos** | `TecnicosProyectos/getters/insert_contacto.php` | Inserción directa | `$usuario = $tecnico = $usuario_sesion`. Nace `'confirmado'`. |

### Módulo 2: Agenda y Calendario
| Sistema | Archivo | Funciones Clave | Comportamiento |
|---|---|---|---|
| **Android** | `AgendaFragment.java` + `AdapterAgenda.java` | `cargarAgenda()`, `aplicarEstado()` | Acordeón con estados y badge "Completada". |
| **Web Maestra** | `Proyectos2/.../agenda.js` | `AgendaEntrar()`, `renderizarSemana()` | Calendario interactivo semanal por técnico. |
| **Web Maestra** | `Proyectos2/getters/get_agenda.php` | Updates perezosos + `SELECT` | Marca `vencida` y `completada` al consultar. |
| **Web Maestra** | `Proyectos2/getters/update_agenda.php` | `guardar`, `marcar_asistio`, `cancelar` | Prórroga `asistio_pendiente_proforma`. |
| **Web Técnicos** | `TecnicosProyectos/pages/agenda.php` | `cargarAgenda()`, `renderEventos()` | Vista semanal y diaria del técnico. |
| **Web Técnicos** | `TecnicosProyectos/getters/get_agenda.php` | Consulta de citas | `WHERE activar = 'SI' AND (tecnico = ? OR usuario = ?)`. |

### Módulo 3: Proforma y Visita en Terreno
| Sistema | Archivo | Funciones Clave | Comportamiento |
|---|---|---|---|
| **Android** | `ProformaFragment.java` + `UtilidadesProforma.java` | `onGuardarVisita()`, `onEvidencia()` | Rondas múltiples. Convierte fotos a Base64. |
| **Backend Móvil** | `AppPintuco/insert_proforma.php` | `FuncionesSamsung::insertProforma()` | Sube foto a Blob `Proforma/` y actualiza cita. |
| **Web Maestra** | `Proyectos2/.../proforma.js` | `ProformaRecargar()`, `abrirAuditoria()` | Modal de auditoría de cotización y foto. |
| **Web Maestra** | `Proyectos2/getters/update_proforma.php` | `guardar`, `rechazar_calidad`, `rechazar` | `correccion_solicitada` o cierre con `motivo_cierre`. |
| **Web Técnicos** | `TecnicosProyectos/pages/proforma.php` | `cargarProformas()`, `guardarRonda()` | Permite al técnico registrar nuevas rondas. |
| **Web Técnicos** | `TecnicosProyectos/getters/insert_proforma.php` | `subir_foto_blob()` + `INSERT` | Sube a Blob `Proforma/` y pasa cita a `'completada'`. |

### Módulo 4: Facturación y Cobranza a Plazos
| Sistema | Archivo | Funciones Clave | Comportamiento |
|---|---|---|---|
| **Android** | `FacturasFragment.java` + `AdapterFacturas.java` | `onConfirmarFactura()`, `onAdjuntarPago()` | Modalidad directa vs cuotas con fotos. |
| **Backend Móvil** | `AppPintuco/insert_pago_factura.php` | `FuncionesSamsung::insertPagoFactura()` | Sube cuota a Blob `PagoFactura/`. |
| **Web Maestra** | `Proyectos2/.../factura.js` | `FacturaRecargar()`, `abrirCuotas()` | Liquidación de cuotas y montos cobrados. |
| **Web Maestra** | `Proyectos2/getters/update_proforma.php` | `cerrar_plan_pago` | Sella `estado_pago = 'cerrado'` con motivo. |
| **Web Técnicos** | `TecnicosProyectos/pages/facturas.php` | `cargarFacturas()`, `guardarCuota()` | Lista facturas y formulario de abono. |
| **Web Técnicos** | `TecnicosProyectos/getters/confirmar_factura.php` | Confirmación inicial | Sube factura a Blob `Factura/`. |
| **Web Técnicos** | `TecnicosProyectos/getters/insert_pago_factura.php` | Inserción + **Recálculo** | Suma abonos y actualiza `insert_proforma.estado_pago`. |

### Módulo 5: Dashboard y Reportes
| Sistema | Archivo | Funciones Clave | Comportamiento |
|---|---|---|---|
| **Web Maestra** | `Proyectos2/.../principal.js` | `DashboardRecargar()`, `renderPromotores()` | Embudo 5 fases y Top Promotores. |
| **Web Maestra** | `Proyectos2/getters/get_dashboard.php` | Consulta cruda agregada | Agendamientos, proformas y pagos. |
| **Web Maestra** | `Proyectos2/getters/get_avance.php` | Resumen por mercaderista | `GROUP BY c.usuario` y metas de tienda. |
| **Web Maestra** | `Proyectos2/.../estado-flujo.js` | `EstadoFlujoRecargar()` | Tablero visual de obras en fase 1 a 5. |

---

## 7. Checklist de Ajustes Pendientes en Código

- [x] **1. Tablas Test en HeidiSQL:** Creadas `insert_proyectos_contacto_test`, `insert_proforma_test` e `insert_pago_factura_test` en Azure `luckyec_pintuco` (2026-10-06, vía script PHP directo con mysqli — verificadas con la misma cantidad de columnas que las reales y 0 filas).
- [x] **2. Conexión Aislada en Dev:** `APP_ENV` + constantes `TABLA_CONTACTO`/`TABLA_PROFORMA`/`TABLA_PAGOS`/`BLOB_SUFIJO` ya en `TecnicosProyectos/config.php` y `Proyectos2/Pintuco/config.php`. Todos los getters de ambos proyectos migrados a usar las constantes (ya no hay nombres de tabla quemados en SQL real, solo quedan 2 menciones dentro de un comentario/docblock en `Proyectos2/Pintuco/getters/update_proforma.php`, no son SQL ejecutable). `APP_ENV` está en `'local'` en ambos archivos ahora mismo — recordar volver a `'production'` antes de cualquier despliegue real.
- [x] **3. Permisos Cruzados en `confirmar_factura.php`:** Ya usa `(p.usuario = ? OR c.tecnico = ? OR c.usuario = ?)` con JOIN a `TABLA_CONTACTO`.
- [x] **4. Permisos Cruzados en `update_proforma.php`:** Mismo criterio aplicado al SELECT de verificación antes de rechazar/cerrar plan de pago.
- [x] **5. Recálculo en `insert_pago_factura.php`:** Tras cada INSERT exitoso, suma `SUM(monto_pago)` contra `monto_total_factura` y sella `estado_pago` en `'completado'`/`'en_proceso'` (nunca pisa `'cerrado'`, que es terminal).
- [x] **6. Visibilidad en `get_pagos_factura.php`:** Las dos queries (facturas y pagos) ya filtran con `(usuario = ? OR c.tecnico = ? OR c.usuario = ?)`, la de pagos con JOIN nuevo a `TABLA_CONTACTO` vía `id_agendamiento` (la tabla de pagos no tiene columna `tecnico` propia).
- [x] **7. Switch de Canales en `Proyectos2`:** Implementado y **ya conectado de punta a punta** (ver nota abajo) — el mecanismo final quedó distinto al diseño original de esta sección (sidebar global, no pastillas de cabecera por módulo), pero funcionalmente completo.

### Nota sobre el punto 7 (estado real, actualizado 2026-10-07)
El switch de canales NO quedó como pastillas en la cabecera de cada módulo (como describía originalmente la sección 5). Es un control único en el sidebar de `Proyectos2` (`partials/sidebar.php`), anclado al fondo (sidebar-nav con `flex:1` empuja el switch abajo), global a toda la cuenta, persistido en `localStorage` como `canalActivo` y propagado vía evento `canalCambio` (ver `index.php`).

**Ya conectado a los 7 módulos** (antes solo Dashboard):
- Server-side (`$_GET['canal']` + helper compartido `Proyectos2/Pintuco/getters/_canal.php`): `get_agenda.php`, `get_contactados.php`, `get_avance.php`, `get_dashboard.php`.
- Client-side (cargan todo una vez y recalculan en el navegador, mismo criterio que `principal.js`; `proformas_listar.php` expone `usuarios_tecnicos` para que lo consuman): `proforma.js`, `factura.js`, `estado-flujo.js`.

**Bugs reales encontrados y corregidos en el camino:**
1. **Condición de carrera** en los 7 archivos JS: todos leían `window.CanalActivo` en su carga inicial, pero esa variable recién se define dentro de `$(document).ready(...)` en `index.php`, que corre *después* de que esos scripts ya se ejecutaron. Si el analista dejaba el switch en "Técnicos" y recargaba la página, todo volvía a "Promotores" hasta volver a tocar el switch. Fix: fallback a `window.CanalActivo || localStorage.getItem('canalActivo') || 'promotores'` en los 7 archivos.
2. **`NOT IN` + `NULL` en `_canal.php`:** si `repositorio_usuario_tecnicos.usuario_tecnico` trajera algún `NULL`, `x NOT IN (subquery con NULL)` se vuelve `UNKNOWN` para TODAS las filas (lógica de 3 valores de SQL) y el canal "promotores" devolvería 0 filas siempre. Blindado con `AND usuario_tecnico IS NOT NULL` en ambas subconsultas.

**Diseño de la UI (pedido explícito del usuario, 2026-10-07):** se rediseñó de pastillas con emoji (🏪🛠️🌐) a un segmented control real con SVG trazo, relabel "Obras"→"Técnicos" (claridad), y **se quitó la opción "Todo" por ahora** (solo quedan Promotores/Técnicos — Promotores es el default, coincide con lo que está en producción; Todo se puede reagregar más adelante, la lógica de backend para `canal=todos` sigue intacta sin botón que la dispare).

**Nota operativa para pruebas:** con `APP_ENV='local'`, las tablas `_test` están casi vacías (solo lo que se prueba manualmente) — es normal que "Promotores" no muestre nada si no se ha insertado ningún dato de promotor de prueba; no es un bug del switch.

## 8. Otros cambios de esta sesión (2026-10-07)

- [x] **Navbar legacy eliminado en `Proyectos2`:** la barra azul superior de Bootstrap (`<nav class="navbar navbar-fixed-top">` + `nav.php`, un solo link "Principal") era 100% redundante con el sidebar. Se quitó de `index.php`, se ajustó `.app-wrapper` (ya no compensa los 50px que ocupaba) y se borró `nav.php`.
- [x] **Bug del combo de PDV en `TecnicosProyectos/pages/contacto.php` (se quedaba abierto):** en `components.css`, `.pdv-combo-panel { display: block; }` le ganaba en cascada al `display:none` nativo del atributo `[hidden]` (misma especificidad, pero la regla de autor siempre gana sobre la del user-agent) — el panel se quedaba visualmente abierto sin importar lo que hiciera el JS. Afectaba móvil y desktop por igual (un solo CSS compartido). Fix: `.pdv-combo-panel[hidden] { display: none; }`.
- [x] **Rediseño del header de `TecnicosProyectos` (móvil + desktop), aplicado a producción:** `_header.php` + `layout.css` ahora tienen header de 2 filas (marca+usuario arriba, título de página grande abajo — 23px/800 móvil, 22px/800 desktop). Se quitó el `backdrop-filter: blur()` que no tenía función real (no había nada translúcido detrás). `--header-height` (en `base.css`) subió de 68px a 128px para que el sidebar de escritorio siga alineado bajo el header de 2 filas.
- [x] **Rediseño del módulo Proforma móvil de `TecnicosProyectos`, aplicado a producción:** `pages/proforma.php` + `assets/js/proforma.js` + `assets/css/components.css`, replicando la funcionalidad real de la app Android (`ProformaFragment.java`/`AdapterProforma.java`/`UtilidadesProforma.java`, no diseño inventado) — pills Activos/Vencidas/Completados + buscador, alerta ámbar cuando la última ronda está en `correccion_solicitada`/`rechazado`, campo de foto con dropzone+preview+"Cambiar foto", "Acompañamiento técnico" como radio Sí/No (antes textarea libre), diálogo nativo `<dialog>` para "Cerrar proceso" con contador 0/250. Se agregaron a `get_proforma.php` las mismas 2 transiciones perezosas de estado (`vencida`/`completada`) que ya tenía `get_agenda.php` — eran necesarias para que los pills reflejen el estado real sin depender de pasar antes por Agenda.

## 10. Sesión 2026-10-08 — Facturas, verificación end-to-end, auditoría de canal y GPS

- [x] **Rediseño completo de Facturas en `TecnicosProyectos`, aplicado a producción:** `pages/facturas.php` + `assets/js/facturas.js` + `getters/get_pagos_factura.php` (agregadas columnas `fecha_agendamiento`/`no_requiere_visita` al SELECT), replicando `FacturasFragment.java`/`AdapterFacturas.java`/`FacturaConPagos.java` reales (no diseño inventado) — pills **A plazos/Directo/Todo** (la categoría real es `plazo_meses > 0`, **antes el filtro comparaba contra `estado_pago`, que nunca vale eso — el filtro nunca funcionó**), secciones Activos/Completado/Cerrados dentro de "A plazos", selector de mes (`<input type="month">`, simplificado vs. el NumberPicker nativo de Android), "Adjuntar otra factura" sin límite con número de cuota y fecha autocalculados (ya no se le piden al técnico, igual que el app), visor de foto de solo lectura con zoom, mismo `<dialog>` de cierre que ya tenía Proforma. Se probó y descartó una marca de agua tipo `ImageMark.java` — el usuario pidió explícitamente no implementarla.
- [x] **`TecnicosProyectos/includes/blob_upload.php` — `require_once` del SDK de Azure movido adentro de `subir_foto_blob()`:** antes cargaba al incluir el archivo, así que *cualquier* guardado en Proforma/Factura (incluso sin foto nueva) tronaba con fatal error en cualquier entorno sin el SDK instalado (p. ej. local). Ahora solo se exige el SDK en el momento real en que hay una foto que subir. **No afectaba producción** (ahí el SDK ya existe) — es una mejora de poder desarrollar/probar en local, no una corrección de un bug visible para un técnico real.
- [x] **Verificación end-to-end real contra Azure (tablas `_test`):** login con cuenta de técnico temporal (creada y borrada en la misma sesión), flujo completo contacto→proforma→factura a plazos→cuota→recálculo→cierre de plan→candado anti-reapertura, y permiso cruzado probado con 3 técnicos (uno asignado por terceros que sí ve la obra, uno sin relación que no ve nada). Todo limpiado al final (0 filas en `_test`, 0 cuentas temporales, `git status` limpio).
- [x] **Sidebar de `Proyectos2` ya no se desplaza con el scroll:** `#sidebar` tenía `align-items:stretch` heredado del `.app-wrapper` flex, así que en páginas más altas que la pantalla el sidebar crecía con el contenido y había que bajar para ver su final. Fix: `position: sticky; top: 0; height: 100vh; align-self: flex-start;` en `style.css`.
- [x] **Etiquetas "Promotor"/"Top promotores" dinámicas según el canal activo:** mecanismo central `actualizarEtiquetasCanal()` en `index.php` (corre al cargar y en cada evento `canalCambio`) que recorre `.etiqueta-canal` y cambia el texto leyendo `data-promotor`/`data-tecnico`. Aplicado en los 6 módulos (Principal, Agendamientos — filtro, modal crear, modal editar —, Contactados — filtro, columna, opción "mercaderistas" —, Estado de Flujo, Factura, Proforma). `avance.js`/`avance.css` quedaron sin tocar: están **completamente desconectados**, ningún `.php` los incluye — es código muerto, no afecta nada visible.
- [x] **Campo duplicado en Agendamientos, canal Técnicos:** el filtro que antes decía "Promotor" (filtra por `usuario`, quién registró) y el que ya existía "Técnico asignado" (filtra por `tecnico`, quién ejecuta) quedaban diciendo lo mismo. **Pedido explícito del usuario:** en canal Técnicos esto es irrelevante ("somos el técnico acá") — ambos filtros se ocultan por completo con la clase `.oculto-en-tecnicos` (mismo mecanismo central, extendido con `$('.oculto-en-tecnicos').toggle(!esTecnico)`).
- [x] **Switch del sidebar renombrado:** "Tiendas" → "Promotores" (pedido explícito del usuario).
- [x] **KPIs "Puntos de venta"/"Top PDV" del Dashboard — confirmado que NO cambian de nombre:** el usuario confirmó que los técnicos también reportan por PDV igual que un promotor: mismo KPI, mismo cálculo, el switch de canal ya se encarga de que los datos salgan solo de técnicos cuando ese canal está activo. No hay cambio de código pendiente acá.
- [x] **Combo de PDV "lagueado"/pegado en celular, `TecnicosProyectos/pages/contacto.php` — causa real encontrada y arreglada:** el panel usaba `position: fixed` con coordenadas calculadas **una sola vez** en JS (`getBoundingClientRect()`) justo antes de enfocar el buscador — en celular eso abre el teclado, cambia el viewport, y como nada recalculaba la posición el panel quedaba desalineado del botón. El fix anterior (`[hidden]{display:none}`, sección 8) solo tapaba que no se cerraba visualmente; esta es la causa de fondo del desfase. Fix: `position: absolute` anclado a `.pdv-combo` (que ya tenía `position:relative`, nada lo recorta) en vez de `fixed` + JS — se reacomoda solo sin recalcular nada. Simplificó también el JS (se borró todo el cálculo de ancho/izquierda). **Prioridad declarada por el usuario de aquí en adelante: diseño y funcionalidad del lado MÓVIL de `TecnicosProyectos` por sobre escritorio.**
- [x] **GPS en Contacto de técnicos — resuelto:** `assets/js/contacto.js` ahora pide `navigator.geolocation.getCurrentPosition()` apenas carga la página (no al enviar, para que el permiso ya esté resuelto y no retrase el guardado) y manda `latitud`/`longitud` en el POST a `insert_contacto.php` (que ya los aceptaba desde antes). Si el técnico niega el permiso o el navegador no lo soporta, se manda sin coordenadas — no bloquea el registro. **Efecto visible confirmado:** sin esto, las visitas de técnicos aparecían completas en el calendario de Agendamientos de `Proyectos2` pero **sin pin en el mapa** (`pintarMapa()` en `agenda.js` hace `if (!lat || !lng) return;` — no truena, solo no dibuja el marcador). Con el fix, el pin ya debería aparecer para visitas nuevas.
- [x] **Confirmado (no hubo que tocar nada):** un agendamiento creado por un técnico ya aparece como tarjeta normal en el calendario de Agendamientos de `Proyectos2` — mismo template, mismo filtro de canal por `usuario`, `agenda.js` ya manda `&canal=...` y se refresca solo en `canalCambio`.

## 11. Pendientes abiertos (no cerrados, continuar en otra sesión)

- [ ] **UX de escritorio para `TecnicosProyectos` (Proforma/Facturas):** Proforma y Facturas ya tienen rediseño propio de **móvil** (ver secciones 8 y 10), pero ninguna tiene layout propio de **escritorio** — es la vista de celular (`.app-main`, `max-width:760px`) centrada con espacio vacío alrededor. Se probó un mockup de escritorio el 2026-10-06 — **el usuario lo rechazó ("no me gustó para nada")**. No repetir ese enfoque visual sin preguntar antes. **Además, el usuario declaró explícitamente (2026-10-08) que la prioridad ahora es móvil, no escritorio** — no retomar esto sin que el usuario lo pida.
- [ ] **Campo "Técnico asignado" en `Proyectos2` sin validar:** `agendaEditTecnico` (en `components/agendamiento/partials/modal-edicion.php`) es un `<input type="text">` libre — la analista escribe el `usuario_tecnico` a mano, sin autocompletar ni validar contra `repositorio_usuario_tecnicos`. `update_agenda.php` guarda el valor tal cual (sin trim, sensible a mayúsculas). Un typo deja la cita invisible del lado del técnico (`tecnico = ?` no hace match). Falta: convertirlo en un selector real poblado desde `repositorio_usuario_tecnicos` (igual que se hizo con el combo de PDV en `TecnicosProyectos/pages/contacto.php`).
- [ ] **El dropdown "Promotor" del modal "Nueva Visita" en Agendamientos** (`agendaCrearPromotor`) ya tiene la etiqueta dinámica, pero su lista de opciones sigue siendo solo promotores reales — no se evaluó si en canal Técnicos ese selector debería ofrecer técnicos también. No se tocó por ser un cambio funcional, no solo de texto.
- [ ] **Código muerto confirmado, no limpiado:** `Proyectos2/Pintuco/components/avance/assets/avance.js` y `avance.css` no están incluidos por ningún `.php` — nadie los ve. Limpiar si se quiere, no es urgente.

- [ ] **Probar en dispositivo real todo lo de la sección 12:** nada de lo hecho en la segunda parte del 2026-10-08 se probó en celular ni en navegador (solo `php -l`, `node --check` y balance de llaves CSS). Prioridad: cámara/foto de Proforma, selector de PDV a pantalla completa con teclado, agenda móvil con calendario fijo, header nuevo y el sidebar de `Proyectos2`.
- [ ] **`Proyectos2`, visitas `completada` ocultas por defecto en el calendario:** `getters/get_agenda.php` excluye `estado_agenda = 'completada'` si no se filtra por Estado. Una visita de técnico con su primera foto de proforma "desaparece" de Agendamientos (pasó con el id 3 de las tablas `_test`). Falta decidir si en canal Técnicos deben seguir visibles.
- [ ] **Formulario de Contacto (técnicos):** quedaron fuera el borrador automático (sessionStorage, igual que las fotos) y la confirmación del botón "Cancelar" en escritorio, que borra todo con un clic.
- [ ] **`TecnicosProyectos/CLAUDE.md` y `GEMINI.md` están desactualizados:** el de la raíz es el vigente (llega a la sección 12); el de la carpeta termina en la sección 7. Unificar o borrar la copia.
- [ ] **Hipótesis sin confirmar:** que el celular recargue la página al volver de la cámara (por eso la tarjeta de Proforma se cerraba). Se blindó con borrador en `sessionStorage`, pero no se reprodujo.
- [ ] **Dependencias del hosting a confirmar:** la conexión persistente (`p:` en `db_connect.php`) y la regla `<If>` de caché en `.htaccess` asumen Apache y un límite de conexiones de Azure MySQL holgado.

## 12. Sesión 2026-10-08 (segunda parte) — Rediseño móvil, rendimiento y sidebar de `Proyectos2`

Los diseños se hicieron primero en Claude Design (lienzo privado: https://claude.ai/artifact/JgTzvaRBNQa9vE6XY3FQkj) y luego se pasaron al código.

### `TecnicosProyectos`

- [x] **Contacto:** al guardar sale un aviso ("Visita registrada") y redirige a Agenda; formulario compacto (sin subtítulo, sin etiquetas de sección, sin textos de ayuda); validación en línea con las mismas reglas que `insert_contacto.php`; `min` de hoy en la fecha; hora obligatoria si hay fecha (y al revés, también validado en el servidor); botón "Guardando…"; estado del GPS ("Ubicación lista" / "Sin ubicación · Reintentar"); inputs de 16 px (iOS no hace zoom). El selector de PDV es una hoja a pantalla completa en móvil (usa `interactive-widget=resizes-content`, así el teclado no tapa la lista) y se quitó la fila "Seleccionar PDV".
- [x] **Bug corregido en `includes/functions.php`:** `buscar_conflicto_horario()` consultaba `insert_proyectos_contacto` (producción) con el nombre quemado; ahora usa `TABLA_CONTACTO` (también afecta `update_agenda.php`).
- [x] **Proforma:** se quitó "Acompañamiento técnico" (el servidor guarda siempre `'SI'`); tarjeta rediseñada (recuadro de fecha, empresa, estado y barra Visita → Validada → Facturada) con acciones en línea "Registrar visita" y "Enviar factura"; placeholder "Escribe aquí…". **La factura se habilita desde la primera visita con evidencia** (distinto del app Android, que exige `monto_validado`); `getters/confirmar_factura.php` valida en el servidor que haya evidencia, que la obra no esté cerrada y que no esté ya facturada.
- [x] **Fotos:** el campo es un `<label>` con el input oculto dentro; la foto se reduce a base64 al elegirla y se guarda un borrador en `sessionStorage` (foto, tarjeta abierta y formulario abierto) por si el celular recarga la página al volver de la cámara. API en `camera-upload.js`: `activarCampoFoto(campo, clave, alFallar)`; Facturas usa el mismo campo.
- [x] **Header nuevo (móvil y escritorio):** barra blanca con logo de Pintuco (`assets/img/pintuco-header.png`, 168×78), título en la página con Plus Jakarta Sans, menú de usuario con scrim (el único "Cerrar sesión" en móvil), franja de "Sin conexión" y un solo `h1` por página. En móvil usa `display:contents`, así la barra de 56 px queda fija y el título se desplaza.
- [x] **Agenda móvil:** calendario fijo y compacto (una semana, flechas de semana) que se despliega a mes completo con un toque en el título (asa para contraer); tarjetas con la empresa como título y el PDV debajo; paleta morada en vez del azul Pintuco.
- [x] **Navegación inferior:** `.nav-logout` ya no aparece en móvil (bug de especificidad: `.bottom-nav a` le ganaba al `display:none`); etiquetas de 12 px.
- [x] **Errores de red y avisos:** `Api` distingue "sin internet", "sin servidor" y "tardó demasiado" (timeout de 60 s en envíos); `mostrarToast()` confirma cada guardado; `mostrarError()` hace scroll hasta el aviso.
- [x] **Rendimiento al navegar entre módulos:** sesión con `read_and_close` (`auth_guard.php`, sin cola de peticiones); conexión persistente `p:`; `actualizar_estados_agenda()` junta las dos actualizaciones perezosas en una sola ida; fuentes por `<link>` con `preconnect`; `asset()` agrega `?v=fecha` a CSS/JS/imágenes y `.htaccess` los cachea un año solo si traen `?v`; las pantallas de Agenda, Proforma y Facturas muestran al instante lo último visto (`Api.getConCache`, `sessionStorage`, se borra al entrar al login); fundido corto entre páginas (`@view-transition`).
- [x] **Utilidades CSS:** `.btn-ghost` (el `.btn-outline` se oculta en móvil a propósito) y `[hidden] { display: none !important; }` global.

### `Proyectos2`

Decisión del usuario: se mantiene la paleta propia de `Proyectos2` (periwinkle `--brand` / `--brand-dark` / azul `--brand-light`); no se aplica el morado de Técnicos. El rediseño del sidebar fue solo mover y decorar, sin tocar funciones.

- [x] **Sidebar:** degradado con `--brand-top` (única variable nueva), íconos SVG de trazo, ítem activo como píldora translúcida, switch de Canal más alto (58 px), nombre de la marca a 12 px para que no lo tape el botón. Se mantienen la posición del usuario, de Cuenta, el orden de las pestañas y los ids/atributos que usa el JS (`#sidebar`, `#sidebarCollapse`, `#selectCuenta`, `data-toggle="section"`, `data-canal`).
- [x] **Barra superior del contenido (`.main-topbar` en `index.php`):** título de la sección activa (se actualiza al cambiar de pestaña), chip "Canal: Técnicos/Promotores" (se actualiza en `actualizarEtiquetasCanal()`) y el logo de Pintuco (`assets/img/pintuco-header.png`; hay que subirlo al servidor).
- [x] **Menú recogido (solo escritorio):** franja de íconos de 72 px que se despliega encima del contenido al pasar el mouse o con Tab, sin empujarlo; un clic en una zona vacía lo deja abierto y fijo (`localStorage.sidebarActivo`). Se probó y se descartó recoger el menú con cualquier clic en el contenido: hacía saltar la pantalla y costaba clics. En móvil `.active` sigue significando "revelar".
- [x] **Hueco bajo el sidebar corregido:** el pie `© PromoLucky` estaba fuera del `.app-wrapper`; ahora es `.app-footer` dentro de `.main-content`.

### Herramientas

- [x] **Skill `ui-ux-pro-max`** instalada con `npm install -g ui-ux-pro-max-cli` y `uipro init --ai claude` (queda en `.claude/skills/`, ignorado por git). Se revisó su contenido y no hay nada malicioso. Python no está disponible en esta máquina (`python` abre el acceso directo de la Microsoft Store), así que sus búsquedas por CLI no corren; se usa su lista de reglas.
