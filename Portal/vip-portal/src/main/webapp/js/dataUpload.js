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

function previewFiles(files) {
    var preview = document.querySelector('.preview');
    while (preview.firstChild) {
        preview.removeChild(preview.firstChild);
    }
    var curFiles = files;
    if (curFiles.length === 0) {
        var para = document.createElement('p');
        para.textContent = 'No files currently selected for upload';
        preview.appendChild(para);
    } else {
        //TODO: add a scrollable item if we want to preview the list of files
        /*
        var list = document.createElement('ol');
        preview.appendChild(list);
        for (var i = 0; i < curFiles.length; i++) {
            var listItem = document.createElement('li');
            var para = document.createElement('p');
            para.textContent = 'File name ' + curFiles[i].name;
            listItem.appendChild(para);
            list.appendChild(listItem);
        }
        */
        var para = document.createElement('p');
        para.textContent = " " + curFiles.length + " files selected for upload. Click on the upload button to proceed.";
        preview.appendChild(para);
    }
}



//zip a list of files and call uploadZip to send the final zip to the given url (which is a file upload service)
function zipAndUploadFiles(url, destPath, target, usePool) {
    var zip = new JSZip();
    var reader = new FileReader();
    var fileList = document.getElementById('data_uploads').files;
    if (fileList.length === 0) {
        alert("No folder selected for upload");
        return;
    }
    //TODO handle single file use-case: no need for zipping + set single=true
    function zipFile(index) {
        //upload zipped file when finished zipping all files (index == fileList.length)
        if (index === fileList.length) {
            if (JSZip.support.blob) {
                uploadZip(zip, url, destPath, target, usePool);
            } else {
                alert("JSZip blob not supported on this browser.");
            }
            return;
        } else {
            var file = fileList[index];
            var path = file.webkitRelativePath;
            reader.onload = function (e) {
                // get file content and add it to zip
                var data = e.target.result;
                zip.file(path, data);
                // zip next file
                zipFile(index + 1);
            };
            reader.readAsArrayBuffer(file);
        }
    }
    zipFile(0);
}

// upload generated zip file to destPath using the given url (which is a file upload service)
function uploadZip(zip, url, destPath, target, usePool) {
    var fd = new FormData();
    var xhr = new XMLHttpRequest();
    xhr.upload.addEventListener("progress", uploadProgress, false);
    xhr.addEventListener("load", uploadComplete, false);
    xhr.addEventListener("error", uploadFailed, false);
    xhr.addEventListener("abort", uploadCanceled, false);
    var getTimestamp = function() {  return new Date().getTime(); };
    zip.generateAsync({type: "blob"}).then(function (blob) {
        fd.append("file", blob, "file-"+getTimestamp()+".zip");
        fd.append("path", destPath);
        fd.append("single", 'false');
        fd.append("unzip", 'true');
        fd.append("pool", usePool);
        fd.append("target", target);
        xhr.open("POST", url);
        xhr.send(fd);
    }, function (err) {
        alert("An error occured:" + err);
    });
}

//callbacks for upload
function uploadProgress(evt) {
    if (evt.lengthComputable) {
        var percentComplete = Math.round(evt.loaded * 100 / evt.total);
        document.getElementById('progressNumber').innerHTML = percentComplete.toString() + '%';
    } else {
        document.getElementById('progressNumber').innerHTML = 'unable to compute';
    }
}

function uploadComplete(evt) {
    //This event is raised when the server sends back a response 
    document.getElementById('serverResponse').innerHTML = evt.target.responseText;
    //the following is used in order to execute the script from the response, which look like this: 
    //"if (parent.dataManagerUploadComplete) parent.dataManagerUploadComplete('Upload-2092952139087909##Upload-2092952141816446##');"
    //TODO: check whether this could be a security issue
    //TODO: when GateLab is also replaced, change the HTML response to XML data that could be directly processed here
    eval(document.getElementById("runscript").innerHTML);
}

function uploadFailed(evt) {
    alert("There was an error attempting to upload the file.");
}

function uploadCanceled(evt) {
    alert("The upload has been canceled by the user or the browser dropped the connection.");
}


