/* Internet sources used */
//https://dior.ics.muni.cz/~makub/massupload.html
//https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/file
//https://stuk.github.io/jszip/documentation/examples/download-zip-file.html


// upload generated zip file to destPath using the given url (which is a file upload service)
function uploadZip(fileName, zip, url, destPath, target, usePool, doUnzip) {
    var fd = new FormData();
    var xhr = new XMLHttpRequest();
    xhr.upload.addEventListener("progress", uploadProgress, false);
    xhr.addEventListener("load", uploadComplete, false);
    xhr.addEventListener("error", uploadFailed, false);
    xhr.addEventListener("abort", uploadCanceled, false);
    zip.generateAsync({type: "blob"}).then(function (blob) {
        fd.append("file", blob, fileName);
        fd.append("path", destPath);
        fd.append("single", 'false');
        fd.append("unzip", doUnzip);
        fd.append("pool", usePool);
        fd.append("target", target);
        xhr.open("POST", url);
        xhr.send(fd);
    }, function (err) {
        alert("An error occured:" + err);
    });
}

//callbacks for upload
//TODO: could be improved by printing the uploaded % (or nb of files)
function uploadProgress(evt) {
    document.getElementById('progressNumber').innerHTML = 'Uploading...';
}

function uploadComplete(evt) {
    //This event is raised when the server sends back a response 
    document.getElementById('serverResponse').innerHTML = evt.target.responseText;
    //the following is used in order to execute the script from the response, which look like this: 
    //"if (parent.dataManagerUploadComplete) parent.dataManagerUploadComplete('Upload-2092952139087909##Upload-2092952141816446##');"
    //TODO: check whether this could be a security issue
    //TODO: change the HTML response to XML data that could be directly processed here
    eval(document.getElementById("runscript").innerHTML);
}

function uploadFailed(evt) {
    alert("There was an error attempting to upload the file.");
}

function uploadCanceled(evt) {
    alert("The upload has been canceled by the user or the browser dropped the connection.");
}


