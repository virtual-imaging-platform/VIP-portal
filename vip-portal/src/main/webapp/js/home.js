function checkSession() {
    fetch("/internal/session").then(function (response) {
        if ( ! response.ok) {
            window.location = "index.html";
        }
    });
}

checkSession();