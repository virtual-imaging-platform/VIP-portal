function checkIfCookieExist(){
    if (getCookie("vip-cookie-user") == true && getCookie("vip-cookie-user") == true) {
        return true;
    } else {
        window.location.href="index.html";
        return false;
    }
}

function getCookie(cName) {
    let cookieExist = true;
    const name = cName + "=";
    const cDecoded = decodeURIComponent(document.cookie);
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

checkIfCookieExist();