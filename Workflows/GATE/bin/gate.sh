#!/bin/bash -l

SPLIT="unknown"

function error {
    local D=`date`
    echo "[ERROR - ${D}] $*">&2
    exit 1
}

function float_eval()
{
    local float_scale=10
    local stat=0
    local result=0.0
    if [[ $# -gt 0 ]]; then
        result=$(echo "scale=$float_scale; $*" | bc -q 2>/dev/null)
        stat=$?
        if [[ $stat -eq 0  &&  -z "$result" ]]; then stat=1; fi
    fi
    echo $result
    return $stat
}

function checkdir {
	lfc-ls $1 > /dev/null
	if [ $? != 0 ]
	then
        	echo "Directory $1 not found!"
        	exit 6
	fi
}

function downloadDir {
	checkdir $1
	LOCALDIR=`basename $1`
	mkdir -p ${LOCALDIR}
	for i in `lfc-ls $1`
	do 
		lcg-cp -v lfn:$1/$i file:$PWD/${LOCALDIR}/$i
		if [ $? != 0 ]
		then
			echo "lcg-cp failed: trying once more"
			lcg-cp -v lfn:$1/$i file:$PWD/${LOCALDIR}/$i
			if [ $? != 0 ]
	                then
				echo "Unable to download file lfn:$1/$i: giving up."
				exit 1
			fi
		fi
	done 
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


#takes as args resultsDIR SEED SE 
function saveAllResults {

   lfc-mkdir $1/output
   tar -czf output-$2.tar.gz output/*
   lcg-cr -v -d $3 -l lfn:$1/output/output-$2.tar.gz file:$PWD/output-$2.tar.gz
   if [ $? != 0 ]
   then
        echo "lcg-cr failed: trying once more"
   	lcg-cr -v -d $3 -l lfn:$1/output/output-$2.tar.gz file:$PWD/output-$2.tar.gz
        if [ $? != 0 ]
        then
                echo "Unable to upload file output-$2: proceeding"
        fi
   fi
}

#takes as args initial mac file name, output mac file  and numberOfEvents
function stopOnScript {

	INIT_PATTERN=`awk '$1=="/gate/run/initialize" || $1=="/run/initialize" {print $1}' $1`
        if [ "${INIT_PATTERN}" = "" ]
        then
                echo "Error : no run/initialize or /gate/run/initialize command, exiting 6"
                exit 6
        fi
	sed 's%^\s*'$INIT_PATTERN'%/gate/actor/addActor                      SimulationStatisticActor statSCP\n/gate/actor/statSCP/save        output/statSCP.txt\n/gate/actor/addActor     StopOnScriptActor stop\n/gate/actor/stop/save                ./stopOnScript.sh\n/gate/actor/stop/saveEveryNSeconds     '$3'\n/gate/actor/stop/saveAllActors        true\n'$INIT_PATTERN'\n%g' $1 > $2

	if [ "${SPLIT}" = "event" ]
	then
	    echo "echo -e \"\n\`grep NumberOfEvents output/*statSCP*\` \n\" " > stopOnScript.sh
	    echo "NBPARTICLE=\`grep NumberOfEvents output/*statSCP* | awk -F'= ' '{print \$2}'\`" >> stopOnScript.sh
	    echo " echo \"Current number of simulated particles ==== \" \$NBPARTICLE " >> stopOnScript.sh
	    echo "${DIRACPYTHON} testenv.py false \$NBPARTICLE" >> stopOnScript.sh
	    else
	    if [ "${SPLIT}" = "time" ]
		then
		echo "echo -e \"\n\`grep ElapsedSimulationTime output/*statSCP*\` \n\" " > stopOnScript.sh
		echo "ELAPSEDSECONDS=\`grep ElapsedSimulationTime output/*statSCP* | awk -F'= ' '{print \$2}'\`" >> stopOnScript.sh
		echo " echo \"Elapsed time  ==== \" \${ELAPSEDSECONDS} " >> stopOnScript.sh
		echo "PERCENTAGE=\`echo \"(100*\${ELAPSEDSECONDS})/(${TIME_STOP}-${TIME_START})\" | bc | awk '{printf \"%.0f\n\", \$1}'\`" >> stopOnScript.sh
		echo "${DIRACPYTHON} testenv.py false \${PERCENTAGE}" >> stopOnScript.sh
	    else
		error "Unknown SPLIT mode: ${SPLIT}"
	    fi
	fi
	echo "simu_status=\`cat ../std.in\`" >>stopOnScript.sh
	#echo "echo Simu Status is \$simu_status" >> stopOnScript.sh
	echo "echo \"Simu Status is \$simu_status\"" >>stopOnScript.sh
        echo "Simu Status is \$simu_status"
        echo "if [ \"\$simu_status\" == \"stop\" ]; then">>stopOnScript.sh
        echo "  echo \"0\" > errorcode.txt">>stopOnScript.sh
        echo "  exit 1" >>stopOnScript.sh
        echo "fi" >>stopOnScript.sh
        echo "exit 0" >>stopOnScript.sh
        chmod a+x stopOnScript.sh

}


#checks if auto seed exists or if it has to pass the right rndm file; arguments are: init/mac, final.mac, rndm file
function checkEngineSeed {
        cp $1 $2
        grep '^\s*\/gate\/random\/setEngineSeed' $1
        if [ $? == 0 ]
        then
                echo "auto seed exists, nothing to do"
        else
                echo "grep '^\s*\/gate\/random\/resetEngineFrom rndm\/[A-Za-z0-9]*' $1"
                grep '^\s*\/gate\/random\/resetEngineFrom rndm\/[A-Za-z0-9]*' $1
                if [ $? == 0 ]
                then
                        sed -e 's/^\s*\/gate\/random\/resetEngineFrom rndm\/[A-Za-z0-9.-]*/\/gate\/random\/resetEngineFrom rndm\/'$3'\n/g' $1 > $2
                        echo "Setting /gate/random/resetEngineFrom rndm/'$3' "
                fi
        fi

}

#replaces the total nb of part with the right value for static wfl; 
#$1 the init mac file, 
#$2 the modified mac file 
#$3 the total nb of jobs
#$4 is the job number (id)

function setStatNbPartOrTime {

local INIT_MAC=$1
local END_MAC=$2
local TOTAL_JOBS=$3
local JOB_ID=$4

echo "Number of jobs is $TOTAL_JOBS and job id is $JOB_ID"

local EVENT_PATTERN=`awk '$1=="/run/beamOn" || $1=="/gate/application/SetTotalNumberOfPrimaries" || $1=="/gate/application/setTotalNumberOfPrimaries" {print $1}' ${INIT_MAC}`
N_EVENTS=`awk '$1=="/run/beamOn" || $1=="/gate/application/SetTotalNumberOfPrimaries" || $1=="/gate/application/setTotalNumberOfPrimaries" {print $2}' ${INIT_MAC}`

local SLICE_TIME_PATTERN=`awk '$1=="/gate/application/setTimeSlice" {print $1}' ${INIT_MAC}`
local SLICE_TIME=`awk '$1=="/gate/application/setTimeSlice" {print $2}' ${INIT_MAC}`
local TIME_UNIT=`awk '$1=="/gate/application/setTimeSlice" {print $3}' ${INIT_MAC}`

local TIME_START_PATTERN=`awk '$1=="/gate/application/setTimeStart" {print $1}' ${INIT_MAC}`
TIME_START=`awk '$1=="/gate/application/setTimeStart" {print $2}' ${INIT_MAC}`
local TIME_START_UNIT=`awk '$1=="/gate/application/setTimeStart" {print $3}' ${INIT_MAC}`

local TIME_STOP_PATTERN=`awk '$1=="/gate/application/setTimeStop" {print $1}' ${INIT_MAC}`
TIME_STOP=`awk '$1=="/gate/application/setTimeStop" {print $2}' ${INIT_MAC}`
local TIME_STOP_UNIT=`awk '$1=="/gate/application/setTimeStop" {print $3}' ${INIT_MAC}`

if [ "${N_EVENTS}" = "" ]
  then
      
    if [ "${SLICE_TIME_PATTERN}" != ""  ] && [ "${TIME_START_PATTERN}" != ""  ] &&  [ "${TIME_STOP_PATTERN}" != ""  ]
    then
	#split in time
	echo "Found ${SLICE_TIME_PATTERN}: splitting in time"
	SPLIT="time"
	local START_PATTERN=`awk '$1=="/gate/application/startDAQ" || $1=="/gate/application/start" {print $1}' ${INIT_MAC}`
	if [ "${START_PATTERN}" = "" ]
	    then
	    error "Cannot find start or startDAQ"
	    fi
	if [ "${TIME_UNIT}" != "${TIME_STOP_UNIT}" ] || [ "${TIME_UNIT}" != "${TIME_START_UNIT}" ]
	then
	    error "All time units must be the same"
	fi
	local TIME_PER_JOB=$(float_eval "($TIME_STOP-$TIME_START)/${TOTAL_JOBS}")
	local TIME_START_JOB=$(float_eval "${TIME_START}+${TIME_PER_JOB}*${JOB_ID}")
	local TIME_STOP_JOB=$(float_eval "${TIME_START}+${TIME_PER_JOB}*(${JOB_ID}+1)")
	
	local MAX_JOB_ID=$[${TOTAL_JOBS}-1]
	if [ "${JOB_ID}" = "${MAX_JOB_ID}" ]
	then
	    TIME_STOP_JOB=${TIME_STOP}
	fi
	sed 's%^\s*'${START_PATTERN}'.*%'/gate/application/startDAQCluster'     '${TIME_START_JOB}' '${TIME_STOP_JOB}' 0 '${TIME_UNIT}'\n%g' ${INIT_MAC} > ${END_MAC} 
    else
	#error
	error "Macro must contain  \"SetTotalNumberOfPrimaries\" or setTime{Slice,Start,Stop}"
    fi
  else
    #split in particles
    echo "Found ${EVENT_PATTERN}: splitting in particles"
    SPLIT="event"
    echo "init Nb of particles is ${N_EVENTS}"
    
    #local EVENTS=`echo $[${N_EVENTS}/${TOTAL_JOBS}]`
    EVENTS=`echo $[${N_EVENTS}/${TOTAL_JOBS}]`
    if [ ${JOB_ID} == 0 ]
    then
        EVENTS=`echo $[${EVENTS}+${N_EVENTS}-${TOTAL_JOBS}*${EVENTS}]`
    fi
    echo "I'm going to simulate ${EVENTS} particles among the ${N_EVENTS}"	 
    sed 's%^\s*'${EVENT_PATTERN}'.*%'${EVENT_PATTERN}'     '${EVENTS}'\n%g' ${INIT_MAC} > ${END_MAC} 
fi 

}



#set the polling frequency of diane pilot
function setPollingFrequency {
	echo "Changing polling frequency of pilot:" $1 "seconds."
	echo $1 > ../pollingFrequency.txt
}

#export environment variables needed if the external release is used
function exportEnvVar {

	export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:.
	new_G4LEDATA=`ls | grep -i G4EMLOW`
	echo "exporting G4LEDATA=$PWD/$new_G4LEDATA"
	export G4LEDATA=$PWD/$new_G4LEDATA

	new_G4ABLA=`ls | grep -i G4ABLA`
	echo "exporting G4ABLADATA=$PWD/$new_G4ABLA"
	export G4ABLADATA=$PWD/$new_G4ABLA
	new_G4NDL=`ls | grep -i G4NDL`
	echo "exporting G4NEUTRONHPDATA=$PWD/$new_G4NDL"
	export G4NEUTRONHPDATA=$PWD/$new_G4NDL
	new_PhotonEvaporation=`ls | grep -i PhotonEvaporation`
	echo "exporting G4LEVELGAMMADATA=$PWD/$new_PhotonEvaporation"
	export G4LEVELGAMMADATA=$PWD/$new_PhotonEvaporation
	new_RadioactiveDecay=`ls | grep -i RadioactiveDecay`
	echo "exporting G4RADIOACTIVEDATA=$PWD/$new_RadioactiveDecay"
	export G4RADIOACTIVEDATA=$PWD/$new_RadioactiveDecay
	new_G4NEUTRONXS=`ls | grep -i G4NEUTRONXS`
	echo "exporting G4NEUTRONXSDATA=$PWD/$new_G4NEUTRONXS"
	export G4NEUTRONXSDATA=$PWD/$new_G4NEUTRONXS

	if ( test -d root-forgatelab ) ; then
		echo "ROOTSYS_before = $ROOTSYS"
		export ROOTSYS=$PWD/root-forgatelab/
		echo "ROOTSYS_after = $ROOTSYS"

		echo "ls root (non static)"
		ls $ROOTSYS/
		ls $ROOTSYS/etc

		ls $ROOTSYS/etc/plugins/TVirtualStreamerInfo
		du -hs $ROOTSYS
		cat $ROOTSYS/etc/plugins/TVirtualStreamerInfo/P010_TStreamerInfo.C
	else
		echo "Folder root-forgatelab does not exist, no ROOTSYS export"
	fi
}

if [ $# != 9 ]
then
	echo "usage: gate.sh release_fgate.tar.gz input.tar.gz seedNumber totalSeedNumber macfile_name stoponscript(s) wrappertype output_name.tar.gz"
	echo "       assumes that release_fgate.tar.gz containes the executable fGate, the fgate_shared_libs.tar.gz and the G4EMLOW.tar.gz"
	echo "       assumes that input files are organized like mac/*.mac, data/, materials/"
	echo "       assumes that macfile_name is to be found in mac/*"
	echo "       assumes that stoponscript is the number of seconds between two stoponscript"
	echo "       assumes that  wrappertype is dyn or sys"
	echo "       outputs must be put in output_name.tar.gz"
	exit 6
fi

RELEASE=$1
INPUT=$2
SEED=$3
TOTALSEED=$4
OUTPUT=$9
#MACFILE=$5
GATEALIAS=$5
STOP=$6
WRAPPERTYPE=$7
EVENTS=0
#mkdir fgate
#cd fgate

setPollingFrequency $STOP
if [ $? != 0 ]
then
        echo "A problem occurred while changing polling frequency of pilot: giving up!"
        exit 2
fi
echo "Polling frequency of pilot:" `cat ../pollingFrequency.txt` "seconds."

#tar zxf $INPUT
echo "unzipping inputs"
unziptar ${INPUT}
if [ $? != 0 ]
then
        echo "A problem occurred while untargzing the input: giving up!"
        exit 2
fi

echo "unzipping release"
tar zxf ${RELEASE}
if [ $? != 0 ]
then
        echo "A problem occurred while untargzing the release: giving up!"
        exit 2
fi

echo "unzipping gate shared libs"
tar zxf *gate_shared_libs.tar.gz
if [ $? != 0 ]
then
	echo "A problem occurred while untargzing the shared libs: giving up!"
	exit 2
fi

#removing libresolv.so to avoid DNS problems
echo "removing libresolv.so"
rm -rf libresolv.so*

#MACFILE is no longer an input param
MACFILE=`cat wfl_config.txt`
echo "Main MACFILE is ${MACFILE}"
echo "ALIAS is ${GATEALIAS}"

#if Gate already installed locally and  $GATE_RELEASE points to the Gate executable, then use it instead of the downloaded release
localInstall=`echo $GATE_RELEASE`

if [ "x${localInstall}" != "x" ]
then
	EXECFILE=${localInstall}	
	
	if ( test -f  $EXECFILE ) ; then
		echo "Local Gate installation found; using $EXECFILE instead of the grid release"		
        else
		echo "Could not find the local installation indicated by GATE_RELEASE=$EXECFILE. Exiting 6"
		exit 6
	fi

else
	if ( test -f  Gate ) ; then
        	EXECFILE="./Gate"	
        	echo "Exec file is Gate"
	else

		if ( test -f  fGate ) ; then
        		EXECFILE="./fGate"	
        		echo "Exec file is fGate"
		else
                	echo "No exec file found (Exec file is neither Gate nor fGate)!"
                	exit 6
        	fi
	fi

	echo "Using the grid release with the executable $EXECFILE"
	chmod 755 ${EXECFILE}
	exportEnvVar

fi

#executes fgate
if ((${SEED}<10))
then
	NUM=000${SEED}
else
	if ((${SEED}<100))
	then
		NUM=00${SEED}
	else
		if ((${SEED}<1000))
		then
			NUM=0${SEED}
		else
			NUM=${SEED}
		fi
	fi
fi

mkdir output
mkdir output-${NUM}


#dos2unix mac/${MACFILE}
./dos2unix mac/*.mac
./dos2unix data/GateMaterials.db

#!!If StopOnScript simulate all aprticles!!!
#EVENT=`expr ${ORIG} / ${TOTALSEED}`

if [ ${WRAPPERTYPE} == "stat" ]
then 
	setStatNbPartOrTime mac/${MACFILE} mac/tmp-adapted-${MACFILE} ${TOTALSEED} ${SEED}

else
	if [ ${WRAPPERTYPE} == "dyn" ]
	then 
		SPLIT="event"
		EVENT=${N_EVENTS}
		cp  mac/${MACFILE} mac/tmp-adapted-${MACFILE}
	else
	        error "the wrapper type is neither static nor dynamic, exiting..."
	fi
fi
echo "***********************************************"


ap_start=`grep '^\s*/gate/application/start' mac/${MACFILE}`

stopOnScript mac/tmp-adapted-${MACFILE} mac/tmp2-adapted-${MACFILE} ${STOP}
checkEngineSeed mac/tmp2-adapted-${MACFILE} mac/adapted-${MACFILE} status${NUM}.rndm

cp mac/adapted-${MACFILE} output/adapted-${MACFILE}


echo "this is the content of mac/adapted-${MACFILE} "
cat mac/adapted-${MACFILE}
echo "*************************Executing ${EXECFILE} mac/adapted-${MACFILE}***********************"

echo "****lauching the Dirac python Client ******" 
tar -zxvf testenv.tar.gz
echo "####Fin execution DIRAC CLIENT#########"

touch std.in
touch output/statSCP.txt

echo "Executing '${EXECFILE} mac/adapted-${MACFILE}' "
${EXECFILE} mac/adapted-${MACFILE}; echo $? > exitcode
finished=`ls | grep exitcode`

exitfgate=`cat exitcode`
if [ $exitfgate != "0" ]
then
	echo "[WARNING] ${EXECFILE} exited with a non-zero code. This is the macro file used:"
	#cat mac/adapted-${MACFILE}
	exit ${exitfgate}
fi

part=0
if ( test -f  output/statSCP.txt ) ; then
	NBOUT=`ls output/ | grep -v ".mac" | wc -l`
	if [ ${NBOUT} -lt 2 ]
	then
		echo "error: less than one real output!"
                exit 6
	fi


	if [ "${SPLIT}" = "event" ]
	then
            part=`grep NumberOfEvents output/*statSCP* | awk -F'= ' '{print $2}'`
            echo "Number of particules is: " $part
	else
	    if [ "${SPLIT}" = "time" ]
	    then
			part=`grep ElapsedSimulationTime output/*statSCP* | awk -F'= ' '{print $2}'`
			part=`echo "scale=4; (${part}*100)/(${TIME_STOP}-${TIME_START})" | bc `
			echo "Time percentage: " $part
	    else
			error "Unknown SPLIT mode: ${SPLIT}"
	    fi
	fi
	partint=`echo ${part} | awk '{printf "%.0f\n", $1}'`
    ${DIRACPYTHON} testenv.py true ${partint}
    if [ "$part" = "" ] || [ "$part" == "0" ] ; then
                echo "error: finished with 0 particle (or time percentage)!"
                exit 6
	else
                echo "GATE finished with $part particles!"
        fi
else
                echo "error: finished without writting the stats!"
				ls output/
                exit 6
fi

#in case I'm a static job and I received stop, I won't upload my results
if [ ${WRAPPERTYPE} == "stat" ]
then
        STATUS=`cat ../std.in`
        if [ ${STATUS} == stop ]
        then
                echo "I'm a static job and I received stop, I am not allowed to upload my results; exiting in error"
                exit 6
        fi
	if [ "${SPLIT}" = "event" ]
        then
		if [ "$part" != "$EVENTS" ]
		then
			echo "I'm a static job and I simulated $part events instead of $EVENTS; exiting in error"
                	exit 6
		fi
	fi

fi


cp -r output/* output-${NUM}/
tar czvf $OUTPUT ./output-${NUM}/*

exit 0

