async function get_fetch(firstName, lastName, email, institution, password, country){
    const data = await fetch('rest/register', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ "firstName": firstName, "lastName": lastName, "email": email, "institution": institution, "password": password, "countryCode": country})
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

    if (!new_firstName || !new_lastName || !new_email || !new_reEmail || !new_institution || new_country === "select your country" || new_country === "select your country" || !new_password || !new_rePassword) {
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
                await get_fetch_authenticate(new_email, new_password);
            } catch (error) {
                console.log(error);
                document.getElementById('fetch-failed').style.display = 'block';
                setTimeout(function(){document.getElementById('fetch-failed').style.display = 'none'}, 30000);
            }
        }
}
