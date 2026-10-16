<?php
	require $_SERVER["DOCUMENT_ROOT"].'/App/XploraEcuador/includes/config.php';
	require $_SERVER["DOCUMENT_ROOT"].'/App/XploraEcuador/includes/db_connect.php';
	require $_SERVER["DOCUMENT_ROOT"].'/App/XploraEcuador/includes/functions.php';

	sec_session_start();

	$tipo_cuenta = $_SESSION['tipo'];
	$username = $_SESSION['username'];
	$user_id = $_SESSION['user_id'];

	if (isset($username)) {

		// Cuentas habilitadas para ESTE usuario (igual consulta que usaba el
		// hub original) — esto es lo que llena el <select> "Cuenta" del sidebar.
		$consulta = "SELECT cuenta, detalle FROM vi_cuentas_por_usuario WHERE id_usuario=? AND activo=1 ORDER BY detalle;";
		$sql = $mysqli->prepare($consulta);
		$sql->bind_param('s', $user_id);
		$sql->execute();
		$sql->store_result();
		$sql->bind_result($cuenta_cod, $cuenta_detalle);
		$cuentas_disponibles = [];
		while ($sql->fetch()) {
			$cuentas_disponibles[$cuenta_cod] = $cuenta_detalle;
		}
		$sql->close();

		// Datos reales de sesión del hub XploraEcuador. Aún no hay fuente para
		// nombre completo / último ingreso / actualizado, así que se omiten
		// hasta que se identifique de dónde traerlos.
		$usuario_actual = [
			'nombre' => $username,
		];

		// ================================================================
		// MÓDULOS YA CONSTRUIDOS
		// No toda cuenta de $cuentas_disponibles tiene su carpeta lista todavía.
		// Esto solo dice "si elige X, dónde está su código"; el listado real de
		// cuentas que ve cada usuario sale de $cuentas_disponibles (arriba).
		// Para agregar una cuenta nueva ya construida: una línea aquí.
		// ================================================================
		$modulos_implementados = [
			'PINTUCO' => 'Pintuco',
		];

		$cuenta_actual = isset($_GET['cuenta']) ? strtoupper($_GET['cuenta']) : null;
		$cuenta_habilitada = $cuenta_actual !== null
			&& isset($cuentas_disponibles[$cuenta_actual])
			&& isset($modulos_implementados[$cuenta_actual]);

		$cuenta_dir = null;
		$secciones = [];
		$tabs = [];

		if ($cuenta_habilitada) {
			$cuenta_dir = __DIR__.'/'.$modulos_implementados[$cuenta_actual];
			require $cuenta_dir.'/includes/mock_data.php';

		}
?>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Proyectos y Obras</title>

	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<link href="/App/XploraEcuador/Proyectos/assets/bootstrap2/css/style_nav.css" rel="stylesheet">
	<?php
		// Cache-busting: sin esto el navegador sigue sirviendo el style.css
		// viejo en caché aunque se suba uno nuevo al servidor (causa real de
		// "subí el cambio y no se ve") — el query param cambia solo cuando
		// el archivo realmente cambia, forzando a descargarlo de nuevo.
		$style_v = @filemtime(__DIR__.'/style.css') ?: time();
	?>
	<link rel="stylesheet" href="style.css?v=<?= $style_v ?>">
</head>
<body>

	<div class="app-wrapper">

		<?php include __DIR__.'/partials/sidebar.php'; ?>

		<div class="main-content">

			<?php if ($cuenta_habilitada): ?>

				<header class="main-topbar">
					<h1 id="topbarTitulo"><?= htmlspecialchars($secciones[0]['label']) ?></h1>
					<span class="topbar-canal" id="topbarCanal" data-canal="promotores">
						<svg data-icono="promotores" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M3 9.5 12 3l9 6.5"></path><path d="M5 9.5V20a1 1 0 0 0 1 1h3v-6h6v6h3a1 1 0 0 0 1-1V9.5"></path></svg>
						<svg data-icono="tecnicos" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M14.7 6.3a4 4 0 0 0-5.4 5.4l-6 6a1.5 1.5 0 0 0 2.1 2.1l6-6a4 4 0 0 0 5.4-5.4l-2.1 2.1-2-2z"></path></svg>
						<span id="topbarCanalTexto">Canal: Promotores</span>
					</span>
					<img class="topbar-logo" src="assets/img/pintuco-header.png" alt="Pintuco" width="64" height="30">
				</header>

				<?php foreach ($secciones as $i => $seccion): ?>
				<div class="section-pane <?= $i === 0 ? 'active' : '' ?>" id="sec-<?= $seccion['id'] ?>">
					<?php if ($seccion['id'] === 'principal'): ?>

						<div class="content-panel">
							<?php include __DIR__.'/partials/tab-avance.php'; ?>
						</div>

					<?php else: ?>

						<div class="content-panel">
							<?php
								$placeholder_message = null;
								include $cuenta_dir.'/'.$seccion['componente'];
							?>
						</div>

					<?php endif; ?>
				</div>
				<?php endforeach; ?>

			<?php elseif ($cuenta_actual !== null): ?>

				<div class="content-panel">
					<?php
						$placeholder_label = $cuentas_disponibles[$cuenta_actual] ?? $cuenta_actual;
						$placeholder_message = htmlspecialchars($placeholder_label).' — Próximamente';
						include __DIR__.'/partials/tab-placeholder.php';
					?>
				</div>

			<?php else: ?>

				<div class="content-panel">
					<?php
						$placeholder_label = 'cuenta';
						$placeholder_message = 'Elija una cuenta en el sidebar para comenzar';
						include __DIR__.'/partials/tab-placeholder.php';
					?>
				</div>

			<?php endif; ?>

			<footer class="app-footer">&copy; PromoLucky <?php echo date("Y");?></footer>

		</div>

	</div>

	<div class="visor-foto" id="visorFoto" role="dialog" aria-modal="true" aria-label="Foto del punto de venta" hidden>
		<button type="button" class="visor-foto-cerrar" id="visorFotoCerrar" aria-label="Cerrar">&times;</button>
		<img id="visorFotoImg" alt="Foto del punto de venta">
	</div>

	<dialog class="confirmar" id="confirmarDialog" role="alertdialog" aria-labelledby="confirmarTitulo" aria-describedby="confirmarTexto">
		<form method="dialog">
			<span class="confirmar-icono" id="confirmarIcono" aria-hidden="true"></span>
			<h4 class="confirmar-titulo" id="confirmarTitulo"></h4>
			<p class="confirmar-texto" id="confirmarTexto"></p>
			<div class="confirmar-acciones">
				<button type="submit" value="" class="confirmar-btn confirmar-btn-cancelar" id="confirmarCancelar">Cancelar</button>
				<button type="submit" value="ok" class="confirmar-btn confirmar-btn-ok" id="confirmarOk">Aceptar</button>
			</div>
		</form>
	</dialog>

	<script src="https://code.jquery.com/jquery-1.12.0.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script>
		$(document).ready(function () {
			// Un solo mapa sección→función de recarga: lo usan tanto el botón
			// "Actualizar" manual como la navegación automática de abajo, para
			// no mantener dos listas separadas que se puedan desincronizar.
			function funcionRefrescoDe(idSeccion) {
				return {
					// AgendaEntrar (no AgendaRecargar a secas): además de traer
					// datos frescos, resetea el calendario a la semana de hoy
					// — sin esto quedaba parqueado en la última fecha que se
					// hubiera visto, obligando a apretar "Hoy" a mano cada vez
					// que se volvía a esta sección (pedido explícito del
					// usuario 2026-07-16).
					'sec-agendamientos': window.AgendaEntrar,
					'sec-contactados': window.ContactadosRefrescar,
					'sec-proforma': window.ProformaRecargar,
					'sec-factura': window.FacturaRecargar,
					'sec-estado-flujo': window.EstadoFlujoRecargar,
					'sec-usuarios-tecnicos': window.UsuariosTecnicosRecargar,
					'sec-principal': window.DashboardRecargar
				}[idSeccion];
			}

$('.sidebar-nav a[data-toggle="section"]').on('click', function (e) {
				e.preventDefault();
				$('.sidebar-nav li').removeClass('active');
				$(this).parent('li').addClass('active');
				$('#topbarTitulo').text($.trim($(this).text()));
				$('.section-pane').removeClass('active');
				$($(this).attr('href')).addClass('active');

				// Auto-actualizar la sección a la que se acaba de entrar, para
				// que los datos siempre estén frescos sin depender de que el
				// analista se acuerde de pulsar "Actualizar" cada vez.
				var fnAuto = funcionRefrescoDe($(this).attr('href').replace('#', ''));
				if (fnAuto) fnAuto();

				window.dispatchEvent(new Event('resize'));
			});

			// La hamburguesa ahora vive dentro del propio sidebar (ver
			// partials/sidebar.php) y .active lo encoge a un riel angosto
			// que solo deja ver el botón — ya no hay un botón flotante
			// aparte que necesite sincronizar su posición a mano.
			$('#sidebarCollapse').on('click', function () {
				$('#sidebar').toggleClass('active');
				localStorage.setItem('sidebarActivo', $('#sidebar').hasClass('active') ? '1' : '0');
			});

			if (localStorage.getItem('sidebarActivo') === '1') {
				$('#sidebar').addClass('active');
			}

			// Solo escritorio: con el menú recogido, un clic en una zona vacía lo deja abierto y fijo.
			var esEscritorio = window.matchMedia('(min-width: 901px)');
			function fijarSidebarRecogido(recogido) {
				$('#sidebar').toggleClass('active', recogido);
				localStorage.setItem('sidebarActivo', recogido ? '1' : '0');
				setTimeout(function () { window.dispatchEvent(new Event('resize')); }, 300);
			}

			$('#sidebar').on('click', function (e) {
				if (!esEscritorio.matches || !$(this).hasClass('active')) return;
				if ($(e.target).closest('a, button, select, label').length) return;
				fijarSidebarRecogido(false);
			});

			// Switch de canales (sidebar): global a toda la cuenta, no por
			// sección — se guarda en localStorage y se avisa a window.CanalActivo
			// vía evento para que cada dashboard (Principal, Estado de Flujo, etc.)
			// recalcule sin pedir de nuevo al servidor (ver sección 5 de CLAUDE.md).
			window.CanalActivo = localStorage.getItem('canalActivo') || 'promotores';
			$('.sidebar-canal-btn[data-canal="' + window.CanalActivo + '"]').addClass('is-activo')
				.siblings().removeClass('is-activo');

			// Visor de fotos compartido: los módulos llaman AbrirVisorFoto(ruta) con la ruta guardada en la BD.
			window.UrlFoto = function (ruta) { return 'https://luckyecuadorweb.blob.core.windows.net/app/AppPintuco/Inserts/' + ruta; };
			// Técnicos: sin foto la BD guarda 'sin_foto' (sigue contando como "ya enviado"), pero no hay imagen que mostrar.
			window.HayFoto = function (ruta) { return !!ruta && ruta !== 'sin_foto'; };
			// Excel: reemplaza las celdas de la columna por "Ver foto" con hipervínculo; rutas[i] corresponde a la fila i de datos.
			window.EnlazarFotosExcel = function (hoja, datos, columna, rutas) {
				var col = Object.keys(datos[0] || {}).indexOf(columna);
				if (col < 0) return;
				rutas.forEach(function (ruta, i) {
					if (!window.HayFoto(ruta)) return;
					hoja[XLSX.utils.encode_cell({ r: i + 1, c: col })] = { t: 's', v: 'Ver foto', l: { Target: window.UrlFoto(ruta), Tooltip: 'Abrir foto' } };
				});
			};
			window.AbrirVisorFoto = function (ruta) {
				$('#visorFotoImg').attr('src', window.UrlFoto(ruta));
				$('#visorFoto').prop('hidden', false);
				$('#visorFotoCerrar').trigger('focus');
			};
			// Diálogo compartido: Confirmar(titulo, texto, boton, peligro) y Avisar(titulo, texto) devuelven una promesa (true si se acepta).
			var ICONOS_DIALOGO = {
				peligro: '<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M10.3 3.9 1.8 18a2 2 0 0 0 1.7 3h17a2 2 0 0 0 1.7-3L13.7 3.9a2 2 0 0 0-3.4 0z"></path><line x1="12" y1="9" x2="12" y2="13"></line><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>',
				pregunta: '<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><path d="M9.1 9a3 3 0 0 1 5.8 1c0 2-3 3-3 3"></path><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>',
				error: '<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>'
			};
			function abrirDialogo(tipo, titulo, texto, boton, soloAviso) {
				var dlg = document.getElementById('confirmarDialog');
				var cancelar = document.getElementById('confirmarCancelar');
				dlg.className = 'confirmar confirmar-' + tipo;
				document.getElementById('confirmarIcono').innerHTML = ICONOS_DIALOGO[tipo];
				document.getElementById('confirmarTitulo').textContent = titulo;
				document.getElementById('confirmarTexto').textContent = texto || '';
				document.getElementById('confirmarOk').textContent = boton;
				cancelar.hidden = soloAviso;
				return new Promise(function (resolver) {
					dlg.addEventListener('close', function () { resolver(dlg.returnValue === 'ok'); }, { once: true });
					dlg.returnValue = '';
					dlg.showModal();
					(tipo === 'peligro' ? cancelar : document.getElementById('confirmarOk')).focus();
				});
			}
			window.Confirmar = function (titulo, texto, boton, peligro) { return abrirDialogo(peligro ? 'peligro' : 'pregunta', titulo, texto, boton || 'Aceptar', false); };
			window.Avisar = function (titulo, texto) { return abrirDialogo('error', titulo, texto, 'Entendido', true); };
			function cerrarVisorFoto() { $('#visorFoto').prop('hidden', true); }
			$('#visorFoto').on('click', function (ev) { if (ev.target !== $('#visorFotoImg')[0]) cerrarVisorFoto(); });
			$(document).on('keydown', function (ev) { if (ev.key === 'Escape') cerrarVisorFoto(); });

			// Relabela .etiqueta-canal (data-promotor/data-tecnico); .solo-tecnicos/.solo-promotores se ocultan por CSS con body[data-canal].
			function actualizarEtiquetasCanal() {
				var esTecnico = window.CanalActivo === 'tecnicos';
				document.body.dataset.canal = window.CanalActivo;
				$('.etiqueta-canal').each(function () {
					$(this).text(esTecnico ? $(this).data('tecnico') : $(this).data('promotor'));
				});
				if ($('.sidebar-nav li.active').is(':hidden')) $('.sidebar-nav li:visible:first a').trigger('click');
				// Si el filtro de estado tenía elegida una opción que el canal nuevo oculta, vuelve a "Todos".
				var claseOculta = esTecnico ? 'solo-promotores' : 'solo-tecnicos';
				$('#proformaFiltroEstado, #agendaFiltroEstado').each(function () {
					if ($(this).find('option:selected').hasClass(claseOculta)) {
						this.value = '';
						this.dispatchEvent(new Event('change'));
					}
				});
				$('#topbarCanal').attr('data-canal', window.CanalActivo);
				$('#topbarCanalTexto').text('Canal: ' + (esTecnico ? 'Técnicos' : 'Promotores'));
			}
			actualizarEtiquetasCanal();
			// Tras recargar por el cambio de canal, vuelve a la misma pestaña.
			var seccionGuardada = sessionStorage.getItem('seccionActiva');
			sessionStorage.removeItem('seccionActiva');
			if (seccionGuardada) $('.sidebar-nav a[data-toggle="section"][href="' + seccionGuardada + '"]:visible').trigger('click');

			$('.sidebar-canal-btn').on('click', function () {
				var canal = $(this).data('canal');
				if (canal === window.CanalActivo) return;
				localStorage.setItem('canalActivo', canal);
				sessionStorage.setItem('seccionActiva', $('.sidebar-nav li.active a').attr('href'));
				location.reload();
			});
		});
	</script>
</body>
</html>

<?php
	} else {
		header('Location: '.'/App/XploraEcuador/login.php');
	}
?>
