#!/usr/bin/env python2
# coding: utf-8

import re
import sys
import os
import subprocess

def shellcommand(command):
    process = subprocess.Popen(command,stdout=subprocess.PIPE,stderr=subprocess.PIPE)
    stdout,stderr = process.communicate()
    assert(process.returncode==0) # shell command returned with no zero code
    return [line for line in stdout.split('\n') if line]

def printwithindent(indent,string):
    print "  "*indent+string

def createnewdata():
    data = {}
    data["actors"] = []
    data["nbprimaries"] = 0
    data["random"] = None
    data["macros"] = []
    data["datafiles"] = []
    data["mainmacro"] = None
    data["alias_def"] = {}
    data["alias_use"] = {}
    return data

def parsemacro(macro,indent=0,data=createnewdata()):
    printwithindent(indent,macro)

    for kk,line in enumerate(open(macro,"r").readlines()):

        match = re.match(r'''^\s*(#*)\s*[\w/]+\s+(.*{\w+}.*)$''',line)
        if match is not None and not match.groups()[0]:
            args = match.groups()[1]
            aliasre = re.compile(r'''({(\w+)})''')
            alias = aliasre.search(args)
            while alias is not None:
                alias_use = alias.groups()[1]
                if alias_use in data["alias_use"]:
                    data["alias_use"][alias_use] += 1
                else:
                    data["alias_use"][alias_use] = 1
                
                args = args.replace(alias.groups()[0],'')
                alias = aliasre.search(args)

        match = re.match(r'''^\s*(#*)\s*/control/execute\s+(.+mac)$''',line)
        if match is not None and not match.groups()[0]:
            submacro = match.groups()[1]
            parsemacro(submacro,indent+1,data)

        match = re.match(r'''^\s*(#*)\s*/control/alias\s+(\w+)\s+(.+)$''',line)
        if match is not None and not match.groups()[0]:
            assert(match.groups()[1] not in data["alias_def"]) # no alias redefinition
            data["alias_def"][match.groups()[1]] = match.groups()[2]

        match = re.match(r'''^\s*(#*)\s*([\w/]+)\s+(.+(txt|hdr|db))$''',line)
        if match is not None and not match.groups()[0]:
            command = match.groups()[1].split('/')
            if command[-1]!="save" and "output" not in command[-1].lower():
                data["datafiles"].append(match.groups()[2])

        match = re.match(r'''^\s*(#*)\s*/gate/actor/addActor\s+(\w+)\s+(\w+)$''',line)
        if match is not None and not match.groups()[0]:
            data["actors"].append(match.groups()[1:])

        match  = re.match(r'''^\s*(#*)\s*/gate/application/setTotalNumberOfPrimaries\s+(\d+)$''',line)
        if match is not None and not match.groups()[0]:
            data["nbprimaries"] = int(match.groups()[1])

        match = re.match(r'''^\s*(#*)\s*/gate/random/setEngineSeed\s+(.*)$''',line)
        if match is not None and not match.groups()[0]:
            data["random"] = match.groups()[1]

    data["macros"].append(macro)
    data["mainmacro"] = macro
    return data

class CheckError(Exception):
    def __init__(self,value):
        self.value = value
    def __str__(self):
        return "formatting problem: %s" % self.value

if __name__=="__main__":
    try:
        macro = sys.argv[1]
        archivename = sys.argv[2]
    except IndexError:
        print "usage:"
        print "%s mac/main.mac package.zip" % os.path.basename(sys.argv[0])
        print "should be executed from the directory having the mac and data subdirectories."
        exit(1)

    print "**** PARSING MACROS ****"
    data = parsemacro(macro)

    print "**** PARSED ****"
    print "mainmacro",data["mainmacro"]
    print "macros",data["macros"]
    print "datas",data["datafiles"]
    print "actors",data["actors"]
    print "alias_def",data["alias_def"]
    print "alias_use",data["alias_use"]

    print "**** CHECKING STUFF ****"
    try:
        if (data["random"]!="auto"): raise CheckError("non automatic seed generation (should be set to 'auto')")
        if (data["nbprimaries"]<=0): raise CheckError("invalid number of primaries (sould be strickly positive)")
        for macro in data["macros"]:
            if (os.path.dirname(macro)!="mac"): raise CheckError("macro %s should be in mac/" % macro)
        for datafile in data["datafiles"]:
            if (os.path.dirname(datafile)!="data"): raise CheckError("data file %s should be in data/" % datafile)
        for actortype,actorname in data["actors"]:
            for reservedactorname in ["statSCP","stop"]:
                if (actorname==reservedactorname): raise CheckError("reserved actor name %s" % reservedactorname)
        for alias in data["alias_use"]:
            if (alias not in data["alias_def"]): raise CheckError("undefined alias %s" % alias)
    except CheckError as err:
        print err
        exit(2)

    print "**** PACKAGING ****"
    command = ["zip","-v",archivename]
    command.extend(data["macros"])
    command.extend(data["datafiles"])
    shellcommand(command)

  
