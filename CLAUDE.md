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
- [x] **7. Switch de Canales en `Proyectos2`:** Implementado, pero **el mecanismo final quedó distinto al diseño original de esta sección** (ver nota abajo) — terminó en el sidebar global, no en pastillas de cabecera por módulo.

### Nota sobre el punto 7 (desviación del diseño original)
El switch de canales NO quedó como pastillas en la cabecera de cada módulo (como describe la sección 5 de este documento). Terminó como un control único en el sidebar de `Proyectos2` (`partials/sidebar.php`), global a toda la cuenta, persistido en `localStorage` como `window.CanalActivo` y propagado vía evento `canalCambio` (ver `index.php`). Hoy solo `get_dashboard.php` + `principal.js` lo consumen. **Si se retoma el punto 7 "de verdad" (según el diseño de la sección 5), falta**: conectar el mismo filtro a `get_avance.php`, `get_agenda.php`, `get_contactados.php` y `proformas_listar.php` del lado servidor (hoy esos devuelven todo sin filtrar por canal) y escuchar `canalCambio` en `agenda.js`, `contactados.js`, `proforma.js`, `estado-flujo.js` del lado cliente.

## 8. Pendientes abiertos (no cerrados, continuar en otra sesión)

- [ ] **GPS en Contacto de técnicos:** `TecnicosProyectos/getters/insert_contacto.php` ya acepta y guarda `latitud`/`longitud`, pero `assets/js/contacto.js` nunca los captura ni los envía (no hay `navigator.geolocation` en el formulario). Todo contacto creado por un técnico queda con esas columnas en `NULL`. Falta: agregar captura de GPS al enviar el formulario.
- [ ] **UX de escritorio para `TecnicosProyectos` (Proforma/Facturas):** Confirmado que hoy esas dos páginas no tienen layout propio de escritorio — es la vista de celular (`.app-main`, `max-width:760px`) centrada con espacio vacío alrededor. Se probó un mockup de rediseño (Artifact, sidebar + grilla de tarjetas + panel de detalle + 3 estados de vacío diferenciados) el 2026-10-06 — **el usuario lo rechazó explícitamente ("no me gustó para nada")**. No repetir ese mismo enfoque visual en la próxima sesión; preguntar primero qué dirección de diseño prefiere antes de volver a armar un mockup.
