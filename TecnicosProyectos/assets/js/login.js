(function() {
    const toggleBtn = document.getElementById('togglePassword');
    const pwInput = document.getElementById('password');
    const eyeIcon = document.getElementById('eyeIcon');
    const form = document.getElementById('loginForm');
    const btnSubmit = document.getElementById('btnSubmit');
    const btnTextMobile = document.getElementById('btnTextMobile');
    const btnTextDesktop = document.getElementById('btnTextDesktop');
    const usuarioInput = document.getElementById('usuario_tecnico');
    const alertBox = document.getElementById('alertBox');
    const alertMessage = document.getElementById('alertMessage');
    const loginCard = document.getElementById('loginCard');

    // Toggle Password Show/Hide
    if (toggleBtn && pwInput) {
        toggleBtn.addEventListener('click', function(e) {
            e.preventDefault();
            const isPassword = pwInput.getAttribute('type') === 'password';
            pwInput.setAttribute('type', isPassword ? 'text' : 'password');

            if (isPassword) {
                toggleBtn.setAttribute('aria-label', 'Ocultar contraseña');
                eyeIcon.innerHTML = `
                    <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
                    <line x1="1" y1="1" x2="23" y2="23"></line>
                `;
            } else {
                toggleBtn.setAttribute('aria-label', 'Mostrar contraseña');
                eyeIcon.innerHTML = `
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                    <circle cx="12" cy="12" r="3"></circle>
                `;
            }
        });
    }

    // Placeholder adaptativo según viewport
    function adaptPlaceholder() {
        if (!usuarioInput) return;
        if (window.innerWidth >= 900) {
            usuarioInput.placeholder = 'Admin';
        } else {
            usuarioInput.placeholder = 'Iniciar sesión';
        }
    }

    window.addEventListener('resize', adaptPlaceholder);
    adaptPlaceholder();

    // Control de alertas de error
    function showError(msg) {
        if (alertMessage) alertMessage.textContent = msg;
        if (alertBox) {
            alertBox.style.display = 'flex';
            alertBox.classList.remove('fadeIn');
            void alertBox.offsetWidth;
            alertBox.classList.add('fadeIn');
        }
        if (loginCard) {
            loginCard.classList.remove('shake-card');
            void loginCard.offsetWidth;
            loginCard.classList.add('shake-card');
        }
    }

    function hideError() {
        if (alertBox) alertBox.style.display = 'none';
        if (loginCard) loginCard.classList.remove('shake-card');
    }

    function resetButton() {
        btnSubmit.disabled = false;
        if (btnTextMobile) btnTextMobile.textContent = 'Iniciar sesión';
        if (btnTextDesktop) btnTextDesktop.textContent = 'Ingresar';
    }

    // Manejo de envío con animación de apertura al iniciar sesión
    if (form && btnSubmit) {
        form.addEventListener('submit', async function(e) {
            e.preventDefault();
            hideError();

            // Estado de carga
            btnSubmit.disabled = true;
            if (btnTextMobile) btnTextMobile.textContent = 'Verificando…';
            if (btnTextDesktop) btnTextDesktop.textContent = 'Verificando…';

            const formData = new FormData(form);
            formData.append('ajax', '1');

            try {
                const response = await fetch('login.php', {
                    method: 'POST',
                    body: formData,
                    headers: {
                        'X-Requested-With': 'XMLHttpRequest'
                    }
                });

                const data = await response.json();

                if (data && data.success) {
                    const isDesktop = window.innerWidth >= 900;
                    const redirectUrl = data.redirect || '../pages/contacto.php';
                    const hora = new Date().getHours();
                    const momento = (hora >= 5 && hora < 12) ? 'Buenos días,' : ((hora >= 12 && hora < 19) ? 'Buenas tardes,' : 'Buenas noches,');
                    const nombreUsuario = (data.nombre || data.usuario || (usuarioInput ? usuarioInput.value : '')).trim();

                    const greetingEl = document.getElementById('portalGreeting');
                    const usernameEl = document.getElementById('portalUsername');
                    if (greetingEl) greetingEl.textContent = momento;
                    if (usernameEl) usernameEl.textContent = nombreUsuario;

                    if (isDesktop) {
                        document.body.classList.add('is-unlocking');
                    } else {
                        document.body.classList.add('is-unlocking-mobile');
                    }

                    setTimeout(function() {
                        window.location.href = redirectUrl;
                    }, 800);
                } else {
                    // Error de autenticación
                    resetButton();
                    showError(data.message || 'Usuario o contraseña incorrectos.');
                    if (pwInput) pwInput.focus();
                }
            } catch (err) {
                // Si falla el fetch por conectividad o servidor, fallback al envío tradicional POST
                form.submit();
            }
        });
    }
})();
