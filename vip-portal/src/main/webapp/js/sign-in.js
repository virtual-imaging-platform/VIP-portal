

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

function forgot_password(){
    document.getElementById('forgot_password').style.display = 'block';
}

async function continue_forgotpsw() {
    email = document.getElementById("floatingEmailWarning").value;
    localStorage.setItem("email", email);
    fetch('http://localhost:8080/rest/reset-code', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email })
    })

    // Create the new alert block
    var alertHTML =
        '<div class="alert alert-success">' +
            '<p>An email with your reset code has been sent to your email address.</p>' +
            '<div class="form-group">' +
                '<label for="activationCode">Reset code:</label>' +
                '<input type="text" class="form-control" id="activationCode" placeholder="Enter your reset code">' +
            '</div>' +
            '<div class="form-group">' +
                '<label for="newPassword">New password:</label>' +
                '<input type="password" class="form-control" id="newPassword" placeholder="Enter your new password">' +
            '</div>' +
            '<div class="form-group">' +
                '<label for="reEnterNewPassword">Re-enter your new password:</label>' +
                '<input type="password" class="form-control" id="reEnterNewPassword" placeholder="Re-enter your new password">' +
            '</div>' +
            '<button type="submit" class="btn btn-primary" onclick="resetPassword()">Reset password</button>' +
        '</div>';

    // Replace the existing alert block with the new one
    var oldAlert = document.getElementById('forgot_password');
    var newAlert = document.createRange().createContextualFragment(alertHTML);
    oldAlert.parentNode.replaceChild(newAlert, oldAlert);
}

function resetPassword() {
    // Retrieve the values of the activation code, new password, and re-enter new password fields
    var email = localStorage.getItem("email");
    var activationCode = document.getElementById('activationCode').value;
    var newPassword = document.getElementById('newPassword').value;
    var reEnterNewPassword = document.getElementById('reEnterNewPassword').value;

    // Check that the new password and re-enter new password fields are the same
    if (newPassword !== reEnterNewPassword) {
        // Display an error message if the fields are not the same
        alert('The entered passwords do not match!');
        return;
    }

    // Send the data to the server to reset the password
    fetch('http://localhost:8080/rest/reset-password', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email,
            activationCode: activationCode,
            newPassword: newPassword
        })
    })
    .then(response => {
        if (response.ok) {
            // Create the new alert block
            var alertHTML =
                '<div class="alert alert-success">' +
                    '<p>Your password has been successfully reset!</p>' +
                '</div>';

        } else {
            // Display an error message if there was a problem resetting the password
            alert('There was a problem resetting your password. Please try again.');
        }
    });
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

function createAnAccount(){
    window.location.href="sign-up.html";
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

function make_table(data) {
    window.onload = function() {
    var application = new Array();
    for (let i = 0; i < data[0].length; i++){
        name_classes = data[0][i].applicationClasses.toString();
        name_groups = data[0][i].applicationGroups.toString();
        application.push([i, data[0][i].name, name_classes, name_groups])
    }

    var tablecontents = "";
    for (var i = 0; i < application.length; i++) {
        tablecontents += "<tr>";
        for (var j = 0; j < application[i].length; j++) {
            tablecontents += "<td>" + application[i][j] + "</td>";
        }
        tablecontents += "</tr>";
    }
    document.getElementById("my_tbody_app").innerHTML = tablecontents;

    var publication = new Array();
    for (let i = 0; i < data[1].length; i++){
        name_title = data[1][i].title.toString();
        name_type = data[1][i].type.toString();
        name_typeName = data[1][i].typeName.toString();
        name_vipAuthor = data[1][i].vipAuthor.toString();
        name_date = data[1][i].date.toString();
        name_vipApplication = data[1][i].vipApplication.toString();
        publication.push([i, name_title, name_type, name_typeName, name_vipAuthor, name_date, name_vipApplication])
    }

    var tablecontents = "";
    for (var i = 0; i < publication.length; i++) {
        tablecontents += "<tr>";
        for (var j = 0; j < publication[i].length; j++) {
            tablecontents += "<td>" + publication[i][j] + "</td>";
        }
        tablecontents += "</tr>";
    }
    document.getElementById("my_tbody_publi").innerHTML = tablecontents;

    $(document).ready(function () {
        $('#id_table_Publication').DataTable();
    });
}};

function make_table_publi(data) {
    window.onload = function() {
    var publication = new Array();
    for (let i = 0; i < data.length; i++){
        name_title = data[i].title.toString();
        name_type = data[i].type.toString();
        name_typeName = data[i].typeName.toString();
        name_vipAuthor = data[i].vipAuthor.toString();
        name_date = data[i].date.toString();
        name_vipApplication = data[i].vipApplication.toString();
        publication.push([i, data[i].name, name_title, name_type, name_typeName, name_vipAuthor, name_date, name_vipApplication])
    }

    var tablecontents = "";
    for (var i = 0; i < publication.length; i++) {
        tablecontents += "<tr>";
        for (var j = 0; j < publication[i].length; j++) {
            tablecontents += "<td>" + publication[i][j] + "</td>";
        }
        tablecontents += "</tr>";
    }
    document.getElementById("my_tbody_publi").innerHTML = tablecontents;

}};

async function createGrid(){
    Promise.all([
        await fetch('http://localhost:8080/rest/pipelines?public')
        .then((response_app) => response_app.json()),
        await fetch('http://localhost:8080/rest/publication')
        .then((response_publi) => response_publi.json())
      ]).then((data) => make_table(data));
}


function clickinner(){
    email = document.getElementById("floatingEmail").value;
    password = document.getElementById("floatingPassword").value;
    validateEmail(email);
    get_fetch(email, password).then(data => setCookie(email, data.httpHeaderValue, 7));
};

checkIfCookieExist();
createGrid();