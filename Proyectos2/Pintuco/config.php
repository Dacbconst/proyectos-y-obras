<?php
/**
 * DB configuration variables
 */
	define('HOST','mysqlecuadorsf.mysql.database.azure.com');
	define('USER','xplora_mysql');
    define('PASS','XpL0r@Ec8Ad0R..');
	define('DB','luckyec_pintuco');

	define("CAN_REGISTER", "any");
	define("DEFAULT_ROLE", "member");

	define("SECURE", FALSE);    // ¡¡¡SOLO PARA DESARROLLAR!!!!

	// Entorno: 'local' (tablas _test) o 'production' (real) — cambiar SOLO esta línea (ver CLAUDE.md sección 2).
	define('APP_ENV', 'local');

	if (APP_ENV === 'local') {
		define('TABLA_CONTACTO', 'insert_proyectos_contacto_test');
		define('TABLA_PROFORMA', 'insert_proforma_test');
		define('TABLA_PAGOS', 'insert_pago_factura_test');
	} else {
		define('TABLA_CONTACTO', 'insert_proyectos_contacto');
		define('TABLA_PROFORMA', 'insert_proforma');
		define('TABLA_PAGOS', 'insert_pago_factura');
	}
?>