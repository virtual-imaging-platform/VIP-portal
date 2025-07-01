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
        var parsedValuesArray = {
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
            {reg: ".*\\s*mac/(.*\.mac)", val: "macFilesArray"},
            {reg: ".*\\s*data/(.*)", val: "inputFilesArray"},
            {reg: ".*\\s*output/(.*)", val: "outputFilesArray"},
            {reg: "/gate/random/setEngineSeed\\s*(.*)", val: "engineSeed"},
            {reg: "^/vis.*\\s*(.*)", val: "visu"},
            {reg: "/run/beamOn\\s*(.*)", val: "beamOnEvents"},
            {reg: "/gate/application/setTotalNumberOfPrimaries\\s*(.*)", val: "totalNumberOfPrimaries"},
            {reg: "/gate/application/setTimeSlice\\s*(.*)", val: "timeSimu"},
            {reg: "[_a-zA-Z0-9\\-\\./]+\\.[_a-zA-Z][_a-zA-Z0-9\\-\\.]+", val: "otherFilesArray"}
        ];

        //init the ret[macFilesArray] with the mainMacFile
        parsedValuesArray["macFilesArray"].push(mainMacFile.name);
        function parseFile(fileIndex) {
            if (fileIndex === parsedValuesArray["macFilesArray"].length) {
                //we reached the end: resolve parsedValuesArray and return
                resolve(parsedValuesArray);
            } else {
                var macFileName = parsedValuesArray["macFilesArray"][fileIndex];
                var macFile = getFileByName(macFileName, parentFolderId);
                checkAndThrow(macFile, "Macro file " + macFileName + " not found in the mac folder.");
                var reader = new FileReader();
                reader.onload = function (e) {
                    // Read by lines
                    var lines = e.target.result.split('\n');
                    for (var lineIndex = 0; lineIndex < lines.length; lineIndex++) {
                        parseLine(lines[lineIndex], parserArray, parsedValuesArray);
                    }
                    parseFile(fileIndex + 1);
                };
                reader.readAsText(macFile);
            }//end else
        }// end function parseFile
        parseFile(0);
    });
}

function checkAndThrow(varToCkeck, message) {
    if (varToCkeck === null) {
        throw message;
    }
    if (Array.isArray(varToCkeck)) {
        if (varToCkeck.length === 0) {
            throw message;
        }
    }

}

function parseLine(line, regexpArray, resultArray) {
    //remove all comments starting with #, then trim
    var trimmedLine = line.replace(/#.*/, "").trim();
    //if trimmedLine not empty after removing comments 
    if (trimmedLine) {
        for (var parseIndex = 0; parseIndex < regexpArray.length; parseIndex++) {
            var m = trimmedLine.match(regexpArray[parseIndex].reg);
            if (m) {
                if (Array.isArray(resultArray[regexpArray[parseIndex].val])) {
                    //add element only if it does not exist
                    if (resultArray[regexpArray[parseIndex].val].indexOf(m[1]) === -1) {
                        resultArray[regexpArray[parseIndex].val].push(m[1]);
                    }
                } else
                    resultArray[regexpArray[parseIndex].val] = m[1];
                //each line contains max one match, i.e. max one value m to add to resultArray; if m found, go to next line
                break;
            }
        }
    }//end if not a comment
}

//retrieve the file object corresponding to the exact fileName from listOfFilesID
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
    checkAndThrow(myFile, "Warning: file " + fileName + " not found in parent folder.");
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
    checkAndThrow(myFiles, "Warning: file " + fileName + " not found in parent folder.");
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
        checkFileIsInFolder(myFile, "mac");
        //add file only if it doesn't exist
        if (myListOfFiles.indexOf(myFile) === -1) {
            myListOfFiles.push(myFile);
        }
    }

    //add input files, knowing that .hdr/.img files are coupled
    for (var parseIndex = 0; parseIndex < dataArray.inputFilesArray.length; parseIndex++) {
        var fileName = dataArray.inputFilesArray[parseIndex];
        myFiles = getFilesByNameWithoutExtension(fileName, parentFolderId);
        checkFileIsInFolder(myFiles[0], "data");
        if (myListOfFiles.indexOf(myFiles[0]) === -1) {
            //pushing a list to myListOfFiles
            myListOfFiles.push.apply(myListOfFiles, myFiles);
        }
    }

    //Add Materials.xml file needed by default by Gate 9.2 onwards
    var materialsFileName = "Materials.xml";
    try {
        var myMaterialsFile = getFileByName(materialsFileName, parentFolderId);
        myListOfFiles.push(myMaterialsFile);
    } catch (e) {
        alert(e.toString() + " This shouldn't be an issue for Gate version inferior to 9.2. Please add this file to your data folder for Gate 9.2 and above.");
    }

    return myListOfFiles;
}

function fillInInputs(fileName, dataArray) {
    if (dataArray.engineSeed !== "auto") {
        throw "SetEngineSeed is not auto. Please set the auto engine seed mode.";
    }
    if (dataArray.visu === "visu") {
        throw "Vizualisation found in the GATE macro files. Please remove any vis commands and start again.";
    }
    var inputsList = "gateInput = " + fileName + ", macfileName = " + dataArray.macFilesArray[0];
    return inputsList;
}

//checks if file (file object) is located at the base of the folder given as arg (String)
function checkFileIsInFolder(file, folder) {
    checkAndThrow(file, "File not found");
    var path = file.webkitRelativePath;
    var pathArray = path.split('/');
    if (pathArray.length !== 3) {
        var message = "Folder " + folder + " should be placed at the base of the parent folder";
        throw message;
    }
    //only consider the first level folder
    if (pathArray[1] !== folder) {
        var message = "File " + file.name + " was not found in " + folder + " folder";
        throw message;
    }
}
