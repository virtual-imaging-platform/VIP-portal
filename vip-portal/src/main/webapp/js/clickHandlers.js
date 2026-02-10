function zipAndUploadFiles(data, url, destPath, target, usePool, doUnzip) {
    var isGateLab = "false";
    zipAndUpload(data, url, destPath, target, usePool, doUnzip, isGateLab);
}

//zip a list of files and call uploadZip to send the final zip to the given url (which is a file upload service)
//data can be a fileList or the id of of a fileList element
function zipAndUpload(data, url, destPath, target, usePool, doUnzip, isGateLab) {
    var zip = new JSZip();
    var reader = new FileReader();
    var fileList = getFileList(data);
    var fileName = getZipName();

    return new Promise(function (resolve, reject) {
        //TODO handle single file use-case: no need for zipping + set single=true
        //TODO make the uploadZip function a callback given as input to zipFile
        function zipFile(index) {
            //upload zipped file when finished zipping all files (index == fileList.length)
            if (index === fileList.length) {
                if (JSZip.support.blob) {
                    uploadZip(fileName, zip, url, destPath, target, usePool, doUnzip);
                    resolve(fileName);
                } else {
                    alert("JSZip blob not supported on this browser.");
                }
                return;
            } else {
                var file = fileList[index];
                var path = getPathForZip(file, isGateLab);
                reader.onload = function (e) {
                    // get file content and add it to zip
                    var data = e.target.result;
                    zip.file(path, data);
                    zipFile(index + 1);
                };
                reader.readAsArrayBuffer(file);
            }
        }
        zipFile(0);
    });
}

function getFileList(data) {
    var fileList;
    if (Array.isArray(data)) {
        fileList = data;
    } else {
        fileList = document.getElementById(data).files;
    }
    if (fileList.length === 0) {
        alert("No folder selected for upload");
        return;
    }
    return fileList;
}

function getZipName() {
    var timestamp = new Date().getTime();
    return "file-" + timestamp + ".zip";
}

function getPathForZip(file, isGateLab) {
    var path = file.webkitRelativePath;
    if (isGateLab === "true") {
        //the workflow config file (wfl_config.txt) has no path, setting it to the file name
        if (path === "") {
            path = file.name;
        }
        var pathArray = path.split('/');
        //removing parent folders for the GateLab
        if (pathArray.length > 2) {
            path = pathArray[pathArray.length - 2] + "/" + pathArray[pathArray.length - 1];
        }
    }
    return path;
}

function parseAndUploadMac(parentFolderId, macId, url, destPath, target, usePool, doUnzip) {

    var promise = parseMacFile(macId, parentFolderId);
    var macroData;
    var isGateLab = "true";
    //parseMacFile returns a promise that is resolved to the value localMacroData when finished 
    promise.then(function (localMacroData) {
        //localMacroData is only acessible in this block, but fillInInputs() also needs access to it
        macroData = localMacroData;
        return getListOfFiles(localMacroData, parentFolderId);
    }).then(function (filesToUpload) {
        return zipAndUpload(filesToUpload, url, destPath, target, usePool, doUnzip, isGateLab);
    }).then(function (fileName) {
        var inputs = fillInInputs(fileName, macroData);
        uploadMacComplete(inputs);
    }).catch(function (error) {
        console.log("Failed!", error);
        alert(error + " Please start over.");
        window.close();
        return null;
    });

}
