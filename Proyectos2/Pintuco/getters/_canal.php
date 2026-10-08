<?php
// Switch de canales (sección 5 de CLAUDE.md): separa obras de técnicos de tiendas Kywi (promotores).

// Condición SQL (sin "AND") para filtrar por canal, o null si no aplica.
function canal_condicion_sql(string $canal, string $aliasUsuario): ?string
{
    // "IS NOT NULL" evita que un usuario_tecnico NULL vuelva UNKNOWN todo el NOT IN.
    if ($canal === 'tecnicos') {
        return "$aliasUsuario IN (SELECT usuario_tecnico FROM repositorio_usuario_tecnicos WHERE activo = 1 AND usuario_tecnico IS NOT NULL)";
    }
    if ($canal === 'promotores') {
        return "$aliasUsuario NOT IN (SELECT usuario_tecnico FROM repositorio_usuario_tecnicos WHERE activo = 1 AND usuario_tecnico IS NOT NULL)";
    }
    return null;
}

// Lista de usuarios técnicos activos, para que el cliente decida el canal sin pedir de nuevo al servidor.
function canal_usuarios_tecnicos(mysqli $mysqli): array
{
    $usuarios = [];
    $q = $mysqli->query("SELECT usuario_tecnico FROM repositorio_usuario_tecnicos WHERE activo = 1");
    if ($q) {
        while ($r = $q->fetch_assoc()) {
            $usuarios[] = $r['usuario_tecnico'];
        }
    }
    return $usuarios;
}
