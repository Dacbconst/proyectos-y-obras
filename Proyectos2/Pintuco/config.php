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

	// Entorno: 'local' (desarrollo, tablas _test aisladas) o 'production' (real).
	// Cambiar SOLO esta línea para alternar — ver sección 2 de CLAUDE.md.
	define('APP_ENV', 'production');

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