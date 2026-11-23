async function checkSession() {
    const response = await fetch("internal/session");

    if ( ! response.ok) {
        window.location.replace('index.html');
    }
}

checkSession();