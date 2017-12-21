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


//https://www.html5rocks.com/en/tutorials/file/dndfiles/
//asynchronous function that returns a promise resolved to ret (array of parsed values) once the parsing is finished
function parseMacFile(macId, parentFolderId) {
    return new Promise(function (resolve, reject) {
        var mainMacFile = document.getElementById(macId).files[0];
        var ret =
                {
                    macFilesArray: [],
                    inputFilesArray: [],
                    outputFilesArray: [],
                    engineSeed: "",
                    visu: "",
                    beamOnEvents: "",
                    totalNumberOfPrimaries: "",
                    timeSimu: "",
                    otherFilesArray: []
                };

        var parserArray = [
            {reg: ".* mac/(.*\.mac)", val: "macFilesArray"},
            {reg: ".* data/(.*)", val: "inputFilesArray"},
            {reg: ".* output/(.*)", val: "outputFilesArray"},
            {reg: "/gate/random/setEngineSeed (.*)", val: "engineSeed"},
            {reg: "^/vis.* (.*)", val: "visu"},
            {reg: "/run/beamOn (.*)", val: "beamOnEvents"},
            {reg: "/gate/application/setTotalNumberOfPrimaries (.*)", val: "totalNumberOfPrimaries"},
            {reg: "/gate/application/setTimeSlice (.*)", val: "timeSimu"},
            {reg: "[_a-zA-Z0-9\\-\\./]+\\.[_a-zA-Z][_a-zA-Z0-9\\-\\.]+", val: "otherFilesArray"}
        ];


        //init the ret[macFilesArray] with the mainMacFile
        ret["macFilesArray"].push(mainMacFile.name);
        function parseFile(fileIndex) {
            if (fileIndex === ret["macFilesArray"].length) {
                //we reached the end: resolve ret and return
                console.log("macFilesArray has " + ret["macFilesArray"].length + "elements");
                resolve(ret);
                return;

            } else {
                macFileName = ret["macFilesArray"][fileIndex];
                var macFile = getFileByName(macFileName, parentFolderId);
                if (macFile === null) {
                    alert("Macro file " + macFileName + " not found in the mac folder");
                    return;
                }
                var reader = new FileReader();
                reader.onload = function (e) {
                    console.log("Parsing macro file " + macFile.name);
                    // Read by lines
                    var lines = e.target.result.split('\n');
                    for (var lineIndex = 0; lineIndex < lines.length; lineIndex++) {
                        trimmedLine = lines[lineIndex].trim();
                        //if the line is not a comment, i.e. it doesn't start with #
                        if (!trimmedLine.match("^#")) {
                            for (var parseIndex = 0; parseIndex < parserArray.length; parseIndex++) {
                                var m = trimmedLine.match(parserArray[parseIndex].reg);
                                if (m) {
                                    //console.log("Found match " + m[1] + "for " + parserArray[parseIndex].val + " at line " + lineIndex);
                                    if (Array.isArray(ret[parserArray[parseIndex].val])) {
                                        //add element only if it does not exist
                                        if (ret[parserArray[parseIndex].val].indexOf(m[1]) === -1) {
                                            //console.log("element " + m[1] + " doesn't exist, adding it");
                                            ret[parserArray[parseIndex].val].push(m[1]);
                                        }
                                    } else
                                        ret[parserArray[parseIndex].val] = m[1];
                                    //each line contains max one match, i.e. max one value m to add to ret; if m found, go to next line
                                    break;
                                }
                            }
                        }//end if not a comment
                    }
                    parseFile(fileIndex + 1);
                };
                reader.readAsText(macFile);
            }//end else
        }// end function parseFile
        parseFile(0);
    });
}

//retrieve the file object corresponding to fileName from listOfFilesID
//TODO add something like getFileByNameWithoutExt for input files (e.g., .hdr/.img files, since only .hdr file will be parsed)
function getFileByName(fileName, parentFolderId) {
    var listOfFiles = document.getElementById(parentFolderId).files;
    //var myFile = listOfFiles[listOfFiles.map(function(x){return x.name}).indexOf(myFileName)];
    //For some reason, map doesn't seem to work, so looking for the file name ourselves
    var myFile = null;
    for (var searchIndex = 0; searchIndex < listOfFiles.length; searchIndex++) {
        if (listOfFiles[searchIndex].name === fileName) {
            myFile = listOfFiles[searchIndex];
            return myFile;
        }
    }
    if(myFile === null){
        alert("Warning: file "+fileName+" not found in parent folder");
    }
    return myFile;
}

//Get coupled files (e.g., .hdr/.img files, since only .hdr file will be parsed)
function getFilesByNameWithoutExtension(fileName, parentFolderId) {
    var listOfFiles = document.getElementById(parentFolderId).files;
    var myFiles = [];
    for (var searchIndex = 0; searchIndex < listOfFiles.length; searchIndex++) {
        if (listOfFiles[searchIndex].name.replace(/\.[^/.]+$/, "") === fileName.replace(/\.[^/.]+$/, "")) {
            myFiles.push(listOfFiles[searchIndex]);
        }
    }
    if(myFiles.length===0){
        alert("Warning: file "+fileName+" not found in parent folder");
    }
    return myFiles;
}

//retrive the list of macro and input files based on their names in dataArray.macFilesArray and dataArray.inputFilesArray
function getListOfFiles(dataArray, parentFolderId) {
    var myListOfFiles = [];
    var myFile = null;
    var myFiles = [];

    //add macro files
    for (var parseIndex = 0; parseIndex < dataArray.macFilesArray.length; parseIndex++) {
        var fileName = dataArray.macFilesArray[parseIndex];
        myFile = getFileByName(fileName, parentFolderId);
        //add file only if it doesn't exist
        if (myListOfFiles.indexOf(myFile) === -1) {
            myListOfFiles.push(myFile);
        }
    }

    //add input files, knowing that .hdr/.img files are coupled
    for (var parseIndex = 0; parseIndex < dataArray.inputFilesArray.length; parseIndex++) {
        var fileName = dataArray.inputFilesArray[parseIndex];
        myFiles = getFilesByNameWithoutExtension(fileName, parentFolderId);
        //add file only if it doesn't exist
        if (myListOfFiles.indexOf(myFiles[0]) === -1) {
            //pushing a list to myListOfFiles
            myListOfFiles.push.apply(myListOfFiles, myFiles);
        }
    }

    return myListOfFiles;
}

function isStatic(dataArray) {
    if (dataArray.timeSimu === "timeSimu") {
        return true;
    }
    return false;
}
function fillInInputs(fileName, dataArray) {
    var type = isStatic(dataArray) ? "stat" : "dyn";
    //TODO handle phaseSpace use-case
    var ps = "dummy";
    var parts = dataArray.totalNumberOfPrimaries;
    if (parts === "") {
        parts = "100";
    }
    if(dataArray.engineSeed !== "auto"){
        alert("SetEngineSeed is not auto. Please set the auto engine seed mode.");
        return;
    }
    if(dataArray.visu === "visu"){
        alert("Vizualisation found in the GATE macro files. Please remove any vis commands and start again.");
        return;
    }
    var inputsList = "GateInput = " + fileName + ", ParallelizationType = " + type + ", NumberOfParticles = " + parts + ", phaseSpace = " + ps;
    return inputsList;
}
