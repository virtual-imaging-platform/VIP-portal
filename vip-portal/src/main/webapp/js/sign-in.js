
function clickinner(){
    alert(document.cookie);
    alert(session)
    email = document.getElementById("floatingEmail").value;
    password = document.getElementById("floatingPassword").value;
    setCookie(email, 7);
    validateEmail(email);
};

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

function setCookie(value_user, exdays) {
    cname = "vip-cookie-user"
    const d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    let expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + value_user + ";" + expires + ";path=/";
    alert(document.cookie)
}