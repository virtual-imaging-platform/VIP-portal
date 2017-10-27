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

function parseMacFile(mainMacFileID, macFilesListID) {
    var mainMacFile = document.getElementById(mainMacFileID).files[0];
    //var macFilesList = document.getElementById(macFilesListID).files;
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
            //TODO: add stop funtion
            console.log("we should stop");
            console.log("macFilesArray has " + ret["macFilesArray"].length + "elements");
            return;
        } else {
            macFileName = ret["macFilesArray"][fileIndex];
            var macFile = getFileByName(macFileName, macFilesListID);
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

}

//retrieve the file object corresponding to fileName from listOfFilesID
//TODO add something like getFileByNameWithoutExt for input files (e.g., .hdr/.img files, since only .hdr file will be parsed)
function getFileByName(fileName, listOfFilesID) {
    var listOfFiles = document.getElementById(listOfFilesID).files;
    //var myFile = listOfFiles[listOfFiles.map(function(x){return x.name}).indexOf(myFileName)];
    //For some reason, map doesn't seem to work, so looking for the file name ourselves
    var myFile = null;
    for (var searchIndex = 0; searchIndex < listOfFiles.length; searchIndex++) {
        if (listOfFiles[searchIndex].name === fileName) {
            myFile = listOfFiles[searchIndex];
            break;
        }
    }
    return myFile;
}
