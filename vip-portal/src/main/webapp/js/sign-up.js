async function get_fetch(firstName, lastName, email, institution, password, country){
    const data = await fetch('http://localhost:8080/rest/register', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ "firstName": firstName, "lastName": lastName, "email": email, "institution": institution, "password": password, "countryCode": country})
        })

}

async function get_fetch_authenticate(form_email, form_password){
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

async function createUser(){
    let isValid = true;

    const new_firstName = document.getElementById("firstName").value;
    const new_lastName = document.getElementById("lastName").value;
    const new_email = document.getElementById("email").value;
    const new_reEmail = document.getElementById("reEmail").value;
    const new_institution = document.getElementById("institution").value;
    const new_application = document.getElementById("application").value;
    const new_country = document.getElementById("country").value.toLowerCase();
    const new_password = document.getElementById("password").value;
    const new_rePassword = document.getElementById("rePassword").value;
    const emailRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;

    console.log(new_application);
    console.log(new_country);

    if (!new_firstName || !new_lastName || !new_email || !new_reEmail || !new_institution || new_application === "Choose your Application" || new_country === "select your country" || !new_password || !new_rePassword) {
        isValid = false;
        document.getElementById('emptyFields-failed').style.display = 'block';
        setTimeout(function(){document.getElementById('emptyFields-failed').style.display = 'none'}, 30000);
    }
    if (!emailRegex.test(new_email)) {
        isValid = false;
        document.getElementById('email-failed').style.display = 'block';
        setTimeout(function(){document.getElementById('email-failed').style.display = 'none'}, 30000);
    }
    if (new_email !== new_reEmail) {
        isValid = false;
        document.getElementById('emailMatch-failed').style.display = 'block';
        setTimeout(function(){document.getElementById('emailMatch-failed').style.display = 'none'}, 30000);
    }
    if (new_password !== new_rePassword) {
        isValid = false;
        document.getElementById('passwordMatch-failed').style.display = 'block';
        setTimeout(function(){document.getElementById('passwordMatch-failed').style.display = 'none'}, 30000);
    }
    if (!document.getElementById("termsOfUse").checked) {
        isValid = false;
        document.getElementById('termsOfUse-failed').style.display = 'block';
        setTimeout(function(){document.getElementById('termsOfUse-failed').style.display = 'none'}, 30000);
    }
    if (isValid) {
        try {
                await get_fetch(new_firstName, new_lastName, new_email, new_institution, new_password, new_country);
                const data = await get_fetch_authenticate(new_email, new_password);
                setCookie(new_email, data.httpHeaderValue, 7);
            } catch (error) {
                console.log(error);
                document.getElementById('fetch-failed').style.display = 'block';
                setTimeout(function(){document.getElementById('fetch-failed').style.display = 'none'}, 30000);
            }
        }
}

function createSelectApp(applications) {
    // Créer l'élément de sélection
    const select = document.createElement("select");
    select.setAttribute("id", "application");
    select.classList.add("form-select");
    select.setAttribute("aria-label", "Default select example");

    // Ajouter une option par défaut
    const defaultOption = document.createElement("option");
    defaultOption.textContent = "Choose your Application";
    select.appendChild(defaultOption);

    // Ajouter les options correspondant aux applications
    applications.forEach((application) => {
      const option = document.createElement("option");
      option.value = application.name;
      option.textContent = application.name;
      select.appendChild(option);
    });

    return select;
  }

  fetch('http://localhost:8080/rest/pipelines?public')
    .then((response_app) => response_app.json())
    .then((data) => {
      var application = new Array();
      data.forEach((item, index) => {
          let name_classes = item.applicationClasses.toString();
          let name_groups = item.applicationGroups.toString();
          application.push({name:item.name,name_classes:name_classes,name_groups:name_groups});
      });
      return application;
    })
    .then((applications) => {
        const select = createSelectApp(applications);

        const selectAppDiv = document.getElementById("select-app");
        selectAppDiv.appendChild(select);
    });


