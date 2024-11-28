async function get_fetch(firstName, lastName, email, institution, password, country, applications){
    const data = await fetch('rest/register', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ "firstName": firstName, "lastName": lastName, "email": email, "institution": institution, "password": password, "countryCode": country, "applications": applications})
        })

}

async function get_fetch_authenticate(form_email, form_password){
    const data = await fetch('rest/session', {
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
    const new_country = document.getElementById("country").value.toLowerCase();
    const new_password = document.getElementById("password").value;
    const new_rePassword = document.getElementById("rePassword").value;
    const emailRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;

    const selectedApplications = Array.from(document.querySelectorAll('input[name="applications"]:checked'))
    .map((checkbox) => checkbox.value);

    if (!new_firstName || !new_lastName || !new_email || !new_reEmail || !new_institution || selectedApplications.length === 0 || new_country === "select your country" || new_country === "select your country" || !new_password || !new_rePassword) {
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
                await get_fetch(new_firstName, new_lastName, new_email, new_institution, new_password, new_country, selectedApplications);
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
    const selectContainer = document.createElement("div");
    selectContainer.classList.add("dropdown-menu");
    selectContainer.setAttribute("aria-labelledby", "application-dropdown");

    applications.forEach((application) => {
      const label = document.createElement("label");
      label.classList.add("dropdown-item");

      const checkbox = document.createElement("input");
      checkbox.type = "checkbox";
      checkbox.classList.add("form-check-input");
      checkbox.name = "applications";
      checkbox.value = application.name;

      label.appendChild(checkbox);
      label.appendChild(document.createTextNode(" " + application.name));
      selectContainer.appendChild(label);
    });

    const dropdownToggle = document.createElement("button");
    dropdownToggle.classList.add("btn", "btn-primary", "dropdown-toggle", "w-100");
    dropdownToggle.setAttribute("type", "button");
    dropdownToggle.setAttribute("id", "select-applications-btn");
    dropdownToggle.setAttribute("data-bs-toggle", "dropdown");
    dropdownToggle.setAttribute("aria-expanded", "false");
    dropdownToggle.textContent = "Select applications ";

    const dropdownMenu = document.createElement("div");
    dropdownMenu.classList.add("dropdown");
    dropdownMenu.appendChild(dropdownToggle);
    dropdownMenu.appendChild(selectContainer);

    dropdownToggle.addEventListener("click", function() {
      const checkboxes = selectContainer.querySelectorAll("input[name='applications']");
      const selectedApplications = [];
      checkboxes.forEach((checkbox) => {
        if (checkbox.checked) {
          selectedApplications.push(checkbox.value);
        }
      });
    });
    return dropdownMenu;
  }

  fetch('rest/pipelines?public')
    .then((response_app) => response_app.json())
    .then((data) => {
      var application = new Array();
      data.forEach((item, index) => {
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