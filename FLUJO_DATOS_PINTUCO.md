# Documentación Técnica Integral: Flujo de Datos, Ciclo de Vida y Arquitectura de Integración
**Ecosistema:** PintucoApp (Móvil) ↔ Proyectos2 (Web Maestra) ↔ TecnicosProyectos (Web Técnicos)

> **GUÍA MAESTRA PARA DESARROLLADORES Y AGENTES DE IA (CLAUDE / GEMINI):**  
> Este documento contiene el mapeo exhaustivo y definitivo de todo el ecosistema de Proyectos y Obras de Pintuco. Detalla el ciclo de vida completo de 6 fases, la estructura de base de datos en Azure MySQL, las llamadas a Azure Blob Storage, las funciones y archivos exactos de cada sistema (móvil, web maestra y técnicos), las diferencias de negocio y la estrategia del **Switch de Canales** para preservar la analítica de ventas.

---

## 1. Infraestructura y Repositorio Común de Datos

Los tres aplicativos operan concurrentemente sobre el mismo repositorio en la nube de Azure:

* **Servidor MySQL:** `mysqlecuadorsf.mysql.database.azure.com` (Azure Database for MySQL en **PRODUCCIÓN REAL**)
* **Base de Datos:** `luckyec_pintuco`
* **Almacenamiento de Archivos (Fotos y Evidencias):** Azure Blob Storage
  * Cuenta: `luckyecuadorweb`
  * Contenedor: `app`
  * URL Base pública: `https://luckyecuadorweb.blob.core.windows.net/app/AppPintuco/Inserts/`
  * Subcarpetas de blobs:
    * `Proforma/`: Fotos de levantamiento técnico en obra, estado del sustrato y fachada.
    * `Factura/`: Fotos de la factura de compra emitida en el Punto de Venta (PDV).
    * `PagoFactura/`: Recibos y comprobantes de abonos/cuotas en ventas a crédito/plazos.

### Tablas Centrales del Modelo

| Tabla | Rol en el Ecosistema | Columnas Clave |
|---|---|---|
| **`insert_proyectos_contacto`** | Obra, cliente comercial y cita de agenda. | `id` (PK), `codigo_pdv`, `pdv`, `ciudad_pdv`, `usuario`, `tecnico`, `contacto`, `empresa`, `direccion`, `telefono`, `fecha_agendamiento`, `hora`, `estado_agenda`, `no_requiere_visita`, `activar`. |
| **`insert_proforma`** | Rondas de visita técnica, evidencia fotográfica, auditoría de cotización y factura madre. | `id` (PK), `id_agendamiento` (FK), `codigo_pdv`, `usuario`, `fecha_proforma`, `estado_proforma`, `evidencia`, `caracteristica_visita`, `acompanamiento_tecnico`, `foto_factura`, `monto_validado`, `monto_total_factura`, `plazo_meses`, `estado_pago`, `motivo_cierre`, `motivo_cierre_pago`, `fecha_auditoria`. |
| **`insert_pago_factura`** | Pagos parciales / cuotas de facturas a plazos. | `id` (PK), `id_proforma` (FK -> `insert_proforma.id`), `id_agendamiento`, `codigo_pdv`, `usuario`, `numero_cuota`, `monto_pago`, `foto_pago`, `fecha_pago`, `observacion`. |
| **`lvi_rutero`** | Catálogo maestro de locales comerciales, Puntos de Venta (PDVs) y cadenas. | `pos_id`, `pos_name`, `subchannel`, `city`, `region`, `mercaderista`, `activar`, `habilitado`. |
| **`repositorio_usuario_tecnicos`** | Credenciales y perfiles de los técnicos de obra autorizados. | `id` (PK), `usuario_tecnico`, `nombre_completo`, `password`, `activo`, `ultimo_login`. |

---

## 2. El Ciclo de Vida Completo de 6 Fases (Pipeline de Obras)

Toda obra transita por un flujo de 6 fases secuenciales que se reflejan en tiempo real en los 3 sistemas:

```
[Fase 1: Contacto] ──> [Fase 2: Agenda] ──> [Fase 3: Visita en Obra]
 (Prospecto y PDV)    (Fecha, Hora, Técnico)    (Foto Evidencia en Blob)
                                                         │
[Fase 6: Liquidación] <── [Fase 5: Factura] <── [Fase 4: Auditoría Web]
(Cuotas cubiertas 100%)  (Directa o a Plazos)   (Validación Presupuesto)
```

### Detalle de cada Fase:

#### Fase 1: Contacto / Prospecto (Captación Comercial)
* **Objetivo:** Registrar los datos del cliente y de la obra vinculados a un Punto de Venta.
* **Datos:** PDV (`codigo_pdv`, `pdv`, `ciudad_pdv`), Contacto, Empresa, Email, Dirección, Lat/Long, Teléfonos.
* **Decisión de Visita:**
  * Si marca `no_requiere_visita = 'SI'`: No se fija fecha ni hora; el contacto salta directo a Fase 3 (espera de proforma).
  * Si requiere visita: Se propone fecha y hora tentativa.

#### Fase 2: Agendamiento y Asignación de Visita
* **Objetivo:** Coordinar la fecha física, hora y el técnico que irá a la obra.
* **Regla de Choques de Horario:** El sistema valida que ningún técnico tenga dos visitas con menos de 45 minutos de diferencia.
* **Máquina de Estados de la Agenda (`estado_agenda` en `insert_proyectos_contacto`):**
  * `pendiente`: Visita con fecha/hora tentativa o sin técnico asignado (origen app móvil).
  * `confirmado`: Visita con fecha, hora (06:00 a 23:00) y técnico formalmente asignados.
  * `vencida`: La fecha de la visita ya pasó (`< CURDATE()`) y no tiene evidencia subida. (Calculado perezosamente en consultas).
  * `asistio_pendiente_proforma`: Prórroga otorgada por la analista web ("Sí asistió") a una visita vencida para que el técnico pueda subir la foto sin mover la fecha original.
  * `reagendada`: Estado exclusivo cuando una visita que estaba vencida recibe una nueva fecha.
  * `completada`: Se activa automáticamente cuando existe al menos una fila con foto de evidencia válida en `insert_proforma`.
  * `cancelada`: Cancelación formal del proceso.

#### Fase 3: Visita Física en Obra y Levantamiento Técnico
* **Objetivo:** El técnico asiste a la obra para evaluar sustratos, humedad, áreas y requerimientos de pintura.
* **Multi-ronda:** Una misma obra (`id_agendamiento`) puede tener múltiples rondas de visita en `insert_proforma`.
* **Datos levantados:**
  * `caracteristica_visita`: Tipo de pintura/trabajo (fachada, interiores, pisos, etc.).
  * `acompanamiento_tecnico`: `'SI'` o `'NO'`.
  * `evidencia`: Foto de la obra subida a Azure Blob (`app/AppPintuco/Inserts/Proforma/<unique>.png`).
  * `fase_actual`: Estado físico de la obra (fase 1 obra gris, fase 2 enlucido, fase 3 pintura).
* **Efecto automático:** Al subirse la foto, `insert_proyectos_contacto.estado_agenda` pasa a `'completada'`.

#### Fase 4: Negociación y Auditoría en la Web Maestra (`Proyectos2`)
* **Objetivo:** La analista en oficina revisa la proforma y cotización técnica.
* **Acciones posibles en `update_proforma.php`:**
  * **`guardar` (Aprobación):** Sella `monto_validado`, `observaciones_auditoria` y `fecha_auditoria = NOW()`.
  * **`rechazar_calidad` (Corrección de Foto):** Pasa `estado_proforma = 'correccion_solicitada'`. La foto no se borra; el móvil muestra alerta al promotor/técnico para que vuelva a tomarla.
  * **`cancelar_correccion`:** Vuelve el estado a `'en_proceso'`.
  * **`rechazar` ("Cerrar Proceso"):** Cierre irreversible de la negociación cuando el cliente descarta la compra. Exige `motivo_cierre` obligatorio.

#### Fase 5: Cierre Comercial y Facturación
* **Objetivo:** El cliente realiza la compra física en el PDV.
* **Comprobante:** Se fotografía la factura de compra emitida en el local y se sube a Azure Blob (`app/AppPintuco/Inserts/Factura/<unique>.png`).
* **Contrato de Cobro Híbrido:**
  * **Modalidad A: Pago Directo (Contado):**
    * `plazo_meses <= 1` o vacío.
    * Factura única que cubre el 100% de la cotización.
    * `monto_total_factura` = total cotizado y facturado.
    * `estado_pago = 'completado'`.
  * **Modalidad B: A Plazos (Crédito / Entregas Parciales):**
    * `plazo_meses > 1`.
    * `monto_total_factura` = meta cotizada fija (no se vuelve a alterar).
    * La primera entrega se guarda como cuota #1 en `insert_pago_factura`.
    * `estado_pago = 'pendiente'`.

#### Fase 6: Cobranza de Cuotas y Liquidación Total
* **Objetivo:** En ventas a plazos, registrar cada cuota hasta cubrir el 100% de la venta.
* **Registro de Cuotas (`insert_pago_factura`):**
  * Campos: `id_proforma`, `id_agendamiento`, `codigo_pdv`, `usuario`, `numero_cuota`, `monto_pago`, `foto_pago` (Blob `PagoFactura/<unique>.png`), `fecha_pago`.
* **Recálculo Automático de `estado_pago` en `insert_proforma`:**
  * `sumaPagos = SUM(monto_pago) WHERE id_proforma = ?`
  * Si `round(sumaPagos * 100) >= round(monto_total_factura * 100)` -> `estado_pago = 'completado'`.
  * Si `sumaPagos > 0` pero menor al total -> `estado_pago = 'en_proceso'`.
  * Si el cliente cancela el crédito -> Acción web `cerrar_plan_pago` sella `estado_pago = 'cerrado'` con `motivo_cierre_pago`.

---

## 3. Mapeo Exhaustivo Archivo por Archivo y Función por Función

### MÓDULO 1: CONTACTO / PROSPECTO

| Entorno | Archivo | Funciones Clave | Qué hace / Reglas |
|---|---|---|---|
| **Móvil Android** | `ContactoFragment.java` | `guardarContacto()`, `validarFormulario()`, `buscarPDV()` | Captura datos de contacto y PDV (solo locales Kywi). Guarda localmente en SQLite con `usuario = promotor` y `tecnico = NULL`. |
| **Backend Móvil** | `AppPintuco/Inserts/insert_contacto.php` | `FuncionesSamsung::insertProyectosContacto()` | Recibe JSON de SQLite. Inserta en `insert_proyectos_contacto` con `estado_agenda = 'pendiente'`. |
| **Web Maestra** | `Proyectos2/Pintuco/components/agendamiento/assets/agenda-crear.js` | `guardarVisita()`, `validarFormulario()`, `actualizarMinHora()` | Permite a la analista crear contactos manuales. Asigna promotor y técnico obligatorios. |
| **Web Maestra** | `Proyectos2/Pintuco/getters/insert_contacto.php` | Bloque principal de inserción | Valida choques de 45m del técnico y guarda en BD. |
| **Web Técnicos** | `TecnicosProyectos/pages/contacto.php` + `assets/js/contacto.js` | Formulario con combobox flotante (`pdv-combo`) | **NO pide técnico.** Lista **todos los PDVs** activos de cualquier cadena. |
| **Web Técnicos** | `TecnicosProyectos/getters/insert_contacto.php` | Bloque principal | Autoasigna `$tecnico = $usuario` (técnico logueado). Si trae fecha y hora, nace directamente como `'confirmado'`. |

---

### MÓDULO 2: AGENDA Y CALENDARIO SEMANAL

| Entorno | Archivo | Funciones Clave | Qué hace / Reglas |
|---|---|---|---|
| **Móvil Android** | `AgendaFragment.java` + `AdapterAgenda.java` | `cargarAgenda()`, `aplicarEstado()`, `filtrarPorPills()` | Muestra visitas en acordeón. Pinta badges por estado. Si `visitado = true`, muestra badge "Completada". |
| **Web Maestra** | `Proyectos2/Pintuco/components/agendamiento/assets/agenda.js` | `AgendaEntrar()`, `renderizarSemana()`, `cargarOpcionesTecnico()` | Renderiza calendario semanal interactivo. Permite arrastrar citas, reasignar técnico y hora. |
| **Web Maestra** | `Proyectos2/Pintuco/getters/get_agenda.php` | Ejecución de updates perezosos + `SELECT` | Pasa visitas viejas a `'vencida'` y visitas con proforma a `'completada'`. Filtra por fecha y técnico. |
| **Web Maestra** | `Proyectos2/Pintuco/getters/update_agenda.php` | Acciones: `guardar`, `cancelar`, `marcar_asistio`, `eliminar` | Asigna técnico a citas pendientes, valida choques <45m. Con `marcar_asistio`, pasa citas vencidas a `'asistio_pendiente_proforma'`. |
| **Web Técnicos** | `TecnicosProyectos/pages/agenda.php` + `assets/js/agenda.js` | `cargarAgenda()`, `renderEventos()` | Muestra las citas del técnico logueado. |
| **Web Técnicos** | `TecnicosProyectos/getters/get_agenda.php` | Updates perezosos + `SELECT` | **DEBE BUSCAR:** `WHERE activar = 'SI' AND (tecnico = ? OR usuario = ?)`. |

---

### MÓDULO 3: PROFORMA Y GESTIÓN DE VISITAS TÉCNICAS

| Entorno | Archivo | Funciones Clave | Qué hace / Reglas |
|---|---|---|---|
| **Móvil Android** | `ProformaFragment.java` + `AdapterProforma.java` | `onGuardarVisita()`, `onEvidencia()`, `cargarVisitas()` | Muestra tarjetas por agendamiento. Soporta múltiples rondas. Convierte fotos tomadas con cámara a Base64. |
| **Móvil Android** | `UtilidadesProforma.java` | `deCursorAJSONObject()`, `mostrarDialogoCerrarProceso()` | Serializa rondas y gestiona diálogos de cierre y validación. |
| **Backend Móvil** | `AppPintuco/Inserts/insert_proforma.php` | `FuncionesSamsung::insertProforma()`, `uploadBlobSample()` | Inserta la fila en `insert_proforma` y sube la foto a Azure Blob `Proforma/<unique>.png`. |
| **Web Maestra** | `Proyectos2/Pintuco/components/proforma/assets/proforma.js` | `ProformaRecargar()`, `getFase()`, `abrirAuditoria()` | Interfaz de auditoría. Permite ver la foto ampliada, sellar monto presupuestado o pedir corrección. |
| **Web Maestra** | `Proyectos2/Pintuco/getters/proformas_listar.php` | Consulta con `LEFT JOIN` | Lista agendamientos con sus rondas de proforma. |
| **Web Maestra** | `Proyectos2/Pintuco/getters/update_proforma.php` | Acciones: `guardar`, `rechazar_calidad`, `cancelar_correccion`, `rechazar` | Aplica auditoría o cierra el proceso con `motivo_cierre`. |
| **Web Técnicos** | `TecnicosProyectos/pages/proforma.php` + `assets/js/proforma.js` | `cargarProformas()`, `guardarRonda()` | Permite al técnico registrar nuevas rondas con fotos de evidencia tomadas en el navegador o móvil. |
| **Web Técnicos** | `TecnicosProyectos/getters/insert_proforma.php` | `subir_foto_blob()` + `INSERT` | Sube foto a Blob `Proforma/` y actualiza `insert_proyectos_contacto.estado_agenda = 'completada'`. |

---

### MÓDULO 4: FACTURACIÓN Y PLANES DE PAGO A PLAZOS

| Entorno | Archivo | Funciones Clave | Qué hace / Reglas |
|---|---|---|---|
| **Móvil Android** | `FacturasFragment.java` + `AdapterFacturas.java` | `onConfirmarFactura()`, `onAdjuntarPago()`, `onCerrarFactura()` | Muestra facturas en acordeón. Permite adjuntar fotos de facturas y cuotas subsiguientes. |
| **Móvil Android** | `UtilidadesProforma.java` | `actualizarEstadoPago()`, `calcularEstadoPago()` | Suma cuotas de `insert_pago_factura` contra `monto_total_factura` y actualiza `estado_pago = 'completado'`. |
| **Backend Móvil** | `AppPintuco/Inserts/insert_pago_factura.php` | `FuncionesSamsung::insertPagoFactura()` | Inserta cuota en `insert_pago_factura` y sube comprobante a Blob `PagoFactura/<unique>.png`. |
| **Web Maestra** | `Proyectos2/Pintuco/components/factura/assets/factura.js` | `FacturaRecargar()`, `totalFacturadoDe()`, `abrirCuotas()` | Calcula facturación híbrida (Directo = `monto_total_factura`, Plazos = suma de `monto_pago`). |
| **Web Maestra** | `Proyectos2/Pintuco/getters/get_pagos_factura.php` | Consulta de cuotas | Lista cuotas filtradas por `id_proforma`. |
| **Web Maestra** | `Proyectos2/Pintuco/getters/update_proforma.php` | Acción: `cerrar_plan_pago` | Sella `estado_pago = 'cerrado'` con `motivo_cierre_pago` cuando el cliente interrumpe el plan. |
| **Web Técnicos** | `TecnicosProyectos/pages/facturas.php` + `assets/js/facturas.js` | `cargarFacturas()`, `guardarCuota()` | Permite al técnico ver facturas de sus obras y subir abonos con foto. |
| **Web Técnicos** | `TecnicosProyectos/getters/confirmar_factura.php` | Validación + `UPDATE insert_proforma` | Confirma factura inicial (`directo` vs `a_plazos`). Sube foto a Blob `Factura/`. |
| **Web Técnicos** | `TecnicosProyectos/getters/insert_pago_factura.php` | Inserción de cuota + **Recálculo de estado** | Inserta cuota en `insert_pago_factura` y **debe recalcular** `insert_proforma.estado_pago`. |

---

### MÓDULO 5: ANALÍTICA, DASHBOARD Y AVANCE

| Entorno | Archivo | Funciones Clave | Qué hace / Reglas |
|---|---|---|---|
| **Web Maestra** | `Proyectos2/Pintuco/components/principal/assets/principal.js` | `DashboardRecargar()`, `renderKpis()`, `renderPromotores()` | Calcula embudo de conversión de 5 fases, montos negociados vs facturados y ranking Top Promotores. |
| **Web Maestra** | `Proyectos2/Pintuco/getters/get_dashboard.php` | Consulta cruda de agendamientos y pagos | Devuelve agendamientos con proformas y pagos para cálculo en el cliente. |
| **Web Maestra** | `Proyectos2/Pintuco/getters/get_avance.php` | Resumen global y agrupación por mercaderista | Agrupa `GROUP BY c.usuario AS mercaderista` calculando % de avance y PDVs completados. |
| **Web Maestra** | `Proyectos2/Pintuco/components/estado-flujo/assets/estado-flujo.js` | `EstadoFlujoRecargar()`, `getFase()` | Pipeline visual con tarjetas de obras según su fase (1 a 5). |

---

## 4. La Estrategia del Switch de Canales en la Web Maestra (`Proyectos2`)

### El Problema de la Mezcla de Datos
Los promotores de tienda Kywi y los técnicos de obra tienen naturalezas y metas completamente distintas:
* **Promotor de Tienda:** Mide flujo de mostrador en un local Kywi específico, cantidad de clientes derivados y metas de mercaderista retail.
* **Técnico de Obra:** Mide metros cuadrados levantados, especificaciones técnicas de producto, obras de construcción y ventas de gran escala en cualquier cadena o ferretería.

Si un técnico ingresa citas con `usuario = 'DIEGO'`:
1. Diego aparecerá en el ranking de *"Top Promotores"* de `principal.js`.
2. Diego aparecerá como un "mercaderista" en `get_avance.php` con 0 PDVs Kywi asignados, distorsionando los promedios de la cadena.
3. La analista de retail no sabrá si una venta provino de una tienda Kywi o de una prospección directa de campo.

### La Solución Arquitectónica: Switch Conmutador de Canal
En el encabezado de [Proyectos2](file:///c:/Users/DiegoAntonioConstant/Desktop/proyectos%20y%20obras/Proyectos2):

```
┌────────────────────────────────────────────────────────────────────────┐
│  Canal: [ 🏪 Tiendas (Promotores) ]  [ 🛠️ Obras (Técnicos) ]  [ 🌐 Todo ]  │
└────────────────────────────────────────────────────────────────────────┘
```

#### Implementación Técnica (Cero DDL en Azure):
Los endpoints de `Proyectos2` (`get_dashboard.php`, `get_avance.php`, `get_agenda.php`, `get_contactados.php`, `proformas_listar.php`) reciben `$_GET['canal']`:

```php
$canal = isset($_GET['canal']) ? $_GET['canal'] : 'promotores'; // 'promotores' por defecto

$whereCanal = "";
if ($canal === 'tecnicos') {
    // Solo registros creados por técnicos autorizados
    $whereCanal = " AND c.usuario IN (SELECT usuario_tecnico FROM repositorio_usuario_tecnicos WHERE activo = 1)";
} elseif ($canal === 'promotores') {
    // Solo registros creados por promotores de tienda Kywi
    $whereCanal = " AND c.usuario NOT IN (SELECT usuario_tecnico FROM repositorio_usuario_tecnicos WHERE activo = 1)";
}
// Si $canal === 'todos', no se filtra por canal (vista consolidada)
```

* **Modo `promotores` (Por defecto):** La analista ve exactamente lo mismo de siempre. Cero regresiones y cero contaminación de KPIs.
* **Modo `tecnicos`:** La analista supervisa exclusivamente las obras, visitas, cotizaciones y facturación del equipo técnico.
* **Modo `todos`:** Consolidado para la gerencia general de Pintuco.

---

## 5. Checklist de Ajustes para Paridad Total en `TecnicosProyectos`

Para que [TecnicosProyectos](file:///c:/Users/DiegoAntonioConstant/Desktop/proyectos%20y%20obras/TecnicosProyectos) cumpla a la perfección el flujo:

- [ ] **Ajuste 1: Autorización en `confirmar_factura.php`:**
  Reemplazar `WHERE id = ? AND usuario = ?` por validación cruzada:
  `WHERE p.id = ? AND (p.usuario = ? OR c.tecnico = ? OR c.usuario = ?)`
- [ ] **Ajuste 2: Autorización en `update_proforma.php`:**
  Permitir al técnico asignado cerrar la proforma (`motivo_cierre`) aunque la cita haya sido originada por un promotor.
- [ ] **Ajuste 3: Recálculo de `estado_pago` en `insert_pago_factura.php`:**
  Tras insertar cada cuota en `insert_pago_factura`, sumar cuotas pagadas y actualizar `insert_proforma.estado_pago` a `'en_proceso'` o `'completado'`.
- [ ] **Ajuste 4: Visibilidad en `get_pagos_factura.php`:**
  Permitir consultar cuotas de todas las visitas asignadas al técnico (`c.tecnico = ? OR c.usuario = ? OR p.usuario = ?`).
- [ ] **Ajuste 5: Switch en `Proyectos2`:**
  Implementar el parámetro `canal` en los getters y el selector de canal en la interfaz de la web maestra.

---

## 6. Política Estricta de Ambientes de Prueba

> [!CAUTION]
> La base de datos `mysqlecuadorsf.mysql.database.azure.com` (`luckyec_pintuco`) está en **PRODUCCIÓN REAL**.
> 
> No insertar datos de prueba ficticios en Azure:
> * Contaminan los reportes oficiales de ventas de Pintuco.
> * Se descargan a los teléfonos de los promotores en las tiendas durante su sincronización matutina.
> * Generan archivos basura en el Azure Blob Storage corporativo.

Para pruebas, utilizar el switch en `config.php`:
```php
define('APP_ENV', 'local'); // 'local' para XAMPP; 'production' solo para despliegue final
```
