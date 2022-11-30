function validateEmail(emailField){
    var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;

    if (reg.test(emailField) == false)
    {
        document.getElementById('login-failed').style.display = 'block';
        setTimeout(function(){document.getElementById('login-failed').style.display = 'none'}, 3000);
        return false;
    } else{
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
}

function getCookie(cName) {
    let cookieExist = true;
    const name = cName + "=";
    const cDecoded = decodeURIComponent(document.cookie);
    console.log(cDecoded);
    const cArr = cDecoded.split('; ');
    let res;
    cArr.forEach(val => {
      if (val.indexOf(name) === 0) res = val.substring(name.length);
    })
    if (res == undefined){
        cookieExist = false;
    }
    return cookieExist;
  }

function checkIfCookieExist(){
    if (getCookie("vip-cookie-user") == true && getCookie("vip-cookie-user") == true) {
        window.location.href="home.html";
        return true;
    } else {
        return false;
    }
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

function grid(data) {
    window.onload = function(){
    var section = document.createElement("section");
    section.id = "appGrid"
    var container = document.createElement("div");
    container.className = "container";

    for (let i = 0; i < data.length; i++){
        var row = document.createElement("div");
        row.className = "row";
        row.id = "appRow" + i;

        var colApp = document.createElement("div");
        colApp.className = "col";
        colApp.innerText = data[i].identifier;
        var colName = document.createElement("div");
        colName.className = "col";
        colName.innerText = data[i].name;

        row.append(colApp);
        row.appendChild(colName);
        container.appendChild(row);
    }

    section.appendChild(container);
    document.body.appendChild(section);}
};

function createGridapp(){
    fetch('http://localhost:8080/rest/pipelines/public')
    .then((response) => response.json())
    .then((data) => grid(data));
}


function clickinner(){
    email = document.getElementById("floatingEmail").value;
    password = document.getElementById("floatingPassword").value;
    validateEmail(email);
    get_fetch(email, password).then(data => setCookie(email, data.httpHeaderValue, 7));
};

checkIfCookieExist();
createGridapp();