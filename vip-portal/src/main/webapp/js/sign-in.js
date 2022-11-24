
function validateEmail(emailField){
    var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;

    if (reg.test(emailField) == false)
    {
        alert('Invalid Email Address');
        return false;
    } else{
        window.location.href="home.html";
        return true;
    }
}

function setCookie(value_user, value_session, exdays) {
    cname = "vip-cookie-user"
    csession = "vip-cookie-session"
    const d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    let expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + value_user + ";" + expires + ";path=/";
    document.cookie = csession + "=" + value_session + ";" + expires + ";path=/";
    window.location.href="home.html";
    alert(document.cookie)
}

async function get_fetch(form_email, form_password){
    const data = await fetch('http://localhost:8080/rest/authenticate', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ "username": form_email, "password" : form_password})
        })
        if (data.ok == true){
           return data.json();
        }
        throw new Error("Unable to contact the server")

}

function clickinner(){
    email = document.getElementById("floatingEmail").value;
    password = document.getElementById("floatingPassword").value;
    //validateEmail(email);*

    get_fetch(email, password).then(data => setCookie(email, data.httpHeaderValue, 7));


    //setCookie("admin@vip-local-test.local", "15b1c9bd-765a-4047-a9c5-3031f2a47afa", 7)
    //window.location.href="home.html";

};