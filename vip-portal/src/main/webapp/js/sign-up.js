async function get_fetch(firstName, lastName, email, institution, country){
    const data = await fetch('http://localhost:8080/rest/register', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ "firstName": firstName, "lastName": lastName, "email": email, "institution": institution, "countryCode": country})
        })

        window.location.href="activation.html";

}

function validateEmail(emailField){
    var reg = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

    if (reg.test(emailField) == false)
    {
        document.getElementById('email-failed').style.display = 'block';
        setTimeout(function(){document.getElementById('email-failed').style.display = 'none'}, 3000);
        return false;
    } else{
        return true;
    }
}


function createUser(){
    new_firstName = document.getElementById("firstName").value;
    new_lastName = document.getElementById("lastName").value;
    new_email = document.getElementById("email").value;
    new_reEmail = document.getElementById("reEmail").value;
    new_institution = document.getElementById("institution").value;
    new_country = document.getElementById("country").value;
    new_password = document.getElementById("password").value;
    new_rePassword = document.getElementById("rePassword").value;

    new_country = new_country.toLowerCase();
    console.log(new_country);

    if (validateEmail(new_email)) {
        if (new_email == new_reEmail && new_password == new_rePassword){
            if (document.getElementById("termsOfUse").checked){
                get_fetch(new_firstName, new_lastName, new_email, new_institution, new_country);
            } else {
                document.getElementById('termsOfUse-failed').style.display = 'block';
                setTimeout(function(){document.getElementById('termsOfUse-failed').style.display = 'none'}, 30000);
            }
        } else {
            document.getElementById('signUp-failed').style.display = 'block';
            setTimeout(function(){document.getElementById('signUp-failed').style.display = 'none'}, 30000);
        }
    }
};
