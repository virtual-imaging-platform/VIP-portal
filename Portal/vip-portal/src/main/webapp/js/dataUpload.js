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

//view-source:https://dior.ics.muni.cz/~makub/massupload.html
//https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/file


     function handleFiles(files) {
          var preview = document.querySelector('.preview');
          while(preview.firstChild) {
            preview.removeChild(preview.firstChild);
          }
          var curFiles = files;
          if(curFiles.length === 0) {
            var para = document.createElement('p');
            para.textContent = 'No files currently selected for upload';
            preview.appendChild(para);
          } else {
            var list = document.createElement('ol');
            preview.appendChild(list);
            for(var i = 0; i < curFiles.length; i++) {
              var listItem = document.createElement('li');
              var para = document.createElement('p');
              para.textContent = 'File name ' + curFiles[i].name ;
              //var image = document.createElement('img');
              //image.src = window.URL.createObjectURL(curFiles[i]);
              //listItem.appendChild(image);
              listItem.appendChild(para);
              list.appendChild(listItem);
            }
          }
     }

//upload files
    function uploadFiles() {
        //todo: check empty list ?
        var xhr = new XMLHttpRequest();
        var fileList = document.getElementById('data_uploads').files;
        var fd = new FormData();
        for (var i = 0, file; file = fileList[i]; i++) {
            fd.append("file", file);
        }
        /* event listeners */
        // http://www.w3.org/TR/XMLHttpRequest2/#xmlhttprequestupload
        // http://www.w3.org/TR/progress-events/#interface-progressevent
        xhr.upload.addEventListener("progress", uploadProgress, false);
        xhr.addEventListener("load", uploadComplete, false);
        xhr.addEventListener("error", uploadFailed, false);
        xhr.addEventListener("abort", uploadCanceled, false);
        //todo : check if files are uploaded one by one; if not, check if they can be compressed and sent to /fr.insalyon.creatis.vip.portal.Main/uploadfilesservice
        xhr.open("POST", "/fr.insalyon.creatis.vip.portal.Main/fileuploadservice");
        xhr.send(fd);
    }

    //callbacks for upload
    function uploadProgress(evt) {
        if (evt.lengthComputable) {
            var percentComplete = Math.round(evt.loaded * 100 / evt.total);
            document.getElementById('progressNumber').innerHTML = percentComplete.toString() + '%';
        }
        else {
            document.getElementById('progressNumber').innerHTML = 'unable to compute';
        }
    }

    function uploadComplete(evt) {
        /* This event is raised when the server send back a response */
        document.getElementById('serverResponse').innerHTML = evt.target.responseText;
    }

    function uploadFailed(evt) {
        alert("There was an error attempting to upload the file.");
    }

    function uploadCanceled(evt) {
        alert("The upload has been canceled by the user or the browser dropped the connection.");
    }


