<?php
require_once __DIR__ . '/../includes/auth_guard.php';
exigir_sesion();
header('Location: contacto.php');
exit;
