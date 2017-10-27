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

//TODO: take out the preview and give it as input param
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


//TODO: take out the document.getElementById('data_uploads').files and give it as input param
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
	//TODO make the uploadZip function a callback given as input to zipFile
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

function parseAndUploadMac(url, destPath, target, usePool) {
	return;
}