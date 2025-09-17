function checkSession() {
    fetch("/internal/session").then(function (response) {
        if (response.status != 200) {
            window.location = "index.html";
        }
    });
}

checkSession();