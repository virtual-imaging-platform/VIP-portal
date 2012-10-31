/*
 * Isomorphic SmartClient
 * Version 8.2 (2011-12-05)
 * Copyright(c) 1998 and beyond Isomorphic Software, Inc. All rights reserved.
 * "SmartClient" is a trademark of Isomorphic Software, Inc.
 *
 * licensing@smartclient.com
 *
 * http://smartclient.com/license
 */
if(typeof isomorphicDir == 'undefined'){isomorphicDir = 'fr.insalyon.creatis.vip.simulatedata.SimulatedData/sc/';}


var isc = window.isc ? window.isc : {};if(window.isc&&!window.isc.module_Core){isc.module_Core=1;isc._moduleStart=isc._Core_start=(isc.timestamp?isc.timestamp():new Date().getTime());if(isc._moduleEnd&&(!isc.Log||(isc.Log && isc.Log.logIsDebugEnabled('loadTime')))){isc._pTM={ message:'Core load/parse time: ' + (isc._moduleStart-isc._moduleEnd) + 'ms', category:'loadTime'};
if(isc.Log && isc.Log.logDebug)isc.Log.logDebug(isc._pTM.message,'loadTime')
else if(isc._preLog)isc._preLog[isc._preLog.length]=isc._pTM
else isc._preLog=[isc._pTM]}isc.definingFramework=true;var isc=window.isc?window.isc:{};isc.$a=new Date().getTime();isc.version="8.2/LGPL Development Only";isc.versionNumber="8.2";isc.buildDate="2011-12-05";isc.expirationDate="${expiration}";isc.licenseType="LGPL";isc.licenseCompany="";isc.licenseSerialNumber="79fbb17a1eaacae68ca9bdda0281a3ef";isc.licensingPage="http://smartclient.com/product/";isc.$b={SCServer:{present:"false",name:"SmartClient Server",serverOnly:true,isPro:true},Drawing:{present:"${includeDrawing}",name:"Drawing Module"},PluginBridges:{present:"${includePluginBridges}",name:"PluginBridges Module"},RichTextEditor:{present:"${includeRichTextEditor}",name:"RichTextEditor Module"},Calendar:{present:"${includeCalendar}",name:"Calendar Module"},Analytics:{present:"false",name:"Analytics Module"},Charts:{present:"false",name:"Charts Module"},Tools:{present:"${includeTools}",name:"Portal and Tools Module"},NetworkPerformance:{present:"false",name:"Network Performance Module"},FileLoader:{present:"false",name:"Network Performance Module"},RealtimeMessaging:{present:"false",name:"RealtimeMessaging Module"},serverCriteria:{present:"${includeServerCriteria}",name:"Server Advanced Filtering",serverOnly:true,isFeature:true},customSQL:{present:"${includeCustomSQL}",name:"SQL Templating",serverOnly:true,isFeature:true},chaining:{present:"${includeChaining}",name:"Transaction Chaining",serverOnly:true,isFeature:true},batchDSGenerator:{present:"${includeBatchDSGenerator}",name:"Batch DS-Generator",serverOnly:true,isFeature:true},batchUploader:{present:"${includeBatchUploader}",name:"Batch Uploader",serverOnly:true,isFeature:true},transactions:{present:"${includeTransactions}",name:"Automatic Transaction Management",serverOnly:true,isFeature:true}};isc.canonicalizeModules=function(_1){if(!_1)return null;if(isc.isA.String(_1)){if(_1.indexOf(",")!=-1){_1=_1.split(",");var _2=/^\s+/,_3=/\s+$/;for(var i=0;i<_1.length;i++){_1[i]=_1[i].replace(_2,"").replace(_3,"")}}else _1=[_1]}
return _1};isc.hasOptionalModules=function(_1){if(!_1)return true;_1=isc.canonicalizeModules(_1);for(var i=0;i<_1.length;i++)if(!isc.hasOptionalModule(_1[i]))return false;return true};isc.getMissingModules=function(_1){var _2=[];_1=isc.canonicalizeModules(_1);for(var i=0;i<_1.length;i++){var _4=_1[i];if(!isc.hasOptionalModule(_4))_2.add(isc.$b[_4])}
return _2};isc.hasOptionalModule=function(_1){var v=isc.$b[_1];if(!v){if(isc.Log)isc.Log.logWarn("isc.hasOptionalModule - unknown module: "+_1);return false}
return v.present=="true"||v.present.charAt(0)=="$"};isc.getOptionalModule=function(_1){return isc.$b[_1]};isc.$d=window.isc_useSimpleNames;if(isc.$d==null)isc.$d=true;if(window.OpenAjax){isc.$e=isc.versionNumber.replace(/[a-zA-Z_]+/,".0");OpenAjax.registerLibrary("SmartClient","http://smartclient.com/SmartClient",isc.$e,{namespacedMode:!isc.$d,iscVersion:isc.version,buildDate:isc.buildDate,licenseType:isc.licenseType,licenseCompany:isc.licenseCompany,licenseSerialNumber:isc.licenseSerialNumber});OpenAjax.registerGlobals("SmartClient",["isc"])}
isc.$f=window.isc_useLongDOMIDs;isc.$g="isc.";isc.addGlobal=function(_1,_2){if(_1.indexOf(isc.$g)==0)_1=_1.substring(4);isc[_1]=_2;if(isc.$d)window[_1]=_2}
isc.onLine=true;isc.isOffline=function(){return!isc.onLine};isc.goOffline=function(){isc.onLine=false};isc.goOnline=function(){isc.onLine=true};if(window.addEventListener){window.addEventListener("online",isc.goOnline,false);window.addEventListener("offline",isc.goOffline,false)}
isc.addGlobal("Browser",{isSupported:false});isc.Browser.isOpera=(navigator.appName=="Opera"||navigator.userAgent.indexOf("Opera")!=-1);isc.Browser.isNS=(navigator.appName=="Netscape"&&!isc.Browser.isOpera);isc.Browser.isIE=(navigator.appName=="Microsoft Internet Explorer"&&!isc.Browser.isOpera);isc.Browser.isMSN=(isc.Browser.isIE&&navigator.userAgent.indexOf("MSN")!=-1);isc.Browser.minorVersion=parseFloat(isc.Browser.isIE?navigator.appVersion.substring(navigator.appVersion.indexOf("MSIE")+5):navigator.appVersion);isc.Browser.version=parseInt(isc.Browser.minorVersion);isc.Browser.isIE6=isc.Browser.isIE&&isc.Browser.version<=6;isc.Browser.isMoz=(navigator.userAgent.indexOf("Gecko")!=-1)&&(navigator.userAgent.indexOf("Safari")==-1)&&(navigator.userAgent.indexOf("AppleWebKit")==-1);isc.Browser.isCamino=(isc.Browser.isMoz&&navigator.userAgent.indexOf("Camino/")!=-1);if(isc.Browser.isCamino){isc.Browser.caminoVersion=navigator.userAgent.substring(navigator.userAgent.indexOf("Camino/")+7)}
isc.Browser.isFirefox=(isc.Browser.isMoz&&navigator.userAgent.indexOf("Firefox/")!=-1);if(isc.Browser.isFirefox){isc.Browser.firefoxVersion=navigator.userAgent.substring(navigator.userAgent.indexOf("Firefox/")+8)}
if(isc.Browser.isMoz){isc.Browser.$h=navigator.userAgent.indexOf("Gecko/")+6;isc.Browser.geckoVersion=parseInt(navigator.userAgent.substring(isc.Browser.$h,isc.Browser.$h+8));if(isc.Browser.isFirefox){if(isc.Browser.firefoxVersion.match(/^1\.0/))isc.Browser.geckoVersion=20050915;else if(isc.Browser.firefoxVersion.match(/^2\.0/))isc.Browser.geckoVersion=20071108}}
isc.Browser.isStrict=document.compatMode=="CSS1Compat";if(isc.Browser.isStrict&&isc.Browser.isMoz){isc.Browser.$i=document.doctype.publicId;isc.Browser.$j=document.doctype.systemId}
isc.Browser.isTransitional=/.*(Transitional|Frameset)/.test((document.all&&document.all[0]&&document.all[0].nodeValue)||(document.doctype&&document.doctype.publicId));isc.Browser.isIE7=isc.Browser.isIE&&isc.Browser.version==7;isc.Browser.isIE8=isc.Browser.isIE&&isc.Browser.version>=8&&document.documentMode==8
isc.Browser.isIE8Strict=isc.Browser.isIE&&isc.Browser.isStrict&&document.documentMode>=8;isc.Browser.isIE9=isc.Browser.isIE&&isc.Browser.version>=9&&document.documentMode>=9;isc.Browser.isAIR=(navigator.userAgent.indexOf("AdobeAIR")!=-1);isc.Browser.AIRVersion=(isc.Browser.isAIR?navigator.userAgent.substring(navigator.userAgent.indexOf("AdobeAir/")+9):null);isc.Browser.isWebKit=navigator.userAgent.indexOf("WebKit")!=-1;isc.Browser.isSafari=isc.Browser.isAIR||navigator.userAgent.indexOf("Safari")!=-1||navigator.userAgent.indexOf("AppleWebKit")!=-1;isc.Browser.isChrome=isc.Browser.isSafari&&(navigator.userAgent.indexOf("Chrome/")!=-1);if(isc.Browser.isSafari){if(isc.Browser.isAIR){isc.Browser.safariVersion=530}else{if(navigator.userAgent.indexOf("Safari/")!=-1){isc.Browser.rawSafariVersion=navigator.userAgent.substring(navigator.userAgent.indexOf("Safari/")+7)}else if(navigator.userAgent.indexOf("AppleWebKit/")!=-1){isc.Browser.rawSafariVersion=navigator.userAgent.substring(navigator.userAgent.indexOf("AppleWebKit/")+12)}else{isc.Browser.rawSafariVersion="530"}
isc.Browser.safariVersion=(function(){var _1=isc.Browser.rawSafariVersion,_2=_1.indexOf(".");if(_2==-1)return parseInt(_1);var _3=_1.substring(0,_2+1),_4;while(_2!=-1){_2+=1;_4=_1.indexOf(".",_2);_3+=_1.substring(_2,(_4==-1?_1.length:_4));_2=_4}
return parseFloat(_3)})()}}
isc.Browser.isWin=navigator.platform.toLowerCase().indexOf("win")>-1;isc.Browser.isWin2k=navigator.userAgent.match(/NT 5.01?/)!=null;isc.Browser.isMac=navigator.platform.toLowerCase().indexOf("mac")>-1;isc.Browser.isUnix=(!isc.Browser.isMac&&!isc.Browser.isWin);isc.Browser.isAndroid=navigator.userAgent.indexOf("Android")>-1;isc.Browser.isMobileWebkit=(isc.Browser.isSafari&&navigator.userAgent.indexOf(" Mobile/")>-1||isc.Browser.isAndroid);isc.Browser.isMobile=(isc.Browser.isMobileWebkit);isc.Browser.isTouch=(isc.Browser.isMobileWebkit);isc.Browser.isIPhone=(isc.Browser.isMobileWebkit&&navigator.userAgent.indexOf("AppleWebKit"));isc.Browser.isHandset=(isc.Browser.isMobileWebkit&&navigator.userAgent.indexOf("iPad")==-1);isc.Browser.isIPad=(isc.Browser.isIPhone&&navigator.userAgent.indexOf("iPad"));isc.Browser.isTablet=(isc.Browser.isIPad);isc.Browser.isBorderBox=(isc.Browser.isIE&&!isc.Browser.isStrict);isc.Browser.lineFeed=(isc.Browser.isWin?"\r\n":"\r");isc.Browser.$k=false;isc.Browser.isDOM=(isc.Browser.isMoz||isc.Browser.isOpera||isc.Browser.isSafari||(isc.Browser.isIE&&isc.Browser.version>=5));isc.Browser.isSupported=((isc.Browser.isIE&&isc.Browser.minorVersion>=5.5&&isc.Browser.isWin)||isc.Browser.isMoz||isc.Browser.isOpera||isc.Browser.isSafari||isc.Browser.isAIR);isc.Browser.allowsXSXHR=((isc.Browser.isFirefox&&isc.Browser.firefoxVersion>="3.5")||(isc.Browser.isChrome)||(isc.Browser.isSafari&&isc.Browser.safariVersion>=531));isc.noOp=function(){};isc.emptyObject={};isc.$ag=[];isc.emptyString=isc.$ah="";isc.dot=".";isc.semi=";";isc.colon=":";isc.slash="/";isc.star="*";isc.auto="auto";isc.px="px";isc.nbsp="&nbsp;";isc.xnbsp="&amp;nbsp;";isc.$ai="false";isc.$aj="FALSE";isc.$ak="_";isc.$al="$";isc.$am="_$observed_";isc.$an="_$SuperProto_";isc.gwtRef="__ref";isc.logWarn=function(_1,_2){isc.Log.logWarn(_1,_2)}
isc.echo=function(_1){return isc.Log.echo(_1)}
isc.echoAll=function(_1){return isc.Log.echoAll(_1)}
isc.echoLeaf=function(_1){return isc.Log.echoLeaf(_1)}
isc.echoFull=function(_1){return isc.Log.echoFull(_1)}
isc.logEcho=function(_1,_2){if(_2)_2+=": ";isc.Log.logWarn((_2||isc.$ah)+isc.echo(_1))}
isc.logEchoAll=function(_1,_2){if(_2)_2+=": ";isc.Log.logWarn((_2||isc.$ah)+isc.echoAll(_1))}
isc.$ao=function(_1,_2){var _3=_2||_1;return _2==null?new Function(_3):new Function(_1,_3)};isc.doEval=function(_1){if(isc.Browser.isMoz)return isc.$ap(_1);if(!isc.$aq)isc.$aq=[];isc.$aq[isc.$aq.length]=_1;return null}
isc.finalEval=function(){if(isc.$aq){if(isc.Browser.isMoz){for(var i=0;i<isc.$aq.length;i++){isc.eval(isc.$aq[i])}}
var _2=isc.$aq.join("");if(isc.Browser.isSafari)_2=isc.$ap(_2);if(isc.Browser.isIE)window.execScript(_2,"javascript");else isc.eval(_2)}
isc.$aq=null}
isc.$ar="//$0";isc.$as="//$1";isc.$at=0;isc.$au=true;isc.$ap=function(_1){isc.$av=true;var _2=isc.timeStamp?isc.timeStamp():new Date().getTime();var _3=isc.$aw,_4=isc.$ax;if(isc.$au)_3=isc.$ay+_3;var _5=_1.split(isc.$az),_6=[];var _5=_1.split(isc.$ar);_1=_5.join(_3);_5=_1.split(isc.$as);_1=_5.join(_4);if(isc.$au){_5=_1.split("//$2");_1=_5.join(isc.$a0)}
var _7=isc.timeStamp?isc.timeStamp():new Date().getTime();isc.$at+=(_7-_2);return _1}
isc.$ax="}catch($a1){isc.eval(isc.$a2(";isc.$a2=function(_1){var _2="var _ = {";if(_1!=""){var _3=_1.split(",");for(var i=0;i<_3.length;i++){var _5=_3[i];_2+=_5+":"+_5;if(i<_3.length-1)_2+=","}}
_2+="};";_2+="if(isc.Log)isc.Log.$a3($a1,arguments,this,_);throw $a1;";return _2}
isc.fillList=function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13,_14,_15,_16,_17,_18,_19,_20,_21,_22,_23,_24,_25,_26,_27){if(_1==null)_1=[];else _1.length=0;var _28;if(_25===_28&&_26===_28&&_27===_28){_1[0]=_2;_1[1]=_3;_1[2]=_4;_1[3]=_5;_1[4]=_6;_1[5]=_7;_1[6]=_8;_1[7]=_9;_1[8]=_10;_1[9]=_11;_1[10]=_12;_1[11]=_13;_1[12]=_14;_1[13]=_15;_1[14]=_16;_1[15]=_17;_1[16]=_18;_1[17]=_19;_1[18]=_20;_1[19]=_21;_1[20]=_22;_1[21]=_23;_1[22]=_24}else{for(var i=1;i<arguments.length;i++){_1[i-1]=arguments[i]}}
return _1}
isc.$a4=[];isc.addGlobal("addProperties",function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13,_14,_15,_16,_17,_18,_19,_20,_21,_22,_23,_24,_25,_26,_27){var _28,_29=isc.$a4;if(_25===_28&&_26===_28&&_27===_28){isc.fillList(_29,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13,_14,_15,_16,_17,_18,_19,_20,_21,_22,_23,_24,_25,_26,_27)}else{_29.length=0;for(var i=1;i<arguments.length;i++){_29[i-1]=arguments[i]}}
var _31=isc.addPropertyList(_1,_29);_29.length=0;return _31});isc.$a5={};isc.$a6={};isc.$a7=function(_1){var _2=_1.Class,_3;if(isc.isA.ClassObject(_1)){_3=isc.$a6[_2]=isc.$a6[_2]||[]}else if(isc.isAn.InstancePrototype(_1)){_3=isc.$a5[_2]=isc.$a5[_2]||[]}
return _3}
isc.addPropertyList=function(_1,_2){if(_1==null){if(isc.Log)isc.Log.logWarn("Attempt to add properties to a null object. "+"Creating a new object for the list of properties.");_1={}}
var _3,_4=(isc.isA!=null),_5=(isc.isAn&&isc.isAn.Instance(_1)?_1.getClass()._stringMethodRegistry:_1._stringMethodRegistry);if(_5==null)_5=isc.emptyObject;var _6=_1.$a8?isc.$a7(_1):null;var _7;for(var i=0,l=_2.length;i<l;i++){var _10=_2[i];if(_10==null)continue;for(var _11 in _10){var _12=_10[_11];var _13=_4&&isc.isA.Function(_12);if(_5[_11]!==_7||_13)
{if(_3==null)_3={};_3[_11]=_12}else{if(_6!=null)_6[_6.length]=_11;var _14=_1[_11];if(!_13&&_14!=null&&isc.isA.Function(_1[_11]))
{if(isc.Log!=null){isc.Log.logWarn("method "+_11+" on "+_1+" overridden with non-function: '"+_12+"'")}}
_1[_11]=_12}}}
if(_3!=null)isc.addMethods(_1,_3);return _1}
isc.$a9="string";isc.$ba="function";isc.$bb="constructor";isc.$bc="object";isc.addGlobal("addMethods",function(_1,_2){if(!_1||!_2)return _1;var _3=_1.$a8?isc.$a7(_1):null;if(!isc.$bd)isc.$bd={};for(var _4 in _2){if(_3!=null)_3[_3.length]=_4;var _5=_2[_4];if(isc.isAn.Instance!=null&&_5!=null&&(typeof _5==isc.$a9||typeof _5==isc.$bc))
{var _6=(isc.isAn.Instance(_1)?_1.getClass()._stringMethodRegistry:_1._stringMethodRegistry);var _7;if(_6&&!(_6[_4]===_7)&&_4!=isc.$bb)
{_5=isc.Func.expressionToFunction(_6[_4],_2[_4])}}
var _8=_1.$be,_9=(_8!=null&&_8[_4]!=null?isc.$am+_4:_4);if(_5!==_1[_9]){if(_5!=null){this.$bf(_5,_4,_1)}
_1[_9]=_5;if(_5!=null){if(isc.$bd[_4]){var _10=(_1.$be!=null&&_1.$be[isc.$bd[_4]]!=null?isc.$am+isc.$bd[_4]:isc.$bd[_4]);_1[_10]=_5}}}}
return _1});isc._allFuncs=[]
isc._allFuncs._maxIndex=0;isc._funcClasses=new Array(5000);isc.$bf=function(_1,_2,_3){if(typeof _1!=isc.$ba)return;if(_3.Class==null)return _1.$bg=_2;if(isc.isA!=null&&isc.isAn.InstancePrototype!=null&&(isc.isAn.InstancePrototype(_3)||isc.isA.ClassObject(_3)))
{var _4=isc._allFuncs;_4[_4._maxIndex]=_1;isc._funcClasses[_4._maxIndex]=_3.Class;_4._maxIndex++;return}
var _5=(_3==isc.isA?"isA":_3.Class);_1.$bh=_5;if(isc[_3.Class]==null)_1.$bg=_2;if(isc.isA!=null&&isc.isAn.Instance!=null&&isc.isAn.Instance(_3)&&!isc.isAn.InstancePrototype(_3))
{_1.$bg=_2;_1.$bi=true;if(_3[_2]!=null)_1.$bj=true}}
isc.addGlobal("getKeys",function(_1){var _2=[];if(_1!=null){for(var _3 in _1){_2[_2.length]=_3}}
return _2});isc.addGlobal("firstKey",function(_1){for(var _2 in _1)return _2});isc.addGlobal("getValues",function(_1){var _2=[];if(_1!=null){for(var _3 in _1){_2[_2.length]=_1[_3]}}
return _2});isc.addGlobal("sortObject",function(_1,_2){if(!isc.isA.Object(_1))return _1;if(isc.isAn.Array(_1)){if(_2!=null)return _1.sort(_2);return _1.sort()}
var _3=isc.getKeys(_1);_3=(_2==null?_3.sort():_3.sort(_2));var _4={};for(var i=0;i<_3.length;i++){_4[_3[i]]=_1[_3[i]]}
return _4});isc.addGlobal("sortObjectByProperties",function(_1,_2){if(!isc.isA.Object(_1))return _1;if(isc.isAn.Array(_1)){if(_2!=null)return _1.sort(_2);return _1.sort()}
var _3=isc.getValues(_1);_3=(_2==null?_3.sort():_3.sort(_2));var _4={};for(var i=0;i<_3.length;i++){var _6=_3[i];for(var _7 in _1){if(_1[_7]===_6){_4[_7]=_1[_7];continue}}}
return _4});isc.addGlobal("addDefaults",function(_1,_2){if(_1==null)return;var _3;for(var _4 in _2){if(_1[_4]===_3)_1[_4]=_2[_4]}
return _1});isc.addGlobal("propertyDefined",function(_1,_2){if(_1==null)return false;var _3;if(_1[_2]!==_3)return true;var _4=isc.getKeys(_1);return(_4.contains(_2))});isc.$bk="__ref";isc.addGlobal("objectsAreEqual",function(_1,_2){if(_1===_2)return true;else if(isc.isAn.Object(_1)&&isc.isAn.Object(_2)){if(isc.isA.Date(_1)){return isc.isA.Date(_2)&&(Date.compareDates(_1,_2)==0)}else if(isc.isAn.Array(_1)){if(isc.isAn.Array(_2)&&_1.length==_2.length){for(var i=0;i<_1.length;i++){if(!isc.objectsAreEqual(_1[i],_2[i]))return false}
return true}
return false}else{if(isc.isAn.Array(_2))return false;var _4=0;for(var _5 in _1){if(_5==isc.$bk)continue;if(!isc.objectsAreEqual(_1[_5],_2[_5]))return false;_4++}
var _6=0;for(var _7 in _2){if(_5==isc.$bk)continue;_6++;if(_6>_4)return false}
if(_6!=_4)return false;return true}}else{return false}});isc.addGlobal("combineObjects",function(_1,_2){if(_1==null||!isc.isAn.Object(_1))return _2;if(_2==null||!isc.isAn.Object(_2))return _1;for(var _3 in _2){var _4=_1[_3],_5=_2[_3];if(isc.isAn.Object(_4)&&!isc.isAn.Array(_4)&&!isc.isA.Date(_4)&&isc.isAn.Object(_5)&&!isc.isAn.Array(_5)&&!isc.isA.Date(_5))
{isc.combineObjects(_4,_5)}else{_1[_3]=_5}}});isc.applyMask=function(_1,_2){var _3={};if(_1==null)return _3;if(_2==null){return isc.addProperties(_3,_1)}
var _4=false;if(!isc.isAn.Array(_1)){_4=true;_1=[_1]}
if(!isc.isAn.Array(_2))_2=isc.getKeys(_2);var _3=[],_5,_6,_7,_8;for(var i=0;i<_1.length;i++){_5=_1[i];_6=_3[i]={};for(var j=0;j<_2.length;j++){_7=_2[j];if(_5[_7]===_8)continue;_6[_7]=_5[_7]}}
return(_4?_3[0]:_3)}
isc.getProperties=function(_1,_2){if(_1==null)return null;var _3={};if(_2==null)return _3;for(var i=0;i<_2.length;i++){var _5=_2[i];_3[_5]=_1[_5]}
return _3}
isc.$bl={};isc.$bm=Math.floor;isc.$bn="-";for(isc.$bo=0;isc.$bo<10;isc.$bo++)
isc.$bl[isc.$bo]=isc.$bo.toString();isc.$bp=function(_1,_2,_3,_4){var _5=_3+_4-1,_6=_2,_7=false,_8;if(_2<0){_8=true;_2=-_2;_1[_3]=this.$bn;_3+=1;_4-=1}
while(_2>9){var _9=this.$bm(_2/ 10),_10=_2-(_9*10);_1[_5]=this.$bl[_10];_2=_9;if(_5==(_3+1)&&_2>9){isc.Log.logWarn("fillNumber: number too large: "+_6+isc.Log.getStackTrace());_7=true;break}
_5-=1}
if(_7){_5=_3+_4-1
_1[_5--]=(!_8?_6:-_6)}else{_1[_5--]=this.$bl[_2]}
for(var i=_5;i>=_3;i--){_1[i]=null}}
isc.booleanValue=function(_1,_2){if(_1==null)return _2;if(isc.isA.String(_1))return _1!=isc.$ai&&_1!=isc.$aj;return _1?true:false}
isc.iscToLocaleString=function(_1){if(_1!=null){return _1.iscToLocaleString?_1.iscToLocaleString():(_1.toLocaleString?_1.toLocaleString():(_1.toString?_1.toString():isc.emptyString+_1))}
return isc.emptyString+_1}
isc.addGlobal("isA",{});isc.addGlobal("isAn",isc.isA);isc.addGlobal("is",isc.isA);isc.isA.Class="isA";isc.isA.isc=isc.isA;Function.$n=1;Array.$n=2;Date.$n=3;String.$n=4;Number.$n=5;Boolean.$n=6;RegExp.$n=7;Object.$n=8;Function.prototype.$n=1;isc.A=isc.isA;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.useTypeOf=isc.Browser.isMoz||isc.Browser.isSafari;isc.A.$bc="object";isc.A.$bq="String";isc.A.$ba="function";isc.A.$br="text/xml";isc.A.$bs={SelectItem:true,Time:true};isc.B.push(isc.A.emptyString=function isc_isA_emptyString(_1){return isc.isA.String(_1)&&_1==isc.emptyString}
,isc.A.nonemptyString=function isc_isA_nonemptyString(_1){return isc.isA.String(_1)&&_1!=isc.emptyString}
,isc.A.Object=function isc_isA_Object(_1){if(_1==null)return false;if(isc.Browser.isIE&&typeof _1==this.$ba)return false;if(this.useTypeOf){var _2=typeof _1;return(_2=="object"||_2=="array"||_2=="date"||(isc.Browser.isMoz&&_2=="function"&&isc.isA.RegularExpression(_1)))}
if(_1.constructor&&_1.constructor.$n!=null){var _3=_1.constructor.$n;if(_3==1){}else{return(_3==8||_3==7||_3==3||_3==2)}}
if(_1.Class!=null&&_1.Class==this.$bq)return false;if(typeof _1==this.$bc){if(isc.Browser.isIE&&isc.isA.Function(_1))return false;else return true}else return false}
,isc.A.emptyObject=function isc_isA_emptyObject(_1){if(!isc.isAn.Object(_1))return false;for(var i in _1){return false}
return true}
,isc.A.emptyArray=function isc_isA_emptyArray(_1){return isc.isAn.Array(_1)&&_1.length==0}
,isc.A.String=function isc_isA_String(_1){if(_1==null)return false;if(this.useTypeOf){return typeof _1=="string"||(_1.Class!=null&&_1.Class==this.$bq)}
if(_1.constructor&&_1.constructor.$n!=null){return _1.constructor.$n==4}
if(_1.Class!=null&&_1.Class==this.$bq)return true;return typeof _1=="string"}
,isc.A.Array=function isc_isA_Array(_1){if(_1==null)return false;if(this.useTypeOf&&typeof _1=="array")return true;if(typeof _1==this.$ba)return false;if(_1.constructor&&_1.constructor.$n!=null){return _1.constructor.$n==2}
if(isc.Browser.isSafari)return""+_1.splice=="(Internal function)";return""+_1.constructor==""+Array}
,isc.A.Function=function isc_isA_Function(_1){if(_1==null)return false;if(isc.Browser.isIE&&typeof _1==this.$ba)return true;var _2=_1.constructor;if(_2&&_2.$n!=null){if(_2.$n!=1)return false;if(_2===Function)return true}
return isc.Browser.isIE?(isc.emptyString+_1.constructor==Function.toString()):(typeof _1==this.$ba)}
,isc.A.Number=function isc_isA_Number(_1){if(_1==null)return false;if(this.useTypeOf&&typeof _1=="number"){return!isNaN(_1)&&_1!=Number.POSITIVE_INFINITY&&_1!=Number.NEGATIVE_INFINITY}
if(_1.constructor&&_1.constructor.$n!=null){if(_1.constructor.$n!=5)return false}else{if(typeof _1!="number")return false}
return!isNaN(_1)&&_1!=Number.POSITIVE_INFINITY&&_1!=Number.NEGATIVE_INFINITY}
,isc.A.SpecialNumber=function isc_isA_SpecialNumber(_1){if(_1==null)return false;if(_1.constructor&&_1.constructor.$n!=null){if(_1.constructor.$n!=5)return false}else{if(typeof _1!="number")return false}
return(isNaN(_1)||_1==Number.POSITIVE_INFINITY||_1==Number.NEGATIVE_INFINITY)}
,isc.A.Boolean=function isc_isA_Boolean(_1){if(_1==null)return false;if(_1.constructor&&_1.constructor.$n!=null){return _1.constructor.$n==6}
return typeof _1=="boolean"}
,isc.A.Date=function isc_isA_Date(_1){if(_1==null)return false;if(_1.constructor&&_1.constructor.$n!=null){return _1.constructor.$n==3}
return(""+_1.constructor)==(""+Date)&&_1.getDate&&isc.isA.Number(_1.getDate())}
,isc.A.RegularExpression=function isc_isA_RegularExpression(_1){if(_1==null)return false;if(_1.constructor&&_1.constructor.$n!=null){return _1.constructor.$n==7}
return(""+_1.constructor)==(""+RegExp)}
,isc.A.XMLNode=function isc_isA_XMLNode(_1){if(_1==null)return false;if(isc.Browser.isIE){return _1.specified!=null&&_1.parsed!=null&&_1.nodeType!=null&&_1.hasChildNodes!=null}
var _2=_1.ownerDocument;if(_2==null)return false;return _2.contentType==this.$br}
,isc.A.AlphaChar=function isc_isA_AlphaChar(_1){var _2=_1.charCodeAt(0)
return((_2>=65&&_2<=90)||(_2>=97&&_2<=122))}
,isc.A.NumChar=function isc_isA_NumChar(_1){var _2=_1.charCodeAt(0)
return(_2>=48&&_2<=57)}
,isc.A.AlphaNumericChar=function isc_isA_AlphaNumericChar(_1){return(isc.isA.AlphaChar(_1)||isc.isA.NumChar(_1))}
,isc.A.WhitespaceChar=function isc_isA_WhitespaceChar(_1){var _2=_1.charCodeAt(0)
return(_2>=0&&_2<=32)}
,isc.A.color=function isc_isA_color(_1){if(!isc.isA.String(_1))return false;if(!this.$bt){this.$bt=new RegExp("^(#([\\dA-F]{2}){3}|"+"rgb\\((\\s*[\\d]{1,3}\\s*,\\s*){2}\\s*[\\d]{1,3}\\s*\\)|"+"[a-z]+)$","i")}
return this.$bt.test(_1)}
,isc.A.ResultSet=function isc_isA_ResultSet(_1){return false}
,isc.A.ResultTree=function isc_isA_ResultTree(_1){return false}
,isc.A.SelectItem=function isc_isA_SelectItem(_1){if(!_1||!isc.isA.FormItem(_1))return false;var _2=_1.getClass();return(_2==isc.SelectItem||_2==isc.NativeSelectItem)}
,isc.A.SelectOtherItem=function isc_isA_SelectOtherItem(_1){if(!_1||!isc.isA.FormItem(_1))return false;var _2=_1.getClass();return((_2==isc.SelectItem||_2==isc.NativeSelectItem)&&_1.isSelectOther)}
,isc.A.Time=function isc_isA_Time(_1){return isc.isA.Date(_1)}
);isc.B._maxIndex=isc.C+24;isc.addGlobal("ClassFactory",{});isc.ClassFactory.Class="ClassFactory";isc.A=isc.isA;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.Instance=function isc_isA_Instance(_1){return(_1!=null&&_1.$bu!=null)}
,isc.A.ClassObject=function isc_isA_ClassObject(_1){return(_1!=null&&_1.$bv==true)}
,isc.A.Interface=function isc_isA_Interface(_1){return(_1!=null&&_1.$a8==true)}
,isc.A.InstancePrototype=function isc_isA_InstancePrototype(_1){return(isc.isAn.Instance(_1)&&_1.$bu==_1)}
);isc.B._maxIndex=isc.C+4;isc.A=isc.ClassFactory;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$g="isc.";isc.A.$bw="Window";isc.A.$bx="Selection";isc.A.$by={};isc.A.$bz="object";isc.A.$b0=["if(object==null||object.isA==null||object.isA==isc.isA)return false;return object.isA(isc.",null,")"];isc.A.$b1={toolbar:true,parent:true,window:true,top:true,opener:true,event:true};isc.A._$isc_OID_="isc_OID_";isc.A._$isc_="isc_";isc.A.$b2="_";isc.A.$b3=[];isc.A.$b4={};isc.A.reuseGlobalIDs=true;isc.A.globalIDClassPoolSize=1000;isc.A.$b5={};isc.A.$b6=0;isc.A._$isc_="isc_";isc.A.$b7=[null,"_",null];isc.A.reuseDOMIDs=false;isc.A.DOMIDPoolSize=10000;isc.A.$b8=[];isc.A.$b9=["0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];isc.A.$ca=[];isc.A.$cb="a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p";isc.B.push(isc.A.defineClass=function isc_ClassFactory_defineClass(_1,_2,_3,_4){return this.$cc(_1,_2,_3,null,_4)}
,isc.A.overwriteClass=function isc_ClassFactory_overwriteClass(_1,_2,_3,_4){return this.$cc(_1,_2,_3,null,_4,true)}
,isc.A.defineInterface=function isc_ClassFactory_defineInterface(_1,_2){return this.$cc(_1,_2,null,true)}
,isc.A.defineRootClass=function isc_ClassFactory_defineRootClass(_1){return this.$cd(_1,null)}
,isc.A.$cc=function isc_ClassFactory__defineNonRootClass(_1,_2,_3,_4,_5,_6){_2=(_2||isc.ClassFactory.defaultSuperClass);if(!_2){isc.Log.logWarn("isc.ClassFactory.defineClass("+_1+") called with null"+" superClass and no ClassFactory.defaultRootClass is defined.");return null}
return this.$cd(_1,_2,_3,_4,_5,_6)}
,isc.A.$cd=function isc_ClassFactory__defineClass(_1,_2,_3,_4,_5,_6){var _7=(isc.Browser.isMoz&&(_1==this.$bw||_1==this.$bx))||(isc.Browser.isChrome&&_1=="DataView");var _8,_9,_10=(isc.$d&&!_5);_8=isc[_1];if(_8!=null)_9=true
else if(_10&&!_7){_8=window[_1]}
if(_8!=null&&_1!="IButton"&&_6!=true)
{var _11="New Class ID: '"+_1+"' collides with ID of existing "+(isc.isA&&isc.isA.Function(isc.isA.Class)&&isc.isA.Class(_8)?"Class object '":"object with value '")+_8+"'.  Existing object will be replaced.";if(!_9)_11+="\nThis conflict would be avoided by disabling "+"ISC Simple Names mode.  See documentation for "+"further information."
if(window.isc.Log)isc.Log.logWarn(_11)}
_2=this.getClass(_2);var _12=(_2?new _2.$ce.$cf():{});var _13=this.$cg(_2);_12.$cf=this.$ch(_12);_13.Class=_1;_13.$bv=true;if(isc.definingFramework==true)_13.isFrameworkClass=true;else _13.isFrameworkClass=false;if(!_13.isFrameworkClass){var _14=_2;while(_14&&!_14.isFrameworkClass){_14=_14.getSuperClass()}
if(_14)_13.$ci=_14.Class}
if(!_13.$ci)_13.$ci=_13.Class;_13.$a8=_12.$a8=!!_4;_13.$cj=_2;_13.$ce=_12;_12.Class=_1;_12.$ck=_13;_12.$bu=_12;_12.isFrameworkClass=_13.isFrameworkClass;_12.$ci=_13.$ci;isc[_1]=_13;if(_10)window[_1]=_13;this.classList[this.classList.length]=_1
if(!(isc.isA.$bs[_1]&&isc.isA[_1])){isc.isA[_1]=this.makeIsAFunc(_1)}
if(_3!=null){if(!isc.isAn.Array(_3))_3=[_3];for(var i=0;i<_3.length;i++){this.mixInInterface(_1,_3[i])}}
return _13}
,isc.A.makeIsAFunc=function isc_ClassFactory_makeIsAFunc(_1){if(this.isFirefox2==null){this.isFirefox2=(isc.Browser.isFirefox&&isc.Browser.geckoVersion>=20061010)}
if(this.isFirefox2){return function(_3){if(_3==null||_3.isA==null||_3.isA==isc.isA)return false;return _3.isA(_1)}}else{var _2=this.$b0;_2[1]=_1;return new Function(this.$bz,_2.join(isc.$ah))}}
,isc.A.$cg=function isc_ClassFactory__makeSubClass(_1){if(!_1)return{};var _2=_1.$cj,_3=_1.$cl;if(!
(_3&&(_2==null||_3!==_2.$cl)))
{_3=_1.$cl=this.$ch(_1)}
return new _3()}
,isc.A.getClass=function isc_ClassFactory_getClass(_1){if(isc.isA.String(_1)){var _2=isc[_1];if(_2&&isc.isA.ClassObject(_2)){return _2}}
if(isc.isA.ClassObject(_1))return _1;if(isc.isAn.Instance(_1))return _1.$ck;return null}
,isc.A.newInstance=function isc_ClassFactory_newInstance(_1,_2,_3,_4,_5,_6){var _7=this.getClass(_1);if(_7==null&&isc.isAn.Object(_1)){var _8;for(var i=0;i<arguments.length;i++){var _10=arguments[i];if(_10!=null&&_10._constructor!=null)
{_8=_10._constructor}}
_6=_5;_5=_4;_4=_3;_3=_2;_2=_1;_1=_8;if(isc.isA.String(_2.constructor)){if(_1==null)_1=_2.constructor;isc.Log.logWarn("ClassFactory.newInstance() passed an object with illegal 'constructor' "+"property - removing this property from the final object. "+"To avoid seeing this message in the future, "+"specify the object's class using '_constructor'.","ClassFactory");_2.constructor=null}
_7=this.getClass(_8)}
if(_7==null){isc.Log.logWarn("newInstance("+_1+"): class not found","ClassFactory");return null}
return _7.newInstance(_2,_3,_4,_5,_6)}
,isc.A.$ch=function isc_ClassFactory__getConstructorFunction(_1){var _2=(isc.Browser.isSafari?function(){}:new Function());_2.prototype=_1;return _2}
,isc.A.addGlobalID=function isc_ClassFactory_addGlobalID(_1,_2,_3){_1.ID=_2||_1.ID;if(_1.ID==null){_1.ID=this.getNextGlobalID(_1);_1.$cm=true}
var _4=this.getWindow();var _5,_6;if(_4[_1.ID]!=null){var _7=isc.isA.Canvas(_4[_1.ID]);if(!_3){isc.Log.logWarn("ClassFactory.addGlobalID: ID:'"+_1.ID+"' for object '"+_1+"' collides with ID of existing object '"+_4[_1.ID]+"'."+(_7?" The pre-existing widget will be destroyed.":" The global reference to this object will be replaced"))}
if(_7)_4[_1.ID].destroy();if(!_7){if(this.$b1[_2])_5=true;else _6=true}}
if(!_5){if(_6){try{_4[_1.ID]=_1}catch(e){_5=true}
if(_4[_1.ID]!=_1){_5=true}}else{_4[_1.ID]=_1}}
if(_5){var _8=this.getNextGlobalID(_1);isc.logWarn("ClassFactory.addGlobalID: ID specified as:"+_1.ID+". This is a reserved word in Javascript or a native property of the"+" browser window object and can not be used as an ID."+" Setting ID to "+_8+" instead.");_1.ID=_8;_1.$cm=true;_4[_1.ID]=_1}
if(isc.globalsSnapshot)isc.globalsSnapshot.add(_1.ID)}
,isc.A.getNextGlobalID=function isc_ClassFactory_getNextGlobalID(_1){var _2=_1!=null&&isc.isA.String(_1.Class)?_1.Class:null;return this.getNextGlobalIDForClass(_2)}
,isc.A.getNextGlobalIDForClass=function isc_ClassFactory_getNextGlobalIDForClass(_1){if(_1){var _2=this.$b5[_1]
if(_2&&_2.length>0){var _3=_2[_2.length-1];_2.length=_2.length-1;return _3}
var _4;if(this.$b4[_1]==null)this.$b4[_1]=0;_4=this.$b4[_1]++;var _5=this.$b3;_5[0]=this._$isc_;_5[1]=_1;_5[2]=this.$b2;isc.$bp(_5,_4,3,5);var _6=_5.join(isc.emptyString);return _6}
return this._$isc_OID_+this.$cn++}
,isc.A.dereferenceGlobalID=function isc_ClassFactory_dereferenceGlobalID(_1){if(window[_1.ID]==_1){window[_1.ID]=null;if(_1.Class!=null&&_1.$cm){this.releaseGlobalID(_1.Class,_1.ID)}}}
,isc.A.releaseGlobalID=function isc_ClassFactory_releaseGlobalID(_1,_2){if(!this.reuseGlobalIDs)return;var _3=this.$b5[_1];if(!_3)this.$b5[_1]=[_2];else if(_3.length<=this.globalIDClassPoolSize)_3[_3.length]=_2}
,isc.A.releaseDOMID=function isc_ClassFactory_releaseDOMID(_1){if(!this.reuseDOMIDs||this.$b8.length>this.DOMIDPoolSize)return;this.$b8[this.$b8.length]=_1}
,isc.A.getDOMID=function isc_ClassFactory_getDOMID(_1,_2){if(!isc.$f||!_1||!_2){var _3=this.$b8.length
if(_3>0){var _1=this.$b8[_3-1];this.$b8.length=_3-1;return _1}
var _4=this.$b6++;return this.$co(_4,this._$isc_)}
this.$b7[0]=_1;this.$b7[2]=_2;return this.$b7.join(isc.emptyString)}
,isc.A.$co=function isc_ClassFactory__convertToBase36(_1,_2){var _3=this.$b9,_4=this.$ca;_4.length=0;if(_2)_4[0]=_2;var _5=3;if(_1>46655){while(Math.pow(36,_5)<=_1)_5+=1}
while(_1>=36){var _6=_1%36;_4[_5-(_2?0:1)]=_3[_6];_5-=1;_1=Math.floor(_1/ 36)}
_4[_5-(_2?0:1)]=_3[_1];return _4.join(isc.emptyString)}
,isc.A.mixInInterface=function isc_ClassFactory_mixInInterface(_1,_2){var _3=this.getClass(_2),_4=this.getClass(_1);if(!_3||!_4)return null;if(!_3.$a8){isc.Log.logWarn("ClassFactory.mixInInterface asked to mixin a class which was not"+" declared as an Interface: "+_2+" onto "+_1);return}
if(!_4.$cp)_4.$cp=[];else _4.$cp=_4.$cp.duplicate();while(_3){this.$cq(_3,_4,true);this.$cq(_3,_4);_4.$cp[_4.$cp.length]=_2;_3=_3.getSuperClass();if(_3&&!_3.$a8)break}}
,isc.A.$cq=function isc_ClassFactory__mixInProperties(_1,_2,_3){var _4;if(_3){_4=isc.$a6[_1.Class]}else{_4=isc.$a5[_1.Class];_1=_1.getPrototype();_2=_2.getPrototype()}
if(_4==null)return;for(var i=0;i<_4.length;i++){var _6=_4[i];if(_2[_6]!=null)continue;var _7=_1[_6];if(isc.isA.String(_7)&&_7==this.TARGET_IMPLEMENTS){var _8=(_3?"Class":"Instance")+" method "+_6+" of Interface "+_1.Class+" must be implemented by "+"class "+_2.Class;_2[_6]=new Function('this.logError("'+_8+'")')}else{_2[_6]=_7}}}
,isc.A.makePassthroughMethods=function isc_ClassFactory_makePassthroughMethods(_1,_2,_3,_4){if(!_2)_2="parentElement";var _5;if(!_3){_5=this.$cr;if(_5==null){_5=this.$cr=["return this.",,".",,"("+this.$cb+")"]}}else{_5=this.$cs;if(_5==null){_5=this.$cs=["if(this.",,"==null){\n",,"return}\nreturn this.",,".",,"("+this.$cb+")"]}}
var _6={};for(var i=0;i<_1.length;i++){var _8=_1[i];if(_3){_5[1]=_2;if(_4!=null){var _9={methodName:_8,propName:_2};var _10=_4.evalDynamicString(this,_9);_5[3]="isc.logWarn(\""+_10+"\");"}
_5[5]=_2;_5[7]=_8}else{_5[1]=_2;_5[3]=_8}
_6[_8]=new Function(this.$cb,_5.join(isc.emptyString))}
return _6}
,isc.A.writePassthroughFunctions=function isc_ClassFactory_writePassthroughFunctions(_1,_2,_3){var _4=this.makePassthroughMethods(_3,_2);_1.addMethods(_4)}
);isc.B._maxIndex=isc.C+23;isc.A=isc.ClassFactory;isc.A.TARGET_IMPLEMENTS="TARGET_IMPLEMENTS";isc.A.$cn=0;isc.A.classList=[];isc.defineClass=function(_1,_2,_3,_4){return isc.ClassFactory.defineClass(_1,_2,_3,_4)}
isc.overwriteClass=function(_1,_2,_3,_4){return isc.ClassFactory.overwriteClass(_1,_2,_3,_4)}
isc.defineInterface=function(_1,_2){return isc.ClassFactory.defineInterface(_1,_2)}
isc.defer=function(_1){var _2=isc.ClassFactory.getClass(isc.ClassFactory.classList.last()),_3=_2.$ct;isc.Log.logWarn("deferred code being placed on class: "+_2);if(!_3)_2.$ct=[_1];else _3.add(_1)}
if(!isc.Browser.isSafari){isc.$cv=window;isc.$cw=window.document}
if(window.isc_enableCrossWindowCallbacks&&isc.Browser.isIE){isc.enableCrossWindowCallbacks=true;Object.$cv=window}
isc.ClassFactory.defineRootClass('Class');isc.ClassFactory.defaultSuperClass=isc.Class;isc.A=isc.Class;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.addClassMethods=function isc_Class_addClassMethods(){for(var i=0;i<arguments.length;i++)
isc.addMethods(this,arguments[i])}
);isc.B._maxIndex=isc.C+1;isc.A=isc.Class;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$cx={};isc.A.dontDup={StringBuffer:true,Action:true,MathFunction:true,JSONEncoder:true};isc.A.$cy={};isc.A.fireOnPauseDelay=200;isc.A.$cz="$c0";isc.A.$c1={};isc.A.$c2={};isc.A.useFastEvalWithVars=isc.Browser.isMoz&&isc.Browser.geckoVersion>=20061010;isc.A.$c3="ID";isc.A.getWindow=(isc.Browser.isSafari?function(){return window}:function(){return this.ns.$cv});isc.A.getDocument=(isc.Browser.isSafari?function(){return window.document}:function(){return this.ns.$cw});isc.B.push(isc.A.create=function isc_c_Class_create(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13){var _14=this.createRaw();_14=_14.completeCreation(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13);return _14}
,isc.A.createRaw=function isc_c_Class_createRaw(){if(!this.initialized())this.init();var _1=new this.$ce.$cf();_1.ns=this.ns;return _1}
,isc.A.init=function isc_c_Class_init(){var _1=this.getSuperClass();if(_1&&!_1.initialized())_1.init();var _2=this.$ct;if(_2){this.$ct=null;_2.map(eval)}
if(this.autoDupMethods){isc.Class.duplicateMethods(this,this.autoDupMethods)}
this.$cx[this.Class]=true}
,isc.A.duplicateMethods=function isc_c_Class_duplicateMethods(_1,_2){if(_1.Class&&this.dontDup[_1.Class])return;for(var i=0;i<_2.length;i++){var _4=_2[i];this.duplicateMethod(_4,_1)}}
,isc.A.duplicateMethod=function(methodName,target){if(!target)target=this;var method=target[methodName];if(method==null)return;if(method.$c4){while(method.$c4)method=method.$c4}
var dup;if(method.toSource==null){dup=eval("dup = "+method.toString())}else{dup=eval(method.toSource())}
if(!method.$c5)isc.Func.getName(method,true);dup.$c5=method.$c5+"[d]";dup.$c4=method;target[methodName]=dup;return dup}
,isc.A.initialized=function isc_c_Class_initialized(){return this.$cx[this.Class]}
,isc.A.getClassName=function isc_c_Class_getClassName(){return this.Class}
,isc.A.getSuperClass=function isc_c_Class_getSuperClass(){return this.$cj}
,isc.A.getPrototype=function isc_c_Class_getPrototype(){return this.$ce}
,isc.A.addMethods=function isc_c_Class_addMethods(){if(this.$a8){this.logWarn("Use addInterfaceMethods() to add methods to interface "+this)}
for(var i=0;i<arguments.length;i++)
isc.addMethods(this.$ce,arguments[i]);return this.$ce}
,isc.A.addInterfaceMethods=function isc_c_Class_addInterfaceMethods(){for(var i=0;i<arguments.length;i++)
isc.addMethods(this.$ce,arguments[i])}
,isc.A.addInterfaceProperties=function isc_c_Class_addInterfaceProperties(){isc.addPropertyList(this.$ce,arguments)}
,isc.A.registerStringMethods=function isc_c_Class_registerStringMethods(_1,_2){var _3=this._stringMethodRegistry;if(!this.isOverridden("_stringMethodRegistry")){var _4={},_5=_4.$c6=(_3.$c6?_3.$c6.duplicate():[]);for(var i=0;i<_5.length;i++){_4[_5[i]]=_3[_5[i]]}
this._stringMethodRegistry=_3=_4}
if(!isc.isA.String(_1)){var _7=_1;if(!isc.isAn.Object(_7)){this.logWarn("registerStringMethods() called with a bad argument: "+_1);return false}
for(var _1 in _7){_3[_1]=_7[_1]
_3.$c6.add(_1)}}else{if(_2==null)_2=null;_3[_1]=_2;_3.$c6.add(_1)}
return true}
,isc.A.registerDupProperties=function isc_c_Class_registerDupProperties(_1,_2){if(this.$c7==null||this.$c7.$bh!=this.getClassName()){if(this.$c7!=null){var _3=this.$c7;this.$c7=this.$c7.duplicate();if(_3.$c8!=null){this.$c7.$c8=isc.shallowClone(_3.$c8)}}else{this.$c7=[]}
this.$c7.$bh=this.getClassName()}
if(!this.$c7.contains(_1)){this.$c7.add(_1)}
if(_2!=null){var _4=this.$c7.$c8||{};_4[_1]=_2;this.$c7.$c8=_4}}
,isc.A.isDupProperty=function isc_c_Class_isDupProperty(_1){return this.$c7!=null&&this.$c7.contains(_1)}
,isc.A.cloneDupPropertyValue=function isc_c_Class_cloneDupPropertyValue(_1,_2){if(isc.isA.Array(_2)){var _3=[];for(var i=0;i<_2.length;i++){_3[i]=this.cloneDupPropertyValue(_1,_2[i])}
return _3}
if(isc.Canvas&&isc.isA.Canvas(_2)){this.logWarn("Default value for property '"+_1+"' is set to a live Canvas (with ID '"+_2.getID()+"') at the Class or AutoChild-defaults level. "+"SmartClient cannot clone a live widget, so each instance of this "+"class may end up pointing to the same live component. "+"To avoid unpredictable behavior and suppress this warning, use the "+"AutoChild subsystem to set up re-usable default properties for sub-components.");return _2}
var _5=isc.shallowClone(_2);var _6=this.$c7;if(_6.$c8!=null&&_6.$c8[_1]!=null&&_5!=null)
{for(var i=0;i<_6.$c8[_1].length;i++){var _7=_6.$c8[_1][i];if(_5[_7]!=null){_5[_7]=isc.shallowClone(_5[_7])}}}
return _5}
,isc.A.evaluate=function isc_c_Class_evaluate(_1,_2,_3){if(!isc.$c9)isc.$c9=0;isc.$c9++;var _4;if(_2){with(_2){if(_3)_4=window.eval(_1)
else _4=eval(_1)}}else{if(_3)_4=window.eval(_1)
else _4=eval(_1)}
if(isc.$c9!=null)isc.$c9--;if(isc.$c9==0)delete isc.$c9;return _4}
,isc.A.addClassProperties=function isc_c_Class_addClassProperties(){isc.addPropertyList(this,arguments);return this}
,isc.A.markAsFrameworkClass=function isc_c_Class_markAsFrameworkClass(){this.isFrameworkClass=true;this.$ce.isFrameworkClass=true;this.$ci=this.Class;this.$ce.$ci=this.Class}
,isc.A.addProperties=function isc_c_Class_addProperties(){if(this.$a8){this.logWarn("Use addInterfaceProperties() to add methods to interface "+this)}
isc.addPropertyList(this.$ce,arguments);return this}
,isc.A.addPropertyList=function isc_c_Class_addPropertyList(_1){isc.addPropertyList(this.$ce,_1);return this}
,isc.A.changeDefaults=function isc_c_Class_changeDefaults(_1,_2){var _3=this.$da(_1),_4=false;var _5=this.getSuperClass();if(_5){var _6=_5.$da(_1);if(_6!=null&&_6==_3){_3=isc.addProperties({},_3);_4=true}}
if(_3==null){_3=_2||{};_4=true}else{isc.addProperties(_3,_2)}
if(_4){var _7={};_7[_1]=_3;this.addProperties(_7)}}
,isc.A.$da=function isc_c_Class__getDefaults(_1){var _2=this.$cy[this.Class],_3=this.getInstanceProperty(_1)||(_2?_2[_1]:null);return _3}
,isc.A.replaceDefaults=function isc_c_Class_replaceDefaults(_1,_2){this.changeDefaults(_1,_2)}
,isc.A.setProperties=function isc_c_Class_setProperties(){var _1;if(arguments.length==1){_1=arguments[0]}else{_1={};for(var i=0;i<arguments.length;i++){isc.addProperties(_1,arguments[i])}}
this.$ce.setProperties(_1)}
,isc.A.isOverridden=function isc_c_Class_isOverridden(_1){return(!(this[_1]===this.$cj[_1]))}
,isc.A.isA=function isc_c_Class_isA(_1){if(_1==null)return false;if(!isc.isA.String(_1)){_1=_1.Class;if(!isc.isA.String(_1))return false}
if(isc.startsWith(_1,isc.ClassFactory.$g)){_1=_1.substring(4)}
var _2=this;while(_2){if(_2.Class==_1)return true;_2=_2.$cj}
if(this.$cp){for(var i=0;i<this.$cp.length;i++){var _4=isc.ClassFactory.getClass(this.$cp[i]);while(_4){if(_4.Class==_1)return true;_4=_4.$cj}}}
return false}
,isc.A.$db=function isc_c_Class__getNextImplementingSuper(_1,_2,_3,_4){var _5;for(;;){if(_2==null){_5=null;break}
var _5=isc.Class.$dc(_3,_2);if(_5==null)break;if(_1!=_5){break}
if(_4){_2=_2.$cj}else{_2=_2.$ck.$cj.$ce}}
if(_5!=null)return _2;return null}
,isc.A.Super=function isc_c_Class_Super(_1,_2,_3){if(isc.$dd)arguments.$de=this;if(this.autoDupMethods&&isc.isAn.Instance(this)){this.duplicateMethod("Super");this.duplicateMethod("invokeSuper")}
if(_2!=null&&(_2.length==null||isc.isA.String(_2)))_2=[_2];if(_2==null)_2=isc.$ag;this.$df=_3||_2;this.$dg=_2;this.$dh=isc.Class.$di(_1,this);this.$dj=true;return this.invokeSuper(null,_1)}
,isc.A.$dc=function isc_c_Class__getOriginalMethod(_1,_2){var _3=_2[_1];while(_3!=null&&_3.$dk){_3=_2[_3.$dk]}
if(_3!=null&&_3.$c4!=null)_3=_3.$c4;return _3}
,isc.A.invokeSuper=function isc_c_Class_invokeSuper(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10){var _11=this.$bv;var _12=this.$dj;this.$dj=null;var _13=this.$df;this.$df=null;var _14=this.$dg;this.$dg=null;var _15;if(_12){_15=this.$dh;this.$dh==null}else{if(_1!=null){_15=_11?_1:_1.$ce}}
var _16,_17;if(_15==null){_16=isc.Class.$dc(_2,this);_17=_11?this:this.getPrototype()}else{_16=isc.Class.$dc(_2,_15);if(_11){_17=_15.$cj}else{_17=_15.$ck.$cj.$ce}
if(_13&&_13.callee!=null&&_13.callee!=_16)
{_16=isc.Class.$dc(_2,this);_17=_11?this:this.getPrototype()}}
_17=isc.Class.$db(_16,_17,_2,_11);if(_17==null){if(isc.Log)isc.Log.logWarn("Call to Super for method: "+_2+" failed on: "+this+": couldn't find a superclass implementation of : "+(_15?_15.Class:this.Class)+"."+_2+this.getStackTrace());return null}
var _18=_17[_2];isc.Class.$dl(_2,_17,this);var _19;if(_12){if(_14!=null||_13!=null){_19=_18.apply(this,_14==null?_13:_14)}else{_19=_18.apply(this)}}else{_19=_18.call(this,_3,_4,_5,_6,_7,_8,_9,_10)}
isc.Class.$dm(_2,this);return _19}
,isc.A.$di=function isc_c_Class__getLastProto(_1,_2){var _3=_2.$dn,_4=_3==null?null:_3[_1];if(isc.isAn.Array(_4))return _4.last();return _4}
,isc.A.$dm=function isc_c_Class__clearLastProto(_1,_2){var _3=_2.$dn,_4=_3[_1];if(_4==null){return}
if(!_4.$do){_3[_1]=null}else{_4.length=Math.max(0,_4.length-1);if(_4.length==0)_3[_1]=null}}
,isc.A.$dl=function isc_c_Class__addProto(_1,_2,_3){var _4=_3.$dn=_3.$dn||{},_5=_4[_1];if(_5==null){_4[_1]=_2}else{if(isc.isAn.Array(_5))_5.add(_2);else{_4[_1]=[_5,_2];_4[_1].$do=true}}}
,isc.A.map=function isc_c_Class_map(_1,_2,_3,_4,_5,_6,_7){if(_1==null)return _2;var _8=[];for(var i=0;i<_2.length;i++){_8.add(this[_1](_2[i],_3,_4,_5,_6,_7))}
return _8}
,isc.A.getInstanceProperty=function isc_c_Class_getInstanceProperty(_1){var _2=this.$ce[_1];return _2}
,isc.A.setInstanceProperty=function isc_c_Class_setInstanceProperty(_1,_2){this.$ce[_1]=_2}
,isc.A.getArgString=function isc_c_Class_getArgString(_1){var _2=this._stringMethodRegistry[_1];var _3;if(_2!==_3)return _2||isc.emptyString;var _4=this.getInstanceProperty(_1);if(_4==null)return"";return isc.Func.getArgString(_4)}
,isc.A.fireCallback=function isc_c_Class_fireCallback(_1,_2,_3,_4,_5){arguments.$de=this;if(_1==null)return;var _6;if(_2==null)_2=_6;var _7=_1;if(isc.isA.String(_1)){if(_4!=null&&isc.isA.Function(_4[_1]))_7=_4[_1];else _7=this.$dp(_1,_2)}else if(isc.isAn.Object(_1)&&!isc.isA.Function(_1)){if(_1.caller!=null)_4=_1.caller;else if(_1.target!=null)_4=_1.target;if(_1.args)_3=_1.args;if(_1.argNames)_2=_1.argNames;if(_1.method)_7=_1.method;else if(_1.methodName&&_4!=null)_7=_4[_1.methodName];else if(_1.action)
_7=this.$dp(_1.action,_2)}
if(!isc.isA.Function(_7)){this.logWarn("fireCallback() unable to convert callback: "+this.echo(_1)+" to a function.  target: "+_4+", argNames: "+_2+", args: "+_3);return}
if(_4==null)_4=window;else if(_4.destroyed){if(this.logIsInfoEnabled("callbacks")){this.logInfo("aborting attempt to fire callback on destroyed target:"+_4+". Callback:"+isc.Log.echo(_1)+",\n stack:"+this.getStackTrace())}
return}
_7.$dq=true;if(_3==null)_3=[];if(isc.enableCrossWindowCallbacks&&isc.Browser.isIE){var _8=_4.constructor?_4.constructor.$cv:_4;if(_8&&_8!=window&&_8.isc){var _9=_8.Array.newInstance();for(var i=0;i<_3.length;i++)_9[i]=_3[i];_3=_9}}
var _11;if(!_5||isc.Log.supportsOnError){_11=_7.apply(_4,_3)}else{try{_11=_7.apply(_4,_3)}catch(e){isc.Log.$a3(e);throw e;}}
return _11}
,isc.A.delayCall=function isc_c_Class_delayCall(_1,_2,_3,_4){if(_4==null)_4=this;if(_3==null)_3=0;return isc.Timer.setTimeout({target:_4,methodName:_1,args:_2},_3)}
,isc.A.$dp=function isc_c_Class__makeCallbackFunction(_1,_2){if(_2==null){var _3;_2=_3}
var _4=isc.$ao(_2,_1);_4.$dr=true;return _4}
,isc.A.fireOnPause=function isc_c_Class_fireOnPause(_1,_2,_3,_4,_5){if(!_1)return;if(!_3)_3=this.fireOnPauseDelay;if(_5==null)_5=this.getClassName();if(!this.$c1[_1]){this.$c1[_1]={}}
this.$c1[_1][_5]={fireTime:_3,callback:_2,target:_4};var _6=isc.timeStamp(),_7=this.$ds?_6-this.$ds:null;this.$ds=_6;if(_7&&this.$dt!=null&&_3>=(this.$dt-_7))return;if(this.$du)isc.Timer.clearTimeout(this.$du);this.$du=this.delayCall(this.$cz,null,_3);this.$dt=_3}
,isc.A.$c0=function isc_c_Class__fireActionsOnPause(){var _1;var _2=isc.timeStamp()-this.$ds,_1;for(var _3 in this.$c1){var _4=this.$c1[_3];for(var _5 in _4){var _6=_4[_5];if(_6.fireTime<=_2){this.fireCallback(_6.callback,null,null,_6.target);delete this.$c1[_3][_5]}else{_6.fireTime-=_2;if(_1==null)_1=_6.fireTime;else _1=Math.min(_1,_6.fireTime)}}
if(isc.isAn.emptyObject(this.$c1[_3]))delete this.$c1[_3]}
if(_1!=null){this.$dt=_1;this.$ds=isc.timeStamp();this.delayCall(this.$cz,null,_1)}else{this.$dt=null;this.$ds=null}}
,isc.A.evalWithVars=function isc_c_Class_evalWithVars(_1,_2,_3){if(!_3)_3=window;if(this.useFastEvalWithVars){return this.evaluate.call(_3,_1,_2)}
var _4="_1";while(_2&&isc.propertyDefined(_2,_4)){_4+="1"}
var _5=[_4];var _6=[_1];if(_2){for(var _7 in _2){_5.push(_7);_6.push(_2[_7])}}
var _8=isc.$ao(_5.join(","),"return eval("+_4+")");return _8.apply(_3,_6)}
,isc.A.evalWithCapture=function isc_c_Class_evalWithCapture(_1,_2,_3){var _4=isc.globalsSnapshot=[];this.evalWithVars(_1,_2,_3);isc.globalsSnapshot=null;return _4}
,isc.A.destroyGlobals=function isc_c_Class_destroyGlobals(_1){if(!isc.isAn.Array(_1))_1=[_1];for(var i=0;i<_1.length;i++){var _3=_1[i];if(window[_3]&&isc.isA.Function(window[_3].destroy))window[_3].destroy();else window[_3]=null}}
,isc.A.globalEvalWithCapture=function isc_c_Class_globalEvalWithCapture(_1,_2,_3,_4){if(_4==null)_4=true;this.$dv=_3;this.$dw=_2;if(isc.Browser.isSafari){_1="isc.Class.$dx();\n"+_1+"\nisc.Class.$dy();";window.setTimeout(_1,0);return}
this.$dx(_3);var _5;try{if(isc.Browser.isIE){window.execScript(_1,"javascript")}else{isc.Class.evaluate(_1,null,true)}}catch(e){if(_4)isc.Log.$a3(e);_5=e}
this.$dy(_5)}
,isc.A.$dx=function isc_c_Class__globalEvalWithCaptureStart(){var _1,_2=this.$dv;this.$dz={};if(_2){for(var _3 in _2){var _4=window[_3];if(_4!==_1)this.$dz[_3]=_4;window[_3]=_2[_3]}}
isc.globalsSnapshot=[]}
,isc.A.$dy=function isc_c_Class__globalEvalWithCaptureEnd(_1){var _2,_3=this.$dv;if(_3){for(var _4 in _3){var _5=this.$dz[_4];if(_5!==_2)window[_4]=this.$dz[_4];else window[_4]=_2}}
var _6=this.$dw;var _7=isc.globalsSnapshot;isc.globalsSnapshot=this.$dw=this.$dv=this.$dz=null;this.fireCallback(_6,["globals","error"],[_7,_1])}
,isc.A.$d0=function isc_c_Class__notifyFunctionComplete(_1,_2,_3){_3.$d1-=1;if(_3.$d1)return;var _4=false;for(var i=0;i<_3.length;i++){if(_3[i].$d2){_4=true;_3.removeItem(i);i--;continue}
if(_3[i].$d3){delete _3[i].$d3;_4=true}}
if(_4){if(_3.length==0){var _6=isc.$am+_2;_1[_2]=_1[_6];delete _1[_6]}else{_1[_2]=_1.$d4(_2,_3)}}}
,isc.A.getArrayItem=function isc_c_Class_getArrayItem(_1,_2,_3){if(_2==null)return null;if(isc.isA.Number(_1))return _2[_1];if(isc.isAn.Object(_1))return _1;if(isc.isA.String(_1))return _2.find(_3||this.$c3,_1);return null}
,isc.A.getArrayItemIndex=function isc_c_Class_getArrayItemIndex(_1,_2,_3){if(isc.isA.Number(_1))return _1;var _4=isc.Class.getArrayItem(_1,_2,_3);return _2.indexOf(_4)}
,isc.A.getDocumentBody=function isc_c_Class_getDocumentBody(_1){var _2=(!_1&&isc.Browser.isIE&&isc.Browser.isStrict);var _3=(_2?this.ns.$d5:this.ns.$d6);if(_3!=null)return _3;var _4=this.getDocument();if(_2){this.ns.$d5=_4.documentElement;return this.ns.$d5}
if(isc.Browser.isIE){_3=_4.body}else{if(_4.body!=null)_3=_4.body;else{var _5=_4.documentElement.namespaceURI;_3=_4.getElementsByTagNameNS(_5,"body")[0];if(_3==null){_3=_4.documentElement.childNodes[1];if(_3!=null&&_3.tagName!="body")_3=null}
if(!_3)return null}}
this.ns.$d6=_3;return _3}
,isc.A.getActiveElement=function isc_c_Class_getActiveElement(){try{return this.getDocument().activeElement}catch(e){this.logWarn("error accessing activeElement: "+e.message)}
return null}
);isc.B._maxIndex=isc.C+54;isc.A=isc.Class;isc.A.newInstance=isc.Class.create;isc.Class.ns=isc;isc.A=isc.ClassFactory;isc.A.ns=isc;isc.A.getWindow=isc.Class.getWindow;isc.A.getDocument=isc.Class.getDocument;isc.A=isc.Class.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.getWindow=(isc.Browser.isSafari?function(){return window}:function(){return this.ns.$cv});isc.A.getDocument=(isc.Browser.isSafari?function(){return window.document}:function(){return this.ns.$cw});isc.A.$d7="_autoMaker";isc.A.$d8="creator";isc.A.$d9="show";isc.A.$ea="Constructor";isc.A.$eb="Defaults";isc.A.$ec="Properties";isc.A.map=isc.Class.map;isc.A.Super=isc.Class.Super;isc.A.invokeSuper=isc.Class.invokeSuper;isc.B.push(isc.A.init=function isc_Class_init(){}
,isc.A.destroy=function isc_Class_destroy(){}
,isc.A.completeCreation=function isc_Class_completeCreation(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13){if(this.addPropertiesOnCreate!=false){if(isc.captureInitData){var _14={className:this.Class,defaults:isc.addProperties({},_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13)}
if(!isc.capturedComponents)isc.capturedComponents=[];isc.capturedComponents.add(_14);if(_14.defaults.ID){isc.ClassFactory.addGlobalID(_14,_14.defaults.ID)}
return _14}
isc.addProperties(this,_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13)}
var _15=this.getClass(),_16=_15.$c7||[];for(var i=0;i<_16.length;i++){var _18=_16[i];if(this[_18]==_15.$ce[_18])
{this[_18]=_15.cloneDupPropertyValue(_18,this[_18])}}
this.init(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13);if(this.autoDupMethods){isc.Class.duplicateMethods(this,this.autoDupMethods)}
return this}
,isc.A.duplicateMethod=function isc_Class_duplicateMethod(_1){isc.Class.duplicateMethod(_1,this)}
,isc.A.getUniqueProperties=function isc_Class_getUniqueProperties(_1){if(_1==null)_1={};var _2=this.getPrototype();for(var _3 in this){if(_3.startsWith("_"))continue;if(_3=="ns")continue;if(_3=="ID"&&this.ID.startsWith("isc_OID_"))continue;var _4=this[_3];if(isc.isA.Function(_4))continue;if(_4!=_2[_3]){_1[_3]=this[_3]}}
return _1}
,isc.A.clone=function isc_Class_clone(){return this.getClass().create(this.getUniqueProperties())}
,isc.A.serialize=function isc_Class_serialize(_1){return isc.Comm.serialize(this,_1)}
,isc.A.xmlSerialize=function isc_Class_xmlSerialize(_1){return isc.Comm.xmlSerialize(this.getClassName(),this,_1)}
,isc.A.getSerializeableFields=function isc_Class_getSerializeableFields(_1,_2){var _3=isc.DS?isc.DS.getNearestSchema(this):null;var _4=this.getUniqueProperties();if(_3==null){this.logDebug("No schema available for class"+this.getClassName());return _4}else{this.logDebug("Constraining serializeable fields for class: "+this.getClassName()+" with schema : "+_3.ID)}
var _5=isc.applyMask(_4,_3.getFields());_1=_1||[];_2=_2||[];_1.map(function(_7){delete _5[_7]});for(var i=0;i<_2.length;i++){_5[_2[i]]=this[_2[i]]}
return _5}
,isc.A.getID=function isc_Class_getID(){return this.ID}
,isc.A.getClass=function isc_Class_getClass(){return this.$ck}
,isc.A.getSuperClass=function isc_Class_getSuperClass(){return this.$ck.$cj}
,isc.A.getClassName=function isc_Class_getClassName(){return this.getClass().getClassName()}
,isc.A.getPrototype=function isc_Class_getPrototype(){return this.$bu}
,isc.A.getGlobalReference=function isc_Class_getGlobalReference(_1){if(typeof _1=="string")return this.evaluate(_1);return _1}
,isc.A.addMethods=function isc_Class_addMethods(){for(var i=0;i<arguments.length;i++){return isc.addMethods(this,arguments[i])}}
,isc.A.addProperties=function isc_Class_addProperties(){return isc.addPropertyList(this,arguments)}
,isc.A.addPropertyList=function isc_Class_addPropertyList(_1){return isc.addPropertyList(this,_1)}
,isc.A.$ed=function isc_Class__getSetter(_1){var _2="set"+_1.substring(0,1).toUpperCase()+_1.substring(1);return(isc.isA.Function(this[_2])?_2:null)}
,isc.A.$ee=function isc_Class__getGetter(_1){var _2="get"+_1.substring(0,1).toUpperCase()+_1.substring(1);return(isc.isA.Function(this[_2])?_2:null)}
,isc.A.setProperty=function isc_Class_setProperty(_1,_2){var _3={};_3[_1]=_2;this.setProperties(_3)}
,isc.A.setProperties=function isc_Class_setProperties(){var _1=isc.isA,_2,_3={};if(arguments.length<1)return;if(arguments.length==1){_2=arguments[0];if(_2==null)return}else{_2={};for(var i=0;i<arguments.length;i++){isc.addProperties(_2,arguments[i])}}
for(var _5 in _2){var _6=_2[_5],_7=this.$ed(_5);if(isc.isA.StringMethod(_6))_6=_6.getValue();if(_7){this[_7](_6);if(this.propertyChanged)this.propertyChanged(_5,_6)}else{_3[_5]=_6}}
this.addProperties(_3)
if(this.propertyChanged){for(var _5 in _3){this.propertyChanged(_5,_3[_5])}}
if(this.doneSettingProperties)this.doneSettingProperties(_2)}
,isc.A.getProperty=function isc_Class_getProperty(_1){var _2=this.$ee(_1);if(_2)return this[_2]();return this[_1]}
,isc.A.$ef=function isc_Class__firstNonNull(_1,_2,_3,_4,_5,_6){return _1!=null?_1:(_2!=null?_2:(_3!=null?_3:(_4!=null?_4:(_5!=null?_5:_6))))}
,isc.A.isA=function isc_Class_isA(_1){return this.getClass().isA(_1)}
,isc.A.observe=function isc_Class_observe(_1,_2,_3){if(_1==null){this.logWarn("Invalid observation: Target is not an object.  target: "+_1+", methodName: "+_2+", action: '"+_3+"'");return false}
if(!isc.Func.convertToMethod(_1,_2)){this.logWarn("Invalid observation: property: '"+_2+"' is not a method on "+_1);return false}
var _4=isc.$bd[_2];if(_1[_4])this.observe(_1,_4,_3)
var _5=_1[_2],_6;if(isc.isAn.Instance(_1)&&_1.getClass().getInstanceProperty(_2)){_6=_1.getClass().getArgString(_2)}else{_6=isc.Func.getArgString(_5)}
var _7=_6.split(",");if(!_1.$be)_1.$be={};if(!_1.$be[_2]){var _8=_1.$be[_2]=[];if(_7.length>0){_8.argStr=_6}}else{var _8=_1.$be[_2];for(var i=0,_10=_8.length;i<_10;i++){if(_8[i].target==this){if(_8[i].$d2){_8[i].$d2=false;_8[i].action=_3;return true}
this.logWarn("Observer: "+this+" is already observing method '"+_2+"' on object '"+_1+"', ignoring");return false}}}
if(_3==null||isc.is.emptyString(_3)){if(!this[_2]||!this.convertToMethod(_2)){this.logWarn("Invalid Observation - no action specified, and observer: "+this+" has no method '"+_2+"', ignoring");return false}
_3="it."+_2+"("+_6+")"}
var _11=_8.$d1;_8.add({target:this,action:_3,$d3:_11});var _12=isc.$am+_2;if(!_1[_12]){_1[_12]=_5}else if(!_1[_2].$eg){this.logWarn("Observation error: method "+_2+" is being observed on object "+_1+" but the function appears to have "+"been directly overridden. This may lead to unexpected behavior - to avoid "+"seeing this message in the future, ensure the addMethods() or addProperties() "+"API is used to modify methods on live SmartClient instances, rather than simply "+"reassigning the method name to a new function instance.");_1[_12]=_1[_2]}
if(!_11)_1[_2]=this.$d4(_2,_8);return true}
,isc.A.$d4=function isc_Class__makeNotifyFunction(_1,_2){var _3=isc.StringBuffer.create();_3.append((isc.$dd?"arguments.$de=this;":""),"var queue=this.$be.",_1,";\r","queue.$d1=queue.$d1?queue.$d1+1:1;\r","var returnVal=this.",isc.$am,_1,"(",(_2.argStr?_2.argStr:""),"),\r","observed=this,observer,it;\r");for(var i=0,_5=_2.length;i<_5;i++){_3.append("if(!queue)return;\r");_3.append("observer=it=queue[",i,"].target;\r");if(isc.isA.String(_2[i].action))_3.append(_2[i].action,";\r");if(isc.isA.Function(_2[i].action)){_3.append("queue[",i,"].action.apply(it, ",(_2.argStr?"'"+_2.argStr+"'":"null"),");\r")}}
if(isc.Browser.isSafari){_3.append("arguments.callee.$eh.Class.$d0(this,'",_1,"',queue);\r")}else{_3.append("isc.Class.$d0(this,'",_1,"',queue);\r")}
_3.append("return returnVal;\r");var _6=isc.$ao(_2.argStr,_3);_6.$eg=true;_6.$c5=_1+"Observation";_6.$dk=isc.$am+_1;if(isc.Browser.isSafari)_6.$eh=isc;return _6}
,isc.A.ignore=function isc_Class_ignore(_1,_2){var _3;var _4=isc.$bd[_2];if(_4!==_3&&_1[_4])this.ignore(_1,_4);var _5=isc.$am+_2;if(!_1[_5]||!_1.$be)return false;var _6=_1.$be[_2],_7=_6.$d1;for(var i=0,_9=_6.length;i<_9;i++){if(_6[i].target==this){if(_7)
_6[i].$d2=true;else
_6.removeAt(i);break}}
if(!_1[_2]||!_1[_2].$eg){this.logWarn("Observation error caught in ignore(): Method "+_2+" was being observed on object "+_1+" but the function appears to have "+"been directly overridden. This may lead to unexpected behavior - to avoid "+"seeing this message in the future, ensure the addMethods() or addProperties() "+"API is used to modify methods on live SmartClient instances, rather than simply "+"reassigning the method name to a new function instance.");_1[_5]=_1[_2]}
if(_6.length==0){_1[_2]=_1[_5];delete _1[_5];delete _1.$be[_2]}else{if(!_7){_1[_2]=this.$d4(_2,_6)}}
return true}
,isc.A.getObserversOf=function isc_Class_getObserversOf(_1){if(!this.$be||!this.$be[_1])return null;var _2=this.$be[_1];for(var _3=[],i=0;i<_2.length;i++){_3[i]=(_2[i]?_2[i].target:null)}
return _3}
,isc.A.isObserving=function isc_Class_isObserving(_1,_2){if(!_1.$be)return false;var _3=_1.$be[_2];if(!_3)return false;for(var i=0;i<_3.length;i++){if(_3[i].target==this)return true}
return false}
,isc.A.convertToMethod=function isc_Class_convertToMethod(_1){return isc.Func.convertToMethod(this,_1)}
,isc.A.evaluate=function isc_Class_evaluate(_1,_2){return isc.Class.evaluate.apply(this,[_1,_2])}
,isc.A.fireCallback=function isc_Class_fireCallback(_1,_2,_3,_4){return this.getClass().fireCallback(_1,_2,_3,this,_4)}
,isc.A.delayCall=function isc_Class_delayCall(_1,_2,_3){return this.getClass().delayCall(_1,_2,_3,this)}
,isc.A.fireOnPause=function isc_Class_fireOnPause(_1,_2,_3){return this.getClass().fireOnPause(_1,_2,_3,this,this.getID())}
,isc.A.evalWithVars=function isc_Class_evalWithVars(_1,_2){return isc.Class.evalWithVars(_1,_2,this)}
,isc.A.getDocumentBody=function isc_Class_getDocumentBody(){return isc.Class.getDocumentBody()}
,isc.A.getActiveElement=function isc_Class_getActiveElement(){return isc.Class.getActiveElement()}
,isc.A.addAutoChildren=function isc_Class_addAutoChildren(_1,_2,_3){if(_1==null)return;if(!isc.isAn.Array(_1))_1=[_1];for(var i=0;i<_1.length;i++){var _5=_1[i];if(isc.isA.Canvas(_5)){_2=_2||this;this.$ei(_5,_2,_3);continue}
this.addAutoChild(_5,null,null,_2,_3)}}
,isc.A.addAutoChild=function isc_Class_addAutoChild(_1,_2,_3,_4,_5){var _6=this[_1];if(isc.isAn.Instance(_6))return _6;if(isc.isAn.Object(_1)&&_1.autoChildName){_2=_1;_3=_2._constructor||_3;_1=_2.autoChildName}
if(isc.isA.String(_6)&&window[_6]){this[_1]=window[_6];return this[_1]}
if(_1!=null&&!this.shouldCreateChild(_1))return;var _7,_8=_1+this.$d7;if(_1!=null&&this[_8])_7=this[_8](_2);else{_7=this.createAutoChild(_1,_2,_3,true)}
if(!_7)return;this[_1]=_7;this.$ej(_1,_7,_4,_5);return _7}
,isc.A.$ej=function isc_Class__addToParent(_1,_2,_3,_4){if(_3==null){_3=_2.autoParent||this.getAutoChildParent(_1)}
if(isc.isA.String(_3)){if(_3==isc.Canvas.NONE){if(this.isDrawn())_2.draw();return}
var _5=this[_3]||window[_3]||_3;if(!isc.isA.Canvas(_5)){this.logWarn("no valid parent could be found for String '"+_3+"'")}else _3=_5}
if(!isc.isA.Canvas(_2)||!isc.isA.Canvas(_3))return;this.$ei(_2,_3,_4)}
,isc.A.$ei=function isc_Class__addAutoChildToParent(_1,_2,_3){if(_1.addAsPeer||_1.snapEdge)_2.addPeer(_1);else if(isc.isA.Layout(_2)&&!_1.addAsChild&&!_1.snapTo)_2.addMember(_1,_3);else if(isc.TileLayout&&isc.isA.TileLayout(_2)&&!_1.addAsChild&&!_1.snapTo)_2.addTile(_1,_3);else _2.addChild(_1)}
,isc.A.shouldCreateChild=function isc_Class_shouldCreateChild(_1){var _2=this.$d9+_1.charAt(0).toUpperCase()+_1.substring(1);if(this[_2]!=null&&this[_2]==false)return false;var _3=this.$ek(_1);if(_3==null)return true;return(this.shouldCreateChild(_3))}
,isc.A.getAutoChildClass=function isc_Class_getAutoChildClass(_1,_2,_3,_4,_5){_4=_4||this.$el(_1);var _6=this[_4];_5=_5||this.$em(_1);var _7=this[_5];return this[_1+this.$ea]||(_2?_2._constructor:null)||(_7?_7._constructor:null)||(_6?_6._constructor:null)||_3||isc.Canvas}
,isc.A.applyBaseDefaults=function isc_Class_applyBaseDefaults(_1,_2,_3){_1.autoDraw=false;_1._generated=true;_1.creator=this;var _4=this.creatorName;if(_4)_1[_4]=this;var _5;if(_3==null||_3.ID===_5){_1.ID=this.getID()+isc.$ak+_2;if(window[_1.ID]){_1.ID=_1.ID+isc.$ak+isc.ClassFactory.getNextGlobalID()}}}
,isc.A.getDynamicDefaults=function isc_Class_getDynamicDefaults(){}
,isc.A.$el=function isc_Class__getDefaultsName(_1){var _2=isc.Class.$en;if(!_2)isc.Class.$en=_2={};if(_2[_1])return _2[_1];var _3=_1+this.$eb;if(this[_3])_2[_1]=_3;return _3}
,isc.A.$em=function isc_Class__getPropertiesName(_1){var _2=isc.Class.$eo;if(!_2)isc.Class.$eo=_2={};if(_2[_1])return _2[_1];var _3=_1+this.$ec;if(this[_3])_2[_1]=_3;return _3}
,isc.A.createAutoChild=function isc_Class_createAutoChild(_1,_2,_3,_4){var _5=this.getDynamicDefaults(_1);if(_5!=null&&_2!=null){_5=isc.addProperties({},_5,_2)}else{_5=_2||_5}
var _6=this.$el(_1),_7=this[_6],_8=this.$em(_1),_9=this[_8],_10=this.getAutoChildClass(_1,_5,_3,_6,_8),_11=isc.ClassFactory.getClass(_10);if(_11==null){this.logWarn("Unable to create autoChild '"+_1+"' of type '"+_10+"' - no such class in runtime.");return null}
_5=this.applyDuplicateAutoChildDefaults(_11,_6,_5);var _12=_11.createRaw();var _13=this.autoPassthroughs,_14,_15;if(_13){for(var _16 in _13){var _17=_13[_16];if(_1==_17&&this[_16]!==_15){_12[_16]=this[_16]}}}
this.applyBaseDefaults(_12,_1,_2);isc.addProperties(_12,this.autoChildDefaults,_7,_14,_5);if(_4)this[_1]=_12;if(_12.autoConfigure)_12.autoConfigure(this,_1);if(this.configureAutoChild)this.configureAutoChild(_12,_1);isc.addProperties(_12,this[_8]);_12.init();if(!this.$ep)this.$ep={};var _18=_12.getID?_12.getID():null;if(_18!=null){if(!isc.isAn.Array(this.$ep[_1])){if(this.$ep[_1]!=null){isc.logWarn(this+".createAutoChild(): Creating auto child named:"+_1+" appears to be replacing autoChild with same name...")}
this.$ep[_1]=[_18]}else{this.$ep[_1].add(_18)}}
return _12}
,isc.A.applyDuplicateAutoChildDefaults=function isc_Class_applyDuplicateAutoChildDefaults(_1,_2,_3){var _4=_1.$c7;if(_4&&_4.length>0){var _5=this[_2];if(_5!=null||this.autoChildDefaults!=null){for(var i=0;i<_4.length;i++){var _7=_4[i],_8;if(_5!=null&&_5[_7]!=null){if(_3==null)_3={};if(_3[_7]===_8){_3[_7]=_1.cloneDupPropertyValue(_7,_5[_7])}}else if(this.autoChildDefaults!=null&&this.autoChildDefaults[_7]!=null)
{if(_3==null)_3={};if(_3[_7]===_8){_3[_7]=_1.cloneDupPropertyValue(_7,this.autoChildDefaults[_7])}}}}}
return _3}
,isc.A.$eq=function isc_Class__completeCreationWithDefaults(_1,_2,_3){this.applyBaseDefaults(_2,_1,_3);var _4=this.$el(_1),_5=this.$em(_1);var _6=_2.getClass();_3=this.applyDuplicateAutoChildDefaults(_6,_4,_3);_2.completeCreation(this.autoChildDefaults,this[_4],_3,this[_5])}
,isc.A.$ek=function isc_Class__getAutoChildParentName(_1){var _2=this.autoChildParentMap;if(_2)return _2[_1]}
,isc.A.getAutoChildParent=function isc_Class_getAutoChildParent(_1){var _2=this.$ek(_1);if(_2)return this[_2];return this}
,isc.A.setAutoChild=function isc_Class_setAutoChild(_1,_2){if(!this.shouldCreateChild(_1)){if(this[_1])this[_1].destroy();delete this[_1]}else{if(isc.isA.Canvas(_2)){var _3=_2;if(this[_1])this[_1].destroy();this[_1]=_3;this.$ej(_1,_3);return}
return this.addAutoChild(_1,_2)}}
);isc.B._maxIndex=isc.C+54;isc.Class.toString=function(){return"[Class "+this.Class+"]"}
isc.Class.getPrototype().toString=function(){return"["+this.Class+" ID:"+this.ID+"]"}
isc.A=isc.Class;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.ns=isc;isc.A._stringMethodRegistry={};isc.B.push(isc.A.NO_OP=function isc_c_Class_NO_OP(){}
);isc.B._maxIndex=isc.C+1;isc.A=isc.ClassFactory;isc.A.observe=isc.Class.getPrototype().observe;isc.A.ignore=isc.Class.getPrototype().ignore;isc.A.$d4=isc.Class.getPrototype().$d4;isc.A=isc.Class;isc.A.$d4=isc.Class.getPrototype().$d4;isc.eval=function(_1){return isc.Class.evaluate(_1)}
Function.prototype.Class="Function";isc.ClassFactory.defineClass("Func");isc.A=isc.Func;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$er=new RegExp("function\\s+([\\w$]+)\\s*\\(");isc.B.push(isc.A.getName=function isc_c_Func_getName(_1,_2){if(_1==Function.prototype.apply)return"Function.apply";if(_1==Function.prototype.call)return"Function.call";if(_1.$c5==null){if(_1.$bh==null&&isc._allFuncs){var _3=isc._allFuncs.indexOf(_1);if(_3!=-1){for(var _4=isc._funcClasses[_3];_4==null;_3--){_4=isc._funcClasses[_3]}
_1.$bh=_4}}
var _5=_1.$bg,_6;if(_5==null&&_1.$bh!=null){var _7;var _8=isc.ClassFactory.getClass(_1.$bh);if(_8==null){_8=isc[_1.$bh]||window[_1.$bh]}else{_7=_8.getPrototype()}
if(_7!=null){for(var _9 in _7){if(_7[_9]===_1){_5=_9;break}}}
if(_5==null&&_8!=null){for(var _9 in _8){if(_8[_9]===_1){_5=_9;_6=true;break}}
if(_5==null&&!isc.isA.Class(_8)&&_8.prototype!=null){for(var _9 in _8.prototype){if(_8.prototype[_9]===_1){_5=_9;break}}}}}
if(_5!=null){_1.$c5=(_1.$bi?(_1.$bj?"[o]":"[a]"):isc.$ah)+(_6?"[c]":isc.$ah)+(_1.$bh?_1.$bh+isc.dot:isc.$ah)+_5}else{if(_1.$dq)_1.$c5="callback";else{var _10=isc.Func.$er.exec(_1.toString());if(_10)_1.$c5=_10[1];else _1.$c5="anonymous"}}}
return _1.$c5}
,isc.A.getArgs=function isc_c_Func_getArgs(_1){var _2=isc.Func.getArgString(_1);if(_2=="")return[];return _2.split(",")}
,isc.A.getArgString=function isc_c_Func_getArgString(_1){var _2=_1.toString(),_3=_2.substring(_2.indexOf("(")+1,_2.indexOf(")"));return _3}
,isc.A.getBody=function isc_c_Func_getBody(_1){var _2=_1.toString();return _2.substring(_2.indexOf("{")+1,_2.lastIndexOf("}"))}
,isc.A.getShortBody=function isc_c_Func_getShortBody(_1){var _2=_1.toString();return _2.substring(_2.indexOf("{")+1,_2.lastIndexOf("}")).replace(/[\r\n\t]*/g,"")}
);isc.B._maxIndex=isc.C+5;if(!Function.prototype.apply){isc.A=Function.prototype;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.apply=function(targetObject,args){if(targetObject==null)targetObject=window;var tempFunctionName="__TEMPF_"+Function.prototype.$es++;var returnValue;targetObject[tempFunctionName]=this;if(!args)args=[];if(args.length<=10){returnValue=targetObject[tempFunctionName](args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9])}else{var functionString='targetObject[tempFunctionName](';for(var i=0;i<args.length;i++){functionString+="args"+'['+i+']';if(i+1<args.length){functionString+=','}}
functionString+=');';isc.eval('returnValue ='+functionString)}
delete targetObject[tempFunctionName];return returnValue}
);isc.B._maxIndex=isc.C+1;Function.prototype.$es=0}
isc.A=isc.Func;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$et=[["//","\n"],["//","\\n"],["/*","*/"]];isc.A.$eu=["\"","\'"];isc.A.$ev=["switch","while","if","return","for","var"];isc.A.$ew=["(",")","[","]","{","}",":","?","!","+","-","/","*","=",">","<","|","&",",","\\"];isc.B.push(isc.A.expressionToFunction=function isc_c_Func_expressionToFunction(_1,_2,_3){var _4=this.$ex(_1,_2,_3);return _4}
,isc.A.$ex=function isc_c_Func__expressionToFunction(_1,_2,_3){if(_2==null){isc.Log.logInfo("makeFunctionExpression() called with empty expression");_2=""}
if(isc.isAn.Object(_2)){if(isc.isA.StringMethod(_2))_2=_2.getValue();else if(_2.Action&&!_2.target)_2=_2.Action;var _4=_1;if(isc.isA.String(_4))_4=_1.split(",");else if(isc.isAn.Array(_4)){_1=_4.join()}
if(!isc.isAn.Array(_4))_4=[];var _5=["if (!window.",,"){var message='Component ID \"",,"\", target of action \"",,"\" does not exist';isc.Log.logWarn(message);if(isc.designTime)isc.say(message)}",,".",,"(",,")"];_5[1]=_5[3]=_5[7]=_2.target;_5[9]=_2.name;if(_2.title)_5[5]=_2.title;else _5[5]="[No title specified]"
var _6=_2.mapping||[];if(!isc.isAn.Array(_6))_6=[];_5[11]=_6.join();var _7=_5.join(isc.emptyString);var _8;try{_8=isc.$ao(_1,_7)}catch(e){this.logWarn("invalid code: "+_7+" generated from action: "+this.echo(_2));_8=new Function()}
_8.iscAction=_2;return _8}
var _9="swirfv";if(isc.isAn.Array(_1)){_1=_1.join()}
var _10=true;var i=0;var _12=this.$et;var _13=this.$eu;var _14=this.$ev;var _15=this.$ew;var _16=false;var _17=isc.$ah,_18=isc.slash,_19="\n",_20="\\",_21="+",_22=isc.semi;var _23=_17;var _24=_17;while(i<_2.length){var _25=_2.charAt(i);if(_25==_18){for(var j=0;j<_12.length;j++){var _27=_12[j],_28=_27[0],_29=_27[1];if(_2.indexOf(_28,i)==i){var k=i+_28.length;while(k<_2.length){if(_2.substring(k,k+_29.length)==_29){k=k+_29.length;break}
k++}
i=k;_23=_17;_24=this.$ey(_2,i)}}}
if(_16){if(_24==_17){break}else{if(isc.isA.WhitespaceChar(_25)){i++;continue}else{_10=false;break}}}
for(var j=0;j<_13.length;j++){var _31=_13[j]
if(_25==_31){var k=i+1;while(k<_2.length){if(_2.charAt(k)==_20)k=k+2;if(_2.charAt(k)==_31){k++;break}
k++}
i=k;_23=_31.charAt(0);_24=this.$ey(_2,i)}}
if(_25==_19){var _32=false;for(var j=0;j<_15.length;j++){if(_23==_15[j]){_32=true;break}}
if(_32||_24==_21){_23=_17}else{_10=false;break}}
if(_25==_22){_16=true}
if(_9.indexOf(_25)!=-1){for(var j=0;j<_14.length;j++){var _33=_14[j],_34=_33.length;if((i+_34<=_2.length)&&(_2.substring(i,i+_34)==_33)&&(i+_34==_2.length||!isc.isA.AlphaNumericChar(_2.charAt(i+_34)))&&(i==0||!isc.isA.AlphaNumericChar(_2.charAt(i-1)))){_10=false;break}}}
if(!isc.isA.WhitespaceChar(_25))_23=_25;i++;_24=this.$ey(_2,i)}
if(_10){_2="return "+_2}
if(_3)_2="//"+_3+"\r\n"+_2;var _8=isc.$ao(_1,_2);return _8}
,isc.A.$ey=function isc_c_Func__getNextNonWhitespaceChar(_1,_2){var _3=isc.$ah;for(var j=(_2+1);j<_1.length;j++){if(!isc.isA.WhitespaceChar(_1.charAt(j))){_3=_1.charAt(j);break}}
if(j>=_1.length)_3=isc.$ah;return _3}
,isc.A.convertToMethod=function isc_c_Func_convertToMethod(_1,_2){if(!isc.isAn.Object(_1)||!isc.isA.nonemptyString(_2)){isc.Log.logWarn("convertToMethod() called with bad parameters.  Cannot convert "+" property '"+_2+"' on object "+_1+" to a function.  Returning false.");return false}
if(_1[_2]&&isc.isA.Function(_1[_2]))return true;var _3=(isc.isAn.Instance(_1)?_1.getClass()._stringMethodRegistry:_1._stringMethodRegistry);if(_3==null)return false;var _4;var _5=_3[_2];if(_5===_4)return false;isc.Func.replaceWithMethod(_1,_2,_5);return true}
,isc.A.replaceWithMethod=function isc_c_Func_replaceWithMethod(_1,_2,_3,_4){if(_1[_2]==null){_1[_2]=isc.is.emptyString(_3)?isc.Class.NO_OP:new Function(_3,isc.$ah)}
var _5=_1[_2];if(isc.isA.Function(_5))return;var _6;if(isc.isA.String(_5)||isc.isA.Object(_5)){_6=isc.Func.expressionToFunction(_3,_5,_4)}else{isc.Log.logWarn("Property '"+_2+"' on object "+_1+" is of type "+typeof _5+".  This can not be converted to a method.","Function");return}
var _7={};_7[_2]=_6;isc.addMethods(_1,_7)}
);isc.B._maxIndex=isc.C+5;Array.prototype.Class="Array";Array.newInstance=function(){var _1=[];isc.addPropertyList(_1,arguments);return _1}
Array.create=Array.newInstance;Array.LOADING="loading";Array.isLoading=function(_1){return _1!=null&&!isc.isAn.XMLNode(_1)&&(_1===Array.LOADING)}
Array.CASE_INSENSITIVE=function(_1,_2,_3){if(isc.isA.String(_1)&&isc.isA.String(_2)&&_1.toLowerCase()==_2.toLowerCase()){return true}else{return _1==_2}}
Array.DATE_VALUES=function(_1,_2,_3){if(isc.isA.Date(_1)&&isc.isA.Date(_2)&&Date.compareLogicalDates(_1,_2)==0){return true}else{return _1==_2}}
Array.DATETIME_VALUES=function(_1,_2,_3){if(isc.isA.Date(_1)&&isc.isA.Date(_2)&&Date.compareDates(_1,_2)==0){return true}else{return _1==_2}}
if(!Array.prototype.localeStringFormatter)
Array.prototype.localeStringFormatter="toString";isc.A=Array.prototype;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.newInstance=Array.newInstance;isc.A.create=Array.newInstance;isc.A.slice=(Array.prototype.slice?Array.prototype.slice:function(_1,_2){if(_2==null)_2=this.length;for(var _3=[],l=this.length;_1<_2&&_1<l;_1++)
_3[_3.length]=this[_1];return _3});isc.A.observe=isc.Class.getPrototype().observe;isc.A.ignore=isc.Class.getPrototype().ignore;isc.A.$d4=isc.Class.getPrototype().$d4;isc.B.push(isc.A.iscToLocaleString=function isc_Arra_iscToLocaleString(){return this[this.localeStringFormatter]()}
,isc.A.getPrototype=function isc_Arra_getPrototype(){return Array.prototype}
,isc.A.get=function isc_Arra_get(_1){return this[_1]}
,isc.A.getLength=function isc_Arra_getLength(){return this.length}
,isc.A.isEmpty=function isc_Arra_isEmpty(){return this.getLength()==0}
,isc.A.first=function isc_Arra_first(){return this[0]}
,isc.A.last=function isc_Arra_last(){return this[this.length-1]}
,isc.A.indexOf=function isc_Arra_indexOf(_1,_2,_3){if(_2==null)_2=0;if(_3==null)_3=this.length-1;for(var i=_2;i<=_3;i++)
if(this[i]==_1)return i;return-1}
,isc.A.lastIndexOf=function isc_Arra_lastIndexOf(_1,_2,_3){if(_2==null)_2=this.length-1;if(_3==null)_3=0;for(var i=_2;i>=_3;i--)
if(this[i]==_1)return i;return-1}
,isc.A.contains=function isc_Arra_contains(_1,_2){return(this.indexOf(_1,_2)!=-1)}
,isc.A.containsAll=function isc_Arra_containsAll(_1){if(_1==null)return true;var _2=_1.getLength();for(var i=0;i<_2;i++){if(!this.contains(_1.get(i)))return false}
return true}
,isc.A.intersect=function isc_Arra_intersect(){var _1=[];for(var i=0;i<this.length;i++){var _3=this.get(i),_4=true;if(_3==null)continue;for(var a=0;a<arguments.length;a++){if(!arguments[a].contains(_3)){_4=false;break}}
if(_4)_1.add(_3)}
return _1}
,isc.A.equals=function isc_Arra_equals(_1){if(_1==null||!isc.isA.List(_1))return false;var _2=_1.getLength();if(_2!=this.getLength())return false;for(var i=0;i<_2;i++){if(_1.get(i)!=this.get(i))return false}
return true}
,isc.A.getItems=function isc_Arra_getItems(_1){var _2=[],_3=_1.getLength();for(var i=0;i<_3;i++){_2[i]=this.get(_1.get(i))}
return _2}
,isc.A.getRange=function isc_Arra_getRange(_1,_2){if(_2==null)_2=this.length-1;return this.slice(_1,_2)}
,isc.A.duplicate=function isc_Arra_duplicate(){return isc.$ag.concat(this)}
,isc.A.set=function isc_Arra_set(_1,_2){this[_1]=_2;this.dataChanged()}
,isc.A.addAt=function isc_Arra_addAt(_1,_2){if(_2==null)_2=0;for(var i=this.length-1;i>=_2;i--){this[i+1]=this[i]}
this[_2]=_1;this.dataChanged();return _1}
,isc.A.removeAt=function isc_Arra_removeAt(_1){var _2=this.length;if(_1>=_2||_1<0)return null;var _3=this[_1];for(;_1<_2-1;_1++)
this[_1]=this[_1+1];this.length--;this.dataChanged();return _3}
,isc.A.add=function isc_Arra_add(_1,_2){var _3;if(_2!==_3){return this.addAt(_1,_2)}
var _4;if(this.sortUnique){_4=this.indexOf(_1);if(_4==-1)_4=this.length}else{_4=this.length}
this[_4]=_1;if(this.sortProps&&this.sortProps.length>0){this.sortByProperties(this.sortProps,this.sortDirections,this.sortNormalizers)}
this.dataChanged();return _1}
,isc.A.addList=function isc_Arra_addList(_1,_2,_3){if(_1==null)return null;this.$ez();if(_2==null)_2=0;if(_3==null)_3=_1.getLength();for(var _4=_2;_4<_3;_4++){this.add(_1.get(_4))}
this.$e0();return _1}
,isc.A.setLength=function isc_Arra_setLength(_1){this.length=_1}
,isc.A.addListAt=function isc_Arra_addListAt(_1,_2){if(_1==null)return null;for(var i=this.length-1,l=_1.length;i>=_2;i--){this[i+l]=this[i]}
for(i=0;i<l;i++){this[i+_2]=_1[i]}
this.dataChanged();return _1}
,isc.A.remove=function isc_Arra_remove(_1){var _2=this.indexOf(_1);if(_2==-1)return false;for(var i=_2;i<this.length;i++)this[i]=this[i+1];this.length=this.length-1;this.dataChanged();return true}
,isc.A.removeList=function isc_Arra_removeList(_1){if(_1==null)return null;for(var _2=[],i=0,l=this.length;i<l;i++){if(!_1.contains(this[i]))_2.add(this[i])}
this.setArray(_2);return _1}
,isc.A.removeEvery=function isc_Arra_removeEvery(_1){this.removeList([_1]);return this}
,isc.A.$ez=function isc_Arra__startChangingData(){var _1;if(this.$e1===_1)this.$e1=0;this.$e1++}
,isc.A.$e0=function isc_Arra__doneChangingData(){if(--this.$e1==0)this.dataChanged()}
,isc.A.dataChanged=function isc_Arra_dataChanged(){if(this.onDataChanged)this.onDataChanged()}
,isc.A.$e2=function isc_Arra__isChangingData(){return(this.$e1!=null&&this.$e1>0)}
,isc.A.setArray=function isc_Arra_setArray(_1){this.setLength(_1.length);for(var i=0;i<_1.length;i++)this[i]=_1[i];this.dataChanged()}
,isc.A.addAsList=function isc_Arra_addAsList(_1){if(!isc.isAn.Array(_1))_1=[_1];return this.addList(_1)}
,isc.A.removeRange=function isc_Arra_removeRange(_1,_2){var _3;if(_1===_3)return this;if(!isc.isA.Number(_1))_1=0;if(!isc.isA.Number(_2))_2=this.length;return this.splice(_1,_2-_1)}
,isc.A.removeWhere=function isc_Arra_removeWhere(_1,_2){for(var i=0,_4=[];i<this.length;i++){if(!this[i]||this[i][_1]!=_2){_4.add(this[i])}}
this.setArray(_4)}
,isc.A.removeUnless=function isc_Arra_removeUnless(_1,_2){for(var i=0,_4=[];i<this.length;i++){if(this[i]&&this[i][_1]==_2){_4.add(this[i])}}
this.setArray(_4)}
,isc.A.removeEmpty=function isc_Arra_removeEmpty(_1,_2){for(var i=0,_4=[];i<this.length;i++){if(this[i]!=null){_4.add(this[i])}}
this.setArray(_4)}
,isc.A.getProperty=function isc_Arra_getProperty(_1){for(var _2=[],i=0,l=this.length;i<l;i++)
_2[_2.length]=(this[i]?this[i][_1]:null);return _2}
,isc.A.getValueMap=function isc_Arra_getValueMap(_1,_2){var _3={};for(var i=0,l=this.getLength();i<l;i++){var _6=this.get(i);if(!isc.isAn.Object(_6))continue;if(_6&&_6[_1]!=null){_3[_6[_1]]=_6[_2]}}
return _3}
,isc.A.map=function isc_Arra_map(_1,_2,_3,_4,_5,_6){var _7=isc.isA.Function(_1),_8=[],_9=this.getLength();var _10,_11=_7&&(_2===_10||isc.isAn.Object(_2))&&_3===_10&&_4===_10&&_5===_10&&_6===_10;for(var i=0;i<_9;i++){var _13=this.get(i);if(_11){if(_2==null)_8[i]=_1(_13,i,this);else{_2.$e3=_1;_8[i]=_2.$e3(_13,i,this);delete _2.$e3}}else if(_7){_8[i]=_1(_13,_2,_3,_4,_5,_6)}else{_8[i]=(_13&&_13[_1]!=null?_13[_1](_2,_3,_4,_5,_6):null)}}
return _8}
,isc.A.setProperty=function isc_Arra_setProperty(_1,_2){for(var i=0,l=this.length;i<l;i++)
if(this[i])this[i][_1]=_2}
,isc.A.clearProperty=function isc_Arra_clearProperty(_1){var _2=false,_3;for(var i=0,l=this.length;i<l;i++){_2=_2||this[i]!==_3;if(this[i])delete this[i][_1]}
return _2}
,isc.A.getProperties=function isc_Arra_getProperties(_1){return isc.applyMask(this,_1)}
,isc.A.getUniqueItems=function isc_Arra_getUniqueItems(){for(var _1=[],i=0,l=this.length;i<l;i++){if(!_1.contains(this[i]))_1[_1.length]=this[i]}
return _1}
,isc.A.findIndex=function isc_Arra_findIndex(_1,_2,_3){return this.findNextIndex(0,_1,_2,null,_3)}
,isc.A.findNextIndex=function isc_Arra_findNextIndex(_1,_2,_3,_4,_5){if(_1==null)_1=0;else if(_1>=this.length)return-1;if(_4==null)_4=this.length-1;if(_2==null)return-1;if(isc.isA.String(_2)){if(_5){for(var i=_1;i<=_4;i++){if(this[i]&&_5(this[i][_2],_3,_2))return i}}else{for(var i=_1;i<=_4;i++){if(this[i]&&this[i][_2]==_3)return i}}
return-1}else if(isc.isA.Function(_2)){for(var i=_1;i<=_4;i++){if(_2(this[i]))return i}
return-1}else{return this.findNextMatch(_2,_1,_4,_5)}}
,isc.A.findNextMatch=function isc_Arra_findNextMatch(_1,_2,_3,_4){var _5=isc.getKeys(_1);if(_4){for(var i=_2;i<=_3;i++){var _7=this.get(i);if(!_7)continue;var _8=true;for(var j=0;j<_5.length;j++){var _10=_5[j];if(!_4(_7[_10],_1[_10],_10)){_8=false;break}}
if(_8)return i}}else{for(var i=_2;i<=_3;i++){var _7=this.get(i);if(!_7)continue;var _8=true;for(var j=0;j<_5.length;j++){var _10=_5[j];if(_7[_10]!=_1[_10]){_8=false;break}}
if(_8)return i}}
return-1}
,isc.A.find=function isc_Arra_find(_1,_2,_3){var _4=this.findIndex(_1,_2,_3);return(_4!=-1)?this.get(_4):null}
,isc.A.findByKeys=function isc_Arra_findByKeys(_1,_2,_3,_4){if(_1==null){isc.Log.logWarn("findByKeys: passed null record");return-1}
var _5={},_6=_2.getPrimaryKeyFields(),_7=false;for(var _8 in _6){_7=true;if(_1[_8]==null){isc.Log.logWarn("findByKeys: passed record does not have a value for key field '"+_8+"'");return-1}
_5[_8]=_1[_8]}
if(!_7){isc.Log.logWarn("findByKeys: dataSource '"+_2.ID+"' does not have primary "+"keys declared, can't find record");return-1}
return this.findNextIndex(_3,_5,null,_4)}
,isc.A.containsProperty=function isc_Arra_containsProperty(_1,_2){var _3=this.findIndex(_1,_2);return(_3!=-1)}
,isc.A.findAll=function isc_Arra_findAll(_1,_2){if(_1==null)return null;if(isc.isA.String(_1)){var _3=null,l=this.length;var _5=isc.isAn.Array(_2);for(var i=0;i<l;i++){var _7=this[i];if(_7&&(_5?_2.contains(_7[_1]):_7[_1]==_2)){if(_3==null)_3=[];_3.add(_7)}}
return _3}else if(isc.isA.Function(_1)){var _3=null,l=this.length,_8=_1,_9=_2;for(var i=0;i<l;i++){var _7=this[i];if(_8(_7,_9)){if(_3==null)_3=[];_3.add(_7)}}
return _3}else{return this.findAllMatches(_1)}}
,isc.A.findAllMatches=function isc_Arra_findAllMatches(_1){var l=this.getLength(),_3=isc.getKeys(_1),_4=null;for(var i=0;i<l;i++){var _6=this.get(i);if(!_6)continue;var _7=true;for(var j=0;j<_3.length;j++){var _9=_3[j];if(_6[_9]!=_1[_9]){_7=false;break}}
if(_7){if(_4==null)_4=[];_4.add(_6)}}
return _4}
,isc.A.slide=function isc_Arra_slide(_1,_2){this.slideRange(_1,_1+1,_2)}
,isc.A.slideRange=function isc_Arra_slideRange(_1,_2,_3){var _4=this.splice(_1,_2-_1);this.addListAt(_4,_3)}
,isc.A.slideList=function isc_Arra_slideList(_1,_2){var _3=[],i;if(_2<0)_2=0;for(i=0;i<_2;i++)
if(!_1.contains(this[i]))
_3.add(this[i]);for(i=0;i<_1.length;i++)
_3.add(_1[i]);for(i=_2;i<this.length;i++)
if(!_1.contains(this[i]))
_3.add(this[i]);this.setArray(_3)}
,isc.A.makeIndex=function isc_Arra_makeIndex(_1,_2,_3){var _4={};var _5=(_2==-1);_2=(_2!=null&&_2!=0);for(var i=0;i<this.length;i++){var _7=this[i],_8=_7[_1];if(_8==null){if(!_3)continue;_8=i}
if(_5){_4[_8]=_7;continue}
var _9=_4[_8];if(_9==null){if(_2){_4[_8]=[_7]}else{_4[_8]=_7}}else{if(_2){_4[_8].add(_7)}else{if(isc.isAn.Array(_9)){_4[_8].add(_7)}else{_4[_8]=[_9,_7]}}}}
return _4}
,isc.A.arraysToObjects=function isc_Arra_arraysToObjects(_1){var _2=_1.length;for(var _3=[],i=0,l=this.length;i<l;i++){var _6=_3[i]={};for(var p=0;p<_2;p++){var _8=_1[p];_6[_8]=this[i][p]}}
return _3}
,isc.A.objectsToArrays=function isc_Arra_objectsToArrays(_1){var _2=_1.length;for(var _3=[],i=0,l=this.length;i<l;i++){var _6=_3[i]=[];for(var p=0;p<_2;p++){var _8=_1[p];_6[p]=this[i][_8]}}
return _3}
,isc.A.spliceArray=function isc_Arra_spliceArray(_1,_2,_3){var _4;if(_1===_4)return this.splice();if(_2===_4)return this.splice(_1);if(_3===_4)return this.splice(_1,_2);if(!isc.isAn.Array(_3)){isc.Log.logWarn("spliceArray() method passed a non-array third parameter. Ignoring...","Array");return this.splice(_1,_2)}
return this.splice.apply(this,[_1,_2].concat(_3))}
,isc.A.peek=function isc_Arra_peek(){var _1=this.pop();this.push(_1);return _1}
,isc.A.removeItem=function isc_Arra_removeItem(_1){return this.removeAt(_1)}
,isc.A.getItem=function isc_Arra_getItem(_1){return this.get(_1)}
,isc.A.setItem=function isc_Arra_setItem(_1){return this.set(_1)}
,isc.A.clearAll=function isc_Arra_clearAll(_1){return this.removeList(this)}
,isc.A.size=function isc_Arra_size(){return this.getLength()}
,isc.A.subList=function isc_Arra_subList(_1,_2){return this.getRange(_1,_2)}
,isc.A.addAll=function isc_Arra_addAll(_1){return this.addList(_1)}
,isc.A.removeAll=function isc_Arra_removeAll(_1){var _2=this.getLength();this.removeList(_1);return this.getLength()!=_2}
,isc.A.clear=function isc_Arra_clear(){this.setLength(0)}
,isc.A.toArray=function isc_Arra_toArray(){return this.duplicate()}
);isc.B._maxIndex=isc.C+69;Number.prototype.Class="Number";isc.A=Number.prototype;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.stringify=function isc_Numbe_stringify(_1,_2){if(!_1)_1=2;var _3=this.toString(),_4=_1-_3.length;if(_2){var _5=_3.indexOf(isc.dot);if(_5!=-1){_4+=(_3.length-_5)}}
var _6=Number.$e4(_4);if(_6==null)return _3;return _6+_3}
,isc.A.toCurrencyString=function isc_Numbe_toCurrencyString(_1,_2,_3,_4){var _5=Math.floor(this),_6=Math.round((this-_5)*100),_7=isc.StringBuffer.create();if(!isc.isA.String(_1))_1="$";if(!isc.isA.nonemptyString(_2))_2=".";if(_3==null)_3=true;if(_4!=true)_7.append(_1);_7.append(_5.stringify(1));if(_3){_7.append(_2);_7.append(_6.stringify(2))}else if(_6!=0){_7.append(_2);if(_6%10==0)_7.append(_6/ 10);else _7.append(_6.stringify(2))}
if(_4==true)_7.append(_1);return _7.toString()}
);isc.B._maxIndex=isc.C+2;isc.A=Number;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A._1zero="0";isc.A._2zero="00";isc.A._3zero="000";isc.A._4zero="0000";isc.B.push(isc.A.setStandardFormatter=function isc_Number_setStandardFormatter(_1){if(isc.isA.Function(Number.prototype[_1]))
Number.prototype.formatter=_1}
,isc.A.setStandardLocaleStringFormatter=function isc_Number_setStandardLocaleStringFormatter(_1){if(isc.isA.Function(Number.prototype[_1]))
Number.prototype.localeStringFormatter=_1}
,isc.A.$e4=function isc_Number__getZeroString(_1){if(_1<=0)return;var _2;while(_1>4){if(_2==null)_2=this._4zero;else _2+=this._4zero;_1-=4}
var _3;switch(_1){case 4:_3=this._4zero;break;case 3:_3=this._3zero;break;case 2:_3=this._2zero;break;case 1:_3=this._1zero;break}
if(_2==null)return _3;return _2+_3}
);isc.B._maxIndex=isc.C+3;if(!Number.prototype.formatter)Number.prototype.formatter="toString";if(!Number.prototype.localeStringFormatter)
Number.prototype.localeStringFormatter="toString";isc.A=Number.prototype;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.localeProperties={decimalSymbol:".",groupingSymbol:",",negativeSymbol:"-",currencySymbol:"$",negativeFormat:1,groupingFormat:1};isc.A.$e5=".";isc.B.push(isc.A.iscToLocaleString=function isc_Numbe_iscToLocaleString(){return this[this.localeStringFormatter]()}
,isc.A.toFormattedString=function isc_Numbe_toFormattedString(_1){return this[(_1?_1:this.formatter)]()}
,isc.A.toLocalizedString=function isc_Numbe_toLocalizedString(_1,_2,_3,_4){var _5=!_1?this:Math.round(this*Math.pow(10,_1))/Math.pow(10,_1);var _6=Math.abs(_5),_7=Math.floor(_6),_8,_9,_10=[];if(_1){var _11=Math.round((_6-_7)*Math.pow(10,_1));_9=_11.stringify(_1)}else if(_1==0){_7=Math.round(_6)}else{if(_6-_7>0){var _12=_6.toString();_9=_12.substring(_12.indexOf(this.$e5)+1)}}
_8=_7.toString();var _13=_8.length;var _14=Math.floor(_13/ 3);if(_13%3){_10[0]=_8.substr(0,_13%3)}
for(var i=0;i<_14;i++){_10[_10.length]=_8.substr(_13%3+i*3,3)}
var _16=_10.join(_3||this.localeProperties.groupingSymbol);if(_9)_16=_16+(_2||this.localeProperties.decimalSymbol)+_9;if(_5<0)_16=(_4||this.localeProperties.negativeSymbol)+_16;return _16}
,isc.A.toUSString=function isc_Numbe_toUSString(_1){return this.toLocalizedString(_1)}
,isc.A.toUSDollarString=function isc_Numbe_toUSDollarString(_1){return this.localeProperties.currencySymbol+this.toLocalizedString(_1)}
);isc.B._maxIndex=isc.C+5;isc.defineClass("Format");isc.A=isc.Format;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.toUSString=function isc_c_Format_toUSString(_1,_2){if(!isc.isA.Number(_1))return _1;return _1.toUSString(_2)}
,isc.A.toUSDollarString=function isc_c_Format_toUSDollarString(_1,_2){if(!isc.isA.Number(_1))return _1;return _1.toUSDollarString(_2)}
,isc.A.toCurrencyString=function isc_c_Format_toCurrencyString(_1,_2,_3,_4,_5){if(!isc.isA.Number(_1))return _1;return _1.toCurrencyString(_2,_3,_4,_5)}
);isc.B._maxIndex=isc.C+3;isc.Math={random:function(_1,_2){if(_2==null){return Math.round(Math.random()*_1)}else{return Math.round(Math.random()*(_2-_1))+_1}}}
isc.defineClass("DateUtil");isc.addGlobal("timeStamp",function(){return new Date().getTime()});isc.addGlobal("timestamp",isc.timeStamp);Date.prototype.Class="Date";Date.Class="Date";isc.Date=Date;isc.A=Date;isc.A.INVALID_DATE_STRING="Invalid date format";isc.A=Date;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$e6={toUSShortDate:"MDY",toUSShortDateTime:"MDY",toUSShortDatetime:"MDY",toEuropeanShortDate:"DMY",toEuropeanShortDateTime:"DMY",toEuropeanShortDatetime:"DMY",toJapanShortDate:"YMD",toJapanShortDateTime:"YMD",toJapanShortDatetime:"YMD"};isc.B.push(isc.A.newInstance=function isc_Date_newInstance(_1,_2,_3,_4,_5,_6,_7){return new Date(_1,_2,_3,_4,_5,_6,_7)}
,isc.A.create=function isc_Date_create(_1,_2,_3,_4,_5,_6,_7){var _8;if(_1===_8)return new Date();if(_2===_8)return new Date(_1);if(_3===_8)_3=0;if(_4===_8)_4=0;if(_5===_8)_5=0;if(_6===_8)_6=0;if(_7===_8)_7=0;return new Date(_1,_2,_3,_4,_5,_6,_7)}
,isc.A.createLogicalDate=function isc_Date_createLogicalDate(_1,_2,_3,_4){var d=new Date();d.setHours(12);d.setMinutes(0);d.setSeconds(0);d.setMilliseconds(0);if(_3!=null)d.setDate(1);if(_1!=null)d.setYear(_1);if(_2!=null)d.setMonth(_2);if(_3!=null)d.setDate(_3);if(_4){var _6=(d.getFullYear()==_1&&d.getMonth()==_2&&d.getDate()==_3);if(!_6)return null}
d.logicalDate=true;return d}
,isc.A.createLogicalTime=function isc_Date_createLogicalTime(_1,_2,_3,_4){return isc.Time.createLogicalTime(_1,_2,_3,_4)}
,isc.A.createDatetime=function isc_Date_createDatetime(_1,_2,_3,_4,_5,_6,_7,_8){var _9=_4!=null,_10=_5!=null,_11=_6!=null;if(isc.isA.String(_4))_4=parseInt(_4||12,10);if(isc.isA.String(_5))_5=parseInt(_5||0,10);if(isc.isA.String(_6))_6=parseInt(_6||0,10);var _12;if(!isc.Time.$e7){_12=new Date(_1,_2,_3);if(_9){if(_4!=null)_12.setHours(_4);if(_5!=null)_12.setMinutes(_5);if(_6!=null)_12.setSeconds(_6);if(_7!=null)_12.setMilliseconds(_7)}
if(!_8)return _12;var _13=(_12.getFullYear()==_1&&_12.getMonth()==_2&&_12.getDate()==_3&&(!_9||_12.getHours()==_4)&&(!_10||_12.getMinutes()==_5)&&(!_11||_12.getSeconds()==_6));return(_13?_12:null)}else{if(_4==null)_4=0;if(_5==null)_5=0;if(_6==null)_6=0;if(_7==null)_7=0;_12=new Date(Date.UTC(_1,_2,_3,_4,_5,_6,_7));if(_8){var _13=(_12.getUTCFullYear()==_1&&_12.getUTCMonth()==_2&&_12.getUTCDate()==_3&&(!_9||_12.getUTCHours()==_4)&&(!_10||_12.getUTCMinutes()==_5)&&(!_11||_12.getUTCSeconds()==_6));if(!_13)_12=null}
if(_12!=null){_12.$e8(-isc.Time.getUTCHoursDisplayOffset(_12),-isc.Time.getUTCMinutesDisplayOffset(_12))}
return _12}}
,isc.A.compareDates=function isc_Date_compareDates(_1,_2){if(_1==_2)return 0;var _3=(isc.isA.Date(_1)?_1.getTime():0),_4=(isc.isA.Date(_2)?_2.getTime():0);return _3>_4?-1:(_4>_3?1:0)}
,isc.A.compareLogicalDates=function isc_Date_compareLogicalDates(_1,_2){if(_1==_2)return 0;if(!isc.isA.Date(_1)||!isc.isA.Date(_2))return false;var _3=_1.getFullYear(),_4=_1.getMonth(),_5=_1.getDate(),_6=_2.getFullYear(),_7=_2.getMonth(),_8=_2.getDate();var _9=_3*10000+_4*100+_5,_10=_6*10000+_7*100+_8;return _9>_10?-1:(_10>_9?1:0)}
,isc.A.setInputFormat=function isc_Date_setInputFormat(_1){this.$e9=_1}
,isc.A.getInputFormat=function isc_Date_getInputFormat(){if(this.$e9!=null)return this.$e9;return this.mapDisplayFormatToInputFormat("toShortDate")}
,isc.A.mapDisplayFormatToInputFormat=function isc_Date_mapDisplayFormatToInputFormat(_1){if(_1==null||_1=="toShortDate"){_1=Date.prototype.$fa}else if(_1=="toNormalDate"){_1=Date.prototype.formatter}
if(isc.isA.Function(_1)){isc.Log.logInfo("Unable to determine input format associated with display format "+"function - returning default input format","Date");return this.$e9||"MDY"}
var _2=this.$e6[_1];if(_2!=null&&isc.isA.String(_2))return _2;if(_1=="toSerializeableDate")return this.parseSchemaDate;isc.Log.logInfo("Unable to determine input format associated with display format "+_1+" - returning default input format","Date");return this.$e9||"MDY"}
,isc.A.parseInput=function isc_Date_parseInput(_1,_2,_3,_4,_5){var _6=(_5==false);if(isc.isA.Date(_1))return _1;if(!isc.isA.String(_1)||isc.isAn.emptyString(_1)){return null}
if(_2==null)_2=this.getInputFormat();if(isc.isA.Function(Date[_2]))_2=Date[_2];if(isc.isA.Function(_2)){return _2(_1,_3,_4)}
var _7=this.$fb(_1,_2);if(_7!=null){var _8=_7[0];if(_8&&_8.length<=2){_8=parseInt(_8,10);if(_8<_3)_8+=2000;else _8+=1900
_7[0]=_8}
if(_6){return Date.createLogicalDate(_7[0],_7[1],_7[2],_4)}else{return Date.createDatetime(_7[0],_7[1],_7[2],_7[3],_7[4],_7[5],null,_4)}}else{return null}}
,isc.A.parseSchemaDate=function isc_Date_parseSchemaDate(_1){if(isc.isA.Date(_1))return _1;if(!isc.isA.String(_1))_1=(_1.toString?_1.toString():_1+"");var _2=_1.match(/(\d{4})[\/-](\d{2})[\/-](\d{2})([T ](\d{2}):(\d{2}):(\d{2}))?(\.(\d+))?([+-]\d{2}:\d{2}|Z)?/);if(_2==null)return null;var _3;if(!_2[4]){_3=Date.createLogicalDate(_2[1],_2[2]-1,_2[3])}else if(!_2[9]){_3=new Date(Date.UTC(_2[1],_2[2]-1,_2[3],_2[5],_2[6],_2[7]))}else{var _4=_2[9];if(_4.length!=3){var _5=Math.pow(10,3-_4.length);_4=Math.round(parseInt(_4,10)*_5)}
_3=new Date(Date.UTC(_2[1],_2[2]-1,_2[3],_2[5],_2[6],_2[7],_4))}
if(_2[10]&&_2[10].toLowerCase()!="z"){var _6=_2[10].split(":"),H=_6[0],_8=H&&H.startsWith("-"),M=_6[1];H=parseInt(H,10);M=parseInt(M,10);var _10=_3.getTime();if(isc.isA.Number(H))_10-=(3600000*H);if(isc.isA.Number(M))_10-=(60000*M*(_8?-1:1));_3.setTime(_10)}
return _3}
,isc.A.parseDate=function isc_Date_parseDate(_1,_2,_3,_4){return this.parseInput(_1,_2,_3,_4)}
,isc.A.parseDateTime=function isc_Date_parseDateTime(_1,_2,_3,_4){return this.parseDatetime(_1,_2,_3,_4)}
,isc.A.parseDatetime=function isc_Date_parseDatetime(_1,_2,_3,_4){return this.parseInput(_1,_2,_3,_4)}
,isc.A.parseServerDate=function isc_Date_parseServerDate(_1,_2,_3){return Date.createLogicalDate(_1,_2,_3)}
,isc.A.parseServerTime=function isc_Date_parseServerTime(_1,_2,_3){return Date.createLogicalTime(_1,_2,_3)}
,isc.A.$fb=function isc_Date__splitDateString(_1,_2){var _3,_4,_5,_6,_7,_8;var _9=_2?_2.indexOf("M"):0,_10=_2?_2.indexOf("D"):1,_11=_2?_2.indexOf("Y"):2;if(isc.Browser.isSafari&&isc.Browser.safariVersion<=312){var _12=this.$fc(_1,_9,_10,_11);_5=_12[0];_3=_12[1];_4=_12[2];_6=_12[3];_7=_12[4];_8=_12[5]}else{var _13=new RegExp(/^\s*(\d{4}|\d{1,2})[^\d](\d{4}|\d{1,2})[^\d](\d{4}|\d{1,2})([^\d](\d{1,2})[^\d](\d\d)[^\d]?(\d\d)?)?\s*$/),_14=_1.match(_13);if(_14==null)return null;_3=_14[_9+1]-1;_4=_14[_10+1];_5=_14[_11+1];_6=_14[5]||0;_7=_14[6]||0;_8=_14[7]||0}
if(isc.isA.Number(_5-_3-_4-_6-_7-_8))
return([_5,_3,_4,_6,_7,_8]);else return null}
,isc.A.setNormalDisplayFormat=function isc_Date_setNormalDisplayFormat(_1){if(isc.isA.Function(Date.prototype[_1])||isc.isA.Function(_1)){Date.prototype.formatter=_1}}
,isc.A.setShortDisplayFormat=function isc_Date_setShortDisplayFormat(_1){if(isc.isA.Function(Date.prototype[_1])||isc.isA.Function(_1)){Date.prototype.$fa=_1}}
,isc.A.setDefaultDateSeparator=function isc_Date_setDefaultDateSeparator(_1){Date.prototype.$fd=[,,,,_1,,,,,_1,,,,null];Date.prototype.$fe=_1}
,isc.A.getDefaultDateSeparator=function isc_Date_getDefaultDateSeparator(_1){if(Date.prototype.$fe)return Date.prototype.$fe;else return"/"}
,isc.A.setShortDatetimeDisplayFormat=function isc_Date_setShortDatetimeDisplayFormat(_1){if(isc.isA.Function(Date.prototype[_1])||isc.isA.Function(_1)){Date.prototype.$ff=_1}}
,isc.A.setFormatter=function isc_Date_setFormatter(_1){Date.setNormalDisplayFormat(_1)}
,isc.A.setLocaleStringFormatter=function isc_Date_setLocaleStringFormatter(_1){if(isc.isA.Function(Date.prototype[_1])||isc.isA.Function(_1))
Date.prototype.localeStringFormatter=_1}
,isc.A.getShortMonthNames=function isc_Date_getShortMonthNames(_1){_1=_1||3;var _2=Date.shortMonthNames;if(_2==null)_2=Date.$fg;if(_2==null){var _3=Date.$fg=[];for(var i=0;i<12;i++){var _5=Date.createLogicalDate(2000,i,2);_3[i]=_5.deriveShortMonthName()}
_2=Date.$fg}
var _6=[];for(var i=0;i<12;i++){_6[i]=_2[i].substring(0,_1)}
return _6}
,isc.A.getShortDayNames=function isc_Date_getShortDayNames(_1){_1=_1||3;var _2=Date.shortDayNames;if(_2==null)_2=Date.$fh;if(_2==null){Date.$fh=[];var _3=new Date();_3.setDate(1);if(_3.getDay()>0)_3.setDate(_3.getDate()+(7-_3.getDay()));var _4=_3.getDate();for(var i=0;i<7;i++){_3.setDate(_4+i);Date.$fh[i]=_3.deriveShortDayName()}
_2=Date.$fh}
var _6=[];for(var i=0;i<7;i++){_6[i]=_2[i].substring(0,_1)}
return _6}
,isc.A.getWeekendDays=function isc_Date_getWeekendDays(){var _1=Date.weekendDays;if(_1==null)_1=Date.$fi;if(_1==null){_1=Date.$fi=[0,6]}
return _1}
,isc.A.getFormattedDateRangeString=function isc_Date_getFormattedDateRangeString(_1,_2){if(_1!=null&&!isc.isA.Date(_1)){_1=null}
if(_2!=null&&!isc.isA.Date(_2)){_2=null}
var _3=_1?_1.getMonth():null,_4=_1?_1.getShortMonthName():null,_5=_1?_1.getFullYear():null,_6=_1?_1.getDate():null,_7=_2?_2.getMonth():null,_8=_2?_2.getShortMonthName():null,_9=_2?_2.getFullYear():null,_10=_2?_2.getDate():null,_11="";if(_1&&_2){if(_5==_9){if(_3==_7){if(_6==_10){_11=_4+" "+_1.getDate()+", "+_5}else{_11=_4+" "+_1.getDate()+" - "+_2.getDate()+", "+_5}}else{_11=_4+" "+_1.getDate()+" - "+_8+" "+_2.getDate()+", "+_5}}else{_11=_4+" "+_1.getDate()+", "+_5+" - "+_8+" "+_2.getDate()+", "+_9}}else if(_1){_11=_4+" "+_1.getDate()+", "+_5}else if(_2){_11=_8+" "+_2.getDate()+", "+_9}
return _11}
);isc.B._maxIndex=isc.C+29;isc.A=Date.prototype;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$fd=[,,,,"/",,,,,"/",,,,null];isc.A.$fj="MDY";isc.A.$fk="DMY";isc.A.$fl="YMD";isc.A.$fj="MDY";isc.A.$fm="0";isc.A.$fn=[null,null];isc.B.push(isc.A.duplicate=function isc_Dat_duplicate(){var _1=new Date();_1.setTime(this.getTime());_1.logicalDate=this.logicalDate;_1.logicalTime=this.logicalTime;return _1}
,isc.A.clearTimeFields=function isc_Dat_clearTimeFields(){this.setHours(0);this.setMinutes(0);this.setSeconds(0);this.setMilliseconds(0);return this}
,isc.A.deriveShortDayName=function isc_Dat_deriveShortDayName(_1){var _2=this.toString();if(_1==null||_1<=0||_1>3)_1=3;return _2.substring(0,_1)}
,isc.A.getShortDayName=function isc_Dat_getShortDayName(){return Date.getShortDayNames()[this.getDay()]}
,isc.A.deriveShortMonthName=function isc_Dat_deriveShortMonthName(_1){var _2=this.toUTCString();var _3=8;if(_1==null||_1<0||_1>3)_1=3;if(_2.substring(6,7)==' '){_3=7}
return _2.substring(_3,(_3+_1))}
,isc.A.getShortMonthName=function isc_Dat_getShortMonthName(){return Date.getShortMonthNames()[this.getMonth()]}
,isc.A.getShortYear=function isc_Dat_getShortYear(){var _1=this.getFullYear();return(_1%100).stringify(2)}
,isc.A.getWeek=function isc_Dat_getWeek(){var _1=new Date(this.getFullYear(),0,1);return Math.ceil((((this-_1)/86400000)+_1.getDay())/7)}
,isc.A.toDateStamp=function isc_Dat_toDateStamp(){return this.getUTCFullYear()+(this.getUTCMonth()+1).stringify()+this.getUTCDate().stringify()+"T"+this.getUTCHours().stringify()+this.getUTCMinutes().stringify()+this.getUTCSeconds().stringify()+"Z"}
,isc.A.toNormalDate=function isc_Dat_toNormalDate(_1,_2){if(!_1)_1=this.formatter;if(isc.isA.Function(_1)){return _1.apply(this,[_2])}else if(this[_1]){return this[_1](_2)}}
,isc.A.toShortDate=function isc_Dat_toShortDate(_1,_2){if(!_1)_1=this.$fa;if(isc.isA.Function(_1))return _1.apply(this,[_2]);else if(isc.isA.Function(this[_1]))return this[_1](_2);isc.logWarn("Date.toShortDate() specified formatter not understood:"+_1);return this.toUSShortDate()}
,isc.A.toShortDateTime=function isc_Dat_toShortDateTime(_1,_2){return this.toShortDatetime(_1,_2)}
,isc.A.toShortDatetime=function isc_Dat_toShortDatetime(_1,_2){if(!_1)_1=this.$ff;return this.toShortDate(_1,_2)}
,isc.A.setDefaultDateSeparator=function isc_Dat_setDefaultDateSeparator(_1){this.$fd=[,,,,_1,,,,,_1,,,,null];this.$fe=_1}
,isc.A.getDefaultDateSeperator=function isc_Dat_getDefaultDateSeperator(_1){if(this.$fe)return this.$fe;else return"/"}
,isc.A.$e8=function isc_Dat__applyTimezoneOffset(_1,_2,_3){if(_3==null)_3=this.getTime();if(isc.isA.Number(_1))_3+=(3600000*_1);if(isc.isA.Number(_2))_3+=(60000*_2);this.setTime(_3)}
,isc.A.$fo=function isc_Dat__getTimezoneOffsetDate(_1,_2){var _3=Date.$fp;if(_3==null)_3=Date.$fp=new Date();_3.$e8(_1,_2,this.getTime());return _3}
,isc.A.$fq=function isc_Dat__toShortDate(_1,_2){if(_2==null){_2=!this.logicalDate}
var _3=this.$fd,_4,_5,_6;if(!_2||!isc.Time.$e7){_4=this.getMonth()+1;_5=this.getDate();_6=this.getFullYear()}else{var _7=this.$fo(isc.Time.getUTCHoursDisplayOffset(this),isc.Time.getUTCMinutesDisplayOffset(this));_4=_7.getUTCMonth()+1;_5=_7.getUTCDate();_6=_7.getUTCFullYear()}
var _8,_9,_10;if(_1==this.$fj){_8=0;_9=5;_10=10}else if(_1==this.$fk){_9=0;_8=5;_10=10}else if(_1==this.$fl){_10=0;_8=5;_9=10}else{_9=_1.indexOf("D")*5;_10=_1.indexOf("Y")*5;_8=_1.indexOf("M")*5}
_3[_9]=_5<10?this.$fm:null
isc.$bp(_3,_5,_9+1,3);_3[_8]=_4<10?this.$fm:null
isc.$bp(_3,_4,_8+1,3);isc.$bp(_3,_6,_10,4);return _3.join(isc.emptyString)}
,isc.A.toUSShortDate=function isc_Dat_toUSShortDate(_1){return this.$fq(this.$fj,_1)}
,isc.A.$fr=function isc_Dat__toShortTime(_1){return isc.Time.toShortTime(this,"toShortPadded24HourTime")}
,isc.A.toUSShortDateTime=function isc_Dat_toUSShortDateTime(_1){return this.toUSShortDatetime(_1)}
,isc.A.toUSShortDatetime=function isc_Dat_toUSShortDatetime(_1){return this.toUSShortDate(_1)+" "+this.$fr(_1)}
,isc.A.toEuropeanShortDate=function isc_Dat_toEuropeanShortDate(_1){return this.$fq(this.$fk,_1)}
,isc.A.toEuropeanShortDateTime=function isc_Dat_toEuropeanShortDateTime(_1){return this.toEuropeanShortDatetime()}
,isc.A.toEuropeanShortDatetime=function isc_Dat_toEuropeanShortDatetime(_1){return this.toEuropeanShortDate(_1)+" "+this.$fr(_1)}
,isc.A.toJapanShortDate=function isc_Dat_toJapanShortDate(_1){return this.$fq(this.$fl,_1)}
,isc.A.toJapanShortDateTime=function isc_Dat_toJapanShortDateTime(_1){return this.toJapanShortDatetime(_1)}
,isc.A.toJapanShortDatetime=function isc_Dat_toJapanShortDatetime(_1){return this.toJapanShortDate(_1)+" "+this.$fr(_1)}
,isc.A.$fs=function isc_Dat__serialize(){if(isc.Comm.$ft){return isc.SB.concat('"'+this.toDBDate(),'"')}else{return isc.SB.concat("new Date(",this.getTime(),")")}}
,isc.A.$fu=function isc_Dat__xmlSerialize(_1,_2,_3,_4){return isc.Comm.$fv(_1,this.toSchemaDate(),_2||(this.logicalDate?"date":(this.logicalTime&&!isc.DataSource.serializeTimeAsDatetime?"time":"datetime")),_3,_4)}
,isc.A.toSchemaDate=function isc_Dat_toSchemaDate(_1){if((_1=="date")||this.logicalDate){return isc.SB.concat(this.getFullYear().stringify(4),"-",(this.getMonth()+1).stringify(2),"-",this.getDate().stringify(2))};if((!isc.DataSource||!isc.DataSource.serializeTimeAsDatetime)&&(_1=="time"||this.logicalTime))
{return isc.SB.concat(this.getHours().stringify(2),":",this.getMinutes().stringify(2),":",this.getSeconds().stringify(2))}
return isc.SB.concat(this.getUTCFullYear().stringify(4),"-",(this.getUTCMonth()+1).stringify(2),"-",this.getUTCDate().stringify(2),"T",this.getUTCHours().stringify(2),":",this.getUTCMinutes().stringify(2),":",this.getUTCSeconds().stringify(2))}
,isc.A.toSerializeableDate=function isc_Dat_toSerializeableDate(_1){var _2=isc.SB.create();_2.append(this.getFullYear().stringify(4),"-",(this.getMonth()+1).stringify(2),"-",this.getDate().stringify(2));if(!_1)_2.append((isc.Comm.xmlSchemaMode?"T":" "),this.getHours().stringify(2),":",this.getMinutes().stringify(2),":",this.getSeconds().stringify(2));return _2.toString()}
,isc.A.toDBDate=function isc_Dat_toDBDate(){return isc.StringBuffer.concat("$$DATE$$:",this.toSerializeableDate())}
,isc.A.toDBDateTime=function isc_Dat_toDBDateTime(){return this.toDBDate()}
,isc.A.setFormatter=function isc_Dat_setFormatter(_1){this.setNormalDisplayFormat(_1)}
,isc.A.setLocaleStringFormatter=function isc_Dat_setLocaleStringFormatter(_1){if(isc.isA.Function(this[_1])||isc.isA.Function(_1))
this.localeStringFormatter=_1}
,isc.A.isBeforeToday=function isc_Dat_isBeforeToday(_1){var _2=new Date(this.getFullYear(),this.getMonth(),this.getDate(),0).getTime();if(_1.getTime()<_2)return true;else return false}
,isc.A.isToday=function isc_Dat_isToday(_1){if(this.getFullYear()==_1.getFullYear()&&this.getMonth()==_1.getMonth()&&this.getDate()==_1.getDate())
return true;else return false}
,isc.A.isTomorrow=function isc_Dat_isTomorrow(_1){var _2=new Date(this.getFullYear(),this.getMonth(),this.getDate()+1,0);var _3=new Date(this.getFullYear(),this.getMonth(),this.getDate()+1,23);var _4=_1.getTime();if(_4>=_2.getTime()&&_4<=_3.getTime()){return true}else{return false}}
,isc.A.isThisWeek=function isc_Dat_isThisWeek(_1){var _2=new Date(this.getFullYear(),this.getMonth(),this.getDate()-this.getDay(),0);var _3=new Date(this.getFullYear(),this.getMonth(),this.getDate()+(7-this.getDay()),23);var _4=_1.getTime();if(_4>=_2.getTime()&&_4<=_3.getTime()){return true}else{return false}}
,isc.A.isNextWeek=function isc_Dat_isNextWeek(_1){var _2=new Date(this.getFullYear(),this.getMonth(),(this.getDate()-this.getDay())+7,0);var _3=new Date(this.getFullYear(),this.getMonth(),(this.getDate()-this.getDay())+14,23);var _4=_1.getTime();if(_4>=_2.getTime()&&_4<=_3.getTime()){return true}else{return false}}
,isc.A.isNextMonth=function isc_Dat_isNextMonth(_1){var _2=new Date(this.getFullYear(),this.getMonth());_2.setMonth(_2.getMonth()+1);if(_2.getFullYear()==_1.getFullYear()&&_2.getMonth()==_1.getMonth()){return true}else{return false}}
);isc.B._maxIndex=isc.C+42;Date.prototype.toBrowserString=Date.prototype.toString;Date.prototype.toBrowserLocaleString=Date.prototype.toLocaleString;if(!Date.prototype.formatter)Date.prototype.formatter="toLocaleString"
if(!Date.prototype.$fa)Date.setShortDisplayFormat("toUSShortDate");if(!Date.prototype.$ff)Date.setShortDatetimeDisplayFormat("toUSShortDatetime");Date.prototype.iscToLocaleString=function(){var _1=this.localeStringFormatter;if(isc.isA.Function(_1))return _1.apply(this);else if(this[_1])return this[_1]()}
if(!Date.prototype.localeStringFormatter)
Date.prototype.localeStringFormatter="toLocaleString";isc.A=Date;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.$fc=function isc_Date__splitDateViaSubstring(_1,_2,_3,_4){var _5=_4*3,_6=_1.substring(_5,_5+4);var _7=(parseInt(_6)!=_6);if(_7)_6=_6.substring(0,2);var _8=0,_9=0;if(_2>_3)_8+=3;else _9+=3;if(_2>_4)_8+=(_7?3:5);if(_3>_4)_9+=(_7?3:5);var _10=_1.substring(_8,_8+2)-1;var _11=_1.substring(_9,_9+2);var _12=_7?9:11,_13=(_1.substring(_12,_12+2)||0),_14=(_1.substring(_12+3,_12+5)||0),_15=(_1.substring(_12+6,_12+8)||0);return[_6,_10,_11,_13,_14,_15]}
);isc.B._maxIndex=isc.C+1;isc.A=Date.prototype;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.toPrettyString=function isc_Dat_toPrettyString(){return this.toUSShortDatetime()}
);isc.B._maxIndex=isc.C+1;isc.A=Date;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.parseStandardDate=function isc_Date_parseStandardDate(_1){if(!isc.isA.String(_1))return null;var _2=_1.substring(0,4),_3=_1.substring(5,7)-1,_4=_1.substring(8,10),_5=_1.substring(11,13),_6=_1.substring(14,16),_7=_1.substring(17,19);if(_1.length<19){if(!isc.isA.Number(_2-_3-_4))return null}else{if(!isc.isA.Number(_2-_3-_4-_5-_6-_7))return null}
return new Date(_2,_3,_4,_5,_6,_7)}
,isc.A.parseSerializeableDate=function isc_Date_parseSerializeableDate(_1){return this.parseStandardDate(_1)}
,isc.A.parseDBDate=function isc_Date_parseDBDate(_1){if(isc.isA.String(_1)&&_1.startsWith("$$DATE$$:")){_1=_1.substring(9)
return this.parseStandardDate(_1)}
return null}
,isc.A.parseDateStamp=function isc_Date_parseDateStamp(_1){if(_1==null||isc.isA.Date(_1))return _1;var _2=new Date(Date.UTC(_1.substring(0,4),parseInt(_1.substring(4,6),10)-1,_1.substring(6,8),_1.substring(9,11),_1.substring(11,13),_1.substring(13,15)));if(isc.isA.Date(_2))return _2;else return null}
,isc.A.parseShortDate=function isc_Date_parseShortDate(_1,_2){return this.parseInput(_1,"MDY",_2)}
,isc.A.parseShortDateTime=function isc_Date_parseShortDateTime(_1,_2){return this.parseShortDate(_1,_2)}
,isc.A.parsePrettyString=function isc_Date_parsePrettyString(_1,_2){return this.parseShortDate(_1,_2)}
,isc.A.parseEuropeanShortDate=function isc_Date_parseEuropeanShortDate(_1,_2){return this.parseInput(_1,"DMY",_2)}
,isc.A.parseEuropeanShortDateTime=function isc_Date_parseEuropeanShortDateTime(_1,_2){return this.parseInput(_1,"DMY",_2)}
,isc.A.setToZeroTime=function isc_Date_setToZeroTime(_1){if(_1==null||!isc.isA.Date(_1))return _1;var _2=_1.logicalDate;_1.logicalDate=false;var _3=_1.getTime();var _4=isc.Time.getUTCHoursDisplayOffset(_1),_5=isc.Time.getUTCMinutesDisplayOffset(_1),_6=_4>0?24-_4:0-_4,_7=_5>0?60-_5:0-_5;var _8;if(_2){_8=_1.getDate()}else{var _9=_1.$fo(_4,_5);_8=_9.getUTCDate()}
_1.setUTCHours(_6);var _10=_1.$fo(_4,_5),_11=_10.getUTCDate(),_12=_6;if(_11!=_8){var _13=_1.getTime()<_3;_12+=_13?24:-24;_1.setUTCHours(_12)}
if(_1.getUTCHours()!=_6){_1.setTime(_3);_1.setUTCHours(_12+1);if(_1.getUTCHours()!=_6+1){_1.setTime(_3);_1.setUTCHours(_12+2)}}
_1.setUTCMinutes(_7)}
);isc.B._maxIndex=isc.C+10;isc.A=isc.DateUtil;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$fw={s:true,S:true,mn:true,MN:true,h:true,H:true,d:true,D:true};isc.B.push(isc.A.mapRelativeDateShortcut=function isc_c_DateUtil_mapRelativeDateShortcut(_1,_2){switch(_1){case"$now":return"+0MS";case"$today":if(_2=="end"){return"+0D"}else{return"-0D"}
case"$startOfToday":return"-0D";case"$endOfToday":return"+0D";case"$yesterday":if(_2=="end"){return"-1d[+0D]"}else{return"-1D"}
case"$startOfYesterday":return"-1D";case"$endOfYesterday":return"-1d[+0D]";case"$tomorrow":if(_2=="end"){return"+1D"}else{return"+1d[-0D]"}
case"$startOfTomorrow":return"+1d[-0D]";case"$endOfTomorrow":return"+1D";case"$startOfWeek":return"-0W";case"$endOfWeek":return"+0W";case"$startOfMonth":return"-0M";case"$endOfMonth":return"+0M";case"$startOfYear":return"-0Y";case"$endOfYear":return"+0Y";case"$weekFromNow":if(_2=="end"){return"+1w[+0D]"}else{return"+1w[-0D]"}
case"$weekAgo":if(_2=="end"){return"-1w[+0D]"}else{return"-1w[-0D]"}
case"$monthFromNow":if(_2=="end"){return"+1m[+0D]"}else{return"+1m[-0D]"}
case"$monthAgo":if(_2=="end"){return"-1m[+0D]"}else{return"-1m[-0D]"}}
return _1}
,isc.A.getAbsoluteDate=function isc_c_DateUtil_getAbsoluteDate(_1,_2,_3,_4){if(this.isRelativeDate(_1)){if(!_3)_3=_1.rangePosition;_1=_1.value}
if(_1.startsWith("$")){_1=this.mapRelativeDateShortcut(_1,_3)}
var _5=_1,_6=_4?Date.createLogicalDate():new Date();if(_2!=null)_6.setTime(_2.getTime());var _7=this.getRelativeDateParts(_5);if(_7.qualifier){_7.qualifier=_7.qualifier.toUpperCase();var _8=this.getRelativeDateParts(_7.qualifier);var _9=["S","MN","H","D","W","M","Q","Y"];if(_9.contains(_8.period)){_6=this.dateAdd(_6,_8.period,_8.countValue,(_8.direction=="+"?1:-1),_4)}else{isc.logWarn("Invalid date-offset qualifier provided: "+_8.period+".  Valid "+"options are: S, MN, H, D, W, M, Q and Y.")}}
var _10=this.dateAdd(_6,_7.period,_7.countValue,(_7.direction=="+"?1:-1),_4);return _10}
,isc.A.isRelativeDate=function isc_c_DateUtil_isRelativeDate(_1){if(isc.isA.Date(_1))return false;if(isc.isAn.Object(_1)&&_1._constructor=="RelativeDate")return true;return false}
,isc.A.getRelativeDateParts=function isc_c_DateUtil_getRelativeDateParts(_1){var _2=_1,_3=_2.substring(0,1),_4=_2.indexOf("["),_5=(_4>0?_2.substring(_4):null),_6=(_5!=null?_2.substring(1,_4):_2.substring(1)),_7=parseInt(_6),_8=_6.replace(_7,"");return{direction:(_3=="+"||_3=="-"?_3:"+"),qualifier:_5?_5.replace("[","").replace("]","").replace(",",""):null,countValue:isc.isA.Number(_7)?_7:0,period:_8?_8:_3}}
,isc.A.dateAdd=function isc_c_DateUtil_dateAdd(_1,_2,_3,_4,_5){var _6=false;switch(_2){case"MS":case"ms":_1.setMilliseconds(_1.getMilliseconds()+(_3*_4));break;case"S":_6=true;case"s":_1.setSeconds(_1.getSeconds()+(_3*_4));break;case"MN":_6=true;case"mn":_1.setMinutes(_1.getMinutes()+(_3*_4));break;case"H":_6=true;case"h":_1.setHours(_1.getHours()+(_3*_4));break;case"D":_6=true;case"d":_1.setDate(_1.getDate()+(_3*_4));break;case"W":_6=true;case"w":_1.setDate(_1.getDate()+((_3*7)*_4));break;case"M":_6=true;case"m":_1.setMonth(_1.getMonth()+(_3*_4));break;case"Q":_6=true;case"q":_1.setMonth(_1.getMonth()+((_3*3)*_4));break;case"Y":_6=true;case"y":_1.setFullYear(_1.getFullYear()+(_3*_4));break;case"DC":_6=true;case"dc":_1.setFullYear(_1.getFullYear()+((_3*10)*_4));break;case"C":_6=true;case"c":_1.setFullYear(_1.getFullYear()+((_3*100)*_4));break}
if(_6){if(_4>0){_1=this.getEndOf(_1,_2,_5)}else{_1=this.getStartOf(_1,_2,_5)}}
return _1}
,isc.A.getStartOf=function isc_c_DateUtil_getStartOf(_1,_2,_3){var _4,_5,_6,_7,_8,_9,_10;if(_3==null)_3=_1.logicalDate;if(_3&&this.$fw[_2]==true){this.logInfo("DateUtil.getStartOf() passed period:"+_2+" for logical date. Ignoring");var _11=new Date(_1.getTime());_1.logicalDate=true;return _11}
if(!isc.Time.$e7||_3){_5=_1.getMonth();_6=_1.getDate();_4=_1.getFullYear();_7=_1.getHours();_8=_1.getMinutes();_9=_1.getSeconds();_10=_1.getDay()}else{var _12=_1.$fo(isc.Time.getUTCHoursDisplayOffset(_1),isc.Time.getUTCMinutesDisplayOffset(_1));_5=_12.getUTCMonth();_6=_12.getUTCDate();_4=_12.getUTCFullYear();_7=_12.getUTCHours();_8=_12.getUTCMinutes();_9=_12.getUTCSeconds();_10=_12.getDay()}
switch(_2){case"s":case"S":return Date.createDatetime(_4,_5,_6,_7,_8,_9,0);case"mn":case"MN":return Date.createDatetime(_4,_5,_6,_7,_8,0,0);case"h":case"H":return Date.createDatetime(_4,_5,_6,_7,0,0,0);case"d":case"D":return Date.createDatetime(_4,_5,_6,0,0,0,0);case"w":case"W":if(_3){return Date.createLogicalDate(_4,_5,(_6-_10))}else{return Date.createDatetime(_4,_5,(_6-_10),0,0,0,0)}
case"m":case"M":if(_3){return Date.createLogicalDate(_4,_5,1)}else{return Date.createDatetime(_4,_5,1,0,0,0,0)}
case"q":case"Q":var _13=_5-(_5%3);if(_3){return Date.createLogicalDate(_4,_13,1)}else{return Date.createDatetime(_4,_13,1,0,0,0,0)}
case"y":case"Y":if(_3){return Date.createLogicalDate(_4,0,1)}else{return Date.createDatetime(_4,0,1,0,0,0,0)}
case"dc":case"DC":var _14=_4-(_4%10);if(_3){return Date.createLogicalDate(_14,0,1)}else{return Date.createDatetime(_14,0,1,0,0,0,0)}
case"c":case"C":var _15=_4-(_4%100);if(_3){return Date.createLogicalDate(_15,0,1)}else{return Date.createDatetime(_15,0,1,0,0,0,0)}}
return _1.duplicate()}
,isc.A.getEndOf=function isc_c_DateUtil_getEndOf(_1,_2,_3){var _4,_5,_6,_7,_8,_9,_10;if(_3==null)_3=_1.logicalDate;if(_3&&this.$fw[_2]==true){this.logInfo("DateUtil.getEndOf() passed period:"+_2+" for logical date. Ignoring");var _11=new Date(_1.getTime());_1.logicalDate=true;return _11}
if(!isc.Time.$e7||_3){_5=_1.getMonth();_6=_1.getDate();_4=_1.getFullYear();_7=_1.getHours();_8=_1.getMinutes();_9=_1.getSeconds();_10=_1.getDay()}else{var _12=_1.$fo(isc.Time.getUTCHoursDisplayOffset(_1),isc.Time.getUTCMinutesDisplayOffset(_1));_5=_12.getUTCMonth();_6=_12.getUTCDate();_4=_12.getUTCFullYear();_7=_12.getUTCHours();_8=_12.getUTCMinutes();_9=_12.getUTCSeconds();_10=_12.getDay()}
switch(_2){case"s":case"S":return Date.createDatetime(_4,_5,_6,_7,_8,_9,999);case"mn":case"MN":return Date.createDatetime(_4,_5,_6,_7,_8,59,999);case"h":case"H":return Date.createDatetime(_4,_5,_6,_7,59,59,999);case"d":case"D":return Date.createDatetime(_4,_5,_6,23,59,59,999);case"w":case"W":var _13=_6+(6-_10);if(_3){return Date.createLogicalDate(_4,_5,_13)}else{return Date.createDatetime(_4,_5,_13,23,59,59,999)}
case"m":case"M":var _11;if(_3){_11=Date.createLogicalDate(_4,_5+1,1);_11.setTime(_11.getTime()-(24*60*60*1000))}else{_11=Date.createDatetime(_4,_5+1,1,0,0,0,0);_11.setTime(_11.getTime()-1)}
return _11;case"q":case"Q":var _14=_5+3-(_5%3),_11;if(_3){_11=Date.createLogicalDate(_4,_14,1);_11.setDate(_11.getDate()-1)}else{_11=Date.createDatetime(_4,_14,1,0,0,0,0);_11.setTime(_11.getTime()-1)}
return _11;case"y":case"Y":if(_3){return Date.createLogicalDate(_4,11,31)}else{return Date.createDatetime(_4,11,31,23,59,59,999)}
case"dc":case"DC":var _15=_4+10-(_4%10);if(_3){return Date.createLogicalDate(_15,11,31)}else{return Date.createDatetime(_15,11,31,23,59,59,999)}
case"c":case"C":var _16=_4+100-(_4%100);if(_3){return Date.createLogicalDate(_16,11,31)}else{return Date.createDatetime(_16,11,31,23,59,59,999)}}
return _1.duplicate()}
);isc.B._maxIndex=isc.C+7;String.prototype.Class="String";isc.$fx=function(){var _1=[Array,Number,Date].getProperty("prototype");for(var i=0;i<_1.length;i++){var _3=_1[i];if(_3.toLocaleString==null){_3.toLocaleString=_3.toString}}
var _4=String.prototype;if(!_4.toLocaleUpperCase){_4.toLocaleUpperCase=_4.toUpperCase;_4.toLocaleLowerCase=_4.toLowerCase}
if(isc.Browser.isMoz){var _5="x",_6=_5.toLocaleString();if(_6!=_5){_4.toBrowserLocaleString=_4.toLocaleString;_4.toLocaleString=_4.toString}
_5=true;_6=_5.toLocaleString();if(_6!=_5+""){Boolean.prototype.toBrowserLocaleString=Boolean.prototype.toLocaleString;Boolean.prototype.toLocaleString=Boolean.prototype.toString}}}
isc.$fx();isc.A=String;isc.A.$fy=new RegExp("'","g");isc.A.$fz=new RegExp("\"","g");isc.A=String.prototype;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.replaceAll=function isc_Strin_replaceAll(_1,_2){return isc.replaceAll(this,_1,_2)}
,isc.A.contains=function isc_Strin_contains(_1){if(_1&&!isc.isA.String(_1))_1=_1.toString();return isc.contains(this,_1)}
,isc.A.startsWith=function isc_Strin_startsWith(_1){if(_1&&!isc.isA.String(_1))_1=_1.toString();return isc.startsWith(this,_1)}
,isc.A.endsWith=function isc_Strin_endsWith(_1){if(_1&&!isc.isA.String(_1))_1=_1.toString();return isc.endsWith(this,_1)}
,isc.A.trim=function isc_Strin_trim(_1){var _2=_1||" \t\n\r",l=this.length,_4=0,_5=l-1,i=0;while(_4<l&&_2.contains(this.charAt(i++)))_4++;i=l-1;while(_5>=0&&_5>=_4&&_2.contains(this.charAt(i--)))_5--;return this.substring(_4,_5+1)}
,isc.A.convertTags=function isc_Strin_convertTags(_1,_2){return(_1?_1:"")+this.replace(/</g,"&lt;").replace(/>/g,"&gt;")+(_2?_2:"")}
,isc.A.asHTML=function isc_Strin_asHTML(_1){var s=this.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/(\r\n|\r|\n) /g,"<BR>&nbsp;").replace(/(\r\n|\r|\n)/g,"<BR>").replace(/\t/g,"&nbsp;&nbsp;&nbsp;&nbsp;");return(_1?s.replace(/ /g,"&nbsp;"):s.replace(/  /g," &nbsp;"))}
,isc.A.unescapeHTML=function isc_Strin_unescapeHTML(){return this.replace(/&nbsp;/g," ").replace(/<BR>/gi,"\n").replace(/&gt;/g,">").replace(/&lt;/g,"<").replace(/&amp;/g,"&")}
,isc.A.toInitialCaps=function isc_Strin_toInitialCaps(){var _1=this.toLowerCase().split(" ");for(var i=0;i<_1.length;i++){_1[i]=_1[i].substring(0,1).toLocaleUpperCase()+_1[i].substring(1)}
return _1.join(" ")}
,isc.A.evalDynamicString=function isc_Strin_evalDynamicString(_1,_2){if(this.indexOf("${")<0)return this.toString();var _3=this,_4,_5,_6,_7;var _8=isc.StringBuffer.create();while((_5=_3.indexOf("${"))!=-1){_6=_3.indexOf("}",_5+1);if(_6==-1)break;if(_3.charAt(_5-1)=='\\'){_8.append(_3.slice(0,_5-1),_3.slice(_5,_6+1));_3=_3.substring(_6+1,_3.length);continue}
var _7=_3.slice(_5+2,_6);var _9;if(_2!=null&&_2[_7]){_9=_2[_7]}else{try{_9=isc.Class.evalWithVars(_7,_2,_1)}catch(e){var _10=_1?_1:isc.Log;_10.logWarn("dynamicContents eval error - returning empty string for block -->${"+_7+"}<-- error was: "+isc.Log.echo(e));_9=isc.emptyString}}
_8.append(_3.slice(0,_5),_9);_3=_3.substring(_6+1,_3.length)}
_8.append(_3);_3=_8.toString();return _3}
,isc.A.asSource=function isc_Strin_asSource(_1){return String.asSource(this,_1)}
,isc.A.cssToCamelCaps=function isc_Strin_cssToCamelCaps(){return this.replace(/-([^a-z]*)([a-z])/g,function(_1,_2,_3,_4,_5){return _2+_3.toUpperCase()})}
);isc.B._maxIndex=isc.C+12;isc.A=String;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.asSource=function isc_String_asSource(_1,_2){if(!isc.isA.String(_1))_1=""+_1;var _3=_2?String.$fy:String.$fz,_4=_2?"'":'"';return _4+_1.replace(/\\/g,"\\\\").replace(_3,'\\'+_4).replace(/\t/g,"\\t").replace(/\r/g,"\\r").replace(/\n/g,"\\n")+_4}
);isc.B._maxIndex=isc.C+1;isc.addMethods(isc,{replaceAll:function(_1,_2,_3){return _1.split(_2).join(_3)},contains:function(_1,_2){if(_1==null)return false;return _1.indexOf(_2)>-1},startsWith:function(_1,_2){if(_1==null)return false;return(_1.lastIndexOf(_2,0)==0)},endsWith:function(_1,_2){if(_1==null)return false;var _3=_1.length-_2.length;if(_3<0)return false;return(_1.indexOf(_2,_3)==_3)},makeXMLSafe:function(_1){if(_1==null)return isc.emptyString;else if(!isc.isA.String(_1))_1=_1.toString();return _1.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(String.$fz,"&quot;").replace(String.$fy,"&apos;").replace(/\r/,"&#x000D;")},makeCDATA:function(_1){return"<![CDATA["+_1.replace(/\]\]>/,"]]<![CDATA[>")+"]]>"}});isc.ClassFactory.defineClass("StringBuffer");isc.SB=isc.StringBuffer;isc.A=isc.StringBuffer;isc.A.$f0=[];isc.A.$f1=50;isc.A=isc.StringBuffer.getPrototype();isc.A.maxStreamLength=(isc.Browser.isIE6?1000:100000);isc.A.addPropertiesOnCreate=false;isc.A=isc.StringBuffer.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.init=function isc_StringBuffer_init(){this.$f2=[]}
,isc.A.append=function isc_StringBuffer_append(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13,_14,_15,_16,_17,_18,_19,_20,_21,_22,_23,_24,_25,_26,_27){var _28=this.$f2,_29,_30;if(_1!=null&&_1.constructor.$n==2){var _31=_1.length;if(_31<=30){var _31=_28.length;for(var i=0;i<_1.length;i++){_28[_31++]=_1[i]}}else{_28[_28.length]=_1.join(isc.emptyString)}}else{if(_27===_30&&_26===_30&&_25===_30){if(_1!=null)_28[_28.length]=_1;if(_2!=null)_28[_28.length]=_2
if(_3!=null)_28[_28.length]=_3
if(_4!=null)_28[_28.length]=_4
if(_5!=null)_28[_28.length]=_5
if(_6!=null)_28[_28.length]=_6
if(_7!=null)_28[_28.length]=_7
if(_8!=null)_28[_28.length]=_8
if(_9!=null)_28[_28.length]=_9
if(_10!=null)_28[_28.length]=_10
if(_11!=null)_28[_28.length]=_11
if(_12!=null)_28[_28.length]=_12
if(_13!=null)_28[_28.length]=_13
if(_14!=null)_28[_28.length]=_14
if(_15!=null)_28[_28.length]=_15
if(_16!=null)_28[_28.length]=_16
if(_17!=null)_28[_28.length]=_17
if(_18!=null)_28[_28.length]=_18
if(_19!=null)_28[_28.length]=_19
if(_20!=null)_28[_28.length]=_20
if(_21!=null)_28[_28.length]=_21
if(_22!=null)_28[_28.length]=_22
if(_23!=null)_28[_28.length]=_23
if(_24!=null)_28[_28.length]=_24}else{_29=arguments;for(var i=0,l=_29.length;i<l;i++){_28[_28.length]=_29[i]}}}
if(_28.length>this.maxStreamLength){_28[0]=_28.join(isc.emptyString);_28.length=1}
return this}
,isc.A.appendNumber=function isc_StringBuffer_appendNumber(_1,_2){var _3=this.$f2;if(_2==null){_2=5;var _4=_1;if(_4<0){_4=0-_4;_2+=1}
if(_4>=100000){_4=_4/ 100000;while(_4>=1){_2+=1;_4=_4/ 10}}}
isc.$bp(_3,_1,_3.length,_2)}
,isc.A.clear=function isc_StringBuffer_clear(){this.$f2.length=0}
,isc.A.release=function isc_StringBuffer_release(){var _1=isc.SB,_2=_1.$f0,_3=this.toString();if(_2.length<_1.$f1){this.clear();_2[_2.length]=this}
return _3}
,isc.A.getArray=function isc_StringBuffer_getArray(){return this.$f2}
);isc.B._maxIndex=isc.C+6;isc.StringBuffer.getPrototype().toString=function(){return this.$f2.join(isc.emptyString)}
isc.StringBuffer.$f3=Array.prototype.join;isc.A=isc.StringBuffer;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$b3=[];isc.B.push(isc.A.create=function isc_c_StringBuffer_create(){var _1=this.$f0,_2=_1.length;if(_2>0){var _3=_1[_2-1];_1.length=_2-1;return _3}else{return isc.Class.create.apply(this)}}
,isc.A.concat=function isc_c_StringBuffer_concat(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13,_14,_15,_16,_17,_18,_19,_20,_21,_22,_23,_24,_25,_26,_27,_28,_29,_30,_31,_32,_33,_34,_35,_36,_37,_38,_39,_40,_41,_42,_43,_44,_45,_46,_47,_48,_49,_50,_51,_52){var _53,_54;if(isc.Browser.isIE&&_50===_53&&_51===_53&&_52===_53){var _55=this.$b3;_55.length=0;if(_1!=null)_55[_55.length]=_1;if(_2!=null)_55[_55.length]=_2;if(_3!=null)_55[_55.length]=_3;if(_4!=null)_55[_55.length]=_4;if(_5!=null)_55[_55.length]=_5;if(_6!=null)_55[_55.length]=_6;if(_7!=null)_55[_55.length]=_7;if(_8!=null)_55[_55.length]=_8;if(_9!=null)_55[_55.length]=_9;if(_10!=null)_55[_55.length]=_10;if(_11!=null)_55[_55.length]=_11;if(_12!=null)_55[_55.length]=_12;if(_13!=null)_55[_55.length]=_13;if(_14!=null)_55[_55.length]=_14;if(_15!=null)_55[_55.length]=_15;if(_16!=null)_55[_55.length]=_16;if(_17!=null)_55[_55.length]=_17;if(_18!=null)_55[_55.length]=_18;if(_19!=null)_55[_55.length]=_19;if(_20!=null)_55[_55.length]=_20;if(_21!=null)_55[_55.length]=_21;if(_22!=null)_55[_55.length]=_22;if(_23!=null)_55[_55.length]=_23;if(_24!=null)_55[_55.length]=_24;if(_25!=null)_55[_55.length]=_25;if(_26!=null)_55[_55.length]=_26;if(_27!=null)_55[_55.length]=_27;if(_28!=null)_55[_55.length]=_28;if(_29!=null)_55[_55.length]=_29;if(_30!=null)_55[_55.length]=_30;if(_31!=null)_55[_55.length]=_31;if(_32!=null)_55[_55.length]=_32;if(_33!=null)_55[_55.length]=_33;if(_34!=null)_55[_55.length]=_34;if(_35!=null)_55[_55.length]=_35;if(_36!=null)_55[_55.length]=_36;if(_37!=null)_55[_55.length]=_37;if(_38!=null)_55[_55.length]=_38;if(_39!=null)_55[_55.length]=_39;if(_40!=null)_55[_55.length]=_40;if(_41!=null)_55[_55.length]=_41;if(_42!=null)_55[_55.length]=_42;if(_43!=null)_55[_55.length]=_43;if(_44!=null)_55[_55.length]=_44;if(_45!=null)_55[_55.length]=_45;if(_46!=null)_55[_55.length]=_46;if(_47!=null)_55[_55.length]=_47;if(_48!=null)_55[_55.length]=_48;if(_49!=null)_55[_55.length]=_49;if(_50!=null)_55[_55.length]=_50;if(_51!=null)_55[_55.length]=_51;if(_52!=null)_55[_55.length]=_52;_54=_55.join(isc.emptyString)}else{arguments.join=this.$f3;_54=arguments.join(isc.emptyString)}
return _54}
);isc.B._maxIndex=isc.C+2;isc.defineClass("StringMethod");isc.A=isc.StringMethod.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.toString=function isc_StringMethod_toString(){var _1=this.getValue();if(_1==null||isc.isA.String(_1))return _1;return _1.toString()}
,isc.A.getValue=function isc_StringMethod_getValue(){return this.value}
,isc.A.getDisplayValue=function isc_StringMethod_getDisplayValue(){var _1=this.getValue();if(_1==null||isc.isA.String(_1))return _1;if(_1.title!=null)return"["+_1.title+"]"
return _1}
,isc.A.cdata=function isc_StringMethod_cdata(_1){var _2=_1.indexOf("]]>");if(_2==-1)return"<![CDATA["+_1+"]]>";return this.cdata(_1.slice(0,_2))+"]]&gt;"+this.cdata(_1.slice(_2+3))}
,isc.A.$fu=function isc_StringMethod__xmlSerialize(_1,_2,_3,_4,_5,_6){var _7=this.value;if(isc.isA.String(_7))return isc.Comm.$fv(_1,this.cdata(_7),_2||"stringMethod",_3,_4);else
return isc.StringMethod.$f4(_7,_1,_4,_5,_6)}
);isc.B._maxIndex=isc.C+5;isc.A=isc.StringMethod;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$f5="Action";isc.B.push(isc.A.$f4=function isc_c_StringMethod__xmlSerializeAction(_1,_2,_3,_4,_5){var _6=isc.DataSource.get(this.$f5);if(!_6)return isc.Comm.$f6(_2,_1,_5,_4,_3);return[isc.Comm.$f7(_2),_6.xmlSerialize(_1,null,_3+"        ",this.$f5),"\n",_3,isc.Comm.$f8(_2)].join(isc.emptyString)}
);isc.B._maxIndex=isc.C+1;isc.ClassFactory.defineClass("Cookie");isc.A=isc.Cookie;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.init=function isc_c_Cookie_init(){isc.Cookie.list={};if(document.cookie=="")return;var _1=(""+document.cookie).split("; ");for(var i=0,_3=_1.length,_4;_4=_1[i],i<_3;i++){var _5=_4.indexOf('='),_6=(_5==-1?_4:_4.substring(0,_5));isc.Cookie.list[_6]=(_5==-1?'':unescape(_4.substring(_5+1)))}}
,isc.A.get=function isc_c_Cookie_get(_1){isc.Cookie.init();return isc.Cookie.list[_1]}
,isc.A.set=function isc_c_Cookie_set(_1,_2,_3,_4,_5){isc.Cookie.init();document.cookie=_1+"="+escape(_2)+(_3?";path="+_3:"")+(_4?";domain="+_4:"")+(_5?";expires="+(isc.isA.String(_5)?_5:_5.toGMTString()):"")}
,isc.A.clear=function isc_c_Cookie_clear(_1,_2,_3){isc.Cookie.init();this.set(_1,"",_2,_3,"Thu, 01-Jan-70 00:00:01 GMT")}
,isc.A.getList=function isc_c_Cookie_getList(){isc.Cookie.init();return isc.getKeys(isc.Cookie.list)}
);isc.B._maxIndex=isc.C+5;isc.defineClass("StackTrace");isc.A=isc.StackTrace;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.fromNativeStack=function isc_c_StackTrace_fromNativeStack(_1){if(isc.Browser.isMoz){return isc.MozStackTrace.create({stack:_1})}else if(isc.Browser.isChrome){return isc.ChromeStackTrace.create({stack:_1})}else{return isc.UnsupportedStackTrace.create({stack:_1})}}
);isc.B._maxIndex=isc.C+1;isc.A=isc.StackTrace.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.stack=null;isc.A.$f9="";isc.B.push(isc.A.init=function isc_StackTrace_init(){if(this.stack){this.$ga()}}
,isc.A.extractFunctionFromLine=function isc_StackTrace_extractFunctionFromLine(_1){this.logError("Should implement extractFunctionFromLine in subclass")}
,isc.A.extractArgumentsFromLine=function isc_StackTrace_extractArgumentsFromLine(_1){this.logError("Should implement extractArgumentsFromLine in subclass")}
,isc.A.extractSourceFromLine=function isc_StackTrace_extractSourceFromLine(_1){this.logError("Should implement extractSourceFromLine in subclass")}
,isc.A.$ga=function isc_StackTrace__parseStack(){try{var _1=this.stack.split("\n"),_2=isc.StringBuffer.create(),_3=isc.Page.getAppDir(),_4=window.location.protocol+"//"+window.location.host;for(var i=0;i<_1.length;i++){var _6=_1[i],_7=null,_8=null,_9=null;var _10=this.extractFunctionFromLine(_6);if(_10==""){_10="unnamed"}else if(_10.startsWith("isc_")){var _11;if(_10.startsWith("isc_c_")){_10=_10.substring(6);_11=true}else{_10=_10.substring(4)}
_8=_10.substring(0,_10.indexOf("_"));_9=_10.substring(_8.length+1);var _12=isc.ClassFactory.getClass(_8),_13=null;if(_12){_13=_11?_12[_9]:_12.getInstanceProperty(_9)}
if(_13!=null){_10=isc.Func.getName(_13,true);var _14;if(!_11){_14=_12.getArgString(_9)}else{_14=isc.Func.getArgString(_13)}
_7=_14.split(",")}else{_10=_10.replace(/_{1}/,".");_10=_10.replace(/_{2}/,"._")}}
_2.append("    ",_10,"(");var _14=this.extractArgumentsFromLine(_6);var _15=0;while(_14&&_14.length>0){if(_15>0)_2.append(", ");if(_7)_2.append(_7[_15]+"=>");var _16=_14.length;_14=this.$gb(_14,_2);if(_14.length==_16){isc.logWarn("failure to parse next arg at:\n"+_14);break}
_15++}
_2.append(")");var _17=_6.lastIndexOf("@");_2.append(this.$gc(this.extractSourceFromLine(_6),_3,_4));_2.append("\n")}
this.$f9=_2.toString()}
catch(e){this.$f9=this.stack}}
,isc.A.$gb=function isc_StackTrace__parseArgument(_1,_2){var _3=_1.charAt(0);if(_3=="\""){var _4=_1.search(/[^\\]"/);if(_4==-1)_4=_1.length;var _5=_1.substring(0,_4+2);if(_5.length>40){_5=_5.substring(0,40)+"...\"[ "+_5.length+"]"}
_2.append(_5);return _1.substring(_4+3)}else if(_3=="["){var _6=_1.substring(1).indexOf("]"),_7=_1.substring(0,_6+2);if(_7=="[object Object]")_7="{Obj}";_2.append(_7);return _1.substring(_6+3)}else if(_1.startsWith("(void 0)")){_2.append("undef");return _1.substring(9)}else if(_1.startsWith("undefined")){_2.append("undef");return _1.substring(10)}else if(_1.startsWith("(function ")){var _8=_1.substring(1,_1.indexOf("{"));if(_8.endsWith(" "))_8=_8.substring(0,_8.length-1);_2.append(_8);var _9=_1.indexOf("}),");if(_9==-1)return"";return _1.substring(_9+3)}else{var _10=_1.indexOf(",");if(_10==-1)_10=_1.length;_2.append(_1.substring(0,_10));return _1.substring(_10+1)}}
,isc.A.$gc=function isc_StackTrace__getSourceLine(_1,_2,_3){var _4=_1.indexOf("/system/modules/ISC_"),_5=_1.indexOf("/system/development/ISC_");if(_4!=-1){_1=_1.substring(_4+16)}else if(_5!=-1){_1=_1.substring(_5+20)+"[d]"}
if(_4!=-1||_5!=-1){if(!this.logIsDebugEnabled("traceLineNumbersCore"))return"";var _6=_1.indexOf("?isc_version");if(_6!=-1){_1=_1.substring(0,_6)+_1.substring(_1.indexOf(":"))}}
if(_1.startsWith(_2)){_1=_1.substring(_2.length)}else if(_1.startsWith(_3)){_1=_1.substring(_3.length)}
return" @ "+_1}
,isc.A.toString=function isc_StackTrace_toString(){return this.$f9}
);isc.B._maxIndex=isc.C+8;isc.defineClass("MozStackTrace",isc.StackTrace);isc.A=isc.MozStackTrace.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.extractFunctionFromLine=function isc_MozStackTrace_extractFunctionFromLine(_1){var _2=_1.indexOf("(");return _1.substring(0,_2)}
,isc.A.extractArgumentsFromLine=function isc_MozStackTrace_extractArgumentsFromLine(_1){var _2=_1.indexOf("(");var _3=_1.lastIndexOf("@");return _1.substring(_2+1,_3-1)}
,isc.A.extractSourceFromLine=function isc_MozStackTrace_extractSourceFromLine(_1){var _2=_1.lastIndexOf("@");if(_2>=0){return _1.substring(_2+1)}else{return""}}
);isc.B._maxIndex=isc.C+3;isc.defineClass("ChromeStackTrace",isc.StackTrace);isc.A=isc.ChromeStackTrace.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$gd=/Object\.([^ ]+)/;isc.A.$ge=/\((.+)\)/;isc.B.push(isc.A.extractFunctionFromLine=function isc_ChromeStackTrace_extractFunctionFromLine(_1){var _2=_1.match(this.$gd);return _2?_2[1]:""}
,isc.A.extractArgumentsFromLine=function isc_ChromeStackTrace_extractArgumentsFromLine(_1){return""}
,isc.A.extractSourceFromLine=function isc_ChromeStackTrace_extractSourceFromLine(_1){var _2=_1.match(this.$ge);return _2?_2[1]:""}
);isc.B._maxIndex=isc.C+3;isc.defineClass("UnsupportedStackTrace",isc.StackTrace);isc.A=isc.UnsupportedStackTrace.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.$ga=function isc_UnsupportedStackTrace__parseStack(){}
,isc.A.toString=function isc_UnsupportedStackTrace_toString(){return this.stack}
);isc.B._maxIndex=isc.C+2;isc.$gf={getCallTrace:function(_1,_2,_3){if(_1==null)_1=arguments.caller;if(_1==null)return"[getCallTrace(): Error: couldn't get arguments object]";var _4,_5=_1.callee;if(_5==null){_4="[args.callee == null]"}else if(!isc.Func){_4="[Func utility class not loaded]"}else{_4=isc.Func.getName(_5,true)}
_4+="(";var _6=(_5!=null?isc.Func.getArgs(_5):[]);var _7=Math.max(_1.length,_6.length);for(var i=0;i<_7;i++){var _9=_6[i],_10=_1[i];if(i>0)_4+=", ";if(_9!=null){_4+=_9+"=>"}
_4+=this.echoLeaf(_10)}
_4+=")";_2=_2||_1.$de;if(_2)_4+=" on "+this.echoLeaf(_2);if(!_3&&!_5.$dr)return _4;var _11=this.$gg(_5);if(!_5.$dr){var _12=_11.split(/[\r\n]+/);if(_12.length>1||_12[0].length>200)return _4}
_4+='\n        "'+_11+'"';return _4},$gg:function(_1){var _2=isc.Func.getBody(_1);return _2.trim()},getStackTrace:function(_1,_2,_3,_4){var _5="";_5+=this.$gh(_1,_2,_3);if(this.hasFireBug()&&!_4){isc.Log.$gi=isc.Log.$gi||0;var _6="FBugTrace"+isc.Log.$gi++;_5+="\r\n"+this.fireBugTrace(_6)}
return _5},$gh:function(_1,_2,_3){if(!arguments||!arguments.callee||!arguments.callee.caller){return" [Stack trace not supported in this browser]"}
if(_1==null)_1=arguments.caller||arguments.callee.caller.arguments;var _4=[];var _5=isc.Browser.isIE&&isc.Browser.version<=5;if(_2!=null){for(var i=0;i<_2;i++){if(_1==null)break;if(!_5){_1=_1.callee.caller.arguments}else{_1=_1.caller}}}
if(_1==null){return""}
var _7=_1.callee;var _8=[];var _9=true;if(_3==null)_3=Number.MAX_VALUE;var _10=0;while(_7!=null&&_1!=null&&_10<_3){if(_1.timerTrace){_4.add("\nStack trace for setTimeout() call:   "+_1.timerTrace);break}
if(!_5){if(_8.contains(_7)){_4.add("    ** recursed on "+isc.Func.getName(_7,true));break}
_8.add(_7)}
_4.add("    "+this.getCallTrace(_1,null,(_9||_1.callee.caller==null)));if(_10==0){var _11=this.$gj(_1.$gk);if(_11)_4.add(_11)}
_7=_1.callee;if(!_5){_7=_7.caller;if(_7)_1=_7.arguments}else _1=_1.caller;_9=false;_10++}
if(_4.length==0)return"";return"\r\n"+_4.join("\r")+"\r"},hasFireBug:function(){return isc.Browser.isMoz&&window.console!=null&&window.console.trace!=null},fireBugVersion:function(){return this.hasFireBug()?window.console.firebug:null},fireBugTrace:function(_1){window.console.trace(_1);return" [Complete stack trace logged via Firebug: "+_1+"]"},$gj:function(_1){var _2=isc.SB.create();for(var _3 in _1){var _4=_1[_3],_5;if(_4===_5)continue;if(isc.startsWith(_3,isc.$ak))continue;_2.append("\n        "+_3+" = "+this.echoLeaf(_4))}
return _2.toString()},$a3:function(_1,_2,_3,_4){if(_1.$gl)return;_1.$gl=true;var _5=_1.toString();if(_1.stack){_5+="\n";_5+=isc.StackTrace.fromNativeStack(_1.stack).toString()}else{_5+="  [No error.stack available]"}
this.logWarn(_5)},transformMozStackTrace:function(_1){return isc.StackTrace.fromNativeStack(_1).toString()},echoLeaf:function(_1,_2){var _3="",_4;if(_1===_4)return"undef";try{if(isc.isA.Class(_1)){_3+=_1.toString()}else if(isc.isAn.Array(_1)){_3+="Array["+_1.length+"]"}else if(isc.isA.Date(_1)){_3+="Date("+_1.toShortDate()+")"}else if(isc.isA.Function(_1)){_3+=isc.Func.getName(_1,true)+"()"}else{switch(typeof _1){case"string":if(_1.length<=40||_2){_3+='"'+_1+'"';break}
_3+='"'+_1.substring(0,40)+'..."['+_1.length+']';_3=_3.replaceAll("\n","\\n").replaceAll("\r","\\r");break;case"object":if(_1==null){_3+="null";break}
if(_1.tagName!=null){_3+="["+_1.tagName+"Element]"+this.getIDText(_1);break}
var _5=""+_1;if(_5!=""&&_5!="[object Object]"&&_5!="[object]")
{_3+=_5;break}
_3+="Obj"+this.getIDText(_1);break;default:_3+=""+_1}}
return _3}catch(e){var _6="[Error in echoLeaf: "+e+"]";_3+=_6;this.logDebug(_6,"Log");return _3}},getIDText:function(_1){var _2=_1.name||(isc.isAn.XMLNode(_1)?_1.getAttribute("name"):null);if(_2!=null&&!isc.isAn.emptyString(_2))return"{name:"+_2+"}";var _3=_1.ID!=null?_1.ID:_1.id!=null?_1.id:(isc.isAn.XMLNode(_1)?_1.getAttribute("id"):null);if(_3!=null&&!isc.isAn.emptyString(_3))return"{ID:"+_3+"}";if(_1.nodeName!=null&&!isc.isAn.emptyString(_1.nodeName)){return"{nodeName:"+_1.nodeName+"}"}
var _4=_1.title||(isc.isAn.XMLNode(_1)?_1.getAttribute("title"):null);if(_4!=null&&!isc.isAn.emptyString(_4))return"{title:"+_4+"}";var _5=_1.type||(isc.isAn.XMLNode(_1)?_1.getAttribute("type"):null);if(_5!=null&&!isc.isAn.emptyString(_5))return"{type:"+_5+"}";var _5=_1._constructor;if(_5!=null&&!isc.isAn.emptyString(_5))return"{_constructor:"+_5+"}";var _6=_1.label||(isc.isAn.XMLNode(_1)?_1.getAttribute("label"):null);if(_6!=null&&!isc.isAn.emptyString(_6))return"{label:"+_6+"}";var _5=_1.className;if(_5!=null&&!isc.isAn.emptyString(_5))return"{className:"+_5+"}";if(_1.length!=null)return"{length:"+_1.length+"}";return""},echo:function(_1,_2,_3,_4){if(_1==null)return this.echoLeaf(_1);if(_2==null)_2=true;if(_1.tagName)return this.echoDOM(_1);if(typeof _1!="object"||isc.isA.Date(_1))return this.echoLeaf(_1,true);if(isc.isAn.Array(_1)){var _5=(_3?"[\n":"[");for(var i=0;i<_1.length;i++){_5+=(_3?this.echo(_1[i],_2):this.echoLeaf(_1[i]));if(i+1<_1.length)_5+=(_3?",\n":", ")}
_5+="\n]";return _5}
var _5="{";if(_1.getUniqueProperties!=null){_5=_1.getClassName()+"{";_1=_1.getUniqueProperties();if(_4==null)_4=false}
if(_4==null)_4=true;var _7;try{_7=isc.getKeys(_1)}catch(e){return this.echoLeaf(_1)}
if(isc.Browser.isSafari){var _8=false,_9="[object CSSStyleDeclaration]";try{_8=(_1+""==_9)}catch(e){}
if(_8){_5=_9+"{\n[standard props only]\n";_7=isc.getKeys(isc.Canvas.$gm());_7.add("cssText")}}
for(var i=0;i<_7.length;i++){var _10=_7[i],_11;try{_11=_1[_10]}catch(e){_11="[error accessing property: "+e+"]"}
if(!_4&&isc.isA.Function(_11))continue;if(_10.startsWith("$"))continue;var _12;if(_10==isc.gwtRef){_12="{GWT Java Obj}"}else{_12=this.echoLeaf(_11)}
_5+=_10+": "+_12;if(i+1<_7.length)_5+=(_2?",\r":", ")}
_5+="}";return _5},echoAll:function(_1,_2){return this.echo(_1,_2,true)},echoFull:function(_1){return isc.JSON.encode(_1,{prettyPrint:true,showDebugOutput:true})},echoShort:function(_1){return this.echo(_1,false,false)},echoArray:function(_1){if(!isc.isAn.Array(_1))return this.echo(_1);if(_1.length==0)return"[empty array]";var _2=["["];for(var i=0;i<_1.length;i++){_2.addList([i,":",_1[i],"\n"])}
_2.add("]");return _2.join("")},$gn:{outerText:false,innerText:false,parentTextEdit:false,isTextEdit:false,parentTextEdit:false,contentEditable:false,canHaveHTML:true,isMultiLine:false,filters:false,canHaveChildren:false,behaviorUrns:false,sourceIndex:false,accelerator:false,textDecorationUnderline:false,textDecorationNone:false},echoDOM:function(_1){return this.echoDelta(_1,window.Node,_1.tagName+this.getIDText(_1))},echoEvent:function(_1){return this.echoDelta(_1,(isc.Browser.isMoz?window.KeyEvent:window.Event))},echoDelta:function(_1,_2,_3){if(_1==null)return null;if(isc.Browser.isIE&&isc.isAn.XMLNode(_1)){var _4="<"+_1.tagName+" [XMLNode] ";var _5=_1.attributes;for(var i=0;i<_5.length;i++){var _7=_5[i];if(i>0)_4+=" ";_4+=_7.name+"="+this.echoLeaf(_7.value)}
_4+=(i>0?" [":"")+_1.childNodes.length+" child nodes]>";return _4}
var _4=(_3||isc.emptyString)+"{",_8=isc.getKeys(_1);for(var i=0;i<_8.length;i++){var _9=_8[i];if(this.$gn[_9]!=null)continue;if(_2!=null&&_2[_9]!=null)continue;if(_9.length>3&&_9.toUpperCase()==_9)continue;try{var _10=_1[_9];if(_10==null||_10=="")continue;if(isc.isA.Function(_10))continue;_4+=_9+": "+this.echoLeaf(_1[_9])}catch(e){_4+=_9+": "+this.echoLeaf(e)}
if(i+1<_8.length)_4+=", "}
_4+="}";return _4},echoElementSize:function(_1){var _2;return this.echo({scrollLeft:_1.scrollLeft,scrollTop:_1.scrollTop,scrollWidth:_1.scrollWidth,scrollHeight:_1.scrollHeight,clientWidth:_2,clientHeight:_2,offsetWidth:_1.offsetWidth,offsetHeight:_1.offsetHeight,styleLeft:_1.style.left,styleTop:_1.style.top,styleWidth:_1.style.width,styleHeight:_1.style.height,styleClip:_1.style.clip})}};isc.Class.addProperties(isc.$gf)
isc.Class.addClassProperties(isc.$gf)
isc.$go={logMessage:function(_1,_2,_3,_4){var _5=isc.Log;if(!_5)return;if(_1==null)_1=_5.defaultPriority;if(_1<=_5.stackTracePriority&&this.getStackTrace!=null){_2+="\nStack trace:\n"+this.getStackTrace(arguments,2)}
if(!_3)_3=this.Class;_5.log(_1,_2,_3,this.ID,this,_4)},logDebug:function(_1,_2){return this.logMessage(isc.Log.DEBUG,_1,_2)},logInfo:function(_1,_2){return this.logMessage(isc.Log.INFO,_1,_2)},logWarn:function(_1,_2){return this.logMessage(isc.Log.WARN,_1,_2)},logError:function(_1,_2){return this.logMessage(isc.Log.ERROR,_1,_2)},logFatal:function(_1,_2){return this.logMessage(isc.Log.FATAL,_1,_2)},logIsEnabledFor:function(_1,_2){return(isc.Log.isEnabledFor&&isc.Log.isEnabledFor((_2?_2:this.Class),_1,this))},logIsDebugEnabled:function(_1){return this.logIsEnabledFor(isc.Log.DEBUG,_1)},logIsInfoEnabled:function(_1){return this.logIsEnabledFor(isc.Log.INFO,_1)},logIsWarnEnabled:function(_1){return this.logIsEnabledFor(isc.Log.WARN,_1)},logIsErrorEnabled:function(_1){return this.logIsEnabledFor(isc.Log.ERROR,_1)},setLogPriority:function(_1,_2){isc.Log.setPriority(_1,_2,this)},setDefaultLogPriority:function(_1){isc.Log.setDefaultPriority(_1,this)},getDefaultLogPriority:function(){return isc.Log.getDefaultPriority(this)},clearLogPriority:function(_1){isc.Log.clearPriority(_1,this)}};isc.Class.addMethods(isc.$go)
isc.Class.addClassMethods(isc.$go)
isc.ClassFactory.defineClass("Log");isc.A=isc.Log;isc.A.FATAL=1;isc.A.ERROR=2;isc.A.WARN=3;isc.A.INFO=4;isc.A.DEBUG=5;isc.A.PRIORITY_NAMES=["NONE","FATAL","ERROR","WARN","INFO","DEBUG"];isc.A=isc.Log;isc.A.defaultPriority=isc.Log.WARN;isc.A.stackTracePriority=isc.Log.ERROR;isc.A.$gp={};isc.A.$gq={};isc.A.$gr=1000;isc.A.$gs=0;isc.A.$gt=[];isc.A.$gu=":";isc.A.$gv=".";isc.A._allCategories="_allCategories";isc.A.$gw="$gw";isc.A=isc.Log;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A._1zero="0";isc.A._2zero="00";isc.A.showInlineLogs=false;isc.A.$gx="$T_";isc.A.$gy={};isc.A.$gz=["Timed ",,": ",,"ms"];isc.A.flashHiliteCount=7;isc.A.flashHilitePeriod=500;isc.B.push(isc.A.applyLogPriorities=function isc_c_Log_applyLogPriorities(_1){if(!this.$gp){this.$gp={}}
if(_1){isc.addProperties(this.$gp,_1)}}
,isc.A.getLogPriorities=function isc_c_Log_getLogPriorities(_1,_2){var _3;if(_1!=null){var _4=this.$g0(_1);_3=this.$gq[_4];if(_2){return isc.addProperties({},_3)}}
var _5=isc.addProperties({},this.$gp);if(_3)_5=isc.addProperties(_5,_3);return _5}
,isc.A.$g0=function isc_c_Log__getObjectID(_1){var _2;if(_1==null)_2=isc.emptyString;else _2=(_1.getID?_1.getID():_1.getClassName());return _2}
,isc.A.getPriority=function isc_c_Log_getPriority(_1,_2){if(_2!=null){var _3=this.$g0(_2),_4=this.$gq[_3];if(_4){if(_4._allCategories!=null)return _4._allCategories;if(_4[_1]!=null)return _4[_1];if(_4.$gw!=null)return _4.$gw}}
var _5=this.$gp;return _5[_1]||_5.$gw}
,isc.A.setPriority=function isc_c_Log_setPriority(_1,_2,_3){if(_3!=null){var _4=this.$g0(_3);if(this.$gq[_4]==null)
this.$gq[_4]={};if(!_1)_1=this._allCategories;this.$gq[_4][_1]=_2}else{this.$gp[_1]=_2}}
,isc.A.setDefaultPriority=function isc_c_Log_setDefaultPriority(_1,_2){if(!_2||_2==isc.Log)isc.Log.defaultPriority=_1;else isc.Log.setPriority("$gw",_1,_2)}
,isc.A.getDefaultPriority=function isc_c_Log_getDefaultPriority(_1){var _2;if(_1&&_1!=isc.Log)_2=this.getPriority("$gw",_1);return _2||isc.Log.defaultPriority}
,isc.A.clearPriority=function isc_c_Log_clearPriority(_1,_2){if(_2){var _3=this.$g0(_2);if(!_1)
delete this.$gq[_3];else if(this.$gq[_3])
delete this.$gq[_3][_1]}else{delete this.$gp[_1]}}
,isc.A.isEnabledFor=function isc_c_Log_isEnabledFor(_1,_2,_3){if(!_1)_1=isc.$ah;while(_1!=isc.$ah){var _4=this.getPriority(_1,_3);if(_4!=null){return _2<=_4}
var _5=_1.lastIndexOf(this.$gv);if(_5>0){_1=_1.substring(0,_5)}else{break}}
return _2<=isc.Log.defaultPriority}
,isc.A.log=function isc_c_Log_log(_1,_2,_3,_4,_5,_6){if(this.isEnabledFor(_3,_1,_5))
this.addLogMessage(_1,_2,_3,_4,_6);else if(this.reportSuppressedLogs){this.logWarn("suppressed log, category: "+_3+": "+_2)}}
,isc.A.getLogTimestamp=function isc_c_Log_getLogTimestamp(_1){var _2=this.$g1;if(_2==null){_2=this.$g1=[];_2[2]=this.$gu;_2[5]=this.$gu;_2[8]=this.$gv}
if(_1==null)_1=new Date();var _3=_1.getHours(),_4=_1.getMinutes(),_5=_1.getSeconds(),_6=_1.getMilliseconds();_2[1]=_3;if(_3<10)_2[0]=this._1zero;else _2[0]=null;_2[4]=_4;if(_4<10)_2[3]=this._1zero;else _2[3]=null;_2[7]=_5;if(_5<10)_2[6]=this._1zero;else _2[6]=null;_2[10]=_6;if(_6<10)_2[9]=this._2zero;else if(_6<100)_2[9]=this._1zero;else _2[9]=null;return _2.join(isc.$ah)}
,isc.A.getPriorityName=function isc_c_Log_getPriorityName(_1){if(_1==null)return isc.$ah;return this.PRIORITY_NAMES[_1]}
,isc.A.$g2=function isc_c_Log__makeLogMessage(_1,_2,_3,_4,_5){var _6=this.$g3;if(_6==null){_6=this.$g3=[]}
if(!_3)_3=this.category;_6[0]=this.getLogTimestamp(_5);_6[1]=this.$gu;if(this.ns.EH&&this.ns.EH.$g4!=null){_6[2]=this.ns.EH.$g4;_6[3]=this.$gu}
if(_1!=null){_6[4]=this.getPriorityName(_1);_6[5]=this.$gu}
_6[6]=_3;_6[7]=this.$gu;if(_4){_6[8]=_4
_6[9]=this.$gu}
_6[10]=_2;var _7=_6.join(isc.$ah);_6.length=0;return _7}
,isc.A.addLogMessage=function isc_c_Log_addLogMessage(_1,_2,_3,_4,_5){var _6=this.$g2(_1,_2,_3,_4,_5);this.addToMasterLog(_6);if(this.warningLogged!=null&&_1!=null&&_1<=this.WARN){this.warningLogged(_6)}
if(_1!=null&&_1<=this.ERROR){alert(_2)}}
,isc.A.addToMasterLog=function(message){this.$gt[this.$gs]=message;this.$gs++;if(this.$gs>this.$gr){this.$gs=0}
if(this.showInlineLogs){this.updateInlineLogResults()}}
,isc.A.updateInlineLogResults=function isc_c_Log_updateInlineLogResults(){if(isc.Canvas==null||this.$gt==null)return;if(!this.inlineLogCanvas){this.inlineLogCanvas=isc.Canvas.create({width:"50%",height:"100%",overflow:"auto",backgroundColor:"white",canDragReposition:true,autoDraw:true})}
this.inlineLogCanvas.setContents(this.$gt.join("<br>"));this.inlineLogCanvas.bringToFront()}
,isc.A.getMessages=function isc_c_Log_getMessages(){var _1=this.$gt,_2=this.$gs,_3=this.$gr;return _1.slice(_3-_2,_3).concat(_1.slice(0,_2))}
,isc.A.show=function isc_c_Log_show(_1,_2,_3,_4,_5){if(!this.logViewer)this.logViewer=isc.LogViewer.create();this.logViewer.showLog(_1,_2,_3,_4,_5)}
,isc.A.clear=function isc_c_Log_clear(){this.$gt=[];this.$gs=0;if(this.logViewer)this.logViewer.clear()}
,isc.A.evaluate=function isc_c_Log_evaluate(_1,_2){var _3=isc.timeStamp();var _4,_5;if(isc.Log.supportsOnError){_5=isc.Class.evalWithVars(_1,_2,this)}else{try{_5=isc.Class.evalWithVars(_1,_2,this)}catch(e){_4=e}}
var _6=isc.timeStamp(),_7=isc.Log.getLogTimestamp()+":";var _8=_1.split(/[\r\n]+/);if(_8.length>1)_1=_8[0]+"...";if(_1.length>200)_1=_1.substring(0,200)+"...";if(_4){if(!isc.Log.supportsOnError){isc.Log.$a3(_4);return}
_7+="Evaluator: '"+_1+"' returned a script error: \r\n"+"'"+_4+"'"}else{_7="Evaluator: result of '"+_1+"' ("+(_6-_3)+"ms):\r\n"+this.echo(_5)}
if(this.logViewer)this.logViewer.addToLog(_7,true)}
,isc.A.updateStats=function isc_c_Log_updateStats(_1){if(this.logViewer)this.logViewer.updateStats(_1)}
,isc.A.$g5=function isc_c_Log__logPrelogs(){var _1=isc.$m;if(!_1)return;for(var i=0;i<_1.length;i++){var _3=_1[i];if(isc.isA.String(_3))this.logDebug(_3);else this.logMessage(_3.priority||isc.Log.INFO,_3.message,_3.category,_3.timestamp)}
isc.$m=null}
,isc.A.traceMethod=function isc_c_Log_traceMethod(_1,_2,_3){var _4=this.validObservation(_1,_2);if(!_4)return;if(!this.$g6)this.$g6={};if(!this.$g6[_1])this.$g6[_1]=[];if(!this.$g7)this.$g7=isc.Class.create();var _5=this.$g7;if(_5.isObserving(_4,_2)&&this.$g6[_1].contains(_2))
{_5.ignore(_4,_2);this.logWarn("MethodTimer: Stopped logging stack traces for "+_2+" method on "+_1);this.$g6[_1].remove(_2)}else{var _6=_4.ID?_4.ID:(_4.Class?_4.Class:_4),_7="isc.Log.logWarn('"+_6+"."+_2+"() - trace:' +";if(_3){_7+="'\\n' + isc.Log.getCallTrace(arguments))"}else{_7+="isc.Log.getStackTrace())"}
this.logWarn("expression is: "+_7);_5.observe(_4,_2,_7);this.logWarn("MethodTimer: Logging traces whenever "+_2+" method on "+_1+" is called");this.$g6[_1].add(_2)}}
,isc.A.traceCall=function isc_c_Log_traceCall(_1,_2){this.traceMethod(_1,_2,true)}
,isc.A.timeMethod=function isc_c_Log_timeMethod(_1,_2,_3,_4,_5){var _6=this.validObservation(_1,_2);if(!_6)return;if(!this.$g8)this.$g8={};if(!this.$g8[_1])this.$g8[_1]=[];if(this.$g8[_1].contains(_2))return;var _7=isc.Log.$gx+_2,_8=isc.$am+_2,_9=(_6[_8]?_8:_2);_6[_7]=_6[_9];_6[_9]=isc.Log.makeTimerFunction(_2,_6,_3,_4,_5);this.logWarn("MethodTimer: Timing "+_2+" method on "+_1);this.$g8[_1].add(_2)}
,isc.A.stopTimingMethod=function isc_c_Log_stopTimingMethod(_1,_2){var _3=this.validObservation(_1,_2);if(!_3)return;if(this.$g8[_1].contains(_2)){var _4=isc.Log.$gx+_2,_5=isc.$am+_2,_6=(_3[_5]?_5:_2)
if(!_3[_4]){this.logWarn("Not timing method '"+_2+"' on object '"+_1+"'.");this.$g8[_1].remove(_2);return}
_3[_6]=_3[_4];delete _3[_4];this.logWarn("MethodTimer: "+_2+" method on "+_1+" is no longer being timed");this.$g8[_1].remove(_2);return}}
,isc.A.makeTimerFunction=function isc_c_Log_makeTimerFunction(_1,_2,_3,_4,_5){var _6=_2[_1],_7=isc.Func.getName(_6,true);var _8=function(_12,_13,_14,_15,_16,_17,_18,_19,_20,_21,_22){if(_5)isc.Log.$g9();var _9=isc.timeStamp();var _10=_6.call(this,_12,_13,_14,_15,_16,_17,_18,_19,_20,_21,_22);var _11=(isc.timeStamp()-_9);if(!_4)isc.Log.$ha(this,_7,_11);return _10}
_8.$c5=(_2.ID||_2.Class||"")+"_"+_1+"Timing";_8.$hb=true;_8.$dk=isc.Log.$gx+_1;return _8}
,isc.A.$ha=function isc_c_Log__logTimerResult(_1,_2,_3){if(this.deferTimerLogs)return this.$hc(_1,_2,_3);var _4=isc.Log.$gz;_4[1]=(_1.logWarn?_2:_2+" on "+this.echoLeaf(_1));_4[3]=_3.toFixed(3);var _5=_4.join(isc.emptyString);if(_1.logMessage)_1.logWarn(_5);else isc.Log.logWarn(_5)}
,isc.A.validObservation=function isc_c_Log_validObservation(_1,_2){if(isc.isAn.emptyString(_1)||isc.isAn.emptyString(_2))return false;var _3=_1;if(isc.isA.String(_1)){_3=isc.Class.evaluate(_1);if(!_3){this.logWarn("MethodTimer: "+_1+" is not an object.");return false}}
if(_2.indexOf("(")!=-1){_2=_2.slice(0,_2.indexOf("("))}
if(isc.isA.ClassObject(_3)){var _4=_3.getPrototype();if(isc.isA.Function(_4[_2]))return _4;if(!_3[_2]){this.logWarn("MethodTimer: "+_2+" could not be found as a static or instance property on "+_1);return false}}else if(!_3[_2]){this.logWarn("MethodTimer: "+_2+" is undefined or null on "+_1);return false}
if(!isc.Func.convertToMethod(_3,_2)){this.logWarn("MethodTimer: "+_2+" is not a method on "+_1);return false}
return _3}
,isc.A.hiliteCanvas=function isc_c_Log_hiliteCanvas(_1){var _2=_1;if(isc.isA.String(_1))_2=window[_1];if(!isc.isA.Canvas(_2)){this.logWarn("Unable to find specified canvas '"+_1+"'.");return}
this.showHiliteCanvas(_2.getPageRect())}
,isc.A.hiliteElement=function isc_c_Log_hiliteElement(_1){var _2=_1||this.elementToHilite;if(isc.isA.String(_1))_2=isc.Element.get(_1);if(_2==null){this.logWarn("Unable to find specified element '"+_1+"'.");return}
this.showHiliteCanvas(isc.Element.getElementRect(_2));this.elementToHilite=null}
,isc.A.showHiliteCanvas=function isc_c_Log_showHiliteCanvas(_1){var _2=this._hiliteCanvas;if(!_2){_2=this._hiliteCanvas=isc.Canvas.create({ID:"logHiliteCanvas",autoDraw:false,overflow:"hidden",hide:function(){this.Super("hide",arguments);this.resizeTo(1,1);this.setTop(-20)},border1:"2px dotted red",border2:"2px dotted white"})}
_2.setPageRect(_1);isc.Page.setEvent("click",_2.getID()+".hide()");_2.setBorder(_2.border1);_2.bringToFront();_2.show();this.$hd()}
,isc.A.hideHiliteCanvas=function isc_c_Log_hideHiliteCanvas(){if(this._hiliteCanvas)this._hiliteCanvas.hide()}
,isc.A.$hd=function isc_c_Log__flashHiliteCanvas(){var _1=[this._hiliteCanvas.border1,this._hiliteCanvas.border2];for(var i=0;i<this.flashHiliteCount;i++){isc.Timer.setTimeout({target:this._hiliteCanvas,methodName:"setBorder",args:[_1[i%2]]},(this.flashHilitePeriod*i))}}
);isc.B._maxIndex=isc.C+34;isc.ClassFactory.defineClass("LogViewer");isc.A=isc.LogViewer;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.getGlobalLogCookie=function isc_c_LogViewer_getGlobalLogCookie(){var _1=isc.Cookie.get("GLog");if(!_1)return null;try{var _2=new Function("return "+_1);return _2()}catch(e){this.logWarn("bad log cookie: "+_1+this.getStackTrace())}}
,isc.A.getLogCookie=function isc_c_LogViewer_getLogCookie(){var _1=isc.Cookie.get("Log");if(!_1)return null;try{var _2=new Function("return "+_1);return _2()}catch(e){this.logWarn("bad log cookie: "+_1+this.getStackTrace())}}
);isc.B._maxIndex=isc.C+2;isc.A=isc.LogViewer.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.showConsoleInline=isc.Browser.isTouch;isc.A.$he=0;isc.A.$hf=25;isc.A.$hg="count";isc.B.push(isc.A.logWindowLoaded=function isc_LogViewer_logWindowLoaded(){return(this._logWindowLoaded&&this._logWindow!=null&&!this._logWindow.closed)}
,isc.A.showLog=function isc_LogViewer_showLog(_1,_2,_3,_4,_5){if(_5==null)_5=this.showConsoleInline;if(_2)this._logWindow=_2;if(this.logWindowLoaded()){this._logWindow.setResultsValue(isc.Log.getMessages().join("\r"));if(!this.$hh){this._logWindow.focus()}
return}
if(!isc.Log.logViewer)isc.Log.logViewer=this;if(this._logWindow&&!this._logWindow.closed){return}
var _6={},_7=(_3?null:isc.LogViewer.getGlobalLogCookie());if(_7!=null){_6=_7}else{_6.left=100;_6.top=100;_6.width=640;_6.height=480}
if(_5){if(this.inlineWindow==null){this.inlineWindow=isc.Window.create({title:"Inline Developer Console",src:isc.Page.getIsomorphicClientDir()+"helpers/Log.html",animateMinimize:false,width:"50%",height:Math.round(isc.Page.getHeight()*0.8),headerControls:["headerIcon","headerLabel",isc.Button.create({width:16,height:14,title:"TL",layoutAlign:"center",click:function(){isc.Log.logViewer.inlineWindow.moveTo(0,0)}}),isc.Button.create({width:16,height:14,title:"BL",layoutAlign:"center",click:function(){isc.Log.logViewer.inlineWindow.moveTo(0,isc.Page.getHeight()-isc.Log.logViewer.inlineWindow.getHeight())}}),isc.Button.create({width:16,height:14,title:"TR",layoutAlign:"center",click:function(){isc.Log.logViewer.inlineWindow.moveTo(isc.Page.getWidth()-isc.Log.logViewer.inlineWindow.getWidth(),0)}}),isc.Button.create({width:16,height:14,title:"BR",layoutAlign:"center",click:function(){isc.Log.logViewer.inlineWindow.moveTo(isc.Page.getWidth()-isc.Log.logViewer.inlineWindow.getWidth(),isc.Page.getHeight()-isc.Log.logViewer.inlineWindow.getHeight())}}),"minimizeButton","maximizeButton","closeButton"],showMaximizeButton:true,showMinimizeButton:true,canDragReposition:true,canDragResize:true})}
if(!this.inlineWindow.isDrawn()){this.inlineWindow.draw()}
this.$hh=true}else{var _8="RESIZABLE,WIDTH="+_6.width+",HEIGHT="+_6.height;if(_7){if(isc.Browser.isIE){_8+=",left="+_6.left+",top="+_6.top}else{_8+=",screenX="+_6.left+",screenY="+_6.top}
if(_7.evals)this.$hi=_7.evals.length-1}
_4=_4||"_simpleLog";this._logWindow=window.open(isc.Page.getIsomorphicClientDir()+"helpers/Log.html",_4+(isc.version.contains("version")?"Dev":""),_8)}
this.$hj(_3)}
,isc.A.$hj=function isc_LogViewer__initLogWindow(_1){if(this._logWindow==null&&this.inlineWindow!=null){var _2=this.inlineWindow.body.$hk();if(_2){this._logWindow=this.inlineWindow.body.$hk().contentWindow}
if(this._logWindow==null){return}}
if(this._logWindow==null)return;if(isc.Browser.isIE){try{this._logWindow.$hl=true}catch(e){this.delayCall("$hj",[_1],this.$hf);return}}
if(isc.Browser.isIE||this.$hh){this._logWindow.launchWindow=window;if(this.$hh){this._logWindow.showingInline=true}}
if(_1)this._logWindow.dontSaveState=true;var _3=function(){if(isc.Log.logViewer){var _4=isc.Log.logViewer._logWindow;if(_4&&!_4.closed)_4.focus()}}
isc.Page.setEvent("idle",_3,isc.Page.FIRE_ONCE);if(this._logWindow.initializePage)this._logWindow.initializePage()}
,isc.A.addToLog=function isc_LogViewer_addToLog(_1,_2){if(this.logWindowLoaded()&&!this.$hm){this._logWindow.addToLog(_1,_2)}}
,isc.A.updateStats=function isc_LogViewer_updateStats(_1){if(isc.$hn)return;if(!this.logWindowLoaded())return;var _2=isc.Canvas,_3=this._logWindow.staticForm;if(_1==this.$hg){_3.setValue(_1,_2._canvasList.length-_2._iscInternalCount)}else{_3.setValue(_1,_2._stats[_1])}}
,isc.A.displayEventTarget=function isc_LogViewer_displayEventTarget(){var _1=isc.EH.lastTarget?isc.EH.lastTarget.getID():"";if(_1==this.$ho)return;this.$ho=_1;if(this.logWindowLoaded()){this._logWindow.staticForm.setValue("currentCanvas",_1)}
var _2=isc.EH.lastEvent.nativeTarget;var _3=(_2?(_2.id||_2.ID||_2.tagName):'none')
if(this.logWindowLoaded()){this._logWindow.staticForm.setValue("nativeTarget",_3)}}
,isc.A.displayFocusTarget=function isc_LogViewer_displayFocusTarget(){var _1=isc.EH.getFocusCanvas(),_2=_1?_1.getID():"";if(_2==this.$hp)return;this.$hp=_2;if(this.logWindowLoaded()){this._logWindow.staticForm.setValue("currentFocusCanvas",_2)}}
,isc.A.displayMouseDownTarget=function isc_LogViewer_displayMouseDownTarget(){var _1=isc.EH.mouseDownEvent.target,_2=_1?_1.getID():"";if(this.logWindowLoaded()){this._logWindow.staticForm.setValue("lastMouseDown",_2);if(isc.AutoTest!=null&&isc.Log.showLocatorOnMouseDown){var _3=isc.AutoTest.getLocator();this._logWindow.staticForm.setValue("autoTestLocator",_3||"none")}}}
,isc.A.updateRPC=function isc_LogViewer_updateRPC(){if(this.logWindowLoaded()&&this._logWindow.RPCTracker)
this._logWindow.RPCTracker.dataChanged()}
,isc.A.evaluate=function isc_LogViewer_evaluate(_1,_2){return isc.Log.evaluate(_1,_2)}
,isc.A.clear=function isc_LogViewer_clear(){if(this.logWindowLoaded())this._logWindow.clearResults()}
);isc.B._maxIndex=isc.C+11;isc.$hq=isc.LogViewer.getGlobalLogCookie();if(isc.$hq!=null){isc.Log.applyLogPriorities(isc.$hq.priorityDefaults)
if(isc.$hq.defaultPriority!=null)
isc.Log.defaultPriority=isc.$hq.defaultPriority}else{isc.Log.setPriority("Log",isc.Log.INFO)}
isc.showConsole=function(_1,_2,_3,_4){isc.showLog(_1,_2,_3,_4)}
isc.addGlobal("showLog",function(_1,_2,_3,_4){isc.Log.show(_1,_2,_3,_4)})
isc.addGlobal("showConsoleInline",function(){isc.Log.show(null,null,null,null,true)});isc.Log.logInfo("initialized");isc.Log.$g5();isc.Log.supportsOnError=(isc.Browser.isIE);if(isc.Log.supportsOnError&&!(window.isc_installOnError==false)){window.onerror=function(_1,_2,_3){var _4=arguments.caller,_5;if(_4==null&&arguments.callee.caller!=null){_5=arguments.callee.caller;_4=_5.arguments}
if(_4&&_4.$hr){return}
var _6="Error:\r\t'"+_1+"'\r\tin "+_2+"\r\tat line "+_3;if(_5!=null&&_4==null&&isc.Browser.isIE&&isc.Browser.version>=9)
{_6+="\r\n    crashed in:  "+isc.Func.getName(_5,true)+"()"+"\r\n    Use a pre-9.0 Internet Explorer for best diagnostics, otherwise Firefox or Chrome"}else if(_4!=null){_6+=isc.Log.getStackTrace(_4)}
isc.Log.logWarn(_6);if(isc.Browser.isIE&&isc.useIEDebugger){if(confirm("Run debugger?\r\r"+_6)){debugger}}}}
isc.$hs=function(){return"["+this.Class+" ID:"+this.ID+" (created by: "+this.componentId+")]"}
isc.$ht=function(_1,_2,_3,_4){var _5=isc.Log;if(!_5)return;if(_1==null)_1=_5.defaultPriority;if(_1<=_5.stackTracePriority&&this.getStackTrace!=null){_2+="\nStack trace:\n"+this.getStackTrace(arguments,2)}
if(!_3)_3=this.Class;_5.log(_1,_2,_3,this.ID+" (created by: "+this.componentId+")",this,_4)}
isc.A=Array;isc.A.ASCENDING=true;isc.A.DESCENDING=false;isc.A=Array;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.shouldSortAscending=function isc_Array_shouldSortAscending(_1){if(_1==Array.ASCENDING)return true;if(_1==Array.DESCENDING)return false;if(isc.isA.String(_1)){if(_1.toLowerCase()=="ascending")return true;if(_1.toLowerCase()=="descending")return false}
return null}
);isc.B._maxIndex=isc.C+1;isc.A=Array.prototype;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.sortByProperty=function isc_Arra_sortByProperty(_1,_2,_3,_4){return this.sortByProperties({property:_1,direction:_2,normalizer:_3,context:_4})}
,isc.A.setSort=function isc_Arra_setSort(_1){var _2=[],_3=[],_4=[],_5=[];for(var i=0;i<_1.length;i++){var _7=_1[i];_2[i]=_7.property;_3[i]=Array.shouldSortAscending(_7.direction);_4[i]=_7.normalizer;_5[i]=_7.context}
return this.sortByProperties(_2,_3,_4,_5)}
,isc.A.sortByProperties=function isc_Arra_sortByProperties(){var _1=isc.$hu,_2=isc.$hv;if(isc.isAn.Array(arguments[0])){this.sortProps=arguments[0];this.sortDirections=arguments[1]||[];this.normalizers=arguments[2]||[];this.contexts=arguments[3]||[]}else{if(!this.sortProps){this.sortProps=[];this.normalizers=[];this.sortDirections=[];this.contexts=[]}else{this.sortProps.clear();this.sortDirections.clear();this.normalizers.clear();this.contexts.clear()}
for(var i=0;i<arguments.length;i++){this.sortProps[i]=arguments[i].property;this.sortDirections[i]=arguments[i].direction;this.normalizers[i]=arguments[i].normalizer;this.contexts[i]=arguments[i].context}}
var _4=this.sortProps,_5=this.normalizers,_6=this.contexts;var _7=isc.timestamp();for(var i=0;i<_4.length;i++){isc.$hw[i]=this.sortDirections[i];var _8=_4[i],_9=_5[i],_10=_6[i];var _11;if(_9==null){_11=this.$hx(_4[i])}else if(isc.isA.String(_9)){_11=_9}
if(_11!=null)_9=Array.$hy(_11);if(_9==null)_9=Array.$hz;this.normalizers[i]=_9;if(this.length==0)continue;_1[i]=[];_2[i]=[];if(isc.isA.Function(_9)){for(var _12=0,l=this.length,_14;_12<l;_12++){_14=this[_12];if(_14==null){isc.$h0=true;continue}
_14.$h1=_12;var _15=_9(_14,this.sortProps[i],_10);_1[i][_12]=_15;if(_11!=null&&!Array.$h2(_14[this.sortProps[i]],_11)){_2[i][_12]=_14[this.sortProps[i]]}
var _16;if(isc.isA.SpecialNumber(_15)&&isNaN(_15)){_1[i][_12]=0-Number.MAX_VALUE}}}else{var _17=this.normalizers[i];for(var _12=0,l=this.length,_14;_12<l;_12++){_14=this[_12];if(_14==null){isc.$h0=true;continue}
var _18=_14[this.sortProps[i]];if(_18==null)_18='';var _15=_17[_18];if(_15==null)_15=_18;_14.$h1=_12;_1[i][_12]=_15}}}
var _19=false;for(var i=0;i<isc.$hv.length;i++){if(isc.$hv[i].length>0){_19=true;break}}
isc.$h3=_19;var _20=isc.$hu,_21=isc.$hw,_19=isc.$h3;var _22=this;_22.compareAscending=Array.compareAscending;_22.compareDescending=Array.compareDescending;var _23=function(_32,_33){var _24=(_32!=null?_32.$h1:null),_25=(_33!=null?_33.$h1:null);for(var i=0;i<_20.length;i++){var _26=_20[i][_24],_27=_20[i][_25];if(_19&&_26!=null&&_27!=null){var _28=isc.$hv,_29=_28[i][_24],_30=_28[i][_25];if(_29!=null&&_30!=null){_26=_29;_27=_30}}
var _31=(_21[i]?_22.compareAscending(_26,_27):_22.compareDescending(_26,_27));if(_31!=0)return _31}
return 0};var _7=isc.timeStamp();this.sort(_23);if(isc.$h0){isc.Log.logWarn("Attempt to sort array by property hit null entry where a record should be. Array:"+isc.Log.echo(this));isc.$h0=null}
this.clearProperty("$h1");_1.clear();_2.clear();isc.$hw.clear();this.dataChanged();return this}
,isc.A.unsort=function isc_Arra_unsort(){if(this.sortProps)this.sortProps.clear();return true}
,isc.A.$hx=function isc_Arra__getSortDataType(_1,_2){var _3=(_2!=null?(isc.isAn.Array(_2)?_2:[_2]):this);for(var i=0;i<_3.length;i++){if(!isc.isAn.Object(_3[i]))continue;_2=_3[i][_1];if(_2==null)continue;var _5=Array.$h4(_2);if(_5!=null)return _5}
return null}
,isc.A.$h5=function isc_Arra__getNormalizer(_1,_2){var _3=this.$hx(_1,_2);var _4=Array.$hy(_3);return _4||Array.$hz}
,isc.A.normalize=function isc_Arra_normalize(_1,_2){var _3=(this.normalizer?this.normalizer:this.$h5(_2,_1));return _3(_1,_2)}
);isc.B._maxIndex=isc.C+7;isc.A=Array;isc.A.$h6="$h7";isc.A.$h8="$h9";isc.A=Array;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$a9="string";isc.A.$ia="text";isc.A.$ib="number";isc.A.$ic="integer";isc.A.$id="float";isc.A.$ie="int";isc.A.$if="boolean";isc.A._$Date_="Date";isc.A.$ig="Time";isc.A.$ih="datetime";isc.A.$ii="Datetime";isc.A.$ij="date";isc.A.$ik="time";isc.A.$bc="object";isc.A.$il={"float":"number","int:":"number","integer":"number","text":"string","Date":"date","Time":"date","time":"date"};isc.B.push(isc.A.$hz=function isc_Array__normalizeObj(_1,_2){return _1[_2]}
,isc.A.$im=function isc_Array__normalizeStr(_1,_2){return(isc.isA.String(_1[_2])?_1[_2].toLowerCase():isc.emptyString)}
,isc.A.$in=function isc_Array__normalizeNum(_1,_2){var _3=_1[_2];return isc.isA.Number(_3)?_3:(0-Number.MAX_VALUE)}
,isc.A.$io=function isc_Array__normalizeBool(_1,_2){var _3=_1[_2];if(_3==true)return 1;if(_3==false)return 0;if(_3==null)return-1;return-2}
,isc.A.$ip=function isc_Array__normalizeDate(_1,_2){var _3=(_1[_2]&&isc.isA.Date(_1[_2])?_1[_2].getTime():new Date(_1[_2]).getTime())
if(isNaN(_3))return-8640000000000000;return _3}
,isc.A.$iq=function isc_Array__normalizeTime(_1,_2){var _3=_1[_2];if(!isc.isA.Date(_3)&&_3!=null)_3=isc.Time.parseInput(_3);if(isc.isA.Date(_3))return _3.getTime();return 0}
,isc.A.textToNumericNormalizer=function isc_Array_textToNumericNormalizer(_1,_2){var _3=parseInt(_1[_2],10);if(isc.isA.Number(_3))return _3;else return 0}
,isc.A.$hy=function isc_Array__getNormalizerFromType(_1){if(!_1||!isc.isA.String(_1))return null;switch(_1){case this.$a9:case this.$ia:return Array.$im;case this.$if:return Array.$io;case this._$Date_:case this.$ij:case this.$ii:case this.$ih:return Array.$ip;case this.$ig:case this.$ik:return Array.$iq;case this.$ib:case this.$ic:case this.$ie:case this.$id:return Array.$in}
return Array.$hz}
,isc.A.$h4=function isc_Array__getType(_1){var _2=typeof _1;if(_2==this.$bc){if(isc.isA.Date(_1))_2=this.$ij}
return _2}
,isc.A.$h2=function isc_Array__matchesType(_1,_2){var _3=this.$h4(_1);if(_3==_2)return true;return(this.$il[_2]==_3)}
,isc.A.compareAscending=function isc_Array_compareAscending(_1,_2){if(_1!=null&&_1.localeCompare!=null){var _3=_1.localeCompare(_2);return _3}
if(_2!=null&&_2.localeCompare!=null){var _3=_2.localeCompare(_1);return _3}
return(_2>_1?-1:_2<_1?1:0)}
,isc.A.compareDescending=function isc_Array_compareDescending(_1,_2){if(_1!=null&&_1.localeCompare!=null){var _3=_1.localeCompare(_2);return-1*_3}
if(_2!=null&&_2.localeCompare!=null){var _3=_2.localeCompare(_1);return-1*_3}
return(_2<_1?-1:_2>_1?1:0)}
,isc.A.safariCompareAscending=function isc_Array_safariCompareAscending(_1,_2){if(_1!=null&&_1.localeCompare!=null){var _3=_1.localeCompare(_2);return _3-2}
if(_2!=null&&_2.localeCompare!=null){var _3=_2.localeCompare(_1);return _3-2}
return(_2>_1?-1:_2<_1?1:0)}
,isc.A.safariCompareDescending=function isc_Array_safariCompareDescending(_1,_2){if(_1!=null&&_1.localeCompare!=null){var _3=_1.localeCompare(_2);return-1*(_3-2)}
if(_2!=null&&_2.localeCompare!=null){var _3=_2.localeCompare(_1);return-1*(_3-2)}
return(_2<_1?-1:_2>_1?1:0)}
);isc.B._maxIndex=isc.C+14;isc.$hu=[];isc.$hv=[];isc.$hw=[];(function(){if(isc.Browser.isSafari){var b="b";if(b.localeCompare("a")==3){Array.compareAscending=Array.safariCompareAscending;Array.compareDescending=Array.safariCompareDescending}}})();isc.A=Array.prototype;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.max=function isc_Arra_max(_1,_2){if(_1==null)_1=0;if(_2==null)_2=this.length;var _3=null;for(var i=_1;i<_2;i++){var _5=this[i];if(isc.isA.Number(_5)){if(_3==null)_3=_5;else _3=Math.max(_3,_5)}}
return _3}
,isc.A.min=function isc_Arra_min(_1,_2){if(_1==null)_1=0;if(_2==null)_2=this.length;var _3=null;for(var i=_1;i<_2;i++){var _5=this[i];if(isc.isA.Number(_5)){if(_3==null)_3=_5;else _3=Math.min(_3,_5)}}
return _3}
,isc.A.sum=function isc_Arra_sum(_1,_2){if(_1==null)_1=0;if(_2==null)_2=this.length;var _3=0;for(var i=_1;i<_2;i++)
if(isc.isA.Number(this[i]))_3+=this[i];return _3}
,isc.A.and=function isc_Arra_and(_1,_2){if(_1==null)_1=0;if(_2==null)_2=this.length;for(var i=_1;i<_2;i++)
if(!this[i])return false;return true}
,isc.A.or=function isc_Arra_or(_1,_2){if(_1==null)_1=0;if(_2==null)_2=this.length;var _3=0;for(var i=_1;i<_2;i++)
if(this[i])return true;return false}
);isc.B._maxIndex=isc.C+5;isc.getValueForKey=function(_1,_2,_3){if(_2&&_2[_1]!=null&&!isc.isAn.Array(_2))return _2[_1];return(arguments.length<3?_1:_3)}
isc.getKeyForValue=function(_1,_2,_3){if(_2){for(var _4 in _2){if(_2[_4]==_1)return _4}}
return(arguments.length<3?_1:_3)}
isc.makeReverseMap=function(_1){var _2={},_3;for(var _4 in _1){_3=_1[_4];_2[_3]=_4}
return _2}
isc.sortByKey=function(_1){var _2={},_3=isc.getKeys(_1).sort();for(var i=0;i<_3.length;i++){_2[_3[i]]=_1[_3[i]]}
return _2}
isc.sortByValue=function(_1){return isc.makeReverseMap(isc.sortByKey(isc.makeReverseMap(_1)))}
isc.ClassFactory.defineClass("Time");isc.A=isc.Time;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$ir=[/^\s*(\d?\d)\s*[: ]\s*(\d?\d)\s*[: ]\s*(\d?\d)?\s*([AaPp][Mm]?)?\s*([+-]\d{2}:\d{2}|Z)?\s*$/,/^\s*(\d?\d)\s*[: ]\s*(\d?\d)(\s*)([AaPp][Mm]?)?\s*([+-]\d{2}:\d{2}|Z)?\s*$/,/^\s*(\d\d)(\d\d)(\d\d)?\s*([AaPp][Mm]?)?\s*([+-]\d{2}:\d{2}|Z)?\s*$/,/^\s*(\d)(\d\d)(\d\d)?\s*([AaPp][Mm]?)?\s*([+-]\d{2}:\d{2}|Z)?\s*$/,/^\s*(\d\d?)(\s)?(\s*)([AaPp][Mm]?)?\s*([+-]\d{2}:\d{2}|Z)?\s*$/];isc.A.formatterMap={toTime:{showSeconds:true,padded:false,show24:false},to24HourTime:{showSeconds:true,padded:false,show24:true},toPaddedTime:{showSeconds:true,padded:true,show24:false},toPadded24HourTime:{showSeconds:true,padded:true,show24:true},toShortTime:{showSeconds:false,padded:false,show24:false},toShort24HourTime:{showSeconds:false,padded:false,show24:true},toShortPaddedTime:{showSeconds:false,padded:true,show24:false},toShortPadded24HourTime:{showSeconds:false,padded:true,show24:true}};isc.A.displayFormat="toTime";isc.A.shortDisplayFormat="toShortTime";isc.A.AMIndicator=" am";isc.A.PMIndicator=" pm";isc.B.push(isc.A.setDefaultDisplayTimezone=function isc_c_Time_setDefaultDisplayTimezone(_1,_2){this.$e7=!_2;if(_1==null)return;var _3,_4;if(isc.isA.Number(_1)){_1=-_1;_3=Math.floor(_1/ 60);_4=_1-(_3*60)}else if(isc.isA.String(_1)){var _5=_1.split(":");_3=_5[0];var _6=_3&&_3.startsWith("-");if(_6)_3=_3.substring(1);_4=_5[1];_3=(_6?-1:1)*parseInt(_3,10);_4=(_6?-1:1)*parseInt(_4,10)}
if(isc.isA.Number(_3)&&isc.isA.Number(_4)){this.UTCHoursDisplayOffset=_3;this.UTCMinutesDisplayOffset=_4}}
,isc.A.getDefaultDisplayTimezone=function isc_c_Time_getDefaultDisplayTimezone(){var H=this.UTCHoursDisplayOffset,M=this.UTCMinutesDisplayOffset,_3=H<0;return(!_3?"+":"")+H.stringify(2)+":"+((_3?-1:1)*M).stringify(2)}
);isc.B._maxIndex=isc.C+2;isc.A=isc.Time;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$is=[null,":",null,":"];isc.A.$it=[null,":"];isc.B.push(isc.A.toTime=function isc_c_Time_toTime(_1,_2,_3){return this.format(_1,_2,false,_3)}
,isc.A.toShortTime=function isc_c_Time_toShortTime(_1,_2,_3){return this.format(_1,_2,true,_3)}
,isc.A.format=function isc_c_Time_format(_1,_2,_3,_4){if(!isc.isA.Date(_1))return _1;var _5=_2;if(!_2&&!isc.isA.String(_2)&&!isc.isA.Function(_2)){_2=_3?this.shortDisplayFormat:this.displayFormat}
if(isc.isA.Function(_2))return _2(_1,_4);if(isc.isA.String(_2))_2=this.formatterMap[_2];if(!isc.isAn.Object(_2)){this.logWarn("Invalid time formatter:"+_5+" - using 'toTime'");_2=this.formatterMap.toTime}
var _6=_2.showSeconds,_7=_2.padded,_8=_2.show24;var _9;if(_4!=null)_9=!_4;else _9=!_1.logicalTime&&!_1.logicalDate;var _10,_11;if(!_9){_10=_1.getHours();_11=_1.getMinutes()}else{var _10=_1.getUTCHours(),_11=_1.getUTCMinutes();var _12=this.$e8(_10,_11,this.getUTCHoursDisplayOffset(_1),this.getUTCMinutesDisplayOffset(_1));_10=_12[0];_11=_12[1]}
var _13=_6?_1.getUTCSeconds():null,_14=_8?null:(_10>=12);if(!_8){if(_10>12)_10=_10-12;if(_10==0)_10=12}
if(_7)_10=_10.stringify(2);var _15=_6?this.$is:this.$it;_15[0]=_10;_15[2]=_11.stringify();if(_6)_15[4]=_13.stringify();if(!_8)_15[5]=(_14?this.PMIndicator:this.AMIndicator);else _15[5]=null;return _15.join(isc.emptyString)}
,isc.A.parseInput=function isc_c_Time_parseInput(_1,_2,_3,_4,_5){var _6=0,_7=0,_8=0,_9=0;var _10,_11;if(isc.isA.Date(_1)){_3=true;_6=_1.getUTCHours();_7=_1.getUTCMinutes();_8=_1.getUTCSeconds();_9=_1.getUTCMilliseconds()}else if(_1){for(var i=0;i<isc.Time.$ir.length;i++){var _13=isc.Time.$ir[i].exec(_1);if(_13)break}
if(_13){var _6=Math.min(parseInt(_13[1]|0,10),23),_7=Math.min(parseInt(_13[2]|0,10),59),_8=Math.min(parseInt(_13[3]|0,10),59),_14=_13[4];;if(_14){if(!this.$iu)this.$iu={p:true,P:true,pm:true,PM:true,Pm:true};if(this.$iu[_14]==true){if(_6<12)_6+=12}else if(_6==12)_6=0}
if(_4&&_13[5]!=null&&_13[5]!=""&&_13[5].toLowerCase()!="z"){var _15=_13[5].split(":"),H=_15[0],_17=H&&H.startsWith("-"),M=_15[1];_10=parseInt(H,10);_11=(_17?-1:1)*parseInt(M,10)}}else if(_2)return null}else if(_2)return null;var _19=_4&&_5!=null?_5.duplicate():new Date(null);if(_4||_3){if(_10==null){_10=_3?0:this.getUTCHoursDisplayOffset(_19)}
if(_11==null){_11=_3?0:this.getUTCMinutesDisplayOffset(_19)}
var _20=this.$e8(_6,_7,(0-_10),(0-_11));_6=_20[0];_7=_20[1];if(_6!=null)_19.setUTCHours(_6);if(_7!=null)_19.setUTCMinutes(_7);if(_8!=null)_19.setUTCSeconds(_8);if(_9!=null)_19.setUTCMilliseconds(_9)}else{if(_6!=null)_19.setHours(_6);if(_7!=null)_19.setMinutes(_7);if(_8!=null)_19.setSeconds(_8);if(_9!=null)_19.setMilliseconds(_9)}
if(!_4)_19.logicalTime=true;return _19}
,isc.A.$e8=function isc_c_Time__applyTimezoneOffset(_1,_2,_3,_4){if(_2==null||_1==null){this.logWarn("applyTimezoneOffset passed null hours/minutes");return[_1,_2]}
if(_3==null)_3=0;if(_4==null)_3=0;if(_3==0&&_4==0)return[_1,_2,0];_1+=_3;_2+=_4;while(_2>=60){_2-=60;_1+=1}
while(_2<0){_2+=60;_1-=1}
var _5=0;while(_1>=24){_1-=24;_5+=1}
while(_1<0){_1+=24;_5-=1}
return[_1,_2,_5]}
,isc.A.createDate=function isc_c_Time_createDate(_1,_2,_3,_4,_5){return this.createLogicalTime(_1,_2,_3,_4,_5)}
,isc.A.createLogicalTime=function isc_c_Time_createLogicalTime(_1,_2,_3,_4,_5){var _6=new Date(null);if(_1==null)_1=0;if(_2==null)_2=0;if(_3==null)_3=0;if(_4==null)_4=0;if(_5){_6.setUTCHours(_1);_6.setUTCMinutes(_2);_6.setUTCSeconds(_3);_6.setUTCMilliseconds(_4)}else{_6.setHours(_1);_6.setMinutes(_2);_6.setSeconds(_3);_6.setMilliseconds(_4)}
_6.logicalTime=true;return _6}
,isc.A.setShortDisplayFormat=function isc_c_Time_setShortDisplayFormat(_1){this.shortDisplayFormat=_1}
,isc.A.setNormalDisplayFormat=function isc_c_Time_setNormalDisplayFormat(_1){this.displayFormat=_1}
,isc.A.compareTimes=function isc_c_Time_compareTimes(_1,_2){if(isc.isA.String(_1))_1=isc.Time.parseInput(_1);if(isc.isA.String(_2))_2=isc.Time.parseInput(_2);if(_1==null&&_2==null)return true;if(!isc.isA.Date(_1)||!isc.isA.Date(_2))return false;return((_1.getUTCHours()==_2.getUTCHours())&&(_1.getUTCMinutes()==_2.getUTCMinutes())&&(_1.getUTCSeconds()==_2.getUTCSeconds()))}
,isc.A.$iv=function isc_c_Time__performDstInit(){var _1=new Date(),_2=new Date(0),_3=new Date(0);_2.setUTCFullYear(_1.getUTCFullYear());_2.setUTCMonth(0);_2.setUTCDate(1);_3.setUTCFullYear(_1.getUTCFullYear());_3.setUTCMonth(6);_3.setUTCDate(1);var _4=_1.getTimezoneOffset();this.januaryDstOffset=_2.getTimezoneOffset();var _5=_3.getTimezoneOffset();this.dstDeltaMinutes=this.januaryDstOffset-_5;if(this.dstDeltaMinutes>0){this.southernHemisphere=false;this.adjustForDST=true;if(_4==_5)this.currentlyInDST=true}else if(this.dstDeltaMinutes<0){this.southernHemisphere=true;this.adjustForDST=true;if(_4==this.januaryDstOffset)this.currentlyInDST=true}else{this.adjustForDST=false}
this.dstDeltaMinutes=Math.abs(this.dstDeltaMinutes);this.dstDeltaHours=Math.floor(this.dstDeltaMinutes/ 60);this.dstDeltaMinutes-=(this.dstDeltaHours*60)}
,isc.A.getUTCHoursDisplayOffset=function isc_c_Time_getUTCHoursDisplayOffset(_1){var _2=this.currentlyInDST?-(this.dstDeltaHours):0;if(this.adjustForDST){if(_1.getTimezoneOffset()==this.januaryDstOffset){if(this.southernHemisphere){_2+=this.dstDeltaHours}}else{if(!this.southernHemisphere){_2+=this.dstDeltaHours}}}
return this.UTCHoursDisplayOffset+(this.adjustForDST?_2:0)}
,isc.A.getUTCMinutesDisplayOffset=function isc_c_Time_getUTCMinutesDisplayOffset(_1){var _2=this.currentlyInDST?-(this.dstDeltaMinutes):0;if(this.adjustForDST){if(_1.getTimezoneOffset()==this.januaryDstOffset){if(this.southernHemisphere){_2+=this.dstDeltaMinutes}}else{if(!this.southernHemisphere){_2+=this.dstDeltaMinutes}}}
return this.UTCMinutesDisplayOffset+(this.adjustForDST?_2:0)}
);isc.B._maxIndex=isc.C+13;isc.Time.$iv();isc.Time.setDefaultDisplayTimezone(new Date().getTimezoneOffset(),true);isc.ClassFactory.defineClass("Page");isc.A=isc.Page;isc.A.$iw=[];isc.A.$ix=false;isc.A.defaultUnsupportedBrowserURL="[SKIN]/unsupported_browser.html";isc.A.$iy={};isc.A.protocolURLs=window.isc_protocolURLs||["http://","https://","file://","mailto:","app-resource:","data:"];isc.A.textDirection=null;isc.A.LTR="ltr";isc.A.RTL="rtl";isc.A=isc.Page;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$iz="[SKIN]";isc.A.$i0="[SKIN]/";isc.A.$i1="[";isc.A.$i2="./";isc.A.$i3="..";isc.A.leaveScrollbarGap=isc.Browser.isMoz&&isc.Browser.geckoVersion<20051107;isc.A.getWidth=(isc.Browser.isNS?function(_1,_2){if(!_1)_1=window;_2=_2||(isc.Browser.isMoz&&isc.Browser.geckoVersion>=20080529);if(isc.Browser.isMoz&&_1==window&&!_2){if(this.width!=null){return this.width-(this.leaveScrollbarGap?(isc.Element?isc.Element.getNativeScrollbarSize():16):0)}
this.logInfo("NOTE: isc.Page.getWidth() unable to determine page width.  Returning 500","sizing");return 500}else{var _3=isc.Browser.geckoVersion!=null&&isc.Browser.geckoVersion<20051111;var _4=!this.leaveScrollbarGap&&!_3&&_1.document.body!=null,_5;if(_4){var _6=isc.Browser.isStrict?_1.document.documentElement:_1.document.body;if(_6!=null)_5=_6.clientWidth}
if(_5==null||_5==0){_5=_1.innerWidth}
if(_1==window)this.width=_5;return _5}}:function(_1){if(!_1)_1=window;var _2=_1.document.body;if(isc.Browser.isStrict&&!isc.Browser.isOpera)
_2=_1.document.documentElement;if(_2){return _2.clientWidth}else{if(!isc.Page.isLoaded()){isc.Page.setEvent("load","isc.EH.$i4()",isc.Page.FIRE_ONCE)}
this.logWarn("NOTE: isc.Page.getWidth() called before <BODY> tag was written out -- "+"value cannot be determined.  Returning 500");return 500}});isc.A.getHeight=(isc.Browser.isNS?function(_1,_2){if(!_1)_1=window;_2=_2||(isc.Browser.isMoz&&isc.Browser.geckoVersion>=20080529);if(isc.Browser.isMoz&&_1==window&&!_2){if(this.height!=null)return this.height;return 500}else{var _3=isc.Browser.isMobileWebkit||(!isc.Browser.isStrict&&isc.Browser.geckoVersion>=20051111)&&_1.document.body!=null,_4;if(_3){_4=_1.document.body.clientHeight}
if(_4==null||_4==0){_4=_1.innerHeight}
if(isc.Browser.isTouch&&isc.Browser.isAndroid){if(this.$i5==null){this.$i5=_4;this.$i6=isc.Page.getWidth(_1,_2)}else{if(_4!=this.$i5){var _5=isc.Page.getWidth(_1,_2);if(_5==this.$i6){_4=this.$i5}else{this.$i5=_4;this.$i6=_5}}}}
if(_1==window)this.height=_4;return _4}}:function(_1){if(!_1)_1=window;var _2=_1.document.body;if(isc.Browser.isStrict&&!isc.Browser.isOpera)
_2=_1.document.documentElement;if(_2){return _2.clientHeight}else{this.logWarn("NOTE: isc.Page.getHeight() called before <BODY> tag was written out -- value cannot be determined.  Returning 500");return 500}});isc.A.getScrollHeight=(isc.Browser.isNS?function(_1){var _1=_1||document;var _2=_1.body.scrollHeight;if(isc.isA.Number(_2))return _2}:function(_1){var _1=_1||document;if(_1==null||_1.body==null)return 800;if(isc.Browser.version>=6){return Math.max(_1.body.scrollHeight,_1.documentElement.clientHeight)}
return _1.body.scrollHeight});isc.A.getScrollLeft=(isc.Browser.isNS?function(){return window.pageXOffset}:function(){if(document==null||document.body==null)return 0;return(isc.Browser.isStrict?document.documentElement.scrollLeft:document.body.scrollLeft)});isc.A.getScrollTop=(isc.Browser.isNS?function(){return window.pageYOffset}:function(){if(document==null||document.body==null)return 0;return(isc.Browser.isStrict?document.documentElement.scrollTop:document.body.scrollTop)});isc.A.unsupportedBrowserAction="continue";isc.B.push(isc.A.finishedLoading=function isc_c_Page_finishedLoading(){isc.Page.$ix=true;isc.Log.logInfo("isc.Page is loaded");isc.EH.startIdleTimer();if(isc.Browser.isSafari)isc.Canvas.clearCSSCaches();if(!window.suppressAutoLogWindow){var _1=isc.LogViewer.getLogCookie();if(_1!=null&&_1.keepOpen){isc.Timer.setTimeout("isc.Log.show(true)",1000)}}
if(isc.Time&&isc.Time.UTCHoursOffset!=null){isc.logWarn("This application includes code to set the Time.UTCHoursOffset attribute. "+"This property will be respected but has been deprecated in favor of the "+"classMethod isc.Time.setDefaultDisplayTimezone().");isc.Time.setDefaultDisplayTimezone(isc.Time.UTCHoursOffset.stringify()+":00")}
if(isc.Page.pollPageSize){isc.EH.$i4()}else{isc.EH.delayCall("$i4",[true],200)}}
,isc.A.isLoaded=function isc_c_Page_isLoaded(){return this.$ix}
,isc.A.getBlankFrameURL=function isc_c_Page_getBlankFrameURL(){if(isc.Browser.isIE&&("https:"==window.location.protocol||document.domain!=location.hostname)){return this.getURL("[HELPERS]empty.html")}
return"about:blank"}
,isc.A.setTitle=function isc_c_Page_setTitle(_1){document.title=_1}
,isc.A.setDirectories=function isc_c_Page_setDirectories(_1){if(_1==null){_1={imgDir:window.imgDir,isomorphicDir:(window.isomorphicDir?window.isomorphicDir:window.IsomorphicDir),isomorphicClientDir:window.isomorphicClientDir,isomorphicDocsDir:window.isomorphicDocsDir,skinDir:window.skinDir,helperDir:window.helperDir}}
this.$i7();this.setIsomorphicDir(_1.isomorphicDir);this.setIsomorphicClientDir(_1.isomorphicClientDir);this.setIsomorphicDocsDir(_1.isomorphicDocsDir);this.setAppImgDir(_1.imgDir);this.setSkinDir(_1.skinDir);this.setHelperDir(_1.helperDir)}
,isc.A.$i7=function isc_c_Page__deriveAppDir(){var _1=window.location.href;if(_1.contains("?"))_1=_1.substring(0,_1.indexOf("?"));if(_1.contains("#"))_1=_1.substring(0,_1.indexOf("#"));if(_1.charAt(_1.length-1)!="/"){_1=_1.substring(0,_1.lastIndexOf("/")+1)}
this.$iy.APP=_1;if(this.logIsInfoEnabled()){this.logInfo("app dir is "+this.$iy.APP)}
this.setAppImgDir()}
,isc.A.getAppDir=function isc_c_Page_getAppDir(){return this.$iy.APP}
,isc.A.setAppImgDir=function isc_c_Page_setAppImgDir(_1){this.$iy.APPIMG=this.combineURLs(this.getAppDir(),_1!=null?_1:"[APP]images/")}
,isc.A.getAppImgDir=function isc_c_Page_getAppImgDir(_1){if(_1!=null&&(isc.startsWith(_1,isc.slash)||this.getProtocol(_1)!=isc.emptyString))
{return _1}
if(_1)return this.$iy.APPIMG+_1;else return this.$iy.APPIMG}
,isc.A.setAppFilesDir=function isc_c_Page_setAppFilesDir(_1){this.$iy.APPFILES=this.combineURLs(this.getAppDir(),_1)}
,isc.A.getAppFilesDir=function isc_c_Page_getAppFilesDir(_1){return this.$iy.APPFILES}
,isc.A.setIsomorphicDir=function isc_c_Page_setIsomorphicDir(_1){this.$iy.ISOMORPHIC=this.combineURLs(this.getAppDir(),_1!=null?_1:"../isomorphic/");this.setIsomorphicClientDir();this.setIsomorphicDocsDir()}
,isc.A.getIsomorphicDir=function isc_c_Page_getIsomorphicDir(){return this.$iy.ISOMORPHIC}
,isc.A.setSkinDir=function isc_c_Page_setSkinDir(_1){this.$iy.SKIN=this.combineURLs(this.getAppDir(),_1!=null?_1:"[ISOMORPHIC]/skins/standard/");this.$iy.SKINIMG=this.$iy.SKIN+"images/";if(isc.Canvas)isc.Canvas.$i8=isc.Canvas.$i9=null}
,isc.A.getSkinDir=function isc_c_Page_getSkinDir(){return this.$iy.SKIN}
,isc.A.getSkinImgDir=function isc_c_Page_getSkinImgDir(_1){if(_1==null)return this.$iy.SKINIMG;return this.combineURLs(this.$iy.SKIN,_1)}
,isc.A.setIsomorphicClientDir=function isc_c_Page_setIsomorphicClientDir(_1){this.$iy.ISOMORPHIC_CLIENT=this.combineURLs(this.getAppDir(),_1!=null?_1:"[ISOMORPHIC]/system/");this.setSkinDir();this.setHelperDir()}
,isc.A.getIsomorphicClientDir=function isc_c_Page_getIsomorphicClientDir(){return this.$iy.ISOMORPHIC_CLIENT}
,isc.A.setIsomorphicDocsDir=function isc_c_Page_setIsomorphicDocsDir(_1){this.$iy.ISOMORPHIC_DOCS=this.combineURLs(this.getAppDir(),_1!=null?_1:"[ISOMORPHIC]/system/reference/");this.setIsomorphicDocsSkinDir()}
,isc.A.getIsomorphicDocsDir=function isc_c_Page_getIsomorphicDocsDir(){return this.$iy.ISOMORPHIC_DOCS}
,isc.A.setIsomorphicDocsSkinDir=function isc_c_Page_setIsomorphicDocsSkinDir(_1){this.$iy.ISO_DOCS_SKIN=this.combineURLs(this.getIsomorphicDocsDir(),_1!=null?_1:"skin/")}
,isc.A.getIsomorphicDocsSkinDir=function isc_c_Page_getIsomorphicDocsSkinDir(){return this.$iy.ISO_DOCS_SKIN}
,isc.A.setHelperDir=function isc_c_Page_setHelperDir(_1){this.$iy.HELPERS=this.combineURLs(this.getAppDir(),_1!=null?_1:"[ISOMORPHIC_CLIENT]/helpers/")}
,isc.A.getHelperDir=function isc_c_Page_getHelperDir(){return isc.Page.$iy.HELPERS}
,isc.A.getImgURL=function isc_c_Page_getImgURL(_1,_2){var _3;if(isc.startsWith(_1,this.$iz)){_3=isc.Page.getSkinImgDir(_2);var _4=isc.startsWith(_1,this.$i0)?7:6;_1=_1.substring(_4)}else{_3=isc.Page.getAppImgDir(_2)}
return isc.Page.combineURLs(_3,_1)}
,isc.A.getURL=function isc_c_Page_getURL(_1){if(isc.startsWith(_1,this.$i1)){var _2=_1.indexOf("]");if(_2>0){var _3=_1.substring(1,_2).toUpperCase(),_4=isc.Page.$iy[_3];if(_4!=null){_1=isc.Page.combineURLs(_4,_1.substring(_2+(_1.charAt(_2+1)!="/"?1:2)))}else{this.logDebug("getURL("+_1+"): couldn't find cached directory "+_3)}}else{this.logDebug("getURL("+_1+"): didn't find matching ']' in URL")}}
return _1}
,isc.A.combineURLs=function isc_c_Page_combineURLs(_1,_2){if(!isc.isA.String(_2))return _1;if(isc.startsWith(_2,this.$i1)){return this.getURL(_2)}
var _3=isc.$ah;if(_1==null||_1==_3||isc.Page.getProtocol(_2)!=_3){return _2}
var _4=isc.slash;var _5=isc.Page.getProtocol(_1);if(isc.startsWith(_2,_4)){if(isc.isAn.emptyString(_5)){_1=isc.emptyString}else if(_1.indexOf(_4,_5.length)!=-1){_1=_1.substring(0,_1.indexOf(_4,_5.length))}}else if(_2.indexOf(this.$i2)>-1){_1=_1.substring(_5.length,_1.length-1);var _6=_1.split(_4),_7=_2.split(_4);var _8=_6[0];_6.shift();while(_7[0]==isc.dot||_7[0]==this.$i3){if(_7[0]==isc.dot){_7.shift();continue}
_7.shift();if(_6.length==0)break;_6.pop()}
_1=_5+_8+_4;if(_6.length>0)_1+=_6.join(_4)+_4;_2=_7.join(_4)}
return _1+_2}
,isc.A.getProtocol=function isc_c_Page_getProtocol(_1){for(var i=0;i<isc.Page.protocolURLs.length;i++){if(isc.startsWith(_1,isc.Page.protocolURLs[i]))return isc.Page.protocolURLs[i]}
return isc.$ah}
,isc.A.getLastSegment=function isc_c_Page_getLastSegment(_1){if(_1==null)return isc.emptyString;var _2=_1.lastIndexOf(isc.slash);if(_2==-1)return _1;return _1.substring(_2+1)}
,isc.A.isXHTML=function isc_c_Page_isXHTML(){if(this.$ja!=null)return this.$ja;if(isc.Browser.isIE)return false;var _1=this.getWindow();return(this.$ja=(this.getDocument().constructor==this.getWindow().XMLDocument))}
,isc.A.isRTL=function isc_c_Page_isRTL(){return this.getTextDirection()==isc.Canvas.RTL}
,isc.A.getTextDirection=function isc_c_Page_getTextDirection(){if(this.textDirection==null){var _1=document.documentElement,_2=document.body,_3=(_2?_2.dir:null)||_1.dir;if(_3)return(this.textDirection=_3.toLowerCase());else if(_2)return(this.textDirection=this.LTR);return this.LTR}
return this.textDirection}
,isc.A.loadStyleSheet=function isc_c_Page_loadStyleSheet(_1,_2,_3){var _4=isc.Page.getURL(_1);var _5="<link rel='stylesheet' type='text/css' href=\""+_4+"\"\/>";if(_2==null)_2=window;if(isc.Page.isLoaded()&&_2==window){if(isc.FileLoader){var _6=isc.FileLoader.$jb;if(_6!=null){for(var i=0;i<_6.length;i++){if(_4.indexOf(_6[i])!=-1){this.logDebug("skin "+_6[i]+" already loaded by FileLoader - not loading css file");return}}}
isc.FileLoader.loadCSSFile(_4,_3)}else{this.logWarn("isc.Page.loadStylesheet('"+_1+"') called after page load.  Stylesheet not loaded.")}}else{if(this.isXHTML()){var _8=this.getDocument(),_9=_8.documentElement.firstChild,_10=_8.createElementNS(_8.documentElement.namespaceURI,"link");_10.rel="stylesheet";_10.type="text/css";_10.href=_4;_9.appendChild(_10);this.logWarn("added stylesheet DOM style")}else{_2.document.write(_5)}}}
,isc.A.resizeTo=function isc_c_Page_resizeTo(_1,_2){window.resizeTo(_1,_2)}
,isc.A.moveTo=function isc_c_Page_moveTo(_1,_2){window.moveTo(_1,_2)}
,isc.A.scrollTo=function isc_c_Page_scrollTo(_1,_2){window.scroll(_1,_2)}
,isc.A.getOrientation=function isc_c_Page_getOrientation(){if(window.orientation!=null){return window.orientation==0||window.orientation==180?"portrait":"landscape"}
return this.getWidth()>this.getHeight()?"landscape":"portrait"}
,isc.A.updateViewport=function isc_c_Page_updateViewport(_1,_2,_3,_4){var _5=[];if(_1!=null){if(isc.isA.Number(_1))_1=_1.toFixed(2);_5[_5.length]=("initial-scale="+_1)}
if(_2!=null)_5[_5.length]=("width="+_2);if(_3!=null)_5[_5.length]=("height="+_3);if(_4!=null){_5[_5.length]=("user-scalable="+(_4==false?"no":"yes"));if(_4==false&&_1!=null){_5[_5.length]="minimum-scale="+_1+", maximum-scale="+_1}}
_5=_5.join(", ");var _6=document.getElementsByTagName("meta"),_7;for(var i=_6.length-1;i>=0;i--){if(_6[i].name=="viewport"){_7=_6[i];_7.parentNode.removeChild(_7);_7=null}}
if(_7!=null){_7.content=_5}else{_7=document.createElement('meta');_7.name='viewport';_7.content=_5;document.getElementsByTagName('head')[0].appendChild(_7)}}
,isc.A.getScrollWidth=function isc_c_Page_getScrollWidth(_1){var _1=_1||document;if(_1==null||_1.body==null)return 500;if(isc.Browser.isIE&&isc.Browser.version>=6){return Math.max(_1.body.scrollWidth,_1.documentElement.clientWidth)}
return _1.body.scrollWidth}
,isc.A.getScreenWidth=function isc_c_Page_getScreenWidth(){return screen.width}
,isc.A.getScreenHeight=function isc_c_Page_getScreenHeight(){return screen.height}
,isc.A.getWindowRect=function isc_c_Page_getWindowRect(_1){if(!_1)_1=window;return{left:(isc.Browser.isIE||isc.Browser.isOpera?_1.screenLeft:_1.screenX),top:(isc.Browser.isIE||isc.Browser.isOpera?_1.screenTop:_1.screenY),width:isc.Page.getWidth(_1),height:isc.Page.getHeight(_1)}}
,isc.A.setUnloadMessage=function isc_c_Page_setUnloadMessage(_1){if(_1==null)window.onbeforeunload=null;else window.onbeforeunload=function(){return _1}}
,isc.A.goBack=function isc_c_Page_goBack(){if(history.length==0&&window.opener){window.close()}else{history.back()}}
,isc.A.print=function isc_c_Page_print(_1){if(!_1)_1=window;if(_1.print){_1.print()}else{var _2=_1.document;if(!_2||!_2.body){this.logError("isc.Page.print() called on a window that doesn't have a document.body defined.  Exiting.");return}
if(isc.Browser.isWin){_2.body.insertAdjacentHTML('beforeEnd','<OBJECT ID="printControl" WIDTH=0 HEIGHT=0 CLASSID="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></OBJECT>');var _3=_2.all.printControl;if(!_3){this.logError("isc.Page.print() couldn't create or find print control.  Exiting.");return}
_3.ExecWB(6,1);_3.outerHTML=""}else{alert("Choose 'Print...' from the File menu to print this page.")}}}
,isc.A.observe=function isc_c_Page_observe(_1,_2,_3){var _4=isc.Class.create();return _4.observe(_1,_2,_3)}
,isc.A.waitFor=function isc_c_Page_waitFor(_1,_2,_3,_4,_5){var _6=isc.Class.create({$jc:_1,$jd:_2,$je:_3,_fired:function(_8){if(this.$jf){isc.Timer.clear(this.$jf)}
this.ignore(this.$jc,this.$jd);this.fireCallback(this.$je,"observed",[_8]);this.destroy()},$jg:function(){this.ignore(this.$jc,this.$jd);this.fireCallback(this.$jh);this.destroy()}});isc.ClassFactory.addGlobalID(_6);var _7=_6.observe(_1,_2,_6.getID()+"._fired(observed)");if(!_7)_6.destroy();if(_4&&_5){_6.$jh=_5;_6.$jf=isc.Timer.setTimeout(function(){_6.$jg()},_4)}
return _7}
,isc.A.waitForMultiple=function isc_c_Page_waitForMultiple(_1,_2,_3,_4){var _5=true;var _6=isc.Class.create({$ji:_1,$jj:[],$je:_2,$jk:function(_9){this.$jj.remove(_9);if(this.$jj.isEmpty()){if(this.$jf){isc.Timer.clear(this.$jf)}
this.fireCallback(this.$je);this.destroy()}},$jg:function(){var _7=this.$jj;for(var i=0;i<_7.length;i++){_7[i].ignore(_7[i].$jc,_7[i].$jd);_7[i].destroy()}
this.fireCallback(this.$jh);this.destroy()}});for(var i=0;i<_1.length;i++){var _9=isc.Class.create({$jc:_1[i].object,$jd:_1[i].method,$jl:_6,_fired:function(_11){this.ignore(this.$jc,this.$jd);this.$jl.$jk(this);this.destroy()}});isc.ClassFactory.addGlobalID(_9);var _10=_9.observe(_1[i].object,_1[i].method,_9.getID()+"._fired(observed)");if(_10){_6.$jj.add(_9)}else{_9.destroy();_5=false}}
if(_3&&_4){_6.$jh=_4;_6.$jf=isc.Timer.setTimeout(function(){_6.$jg()},_3)}
return _5}
,isc.A.checkBrowserAndRedirect=function isc_c_Page_checkBrowserAndRedirect(_1){if(!isc.Browser.isSupported){if(isc.Log){isc.Log.logWarn("Unsupported browser detected - userAgent:"+navigator.userAgent)}
if(this.unsupportedBrowserAction=="continue")return;var _2=this.unsupportedBrowserAction=="confirm"&&confirm(this.getUnsupportedBrowserPromptString())
if(_2)return;if(_1==null)_1=isc.Page.defaultUnsupportedBrowserURL;var _3=true;window.location.replace(isc.Page.getURL(_1))}}
,isc.A.getUnsupportedBrowserPromptString=function isc_c_Page_getUnsupportedBrowserPromptString(){var _1="This page uses the Isomorphic SmartClient web presentation layer "+"(Version"+isc.version+" - "+isc.buildDate+"). The web browser you are using is not supported by this version of SmartClient"+" and you may encounter errors on this page. Would you like to continue anyway?\n\n"+"(Reported userAgent string for this browser:"+navigator.userAgent+")";return _1}
);isc.B._maxIndex=isc.C+50;if(isc.Page.isXHTML())isc.nbsp=isc.xnbsp;isc.Page.setDirectories();if(isc.Browser.isMoz){isc.Page.getWidth(null,true);isc.Page.getHeight(null,true)}
isc.addGlobal("Params",function(_1){if(!_1)_1=window;var _2=isc.isA.String(_1)?_1:_1.location.href;var _3=_2.indexOf("?"),_4=_2.indexOf("#");if(_4<0||_4<_3)_4=_2.length;if(_3!=-1){var _5=_2.substring(_3+1,_4).split("&");for(var i=0,_7,_8;i<_5.length;i++){_7=_5[i];if(!_7)continue;_8=_7.indexOf("=");this[_7.substring(0,_8)]=unescape(_7.substring(_8+1))}}})
isc.params=new isc.Params();isc.getParams=function(_1){return new isc.Params(_1)}
isc.ClassFactory.defineClass("Comm");isc.A=isc.Comm;isc.A.sendMethod="POST";isc.A.$jm=0;isc.A=isc.Comm;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.xmlHttpConstructors=["MSXML2.XMLHTTP","Microsoft.XMLHTTP","MSXML.XMLHTTP","MSXML3.XMLHTTP"];isc.A.$jn=[];isc.B.push(isc.A.$jo=function isc_c_Comm__fireXMLCallback(_1,_2){isc.EH.$jp("XRP");isc.Class.fireCallback(_2,"xmlHttpRequest",[_1],null,true);isc.EH.$jq()}
,isc.A.$jr=function isc_c_Comm__getStateChangeHandler(){return function(){var _1=arguments.callee.request;if(!_1)return;if(_1.readyState!=4)return;arguments.callee.request=null;isc.Timer.setTimeout({target:isc.Comm,methodName:"$jo",args:[_1,arguments.callee.callback]},0)}}
,isc.A.createXMLHttpRequest=function isc_c_Comm_createXMLHttpRequest(){if(isc.Browser.isIE){var _1;if(this.preferNativeXMLHttpRequest){_1=this.getNativeRequest();if(!_1)_1=this.getActiveXRequest()}else{_1=this.getActiveXRequest();if(!_1)_1=this.getNativeRequest()}
if(!_1)isc.rpc.logWarn("Couldn't create XMLHttpRequest");return _1}else{return new XMLHttpRequest()}}
,isc.A.getNativeRequest=function isc_c_Comm_getNativeRequest(){var _1;if(isc.Browser.version>=7){isc.rpc.logDebug("Using native XMLHttpRequest");_1=new XMLHttpRequest()}
return _1}
,isc.A.getActiveXRequest=function isc_c_Comm_getActiveXRequest(){var _1;if(!this.$js){for(var i=0;i<this.xmlHttpConstructors.length;i++){try{var _3=this.xmlHttpConstructors[i];_1=new ActiveXObject(_3);if(_1){this.$js=_3;break}}catch(e){}}}else{_1=new ActiveXObject(this.$js)}
if(_1)isc.rpc.logDebug("Using ActiveX XMLHttpRequest via constructor: "+this.$js);return _1}
,isc.A.sendScriptInclude=function isc_c_Comm_sendScriptInclude(_1){var _2=_1.URL,_3=_1.fields,_4=_1.data,_5=_1.callbackParam,_6=_1.transaction;var _7="_scriptIncludeReply_"+_6.transactionNum;this[_7]=function(){var _8=arguments.length==1?arguments[0]:[];if(arguments.length>1){for(var i=0;i<arguments.length;i++)_8[i]=arguments[i]}
isc.Comm.performScriptIncludeReply(_6.transactionNum,_8)}
var _10="isc.Comm."+_7;var _11={};_11[_5?_5:"callback"]=_10;_2=isc.rpc.addParamsToURL(_2,_3);_2=isc.rpc.addParamsToURL(_2,_11);if(_6)_6.mergedActionURL=_2;isc.rpc.logInfo("scriptInclude call to: "+_2);this.$jn[_6.transactionNum]=_6.callback;var _12=this.getDocument(),_13=this.getDocumentBody(),_14=_12.createElement("script");_14.src=_2;_13.appendChild(_14)}
,isc.A.performScriptIncludeReply=function isc_c_Comm_performScriptIncludeReply(_1,_2){delete this["_scriptIncludeReply_"+_1];var _3=this.$jn[_1];delete this.$jn[_1];this.logDebug("scriptInclude reply for transactionNum: "+_1+", data: "+this.echoLeaf(_2),"xmlBinding");this.fireCallback(_3,"transactionNum,results,wd",[_1,_2])}
,isc.A.sendXmlHttpRequest=function isc_c_Comm_sendXmlHttpRequest(_1){var _2=_1.URL,_3=_1.fields,_4=_1.httpMethod,_5=_1.contentType,_6=_1.httpHeaders,_7=_1.data,_8=_1.transaction,_9=_1.blocking!=null?_1.blocking:false;this.$jn[_8.transactionNum]=_8.callback;var _10="isc.Comm.performXmlTransactionReply("+_8.transactionNum+", xmlHttpRequest)";if(!_4)_4="POST";var _11=this.createXMLHttpRequest();var _12;if(isc.Browser.isIE){_12=this.$jr();_12.request=_11;_12.callback=_10}else{_12=function(){if(_11.readyState!=4)return;isc.Comm.$jo(_11,_10)}}
_11.onreadystatechange=_12;if(isc.rpc.logIsDebugEnabled()){this.lastXmlHttpRequest=_11}
if(_4=="POST"||_4=="PUT"){if(_7){_5=_5||"text/xml";_2=isc.rpc.addParamsToURL(_2,_3)}else{_5=_5||"application/x-www-form-urlencoded; charset=UTF-8";_7=isc.SB.create();var _13=true;for(var _14 in _3){if(!_13)_7.append("&");var _15=_3[_14];_7.append(isc.rpc.encodeParameter(_14,_15));_13=false}
_7=_7.toString()}
if(isc.rpc.logIsDebugEnabled()){isc.rpc.logDebug("XMLHttpRequest POST to "+_2+" contentType: "+_5+" with body -->"+decodeURIComponent(_7)+"<--")}
_11.open(_4,_2,!_9);_11.setRequestHeader("Content-Type",_5);this.$jt(_11,_6);if(_8){_8.xhrHeaders=_6;_8.xhrData=_7}
if(_7!=null&&!isc.isA.String(_7)){this.logWarn("Non-string data object passed to sendXML as request.data:"+this.echo(_7)+" attempting to convert to a string.");_7=_7.toString?_7.toString():""+_7}
_11.send(_7)}else{var _16=isc.rpc.addParamsToURL(_2,_3);_11.open(_4,_16,!_9);if(_1.bypassCache){_11.setRequestHeader("If-Modified-Since","Thu, 01 Jan 1970 00:00:00 GMT")}
this.$jt(_11,_6);if(isc.rpc.logIsDebugEnabled()){isc.rpc.logDebug("XMLHttpRequest GET from "+_2+" with fields: "+isc.Log.echoAll(_3)+" full URL string: "+_16)}
_11.send(null)}
return _11}
,isc.A.performXmlTransactionReply=function isc_c_Comm_performXmlTransactionReply(_1,_2){var _3=this.$jn[_1]
delete this.$jn[_1];this.fireCallback(_3,"transactionNum,results,wd",[_1,_2])}
,isc.A.$jt=function isc_c_Comm__setHttpHeaders(_1,_2){if(_2==null)return;for(var _3 in _2){var _4=_2[_3];if(_4!=null)_1.setRequestHeader(_3,_4)}}
);isc.B._maxIndex=isc.C+10;isc.ClassFactory.defineClass("Timer");isc.A=isc.Timer;isc.A.$ju=null;isc.A.listEvent={action:null,iterationInterval:null,iterationsRemaining:0,$jv:null,$jw:null};isc.A.MSEC=1;isc.A.SEC=1000;isc.A.MIN=60000;isc.A.HOUR=3600000;isc.A.DEFAULT_TIMEOUT_LENGTH=100;isc.A.$jx=null;isc.A=isc.Timer;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$jy=["isc.Timer.$jz('",null,"')"];isc.A.$j0=0;isc.A.$j1={};isc.A.$j2="TMR";isc.A.$j3=5000;isc.B.push(isc.A.setTimeout=function isc_c_Timer_setTimeout(_1,_2,_3,_4){if(_1==null)return;if(_1.action!=null){_2=_1.delay;_3=_1.units;_1=_1.action}
if(_3==null)_3=isc.Timer.MSEC;if(_2==null)_2=isc.Timer.DEFAULT_TIMEOUT_LENGTH;_2=_2*_3;var _5="$j4"+this.$j0++;this.$jy[1]=_5;this[_5]=_1;if(this.logIsDebugEnabled("traceTimers"))
{_1.timerTrace=this.getStackTrace(null,1,null,true)}
var _6=this.$jy.join(isc.emptyString);var _7=setTimeout(_6,_2);this.$j1[_7]=_5;return _7}
,isc.A.$jz=function isc_c_Timer__fireTimeout(_1){if(isc.$c9!=null){if(this.logIsInfoEnabled()){this.logInfo("timer ID:"+_1+" fired during eval. Delaying until this "+"thread completes")}
if(!this.$j5)this.$j5=isc.timeStamp();if((isc.timeStamp()-this.$j5)>this.$j3){this.logWarn("timer ID:"+_1+" fired during eval thread lasting more than "+this.$j3+"ms. Thread may have caused an "+"error and failed to complete. Allowing delayed action to fire.");delete isc.$c9}else{this.$jy[1]=_1;var _2=this.$jy.join(isc.emptyString);var _3=setTimeout(_2,0);if(!this.$j6)this.$j6={};this.$j6[_1]=_3;return}}
delete this.$j5;var _4=this[_1];delete this[_1];var _5=this.$j1;for(var i in _5){if(_5[i]=_1){delete _5[i];break}}
var _7=this.$j6;if(_7){for(var i in _7){if(_7[i]=_1){delete _7[i];break}}}
if(_4==null)return;isc.EH.$jp(this.$j2);arguments.timerTrace=_4.timerTrace;this.fireCallback(_4,null,null,null,true);isc.EH.$jq()}
,isc.A.clear=function isc_c_Timer_clear(_1){if(isc.isAn.Array(_1))
for(var i=0;i<_1.length;i++)this.clear(_1[i]);else{var _3=this.$j1[_1];delete this[_3]
delete this.$j1[_1];if(this.$j6&&this.$j6[_3]){_1=this.$j6[_3];delete this.$j6[_3]}
clearTimeout(_1)}
return null}
,isc.A.clearTimeout=function isc_c_Timer_clearTimeout(_1){return this.clear(_1)}
);isc.B._maxIndex=isc.C+4;isc.A=isc.Page;isc.A.$j7={};isc.A.$j8=0;isc.A.FIRE_ONCE="once";isc.A.$j9={};isc.A=isc.Page;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$c3="ID";isc.B.push(isc.A.setEvent=function isc_c_Page_setEvent(_1,_2,_3,_4){if(isc.isA.String(_2)){if(_1==isc.EH.LOAD||_1==isc.EH.IDLE||_1==isc.EH.RESIZE||_1==isc.EH.ORIENTATION_CHANGE)
{_2=new Function("target,eventInfo",_2)}else{_2=isc.Func.expressionToFunction("target,eventInfo",_2)}}
if(this.logIsDebugEnabled()){this.logDebug("setEvent("+_1+"): action => "+(isc.isA.Function(_2)?isc.Func.getShortBody(_2):_2))}
var _5=isc.Page.$j8++,_6={action:_2,functionName:_4,fireStyle:_3,ID:_5};var _7=this.$j7;if(!isc.isAn.Array(_7[_1]))_7[_1]=[];_7[_1].add(_6);if(_1==isc.EH.IDLE){isc.EventHandler.startIdleTimer()}
return _5}
,isc.A.clearEvent=function isc_c_Page_clearEvent(_1,_2){if(_2==null){this.$j7[_1]=[]}else{if(this.$ka==_1){var _3=this.$j7[_1],_4=isc.isA.Array(_3)?_3.findIndex(this.$c3,_2):-1;if(_4!=-1)_3[_4]=null}else{if(isc.isA.Array(this.$j7[_1]))
this.$j7[_1].removeWhere(this.$c3,_2)}}}
,isc.A.$kb=function isc_c_Page__getPageEventName(_1){var _2=this.$kc=this.$kc||{};if(!_2[_1]){_2[_1]="page"+_1.charAt(0).toUpperCase()+_1.substring(1)}
return _2[_1]}
,isc.A.handleEvent=function isc_c_Page_handleEvent(_1,_2,_3){if(_2==isc.EH.UNLOAD)isc.Canvas.$kd();var _4=isc.Page.$j7[_2];if(!isc.isAn.Array(_4)||_4.length==0)return true;var _5=this.$kb(_2);var _6=true;this.$ka=_2;for(var i=0,_8=_4.length;_6&&(i<_8);i++){var _9=_4[i];if(!_9)continue;if(_9.fireStyle==isc.Page.FIRE_ONCE)_4[i]=null;if(this.logIsDebugEnabled()){this.logDebug("handleEvent("+_2+"): firing action => "+isc.Func.getShortBody(_9.action))}
if(isc.isA.Function(_9.action)){_6=(_9.action(_1,_3)!=false)}else{var _10=_9.action;if(!_10||_10.destroyed){_4[i]=null;continue}
var _11=_9.functionName||_5;if(isc.isA.Function(_10[_11])){_6=(_10[_11](_1,_3)!=false)}}}
this.$ka=null;this.$j7[_2].removeEmpty();return _6}
,isc.A.actionsArePendingForEvent=function isc_c_Page_actionsArePendingForEvent(_1){return(isc.isAn.Array(this.$j7[_1])&&this.$j7[_1].length!=0)}
,isc.A.registerKey=function isc_c_Page_registerKey(_1,_2,_3){if(_1==null||_2==null)return;var _4=_1,_5,_6,_7,_8;if(isc.isAn.Object(_1)){_4=_1.keyName;_5=_1.ctrlKey;_6=_1.shiftKey;_7=_1.altKey;_8=_1.metaKey}
if(_4.length==1)_4=_4.toUpperCase();var _9=false;for(var i in isc.EH.$ke){if(isc.EH.$ke[i]==_4){_9=true;break}}
if(!_9){this.logWarn("Page.registerKey() passed unrecognized key name '"+_1+"'. Not registering","events");return}
var _11=this.$j9;if(!_11[_4])_11[_4]=[];_11[_4].add({target:_3,action:_2,ctrlKey:_5,shiftKey:_6,altKey:_7,metaKey:_8})}
,isc.A.unregisterKey=function isc_c_Page_unregisterKey(_1,_2){if(!this.$j9[_1]){isc.Log.logInfo("Page.unregisterKey(): No events registered for key "+isc.Log.echo(_1)+".","events");return false}
this.$j9[_1].removeWhere("target",_2)}
,isc.A.handleKeyPress=function isc_c_Page_handleKeyPress(){var _1=isc.EH,_2=_1.getKey(),_3=this.$j9;if(!_3[_2])return true;var _4=_3[_2],_5=_4.duplicate(),_6=_5.length,_7=true;for(var i=0;i<_6;i++){var _9=_5[i];if(!_4.contains(_9))continue;if(_9.ctrlKey!=null&&_9.ctrlKey!=_1.ctrlKeyDown())continue;if(_9.altKey!=null&&_9.altKey!=_1.altKeyDown())continue;if(_9.shiftKey!=null&&_9.shiftKey!=_1.shiftKeyDown())continue;if(_9.metaKey!=null&&_9.metaKey!=_1.metaKeyDown())continue;if(_9.action!=null&&!isc.isA.Function(_9.action)){isc.Func.replaceWithMethod(_9,"action","key,target")}
_7=((_9.action(_2,_9.target)!=false)&&_7)}
return _7}
);isc.B._maxIndex=isc.C+8;isc.ClassFactory.defineClass("EventHandler");isc.EH=isc.Event=isc.EventHandler;isc.A=isc.EventHandler;isc.A.lastEvent={};isc.A.$kf=[];isc.A.$kg=[];isc.A.passThroughEvents=true;isc.A.maskNativeTargets=true;isc.A.STILL_DOWN_DELAY=100;isc.A.DOUBLE_CLICK_DELAY=500;isc.A.IDLE_DELAY=10;isc.A.STOP_BUBBLING="***STOP***";isc.A.ALL_EDGES=["T","L","B","R","TL","TR","BL","BR"];isc.A.eventTypes={MOUSE_DOWN:"mouseDown",RIGHT_MOUSE_DOWN:"rightMouseDown",MOUSE_MOVE:"mouseMove",MOUSE_UP:"mouseUp",SHOW_CONTEXT_MENU:"showContextMenu",CLICK:"click",DOUBLE_CLICK:"doubleClick",MOUSE_OUT:"mouseOut",MOUSE_STILL_DOWN:"mouseStillDown",MOUSE_OVER:"mouseOver",TOUCH_START:"touchStart",TOUCH_MOVE:"touchMove",TOUCH_END:"touchEnd",TOUCH_CANCEL:"touchCancel",LONG_TOUCH:"longTouch",SET_DRAG_TRACKER:"setDragTracker",GET_DRAG_DATA:"getDragData",RELEASE_DRAG_DATA:"releaseDragData",DRAG_START:"dragStart",DRAG_STOP:"dragStop",DRAG_MOVE:"dragMove",DRAG_OUT:"dragOut",DRAG_REPOSITION_START:"dragRepositionStart",DRAG_REPOSITION_MOVE:"dragRepositionMove",DRAG_REPOSITION_STOP:"dragRepositionStop",DRAG_RESIZE_START:"dragResizeStart",DRAG_RESIZE_MOVE:"dragResizeMove",DRAG_RESIZE_STOP:"dragResizeStop",DROP_OVER:"dropOver",DROP_MOVE:"dropMove",DROP_OUT:"dropOut",DROP:"drop",KEY_DOWN:"keyDown",KEY_UP:"keyUp",KEY_PRESS:"keyPress",MOUSE_WHEEL:"mouseWheel",SELECT_START:"selectStart",SELECTION_CHANGE:"selectionChange",FOCUS_IN:"focusIn",FOCUS_OUT:"focusOut",IDLE:"idle",LOAD:"load",UNLOAD:"unload",RESIZE:"resize",ORIENTATION_CHANGE:"orientationChange"};isc.A.$kh={mousemove:"mouseMove",mousedown:"mouseDown",mouseup:"mouseUp",contextmenu:"contextMenu",mousewheel:"mouseWheel",selectionchange:"selectionChange",DOMMouseScroll:"mouseWheel",mouseMove:"mouseMove",mouseDown:"mouseDown",mouseUp:"mouseUp",mouseWheel:"mouseWheel",touchstart:"touchStart",touchmove:"touchMove",touchend:"touchEnd",touchStart:"touchStart",touchMove:"touchMove",touchEnd:"touchEnd",selectionstart:"selectionStart",selectionStart:"selectionStart",selectionchange:"selectionChange",selectionChange:"selectionChange"};isc.A.$ki="event,eventInfo";isc.A.DRAG_RESIZE="dragResize";isc.A.DRAG_REPOSITION="dragReposition";isc.A.DRAG_SCROLL="dragScroll";isc.A.DRAG_SELECT="dragSelect";isc.A.DRAG="drag";isc.A.NONE="none";isc.A.TRACKER="tracker";isc.A.TARGET="target";isc.A.OUTLINE="outline";isc.A.INTERSECT_WITH_MOUSE="mouse";isc.A.INTERSECT_WITH_RECT="rect";isc.A.dragTargetShadowDepth=10;isc.A.$kj={A:true,AREA:true};isc.A.$kk={INPUT:true,TEXTAREA:true,SELECT:true,OPTION:true};isc.A.$kl="LABEL";isc.A.$km={keydown:"keyDown",keyup:"keyUp",keypress:"keyPress",contextmenu:"contextMenu"};isc.A.$kn={Backspace:8,Tab:9,Shift:16,Ctrl:17,Alt:18,Pause_Break:19,Caps_Lock:20,Page_Up:33,Page_Down:34,End:35,Home:36,Arrow_Left:37,Arrow_Up:38,Arrow_Right:39,Arrow_Down:40,Insert:45,Delete:46,Meta:91,f1:112,f2:113,f3:114,f4:115,f5:116,f6:117,f7:118,f8:119,f9:120,f10:121,f11:122,f12:123,Num_Lock:144,Scroll_Lock:145};isc.A.$ke={'0':'$ko','8':'Backspace','9':'Tab','13':'Enter','16':'Shift','17':'Ctrl','18':'Alt','19':'Pause_Break','20':'Caps_Lock','27':'Escape','32':'Space','33':'Page_Up','34':'Page_Down','35':'End','36':'Home','37':'Arrow_Left','38':'Arrow_Up','39':'Arrow_Right','40':'Arrow_Down','44':'Print_Screen','45':'Insert','46':'Delete','48':'0',"49":"1","50":"2","51":"3","52":"4","53":"5","54":"6","55":"7","56":"8","57":"9",'58':';','59':';','60':',','61':'=','62':"/",'65':'A','66':'B','67':'C','68':'D','69':'E','70':'F','71':'G','72':'H','73':'I','74':'J','75':'K','76':'L','77':'M','78':'N','79':'O','80':'P','81':'Q','82':'R','83':'S','84':'T','85':'U','86':'V','87':'W','88':'X','89':'Y','90':'Z','91':'Meta','92':'Meta','93':'Menu','96':'0','97':'1','98':'2','99':'3','100':'4','101':'5','102':'6','103':'7','104':'8','105':'9','106':'*','107':'+','109':'-','110':'.','111':'/','112':'f1','113':'f2','114':'f3','115':'f4','116':'f5','117':'f6','118':'f7','119':'f8','120':'f9','121':'f10','122':'f11','123':'f12','144':'Num_Lock','145':'Scroll_Lock','160':'Shift','161':'Shift','162':'Ctrl','163':'Ctrl','164':'Alt','165':'Alt','186':';','187':'=','188':',','189':'-','190':'.','191':'/','192':'`','219':'[','220':'\\','221':']','222':"'"};isc.A.$kp={'8':'Backspace','9':'Tab','13':'Enter','27':'Escape','32':'Space','33':'1','34':"'",'35':'3','36':'4','37':'5','38':'7','39':"'",'40':'9','41':'0','42':'8','43':'=','44':',','45':'-','46':'.','47':'/','48':'0','49':'1','50':'2','51':'3','52':'4','53':'5','54':'6','55':'7','56':'8','57':'9','58':';','59':';','60':',','61':'=','62':'.','63':'/','64':'2','65':'A','66':'B','67':'C','68':'D','69':'E','70':'F','71':'G','72':'H','73':'I','74':'J','75':'K','76':'L','77':'M','78':'N','79':'O','80':'P','81':'Q','82':'R','83':'S','84':'T','85':'U','86':'V','87':'W','88':'X','89':'Y','90':'Z','91':'[','92':'\\','93':']','94':'6','95':'-','96':'`','97':'A','98':'B','99':'C','100':'D','101':'E','102':'F','103':'G','104':'H','105':'I','106':'J','107':'K','108':'L','109':'M','110':'N','111':'O','112':'P','113':'Q','114':'R','115':'S','116':'T','117':'U','118':'V','119':'W','120':'X','121':'Y','122':'Z','123':'[','124':'\\','125':']','126':'`'};isc.A.$kq={'3':"Enter",'25':"Tab",'63232':"Arrow_Up",'63233':"Arrow_Down",'63234':"Arrow_Left",'63235':"Arrow_Right",'64236':"f1",'64237':"f2",'64238':"f3",'64239':"f4",'64240':"f5",'64241':"f6",'64242':"f7",'64243':"f8",'64244':"f9",'64245':"f10",'64246':"f11",'63247':"f12",'63273':"Home",'63275':"End",'63276':"Page_Up",'63277':"Page_Down"};isc.A.$kr={};isc.A.dynamicBackMask=false;isc.A.alwaysBackMask=false;isc.A.dragTrackerDefaults={ID:"isc_dragTracker",width:10,height:10,offsetX:-10,offsetY:-10,autoDraw:false,visibility:"hidden",overflow:"visible",cursor:"arrow"};isc.EventHandler.addClassProperties(isc.EventHandler.eventTypes)
isc.A=isc.EventHandler;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$ks="f10";isc.A.$kt="Escape";isc.A.$ku=[];isc.A.$kv={};isc.A.$kw="Tab";isc.A.$kx={keydown:true,keyup:true,keypress:true};isc.A.$ky="IMG";isc.A.$kz="progid:DXImageTransform.Microsoft.AlphaImageLoader";isc.A.longTouchDelay=500;isc.A.$k0={};isc.A.$k1="BODY";isc.A.$k2="HTML";isc.A.$k3="eventProxy";isc.A.$k4="[object Text]";isc.A.$k5={autoDraw:false,$k6:true,$k7:false,overflow:"hidden",visibility:"hidden",$k8:false,getTarget:function(){return this.$k9},show:function(){var _1=this.masterElement;this.moveAbove(_1);return this.Super("show",arguments)}};isc.A.$la=isc.Browser.isIE?isc.undef:true;isc.A.$lb="handleNativeEvents";isc.A.$lc="APPLET";isc.A.$ld={mouseMove:true,mouseOver:true,mouseOut:true};isc.A.$le="selectionChange";isc.A.currentOrientation=isc.Page.getOrientation();isc.A.$lf="$lg";isc.A.$lh=0;isc.A.$li={load:"LOD",mousedown:"MDN",mouseup:"MUP",mousemove:"MMV",mouseout:"MOU",touchstart:"TDN",touchmove:"TMVP",touchend:"TUP",contextmenu:"CXT",keypress:"KPR",keydown:"KDN",keyup:"KUP",resize:"RSZ"};isc.A.$lj="nativeEvents";isc.A.$lk="event";isc.A.$ll="if (!isc.Browser.isIE && event == null) return;"+(isc.Browser.isMoz?"if(event.getPreventDefault&&event.getPreventDefault())return;":isc.Browser.isSafari?"if(event.returnValue==false)return;":"")+"var returnVal=arguments.callee.$cv.isc.EH.dispatch(arguments.callee.$lm,event);"+(!isc.Browser.isIE&&isc.Browser.isDOM?"if(returnVal==false)event.preventDefault();else if(returnVal==isc.EH.STOP_BUBBLING)event.stopPropogation();":"")+"return returnVal;";isc.A.$ln={};isc.A.$lo={onmousedown:"mousedown",onmouseup:"mouseup",onclick:"click",ondblclick:"dblclick",oncontextmenu:"contextmenu",onmousewheel:"mousewheel",onmouseover:"mouseover",onmouseout:"mouseout",onmousemove:"mousemove",onresize:"resize",onload:"load",onunload:"unload",onselecttext:"selecttext",onselectionchanged:"selectionchanged",onkeydown:"keydown",onkeyup:"keyup",onkeypress:"keypress"};isc.A.$lp={};isc.A.getMouseEventProperties=(isc.Browser.isIE?function(_1){var _2=this.lastEvent;if(!_1)_1=this.getWindow().event;_2.DOMevent=_1;_2.eventType=this.$kh[_1.type];_2.y=parseInt(_1.clientY)+this.ns.Page.getScrollTop();_2.x=parseInt(_1.clientX);if(!isc.Page.isRTL()){_2.x+=this.ns.Page.getScrollLeft()}else{var _3=this.ns.Page.getScrollLeft();if(_3>0){var _4=(this.ns.Page.getScrollWidth()-this.ns.Page.getWidth());_2.x-=(_4-_3)}
_2.x-=15}
_2.nativeTarget=_1.srcElement;var _5=_1.wheelDelta;if(_5!=null)_2.wheelDelta=-Math.round(_5/ 120);else _2.wheelDelta=null;_2.screenX=_1.screenX;_2.screenY=_1.screenY;_2.buttonNum=_1.button;_2.shiftKey=(_1.shiftKey==true);_2.ctrlKey=(_1.ctrlKey==true);_2.altKey=(_1.altKey==true);_2.metaKey=(_1.metaKey==true);_2.target=this.getEventTargetCanvas(_1,_2.nativeTarget,_2);return _2}:function(_1){var _2=this.lastEvent;_2.DOMevent=_1;_2.eventType=this.$kh[_1.type];var _3=false;if(isc.Browser.isMobileWebkit){if(isc.startsWith(_2.eventType,"touch")){if(_2.eventType==isc.EH.TOUCH_END){_3=true}else if(_1.touches!=null&&_1.touches[0]!=null){var _4=_1.touches[0];_2.clientX=_4.clientX;_2.clientY=_4.clientY;_2.screenX=_4.screenX;_2.screenY=_4.screenY;_2.x=_4.pageX;_2.y=_4.pageY}}}else{_2.screenX=_1.screenX;_2.screenY=_1.screenY;if(isc.Browser.isSafari){var _5=isc.Browser.safariVersion>=523.12
_2.x=parseInt(_1.x);_2.y=parseInt(_1.y);if(_5){_2.x+=isc.Page.getScrollLeft();_2.y+=isc.Page.getScrollTop()}
if(_2.eventType!=this.MOUSE_WHEEL){var _5=true;_2.x=parseInt(_1.clientX)+(_5?isc.Page.getScrollLeft():0);_2.y=parseInt(_1.clientY)+(_5?isc.Page.getScrollTop():0)}}else{if(_2.eventType!=this.MOUSE_WHEEL){var _5=true;_2.x=parseInt(_1.clientX)+(_5?isc.Page.getScrollLeft():0);_2.y=parseInt(_1.clientY)+(_5?isc.Page.getScrollTop():0)}}}
_2.nativeTarget=_1.target;_2.$lq=null;_2.target=this.getEventTargetCanvas(_1,_2.nativeTarget,_2);if(_2.eventType==this.MOUSE_WHEEL){var _6=_1.wheelDelta,_7=_1.detail;if(_7==0||_7==null&&_6!=null){_2.wheelDelta=-Math.round(_6/ 120);if(!isc.isA.Number(_2.wheelDelta))_2.wheelDelta=null}else{if(isc.Canvas.useNativeWheelDelta&&_1.detail==_1.SCROLL_PAGE_UP){_2.wheelDelta=-Math.floor(_2.target.height/ isc.Canvas.scrollWheelDelta)}else if(isc.Canvas.useNativeWheelDelta&&_1.detail==_1.SCROLL_PAGE_DOWN){_2.wheelDelta=Math.floor(_2.target.height/ isc.Canvas.scrollWheelDelta)}else{var _8=_1.detail/ 3;if(!isc.isA.Number(_8))_8=0;if(_8>15||_8<-15)_8=(_8>0?1:-1);_2.wheelDelta=_8}}}else{_2.wheelDelta=null}
if(_2.eventType==isc.EH.MOUSE_MOVE||_2.eventType==isc.EH.TOUCH_MOVE){if(!this.$lr)_2.buttonNum=0}else if(isc.Browser.isTouch){if(_1.targetTouches&&_1.targetTouches.length>1){_2.buttonNum=2}else{_2.buttonNum=1}}else{_2.buttonNum=((_1.which==1||isc.Browser.isSafari&&_1.which==65536)?1:2)}
_2.shiftKey=(_1.shiftKey==true);_2.ctrlKey=(_1.ctrlKey==true);_2.altKey=(_1.altKey==true);_2.metaKey=(_1.metaKey==true);return _2});isc.A.$ls="f1";isc.A.$lt="help";isc.A.HARD="hard";isc.A.SOFT="soft";isc.A.SOFT_CANCEL="softCancel";isc.A.$lu=0;isc.A.clickMaskRegistry=[];isc.A.$c3='ID';isc.B.push(isc.A.handleSyntheticEvent=function isc_c_EventHandler_handleSyntheticEvent(_1){var _2=_1.target;_1.$lv=true;if(_2){_1.clientX+=_2.getPageLeft();_1.clientY+=_2.getPageTop();if(isc.Browser.isIE){_1.clientX+=_2.getLeftMargin()+_2.getLeftBorderSize()+_2.getLeftPadding()+2;_1.clientY+=_2.getTopMargin()+_2.getRightBorderSize()+_2.getTopPadding()+2}
switch(_1.type){case"mouseup":this.handleMouseUp(_1);break;case"mousedown":this.handleMouseDown(_1);break;case"mousemove":this.handleMouseMove(_1);break}}}
,isc.A.handleEvent=function isc_c_EventHandler_handleEvent(_1,_2,_3){this.$lw=_2;var _4=isc.EH;var _5;if(isc.Page.handleEvent(_1,_2,_3)==false){_5=false}else if(_4.targetIsEnabled(_1)&&_4.bubbleEvent(_1,_2,_3)==false){_5=false}else{_5=true}
delete this.$lw;return _5}
,isc.A.handleLoad=function isc_c_EventHandler_handleLoad(_1){if(isc.SA_Page)isc.SA_Page.$r();if(!isc.Browser.isMoz){if(isc.EH.$lx()&&document&&document.body){document.body.addEventListener("unload",isc.EH.handleUnload,false)}
return(isc.Page.handleEvent(null,isc.EH.LOAD)!=false)}else{try{return(isc.Page.handleEvent(null,isc.EH.LOAD)!=false)}catch(e){isc.Log.$a3(e);throw e;}}}
,isc.A.handleUnload=function isc_c_EventHandler_handleUnload(_1){var _2=isc.EH;var _3=(isc.Page.handleEvent(null,_2.UNLOAD)!=false);if(_3==true){this.releaseEvents()}
return _3}
,isc.A.$ly=function isc_c_EventHandler__handleNativeKeyDown(_1,_2){if(!isc.Page.isLoaded())return false;var _3=isc.EH;var _4=_3.lastEvent;if(!_1)_1=_3.getWindow().event;_3.getKeyEventProperties(_1);if(isc.Browser.isIE&&_4.keyName==this.$ls&&!_2){return}
var _5=true;var _6=_4.keyName,_7=_4.characterValue;var _8=_3.$ku.duplicate();for(var i=0;i<_8.length;i++){var _10=_8[i];if(_10==null||_10==_6)break;_4.characterValue=null;_4.keyName=_10;this.handleKeyPress();_3.$kv[_4.keyName]=true}
_4.keyName=_6;_4.characterValue=_7;if(_3.$ku.indexOf(_4.keyName)!=-1){_5=_3.handleKeyPress();_3.$kv[_4.keyName]=true}else{_5=_3.handleKeyDown(_1)}
_3.$ku[_3.$ku.length]=_4.keyName;_3.$lz=_4.ctrlKey;_3.$l0=_4.altKey;if(_5!=false&&((isc.Browser.isIE&&_3.$kv[_4.keyName]!=true&&_3.$kn[_4.keyName]!=null)||(isc.Browser.isMoz&&_4.keyName==this.$ks&&this.shiftKeyDown())))
{_5=_3.handleKeyPress(_1);_3.$kv[_4.keyName]=true}
if(_5==false){this.cancelKeyEvent(_1)}
return _5}
,isc.A.handleKeyDown=function isc_c_EventHandler_handleKeyDown(_1,_2){var _3=isc.EH,_4=_3.lastEvent,_5;var _6=_3.eventHandledNatively(_4.eventType,_4.nativeKeyTarget);if(_6)_5=_3.$la;if(_2!=null)isc.addProperties(_4,_2);if(!_6){var _7=[_4,_4.target,_4.keyName];var _8=_4.keyTarget;if(_8==null)_8=this.getEventTargetCanvas(_1,_4.nativeKeyTarget);if(_3.targetIsEnabled(_8))
_5=(_3.bubbleEvent(_8,_3.KEY_DOWN,_7)!=false)}
if(_5!=false&&this.clickMaskUp()&&_4.keyName==this.$kw){var _9,_10=this.clickMaskRegistry;for(var i=_10.length-1;i>=0;i--){if(this.isHardMask(_10[i])){_9=_10[i];break}}
if(_9)_5=false}
return _5}
,isc.A.$l1=function isc_c_EventHandler__handleNativeKeyUp(_1){if(!isc.Page.isLoaded())return false;var _2=isc.EH,_3=_2.lastEvent;if(!_1)_1=_2.getWindow().event;_2.getKeyEventProperties(_1);_2.$lz=_3.ctrlKey
_2.$l0=_3.altKey
_2.$kv[_3.keyName]=null;if(_2.$ku.indexOf(_2.lastEvent.keyName)!=-1){if(_2.handleKeyPress(_1)==false){this.cancelKeyEvent(_1);return false}}
var _4=_2.handleKeyUp(_1)
return _4}
,isc.A.handleKeyUp=function isc_c_EventHandler_handleKeyUp(_1,_2){var _3=isc.EH,_4=_3.lastEvent,_5=[_4,_4.target,_4.keyName];if(_3.eventHandledNatively(_4.eventType,_4.nativeKeyTarget)){return _3.$la}
var _6=true;if(_2!=null){isc.addProperties(_4,_2)}
var _7=_4.keyTarget;if(_7==null)_7=this.getEventTargetCanvas(_1,_4.nativeKeyTarget);if(_3.targetIsEnabled(_7))
_6=(_3.bubbleEvent(_7,_3.KEY_UP,_5)!=false);if(!isc.Browser.isMac&&_4.keyName==_3.$ks&&_3.shiftKeyDown()&&isc.Menu&&isc.Menu.$l2&&isc.Menu.$l2.length>0)
{_6=false}
_3.clearKeyEventProperties(_4.keyName);return _6}
,isc.A.$l3=function isc_c_EventHandler__handleNativeKeyPress(_1){if(!isc.Page.isLoaded())return false;var _2=isc.EH;var _3=_2.lastEvent,_4=_2.KEY_PRESS;if(!_1)_1=_2.getWindow().event;_2.getKeyEventProperties(_1);_3.eventType=_4;_3.ctrlKey=_2.$lz;_3.altKey=_2.$l0;if(_2.$kv[_3.keyName]==true){_2.$kv[_3.keyName]=null;return}
var _5=_2.handleKeyPress(_1);if(_5==false){this.cancelKeyEvent(_1)}
return _5}
,isc.A.cancelKeyEvent=function isc_c_EventHandler_cancelKeyEvent(_1){if(isc.Browser.isIE||isc.Browser.isSafari){if(this.$kx[_1.type]==true){try{_1.keyCode=0}catch(e){}}}}
,isc.A.handleKeyPress=function isc_c_EventHandler_handleKeyPress(_1,_2){var _3=isc.EH,_4=_3.lastEvent,_5=_3.KEY_PRESS;if(_2!=null){isc.addProperties(_4,_2)}
var _6={keyName:_4.keyName,characterValue:_4.characterValue};_4.eventType=_5;_3.$ku.removeAt(0);if(isc.Page.handleEvent(_4.keyTarget,_5)==false)return false;var _7=(_3.eventHandledNatively(_5,_4.nativeKeyTarget));if(_7!==false){_3.logDebug("keyPress handled natively");return _3.$la}else{_3.logDebug("keyPress not handled natively")}
var _8=_4.keyTarget;if(_8==null)_8=this.getEventTargetCanvas(_1,_4.nativeKeyTarget);if(_3.targetIsEnabled(_8)){var _9=_3.bubbleEvent(_8,_4.eventType,_6)
if(_9==false)return false}
if(_9!=_3.STOP_BUBBLING&&isc.Page.handleKeyPress()==false)return false;if(this.clickMaskUp()&&_4.keyName==this.$kw){var _10,_11=this.clickMaskRegistry;for(var i=_11.length-1;i>=0;i--){if(this.isHardMask(_11[i])){_10=_11[i];break}}
if(_10!=null){var _13=_3.$l4;if(_13!=null){this.logInfo("Telling focus canvas:"+_13+" to shift focus","syntheticTabIndex")
_13.$l5(!this.shiftKeyDown(),_10)}else{if(this.shiftKeyDown()){this.logInfo("Putting focus into last widget in response to Tab keydown","syntheticTabIndex")
this.$l6(_10)}else{this.logInfo("Putting focus into first widget in response to Tab keydown","syntheticTabIndex")
this.$l7(_10)}}
return false}}
if((isc.Browser.isIE||isc.Browser.isMoz)&&_4.keyName==isc.EH.$ks&&isc.EH.shiftKeyDown()){var _14=this.handleContextMenu(_1);if(_14){this.$l8=true}
return _14}
if(isc.Browser.isMoz&&isc.RPCManager&&isc.RPCManager.$l9.length>0&&_4.keyName==isc.EH.$kt)
{return false}
return true}
,isc.A.$l7=function isc_c_EventHandler__focusInFirstWidget(_1){var _2=this.$ma;if(_2){if((!_1||!this.targetIsMasked(_2,_1))&&!_2.isDisabled()&&_2.$mb())
{_2.focusAtEnd(true)}else{_2.$l5(true,_1)}}}
,isc.A.$l6=function isc_c_EventHandler__focusInLastWidget(_1){var _2=this.$mc;if(_2){if((!_1||!this.targetIsMasked(_2,_1))&&!_2.isDisabled()&&_2.$mb())
{_2.focusAtEnd()}else{_2.$l5(false,_1)}}}
,isc.A.handleMouseDown=function isc_c_EventHandler_handleMouseDown(_1,_2){if(isc.Browser.isTouch&&!_2)return;var _3=isc.EH;_3.$md=true;var _4=_3.doHandleMouseDown(_1,_2);_3.$md=false;return _4}
,isc.A.doHandleMouseDown=function isc_c_EventHandler_doHandleMouseDown(_1,_2){if(!isc.Page.isLoaded())return false;var _3=this;_3.$lr=true;var _4=_2||_3.getMouseEventProperties(_1);var _5=_3.$l4,_6=_5!=null&&(_5!=_4.target)&&!_5._useNativeTabIndex&&!_5._useFocusProxy&&!(isc.isA.DynamicForm!=null&&isc.isA.DynamicForm(_5)&&_5.getFocusSubItem()&&_5.getFocusSubItem().hasFocus);if(_6){if(isc.Browser.isIE){var _7=_3.$l4.getID();if(_3.$me==null)
_3.$me=["if (",_7," && ",_7,".hasFocus)",_7,".blur()"]
else
_3.$me[1]=_3.$me[3]=_3.$me[5]=_7;isc.Timer.setTimeout(_3.$me.join(isc.emptyString),0)}else{_3.$l4.blur()}}
_3.mouseDownEvent=isc.addProperties({},_4);var _8=_4.target;var _9=(_3.clickMaskClick(_8)==false);if(_9){_3.$mf=true;return false}else{_3.$mf=false}
var _10=_3.rightButtonDown()?_3.RIGHT_MOUSE_DOWN:_3.MOUSE_DOWN;if(isc.Page.handleEvent(_8,_10)==false){return false}
if(_3.eventHandledNatively(_10,_4.nativeTarget))
return _3.$la;if(!_3.targetIsEnabled(_8))return false;var _11;if(_8&&!_8.hasFocus){if(((isc.Browser.isMoz&&_8.canSelectText)||isc.Browser.isSafari)&&_8._useFocusProxy)
{_3.focusInCanvas(_8)}else if(!_8._useNativeTabIndex){_8.focus()}else if(isc.Browser.isMoz||isc.Browser.isSafari){_8.focus()}else if(isc.Browser.isIE){var _12=_4.nativeTarget;if(_12&&_12.tagName==this.$ky){var _13=_12.style,_14=_13?_13.filter:null;if(_14.contains(this.$kz)){_11=_8}}}}
if(_8)_3.prepareForDragging(_8);var _15=_3.bubbleEvent(_8,_10,null,_9);if(_11!=null)_11.focus();if(_15==false){delete _3.dragTarget;delete _3.dragTargetLink}
if(_3.rightButtonDown()){if(!this.useSyntheticRightButtonEvents())return true;if(_8&&(_3.getBubbledProperty(_8,"contextMenu")||_3.getBubbledProperty(_8,"showContextMenu")!=isc.Canvas.getInstanceProperty("showContextMenu")))
{_4.returnValue=false;return false}
return true}
if(_15!=false){if(_3.hasEventHandler(_8,_3.MOUSE_STILL_DOWN)){_3.$mg()}}
var _16=_3.dragTarget!=null&&_3.dragOperation!=_3.DRAG_SELECT;var _17=(!_16&&(!(isc.Browser.isMoz||isc.Browser.isSafari)||!!_8.$mh(_4)));return _17}
,isc.A.stillWithinMouseDownTarget=function isc_c_EventHandler_stillWithinMouseDownTarget(){var _1=this.mouseDownTarget();if(!_1)return false;var _2=this.lastEvent;var _3=(_1==_2.target);if(!_3)return false;if(_2.$lq!=null)return _2.$lq;if(!(isc.Browser.isMoz&&_1.$mi))
{return _3}
var x=_2.x,y=_2.y,_6=_1.visibleAtPoint(x,y,true);if(!_6&&_1.$mj!=null){for(var i=0;i<_1.$mj.length;i++){_6=_1.$mj[i].visibleAtPoint(_2.x,_2.y,true);if(_6)break}}
_2.$lq=_6;return _6}
,isc.A.handleMouseMove=function isc_c_EventHandler_handleMouseMove(_1){if(isc.Browser.isTouch)return;if(!isc.Page.isLoaded())return false;var _2=isc.EH;if(_2.$md||_2.$mk)return;var _3=_2.getMouseEventProperties(_1);if((isc.Browser.isMoz||isc.Browser.isIE)&&!_2.immediateMouseMove){if(_2.delayedMouseMoveTimer==null){_2.delayedMouseMoveTimer=isc.Timer.setTimeout({target:_2,methodName:"$ml",args:[isc.timeStamp()]},0,true)}
_2.$mm=0;return true}
var _4=isc.timeStamp();var _5=_2.$mn(_1,_3);_2.$mm=isc.timeStamp()-_4;return _5}
,isc.A.$ml=function isc_c_EventHandler__delayedMouseMove(_1){this.delayedMouseMoveTimer=null;this.$mn(null,this.lastEvent)}
,isc.A.$mn=function isc_c_EventHandler__handleMouseMove(_1,_2){this.$mo=true;var _3=this.$mp(_1,_2);this.$mo=null;return _3}
,isc.A.$mp=function isc_c_EventHandler___handleMouseMove(_1,_2){var _3=this;var _4;if(isc.Browser.isIE){var _5=_3.$lr,_6=_2.buttonNum;if(_5){if(_6==0){_3.$lr=false;_4=true}}else if(_6==1&&_2.eventType==_3.MOUSE_MOVE){_2.eventType=_3.MOUSE_DOWN;_3.handleMouseDown(null,_2);_2.eventType=_3.MOUSE_MOVE}}
var _7=_2.target,_8=_3.eventHandledNatively(_3.MOUSE_MOVE,_2.nativeTarget);if(_3.$mq){_3.handleMouseUp(_1,true)}else if(_3.$mr&&_4){_3.logInfo("sythesizing mouseUp due to mouseUp outside window, buttonNum: "+_2.buttonNum);_3.handleMouseUp(_1,true)}
delete _3.$mr;var _9=_3.mouseIsDown();if(isc.Browser.isMoz&&(isc.Browser.geckoVersion<20100914)&&_9&&_2.target&&_2.target.$mi&&_2.target!=_3.mouseDownTarget())
{_2.nativeDraggingTarget=_2.nativeTarget;_2.nativeTarget=null;_7=_2.target=_3.mouseDownTarget()}
if(_9&&_3.dragTarget&&!_3.dragging&&(Math.abs(_2.x-_3.mouseDownEvent.x)>_3.dragTarget.dragStartDistance||Math.abs(_2.y-_3.mouseDownEvent.y)>_3.dragTarget.dragStartDistance))
{_3.handleDragStart(_2)}
if(_3.dragging){return _3.handleDragMove()}
if(_3.rightButtonDown()){if(!isc.Browser.isMac||!_3.ctrlKeyDown())return true}
if(_9){_7=_3.stillWithinMouseDownTarget()?_3.mouseDownTarget():null}else{_7=_2.target}
if(_7!=_3.lastMoveTarget){if(this.logIsDebugEnabled()){this.logDebug((_3.lastMoveTarget?"mousing out of "+_3.lastMoveTarget+"  ":"")+(_7?"mousing over "+_7:""))}
var _10=_3.lastMoveTarget,_11,_12=_3.lastHoverTarget;if(_10){_3.handleEvent(_10,_3.MOUSE_OUT)}
if(_7){_3.handleEvent(_7,_3.MOUSE_OVER);_11=_7.getHoverTarget(_2)}
if(_11!=_12){if(_12)_12.stopHover();if(_11)_11.startHover();_3.lastHoverTarget=_11}
_3.lastMoveTarget=_7}
if(isc.Page.handleEvent(_7,_3.MOUSE_MOVE)==false)return false;if(_8)return _3.$la;if(!_3.targetIsEnabled(_7))return false;_3.bubbleEvent(_7,_3.MOUSE_MOVE);if(_7)_7.$ms();return true}
,isc.A.getNativeMouseTarget=function isc_c_EventHandler_getNativeMouseTarget(_1){if(!this.nativeTargetWarningLogged){this.nativeTargetWarningLogged=true;this.logWarn("getNativeMouseTarget(). This method will return the DOM element "+"the browser reports as the target or source of the current mouse event. "+"Please note that SmartClient cannot guarantee that the same element will "+"be reported in all browser/platform configurations for all event types. "+"If you wish to make use of this value, we recommend testing your use case "+"in all target browser configurations.")}
if(_1==null)_1=this.lastEvent;return _1.nativeTarget||_1.nativeDraggingTarget}
,isc.A.handleNativeMouseOut=function isc_c_EventHandler_handleNativeMouseOut(_1){if(isc.Browser==null)return;var _2=isc.EH;if(_2.$md||_2.$mk)return;var _3=(_1?_1:_2.getWindow().event),_4=(isc.Browser.isDOM?_3.target:_3.srcElement),_5=false;if(isc.Browser.isIE){_5=(_3.toElement==null)}else{_5=(_3.relatedTarget==null)}
if(_5)_2.$mr=true;if(_5&&_2.lastMoveTarget!=null){_2.$mt(_3);_2.handleEvent(_2.lastMoveTarget,_2.MOUSE_OUT);_2.lastMoveTarget=null;if(_2.lastHoverTarget){_2.lastHoverTarget.stopHover();delete _2.lastHoverTarget}}}
,isc.A.$mt=function isc_c_EventHandler__updateMouseOutEventProperties(_1){var _2=isc.EH;var _3=_2.lastEvent;if(isc.Browser.isIE){_3.nativeTarget=_1.toElement}else{_3.nativeTarget=_1.relatedTarget}
if(_3.nativeTarget==null)_3.target=null
else _3.target=this.getEventTargetCanvas(_1,_3.nativeTarget)}
,isc.A.$mg=function isc_c_EventHandler__handleMouseStillDown(_1){if(!isc.Page.isLoaded())return false;var _2=this;_2.$mu=isc.Timer.clear(_2.$mu);if(!_2.mouseIsDown()||!_2.mouseDownTarget())return false;if(_2.bubbleEvent(_2.mouseDownTarget(),_2.MOUSE_STILL_DOWN)==false)return false;var _3=_2.mouseDownTarget(),_4=this.$md?_3.mouseStillDownInitialDelay:_3.mouseStillDownDelay;_2.$mu=this.delayCall("$mg",[],_4);return true}
,isc.A.handleMouseUp=function isc_c_EventHandler_handleMouseUp(_1,_2){if(isc.Browser.isTouch&&!_2)return;var _3=isc.EH;if(isc.Browser.isIE&&!_3.$lr){var _4=_3.lastEvent;_4.eventType=_3.MOUSE_DOWN;_3.handleMouseDown(null,_3.lastEvent)}
if(!_2)_3.$mk=true;var _5=_3.$mv(_1,_2);_3.$mk=false;if(isc.Browser.isSafari)_5=true;return _5}
,isc.A.$mv=function isc_c_EventHandler__handleMouseUp(_1,_2){if(!isc.Page.isLoaded())return false;var _3=this,_4=(!_2?_3.getMouseEventProperties(_1):_3.lastEvent),_5=false;_3.$lr=false;delete _3.$mw;_3.$mu=isc.Timer.clear(_3.$mu);var _6=_3.$mx;if(_6){_6.focus();_3.$mx=null}
var _7=_3.$mf;_3.$mf=null;var _8;if(_7==null){_7=(_3.clickMaskClick(_4.target)==false);_8=_7}
if(_7==true){if(_3.logIsDebugEnabled())_3.logDebug("mouseUp cancelled by clickMask");return false}
var _9=false;if(_3.dragging){_9=_3.handleDragStop()}
if(_3.rightButtonDown(_4)){if(this.useSyntheticRightButtonEvents()){_3.handleContextMenu()}
_3.$mw=false}else{if(!_9){if(isc.Page.handleEvent(_4.target,_3.MOUSE_UP)!=false){var _10=true,x=this.lastEvent.x,y=this.lastEvent.y,_13=_3.mouseDownTarget();_5=_3.eventHandledNatively(_3.MOUSE_UP,_4.nativeTarget);if(!_5&&_3.targetIsEnabled(_13)){if(_13.visibleAtPoint(x,y))
_10=_3.bubbleEvent(_13,_3.MOUSE_UP,null,_8);else if(_13.containsPoint(x,y))
_10=_3.bubbleEvent(_13,_3.MOUSE_OUT,null,_8)}
if(_10!=false){_3.$mw=_3.handleClick(_4.target)}}}}
delete _3.redrawnWhileDown;_3.clearDragProperties();if(_3.$mq)_3.$mq=false;var _14=_4.target,_15=isc.isA.DynamicForm!=null&&isc.isA.DynamicForm(_14);if(_5&&(_15||_3.$mw==true))
return _3.$la;return(_15&&_3.$mw==true)}
,isc.A.clearDragProperties=function isc_c_EventHandler_clearDragProperties(){var _1=this;_1.dragging=false;delete _1.dragTarget;delete _1.dragTargetStartRect;delete _1.dragTargetLink;delete _1.dragMoveTarget;delete _1.dragMoveAction;delete _1.dragOperation;delete _1.dragAppearance;delete _1.dropTarget;delete _1.lastDropTarget}
,isc.A.handleContextMenu=function isc_c_EventHandler_handleContextMenu(_1){if(!isc.Page.isLoaded())return false;var _2=isc.EH;_2.$mk=true;var _3=_2.$my(_1);_2.$mk=false;return _3}
,isc.A.$my=function isc_c_EventHandler__handleContextMenu(_1){var _2=isc.Browser.isSafari||(this.isMouseEvent(this.lastEvent.eventType));if(this.$l8){delete this.$l8;return true}
if(_1)this.getMouseEventProperties(_1);var _3=this,_4=_3.lastEvent,_5=!_2?_4.keyTarget||_4.target:_4.target;_4.keyboardContextMenu=!_2;if(!_2&&!isc.Browser.isMoz){_4.x=_5?_5.getPageLeft():0;_4.y=_5?_5.getPageTop():0}
if(isc.Browser.isSafari&&_3.clickMaskClick(_5)==false){return false}
if(isc.Page.handleEvent(_5,_3.SHOW_CONTEXT_MENU)==false){return false}
var _6=true;if(_3.targetIsEnabled(_5)){_6=_3.bubbleEvent(_5,_3.SHOW_CONTEXT_MENU)}
if(_6!=false){if(_3.lastMoveTarget)_3.handleEvent(_3.lastMoveTarget,_3.MOUSE_OUT);delete _3.lastMoveTarget}
return _6}
,isc.A.handleNativeClick=function isc_c_EventHandler_handleNativeClick(){var _1=isc.EH,_2=(_1.$mw!=false);delete _1.$mw}
,isc.A.handleClick=function isc_c_EventHandler_handleClick(_1,_2){if(!isc.Page.isLoaded())return false;var _3=this,_4=_3.lastEvent,_5;if(!_2)_2=(_3.isDoubleClick(_1)?_3.DOUBLE_CLICK:_3.CLICK);if(isc.Page.handleEvent(_1,_2)==false){_5=false}else if(_3.eventHandledNatively(_2,_4.nativeTarget)){_5=_3.$la}else if(!_3.targetIsEnabled(_1)){_5=false}else if(!_3.stillWithinMouseDownTarget()){_5=false}else{var _1=_3.mouseDownTarget();_5=_3.bubbleEvent(_1,_2)}
_3.$mz=isc.timeStamp();return _5}
,isc.A.isDoubleClick=function isc_c_EventHandler_isDoubleClick(_1){var _2=this,_1=_1||_2.lastEvent.nativeTarget;var _3=_2.useNativeEventTime!=null?_2.useNativeEventTime:(isc.Browser.isMoz&&isc.Browser.isWin),_4,_5;if(_2._isSecondClick!=null){_5=_2._isSecondClick}else{if(_3){var _6=_2.lastEvent.DOMevent
_4=_6?_6.timeStamp:null;if(_4==0||!isc.isA.Number(_4)){this.logDebug("Unable to derive native 'timeStamp' attribute from DOM event");_4=isc.timeStamp()}
_5=((_4-_2.lastClickTime)<_2.DOUBLE_CLICK_DELAY)}else{_4=isc.timeStamp();_5=((_2.$mz-_2.lastClickTime)<_2.DOUBLE_CLICK_DELAY)?((_4-_2.lastClickTime)<_2.DOUBLE_CLICK_DELAY):((_4-_2.$mz)<100)}}
_2.lastClickTime=_4;if(!_5){delete _2.lastClickTarget}
var _7=false;if(_1==_2.lastClickTarget){_7=!_1.noDoubleClicks;if(_7){var _8=_1;while(_8.parentElement){_8=_8.parentElement;if(_8.noDoubleClicks){_7=false;break}}}}
_2.lastClickTarget=(_7?null:_1);return _7}
,isc.A.targetIsEnabled=function isc_c_EventHandler_targetIsEnabled(_1){if(!_1)return false;if(_1.destroyed)return false;if(isc.isA.Function(_1.isDisabled))return!_1.isDisabled();return true}
,isc.A.$m0=function isc_c_EventHandler__handleTouchStart(_1){var _2=isc.EH;_2.DOMevent=_1;var _3=_2.getMouseEventProperties(_1);var _4=_2.handleEvent(_3.target,_2.TOUCH_START);if(_4!==false){_3.originalType=_2.TOUCH_START;_3.eventType=_2.MOUSE_DOWN;_2.doHandleMouseDown(_1,_3);if(_2.$m1!=null)isc.Timer.clear(_2.$m1);_2.$m1=this.delayCall("$m2",[],_2.longTouchDelay)}}
,isc.A.$m2=function isc_c_EventHandler__handleLongTouch(){var _1=this;if(!_1.mouseIsDown()||!_1.mouseDownTarget()||!_1.stillWithinMouseDownTarget())return;_1.bubbleEvent(_1.mouseDownTarget(),_1.LONG_TOUCH)}
,isc.A.$m3=function isc_c_EventHandler__handleTouchMove(_1){var _2=isc.EH;_2.DOMevent=_1;var _3=_2.getMouseEventProperties(_1);var _4=_2.handleEvent(_3.target,_2.TOUCH_MOVE);if(_4!==false){_3.originalType=_2.TOUCH_MOVE;_3.eventType=_2.MOUSE_MOVE;_2.$mn(_1,_3);if(_2.dragging&&window.event!=null)window.event.preventDefault()}
if(_2.$m1!=null)isc.Timer.clear(_2.$m1)}
,isc.A.$m4=function isc_c_EventHandler__handleTouchEnd(_1){var _2=isc.EH;_2.DOMevent=_1;var _3=_2.getMouseEventProperties(_1);var _4=_2.handleEvent(_3.target,_2.TOUCH_END);if(_4!==false){_3.originalType=_2.TOUCH_END;_3.eventType=_2.MOUSE_UP;_2.$mv(_1,true)}
if(_2.$m1!=null)isc.Timer.clear(_2.$m1)}
,isc.A.$m5=function isc_c_EventHandler__handleTouchCancel(_1){var _2=isc.EH;_2.DOMevent=_1;var _3=_2.getMouseEventProperties(_1);this.delayCall("$m6",[_3,_1])}
,isc.A.$m6=function isc_c_EventHandler__handleDelayedTouchCancel(_1,_2){var _3=isc.EH;var _4=_3.handleEvent(_1.target,_3.TOUCH_END);if(_4!==false){_1.originalType=_3.TOUCH_CANCEL
_1.eventType=_3.MOUSE_UP;_3.$mv(_2,true)}
if(_3.$m1!=null)isc.Timer.clear(_3.$m1)}
,isc.A.getFocusCanvas=function isc_c_EventHandler_getFocusCanvas(){return this.$l4}
,isc.A.$m7=function isc_c_EventHandler__logFocus(_1,_2){if(!this.logIsDebugEnabled("nativeFocus"))return;this.logDebug((_2?"onfocus":"onblur")+" fired on: "+_1+this.$m8(),"nativeFocus")}
,isc.A.$m8=function isc_c_EventHandler__getActiveElementText(){if(!isc.Browser.isIE)return isc.$ah;var _1=this.getActiveElement();if(_1==null)return isc.$ah;return", activeElement: "+(_1.tagName)}
,isc.A.blurFocusCanvas=function isc_c_EventHandler_blurFocusCanvas(_1,_2){var _3=this.$g4;if(_2){this.$jp("BLR");this.$m7(_1);isc.EH.$m9=null}
this.$na(_1,_2);if(_2)this.$g4=_3}
,isc.A.$na=function isc_c_EventHandler__blurFocusCanvas(_1,_2){if(this.$l4){var _3=this.$l4;if(_1!=null&&_3!=_1)return;this.$l4=null;_3.$nb(false)}}
,isc.A.focusInCanvas=function isc_c_EventHandler_focusInCanvas(_1,_2){var _3=this.$g4;if(_2){this.$jp("FCS");this.$m7(_1,true);isc.EH.$nc=null}
if(isc.Browser.isMoz){if(_2&&(this.lastEvent.eventType!=this.KEY_DOWN&&this.lastEvent.eventType!=this.KEY_PRESS&&this.lastEvent.eventType!=this.KEY_UP))
{if(_1&&_1.showFocusOutline)_1.setShowFocusOutline(false,true)}else{if(_1&&_1.showFocusOutline)_1.setShowFocusOutline(true,true)}}
this._focusInCanvas(_1,_2);if(_2)this.$g4=_3}
,isc.A._focusInCanvas=function isc_c_EventHandler__focusInCanvas(_1,_2){if(!_1||_1.hasFocus||!_1.$mb()||_1.isDisabled())return;if(this.$l4==_1)return;this.checkMaskedFocus(_1);if(_2&&isc.Browser.isMoz){if(_1.parentElement)_1.parentElement.$nd(null,true)}
var _3=this.$l4;this.$l4=_1;if(_3)_3.$nb(false)
if(this.$l4!=_1)return;_1.$nb(true);if(this.targetIsMasked(_1)){var _4=this.clickMaskRegistry.last();this.setMaskedFocusCanvas(_1,_4)}}
,isc.A.setMaskedFocusCanvas=function isc_c_EventHandler_setMaskedFocusCanvas(_1,_2){if(!_2)return;_2.$ne=_1}
,isc.A.getMaskedFocusCanvas=function isc_c_EventHandler_getMaskedFocusCanvas(_1){if(_1==null)_1=this.clickMaskRegistry.last();else _1=this.getClickMask(_1);if(_1)return _1.$ne}
,isc.A.checkMaskedFocus=function isc_c_EventHandler_checkMaskedFocus(_1){if(isc.Browser.isIE){var _2=this.getActiveElement();var _3=_1?_1.getHandle():null;if(!_3)return;var _4;while(_2&&_2.tagName){if(_2==_3){_4=true;break}
if(_2.eventProxy){_4=(_2.eventProxy==_1.getID());break}
_2=_2.parentElement}
if(!_4)return}
var _5=this.clickMaskRegistry;for(var i=_5.length-1;i>=0;i--){var _7=_5[i];if(!this.targetIsMasked(_1,_7))return;else{if(this.isHardMask(_7))return false;this.$nf(_7)}}}
,isc.A.prepareForDragging=function isc_c_EventHandler_prepareForDragging(_1){var _2=this;if(_2.dragging)_2.handleDragStop();delete _2.dragMoveAction;delete _2.dragTarget;_2.bubbleEvent(_1,"prepareForDragging");if(!_2.dragTarget){if(this.logIsDebugEnabled("dragDrop"))this.logDebug("No dragTarget, not dragging","dragDrop");return}
if(this.logIsInfoEnabled("dragDrop"))
this.logInfo("target is draggable with dragOperation: "+_2.dragOperation+", dragTarget is : "+_2.dragTarget+(_2.dragTarget!=_1?" (delegated from: "+_1+")":""),"dragDrop");_2.dragTargetStartRect=_2.dragTarget.getRect()}
,isc.A.handleDragStart=function isc_c_EventHandler_handleDragStart(){var _1=this,_2=_1.lastEvent;if(!_1.mouseIsDown()||!_1.dragTarget)return false;delete _1.dropTarget;delete _1.dragMoveTarget;_1.dragOffsetX=-10;_1.dragOffsetY=-10;_1.handleEvent(_1.lastMoveTarget,_1.MOUSE_OUT);if(_1.lastMoveTarget!=_1.mouseDownTarget()){_1.handleEvent(_1.mouseDownTarget(),_1.MOUSE_OUT)}
if(isc.Hover)isc.Hover.clear();_1.dragStartOffsetX=_1.mouseDownEvent.x-_1.dragTarget.getPageLeft();_1.dragStartOffsetY=_1.mouseDownEvent.y-_1.dragTarget.getPageTop();var _3=_1.dragOperation+"Start";if(_1.handleEvent(_1.dragTarget,_3)==false){this.logInfo("drag cancelled by false return from: "+_3+" on "+_1.dragTarget,"dragDrop");delete _1.dragTarget;delete _1.dragTargetLink;_1.handleEvent(_1.dragTarget,_1.MOUSE_OVER);return false}
delete _1.lastMoveTarget;var _4=_1.dragTarget.getDragAppearance(_1.dragOperation);if(_4!=_1.TRACKER)
{_1.dragOffsetX=_1.dragStartOffsetX;_1.dragOffsetY=_1.dragStartOffsetY}
if(_1.dragOperation==_1.DRAG_SCROLL){_1.dragAppearance=_1.NONE}else{_1.dragAppearance=_1.dragTarget.getDragAppearance(_1.dragOperation)}
if(_1.dragAppearance==_1.TRACKER){_1.dragMoveTarget=_1.$ng();if(!_1.dragMoveAction)_1.dragMoveAction=_1.$nh;_1.dragTracker.setOverflow(isc.Canvas.VISIBLE);_1.bubbleEvent(_1.dragTarget,_1.SET_DRAG_TRACKER);_1.dragOffsetX=_1.dragTracker.offsetX;_1.dragOffsetY=_1.dragTracker.offsetY}else if(_1.dragAppearance==_1.OUTLINE){_1.dragMoveTarget=_1.getDragOutline(_1.dragTarget);if(!_1.dragMoveAction)_1.dragMoveAction=_1.$nh}else if(_1.dragAppearance==_1.TARGET){_1.dragMoveTarget=_1.dragTarget;if(!_1.dragMoveAction)_1.dragMoveAction=_1.$nh;if(_1.dragTarget.showDragShadow)this.$ni();if(_1.dragTarget.dragOpacity!=null)this.$nj()}else{}
if(_1.dragMoveTarget){if(_1.dragMoveTarget!=_1.dragTarget){_1.dragMoveTarget.dragIntersectStyle=_1.dragTarget.dragIntersectStyle}
_1.dragMoveTarget.show();_1.dragMoveTarget.bringToFront()}
var _5=_1.dragMoveTarget?_1.dragMoveTarget:_1.dragTarget;if((isc.Browser.isIE||isc.Browser.isMoz)&&_1.dragAppearance!=_1.OUTLINE&&!(_5.$nk||_5.neverBackMask))
{if(_1.alwaysBackMask){this.$nl(_5)}else{var _6=[];if(isc.BrowserPlugin){var _7=isc.BrowserPlugin.instances;for(var i=0;i<_7.length;i++){var _9=_7[i];if(_9.isVisible()&&(_5.parentElement==null||_5.parentElement.contains(_9,true)))
{_6.add({instance:_9,rect:_9.getPageRect()})}}}
if(isc.Browser.isIE&&isc.Browser.minorVersion>=5.5&&isc.NativeSelectItem){var _10=isc.NativeSelectItem.instances;for(var i=0;i<_10.length;i++){var _11=_10[i];if(_11.isVisible()&&(_5.parentElement==null||_5.parentElement.contains(_11.containerWidget,true)))
{_6.add({instance:_11,rect:_11.getPageRect()})}}}
if(_6.length>0&&_1.dynamicBackMask===false)
{this.$nl(_5)}else{_1.$nm=_6}}}
_1.showEventMasks((_1.dragOperation==_1.DRAG_RESIZE));_1.dragging=true;this.logInfo("Started dragOperation: "+_1.dragOperation+" with dragTarget: "+_1.dragTarget+" dragAppearance: "+_1.dragAppearance,"dragDrop");return true}
,isc.A.$ni=function isc_c_EventHandler__showTargetDragShadow(){var _1=isc.EH;var _2=_1.dragTarget;_1.$nn=(!_2.showShadow);_1.$no=_2.shadowDepth;_2.shadowDepth=_1.dragTargetShadowDepth;_2.updateShadow();if(!_2.showShadow)_2.setShowShadow(true)}
,isc.A.$np=function isc_c_EventHandler__hideTargetDragShadow(){var _1=isc.EH;var _2=_1.dragTarget;if(_1.$nn)_2.setShowShadow(false);_2.shadowDepth=_1.$no;_2.updateShadow();delete _1.$nn;delete _1.$no}
,isc.A.$nj=function isc_c_EventHandler__setTargetDragOpacity(){var _1=isc.EH;var _2=_1.dragTarget;_1.$nq=_2.opacity;_2.setOpacity(_2.dragOpacity)}
,isc.A.$nr=function isc_c_EventHandler__resetTargetDragOpacity(){var _1=isc.EH,_2=_1.dragTarget;_2.setOpacity(_1.$ns)}
,isc.A.$nl=function isc_c_EventHandler__showBackMask(_1){if(_1._backMask){if(!_1._backMask.isVisible())_1._backMask.show()}else{_1.makeBackMask({$nt:true})}}
,isc.A.$nu=function isc_c_EventHandler__hideBackMask(_1){if(_1._backMask&&_1._backMask.$nt&&_1._backMask.isVisible())
{_1._backMask.hide()}}
,isc.A.$nv=function isc_c_EventHandler__getDragMoveComponents(){var _1=this.dragMoveTarget;if(!_1)return;var _2=[_1];if(_1._backMask)_2.add(_1._backMask);if(_1.$nw)_2.add(_1.$nw);if(_1._shadow)_2.add(_1._shadow);return _2}
,isc.A.$nx=function isc_c_EventHandler__getDragMoveEventName(_1){var _2=this.$k0;if(!_2[_1]){_2[_1]=_1+"Move"}
return _2[_1]}
,isc.A.handleDragMove=function isc_c_EventHandler_handleDragMove(){var _1=this,_2=_1.lastEvent;isc.$ny=true;_1.dropTarget=_1.getDropTarget(_2);isc.$ny=false;if(_1.$nm&&_1.dynamicBackMask){var _3=false;var _4=_1.dragMoveTarget?_1.dragMoveTarget:_1.dragTarget;var _5=_4.getRect();for(var i=0;i<_1.$nm.length;i++){var _7=_1.$nm[i];if(isc.Canvas.rectsIntersect(_7.rect,_5))
{_1.$nz=_7.instance;_3=true;break}}
if(_1.$nz){if(_1.$nz.repaintIfRequired)_1.$nz.repaintIfRequired()}
if(_3){this.$nl(_4)}else{this.$nu(_4);delete _1.$nz}}else if(isc.BrowserPlugin){isc.BrowserPlugin.handleDragMoveNotify()}
if(_1.dragMoveAction)_1.dragMoveAction();if(_1.handleEvent(_1.dragTarget,this.$nx(_1.dragOperation))==false){delete _1.dropTarget;return false}
if(_1.dropTarget!=_1.lastDropTarget){this.logDebug("New drop target: "+_1.dropTarget,"dragDrop");if(_1.lastDropTarget){_1.handleEvent(_1.lastDropTarget,_1.DROP_OUT)}
if(_1.dropTarget){_1.handleEvent(_1.dropTarget,_1.DROP_OVER)}
_1.lastDropTarget=_1.dropTarget}
if(_1.dropTarget){_1.handleEvent(_1.dropTarget,_1.DROP_MOVE)}
isc.$ny=true;this.$n0();isc.$ny=false;return false}
,isc.A.$n0=function isc_c_EventHandler__handleDragScroll(){var _1=this,_2=_1.dragTarget;if(_1.dragOperation==_1.DRAG_SCROLL)return;if(_1.dragOperation==_1.DRAG_SELECT){if(_2.overflow==isc.Canvas.VISIBLE)return;if(!_2.containsEvent()||_2.$n1(_2.dragScrollDirection))
{_2.$n2(_2.dragScrollDirection,true)}}
var _3=[];var _4=_2.dragScrollType=="parentsOnly"?_2.getParentElements():isc.Canvas._canvasList;;if(_4==null||_4.length==0)return;for(var i=0;i<_4.length;i++){if((_4[i].hscrollOn||_4[i].vscrollOn)&&_4[i].isDrawn()&&_4[i].isVisible()&&_4[i].shouldDragScroll()){_3.add(_4[i])}}
var _6=_1.lastEvent,_7=_6.x,_8=_6.y,_9=[];for(var i=0;i<_3.length;i++){if(_3[i].visibleAtPoint(_7,_8,false,_1.$nv()))
_9.add(_3[i])}
if(_9.length>0){var _10;for(var i=0;i<_9.length;i++){if(_9[i].$n1(_2.dragScrollDirection)){if(_10==null||_10.contains(_9[i],true))
_10=_9[i]}}
if(_10!=null)_10.$n2(_2.dragScrollDirection)}}
);isc.evalBoundary;isc.B.push(isc.A.handleDragStop=function isc_c_EventHandler_handleDragStop(){var _1=this,_2=_1.lastEvent,_3=false;_1.dragging=false;this.logInfo("end of drag interaction","dragDrop");_1.dragOffsetX=_1.dragOffsetY=0;var _4=_1.dragTarget,_5=_1.dragMoveTarget,_6=_1.dragOperation;if(_5&&(_5==_1.dragTracker||_5==_1.dragOutline))
{_5.hide()}else{if(_4.showDragShadow)_1.$np();if(_4.dragOpacity!=null)_1.$nr()}
if(this.dragTracker&&this.dragTracker.$n3){this.dragTracker.destroy();delete this.dragTracker}
var _7=_1.dragMoveTarget?_1.dragMoveTarget:_1.dragTarget;this.$nu(_7);if(_1.$nm)delete _1.$nm;var _8=_1.dropTarget;if(_8){_1.handleEvent(_1.dropTarget,_1.DROP_OUT);if(_8.willAcceptDrop())_1.handleEvent(_8,_1.DROP);_3=true}
var _9=(_4==_5);if(_1.handleEvent(_4,_6+"Stop")!=false){_3=true;if(_6==_1.DRAG_RESIZE){if(!_9){if(_5!=null&&this.dragAppearance!=this.TRACKER){_4.setPageRect(_5.getPageLeft(),_5.getPageTop(),_5.getWidth(),_5.getHeight(),true)}else{var _10=isc.EH.resizeEdge;if(_10!=null){var X=isc.EH.getX(),Y=isc.EH.getY(),_13=_10.contains("L")?X-_1.dragTargetStartRect[0]:0,_14=_10.contains("T")?Y-_1.dragTargetStartRect[1]:0;_4.setPageRect(_10.contains("L")?X:_1.dragTargetStartRect[0],_10.contains("T")?Y:_1.dragTargetStartRect[1],_10.contains("R")?X-_4.getPageLeft():_1.dragTargetStartRect[2]-_13,_10.contains("B")?isc.EH.getY()-_4.getPageTop():_1.dragTargetStartRect[3]-_14,true)}}}
var _15=_4.getVisibleWidth()-_1.dragTargetStartRect[2],_16=_4.getVisibleHeight()-_1.dragTargetStartRect[3];_4.dragResized(_15,_16)}else if(_6==_1.DRAG_REPOSITION){if(!_9){if(_5!=null){_4.setPageRect(_5.getPageLeft(),_5.getPageTop())}else{_4.setPageRect(isc.EH.getX(),isc.EH.getY())}
_4.bringToFront()}
_1.dragTarget.dragRepositioned()}}else{if(_6==_1.DRAG_RESIZE){if(_9){_4.setRect(_1.dragTargetStartRect)}}else if(_1.dragOperation==_1.DRAG_REPOSITION){if(_9){_4.moveTo(_1.dragTargetStartRect[0],_1.dragTargetStartRect[1])}}}
_1.clearDragProperties();_1.hideEventMasks();var _17=_1.lastEvent.target;if(_17)_1.handleEvent(_17,_1.MOUSE_OVER);_1.lastMoveTarget=_17;return _3}
,isc.A.getEventTargetCanvas=function isc_c_EventHandler_getEventTargetCanvas(_1,_2,_3){if(_1==null)_1={};var _4=this,_5=this.getWindow();if(!_2)_2=(isc.Browser.isIE?_1.srcElement:_1.target);if(!_4.$n4(_2)){return _4.lastTarget}
if(_1&&_1.$lv)return _1.target;if(!_2||_2.tagName==this.$k1||_2.tagName==this.$k2){return(_4.lastTarget=null)}
if(_2&&_2.tagName&&_2.tagName==this.$lc){var _6=isc.Applet?isc.Applet.idForName(_2.name):null;return _6?window[_6]:_4.lastTarget}
if(isc.Browser.isIE&&_2.parentElement==null){_2=_4.lastTarget}else{var _7=this.$k3;if(isc.Browser.isIE&&!isc.Browser.isIE9){while(_2!=null){if(_2.eventProxy)break;_2=_2.parentElement}}else{while(_2!=null){if(_2.eventProxy!=null||(_2.hasAttribute!=null&&_2.hasAttribute(_7)))break;_2=_2.parentNode}}
if(!_2)return(_4.lastTarget=null);_2=_5[_2.getAttribute(_7)];while(_2&&_2.eventProxy){if(isc.isA.String(_2.eventProxy)){_2.eventProxy=_5[_2.eventProxy]}
_2=_2.eventProxy}
if(this.logIsInfoEnabled()&&!_1||(_1.type!="mousemove"&&_1.type!="selectstart"))
{if(_2!=null){this.logInfo("Target Canvas for event '"+_1.type+"': "+_2)}else{this.logDebug("No target Canvas for event '"+_1.type+"'")}}
if(_2==_4.dragTracker){_2=_4.lastTarget}
_4.lastTarget=_2}
if(isc.isA.Canvas(_2)){if(_3&&_2.getEventTarget){_2=_2.getEventTarget(_3)}
return _2}
return null}
,isc.A.$n4=function isc_c_EventHandler__canAccessNativeTargetProperties(_1){try{if(!(isc.Browser.isMoz&&_1==this.$k4))return true;_1.parentNode}catch(e){return false}
return true}
,isc.A.getDropTarget=function isc_c_EventHandler_getDropTarget(_1){var _2=this;if(!_2.dragTarget||!_2.dragTarget.canDrop||_2.dragOperation==_2.DRAG_RESIZE)return null;var _3=(_2.dragMoveTarget||_2.dragTarget),_4=_2.$kf,_5=[],i=0,_7=_4.length,_8=(_3.getDragAppearance(_2.dragOperation)!=isc.EH.TARGET);if(_3.dragIntersectStyle==_2.INTERSECT_WITH_MOUSE){if((_1.target!=this.mouseDownTarget()||(isc.Browser.isIE||(isc.Browser.isSafari&&!isc.Browser.isTouch)||(isc.Browser.isMoz&&isc.Browser.geckoVersion>20040616&&!this.mouseDownTarget().$mi))))
{var _9=_1.target;while(_9&&_9.dropTarget)_9=_9.dropTarget;if((_8||_9!=_3)&&(_4.contains(_9)))
{return _9}}
for(;i<_7;i++){var _10=_4[i];if(_10.canAcceptDrop&&!_10.isDisabled()&&(_10.visibleAtPoint(_1.x,_1.y,false,_2.$nv()))&&(_8||_10!=_3))
{_5.add(_10)}}}else{for(;i<_7;i++){var _10=_4[i];if(!_8&&_10==_3)continue;if(_10.intersects(_3)&&_10.canAcceptDrop&&!_10.isDisabled())
{_5.add(_10)}}}
if(_5.length<2)return _5[0];var _11=_5[0];for(var i=1;i<_5.length;i++){var _12=_5[i];if(_11.contains(_12,true)){_11=_12}else if(_3.dragIntersectStyle==_2.INTERSECT_WITH_RECT){var _13=null,_14=_11,_15=_12;while(_13==null){if(_14.parentElement==null){_13=true;_15=_12.topElement||_12}else if(_14.parentElement.contains(_12,true)){_13=_14.parentElement;while(_15.parentElement!=_13){_15=_15.parentElement}}else{_14=_14.parentElement}}
if(_15.getZIndex()>_14.getZIndex()){_11=_12}}}
return _11}
,isc.A.registerDroppableItem=function isc_c_EventHandler_registerDroppableItem(_1){if(!_1.$n5){this.$kf.add(_1);_1.$n5=true}}
,isc.A.unregisterDroppableItem=function isc_c_EventHandler_unregisterDroppableItem(_1){this.$kf.remove(_1);delete _1.$n5}
,isc.A.registerMaskableItem=function isc_c_EventHandler_registerMaskableItem(_1,_2){if(!this.$kg.contains(_1)){this.$kg.add(_1);if(_2)this.makeEventMask(_1,{eventProxy:_1})}}
,isc.A.unregisterMaskableItem=function isc_c_EventHandler_unregisterMaskableItem(_1){this.$kg.remove(_1);if(_1._eventMask)_1._eventMask.destroy();delete _1._eventMask}
,isc.A.makeEventMask=function isc_c_EventHandler_makeEventMask(_1,_2,_3){if(isc.isA.Function(_1.makeEventMask))return _1.makeEventMask(_2,_3);var _4=this.$k5;if(!_4.contents)_4.contents=isc.Browser.isIE&&isc.Browser.version>6?isc.Canvas.blankImgHTML(3200,2400):isc.Canvas.spacerHTML(3200,2400);var _5=isc.Canvas.create({ID:_1.getID()+"_eventMask",cursor:_1.cursor,$k9:_1},_4,_2);_5.setRect(_3?_3:_1.getRect());_1._eventMask=_5;_1.addPeer(_5);return _5}
,isc.A.showEventMasks=function isc_c_EventHandler_showEventMasks(_1,_2){var _3=this,_4=_3.$kg;if(_1){if(!_3._eventMask)_3._eventMask=isc.ScreenSpan.create({ID:"isc_EH_eventMask",mouseDown:function(){this.hide()},pointersToThis:[{object:_3,property:"_eventMask"}]});_3._eventMask.show();_3._eventMask.bringToFront();if(isc.BrowserPlugin){_4.intersect(isc.BrowserPlugin.instances).map("$n6")}}else{for(var i=0;i<_4.length;i++){var _6=_4[i];if(_2&&_2[_6.getID()]){_6.$n7()}else{_6.$n6()}}}}
,isc.A.hideEventMasks=function isc_c_EventHandler_hideEventMasks(){var _1=this,_2=_1.$kg;if(_1._eventMask&&_1._eventMask.isVisible()){_1._eventMask.hide();if(isc.BrowserPlugin){_2.intersect(isc.BrowserPlugin.instances).map("$n7")}}else{for(var i=0;i<_2.length;i++){_2[i].$n7()}}}
,isc.A.eventHandledNatively=function isc_c_EventHandler_eventHandledNatively(_1,_2,_3){var _4=_1;if(!this.reverseEventTypes[_1]){if(this.$kh[_1])
_4=this.$kh[_1];else if(this.$km[_1])
_4=this.$km[_1]}
var _5=this.$n8(_4,_2,_3);if(_5&&this.logIsDebugEnabled()&&_4!="mouseMove"){this.logDebug(_1+" event on "+(_3?" native target:"+_2:this.lastTarget)+" handled natively")}
return _5}
,isc.A.$n8=function isc_c_EventHandler__eventHandledNatively(_1,_2,_3){_1=(_1||"");var _4=this,_5=_4.lastEvent;if(!_4.$n4(_2)){return true}
if(_2&&_2.tagName==this.$lc)return true;var _6=_4.isMouseEvent(_1),_7=_6?_5.target:_5.keyTarget;if(!_3&&_6&&_7==null)return true;if((this.logIsInfoEnabled()&&_1==_4.KEY_DOWN)||(this.logIsDebugEnabled()&&(_1==_4.KEY_UP||_1==_4.KEY_PRESS)))
{this.logInfo(_1+" event with Canvas target: "+this.lastEvent.keyTarget+", native target: "+this.echoLeaf(_2))}
if(_4.passThroughEvents&&_2){var _8=_2,_9=(_8.handleNativeEvents||(_8.getAttribute?_8.getAttribute(this.$lb):null)),_10=_8.tagName,_11;if(!_4.$n9)_4.$n9="false";if(_9==null){_11=(!_8.focusProxy&&((_8.form!=null&&_10!=_4.$kl)||_4.$kk[_10]!=null||(_8.isContentEditable&&!_8.eventProxy)));if(!_11&&(_1!=_4.MOUSE_WHEEL)&&(_1!=_4.MOUSE_MOVE)){while(_8&&_8.tagName!=_4.BODY_TAG&&_8.tagName!=this.$k2)
{if(_8.eventProxy!=null||(_8.hasAttribute!=null&&_8.hasAttribute(this.$k3)))break;if(_4.$kj[_8.tagName]!=null){var _12=(_8.handleNativeEvents||(_8.getAttribute?_8.getAttribute(this.$lb):null));if(_12!=null&&!isc.isA.emptyString(_12)){if(isc.isA.String(_12))
_12=(_12==isc.EH.$n9?false:true)}
if(_12!=false){_11=true;break}}
_8=_8.parentNode}}}else{_11=_9;if(_11==_4.$n9)_11=false}
if(_11){return true}else if(_9!=null){return false}}
if(!_3&&_6&&this.$oa(_7,_1,_5))
{return true}
return false}
,isc.A.isMouseEvent=function isc_c_EventHandler_isMouseEvent(_1){_1=_1||this.lastEvent.eventType;if(this.$ob==null){this.$ob={mouseOver:true,mouseover:true,mouseDown:true,mousedown:true,rightMouseDown:true,mouseMove:true,mousemove:true,mouseOut:true,mouseout:true,mouseUp:true,mouseup:true,DOMMouseScroll:true,mousewheel:true,mouseWheel:true,click:true,doubleClick:true,doubleclick:true,showContextMenu:true,showcontextmenu:true,selectStart:true,selectstart:true}}
if(this.$ob[_1]==true)return true;if(_1=="selectionChange"){return(this.lastEvent.keyName==null||this.lastEvent.keyName=="")}
if(_1=="contextMenu"||_1=="contextmenu"){return!this.lastEvent.keyboardContextMenu}
return false}
,isc.A.isKeyEvent=function isc_c_EventHandler_isKeyEvent(_1){_1=_1||this.lastEvent.eventType;if(this.$oc==null){this.$oc={};var _2=this.$oc;_2[this.KEY_DOWN]=true;_2[this.KEY_PRESS]=true;_2[this.KEY_UP]=true;var _3=this.$km;for(var _4 in _3)_2[_4]=true}
if(this.$oc[_1]==true)return true;if(_1=="contextMenu"||_1=="contextmenu"){return!!this.lastEvent.keyboardContextMenu}
return false}
,isc.A.$oa=function isc_c_EventHandler__eventOverCSSScrollbar(_1,_2,_3){if(isc.Browser.isTouch)return false;var _4=this;if(!_1||_1.showCustomScrollbars||!(_1.vscrollOn||_1.hscrollOn))return false;var _5=isc.Element.getNativeScrollbarSize();if(_1.isRTL()){if((_1.vscrollOn&&(_3.x<_1.getPageLeft()+_5))||(_1.hscrollOn&&(_3.y>_1.getPageTop()+_1.getHeight()-_5)))
{if(_2==_4.MOUSE_DOWN)_4.$mq=true;return true}}else{if((_1.vscrollOn&&(_3.x>_1.getPageRight()-_5))||(_1.hscrollOn&&(_3.y>_1.getPageBottom()-_5)))
{if(_2==_4.MOUSE_DOWN)_4.$mq=true;return true}}
return false}
,isc.A.bubbleEvent=function isc_c_EventHandler_bubbleEvent(_1,_2,_3,_4){var _5=this,_6=_5.lastEvent;var _7=this.logIsDebugEnabled()&&!this.$ld[_2];var _8=this.isMouseEvent(_2);if(_8){if(_4==null){_4=this.targetIsMasked(_1,null)}
if(_4){if(_7){this.logDebug(_2+" on "+_1+" blocked by clickmask")}
return false}}
var _9=this.$od(_2);while(_1){if(_1.destroyed)break;var _10=null;var _11=null;if(_1.mouseEventParent&&_2.startsWith("mouse")){_10=_1.mouseEventParent}else if(_1.keyEventParent&&_2.startsWith("key")){_10=_1.keyEventParent}else{_10=(_1.eventParent||_1.parentElement)}
if(_10&&_10.eventProxy)_10=_10.eventProxy;if(_1[_9]!=null){_11=_9}else if(_1[_2]!=null&&_1[_2]!=isc.Class.NO_OP&&!isc.is.emptyString(_1[_2])){_11=_2;if(isc.isA.String(_1[_2])){_1.convertToMethod(_2)}
if(_7){this.logDebug("Bubbling event '"+_2+"', target '"+_1+"' has handler: "+this.echoLeaf(_1[_2]))}}
if(_11!=null&&_1[_11]!=null){var _12;_12=_1[_11](_6,_3);if(_12==false){if(_7){this.logDebug("Bubbling for event '"+_2+"' cancelled via false return value by target: "+_1)}
return false}
if(_12==_5.STOP_BUBBLING){if(_7){this.logDebug("Bubbling for event '"+_2+"' cancelled via STOP_BUBBLING return value by target: "+_1)}
return _5.STOP_BUBBLING}}
if(_1.bubbleEvents==false||(_1.bubbleMouseEvents==false&&_5.isMouseEvent(_2)))
{if(_7){this.logDebug("Bubbling for event '"+_2+"' stopped by '"+_1+"' which does not allow bubbling")}
return true}else if(isc.isAn.Array(_1.bubbleMouseEvents)){if(_1.bubbleMouseEvents.contains(_2)){if(_7){this.logDebug("Bubbling for event '"+_2+"' stopped by '"+_1+"' which does not allow bubbling")}
return true}}
_1=_10}
if(_7)this.logDebug("Event '"+_2+"' bubbled to top");return true}
,isc.A.$od=function isc_c_EventHandler__getInternalHandlerName(_1){if(!this.$kr[_1]){this.$kr[_1]="handle"+_1.charAt(0).toUpperCase()+_1.substring(1)}
return this.$kr[_1]}
,isc.A.hasEventHandler=function isc_c_EventHandler_hasEventHandler(_1,_2){if(!isc.isAn.Object(_1)||!isc.isA.String(_2)){isc.Log.logWarn("EventHandler.hasEventHandler() passed bad parameters ["+[_1,_2]+"]. returning null;","event");return null}
var _3=this.$od(_2);if(this.getBubbledProperty(_1,_2)!=null||this.getBubbledProperty(_1,_3)!=null)return true;return false}
,isc.A.getBubbledProperty=function isc_c_EventHandler_getBubbledProperty(_1,_2){while(_1){if(_1[_2])return _1[_2];_1=(_1.eventParent||_1.parentElement);if(_1&&_1.eventProxy)_1=_1.eventProxy}
return null}
,isc.A.handleSelectStart=function isc_c_EventHandler_handleSelectStart(){var _1=isc.EH;var _2=_1.getWindow(),_3=_2.event?_2.event.srcElement:null,_4=_1.mouseDownEvent?_1.mouseDownEvent.nativeTarget:null;if(_3&&_4==_3&&_3.form&&!_1.dragging)
{return true}
if(isc.EH.$oe)return true;var _5=isc.EH.mouseIsDown()?_1.mouseDownTarget():null,_6=_1.getEventTargetCanvas(_2.event);var _7=(_1.dragging||_1.dragTarget)&&_1.dragOperation!=_1.DRAG_SELECT;var _8=!_7&&(_5!=null?_5.$mh():true)&&(_6!=null?_6.$mh():true);if(_8)return true;return _1.killEvent()}
,isc.A.handleSelectionChange=function isc_c_EventHandler_handleSelectionChange(_1){if(!_1)_1=window.event;var _2=isc.EH;var _3=_2.lastEvent;var _4=isc.Element.$of(document);if(_4){var _5=_2.getEventTargetCanvas(_1,_4);_3.nativeKeyTarget=_4
_3.keyTarget=_5;_3.eventType=this.$le;if(_5){_5.keyTarget=_5;_2.bubbleEvent(_3.keyTarget,"selectionChange")}}
return true}
,isc.A.handleNativeHelp=function isc_c_EventHandler_handleNativeHelp(){if(this.$og){if(this.$og()==false)return false}
if(this.$oh){if(this.$oh()==false)return false}
return isc.EH.$ly(window.event,true)}
,isc.A.handleNativeDragStart=function isc_c_EventHandler_handleNativeDragStart(){if(isc.EH.dragTarget)return false;var _1=isc.EH.mouseDownTarget();if(_1)return!!(_1.$mh());if(this.$oi)return this.$oi();if(this.$oj)return this.$oj()}
,isc.A.handleResize=function isc_c_EventHandler_handleResize(_1){if(isc.EH.resizeTimer==null){isc.EH.resizeTimer=isc.Timer.setTimeout("isc.EH.$i4()",0)}
return true}
,isc.A.handleOrientationChange=function isc_c_EventHandler_handleOrientationChange(_1){this.$ok()}
,isc.A.$ol=function isc_c_EventHandler__pageResizePollMethod(){isc.EH.$i4(true)}
,isc.A.$i4=function isc_c_EventHandler__pageResize(_1){isc.EH.resizeTimer=null;var _2=isc.Page.getOrientation();if(!_1){this.$om=isc.Page.getWidth(window,true);this.$on=isc.Page.getHeight(window,true);if(this.resizingPollTimer!=null)isc.Timer.clearTimeout(this.resizingPollTimer);this.resizingPollTimer=isc.Timer.setTimeout(this.$ol,100)}else{var _3=isc.Page.getWidth(window,true),_4=isc.Page.getHeight(window,true),_5=(_2==this.currentOrientation)&&(_3==this.$om&&_4==this.$on)
if(isc.Page.pollPageSize){isc.Page.setEvent(isc.EH.IDLE,this.$ol,isc.Page.FIRE_ONCE)}
if(_5)return;this.$om=_3;this.$on=_4}
this.$ok(_2)}
,isc.A.$ok=function isc_c_EventHandler__fireResizeEvent(_1){isc.Page.handleEvent(null,isc.EH.RESIZE);if(_1==null)_1=isc.Page.getOrientation();if(_1!=this.currentOrientation){this.currentOrientation=_1;isc.Page.handleEvent(null,isc.EH.ORIENTATION_CHANGE)}}
,isc.A.handleMouseWheel=function isc_c_EventHandler_handleMouseWheel(_1){var _2=isc.EH;if(!_1)_1=_2.getWindow().event;var _3=(_1.srcElement||_1.target);if(_2.eventHandledNatively(_1.type,_3))return _2.$la;_2.getMouseEventProperties(_1);var _4=_2.getEventTargetCanvas(_1);if(_2.bubbleEvent(_4,_2.eventTypes.MOUSE_WHEEL)==false){if(_1.preventDefault)_1.preventDefault();return false}
return true}
,isc.A.getWheelDelta=function isc_c_EventHandler_getWheelDelta(_1){return(_1||this.lastEvent).wheelDelta}
,isc.A.handleDOMMouseScroll=function isc_c_EventHandler_handleDOMMouseScroll(_1){return isc.EH.handleMouseWheel(_1)}
,isc.A.handleScroll=function isc_c_EventHandler_handleScroll(_1){}
,isc.A.prepareForLinkDrag=function isc_c_EventHandler_prepareForLinkDrag(_1,_2){this.dragTarget=(isc.isA.String(_1)?this.getWindow()[_1]:_1);this.dragTargetLink=_2;return false}
,isc.A.setDragTracker=function isc_c_EventHandler_setDragTracker(_1,_2,_3,_4,_5,_6){var _7=this.$ng(_6);_2=_2||10;_3=_3||10;_7.resizeTo(_2,_3);_7.setContents(_1);_7.redrawIfDirty("setDragTracker");if(_4)_7.offsetX=_4;if(_5)_7.offsetY=_5;_7.$n3=true}
,isc.A.$ng=function isc_c_EventHandler__makeDragTracker(_1){if(!this.dragTracker){var _2=this.dragTrackerDefaults;_2.contents=isc.Canvas.imgHTML("[SKIN]black.gif",10,10);this.dragTracker=isc.Canvas.create(_2,_1)}else if(_1!=null)this.dragTracker.setProperties(_1);return this.dragTracker}
,isc.A.getDragOutline=function isc_c_EventHandler_getDragOutline(_1,_2,_3){if(!this.dragOutline){this.dragOutline=isc.Canvas.create({autoDraw:false,overflow:isc.Canvas.HIDDEN})
if(isc.Browser.isIE)this.dragOutline.setContents(isc.Canvas.spacerHTML(3200,2400))}
var _4=this.dragOutline;if(isc.Element.getStyleDeclaration(_1.dragOutlineStyle)){_4.setStyleName(_1.dragOutlineStyle)}else{_4.setBorder((_2||1)+"px solid "+(_3||"black"))}
_4.setPageRect(_1.getPageLeft(),_1.getPageTop(),_1.getVisibleWidth(),_1.getVisibleHeight());_4.minWidth=_1.minWidth;_4.minHeight=_1.minHeight;_4.maxWidth=_1.maxWidth;_4.maxHeight=_1.maxHeight;if(isc.isAn.Array(_1.keepInParentRect)){_4.keepInParentRect=_1.keepInParentRect}else if(_1.keepInParentRect==true){_4.keepInParentRect=_1.getParentPageRect()}else{_4.keepInParentRect=null}
return _4}
,isc.A.getDragRect=function isc_c_EventHandler_getDragRect(){var _1=this.dragMoveTarget||this.dragTarget;if(!_1)return null;return _1.getPageRect()}
,isc.A.$nh=function isc_c_EventHandler__moveDragMoveTarget(){var _1=this;var _2=_1.dragMoveTarget;if(!_2)return true;var _3=(isc.Browser.isMoz&&isc.Browser.geckoVersion<20031007&&!_2.keepInParentRect);if(_3&&(_2.parentElement&&!_2.parentElement.containsPoint(_1.lastEvent.x,_1.lastEvent.y)))
{return true}
isc.$ny=true;_1.dragMoveTarget.moveToEvent(_1.dragOffsetX,_1.dragOffsetY);isc.$ny=false;return true}
,isc.A.$oo=function isc_c_EventHandler__resizeDragMoveTarget(){var _1=this;if(_1.dragMoveTarget)_1.dragMoveTarget.resizeToEvent(_1.resizeEdge);return true}
,isc.A.killEvent=function isc_c_EventHandler_killEvent(){isc.EH.getWindow().event.cancelBubble=true;return false}
,isc.A.stopBubbling=function isc_c_EventHandler_stopBubbling(){return isc.EH.STOP_BUBBLING}
,isc.A.startIdleTimer=function isc_c_EventHandler_startIdleTimer(){if(!isc.Page.isLoaded())return;if(!this.idleTimer){this.idleTimer=isc.Timer.setTimeout({target:isc.EH,methodName:this.$lf},this.IDLE_DELAY)}}
,isc.A.$lg=function isc_c_EventHandler__handleIdle(){this.idleTimer=null;var _1=isc.Page.handleEvent(null,this.IDLE);if(isc.Page.actionsArePendingForEvent(this.IDLE))this.startIdleTimer();return _1}
,isc.A.$jp=function isc_c_EventHandler__setThread(_1){var _2=_1+this.$lh++;if(this.$g4!=null)this.$op=this.$g4;this.$g4=_2;if(this.$lh>9)this.$lh=0}
,isc.A.$jq=function isc_c_EventHandler__clearThread(){if(this.$oq!=null)this.runTeas();if(this.$op){this.$g4==this.$op;this.$op=null}else{this.$g4=null}}
,isc.A.$or=function isc_c_EventHandler__setThreadExitAction(_1){isc.Timer.setTimeout(_1,0);var _2=this.$oq;if(_2==null)_2=this.$oq=[];_2.add(_1)}
,isc.A.runTeas=function isc_c_EventHandler_runTeas(){this.$g4+="[E]";while(this.$oq!=null){var _1=this.$oq;this.$oq=null;if(this.logIsDebugEnabled()){this.logDebug("firing threadExitActions: "+this.echoAll(_1))}
for(var i=0;i<_1.length;i++){var _3=_1[i];if(isc.isA.String(_3))isc.eval(_3);else _3()}}}
,isc.A.dispatch=function isc_c_EventHandler_dispatch(_1,_2){if(isc.$c9!=null){delete isc.$c9}
if(isc.Browser.isIE)_2=this.getWindow().event;this.$jp(this.$li[_2.type]||_2.type);if(isc.Log.supportsOnError){var _3=_1.call(this,_2)}else{try{var _3=_1.call(this,_2)}catch(e){isc.Log.$a3(e);throw e;}}
this.$jq();if(_3!=false&&this.$ln[_2.type]){var _4=this.$ln[_2.type](_2);if(_4==false)_3=false}
return _3}
,isc.A.captureEvent=function isc_c_EventHandler_captureEvent(_1,_2,_3,_4){var _5=this.getWindow(),_6=this.$os;var _7=isc.$ao(this.$lk,this.$ll);_7.$cv=_5;_7.$lm=_4;var _8;if(!_6){if(_1[_2]!=null){var _8=this.$lo[_2]||_2.substring(2);this.$ln[_8]=_1[_2]}
_1[_2]=_7}else{if(isc.Browser.isIE){_1.attachEvent(_2,_7)}else if(isc.Browser.isDOM){_8=this.$lo[_2]||_2.substring(2);_1.addEventListener(_8,_7,false)}else{this.logWarn("Unable to use event listeners in this browser");this.$os=false;return this.captureEvent(_1,_2,_3,_4)}}
if(_1===_5.document){var _9=(!_6||isc.Browser.isIE)?_2:_8;this.$lp[_9]=_7}}
,isc.A.captureEvents=function isc_c_EventHandler_captureEvents(_1){var _2=this;if(window.isc_useEventListeners!=null)_2.$os=window.isc_useEventListeners;var _3=isc.makeReverseMap(_2.eventTypes);isc.addProperties(_2,{reverseEventTypes:_3});if(_1==null)_1=this.getWindow();var _4=_1.document;isc.Page.setEvent(_2.LOAD,isc.Page.finishedLoading);if(isc.Browser.isIE){_1.attachEvent("onload",_2.handleLoad)}else if(isc.Browser.isDOM&&!isc.Browser.isOpera){_1.addEventListener("load",_2.handleLoad,true)}else{this.captureEvent(_1,"onload",_2.LOAD,_2.handleLoad)}
if(!this.$lx()){this.captureEvent(_1,"onunload",_2.UNLOAD,_2.handleUnload)}
this.captureEvent(_1,"onresize",_2.RESIZE,_2.handleResize);this.captureEvent(_4,"onmousedown",_2.MOUSE_DOWN,_2.handleMouseDown);this.captureEvent(_4,"onmousemove",_2.MOUSE_MOVE,_2.handleMouseMove);this.captureEvent(_4,"onmouseup",_2.MOUSE_UP,_2.handleMouseUp);this.captureEvent(_4,"onclick",_2.CLICK,_2.handleNativeClick);this.captureEvent(_4,"ondblclick",_2.DOUBLE_CLICK,_2.handleNativeClick);this.captureEvent(_4,"onscroll","scroll",_2.handleScroll);this.captureEvent(_4,"onmousewheel",_2.MOUSE_WHEEL,_2.handleMouseWheel);if(isc.Browser.isMoz){_1.addEventListener("DOMMouseScroll",_2.handleDOMMouseScroll,true)}
this.captureEvent(_4,"onmouseout",_2.MOUSE_OUT,_2.handleNativeMouseOut);this.captureEvent(_4,"oncontextmenu",_2.SHOW_CONTEXT_MENU,_2.handleContextMenu);this.captureEvent(_4,"onselectstart",_2.SELECT_START,_2.handleSelectStart);this.captureEvent(_1,"onselectstart",_2.SELECT_START,_2.handleSelectStart);if(isc.Browser.isIE){this.captureEvent(_4,"onselectionchange",_2.SELECTION_CHANGE,_2.handleSelectionChange)}
if(_1.isc_captureKeyEvents!=false){this.captureEvent(_4,"onkeydown",_2.KEY_DOWN,_2.$ly);this.captureEvent(_4,"onkeypress",_2.KEY_PRESS,_2.$l3);this.captureEvent(_4,"onkeyup",_2.KEY_UP,_2.$l1)}
if(isc.Browser.isIE){this.$oj=_1.ondragstart;this.$oi=_4.ondragstart;_4.ondragstart=_1.ondragstart=_2.handleNativeDragStart;this.$oh=_1.onhelp;this.$og=_4.onhelp;_4.onhelp=_1.onhelp=_2.handleNativeHelp}
if(isc.Browser.isTouch){this.captureEvent(_4,"ontouchstart",_2.TOUCH_START,_2.$m0);this.captureEvent(_4,"ontouchmove",_2.TOUCH_MOVE,_2.$m3);this.captureEvent(_4,"ontouchend",_2.TOUCH_END,_2.$m4);this.captureEvent(_4,"ontouchcancel",_2.TOUCH_CANCEL,_2.$m5)}
if(isc.Browser.isMobile){isc.Page.pollPageSize=true}}
,isc.A.$lx=function isc_c_EventHandler__useEventListenerForUnload(){return(isc.Browser.isSafari&&isc.Browser.safariVersion<=412)}
,isc.A.releaseEvents=function isc_c_EventHandler_releaseEvents(_1){var _2=this;if(_1==null)_1=this.getWindow();var _3=_1.document,_4=this.$lp;for(var _5 in _4){if(!this.$os){_3[_5]=null}else{if(isc.Browser.isIE){_3.detachEvent(_5,_4[_5])}else if(isc.Browser.isDOM){_3.removeEventListener(_5,_4[_5],false)}}}
if(isc.Browser.isIE){_3.ondragstart=_1.onhelp=null;_3.onhelp=_1.onhelp=null}
delete this.$lp}
,isc.A.getLastEvent=function isc_c_EventHandler_getLastEvent(){return this.lastEvent}
,isc.A.getEventType=function isc_c_EventHandler_getEventType(_1){return(_1||this.lastEvent).eventType}
,isc.A.getTarget=function isc_c_EventHandler_getTarget(_1){return(_1||this.lastEvent).target}
,isc.A.getDragTarget=function isc_c_EventHandler_getDragTarget(){return this.dragTarget}
,isc.A.getX=function isc_c_EventHandler_getX(_1){return(_1||this.lastEvent).x}
,isc.A.getY=function isc_c_EventHandler_getY(_1){return(_1||this.lastEvent).y}
,isc.A.getScreenX=function isc_c_EventHandler_getScreenX(_1){return(_1||this.lastEvent).screenX}
,isc.A.getScreenY=function isc_c_EventHandler_getScreenY(_1){return(_1||this.lastEvent).screenY}
,isc.A.mouseIsDown=function isc_c_EventHandler_mouseIsDown(){return(this.$lr)}
,isc.A.mouseDownTarget=function isc_c_EventHandler_mouseDownTarget(){return(this.mouseDownEvent?this.mouseDownEvent.target:null)}
,isc.A.getButtonNum=function isc_c_EventHandler_getButtonNum(_1){return(_1||this.lastEvent).buttonNum}
,isc.A.leftButtonDown=function isc_c_EventHandler_leftButtonDown(_1){return((_1||this.lastEvent).buttonNum==1)}
,isc.A.rightButtonDown=function isc_c_EventHandler_rightButtonDown(_1){if(!_1)_1=this.lastEvent;return(_1.buttonNum==2)||(_1.button==2)||(isc.Browser.isMac&&_1.ctrlKey)||(isc.Browser.isOpera&&(_1.ctrlKey&&_1.shiftKey))||((isc.Browser.isSafari&&(isc.Browser.safariVersion<125))&&_1.altKey)}
,isc.A.useSyntheticRightButtonEvents=function isc_c_EventHandler_useSyntheticRightButtonEvents(){return isc.Browser.isOpera||(isc.Browser.isSafari&&(isc.Browser.safariVersion<125))}
,isc.A.getKeyEventCharacterValue=function isc_c_EventHandler_getKeyEventCharacterValue(_1){return(_1||this.lastEvent).characterValue}
,isc.A.getKeyEventCharacter=function isc_c_EventHandler_getKeyEventCharacter(_1){return String.fromCharCode(this.getKeyEventCharacterValue(_1))}
,isc.A.getKey=function isc_c_EventHandler_getKey(_1){return(_1||this.lastEvent).keyName||null}
,isc.A.getKeyName=function isc_c_EventHandler_getKeyName(_1){return this.getKey(_1)}
,isc.A.shiftKeyDown=function isc_c_EventHandler_shiftKeyDown(_1){return!!((_1||this.lastEvent).shiftKey)}
,isc.A.ctrlKeyDown=function isc_c_EventHandler_ctrlKeyDown(_1){return!!((_1||this.lastEvent).ctrlKey)}
,isc.A.altKeyDown=function isc_c_EventHandler_altKeyDown(_1){return!!((_1||this.lastEvent).altKey)}
,isc.A.metaKeyDown=function isc_c_EventHandler_metaKeyDown(_1){return!!((_1||this.lastEvent).metaKey)}
,isc.A.getKeyEventProperties=function isc_c_EventHandler_getKeyEventProperties(_1){if(_1==null)_1=this.getWindow().event;var _2=this.lastEvent;_2.nativeKeyTarget=(_1.target||_1.srcElement);_2.keyTarget=this.$l4;if(isc.isA&&isc.DynamicForm&&isc.isA.DynamicForm(this.$l4)){var _3=isc.DynamicForm.$ot(_2.nativeKeyTarget,_2.keyTarget);if(_3&&_3.item)_2.keyTarget=_3.item}
_2.eventType=this.getKeyEventType(_1.type);if(_2.eventType==this.KEY_PRESS){_2.characterValue=this.$ou(_1)}
var _4=this.determineEventKeyName(_1);if(_4!=null){_2.keyName=_4}else if(_2.eventType!=isc.EH.keyPress)delete _2.keyName;_2.nativeKeyCode=_1.keyCode;_2.shiftKey=(_1.shiftKey==true||(isc.Browser.isMoz&&_2.shiftKey));_2.ctrlKey=(_1.ctrlKey==true);_2.altKey=(_1.altKey==true);_2.metaKey=(_1.metaKey==true)}
,isc.A.getKeyEventType=function isc_c_EventHandler_getKeyEventType(_1){if(!_1)return;return this.$km[_1]}
,isc.A.$ou=function isc_c_EventHandler__determineKeyEventCharacterValue(_1){if(isc.Browser.isIE)return(_1.keyCode||null);if(isc.Browser.isMoz){return(_1.which||null)}
return(_1.which||_1.keyCode||null)}
,isc.A.determineEventKeyName=function isc_c_EventHandler_determineEventKeyName(_1){if(_1==null)return;var _2=_1.keyCode,_3=_1.which,_4=isc.EH,_5=_4.getKeyEventType(_1.type),_6=this.getWindow().event;if(_1.type==this.$lt)return this.$ls;if(isc.Browser.isIE){if(_5==_4.KEY_DOWN||_5==_4.KEY_UP){return _4.$ke[_2]}
if(_5==_4.KEY_PRESS){var _7=_4.$ku[_4.$ku.length-1];if(_7!=null)return _7;var _8=_4.$kp[_2];if(!_8&&_6&&_6.ctrlKey){_8=isc.EH.$ov(_2)}
return _8}}else if(isc.Browser.isMoz){if(_5==_4.KEY_DOWN||_5==_4.KEY_UP){return _4.$ke[_2]}else if(_5==_4.KEY_PRESS){if(_3==0&&_2!=0)return _4.$ke[_2];var _7=_4.$ku[_4.$ku.length-1];if(_7!=null)return _7;if(_2==0){return _4.$kp[_3]}else{return _4.$ke[_3]}}}else if(isc.Browser.isSafari){if(_5==_4.KEY_DOWN||_5==_4.KEY_UP){return _4.$ke[_2]}
var _9=(_3!=null?_3:_2);if(_9!=null&&_9!=0){if(_6&&_6.ctrlKey){var _10=isc.EH.$kp[_9];if(_10==null){if(_9==10)_10="Enter";else _10=isc.EH.$ov(_9)}
return _10}
var _10=isc.EH.$kp[_9];if(_10==null)_10=isc.EH.$kq[_9]
return _10}else if(_5==this.KEY_PRESS){return null}}else{var _9=_3;if(_9==null||(_9==0&&_2))_9=_2
if(_9!=null)return isc.EH.$kp[_9]}
isc.Log.logWarn("EventHandler.determineEventKeyName(): Unable to determine key for '"+_1.type+"' event. Returning null");return null}
,isc.A.$ov=function isc_c_EventHandler__getKeyNameFromCtrlCharValue(_1){if(_1==30)return"6";if(_1==31)return"-";return String.fromCharCode(_1+64)}
,isc.A.clearKeyEventProperties=function isc_c_EventHandler_clearKeyEventProperties(_1){var _2=this.lastEvent;delete _2.eventType;delete _2.nativeKeyTarget;delete _2.characterValue;delete _2.keyName;delete _2.shiftKey;delete _2.ctrlKey;delete _2.altKey;delete _2.metaKey;this.$ku.remove(_1)}
,isc.A.canvasDestroyed=function isc_c_EventHandler_canvasDestroyed(_1){if(this.clickMaskUp())isc.EH.maskTarget(_1);if(_1.$ow)isc.Page.clearEvent(_1.$ox,_1.$ow);if(this.mouseDownEvent&&this.mouseDownEvent.target==_1)
this.mouseDownEvent.target=null;if(this.lastClickTarget==_1)this.lastClickTarget=null;if(this.lastEvent.target==_1)this.lastEvent.target=null;if(this.lastEvent.keyTarget==_1)this.lastEvent.keyTarget=null;if(this.$l4==_1)this.$l4=null;if(this.$mx==_1)this.$mx=null}
,isc.A.showClickMask=function isc_c_EventHandler_showClickMask(_1,_2,_3,_4){var _5;if(_2==true){_5=true;_2=isc.EH.SOFT}else if(_2==false||_2==null){_5=false;_2=isc.EH.HARD}else{_5=(_2!=isc.EH.HARD)}
if(_3==null)_3=[];else if(!isc.isAn.Array(_3))_3=[_3]
var _6=this,_7=_6.clickMaskRegistry,_8=_6.getFocusCanvas();if(this.logIsInfoEnabled("clickMask")){this.logInfo("showing click mask, action: "+_1+(_5?", autoHide true ":", autoHide false ")+(_4?", ID: "+_4:"")+", focusCanvas: "+_8,"clickMask")}
if(_6.lastMoveTarget)_6.handleEvent(_6.lastMoveTarget,_6.MOUSE_OUT);delete _6.lastMoveTarget;var _9={autoHide:_5,mode:_2,ID:(_4!=null?_4:"cm_"+_6.$lu++),$oy:{}};this.$oz(_3,_9);var _10=_7.last();_7.add(_9);_9.clickAction=_1;if(_8!=null&&!_3.contains(_8)){_8.blur("showing clickMask");this.setMaskedFocusCanvas(_8,_9)}else if(_10!=null){this.setMaskedFocusCanvas(_10.$ne,_9)}
var _11=this.isHardMask(_9);if(_11){var _12=isc.timeStamp();var _13;if(_7.length>1){var _14=false,_15=[];for(var i=_7.length-2;i>=0;i--){_15.add(_7[i]);if(this.isHardMask(_7[i])){_14=true;break}}
if(_14){for(var i=0;i<_15.length;i++){var _17=_15[i].$oy;if(_17){this.$o0(_17,_3,true,true)}}}else{_13=true;this.$o0(isc.Canvas._canvasList,_3,false,true)}}else{_13=true;this.$o0(isc.Canvas._canvasList,_3,false,true)}}
if(this.maskNativeTargets){if(_10==null){this.showScreenSpan(_9)}else if(_11){this.$o1(_9.$oy)}}
this.updateEventMasks();return _9.ID}
,isc.A.updateEventMasks=function isc_c_EventHandler_updateEventMasks(){var _1=this.clickMaskRegistry,_2=_1?_1[_1.length-1]:null;if(_2&&_2.autoHide){var _3={};isc.addProperties(_3,_2.$oy);this.showEventMasks(false,_3)}else{this.hideEventMasks()}}
,isc.A.$oz=function isc_c_EventHandler__applyUnmaskedTargets(_1,_2){_1=this.$o2(_1,_2);for(var i=0;i<_1.length;i++){if(_1[i]==null)continue;_2.$oy[_1[i].getID()]=true}}
,isc.A.$o2=function isc_c_EventHandler__getFullSetOfTargetsToUnmask(_1,_2){if(!_1||_1.length==0||!_2)return _1;for(var i=0;i<_1.length;i++)
_1[i]=this.$o3(_1[i]);var _4=_2.$oy;if(!_2.autoHide&&_1.length>0){var _5=_1.length;for(var i=0;i<_5;i++){var _6=_1[i];if(_6.topElement&&!_4[_6.topElement.getID()]&&!_1.contains(_6.topElement))
{this.logWarn("Attempting to unmask target canvas:"+_6.getID()+" with respect to a hard click mask. "+"This is not a top level Canvas - all ancestors of "+"this Canvas will also be unmasked.","clickMask");_1.add(_6.topElement)}}}
this.$o4(_1);this.$o5(_1);return _1}
,isc.A.$o5=function isc_c_EventHandler__combineDescendantsIntoList(_1){var _2=_1.length;for(var i=0;i<_2;i++){if(_1[i]==null)continue;this.$o6(_1[i],_1)}}
,isc.A.$o6=function isc_c_EventHandler__addDescendantsToList(_1,_2,_3){if(_3&&!_2.contains(_1))_2.add(_1);if(_1.children){for(var i=0;i<_1.children.length;i++){this.$o6(_1.children[i],_2,true)}}
if(isc.DynamicForm&&isc.CanvasItem&&isc.isA.DynamicForm(_1)){var _5=_1.getItems()||[];for(var i=0;i<_5.length;i++){if(_5[i].containerWidget==_1)continue;if(isc.isA.CanvasItem(_5[i])&&isc.isA.Canvas(_5[i].canvas)){this.$o6(_5[i].canvas,_2,true)}}}}
,isc.A.$o4=function isc_c_EventHandler__combineTopPeersIntoList(_1){for(var i=0,_3=_1.length;i<_3;i++){var t=_1[i];if(t.parentElement&&_1.contains(t.parentElement))continue;this.$o7(_1[i],_1)}}
,isc.A.$o7=function isc_c_EventHandler__addPeersToList(_1,_2,_3){if(_3&&!_2.contains(_1))_2.add(_1);var _4=_1.peers;if(_4){for(var i=0;i<_4.length;i++){this.$o7(_4[i],_2,true)}}
this.$o6(_1,_2)}
,isc.A.getClickMask=function isc_c_EventHandler_getClickMask(_1){var _2=this.clickMaskRegistry;if(isc.isAn.Object(_1)){return _2.contains(_1)?_1:null}
return _2.find(this.$c3,_1)}
,isc.A.isHardMask=function isc_c_EventHandler_isHardMask(_1){if(!isc.isAn.Object(_1))_1=this.getClickMask(_1);return _1==null?false:(_1.mode==isc.EH.HARD)}
,isc.A.getTopHardMask=function isc_c_EventHandler_getTopHardMask(){var _1=this.clickMaskRegistry;for(var i=_1.length-1;i>=0;i--){if(this.isHardMask(_1[i]))return _1[i]}
return null}
,isc.A.$o0=function isc_c_EventHandler__hardMaskTargets(_1,_2,_3,_4){if(!_1)return;if(_3){for(var _5 in _1){var _6=this.$o3(_5);this.$o8(_6,_2,_4)}}else{for(var i=0;i<_1.length;i++){var _6=this.$o3(_1[i]);this.$o8(_6,_2,_4)}}}
,isc.A.$o8=function isc_c_EventHandler__hardMaskTarget(_1,_2,_3){if(!isc.isA.Canvas(_1)||_1.destroyed){isc.Log.logWarn("showClickMask - attempting to remove invalid object :"+isc.Log.echo(_1)+" from tab order","clickMask");return}
if(_2&&_2[_1.getID()])return;if(_1.isDrawn()){if(_1.parentElement==null){if(!_3&&this.$o9&&this.$o9.isDrawn()&&_1.getZIndex()>=this.$o9.getZIndex())
{this.logDebug("lowering zIndex of: "+_1,"clickMask");_1.setZIndex(isc.EH.$o9.getZIndex()-1)}}}
if(_1.accessKey!=null&&_1.isDrawn()){_1.$pa(null)}}
);isc.evalBoundary;isc.B.push(isc.A.hideClickMask=function isc_c_EventHandler_hideClickMask(_1){if(this.logIsInfoEnabled("clickMask"))
this.logInfo("hideClickMask called with ID: "+_1,"clickMask");var _2=this.clickMaskRegistry;if(_2.length==0)return;if(_1==null){this.hideClickMask(_2[0].ID)
if(_2.length>0){this.hideClickMask()}else{this.logInfo("all clickmasks hidden","clickMask")}
return}
var _3=this.getClickMask(_1);if(_3==null)return;var _4=_2.indexOf(_3),_5=(_4==(_2.length-1)),_6=this.isHardMask(_3),_7=(_4>0?_2[_4-1]:null),_8,_9;if(this.logIsInfoEnabled("clickMask")){var _10="hiding clickMask ID: "+_1;if(_6)_10+="[autoHide:false]";else _10+="[autoHide:true]";if(_2.length<2){_10+=", all masks hidden"}else{_10+=" with index: "+_4+" of "+(_2.length-1)}
this.logInfo(_10,"clickMask")}
if(_6){_9=this.$pb(_4,false);var _11=this.$pb(_4,true);_8=(_11==null)}
var _12=_3.$ne,_13=_3.$oy;_2.remove(_3);if(_7!=null){if(_13!=null){if(_7.$oy==null)_7.$oy={};isc.addProperties(_7.$oy,_13)}
if(_12&&!_7.$oy[_12.getID()]){this.setMaskedFocusCanvas(_12,_7)}}
if(this.$o9){if(_5&&_7==null){if(isc.Browser.isIE){isc.Timer.setTimeout({target:this.$o9,methodName:"hide"},0)}else{this.$o9.hide()}}else if(_8){if(_9){var _13=isc.addProperties({},_9.$oy);var _14=_2.length-1,_15=_2[_14];while(_15!=_9){isc.addProperties(_13,_15.$oy);_14--;_15=_2[_14]}
this.$o1(_13)}else this.$o9.sendToBack()}
if(_6){var _16;if(_9!=null){_16=[];for(var i=_4-1;i>=0;i--){var _18=_2[i];_16.addList(isc.getKeys(_18.$oy));if(_18==_9)break}}else{_16=isc.Canvas._canvasList}
this.$pc(_16,true)}
if(_12!=null&&!_12.destroyed&&!this.targetIsMasked(_12)){if(this.logIsInfoEnabled("clickMask")){this.logInfo("focusing in "+_12+" on clickMask hide "+"with current focusCanvas: "+isc.EH.$l4,"clickMask")}
var _19=(isc.Browser.isIE&&this.lastEvent.eventType==this.MOUSE_DOWN)
if(_19){this.$mx=_12}else{try{_12.focus()}catch(e){}}}}
this.updateEventMasks()}
,isc.A.$pb=function isc_c_EventHandler__getNextHardMask(_1,_2){var _3=this.clickMaskRegistry;if(_2){for(var i=_1+1;i<_3.length;i++){if(this.isHardMask(_3[i]))return _3[i]}}else{for(var i=_1-1;i>=0;i--){if(this.isHardMask(_3[i]))return _3[i]}}
return null}
,isc.A.$o3=function isc_c_EventHandler__getCanvas(_1){if(isc.isA.String(_1))return window[_1];return _1}
,isc.A.$pc=function isc_c_EventHandler__hardUnmaskTargets(_1,_2){if(!_1||_1.length==0)return;for(var i=0;i<_1.length;i++){var _4=this.$o3(_1[i]);if(!_4)continue;if(_4.accessKey!=null&&_4.isDrawn()){_4.$pa(_4.accessKey)}
if(!_4.isDrawn()&&isc.isA.DynamicForm&&isc.isA.DynamicForm(_4)&&_4.items&&_4.items.length>0)
{var _5=_4.items[0];if(_5.containerWidget!=_4)_4=_5.containerWidget}
if(!_2&&_4.parentElement==null&&_4.getZIndex()<=this.$o9.getZIndex()&&_4!=this.$o9)
{_4.setZIndex(this.$o9.getZIndex()+1);this.logDebug("raised above screenspan: "+_4,"clickMask")}}}
,isc.A.clickMaskUp=function isc_c_EventHandler_clickMaskUp(_1){var _2=this.clickMaskRegistry;if(_1==null)return(_2.length>0);else return(_2.find("ID",_1)!=null)}
,isc.A.getAllClickMaskIDs=function isc_c_EventHandler_getAllClickMaskIDs(){var _1=this.clickMaskRegistry;if(_1.length<1)return[];return _1.getProperty("ID")}
,isc.A.showScreenSpan=function isc_c_EventHandler_showScreenSpan(_1){if(!this.$o9){this.$o9=isc.ScreenSpan.create({ID:"isc_EH_screenSpan",pointersToThis:[{object:this,property:"$o9"}]},this.clickMaskProperties)}
var _2=this.$o9;_2.show();if(!this.isHardMask(_1)){_2.sendToBack()}else{this.$o1(_1.$oy)}}
,isc.A.$o1=function isc_c_EventHandler__adjustSpanZIndex(_1){this.$pd=true;var _2;for(var _3 in _1){var _4=this.$o3(_3);if(!_4||_4.destroyed||_4.parentElement!=null){continue}
if(_4.masterElement&&_1[_4.masterElement.getID()])continue;_4.bringToFront();if(_2==null)_2=_4.getZIndex(true);if(_4.peers){for(var i=0;i<_4.peers.length;i++){if(!_4.peers[i].isDrawn())continue;_2=Math.min(_2,_4.peers[i].getZIndex(true))}}}
if(_2!=null)this.$o9.setZIndex(_2-1);else this.$o9.bringToFront();this.$pd=false}
,isc.A.maskTarget=function isc_c_EventHandler_maskTarget(_1,_2){return this.maskTargets(_1,_2)}
,isc.A.maskTargets=function isc_c_EventHandler_maskTargets(_1,_2,_3){var _4=this.clickMaskRegistry;if(_1==null||_4.length==0)return;if(!isc.isAn.Array(_1))_1=[_1];else if(_1.length==0)return;var _5=(_2==null?_4[0]:(isc.isA.String(_2)?this.getClickMask(_2):_2));if(_5==null){this.logInfo("maskTargets called with invalid maskID - returning.","event")
return}
var _6=_1.length
for(var i=0;i<_6;i++){var _8=_1[i];if(!_3&&_8.children!=null){this.$o6(_8,_1)}
var _9=_8.parentElement;while(_9!=null){if(!_1.contains(_9)){_1.add(_9);if(_3)this.$o7(_9,_1)}
_9=_9.parentElement}
if(_3||!_9){this.$o7(_8,_1)}
if(_9)this.$o7(_9,_1)}
var _10=_4.indexOf(_5);var _11;for(var i=_10;i<_4.length;i++){_14=_4[i];if(this.isHardMask(_14))_11=i}
var _12;if(_11!=null)_12=[];for(var n=0;n<_1.length;n++){var _8=_1[n];if(_8.hasFocus)_8.blur();var _14,_11,_15=null;for(var i=_10;i<_4.length;i++){_14=_4[i];if(_14.$oy[_8.getID()]){_15=i;delete _14.$oy[_8.getID()]}}
if(_11!=null&&_15!=null&&(_11<=_15)){_12.add(_8)}}
if(_11!=null)this.$o0(_12,null,false,false)}
,isc.A.addUnmaskedTarget=function isc_c_EventHandler_addUnmaskedTarget(_1,_2){return this.addUnmaskedTargets(_1,_2)}
,isc.A.addUnmaskedTargets=function isc_c_EventHandler_addUnmaskedTargets(_1,_2){if(isc.$pe&&this.$pd)return;var _3=this.clickMaskRegistry;if(_1==null||_3.length==0)return;if(!isc.isAn.Array(_1))_1=[_1];if(_1.length==0)return;var _4;if(_2==null){_4=_3.last()}else{if(isc.isA.String(_2))_4=this.getClickMask(_2);else _4=_2}
if(_4==null){this.logInfo("addUnmaskedTargets called with invalid maskID - returning.","clickMask")
return}
var _5=_4;while(_5&&!this.isHardMask(_5)){_5=_3[_3.indexOf(_5)-1]}
if(_5!=null){if(isc.$pe&&_1.length==1&&_1[0].topElement!=null){return}}
_1=this.$o2(_1,_4);if(this.logIsDebugEnabled("clickMask")){this.logDebug("Added unmasked targets:"+_1.getProperty("ID")+" [+ decendants] to clickMask with ID: "+_4.ID,"clickMask")}
var _6=false;for(var i=_3.indexOf(_4)+1;i<_3.length;i++){if(this.isHardMask(_3[i]))_6=true}
for(var n=0;n<_1.length;n++){var _9=_1[n];if(_4.$oy==null)_4.$oy={};_4.$oy[_9.getID()]=true}
if(!_6){this.$pc(_1)}}
,isc.A.targetIsMasked=function isc_c_EventHandler_targetIsMasked(_1,_2,_3){var _4=this.clickMaskRegistry;if(_4.length==0)return false;if(_1==null)return true;var _5;if(_2==null)_5=_4.last();else if(isc.isA.String(_2))_5=_4.find("ID",_2);else _5=_2;if(!isc.isAn.Object(_5)){this.logWarn("EventHandler.targetIsMasked() passed invalid maskID:"+_2,"clickMask");return false}
var _6=_4.indexOf(_5);var _7=false;for(var i=_6;i<_4.length;i++){if(i!=_6)_5=_4[i];if(_3){if(_5.mode==isc.EH.HARD||_5.mode==isc.EH.SOFT_CANCEL){_7=true}else{continue}}
if(_5.$oy){if(_5.$oy[_1.getID()])return false;if(isc.isA.DynamicForm!=null&&isc.isA.DynamicForm(_1)){var _9=_1.$pf(isc.EH.lastEvent);if(_9&&_9.item&&_9.item.form==_1&&_9.item.containerWidget!=_1&&_5.$oy[_9.item.containerWidget.getID()])return false}}}
return(_3&&!_7?false:true)}
,isc.A.clickMaskClick=function isc_c_EventHandler_clickMaskClick(_1){var _2=this.clickMaskRegistry.duplicate(),_3=_2.last();while(_3!=null&&(this.targetIsMasked(_1)||_1==this.$o9)){if(this.logIsInfoEnabled("clickMask")){this.logInfo("mouseDown on masked "+_1+(_3.clickAction!=null?" firing clickAction, ":"")+(_3.autoHide?"will hide mask"+(_3.mode==isc.EH.SOFT_CANCEL?" and block click":""):"will block click"))}
var _4=(_3.mode!=isc.EH.SOFT);this.$nf(_3)
if(_4)return false;_3=_2[_2.indexOf(_3)-1]}
return true}
,isc.A.$nf=function isc_c_EventHandler__clickMaskClick(_1){var _2=_1.autoHide,_3=_1.clickAction;if(_2==true)this.hideClickMask(_1.ID);if(_3!=null)this.fireCallback(_3)}
);isc.B._maxIndex=isc.C+171;isc.EventHandler.captureEvents();isc.ClassFactory.defineClass("Element",null,null,true);isc.A=isc.Element;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$pg=window.isc_insertAfterBodyStart;isc.A.$ph="isc_global_insertion_marker";isc.A.$pi="afterBegin";isc.A.$pj="afterEnd";isc.A.$pk="beforeBegin";isc.A.$pl="beforeEnd";isc.A.$pm=(isc.Browser.isIE&&!isc.Browser.isIE9)||isc.Browser.isOpera;isc.A.$pn=isc.Browser.isMoz?"border-left-width":"borderLeftWidth";isc.A.$po=isc.Browser.isMoz?"border-top-width":"borderTopWidth";isc.A.$pp=isc.Browser.isMoz?"margin-left":"marginLeft";isc.A.$pq=isc.Browser.isMoz?"margin-top":"marginTop";isc.A.$pr="none";isc.A.cacheCount=0;isc.A.uncachedCount=0;isc.A.$ps="$ps";isc.A.$pt="$pt";isc.A.cacheOffsetCoords=true;isc.A.$pu={};isc.A.$pv={border:"borderStyle",borderWidth:"borderStyle",borderLeft:"borderLeftStyle",borderRight:"borderRightStyle",borderTop:"borderTopStyle",borderBottom:"borderBottomStyle",borderLeftWidth:"borderLeftStyle",borderRightWidth:"borderRightStyle",borderBottomWidth:"borderBottomStyle",borderTopWidth:"borderTopStyle"};isc.A.$pw={};isc.A.$px=";";isc.A.vendorCSSPrefix=(isc.Browser.isMoz?"-moz-":isc.Browser.isSafari?"-webkit-":isc.Browser.isOpera?"-o-":"");isc.B.push(isc.A.get=function isc_c_Element_get(_1,_2){_2=_2||this.getDocument();if(isc.Browser.isDOM)return _2.getElementById(_1)}
,isc.A.$of=function isc_c_Element__getElementFromSelection(_1){if(!_1)_1=document;if(isc.Browser.isIE){var _2=_1.selection,_3=_2.type.toLowerCase(),_4=(_3=="text"||_3=="none");if(!_2)return null;if(_4){var _5;try{_5=_2.createRange()}catch(e){}
return _5?_5.parentElement():null}else{var _5=_2.createRange(),_6;for(var i=0;i<_5.length;i++){if(!_6){_6=_5(i).parentElement}else{while(!_6.contains(_5(i))){_6=_6.parentElement}}}
return _6}}}
,isc.A.findAttribute=function isc_c_Element_findAttribute(_1,_2,_3){if(!_1)return null;if(_1[_2]==_3||(_1.getAttribute&&_1.getAttribute(_2)==_3)){return _1}
var _4=_1.childNodes;for(var i=0;i<_4.length;i++){var _6=this.findAttribute(_4[i],_2,_3);if(_6)return _6}
return null}
,isc.A.getInsertionMarkerHTML=function isc_c_Element_getInsertionMarkerHTML(){return"<span id='"+this.$ph+"' style='display:none'></span>"}
,isc.A.getInsertionMarker=function isc_c_Element_getInsertionMarker(){return document.getElementById(this.$ph)}
,isc.A.createAbsoluteElement=function isc_c_Element_createAbsoluteElement(_1,_2){var _3=_2||this.getWindow(),_4=this.getDocumentBody(true);if(_4==null&&!isc.Element.noBodyTagMessageShown){isc.Element.noBodyTagMessageShown=true;var _5="Error: Attempt to write content into a page outside the BODY tag.  Isomorphic "+"SmartClient requires this tag be present and all widgets be written out inside "+"it.\r"+"Please ensure your file has a BODY tag and any code to draw SmartClient widgets "+"is enclosed in this tag.";this.logError(_5);return}
if(this.$pg){return isc.Element.insertAdjacentHTML(_4,this.$pi,_1,true)}
if(isc.Browser.isIE){if(!this.$py){if(_4.childNodes.length<2){isc.Element.insertAdjacentHTML(_4,this.$pi,this.getInsertionMarkerHTML())}else{var _6=_4.lastChild;while(_6&&_6.nodeType==3)_6=_6.previousSibling;if(_6!=null){isc.Element.insertAdjacentHTML(_6,this.$pk,this.getInsertionMarkerHTML())}else{isc.Element.insertAdjacentHTML(_4,this.$pi,this.getInsertionMarkerHTML())}}
this.$py=this.getInsertionMarker()}
return isc.Element.insertAdjacentHTML(this.$py,this.$pj,_1,true)}else{return isc.Element.insertAdjacentHTML(_4,this.$pl,_1,true)}}
,isc.A.insertAdjacentHTML=function isc_c_Element_insertAdjacentHTML(_1,_2,_3,_4){if(isc.isA.String(_1))_1=isc.Element.get(_1);if(!_1)this.logWarn("insertAdjacentHTML: element is null for where: '"+_2+"' with html: "+_3);if(isc.Browser.isIE||isc.Browser.isOpera){_1.insertAdjacentHTML(_2,_3);return}
var _5;if(_4){var _6=_1.ownerDocument.createElement("DIV");_6.innerHTML=_3;_5=_6.firstChild}else{var _7=_1.ownerDocument.createRange();_7.setStartBefore(_1);_5=_7.createContextualFragment(_3)}
switch(_2){case"beforeBegin":_1.parentNode.insertBefore(_5,_1)
break;case"afterBegin":_1.insertBefore(_5,_1.firstChild);break;case"beforeEnd":_1.appendChild(_5);break;case"afterEnd":if(_1.nextSibling)_1.parentNode.insertBefore(_5,_1.nextSibling);else _1.parentNode.appendChild(_5);break}
if(_4)return _5}
,isc.A.clear=function isc_c_Element_clear(_1,_2){if(_1==null)return;if(!_2&&isc.Page.isLoaded()&&isc.Browser.isIE){_1.outerHTML=isc.emptyString;return}
if(_1.parentNode){_1.parentNode.removeChild(_1)}else{isc.Log.logWarn("element parentNode null");_1.innerHTML=""}}
,isc.A.isBorderBox=function isc_c_Element_isBorderBox(_1){if(!_1)return;if(!isc.Browser.isMoz)return isc.Browser.isBorderBox;return(_1.style.MozBoxSizing=="border-box")}
,isc.A.getScrollHeight=function isc_c_Element_getScrollHeight(_1){if(_1==null)return 0;var _2=((_1.scrollHeight!=null&&_1.scrollHeight!="undefined")?_1.scrollHeight:_1.offsetHeight);var _3=this.$pz(_1);return _3>_2?_3:_2}
,isc.A.$pz=function isc_c_Element__getPositionedChildrenBottom(_1){if(_1.childNodes==null)return 0;var _2=0,_3=document.ELEMENT_NODE||1,_4=this.logIsDebugEnabled("sizing");for(var i=0;i<_1.childNodes.length;i++){var _6=_1.childNodes.item(i);if(_6.nodeType!=_3)continue;var _7=isc.Element.getComputedStyleAttribute(_6,"position");var _8=0;if(_7==isc.Canvas.ABSOLUTE||_7==isc.Canvas.RELATIVE){_8+=isc.Element.getOffsetTop(_6)}else{continue}
var _9=_6.getAttribute("eventProxy"),_10;if(_9!=null&&!isc.isAn.emptyString(_9)&&!window[_9].$p0&&isc.isA.Function(window[_9].getVisibleHeight))
{_10=window[_9].getVisibleHeight()}else{_10=isc.Element.getVisibleHeight(_6)}
var _11=_8+_10;if(_7==isc.Canvas.ABSOLUTE&&(_1.style.overflow==isc.Canvas.SCROLL||_1.style.overflow==isc.Canvas.AUTO||_1.style.overflow==isc.Canvas.HIDDEN))
_11-=isc.Element.getBottomMargin(_6);if(_11>_2)_2=_11}
return _2}
,isc.A.getScrollWidth=function isc_c_Element_getScrollWidth(_1){if(_1==null)return 0;var _2=((_1.scrollWidth!=null&&_1.scrollWidth!="undefined")?_1.scrollWidth:_1.offsetWidth);var _3=this.$p1(_1);return _3>_2?_3:_2}
,isc.A.$p1=function isc_c_Element__getPositionedChildrenRight(_1){if(_1.childNodes==null)return 0;var _2=0,_3=document.ELEMENT_NODE||1,_4=this.logIsDebugEnabled("sizing");for(var i=0;i<_1.childNodes.length;i++){var _6=_1.childNodes.item(i);if(_6.nodeType!=_3)continue;var _7=isc.Element.getComputedStyle(_6,["position","display","left"]);var _8=0;if(_7.position==isc.Canvas.ABSOLUTE||_7.position==isc.Canvas.RELATIVE)
{_8=isc.Element.getOffsetLeft(_6)}else{continue}
var _9=_6.getAttribute("eventProxy"),_10;if(_9!=null&&!isc.isAn.emptyString(_9)&&!window[_9].$p2&&isc.isA.Function(window[_9].getVisibleWidth))
{_10=window[_9].getVisibleWidth()}else{_10=isc.Element.getVisibleWidth(_6)}
var _11=_8+_10;if(_1.style.overflow==isc.Canvas.SCROLL||_1.style.overflow==isc.Canvas.HIDDEN||_1.style.overflow==isc.Canvas.AUTO){_11-=isc.Element.getRightMargin(_6)}
if(_11>_2)_2=_11;if(_4){this.logInfo("getChildNodesRight: child node "+i+" of "+_1.childNodes.length+" ("+this.echoLeaf(_6)+")"+" left:"+_8+", width: "+_10+", right:"+_11,"sizing")}}
return _2}
,isc.A.getElementRect=function isc_c_Element_getElementRect(_1){var _2=this.getDocumentBody(),_3=this.getLeftOffset(_1,_2),_4=this.getTopOffset(_1,_2);var _5=0,_6=0;if(_1.style&&_1.style.overflow=="visible"){_5=this.getScrollWidth(_1);_6=this.getScrollHeight(_1)}
_5=Math.max(_1.offsetWidth,_1.clientWidth,_5);_6=Math.max(_1.offsetHeight,_1.clientHeight,_6);return[_3,_4,_5,_6]}
,isc.A.getInnerWidth=function isc_c_Element_getInnerWidth(_1){var _2=_1.style.width;if(_2!=null&&!isc.isAn.emptyString(_2)){_2=parseInt(_2);if(isc.isA.Number(_2))return _2}
var _3=_1.clientWidth,_4=parseInt(this.getComputedStyleAttribute("paddingLeft")),_5=parseInt(this.getComputedStyleAttribute("paddingRight")),_6=_4+_5;if(isc.isA.Number(_6))_3-=_6;return _3}
,isc.A.getInnerHeight=function isc_c_Element_getInnerHeight(_1){var _2=_1.style.height;if(_2!=null&&!isc.isAn.emptyString(_2)){_2=parseInt(_2);if(isc.isA.Number(_2))return _2}
var _3=_1.clientHeight,_4=parseInt(this.getComputedStyleAttribute("paddingTop")),_5=parseInt(this.getComputedStyleAttribute("paddingBottom")),_6=_4+_5;if(isc.isA.Number(_6))_3-=_6;return _3}
,isc.A.getNativeInnerWidth=function isc_c_Element_getNativeInnerWidth(_1){if(isc.Browser.isMoz)return this.getInnerWidth(_1);var _2=_1.offsetWidth;if(!_2)_2=this.getInnerWidth(_1);return _2}
,isc.A.getNativeInnerHeight=function isc_c_Element_getNativeInnerHeight(_1){if(isc.Browser.isMoz)return this.getInnerHeight(_1);var _2=_1.offsetHeight;if(!_2)_2=this.getInnerHeight(_1);return _2}
,isc.A.getTopMargin=function isc_c_Element_getTopMargin(_1){if(_1!=null){var _2;if(_1.style!=null)_2=parseInt(_1.style.marginTop);if(isc.isA.Number(_2))return _2;if(_1.className!=null)return isc.Element.$p3(_1.className)}
return 0}
,isc.A.getBottomMargin=function isc_c_Element_getBottomMargin(_1){if(_1!=null){var _2;if(_1.style!=null)_2=parseInt(_1.style.marginBottom);if(isc.isA.Number(_2))return _2;if(_1.className!=null)return isc.Element.$p4(_1.className)}
return 0}
,isc.A.getLeftMargin=function isc_c_Element_getLeftMargin(_1){if(_1!=null){var _2;if(_1.style!=null)_2=parseInt(_1.style.marginLeft);if(isc.isA.Number(_2))return _2;if(_1.className!=null)return isc.Element.$p5(_1.className)}
return 0}
,isc.A.getRightMargin=function isc_c_Element_getRightMargin(_1){if(_1!=null){var _2;if(_1.style!=null)_2=parseInt(_1.style.marginRight);if(isc.isA.Number(_2))return _2;if(_1.className!=null)return isc.Element.$p6(_1.className)}
return 0}
,isc.A.getHMarginSize=function isc_c_Element_getHMarginSize(_1){return isc.Element.getLeftMargin(_1)+isc.Element.getRightMargin(_1)}
,isc.A.getVMarginSize=function isc_c_Element_getVMarginSize(_1){return isc.Element.getTopMargin(_1)+isc.Element.getBottomMargin(_1)}
,isc.A.getTopBorderSize=function isc_c_Element_getTopBorderSize(_1){if(_1==null)return 0;if(isc.Browser.isOpera&&_1.currentStyle.borderTopStyle==this.$pr)return 0;var _2=(this.$pm?parseInt(_1.currentStyle.borderTopWidth):parseInt(isc.Element.getComputedStyleAttribute(_1,"borderTopWidth")));return isNaN(_2)?0:_2}
,isc.A.getBottomBorderSize=function isc_c_Element_getBottomBorderSize(_1){if(_1==null)return 0;if(isc.Browser.isOpera&&_1.currentStyle.borderBottomStyle==this.$pr)return 0;var _2=(this.$pm?parseInt(_1.currentStyle.borderBottomWidth):parseInt(isc.Element.getComputedStyleAttribute(_1,"borderBottomWidth")));return isNaN(_2)?0:_2}
,isc.A.getLeftBorderSize=function isc_c_Element_getLeftBorderSize(_1){if(_1==null)return 0;if(isc.Browser.isOpera&&_1.currentStyle.borderLeftStyle==this.$pr)return 0;var _2=(this.$pm?parseInt(_1.currentStyle.borderLeftWidth):parseInt(isc.Element.getComputedStyleAttribute(_1,"borderLeftWidth")));return isNaN(_2)?0:_2}
,isc.A.getRightBorderSize=function isc_c_Element_getRightBorderSize(_1){if(_1==null)return 0;if(isc.Browser.isOpera&&_1.currentStyle.borderRightStyle==this.$pr)return 0;var _2=(this.$pm?parseInt(_1.currentStyle.borderRightWidth):parseInt(isc.Element.getComputedStyleAttribute(_1,"borderRightWidth")));return isNaN(_2)?0:_2}
,isc.A.getVBorderSize=function isc_c_Element_getVBorderSize(_1){return isc.Element.getTopBorderSize(_1)+isc.Element.getBottomBorderSize(_1)}
,isc.A.getHBorderSize=function isc_c_Element_getHBorderSize(_1){return isc.Element.getLeftBorderSize(_1)+isc.Element.getRightBorderSize(_1)}
,isc.A.getVisibleWidth=function isc_c_Element_getVisibleWidth(_1){if(_1==null)return 0;var _2=isc.Element.getComputedStyleAttribute(_1,"overflow"),_3;if(_2==isc.Canvas.VISIBLE||!isc.isA.Number(parseInt(_1.style.width))){_3=isc.Element.getScrollWidth(_1)+isc.Element.getHBorderSize(_1)}else{_3=parseInt(_1.style.width)}
return _3+isc.Element.getHMarginSize(_1)}
,isc.A.getVisibleHeight=function isc_c_Element_getVisibleHeight(_1){if(_1==null)return 0;var _2=isc.Element.getComputedStyleAttribute(_1,"overflow"),_3;if(_2==isc.Canvas.VISIBLE||!isc.isA.Number(parseInt(_1.style.height))){_3=isc.Element.getScrollHeight(_1)+isc.Element.getVBorderSize(_1)}else{_3=parseInt(_1.style.height)}
return _3+isc.Element.getVMarginSize(_1)}
,isc.A.getOffsetLeft=function isc_c_Element_getOffsetLeft(_1){if(_1==null){this.logWarn("getOffsetLeft: passed null element");return 0}
var _2=_1.offsetLeft;if(isc.Browser.isIE&&isc.Page.isRTL()&&_2<0){_2=-_2}
if(_1.$p7==_2){return _1.$p8}else{}
var _3=parseInt(isc.Element.getComputedStyleAttribute(_1,"marginLeft"));if(isc.isA.Number(_3)&&_3>0){_2-=_3}
var _4=this.getDocumentBody(),_5,_6="px",_7=_1.style.position;if(isc.Browser.isMoz){if(_1.offsetParent==null)return _2;if(_1.offsetParent!=_4){_5=this.ns.Element.getComputedStyle(_1.offsetParent,["borderLeftWidth","overflow"]);var _8=isc.Browser.geckoVersion,_9=(_5.overflow!="visible")&&(_8>=20051111||(_7==isc.Canvas.ABSOLUTE&&_5.overflow!="hidden")),_10=(_8>20020826&&(_1.offsetParent.style.MozBoxSizing=="border-box"));if(_10!=_9){if(_10){_2-=(isc.isA.Number(parseInt(_5.borderLeftWidth))?parseInt(_5.borderLeftWidth):0)}
if(_9){_2+=(isc.isA.Number(parseInt(_5.borderLeftWidth))?parseInt(_5.borderLeftWidth):0)}}}}
if(isc.Browser.isIE&&!isc.Browser.isIE8Strict&&!isc.Browser.isIE9){var _11=_1.offsetParent,_5;if(_5!=_4)_5=_11.currentStyle;var _12=(_1.currentStyle.height!=isc.Canvas.AUTO||_1.currentStyle.width!=isc.Canvas.AUTO);var _13=true;while(_11!=_4){if(_5.position==isc.Canvas.ABSOLUTE)_13=false;if(_5.width==isc.Canvas.AUTO&&_5.height==isc.Canvas.AUTO&&_5.position==isc.Canvas.RELATIVE){if(_13&&isc.isA.String(_5.borderLeftWidth)&&_5.borderLeftWidth.contains(_6)){_2-=parseInt(_5.borderLeftWidth)}
if(_12){if(isc.isA.String(_5.marginLeft)&&_5.marginLeft.contains(_6))
{var _14=parseInt(_5.marginLeft);if(_14>0)_2-=_14}
if(_11.offsetParent!=_4){var _15=_11.offsetParent.currentStyle.padding;if(isc.isA.String(_15)&&_15.contains(_6)){_2-=parseInt(_15)}}else{_2-=(_4.leftMargin?parseInt(_4.leftMargin):0)}}}
_7=_11.style.position;_11=_11.offsetParent;if(_11!=document.body){_5=_11.currentStyle}}}
if(isc.Browser.isSafari&&isc.Browser.safariVersion<525.271){if(_1.offsetParent!=null&&_1.offsetParent!=_4){var _16=this.ns.Element.getComputedStyle(_1.offsetParent,["borderLeftWidth"]).borderLeftWidth;if(_16!=null)_16=parseInt(_16);if(isc.isA.Number(_16))_2-=_16}}
_1.$p7=_1.offsetLeft;_1.$p8=_2;return _2}
,isc.A.getOffsetTop=function isc_c_Element_getOffsetTop(_1){if(_1==null){this.logWarn("getOffsetTop: passed null element");return 0}
var _2=_1.offsetTop;if(_1.$p9==_2){return _1.$qa}else{}
var _3=parseInt(isc.Element.getComputedStyleAttribute(_1,"marginTop"));if(isc.isA.Number(_3)&&_3>0){_2-=_3}
var _4=this.getDocumentBody(),_5,_6="px",_7=_1.style.position;if(isc.Browser.isMoz){if(_1.offsetParent==null)return _2;if(_1.offsetParent!=_4){_5=this.ns.Element.getComputedStyle(_1.offsetParent,["overflow","borderTopWidth"]);var _8=(_5.overflow!="visible")&&(isc.Browser.geckoVersion>=20051111||(_7==isc.Canvas.ABSOLUTE&&_5.overflow!="hidden")),_9=(isc.Browser.geckoVersion>20020826&&_1.offsetParent.style.MozBoxSizing=="border-box");if(_9!=_8){if(_9){_2-=(isc.isA.Number(parseInt(_5.borderTopWidth))?parseInt(_5.borderTopWidth):0)}
if(_8){_2+=(isc.isA.Number(parseInt(_5.borderTopWidth))?parseInt(_5.borderTopWidth):0)}}}}
if(isc.Browser.isIE&&!isc.Browser.isIE9){if(_1.offsetParent&&_1.offsetParent!=_4){_5=_1.offsetParent.currentStyle;if(_5.position==isc.Canvas.RELATIVE&&_5.height==isc.Canvas.AUTO&&_5.width==isc.Canvas.AUTO&&isc.isA.String(_5.borderTopWidth)&&_5.borderTopWidth.contains(_6)){_2-=parseInt(_5.borderTopWidth)}}}
if(isc.Browser.isSafari&&isc.Browser.safariVersion<525.271){if(_1.offsetParent&&_1.offsetParent!=_4){var _10=this.ns.Element.getComputedStyle(_1.offsetParent,["borderTopWidth"]).borderTopWidth;if(_10!=null)_10=parseInt(_10);if(isc.isA.Number(_10))_2-=_10}}
_1.$p9=_1.offsetTop;_1.$qa=_2;return _2}
,isc.A.getLeftOffset=function isc_c_Element_getLeftOffset(_1,_2,_3,_4){return this.getOffset(isc.Canvas.LEFT,_1,_2,_3,_4)}
,isc.A.getTopOffset=function isc_c_Element_getTopOffset(_1,_2,_3){return this.getOffset(isc.Canvas.TOP,_1,_2,null,_3)}
,isc.A.getOffset=function isc_c_Element_getOffset(_1,_2,_3,_4,_5){var _6=_5||isc.isA.Canvas(_2),_7=_5||_3==null||isc.isA.Canvas(_3);var _8=_6&&_7&&this.cacheOffsetCoords&&(_2.cacheOffsetCoords!=false);var _9=(_1==isc.Canvas.LEFT)?this.$ps:this.$pt;if(_8&&_2[_9]!=null){var _10=_2[_9][_3?_3.ID:this.$pr];if(_10!=null){this.cacheCount++;return _10}}
this.uncachedCount++;var _11=_6?_2.getClipHandle():_2;var _12;if(_3==null)_12=this.getDocumentBody();else if(_7)_12=_3.getHandle();else _12=_3;if(_12==null||_11==null){return 0}
var _13=_11.offsetParent;if(isc.Browser.isMoz&&_13==null)return 0;var _14=_12.offsetParent,_15=_11,_16=0,_17=(_1==isc.Canvas.LEFT),_18=(_17?this.$pn:this.$po),_19=(_17?this.$pp:this.$pq);if(!_17)_4=false;else if(_4==null)_4=(isc.Page.getTextDirection()==isc.Canvas.RTL);var _20=0;while(_13!=_12&&_13!=_14){var _21=(_17?this.ns.Element.getOffsetLeft(_15):this.ns.Element.getOffsetTop(_15));_16+=_21;if(!_4){_16-=((_17?_13.scrollLeft:_13.scrollTop)||0)}else{if(isc.isA.Number(_13.scrollLeft)){var _22=(_13.scrollWidth-_13.clientWidth);_16+=(_22-_13.scrollLeft)}}
var _23,_24,_25;if(this.$pm){_23=_13.currentStyle;if(isc.Browser.isOpera&&(_17?_23.borderLeftStyle==this.$pr:_23.borderTopStyle==this.$pr))_24=null;else _24=parseInt(_23[_18]);if(isc.isA.Number(_24))_16+=_24;_25=parseInt(_23[_19]);if(isc.isA.Number(_25)&&_25>0)_16+=_25}else if(isc.Browser.isMoz){_23=document.defaultView.getComputedStyle(_13,null);_24=parseInt(_23.getPropertyValue(_18));_16+=_24;_25=parseInt(_23.getPropertyValue(_19));if(_25>0)_16+=_25}else{_24=parseInt(this.getComputedStyleAttribute(_13,_18));if(isc.isA.Number(_24))_16+=_24;_25=parseInt(this.getComputedStyleAttribute(_13,_19));if(isc.isA.Number(_25)&&_25>0)_16+=_25}
_15=_13;_13=_15.offsetParent;_20++}
_16+=(_17?this.ns.Element.getOffsetLeft(_15):this.ns.Element.getOffsetTop(_15));if(_13==_14){_16-=(_17?this.ns.Element.getOffsetLeft(_12):this.ns.Element.getOffsetTop(_12))}
if(_8){var _26=_2[_9]=_2[_9]||{};_26[_3?_3.ID:this.$pr]=_16}
return _16}
,isc.A.getStyleEdges=function isc_c_Element_getStyleEdges(_1){if(isc.Browser.isSafari&&!isc.Element.$qb){isc.Browser.isStrict=isc.Element.$qc();isc.Element.$qb=true}
if(_1==null)return null;var _2;if(this.$pu[_1]!==_2)return this.$pu[_1];var _3=(isc.Browser.isMoz&&isc.Browser.geckoVersion<20040616),_4;if(_3){_4=this.getStyleDeclaration(_1)}else{var _5=isc.Browser.isIE?this.$qd:this.$qe;_4=this.$qf(_1,_5)}
this.$pu[_1]=_4;return _4}
,isc.A.$qc=function isc_c_Element__testForSafariStrictMode(){if(document.compatMode!=null){return document.compatMode=="CSS1Compat"}
var _1="<TABLE cellspacing=0 cellpadding=2 border=0><tr><td height=30>x</td></tr></TABLE>"
var _2=isc.Element.createAbsoluteElement(_1);var _3=_2.offsetHeight>30;isc.Element.clear(_2);return _3}
,isc.A.$qf=function isc_c_Element__deriveStyleProperties(_1,_2){var _3=(isc.Browser.isIE||isc.Browser.isOpera||isc.Browser.isSafari||(isc.Browser.isMoz&&isc.Browser.geckoVersion>=20080205));if(!this.$qg){this.createAbsoluteElement("<TABLE CELLPADDING=81 STYLE='position:absolute;left:0px;top:-2000px;'><TR><TD "+(isc.Browser.isIE8Strict?" ID=isc_cellStyleTester STYLE='border:0px;margin:0px'><DIV ID=isc_cellInnerStyleTester>"+isc.Canvas.blankImgHTML(30,30)+"</DIV></TD>":" ID=isc_cellStyleTester>&nbsp;</TD>"+"<TD ID=isc_cellNoStyleTester>&nbsp;</TD></TR></TABLE>"));this.$qg=isc.Element.get("isc_cellStyleTester");if(isc.Browser.isIE8Strict){this.$qh=isc.Element.get("isc_cellInnerStyleTester")}
this.$qi="81px";if(isc.Browser.isSafari||isc.Browser.isChrome){var _4=isc.Element.get("isc_cellNoStyleTester");var _5=["paddingLeft"];var _6=this.getComputedStyle(_4,_5).paddingLeft;if(_6!=this.$qi){this.logDebug("Browser natively misreporting cell-padding (81px reported as:"+_6+"). This behavior is known to occur when the view is "+"zoomed in certain browsers but is worked around by SmartClient and "+"should have no visible effect on the application.","sizing");this.$qi=_6}}
this.$qj="-16384px";if(_3){this.createAbsoluteElement("<DIV ID=isc_styleTester STYLE='position:absolute;left:0px;top:-2000px;'>&nbsp;</DIV>");this.$qk=isc.Element.get("isc_styleTester");this.$ql=["marginLeft","marginTop","marginRight","marginBottom"];if(isc.Browser.isIE8Strict){this.$ql.addList(["borderLeftWidth","borderTopWidth","borderRightWidth","borderBottomWidth"])}}}
this.$qg.className=_1;var _7=this.getComputedStyle(this.$qg,_2);var _8=this.$qi;if(_7.paddingLeft==_8)_7.paddingLeft=null;if(_7.paddingTop==_8)_7.paddingTop=null;if(_7.paddingRight==_8)_7.paddingRight=null;if(_7.paddingBottom==_8)_7.paddingBottom=null;if(isc.Browser.isIE8Strict){var _9=this.$qh,_10=_9.offsetLeft,_11=_9.offsetTop;if(_10==81)_7.paddingLeft=null;if(_11==81)_7.paddingTop=null;if(this.$qg.offsetWidth-_10-30==81){_7.paddingRight=null}
if(this.$qg.offsetHeight-_11-30==81){_7.paddingBottom=null}}
if(isc.Browser.isSafari){if(isc.Browser.safariVersion<419.3){_8=isc.Canvas.AUTO;if(_7.paddingLeft==_8)_7.paddingLeft=null;if(_7.paddingTop==_8)_7.paddingTop=null;if(_7.paddingRight==_8)_7.paddingRight=null;if(_7.paddingBottom==_8)_7.paddingBottom=null}
_8=this.$qj;if(_7.marginTop==_8)_7.marginTop=null;if(_7.marginBottom==_8)_7.marginBottom=null}
if(_3){this.$qk.className=_1;var _12=this.getComputedStyle(this.$qk,this.$ql);_7.marginLeft=_12.marginLeft;_7.marginRight=_12.marginRight;_7.marginTop=_12.marginTop;_7.marginBottom=_12.marginBottom;if(isc.Browser.isIE8Strict){_7.borderLeftWidth=_12.borderLeftWidth;_7.borderRightWidth=_12.borderRightWidth;_7.borderTopWidth=_12.borderTopWidth;_7.borderBottomWidth=_12.borderBottomWidth}}
return _7}
,isc.A.getComputedStyle=function isc_c_Element_getComputedStyle(_1,_2){var _3,_4,_5;if(isc.isA.String(_1)){_3=isc.Element.get(_1)}else{_3=_1}
if(_3==null||!isc.isAn.Object(_3)){this.logWarn("getComputedStyle: Unable to get to DOM element specified by '"+_1+"'."+this.getStackTrace());return null}
if(this.$pm){_4=_3.currentStyle;if(_2==null)_2=this.$qm;var _6=isc.applyMask(_4,_2);return _6}
if(_2==null){_2=this.$qn}else if(isc.isAn.Array(_2)){var _7={},_8=this.$qn;for(var i=0;i<_2.length;i++){_7[_2[i]]=_8[_2[i]]}
_2=_7}
var _10=isc.Browser.isSafari&&isc.Browser.safariVersion<312,_11;if(_10){_4=_3.style;_11=this.getStyleDeclaration(_3.className)}else{_4=document.defaultView.getComputedStyle(_3,null)}
_5={};for(var _12 in _2){_5[_12]=_4.getPropertyValue(_2[_12]);if(_10&&_5[_12]==null&&_11!=null&&_11[_12]!=null&&!isc.isAn.emptyString(_11[_12]))
{_5[_12]=_11[_12]}}
return _5}
,isc.A.getComputedStyleAttribute=function isc_c_Element_getComputedStyleAttribute(_1,_2){if(_1==null||_2==null)return null;if(this.$pm){if(_1.currentStyle==null)return null;if(isc.Browser.isOpera&&this.$pv[_2]!=null&&_1.currentStyle[this.$pv[_2]]==this.$pr)return 0;return _1.currentStyle[_2]}
if(isc.Browser.isSafari){var _3=null;if(_1.style)_3=_1.style[_2];if((_3==null||isc.isAn.emptyString(_3))&&_1.className)
{var _4=isc.Element.getStyleEdges(_1.className);if(_4)_3=_4[_2]}
if(isc.isAn.emptyString(_3))return null;return _3}
var _5=this.$qn;var _6=this.$qo=this.$qo||document.defaultView;var _7=(_5[_2]||_2),_8=_6.getComputedStyle(_1,null);return _8.getPropertyValue(_7)}
,isc.A.getStyleDeclaration=function isc_c_Element_getStyleDeclaration(_1,_2){if(!_1)return null;if(!isc.allowDuplicateStyles)_2=false;if(isc.Browser.isSafari&&isc.Browser.safariVersion>=312){_1=_1.toLowerCase()}
var _3="."+_1,_4=", ";var _5,_6=_2?[]:null;for(var i=document.styleSheets.length-1;i>=0;i--){var _8=this.$qp(document.styleSheets[i]);if(_8==null)continue;for(var j=_8.length-1;j>=0;j--){var _10=_8[j].selectorText;if(_10==null)continue;if(isc.Browser.isSafari&&isc.Browser.safariVersion>=312){_10=_10.toLowerCase()}
if(isc.Browser.isMoz||isc.Browser.isIE9){var _11=_10.split(_4);for(var k=0;k<_11.length;k++){if(_11[k]==_3){_5=_8[j].style;if(_5!=null){if(_2)_6[_6.length]=_5;else return _5}}}}else{if(_10==_3){_5=_8[j].style;if(_5!=null){if(_2)_6[_6.length]=_5;else return _5}}}}}
if(_2&&_6.length>0)return _6;return null}
,isc.A.$qp=function isc_c_Element__getCSSRules(_1){if(!this.$qq){var _2="try{return $qr.rules||$qr.cssRules}"+"catch(e){isc.Page.$qs = true}";this.$qq=new Function("$qr",_2)}
return this.$qq(_1)}
,isc.A.getStyleText=function isc_c_Element_getStyleText(_1,_2){if(!isc.allowDuplicateStyles)_2=false;var _3=this.$pw,_4=_3[_1];if(_4!=null)return _4;var _5=this.getStyleDeclaration(_1,_2);if(_5==null){if(!isc.Browser.isSafari||isc.Page.isLoaded())
this.$pw[_1]=isc.emptyString;return isc.emptyString}
if(_2){for(var i=_5.length-1;i>-1;i--){var _7=_5[i];var _8=_7.cssText;if(_8==null)continue;if(!isc.endsWith(_8,this.$px))_8+=this.$px;if(_4==null)_4=_8;else _4+=_8}
if(_4==null)_4=isc.$ah}else{_4=(_5.cssText||isc.$ah)}
if(!isc.endsWith(_4,isc.semi))_4+=isc.semi;return(_3[_1]=_4)}
,isc.A.$qt=function isc_c_Element__clearCSSCaches(){isc.Element.$pu={};isc.Element.$pw={};isc.Element.$qu=isc.Element.$qv=isc.Element.$qw=null}
,isc.A.$p3=function isc_c_Element__getTopMargin(_1){return this.$qx(_1).top}
,isc.A.$p4=function isc_c_Element__getBottomMargin(_1){return this.$qx(_1).bottom}
,isc.A.$p5=function isc_c_Element__getLeftMargin(_1){return this.$qx(_1).left}
,isc.A.$p6=function isc_c_Element__getRightMargin(_1){return this.$qx(_1).right}
,isc.A.$qx=function isc_c_Element__calculateMargins(_1){if(this.$qv==null)this.$qv={};else if(this.$qv[_1]!=null){return this.$qv[_1]}
var _2={top:0,bottom:0,left:0,right:0},_3=isc.Element.getStyleEdges(_1);if(_3==null)return _2;var _4=_3.marginTop,_5=_3.marginBottom,_6=_3.marginLeft,_7=_3.marginRight,_8=isc.px;if(isc.isA.String(_4)&&isc.endsWith(_4,_8))
_2.top=parseInt(_4);if(isc.isA.String(_5)&&isc.endsWith(_5,_8))
_2.bottom=parseInt(_5);if(isc.isA.String(_6)&&isc.endsWith(_6,_8))
_2.left=parseInt(_6);if(isc.isA.String(_7)&&isc.endsWith(_7,_8))
_2.right=parseInt(_7);this.$qv[_1]=_2;return _2}
,isc.A.$qy=function isc_c_Element__getTopBorderSize(_1){return this.$qz(_1).top}
,isc.A.$q0=function isc_c_Element__getBottomBorderSize(_1){return this.$qz(_1).bottom}
,isc.A.$q1=function isc_c_Element__getLeftBorderSize(_1){return this.$qz(_1).left}
,isc.A.$q2=function isc_c_Element__getRightBorderSize(_1){return this.$qz(_1).right}
,isc.A.$qz=function isc_c_Element__calculateBorderSize(_1){if(this.$qu==null)this.$qu={};else if(this.$qu[_1]!=null){return this.$qu[_1]}
var _2={top:0,bottom:0,left:0,right:0},_3=isc.Element.getStyleEdges(_1);if(_3==null)return _2;var _4=_3.borderTopWidth,_5=_3.borderBottomWidth,_6=_3.borderLeftWidth,_7=_3.borderRightWidth,_8=isc.px;if(isc.isA.String(_4)&&isc.endsWith(_4,_8))
_2.top=parseInt(_4);if(isc.isA.String(_5)&&isc.endsWith(_5,_8))
_2.bottom=parseInt(_5);if(isc.isA.String(_6)&&isc.endsWith(_6,_8))
_2.left=parseInt(_6);if(isc.isA.String(_7)&&isc.endsWith(_7,_8))
_2.right=parseInt(_7);this.$qu[_1]=_2;return _2}
,isc.A.$q3=function isc_c_Element__getVBorderSize(_1){return this.$qy(_1)+this.$q0(_1)}
,isc.A.$q4=function isc_c_Element__getHBorderSize(_1){return this.$q1(_1)+this.$q2(_1)}
,isc.A.$q5=function isc_c_Element__getTopPadding(_1,_2){var _3=this.$q6(_1);if(_2&&_3.nullTop)return null;return _3.top}
,isc.A.$q7=function isc_c_Element__getBottomPadding(_1,_2){var _3=this.$q6(_1);if(_2&&_3.nullBottom)return null;return _3.bottom}
,isc.A.$q8=function isc_c_Element__getLeftPadding(_1,_2){var _3=this.$q6(_1);if(_2&&_3.nullLeft)return null;return _3.left}
,isc.A.$q9=function isc_c_Element__getRightPadding(_1,_2){var _3=this.$q6(_1);if(_2&&_3.nullRight)return null;return _3.right}
,isc.A.$q6=function isc_c_Element__calculatePadding(_1){if(this.$qw==null)this.$qw={};else if(this.$qw[_1]!=null){return this.$qw[_1]}
var _2={top:0,bottom:0,left:0,right:0},_3=isc.Element.getStyleEdges(_1);if(_3==null){_2.nullLeft=true;_2.nullRight=true;_2.nullTop=true;_2.nullBottom=true;return _2}
var _4=_3.paddingTop,_5=_3.paddingBottom,_6=_3.paddingLeft,_7=_3.paddingRight,_8=isc.px;_2.nullTop=(_4==null||_4==isc.emptyString);_2.nullBottom=(_5==null||_5==isc.emptyString)
_2.nullLeft=(_6==null||_6==isc.emptyString);_2.nullRight=(_7==null||_7==isc.emptyString);if(isc.isA.String(_4)&&isc.endsWith(_4,_8))
_2.top=parseInt(_4);if(isc.isA.String(_5)&&isc.endsWith(_5,_8))
_2.bottom=parseInt(_5);if(isc.isA.String(_6)&&isc.endsWith(_6,_8))
_2.left=parseInt(_6);if(isc.isA.String(_7)&&isc.endsWith(_7,_8))
_2.right=parseInt(_7);this.$qw[_1]=_2;return _2}
,isc.A.$ra=function isc_c_Element__getVPadding(_1){return this.$q5(_1)+this.$q7(_1)}
,isc.A.$rb=function isc_c_Element__getHPadding(_1){return this.$q8(_1)+this.$q9(_1)}
,isc.A.$rc=function isc_c_Element__getVBorderPad(_1){return this.$q3(_1)+this.$ra(_1)}
,isc.A.$rd=function isc_c_Element__getHBorderPad(_1){return this.$q4(_1)+this.$rb(_1)}
,isc.A.getNativeScrollbarSize=function isc_c_Element_getNativeScrollbarSize(){if(isc.Element.$re==null){if(isc.Browser.isMobileWebkit){return(isc.Element.$re=16)}
var _1="<div id=isc_ScrollbarTest "+"style='position:absolute;top:-100px;border:0px;padding:0px;margin:0px;height:100px;width:100px;overflow:scroll;'>"+isc.nbsp+"</div>";this.createAbsoluteElement(_1);var _2=this.get('isc_ScrollbarTest');isc.Element.$re=parseInt(_2.style.height)-_2.clientHeight;this.clear(_2)}
return isc.Element.$re}
,isc.A.getRotationCSS=function isc_c_Element_getRotationCSS(_1,_2){var _3=this.vendorCSSPrefix;var _4=_3+"transform: rotate("+_1+"deg);";if(_2!=null){_4+=(_3+"transform-origin: "+_2+";")}
return _4}
);isc.B._maxIndex=isc.C+69;isc.Element.$rf=function(){var _1=this.$qe={borderLeftWidth:"border-left-width",borderRightWidth:"border-right-width",borderTopWidth:"border-top-width",borderBottomWidth:"border-bottom-width",marginLeft:"margin-left",marginRight:"margin-right",marginTop:"margin-top",marginBottom:"margin-bottom",paddingLeft:"padding-left",paddingRight:"padding-right",paddingTop:"padding-top",paddingBottom:"padding-bottom"}
var _2=this.$qn=isc.addProperties({position:"position",overflow:"overflow",top:"top",left:"left",width:"width",height:"height",display:"display"},_1);if(isc.Browser.isIE||isc.Browser.isOpera){this.$qm=isc.getKeys(_2);this.$qd=isc.getKeys(_1)}}
isc.Element.$rf();isc.ClassFactory.defineClass("Canvas");isc.isA.Canvas=function(_1){return(_1!=null&&_1._isA_Canvas)}
isc.A=isc.Canvas;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A._isA_Canvas=true;isc.A.AUTO="auto";isc.A.ANYTHING="**anything**";isc.A.ABSOLUTE="absolute";isc.A.RELATIVE="relative";isc.A.INHERIT="inherit";isc.A.VISIBLE="visible";isc.A.HIDDEN="hidden";isc.A.COMPLETE="complete";isc.A.DRAWN="complete";isc.A.DRAWING_HANDLE="drawingHandle";isc.A.HANDLE_DRAWN="handleDrawn";isc.A.UNDRAWN="undrawn";isc.A.SCROLL="scroll";isc.A.CLIP_H="clip-h";isc.A.CLIP_V="clip-v";isc.A.IGNORE="ignore";isc.A.NATIVE="native";isc.A.CLIP="clip";isc.A.NESTED_DIV="nestedDiv";isc.A.CENTER="center";isc.A.LEFT="left";isc.A.RIGHT="right";isc.A.TOP="top";isc.A.BOTTOM="bottom";isc.A.UP="up";isc.A.DOWN="down";isc.A.BOTH="both";isc.A.NONE="none";isc.A.VERTICAL="vertical";isc.A.HORIZONTAL="horizontal";isc.A.MARKED="marked";isc.A.MIDDLE="middle";isc.A.ALL="all";isc.A.DEFAULT="default";isc.A.ARROW="default";isc.A.WAIT="wait";isc.A.HAND=(isc.Browser.isMoz||(isc.Browser.isSafari&&isc.Browser.isStrict)?"pointer":"hand");isc.A.MOVE="move";isc.A.HELP="help";isc.A.TEXT="text";isc.A.CROSSHAIR="crosshair";isc.A.NOT_ALLOWED="not-allowed";isc.A.COL_RESIZE=(isc.Browser.isIE&&isc.Browser.version>=6?"col-resize":"e-resize");isc.A.ROW_RESIZE=(isc.Browser.isIE&&isc.Browser.version>=6?"row-resize":"n-resize");isc.A.TILE="tile";isc.A.STRETCH="stretch";isc.A.NORMAL="normal";isc.A.REPEAT="repeat";isc.A.NO_REPEAT="no-repeat";isc.A.REPEAT_X="repeat-x";isc.A.REPEAT_Y="repeat-y";isc.A.LTR="ltr";isc.A.RTL="rtl";isc.A.BEFORE="before";isc.A.AFTER="after";isc.A.NEAREST="nearest";isc.A.$rg=200000;isc.A.$rh=199950;isc.A.$ri=800000;isc.A.TAB_INDEX_GAP=50;isc.A.TAB_INDEX_FLOOR=1000;isc.A.TAB_INDEX_CEILING=32766;isc.A.$rj=[];isc.A.textStyleAttributes=["fontFamily","fontSize","color","backgroundColor","fontWeight","fontStyle","textDecoration","textAlign"];isc.A.$rk=[];isc.A.allowExternalFilters=true;isc.A.$rl=[];isc.A._redrawQueueDelay=(0);isc.A.$rm=200;isc.A._canvasList=[];isc.A._iscInternalCount=0;isc.A._stats={redraws:0,clears:0,destroys:0,draws:0};isc.A.$rn={};isc.A.$ro={};isc.A.$rp=[];isc.A.useMozBackMasks=false;isc.A.useNativeWheelDelta=true;isc.A.scrollWheelDelta=50;isc.A.loadingImageSrc="[SKINIMG]loadingSmall.gif";isc.A.loadingImageSize=16;isc.B.push(isc.A.$rq=function isc_c_Canvas__setDoublingStrings(){this.$rr=isc.Browser.isIE&&(!this.neverUseFilters||this.allowExternalFilters)?"margin:0px;border:0px;padding:0px;background-image:none;background-color:transparent;filter:none;":"margin:0px;border:0px;padding:0px;background-image:none;background-color:transparent;";isc.Canvas.addProperties({$rs:"' style='"+isc.Canvas.$rr});for(var i=0;i<this.$rk.length;i++){var _2=this.$rk[i];if(_2.target==null||_2.target.destroyed)continue;_2.target[_2.methodName](this.$rr)}}
,isc.A.setNeverUseFilters=function isc_c_Canvas_setNeverUseFilters(_1){this.neverUseFilters=_1;this.$rq()}
,isc.A.setAllowExternalFilters=function isc_c_Canvas_setAllowExternalFilters(_1){this.allowExternalFilters=_1;this.$rq()}
);isc.B._maxIndex=isc.C+3;isc.Canvas.$rq();isc.A=isc.Canvas.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A._isA_Canvas=true;isc.A.autoDraw=true;isc.A.allowContentAndChildren=true;isc.A.htmlPosition="afterBegin";isc.A.position=null;isc.A.left=0;isc.A.top=0;isc.A.defaultWidth=100;isc.A.defaultHeight=100;isc.A.minWidth=10;isc.A.maxWidth=10000;isc.A.minHeight=10;isc.A.maxHeight=10000;isc.A.zIndex=isc.Canvas.AUTO;isc.A.autoShowParent=false;isc.A.visibility=isc.Canvas.INHERIT;isc.A.styleName="normal";isc.A.contents=isc.nbsp;isc.A.backgroundRepeat=isc.Canvas.REPEAT;isc.A.mozOutlineOffset="-1px";isc.A.appImgDir="";isc.A.skinImgDir="images/";isc.A.cursor=isc.Canvas.DEFAULT;isc.A.disabledCursor=isc.Canvas.DEFAULT;isc.A.noDropCursor=isc.Canvas.NOT_ALLOWED;isc.A.$rt=(isc.Browser.isMoz&&isc.Browser.geckoVersion<20081201);isc.A.overflow=isc.Canvas.VISIBLE;isc.A.alwaysShowVScrollbar=false;isc.A.showCustomScrollbars=!((isc.Browser.isOpera||isc.Browser.isIE&&isc.Browser.version>4)||(isc.Browser.isUnix&&isc.Browser.isMoz&&isc.Browser.geckoVersion>=20020826&&isc.Browser.geckoVersion<=20031007));isc.A.scrollbarSize=16;isc.A.scrollbarConstructor="Scrollbar";isc.A.scrollLeft=0;isc.A.scrollTop=0;isc.A.scrollDelta=20;isc.A.$ru="unset";isc.A.enabled="unset";isc.A.redrawOnDisable=false;isc.A.$k7=true;isc.A.$k6=true;isc.A.$rv=true;isc.A.$rw=true;isc.A.$k8=true;isc.A._redrawWithParent=true;isc.A.showFocusOutline=true;isc.A._useNativeTabIndex=(isc.Browser.isIE&&isc.Browser.version>=5)||isc.Browser.isSafari||(isc.Browser.isMoz&&isc.Browser.geckoVersion>=20051111);isc.A._useFocusProxy=(isc.Browser.isMoz&&isc.Browser.geckoVersion<20051111)||isc.Browser.isOpera;isc.A.contextMenuProperties={autoDraw:false,width:200,showIcons:true};isc.A.menuConstructor="Menu";isc.A.clippedCorners=["TL","TR","BL","BR"];isc.A.cornerClipColor="FFFFFF";isc.A.cornerClipImage="[SKIN]corner.gif";isc.A.cornerClipSize=10;isc.A.$rx={_generated:true,overflow:"hidden",$k7:false,$k6:false,autoDraw:false,skinImgDir:"images/corners/",draw:function(){this.Super("draw",arguments)}};isc.A.dragOutlineStyle="dragOutline";isc.A.dragStartDistance=5;isc.A.canDragScroll=true;isc.A.dragScrollDelay=100;isc.A.dragScrollThreshold="10%";isc.A.minDragScrollIncrement=1;isc.A.maxDragScrollIncrement="5%";isc.A.dragIntersectStyle=isc.EventHandler.INTERSECT_WITH_MOUSE;isc.A.dragRepositionCursor=isc.Canvas.MOVE;isc.A.dragScrollType="any";isc.A.hoverDelay=300;isc.A.showHover=true;isc.A.edgeMarginSize=5;isc.A.edgeCursorMap={"T":"n-resize","L":"w-resize","B":"s-resize","R":"e-resize","TL":"nw-resize","TR":"ne-resize","BL":"sw-resize","BR":"se-resize"};isc.A.dragAppearance=isc.EventHandler.OUTLINE;isc.A.dropTypes=isc.Canvas.ANYTHING;isc.A.mouseStillDownInitialDelay=400;isc.A.mouseStillDownDelay=100;isc.A.doubleClickDelay=250;isc.A.refreshVariable="refresh";isc.A.$mi=(isc.Browser.isMoz&&(!isc.Browser.isUnix||isc.Browser.geckoVersion>20031007));isc.A.useClipDiv=(isc.Browser.isMoz||isc.Browser.isSafari||isc.Browser.isOpera);isc.A.manageChildOverflow=true;isc.A.$ry={};isc.A.percentBox="visible";isc.A.$rz="viewport";isc.A.snapHGap=20;isc.A.snapVGap=20;isc.A.snapHDirection=isc.Canvas.AFTER;isc.A.snapVDirection=isc.Canvas.AFTER;isc.A.snapAxis=isc.Canvas.BOTH;isc.A.snapOnDrop=true;isc.B.push(isc.A.getDragAppearance=function isc_Canvas_getDragAppearance(_1){if(_1==isc.EH.DRAG_RESIZE&&this.dragResizeAppearance!=null)
return this.dragResizeAppearance;if(_1==isc.EH.DRAG_REPOSITION&&this.dragRepositionAppearance!=null)
return this.dragRepositionAppearance;return this.dragAppearance}
);isc.B._maxIndex=isc.C+1;isc.A=isc.Canvas.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$ox="resize";isc.A.$r0="draw";isc.A.$r1="hidden";isc.A.$r2="redraw";isc.A.$r3="undefined";isc.A.$r4="draws";isc.A.$r5="drawing";isc.A.$r6="redraws";isc.A.$r7="autoDraw";isc.A.$pk="beforeBegin";isc.A.$pi="afterBegin";isc.A.$pl="beforeEnd";isc.A.$pj="afterEnd";isc.A.$r8=">";isc.A.$r9="'";isc.A.$sa='"';isc.A.$sb="initWidget";isc.A.$sc="html";isc.A.$sd="&nbsp;";isc.A.$se="initial draw";isc.A.$sf="parentDrawn";isc.A.notifyAncestorsOnReflow=false;isc.A.$sg="eventProxy";isc.A.reuseDOMIDs=false;isc.A.$sh="canvas";isc.A.$si="canvas_clipDiv";isc.A.clipHandleIsFocusHandle=true;isc.A.$pp="MARGIN-LEFT:";isc.A.$sj="MARGIN-RIGHT:";isc.A.$pq="MARGIN-TOP:";isc.A.$sk="MARGIN-BOTTOM:";isc.A.$sl="MARGIN:";isc.A.$sm="</div>";isc.A.$sn="</div></div>";isc.A.$so=[];isc.A.isBorderBox=(isc.Browser.isMoz||isc.Browser.isBorderBox);isc.A.$sp="isc.EH.focusInCanvas(";isc.A.$sq="if(event.target!=this)return;isc.EH.focusInCanvas(";isc.A.$sr="if(window.isc)isc.EH.blurFocusCanvas(";isc.A.$ss=",true);";isc.A.$st="autoChild:";isc.A.$su="spacer:";isc.A.$sv=["<DIV ID='",null,"'style='position:absolute;width:1px;height:1px;overflow:hidden;left:",null,"px;top:",null,"px;'>&nbsp;</DIV>"];isc.A.$sw="scrollSizeDiv";isc.A.$sx="enforceScrollSize";isc.A.$sy="-1px";isc.A.useClientRectAPI=(isc.Browser.isMoz&&isc.Browser.geckoVersion>20071109);isc.A.useBoxObjectAPI=false;isc.A.useBoxObjectAPISelectively=true;isc.A.$sz="0px";isc.A.$s0="left";isc.A.$s1="top";isc.A.$s2="right";isc.A.$s3="bottom";isc.A.$s4="center";isc.A.$s5="childMoved";isc.A.$s6="height";isc.A.$s7="width";isc.A.$s0="left";isc.A.$s1="top";isc.A.$s8="$s9";isc.A.$ta="%";isc.A.$tb="*";isc.A.$tc={height:"_percent_height",width:"_percent_width",left:"_percent_left",top:"_percent_top"};isc.A.$td={height:"minHeight",width:"minWidth"};isc.A.$te="resized";isc.A.$tf="childResized";isc.A.momentumScrolling=true;isc.A.momentumScrollTime=1500;isc.A.momentumScrollAcceleration="smoothStart";isc.A.hoopSelectorDefaults={_constructor:"Canvas",keepInParentRect:true,redrawOnResize:false,overflow:"hidden",border:"1px solid blue",opacity:10,backgroundColor:"blue"};isc.A.hoopSelectAxis="both";isc.A.shouldSetNoDropTracker=isc.Browser.isOpera;isc.A.noDropTracker="[SKIN]/shared/no_drop.png";isc.A.adjustOverflowWhileDirty=true;isc.A.$tg={hidden:true,visible:true,scroll:true,auto:true,"clip-v":true,"clip-h":true,ignore:true};isc.A.$th="sizing";isc.A.$ti="overflow";isc.A.cancelNativeScrollOnKeyDown=isc.Browser.isSafari;isc.A.$tj={Page_Up:true,Page_Down:true,Arrow_Up:true,Arrow_Down:true,Arrow_Left:true,Arrow_Right:true,Home:true,End:true};isc.A.$tk="px";isc.A.$pr="none";isc.A.$tl="relative";isc.A.$tm="disabled";isc.A.$pr="none";isc.A.$tn="styleName";isc.A.$to="eventpart";isc.A.$hg="count";isc.A.$tp=["edgeImage","edgeColor","customEdges","shownEdges","edgeSize","edgeTop","edgeBottom","edgeLeft","edgeRight","edgeOffset","edgeOffsetTop","edgeOffsetBottom","edgeOffsetLeft","edgeOffsetRight","canDragResize","canDragReposition"];isc.A.shadowDepth=4;isc.A.dragResizeFromShadow=true;isc.A.$tq="shadow";isc.A.isGroup=false;isc.A.groupBorderCSS="2px solid black";isc.A.groupLabelPadding=10;isc.A.showGroupLabel=true;isc.A.groupLabelStyleName="groupLabel";isc.A.groupLabelDefaults={_constructor:"Label",overflow:"visible",height:1,width:1,wrap:false,vAlign:"center",align:"center"};isc.B.push(isc.A.init=function isc_Canvas_init(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13){if(isc.$dd)arguments.$de=this;if(!isc.Canvas.$tr){if(this.getDocumentBody(true)==null){isc.logWarn("Canvas created in a page outside the BODY tag. This is not supported. "+"Isomorphic Software requires the tag to be present and all widgets be created "+"and drawn inside it. Canvas details follow:\n"+isc.Log.echo(this))}
isc.Canvas.$tr=true}
this.ns.ClassFactory.addGlobalID(this);this._canvasList(true);if(this.position==null){this.position=this.htmlElement!=null?isc.Canvas.RELATIVE:isc.Canvas.ABSOLUTE}
if(this.className!=null&&this.logIsInfoEnabled(this.$tn)){this.logInfo("'className' property specified. This property has been deprecated in "+"favor of 'styleName' as of SmartClient 5.5.",this.$tn)}
if(this.styleName!=null){if(this.className!=null){var _14=this.getPrototype(),_15=(this.styleName!=_14.styleName),_16=(this.className!=_14.className);if(_15)this.className=this.styleName;else if(_16)this.styleName=this.className;else this.styleName=this.className}else{this.className=this.styleName}}else if(this.className!=null){this.styleName=this.className}
if(this.size!=null)this.height=this.width=this.size;this.$ts=this.width;this.$tt=this.height;if(this.width==null)this.width=this.defaultWidth;if(this.height==null)this.height=this.defaultHeight;this.$s9=this.height;if(isc.isA.String(this.margin)){var _17=parseInt(this.margin);if(isc.isA.Number(_17))this.margin=_17;else{this.logWarn("Invalid setting for this.margin:"+this.margin+". This should be a numeric value - ignoring");this.margin=null}}
if(isc.isA.String(this.padding)){var _18=parseInt(this.padding);if(isc.isA.Number(_18))this.padding=_18;else{this.logWarn("Invalid setting for this.padding:"+this.padding+". This should be set to a numeric value - ignoring");this.padding=null}}
if(this.border!=null&&!isc.isA.String(this.border)){this.border=this.$tu(this.border)}
if(this.percentSource)this.setPercentSource(this.percentSource,true);this.$tv=true;this.resizeTo(this.width,this.$s9);this.moveTo(this.left,this.top);this.$tv=null;if(this.children&&!isc.isAn.Array(this.children))this.children=[this.children];if(this.peers&&!isc.isAn.Array(this.peers))this.peers=[this.peers];if(this.enabled!=this.$ru){this.logWarn("Widget initialized with explicitly specified 'enabled' property. "+"This property has been deprecated - use 'disabled' instead.");this.disabled=!this.enabled}
if(this.redrawOnEnable!=null){this.logWarn("Widget initialized with deprecated 'redrawOnEnable' - use 'redrawOnDisable' instead.");this.redrawOnDisable=this.redrawOnEnable}
this.initWidget(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13);this.$tw();if(this.showShadow)this.$tx();if(this.clipCorners)this.$ty();if(this.useBackMask&&((isc.Browser.isIE&&isc.Browser.minorVersion>=5.5)||(isc.Canvas.useMozBackMasks&&isc.Browser.isMoz))){this.makeBackMask()}
if(this.isGroup){delete this.isGroup;this.setIsGroup(true)}
if(this.children)this.children.setProperty(this.$r7,false);if(this.peers)this.peers.setProperty(this.$r7,false);if(this.observes){var _19,_20,_21=this.observes,_22=_21.length;for(var i=0;i<_22;i++){var _19=_21[i];if(!_19)continue;if(isc.isA.String(_19.source))_20=this.getGlobalReference(_19.source);else _20=_19.source;if(_20){this.observe(_20,_19.message,_19.action)}}}
this.$tz();if(this.autoChildren)this.addAutoChildren(this.autoChildren);if(this.addOns)this.addAutoChildren(this.addOns);if(this._adjacentHandle&&!this.drawContext){this.drawContext={element:this._adjacentHandle}}
if(this.htmlElement){var _24=this.htmlElement;delete this.htmlElement;this.setHtmlElement(_24)}
if(this.eventProxy!=null){if(!isc.isA.Canvas(this.eventProxy)){this.logWarn("Canvas ID:'"+this.getID()+"' initialized with bad eventProxy. "+"This property should be set to another Canvas instance. Clearing this property.")
delete this.eventProxy}else{if(this.eventProxy.$mj==null)this.eventProxy.$mj=[];this.eventProxy.$mj.add(this)}}
var _25=this.parentElement;if(_25){this.parentElement=null;if(isc.isA.String(_25))_25=window[_25];_25.addChild(this)}
if(this.autoFetchAsFilter!=null){var _26=this.autoFetchAsFilter?"substring":"exact";this.logWarn("This component has autoFetchAsFilter explicitly specified as:"+this.autoFetchAsFilter+". This attribute is deprecated in favor of "+"this.autoFetchTextMatchStyle. Defaulting autoFetchTextMatchStyle to \""+_26+"\" based on this setting.");this.autoFetchTextMatchStyle=_26}
this.initializeValuesManager();if(this.showPanelHeader==true){if(this.setupPanelHeader)this.setupPanelHeader();if(this.refreshPanelControls)this.refreshPanelControls()}
if(this.autoDraw&&!this.parentElement&&!isc.noAutoDraw){if(isc.Browser.isSafari&&!isc.Browser.isChrome&&isc.deferAutoDraw&&!isc.Page.isLoaded()&&this.position!="relative")
{isc.Page.setEvent("load","if(window."+this.getID()+")"+this.getID()+".$t0()")}else{this.draw()}}}
,isc.A.$t0=function isc_Canvas__deferredAutoDraw(){if(this.destroyed||this.isDrawn())return;this.draw()}
,isc.A.initWidget=function isc_Canvas_initWidget(){}
,isc.A.setID=function isc_Canvas_setID(_1){var _2=this.pointersToThis=this.pointersToThis||[];_2.add({object:window,property:this.ID});this.ID=_1;window[this.ID]=this;this.clear();this.draw()}
,isc.A.clearIDs=function isc_Canvas_clearIDs(){this.clear();window[this.ID]=null;if(this.children){for(var i=0;i<this.children.length;i++){this.children[i].clearIDs()}}
if(this.peers){for(var i=0;i<this.peers.length;i++){this.peers[i].clearIDs()}}}
,isc.A.getDrawnState=function isc_Canvas_getDrawnState(){if(this.$t1==true)return isc.Canvas.COMPLETE;if(this.$t2==true)return isc.Canvas.HANDLE_DRAWN;if(this.$t3==true)return isc.Canvas.DRAWING_HANDLE;return isc.Canvas.UNDRAWN}
,isc.A.setDrawnState=function isc_Canvas_setDrawnState(_1){if(_1==isc.Canvas.COMPLETE)this.$t1=true;else this.$t1=false;if(_1==isc.Canvas.HANDLE_DRAWN)this.$t2=true;else this.$t2=false;if(_1==isc.Canvas.DRAWING_HANDLE)this.$t3=true;else this.$t3=false}
,isc.A.isDrawn=function isc_Canvas_isDrawn(){return!!this.$t1}
,isc.A.handleDrawn=function isc_Canvas_handleDrawn(){return!!this.$t2}
,isc.A.getID=function isc_Canvas_getID(){if(this.ID==null)this.ns.ClassFactory.addGlobalID(this);return this.ID}
,isc.A.getAttribute=function isc_Canvas_getAttribute(_1){return this[_1]}
,isc.A.getInnerHTML=function isc_Canvas_getInnerHTML(){var _1;if(!this.containsIFrame())_1=this.getContents();else{var _2=this.getContentsURL();_2=isc.Page.getURL(_2);if(isc.rpc)_2=isc.rpc.addParamsToURL(_2,this.contentsURLParams);isc.EventHandler.registerMaskableItem(this,true);_1=this.getIFrameHTML(_2)}
return _1}
,isc.A.getIFrameHTML=function isc_Canvas_getIFrameHTML(_1){return"<iframe height='100%' width='100%' scrolling='"+(this.overflow==isc.Canvas.HIDDEN?"no'":"auto'")+(isc.Browser.isSafari?" id="+this.$t4("iframe"):"")+" frameborder='0'"+" src=\""+_1+"\"></iframe>"}
,isc.A.$t5=function isc_Canvas__sizeIFrame(){var _1=this.getDrawnState();if(_1!=isc.Canvas.COMPLETE&&_1!=isc.Canvas.HANDLE_DRAWN)return;var _2=this.getHandle(),_3=_2?_2.firstChild:null;if(_3==null)return;_3.style.height=(this.getInnerContentHeight()-2)+isc.px}
,isc.A.$t6=function isc_Canvas__getInnerHTML(_1){if(isc.$dd)arguments.$de=this;var _2=this.getInnerHTML(_1);if(this.$t7){var _3=this.$t7.join(isc.emptyString);_2=(_2==null||_2==isc.nbsp?_3:_2+_3)}
return _2}
,isc.A.readyToDraw=function isc_Canvas_readyToDraw(){var _1=this.getDrawnState();if(this.getDrawnState()!=isc.Canvas.UNDRAWN){var _1=this.getDrawnState();this.logWarn("draw() called on widget with current drawn state: "+_1+(_1==isc.Canvas.COMPLETE?", use redraw() instead.":", ignoring.")+this.getStackTrace(),"drawing");return false}
if(this.showIf!=null){this.convertToMethod("showIf");if(this.showIf(this)==false)return false}
if(this.getHeight()<=0||this.getWidth()<=0){if(this.$t8){this.$t9();return false}
this.logWarn("negative or zero area: height: "+this.getHeight()+", width: "+this.getWidth()+", refusing to draw"+this.getStackTrace(),"drawing");return false}
if(this.deferredDrawEvent!=null){this.logInfo("draw() called while object already pending a delayed draw - no action to take","drawing");return false}
if(this.parentElement!=null&&(!isc.isA.Canvas(this.parentElement)||this.parentElement.getDrawnState()==isc.Canvas.UNDRAWN))
{this.logWarn("Attempt to draw child of an undrawn parent - ignoring"+this.getStackTrace(),"drawing");return false}
if(isc.Browser.isSafari&&!isc.Page.isLoaded()){var _2=isc.Browser.safariVersion;if(parseInt(_2)<100){this.drawDeferred();return false}else{}}
return true}
,isc.A.$t9=function isc_Canvas__deferDrawForPageSize(){if(isc.Page.isLoaded())this.drawDeferred();else{isc.Page.setEvent("load",this.getID()+".$ua()")}}
,isc.A.$ua=function isc_Canvas__fireDeferredDrawForPageResize(){if(this.destroyed)return;if(isc.Page.getWidth()==0||isc.Page.getHeight()==0){this.delayCall("draw",null,100)}
else{this.draw()}}
,isc.A.$ub=function isc_Canvas__mustDocumentWrite(){return false}
,isc.A.$uc=function isc_Canvas__requestsDocumentWrite(){if(this.$ud)return true;var _1=this.parentElement;while(_1){if(_1.$ud)return true;_1=_1.parentElement}
if(this.children){for(var i=0;i<this.children.length;i++){if(this.children[i].$ub())return true}}
return false}
,isc.A.draw=function isc_Canvas_draw(_1){if(isc.$dd)arguments.$de=this;if(!this.readyToDraw())return this;if(this.overflow==isc.Canvas.AUTO)this.getTabIndex();if(this.logIsInfoEnabled(this.$r4)){this.logInfo("draw(): drawing "+this.Class+(this.parentElement?" with parent: "+this.parentElement:"")+(!isc.Page.isLoaded()?" before page load":"")+(this.logIsDebugEnabled(this.$r4)?this.getStackTrace():""),this.$r4)}
this.$ue(this.$r4);var _2=this.doInitialFetch();if(this.peers!=null&&this.peers.getLength()>0){this.predrawPeers()}
var _3=(isc.Browser.isIE&&this.fixIEOpacity&&!this.masterElement),_4=isc.Element.cacheOffsetCoords;if(this.position==isc.Canvas.RELATIVE){this.cacheOffsetCoords=false;_4=false}
if(_3||_4){var _5=this.parentElement;while(_5){if(_3){if(_5.opacity!=null&&_5.opacity!=100){this.setOpacity(100,null,true);_3=false;if(!_4)break}}
if(_4){if(_5.position==isc.Canvas.RELATIVE){this.cacheOffsetCoords=false;_4=false;if(!_3)break}}
_5=_5.parentElement}}
if(_4)this.cacheOffsetCoords=true;if(this.htmlElement!=null&&this.matchElement){if(isc.isA.String(this.htmlElement))this.htmlElement=isc.Element.get(this.htmlElement);var _6=isc.Element.getNativeInnerWidth(this.htmlElement),_7=isc.Element.getNativeInnerHeight(this.htmlElement);this.setWidth(_6);this.setHeight(_7)}
var _8=this.parentElement;var _9=(!isc.Page.isLoaded()&&!this.drawContext&&(_8==null&&this.position==isc.Canvas.RELATIVE));_9=_9||this.$ub();var _10=this.separateContentInsertion;if(isc.Page.isLoaded()||!_9){this.$uf(!_10);if(_10)this.$ug();this.drawChildren();this.$uh()}else{var _5=this.parentElement;if((isc.Browser.isOpera||isc.Browser.isIE)&&this.getDocument().readyState=="complete")
{isc.Page.finishedLoading()}
this.$ui()}
if(_2)isc.RPCManager.sendQueue();if(this._useFocusProxy&&this.$mb())this.makeFocusProxy();if(this.accessKey!=null&&this.$uj()&&this.$mb()){this.$uk()}
if(this.$ul!=null)
this.enforceScrollSize(this.$ul[0],this.$ul[1]);if(this.$um())isc.EH.$o0([this]);if(this.clipCorners)this.$un();this.$uo=this.isVisible();if(!_1&&this.$uo)this.show();if(this.parentElement)this.parentElement.childDrawn(this);if(this.masterElement)this.masterElement.peerDrawn(this);if(this.parentElement==null&&isc.Page.isLoaded()&&!isc.Page.pollPageSize)
{if(this.getPageRight()>=isc.Page.getWidth()||this.getPageBottom()>=isc.Page.getHeight())
{isc.EH.fireOnPause("checkForBodyOverflowChange",{target:isc.Canvas,methodName:"checkForPageResize"},100)}}
if(this.parentElement==null&&this.position==this.$tl){this.$up=this.getPageLeft();this.$uq=this.getPageTop();isc.Page.setEvent("resize",this,isc.Page.FIRE_ONCE,"$ur")}
this.onDraw();return this}
,isc.A.onDraw=function isc_Canvas_onDraw(){}
,isc.A.doInitialFetch=function isc_Canvas_doInitialFetch(){}
,isc.A.$ui=function isc_Canvas__writeHTML(){this.setDrawnState(isc.Canvas.DRAWING_HANDLE);var _1=this.getDocument(),_2=this.separateContentInsertion;if(this.children!=null&&this.$ub()){this.$ud=true;var _3=this.getTagStart(),_4=this.getTagEnd();_1.write(_2?_3:_3+this.$t6())
this.drawChildren();_1.write(_2?this.$t6()+_4:_4);this.setDrawnState(isc.Canvas.HANDLE_DRAWN)}else{_1.write(isc.SB.concat(this.getTagStart(),(_2?null:this.$t6()),this.getTagEnd()));this.setDrawnState(isc.Canvas.HANDLE_DRAWN);if(_2)this.$ug();this.drawChildren()}
this.$uh();if(isc.Browser.isMoz&&this.getScrollingMechanism()==isc.Canvas.NATIVE)
this.checkNativeScroll();return this}
,isc.A.drawDeferred=function isc_Canvas_drawDeferred(){var _1=(isc.Page.isLoaded()?"idle":"load");if(this.deferredDrawEvent!=null){this.logInfo("drawDeferred() called when object is already pending drawing "+"- No action to take.");return}
var _2=this.getID();this.deferredDrawEvent=isc.Page.setEvent(_1,"delete "+_2+".deferredDrawEvent;"+_2+".draw();",isc.Page.FIRE_ONCE)}
,isc.A.getPrintHTML=function(printProperties,callback){this.isPrinting=true;printProperties=isc.addProperties({},printProperties,this.printProperties);if(printProperties.topLevelCanvas==null){printProperties.topLevelCanvas=this;printProperties.isDrawn=this.isDrawn();printProperties.isVisible=this.isVisible()}
if(printProperties.omitControls==null)
printProperties.omitControls=isc.Canvas.printOmitControls;if(printProperties.includeControls==null)
printProperties.includeControls=isc.Canvas.printIncludeControls;this.currentPrintProperties=printProperties||{};var HTML=[this.getPrintTagStart(),,,this.getPrintTagEnd()];if(!this.children||this.children.length==0||this.allowContentAndChildren){HTML[1]=this.getPrintInnerHTML()}
delete printProperties.inline;if(printProperties.omitComponents){var omitComponents=printProperties.omitComponents
for(var i=0;i<omitComponents.length;i++){if(isc.isA.String(omitComponents[i]))
omitComponents[i]=window[omitComponents[i]];if(!isc.isAn.Instance(omitComponents[i]))omitComponents[i]=[]}
omitComponents.removeEmpty()}
var children=this.getPrintChildren();var self=this;var completePrintHTML=this.getCompletePrintHTMLFunction(HTML,callback);if(children){var childrenHTML=[],childCount=children.length,completedCount=0;var childHTMLComplete=function(childIndex,html){childrenHTML[childIndex]=html;++completedCount;if(completedCount==childCount){return completePrintHTML(childrenHTML)}
return null}
var thisHTML=null;for(var i=0;i<childCount;i++){var child=children[i];var func=(function(i){return function(html){childHTMLComplete(i,html)}})(i);var childHTML=this.getChildPrintHTML(child,printProperties,func);if(childHTML!==null){thisHTML=childHTMLComplete(i,childHTML)}}
return thisHTML}else{return completePrintHTML()}}
,isc.A.getChildPrintHTML=function isc_Canvas_getChildPrintHTML(_1,_2,_3){return _1.getPrintHTML(_2,_3)}
,isc.A.getCompletePrintHTMLFunction=function isc_Canvas_getCompletePrintHTMLFunction(_1,_2){var _3=this;return function(_4){_3.isPrinting=false;if(isc.isAn.Array(_4))_4=_4.join(isc.emptyString);if(_4)_1[2]=_4;_1=_1.join(isc.emptyString);delete _3.currentPrintProperties;if(_2){_3.fireCallback(_2,"HTML,callback",[_1,_2]);return null}else{return _1}}}
,isc.A.getPrintInnerHTML=function isc_Canvas_getPrintInnerHTML(){var _1=this.children!=null&&this.children.length>0;var _2=this.$t6();if(_1&&_2==this.$sd)return null;return _2}
,isc.A.getPrintChildren=function isc_Canvas_getPrintChildren(){var _1=this.children;if(!_1||_1.length==0)return;var _2=[];for(var i=0;i<_1.length;i++){if(this.shouldPrintChild(_1[i]))_2.add(_1[i])}
return(_2.length>0)?_2:null}
,isc.A.shouldPrintChild=function isc_Canvas_shouldPrintChild(_1){if(_1.shouldPrint!=null)return _1.shouldPrint;if(_1.masterElement)return false;var _2=this.currentPrintProperties,_3=_2.omitControls,_4=_2.omitComponents;if(!isc.isAn.Instance(_1)||(_4&&_4.contains(_1)))
{return false}
if(_3){var _5=_2.includeControls;if(_5&&_5.length>0){for(var i=0;i<_5.length;i++){var _7=_5[i];if(isc.isA[_7]&&isc.isA[_7](_1))return true}}
for(var i=0;i<_3.length;i++){var _7=_3[i];if(isc.isA[_7]&&isc.isA[_7](_1)){return false}}}
if((!_1.isDrawn()&&_2.isDrawn)||(!_1.isVisible()&&_2.isVisible))return false;return true}
,isc.A.$us=function isc_Canvas__fixPNG(){if(this.isPrinting)return false;return true}
,isc.A.getPrintStyleName=function isc_Canvas_getPrintStyleName(){return this.printStyleName||this.styleName}
,isc.A.getPrintTagStart=function isc_Canvas_getPrintTagStart(){var _1=this.currentPrintProperties,_2=_1.topLevelCanvas==this,_3=!_2&&_1.inline,_4=this.getPrintStyleName();return[(_3?"<span ":"<div "),(_4?"class='"+_4+"' ":null),this.getPrintTagStartAttributes(),">"].join(isc.emptyString)}
,isc.A.getPrintTagStartAttributes=function isc_Canvas_getPrintTagStartAttributes(){return null}
,isc.A.getPrintTagEnd=function isc_Canvas_getPrintTagEnd(){var _1=this.currentPrintProperties,_2=_1.topLevelCanvas==this,_3=!_2&&_1.inline;return _3?"</span>":"</div>"}
,isc.A.makeBackMask=function isc_Canvas_makeBackMask(_1){if(isc.Browser.isMoz&&!isc.Page.isLoaded()){this.$ut=_1;isc.Page.setEvent("load",this,isc.Page.FIRE_ONCE,"makeBackMask");return}
if(this.$ut){_1=this.$ut;delete this.$ut}
this._backMask=isc.BackMask.create(_1);this.addPeer(this._backMask);this._backMask.setZIndex(this.getZIndex(true)-2);this.$uu()}
,isc.A.makeFocusProxy=function isc_Canvas_makeFocusProxy(){if(!this._useFocusProxy||this.$uv||this.$uw||!this.isDrawn()||this.$ux!=null)return;this.$uw=true;this.$uy();this.$uw=null}
,isc.A.$uy=function isc_Canvas__makeFocusProxy(){if(!isc.Page.isLoaded()&&isc.Browser.isSafari){this.getTabIndex();this.$ux=isc.Page.setEvent("load",this,null,"delayedMakeFocusProxy");return}
var _1=this.getTabIndex();if(this.isDisabled())_1=-1;if(isc.Browser.isSafari&&_1==-1){return}
var _2=(isc.Browser.isSafari?1:this.getViewportWidth()),_3=(isc.Browser.isSafari?1:this.getViewportHeight());var _4=isc.Canvas.getFocusProxyString(this.getCanvasName(),true,this.getOffsetLeft()-1,this.getOffsetTop()-1,_2,_3,this.isVisible(),this.$mb(),_1,this.accessKey,false,this.$uz(),this.$u0());isc.Element.insertAdjacentHTML(this.getClipHandle(),"afterEnd",_4)
this.$uv=true}
,isc.A.delayedMakeFocusProxy=function isc_Canvas_delayedMakeFocusProxy(){this.$ux=null;this.makeFocusProxy()}
,isc.A.$u1=function isc_Canvas__clearFocusProxy(){if(!this._useFocusProxy)return;if(this.$ux!=null){isc.Page.clearEvent("load",this.$ux);this.$ux=null}
if(!this.$uv)return;var _1=this.$u2();if(_1!=null){if(isc.Browser.isDOM){if(_1.parentNode){_1.parentNode.removeChild(_1)}else{this.logWarn("Unable clear focusProxy for this widget - element has no parentNode.")}}
this.$u3=null}
this.$uv=null}
,isc.A.$uj=function isc_Canvas__useAccessKeyProxy(){return(isc.Browser.isMoz&&this._useNativeTabIndex)}
,isc.A.$uk=function isc_Canvas__makeAccessKeyProxy(){var _1=this.accessKey;if(!_1||!this.isDrawn()||!this.$mb())return;var _2=this.$t4("focusProxy");var _3=isc.StringBuffer.concat("<a id='",_2,"' href='javascript:void(0)","' onfocus='var _0=window.",this.getID(),";if(_0){_0.focus()}' ","accessKey='"+_1+"'></a>");isc.Element.insertAdjacentHTML(this.getClipHandle(),"beforeEnd",_3);this.$u4=isc.Element.get(_2)}
,isc.A.$u5=function isc_Canvas__clearAccessKeyProxy(){var _1=this.$u4;delete this.$u4;if(_1)isc.Element.clear(_1)}
,isc.A.drawChildren=function isc_Canvas_drawChildren(){if(this.children==null)return true;if(this.isDrawn()){this.logWarn("drawChildren() is only safe to call BEFORE a canvas has been drawn"+this.getStackTrace());return}
if(this.children&&this.logIsInfoEnabled(this.$r5)){this.logInfo("drawChildren(): "+this.children.length+" children",this.$r5)}
this.$tz();this.layoutChildren(this.$se);if(this.manageChildOverflow)this.$u6=true;for(var i=0;i<this.children.length;i++){var _2=this.children[i];if(_2.masterElement)continue;if(!_2.isDrawn())_2.draw()}}
,isc.A.$u7=function isc_Canvas__completeChildOverflow(_1){if(!this.manageChildOverflow)return;this.$u6=null;this.$u8();var _2=0;for(var i=0;i<_1.length;i++){var _4=_1[i];if(_4!=null&&_4.$u9){_2++;_4.$u9=null;_4.adjustOverflow(this.$sf)}}}
,isc.A.predrawPeers=function isc_Canvas_predrawPeers(){if(!this.peers)return;for(var i=0;i<this.peers.getLength();i++){var _2=this.peers[i];if(_2.$va==true){if(!isc.isA.Canvas(_2)||_2.masterElement!=this){this.peers.remove(_2);this.addPeer(_2)}
if(!_2.isDrawn())_2.draw()}}}
,isc.A.drawPeers=function isc_Canvas_drawPeers(){if(!this.peers)return true;if(this.logIsInfoEnabled(this.$r5)){this.logInfo("drawPeers(): "+this.peers.length+" peers","drawing")}
var _1=this.peers;this.peers=[];for(var i=0,_3;i<_1.length;i++){_3=_1[i];if(!isc.isA.Canvas(_3)||_3.masterElement!=this){this.addPeer(_3)}else{this.peers.add(_3)}}
for(i=0;i<this.peers.length;i++){var _3=this.peers[i];if(_3.snapTo||_3.snapEdge)_3.$vb();if(!_3.isDrawn())_3.draw()}}
,isc.A.$uf=function isc_Canvas__insertHTML(_1){this.setDrawnState(isc.Canvas.DRAWING_HANDLE);var _2=_1?this.$t6():null,_3=this.getTagStart(true),_4=isc.isAn.Array(_3),_5;if(_4){var _6=_3.length;_3[_3.length]=_2;_3[_3.length]=this.getTagEnd();_5=_3.join(isc.$ah);_3.length=_6}else{_5=isc.SB.concat(_3,_2,this.getTagEnd())}
var _7;var _8=this.logIsInfoEnabled(this.$r5);var _9=this.drawContext;if(_9){var _10=_9.element,_11=_9.position||"beforeBegin";this.logInfo("$uf(): drawing with "+_11+" relative to element: "+this.echoLeaf(_10),"drawing");if(_11=="replace"){_11="beforeBegin";if(isc.isA.String(_10))_10=isc.Element.get(_10);_7=this.$vc(_10,_11,_5,true);_10.parentNode.removeChild(_10);this.drawContext=null;if(this.htmlElement)this.htmlElement=null}else{_7=this.$vc(_10,_11,_5,true)}}else if(this.masterElement&&(this.masterElement.getClipHandle()!=null)){if(_8){this.logInfo("inserting HTML next to master element: "+this.masterElement,"drawing")}
var _12=this.masterElement.getClipHandle();_7=this.$vc(_12,this.$pj,_5,true)}else if(this.parentElement){if(_8){this.logInfo("inserting HTML into parent: "+this.parentElement,"drawing")}
var _13=this.parentElement.getHandle();_7=this.$vc(_13,this.$pl,_5,true)}else{if(_8){this.logDebug("inserting HTML at top level","drawing")}
_7=this.$vd(_5)}
if(!(isc.Browser.isIE||isc.Browser.isOpera)){if(_7!=null){if(this.useClipDiv){this._clipDiv=_7;this.$ve=_7.firstChild}else{this.$ve=_7}}else{}}
this.setDrawnState(isc.Canvas.HANDLE_DRAWN)}
,isc.A.$vd=function isc_Canvas__createAbsoluteElement(_1){return this.ns.Element.createAbsoluteElement(_1)}
,isc.A.$vc=function isc_Canvas__insertAdjacentHTML(_1,_2,_3,_4){return this.ns.Element.insertAdjacentHTML(_1,_2,_3,_4)}
,isc.A.$uh=function isc_Canvas__completeHTMLInit(){this.modifyContent();if(isc.Browser.isMoz&&isc.Browser.isStrict&&this.containsIFrame())this.$t5();if(this.manageChildOverflow&&this.children!=null){this.$u7(this.children)}
this.setUpEvents();if(this.$vf){this.$vg(this.left,this.top,this.width,this.$s9);var _1=this.$vh;if(isc.isAn.Array(_1))this.setClip(_1)}
this.setDrawnState(isc.Canvas.COMPLETE);this.$vi=false;if(this.parentElement==null)isc.Canvas.$vj(this);if(this.parentElement!=null&&this.parentElement.$u6){this.$u9=true}else{this.adjustOverflow(this.$r0)}
this.drawPeers()}
,isc.A.setHtmlElement=function isc_Canvas_setHtmlElement(_1){if(this.htmlElement==_1)return;this.htmlElement=_1;if(!this.htmlPosition)this.htmlPosition="afterBegin";var _2=_1?{position:this.htmlPosition,element:this.htmlElement}:null;this.setDrawContext(_2)}
,isc.A.setHtmlPosition=function isc_Canvas_setHtmlPosition(_1){if(_1==null)_1="afterBegin";if(this.htmlPosition==_1)return;this.htmlPosition=_1;if(this.htmlElement==null)return;var _2={position:this.htmlPosition,element:this.htmlElement};this.setDrawContext(_2)}
,isc.A.isDirty=function isc_Canvas_isDirty(){return this.$vi==true}
,isc.A.markForRedraw=function isc_Canvas_markForRedraw(_1){if(isc.$dd)arguments.$de=this;if(this.isDrawn()&&!this.isDirty()){this.$vk(_1);isc.Canvas.scheduleRedraw(this);this.$vi=true}}
,isc.A.readyToRedraw=function isc_Canvas_readyToRedraw(_1,_2){if(isc.$dd)arguments.$de=this;if(!this.isDrawn()){return false}
var _3=this.ns.EH;if(_3.lastTarget==this&&(_3.$mk||_3.$md||(isc.Browser.isMobileWebkit&&_3.dragOperation==_3.DRAG_SCROLL)))
{if(_2){this.$vk(_1,true);this.priorityRedraw=true;this.$vi=false;this.markForRedraw(false)}
return false}
return true}
,isc.A.$vk=function isc_Canvas__logRedraw(_1,_2){if(_1==false||!this.logIsInfoEnabled(this.$r6))return;var _3=(!_1&&this.logIsDebugEnabled(this.$r6)||this.logIsDebugEnabled("redrawTrace"));var _4;if(_2==null)_4="Scheduling redraw ";else _4=(_2==true?"DEFERRED ":"")+"Immediate redraw ";this.logInfo(_4+(this.isDirty()&&_2!=null?"of dirty widget ":"")+(this.children&&this.children.length>0?"("+this.getChildCount()+" children) ":"")+"("+(_1?_1:"no reason provided")+")"+(_3?this.getStackTrace():""),this.$r6)}
,isc.A.redraw=function isc_Canvas_redraw(_1){if(isc.$dd)arguments.$de=this;if(!this.readyToRedraw(_1,true))return this;this.$vk(_1,false);this.$ue(this.$r6);var _2=isc.timeStamp();this.$vl();this.$vm=isc.timeStamp()-_2;return this}
,isc.A.redrawIfDirty=function isc_Canvas_redrawIfDirty(_1){if(this.isDrawn()&&this.isDirty())return this.redraw(_1)}
,isc.A.$vl=function isc_Canvas__updateHTML(){var _1=this.logIsDebugEnabled(this.$r5),_2=this.logIsInfoEnabled(this.$r5),_3;if(_1)_3=isc.timeStamp();if(_2)this.logInfo("$vl(): redrawing","drawing");if(this.peers!=null&&this.peers.getLength()>0)this.redrawPredrawnPeers();var _4=this.children&&this.children.length>0,_5=this.allowContentAndChildren&&_4;if(_4&&!_5&&this.shouldRedrawOnResize()){_5=true}
if((!_4||_5)&&(this.getVisibleWidth()>this.getWidth()||this.getVisibleHeight()>this.getHeight()))
{if(this.notifyAncestorsOnReflow&&this.parentElement!=null){this.notifyAncestorsAboutToReflow()}
this.$vg(null,null,this.width,this.$s9)}
if(_4){if(_5)this.$ug();this.redrawChildren()}else{this.$vn()}
if(this.$ul&&!_4){delete this.$vo;this.enforceScrollSize(this.$ul[0],this.$ul[1])}
this.modifyContent();this.setUpEvents();this.$vi=false;this.adjustOverflow(this.$r2,null,true);this.redrawPeers();if(_1){this.logDebug("Redraw() - Total time to redraw in DOM:"+(isc.timeStamp()-_3),"drawing")}
if(this.notifyAncestorsOnReflow&&this.parentElement!=null){this.notifyAncestorsReflowComplete()}
return this}
,isc.A.notifyAncestorsAboutToReflow=function isc_Canvas_notifyAncestorsAboutToReflow(){if(this.parentElement)this.parentElement.$vp(this)}
,isc.A.notifyAncestorsReflowComplete=function isc_Canvas_notifyAncestorsReflowComplete(){if(this.parentElement)this.parentElement.$vq(this)}
,isc.A.$vp=function isc_Canvas__childAboutToReflow(_1){if(this.overflow!=isc.Canvas.VISIBLE){this.$vr=this.getScrollTop();this.$vs=this.getScrollLeft();this.$vt=true}else{if(this.parentElement)this.parentElement.$vp(_1)}}
,isc.A.$vq=function isc_Canvas__childReflowComplete(_1){if(this.overflow!=isc.Canvas.VISIBLE){delete this.$vt;var _2=false,_3,_4;if(this.$vr!=null&&this.$vr!=this.getScrollTop()){_2=true;_4=this.$vr}
if(this.$vs!=null&&this.$vs!=this.getScrollLeft()){_2=true;_4=this.$vr}
if(_2){this.scrollTo(_3,_4,"Reset scroll position for child content reflow")}}else{if(this.parentElement)this.parentElement.$vq(_1)}}
,isc.A.$ug=function isc_Canvas__updateParentHTML(){var _1=this.$t6(),_2=this.getHandle();while(_2.hasChildNodes()){var _3=_2.firstChild.getAttribute?_2.firstChild.getAttribute(this.$sg):null;if(_3&&isc.isA.Canvas(window[_3]))break;_2.removeChild(_2.firstChild)}
isc.Element.insertAdjacentHTML(_2,this.$pi,_1)}
,isc.A.$vn=function isc_Canvas__updateInnerHTML(){var _1=this.isPrinting;this.isPrinting=false;var _2=this.$t6();this.getHandle().innerHTML=_2;this.isPrinting=_1}
,isc.A.modifyContent=function isc_Canvas_modifyContent(){}
,isc.A.redrawChildren=function isc_Canvas_redrawChildren(){if(!this.children)return true;this.logInfo("redrawChildren(): "+this.children.length+" children","drawing");for(var _1=this.children,i=0;i<_1.length;i++){var _3=_1[i];if(!isc.isA.Canvas(_3))continue;if(_3._redrawWithParent){_3.redraw(false)}}}
,isc.A.redrawPredrawnPeers=function isc_Canvas_redrawPredrawnPeers(){if(!this.peers||this.peers.getLength<1)return;for(var _1=this.peers,i=0;i<_1.length;i++){if(_1[i]&&_1[i].$k7&&_1[i].$va){_1[i].redraw("redrawPeers")}}}
,isc.A.redrawPeers=function isc_Canvas_redrawPeers(){if(!this.peers)return true;this.logInfo("redrawPeers(): "+this.peers.length+" peers","drawing");for(var _1=this.peers,i=0;i<_1.length;i++){if(_1[i]&&_1[i].$k7&&!_1[i].$va){_1[i].redraw("redrawPeers")}}}
,isc.A.updateFromServer=function isc_Canvas_updateFromServer(_1){_1=isc.clone(_1);isc.addProperties(_1,{useXmlHttpRequest:true,evalResult:true,suppressAutoDraw:true});if(!_1.evalVars)_1.evalVars={};if(!_1.evalVars.targetComponent)_1.evalVars.targetComponent=this;isc.rpc.sendRequest(_1)}
,isc.A.refreshFromServer=function isc_Canvas_refreshFromServer(_1,_2,_3,_4){this.$vu("refresh",_1,_2,_3,_4)}
,isc.A.replaceFromServer=function isc_Canvas_replaceFromServer(_1,_2,_3,_4){this.$vu("replace",_1,_2,_3,_4)}
,isc.A.$vu=function isc_Canvas__refreshOrReplaceFromURL(_1,_2,_3,_4,_5){if(this.$vv){this.logWarn("Attempt to "+_1+" while "+this.$vw+" is in progress - ignoring.");return}
this.$vv=true;this.$vw=_1;this.$vx=_5;this.logDebug("Submitting to "+_1+" URL: "+_2+", with data: "+this.echo(_3));isc.Comm.sendFieldsToServer({URL:_2,fields:_3,prompt:_4,callback:this.getID()+".$vy(frame)",resultVarName:this.refreshVariable})}
,isc.A.$vy=function isc_Canvas__refreshReply(_1){this.$vv=false;var _2=this.$vw;var _3=_1[this.refreshVariable];if(!isc.isAn.Object(_3)){this.logError("Expected object literal for "+_2+", but got: "+isc.Log.echo(_3));return}
_3=isc.clone(_3);var _4=this;if(_2=="refresh")this.setProperties(_3);else{if(!_3._constructor)_3._constructor=this.getClassName();_4=this.replaceWith(_3)}
isc.clearPrompt();if(this.$vx){if(!isc.isA.Function(this.$vx)){this.$vx=isc.Func.expressionToFunction("canvas",this.$vx)}
if(!isc.isA.Function(this.$vx)){this.logError("Can't convert "+_2+" callback '"+this.$vx+" to a function - not firing callback!");return}
this.$vx(_4)}}
,isc.A.clear=function isc_Canvas_clear(_1){this.$vz=true;if(!_1&&this.logIsInfoEnabled("clears")){var _2="clear()"+(this.children&&this.children.length>0?" ("+this.getChildCount()+" children) ":"")+(this.logIsDebugEnabled("clears")?this.getStackTrace():"");this.logInfo(_2,"clears")}
this.$v0();if(this._eventMask)this.ns.EH.unregisterMaskableItem(this);if(this==isc.Canvas.$v1)isc.Canvas.hideResizeThumbs();if(this._useFocusProxy)this.$u1();if(this.children){for(var _3=this.children,i=0;i<_3.length;i++){var _5=_3[i];if(!isc.isA.Canvas(_5))continue;_5.$v2=true;_5.clear(true);_5.$v2=null}}
if(this.getHandle())this.clearHandle();if(this.parentElement)this.parentElement.childCleared(this);if(this.masterElement)this.masterElement.peerCleared(this);delete this.$vo;delete this.$u4;if(this.deferredDrawEvent){isc.Page.clearEvent(this.deferredDrawEvent);delete this.deferredDrawEvent}
if(this.peers){for(var _3=this.peers,i=0;i<_3.length;i++){if(this.$v2)_3[i].$v2=true;_3[i].clear(true);_3[i].$v2=null}}
if(this.canAcceptDrop)this.ns.EH.unregisterDroppableItem(this);this.setDrawnState(isc.Canvas.UNDRAWN);this.$ps=this.$pt=null;delete this.$vz}
);isc.evalBoundary;isc.B.push(isc.A.destroy=function isc_Canvas_destroy(_1){if(this.doNotDestroy){this.clear();return}
if(this.destroyed)return;this.destroying=true;if(this.$v3)isc.Timer.clearTimeout(this.$v4);this.hideClickMask();this.$v5(true,_1);if(isc.Hover.lastHoverCanvas==this)isc.Hover.hide();this.clear(true);this.deparent();this.depeer();if(this.children){for(var _2=this.children.duplicate(),i=0;i<_2.length;i++){var _4=_2[i];if(!isc.isA.Canvas(_4))continue;_4.destroy(true)}}
if(this.peers){for(var _2=this.peers.duplicate(),i=0;i<_2.length;i++){_2[i].destroy(true)}}
delete this.peers;delete this.children;if(this.hscrollbar&&!this.hscrollbar.destroyed){this.hscrollbar.destroy(true);delete this.hscrollbar}
if(this.vscrollbar&&!this.vscrollbar.destroyed){this.vscrollbar.destroy(true);delete this.vscrollbar}
if(this.$ep){var _5=this.$ep;for(var _6 in _5){var _7=_5[_6];for(var i=0;i<_7.length;i++){var _8=_7[i],_4=_8?window[_8]:null;if(_4&&!_4.destroyed&&_4.destroy&&!_4.dontAutoDestroy)
{_4.destroy()}}
delete this[_6]}}
if(this.eventProxy!=null)this.clearEventProxy();if(this.$mj!=null){for(var _2=this.$mj.duplicate(),i=0;i<_2.length;i++){_2[i].clearEventProxy()}}
if(this.locatorParent&&this.locatorParent.locatorChildDestroyed){this.locatorParent.locatorChildDestroyed(this)}
delete this.locatorParent;this._canvasList();isc.Canvas.$v6(this);this.$v7();isc.EH.canvasDestroyed(this);isc.ClassFactory.dereferenceGlobalID(this);if(this.pointersToThis!=null){for(var i=0;i<this.pointersToThis.length;i++){var _9=this.pointersToThis[i];if(_9.object&&(_9.object[_9.property]==this)){var _10;_9.object[_9.property]=_10}}
delete this.pointersToThis}
if(this.$v8){for(var _11 in this){delete this[_11]}}
this.$v9();this.destroyed=true}
,isc.A.markForDestroy=function isc_Canvas_markForDestroy(){if(isc.$dd)arguments.$de=this;if(this.destroyed||this.destroying||this.isPendingDestroy())return;this.$wa=true;this.$v5(false,false);isc.Canvas.scheduleDestroy(this)}
,isc.A.isPendingDestroy=function isc_Canvas_isPendingDestroy(){return!this.destroyed&&!this.destroying&&(this.$wa==true)}
,isc.A.$v5=function isc_Canvas__logDestroy(_1,_2){if(this.$wb)return;if(_1)this.$ue("destroys");if(!_2&&this.logIsInfoEnabled("destroys")){this.logInfo((_1?"destroy()":"markForDestroy()")+(this.children&&this.children.length>0?" ("+this.getChildCount()+" children) ":"")+(this.logIsDebugEnabled("destroys")?this.getStackTrace():""),"destroys")}}
,isc.A.clearHandle=function isc_Canvas_clearHandle(){if(!this.getHandle())return;this.$ue("clears");this.getHandle().eventProxy=null;this.getClipHandle().eventProxy=null;var _1=this.getClipHandle();this.$ve=null;this.$wc=null;this._clipDiv=null;isc.Element.clear(_1,this._clearWithRemoveChild)}
,isc.A.replaceWith=function isc_Canvas_replaceWith(_1){if(!isc.isAn.Object(_1))return;var _2;if(isc.Browser.isDOM){var _3=isc.ClassFactory.getNextGlobalID();isc.Element.insertAdjacentHTML(this.getClipHandle(),"afterEnd","<DIV ID="+_3+"></DIV>");var _2=this.getDocument().getElementById(_3);_1.drawContext={element:_2}}
var _4=this.parentElement,_5=this.masterElement,_6=(isc.isA.Layout(_4)&&_4.hasMember(this)),_7=(_6?_4.getMemberNumber(this):0);this.destroy();if(isc.isA.Canvas(_1)){_1.clear()}else{_1.autoDraw=false;_1=isc.ClassFactory.newInstance(_1);if(_1==null){this.logWarn("canvas.replaceWith(): Unable to create a widget "+"instance from the argument passed in.  Returning.")
return}}
if(_6){_4.addMember(_1,_7)}else if(_4){_4.addChild(_1)}else if(_5){_5.addPeer(_1)}
if(!_1.isDrawn())_1.draw();if(isc.Browser.isDOM){if(_2.parentNode){_2.parentNode.removeChild(_2)}else{this.logWarn("unable to clear marker")}}
return _1}
,isc.A.setDrawContext=function isc_Canvas_setDrawContext(_1){var _2=this.isDrawn();this.deparent();if(_2)this.clear();this.drawContext=_1;if(_2)this.draw()}
,isc.A.$t4=function isc_Canvas__getDOMID(_1,_2,_3){if(_2){var _4=isc.ClassFactory.getDOMID(this.getID(),_1);if(this.reuseDOMIDs){if(!this.$wd)this.$wd=[];this.$wd[this.$wd.length]=_4}
return _4}
if(!this.$we)this.$we={};if(!this.$we[_1])
this.$we[_1]=isc.ClassFactory.getDOMID(this.getID(),_1);return this.$we[_1]}
,isc.A.$wf=function isc_Canvas__getDOMPartName(_1){if(!this.$we)return null;for(var _2 in this.$we){if(this.$we[_2]==_1)return _2}}
,isc.A.$v9=function isc_Canvas__releaseDOMIDs(){if(!this.reuseDOMIDs)return;if(this.$wd){for(var i=0;i<this.$wd.length;i++){isc.ClassFactory.releaseDOMID(this.$wd[i])}}
if(this.$we){for(var i in this.$we){isc.ClassFactory.releaseDOMID(this.$we[i])}}}
,isc.A.getCanvasName=function isc_Canvas_getCanvasName(){if(!this.$wg)this.$wg=this.$t4(this.$sh,true);return this.$wg}
,isc.A.$wh=function isc_Canvas__getClipDivDOMID(){return this.$t4(this.$si)}
,isc.A.getTransformCSS=function isc_Canvas_getTransformCSS(){if(this.rotation!=null)return";"+isc.Element.getRotationCSS(this.rotation,this.transformOrigin);return null}
,isc.A.getTagStart=function isc_Canvas_getTagStart(_1){var _2=isc.Canvas,_3=this.$wi();if(this.zIndex==_2.AUTO)this.zIndex=_2.getNextZIndex();var _4=(this.eventProxy?this.eventProxy.ID:this.ID);var _5=this.$wj(),_6=_5[0],_7=_5[1];if(!_2.$wk){_2.$wk=" onfocus=";_2.$wl=" onblur=";_2.$wm=" tabindex="
_2.$wn=" accessKey="}
var _8=isc.Browser.isMoz;var _9=this.opacity;if(!isc.Browser.isIE){if(_9!=null)_9=_9/ 100}
if(isc.Browser.isMoz){if(this.smoothFade&&(_9==1||_9==null))_9=0.9999}
if(this.useClipDiv){var _10=this.getCurrentCursor(),_11,_12,_13=this._useNativeTabIndex;if(this.clipHandleIsFocusHandle==false)_13=false;if(_13&&this.$mb()){_11=isc.SB.concat(_2.$wk,this.$uz(),_2.$wl,this.$u0(),!this.isDisabled()?_2.$wm+this.getTabIndex():null,(!this.$uj()&&this.accessKey!=null)?_2.$wn+this.accessKey:null);if(isc.Browser.isMoz){_12=isc.StringBuffer.concat((this.mozOutlineOffset!=null?";-moz-outline-offset:"+this.mozOutlineOffset:null),(this.mozOutlineColor!=null?";-moz-outline-color:"+this.mozOutlineColor:null),(!this.showFocusOutline?";-moz-outline-style:none":null))}else if(isc.Browser.isSafari){if(!this.showFocusOutline){_12=";outline-style:none"}}}
var _14=isc.Browser.isMoz&&isc.Browser.geckoVersion>=20080529;var _15=isc.StringBuffer.concat("<div id='",this.$wh(),"' eventProxy=",_4,(_14&&this.ariaRole?" role='"+this.ariaRole+"'":""),(_14&&this.ariaState?this.getAriaStateAttributes():""),(this.className?" class='"+this.className+"'":""),_11," style='","POSITION:",this.position,";LEFT:",this.left,"px;TOP:",this.top,"px;WIDTH:",_6,"px;HEIGHT:",_7,"px;Z-INDEX:",this.zIndex,(this.visibility==_2.INHERIT?"":";VISIBILITY:"+this.visibility),(this.backgroundColor==null?"":";BACKGROUND-COLOR:"+this.backgroundColor),(this.backgroundImage==null?"":";BACKGROUND-IMAGE:url("+this.getImgURL(this.backgroundImage)+")"+";BACKGROUND-REPEAT:"+this.backgroundRepeat+(this.backgroundPosition?";BACKGROUND-POSITION:"+this.backgroundPosition:"")),(this.border?";BORDER:"+this.border:""),(this.padding!=null||this.$wo?";PADDING:0px":""),this.$wp(),(_9!=null?(this.$rt?";-moz-opacity:":";opacity:")+_9:""),(_8?";-moz-box-sizing:border-box":null),_12,this.getTransformCSS(),(isc.Browser.isTouch?(!this.canSelectText?";-webkit-user-select:none":";-webkit-user-select:text"):null),";OVERFLOW:",_3,";' ONSCROLL='return "+_4+".$nd()'>","<div id='",this.getCanvasName(),"' eventProxy='",_4,(this.textDirection!=null?"' dir='"+this.textDirection:""),"' style='POSITION:relative;VISIBILITY:inherit",";Z-INDEX:",this.zIndex,(_10==_2.AUTO?"":";CURSOR:"+_10),(this.padding!=null?";PADDING:"+this.padding+"px":""),(this.topPadding!=null?";padding-top:"+this.topPadding+"px":""),(this.bottomPadding!=null?";padding-bottom:"+this.bottomPadding+"px":""),(this.leftPadding!=null?";padding-left:"+this.leftPadding+"px":""),(this.rightPadding!=null?";padding-right:"+this.rightPadding+"px":""),";'>")}else{if(!_2.$wq){_2.$wr=" style='POSITION:absolute;LEFT:";_2.$ws=" style='POSITION:relative;LEFT:";_2.$bh=" class='";_2.$wt="'";_2.$wu=";VISIBILITY:";_2.$wv=";CURSOR:";var _16=_2.$wq=[];_16[0]="<div id=";_16[2]=" eventProxy=";_16[15]="px;TOP:";_16[22]="px;WIDTH:";_16[28]="px;HEIGHT:";_16[34]="px;Z-INDEX:";_16[44]=";OVERFLOW:";_16[59]="' ONSCROLL='return ";_16[61]=".$nd()' "}
var _16=_2.$wq;_16[1]=this.getCanvasName();_16[3]=_4;if(this.className!=null){_16[4]=_2.$bh;_16[5]=this.className;_16[6]=_2.$wt}else{_16[4]=_16[5]=_16[6]=null}
_16[7]=(this.textDirection!=null?" dir="+this.textDirection:null);_16[8]=(this.position==_2.RELATIVE?_2.$ws:_2.$wr);isc.$bp(_16,this.left,9,6);isc.$bp(_16,this.top,16,6);isc.$bp(_16,_6,23,5);isc.$bp(_16,_7,29,5);if(this.zIndex!=_2.AUTO)isc.$bp(_16,this.zIndex,35,9);else{_16[35]=this.zIndex;_16[36]=_16[37]=_16[38]=_16[39]=_16[40]=_16[41]=_16[42]=_16[43]=null}
_16[45]=_3;if(this.visibility!=_2.INHERIT){_16[46]=_2.$wu;_16[47]=this.visibility}else{_16[46]=_16[47]=null}
_16[48]=(this.backgroundColor==null?null:";BACKGROUND-COLOR:"+this.backgroundColor);_16[49]=(this.backgroundImage==null?null:";BACKGROUND-IMAGE:url("+this.getImgURL(this.backgroundImage)+");BACKGROUND-REPEAT:"+this.backgroundRepeat+(this.backgroundPosition?";BACKGROUND-POSITION:"+this.backgroundPosition:""));_16[50]=(_8?";-moz-box-sizing:border-box":null);var _10=this.getCurrentCursor();if(_10==_2.AUTO){_16[51]=_16[52]=null}else{_16[51]=_2.$wv;_16[52]=_10}
_16[53]=this.$wp();_16[54]=(this.padding!=null?";PADDING:"+this.padding+isc.px:null);if(this.topPadding!=null)
_16[54]=(_16[54]||"")+";padding-top:"+this.topPadding+"px";if(this.bottomPadding!=null)
_16[54]=(_16[54]||"")+";padding-bottom:"+this.bottomPadding+"px";if(this.leftPadding!=null)
_16[54]=(_16[54]||"")+";padding-left:"+this.leftPadding+"px";if(this.rightPadding!=null)
_16[54]=(_16[54]||"")+";padding-right:"+this.rightPadding+"px";_16[55]=(this.border?";BORDER:"+this.border:null);if(isc.Browser.isIE){if(!isc.Canvas.neverUseFilters||this.useOpacityFilter){_16[56]=(_9==null?null:";filter:progid:DXImageTransform.Microsoft.Alpha(opacity="+_9+")")}else{_16[56]=null}
if(!isc.Canvas.neverUseFilters){if(this.$ww){_16[57]=";filter:progid:DXImageTransform.Microsoft.iris(irisStyle=circle)"}else{_16[57]=null}}else{_16[57]=null}}else{if(_9!=null){_16[56]=(this.$rt?";-moz-opacity:":";opacity:")+_9}else{_16[56]=null}}
_16[58]=this.getTransformCSS();_16[60]=_4;var _17=64;if(this.$mb()&&this._useNativeTabIndex&&this.clipHandleIsFocusHandle){_16[64]=_2.$wk;_16[65]=this.$uz();_16[66]=_2.$wl;_16[67]=this.$u0();if(!this.isDisabled()){_16[68]=_2.$wm;isc.$bp(_16,this.getTabIndex(),69,5);if(this.accessKey!=null){_16[74]=_2.$wn;_16[75]=this.accessKey;_17=76}else _17=74;if(!this.showFocusOutline){if(!_2.$wx)_2.$wx=" hideFocus=true";_16[_17]=_2.$wx;_17+=1}}else _17=68}
if((this.ariaRole||this.ariaState)&&isc.Canvas.ariaEnabled()&&!isc.Canvas.useLiteAria())
{if(this.ariaRole){_16[_17++]=" role='";_16[_17++]=this.ariaRole;_16[_17++]="' "}
if(this.ariaState){_16[_17++]=this.getAriaStateAttributes()}}
_16.length=_17;_16[_17]=this.$r8;if(_1)return _16;return _16.join(isc.emptyString)}
return _15}
,isc.A.$wp=function isc_Canvas__getMarginHTML(){if(!this.$wy()&&this.$wz==null){if(this.margin==null)return null;return isc.SB.concat(isc.semi,this.$sl,this.margin,isc.px)}
var _1=this.$qx(),_2=isc.SB.concat(isc.semi,this.$pp,_1.left,isc.px,isc.semi,this.$sj,_1.right,isc.px,isc.semi,this.$pq,_1.top,isc.px,isc.semi,this.$sk,_1.bottom,isc.px);return _2}
,isc.A.getTagEnd=function isc_Canvas_getTagEnd(){if(this.useClipDiv)return this.$sn;return this.$sm}
,isc.A.$wi=function isc_Canvas__getHandleOverflow(){var _1=this.overflow;var _2=(this.overflow==isc.Canvas.SCROLL||this.overflow==isc.Canvas.AUTO),_3=_2&&this.showCustomScrollbars,_4=_2&&!this.showCustomScrollbars;if(this.overflow==isc.Canvas.HIDDEN||_3)
{if(this.$mi){_1=this.$w0?this.$r1:"-moz-scrollbars-none";this.$w1=true}else{_1=this.$r1}}else if(isc.Browser.isOpera&&this.overflow==isc.Canvas.VISIBLE){_1=this.$r1}else if(isc.Browser.isMoz){if(_4)this.$w1=true;else if(this.$mi){_1=this.$w0?this.$r1:"-moz-scrollbars-none";this.$w1=true}}
if(this.useClipDiv&&(this.overflow==isc.Canvas.CLIP_H||this.overflow==isc.Canvas.CLIP_V))
{_1=this.$r1}
return _1}
,isc.A.$wj=function isc_Canvas__getInitialHandleSize(){var _1=this.getInitialWidth(),_2=this.getInitialHeight();return this.$w2(_1,_2)}
,isc.A.getInitialWidth=function isc_Canvas_getInitialWidth(){return this.getWidth()}
,isc.A.getInitialHeight=function isc_Canvas_getInitialHeight(){return this.getHeight()}
,isc.A.$w2=function isc_Canvas__adjustHandleSize(_1,_2){var _3=this.$qx();if(_1!=null){if(this.showCustomScrollbars&&this.vscrollOn){_1-=this.getScrollbarSize()}
_1-=(_3.left+_3.right);if(this.isBorderBox){}else if(this.useClipDiv){if(this.padding==null&&!this.$wo){_1-=this.getHBorderPad()}else{_1-=this.getHBorderSize()}}else{_1-=this.getHBorderPad()}}
if(_2!=null){if(this.showCustomScrollbars&&this.hscrollOn){_2-=this.getScrollbarSize()}
_2-=(_3.top+_3.bottom);if(this.isBorderBox){}else if(this.useClipDiv){if(this.padding==null&&!this.$wo){_2-=this.getVBorderPad()}else{_2-=this.getVBorderSize()}}else{_2-=this.getVBorderPad()}}
if(_1!=null&&_1<1){this.logInfo("Specified width:"+this.getInitialWidth()+" adjusted for border, margin, "+"and scrollbars would cause initial handle size to be less than or equal to "+"zero, which is not supported. Clamping handle width to 1px.","sizing");_1=1}
if(_2!=null&&_2<1){this.logInfo("Specified height:"+this.getInitialHeight()+" adjusted for border, margin, "+"and scrollbars would cause initial handle size to be less than or equal to "+"zero, which is not supported. Clamping handle height to 1px.","sizing");_2=1}
var _4=this.$so;_4[0]=_1;_4[1]=_2;return _4}
,isc.A.$uz=function isc_Canvas__getNativeFocusHandlerString(_1){var _2=this.getID();var _3=_1?null:this.$r9;if(isc.Browser.isMoz)
return isc.SB.concat(_3,this.$sq,_2,this.$ss,_3);return isc.SB.concat(_3,this.$sp,this.getID(),this.$ss,_3)}
,isc.A.$u0=function isc_Canvas__getNativeBlurHandlerString(_1){var _2=_1?null:this.$r9;return isc.SB.concat(_2,this.$sr,this.getID(),this.$ss,_2)}
,isc.A.$w3=function isc_Canvas__getNativeFocusHandlerMethod(){if(!this.$w4){this.$w4=new Function("event",this.$uz(true))}
return this.$w4}
,isc.A.$w5=function isc_Canvas__getNativeBlurHandlerMethod(){if(!this.$w6){this.$w6=new Function("event",this.$u0(true))}
return this.$w6}
,isc.A.getHandle=function isc_Canvas_getHandle(){if(isc.$dd)arguments.$de=this;if(this.destroyed){this.logWarn("Attempt to access destroyed widget in the DOM - "+"destroy() called at invalid time (eg: mid-draw) or invalid method "+"called on destroy()d widget. Stack Trace:"+this.getStackTrace())}
if(!(this.$t2||this.$t1))return null;if(this.$ve==null){var _1=this.getCanvasName();this.$ve=this.ns.Element.get(_1);if(this.$ve==null){this.logWarn("Unable to find handle for drawn Canvas, elementId: "+_1)}}
return this.$ve}
,isc.A.getClipHandle=function isc_Canvas_getClipHandle(){if(!this.useClipDiv)return this.getHandle();if(!(this.$t2||this.$t1))return null;if(this._clipDiv==null){var _1=this.$wh();this._clipDiv=this.ns.Element.get(_1);if(this._clipDiv==null){this.logWarn("Unable to find clipHandle for drawn Canvas, elementId: "+_1)}}
return this._clipDiv}
,isc.A.getScrollHandle=function isc_Canvas_getScrollHandle(){return this.getClipHandle()}
,isc.A.$hk=function isc_Canvas__getURLHandle(){if(!this.containsIFrame())return null;var _1=this.getHandle();if(!_1)return null;_1=_1.firstChild;if(_1&&_1.tagName&&(_1.tagName.toLowerCase()=="iframe"))return _1
return null}
,isc.A.$w7=function isc_Canvas__getFocusProxyHandle(){if(!this._useFocusProxy||!this.$uv)return null;if(!this.$u3){var _1=this.getCanvasName()+"__focusProxy";this.$u3=this.getDocument().getElementById(_1)}
return this.$u3}
,isc.A.$u2=function isc_Canvas__getFocusProxyParentHandle(){if(!this._useFocusProxy)return null;if(!this.$u3)this.$u3=this.$w7();return(this.$u3!=null?this.$u3.parentNode:null)}
,isc.A.getStyleHandle=function isc_Canvas_getStyleHandle(){if(!this.$wc){this.$wc=(this.getClipHandle()?this.getClipHandle().style:null)}
return this.$wc}
,isc.A.setUpEvents=function isc_Canvas_setUpEvents(){if(this.canAcceptDrop)this.ns.EH.registerDroppableItem(this)}
,isc.A.$tz=function isc_Canvas__instantiateChildren(_1){if(!_1)_1=this.children;if(!_1)return;this.children=[];for(var i=0,_3;i<_1.length;i++){_3=_1[i];if(!_3)continue;if(!isc.isA.Canvas(_3)||_3.parentElement!=this){this.addChild(_3)}else{this.children.add(_3)}}}
,isc.A.$w8=function isc_Canvas__lazyAutoChildCreate(_1){_1=_1.substring(this.$st.length);var _2=this.$el(_1);var _3=this[_2]?this:isc.isA.Canvas(this.creator)&&this.creator[_2]?this.creator:this;if(isc.isA.Canvas(_3[_1]))return _3[_1];return(_3[_1]=_3.createAutoChild(_1))}
,isc.A.createCanvas=function isc_Canvas_createCanvas(_1){if(isc.isA.Canvas(_1))return _1;if(_1==null)return;if(isc.isA.String(_1)){if(isc.startsWith(_1,this.$st)){return this.$w8(_1)}
if(isc.startsWith(_1,this.$su)){var _2=_1.substring(this.$su.length);var _3="width";if(this.orientation==isc.Layout.VERTICAL)_3="height";var _4={autoDraw:false};_4[_3]=_2;return isc.LayoutSpacer.create(_4)}
return window[_1]}
var _5=_1.autoChildName;if(_5){return this[_5]=this.createAutoChild(_5,_1)}
var _6=_1._constructor;if(_6==null||isc.ClassFactory.getClass(_6)==null){_6=isc.Canvas}
_1._constructor=null;_1.autoDraw=false;return isc.ClassFactory.newInstance(_6,_1)}
,isc.A.createCanvii=function isc_Canvas_createCanvii(_1){if(_1==null)return;for(var i=0;i<_1.length;i++){_1[i]=this.createCanvas(_1[i])}
return _1}
,isc.A.setEventProxy=function isc_Canvas_setEventProxy(_1){var _2=this.eventProxy;if(_2==_1)return;if(_2!=null){_2.$mj.remove(this);if(this.isDrawn()){if(this.getHandle()!=null)this.getHandle().eventProxy=null;if(this.getClipHandle()!=this.getHandle())this.getClipHandle().eventProxy=null}}
this.eventProxy=_1;if(_1!=null){if(!isc.isA.Canvas(_1)){this.logWarn("setEventProxy() passed invalid eventProxy - clearing this property");this.eventProxy=null}else{if(_1.$mj==null)_1.$mj=[];_1.$mj.add(this)}}
if(this.isDrawn())this.redraw("eventProxy updated")}
,isc.A.clearEventProxy=function isc_Canvas_clearEventProxy(){this.setEventProxy()}
,isc.A.addChild=function isc_Canvas_addChild(_1,_2,_3){if(isc.$dd)arguments.$de=this;if(!_1)return null;if(_1==this){this.logWarn("Attempt to add a child to itself");return}
if(!isc.isAn.Instance(_1))_1=this.createCanvas(_1);if(!isc.isA.Canvas(_1)){this.logWarn("addChild(): trying to install a non-canvas as a child.  Returning.");return null}
if(_1.parentElement==this)return _1;var _4=_1.isDrawn();if(_1.parentElement)_1.deparent(_2);isc.Canvas.$v6(_1);if(_1.drawContext)_1.drawContext=null;if(_1.htmlElement)_1.htmlElement=null;_1.parentElement=this;_1.topElement=(this.topElement||this);_1.$w9();if(_2)this[_2]=_1;if(!this.children)this.children=[];if(!this.children.contains(_1))this.children.add(_1);var _5=_1.masterElement;if(_5&&_5.parentElement!=this){_5.peers.remove(_1);if(_5[_2]==_1)_5[_2]=null;_1.masterElement=null}
if(_1.peers){for(var i=0;i<_1.peers.length;i++)this.addChild(_1.peers[i])}
if(_1.isDrawn())_1.clear();if(_4&&!this.warnAboutClear&&!isc.Page.isLoaded()){this.logWarn("Adding already drawn widget:"+_1.getID()+" to new parent:"+this.getID()+". Child has been cleared so it can be drawn inside the new "+"parent. This may be a result of autoDraw being enabled for the child.")}
if(this.isDrawn())_1.$vb();var _7=this.ns.EH;if(_7.clickMaskUp()){var _8=_7.getAllClickMaskIDs();for(var i=_8.length-1;i>=0;i--){var _9=_7.targetIsMasked(this,_8[i]);if(!_9){_7.addUnmaskedTarget(_1,_8[i]);break}else{var _10=_7.targetIsMasked(_1,_8[i]);if(!_10)_7.maskTarget(_1,_8[i])}}}
if(_3==false||_1.$xa){_1.$xa=null;return _1}
var _11=false,_12=(_1.$xb||!_1.tabIndex);if(isc.isA&&isc.isA.Layout&&_12&&(_1.$mb()||(_1.children!=null&&_1.children.length>0)))
{var _13=_1;while(_13.parentElement){if(isc.isA.Layout(_13.parentElement)&&_13.parentElement.isDrawn())
{_13.parentElement.updateMemberTabIndex(_13);if(_13.parentElement==this)_11=true}
_13=_13.parentElement}}
if(this.isDrawn()&&!_1.masterElement){if(this.logIsDebugEnabled(this.$r5)){this.logInfo("child added to already drawn parent: "+(isc.Page.isLoaded()?"page loaded, will draw immediately":"page not loaded, will defer child drawing"),"drawing")}
if(!_11&&_1.$mb()&&_12){var _14;if(this.children.length>1){for(var i=this.children.length-2;i>=0;i--){if(this.children[i].$mb()&&this.children[i].$xb){_14=this.children[i];break}}}
if(_14==null&&this.$mb()&&this.$xb){_14=this}
if(_14!=null)_1.$xc(_14)}
_1.draw();this.adjustOverflow("addChild")}
return _1}
,isc.A.$w9=function isc_Canvas__updateChildrenTopElement(){if(this.dataPath)this.setDataPath(this.dataPath);var _1=this.children;if(!_1||_1.length==0)return;for(var i=0;i<_1.length;i++){var _3=_1[i];_3.topElement=this.topElement;_3.$w9()}}
,isc.A.reparent=function isc_Canvas_reparent(_1){if(this.getID()==_1.getID())return false;if((this.parentElement==_1.parentElement)&&this.getClipHandle()&&_1.getClipHandle()&&(this.getClipHandle().parentNode==_1.getClipHandle().parentNode)){return false}
this._adjacentHandle=_1.getClipHandle();if(_1.parentElement){_1.parentElement.addChild(this)}else{if(this.parentElement)this.deparent();else this.clear();this.draw()}
return true}
,isc.A.removePeer=function isc_Canvas_removePeer(_1,_2){if(_1==null)return;var _3=this.peers,_4;if(!_3||(_4=_3.indexOf(_1))==-1){this.logWarn("Attempt to remove peer: "+_1+" from Canvas that is not its master");return}
_3.removeAt(_4);if(this[_2]==_1)this[_2]=null;_1.masterElement=null;if(_1.depeered)_1.depeered(this,_2);if(this.peerRemoved)this.peerRemoved(_1,_2)}
,isc.A.depeer=function isc_Canvas_depeer(_1){if(!this.masterElement)return;this.masterElement.removePeer(this,_1)}
,isc.A.removeChild=function isc_Canvas_removeChild(_1,_2){if(isc.$dd)arguments.$de=this;if(_1==null)return;var _3=this.children,_4;if(!_3||(_4=_3.indexOf(_1))==-1){this.logWarn("Attempt to remove child: "+_1+" from Canvas that is not its parent");return}
_3.removeAt(_4);if(this[_2]==_1)this[_2]=null;if(_1.isDrawn())_1.clear();delete _1.parentElement;delete _1.topElement;isc.Canvas.$vj(_1);if(_1.peers)_1.peers.map("deparent");if(_1.deparented)_1.deparented(this,_2);if(this.childRemoved)this.childRemoved(_1,_2)}
,isc.A.deparent=function isc_Canvas_deparent(_1){if(!this.parentElement)return;this.parentElement.removeChild(this,_1)}
,isc.A.addPeer=function isc_Canvas_addPeer(_1,_2,_3,_4){if(!_1)return null;if(!isc.isAn.Instance(_1))_1=this.createCanvas(_1);if(_4==true)_1.$va=true;if(_1.masterElement==this)return null;if(_1.masterElement)_1.depeer(_2);_1.masterElement=this;if(_2)this[_2]=_1;if(!this.peers)this.peers=[];if(!this.peers.contains(_1))this.peers.add(_1);if(this.parentElement){this.parentElement.addChild(_1,_2)}else if(_1.parentElement){_1.deparent()}
if(_1.$rw&&(_1.opacity!=this.opacity))
_1.setOpacity(this.opacity);if(_1.$k8&&(_1.visibility!=this.visibility)){_1.setVisibility(this.visibility)}
if(_1.snapTo||_1.snapEdge)_1.$vb();var _5=this.ns.EH;if(_5.clickMaskUp()){var _6=_5.getAllClickMaskIDs();for(var i=_6.length-1;i>=0;i--){var _8=_5.targetIsMasked(this,_6[i]);if(!_8){_5.addUnmaskedTarget(_1,_6[i]);break}else{var _9=_5.targetIsMasked(_1,_6[i]);if(!_9)_5.maskTarget(_1,_6[i])}}}
if(_3==false)return _1;if(this.isDrawn()&&!_1.isDrawn()){_1.draw();if(_1.$va)this.redraw()}
return _1}
,isc.A.setSnapTo=function isc_Canvas_setSnapTo(_1){this.snapTo=_1;this.parentResized()}
,isc.A.getSnapTo=function isc_Canvas_getSnapTo(){return this.snapTo}
,isc.A.setSnapEdge=function isc_Canvas_setSnapEdge(_1){this.snapEdge=_1;this.parentResized()}
,isc.A.getSnapEdge=function isc_Canvas_getSnapEdge(){return this.snapEdge}
,isc.A.getFieldMethod=function isc_Canvas_getFieldMethod(_1,_2,_3){if(_2=="children"){if(_3=="add")return"addChild";if(_3=="remove")return"removeChild"}
return this.Super("getFieldMethod",arguments)}
,isc.A.getParentElements=function isc_Canvas_getParentElements(){var _1=[],_2=this.parentElement;while(_2){_1.add(_2);_2=_2.parentElement}
return _1}
,isc.A.contains=function isc_Canvas_contains(_1,_2){if(!_2&&_1)_1=_1.parentElement;while(_1){if(_1==this)return true;_1=_1.parentElement}
return false}
,isc.A.$xd=function isc_Canvas__isVisibilityAncestorOf(_1){var _2=_1;while(_2){if(_2==this)return true;var _3=(_2.visibility==isc.Canvas.INHERIT);if(!_3)return false;_2=_2.parentElement}
return false}
,isc.A.getChildCount=function isc_Canvas_getChildCount(){if(this.children==null)return;return this.children.map("getChildCount").sum()+this.children.length}
,isc.A.showClickMask=function isc_Canvas_showClickMask(_1,_2,_3){var _4=this.getID();if(!this.ns.EH.clickMaskUp(_4)){return this.ns.EH.showClickMask(_1,_2,_3,_4)}}
,isc.A.hideClickMask=function isc_Canvas_hideClickMask(_1){if(_1==null)_1=this.getID();if(this.ns.EH.clickMaskUp(_1))this.ns.EH.hideClickMask(_1)}
,isc.A.clickMaskUp=function isc_Canvas_clickMaskUp(_1){if(_1==null)_1=this.getID();return this.ns.EH.clickMaskUp(_1)}
,isc.A.unmask=function isc_Canvas_unmask(_1){this.ns.EH.addUnmaskedTarget(this,_1)}
,isc.A.mask=function isc_Canvas_mask(_1){this.ns.EH.maskTarget(this,_1)}
,isc.A.isMasked=function isc_Canvas_isMasked(_1){return this.ns.EH.targetIsMasked(this,_1)}
,isc.A.$um=function isc_Canvas__isHardMasked(){var _1=isc.EH.clickMaskRegistry;if(!_1||_1.length==0)return false;for(var i=_1.length-1;i>=0;i--){var _3=_1[i];if(!this.isMasked(_3))return false;if(isc.EH.isHardMask(_3))return true}
return false}
,isc.A.showComponentMask=function isc_Canvas_showComponentMask(_1){if(!this.componentMask){this.componentMask=this.addAutoChild("componentMask",isc.addProperties({},_1,{disabled:true,autoDraw:false,$rw:false}),isc.Canvas);this.componentMask.setRect(this.getOffsetLeft(),this.getOffsetTop(),this.getVisibleWidth(),this.getVisibleHeight());this.addPeer(this.componentMask)}else if(!this.componentMask.isDrawn())this.componentMask.draw();this.disableKeyboardEvents(true,true)}
,isc.A.hideComponentMask=function isc_Canvas_hideComponentMask(){if(this.componentMask)this.componentMask.clear();this.disableKeyboardEvents(false,true)}
,isc.A.setRect=function isc_Canvas_setRect(_1,_2,_3,_4,_5){if(isc.$dd)arguments.$de=this;if(isc.isAn.Array(_1)){_2=_1[1];_3=_1[2];_4=_1[3];_1=_1[0]}else if(_1!=null&&_1.top!=null){_2=_1.top;_3=_1.width;_4=_1.height;_1=_1.left}
if(this.logIsDebugEnabled()){this.logDebug("setRect: "+this.echo({left:_1,top:_2,width:_3,height:_4}))}
var _6=this.resizeTo(_3,_4,_5,true);if(_6)this.$xe=true;this.moveTo(_1,_2,_5,true);this.$xe=null;return _6}
,isc.A.getRect=function isc_Canvas_getRect(){return[this.getLeft(),this.getTop(),this.getVisibleWidth(),this.getVisibleHeight()]}
,isc.A.getLeft=function isc_Canvas_getLeft(){var _1=this.getStyleHandle();if(_1==null)return this.left;var _2=(isc.Browser.isIE?_1.pixelLeft:parseInt(_1.left));if(this.vscrollOn&&this.showCustomScrollbars&&this.isRTL()){return _2-this.getScrollbarSize()}
return _2}
,isc.A.getOffsetLeft=function isc_Canvas_getOffsetLeft(){var _1=this.getClipHandle();if(isc.Browser.isMoz&&this.$xf())_1=null;if(_1==null){if(this.logIsInfoEnabled()){this.logInfo("getOffsetLeft() called before widget is drawn - unable to calculate offset "+"coordinates.  Returning specified coordinates")}
return this.left}
var _2=isc.Element.getOffsetLeft(_1);if(this.vscrollOn&&this.showCustomScrollbars&&this.isRTL()){_2-=this.getScrollbarSize()}
return _2}
,isc.A.setLeft=function isc_Canvas_setLeft(_1){this.moveTo(_1,null)}
,isc.A.getTop=function isc_Canvas_getTop(){var _1=this.getStyleHandle();if(_1==null)return this.top;var _2=(isc.Browser.isIE?_1.pixelTop:parseInt(_1.top));return _2}
,isc.A.getOffsetTop=function isc_Canvas_getOffsetTop(){var _1=this.getClipHandle();if(isc.Browser.isMoz&&this.$xf())_1=null;if(_1==null)return this.top;var _2=isc.Element.getOffsetTop(_1);return _2}
,isc.A.setTop=function isc_Canvas_setTop(_1){this.moveTo(null,_1)}
,isc.A.getWidth=function isc_Canvas_getWidth(){return this.width}
,isc.A.setWidth=function isc_Canvas_setWidth(_1){this.resizeTo(_1)}
,isc.A.getHeight=function isc_Canvas_getHeight(){return this.$s9}
,isc.A.setHeight=function isc_Canvas_setHeight(_1){this.resizeTo(null,_1)}
,isc.A.getMinWidth=function isc_Canvas_getMinWidth(){return this.minWidth}
,isc.A.getMinHeight=function isc_Canvas_getMinHeight(){return this.minHeight}
,isc.A.getMaxWidth=function isc_Canvas_getMaxWidth(){return this.maxWidth}
,isc.A.getMaxHeight=function isc_Canvas_getMaxHeight(){return this.maxHeight}
,isc.A.getRight=function isc_Canvas_getRight(){return this.getLeft()+this.getVisibleWidth()}
,isc.A.setRight=function isc_Canvas_setRight(_1){if(isc.isA.Number(_1)){this.resizeTo(_1-this.getLeft(),null)}else{this.logWarn("setRight() expects an integer value")}}
,isc.A.getBottom=function isc_Canvas_getBottom(){return this.getTop()+this.getVisibleHeight()}
,isc.A.setBottom=function isc_Canvas_setBottom(_1){if(isc.isA.Number(_1)){this.resizeTo(null,_1-this.getTop())}else{this.logWarn("setBottom() expects an integer value")}}
,isc.A.enforceScrollSize=function isc_Canvas_enforceScrollSize(_1,_2){if(this.logIsDebugEnabled(this.$sx)){this.logDebug("enforcing scroll size:"+[_1,_2],"enforceScrollSize")}
if(!this.$t2&&!this.$t1)return;if(_1==null)_1=0;if(_2==null)_2=0;if(isNaN(_1)||isNaN(_2)||_1<0||_2<0){this.logWarn("Invalid width or height in Canvas.enforceScrollSize()"+" on component: "+this.getID()+" with sizes: "+[_1,_2]+this.getStackTrace());return}
if(this.$vo==null){var _3=this.$sv;var _4=this.$t4(this.$sw);_3[1]=_4;_3[3]=_1-1;_3[5]=_2-1;var _5=_3.join(isc.emptyString);this.$vo=isc.Element.insertAdjacentHTML(this.getHandle(),this.$pl,_5,true);if(this.$vo==null){this.$vo=document.getElementById(_4)}}else if(!this.$ul||this.$ul[0]!=_1||this.$ul[1]!=_2)
{this.$vo.style.left=(_1-1)+isc.px;this.$vo.style.top=(_2-1)+isc.px}
this.$ul=[_1,_2]}
,isc.A.stopEnforcingScrollSize=function isc_Canvas_stopEnforcingScrollSize(){if(this.logIsDebugEnabled(this.$sx)){this.logDebug("stop enforcing scroll size","enforceScrollSize")}
delete this.$ul;if(!this.isDrawn())return;if(this.$vo){this.$vo.style.left=this.$sy;this.$vo.style.top=this.$sy}}
,isc.A.getScrollWidth=function isc_Canvas_getScrollWidth(_1){if(isc.$dd)arguments.$de=this;if(this.$u9){this.$u9=null;this.adjustOverflow("widthCheckWhileDeferred")}
if(!_1&&this.$xg!=null)return this.$xg;var _2=0,_3=this.getClipHandle();if(_3==null){this.logDebug("No size info available from DOM, returning user-specified size");return this.getInnerWidth()}
if(this.allowNativeContentPositioning){this.$p2=true;if(isc.Browser.isSafari||(isc.Browser.isMoz&&((_3.scrollWidth||_3.offsetWidth)<=parseInt(_3.style.width))))
{_2=isc.Element.getScrollWidth(this.getHandle())}else{_2=isc.Element.getScrollWidth(_3)}
delete this.$p2}else{var _4=this.children,_5=_4&&_4.length>0,_6=0;if(!_5||this.allowContentAndChildren){if(isc.Browser.isSafari&&this.overflow==isc.Canvas.VISIBLE){_2=this.getHandle().scrollWidth;if(this.useClipDiv&&this.padding==null){_2+=isc.Element.$rb(this.styleName)}}else{_6=(_3.scrollWidth||_3.offsetWidth);if(_6!=null&&_6!=this.$r3){_2=_6;if(isc.Browser.isOpera){_2-=(this.getLeftBorderSize()+this.getLeftPadding())}
if(isc.Browser.isMoz)_2-=this.$xh();if(isc.Browser.isMoz&&this.getScrollingMechanism()==isc.Canvas.NESTED_DIV)
{var _7=this.getHandle().offsetLeft;if(_7<0)_7=-_7;_2-=_7}}
if(isc.Browser.isSafari||(isc.Browser.isMoz&&_2<=parseInt(_3.style.width)))
{var _8=this.getHandle(),_9=_8.scrollWidth||_8.offsetWidth;if(_9>_2)_2=_9}}}
if(_5){var _10=this.$xi(this.children);_2=Math.max(_10,_2);if(this.$ul!=null){var _11=this.$ul[0];_2=Math.max(_2,_11)}}}
this.$xg=_2;return _2}
,isc.A.$xi=function isc_Canvas__getWidthSpan(_1,_2){var _3=0,_4=0,_5=this.overflow==isc.Canvas.VISIBLE||this.overflow==isc.Canvas.CLIP_H,_6;for(var i=0;i<_1.length;i++){var _8=_1[i];if(!_8.isDrawn()&&!_8.$xj)continue;if(_2&&_8.visibility==isc.Canvas.HIDDEN)continue;var _9=(_8.position!=isc.Canvas.RELATIVE),_10=_8.getVisibleWidth(),_11=(_9?_8.getLeft():_8.getOffsetLeft());if(!_5&&_9)_10-=_8.getRightMargin();if(_11+_10>_4){_4=_11+_10;_6=_8}
if(_11<_3)_3=this.isRTL()?_11:Math.max(0,_11)}
return _4-_3}
,isc.A.getScrollHeight=function isc_Canvas_getScrollHeight(_1){if(isc.$dd)arguments.$de=this;if(this.$u9){this.$u9=null;this.adjustOverflow("heightCheckWhileDeferred")}
if(!_1&&this.$xk!=null)return this.$xk;var _2=0,_3=this.getClipHandle();if(_3==null){this.logDebug("No size info available from DOM, returning user-specified size");return this.getInnerHeight()}
if(this.allowNativeContentPositioning){this.$p0=true;if(isc.Browser.isSafari||(isc.Browser.isMoz&&((_3.scrollHeight||_3.offsetHeight)<=parseInt(_3.style.height))))
{_2=isc.Element.getScrollHeight(this.getHandle())}else{_2=isc.Element.getScrollHeight(_3)}
delete this.$p0}else{var _4=(this.children&&this.children.length>0);if(!_4||this.allowContentAndChildren){if(isc.Browser.isSafari&&this.overflow==isc.Canvas.VISIBLE){_2=this.getHandle().scrollHeight;if(this.useClipDiv&&this.padding==null){_2+=isc.Element.$ra(this.styleName)}}else{var _5=(_3.scrollHeight||_3.offsetHeight);if(_5!=null&&_5!=this.$r3){_2=_5;if(isc.Browser.isMoz)_2-=this.$xl();if(this.useClipDiv&&(isc.Browser.isSafari||(isc.Browser.isMoz&&_2<=parseInt(_3.style.height))))
{var _6=this.getHandle(),_7=_6.scrollHeight||_6.offsetHeight;if(_7>_2)_2=_7}}}}
if(_4){var _8=this.$xm(this.children);if(_8>_2){_2=_8}
if(this.$ul!=null){var _9=this.$ul[1];_2=Math.max(_2,_9)}}}
this.$xk=_2;return _2}
,isc.A.$xl=function isc_Canvas__offscreenChildrenHeight(){if(!isc.isAn.Array(this.children))return 0;var _1=0;for(var i=0;i<this.children.length;i++){var _3=this.children[i],_4=(_3.position==isc.Canvas.ABSOLUTE?_3.getTop():_3.getOffsetTop());if(_4<_1)_1=_4}
return-_1}
,isc.A.$xh=function isc_Canvas__offscreenChildrenWidth(){if(!isc.isAn.Array(this.children))return 0;if(!this.useClipDiv)return 0;var _1=0;for(var i=0;i<this.children.length;i++){var _3=this.children[i],_4=(_3.position==isc.Canvas.ABSOLUTE?_3.getLeft():_3.getOffsetLeft());if(_4<_1)_1=_4}
return-_1}
,isc.A.$xm=function isc_Canvas__getHeightSpan(_1,_2){var _3=0,_4=0,_5=this.overflow==isc.Canvas.VISIBLE||this.overflow==isc.Canvas.CLIP_H;for(var i=0;i<_1.length;i++){var _7=_1[i];if(!_7.isDrawn()&&!_7.$xj)continue;if(_2&&_7.visibility==isc.Canvas.HIDDEN)continue;var _8=_7.position!=isc.Canvas.RELATIVE,_9=_7.getVisibleHeight(),_10=(_8?_7.getTop():_7.getOffsetTop());if(!_5&&_8)_9-=_7.getBottomMargin();if(_9+_10>_4)_4=_9+_10;if(_10<_3)_3=Math.max(0,_10)}
return _4-_3}
);isc.evalBoundary;isc.B.push(isc.A.getScrollLeft=function isc_Canvas_getScrollLeft(){if(!this.isDrawn()||this.getScrollingMechanism()!=isc.Canvas.NATIVE){return this.scrollLeft}
return this.getScrollHandle().scrollLeft}
,isc.A.getScrollTop=function isc_Canvas_getScrollTop(){if(!this.isDrawn()||this.getScrollingMechanism()!=isc.Canvas.NATIVE){return this.scrollTop}
return this.getScrollHandle().scrollTop}
,isc.A.setPageLeft=function isc_Canvas_setPageLeft(_1){this.moveBy(_1-this.getPageLeft(),0)}
,isc.A.setPageTop=function isc_Canvas_setPageTop(_1){this.moveBy(0,_1-this.getPageTop())}
,isc.A.getParentPageRect=function isc_Canvas_getParentPageRect(){if(this.parentElement){var _1=this.parentElement,_2=_1.getPageRect();var _3=_1.getLeftMargin(),_4=_1.getTopMargin();_2[0]+=_3;_2[1]+=_4;_2[2]-=(_3+_1.getRightMargin());_2[3]-=(_4+_1.getBottomMargin());if(this.peers&&this.peers.length>0){var _5=this.getPeerRect(),_6=this.getPageRect();_2[0]+=(_6[0]-_5[0]);_2[1]+=(_6[1]-_5[1]);_2[2]-=(_5[2]-_6[2]);_2[3]-=(_5[3]-_6[3])}
var _7=_1.$qz();_2[0]+=_7.left;_2[1]+=_7.top;_2[2]-=_7.right+_7.left;_2[3]-=_7.bottom+_7.top;var _8=_1.getScrollbarSize();if(_1.vscrollOn)_2[2]-=_8;if(_1.hscrollOn)_2[3]-=_8;return _2}
else return[0,0,isc.Page.getWidth(),isc.Page.getHeight()]}
,isc.A.setPageRect=function isc_Canvas_setPageRect(_1,_2,_3,_4,_5){if(isc.isAn.Array(_1)){_2=_1[1];_3=_1[2];_4=_1[3];_1=_1[0]}
if(this.keepInParentRect&&this.ns.EH.dragging&&this==this.ns.EH.dragMoveTarget){var _6=(_3==null&&_4==null);if(_3==null)_3=this.getVisibleWidth();if(_4==null)_4=this.getVisibleHeight();var _7=_1+_3,_8=_2+_4,_9;var _10=isc.isAn.Array(this.keepInParentRect);if(_10){_9=this.keepInParentRect}else{_9=this.getParentPageRect()}
var _11=_9[0],_12=_9[1],_13=_9[2],_14=_9[3],_15=_11+_13,_16=_12+_14;var _17=this.ns.EH,_18=_17.getDragTarget(_17.getLastEvent()).parentElement;if(_18){var _19=_18.getScrollLeft(),_20=_18.getScrollWidth()-
_18.getViewportWidth()-_19,_21=_18.getScrollTop(),_22=_18.getScrollHeight()-
_18.getViewportHeight()-_21}else{var _19=isc.Page.getScrollLeft(),_20=isc.Page.getScrollWidth()-
isc.Page.getWidth()-_19,_21=isc.Page.getScrollTop(),_22=isc.Page.getScrollHeight()-
isc.Page.getHeight()-_21}
if(_20<0)_20=0;if(_22<0)_22=0;if(_6){if(_1<_11-_19){_1=_11-_19}
else if(_7>_15+_20){_1=_15+_20-_3}
if(_2<_12-_21){_2=_12-_21}
else if(_8>_16+_22){_2=_16+_22-_4}}else{if(_1<_11){_3=_3-(_11-_1);_1=_11}else if(_7>_15){_3=_3-(_7-_15)}
if(_2<_12){_4=_4-(_12-_2);_2=_12}else if(_8>_16){_4=_4-(_8-_16)}}}
this.moveBy(_1-this.getPageLeft(),_2-this.getPageTop());if(_5){var _23=this.getVisibleWidth(),_24=this.getVisibleHeight(),_25=_23-_3,_26=_24-_4;this.resizeTo(_3,_4);this.redrawIfDirty("setPageRect");var _27=(_23-this.getVisibleWidth()),_28=(_24-this.getVisibleHeight());if(_1>this.getPageLeft())_1-=(_25-_27);if(_2>this.getPageTop())_2-=(_26-_28)}else{this.resizeTo(_3,_4)}}
,isc.A.getCanvasLeft=function isc_Canvas_getCanvasLeft(_1){if(_1!=null){if(!_1.contains(this,false)){this.logWarn("getCanvasTop passed ancestor:"+_1+". This is not an ancestor of this component - ignoring");_1=this.parentElement}}else{_1=this.parentElement}
if(!this.isDrawn()||(isc.Browser.isMoz&&this.$xf()))
{if(!this.isDrawn()&&this.position==isc.Canvas.RELATIVE){this.logWarn("getCanvasLeft(): Called on undrawn relatively-position widget '"+this.getID()+"'.  The drawn coordinates can not be reliably "+"calculated until the widget has drawn - returning estimated position")}
var _2=this.left,_3=this.parentElement;while(_1!=_3){_2+=_3.left;_3=_3.parentElement}
return _2}
var _4=this.getLeftOffset(_1);return _4}
,isc.A.getPageLeft=function isc_Canvas_getPageLeft(){if(isc.$dd)arguments.$de=this;var _1=this.getClipHandle();if(_1&&isc.Browser.isMoz&&this.$xf())_1=null;if(_1==null){if(!this.isDrawn()&&this.position==isc.Canvas.RELATIVE){this.logWarn("getPageLeft(): Called on undrawn relatively-position widget '"+this.getID()+"'.  The page level coordinates can not be reliably "+"calculated until the widget has drawn - returning estimated position")}
var _2=this.parentElement;if(_2){var _3=0;if(_2.hscrollOn){if(!this.isRTL())_3=_2.getScrollLeft();else{var _4=_2.getScrollWidth()-_2.getViewportWidth();_3=-1*(_4-_2.getScrollLeft())}}
return this.getOffsetLeft()+_2.getLeftBorderSize()+_2.getLeftMargin()+_2.getPageLeft()-_3}else{return this.getOffsetLeft()}}
if(this.useClientRectAPI&&_1.getBoundingClientRect!=null){var _5=_1.getBoundingClientRect().left;_5-=this.getLeftMargin();_5+=isc.Page.getScrollLeft();return _5}
return this.getLeftOffset()}
,isc.A.getLeftOffset=function isc_Canvas_getLeftOffset(_1){var _2=this.ns.Element.getOffset(isc.Canvas.LEFT,this,_1,this.isRTL(),true);return _2}
,isc.A.getCanvasTop=function isc_Canvas_getCanvasTop(_1){if(_1!=null){if(!_1.contains(this,false)){this.logWarn("getCanvasTop passed ancestor:"+_1+". This is not an ancestor of this component - ignoring");_1=this.parentElement}}else{_1=this.parentElement}
if(!this.isDrawn()||(isc.Browser.isMoz&&this.$xf()))
{if(!this.isDrawn()&&this.position==isc.Canvas.RELATIVE){this.logWarn("getCanvasTop(): Called on undrawn relatively-position widget '"+this.getID()+"'.  The drawn coordinates can not be reliably "+"calculated until the widget has drawn - returning estimated position")}
var _2=this.top,_3=this.parentElement;while(_1!=_3){_2+=_3.top;_3=_3.parentElement}
return _2}
var _4=this.getTopOffset(_1);return _4}
,isc.A.getPageTop=function isc_Canvas_getPageTop(){var _1=this.getClipHandle();if(_1&&isc.Browser.isMoz&&this.$xf())_1=null;if(_1==null){if(!this.isDrawn()&&this.position==isc.Canvas.RELATIVE){this.logWarn("getPageTop(): Called on undrawn relatively-positioned widget '"+this.getID()+"'.  The page level coordinates can not be reliably "+"calculated until the widget has drawn - returning estimated position")}
var _2=this.parentElement;if(_2){return this.getOffsetTop()+_2.getTopBorderSize()+_2.getTopMargin()+_2.getPageTop()-_2.getScrollTop()}else{return this.getOffsetTop()}}
if(this.useClientRectAPI&&_1.getBoundingClientRect!=null){var _3=_1.getBoundingClientRect().top;_3-=this.getTopMargin();_3+=isc.Page.getScrollTop();return _3}
return this.getTopOffset()}
,isc.A.getTopOffset=function isc_Canvas_getTopOffset(_1){var _2=this.ns.Element.getOffset(isc.Canvas.TOP,this,_1,null,true);return _2}
,isc.A.getPageRight=function isc_Canvas_getPageRight(){return this.getPageLeft()+this.getVisibleWidth()}
,isc.A.getPageBottom=function isc_Canvas_getPageBottom(){return this.getPageTop()+this.getVisibleHeight()}
,isc.A.getPageRect=function isc_Canvas_getPageRect(){return[this.getPageLeft(),this.getPageTop(),this.getVisibleWidth(),this.getVisibleHeight()]}
,isc.A.usingCSSScrollbars=function isc_Canvas_usingCSSScrollbars(){return!this.showCustomScrollbars&&(this.overflow==isc.Canvas.AUTO||this.overflow==isc.Canvas.SCROLL)}
,isc.A.getScrollingMechanism=function isc_Canvas_getScrollingMechanism(){if(!this.$xn){if(!this.showCustomScrollbars&&(this.overflow==isc.Canvas.AUTO||this.overflow==isc.Canvas.SCROLL))
{this.$xn=isc.Canvas.NATIVE}else{if((isc.Browser.isSafari&&isc.Browser.SafariVersion<125)||(isc.Browser.isMoz&&isc.Browser.isUnix&&isc.Browser.geckoVersion<=20031007))
{this.$xn=isc.Canvas.NESTED_DIV}else{this.$xn=isc.Canvas.NATIVE}}}
return this.$xn}
,isc.A.setMargin=function isc_Canvas_setMargin(_1){this.$xo=null;this.$xp=null;if(_1==null){delete this.margin}else{var _2=_1;if(isc.isA.String(_1))_1=parseInt(_1);if(!isc.isA.Number(_1)){this.logWarn("setMargin() passed invalid margin:"+_2+", ignoring.");return}
this.margin=_1}
var _3=this.getStyleHandle();if(!_3)return;this.$xq();this.adjustOverflow("setMargin");this.innerSizeChanged("Margin thickness changed")}
,isc.A.$xq=function isc_Canvas__applyFullMargins(){var _1=this.getClipHandle();if(!_1)return;if(!this.$wy()&&this.$wz==null){_1.style.marginTop="";_1.style.marginBottom="";_1.style.marginLeft="";_1.style.marginRight="";if(this.margin==null)_1.style.margin=0;else _1.style.margin=this.margin+isc.px;return}
var _2=this.$qx();_1.style.marginTop=_2.top+isc.px;_1.style.marginLeft=_2.left+isc.px;_1.style.marginBottom=_2.bottom+isc.px;_1.style.marginRight=_2.right+isc.px}
,isc.A.getMargin=function isc_Canvas_getMargin(){return this.margin}
,isc.A.getTopMargin=function isc_Canvas_getTopMargin(){return this.$qx().top}
,isc.A.getLeftMargin=function isc_Canvas_getLeftMargin(){return this.$qx().left}
,isc.A.getBottomMargin=function isc_Canvas_getBottomMargin(){return this.$qx().bottom}
,isc.A.getRightMargin=function isc_Canvas_getRightMargin(){return this.$qx().right}
,isc.A.$xr=function isc_Canvas__removeDestroyedPeers(_1,_2){var _3=[];for(var i=0;i<_1.length;i++){if(_1[i].destroyed){_3[_3.length]={peer:_1[i],side:_2};_1[i]=null}}
_1.removeEmpty();return _3}
,isc.A.$qx=function isc_Canvas__calculateMargins(){var _1=this.$wz,_2=(_1!=null),_3,_4,_5,_6;if(_2){_3=_1.top;_6=_1.bottom;_4=_1.left;_5=_1.right;var _7=[];if(_3!=null)_7.addList(this.$xr(_3,"top"));if(_6!=null)_7.addList(this.$xr(_6,"bottom"));if(_4!=null)_7.addList(this.$xr(_4,"left"));if(_5!=null)_5.addList(this.$xr(_5,"right"));if(_7.length>0){for(var i=0;i<_7.length;i++){this.$xs(_7[i].peer,_7[i].side)}}
if((_3==null||_3.length==0)&&(_6==null||_6.length==0)&&(_4==null||_4.length==0)&&(_5==null||_5.length==0))_2=false}
if(!this.$wy()&&!_2)return this.$xt();var _9=this.$xp;if(_9)return _9;var _10=this.$xu();_9={left:_10.left,right:_10.right,top:_10.top,bottom:_10.bottom};if(_2){if(_3){for(var i=0;i<_3.length;i++){var _11=_3[i];_9.top+=_11.getVisibleHeight();if(_11.$xv!=null){_9.top-=_11.$xv}}}
if(_6){for(var i=0;i<_6.length;i++){var _12=_6[i];_9.bottom+=_12.getVisibleHeight();if(_12.$xv!=null){_9.bottom-=_12.$xv}}}
if(_4){for(var i=0;i<_4.length;i++){var _13=_4[i];_9.left+=_13.getVisibleWidth();if(_13.$xv!=null){_9.left-=_13.$xv}}}
if(_5){for(var i=0;i<_5.length;i++){var _14=_5[i];_9.right+=_14.getVisibleWidth();if(_14.$xv!=null){_9.right-=_14.$xv}}}}
if(this.$wy()){var _15=this.$tw();_9.left+=_15.$xw,_9.right+=_15.$xx,_9.top+=_15.$xy,_9.bottom+=_15.$xz}
return(this.$xp=_9)}
,isc.A.$xu=function isc_Canvas__getSpecifiedMargins(){var _1=this.$t1;this.$t1=false;var _2=this.$xt();this.$t1=_1;return _2}
,isc.A.$xt=function isc_Canvas__calculateNormalMargins(){if(this.$xo!=null)return this.$xo;var _1={},_2=isc.px;if(!this.isDrawn()){var _3=this.margin;if(isc.isA.String(_3)){if(isc.endsWith(_3,_2)||parseInt(_3)+isc.emptyString==_3)
_3=parseInt(_3)}
if(isc.isA.Number(_3)){_1.top=_3;_1.bottom=_3;_1.left=_3;_1.right=_3;this.$xo=_1;return _1}}else{var _4=this.getStyleHandle(),_5=_4.marginLeft,_6=_4.marginRight,_7=_4.marginTop,_8=_4.marginBottom;if(isc.isA.String(_5)&&isc.endsWith(_5,_2))
_5=parseInt(_5);if(isc.isA.String(_6)&&isc.endsWith(_6,_2))
_6=parseInt(_6)
if(isc.isA.String(_7)&&isc.endsWith(_7,_2))
_7=parseInt(_7);if(isc.isA.String(_8)&&isc.endsWith(_8,_2))
_8=parseInt(_8)
if(isc.isA.Number(_5))_1.left=_5;if(isc.isA.Number(_6))_1.right=_6;if(isc.isA.Number(_7))_1.top=_7;if(isc.isA.Number(_8))_1.bottom=_8}
if(this.className){if(!isc.isA.Number(_1.left))
_1.left=isc.Element.$p5(this.className);if(!isc.isA.Number(_1.right))
_1.right=isc.Element.$p6(this.className);if(!isc.isA.Number(_1.top))
_1.top=isc.Element.$p3(this.className);if(!isc.isA.Number(_1.bottom))
_1.bottom=isc.Element.$p4(this.className)}else{if(!isc.isA.Number(_1.left))
_1.left=0;if(!isc.isA.Number(_1.right))
_1.right=0;if(!isc.isA.Number(_1.top))
_1.top=0;if(!isc.isA.Number(_1.bottom))
_1.bottom=0}
return(this.$xo=_1)}
,isc.A.getTopBorderSize=function isc_Canvas_getTopBorderSize(){return this.$qz().top}
,isc.A.getBottomBorderSize=function isc_Canvas_getBottomBorderSize(){return this.$qz().bottom}
,isc.A.getLeftBorderSize=function isc_Canvas_getLeftBorderSize(){return this.$qz().left}
,isc.A.getRightBorderSize=function isc_Canvas_getRightBorderSize(){return this.$qz().right}
,isc.A.getHBorderSize=function isc_Canvas_getHBorderSize(){return(this.getLeftBorderSize()+this.getRightBorderSize())}
,isc.A.getVBorderSize=function isc_Canvas_getVBorderSize(){return this.getTopBorderSize()+this.getBottomBorderSize()}
,isc.A.$qz=function isc_Canvas__calculateBorderSize(){if(this.$x0!=null)return this.$x0;var _1={},_2=isc.px;if(!this.isDrawn()){var _3=this.border;if(_3!=null&&isc.contains(_3,_2)){var _4=_3.match(/\s*\d+px/g);if(isc.isAn.Array(_4))_4=parseInt(_4[0]);else _4=parseInt(_4);if(isc.isA.Number(_4)){this.$x0={left:_4,right:_4,top:_4,bottom:_4}
return this.$x0}}}else{var _5=this.getStyleHandle(),_6=_5.borderLeftWidth,_7=_5.borderRightWidth,_8=_5.borderTopWidth,_9=_5.borderBottomWidth;if(isc.isA.String(_6)&&isc.endsWith(_6,_2))
_6=parseInt(_6);if(isc.isA.String(_7)&&isc.endsWith(_7,_2))
_7=parseInt(_7)
if(isc.isA.String(_8)&&isc.endsWith(_8,_2))
_8=parseInt(_8);if(isc.isA.String(_9)&&isc.endsWith(_9,_2))
_9=parseInt(_9)
if(isc.isA.Number(_6))_1.left=_6;if(isc.isA.Number(_7))_1.right=_7;if(isc.isA.Number(_8))_1.top=_8;if(isc.isA.Number(_9))_1.bottom=_9}
if(this.className){if(!isc.isA.Number(_1.left))
_1.left=isc.Element.$q1(this.className);if(!isc.isA.Number(_1.right))
_1.right=isc.Element.$q2(this.className);if(!isc.isA.Number(_1.top))
_1.top=isc.Element.$qy(this.className);if(!isc.isA.Number(_1.bottom))
_1.bottom=isc.Element.$q0(this.className)}else{if(!isc.isA.Number(_1.left))
_1.left=0;if(!isc.isA.Number(_1.right))
_1.right=0;if(!isc.isA.Number(_1.top))
_1.top=0;if(!isc.isA.Number(_1.bottom))
_1.bottom=0}
return(this.$x0=_1)}
,isc.A.setTopPadding=function isc_Canvas_setTopPadding(_1){this.$x1=null;this.topPadding=_1;if(isc.isA.Number(_1))_1+="px";if(this.isDrawn())this.getHandle().paddingTop=_1}
,isc.A.setLeftPadding=function isc_Canvas_setLeftPadding(_1){this.$x1=null;this.leftPadding=_1;if(isc.isA.Number(_1))_1+="px";if(this.isDrawn())this.getHandle().paddingLeft=_1}
,isc.A.setRightPadding=function isc_Canvas_setRightPadding(_1){this.$x1=null;this.rightPadding=_1;if(isc.isA.Number(_1))_1+="px";if(this.isDrawn())this.getHandle().paddingRight=_1}
,isc.A.setBottomPadding=function isc_Canvas_setBottomPadding(_1){this.$x1=null;this.bottomPadding=_1;if(isc.isA.Number(_1))_1+="px";if(this.isDrawn())this.getHandle().paddingBottom=_1}
,isc.A.setPadding=function isc_Canvas_setPadding(_1){this.$x1=null;if(_1!=null){var _2=_1;if(isc.isA.String(_1))_1=parseInt(_1);if(!isc.isA.Number(_1)){this.logWarn("setPadding passed unrecognized value:"+_2+" - ignoring");return}}
this.padding=_1;var _3=isc.Browser.isDOM?this.getHandle():null;if(!_3){return}
if(_1==null){_3.style.padding=null;if(this.useClipDiv)this.getClipHandle().style.padding=null}else{_3.style.padding=this.padding+isc.px;if(this.useClipDiv)this.getClipHandle().style.padding=this.$sz}}
,isc.A.getPadding=function isc_Canvas_getPadding(){return this.padding}
,isc.A.getTopPadding=function isc_Canvas_getTopPadding(){return this.$q6().top}
,isc.A.getBottomPadding=function isc_Canvas_getBottomPadding(){return this.$q6().bottom}
,isc.A.getLeftPadding=function isc_Canvas_getLeftPadding(){return this.$q6().left}
,isc.A.getRightPadding=function isc_Canvas_getRightPadding(){return this.$q6().right}
,isc.A.getVPadding=function isc_Canvas_getVPadding(){return this.getTopPadding()+this.getBottomPadding()}
,isc.A.getHPadding=function isc_Canvas_getHPadding(){return this.getLeftPadding()+this.getRightPadding()}
,isc.A.$q6=function isc_Canvas__calculatePadding(){if(this.$x1!=null)return this.$x1;var _1={},_2=isc.px;if(this.isDrawn()&&this.getHandle()!=null){var _3=this.getHandle().style;if(_3.paddingTop!=null&&!isc.isAn.emptyString(_3.paddingTop)&&isc.endsWith(_3.paddingTop,_2)){_1.top=parseInt(_3.paddingTop)}
if(_3.paddingBottom!=null&&!isc.isAn.emptyString(_3.paddingBottom)&&isc.endsWith(_3.paddingBottom,_2)){_1.bottom=parseInt(_3.paddingBottom)}
if(_3.paddingLeft!=null&&!isc.isAn.emptyString(_3.paddingLeft)&&isc.endsWith(_3.paddingLeft,_2)){_1.left=parseInt(_3.paddingLeft)}
if(_3.paddingRight!=null&&!isc.isAn.emptyString(_3.paddingRight)&&isc.endsWith(_3.paddingRight,_2)){_1.right=parseInt(_3.paddingRight)}}else{if(this.topPadding!=null)_1.top=this.topPadding;if(this.leftPadding!=null)_1.left=this.leftPadding;if(this.rightPadding!=null)_1.right=this.rightPadding;if(this.bottomPadding!=null)_1.bottom=this.bottomPadding;if(this.padding!=null){var _4=parseInt(this.padding);if(_1.left==null)_1.left=_4;if(_1.top==null)_1.top=_4;if(_1.bottom==null)_1.bottom=_4;if(_1.right==null)_1.right=_4}}
if(this.className){if(!isc.isA.Number(_1.left))_1.left=isc.Element.$q8(this.className);if(!isc.isA.Number(_1.right))_1.right=isc.Element.$q9(this.className);if(!isc.isA.Number(_1.top))_1.top=isc.Element.$q5(this.className);if(!isc.isA.Number(_1.bottom))_1.bottom=isc.Element.$q7(this.className)}else{if(!isc.isA.Number(_1.left))_1.left=0;if(!isc.isA.Number(_1.right))_1.right=0;if(!isc.isA.Number(_1.top))_1.top=0;if(!isc.isA.Number(_1.bottom))_1.bottom=0}
return(this.$x1=_1)}
,isc.A.containsPoint=function isc_Canvas_containsPoint(_1,_2,_3){if(isc.$dd)arguments.$de=this;if(!this.isVisible()||!this.isDrawn())return false;if(_3==null)_3=false;var _4=this.getPageLeft()+this.getLeftMargin();if(_1<_4)return false;var _5=this.getPageTop()+this.getTopMargin();if(_2<_5)return false;var _6=_3?this.getViewportWidth():(this.getVisibleWidth()-this.getHMarginSize());if(_1>_4+_6)return false;var _7=_3?this.getViewportHeight():(this.getVisibleHeight()-this.getVMarginSize());if(_2>_5+_7)return false;var _8=0,_9=0;var _10=this.$x2=this.$x2||[];_10.length=1;_10[0]=this;var i=1,_12=this;while(_12.parentElement!=null){_12=_12.parentElement
_10[i]=_12;i++}
var _13,_14;for(var j=_10.length-1;j>=0;j--){var _16=_10[j];_8+=_16.getCanvasLeft();_9+=_16.getCanvasTop();if(j+1<_10.length){var _17=_10[j+1];_8-=_17.getScrollLeft();_9-=_17.getScrollTop()}
_8+=_16.getLeftMargin();_9+=_16.getTopMargin();if(_16==this&&!_3){_13=_16.getVisibleWidth()-_16.getHMarginSize();_14=_16.getVisibleHeight()-_16.getVMarginSize()}else{_8+=_16.getLeftBorderSize();_9+=_16.getTopBorderSize();_13=_16.getViewportWidth();_14=_16.getViewportHeight()}
if(!((_1>=_8)&&(_1<=_8+_13)&&(_2>=_9)&&(_2<=_9+_14)))
{return false}}
return true}
,isc.A.visibleAtPoint=function isc_Canvas_visibleAtPoint(_1,_2,_3,_4,_5){if(isc.$dd)arguments.$de=this;if(!this.containsPoint(_1,_2,_3)){return false}
if(!isc.isAn.Array(_4))_4=[_4];var _6=this;while(_6!=null&&_6!=_5){var _7=(_6.parentElement!=null?_6.parentElement.children:isc.Canvas.$x3);for(var i=0;i<_7.length;i++){var _9=_7[i];if(_9==null||_9==_6||!_9.isDrawn()||!_9.isVisible()||_4.contains(_9)||_9.isMouseTransparent||(_9.getZIndex()<_6.getZIndex()))
{continue}
if(_9.$k9)continue;if(isc.isA.Scrollbar(_9)||isc.isA.ScrollThumb(_9))continue;if(isc.EdgedCanvas&&isc.isA.EdgedCanvas(_9)&&_9.masterElement&&_9.masterElement.$nw==_9)continue;if(isc.Layout&&isc.isA.Layout(_9.parentElement)&&_9.parentElement.hasMember(_9)&&_9.parentElement.hasMember(_6))
{continue}
if(isc.TabSet&&isc.isA.TabBar(_9)&&isc.isA.TabSet(_9.parentElement)&&_9.parentElement.paneContainer&&_9.parentElement.paneContainer.contains(this))
{continue}
if(_9.containsPoint(_1,_2,false)){return false}}
_6=_6.parentElement}
return true}
,isc.A.scrollIntoView=function isc_Canvas_scrollIntoView(_1,_2,_3,_4,_5,_6,_7,_8,_9){if(_3==null)_3=0;if(_4==null)_4=0;var _10=true;var _11,_12;if(this.overflow!=isc.Canvas.VISIBLE&&this.overflow!=isc.Canvas.IGNORE){if(_1!=null){var _13=this.getScrollLeft(),_14=this.getViewportWidth(),_15=_13+_14,_16=false,_17=false;if(_1+_3>_15)_16=true;if(_1<_13)_17=true;if(_16!=_17||_9){if(_5==this.$s0){_11=_1}else if(_5==this.$s2){_11=(_1+_3)-this.getViewportWidth()}else{_11=(_1+parseInt(_3/ 2))
-parseInt(this.getViewportWidth()/2)}}}
if(_2!=null){var _18=this.getScrollTop(),_19=_18+this.getViewportHeight(),_20=false,_21=false;if(_2+_4>_19)_21=true;if(_2<_18)_20=true;if(_20!=_21||_9){if(_6==this.$s1){_12=_2}else if(_6==this.$s3){_12=(_2+_4)-this.getViewportHeight()}else{_12=(_2+parseInt(_4/ 2))
-parseInt(this.getViewportHeight()/2)}}}
if(_11!=null||_12!=null){if(_7){this.animateScroll(_11,_12,_8);_10=false}else{this.scrollTo(_11,_12,"scrollIntoView")}}}
if(this.parentElement!=null){var _22=_1,_23=_2;if(_22!=null){_22-=this.getScrollLeft();_22+=this.getOffsetLeft()}
if(_23!=null){_23-=this.getScrollTop();_23+=this.getOffsetTop()}
this.parentElement.scrollIntoView(_22,_23,_3,_4)}
if(_8&&_10)this.fireCallback(_8)}
,isc.A.intersects=function isc_Canvas_intersects(_1){var _2=_1.getPageLeft(),_3=_1.getVisibleWidth(),_4=_1.getPageTop(),_5=_1.getVisibleHeight();return this.intersectsRect(_2,_4,_3,_5)}
,isc.A.intersectsRect=function isc_Canvas_intersectsRect(_1,_2,_3,_4){var _5,_6=[];if(isc.isAn.Array(_1))_5=_1;else _5=[_1,_2,_3,_4];return isc.Canvas.rectsIntersect(_5,[this.getPageLeft(),this.getPageTop(),this.getVisibleWidth(),this.getVisibleHeight()])}
,isc.A.containsEvent=function isc_Canvas_containsEvent(){return this.containsPoint(this.ns.EH.getX(),this.ns.EH.getY())}
,isc.A.getEventEdge=function isc_Canvas_getEventEdge(_1){var _2=this.ns.EH;if(!_1)_1=(this.resizeFrom||_2.ALL_EDGES);var _3=this.edgeMarginSize;if(!isc.isAn.Array(_1))_1=[_1];var _4=this.$xu(),_5=_4.left,_6=_4.right,_7=_4.top,_8=_4.bottom;var _9=this.getPageLeft()+_5,_10=this.getPageTop()+_7,_11=(this.getPageRight()-_6)+1,_12=(this.getPageBottom()-_8)+1,y=_2.getY(),x=_2.getX(),_15="",_16="";if(y<_10||y>_12||x<_9||x>_11)return null;if(y>=(_12-_3)&&y<=_12)_16="B";else if(y>=_10&&y<=(_10+_3+1))_16="T";if(x>=(_11-_3)&&x<=_11)_15="R";else if(x>=_9&&x<=(_9+_3+1))_15="L";if(_15!=""||_16!=""){var _17=_16+_15;if(_1.contains(_17))return _17;else if(_15!=""&&_1.contains(_15))return _15;else if(_16!=""&&_1.contains(_16))return _16}
return null}
,isc.A.getOffsetX=function isc_Canvas_getOffsetX(){var _1=this.ns.EH.getX()
-(this.getPageLeft()+this.getLeftBorderSize())+this.getScrollLeft()
-(this.vscrollOn&&this.isRTL()?this.getScrollbarSize():0);return _1}
,isc.A.getOffsetY=function isc_Canvas_getOffsetY(){return this.ns.EH.getY()+this.getScrollTop()
-(this.getPageTop()+this.getTopBorderSize())}
,isc.A.setClip=function isc_Canvas_setClip(_1,_2,_3,_4){if(isc.isAn.Array(_1))
this.$vh=_1;else
this.$vh=[_1,_2,_3,_4];var _5=this.getClipHandle();if(_5!=null){var _6=this.$vh;_5.style.clip="rect("+_6.join("px ")+"px)"}}
,isc.A.getScrollbarSize=function isc_Canvas_getScrollbarSize(){if(this.showCustomScrollbars)return this.getCustomScrollbarSize();return isc.Element.getNativeScrollbarSize()}
,isc.A.getViewportWidth=function isc_Canvas_getViewportWidth(){return this.getVisibleWidth()-
(this.vscrollOn?this.getScrollbarSize():0)-
this.getHMarginBorder()}
,isc.A.getViewportHeight=function isc_Canvas_getViewportHeight(){return this.getVisibleHeight()-
(this.hscrollOn?this.getScrollbarSize():0)-
this.getVMarginBorder()}
,isc.A.getOuterViewportWidth=function isc_Canvas_getOuterViewportWidth(){return this.getVisibleWidth()-(this.vscrollOn?this.getScrollbarSize():0)-
this.getHMarginSize()}
,isc.A.getOuterViewportHeight=function isc_Canvas_getOuterViewportHeight(){return this.getVisibleHeight()-(this.hscrollOn?this.getScrollbarSize():0)-
this.getVMarginSize()}
,isc.A.getInnerHeight=function isc_Canvas_getInnerHeight(){return this.getHeight()
-((this.hscrollOn||this.overflow==isc.Canvas.SCROLL)?this.getScrollbarSize():0)
-this.getVMarginBorder()}
,isc.A.getInnerWidth=function isc_Canvas_getInnerWidth(){var _1=this.getWidth();if(this.vscrollOn||this.overflow==isc.Canvas.SCROLL||this.alwaysShowVScrollbar)
_1-=this.getScrollbarSize();return _1-this.getHMarginBorder()}
,isc.A.getInnerContentHeight=function isc_Canvas_getInnerContentHeight(){return this.getHeight()
-(this.hscrollOn||this.overflow==isc.Canvas.SCROLL?this.getScrollbarSize():0)
-this.getVMarginBorderPad()}
,isc.A.getInnerContentWidth=function isc_Canvas_getInnerContentWidth(){var _1=this.getWidth();if(this.vscrollOn||this.overflow==isc.Canvas.SCROLL||this.alwaysShowVScrollbar)
_1-=this.getScrollbarSize();return _1-this.getHMarginBorderPad()}
,isc.A.getVBorderPad=function isc_Canvas_getVBorderPad(){return this.getVBorderSize()+this.getVPadding()}
,isc.A.getHBorderPad=function isc_Canvas_getHBorderPad(){return this.getHBorderSize()+this.getHPadding()}
,isc.A.getHMarginSize=function isc_Canvas_getHMarginSize(){return this.getLeftMargin()+this.getRightMargin()}
,isc.A.getVMarginSize=function isc_Canvas_getVMarginSize(){return this.getTopMargin()+this.getBottomMargin()}
,isc.A.getVMarginBorder=function isc_Canvas_getVMarginBorder(){var _1=this.$qx(),_2=this.$qz();return _1.top+_1.bottom+_2.top+_2.bottom}
,isc.A.getHMarginBorder=function isc_Canvas_getHMarginBorder(){var _1=this.$qx(),_2=this.$qz();return _1.left+_1.right+_2.left+_2.right}
,isc.A.getVMarginBorderPad=function isc_Canvas_getVMarginBorderPad(){return this.getVMarginSize()+this.getVBorderPad()}
,isc.A.getHMarginBorderPad=function isc_Canvas_getHMarginBorderPad(){return this.getHMarginSize()+this.getHBorderPad()}
,isc.A.getClipWidth=function isc_Canvas_getClipWidth(){return this.getVisibleWidth()}
,isc.A.getClipHeight=function isc_Canvas_getClipHeight(){return this.getVisibleHeight()}
,isc.A.getVisibleWidth=function isc_Canvas_getVisibleWidth(_1){if((this.$t1||this.$t2)&&(this.overflow==isc.Canvas.VISIBLE||this.overflow==isc.Canvas.CLIP_V)){return Math.max(this.width,(this.getScrollWidth(_1)+this.getHMarginBorder()))}else{var _2=this.isAnimating(this.$d9)?this.$showAnimationInfo:this.isAnimating(this.$x4)?this.$hideAnimationInfo:null;if(_2!=null&&!_2.$x5&&this.vscrollOn){var _3=0;if(this.vscrollbar.visibility==isc.Canvas.HIDDEN){_3=this.getScrollbarSize()}else{_3=this.getScrollbarSize()-this.getScrollbarSize()}
return Math.max(this.getWidth()-_3,1)}
return this.getWidth()}}
,isc.A.getVisibleHeight=function isc_Canvas_getVisibleHeight(_1){if((this.$t1||this.$t2)&&(this.overflow==isc.Canvas.VISIBLE||this.overflow==isc.Canvas.CLIP_H))
{return Math.max(this.getHeight(),(this.getScrollHeight(_1)+this.getVMarginBorder()))}else{if(this.isAnimating()){var _2=this.isAnimating(this.$d9)?this.$showAnimationInfo:this.isAnimating(this.$x4)?this.$hideAnimationInfo:null;if(_2!=null&&_2.$x5&&this.hscrollOn){var _3=0;if(this.hscrollbar&&this.hscrollbar.visibility==isc.Canvas.HIDDEN){_3=this.getScrollbarSize()}else{_3=this.getScrollbarSize()-this.getScrollbarSize()}
return Math.max(this.getHeight()-_3,1)}}
return this.getHeight()}}
,isc.A.getPeerRect=function isc_Canvas_getPeerRect(){var _1=this.getPageRect();if(this.peers==null)return _1;for(var i=0;i<this.peers.length;i++){var _3=this.peers[i];if(!_3.isDrawn()||(this.isVisible()&&!_3.isVisible()))continue;if((!this.vscrollOn&&_3==this.vscrollbar)||(!this.hscrollOn&&_3==this.hscrollbar))continue;var _4=_3.getPageRect();if(_4[0]<_1[0])_1[0]=_4[0];if(_4[1]<_1[1])_1[1]=_4[1];var _5=_4[0]+_4[2];if(_5>_1[0]+_1[2])_1[2]=_5-_1[0];var _6=_4[1]+_4[3];if(_6>_1[1]+_1[3])_1[3]=_6-_1[1]}
return _1}
,isc.A.moveBy=function isc_Canvas_moveBy(_1,_2,_3,_4){var _5=_3&&_4;if(!_5&&this.rectAnimation)this.finishAnimation("rect");else if(!_3&&this.moveAnimation)this.finishAnimation("move");if(isc.$dd)arguments.$de=this;if(isc.isA.Number(_1))
this.left+=_1;else
_1=0;if(isc.isA.Number(_2))
this.top+=_2;else
_2=0;var _6=(_1!=0||_2!=0);if(!_6&&!_4)return false;this.$x6=_1;this.$x7=_2;var _7=(_4&&this.$x8?this.width:null),_8=(_4&&this.$x9?this.$s9:null);this.$vg(this.left,this.top,_7,_8);if(_4)this.$ya();this.$yb();return _6}
,isc.A.$yb=function isc_Canvas__completeMoveBy(){var _1=(this.$x6||0),_2=(this.$x7||0),_3;this.$x6=_3;this.$x7=_3;if(!_1&&!_2)return;this.$yc(this,_1,_2);this.$yd(_1,_2);if(this.parentElement)this.parentElement.childMoved(this,_1,_2);if(this.masterElement)this.masterElement.peerMoved(this,_1,_2);if(this._useFocusProxy&&this.$uv){var _4=this.$u2();if(_4!=null){var _5=parseInt(_4.style.left)+_1,_6=parseInt(_4.style.top)+_2;_4.style.left=_5+"px";_4.style.top=_6+"px"}}
this.$ps=this.$pt=null;this.handleMoved(_1,_2)}
,isc.A.handleMoved=function isc_Canvas_handleMoved(_1,_2){if(!this.$ye&&this.isDrawn()&&this.parentElement==null&&!isc.Page.pollPageSize)
{isc.EH.fireOnPause("checkForBodyOverflowChange",{target:isc.Canvas,methodName:"checkForPageResize"},100)}
this.moved(_1,_2)}
,isc.A.moved=function(deltaX,deltaY){}
,isc.A.parentMoved=function isc_Canvas_parentMoved(_1,_2,_3){}
,isc.A.handleParentMoved=function isc_Canvas_handleParentMoved(_1,_2,_3){this.$ps=this.$pt=null;this.parentMoved(_1,_2,_3);this.$yc(_1,_2,_3)}
,isc.A.$yc=function isc_Canvas__fireParentMoved(_1,_2,_3){var _4=this.children;if(_4!=null){for(var i=0;i<_4.length;i++){if(isc.isA.Canvas(_4[i])){_4[i].handleParentMoved(_1,_2,_3)}}}}
,isc.A.childMoved=function isc_Canvas_childMoved(_1,_2,_3){if(_1&&_1.masterElement!=null&&_1.containedPeer==true)return;if(this.allowContentAndChildren&&this.overflow==isc.Canvas.VISIBLE)
this.$yf=true;this.$yg(this.$s5)}
,isc.A.$yd=function isc_Canvas__fireMasterMoved(_1,_2){var _3=this.peers;if(_3==null)return;for(var i=0;i<_3.length;i++){if(_3[i])_3[i].masterMoved(_1,_2)}}
,isc.A.masterMoved=function isc_Canvas_masterMoved(_1,_2){if(this.$rv)this.moveBy(_1,_2)}
,isc.A.peerMoved=function isc_Canvas_peerMoved(_1,_2,_3){}
,isc.A.dragRepositioned=function isc_Canvas_dragRepositioned(){}
,isc.A.getDelta=function isc_Canvas_getDelta(_1,_2,_3){if(_2==null)return null;var _4=_1,_5=this.$tc[_1];if(_1==this.$s6)_4=this.$s8;if(isc.isA.Number(_2)){var _6=Math.round(_2);if(_6!=_2){this.logWarn(_1+" specified as fractional coordinate:"+_2+". Rounded to:"+_6);_2=_6}}else if(isc.isA.String(_2)&&isc.endsWith(_2,this.$ta)){this[_5]=_2;if(this.masterElement==null&&this.parentElement==null&&this.$ow==null){this.$ow=isc.Page.setEvent(this.$ox,this,isc.Page.FIRE_ONCE)}
if(this.$tv){_3=this[_4]=0;if(this.percentBox=="custom")this[_4]=1}
if(this.percentBox=="custom")return 0;var _7,_8,_9,_10=(_1==this.$s0||_1==this.$s7);if(this.percentSource||(this.snapTo&&this.masterElement)){_7=this.percentSource||this.masterElement;_9=(this.percentBox==this.$rz),_8=_10?(_9?_7.getViewportWidth():_7.getVisibleWidth()):(_9?_7.getViewportHeight():_7.getVisibleHeight())}else{_7=this.parentElement;_8=(_10?(_7?_7.getInnerWidth():isc.Page.getWidth()):(_7?_7.getInnerHeight():isc.Page.getHeight()))}
if(isc.Browser.isIE&&!isc.Page.isLoaded()&&((isc.Page.getWidth()==0)||(isc.Page.getHeight()==0)))
{isc.Page.setEvent("load","if(window["+this.ID+"])"+this.ID+".pageResize()",isc.Page.FIRE_ONCE);this.$t8=true}
if(isc.Browser.isChrome&&(!isc.Page.isLoaded()||isc.EH.$lw=="load")&&(isc.Page.getWidth()==0||isc.Page.getHeight()==0))
{if(isc.Page.isLoaded()){isc.Page.setEvent("idle","if(window."+this.ID+")"+this.ID+".pageResize()",isc.Page.FIRE_ONCE)}else{isc.Page.setEvent("load","if(window."+this.ID+")"+this.ID+".delayCall('pageResize',[],100)",isc.Page.FIRE_ONCE)}
this.$t8=true}
_2=Math.round((parseInt(_2,10)/100)*_8);var _11=this[this.$td[_1]];if(_11!=null&&_2<_11){_2=_11}
return _2-_3}
var _12=_2;if(!isc.isA.Number(_2)){_2=parseInt(_2);if(isc.isA.Number(_2)&&isc.isA.String(_3)){this[_4]=_3=_2}}
this[_5]=null;var _13=false;if(!isc.isA.Number(_2)||(_2<0&&(_1==this.$s7||_1==this.$s6)))
{if(_12!="*"){this.logWarn("ignoring bad or negative "+_1+": "+_12+(this.logIsDebugEnabled("sizing")?this.getStackTrace():" [enable 'sizing' log for stack trace]"))}else{_1==this.$s7?this.$ts="*":this.$tt="*";var _7=this.parentElement;if(isc.isA.Layout(_7)&&_7.hasMember(this)){_7.reflow(this.getID()+" set "+_1+" to '*'");_13=true}}
if(!_13&&(_3==this[_1]||_3==this[_4]))
{_3=this.restoreDefaultSize(_1==this.$s6)}
this.adjustOverflow();return null}
return _2-_3}
,isc.A.restoreDefaultSize=function isc_Canvas_restoreDefaultSize(_1){var _2=_1?this.$s6:this.$s7,_3=this.getClass().getInstanceProperty(_2);if(!isc.isA.Number(_3)){if(_1)_3=this.defaultHeight;else _3=this.defaultWidth}
var _4=this[_2]=(isc.isA.Number(_3)?_3:0);if(_1)this.$s9=_4;return _4}
,isc.A.pageResize=function isc_Canvas_pageResize(){this.$ye=true;this.$ow=null;this.$t8=null;this.$vb();delete this.$ye}
,isc.A.moveTo=function isc_Canvas_moveTo(_1,_2,_3,_4){if(!_4&&_1==null&&_2==null)return false;if(isc.$dd)arguments.$de=this;if(_1!=null&&_1.top!=null){_2=_1.top;_1=_1.left}
var _5=this.getDelta(this.$s0,_1,this.getLeft()),_6=this.getDelta(this.$s1,_2,this.getTop());return this.moveBy(_5,_6,_3,_4)}
,isc.A.moveToEvent=function isc_Canvas_moveToEvent(_1,_2){var _3=this.ns.EH.getLastEvent(),x=_3.x,y=_3.y;if(isc.isA.Number(_1))x-=_1;if(isc.isA.Number(_2))y-=_2;var _6=this.ns.EH;var _7=_6.getDragTarget(_3);var _8;if(_6.getDragTarget().canDrop){_8=_6.getDropTarget(_3);if(_8){if(!_7.snapOnDrop||!_8.shouldSnapOnDrop(_7)){_8=null}}else{_8=_6.getDragTarget(_3).parentElement}}else{_8=_6.getDragTarget(_3).parentElement}
if(isc.isA.Canvas(_8)&&(_7.snapToGrid==true||(_7.snapToGrid==null&&_8.childrenSnapToGrid==true)))
{if(_8.noSnapDragOffset(this)){x=_3.x,y=_3.y}
if(_8.suppressHSnapOffset==true)x=_3.x;if(_8.suppressVSnapOffset==true)y=_3.y;if(_8.snapAxis==isc.Canvas.HORIZONTAL||_8.snapAxis==isc.Canvas.BOTH)
{var _9=(_8.getPageLeft()+_8.getLeftBorderSize()+_8.getLeftMargin()-_8.getScrollLeft());x-=_9;x=_8.getHSnapPosition(x)+_8.getHSnapOrigin(_7);x+=_9}
if(_8.snapAxis==isc.Canvas.VERTICAL||_8.snapAxis==isc.Canvas.BOTH)
{var _9=(_8.getPageTop()+_8.getTopBorderSize()+_8.getTopMargin()-_8.getScrollTop())
y-=_9;y=_8.getVSnapPosition(y)+_8.getVSnapOrigin(_7);y+=_9}}
this.setPageRect(x,y)}
,isc.A.getVSnapOrigin=function isc_Canvas_getVSnapOrigin(_1){return this.VSnapOrigin?this.VSnapOrigin:0}
,isc.A.getHSnapOrigin=function isc_Canvas_getHSnapOrigin(_1){return this.HSnapOrigin?this.HSnapOrigin:0}
,isc.A.placeNextTo=function isc_Canvas_placeNextTo(_1,_2,_3,_4){var _5=_1.getPeerRect(),_6=this.getPeerRect(),_7=isc.Canvas.$yh(_6[2],_6[3],_5,_2,_3,_4);this.setPageRect(_7[0],_7[1])}
,isc.A.showNextTo=function isc_Canvas_showNextTo(_1,_2,_3){if(_2==null)_2="right";if(_3==null)_3=false;this.placeNextTo(_1,_2,_3);this.animateShow("fade")}
,isc.A.placeNear=function isc_Canvas_placeNear(_1,_2){if(isc.isAn.Array(_1)){_2=_1[1];_1=_1[0]}else if(isc.isAn.Object(_1)){_2=_1.top;_1=_1.left}
var _3=this.getPeerRect(),_4=isc.Canvas.$yh(_3[2],_3[3],{left:_1,top:_2});this.setPageRect(_4[0],_4[1])}
,isc.A.resizeBy=function isc_Canvas_resizeBy(_1,_2,_3,_4){if(isc.$dd)arguments.$de=this;var _5=_3&&_4;if(!_5&&this.rectAnimation)this.finishAnimation("rect");if(!_3){if(_5&&this.resizeAnimation)this.finishAnimation("resize");if(this.hideAnimation)this.finishAnimation("hide");if(this.showAnimation)this.finishAnimation("show")}
var _6=this.getWidth(),_7=this.getHeight();if(isc.isA.Number(_1)){this.width+=_1;if(!this.$tv)this.$yi=true}else{_1=0}
if(isc.isA.Number(_2)){this.height=this.$s9=_7+_2;if(!this.$tv)this.$yj=true}else{_2=0}
if(_1==0&&_2==0)return false;this.$x8=_1;this.$x9=_2;this.$yk=_3;if(this.isDrawn()&&this.logIsInfoEnabled(this.$ox)){this.logInfo("resize of drawn component: "+"new width/height: "+[this.width,this.$s9]+", old width/height: "+[_6,_7]+", delta width/height: "+[_1,_2]+(this.logIsDebugEnabled(this.$ox)?this.getStackTrace():""),this.$ox)}
if(!_4){var _8=this.$vh;if(isc.isAn.Array(_8)){_8[1]+=_1;_8[2]+=_2}
var _9=this.getDrawnState();if(_9==isc.Canvas.COMPLETE){this.$vg(this.left,this.top,this.width,this.$s9);if(isc.isAn.Array(_8))this.setClip(_8)}else if(_9!=isc.Canvas.UNDRAWN){this.$vf=true}
this.$ya()}
return true}
);isc.evalBoundary;isc.B.push(isc.A.$ya=function isc_Canvas__completeResizeBy(){var _1=(this.$x8||0),_2=(this.$x9||0),_3=this.$yk,_4;this.$x8=_4;this.$x9=_4;this.$yk=_4;if(!_1&&!_2)return;var _5;if(this.isDrawn()){_5=this.shouldRedrawOnResize(_1,_2,_3);if(_5){this.markForRedraw(this.$ox)}}
if(!_3)this.layoutChildren(this.$te,_1,_2)
if(isc.Browser.isMoz&&this.containsIFrame())this.$t5();this.$yl(_1,_2);if(!_5)this.adjustOverflow(this.$ox);if(!_3&&this._useFocusProxy&&this.$uv){var _6=this.$w7();if(_6!=null){_6.style.width=this.getWidth()+isc.px;_6.style.height=this.getHeight()+isc.px}}
this.resizePeersBy(_1,_2);this.$ym(_1,_2)}
,isc.A.shouldRedrawOnResize=function isc_Canvas_shouldRedrawOnResize(_1,_2){var _3=this.redrawOnResize;if(_3==null){_3=!((this.children!=null&&this.children.length>0&&!this.allowContentAndChildren)||(this.getInnerHTML==isc.Canvas.$ce.getInnerHTML&&!isc.isA.Function(this.contents)))}
return _3}
,isc.A.dragResizing=function isc_Canvas_dragResizing(){var _1=isc.EH;return(_1.dragging&&_1.dragOperation==_1.DRAG_RESIZE&&_1.dragTarget==this)}
,isc.A.$ym=function(deltaX,deltaY,reason){if(isc.$dd)arguments.$de=this;if(this.snapTo)this.$vb(true);if(this.parentElement)this.parentElement.childResized(this,deltaX,deltaY,reason);if(this.masterElement)this.masterElement.peerResized(this,deltaX,deltaY,reason);var peers=this.peers;if(peers){for(var i=0;i<peers.length;i++){if(isc.isA.Canvas(peers[i]))peers[i].masterResized(deltaX,deltaY,reason)}}
if(this.clipCorners&&this.$yn){var clips=this.$yn;if(clips.TR)clips.TR.moveBy(deltaX,null);if(clips.BL)clips.BL.moveBy(null,deltaY);if(clips.BR)clips.BR.moveBy(deltaX,deltaY)}
if(this.$yo!=null)delete this.$yo;if(this.$yp!=null)delete this.$yp;this.resized(deltaX,deltaY,reason);if(!this.$ye&&this.isDrawn()&&this.parentElement==null&&!isc.Page.pollPageSize)
{isc.EH.fireOnPause("checkForBodyOverflowChange",{target:isc.Canvas,methodName:"checkForPageResize"},100)}}
,isc.A.$yl=function isc_Canvas__handleResized(){}
,isc.A.resized=function isc_Canvas_resized(_1,_2){}
,isc.A.innerSizeChanged=function isc_Canvas_innerSizeChanged(_1){this.$yq();this.layoutChildren(_1);var _2=this.peers;if(_2){for(var i=0;i<_2.length;i++){if(!_2[i].percentSource&&_2[i].snapTo&&_2[i].percentBox==this.$rz)
{_2[i].$vb()}}}}
,isc.A.setPercentSource=function isc_Canvas_setPercentSource(_1,_2){if(isc.isA.String(_1))_1=window[_1];if(!_2&&this.percentSource==_1)return;if(this.percentSource&&this.isObserving(this.percentSource,"innerSizeChanged")){this.ignore(this.percentSource,"innerSizeChanged");this.ignore(this.percentSource,"resized")}
if(!isc.isA.Canvas(_1)){this.percentSource=null;return}
this.percentSource=_1;this.observe(_1,"innerSizeChanged","observer.percentSourceInnerSizeChanged()");this.observe(_1,"resized","observer.$vb()")}
,isc.A.percentSourceInnerSizeChanged=function isc_Canvas_percentSourceInnerSizeChanged(){if(this.percentBox==this.$rz)this.$vb()}
,isc.A.childResized=function isc_Canvas_childResized(_1,_2,_3,_4){if(this.allowContentAndChildren&&this.overflow==isc.Canvas.VISIBLE)
this.$yf=true;this.$yg(this.$tf)}
,isc.A.peerResized=function isc_Canvas_peerResized(_1,_2,_3,_4){}
,isc.A.masterResized=function isc_Canvas_masterResized(_1,_2,_3){this.$vb()}
,isc.A.dragResized=function isc_Canvas_dragResized(){}
,isc.A.resizePeersBy=function isc_Canvas_resizePeersBy(_1,_2){var _3=this.peers;if(_3){for(var i=0;i<_3.length;i++){if(_3[i]&&_3[i].masterElement==this&&_3[i].$k6){_3[i].resizeBy(_1,_2)}}}}
,isc.A.layoutChildren=function isc_Canvas_layoutChildren(_1,_2,_3){if(this.children)this.$yr()}
,isc.A.$yr=function isc_Canvas__resolveChildPercentSizes(){var _1=this.children;if(_1!=null&&_1.length>0){for(var i=0;i<_1.length;i++){if(isc.isA.Canvas(_1[i]))_1[i].parentResized()}}}
,isc.A.resizeTo=function isc_Canvas_resizeTo(_1,_2,_3,_4){if(isc.$dd)arguments.$de=this;if(_1==null&&_2==null)return false;var _5=this.getDelta(this.$s7,_1,this.getWidth()),_6=this.getDelta(this.$s6,_2,this.getHeight());return this.resizeBy(_5,_6,_3,_4)}
,isc.A.resizeToEvent=function isc_Canvas_resizeToEvent(_1){var _2=this.ns.EH,_3=_2.getLastEvent(),x=_3.x,y=_3.y,_6=this.getPageLeft(),_7=this.getPageTop(),_8=this.getPageRight(),_9=this.getPageBottom();var _10=_2.getDragTarget(_3);var _11=_2.getDragTarget(_3).parentElement;if(_11){if(_10.snapResizeToGrid==true||(_10.snapResizeToGrid==null&&_10.snapToGrid==true)||(_10.snapResizeToGrid==null&&(_11.childrenSnapResizeToGrid==true||(_11.childrenSnapResizeToGrid==null&&_11.childrenSnapToGrid==true)))){if(_11.snapAxis==isc.Canvas.HORIZONTAL||_11.snapAxis==isc.Canvas.BOTH){var _12=(_11.getPageLeft()+_11.getLeftBorderSize()+_11.getLeftMargin()-_11.getScrollLeft());x-=_12;x=_11.getHSnapPosition(x)+_11.getHSnapOrigin(_10);x+=_12}
if(_11.snapAxis==isc.Canvas.VERTICAL||_11.snapAxis==isc.Canvas.BOTH){_12=(_11.getPageTop()+_11.getTopBorderSize()+_11.getTopMargin()-_11.getScrollTop());y-=_12;y=_11.getVSnapPosition(y)+_11.getVSnapOrigin(_10);y+=_12}}}
if(this.logIsDebugEnabled("dragResize")){this.logDebug("resizeToEvent: coords: "+isc.Log.echo({x:x,y:y,left:_6,top:_7,right:_8,bottom:_9}),"dragResize")}
_1=_1||_2.resizeEdge||"BR";if(_1.contains("T")){var _13=Math.min(this.maxHeight,Math.max(_9-y,this.minHeight));_7=_9-_13}else if(_1.contains("B")){var _13=Math.min(this.maxHeight,Math.max(y-_7,this.minHeight));_9=_7+_13}
if(_1.contains("L")){var _14=Math.min(this.maxWidth,Math.max(_8-x,this.minWidth));_6=_8-_14}else if(_1.contains("R")){var _14=Math.min(this.maxWidth,Math.max(x-_6,this.minWidth));_8=_6+_14}
var _15=_8-_6,_16=_9-_7;this.setPageRect(_6,_7,_15,_16,true);_2.dragResizeWidth=_15;_2.dragResizeHeight=_16;if(this==this.ns.EH.dragTracker)this.redrawIfDirty()}
,isc.A.resizeTarget=function isc_Canvas_resizeTarget(_1,_2,_3,_4,_5,_6,_7){_5=_5||0;_4=_4||0;if(_6==null)_6=_2?isc.EH.getY():isc.EH.getX();_6+=_4;if(this.parentElement){var _8=this.getParentPageRect(),_9=_2?(_8[1]+_8[3]):(_8[0]+_8[2]);_9-=_2?this.getVisibleHeight():this.getVisibleWidth();if(_6>_9)_6=_9}
_7=_7!=null?_7:!_2&&this.isRTL();var _10=_2?_1.getMinHeight():_1.getMinWidth(),_11=_2?_1.getMaxHeight():_1.getMaxWidth();var _12;if(_7){_12=(_2?_1.getPageBottom():_1.getPageRight())
-(_2?this.getVisibleHeight():this.getVisibleWidth())}else{_12=_2?_1.getPageTop():_1.getPageLeft()}
var _13=!_7?_6-_12-_5:_12-_6-_5;if(_13<_10){_13=_10}else if(_13>_11){_13=_11}
this.$ys=_13;_6=_12+_5+(_7?-_13:_13);if(_3){_2?_1.setHeight(this.$ys):_1.setWidth(this.$ys)}else{_2?this.setPageTop(_6):this.setPageLeft(_6)}}
,isc.A.finishTargetResize=function isc_Canvas_finishTargetResize(_1,_2,_3){if(_3)return;_2?_1.setHeight(this.$ys):_1.setWidth(this.$ys)}
,isc.A.parentResized=function isc_Canvas_parentResized(){if(isc.$dd)arguments.$de=this;this.$vb()}
,isc.A.$vb=function isc_Canvas__resolvePercentageSize(_1){if(this.snapTo!=null&&this.percentBox!="custom"){if((this._percent_width||this._percent_height)&&!_1){this.resizeTo(this._percent_width,this._percent_height)}
var _2,_3,_4;_2=(this.masterElement?this.masterElement:this.parentElement);if(!_2)return;isc.Canvas.snapToEdge(_2,this.snapTo,this,this.snapEdge)}
if(this.snapTo==null&&!_1){if(this._percent_left||this._percent_top||this._percent_width||this._percent_height)
{this.setRect(this._percent_left,this._percent_top,this._percent_width,this._percent_height)}}}
,isc.A.prepareForDragging=function isc_Canvas_prepareForDragging(){var _1=this.ns.EH;if(_1.dragTarget)return;var _2=false,_3=this.dragOperation;if(isc.Browser.isTouch&&this.touchDragOperation&&_1.lastEvent.originalType==_1.TOUCH_START)
{_3=this.touchDragOperation}
if(_3){_2=true;_1.dragOperation=_3}else if(this.canDragResize){_1.resizeEdge=this.getEventEdge();if(_1.resizeEdge){_2=true;_1.dragOperation=_1.DRAG_RESIZE;var _4=this.getDragAppearance(_1.DRAG_RESIZE);_1.dragMoveAction=(_4=="tracker")?_1.$nh:_1.$oo}}
if(!_2){if(this.canDragReposition){_2=true;_1.dragOperation=_1.DRAG_REPOSITION;_1.dragMoveAction=_1.$nh}else if(isc.Browser.isTouch&&(this.hscrollOn||this.vscrollOn)&&!this.dragOperation)
{_2=true;_1.dragOperation=_1.DRAG_SCROLL}else if(this.canDrag){_2=true;_1.dragOperation=_1.DRAG}else if(this.canSelectText&&this.overflow!="visible"){_2=true;_1.dragOperation=_1.DRAG_SELECT;this.dragAppearance="none"}}
if(_2){var _5=this;if(this.dragTarget!=null){if(isc.isA.Canvas(this.dragTarget)){_5=this.dragTarget}else if(this.dragTarget=="top"&&this.topElement){_5=this.topElement}else if(this.dragTarget=="parent"&&this.parentElement){_5=this.parentElement}else if(this.dragTarget=="creator"&&this.creator){_5=this.creator}else if(isc.isA.String(this.dragTarget)&&isc.isA.Canvas(window[this.dragTarget]))
{_5=window[this.dragTarget]}else{this.logWarn('prepareForDragging():  target.dragTarget not understood : '+this.dragTarget)}}
_1.dragTarget=_5}}
,isc.A.dragScrollStart=function isc_Canvas_dragScrollStart(){var _1=this.dragScrollTarget||this;this.$yt=isc.EH.getX();this.$yu=isc.EH.getY();this.$yv=_1.scrollLeft||0;this.$yw=_1.scrollTop||0;this.$yx=this.$yy=isc.EH.getX();this.$yz=this.$y0=isc.EH.getY();this.$y1=this.$y2=isc.timestamp()}
,isc.A.dragScrollMove=function isc_Canvas_dragScrollMove(){var _1=this.dragScrollTarget||this;var _2=this.$yt-isc.EH.getX(),_3=this.$yu-isc.EH.getY();_1.scrollTo(this.$yv+_2,this.$yw+_3,"dragScrollMove");if(window.event)window.event.preventDefault();this.$yx=this.$yy;this.$yz=this.$y0;this.$y1=this.$y2;this.$yy=isc.EH.getX();this.$y0=isc.EH.getY();this.$y2=isc.timestamp()}
,isc.A.dragScrollStop=function isc_Canvas_dragScrollStop(){if(!this.momentumScrolling)return;var _1=(this.$y2-this.$y1);if(_1==0)return;if(isc.timestamp()-this.$y2>100)return;var _2=(this.$yy-this.$yx)/_1,_3=(this.$y0-this.$yz)/_1,_4=this,_5=this.dragScrollTarget||this;if(!_5.hscrollOn)_2=0;if(!_5.vscrollOn)_3=0;if(this.logIsDebugEnabled("dragScroll")){this.logDebug("dragScroll: x/y: "+[this.$yy,this.$y0]+", last: "+[this.$yx,this.$yz]+", elapsed: "+_1+", speed: "+[_2,_3],"dragScroll")}
if(_2==0&&_3==0)return;var _6=this.$y3=this.registerAnimation(function(_14){var _7=isc.timestamp(),_1=_7-_4.$y2;_4.$y2=_7;var _8=_2*(1-_14),_9=_3*(1-_14);var _10=Math.round(_8*_1),_11=Math.round(_9*_1);if(this.logIsDebugEnabled("dragScroll")){this.logDebug("animating: elapsed: "+_1+", frame speed: "+[_8,_9]+", distance: "+[_10,_11],"dragScroll")}
if(_10==0&&_11==0)_4.cancelAnimation(_6);var _12=_5.getScrollLeft(),_13=_5.getScrollTop();_5.scrollTo(_5.getScrollLeft()-_10,_5.getScrollTop()-_11,"dragScrollStop");if(_12==_5.getScrollLeft()&&_13==_5.getScrollTop())
{_4.cancelAnimation(_6)}},this.momentumScrollTime,this.momentumScrollAcceleration)}
,isc.A.hoopSelectStart=function isc_Canvas_hoopSelectStart(){if(!this.hoopSelector)this.hoopSelector=this.createAutoChild("hoopSelector");if(this.hoopSelectorRect)this.hoopSelector.keepInParentRect=this.hoopSelectorRect;var _1=this.$y4=this.hoopSelectorRect||[this.getPageLeft()+this.getLeftBorderSize(),this.getPageTop()+this.getTopBorderSize(),this.getViewportWidth(),this.getViewportHeight()];this.$y5=this.hoopSelectAxis=="horizontal"?_1[3]:null;this.$y6=this.hoopSelectAxis=="vertical"?_1[2]:null;this.$y7=this.getOffsetX();this.$y8=this.getOffsetY();this.resizeHoopSelector();this.hoopSelector.show();return isc.EH.STOP_BUBBLING}
,isc.A.hoopSelectMove=function isc_Canvas_hoopSelectMove(){this.resizeHoopSelector()}
,isc.A.hoopSelectStop=function isc_Canvas_hoopSelectStop(){if(this.hoopSelector)this.hoopSelector.hide()}
,isc.A.resizeHoopSelector=function isc_Canvas_resizeHoopSelector(){if(!this.hoopSelector)return;var x=this.getOffsetX(),y=this.getOffsetY();if(this.hoopSelector.keepInParentRect){if(x<0)x=0;var _3=this.$y4[3];if(y>_3)y=_3}
var _4=Math.max(1,this.$y5?this.$y5:Math.abs(y-this.$y8));var _5=Math.max(1,this.$y6?this.$y6:Math.abs(x-this.$y7));this.hoopSelector.resizeTo(_5,_4);if(!this.$y6){if(x<this.$y7)this.hoopSelector.setLeft(x);else this.hoopSelector.setLeft(this.$y7)}else{this.hoopSelector.setLeft(this.$y4[0])}
if(!this.$y5){if(y<this.$y8)this.hoopSelector.setTop(y);else this.hoopSelector.setTop(this.$y8)}else{this.hoopSelector.setTop(this.$y4[1])}
this.updateHoopSelection()}
,isc.A.updateHoopSelection=function isc_Canvas_updateHoopSelection(){}
,isc.A.setNoDropIndicator=function isc_Canvas_setNoDropIndicator(){this.$y9=true;this.$ms();if(this.shouldSetNoDropTracker&&isc.EH.dragTracker&&isc.EH.dragTracker.isVisible()){if(!this.$za)this.$za=isc.EH.dragTracker.getContents();isc.EH.setDragTracker(this.imgHTML(this.noDropTracker))}}
,isc.A.clearNoDropIndicator=function isc_Canvas_clearNoDropIndicator(){if(!this.$y9)return;delete this.$y9;this.$ms();if(this.shouldSetNoDropTracker&&isc.EH.dragTracker){isc.EH.setDragTracker(this.$za);delete this.$za}}
,isc.A.shouldDragScroll=function isc_Canvas_shouldDragScroll(){return this.canDragScroll}
,isc.A.$zb=function isc_Canvas__getVDragScrollDirection(_1){var _2=this.getVDragScrollThreshold();if(_1<_2)return-1;if(_1>(this.getViewportHeight()-_2))return 1;return 0}
,isc.A.$zc=function isc_Canvas__getHDragScrollDirection(_1){var _2=this.getHDragScrollThreshold();if(_1<_2)return-1;if(_1>(this.getViewportWidth()-_2))return 1;return 0}
,isc.A.$n1=function isc_Canvas__overDragThreshold(_1){var _2=(this.getOffsetY()-this.getScrollTop()),_3=(this.getOffsetX()-this.getScrollLeft());if(_1!=null){if(_1==isc.Canvas.VERTICAL)
return this.$zb(_2)!=0;else
return this.$zc(_3)!=0}
return(this.$zb(_2)!=0||this.$zc(_3)!=0)}
,isc.A.getHDragScrollThreshold=function isc_Canvas_getHDragScrollThreshold(){if(this.$yo!=null)return this.$yo;var _1=this.dragScrollThreshold;if(isc.isA.Number(_1))this.$yo=_1;else{_1=parseInt(_1);if(!isNaN(_1)){this.$yo=parseInt(_1*this.getViewportWidth()/100);return this.$yo}else{isc.Log.logWarn("Unable to resolve specified drag scroll threshold '"+this.dragScrollThreshold+"' to a valid size. Should be specified as"+" an absolute pixel value, or a percentage of widget viewport.");return 0}}}
,isc.A.getVDragScrollThreshold=function isc_Canvas_getVDragScrollThreshold(){if(this.$yp!=null)return this.$yp;var _1=this.dragScrollThreshold;if(isc.isA.Number(_1))this.$yp=_1;else{_1=parseInt(_1);if(!isNaN(_1)){this.$yp=parseInt(_1*this.getViewportHeight()/100);return this.$yp}else{isc.Log.logWarn("Unable to resolve specified drag scroll threshold '"+this.dragScrollThreshold+"' to a valid size. Should be specified as"+" an absolute pixel value, or a percentage of widget viewport.");return 0}}}
,isc.A.$n2=function isc_Canvas__setupDragScroll(_1,_2){if(this.$zd!=null)return;var _3=(this.getOffsetY()-this.getScrollTop()),_4=(this.getOffsetX()-this.getScrollLeft()),_5=this.$zc(_4),_6=this.$zb(_3);this.$zd=isc.Timer.setTimeout({target:this,methodName:"$ze",args:[_5,_6,true,_1,_2]},this.dragScrollDelay)}
,isc.A.$ze=function isc_Canvas__performDragScroll(_1,_2,_3,_4,_5){this.$zd=null;var _6=0,_7=0;var _8=this.containsEvent();if(this.ns.EH.dragging&&(_5||_8)){var _9=this.getOffsetX()-this.getScrollLeft(),_10=this.getOffsetY()-this.getScrollTop(),_11=this.getViewportWidth(),_12=this.getViewportHeight();if(!isc.isA.Number(this.maxDragScrollIncrement)){var _13=parseInt(this.maxDragScrollIncrement);if(!isc.isA.Number(_13))
this.logWarn("Unable to resolve this.maxDragScrollIncrement '"+this.maxDragScrollIncrement+"' to a valid value. This should be an "+"absolute pixel value or a percentage to scroll by.");this.$zf=parseInt(_13/ 100*this.getScrollWidth());this.$zg=parseInt(_13/ 100*this.getScrollHeight())}else{this.$zf=this.$zg=this.maxDragScrollIncrement}
if(!isc.isA.Number(this.minDragScrollIncrement)){var _14=parseInt(this.minDragScrollIncrement);if(!isc.isA.Number(_14))
this.logWarn("Unable to resolve this.minDragScrollIncrement '"+this.minDragScrollIncrement+"' to a valid value. This should be an "+"absolute pixel value or a percentage to scroll by.");this.$zh=parseInt(_14/ 100*(this.getScrollWidth()-_11));this.$zi=parseInt(_14/ 100*(this.getScrollHeight()-_12))}else{this.$zh=this.$zi=this.minDragScrollIncrement}
var _15=(_4==isc.Canvas.VERTICAL?0:this.$zc(_9)),_16=(_4==isc.Canvas.HORIZONTAL?0:this.$zb(_10));if(_3){if(_1!=0&&_1!=_15)
_1=0;if(_2!=0&&_2!=_16)
_2=0}else{_1=_15;_2=_16}
if(_8){_6=this.getScrollIncrement(_1,_9,_11,this.getHDragScrollThreshold(),this.$zf,this.$zh);_7=this.getScrollIncrement(_2,_10,_12,this.getVDragScrollThreshold(),this.$zg,this.$zi)}else{_6=_1*this.$zf;_7=_2*this.$zg}
if((_6>0&&(this.getScrollLeft()>=this.getScrollRight()))||(_6<0&&(this.getScrollLeft()<=0)))_6=0;if((_7>0&&(this.getScrollTop()>=this.getScrollBottom()))||(_7<0&&(this.getScrollTop()<=0)))_7=0}
if(_6!=0||_7!=0){this.scrollBy(_6,_7);this.$zd=isc.Timer.setTimeout({target:this,methodName:"$ze",args:[null,null,null,_4,_5]},50)}else{delete this.$zf;delete this.$zh;delete this.$zg;delete this.$zi}}
,isc.A.getScrollIncrement=function isc_Canvas_getScrollIncrement(_1,_2,_3,_4,_5,_6){if(_1==null||_1==0)return 0;if(_1>0){_2=_2-(_3-_4)}else if(_1<0){_2=_4-_2}
if(_2<0||_2>_4)return 0;var _7=_1*
((_2/ _4)*(_5-_6)+_6);return parseInt(_7)}
,isc.A.hasInherentHeight=function isc_Canvas_hasInherentHeight(){if(this.inherentHeight!=null)return this.inherentHeight;return(this.children==null&&(this.overflow==isc.Canvas.VISIBLE||this.overflow==isc.Canvas.CLIP_H))}
,isc.A.hasInherentWidth=function isc_Canvas_hasInherentWidth(){if(this.inherentWidth!=null)return this.inherentWidth;return(this.children==null&&(this.overflow==isc.Canvas.VISIBLE||this.overflow==isc.Canvas.CLIP_V))}
,isc.A.canOverflowWidth=function isc_Canvas_canOverflowWidth(){return this.overflow==isc.Canvas.VISIBLE||this.overflow==isc.Canvas.CLIP_H}
,isc.A.canOverflowHeight=function isc_Canvas_canOverflowHeight(){return this.overflow==isc.Canvas.VISIBLE||this.overflow==isc.Canvas.CLIP_V}
,isc.A.getOverflow=function isc_Canvas_getOverflow(){return this.overflow}
,isc.A.setOverflow=function isc_Canvas_setOverflow(_1){if(this.$zj!=null&&!this.$zk)
this.finishAnimation(this.$zj);if(this.$zl!=null&&!this.$zm)
this.finishAnimation(this.$zl);if(this.overflow==_1)return;var _2=this.overflow;this.overflow=_1;if(!this.isDrawn())return;if(_1!=isc.Canvas.SCROLL&&_1!=isc.Canvas.AUTO&&(this.hscrollOn||this.vscrollOn))
{this.hscrollOn=this.vscrollOn=false;if(this.hscrollbar!=null)this.hscrollbar.hide();if(this.vscrollbar!=null)this.vscrollbar.hide()}
if(isc.Browser.isIE&&(_1==isc.Canvas.CLIP_H||_1==isc.Canvas.CLIP_V))
{this.markForRedraw();return}
var _3=this.getStyleHandle();_3.overflow=this.$wi();var _4=this.$wj();_3.width=_4[0];_3.height=_4[1];if(_3.clip!=null&&_3.clip!=""&&_3.clip!="rect(auto auto auto auto)")
{_3.clip=(isc.Browser.isIE?"rect(auto)":"")}
this.adjustOverflow("setOverflow");if(_2==isc.Canvas.VISIBLE&&_1!=isc.Canvas.VISIBLE){var _5=Math.max(this.getScrollWidth()-this.getInnerWidth(),0),_6=Math.max(this.getScrollHeight()-this.getInnerHeight(),0);if(_5>0||_6>0)this.$ym(-_5,-_6,"overflow changed")}else if(_2!=isc.Canvas.VISIBLE&&_1==isc.Canvas.VISIBLE){var _5=Math.max(this.getScrollWidth()-this.getInnerWidth(),0),_6=Math.max(this.getScrollHeight()-this.getInnerHeight(),0);if(_5>0||_6>0)this.$ym(_5,_6,"overflow changed")}
if((_1==isc.Canvas.HIDDEN||_1==isc.Canvas.VISIBLE)&&(_2==isc.Canvas.HIDDEN||_2==isc.Canvas.VISIBLE)){}else{this.$zn()}}
,isc.A.$yg=function isc_Canvas__markForAdjustOverflow(_1){if(!this.isDrawn()||this.isDirty()||this.destroying||this.$vz)return;if(!this.$v3){if(this.logIsDebugEnabled())
this.logDebug("delaying adjustOverflow: "+(_1?_1:this.getStackTrace()));var _2=this;this.$v4=isc.Timer.setTimeout(function(){if(!_2.destroyed)_2.adjustOverflow(_1,true)},0)}
this.$v3=true}
,isc.A.adjustForContent=function isc_Canvas_adjustForContent(_1){var _2="adjustForContent() called";if(_1)this.adjustOverflow(_2);else this.$yg(_2)}
,isc.A.$u8=function isc_Canvas__browserDoneDrawing(){var _1=this.getHandle();if(isc.Browser.isOpera){var _1=this.getHandle();return!(_1.scrollHeight==0&&_1.scrollWidth==0)}
if(!isc.Browser.isIE){var _2=this.getClipHandle();if(_2==null)return false;var _3=_2.scrollHeight;if(_3==null||_3==0)_3=this.getClipHandle().offsetHeight;return _3!=0}
var _4;if(isc.Browser.isWin){return _1!=null&&_1.scrollHeight!=this.$r3&&_1.scrollHeight!=0}}
,isc.A.adjustOverflow=function isc_Canvas_adjustOverflow(_1,_2,_3){if(isc.$dd)arguments.$de=this;if(_2&&!this.$v3){return}
this.$v3=false;if(!this.isDrawn()||this.overflow==isc.Canvas.IGNORE)return true;if(!this.adjustOverflowWhileDirty&&!_3&&this.isDirty()&&(this.overflow!=isc.Canvas.VISIBLE))
{return}
if(!isc.Page.isLoaded()&&(isc.Browser.isSafari||(isc.Browser.isMoz&&isc.Browser.geckoVersion<20040616)))
{isc.Page.setEvent("load",this,isc.Page.FIRE_ONCE,"$zo");if(isc.Browser.isMoz)return}
if(this.$vt)return;if(this.$u8())return this.$zp(_1);if(this.logIsDebugEnabled("overflow")){this.logDebug("browser not done drawing, deferring overflow.","overflow");if(this.useClipDiv){this.logDebug("clipHandle sizes: "+this.echoElementSize(this.getClipHandle()),"overflow")}
this.logDebug("handle sizes: "+this.echoElementSize(this.getHandle()),"overflow")}
if(!this.$zq){this.$yg();this.$zq=true}else{this.logDebug("still waiting for size to become available","overflow");this.$zr()}
return false}
,isc.A.$zo=function isc_Canvas__adjustOverflowForPageLoad(){if(!this.destroyed&&this.isDrawn())this.adjustOverflow("pageLoad")}
,isc.A.$zr=function isc_Canvas__queueForDelayedAdjustOverflow(){isc.Canvas.$zr(this.getID())}
,isc.A.$zp=function isc_Canvas__adjustOverflow(_1){if(this.$zs){return}
this.$zs=true;this.$zt(_1);this.$zs=false}
,isc.A.$zt=function isc_Canvas___adjustOverflow(_1){if(!this.$tg[this.overflow]){this.logWarn("This widget has overflow specified as "+this.echo(this.overflow)+".  This overflow setting is not supported - defaulting to overflow:\"visible\".");this.overflow=isc.Canvas.VISIBLE}
if(this.$xg!=null)delete this.$xg;if(this.$xk!=null)delete this.$xk;var _2=this.$zu,_3=this.$zv;delete this.$zu;delete this.$zv;var _4=this.$zw;this.$zw=false;var _5=isc.Canvas;this.$zq=null;if(this.getHandle()==null)this.logWarn("adjustOverflow: handle null");if(this.getClipHandle()==null)this.logWarn("adjustOverflow: clipHandle null");if(this.alwaysShowVScrollbar){if(this.overflow!=isc.Canvas.AUTO||this.overflow!=isc.Canvas.SCROLL){this.logInfo("alwaysShowVScrollbar specified as true, but overflow set to \""+this.overflow+"\". Property will be ignored.")}else if(this.showCustomScrollbars==false){this.logWarn("alwaysShowVScrollbar property not supported when showing native scrollbars")}}
if(this.logIsInfoEnabled(this.$th)){this.logInfo("Specified size: "+this.getWidth()+"x"+this.getHeight()+", drawn scroll size: "+this.getScrollWidth(true)+"x"+this.getScrollHeight(true)+", border: "+this.getVBorderSize()+"x"+this.getHBorderSize()+", margin: "+this.getVMarginSize()+"x"+this.getHMarginSize()+(_2==null?"":", old size: "+_2+"x"+_3)+", reason: "+_1,"sizing")}
if(this.logIsDebugEnabled(this.$th)){if(this.useClipDiv){this.logDebug("clipHandle sizes: "+this.echoElementSize(this.getClipHandle()),"sizing")}
this.logDebug("handle sizes: "+this.echoElementSize(this.getHandle()),"sizing")}
if(this.overflow==_5.IGNORE){}else if(this.overflow==_5.VISIBLE){if(this.$yf){if(this.getWidth()<this.getVisibleWidth()||this.getHeight()<this.getVisibleHeight())
{this.$vg(null,null,this.width,this.$s9)}
delete this.$yf}
var _6=this.getScrollWidth(true),_7=this.getScrollHeight(true);if(this.$mi){var _8=this.getScrollHandle();if(_8.scrollTop!=0||_8.scrollLeft!=0){_8.scrollTop=_8.scrollLeft=0}}
var _9=this.getInnerWidth(),_10=this.getInnerHeight();var _11=this.$zw=(_6>_9||_7>_10);if(!_11&&!_4)
{this.$zu=_6;this.$zv=_7;return}
var _12=this.getHMarginBorder(),_13=this.getVMarginBorder();this.$vg(this.left,this.top,Math.max((_6+_12),this.getWidth()),Math.max((_7+_13),this.getHeight()));var _14=this.children&&this.children.length>0;if(!_14||this.allowContentAndChildren){var _15=this.getScrollHeight(true),_16=this.getScrollWidth(true);if(_15!=_7||_16!=_6){_6=_16;_7=_15;this.$vg(this.left,this.top,Math.max((_6+_12),this.getWidth()),Math.max((_7+_13),this.getHeight()))}}
if(this.snapTo!=null&&_11&&(_1==this.$sf||_1==this.$r0))
{this.$vb(true)}
this.$zu=_6;this.$zv=_7;if((_2!=null&&_2!=_6)||(_3!=null&&_3!=_7))
{if(!_11&&_1==this.$ox)return;this.$ym(_6-_2,_7-_3,this.$ti)}}else if(this.overflow==_5.HIDDEN){this.$vg(this.left,this.top,this.getWidth(),this.getHeight());if(isc.Browser.isIE&&this.isRTL()){this.scrollLeft=this.getClipHandle().scrollLeft}
if(this.scrollLeft!=0||this.scrollTop!=0)this.$zx()}else if(this.overflow==_5.CLIP_H){var _7=this.getScrollHeight(),_13=this.getVMarginBorder(),_17=Math.max(_7+_13,this.getHeight());this.$zv=_17;this.setClip(0,this.getWidth(),_17,0);this.$vg(this.left,this.top,this.getWidth(),_17)}else if(this.overflow==_5.CLIP_V){var _6=this.getScrollWidth(),_12=this.getHMarginBorder();if((isc.Browser.isIE||isc.Browser.isMoz||isc.Browser.isOpera)&&(_6>this.getInnerWidth())&&(this.$zu==_6)){this.$vg(this.left,this.top,this.getWidth(),this.getHeight());_6=this.getScrollWidth(true)
if(_6>this.getInnerWidth()){this.$vg(this.left,this.top,_6+_12,this.getHeight())}}else{this.$vg(this.left,this.top,Math.max(_6+_12,this.getWidth()),this.getHeight())}
var _18=Math.max(_6+_12,this.getWidth());this.setClip(0,_18,this.getHeight(),0);this.$zu=_18}else{if(isc.Browser.isIE&&this.showCustomScrollbars&&this.getScrollingMechanism()==isc.Canvas.NATIVE)
{var _19=this.scrollLeft,_20=this.scrollTop;if(this.getScrollLeft()!=_19||this.getScrollTop()!=_20){this.$nd()}}
var _21=this.vscrollOn,_22=this.hscrollOn,_23=this.$mb();var _24=(this.alwaysShowVScrollbar&&this.showCustomScrollbars);if(this.overflow==isc.Canvas.SCROLL){this.hscrollOn=this.vscrollOn=true}else{var _7=this.getScrollHeight(),_25=this.getHeight(),_6=this.getScrollWidth(),_26=this.getWidth(),_27=this.getScrollbarSize(),_28;var _13=this.getVMarginBorder(),_12=this.getHMarginBorder();if(!this.showCustomScrollbars&&this.getHandle().clientHeight!=null){this.hscrollOn=(this.getClipHandle().clientHeight<_25-_13);this.vscrollOn=_24||(this.getClipHandle().clientWidth<_26-_12)}else{this.vscrollOn=_24||((_7-(_25-_13))>0);this.hscrollOn=(_6-(_26-_12))>0}
if((this.vscrollOn&&!_21&&!this.hscrollOn)||(this.hscrollOn&&!_22&&!this.vscrollOn))
{if(this.showCustomScrollbars){this.$vg(this.left,this.top,this.getWidth(),this.getHeight())}
_28=(this.vscrollOn?"V":"")+(this.hscrollOn?"H":"");this.innerSizeChanged("introducing scrolling");var _16=this.getScrollWidth(true),_15=this.getScrollHeight(true);if(this.logIsDebugEnabled("scrolling")){this.logDebug("Rechecking scrollWidth/Height on introduction of scroll:"+" old: "+[_6,_7]+", new: "+[_16,_15],"scrolling")}
_6=_16;_7=_15}
if(this.vscrollOn&&!this.hscrollOn){if(this.showCustomScrollbars||(this.getClipHandle().clientHeight==null))
this.hscrollOn=_6-(_26-_12-_27)>0;else
this.hscrollOn=(_25>this.getClipHandle().clientHeight+this.getVBorderSize())}else if(this.hscrollOn){if(this.showCustomScrollbars||(this.getClipHandle().clientWidth==null))
this.vscrollOn=_24||(_7-(_25-_13-_27)>0);else
this.vscrollOn=_24||(_26>this.getClipHandle().clientWidth+this.getHBorderSize())}}
if(this.logIsInfoEnabled("scrolling")){this.logInfo("Drawn size: "+this.getScrollWidth(true)+" by "+this.getScrollHeight(true)+", specified: "+this.getWidth()+" by "+this.getHeight()+", scrollbar state: "+(this.hscrollOn?"h":"")+(this.vscrollOn?"v":""),"scrolling")}
if(this.showCustomScrollbars&&(this.hscrollOn!=_22||this.vscrollOn!=_21))
{this.$vg(this.left,this.top,this.getWidth(),this.getHeight());if(this.$xg!=null)delete this.$xg;if(this.$xk!=null)delete this.$xk}
var _29=((_21?"V":"")+(_22?"H":"")),_30=((this.vscrollOn?"V":"")+(this.hscrollOn?"H":""));if(_29!=_30){this.logInfo("Scrollbar state: "+_29+" -> "+_30,"scrolling");if(_28==null||_30!=_28)
{this.innerSizeChanged("scrolling state changed")}}
if(this.isRTL()&&this.hscrollOn&&!_22){var _31=this.getClipHandle().scrollLeft;this.scrollLeft=_31}
if(this.showCustomScrollbars){if(!this.hscrollOn&&_22)this.hscrollbar.hide();if(!this.vscrollOn&&_21)this.vscrollbar.hide();if(this.hscrollOn){this.$zy()}else{if(_22)this.scrollTo(0,null,"ending hscroll")}
if(this.vscrollOn){this.$zz()}else{if(_21)this.scrollTo(null,0,"ending vscroll")}
this.$zx()}
if((this._useNativeTabIndex||this._useFocusProxy)&&_23!=this.$mb())
{this.$zn()}}
return true}
,isc.A.$zx=function isc_Canvas__clampToContent(){if(this.scrollLeft==0&&this.scrollTop==0)return;var _1=Math.max(0,this.getScrollBottom()),_2=Math.max(0,this.getScrollRight()),_3=this.getScrollLeft(),_4=this.getScrollTop(),_5=false;if(_3>_2){_5=true;_3=_2}
if(_4>_1){_5=true;_4=_1}
if(_5){this.scrollTo(_3,_4,"clampToContent")}}
,isc.A.checkNativeScroll=function isc_Canvas_checkNativeScroll(){var _1=this.getScrollHandle();if(this.getScrollingMechanism()!=isc.Canvas.NATIVE||_1==null)return;if(_1.scrollLeft!=this.scrollLeft||_1.scrollTop!=this.scrollTop){this.scrollTo(this.scrollLeft,this.scrollTop,"removing native scroll")}}
,isc.A.$zy=function isc_Canvas__setHorizontalScrollbar(){var _1=this.hscrollbar;if(!_1){_1=this.hscrollbar=isc.ClassFactory.newInstance(this.scrollbarConstructor,{ID:this.getID()+"_hscroll",autoDraw:false,_generated:true,zIndex:this.getZIndex()+1,vertical:false,scrollTarget:this,visibility:this.visibility,$k7:false,$k6:false,_redrawWithParent:false,$z0:false})}
if(!isc.Page.isLoaded()){var _2=this;isc.Page.setEvent("load",function(){if(!_2.destroyed)_2.$zy()});return}
if(!this.hscrollOn)return;_1.setRect(this.getOffsetLeft()+this.getLeftMargin()+(this.vscrollOn&&this.isRTL()?this.getCustomScrollbarSize():0),this.getOffsetTop()+this.getHeight()-
(this.getBottomMargin()+this.getCustomScrollbarSize()),this.getOuterViewportWidth(),this.getCustomScrollbarSize());if(!_1.masterElement){this.addPeer(_1)}else{if(this.visibility!=isc.Canvas.HIDDEN)_1.show()}}
,isc.A.getCustomScrollbarSize=function isc_Canvas_getCustomScrollbarSize(){var _1=this.scrollbarConstructor;if(isc.isA.String(_1))_1=isc[_1];if(isc.NativeScrollbar!=null&&_1==isc.NativeScrollbar)return isc.NativeScrollbar.getScrollbarSize();return this.scrollbarSize}
,isc.A.$zz=function isc_Canvas__setVerticalScrollbar(){var _1=this.vscrollbar
if(!_1){_1=this.vscrollbar=isc.ClassFactory.newInstance(this.scrollbarConstructor,{ID:this.getID()+"_vscroll",autoDraw:false,_generated:true,zIndex:this.getZIndex()+1,vertical:true,scrollTarget:this,visibility:this.visibility,$k7:false,$k6:false,_redrawWithParent:false,$z0:false})}
if(!isc.Page.isLoaded()){var _2=this;isc.Page.setEvent("load",function(){if(!_2.destroyed)_2.$zz()});return}
if(!this.vscrollOn)return;_1.setShowCorner(this.hscrollOn&&this.vscrollOn);_1.setRect(this.getOffsetLeft()+(this.isRTL()?this.getLeftMargin():this.getWidth()-(this.getRightMargin()+this.getScrollbarSize())),this.getOffsetTop()+this.getTopMargin(),this.getScrollbarSize(),this.getHeight()-this.getVMarginSize());if(!_1.masterElement){this.addPeer(_1)}else{if(this.visibility!=isc.Canvas.HIDDEN)_1.show()}}
,isc.A.scrollByPage=function isc_Canvas_scrollByPage(_1,_2,_3){var _4=(_1?this.getViewportHeight():this.getViewportWidth())-
this.scrollDelta;this.$z1(_1,_2*_4,_3||"scrollByPage")}
,isc.A.scrollByDelta=function isc_Canvas_scrollByDelta(_1,_2,_3){this.$z1(_1,_2*this.scrollDelta,_3||"scrollByDelta")}
,isc.A.$z1=function isc_Canvas__scrollByAmount(_1,_2,_3){if(_1){this.scrollTo(null,this.getScrollTop()+_2,_3)}else{this.scrollTo(this.getScrollLeft()+_2,_3)}}
,isc.A.canScroll=function isc_Canvas_canScroll(_1){var _2=_1?this.getScrollHeight():this.getScrollWidth(),_3=_1?this.getViewportHeight():this.getViewportWidth();return(_2>_3)}
,isc.A.getScrollRatio=function isc_Canvas_getScrollRatio(_1){var _2=_1?this.getScrollHeight():this.getScrollWidth(),_3=_1?this.getViewportHeight():this.getViewportWidth(),_4=_1?this.getScrollTop():this.getScrollLeft(),_5=_2-_3;if(_5==0)return 0;return _4/ _5}
,isc.A.scrollToRatio=function isc_Canvas_scrollToRatio(_1,_2,_3){var _4=Math.max(0,(_1?this.getScrollBottom():this.getScrollRight())),_5=Math.round(_4*_2),_3=_3||"scrollToRatio";if(_1){this.scrollTo(null,_5,_3)}else{this.scrollTo(_5,null,_3)}}
,isc.A.getViewportRatio=function isc_Canvas_getViewportRatio(_1){if(_1){return this.getViewportHeight()/this.getScrollHeight()}else{return this.getViewportWidth()/this.getScrollWidth()}}
,isc.A.getScrollBottom=function isc_Canvas_getScrollBottom(){if(this.overflow==isc.Canvas.VISIBLE)return 0;return this.getScrollHeight()-this.getViewportHeight()}
,isc.A.getScrollRight=function isc_Canvas_getScrollRight(){if(this.overflow==isc.Canvas.VISIBLE)return 0;return this.getScrollWidth()-this.getViewportWidth()}
,isc.A.scrollToTop=function isc_Canvas_scrollToTop(){this.scrollTo(null,0,"scrollToTop")}
,isc.A.scrollToBottom=function isc_Canvas_scrollToBottom(){this.scrollTo(null,this.getScrollBottom(),"scrollToBottom")}
,isc.A.scrollToLeft=function isc_Canvas_scrollToLeft(){this.scrollTo(0,null,"scrollToLeft")}
,isc.A.scrollToRight=function isc_Canvas_scrollToRight(){this.scrollTo(this.getScrollRight(),null,"scrollToRight")}
,isc.A.scrollBy=function isc_Canvas_scrollBy(_1,_2,_3){var _4,_5;if(_1!=null)_4=this.getScrollLeft()+_1;if(_2!=null)_5=this.getScrollTop()+_2;return this.scrollTo(_4,_5,_3||"scrollBy")}
,isc.A.scrollByPercent=function isc_Canvas_scrollByPercent(_1,_2){if(isc.isA.String(_1))_1=parseInt(_1);if(isc.isA.String(_2))_2=parseInt(_2);if(!isc.isA.Number(_1))_1=0;else
_1=parseInt(_1/ 100*Math.max(0,(this.getScrollWidth()-this.getViewportWidth())));if(!isc.isA.Number(_2))_2=0;else
_2=parseInt(_2/ 100*Math.max(0,(this.getScrollHeight()-this.getViewportHeight())));this.scrollBy(_1,_2)}
,isc.A.scrollTo=function(left,top,reason,animating){if(isc.$dd)arguments.$de=this;if(!animating){if(this.scrollAnimation)this.finishAnimation("scroll");if(this.hideAnimation&&this.$hideAnimationInfo.slideOut)
this.$hideAnimationInfo.slideOut=false;if(this.showAnimation&&this.$showAnimationInfo.slideIn)
this.$showAnimationInfo.slideIn=false}
if(this.logIsDebugEnabled("scrolling")){this.logDebug("scrollTo("+left+", "+top+"), reason: "+reason,"scrolling")}
if(!isc.isA.Number(left))left=this.getScrollLeft();if(!isc.isA.Number(top))top=this.getScrollTop();var actuallyMoved=false;if((left!=null&&left!=this.scrollLeft)||(top!=null&&top!=this.scrollTop)){actuallyMoved=true;this.lastScrollLeft=this.scrollLeft;this.lastScrollTop=this.scrollTop;this.lastScrollDirection=(left!=null&&left!=this.scrollLeft&&top!=null&&top!=this.scrollTop?"both":top!=null&&top!=this.scrollTop?"vertical":"horizontal")}
if(reason=="nativeScroll"||!this.isDrawn()){this.scrollLeft=left;this.scrollTop=top}else{var maxScrollLeft=this.getScrollRight();this.scrollLeft=Math.max(0,Math.min(maxScrollLeft,left));var maxScrollTop=this.getScrollBottom();this.scrollTop=Math.max(0,Math.min(maxScrollTop,top));this.$z2(this.scrollLeft,this.scrollTop)}
if(this.showCustomScrollbars){if(this.hscrollOn&&this.hscrollbar)this.hscrollbar.setThumb();if(this.vscrollOn&&this.vscrollbar)this.vscrollbar.setThumb()}
if(actuallyMoved)this.$z3()}
);isc.evalBoundary;isc.B.push(isc.A.scrolled=function isc_Canvas_scrolled(){}
,isc.A.$z3=function isc_Canvas__scrolled(){if(!isc.EH.$mo){var _1=isc.EH.lastEvent,_2=isc.EH.isMouseEvent(_1.eventType),_3=_2?_1.target:isc.EH.lastMoveTarget;if(_3!=null){if(!this.contains(_3,true))_3=null;else if(!_2&&_3!=this){var _4=this.getOffsetX(),_5=this.getOffsetY();if(!_3.visibleAtPoint(isc.EH.getX(),isc.EH.getY(),false,null,this))
{_3=null}}
if(_3!=null){isc.EH.$mn(null,isc.EH.lastEvent)}}}
this.$yq();if(this.scrolled)this.scrolled()}
,isc.A.$yq=function isc_Canvas__childrenCoordsChanged(){if(!isc.Element.cacheOffsetCoords)return;var _1=this.children;if(_1!=null&&_1.length>0){for(var i=0;i<_1.length;i++){_1[i].$ps=_1[i].$pt=null;_1[i].$yq()}}}
,isc.A.scrollToPercent=function isc_Canvas_scrollToPercent(_1,_2,_3){if(isc.isA.String(_1))_1=parseInt(_1);if(isc.isA.String(_2))_2=parseInt(_2);if(!isc.isA.Number(_1))_1=0;if(!isc.isA.Number(_2))_2=0;_1=parseInt(_1/ 100*Math.max(0,(this.getScrollWidth()-this.getViewportWidth())));_2=parseInt(_2/ 100*Math.max(0,(this.getScrollHeight()-this.getViewportHeight())));this.scrollTo(_1,_2,_3||"scrollToPercent")}
,isc.A.$z2=function isc_Canvas__scrollHandle(_1,_2){var _3=this.getScrollingMechanism();if(_3==isc.Canvas.NATIVE){var _4=this.getScrollHandle();if(_4){this.$z4=true;_4.scrollLeft=_1;_4.scrollTop=_2;delete this.$z4;if(_4.scrollLeft!=this.scrollLeft||_4.scrollTop!=this.scrollTop){this.scrollLeft=_4.scrollLeft;this.scrollTop=_4.scrollTop}}}else if(_3==isc.Canvas.NESTED_DIV){var _4=this.getHandle();if(_4==null){this.logWarn(this.getCallTrace(arguments)+" in NS6 with null handle");return}
_4=_4.style;_4.left=-_1+"px";_4.top=-_2+"px"}}
,isc.A.$nd=function isc_Canvas__handleCSSScroll(_1,_2){isc.EH.$jp("SCR");if(isc.$dd)arguments.$de=this;if(this.$z4)return;if(isc.Browser.isMoz&&!_1&&(_2||isc.Browser.geckoVersion<20030312)){if(!this.$z5)
this.$z5=this.delayCall("$nd",[true],10);return}
this.$z5=null;if(!this.isDrawn())return;var _3=this.getScrollHandle(),_4=_3.scrollLeft,_5=_3.scrollTop;if(_4==this.scrollLeft&&_5==this.scrollTop)return;var _6=this.getScrollingMechanism();if(_6!=isc.Canvas.NATIVE){this.logWarn("unsupported native scroll occurred on this widget - resetting");if(_6==isc.Canvas.NESTED_DIV){_3.scrollLeft=_3.scrollTop=0}else{_3.scrollLeft=this.scrollLeft;_3.scrollTop=this.scrollTop}
return}
this.scrollTo(_4,_5,"nativeScroll");isc.EH.$jq()}
,isc.A.mouseWheel=function isc_Canvas_mouseWheel(){if((this.overflow==isc.Canvas.AUTO||this.overflow==isc.Canvas.SCROLL)&&this.showCustomScrollbars&&this.vscrollOn)
{var _1=this.ns.EH.lastEvent.wheelDelta;var _2=this.scrollTop+Math.round(_1*isc.Canvas.scrollWheelDelta);this.scrollTo(this.getScrollLeft(),_2,"mouseWheel");return false}
return true}
,isc.A.isDragScrolling=function isc_Canvas_isDragScrolling(){if(this.vscrollOn&&this.vscrollbar&&this.vscrollbar.isDragScrolling())return true;if(this.hscrollOn&&this.hscrollbar&&this.hscrollbar.isDragScrolling())return true;return false}
,isc.A.isRepeatTrackScrolling=function isc_Canvas_isRepeatTrackScrolling(){if(this.vscrollOn&&this.vscrollbar&&this.vscrollbar.isRepeatTrackScrolling())return true;if(this.hscrollOn&&this.hscrollbar&&this.hscrollbar.isRepeatTrackScrolling())return true;return false}
,isc.A.handleKeyPress=function isc_Canvas_handleKeyPress(_1,_2){var _3;if(this.convertToMethod("keyPress")){_3=this.keyPress(_1,_2)}
if(_3!=false&&this.shouldCancelKey!=null&&this.shouldCancelKey(_1,_2))
{_3=false}
if(_3==false)return false;var _4=_1.keyName;if(this._useFocusProxy&&((isc.Browser.isMoz&&this.canSelectText)||isc.Browser.isSafari)&&_4=="Tab")
{this.setFocus(true)}
if((this.overflow==isc.Canvas.AUTO||this.overflow==isc.Canvas.SCROLL)&&this.showCustomScrollbars)
{_3=this.handleKeyboardScroll(_4)}
return _3}
,isc.A.handleKeyboardScroll=function isc_Canvas_handleKeyboardScroll(_1){var _2=0,_3=0;if(_1=="Page_Up")_3-=this.getViewportHeight();else if(_1=="Page_Down")_3+=this.getViewportHeight();else if(_1=="Arrow_Up")_3-=10;else if(_1=="Arrow_Down")_3+=10;else if(_1=="Arrow_Left")_2-=10;else if(_1=="Arrow_Right")_2+=10;var _4="cancel native keyPress scrolling";if(_2!=0||_3!=0){this.scrollTo(this.scrollLeft+_2,this.scrollTop+_3,_4);return false}
if(_1=="Home"){this.scrollTo(null,0,_4);return false}else if(_1=="End"){this.scrollTo(null,(this.getScrollHeight()-this.getViewportHeight()),_4);return false}}
,isc.A.handleKeyDown=function isc_Canvas_handleKeyDown(_1,_2){var _3
if(this.convertToMethod("keyDown")){_3=this.keyDown(_1,_2)}
if(this.cancelNativeScrollOnKeyDown&&(this.overflow==isc.Canvas.AUTO||this.overflow==isc.Canvas.SCROLL)&&this.showCustomScrollbars)
{var _4=isc.EH.getKey();if(this.$tj[_4]==true)_3=false}
return _3}
,isc.A.$vg=function isc_Canvas__setHandleRect(_1,_2,_3,_4){if(this.showCustomScrollbars&&this.vscrollOn&&_1!=null&&this.isRTL()){_1+=this.getScrollbarSize()}
if(_3!=null||_4!=null){var _5=this.$w2(_3,_4);_3=_5[0];_4=_5[1]}
var _6=this.getStyleHandle();if(_6){if(_1!=null&&isc.isA.Number(_1))this.$z6(_6,isc.Canvas.LEFT,_1);if(_2!=null&&isc.isA.Number(_2))this.$z6(_6,isc.Canvas.TOP,_2);if(_3!=null&&isc.isA.Number(_3))this.$z6(_6,this.$s7,Math.max(_3,1));if(_4!=null&&isc.isA.Number(_4))this.$z6(_6,this.$s6,Math.max(_4,1))}}
,isc.A.$z6=function isc_Canvas__assignSize(_1,_2,_3){if(isc.Browser.isIE||isc.Browser.isOpera){if(!isc.Browser.isStrict){_1[_2]=_3}else{if(_3<0&&(_2==this.$s7||_2==this.$s6))_3=0;_1[_2]=_3+this.$tk}}else{if(_1==null){return}
_1[_2]=_3+this.$tk}}
,isc.A.$uu=function isc_Canvas__sizeBackMask(){var _1=this._backMask;if(!_1)return;if(this.showEdges){var _2=this.$nw,_3=this.maskEdgeCenterOnly,_4=_3?_2.$z7:_2.$xw,_5=_3?_2.$z8:_2.$xx,_6=_3?_2.$z9:_2.$xy,_7=_3?_2.$0a:_2.$xz,_8=this.getVisibleWidth()-(_4+_5),_9=this.getVisibleHeight()-(_6+_7);if(_8<=0||_9<=0)_1.hide();else{if(this.isVisible())_1.show();_1.setRect(this.getLeft()+_4,this.getTop()+_6,_8,_9)}}else{_1.setRect(this.getRect())}}
,isc.A.getTextDirection=function isc_Canvas_getTextDirection(){if(this.$0b)return this.$0b;var _1=this;while(_1){if(_1.textDirection!=null){return(this.$0b=_1.textDirection)}
_1=_1.parentElement;if(_1&&_1.eventProxy)_1=_1.eventProxy}
return(this.$0b=isc.Page.getTextDirection())}
,isc.A.isRTL=function isc_Canvas_isRTL(){return(this.getTextDirection()==isc.Canvas.RTL)}
,isc.A.getRTLSign=function isc_Canvas_getRTLSign(){return this.isRTL()?-1:1}
,isc.A.setVisibility=function isc_Canvas_setVisibility(_1){if(this.$zj!=null&&!this.$zk)
this.finishAnimation(this.$zj);if(this.$zl!=null&&!this.$zm)
this.finishAnimation(this.$zl);if(this.fadeAnimation)this.finishAnimation("fade");if(!isc.isA.String(_1)){_1=(_1!=false?isc.Canvas.INHERIT:isc.Canvas.HIDDEN)}
if(this.visibility==_1)return;var _2=this.isVisible();this.visibility=_1;if(this.isDrawn()){if(!_2&&this.isVisible()){if(this.isDirty()){this.redraw("show() while dirty")}else if(this.children&&this.children.length>0){var _3=isc.Canvas.$rl.duplicate();for(var i=0;i<_3.length;i++){var _5=_3[i];if(_5&&_5.isDirty()&&this.$xd(_5)){_5.redraw("show() on parent while dirty")}}}
this.$0c()}
this.$0d(_1)}
if(_2&&!this.isVisible()){this.$0c()}
if(this.peers){for(var i=0;i<this.peers.length;i++){var _6=this.peers[i];if(this.isVisible()&&((_6==this.hscrollbar&&!this.hscrollOn)||(_6==this.vscrollbar&&!this.vscrollOn)))continue;if(this.isVisible()&&_6==this._shadow&&!this.showShadow)continue;if(_6.$k8)_6.setVisibility(_1)}}
if(this.children)this.children.map("parentVisibilityChanged",_1,this);if(this.parentElement)this.parentElement.childVisibilityChanged(this,_1);if(this._useFocusProxy)this.$0e();this.$0f()}
,isc.A.$0f=function isc_Canvas__visibilityChanged(){if(!this.isDrawn())return;var _1=this.isVisible();if(_1!=this.$uo){this.$uo=_1;if(this.visibilityChanged!=null){this.visibilityChanged(this.isVisible())}}}
,isc.A.parentVisibilityChanged=function isc_Canvas_parentVisibilityChanged(_1,_2){if(this.children)this.children.map("parentVisibilityChanged",_1,_2);this.$0c();if(this==isc.Canvas.$v1)isc.Canvas.hideResizeThumbs();if(this._useFocusProxy)this.$0e();if(_2.$xd(this))this.$0f()}
,isc.A.childVisibilityChanged=function isc_Canvas_childVisibilityChanged(_1,_2){this.$yg("childVisChange")}
,isc.A.childCleared=function isc_Canvas_childCleared(_1){if(!this.destroying)this.$yg("childClear")}
,isc.A.peerCleared=function isc_Canvas_peerCleared(_1){}
,isc.A.childDrawn=function isc_Canvas_childDrawn(_1){if(this.isDrawn())this.$yg("childDraw")}
,isc.A.peerDrawn=function isc_Canvas_peerDrawn(_1){}
,isc.A.$0e=function isc_Canvas__updateFocusProxyVisibility(){if(!this._useFocusProxy||!this.$uv)return;var _1=this.isVisible(),_2=this.$w7();if(_2){if(_1&&_2.style.visibility==isc.Canvas.HIDDEN)
_2.style.visibility=isc.Canvas.VISIBLE
if(!_1&&_2.style.visibility!=isc.Canvas.HIDDEN)
_2.style.visibility=isc.Canvas.HIDDEN}}
,isc.A.$0d=function isc_Canvas__setHandleVisibility(_1){var _2=this.getStyleHandle();if(_2!=null)_2.visibility=_1}
,isc.A.$0c=function isc_Canvas__updateHandleDisplay(){if(!this.hideUsingDisplayNone||!this.isDrawn())return;var _1=this.getStyleHandle();if(!this.isVisible()&&!this.$0g){this.$0h=_1.display;this.$0g=true;_1.display=this.$pr;this.$ps=this.$pt=null}else if(this.isVisible()&&this.$0g){_1.display=(this.$0h?this.$0h:isc.emptyString);delete this.$0g}}
,isc.A.$0i=function isc_Canvas__drawOnShow(){return(this.getDrawnState()==isc.Canvas.UNDRAWN)&&!this.parentElement&&!this.masterElement}
,isc.A.show=function isc_Canvas_show(){if(isc.$dd)arguments.$de=this;var _1=this.hasFocus;if(this.$0i()){this.draw(true)}
this.setVisibility(isc.Canvas.INHERIT);if(_1&&this.hasFocus){this.logInfo("Show: Hidden / Undrawn widget marked as having focus - calling focus()","events");this.hasFocus=false;this.focus()}
if(this.autoShowParent&&this.parentElement)this.parentElement.show()}
,isc.A.showRecursively=function isc_Canvas_showRecursively(){var _1=this.getParentElements();if(this.$0j==null&&_1.isEmpty()){this.show()}else{this.setVisibility(isc.Canvas.INHERIT);if(this.$0j!=null){_1.add(window[this.$0j])}
for(var i=0;i<_1.length;i++){_1[i].showRecursively();if(isc.TabSet!=null&&isc.isA.TabSet(_1[i])){_1[i].selectTab(_1[i].tabForPane(this))}else if(isc.SectionStack!=null&&isc.isA.SectionStack(_1[i])){_1[i].expandSection(_1[i].sectionForItem(this))}}}}
,isc.A.$ur=function isc_Canvas__relativePageResized(){if(!this.isDrawn()||this.parentElement||this.position!=this.$tl)return;var _1=this.$up,_2=this.$uq,_3=this.getPageLeft(),_4=this.getPageTop();this.$x6=(_3-_1);this.$x7=(_4-_2);this.$yb();this.$up=_3;this.$uq=_4;isc.Page.setEvent("resize",this,isc.Page.FIRE_ONCE,"$ur")}
,isc.A.hide=function isc_Canvas_hide(){this.$v0();this.setVisibility(isc.Canvas.HIDDEN)}
,isc.A.isVisible=function isc_Canvas_isVisible(){var _1=this;while(_1){if(_1.visibility==isc.Canvas.HIDDEN)return false;if(_1.visibility==isc.Canvas.VISIBLE)return true;_1=_1.parentElement}
return true}
,isc.A.$xf=function isc_Canvas__isDisplayNone(){var _1=this;while(_1){if(_1.visibility==isc.Canvas.HIDDEN&&_1.hideUsingDisplayNone)return true;_1=_1.parentElement}
return false}
,isc.A.setEnabled=function isc_Canvas_setEnabled(_1){this.logWarn("call to deprecated method 'setEnabled()' - use 'setDisabled()' instead.");var _2=((_1==null||isc.isA.Boolean(_1))?!_1:(_1==this.$tm));this.setDisabled(_2)}
,isc.A.setDisabled=function isc_Canvas_setDisabled(_1){if(_1==null)_1=false;if(!isc.isA.Boolean(_1))_1=(_1==this.$tm);if(this.disabled==_1)return;if(this.peers)this.peers.map("masterDisabled",_1);var _2=this.isDisabled()
this.disabled=_1;var _3=this.isDisabled();if(_2!=_3){this.setHandleDisabled(_3);if(this.children)this.children.map("parentDisabled",_3)}}
,isc.A.masterDisabled=function isc_Canvas_masterDisabled(_1){this.setDisabled(_1)}
,isc.A.parentDisabled=function isc_Canvas_parentDisabled(_1){if(this.disabled)return;if(!this.parentElement.redrawOnDisable)this.setHandleDisabled(_1);if(this.children)this.children.map("parentDisabled",_1)}
,isc.A.setHandleDisabled=function isc_Canvas_setHandleDisabled(_1){if(!this.isDrawn())return;if(this.redrawOnDisable)this.markForRedraw("setDisabled");if(this.$mb())this.disableKeyboardEvents(_1)}
,isc.A.disableKeyboardEvents=function isc_Canvas_disableKeyboardEvents(_1,_2){if(_1){this.$0k(-1);if(this.accessKey!=null)this.$pa(null)}else{this.$0k(this.getTabIndex());if(this.accessKey!=null)this.$pa(this.accessKey)}
if(_1&&this.hasFocus)this.blur();if(_2&&this.children){for(var i=0;i<this.children.length;i++){this.children[i].disableKeyboardEvents(_1,true)}}}
,isc.A.enable=function isc_Canvas_enable(){if(this.disabled)this.setDisabled(false)}
,isc.A.disable=function isc_Canvas_disable(){if(!this.disabled)this.setDisabled(true)}
,isc.A.isDisabled=function isc_Canvas_isDisabled(){var _1=this;while(_1){if(_1.disabled)return true;_1=_1.parentElement;if(_1&&_1.eventProxy)_1=_1.eventProxy}
return false}
,isc.A.isEnabled=function isc_Canvas_isEnabled(){this.logWarn("Call to deprecated 'isEnabled()' method - should use isDisabled() instead");return!this.isDisabled()}
,isc.A.$mb=function isc_Canvas__canFocus(){if(this.canFocus!=null)return this.canFocus;if((this.overflow==isc.Canvas.SCROLL)||((this.overflow==isc.Canvas.AUTO)&&(this.vscrollOn||this.hscrollOn))){return true}
return false}
,isc.A.setCanFocus=function isc_Canvas_setCanFocus(_1){this.canFocus=_1;this.$zn()}
,isc.A.$zn=function isc_Canvas__updateCanFocus(){this.$0l(this.$mb());this.canFocusChanged()}
,isc.A.$0l=function isc_Canvas__updateHandleForFocus(_1){var _2;if(this._useFocusProxy){if(_1){_2=this.$w7();if(!_2)return this.makeFocusProxy()}else{this.$u1();return}
if(isc.Browser.isSafari&&this.getTabIndex()==-1){this.$u1();return}}
if(this.$uj()){if(_1&&this.accessKey){this.$uk()}else if(this.$u4){this.$u5()}}
if(this._useNativeTabIndex)_2=this.getFocusHandle();if(_1){this.$0m(this.getTabIndex(),this.$xb);if(_2!=null){var _3=this.$w3(),_4=this.$w5();_2.onfocus=_3;_2.onblur=_4;if(this.accessKey)this.$pa(this.accessKey)}}else{if(_2!=null){_2.onFocus=null;_2.onBlur=null;this.$0k(-1);if(_2.accessKey!=null)this.$pa(null)}}}
,isc.A.canFocusChanged=function isc_Canvas_canFocusChanged(){var _1=this.parentElement;while(_1){_1.childCanFocusChanged(this);_1=_1.parentElement}}
,isc.A.childCanFocusChanged=function isc_Canvas_childCanFocusChanged(_1){}
,isc.A.setShowFocusOutline=function isc_Canvas_setShowFocusOutline(_1,_2){if(!_2&&this.showFocusOutline==_1)return;if(!_2)this.showFocusOutline=_1;if(isc.Browser.isMoz){var _3=this.getClipHandle();if(_3){_3.style.MozOutlineStyle=(_1?isc.emptyString:this.$pr)}}else{var _3=this.getHandle();if(_3)_3.hideFocus=!_1}}
,isc.A.$0n=function isc_Canvas__readyToSetFocus(_1){return(this.isDrawn()&&this.visibleInDOM()&&(!_1||!this.isDisabled()))}
,isc.A.visibleInDOM=function isc_Canvas_visibleInDOM(){if(!this.isVisible())return false;var _1=this;while(_1.parentElement)_1=_1.parentElement;if(_1.position==isc.Canvas.ABSOLUTE)return true;var _2=this.getDocumentBody();var _3=_1.getClipHandle().parentNode;while(_3&&_3!=_2){var _4=_3.style;if(_4&&_4.visibility==this.$r1)return false;if(_4&&_4.display==this.$pr)return false;_3=_3.parentNode}
return true}
,isc.A.getFocusHandle=function isc_Canvas_getFocusHandle(){if(this._useNativeTabIndex){return this.getClipHandle()}else if(this._useFocusProxy&&this.$uv){return this.$w7()}
return null}
,isc.A.setFocus=function isc_Canvas_setFocus(_1,_2){if(!this.$0n(_1))return;var _3=this.getFocusHandle(_1);if(_1&&this.$mb()){if(_3!=null){this.logInfo("about to call native focus()"+(this.logIsDebugEnabled("traceFocus")?this.getStackTrace():""),"nativeFocus");isc.EH.$nc=this;_3.focus();isc.EH.$0o=this}else{this.ns.EH.focusInCanvas(this)}}else if(this.hasFocus){if(_3){this.logInfo("about to call native blur()"+(this.logIsDebugEnabled("traceBlur")?this.getStackTrace():""),"nativeFocus");isc.EH.$m9=this;_3.blur()}else{this.ns.EH.blurFocusCanvas(this)}}}
,isc.A.$0p=function isc_Canvas__restoreFocus(){var _1=isc.EH.$l4;if(_1!=null&&_1!=this){this.logDebug("not restoring focus; focus moved to: "+_1,"nativeFocus");return}
var _2=isc.EH.$nc;if(_2!=null&&_2!=this){this.logDebug("not restoring focus; focus about to move to:"+_2,"nativeFocus");return}
this.logDebug("restoring focus from zIndex change","nativeFocus");this.$0q(true)}
,isc.A.focus=function isc_Canvas_focus(_1){if(isc.$dd)arguments.$de=this;this.setFocus(true,_1)}
,isc.A.blur=function isc_Canvas_blur(_1){if(isc.$dd)arguments.$de=this;this.setFocus(false,_1)}
,isc.A.focusAtEnd=function isc_Canvas_focusAtEnd(_1){return this.focus()}
,isc.A.$0q=function isc_Canvas__setFocusWithoutHandler(_1,_2){this.$0r=true;this.setFocus(_1,_2)}
,isc.A.$nb=function isc_Canvas__focusChanged(_1){if(_1==null)_1=(this.ns.EH.$l4==this);this.hasFocus=_1;if(this.$0r){delete this.$0r;return false}
this.$0s=true;if(this.focusChanged!=null){this.convertToMethod("focusChanged");this.focusChanged(_1)}
if(this.redrawOnFocus)this.markForRedraw("setFocus");this.$0s=false}
,isc.A.$v0=function isc_Canvas__updateFocusForHide(){var _1=this.ns.EH.getFocusCanvas();if(this.$xd(_1)){if(isc.isA.Canvas(_1.focusOnHide)&&_1.focusOnHide.isDrawn()&&_1.focusOnHide.isVisible()){_1.focusOnHide.focus()}
else{_1.blur();if(_1.hasFocus)isc.EH.blurFocusCanvas(_1)}}}
,isc.A.containsFocus=function isc_Canvas_containsFocus(){var _1=this.ns.EH.getFocusCanvas();return this.contains(_1,true)}
,isc.A.setAccessKey=function isc_Canvas_setAccessKey(_1){this.accessKey=_1;if(this.$mb()&&!this.isDisabled()){this.$pa(this.accessKey)}}
,isc.A.$pa=function isc_Canvas__setHandleAccessKey(_1){if(this.$uj()){if(_1==null)this.$u5();else{if(this.$u4)this.$u4.accessKey=_1;else this.$uk()}
return}
if(this._useNativeTabIndex){var _2=this.getHandle();if(_2!=null)_2.accessKey=_1}
if(this._useFocusProxy&&this.$uv){var _2=this.$w7();if(_2!=null){if(isc.Browser.isMoz){this.$u1();this.makeFocusProxy()}else{_2.accessKey=_1}}}}
,isc.A.getAccessKey=function isc_Canvas_getAccessKey(){return this.accessKey}
,isc.A.getTabIndex=function isc_Canvas_getTabIndex(){if(this.tabIndex==null){this.$0t()}
return this.tabIndex}
,isc.A.getTabIndexSpan=function isc_Canvas_getTabIndexSpan(){return 1}
,isc.A.setTabIndex=function isc_Canvas_setTabIndex(_1){var _2=isc.Canvas.TAB_INDEX_FLOOR;if(_1>=_2){var _3=_2-1;this.logWarn("setTabIndex(): Passed index of "+_1+". This method does not support setting a tab index greater than "+_3+".  Setting tab index for this widget to "+_3+this.getStackTrace());_1=_3}
this.$v7();this.$0m(_1,false)}
,isc.A.$0m=function isc_Canvas__setTabIndex(_1,_2){this.$xb=_2;this.tabIndex=_1;if(this.$mb()&&!this.isDisabled()){this.$0k(_1)}}
,isc.A.$0k=function isc_Canvas__setHandleTabIndex(_1){if(this._useNativeTabIndex&&this.isDrawn()){this.getClipHandle().tabIndex=_1;if(isc.Browser.isIE)isc.Canvas.$0u()}
if(this._useFocusProxy){if(!this.$uv)return this.makeFocusProxy();var _2=this.$w7();var _3=(this.hasFocus&&!this.$0s);if(_3&&_2)_2.blur();if(isc.Browser.isSafari&&_1<0)return this.$u1();if(_2!=null){_2.tabIndex=_1;if(isc.Browser.isMoz){_2.style.MozUserFocus=(_1<0?"ignore":"normal")}
if(_3)_2.focus()}}}
,isc.A.$0t=function isc_Canvas__autoAllocateTabIndex(){var _1=isc.Canvas;if(_1.$0v==null){_1.$0v=_1.TAB_INDEX_FLOOR}
var _2=isc.EH.$mc;if(_2)_1.$0v+=_2.getTabIndexSpan();_1.$0v+=_1.TAB_INDEX_GAP
if(_1.$0v>isc.Canvas.TAB_INDEX_CEILING&&!isc.Canvas.$0w)
{isc.Canvas.logWarn("Auto allocation of tab-indices has reached native browser ceiling "+"- tab-order cannot be guaranteed for widgets on this page.");isc.Canvas.$0w=true}
this.$0m(_1.$0v,true);if(_2){_2.$0x(this);this.$0y=_2}else{isc.EH.$ma=this}
isc.EH.$mc=this}
,isc.A.$0z=function isc_Canvas__setTabBefore(_1){if(this==_1||this.$00()==_1)return;var _2=_1.getTabIndex();if(!_1.$xb){this.logWarn("$0z() attempting to set tab index adjacent to widget "+_1+" with explicitly specified tabIndex ["+_1.tabIndex+"]. This method can only manipulate widgets with auto-assigned tab indexes.");return}
var _3=_1.$0y;var _4=this.$01(),_5=this.$00();if(isc.EH.$mc==this)isc.EH.$mc=_4;if(isc.EH.$ma==this)isc.EH.$ma=_5;if(_4!=null)
_4.$0x(_5);if(_5!=null)
_5.$02(_4);this.$02(null);this.$0x(null);this.$03(_1.$01(),_1);this.$04()}
,isc.A.$xc=function isc_Canvas__setTabAfter(_1){if(this==_1||this.$0y==_1)return;_1.getTabIndex();if(!_1.$xb){this.logWarn("$xc() attempting to set tab index adjacent to widget "+_1+" with explicitly specified tabIndex ["+_1.tabIndex+"]. This method can only manipulate widgets with auto-assigned tab indexes.");return}
var _2=_1,_3=this.$01(),_4=this.$00();if(isc.EH.$mc==this)isc.EH.$mc=_3;if(isc.EH.$ma==this)isc.EH.$ma=_4;if(_3!=null)
_3.$0x(_4);if(_4!=null)
_4.$02(_3);this.$02(null);this.$0x(null);this.$03(_1,_1.$00());this.$04()}
,isc.A.$03=function isc_Canvas__slotTabBetween(_1,_2){if(_2==null)return this.$0t();if(_1==null){var _3=_2.$00();_2.$v7();this.$0x(_3);this.$02(null);this.$0m(_2.tabIndex,true);isc.EH.$ma=this;_2.$03(this,_3);return}
this.$0x(_2);_2.$02(this);this.$02(_1);_1.$0x(this);var _4=_1.tabIndex+_1.getTabIndexSpan(),_5=_2.tabIndex,_6=_4+Math.floor((_5-_4)/2),_7=this.getTabIndexSpan();if((_6+_7)>_5){_2.$05((_6+_7)-_5)}
if(this.logIsDebugEnabled("tabIndex")){this.logDebug("Putting "+this.getID()+" in tab order between: "+_1.getID()+":"+_1.tabIndex+", and :"+_2.getID()+":"+_2.tabIndex+". Resulting tabIndex:"+_6,"tabIndex")}
this.$0m(_6,true)}
,isc.A.$05=function isc_Canvas__shiftTabIndexForward(_1){var _2=this.$00();if(_2==null){this.$0m(this.tabIndex+_1+isc.Canvas.TAB_INDEX_GAP,true);return}
var _3=_2.getTabIndex(),_4=_3-this.getTabIndexSpan();if(this.tabIndex+_1<_4)this.$0m(_4,true);else{_2.$05(_1-(_4-this.tabIndex));this.$0m(_2.tabIndex-this.getTabIndexSpan(),true)}}
,isc.A.$00=function isc_Canvas__getNextTabWidget(_1){if(!_1)return this.$06;else return this.$0y}
,isc.A.$01=function isc_Canvas__getPreviousTabWidget(){return this.$00(true)}
,isc.A.$0x=function isc_Canvas__setNextTabWidget(_1,_2){if(!_2)this.$06=_1;else this.$0y=_1}
,isc.A.$02=function isc_Canvas__setPreviousTabWidget(_1){return this.$0x(_1,true)}
,isc.A.$l5=function isc_Canvas__focusInNextTabElement(_1,_2){if(isc.CanvasItem&&this.canvasItem!=null&&this.canvasItem.form!=null){this.canvasItem.form.$l5(_1,_2);return}
var _3=this;do{_3=(_1?_3.$00():_3.$01())}while(_3&&(isc.EH.targetIsMasked(_3,_2)||_3.isDisabled()||!_3.isDrawn()||!_3.isVisible()||!_3.$mb())&&(!isc.CanvasItem||_3.canvasItem==null||_3.canvasItem.form==null))
if(_3){this.logInfo("focusInNextTabElement() shifting focus to:"+_3,"syntheticTabIndex");_3.focusAtEnd(_1)}else if(_1){this.logInfo("focusInNextTabElement() shifting focus to first widget","syntheticTabIndex");if(isc.EH.$ma==null||(isc.EH.$ma==this&&(this.isDisabled()||!this.isDrawn()||!this.isVisible()||!this.$mb()||this.isMasked(_2))))
{return}
isc.EH.$l7(_2)}else{this.logInfo("focusInNextTabElement() shifting focus to last widget","syntheticTabIndex");if(isc.EH.$mc==null||(isc.EH.$mc==this&&(this.isDisabled()||!this.isDrawn()||!this.isVisible()||!this.$mb()||this.isMasked(_2))))
{return}
isc.EH.$l6(_2)}}
,isc.A.$04=function isc_Canvas__slotChildrenIntoTabOrder(){var _1=isc.isA.Layout(this)?this.members:this.children;if(!_1||_1.length==0)return;var _2=this.$00();for(var i=_1.length-1;i>=0;i--){if(_1[i]==null||(_1[i].tabIndex!=null&&!_1[i].$xb))continue;if(_2==null)_1[i].$xc(this);else _1[i].$0z(_2);_2=_1[i]}}
,isc.A.$07=function isc_Canvas__getLastAutoIndexDescendant(){var _1=this.children;if(isc.Layout&&isc.isA.Layout(this))_1=this.members;if(_1!=null){for(var i=_1.length-1;i>=0;i--){if(_1[i]==null)continue;var _3=_1[i].$07();if(_3!=null)return _3}}
if(this.tabIndex==null||this.$xb)return this;return null}
,isc.A.$v7=function isc_Canvas__removeFromAutoTabOrder(){if(!this.$xb||!this.tabIndex)return;var _1=this.$01(),_2=this.$00();if(_1==null&&_2==null&&isc.EH.$mc!=this&&isc.EH.$ma!=this)return;if(_1){_1.$0x(_2)}else{isc.EH.$ma=_2}
if(_2){_2.$02(_1)}else{isc.EH.$mc=_1}
this.$02(null);this.$0x(null)}
,isc.A.getZIndex=function isc_Canvas_getZIndex(_1){if(!this.isDrawn()||isc.Browser.isSafari){if(_1&&this.zIndex==isc.Canvas.AUTO){this.setZIndex(isc.Canvas.getNextZIndex())}
return this.zIndex}
return parseInt(this.getStyleHandle().zIndex)}
,isc.A.setZIndex=function isc_Canvas_setZIndex(_1){var _2=this.zIndex;if(_2==_1)return;var _3=false;if(isc.Browser.isIE&&this.hasFocus&&this._useNativeTabIndex)
{_3=true;this.logDebug("blurring due to zIndex change","nativeFocus");this.$0q(false)}
if(_1<_2)this.$08(_1);this.zIndex=_1;if(this.isDrawn()){if(this.useClipDiv)this.getHandle().style.zIndex=_1
this.getStyleHandle().zIndex=_1}
if(_1>_2)this.$08(_1);if(this.hscrollbar)this.hscrollbar.moveAbove(this);if(this.vscrollbar)this.vscrollbar.moveAbove(this);if(this.clipCorners){var _4=this.$yn;if(_4.TL)_4.TL.moveAbove(this);if(_4.TR)_4.TR.moveAbove(this);if(_4.BL)_4.BL.moveAbove(this);if(_4.BR)_4.BR.moveAbove(this)}
if(_3){this.delayCall("$0p",[],0)}
this.zIndexChanged(_2,_1)}
,isc.A.$08=function isc_Canvas__adjustSpecialPeers(_1){if(this.$wy())this.$nw.setZIndex(_1-1);if(this._backMask)this._backMask.setZIndex(_1-2);if(this._shadow)this._shadow.setZIndex(_1-3);if(this.modalMask)this.modalMask.setZIndex(_1-4)}
,isc.A.zIndexChanged=function isc_Canvas_zIndexChanged(_1,_2){if(this.children)this.children.map("parentZIndexChanged")}
,isc.A.parentZIndexChanged=function isc_Canvas_parentZIndexChanged(){if(this.children)this.children.map("parentZIndexChanged")}
,isc.A.bringToFront=function isc_Canvas_bringToFront(_1){if(isc.$dd)arguments.$de=this;isc.Canvas.$ri+=18;this.setZIndex(isc.Canvas.$ri);if(_1&&!this.$um())return;isc.$pe=true;this.unmask();isc.$pe=false}
,isc.A.sendToBack=function isc_Canvas_sendToBack(){isc.Canvas.$rh-=18;this.setZIndex(isc.Canvas.$rh)}
,isc.A.moveAbove=function isc_Canvas_moveAbove(_1){var z=_1.getZIndex(true);this.setZIndex(z+6)}
,isc.A.moveBelow=function isc_Canvas_moveBelow(_1){var z=_1.getZIndex(true);this.setZIndex(z-6)}
,isc.A.getContents=function isc_Canvas_getContents(){var _1=(isc.isA.Function(this.contents)?this.contents():this.contents);return this.dynamicContents?_1.evalDynamicString(this,this.dynamicContentsVars):_1}
,isc.A.setContents=function isc_Canvas_setContents(_1){if(_1!=null)this.contents=_1;this.markForRedraw("setContents")}
,isc.A.containsIFrame=function isc_Canvas_containsIFrame(){return this.contentsURL!=null&&this.contentsType=="page"}
,isc.A.getContentsURL=function isc_Canvas_getContentsURL(){return this.contentsURL}
,isc.A.setContentsURL=function isc_Canvas_setContentsURL(_1,_2){this.contentsURL=_1;_1=isc.Page.getURL(_1);var _3=isc.addProperties({},this.contentsURLParams,_2),_1=isc.rpc.addParamsToURL(_1,_3);if(!this.isDrawn())return;if(this.containsIFrame()){var _4=this.$hk();if(!_4||!_1)this.markForRedraw("setContentsURL");else _4.src=_1}}
,isc.A.setBackgroundColor=function isc_Canvas_setBackgroundColor(_1){if(_1)this.backgroundColor=_1;if(this.isDrawn()){return this.getStyleHandle().backgroundColor=_1}}
,isc.A.setBackgroundImage=function isc_Canvas_setBackgroundImage(_1){if(_1)this.backgroundImage=_1;if(this.isDrawn()){if(this.isDrawn())this.getStyleHandle().backgroundImage='url('+this.getImgURL(this.backgroundImage)+')'}}
,isc.A.setBorder=function isc_Canvas_setBorder(_1){this.$x0=null;if(_1!=null&&!isc.isA.String(_1)){_1=this.$tu(_1)}
if(_1==null)return;if(isc.endsWith(_1,isc.semi))_1=_1.slice(0,_1.length-1);this.border=_1;var _2=this.getStyleHandle();if(!_2)return;if(_2.border!=_1){_2.border=_1}
this.adjustOverflow("setBorder");this.innerSizeChanged("Border thickness changed")}
,isc.A.$tu=function isc_Canvas__convertBorderToString(_1){var _2=_1;if(isc.isA.Number(_1)){_1+="px solid"}else{_1=null;this.logWarn("this.border defined as "+_2+". This property should have a string value - dropping this attribute.")}
return _1}
,isc.A.getBorder=function isc_Canvas_getBorder(){return this.border}
,isc.A.setOpacity=function isc_Canvas_setOpacity(_1,_2,_3){if(!_2&&this.fadeAnimation)this.finishAnimation("fade");var _4=this.opacity;this.opacity=_1;if(this.opacity==100&&!_3&&!(this.smoothFade&&isc.Browser.isMoz))this.opacity=null;if(this.isDrawn()){if(isc.Browser.isMoz){var _5=(this.opacity!=null)?this.opacity/ 100:"";if(this.smoothFade&&(_5==1||this.opacity==null))_5=0.9999;if(this.$rt)this.getStyleHandle().MozOpacity=_5;else this.getStyleHandle().opacity=_5}else if(isc.Browser.isIE){if(!isc.Canvas.neverUseFilters||this.useOpacityFilter){this.getStyleHandle().filter=(this.opacity==null?"":"progid:DXImageTransform.Microsoft.Alpha(opacity="+this.opacity+")")}}else{var _5=(this.opacity!=null)?this.opacity/ 100:"";this.getStyleHandle().opacity=_5}}
this.$09(_1,_2,_3||_1!=null);if(isc.Browser.isIE&&this.fixIEOpacity&&this.children){for(var i=0;i<this.children.length;i++){var _7=this.children[i];if(_7.opacity==null&&(_3||_1!=null)){_7.setOpacity(100,_2,true)}else if(_7.opacity==100){_7.setOpacity(null)}}}
this.opacityChanged(_1,_2)}
,isc.A.opacityChanged=function isc_Canvas_opacityChanged(_1,_2){}
,isc.A.$09=function isc_Canvas__setPeersOpacity(_1,_2,_3){if(!this.peers)return;for(var i=0;i<this.peers.length;i++){if(this.peers[i].$rw){this.peers[i].setOpacity(_1,_2,_3)}else if(this.peers[i]==this.edgedCanvas&&this.edgeOpacity){var _5=Math.round(this.opacity*(this.edgeOpacity*.01));this.peers[i].setOpacity(_5,_2,_3)}}}
,isc.A.setPrompt=function isc_Canvas_setPrompt(_1){this.prompt=_1;this.updateHover()}
,isc.A.setCursor=function isc_Canvas_setCursor(_1){if(_1&&_1!=this.cursor){this.cursor=_1;this.$ms()}}
,isc.A.$1a=function isc_Canvas__applyCursor(_1){if(this.isDrawn()){if((isc.Browser.isMoz||(isc.Browser.isStrict&&isc.Browser.isSafari))&&_1=="hand")_1=isc.Canvas.HAND;this.$1b=_1;this.getStyleHandle().cursor=_1;if(this.useClipDiv)this.getHandle().style.cursor=_1;if(this.$mj){for(var i=0;i<this.$mj.length;i++){this.$mj[i].$1a(_1)}}
if(this.ns.EH.$1c&&(this==this.ns.EH.getTarget())){this.ns.EH.$1c.setCursor(_1)}}
if(isc.Browser.isOpera&&isc.EH.lastEvent.target==this)this.markForRedraw()}
,isc.A.$ms=function isc_Canvas__updateCursor(){var _1=this.getCurrentCursor();if(this.$1b==_1)return;this.$1a(_1)}
,isc.A.getCurrentCursor=function isc_Canvas_getCurrentCursor(){var _1=this.cursor;if(isc.EH.dragging&&this.$y9&&(isc.EH.dragMoveTarget!=this)){_1=this.noDropCursor}else if(this.isDisabled())_1=this.disabledCursor;else{var _2;if(this.canDragResize&&this.edgeCursorMap){var _3=this.getEventEdge();if(_3&&this.edgeCursorMap[_3]){_1=this.edgeCursorMap[_3];_2=true}}
if(!_2&&this.canDragReposition&&this.dragRepositionCursor){_1=this.dragRepositionCursor}}
return _1}
,isc.A.getHoverTarget=function isc_Canvas_getHoverTarget(_1,_2){var _3=this;while(_3){if(_3.getCanHover()==null){if(_3.prompt!=null)return _3;_3=_3.parentElement}else if(_3.getCanHover()){return _3}else{return null}}
return null}
,isc.A.startHover=function isc_Canvas_startHover(_1){isc.Hover.setAction(this,this.$1d,null,this.hoverDelay)}
,isc.A.stopHover=function isc_Canvas_stopHover(_1){isc.Hover.clear()}
,isc.A.$1d=function isc_Canvas__handleHover(){var _1=isc.EH,_2=_1.lastMoveTarget;var _3=_1.lastEvent;if(!_2||_2.getHoverTarget(_3)!=this)return;return this.handleHover()}
,isc.A.getCanHover=function isc_Canvas_getCanHover(){return this.canHover}
,isc.A.getHoverComponent=function isc_Canvas_getHoverComponent(){}
,isc.A.handleHover=function isc_Canvas_handleHover(){if(this.hover&&this.hover()==false)return;if(this.showHover){var _1=this.showHoverComponents&&this.getHoverComponent?this.getHoverComponent():null;if(_1!=null&&isc.isA.Canvas(_1)){var _2=this.$1e();isc.Hover.show(_1,_2,null,this)}else{var _3=this.getHoverHTML();if(_3!=null&&!isc.isAn.emptyString(_3)){var _2=this.$1e();isc.Hover.show(_3,_2,null,this)}}}}
,isc.A.updateHover=function isc_Canvas_updateHover(_1){if(isc.Hover.lastHoverCanvas!=this||!isc.Hover.hoverCanvas.isVisible())return;if(_1==null)_1=this.getHoverHTML();isc.Hover.show(_1,this.$1e(),null,this)}
,isc.A.$1f=function isc_Canvas__hoverHidden(){if(this.hoverCanvas&&this.hoverCanvas.hoverAutoDestroy!=false){this.hoverCanvas.markForDestroy();this.hoverCanvas=null;delete this.hoverCanvas}
this.hoverHidden()}
,isc.A.hoverHidden=function isc_Canvas_hoverHidden(){}
,isc.A.$1e=function isc_Canvas__getHoverProperties(){return{width:this.hoverWidth,height:this.hoverHeight,align:this.hoverAlign,valign:this.hoverVAlign,baseStyle:this.hoverStyle,opacity:this.hoverOpacity,moveWithMouse:this.hoverMoveWithMouse,wrap:this.hoverWrap}}
);isc.evalBoundary;isc.B.push(isc.A.getHoverHTML=function isc_Canvas_getHoverHTML(){return this.prompt}
,isc.A.setClassName=function isc_Canvas_setClassName(_1){if(this.logIsInfoEnabled(this.$tn)){this.logInfo("call to deprecated setClassName() property - use setStyleName() instead")}
return this.setStyleName(_1)}
,isc.A.setStyleName=function isc_Canvas_setStyleName(_1){this.$x0=null;this.$x1=null;this.$yq();if(_1){this.styleName=_1;this.className=_1}
if(this.getClipHandle())this.getClipHandle().className=this.styleName;if(this.overflow!=isc.Canvas.HIDDEN)this.adjustOverflow("setStyleName")}
,isc.A.getStateName=function isc_Canvas_getStateName(){var _1=this.getClipHandle().className;return(_1!=null?_1:this.styleName)}
,isc.A.handleShowContextMenu=function isc_Canvas_handleShowContextMenu(_1){if(_1.target==this&&this.useEventParts){var _2=this.getEventPart(_1);if(_2.part){if(this.$1g(_2.part,"showContextMenu",_2.element,_2.ID,_1)==false)return false}}
if(this.showContextMenu)return this.showContextMenu(_1)}
,isc.A.showContextMenu=function isc_Canvas_showContextMenu(){var _1=this.contextMenu;if(_1){_1.target=this;if(!isc.isA.Canvas(_1)){_1.autoDraw=false;this.contextMenu=_1=this.getMenuConstructor().create(_1)}
_1.showContextMenu()}
return(_1==null)}
,isc.A.getMenuConstructor=function isc_Canvas_getMenuConstructor(){var _1=isc.ClassFactory.getClass(this.menuConstructor);if(!_1){isc.logWarn("Class not found for menuConstructor:"+this.menuConstructor+". Defaulting to isc.Menu class");_1=isc.ClassFactory.getClass("Menu")}
return _1}
,isc.A.hideContextMenu=function isc_Canvas_hideContextMenu(){if(this.contextMenu)this.contextMenu.hideContextMenu()}
,isc.A.$mh=function isc_Canvas__allowNativeTextSelection(_1){return this.canSelectText}
,isc.A.handleMouseMove=function isc_Canvas_handleMouseMove(_1,_2){if(_1.target==this&&this.useEventParts){var _3=this.getEventPart(_1),_4=this.$ry;if(_4&&_4.part&&(_4.part!=_3.part||_4.ID!=_3.ID))
{this.$1g(_4.part,isc.EH.MOUSE_OUT,_4.element,_4.ID,_1)}
if(_3.part){var _5=!_4||(_4.ID!=_3.ID),_6=(_5?isc.EH.MOUSE_OVER:isc.EH.MOUSE_MOVE);this.$1g(_3.part,_6,_3.element,_3.ID,_1);if(_5){isc.Hover.setAction(this,this.$1h,[_3.element,_3.ID],this.hoverDelay)}}
this.$ry=_3}
if(this.mouseMove)return this.mouseMove(_1,_2)}
,isc.A.$1h=function isc_Canvas__handleRectHover(_1,_2){if(this.$ry)this.$1g(this.$ry.part,"hover",_1,_2)}
,isc.A.handleMouseOut=function isc_Canvas_handleMouseOut(_1,_2){if(_1.target==this&&this.useEventParts){var _3=this.$ry;if(_3&&_3.part){this.$1g(_3.part,isc.EH.MOUSE_OUT,_3.element,_3.ID,_1)}}
if(this.mouseOut)return this.mouseOut(_1,_2)}
,isc.A.handleMouseDown=function isc_Canvas_handleMouseDown(_1,_2){var _3=this.$y3;if(_3!=null){this.cancelAnimation(_3)}
if(_1.target==this&&this.useEventParts)this.firePartEvent(_1,isc.EH.MOUSE_DOWN);if(this.mouseDown)return this.mouseDown(_1,_2)}
,isc.A.handleMouseUp=function isc_Canvas_handleMouseUp(_1,_2){if(_1.target==this&&this.useEventParts)this.firePartEvent(_1,isc.EH.MOUSE_UP);if(this.mouseUp)return this.mouseUp(_1,_2)}
,isc.A.handleClick=function isc_Canvas_handleClick(_1,_2){if(_1.target==this&&this.useEventParts)this.firePartEvent(_1,isc.EH.CLICK);if(this.click)return this.click(_1,_2)}
,isc.A.handleDoubleClick=function isc_Canvas_handleDoubleClick(_1,_2){if(_1.target==this&&this.useEventParts)this.firePartEvent(_1,isc.EH.DOUBLE_CLICK);if(this.doubleClick)return this.doubleClick(_1,_2)}
,isc.A.handleLongTouch=function isc_Canvas_handleLongTouch(_1,_2){return this.handleShowContextMenu(_1,_2)}
,isc.A.getEventPart=function isc_Canvas_getEventPart(_1){if(!_1)_1=isc.EH.lastEvent;var _2=_1.nativeTarget;return this.getElementPart(_2)}
,isc.A.getElementPart=function isc_Canvas_getElementPart(_1){var _2,_3;if(_1&&_1.getAttribute)_2=_1.getAttribute(this.$to);if(_2&&_2!=isc.emptyString){var _4=_1.id;if(_4&&_4!=isc.emptyString){_3=_4.substring(this.getID().length+_2.length+2)}}
return{part:_2,ID:_3,element:_1}}
,isc.A.getPartElement=function isc_Canvas_getPartElement(_1){var _2=_1.part,_3=_1.partID,_4=this.getID+"_"+_2;if(_3)_4+=_3;var _5=isc.Element.get(_4);if(_5)return _5;return isc.Element.findAttribute(this.getHandle(),this.$to,_2)}
,isc.A.firePartEvent=function isc_Canvas_firePartEvent(_1,_2){if(!this.useEventParts||!_1)return;var _3=this.getEventPart(_1);if(!_3.part)return;if(!_2)_2=_1.eventType;return this.$1g(_3.part,_2,_3.element,_3.ID,_1)}
,isc.A.$1g=function isc_Canvas__firePartEvent(_1,_2,_3,_4,_5){var _6=this.getPartEventHandler(_1,_2);if(this[_6]){return this[_6](_3,_4,_5)}}
,isc.A.getPartEventHandler=function isc_Canvas_getPartEventHandler(_1,_2){if(!isc.Canvas.$ro[_1])isc.Canvas.$ro[_1]={};if(!isc.Canvas.$ro[_1][_2]){var _3=_2.substring(0,1).toUpperCase()+_2.substring(1);isc.Canvas.$ro[_1][_2]=_1+_3}
return isc.Canvas.$ro[_1][_2]}
,isc.A.getDragType=function isc_Canvas_getDragType(){return this.dragType}
,isc.A.willAcceptDrop=function isc_Canvas_willAcceptDrop(){if(this.ns.EH.dragTarget==null)return false;if(this.dropTypes==isc.Canvas.ANYTHING||this.dropTypes==null||isc.is.emptyString(this.dropTypes))
{return true}
var _1=this.ns.EH.dragTarget.getDragType();if(_1==null||isc.is.emptyString(_1))return false;if(isc.isA.String(_1)){return this.dropTypes.contains(_1)}else if(isc.isAn.Array(_1)){for(var i=0,_3=true,_4=_1.length;i<_4&&_3;i++){_3=_3&&(this.dropTypes.contains(_1))}
return _3}
return false}
,isc.A.$n6=function isc_Canvas__showDragMask(){if(this._eventMask.visibility==isc.Canvas.HIDDEN)this._eventMask.show()}
,isc.A.$n7=function isc_Canvas__hideDragMask(){if(this._eventMask.visibility!=isc.Canvas.HIDDEN)this._eventMask.hide()}
,isc.A.handleDrop=function isc_Canvas_handleDrop(_1,_2){if(this.onDrop!=null&&(this.onDrop()==false))return false;return this.drop(_1,_2)}
,isc.A.getHSnapPosition=function isc_Canvas_getHSnapPosition(_1,_2){if(!_2){_2=this.snapHDirection}
if(_2!=isc.Canvas.BEFORE&&_2!=isc.Canvas.AFTER&&_2!=isc.Canvas.NEAREST){return _1}
var _3=Math.floor(_1/ this.snapHGap)*this.snapHGap;var _4=_3+this.snapHGap;var _5=_3+this.snapHGap/ 2;if(_2==isc.Canvas.BEFORE){return _3}else if(_2==isc.Canvas.AFTER){return _4}else{if(_1<=_5)return _3;else return _4}}
,isc.A.getVSnapPosition=function isc_Canvas_getVSnapPosition(_1,_2){if(!_2){_2=this.snapVDirection}
if(_2!=isc.Canvas.BEFORE&&_2!=isc.Canvas.AFTER&&_2!=isc.Canvas.NEAREST){return _1}
var _3=Math.floor(_1/ this.snapVGap)*this.snapVGap;var _4=_3+this.snapVGap;var _5=_3+this.snapVGap/ 2;if(_2==isc.Canvas.BEFORE){return _3}else if(_2==isc.Canvas.AFTER){return _4}else{if(_1<=_5)return _3;else return _4}}
,isc.A.shouldSnapOnDrop=function isc_Canvas_shouldSnapOnDrop(_1){return true}
,isc.A.noSnapDragOffset=function isc_Canvas_noSnapDragOffset(_1){return false}
,isc.A.setAppImgDir=function isc_Canvas_setAppImgDir(_1){if(_1)this.appImgDir=_1}
,isc.A.getAppImgDir=function isc_Canvas_getAppImgDir(){return isc.Page.getImgURL("",this.appImgDir)}
,isc.A.setSkinImgDir=function isc_Canvas_setSkinImgDir(_1){if(_1)this.skinImgDir=_1}
,isc.A.getSkinImgDir=function isc_Canvas_getSkinImgDir(){return isc.Page.getSkinImgDir(this.skinImgDir)}
,isc.A.getImgURL=function isc_Canvas_getImgURL(_1,_2){return isc.Canvas.getImgURL(_1,_2,this)}
,isc.A.imgHTML=function isc_Canvas_imgHTML(_1,_2,_3,_4,_5,_6,_7){return isc.Canvas.imgHTML(_1,_2,_3,_4,_5,_6,_7,this)}
,isc.A.$1i=function isc_Canvas__getImgHTMLTemplate(_1,_2,_3,_4,_5,_6,_7){return isc.Canvas.imgHTML(_1,_2,_3,_4,_5,_6,_7,this,true)}
,isc.A.getImage=function isc_Canvas_getImage(_1){if(isc.isA.String(_1))_1=this.getCanvasName()+_1;var _2=this.getHandle();if(_2){if(isc.Page.isXHTML()){return document.getElementById(_1)}else{if(_2.document){return _2.document.images[_1]}else{return document.images[_1]}}}
return null}
,isc.A.setImage=function isc_Canvas_setImage(_1,_2,_3){var _4=this.getImage(_1);if(_4==null){this.logWarn("setImage: image '"+_1+"' couldn't be found");return}
isc.Canvas.$1j(_4,_2,_3,this)}
,isc.A.linkHTML=function isc_Canvas_linkHTML(_1,_2,_3,_4,_5,_6,_7){return isc.Canvas.linkHTML(_1,_2,_3,_4,_5,_6,_7)}
,isc.A.inWhichPosition=function isc_Canvas_inWhichPosition(_1,_2,_3){if(!_1||_2<0)return-1;if(_3==isc.Page.RTL){var _4=_1.sum();for(var c=0,_6=_1.length;c<_6;c++){if(_2>=_4-_1[c])return c;_4-=_1[c]}}else{for(var c=0,_6=_1.length;c<_6;c++){if(_2<=_1[c]){return c}
_2-=_1[c]}}
return-2}
,isc.A._canvasList=function isc_Canvas__canvasList(_1){var _2=isc.Canvas._canvasList;if(_1)_2.add(this);else _2.remove(this);if(this.$wb){isc.Canvas._iscInternalCount+=(_1?1:-1)}else{isc.Log.updateStats(this.$hg)}}
,isc.A.$ue=function isc_Canvas__addStat(_1){if(!this.$wb){isc.Canvas._stats[_1]++;isc.Log.updateStats(_1)}}
,isc.A.$1k=function isc_Canvas__attachedPeers(_1){var _2=this.$wz;if(!_2)return null;if(_1)return _2[_1]}
,isc.A.$1l=function isc_Canvas__registerAttachedPeer(_1,_2,_3,_4){if(_1==null||_2==null)return;if(!this.$wz)this.$wz={};if(!this.$wz[_2])this.$wz[_2]=[];this.$wz[_2].add(_1);if(_3!=null)_1.$xv=_3
if(_4){this.observe(_1,"resized","observer.$1m(observed)")}
delete this.$xo;delete this.$xp}
,isc.A.$xs=function isc_Canvas__unRegisterAttachedPeer(_1,_2,_3){if(_1==null||_2==null)return;if(!this.$wz||!this.$wz[_2])return;this.$wz[_2].remove(_1);if(this.isObserving(_1,"resized")){this.ignore(_1,"resized")}
delete _1.$xv;delete this.$xo;delete this.$xp}
,isc.A.$1m=function isc_Canvas__attachedPeerResized(_1){this.refreshMargin()}
,isc.A.refreshMargin=function isc_Canvas_refreshMargin(){this.setMargin(this.margin)}
,isc.A.$ty=function isc_Canvas__makeCornerClips(){this.$yn={};for(var i=0;i<this.clippedCorners.length;i++){this.$1n(this.clippedCorners[i])}}
,isc.A.$1n=function isc_Canvas__makeCornerClip(_1){var _2=this.$yn,_3=this.left,_4=this.top,_5=this.cornerClipWidth||this.cornerClipSize,_6=this.cornerClipHeight||this.cornerClipSize;if(_1=="TR"||_1=="BR"){_3=_3+this.getWidth()-_5}
if(_1=="BL"||_1=="BR"){_4=_4+this.getHeight()-_6}
if(this.noCornerClipImages&&!(isc.Browser.isIE&&isc.Browser.minorVersion>=5.5)){this.noCornerClipImages=false}
var _7=_2[_1]=isc.ClassFactory.newInstance({_constructor:(this.noCornerClipImages?"Canvas":"Img"),left:_3,top:_4,width:_5,height:_6,eventProxy:this,src:(this.noCornerClipImages?null:this.$1o(_1)),contents:(this.noCornerClipImages?this.$1p(_5,_6,_1):null)},this.$rx);this.addPeer(_7);_7.moveAbove(this)}
,isc.A.$un=function isc_Canvas__finishCornerClips(){if(!this.noCornerClipImages)return;for(var _1 in this.$yn){var _2=this.$yn[_1],_3=_2.getHandle().firstChild,_4=_3.style;_3.filters[0].apply();_4.visibility="hidden";_3.filters[0].percent=71}}
,isc.A.$1o=function isc_Canvas__getCornerImage(_1){return isc.Img.urlForState(this.cornerClipImage,null,null,this.cornerClipColor,_1)}
,isc.A.$1p=function isc_Canvas__getCornerHTML(_1,_2,_3){var _4=isc.SB.create();_4.append("<DIV STYLE='width:",2*_1,"px;height:",2*_2,"px;filter:progid:DXImageTransform.Microsoft.iris(irisStyle=circle,motion=out);");if(_3.contains("R"))_4.append("margin-left:",-_1,"px;");if(_3.contains("B"))_4.append("margin-top:",-_2,"px;");_4.append("'><DIV STYLE='overflow:hidden;width:",_1,"px;height:",_2,"px;background-color:",this.cornerClipColor,";");if(_3.contains("R"))_4.append("margin-left:",_1,"px;");if(_3.contains("B"))_4.append("margin-top:",_2,"px;");_4.append("'></DIV></DIV>");return _4.toString()}
,isc.A.$wy=function isc_Canvas__edgesAsPeer(){return this.showEdges&&!this.edgesAsChild}
,isc.A.$tw=function isc_Canvas__createEdges(){if(!this.showEdges||isc.isA.EdgedCanvas(this)||this.$nw!=null){return this.$nw}
var _1=this.$nw=this.$1q();if(this.edgesAsChild){_1.resizeTo("100%","100%");_1.sendToBack();this.addChild(_1)}else{this.addPeer(_1)}
return _1}
,isc.A.setEdgeOpacity=function isc_Canvas_setEdgeOpacity(_1){var _2=this.edgeOpacity=_1;if(this.opacity>0&&this.opacity<100){_2=this.opacity*(this.edgeOpacity/ 100)}
this.$nw.setOpacity(_2)}
,isc.A.$1q=function isc_Canvas__createEdgedCanvas(){var _1=this.$tp,_2=isc.EdgedCanvas.createRaw();_2.autoDraw=false;_2._generated=true;_2.containedPeer=true;_2.dragTarget=this;_2.visibility=this.visibility;_2.opacity=this.opacity;if(this.edgeOpacity!=null){_2.opacity=this.edgeOpacity;_2.$rw=false}
_2.smoothFade=this.smoothFade;if(this.edgeOverflow!=null)_2.overflow=this.edgeOverflow;_2.eventProxy=this;for(var i=0;i<_1.length;i++){var _4=_1[i];if(this[_4]!=null)_2[_4]=this[_4]}
if(this.edgeBackgroundColor)_2.backgroundColor=this.edgeBackgroundColor;if(this.edgeCenterBackgroundColor){_2.centerBackgroundColor=this.edgeCenterBackgroundColor}
if(this.edgeShowCenter!=null)_2.showCenter=this.edgeShowCenter;if(!this.edgesAsChild)_2.zIndex=this.getZIndex(true)-1;_2.completeCreation();return _2}
,isc.A.setShowShadow=function isc_Canvas_setShowShadow(_1){this.showShadow=_1;if(_1){if(!this._shadow)this.$tx();else if(this.isDrawn())this._shadow.show()}else{if(this._shadow)this._shadow.hide()}}
,isc.A.$tx=function isc_Canvas__createShadow(){var _1=this._shadow=this.createAutoChild("shadow",{visibility:this.visibility,zIndex:this.getZIndex(true)-3},isc.DropShadow);this.updateShadow(true);this.addPeer(_1);_1.moveBelow(this)}
,isc.A.updateShadow=function isc_Canvas_updateShadow(_1){if(!_1)this.setShowShadow(this.showShadow);var _2=this._shadow;if(!_2)return;_2.offset=this.shadowOffset;_2.offsetX=this.shadowOffsetX;_2.offsetY=this.shadowOffsetY;_2.softness=this.shadowSoftness;if(this.shadowImage)_2.setEdgeImage(this.shadowImage);_2.setDepth(this.shadowDepth);if(this.dragResizeFromShadow&&this.canDragResize){_2.canDragResize=this.canDragResize;_2.resizeFrom=this.resizeFrom;_2.dragTarget=this}}
,isc.A.propertyChanged=function isc_Canvas_propertyChanged(_1,_2){if(isc.contains(_1,this.$tq)&&this.updateShadow)this.updateShadow()}
,isc.A.setIsGroup=function isc_Canvas_setIsGroup(_1){if(_1==this.isGroup)return;var _2=this.shouldShowGroupLabel()&&this.isDrawn();if(_2)this.clear();if(_1){this.$1r=this.border;this.setBorder(this.groupBorderCSS);if(this.shouldShowGroupLabel())this.$1s()}else{this.setBorder(this.$1r||"");if(this.shouldShowGroupLabel())this.$1t()}
this.isGroup=_1;if(_2)this.draw()}
,isc.A.shouldShowGroupLabel=function isc_Canvas_shouldShowGroupLabel(){return this.showGroupLabel}
,isc.A.makeGroupLabel=function isc_Canvas_makeGroupLabel(){if(!this.groupLabel){var _1={autoDraw:false,$k6:false,$rv:true,backgroundColor:this.getGroupLabelBackgroundColor(),eventProxy:this,styleName:this.groupLabelStyleName}
if(this.groupTitle!=null)_1.contents=this.groupTitle;this.groupLabel=this.createAutoChild("groupLabel",_1)}else{if(this.groupTitle!=null)this.groupLabel.setContents(this.groupTitle);this.groupLabel.setBackgroundColor(this.getGroupLabelBackgroundColor())}}
,isc.A.getGroupLabelBackgroundColor=function isc_Canvas_getGroupLabelBackgroundColor(){if(this.groupLabelBackgroundColor)return this.groupLabelBackgroundColor;if(this.backgroundColor!=null)return this.backgroundColor;return"white"}
,isc.A.$1s=function isc_Canvas__showGroupLabel(){this.makeGroupLabel();var _1=this.groupLabel;var _2;if(_1.overflow==isc.Canvas.VISIBLE){if(_1.parentElement!=null)_1.deparent();_1.setTop(-1000);_1.draw();_2=_1.getVisibleHeight()}else{_2=_1.getVisibleHeight()}
var _3=Math.round(_2/ 2);this.$1l(_1,isc.Canvas.TOP,_3);var _4=_2-_3;if(this.padding)_4+=this.padding;this.setTopPadding(_4);_1.setLeft(this.getLeft()+this.groupLabelPadding);_1.setTop(this.getTop());if(_1.masterElement!=this)this.addPeer(_1);if(this.isDrawn()){if(!_1.isDrawn())_1.draw()}
this.getTopMargin();_1.moveAbove(this);if(_1.isDrawn()&&!this.isDrawn())_1.clear()}
,isc.A.$1t=function isc_Canvas__hideGroupLabel(){if(!this.groupLabel)return;var _1=this.groupLabel;this.$xs(_1,isc.Canvas.TOP);this.setTopPadding(null);_1.clear();_1.depeer()}
,isc.A.setGroupTitle=function isc_Canvas_setGroupTitle(_1){this.groupTitle=_1;if(this.groupLabel){this.groupLabel.setContents(this.groupTitle)}else{this.$1s()}}
);isc.B._maxIndex=isc.C+544;isc.A=isc.Canvas;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$iz="[SKIN]";isc.A.printOmitControls=["Button","StretchImgButton","ImgButton","MenuButton","Toolbar","ToolStrip","ButtonItem","ToolbarItem"];isc.A.printIncludeControls=["Label"];isc.A.$1u=0;isc.A.$1v="ID='";isc.A.$r9="'";isc.A.$1w="absmiddle";isc.A.$1x=[,,," eventpart='valueicon' style='vertical-align:middle;margin-left:",,"px;margin-right:",,"px;'"];isc.A.$1y={};isc.A.$1z={png:true,PNG:true,Png:true};isc.A.$10=["<a",," href='",,"' target='",,"'",,,,">",,"</a>"];isc.A.$11="[SKINIMG]/blank.gif";isc.A.$fm="0";isc.A.$12="clearRedrawQueue";isc.A.$13=[];isc.A.$14=0;isc.A.$15="clearDestroyQueue";isc.A.$x3=[];isc.B.push(isc.A.stripScriptTags=function isc_c_Canvas_stripScriptTags(_1){return _1.replace(/<script([^>]*)?>(.|\n|\r)*?<\/script>/ig,isc.emptyString)}
,isc.A.stripLinkTags=function isc_c_Canvas_stripLinkTags(_1){return _1.replace(/<link([^>]*)?>/ig,isc.emptyString)}
,isc.A.getById=function isc_c_Canvas_getById(_1){var _2=window[_1]||null;return _2?(isc.isA.Canvas(_2)?_2:null):null}
,isc.A.getNextZIndex=function isc_c_Canvas_getNextZIndex(){return(isc.Canvas.$rg+=18)}
,isc.A.getFocusProxyString=function isc_c_Canvas_getFocusProxyString(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13,_14,_15,_16){if(this.$16==null){this.$17="' ONFOCUS=";this.$18="' ";this.$19=" ONBLUR=";this.$16=["<div"," id='",null,"$2a'"+" style='overflow:hidden;width:0px;height:0px;position:",,";left:",null,"px;top:",null,"px;'>",(isc.Browser.isSafari?"<textarea":(isc.Browser.isMoz&&isc.Browser.geckoVersion>=20051111?"<div":"<button onclick='event.cancelBubble=true;return false;'"))," id='",null,"__focusProxy'"," style='VISIBILITY:",null,"left:1px;top:1px;"+"width:",(isc.Browser.isSafari?"1":null),"px;height:",(isc.Browser.isSafari?"1":null),"px;",null,this.$17,null,this.$19,null,null,null,null," tabindex='",null,null,"' focusProxy='true' handleNativeEvents='",null,"'>",(isc.Browser.isSafari?"</textarea>":(isc.Browser.isMoz&&isc.Browser.geckoVersion>=20051111?"</div>":"</button>")),"</div>"]}
var _17=this.$16;_17[2]=_1;_17[4]=(_2?"absolute":"inline");_17[6]=_3;_17[8]=_4;_17[12]=_1;_17[15]=(_7?"visible;":"hidden;");_17[17]=_5;_17[19]=_6;if(isc.Browser.isMoz){if(!_8||_9==-1)_17[21]="-moz-user-focus:ignore;";else _17[21]="-moz-user-focus:normal;"}
if(_12&&_12!=isc.emptyString){_17[22]=this.$17;_17[23]=_12}else{_17[22]=this.$18;_17[23]=null}
if(_13&&_13!=isc.emptyString){_17[24]=this.$19;_17[25]=_13}else{_17[24]=null;_17[25]=null}
_17[26]=(_14!=null?" onkeydown="+_14:null);_17[27]=(_15!=null?" onkeypress="+_15:null);_17[28]=(_16!=null?" onkeyup="+_16:null);_17[30]=(_8?_9:-1);_17[31]=(_8&&_10?"' accesskey='"+_10:null);_17[33]=(_11?true:false);return _17.join(isc.$ah)}
,isc.A.clearCSSCaches=function isc_c_Canvas_clearCSSCaches(){isc.Element.$qt();var _1=isc.Canvas._canvasList;for(var i=0;i<_1.length;i++){var _3=_1[i];if(_3==null||_3.destroyed)continue;_3.$xp=_3.$xo=_3.$x0=_3.$x1=null}}
,isc.A.setAppImgDir=function isc_c_Canvas_setAppImgDir(_1){this.getPrototype().appImgDir=_1}
,isc.A.getAppImgDir=function isc_c_Canvas_getAppImgDir(){return isc.Page.getImgURL(isc.emptyString,this.getPrototype().appImgDir)}
,isc.A.setSkinImgDir=function isc_c_Canvas_setSkinImgDir(_1){this.getPrototype().skinImgDir=_1}
,isc.A.getSkinImgDir=function isc_c_Canvas_getSkinImgDir(){return isc.Page.getSkinImgDir(this.getPrototype().skinImgDir)}
,isc.A.getImgURL=function isc_c_Canvas_getImgURL(_1,_2,_3){if(_1==null||isc.isAn.emptyString(_1))return isc.$ah;_3=_3||this.getPrototype();if(_1.imgDir!=null&&_2==null)_2=_1.imgDir;if(_1.src!=null)_1=_1.src;if(_2==null){_2=(isc.startsWith(_1,this.$iz)?_3.skinImgDir:_3.appImgDir)}
var _4=isc.Page.getImgURL(_1,_2);return _4}
,isc.A.setShowCustomScrollbars=function isc_c_Canvas_setShowCustomScrollbars(_1){isc.Canvas.addProperties({showCustomScrollbars:_1})}
,isc.A.getPrintHTML=function isc_c_Canvas_getPrintHTML(_1,_2,_3,_4,_5,_6){if(!isc.isAn.Array(_1))_1=[_1];if(_5==null)_5=[];if(_6==null)_6=0;var _7,_8={target:this,methodName:"gotComponentPrintHTML",components:_1,printProperties:_2,callback:_3,HTML:_5,index:_6,separator:_4};for(;_6<_1.length;_6++){_8.index+=1;var _9=_1[_6];var _10;if(isc.isA.String(_9))_10=_9;else _10=_9.getPrintHTML(_2,_8);if(_10!=null){_5.add(_10)}else{_7=true;break}}
if(_7){if(!_3){this.logWarn("getPrintHTML(): HTML generated asynchronously, but no callback passed in")}
return null}
if(_3){this.fireCallback(_3,"HTML,callback",[_5.join(_4||isc.emptyString),_3])}
return _5.join(_4||isc.emptyString)}
,isc.A.gotComponentPrintHTML=function isc_c_Canvas_gotComponentPrintHTML(_1,_2){_2.HTML.add(_1);this.getPrintHTML(_2.components,_2.printProperties,_2.callback,_2.separator,_2.HTML,_2.index)}
,isc.A.getImgHTML=function isc_c_Canvas_getImgHTML(_1,_2,_3,_4,_5,_6,_7,_8,_9){return this.imgHTML(_1,_2,_3,_4,_5,_6,_7,_8,_9)}
,isc.A.$1i=function isc_c_Canvas__getImgHTMLTemplate(_1,_2,_3,_4,_5,_6,_7){return isc.Canvas.imgHTML(_1,_2,_3,_4,_5,_6,_7,null,true)}
,isc.A.imgHTML=function isc_c_Canvas_imgHTML(_1,_2,_3,_4,_5,_6,_7,_8,_9){var _10;if(isc.isAn.Object(_1)){if(_1.width!=null)_2=_1.width;if(_1.height!=null)_3=_1.height;if(_1.name!=null)_4=_1.name;if(_1.extraStuff!=null)_5=_1.extraStuff;if(_1.imgDir!=null)_6=_1.imgDir;if(_1.align!=null)_10=_1.align;if(_1.activeAreaHTML!=null)_7=_1.activeAreaHTML;_1=_1.src}
if(_1==null||isc.isAn.emptyString(_1)){return(_9?[isc.$ah]:isc.$ah)}
var _11=this.$2b;if(!_11){this.$2c="<img src='";this.$2d="' width='";this.$2e="' height='";this.$2f="' align='";this.$2g=(isc.Page.isXHTML()?"' id='":"' name='");this.$2h="' ";this.$2i=isc.Browser.isOpera?"middle":"TEXTTOP";this.$2j=" border='0' suppress='TRUE'/>";this.$2b=_11=[this.$2c];this.$2k="' style='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\"";this.$2l="\",sizingMethod=\"scale\");"}
if(_10==null)_10=this.$2i;if(!this.$i9)this.$i9=this.getImgURL("[SKIN]/blank.gif");if(isc.Browser.isSafari&&(_2>32000||_3>32000)){this.logWarn("Attempting to draw an image of size "+_2+" x "+_3+".  Images larger than 32000 pixels in either direction are not reliably "+" rendered in this browser.")}
var _12=this.getImgURL(_1,_6,_8);if(_9)_11=[this.$2c];if(isc.Page.isXHTML())_12=isc.makeXMLSafe(_12);if(isc.screenReader){if(_5==null||!isc.contains(_5,"alt=")){_11[0]="<img role='presentation' src='"}else{_11[0]=this.$2c}}
if(!this.$us(_8)||!this.$2m(_1)){_11[1]=_12}else{_11[1]=this.$i9;_11[3]=this.$2k;_11[4]=_12;_11[5]=this.$2l;if(_2==null)_2=16;if(_3==null)_3=16}
if(_2){_11[6]=this.$2d;_11[7]=_2}
if(_3){_11[8]=this.$2e;_11[9]=_3}
_11[10]=this.$2f;_11[11]=_10;if(_4){_11[12]=this.$2g;if(_8)_11[13]=_8.getCanvasName();_11[14]=_4}
var _13;if(_7){_13="ISC_IMGMAP_"+this.$1u++;_11[15]="' usemap='#"+_13}
_11[16]=this.$2h;if(_5){_11[17]=_5}
_11[18]=this.$2j;if(_7){_11[19]="<map name='"+_13+"'>"+_7+"</map>"}
if(_9)return _11;var _14=_11.join(isc.$ah);_11.length=3;return _14}
,isc.A.$2n=function isc_c_Canvas__getValueIconHTML(_1,_2,_3,_4,_5,_6,_7,_8){var _9=this.$1x;if(_7!=null){_9[0]=this.$1v;_9[1]=_7;_9[2]=this.$r9}else{_9[0]=_9[1]=_9[2]=null}
_9[4]=_5||0;_9[6]=_6||0;var _1=isc.Canvas.getImgURL(_1,_2,_8),_10=_9.join(isc.emptyString),_11=this.$1y;_11.src=_1;_11.width=_3
_11.height=_4
if(_4!=null&&_4<16&&(isc.Browser.isMoz||isc.Browser.isSafari)){_11.align=null}else{_11.align=this.$1w}
_11.imgDir=_2;_11.extraStuff=_10;return isc.Canvas.imgHTML(_11)}
,isc.A.$us=function isc_c_Canvas__fixPNG(_1){if(this.usePNGFix==false)return false;var _2=isc.Browser.isIE&&isc.Browser.minorVersion>=5.5&&isc.Browser.isWin&&(!isc.Canvas.neverUseFilters&&this.neverUsePNGWorkaround!=true);if(_2&&_1&&_1.$us&&!_1.$us()){_2=false}
return _2}
,isc.A.$2m=function isc_c_Canvas__isPNG(_1){return(_1&&this.$1z[_1.substring(_1.lastIndexOf(isc.dot)+1)])}
,isc.A.$1j=function isc_c_Canvas__setImageURL(_1,_2,_3,_4){var _5=this.getImgURL(_2,_3,_4);if(!this.$us(_4)){_1.src=_5}else{var _6=_1.src,_7=this.$2m(_6),_8=this.$2m(_2);if(_8){_1.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\""+_5+"\",sizingMethod=\"scale\")";if(!_7)_1.src=this.$i9}else{if(_7)_1.style.filter="";_1.src=_5}}}
,isc.A.linkHTML=function isc_c_Canvas_linkHTML(_1,_2,_3,_4,_5,_6,_7){_1=_1.replaceAll("'","\\'");if(_2==null)_2=_1;_3=_3?_3.replaceAll("'","\\'"):"_blank";var _8=this.$10;if(_4!=null)_8[1]=" ID='"+_4+"'";else _8[1]=null;_8[3]=_1;_8[5]=_3;if(_5!=null)_8[7]=" tabIndex="+_5;else _8[7]=null;if(_6!=null)_8[8]=" accessKey='"+_6+"'";else _8[8]=null;if(_7)_8[9]=" "+_7;_8[11]=_2;return _8.join(isc.emptyString)}
,isc.A.blankImgHTML=function isc_c_Canvas_blankImgHTML(_1,_2){var _3=this.$i8;if(!_3){_3=this.$i8=this.$1i(this.$11,1,1)}
_3[7]=_1||this.$fm;_3[9]=_2||this.$fm;return _3.join(isc.$ah)}
,isc.A.spacerHTML=function isc_c_Canvas_spacerHTML(_1,_2,_3){if(_1==0&&_2==0)return isc.$ah;if(isc.Browser.isMoz||isc.Browser.isSafari||isc.Browser.isOpera||isc.Browser.isStrict||(_2<3&&isc.Browser.isIE&&(isc.Browser.minorVersion==5.5||isc.Browser.isMac)))
{var _4;if(isc.Browser.isSafari){_4=32000}else if(isc.Browser.isFirefox&&isc.Browser.geckoVersion>=20090219){_4=17895580}else if(isc.Browser.isIE&&isc.Browser.isStrict){_4=16000}
if(_4!=null&&(_1>_4||_2>_4)){var _5=isc.SB.create(),_6=_4,_7=Math.floor(_2/ _6),_8=Math.floor(_1/ _6);_5.append("<TABLE role='presentation' CELLPADDING=0 CELLSPACING=0 BORDER=0 MARGIN=0>");for(var i=0;i<=_7;i++){_5.append("<TR>");for(var j=0;j<=_8;j++){_5.append("<TD>");var _11=((i==j)||(i>_8&&j==0)||(j>_7&&i==0));if(_11){var _12=(i<_7?_6:_2-(i*_6)),_13=(j<_8?_6:_1-(j*_6));_5.append(this.blankImgHTML(_13,_12))}
_5.append("</TD>")}
_5.append("</TR>")}
_5.append("</TABLE>");return _5.toString()}
return this.blankImgHTML(_1,_2)}
var _14=1300000;if(_2>_14){var _15=[];var _16=0;while(_16<_2){var _17,_18;if(_16+1400>=_2){_17=true;_18=_2-_16}else{_18=1400;_17=false}
_15[_15.length]=this.spacerHTML(_1,_18);_15[_15.length]="<br>";_16+=_18}
return _15.join(isc.$ah)}
var _19=this.$2o;if(_19==null){_19=this.$2o=["<SPAN STYLE='WIDTH:",null,"px;HEIGHT:",null,"px;overflow:hidden;'>",null,"</SPAN>"]}
_19[1]=_1;_19[3]=_2;_19[5]=_3?_3:isc.nbsp;return _19.join(isc.$ah)}
,isc.A.hiliteCharacter=function isc_c_Canvas_hiliteCharacter(_1,_2,_3,_4){if(!isc.isA.String(_1)||!isc.isA.String(_2)||_2.length!=1)
return _1;if(_2==" ")return _1;if(_3==null||_4==null){_3="<span style='text-decoration:underline;'>";_4="</span>"}
var _5=_1.indexOf(_2.toUpperCase());if(_5==-1)_5=_1.indexOf(_2.toLowerCase());if(_5!=-1){var _6=_1.slice(0,_5),_7=_1.slice(_5,_5+1),_8=_1.slice(_5+1);_7=_3+_7+_4;_1=_6.concat(_7,_8)}
return _1}
,isc.A.scheduleRedraw=function isc_c_Canvas_scheduleRedraw(_1){if(_1&&_1.priorityRedraw){this.$rl.addAt(_1,0)}else{this.$rl.add(_1)}
if(!this.$2p){this.$2p=isc.Timer.setTimeout({target:isc.Canvas,methodName:this.$12},this._redrawQueueDelay)}}
,isc.A.clearRedrawQueue=function isc_c_Canvas_clearRedrawQueue(){isc.EH.$jp("RDQ");var _1=isc.timeStamp();this.$2p=null;var _2=this.$rl;this.$rl=[];if(this.logIsDebugEnabled()){var _3="";for(var i=0;i<_2.length;i++){_3+=_2[i];if(i!=_2.length-1)_3+=", "}
this.logDebug("clearRedrawQueue: "+_3,"drawing")}
var _5,_6;for(var i=0;i<_2.length;i++){_5=_2[i];if(_5&&_5.priorityRedraw){_5.priorityRedraw=false;if(_6==null)_6=[];_6.add(_5);_2[i]=null}}
if(_6!=null){this.logInfo("Priority redraw: postponing non-priority items","drawing");this.$rl=_2;this.scheduleRedraw(_2[0]);_2=_6}
var _7=0,_5;for(var i=0;i<_2.length;i++){_5=_2[i];if(_5==null||_5.destroyed)continue;if(_5&&_5.isDirty()){_5.redraw(false);_7++}}
if(this.logIsDebugEnabled("redraws")){this.logDebug("clearRedrawQueue: "+_7+" redraws ("+_2.length+" items), "+(isc.timeStamp()-_1)+"ms","redraws")}
isc.EH.$jq()}
,isc.A.$zr=function isc_c_Canvas__queueForDelayedAdjustOverflow(_1){if(!isc.Canvas.$2q)isc.Canvas.$2q=[];isc.Canvas.$2q.add(_1);if(!isc.Canvas.$2r){isc.Canvas.$2r=isc.Timer.setTimeout({target:isc.Canvas,methodName:"$2s"},isc.Canvas.$rm)}}
,isc.A.$2s=function isc_c_Canvas__clearDelayedAdjustOverflowQueue(){var _1=isc.Canvas.$2q;isc.Canvas.$2q=[];isc.Canvas.$2r=null;if(!_1||_1.length==0)return;for(var i=0;i<_1.length;i++){var _3=window[_1[i]];if(isc.isA.Canvas(_3))_3.adjustOverflow("delayed")}}
,isc.A.checkForPageResize=function isc_c_Canvas_checkForPageResize(){isc.EH.$i4(true)}
,isc.A.moveOffscreen=function isc_c_Canvas_moveOffscreen(_1){if(_1.isDrawn())return;var _2=(!(!isc.Browser.isWin&&isc.Browser.isMoz&&this.showCustomScrollbars==false&&(this.overflow==isc.Canvas.AUTO)));if(_2)_1.moveTo(null,-9999)}
,isc.A.scheduleDestroy=function isc_c_Canvas_scheduleDestroy(_1){if(!_1||_1.destroyed||_1.destroying||!_1.destroy)return;this.$13.add(_1);if(!this.$2t){this.$2t=isc.Timer.setTimeout({target:isc.Canvas,methodName:this.$15},this.$14)}}
,isc.A.clearDestroyQueue=function isc_c_Canvas_clearDestroyQueue(){isc.EH.$jp("DSQ");var _1=isc.timeStamp();this.$2t=null;var _2=this.$13;this.$13=[];if(this.logIsDebugEnabled("destroys")){var _3="";for(var i=0;i<_2.length;i++){_3+=_2[i];if(i!=_2.length-1)_3+=", "}
this.logDebug("clearDestroyQueue: "+_3,"destroys")}
var _5=0,_6;for(var i=0;i<_2.length;i++){_6=_2[i];if(_6==null||_6.destroyed||_6.destroying)continue;_6.destroy(false);_5++}
if(this.logIsDebugEnabled("destroys")){this.logDebug("clearDestroyQueue: "+_5+" direct destroy() calls ("+_2.length+" items), "+(isc.timeStamp()-_1)+"ms","destroys")}
isc.EH.$jq()}
,isc.A.outsetRect=function isc_c_Canvas_outsetRect(_1,_2){if(!_2)return _1;if(isc.isAn.Array(_1)){_1[0]-=_2;_1[1]-=_2;_1[2]+=2*_2;_1[3]+=2*_2;return _1}
_1.left-=_2;_1.top-=_2;_1.width+=2*_2;_1.height+=2*_2;return _1}
,isc.A.rectsIntersect=function isc_c_Canvas_rectsIntersect(_1,_2){var _3=_1[0],_4=_1[1],_5=_1[2],_6=_1[3],_7=_2[0],_8=_2[1],_9=_2[2],_10=_2[3],_11=((_3>_7+_9-1)||(_3+_5-1<_7)),_12=((_4>_8+_10-1)||(_4+_6-1<_8));return!_11&&!_12}
,isc.A.$0u=function isc_c_Canvas__forceNativeTabOrderUpdate(){if(!this.$2u){this.ns.Element.createAbsoluteElement("<DIV ID='$2v'"+" style='position:absolute;left:0px;top:-100px'>&nbsp;</DIV>");this.$2u=document.all["$2v"]}else{this.$2u.innerHTML="&nbsp;"}}
,isc.A.$vj=function isc_c_Canvas__addToTopLevelCanvasList(_1){if(!isc.isA.Canvas(_1)||_1.$2w!=null)return;this.$x3.add(_1);_1.$2w=this.$x3.length-1}
,isc.A.$v6=function isc_c_Canvas__removeFromTopLevelCanvasList(_1){if(!isc.isA.Canvas(_1)||_1.$2w==null)return;this.$x3[_1.$2w]=null;_1.$2w=null}
,isc.A.showClickMask=function isc_c_Canvas_showClickMask(_1,_2,_3){return this.ns.EH.showClickMask(_1,_2,_3)}
,isc.A.hideClickMask=function isc_c_Canvas_hideClickMask(_1){this.ns.EH.hideClickMask(_1)}
,isc.A.$yh=function isc_c_Canvas__placeRect(_1,_2,_3,_4,_5,_6){if(isc.isAn.Array(_3)){_3={left:_3[0],top:_3[1],width:_3[2],height:_3[3]}}else if(_3==null){_3={left:this.ns.EH.getX(),top:this.ns.EH.getY()}}
if(_3.width==null)_3.width=0;if(_3.height==null)_3.height=0;if(_4==null)_4="bottom";if(_5==null)_5=true;var _7=(_4=="bottom"||_4=="top");if(_7){if(_6=="inside-right")_6="right";if(_6!="right"&&_6!="outside-right"&&_6!="outside-left")_6="left"}else{if(_6=="inside-bottom")_6="bottom";if(_6!="bottom"&&_6!="outside-bottom"&&_6!="outside-top")_6="top"}
var _8=_3.left;if(_7){if(_6=="right")_8+=(_3.width-_1);else if(_6=="outside-right")_8+=_3.width;else if(_6=="outside-left")_8-=_1}else{if(_4=="left")_8-=_1;else _8+=_3.width}
var _9=_3.top;if(_7){if(_4=="top")_9-=_2;else _9+=_3.height}else{if(_6=="bottom")_9+=(_3.height-_2);else if(_6=="outside-bottom")_9+=_3.height;else if(_6=="outside-top")_9-=_2}
var _10=isc.Page.getWidth(),_11=isc.Page.getHeight(),_12=isc.Page.getScrollLeft(),_13=isc.Page.getScrollTop();var _14=_12-_8,_15=_8+_1-(_10+_12),_16=_13-_9,_17=_9+_2-(_11+_13);;if(_14<=0&&_15<=0&&_16<=0&&_17<=0){return[_8,_9]}
if(_14>0){if(_4=="left"&&!_5){if(_3.left+_3.width<_12){_8=_12}else{_8=_3.left+_3.width}}else{_8=_12}}else if(_15>0){if(_4=="right"&&!_5){if((_3.left-_1)>=_12){if(_3.left>(_12+_10))
_8=(_12+_10)-_1;else _8=_3.left-_1}}else{if(_10<_1){_8=_12}else{_8=_12+_10-_1}}}
if(_16>0){if(_4=="top"&&!_5){if(_3.top+_3.height<_13){_9=_13}else{_9=_3.top+_3.height}}else{_9=_13}}else if(_17>0){if(_4=="bottom"&&!_5){if((_3.top-_2)>=_13){if(_3.top>(_13+_11))
_9=(_13+_11)-_2;else _9=_3.top-_2}}else{if(_11<_2){_9=_13}else{_9=_13+_11-_2}}}
return[_8,_9]}
,isc.A.$kd=function isc_c_Canvas__handleUnload(){if(isc.Browser.isIE)this.$2x();var _1=isc.Log.logViewer;if(_1&&_1.logWindowLoaded()){_1._logWindow.openerUnloading();_1._logWindow=null}}
,isc.A.$2x=function isc_c_Canvas__clearDOMHandles(){var _1=this._canvasList;for(var i=0;i<_1.length;i++){var _3=_1[i];if(_3){if(_3.$ve){_3.$ve.eventProxy=null;_3.$ve=null}}}
return true}
,isc.A.snapToEdge=function isc_c_Canvas_snapToEdge(_1,_2,_3,_4,_5){var _6,_7,_8;if(isc.isAn.Array(_1)){_7=false;_8=[_1[1],_1[0]];_6=[_1[2],_1[3]]}else if(_3.masterElement){_7=(_3.percentBox==_3.$rz),_6=[_7?_1.getViewportWidth():_1.getVisibleWidth(),_7?_1.getViewportHeight():_1.getVisibleHeight()];_8=[_1.getTop()+(_7?(_1.getTopBorderSize()+_1.getTopMargin()):0),_1.getLeft()+(_7?(_1.getLeftBorderSize()+_1.getLeftMargin()):0)]}else if(isc.isA.Canvas(_5)){_7=(_3.percentBox==_3.$rz),_6=[_7?_5.getViewportWidth():_5.getVisibleWidth(),_7?_5.getViewportHeight():_5.getVisibleHeight()];_8=[_5.getPageTop()+(_7?(_5.getTopBorderSize()+_5.getTopMargin()):0),_5.getPageLeft()+(_7?(_5.getLeftBorderSize()+_5.getLeftMargin()):0)]}else{_7=true;_6=[_1.getViewportWidth(),_1.getViewportHeight()];_8=[0,0]}
var _9=isc.Canvas.$2y(_2,_8,_6,false);var _10=isc.Canvas.$2y((_4||_2),_9,[_3.getVisibleWidth(),_3.getVisibleHeight()],true);if(_3.snapOffsetLeft!=null)_10[1]+=_3.snapOffsetLeft;if(_3.snapOffsetTop!=null)_10[0]+=_3.snapOffsetTop;_3.moveTo(_10[1],_10[0]);_3.$k6=false}
,isc.A.$2y=function isc_c_Canvas__getSnapPoint(_1,_2,_3,_4){var _5=_3[0],_6=_3[1];var _7;if(_1=="TL")_7=[0,0];else if(_1=="T")_7=[0,_5/ 2];else if(_1=="TR")_7=[0,_5];else if(_1=="R")_7=[_6/ 2,_5];else if(_1=="BR")_7=[_6,_5];else if(_1=="B")_7=[_6,_5/ 2];else if(_1=="BL")_7=[_6,0];else if(_1=="L")_7=[_6/ 2,0];else if(_1=="C")_7=[_6/ 2,_5/ 2];else _7=[0,0];_7[0]=Math.floor(_7[0]);_7[1]=Math.floor(_7[1]);if(_4)return[_2[0]-_7[0],_2[1]-_7[1]];else return[_2[0]+_7[0],_2[1]+_7[1]]}
,isc.A.ariaEnabled=function isc_c_Canvas_ariaEnabled(){return false}
,isc.A.useLiteAria=function isc_c_Canvas_useLiteAria(){return false}
);isc.B._maxIndex=isc.C+47;isc.Canvas.registerStringMethods({resized:"deltaX,deltaY",showIf:"canvas",childRemoved:"child,name",peerRemoved:"peer,name",deparented:"oldParent,name",depeered:"oldMaster,name",parentMoved:"parent,deltaX,deltaY",moved:"deltaX,deltaY",focusChanged:"hasFocus",scrolled:null,hover:"",onDrop:"",visibilityChanged:"isVisible"});isc.Canvas.$2z=function(){var _1=isc.EH,_2={};for(var _3 in _1.eventTypes){this.registerStringMethods(_1.eventTypes[_3],_1.$ki);var _4=_1.eventTypes[_3];if(this.getInstanceProperty(_4)==null){_2[_4]=isc.Class.NO_OP}}
this.addMethods(_2)}
isc.Canvas.$2z();isc.defineClass("BackMask","Canvas");isc.A=isc.BackMask.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.autoDraw=false;isc.A.$nk=true;isc.A._generated=true;isc.A.useClipDiv=false;isc.A.hideUsingDisplayNone=isc.Browser.isMoz;isc.A.overflow=isc.Canvas.HIDDEN;isc.A.contents="<iframe width='100%' height='100%' border='0' frameborder='0' src=\""+isc.Page.getBlankFrameURL()+"\" marginwidth='0' marginheight='0' scrolling='no' tabIndex='-1' tabStop='false'></iframe>";isc.A.$rv=false;isc.A.$k6=false;isc.A.$k7=false;isc.A._redrawWithParent=false;isc.B.push(isc.A.masterMoved=function isc_BackMask_masterMoved(){this.masterElement.$uu()}
,isc.A.masterResized=function isc_BackMask_masterResized(){this.masterElement.$uu()}
,isc.A.draw=function isc_BackMask_draw(_1,_2,_3){if(this.suppressed)return this;if(!this.readyToDraw())return this;this.invokeSuper(isc.BackMask,this.$r0,_1,_2,_3);if(this.masterElement.overflow==isc.Canvas.VISIBLE)this.masterElement.$uu();return this}
,isc.A.show=function isc_BackMask_show(){if(!this.suppressed)this.invokeSuper(isc.BackMask,"show")}
);isc.B._maxIndex=isc.C+4;isc.defineClass("ScreenSpan","Canvas");isc.A=isc.ScreenSpan.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A._generated=true;isc.A.src="[SKINIMG]/blank.gif";isc.A.redrawOnResize=false;isc.A.overflow="hidden";isc.B.push(isc.A.getInnerHTML=function isc_ScreenSpan_getInnerHTML(){if(!this.$20){this.$20=isc.Browser.isIE&&isc.Browser.version>6?isc.Canvas.imgHTML(this.src,3200,2400):isc.Canvas.spacerHTML(3200,2400)}
return this.$20}
,isc.A.hide=function isc_ScreenSpan_hide(_1,_2,_3,_4){this.resizeTo(1,1);this.moveTo(null,-this.getHeight());return this.invokeSuper(isc.ScreenSpan,"hide",_1,_2,_3,_4)}
,isc.A.show=function isc_ScreenSpan_show(_1,_2,_3,_4){this.fitToScreen();isc.Page.setEvent("resize",this,isc.Page.FIRE_ONCE,"pageResized");return this.invokeSuper(isc.ScreenSpan,"show",_1,_2,_3,_4)}
,isc.A.pageResized=function isc_ScreenSpan_pageResized(){if(!this.isVisible())return;this.resizeTo(isc.Page.getWidth(),isc.Page.getHeight());this.fitToScreen();isc.Page.setEvent("resize",this,isc.Page.FIRE_ONCE,"pageResized")}
,isc.A.fitToScreen=function isc_ScreenSpan_fitToScreen(){var _1=Math.max(isc.Page.getWidth(),isc.Page.getScrollWidth()),_2=Math.max(isc.Page.getHeight(),isc.Page.getScrollHeight());this.resizeTo(_1,_2);this.moveTo(0,0)}
);isc.B._maxIndex=isc.C+5;isc.$21={getForm:function(_1){if(_1&&typeof _1=="object")return _1;var _2;if(_1!=null&&isc.Browser.isDOM){_2=document.getElementById(_1)}
if(_2!=null)return _2;if(_1==null)_1=0;if(_2==null)return document.forms[_1];return _2},getFormElementValue:function(_1,_2){var _3=this.getFormElement(_1,_2);if(!_3)return;switch(_3.type){case"radio":return(_3.checked?_3.value:null)
case"checkbox":return _3.checked;case"select-one":if(!_3.options||_3.options.length==0)return null;var _4=_3.options[_3.selectedIndex];return _4.value;case"select-multiple":var _5=[];for(var i=0,_7=_3.options.length;i<_7;i++){var _4=_3.options[i];if(_4.selected)
_5.add(_4.value)}
return _5;case"button":case"reset":case"submit":return null;default:return _3.value}},getFormValues:function(_1){var _2=this.getForm(_1);if(!_2)return null;var _3={};if(!_2.elements){this.logWarn("Form '"+_1+"' contains no elements - returning empty map for data.");return{}}
for(var i=0;i<_2.elements.length;i++){var _5=_2.elements[i];if(_5.name!=null){var _6=this.getFormElementValue(_2,_2.elements[i]);if(_6!=null)_3[_5.name]=_6}}
return _3},getFormElement:function(_1,_2){if(typeof _2=="object")return _2;var _3=this.getForm(_1);if(_3)return _3.elements[_2];return null}};isc.Canvas.addClassMethods(isc.$21)
isc.Canvas.addMethods(isc.$21)
isc.setAutoDraw=function(_1){if(_1==null)_1=true;isc.Canvas.addProperties({autoDraw:_1})};isc.allowDuplicateStyles=true;isc.defineClass("PrintCanvas","Canvas");isc.A=isc.PrintCanvas.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.redrawOnResize=false;isc.A.overflow="hidden";isc.A.useExplicitHeight=isc.Browser.isSafari;isc.A.printFrameURL="[HELPERS]printFrame.html";isc.B.push(isc.A.initWidget=function isc_PrintCanvas_initWidget(){this.Super("initWidget",arguments)}
,isc.A.resized=function isc_PrintCanvas_resized(){if(this.useExplicitHeight){var _1=this.getIFrameHandle();if(_1){_1.style.width=this.getInnerWidth();_1.style.height=this.getInnerHeight()}}}
,isc.A.getInnerHTML=function isc_PrintCanvas_getInnerHTML(){var _1="100%",_2="100%";if(this.useExplicitHeight){_1=this.getInnerWidth();_2=this.getInnerHeight()}
return"<iframe height='"+_2+"' width='"+_1+"' scrolling='auto' id='"+this.getIFrameID()+"' frameborder='0' src=\""+this.getPrintFrameURL(this.title)+"\"></iframe>"}
,isc.A.getIFrameID=function isc_PrintCanvas_getIFrameID(){return this.getID()+"$22"}
,isc.A.getPrintFrameURL=function isc_PrintCanvas_getPrintFrameURL(_1){return isc.Page.getURL(this.printFrameURL+"?id="+this.getID()+"&title="+(_1||""))}
,isc.A.getIFrameHandle=function isc_PrintCanvas_getIFrameHandle(){return document.getElementById(this.getIFrameID())}
,isc.A.getIFrameWindow=function isc_PrintCanvas_getIFrameWindow(){return this.getIFrameHandle().contentWindow}
,isc.A.iframeLoad=function isc_PrintCanvas_iframeLoad(){this.iframeLoaded=true}
,isc.A.setHTML=function isc_PrintCanvas_setHTML(_1,_2){if(!this.isDrawn()){this.$23={HTML:_1,callback:_2};return}
if(!this.iframeLoaded){this.delayCall("setHTML",[_1,_2],100);return}
var _3=this.getIFrameWindow();_3.assignHTML(_1);this.fireCallback(_2,["printPreview","callback"],[this,_2])}
,isc.A.draw=function isc_PrintCanvas_draw(){this.Super("draw",arguments);if(this.$23!=null){var _1=this.$23;this.$23=null;this.setHTML(_1.HTML,_1.callback)}}
,isc.A.setTitle=function isc_PrintCanvas_setTitle(_1){this.title=_1;if(!this.isDrawn()&&!this.iframeLoaded)return;delete this.iframeLoaded;if(this.isDrawn())this.redraw()}
,isc.A.printHTML=function isc_PrintCanvas_printHTML(_1,_2,_3){var _4=this;this.setTitle(_2);this.setHTML(_1,function(){_4.print()})}
,isc.A.print=function isc_PrintCanvas_print(){if(!this.isDrawn()){this.logWarn("print(): Attempt to print an undrawn PrintCanvas. Ignoring.");return}
if(!this.iframeLoaded){this.delayCall("print",[],100);return}
this.getIFrameWindow().doPrint()}
,isc.A.printComplete=function isc_PrintCanvas_printComplete(){}
);isc.B._maxIndex=isc.C+14;isc.A=isc.Canvas;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.printComponents=function isc_c_Canvas_printComponents(_1,_2,_3,_4){isc.Canvas.getPrintHTML(_1,_2,{target:this,methodName:"$24",title:_3,debugOnly:_4})}
,isc.A.$24=function isc_c_Canvas__printComponentHTML(_1,_2){var _3=_2.title,_4=_2.debugOnly;if(!this.$25)this.$25=isc.PrintCanvas.create({width:"100%",height:"100%",autoDraw:false,backgroundColor:"white"});this.$25.moveTo(null,-isc.Page.getHeight());if(!this.$25.isDrawn())this.$25.draw();this.$25.printHTML(_1,_3,_4)}
,isc.A.getPrintPreview=function isc_c_Canvas_getPrintPreview(_1,_2,_3,_4,_5){if(_3==null)_3={};_3.autoDraw=true;isc.Canvas.getPrintHTML(_1,_2,{target:this,methodName:"$26",origCallback:_4,previewProperties:_3},_5)}
,isc.A.$26=function isc_c_Canvas__createPrintPreview(_1,_2){var _3=isc.PrintCanvas.create(_2.previewProperties);_3.setHTML(_1,{target:this,methodName:"$27",origCallback:_2.origCallback})}
,isc.A.$27=function isc_c_Canvas__printPreviewGenerated(_1,_2){if(_2.origCallback){this.fireCallback(_2.origCallback,["printPreview"],[_1])}}
,isc.A.showPrintPreview=function isc_c_Canvas_showPrintPreview(_1,_2,_3,_4,_5){if(!isc.PrintWindow){isc.definePrintWindow()}
if(!isc.PrintWindow)return;if(_3==null)_3={};_3.autoDraw=false;if(_3.width==null)_3.width="100%";if(_3.height==null)_3.height="100%";if(_3.left==null)_3.left=0;if(_3.top==null)_3.top=0;if(!this.$28){this.$28=isc.PrintWindow.create(_3)}else{this.$28.setProperties(_3)}
this.$28.showPrintPreview(_1,_2,_4,_5)}
);isc.B._maxIndex=isc.C+6;isc.definePrintWindow=function(){if(!isc.Window){isc.logWarn("Attempting to create PrintWindow class with no defined Window class. "+"Ensure the required 'Containers' module is laoded");return}
isc.defineClass("PrintWindow","Window");isc.PrintWindow.addProperties({isModal:true,headerControls:["headerIcon","headerLabel","printButton","closeButton"],printButtonDefaults:{_constructor:"IButton",height:20,click:"this.creator.printClicked()"},showMinimizeButton:false,showShadow:false,title:"Print Preview",printButtonTitle:"Print",setPrintButtonTitle:function(_3){this.printButtonTitle=_3;if(this.printButton!=null)this.printButton.setTitle(_3)},initWidget:function(){this.printButtonDefaults.title=this.printButtonTitle;this.Super("initWidget",arguments)},showPrintPreview:function(_3,_4,_5,_6){if(!isc.isAn.Array(_3))_3=[_3];isc.Canvas.getPrintHTML(_3,_4,{target:this,methodName:"$29",origCallback:_5},_6)},$29:function(_3,_4){if(!this.previewPane){this.previewPane=this.createPreviewPane();this.previewPane.addProperties({title:this.title});this.addItem(this.previewPane)}else{this.previewPane.setTitle(this.title)}
this.setVisibility("hidden");if(!this.isDrawn())this.draw();this.previewPane.setHTML(_3,{target:this,methodName:"$27",origCallback:_4.origCallback})},$27:function(_3,_4){if(!this.isVisible())this.show();this.bringToFront();if(_4.origCallback){this.fireCallback(_4.origCallback,["printPreview","printWindow"],[_3,this])}},printClicked:function(){var _1=this.getPrintCanvas();if(!_1)return;_1.print()},createPreviewPane:function(_3){var _2=isc.PrintCanvas.create({width:"100%",height:"100%"});return _2},getPrintCanvas:function(){return this.previewPane},closeClick:function(){this.Super("closeClick",arguments);this.clear()}})}
isc.ClassFactory.defineInterface("DataBoundComponent");isc.A=isc.Canvas;isc.A.COPY="copy";isc.A.MOVE="move";isc.A.CLONE="clone";isc.A=isc.Canvas;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$3a="/";isc.B.push(isc.A.getFieldImageDimensions=function isc_c_Canvas_getFieldImageDimensions(_1,_2){var _3,_4;var _5,_6,_7;if(isc.isA.String(_1.imageWidth)){_5=_1.imageWidth}else{_3=_1.imageWidth}
if(isc.isA.String(_1.imageHeight)){_6=_1.imageHeight}else{_4=_1.imageHeight}
if(isc.isA.String(_1.imageSize)){_7=_1.imageSize}else{_3=_3||_1.imageSize;_4=_4||_1.imageSize}
if(_2!=null){_3=_3||_2[_5]||_2[_7];_4=_4||_2[_6]||_2[_7]}
return{width:_3,height:_4}}
,isc.A.$3b=function isc_c_Canvas__performActionOnValue(_1,_2,_3,_4,_5,_6,_7){if(!_3||_2==null||isc.isAn.emptyString(_2))return;var _8=_3;var _9=_2.contains(this.$3a);if(_9){_2=_2.trim(isc.Canvas.$3a);var _10=_2.split(this.$3a),_11=[],_12;if(_10[0]&&_3[_10[0]]===_12&&_1=="get"&&!_7)
{if(_4&&isc.ValuesManager&&isc.isA.ValuesManager(_4.valuesManager))
{return this.$3b(_1,_2,_4.valuesManager.getValues(),_4,_5,_6,true)}}
if(isc.isAn.emptyString(_10.last()))_10.length-=1;for(var i=0;i<_10.length;i++){if(isc.isAn.emptyString(_10[i]))continue;if(_3==null){_11.length=0;break}
_11.add(_3);if(i==_10.length-1){if(_1=="get"){return _3[_10[i]]}else if(_1=="clear"){delete _3[_10[i]]}else if(_1=="save"){_3[_10[i]]=_6}}else{var _14=_3[_10[i]];if(_14==_12){if(_1=="get"){return _12}else if(_1=="clear"){return}else if(_1=="save"){_14=_3[_10[i]]={}}}
_3=_14;if(isc.isAn.Array(_3)){var _15=null;var _16=(parseInt(_10[i+1])==_10[i+1])
if(_16){_15=parseInt(_10[i+1])
_10.removeAt(i+1)}else if(_4&&_4.selectionComponent){var _17=isc.Canvas.$3a,_18=_4,_19;for(var j=0;j<=i;j++){_17+=_10[j]+isc.Canvas.$3a}
_17=_17.trim(isc.Canvas.$3a);_18=_4.selectionComponent;while(_18){var _21=_18.dataPath;if(_21)_21=_21.trim(isc.Canvas.$3a);if(_17==_21){var _22=_18.getSelectedRecord();if(_22){_15=_18.getRecordIndex(_22)}else{_19=true}
break}
_18=_18.selectionComponent}
if(_15==null){if(!_19&&_5){_15=0}else{return}}}else{if(_5){_15=0}else{return}}
_3=_3[_15]}}}
if(_1=="clear"){for(var i=_11.length-1;i>0;i--){if(isc.isAn.emptyObject(_11[i])){delete _11[i-1][_10[i-1]]}}}}else{if(_1=="get")return _3[_2];else if(_1=="clear")delete _3[_2];else if(_1=="save")_3[_2]=_6}}
,isc.A.$3c=function isc_c_Canvas__clearFieldValue(_1,_2,_3,_4){this.$3b("clear",_1,_2,_3,_4,null,false)}
,isc.A.$3d=function isc_c_Canvas__saveFieldValue(_1,_2,_3,_4,_5){this.$3b("save",_1,_3,_4,_5,_2,false);return _3}
,isc.A.$3e=function isc_c_Canvas__getFieldValue(_1,_2,_3,_4){return this.$3b("get",_1,_2,_3,_4,null,false)}
,isc.A.$3f=function isc_c_Canvas__combineDataPaths(_1,_2){if(_1==null&&_2==null)return null;if(isc.isA.String(_2)&&_2.startsWith(this.$3a))return _2;if(_1==null)return""+_2;if(_2==null)return _1+"";if(isc.isA.String(_1)&&_1.endsWith(this.$3a)){return _1+_2}else{return _1+this.$3a+_2}}
,isc.A.evalViewState=function isc_c_Canvas_evalViewState(_1,_2,_3,_4){if(isc.isA.String(_1)){var _5=_1;try{_1=isc.eval(_1)}catch(e){if(!_3){var _6="Unable to parse "+_2+" object passed in: "+isc.Log.echo(_5)+" Ignoring."
if(!_4||_4.logWarn==null){if(_4)_6+=" [target:"+isc.Log.echo(_4)+"]";this.logWarn(_6)}else{_4.logWarn(_6)}}
return}}
return _1}
);isc.B._maxIndex=isc.C+7;isc.A=isc.Canvas.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.showComplexFields=true;isc.A.exportIncludeSummaries=true;isc.A.ignoreEmptyCriteria=true;isc.A.dragRecategorize=false;isc.A.duplicateDragMessage="Duplicates not allowed";isc.A.showOfflineMessage=true;isc.A.offlineMessage="This data not available while offline";isc.A.offlineMessageStyle="offlineMessage";isc.A.offlineSaveMessage="Data cannot be saved because you are not online";isc.A.addDropValues=true;isc.A.fieldIdProperty="name";isc.A.dataArity="multiple";isc.A.autoTrackSelection=true;isc.A.$3g={date:true,DateItem:true};isc.A.$3h="false;";isc.A.$3i="false";isc.A.styleOpposite="cellHiliteOpposite";isc.A.hiliteProperty="_hilite";isc.A.hiliteMarker="$3j";isc.A.$3k=0;isc.A.$3l=[];isc.A.dragDataAction=isc.Canvas.MOVE;isc.A.dragTrackerStyle="gridDragTracker";isc.A.canExport=true;isc.A.canPrint=true;isc.A.panelControls=["action:edit","action:editNew","action:sort","action:export","action:print"];isc.A.dbcProperties=["autoFetchData","autoFetchTextMatchStyle","autoFetchAsFilter","dataSource"];isc.A.badFormulaResultValue=".";isc.A.missingSummaryFieldValue="-";isc.A.canAddFormulaFields=false;isc.A.addFormulaFieldText="Add formula column...";isc.A.editFormulaFieldText="Edit formula...";isc.A.removeFormulaFieldText="Remove formula";isc.A.canAddSummaryFields=false;isc.A.addSummaryFieldText="Add summary column...";isc.A.editSummaryFieldText="Edit summary format...";isc.A.removeSummaryFieldText="Remove summary column..";isc.A.formulaFieldNamePrefix="formulaField";isc.A.summaryFieldNamePrefix="summaryField";isc.A.uniqueFieldNamePrefix="field";isc.A.exportDataChunkSize=50;isc.A.emptyExportMessage="You are attempting to export an empty dataset";isc.A.unknownErrorMessage="Invalid value";isc.A.$3m=["isInteger","isFloat","isBoolean","isString"];isc.A.$3n="partial";isc.A.$3o={};isc.A.$3p=null;isc.B.push(isc.A.setValuesManager=function isc_Canvas_setValuesManager(_1){if(_1)_1.addMember(this)}
,isc.A.initializeValuesManager=function isc_Canvas_initializeValuesManager(){var _1=this.valuesManager;delete this.valuesManager;if(_1!=null){if(isc.ValuesManager==null){this.logWarn("Widget initialized with specified 'valuesManager' property but "+"ValuesManager class is not loaded. This functionality requires the "+"Forms module.");return}
if(isc.isA.ValuesManager(_1)){_1.addMember(this)}else if(isc.isA.ValuesManager(window[_1])){window[_1].addMember(this)}else if(isc.isA.String(_1)){isc.ValuesManager.create({ID:_1,dataSource:this.dataSource,members:[this]})}else{this.logWarn("Widget initialized with invalid 'valuesManager' property:"+isc.Log.echo(_1)+", clearing this property out")}}}
,isc.A.setDataPath=function isc_Canvas_setDataPath(_1){this.dataPath=_1;if(this.getFields==null||this.getFields()==null)return;if(_1==null){delete this.$3q;if(this.valuesManager&&this.$3r){this.valuesManager.removeMember(this);delete this.$3r}
return}
var _2;var _3=this;while(_3&&(!_3.valuesManager||_3.$3r)&&!_3.dataSource)
{if(_3.dataPath){if(_2){_2=isc.Canvas.$3f(_3.dataPath,_2)}else{_2=_3.dataPath}}
_3=_3.parentElement}
this.$3q=_2;if(_3){if(_3!=this||!this.dataSource){if(_3.valuesManager==null){_3.createDefaultValuesManager()}
var _4=isc.isA.DynamicForm(this)?this.$3s:this.getFields();_4=_4||this.getFields();if(_3.valuesManager.getDataSource()){this.setDataSource(_3.valuesManager.getDataSource(),_4)}
_3.valuesManager.addMember(this,true)}}}
,isc.A.getFullDataPath=function isc_Canvas_getFullDataPath(){var _1=this.$3q||this.dataPath;if(!_1&&this.masterElement){return this.masterElement.$3q||this.masterElement.dataPath}
return _1}
,isc.A.buildFieldDataPath=function isc_Canvas_buildFieldDataPath(_1,_2){var _3=_2.dataPath||_2.name;if(_1&&_3&&!_3.startsWith("/")){_3=_1+"/"+_3}
return!_3?null:_3.replace(/^\/*/,"")}
,isc.A.createDefaultValuesManager=function isc_Canvas_createDefaultValuesManager(_1){if(!_1)_1=[];_1.add(this);isc.ValuesManager.create({members:_1,ID:this.getID()+"$3t",dataSource:this.dataSource})}
,isc.A.getDataPathField=function isc_Canvas_getDataPathField(_1){if(!_1)return null;var _2=this.getDataSource(),_3=_1.split(isc.slash),_4;if(!_2)return;for(var i=0;i<_3.length;i++){var _6=_3[i],_7=_2.getField(_6);_2=_7?(_2.getSchema(_7.type)||_2):_2;if(_7==null){this.logWarn("Unable to find dataSource field matching specified dataPath: '"+_1+"'");return}}
return _7}
,isc.A.registerWithDataView=function isc_Canvas_registerWithDataView(_1){if(!this.inputDataPath)return;_1=this.parentElement;while(_1&&!isc.isA.DataView(_1))_1=_1.parentElement;if(!_1){this.logWarn("Component initialized with an inputDataPath property, but no DataView "+"was found in the parent hierarchy. inputDataPath is only applicable to "+"DataBoundComponents and FormItems being managed by a DataView");return}
_1.registerItem(this)}
,isc.A.bindToDataSource=function isc_Canvas_bindToDataSource(_1,_2){if(this.dataPath)this.setDataPath(this.dataPath);if(this.dataSource==null&&this.data!=null)this.dataSource=this.data.dataSource;var _3=this.fields||this.items;if(isc.isAn.Array(_3))this.originalFields=_3.duplicate();var _4=this.getDataSource();if(_4==null&&this.valuesManager&&this.valuesManager.getDataSource){_4=this.valuesManager.getDataSource()}
if(_4!=null&&isc.isA.String(_4)){this.logWarn("unable to look up DataSource: "+_4+", databinding will not be used");return _1}
var _5=(_1==null||_1.length==0),_6;if(_4){var _7=this.useFlatFields;if(_7==null)_7=_4.useFlatFields;_6=_7?_4.getFlattenedFields():_4.getFields()}
if(_4==null||_6==null){if(_1!=null&&isc.SimpleType){for(var i=0;i<_1.length;i++){if(_1[i].type==null&&this.$3g[_1[i].editorType]==true)
{_1[i].type="date"}
isc.SimpleType.addTypeDefaults(_1[i])}}
this.addFieldValidators(_1);return _1}
if(this.doNotUseDefaultBinding)return[];if(_4!=null&&_5){_1=[];for(var _9 in _6){var _10=_6[_9];if(!this.shouldUseField(_10,_4))continue;_1.add(isc.addProperties({},_10))}
this.addFieldValidators(_1);return _1}
if(_4!=null&&!_5){for(var i=_1.length-1;i>=0;i--){var _10=_1[i];if(_10==null)continue;var _11=(_10.name!=null)?_4.getField(_10.name):null;if(_11&&_11.canView===false){this.logInfo("Dropping explicitly-named field "+_10.name+" because it is marked canView: false");_1.removeAt(i)}
if(_10.type==null&&this.$3g[_10.editorType]==true){var _12=_10.name;var _11=(_12!=null)?_4.getField(_12):null;if(_11==null||_11.type==null){_10.type="date"}}
if(_10.type!=null)isc.SimpleType.addTypeDefaults(_10)}
if(this.useAllDataSourceFields||_2){var _13=this;var _14=_4.combineFieldOrders(_6,_1,function(_10,_4){return _13.shouldUseField(_10,_4)});for(var i=0;i<_14.length;i++){var _10=_14[i];if(!_1.containsProperty("name",_10.name)){if(_2&&_10.showIf==null){_10.showIf="return false"}}else{if(_10.includeFrom!=null&&_4.getField(_10.name)==null){this.$3u(_10)}}}
this.addFieldValidators(_14);return _14}else{for(var i=0;i<_1.length;i++){var _10=_1[i];if(!_10)continue;_10=this.combineFieldData(_10)}
this.addFieldValidators(_1);return _1}}}
,isc.A.combineFieldData=function isc_Canvas_combineFieldData(_1){var _2=this.getDataSource();if(this.getFullDataPath()||_1.dataPath){var _3=this.buildFieldDataPath(this.getFullDataPath(),_1);isc.DataSource.combineFieldData(_1,this.getDataPathField(_3));return _1}else if(_2!=null&&_2.getField(_1.name)){return _2.combineFieldData(_1)}else if(_1.includeFrom!=null){return this.$3u(_1)}
return _1}
,isc.A.$3u=function isc_Canvas__combineIncludeFromFieldData(_1){var _2=_1.includeFrom.split(".");if(_2==null||_2.length!=2){this.logWarn("This component includes a field with includeFrom set to:"+_1.includeFrom+". Format not understood.")}else{var _3=isc.DataSource.get(_2[0]),_4=_2[1];if(_3==null){this.logWarn("Field specifies includeFrom:"+_1.includeFrom+". Unable to find dataSource with ID:"+_2[0])}else{if(_1.name==null)_1.name=_4;return _3.combineFieldData(_1,_3.getField(_4))}}}
,isc.A.shouldUseField=function isc_Canvas_shouldUseField(_1,_2){if(_1.canView===false)return false;if(_1.hidden&&!this.showHiddenFields)return false;if(_1.canFilter==false&&this.showFilterFieldsOnly){return false}
if(_1.detail&&!this.showDetailFields)return false;if(!this.showComplexFields&&_2.fieldIsComplexType(_1.name))return false;return true}
,isc.A.addFieldValidators=function isc_Canvas_addFieldValidators(_1){if(_1==null)return;for(var i=0;i<_1.length;i++){var _3=_1[i];if(_3.required){var _4=this.getRequiredValidator(_3),_5=_4.errorMessage;if(!_3.validators){_3.validators=[_4]}else{if(!isc.isAn.Array(_3.validators)){_3.validators=[_3.validators]}
if(!_3.validators.containsProperty("type",_4.type)&&!_3.validators.containsProperty("_constructor",_4.type))
{if(_3.validators.$3v){_3.validators=_3.validators.duplicate()}
_3.validators.add(_4)}else if(_5!=null){var _6=this.getDataSource(),v=(_3.validators.find("type",_4.type)||_3.validators.find("_constructor",_4.type));if(v.errorMessage==null||(_6&&v.errorMessage==_6.requiredMessage)){v.errorMessage=_5}}}}else if(_3.required==false){var _8=_3.validators;if(_3.validators!=null){var _9=_3.validators.findIndex("type","required");if(_9!=-1){_3.validators.removeAt(_9)}}}
if(_3.multiple&&_3.validateEachItem==null)_3.validateEachItem=true}}
,isc.A.getRequiredValidator=function isc_Canvas_getRequiredValidator(_1){var _2={type:"required"},_3=_1.requiredMessage||this.requiredMessage;if(_3!=null)_2.errorMessage=_3;return _2}
,isc.A.getAllFields=function isc_Canvas_getAllFields(){return this.completeFields||this.fields}
,isc.A.getField=function isc_Canvas_getField(_1){if(!this.fields)return null;return isc.Class.getArrayItem(_1,this.fields,this.fieldIdProperty)}
,isc.A.getFieldNum=function isc_Canvas_getFieldNum(_1){if(!this.fields)return-1;if(isc.isA.Object(_1)&&(_1[this.fieldIdProperty]!=null)){_1=_1[this.fieldIdProperty]}
return isc.Class.getArrayItemIndex(_1,this.fields,this.fieldIdProperty)}
,isc.A.isXMLRequired=function isc_Canvas_isXMLRequired(_1){if(!_1||!this.useXMLRequired||!_1.xmlRequired)return false;if(!_1.dataPath)return true;var _2=this.getDataSource();if(!_2)return true;var _3=_1.dataPath.split(isc.slash),_1;for(var i=0;i<_3.length;i++){var _5=_3[i];if(!_2)return true;_1=_2.getField(_5);if(!_1)return true;if(_1.xmlMinOccurs!=null&&_1.xmlMinOccurs<1){return false}
_2=_2.getSchema(_1.type)}
return true}
,isc.A.evalViewState=function isc_Canvas_evalViewState(_1,_2,_3){return isc.Canvas.evalViewState(_1,_2,_3,this)}
,isc.A.getFieldState=function isc_Canvas_getFieldState(_1){var _2=[];var _3=this.getAllFields();if(_3){for(var i=0;i<_3.length;i++){var _5=_3[i];if(_5.excludeFromState)continue;var _6=_5[this.fieldIdProperty],_7=this.getStateForField(_6,_1);_2.add(_7)}}
return isc.Comm.serialize(_2,false)}
,isc.A.getStateForField=function isc_Canvas_getStateForField(_1,_2){var _3=this.getAllFields().find(this.fieldIdProperty,_1),_4={name:_1};if(_3.frozen==true)_4.frozen=true;if(!this.fieldShouldBeVisible(_3))_4.visible=false;if(_3.userFormula)_4.userFormula=_3.userFormula;if(_3.userSummary)_4.userSummary=_3.userSummary;if(_2||_3.userSummary||_3.userFormula){_4.title=_3.title}
if(this.getSpecifiedFieldWidth)_4.width=this.getSpecifiedFieldWidth(_3);if(_3.autoFitWidth)_4.autoFitWidth=true;return _4}
,isc.A.$3w=function isc_Canvas__setFieldState(_1,_2){if(_1==null)return;var _3=this.getAllFields();var _4=_3.getProperty(this.fieldIdProperty),_5=[];for(var i=0;i<_1.length;i++){var _7=_1[i],_8=_3.find(this.fieldIdProperty,_7.name);if(_8==null){if(_7.userFormula||_7.userSummary){_8={};_8[this.fieldIdProperty]=_7.name}else continue}
_4.remove(_7.name);if(_7.visible==false){_8.showIf=this.$3i}else{_8.showIf=null;_8.detail=false}
if(_7.width!=null&&(!isNaN(_7.width)||_7.width=="*"))_8.width=_7.width;_8.frozen=_7.frozen;if(_7.title)_8.title=_7.title;if(_7.userFormula!=null)_8.userFormula=_7.userFormula;if(_7.userSummary!=null)_8.userSummary=_7.userSummary;if(_7.autoFitWidth!=_8.autoFitWidth)_8.autoFitWidth=_7.autoFitWidth;_5.add(_8)}
for(var i=0;i<_4.length;i++){var _9=_4[i],_10=_3.findIndex(this.fieldIdProperty,_9),_8=_3[_10],_11=_3[_10-1];if(_2)_8.showIf=this.$3i;if(_11!=null){var _12=_5.indexOf(_11);if(_12!=-1){_5.addAt(_8,_12+1);continue}}
if(this.fieldShouldBeVisible(_8,_10)&&!_2){_5.addAt(_8,this.$3x(_5)+1)}else{_5.add(_8)}}
return _5}
,isc.A.fieldStateChanged=function isc_Canvas_fieldStateChanged(){}
,isc.A.$3x=function isc_Canvas__lastVisibleFieldIndex(_1){if(_1==null)_1=this.completeFields;var _2=this.getVisibleFields(_1);if(_2.length==0)return-1;return _1.lastIndexOf(_2.last())}
,isc.A.getVisibleFields=function isc_Canvas_getVisibleFields(_1){var _2=[];for(var i=0;i<_1.length;i++){var _4=_1[i];if(_4==null)continue;if(this.fieldShouldBeVisible(_4,i))_2.add(_4)}
return _2}
,isc.A.fieldShouldBeVisible=function isc_Canvas_fieldShouldBeVisible(_1,_2){if(_1.showIf!=null){if(_1.showIf==this.$3i||_1.showIf==this.$3h)return false;isc.Func.replaceWithMethod(_1,"showIf","list,field,fieldNum");if(!_1.showIf(this,_1,_2))return false}
return true}
,isc.A.setValueMap=function isc_Canvas_setValueMap(_1,_2){if(!isc.isAn.Object(_1))_1=this.getField(_1);if(!_1)return;_1.valueMap=_2}
,isc.A.setDataSource=function isc_Canvas_setDataSource(_1,_2){if(isc.$dd)arguments.$de=this;this.dataSource=_1||this.dataSource;if(this.setFields)this.setFields(_2);if(this.dataSource){if(this.isA("DynamicForm"))this.setData({});else this.setData([])}
this.markForRedraw("bind")}
,isc.A.bind=function isc_Canvas_bind(_1,_2){this.setDataSource(_1,_2)}
,isc.A.getDataSource=function isc_Canvas_getDataSource(){if(isc.isA.String(this.dataSource)){if(this.serviceNamespace||this.serviceName){this.dataSource=this.lookupSchema()}else{var _1=isc.DS.get(this.dataSource);if(_1!=null)return _1;_1=this.getWindow()[this.dataSource];if(_1&&isc.isA.DataSource(_1))return(this.dataSource=_1)}}
return this.dataSource}
,isc.A.setData=function isc_Canvas_setData(_1){this.data=_1}
,isc.A.lookupSchema=function isc_Canvas_lookupSchema(){var _1;if(this.serviceName)_1=isc.WebService.getByName(this.serviceName,this.serviceNamespace);else _1=isc.WebService.get(this.serviceNamespace);if((this.serviceNamespace||this.serviceName)&&_1==null){this.logWarn("Could not find WebService definition: "+(this.serviceName?"serviceName: "+this.serviceName:"")+(this.serviceNamespace?"   serviceNamespace: "+this.serviceNamespace:""))}
if(!isc.isA.String(this.dataSource)){this.logWarn("this.dataSource was not a String in lookupSchema");return}
var _2;if(_1)_2=_1.getSchema(this.dataSource);return _2||this.dataSource}
,isc.A.fieldValuesAreEqual=function isc_Canvas_fieldValuesAreEqual(_1,_2,_3){if(_2==_3)return true;if(_1==null)return false;if(_1.type=="date"){if(isc.isA.Date(_2)&&isc.isA.Date(_3)){return(Date.compareLogicalDates(_2,_3)==0)}}else if(_1.type=="datetime"){if(isc.isA.Date(_2)&&isc.isA.Date(_3)){return(Date.compareDates(_2,_3)==0)}}else if(_1.type=="valueMap"){if(isc.isAn.Array(_2)&&isc.isAn.Array(_3)){return _2.equals(_3)}else if(isc.isAn.Object(_2)&&isc.isAn.Object(_3)){for(var i in _2){if(_3[i]!=_2[i])return false}
for(var j in _3){if(_2[j]!=_3[j])return false}
return true}}
return false}
,isc.A.setFields=function isc_Canvas_setFields(_1){_1=this.bindToDataSource(_1);this.fields=_1}
,isc.A.getSerializeableFields=function isc_Canvas_getSerializeableFields(_1,_2){_1.addList(["zIndex","data"]);if(this.ID&&this.ID.startsWith("isc_"))_1.add("ID");if(this.dataSource)_1.addList(["fields","items"]);if(this.getClassName()!="Canvas"&&this.getClassName()!="Layout"){_1.add("children")}
return this.Super("getSerializeableFields",arguments)}
,isc.A.addField=function isc_Canvas_addField(_1,_2,_3){if(_1==null)return;if(_3==null)_3=(this.fields||this.items||isc.$ag);_3=_3.duplicate();var _4=this.getField(_1.name);if(_4)_3.remove(_4);if(_2==null||_2>_3.length)_2=_3.length;_3.addAt(_1,_2);this.setFields(_3)}
,isc.A.removeField=function isc_Canvas_removeField(_1,_2){if(_2==null)_2=(this.fields||this.items||isc.$ag);_2=_2.duplicate();var _3=_1.name?_1.name:_1;_2.remove(_2.find("name",_3));this.setFields(_2)}
,isc.A.setCanEdit=function isc_Canvas_setCanEdit(_1){this.canEdit=_1}
,isc.A.filterData=function isc_Canvas_filterData(_1,_2,_3){this.$3y("filter",_1,_2,_3)}
,isc.A.fetchData=function isc_Canvas_fetchData(_1,_2,_3){if(!_3)_3={};if(!_3.textMatchStyle)_3.textMatchStyle="exact";this.$3y("fetch",_1,_2,_3)}
,isc.A.$3z=function isc_Canvas__canExportField(_1){return(this.canExport!=false&&_1.canExport!=false&&!_1.userFormula&&!_1.userSummary&&!_1.hidden)}
,isc.A.exportData=function isc_Canvas_exportData(_1,_2){if(!_1)_1={};var _3=this.getSort();if(_3)_1.sortBy=isc.DS.getSortBy(_3);else if(this.sortField)_1.sortBy=(!this.sortDirection?"-":"")+this.sortField;if(!_1.textMatchStyle){var _4=this.data.context;if(_4&&_4.textMatchStyle)_1.textMatchStyle=_4.textMatchStyle}
if(!this.exportAll&&!_1.exportFields){var _5=this.exportFields;if(!_5){_5=[];for(var i=0;i<this.fields.length;i++){var _7=this.fields.get(i);if(this.$3z(_7)){_5.add(_7.name);if(_7.displayField&&!_7.optionDataSource)_5.add(_7.displayField)}}}
if(_5&&_5.length>0)_1.exportFields=_5}
var _8=_1.exportFields||this.exportFields||this.fields;var _9={};for(var i=0;i<_8.length;i++){var _7=_8[i];var _10;if(isc.isA.String(_7)){_10=_7;_7=this.getField(_10)}
if(_7){_9[_7.name]=_7.exportTitle||_7.title}else{_9[_10]=_10}}
_1.exportFieldTitles=_9;this.getDataSource().exportData(this.getCriteria(),_1,_2)}
,isc.A.setCriteria=function isc_Canvas_setCriteria(_1){if(this.data&&this.data.setCriteria)this.data.setCriteria(_1);else this.initialCriteria=_1}
,isc.A.getCriteria=function isc_Canvas_getCriteria(){if(!this.isDrawn()&&(!this.data||this.data.getLength()==0)){return isc.shallowClone(this.initialCriteria)}
else if(this.data&&this.data.getCriteria){if(isc.isA.Tree(this.data)){return isc.shallowClone(this.data.getCriteria(this.getDataSource()))}else{return isc.shallowClone(this.data.getCriteria())}}else return null}
,isc.A.doInitialFetch=function isc_Canvas_doInitialFetch(){var _1=false;if(this.autoFetchData&&!this.$30&&this.fetchData){if(!this.dataSource){this.logWarn("autoFetchData is set, but no dataSource is specified, can't fetch")}else{_1=!isc.RPCManager.startQueue();this.fetchData(this.getInitialCriteria(),null,this.getInitialFetchContext());this.$30=true}}
return _1}
,isc.A.getInitialCriteria=function isc_Canvas_getInitialCriteria(){return this.initialCriteria}
,isc.A.getInitialFetchContext=function isc_Canvas_getInitialFetchContext(){var _1={};_1.textMatchStyle=this.autoFetchTextMatchStyle;return _1}
,isc.A.fetchRelatedData=function isc_Canvas_fetchRelatedData(_1,_2,_3,_4){var _5=isc.isA.DataSource(_2)?_2:isc.isA.String(_2)?isc.DS.get(_2):isc.isA.Canvas(_2)?_2.dataSource:null;if(!_5){this.logWarn("schema not understood: "+this.echoLeaf(_2));return}
var _6=this.getDataSource().getTreeRelationship(_5);var _7={};_7[_6.parentIdField]=_1[_6.idField];this.fetchData(_7,_3,_4)}
,isc.A.clearCriteria=function isc_Canvas_clearCriteria(_1,_2){this.$3y("filter",null,_1,_2)}
,isc.A.$3y=function isc_Canvas__filter(_1,_2,_3,_4){if(isc.$dd)arguments.$de=this;if(_4==null&&isc.isAn.Object(_3)&&_3.methodName==null)
{_4=_3;_3=null}
_4=this.buildRequest(_4,_1,_3);if(this.onFetchData!=null){this.onFetchData(_2,_4)}
var _5=this.getAllFields();if(_5!=null){for(var i=0;i<_5.length;i++){if(_5[i].includeFrom!=null&&this.getDataSource().getField(_5[i].name)==null)
{if(_4.additionalOutputs==null)_4.additionalOutputs="";else _4.additionalOutputs+=",";_4.additionalOutputs+=[_5[i].name,_5[i].includeFrom].join(":")}}}
if(_2==null){_2={}}else if(isc.isA.Class(_2)){_2=isc.DynamicForm.getFilterCriteria(_2)}
this.filterWithCriteria(_2,_4.operation,_4)}
,isc.A.filterWithCriteria=function isc_Canvas_filterWithCriteria(_1,_2,_3){_3.prompt=(_3.prompt||isc.RPCManager.fetchDataPrompt);var _4=_1;if(this.ignoreEmptyCriteria){_4=isc.DataSource.filterCriteriaForFormValues(_1)}else{_4=isc.addProperties({},_4)}
_4=isc.DS.checkEmptyCriteria(_4);var _5=this.getData();if(this.useExistingDataModel(_1,_2,_3)){var _6=this.updateDataModel(_4,_2,_3);if(_6!=null)_5=_6}else{_5=this.createDataModel(_4,_2,_3)}
this.setData(_5);if(!_3.$31&&this.requestVisibleRows!=null){var _7=this.data,_8=_7.fetchDelay;_7.fetchDelay=0;this.requestVisibleRows();_7.fetchDelay=_8}}
,isc.A.useExistingDataModel=function isc_Canvas_useExistingDataModel(_1,_2,_3){var _4=this.getData();if(!(isc.isA.ResultSet(_4)||isc.isA.ResultTree(_4))){_4=this.originalData;if(_4==null)return false;if(!isc.isA.ResultSet(_4)&&!isc.isA.ResultTree(_4))return false}
var _5=_4.getOperationId("fetch");return _5==null||_5==_2.ID}
,isc.A.createDataModel=function isc_Canvas_createDataModel(_1,_2,_3){if(this.logIsInfoEnabled("ResultSet")){this.logInfo("Creating new isc.ResultSet for operation '"+_2.ID+"' with filterValues: "+this.echoFull(_1),"ResultSet")}
var _4=this.getDataSource();if(!_4){this.logWarn("No DataSource or invalid DataSource specified, can't create data model"+this.getStackTrace());return null}
var _5=this.dataProperties||{};if(_5.context)_3=isc.addProperties({},_5.context,_3);if(this.dataFetchDelay)_5.fetchDelay=this.dataFetchDelay;isc.addProperties(_5,{operation:_2,filter:_1,context:_3,componentId:this.ID});if(this.getSort!=null){var _6=this.getSort();if(_6!=null&&_6.length>0){_5.$32=_6;_5.$33=isc.DS.getSortBy(_5.$32)}}
return _4.getResultSet(_5)}
,isc.A.updateDataModel=function isc_Canvas_updateDataModel(_1,_2,_3){this.logDebug("Setting filter to: "+this.echoFull(_1));var _4=this.getData();if(!isc.isA.ResultSet(_4))_4=this.originalData;if(!isc.isA.ResultSet(_4)){return}
_4.setContext(_3);if(!_4.willFetchData(_1))delete _3.afterFlowCallback;_4.setCriteria(_1);return _4}
,isc.A.requestVisibleRows=function isc_Canvas_requestVisibleRows(){return this.data.get(0)}
,isc.A.invalidateCache=function isc_Canvas_invalidateCache(){if(this.data&&this.data.invalidateCache!=null)return this.data.invalidateCache();else if(this.isGrouped&&isc.isA.ResultSet(this.originalData)){this.originalData.invalidateCache();this.regroup()}}
,isc.A.willFetchData=function isc_Canvas_willFetchData(_1,_2){var _3=this.data;if(_3&&_3.willFetchData==null&&this.originalData!=null)_3=this.orginalData;if(_3&&_3.willFetchData!=null){return _3.willFetchData(_1,_2)}
return true}
,isc.A.findByKey=function isc_Canvas_findByKey(_1){return this.data.findByKey(_1)}
,isc.A.shouldSaveLocally=function isc_Canvas_shouldSaveLocally(){return(!this.dataSource||this.getFullDataPath()!=null||this.saveLocally)}
,isc.A.addData=function isc_Canvas_addData(_1,_2,_3){return this.$34("add",_1,_2,_3)}
,isc.A.updateData=function isc_Canvas_updateData(_1,_2,_3){return this.$34("update",_1,_2,_3)}
,isc.A.removeData=function isc_Canvas_removeData(_1,_2,_3){return this.$34("remove",_1,_2,_3)}
,isc.A.$34=function isc_Canvas__performDSOperation(_1,_2,_3,_4){if(isc.$dd)arguments.$de=this;if(_4==null&&isc.isAn.Object(_3)&&_3.methodName==null)
{_4=_3;_3=null}
if(this.shouldSaveLocally()||this.getDataSource()==null){if(_1=="update"){var _5=this.getDataSource();if(!_5){isc.logWarn("Update by primary key cannot be performed without a DataSource."+"Modify the record directly instead");return}
var _6=this.data.get(_5.findByKeys(_2,this.data));isc.addProperties(_6,_2);return this.data.dataChanged()}else if(_1=="add"){if(this.originalData){this.originalData.add(_2);this.dataChanged("add",null,null,_2)}else{if(isc.isA.Tree(this.data)){var _7=this.data.getParent(_2)||this.data.getRoot();this.data.add(_2,_7)}else{this.data.add(_2)}}
return}}
_4=this.buildRequest(_4,_1);return this.getDataSource().performDSOperation(_1,_2,_3,_4)}
,isc.A.removeSelectedData=function isc_Canvas_removeSelectedData(_1,_2){if(_2==null&&isc.isAn.Object(_1)&&_1.methodName==null)
{_2=_1;_1=null}
var _3=this.getSelection();if(isc.isA.ListGrid(this)&&this.canEdit&&this.selectOnEdit&&(_3==null||_3.length==0)&&this.getEditRow()!=null&&this.getRecord(this.getEditRow())==null)
{this.discardEdits(this.getEditRow());return}
if(this.dataSource==null||this.shouldSaveLocally()){if(this.data){this.data.removeList(this.getSelection());if(_1)this.fireCallback(_1)}
return}
var _4=this.buildRequest(_2,"remove",_1),_5=this.getDataSource();if(_3.length>0)this.deleteRecords(_3,_4.operation,_4,_5)}
,isc.A.deleteRecords=function isc_Canvas_deleteRecords(_1,_2,_3,_4){isc.addProperties(_3,{prompt:(_3.prompt||isc.RPCManager.removeDataPrompt)});var _5=isc.RPCManager.startQueue();if(!isc.isAn.Array(_1))_1=[_1];for(var i=0;i<_1.length;i++){if(_1[i].$35)continue;_4.performDSOperation(_2.type,_1[i],null,_3)}
if(!_5)isc.RPCManager.sendQueue()}
,isc.A.createSelectionModel=function isc_Canvas_createSelectionModel(){if(this.selection)this.destroySelectionModel();if(this.canSelectCells){var _1=[];if(this.numRows!=null){for(var i=0;i<this.numRows;i++){_1[i]={}}}}else{var _1=this.data}
var _3,_4={ID:this.getID()+"_selection",data:_1,target:this,selectionProperty:this.selectionProperty,simpleDeselect:this.simpleDeselect,dragSelection:this.canDragSelect};if(this.canSelectCells&&this.fields!=null)_4.numCols=this.fields.length;if(this.recordEnabledProperty!=null)_4.enabledProperty=this.recordEnabledProperty;if(this.recordCanSelectProperty!=null)_4.canSelectProperty=this.recordCanSelectProperty;if(this.cascadeSelection!=null)_4.cascadeSelection=this.cascadeSelection;if(this.data.getNewSelection){_3=this.data.getNewSelection(_4)}
if(_3==null){if(this.canSelectCells){_3=isc.CellSelection.create(_4)}else{_3=isc.Selection.create(_4)}}
this.selection=_3}
,isc.A.destroySelectionModel=function isc_Canvas_destroySelectionModel(){if(!this.selection)return;if(this.selection.destroy)this.selection.destroy();delete this.selection}
,isc.A.removeSelectionMarkers=function isc_Canvas_removeSelectionMarkers(_1){var _2=true;if(!isc.isAn.Array(_1)){_1=[_1];_2=false}
_1.clearProperty(this.selectionProperty||this.selection?this.selection.selectionProperty:null);return _2?_1:_1[0]}
,isc.A.getSelection=function isc_Canvas_getSelection(_1){if(!this.selection)return null;if(this.canSelectCells){var _2=this.selection.getSelectedCells();if(_2==null)return null;var _3=[];for(var i=0;i<_2.length;i++){var _5=_2[i],_6=this.getCellRecord(_5[0],_5[1]);if(_6==null)continue;_3.add(_6)}
return _3}else{return this.selection.getSelection(_1)}}
,isc.A.getSelectedRecords=function isc_Canvas_getSelectedRecords(_1){return this.getSelection(_1)}
,isc.A.getSelectedRecord=function isc_Canvas_getSelectedRecord(){if(!this.selection)return null;return this.selection.getSelectedRecord()}
,isc.A.getSelectionObject=function isc_Canvas_getSelectionObject(){return this.selection}
,isc.A.isSelected=function isc_Canvas_isSelected(_1){if(!_1||!this.selection)return false;return this.selection.isSelected(_1)}
,isc.A.isPartiallySelected=function isc_Canvas_isPartiallySelected(_1){if(!_1||!this.selection)return false;return this.selection.isPartiallySelected(_1)}
,isc.A.selectRecord=function isc_Canvas_selectRecord(_1,_2,_3){this.selectRecords(_1,_2,_3)}
,isc.A.selectSingleRecord=function isc_Canvas_selectSingleRecord(_1){this.selection.deselectAll();this.selectRecord(_1)}
,isc.A.deselectRecord=function isc_Canvas_deselectRecord(_1,_2){this.selectRecord(_1,false,_2)}
,isc.A.selectRecords=function isc_Canvas_selectRecords(_1,_2,_3){if(_2==null)_2=true;if(!isc.isAn.Array(_1))_1=[_1];if(isc.isA.ResultSet(this.data)&&!this.data.lengthIsKnown()){this.logWarn("ignoring attempt to select records while data is loading");return}
for(var i=0;i<_1.length;i++){if(_1[i]==null)continue;if(isc.isA.Number(_1[i])){var _5=_1[i];_1[i]=this.getRecord(_5,_3)}}
var _6=this.getSelectionObject(_3);if(_6){_6.selectList(_1,_2,_3);this.fireSelectionUpdated()}}
,isc.A.deselectRecords=function isc_Canvas_deselectRecords(_1,_2){this.selectRecords(_1,false)}
,isc.A.selectAllRecords=function isc_Canvas_selectAllRecords(){this.selection.selectAll();this.fireSelectionUpdated()}
,isc.A.deselectAllRecords=function isc_Canvas_deselectAllRecords(){this.selection.deselectAll();this.fireSelectionUpdated()}
,isc.A.anySelected=function isc_Canvas_anySelected(){return this.selection&&this.selection.anySelected()}
,isc.A.getRecord=function isc_Canvas_getRecord(_1,_2){if(this.data)return this.data.get(_1);return null}
,isc.A.fireSelectionUpdated=function isc_Canvas_fireSelectionUpdated(){if(this.selectionUpdated){var _1=this.getSelection(),_2=(_1&&_1.length>0?_1[0]:null);this.selectionUpdated(_2,_1)}}
,isc.A.getHilites=function isc_Canvas_getHilites(){return this.hilites}
,isc.A.setHilites=function isc_Canvas_setHilites(_1){this.hilites=_1;this.$36(this.hilites)}
,isc.A.getHiliteState=function isc_Canvas_getHiliteState(){var _1=this.getHilites();if(_1==null)return null;return"("+isc.JSON.encode(_1,{dateFormat:"dateConstructor",prettyPrint:false})+")"}
,isc.A.setHiliteState=function isc_Canvas_setHiliteState(_1){if(_1==null)this.setHilites(null);var _2=eval(_1);this.setHilites(_2)}
,isc.A.$36=function isc_Canvas__setupHilites(_1,_2){if(_1!=null){for(var i=0;i<_1.length;i++){if(_1[i].id==null){this.$37=this.$37||0;_1[i].id=this.$37++}}
this.$38=_1.makeIndex("id")}
if(!_2)this.applyHilites()}
,isc.A.applyHilites=function isc_Canvas_applyHilites(){var _1=this.hilites,_2=this.data;if(_1&&!this.$38)this.$36(_1,true);if(isc.isA.ResultSet(_2))_2=_2.getAllLoadedRows();if(isc.isA.Tree(_2))_2=_2.getAllItems();_2.setProperty(this.hiliteMarker,null);var _3=this.getAllFields();for(var i=0;i<_3.length;i++){var _5=_3[i],_6=_5[this.fieldIdProperty];if(_5.userFormula||_5.userSummary){if(_5.userSummary&&!_5.$39)
this.getSummaryFunction(_5);for(var j=0;j<_2.length;j++){if(_5.userFormula)
_2[j][_6]=this.getFormulaFieldValue(_5,_2[j]);else
_2[j][_6]=_5.$39(_2[j],_6,this)}}}
if(_1!=null){for(var i=0;i<_1.length;i++){this.applyHilite(_1[i],_2)}}
this.redrawHilites()}
,isc.A.getHilite=function isc_Canvas_getHilite(_1){if(isc.isAn.Object(_1))return _1;if(this.hilites==null)return null;if(!this.$38&&this.hilites){this.$36(this.hilites)}
var _2=this.$38[_1];if(_2==null)_2=this.hilites[_1];return _2}
,isc.A.applyHilite=function isc_Canvas_applyHilite(_1,_2,_3){_1=this.getHilite(_1);if(!_1.criteria)return;if(_1.disabled)return;var _3=_3||_1.fieldName;if(_3==null)_3=this.fields.getProperty("name");var _4=[];if(this.getDataSource()){_4=this.getDataSource().applyFilter(_2,_1.criteria)}else{_4=this.unboundApplyFilter(_2,_1.criteria)}
var _5=isc.isAn.Array(_3)?_3:[_3];if(this.logIsDebugEnabled("hiliting")){this.logDebug("applying filter: "+this.echoFull(_1.criteria)+", produced matches: "+isc.echoLeaf(_4)+", on fields: "+_5,"hiliting")}
for(var j=0;j<_5.length;j++){var _7=this.getField(_5[j]);for(var i=0;i<_4.length;i++){var _9=_4[i];this.hiliteRecord(_9,_7,_1)}}}
,isc.A.unboundApplyFilter=function isc_Canvas_unboundApplyFilter(_1,_2){var _3=[];if(_1){if(_2){for(var _4=0;_4<_1.length;_4++){if(this.evaluateCriterion(_1[_4],_2)){_3.add(_1[_4])}}}else{_3=_1}}
return _3}
,isc.A.evaluateCriterion=function isc_Canvas_evaluateCriterion(_1,_2){var _3=isc.DataSource.$4a[_2.operator];if(_3==null){isc.logWarn("Attempted to use unknown operator "+_2.operator);return false}
var _4=this.getDataSource();return _3.condition(_2.value,_1,_2.fieldName,_2,_3,_4||this)}
,isc.A.compareValues=function isc_Canvas_compareValues(_1,_2,_3,_4){if(isc.isA.Date(_1)&&isc.isA.Date(_2)){if(_1.logicalDate||_2.logicalDate){return Date.compareLogicalDates(date1,date2)}else{return Date.compareDates(date1,date2)}}else{var _5=_4&&_1.toLowerCase?_1.toLowerCase():_1,_6=_4&&_2.toLowerCase?_2.toLowerCase():_2;if(_5==null&&_6!=null)return 1;if(_5!=null&&_6==null)return-1;return _5>_6?-1:(_5<_6?1:(_5==_6?0:2))}}
,isc.A.hiliteRecord=function isc_Canvas_hiliteRecord(_1,_2,_3){if(!_2)return;var _4=_1[this.hiliteMarker];if(_4==null)_4=_1[this.hiliteMarker]=this.$3k++;var _5=_2.$4b=_2.$4b||{},_6=_5[_4];if(_6==null)_5[_4]=_3.id;else _5[_4]=[_6,_3.id]}
,isc.A.getHiliteCSSText=function isc_Canvas_getHiliteCSSText(_1){var _1=this.getHilite(_1);if(!_1)return;var _2=_1.cssText||"";if(_2==""){if(_1.textColor)_2+="color:"+_1.textColor+";";if(_1.backgroundColor)_2+="background-color:"+_1.backgroundColor+";";if(_2=="")_2==null}
return _2||_1.style}
,isc.A.addHiliteCSSText=function isc_Canvas_addHiliteCSSText(_1,_2,_3){if(!_1)return _3;var _4=_1[this.hiliteMarker],_2=this.getField(_2);if(!_2||!_2.$4b)return _3;var _5=_2.$4b[_4];if(_5==null)return _3;if(!isc.isAn.Array(_5)){this.$3l[0]=_5;_5=this.$3l}
for(var i=0;i<_5.length;i++){var _7=this.getHiliteCSSText(_5[i]);if(_7!=null){_3=_3?_3+isc.semi+_7:_7}}
return _3}
,isc.A.addObjectHilites=function isc_Canvas_addObjectHilites(_1,_2,_3){if(!this.hilites||!_1)return _2;var _4;if(!isc.isAn.Array(_1)){this.$3l[0]=_1;_4=this.$3l}
if(_4&&_4.length>0){for(var i=0;i<_4.length;i++){var _6,_7,_8;var _9=_4[i];if(isc.isA.String(_9))_6=_9;else _6=(_9!=null?_9[this.hiliteProperty]:null);_7=this.getHilite(_6);if(_7!=null&&!_7.disabled){_8=_7.cssText||_7.style;var _10=[];if(_7)
_10=isc.isAn.Array(_7.fieldName)?_7.fieldName:[_7.fieldName];var _11=(!_7.fieldName||!_3||_10.contains(_3.name));if(_8!=null&&_8!=isc.emptyString&&_11){if(_2==null)_2=_8;else _2+=isc.semi+_8}}}}
return _2}
,isc.A.getFieldHilites=function isc_Canvas_getFieldHilites(_1,_2){if(!_1||!_2)return null;if(_1[this.hiliteProperty]!=null){var _3=this.getHilite(_1[this.hiliteProperty]),_4;if(_3)
_4=isc.isAn.Array(_3.fieldName)?_3.fieldName:[_3.fieldName];if(_4&&_4.contains(_2.name))return[_3];else return null}
if(_1[this.hiliteMarker]!=null){var _5=_1[this.hiliteMarker];if(!_2.$4b)return null;else return _2.$4b[_5]}}
,isc.A.applyHiliteHTML=function isc_Canvas_applyHiliteHTML(_1,_2){if(!this.hilites)return _2;var _3,_4,_5;if(!isc.isAn.Array(_1)){this.$3l[0]=_1;_1=this.$3l}
for(var i=0;i<_1.length;i++){_5=_1[i];_3=this.getHilite(_5);if(_3!=null){if(_3.htmlValue!=null)_2=_3.htmlValue;if(!_3.disabled){_4=_3.htmlBefore;if(_4!=null&&_4.length>0){_2=_4+_2}
_4=_3.htmlAfter;if(_4!=null&&_4.length>0){_2=_2+_4}
var _7=_3.htmlOpposite,_8=_3.styleOpposite||this.styleOpposite;if(_7){if(!isc.Browser.isIE){_2="<nobr><div class='"+_8+"' style='float:left'>&nbsp;"+_7+"&nbsp;</div>"+_2+"</nobr>"}else{_2="<nobr><table role='presentation' align=left><tr><td class='"+_8+"'>"+_7+"</td></tr></table>"+_2+"</nobr>"}}}}}
return _2}
,isc.A.enableHilite=function isc_Canvas_enableHilite(_1,_2){if(_2==null)_2=true;var _3=this.getHilite(_1);if(_3==null)return;_3.disabled=!_2;this.redrawHilites()}
,isc.A.disableHilite=function isc_Canvas_disableHilite(_1){this.enableHilite(_1,false)}
,isc.A.enableHiliting=function isc_Canvas_enableHiliting(_1){if(_1==null)_1=true;if(this.hilites)this.hilites.setProperty("disabled",!_1);this.redrawHilites()}
,isc.A.disableHiliting=function isc_Canvas_disableHiliting(){this.enableHiliting(false)}
,isc.A.redrawHilites=function isc_Canvas_redrawHilites(){this.markForRedraw()}
,isc.A.editHilites=function isc_Canvas_editHilites(){var _1=this.getAllFields();if(!_1)return;var _2=_1?_1.findAll("canHilite",false):null;if(_2&&_2.length>0){_1.removeList(_2)}
var _3=isc.DataSource.create({fields:_1});if(this.hiliteWindow){this.hiliteEditor.setDataSource(_3);this.hiliteEditor.clearHilites();this.hiliteEditor.setHilites(this.getHilites());this.hiliteWindow.show();return}
var _4=this,_5=this.hiliteEditor=isc.HiliteEditor.create({autoDraw:false,dataSource:_3,hilites:this.getHilites(),callback:function(_7){if(_7!=null)_4.setHilites(_7);_4.hiliteWindow.hide()}}),_6=this.hiliteWindow=isc.Window.create({autoDraw:true,items:[_5],autoSize:true,autoCenter:true,isModal:true,showModalMask:true,closeClick:function(){this.hide()},title:"Edit Highlights",bodyProperties:{layoutMargin:8,membersMargin:8}});return _6}
,isc.A.transferRecords=function isc_Canvas_transferRecords(_1,_2,_3,_4,_5){if(!this.$4c("transferRecords",_1,_2,_3,_4,_5)){return}
if(isc.isAn.Array(this.data)&&this.data.length==0&&this.dataSource&&!this.shouldSaveLocally())
{this.fetchData(null,null,{$31:true});this.data.setFullLength(0)}
if(_4==this){if(_3!=null&&!this.isGrouped)this.data.slideList(_1,_3)}else{var _6=this.getDataSource();var _7=_4.getDataSource();if(_6&&_6==_7&&_4.dragDataAction==isc.Canvas.MOVE&&!(_4.shouldSaveLocally()||this.shouldSaveLocally()))
{var _8=isc.rpc.startQueue();for(var i=0;i<_1.length;i++){var _10={};var _11=_6.getPrimaryKeyFieldNames();for(var j=0;j<_11.length;j++){_10[_11[j]]=_1[i][_11[j]]}
isc.addProperties(_10,this.getDropValues(_10,_7,_2,_3,_4));this.updateDataViaDataSource(_10,_7,null,_4)}
if(!_8)isc.rpc.sendQueue()}else{if(!isc.isAn.Array(_1))_1=[_1];var _13=true;if(_7!=null&&_6!=null){var _14=_6.getPrimaryKeyField();_13=_14&&(_7.getField(_14.name)!=null)}
if(_13){if(this.selectionType==isc.Selection.MULTIPLE||this.selectionType==isc.Selection.SIMPLE)
{this.selection.deselectAll();this.selection.selectList(_1)}else if(this.selectionType==isc.Selection.SINGLE){this.selection.selectSingle(_1[0])}}
if(_6){this.$4d=isc.rpc.startQueue();for(var i=0;i<_1.length;i++){if(_1[i].$35)continue;var _10={};isc.addProperties(_10,_1[i]);isc.addProperties(_10,this.getDropValues(_10,_7,_2,_3,_4));if(_6!=_7){var _15=_6.getForeignKeysByRelation(_10,_7);var _16=false;var _17=[];if(_7)_17=_7.getPrimaryKeyFields();isc.addProperties(_10,_15);if(_6.titleField&&_7&&_7.titleField&&_6.titleField!=_7.titleField){var _18;if(_10[_6.titleField]===_18){_10[_6.titleField]=_10[_7.titleField]}}}
this.$4e(_10,_7,_4,_15)}}else{if(this.isGrouped){for(var i=0;i<_1.length;i++){var _10={};isc.addProperties(_10,_1[i]);isc.addProperties(_10,this.getDropValues(_10,_7,_2,_3,_4));if(!this.$4f(_10)){this.$4g(_10,true);this.originalData.add(_10)}}}else{for(var i=0;i<_1.length;i++){var _10={};isc.addProperties(_10,_1[i]);isc.addProperties(_10,this.getDropValues(_10,_7,_2,_3,_4));if(_3!=null){if(this.$4e(_10,null,_4,null,_3)){_3++}}else{this.$4e(_10,null,_4)}}}}}}
if(this.canReorderRecords&&this.$4h()!=null){this.unsort()}
if(!this.$4i){isc.Log.logDebug("Invoking transferDragData from inside transferRecords - no server "+"queries needed?","dragDrop");_4.transferDragData(this.$4j,this);if(_6){if(!this.$4d)isc.rpc.sendQueue()}}
this.$4k=false}
);isc.evalBoundary;isc.B.push(isc.A.$4c=function isc_Canvas__storeTransferState(_1,_2,_3,_4,_5,_6){if(!isc.isAn.Array(this.$4l))this.$4l=[];if(this.$4i&&this.$4i!=0){isc.logWarn("transferRecords was invoked but the prior transfer is not yet complete - \
                     the transfer will be queued up to run after the current transfer");this.$4l.add({implementation:_1,dropRecords:_2,targetRecord:_3,index:_4,sourceWidget:_5,callback:_6});return false}
this.$4l.addAt({implementation:_1,dropRecords:_2,targetRecord:_3,index:_4,sourceWidget:_5,callback:_6},0);this.$4k=true;this.$4j=[];this.$4i=0;return true}
,isc.A.updateDataViaDataSource=function isc_Canvas_updateDataViaDataSource(_1,_2,_3,_4){var _5=this;if(this.updateOperation){if(_3==null)_3={};isc.addProperties(_3,{operationId:this.updateOperation})}
if(!this.preventDuplicates){if(!_4.$4m)_4.$4m=0;_4.$4m++;_2.updateData(_1,function(_7,_8,_9){_4.$4n(_7,_8,_9)},_3);return}
var _6=this.getCleanRecordData(_1);if(this.data.find(_6,null,Array.DATETIME_VALUES)){isc.Log.logDebug("Found client-side duplicate, skipping update for '"+_1[isc.firstKey(_1)]+"'","dragDrop");this.$4j.add(this.getCleanRecordData(_1))}else{if(this.data.allMatchingRowsCached()){if(!_4.$4m)_4.$4m=0;_4.$4m++;_2.updateData(_1,function(_7,_8,_9){_4.$4n(_7,_8,_9)},_3)}else{isc.Log.logDebug("Incrementing dup query count: was "+_5.$4i,"dragDrop");this.$4i++;_2.fetchData(_6,function(_7,_8,_9){if(_8&&_8.length>0){isc.Log.logDebug("Found server-side duplicate, skipping update for '"+_1[isc.firstKey(_1)]+"'","dragDrop");_5.$4j.add(_5.getCleanRecordData(_8[0]))}else{if(!_4.$4m)_4.$4m=0;_4.$4m++;_2.updateData(_1,function(_7,_8,_9){_4.$4n(_7,_8,_9)},_3)}
isc.Log.logDebug("Decrementing dup query count: was "+_5.$4i,"dragDrop");if(--_5.$4i==0&&!_5.$4k){if(_4.dragDataAction==isc.Canvas.MOVE){isc.Log.logDebug("Invoking transferDragData from inside callback","dragDrop");_4.transferDragData(_5.$4j,_5);delete _5.$4j;if(!_5.$4d)isc.rpc.sendQueue()}}},{sendNoQueue:true})}}}
,isc.A.$4e=function isc_Canvas__addIfNotDuplicate(_1,_2,_3,_4,_5,_6){var _7=this.getDataSource(),_8,_9=this,_10={};if(this.addOperation){isc.addProperties(_10,{operationId:this.addOperation})}
if(_7)_8=_7.getPrimaryKeyFields();if(_7){var _11;if(_8&&isc.firstKey(_8)!=null){for(var _12 in _8){if(_8[_12].type=="sequence"){_11=true;break}}}
if(_11){var _13;for(var _12 in _8){_1[_12]=_13}
if(!_3.$4m)_3.$4m=0;_3.$4m++;this.addData(_1,function(_15,_16,_17){_3.$4n(_15,_16,_17)});return true}}
if(!this.preventDuplicates){if(_7){if(!_3.$4m)_3.$4m=0;_3.$4m++;this.addData(_1,function(_15,_16,_17){_3.$4n(_15,_16,_17)},_10)}else{if(isc.Tree&&isc.isA.Tree(this.data)){this.data.add(_1,_6,_5)}else{if(_5!=null)this.data.addAt(_1,_5);else this.data.add(_1)}}
return true}
if(this.$4f(_1,_2,_4)){if(this.duplicateDragMessage!=null)isc.warn(this.duplicateDragMessage);isc.Log.logDebug("Found client-side duplicate, adding '"+_1[isc.firstKey(_1)]+"' to exception list","dragDrop");this.$4j.add(this.getCleanRecordData(_1));return false}else{if(!_7){if(isc.Tree&&isc.isA.Tree(this.data)){this.data.add(_1,_6,_5);return true}else{if(_5!=null)this.data.addAt(_1,_5);else this.data.add(_1);return true}}else{if(!isc.ResultSet||!isc.isA.ResultSet(this.data)){if(!_3.$4m)_3.$4m=0;_3.$4m++;this.addData(_1,function(_15,_16,_17){_3.$4n(_15,_16,_17)},_10);return true}else{if(this.data.allRowsCached()||(_4&&isc.firstKey(_4)&&this.data.allMatchingRowsCached())){if(!_3.$4m)_3.$4m=0;_3.$4m++;this.addData(_1,function(_15,_16,_17){_3.$4n(_15,_16,_17)},_10);return true}
if(_7&&_2==_7){if(_8&&isc.firstKey(_8)!=null){var _14=isc.applyMask(_1,_8)}else{_14=this.getCleanRecordData(_1)}}else if(_4&&isc.firstKey(_4)){_14=isc.addProperties({},this.data.getCriteria());isc.addProperties(_14,_4)}else if(_7&&_8&&isc.firstKey(_8)!=null){_14=isc.applyMask(_1,_8)}else{_14=this.getCleanRecordData(_1)}
isc.Log.logDebug("Incrementing dup query count: was "+_9.$4i,"dragDrop");this.$4i++;_7.fetchData(_14,function(_15,_16,_17){if(_16&&_16.length>0){if(_9.duplicateDragMessage!=null)isc.warn(_9.duplicateDragMessage);isc.Log.logDebug("Found server-side duplicate, adding '"+_1[isc.firstKey(_1)]+"' to exception list","dragDrop");_9.$4j.add(_9.getCleanRecordData(_1))}else{if(!_3.$4m)_3.$4m=0;_3.$4m++;_7.addData(_1,function(_15,_16,_17){_3.$4n(_15,_16,_17)},_10)}
isc.Log.logDebug("Decrementing dup query count: was "+_9.$4i,"dragDrop");if(--_9.$4i==0&&!_9.$4k){if(_3.dragDataAction==isc.Canvas.MOVE){isc.Log.logDebug("Invoking transferDragData from inside callback","dragDrop");_3.transferDragData(_9.$4j,_9);delete _9.$4j;if(!_9.$4d)isc.rpc.sendQueue()}}},{sendNoQueue:true})}}}}
,isc.A.$4f=function isc_Canvas__isDuplicateOnClient(_1,_2,_3){var _4=this.getDataSource(),_5;if(!this.preventDuplicates)return false;if(_4)_5=_4.getPrimaryKeyFields();if(_4&&_4==_2){if(_5&&isc.firstKey(_5)!=null){for(var _6 in _5){if(_5[_6].type=="sequence"){return false}}}}
if(!_4){var _7=this.getCleanRecordData(_1)}else if(_4&&_2==_4){if(_5&&isc.firstKey(_5)!=null){_7=isc.applyMask(_1,_5)}else{_7=this.getCleanRecordData(_1)}}else if(_3&&isc.firstKey(_3)){_7={};var _8=this.data.getCriteria();if(!_4.isAdvancedCriteria(_8)){var _9=this.data.context;if(_9&&(_9.textMatchStyle==null||_9.textMatchStyle=="exact")){isc.addProperties(_7,_8)}}
isc.addProperties(_7,_3)}else if(_4&&_5&&isc.firstKey(_5)!=null){_7=isc.applyMask(_1,_5)}else{_7=this.getCleanRecordData(_1)}
if(this.data.find(_7,null,Array.DATETIME_VALUES))return true;else return false}
,isc.A.getCleanRecordData=function isc_Canvas_getCleanRecordData(_1){if(isc.Tree&&isc.isA.Tree(this.data)){return this.data.getCleanNodeData(_1,false)}
var _2={};for(var _3 in _1){if(_3.startsWith("_selection_"))continue;if(_3=="$4o")continue;if(_3=="$4p")continue;_2[_3]=_1[_3]}
return _2}
,isc.A.$4n=function isc_Canvas__updateComplete(_1,_2,_3){if(this.$4m){isc.Log.logDebug("Decrementing update count - was "+this.$4m,"dragDrop");this.$4m-=1}
if(!this.$4m){isc.Log.logDebug("All updates complete, calling dragComplete()","dragDrop");if(isc.isA.Function(this.dragComplete))this.dragComplete()}}
,isc.A.getDropValues=function isc_Canvas_getDropValues(_1,_2,_3,_4,_5,_6){if(!this.addDropValues)return;var _7={},_8;if(this.data&&this.data.getNodeDataSource){_8=this.data.getNodeDataSource(_3)}
if(!_8){_8=this.getDataSource()}
if(this.data&&this.data.getCriteria)_7=this.data.getCriteria(_8);var _9;if(isc.isAn.emptyObject(_7)||(_8&&!_8.isAdvancedCriteria(_7))){var _10=this.data.context;if(_10&&(_10.textMatchStyle==null||_10.textMatchStyle=="exact")){_9=isc.addProperties({},_7);if(this.dropValues){_9=isc.addProperties(_9,this.dropValues)}
return _9}}
return this.dropValues}
,isc.A.transferDragData=function isc_Canvas_transferDragData(_1,_2){var _3=[],_4,_5,_6;if(_2&&_2.$4l){_6=_2.$4l.shift();_4=_6.dropRecords;_5=_6.callback}else{_4=this.getDragData();_6={}}
if(_4==null)_4=[];for(var i=0;i<_4.length;i++){var _8=this.getCleanRecordData(_4[i]);if(!_1||!_1.find(_8,null,Array.DATETIME_VALUES)){_3.add(_4[i])}}
if(this.dragDataAction==isc.Canvas.MOVE&&_2!=this&&!_6.noRemove){if(this.dataSource&&!this.shouldSaveLocally()){var _9=_2.getDataSource();if(_9!=this.getDataSource()){var _10=isc.rpc.startQueue();for(var i=0;i<_3.length;i++){this.getDataSource().removeData(_3[i])}
if(!_10)isc.rpc.sendQueue()}}else if(this.data){for(var i=0;i<_3.length;i++){this.data.remove(_3[i]);if(this.isGrouped){this.originalData.remove(_3[i])}}}
if(this.selection&&this.selection.deselectList){this.selection.deselectList(_4)}}
if(_2){if(isc.isA.Function(_2.dropComplete))_2.dropComplete(_3);if(_5){this.fireCallback(_5,"records",[_3])}
if(_2.$4l&&_2.$4l.length>0){var _11=_2.$4l.shift();isc.Timer.setTimeout(function(){if(_11.implementation=="transferNodes"){_2.transferNodes(_11.dropRecords,_11.targetRecord,_11.index,_11.sourceWidget,_11.callback)}else{_2.transferRecords(_11.dropRecords,_11.targetRecord,_11.index,_11.sourceWidget,_11.callback)}},0)}}
return _3}
,isc.A.getDragData=function isc_Canvas_getDragData(){var _1=(this.selection&&this.selection.getSelection)?this.selection.getSelection():null;return _1}
,isc.A.cloneDragData=function isc_Canvas_cloneDragData(){var _1=this.$4q;if(_1==null){_1=(this.selection&&this.selection.getSelection)?this.selection.getSelection():null}
this.$4q=null;var _2=this.dragDataAction==isc.Canvas.COPY||this.dragDataAction==isc.Canvas.CLONE;var _3=[]
if(_2&&_1){if(isc.isA.Tree(this.data)){_1=this.data.getCleanNodeData(_1)}else{if(!isc.isAn.Array(_1))_1=[_1];var _4=[];for(var i=0;i<_1.length;i++){_4[i]=this.getCleanRecordData(_1[i])}
_1=_4}}
return _1}
,isc.A.transferSelectedData=function isc_Canvas_transferSelectedData(_1,_2,_3){if(!this.isValidTransferSource(_1)){if(_3)this.fireCallback(_3);return}
if(_2!=null)_2=Math.min(_2,this.data.getLength());var _4=_1.cloneDragData();var _5;if(_2!=null)_5=this.data.get(_2);this.transferRecords(_4,_5,_2,_1,_3)}
,isc.A.isValidTransferSource=function isc_Canvas_isValidTransferSource(_1){if(!_1||!_1.transferDragData){this.logWarn("transferSelectedData(): "+(_1?"Invalid ":"No ")+"source widget passed in - "+(_1||"")+" taking no action.");return false}
if(_1==this){this.logWarn("transferSelectedData(): target parameter contains a pointer back to this grid - ignoring");return false}
return true}
,isc.A.setDragTracker=function isc_Canvas_setDragTracker(){var _1=isc.EH,_2=this.dragTrackerMode;if(_2=="none"||_1.dragOperation==_1.DRAG_SCROLL){_1.setDragTracker("");return false}else if(_2=="icon"){var _3=this.getSelection(),_4=this.getDragTrackerIcon(_3);_1.setDragTracker(this.imgHTML(_4),null,null,null,null,this.getDragTrackerProperties());return false}else{var _5=this.getSelectedRecord(),_6=_5&&this.data?this.data.indexOf(_5):-1;if(_2=="title"){var _7=this.getDragTrackerTitle(_5,_6);_1.setDragTracker(_7,null,null,null,null,this.getDragTrackerProperties());return false}else if(_2=="record"){var _8=this.body.getTableHTML([0,this.fields.length-1],_6,_6+1);_1.setDragTracker(_8,null,null,null,null,this.getDragTrackerProperties());return false}}}
,isc.A.getDragTrackerProperties=function isc_Canvas_getDragTrackerProperties(){var _1=isc.addProperties({},this.dragTrackerProperties);_1.styleName=this.dragTrackerStyle;if(this.dragTrackerMode=="record")_1.opacity=50;return _1}
,isc.A.makeDragLine=function isc_Canvas_makeDragLine(){if(this._dragLine)return false;var _1={ID:this.getID()+"_dragLine",width:2,height:2,overflow:isc.Canvas.HIDDEN,visibility:isc.Canvas.HIDDEN,isMouseTransparent:true,dropTarget:this,redrawOnResize:false,styleName:"dragLine"};if(this.ns.Element.getStyleEdges(_1.styleName)==null){_1.backgroundColor="black"}
isc.addProperties(_1,this.dragLineDefaults,this.dragLineProperties);this._dragLine=this.ns.Canvas.create(_1);return true}
,isc.A.hideDragLine=function isc_Canvas_hideDragLine(){if(this._dragLine)this._dragLine.hide()}
,isc.A.configureFrom=function isc_Canvas_configureFrom(_1){var _2=this.dbcProperties;for(var i=0;i<_2.length;i++){this[_2[i]]=_1[_2[i]];if(_2[i]=="dataSource"){var _4=this.autoFetchData;this.autoFetchData=false;this.setDataSource(isc.DS.getDataSource(this.dataSource));this.autoFetchData=_4}}
this.setCriteria(_1.getCriteria());this.setData(_1.getData())}
,isc.A.addFormulaField=function isc_Canvas_addFormulaField(){this.editFormulaField()}
,isc.A.editFormulaField=function isc_Canvas_editFormulaField(_1){if(isc.FormulaBuilder==null)return;var _2=this,_3=!_1?false:true;if(!_3){_1={name:_2.getUniqueFieldName(this.formulaFieldNamePrefix),title:"New Field",width:"50",canFilter:false,canExport:false,canSortClientOnly:true}}
this.$4r=isc.Window.create({title:"Formula Editor ["+_1.title+"]",showMinimizeButton:false,showMaximizeButton:false,isModal:true,showModalMask:true,autoSize:true,autoCenter:true,autoDraw:true,headerIconProperties:{padding:1,src:"[SKINIMG]ListGrid/formula_menuItem.png"},closeClick:function(){this.items.get(0).completeEditing(true);return this.Super('closeClick',arguments)},items:[isc.FormulaBuilder.create({width:300,component:_2,dataSource:_2.getDataSource(),editMode:_3,field:_1,mathFunctions:isc.MathFunction.getDefaultFunctionNames(),fireOnClose:function(){_2.userFieldCallback(this)}},this.formulaBuilderProperties)]},this.formulaBuilderProperties)}
,isc.A.getFormulaFieldValue=function isc_Canvas_getFormulaFieldValue(_1,_2){return this.getFormulaFunction(_1)(_2,this)}
,isc.A.getFormulaFunction=function isc_Canvas_getFormulaFunction(_1){if(!_1.userFormula)return null;var _2=_1.$4s;if(_2!=null)return _2;_2=_1.$4s=_1.sortNormalizer=isc.FormulaBuilder.generateFunction(_1.userFormula,this.getAllFields(),this);return _2}
,isc.A.addSummaryField=function isc_Canvas_addSummaryField(){this.editSummaryField()}
,isc.A.editSummaryField=function isc_Canvas_editSummaryField(_1){if(isc.FormulaBuilder==null)return;var _2=this,_3=!_1?false:true;if(isc.isA.String(_1)){_1=this.getField(_1)}
if(!_3){_1={name:_2.getUniqueFieldName(this.summaryFieldNamePrefix),title:"New Field",width:"50",canFilter:false,canExport:false,canSortClientOnly:true}}
this.$4r=isc.Window.create({title:"Summary Editor ["+_1.title+"]",showMinimizeButton:false,showMaximizeButton:false,isModal:true,showModalMask:true,autoSize:true,autoCenter:true,autoDraw:true,headerIconProperties:{padding:1,src:"[SKINIMG]ListGrid/formula_menuItem.png"},closeClick:function(){this.items.get(0).completeEditing(true);return this.Super('closeClick',arguments)},items:[isc.SummaryBuilder.create({width:300,component:_2,dataSource:_2.getDataSource(),editMode:_3,field:_1,fireOnClose:function(){_2.userFieldCallback(this)}},this.summaryBuilderProperties)]},this.summaryEditorProperties)}
,isc.A.userFieldCallback=function isc_Canvas_userFieldCallback(_1){if(!_1)return;var _2=this.$4r;if(_1.cancelled){_2.destroy();return}
var _3=_1.getUpdatedFieldObject();if(this.userAddedField&&this.userAddedField(_3)==false){_2.destroy();return}
if(this.hideField&&_1.shouldHideUsedFields()){var _4=_1.getUsedFields();for(var i=0;i<_4.length;i++){var _6=_4.get(i);this.hideField(_6.name)}}
var _7=this.getAllFields();var _8=isc.Class.getArrayItemIndex(_3.name,_7,this.fieldIdProperty);if(_8>=0)_7[_8]=_3;else _7.addAt(_3,this.getFields().length);this.setFields(_7);if(this.markForRedraw)this.markForRedraw();var _9=_1.restartBuilder,_10=_1.builderTypeText;_2.destroy();if(_9){if(_10=="Formula")this.addFormulaField();else this.addSummaryField()}}
,isc.A.getUniqueFieldName=function isc_Canvas_getUniqueFieldName(_1){if(!_1||_1=="")_1=this.uniqueFieldNamePrefix;var _2=this.getFields(),_3=1,_4=_1.length;for(var i=0;i<_2.length;i++){var _6=_2.get(i);if(_6.name.startsWith(_1)){var _7=_6.name.substr(_4),_8=new Number(_7);if(_8&&_8>=_3)_3=_8+1}}
return _1+_3}
,isc.A.getSummaryFunction=function isc_Canvas_getSummaryFunction(_1){if(!_1.userSummary)return null;var _2=_1.$39;if(_2!=null)return _2;_2=_1.$39=_1.sortNormalizer=isc.SummaryBuilder.generateFunction(_1.userSummary,this.getAllFields(),this);return _2}
,isc.A.getSummaryFieldValue=function isc_Canvas_getSummaryFieldValue(_1,_2){return this.getSummaryFunction(_1)(_2,_1[this.fieldIdProperty],this)}
,isc.A.getRecordIndex=function isc_Canvas_getRecordIndex(_1){return this.data.indexOf(_1)}
,isc.A.getTitleFieldValue=function isc_Canvas_getTitleFieldValue(_1){}
,isc.A.getTitleField=function isc_Canvas_getTitleField(){if(this.titleField!=null)return this.titleField;if(this.dataSource!=null){var _1=this.getDataSource().getTitleField();if(!this.getField(_1))_1=this.getFields()[0][this.fieldIdProperty];this.titleField=_1}else{var _2=this.getFields().getProperty(this.fieldIdProperty);this.titleField=_2.contains("title")?"title":_2.contains("label")?"label":_2.contains("name")?"name":_2.contains("id")?"id":_2.first()}
return this.titleField}
,isc.A.getRecordHiliteCSSText=function isc_Canvas_getRecordHiliteCSSText(_1,_2,_3){_2=this.addObjectHilites(_1,_2,_3);_2=this.addHiliteCSSText(_1,_3,_2);return _2}
,isc.A.convertCSSToProperties=function isc_Canvas_convertCSSToProperties(_1,_2){if(_1==null)return null;var _3=_1.split(";"),_4;_3.map(function(_9){var _5=_9.split(":");if(_5.length!=2)return null;var _6=/^\s*(\S*)\s*$/,_7=_5[0].cssToCamelCaps().replace(_6,"$1"),_8=_5[1].replace(_6,"$1");if(!_2||_2.contains(_7)){if(!_4)_4={};_4[_7]=_8}});return _4}
,isc.A.getExportFieldValue=function isc_Canvas_getExportFieldValue(_1,_2,_3){return this.htmlUnescapeExportFieldValue(this.getStandaloneFieldValue(_1,_2,false))}
,isc.A.addDetailedExportFieldValue=function isc_Canvas_addDetailedExportFieldValue(_1,_2,_3,_4,_5,_6,_7,_8){var _9=_4.name,_10=this.getRecordHiliteCSSText(_3,null,_4),_11,_12={};if(!_8){_12=this.getDateFormattingProperties(_4,_3[_4.name],_1[_4.title])}
if(_4.exportRawValues||(this.exportRawValues&&_4.exportRawValues!=false))
_11=_3[_4[this.fieldIdProperty]];else
_11=this.getExportFieldValue(_3,_4.name,_5);if(!_4.userSummary){if(_10||_12){var _13=this.convertCSSToProperties(_10,_6);if(_12){if(!_13)_13={};isc.addProperties(_13,_12)}
if(_13){if(_7)
_1[_2]=[{value:_11,style:_13}];else
_1[_2]=_13}}
return}
if(!_4.userSummary.text)this.logError("Summary field does not have text format");var _14=[],_15={},_16={};var _17=(_10&&_10!="");for(var _18 in _4.userSummary.summaryVars){var _19=_4.userSummary.summaryVars[_18],_20=this.getField(_19);if(!_20)_14.add(_19);else{_15[_18]=_20;var _21=this.getRecordHiliteCSSText(_3,null,_20);if(_21){_16[_18]=_21;_17=true}}}
if(!_17)return;if(_14.length!=0&&_10){if(_7){_1[_2]={style:this.convertCSSToProperties(_10,_6),value:_11}}else{_1[_2]=this.convertCSSToProperties(_10,_6)}
return}
var _22=null,_23=null,_24=[];var _25=this;var _26=function(_37,_38){if(_37){_37=_25.htmlUnescapeExportFieldValue(_37);if(_22&&_23==_38){_22.value+=_37}else{if(_22)_24.push(_22);_22={value:_37};_23=_38;if(_38)_22.style=_25.convertCSSToProperties(_38,_6)}}};var _27=_4.userSummary.text.split("#"),_28=/^\{([A-Z]+)\}/;if(_27[0])_26(_27[0],_10);for(var i=1;i<_27.length;i++){var _30=_27[i],_31,_32,_33,_34,_35,_36;_33=_30.charAt(0);_32=_15[_33];if(_32)_36=_30.substr(1);else if(_31=_30.match(_28)){_36=_30.substr(_31[0].length);_33=_31[1];_32=_15[_33];if(!_32)_36=this.missingSummaryFieldValue+_36}else _36="#"+_30;if(_32){_34=this.getExportFieldValue(_3,_32.name,this.getFieldNum(_32.name));_35=null;if(_10)_35=(_35||"")+_10;if(_16[_33])_35=(_35||"")+_16[_33]}
_26(_34,_35);_26(_36,_10)}
if(_22)_24.push(_22);_1[_2]=_24}
,isc.A.getClientExportData=function isc_Canvas_getClientExportData(_1,_2){var _3=this.originalData||this.data,_4=[],_5=this.getClientExportFields(_1),_6,_7,_8,_9,_10=_1&&_1.exportFields;if(_1==null)_1={};if(_1.exportData!=null)_3=_1.exportData;_6=_1.includeHiddenFields;_7=_1.allowedProperties;_8=_1.includeCollapsedNodes;_9=_1.alwaysExportExpandedStyles;if(_10){if(_6!==false)_6=true}
if(isc.isA.ResultSet(_3))_3=_3.getAllLoadedRows();if(isc.isA.Tree(_3)){if(_8)_3=_3.getAllNodes();else _3=_3.getOpenList()}
var _11={settings:_1,callback:_2,chunkSize:this.exportDataChunkSize,data:_3,exportData:_4,fields:_5,includeHiddenFields:_6,allowedProperties:_7,includeCollapsedNodes:_8,alwaysExportExpandedStyles:_9,totalRows:_3.getLength(),startRow:0,endRow:Math.min(this.exportDataChunkSize,_3.getLength()),exportFieldsSpecified:_10};_11.firstTimeStamp=_11.thisTimeStamp=isc.timeStamp();this.logInfo("starting export chunking process - "+_11.firstTimeStamp,"export");this.getClientExportDataChunk(_11);return}
,isc.A.getClientExportDataChunk=function isc_Canvas_getClientExportDataChunk(_1){var _2=_1.settings,_3=_1.data,_4=_1.exportData,_5=_1.fields,_6=_1.includeHiddenFields,_7=_1.allowedProperties,_8=_1.includeCollapsedNodes,_9=_1.alwaysExportExpandedStyles,_10=_1.totalRows,_11=_1.startRow,_12=_1.endRow,_13=_1.settings.exportValueFields,_14=_1.exportFieldsSpecified,_15=_1.settings.exportDatesAsFormattedString;for(var _16=_11;_16<_12;_16++){var _17=_3[_16],_18=this.getRecordExportObject(_17,_5,_7,_6,_8,_9,_13,_14,_15);_4.push(_18)}
if(_1.endRow<_1.totalRows){_1.lastTimeStamp=_1.thisTimeStamp;_1.thisTimeStamp=isc.timeStamp();if(this.logIsInfoEnabled("export")){this.logInfo("processed "+_1.endRow+" rows - starting next chunk - "+((_1.thisTimeStamp-_1.lastTimeStamp)/1000),"export")}
_1.startRow=_1.endRow;_1.endRow=Math.min(_1.startRow+_1.chunkSize,_1.totalRows);return this.delayCall("getClientExportDataChunk",[_1],0)}
if(this.showGridSummary&&this.summaryRow&&this.exportIncludeSummaries){var _19=this.summaryRow,_3=this.getGridSummaryData(true);for(var _16=0;_16<_3.getLength();_16++){var _17=_3[_16],_18=this.getRecordExportObject(_17,_5,_7,_6,_8,_9,_15);_4.push(_18)}}
if(_1.callback){var _3=_1.exportData;if(this.logIsInfoEnabled("export")){this.logInfo("finished processing "+_1.endRow+" rows - about to export - "+isc.timestamp(),"export")}
this.fireCallback(_1.callback,"data,context",[_3,_1.settings])}}
,isc.A.getClientExportFields=function isc_Canvas_getClientExportFields(_1){var _2=this.getAllFields();if(isc.isA.Object(_1)){if(_1&&_1.exportFields){var _3=[];for(var i=0;i<_2.length;i++){if(_1.exportFields.contains(_2[i].name))_3.add(_2[i])}
_2=_3}}
return _2}
,isc.A.getRecordExportObject=function isc_Canvas_getRecordExportObject(_1,_2,_3,_4,_5,_6,_7,_8,_9){var _10={};for(var _11=0;_11<_2.length;_11++){var _12=_2[_11];if((!this.fields.contains(_12))&&!_4)continue;var _13=this.getFieldNum(_12.name),_14=this.htmlUnescapeExportFieldTitle(_12.exportTitle||_12.title||_12.name),_15=_14+"$style",_16;if(_12.exportRawValues||(this.exportRawValues&&_12.exportRawValues!=false))
_16=_1[_12[this.fieldIdProperty]];else
_16=this.getExportFieldValue(_1,_12.name,_13);if(_16==null||_16=="&nbsp;")_16="";if(!_8){if(_7){if(_12.displayField){var _17=_12.name;if(_17==_14)_17+="_value";_10[_17]=_1[_12.name]}}}
_10[_14]=_16;this.addDetailedExportFieldValue(_10,_15,_1,_12,_13,_3,_6,_9)}
return _10}
,isc.A.htmlUnescapeExportFieldTitle=function isc_Canvas_htmlUnescapeExportFieldTitle(_1){return this.htmlUnescapeExportFieldValue(_1)}
,isc.A.htmlUnescapeExportFieldValue=function isc_Canvas_htmlUnescapeExportFieldValue(_1){if(isc.isA.String(_1))return _1.unescapeHTML().replace(/<.*?>/g,isc.emptyString);return _1}
,isc.A.addHiliteSpan=function isc_Canvas_addHiliteSpan(_1,_2,_3){var _4=this.getRecordHiliteCSSText(_1,null,_2);if(_4)return"<span style=\""+_4+"\">"+_3+"</span>";else return _3}
,isc.A.getRawValue=function isc_Canvas_getRawValue(_1,_2){return this.getCellValue(_1,this.getField(_2))}
,isc.A.getFormattedValue=function isc_Canvas_getFormattedValue(_1,_2,_3){return _3}
,isc.A.fieldIsVisible=function isc_Canvas_fieldIsVisible(_1){return true}
,isc.A.getSpecifiedField=function isc_Canvas_getSpecifiedField(_1){return this.getField(_1)}
,isc.A.getStandaloneFieldValue=function isc_Canvas_getStandaloneFieldValue(_1,_2,_3){var _4=this.getSpecifiedField(_2),_5;if(!_4)return;if(_4.userFormula)_5=this.getFormulaFieldValue(_4,_1);else if(_4.userSummary)_5=this.getSummaryFieldValue(_4,_1);else{if(this.$4t&&this.$4t(_4)){_5=_1[_4.displayField]}else{_5=this.getRawValue(_1,_2)}
if(!_3)_5=this.getFormattedValue(_1,_2,_5)}
var _6=this.addHiliteSpan(_1,_4,_5);return _6}
,isc.A.getDateFormattingProperties=function isc_Canvas_getDateFormattingProperties(_1,_2,_3){if(!isc.SimpleType.inheritsFrom(_1.type,"date"))return;if(!isc.isA.Date(_2))return;var _4=isc.SimpleType.inheritsFrom(_1.type,"datetime");var _5;if(_1.dateFormatter&&isc.isA.Function(Date.prototype[_1.dateFormatter])){_5=_1.dateFormatter}else if(_1.displayFormat&&isc.isA.Function(Date.prototype[_1.displayFormat])){_5=_1.displayFormat}
if(!_5){var _6=this.getDataSource().getField(_1.name),_7=_6?_6.dateFormatter||_6.displayFormat:null;if(_7&&isc.isA.Function(Date.prototype[_7])){_5=_7}}
if(!_5){var _8;if(this.datetimeFormatter!=null&&_4){_8=this.datetimeFormatter}else{_8=this.dateFormatter}
if(_8&&isc.isA.Function(Date.prototype[_8])){_5=_8}}
if(!_5){var _9=!_4?Date.prototype.$fa:Date.prototype.$ff;if(_9&&isc.isA.Function(Date.prototype[_9])){_5=_9}}
var _10={rawValue:_2,dateFormatter:_5};return _10}
,isc.A.exportClientData=function isc_Canvas_exportClientData(_1,_2){if(_2)_1.$4u=_2;this.getClientExportData(_1,this.getID()+".exportClientDataReply(data,context)");return}
,isc.A.exportClientDataReply=function isc_Canvas_exportClientDataReply(_1,_2){if(_1==null||_1.length==0){isc.warn(this.emptyExportMessage)}
var _3=_2||{},_4=_3.exportAs?_3.exportAs:"csv",_5=_3.exportFilename?_3.exportFilename:"export",_6=_3.exportDisplay?_3.exportDisplay:"download";var _7={showPrompt:false,transport:_3.exportToClient===false?"xmlHttpRequest":"hiddenFrame",exportResults:true,downloadResult:!(_3.exportToClient===false),downloadToNewWindow:(_6=="window"),download_filename:(_6=="window"?_5:null)};var _8={exportAs:_3.exportAs,exportToClient:_3.exportToClient,exportToFilesystem:_3.exportToFilesystem,exportPath:_3.exportPath,exportFilename:_5,exportDelimiter:_3.exportDelimiter,exportHeader:_3.exportHeader,exportFooter:_3.exportFooter,exportTitleSeparatorChar:_3.exportTitleSeparatorChar,lineBreakStyle:_3.lineBreakStyle,exportDatesAsFormattedString:_3.exportDatesAsFormattedString};if(_3.exportFields){var _9=this.getAllFields(),_10=[],_11=[];for(var i=0;i<_3.exportFields.length;i++){var _13=_3.exportFields[i],_14=_9.find("name",_13),_15=_14?(_14.exportTitle?_14.exportTitle:_14.title):null;if(_15){var _16=this.htmlUnescapeExportFieldTitle(_15);_16=_16.replace("\n"," ");if(_15!=_16&&_1&&_1.length){for(var j=0;j<_1.length;j++){_1[j][_16]=_1[j][_15];delete _1[j][_15]}}
_10.add(_16)}else{_11.add(_13)}}
if(_11.length>0){this.logWarn("exportFields was specified but contains the following field-names "+"that are not available in this component: "+_11.join(",")+".")}
if(_10.length>0)_8.exportFields=_10}
isc.DMI.callBuiltin({methodName:"downloadClientExport",arguments:[_1,_4,_5,_6,_8],requestParams:_7,callback:_2.$4u});if(_2.$4u&&_7.downloadResult)this.fireCallback(_2.$4u)}
,isc.A.getSort=function isc_Canvas_getSort(){return this.$32?isc.shallowClone(this.$32):null}
,isc.A.setSort=function isc_Canvas_setSort(_1){this.$32=isc.shallowClone(_1);if(this.data&&this.$32&&this.$32.length>0){if(this.data.setSort)this.data.setSort(this.$32);else if(this.data.sortByProperty){var _2=this.$32[0];this.data.sortByProperty(_2.property,Array.shouldSortAscending(_2.direction),_2.normalizer,_2.context)}}}
,isc.A.askForSort=function isc_Canvas_askForSort(){if(isc.MultiSortDialog&&this.canMultiSort!=false){isc.MultiSortDialog.askForSort(this,this.getSort(),this.getID()+".multiSortReply(sortLevels)")}}
,isc.A.multiSortReply=function isc_Canvas_multiSortReply(_1){if(_1!=null){this.setSort(_1)}}
,isc.A.addValidationError=function isc_Canvas_addValidationError(_1,_2,_3){var _4=false;if(isc.isAn.Array(_3)){for(var i=0;i<_3.length;i++){_4=this.addValidationError(_1,_2,_3[i])||_4}
return _4}
var _6=_2.contains(this.$3a);if(_6){var _7=_1,_8=_2.trim(this.$3a).split();for(var i=0;i<_8.length;i++){if(!_7[_8[i]]){if(i<_8.length-1){if(parseInt(_8[i+1])==_8[i+1]){_7[_8[i]]=[]}else{_7[_8[i]]={}}}else{_7[_8[i]]=_3;_4=true}}
_7=_7[_8[i]]}}else{if(!_1[_2]){_1[_2]=_3;_4=true}else{if(!isc.isAn.Array(_1[_2]))_1[_2]=[_1[_2]];if(!_1[_2].contains(_3)){_1[_2].add(_3);_4=true}}}
return _4}
,isc.A.isFieldDependentOnOtherField=function isc_Canvas_isFieldDependentOnOtherField(_1,_2){if(!_1.validators)return false;var _3=this.getDataSource();for(var i=0;i<_1.validators.length;i++){var _5=_1.validators[i];if(!_5)continue;if(!_5.$4v&&_5.applyWhen&&_3!=null){_5.$4v=_3.getCriteriaFields(_5.applyWhen)}
if(_5.dependentFields&&_5.dependentFields.contains(_2)){return true}
if(_5.$4v&&_5.$4v.length>0&&_5.$4v.contains(_2))
{return true}}
return false}
,isc.A.getFieldDependencies=function isc_Canvas_getFieldDependencies(_1){if(!_1.validators)return null;var _2=this.getDataSource(),_3=[];for(var i=0;i<_1.validators.length;i++){var _5=_1.validators[i];if(!_5)continue;if(!_5.$4v&&_5.applyWhen&&_2!=null){_5.$4v=_2.getCriteriaFields(_5.applyWhen)}
if(_5.dependentFields){if(!isc.isAn.Array(_5.dependentFields)){_5.dependentFields=[_5.dependentFields]}
for(var i=0;i<_5.dependentFields.length;i++){_3.add(_5.dependentFields[i])}}
if(_5.$4v&&_5.$4v.length>0)
{_3.addList(_5.$4v)}}
return(_3.length==0?null:_3)}
,isc.A.validateFieldAndDependencies=function isc_Canvas_validateFieldAndDependencies(_1,_2,_3,_4,_5){var _6={},_7=false,_8={valid:true,errors:null,resultingValue:null};_4[_1.name]=_3;var _9=this.validateField(_1,_1.validators,_3,_4,_5);if(_9!=null){_8.valid=_9.valid;_8.stopOnError=_9.stopOnError;if(_9.errors!=null){this.addValidationError(_6,_1.name||_1.dataPath,_9.errors)}
if(_9.resultingValue!=null){_8.resultingValue=_9.resultingValue;_4[_1.name]=_9.resultingValue}
_7=true}
var _10=_1.name||_1.dataPath,_11=this.getFields()||[];for(var i=0;i<_11.length;i++){var _13=_11[i];if(_13.name!=_10&&_13.dataPath!=_10&&this.isFieldDependentOnOtherField(_13,_10))
{_9=this.validateField(_13,_13.validators,_4[_13.name],_4,_5);if(_9!=null){if(_9.errors!=null){this.addValidationError(_6,_13.name||_13.dataPath,_9.errors)}else{this.addValidationError(_6,_13.name||_13.dataPath,null)}
if(_9.resultingValue!=null){_4[_13.name]=_9.resultingValue}}}}
_8.errors=_6;return(_7?_8:null)}
,isc.A.validateField=function isc_Canvas_validateField(_1,_2,_3,_4,_5){if(!_2)return null;var _6=[],_7=false,_8=null,_9={valid:true,errors:null,resultingValue:null},_10=false,_11=false;if(!isc.isAn.Array(_2)){_2=[_2]}
for(var i=0;i<_2.length;i++){var _13=_2[i];if(!_13)continue;var _14=isc.Validator.getValidatorType(_13);if(_5&&_5.typeValidationsOnly&&!this.$3m.contains(_14))
{continue}
if(_5&&_5.dontValidateNullValue&&_3==null&&_14!="required"&&_14!='requiredIf')
{continue}
if(!_5||!_5.changing||(_13.validateOnChange!=false&&(_13.validateOnChange||_1.validateOnChange||this.validateOnChange)))
{if(isc.Validator.isServerValidator(_13)){_10=true;if(_13.stopOnError)_11=true;continue}
if(_13.applyWhen){var _15=this.getDataSource(),_16=_13.applyWhen;if(_15==null){isc.logWarn("Conditional validator criteria ignored because form has no dataSource")}else{var _17=_15.applyFilter([_4],_16);if(_17.length==0){isc.Validator.performAction(null,_1,_13,_4,this);continue}}}
_7=true;var _18=(isc.Validator.processValidator(_1,_13,_3,null,_4)==true);isc.Validator.performAction(_18,_1,_13,_4,this);if(!_18){var _19=isc.Validator.getErrorMessage(_13);if(_19==null){if(_5&&_5.unknownErrorMessage){_19=_5.unknownErrorMessage}else{_19=this.unknownErrorMessage}}
_6.add(_19);if(_13.stopOnError)_8=true}
if(_13.resultingValue!=null){_9.resultingValue=_13.resultingValue;_3=_13.resultingValue}
if(!_18&&_13.stopIfFalse)break}}
if(_10&&(!_5||_5.skipServerValidation!=true)){_11=this.$4w(_11,_1.stopOnError,this.stopOnError);var _20=((_5&&_5.serverValidationMode)?_5.serverValidationMode:this.$3n),_21=isc.addProperties({},_4),_22=(_11||_1.synchronousValidation||this.synchronousValidation||false);_21[_1.name]=_3;this.fireServerValidation(_1,_21,_20,_22,_5.rowNum)}
_9.stopOnError=(_6.length>0&&this.$4w(_8,_1.stopOnError,this.stopOnError));_9.errors=(_6.length==0?null:_6);_9.valid=(_6.length==0);return(_7?_9:null)}
,isc.A.$4w=function isc_Canvas__resolveStopOnError(_1,_2,_3){if(_1!=null)return _1;return(_2==null&&_3)||_2||false}
,isc.A.fireServerValidation=function isc_Canvas_fireServerValidation(_1,_2,_3,_4,_5){var _6=this.getDataSource();if(_6==null)return;var _7={showPrompt:_4,prompt:isc.RPCManager.validateDataPrompt,validationMode:_3,clientContext:{component:this,fieldName:_1.name,rowNum:_5}};if(_3==this.$3n){for(var _8 in _2){if(_2[_8]===null)delete _2[_8]}}
if(!_4){var _9=this.$4x(_1);_7.clientContext.pendingFields=_9}
_6.validateData(_2,this.$4y,_7)}
,isc.A.$4y=function isc_Canvas__handleServerValidationReply(_1,_2,_3){if(_1.status==isc.DSResponse.STATUS_FAILURE){isc.logWarn("Server-side validation failed: "+_1.data);isc.say(_1.data)}
var _4=_1.clientContext,_5=_4.component,_6=_4.pendingFields,_7=_1.errors==null?null:isc.DynamicForm.getSimpleErrors(_1.errors);if(_1.errors){for(var _8 in _7){var _9=_7[_8],_10=_5.getField(_8);if(_9!=null&&_10!=null){if(!isc.isAn.Array(_9))_9=[_9];var _11=null;for(var i=0;i<_9.length;i++){_5.addFieldErrors(_8,_9[i].errorMessage,false,_4.rowNum);if(_9[i].stopOnError)_11=true}
if(_10.redraw)_10.redraw();_11=_5.$4w(_11,_10.stopOnError,_5.stopOnError);if(_8==_4.fieldName&&_11==true&&!_10.hasFocus){if(!_10.synchronousValidation&&!_5.synchronousValidation){isc.logWarn("Server validation for "+_8+" signaled stopOnError but validation is not set for"+" synchronousValidation:true - stop ignored.")}else{_5.focusInItem(_10)}}}}}
if(_6){_5.$4z(_6)}
if(_5&&_5.handleAsyncValidationReply!=null){if(_7!=null){_7=isc.DynamicForm.formatValidationErrors(_7)}
_5.handleAsyncValidationReply(_7==null,_7)}}
,isc.A.handleAsyncValidationReply=function isc_Canvas_handleAsyncValidationReply(_1,_2){}
,isc.A.isPendingAsyncValidation=function isc_Canvas_isPendingAsyncValidation(){return!isc.isAn.emptyObject(this.$3o)}
,isc.A.$4x=function isc_Canvas__registerAsyncValidation(_1){var _2=this.getFields()||[],_3=[_1.name],_4=_1.name;this.$3o[_4]=(this.$3o[_4]==null?1:this.$3o[_4]++);for(var i=0;i<_2.length;i++){var _6=_2[i];if(_6.name!=_4&&this.isFieldDependentOnOtherField(_6,_4)){var _7=_6.name;_3.add(_7);this.$3o[_7]=(this.$3o[_7]==null?1:this.$3o[_7]++)}}
return _3}
,isc.A.$4z=function isc_Canvas__clearAsyncValidation(_1){var _2=false;for(var i=0;i<_1.length;i++){this.$3o[_1[i]]--;if(this.$3o[_1[i]]==0){delete this.$3o[_1[i]];_2=true}}
if(_2&&this.$3p!=null){var _4=true;for(var i=0;i<this.$3p;i++){if(this.$3o[this.$3p[i]]>0){_4=false;break}}
if(_4){this.$3p=null;isc.clearPrompt()}}}
,isc.A.blockOnFieldBusy=function isc_Canvas_blockOnFieldBusy(_1){if(this.$3p!=null)return true;var _2=false;for(var _3 in this.$3o){_2=true;break}
if(!_2)return false;var _4=this.getFieldDependencies(_1)||[];_4.add(_1.name);var _5=[];for(var i=0;i<_4.length;i++){var _7=_4[i];if(this.$3o[_7]>0){_5.add(_7)}}
if(_5.length>0){this.$3p=_5;this.delayCall("showValidationBlockingPrompt");return true}
return false}
,isc.A.showValidationBlockingPrompt=function isc_Canvas_showValidationBlockingPrompt(){if(this.$3p)isc.showPrompt(isc.RPCManager.validateDataPrompt)}
,isc.A.enableField=function isc_Canvas_enableField(_1){if(_1==null||isc.isAn.emptyString(_1))return;var _2=this.getField(_1);if(_2){_2.disabled=false;this.redraw()}}
);isc.evalBoundary;isc.B.push(isc.A.disableField=function isc_Canvas_disableField(_1){if(_1==null||isc.isAn.emptyString(_1))return;var _2=this.getField(_1);if(_2){_2.disabled=true;this.redraw()}}
,isc.A.showField=function isc_Canvas_showField(_1){if(_1==null||isc.isAn.emptyString(_1))return;var _2=this.getField(_1);if(_2)_2.show()}
,isc.A.hideField=function isc_Canvas_hideField(_1){if(_1==null||isc.isAn.emptyString(_1))return;var _2=this.getField(_1);if(_2)_2.hide()}
,isc.A.setFieldCanEdit=function isc_Canvas_setFieldCanEdit(_1,_2){if(_1==null||isc.isAn.emptyString(_1))return;var _3=this.getField(_1);if(_3){_3.canEdit=_2;this.redraw()}}
,isc.A.isOffline=function isc_Canvas_isOffline(){if(this.data&&this.data.$40)return true;return false}
,isc.A.setSelectionComponent=function isc_Canvas_setSelectionComponent(_1,_2){if(!_1){if(this.selectionComponent!=null){this.ignore(this.selectionComponent,"selectionChanged");this.ignore(this.selectionComponent,"cellSelectionChanged")}
delete this.selectionComponent;if(this.valuesManager){this.ignore(this.valuesManager,"$41")}}else{var _3=_1;if(isc.isA.String(_1))_1=window[_1];if(!_1||!isc.isA.Canvas(_1)||_1.dataArity!="multiple"){this.logWarn("setSelectionComponent() - selection component specified as:"+_3+" this is not a valid component");return}
if(!_1.getSelection){this.logWarn("setSelectionComponent() - specified selection component:"+_1+" does not support selection - ignoring");return}
if(!_2&&this.selectionComponent){if(this.selectionComponent==_1)return
if(this.isObserving(this.selectionComponent,"selectionChanged")){this.ignore(this.selectionComponent,"selectionChanged")}
if(this.isObserving(this.selectionComponent,"cellSelectionChanged")){this.ignore(this.selectionComponent,"cellSelectionChanged")}}
this.selectionComponent=_1;if(!this.selectionComponent.useCellRecords){this.observe(this.selectionComponent,"selectionChanged","observer.selectionComponentSelectionChanged(observed, record,state)")}else{this.observe(this.selectionComponent,"cellSelectionChanged","observer.selectionComponentCellSelectionChanged(observed, cellList)")}
var _4=this.selectionComponent.getSelection}}
,isc.A.selectionComponentSelectionChanged=function isc_Canvas_selectionComponentSelectionChanged(_1,_2,_3){if(!_3){if(this.dataArity=="single"){_2=null}else{return}}
if(this.dataArity=="single"){this.setData(_2)}else{var _4=this.dataPath.split("/");this.setData(_2[_4[_4.length-1]]);if(this.dataArity=="multiple"&&isc.isA.Function(this.deselectAllRecords)){this.deselectAllRecords()}}}
,isc.A.selectionComponentCellSelectionChanged=function isc_Canvas_selectionComponentCellSelectionChanged(_1,_2){for(var i=0;i<_2.length;i++){var _4=_2[i],_5=this.selectionComponent.getCellRecord(_4[0],_4[1]);if(_1.cellIsSelected(_5))break;_5=null}
if(_5){this.$42=_1.getPrimaryKeys(_5);this.editRecord(_5)}}
);isc.B._maxIndex=isc.C+184;isc.ClassFactory.defineClass("MathFunction","Class");isc.A=isc.MathFunction;isc.A.$43={};isc.A=isc.MathFunction;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.registerFunction=function isc_c_MathFunction_registerFunction(_1){if(!this.$43[_1.name]){this.$43[_1.name]=_1}}
,isc.A.getRegisteredFunctionNames=function isc_c_MathFunction_getRegisteredFunctionNames(){return isc.getKeys(this.$43)}
,isc.A.getDefaultFunctionNames=function isc_c_MathFunction_getDefaultFunctionNames(){var _1=this.getDefaultFunctions(),_2=_1.makeIndex("name",false);return isc.getKeys(_2)}
,isc.A.getRegisteredFunctions=function isc_c_MathFunction_getRegisteredFunctions(){return isc.getValues(this.$43)}
,isc.A.getDefaultFunctions=function isc_c_MathFunction_getDefaultFunctions(){var _1=this.getRegisteredFunctions(),_2=_1.findAll("defaultSortPosition",-1)||[];for(var i=0;i<_2.length;i++){var _4=_2[i];_1.remove(_4)}
_1.sortByProperties(["defaultSortPosition"],["true"]);return _1}
,isc.A.getRegisteredFunctionIndex=function isc_c_MathFunction_getRegisteredFunctionIndex(){var x=this.getRegisteredFunctions();var _2=x.makeIndex("name",false);return _2}
,isc.A.getDefaultFunctionIndex=function isc_c_MathFunction_getDefaultFunctionIndex(){return this.getDefaultFunctions().makeIndex("name",false)}
,isc.A.isRegistered=function isc_c_MathFunction_isRegistered(_1){if(this.$43[_1])return true;return false}
);isc.B._maxIndex=isc.C+8;isc.A=isc.MathFunction.getPrototype();isc.A.defaultSortPosition=-1;isc.MathFunction.registerFunction(isc.MathFunction.create({name:"max",description:"Maximum of two values",usage:"max(value1, value2)",defaultSortPosition:1,jsFunction:function(_1,_2){return Math.max(_1,_2)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"min",description:"Minimum of two values",usage:"min(value1, value2)",defaultSortPosition:2,jsFunction:function(_1,_2){return Math.min(_1,_2)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"round",description:"Round a value up or down, optionally providing <i>decimalDigits</i> "+"as the maximum number of decimal places to round to.  For fixed or precision "+"rounding, use <i>toFixed()</i> and <i>toPrecision()</i> respectively.",usage:"round(value,decimalDigits)",defaultSortPosition:3,jsFunction:function(_1,_2){if(_2){var _3=Math.pow(10,_2),_4=Math.round(_1*_3)/_3;return _4}
return Math.round(_1)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"ceil",description:"Round a value up",usage:"ceil(value)",defaultSortPosition:4,jsFunction:function(_1){return Math.ceil(_1)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"floor",description:"Round a value down",usage:"floor(value)",defaultSortPosition:5,jsFunction:function(_1){return Math.floor(_1)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"abs",description:"Absolute value",usage:"abs(value)",defaultSortPosition:6,jsFunction:function(_1){return Math.abs(_1)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"pow",description:"Value1 to the power of Value2",usage:"pow(value1, value2)",defaultSortPosition:7,jsFunction:function(_1,_2){return Math.pow(_1,_2)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"sin",description:"Sine of a value",usage:"sin(value)",defaultSortPosition:8,jsFunction:function(_1){return Math.sin(_1)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"cos",description:"Cosine of a value",usage:"cos(value)",defaultSortPosition:9,jsFunction:function(_1){return Math.cos(_1)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"tan",description:"Tangent of a value",usage:"tan(value)",defaultSortPosition:10,jsFunction:function(_1){return Math.tan(_1)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"ln",description:"Natural logarithm of a value",usage:"ln(value)",defaultSortPosition:11,jsFunction:function(_1){return Math.log(_1)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"log",description:"logarithm of a value with the specified <i>base</i>",usage:"log(base, value)",defaultSortPosition:12,jsFunction:function(_1,_2){return Math.log(_2)/Math.log(_1)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"asin",description:"Arcsine of a value",usage:"asin(value)",defaultSortPosition:13,jsFunction:function(_1){return Math.asin(_1)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"acos",description:"Arccosine of a value",usage:"acos(value)",defaultSortPosition:14,jsFunction:function(_1){return Math.acos(_1)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"atan",description:"Arctangent of a value (-PI/2 to PI/2 radians)",usage:"atan(value)",defaultSortPosition:15,jsFunction:function(_1){return Math.atan(_1)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"atan2",description:"Angle theta of a point (-PI to PI radians)",usage:"atan2(value1,value2)",defaultSortPosition:16,jsFunction:function(_1,_2){return Math.atan2(_1,_2)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"exp",description:"The value of E<sup>value</sup>",usage:"exp(value)",defaultSortPosition:17,jsFunction:function(_1){return Math.exp(_1)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"random",description:"Random number between 0 and 1",usage:"random()",defaultSortPosition:18,jsFunction:function(){return Math.random()}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"sqrt",description:"Square root of a value",usage:"sqrt(value)",defaultSortPosition:19,jsFunction:function(_1){return Math.sqrt(_1)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"toPrecision",description:"Format a number to a length of <i>precision</i> digits, rounding or "+"adding a decimal point and zero-padding as necessary.  Note that the values "+"123, 12.3 and 1.23 have an equal precision of 3.  Returns a formatted "+"string and should be used as the outermost function call in a formula. "+"For rounding, use <i>round()</i>.",usage:"toPrecision(value,precision)",defaultSortPosition:20,jsFunction:function(_1,_2){var _3=_1;if(isc.isA.String(_3))_3=parseFloat(_3);if(isNaN(_3))return _1;return _3.toPrecision(_2)}}));isc.MathFunction.registerFunction(isc.MathFunction.create({name:"toFixed",description:"Round or zero-pad a number to <i>digits</i> decimal places.  Returns "+"a formatted string and should be used as the outermost function call in a "+"formula.  To round values or restrict precision, use <i>round()</i> and "+"<i>toPrecision()</i> respectively.",usage:"toFixed(value,digits)",defaultSortPosition:21,jsFunction:function(_1,_2){var _3=_1;if(isc.isA.String(_3))_3=parseFloat(_3);if(isNaN(_3))return _1;return _3.toFixed(_2)}}));isc.Canvas.registerStringMethods({userAddedField:"field",selectionUpdated:"record,recordList",onFetchData:"criteria,requestProperties"});isc.defineClass("EdgedCanvas","Canvas");isc.A=isc.EdgedCanvas.getPrototype();isc.A.redrawOnResize=false;isc.A._redrawWithParent=false;isc.A.$k7=false;isc.A.$k6=false;isc.A.useClipDiv=false;isc.A.overflow=isc.Browser.isMoz?isc.Canvas.VISIBLE:isc.Canvas.HIDDEN;isc.A.$mi=false;isc.A.$xw=0;isc.A.$xy=0;isc.A.$xx=0;isc.A.$xz=0;isc.A.$44=["TL","T","TR","L","center","R","BL","B","BR"];isc.A.skinImgDir="images/edges/";isc.A.edgeImage="[SKIN]/rounded/frame/FFFFFF/6.png";isc.A.shownEdges={TL:true,T:true,TR:true,L:true,R:true,BL:true,B:true,BR:true};isc.A.edgeSize=6;isc.A=isc.EdgedCanvas.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$45="<TD class='";isc.A.$46="' ></TD>";isc.A.$47="<TABLE CELLPADDING=0 CELLSPACING=0 "+"STYLE='height:100%;width:100%;table-layout:fixed'>"+"<COL WIDTH=";isc.A.$48="><COL><COL WIDTH=";isc.A.$49=" class=";isc.A.addEdgeStyleSuffix=false;isc.A.forceMozRowHeight=true;isc.B.push(isc.A.initWidget=function isc_EdgedCanvas_initWidget(){this.invokeSuper(isc.EdgedCanvas,this.$sb);var _1=this.customEdges;if(_1){var _2=this.shownEdges={};if(_1.contains("T")){_2.T=_2.TL=_2.TR=true}
if(_1.contains("B")){_2.B=_2.BL=_2.BR=true}
if(_1.contains("L")){_2.L=_2.TL=_2.BL=true}
if(_1.contains("R")){_2.R=_2.TR=_2.BR=true}}
this.updateEdgeSizes()}
,isc.A.updateEdgeSizes=function isc_EdgedCanvas_updateEdgeSizes(){var _1=this.edgeSize;this.$z7=this.$ef(this.edgeLeft,_1);this.$z8=this.$ef(this.edgeRight,_1);this.$z9=this.$ef(this.edgeTop,_1);this.$0a=this.$ef(this.edgeBottom,_1);var _2=this.shownEdges,_3=this.edgeOffset;if(_2.L)this.$xw=this.$ef(this.edgeOffsetLeft,_3,this.$z7);if(_2.R)this.$xx=this.$ef(this.edgeOffsetRight,_3,this.$z8);if(_2.T)this.$xy=this.$ef(this.edgeOffsetTop,_3,this.$z9);if(_2.B)this.$xz=this.$ef(this.edgeOffsetBottom,_3,this.$0a);this.markForRedraw()}
,isc.A.getInnerWidth=function isc_EdgedCanvas_getInnerWidth(_1,_2,_3){var _4=this.invokeSuper(isc.EdgedCanvas,"getInnerWidth",_1,_2,_3);return _4-this.$xw-this.$xx}
,isc.A.getInnerHeight=function isc_EdgedCanvas_getInnerHeight(_1,_2,_3){var _4=this.invokeSuper(isc.EdgedCanvas,"getInnerHeight",_1,_2,_3);return _4-this.$xy-this.$xz}
,isc.A.getInnerHTML=function isc_EdgedCanvas_getInnerHTML(){var _1=isc.SB.create(),_2=this.edgeImage,_3=_2.lastIndexOf(isc.dot),_4=_2.substring(0,_3),_5=_2.substring(_3),_6=this.getImgURL(_4),_7="<TD HEIGHT=",_8="<TD",_9,_10;if(!((isc.Browser.isStrict&&isc.Browser.isIE&&isc.Browser.version>=8)||(isc.Browser.isMoz&&isc.Browser.isUnix)))
{var _11=isc.EdgedCanvas.$5a;if(!_11){_11=isc.EdgedCanvas.$5a={width:"100%",height:"100%"};if(isc.Browser.isSafari)_11.align="middle";if(isc.Browser.isStrict&&!isc.Browser.isTransitional)
_11.extraStuff="style='display:block;'"}
_11.src=_2;var _12=this.imgHTML(_11);if(isc.Browser.isSafari){_12="<DIV style='overflow:hidden;width:100%;height:100%'>"+_12+"</DIV>"}
var _13=_12.lastIndexOf(isc.dot);_9=this.$r8+_12.substring(0,_13);_10=_12.substring(_13)+"</TD>"}else{_9=" STYLE='background:url("+_6;_10=_5+")'></TD>"}
if(this.edgeColor)_9+=isc.$ak+this.edgeColor;var _14=this.shownEdges;_1.append(this.$47,this.$z7,this.$48,this.$z8,this.$r8,"<TR HEIGHT=",this.$z9,this.$r8);this.$5b(_7,this.$z9,_9,_10,0,2,_14,_1);var _15=this.getHeight()-this.$z9-this.$0a;if(isc.Browser.isIE&&isc.Browser.isStrict){_1.append("</TR><TR HEIGHT=",_15,">")}else{_1.append("</TR><TR>")}
if(isc.Browser.isMoz){this.$5b(_7,"100%",_9,_10,3,5,_14,_1)}else if(isc.Browser.isWebKit){this.$5b(_7,_15,_9,_10,3,5,_14,_1)}else{this.$5b(_8,null,_9,_10,3,5,_14,_1)}
_1.append("</TR><TR HEIGHT=",this.$0a,">");this.$5b(_7,this.$0a,_9,_10,6,8,_14,_1);_1.append("</TR></TABLE>");return _1.toString()}
,isc.A.$5b=function isc_EdgedCanvas__writeEdgeCells(_1,_2,_3,_4,_5,_6,_7,_8){for(var i=_5;i<=_6;i++){var _10=this.$44[i];var _11=this.getEdgeStyleName(_10),_12=_11?this.$49:null;if(_7[_10]||(this.showCenter&&_10==isc.Canvas.CENTER)){_8.append(_1,_2,_12,_11,_3,this.getEdgePrefix(_10),isc.$ak,_10,_4)}else{if(this.centerBackgroundColor&&_10==isc.Canvas.CENTER){_8.append("<TD ",_12,_11," style='background-color:",this.centerBackgroundColor,"'></TD>")}else{_8.append(this.$45,_12,_11,this.$46)}}}}
,isc.A.getEdgeStyleName=function isc_EdgedCanvas_getEdgeStyleName(_1){if(this.edgeStyleName==null)return;if(!this.addEdgeStyleSuffix)return this.edgeStyleName;if(!this.$5c||this.$5c.base!=this.edgeStyleName){var _2=this.edgeStyleName;this.$5c={base:_2,TL:_2+"$5d",T:_2+"$5e",TR:_2+"$5f",L:_2+"$5g",C:_2+"$5h",R:_2+"$5i",BL:_2+"$5j",B:_2+"$5k",BR:_2+"$5l"}}
return this.$5c[_1]}
,isc.A.getEdgePrefix=function isc_EdgedCanvas_getEdgePrefix(_1){}
,isc.A.$yl=function isc_EdgedCanvas__handleResized(){if(!this.isDrawn()||this.$5m)return;if(isc.Browser.isOpera){this.masterElement.bringToFront();return}
if(isc.Browser.isIE&&isc.Browser.isStrict){var _1=this.getHandle().firstChild.rows[1];this.$z6(_1.style,this.$s6,this.getHeight()-this.$z9-this.$0a);return}
if(isc.Browser.isWebKit){var _1=this.getHandle().firstChild.rows[1];var _2=Math.max(0,this.getHeight()-this.$z9-this.$0a);for(var i=0;i<_1.cells.length;i++){this.$z6(_1.cells[i].style,this.$s6,_2)}
return}
if(!isc.Browser.isMoz)return;var _4=this.getHandle().firstChild.rows[1].cells[1],_5=this.getHeight()-this.$z9-this.$0a;if(_5<0)_5=0;this.$z6(_4.style,this.$s6,_5);if(this.forceMozRowHeight){var _6=_4.parentNode.cells;this.$z6(_6[0].style,this.$s6,_5);this.$z6(_6[2].style,this.$s6,_5)}}
,isc.A.layoutChildren=function isc_EdgedCanvas_layoutChildren(_1,_2,_3){var _4=this.children;if(!_4)return;isc.Canvas.$ce.layoutChildren.call(this,_1,_2,_3);if(_4.length==0)return;var _5=_4[0];_5.setRect(this.$xw,this.$xy,this.getInnerWidth(),this.getInnerHeight())}
,isc.A.addChild=function isc_EdgedCanvas_addChild(_1,_2,_3){isc.Canvas.$ce.addChild.call(this,_1,_2,_3);this.layoutChildren("addChild")}
,isc.A.draw=function isc_EdgedCanvas_draw(_1,_2,_3,_4){if(!this.readyToDraw())return this;if(this.masterElement){var _5=this.masterElement,_6=false;while(_5){if(_5.position==this.$tl){_6=true;break}
_5=_5.parentElement}
if(!_6||isc.Page.isLoaded())this.fitToMaster();else isc.Page.setEvent("load",this,isc.Page.FIRE_ONCE,"fitToMaster")}
this.invokeSuper(isc.EdgedCanvas,"draw",_1,_2,_3,_4);this.$yl();return this}
,isc.A.fitToMaster=function isc_EdgedCanvas_fitToMaster(){if(this.destroyed)return;var _1=this.masterElement;if(_1.$zq){isc.Timer.setTimeout({target:this,methodName:"fitToMaster"},200);return}else if(_1.$v3){_1.adjustOverflow()}
var _2=_1.$xu();this.setRect(_1.getOffsetLeft()+_2.left,_1.getOffsetTop()+_2.top,Math.max(1,(_1.getVisibleWidth()-_2.left-_2.right)),Math.max(1,(_1.getVisibleHeight()-_2.top-_2.bottom)))}
,isc.A.redraw=function isc_EdgedCanvas_redraw(){this.Super("redraw",arguments);this.$yl();return this}
,isc.A.masterResized=function isc_EdgedCanvas_masterResized(){var _1=this.masterElement;if(this.masterElement.isAnimating([this.$d9,this.$x4]))return;var _2=isc.Browser.isSafari;if(_2){this.delayCall("$5n",[_1])}else{this.$5n(_1)}}
,isc.A.$5n=function isc_EdgedCanvas__sizeToMaster(_1){if(this.destroyed||_1!=this.masterElement)return;var _2=_1.$xu();this.resizeTo(Math.max(1,_1.getVisibleWidth()-_2.left-_2.right),Math.max(1,_1.getVisibleHeight()-_2.top-_2.bottom))}
,isc.A.setEdgeImage=function isc_EdgedCanvas_setEdgeImage(_1){if(this.edgeImage==_1)return;this.edgeImage=_1;this.markForRedraw("setEdgeImage")}
);isc.B._maxIndex=isc.C+17;isc.defineClass("DropShadow","EdgedCanvas");isc.A=isc.DropShadow.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.skinImgDir="images/shared/shadows/";isc.A.edgeImage="[SKIN]ds.png";isc.A.isMouseTransparent=true;isc.A.shownEdges={center:true,TL:true,T:true,TR:true,L:true,R:true,BL:true,B:true,BR:true};isc.A.depth=4;isc.A.showShadow=false;isc.B.push(isc.A.initWidget=function isc_DropShadow_initWidget(){this.setDepth(this.depth);this.Super(this.$sb)}
,isc.A.setDepth=function isc_DropShadow_setDepth(_1){if(_1!=null)this.depth=_1;var _2=(this.softness||this.depth),_3=this.$5o!=_2;this.$5o=_2;this.edgeSize=2*this.$5o;var _4=this.$ef(this.offset,Math.round(this.depth/ 2));this.$5p=this.$ef(this.offsetX,_4);this.$5q=this.$ef(this.offsetY,_4);this.updateEdgeSizes();if(this.isDrawn()){this.masterMoved();if(_3||this.isDirty())this.redraw();this.masterResized()}}
,isc.A.getEdgePrefix=function isc_DropShadow_getEdgePrefix(_1){if(_1!=isc.Canvas.CENTER)return this.$5o}
,isc.A.masterMoved=function isc_DropShadow_masterMoved(){var _1=this.visibility==isc.Canvas.HIDDEN,_2=this.masterElement,_3=_2.getOffsetLeft(),_4=_2.getOffsetTop();if(!_1){_3+=this.$5p-this.$5o;_4+=this.$5q-this.$5o}
this.moveTo(_3,_4)}
,isc.A.masterResized=function isc_DropShadow_masterResized(){if(this.visibility==isc.Canvas.HIDDEN)return;var _1=this.masterElement;this.resizeTo(_1.getVisibleWidth()+2*this.$5o,_1.getVisibleHeight()+2*this.$5o)}
,isc.A.fitToMaster=function isc_DropShadow_fitToMaster(){this.masterMoved();this.masterResized()}
,isc.A.setVisibility=function isc_DropShadow_setVisibility(_1,_2,_3,_4,_5){var _6=(_1!=this.visibility);this.invokeSuper(isc.DropShadow,"setVisibility",_1,_2,_3,_4,_5);if(_6){if(_1==isc.Canvas.HIDDEN){this.resizeTo(1,1);var _7=this.masterElement;this.moveTo(_7.getOffsetLeft(),_7.getOffsetTop())}else{this.fitToMaster()}}}
,isc.A.getCurrentCursor=function isc_DropShadow_getCurrentCursor(_1,_2,_3,_4){var _5=this.masterElement;if(_5&&_5.dragResizeFromShadow)this.canDragResize=_5.canDragResize;return this.invokeSuper(isc.DropShadow,"getCurrentCursor",_1,_2,_3,_4)}
,isc.A.prepareForDragging=function isc_DropShadow_prepareForDragging(_1,_2,_3,_4){var _5=this.masterElement;if(_5&&_5.dragResizeFromShadow)this.canDragResize=_5.canDragResize;return this.invokeSuper(isc.DropShadow,"prepareForDragging",_1,_2,_3,_4)}
);isc.B._maxIndex=isc.C+9;isc.ClassFactory.defineClass("Hover");isc.A=isc.Hover;isc.A.delay=500;isc.A.leftOffset=15;isc.A.topOffset=15;isc.A.hoverCanvasDefaults={defaultWidth:100,defaultHeight:1,baseStyle:"canvasHover",align:isc.Canvas.LEFT,valign:isc.Canvas.TOP,wrap:true,autoDraw:false};isc.A=isc.Hover;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.show=function isc_c_Hover_show(_1,_2,_3,_4){if(isc.isA.Canvas(_1)){this.showingHoverComponent=true;this.hoverCanvas=_1;this.hoverCanvas.hide=function(){this.Super("hide",arguments);isc.Hover.hoverCanvasHidden()};_4.hoverCanvas=_1}
if(!this.hoverCanvas)this.$5r();var _5=this.hoverCanvas;if(_1==null||_1==""){_5.hide();return}
this.lastHoverCanvas=_4;if(!this.showingHoverComponent)_5.setContents(_1);if(_2==null)_2={};var _6=this.hoverCanvasDefaults;if(_5.setAlign)_5.setAlign(_2.align||_6.align);if(_5.setVAlign)_5.setVAlign(_2.valign||_6.valign);if(_5.setBaseStyle)_5.setBaseStyle(_2.baseStyle||_6.baseStyle);if(_5.setOpacity)_5.setOpacity(_2.opacity||_6.opacity);if(_5.setWrap)_5.setWrap(_2.wrap!=null?_2.wrap:_6.wrap);if(_2.moveWithMouse!=null)this.$5s=_2.moveWithMouse
else this.$5s=this.moveWithMouse;var _7=isc.EH.getX(),_8=isc.EH.getY(),_9=_2.left,_10=_2.top,_11=_2.width||(this.showingHoverComponent?_5.width:_6.defaultWidth),_12=_2.height||(this.showingHoverComponent?_5.height:_6.defaultHeight);if(_9!=null||_10!=null){_9=_9?_9:_7+this.leftOffset;_10=_10?_10:_8+this.topOffset}else{_5.setRect(null,-9999,_11,_12);if(!_5.isDrawn())_5.draw();if(!_5.isVisible())_5.show();else _5.redrawIfDirty("placing hover");var _13=_3?_3:[_7-this.leftOffset,_8-this.topOffset,2*this.leftOffset,2*this.topOffset];var _14=_5.getPeerRect();var _15=isc.Canvas.$yh(_14[2],_14[3],_13,"bottom",false,"outside-right");_9=_15[0];_10=_15[1]}
_5.setRect(_9,_10,_11,_12);_5.bringToFront();if(!_5.isDrawn()||!_5.isVisible())_5.show();if(this.$5s){this.$5t=isc.Page.setEvent("mouseMove",function(){isc.Hover.$5u()})}
return}
,isc.A.hoverCanvasHidden=function isc_c_Hover_hoverCanvasHidden(){var _1=this.lastHoverCanvas;delete this.lastHoverCanvas;if(_1!=null){_1.$1f()}}
,isc.A.hide=function isc_c_Hover_hide(){var _1=isc.Hover.hoverCanvas;if(_1!=null){if(this.$5t){isc.Page.clearEvent("mouseMove",this.$5t);delete this.$5t}
_1.hide();if(this.showingHoverComponent){if(!_1)return;delete this.hoverCanvas;this.showingHoverComponent=false}else{var _2=this.hoverCanvasDefaults;_1.setRect(0,-1000)}}}
,isc.A.$5r=function isc_c_Hover__makeHoverCanvas(){var _1=isc.addProperties({hide:function(){this.Super("hide",arguments);isc.Hover.hoverCanvasHidden()}},this.hoverCanvasDefaults);this.hoverCanvas=isc.Label.create(_1)}
,isc.A.$5u=function isc_c_Hover__moveWithMouse(){var _1=this.hoverCanvas.getPeerRect();var _2=isc.Canvas.$yh(_1[2],_1[3],this.getMousePointerRect(),"bottom",false,"outside-right");this.hoverCanvas.moveTo(_2[0],_2[1])}
,isc.A.getMousePointerRect=function isc_c_Hover_getMousePointerRect(){return[isc.EH.getX()-this.leftOffset,isc.EH.getY()-this.topOffset,2*this.leftOffset,2*this.topOffset]}
,isc.A.setAction=function isc_c_Hover_setAction(_1,_2,_3,_4){if(_4==null)_4=this.delay;if(this.isActive||_4==0){_2.apply((_1?_1:this),_3?_3:[]);this.isActive=true}
else{if(this.timer!=null)this.timer=isc.Timer.clear(this.timer);this.actionTarget=(_1?_1:this);this.action=_2;this.actionArgs=_3?_3:[];this.timer=isc.Timer.setTimeout({target:isc.Hover,methodName:"$5v"},_4)}}
,isc.A.$5v=function isc_c_Hover__doAction(){if(this.action&&!this.actionTarget.destroyed){this.action.apply(this.actionTarget,this.actionArgs)}
this.actionTarget=this.action=this.actionArgs=null;this.isActive=true}
,isc.A.clear=function isc_c_Hover_clear(){this.hide();if(this.timer!=null)this.timer=isc.Timer.clear(this.timer);this.actionTarget=this.action=this.actionArgs=null;this.isActive=false}
);isc.B._maxIndex=isc.C+9;if(!isc.Comm)isc.ClassFactory.defineClass("Comm");isc.A=isc.Comm;isc.A.$5w=/^[\$_a-zA-Z][\$\w]*$/;isc.A.BACKREF_PREFIX="$$BACKREF$$:";isc.A.indent="    ";isc.A=isc.Comm;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.serialize=function isc_c_Comm_serialize(_1,_2){var _3={strictQuoting:false,dateFormat:"dateConstructor"};if(_2!=null)_3.prettyPrint=_2;return isc.JSON.encode(_1,_3)}
);isc.B._maxIndex=isc.C+1;isc.ClassFactory.defineClass("JSON",null,null,true);isc.A=isc.JSON;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.encode=function isc_c_JSON_encode(_1,_2){return isc.JSONEncoder.create(_2).encode(_1)}
,isc.A.decode=function isc_c_JSON_decode(_1){return eval("("+_1+")")}
);isc.B._maxIndex=isc.C+2;isc.ClassFactory.defineClass("JSONEncoder");isc.A=isc.JSONEncoder;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.$5x=function isc_c_JSONEncoder__serialize_remember(_1,_2,_3){_1.obj.add(_2);_1.path.add(_3)}
,isc.A.$5y=function isc_c_JSONEncoder__serialize_cleanNode(_1){var _2=_1["$5z"];if(_2!=null){var _3=window[_2];if(_3&&_3.parentProperty&&_1[_3.parentProperty]){_1=_3.getCleanNodeData(_1)}}
return _1}
,isc.A.$50=function isc_c_JSONEncoder__serialize_alreadyReferenced(_1,_2){var _3=_1.obj.indexOf(_2);if(_3==-1)return null;return _1.path[_3]}
,isc.A.$51=function isc_c_JSONEncoder__serialize_addToPath(_1,_2){if(isc.isA.Number(_2)){return _1+"["+_2+"]"}else if(!isc.Comm.$5w.test(_2)){return _1+'["'+_2+'"]'}else{return _1+"."+_2}}
);isc.B._maxIndex=isc.C+4;isc.A=isc.JSONEncoder.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.serializeInstances=true;isc.A.dateFormat="xmlSchema";isc.A.strictQuoting=true;isc.A.circularReferenceMode="path";isc.A.circularReferenceMarker="$$BACKREF$$";isc.A.prettyPrint=true;isc.B.push(isc.A.encode=function isc_JSONEncoder_encode(_1){this.objRefs={obj:[],path:[]};var _2=this.$fs(_1,this.prettyPrint?"":null,null);this.objRefs=null;return _2}
,isc.A.encodeDate=function isc_JSONEncoder_encodeDate(_1){if(this.dateFormat=="dateConstructor"){return _1.$fs()}else{return'"'+_1.toSchemaDate()+'"'}}
,isc.A.$fs=function isc_JSONEncoder__serialize(_1,_2,_3){if(!_3){if(_1&&_1.getID)_3=_1.getID();else _3=""}
if(_1==null)return null;if(isc.isA.String(_1))return(_1.asSource!=null?_1.asSource():String.asSource(_1));if(isc.isA.Function(_1))return null;if(isc.isA.Number(_1)||isc.isA.SpecialNumber(_1))return _1;if(isc.isA.Boolean(_1))return _1;if(isc.isA.Date(_1))return this.encodeDate(_1);if(isc.isAn.Instance(_1)){if(this.serializeInstances=="skip")return null;else if(this.serializeInstances=="short")return isc.echoLeaf(_1)}
if(isc.isA.Class(_1)){if(this.serializeInstances=="skip")return null;else if(this.serializeInstances=="short")return isc.echoLeaf(_1)}
var _4=isc.JSONEncoder.$50(this.objRefs,_1);if(_4!=null&&_3.contains(_4)){var _5=_3.substring(_4.length,_4.length+1);if(_5=="."||_5=="["||_5=="]"){var _6=this.circularReferenceMode;if(_6=="marker"){return"'"+this.circularReferenceMarker+"'"}else if(_6=="path"){return"'"+this.circularReferenceMarker+":"+_4+"'"}else{return null}}}
if(_1==window){this.logWarn("Serializer encountered the window object at path: "+_3+" - returning null for this slot.");return null}
isc.JSONEncoder.$5x(this.objRefs,_1,_3);if(isc.isA.Function(_1.$fs))return _1.$fs(_2,this.objRefs,_3);if(isc.isAn.Array(_1))return this.$52(_1,_3,this.objRefs,_2);var _7;if(_1.getSerializeableFields){_7=_1.getSerializeableFields([],[])}else{_7=_1}
return this.$53(_7,_3,this.objRefs,_2)}
,isc.A.$52=function isc_JSONEncoder__serializeArray(_1,_2,_3,_4){var _5=isc.SB.create();_5.append("[");for(var i=0,_7=_1.length;i<_7;i++){var _8=_1[i];if(_4!=null)_5.append("\r",_4,isc.Comm.indent);var _2=isc.JSONEncoder.$51(_2,i);var _9=this.$fs(_8,(_4!=null?_4+isc.Comm.indent:null),_2);_5.append(_9+",");if(_4!=null)_5.append(" ")}
_5=_5.toString();var _10=_5.lastIndexOf(",");if(_10>-1)_5=_5.substring(0,_10);if(_4!=null)_5+="\r"+_4;_5+="]";return _5}
,isc.A.$53=function isc_JSONEncoder__serializeObject(_1,_2,_3,_4){var _5=isc.SB.create(),_6;_1=isc.JSONEncoder.$5y(_1);try{for(var _7 in _1)break}catch(e){if(this.showDebugOutput){if(isc.isAn.XMLNode(_1))return isc.echoLeaf(_1);var _8;if(e.message){_8=(e.message.asSource!=null?e.message.asSource():String.asSource(e.message));return"{ cantEchoObject: "+_8+"}"}else{return"{ cantEchoObject: 'unspecified error' }"}}else return null}
_5.append("{");for(var _7 in _1){if(_7==null)continue;if(this.skipInternalProperties&&(isc.startsWith(_7,isc.$ak)||isc.startsWith(_7,isc.$al)))continue;var _9=_1[_7];if(isc.isA.Function(_9))continue;if(_7!=isc.gwtRef&&isc.isAn.Instance(_9)&&this.serializeInstances=="skip")continue;var _10=_7.toString();if(!isc.Comm.$5w.test(_10)||this.strictQuoting){if(_10.contains('"')){if(_10.contains("'")){_10='"'+this.convertToEncodedQuotes(_10)+'"'}else{_10="'"+_10+"'"}}else{_10='"'+_10+'"'}}
var _2=isc.JSONEncoder.$51(_2,_7);var _11;if(_7==isc.gwtRef){if(!this.showDebugOutput)continue;_11="{GWT Java Obj}"}else{_11=this.$fs(_9,(_4!=null?_4+isc.Comm.indent:null),_2)}
if(_4!=null)_5.append("\r",_4,isc.Comm.indent);_5.append(_10,":"+_11,",");if(_4!=null)_5.append(" ")}
_5=_5.toString();var _12=_5.lastIndexOf(",");if(_12>-1)_5=_5.substring(0,_12);if(_4!=null)_5+="\r"+_4;_5+="}";return _5}
,isc.A.convertToEncodedQuotes=function isc_JSONEncoder_convertToEncodedQuotes(_1){return _1.replace(String.$fz,"&quot;").replace(String.$fy,"&apos;")}
,isc.A.convertFromEncodedQuotes=function isc_JSONEncoder_convertFromEncodedQuotes(_1){return _1.replace(new RegExp("&quot;","g"),'"').replace(new RegExp("&apos;","g"),"'")}
);isc.B._maxIndex=isc.C+7;isc.addGlobal("clone",function(_1,_2){return isc.Comm.$54(_1)});isc.addGlobal("shallowClone",function(_1){return isc.Comm.$55(_1)});isc.A=isc.Comm;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.clone=isc.clone;isc.A.shallowClone=isc.shallowClone;isc.B.push(isc.A.$54=function isc_c_Comm__clone(_1){var _2;if(_1===_2)return _2;if(_1==null)return null;if(isc.isA.String(_1)||isc.isA.Boolean(_1)||isc.isA.Number(_1)||isc.isA.Function(_1))return _1;if(isc.isA.Date(_1))return _1.duplicate();if(isc.isAn.Array(_1))return isc.Comm.$56(_1);if(isc.isA.Function(_1.clone)){if(isc.isA.Class(_1))return isc.echoLeaf(_1);return _1.clone()}
return isc.Comm.$57(_1)}
,isc.A.$56=function isc_c_Comm__cloneArray(_1){var _2=[];for(var i=0,_4=_1.length;i<_4;i++){_2[i]=isc.Comm.$54(_1[i])}
return _2}
,isc.A.$57=function isc_c_Comm__cloneObject(_1){var _2={};for(var _3 in _1){var _4=_1[_3];if(_3==isc.gwtRef)continue;_2[_3]=isc.Comm.$54(_4)}
return _2}
,isc.A.$55=function isc_c_Comm__shallowClone(_1){var _2;if(_1===_2)return _2;if(_1==null)return null;if(isc.isA.String(_1)||isc.isA.Boolean(_1)||isc.isA.Number(_1)||isc.isA.Function(_1))return _1;if(isc.isA.Date(_1))return _1.duplicate();if(isc.isAn.Array(_1))return isc.Comm.$58(_1);return isc.addProperties({},_1)}
,isc.A.$58=function isc_c_Comm__shallowCloneArray(_1){var _2=[];for(var i=0,_4=_1.length;i<_4;i++){if(isc.isAn.Array(_1[i]))_2[i]=_1[i];else _2[i]=isc.Comm.$55(_1[i])}
return _2}
);isc.B._maxIndex=isc.C+5;isc.defineClass("AutoTest");isc.A=isc.AutoTest;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.fallback_valueOnlyField="$59";isc.A.fallback_startMarker="[";isc.A.fallback_endMarker="]";isc.A.fallback_separator="||";isc.A.fallback_equalMarker="=";isc.A.slashMarker="$fs$";isc.A.robustLocatorWarning="If you are seeing unexpected results in recorded tests, it is likely"+" that the application has been modified since the test was recorded. We would recommend re-recording"+" your test script with the latest version of your application. Note that you may be able to"+" avoid seeing this message in future by using the AutoChild subsystem or providing explicit"+" global IDs to components whose function within the page is unlikely to change.";isc.B.push(isc.A.getLocator=function isc_c_AutoTest_getLocator(_1,_2){var _3;if(_1==null){_3=true;_1=isc.EH.lastEvent?isc.EH.lastEvent.nativeTarget:null}
var _4;if(isc.isA.Canvas(_1))_4=_1;else{_4=isc.AutoTest.locateCanvasFromDOMElement(_1)}
var _5=_4?_4.getLocator(_1,_3):"";if(_2&&_5&&_5!=""&&_4.checkLocatorForNativeElement(_5,_1))
{_5=""}
return _5}
,isc.A.locateCanvasFromDOMElement=function isc_c_AutoTest_locateCanvasFromDOMElement(_1){return isc.EH.getEventTargetCanvas(null,_1)}
,isc.A.getElement=function isc_c_AutoTest_getElement(_1){if(!_1)return null;if(_1.startsWith("'")||_1.startsWith('"'))_1=_1.substring(1);if(_1.endsWith("'")||_1.endsWith('"'))_1=_1.substring(0,_1.length-1);if(!_1.startsWith("//")){if(_1.startsWith("ID=")||_1.startsWith("id=")){_1=_1.substring(3)}
_1='//*any*[ID="'+_1+'"]'}
var _2=_1.split("/"),_3;var _4=_2[2];if(!_4)return null;_2=_2.slice(3);var _5=this.getBaseComponentFromLocatorSubstring(_4);var _6=_5?_5.getElementFromSplitLocator(_2):null;return _6}
,isc.A.getPageCoords=function isc_c_AutoTest_getPageCoords(_1){var _2=this.getElement(_1);if(_2==null)return;var _3=this.locateCanvasFromDOMElement(_2);return _3?_3.getAutoTestLocatorCoords(_1,_2):null}
,isc.A.getBaseComponentFromLocatorSubstring=function isc_c_AutoTest_getBaseComponentFromLocatorSubstring(_1){var _2=_1.match("(.*)\\[");var _3=_2?_2[1]:null;if(_3=="autoID"){var _4=isc.AutoTest.parseLocatorFallbackPath(_1),_5=_4.config,_6="name",_7="Class";return isc.Canvas.getCanvasFromFallbackLocator(_1,_5,isc.Canvas.$x3,_6,_7)}else{var _8=_3,_2=_1.match('ID=[\\"\'](.*)[\'\\"]'),_9=_2?_2[1]:null;if(_9==null)return null;var _10=window[_9];if(!_10)return null;if(_10&&_8!="*any*"&&(!isc.isA[_8]||!isc.isA[_8](_10)))
{this.logWarn("AutoTest.getElement(): Component:"+_10+" expected to be of class:"+_8)}
return _10}}
,isc.A.getLocatorCanvas=function isc_c_AutoTest_getLocatorCanvas(_1){if(_1==null||isc.isAn.emptyString(_1))return null;var _2=_1.split("/"),_3;if(_2==null||_2.length<3)return null;var _4=_2[2];var _5=_2.length;for(var i=3;i<_5;i++){_2[i-3]=_2[i]}
_2.length=_5-3;if(!_4)return null;var _7=this.getBaseComponentFromLocatorSubstring(_4);if(_7){var i=0,_8=_7.getChildFromLocatorSubstring(_2[i],i,_2);while(_8!=null){i++;_7=_8;_8=_7.getChildFromLocatorSubstring(_2[i],i,_2)}
return _7}
return null}
,isc.A.getLocatorFormItem=function isc_c_AutoTest_getLocatorFormItem(_1){if(_1==null||isc.isAn.emptyString(_1))return null;var _2=_1.split("/"),_3;if(_2==null||_2.length<3)return null;var _4=_2[2];var _5=_2.length;for(var i=3;i<_5;i++){_2[i-3]=_2[i]}
_2.length=_5-3;if(!_4)return null;var _7=this.getBaseComponentFromLocatorSubstring(_4);if(_7){var _8=_7.getChildFromLocatorSubstring(_2[0],0,_2);while(_8!=null){_2.removeAt(0);_7=_8;_8=_7.getChildFromLocatorSubstring(_2[0],0,_2)}}
if(isc.isA.DynamicForm(_7)){return _7.getItemFromSplitLocator(_2)}
return null}
,isc.A.createLocatorFallbackPath=function isc_c_AutoTest_createLocatorFallbackPath(_1,_2){var _3=[];for(var _4 in _2){var _5=_2[_4];if(isc.isA.String(_5)){_5=_5.replace("/",this.slashMarker);_5=escape(_5)}
if(_4==this.fallback_valueOnlyField){_3.add(_5)}else{_3.add(_4+this.fallback_equalMarker+_5)}}
return _1+this.fallback_startMarker+_3.join(this.fallback_separator)+this.fallback_endMarker}
,isc.A.parseLocatorFallbackPath=function isc_c_AutoTest_parseLocatorFallbackPath(_1){var _2=_1.split(this.fallback_startMarker);if(_2==null||_2.length<2)return;var _3=_2[0],_1=_2[1].substring(0,_2[1].length-this.fallback_endMarker.length);var _4=_1.split(this.fallback_separator),_5={};for(var i=0;i<_4.length;i++){var _7=_4[i],_8=_7.indexOf(this.fallback_equalMarker),_9;if(_8==-1){_9=this.fallback_valueOnlyField}else{_9=_7.substring(0,_8);_7=_7.substring(_8+1)}
_7=_7.replace(this.slashMarker,"/");_7=unescape(_7);_5[_9]=_7}
if(_2[2]!=null){var _7=_2[2].substring(0,_2[2].length-this.fallback_endMarker.length),_8=_7.indexOf(this.fallback_equalMarker),_10=_7.substring(0,_8),_11=_7.substring(_8+1);if(_11.startsWith("\""))_11=_11.substring(1,_11.length-1);_5[_10]=_11}
return{name:_3,config:_5}}
,isc.A.getObjectLocatorFallbackPath=function isc_c_AutoTest_getObjectLocatorFallbackPath(_1,_2,_3,_4){if(_3==null)_3={};if(_4==null)_4={title:"title",Class:"ClassName"};if(isc.isAn.Array(_4)){for(var i=0;i<_4.length;i++){var _6=_2.getProperty?_2.getProperty(_4[i]):_2[_4[i]];if(_6!=null&&!isc.isAn.emptyString(_6))_3[_4[i]]=_6}}else{for(var _7 in _4){var _6=_2.getProperty?_2.getProperty(_4[_7]):_2[_4[_7]];if(_6!=null&&!isc.isAn.emptyString(_6))_3[_7]=_6}}
return isc.AutoTest.createLocatorFallbackPath(_1,_3)}
,isc.A.logRobustLocatorWarning=function isc_c_AutoTest_logRobustLocatorWarning(){if(this.$6a)return;this.logWarn(this.robustLocatorWarning,"AutoTest");this.$6a=true}
);isc.B._maxIndex=isc.C+11;isc.ApplyAutoTestMethods=function(){isc.Canvas.addClassMethods({getCanvasLocatorFallbackPath:function(_9,_39,_159,_160,_161){if(_160==null)_160={};if(_161==null)_161={};else if(isc.isAn.Array(_161)){var _1={};for(var i=0;i<_161.length;i++){_1[_161[i]]=_161[i]}
_161=_1}
if(_161.title==null)_161.title="title";if(_161.scRole==null)_161.scRole="ariaRole";if(_161.name==null)_161.name="name";var _3=_39.getClassName(),_4=_39.getClass();_160.Class=_3;var _5;if(!_4.isFrameworkClass){_5=_4.$6b}
if(_5!=null)_160.scClass=_5;if(_159!=null){_160.index=_159.indexOf(_39);_160.length=_159.length;var _6=_159.findAll("Class",_3);_160.classIndex=_6.indexOf(_39);_160.classLength=_6.length;if(_5!=null){var _7=_159.findAll("$ci",_5);_160.scClassIndex=_7.indexOf(_39);_160.scClassLength=_7.length}
if(_39.ariaRole!=null){var _8=_159.findAll("ariaRole",_39.ariaRole);_160.roleIndex=_8.indexOf(_39);_160.roleLength=_8.length}}
return isc.AutoTest.getObjectLocatorFallbackPath(_9,_39,_160,_161)},getCanvasFromFallbackLocator:function(_159,_43,_44,_45,_46){var _9=_43.name;var _10=_43.Class,_5=_43.scClass||_43.Class,_11=_43.scRole;switch(_45){case"name":if(_9!=null){var _12=_44.find("name",_9);if(_12){switch(_46){case"Class":if(_10&&isc.isA[_10]&&isc.isA[_10](_12)){if(this.logIsDebugEnabled("AutoTest")){this.logDebug("Locator string:"+_159+" - returning widget with matching name and ClassName:"+_12,"AutoTest")}
return _12}
case"scClass":if(_5&&isc.isA[_5]&&isc.isA[_5](_12))
{if(this.logIsDebugEnabled("AutoTest")){this.logDebug("Locator string:"+_159+" - returning widget with matching name and scClassName:"+_12,"AutoTest")}
return _12}
case"role":var _13=_43.scRole;if(_12.ariaRole==_13){if(this.logIsDebugEnabled("AutoTest")){this.logDebug("Locator string:"+_159+" - returning widget with matching name and role:"+_12,"AutoTest")}
return _12}
default:if(_46!="none"){isc.AutoTest.logRobustLocatorWarning();this.logWarn("Locator string:"+_159+". Returning closest match:"+_12+". This has the same name "+"as the recorded component but does not match class or role. ","AutoTest")}else{if(this.logIsDebugEnabled("AutoTest")){this.logDebug("Locator string:"+_159+" - returning widget with matching name:"+_12,"AutoTest")}}
return _12}}}
case"title":var _14=_43.title;if(_14!=null){var _15=_44.findAll("title",_14);if(_15&&_15.length>0){var _16;switch(_46){case"Class":if(_10){var _17=_15.findAll("Class",_10);if(_17!=null){_16=_17[0];if(_17.length==1&&_16){if(this.logIsDebugEnabled("AutoTest")){this.logDebug("Locator string:"+_159+" - returning widget with matching title and ClassName:"+_16,"AutoTest")}
return _16}}}
case"scClass":if(_5){var _17=_15.findAll("$ci",_5);if(_17!=null){if(_17.length==1||_16==null)
_16=_17[0];if(_17.length==1&&_16){if(this.logIsDebugEnabled("AutoTest")){this.logDebug("Locator string:"+_159+" - returning widget with matching name and scClassName:"+_16,"AutoTest")}
return _16}}}
case"role":if(_11){var _17=_15.findAll("ariaRole",_11);if(_17!=null){if(_17.length==1||_16==null)
_16=_17[0];if(_17.length==1&&_16){if(this.logIsDebugEnabled("AutoTest")){this.logDebug("Locator string:"+_159+" - returning widget with matching title and role:"+_16,"AutoTest")}
return _16}}}
default:if(_15.length==1){if(_46!="none"){isc.AutoTest.logRobustLocatorWarning();this.logWarn("Locator string:"+_159+". Returning closest match:"+_15[0]+". This has the same title "+"as the recorded component but does not match class or role.","AutoTest")}else{if(this.logIsDebugEnabled("AutoTest")){this.logDebug("Locator string:"+_159+" - returning widget with matching title:"+_16,"AutoTest")}}
return _15[0]}else{this.logWarn("Locator string:"+_159+", attempt to match by title failed -- multiple candidate components have this "+"same title. Attempting to match by index instead.","AutoTest")}}}}
default:var _18,_19,_20;switch(_46){case"Class":if(_10&&_43.classIndex){var _21=_44.findAll("Class",_10);if(_21&&_21.length>0){_18=_21[parseInt(_43.classIndex)];if(_21.length==parseInt(_43.classLength)){if(this.logIsInfoEnabled("AutoTest")){this.logInfo("Locator string:"+_159+" - returning widget with matching ClassName / index by ClassName:"+_18,"AutoTest")}
return _18}}}
case"scClass":if(_5&&_43.scClassIndex){var _22=_44.findAll("$ci",_5);if(_22&&_22.length>0){_19=_22[parseInt(_43.scClassIndex)];if(_22.length==parseInt(_43.scClassLength)){if(this.logIsInfoEnabled("AutoTest")){this.logInfo("Locator string:"+_159+" - returning widget with matching SmartClient superclass / index by ClassName:"+_19,"AutoTest")}
return _19}}}
case"role":if(_11&&_43.roleIndex){var _23=_44.findAll("ariaRole",_11);if(_23&&_23.length>0){_20=_23[parseInt(_43.roleIndex)];if(_23.length==parseInt(_43.roleLength)){if(this.logIsInfoEnabled("AutoTest")){this.logInfo("Locator string:"+_159+" - returning widget with matching role / index by role:"+_20,"AutoTest")}
return _20}}}
default:if((_46!="none"&&(_10||_5||_11))||(_43.length!=null&&(parseInt(_43.length)!=_44.length)))
{isc.AutoTest.logRobustLocatorWarning()}
var _24=_18||_19||_20;if(_24==null){var _25=_43[isc.AutoTest.fallback_valueOnlyField];if(_25==null)_25=_43.index;_25=parseInt(_25);_24=_44[_25]}
if(_24){this.logWarn("Locator string:"+_159+" matching by index gave "+_24+". Reliability cannot be guaranteed for matching by index if the underlying "+"application undergoes any changes.","AutoTest");return _24}}}
this.logDebug("AutoTest.getElement(): locator substring:"+_159+" parsed to fallback locator name:"+_9+", unable to find relevant child - may refer to inner element.","AutoTest")}});isc.Canvas.addMethods({getLocator:function(_50,_159){var _26,_27;if(this._generated||this.locatorParent||this.creator||this.$cm){_27=this.getLocatorParent()}
if(!_27){_26=this.getLocatorRoot()}else{_26=_27.getLocator()+"/"+_27.getChildLocator(this)}
if(_50)return[_26,this.getInteriorLocator(_50,_159)].join("/");return _26},$6c:["//",,'[ID="',,'"]'],getLocatorRoot:function(){if(!this.locatorRoot){if(this.$cm&&this.parentElement==null){this.locatorRoot="//"+isc.Canvas.getCanvasLocatorFallbackPath("autoID",this,isc.Canvas.$x3)}else{this.$6c[1]=this.getClassName();this.$6c[3]=this.getID();this.locatorRoot=this.$6c.join(isc.emptyString)}}
return this.locatorRoot},containsLocatorChild:function(_39){if(this.namedLocatorChildren!=null){for(var i=0;i<this.namedLocatorChildren.length;i++){var _9=this.namedLocatorChildren[i];if(isc.isAn.Object(_9))_9=_9.attribute;if(_39==this[_9]){return true}}}
return false},getLocatorParent:function(){if(this.locatorParent&&this.locatorParent.containsLocatorChild(this)){return this.locatorParent}
if(this.creator&&isc.isA.Canvas(this.creator)){var _28=this.creator.getAutoChildLocator(this);if(_28==null){this.logInfo("Locator code failed to find relationship between parent:"+this.creator.getID()+" and autoChild:"+this.getID(),"AutoTest")}else{return this.creator}}
return this.masterElement||this.parentElement},$6d:[,"[",,'][Class="',,'"]'],getChildLocator:function(_39){if(_39==this.hscrollbar){return"hscrollbar"}
if(_39==this.vscrollbar){return"vscrollbar"}
if(_39.creator==this){var _29=this.getAutoChildLocator(_39);if(_29)return _29}
return this.getStandardChildLocator(_39)},checkLocatorForNativeElement:function(_67,_50){if(_50==null||_67==null)return false;return(isc.EventHandler.eventHandledNatively("mousedown",_50,true)&&(isc.AutoTest.getElement(_67)!=_50))},getCanvasLocatorFallbackPath:function(_9,_39,_159,_160,_161){return isc.Canvas.getCanvasLocatorFallbackPath(_9,_39,_159,_160,_161)},getAutoChildLocator:function(_39){if(this.$ep){var _30=_39.getID();for(var _31 in this.$ep){var _32=this.$ep[_31];if(_32.contains(_30)){if(_39==this[_31])return _31;else{var _33=[];for(var i=0;i<_32.length;i++){_33[i]=window[_32[i]]}
return this.getCanvasLocatorFallbackPath(_31,_39,_33)}}}}
return null},getNamedLocatorChildString:function(_39){if(_39.locatorParent==this&&this.namedLocatorChildren){for(var i=0;i<this.namedLocatorChildren.length;i++){var _9=this.namedLocatorChildren[i],_34=_9;if(isc.isA.Object(_9)){_34=_9.attribute,_9=_9.name}
if(_39==this[_34]){return _9}}}},getStandardChildLocator:function(_39){var _35=this.getNamedLocatorChildString(_39);if(_35)return _35;if(_39.masterElement==this){return this.getCanvasLocatorFallbackPath("peer",_39,this.peers)}else if(_39.parentElement==this){return this.getCanvasLocatorFallbackPath("child",_39,this.children)}else{this.logWarn("unexpected error - failed to find relationship between parent:"+this.getID()+" and child:"+_39.getID());return _39.getLocatorRoot()}},getInteriorLocator:function(_50,_159){if(_50&&this.useEventParts){var _36=this.getElementPart(_50);if(_36!=null&&_36.part!=null){return(_36.partID&&_36.partID!=isc.emptyString)?_36.part+"_"+_36.partID:_36.part}}
return isc.emptyString},getElementFromSplitLocator:function(_159){var _37=this.getChildFromLocatorSubstring(_159[0],0,_159);if(_37){_159.removeAt(0);return _37.getElementFromSplitLocator(_159)}
return this.getInnerElementFromSplitLocator(_159)},getChildFromLocatorSubstring:function(_159,_25,_160){if(_159==null||_159=="")return null;if(isc.isA.Canvas(this[_159])){return this[_159]}
if(this.namedLocatorChildren!=null){var _38=this.namedLocatorChildren.find("name",_159);if(_38!=null){var _39=this[_38.attribute];if(isc.isA.Canvas(_39))return _39;this.logWarn("Locator substring:"+_159+" remaps to attribute:"+_38.attribute+" but no canvas exists under that attribute name.","AutoTest")}}
var _40=isc.AutoTest.parseLocatorFallbackPath(_159);if(_40!=null){return this.getChildFromFallbackLocator(_159,_40)}
return null},getChildLocatorStrategy:function(_159){if(isc.AutoTest.locStrategyNames==null){isc.AutoTest.locStrategyNames={}}
var _34=isc.AutoTest.locStrategyNames[_159];if(_34==null){var _41=_159;if(isc.isA.String(this.$6e[_159]))_41=this.$6e[_159];_34=isc.AutoTest.locStrategyNames[_159]="locate"+_41.substring(0,1).toUpperCase()+_41.substring(1)+"By"}
return this[_34]},getChildLocatorTypeStrategy:function(_159){if(isc.AutoTest.locStrategyTypes==null){isc.AutoTest.locStrategyTypes={}}
var _34=isc.AutoTest.locStrategyTypes[_159];if(_34==null){var _41=_159;if(isc.isA.String(this.$6e[_159]))_41=this.$6e[_159];_34=isc.AutoTest.locStrategyTypes[_159]="locate"+_41.substring(0,1).toUpperCase()+_41.substring(1)+"Type"}
return this[_34]},getChildFromFallbackLocator:function(_159,_40){var _42=_40.name,_43=_40.config;var _44=this.getFallbackLocatorCandidates(_42);if(_44&&_44.length>0){var _45=this.getChildLocatorStrategy(_42);if(_45==null)_45="name";var _46=this.getChildLocatorTypeStrategy(_42);if(_46==null)_46="Class";var _24=isc.Canvas.getCanvasFromFallbackLocator(_159,_43,_44,_45,_46);if(_24!=null)return _24}
this.logDebug("AutoTest.getElement(): locator substring:"+_159+" parsed to fallback locator name:"+_42+", unable to find relevant child - may refer to inner element.","AutoTest")},$6e:{peer:"peers",child:"children"},getFallbackLocatorCandidates:function(_9){var _44;if(this.$ep!=null&&this.$ep[_9]!=null){var _47=this.$ep[_9];_44=[];for(var i=0;i<_47.length;i++){_44[i]=window[_47[i]]}}else if(isc.isA.String(this.$6e[_9])){_44=this[this.$6e[_9]]}else if(this[_9]&&isc.isAn.Array(this[_9])){_44=this[_9]}
return _44},emptyLocatorArray:function(_159){return _159==null||_159.length==0||(_159.length==1&&_159[0]=="")},getInnerElementFromSplitLocator:function(_159){if(!this.emptyLocatorArray(_159)){if(_159.length==1){var _48=_159[0].split("_");var _49={part:_48[0],partID:_48[1]};var _50=this.getPartElement(_49);if(_50)return _50}}
return this.getHandle()},getAutoTestLocatorCoords:function(_67,_50){if(_67==null||_50==null)return null;var _51=isc.Element.getElementRect(_50);var _52=_51[0],_53=_51[2];_52+=Math.floor(_53/ 2);var _54=_51[1],_55=_51[3];_54+=Math.floor(_55/ 2);return[_52,_54]}});if(isc.Layout){isc.Layout.addProperties({getStandardChildLocator:function(_39){var _35=this.getNamedLocatorChildString(_39);if(_35)return _35;if(this.members.contains(_39)){return this.getCanvasLocatorFallbackPath("member",_39,this.members)}
return this.Super("getStandardChildLocator",arguments)},$6e:{member:"members",peer:"peers",child:"children"}})}
if(isc.Window){isc.Window.addProperties({containsLocatorChild:function(_39){if(this.items&&this.items.contains(_39))return true;return this.Super("containsLocatorChild",arguments)},getStandardChildLocator:function(_39){if(this.items&&this.items.contains(_39)){var _56=this.$6d;_56[0]="item";_56[2]=this.items.indexOf(_39);_56[4]=_39.getClassName();return _56.join(isc.emptyString)}
return this.invokeSuper(isc.Window,"getStandardChildLocator",_39)},$6e:{item:"items",member:"members",peer:"peers",child:"children"}})}
if(isc.SectionStack){isc.ImgSectionHeader.changeDefaults("$6e",{item:"items"});isc.SectionHeader.changeDefaults("$6e",{item:"items"});isc.SectionStack.changeDefaults("$6e",{section:"sections"});isc.SectionStack.addProperties({getStandardChildLocator:function(_39){var _57=this.sections||[],_58;for(var i=0;i<_57.length;i++){var _59=_57[i].items,_60,_61;if(_39==_57[i]){_60=_39}else if(_59&&_59.contains(_39)){_60=_57[i];_61=_39}
if(_60!=null){_58=this.getCanvasLocatorFallbackPath("section",_60,this.sections)}
if(_61!=null){_58+="/"+this.getCanvasLocatorFallbackPath("item",_61,_60.items)}
if(_58!=null)return _58}
return this.Super("getStandardChildLocator",arguments)}})}
if(isc.StretchImg){isc.StretchImg.addProperties({getInteriorLocator:function(_50,_159){var _62=_50,_63=this.getHandle(),_64=this.getCanvasName();while(_50&&_50!=_63&&_50.getAttribute){var _30=_50.getAttribute("name");if(_30&&_30.startsWith(_64)){return _30.substring(_64.length)}
_50=_50.parentNode}
return this.Super("getInteriorLocator",[_62,_159])},getInnerElementFromSplitLocator:function(_159){if(!this.emptyLocatorArray(_159)&&_159.length==1){var _65=this.getImage(_159[0]);if(_65)return _65}
return this.Super("getInnerElementFromSplitLocator",arguments)}})}
if(isc.DynamicForm){isc.DynamicForm.addProperties({getInteriorLocator:function(_50){var _66=isc.DynamicForm.$ot(_50,this);if(!_66.item)return this.Super("getInteriorLocator",arguments);var _61=_66.item,_67=[this.getItemLocator(_61),'/'];if(_66.overElement)_67[_67.length]="element";else if(_66.overTitle)_67[_67.length]="title";else if(_66.overTextBox)_67[_67.length]="textbox";else if(_66.overControlTable)_67[_67.length]="controltable";else if(_66.overIcon)_67[_67.length]="[icon='"+_66.overIcon+"']"
return _67.join(isc.emptyString)},getItemLocator:function(_61){if(_61.parentItem&&(_61.parentItem!=this)){return this.getItemLocator(_61.parentItem)+"/"+_61.parentItem.getItemLocator(_61)}
var _68={};if(_61.name!=null)_68.name=_61.name;var _14=_61.getTitle();if(_14!=null)_68.title=_14;var _69=_61.getValue();if(_69!=null)_68.value=_69;_68.index=this.getItems().indexOf(_61);_68.Class=_61.getClassName();var _70=isc.AutoTest.createLocatorFallbackPath("item",_68);return _70},containsLocatorChild:function(_39){if(isc.isA.DateChooser(_39)&&_39.callingForm==this)return true;return this.Super("containsLocatorChild",arguments)},getChildLocator:function(_39){if(_39.canvasItem){var _61=_39.canvasItem;return this.getItemLocator(_61)+"/canvas"}
if(isc.isA.PickListMenu(_39)){var _61=_39.formItem;return this.getItemLocator(_61)+"/pickList"}
if(isc.isA.DateChooser(_39)){var _61=_39.callingFormItem;return this.getItemLocator(_61)+"/picker"}
return this.Super("getChildLocator",arguments)},getItemFromSplitLocator:function(_159){var _71=_159[0],_10;if(_71.contains("[Class=")){var _72=_71.match("item\\[(.+)'\\]\\[Class=\"(.+)\"\\]");_10=_72[1].substring(6,_72[1].length-2);_71=_72[0]}
var _73=isc.AutoTest.parseLocatorFallbackPath(_71);if(_73&&_73.name=="item"&&_73.config!=null){var _43=_73.config;_10=_43.Class;var _61;if(_43.name!=null){_61=this.getItem(_43.name)}else{for(var i=0;i<this.items.length;i++){var _74=this.items[i],_75=_74.locateItemBy;if(_75==null)_75="title";if(_75=="title"&&_43.title!=null&&_74.title==_43.title)
{_61=_74}else if(_75=="value"&&_43.value!=null&&_74.getValue()==_43.value)
{_61=_74}}
if(_61==null){var _25=_43.index;if(isc.isA.String(_25)){if(_25.startsWith("'")||_25.startsWith('"'))
{_25=_25.substring(1)}
_25=parseInt(_25)}
_61=this.items[_25]}}
if(!_61){this.logWarn("AutoTest.getElement(): Unable to find item from "+"locator string:"+_71);return null}
if(!isc.isA[_10]||!isc.isA[_10](_61)){this.logWarn("AutoTest.getElement(): identifier:"+_71+" returned an item of class:"+_61.getClassName())}
return _61}
return null},getInnerElementFromSplitLocator:function(_159){if(this.emptyLocatorArray(_159)){return this.getHandle()}
var _61=this.getItemFromSplitLocator(_159);if(_61!=null){_159.removeAt(0);return _61.getInnerElementFromSplitLocator(_159)}
return this.getHandle()}});isc.ContainerItem.addProperties({getItemLocator:isc.DynamicForm.getPrototype().getItemLocator,getItemFromSplitLocator:isc.DynamicForm.getPrototype().getItemFromSplitLocator,getInnerElementFromSplitLocator:function(_159){if(!this.emptyLocatorArray(_159)){var _76=this.getItemFromSplitLocator(_159);if(_76!=null){_159.removeAt(0);return _76.getInnerElementFromSplitLocator(_159)}}
return this.Super("getInnerElementFromSplitLocator",arguments)}});isc.FormItem.addProperties({getElementFromSplitLocator:function(_159){return this.getInnerElementFromSplitLocator(_159)},getInnerElementFromSplitLocator:function(_159){if(!this.emptyLocatorArray(_159)){var _49=_159[0];if(_49=="element")return this.getDataElement();if(_49=="title")return this.form.getTitleCell(this);if(_49=="textbox")return this.$6f();if(_49=="controltable")return this.$6g();if(_49=="canvas"){if(this.canvas){_159.removeAt(0);return this.canvas.getElementFromSplitLocator(_159)}}
if(_49=="picker"){if(this.picker){_159.removeAt(0);return this.picker.getElementFromSplitLocator(_159)}}
if(_49=="pickList"){if(!this.pickList)this.makePickList(false);_159.removeAt(0);return this.pickList.getElementFromSplitLocator(_159)}
var _77=_49.match("\\[icon='(.+)'\\]"),_78=_77?_77[1]:null;if(_78){return this.$6h(_78)}}else{var _50=this.getFocusElement();if(_50==null)_50=this.$6f();return _50}},emptyLocatorArray:isc.Canvas.getPrototype().emptyLocatorArray});isc.HeaderItem.addProperties({locateItemBy:"value"});if(isc.PickListMenu){isc.PickListMenu.addProperties({getLocatorParent:function(){if(this.formItem)return this.formItem.form;return this.Super("getLocatorParent",arguments)}})}}
if(isc.GridRenderer){isc.GridRenderer.addProperties({getInteriorLocator:function(_50,_159){var _79=this.getCellFromDomElement(_50);if(_79==null)return this.Super("getInteriorLocator",[_50,_159]);var _80=_79[0],_81=_79[1];return this.getCellLocator(_80,_81)},getCellFromDomElement:function(_50){var _63=this.getHandle(),_82=this.getTableElement();if(!_82)return null;var _83=_82.rows,_84,_85,_79,_86="tr",_87="TR",_88="td",_89="TD";while(_50&&_50!=_82&&_50!=_63){_84=_50.tagName;if(_84==_88||_84==_89){_79=_50}
if(_84==_86||_84==_87){_85=_50}
_50=_50.parentNode}
if(!_85||!_79)return null;var _83=_82.rows,_80,_90;for(var i=0;i<_83.length;i++){if(_83[i]==_85){_80=i;break}}
var _91=_85.cells,_81,_92;for(var i=0;i<_91.length;i++){if(_91[i]==_79){_81=i;break}}
_90=_80+(this.$6i||0);_92=_81+(this.$6j||0);return[_90,_92]},getCellLocator:function(_80,_81){return"row["+_80+"]/col["+_81+"]"},getInnerElementFromSplitLocator:function(_159){if(this.emptyLocatorArray(_159))return this.getHandle();if(_159.length==2){var _79=this.getCellFromLocator(_159[0],_159[1]),_80=_79[0],_81=_79[1];if(isc.isA.Number(_80)&&isc.isA.Number(_81)){if(this.$6k())return null;return this.getTableElement(_80,_81)}}
return this.Super("getInnerElementFromSplitLocator",arguments)},getCellFromLocator:function(_111,_108){var _93=_111.substring(4,_111.length-1),_94=_108.substring(4,_108.length-1);return[_80,_81]}})}
if(isc.ListGrid){isc.ListGrid.addProperties({namedLocatorChildren:["header","frozenHeader","body","frozenBody",{attribute:"$6l",name:"editRowForm"}]});isc.GridBody.addProperties({getInteriorLocator:function(_50,_159){if(_159){var _32=this.children;if(_32!=null&&_32.length>0){for(var i=0;i<_32.length;i++){var _37=_32[i];if(_37&&_37.eventProxy==this){var _63=_37.getHandle();if(_63!=null){var _95=_50;while(_95!=this.getHandle()&&_95!=null)
{if(_95==_63){var _80=this.getEventRow(),_81=this.getEventColumn();return this.getCellLocator(_80,_81)}
_95=_95.parentNode}}}}}}
return this.Super("getInteriorLocator",arguments)},getCellLocator:function(_80,_81){var _96=this.grid;if(_96==null)return this.Super("getCellLocator",arguments);return _96.getCellLocator(this,_80,_81)}});isc.ListGrid.addProperties({getCellLocator:function(_107,_80,_81){var _97=this.getRowLocatorOptions(_107,_80,_81),_98=this.getColLocatorOptions(_107,_80,_81);return isc.AutoTest.createLocatorFallbackPath("row",_97)+"/"+isc.AutoTest.createLocatorFallbackPath("col",_98)},getRowLocatorOptions:function(_107,_80,_81){var _99={},_100=this.getFieldNumFromLocal(_81,_107),_101=this.getCellRecord(_80,_100),_102=this.getDataSource();if(_101!=null){if(_102!=null){var _103=_102.getPrimaryKeyFieldName();if(_103!=null&&_101[_103]!=null){_99[_103]=_101[_103]}}
var _104=this.getTitleField();if(_104!=null&&_101[_104]!=null){_99[_104]=_101[_104]}
var _105=this.getFieldName(_100);if(_105!=null&&_101[_105]!=null){_99[_105]=_101[_105]}}
_99[isc.AutoTest.fallback_valueOnlyField]=_80;return _99},getColLocatorOptions:function(_107,_80,_81){var _99={},_100=this.getFieldNumFromLocal(_81,_107);var _106=this.getField(_100);if(this.isCheckboxField(_106)){_99.isCheckboxField=true}else{var _105=this.getFieldName(_100);if(_105!=null)_99.fieldName=_105}
_99[isc.AutoTest.fallback_valueOnlyField]=_81;return _99},getChildFromLocatorSubstring:function(_159,_25,_160){if(_159=="frozenBody"||_159=="body"){if(_160.length==_25+3&&_160[_25+1].startsWith("row[")&&_160[_25+2].startsWith("col["))
{return null}}
return this.Super("getChildFromLocatorSubstring",arguments)},getInnerElementFromSplitLocator:function(_159){if(this.emptyLocatorArray(_159))return this.getHandle();var _107=_159[0];if(_159.length==3&&(_107=="body"||_107=="frozenBody")){var _108=_159[2],_109=isc.AutoTest.parseLocatorFallbackPath(_108);if(_109.name!="col"){this.logWarn("Error parsing locator:"+_159.join("")+" returning ListGrid handle");return this.getHandle()}
var _106=this.getFieldFromColLocatorConfig(_109.config),_110;if(_106==null){_110=parseInt(_109.config[isc.AutoTest.fallback_valueOnlyField]);if(_107=="frozenBody"&&this.frozenBody==null){_107="body"}
_107=this[_107]}else{_110=this.getLocalFieldNum(this.getFieldNum(_106));if(this.fieldIsFrozen(_106))_107=this.frozenBody;else _107=this.body}
if(_107==null)return null;var _111=_159[1],_112=isc.AutoTest.parseLocatorFallbackPath(_111),_80=this.getRowNumFromLocatorConfig(_112.config);if(isc.isA.Number(_80)&&isc.isA.Number(_110)){if(_107.$6k())return null;return _107.getTableElement(_80,_110)}}
return this.Super("getInnerElementFromSplitLocator",arguments)},getFieldFromColLocatorConfig:function(_159){if(_159.isCheckboxField!=null){for(var i=0;i<this.fields.length;i++){if(this.isCheckboxField(this.fields[i])){return this.fields[i]}
this.logWarn("AutoTest stored a locator for interaction with "+"checkbox field - but this grid is not showing a checkbox field - "+"recorded test may be invalid.","AutoTest");return-1}}else{var _113=this.locateColumnsBy;if(_113=="fieldName"||_113==null){var _105=_159.fieldName;if(_105!=null){return this.getField(_105)}}}},getRowNumFromLocatorConfig:function(_159){var _114=this.locateRowsBy;if(_114==null)_114="primaryKey";switch(_114){case"primaryKey":var _102=this.getDataSource();if(_102!=null){var _115=_102.getPrimaryKeyFieldName();if(_102!=null&&_159[_115]!=null){return this.findRowNum(_159)}}
case"titleField":var _104=this.getTitleField();if(_104!=null&&_159[_104]!=null){var _116=this.data;return _116.findIndex(_104,_159[_104])}
case"targetCellValue":for(var _105 in _159){if(_105==isc.AutoTest.fallback_valueOnlyField)continue;if(_159[_105]!=null){return this.data.findIndex(_105,_159[_105])}}
default:return parseInt(_159[isc.AutoTest.fallback_valueOnlyField])}}})}
if(isc.TreeGrid){isc.TreeGridBody.addProperties({getInteriorLocator:function(_50){var _62=_50;var _63=this.getHandle(),_117=this.getTableElement();if(!_50||!_63||!_117)return isc.emptyString;var _118=this.grid.getCanvasName()+this.grid.$6m,_80,_81;var _119=this.grid.getCanvasName()+this.grid.$6n;while(_50!=this.tableElement&&_50!=_63&&_50.getAttribute){var _30=_50.getAttribute("name");if(_30){if(_30.startsWith(_118)){_80=parseInt(_30.substring(_118.length));_81=this.grid.getTreeFieldNum();return this.getCellLocator(_80,_81)+"/open"}
if(_30.startsWith(_119)){_80=parseInt(_30.substring(_119.length));_81=this.grid.getTreeFieldNum();return this.getCellLocator(_80,_81)+"/extra"}}
_50=_50.parentNode}
return this.Super("getInteriorLocator",[_62])},getInnerElementFromSplitLocator:function(_159){if(this.emptyLocatorArray(_159))return this.getHandle();if(_159.length==3){if(_159[2]=="open"){if(this.$6k())return null;var _111=_159[0];var _80;if(_111.charAt(3)!="["){_80=parseInt(_111.substring(3))}else{var _112=isc.AutoTest.parseLocatorFallbackPath(_111);if(_112==null||_112.name!="row"){this.logInfo("Locator appears to be click-in-open-area locator but "+"doesn't contain row/col info? returning null.\n"+_159.join("/"),"AutoTest")}
_80=this.grid.getRowNumFromLocatorConfig(_112.config)}
var _120=this.grid.$6m+_80,_65=this.grid.getImage(_120);if(_65)return _65}else if(_159[2]=="extra"){if(this.$6k())return null;var _111=_159[0];var _80;if(_111.charAt(3)!="["){_80=parseInt(_111.substring(3))}else{var _112=isc.AutoTest.parseLocatorFallbackPath(_111);if(_112==null||_112.name!="row"){this.logInfo("Locator appears to be click-in-open-area locator but "+"doesn't contain row/col info? returning null.\n"+_159.join("/"),"AutoTest")}
_80=this.grid.getRowNumFromLocatorConfig(_112.config)}
var _120=this.grid.$6n+_80,_65=this.grid.getImage(_120);if(_65)return _65}}
return this.Super("getInnerElementFromSplitLocator",arguments)},getAutoTestLocatorCoords:function(_67,_50){var _121=this.Super("getAutoTestLocatorCoords",arguments);if(_121==null)return _121;var _122=this.grid;if(_122==null||_67.endsWith("open")||_67.endsWith("extra"))return _121;var y=_121[1],_80=this.getEventRow(y),_81=this.getEventColumn(_121[0]),_116=_122.data,_124=_122.getRecord(_80),_125=_122.getTreeFieldNum()==_122.getFieldNumFromLocal(_81,this);if(_125&&_122.data&&_122.data.isFolder(_124)){var _126=_122.getOpenAreaWidth(_124),_51=isc.Element.getElementRect(_50),_52=(_51[0]+_126),_53=_51[2]-_52;_121[0]=_52+Math.floor(_53/ 2)}
return _121}})}
if(isc.TabSet){isc.TabSet.addProperties({containsLocatorChild:function(_39){if(this.Super("containsLocatorChild",arguments))return true;if(this.getTabNumber(_39)!=-1)return true;return false},getStandardChildLocator:function(_39){var _127=this.getTabNumber(_39);if(_127!=-1){var _128=this.getTabObject(_127);var _129={};if(_128.ID!=null)_129.ID=_128.ID;if(_128.title!=null)_129.title=_128.title;_129.index=_127;return isc.AutoTest.createLocatorFallbackPath("tab",_129)}
return this.Super("getStandardChildLocator",arguments)},getChildFromLocatorSubstring:function(_159){if(_159&&_159.startsWith("tab[")){var _130=isc.AutoTest.parseLocatorFallbackPath(_159),_43=_130.config;if(_43.ID!=null){return this.getTab(_43.ID)}
var _131=this.locateTabsBy;if(_131==null)_131="title";if(_43.title&&_131=="title"){var _127=this.tabs.findIndex("title",_43.title);return this.getTab(_127)}
return this.getTab(parseInt(_43.index))}
return this.Super("getChildFromLocatorSubstring",arguments)}})}
if(isc.StatefulCanvas){isc.StatefulCanvas.addProperties({getInnerElementFromSplitLocator:function(_159){if(!this.emptyLocatorArray(_159)&&this.label){return this.label.getInnerElementFromSplitLocator(_159)}
return this.Super("getInnerElementFromSplitLocator",arguments)}})}
if(isc.DateChooser){isc.DateChooser.addMethods({getInteriorLocator:function(_50){var _63=this.getHandle();if(!_63||!_50)return"";var _132=_50.$6o;if(_132!=null&&_132!="")return _132;return _50.$6o=this.$6p(_50,_63)},$6p:function(_50,_63){var _133=_50;while(_133&&_133!=null){if(_133==_63){_133=null;break}
if(_133.tagName&&_133.tagName.toLowerCase()=="td"){break}
_133=_133.parentElement}
if(_133==null)return"";var _134=_63.childNodes,_135=[];for(var i=0;i<_134.length;i++){if(!_134[i].tagName||_134[i].tagName.toLowerCase()!="table"){continue}
_135[_135.length]=_134[i]}
var _136=_135.length==2?_135[0]:null,_137=_135.length==2?_135[1]:_135[0];if(_136!=null&&_133.offsetParent==_136){var _138=_133.onclick,_139=_138?_138.toString():null;if(!_139)return"";if(_139.contains("showPrevYear")){return"prevYearButton"}else if(_139.contains("showNextYear")){return"nextYearButton"}else if(_139.contains("showPrevMonth")){return"prevMonthButton"}else if(_139.contains("showNextMonth")){return"nextMonthButton"}else if(_139.contains("showMonthMenu")){return"monthMenuButton"}else if(_139.contains("showYearMenu")){return"yearMenuButton"}
return""}else if(_137!=null&&_133.offsetParent==_137){var _138=_133.onclick,_139=_138?_138.toString():null;if(!_139)return"";if(_139.contains("cancelClick"))return"cancelButton";else if(_139.contains("todayClick"))return"todayButton";else{var _140=_139.match("dateClick\\(\(.*\)\\)");if(_140&&_140[1]){var _141=_140[1].split(",");for(var i=0;i<_141.length;i++){_141[i]=_141[i].trim()}
return _141.join("/")}}}
return""},getInnerElementFromSplitLocator:function(_159){if(this.emptyLocatorArray(_159))return this.getHandle();var _63=this.getHandle();if(_63==null)return;var _142=(_159.length==3);if(!_142){var _58=_159[0];if(_58=="")return _63;var _143=(_58=="todayButton"),_144=!_143?(_58=="cancelButton"):false;var _134=_63.childNodes;if(_143||_144){if(_143&&!this.showTodayButton){this.logWarn("DateChooser attempting to locate element for "+"'todayButton' but showTodayButton is false. Returning handle.","AutoTest");return _63}
if(_144&&!this.showCancelButton){this.logWarn("DateChooser attempting to locate element for "+"'cancelButton' but showCancelButton is false. Returning handle.","AutoTest");return _63}
var _137;for(var i=_134.length-1;i>=0;i--){if(_134[i].tagName&&_134[i].tagName.toLowerCase()=="table")
{_137=_134[i];break}}
var _145=_137.rows[_137.rows.length-1],_91=_145.cells;for(var i=0;i<_91.length;i++){if(this.getInteriorLocator(_91[i])==_58){return _91[i]}}}else{if(!this.showHeader){this.logWarn("DateChooser attempting to locate element for "+_159+" but this.showHeader is false so this element will not be present. "+"Returning handle.","AutoTest");return _63}
var _136
for(var i=0;i<_134.length;i++){if(_134[i].tagName&&_134[i].tagName.toLowerCase()=="table")
{_136=_134[i];break}}
var _85=_136.rows[0],_91=_85.cells;for(var i=0;i<_91.length;i++){if(this.getInteriorLocator(_91[i])==_58){return _91[i]}}}}else{var _146=_159[0],_147=_159[1],_148=_159[2];if((_146==this.year)&&(this.month==_147||this.month==_147+1||this.month==_147-1))
{var _149=Date.createLogicalDate(_146,_147,_148),_150=_149.getDay();if(this.showWeekends||!Date.getWeekendDays().contains(_150)){var _151=Date.createLogicalDate(this.year,this.month,1);var _152=_151.getDay(),_153=_152+this.firstDayOfWeek-
(_152<this.firstDayOfWeek?7:0);_151.setDate(_151.getDate()-_153);if(Date.compareDates(_149,_151)!=1){var _154=Date.createLogicalDate(this.year,this.month+1,1);_154.setTime(_154.getTime()-86400000);var _155=_154.getDay(),_156=this.firstDayOfWeek+6;if(_156>6)_156-=7;var _157=_156>_155?_156-_155:_156+7-_155;if(_157!=0){_154.setTime(_154.getTime()+(86400000*_157))}
if(Date.compareDates(_149,_154)!=-1){var _80=Math.floor(((parseInt(_148)+_153)/7))
_80+=1;var _158=this.firstDayOfWeek;if(!this.showWeekends){while(Date.getWeekendDays().contains(_158)){_158+=1;if(_158==7)_158=0}}
var _81=_149.getDay()-_158;if(_81<0)_81+=7;var _134=_63.childNodes,_137;for(var i=_134.length-1;i>=0;i--){if(_134[i].tagName&&_134[i].tagName.toLowerCase()=="table")
{_137=_134[i];break}}
if(_137)return _137.rows[_80].cells[_81]}else{this.logInfo("DateChooser Passed ID for a date after end. "+"end date:"+[_154.getFullYear(),_154.getMonth(),_154.getDay()]+" vs:"+[_146,_147,_148],"AutoTest")}}else{this.logInfo("DateChooser Passed ID for a date before start date. "+"startDate:"+[_151.getFullYear(),_151.getMonth(),_151.getDay()]+" vs:"+[_146,_147,_148],"AutoTest")}}else{this.logInfo("DateChooser Passed ID for a weekend - not showing weekends","AutoTest")}}else{this.logInfo("DateChooser passed ID for the wrong year or month - passed:"+_159+", showing:"+[this.year,this.month],"AutoTest")}
this.logWarn("DateChooser - passed inner locator for date ("+_159.join("/")+") -- not currently showing this date.","AutoTest")}
this.logWarn("DateChooser, unable to find element for inner locator:"+_159+" returning handle");return _63}})}}
isc.AutoTest.customizeCalendar=function(){isc.$6q={getRowLocatorOptions:function(_47,_28,_29){var _1=this.Super("getRowLocatorOptions",arguments);var _2=this.creator.chosenDate;_1.date=_2.toSchemaDate("date");_1.minutes=_28*30;return _1},getRowNumFromLocatorConfig:function(_47){var _3=this.creator.locateCellsBy;if((_3=="date"||_3==null)&&_47.date!=null)
{var _2=isc.Date.parseSchemaDate(_47.date);if(!this.showingDate(_2)){this.logWarn("Locator for cell in this calendar day-view grid has date "+"stored as:"+_2.toUSShortDate()+", but we're currently showing "+this.creator.chosenDate.toUSShortDate()+". The stored date doesn't map to a visible cell so not returning a cell "+"- if this is not the intended behavior in this test case you may need to "+"set calendar.locateCellsBy to 'index'.","AutoTest");return-1}
return parseInt(_47.minutes)/30}
this.locateRowsBy="index";return this.Super("getRowNumFromLocatorConfig",arguments)},showingDate:function(_2){return(isc.Date.compareLogicalDates(_2,this.creator.chosenDate)==0)}}
isc.DaySchedule.addProperties(isc.$6q);isc.WeekSchedule.addProperties(isc.$6q,{showingDate:function(_2){for(var i=0;i<this.fields.length;i++){var _5=this.fields[i];if(_5.$6r==null)continue;if(Date.compareLogicalDates(Date.createLogicalDate(_5.$6r,_5.$6s,_5.$6t),_2)==0)
{this.logWarn("does contain date"+_2.toShortDate());return true}
this.logWarn("date passed in:"+_2.toShortDate()+"compared with:"+Date.createLogicalDate(_5.$6r,_5.$6s,_5.$6t).toShortDate())}
this.logWarn("doesn't contain date:"+_2);return false},getColLocatorOptions:function(_47,_28,_29){var _6=this.Super("getColLocatorOptions",arguments),_7=this.getFieldNumFromLocal(_29,_47),_5=this.getField(_7);if(_5&&_5.$6t!=null){_6.date=[_5.$6r,(_5.$6s+1),_5.$6t].join("-")}
return _6},getFieldFromColLocatorConfig:function(_47){if((this.locateCellsBy=="date"||this.locateCellsBy==null)&&(_47.date!=null))
{var _8=_47.date.split("-");return this.getFields().find("$6t",_8[2])}
return this.Super("getFieldFromColLocatorConfig",arguments)}});isc.MonthSchedule.addProperties({getRowLocatorOptions:function(_47,_28,_29){var _1=this.Super("getRowLocatorOptions",arguments);var _9=this.getRecord(_28);if(!_9)return _1;var _5=this.getField(_29);var _10=_5.$6u;_1.dayIndex=_10;var _2=_9["date"+_10];_1.date=_2.toSchemaDate("date");var _11=_9["event"+_10];if(_11==null){_1.isHeaderRow=true}else{_1.isHeaderRow=false}
return _1},getRowNumFromLocatorConfig:function(_47){var _3=this.creator.locateCellsBy;if((_3=="date"||_3==null)&&_47.date!=null)
{var _2=isc.Date.parseSchemaDate(_47.date),_12=(_47.isHeaderRow=="true"),_13="date"+_47.dayIndex,_14="event"+_47.dayIndex;for(var i=0;i<this.data.length;i++){var _15=(this.data[i][_14]==null);if(_15==_12){if(Date.compareLogicalDates(this.data[i][_13],_2)==0){return i}}}
return-1}
this.locateRowsBy="index";return this.Super("getRowNumFromLocatorConfig",arguments)},getColLocatorOptions:function(_47,_28,_29){var _1=this.Super("getColLocatorOptions",arguments);_1.dayIndex=this.getField(_29).$6u;return _1},getColNumFromLocatorConfig:function(_47){var _3=this.locateCellsBy;if(_3==null||_3=="date"){return this.fields.findIndex("$6u",parseInt(_47.dayIndex))}
this.locateColsBy="index";return this.Super("getColNumFromLocatorConfig",arguments)}});isc.MonthScheduleBody.addProperties({getInteriorLocator:function(_47){if(_47.tagName.toLowerCase()=="a"){var _16=_47.href;if(_16!=null){var _17=_16.match("javascript:.*monthViewEventClick\\((\\d+),(\\d+),(\\d+)\\);");if(_17){var _18=parseInt(_17[1]),_19=parseInt(_17[2]),_20=parseInt(_17[3]);var _11=this.grid.getEvents(_18,_19),_21=_11[_20];if(_21==null){this.logWarn("Unable to determine event associated with apparent event "+"link element -- returning cell");return this.Super("getInteriorLocator",arguments)}
var _22=this.grid.creator,_23=_22.getEventLocatorConfig(_21);var _24=isc.AutoTest.createLocatorFallbackPath("eventLink",_23);return _24}}}
return this.Super("getInteriorLocator",arguments)},getInnerElementFromSplitLocator:function(_47){if(this.emptyLocatorArray(_47))return this.getHandle();if(_47.length==1&&_47[0].startsWith("eventLink")){var _25=isc.AutoTest.parseLocatorFallbackPath(_47[0]);var _22=this.grid.creator;var _21=_22.getEventFromLocatorConfig(_25.config);var _26=this.grid.getEventCell(_21);if(_26!=null){var _27=this.grid.data,_28=_26[0],_29=_26[1],_10=this.grid.getField(_29).$6u;var _30=this.getTableElement(_28,_29),_31=_30.getElementsByTagName("A");if(_31!=null){for(var _32=0;_32<_31.length;_32++){var _16=_31[_32].href;if(_16!=null){var _17=_16.match("javascript:.*monthViewEventClick\\((\\d+),(\\d+),(\\d+)\\);");if(_17&&_27[_28]["event"+_10][parseInt(_17[3])]==_21)
{return _31[_32]}}}}}
return this.Super("getInnerElementFromSplitLocator",arguments)}}});isc.Calendar.addProperties({getCanvasLocatorFallbackPath:function(_45,_47,_48,_49,_50){if(_45=="eventWindow"){var _1=this.getEventLocatorConfig(_47.event);return isc.AutoTest.createLocatorFallbackPath("eventWindow",_1)}
return this.Super("getCanvasLocatorFallbackPath",arguments)},getEventLocatorConfig:function(_21){var _23={};if(this.dataSource){var _33=this.getDataSource().getPrimaryKeyFieldName();_23[_33]=_21[_33]}
var _34=this.nameField;_23[_34]=_21[_34];var _35=this.startDateField;var _36=_21[_35];_23[_35]=_36.toSchemaDate();var _37=this.endDateField;var _38=_21[_37];_23[_37]=_38.toSchemaDate();_23.index=this.data.indexOf(_21);return _23},getChildFromFallbackLocator:function(_47,_48){var _39=_48.name,_23=_48.config;if(_39=="eventWindow"){var _40=this.mainView.getSelectedTab().viewName;if(_40=="day"){var _41=this.dayView.body.children}else if(_40=="week"){var _41=this.weekView.body.children}
if(_41!=null){var _21=this.getEventFromLocatorConfig(_23),_42=_41.find("event",_21);return _42}
this.logWarn("unable to find event window associated with event:"+this.echo(_21)+" based on locator string:"+_47+". It's possible that this event is not visible in the current view of "+"this Calendar","AutoTest");return null}
return this.Super("getChildFromFallbackLocator",arguments)},getEventFromLocatorConfig:function(_23){var _43=this.locateEventsBy;if(_43==null)_43="primaryKey";switch(_43){case"primaryKey":var _44=this.getDataSource();if(_44){var _33=_44.getPrimaryKeyFieldName();if(_33&&_23[_33]!=null){return this.data[this.data.findByKey(_23)]}}
case"name":var _45=_23[this.nameField];if(_45!=null)return this.data.find(this.nameField,_45);case"date":var _36=_23[this.startDateField],_38=_23[this.endDateField];for(var i=0;i<this.data.length;i++){var _46=this.data.get(i);if(_46==null)continue;if(_46[this.startDateField].toSchemaDate()==_36&&_46[this.endDateField].toSchemaDate()==_38)
{return _46}
this.logWarn("attempt to match calendar event by startDate / endDate "+"unable to locate any events. Backing off to index within data array")}
default:var _20=parseInt(_23.index);return this.data.get(_20)}}})}
if(isc.Calendar)isc.AutoTest.customizeCalendar();if(!isc.Page.isLoaded()){isc.Page.setEvent("load","isc.ApplyAutoTestMethods()")}else{isc.ApplyAutoTestMethods()}
isc.Page.logInfo("SmartClient Core ("+isc.version+" "+isc.buildDate+") initialized: "+(isc.timeStamp()-isc.$a)+"ms");isc.Page.logInfo("document.compatMode: "+document.compatMode);if(isc.Log.hasFireBug()){isc.Log.logWarn("NOTE: Firebug is enabled. Firebug greatly slows the performance of "+"applications that make heavy use of JavaScript. Isomorphic highly recommends Firebug "+"for troubleshooting, but Firebug and other development tools should be disabled when "+"assessing the real-world performance of SmartClient applications.")}
isc._moduleEnd=isc._Core_end=(isc.timestamp?isc.timestamp():new Date().getTime());if(isc.Log&&isc.Log.logIsInfoEnabled('loadTime'))isc.Log.logInfo('Core module init time: ' + (isc._moduleEnd-isc._moduleStart) + 'ms','loadTime');delete isc.definingFramework;}else{if(window.isc && isc.Log && isc.Log.logWarn)isc.Log.logWarn("Duplicate load of module 'Core'.");}
/*
 * Isomorphic SmartClient
 * Version 8.2 (2011-12-05)
 * Copyright(c) 1998 and beyond Isomorphic Software, Inc. All rights reserved.
 * "SmartClient" is a trademark of Isomorphic Software, Inc.
 *
 * licensing@smartclient.com
 *
 * http://smartclient.com/license
 */

