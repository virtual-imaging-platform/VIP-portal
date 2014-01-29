#!/bin/bash


set -u

if [ $# != 5 ]
then
    echo "usage: merge.sh input.zip output_dir_lfn timestamp result.tar.gz events_file"
    echo "       assumes that the merge_release.tar.gz contains the merge executable plus shared libs"
    echo "       assumes that the output_dir_lfn contains the tar.gz with all outputs"
    echo "       result.tar.gz is the names of the final output "
    echo "       events_file is the names of the file containing the nb of merged events "
    echo "       the final output will be copied in the output_dir_lfn"
    exit 6
fi

DIR=$2
TIMESTAMP=$3
RESULT=$4
INPUT=$1
MERGE="merge_release.tar.gz"
EVENTS_FILE=$5

function error {
echo "ERROR: $1"
exit 1
}

function warning {
echo "WARNING: $1"
}

function info {
echo "=============================================================="
echo $1
echo "=============================================================="
}

function unziptar {
	zipname=`echo $1 | grep '\.zip'`
	echo $zipname
	tarname=`echo $1 | grep '\.tar.gz'`
	echo $tarname
	tgzname=`echo $1 | grep '\.tgz'`
	echo $tgzname
	if [ "x$zipname" != "x" ]
		then
		echo "ZIP File"
		./unzip $1
	else
		if [ "x$tarname" != "x" ]
               		then
                	echo "TARGZ File"
                	tar -zxvf $1
		else
			if [ "x$tgzname" != "x" ]
               			then
                		echo "TGZ File"
                		tar -zxvf $1
			else
                		echo "Unknown File Type"
				exit 6
			fi			
		fi
	fi
	
	if [ $? != 0 ]
		then
        	echo "A problem occurred while untargzing the input: giving up!"
        	exit 2
	fi
}

function getSavedActorNames {
awk '$1 ~ /\/gate\/actor\/.*\/save/ && $1 !~ /.*saveEveryNSeconds/{print $1}' mac/*.mac | awk -F '/' '{print $4}'
if [ $? != 0 ]
then	
    error "cannot get list of saved actors"
fi
}

function getActorType {
local NAME=$1
awk -v N=${NAME} '$1=="/gate/actor/addActor" && $3==N {print $2}' mac/*.mac
if [ $? != 0 ]
then	
    error "cannot get type of actor ${NAME}"
fi
}

function getFileName {
local ACTORNAME=$1
basename `awk -v N=${ACTORNAME} '$1 == "/gate/actor/"N"/save" {print $2}' mac/*.mac`
if [ $? != 0 ]
then
    error "cannot get file name for actor ${ACTORNAME}"
fi
}

function getOutputFileNames {
awk '$1 ~ /\/gate\/output\/.*\/setFileName/ {print $2}' mac/*.mac | awk -F '/' '{print $NF}'
if [ $? != 0 ]
then
    error "cannot get list of saved actors"
fi
}

function getOutputType {
local NAME=$1
awk -v N=${NAME} '$2 ~ N {print $1}' mac/*.mac | awk -F '/' '{print $4}'
if [ $? != 0 ]
then
    error "cannot get type of actor ${NAME}"
fi
}

function getActorDim {
local ACTORNAME=$1
DIM=1
#basename `awk -v N=${ACTORNAME} '$1 == "/gate/actor/"N"/setResolution" {print $2}' mac/*.mac`
ndims=`awk -v N=${ACTORNAME} '$1 == "/gate/actor/"N"/setResolution" {print NF}' mac/*.mac`
test $ndims
if (($?!=0))
then
    DIM=1
else
    dims=`awk -v N=${ACTORNAME} '$1 == "/gate/actor/"N"/setResolution" {print $2,$3,$4}' mac/*.mac`
    count_dim=0
    for val in $dims
    do
        if((${val}==1))
        then
            count_dim=`echo $[${count_dim}+1]`
        fi
    done
    DIM=`echo $[3-${count_dim}]`
fi
echo "INFO: dim is $DIM"
}



function downloadOutputs {
#export LFC_HOST=lfc-biomed.in2p3.fr
declare -a list_pb_files
success_files=0
#MAINDIR=`dirname $1`
echo "OUTPUTDIR is ${DIR}"
INDEX=`expr match "${DIR}" .*/grid`
echo "INDEX is ${INDEX}"
#5 is the size of string "/grid"
GLITEMAINDIR=${DIR:`expr ${INDEX} - 5`}
echo "GLITEMAINDIR is ${GLITEMAINDIR}"
lfc-ls -d ${GLITEMAINDIR} > /dev/null
if [ $? != 0 ]
then
    warning "Unable to list directory ${GLITEMAINDIR}: sleeping 10s and trying once more!"
    sleep 10
    lfc-ls -d ${GLITEMAINDIR} > /dev/null
    if [ $? != 0 ]
    then
        error "Unable to list directory ${GLITEMAINDIR}: giving up !"
    fi
fi

#download inputs to retrieve actor names in mac files

unziptar ${INPUT}

#for i in `lfc-ls ${GLITEMAINDIR} | grep zip`
#do
#    LINE="lcg-cp -v --connect-timeout 30 --sendreceive-timeout 900 --bdii-timeout 30 --srm-timeout 300 lfn:${GLITEMAINDIR}/$i file:${PWD}/$i"
#    echo ${LINE}
#    ${LINE}
#    if [ $? != 0 ]
#    then
#        warning "lcg-cp failed: sleep 2"
#        sleep 2	
#        ${LINE}
#        if [ $? != 0 ]
#        then
#            error "Cannot copy input file"
#        fi
#    else
#        unzip $i
#    fi
#done

#download outputs
info "Listing ${GLITEMAINDIR}"

for i in `lfc-ls ${GLITEMAINDIR} | grep -iv garbage | grep -iv uploadtest | grep tar.gz`
do
    if [[ $i != *garbage* ]]
    then
        #test against lcg-cp bug (see savannah bug 60334 ; should be fixed in lcg_utils 1.7.11)
        NREP=`lcg-lr lfn:${GLITEMAINDIR}/$i | wc -l | awk '{print $1}'`
        if [ ${NREP} -gt 0 ]
        then
            LINE="lcg-cp -v --connect-timeout 30 --sendreceive-timeout 900 --bdii-timeout 30 --srm-timeout 300 lfn:${GLITEMAINDIR}/$i file:${PWD}/$i"
            echo ${LINE}
            ${LINE}
            if [ $? != 0 ]
            then
                warning "lcg-cp failed: sleep 2 and add $i to list_pb_files"
                sleep 2
                list_pb_files[${#list_pb_files[*]}]=$i
                #${LINE}
                #if [ $? != 0 ]
                #then
                #        echo "Unable to download file lfn:${GLITEMAINDIR}/output/$i: giving up."
                #        exit 1
                #fi
            else
                tar zxvf $i
                let "success_files = $success_files + 1"
            fi
        else
            warning "Skipping file ${GLITEMAINDIR}/$i because it has no replica"
        fi
    fi
done
index=0
element_count=${#list_pb_files[@]}
while (($index<$element_count))
do    # List all the elements in the array.
    LINE="lcg-cp -v --connect-timeout 30 --sendreceive-timeout 900 --bdii-timeout 30 --srm-timeout 300 lfn:${GLITEMAINDIR}/${list_pb_files[$index]} file:${PWD}/${list_pb_files[$index]}"
    echo ${LINE}
    ${LINE}
    if [ $? != 0 ]
    then
        warning "lcg-cp for lfn:${GLITEMAINDIR}/${list_pb_files[$index]} failed: go to the next file"
        #sleep 2
    else
        tar zxvf ${list_pb_files[$index]}
        let "success_files = $success_files + 1"
    fi
    let "index = $index + 1"
    #echo "sleeping 2"
    #sleep 2
done

if (($success_files<2))
then
    warning "The number of successefully downloaded outputs is $success_files"
fi
}


function mergeResults {
info "LISTING RESULTS"
find .
info "LISTED RESULTS"

info "REMOVING BAD PARTIAL DIRECTORIES"
local rundir="."
nboutputfiles="$(for outputdir in $(find "${rundir}" -mindepth 1 -type d -name 'output*'); do find "${outputdir}" -regextype 'posix-extended' -type f -regex '.*(hdr|root|txt)' | wc -l; done | sort -r | head -n 1)"
#nboutputfiles="$(find "${rundir}" -regextype 'posix-extended' -mindepth 2 -type f -regex '.*\.(hdr|root|txt)' | awk -F '/' '{ print $NF }' | sort | uniq | wc -l)"
echo "detected ${nboutputfiles} different filename in output partial output directories"
for outputdir in $(find "${rundir}" -mindepth 1 -type d -name 'output*')
do
    this_partial_file_count="$(find "${outputdir}" -regextype 'posix-extended' -type f -regex '.*(hdr|root|txt)' | wc -l)"
    test $nboutputfiles -eq $this_partial_file_count && continue
    echo "removing ${outputdir} because some partial results are missing (only ${this_partial_file_count} files)"
    rm -r "${outputdir}"
done
info "REMOVED BAD PARTIAL DIRECTORIES"

info "MERGING RESULTS"

test -f "gate_power_merge.sh" || error "can't find gate_power_merge.sh : merge release too old"

local gate_power_merge_version="$( awk '/\"!+ .* !+\"/ { print $6; }' gate_power_merge.sh )"

./gate_power_merge.sh .

test $? -eq 0 || error "error while calling gate_power_merge.sh version ${gate_power_merge_version}"

info "MERGED RESULTS"

}

function mergeImageUncertainty {
info "MERGING IMAGE UNCERTAINTY"

local mergedStatFile="results/statSCP.txt"
if [ -z "${mergedStatFile}" ]
then
    warning "can't locate merged stat file. unable to compute image uncertainties."
    return
fi

local totalEvents="$(grep "NumberOfEvents" "${mergedStatFile}" | cut -d' ' -f4)"
echo "totalEvents=${totalEvents}"

for NAME in `getSavedActorNames`
do
    local TYPE=`getActorType ${NAME}`
    echo "currentActor=${NAME} type=${TYPE}"

    test "${TYPE}" == "DoseActor" || continue # only for dose actor

    local FILE=`getFileName ${NAME}`
    local EXTENSION=`echo ${FILE} | awk -F '.' '{print $NF}'`
    local BASENAME=`basename ${FILE} .${EXTENSION}`
    echo "BASENAME=${BASENAME}"

 # compute uncertainty using clitkImageUncertainty for hdr
    # for dose
    local summed="results/${BASENAME}-Dose.hdr"
    local squared="results/${BASENAME}-Dose-Squared.hdr"
    local uncertoutput="results/${BASENAME}-Dose-Uncertainty.hdr"
    test -f "${summed}" && test -f "${squared}" && test -f "${uncertoutput}" && \
    echo "merging dose for actor ${NAME}" && \
    ./clitkImageUncertainty -i "${summed}" -s "${squared}" -o "${uncertoutput}" -n "${totalEvents}" && \
    echo "SUCCESS"

    # for edep
    local summed="results/${BASENAME}-Edep.hdr"
    local squared="results/${BASENAME}-Edep-Squared.hdr"
    local uncertoutput="results/${BASENAME}-Edep-Uncertainty.hdr"
    test -f "${summed}" && test -f "${squared}" && test -f "${uncertoutput}" && \
    echo "merging edep for actor ${NAME}" && \
    ./clitkImageUncertainty -i "${summed}" -s "${squared}" -o "${uncertoutput}" -n "${totalEvents}" && \
    echo "SUCCESS"

    # compute uncertainty using clitkImageUncertainty for mhd
    # for dose
    local summed="results/${BASENAME}-Dose.mhd"
    local squared="results/${BASENAME}-Dose-Squared.mhd"
    local uncertoutput="results/${BASENAME}-Dose-Uncertainty.mhd"
    test -f "${summed}" && test -f "${squared}" && test -f "${uncertoutput}" && \
    echo "merging dose for actor ${NAME}" && \
    ./clitkImageUncertainty -i "${summed}" -s "${squared}" -o "${uncertoutput}" -n "${totalEvents}" && \
    echo "SUCCESS"

    # for edep
    local summed="results/${BASENAME}-Edep.mhd"
    local squared="results/${BASENAME}-Edep-Squared.mhd"
    local uncertoutput="results/${BASENAME}-Edep-Uncertainty.mhd"
    test -f "${summed}" && test -f "${squared}" && test -f "${uncertoutput}" && \
    echo "merging edep for actor ${NAME}" && \
    ./clitkImageUncertainty -i "${summed}" -s "${squared}" -o "${uncertoutput}" -n "${totalEvents}" && \
    echo "SUCCESS"
done

info "MERGED IMAGE UNCERTAINTY"
}

function merge_bin_mpg_image {
  local rundir="."
  local nboutputfiles=`find . -mindepth 1 -type d -name 'output*' | wc -l`
  if ((${nboutputfiles}>1))
  then
        echo "Entering bin_mpg image merger"
	dos2unix ./mac/*.mac
        echo "Merger is mpgImageMerger"
	local nbWin=$[`awk '$1 == "/gate/output/projection/addInputDataName" {print $1}' ./mac/*.mac | wc -l ` + 1]
	if [ "$nbWin" != "" ]
        then
	        local runs=`awk '$1 == "/gate/application/setTimeStop" {print $2}' ./mac/*.mac`
        	local t=`awk '$1 == "/gate/SPECThead/ring/setRepeatNumber" {print $2}' ./mac/*.mac`
        	local name=`ls output* | grep .bin | head -n 1`
        	local rad=`echo $name | awk -F'_' '{print $1}'`
        	local fn=`cat ./mac/*.mac | grep $rad | awk '$1 == "/gate/output/projection/setFileName" {print $2}' | awk -F'/' '{print $NF}'`
		if [ "$fn" != "" ]
                then
       			#ImageMerger -d /Users/merry/Development/Milan_tomo/cluster_data/merge -f HitList_tomo -n 2 -r 15 -e 6 -t 4 --hitlist
        		echo "Launching mpgImageMerger -d ./ -f ${fn} -r $runs -e ${nbWin} -t $t --hitlist"
        		./mpgImageMerger -d ./ -f ${fn} -r ${runs} -e ${nbWin} -t $t --hitlist > /dev/null > /dev/null || warning "error while calling mpgImageMerger"
        		#./mpgImageMerger -d ./ -f ${fn} -n ${nboutputfiles} -r ${runs} -e ${nbWin} -t $t --hitlist > /dev/null > /dev/null || warning "error while calling mpgImageMerger"

			mv *.bin  results/
		else
                        echo "No file name, no mpgMerger"
                fi
        else
                 echo "No nbWin, no mpgMerger"
        fi

  else
	 echo "No bin_mpg image"
  fi

}


mkdir output || error "Unable to create directory output"

test -f "${MERGE}" || error "Can't found merge release archive"
cp ${MERGE} output/
mv ${INPUT} output/
cp unzip output/
cd output

tar zxvf `basename ${MERGE}` || error "A problem occurred while untargzing the merge release"

#do it as late as possible so that we don't have to wait for this heavy download to detect failures (e.g. bad merge archive).
downloadOutputs ${DIR}

export LD_LIBRARY_PATH=.:$LD_LIBRARY_PATH

mergeResults
mergeImageUncertainty
merge_bin_mpg_image


#send error in case nothing was merged
TOTAL_MERGED_FILES=0
TOTAL_MERGED_FILES=`ls results/ | wc -l`
if ((${TOTAL_MERGED_FILES}==0))
then
    warning "Total events merged by all merging procedures is 0, there must be a No Retry error! Cleaning up and exiting 1200"
    rm -Rf output
    rm -f ${MERGE}
    exit 1200
fi

info "CREATING MERGED ARCHIVE"
tar -czvf ${RESULT} results/*
mv $RESULT ../
info "CREATED MERGED ARCHIVE"

cd ..
echo "Deprecated" > $EVENTS_FILE

#cleanup in case it's a local execution
rm -Rf output
rm -f ${MERGE}
