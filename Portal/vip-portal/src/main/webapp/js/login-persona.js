/* Copyright CNRS-CREATIS
 *
 * Tristan Glatard
 * glatard@creatis.insa-lyon.fr
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

var currentUser=getCookie("vip-cookie-user");

if (!currentUser)
    currentUser = null; //currentUser has to contain either a valid email or null. Otherwise, Persona will go in an infinite loop
else
    currentUser=decodeURIComponent(currentUser);

function getCookie(cname)
{
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++)
    {
        var c = ca[i].trim();
        if (c.indexOf(name)==0) return c.substring(name.length,c.length);
    }
    return "";
}


function verifyAssertion(assertion) {
    // Your backend must return HTTP status code 200 to indicate successful
    // verification of user's email address and it must arrange for the binding
    // of currentUser to said address when the page is reloaded
    var xhr = new XMLHttpRequest();
    xhr.open("POST",  "/fr.insalyon.creatis.vip.portal.Main/personaauthenticationservice", true);
    // see http://www.openjs.com/articles/ajax_xmlhttp_using_post.php
    var param = "assertion="+assertion;
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
   // xhr.setRequestHeader("Content-length", param.length);
   // xhr.setRequestHeader("Connection", "close");
    xhr.send(param); // for verification by your backend

    xhr.onreadystatechange = function() {
    if (xhr.readyState == 4) {
	if(xhr.status != 200){
		window.alert("Couldn't login ("+xhr.status+")");
		navigator.id.logout();
	}
	else{
         window.parent.location.reload(true);
	}
      }
   }
}

function signoutUser() {
    currentUser = null;
    }

// Go!
navigator.id.watch( {
    loggedInUser: currentUser,
    onlogin: verifyAssertion,
    onlogout: signoutUser } );


