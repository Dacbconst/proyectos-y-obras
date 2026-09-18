const Api = {
    async post(url, body) {
        const res = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body),
        });
        return Api._parse(res);
    },
    async get(url, params) {
        const query = params ? '?' + new URLSearchParams(params).toString() : '';
        const res = await fetch(url + query, { method: 'GET' });
        return Api._parse(res);
    },
    async _parse(res) {
        let data;
        try {
            data = await res.json();
        } catch (e) {
            throw new Error('Respuesta inválida del servidor.');
        }
        if (res.status === 401) {
            window.location.href = '../auth/login.php';
            throw new Error('Sesión expirada.');
        }
        return data;
    },
};

function mostrarError(contenedor, mensaje) {
    contenedor.textContent = mensaje;
    contenedor.style.display = mensaje ? 'block' : 'none';
}

function formatearFecha(fechaISO) {
    if (!fechaISO) return '';
    const [y, m, d] = fechaISO.split('-');
    return `${d}/${m}/${y}`;
}
