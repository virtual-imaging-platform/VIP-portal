/*
 Copyright 2009-2015
 
 CREATIS
 CNRS UMR 5220 -- INSERM U1044 -- Université Lyon 1 -- INSA Lyon
 
 Authors
 
 Nouha Boujelben (nouha.boujelben@creatis.insa-lyon.fr)
 Frédéric Cervenansky (frederic.cervnansky@creatis.insa-lyon.fr)
 Rafael Ferreira da Silva (rafael.silva@creatis.insa-lyon.fr)
 Tristan Glatard (tristan.glatard@creatis.insa-lyon.fr)
 Ibrahim  Kallel (ibrahim.kallel@creatis.insa-lyon.fr)
 Kévin Moulin (kevmoulin@wanadoo.fr)
 Sorina Pop (sorina.pop@creatis.insa-lyon.fr)
 
 This software is a web portal for pipeline execution on distributed systems.
 
 This software is governed by the CeCILL-B license under French law and
 abiding by the rules of distribution of free software.  You can use,
 modify and/ or redistribute the software under the terms of the
 CeCILL-B license as circulated by CEA, CNRS and INRIA at the following
 URL "http://www.cecill.info".
 
 As a counterpart to the access to the source code and rights to copy,
 modify and redistribute granted by the license, users are provided
 only with a limited warranty and the software's author, the holder of
 the economic rights, and the successive licensors have only limited
 liability.
 
 In this respect, the user's attention is drawn to the risks associated
 with loading, using, modifying and/or developing or reproducing the
 software by the user in light of its specific status of free software,
 that may mean that it is complicated to manipulate, and that also
 therefore means that it is reserved for developers and experienced
 professionals having in-depth computer knowledge. Users are therefore
 encouraged to load and test the software's suitability as regards
 their requirements in conditions enabling the security of their
 systems and/or data to be ensured and, more generally, to use and
 operate it in the same conditions as regards security.
 
 The fact that you are presently reading this means that you have had
 knowledge of the CeCILL-B license and that you accept its terms.
 */

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


