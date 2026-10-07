<?php
// Switch de canales (sección 5 de CLAUDE.md): separa obras de técnicos
// (TecnicosProyectos) de tiendas Kywi (promotores) sin tocar el esquema de
// insert_proyectos_contacto. Reutilizado por los getters que filtran o
// exponen el canal — evita repetir la subconsulta en cada uno.

// Condición SQL (sin "AND") para filtrar por canal, o null si no aplica
// ('todos': sin filtro).
function canal_condicion_sql(string $canal, string $aliasUsuario): ?string
{
    // "IS NOT NULL" en la subconsulta es obligatorio para el caso NOT IN: si
    // repositorio_usuario_tecnicos.usuario_tecnico trajera algún NULL, un
    // "x NOT IN (subquery con NULL)" se vuelve UNKNOWN para TODAS las filas
    // en SQL (three-valued logic) y el canal "promotores" devolvería 0 filas
    // siempre, sin importar los datos reales. Blindaje preventivo.
    if ($canal === 'tecnicos') {
        return "$aliasUsuario IN (SELECT usuario_tecnico FROM repositorio_usuario_tecnicos WHERE activo = 1 AND usuario_tecnico IS NOT NULL)";
    }
    if ($canal === 'promotores') {
        return "$aliasUsuario NOT IN (SELECT usuario_tecnico FROM repositorio_usuario_tecnicos WHERE activo = 1 AND usuario_tecnico IS NOT NULL)";
    }
    return null;
}

// Lista de usuarios técnicos activos — la usan los getters que devuelven
// filas sin filtrar para que el cliente (igual que principal.js) decida el
// canal sin pedir de nuevo al servidor.
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
