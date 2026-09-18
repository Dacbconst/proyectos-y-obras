<?php
/**
 * Config de conexión — mismo servidor/BD que Proyectos2 y AppPintuco
 * (ver Proyectos2/Pintuco/config.php). Ojo: estas credenciales ya están
 * hardcodeadas en texto plano en esos dos proyectos hermanos; se replica
 * el mismo patrón aquí para apuntar a la misma base de producción.
 */
define('HOST', 'mysqlecuadorsf.mysql.database.azure.com');
define('USER', 'xplora_mysql');
define('PASS', 'XpL0r@Ec8Ad0R..');
define('DB', 'luckyec_pintuco');
