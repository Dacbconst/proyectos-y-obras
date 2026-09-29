<?php
require_once __DIR__ . '/includes/auth_guard.php';

$usuario = usuario_actual();
if ($usuario === null) {
    header('Location: auth/login.php');
} else {
    header('Location: pages/contacto.php');
}
exit;
