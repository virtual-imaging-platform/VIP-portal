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

if(window.isc&&window.isc.module_Core&&!window.isc.module_Grids){isc.module_Grids=1;isc._moduleStart=isc._Grids_start=(isc.timestamp?isc.timestamp():new Date().getTime());if(isc._moduleEnd&&(!isc.Log||(isc.Log && isc.Log.logIsDebugEnabled('loadTime')))){isc._pTM={ message:'Grids load/parse time: ' + (isc._moduleStart-isc._moduleEnd) + 'ms', category:'loadTime'};
if(isc.Log && isc.Log.logDebug)isc.Log.logDebug(isc._pTM.message,'loadTime')
else if(isc._preLog)isc._preLog[isc._preLog.length]=isc._pTM
else isc._preLog=[isc._pTM]}isc.definingFramework=true;isc.ClassFactory.defineInterface("List");isc.List.addInterfaceMethods({init:function(){if(!this.data)this.data=[]},first:function(){return this.get(0)},last:function(){return this.get(this.getLength()-1)},indexOf:function(_1,_2,_3){if(_2==null)_2=0;if(_3==null)_3=this.getLength()-1;for(var i=_2;i<=_3;i++){if(this.get(i)==_1)return i}
return-1},lastIndexOf:function(_1,_2,_3){if(_2==null)_2=this.getLength()-1;if(_3==null)_3=0;for(var i=_2;i>=_3;i--)
if(this.get(i)==_1)return i;return-1},findNextIndex:function(_1,_2,_3,_4){var _5=this.getLength();if(_1==null)_1=0;else if(_1>=_5)return-1;if(_4==null)_4=_5-1;if(_2==null)return-1;if(isc.isA.String(_2)){for(var i=_1;i<=_4;i++){var _7=this.get(i);if(_7&&_7[_2]==_3)return i}
return-1}else{return this.findNextMatch(_2,_1,_4)}},findAll:function(_1,_2){if(_1==null)return null;if(isc.isA.String(_1)){var _3=null,l=this.getLength();for(var i=0;i<l;i++){var _6=this.get(i);if(_6&&_6[_1]==_2){if(_3==null)_3=[];_3.add(_6)}}
return _3}else{return this.findAllMatches(_1)}},getRange:function(_1,_2){if(_2==null)_2=this.getLength()-1;var _3=[];for(var i=_1;i<_2;i++){_3[_3.length]=this.get(i)}
return _3},duplicate:function(){return this.newInstance().addList(this)},add:function(_1,_2){var _3;if(_2!==_3){return this.addAt(_1,_2)}
this.addAt(_1,this.getLength());return _1},setLength:function(_1){this.$ez();if(_1>this.getLength()){var _2;while(_1>this.getLength())this.add(_2)}else{while(_1<this.getLength())this.removeAt(this.getLength()-1)}
this.$e0()},addListAt:function(_1,_2){this.$ez();var _3=_1.getLength();for(var i=0;i<_3;i++){this.addAt(_1.get(i),_2+i)}
this.$e0();return _1},remove:function(_1){var _2=this.indexOf(_1);if(_2==-1)return false;this.$ez();var _3=this.getLength();for(var i=_2;i<_3;i++)this.set(i,this.get(i+1));this.setLength(_3-1);this.$e0();return true},removeList:function(_1){if(_1==null)return null;this.$ez();var _2=false;for(var i=0;i<this.getLength();i++){var _4=this.get(i);if(_1.contains(_4)){_2=true;this.removeAt(i);i--}}
this.$e0();return _1},sort:function(_1){var _2=this.getRange(0,this.getLength());_2.sort(_1);for(var i=0;i<_2.length;i++)this.set(i,_2[i]);return this},getProperty:function(_1){var _2=[];for(var i=0;i<this.getLength();i++){var _4=this.get(i);_2[i]=_4!=null?_4[_1]:null}
return _2},sortByProperty:function(_1,_2,_3,_4){var _5=this.getRange(0,this.getLength());_5.sortByProperty(_1,_2,_3,_4);for(var i=0;i<_5.length;i++)this.set(i,_5[i]);return this},dataChanged:function(){if(this.onDataChanged)this.onDataChanged()}});isc.$44a=function(){var _1=["isEmpty","contains","containsAll","intersect","equals","getItems","addList","getValueMap","removeEvery","$ez","$e0","$e2","getItem","setItem","removeItem","clearAll","find","findIndex","findNextMatch","findAllMatches","findByKeys","size","subList","addAll","removeAll","clear"];var _2={};for(var i=0;i<_1.length;i++){var _4=_1[i];_2[_4]=Array.prototype[_4]}
isc.List.addInterfaceMethods(_2)}
isc.$44a();isc.A=isc.isA;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$44b="List";isc.B.push(isc.A.List=function isc_isA_List(_1){if(_1==null)return false;if(isc.isA.Array(_1))return true;return _1.isA&&_1.isA(this.$44b)}
);isc.B._maxIndex=isc.C+1;isc.ClassFactory.defineClass("Tree",null,"List");isc.A=isc.Tree.getPrototype();isc.A.getProperty=isc.List.getInstanceProperty("getProperty");isc.A=isc.Tree;isc.A.FOLDERS_AND_LEAVES=null;isc.A.FOLDERS_ONLY="folders";isc.A.LEAVES_ONLY="leaves";isc.A.UNLOADED=null;isc.A.LOADING="loading";isc.A.FOLDERS_LOADED="foldersLoaded";isc.A.LOADED="loaded";isc.A.PARENT="parent";isc.A.CHILDREN="children";isc.A.STRICT="strict";isc.A.KEEP_PARENTS="keepParents";isc.A.autoID=0;isc.A=isc.Tree.getPrototype();isc.A.modelType="children";isc.A.isFolderProperty="isFolder";isc.A.reportCollisions=true;isc.A.autoSetupParentLinks=true;isc.A.pathDelim="/";isc.A.treeProperty="$5z";isc.A.nameProperty="name";isc.A.titleProperty="title";isc.A.childrenProperty="children";isc.A.cacheOpenList=true;isc.A.discardParentlessNodes=false;isc.A.indexByLevel=false;isc.A.showOpenIcon=false;isc.A.showDropIcon=false;isc.A.sortDirection=Array.ASCENDING;isc.A.showRoot=false;isc.A.autoOpenRoot=true;isc.A.separateFolders=false;isc.A.sortFoldersBeforeLeaves=true;isc.A.defaultNodeTitle="Untitled";isc.A.defaultLoadState=isc.Tree.UNLOADED;isc.A=isc.Tree.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$44c=["autoOpenRoot","childrenProperty","defaultIsFolder","defaultNodeTitle","discardParentlessNodes","idField","isFolderProperty","modelType","nameProperty","parentIdField","pathDelim","reportCollisions","rootValue","showRoot","titleProperty"];isc.A.$44d="openProperty";isc.A.$44e="treeLinking";isc.A.$44f=0;isc.A.loadBatchSize=50;isc.B.push(isc.A.init=function isc_Tree_init(){this.setupProperties();this.setRoot(this.root||this.makeRoot());if(this.loadOnInit&&this.loadBatchSize>=0)this.loadSubtree(null,null,true)}
,isc.A.setupProperties=function isc_Tree_setupProperties(){if(this.ID==null||window[this.ID]!=this)isc.ClassFactory.addGlobalID(this);if(!this.parentProperty)this.parentProperty="_parent_"+this.ID;if(!this.isFolderProperty)this.isFolderProperty="_isFolder_"+this.ID;if(this.idField==null)this.idField="id";if(this.parentIdField==null)this.parentIdField="parentId";if(!this.openProperty)this.openProperty="_isOpen_"+this.ID;if(this.indexByLevel)this.$44g=[]}
,isc.A.duplicate=function isc_Tree_duplicate(_1,_2){var _3=isc.Tree.create();var _4;for(var i=0;i<this.$44c.length;i++){var _6=this.$44c[i],_7=this[_6];if(_7!==_4)_3[_6]=_7}
var _7=this[this.$44d];if(_7!==_4&&!_7.startsWith("_isOpen_"))_3[this.$44d]=_7;_3.setRoot(this.getCleanNodeData(this.getRoot(),false,_2));if(_1){var _8=this.getOpenList(null,isc.Tree.FOLDERS_AND_LEAVES,null,null,null,null,true);_8=this.getCleanNodeData(_8,false,_2);_3.linkNodes(_8)}
return _3}
,isc.A.destroy=function isc_Tree_destroy(){this.destroyed=true;if(window[this.ID]==this)window[this.ID]=null}
,isc.A.makeRoot=function isc_Tree_makeRoot(){var _1={};var _2;if(this.idField!==_2)_1[this.idField]=this.rootValue;_1[this.treeProperty]=this.ID;return _1}
,isc.A.convertToFolder=function isc_Tree_convertToFolder(_1){_1[this.isFolderProperty]=true}
,isc.A.makeNode=function isc_Tree_makeNode(_1,_2){var _3=this.find(_1);if(_3){if(_2)this.convertToFolder(_3);return _3}
var _4=_1.split(this.pathDelim);var _5=this.getRoot()[this.nameProperty];if(_5.endsWith(this.pathDelim)){_5=_5.substring(0,_5.length-this.pathDelim.length)}
if(_4[0]!=_5)_4.addAt(_5,0);var _6=_4[_4.length-1],_7=(_6!=isc.emptyString);if(!_7){_4.length=_4.length-1;_6=_4[_4.length-1]}
var _8=_4.slice(0,(_4.length-1)).join(this.pathDelim)+this.pathDelim;var _9=this.find(_8);if(_9==null){_9=this.find(_8.substring(0,_8.length-this.pathDelim.length))}
if(!_9){_9=this.makeNode(_8,_2)}else if(!this.isFolder(_9)){this.convertToFolder(_9)}
var _3={};_3[this.nameProperty]=_6;if(!_7)this.convertToFolder(_3);return this.add(_3,_9)}
,isc.A.isRoot=function isc_Tree_isRoot(_1){return this.root==_1}
,isc.A.setupParentLinks=function isc_Tree_setupParentLinks(_1){if(!_1)_1=this.root;if(_1[this.idField]!=null)this.nodeIndex[_1[this.idField]]=_1;var _2=_1[this.childrenProperty];if(_2){this.setLoadState(_1,isc.Tree.LOADED);if(!isc.isAn.Array(_2)){_2=_1[this.childrenProperty]=[_2]}}
if(!_2||_2.length==0)return;for(var i=0,_4=_2.length,_5;i<_4;i++){_5=_2[i];if(!_5)continue;if(_5[this.parentIdField]==null&&_1[this.idField]!=null)
_5[this.parentIdField]=_1[this.idField];_5[this.parentProperty]=_1;this.$44h(_5,_1);if(this.isFolder(_5)){this.setupParentLinks(_5)}else if(_5[this.idField]!=null){this.nodeIndex[_5[this.idField]]=_5}}}
,isc.A.connectByParentID=function isc_Tree_connectByParentID(_1,_2,_3,_4,_5){this.linkNodes(_1,_2,_3,_4,_5)}
,isc.A.connectByParentId=function isc_Tree_connectByParentId(_1,_2,_3,_4,_5){this.linkNodes(_1,_2,_3,_4,_5)}
,isc.A.linkNodes=function isc_Tree_linkNodes(_1,_2,_3,_4,_5,_6,_7){if(this.modelType=="fields"){this.connectByFields(_1);return}
_1=_1||this.data;_2=(_2!=null)?_2:this.idField;_3=(_3!=null)?_3:this.parentIdField;_4=(_4!=null)?_4:this.rootValue;var _8=[];_8.addList(_1);var _9={};for(var i=0;i<_8.length;i++){var _11=_8[i][_2];if(_11!=null)_9[_11]=_8[i]}
for(var i=0;i<_8.length;i++){var _12=_8[i];if(this.nodeIndex[_12[_2]]==_12)continue;if(_12==null)continue;var _13=_12[_3],_14=_13!=null?_9[_13]:null,_15=[];while(_14!=null){if(_14)_15.add(_14);_13=_14[_3];_14=_13!=null&&_13!=_12[_3]?_9[_13]:null}
if(_15.length>0){for(var _16=(_15.length-1);_16>=0;_16--){if(this.logIsDebugEnabled(this.$44e)){this.logDebug("linkNodes running - adding interlinked parents to the tree in "+" reverse hierarchical order -- currently adding node with id:"+_15[_16][_2],this.$44e)}
this.$44i(_15[_16],_2,_3,_6,_4);delete _9[_15[_16][_2]]}}
this.$44i(_12,_2,_3,_6,_4);delete _9[_12[_2]]}
this.$44j(true);if(!_7)this.dataChanged()}
,isc.A.connectByParentID=function isc_Tree_connectByParentID(_1,_2,_3,_4,_5){this.linkNodes(_1,_2,_3,_4,_5)}
,isc.A.connectByParentId=function isc_Tree_connectByParentId(_1,_2,_3,_4,_5){this.linkNodes(_1,_2,_3,_4,_5)}
,isc.A.$44i=function isc_Tree__linkNode(_1,_2,_3,_4,_5){var _6=this.logIsDebugEnabled(this.$44e);var _7=_1[_2],_8=_1[_3],_9,_10=(_5==null),_11=(_8==null||_8==-1||_8===isc.emptyString),_12=this.nodeIndex[_8];if(_12){if(_6){this.logDebug("found parent "+_12[_2]+" for child "+_1[_2],this.$44e)}
this.$44k(_1,_12)}else if(!_10&&_8==_5){if(_6){this.logDebug("root node: "+_1[_2],this.$44e)}
this.$44k(_1,this.root)}else{if(!_11&&this.discardParentlessNodes){this.logWarn("Couldn't find parent: "+_8+" for node with id:"+_7,this.$44e)}else{var _13=_4||this.root;if(_6){this.logDebug("child:"+_1[_2]+(_11?" has no explicit parent ":(" unable to find specified parent:"+_8))+"- linking to default node "+_13[_2],this.$44e)}
this.$44k(_1,_13)}}}
,isc.A.connectByFields=function isc_Tree_connectByFields(_1){if(!_1)_1=this.data;for(var i=0;i<_1.length;i++){this.addNodeByFields(_1[i])}}
,isc.A.addNodeByFields=function isc_Tree_addNodeByFields(_1){var _2=this.root;for(var i=0;i<this.fieldOrder.length;i++){var _4=this.fieldOrder[i],_5=_1[_4];var _6=isc.isA.String(_5)?_5:_5+isc.emptyString,_7=this.findChildNum(_2,_6),_8;if(_7!=-1){_8=this.getChildren(_2).get(_7)}else{_8={};_8[this.nameProperty]=_6;this.add(_8,_2);this.convertToFolder(_8)}
_2=_8}
this.add(_1,_2)}
,isc.A.getRoot=function isc_Tree_getRoot(){return this.root}
,isc.A.setRoot=function isc_Tree_setRoot(_1,_2){this.root=_1;if(_1&&isc.endsWith(this.parentProperty,this.ID))_1[this.parentProperty]=null;this.root[this.treeProperty]=this.ID;if(this.rootValue==null)this.rootValue=this.root[this.idField];var _3=this.root[this.nameProperty];if(_3==null||_3==isc.emptyString)this.root[this.nameProperty]=this.pathDelim;if(!this.isFolder(this.root))this.convertToFolder(this.root);this.nodeIndex={};if("parent"==this.modelType){if(this.data)this.linkNodes()}else if("fields"==this.modelType){if(this.data)this.connectByFields()}else if("children"==this.modelType){if(this.autoSetupParentLinks)this.setupParentLinks();if(this.data){var _4=this.data;this.data=null;this.addList(_4,this.root)}}else{this.logWarn("Unsupported modelType: "+this.modelType)}
if(_2!==false&&(this.autoOpenRoot||_2)){this.openFolder(_1)}
this.setupParentLinks();this.$44j();this.dataChanged()}
,isc.A.getCleanNodeData=function isc_Tree_getCleanNodeData(_1,_2,_3,_4){if(_1==null)return null;var _5=[],_6=false;if(!isc.isAn.Array(_1)){_1=[_1];_6=true}
for(var i=0;i<_1.length;i++){var _8=_1[i],_9={};for(var _10 in _8){if(_10==this.parentProperty||(!_4&&_10=="$44l")||_10=="$5z"||_10=="__ref"||_10.startsWith("_isOpen_")||_10.startsWith("_isFolder_")||_10.startsWith("$44m")||_10=="$44n"||_10.startsWith("_selection_")||(_2==false&&_10==this.childrenProperty))continue;_9[_10]=_8[_10];if(_10==this.childrenProperty&&isc.isAn.Array(_9[_10])){_9[_10]=this.getCleanNodeData(_9[_10],false,_3,_4)}}
_5.add(_9)}
if(_6)return _5[0];return _5}
,isc.A.getName=function isc_Tree_getName(_1){var _2=isc.$ah;if(!_1)return _2;var _3=_1[this.nameProperty];if(_3==null)_3=_1[this.idField];if(_3==null){if(!this.isDescendantOf(_1,this.root)&&_1!=this.root)return null;if(!this.$44o)this.$44o=isc.Tree.autoID+++"_";_3=this.$44o+this.$44f++}
if(!isc.isA.String(_3))_3=_2+_3;_1[this.nameProperty]=_3;return _3}
,isc.A.getTitle=function isc_Tree_getTitle(_1){if(!_1)return null;if(_1[this.titleProperty]!=null)return _1[this.titleProperty];var _2=_1[this.nameProperty];if(_2==null)_2=this.defaultNodeTitle;return(isc.endsWith(_2,this.pathDelim)?_2.substring(0,_2.length-this.pathDelim.length):_2)}
,isc.A.getPath=function isc_Tree_getPath(_1){var _2=this.getParent(_1);if(_2==null)return this.getName(_1);var _3=this.getName(_2);return this.getPath(_2)+(_3==this.pathDelim?isc.emptyString:this.pathDelim)+this.getName(_1)}
,isc.A.getParentPath=function isc_Tree_getParentPath(_1){var _2=this.getName(_1),_3=this.getPath(_1);return _3.substring(0,_3.length-_2.length-this.pathDelim.length)}
,isc.A.getParent=function isc_Tree_getParent(_1){if(_1==null)return null;return _1[this.parentProperty]}
,isc.A.getParents=function isc_Tree_getParents(_1){var _2=[],_3=this.getParent(_1);while(_3){_2.add(_3);if(_3==this.root)break;_3=this.getParent(_3)}
return _2}
,isc.A.getLevel=function isc_Tree_getLevel(_1){return this.getParents(_1).length}
,isc.A.$44p=function isc_Tree__getFollowingSiblingLevels(_1){var _2=[],_3=this.getParents(_1),_4=_3.length;for(var i=0;i<_4;i++){var _6=this.getChildren(_3[i]);if(_6.indexOf(_1)!=_6.length-1)_2.add(_4-i);_1=_3[i]}
return _2}
,isc.A.isFolder=function isc_Tree_isFolder(_1){if(_1==null)return false;var _2=_1[this.isFolderProperty];if(_2!=null)return _2;if(_1[this.childrenProperty])return true;var _3=this.getName(_1);if(_3==null)return false;return isc.endsWith(_3,this.pathDelim)}
,isc.A.isLeaf=function isc_Tree_isLeaf(_1){return!this.isFolder(_1)}
,isc.A.isFirst=function isc_Tree_isFirst(_1){var _2=this.getParent(_1);if(!_2)return true;var _3=this.getChildren(_2,this.opendisplayNodeType,this.$44q,this.sortDirection,null,this.$44r);return _3[0]==_1}
,isc.A.isLast=function isc_Tree_isLast(_1){var _2=this.getParent(_1);if(!_2)return true;var _3=this.getChildren(_2,this.opendisplayNodeType,this.$44q,this.sortDirection,null,this.$44r);return _3[_3.length-1]==_1}
,isc.A.findById=function isc_Tree_findById(_1){return this.find(this.idField,_1)}
,isc.A.find=function isc_Tree_find(_1,_2){var _3;if(_2===_3&&isc.isA.String(_1))return this.$44s(_1);if(_2!==_3){if(_1==this.idField)return this.nodeIndex[_2];if(this.root[_1]==_2)return this.root;return this.getDescendants().find(_1,_2)}else{var _4=this.getDescendants();_4.add(this.root);return _4.find(_1)}}
,isc.A.findAll=function isc_Tree_findAll(_1,_2){return this.getDescendants().findAll(_1,_2)}
,isc.A.$44s=function isc_Tree__findByPath(_1){if(_1==this.pathDelim)return this.root;var _2=this.getPath(this.root);if(_1==_2)return this.root;var _3=this.root,_4=0,_5=this.pathDelim.length;if(isc.startsWith(_1,_2)){_4=_2.length}else if(isc.startsWith(_1,this.pathDelim)){_4+=_5}
while(true){var _6=_1.indexOf(this.pathDelim,_4);if(_6==_4){_4++;continue}
var _7=(_6!=-1),_8=_1.substring(_4,_7?_6:_1.length),_9=this.findChildNum(_3,_8);if(_9==-1)return null;_3=_3[this.childrenProperty][_9];if(!_7)return _3;_4=_6+_5;if(_4==_1.length)return _3}}
,isc.A.findChildNum=function isc_Tree_findChildNum(_1,_2){var _3=this.getChildren(_1);if(_3==null)return-1;if(_2==null)return-1;var _4=_3.getLength(),_5=isc.endsWith(_2,this.pathDelim),_6=this.pathDelim.length;for(var i=0;i<_4;i++){var _8=this.getName(_3.get(i)),_9=_8.length-_2.length;if(_9==0&&_8==_2)return i;if(_9==_6){if(isc.startsWith(_8,_2)&&isc.endsWith(_8,this.pathDelim)&&!_5)
{return i}}else if(_5&&_9==-_6){if(isc.startsWith(_2,_8))return i}}
return-1}
,isc.A.getChildren=function isc_Tree_getChildren(_1,_2,_3,_4,_5,_6){if(_3==null&&this.$44q==null&&this.separateFolders){this.sortByProperty()}
if(_1==null)_1=this.root;if(this.isLeaf(_1))return null;if(_1[this.childrenProperty]==null){var _7=[];_1[this.childrenProperty]=_7;return _7}
var _8=_1[this.childrenProperty],_9;if(_5){_9=[];for(var i=0,_11=_8.length;i<_11;i++){var _12=_8[i];if(this.fireCallback(_5,"node,parent,tree",[_12,_1,this]))
_9[_9.length]=_12}
_8=_9}
if(_2==isc.Tree.FOLDERS_ONLY){_9=[];for(var i=0,_11=_8.length;i<_11;i++){if(this.isFolder(_8[i]))_9[_9.length]=_8[i]}}else if(_2==isc.Tree.LEAVES_ONLY){_9=[];for(var i=0,_11=_8.length;i<_11;i++){if(this.isLeaf(_8[i]))_9[_9.length]=_8[i]}}else{_9=_8}
if(_3){if(!this.$44t||this.alwaysSortGroupHeaders||(this.$44t!=this.sortProp&&_1!=this.getRoot())||(this.$44t==this.sortProp&&_1==this.getRoot()))
{_9.sortByProperty(this.sortProp,_4,_3,_6)}}
return _9}
,isc.A.getFolders=function isc_Tree_getFolders(_1,_2,_3,_4,_5){return this.getChildren(_1,isc.Tree.FOLDERS_ONLY,_2,_3,_4,_5)}
,isc.A.getLeaves=function isc_Tree_getLeaves(_1,_2,_3,_4,_5){return this.getChildren(_1,isc.Tree.LEAVES_ONLY,_2,_3,_4,_5)}
,isc.A.getLevelNodes=function isc_Tree_getLevelNodes(_1,_2){if(this.indexByLevel&&(_2==null||_2==this.getRoot())){return this.$44g[_1]||[]}else{if(!_2)_2=this.getRoot();var _3=this.getChildren(_2);if(_1==0)return _3;var _4=[];if(!_3)return _4;for(var i=0;i<_3.length;i++){var _6=this.getLevelNodes(_1-1,_3[i]);if(_6)_4.addList(_6)}
return _4}}
,isc.A.getDepth=function isc_Tree_getDepth(){if(this.$44g)return this.$44g.length;return null}
,isc.A.hasChildren=function isc_Tree_hasChildren(_1,_2){var _3=this.getChildren(_1,_2);return _3!=null&&_3.length>0}
,isc.A.hasFolders=function isc_Tree_hasFolders(_1){return this.hasChildren(_1,isc.Tree.FOLDERS_ONLY)}
,isc.A.hasLeaves=function isc_Tree_hasLeaves(_1){return this.hasChildren(_1,isc.Tree.LEAVES_ONLY)}
,isc.A.isDescendantOf=function isc_Tree_isDescendantOf(_1,_2){if(_1==_2)return false;var _3=_1;while(_3!=null){if(_3==_2)return true;_3=_3[this.parentProperty]}
return false}
,isc.A.getDescendants=function isc_Tree_getDescendants(_1,_2,_3){if(!_1)_1=this.root;var _4=[];if(!_3)_3=function(){return true};if(this.isLeaf(_1))return _4;var _5=this.getChildren(_1);if(!_5)return _4;for(var i=0,_7=_5.length,_8;i<_7;i++){_8=_5[i];if(this.isFolder(_8)){if(_2!=isc.Tree.LEAVES_ONLY&&_3(_8))
_4[_4.length]=_8;_4=_4.concat(this.getDescendants(_8,_2,_3))}else{if(_2!=isc.Tree.FOLDERS_ONLY&&_3(_8)){_4[_4.length]=_8}}}
return _4}
,isc.A.getDescendantFolders=function isc_Tree_getDescendantFolders(_1,_2){return this.getDescendants(_1,isc.Tree.FOLDERS_ONLY,_2)}
,isc.A.getDescendantLeaves=function isc_Tree_getDescendantLeaves(_1,_2){return this.getDescendants(_1,isc.Tree.LEAVES_ONLY,_2)}
,isc.A.dataChanged=function isc_Tree_dataChanged(){}
,isc.A.add=function isc_Tree_add(_1,_2,_3){if(_2==null&&this.modelType==isc.Tree.PARENT){var _4=_1[this.parentIdField];if(_4!=null)_2=this.findById(_4)}
if(isc.isA.String(_2)){_2=this.find(_2)}else if(!this.getParent(_2)&&_2!==this.getRoot()){isc.logWarn('Tree.add(): specified parent node:'+this.echo(_2)+' is not in the tree, returning');return null}
if(!_2){var _5=this.getParentPath(_1);if(_5)_2=this.find(_5);if(!_2)return null}
this.$44k(_1,_2,_3);this.$44j(true);this.dataChanged();return _1}
,isc.A.$44u=function isc_Tree__reportCollision(_1){if(this.reportCollisions){this.logWarn("Adding node to tree with id property set to:"+_1+". A node with this ID is already present in this Tree - that node will be "+"replaced. Note that this warning may be disabled by setting the "+"reportCollisions attribute to false.")}}
,isc.A.$44k=function isc_Tree__add(_1,_2,_3){var _4=_1[this.idField];if(_4!=null&&this.modelType==isc.Tree.PARENT){var _5=this.findById(_4);if(_5){this.$44u(_4);this.remove(_5)}}
this.getName(_1);this.convertToFolder(_2);var _6=_2[this.childrenProperty];if(!_6)_6=_2[this.childrenProperty]=[];if(_6!=null&&!isc.isAn.Array(_6))
_2[this.childrenProperty]=_6=[_6];if(_3==null||_3>_6.length){_6.add(_1)}else{_6.addAt(_1,_3)}
var _7=this.idField
_1[this.parentIdField]=_2[_7];_1[this.parentProperty]=_2;_1[this.treeProperty]=this.ID;if(_1[_7]!=null)this.nodeIndex[_1[_7]]=_1;this.setLoadState(_2,isc.Tree.LOADED);this.$44h(_1,_2,_3)
var _8=_1[this.childrenProperty];if(_8!=null){_1[this.childrenProperty]=[];if(!isc.isAn.Array(_8))this.$44k(_8,_1);else if(_8.length>0)this.$44v(_8,_1);this.setLoadState(_1,isc.Tree.LOADED)}else{var _9=_1[this.isFolderProperty];if(_9!=null&&!isc.isA.Boolean(_9))
_9=isc.booleanValue(_9,true);if(_9==null&&this.defaultIsFolder)_9=true;_1[this.isFolderProperty]=_9}}
,isc.A.$44h=function isc_Tree__addToLevelCache(_1,_2,_3){if(!this.indexByLevel)return;var _4=this.getLevel(_2);if(!this.$44g[_4])this.$44g[_4]=[];var _5=this.$44g[_4];if(_5.length==0){if(!isc.isAn.Array(_1)){_5.push(_1)}else{_5.concat(_1)}}else{if(!isc.isAn.Array(_1)){if(_5.contains(_1))return}else{var _6=[];for(var j=0;j<_1.length;j++){if(!_5.contains(_1[j])){_6.push(_1[j])}}}
var _8=false,_9=0,i=0;for(i;i<_5.length;i++){if(this.getParent(_5[i])==_2){_8=true}else if(_8){break}else{continue}
if(_9===_3){break}
_9++}
if(!isc.isAn.Array(_1)){_5.splice(i,0,_1)}else{if(i==0){this.$44g[_4]=_6.concat(_5)}else if(i==_5.length){this.$44g[_4]=_5.concat(_6)}else{this.$44g[_4]=_5.slice(0,i).concat(_6,_5.slice(i))}}}}
,isc.A.addList=function isc_Tree_addList(_1,_2,_3){if(isc.isA.String(_2))_2=this.find(_2);if(!_2)return false;this.$44v(_1,_2,_3);this.$44j(true);this.dataChanged();return _1}
,isc.A.$44v=function isc_Tree__addList(_1,_2,_3){for(var i=0,_5=_1.length;i<_5;i++){this.$44k(_1[i],_2,_3!=null?_3++:null)}}
,isc.A.move=function isc_Tree_move(_1,_2,_3){this.moveList([_1],_2,_3)}
,isc.A.moveList=function isc_Tree_moveList(_1,_2,_3){var _4=_1[0],_5=this.getParent(_4),_6=this.getChildren(_5).indexOf(_4);this.removeList(_1);if(_2==_5&&_1.length==1){if(_3>_6)_3--}else{var _7=this.getChildren(_2);if(_7&&_3>_7.length)_3=_7.length}
this.addList(_1,_2,_3);this.dataChanged()}
,isc.A.remove=function isc_Tree_remove(_1,_2){var _3=this.getParent(_1);if(!_3)return false;var _4=this.getChildren(_3);if(!_4)return false;if(_4.remove(_1)){delete this.nodeIndex[_1[this.idField]];if(!_2){this.$44j(true);this.dataChanged()}
this.removeChildrenFromNodeIndex(_1);this.$44w(_1);return true}
return false}
,isc.A.removeChildrenFromNodeIndex=function isc_Tree_removeChildrenFromNodeIndex(_1){var _2=this.getChildren(_1);if(!_2)return;for(var i=0;i<_2.length;i++){if(this.getChildren(_2[i])){this.removeChildrenFromNodeIndex(_2[i])}
delete this.nodeIndex[_2[i][this.idField]]}}
,isc.A.removeList=function isc_Tree_removeList(_1){var _2=false;for(var _3=_1.length-1,i=_3;i>=0;i--){if(this.remove(_1[i],true))_2=true}
if(_2){this.$44j(true);this.dataChanged()}
return _2}
,isc.A.$44w=function isc_Tree__removeFromLevelCache(_1,_2){if(!this.indexByLevel)return;_2=_2||this.getLevel(_1)-1;var _3=this.getChildren(_1);if(_3){for(var i=0;i<_3.length;i++){this.$44w(_3[i],_2+1)}}
if(this.$44g[_2]){var _5=this.$44g[_2];for(var i=0;i<_5.length;i++){if(_5[i]==_1){_5.splice(i,1);break}}}}
,isc.A.getLoadState=function isc_Tree_getLoadState(_1){if(!_1)return null;if(!_1.$44l)return this.defaultLoadState;return _1.$44l}
,isc.A.isLoaded=function isc_Tree_isLoaded(_1){var _2=this.getLoadState(_1);return(_2==isc.Tree.LOADED||_2==isc.Tree.LOADING)}
,isc.A.setLoadState=function isc_Tree_setLoadState(_1,_2){_1.$44l=_2}
,isc.A.loadRootChildren=function isc_Tree_loadRootChildren(_1){this.loadChildren(this.root,_1)}
,isc.A.loadChildren=function isc_Tree_loadChildren(_1,_2){if(!_1)_1=this.root;this.setLoadState(_1,isc.Tree.LOADED);if(_2){this.fireCallback(_2,"node",[_1],this)}}
,isc.A.unloadChildren=function isc_Tree_unloadChildren(_1,_2){if(this.isLeaf(_1))return;var _3;if(_2==isc.Tree.LEAVES_ONLY){_3=this.getLeaves(_1);_1[this.childrenProperty]=this.getFolders(_1);this.setLoadState(_1,isc.Tree.FOLDERS_LOADED)}else{_3=_1[this.childrenProperty];_1[this.childrenProperty]=[];this.setLoadState(_1,isc.Tree.UNLOADED)}
if(_3){for(var i=0;i<_3.length;i++){var _1=_3[i];delete this.nodeIndex[_1[this.idField]]}}
this.$44j(true);this.dataChanged()}
,isc.A.reloadChildren=function isc_Tree_reloadChildren(_1,_2){this.unloadChildren(_1,_2);this.loadChildren(_1,_2)}
,isc.A.$44j=function isc_Tree__clearNodeCache(_1){if(_1)this.$44x=null;this.$44y=null}
,isc.A.isOpen=function isc_Tree_isOpen(_1){return _1!=null&&!!_1[this.openProperty]}
,isc.A.getOpenFolders=function isc_Tree_getOpenFolders(_1){if(_1==null)_1=this.root;var _2=this.getDescendantFolders(_1,new Function("node","return node."+this.openProperty));if(this.isOpen(_1))_2.add(_1);return _2}
,isc.A.getOpenFolderPaths=function isc_Tree_getOpenFolderPaths(_1){var _2=this.getOpenFolders(_1);for(var i=0;i<_2.length;i++){_2[i]=this.getPath(_2[i])}
return _2}
,isc.A.changeDataVisibility=function isc_Tree_changeDataVisibility(_1,_2,_3){if(this.isLeaf(_1))return false;_1[this.openProperty]=_2;this.$44j();if(_2&&!this.isLoaded(_1)){this.loadChildren(_1,_3)}}
,isc.A.toggleFolder=function isc_Tree_toggleFolder(_1){this.changeDataVisibility(_1,!this.isOpen(_1))}
,isc.A.openFolder=function isc_Tree_openFolder(_1,_2){if(_1==null)_1=this.root;if(!this.isOpen(_1)){this.changeDataVisibility(_1,true,_2)}}
,isc.A.openFolders=function isc_Tree_openFolders(_1){for(var i=0;i<_1.length;i++){var _3=_1[i];if(_3==null)continue;if(isc.isA.String(_3))_3=this.find(_3);if(_3!=null){this.openFolder(_3)}}}
,isc.A.closeFolder=function isc_Tree_closeFolder(_1){if(this.isOpen(_1)){this.changeDataVisibility(_1,false)}}
,isc.A.closeFolders=function isc_Tree_closeFolders(_1){for(var i=0;i<_1.length;i++){var _3=_1[i];if(_3==null)continue;if(isc.isA.String(_3))_3=this.find(_3);if(_3!=null){this.closeFolder(_3)}}}
,isc.A.openAll=function isc_Tree_openAll(_1){if(!_1)_1=this.root;var _2=this.getDescendants(_1,isc.Tree.FOLDERS_ONLY);for(var i=0,_4=_2.length;i<_4;i++){if(!this.isOpen(_2[i])){this.changeDataVisibility(_2[i],true)}}
this.changeDataVisibility(_1,true)}
,isc.A.closeAll=function isc_Tree_closeAll(_1){if(!_1)_1=this.root;var _2=this.getDescendants(_1,isc.Tree.FOLDERS_ONLY);for(var i=0,_4=_2.length;i<_4;i++){if(this.isOpen(_2[i])){this.changeDataVisibility(_2[i],false)}}
if(!(_1==this.root&&this.showRoot==false))this.changeDataVisibility(_1,false)}
,isc.A.getOpenList=function isc_Tree_getOpenList(_1,_2,_3,_4,_5,_6,_7){if(!_1)_1=this.root;if(_3==null)_3=this.$44q;if(_4==null)_4=this.sortDirection;if(_6==null)_6=this.$44r;if(this.isLeaf(_1)){if(_1==this.root)return[];return null}
var _8=[];if(_2!=isc.Tree.LEAVES_ONLY)_8[_8.length]=_1;if(!_7&&!this.isOpen(_1))return _8;var _9=this.getChildren(_1,_2,_3,_4,_5,_6);for(var i=0,_11=_9.length,_12;i<_11;i++){_12=_9[i];if(!_12){continue}
var _13=_12[this.childrenProperty];if(_13&&_13.length){_8=_8.concat(this.getOpenList(_12,_2,_3,_4,_5,_6,_7))}else{if(_2!=isc.Tree.FOLDERS_ONLY){_8[_8.length]=_12}}}
if(!this.showRoot&&_8[0]==this.root){_8=_8.slice(1,_8.length)}
return _8}
,isc.A.$44z=function isc_Tree__getOpenList(){if(!this.$44y||!this.cacheOpenList){this.$44y=this.getOpenList(this.root,this.openDisplayNodeType,this.$44q,this.sortDirection,this.openListCriteria)}
return this.$44y}
,isc.A.getNodeList=function isc_Tree_getNodeList(){if(!this.$44x||!this.cacheAllList){this.$44x=this.getAllNodes(this.root)}
return this.$44x}
,isc.A.getAllNodes=function isc_Tree_getAllNodes(_1){return this.getOpenList(_1,null,null,null,null,null,true)}
,isc.A.getLength=function isc_Tree_getLength(){return this.$44z().length}
,isc.A.get=function isc_Tree_get(_1){return this.$44z()[_1]}
,isc.A.getRange=function isc_Tree_getRange(_1,_2){return this.$44z().slice(_1,_2)}
,isc.A.indexOf=function isc_Tree_indexOf(_1,_2,_3){return this.$44z().indexOf(_1,_2,_3)}
,isc.A.lastIndexOf=function isc_Tree_lastIndexOf(_1,_2,_3){return this.$44z().lastIndexOf(_1,_2,_3)}
,isc.A.getAllItems=function isc_Tree_getAllItems(){return this.$44z()}
,isc.A.sortByProperty=function isc_Tree_sortByProperty(_1,_2,_3,_4){if(_1!=null)this.sortProp=_1;if(_2!=null)this.sortDirection=_2;if(_3&&isc.isA.Function(_3)){this.$44q=_3}else{this.$440()}
this.$44r=_4;this.$44j(true);this.dataChanged()}
,isc.A.$440=function isc_Tree__makeOpenNormalizer(){var _1=this.sortProp,_2=this.sortDirection,_3=this.separateFolders!=false;var _4=isc.SB.create();_4.append("var __tree__ = ",this.getID(),";\rvar value = '';");_4.append("if (__tree__ == null) return;\r");if(_3){var _5,_6;if(this.sortFoldersBeforeLeaves){_5="0:";_6="1:"}else{_5="1:";_6="0:"}
_4.append("value+=(__tree__.isFolder(obj) ? '"+_5+"' : '"+_6+"');")}
if(_1&&_1!="title"){_4.append("var prop = obj['",_1,"'];","if (isc.isA.Number(prop)) {","if (prop > 0) prop = '1' + prop.stringify(12,true);","else {","prop = 999999999999 + prop;","prop = '0' + prop.stringify(12,true);","}","} else if (isc.isA.Date(prop)) prop = prop.getTime();","if (prop != null) value += prop + ':';")}
if(_1){_4.append("var title = __tree__.getTitle(obj);","if (isc.isA.Number(title)) {","if (title > 0) title = '1' + title.stringify(12,true);","else {","title = 999999999999 + prop;","title = '0' + title.stringify(12,true);","}","} else if (isc.isA.Date(title)) title = title.getTime();","if (title != null) {title = title + ''; value += title.toLowerCase()}")}
_4.append("return value;");this.addMethods({$44q:new Function("obj,property",_4.toString())})}
,isc.A.loadSubtree=function isc_Tree_loadSubtree(_1,_2,_3){if(!_1)_1=this.getRoot();if(_2==null)_2=this.loadBatchSize;this.$441=_3?2:1;var _4=0,_5=1;while(_4<_2){var _6=this.$442(_2,_1,_4,_5++);if(_6==0)break;_4+=_6}
this.$441=null;if(_4>0)this.$44j(true)}
,isc.A.loadingBatch=function isc_Tree_loadingBatch(_1){if(_1)return this.$441==2;else return this.$441}
,isc.A.$442=function isc_Tree__loadToDepth(_1,_2,_3,_4){var _5=0;if(!this.isOpen(_2)){if(!this.isLoaded(_2))this.loadChildren(_2);if(this.isLoaded(_2)){if(this.openFolder(_2)===false)return _5}
if(_2.children){_5+=_2.children.length;_3+=_2.children.length}}
var _6=_2.children;if(_3>=_1||_4==0||_6==null)return _5;for(var i=0;i<_6.length;i++){var _8=_6[i];var _9=this.$442(_1,_8,_3,_4-1);_5+=_9;_3+=_9;if(_3>=_1)return _5}
return _5}
,isc.A.getFilteredTree=function isc_Tree_getFilteredTree(_1,_2,_3){_2=_2||isc.Tree.STRICT;var _3=this.dataSource||_3;if(!_3){isc.logWarn("Cannot apply filter to Tree without dataSource");return null}
var _4=this.duplicate(true,true);_4.$443(_1,_2,_3,_4.getRoot());return _4}
,isc.A.$443=function isc_Tree__filterChildren(_1,_2,_3,_4){if(_4.children==null||_4.children.length==0)return false;var _5=_4.children,_6=false;for(var i=_5.length-1;i>=0;i--){var _8=_5[i],_9=false;if(_8.children!=null&&_8.children.length>0){_9=this.$443(_1,_2,_3,_8)}
_6=_6||_9;if(!_9||_2==isc.Tree.STRICT){var _10=_3.applyFilter([_8],_1);if(_10!=null&&_10.length>0){_6=true}else{this.remove(_8,true)}}}
return _6}
);isc.B._maxIndex=isc.C+98;isc.A=isc.Tree;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.findChildrenProperty=function isc_c_Tree_findChildrenProperty(_1,_2){if(!isc.isAn.Object(_1))return;if(!_2)_2="any";var _3=(_2=="any"),_4=(_2=="object"),_5=(_2=="array"),_6=(_2=="objectArray");for(var _7 in _1){var _8=_1[_7];if(isc.isAn.Object(_8)){if(_3)return _7;if(isc.isAn.Array(_8)){if(isc.isAn.Object(_8[0]))return _7;if(!_4&&!_6)return _7}else{if(_4)return _7}}}}
,isc.A.discoverTree=function isc_c_Tree_discoverTree(_1,_2,_3){if(!_2)_2={};var _4=_2.childrenMode||"any";var _5=_2.scanMode||"branch";var _6=_2.tieMode||"node";var _7=_2.newChildrenProperty||isc.Tree.getInstanceProperty("childrenProperty"),_8=_2.typeProperty||"nodeType",_9=_2.nameProperty||"name";if(!isc.isAn.Array(_1))_1=[_1];var _10;if(_5=="level"||_5=="branch"){var _11={};for(var i=0;i<_1.length;i++){var _13=_1[i],_14=null;if(_13.$444)continue;_14=this.findChildrenProperty(_13,_4);if(_14==null)continue;_11[_14]=(_11[_14]||0);_11[_14]++}
var _15=isc.getValues(_11),_16=isc.getKeys(_11);if(_16.length==0){return}else if(_16.length==1){_10=_16[0]}else if(_6=="node"){}else if(_6=="stop"){return}else{var _17=_15.max(),_18=_15.indexOf(_17);_10=_16[_18]}}
var _19=[];for(var i=0;i<_1.length;i++){var _13=_1[i];var _20=_10;if(_13.$444)continue;if(!_20){_20=this.findChildrenProperty(_13,_4)}
if(_20==null)continue;var _21=_13[_20];if(_21!=null&&!isc.isAn.Array(_21))_21=[_21];else if(_21==null)_21=[];_13[_7]=_21;for(var j=0;j<_21.length;j++){var _23=_21[j];if(isc.isA.String(_23)){_21[j]=_23={name:_23,$444:true}}
_23[_8]=_20}
if(_5=="level"){_19.addAll(_21)}else{this.discoverTree(_21,_2,_20)}}
if(_5=="level"&&_19.length>0)this.discoverTree(_19,_2)}
,isc.A.getCleanNodeData=function isc_c_Tree_getCleanNodeData(_1,_2,_3,_4){if(_1==null)return null;var _5=[],_6=false;if(!isc.isAn.Array(_1)){_1=[_1];_6=true}
for(var i=0;i<_1.length;i++){var _8=_1[i],_9={};if(_4==null){var _10=_8.$5z;if(_10)_4=window[_10]}
for(var _11 in _8){if((_4!=null&&_11==_4.parentProperty)||_11=="$44l"||_11=="$5z"||_11=="__ref"||_11.startsWith("_isOpen_")||_11.startsWith("_isFolder_")||_11.startsWith("$44m")||_11=="$44n"||_11.startsWith("_selection_")||(_2==false&&_4&&_11==_4.childrenProperty))
{continue}
_9[_11]=_8[_11];if(_3&&_4&&_11==_4.childrenProperty&&isc.isAn.Array(_9[_11]))
{_9[_11]=_4.getCleanNodeData(_9[_11],true,true,_4)}}
_5.add(_9)}
if(_6)return _5[0];return _5}
);isc.B._maxIndex=isc.C+3;isc.ClassFactory.defineClass("Selection");isc.A=isc.Selection.getPrototype();isc.A.enabledProperty="enabled";isc.A.canSelectProperty="canSelect";isc.A.cascadeSelection=false;isc.A.$vi=true;isc.A=isc.Selection;isc.A.NONE="none";isc.A.SINGLE="single";isc.A.MULTIPLE="multiple";isc.A.SIMPLE="simple";isc.A.$445=0;isc.A=isc.Selection.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$446="up";isc.A.$447="down";isc.A.selectionRangeNotLoadedMessage="Can't select that many records at once.<br><br>"+"Please try working in smaller batches.";isc.B.push(isc.A.init=function isc_Selection_init(){if(!this.selectionProperty)this.selectionProperty="_selection_"+isc.Selection.$445++;this.partialSelectionProperty="$448"+this.selectionProperty;this.setData((this.data?this.data:[]))}
,isc.A.destroy=function isc_Selection_destroy(){if(this.data)this.ignoreData(this.data);delete this.data}
,isc.A.setData=function isc_Selection_setData(_1){if(this.data!=null)this.ignoreData(this.data);this.data=_1;if(this.data!=null)this.observeData(this.data);this.markForRedraw()}
,isc.A.observeData=function isc_Selection_observeData(_1){this.observe(_1,"dataChanged","observer.dataChanged()");if(_1.dataArrived)this.observe(_1,"dataArrived","observer.dataChanged()")}
,isc.A.ignoreData=function isc_Selection_ignoreData(_1){if(!_1)return;if(this.isObserving(_1,"dataChanged"))this.ignore(_1,"dataChanged");if(this.isObserving(_1,"dataArrived"))this.ignore(_1,"dataArrived")}
,isc.A.dataChanged=function isc_Selection_dataChanged(){this.markForRedraw()}
,isc.A.markForRedraw=function isc_Selection_markForRedraw(){this.$vi=true}
,isc.A.isSelected=function isc_Selection_isSelected(_1){if(this.$vi)this.cacheSelection();if(_1==null)return false;if(isc.isAn.XMLNode(_1))return"true"==_1.getAttribute(this.selectionProperty);return!!_1[this.selectionProperty]}
,isc.A.isPartiallySelected=function isc_Selection_isPartiallySelected(_1){if(this.$vi)this.cacheSelection();if(_1==null)return false;if(isc.isAn.XMLNode(_1))return"true"==_1.getAttribute(this.partialSelectionProperty);return!!_1[this.partialSelectionProperty]}
,isc.A.anySelected=function isc_Selection_anySelected(){return this.getSelection().length>0}
,isc.A.multipleSelected=function isc_Selection_multipleSelected(){return this.getSelection().length>1}
,isc.A.getSelection=function isc_Selection_getSelection(_1){if(this.$vi)this.cacheSelection();var _2=this.$449;if(_1==true&&_2!=null&&_2.length>0){var _3=this.$449;_2=[];for(var i=0;i<_3.length;i++){var _5=_3[i];if(!this.isPartiallySelected(_5)){_2[_2.length]=_5}}}
return _2}
,isc.A.getSelectedRecord=function isc_Selection_getSelectedRecord(){var _1=this.getSelection();if(_1&&_1.length>0)return _1[0]}
,isc.A.cacheSelection=function isc_Selection_cacheSelection(){if(this.$45a||this.$45b)return;this.$449=[];var _1=this.getItemList(),_2=isc.isA.ResultSet!=null&&isc.isA.ResultSet(_1),_3=_1.getLength();if(_2&&!_1.lengthIsKnown()){this.$vi=false;return}
this.$45a=true;var _4=false;for(var i=0;i<_3;i++){if(_2&&!_1.rowIsLoaded(i))continue;var _6=_1.get(i);if(_6!=null&&this.isSelected(_6)){if(this.cascadeSelection&&!this.isPartiallySelected(_6)){this.setSelected(_6,true,null,true);_4=true}
if(!_4){this.$449[this.$449.length]=_6}}}
if(_4){this.$449=[];for(var i=0;i<_3;i++){if(_2&&!_1.rowIsLoaded(i))continue;var _6=_1.get(i);if(_6!=null&&this.isSelected(_6)){this.$449[this.$449.length]=_6}}}
this.$45a=false;this.$vi=false}
,isc.A.setSelected=function isc_Selection_setSelected(_1,_2,_3,_4){if(_1==null)return false;if(this.data==null||this.data.destroyed)return false;if(_1[this.enabledProperty]==false)return false;if(_1[this.canSelectProperty]==false)return false;var _5=this.$45b;this.$45b=true;var _6=this.selectionProperty,_7=this.partialSelectionProperty,_8=isc.isAn.XMLNode(_1),_9=(_8?_1.getAttribute(_7):_1[_7]);if(_2==null)_2=true;if(this.cascadeSelection&&!this.useRemoteSelection){if(_3==this.$446){var _10=false,_11=_1.children.length;for(var i=0;i<_11;i++){var _13=_1.children.get(i),_14=isc.isAn.XMLNode(_13),_15=(_14?_13.getAttribute(_7):_13[_7]);if(_15||(_2&&!this.isSelected(_13))||(!_2&&this.isSelected(_13)))
{_10=true;break}}
if(_8){_1.setAttribute(_7,_10+"")}else{_1[_7]=_10}
if(_2!=_10)_2=true}else if(_1.children&&_1.children.length>0){if(_8){_1.removeAttribute(_7)}else{delete _1[_7]}}}
var _16=_8?_1.getAttribute(_6):_1[_6];if(_16==null)_16=false;if(_8){_1.setAttribute(_6,(_2==true)+"")}else{_1[_6]=_2}
this.lastSelectionItem=_1;this.lastSelectionState=_2;this.lastSelectionPreviousState=_16;this.lastSelectionPartialValue=_10;this.lastSelectionPreviousPartialValue=_9;var _17=(_8?_1.getAttribute(_7):_1[_7]);var _18=true;if(_2==_16&&_17==_9){_18=false}
if(!_4&&_18==false){if(!_5)this.$45b=false;return false}
this.markForRedraw();if(_18&&this.target&&this.target.selectionChange)this.target.selectionChange(_1,_2);if(this.cascadeSelection&&!this.useRemoteSelection)
{var _19=_1,_20=_2,_21=_16,_22=_10,_23=_9;var _24=false;if(this.cascadeSyncOnly==null){_24=true;this.cascadeSyncOnly=!_18}
if(_3!=this.$446&&!_8&&_1.children&&_1.children.length>0)
{this.selectList(_1.children,_2,this.$447)}
if(_3!=this.$447&&isc.isA.Tree(this.data)&&this.data.getParent(_1))
{this.setSelected(this.data.getParent(_1),_2,this.$446)}
this.lastSelectionItem=_19;this.lastSelectionState=_20;this.lastSelectionPreviousState=_21;this.lastSelectionPartialValue=_22;this.lastSelectionPreviousPartialValue=_23;if(_24){this.cascadeSyncOnly=null}}
if(!_5)this.$45b=false;return true}
,isc.A.select=function isc_Selection_select(_1){return this.setSelected(_1,true)}
,isc.A.deselect=function isc_Selection_deselect(_1){return this.setSelected(_1,false)}
,isc.A.selectSingle=function isc_Selection_selectSingle(_1){this.deselectAll();return this.select(_1)}
,isc.A.selectList=function isc_Selection_selectList(_1,_2){if(_2==null)_2=true;if(!_1)return false;var _3=false,_4=_1.getLength();for(var i=0;i<_4;i++){var _6=_1.get(i);if(this.isSelected(_6)==_2)continue;_3=this.setSelected(_6,_2)||_3}
return _3}
,isc.A.deselectList=function isc_Selection_deselectList(_1){this.selectList(_1,false)}
,isc.A.selectAll=function isc_Selection_selectAll(){return this.selectRange(0,this.getItemList().getLength())}
,isc.A.deselectAll=function isc_Selection_deselectAll(){return this.deselectList(this.getSelection())}
,isc.A.selectItem=function isc_Selection_selectItem(_1){return this.selectRange(_1,_1+1)}
,isc.A.deselectItem=function isc_Selection_deselectItem(_1){return this.deselectRange(_1,_1+1)}
,isc.A.selectRange=function isc_Selection_selectRange(_1,_2,_3){if(_3==null)_3=true;var _4=this.data;if(isc.isA.ResultSet!=null&&isc.isA.ResultSet(_4)&&!_4.rangeIsLoaded(_1,_2))
{isc.warn(this.selectionRangeNotLoadedMessage);return false}
return this.selectList(_4.getRange(_1,_2),_3)}
,isc.A.deselectRange=function isc_Selection_deselectRange(_1,_2){return this.selectRange(_1,_2,false)}
,isc.A.selectOnMouseDown=function isc_Selection_selectOnMouseDown(_1,_2){var _3=_1.selectionType||isc.Selection.MULTIPLE;if(_3==isc.Selection.NONE)return false;this.startRow=this.lastRow=_2;this.logDebug("selectOnMouseDown: recordNum: "+_2);var _4=this.data.get(_2),_5=this.isSelected(_4),_6=this.getSelection();if(Array.isLoading(_4))return false;this.deselectRecordOnMouseUp=false;this.deselectOthersOnMouseUp=false;var _7=(isc.Browser.isMac?isc.EventHandler.metaKeyDown():isc.EventHandler.ctrlKeyDown()),_8=isc.EH.shiftKeyDown();if(_3==isc.Selection.SINGLE){if(_7&&_5)this.deselect(_4);else if(!_5)this.selectSingle(_4);return true}else if(_8){if(_6.length==0){this.select(_4);return true}else{var _9=this.data,_10=_9.indexOf(_6[0]),_11=_9.indexOf(_6.last());if(_2>=_11){this.selectRange(_10,_2+1)}else if(_2<=_10){this.selectRange(_2,_11+1)}else{this.selectRange(_10,_2+1);this.deselectRange(_2+1,_11+1)}
return true}}else if(_3==isc.Selection.SIMPLE){if(!_5){this.select(_4);return true}else{this.deselectRecordOnMouseUp=true;return false}}else if(_7){this.setSelected(_4,!_5);return true}else{if(!_5){this.selectSingle(_4);return true}else if(isc.EventHandler.rightButtonDown()){this.deselectOnDragMove=true;return false}else{if(this.dragSelection){if(this.simpleDeselect){this.deselectAll();this.selectOriginOnDragMove=true;return true}
this.selectSingle(_4);return true}else{if(this.simpleDeselect){this.deselectAllOnMouseUp=true}else{this.deselectOthersOnMouseUp=(_6.length>1)}
return false}}}}
,isc.A.selectOnDragMove=function isc_Selection_selectOnDragMove(_1,_2){var _3=this.startRow,_4=this.lastRow;if(_2<0){this.logWarn("selectOnDragMove: got negative coordinate: "+_2);return}
if(_2==_4)return;if(this.selectOriginOnDragMove){this.select(this.data.getItem(_3));this.selectOriginOnDragMove=false}else if(this.deselectOnDragMove||this.deselectAllOnMouseUp||this.deselectOthersOnMouseUp){this.selectSingle(this.data.getItem(_3));this.deselectAllOnMouseUp=this.deselectOthersOnMouseUp=this.deselectOnDragMove=false}
if((_2>_3&&_3>_4)||(_4>_3&&_3>_2))
{this.deselectAll();if(_3>_2){this.selectRange(_2,_3+1)}else{this.selectRange(_3,_2+1)}}else if(_3>=_4&&_4>_2){this.selectRange(_2,_4)}else if(_3>=_2&&_2>_4){this.deselectRange(_4,_2)}else if(_3<=_2&&_2<_4){this.deselectRange(_2+1,_4+1)}else if(_3<=_4&&_4<_2){this.selectRange(_4,_2+1)}else{this.logWarn("dragMove case not handled: lastRow: "+_4+", currRow: "+_2+", startRow "+_3)}
this.lastRow=_2}
,isc.A.selectOnMouseUp=function isc_Selection_selectOnMouseUp(_1,_2){if(_1.selectionType==isc.Selection.NONE)return false;this.logDebug("selectOnMouseUp: recordNum: "+_2);if(this.deselectOthersOnMouseUp){this.selectSingle(this.data.getItem(_2));this.deselectOthersOnMouseUp=false;return true}else if(this.deselectRecordOnMouseUp){this.deselect(this.data.getItem(_2));this.deselectRecordOnMouseUp=false;return true}else if(this.deselectAllOnMouseUp){this.deselectAll();this.deselectAllOnMouseUp=false;return true}else
return false}
,isc.A.getItemList=function isc_Selection_getItemList(){if(this.data&&isc.isA.Tree(this.data))return this.data.getNodeList();return(this.data?this.data:[])}
);isc.B._maxIndex=isc.C+30;isc.ClassFactory.defineClass("DetailViewer","Canvas","DataBoundComponent");isc.A=isc.DetailViewer.getPrototype();isc.A.dataFetchMode="basic";isc.A.dataArity="either";isc.A.fieldIdProperty="name";isc.A.recordsPerBlock=1;isc.A.blockSeparator="<br><br>";isc.A.showEmptyField=true;isc.A.emptyCellValue="&nbsp;";isc.A.labelPrefix="";isc.A.labelSuffix=":";isc.A.valueAlign="left";isc.A.wrapValues=true;isc.A.useInnerWidth=true;isc.A.clipValues=false;isc.A.styleName="detailViewer";isc.A.blockStyle="detailBlock";isc.A.labelStyle="detailLabel";isc.A.cellStyle="detail";isc.A.headerStyle="detailHeader";isc.A.separatorStyle="detail";isc.A.cellPadding=3;isc.A.showEmptyMessage=true;isc.A.emptyMessage="No items to display.";isc.A.emptyMessageStyle="normal";isc.A.loadingMessage="&nbsp;${loadingImage}";isc.A.loadingMessageStyle="normal";isc.A.defaultHeight=35;isc.A.showLabel=true;isc.A=isc.DetailViewer.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$ij="date";isc.B.push(isc.A.initWidget=function isc_DetailViewer_initWidget(){this.Super("initWidget",arguments);if(this.fieldState!=null)this.setFieldState(this.fieldState);else this.setFields(this.fields)}
,isc.A.setData=function isc_DetailViewer_setData(_1){if(this.data)this.ignore(this.data,"dataChanged");this.data=_1;if(this.data&&this.data.dataChanged){this.observe(this.data,"dataChanged","observer.dataChanged()")}
this.markForRedraw("new data")}
,isc.A.dataChanged=function isc_DetailViewer_dataChanged(){this.applyHilites();this.markForRedraw()}
,isc.A.getData=function isc_DetailViewer_getData(){return this.data}
,isc.A.getFields=function isc_DetailViewer_getFields(){return this.fields}
,isc.A.getInnerHTML=function isc_DetailViewer_getInnerHTML(){var _1=this.getData();if(this.fields==null||this.fields.length==0){return"Note: you must define detailViewer.fields to specify what to display!"}
if(isc.ResultSet!=null&&isc.isA.ResultSet(_1)&&!_1.lengthIsKnown()){_1.get(0);return this.loadingMessageHTML()}
if(_1==null||(_1.getLength&&_1.getLength()==0)){return this.emptyMessageHTML()}
if(!isc.isA.List(_1))_1=[_1];if(Array.isLoading(_1.get(0))&&this.isOffline()){return this.emptyMessageHTML()}
if(_1.getLength()==1||this.recordsPerBlock=="*"){return this.getBlockHTML(_1)}else{var _2=isc.StringBuffer.newInstance();for(var _3=0;_3<_1.getLength();_3+=this.recordsPerBlock){_2.append(this.getBlockHTML(_1.getRange(_3,_3+this.recordsPerBlock)),this.blockSeparator)}
return _2.toString()}}
,isc.A.getBlockHTML=function isc_DetailViewer_getBlockHTML(_1){var _2=_1.getLength();var _3="<TABLE BORDER=0 CELLSPACING=0 CLASS="+this.blockStyle+" WIDTH="+(this.useInnerWidth&&!this.isPrinting?this.getInnerWidth():"'100%'")+" CELLPADDING="+this.cellPadding+(this.clipValues?" STYLE='table-layout:fixed'":"");_3+=">";var _4=this.fields;for(var _5=0,_6=_4.length;_5<_6;_5++){var _7=_4[_5];if(!_7||_7.hidden||_7.visible==false)continue;if(_7.showIf){if(!isc.isA.Function(_7.showIf)){isc.Func.replaceWithMethod(_7,"showIf","viewer,valueList")}
if(_7.showIf(this,_1)==false)continue}
var _8=_7.type?_7.type:"";if(_8!="separator"&&_8!="header"&&!this.showEmptyField){var _9=true;for(var i=0;i<_1.getLength();i++){var _11=_1.get(i)[_7[this.fieldIdProperty]]
if(!(_11==null||_11=="")){_9=false;break}}
if(_9)continue}
if(_7.output){if(!isc.isA.Function(_7.output)){isc.Func.replaceWithMethod(_7,"output","fieldNum,field,valueList")}
_3+=_7.output(_5,_7,_1)}else{_3+=this.outputItem(_5,_7,_1)}}
_3+="</TABLE>";return _3}
,isc.A.outputItem=function isc_DetailViewer_outputItem(_1,_2,_3){var _4=(_2.type?_2.type:"value"),_5="output_"+_4,_6="";if(!this[_5])_5="output_value";_6+="<TR"+(this.rowClass!=null?" CLASS='"+this.rowClass+"'":"")+">";_6+=this[_5](_1,_2,_3);_6+="</TR>\r";return _6}
,isc.A.output_blob=function isc_DetailViewer_output_blob(_1,_2,_3){return this.output_binary(_1,_2,_3)}
,isc.A.output_upload=function isc_DetailViewer_output_upload(_1,_2,_3){return this.output_binary(_1,_2,_3)}
,isc.A.output_binary=function isc_DetailViewer_output_binary(_1,_2,_3){var _4="<TD WIDTH=10% CLASS='"+(this.isPrinting?this.printLabelStyle||this.labelStyle:this.labelStyle)+"' ALIGN=RIGHT"+(this.wrapLabel?">":" NOWRAP><NOBR>")+this.labelPrefix+(_2.title?_2.title:_2[this.fieldIdProperty])+this.labelSuffix+"<\/NOBR><\/TD>";for(var i=0;i<_3.getLength();i++){var _6=_3.get(i),_7=this.getData().indexOf(_6),_8=_2.nativeName||_2.name,_9=_6[_8+"_filename"],_10=isc.Canvas.imgHTML("[SKIN]actions/view.png",16,16,null,"style='cursor:"+isc.Canvas.HAND+"' onclick='"+this.getID()+".viewRow("+_7+")'"),_11=isc.Canvas.imgHTML("[SKIN]actions/download.png",16,16,null,"style='cursor:"+isc.Canvas.HAND+"' onclick='"+this.getID()+".downloadRow("+_7+")'"),_12=_10+"&nbsp;"+_11+"&nbsp;"+_9;_4+="<TD CLASS='"+this.getCellStyle(_12,_2,_6,this)+"'>"+_12+"<\/TD>"}
return _4}
,isc.A.viewRow=function isc_DetailViewer_viewRow(_1){isc.DS.get(this.dataSource).viewFile(this.getData().get(_1))}
,isc.A.downloadRow=function isc_DetailViewer_downloadRow(_1){isc.DS.get(this.dataSource).downloadFile(this.getData().get(_1))}
,isc.A.output_value=function isc_DetailViewer_output_value(_1,_2,_3){var _4;if(this.showLabel){_4="<TD WIDTH=10% CLASS='"+(this.isPrinting?this.printLabelStyle||this.labelStyle:this.labelStyle)+"' ALIGN=RIGHT"+(this.wrapLabel?">":" NOWRAP><NOBR>")+this.labelPrefix+(_2.title?_2.title:_2[this.fieldIdProperty])+this.labelSuffix+"<\/NOBR><\/TD>"}else{_4=""}
if(_2.valueMap&&isc.isA.String(_2.valueMap))
_2.valueMap=this.getGlobalReference(_2.valueMap);for(var i=0;i<_3.getLength();i++){var _6=_3.get(i),_7;if(_2.type=="image"){var _8=isc.Canvas.getFieldImageDimensions(_2,_6);var _9=this.getCellValue(_6,_2),_10=_2.imageURLPrefix||_2.baseURL||_2.imgDir;_7=this.imgHTML(_9,_8.width,_8.height,null,_2.extraStuff,_10,_2.activeAreaHTML)}else{_7=this.getCellValue(_6,_2)}
var _11=this.getRawValue(_6,_2);var _12;if(_2.getCellStyle){_12=_2.getCellStyle(_11,_2,_6,this)}else{_12=(this.getCellStyle(_11,_2,_6,this)||this.cellStyle)}
var _13=" style='";if(this.clipValues)_13+="overflow:hidden;";_13+="text-align:"+this.valueAlign;if(this.getCellCSSText){var _14=this.getCellCSSText(_11,_2,_6,this);if(_14!=null)_13+=isc.semi+_14}
_13+="'";_4+="<TD CLASS='"+_12+"'"+_13+(this.wrapValues?">":" NOWRAP><NOBR>")+_7+(this.wrapValues?"<\/NOBR>":"")+"<\/TD>"}
return _4}
,isc.A.getRawValue=function isc_DetailViewer_getRawValue(_1,_2){if(!_1||!_2)return null;if(_2.dataPath)return isc.Canvas.$3e(_2.dataPath,_1);return _1[_2.name]}
,isc.A.getCellCSSText=function isc_DetailViewer_getCellCSSText(_1,_2,_3,_4){return this.getRecordHiliteCSSText(_3,"",_2)}
,isc.A.getCellStyle=function isc_DetailViewer_getCellStyle(_1,_2,_3,_4){if(_2){if(_2.getCellStyle)return _2.getCellStyle(_1,_2,_3,_4);if(this.isPrinting&&_2.printCellStyle){return _2.printCellStyle}
if(_2.cellStyle){return _2.cellStyle}}
return(this.isPrinting&&this.printCellStyle!=null)?this.printCellStyle:this.cellStyle}
,isc.A.getSelectedRecord=function isc_DetailViewer_getSelectedRecord(){return this.data.get(0)}
,isc.A.getCellValue=function isc_DetailViewer_getCellValue(_1,_2){var _3=this.getRawValue(_1,_2);if(isc.isA.String(_2.formatCellValue)){_2.formatCellValue=isc.Func.expressionToFunction("value,record,field,viewer",_2.formatCellValue)}
if(_2.getCellValue!=null){if(isc.isA.String(_2.getCellValue)){_2.getCellValue=isc.Func.expressionToFunction("value,record,field,viewer",_2.getCellValue)}
_3=_2.getCellValue(_3,_1,_2,this);if(_2.formatCellValue)_3=_2.formatCellValue(_3,_1,_2,this)}else{if(_2.valueMap!=null)_3=isc.getValueForKey(_3,_2.valueMap);if(_2.formatCellValue)_3=_2.formatCellValue(_3,_1,_2,this);if(_3==null||isc.is.emptyString(_3))_3=this.emptyCellValue}
if(_2.formatCellValue==null&&this.formatCellValue){_3=this.formatCellValue(_3,_1,_2)}else{_3=this.$28p(_1,_2,_3);var _4=_2.escapeHTML;if(_4==null)_4=_2.asHTML;if(_4)_3=_3.asHTML()}
if(_2){if(_2.userFormula)_3=this.getFormulaFieldValue(_2,_1);else if(_2.userSummary)_3=this.getSummaryFieldValue(_2,_1);else if(_2.type=="imageFile"){if(_2.showFileInline!=false){if(!_1[_2[this.fieldIdProperty]+"$351"]){var _5=isc.Canvas.getFieldImageDimensions(_2,_1),_6=this.getDataSource().streamFile(_1);_3=_1[_2[this.fieldIdProperty]+"$351"]=this.imgHTML(_6,_5.width,_5.height)}else
_3=_1[_2[this.fieldIdProperty]+"$351"]}else{_3=this.getViewDownloadHTML(_2,_1)}}else if(_2.showFileInline==true){this.logWarn("getCellValue(): Unsupported field-type for showFileInline: "+_2.type)}}
var _7=this.getFieldHilites(_1,_2);if(_7!=null)_3=this.applyHiliteHTML(_7,_3);return _3}
,isc.A.getViewDownloadHTML=function isc_DetailViewer_getViewDownloadHTML(_1,_2){if(_2==null)return null;var _3=_1.nativeName||_1.name,_4=_2[_3+"_filename"];if(_4==null||isc.isA.emptyString(_4))return"&nbsp;";var _5=isc.Canvas.imgHTML("[SKIN]actions/view.png",16,16,null,"style='cursor:"+isc.Canvas.HAND+"' onclick='"+this.getID()+".viewFile("+_2+","+_1+")'");var _6=isc.Canvas.imgHTML("[SKIN]actions/download.png",16,16,null,"style='cursor:"+isc.Canvas.HAND+"' onclick='alert('running');"+this.getID()+".downloadFile("+_2+","+_1+")'");return"<nobr>"+_5+"&nbsp;"+_6+"&nbsp;"+_4+"</nobr>"}
,isc.A.viewFile=function isc_DetailViewer_viewFile(_1,_2){isc.DS.get(this.dataSource).viewFile(_1,_2.name)}
,isc.A.downloadFile=function isc_DetailViewer_downloadFile(_1,_2){isc.DS.get(this.dataSource).downloadFile(_1,_2.name)}
,isc.A.$28p=function isc_DetailViewer__formatDataType(_1,_2,_3){if(isc.isA.Date(_3)){if(this.$28q(_2)){var _4=isc.SimpleType.inheritsFrom(_2.type,"time");_3=isc.Time.toTime(_3,this.$28r(_2),_4)}else{if(isc.SimpleType.inheritsFrom(_2.type,"date")&&!isc.SimpleType.inheritsFrom(_2.type,"datetime"))
{_3=_3.toShortDate(this.$28s(_2),false)}else{_3=_3.toNormalDate(this.$28s(_2))}}}
if(_2.$141!=null){_3=_2.$14v.normalDisplayFormatter(_3,_2,this,_1)}
return isc.iscToLocaleString(_3)}
,isc.A.$28q=function isc_DetailViewer__formatAsTime(_1){if(_1==null)return false;if(_1.dateFormatter==null&&_1.timeFormatter!=null)return true;if(_1.timeFormatter==null&&_1.dateFormatter!=null)return false;return isc.SimpleType.inheritsFrom(_1.type,"time")}
,isc.A.$28s=function isc_DetailViewer__getDateFormatter(_1){if(_1.dateFormatter)return _1.dateFormatter;if(_1.displayFormat!=null&&isc.SimpleType.inheritsFrom(_1.type,"date")){return _1.displayFormat}
if(this.datetimeFormatter!=null&&isc.SimpleType.inheritsFrom(_1.type,"datetime")){return this.datetimeFormatter}
return this.dateFormatter}
,isc.A.$28r=function isc_DetailViewer__getTimeFormatter(_1){if(_1.timeFormatter)return _1.timeFormatter;if(_1.displayFormat!=null&&isc.SimpleType.inheritsFrom(_1.type,"time")){return _1.displayFormat}
return this.timeFormatter}
,isc.A.getRecordIndex=function isc_DetailViewer_getRecordIndex(_1){var _2=this.Super('getRecordIndex',arguments);if(_2==-1)_2=0;return _2}
,isc.A.output_header=function isc_DetailViewer_output_header(_1,_2,_3){return"<TD COLSPAN="+(_3.getLength()+1)+" CLASS='"+(this.isPrinting&&this.printHeaderStyle?this.printHeaderStyle:this.headerStyle)+"'>"+_2.value+"</TD>"}
,isc.A.output_separator=function isc_DetailViewer_output_separator(_1,_2,_3){var _4=(_2.width==null?_2.defaultSeparatorWidth:_2.width),_5=(_2.height==null?_2.defaultSeparatorHeight:_2.height);return"<TD COLSPAN="+(_3.getLength()+1)+" CLASS='"+this.separatorStyle+"'>"+isc.Canvas.spacerHTML(_4,_5)+"</TD>"}
,isc.A.getEmptyMessage=function isc_DetailViewer_getEmptyMessage(){return this.emptyMessage}
,isc.A.getLoadingMessage=function isc_DetailViewer_getLoadingMessage(){return this.loadingMessage==null?"&nbsp;":this.loadingMessage.evalDynamicString(this,{loadingImage:this.imgHTML(isc.Canvas.loadingImageSrc,isc.Canvas.loadingImageSize,isc.Canvas.loadingImageSize)})}
,isc.A.emptyMessageHTML=function isc_DetailViewer_emptyMessageHTML(){if(this.isOffline()){if(!this.showOfflineMessage)return"&nbsp;"}else{if(!this.showEmptyMessage)return"&nbsp;"}
return"<TABLE WIDTH=100%>"+"<TR><TD CLASS='"+this.emptyMessageStyle+"' ALIGN=CENTER><BR><BR>"+(this.isOffline()?this.offlineMessage:this.getEmptyMessage())+"<\/TD><\/TR><\/TABLE>"}
,isc.A.loadingMessageHTML=function isc_DetailViewer_loadingMessageHTML(){return"<TABLE WIDTH=100%>"+"<TR><TD CLASS='"+this.loadingMessageStyle+"' ALIGN=CENTER><BR><BR>"+this.getLoadingMessage()+"<\/TD><\/TR><\/TABLE>"}
,isc.A.setFieldState=function isc_DetailViewer_setFieldState(_1){if(_1==null&&this.fieldState!=null){if(isc.isA.String(this.fieldState)){_1=this.evalViewState(this.fieldState,"fieldState")}}else _1=this.evalViewState(_1,"fieldState");this.completeFields=this.$3w(_1,true);this.setFields(this.completeFields);this.markForRedraw();this.fieldStateChanged()}
,isc.A.setFields=function isc_DetailViewer_setFields(_1){if(this.completeFields==null||this.fields==null)this.fields=[];this.completeFields=this.bindToDataSource(_1);if(this.completeFields==null)this.completeFields=[];this.deriveVisibleFields()}
,isc.A.deriveVisibleFields=function isc_DetailViewer_deriveVisibleFields(){this.fields.setArray(this.getVisibleFields(this.completeFields))}
,isc.A.getVisibleFields=function isc_DetailViewer_getVisibleFields(_1){var _2=_1.duplicate();for(var i=0;i<_1.length;i++){var _4=_1.get(i);if(!this.fieldShouldBeVisible(_4)||_4.visible==false)_2.remove(_4)}
return _2}
,isc.A.getTitleFieldValue=function isc_DetailViewer_getTitleFieldValue(_1){var _2=this.getDataSource().getTitleField(),_3=this.getCellValue(_1,this.getDataSource().getField(_2));return _3}
,isc.A.getStandaloneFieldValue=function isc_DetailViewer_getStandaloneFieldValue(_1,_2){var _3=this.getCellValue(_1,this.getField(_2));return _3}
,isc.A.hideField=function isc_DetailViewer_hideField(_1){this.toggleField(_1,false)}
,isc.A.showField=function isc_DetailViewer_showField(_1){this.toggleField(_1,true)}
,isc.A.toggleField=function isc_DetailViewer_toggleField(_1,_2){var _3=this.getField(_1);_3.showIf=_2?"true":"false";_3.visible=_2;this.setFields(this.getAllFields());this.markForRedraw();this.fieldStateChanged()}
,isc.A.getField=function isc_DetailViewer_getField(_1){var _2=this.getAllFields(),_3=this.fields,_4;if(isc.isAn.Object(_1)&&_1[this.fieldIdProperty]!=null){_4=_2.find(this.fieldIdProperty,_1[this.fieldIdProperty])||_3.find(this.fieldIdProperty,_1[this.fieldIdProperty])}else if(isc.isA.Number(_1)){_4=_2[_1]||_3[_1]}else{_4=_2.find(this.fieldIdProperty,_1)||_3.find(this.fieldIdProperty,_1)}
return _4}
,isc.A.getFormattedValue=function isc_DetailViewer_getFormattedValue(_1,_2,_3){return this.getCellValue(_1,this.getSpecifiedField(_2))}
,isc.A.getPivotedExportData=function isc_DetailViewer_getPivotedExportData(_1){var _2=[],_3=this.getAllFields(),_4=this.data,_5,_6,_7;if(isc.isA.Object(_1)){_5=_1.includeHiddenFields;_6=_1.allowedProperties;_7=_1.alwaysExportExpandedStyles}
if(isc.isA.ResultSet(_4))_4=_4.getAllLoadedRows();if(!isc.isA.Array(_4))_4=[_4];for(var _8=0;_8<_3.length;_8++){var _9=_3[_8],_10={},_11=this.recordsPerBlock;_10.title=_9.title||_9.name;if(isc.isA.String(_10.title))_10.title=this.htmlUnescapeExportFieldTitle(_10.title);if(_11==null)_11=1;if(_11=="*")_11=100000;if((!this.fields.contains(_9))&&!_5)continue;for(var _12=0;_12<_11&&_12<_4.getLength();_12++)
{var _13=_4[_12],_14=this.getFieldNum(_9.name),_15="value"+(_12+1),_16=_15+"$style";var _17=this.getExportFieldValue(_13,_9.name,_14);if(!(_17==null||_17=="&nbsp;"))_10[_15]=_17;this.addDetailedExportFieldValue(_10,_16,_13,_9,_14,_6,_7);if(_10[_16]==null||_10[_16]=="&nbsp;")
delete _10[_16]}
_2.push(_10)}
return _2}
);isc.B._maxIndex=isc.C+45;isc.DetailViewer.registerStringMethods({getCellValue:"record,field",getCellStyle:"value,field,record,viewer",getCellCSSText:"value,field,record,viewer",formatCellValue:"value,record,field,viewer",fieldStateChanged:""});isc.ClassFactory.defineClass("GridRenderer","Canvas");isc.A=isc.GridRenderer;isc.A.SELECTED="Selected";isc.A.DISABLED="Disabled";isc.A.OVER="Over";isc.A.standardStyleSuffixes=["","Over","Selected","SelectedOver","Disabled","DisabledOver","DisabledSelected","DisabledSelectedOver","Dark","OverDark","SelectedDark","SelectedOverDark","DisabledDark"];isc.A=isc.GridRenderer.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.totalRows=0;isc.A.drawAllMaxCells=250;isc.A.recordCanSelectProperty="canSelect";isc.A.isSeparatorProperty="isSeparator";isc.A.singleCellValueProperty="singleCellValue";isc.A.instantScrollTrackRedraw=true;isc.A.scrollRedrawDelay=75;isc.A.drawAheadRatio=1.3;isc.A.quickDrawAheadRatio=1.0;isc.A.cellHeight=20;isc.A.fixedRowHeights=true;isc.A.fixedColumnWidths=true;isc.A.cellSpacing=0;isc.A.cellPadding=2;isc.A.canSelectOnRightMouse=true;isc.A.hoverByCell=true;isc.A.backgroundColor="white";isc.A.tableStyle="listTable";isc.A.baseStyle="cell";isc.A.alternateRowFrequency=1;isc.A.emptyCellValue="&nbsp;";isc.A.fastCellUpdates=true;isc.A.overflow="auto";isc.A.$ww=true;isc.A.canFocus=true;isc.A.animateRowsMaxTime=1000;isc.A.snapToCells=false;isc.A.snapInsideBorder=false;isc.A.snapHDirection=isc.Canvas.BEFORE;isc.A.snapVDirection=isc.Canvas.BEFORE;isc.B.push(isc.A.setFastCellUpdates=function isc_GridRenderer_setFastCellUpdates(_1){if(_1&&!isc.Browser.isIE){this.fastCellUpdates=false;return}
if(_1==this.fastCellUpdates)return;this.fastCellUpdates=_1;this.markForRedraw()}
);isc.B._maxIndex=isc.C+1;isc.A=isc.GridRenderer.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$45c=["<DIV style='position:"+(isc.Page.isRTL()?"inline":"relative")+";z-index:",,";'>",,"</DIV>"];isc.A.$45d="<table style='position:absolute;top:0px;font-size:1px;height:100%;width:100%;z-index:1;overflow:hidden;visibility:hidden;'><tr><td>&nbsp;</td></tr></table>";isc.A.maxAnimateSlideInRows=100;isc.A.$pr="none";isc.A.screenReader_suppressHandleFocus=true;isc.A.$45e={};isc.A.$45f="<DIV role='presentation' cellClipDiv=true style='overflow:hidden;";isc.A.$45g="<div>";isc.A.$45h="</div>";isc.A.$45i="within";isc.A.$45j="expand";isc.A.$25e="cell";isc.A.alignSnapToMap={left:{top:"TL",center:"L",bottom:"BL"},right:{top:"TR",center:"R",bottom:"BR"},center:{top:"T",center:"C",bottom:"B"}};isc.A.recordCustomStyleProperty="customStyle";isc.A.showSelectedStyle=true;isc.A.$45k="NOBR";isc.A.$45l="cellClipDiv";isc.A.$s6="height";isc.A.$45m="minHeight";isc.A.$px=";";isc.A.$45n="padding-top:0px;padding-bottom:0px;";isc.A.$45o="overflow:hidden;";isc.A.avgRowHeight=60;isc.B.push(isc.A.initWidget=function isc_GridRenderer_initWidget(){if(!this.$45p)this.setColumnWidths([]);if(this.selection)this.setSelection(this.selection);if(this.overflow==isc.Canvas.VISIBLE){this.showAllRows=true}
if(!this.fixedRowHeights&&this.virtualScrolling==null)this.virtualScrolling=true;if(!this.fixedRowHeights&&!this.showAllRows){if(this.showCustomScrollbars==false){this.logInfo("Variable height records cannot be used with native scrollbars;"+" setting showCustomScrollbars:true on this GridRenderer and using"+" the special 'NativeScrollbar' class as a scrollbarConstructor.");this.showCustomScrollbars=true;this.scrollbarConstructor="NativeScrollbar"}}
this.setFastCellUpdates(this.fastCellUpdates)}
,isc.A.shouldShowAllColumns=function isc_GridRenderer_shouldShowAllColumns(){if(this.showAllColumns)return true;if(!this.fixedRowHeights&&!this.showAllRows)return true;if(this.overflow==isc.Canvas.VISIBLE){return true}
return false}
,isc.A.isEmpty=function isc_GridRenderer_isEmpty(){return false}
,isc.A.$45q=function isc_GridRenderer__showEmptyMessage(_1,_2){return this.getEmptyMessageHTML(_1,_2,this.grid.isOffline())}
,isc.A.getEmptyMessageHTML=function isc_GridRenderer_getEmptyMessageHTML(_1,_2,_3){if(!_3){if(!this.showEmptyMessage)return"&nbsp;"}else{if(!this.showOfflineMessage)return"&nbsp;"}
if(this.isPrinting){if(_1==null)_1=0;if(_2==null)_2=this.fields?this.fields.getLength()-1:0;return"<TABLE role='presentation' cellspacing=0 style='width:100%' class='"+this.emptyMessageTableStyle+"'>"+this.grid.getPrintHeaders(_1,_2)+"<TR><TD  ALIGN=CENTER VALIGN=TOP class='"+(_3?this.offlineMessageStyle:this.emptyMessageStyle)+"' colspan='"+((_2-_1)+1)+"'>"+(_3?this.getOfflineMessage():this.getEmptyMessage())+"</TD></TR></TABLE>"}
var _4=this.getInnerWidth(),_5=0;if(this.expandEmptyMessageToMatchFields&&this.$45p){_5=this.$45p.sum()-_4;if(_5<0)_5=0}
var _6=_5&&this.overflow!=isc.Canvas.VISIBLE;var _7=isc.StringBuffer.create();_7.append("<TABLE role='presentation' BORDER=0 MARGIN=0 CELLSPACING=0 CLASS='",this.emptyMessageTableStyle,"' style='width:",(_4+_5),"px;",(isc.Browser.isSafari?"height"+this.getInnerHeight()+":px;'":"' HEIGHT=100%"),"><TR><TD ALIGN=CENTER VALIGN=TOP CLASS='",(_3?this.offlineMessageStyle:this.emptyMessageStyle),"' style='padding-left:0px;padding-right:0px;'>",(_3?this.getOfflineMessage():this.getEmptyMessage()),(_5&&_6?"<br>"+isc.Canvas.spacerHTML(_4,1):null),"</TD>");if(_5&&_6){_7.append("<TD style='padding-left:0px;padding-right:0px;'>",isc.Canvas.spacerHTML(_5,1),"</TD>")}
_7.append("</TR></TABLE>");return _7.release()}
,isc.A.getEmptyMessage=function isc_GridRenderer_getEmptyMessage(){return this.emptyMessage}
,isc.A.getOfflineMessage=function isc_GridRenderer_getOfflineMessage(){return this.grid.offlineMessage}
,isc.A.getInnerHTML=function isc_GridRenderer_getInnerHTML(){var _1=this.getTableHTML(),_2=this.$45c;_2[1]=this.getTableZIndex();_2[3]=_1;if(isc.Browser.isMoz)_2[5]=this.$45d;return _2.join(isc.emptyString)}
,isc.A.isFastScrolling=function isc_GridRenderer_isFastScrolling(){return this.isDragScrolling()||this.isRepeatTrackScrolling()}
,isc.A.shouldUseQuickDrawAheadRatio=function isc_GridRenderer_shouldUseQuickDrawAheadRatio(){return this.useQuickDrawAheadRatio||this.isFastScrolling()}
,isc.A.doneFastScrolling=function isc_GridRenderer_doneFastScrolling(){var _1=this.$45r;if(_1){this.$45s=true;this.markForRedraw("Done Fast scrolling.")}}
,isc.A.addDrawAhead=function isc_GridRenderer_addDrawAhead(_1,_2,_3,_4,_5){var _6=this.shouldUseQuickDrawAheadRatio(),_7=_6&&this.quickDrawAheadRatio!=null?this.quickDrawAheadRatio:this.drawAheadRatio,_8=Math.ceil((_2-_1)*_7);if(this.$45s)_4=null;if(_4!=null){if(_4)_2=_1+_8;else _1=_2-_8}else{if(_1==0)_2=_8;else{var _9=Math.ceil((_8-(_2-_1))/2);_1-=_9;_2+=_9}}
if(_1<0){_2-=_1;_1=0}
if(_2>=_3){var _10=_2-(_3-1);_1=Math.max(0,(_1-_10));_2=Math.max(0,_3-1)}
if(_6)this.$45r=true;else delete this.$45r;return[_1,_2]}
,isc.A.getExtraRowHeight=function isc_GridRenderer_getExtraRowHeight(_1,_2){var _3=0;for(var _4=_1;_4<_2;_4++){var _5=this.getRowHeight(this.getCellRecord(_4,0),_4),_6=(_5-this.cellHeight);if(_6>0){_3+=_6}}
return _3}
,isc.A.getDrawArea=function isc_GridRenderer_getDrawArea(_1){var _2=this.getTotalRows(),_3,_4,_5;var _6=_2*this.fields.length,_7=_6<=this.drawAllMaxCells&&!isc.EH.dragging&&!this.isAnimating()&&!(this.parentElement&&this.parentElement.isAnimating());if(this.showAllRows||_7){_3=0;_4=Math.max(_2-1,0)}else{var _8=this.$45t();_3=_8[0];_4=_8[1];_5=_8[2]}
var _9,_10,_11=this.fields.length,_12;if(_1!=null){_9=_1;_10=_1+1}else if(_7||this.shouldShowAllColumns()){_9=0;_10=_11-1}else{var _13=this.getVisibleColumns();_12=(this.lastScrollLeft==null?null:this.lastScrollLeft<this.getScrollLeft());var _14=this.addDrawAhead(_13[0],_13[1],_11,_12);_9=_14[0];_10=_14[1]}
if(this.cacheDOM&&!this.$45u){this.$45u=_4-_3;this.$45v=_10-_9}
return[_3,_4,_9,_10]}
,isc.A.$45t=function isc_GridRenderer__getDrawRows(){var _1=this.$45w();var _2=(this.lastScrollTop==null?null:this.lastScrollTop<this.getScrollTop());var _3=this.getTotalRows();var _4=this.addDrawAhead(_1[0],_1[1],_3,_2,true);_4[2]=_2;return _4}
,isc.A.getRowCoordinate=function isc_GridRenderer_getRowCoordinate(_1){var _2=this.getEventRow(_1),_3=this.getRowTop(_2),_4=_1-_3,_5=this.getRowSize(_2),_6=_4/ _5;return _2+_6}
,isc.A.scrollToRatio=function isc_GridRenderer_scrollToRatio(_1,_2,_3,_4,_5){if(!_1||!this.$45x){return this.invokeSuper(isc.GridRenderer,"scrollToRatio",_1,_2,_3,_4,_5)}
var _6=this.getTotalRows()-1,_7=_2*_6,_8=Math.floor(_7),_9=Math.round((_7-_8)*this.getRowSize(_8));this.$45y=_8;this.$45z=_9;this.$450(_3||"scrollToRatio");if(this.isDirty()){this.$451=_2;this.$45y=_8;this.$45z=_9}}
,isc.A.getScrollRatio=function isc_GridRenderer_getScrollRatio(_1,_2,_3,_4){if(!_1||!this.$45x){return this.invokeSuper(isc.GridRenderer,"getScrollRatio",_1,_2,_3,_4)}
if(this.isDirty()&&this.$451!=null)return this.$451;var _5=this.getTotalRows()-1;if(_5<=0)return 0;var _6=this.getScrollTop(),_7=this.getRowCoordinate(_6),_8=_7/ _5;return Math.min(1,_8)}
,isc.A.getViewportRatio=function isc_GridRenderer_getViewportRatio(_1,_2,_3,_4){if(!_1||!this.$45x){return this.invokeSuper(isc.GridRenderer,"getViewportRatio",_1,_2,_3,_4)}
var _5=this.$452||this.getAvgRowHeight();return Math.min(1,(this.getViewportHeight()/_5)/this.getTotalRows())}
,isc.A.$453=function isc_GridRenderer__storeTargetRow(_1,_2){if(this.$454)return;if(this.isEmpty())return;var _3
if(_2>0){_3=_1+this.getViewportHeight()}else{_3=_1}
var _4=this.getEventRow(_3),_5=this.getTotalRows()-1,_6=_1;if(_4<0||_4>_5){this.$45y=_5;this.$45z=0;_6=this.getRowTop(_5)}else{this.$45y=_4;this.$45z=_1-this.getRowTop(this.$45y)+_2;if(Math.abs(this.$45z)>this.getViewportHeight()){this.logInfo("storeTargetRow: targetRow: "+_4+" with offset: "+this.$45z+", clearing","virtualScrolling");this.$45z=this.$45y=null}}
return _6}
,isc.A.$450=function isc_GridRenderer__scrollToTargetRow(_1){var _2=this.$45y,_3=this.$45z;var _4=this.getRowTop(_2)+_3;this.$454=true;this.$xk=null;this.scrollTo(null,_4,_1||"targetRow");this.$454=false;this.$451=null}
,isc.A.scrollTo=function isc_GridRenderer_scrollTo(_1,_2,_3,_4){if(isc.$dd)arguments.$de=this;if(this.$45x&&_2!=null&&_3!="nativeScroll"){var _5=this.getScrollTop(),_6=_2-_5;if(_6!=0){this.$453(_5,_6);_2=Math.min(_2,this.getRowTop(this.getTotalRows()-1))}}
this.invokeSuper(isc.GridRenderer,"scrollTo",_1,_2,_3,_4);if(this.isDirty()||this.$455)return;var _7=(this.$456()||this.$457());if(_7){if(!this.isFastScrolling()&&this.instantScrollTrackRedraw){this.redraw("scrolled")}else if(this.scrollRedrawDelay==0){this.markForRedraw("scrolled")}else{var _8=this;this.fireOnPause("scrollRedraw",function(){_8.markForRedraw("scrolled")},this.scrollRedrawDelay)}
this.$458=true}}
,isc.A.$456=function isc_GridRenderer__needRowRedraw(){if(this.showAllRows)return false;var _1=this.getVisibleRows(),_2=_1[0],_3=_1[1];var _4=this.getTotalRows();if(_3>_4-1)_3=_4-1;var _5=(_2<this.$6i||_3>this.$459);return _5}
,isc.A.$457=function isc_GridRenderer__needColumnRedraw(){if(this.shouldShowAllColumns())return false;var _1=this.getVisibleColumns(),_2=_1[0],_3=_1[1],_4=(_2<this.$6j||_3>this.$46a);return _4}
,isc.A.setOverflow=function isc_GridRenderer_setOverflow(_1){if(_1==isc.Canvas.VISIBLE){this.showAllRows=true}
return this.Super("setOverflow",arguments)}
,isc.A.getRowChunkNum=function isc_GridRenderer_getRowChunkNum(_1){return Math.round(_1/ this.$45u)}
,isc.A.getColChunkNum=function isc_GridRenderer_getColChunkNum(_1){return Math.round(_1/ this.$45v)}
,isc.A.getTableChunk=function isc_GridRenderer_getTableChunk(_1,_2){var _3=this.$46b;if(!_3)return;_1=_1||0;_2=_2||0;var _4=_3[_1];return _4?_4[_2]:null}
,isc.A.getTableChunkAt=function isc_GridRenderer_getTableChunkAt(_1,_2){var _3=this.getRowChunkNum(_1),_4=this.getColChunkNum(_2),_5=this.getTableChunk(_3,_4);if(_5!=null){this.$46c=_3*this.$45u;this.$6j=_4*this.$45v;return _5}}
,isc.A.$vn=function isc_GridRenderer__updateInnerHTML(_1,_2,_3,_4){if(this.cacheDOM){this.drawVisibleChunks()}else{this.$46d();this.$46e=true;this.invokeSuper(isc.GridRenderer,"$vn",_1,_2,_3,_4);delete this.$46e}}
,isc.A.$ug=function isc_GridRenderer__updateParentHTML(_1,_2,_3,_4){this.$46d();this.$46e=true;this.invokeSuper(isc.GridRenderer,"$ug",_1,_2,_3,_4);delete this.$46e}
,isc.A.drawVisibleChunks=function isc_GridRenderer_drawVisibleChunks(){var _1=this.getVisibleRows(),_2=this.getVisibleColumns(),_3=this.getRowChunkNum(_1[0]),_4=this.getColChunkNum(_2[0]),_5=this.getRowChunkNum(_1[1]),_6=this.getColChunkNum(_2[1]);for(var _7=_3;_7<_5;_7++){for(var _8=_4;_8<_6;_8++){if(this.getTableChunk(_7,_8)==null){this.logWarn("drawing chunk: "+[_7,_8]);this.renderTableChunk(_7,_8)}}}
var _9=this.getTableHTML()}
,isc.A.renderTableChunk=function isc_GridRenderer_renderTableChunk(_1,_2){var _3=_1*this.$45u,_4=_3+this.$45u,_5=_2*this.$45v,_6=_5+this.$45v;var _7=this.getTableHTML([_5,_6],_3,_4),_8=isc.Element.insertAdjacentHTML(this.getHandle(),"beforeEnd",_7,true);var _9=this.$46b=this.$46b||[],_10=_9[_1]=_9[_1]||[];_10[_2]=_8}
,isc.A.getDrawnRows=function isc_GridRenderer_getDrawnRows(){return this.getVisibleRows()}
,isc.A.startRowAnimation=function isc_GridRenderer_startRowAnimation(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10){this.finishRowAnimation();if(!this.isDrawn()||!this.isVisible()){if(_4!=null){var _11=_9?this.parentElement:this;_11.fireCallback(_4)}
return}
if(_1==null)_1=true;if(_2==null)_2=0;if(_3==null)_3=this.getTotalRows()-1;if(_2==_3){this.logWarn("startRowAnimation passed empty row range, aborting: "+[_2,_3]);return}
var _12=this.readyToRedraw("animating show / hide of rows",false);if(!_12){this.$46f=[_1,_2,_3,_4,_5,_6,_7,_8,_9];this.$46g=isc.Timer.setTimeout({target:this,methodName:"$46h"},0);return}
if((_3-_2)>this.maxAnimateSlideInRows)_8=false;this.$46i=_8;var _13=this.$46j(_1,_2,_3,_4,_9);this.animateRowHeight(this.$46k,(_1?_13:0),{target:this,methodName:"$46l"},_5,_6,_7,_8)}
,isc.A.$46h=function isc_GridRenderer__delayedStartRowAnimation(){if(this.$46f==null){this.logWarn("Unable to perform delayed row animation - bailing");return}
var _1=this.$46f,_2=_1[0],_3=_1[1],_4=_1[2],_5=_1[3],_6=_1[4],_7=_1[5],_8=_1[6],_9=_1[7],_10=_1[8];this.$46f=null;this.$46g=null;this.startRowAnimation(_2,_3,_4,_5,_6,_7,_8,_9,_10,true)}
,isc.A.$46j=function isc_GridRenderer__initializeShowHideRow(_1,_2,_3,_4,_5){var _6=0;if(this.$46k==_2&&this.$46m==_3){var _7=this.getTableElement(this.$46k,0),_8=this.$46n(_7);if(!_8){_6=(_3-_2)*this.cellHeight}else _6=_8.scrollHeight}else{this.$46k=_2;this.$46m=_3;if(!_1){var _9=this.$46o();for(var i=_2;i<_3;i++){_6+=_9[i]}
this.$46p=_6;this.redraw("initializing animated hide row")}else{this.$46p=1;this.redraw("initializing animated show row");var _7=this.getTableElement(this.$46k,0),_8=this.$46n(_7);if(!_8){_6=(_3-_2)*this.cellHeight}else _6=_8.scrollHeight}
if(this.isDirty())this.redraw("Initializing row animation requires second redraw")}
this.$46q={callback:_4,target:(_5?this.parentElement:this)};return _6}
,isc.A.finishRowAnimation=function isc_GridRenderer_finishRowAnimation(){if(this.$46k!=null){this.finishAnimateRowHeight()}else{if(this.$46g!=null){isc.Timer.clearTimeout(this.$46g);var _1=this.$46f,_2=_1[0],_3=_1[1],_4=_1[2],_5=_1[3],_6=_1[4],_7=_1[5];delete this.$46f;delete this.$46g;if(!this.readyToRedraw()){this.logWarn("Finish row animation called while Grid is not ready to redraw. "+"GridRenderer HTML will not be updated when callback fires.","animation");var _8=_7?this.parentElement:this;if(_5)_8.fireCallback(_5)}else{var _9=this.$46j(_2,_3,_4,_5,_7);this.setRowHeight(_3,(_2?_9:1));this.$46l()}}}}
,isc.A.$46l=function isc_GridRenderer__rowShowComplete(){var _1=this.$46q;delete this.$46q;delete this.$46k;delete this.$46m;delete this.$46p;if(_1&&_1.callback)_1.target.fireCallback(_1.callback)}
,isc.A.animateRowHeight=function isc_GridRenderer_animateRowHeight(_1,_2,_3,_4,_5,_6,_7,_8){if(!this.isDrawn()){if(_3){var _9=(_8?this.parentElement:this);_9.fireCallback(_3)}
return}
if(this.$46r!=null){this.logInfo("early finish of row animation, because new animation started","animation")
this.finishAnimateRowHeight()}
var _10=this.getRowSize(_1);if(_4!=null){var _11=(_2-_10);if(_11<0)_11=0-_11;_5=Math.round((_11/ _4)*1000);if(_5>this.animateRowsMaxTime)_5=this.animateRowsMaxTime}
this.$46s={$46t:_1,$46u:_10,$46v:_2,$7o:_3,$8a:_7,$46w:_8}
_6=(_6||this.$pr);if(this.logIsInfoEnabled("animation")){this.logInfo("starting row animation, duration: "+_5+", effect: "+_6,"animation")}
this.$46r=this.registerAnimation({target:this,method:this.$46x},_5,_6);if(this.overflow==isc.Canvas.AUTO||this.overflow==isc.Canvas.SCROLL)
this.$vt=true}
,isc.A.$46x=function isc_GridRenderer__fireRowAnimation(_1){var _2=this.$46s,_3=_2.$46t,_4=this.$7p(_2.$46u,_2.$46v,_1);if(isc.Browser.isSafari&&_2.$46u>_2.$46v)
this.$46y=true;this.setRowHeight(_3,_4,null,isc.emptyString,true,true,true);if(isc.Browser.isSafari)delete this.$46y;if(_2.$8a){var _5=this.$46n(this.getTableElement(_3,0));if(_5){var _6=_5.scrollHeight,_7=_5.offsetHeight;if(_6>_7)_5.scrollTop=_6-_7;else _5.scrollTop=0}}
if(_1==1){isc.Timer.setTimeout({target:this,methodName:"$46z"},0)}}
,isc.A.$46z=function isc_GridRenderer__rowAnimationComplete(){delete this.$vt;this.adjustOverflow("row animation complete");var _1=this.$46s;delete this.$46r;delete this.$46s;if(_1&&_1.$7o){var _2=_1.$46w?this.parentElement:this;_2.fireCallback(_1.$7o)}}
,isc.A.finishAnimateRowHeight=function isc_GridRenderer_finishAnimateRowHeight(){if(!this.$46r)return;this.cancelAnimation(this.$46r);this.$46x(1)}
,isc.A.$460=function isc_GridRenderer__getPrintChildren(){return this.$4p}
,isc.A.getTablePrintHTML=function isc_GridRenderer_getTablePrintHTML(_1,_2,_3,_4,_5){return this.getTableHTML(_1,_2,_3,_4,_5)}
,isc.A.draw=function isc_GridRenderer_draw(){if(isc.screenReader&&this.screenReader_suppressHandleFocus){this.clipHandleIsFocusHandle=!this.isEmpty()}
return this.Super("draw",arguments)}
,isc.A.handleKeyDown=function isc_GridRenderer_handleKeyDown(_1,_2){var _3=this.Super("handleKeyDown",arguments);if(_3!=false&&isc.screenReader&&this.screenReader_suppressHandleFocus){var _4=isc.EH.clickMaskUp(),_5=false;if(_4){var _6=isc.EH.clickMaskRegistry;for(var i=0;i<_6.length;i++){if(isc.EH.isHardMask(_6[i])){_5=true;break}}}
if(!_5){var _8=_1.keyName;if(_8=="Tab"){this.$l5(!isc.EH.shiftKeyDown())
return false}}}
return _3}
,isc.A.getTableHTML=function isc_GridRenderer_getTableHTML(_1,_2,_3,_4,_5,_6){if(isc.$dd)arguments.$de=this;var _7=isc.timeStamp();if(this.isEmpty()){this.$6i=this.$459=this.$6j=this.$46a=null;if(this.isPrinting){return this.grid.getPrintHeaders(_26,_27)+this.$45q()}
return this.$45q()}
if(this.isPrinting&&(!this.$461||_2==0)){var _8=this.$460();if(_8!=null&&_8.length>0){for(var i=0;i<_8.length;i++){var _10=_8[i];if(_10.$462!=null)continue;var _11={component:_10,colNum:_1,startRow:_2,endRow:_3,descreteCols:_4,asyncCallback:_5};var _12=_8[i].getPrintHTML(this.printProperties,_5==null?null:{target:this,methodName:"gotComponentPrintHTML",context:_11});if(_12!=null){_10.$462=_12}else{return null}}}}
var _13=(_2!=null&&_3!=null),_14=_2!=null?_2:0,_15=_3!=null?_3:this.getTotalRows();var _16=this.getDrawArea(),_17=this.grid,_18;if(_17){if(_17.$463){_18=_17.$463==null?0:isc.isAn.Array(_17.$463)?_17.$463[0]:_17.$463}else if(_17.data&&_17.data.getFirstUsedIndex&&_16[0]==0){_18=_17.data.getFirstUsedIndex()}
if(_18){var _19=_16[1]-_16[0],_20=_18+_19,_21=this.getTotalRows();if(_20>=_21){_18-=(_20-(_21-1))
_20=_21-1}
if(_18<0)_18=0;_16[0]=_18;_16[1]=_20}}
if(!_13){this.$6i=_16[0];this.$459=_16[1];if(this.$46k!=null){this.$459+=(this.$46m-this.$46k);var _21=this.getTotalRows();if(this.$459>=_21)this.$459=_21-1}
_2=this.$6i;_3=this.$459+1;var _22=(_2==0&&_3==this.getTotalRows());if(this.virtualScrolling){this.$45x=!_22&&!this.fixedRowHeights}
if(!this.$45x){delete this.$45y;delete this.$464;delete this.$451;delete this.$465}}else{var _23=_16[0],_24=_16[1]+1;if(this.$466){if(_23>_3||_24<_2){_2=_3}else{if(!this.$46i){_2=Math.max(_2,_23);_3=Math.min(_3,_24)}}}}
this.$6j=_16[2];this.$46a=_16[3];var _25=_1!=null&&isc.isAn.Array(_1),_26,_27;if(!_25)_4=false;if(_1!=null){if(_25){_26=_1[0];_27=_1[1]+1}else{_26=_1;_27=_1+1}}else{_26=this.$6j;_27=this.$46a+1}
var _28;if(_4)_28=_1;else{_28=[];for(var i=_26;i<_27;i++){_28[_28.length]=i}}
var _29=_28.length;var _30=(this.shouldShowAllColumns()||_1!=null);var _31=isc.StringBuffer.create(),_32=this.fields,_33=this.$45p;this.$467=_28[0]!=0?null:this.$45p[0];var _34,_35,_36,_37;if(!_30||this.leftSpace!=null||this.rightSpace!=null){_34=(this.leftSpace!=null)?this.leftSpace:0;_35=(this.rightSpace!=null)?this.rightSpace:0;if(!_30){_34+=this.$45p.slice(0,_26).sum();_35+=this.$45p.slice(_27,this.$45p.length).sum()}
_36=this.$45p.sum()
_37=(this.cacheDOM||(isc.Browser.isIE&&!isc.Browser.isIE8)?"margin":"padding")}
var _38=this.autoFit;var _39="";if(_1!=null){if(!_38&&this.fixedColumnWidths){_39=" WIDTH=100%"}}else if(this.isPrinting&&this.autoFit){_39=" WIDTH=100%"}else if((isc.Browser.isIE8Strict||isc.Browser.isMoz||isc.Browser.isSafari)&&!_38)
{var _40=this.$45p.slice(_26,_27).sum();_39=" WIDTH="+_40}
var _41=this.startSpace||0;if(_2!=_14){var _42=((_2-_14)*this.getAvgRowHeight());this.$468=_42;_41+=_42}else{this.$468=0}
var _43=true;var _44=(_15-_14)*this.getAvgRowHeight();if(isc.Browser.isIE){if(_44>1300000)_43=false}
if(!_13)this.$469=_43;if(_44>10000000){this.logWarn("This grid is showing "+(_15-_14).toLocalizedString()+" rows. Due to native rendering limitations, grids with this many rows"+" may not appear correctly on all browsers. Consider filtering the data"+" displayed to the user to reduce the total number of rows displayed at a time."+" This will improve usability as well as avoiding unpredictable behavior.")}
if(!this.cacheDOM&&!this.isPrinting){_31.append("<DIV style='width:1px;");if(_43){_31.append("height:",_41,"px;overflow:hidden;")}
if(_41==0)_31.append("display:none;");_31.append("' ");if(_13||this.isPrinting){_31.append(">")}else{_31.append(" ID="+this.getID()+"$47a>")}
_31.append(isc.Canvas.spacerHTML(1,_41),"</DIV>")}
if(!this.$461||_2==0){_31.append("<TABLE role='presentation' BORDER=0",_39,((!_13&&!this.isPrinting)?" ID="+this.getTableElementId():null),(this.tableStyle&&isc.Browser.isDOM?" CLASS='"+this.tableStyle+this.$r9:isc.$ah)," CELLSPACING=",this.cellSpacing," CELLPADDING=",this.cellPadding," STYLE='",(isc.Browser.isDOM&&!_38&&this.fixedColumnWidths?"table-layout:fixed;overflow:hidden;wrap:false;":""),(!_30?_37+(this.isRTL()?"-right: ":"-left:")+_34+"px;"+_37+(this.isRTL()?"-left:":"-right:")+_35+"px;":""),(this.cacheDOM&&this.$468>0?"margin-top:"+this.$468+"px;":""),(this.$45y!=null&&!(isc.Browser.isIE&&this.$ww)?"visibility:hidden;":""),"'>",(isc.Browser.isMoz?"<TBODY>":""));var _45=0,_46=0,_47=this.$47b();if(isc.Browser.isStrict&&(isc.Browser.isSafari||isc.Browser.isIE)){if((isc.Browser.isIE&&!isc.Browser.isIE8)||(isc.Browser.isSafari&&isc.Browser.safariVersion<530))
{_46=this.$47c()}
_45=(this.fixedRowHeights?0:this.cellPadding*2);_45+=(this.fixedRowHeights?isc.Element.$q3(_47):isc.Element.$rc(_47))}
this.$47d=_45;this.$47e=_46;if(!_38&&isc.Browser.isDOM){for(var i=0;i<_28.length;i++){_31.append("<COL WIDTH=",(_33[_28[i]]-_46),">")}}
_31.append("<TBODY>")}
var _48=this.cellHeight,_49=(this.wrapCells?"":"<NOBR>"),_50=(this.wrapCells?"":"</NOBR>");var _51=0;if(isc.Browser.isDOM){var _52=this.$47f(_48);var _53=[];_53[0]="<TD";_53[3]=" ALIGN=";_53[17]=this.fastCellUpdates?"' ":"' CLASS=";if(!_13&&!this.isPrinting&&this.getCellElementId)_53[19]=" ID=";_53[23]=">"+_49;_53[30]=_50+(_52?"</DIV></TD>":"</TD>");var _54=1,_55=2,_56=4,_57=5,_58=6,_59=7,_60=9,_61=10,_62=11,_63=18,_64=20,_65=21,_66=24;var _67="<TR",_68="</TR>",_69=">",_70=" HEIGHT=",_71=" VALIGN=";if(isc.Browser.isMobileWebkit)_67+=" onmousedown=\"return true;\"";var _72;if(isc.screenReader){_72=" tabIndex=-1"}
var _73=isc.Canvas.ariaEnabled();var _74=[],_75=0,_76=[];this.$47g(_28,_38,_46,_52);if(this.isPrinting&&(!this.$461||_2==0)){_31.append(this.grid.getPrintHeaders(_26,_27))}
for(var _77=_2;_77<_3;_77++){var _78=(!_13&&this.$46k==_77);var _79=this.getCellRecord(_77);var _80=_78||this.$47h(_77,_79);_31.append(_67);if(_72!=null){if(this.screenReader_suppressHandleFocus){if(this.getNativeFocusRow()==_77){_31.append(" tabIndex=",this.getTabIndex(),isc.Canvas.$wk,this.$uz(),isc.Canvas.$wl,this.$u0())}else{_31.append(_72)}}else{_31.append(_72)}}
if(!_13&&!this.isPrinting&&this.getRowElementId){_31.append(" ID=",this.getRowElementId(_77,_77-_2))}
if(_73&&this.getRowRole!=null){var _81=this.getRowRole(_77,_79);if(_81!=null)_31.append(" role='",_81,"'");var _82=this.getRowAriaState(_77,_79);if(_82)_31.append(isc.Canvas.getAriaStateAttributes(_82))}
_31.append(_69);var _83=_78?this.$46p:(this.getRowHeight!=null?this.getRowHeight(_79,_77):_48);var _84;if(_78){_84=true}else{_84=this.fixedRowHeights;if(_84&&this.shouldFixRowHeight!=null){_84=(this.shouldFixRowHeight(_79,_77)!=false)}}
if(_84){_53[_54]=_70;_53[_55]=_83-_45;_53[_61]=null}else{_53[_54]=null;_53[_55]=null;if(!_80&&_83==this.cellHeight&&!this.fixedRowHeights)
{_53[_61]=null}else{_53[_61]=this.$47i(_79,_77)}}
if(_52){_53[_65]=">"+this.$45f+this.$47j(_83,_79,_77,_78)}
var _85=_80?this.$47k(_79,_77,_26,_27):null;for(var i=0;i<_28.length;i++){_1=_28[i];if(this.useCellRecords)_79=this.getCellRecord(_77,_1);var _86=_32[_1],_87=_79;if(_87==null)_87=this.getCellRecord(_77,_1);if(_74[_1]>0){_86.$47l[_77]=_76[_1];_74[_1]--;if(_74[_1]==0){_75--;_76[_1]=null}
continue}
_53[_56]=this.getCellAlign(_79,_86,_77,_1);var _88=this.getCellVAlign(_79,_86,_77,_1);if(_88!=null){_53[_57]=_71
_53[_58]=_88}
if(_85!=null&&(_1==_85[0])){_51++;_53[_59]=this.$47m(_85[1]-_85[0]);if(_52){_53[_65+1]=this.$r9}}else{_53[_59]=_86.$47n;if(this.getRowSpan){var _89=this.getRowSpan(_79,_77,_1);if(_89>1){var _90=" ROWSPAN="+_89;if(_53[_56]!=null)
_53[_56]+=_90;else
_53[_56]=_90;_74[_1]=_89-1;_75++;_76[_1]=_77;if(_86.$47l==null)_86.$47l={};_86.$47l[_77]=_77}}
if(_73&&this.getCellRole!=null){var _91=this.getCellRole(_77,_1,_79);if(_91!=null){var _92=this.getCellAriaState(_77,_1,_79);_53[_60]=" role='"+_91+(_92?isc.Canvas.getAriaStateAttributes(_92):"")}else{_53[_60]=null}}
if(_52){_53[_65+1]=_86.$47o}else{_53[_65+1]=null}}
var _93=this.getCellStyle(_79,_77,_1),_94=(this.getCellCSSText?this.getCellCSSText(_79,_77,_1):null);if(_78){var _95="padding:0px;border:0px;";if(_94)_94+=";"+_95
else _94=_95}
if(!this.fastCellUpdates){_53[_62]=_94
_53[_63]=_93}else{var _96=this.$47p(_93);_53[_62]=_96;_53[_62+1]=_94}
if(_78){this.$466=true;var _97=this.getTableHTML(null,this.$46k,this.$46m);delete this.$466;if(!_52){_53[_66]=isc.SB.concat(this.$45f,this.$47j(_83,_79,_77,_78),this.$r9,this.$r8,_97,"</DIV>")}else{_53[_66]=_97}}else
_53[_66]=this.$47q(_79,_77,_1);if(!_13&&this.getCellElementId){_53[_64]=this.getCellElementId(_77,_77-_2,_1,_1-_26)}
_31.append(_53);if(!_13&&_87!=null&&_87.$4p!=null){if(_87.$4p[0]&&_87.$4p[0].rowNum==null)
{this.updateEmbeddedComponentCoords(_87.$4p,_87,_77,_1)}}
if(_80&&(_1==_85[0])){i+=_85[1]-_85[0]}}
_31.append(_68);if(this.isPrinting&&_79.$4p!=null){var _98=_79.$4p;for(var _99=0;_99<_98.length;_99++){var _100=_98[_99];if(_100.$47r==null&&_100.$462!=null){_31.append(_67,_69,'<td colspan="',_29,'">',_100.$462,"</td>",_68);delete _100.$462}}}
if(_78){_77=this.$46m-1}}}
if(!this.$461||_3==this.getTotalRows())_31.append("</TABLE>");var _101=_15-_3,_102=(!_13&&this.$45x);var _103=this.cacheDOM?0:(this.endSpace||0);this.$47s=0;if(!this.showAllRows&&(_101!=0||_102)){var _104=_101*this.getAvgRowHeight();if(_102&&_101==0){var _105=this.getViewportHeight();if(_104<_105){_104=_105}}
this.$47s=_104;_103+=this.$47s}
if(!this.cacheDOM&&!this.isPrinting){_31.append("<DIV style='width:1px;");if(_43){_31.append("height:",_103,"px;overflow:hidden;")}
if(_103==0)_31.append("display:none;");_31.append("' ");if(_13||this.isPrinting){_31.append(">")}else{_31.append(" ID="+this.getID()+"$47t>")}
_31.append(isc.Canvas.spacerHTML(1,_103),"</DIV>")}
if(this.logIsDebugEnabled("gridHTML")){var _106=(isc.timeStamp()-_7),_107=(_29*(_3-_2)),_108=(_106/ _107),_109=(1000/_108);if(_108.toFixed!=null)_108=_108.toFixed(2);if(_109.toFixed!=null)_109=_109.toFixed(2);this.logDebug("getTableHTML: columns "+(_4?_28:_26+"->"+(_27-1))+", rows "+_2+"->"+(_3-1)+", time: "+_106+"ms ("+_107+" cells at "+_108+"ms per cell, "+_109+" cells per second), "+"spacerHeights: ["+[_41,_103]+"], "+"left/right pad: ["+[_34,_35]+"], "+_51+" single cell rows","gridHTML")}
var _110=_31.release();if(_6){if(_5!=null){this.fireCallback(_5,"HTML,callback",[_110,_5])}
return null}
return _110}
,isc.A.$47u=function isc_GridRenderer__putNativeFocusInRow(_1,_2){var _3=this.screenReader_suppressHandleFocus;if(_3)this.$0l(false);this.$47v=_1;if(_3)this.$0l(true);if(_2||!this.hasFocus)return;var _4=this.getFocusHandle();if(_4)_4.focus();if(!_3)isc.EH.$l4=null;_4.focus();if(!_3)isc.EH.$l4=this}
,isc.A.getFocusHandle=function isc_GridRenderer_getFocusHandle(){if(!isc.screenReader)return this.Super("getFocusHandle",arguments);if(isc.screenReader){var _1=this.getNativeFocusRow();var _2=this.getTableElement(_1);if(_2!=null)return _2;return this.Super("getFocusHandle",arguments)}}
,isc.A.gotComponentPrintHTML=function isc_GridRenderer_gotComponentPrintHTML(_1,_2){var _3=_2.context,_4=_3.component;if(_3.asyncCallback==null){return}
_4.$462=_1;return this.getTableHTML(_3.colNum,_3.startRow,_3.endRow,_3.discreteCols,_3.asyncCallback,true)}
,isc.A.$47p=function isc_GridRenderer__getEscapedStyleText(_1){if(this.$45e[_1]!=null)return this.$45e[_1];var _2=isc.Element.getStyleText(_1,true);this.$45e[_1]=_2.replaceAll("'",'"');return this.$45e[_1]}
,isc.A.getCellVAlign=function isc_GridRenderer_getCellVAlign(_1,_2,_3,_4){return null}
,isc.A.getCellAlign=function isc_GridRenderer_getCellAlign(_1,_2,_3,_4){return _2.cellAlign||_2.align}
,isc.A.$47k=function isc_GridRenderer__getSingleCellSpan(_1,_2,_3,_4){return[_3,_4]}
,isc.A.$47m=function isc_GridRenderer__getTDSpanHTML(_1){if(!isc.GridRenderer.$47w){isc.GridRenderer.$47w={$47x:{},$47y:{}}}
var _2=this.fixedRowHeights?isc.GridRenderer.$47w.$47x:isc.GridRenderer.$47w.$47y;if(_2[_1])return _2[_1];else{return _2[_1]=" COLSPAN="+_1+" STYLE='"+(this.fixedRowHeights?"padding-top:0px;padding-bottom:0px;":"")}}
,isc.A.$47b=function isc_GridRenderer__getFirstRecordStyle(){var _1=this.grid,_2=0;if(_1){if(_1.$463){_2=_1.$463==null?0:isc.isAn.Array(_1.$463)?_1.$463[0]:_1.$463}else if(_1.data&&_1.data.getFirstUsedIndex){_2=_1.data.getFirstUsedIndex()}}
return(this.getBaseStyle!=null?this.getBaseStyle(this.getCellRecord(_2,0),0,0):this.baseStyle)}
,isc.A.$47g=function isc_GridRenderer__cacheColumnHTML(_1,_2,_3,_4){var _5=this.fields,_6=this.$45p;for(var i=0;i<_1.length;i++){var _8=_1[i],_9=_5[_8];_9.$47l=null;if(_2){_9.$47n=(isc.Browser.isIE&&!isc.Browser.isIEStrict)?" STYLE='":" STYLE='OVERFLOW:hidden;";_9.$47o=this.$r9}else{var _10=isc.Browser.isIE8Strict?" STYLE='overflow:hidden;":" STYLE='";_9.$47n=(isc.Browser.isIE?" WIDTH="+(_6[_8]-_3)+_10:" STYLE='"+this.$47z(_8));if(_4){_9.$47o=this.$470(_8)+this.$r9}}
if(!this.fixedRowHeights){_9.$47n+=this.$47i()}
if(this.fixedRowHeights)_9.$47n+="padding-top:0px;padding-bottom:0px;"}}
,isc.A.$47f=function isc_GridRenderer__writeDiv(_1){return(isc.Browser.isSafari||(isc.Browser.isOpera&&!this.autoFit&&(this.fixedColumnWidths||this.fixedRowHeights))||(isc.Browser.isMoz&&isc.Browser.geckoVersion>=20040113&&this.fixedColumnWidths&&!this.autoFit)||(this.fixedRowHeights&&(this.wrapCells||this.enforceVClipping)&&(isc.Browser.isMoz||(isc.Browser.isStrict&&isc.Browser.isIE))))}
,isc.A.$47h=function isc_GridRenderer__drawRecordAsSingleCell(_1,_2){return(_2&&(_2[this.singleCellValueProperty]!=null||_2[this.isSeparatorProperty]||(Array.isLoading(_2)&&!(isc.Browser.isSafari&&(_1==0||_1==this.$6i)))))}
,isc.A.$47j=function isc_GridRenderer__getCellDivCSSHeight(_1,_2,_3,_4){var _5=_4||(this.fixedRowHeights&&(this.enforceVClipping||this.wrapCells)&&(this.shouldFixRowHeight==null||this.shouldFixRowHeight(_2,_3)!=false));if(_5){var _6=_1-2*this.cellSpacing-
(_4?0:2);if(_3==this.$471){for(var i=0;i<this.$6l.getItems().length;i++){_6=Math.max(this.$6l.getItems()[i].getHeight(),this.$6l.getItems()[i].iconHeight)}}
if(_6<1)_6=1;return(isc.Browser.isMoz||isc.Browser.isSafari?"MAX-HEIGHT:":"HEIGHT:")+_6+"px;"}
return isc.$ah}
,isc.A.$470=function isc_GridRenderer__getFieldDivWidthHTML(_1){if(!this.fixedColumnWidths||this.autoFit)return isc.emptyString;return"WIDTH:"+this.getInnerColumnWidth(_1)+"px;"}
,isc.A.$47i=function isc_GridRenderer__getMinHeightCSSText(_1,_2){var _3=(_2!=null?this.getRowHeight(_1,_2):this.cellHeight),_4=isc.Browser.isIE,_5=isc.Browser.isStrict;if(_5&&(_4||isc.Browser.isSafari))_3-=this.$47d;if(_4&&!_5&&!(this.autoFit||!this.fixedColumnWidths)){return"MIN-HEIGHT:"+_3+"px;"}
return"HEIGHT:"+_3+"px;"}
,isc.A.$47z=function isc_GridRenderer__getCSSTextForColWidth(_1){if(isc.Browser.isIE||this.autoFit)return isc.$ah;if(this.$472==null){this.$472=[];for(var i=0;i<this.$45p.length;i++){var _3=this.$45p[i];this.$472[i]="WIDTH:"+_3+(this.fixedColumnWidths?"px;OVERFLOW:hidden;":"px;")}}
return this.$472[_1]}
,isc.A.getCellRecord=function isc_GridRenderer_getCellRecord(_1,_2){return null}
,isc.A.findRowNum=function isc_GridRenderer_findRowNum(_1){return-1}
,isc.A.findColNum=function isc_GridRenderer_findColNum(_1){return-1}
,isc.A.$47q=function isc_GridRenderer__getCellValue(_1,_2,_3){var _4=this.getCellValue(_1,_2,_3,this);if(!this.isPrinting&&this.$473(_1)){var _5=this.$474(_1);if(_5.allWithin){if(_5.extraHeight&&(_5.extraHeight>this.cellHeight)){_4=[_4,this.$45g,isc.Canvas.spacerHTML(1,_5.extraHeight-this.cellHeight),this.$45h].join(isc.emptyString)}}else if(_5.extraHeight&&_5.extraHeight>0){_4=[_4,this.$45g,isc.Canvas.spacerHTML(1,_5.extraHeight),this.$45h].join(isc.emptyString)}}else if(_1&&_1.$4p!=null){var _6=_1.$4p||[];for(var i=0;i<_6.length;i++){var _8=_1.$4p[i];if(_8==null)continue;if(_8.$47r!=_3)continue;var _9=(_8.embeddedPosition==this.$45i);var _10=_8.$462;if(_10!=null){_4+=_9?this.$45g+_10+this.$45h:_10;delete _8.$462}}}
return _4}
,isc.A.$473=function isc_GridRenderer__writeEmbeddedComponentSpacer(_1){return(_1&&_1.$4p)!=null}
,isc.A.getCellValue=function isc_GridRenderer_getCellValue(_1,_2,_3){return this.emptyCellValue}
,isc.A.getTotalRows=function isc_GridRenderer_getTotalRows(){return this.totalRows}
,isc.A.setColumnWidth=function isc_GridRenderer_setColumnWidth(_1,_2){this.fields[_1].width=this.$45p[_1]=_2;this.$472=null;this.markForRedraw("setColumnWidth")}
,isc.A.setColumnWidths=function isc_GridRenderer_setColumnWidths(_1){var _2=this.$45p;this.$45p=_1.duplicate();this.$472=null;if(_2!=null&&_1!=null&&_2.length==_1.length){if(_2==_1)return;var _3=false;for(var i=0;i<_2.length;i++){if(_2[i]!=_1[i])_3=true}
if(!_3)return;if(!this.fixedColumnWidths&&!this.wrapCells&&this.isDrawn()&&_1.length==1){var _5=this.$467||_2[0],_6=_1[0],_7=this.getColumnSize(0);if((_5==_6)||(_7>_5&&_7>=_6)){return}}}
this.markForRedraw("setColumnWidths")}
,isc.A.shouldRedrawOnResize=function isc_GridRenderer_shouldRedrawOnResize(_1,_2,_3){if(this.redrawOnResize!=null)return this.redrawOnResize;if(isc.isA.ListGrid(this.parentElement)&&isc.isA.Layout(this.parentElement.parentElement))
{var _4=this.parentElement.parentElement.getMembers();if(_4&&_4.map("isAnimating").or())return false}
if(this.$456()||this.$457())return true;if(this.isEmpty())return true;return false}
);isc.evalBoundary;isc.B.push(isc.A.getRowHeight=function isc_GridRenderer_getRowHeight(_1,_2){var _3=this.updateHeightForEmbeddedComponents(_1,_2,this.cellHeight);return _3}
,isc.A.updateHeightForEmbeddedComponents=function isc_GridRenderer_updateHeightForEmbeddedComponents(_1,_2,_3){if(_1&&_1.$4p){var _4=this.$474(_1,_2);if(_4.allWithin&&_4.extraHeight>0){_3=Math.max(_3,_4.extraHeight)}else{_3+=_4.extraHeight}}
return _3}
,isc.A.$474=function isc_GridRenderer__getExtraEmbeddedComponentHeight(_1,_2){var _3=_1.$4p||[],_4=0,_5=true,_6=this.isPrinting;for(var i=0;i<_3.length;i++){var _8=_1.$4p[i];if(_6)continue;if(_2!=null)_8.$475=_2;var _9=(_8.embeddedPosition==this.$45i);if(!_9)_5=false;var _10=_8.getVisibleHeight();var _11=(_9?(_10>this.cellHeight?_10:0):_10);if(_8._percent_height!=null){_8.height=_8._percent_height;_11=this.cellHeight}
var _12=_8.specifiedHeight;if(_9&&_12&&isc.isA.String(_12)&&_12.contains("%"))
_11=0;if(_11>_4){_4=_11}}
return{allWithin:_5,extraHeight:_4}}
,isc.A.getCellStartRow=function isc_GridRenderer_getCellStartRow(_1,_2){var _3=this.fields[_2].$47l;if(_3==null||_3[_1]==null)return _1;return _3[_1]}
,isc.A.getCellRowSpan=function isc_GridRenderer_getCellRowSpan(_1,_2){var _3=this.fields[_2].$47l;var _4=this.getCellStartRow(_1,_2);if(_4==_1)return 1;var _5=_1+1,_6=_1-_4+1;while(_5<=this.$459&&_3[_5]==_4)
{_5++;_6++}
return _6}
,isc.A.addEmbeddedComponent=function isc_GridRenderer_addEmbeddedComponent(_1,_2,_3,_4,_5){if(_5==null)_5=this.$45j;var _6=((_5==this.$45j)||!this.fixedRowHeights);if(!isc.isA.Canvas(_1)){_1.autoDraw=false;var _7=isc.ClassFactory.getClass(_1._constructor);if(_7==null)_7=isc.Canvas;_1=_7.create(_1)}
var _8=false;if(this.$4p&&this.$4p.contains(_1)){if(_2.$4p&&_2.$4p.contains(_1)&&_1.embeddedPosition==_5&&_1.$475==_3&&_1.$47r==_4)
{return}
if(_5==_1.embeddedPosition&&!_6){_8=!this.isDirty()}
this.removeEmbeddedComponent(_1.embeddedRecord,_1,true)}else if(!_6){_8=!this.isDirty()}
if(!_2.$4p)_2.$4p=[];_2.$4p.add(_1);if(this.$4p==null)this.$4p=[];this.$4p.add(_1);_1.embeddedPosition=_5;_1.embeddedRecord=_2;_1.$475=_3;_1.$47r=_4;_1.$476=this.getID();_1.percentBox="custom";if(_1.parentElement!=this){var _9=this.$vt;this.$vt=true;_1.hide();this.addChild(_1);if(_9==null)delete this.$vt}
this.observe(_1,"resized","observer.$477(observed, deltaX, deltaY)");_1.$478=_1._redrawWithParent;_1._redrawWithParent=false;_1.$479=_1.bubbleMouseEvents;if(!_1.bubbleMouseEvents){_1.bubbleMouseEvents=["mouseDown","mouseUp","click","doubleClick","contextClick"]}
if(_8&&(_3==-1||_4==-1)){_8=false}
if(_8){this.placeEmbeddedComponent(_1)}else{this.markForRedraw("added embedded component")}
return _1}
,isc.A.$477=function isc_GridRenderer__handleEmbeddedComponentResize(_1,_2,_3){var _4=_1.embeddedPosition;if(_4!=this.$45i){if(_3!=null&&_3!=0)this.markForRedraw('embedded component resized')}else{this.placeEmbeddedComponent(_1)}}
,isc.A.updateEmbeddedComponentCoords=function isc_GridRenderer_updateEmbeddedComponentCoords(_1,_2,_3,_4){_1.setProperty("$475",_3)}
,isc.A.placeEmbeddedComponent=function isc_GridRenderer_placeEmbeddedComponent(_1){var _2=_1.$475;if(_2==null||_2<this.$6i||_2>this.$459){if(_1.isDrawn())_1.clear();return}
var _3=_1.embeddedRecord,_4=_1.embeddedPosition,_5=_1.$47r,_6=this.getRowTop(_2),_7=_5!=null?this.getColumnLeft(_5):0,_8=(_5!=null&&_5>=0)?this.getColumnWidth(_5):Math.min(this.getInnerWidth()+this.getScrollLeft(),this.$45p.sum());if(_4==this.$45i){var _9=this.getEmbeddedComponentSnapTo(_1,_3,_2,_5),_10=_1.snapEdge||_9;var _11=this.getRowSize(_2),_12=_1._percent_width,_13=_1._percent_height,_14,_15;var _16=0,_17=_3.$4p;for(var i=0;i<_17.length;i++){var _19=_3.$4p[i];var _20=(_19.embeddedPosition==this.$45i);if(_20)continue;var _21=_19.getVisibleHeight();if(_21>_16){_16=_21}}
_11-=_16;if(_1.snapOffsetLeft)_8-=_1.snapOffsetLeft;if(isc.isA.String(_12)&&_12.endsWith("%")){_14=Math.round((parseInt(_12)*_8)/100)}
if(isc.isA.String(_13)&&_13.endsWith("%")){_15=Math.round((parseInt(_13)*_11)/100)}
var _22=_15!=null?_15:_1.getHeight(),_23=_14!=null?_14:_1.getWidth();if(_15||_14){_1.resizeTo(_14,_15);_1._percent_width=_12;_1._percent_height=_13}
isc.Canvas.snapToEdge([_7,_6,_8,_11],_9,_1,_10)}else{_1.moveTo(_7,_6);_1.setWidth(_8)}
var _24=this.isDrawn();if(_24&&!_1.isDrawn())_1.draw();var _25=this.isDirty(),_26=this.getRowHeight(_3,_2),_27=!this.isDirty()||_25;if(_27&&(_26!=this.getRowSize(_2))){this.setRowHeight(_2,_26,_3);this.refreshRow(_2)}
if(_24){if(_4!=this.$45i){var _28=this.getDrawnRowHeight(_2)-_1.getVisibleHeight()-1;_1.moveTo(null,this.getRowTop(_2)+_28)}
if(!_1.isVisible()){if(this.shouldAnimateEmbeddedComponent(_1)){_1.animateShow()}else{_1.show()}}}
this.updateEmbeddedComponentZIndex(_1)}
,isc.A.getEmbeddedComponentSnapTo=function isc_GridRenderer_getEmbeddedComponentSnapTo(_1,_2,_3,_4){if(_1.snapTo!=null)return _1.snapTo;if(_4==null){return"TL"}
var _5=this.getCellAlign(_2,this.fields[_4],_3,_4)||"center",_6=this.getCellVAlign(_2,this.fields[_4],_3,_4)||"center";var _7=this.alignSnapToMap[_5][_6];return _7}
,isc.A.shouldAnimateEmbeddedComponent=function isc_GridRenderer_shouldAnimateEmbeddedComponent(_1){return false}
,isc.A.updateEmbeddedComponentZIndex=function isc_GridRenderer_updateEmbeddedComponentZIndex(_1){}
,isc.A.getEmbeddedComponent=function isc_GridRenderer_getEmbeddedComponent(_1,_2){if(isc.isA.Number(_1))_1=this.getCellRecord(_1,0);var _3=_1.$4p;if(_3==null)return;var _4=null;if(isc.isA.Number(_2))
_4=_3.find({$47r:_2,$476:this.getID()});return _4}
,isc.A.removeEmbeddedComponent=function isc_GridRenderer_removeEmbeddedComponent(_1,_2,_3){if(isc.isA.Number(_1))_1=this.getCellRecord(_1,0);var _4=_1.$4p;if(_4==null)return;if(isc.isA.Number(_2))
_2=_4.find({$47r:_2,$476:this.getID()});if(!_2)
_2=_4.find({$476:this.getID()});if(!_4.contains(_2))return;if(this.isObserving(_2,"resized")){this.ignore(_2,"resized")}
_1.$4p.remove(_2);if(_1.$4p.length==0)_1.$4p=null;if(this.$4p)this.$4p.remove(_2);_2._redrawWithParent=_2.$478;_2.$478=null;_2.bubbleMouseEvents=_2.$479;var _5=_2.embeddedPosition==this.$45j;_2.embeddedPosition=null;_2.$475=null;_2.$47r=null;_2.$476=null;if(_3){_2.hide();return}
if(_2.destroyOnUnEmbed)_2.destroy();else{this.removeChild(_2)}
if(_5){this.markForRedraw("removed embedded component")}}
,isc.A.$48a=function isc_GridRenderer__resetEmbeddedComponents(){var _1=this.$4p;if(_1==null)return;_1.setProperty("$475",null)}
,isc.A.$48b=function isc_GridRenderer__placeEmbeddedComponents(){var _1=this.$4p;if(_1==null)return;_1.sortByProperty("$475",true);for(var i=0;i<_1.length;i++){this.placeEmbeddedComponent(_1[i])}}
,isc.A.getTableZIndex=function isc_GridRenderer_getTableZIndex(){return 1000}
,isc.A.getCellStyle=function isc_GridRenderer_getCellStyle(_1,_2,_3){if(_1&&_1[this.recordCustomStyleProperty]!=null){return _1[this.recordCustomStyleProperty]}
var _4=this.getCellStyleIndex(_1,_2,_3);return this.getCellStyleName(_4,_1,_2,_3)}
,isc.A.getCellStyleName=function isc_GridRenderer_getCellStyleName(_1,_2,_3,_4){var _5=isc.GridRenderer.standardStyleSuffixes;if(this.getBaseStyle){var _6=this.getBaseStyle(_2,_3,_4);if(_6!==this.baseStyle){if(_1==0)return _6;return _6+_5[_1]}}
if(!this.$48c){this.$48c=[];for(var i=0;i<_5.length;i++){this.$48c[i]=this.baseStyle+_5[i]}}
return this.$48c[_1]}
,isc.A.getCellStyleIndex=function isc_GridRenderer_getCellStyleIndex(_1,_2,_3){var _4=0;var _5=true;if(this.grid!=null){var _6=this.grid.getField(this.grid.getFieldNumFromLocal(_3,this));_5=!_6?true:_6.showAlternateStyle!=false}
if(this.alternateRowStyles&&_5){var _7=(Math.floor(_2/ this.alternateRowFrequency)%2==1);if(_7)_4+=8}
if(!this.cellIsEnabled(_2,_3)){_4+=4}else{if(this.shouldShowRollOver(_2,_3)&&!this.isPrinting&&_2==this.lastOverRow&&(!this.useCellRollOvers||_3==this.lastOverCol))
{_4+=1}
if(this.showSelectedStyle&&this.selectionEnabled()){var _8=(this.canSelectCells?this.selection.cellIsSelected(_2,_3):this.selection.isSelected(_1));if(_8)_4+=2}}
return _4}
,isc.A.cellIsEnabled=function isc_GridRenderer_cellIsEnabled(_1,_2){return true}
,isc.A.getTableElementId=function isc_GridRenderer_getTableElementId(){return this.getCanvasName()+"table"}
,isc.A.getDOMTable=function isc_GridRenderer_getDOMTable(_1,_2){if(this.cacheDOM)return this.getTableChunkAt(_1,_2);if((_1!=null&&(_1-this.$6i<0||_1>this.$459))||(_2!=null&&(_2-this.$6j<0||_2>this.$46a)))
return null;var _3=this.$48d;if(_3==null){var _4=this.getTableElementId();var _3=isc.Element.get(_4);if(_3==null)return null;if(this.$46e){this.logInfo("getTableElement() called while updating table HTML. "+"This call may be invalid as the table is being rewritten in the DOM. "+"Suppressing caching of the current element.","redrawing");return _3}}
return this.$48d=_3}
,isc.A.getTableElement=function isc_GridRenderer_getTableElement(_1,_2){var _3=this.getDOMTable(_1,_2);if(_1==null)return _3;if(!_3)return null;var _4=_1-(this.$6i>0?this.$6i:0);if(_4<0){return null}
var _5;if(this.$48e!=null)_5=this.$48e[_4];if(_5==null)_5=_3.rows[_4];if(_5==null)return null;if(!this.$46e){if(this.$48e==null)this.$48e=[];this.$48e[_4]=_5}
if(_2==null)return _5;var _6=_2-this.$6j;if(_6<0){return null}
if(this.getRowSpan){var _7=this.getCellStartRow(_1,_6);if(_7!=_4){_4=_7;_5=this.getTableElement(_7)}
if(_5.cells.length<(this.$46a-this.$6j+1)){var _8=0;for(var i=0;i<_6;i++){if(this.fields[i].$47l!=null&&this.fields[i].$47l[_4]!=null&&this.fields[i].$47l[_4]!=_4)_8++}
_6-=_8}}
return _5.cells[_6]}
,isc.A.$48f=function isc_GridRenderer__updateCellStyle(_1,_2,_3,_4,_5){if(_4==null)_4=this.getTableElement(_2,_3);if(_4==null)return;if(_1==null)_1=this.getCellRecord(_2,_3);if(_5==null)_5=this.getCellStyle(_1,_2,_3);if(this.fastCellUpdates){_4.style.cssText=this.$48g(_1,_2,_3,_5)}else{if(_4.className!=_5)_4.className=_5;if(this.getCellCSSText){_4.style.cssText=this.$48g(_1,_2,_3,_5)}}
if(this.shouldRefreshCellHTML(_1,_2,_3)){this.refreshCellValue(_2,_3)}
if(!this.isDrawn())return;var _6=this.fixedRowHeights&&(this.shouldFixRowHeight==null||this.shouldFixRowHeight(_1,_2)!=false),_7=(this.getRowHeight!=null?this.getRowHeight(_1,_2):this.cellHeight);this.setRowHeight(_2,_7,_1,_5,_6)}
,isc.A.$46n=function isc_GridRenderer__getCellClipDiv(_1){if(_1==null)return null;var _2=_1.childNodes[0];if(!_2)return null;if(_2.tagName==this.$45k)_2=_2.childNodes[0];if(_2&&(_2.cellClipDiv||(_2.getAttribute&&_2.getAttribute(this.$45l))))
{return _2}
return null}
,isc.A.setRowHeight=function isc_GridRenderer_setRowHeight(_1,_2,_3,_4,_5,_6){var _7=this.$6j,_8=this.$46a;if(_5==null){if(_3==null)_3=this.getCellRecord(_1,_7);_5=this.fixedRowHeights&&(this.shouldFixRowHeight==null||this.shouldFixRowHeight(_3,_1)!=false)}
var _9=this.getTableElement(_1,_7),_10=_9?parseInt(_9.height):null,_11;if(!isc.isA.Number(_10))_10=null;if((isc.Browser.isSafari||isc.Browser.isIE)&&isc.Browser.isStrict){if(_3==null)_3=this.getCellRecord(_1,_7);var _12=_4;if(_12==null)_12=this.getCellStyle(_3,_1,_7)
_2-=this.fixedRowHeights?isc.Element.$q3(_12):isc.Element.$rc(_12);if(!this.fixedRowHeights)_2-=(this.cellPadding*2)}
if((!_5&&_10!=null)||(_10!=_2&&!(_10==null&&_2==isc.emptyString)))
{_11=true}
if(!_11)return;var _13=isc.isA.Number(_2);if(_13&&_2<=0)_2=_5?0:1;var _14=this.getTableElement(_1);if(_2==0&&_5){_14.style.display="none"}else{_14.style.display=isc.emptyString;for(var i=_7;i<=_8;i++){var _16=this.getTableElement(_1,i);if(_16){var _17=(!isc.Browser.isIE||isc.Browser.isStrict)?this.$s6:this.$45m;if(_5){_16.height=_2;_16.style[_17]=isc.emptyString}else{_16.height=isc.emptyString;_16.style[_17]=_2}
var _18=this.$46n(_16),_19=(_5?(_13?_2+isc.px:_2):isc.emptyString);if(_18){if(isc.Browser.isMoz||isc.Browser.isSafari)
_18.style.maxHeight=_19;else
_18.style.height=_19}}}}
if(isc.Browser.isSafari&&this.$46y){var _20=this.getTableElement(_1);if(_20!=null){_20.innerHTML=_20.innerHTML}}
this.$46d();if(_6){this.adjustOverflow("cell height changed")}else{this.$yg("cell height changed")}}
,isc.A.$48g=function isc_GridRenderer__getCompleteCellCSSText(_1,_2,_3,_4){var _5=null;if(this.fixedRowHeights)_5=this.$45n;else{_5=this.$47i(_1,_2)}
if(isc.Browser.isIE8Strict){if(_5==null)_5=this.$45o;else _5+=this.$45o}
if(isc.Browser.isMoz||isc.Browser.isSafari){if(_5==null)_5=this.$47z(_3);else _5+=this.$47z(_3)}
if(this.fastCellUpdates){if(_4==null)_4=this.getCellStyle(_1,_2,_3);var _6=isc.Element.getStyleText(_4,true);if(_6==null&&isc.Page.$48h){this.logInfo("fastCellUpdates set to true but this page loads styles from a "+"remote stylesheet. This is unsupported - disabling fastCellUpdates.");this.fastCellUpdates=false;this.redraw()}
if(_5!=null)_5+=_6;else _5=_6}
if(this.getCellCSSText){var _7=this.getCellCSSText(_1,_2,_3)
if(_7!=null){if(!_7.endsWith(this.$px)){_7+=this.$px}
if(_5!=null)_5+=_7
else _5=_7}}
return _5}
,isc.A.shouldRefreshCellHTML=function isc_GridRenderer_shouldRefreshCellHTML(_1,_2,_3){return this.showHiliteInCells}
,isc.A.$48i=function isc_GridRenderer__readyToRefreshCell(_1,_2){if((isc.EH.$mk||isc.EH.$md)&&isc.EH.lastEvent.target==this){var _3=this.getEventRow();if(_3!=_1)return true;if(_2!=null){var _4=this.getEventColumn();if(_2!=_4)return true}
return false}
return true}
,isc.A.refreshCellValue=function isc_GridRenderer_refreshCellValue(_1,_2){var _3=this.getTableElement(_1,_2);if(!_3)return;if(!this.$48i(_1,_2)){this.delayCall("refreshCellValue",[_1,_2]);return}
var _4=this.getCellRecord(_1,_2),_5=this.fields[_2];if(!_5){this.logDebug("refreshCell called for invalid field "+_2);return}
var _6="";var _7=this.getCellStyle(_4,_1,_2),_8=this.$47f(_9);if(_8){_6+=this.$45f;var _9=(this.getRowHeight!=null?this.getRowHeight(_4,_1):this.cellHeight);_6+=this.$47j(_9,_4,_1);var _10=this.$47h(_1,_4);if(!_10){_6+=this.$470(_2)}
_6+="'>"}
if(!this.wrapCells)_6+="<NOBR>";_6+=this.$47q(_4,_1,_2);if(!this.wrapCells)_6+="</NOBR>";if(_8)_6+="</DIV>";_3.innerHTML=_6}
,isc.A.setCellStyle=function isc_GridRenderer_setCellStyle(_1,_2,_3){return this.setRowStyle(_1,_3,_2)}
,isc.A.setRowStyle=function isc_GridRenderer_setRowStyle(_1,_2,_3){if(isc.$dd)arguments.$de=this;if(_1==null||_1<0){this.logWarn("setRowStyle: bad rowNum: "+_1);return false}
var _4=this.getTableElement(_1,_3);if(_4==null){return false}
var _5=this.getCellRecord(_1,_3);if(_5&&_5.$48j){return}
if(_3!=null){this.$48f(_5,_1,_3,_4,_2)}else{var _6=this.getTableElement(_1);if(_6!=null){var _7="TD",_8=(!this.shouldShowAllColumns()?this.$6j:0),_9=(!this.shouldShowAllColumns()?this.$46a:this.fields.length-1),_10=0;for(var _11=_8;_11<=_9;_11++,_10++){var _4;if(this.showColumnsSeparately||this.cacheDOM){_4=this.getTableElement(_1,_11)}else{_4=_6.cells[_10]}
if(_4==null)continue;this.$48f(_5,_1,_11,_4,_2)}}}
return true}
,isc.A.refreshCellStyle=function isc_GridRenderer_refreshCellStyle(_1,_2,_3){return this.setCellStyle(_1,_2,_3)}
,isc.A.refreshCell=function isc_GridRenderer_refreshCell(_1,_2){this.refreshCellStyle(_1,_2);if(!this.shouldRefreshCellHTML())this.refreshCellValue(_1,_2)}
,isc.A.refreshRow=function isc_GridRenderer_refreshRow(_1){if(!this.$48i(_1)){this.delayCall("refreshRow",[_1])}
for(var i=0;i<this.fields.length;i++){this.refreshCell(_1,i)}}
,isc.A.refreshCellStyles=function isc_GridRenderer_refreshCellStyles(_1,_2){this.logDebug("refreshing cell styles: "+_1.length+" cells");for(var i=0;i<_1.length;i++){var _4=_1[i][0],_5=_1[i][1];var _6=this.getTableElement(_4,_5);if(_6==null){continue}else{this.$48f(null,_4,_5,_6,_2)}}
return true}
,isc.A.getCellPageRect=function isc_GridRenderer_getCellPageRect(_1,_2){return[this.getColumnPageLeft(_2),this.getRowPageTop(_1),this.getColumnSize(_2),this.getRowSize(_1)]}
,isc.A.getColumnLeft=function isc_GridRenderer_getColumnLeft(_1){if(this.isRTL()){return this.getScrollWidth()-this.$45p.sum(0,_1+1)-
(this.vscrollOn?this.getScrollbarSize():0)}else{return this.$45p.sum(0,_1)}}
,isc.A.getColumnPageLeft=function isc_GridRenderer_getColumnPageLeft(_1){return this.getPageLeft()-this.getScrollLeft()+this.getColumnLeft(_1)+(this.isRTL()&&this.vscrollOn?this.getScrollbarSize():0)}
,isc.A.getColumnWidth=function isc_GridRenderer_getColumnWidth(_1){return this.$45p[_1]}
,isc.A.getInnerColumnWidth=function isc_GridRenderer_getInnerColumnWidth(_1){var _2=this.getColumnWidth(_1);if(_2==null)return null;return(_2-(2*this.cellSpacing+this.$47c()))}
,isc.A.$47c=function isc_GridRenderer__getCellHBorderPad(_1){if(!_1&&this.$48k!=null)return this.$48k;var _2=this.$47b(),_3=isc.Element.$q8(_2,true),_4=isc.Element.$q9(_2,true),_5=isc.Element.$q4(_2);if(_3==null)_3=this.cellPadding;if(_4==null)_4=this.cellPadding;this.$48k=(_3+_4+_5);return this.$48k}
,isc.A.getRowTop=function isc_GridRenderer_getRowTop(_1){if(_1<this.$6i)return this.getAvgRowHeight()*_1;var _2=this.$48l(),_3=this.$46o();if(_1>this.$459){return _2+_3.sum()+(((_1-1)-this.$459)*this.getAvgRowHeight())}
return _2+_3.sum(0,_1-this.$6i)}
,isc.A.getRowPageTop=function isc_GridRenderer_getRowPageTop(_1){return this.getPageTop()+this.getTopBorderSize()+(this.getRowTop(_1)-this.getScrollTop())}
,isc.A.getRowSize=function isc_GridRenderer_getRowSize(_1){return this.getDrawnRowHeight(_1)}
,isc.A.getDrawnRowHeight=function isc_GridRenderer_getDrawnRowHeight(_1){if(_1<this.$6i||_1>this.$459){return this.getAvgRowHeight()}
var _2=_1-this.$6i,_3=this.$46o();return _3[_2]}
,isc.A.getColumnSize=function isc_GridRenderer_getColumnSize(_1){if((this.fixedFieldWidths&&!this.autoSize)||(_1<this.$6j||_1>this.$46a))
{return this.getColumnWidth(_1)}
var _2=_1-this.$6j,_3=this.getColumnSizes();return _3[_2]}
,isc.A.$48l=function isc_GridRenderer__getUndrawnHeight(){return this.$6i*this.getAvgRowHeight()}
,isc.A.$46o=function isc_GridRenderer__getDrawnRowHeights(){if(this.$171!=null)return this.$171;var _1=this.$171=[];var _2=this.getTableElement();if(!_2||!_2.rows){delete this.$171;return _1}
var _3=this.getDrawnRows(),_4=_3[1]-_3[0]+1,_5=false;for(var _6=0;_6<=_4;_6++){var _7=this.cacheDOM?this.getTableElement(_6+this.$6i):_2.rows[_6];if(_7){var _8=isc.Browser.isSafari&&isc.Browser.safariVersion<500;if(this.getRowSpan&&this.fullRowSpans){_1[_6]=_7.offsetHeight;_5=true;continue}
var _9=(_8&&(this.fixedRowHeights==false||(this.shouldFixRowHeight!=null&&this.shouldFixRowHeight(this.getCellRecord(_6),_6)==false))),_10,_11=[];if(!_8||!_9){_10=_7.cells[_7.cells.length-1]}else{for(var k=0;k<_7.cells.length;k++){_11[k]=_7.cells[k]}}
if(_9){_1[_6]=0;for(var _13=0;_13<_11.length;_13++){var _14=_11[_13],_15=_14.offsetHeight;var _16=parseInt(_14.style?_14.style.height:null);if(isc.Browser.isStrict){if(this.cellPadding)_16+=this.cellPadding;_16+=isc.Element.$rc(_14.className)}
if(isc.isA.Number(_16)&&_16>_15)
_15=_16;if(_15>_1[_6])_1[_6]=_15}
_1[_6]+=this.cellSpacing}else if(_10){if(!_8){_1[_6]=_10.offsetHeight}else{var _17=parseInt(_10.height);if(_17!=null&&isc.isA.Number(_17)){if(isc.Browser.isStrict){_17+=isc.Element.$q3(_10.className)}}else{_17=_10.offsetHeight||0}
_1[_6]=_17}
_1[_6]+=this.cellSpacing}}
var _15=_1[_6];if(_15!=0&&_15!=null)_5=true}
_1[0]+=this.cellSpacing;if(!_5){this.logWarn("row heights not yet available; returning all zeroes");this.$171=null}
if(isc.Browser.isSafari&&!isc.Page.isLoaded())this.$171=null;return _1}
,isc.A.getColumnSizes=function isc_GridRenderer_getColumnSizes(){if(this.$48m!=null)return this.$48m;if(!this.isDrawn())return this.$45p.duplicate()||[];if(this.fixedColumnWidths&&isc.Browser.version>=5){return(this.$48m=this.$45p.duplicate())}else{var _1=this.$48m=[];var _2=this.getTableElement(this.$6i);if(_2==null){this.$48m=_1.concat(this.$45p);return this.$48m}
var _3=(isc.Browser.isMac?this.cellSpacing:0);for(var _4=0;_4<this.fields.length;_4++){var _5;if(this.showColumnsSeparately){_5=this.getTableElement(this.$6i,_4)}else if(!(isc.Browser.isSafari&&isc.Browser.safariVersion<125)){_5=_2.cells[_4]}
if(_5){_1[_4]=_5.offsetWidth+_3}else{_1[_4]=this.$45p[_4]}}
this.innerWidth=this.getTableElement().offsetWidth;return _1}}
,isc.A.getEventRow=function isc_GridRenderer_getEventRow(_1){if(this.isEmpty())return-2;if(_1==null)_1=this.getOffsetY();if(this.startSpace)_1-=this.startSpace;var _2=this.$48l();if(_1<=_2)return Math.floor(_1/ this.getAvgRowHeight());var _3=_1-_2,_4=this.$46o();var _5=this.inWhichPosition(_4,_3),_6;if(_5>=0){_6=this.$6i+_5}else{var _7=_3-_4.sum();_6=this.$459+1+Math.floor(_7/ this.getAvgRowHeight());if(_6>=this.getTotalRows())_6=-2}
return _6}
,isc.A.getEventColumn=function isc_GridRenderer_getEventColumn(_1){var _2=this.getColumnSizes();if(_1==null)_1=this.getOffsetX();return this.inWhichPosition(_2,_1,this.getTextDirection())}
,isc.A.getFocusRow=function isc_GridRenderer_getFocusRow(){return 0}
,isc.A.getFocusCol=function isc_GridRenderer_getFocusCol(){return 0}
,isc.A.getNativeFocusRow=function isc_GridRenderer_getNativeFocusRow(){var _1=this.$47v;if(_1==null)_1=this.getFocusRow();var _2=this.getVisibleRows();if(_2!=null&&_2.length>0&&(_1==null||_1<_2[0]||_1>_2[1])){_1=this.getVisibleRows()[0];if(this.getRowTop(_1)<this.getScrollTop())_1+=1}
return _1}
,isc.A.getNearestRowToEvent=function isc_GridRenderer_getNearestRowToEvent(){var _1=this.getEventRow();if(_1<0){var _2=this.getVisibleRows();if(_1==-1)return _2[0];if(_1==-2)return _2[1]}
return _1}
,isc.A.getNearestColToEvent=function isc_GridRenderer_getNearestColToEvent(){var _1=this.getEventColumn();if(_1<0){var _2=this.getVisibleColumns();if(_1==-1)return _2[0];if(_1==-2)return _2[1]}
return _1}
,isc.A.$45w=function isc_GridRenderer__getViewportFillRows(){var _1=Math.floor(this.getScrollTop()/this.getAvgRowHeight()),_2=_1+Math.ceil(this.getViewportHeight()/this.cellHeight);if(this.startSpace){var _3=Math.floor(this.startSpace/ this.getAvgRowHeight());_1=Math.max(0,_1-_3);_2=Math.max(0,_2-_3)}
var _4=this.virtualScrolling&&!this.fixedRowHeights&&this.$45y!=null;if(_4){if(_1==0&&_2>=(this.getTotalRows()-1))_4=false}
if(!_4)return[_1,_2];var _5=this.$45y;if(this.$45z<0)_5+=Math.floor(this.$45z/ this.cellHeight);if(_5<0)_5=0;var _6=_5+Math.ceil(this.getViewportHeight()/this.cellHeight);return[_5,_6]}
,isc.A.getAvgRowHeight=function isc_GridRenderer_getAvgRowHeight(){return this.fixedRowHeights?this.cellHeight:Math.max(this.cellHeight,this.avgRowHeight)}
,isc.A.getVisibleRows=function isc_GridRenderer_getVisibleRows(){var _1=this.getScrollTop();var _2=[this.getEventRow(_1),this.getEventRow(_1+this.getInnerHeight())];if(_2[1]==-2){var _3=this.getTotalRows();if(_3==0){_2[0]=-1;_2[1]=-1}else{_2[1]=this.getTotalRows()-1}}
return _2}
,isc.A.getVisibleColumns=function isc_GridRenderer_getVisibleColumns(){var _1=this.$45p;if(this.overflow==isc.Canvas.VISIBLE)return[0,_1.length-1];var _2=this.getScrollLeft();if(this.isRTL()){var _3=this.getScrollWidth()-this.getInnerWidth(),_2=_3-_2}
var _4=this.inWhichPosition(_1,_2),_5=this.inWhichPosition(_1,_2+this.getInnerWidth());if(_5==-2)_5=this.$45p.length-1;return[_4,_5]}
,isc.A.getDrawnRows=function isc_GridRenderer_getDrawnRows(){if(this.cacheDOM)return this.getVisibleRows();return[this.$6i,this.$459]}
,isc.A.shouldShowRollOver=function isc_GridRenderer_shouldShowRollOver(_1,_2){return(this.showRollOver&&!this.$46s)}
,isc.A.updateRollOver=function isc_GridRenderer_updateRollOver(_1,_2){this.setRowStyle(_1,null,(this.useCellRollOvers?_2:null))}
,isc.A.startHover=function isc_GridRenderer_startHover(){}
,isc.A.mouseMove=function isc_GridRenderer_mouseMove(_1,_2){if(this.$6k())return;var _3=this.getEventRow(),_4=this.getEventColumn();var _5=(_3>=0&&_4>=0&&this.cellIsEnabled(_3,_4));var _6=this.lastOverRow,_7=this.lastMouseOverRow,_8=this.lastOverCol,_9=this.lastMouseOverCol;if(!(_3==_6&&_4==_8)){if(_6!=null&&_8!=null){this.lastOverRow=null;this.lastOverCol=null;if(_3!=_6||_4<0||this.useCellRollOvers){this.updateRollOver(_6,_8,_5)}}
if(_5){this.lastOverRow=_3;this.lastOverCol=_4;if(_6!=_3||this.useCellRollOvers){if(this.shouldShowRollOver(_3,_4)){this.updateRollOver(_3,_4)}}}}
if(!(_3==_7&&_4==_9)){if(_7!=null&&_9!=null){this.lastMouseOverRow=null;this.lastMouseOverCol=null;if((_3!=_7||_4<0||this.hoverByCell)&&this.getCanHover()&&!this.keepHoverActive)
{this.stopHover()}
var _10=this.getCellRecord(_7,_9);if(this.cellOut){this.cellOut(_10,_7,_9)}
if(_3!=_7&&this.rowOut){this.rowOut(_10,_7,_9)}}
if(_5){this.lastMouseOverRow=_3;this.lastMouseOverCol=_4;if(_3!=_7||this.hoverByCell){if(this.getCanHover()){isc.Hover.setAction(this,this.$48n,[_3,_4],this.hoverDelay)}}
if(this.cellOver){this.cellOver(this.getCellRecord(_3,_4),_3,_4)}
if(_3!=_7&&this.rowOver){this.rowOver(this.getCellRecord(_3,_4),_3,_4)}}}
if(_3>=0&&_4>=0){if(this.cellMove){this.cellMove(this.getCellRecord(_3,_4),_3,_4)}
if(this.rowMove){this.rowMove(this.getCellRecord(_3,_4),_3,_4)}}}
,isc.A.$6k=function isc_GridRenderer__suppressEventHandling(){if(this.$46s!=null)return true;return false}
,isc.A.mouseOut=function isc_GridRenderer_mouseOut(){var _1=isc.EH.getTarget();if(this.$4p){var _2=this.$4p;for(var i=0;i<_2.length;i++){if(_2[i].contains(_1,true)){return}}}
if(_1==this&&!isc.EH.getDragTarget()){return}
if(this.getCanHover())this.stopHover();if(this.lastOverRow!=null&&this.lastOverCol!=null){var _4=this.lastOverRow,_5=this.lastOverCol;this.lastOverRow=null;this.lastOverCol=null;if(this.shouldShowRollOver(_4,_5)){this.updateRollOver(_4,_5)}}
if(this.lastMouseOverRow!=null&&this.lastMouseOverCol!=null){var _4=this.lastMouseOverRow,_5=this.lastMouseOverCol,_6=this.getCellRecord(_4,_5);this.lastMouseOverRow=null;this.lastMouseOverCol=null;if(this.cellOut){this.cellOut(_6,_4,_5)}
if(this.rowOut){this.rowOut(_6,_4,_5)}}}
,isc.A.$48n=function isc_GridRenderer__cellHover(_1,_2){var _3=this.getCellRecord(_1,_2);var _4;if(this.cellHover&&this.cellHover(_3,_1,_2)==false)_4=false;if(this.rowHover&&this.rowHover(_3,_1,_2)==false)_4=false;if(_4==false)return;if(this.showHover)this.$48o(_3,_1,_2)}
,isc.A.$48o=function isc_GridRenderer__showHover(_1,_2,_3){var _4=this.$1e();var _5=this.$48p(_1,_2,_3);if(!_5)_5=this.cellHoverHTML(_1,_2,_3);isc.Hover.show(_5,_4,this.cellHoverBoundary(_2,_3),this.getHoverTarget())}
,isc.A.$48p=function isc_GridRenderer__getCellHoverComponent(_1,_2,_3){}
,isc.A.getHoverTarget=function isc_GridRenderer_getHoverTarget(){return this}
,isc.A.cellHoverHTML=function isc_GridRenderer_cellHoverHTML(_1,_2,_3){return null}
,isc.A.getCellHoverComponent=function isc_GridRenderer_getCellHoverComponent(_1,_2,_3){return null}
,isc.A.cellHoverBoundary=function isc_GridRenderer_cellHoverBoundary(_1,_2){return null}
,isc.A.showContextMenu=function isc_GridRenderer_showContextMenu(){if(this.$6k())return false;var _1=this.getEventRow(),_2=this.getEventColumn();var _3=isc.EH.isKeyEvent();if(_3){_1=this.getFocusRow(),_2=this.getFocusCol()}
if(_1>=0&&_2>=0&&this.cellIsEnabled(_1,_2)){var _4=this.getCellRecord(_1,_2),_5;if(this.cellContextClick)
if(this.cellContextClick(_4,_1,_2)==false)_5=false;if(this.rowContextClick)
if(this.rowContextClick(_4,_1,_2)==false)_5=false;if(this.recordContextClick)
if(this.recordContextClick(_4,_1,_2)==false)_5=false;if(_5==false)return false}
return this.Super("showContextMenu")}
,isc.A.setSelection=function isc_GridRenderer_setSelection(_1){this.selection=_1;if(this.selection.isA("CellSelection")){this.observe(this.selection,"selectionChanged","observer.$48q(observed.changedCells)")}else{this.observe(this.selection,"setSelected","observer.$48r(observed);")}}
,isc.A.clearSelection=function isc_GridRenderer_clearSelection(){if(this.selection){if(this.isObserving(this.selection,"selectionChanged"))
this.ignore(this.selection,"selectionChanged");if(this.isObserving(this.selection,"setSelected"))
this.ignore(this.selection,"setSelected");delete this.selection}}
,isc.A.$48q=function isc_GridRenderer__cellSelectionChanged(_1){if(this.cellSelectionChanged){if(this.cellSelectionChanged(_1)==false)return false}
this.refreshCellStyles(_1)}
,isc.A.$48r=function isc_GridRenderer__setSelectedObservation(_1){var _2=false;if(!!_1.lastSelectionPreviousState!=!!_1.lastSelectionState){_2=true}else if(_1.lastSelectionState&&(!!_1.lastSelectionPartialValue!=!!_1.lastSelectionPreviousPartialValue))
{_2=true}
if(_2){this.$48s(_1.lastSelectionItem,!!_1.lastSelectionState,_1.cascadeSyncOnly)}}
,isc.A.$48s=function isc_GridRenderer__rowSelectionChanged(_1,_2,_3){if(!_3){if(this.handleSelectionChanged(_1,_2)==false){this.logWarn("bailing!!");return false}}
var _4=this.selection,_5=_4.lastSelectionItem,_6=_4.data.indexOf(_5,this.$6i,this.$459);if(_6==-1)_6=_4.data.indexOf(_5);if(_6==-1)return;this.updateRowSelection(_6)}
,isc.A.handleSelectionChanged=function isc_GridRenderer_handleSelectionChanged(_1,_2){if(this.selectionChanged)return this.selectionChanged(_1,_2)}
,isc.A.updateRowSelection=function isc_GridRenderer_updateRowSelection(_1){this.setRowStyle(_1)}
,isc.A.selectionEnabled=function isc_GridRenderer_selectionEnabled(){return this.selection!=null}
,isc.A.canSelectRecord=function isc_GridRenderer_canSelectRecord(_1){return(_1!=null&&_1[this.recordCanSelectProperty]!==false)}
,isc.A.mouseDown=function isc_GridRenderer_mouseDown(){if(this.$6k())return;var _1=this.getEventRow(),_2=this.getEventColumn();if(!(_1>=0&&_2>=0))return;if(!this.cellIsEnabled(_1,_2))return false;this.$48t=_1;this.$48u=_2;this.$48v=isc.EH.getX();this.$48w=isc.EH.getY();var _3=this.getCellRecord(_1,_2);if(!isc.EH.rightButtonDown()){return this.$48x(_3,_1,_2)}else{return this.$48y(_3,_1,_2)}}
,isc.A.rightMouseDown=function isc_GridRenderer_rightMouseDown(){return this.mouseDown()}
,isc.A.$48x=function isc_GridRenderer__cellMouseDown(_1,_2,_3){var _4;if(this.cellMouseDown&&(this.cellMouseDown(_1,_2,_3)==false))_4=false;if(this.rowMouseDown&&(this.rowMouseDown(_1,_2,_3)==false))_4=false;if(this.recordMouseDown&&this.recordMouseDown(_2,_3)==false)_4=false;if(_4==false)return false;this.selectOnMouseDown(_1,_2,_3)}
,isc.A.selectOnMouseDown=function isc_GridRenderer_selectOnMouseDown(_1,_2,_3){if(!this.selectionEnabled())return true;if(_2>=0&&_3>=0&&this.canSelectRecord(_1)&&!this.$48z()){this.$480=true;this.selection.selectOnMouseDown(this,_2,_3)}}
,isc.A.$48z=function isc_GridRenderer__shouldSelectOnMouseUp(){if(isc.EH.dragTarget!=null&&isc.EH.dragOperation==isc.EH.DRAG_SCROLL){return true}
return false}
,isc.A.$48y=function isc_GridRenderer__cellRightMouseDown(_1,_2,_3){if(this.canSelectOnRightMouse)this.selectOnRightMouseDown(_1,_2,_3)}
,isc.A.selectOnRightMouseDown=function isc_GridRenderer_selectOnRightMouseDown(_1,_2,_3){this.selectOnMouseDown(_1,_2,_3)}
,isc.A.mouseUp=function isc_GridRenderer_mouseUp(){if(this.$6k())return;var _1=this.getEventRow(),_2=this.getEventColumn();if(!(_1>=0&&_2>=0))return;if(!this.cellIsEnabled(_1,_2))return;var _3=this.getCellRecord(_1,_2);var _4;if(this.cellMouseUp&&(this.cellMouseUp(_3,_1,_2)==false))_4=false;if(this.rowMouseUp&&(this.rowMouseUp(_3,_1,_2)==false))_4=false;if(this.recordMouseUp&&this.recordMouseUp(_1,_2)==false)_4=false;if(_4==false)return _4;this.selectOnMouseUp(_3,_1,_2)}
,isc.A.selectOnMouseUp=function isc_GridRenderer_selectOnMouseUp(_1,_2,_3){if(!this.selectionEnabled())return true;if(_2>=0&&_3>=0){if(this.$48z()){this.selection.selectOnMouseDown(this,_2,_3)}
this.selection.selectOnMouseUp(this,_2,_3);if(this.$480){if(this.fireSelectionUpdated)this.fireSelectionUpdated();this.$480=null}}}
,isc.A.click=function isc_GridRenderer_click(){if(this.$6k())return;var _1=this.getEventRow(),_2=this.getEventColumn();return this.$481(_1,_2)}
,isc.A.$481=function isc_GridRenderer__rowClick(_1,_2){this.$482=this.$483=null;var _3=this.$48t;if(_3!=null&&_1!=_3){if(isc.EH.getX()==this.$48v){_1=this.$48t}else{return}}
if(isc.EH.getY()==this.$48w){_2=this.$48u}
if(!(_1>=0&&_2>=0))return;if(!this.cellIsEnabled(_1,_2))return false;this.$482=_1;var _4=this.getCellRecord(_1,_2),_5;if(!this.$484(_4,_1,_2))_5=false;if(this.rowClick&&(this.rowClick(_4,_1,_2)==false))
_5=false;this.$48t=null;return _5}
,isc.A.$484=function isc_GridRenderer__cellClick(_1,_2,_3){if(this.$48u!=_3){this.$483=null;return}
this.$483=_3;this.$485=null;return!(this.cellClick&&(this.cellClick(_1,_2,_3)==false))}
,isc.A.doubleClick=function isc_GridRenderer_doubleClick(){if(this.$6k())return;var _1=this.getEventRow(),_2=this.getEventColumn();if(!(_1>=0&&_2>=0))return;if(!this.cellIsEnabled(_1,_2))return false;if(_1!=this.$482){return this.$481(_1,_2)}
var _3=this.getCellRecord(_1,_2),_4;if(_2!=this.$483){_4=this.$484(_3,_1,_2)}else if(this.cellDoubleClick&&(this.cellDoubleClick(_3,_1,_2)==false))
{_4=false}
if(this.rowDoubleClick&&(this.rowDoubleClick(_3,_1,_2)==false))
_4=false;this.$48t=this.$48u=null;this.$482=this.$483=null;if(_4==false)return false}
,isc.A.dragMove=function isc_GridRenderer_dragMove(){if(this.$6k()||!this.selectionEnabled()||!this.canDragSelect)
return true;var _1=this.getNearestRowToEvent(),_2=this.getNearestColToEvent();this.selection.selectOnDragMove(this,_1,_2)}
,isc.A.dragStop=function isc_GridRenderer_dragStop(){this.fireSelectionUpdated()}
,isc.A.noSnapDragOffset=function isc_GridRenderer_noSnapDragOffset(_1){return this.snapToCells}
,isc.A.getHSnapPosition=function isc_GridRenderer_getHSnapPosition(_1,_2){if(!this.snapToCells){return this.Super("getHSnapPosition",arguments)}
var _3=this.ns.EH,_4=_2||this.snapHDirection,_5=this.snapHGap?Math.floor(_1/ this.snapHGap):this.getEventColumn(_1),_6=this.snapHGap?(_5*this.snapHGap):this.getColumnLeft(_5),_7=this.snapHGap?_6+this.snapHGap:this.getColumnLeft(_5)+this.getColumnSize(_5),_8=this.snapHGap?_5+1:this.getEventColumn(_7+1),_9;if(_8>=0){_9=this.snapHGap?_8*this.snapHGap:this.getColumnLeft(_8)}else{_9=_6}
var _10=_6+(this.snapHGap?this.snapHGap:this.getColumnSize(_5))/2;if(this.snapInsideBorder){var _11=isc.Element.$q1(this.baseStyle)
var _12=isc.Element.$q2(this.baseStyle)
_6+=_11;_7-=_12;_9+=_11}
if(_3.dragOperation==_3.DRAG_RESIZE){var _13=isc.EH.resizeEdge.contains("L");return _13?_6:_7}else{if(_4==isc.Canvas.BEFORE){return _6}else if(_4==isc.Canvas.AFTER){return _9}else{if(_1<=_10){return _6}else{return _9}}}}
,isc.A.getVSnapPosition=function isc_GridRenderer_getVSnapPosition(_1,_2){if(!this.snapToCells){return this.Super("getVSnapPosition",arguments)}
var _3=this.ns.EH,_4=_2||this.snapVDirection,_5=this.snapVGap?Math.floor(_1/ this.snapVGap):this.getEventRow(_1),_6=this.snapVGap?(_5*this.snapVGap):this.getRowTop(_5),_7=this.snapVGap?_6+this.snapVGap:this.getRowTop(_5)+this.getRowSize(_5),_8=this.snapVGap?_5+1:this.getEventRow(_7+1),_9;if(_8>=0){_9=this.snapVGap?_8*this.snapVGap:this.getRowTop(_8)}else{_9=_6}
var _10=_6+(this.snapVGap?this.snapVGap:this.getRowSize(_5))/2;if(this.snapInsideBorder){var _11=isc.Element.$qy(this.baseStyle)
var _12=isc.Element.$q0(this.baseStyle)
_6+=_11;_7-=_12;_9+=_11}
if(_3.dragOperation==_3.DRAG_RESIZE){var _13=isc.EH.resizeEdge.contains("T");return _13?_6:_7}else{if(_4==isc.Canvas.BEFORE){return _6}else if(_4==isc.Canvas.AFTER){return _9}else{if(_1<=_10)return _6;else return _9}}}
);isc.evalBoundary;isc.B.push(isc.A.getColumnAutoSize=function isc_GridRenderer_getColumnAutoSize(_1,_2,_3){if(this.getTotalRows()==0){return null}
var _4=this.$486=this.$486||isc.Canvas.create({top:-1000,width:1,height:1,autoDraw:false,_generated:true});var _5=this.autoFit,_6=this.wrapCells;this.autoFit=true;this.wrapCells=false;if(_2==null||_3==null){var _7=this.getDrawArea();_2=_7[0];_3=_7[1]+1}
_4.contents=this.getTableHTML(_1,_2,_3,true);this.autoFit=_5;this.wrapCells=_6;_4.draw();var _8;if(isc.isA.Array(_1)){var _9,_10=_4.getHandle().childNodes;for(var i=0;i<_10.length;i++){if(_10[i].tagName.toLowerCase()=="table"){_9=_10[i];break}}
if(_9&&_9.rows[0]){var _12=_9.rows[0],_13=_12.cells;_8=[];for(var i=0;i<_13.length;i++){_8[i]=_13[i].clientWidth}}}else{_8=_4.getScrollWidth()}
_4.clear();return _8}
,isc.A.redraw=function isc_GridRenderer_redraw(_1,_2,_3,_4){this.$48a();this.invokeSuper(isc.GridRenderer,"redraw",_1,_2,_3,_4);delete this.$45s}
,isc.A.modifyContent=function isc_GridRenderer_modifyContent(){if(!this.$46k)this.$48b();if(this.$45y!=null){this.$455=true;this.$450("scrollToRow in modifyContent");this.$455=null;var _1=this.getTableElement();if(_1)_1.style.visibility="inherit"}
if(this.$45x){var _2=this.$46o().sum();if(_2<this.getViewportHeight()){this.$47s=0;var _3=isc.Element.get(this.getID()+"$47t"),_4=this.$47s+(this.endSpace||0);if(_3){if(_4==0)_3.style.display="none"
else _3.style.display="";_3.style.height=_4+"px"}}
var _5=this.getVisibleRows(),_6=Math.max(1,_5[1]-_5[0]),_7=_6/ this.getTotalRows(),_8=this.getViewportRatio(true);if(isc.isA.Number(_7)&&((_8==1&&_7<1)||_8/ _7>1.25))
{this.$452=Math.max(this.cellHeight,Math.round(this.getViewportHeight()/_6))}}}
,isc.A.setStartSpace=function isc_GridRenderer_setStartSpace(_1){if(!isc.isA.Number(_1)||_1==this.startSpace)return;var _2=this.startSpace&&this.startSpace>_1;this.startSpace=_1;if(!this.isDrawn())return;var _3=_1+this.$468,_4=isc.Element.get(this.getID()+"$47a");if(_4){if(_3==0)_4.style.display="none";else _4.style.display="";if(this.$469){_4.style.height=_3+"px"}
if(!_2||!this.$469){_4.innerHTML=isc.Canvas.spacerHTML(1,_3)}
this.$yg()}}
,isc.A.setEndSpace=function isc_GridRenderer_setEndSpace(_1){if(!isc.isA.Number(_1)||_1==this.endSpace)return;var _2=this.endSpace&&this.endSpace>_1;this.endSpace=_1;if(!this.isDrawn())return;var _3=_1+this.$47s,_4=isc.Element.get(this.getID()+"$47t");if(_4){if(_3==0)_4.style.display="none";else _4.style.display="";if(this.$469)_4.style.height=_3+"px";if(!_2||!this.$469){_4.innerHTML=isc.Canvas.spacerHTML(1,_3)}
this.$yg()}}
,isc.A.setLeftSpace=function isc_GridRenderer_setLeftSpace(_1){if(this.leftSpace==_1)return;this.leftSpace=_1;this.redraw()}
,isc.A.setRightSpace=function isc_GridRenderer_setRightSpace(_1){if(this.rightSpace==_1)return;this.rightSpace=_1;this.redraw()}
,isc.A.clear=function isc_GridRenderer_clear(){this.Super("clear",arguments);this.$46d();delete this.$45s}
,isc.A.$46d=function isc_GridRenderer__clearTableCache(){this.$48e=null;this.$48d=null;delete this.$48m;delete this.$487;delete this.$171;this.$458=false}
);isc.B._maxIndex=isc.C+187;isc.GridRenderer.$488={getCellRecord:"rowNum,colNum",getCellValue:"record,rowNum,colNum,gridBody",findRowNum:"record",findColNum:"record",getBaseStyle:"record,rowNum,colNum",getCellStyle:"record,rowNum,colNum",getCellCSSText:"record,rowNum,colNum",cellIsEnabled:"rowNum,colNum",getRowHeight:"record,rowNum",getRowSpan:"record,rowNum,colNum",cellOut:"record,rowNum,colNum",cellOver:"record,rowNum,colNum",rowOut:"record,rowNum,colNum",rowOver:"record,rowNum,colNum",cellMove:"record,rowNum,colNum",rowMove:"record,rowNum,colNum",cellContextClick:"record,rowNum,colNum",rowContextClick:"record,rowNum,colNum",recordContextClick:"record,recordNum,fieldNum",cellMouseDown:"record,rowNum,colNum",rowMouseDown:"record,rowNum,colNum",recordMouseDown:"recordNum,fieldNum",cellMouseUp:"record,rowNum,colNum",rowMouseUp:"record,rowNum,colNum",recordMouseUp:"recordNum,fieldNum",selectOnMouseDown:"record,rowNum,colNum",selectOnRightMouseDown:"record,rowNum,colNum",selectOnMouseUp:"record,rowNum,colNum",cellClick:"record,rowNum,colNum",cellDoubleClick:"record,rowNum,colNum",rowClick:"record,rowNum,colNum",rowDoubleClick:"record,rowNum,colNum",cellHover:"record,rowNum,colNum",rowHover:"record,rowNum,colNum",cellHoverHTML:"record,rowNum,colNum",getCellHoverComponent:"record,rowNum,colNum",selectionChanged:"record,state",selectionUpdated:"record,recordList",cellSelectionChanged:"cellList",getRowElementId:"rowNum,physicalRowNum",getCellElementId:"rowNum,physicalRowNum,colNum,physicalColNum",shouldFixRowHeight:"record,rowNum",updateEmbeddedComponentZIndex:"component",updateEmbeddedComponentCoords:"component,record,rowNum,colNum",getRowRole:"rowNum,record",getRowAriaState:"rowNum,record",getCellRole:"rowNum,colNum,record",getCellAriaState:"rowNum,colNum,record"};isc.GridRenderer.registerStringMethods(isc.GridRenderer.$488);isc.ClassFactory.defineClass("ListGrid","VLayout","DataBoundComponent");isc.addGlobal("ListViewer",isc.ListGrid);isc.defineClass("GridBody",isc.GridRenderer);isc.A=isc.GridBody.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.adjustOverflowWhileDirty=false;isc.A.expandEmptyMessageToMatchFields=true;isc.B.push(isc.A.adjustOverflow=function isc_GridBody_adjustOverflow(_1,_2,_3,_4,_5){if(this.$489)return;var _6=this.grid;if(_6==null)return this.Super("adjustOverflow",arguments);var _7=_6.data,_8=false;;if(isc.isA.ResultSet(_7)&&!_7.lengthIsKnown()){if(_6.emptyMessageHeight==null){return this.invokeSuper(isc.GridBody,"adjustOverflow",_1,_2,_3,_4,_5)}
_8=true}
var _9=(this.autoFitData=="both"),_10=_9,_11=_6&&_6.frozenFields!=null,_12=_11&&_6&&(_6.frozenBody==this);if(!_9)_9=(this.autoFitData=="vertical");if(!_10)_10=(this.autoFitData=="horizontal");if(_10||_9){var _13,_14,_15,_16,_17,_18,_19;if(_9){var _20=this.grid.getAutoFitMinBodyHeight();_13=_20;var _21=_8?0:this.getTotalRows(),_22=_21;_15=0;if(this.autoFitMaxRecords){_22=Math.min(_22,this.autoFitMaxRecords)}
if(_22>0){var _23=this.$46o();var _24=this.$6i,_25=this.$459;if(this.$6i==null){_24=_22;_25=_22}
if(_24>0){_24=Math.min(_24,_22);for(var i=0;i<_24;i++){_15+=this.getRowHeight?this.getRowHeight(i):this.cellHeight}}
if(_25<_22-1){for(var i=_25+1;i<_22;i++){_15+=this.getRowHeight?this.getRowHeight(i):this.cellHeight}}
_25=Math.min(_25,_22-1);for(var i=0;i<=_25-_24;i++){_15+=_23[i]}
_17=_21>_22;var _27=this.getAutoFitMaxHeight();if(_27&&_15>_27){_15=_27;_17=true}}else{if(this.grid.emptyMessageHeight!=null){_15=this.grid.emptyMessageHeight}}
if(this.autoFitExtraRecords&&this.autoFitExtraRecords>0){var _28=Math.round(this.autoFitExtraRecords*this.cellHeight);_15+=_28}}else{_17=this.getScrollHeight()>this.getHeight()}
if(_10&&!_12){var _14=this.grid.getInnerWidth(),_29;if(_11){var _30=this.grid.getFrozenSlots(this.grid.$45p);_29=_30.sum();_14-=_29}
var _31=this.getColumnSizes(),_32=_31.sum();if(this.autoFitMaxColumns){var _33=this.autoFitMaxColumns;if(_11){_33=Math.max(1,_33-this.grid.frozenFields.length)}
if(_33<_31.length){_31=_31.slice(0,this.autoFitMaxColumns)}}
var _34=_31.sum();if(this.autoFitMaxWidth){var _35=this.autoFitMaxWidth;if(_11)_35=Math.max(20,_35-_29);_34=Math.min(_35,_34)}
_16=(this.overflow==isc.Canvas.SCROLL)?true:(this.overflow==isc.Canvas.AUTO)?(_32>Math.max(_14,_34)):false}else{_16=this.overflow==isc.Canvas.SCROLL?true:this.overflow==isc.Canvas.AUTO?this.getScrollWidth()>this.getWidth():false}
if(_9&&_15!=null){_15+=this.getVBorderPad()+this.getVMarginSize();if(_16){_15+=this.getScrollbarSize();var _27=this.getAutoFitMaxHeight()
if(_27&&_15>_27){_15=_27}}
if(_15>_13){_13=_15;this.$49a=true}else{if(this.$49a)delete this.$49a}}
if(_10&&!_12&&_34!=null){_34+=this.getHBorderPad()+this.getHMarginSize();if(_17||this.alwaysShowVScrollbar){_34+=this.getScrollbarSize();if(this.autoFitMaxWidth){var _35=this.autoFitMaxWidth;if(_11)_35=Math.max(20,_35-_29);_34=Math.min(_35,_34)}}
if(_34>_14){_14=_34;this.$49b=true}else{if(this.$49b)delete this.$49b}}
this.$489=true;_19=this.getDelta(this.$s6,_13,this.getHeight());_18=this.getDelta(this.$s7,_14,this.getWidth());delete this.$489;if(_19!=null||_18!=null){this.resizeBy(_18,_19,null,null,true)}
if(_18!=null||(_12&&_10)){var _36=this.grid,_37=(_14-(_17?_36.getScrollbarSize():0)),_38=_37;if(_11&&_36.headerLayout){if(_12){_38=this.getWidth()+_36.body.getWidth();if(_36.autoFitMaxWidth!=null&&(_38+_36.getHBorderPad()+_36.getHMarginSize()>_36.autoFitMaxWidth))
{return _36.body.adjustOverflow()}
_38-=(_36.body.vscrollOn?_36.getScrollbarSize():0)}else{_38=_37+_36.frozenBody.getWidth()}
_36.headerLayout.setWidth(_38)}
if(!_12){var _39=_36.header;if(_39&&_39.isDrawn()){_39.setWidth(_37)}}}}
var _40=(!_9&&this.$49a),_41=(!_10&&this.$49b);if(_40||_41){delete this.$49a;delete this.$49b;var _42=_40?this.grid.getAutoFitMinBodyHeight():null,_43=_41?(!_11?this.grid.getInnerWidth():(this.grid.getInnerWidth()-this.grid.frozenBody.getWidth())):null;this.resizeTo(_14,_13);this.grid.$49c("autoFitData mode changed")}
var _44=this.invokeSuper(isc.GridBody,"adjustOverflow",_1,_2,_3,_4,_5);if(!_12)this.grid.bodyOverflowed();return _44}
,isc.A.getAutoFitMaxHeight=function isc_GridBody_getAutoFitMaxHeight(){return this.grid?this.grid.getAutoFitMaxBodyHeight():null}
,isc.A.resizeBy=function isc_GridBody_resizeBy(_1,_2,_3,_4,_5){if(!_5){this.$74=this.getWidth()+(_1!=null?_1:0)}
return this.invokeSuper(isc.GridBody,"resizeBy",_1,_2,_3,_4,_5)}
,isc.A.cellContextClick=function isc_GridBody_cellContextClick(_1,_2,_3){var _4=this.grid.getFieldNumFromLocal(_3,this);return this.grid.$49d(_1,_2,_4)}
,isc.A.$481=function isc_GridBody__rowClick(_1,_2){if(!this.grid)return;var _3;var _4=this.grid.getFieldNumFromLocal(_2,this);var _5=this.grid.getField(_4)
if(_5&&_5.isRemoveField){this.grid.removeRecordClick(_1,_2);_3=false}else{_3=this.Super("$481",arguments)}
return _3}
,isc.A.$48p=function isc_GridBody__getCellHoverComponent(_1,_2,_3){if(this.grid&&isc.isA.ListGrid(this.grid)){var _4=this.grid.getFieldNumFromLocal(_3,this);return this.grid.$48p(_1,_2,_4)}}
,isc.A.getInnerHTML=function isc_GridBody_getInnerHTML(){this.grid.bodyDrawing(this);return this.Super("getInnerHTML",arguments)}
,isc.A.getTablePrintHTML=function isc_GridBody_getTablePrintHTML(_1){var _2=_1.startRow,_3=_1.endRow,_4=_3!=null?(_3-_2):this.getTotalRows(),_5=this.printMaxRows,_6=_1.printWidths,_7=_1.printProps;var _8={target:this,methodName:"gotTablePrintHTML",printContext:_1,printCallback:_1.callback}
_1.callback=_8;if(_5<_4){this.logDebug("get table print html - breaking HTML into chunks","printing");if(_2==null)_2=_1.startRow=0;if(_3==null)_3=_1.endRow=this.getTotalRows();this.getPrintHTMLChunk(_1);return null}
var _9=this.grid.$49e(_6,_7);var _10=this.getTableHTML(null,_2,_3,null,_8);this.grid.$49f(_9);return _10}
,isc.A.gotTablePrintHTML=function isc_GridBody_gotTablePrintHTML(_1,_2){var _3=_2.printCallback;if(_3){this.fireCallback(_3,"HTML,callback",[_1,_3])}}
,isc.A.getPrintHTMLChunk=function isc_GridBody_getPrintHTMLChunk(_1){var _2=this.grid.$49e(_1.printWidths);this.$461=true;this.$49g=true;var _3=_1.startRow,_4=_1.endRow,_5=this.printMaxRows,_6=_1.callback;this.currentPrintProperties=_1.printProps;if(!_1.html)_1.html=[];var _7=_1.chunkEndRow=Math.min(_4,(_3+_5)),_8=this.getTableHTML(null,_3,_7,null,{target:this,methodName:"gotPrintChunkHTML",printContext:_1,printCallback:_1.callback});this.grid.$49f(_2);this.$461=false;if(_8!=null){delete this.$49g;this.gotPrintChunkHTML(_8,{printContext:_1})}}
,isc.A.gotPrintChunkHTML=function isc_GridBody_gotPrintChunkHTML(_1,_2){var _3=_2.printContext,_4=_3.startRow,_5=_3.endRow,_6=_3.chunkEndRow,_7=this.printMaxRows,_8=_3.callback;_3.html.add(_1);if(_6<_5){_3.startRow=_6;return this.delayCall("getPrintHTMLChunk",[_3],0)}
if(_8!=null){var _9=_3.html.join(isc.emptyString);this.fireCallback(_8,"HTML,callback",[_9,_8])}}
,isc.A.gotComponentPrintHTML=function isc_GridBody_gotComponentPrintHTML(_1,_2){var _3=_2.context.asyncCallback,_4=_3.printContext;var _5=_4.printWidths;var _6=this.grid.$49e(_5);if(this.$49g){this.$461=true}
var _1=this.Super("gotComponentPrintHTML",arguments);if(this.$461)delete this.$461;if(_1!=null){delete this.$49g}else{this.grid.$49f(_6)}}
,isc.A.getCellVAlign=function isc_GridBody_getCellVAlign(_1,_2,_3,_4){if(this.grid&&this.grid.getCellVAlign){var _5=this.grid.getFieldNumFromLocal(_4,this);return this.grid.getCellVAlign(_1,_3,_5)}}
,isc.A.getCellAlign=function isc_GridBody_getCellAlign(_1,_2,_3,_4){if(this.grid&&this.grid.getCellAlign!=null){var _5=this.grid.getFieldNumFromLocal(_4,this);return this.grid.getCellAlign(_1,_3,_5)}else return _2.cellAlign||_2.align}
,isc.A.$47h=function isc_GridBody__drawRecordAsSingleCell(_1,_2,_3){var _4=this.grid;if(_4.showNewRecordRow&&_4.$49h(_1))return true;return isc.GridRenderer.$ce.$47h.call(this,_1,_2,_3)}
,isc.A.showSingleCellCheckboxField=function isc_GridBody_showSingleCellCheckboxField(_1){var _2=this.grid;return _2&&_2.showSingleCellCheckboxField(_1)}
,isc.A.$47k=function isc_GridBody__getSingleCellSpan(_1,_2,_3,_4){if(_2==this.$46k||!this.showSingleCellCheckboxField(_1)||(this.grid&&this.grid.frozenBody!=null&&this.grid.frozenBody!=this))
{return[_3,_4]}
return[Math.max(_3,1),_4]}
,isc.A.mouseWheel=function isc_GridBody_mouseWheel(){if(this.frozen&&this.grid!=null){var _1=this.ns.EH.lastEvent.wheelDelta;var _2=this.scrollTop+Math.round(_1*isc.Canvas.scrollWheelDelta);this.grid.body.scrollTo(null,_2,"frozenMouseWheel");return false}
return this.Super("mouseWheel",arguments)}
,isc.A.$45t=function isc_GridBody__getDrawRows(){if(this.frozen&&this.grid){var _1=this.grid;return _1.body.$45t()}
return this.Super("$45t",arguments)}
,isc.A.doneFastScrolling=function isc_GridBody_doneFastScrolling(){if(!this.frozen&&this.grid!=null&&this.grid.frozenBody!=null){var _1=this.$45r;this.Super("doneFastScrolling",arguments);if(_1){this.grid.frozenBody.$45s=true;this.grid.frozenBody.markForRedraw("Done fast scrolling on unfrozen body")}}}
,isc.A.scrollTo=function isc_GridBody_scrollTo(_1,_2,_3,_4){if(isc.$dd)arguments.$de=this;if(_1!=null){var _5=this.getScrollWidth()-this.getViewportWidth();_1=Math.max(0,Math.min(_5,_1))}
if(_2!=null){var _6=this.getScrollHeight()-this.getViewportHeight();_2=Math.max(0,Math.min(_6,_2))}
var _7=this.grid;this.invokeSuper(null,"scrollTo",_1,_2,_3,_4);var _8=this.$49i;if(!_8)_7.bodyScrolled(_1,_2,this.frozen);if(!this.isDirty()&&_7.$49j){_7.$6l.itemsMoved()}}
,isc.A.addEmbeddedComponent=function isc_GridBody_addEmbeddedComponent(_1,_2,_3,_4,_5){var _6=this.invokeSuper(isc.GridBody,"addEmbeddedComponent",_1,_2,_3,_4,_5);if(_1.$47r!=null&&_1.$47r!=-1){var _4=_1.$47r;_1.$49k=this.fields[_4].name}
return _1}
,isc.A.updateHeightForEmbeddedComponents=function isc_GridBody_updateHeightForEmbeddedComponents(_1,_2,_3){if(_1&&!_1.$4p&&this.grid.showRecordComponents&&this.grid.recordComponentHeight!=null)
{var _4=this.$474(_1,_2);if(_4.allWithin&&_4.extraHeight>0){_3=Math.max(_3,_4.extraHeight)}else{_3+=_4.extraHeight}
return _3}
return this.invokeSuper(isc.GridBody,"updateHeightForEmbeddedComponents",_1,_2,_3)}
,isc.A.$474=function isc_GridBody__getExtraEmbeddedComponentHeight(_1,_2){var _3=this.invokeSuper(isc.GridBody,"$474",_1,_2);if(this.grid.showRecordComponents&&this.grid.recordComponentHeight!=null){_3.extraHeight=Math.max(_3.extraHeight,this.grid.recordComponentHeight)}
return _3}
,isc.A.$473=function isc_GridBody__writeEmbeddedComponentSpacer(_1){if(_1&&this.grid&&this.grid.showRecordComponents&&this.grid.recordComponentHeight!=null)
{return true}
return this.invokeSuper(isc.GridBody,"$473",_1)}
,isc.A.getAvgRowHeight=function isc_GridBody_getAvgRowHeight(){if(this.grid)return this.grid.getAvgRowHeight(this);return this.Super("getAvgRowHeight",arguments)}
,isc.A.shouldShowAllColumns=function isc_GridBody_shouldShowAllColumns(){if(this.showAllColumns){return true}
if(!this.fixedRowHeights&&!this.showAllRows){if(this.grid.canExpandRecords&&this.grid.$49l){return false}
return true}
if(this.overflow==isc.Canvas.VISIBLE){return true}
return false}
,isc.A.redraw=function isc_GridBody_redraw(_1,_2,_3,_4){this.$49m=true;var _5=this.grid;if(_5.alwaysShowEditors&&!_5.$49j){_5.startEditing(null,null,true,null,true)}
var _6=_5.$6l,_7=_5.$49j,_8,_9,_10,_11,_12;_5.$49n();if(!_5.leaveScrollbarGap&&_5.predictScrollbarGap&&(this.overflow==isc.Canvas.AUTO)){var _13=this.vscrollOn,_14=!_5.isEmpty()&&(_5.getTotalRows()*_5.cellHeight)>this.getInnerHeight();if(_13!=_14){delete this.$49o;_5.$49c("body redrawing with changed vertical scroll-state")}}
var _15=false;if(_7){this.logInfo("redraw with editors showing, editForm.hasFocus: "+_6.hasFocus,"gridEdit");_8=_5.getEditCol();this.$27h();_12=this.$49p()}else if(isc.screenReader){_15=true}
if(this.$49o!=null){var _16=this.$49o;delete this.$49o;_5.$49c(_16)}
var _17=this.getDrawArea();var _18=this.grid,_19=this.$49q;if(!_19)_19=this.$49q=[0,0,0,0];var _18=this.grid,_20=_18.getRecord(_17[0]),_21=_18.getRecord(_17[1]),_22=(_20&&_20!=Array.LOADING)&&(_21&&_21!=Array.LOADING);;if(_22&&!_19.equals(_17))
{if(!this.frozen){_18.$49r(_19[0],_19[1],_19[2],_19[3],this);this.$49q=_17}}
if(!(this.frozen&&(_1==this.$49s||_1=="scrolled"))){_18.updateRecordComponents()}
this.invokeSuper(null,"redraw",_1,_2,_3,_4);delete this.$49m;if(_7){if(_12!=null&&_12.length>0){_6.removeItems(_12)}
_5.$49t(null,true,this);_5.updateEditRow(_5.getEditRow());if(_6.hasFocus||(this.$49u&&isc.EH.getFocusCanvas()==null))
{this.$49v(_8)}else{delete this.$49u}}else if(_6!=null){_5.$49t(null,null,this)}else if(isc.screenReader&&this.hasFocus&&_15){this.$47u(this.getNativeFocusRow())}}
,isc.A.setHandleDisabled=function isc_GridBody_setHandleDisabled(_1){var _2=this.grid;if(this.isDrawn()&&_2&&_2.$49j){this.markForRedraw("Grid body disabled while editing")}
return this.Super("setHandleDisabled",arguments)}
,isc.A.$49p=function isc_GridBody__updateEditItems(){var _1=this.grid,_2=_1.getEditForm(),_3=[],_4=_2.getItems();if(!_1.editByCell){var _5=_1.getEditRow(),_6=_1.getRecord(_5),_7=_1.getEditFormItemFieldWidths(_6);var _4=_2.getItems(),_8=_4.getProperty(this.fieldIdProperty),_9=_1.getDrawnFields(),_10=_9.getProperty(this.fieldIdProperty);var _11=_4.length==_9.length,_12=false;for(var i=0;i<_4.length;i++){var _14=_10.indexOf(_8[i]);if(_14==-1){_12=true;_3.add(_4[i])}else{_4[i].width=_7[_4[i].colNum];_4[i].$172=null;_4[i].redrawing()}}
if(!_11||_12){var _15=_1.getEditedRecord(_5,0);for(var i=0;i<_9.length;i++){if(!_8.contains(_10[i])){var _16=_1.fields.indexOf(_9[i]);var _17=_1.getEditItem(_9[i],_6,_15,_5,_16,_7[_16]);_2.addItem(_17)}}}}
return _3}
,isc.A.$27h=function isc_GridBody__storeFocusForRedraw(){var _1=this.grid,_2=_1.getEditForm(),_3=_1.getEditCol();if(_2.hasFocus){var _4=_2.getFocusSubItem();if(_4){_4.updateValue();var _5=_4;while(_4.parentItem!=null){_4=_4.parentItem}
if(!_1.canEditCell(_4.rowNum,_4.colNum)||_3!=_4.colNum){_2.blur()}else{if(_4.hasFocus){_4.rememberSelection();this.$49u=[_4.$29s,_4.$29t]}
_2.$20u()}}}
_2.$20f=true}
,isc.A.$49v=function isc_GridBody__restoreFocusAfterRedraw(_1){var _2=this.grid,_3=_2.getEditForm(),_4=_3.getItem(_2.getEditorName(_2.getEditRow(),_1));if(_4!=null&&_4.isDrawn()){var _5=_2.body.getScrollLeft(),_6=_2.body.getScrollTop(),_7=_2.body.getViewportWidth(),_8=_2.body.getViewportHeight(),_9=_4.getRect(),_10=_9[0]<_5||_9[1]<_6||_9[0]+_9[2]>(_5+_7)||_9[1]+_9[3]>(_6+_8);if(!_10){_3.$21l(_4);if(this.$49u&&this.$49u[0]!=null){_4.setSelectionRange(this.$49u[0],this.$49u[1])}
delete this.$49u}}}
,isc.A.focusAtEnd=function isc_GridBody_focusAtEnd(_1){var _2=this.grid,_3=_2?_2.getEditForm():null;if(_3){_3.focusAtEnd(_1)}else{return this.Super("focusAtEnd",arguments)}}
,isc.A.isDirty=function isc_GridBody_isDirty(_1,_2,_3){return this.invokeSuper(null,"isDirty",_1,_2,_3)||this.grid.isDirty()}
,isc.A.cellMove=function isc_GridBody_cellMove(_1,_2,_3){var _4=isc.EH.lastEvent?isc.EH.lastEvent.nativeTarget:null;if(_4&&_4.getAttribute!=null&&(_4.getAttribute("isErrorIcon")=="true"))
{if(this.grid.$49w!=null){var _5=this.grid.$49w[0],_6=this.grid.$49w[1];if(_5!=_2||_6!=_3){this.grid.$49x()}}
if(this.grid.$49w==null){this.grid.$49y(_2,_3)}}else{if(this.grid.$49w!=null){this.grid.$49x()}}}
,isc.A.shouldShowRollOver=function isc_GridBody_shouldShowRollOver(_1,_2,_3,_4){if(!this.grid.showRollOver||this.$46s)return false;var _5=this.grid;if(_5.$49j&&!_5.editByCell&&_1==_5.$471)return false;return true}
,isc.A.updateRollOver=function isc_GridBody_updateRollOver(_1,_2,_3){var _4=this.grid;if(_4.showRollOverCanvas){if(!_3){var _5=!(this.lastOverRow==_1&&this.lastOverCol==_2);_4.updateRollOverCanvas(_1,_2,_5)}}
this.setRowStyle(_1,null,(this.useCellRollOvers?_2:null));var _6=(this==_4.body?_4.frozenBody:_4.body);if(_6){_6.lastOverRow=this.lastOverRow;_6.lastOverCol=this.lastOverCol;_6.setRowStyle(_1,null,(this.useCellRollOvers?_2:null))}}
,isc.A.selectOnMouseDown=function isc_GridBody_selectOnMouseDown(_1,_2,_3){var _4=true,_5=this.grid.selectionAppearance,_6=(_5=="checkbox");if(_6){if((this.grid.frozenFields!=null&&this.grid.frozenBody!=this)||(this.grid.getCheckboxFieldPosition()!=_3))
{_4=false}}
if(_4){this.invokeSuper(isc.GridBody,"selectOnMouseDown",_1,_2,_3)}
if(isc.screenReader){this.$47u(_2)}}
,isc.A.selectOnMouseUp=function isc_GridBody_selectOnMouseUp(_1,_2,_3){var _4=this.grid.getCheckboxFieldPosition(),_5=this.grid.selectionAppearance;if(_5!="checkbox"||(_5=="checkbox"&&_4==_3)){this.invokeSuper(isc.GridBody,"selectOnMouseUp",_1,_2,_3)}}
,isc.A.handleSelectionChanged=function isc_GridBody_handleSelectionChanged(_1,_2){var _3=this.Super("handleSelectionChanged",arguments);this.grid.handleViewStateChanged();return _3}
,isc.A.$48f=function isc_GridBody__updateCellStyle(_1,_2,_3,_4,_5,_6,_7,_8){this.invokeSuper(isc.GridBody,"$48f",_1,_2,_3,_4,_5,_6,_7,_8);var _9=this.grid;if(_9&&_9.getEditRow()==_2){var _10=_9.getFieldName(_9.getFieldNumFromLocal(_3,this)),_11=_9.getEditForm(),_12=_11?_11.getItem(_10):null;if(_12&&_12.gridCellStyleChanged){if(_5==null)_5=this.getCellStyle(_1,_2,_3);_12.gridCellStyleChanged(_1,_2,_3,_5)}}}
,isc.A.getHoverTarget=function isc_GridBody_getHoverTarget(){return this.grid}
,isc.A.keyPress=function isc_GridBody_keyPress(_1,_2){return this.grid.bodyKeyPress(_1,_2)}
,isc.A.getFocusRow=function isc_GridBody_getFocusRow(){return this.grid.getFocusRow()}
,isc.A.getFocusCol=function isc_GridBody_getFocusCol(){var _1=this.grid.$49z();return this.grid.getLocalFieldNum(_1)}
,isc.A.$nb=function isc_GridBody__focusChanged(_1){var _2=this.Super("$nb",arguments);var _3=isc.EH.lastEvent;if(_3.target==this&&(_3.eventType==isc.EH.MOUSE_DOWN||_3.eventType==isc.EH.MOUSE_UP||_3.eventType==isc.EH.CLICK||_3.eventType==isc.EH.DOUBLE_CLICK))return _2;var _4,_5=this.grid;if(_1&&_5.isEditable()){if(_5.editOnFocus&&_5.isEditable()&&_5.getEditRow()==null)
{if(this.logIsInfoEnabled("gridEdit")){this.logInfo("Editing on focus: eventType: "+_3.eventType+", lastTarget "+_3.target,"gridEdit")}
if(_5.$490){delete _5.$490}else{_4=_5.findNextEditCell(0,0,true,true);if(_4!=null)
_5.handleEditCellEvent(_4[0],_4[1],isc.ListGrid.FOCUS)}}}
if(_1&&_4==null&&isc.screenReader){var _3=isc.EH.lastEvent,_6=_3.eventType;var _7=this.getNativeFocusRow();_5.$18m(_7);this.$47u(_7)}
return _2}
,isc.A.$47u=function isc_GridBody__putNativeFocusInRow(_1){var _2=this.grid;if(_2)_2.$18m(_1);return this.Super("$47u",arguments)}
,isc.A.updateRowSelection=function isc_GridBody_updateRowSelection(_1){var _2=this.grid;if(!_2)return;if(_2.showSelectionCanvas)_2.updateSelectionCanvas();if(_2.$491)return;this.invokeSuper(isc.GridBody,"updateRowSelection",_1);if(isc.Canvas.ariaEnabled()&&_2.selection){this.setRowAriaState(_1,"selected",_2.selection.isSelected(_2.getRecord(_1)))}
if(_2.getCurrentCheckboxField()!=null){var _3=_2.getCheckboxFieldPosition();if(_2)_2.refreshCell(_1,_3);var _4=(isc.isAn.Array(_2.data)||(isc.isA.ResultSet(_2.data)&&_2.data.allMatchingRowsCached())),_5=_2.getSelection()||[];if(_4){if(_5.length==_2.data.getLength()){_2.$492(true)}else{_2.$492(false)}}}else if(_2.getTreeFieldNum&&_2.selectionAppearance=="checkbox"){var _6=_2.getTreeFieldNum();_2.refreshCell(_1,_6)}}
,isc.A.$48q=function isc_GridBody__cellSelectionChanged(_1,_2,_3,_4){var _5=this.grid;if(_5&&_5.showSelectionCanvas)_5.updateSelectionCanvas();return this.invokeSuper(isc.GridBody,"$48q",_1,_2,_3,_4)}
,isc.A.shouldAnimateEmbeddedComponent=function isc_GridBody_shouldAnimateEmbeddedComponent(_1){var _2=this.grid;if(_1==_2.selectionCanvas)return _2.animateSelection;if(_1==_2.selectionUnderCanvas)return _2.animateSelectionUnder;if(_1==_2.rollOverCanvas)return _2.animateRollOver;if(_1==_2.rollUnderCanvas)return _2.animateRollUnder;return false}
,isc.A.$477=function isc_GridBody__handleEmbeddedComponentResize(_1,_2,_3){this.Super("$477",arguments);this.grid.$477(this,_1,_2,_3)}
,isc.A.draw=function isc_GridBody_draw(_1,_2,_3,_4){var _5=this.grid;if(_5.getEditRow()!=null){var _6=_5.getEditRow(),_7=_5.getRecord(_6),_8=_5.getEditCol(),_9=_5.$6l,_10=_5.getEditRowItems(_7,_6,_8,_5.editByCell),_11=_9.getItems();var _12=_11==null||_10.length!=_11.length;if(!_12){var _13=_11.getProperty("name");for(var i=0;i<_10.length;i++){if(!_13.contains(_10[i].name)){_12=true;break}}}
if(_12){this.logDebug("calling setItems on form from body draw","gridEdit");_9.setItems(_10)}else{this.logDebug("Skipping setItems() on form from body draw","gridEdit")}
_9.$20f=true}
this.invokeSuper(null,"draw",_1,_2,_3,_4);if(_5.$6l){_5.$49t(null,null,this)}
_5.updateEditRow(_5.getEditRow());if(_5.$463!=null){var _15=isc.isAn.Array(_5.$463)?_5.$463[0]:_5.$463,_16=isc.isAn.Array(_5.$463)?_5.$463[1]||0:0;_5.scrollCellIntoView(_15,_16);delete _5.$463}
this.grid.updateRecordComponents()}
,isc.A.layoutChildren=function isc_GridBody_layoutChildren(_1,_2,_3,_4){this.invokeSuper(null,"layoutChildren",_1,_2,_3,_4);if(!this.isDrawn()||(this.grid.frozenFields&&!this.grid.bodyLayout.isDrawn())){return}
if(_1=="scrolling state changed"){if(this.$46r==null){this.grid.layoutChildren("body scroll changed");delete this.$493}else{this.$493=true}}}
,isc.A.$46z=function isc_GridBody__rowAnimationComplete(){this.Super("$46z",arguments);if(this.$493){this.grid.layoutChildren("body scroll changed during animation");delete this.$493}}
,isc.A.handleMoved=function isc_GridBody_handleMoved(_1,_2,_3,_4){this.invokeSuper(null,"handleMoved",_1,_2,_3,_4);var _5=this.grid;if(_5.$49j){_5.$6l.itemsMoved()}}
,isc.A.handleParentMoved=function isc_GridBody_handleParentMoved(_1,_2,_3,_4){this.invokeSuper(null,"handleParentMoved",_1,_2,_3,_4);var _5=this.grid;if(_5.$49j){_5.$6l.itemsMoved()}}
,isc.A.setVisibility=function isc_GridBody_setVisibility(_1,_2,_3,_4){this.invokeSuper(null,"setVisibility",_1,_2,_3,_4);var _5=this.grid;if(_5.$49j)_5.$6l.itemsVisibilityChanged()}
,isc.A.parentVisibilityChanged=function isc_GridBody_parentVisibilityChanged(_1,_2,_3,_4){this.invokeSuper(null,"parentVisibilityChanged",_1,_2,_3,_4);var _5=this.grid;if(_5.$49j)_5.$6l.itemsVisibilityChanged()}
,isc.A.clear=function isc_GridBody_clear(){var _1=this.grid;_1.$49n();this.Super("clear",arguments);if(_1.$49j){_1.$49t(null,null,this);_1.$6l.itemsVisibilityChanged()}}
,isc.A.zIndexChanged=function isc_GridBody_zIndexChanged(){this.Super("zIndexChanged",arguments);var _1=this.grid;if(_1&&_1.$49j)_1.$6l.itemsZIndexChanged()}
,isc.A.parentZIndexChanged=function isc_GridBody_parentZIndexChanged(_1,_2,_3,_4){this.invokeSuper(null,"zIndexChanged",_1,_2,_3,_4);var _5=this.grid;if(_5.$49j)_5.$6l.itemsZIndexChanged()}
,isc.A.redrawFormItem=function isc_GridBody_redrawFormItem(_1,_2){var _3=this.grid;if(_3&&(_1.form==_3.$6l)){var _4=_3.getEditRow(),_5=_3.getColNum(_1.getFieldName());if(_3.getEditCol()==_5){_3.storeUpdatedEditorValue()}
_3.refreshCell(_4,_5,false,true)}else
return this.markForRedraw("Form Item Redraw "+(_2?_2:isc.emptyString))}
,isc.A.sizeFormItem=function isc_GridBody_sizeFormItem(_1){var _2=this.grid;var _3=_1.width,_4;if(isc.isA.String(_3)){var _5=_2.getEditFormItemFieldWidths(_1.record),_6=_5[_2.getFieldNum(_1.getFieldName())];if(_3=="*"){_4=_6}else if(_3[_3.length-1]=="%"){var _7=parseInt(_3);if(isc.isA.Number(_7)){_4=Math.floor(_6*(_7/ 100))}}}
var _8=_1.height,_9;if(isc.isA.String(_8)){var _10=_2.cellHeight;if(_3=="*"){_9=_10}else if(_8[_8.length-1]=="%"){var _11=parseInt(_8);if(isc.isA.Number(_11)){_9=Math.floor(_10*(_11/ 100))}}}
if(_9!=null||_4!=null){_1.$172=[_4==null?_1.width:_4,_9==null?_1.height:_9]}}
,isc.A.startRowAnimation=function isc_GridBody_startRowAnimation(_1,_2,_3,_4,_5,_6,_7,_8,_9){this.finishRowAnimation();var _10=(_9&&(this.$494!=null)),_11=this.grid;if(_10){_11.$495=true;_11.data.openFolder(this.$494);_11.$495=null}
this.Super("startRowAnimation",arguments);if(_10){_11.$495=true;_11.data.closeFolder(this.$494);_11.$495=null}
delete this.$494}
);isc.B._maxIndex=isc.C+64;isc.A=isc.ListGrid;isc.A.CORNER="corner";isc.A.FIELD="field";isc.A.BEFORE="before";isc.A.AFTER="after";isc.A.OVER="over";isc.A.CLICK_OUTSIDE="click_outside";isc.A.CLICK="click";isc.A.DOUBLE_CLICK="doubleClick";isc.A.ENTER_KEYPRESS="enter";isc.A.ESCAPE_KEYPRESS="escape";isc.A.UP_ARROW_KEYPRESS="arrow_up";isc.A.DOWN_ARROW_KEYPRESS="arrow_down";isc.A.LEFT_ARROW_KEYPRESS="arrow_left";isc.A.RIGHT_ARROW_KEYPRESS="arrow_right";isc.A.TAB_KEYPRESS="tab";isc.A.SHIFT_TAB_KEYPRESS="shift_tab";isc.A.EDIT_FIELD_CHANGE="field_change";isc.A.EDIT_ROW_CHANGE="row_change";isc.A.PROGRAMMATIC="programmatic";isc.A.FOCUS="focus";isc.A.$496=["selection","selectionType","canSelectCells","canDragSelect","canSelectOnRightMouse","recordCanSelectProperty","canDrag","canAcceptDrop","canDrop","autoFit","wrapCells","cellSpacing","cellPadding","cellHeight","enforceVClipping","autoFitData","autoFitMaxRecords","autoFitMaxWidth","autoFitMaxColumns","autoFitMaxHeight","autoFitExtraRecords","showAllColumns","drawAllMaxCells","drawAheadRatio","quickDrawAheadRatio","instantScrollTrackRedraw","scrollRedrawDelay","printMaxRows","animateRowsMaxTime","fastCellUpdates","showRollOver","useCellRollOvers","canHover","showHover","hoverDelay","hoverWidth","hoverHeight","hoverAlign","hoverVAlign","hoverStyle","hoverOpacity","hoverMoveWithMouse","hoverByCell","keepHoverActive","cellHoverOutset","showEmptyMessage","emptyMessageStyle","emptyMessageTableStyle","showOfflineMessage","offlineMessageStyle","useCellRecords","singleCellValueProperty","isSeparatorProperty","accessKey","canFocus","_useNativeTabIndex","tableStyle","baseStyle","recordCustomStyleProperty","showSelectedStyle","fullRowSpans","showFocusOutline"];isc.A.$497=["getCellStyleName","getCellStyleIndex","getRowTop","getRowPageTop","getRowSize","getDrawnRowHeight","getCellPageRect","getVisibleRows","getDrawnRows"];isc.A.$498=["getTotalRows","isEmpty","cellIsEnabled","willAcceptDrop","scrolled","getTableElementId","getRowElementId","getCellElementId","shouldFixRowHeight","getEmptyMessage","getCanHover","stopHover","updateEmbeddedComponentZIndex"];isc.A=isc.ListGrid;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.makeBodyMethods=function isc_c_ListGrid_makeBodyMethods(_1){var _2=this.$cr;if(_2==null){_2=this.$cr=[,"this.grid.$499 = this;"+"var returnVal = this.grid.",,"(",,");"+"this.grid.$499=null;"+"return returnVal;"]}
var _3={};for(var i=0;i<_1.length;i++){var _5=_1[i],_6=isc.GridRenderer.getArgString(_5);if(isc.contains(_6,"colNum")){_2[0]="if (this.fields[colNum]) colNum = this.fields[colNum].masterIndex;"}else if(isc.isAn.emptyString(_6)){_6="body";_2[0]="body = this;"}else{_2[0]=null}
_2[2]=_5;_2[4]=_6;var _7=_2.join(isc.emptyString);_3[_5]=new Function(_6,_7)}
return _3}
,isc.A.classInit=function isc_c_ListGrid_classInit(){this.addMethods(isc.ClassFactory.makePassthroughMethods(this.$497,"body"));var _1={};var _2=isc.getKeys(isc.GridRenderer.$488),_1=isc.ListGrid.makeBodyMethods(_2);isc.addProperties(_1,isc.ListGrid.makeBodyMethods(this.$498));this.$50a=_1;var _3={},_4=[,"var $499 = this.$499 || this.body;"+" if ($499 == null) {"+"return;"+"}"+"if($499.__orig_",,")return $499.__orig_",,"(",,")"],_5="__orig_",_6=isc.GridRenderer.getPrototype();for(var i=0;i<_2.length;i++){var _8=_2[i],_9=isc.GridRenderer.getArgString(_8);if(isc.ListGrid.getInstanceProperty(_8)==null){if(isc.contains(_9,"colNum")){_4[0]="if (colNum != null && colNum >= 0) colNum = this.getLocalFieldNum(colNum);"}else{_4[0]=null}
_4[2]=_4[4]=_8;_4[6]=_9
_3[_8]=new Function(_9,_4.join(isc.emptyString))}
_6[_5+_8]=_6[_8]}
this.$50b=_3;this.addMethods(_3)}
);isc.B._maxIndex=isc.C+2;isc.A=isc.ListGrid.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.styleName="listGrid";isc.A.useCellRecords=false;isc.A.recordEnabledProperty="enabled";isc.A.canExpandRecordProperty="canExpand";isc.A.animateFolders=true;isc.A.animateFolderTime=100;isc.A.animateFolderSpeed=3000;isc.A.animateRowsMaxTime=1000;isc.A.autoFetchDisplayMap=true;isc.A.warnOnUnmappedValueFieldChange=true;isc.A.showDetailFields=true;isc.A.groupStartOpen="first";isc.A.canCollapseGroup=true;isc.A.showGroupTitleColumn=true;isc.A.groupTitleColumnDefaults={canEdit:false,canFilter:false,canHide:false,canReorder:false,showDefaultContextMenu:false,showHeaderContextMenuButton:false,autoFreeze:true,sortNormalizer:function(_1,_2,_3){return _1.groupTitle},autoFitWidth:true,autoFitWidthApproach:"value",title:"&nbsp;"};isc.A.groupTitleColumnName="groupTitle";isc.A.showGroupSummaryInHeader=false;isc.A.groupNodeStyle="groupNode";isc.A.groupIcon="[SKINIMG]/TreeGrid/opener.gif";isc.A.groupIconSize=16;isc.A.groupIndentSize=20;isc.A.groupLeadingIndent=10;isc.A.canGroupBy=true;isc.A.groupByMaxRecords=1000;isc.A.nullGroupTitle="-none-";isc.A.valueIconSize=16;isc.A.valueIconLeftPadding=2;isc.A.valueIconRightPadding=2;isc.A.imageSize=16;isc.A.headerSpanVAlign="center";isc.A.showTreeColumnPicker=true;isc.A.fetchDelay=300;isc.A.uniqueMatch=true;isc.A.overflow=isc.Canvas.HIDDEN;isc.A.backgroundColor="white";isc.A.minHeight=50;isc.A.defaultWidth=200;isc.A.drawAllMaxCells=250;isc.A.drawAheadRatio=1.3;isc.A.quickDrawAheadRatio=1.0;isc.A.scrollRedrawDelay=75;isc.A.dataFetchDelay=300;isc.A.bodyConstructor="GridBody";isc.A.bodyOverflow=isc.Canvas.AUTO;isc.A.bodyBackgroundColor="white";isc.A.allowMismatchedHeaderBodyBorder=true;isc.A.emptyCellValue="&nbsp;";isc.A.cellHeight=20;isc.A.normalCellHeight=20;isc.A.fixedRecordHeights=true;isc.A.fixedFieldWidths=true;isc.A.autoFitMaxRecords=50;isc.A.autoFitMaxColumns=50;isc.A.canAutoFitFields=true;isc.A.headerAutoFitEvent="doubleClick";isc.A.autoFitFieldsFillViewport=true;isc.A.autoFitWidthApproach="value";isc.A.autoFitIconFields="title";isc.A.leaveScrollbarGap=true;isc.A.resizeFieldsForScrollbar=true;isc.A.cellSpacing=0;isc.A.cellPadding=2;isc.A.timeFormatter="toShortPaddedTime";isc.A.$50c=["<a href='",,"' target='",,"' onclick='if(window.",,") return ",,".$34i(event,",,",",,");'>",,"</a>"];isc.A.$50d="\\'";isc.A.$50e="_blank";isc.A.linkTextProperty="linkText";isc.A.fastCellUpdates=isc.Browser.isIE&&!isc.Browser.isIE9;isc.A.normalBaseStyle="cell";isc.A.tallBaseStyle="cell";isc.A.editFailedBaseStyle=null;isc.A.editFailedCSSText="color:red;border:1px solid red;";isc.A.editPendingBaseStyle=null;isc.A.editPendingCSSText="color:#0066CC;";isc.A.recordCustomStyleProperty="customStyle";isc.A.recordBaseStyleProperty="_baseStyle";isc.A.shrinkForFreeze=false;isc.A.alternateRecordFrequency=1;isc.A.recordCSSTextProperty="cssText";isc.A.showSelectedStyle=true;isc.A.generateClickOnSpace=true;isc.A.generateDoubleClickOnEnter=true;isc.A.arrowKeyAction="select";isc.A.recordComponentPoolingMode="viewport";isc.A.poolComponentsPerColumn=true;isc.A.showRollOver=!isc.Browser.isTouch;isc.A.backgroundComponentDefaults={snapTo:"TL",autoDraw:false,opacity:"50%"};isc.A.showHover=true;isc.A.cellHoverOutset=5;isc.A.hoverStyle="gridHover";isc.A.selectionAppearance="rowStyle";isc.A.recordCanSelectProperty="canSelect";isc.A.showEmptyMessage=true;isc.A.emptyMessage="No items to show.";isc.A.emptyMessageStyle="emptyMessage";isc.A.filterButtonPrompt="Filter";isc.A.loadingDataMessage="${loadingImage}&nbsp;Loading data...";isc.A.loadingDataMessageStyle="loadingDataMessage";isc.A.loadingMessage="&nbsp;";isc.A.singleCellValueProperty="singleCellValue";isc.A.isSeparatorProperty="isSeparator";isc.A.filterEditorDefaults={shouldPrint:false};isc.A.filterEditorHeight=22;isc.A.autoFetchTextMatchStyle="substring";isc.A.canEditNew=false;isc.A.recordEditProperty="_canEdit";isc.A.editValuesTestedProperty="$50f";isc.A.autoValidate=true;isc.A.warnOnRemoval=false;isc.A.warnOnRemovalMessage="Are you sure you want to delete this record?";isc.A.recordRemovedProperty="$50g";isc.A.removedCSSText="text-decoration:line-through;";isc.A.removeIcon="[SKIN]/actions/remove.png";isc.A.unremoveIcon="[SKIN]/actions/undo.png";isc.A.removeIconSize=16;isc.A.animateRemoveRecord=true;isc.A.animateRemoveTime=100;isc.A.animateRemoveSpeed=200;isc.A.removeFieldTitle="[Remove record]";isc.A.removeFieldDefaults={type:"icon",width:21,showHeaderContextMenuButton:false,showDefaultContextMenu:false,showTitle:false,canEdit:false,canSort:false,canGroupBy:false,canFilter:false};isc.A.autoSaveEdits=true;isc.A.showErrorIcons=true;isc.A.errorIconHeight=16;isc.A.errorIconWidth=16;isc.A.errorIconSrc="[SKIN]/validation_error_icon.png";isc.A.cancelEditingConfirmationMessage="Cancelling this edit will discard unsaved changes for this record. Continue?";isc.A.confirmDiscardEdits=true;isc.A.confirmDiscardEditsMessage="This action will discard all unsaved changes for this list.";isc.A.discardEditsSaveButtonTitle="Save";isc.A.newRecordRowMessage="-- Add New Row --";isc.A.enterKeyEditAction="done";isc.A.escapeKeyEditAction="cancel";isc.A.editEvent=isc.EH.DOUBLE_CLICK;isc.A.editOnF2Keypress=true;isc.A.selectOnEdit=true;isc.A.enumCriteriaAsInitialValues=true;isc.A.autoSelectEditors=true;isc.A.editFormDefaults={canSelectText:true,autoDraw:false,errorOrientation:"left",showErrorText:false,showErrorStyle:false,itemKeyPress:function(_1,_2,_3){return this.grid.editorKeyPress(_1,_2,_3)}};isc.A.longTextEditorThreshold=255;isc.A.longTextEditorType="PopUpTextAreaItem";isc.A.headerHeight=22;isc.A.minFieldWidth=15;isc.A.showHeader=true;isc.A.headerBackgroundColor="#CCCCCC";isc.A.headerDefaults={instantRelayout:true,enforcePolicy:false,itemClick:function(_1,_2){this.Super("itemClick",arguments);this.grid.$50h(_2,this)},itemDoubleClick:function(_1,_2){this.Super("itemDoubleClick",arguments);this.grid.$50i(_2,this)},showContextMenu:function(){return this.grid.headerBarContextClick(this)},backgroundRepeat:isc.Canvas.NO_REPEAT,shouldPrint:false};isc.A.headerButtonDefaults={getCurrentCursor:function(){var _1=this.parentElement?this.parentElement.grid:null;var _2;if(_1&&this.masterIndex!=null){var _3=_1.getField(this.masterIndex),_4=(_1.canSort!=false&&_1.$50j(_3)!=false);if(_4)_2=isc.Canvas.HAND;else _2=isc.Canvas.DEFAULT}else{_2=this.getClass().getPrototype().cursor}
this.cursor=_2;return this.Super("getCurrentCursor",arguments)},dragScrollType:"parentsOnly",minWidth:20};isc.A.sorterConstructor=isc.Button;isc.A.sorterDefaults={_redrawWithParent:false,getTitle:function(){return this.creator.getSortArrowImage()},click:function(){return this.creator.sorterClick()},showContextMenu:function(){return this.creator.sorterContextClick()},canFocus:false};isc.A.canSort=true;isc.A.canUnsort=false;isc.A.invalidateCacheOnUnsort=false;isc.A.sortDirection=Array.ASCENDING;isc.A.canPickFields=true;isc.A.canPickOmittedFields=false;isc.A.openRecordEditorContextMenuItemTitle="Edit";isc.A.dismissEmbeddedComponentContextMenuItemTitle="Dismiss";isc.A.deleteRecordContextMenuItemTitle="Delete";isc.A.canOpenRecordDetailGrid=true;isc.A.canOpenRecordEditor=true;isc.A.recordEditorSaveButtonTitle="Save";isc.A.recordEditorCancelButtonTitle="Cancel";isc.A.leaveHeaderMenuButtonSpace=true;isc.A.headerMenuButtonIcon="[SKIN]/ListGrid/headerMenuButton_icon.gif";isc.A.headerMenuButtonIconWidth=7;isc.A.headerMenuButtonIconHeight=7;isc.A.headerMenuButtonWidth=16;isc.A.headerMenuButtonHeight="100%";isc.A.canDragRecordsOut=false;isc.A.canReorderFields=true;isc.A.canResizeFields=true;isc.A.dragAppearance=isc.EH.TRACKER;isc.A.dragResizeAppearance=isc.EH.OUTLINE;isc.A.dragTrackerMode="title";isc.A.resizeFieldsInRealTime=(isc.Browser.isIE&&isc.Browser.isWin)||(isc.Browser.isFirefox&&isc.Browser.geckoVersion>=20080529)||(isc.Browser.isSafari&&isc.Browser.safariVersion>=500);isc.A.embeddedComponentIndent=25;isc.A.nestedGridDefaults={height:150};isc.A.skinImgDir="images/ListGrid/";isc.A.sortAscendingImage={src:"[SKIN]sort_ascending.gif",width:7,height:7};isc.A.sortDescendingImage={src:"[SKIN]sort_descending.gif",width:7,height:7};isc.A.trackerImage={src:"[SKIN]tracker.gif",width:16,height:16};isc.A.booleanTrueImage=null;isc.A.booleanFalseImage=null;isc.A.booleanPartialImage=null;isc.A.booleanImageWidth=16;isc.A.booleanImageHeight=16;isc.A.mozBodyOutlineColor="white";isc.A.mozBodyNoHeaderOutlineColor="red";isc.A.rowNumberStyle="specialCol";isc.A.rowNumberStart=1;isc.A.rowNumberFieldDefaults={name:"$50k",excludeFromState:true,canEdit:false,canFilter:false,canGroupBy:false,canSort:false,canExport:false,canHide:false,canReorder:false,canDragResize:false,canHilite:false,showAlternateStyle:false,$50l:true,showHeaderContextMenuButton:false,showDefaultContextMenu:false,keyboardFiresRecordClick:false,showGroupSummary:false,showGridSummary:false,formatCellValue:function(_1,_2,_3,_4,_5){if(_5.isGrouped){if(_2==null||_2.$35)return"&nbsp;";var _6=_5.getGroupedRecordIndex(_2);if(_6==-1)return null;return(_5.rowNumberStart+_6)}else{return this.rowNumberStart+_3}},autoFreeze:true};isc.A.$50m=30;isc.A.canExpandMultipleRecords=true;isc.A.maxExpandedRecordsPrompt="This grid is limited to \${count} simultaneously expanded records.  Please collapse some expanded records and retry.";isc.A.expansionFieldDefaults={name:"$50n",canEdit:false,canFilter:false,canGroupBy:false,canSort:false,canExport:false,canHide:false,canReorder:false,canDragResize:false,canHilite:false,$50o:true,showHeaderContextMenuButton:false,showDefaultContextMenu:false,keyboardFiresRecordClick:false,cellAlign:"center",recordClick:function(_1,_2,_3,_4,_5,_6,_7){if(!_1.canExpandRecords||!_4.$50o)return;if(!_1.$50p(_2,_3))return;if(_2.expanded)_1.collapseRecord(_2);else _1.expandRecord(_2)},formatCellValue:function(_1,_2,_3,_4,_5){_2=_5.getCellRecord(_3,_4);if(_2==null||_2.$35)return null;if(!_5.$50p(_2,_3))return null;return _5.getValueIconHTML(_2.expanded?_5.expansionFieldTrueImage:_5.expansionFieldFalseImage,this)},autoFreeze:true};isc.A.expansionFieldTrueImage="[SKINIMG]/ListGrid/group_opened.gif";isc.A.expansionFieldFalseImage="[SKINIMG]/ListGrid/group_opening.gif";isc.A.expansionFieldExtraWidth=16;isc.A.$50q=0;isc.A.expansionDetailFieldDefaults={_constructor:isc.HTMLFlow,autoDraw:false,width:"100%",height:"100%"};isc.A.expansionDetailsDefaults={_constructor:isc.DetailViewer,autoDraw:false,width:"100%"};isc.A.expansionRelatedDefaults={_constructor:isc.ListGrid,autoDraw:false,width:"100%",autoFitData:"vertical",autoFitMaxRecords:4};isc.A.expansionEditorSaveDialogPrompt="You have unsaved changes - do you want to save them now?";isc.A.expansionEditorShowSaveDialog=false;isc.A.expansionEditorDefaults={_constructor:"DynamicForm",autoDraw:false,numCols:4,colWidths:["*","*","*","*"],width:"100%",saveOperationType:"update"};isc.A.expansionDetailRelatedDefaults={_constructor:isc.HLayout,autoDraw:false,width:"100%",height:"100%"};isc.A.expansionLayoutDefaults={_constructor:isc.VLayout,autoDraw:false,width:"100%",height:10,overflow:"visible"};isc.A.recordDetailDSProperty="detailDS";isc.A.expansionCanEdit=false;isc.B.push(isc.A.shouldAnimateFolder=function isc_ListGrid_shouldAnimateFolder(_1){if(!this.animateFolders||!this.isDrawn())return false;var _2=this.data.isFolder(_1)?this.data.getOpenList(_1):null;if(_2==null||_2.length<=1)return false;return(_2.length<=this.getAnimateFolderMaxRows())}
,isc.A.getAnimateFolderMaxRows=function isc_ListGrid_getAnimateFolderMaxRows(){var _1=this.animateFolderMaxRows;if(_1==null){var _2=this.body?this.body.$45w():[0,0];_1=Math.min(75,(_2[1]-_2[0])*3)}
return _1}
,isc.A.getGroupTitleField=function isc_ListGrid_getGroupTitleField(){return this.groupTitleField}
,isc.A.showingGroupTitleColumn=function isc_ListGrid_showingGroupTitleColumn(){return(this.isGrouped&&this.showGroupSummary&&this.showGroupSummaryInHeader&&this.showGroupTitleColumn&&this.getGroupTitleField()==null)}
,isc.A.getGroupTitleColumn=function isc_ListGrid_getGroupTitleColumn(){var _1=this;var _2=isc.addProperties({$50r:true,getAutoFreezePosition:function(){return _1.getGroupTitleColumnPosition()}},this.groupTitleColumnDefaults,this.groupTitleColumnProperties);if(_2.name==null){_2.name=this.groupTitleColumnName}
return _2}
,isc.A.getGroupTitleColumnPosition=function isc_ListGrid_getGroupTitleColumnPosition(){if(!this.showingGroupTitleColumn())return-1;return(this.showRowNumbers?1:0)}
,isc.A.singleCellGroupHeaders=function isc_ListGrid_singleCellGroupHeaders(){if(this.getGroupTitleField()!=null)return false;if(this.showGroupSummary&&this.showGroupSummaryInHeader)return false;return true}
,isc.A.getBreadth=function isc_ListGrid_getBreadth(){return this.getInnerWidth()}
,isc.A.$50s=function isc_ListGrid__formatDateCellValue(_1,_2,_3,_4,_5,_6){if(isc.isA.Date(_1)){if(_3.$28q(_2)){var _7=_3.$28r(_2);var _8=isc.SimpleType.inheritsFrom(_2.type,"time");return isc.Time.toTime(_1,_7,_8)}
var _9=_2&&isc.SimpleType.inheritsFrom(_2.type,"datetime"),_10=!_9&&isc.SimpleType.inheritsFrom(_2.type,"date"),_7=_3.$28s(_2);if(_9)return _1.toShortDateTime(_7,true);return _1.toShortDate(_7,!_10)}
return _1}
,isc.A.$28q=function isc_ListGrid__formatAsTime(_1){if(_1==null)return false;if(_1.timeFormatter!=null&&_1.dateFormatter==null)return true;if(_1.dateFormatter!=null&&_1.timeFormatter==null)return false;return isc.SimpleType.inheritsFrom(_1.type,"time")}
,isc.A.$28s=function isc_ListGrid__getDateFormatter(_1){if(_1==null)return this.dateFormatter;if(_1.dateFormatter!=null)return _1.dateFormatter;if(_1.displayFormat!=null&&isc.SimpleType.inheritsFrom(_1.type,"date")){return _1.displayFormat}
if(this.datetimeFormatter!=null&&isc.SimpleType.inheritsFrom(_1.type,"datetime")){return this.datetimeFormatter}
return this.dateFormatter}
,isc.A.$50t=function isc_ListGrid__getDateInputFormat(_1){var _2;if(_1)_2=_1.inputFormat
if(!_2)_2=this.dateInputFormat;return _2}
,isc.A.$50u=function isc_ListGrid__formatNumberCellValue(_1,_2,_3,_4,_5,_6){if(isc.isA.Number(_1)){var _7=(_2.numberFormatter||_2.formatter||_3.numberFormatter);return _1.toFormattedString(_7)}
return _1}
,isc.A.$28r=function isc_ListGrid__getTimeFormatter(_1){if(_1!=null){if(_1.timeFormatter!=null)return _1.timeFormatter;if(_1.displayFormat!=null&&isc.SimpleType.inheritsFrom(_1.type,"time")){return _1.displayFormat}}
return this.timeFormatter}
,isc.A.$50v=function isc_ListGrid__formatTimeCellValue(_1,_2,_3,_4,_5,_6){var _7=_1;if(isc.isA.String(_7)){_7=isc.Time.parseInput(_7,true)}
if(isc.isA.Date(_7)){if(!_3.$28q(_2)){return _7.toShortDate(_3.$28s(_2),true)}
var _8=_3.$28r(_2);return isc.Time.toTime(_7,_8,true)}
return _1}
,isc.A.$50w=function isc_ListGrid__formatBinaryCellValue(_1,_2,_3,_4,_5,_6){if(isc.isA.String(_1))return _1;if(_4==null)return null;var _7=_2.name,_8=_2.nativeName||_7,_9=_4[_8+"_filename"],_1;if(_2.type=="imageFile"&&_2.showFileInline==true){var _10=_7+"$351";if(!_4[_10]){var _11=isc.Canvas.getFieldImageDimensions(_2,_4),_12=_3.getDataSource().streamFile(_4,_2.name);_11.width=_11.width||_3.imageSize;_11.height=_11.height||_3.imageSize;_1=_4[_10]=isc.Canvas.imgHTML(_12,_11.width,_11.height)}else
_1=_4[_10]}else{if(_2.showFileInline==true){this.logWarn("$50w(): Unsupported field-type for showFileInline: "+_2.type)}
if(_9==null||isc.isA.emptyString(_9))return this.emptyCellValue;var _13=isc.Canvas.imgHTML("[SKIN]actions/view.png",16,16,null,"style='cursor:"+isc.Canvas.HAND+"' onclick='"+_3.getID()+".viewRow("+_5+")'");var _14=isc.Canvas.imgHTML("[SKIN]actions/download.png",16,16,null,"style='cursor:"+isc.Canvas.HAND+"' onclick='"+_3.getID()+".downloadRow("+_5+")'");_1=_13+"&nbsp;"+_14+"&nbsp;"+_9}
return _1}
,isc.A.$50x=function isc_ListGrid__formatLinkCellValue(_1,_2,_3,_4,_5,_6){if(_1==null||isc.is.emptyString(_1))return _1;var _7=_2.target?_2.target.replaceAll(_3.$r9,_3.$50d):_3.$50e;var _8=_2.linkTextProperty?_2.linkTextProperty:_3.linkTextProperty;var _9=(_4&&_4[_8])?_4[_8]:_2.linkText||_1;var _10=_1;if(_2.linkURLPrefix)_10=_2.linkURLPrefix+_10;if(_2.linkURLSuffix)_10=_10+_2.linkURLSuffix;_10=_10.replaceAll(_3.$r9,_3.$50d);if(_7=="javascript"){_10="javascript:void"}
var _11=_3.$50c;_11[1]=_10;_11[3]=_7;var _12=_3.getID();_11[5]=_12;_11[7]=_12;_11[9]=_5;_11[11]=_6;_11[13]=_9;return _11.join(isc.emptyString)}
,isc.A.$34i=function isc_ListGrid__linkClicked(_1,_2,_3){var _4=(this.destroyed||!this.isDrawn()||!this.isVisible()||isc.EH.targetIsMasked(this.body)||!this.recordIsEnabled(_2,_3));var _5=this.getRecord(_2),_6=this.getField(_3);if(_1.target=="javascript"||_6.target=="javascript"){_4=true;this.cellClick(_5,_2,_3)}
if(_4){if(!isc.Browser.isIE){_1.preventDefault()}
return false}
return true}
,isc.A.$50y=function isc_ListGrid__formatImageCellValue(_1,_2,_3,_4,_5,_6){if(_1==null||_1==isc.emptyString)return isc.emptyString;var _7=isc.Canvas.getFieldImageDimensions(_2,_4);_7.width=_7.width||_3.imageSize;_7.height=_7.height||_3.imageSize;var _8=_1,_9=_2.imageURLPrefix||_2.baseURL||_2.imgDir;if(_2.imageURLSuffix!=null)_8+=_2.imageURLSuffix;return isc.Canvas.imgHTML(_8,_7.width,_7.height,null,_2.extraStuff,_9,_2.activeAreaHTML)}
,isc.A.$50z=function isc_ListGrid__formatIconCellValue(_1,_2,_3,_4,_5,_6){if(isc.isA.RecordEditor(_3)&&_3.isAFilterEditor()&&_2.canFilter==false)return null;if(_2.$500)return _2.$500;_2.$500=isc.Canvas.imgHTML(_2.cellIcon||_2.icon,_2.iconWidth||_2.iconSize||_3.imageSize,_2.iconHeight||_2.iconSize||_3.imageSize);return _2.$500}
,isc.A.setFastCellUpdates=function isc_ListGrid_setFastCellUpdates(_1){if(this.body!=null){this.body.setFastCellUpdates(_1);_1=this.body.fastCellUpdates}
if(this.frozenBody!=null){this.frozenBody.setFastCellUpdates(_1)}
this.fastCellUpdates=_1}
,isc.A.shouldShowRemoveField=function isc_ListGrid_shouldShowRemoveField(){if(this.fieldSourceGrid!=null)return this.fieldSourceGrid.shouldShowRemoveField();return this.canRemoveRecords}
,isc.A.markRecordRemoved=function isc_ListGrid_markRecordRemoved(_1,_2){if(!isc.isA.Number(_1))_1=this.findRowNum(_1);if(this.getEditRow()==_1)this.hideInlineEditor();var _3=this.getRecord(_1);this.selection.deselect(_3);this.setEditValue(_1,this.recordRemovedProperty,true,false,false);if(!_2)this.refreshRow(_1)}
,isc.A.recordMarkedAsRemoved=function isc_ListGrid_recordMarkedAsRemoved(_1){if(_1==null)return;if(!isc.isA.Number(_1))_1=this.findRowNum(_1);return(this.getEditValue(_1,this.recordRemovedProperty)==true)}
,isc.A.unmarkRecordRemoved=function isc_ListGrid_unmarkRecordRemoved(_1,_2){if(!isc.isA.Number(_1))_1=this.findRowNum(_1);this.clearEditValue(_1,this.recordRemovedProperty);if(!_2)this.refreshRow(_1)}
,isc.A.markSelectionRemoved=function isc_ListGrid_markSelectionRemoved(){var _1=this.getSelectedRecords();if(_1!=null){for(var i=0;i<_1.length;i++){this.markRecordRemoved(_1[i],true)}}
this.markForRedraw()}
,isc.A.shouldDeferRemoval=function isc_ListGrid_shouldDeferRemoval(){if(this.deferRemoval!=null)return this.deferRemoval;return!this.autoSaveEdits}
,isc.A.editorKeyDown=function isc_ListGrid_editorKeyDown(_1,_2){if(isc.Browser.isMoz&&_1&&_1.multiple&&isc.isA.NativeSelectItem(_1)&&_2=="Enter")
{_1.$501=_1.getValue()}
if(isc.Browser.isSafari&&isc.Browser.safariVersion>=525.13&&_2=="Tab"){return false}}
,isc.A.editorKeyPress=function isc_ListGrid_editorKeyPress(_1,_2,_3){var _4=isc.EH,_5,_6;var _7=isc.isA.PopUpTextAreaItem(_1)||(isc.RichTextItem&&isc.isA.RichTextItem(_1))||isc.isA.TextAreaItem(_1);if(_2=="Tab"){var _8=_4.shiftKeyDown();if(this.ns.isA.ContainerItem(_1)||(_1.icons!=null&&_1.icons.length>0&&!this.ns.isA.PopUpTextAreaItem(_1)))
{if(!this.$21p(_1,_8)){return false}}
_6=_8?isc.ListGrid.SHIFT_TAB_KEYPRESS:isc.ListGrid.TAB_KEYPRESS;_5=false}else if(_2=="Enter"){if(_1.getFocusIconIndex()!=null)return;if(_7&&isc.EH.altKeyDown()==false){return _5}
if(_1.$501!=null){var _9=_1.$501;delete _1.$501;_1.setValue(_9)}
_6=isc.ListGrid.ENTER_KEYPRESS;_5=false}else if(_2=="Escape"){_6=isc.ListGrid.ESCAPE_KEYPRESS;_5=false}else if(_2=="Arrow_Up"){var _10=_7;if(_10&&!isc.EH.altKeyDown())return _5;if(isc.isA.SelectItem(_1)&&!isc.EH.ctrlKeyDown())return _5;if(isc.EH.ctrlKeyDown()&&isc.EH.shiftKeyDown())return _5;_6=isc.ListGrid.UP_ARROW_KEYPRESS;_5=false}else if(_2=="Arrow_Down"){var _10=_7;if(_10&&!isc.EH.altKeyDown())return _5;if(isc.isA.SelectItem(_1)&&!isc.EH.ctrlKeyDown())return _5;if(isc.EH.ctrlKeyDown&&isc.EH.shiftKeyDown())return _5;_6=isc.ListGrid.DOWN_ARROW_KEYPRESS;_5=false}else if(this.moveEditorOnArrow&&this.$502){if(_2=="Arrow_Left"){_6=isc.ListGrid.LEFT_ARROW_KEYPRESS;_5=false}else if(_2=="Arrow_Right"){_6=isc.ListGrid.RIGHT_ARROW_KEYPRESS;_5=false}}
if(_6!=null){if(isc.EH.clickMaskUp()){isc.EH.setMaskedFocusCanvas(null,isc.EH.clickMaskRegistry.last())}
this.cellEditEnd(_6)}
return _5}
,isc.A.$21p=function isc_ListGrid__moveFocusWithinItem(_1,_2){if(!_1)return true;return(!_1.$21p(!_2))}
,isc.A.$503=function isc_ListGrid__editFormItem_elementFocus(_1){var _2=this.form,_3=_2.grid;var _4,_5,_6,_7;if(_3.$49j){_4=_3.$471;if(!_3.editByCell){_4=_3.$471;_6=this.getFieldName();_5=_3.fields.findIndex(_3.fieldIdProperty,_6);if(!_1){_7=(_3.$504!=_5);if(_7){_3.setNewEditCell(_4,_5);_3.cellEditEnd(isc.ListGrid.EDIT_FIELD_CHANGE)}}}else{_5=_3.$504}}
this.Super("elementFocus",arguments);if(_3.$49j){var _8=this.$505,_9=this.$506;delete this.$505;delete this.$506;var _10=isc.addProperties({},_3.getCellRecord(_4,_5),_3.$507(_4,_5));if(_9){var _6=_3.getFieldName(_5);_3.$508(this,_4,_5,_10[_6])}
if(_8)_3.$509(this,_4,_10)}else{_3.logWarn("suppressing editorEnter handlers on focus as listGrid.$49j is null")}}
,isc.A.getParentGroupIndex=function isc_ListGrid_getParentGroupIndex(_1){if(!this.isGrouped)return 0;var _2=this.groupTree,_3=_2.getParent(_1),_4=_2.getChildren(_2.getParent(_3)),_5=0;for(var i=0;i<_4.length;i++){var _7=_4[i];if(_7.groupValue==_3.groupValue){_5=i;break}}
return _5}
,isc.A.getGroupedRecordIndex=function isc_ListGrid_getGroupedRecordIndex(_1){if(!this.isGrouped)return-1;var _2=this.groupTree,_3=_2==null?null:_2.getParent(_1);if(_3==null)return-1;var _4=_2.getChildren(_2.getParent(_3)),_5=0,_6=0;for(var i=0;i<_4.length;i++){var _8=_4[i];if(_8.groupValue==_3.groupValue){var _9=_2.getChildren(_8);for(var j=0;j<_9.length;j++){if(this.objectsAreEqual(_9[j],_1)){return _6+j}}}
var _11=_2.getChildren(_8),_12=_11.length;if(this.showGroupSummary&&!this.showGroupSummaryInHeader){for(var _13=_12-1;_13>=0;_13--){if(_11[_13].groupSummaryRecordProperty)_12-=1;else break}}
_6+=_12}
return _6}
,isc.A.objectsAreEqual=function isc_ListGrid_objectsAreEqual(_1,_2){for(var _3 in _1){if(_1[_3]!=_2[_3])return false}
return true}
,isc.A.getRowNumberField=function isc_ListGrid_getRowNumberField(){var _1=this,_2={width:this.$50m,baseStyle:this.rowNumberStyle,rowNumberStart:this.rowNumberStart,getAutoFreezePosition:function(){return _1.getRowNumberFieldPosition()}};isc.addProperties(_2,this.rowNumberFieldDefaults,this.rowNumberFieldProperties);_2.title="&nbsp;";return _2}
,isc.A.getCurrentRowNumberField=function isc_ListGrid_getCurrentRowNumberField(){var _1=this.completeFields||this.fields,_2=_1.find(this.fieldIdProperty,"$50k");return!_2?null:isc.isAn.Array(_2)?_2[0]:_2}
,isc.A.isRowNumberField=function isc_ListGrid_isRowNumberField(_1){if(!_1||!_1.$50l)return false;else return true}
,isc.A.getRowNumberFieldPosition=function isc_ListGrid_getRowNumberFieldPosition(){if(this.fieldSourceGrid)return this.fieldSourceGrid.getRowNumberFieldPosition();if(!this.showRowNumbers)return-1;return 0}
,isc.A.shouldShowRowNumberField=function isc_ListGrid_shouldShowRowNumberField(){return this.fieldSourceGrid?this.fieldSourceGrid.shouldShowRowNumberField():(this.showRowNumbers==true)}
,isc.A.shouldShowExpansionField=function isc_ListGrid_shouldShowExpansionField(){return this.fieldSourceGrid?this.fieldSourceGrid.shouldShowExpansionField():this.canExpandRecords==true}
,isc.A.getExpansionField=function isc_ListGrid_getExpansionField(){var _1=this,_2={excludeFromState:true,width:this.$51a()+this.expansionFieldExtraWidth,getAutoFreezePosition:function(){return _1.getExpansionFieldPosition()}};_2.valueIconWidth=this.$51a();_2.valueIconHeight=this.$51b();isc.addProperties(_2,this.expansionFieldDefaults,this.expansionFieldProperties);_2.title="&nbsp;";return _2}
,isc.A.getCurrentExpansionField=function isc_ListGrid_getCurrentExpansionField(){var _1=this.completeFields||this.fields,_2=_1.find(this.fieldIdProperty,"$50n");return!_2?null:isc.isAn.Array(_2)?_2[0]:_2}
,isc.A.$51a=function isc_ListGrid__getExpansionFieldImageWidth(){return this.expansionFieldImageWidth||this.booleanImageWidth||(isc.CheckboxItem?isc.CheckboxItem.getInstanceProperty("valueIconWidth"):null)}
,isc.A.$51b=function isc_ListGrid__getExpansionFieldImageHeight(){return this.expansionFieldImageHeight||this.booleanImageHeight||(isc.CheckboxItem?isc.CheckboxItem.getInstanceProperty("valueIconHeight"):null)}
,isc.A.isExpansionField=function isc_ListGrid_isExpansionField(_1){if(!_1||!_1.$50o)return false;else return true}
,isc.A.getExpansionFieldPosition=function isc_ListGrid_getExpansionFieldPosition(){if(this.fieldSourceGrid!=null)return this.fieldSourceGrid.getExpansionFieldPosition();if(!this.canExpandRecords)return-1;var _1=0;if(this.showRowNumbers)_1+=1;if(this.showingGroupTitleColumn())_1+=1;return _1}
,isc.A.$50p=function isc_ListGrid__canExpandRecord(_1,_2){if(_1==null)_1=this.getRecord(_2);if(_1==null)return false;return this.canExpandRecord(_1,_2)}
,isc.A.canExpandRecord=function isc_ListGrid_canExpandRecord(_1,_2){return _1[this.canExpandRecordProperty]==false?false:true&&(this.canExpandRecords!=false)}
,isc.A.setCanExpandRecords=function isc_ListGrid_setCanExpandRecords(_1){if(this.canExpandRecords==_1)return;if(!_1){var _2=this.data;if(_2){var _3=this.data.findAll("expanded",true);if(_3!=null){for(var i=0;i<_3.length;i++){this.collapseRecord(_3[i])}}}}
this.canExpandRecords=_1;this.refreshFields()}
,isc.A.expandRecord=function isc_ListGrid_expandRecord(_1){var _2,_3=this.getRecordIndex(_1);if(!_1.expanded){if(this.onExpandRecord!=null&&!this.onExpandRecord(_1))return;if(!this.canExpandMultipleRecords){if(this.$51c)
this.collapseRecord(this.$51c);this.$51c=_1}else if(this.maxExpandedRecords){if(this.$50q==this.maxExpandedRecords){var _4=this.maxExpandedRecordsPrompt.evalDynamicString(this,{count:this.maxExpandedRecords});isc.say(_4);return}}
_2=this.getExpansionComponent(_1);var _5=this.createAutoChild("expansionLayout",{layoutLeftMargin:this.embeddedComponentIndent,members:[_2]});_5.isExpansionComponent=true;this.addEmbeddedComponent(_5,_1,this.data.indexOf(_1));_1.expanded=true;_1.hasExpansionComponent=true;this.$50q++;if(!this.canExpandMultipleRecords)this.$51c=_1}
this.delayCall("markForRedraw",["Expanded Record"]);this.refreshRow(_3)}
,isc.A.collapseRecord=function isc_ListGrid_collapseRecord(_1){var _2=(_1&&_1.$4p)?_1.$4p.find("isExpansionComponent",true):null;if(isc.isA.Layout(_2)){var _3=_2.getMember(0);if(isc.isA.DynamicForm(_3)&&_3.valuesHaveChanged()){if(this.autoSaveEdits==true){var _4=this;if(this.expansionEditorShowSaveDialog){isc.confirm(this.expansionEditorSaveDialogPrompt,function(_8){if(_8){_4.saveExpansionDetail(_3,_2,_1)}else{_4.$51d(_1,_2)}})}else{this.saveExpansionDetail(_3,_2,_1)}
return}else{var _5=_3.getChangedValues(),_6=this.getRecordIndex(_1),_4=this;if(this.expansionEditorShowSaveDialog){isc.confirm("You have unsaved changes - do you want to save them now?",function(_8){if(_8){for(var _7 in _5){_4.setEditValue(_6,_7,_5[_7])}
_4.$51d(_1,_2)}else{_4.$51d(_1,_2)}});return}else{for(var _7 in _5){this.setEditValue(_6,_7,_5[_7])}
this.$51d(_1,_2);return}}}}
this.$51d(_1,_2)}
,isc.A.saveExpansionDetail=function isc_ListGrid_saveExpansionDetail(_1,_2,_3){var _4=this;_1.saveData(function(_5,_6,_7){if(_6){_3=_6;_4.$51d(_3,_2)}},{showPrompt:true,promptStyle:"cursor"})}
,isc.A.$51d=function isc_ListGrid__collapseRecord(_1,_2){_2=_2||(_1&&_1.$4p?_1.$4p.find("isExpansionComponent",true):null);if(_1.expanded){if(this.onCollapseRecord!=null&&!this.onCollapseRecord(_1))return;if(this.$51c&&this.$51c==_1)
delete this.$51c;this.removeEmbeddedComponent(_1,_2?_2:this.frozenFields?this.frozenFields.length:0);this.$50q--}
_1.expanded=false;this.$51e();this.redraw()}
,isc.A.getCurrentExpansionComponent=function isc_ListGrid_getCurrentExpansionComponent(_1){if(isc.isA.Number(_1))_1=this.getRecord(_1);if(!_1.hasExpansionComponent)return null;var _2=_1.$4p?_1.$4p.find("isExpansionComponent",true):null;if(_2){return _2.members[0]}
return null}
,isc.A.getExpansionComponent=function isc_ListGrid_getExpansionComponent(_1){return this.$51f(_1,true,false)}
,isc.A.getRelatedDataSource=function isc_ListGrid_getRelatedDataSource(_1){return isc.DS.getDataSource(_1[this.recordDetailDSProperty])||isc.DS.get(this.detailDS)}
);isc.B._maxIndex=isc.C+56;isc.A=isc.ListGrid.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.warnOnReusedFields=true;isc.A.autoFitExpandLengthThreshold=10;isc.A.$49s="dataChanged";isc.A.$51g=0;isc.A.$if="boolean";isc.A.$51h="set fields";isc.A.$22d="checkbox";isc.A.checkboxFieldDefaults={name:"_checkboxField",excludeFromState:true,canEdit:false,shouldPrint:false,canFilter:false,canGroupBy:false,canSort:false,canExport:false,canHide:false,canReorder:false,canDragResize:false,canHilite:false,$51i:true,type:"boolean",showDefaultContextMenu:false,showHeaderContextMenuButton:false,hoverHTML:"return null;",autoFreeze:true};isc.A.$51j=15;isc.A.$51k="Getting listGrid fieldWidths. ";isc.A.gridComponents=["filterEditor","header","body","summaryRow"];isc.A.showComponentPropertyMap={header:"showHeader",filterEditor:"showFilterEditor",summaryRow:"showGridSummary"};isc.A.$51l="body";isc.A.$51m="header";isc.A.iconPadding=2;isc.A.$51n="summary";isc.A.$51o="<HR>";isc.A.$51p=["style='margin-left:",,"px;margin-right:",,"px;'"];isc.A.$if="boolean";isc.A.$ia="text";isc.A.$51q={" ":true,"\n":true,"\r":true,"\r\n":true};isc.A.$sd="&nbsp;";isc.A.$51r={text:true,TextItem:true,select:true,SelectItem:true,combobox:true,ComboBoxItem:true,comboBox:true,checkbox:true,CheckboxItem:true,date:true,DateItem:true,spinner:true,SpinnerItem:true,popUpTextArea:true,PopUpTextAreaItem:true};isc.A.$3i="false";isc.A.$302="true";isc.A.$51s="$51t";isc.A.$51u=0;isc.A.$45j="expand";isc.A.printAutoFit=true;isc.A.printWrapCells=true;isc.A.printHeaderStyle="printHeader";isc.A.printMaxRows=100;isc.A.defaultCellHoverComponentWidth=300;isc.A.defaultCellHoverComponentHeight=150;isc.A.$35a="Arrow_Up";isc.A.$35b="Arrow_Down";isc.A.$51v="Space";isc.A.$18n="Enter";isc.A.$51w="f2";isc.A.$51w="f2";isc.A.$kt="Escape";isc.A.$51x="Backspace";isc.A.$51y="Delete";isc.A.$51z="keyPress";isc.A.$pr="none";isc.A.$510="focus";isc.A.$22c="select";isc.A.$511="activate";isc.A._useFocusProxy=false;isc.A.showFocusOutline=!isc.Browser.isSafari;isc.A.showGridSummary=false;isc.A.invalidSummaryValue="&nbsp;";isc.A.includeInSummaryProperty="includeInSummary";isc.A.gridSummaryRecordProperty="isGridSummary";isc.A.groupSummaryRecordProperty="isGroupSummary";isc.A.$51n="summary";isc.A.recordSummaryBaseStyle="recordSummaryCell";isc.A.summaryRowConstructor="ListGrid";isc.A.summaryRowDefaults={showRollOver:false};isc.A.summaryRowHeight=20;isc.A.summaryRowStyle="gridSummaryCell";isc.A.$302="true";isc.A.updateEditorItemsInPlace=true;isc.A.$512={time:true,TimeItem:true};isc.A.$ik="time";isc.A.$3g={date:true,DateItem:true};isc.A.$513={datetime:true,dateTime:true,DatetimeItem:true,DateTimeItem:true};isc.A.$ij="date";isc.A.$ih="datetime";isc.A.$514={popUpTextArea:true,PopUpTextAreaItem:true};isc.A.$515={checkbox:true,CheckboxItem:true};isc.A.$if="boolean";isc.A.$22d="checkbox";isc.A.$22m="CycleItem";isc.A.$516={select:true,SelectItem:true};isc.A.$517=["change","changed","defaultDynamicValue"];isc.A.$518=["change","changed","defaultDynamicValue","keyPress","click","showIf","enableIf"];isc.A.$519={};isc.A.$52a="column remap";isc.A.$52b=0;isc.A.$52c="rowNum,colNum,editCompletionEvent,success";isc.A.$11d=true;isc.A.$52d="toggle frozen fields";isc.A.$52e="rebuilding body";isc.A.selectionCanvasDefaults={opacity:20};isc.A.selectionUnderCanvasDefaults={};isc.A.rollOverCanvasDefaults={snapTo:"TL",width:"100%",height:"100%"};isc.A.rollUnderCanvasDefaults={snapTo:"TL",width:"100%",height:"100%"};isc.A.$52f="header height changed";isc.A.$52g="header visibility changed";isc.A.headerMenuButtonDefaults={snapTo:"R",canFocus:false,resizeFrom:"R",title:null,iconSpacing:0,click:function(){this.parentElement.grid.headerMenuButtonClick()},mouseOut:function(){var _1=isc.EH.getTarget();if(!_1||!this.parentElement.contains(_1))this.hide();this.Super("mouseOut",arguments)}};isc.A.predictScrollbarGap=true;isc.A.sortFieldAscendingText="Sort Ascending";isc.A.sortFieldDescendingText="Sort Descending";isc.A.clearSortFieldText="Clear Sort";isc.A.clearAllSortingText="Clear All Sorting";isc.A.clearFilterText="Clear Filter";isc.A.configureSortText="Configure Sort";isc.A.autoFitFieldText="Auto Fit";isc.A.autoFitAllText="Auto Fit All Columns";isc.A.fieldVisibilitySubmenuTitle="Columns";isc.A.freezeFieldText="Freeze ${title}";isc.A.unfreezeFieldText="Unfreeze ${title}";isc.A.groupByText="Group by ${title}";isc.A.ungroupText="Ungroup";isc.A.headerContextMenuConstructor="Menu";isc.A.headerContextMenuDefaults={hide:function(){this.Super("hide",arguments);if(this.grid&&this.grid.headerMenuButton&&this.grid.headerMenuButton.isVisible()){this.grid.headerMenuButton.hide()}},doSort:function(_1,_2){if(_2=="unsort")this.grid.toggleSort(this.grid.getFieldName(_1),_2);else this.grid.sort(_1,_2)},canHover:true,showHover:true,cellHoverHTML:function(_1,_2,_3){return _1.prompt},groupField:function(_1){var _2=this.grid;if((_1.targetField&&_1.targetField.groupingMode)||((!_2.groupByField)||!_2.groupByField.contains(_1.fieldName))){_2.groupBy(_1.fieldName)}},ungroup:function(){this.grid.ungroup()}};isc.A.$52h="sort";isc.A.canMultiSort=true;isc.A.sortNumeralStyle="sortNumeral";isc.A.$52i="setSort";isc.A.chartConstructor="FacetChart";isc.A.chartType="Column";isc.A.groupIdField="groupId";isc.A.groupParentIdField="groupParentId";isc.A.retainOpenStateOnRegroup=true;isc.A.groupIconPadding=5;isc.A.showGroupSummary=false;isc.A.groupSummaryStyle="gridSummaryCell";isc.A.$52j={canDragSelect:true,canDragSelectText:true,canDragRecordsOut:true,canReorderRecords:true,canAcceptDroppedRecords:true};isc.A.$52k={bodyBackgroundColor:"backgroundColor",bodyStyleName:"styleName",fixedRecordHeights:"fixedRowHeights",fixedFieldWidths:"fixedColumnWidths",alternateRecordStyles:"alternateRowStyles",alternateRecordFrequency:"alternateRowFrequency",showAllRecords:"showAllRows",canSelectText:"canDragSelectText"};isc.B.push(isc.A.initWidget=function isc_ListGrid_initWidget(){this.Super("initWidget",arguments);if(this.showRecordComponents){delete this.showRecordComponents;this.setShowRecordComponents(true)}
if(this.fields==null&&this.defaultFields!=null){this.fields=isc.shallowClone(this.defaultFields)}else if(this.warnOnReusedFields&&this.fields!=null){if(this.fields.$52l){this.logWarn("ListGrid initialized with this.fields attribute set to an array "+"which is already being displayed in another ListGrid instance. To reuse "+"standard field configuration across multiple ListGrids, use "+"listGrid.defaultFields rather than assigning directly to listGrid.fields.")}else{var _1;if(isc.isAn.Array(this.fields)){_1=this.fields.findIndex("$52m",true)!=-1}else if(isc.isAn.Object(this.fields)){for(var _2 in this.fields){var _3=this.fields[_2];if(_3&&_3.$52m){_1=true;break}}}
if(_1){this.logWarn("ListGrid initialized with this.fields attribute set to an array "+"containing fields which are already being displayed in another ListGrid "+"instance. To reuse standard field configuration across multiple ListGrids, use "+"listGrid.defaultFields rather than assigning directly to listGrid.fields.")}}}
if(this.canEditNew)this.listEndEditAction=this.rowEndEditAction="next";if(this.alwaysShowEditors){this.editByCell=false;this.selectionType="none";this.selectOnEdit=false;if(this.canGroup!=false){this.logInfo("grouping functionality is not supported when alwaysShowEditors is true."+" Explicitly disabling this.canGroup","inactiveEditorHTML");this.canGroup=false}
if(this.modalEditing){this.logInfo("modalEditing is not supported when alwaysShowEditors is true."+" Explicitly setting this.modalEditing to fales","inactiveEditorHTML");this.modalEditing=false}
this.editEvent="click";if(!this.isEditable()){this.logInfo("alwaysShowEditors has been set for this grid but canEdit is unset and "+"no fields are explicitly marked as editable. Defaulting this.canEdit to true. "+"Note that to avoid this override developers can explicitly specify canEdit "+"at the grid or field level","inactiveEditorHTML");this.canEdit=true}}
if(this.canExpandRecords||(this.fixedRecordHeights==false&&this.virtualScrolling==null))
{if(this.fixedRecordHeights)this.$49l=this.fixedRecordHeights;this.fixedRecordHeights=false;this.virtualScrolling=true}
if(this.canAddFormulaFields&&isc.FormulaBuilder==null){this.logInfo("Required modules for adding formula fields not present - setting "+"canAddFormulaFields to false.");this.canAddFormulaFields=false}
if(this.canAddSummaryFields&&isc.SummaryBuilder==null){this.logInfo("Required modules for adding summary fields not present - setting "+"canAddSummaryFields to false.");this.canAddSummaryFields=false}
if(this.loadingMessage==null||this.loadingMessage==isc.emptyString)
this.loadingMessage="&nbsp;";if(this.autoFitData!=null){this.$52n=this.overflow;this.setOverflow("visible")}
if(this.canCollapseGroup==false)this.groupStartOpen="all";this.$52o=this.sortDirection;this.setData(this.data?null:this.getDefaultData());this.setSelectionAppearance(this.selectionAppearance,true);this.$52p()}
,isc.A.getDefaultData=function isc_ListGrid_getDefaultData(){return[]}
,isc.A.$52p=function isc_ListGrid__setUpDragProperties(){this.canDrag=!this.canDragSelectText&&(this.canDrag||this.canDragRecordsOut||this.canReorderRecords||this.canDragSelect);this.canDrop=(this.canDrop||this.canDragRecordsOut||this.canReorderRecords);this.canAcceptDrop=(this.canAcceptDrop||this.canAcceptDroppedRecords||this.canReorderRecords)}
,isc.A.getEmptyMessage=function isc_ListGrid_getEmptyMessage(){if(isc.ResultSet&&isc.isA.ResultSet(this.data)&&!this.data.lengthIsKnown()){if(isc.Offline&&isc.Offline.isOffline()){return this.offlineMessage}
return this.loadingDataMessage==null?"&nbsp;":this.loadingDataMessage.evalDynamicString(this,{loadingImage:this.imgHTML(isc.Canvas.loadingImageSrc,isc.Canvas.loadingImageSize,isc.Canvas.loadingImageSize)})}
if(this.isOffline()){return this.offlineMessage}
return this.emptyMessage==null?"&nbsp;":this.emptyMessage.evalDynamicString(this,{loadingImage:this.imgHTML(isc.Canvas.loadingImageSrc,isc.Canvas.loadingImageSize,isc.Canvas.loadingImageSize)})}
,isc.A.isEmpty=function isc_ListGrid_isEmpty(){if(!this.data)return true;if(!this.fields||this.fields.length==0)return true;if(isc.ResultSet&&isc.isA.ResultSet(this.data)){if(this.data.isPaged()){if(!this.data.isEmpty())return false;var _1=this.getAllEditRows();if(_1&&_1.length>0){for(var i=0;i<_1.length;i++){if(_1[i]>=0)return false}}
return true}else{if(this.data.lengthIsKnown())return this.getTotalRows()<=0;else return true}}else{return(this.getTotalRows()<=0)}}
,isc.A.setData=function isc_ListGrid_setData(_1){if(this.data==_1)return;if(!this.preserveEditsOnSetData)this.discardAllEdits();this.clearLastHilite();if(this.data){this.$52q(this.data);if(this.data.$393&&isc.isA.Function(this.data.destroy))
this.data.destroy()}
if(_1)this.data=_1;if(!this.data)return;this.$52r(this.data);this.regroup(true);this.calculateRecordSummaries(null,true);if(!this.selection||(this.data!=this.selection.data)){this.createSelectionModel()}
if(this.preserveEditsOnSetData)this.$52s();var _2=isc.ResultSet&&isc.isA.ResultSet(this.data)&&!this.data.lengthIsKnown();if(!_2&&this.$52t()&&!this.$49j){this.startEditing(null,null,true,null,true)}
if(!this.data.setSort)
this.canMultiSort=false;else if(this.getDataSource()&&this.canMultiSort!=false)
this.canMultiSort=this.getDataSource().canMultiSort&&this.canSort;var _3=this.getSort();if(_3)this.setSort(_3);if(isc.ResultSet&&isc.isA.ResultSet(this.data)&&this.body&&this.body.overflow=="visible")
{this.body.showAllRows=false}
if(this.summaryRow&&this.showGridSummary){this.summaryRow.$52u()}
this.$50q=0;if(!this.canExpandMultipleRecords&&this.$51c)
delete this.$51c;if(this.$52v){this.setSelectedState(this.$52v);delete this.$52v}
this.updateFieldWidthsForAutoFitValue("setData called.");this.$52w("setData")}
,isc.A.getAutoFitExpandField=function isc_ListGrid_getAutoFitExpandField(){if(!this.autoFitFieldsFillViewport)return null;if(this.autoFitExpandField!=null){var _1=this.getField(this.autoFitExpandField);if(_1!=null&&this.fields&&this.fields.contains(_1)&&(!this.frozenFields||!this.frozenFields.contains(_1)))
{return _1}}
var _2=[],_3=[];if(this.fields){for(var i=0;i<this.fields.length;i++){var _1=this.fields[i];if(!_1.showValueIconOnly&&(_1.type=="text"||_1.type==null))
{if(!this.$52x&&_1.frozen)continue;_2.add(_1);if(_2[i]!=null&&_2[i].length!=null){_3.add(_2[i])}}}}
if(_3.length>0){_3.sortByProperty("length",Array.DESCENDING);if(_3.last().length>=this.autoFitExpandLengthThreshold||_3.length==_2.length)
{return _3[0]}}
if(_2.length>0){var i=0;_1=_2[i]
while(_1!=null&&_1.length!=null&&_1.length<this.autoFitExpandLengthThreshold)
{i++;_1=_2[i]}
return _1}
return null}
,isc.A.updateFieldWidthsForAutoFitValue=function isc_ListGrid_updateFieldWidthsForAutoFitValue(_1){if(!this.body||this.body.$49o){return}
var _2=this.fields||[];for(var i=0;i<_2.length;i++){var _4=this.shouldAutoFitField(_2[i]);if(!_4)continue;var _5=this.getAutoFitWidthApproach(_2[i]);if(_5=="value"||_5=="both"){this.fields.$52y=false;this.body.$49o="Updating field widths for field auto-fit"+(_1?(":"+_1):".");break}}}
,isc.A.invalidateCache=function isc_ListGrid_invalidateCache(){if(this.getCheckboxFieldPosition()!=-1){this.deselectAllRecords();this.$492(false)}
if(this.body&&this.body.$49q)delete this.body.$49q;return this.Super("invalidateCache",arguments)}
,isc.A.$50j=function isc_ListGrid__canSort(_1){var _2=(_1.canSort==false)?false:true;if(!_1.canSortClientOnly||isc.isAn.Array(this.data))return _2;if(isc.isA.ResultSet(this.data)){if(!this.data.lengthIsKnown()||!this.data.canSortOnClient()){return false}else return _2}
return _2}
,isc.A.$52z=function isc_ListGrid__getFieldSortDirection(_1){var _2;var _3=_1.sortDirection;if(_3!=null){_2=Array.shouldSortAscending(_3)?"ascending":"descending"}else if(this.sortDirection!=null){_2=Array.shouldSortAscending(this.sortDirection)?"ascending":"descending"}else{_2="ascending"}
return _2}
,isc.A.createSelectionModel=function isc_ListGrid_createSelectionModel(){this.invokeSuper(isc.ListGrid,"createSelectionModel",arguments);if(isc.isA.Canvas(this.body)){this.body.setSelection(this.selection);if(this.frozenBody)this.frozenBody.setSelection(this.selection)}}
,isc.A.destroySelectionModel=function isc_ListGrid_destroySelectionModel(){if(this.body)this.body.clearSelection();if(this.frozenBody)this.frozenBody.clearSelection();return this.Super("destroySelectionModel",arguments)}
,isc.A.setSelectionType=function isc_ListGrid_setSelectionType(_1,_2){this.selectionType=_1;if(this.body)this.body.selectionType=_1}
,isc.A.setSelectionAppearance=function isc_ListGrid_setSelectionAppearance(_1,_2){if(this.selectionAppearance==_1&&!_2)return;this.selectionAppearance=_1;if(_2&&this.selectionType==null){this.selectionType=(_1=="checkbox"?isc.Selection.SIMPLE:isc.Selection.MULTIPLE)}
if(this.completeFields!=null){var _3=[];for(var i=0;i<this.completeFields.length;i++){var _5=this.completeFields[i];if(this.isCheckboxField(_5))continue;_3.add(_5)}
this.setFields(_3)}}
,isc.A.setBodyOverflow=function isc_ListGrid_setBodyOverflow(_1){this.bodyOverflow=_1;if(this.body)this.body.setOverflow(this.bodyOverflow)}
,isc.A.setBodyStyleName=function isc_ListGrid_setBodyStyleName(_1){this.bodyStyleName=_1;if(this.body&&(!this.alternateBodyStyleName||!this.alternateRecordStyles)){this.body.setStyleName(_1)}}
,isc.A.setAlternateBodyStyleName=function isc_ListGrid_setAlternateBodyStyleName(_1){this.alternateBodyStyleName=_1;if(this.body&&this.alternateRecordStyles){this.body.setStyleName(_1||this.bodyStyleName)}}
,isc.A.setAlternateRecordStyles=function isc_ListGrid_setAlternateRecordStyles(_1){if(this.alternateRecordStyles==_1)return;this.alternateRecordStyles=_1;if(this.body&&(this.alternateBodyStyleName!=null)){if(_1)this.body.setStyleName(this.alternateBodyStyleName);else this.body.setStyleName(this.bodyStyleName)}}
,isc.A.hasInherentHeight=function isc_ListGrid_hasInherentHeight(_1,_2,_3,_4){if(this.inherentHeight!=null)return this.inherentHeight;if(this.autoFitData==isc.Canvas.VERTICAL||this.autoFitData==isc.Canvas.BOTH){return true}
return this.invokeSuper(isc.ListGrid,"hasInherentHeight",_1,_2,_3,_4)}
,isc.A.hasInherentWidth=function isc_ListGrid_hasInherentWidth(_1,_2,_3,_4){if(this.inherentWidth!=null)return this.inherentWidth;if(this.autoFitData==isc.Canvas.HORIZONTAL||this.autoFitData==isc.Canvas.BOTH){return true}
return this.invokeSuper(isc.ListGrid,"hasInherentWidth",_1,_2,_3,_4)}
,isc.A.setAutoFitData=function isc_ListGrid_setAutoFitData(_1){this.autoFitData=_1;if(this.$520&&(_1=="both"||_1=="horizontal"||_1=="vertical")){delete this.$520;delete this.canFreezeFields}
if(_1==null&&this.$52n){this.setOverflow(this.$52n)}else if(this.overflow!="visible"){this.$52n=this.overflow;this.setOverflow("visible")}
if(this.body){this.body.autoFitData=this.autoFitData;this.body.adjustOverflow()}}
,isc.A.setAutoFitExtraRecords=function isc_ListGrid_setAutoFitExtraRecords(_1){this.autoFitExtraRecords=_1;if(this.body){this.body.autoFitExtraRecords=_1;this.body.adjustOverflow()}}
,isc.A.setAutoFitMaxRecords=function isc_ListGrid_setAutoFitMaxRecords(_1){this.autoFitMaxRecords=_1;if(this.body){this.body.autoFitMaxRecords=_1;this.body.adjustOverflow()}}
,isc.A.setAutoFitMaxHeight=function isc_ListGrid_setAutoFitMaxHeight(_1){this.autoFitMaxHeight=_1;if(this.body){this.body.adjustOverflow()}}
,isc.A.getAutoFitMaxBodyHeight=function isc_ListGrid_getAutoFitMaxBodyHeight(){if(this.autoFitMaxHeight==null)return null;var _1=this.getVBorderPad();if(this.showHeader)_1+=this.headerHeight;if(this.showFilterEditor)_1+=this.filterEditorHeight;return this.autoFitMaxHeight-_1}
,isc.A.getAutoFitMinBodyHeight=function isc_ListGrid_getAutoFitMinBodyHeight(){var _1=this.getHeight(),_2=this.getVBorderPad();if(this.showHeader)_2+=this.headerHeight;if(this.showFilterEditor)_2+=this.filterEditorHeight;return(_1-_2)}
,isc.A.setAutoFitMaxColumns=function isc_ListGrid_setAutoFitMaxColumns(_1){this.autoFitMaxColumns=_1;if(this.body){this.body.autoFitMaxColumns=_1;this.body.adjustOverflow()}}
,isc.A.setAutoFitMaxWidth=function isc_ListGrid_setAutoFitMaxWidth(_1){this.autoFitMaxWidth=_1;if(this.body){this.body.autoFitMaxWidth=_1;this.body.adjustOverflow()}}
,isc.A.autoFitField=function isc_ListGrid_autoFitField(_1,_2){var _3=this.getField(_1),_4=this.getFieldNum(_3);if(_3==null||_4==-1)return;var _5=this.getFieldAutoFitWidth(_3);if(_5==null||_5==this.$45p[_4])return;var _6=this.shouldAutoFitField(_3);this.resizeField(_1,_5,!_6);if(_6)_3.$521=this.$45p[_4];if(_2)this.scrollColumnIntoView(_4,false);return _5}
,isc.A.autoFitFields=function isc_ListGrid_autoFitFields(_1){if(_1==null)_1=this.fields;for(var i=0;i<_1.length;i++){this.autoFitField(_1[i])}}
,isc.A.shouldAutoFitField=function isc_ListGrid_shouldAutoFitField(_1){if(_1.autoFitWidth!=null)return _1.autoFitWidth;return this.autoFitFieldWidths}
,isc.A.getFieldAutoFitWidth=function isc_ListGrid_getFieldAutoFitWidth(_1,_2){if(this.body==null)return;var _3=this.getAutoFitWidthApproach(_1),_4=_3!="value",_5=_3!="title",_6,_7,_8;var _9=this.getColNum(_1);if(_4){var _10=this.getFieldHeaderButton(_9);if(_10!=null){var _11=_10.getWidth(),_12=_10.getOverflow();_10.setWidth(_2||this.minFieldWidth||1);_10.setOverflow("visible");if(_10.label&&_10.label.isDirty())_10.label.redraw();_10.parentElement.reflow();_6=_10.getVisibleWidth();_10.setWidth(_11);_8=_6}}
if(_5){var _13=this.getAutoFitValueWidths([this.fields[_9]]),_7=_13?_13[_9]:null;if(_7!=null&&(_8==null||_8<_7))_8=_7}
if(_8!=null){if(_2==null||_2<this.minFieldWidth)_2=this.minFieldWidth;if(_2!=null&&_8<_2)_8=_2}
return _8}
,isc.A.setAutoFitWidth=function isc_ListGrid_setAutoFitWidth(_1,_2){var _3=this.getField(_1);if(_3==null)return;if(_3.autoFitWidth==_2)return;_3.autoFitWidth=_2;if(_2)this.autoFitField(_3)}
,isc.A.setAutoFitFieldWidths=function isc_ListGrid_setAutoFitFieldWidths(_1,_2){if(_1==this.autoFitFieldWidths)return;this.autoFitFieldWidths=_1
if(_1){this.$49c("autoFitFieldWidths enabled")}else if(!_2){if(this.showHeader&&this.headerHeight>0)this.updateHeader();this.fields.$52y=false;this.$49c("autoFitFieldWidths disabled")}}
,isc.A.setAutoFitWidthApproach=function isc_ListGrid_setAutoFitWidthApproach(_1){if(this.autoFitWidthApproach==_1)return;this.autoFitWidthApproach=_1;if(this.showHeader&&this.headerHeight>0)this.updateHeader();this.fields.$52y=false;this.$49c("autoFitFieldWidthApproach changed")}
,isc.A.$52w=function isc_ListGrid__markBodyForRedraw(_1){if(this.bodies){this.bodies.map("markForRedraw",_1)}else{this.markForRedraw(_1)}}
,isc.A.redraw=function isc_ListGrid_redraw(_1,_2,_3,_4){if(this.body){if(this.body.$493){this.$49c("scrollbar change during animation");delete this.body.$493}}
this.invokeSuper(isc.ListGrid,"redraw",_1,_2,_3,_4)}
,isc.A.$52r=function isc_ListGrid__observeData(_1){if(!this.isObserving(_1,"dataChanged")){this.observe(_1,"dataChanged","observer.dataChanged("+(isc.ResultSet&&isc.isA.ResultSet(_1)?"arguments[0],arguments[1],arguments[2],arguments[3],arguments[4])":")"))}
if(!this.isObserving(_1,"dataArrived")){if(isc.ResultSet&&isc.isA.ResultSet(_1)){this.observe(_1,"dataArrived","observer.$522(arguments[0],arguments[1])")}else if(isc.ResultTree&&isc.isA.ResultTree(_1)){this.observe(_1,"dataArrived","observer.$522(arguments[0])")}}
if(isc.isA.Tree(_1)){this.observe(_1,"changeDataVisibility","observer.$523()")}}
,isc.A.groupTreeChanged=function isc_ListGrid_groupTreeChanged(){if(this.$524)return;if(!this.$525&&!this.suppressEditRowRemap)this.$52s();var _1=this.getTotalRows()-1;if(this.body){if(this.body.lastOverRow>_1)delete this.body.lastOverRow;if(this.body.lastMouseOverRow>_1)delete this.body.lastMouseOverRow;if(this.body.$526>_1)delete this.body.$526}
if(this.$527>_1)delete this.$527;if(this.hilites)this.applyHilites();if(!this.$528)this.$52w(this.$49s)}
,isc.A.$529=function isc_ListGrid__observeGroupData(_1){this.observe(_1,"dataChanged","observer.groupTreeChanged()");this.observe(_1,"changeDataVisibility","observer.$523()")}
,isc.A.$523=function isc_ListGrid__folderToggleObservation(){if(this.$495){this.$52s();return}
if(this.body)this.body.finishRowAnimation();this.$52s();this.$52w('folderToggled')}
,isc.A.toggleFolder=function isc_ListGrid_toggleFolder(_1){if(this.data.isOpen(_1)){this.closeFolder(_1)}else{this.openFolder(_1);if(this.frozenBody)this.frozenBody.markForRedraw()}}
,isc.A.openFolder=function isc_ListGrid_openFolder(_1){if(this.folderOpened!=null){this.convertToMethod("folderOpened");if(this.folderOpened(_1)==false)return false}
if(this.animateFolders){this.animateOpen(_1)}else{this.data.openFolder(_1)}}
,isc.A.animateOpen=function isc_ListGrid_animateOpen(_1){var _2=this.data;if(_2.isOpen(_1))return;this.$495=true;_2.openFolder(_1);delete this.$495;var _3=_2.getParent(_1);if(_3&&!_2.isOpen(_3))return;if(_2.getLoadState(_1)!=isc.Tree.LOADED){this.$53a=_1;return}
this.$53b(_1)}
,isc.A.closeFolder=function isc_ListGrid_closeFolder(_1){if(this.folderClosed!=null){this.convertToMethod("folderClosed");if(this.folderClosed(_1)==false)return false}
if(this.getEditRow()!=null){var _2=this.getRecord(this.getEditRow());if(this.data.isDescendantOf(_2,_1))this.endEditing()}
if(this.shouldAnimateFolder(_1))
this.animateClose(_1);else
this.data.closeFolder(_1)}
,isc.A.animateClose=function isc_ListGrid_animateClose(_1){if(!this.data.isOpen(_1))return;var _2=this.data.getParent(_1);if(_2&&!this.data.isOpen(_2)){return this.closeFolder(_1)}
var _3=this.data,_4=_3.indexOf(_1),_5=_3.getOpenList(_1).getLength()-1;this.startRowAnimation(false,_4+1,_4+_5+1,{target:this,methodName:"redraw"},this.animateFolderSpeed,this.animateFolderTime,this.animateFolderEffect,true);this.$495=true;this.data.closeFolder(_1);delete this.$495
if(this.body&&this.body.$46g!=null){this.body.$494=_1}}
,isc.A.$53b=function isc_ListGrid__startFolderAnimation(_1){if(!this.shouldAnimateFolder(_1)){this.markForRedraw();return}
var _2=this.data,_3=_2.indexOf(_1),_4=_2.getOpenList(_1).getLength()-1;if(_3<0||_4<=0)return;this.startRowAnimation(true,_3+1,(_3+_4+1),{target:(this.bodyLayout||this.body),methodName:"redraw"},this.animateFolderSpeed,this.animateFolderTime,this.animateFolderEffect,true)}
,isc.A.$53c=function isc_ListGrid__addNodeToOpenState(_1,_2,_3,_4){if(!_1.isOpen(_2)||!_1.isLoaded(_2))return false;var _5=_1.getFolders(_2),_6=false;if(_5!=null){for(var i=0;i<_5.length;i++){_6=this.$53c(_1,_5[i],_3,_4)||_6}}
if(_4){var _8={};_8[_2.groupName]=_2.groupValue;_3.add(_8)}else{_3[_3.length]=_1.getPath(_2)}
return true}
,isc.A.dataChanged=function isc_ListGrid_dataChanged(_1,_2,_3,_4,_5){if(isc.$dd)arguments.$de=this;this.$524=true;var _6=_5||_1=="add"||_1=="remove";if(!_6&&_1=="replace"){if(_2==null||_3==null)_6=true;else{var _7=_11.get(_3);if(_7==null)_6=true;else{for(var i=0;i<this.fields.length;i++){if(this.shouldAutoFitField(this.fields[i])){var _9=this.getFieldName(this.fields[i]);if(_7[_9]!=_4[_9]){_6=true;break}}}}}}
this.calculateRecordSummaries(null,false);var _10=this.getGroupByFields();if(_10!=null&&!this.$53d){if(_1=="add"||_1=="remove"||_1=="replace"||(_1=="update"&&(_2==null||_3==null)))
{this.$53d=true}else if(_1=="update"){var _11=this.data;if(this.data.isGroupedOutput&&this.originalData)_11=this.originalData;var _7=_11.get(_3);if(_7==null)this.$53d=true;if(!this.$53d){var _12=(this.dataSource!=null?this.getDataSource().getPrimaryKeyFieldNames():[]);for(var i=0;i<_12.length;i++){if(_2[_12[i]]!=_7[_12[i]]){this.$53d=true;break}}}
if(!this.$53d)for(var i=0;i<_10.length;i++){var _13,_9=_10[i];if(_2[_9]!==_13&&!this.fieldValuesAreEqual(_9,_2[_9],_7[_9])){this.$53d=true;break}}
if(!this.$53d){var _14=this.getDataSource().getPrimaryKeyFieldNames()[0];var _15=this.data.find(_14,_7[_14]);isc.addProperties(_15,_7)}}}
if(this.$53d&&!this.$525&&(!isc.isA.ResultSet(this.data)||this.data.lengthIsKnown()))
{this.$53d=false;this.$52v=this.getSelectedState(true);this.regroup();if(this.$52v){this.setSelectedState(this.$52v);delete this.$52v}}
if(this.body)this.body.finishRowAnimation();if(!this.$525&&!this.suppressEditRowRemap)this.$52s();this.$51e();if(this.$52t()&&!this.$49j){this.startEditing(null,null,true,null,true)}
var _16=this.getTotalRows()-1;if(this.body){if(this.body.lastOverRow>_16)delete this.body.lastOverRow;if(this.body.lastMouseOverRow>_16)delete this.body.lastMouseOverRow;if(this.body.$526>_16)delete this.body.$526}
if(this.$527>_16)delete this.$527;if(this.hilites)this.applyHilites();if(!this.$528){if(_6)this.updateFieldWidthsForAutoFitValue(this.$49s);this.$52w(this.$49s);if(this.summaryRow&&this.showGridSummary)this.summaryRow.$52u()}
if(this.$52v&&(!isc.isA.ResultSet(this.data)||this.data.lengthIsKnown()))
{this.setSelectedState(this.$52v);delete this.$52v}
delete this.$524}
,isc.A.$522=function isc_ListGrid__dataArrived(_1,_2){var _3=this.$4h();if(_3!=null&&_3!=-1){var _4=this.getFieldNum(_3),_5=this.getField(_4);if(_5&&_5.canSortClientOnly&&!this.$50j(_5)){this.$53e(null);if(_3!=null&&this.header&&isc.isA.Toolbar(this.header)){this.header.deselectButton(_3);var _6=this.header.getButton(_3);if(_6)_6.setTitle(this.getHeaderButtonTitle(_6))}
if(this.sorter)this.sorter.setTitle(this.sorter.getTitle())}}
if(this.getCurrentCheckboxField()){var _7=this.getCheckboxFieldPosition(),_5=this.getField(_7),_8=this.checkboxFieldFalseImage||this.booleanFalseImage;if(isc.ResultSet&&isc.isA.ResultSet(this.data)&&!this.data.allMatchingRowsCached())
{var _9={disabled:true,showHover:true,prompt:this.selection.selectionRangeNotLoadedMessage,title:this.canSelectAll==false?"&nbsp;":this.getValueIconHTML(_8.replace(".","_Disabled."),_5)}
this.setFieldProperties(_7,_9)}else{var _9={disabled:false,showHover:false,prompt:null,title:this.canSelectAll==false?"&nbsp;":this.getValueIconHTML(_8,_5)}
this.setFieldProperties(_7,_9)}}
if(isc.screenReader&&this.body!=null){if(isc.isA.Tree(this.data)){var _10=_1;if(this.data.isOpen(_10)&&this.data.hasChildren(_10)){var _11=this.data.getChildren(_10);if(_11&&_11.length>0)_10=_11[0]}
var _12=this.data.indexOf(_10);this.body.$47u(_12,!this.hasFocus)}}
this.dataArrived(_1,_2)}
,isc.A.dataArrived=function isc_ListGrid_dataArrived(_1,_2){}
,isc.A.$52q=function isc_ListGrid__ignoreData(_1){if(this.body)this.body.finishRowAnimation();if(isc.isA.Tree(this.data))this.ignore(_1,"changeDataVisibility");this.ignore(_1,"dataChanged");if(this.isObserving(_1,"dataArrived")){this.ignore(_1,"dataArrived")}
if(this.selection)this.selection.deselectAll()}
,isc.A.applyFieldDefaults=function isc_ListGrid_applyFieldDefaults(_1){if(_1==null)return;for(var i=0;i<_1.length;i++){var _3=_1[i];if(_3==null)continue;if(!this.allowNamelessFields&&_3[this.fieldIdProperty]==null){if(_3.dataPath==null){this.logWarn("unable to process field with no name / dataPath:"+this.echo(_3));continue}
_3[this.fieldIdProperty]="field"+this.$51g++}
var _4=this.isRTL()?isc.Canvas.RIGHT:isc.Canvas.LEFT;var _5=this.getFieldDisplayType(_3);var _6=(_5!=null?isc.SimpleType.getBaseType(_5):null);if(isc.SimpleType.inheritsFrom(_5,"image")){_3.$53f=this.$50y}else if(_6==this.$ia){if(_3.width==null&&_3.length!=null){if(_3.length<15&&!_3.valueMap){_3.width=_3.length*7}}}else if(_6=="integer"||_6=="float"){_4=isc.Canvas.RIGHT
_3.$53f=this.$50u}else if(_6=="date"){var _7=(this.canEdit==true&&_3.canEdit!=false)||(this.canEdit!=false&&_3.canEdit==true);_3.width=_3.width||(_7?100:80);_4=isc.Canvas.RIGHT;_3.$53f=this.$50s}else if(_6=="time"){_3.width=_3.width||80;_3.$53f=this.$50v;_4=isc.Canvas.RIGHT}else if(_5=="binary"||_5=="blob"||_5=="upload"||_5=="imageFile"){_3.$53f=this.$50w}else if(_5=="link"){_3.$53f=this.$50x}else if(_5=="icon"){if(_3.width==null&&_3.autoFitWidth==null){if(this.autoFitIconFields!="none"){_3.autoFitWidth=true;_3.autoFitWidthApproach=(this.autoFitIconFields=="title")?"both":"value"}}
if(_3.width==null&&_3.autoFitWidth==null){if(this.autoFitIconFields!="none"){_3.autoFitWidth=true;_3.autoFitWidthApproach=(this.autoFitIconFields=="title")?"both":"value";_3.width=this.getDefaultFieldWidth(_3)}}
_3.align=_3.align||"center";_3.$53f=this.$50z;_3.title=_3.title||"&nbsp;"}else if(_5=="boolean"||_5=="checkbox"){if(_3.canToggle==null)_3.canToggle=true}
if(_3.formatCellValue!=null&&!isc.isA.Function(_3.formatCellValue))
isc.Func.replaceWithMethod(_3,"formatCellValue","value,record,rowNum,colNum,grid");if(this.showValueIconOnly(_3)){_4=isc.Canvas.CENTER;if(_3.width==null&&_3.autoFitWidth==null){if(this.autoFitIconFields!="none"){_3.autoFitWidth=true;_3.autoFitWidthApproach=(this.autoFitIconFields=="title")?"both":"value";_3.width=this.getDefaultFieldWidth(_3)}}}
if(!_3.align)_3.align=_4;if(_3.multiple&&_3.validateEachItem==null)_3.validateEachItem=true}}
,isc.A.getDisplayField=function isc_ListGrid_getDisplayField(_1){var _2=_1.displayField;if(_2!=null){var _3=_1.optionDataSource?isc.DataSource.get(_1.optionDataSource):null;if(_3!=null)_2=_3.getField(_2);else{_2=this.getField(_2);if(_2==null&&this.dataSource!=null){_2=this.getDataSource().getField(_1.displayField)}}}
return _2}
,isc.A.getFieldDisplayType=function isc_ListGrid_getFieldDisplayType(_1){var _2=this.getDisplayField(_1),_3;if(_2!=null)_3=_2.type;if(_3==null)_3=_1.type;return _3}
,isc.A.$53g=function isc_ListGrid__formatBooleanFieldAsImages(_1){if(this.booleanTrueImage==null&&this.booleanFalseImage==null)return false;var _2=this.getFieldDisplayType(_1),_3=(_2!=null?isc.SimpleType.getBaseType(_2):null);if(_3!=this.$if)return false;return(!_1.suppressValueIcon&&_1.showValueIconOnly==null&&_1.valueIcons==null&&_1.formatCellValue==null)}
,isc.A.setFieldProperties=function isc_ListGrid_setFieldProperties(_1,_2){var _3,_4=this.getAllFields();var _5=_1;if(isc.isA.Number(_1)){_3=this.getField(_1)}else{var _6=isc.Class.getArrayItemIndex(_1,_4,this.fieldIdProperty);_3=_4[_6];_1=this.getFieldNum(_3)}
if(!_3)return;isc.addProperties(_3,_2);if(this.header!=null&&this.header.isDrawn()){var _7=this.getFieldHeader(_1),_8=_7.getMember(this.getLocalFieldNum(_1));if(_8)_8.setProperties(_2)}}
,isc.A.setFieldTitle=function isc_ListGrid_setFieldTitle(_1,_2){this.setFieldProperties(_1,{title:_2})}
,isc.A.setAutoComplete=function isc_ListGrid_setAutoComplete(_1){this.autoComplete=_1}
,isc.A.setFieldAutoComplete=function isc_ListGrid_setFieldAutoComplete(_1,_2){_1=this.getField(_1);if(_1)_1.autoComplete=_2}
,isc.A.showFields=function isc_ListGrid_showFields(_1,_2){return this.showField(_1,_2)}
,isc.A.showField=function isc_ListGrid_showField(_1,_2){arguments.$de=this;if(!isc.isAn.Array(_1)){_1=[_1]}
var _3=true,_4=true;var _5=this.completeFields!=null,_6=(!_5||this.frozenFields||this.$52x);for(var i=0;i<_1.length;i++){var _8=_1[i],_9=_8;_9=this.getSpecifiedField(_9);if(_9==null){_1[i]=null;this.logWarn("showField(): unable to find field object for field: "+_8+". Taking no action. Call setFields() to add new fields.")
continue}
_3=false;if(_9.showIf!=null)_9.showIf=null;if(_9.frozen)_6=true;if(this.spanMap&&this.spanMap[_9.name]!=null)_6=true;if(_6)continue;if(this.fields.contains(_9)){_1[i]=null;continue}
_4=false;_1[i]=_9}
if(_6){this.setFields(this.completeFields||this.fields);this.handleFieldStateChanged();return}
if(_3||_4)return;this.deriveVisibleFields();_1.removeEmpty();var _10=[],_11=0;for(var i=0;i<this.fields.length;i++){var _12=_1.indexOf(this.fields[i]);if(_12!=-1){_10[_12]=i;_11++;if(_11==_1.length)break}}
var _13=this.header;if(_13!=null){if(!_2)this.header.hPolicy="fill";this.header.addButtons(_1.duplicate(),_10)}
if(this.body){if(this.$49j){var _14=this.getEditRow(),_15=this.getRecord(_14),_16=this.getEditedRecord(_14),_17=false,_18=this.getEditForm().items,_19=_18.length-1,_20=_18[_19],_21=_20.colNum;_10.sort();for(var i=_10.length-1;i>=0;i--){var _22=i+1,_23=_10[i],_24=(_23-i);if(!_17&&this.$504>=_23){this.$504+=_22}
var _9=this.fields[_23],_25=this.getEditFormItemFieldWidths(_15)[_23],_26;while(_20!=null&&_21>=_24){_20.colNum+=_22;_19--;_20=(_19>=0)?_18[_19]:null;_21=(_20!=null)?_20.colNum:null}
var _27=this.body.getDrawArea();if(!this.editByCell&&_23>=_27[2]&&_23<=_27[3]){_26=this.getEditItem(_9,_15,_16,_14,_23,_25)}
if(_26!=null){this.$6l.addItems([_26],_19+1)}}}
this.body.fields=this.normalFields||this.fields;this.setBodyFieldWidths(this.getFieldWidths());this.$53h(this.body);if(this.body.isDrawn())this.body.redraw("show field")}
if(this.sortField!=null){this.sortFieldNum=null;this.sortFieldNum=this.$4h()}
if(this.filterEditor!=null)this.filterEditor.showField(_1,_2);this.recalculateSummaries();if(this.summaryRow!=null&&this.showGridSummary){this.summaryRow.showField(_1,_2)}
this.markForRedraw("showField");this.handleFieldStateChanged()}
,isc.A.hideFields=function isc_ListGrid_hideFields(_1,_2){return this.hideField(_1,_2)}
,isc.A.hideField=function isc_ListGrid_hideField(_1,_2){arguments.$de=this;var _3=true,_4=true;if(!isc.isAn.Array(_1)){_1=[_1]}
var _5=[];var _6=(this.completeFields==null||this.frozenFields);var _7=this.$49j,_8=_7?this.getEditRow():null,_9=_7?this.getEditCol():null,_10=false;for(var i=0;i<_1.length;i++){var _12=_1[i],_13=_12;_13=this.getSpecifiedField(_13);if(_13==null){this.logWarn("hideField(): unable to find field object for field: "+_12+". Taking no action. To add this field use the setFields() method.");_1[i]=null;continue}
_3=false;_13.showIf=this.$3i;if(!this.fields.contains(_13)){_1[i]=null;continue}
_4=false;if(_6)continue;var _14=this.fields.indexOf(_13),_15=this.getFieldName(_14);_5.add(_14);if(_7){if(_9==_14)_10=true;var _16=this.getEditFormItem(_15);if(_16&&_16.hasFocus)_16.blurItem();this.clearEditValue(_8,_14,true)}}
if(_3||_4)return;if(_6){this.setFields(this.completeFields||this.fields);this.handleFieldStateChanged();return}
_1.removeEmpty();if(_7){if(_10){if(this.editByCell){this.cancelEditing(isc.ListGrid.PROGRAMMATIC);_7=false}else{var _17=_9-1,_18=false;while(_17>=0){if(!_5.contains(_17)&&this.canEditCell(_8,_17)&&this.$53i(_8,_17))
{_18=true;break}
_17--}
if(!_18){_17=_9+1;while(_17<this.fields.length){if(!_5.contains(_17)&&this.canEditCell(_8,_17)&&this.$53i(_8,_17))
{_18=true;break}
_17++}}
if(!_18){this.cancelEditing(isc.ListGrid.PROGRAMMATIC);_7=false}else{this.$53j(_8,_17,!this.getEditForm().hasFocus)}}}}
this.deriveVisibleFields();var _19=this.header;if(_19!=null){if(!_2)this.header.hPolicy="fill";var _20=[];for(var i=0;i<_5.length;i++){var _14=_5[i];var _21=this.header.getButton(_14);_20[_20.length]=_21;if(this.headerMenuButton&&this.headerMenuButton.masterElement==_21){this.headerMenuButton.depeer()}}
this.header.removeButtons(_20.duplicate());_20.map("destroy")}
var _22=[];if(_7){_5.sort();var _23=this.$6l,_24=_23.getItems(),_25=_24.length-1,_26=_24[_25],_27=_26.colNum,_28=false;for(var i=_5.length-1;i>=0;i--){var _29=i+1,_30=_5[i];if(!_28&&this.$504>_30){this.$504-=_29;_28=true}
while(_26!=null&&_27>=_30){if(_27==_30)_22.add(_26);else _26.colNum-=_29;_25--;_26=(_25>=0)?_24[_25]:null;_27=(_26!=null)?_26.colNum:null}}}
if(this.body){this.body.fields=this.normalFields||this.fields;this.setBodyFieldWidths(this.getFieldWidths());this.$53h(this.body);if(this.body.isDrawn())this.body.redraw("hide field")}
if(_7&&_22.length>0){for(var i=0;i<_22.length;i++){var _26=_22[i];this.$6l.removeItems([_26])}}
if(this.sortField!=null){this.sortFieldNum=null;this.sortFieldNum=this.$4h()}
if(this.filterEditor!=null)this.filterEditor.hideField(_1,_2);if(this.summaryRow&&this.showGridSummary){this.summaryRow.$52u();this.summaryRow.hideField(_1,_2)}
this.$51e();this.handleFieldStateChanged()}
);isc.evalBoundary;isc.B.push(isc.A.fieldIsVisible=function isc_ListGrid_fieldIsVisible(_1){var _2=_1;if(!isc.isAn.Object(_2))_2=this.getSpecifiedField(_1);return this.fields.contains(_2)}
,isc.A.showActionInPanel=function isc_ListGrid_showActionInPanel(_1){if(_1.name=="editNew")return true;return this.Super("showActionInPanel",arguments)}
,isc.A.setFields=function isc_ListGrid_setFields(_1){if(isc.$dd)arguments.$de=this;if(_1!=null&&this.fields!=null&&this.fields!=_1&&this.completeFields!=_1)
{delete this.fields.$52y}
if(!_1&&this.getDataSource()&&!this.getDataSource().hasFields()){this.logWarn("ListGrid.setFields() : neither this ListGrid nor its dataSource have fields")}
if(!this.booleanFalseImage&&!this.booleanTrueImage&&!this.booleanPartialImage){this.booleanTrueImage=isc.CheckboxItem?isc.CheckboxItem.getInstanceProperty("checkedImage"):null;this.booleanFalseImage=isc.CheckboxItem?isc.CheckboxItem.getInstanceProperty("uncheckedImage"):null;this.booleanPartialImage=isc.CheckboxItem?isc.CheckboxItem.getInstanceProperty("partialSelectedImage"):null;this.booleanImageWidth=isc.CheckboxItem?isc.CheckboxItem.getInstanceProperty("valueIconWidth"):null;this.booleanImageHeight=isc.CheckboxItem?isc.CheckboxItem.getInstanceProperty("valueIconHeight"):null}
var _2=this.$4h(),_3=(_2!=null&&this.fields?this.fields[_2]:null);var _4=this.$49j,_5=this.getEditRow(),_6=this.getEditCol(),_7=this.$6l,_8=this.fields?this.getEditFieldName():null;if(this.completeFields==null)this.fields=[];this.completeFields=this.bindToDataSource(_1,this.canPickOmittedFields);if(this.completeFields==null)this.completeFields=[];if(this.shouldShowRowNumberField()){var _9=this.getCurrentRowNumberField(),_10=this.getRowNumberFieldPosition(),_11=!_9;if(_11&&_1&&_1.find(this.fieldIdProperty,"$50k")!=null)
{_11=false}
if(_11)_9=this.getRowNumberField();if(_11)this.completeFields.addAt(_9,_10);else this.completeFields.slideList([_9],_10)}else{var _9=this.getCurrentRowNumberField();if(_9)this.completeFields.remove(_9)}
if(this.shouldShowExpansionField()){var _12=this.getCurrentExpansionField(),_13=this.getExpansionFieldPosition(),_11=!_12;if(_11)_12=this.getExpansionField();if(_11)this.completeFields.addAt(_12,_13);else this.completeFields.slideList([_12],_13)}else{var _12=this.getCurrentExpansionField();if(_12)this.completeFields.remove(_12)}
if(this.shouldShowCheckboxField()){var _14=this.getCurrentCheckboxField(),_15=this.getCheckboxFieldPosition(),_11=!_14;if(_11&&_1&&_1.find(this.fieldIdProperty,"_checkboxField")){_11=false}
if(_11)_14=this.getCheckboxField();if(_11)this.completeFields.addAt(_14,_15);else this.completeFields.slideList([_14],_15)}else{var _14=this.getCurrentCheckboxField();if(_14)this.completeFields.remove(_14)}
if(this.shouldShowRemoveField()){var _16=this.completeFields.findIndex("isRemoveField",true),_17=(_16>=0)?this.completeFields[_16]:{excludeFromState:true,isRemoveField:true};if(_16==-1&&_1){_16=_1.findIndex("isRemoveField",true)}
if(!_17.$53k){isc.addProperties(_17,this.removeFieldDefaults,this.removeFieldProperties);if(_17.name==null)_17.name="$53l";if(_17.title==null)_17.title=this.removeFieldTitle;if(_17.cellIcon==null&&_17.formatCellValue==null){_17.formatCellValue=function(_32,_33,_34,_35,_36){if(!this.removeIconHTML){this.removeIconHTML=isc.Canvas.imgHTML(_36.removeIcon,_36.removeIconSize);this.unremoveIconHTML=isc.Canvas.imgHTML(_36.unremoveIcon,_36.removeIconSize)}
if(_36.recordMarkedAsRemoved(_34)){return this.unremoveIconHTML}else{return this.removeIconHTML}}}
if(_16==-1){this.completeFields.add(_17)}
_17.$53k=true}}
if(isc.DataSource)this.$53m();if(this.fieldState!=null)this.setFieldState();this.applyFieldDefaults(this.completeFields);this.completeFields.$52l=true;this.completeFields.setProperty("$52m",true);this.deriveVisibleFields();if(this.fields.length==0&&this.completeFields.length>0){this.logWarn("All specified fields for this component are hidden. Note that fields "+"may be hidden via 'showIf' or 'detail' attribute values. "+"In order to display data this grid must have some visible fields.")}
this.$45p=null;var _18,_19,_20;if(_4){this.storeUpdatedEditorValue();_18=this.fields.findIndex(this.fieldIdProperty,_8);if(_18!=-1&&!this.canEditCell(_5,_18))
_18=-1;if(_18==-1){var _21;if(!this.editByCell)_21=this.findNextEditCell(_5,0,1,true,true,false,true);if(_21!=null&&_21[0]==_5){_18=_21[1]}
if(_18==-1){this.cancelEditing(isc.ListGrid.PROGRAMMATIC);_4=false}}else{var _22=_7.getItem(_8);if(_22){_20=_22.hasFocus;if(_20)_7.$20u()}
_19=true}
if(_4)this.hideInlineEditor(false,true)}
var _23=(this.canFreezeFields==null||this.$520)&&this.fixedRecordHeights!=false&&this.fixedFieldWidths!=false&&this.autoFitData!="horizontal"&&this.autoFitData!="both"&&this.bodyOverflow!="visible";if(_23){if(this.completeFields.getProperty("overflow").contains("visible"))_23=false}
if(_23){this.$520=true;this.canFreezeFields=true}else if(this.$520){delete this.$520;this.canFreezeFields=null}
if(this.canSelectCells)this.selection.numCols=this.fields.length;if(_3){var _24=this.fields.indexOf(_3);if(_24==-1)_24=null;this.$53e(_24)}
if(this.showHeader&&this.headerHeight>0&&this.header!=null){this.updateHeader();if(this.body!=null){this.syncHeaderScrolling(this.body.getScrollLeft())}}
this.updateBody();if(this.filterEditor){this.filterEditor.updateDataSource(this.getDataSource());var _25=this.completeFields||[];this.filterEditor.setFields(_25.duplicate())}
this.layoutChildren(this.$51h);if(this.$53n!=null){var _26={};for(var i in this.$53n){if(this.$53n[i]==null)continue;var _28=this.$53n[i].$53o,_29=(this.dataSource!=null?this.getDataSource().getPrimaryKeyFieldNames():[]);for(var _30 in _28){if(!this.fields.containsProperty(this.fieldIdProperty,_30)&&!_29.contains(_30)){_26[_30]=true;this.clearEditValue(_28,_30,true)}}}
_26=isc.getKeys(_26);if(_26.length>0){this.logInfo("'setFields()' removed the following fields which had pending edit "+"values for some row[s]: '"+_26.join("', '")+"'.\n"+"Edit values for these fields have been dropped.","gridEdit")}}
if(_4){if(_19){this.showInlineEditor(_5,_18,false,false,true);if(_20)_7.$21l(_31)}else{this.$53j(_5,_18,!_20)}
if(_20){var _31=this.getEditFieldName();if(_31==_8){_7.$21l(_31)}else{_7.focusInItem(_31)}}}
if(this.$4h()!=null&&this.canSort)this.resort();if(this.summaryRow&&this.showGridSummary){this.summaryRow.setFields(this.completeFields.duplicate());this.summaryRow.$52u()}}
,isc.A.addField=function isc_ListGrid_addField(_1,_2){return this.Super("addField",[_1,_2,this.completeFields],arguments)}
,isc.A.removeField=function isc_ListGrid_removeField(_1){return this.Super("removeField",[_1,this.completeFields],arguments)}
,isc.A.shouldShowCheckboxField=function isc_ListGrid_shouldShowCheckboxField(){if(this.fieldSourceGrid)return this.fieldSourceGrid.shouldShowCheckboxField();return(this.selectionAppearance==this.$22d&&this.selectionType!=this.$pr&&!isc.isA.TreeGrid(this))}
,isc.A.focusInFilterEditor=function isc_ListGrid_focusInFilterEditor(_1){if(this.filterEditor==null)return;var _2=_1!=null?this.getColNum(_1):null;this.filterEditor.startEditing(0,_2)}
,isc.A.filterByEditor=function isc_ListGrid_filterByEditor(){if(this.filterEditor!=null)this.filterEditor.performAction()}
,isc.A.bindToDataSource=function isc_ListGrid_bindToDataSource(_1,_2,_3,_4,_5,_6){var _7=false;var _8=this.invokeSuper(isc.ListGrid,"bindToDataSource",_1,_2,_3,_4,_5,_6);if(this.showDetailFields&&_8!=null){for(var i=0;i<_8.length;i++){var _10=_8[i];if(_10.showIf==null&&_10.detail==true){_10.showIf=this.$3i}
if(isc.isA.Number(parseInt(_10.name))&&parseInt(_10.name).toString()==_10.name)
{_7=true}
_10.$53p=true}}
this.$53q=!_7;return _8}
,isc.A.setFieldState=function isc_ListGrid_setFieldState(_1){if(this.completeFields==null)this.setFields(this.fields);if(_1==null&&this.fieldState!=null){if(isc.isA.String(this.fieldState)){_1=this.evalViewState(this.fieldState,"fieldState")}
this.completeFields=this.$3w(this.fieldState);this.fieldState=null;return}
_1=this.evalViewState(_1,"fieldState")
if(_1){this.completeFields=this.$3w(_1);this.refreshFields()}}
,isc.A.handleFieldStateChanged=function isc_ListGrid_handleFieldStateChanged(){this.fieldStateChanged();this.handleViewStateChanged()}
,isc.A.getCheckboxField=function isc_ListGrid_getCheckboxField(){var _1=this,_2={width:this.$51j+this.$53r(),getAutoFreezePosition:function(){return _1.getCheckboxFieldPosition()}};isc.addProperties(_2,this.checkboxFieldDefaults,this.checkboxFieldProperties);var _3=this.checkboxFieldFalseImage||this.booleanFalseImage;_2.title=(this.canSelectAll==false||this.selectionType=="single"?"&nbsp;":this.getValueIconHTML(_3,_2));return _2}
,isc.A.getCurrentCheckboxField=function isc_ListGrid_getCurrentCheckboxField(){var _1=this.completeFields||this.fields;if(!_1)return null;var _2=_1.find(this.fieldIdProperty,"_checkboxField");return!_2?null:isc.isAn.Array(_2)?_2[0]:_2}
,isc.A.$53r=function isc_ListGrid__getCheckboxFieldImageWidth(){return this.checkboxFieldImageWidth||this.booleanImageWidth||(isc.CheckboxItem?isc.CheckboxItem.getInstanceProperty("valueIconWidth"):null)}
,isc.A.$53s=function isc_ListGrid__getCheckboxFieldImageHeight(){return this.checkboxFieldImageHeight||this.booleanImageHeight||(isc.CheckboxItem?isc.CheckboxItem.getInstanceProperty("valueIconWidth"):null)}
,isc.A.isCheckboxField=function isc_ListGrid_isCheckboxField(_1){if(!_1||!_1.$51i)return false;else return true}
,isc.A.getCheckboxFieldPosition=function isc_ListGrid_getCheckboxFieldPosition(){if(this.fieldSourceGrid)return this.fieldSourceGrid.getCheckboxFieldPosition();if(this.selectionAppearance!="checkbox"||isc.isA.TreeGrid(this))return-1;var _1=0;if(this.showRowNumbers)_1+=1;if(this.showingGroupTitleColumn())_1+=1;if(this.canExpandRecords)_1+=1;return _1}
,isc.A.getSelectedState=function isc_ListGrid_getSelectedState(_1){if(!this.selection)return null;if(!this.dataSource||isc.isAn.emptyObject(this.getDataSource().getPrimaryKeyFields()))
{if(!_1){this.logWarn("can't getSelectedState without a DataSource "+"with a primary key field set")}
return null}
var _2=this.selection.getSelection()||[],_3=[];for(var i=0;i<_2.length;i++){_3[i]=this.getPrimaryKeys(_2[i])}
return isc.Comm.serialize(_3,false)}
,isc.A.setSelectedState=function isc_ListGrid_setSelectedState(_1){_1=this.evalViewState(_1,"selectedState")
if(!_1){if(this.selection)this.selection.deselectAll();return}
var _2=this.selection,_3=this.originalData||this.data;if(isc.ResultSet&&isc.isA.ResultSet(_3)&&!_3.lengthIsKnown())return;if(_3&&_2){_2.deselectAll();var _4=[];for(var i=0;i<_1.length;i++){var _6=_1[i];var _7=_3.findByKeys(_1[i],this.getDataSource());if(_7!=-1)_4.add(_3.get(_7))}
this.selection.selectList(_4)}}
,isc.A.getSortState=function isc_ListGrid_getSortState(){if(this.logIsInfoEnabled("sorting")){this.logInfo("\n"+"grid.sortFieldNum is: "+this.sortFieldNum+"\n"+"grid.sortField is: "+this.sortField+"\n"+"grid.getField(grid.sortFieldNum) is:\n"+isc.echoAll(this.getField(this.sortFieldNum))+"\n"+"-----------------------------------------\n"+"grid.$4h() is: "+this.$4h()+"\n"+"grid.getField(grid.$4h()) is:\n"+isc.echoAll(this.getField(this.$4h()))+"\n"+"","sorting")}
var _1=this.$4h(),_2=(_1!=null?this.getField(_1):null),_3=_2!=null?this.getFieldName(_2):null,_4=_2&&_2.sortDirection!=null?_2.sortDirection:this.sortDirection,_5={fieldName:_3,sortDir:_4};if(this.$32&&this.$32.length>0){var _6=isc.shallowClone(this.$32);_6.clearProperty("primarySort");_6.clearProperty("sortIndex");_6.clearProperty("normalizer");_6.clearProperty("context");_5.sortSpecifiers=_6}
return"("+isc.Comm.serialize(_5,false)+")"}
,isc.A.setSortState=function isc_ListGrid_setSortState(_1){_1=this.evalViewState(_1,"sortState")
if(!_1){this.clearSort();return}
if(_1.sortSpecifiers){this.setSort(isc.shallowClone(_1.sortSpecifiers))}else if(_1.fieldName==null){this.clearSort()}else{var _2=this.getFieldNum(_1.fieldName)
if(_2!=-1)this.sort(_2,_1.sortDir)}}
,isc.A.getViewState=function isc_ListGrid_getViewState(_1){var _2={selected:this.getSelectedState(true),field:this.getFieldState(),sort:this.getSortState(),hilite:this.getHiliteState(),group:this.getGroupState()};if(_1)return _2;return"("+isc.Comm.serialize(_2,false)+")"}
,isc.A.getGroupState=function isc_ListGrid_getGroupState(){var _1=this.getGroupByFields();if(_1==null)_1="";else _1=_1.join(",");return _1}
,isc.A.setGroupState=function isc_ListGrid_setGroupState(_1){if(_1)this.groupBy(_1.split(","))
else this.ungroup()}
,isc.A.setViewState=function isc_ListGrid_setViewState(_1){_1=this.evalViewState(_1,"viewState")
if(!_1)return;if(_1.field)this.setFieldState(_1.field);this.setSortState(_1.sort);this.setGroupState(_1.group);this.setHiliteState(_1.hilite);this.setSelectedState(_1.selected)}
,isc.A.handleViewStateChanged=function isc_ListGrid_handleViewStateChanged(){this.fireOnPause("viewStateChangeNotification",{target:this,methodName:"viewStateChanged"},0)}
,isc.A.getViewStateChangedFunction=function isc_ListGrid_getViewStateChangedFunction(){if(this.$53t==null){var _1=this;this.$53t=function(){if(_1.destroyed)return;_1.viewStateChanged()}}
return this.$53t}
,isc.A.viewStateChanged=function isc_ListGrid_viewStateChanged(){}
,isc.A.setDataSource=function isc_ListGrid_setDataSource(_1,_2){var _3=this.getDataSource();if(_3!=null&&_3!=_1&&_3.ID!=_1){var _4=this.getGroupByFields();if(_4!=null&&_4.length>0&&_4[0]!=null&&this.originalData!=null)
{this.ungroup()}}
this.Super("setDataSource",arguments);this.clearFilterValues();this.discardAllEdits();if(!this.data.setSort){this.canMultiSort=false}else if(this.canMultiSort!=false){_1=this.getDataSource();this.canMultiSort=(_1&&_1.canMultiSort!=null?_1.canMultiSort:true)&&this.canSort}}
,isc.A.deriveVisibleFields=function isc_ListGrid_deriveVisibleFields(){this.fields.setArray(this.getVisibleFields(this.completeFields));this.deriveFrozenFields();this.refreshMasterIndex()}
,isc.A.refreshFields=function isc_ListGrid_refreshFields(){this.setFields(this.completeFields)}
,isc.A.getFieldWidths=function isc_ListGrid_getFieldWidths(_1){var _2=this.$53u();if(this.autoFitFieldWidths&&!this.$53v){this.$53v=true;var _3=_2.duplicate(),_4=null;if(this.frozenFields!=null){var _5=this.freezeLeft;if(_5){_4=_3.slice(0,this.frozenFields.length);_3=_3.slice(this.frozenFields.length)}else{_4=_3.slice(this.frozenFields.length);_3=_3.slice(0,this.frozenFields.length)}}
var _6=this.getAvailableFieldWidth(true),_7=_3.sum();var _8=_6;if(_4!=null)_8-=_4.sum();var _9=true;if(this.autoFitWidthApproach!="title"){var _10=this.getDrawArea();if(!this.data||Array.isLoading(this.data.get(_10[0]))){_9=false}}
if(_7<_8&&_9){var _11=this.getAutoFitExpandField();if(_11){var _12=this.getFieldNum(_11);var _13=_8-_7;_2[_12]+=_13;var _14=this.getFieldHeaderButton(_12);if(_14&&_14.isDrawn()){_14.setWidth(_2[_12])}}}else if(_7>_8&&this.autoFitClipFields!=null){var _15;if(this.header&&this.header.isDrawn()){_15=this.header.hPolicy;this.header.hPolicy="fill"}
var _16=this.autoFitClipFields;for(var i=0;i<_16.length;i++){var _18=this.getField(_16[i]),_19=this.getFieldNum(_18);if(_18==null||_19<0)continue;if(!this.$52x&&_18.frozen){this.logInfo("auto-fitting field:"+_18.name+" is present in the autoFitClipFields array for this grid, but is"+" currently frozen. This is not supported - the field will not be clipped.","frozenFields");continue}
delete _18.$521;var _20=this.getFieldHeader(_19);if(_20&&_20.isDrawn()){_14=_20.getMember(this.getLocalFieldNum(_19));_14.setWidth(_18.width||"*");_14.setOverflow("hidden")}}
if(this.header&&this.header.isDrawn()){var _21=this.$51k;if(_1!=null)_21+=_1;this.header.reflowNow(_21);this.header.hPolicy=_15;if(this.frozenHeader){this.frozenHeader.hPolicy="fill";this.frozenHeader.reflowNow(_21);this.frozenHeader.hPolicy=_15}}
_2=this.$53u()}
this.$53v=false}
return _2}
,isc.A.$53u=function isc_ListGrid__getCalculatedFieldWidths(){var _1=this.header;if(isc.isA.Layout(_1)&&_1.isDrawn()){var _2=_1.members;for(var i=0;i<_2.length;i++){if(_2[i].isDirty())_2[i].redraw();if(_2[i].label!=null&&_2[i].label.isDirty())_2[i].label.redraw()}
var _4=_1.getMemberSizes();if(_4.length>0){if(this.allowMismatchedHeaderBodyBorder){var _5=_1.getLeftBorderSize()+_1.getLeftMargin(),_6=_1.getRightBorderSize()+_1.getRightMargin();if(_5!=0){_4[0]+=_5}}
var _7=_4.sum(),_8=_1.getInnerWidth();if(this.allowMismatchedHeaderBodyBorder){var _9=_7-_8;if(_9>0){_4[_4.length-1]+=Math.min(_9,_6)}}}
if(this.frozenFields){var _10=this.frozenHeader.getMemberSizes();_4.addListAt(_10,this.freezeLeft()?0:_4.length)}}else{var _4=this.getStretchResizeWidths()}
return _4}
,isc.A.getStretchResizeWidths=function isc_ListGrid_getStretchResizeWidths(){if(this.fields==null)return[];var _1=this.fields.getProperty("width"),_2=this.fields.getProperty("$521");for(var i=0;i<_1.length;i++){if(_2[i]!=null)_1[i]=_2[i]}
return isc.Canvas.applyStretchResizePolicy(_1,(this.innerWidth!=null?this.innerWidth:this.getAvailableFieldWidth()),this.minFieldSize)}
,isc.A.getAvailableFieldWidth=function isc_ListGrid_getAvailableFieldWidth(_1){if(_1==null){_1=this.autoFitData!="both"&&this.autoFitData!="horizontal"}
var _2=(!_1?this.getVisibleWidth():this.getWidth())-this.getHMarginBorderPad();var _3=this.$53w();if(_3){_2-=this.body?this.body.getScrollbarSize():this.getScrollbarSize()}
return _2}
,isc.A.getFieldWidth=function isc_ListGrid_getFieldWidth(_1){_1=this.getFieldNum(_1);if(_1==-1||!this.fields||_1>=this.fields.length)return null;if(this.body!=null)return this.getColumnWidth(_1);return this.getFieldWidths()[_1]}
,isc.A.$53x=function isc_ListGrid__adjustFieldSizesForBodyStyling(_1,_2){if(_2==null)_2=false;if(_1==null||_1.length==0)return _1;if(!this.body){return _1}
_1[_1.length-1]=this.$53y(_1[_1.length-1],_1.sum(),_2);_1[0]=this.$53z(_1[0],_2);return _1}
,isc.A.$53z=function isc_ListGrid__adjustFirstFieldForBodyStyling(_1,_2){if(!this.body)return _1;var _3=(_2?this.body.getTopBorderSize()+this.body.getTopMargin():this.body.getLeftBorderSize()+this.body.getLeftMargin());if(_3!=0)_1-=_3;return Math.max(0,_1)}
,isc.A.$53y=function isc_ListGrid__adjustLastFieldForBodyStyling(_1,_2,_3){if(!this.body)return _1;var _4=(_3?this.body.getBottomBorderSize()+this.body.getBottomMargin():this.body.getRightBorderSize()+this.body.getRightMargin());if(_4!=0){var _5=_2-
(_3?this.body.getInnerHeight():this.body.getInnerWidth());if(_5>0){_1-=Math.min(_5,_4)}}
return Math.max(_1,1)}
,isc.A.setBodyFieldWidths=function isc_ListGrid_setBodyFieldWidths(_1){this.$45p=_1;var _2=_1;if(this.allowMismatchedHeaderBodyBorder){if(isc.isAn.Array(_1)){_1=this.$53x(_1.duplicate())}}
var _3=this.frozenFields;if(_3){var _4=this.getFrozenSlots(_1);_1=this.getUnfrozenSlots(_1);this.frozenBody.setColumnWidths(_4);var _5=_4.sum();this.frozenBody.setWidth(_5);this.frozenBody.$ts=_5;if(this.frozenHeader)this.frozenHeader.setWidth(_5)}
if(this.body!=null)this.body.setColumnWidths(_1);if(this.$49j){var _6=this.$6l.getItems(),_7=this.getRecord(this.getEditRow()),_8=this.getEditFormItemFieldWidths(_7);for(var i=0;i<_6.length;i++){var _10=_6[i].colNum;if(_6[i].width!=_8[_10])
_6[i].setWidth(_8[_10])}}}
,isc.A.getGridMembers=function isc_ListGrid_getGridMembers(){var _1=this.gridComponents,_2=[],_3;for(var i=0;i<_1.length;i++){var _5=_1[i],_6=null;if(isc.isA.Canvas(_5)){_6=_5}else if(isc.isA.String(_5)){if(!this.shouldShowGridComponent(_5))continue;switch(_5){case"filterEditor":if(this.filterEditor==null)this.makeFilterEditor();_6=this.filterEditor;break;case"header":if(this.header==null){this.makeHeader()}
_6=this.headerLayout||this.header;break;case"body":_3=true;if(this.body==null){this.createBodies()}
_6=this.bodyLayout||this.body;break;case"summaryRow":_6=this.getSummaryRow();break}}
if(_5!=null&&_6==null){_6=this.createCanvas(_5)}
_2.add(_6)}
if(!_3){this.logWarn("ListGrid specified with gridComponents:"+_1+".  This does not include a \"body\" entry. ListGrids with no body are unsupported,"+" displaying the body as the last member in the grid.");_2[_2.length]=this.createBodies()}
return _2}
,isc.A.shouldShowGridComponent=function isc_ListGrid_shouldShowGridComponent(_1){if(_1==this.$51l)return true;if(_1==this.$51m&&this.headerHeight==0){return false}
var _2=this.showComponentPropertyMap[_1];if(_2==null){this.showComponentPropertyMap[_1]=_2="show"+_1.substring(0,1).toUpperCase+_1.substring(1)}
return this[_2]}
,isc.A.createChildren=function isc_ListGrid_createChildren(){this.updateGridComponents();if(isc.Browser.isMoz&&isc.Browser.geckoVersion>=20051111){if(this.header){this.body.mozOutlineOffset="0px";if(this.body.mozOutlineColor==null)
this.body.mozOutlineColor=this.mozBodyOutlineColor}else{if(this.body.mozOutlineColor==null)
this.body.mozOutlineColor=this.mozBodyNoHeaderOutlineColor;this.body.mozOutlineOffset="-1px"}}
this._useNativeTabIndex=false}
,isc.A.updateGridComponents=function isc_ListGrid_updateGridComponents(){this.setMembers(this.getGridMembers())}
,isc.A.layoutChildren=function isc_ListGrid_layoutChildren(_1,_2,_3){if(this.body!=null){this.$49c(_1,_2,_3)}
isc.VLayout.$ce.layoutChildren.call(this,_1,_2,_3);if(this.body!=null){if(this.frozenBody){var _4=this.shrinkForFreeze&&this.body.hscrollOn;var _5=this.bodyLayout.getInnerHeight();if(_4)_5-=this.body.getScrollbarSize();this.frozenBody.setHeight(_5);if(this.frozenBody.$456())this.frozenBody.markForRedraw("height changed");this.frozenBody.$tt=_5}}}
,isc.A.$49c=function isc_ListGrid__updateFieldWidths(_1,_2,_3){if(this.body==null)return;this.$45p=null;if(this.fields&&!this.fields.$52y&&!this.skipAutoFitWidths){var _4=this.getAutoFitValueWidths(null,true);if(_4==null){this.fields.setProperty("$521",null)}else{for(var i=0;i<this.fields.length;i++){var _6=this.fields[i];if(_4[i]==null){_6.$521=null;continue}
var _7=_6.width;if(!isc.isA.Number(_7))_7=this.minFieldWidth;if(!isc.isA.Number(_7))_7=1;if(_7<_4[i]){_6.$521=_4[i];var _8=this.getFieldHeaderButton(i);if(_8!=null){_8.setWidth(_4[i]);_8.parentElement.reflow()}}else if(_6.$521!=null){_6.$521=null;var _8=this.getFieldHeaderButton(i);if(_8!=null){_8.setWidth(_7);_8.parentElement.reflow()}}}
this.fields.$52y=true}}
var _9=this.getAvailableFieldWidth(true),_10=(_9!=this.innerWidth);this.innerWidth=_9;var _11=this.header,_12=(this.showHeader?this.headerHeight:0);if(_11!=null){var _13=(this.isRTL()&&this.$53w()?this.body.getScrollbarSize():0);_11.hPolicy="fill";if(this.frozenHeader)this.frozenHeader.hPolicy="fill";var _14=this.innerWidth,_15;if(this.autoFitData=="horizontal"||this.autoFitData=="both"){_15=this.getFieldWidths(_1+" [sizing horizontal auto-fit header]");_14=Math.max(_14,_15.sum())}
if(!this.leaveScrollbarGap&&_11.isDrawn()&&_14!=_11.getWidth()&&_1=="body scroll changed"){if(this.$530||!this.resizeFieldsForScrollbar){_11.hPolicy="none"}}
var _16=this.headerLayout||_11;_16.resizeTo(_14,_12);var _17=(_1=="initial draw");if(!_11.isDrawn()&&(_17||this.isDrawn())){if(!this.frozenFields){if(_17)this.$95(_11);_11.draw()}else{if(_15==null){_15=this.getFieldWidths(_1+" [sizing frozen fields]")}
var _18=this.getFrozenSlots(_15);this.frozenHeader.setWidth(_18.sum());if(_17)this.$95(this.headerLayout);this.headerLayout.draw()}}
if(_11.isDrawn())_11.hPolicy="none";if(this.frozenHeader&&this.frozenHeader.isDrawn())this.frozenHeader.hPolicy="none";if(this.sorter){this.updateSorter()}}
if((!this.$45p||_10)&&((_11&&_11.isDrawn())||_12==0))
{var _15=this.getFieldWidths(_1);this.setBodyFieldWidths(_15);if(this.filterEditor&&this.filterEditor.body!=null){this.filterEditor.setBodyFieldWidths(_15.duplicate())}
if(this.summaryRow&&this.showGridSummary&&this.summaryRow.body!=null){this.summaryRow.setBodyFieldWidths(_15.duplicate())}
if(this.logIsDebugEnabled("layout")){this.logDebug("new field widths: "+this.$45p,"layout")}}}
,isc.A.getAutoFitValueWidths=function isc_ListGrid_getAutoFitValueWidths(_1,_2){if(this.data==null)return;if(isc.isA.ResultSet(this.data)){var _3=this.body.getDrawArea();if(!this.data.rangeIsLoaded(_3[0],_3[1])){if(_1==null){this.updateFieldWidthsForAutoFitValue("Delayed resize pending data load")}
return}}
var _4=(_1==null);if(_1==null){_1=[];for(var i=0;i<this.fields.length;i++){var _6=this.fields[i];if(this.shouldAutoFitField(_6)){if(!_2||this.getAutoFitWidthApproach(_6)!="title"){_1.add(_6);_4=false}}}}
if(_4)return;var _7=[],_8=[];for(var i=0;i<_1.length;i++){var _6=_1[i],_9=this.getFieldNum(_6);if(!this.$52x&&_6.frozen){_7.add(_9)}else{_8.add(_9)}}
var _10=[];for(var i=0;i<this.fields.length;i++){if(_1.contains(this.fields[i])){_10[i]=this.getDefaultFieldWidth(this.fields[i])}}
return _10}
,isc.A.getDefaultFieldWidth=function isc_ListGrid_getDefaultFieldWidth(_1){if(_1.type=="icon"){return(_1.iconWidth||_1.iconSize)+2*this.cellPadding+2*this.iconPadding}else if(this.showValueIconOnly(_1)){return this.getValueIconWidth(_1)+(2*this.cellPadding)+this.getValueIconRightPadding(_1)+this.getValueIconLeftPadding(_1)}
var _2=this.getLocalFieldNum(this.getFieldNum(_1));var _3;if(!this.$52x&&_1.frozen){_3=this.frozenBody.getColumnAutoSize(_2)}else{_3=this.body.getColumnAutoSize(_2)}
if(_3==null)_3=_1.width;return _3}
,isc.A.getAutoFitWidthApproach=function isc_ListGrid_getAutoFitWidthApproach(_1){if(_1.autoFitWidthApproach!=null)return _1.autoFitWidthApproach;return this.autoFitWidthApproach}
,isc.A.draw=function isc_ListGrid_draw(_1,_2,_3,_4){if(isc.$dd)arguments.$de=this;if(!this.readyToDraw())return this;this.prepareForDraw();this.invokeSuper(isc.ListGrid,"draw",_1,_2,_3,_4);this.body.getColumnSizes();for(var i=0;i<this.bodies.length;i++){var _6=this.bodies[i];if(_6.$4p)this.markForRedraw()}
var _7=this.$4h();if(this.header&&_7!=null){var _8=this.getFieldHeader(_7),_9=this.getFieldHeaderButton(_7);_8.selectButton(_9)}
this.bodyScrolled();if(this.sorter)this.updateSorter();return this}
,isc.A.$52t=function isc_ListGrid__alwaysShowEditors(_1,_2){if(!_2&&this.getTotalRows()==0)return false;if(this.alwaysShowEditors)return true;var _3=_1?[_1]:this.fields;if(_3){for(var i=0;i<_3.length;i++){if(_3[i].alwaysShowEditors){return true}}}
return false}
,isc.A.prepareForDraw=function isc_ListGrid_prepareForDraw(){if(this.completeFields==null)this.setFields(this.fields);if(this.getEditRow()==null){var _1=this.$52t();if(_1)this.startEditing(null,null,true,null,true)}
if(this.groupByField){var _2;if(isc.isA.Array(this.groupByField)){_2=this.groupByField}else{_2=[this.groupByField]}
this.groupByField=null;this.groupBy(_2)}
this.createChildren()}
,isc.A.getGroupByFields=function isc_ListGrid_getGroupByFields(){var _1=this.groupByField;if(_1!=null&&!isc.isAn.Array(_1)){_1=[_1]}
return _1}
,isc.A.destroy=function isc_ListGrid_destroy(_1){if(this._dragLine){this._dragLine.destroy();this._dragLine=null}
if(this.$531)this.$531.destroy();if(this.$532)this.$532.destroy();if(this.cellContextMenu)this.cellContextMenu.destroy();if(this.$6l){this.$6l.destroy();delete this.$6l;delete this.$49j}
if(this.data){if(this.data.$393&&isc.isA.Function(this.data.destroy)){this.data.destroy()}else{this.$52q(this.data);delete this.data}}
if(this.selection){this.destroySelectionModel()}
if(this.selectionCanvas)this.selectionCanvas.destroy();if(this.selectionUnderCanvas)this.selectionUnderCanvas.destroy();if(this.rollOverCanvas)this.rollOverCanvas.destroy();if(this.rollUnderCanvas)this.rollUnderCanvas.destroy();this.$533();var _2=this.getRecordComponentPool();if(_2){for(var i=0;i<_2.length;i++){var _4=_2[i];if(_4&&_4.destroy&&!_4.destroyed){if(!_4.dontAutoDestroy)_4.destroy();else _4.deparent()}}}
if(this.$534!=null){for(var _5 in this.$534){var _4=this.$534[_5];if(!_4||_4.destroyed||_4.destroying||_4.$wa)
{continue}
if(!_4.dontAutoDestroy)_4.destroy();else _4.deparent()}}
this.Super("destroy",arguments)}
,isc.A.redrawHeader=function isc_ListGrid_redrawHeader(){if(this.header)this.header.markForRedraw()}
,isc.A.getBaseStyle=function isc_ListGrid_getBaseStyle(_1,_2,_3){if(this.canEdit==true&&!this.isPrinting){if(this.editFailedBaseStyle&&this.cellHasErrors(_2,_3))
return this.editFailedBaseStyle;if(this.editPendingBaseStyle&&this.cellHasChanges(_2,_3,false))
return this.editPendingBaseStyle}
if(_1&&this.recordBaseStyleProperty&&_1[this.recordBaseStyleProperty])
return _1[this.recordBaseStyleProperty];var _4=this.getField(_3);if(_4&&_4.baseStyle)return _4.baseStyle;if(_4&&_4.type=="summary"&&this.recordSummaryBaseStyle)
return this.recordSummaryBaseStyle;if(_4&&_4.frozen&&!this.$52x&&this.frozenBaseStyle){return this.frozenBaseStyle}
if(this.isPrinting&&(this.printBaseStyle!=null))return this.printBaseStyle;var _5=this.baseStyle;if(_5==null){if(this.cellHeight!=this.normalCellHeight||this.fastCellUpdates||!this.shouldFixRowHeight(_1,_2)||(_1!=null&&_1.$4p))
{_5=this.tallBaseStyle}else{_5=this.normalBaseStyle}}
return _5}
,isc.A.getCellCSSText=function isc_ListGrid_getCellCSSText(_1,_2,_3){if(_1){var _4=_1[this.recordCSSTextProperty];if(_4!=null)return _4}
var _4;if(this.recordMarkedAsRemoved(_1)&&this.removedCSSText){_4=this.removedCSSText}else{if(this.isEditable()){if(this.editFailedBaseStyle==null&&this.editFailedCSSText&&this.cellHasErrors(_2,_3))
{_4=this.editFailedCSSText}else if(this.editPendingBaseStyle==null&&this.editPendingCSSText&&this.cellHasChanges(_2,_3,false))
{_4=this.editPendingCSSText}}}
_4=this.getRecordHiliteCSSText(_1,_4,this.getField(_3));return _4}
,isc.A.getRawCellValue=function isc_ListGrid_getRawCellValue(_1,_2,_3,_4){var _5,_6,_7;if(_4){_6=_3
_5=this.completeFields?isc.Class.getArrayItem(_6,this.completeFields,this.fieldIdProperty):this.getField(_6)}else{_5=this.fields[_3];_6=_5[this.fieldIdProperty]}
_7=_5?_5.dataPath:null;if(_6==null&&_7==null)return this.emptyCellValue;var _8,_9,_10;if(this.rowEditNotComplete(_2)){_8=this.$535(_2,_3)}
if(_8!==_10){return _8}else{if(_1==null)return this.emptyCellValue;if(this.data.getFieldValue&&_5){_9=this.data.getFieldValue(_1,_6,_5)}else if(_1.ownerDocument&&_5){_9=isc.xml.getFieldValue(_1,_6,_5)}else{if(_7!=null){_9=isc.Canvas.$3e(this.$536(_7),_1,this,true)}else{_9=_1[_6]}}}
if(this.shouldShowRecordSummary(_5,_1)&&!this.shouldApplyRecordSummaryToRecord(_5))
{_9=this.getRecordSummary(_2,_5)}
if(_5&&_5.getRawCellValue){isc.Func.replaceWithMethod(_5,"getRawCellValue","viewer,record,recordNum,field,fieldNum,value");_9=_5.getRawCellValue(this,_1,_2,_5,_3,_9)}
if(_5){if(_5.userFormula)_9=this.getFormulaFieldValue(_5,_1);if(_5.userSummary)this.getSummaryFunction(_5)}
return _9}
,isc.A.$536=function isc_ListGrid__trimDataPath(_1){if(!_1)return _1;var _2=_1.trim(isc.Canvas.$3a);if(!_2.contains(isc.Canvas.$3a))return _1;var _3=this.getFullDataPath();if(_3==null||_3=="")return _2;_3=_3.trim(isc.Canvas.$3a);var _4=_3.split(isc.Canvas.$3a);var _5=_2.split(isc.Canvas.$3a);for(var i=0;i<_4.length;i++){if(_4[i]!=_5[i]){break}}
if(i==0)return _1;var _7="";for(var j=i;j<_5.length;j++){_7+=_5[j];_7+="/"}
return _7}
,isc.A.shouldShowRecordSummary=function isc_ListGrid_shouldShowRecordSummary(_1,_2){if(_1&&_1.type==this.$51n){if(_2[this.groupSummaryRecordProperty]){return(_1.summaryFunction==null&&_1.getGroupSummary==null)}else if(_2[this.gridSummaryRecordProperty]){return(_1.summaryFunction==null&&_1.getGridSummary==null)}
return true}
return false}
,isc.A.getCellValue=function isc_ListGrid_getCellValue(_1,_2,_3,_4){if(_1==null){if(this.showNewRecordRow&&this.$49h(_2)){return this.getNewRecordRowCellValue()}
_1=this.$507(_2,_3)}else{if(_1[this.isSeparatorProperty])return this.$51o;if(_1.$35){var _5=this.fields[_3],_6=this.getGroupTitleField(),_7;if(_6==null){_7=_5.$50r}else{_7=_5.name==_6}
if(this.singleCellGroupHeaders()||_7){return this.getGroupNodeHTML(_1,_4)}else if(!this.showGroupSummaryInHeader){return"&nbsp;"}}}
var _8=this.fields[_3],_9=null;if(_8==null)return"";if(this.isCheckboxField(_8)){var _10;if(!this.body.canSelectRecord(_1)){_10="[SKINIMG]/blank.gif"}else{var _11=this.selection.isSelected(_1)?true:false;_10=_11?(this.checkboxFieldTrueImage||this.booleanTrueImage):(this.checkboxFieldFalseImage||this.booleanFalseImage)}
if(_1&&_1[this.recordEnabledProperty]==false){_10=_10.replace(".","_Disabled.")}
var _12=this.getValueIconHTML(_10,_8);return _12}
var _10,_13=this.showValueIconOnly(_8),_14;if(_1!=null){if(_1[this.singleCellValueProperty]!=null){return _1[this.singleCellValueProperty]}
if(Array.isLoading(_1)){if(!isc.Browser.isSafari||_3==0){return this.loadingMessage}
return"&nbsp;"}
_14=(this.$49j&&this.$471==_2&&(!this.editByCell||this.$504==_3)&&this.canEditCell(_2,_3));if(_14){_9=this.getEditItemCellValue(_1,_2,_3)}else if(this.$537(_3)&&this.canEditCell(_2,_3)){_9=this.getInactiveEditorCellValue(_1,_2,_3);_14=true}else{var _8=this.fields[_3],_15,_16;if(_8.displayField!=null){_16=!_8.valueMap&&!_8.getCellValue&&this.$4t(_8);if(_16){var _17=_8.displayField;_15=this.getRawCellValue(_1,_2,_17,true)}}
_9=this.getRawCellValue(_1,_2,_3);if(_8.getCellValue){isc.Func.replaceWithMethod(_8,"getCellValue","viewer,record,recordNum,field,fieldNum,value");_9=_8.getCellValue(this,_1,_2,_8,_3,_9)}
_10=this.getValueIcon(_8,_9,_1,_2);if(!_13){var _18=_8.valueMap;if(_18){if(isc.isA.String(_18))_18=this.getGlobalReference(_18);if(!isc.isAn.Array(_18)){if(isc.isAn.Array(_9)){var _19=[];for(var i=0;i<_9.length;i++){var _21=isc.getValueForKey(_9[i],_18,_9[i]);_19[_19.length]=_21}
_9=_19}else{_9=isc.getValueForKey(_9,_18)}}}}
if(_16)_9=_15}}
if(!_14){if(_1&&((_1[this.groupSummaryRecordProperty]&&!this.shouldShowGroupSummary(_8))||(_1[this.gridSummaryRecordProperty]&&!this.shouldShowGridSummary(_8))))
{_9=this.emptyCellValue}else{var _22=null;if(_10!=null){_22=this.getValueIconHTML(_10,_8)}
if(_13){if(!_22||isc.isAn.emptyString(_22))_22=this.emptyCellValue;_9=_22}else{_9=this.$32v(_9,_1,_8,_2,_3);var _23=this.getFieldHilites(_1,_8);if(_23)_9=this.applyHiliteHTML(_23,_9);if(_22){if(_8.valueIconOrientation!=isc.Canvas.RIGHT)
_9=_22+_9;else
_9=_9+_22}}
if(this.isEditable()&&this.showErrorIcons&&this.cellHasErrors(_2,_3)){_9=this.getErrorIconHTML(_2,_3)+_9}}}
var _24=this.isGrouped?this.getGroupTitleField():null;if(_24&&this.fields[_3].name==_24){var _25=isc.Canvas.spacerHTML(this.groupIndentSize+this.groupLeadingIndent,1);_9=_25+_9}
return _9}
);isc.evalBoundary;isc.B.push(isc.A.getTitleFieldValue=function isc_ListGrid_getTitleFieldValue(_1){var _2=this.getDataSource().getTitleField(),_3=this.getCellValue(_1,this.getRecordIndex(_1),this.getFieldNum(_2),this.body);if(!_3||_3==""){_3=this.getRawCellValue(_1,this.getRecordIndex(_1),_2,true)}
return _3}
,isc.A.getRawValue=function isc_ListGrid_getRawValue(_1,_2){var _3=this.getRecordIndex(_1),_4=this.getSpecifiedField(_2);if(_1[_4[this.fieldIdProperty]]!=null)return _1[_4[this.fieldIdProperty]];else return this.getRawCellValue(_1,_3,_2,true)}
,isc.A.getFormattedValue=function isc_ListGrid_getFormattedValue(_1,_2,_3){var _4=this.getRecordIndex(_1),_5=this.getSpecifiedField(_2),_6=this.getFieldNum(_2);if(this.fieldIsVisible(_5)&&_3==null)
return this.getCellValue(_1,_4,_6,this.body);if(_3==null)_3=this.getRawValue(_1,_2);var _7=_5?_5.valueMap:null;if(_7){if(isc.isA.String(_7))_7=this.getGlobalReference(_7);if(!isc.isAn.Array(_7)){if(isc.isAn.Array(_3)){var _8=[];for(var i=0;i<_3.length;i++){var _10=isc.getValueForKey(_3[i],_7,_3[i]);_8[_8.length]=_10}
_3=_8}else{_3=isc.getValueForKey(_3,_7)}}}
var _11=this.$32v(_3,_1,_5,_4,_6);return _11}
,isc.A.getSpecifiedFieldWidth=function isc_ListGrid_getSpecifiedFieldWidth(_1){_1=this.getField(_1);var _2=_1[this.fieldIdProperty],_3=this.header?this.header.members:null,_4=this.frozenFields&&this.frozenHeader?this.frozenHeader.members:null,_5;if(_3||_4){var _6;if(_3)_6=_3.find(this.fieldIdProperty,_2);if(!_6&&_4){_6=_4.find(this.fieldIdProperty,_2)}
if(_6&&_6.$ts&&isc.isA.Number(_6.$ts)){_5=_6.$ts}}
return _5}
,isc.A.getValueIconHTML=function isc_ListGrid_getValueIconHTML(_1,_2){var _3=_2.imageURLPrefix||_2.baseURL||_2.imgDir,_4=_2.imageURLSuffix,_5=this.getValueIconWidth(_2),_6=this.getValueIconHeight(_2),_7=this.getValueIconLeftPadding(_2),_8=this.getValueIconRightPadding(_2);if(_4!=null)_1+=_4;var _9=isc.Canvas.$2n(_1,_3,_5,_6,_7,_8,null,this);return _9}
,isc.A.getCellAlign=function isc_ListGrid_getCellAlign(_1,_2,_3){if(_1&&_1[this.singleCellValueProperty]!=null&&(!this.showSingleCellCheckboxField(_1)||!this.isCheckboxField(this.getField(_3))))
{return this.isRTL()?isc.Canvas.RIGHT:isc.Canvas.LEFT}
var _4=_3;var _5=this.fields[_4];if(!_5)return isc.Canvas.LEFT;if(_5.userFormula||_5.userSummary){return this.isRTL()?isc.Canvas.LEFT:isc.Canvas.RIGHT}
return _5.cellAlign||_5.align}
,isc.A.showSingleCellCheckboxField=function isc_ListGrid_showSingleCellCheckboxField(_1){return(this.getCurrentCheckboxField()!=null)&&_1&&!_1.$35&&!_1[this.isSeparatorProperty]}
,isc.A.$4t=function isc_ListGrid__useDisplayFieldValue(_1){if(!_1||_1.valueMap||_1.displayField==null||(_1.displayField==_1.name))
{return false}
if(_1.optionDataSource==null){if(_1.displayValueFromRecord==false)return false;return true}
var _2=_1.autoFetchDisplayMap;if(_2==null)_2=this.autoFetchDisplayMap;if(_2)return false;if(_1.displayValueFromRecord!=null)return _1.displayValueFromRecord;return(_1.valueField==null||_1.valueField==_1.name)&&(isc.DS.get(_1.optionDataSource)==this.getDataSource())}
,isc.A.getValueIcon=function isc_ListGrid_getValueIcon(_1,_2,_3,_4){if(!_1.valueIcons||_1.suppressValueIcon){if(this.$53g(_1)){var _5=(_2?this.booleanTrueImage:this.booleanFalseImage);_4=(_4!=null)?_4:this.findRowNum(_3);var _6=_1.masterIndex;if(!this.canEditCell(_4,_6)&&_1.canToggle){_5=isc.Img.urlForState(_5,false,false,"Disabled")}
if(_5==null)_5=isc.Canvas.getImgURL(isc.Canvas.$11);return _5}
return null}
var _7=_1.valueIcons[_2];return _7}
,isc.A.getValueIconLeftPadding=function isc_ListGrid_getValueIconLeftPadding(_1){return(_1&&_1.valueIconLeftPadding!=null?_1.valueIconLeftPadding:this.valueIconLeftPadding||0)}
,isc.A.getValueIconRightPadding=function isc_ListGrid_getValueIconRightPadding(_1){return(_1&&_1.valueIconRightPadding!=null?_1.valueIconRightPadding:this.valueIconRightPadding||0)}
,isc.A.showValueIconOnly=function isc_ListGrid_showValueIconOnly(_1){if(_1.showValueIconOnly!=null)return _1.showValueIconOnly;if(_1.valueIcons!=null&&_1.valueMap!=null)return true;return this.$53g(_1)}
,isc.A.getValueIconWidth=function isc_ListGrid_getValueIconWidth(_1){if(this.isCheckboxField(_1))return this.$53r();if(this.$53g(_1))return this.booleanImageWidth;return(_1.valueIconWidth!=null?_1.valueIconWidth:(_1.valueIconSize!=null?_1.valueIconSize:(this.valueIconWidth!=null?this.valueIconWidth:this.valueIconSize)))}
,isc.A.getValueIconHeight=function isc_ListGrid_getValueIconHeight(_1){if(this.isCheckboxField(_1))return this.$53s();if(this.isExpansionField(_1))return this.$51b();if(this.$53g(_1))return this.booleanImageHeight;return(_1.valueIconHeight!=null?_1.valueIconHeight:(_1.valueIconSize!=null?_1.valueIconSize:(this.valueIconHeight!=null?this.valueIconHeight:this.valueIconSize)))}
,isc.A.$49h=function isc_ListGrid__isNewRecordRow(_1){return this.showNewRecordRow&&(_1==this.getTotalRows()-1)}
,isc.A.getNewRecordRowCellValue=function isc_ListGrid_getNewRecordRowCellValue(){return'<div align="center">'+this.newRecordRowMessage+'</div>'}
,isc.A.getErrorIconHTML=function isc_ListGrid_getErrorIconHTML(_1,_2){var _3=this.getCellErrors(_1,_2);if(_3==null)return isc.$ah;var _4=this.imgHTML(this.errorIconSrc,this.errorIconWidth,this.errorIconHeight,null," isErrorIcon='true'");if(isc.Browser.isIE&&this.$49j&&this.getEditRow()==_1&&(!this.editByCell||this.getEditCol()==_2)){_4+=" "}else{_4+=isc.Canvas.spacerHTML(this.cellPadding,"auto")}
return _4}
,isc.A.$49y=function isc_ListGrid__handleErrorIconOver(_1,_2){this.$49w=[_1,_2];isc.Hover.setAction(this,this.$295)}
,isc.A.$295=function isc_ListGrid__handleErrorIconHover(){if(this.$49w==null)return;var _1=this.$49w[0],_2=this.$49w[1];var _3=this.getCellErrors(_1,_2);if(_3==null)return;var _4=isc.FormItem.getErrorPromptString(_3);isc.Hover.show(_4,this.$1e())}
,isc.A.$49x=function isc_ListGrid__handleErrorIconOut(){delete this.$49w;if(isc.Hover.isActive){isc.Hover.clear()}}
,isc.A.$32v=function isc_ListGrid__formatCellValue(_1,_2,_3,_4,_5){if(_3&&_3.userSummary){_1=this.getSummaryFieldValue(_3,_2)}else{if(_3&&_3.formatCellValue!=null){_1=_3.formatCellValue(_1,_2,_4,_5,this)}else if(_3&&_3.cellValueTemplate){_1=_3.cellValueTemplate.evalDynamicString(this,{value:_1,record:_2,field:_3})}else if(this.formatCellValue!=null){_1=this.formatCellValue(_1,_2,_4,_5)}else if(_3&&_3.$140!=null){_1=_3.$14v.shortDisplayFormatter(_1,_3,this,_2,_4,_5)}else if(_3&&_3.$53f!=null){_1=_3.$53f(_1,_3,this,_2,_4,_5)}}
if(_1==null||isc.is.emptyString(_1)){if(_3.emptyCellValue!=null){_1=_3.emptyCellValue}else if(_3.type=="summary"){_1=this.invalidSummaryValue}else{_1=this.emptyCellValue}}else if(this.$51q[_1]==true){_1=this.$sd}else if(!isc.isA.String(_1)){if(isc.isA.Date(_1)){if(this.$28q(_3)){var _6=this.$28r(_3);var _7=isc.SimpleType.inheritsFrom(_3.type,"time");_1=isc.Time.toTime(_1,_6,_7)}else{var _8=_3&&isc.SimpleType.inheritsFrom(_3.type,"datetime"),_9=!_8&&_3&&isc.SimpleType.inheritsFrom(_3.type,"date"),_6=this.$28s(_3);if(_8)_1=_1.toShortDateTime(_6,true);else _1=_1.toShortDate(_6,!_9)}}else{_1=isc.iscToLocaleString(_1)}}
if(this.formatDisplayValue){_1=this.formatDisplayValue(_1,_2,_4,_5)}
if(_3&&_3.escapeHTML){if(isc.isA.String(_1)&&_1!=this.$sd){_1=_1.asHTML()}}
return _1}
,isc.A.getEditItemCellValue=function isc_ListGrid_getEditItemCellValue(_1,_2,_3){var _4=this.getEditorName(_2,_3);if(!this.$6l||!this.$6l.getItem(_4)){return"&nbsp;"}
var _5=this.getCellErrors(_2,_3);if(_5){this.$6l.setFieldErrors(_4,_5)}
var _6=this.$6l.getItem(_4),_7=_6.getStandaloneItemHTML(_6.getValue(),false,true);if(_5){this.$6l.setFieldErrors(_4,null)}
var _8=_6.containerWidget;if(!_8.$538)_8.$538=[];if(!_8.$538.contains(_6)){_8.$538.add(_6)}
return _7}
,isc.A.$537=function isc_ListGrid__showInactiveEditor(_1){return this.$52t(_1)}
,isc.A.getInactiveEditorCellValue=function isc_ListGrid_getInactiveEditorCellValue(_1,_2,_3){var _4=this.getEditorName(_2,_3);var _5=this.$6l.getItem(_4),_6=isc.DynamicForm.getEditorType(_5,this.$6l),_7=this.getRawCellValue(_1,_2,_3),_8;if(this.shouldShowEditorPlaceholder(this.getField(_3),_6)){_8=this.getEditorPlaceholderHTML(_6,_7,_1,_2,_3)}else{var _9={grid:this.getID(),record:_1,rowNum:_2,colNum:_3}
_8=_5.getInactiveEditorHTML(_7,false,true,_9);if(!this.$539)this.$539={};var _10="_"+_2+"_"+_3;if(this.$539[_10]){this.logWarn("creating new inactiveEditor context for a cell without having cleared "+"the previous one."+this.getStackTrace(),"inactiveEditorHTML")}
this.$539[_10]=_9}
return _8}
,isc.A.shouldShowEditorPlaceholder=function isc_ListGrid_shouldShowEditorPlaceholder(_1,_2){if(_1.showEditorPlaceholder!=null)return _1.showEditorPlaceholder;return!this.$51r[_2]}
,isc.A.getEditorPlaceholderHTML=function isc_ListGrid_getEditorPlaceholderHTML(_1,_2,_3,_4,_5){return _2}
,isc.A.$49t=function isc_ListGrid__editItemsDrawingNotification(_1,_2,_3){var _4;if(_1)_4=[_1];else{_4=[];var _5=this.$6l.getItems();for(var i=0;i<_5.length;i++){if(_5[i].containerWidget==_3)_4.add(_5[i])}}
var _7=_3.$538;for(var i=0;i<_4.length;i++){var _8=_4[i],_9=_8.isDrawn(),_10=_7?_7.contains(_8):false,_11=isc.CanvasItem&&isc.isA.CanvasItem(_8);if(_9){if(_10){_8.redrawn();if(_2)_8.moved()}
else{_8.cleared()}}else if(_10){if(_11)_8.placeCanvas();_8.drawn()}}
if(this.$6l)this.$6l.destroyOrphanedItems("Grid edit-items removed");delete _3.$538}
,isc.A.$49n=function isc_ListGrid__clearingInactiveEditorHTML(_1,_2){if(this.$539==null)return;if(!this.$6l||!this.$52t((_2!=null?this.getField(_2):null),true))return;if(_1!=null){var _3="_"+_1+"_"+_2,_4=this.$539[_3];if(_4){_4.formItem.clearInactiveEditorContext(_4);delete this.$539[_3]}}else{var _5=this.getEditForm(),_6=_5.getItems();for(var i=0;i<_6.length;i++){_6[i].clearAllInactiveEditorContexts()}
delete this.$539}}
,isc.A.setRecordValues=function isc_ListGrid_setRecordValues(_1,_2){if(!this.shouldSaveLocally()){this.logWarn("setRecordValues() not supported for grids where saveLocally is false");return}
if(!this.data)return;var _3=this.data.indexOf(_1),_4=this.data.get(_3);var _5=isc.Canvas.$3e(this.dataPath,_2,this);this.combineObjects(_4,_5);this.calculateRecordSummaries([_4]);if(this.useCellRecords){_3=this.findRowNum(_4);var _6=this.findColNum(_4);this.refreshCell(_3,_6)}else{this.refreshRow(_3)}
if(this.valuesManager!=null){this.valuesManager.$41(_3,null,_4,this)}}
,isc.A.combineObjects=function isc_ListGrid_combineObjects(_1,_2){return isc.combineObjects(_1,_2)}
,isc.A.setRawCellValue=function isc_ListGrid_setRawCellValue(_1,_2,_3,_4){var _5=this.fields[_3];if(!_1||!_5)return;if(_5.setRawCellValue){isc.Func.replaceWithMethod(_5,"setRawCellValue","viewer,record,recordNum,field,fieldNum,value");_5.setRawCellValue(this,_1,_2,_5,_3,_4)}else{if(_5.dataPath){isc.Canvas.$3d(this.$536(_5.dataPath),_4,_1,this,true)}else{_1[_5[this.fieldIdProperty]]=_4}}
this.data.dataChanged()}
,isc.A.getCellBooleanProperty=function isc_ListGrid_getCellBooleanProperty(_1,_2,_3,_4){var _5=false,_6=this[_1];if(_6==false||_6==this.$3i)return false;var _7=this.fields[_3][_1];if(_7==false||_7==this.$3i)return false;if(_4!=null){var _8=this.getRecord(_2,_3),_9=(_8!=null?_8[_4]:false);if(_9==false||_9==this.$3i)return false;if(_9==true||_9==this.$302)return true}
return(_6==true)||(_7==true)||(_6==this.$302)||(_7==this.$302)}
,isc.A.setShowRecordComponents=function isc_ListGrid_setShowRecordComponents(_1){if(this.showRecordComponents==_1)return;if(_1){if(this.animateFolders){this.$54a=true
this.animateFolders=false}}else{if(this.$54a){this.animateFolders=true;delete this.$54a}}
this.showRecordComponents=_1;this.$54b();if(_1){this.$54c=this.drawAllMaxCells;this.drawAllMaxCells=0;if(this.body!=null)this.body.drawAllMaxCells=0}else{if(this.$54c!=null){this.drawAllMaxCells=this.$54c;if(this.body!=null)this.body.drawAllMaxCells=this.$54c;delete this.$54c}}
this.invalidateRecordComponents()}
,isc.A.$54b=function isc_ListGrid__updateVirtualScrollingForRecordComponents(){if(!this.showRecordComponents){if(this.$54d){delete this.virtualScrolling;delete this.$54d}}else{if(this.virtualScrolling==null||this.$54d){if(this.frozenFields==null){this.virtualScrolling=true;this.$54d=true}else{if(this.recordComponentHeight==null){this.logWarn("This grid has frozen fields and is showing "+"recordComponents. This may lead to unpredictable row heights which "+"are not supported with frozen fields. Setting "+"listGrid.recordComponentHeight will avoid this issue.","recordComponents")}
if(this.$54d){delete this.virtualScrolling;delete this.$54d}}}}
if(this.body&&this.virtualScrolling!=this.body.virtualScrolling){this.body.virtualScrolling=this.virtualScrolling;if(this.frozenBody){this.frozenBody.virtualScrolling=this.virtualScrolling}}}
,isc.A.getDrawArea=function isc_ListGrid_getDrawArea(){if(this.body){var _1=this.body.getDrawArea();if(this.frozenFields&&this.freezeLeft()){_1[2]+=this.frozenFields.length;_1[3]+=this.frozenFields.length}
return _1}
return null}
,isc.A.$49r=function isc_ListGrid__drawAreaChanged(_1,_2,_3,_4,_5){if(this.frozenFields&&this.freezeLeft()){_3+=this.frozenFields.length;_4+=this.frozenFields.length}
var _6=[_1,_2,_3,_4];if(_6.equals(this.getDrawArea()))return;this.drawAreaChanged(_1,_2,_3,_4)}
,isc.A.drawAreaChanged=function isc_ListGrid_drawAreaChanged(){}
,isc.A.updateRecordComponents=function isc_ListGrid_updateRecordComponents(){var _1=this.logIsDebugEnabled("recordComponents");if(this.$54e){if(_1){this.logDebug("updateRecordComponents called recursively - returning","recordComponents")}
return}
var _2=this.body,_3=this.frozenBody;if(_2==null)return;if(_2.$46k!=null){return}
if((!_2.isDrawn()||_2.$45p==null||(_2.$45p.length==0&&_2.fields.length>0))||(_3&&(!_3.isDrawn()||_3.$45p==null||(_3.$45p.length==0&&_3.fields.length>0))))
{return}
this.$54e=true;var _4=this.$54f||[],_5=this.$54g||{};if(_1){this.logDebug("updateRecordComponents - old record components before refreshing:"+this.echo(_4),"recordComponents")}
this.$54g={};this.$54f=[];if(this.showRecordComponents||this.showBackgroundComponents){var _6=this.body.getDrawArea(),_7=this.showRecordComponentsByCell,_8=this.body.getID(),_9=this.frozenBody?this.frozenBody.getID():null;if(_1){this.logDebug("updating to potentially show recordComponents for drawArea:"+_6,"recordComponents")}
for(var _10=_6[0];_10<=_6[1];_10++){var _11=this.getRecord(_10);if(_11==null||Array.isLoading(_11))continue;if(this.showRecordComponents){if(!_7){var _12=this.shouldShowRecordComponent(_11),_13=null;if(_12){_13=this.$54h(_11,null,_8);if(_13!=null){if(_13.isNullMarker){_13=null}else{var _14=_13.getID();_5[_14]=null}}else{_13=this.$54i(_11,null,this.body,_10)}}
if(_13!=null){var _14=_13.getID();this.$54g[_14]=true;this.$54f[this.$54f.length]=_13}}else{if(this.frozenBody!=null){for(var _15=0;_15<this.frozenBody.fields.length;_15++){var _16=this.frozenBody.fields[_15],_17=_16.name;var _12=this.shouldShowRecordComponent(_11,_16.masterIndex),_13=null;if(_12){_13=this.$54h(_11,_17,_9);if(_13!=null){if(!_13.isNullMarker){var _14=_13.getID();_5[_14]=null}else{_13=null}}else{_13=this.$54i(_11,_17,this.frozenBody,_10,_15)}}
if(_13!=null){var _14=_13.getID();this.$54g[_14]=true;this.$54f[this.$54f.length]=_13}}}
for(var _18=_6[2];_18<=_6[3];_18++){var _16=this.body.fields[_18],_17=_16.name;var _12=this.shouldShowRecordComponent(_11,_16.masterIndex),_13=null;if(_12){var _13=this.$54h(_11,_17,_8);if(_13!=null){if(!_13.isNullMarker){var _14=_13.getID();_5[_14]=null}else{_13=null}}else{_13=this.$54i(_11,_17,this.body,_10,_18)}}
if(_13!=null){var _14=_13.getID();this.$54g[_14]=true;this.$54f[this.$54f.length]=_13}}}}
if(this.showBackgroundComponents){if(_11&&_11.backgroundComponent){var _19=_11.$4p?_11.$4p.find("isBackgroundComponent",true):null;if(!_19){if(isc.isA.Canvas(_11.backgroundComponent)){var _20=_11.backgroundComponent.addProperties(this.backgroundComponentProperties,{isBackgroundComponent:true})}else{var _21=isc.addProperties({isBackgroundComponent:true},this.backgroundComponentProperties,_11.backgroundComponent);var _20=this.createAutoChild("backgroundComponent",_21)}
var _22=_2.getTableZIndex();_20.setZIndex(_22-49);_20.setWidth("100%");_20.setHeight("100%");_20.setOverflow("hidden");this.addEmbeddedComponent(_11.backgroundComponent,_11,_10,null,"within")}}}}}
if(this.logIsInfoEnabled("recordComponents")){this.logInfo("updateRecordComponents - new recordComponents:"+this.echo(this.$54g)+", old record components (will be cleaned up if value is 'true'):"+this.echo(_5),"recordComponents")}
for(var i=0;i<_4.length;i++){var _14=_4[i].getID();if(_5[_14]!=true){continue}
if(_1){this.logDebug("cleaning up RecordComponent:"+_4[i],"recordComponents")}
this.$54j(_4[i]);_5[_14]=null}
delete this.$54e}
,isc.A.$54i=function isc_ListGrid__applyNewRecordComponent(_1,_2,_3,_4,_5){if(this.logIsDebugEnabled("recordComponents")){this.logDebug("getting record component for row/field:"+[_4,_2],"recordComponents")}
var _6=_3.getID();var _7=this.recordComponentPoolingMode=="recycle",_8,_9,_10=_2==null?null:this.getColNum(_2);if(_7){var _11=this.getFromRecordComponentPool(_1,_2);_8=_11?_11[0]:null;_9=_11?_11[1]:null}
if(!_8){if(this.createRecordComponent&&isc.isA.Function(this.createRecordComponent)){_8=this.createRecordComponent(_1,this.getColNum(_2));if(_8!=null)_8.isRecordComponent=true;this.logDebug("created new record component:"+_8,"recordComponents")}}else{if(this.updateRecordComponent&&isc.isA.Function(this.updateRecordComponent)){var _12=_8;_8=this.updateRecordComponent(_1,_10,_8,!_9);if(_8==null){if(this.logIsInfoEnabled("recordComponents")){this.logInfo("showRecordComponents: updateRecordComponent() method "+"failed to return an updated component.","recordComponents")}
this.addToRecordComponentPool(_12)}
this.logDebug("updated record component from pool:"+_8,"recordComponents")}}
var _13=_8==null;if(_13){_8={isNullMarker:true,$476:_6,$54k:this.$51u}}
if(_1.$4o==null){_1.$4o={}}
if(_2==null)_2=this.$51s;_1.$4o[_2]=_8;if(_7&&!_13){_8.currentFieldName=_2;_8.currentRecord=_1}
if(!_13){return _3.addEmbeddedComponent(_8,_1,_4,_5,this.getRecordComponentPosition())}}
,isc.A.setDontAutoDestroyComponent=function isc_ListGrid_setDontAutoDestroyComponent(_1,_2){_1.dontAutoDestroy=_2}
,isc.A.$54j=function isc_ListGrid__cleanUpRecordComponent(_1,_2){if(this.logIsDebugEnabled("recordComponents")){this.logDebug("cleaning up recordComponent:"+_1,"recordComponents")}
var _3=this.recordComponentPoolingMode;if(_2)_3="viewport";if(_3=="data"){if(!_1.destroyed&&!_1.destroying&&!_1.$wa){if(this.$534==null){this.$534={}}
var _4=_1.ID;if(this.$534[_4]!=_1){this.$534[_1.ID]=_1;if(_1.pointersToThis==null)_1.pointersToThis=[];_1.pointersToThis.add({object:this.$534,property:_1.ID})}}}else{var _4=_1.ID,_5=isc.Canvas.getById(_1.$476),_6=_1.embeddedRecord,_7=_1.$49k;if(_6.$4o){if(_6.$4o[_7]==_1){delete _6.$4o[_7]}
delete _6.$4o[_7]}
if(_5!=null){_5.removeEmbeddedComponent(_1.embeddedRecord,_1)}
if(_3=="viewport"){if(!_1.dontAutoDestroy)_1.markForDestroy()}else{if(_1.destroying||_1.destroyed||_1.$wa)return;this.addToRecordComponentPool(_1)}}}
,isc.A.getLiveRecordComponent=function isc_ListGrid_getLiveRecordComponent(_1,_2,_3){if(!_1)return null;if(isc.isA.Number(_1))_1=this.getRecord(_1);if(!_3)_3=this.body.getID();var _4=this.$54h(_1,_2,_3);return _4}
,isc.A.$54h=function isc_ListGrid__getLiveRecordComponent(_1,_2,_3){if(_2==null)_2=this.$51s;var _4=_1.$4o;if(_4==null||_4[_2]==null)return null;var _5=_4[_2];if(_5.$476!=_3){return null}
if(_5.isNullMarker&&_5.$54k!=this.$54l){return null}
if(_5.destroyed||_5.destroying||_5.$wa){this.logWarn("Destroyed or Destroying record component:"+_5+" present on record. Ignoring","recordComponents");_4[_2]=null;return null}
return _5}
,isc.A.invalidateRecordComponents=function isc_ListGrid_invalidateRecordComponents(){this.dropRecordComponents(true);if(this.showRecordComponents&&this.isDrawn()){this.updateRecordComponents()}}
,isc.A.dropRecordComponents=function isc_ListGrid_dropRecordComponents(_1){this.$51u++;var _2=this.$54f||[];delete this.$54f;delete this.$54g;for(var i=0;i<_2.length;i++){this.$54j(_2[i],_1)}}
,isc.A.refreshRecordComponent=function isc_ListGrid_refreshRecordComponent(_1,_2){if(!this.showRecordComponents||_1==null||this.body==null)return;if(this.showRecordComponentsByCell&&_2==null){this.logWarn("refreshRecordComponent() called with no colNum. This parameter is required when "+"showRecordComponentsByCell is true. Taking no action.");return}
var _3=this.getRecord(_1);if(_3==null||Array.isLoading(_3))return;var _4=this.body,_5=null,_6=null;if(this.showRecordComponentsByCell){_4=this.getFieldBody(_2);_5=this.getLocalFieldNum(_2);_6=this.getFieldName(_2)}
var _7=this.$54h(_3,_6,_4.getID());if(_7!=null&&_7.isNullMarker)_7=null;if(_7!=null){this.$54j(_7,(this.recordComponentPoolingMode!="recycle"))}
var _8;if(this.shouldShowRecordComponent(_3)){_8=this.$54i(_3,_6,_4,_1,_5);if(_8&&_8.isNullMarker)_8=null}
var _9=this.$54f.length;if(_7){var _10=_7.getID();this.$54g[_7.getID()]=null;if(_8!=null){_9=this.$54f.indexOf(_7);this.$54f[_9]=null}else{this.$54f.remove(_7)}}
if(_8!=null){var _10=_8.getID();this.$54g[_10]=true;this.$54f[_9]=_8}}
,isc.A.getRecordComponentPosition=function isc_ListGrid_getRecordComponentPosition(){if(this.recordComponentPosition!=null)return this.recordComponentPosition;return(this.showRecordComponentsByCell?"within":"expand")}
,isc.A.getRecordComponentPool=function isc_ListGrid_getRecordComponentPool(){if(!this.$54m)this.$54m=[];return this.$54m}
,isc.A.getFromRecordComponentPool=function isc_ListGrid_getFromRecordComponentPool(_1,_2){var _3=this.getRecordComponentPool(),_4=[],_5;if(!_3||_3.length==0)return null;if(this.showRecordComponentsByCell&&this.poolComponentsPerColumn==true){_4=_3.findAll("currentFieldName",_2)}else{_4=_3}
if(!_4||_4.length==0)return null;for(var i=0;i<_4.length;i++){_5=_4[i];var _7=_5.currentRecord;if(this.comparePrimaryKeys(_7,_1)){_3.remove(_5);return[_5,true]}}
_3.length-=1;return[_5,false]}
,isc.A.addToRecordComponentPool=function isc_ListGrid_addToRecordComponentPool(_1){var _2=this.getRecordComponentPool();_2.add(_1)}
,isc.A.shouldShowRecordComponent=function isc_ListGrid_shouldShowRecordComponent(_1,_2){if(_1==null||_1.$35||_1[this.isSeparatorProperty]||Array.isLoading(_1))
{return false}
return this.showRecordComponent(_1,_2)}
,isc.A.showRecordComponent=function isc_ListGrid_showRecordComponent(){return true}
,isc.A.bodyDrawing=function isc_ListGrid_bodyDrawing(_1){if(isc.$dd)arguments.$de=this;if(_1!=this.body)return;var _2;if(this.$54n){_2=!isc.RPCManager.startQueue();this.$54o()}
this.requestVisibleRows();if(_2)isc.RPCManager.sendQueue();this.$54n=null}
,isc.A.setRecordComponentHeight=function isc_ListGrid_setRecordComponentHeight(_1){this.recordComponentHeight=_1;if(this.isDrawn())this.markForRedraw()}
,isc.A.getAvgRowHeight=function isc_ListGrid_getAvgRowHeight(_1){if(this.showRecordComponents&&this.recordComponentHeight!=null){return this.getRecordComponentRowHeight()}
return _1.fixedRowHeights?_1.cellHeight:Math.max(_1.cellHeight,_1.avgRowHeight)}
,isc.A.getRecordComponentRowHeight=function isc_ListGrid_getRecordComponentRowHeight(){if(this.recordComponentHeight==null)return null;var _1=this.getRecordComponentPosition();if(_1==this.$45j)return this.cellHeight+this.recordComponentHeight;else return Math.max(this.recordComponentHeight,this.cellHeight)}
,isc.A.$53m=function isc_ListGrid__setOptionDataSources(){this.$54n=null;var _1=this.$54p;this.$54p=[];var _2=this.getDataSource();for(var i=0;i<this.completeFields.length;i++){var _4=this.completeFields[i];if(_4==null){this.logWarn("Fields array contains an empty entry");continue}
var _5=_4.displayField||_4[this.fieldIdProperty],_6=_4.valueField||_4[this.fieldIdProperty];if(_4.optionDataSource==null||_4.displayField==null||(_4.displayField==_4.valueField)||(_4.valueField==null&&(_4.displayField==_4[this.fieldIdProperty]))||(_4.autoFetchDisplayMap==false)||(this.autoFetchDisplayMap==false&&_4.autoFetchDisplayMap==null))
{continue}
var _7=isc.DS.get(_4.optionDataSource);if(_7==null){this.logWarn(_4.optionDataSource+" dataSource not found, check value of "+"optionDataSource property on the "+_4[this.fieldIdProperty]+" field");continue}
var _8=_7.ID,_9=_4[this.fieldIdProperty],_10=_4.optionCriteria;var _11=false;if(_1&&!_4.optionFilterContext){for(var _12=0;_12<_1.length;_12++){var _13=_1[_12];if(_13==null)continue;if(this.$54q(_4,_13)){_13.$32y=[_4];this.$54p.add(_13);_1[_12]=null;_11=true;break}}}
if(!_11&&!_4.optionFilterContext){for(var _12=0;_12<this.$54p.length;_12++){var _13=this.$54p[_12];if(this.$54q(_4,_13)){_13.$32y.add(_4);_11=true;break}}}
if(!_11){this.$54p.add({$54r:_8,$32y:[_4],$54s:_4.optionTextMatchStyle,$54t:_10,$54u:_4.optionFilterContext,$54v:_4.optionOperationId});this.$54n=true}}
for(var i=0;i<this.$54p.length;i++){if(this.$54p[i].$54w!=null){this.$54x(this.$54p[i])}}
if(_1!=null){for(var i=0;i<_1.length;i++){if(_1[i]&&_1[i].$54w)_1[i].$54w.destroy()}}}
,isc.A.$54q=function isc_ListGrid__fieldMatchesODSConfig(_1,_2){return(_2.$54r==_1.optionDataSource&&(_1.optionTextMatchStyle==_2.$54s)&&(_1.optionOperationId==_2.$54v)&&(isc.DataSource.getDataSource(_2.$54r).compareCriteria(_1.optionCriteria||{},_2.$54t||{})==0))}
,isc.A.$54x=function isc_ListGrid__updateValueMapFromODS(_1){var _2=_1.$54r,_3=_1.$32y;if(_3==null||_3.length==0){this.logWarn("$54x fired for dataSource:"+_2+" which no longer applies to any fields in this ListGrid");return}
var _4=_1.$54w;if(_4==null||(isc.isA.ResultSet(_4)&&!_4.lengthIsKnown()))return;for(var i=0;i<_3.length;i++){var _6=_3[i],_7=(_6.valueField||_6[this.fieldIdProperty]),_8=_4.getValueMap(_7,_6.displayField);if(this.getField(_6[this.fieldIdProperty])==null){_6.valueMap=_8}else{this.setValueMap(_6[this.fieldIdProperty],_8)}}}
,isc.A.$54o=function isc_ListGrid__fetchValueMapData(){var _1;for(var i=0;i<this.$54p.length;i++){var _3=this.$54p[i];if(_3.$54w==null){if(_1==null){_1=!isc.RPCManager.startQueue()}
var _4=_3.$54r;var _5,_6=_3.$32y;if(_6&&_6.length>0){_5="";for(var _7=0;_7<_6.length;_7++){_5+=_6[_7].name;if(_7<_6.length-1)_5+=","}}
var _8=_3.$54u||{};isc.addProperties(_8,{showPrompt:false,clientContext:{ODSConfig:_3},componentContext:_5,textMatchStyle:_3.$54s});if(_3.$54v!=null){_8.operationId=_3.$54v}
isc.DataSource.getDataSource(_4).fetchData(_3.$54t,{target:this,methodName:"$54y"},_8)}}
if(_1)isc.RPCManager.sendQueue()}
,isc.A.$54y=function isc_ListGrid__fetchValueMapCallback(_1,_2,_3){var _4=_3.clientContext.ODSConfig;if(!_4||(_4.$54w!=null)||!this.$54p||!this.$54p.contains(_4))
{return}
_4.$54w=isc.ResultSet.create({dataSource:_4.$54r,ODSConfig:_4,targetGrid:this,dataChanged:"this.targetGrid.$54x(this.ODSConfig)",initialData:_2})
this.$54x(_4);return true}
,isc.A.$533=function isc_ListGrid__dropODSData(){if(this.$54p==null)return;for(var i=0;i<this.$54p.length;i++){var _2=this.$54p[i].$54w;if(_2)_2.destroy()}
delete this.$54p}
,isc.A.requestVisibleRows=function isc_ListGrid_requestVisibleRows(){if(isc.ResultSet&&isc.isA.ResultSet(this.data)){if(this.body==null)return this.data.getRange(0,this.dataPageSize);if(this.data.lengthIsKnown()&&this.data.getLength()==0)return;if(!this.data.lengthIsKnown()){this.body.showAllRows=false}else{this.body.showAllRows=(this.body.overflow==isc.Canvas.VISIBLE?true:this.showAllRecords)}
var _1=this.body.getDrawArea();if(this.$463&&isc.isAn.Array(this.$463)){var _2=_1[1]-_1[0];_1[0]=this.$463[0];_1[1]=_1[0]+_2}
if(this.isGrouped){return this.data.getRange(0,this.groupByMaxRecords)}else{return this.data.getRange(_1[0],_1[1])}}
return null}
,isc.A.getPrintHeaders=function isc_ListGrid_getPrintHeaders(_1,_2){var _3=isc.SB.create();var _4=(this.isRTL()?isc.Canvas.LEFT:isc.Canvas.RIGHT);var _5,_6,_7=(this.headerSpans==null?null:[]),_8=[];var _9=["<TD CLASS=",(this.printHeaderStyle||this.headerBaseStyle)," ALIGN="].join("");for(var _10=_1;_10<_2;_10++){var _11=this.body.fields[_10];if(this.headerSpans!=null){if(_5==null){_5=this.headerSpans[0];_6=1}else{if(!_5.fields.contains(_11[this.fieldIdProperty])){_7.addList([_9,"center colspan=",_6,">",_5.title,"</TD>"]);_5=this.headerSpans[this.headerSpans.indexOf(_5)+1];_6=1}else{_6++}}}
var _12=_11.align||_4;_8.addList([_9,_12,">",this.getHeaderButtonTitle(_11.masterIndex),"</TD>"])}
if(_5!=null){_7.addList([_9,"center colspan=",_6,">",_5.title,"</TD>"]);_3.append("<TR>",_7.join(""),"</TR>")}
_3.append("<TR>",_8.join(""),"</TR>");return _3.toString()}
,isc.A.getPrintHTML=function isc_ListGrid_getPrintHTML(_1,_2){var _3=this.body;if(_3==null){this.createChildren();_3=this.body}
if(this.isDirty()||_3.isDirty()){this.redraw("updating HTML for printing")}
var _4=isc.addProperties({},_1,this.printProperties);var _5,_6;if(isc.isA.ResultSet(this.data)&&!this.data.allMatchingRowsCached()){var _7=this.body.getVisibleRows(),_8=_7?_7[0]:null,_9=this.data.getCachedRange(_8);if(_9!=null){_5=_9[0];_6=_9[1]}}
var _10=isc.Canvas.applyStretchResizePolicy(this.fields.getProperty("width"),_4.width||isc.Page.getWidth());return _3.getTablePrintHTML({startRow:_5,endRow:_6,callback:_2,printWidths:_10,printProps:_4})}
,isc.A.$49e=function isc_ListGrid__prepareForPrinting(_1,_2){this.isPrinting=this.body.isPrinting=true;this.currentPrintProperties=_2;var _3=this.body,_4=this.$49j;var _5=isc.getProperties(_3,["autoFit","wrapCells","showAllRows","showAllColumns","fixedRowHeights","$45p","fields"]);_3.showAllRows=true;_3.showAllColumns=true;this.$49j=false;_3.autoFit=this.printAutoFit;_3.wrapCells=this.printWrapCells;_3.fixedRowHeights=!this.printWrapCells;var _6=this.fields.duplicate();_6.removeAll(_6.findAll("shouldPrint",false));_3.fields=_6;var _7=this.frozenFields;delete this.frozenFields;_3.$45p=_1;return{oldEditorShowing:_4,oldFrozenFields:_7,origProps:_5}}
,isc.A.$49f=function isc_ListGrid__donePrinting(_1){var _2=this.body,_3=_1.origProps,_4=_1.oldEditorShowing;isc.addProperties(_2,_3);if(_3.showAllRows==null)_2.showAllRows=null;this.$49j=_4;this.frozenFields=_1.oldFrozenFields;delete this.currentPrintProperties;this.isPrinting=this.body.isPrinting=false;this.body.redraw()}
,isc.A.rowClick=function isc_ListGrid_rowClick(_1,_2,_3,_4){this.$527=_2;var _1=this.getCellRecord(_2,_3),_5=this.fields[_3];if(Array.isLoading(_1))return;var _6=this.getCellValue(_1,_2,_3);var _7=this.getRawCellValue(_1,_2,_3);if(_1!=null&&_1.$35){if(this.canCollapseGroup==false)return;if(_4){var _8=isc.EH.getKey();if(_8!="Space"&&_8!="Enter")return}
var _9=this,_10=_1;if(this.getEditRow()!=null)this.saveAllEdits(null,function(){_9.toggleFolder(_10)});else this.toggleFolder(_1);return}
if(_5.recordClick&&!(_4&&_5.keyboardFiresRecordClick==false)){isc.Func.replaceWithMethod(_5,"recordClick","viewer,record,recordNum,field,fieldNum,value,rawValue");var _11=_5.recordClick(this,_1,_2,_5,_3,_6,_7);if(_11==false)return false}
if(this.recordClick){var _11=this.recordClick(this,_1,_2,_5,_3,_6,_7);if(_11==false)return false}
var _12=this.isEditable()&&(this.editEvent==isc.EH.CLICK||this.editOnFocus);if(_5.canToggle&&this.canEditCell(_2,_3)&&this.shouldToggle(_5)){var _13=this.getEditorValueMap(_5,this.getEditedRecord(_2,_3));if(_13==null&&isc.SimpleType.getBaseType(_5.type)==this.$if){_13=[true,false]}
if(_13!=null){if(!isc.isAn.Array(_13))_13=isc.getKeys(_13);if(_13.length>1){var _14=this.getFieldName(_3),_15=this.getEditedCell(_2,_3),_16=_13.indexOf(_15);_16+=1;if(_16>=_13.length)_16=0;var _17=_15;_15=_13[_16];var _18;if(_5.change!=null){this.logInfo("canToggle firing specified field.change() event directly","gridEdit");_18=this.fireCallback(_5.change,"form,item,value,oldValue",[null,null,_15,_17])==false}
if(!_18){if(!_12&&this.autoSaveEdits){this.setEditValue(_2,_3,_15,true,false)}else{this.setEditValue(_2,_3,_15)}
if(_5.changed!=null){this.logInfo("canToggle firing specified field.changed() event directly","gridEdit");this.fireCallback(_5.changed,"form,item,value",[null,null,_15])}
if(this.autoSaveEdits)this.saveEdits(null,null,_2,_3)}}}}
if(_12){if(this.handleEditCellEvent(_2,_3,isc.ListGrid.CLICK)==true){return true}
if(_4){for(var i=0;i<this.fields.length;i++){if(i==_3)continue;if(this.handleEditCellEvent(_2,i,isc.ListGrid.CLICK)==true){return true}}}}}
,isc.A.shouldToggle=function isc_ListGrid_shouldToggle(_1){if(!this.$53g(_1))return true;var _2=this.getEventPart();return(_2&&_2.part=="valueicon")}
,isc.A.rowDoubleClick=function isc_ListGrid_rowDoubleClick(_1,_2,_3,_4){var _5=this.fields[_3],_6=this.getCellValue(_1,_2,_3),_7=this.getRawCellValue(_1,_2,_3);if(_1!=null&&_1.$35)return;if(_5.recordDoubleClick){isc.Func.replaceWithMethod(_5,"recordDoubleClick","viewer,record,recordNum,field,fieldNum,value,rawValue");var _8=_5.recordDoubleClick(this,_1,_2,_5,_3,_6,_7);if(_8==false)return _8}
if(this.recordDoubleClick!=null){var _8=this.recordDoubleClick(this,_1,_2,_5,_3,_6,_7);if(_8==false)return _8}
if(this.isEditable()&&this.editEvent==isc.EH.DOUBLE_CLICK){if(this.handleEditCellEvent(_2,_3,isc.ListGrid.DOUBLE_CLICK)==true)return true;if(_4){for(var i=0;i<this.fields.length;i++){if(i==_3)continue;if(this.handleEditCellEvent(_2,i,isc.ListGrid.DOUBLE_CLICK)==true)return true}}}}
,isc.A.$49d=function isc_ListGrid__cellContextClick(_1,_2,_3){this.cellContextItems=null;if(this.cellContextClick){var _1=this.getCellRecord(_2,_3);if(this.cellContextClick(_1,_2,_3)==false)return false}
if(this.showCellContextMenus){if(!this.cellContextMenu)this.cellContextMenu=this.getMenuConstructor().create(this.contextMenuProperties);if(!this.cellContextItems){this.cellContextItems=this.makeCellContextItems(this.getCellRecord(_2,_3),_2,_3)}
if(isc.isAn.Array(this.cellContextItems)&&this.cellContextItems.length>0){this.cellContextMenu.setData(this.cellContextItems);this.cellContextMenu.showContextMenu(this)}
return false}else{return true}}
,isc.A.getShowChildDataSourceContextMenuItemTitle=function isc_ListGrid_getShowChildDataSourceContextMenuItemTitle(_1){return"Show "+_1.getPluralTitle()}
,isc.A.makeCellContextItems=function isc_ListGrid_makeCellContextItems(_1,_2,_3){if(this.dataSource!=null){var _4=[];if(this.canOpenRecordDetailGrid){var _5=isc.DS.get(this.getRecordDataSource(_1)),_6=_5.getChildDataSources();if(_6!=null){for(var i=0;i<_6.length;i++){var _8=_6[i];_4.add({title:this.getShowChildDataSourceContextMenuItemTitle(_8),record:_1,dataSource:_8,click:"target.openRecordDetailGrid(item.record, item.dataSource)"})}}}
if(this.canOpenRecordEditor){_4.add({title:this.openRecordEditorContextMenuItemTitle,record:_1,click:"target.endEditing();target.openRecordEditor(item.record)"})}
if(_1!=null&&this.$54z==_1){_4.add({title:this.dismissEmbeddedComponentContextMenuItemTitle,click:"target.closeRecord()"})}
_4.add({title:this.deleteRecordContextMenuItemTitle,click:"target.removeSelectedData()"});return(_4.length>0?_4:null)}
return null}
);isc.evalBoundary;isc.B.push(isc.A.getCanHover=function isc_ListGrid_getCanHover(){if(this.canHover!=null)return this.canHover;var _1=this.getFields();if(_1!=null){for(var i=0;i<_1.length;i++){if(_1[i].showHover)return true}}
return this.canHover}
,isc.A.cellHoverHTML=function isc_ListGrid_cellHoverHTML(_1,_2,_3){if(this.$49j&&this.getEditRow()==_2&&this.canEditCell(_2,_3)&&(!this.editByCell||this.getEditCol()==_3)){return null}
var _4=this.getField(_3);if(_4.showHover==false)return null;if(_4.showHover==null&&!this.canHover)return null;var _5=this.getCellValue(_1,_2,_3);if(_4.hoverHTML){isc.Func.replaceWithMethod(_4,"hoverHTML","record,value,rowNum,colNum,grid");return _4.hoverHTML(_1,_5,_2,_3,this)}
if(_5!=null&&!isc.isAn.emptyString(_5)&&_5!=this.emptyCellValue){return _5}}
,isc.A.$48p=function isc_ListGrid__getCellHoverComponent(_1,_2,_3){if(!this.showHoverComponents)return null;if(this.$49j&&this.getEditRow()==_2&&(!this.editByCell||this.getEditCol()==_3))return null;var _4=this.getField(_3);if(_4.showHover==false)return null;if(_4.showHover==null&&!this.canHover)return null;if(this.getCellHoverComponent&&isc.isA.Function(this.getCellHoverComponent)){return this.getCellHoverComponent(_1,_2,_3)}else return null}
,isc.A.getCellHoverComponent=function isc_ListGrid_getCellHoverComponent(_1,_2,_3){return this.$51f(_1,false,true,_2,_3)}
,isc.A.$51f=function isc_ListGrid__getStockEmbeddedComponent(_1,_2,_3,_4,_5){var _6=this.getFields(),_7=this.dataSource?isc.getValues(this.getDataSource().getFields()):_6,_8=(!_3?null:this.hoverWidth||this.hoverSize||this.defaultCellHoverComponentWidth),_9=(!_3?null:this.hoverHeight||this.hoverSize||this.defaultCellHoverComponentHeight),_10=[],_11;for(var i=0;i<_7.length;i++){var _13=_7.get(i);if(this.dataSource){if(!_6.find("name",_13.name)){_10.add(_13)}}else{if(!this.isExpansionField(_13)){_10.add(_13)}}}
var _14=(_3?this.hoverMode:(_2?this.expansionMode:null));var _15;if(_14=="detailField"){_11=this.createAutoChild("expansionDetailField",{contents:_1[this.detailField]});_15={width:(_3?_8:"100%"),height:(_3?_9:"100%"),members:[_11]};if(_3){_15=isc.addProperties(_15,{hoverAutoDestroy:this.hoverAutoDestroy,overflow:"auto"})}
_11=isc.VLayout.create(_15)}else if(_14=="details"){_15={dataSource:this.dataSource,fields:_10};if(_3){_15=isc.addProperties(_15,{width:_8,height:_9,hoverAutoDestroy:this.hoverAutoDestroy})}
_11=this.createAutoChild("expansionDetails",_15);_11.setData(_1)}else if(_14=="related"){_15={dataSource:this.getRelatedDataSource(_1)};if(_3){_15=isc.addProperties(_15,{canEdit:false,width:_8,height:_9,dataProperties:{context:{showPrompt:false}},hoverAutoDestroy:this.hoverAutoDestroy})}
if(_2){_15=isc.addProperties(_15,{canExpandRecords:this.childExpansionMode?true:false,expansionMode:this.childExpansionMode,canEdit:this.expansionCanEdit})}
_11=this.createAutoChild("expansionRelated",_15);if(this.expansionCanEdit)_11.autoSaveEdits=true;_11.delayCall("fetchRelatedData",[_1,this.dataSource])}else if(_14=="detailRelated"){_15={dataSource:this.dataSource,fields:_10};if(_3){_15=isc.addProperties(_15,{dataProperties:{context:{showPrompt:false}},hoverAutoDestroy:this.hoverAutoDestroy})}
var _16=this.createAutoChild("expansionDetails",_15)
_16.setData(_1);_15={dataSource:this.getRelatedDataSource(_1),height:"100%",canEdit:(_3?false:(_2?this.expansionCanEdit:null))};if(_3){_15=isc.addProperties(_15,{dataProperties:{context:{showPrompt:false}},hoverAutoDestroy:this.hoverAutoDestroy})}
if(_2){_15=isc.addProperties(_15,{canExpandRecords:this.childExpansionMode?true:false,expansionMode:this.childExpansionMode})}
var _17=this.createAutoChild("expansionRelated",_15);_15={members:[_16,_17]};if(_3){_15=isc.addProperties(_15,{width:_8,height:_9,hoverAutoDestroy:this.hoverAutoDestroy})}
_11=this.createAutoChild("expansionDetailRelated",_15)}else if(_14=="editor"){_11=this.createAutoChild("expansionEditor",{dataSource:this.dataSource,fields:_10});_11.editRecord(this.getEditedRecord(_4)||_1)}
return _11}
,isc.A.selectAllRecords=function isc_ListGrid_selectAllRecords(){this.$491=true;this.selection.selectAll();this.$491=null;this.$52w("select all");if(this.getCurrentCheckboxField()!=null)this.$492(true);this.fireSelectionUpdated()}
,isc.A.deselectAllRecords=function isc_ListGrid_deselectAllRecords(){this.$491=true;this.selection.deselectAll();this.$491=null;this.$52w("deselect all");if(this.getCurrentCheckboxField()!=null)this.$492(false);this.fireSelectionUpdated()}
,isc.A.bodyKeyPress=function isc_ListGrid_bodyKeyPress(_1,_2){if(this.$49j){var _3=_1.keyTarget,_4;while(_4==null&&_3!=this&&_3!=null){_4=_3.canvasItem;_3=_3.parentElement}
if(_4!=null&&_4.form==this.getEditForm()){var _5=this.editorKeyPress(_4,isc.EH.getKey(),isc.EH.getKeyEventCharacterValue());return(_5==null?isc.EH.STOP_BUBBLING:_5)}}
if(this.data.getLength()>0){var _6=isc.EventHandler,_7=_1.keyName;var _8=this.editOnKeyPress&&this.isEditable();if(_8&&this.$540(_1,_2))return false;if(_7==this.$35a){return this.$18j(-1)}else if(_7==this.$35b){return this.$18j(1)}else if(_7==this.$51v){if(this.generateClickOnSpace)
if(this.$18k()==false)return false;if(this.generateDoubleClickOnSpace)
return this.$541()}else if(_7==this.$18n){if(this.generateClickOnEnter)
if(this.$18k()==false)return false;if(this.generateDoubleClickOnEnter)
return this.$541()}else if(_7==this.$51w&&this.editOnF2Keypress&&this.isEditable()&&this.editEvent!="none")
{var _9=this.getFocusRow();if(_9==null)_9=0;this.startEditing(_9);return false}}
return true}
,isc.A.$540=function isc_ListGrid__editOnKeyPress(_1,_2){var _3=_2.keyName,_4=isc.EH.getKeyEventCharacter(_1);if(_3!=this.$51w&&_3!=this.$51y&&_3!=this.$51x&&(_3==this.$kt||isc.EH.$kn[_3]||_4==null||_4==isc.emptyString))
{return false}
var _5=this.getFocusCell(),_6=_5[0]||0,_7=_5[1]||0;if(this.$49j)return false;var _8;if(_3==this.$18n||_3==this.$51w)_4=null;var _9;if(_4!=null){if(_3==this.$51y||_3==this.$51x){_9=null}else if(this.autoSelectEditors){_9=_4}else{_9=this.getEditedCell(_6,_7)+_4}
this.$542=true}
return this.handleEditCellEvent(_5[0],_5[1],this.$51z,_9)}
,isc.A.getArrowKeyAction=function isc_ListGrid_getArrowKeyAction(){var _1=this.arrowKeyAction;if(_1==this.$pr)return this.$pr;if(isc.EH.ctrlKeyDown())return this.$510;return _1}
,isc.A.$18j=function isc_ListGrid__navigateToNextRecord(_1){var _2=this.getArrowKeyAction();if(_2==this.$pr)return true;if(_1==null)_1=1;var _3;_3=this.getFocusRow(_1>0);if(_3==null)_3=this.$527;var _4=isc.isA.Number(_3)?_3:0;if(isc.isA.Number(_3))_3+=_1;else _3=0;var _5=this.getTotalRows()-1;if(_3<0||_3>_5){if(_5<0)return true;_3=_4}
while(!this.recordIsEnabled(_3,0)){_3+=_1;if(_3<0||_3>_5){_3=_4;break}}
if(isc.screenReader){this.body.$47u(_3)}
if(_2==this.$510)this.$18m(_3);else{if(_3==_4){var _6=this.$49z();if(this.body.selectionEnabled()&&this.recordIsEnabled(_3,_6))
{this.selection.selectOnMouseDown(this,_3,_6);this.selection.selectOnMouseUp(this,_3,_6)}}
else if(_2==this.$22c)this.$543(_3);else if(_2==this.$511)this.$544(_3)}
this.scrollRecordIntoView(_3)
return false}
,isc.A.$49z=function isc_ListGrid__getKeyboardClickNum(){var _1=this.keyboardClickField;if(_1==null)return 0;if(isc.isA.Number(_1)&&_1>0&&_1<this.fields.length)return _1;var _2=this.fields.find(this.fieldIdProperty,_1),_3=(_2?this.fields.indexOf(_2):0);return _3}
,isc.A.$543=function isc_ListGrid__generateRecordClick(_1){this.clearLastHilite();if(isc.isAn.Object(_1))_1=this.getRecordIndex(_1);if(!isc.isA.Number(_1)||_1<0)_1=0;if(_1>=this.data.getLength())_1=this.data.getLength()-1;this.body.$526=_1;var _2=this.$49z();var _3=(this.body.selectionEnabled()&&this.recordIsEnabled(_1,_2));if(_3)this.body.selectOnMouseDown(this,_1,_2);this.rowClick(this.getCellRecord(_1,_2),_1,_2,true);if(_3)this.body.selectOnMouseUp(this,_1,_2);return false}
,isc.A.getFocusRow=function isc_ListGrid_getFocusRow(_1){if(this.body.$526!=null&&((this.body.$526==this.body.lastOverRow)||(this.body.$526==this.$527)))
{return this.body.$526}
delete this.body.$526;var _2=this.getSelection();if(_2.length==0)return null;_2=_2[(_1?_2.length-1:0)]
return this.getRecordIndex(_2)}
,isc.A.getFocusCell=function isc_ListGrid_getFocusCell(){return[this.getFocusRow(),this.$49z()]}
,isc.A.$18k=function isc_ListGrid__generateFocusRecordClick(){var _1=this.getFocusRow();if(_1!=null){this.$543(_1);return false}
return true}
,isc.A.$544=function isc_ListGrid__generateRecordDoubleClick(_1){var _2=this.$49z();this.rowDoubleClick(this.getCellRecord(_1,_2),_1,_2,true)}
,isc.A.$541=function isc_ListGrid__generateFocusRecordDoubleClick(){var _1=this.$49z(),_2=this.getFocusRow();if(_2!=null){this.$544(_2);return false}
return true}
,isc.A.scrollRecordToTop=function isc_ListGrid_scrollRecordToTop(_1){return this.scrollRecordIntoView(_1,false)}
,isc.A.scrollRecordIntoView=function isc_ListGrid_scrollRecordIntoView(_1,_2){return this.scrollCellIntoView(_1,null,_2)}
,isc.A.scrollColumnIntoView=function isc_ListGrid_scrollColumnIntoView(_1,_2){return this.scrollCellIntoView(null,_1,_2)}
,isc.A.scrollToRow=function isc_ListGrid_scrollToRow(_1){this.scrollCellIntoView(_1,0);return this}
,isc.A.scrollCellCallback=function isc_ListGrid_scrollCellCallback(_1,_2,_3,_4,_5){if(_5==this.$545){this.$545=null;this.scrollCellIntoView(_1,_2,_3,_4)}}
,isc.A.scrollCellIntoView=function isc_ListGrid_scrollCellIntoView(_1,_2,_3,_4){if((isc.isAn.Array(this.data)&&this.data.length==0&&this.dataSource)||(isc.ResultSet&&isc.isA.ResultSet(this.data)&&!this.data.lengthIsKnown())){if(!this.$545)this.$545=1;else this.$545+=1;var _5=this.$545;isc.Page.waitFor(this,"dataArrived",{method:this.scrollCellCallback,args:[_1,_2,_3,_4,_5],target:this});return}
if(!this.body||!this.body.isDrawn()){this.logInfo("scrollCellIntoView() called before the body has been drawn.  Cell "+_1+","+_2+" will scrolled into view on draw().");this.$463=[_1,_2];return}
if(_3==null)_3=true;var x,y,_8,_9,_10=this.body;if(_1!=null){if(!_10.$45x){y=_10.getRowTop(_1);_9=_10.getRowSize(_1)}else{var _11=_10.$6i==null||_1<_10.$6i||_1>_10.$459,_12=!_11;if(_12){var _13=_10.getRowTop(_1),_14=_10.getRowHeight?_10.getRowHeight(this.getCellRecord(_1),_1):_10.cellHeight,_15=_10.getScrollTop();if(_15>_13||((_10.getViewportHeight()+_15)<(_13+_14)))
{_12=false}}
if(!_12){_10.$45y=_1;if(_3){_10.$45z=-1*((_10.getViewportHeight()/2)-_10.cellHeight)}else{_10.$45z=0}
_10.$450()}
return}}
if(_2!=null){x=this.getColumnLeft(_2);_8=this.getColumnWidth(_2)}
if(this.isDirty()||this.body.isDirty()){var _16;if(_1!=null){var _17=_10.getScrollHeight();if(y+_9>_17)_16=true}
if(!_16&&_2!=null){var _18=_10.getScrollWidth();if(x+_8>_18)_16=true}
if(_16)this.redraw("scrollIntoView")}
_10.scrollIntoView(x,y,_8,_9,(_3?"center":"left"),(_3?"center":"top"),null,null,_4)}
,isc.A.bodyScrolled=function isc_ListGrid_bodyScrolled(_1,_2,_3){if(_3){this.body.$49i=true;var _4=this.frozenBody;if(_4.$454){this.body.$45y=_4.$45y;this.body.$45z=_4.$45z;this.body.$451=_4.$451;this.body.$450()}else{this.body.scrollTo(null,_2,"scrollSync")}
delete this.body.$49i
return}
if(this.frozenBody!=null){this.frozenBody.$49i=true;var _5=this.body,_4=this.frozenBody;if(_5.$454){_4.$45y=_5.$45y;_4.$45z=_5.$45z;_4.$451=_5.$451;_4.$450()}else{_4.scrollTo(null,_2,"bodyScrollSync")}
delete this.frozenBody.$49i}
this.syncHeaderScrolling(_1,_2);this.syncFilterEditorScrolling(_1,_2);this.syncSummaryRowScrolling(_1,_2);if(this.$49j&&this.$49u){var _6=this.getEditForm(),_7=this.getEditRow(),_8=this.getEditCol(),_9=_6.getItem(this.getEditorName(_7,_8));if(_9){if(!_9.hasFocus&&(_6.hasFocus||isc.EH.getFocusCanvas()==null))
{this.$49v(_8)}else{delete this.$49u}}}}
,isc.A.syncHeaderScrolling=function isc_ListGrid_syncHeaderScrolling(_1,_2){if(_1!=null&&this.header){if(!this.isRTL()){if(_1!=this.header.getScrollLeft())this.header.scrollTo(_1,null,"headerScrollSync")}else{var _3=this.header,_4=this.body,_5=_3.getScrollWidth()-_3.getViewportWidth(),_6=_5-_3.getScrollLeft(),_7=_4.getScrollWidth()-_4.getViewportWidth(),_8=_7-_1;if(_8!=_6){_3.scrollTo(_5-_8,null,"scrollSync")}}}}
,isc.A.headerScrolled=function isc_ListGrid_headerScrolled(){if(!this.$546){this.$546=this.delayCall("syncBodyScrolling")}}
,isc.A.syncBodyScrolling=function isc_ListGrid_syncBodyScrolling(){delete this.$546;var _1=this.header.getScrollLeft();if(this.body){if(!this.isRTL()){if(_1!=this.body.getScrollLeft())this.body.scrollTo(_1,null,"scrollSync")}else{var _2=this.header,_3=this.body,_4=_2.getScrollWidth()-_2.getViewportWidth(),_5=_4-_2.getScrollLeft(),_6=_3.getScrollWidth()-_3.getViewportWidth(),_7=_6-_1;if(_7!=_5){_3.scrollTo(_6-_5,null,"scrollSync")}}}}
,isc.A.syncFilterEditorScrolling=function isc_ListGrid_syncFilterEditorScrolling(_1,_2){if(this.filterEditor!=null&&this.filterEditor.body!=null&&this.filterEditor.body.getScrollLeft()!=_1)
{this.filterEditor.body.scrollTo(_1,null,"scrollSync")}}
,isc.A.syncSummaryRowScrolling=function isc_ListGrid_syncSummaryRowScrolling(_1,_2){if(this.summaryRow!=null&&this.showGridSummary&&this.summaryRow.body!=null&&this.summaryRow.body.getScrollLeft()!=_1)
{this.summaryRow.body.scrollTo(_1,null,"scrollSync")}}
,isc.A.$18m=function isc_ListGrid__hiliteRecord(_1){if(!isc.isA.Number(_1)){_1=this.getRecordIndex(_1)}
if(!isc.isA.Number(_1)||_1<0)_1=0;if(_1>=this.data.getLength())_1=this.data.getLength()-1;this.clearLastHilite();this.body.$526=_1;this.body.lastOverRow=_1;this.body.lastOverCol=0;this.bodies.map("setRowStyle",_1)}
,isc.A.clearLastHilite=function isc_ListGrid_clearLastHilite(){if(!this.body)return;this.body.$526=null;var _1=this.body.lastOverRow;if(isc.isA.Number(_1)){delete this.body.lastOverRow;if(this.showRollOver)this.body.updateRollOver(_1)}}
,isc.A.setAccessKey=function isc_ListGrid_setAccessKey(_1){this.Super("setAccessKey",arguments)
if(this.body!=null)this.body.setAccessKey(_1)}
,isc.A.setFocus=function isc_ListGrid_setFocus(_1){if(this.body!=null)this.body.setFocus(_1)}
,isc.A.focusAtEnd=function isc_ListGrid_focusAtEnd(_1){if(!this.$547){this.$547=true;var _2=false;var _3=isc.EH.getFocusCanvas();if(this.contains(_3)||(isc.isA.DynamicForm(_3)&&(_3.grid==this||this.contains(_3.grid))))
{_2=true}
if(_2)this.$l5(_1);this.$547=false;if(_2)return}
if(_1)this.members[0].focusAtEnd(_1);else this.members.last().focusAtEnd(_1)}
,isc.A.$mb=function isc_ListGrid__canFocus(){if(this.body)return this.body.$mb();return false}
,isc.A.recordClick=function isc_ListGrid_recordClick(){}
,isc.A.recordDoubleClick=function isc_ListGrid_recordDoubleClick(){}
,isc.A.setShowGridSummary=function isc_ListGrid_setShowGridSummary(_1){if(this.showGridSummary==_1)return;this.showGridSummary=_1;if(this.showGridSummary){this.showSummaryRow()}else{this.clearSummaryRow()}}
,isc.A.recalculateSummaries=function isc_ListGrid_recalculateSummaries(_1,_2){_2=(_2!=null?_2:true);this.calculateRecordSummaries(_1,_2,true);this.$52u(_2)}
,isc.A.$52u=function isc_ListGrid__recalculateSummaries(_1){if(this.showGridSummary&&this.summaryRow!=null&&_1!=false){this.summaryRow.$52u()}
if(this.showGroupSummary){this.refreshGroupSummary()}}
,isc.A.recalculateGridSummary=function isc_ListGrid_recalculateGridSummary(){if(this.showGridSummary&&this.summaryRow!=null){this.summaryRow.$52u()}}
,isc.A.shouldShowGridSummary=function isc_ListGrid_shouldShowGridSummary(_1){if(_1.showGridSummary!=null)return _1.showGridSummary;return(_1.getGridSummary!=null||this.getGridSummaryFunction(_1)!=null)}
,isc.A.getGridSummaryFunction=function isc_ListGrid_getGridSummaryFunction(_1){if(!_1)return;var _2=_1.summaryFunction||isc.SimpleType.getDefaultSummaryFunction(_1.type);return _2}
,isc.A.getGridSummary=function isc_ListGrid_getGridSummary(_1){if(!_1||!this.data||(isc.isA.ResultSet(this.data)&&!this.data.lengthIsKnown()))
return;var _2=this.getOriginalData(),_3=isc.ResultSet&&isc.isA.ResultSet(_2),_4=isc.isA.Tree(_2);if(_4){_2=_2.getDescendants(_2.getRoot());_3=isc.ResultSet&&isc.isA.ResultSet(_2)}
if(_3&&!_2.allMatchingRowsCached()){this.logWarn("Unable to show summary values - dataset not completely loaded");return}
var _5=_3?_2.getRange(0,_2.getLength()):_2;var _6=this.getAllEditRows();if(_6!=null&&_6.length>0){_5=_5.duplicate();for(var i=0;i<_5.length;i++){var _8=_5[i];var _9=this.getEditSessionRowNum(_8);if(_9!=null)_5[i]=this.getEditedRecord(_9,null,true)}}
if(_1.getGridSummary){var _10;if(this.isGrouped&&this.showGroupSummary&&(this.groupTree!=null)){_10=this.assembleGroupSummaries()}
return _1.getGridSummary(_5,_1,_10)}
return this.getSummaryValue(_5,_1)}
,isc.A.getSummaryRowDataSource=function isc_ListGrid_getSummaryRowDataSource(){return this.summaryRowDataSource}
,isc.A.getSummaryRowCriteria=function isc_ListGrid_getSummaryRowCriteria(){if(this.summaryRowCriteria!=null)return this.summaryRowCriteria;var _1=this.getOriginalData();if(isc.ResultSet&&isc.isA.ResultSet(_1)){return this.data.getCriteria()}
return this.getInitialCriteria()}
,isc.A.getSummaryRowFetchRequestConfig=function isc_ListGrid_getSummaryRowFetchRequestConfig(){return isc.addProperties({textMatchStyle:this.autoFetchTextMatchStyle,showPrompt:false,startRow:0,endRow:1},this.summaryRowFetchRequestDefaults,this.summaryRowFetchRequestProperties)}
,isc.A.getGridSummaryData=function isc_ListGrid_getGridSummaryData(_1){if(this.$548&&!_1)return this.$548;var _2=this.completeFields||this.fields,_3=[];for(var i=0;i<_2.length;i++){var _5=_2[i],_6=_2[i].name;if(!this.shouldShowGridSummary(_5))continue;var _7=this.getGridSummary(_5);if(!isc.isAn.Array(_7)){_7=[_7]}
for(var _8=0;_8<_7.length;_8++){if(_3[_8]==null){_3[_8]={};_3[_8][this.gridSummaryRecordProperty]=true;_3[_8][this.recordCanSelectProperty]=false}
_3[_8][_6]=_7[_8]}}
this.$548=_3;return _3}
,isc.A.getGroupSummaryData=function isc_ListGrid_getGroupSummaryData(_1,_2){var _3=[];for(var i=0;i<this.fields.getLength();i++){var _5=this.getField(i),_6=_5.name,_7;if(this.shouldShowGroupSummary(_5)){_7=this.getGroupSummary(_1,_5,_2);if(!isc.isAn.Array(_7))_7=[_7];for(var _8=0;_8<_7.length;_8++){var _9=_3[_8];if(_9==null){_9=_3[_8]={};_9.customStyle=this.groupSummaryStyle;_9[this.recordEnabledProperty]=false;_9[this.includeInSummaryProperty]=false;_9[this.groupSummaryRecordProperty]=true}
var _10=_7[_8];if(_10==null){_10=this.invalidSummaryValue}else{if(_5.formatGroupSummary){if(isc.isA.String(_5.formatGroupSummary)){_5.formatGroupSummary=isc.Func.expressionToFunction("value",_5.formatGroupSummary)}
_10=_5.formatGroupSummary(_10)}else{var _11=this.getGridSummaryFunction(_5);if(!isc.isAn.Array(_11))_11=[_11];if(_11[_8]=="count"){var _12=_5.pluralTitle;if(_12==null)_12=_5.title;if(_12!=null)_10+=" "+_12}}}
_9[_6]=_10}}}
return _3}
,isc.A.assembleGroupSummaries=function isc_ListGrid_assembleGroupSummaries(_1,_2){var _3=this.groupTree;if(!_1)_1=_3.getRoot();if(!_2)_2=[];var _4=_3.getFolders(_1);for(var i=0;i<_4.length;i++){var _6=_4[i];if(this.groupByFieldSummaries==null||this.groupByFieldSummaries.contains(_6.groupName))
{var _7=_3.combineWithEditVals(_3.getRecordsInGroup(_6));var _8=this.getGroupSummaryData(_7,_6);for(var _9=0;_9<_8.length;_9++){var _10=isc.addProperties({},_8[_9]);delete _10.customStyle;delete _10[this.recordEnabledProperty];delete _10[this.includeInSummaryProperty];_10.groupName=_6.groupName;_10.groupValue=_6.groupValue;_2.add(_10)}}
this.assembleGroupSummaries(_4[i],_2)}
return _2}
,isc.A.getSummaryValue=function isc_ListGrid_getSummaryValue(_1,_2){var _3=[];for(var i=0;i<_1.length;i++){var _5=_1[i];if(!_5||(_5[this.includeInSummaryProperty]==false))continue;_3[_3.length]=_5}
var _6=this.getGridSummaryFunction(_2);if(_6!=null){if(!isc.isAn.Array(_6)){_6=[_6]}
var _7=[];for(var i=0;i<_6.length;i++){var _8=_6[i];if(_8!=null){_7[i]=isc.SimpleType.applySummaryFunction(_3,_2,_8)}}
return _7}
return null}
,isc.A.shouldShowGroupSummary=function isc_ListGrid_shouldShowGroupSummary(_1){if(_1.showGroupSummary!=null)return _1.showGroupSummary;return(_1.getGroupSummary!=null||this.getGridSummaryFunction(_1)!=null)}
,isc.A.getGroupSummary=function isc_ListGrid_getGroupSummary(_1,_2,_3){var _4;if(_2.getGroupSummary!=null){_4=_2.getGroupSummary(_1,_2,_3)}else{_4=this.getSummaryValue(_1,_2)}
return _4}
,isc.A.getRecordSummary=function isc_ListGrid_getRecordSummary(_1,_2){var _3=isc.isAn.Object(_1)?_1:this.getEditedRecord(_1);if(_2.getRecordSummary!=null){return _2.getRecordSummary(_3,_2)}
var _4=[],_5=this.fields;for(var i=0;i<_5.length;i++){var _7=_5[i];if(_7.name==_2.name){if(_2.partialSummary)break;continue}
var _8=_7.includeInRecordSummary;if(_8==null&&(_7.type=="integer"||_7.type=="float")){_8=true}
if(_8&&_7.includeInRecordSummaryFields!=null){if((isc.isA.String(_7.includeInRecordSummaryFields)&&_7.includeInRecordSummaryFields!=_2.name)||(isc.isAn.Array(_7.includeInRecordSummaryFields)&&!_7.includeInRecordSummaryFields.contains(_2.name)))
{_8=false}}
if(_8){_4.add(_7)}}
var _9=_2.recordSummaryFunction||"sum";var _10=isc.DataSource.applyRecordSummaryFunction(_9,_3,_4,_2);return _10}
,isc.A.shouldApplyRecordSummaryToRecord=function isc_ListGrid_shouldApplyRecordSummaryToRecord(_1){return _1&&(_1.type==this.$51n)&&(_1.summaryFunction!=null||_1.getGroupSummary!=null)}
,isc.A.calculateRecordSummaries=function isc_ListGrid_calculateRecordSummaries(_1,_2,_3){if(!this.fields)return;if(_1==null){_1=this.getOriginalData()}
if(_1==null||(isc.isA.ResultSet(_1)&&!_1.lengthIsKnown()))
return;var _4=false;var _5=[];for(var i=0;i<this.fields.length;i++){var _7=this.getField(i);if(this.shouldApplyRecordSummaryToRecord(_7)){_5.add(_7)}}
if(_5.length>0){for(var i=0;i<_1.getLength();i++){var _8=_1.get(i),_9=this.getEditValues(_8),_10=_9?isc.addProperties({},_8,_9):_8;if(_8==null)continue;for(var _11=0;_11<_5.length;_11++){var _7=_5[_11];if(!this.shouldShowRecordSummary(_7,_8))continue;var _12=_8[_7.name];var _13=this.getRecordSummary(_10,_7);_8[_7.name]=_13;if(!this.fieldValuesAreEqual(_7,_12,_13)){_4=true}}}}
if((_3||_4)&&!_2){if(!this.isDirty())this.markForRedraw();if(this.showGridSummary&&this.summaryRow)this.summaryRow.$52u()}}
,isc.A.getSummaryRow=function isc_ListGrid_getSummaryRow(){if(!this.summaryRow){var _1=this.getSummaryRowDataSource();var _2;if(this.completeFields)_2=this.completeFields.duplicate();else if(this.fields)_2=this.fields.duplicate();var _3=this.summaryRowHeight;this.summaryRow=this.createAutoChild("summaryRow",{warnOnReusedFields:false,autoDraw:false,width:"100%",height:_3,autoFitData:"vertical",bodyOverflow:"hidden",showHeader:false,getBaseStyle:function(){return this.creator.summaryRowStyle},alternateRecordStyles:false,disabled:this.disabled,$54o:function(){},$53g:function(_9){return false},dataSource:_1,data:_1==null?this.getGridSummaryData():null,$52u:function(){var _4=this.creator;if(this.dataSource!=null){var _5=_4.getSummaryRowCriteria(),_6=isc.ResultSet&&isc.isA.ResultSet(this.data)&&!this.data.willFetchData(_5);this.fetchData(_5,null,_4.getSummaryRowFetchRequestConfig());if(_6)this.invalidateCache()}else{this.setData(_4.getGridSummaryData(true))}},fieldSourceGrid:this,fields:_2,skipAutoFitWidths:true,getFieldWidths:function(){return this.creator.getFieldWidths()},$32v:function(_9,_10,_11,_12,_13){var _4=this.creator;if(_4.shouldShowGridSummary(_11)){if(_9==null)return this.invalidSummaryValue;if(_11.formatGridSummary){if(!isc.isA.Function(_11.formatGridSummary)){_11.formatGridSummary=isc.Func.expressionToFunction("value",_11.formatGridSummary)}
if(isc.isA.Function(_11.formatGridSummary)){return _11.formatGridSummary(_9)}}else{var _7=this.creator.getGridSummaryFunction(_11);if(!isc.isAn.Array(_7)){_7=[_7]}
if(_7[_12]=="count"){var _8=_11.pluralTitle;if(_8==null)_8=_11.title;if(_8!=null)_9+=" "+_8;return _9}}
return this.Super("$32v",arguments)}
return this.Super("$32v",arguments)}})}else{this.summaryRow.setDataSource(this.getSummaryRowDataSource());this.summaryRow.setFields(this.completeFields.duplicate());this.summaryRow.$52u()}
return this.summaryRow}
,isc.A.showSummaryRow=function isc_ListGrid_showSummaryRow(){var _1=this.gridComponents.indexOf("summaryRow");if(_1==-1){this.logWarn("showGridSummary set to true, but gridComponents array does not include an "+"entry for the summary row - not showing.");return}
this.updateGridComponents()
this.syncSummaryRowScrolling(this.body.getScrollLeft(),this.body.getScrollTop)}
,isc.A.clearSummaryRow=function isc_ListGrid_clearSummaryRow(){if(this.summaryRow&&this.summaryRow.parentElement==this){this.removeMember(this.summaryRow)}}
,isc.A.setShowFilterEditor=function isc_ListGrid_setShowFilterEditor(_1){if(this.showFilterEditor==_1)return;this.showFilterEditor=_1;if(_1){var _2=this.gridComponents.indexOf("filterEditor");if(_2==-1){_1=false;this.logWarn("setShowFilterEditor(true) called, but gridComponents array does not "+"include the filterEditor. FilterEditor will not be shown.")}}
if(_1){if(this.isDrawn()){this.updateGridComponents()}}else if(this.filterEditor){this.filterEditor.destroy();this.filterEditor=null}
this.layoutChildren()}
,isc.A.makeFilterEditor=function isc_ListGrid_makeFilterEditor(){this.filterEditor=isc.RecordEditor.create({autoDraw:false,warnOnReusedFields:false,$54o:function(){},height:this.filterEditorHeight,disabled:this.disabled,sourceWidget:this,ID:this.getID()+"filterEditor",fieldShouldBeVisible:function(_1,_2){return this.sourceWidget.fieldShouldBeVisible(_1,_2)},fieldSourceGrid:this,actionType:"filter",actionButtonPrompt:this.filterButtonPrompt,actionButtonProperties:this.filterButtonProperties,fetchDelay:this.fetchDelay,allowFilterExpressions:this.allowFilterExpressions,expressionDataSource:this.getDataSource()},this.filterEditorDefaults,this.filterEditorProperties)}
,isc.A.getFilterEditorValueMap=function isc_ListGrid_getFilterEditorValueMap(_1){return _1.filterEditorValueMap||_1.valueMap}
,isc.A.getFilterEditorType=function isc_ListGrid_getFilterEditorType(_1){if(_1.filterEditorType!=null)return _1.filterEditorType;if(isc.SimpleType.inheritsFrom(_1.type,"date")&&this.getDataSource()&&this.getDataSource().supportsAdvancedCriteria())
{return"MiniDateRangeItem"}
var _2=isc.addProperties({},_1,{canEdit:_1.canFilter!==false,length:null});if(_2._constructor!=null)delete _2._constructor;if(_1.filterEditorType!=null)_2.editorType=_1.filterEditorType;isc.addProperties(_2,_1.filterEditorProperties);var _3=isc.DynamicForm.getEditorType(_2,this);return _3}
,isc.A.getFilterEditorProperties=function isc_ListGrid_getFilterEditorProperties(_1){var _2=_1.filterEditorProperties||{};if(_1.filterOperator)_2.operator=_1.filterOperator;return _2}
,isc.A.getFilterEditor=function isc_ListGrid_getFilterEditor(){return this.filterEditor}
,isc.A.setFilterEditorCriteria=function isc_ListGrid_setFilterEditorCriteria(_1){if(this.filterEditor){this.setFilterValues(_1)}}
,isc.A.getFilterEditorCriteria=function isc_ListGrid_getFilterEditorCriteria(_1){if(this.filterEditor){if(!_1||!this.filterEditor.getEditForm()){return this.filterEditor.getValuesAsCriteria(this.autoFetchTextMatchStyle)}else{var _2=this.filterEditor.getEditForm().getItems(),_3=true,_4={},_5={operator:"and",criteria:[]};for(var i=0;i<_2.length;i++){if(_2[i].hasAdvancedCriteria()){var _7=_2[i].getCriterion();if(_7!=null){_3=false;_5.criteria.add(_7)}}else{var _8=_2[i].getValue();if(_8!=null){_4[_2[i].getFieldName()]=_8}}}
if(!_3){_4=isc.DataSource.combineCriteria(_4,_5)}
return _4}}}
,isc.A.setCriteria=function isc_ListGrid_setCriteria(_1){if(this.filterEditor!=null){this.setFilterValues(_1)}
return this.Super("setCriteria",arguments)}
,isc.A.setFilterValues=function isc_ListGrid_setFilterValues(_1){this.$549=isc.addProperties({},_1);this.updateFilterEditor()}
,isc.A.updateFilterEditor=function isc_ListGrid_updateFilterEditor(){var _1=this.filterEditor;if(!_1)return;var _2=this.$55a();this.filterEditor.setValuesAsCriteria(_2,true)}
,isc.A.$55a=function isc_ListGrid__getFilterEditorValues(){var _1=isc.addProperties({},this.$549);if(this.updateFilterEditorValues!=null){_1=this.updateFilterEditorValues(_1,this.autoFetchTextMatchStyle)}
if(_1==null){_1={};for(var i=0;i<this.completeFields.length;i++){_1[this.completeFields[i].name]=this.completeFields[i].defaultFilterValue}}
return _1}
,isc.A.clearFilterValues=function isc_ListGrid_clearFilterValues(){this.$549=null;this.updateFilterEditor()}
,isc.A.handleFilterEditorSubmit=function isc_ListGrid_handleFilterEditorSubmit(_1,_2){if(this.filterEditorSubmit!=null&&this.filterEditorSubmit(_1)==false)return;this.filterData(_1,null,_2)}
,isc.A.getInitialCriteria=function isc_ListGrid_getInitialCriteria(){var _1={},_2,_3=this.getFields(),_4;for(var i=0;i<_3.length;i++){if(_3[i].defaultFilterValue!==_4){_2=true;var _6=this.getFieldName(_3[i]);_1[_6]=_3[i].defaultFilterValue}}
if(!_2){_1=this.initialCriteria||this.getCriteria()}else{isc.addProperties(_1,this.initialCriteria||this.getCriteria())}
return _1}
,isc.A.$08=function isc_ListGrid__adjustSpecialPeers(_1){if(this.filterEditor!=null)this.filterEditor.setZIndex(_1-1);return this.Super("$08",arguments)}
,isc.A.canEditCell=function isc_ListGrid_canEditCell(_1,_2){if(_2<0||_2>=this.fields.length)return false;var _3=this.getCellRecord(_1,_2);if(_3!=null){if(!this.recordIsEnabled(_1,_2))return false;if(this.$54z==_3)return false}
var _4=this.getField(_2);if(_4&&_4.disabled)return false;if(_4&&_4.type=="summary")return false;if(!this.isEditable()||this.getCellBooleanProperty("canEdit",_1,_2,(_3!=null?this.recordEditProperty:null))==false){return false}
return true}
,isc.A.isEditable=function isc_ListGrid_isEditable(){if(this.canEdit==false)return false;if(this.canEdit==true||this.canEdit==this.$302){var _1=this.getFields()||[];for(var i=0;i<_1.length;i++){if(_1[i].canEdit!=false)return true}
return false}else{var _1=this.getFields()||[];for(var i=0;i<_1.length;i++){if(_1[i].canEdit==true)return true}
return false}}
,isc.A.setCanEdit=function isc_ListGrid_setCanEdit(_1){if(_1==false){if(this.getEditRow()!=null)this.cancelEditing(isc.ListGrid.PROGRAMMATIC);this.canEdit=false}else{this.canEdit=_1}}
,isc.A.setFieldCanEdit=function isc_ListGrid_setFieldCanEdit(_1,_2){if(isc.isA.String(_1))_1=this.getField(_1);if(_1==null||!this.completeFields.contains(_1)||_1.canEdit==_2)return;_1.canEdit=_2;if(this.$49j){var _3=this.getEditRow(),_4=_1[this.fieldIdProperty],_5=this.getColNum(_1);if(this.editByCell){if(!_2&&_5==this.getEditCol()){this.cancelEditing(isc.ListGrid.PROGRAMMATIC)}}else if(_5>=0){if(!_2&&_5==this.getEditCol()){var _6=this.$6l.getItem(_4),_7=_6.hasFocus;var _8=this.findNextEditCell(_3,_5,-1,true,false,false,true);if(_8==null||_8[0]!=_3)
_8=this.findNextEditCell(_3,_5,1,true,false,false,true);if(_8==null||_8[0]!=_3){this.cancelEditing(isc.ListGrid.PROGRAMMATIC);return}
this.startEditing(_8[0],_8[1],!_7)}
this.refreshCell(this.getEditRow(),_5)}}}
,isc.A.handleEditCellEvent=function isc_ListGrid_handleEditCellEvent(_1,_2,_3,_4){if(_3==this.$51z)this.$502=true;else delete this.$502
if(_1<0||_2<0)return false;if(this.editByCell){if(!this.canEditCell(_1,_2))return false}else{var _5=this.findNextEditCell(_1,_2,-1,true,true,false,true);if(_5==null||_5[0]!=_1)
_5=this.findNextEditCell(_1,_2,1,true,false,false,true);if(_5==null||_5[0]!=_1)return false;_2=_5[1]}
var _6;if(_4!==_6){this.setEditValue(_1,_2,_4)}
return this.startEditing(_1,_2,null,_3)}
,isc.A.startEditing=function isc_ListGrid_startEditing(_1,_2,_3,_4,_5){if(!this.canEdit&&!(this.completeFields||this.fields).getProperty("canEdit").or()){this.canEdit=true}
if(this.completeFields==null)this.setFields(this.fields);var _6=(_1==null),_7=(_2==null);if(_6||_7){var _8=(_6?0:_1),_9=(_7?0:_2);var _10=this.findNextEditCell(_8,_9,1,_7,true);if(_10==null){this.logInfo("startEditing() passed bad cell coordinates:"+[_1,_2],"gridEdit")}else{this.logInfo("startEditing() using derived coordinates:"+_10,"gridEdit");_1=_10[0];_2=_10[1]}}
if(_1==null||_1<0||_1>this.getTotalRows()){if(!_5){this.logWarn("startEditing() passed bad cell coordinates:"+[_1,_2]+", can't edit"+this.getStackTrace(),"gridEdit")}
return false}
if(!this.canEditCell(_1,_2)){this.logInfo("startEditing(): cell "+[_1,_2]+" is non editable. Returning.","gridEdit");return false}
if(this.$49j){this.$55b((_4||isc.ListGrid.PROGRAMMATIC),this.getEditRow(),this.getEditCol(),_1,_2)}else{this.$53j(_1,_2,_3)}
return true}
,isc.A.$55b=function isc_ListGrid__changeEditCell(_1,_2,_3,_4,_5){var _6=this.getEditValue(_2,_3);var _7=this.getFieldName(_3),_8=this.$6l,_9=_8?_8.getItem(_7):null,_10=_9?!_9.$506:true;if(_9){delete _9.$506;delete _9.$505;if(this.$55c(_9,_2,_4,_5)){this.$55d(_9,_3)}}
var _11=(_4!=_2),_12=this.$507(_2,_3);if(_10){var _13=!this.$55e(_1,_2,_3,_6);if(_11&&!_13){_13=!this.$55f(_1,_2,_12)}
if(_13){if(_1==isc.ListGrid.EDIT_FIELD_CHANGE){var _14=this.getFieldName(_5);if(_9&&_8.getItem(_14).hasFocus){_9.focusInItem()}}
return false}}
var _15=(this.autoSaveEdits&&((_11&&this.shouldSaveOnRowExit(_2,_3,_1))||this.shouldSaveOnCellExit(_2,_3,_1)));if(!_15&&this.$55g()){var _16,_17=_11&&this.shouldValidateByRow(_2,_3,_1);if(_17&&!this.usingCellRecords){_16=!this.validateRow(_2)}else{if(_17||this.shouldValidateByCell(_2,_3,_1))
{_16=!this.validateCell(_2,_3)}}
if(this.stopOnErrors&&_16)return false}
if(_15){return this.$55h(_4,_5,_1)}else{this.$53j(_4,_5)}
if(_11&&this.isGrouped&&(!_15||!this.shouldSaveLocally())){this.$55i(_2)}}
,isc.A.$55i=function isc_ListGrid__updateGroupForEditValueChange(_1){if(!this.isGrouped||!isc.isA.Tree(this.data))return;var _2=this.data.get(_1),_3=_2,_4=false;if(_2==null)return;var _5=this.getGroupByFields();if(!isc.isAn.Array(_5))_5=[_5];var _6=this.getEditedRecord(_1);for(var i=_5.length-1;i>=0;i--){var _8=_5[i],_3=this.data.getParent(_3);if(_3==null||_3.groupName!=_8){this.logWarn("error updating group for edit value change - unexpected group "+"tree structure. Regrouping.");_4=true;break}
var _9=_6[_8];if(_3.groupValue!=_9){_4=true;break}}
if(_4){this.regroup();this.$52s();this.markForRedraw()}
return _4}
,isc.A.$55c=function isc_ListGrid__shouldParkFocus(_1,_2,_3,_4){if(!isc.Browser.isIE)return false;var _5=(isc.isA.PopUpTextAreaItem(_1)&&_1.$55j&&_1.$36t.hasFocus)||(_1.hasFocus&&isc.FormItem.$23c(_1));if(!this.editByCell&&_3==_2)return false;var _6=this.getEditorType(this.getField(_4),this.getCellRecord(_3,_4));return!(_6==null||isc.FormItem.$23c(_6,true))}
,isc.A.$53j=function isc_ListGrid__startEditing(_1,_2,_3){if(_1=="delayed"){var _4=this.$55k;if(_4==null)return;_1=_4[0];_2=_4[1];_3=_4[2]}else if(this.isDrawn()&&(!this.body.readyToRedraw()||(this.frozenBody&&!this.frozenBody.readyToRedraw())))
{if(!this.$55k){this.delayCall("$53j",['delayed'],0)}
this.$55k=[_1,_2,_3];return}
delete this.$55k;var _5=this.getEditRow()!=_1;if(!_5&&!_3&&(this.getEditCol()==_2)){this.getEditForm().focusInItem(this.getEditorName(_1,_2));return}
this.logInfo("Starting editing at row "+_1+", colNum "+_2,"gridEdit");if(this.$54z!=null)this.closeRecord();if(this.$49j){if(this.editByCell||_5){this.hideInlineEditor(false,true)}}
var _6=(this.$55l==null)||_5||this.saveByCell;if(_6){this.$55l=this.$55m()}
var _7=this.$49j&&(this.getEditRow()==_1)
this.initializeEditValues(_1,_2,_7);var _8=this.getCellRecord(_1,_2);if(this.selectOnEdit&&_8!=null)this.selectRecordForEdit(_8);if(this.modalEditing)this.clearLastHilite();if(_8==null&&this.addNewBeforeEditing){this.$471=_1;this.$504=_2;var _9=this.shouldWaitForSave();var _10="this.$55n("+_9+","+_3+")";this.saveEdits(isc.ListGrid.PROGRAMMATIC,_10);if(_9)return;else{_1=this.$471;_2=this.$504}}
this.showInlineEditor(_1,_2,true,_5,_3);return true}
);isc.evalBoundary;isc.B.push(isc.A.selectRecordForEdit=function isc_ListGrid_selectRecordForEdit(_1){if(!this.editByCell)_1.$48j=true;if(this.canSelectCells){var _2=this.getRecordCellIndex(_1);this.selection.selectSingleCell(_2[0],_2[1])}else if(this.selection!=null&&(!this.selection.isSelected(_1)||this.selection.multipleSelected())){if(this.selectionType==isc.Selection.NONE){this.logInfo("selectOnEdit is true, but this.selectionType is set to 'none'."+" Unable to perform a selection on edit.","gridEdit")}else if(this.selectionType==isc.Selection.SIMPLE)this.selection.select(_1);else this.selection.selectSingle(_1)}
delete _1.$48j}
,isc.A.$55o=function isc_ListGrid__updateEditorSelection(_1){if(!isc.isA.TextItem(_1)&&!isc.isA.TextAreaItem(_1)&&!(isc.isA.DateItem(_1)&&_1.useTextField))return;if(_1.$55p)return;var _2=isc.isA.DateItem(_1)?_1.dateTextField.getDataElement():_1.getDataElement();if(!_2)return;if(this.$542||!this.autoSelectEditors){var _3=_1.getDataElement().value||"";_1.setSelectionRange(_3.length,_3.length);delete this.$542}else{_2.select()}}
,isc.A.$55n=function isc_ListGrid__updateNewEditRowValues(_1,_2){var _3=this.$471,_4=this.$504,_5=this.getCellRecord(_3,_4);if(_5!=null&&_5!="loading"){var _6=this.getEditValues(_3,_4);for(var i in _5){if(_5[i]!=null&&_6[i]==null){_6[i]=_5[i]}}}
if(_1)this.showInlineEditor(_3,_4,true,true,_2);else this.updateEditRow(_3)}
,isc.A.editField=function isc_ListGrid_editField(_1,_2){if(this.completeFields==null)this.setFields(this.fields);var _3;if(isc.isA.Number(_1))_3=_1;else _3=this.fields.findIndex(this.fieldIdProperty,_1);if(_2==null){_2=this.getEditRow();if(_2==null){this.logWarn("editField(): unable to determine which row to edit - returning.","gridEdit");return}}
return this.startEditing(_2,_3)}
,isc.A.showInlineEditor=function isc_ListGrid_showInlineEditor(_1,_2,_3,_4,_5){if(this.$49j){if(_1!=this.getEditRow()||(this.editByCell&&_2!=this.getEditCol())){this.logWarn("Unexpected call to 'showInlineEditor' during another edit "+"- cancelling previous edit","gridEdit");this.cancelEditing();this.startEditing(_1,_2);return}}
if(this.$55q&&this.$55q[_1]==_1&&this.$55q[_2]==_2)
{return}
this.$55q=[_1,_2];var _6=this.suppressEditScrollIntoView||_5;this.logDebug("showing inline editor at: "+[_1,_2]+", will focus: "+!_5,"gridEdit");var _7=this.body&&(!this.body.shouldShowAllColumns()||!this.body.showAllRows);if(_7&&!_6){var _8=this.body.scrollRedrawDelay;this.body.scrollRedrawDelay=0;this.scrollCellIntoView(_1,_2,false);this.body.scrollRedrawDelay=_8}
if(this.rollOverCanvas&&this.rollOverCanvas.$475==_1){this.updateRollOverCanvas(this.rollOverCanvas.$475,this.rollOverCanvas.$47r,true)}
var _9;if(!this.$49j){var _10=this.makeEditForm(_1,_2);if(this.$52t()&&_10){this.logInfo("Edit Form rebuilt with alwaysShowEditors:true, requires full redraw","inactiveEditorHTML");_9=true}}
this.$471=_1;this.$504=_2;this.$527=_1;this.$55r(_1,_2,_9);this.$55q=null;if(this.body&&!_6){var _11;if(!_7)_11=true;else{var _12=this.body,_13=_12.getRowTop(_1),_14=_12.getRowSize(_1),_15=_12.getScrollTop(),_16=_12.getViewportHeight();_11=(_13<_15)||(_13+_14>_15+_16)}
if(_11){this.scrollCellIntoView(_1,_2,false)}}
if(!this.isDrawn()){return}
this.$55s();var _17=this.getEditorName(_1,_2),_18=this.$6l.getItem(_17);if(_18==null){this.logWarn("ListGrid showing inline editor. Unable to get a pointer to the edit "+"form item for field:"+_17)}else{if(_3)_18.$506=true;if(_4)_18.$505=true;if(!_5){if(isc.Browser.isMoz){var _19=this.body.getClipHandle(),_20=_19.scrollTop}
var _21=isc.EH.lastEvent;var _22=(isc.Browser.isIE&&_21.eventType==isc.EH.MOUSE_DOWN&&_21.target!=this.$6l);if(isc.Browser.isIE){var _23=isc.ListGrid.$55t;if(_23&&(_23.hasFocus||_23.itemHasFocus()))_22=true}
if(_22){this.$55u=isc.Timer.setTimeout(this.$6l.getID()+".focusInItem('"+_17+"');",0)}else{if(isc.Browser.isMoz&&this.body.overflow==isc.Canvas.VISIBLE){this.adjustOverflow()}
this.$6l.focusInItem(_17)}}}}
,isc.A.$55r=function isc_ListGrid__showEditForm(_1,_2,_3){var _4=this.$49j;this.$49j=true;if(!this.isDrawn()||!this.body)return;var _5=this.isEmpty()||(!_4&&(_1>=this.data.getLength())&&(this.showNewRecordRow||(this.body.getTableElement(_1)==null)));var _6=this.$52t();var _7=this.getCellRecord(_1,_2);if(_7&&_7.$4p!=null&&_7.$4p.length>0){_3=true}
if(_3||_5||this.body.isDirty()||(this.frozenBody&&this.frozenBody.isDirty())){var _8=this.bodyLayout?this.bodyLayout:this.body;_8.redraw("Showing editor");return}
if(this.editByCell){var _9=(this.frozenFields!=null)||(this.baseStyle==null);if(_9)this.refreshRow(_1);else this.refreshCell(_1,_2)}else{var _10=this.getDrawnFields();for(var i=0;i<_10.length;i++){if(this.isCheckboxField(_10[i]))continue;var _12=_10[i],_13=_10[i][this.fieldIdProperty],_14=this.$6l.getItem(_13),_2=_14.colNum,_15=!!(_4&&_14.isDrawn()),_16=this.canEditCell(_1,_2);if(_15!=_16)this.refreshCell(_1,_2);else if(this.selectOnEdit||this.lastOverRow){var _8=(_12.frozen&&!this.$52x)?this.frozenBody:this.body;_8.$48f(this.getCellRecord(_1,_2),_1,_2)}}}}
,isc.A.$55s=function isc_ListGrid__showEditClickMask(){if(!this.modalEditing){if(this.canHover)this.stopHover();return}
if(!this.$6l.clickMaskUp()){if(!this.$55v)
this.$55v=new Function(this.getID()+".$55w()");this.$6l.showClickMask(this.$55v,(this.stopOnErrors?isc.EH.SOFT_CANCEL:isc.EH.SOFT),this.$6l)}}
,isc.A.stopHover=function isc_ListGrid_stopHover(){if(this.$49j&&isc.EH.getTarget()==this.getEditForm())return;return this.Super("stopHover",arguments)}
,isc.A.shouldWaitForSave=function isc_ListGrid_shouldWaitForSave(){if(this.stopOnErrors&&!this.waitForSave){var _1="Note: ListGrid initialized with 'waitForSave' false, and 'stopOnErrors' true."+" In this case user input will be be blocked during save, to allow server side "+" errors to be determined before the editor is hidden. Setting 'waitForSave' to true.";if(this.waitForSave==false)this.logWarn(_1,"gridEdit");else this.logInfo(_1,"gridEdit");this.waitForSave=true}
return!!(this.waitForSave||this.stopOnErrors)}
,isc.A.hideInlineEditor=function isc_ListGrid_hideInlineEditor(_1,_2){_1=_1&&(this.hasFocus||(this.body&&this.body.hasFocus)||(this.getEditForm()&&this.getEditForm().hasFocus)||(isc.ListGrid.$55t&&isc.ListGrid.$55t.hasFocus));if(!this.$49j)return false;this.$49j=null;this.$49u=null;var _3=this.$471,_4=this.$504;this.$471=this.$504=null;if(!_2)this.$6l.hideClickMask();if(this.getEditValues(_3,_4)!=null&&(!this.$525||!this.$525[this.getEditValuesID(_3,_4)])&&!this.recordHasChanges(_3,_4,false)&&!this.recordMarkedAsRemoved(_3))
{this.logInfo("hideInlineEditor for row with no edits - dropping edit values","gridEdit");this.$55x(_3,_4)}
if(!this.body)return true;var _5=this.$6l;if(_5.hasFocus){_5.blur()}
if(isc.Browser.isIE){var _6=_5.getFocusSubItem(),_7=isc.EH.$m9;if(_5.hasFocus||(_7&&((_7==_5)||(isc.EH.$m9.form==_5))))
{_6.elementBlur();this.$55d(_6,_4)}}
var _8=this.getCellRecord(_3,_4),_9=false;if(_8&&_8.$4p!=null&&_8.$4p.length>0){_9=true}
if(!this.body.isDirty()&&(!this.bodyLayout||!this.bodyLayout.isDirty())&&!this.isDirty())
{if(_9||_3>=this.getTotalRows()){var _10=this.bodyLayout||this.body;_10.markForRedraw("Editor Hidden")}else{if(this.editByCell){var _11=(this.frozenFields!=null)||(this.baseStyle==null);if(_11)this.refreshRow(_3);else this.refreshCell(_3,_4)}
else this.refreshRow(_3);this.refreshGroupSummary(_3)}}
if(_1){this.$490=true;this.body.focus()}
return true}
,isc.A.$55d=function isc_ListGrid__parkFocus(_1,_2){if(isc.isA.TextItem(_1)||isc.isA.TextAreaItem(_1)||isc.isA.PopUpTextAreaItem(_1))
{var _3=isc.ListGrid.$55t;if(!_3){_3=isc.ListGrid.$55t=isc.DynamicForm.create({pointersToThis:[{object:isc.ListGrid,property:"$55t"}],getFocusParkItem:function(){return this.getItem(0)},autoDraw:false,_redrawWithParent:false,ID:"$55y",_generated:true,selectOnFocus:true,tabIndex:-1,items:[{name:"focusPark",type:"text",handleKeyPress:function(){return false}}],width:1,height:1,overflow:isc.Canvas.HIDDEN,itemHasFocus:function(){var _4=this.getFocusParkItem();if(_4.hasFocus)return true;if(isc.Browser.isIE&&this.isDrawn()&&this.isVisible()&&(this.getActiveElement()==this.getFocusParkItem().getFocusElement()))
return true;return false},redraw:function(){var _5=false;if(this.itemHasFocus())_5=true;this.Super("redraw",arguments);if(_5)this.focusInItem(this.getFocusParkItem())}})}
if(_3.parentElement!=this){if(_3.isVisible())_3.hide();this.addChild(_3)}
_3.moveTo(this.getColumnLeft(_2)-this.body.getScrollLeft(),Math.min(((this.showHeader?this.headerHeight:0)+this.getRowTop(this.getEditRow())
-this.body.getScrollTop()),this.getScrollHeight()-1));_3.moveBelow(this.body);_3.show();if(this.$6l.clickMaskUp())_3.unmask(this.$6l.getID());_3.getFocusParkItem().focusInItem();_1.form.hasFocus=false}}
,isc.A.makeEditForm=function isc_ListGrid_makeEditForm(_1,_2){var _3=this.getCellRecord(_1,_2),_4=this.getEditDisplayValues(_1,_2),_5;if(this.updateEditorItemsInPlace&&this.$6l!=null){_5=true;var _6=this.editByCell?[this.getField(_2)]:this.getDrawnFields();if(_6){if(_6.length!=this.$6l.getItems().length){_5=false}else{for(var i=0;i<_6.length;i++){var _8=_6[i],_9=this.getEditorName(_1,_8),_10=this.$6l.getItem(_9);if(_10==null||(this.getEditorType(_8,_4)!=this.$6l.getEditorType(_10)))
{_5=false;break}}}}
if(_5&&_6){var _11=this.getDrawnFieldWidths(_3,_6);for(var i=0;i<_6.length;i++){var _8=_6[i],_12=this.getColNum(_8),_9=this.getEditorName(_1,_8),_10=this.$6l.getItem(_9),_3=this.getRecord(_1),_13=this.getEditedRecord(_1),_14=this.getEditItem(_8,_3,_13,_1,_12,_11[i],true);_10.setProperties(_14);_10.$172=null;if(_8.frozen){_10.containerWidget=this.frozenBody}else{_10.containerWidget=this.body}}}else{var _15=this.getEditRowItems(_3,_1,_2,this.editByCell);this.$6l.setItems(_15)}
this.$6l.setValues(_4)}else{var _15=this.getEditRowItems(_3,_1,_2,this.editByCell);var _16=isc.addProperties({},this.editFormDefaults,{grid:this,locatorParent:this,showErrorIcons:this.showErrorIcons,tabIndex:this.getTabIndex(),dataSource:this.dataSource,autoComplete:this.autoComplete,uniqueMatch:this.uniqueMatch,autoFocus:false,items:_15,values:_4},this.editFormProperties);if(this.dateFormatter!=null&&_16.dateFormatter==null){_16.dateFormatter=this.dateFormatter}
if(this.datetimeFormatter!=null&&_16.datetimeFormatter==null){_16.datetimeFormatter=this.datetimeFormatter}
if(this.timeFormatter!=null&&_16.timeFormatter==null){_16.timeFormatter=this.timeFormatter}
this.$6l=isc.DynamicForm.create(_16)}
if(this.logIsDebugEnabled()){this.logDebug("editRowForm created with values: "+this.echo(this.$6l.getValues()),"gridEdit")}
return!_5}
,isc.A.getEditForm=function isc_ListGrid_getEditForm(){return this.$49j?this.$6l:null}
,isc.A.getEditFormItem=function isc_ListGrid_getEditFormItem(_1){var _2=this.getEditForm();if(!_2)return null;var _3=this.getEditorName(this.getEditRow(),_1),_4=_2.getItem(_3);if(!isc.isA.Number(_1)||(_4&&_4.colNum==_1))return _4}
,isc.A.getEditFormValue=function isc_ListGrid_getEditFormValue(_1){var _2=this.getEditFormItem(_1);return(_2?_2.getValue():null)}
,isc.A.$55z=function isc_ListGrid__updateEditItemValues(){if(!this.$6l)return;var _1=this.getEditRow(),_2=this.getEditCol(),_3=this.getEditDisplayValues(_1,_2);this.$6l.setValues(_3)}
,isc.A.getEditDisplayValues=function isc_ListGrid_getEditDisplayValues(_1,_2){var _3=this.getEditValues(_1,_2),_4=this.getCellRecord(_1,_2),_5={};for(var _6 in _4){_5[_6]=_4[_6]}
for(var _6 in _3){_5[_6]=_3[_6]}
return _5}
,isc.A.getEditFormItemFieldWidths=function isc_ListGrid_getEditFormItemFieldWidths(_1){var _2=[];for(var i=0;i<this.fields.length;i++){var _4=this.getLocalFieldNum(i),_5=this.fields[i],_6=(!this.$52x&&_5.frozen)?this.frozenBody:this.body;_2[i]=_6.getInnerColumnWidth(_4)}
return _2}
,isc.A.getEditorValueMap=function isc_ListGrid_getEditorValueMap(_1,_2){if(_1.getEditorValueMap!=null){isc.Func.replaceWithMethod(_1,"getEditorValueMap","values,field,grid");return _1.getEditorValueMap(_2,_1,this)}
if(_1.editorValueMap!=null){return _1.editorValueMap}
if(_1.editorProperties&&_1.editorProperties.valueMap){return _1.editorProperties.valueMap}
return _1.valueMap}
,isc.A.getEditorValueIcons=function isc_ListGrid_getEditorValueIcons(_1,_2){return _1.editorValueIcons||_1.valueIcons}
,isc.A.getEditorValueIconWidth=function isc_ListGrid_getEditorValueIconWidth(_1){if(_1.editorValueIconWidth!=null)return _1.editorValueIconWidth;return(_1.valueIconWidth!=null?_1.valueIconWidth:_1.valueIconSize)}
,isc.A.getEditorValueIconHeight=function isc_ListGrid_getEditorValueIconHeight(_1){if(_1.editorValueIconHeight!=null)return _1.editorValueIconHeight;return _1.valueIconHeight!=null?_1.valueIconHeight:_1.valueIconSize}
,isc.A.setEditorValueMap=function isc_ListGrid_setEditorValueMap(_1,_2){var _3=this.getColNum(_1),_4=this.getField(_1),_5=_4[this.fieldIdProperty];_4.editorValueMap=_2;if(this.$49j){var _6=this.getEditRow(),_7=this.getEditedRecord(_6,_3);this.$6l.setValueMap(_5,this.getEditorValueMap(_4,_7))}}
,isc.A.getEditorType=function isc_ListGrid_getEditorType(_1,_2){var _3=isc.addProperties({},_1,_1.editorProperties);return isc.DynamicForm.getEditorType(_3,this)}
,isc.A.getEditorProperties=function isc_ListGrid_getEditorProperties(_1,_2,_3){return isc.addProperties({},this.editorProperties,_1.editorProperties)}
,isc.A.getEditRowItems=function isc_ListGrid_getEditRowItems(_1,_2,_3,_4){var _5=this.body;if(_5==null)return[];var _6,_7,_8=[],_9=[];var _10=this.getEditedRecord(_2,_3);var _11;if(_4)_11=[this.getField(_3)]
else _11=this.getDrawnFields();_8=this.getDrawnFieldWidths(_1,_11);if(_11!=null){for(var i=0;i<_11.length;i++){var _13=this.getColNum(_11[i]);var _14=this.getEditItem(_11[i],_1,_10,_2,_13,_8[i])
if(_14==null)continue;_9[_9.length]=_14}}
return _9}
,isc.A.getDrawnFields=function isc_ListGrid_getDrawnFields(){if(!this.body)return null;var _1=[],_2=this.body;var _3=this.frozenFields,_4=this.freezeLeft();if(_3&&_4){_1.addList(_3)}
var _5,_6;if((_2.$6j==null)||!_2.isDrawn()||_2.isDirty()||_2.$49m){var _7=_2.getDrawArea();_5=_7[2];_6=_7[3]}else{_5=_2.$6j;_6=_2.$46a}
if(_3&&_4){_5+=_3.length;_6+=_3.length}
for(var i=_5;i<=_6;i++){_1.add(this.fields[i])}
if(_3&&!_4){_1.addList(_3)}
return _1}
,isc.A.getDrawnFieldWidths=function isc_ListGrid_getDrawnFieldWidths(_1,_2){if(!_2)_2=this.getDrawnFields();if(!_2)return null;var _3=this.getEditFormItemFieldWidths(_1),_4=[];for(var i=0;i<_2.length;i++){_4[i]=_3[_2[i].masterIndex]}
return _4}
,isc.A.$550=function isc_ListGrid__popUpTextAreaItemKeyPress(_1,_2,_3){return this.grid.editorKeyPress(this,_2,_3)}
,isc.A.$551=function isc_ListGrid__popUpTextAreaItemKeyDown(_1,_2,_3){return this.grid.editorKeyDown(this,_2,_3)}
,isc.A.$552=function isc_ListGrid__popUpTextAreaItemFocus(){var _1=this.form,_2=this.grid,_3,_4,_5,_6;if(_2.$49j){_3=_2.$471;if(!_2.editByCell){_3=_2.$471;_5=this.getFieldName(),_4=_2.fields.findIndex(_2.fieldIdProperty,_5);_6=(_2.$504!=_4);if(_6){_2.setNewEditCell(_3,_4);_2.cellEditEnd(isc.ListGrid.EDIT_FIELD_CHANGE)}}}}
,isc.A.$553=function isc_ListGrid__getPopUpTextAreaTop(){var _1=this.grid,_2=this.getFieldName(),_3=_1.getEditRow(),_4=_1.getCellStyle(_1.getRecord(_3),_3,_1.getColNum(_2)),_5=_1.getRowPageTop(_3)+isc.Element.$qy(_4)+isc.Element.$q5(_4);return _5}
,isc.A.$554=function isc_ListGrid__checkboxClick(_1,_2,_3,_4){if(!this.hasFocus){var _5=this.grid;_5.setNewEditCell(this.rowNum,this.colNum);_5.cellEditEnd(isc.ListGrid.EDIT_FIELD_CHANGE);this.$506=true}
return this.invokeSuper("CheckboxItem","handleClick",_1,_2,_3,_4)}
,isc.A.handleEditorChanged=function isc_ListGrid_handleEditorChanged(_1){var _2=_1.getFieldName(),_3=this.getField(_2),_4;if(_3&&_3.validateOnChange!=null)_4=_3.validateOnChange;else _4=this.validateOnChange;var _5=this.getEditRow();if(_5==null||_5!=_1.rowNum)return;if(_4){this.validateCell(_5,_2,false,true)}else if(!_1.hasFocus&&!this.editByCell){if(_1.isDrawn()&&_1.isVisible()){this.storeUpdatedEditorValue(false,_1.colNum);if(this.saveByCell){var _6=isc.ListGrid.EDIT_FIELD_CHANGE;this.saveEdits(_6,null,_5,_1.colNum)}else if(this.validateByCell)this.validateCell(_5,_2)}}}
,isc.A.$555=function isc_ListGrid__editorHandleChangedOverride(_1,_2,_3,_4){this.invokeSuper(this.getClassName(),"handleChanged",_1,_2,_3,_4);if(!this.destroyed)this.grid.handleEditorChanged(this)}
,isc.A.$556=function isc_ListGrid__editorGetAutoComplete(){var _1=this.grid;if(!_1)return null;var _2=_1.getField(this.getFieldName());if(_2.autoComplete!=null)return _2.autoComplete;if(_1.autoComplete!=null)return _1.autoComplete;return this.Super("$28z",arguments)}
,isc.A.getEditItem=function isc_ListGrid_getEditItem(_1,_2,_3,_4,_5,_6,_7){var _8={};_8.width=_6;_8.record=_2;_8.rowNum=_4;_8.colNum=_5;var _9=this.getEditorName(_4,_1);_8[this.fieldIdProperty]=_9;if(_1.title!=null)_8.title=_1.title;_8.valueMap=this.getEditorValueMap(_1,_3);if(_1.multiple!=null)_8.multiple=_1.multiple;if(_1.valueField!=null)_8.valueField=_1.valueField
if(_1.displayField!=null)_8.displayField=_1.displayField
if(_1.optionDataSource)_8.optionDataSource=_1.optionDataSource
if(_1.optionFilterContext)_8.optionFilterContext=_1.optionFilterContext
if(_1.optionCriteria)_8.optionCriteria=_1.optionCriteria
if(_1.optionOperationId!=null)_8.optionOperationId=_1.optionOperationId;_8.valueIcons=this.getEditorValueIcons(_1,_3);var _10=this.getEditorValueIconWidth(_1),_11=this.getEditorValueIconHeight(_1);if(_10)_8.valueIconWidth=_10;if(_11)_8.valueIconHeight=_11;_8.imageURLPrefix=(_1.editorImageURLPrefix||_1.imageURLPrefix);_8.imageURLSuffix=(_1.editorimageURLSuffix||_1.imageURLSuffix);_8.baseURL=_1.baseURL;_8.imgDir=_1.imgDir;var _12;if(_1.icons!==_12)_8.icons=_1.icons;if(_1.showPickerIcon!==_12)_8.showPickerIcon=_1.showPickerIcon;if(_1.pickerIconSrc!==_12)_8.pickerIconSrc=_1.pickerIconSrc;if(_1.pickerIconWidth!==_12)_8.pickerIconWidth=_1.pickerIconWidth;if(_1.pickerIconHeight!==_12)_8.pickerIconHeight=_1.pickerIconHeight;if(_1.defaultIconSrc!==_12)_8.defaultIconSrc=_1.defaultIconSrc;var _13=(_1.editorIconHeight||_1.iconHeight);if(_13!==_12)_8.iconHeight=_13
var _14=(_1.editorIconWidth||_1.iconWidth);if(_14!==_12)_8.iconWidth=_14;if(_1.iconPrompt!==_12)_8.iconPrompt=_1.iconPrompt;if(_1.iconHSpace!==_12)_8.iconHSpace=_1.iconHSpace;if(_1.iconVAlign!==_12)_8.iconVAlign=_1.iconVAlign;if(this.showValueIconOnly(_1)){if(_1.editorProperties==null)_1.editorProperties={};_1.editorProperties.showValueIconOnly=true}
if(_1.pickListWidth!=null)_8.pickListWidth=_1.pickListWidth;if(_1.pickListFields!=null)_8.pickListFields=_1.pickListFields;_8.textAlign=(_1.cellAlign||_1.align);if(_1.editorProperties!=null){for(var i=0;i<this.$518.length;i++){var _16=this.$518[i],_17=_1.editorProperties[_16];if(_17!=null&&!isc.isA.Function(_17)){var _18=isc.FormItem._stringMethodRegistry;_1.editorProperties[_16]=_17=isc.Func.expressionToFunction(_18[_16],_17)}
if(_17!=null)_1.editorProperties[_16]=_17}}
for(var i=0;i<this.$517.length;i++){var _16=this.$517[i];if(_1.editorProperties&&_1.editorProperties[_16]!=null){continue}
var _17=_1[_16];if(_17!=null){if(_1.editorProperties==null)_1.editorProperties={};if(!isc.isA.Function(_17)){var _18=isc.FormItem._stringMethodRegistry;_17=isc.Func.expressionToFunction(_18[_16],_17)}
_1.editorProperties[_16]=_17}}
_8.valueIconLeftPadding=this.getValueIconLeftPadding(_1);_8.valueIconRightPadding=this.getValueIconRightPadding(_1);if(!_7){_8.$28z=this.$556;_8.autoCompleteCandidates=_1.autoCompleteCandidates;_8.uniqueMatch=_1.uniqueMatch;_8.containerWidget=(!this.$52x&&_1.frozen)?this.frozenBody:this.body;_8.grid=this;_8.handleChanged=this.$555;_8.keyDown=function(_8,_24,_25){return this.form.grid.editorKeyDown(_8,_25)}
_8.inactiveEditorMouseDown=function(_24,_25){if(_24&&(_24.grid==this.form.grid.getID())&&_24.rowNum!=null&&_24.colNum!=null)
{this.form.grid.startEditing(_24.rowNum,_24.colNum)}}
_8.type=_1.type;_8.editorType=this.getEditorType(_1,_2);var _19=_8.editorType;if(_1.dateFormatter!=null)_8.dateFormatter=_1.dateFormatter;if(_1.timeFormatter!=null)_8.timeFormatter=_1.timeFormatter;if(_1.displayFormat!=null)_8.displayFormat=_1.displayFormat;var _20=this.$50t(_1);if(_20)_8.inputFormat=_20;if(this.$3g[_19]==true||this.$513[_19]==true||(_1.type==this.$ij&&_19==null)){_8.editorType=(this.$513[_19]==true?this.$ih:this.$ij);_8.useTextField=true;_8.cellPadding=0;_8.itemCellStyle=null;_8.pickerIconHSpace=0}
if(this.$514[_19]==true){_8.getTextBoxStyle=function(){var _21=this.grid,_2=_21.getCellRecord(this.rowNum,this.colNum);this.textBoxStyle=_21.getCellStyle(_2,this.rowNum,this.colNum);return this.Super("getTextBoxStyle",arguments)}
_8.gridCellStyleChanged=function(_2,_4,_5,_24){var _22=this.$6f();if(_22){_22.className=this.textBoxStyle=_24}}
_8.textBoxCellCSS=isc.Canvas.$rr
_8.textAreaKeyDown=this.$551;_8.textAreaKeyPress=this.$550;_8.getTextAreaTop=this.$553;if(_8.popUpOnEnter==null)_8.popUpOnEnter=true;_8.textAreaFocus=this.$552}
if(this.$515[_19]==true||(_1.type==this.$if&&_19==null)){if(_8.showLabel==null)_8.showLabel=false;if(_8.handleClick==null){_8.handleClick=this.$554}}
if(_19=="RichTextItem"||_19=="richText"){if(_8.overflow==null)_8.overflow="auto"}
_8.elementFocus=this.$503;_8.canTabToIcons=false;_8.focusInItem=this.$557;_8.$27j=this.$558}
var _23=this.getEditorProperties(_1,_2,_4);isc.addProperties(_8,_23);return _8}
,isc.A.$557=function isc_ListGrid__editFormItem_focusInItem(){this.Super("focusInItem",arguments);this.grid.$55o(this)}
,isc.A.$558=function isc_ListGrid__editFormItem_refocusAfterRedraw(){this.$55p=true;this.Super("$27j",arguments);this.$55p=false}
,isc.A.getEditorName=function isc_ListGrid_getEditorName(_1,_2,_3){_2=this.getField(_2);if(!_2)return null;if(_3&&_2.dataPath)return _2.dataPath;return _2[this.fieldIdProperty]}
,isc.A.refreshCell=function isc_ListGrid_refreshCell(_1,_2,_3,_4){if(_1==null){this.logInfo("ListGrid.refreshCell(): first parameter rowNum not present, returning");return}
if(!this.isDrawn()||!this.body)return;var _5=this.getFieldBody(_2);if(_5.isDirty()){this.logDebug("refresh cell redrawing body","gridEdit");_5.redraw("refresh cell");return false}
if(this.$52t(this.getField(_2),true)){this.$49n(_1,_2)}
var _6=this.getLocalFieldNum(_2);_5.refreshCellStyle(_1,_6);if(!_5.shouldRefreshCellHTML()){this.refreshCellValue(_1,_2,_3,_4)}}
,isc.A.refreshCellValue=function isc_ListGrid_refreshCellValue(_1,_2,_3,_4){if(!this.isDrawn()||!this.body)return;var _5=this.getFieldBody(_2),_6=this.getLocalFieldNum(_2);if(!_5.$48i(_1,_6)){this.delayCall("refreshCellValue",[_1,_2,_3,_4]);return}
var _7=this.getEditorName(_1,_2),_8=this.getFieldName(_2),_9=this.$6l,_10,_11=false,_12,_13;if(_9){var _10=_9.getItem(_7),_14=(this.$49j&&_1==this.getEditRow());if(_10&&_10.colNum!=_2)_10=null;if(_14){_12=(_10&&_10.isDrawn());_13=this.canEditCell(_1,_2);if(_10!=null&&_9.hasFocus){var _15=_9.getFocusSubItem();_11=(_15==_10||(_10.items&&_10.items.contains(_15)))}}else if(_10&&_10.rowNum==_1){_13=false;_12=_10.isDrawn()}}
if(!_4&&(_11&&_12&&_13))
{return}
if(_12){this.getUpdatedEditorValue();if(_10!=null){if(_11){if(_13){_9.$20u()}
else _10.blurItem()}
if(_13)_10.redrawing()}}
_5.refreshCellValue(_1,_6);if(_10&&(_12||_13)){this.$49t(_10,null,_5);if(_13){_10.setValue(this.getEditDisplayValue(_1,_2));if(_11){_9.$21l(_10)}}}}
,isc.A.refreshRow=function isc_ListGrid_refreshRow(_1){if(!this.body||!this.isDrawn())return;var _2=this.frozenFields&&this.frozenFields.length>0;if(_2&&!this.frozenBody)_2=false;var _3=false;if(this.body.isDirty())_3=true;if(_2&&!_3){if(this.frozenBody.isDirty()||this.bodyLayout.isDirty())_3=true}
if(_3){var _4=_2?this.bodyLayout:this.body;return _4.redraw("refresh row")}
if(this.$49j&&this.$6l!=null){this.logInfo("refresh row: "+_1,"gridEdit")}
if(_2){for(var i=0;i<this.frozenFields.length;i++){this.refreshCell(_1,this.getFieldNum(this.frozenFields[i]),true)}}
var _6=this.body.$6j,_7=this.body.$46a;for(var i=_6;i<=_7;i++){var _8=this.getFieldNumFromLocal(i,this.body);this.refreshCell(_1,_8,true)}}
,isc.A.startEditingNew=function isc_ListGrid_startEditingNew(_1,_2){if(!this.canEdit&&!(this.completeFields||this.fields).getProperty("canEdit").or()){this.canEdit=true}
if(isc.isAn.Array(this.data)&&this.data.length==0&&this.dataSource&&!this.shouldSaveLocally())
{this.fetchData(null,null,{$31:true});this.data.setFullLength(0)}
var _3=this.body?this.body.getTotalRows():this.getTotalRows();if(this.showNewRecordRow)_3-=1;var _4=this.findNextEditCell(_3,0,1,true,true,true);if(_4==null){this.logInfo("startEditingNew() failed to find any editable fields in this grid.","gridEdit");return}
if(_1!=null)this.setEditValues(_4,isc.addProperties({},_1),true);this.startEditing(_4[0],_4[1],_2)}
,isc.A.updateEditRow=function isc_ListGrid_updateEditRow(_1){if(this.$471!=_1||!this.$6l){return}
this.$6l.setItemValues();delete this.$6l.$20f;this.$6l.$559=false}
,isc.A.shouldFixRowHeight=function isc_ListGrid_shouldFixRowHeight(_1,_2){if(this.canEdit!=false&&this.$49j&&_2==this.$471){return false}
return this.fixedRecordHeights}
,isc.A.$55m=function isc_ListGrid__getNextEditFlowID(){if(this.$56a==null)this.$56a=isc.timeStamp();return this.$56a++}
,isc.A.$55w=function isc_ListGrid__handleClickOutsideEditor(){var _1=this.getEditRow();this.cellEditEnd(isc.ListGrid.CLICK_OUTSIDE)}
,isc.A.getEditDisplayValue=function isc_ListGrid_getEditDisplayValue(_1,_2,_3){var _4;if(_3===_4)_3=this.getCellRecord(_1,_2);var _5=this.$535(_1,_2);if(_5===_4&&_3!=null){_5=this.getRawCellValue(_3,_1,_2)}
_5=this.$56b(_5,_3,_1,_2);return _5}
,isc.A.$56b=function isc_ListGrid__formatEditorValue(_1,_2,_3,_4){if(_2==null)_2=this.$507(_3,_4);var _5=this.fields[_4];if(_5&&_5.formatEditorValue!=null){isc.Func.replaceWithMethod(_5,"formatEditorValue","value,record,rowNum,colNum,grid");_1=_5.formatEditorValue(_1,_2,_3,_4,this)}else if(this.formatEditorValue!=null){_1=this.formatEditorValue(_1,_2,_3,_4)}
return _1}
,isc.A.getEditValuesID=function isc_ListGrid_getEditValuesID(_1){if(_1==null||this.$53n==null)return null;if(isc.isA.String(_1)&&this.$53n[_1]!=null)return _1;if(isc.isA.Number(_1)){return this.$56c[_1]}
for(var i in this.$53n){var _3=this.$53n[i];if(_3==_1)return i;var _4=_3.$56d;if(_4&&this.comparePrimaryKeys(_4,_1)){return i}}
return null}
,isc.A.getEditSession=function isc_ListGrid_getEditSession(_1,_2){if(this.$53n==null)return null;if(!isc.isA.String(_1))_1=this.getEditValuesID(_1,_2);return this.$53n[_1]}
,isc.A.getEditSessionRowNum=function isc_ListGrid_getEditSessionRowNum(_1){_1=this.getEditSession(_1);return(_1!=null?_1.$46t:null)}
,isc.A.getEditSessionColNum=function isc_ListGrid_getEditSessionColNum(_1){_1=this.getEditSession(_1);return(_1!=null?_1.$56e:null)}
,isc.A.getAllEditRows=function isc_ListGrid_getAllEditRows(_1){return this.getAllEditCells(_1,true)}
,isc.A.getAllEditCells=function isc_ListGrid_getAllEditCells(_1,_2){var _3=this.$53n,_4=[];if(!_3)return _4;if(_1)return isc.getKeys(this.$53n);for(var i in _3){var _6=_3[i].$46t;_6=parseInt(_6);if(_6==null||_6<0||isNaN(_6))continue;if(_2)_4[_4.length]=_6
else{var _7=_3[i].$56e;if(_7==null){_7=this.$49j&&(_6==this.getEditRow())?this.getEditCol():this.getRowEditColNum(_6)}
if(_7!=null&&!isc.isA.Number(_7))_7=parseInt(_7);_4[_4.length]=[_6,_7]}}
return _4}
,isc.A.getEditValues=function isc_ListGrid_getEditValues(_1,_2){if(_1==null){return this.logWarn("getEditValues() called with no valuesID. "+(this.logIsDebugEnabled("gridEdit")?this.getStackTrace():""))}
if(_2==null&&isc.isA.Array(_1)){_2=_1[1];_1=_1[0]}
var _3=(isc.isA.Number(_1)?_1:this.getEditSessionRowNum(_1));if(this.$49j&&(this.getEditRow()==_3)){this.storeUpdatedEditorValue()}
var _4=isc.addProperties({},this.$507(_1,_2));if(_4!=null){delete _4[this.recordRemovedProperty]}
return _4}
,isc.A.$507=function isc_ListGrid__getEditValues(_1,_2){var _3=this.getEditSession(_1,_2);return _3!=null?_3.$53o:null}
,isc.A.getEditedRecord=function isc_ListGrid_getEditedRecord(_1,_2,_3){if(_1==null)return this.logWarn("getEditedRecord() called with no valuesID");if(!isc.isA.Number(_1)){_1=this.getEditSessionRowNum(_1);_2=this.getEditSessionColNum(_1)}
var _4=this.getCellRecord(_1,_2),_5=_3?this.$507(_1,_2):this.getEditValues(_1,_2);if(_5!=null){delete _5[this.recordRemovedProperty]}
return isc.addProperties({},_4,_5)}
,isc.A.getEditedCell=function isc_ListGrid_getEditedCell(_1,_2){if(_1==null||_2==null)
return this.logWarn("getEditedCell() called with no record / field parameter");var _3=this.getEditValues(_1,_2),_4=isc.isA.Number(_1)?_1:this.getEditSessionRowNum(_1),_5=isc.isA.Number(_2)?_2:this.getFieldNum(_2),_1=this.getCellRecord(_4,_5);var _6=this.getEditorName(_4,this.getField(_5))
var _7;if(_3&&_3[_6]!==_7)return _3[_6];return _1?_1[_6]:null}
,isc.A.rememberSubmittedEditValues=function isc_ListGrid_rememberSubmittedEditValues(_1,_2){var _3=this.getEditSession(_1);if(_3!=null){_3.$56f=isc.addProperties({},_3.$53o)}}
,isc.A.getSubmittedEditValues=function isc_ListGrid_getSubmittedEditValues(_1,_2){var _3=this.getEditSession(_1,_2);return _3!=null?_3.$56f:null}
,isc.A.clearSubmittedEditValues=function isc_ListGrid_clearSubmittedEditValues(_1,_2){var _3=this.getEditSession(_1);if(_3==null)return;var _4=_3.$56f;if(!_4)return;for(var _5 in _2){if(_4[_5]==_2[_5])delete _4[_5]}
if(isc.isA.emptyObject(_4))_3.$56f=null}
,isc.A.createEditValues=function isc_ListGrid_createEditValues(_1){var _2=this.body.getTotalRows();this.setEditValues(_2,_1);return this.getEditValuesID(_2)}
,isc.A.initializeEditValues=function isc_ListGrid_initializeEditValues(_1,_2,_3){if(this.$507(_1,_2)==null){this.setEditValues([_1,_2],{},_3)}}
,isc.A.setEditValues=function isc_ListGrid_setEditValues(_1,_2,_3){var _4;if(isc.isAn.Array(_1)){_4=_1[1];_1=_1[0]}
if(!isc.isA.Number(_1)){this.logWarn("setEditValues() called with bad rowNum: "+this.echo(_1));return}
if(_2==null)_2={};var _5,_6,_7=true;if(!_3){var _8=this.getCellRecord(_1,_4);if(_8==null)_8={};else _7=false;_5=this.getEditValues(_1,_4);if(_5!=null)_7=false;_6=isc.addProperties({},_5);for(var i in _6){_6[i]=_8[i]}
isc.addProperties(_6,_2)}
if(this.logIsInfoEnabled("gridEdit")){_5=_5||this.getEditValues(_1,_4);if(!_5){this.logInfo("establishing new edit session at row: "+_1+(_4!=null?", col:"+_4:"")+(this.logIsDebugEnabled("gridEdit")?" with values: "+this.echo(_2):""),"gridEdit")}}
var _10=this.recordMarkedAsRemoved(_1);if(_10)_2[this.recordRemovedProperty]=true;this.$56g(_1,_4,_2);var _11=!isc.isAn.emptyObject(_6);if(_3||!this.isDrawn()||!this.body){if(_11){if(this.summaryRow&&this.showGridSummary)this.summaryRow.$52u()}
return}
if(_11&&_1<this.data.getLength()){this.calculateRecordSummaries([this.data.get(_1)],true)}
var _12=(_7||this.body.isDirty());if(_12){var _13=this.isEditingRecord(_1,_4)&&this.$6l!=null
if(_13)this.$55z();this.body.markForRedraw();if(_11&&this.summaryRow&&this.showGridSummary){this.summaryRow.$52u()}}else{this.$56h(_1,_4,_6)}}
,isc.A.$56h=function isc_ListGrid__displayNewEditValues(_1,_2,_3,_4){if(!_3||isc.isAn.emptyObject(_3)){return}
var _5=this.isEditingRecord(_1,_2)&&this.$6l!=null
var _6=false;for(var _7 in _3){var _8=this.getColNum(_7);var _9;if(_5){this.$6l.setValue(_7,_3[_7]);var _10=this.$6l.getItem(_7);_9=(_8>=0&&_10&&this.canEditCell(_1,_8))}
if(_8==-1)continue;_6=true;if(!_9){this.refreshCell(_1,_8)}else if(_4&&_4[_7]){this.showCellErrors(_1,_8)}}
if(_6&&this.summaryRow&&this.showGridSummary){this.summaryRow.$52u();this.refreshGroupSummary(_1)}}
,isc.A.$56g=function isc_ListGrid__storeEditValues(_1,_2,_3,_4){var _5=this.getCellRecord(_1,_2);var _6=this.getEditSession(_1,_2)||this.createEditSession(_1,_2,_5,_4);if(this.dataSource!=null){if(_5=="loading"){_6.$56i=true}else if(_5!=null){var _7=this.getDataSource(),_8=_7.getPrimaryKeyFieldNames();for(var i=0;i<_8.length;i++){_3[_8[i]]=_5[_8[i]]}}}
if(_5==null){_6.$56i=true;var _10=this.completeFields||this.fields||[],_11;for(var i=0;i<_10.length;i++){var _12=_10[i],_13=_12[this.fieldIdProperty];if(_3[_13]===_11){var _14=this.getDefaultEditValue(_13,_12);if(_14!=null){_3[_13]=_14}}}}
for(var i in _6.$53o){delete _6.$53o[i]}
for(var i in _3){_6.$53o[i]=_3[i]}
if(this.$56j==null||_1>=this.$56j){this.$56j=_1}}
,isc.A.getDefaultEditValue=function isc_ListGrid_getDefaultEditValue(_1,_2){var _3=_2.defaultValue;if(_3==null&&this.enumCriteriaAsInitialValues&&_2.type=="enum"&&this.$549!=null&&this.$549[_1]!=null)
{_3=this.$549[_1]}
return _3}
,isc.A.createEditSession=function isc_ListGrid_createEditSession(_1,_2,_3,_4){var _5={};if(_3!=null&&_3!=Array.LOADING)
_5.$56d=this.getPrimaryKeys(_3);_5.$46t=_1;if(this.useCellRecords){_5.$56e=_2;if(this.getCellFacetValues)_5.$56k=this.getCellFacetValues(_1,_2)}
_5.$53o={};if(this.$53n==null)this.$53n={};if(_4==null){if(this.$56l==null)this.$56l=0;_4="_"+this.$56l++}
this.$53n[_4]=_5;if(this.$56c==null)this.$56c={};this.$56c[_1]=_4;return _5}
,isc.A.setEditValue=function isc_ListGrid_setEditValue(_1,_2,_3,_4,_5){if(isc.isA.String(_1)){isc.logWarn("Warning: first parameter rowNum is a string, expecting a number");return}
var _6=isc.isA.String(_2)?_2:this.getEditorName(_1,_2,true);_6=this.$536(_6);if(isc.isA.String(_2))_2=this.getFieldNum(_2);var _7=this.$56m(_1,_2,_6,_3,_5);if(!_7)return;var _8=this.getField(_6);if(_8&&this.$4t(_8)){var _9=false,_10;var _11=this.getEditForm(),_12=_11?_11.getItem(_6):null;if(_12&&this.getEditRow()==_1&&this.fieldValuesAreEqual(_8,_11.getValue(_6),_3))
{_9=true;_10=_12.mapValueToDisplay(_3)}
if(!_9&&(_8.valueField==null||_8.valueField==_8.name)&&(!_8.optionDataSource||isc.DS.get(_8.optionDataSource)==this.getDataSource()))
{var _13=this.data;if(isc.ResultSet&&isc.isA.ResultSet(_13))_13=_13.localData;if(_13){var _14=_13.find(_6,_3);if(_14){_9=true;_10=_14[_8.displayField]}}}
if(_9){this.setEditValue(_1,_8.displayField,_10,_4,true)}else{if(this.warnOnUnmappedValueFieldChange){this.logWarn("Edit value updated for field:"+_6+". This field has 'displayField' attribute specified as '"+_8.displayField+((_8.optionDataSource==null||isc.DataSource.get(_8.optionDataSource)==this.getDataSource())?"', and no unique optionDataSource, ":"', ")+"so display value is derived from the "+"current record. In order to ensure the display value is updated to "+"reflect the new edit-value for this field, developers can explicitly update the "+"edit value for the display-field on this record. To avoid seeing this method "+"set listGrid.warnOnUnmappedValueFieldChange to false.")}}}
if(_4){if(this.summaryRow&&this.showGridSummary)this.summaryRow.$52u();return}
this.setRowEditFieldName(_1,_6);if(!isc.isA.Number(_1)){_2=this.getEditSessionColNum(_1);_1=this.getEditSessionRowNum(_1)}else if(!isc.isA.Number(_2)){_2=this.getFieldNum(_2)}
var _15={};_15[_6]=_3;this.$56h(_1,_2,_15)}
);isc.evalBoundary;isc.B.push(isc.A.$56m=function isc_ListGrid__storeEditValue(_1,_2,_3,_4,_5){var _6=true,_7=false,_8,_9,_10,_11;_8=this.getEditSession(_1,_2)
if(_8!=null){_9=_8.$53o;_10=isc.Canvas.$3e(_3,_9,this,true)}else{this.logInfo("creating new edit values for row: "+_1,"gridEdit");this.initializeEditValues(_1,_2,true);_8=this.getEditSession(_1,_2);_9=_8.$53o}
if(_10===_11){var _12=this.getCellRecord(_1,_2);_10=_12?isc.Canvas.$3e(_3,_12,this,true):null}else _7=true;var _13=this.getField(_3);if(this.fieldValuesAreEqual(_13,_10,_4))_6=false;if(_4===_11){isc.Canvas.$3c(_3,_9,this,true)}else if(_7||_6){isc.Canvas.$3d(_3,_4,_9,this,true)}
if(_6&&!_5){this.$56n(_1,_2,_4,_10)}
return _6}
,isc.A.setRowEditFieldName=function isc_ListGrid_setRowEditFieldName(_1,_2){var _3=this.getEditSession(_1);if(!_3){var _4=this.getColNum(_2);this.setEditValues([_1,_4],null,true);_3=this.getEditSession(_1)}
if(isc.isA.Number(_2))_2=this.getFieldName(_2);_3.$56o=_2}
,isc.A.getRowEditFieldName=function isc_ListGrid_getRowEditFieldName(_1){var _2=this.getEditSession(_1);return(_2?_2.$56o:null)}
,isc.A.getRowEditColNum=function isc_ListGrid_getRowEditColNum(_1){var _2=this.getRowEditFieldName(_1);return _2?this.getColNum(_2):null}
,isc.A.getEditValue=function isc_ListGrid_getEditValue(_1,_2){var _3=_2
if(isc.isA.String(_2))_2=this.getColNum(_2);if(this.$49j&&(this.getEditRow()==_1)&&(this.getEditCol()==_2))
{this.storeUpdatedEditorValue()}
return this.$535(_1,_3)}
,isc.A.$535=function isc_ListGrid__getEditValue(_1,_2){var _3=this.$507(_1,_2);if(_3==null)return;if(!isc.isA.String(_2))_2=this.getEditorName(_1,_2,true);return isc.Canvas.$3e(this.$536(_2),_3,this)}
,isc.A.clearEditValue=function isc_ListGrid_clearEditValue(_1,_2,_3,_4){var _5=(isc.isA.Number(_1)?_1:this.getEditSessionRowNum(_1));var _6=_2;if(isc.isA.Number(_6))_6=this.getEditorName(_5,_6);else _2=this.getColNum(_6);if(this.$49j){if(this.getEditRow()==_5&&this.getEditFieldName()==_6){this.storeUpdatedEditorValue(true)}}
var _7=this.getEditSession(_1,_2);if(_7==null)return;var _8=_7.$53o,_9=false,_10=_7.$56f,_11=_7.$56p,_12=_11&&_11[_6],_5=_7.$46t;if(_8!=null){var _13=this.getRecord(_5);_9=isc.propertyDefined(_8,_6)&&((_5==null||_13==null)||!this.fieldValuesAreEqual(this.getField(_6),_8[_6],_13[_6]));delete _8[_6];if(_10)delete _10[_6];if(_11!=null)delete _11[_6];if(!_4&&isc.isAn.emptyObject(_8)){this.logDebug("no edit values left for row, discarding editSession","gridEdit");return this.$55x(_1,_2,_3)}}
var _14=this.getField(_6);if(_14&&this.$4t(_14)&&(_14.displayField!=_6)){this.clearEditValue(_1,_14.displayField,_3,_4)}
if(_3||_5==null)return;if(_9){var _15;if(this.$49j&&this.getEditRow()==_5){var _16=_13?_13[_6]:null;this.$6l.setValue(_6,_16);_15=_2>=0&&this.canEditCell(_5,_2)&&this.$6l.getItem(_6)}
if(_2>=0&&!_15)this.refreshCell(_5,_2)}else if(_12&&_2>=0){if(_12)this.showCellErrors(_5,_2)}}
,isc.A.$55x=function isc_ListGrid__clearEditValues(_1,_2,_3){if(_1==null){return}
if(!isc.isA.String(_1))_1=this.getEditValuesID(_1,_2);var _4=this.getEditSession(_1);if(_4==null)return;var _5=_4.$46t;if(_5!=null)delete this.$56c[_5];if(_2==null)_2=_4.$56e;var _6=this.$53n[_1];delete this.$53n[_1];var _7=this.$56q();delete this.$56j;if(_5!=null&&_4.$56i){var _8=this.getEditRow();for(var i=_5+1;i<=_7;i++){var _10=this.getEditSession(i);var _11=_10.$46t;_10.$46t=i-1;this.$56c[i-1]=this.$56c[i];delete this.$56c[i]}
if(_8!=null&&_8>_5)this.$471-=1;if(!_3){if(this.$49j){if(_5==_8)this.hideInlineEditor();else{if(_5<_8)this.$56r(_8-1,null,"Earlier temp edit row removed")}}
if(this.body){var _12=this.frozenBody!=null?this.bodyLayout:this.body;_12.markForRedraw("clear edit values, remove row")}}}else if(_5!=null&&!_3){var _13={},_14=this.data.get(_5);for(var _15 in _6.$53o){_13[_15]=_14?_14[_15]:null}
this.$56h(_5,_2,_13,_6.$56p)}}
,isc.A.newRecordForEditValues=function isc_ListGrid_newRecordForEditValues(_1,_2){var _3=this.getEditSession(_1),_4=_3.$53o;_3.$56d=this.getPrimaryKeys(_2);var _5=this.getDataSource().getPrimaryKeyFieldNames();for(var i=0;i<_5.length;i++){var _7=_5[i];_4[_7]=_2[_7]}
delete _3.$56i}
,isc.A.$52s=function isc_ListGrid__remapEditRows(_1){delete this.$56j;delete this.$56s;if(this.$53n==null)return false;var _2=false;var _3=this.getEditRow(),_4=this.getEditCol(),_5=false;var _6=this.data.getLength(),_7={};var _8=isc.getKeys(this.$53n);for(var i=0;i<_8.length;i++){var _10=_8[i],_11=this.$53n[_10];var _12=_11.$46t;var _13=this.$56t(_11,_6);if(_11.$56d==null)_6++;if(_13==null)continue;var _14=_13[0],_15=_13[1];if(_14!=null&&_14>=0){if(_14!=_12)_2=true;if(_3!=null&&_3==_11.$46t&&(!this.useCellRecords||(_4==_11.$56e)))
{_5=true;if(!_1)this.$56r(_14,_15,"remapEditRows")}
_7[_14]=_10;_11.$46t=_14;if(this.useCellRecords)_11.$56e=_15}else{if(_12!=null&&_12>=0)_2=true;var _16=this.getOriginalData();var _17=!this.dataSource||this.shouldSaveLocally()||(isc.ResultSet&&isc.isA.ResultSet(_16)&&_16.allRowsCached());if(_17&&!isc.isA.Tree(this.data)&&(!this.isGrouped||!this.data.find(_11.$56d)))
{this.logWarn("Record:"+this.echo(_11.$56d)+", is no longer present in this List.<br>Clearing edit values for this record.");delete this.$53n[this.getEditValuesID(_10)]}else{this.logWarn("Record:"+this.echo(_11.$56d)+(_17?", hidden in grouped tree data. ":", lost from local cache in paged result set. ")+"Pending edits for this record will be maintained.");delete _11.$46t;delete _11.$56e;this.$56s=true}}}
this.$56c=_7;if(_3!=null&&!_5&&!_1){this.hideInlineEditor(true)}
return _2}
,isc.A.$51e=function isc_ListGrid__remapEmbeddedComponents(){if(!this.body||this.body.$4p==null)return;var _1=this.body.$4p,_2=[];for(var i=0;i<_1.length;i++){var _4=_1[i],_5=_4.embeddedRecord,_6=this.getPrimaryKeys(_5),_7=this.findRowNum(_6),_8;if(_7>=0){_8=this.data.get(_7);if(!_8.$4p)_8.$4p=[];if(!_8.$4p.contains(_4)){_4.$475=_7;_4.embeddedRecord=_8;_8.$4p.add(_4);if(_4.isExpansionComponent){_8.expanded=true
_8.hasExpansionComponent=true;if(!this.canExpandMultipleRecords)this.$51c=_8}}}else{if(this.$56u==false||(this.showRecordComponents&&this.recordComponentPoolingMode=="data")){_2.add(_4)}}}
if(_2.length>0){for(var i=0;i<_2.length;i++){var _9=_2[i];if(this.canExpandRecords){this.$50q--;if(this.$51c&&this.$51c==_9.embeddedRecord)
delete this.$51c}
this.body.$4p.remove(_9);if(this.shouldDestroyOnUnembed(_9,this.$49s)){_9.markForDestroy()}else{_9.deparent()}}}}
,isc.A.$53h=function isc_ListGrid__remapEmbeddedComponentColumns(_1){if(_1.$4p==null)return;var _2=_1.$4p,_3={},_4=false;for(var i=0;i<_1.fields.length;i++){_3[_1.fields[i].name]=i}
var _6=[];for(var i=0;i<_2.length;i++){var _7=_2[i].$49k;if(_7==null){continue}
var _8=_3[_7];if(_8==null){_6[_6.length]=_2[i]}else{if(_2[i].$47r!=_8){_4=true;_2[i].$47r=_8}}}
if(_6.length>0){for(var i=0;i<_6.length;i++){var _9=_6[i];_1.removeEmbeddedComponent(_9.embeddedRecord,_9);if(this.shouldDestroyOnUnembed(_9,this.$52a)){_9.markForDestroy()}}}
if(_4){_1.$48b()}}
,isc.A.$56t=function isc_ListGrid__calculateEditCell(_1,_2){var _3,_4,_5=_1.$56d;if(_5==null){_3=_2}else{var _6=_1.$53o,_7=false,_8;if(_1.$56p!=null&&!isc.isA.emptyObject(_1._validationErrors_))
{_7=true}else{for(var _9 in _6){if(_6[_9]!=_5[_9]||_5[_9]===_8){_7=true;break}}}
if(!_7&&_1.$46t!=null&&!this.isEditingRecord(_1.$46t,_1.$56e))
{this.logInfo("dropping empty editSession","gridEdit");delete this.$53n[this.getEditValuesID(_1)];return null}
_3=this.findRowNum(_5,_1);_4=this.findColNum(_5,_1)}
return[_3,_4]}
,isc.A.$3y=function isc_ListGrid__filter(_1,_2,_3,_4,_5){if(!_5&&this.confirmDiscardEdits&&this.dataSource!=null){if(this.hasChanges()&&!(isc.ResultSet&&isc.isA.ResultSet(this.data)&&this.data.compareCriteria(_2,this.data.getCriteria())==0))
{this.showLostEditsConfirmation({target:this,methodName:"$56v"},{target:this,methodName:"$56w"});this.$56x={type:_1,criteria:_2,callback:_3,requestProperties:_4}
return}}
var _6=_2
if(isc.isA.Class(_6)){_6=isc.DynamicForm.getFilterCriteria(_6)}
this.setFilterValues(_6);return this.Super("$3y",[_1,_2,_3,_4],arguments)}
,isc.A.$56v=function isc_ListGrid__continueFilter(){var _1=this.$56x,_2=_1.type,_3=_1.criteria,_4=_1.callback,_5=_1.requestProperties;delete this.$56x;this.$3y(_2,_3,_4,_5,true)}
,isc.A.$56w=function isc_ListGrid__cancelFilter(){delete this.$56x}
,isc.A.showLostEditsConfirmation=function isc_ListGrid_showLostEditsConfirmation(_1,_2){this.$56y=_1;this.$56z=_2;isc.confirm(this.confirmDiscardEditsMessage,"if(window[this.targetGridID])window[this.targetGridID].lostEditsCallback(value, this);",{targetGridID:this.getID(),buttons:[isc.Dialog.OK,{title:this.discardEditsSaveButtonTitle,width:75,click:"this.hide();this.topElement.returnValue('save');"},isc.Dialog.CANCEL]})}
,isc.A.lostEditsCallback=function isc_ListGrid_lostEditsCallback(_1,_2){var _3=this.$56y,_4=this.$56z;delete this.$56y;delete this.$56z;if(_1==null){this.fireCallback(_4)}else if(_1==true){this.discardAllEdits();this.fireCallback(_3)}else if(_1=="save"){this.saveAllEdits(null,_3)}
_2.targetGrid=null}
,isc.A.findRowNum=function isc_ListGrid_findRowNum(_1){return this.data.indexOf(_1)}
,isc.A.findColNum=function isc_ListGrid_findColNum(_1){return-1}
,isc.A.$56r=function isc_ListGrid__moveEditor(_1,_2,_3){if(!this.$49j||_1==this.$471){return}
var _4=this.$471;this.logInfo(_3+": editSession: "+this.getEditValuesID(_1)+" with values: "+this.echo(this.$507(_1,_2))+" was being edited at row: "+_4+", will now edit at row: "+_1,"gridEdit");this.$471=_1;var _5=this.$6l,_6=_5?_5.getItems():null;if(_6){for(var i=0;i<_6.length;i++){_6[i].rowNum=_1}}}
,isc.A.rowEditNotComplete=function isc_ListGrid_rowEditNotComplete(_1){return(this.$56c!=null&&this.$56c[_1]!=null)}
,isc.A.$56n=function isc_ListGrid__editorChange(_1,_2,_3,_4){var _5=this.getCellRecord(_1,_2);if(this.editorChange!=null)this.editorChange(_5,_3,_4,_1,_2);var _6=this.getFieldName(_2);if(this.editValueChanged!=null)
this.editValueChanged(_1,_6,_3,_4);if(this.$49j&&this.isEditingRecord(_1,_2)&&this.fields!=null){_5=isc.addProperties({},this.$507(_1,_2),_5);var _7=!this.editByCell?this.fields:[this.getEditField()];for(var i=0;i<_7.length;i++){var _9=_7[i],_10=this.$6l.getItem(_9[this.fieldIdProperty]);if(_10==null)continue;var _11=this.getEditorValueMap(_9,this.getEditedRecord(_1,_2,true));if(_10.valueMap!=_11){var _12=true;if(isc.isAn.Array(_11)&&isc.isAn.Array(_10.valueMap)){_12=!_11.equals(_10.valueMap)}else if(isc.isAn.Object(_11)&&isc.isAn.Object(_10.valueMap)){if(isc.getKeys(_11).equals(isc.getKeys(_10.valueMap))){_12=false;for(var _13 in _11){if(_11[_13]!=_10.valueMap[_13]){_12=true;break}}}}
if(_12)_10.setValueMap(_11)}}}}
,isc.A.getEditRow=function isc_ListGrid_getEditRow(){return this.$471}
,isc.A.getEditCol=function isc_ListGrid_getEditCol(){return this.$504}
,isc.A.getEditField=function isc_ListGrid_getEditField(){return this.getField(this.getEditCol())}
,isc.A.getEditFieldName=function isc_ListGrid_getEditFieldName(){return this.getFieldName(this.getEditCol())}
,isc.A.getEditRecord=function isc_ListGrid_getEditRecord(){return this.getCellRecord(this.getEditRow(),this.getEditCol())}
,isc.A.cancelEditing=function isc_ListGrid_cancelEditing(_1){if(!this.$49j)return;this.discardEdits(this.getEditRow(),this.getEditCol(),false,_1)}
,isc.A.cellEditEnd=function isc_ListGrid_cellEditEnd(_1,_2){if(this.$502&&_1!=isc.ListGrid.TAB_KEYPRESS&&_1!=isc.ListGrid.SHIFT_TAB_KEYPRESS&&_1!=isc.ListGrid.UP_ARROW_KEYPRESS&&_1!=isc.ListGrid.DOWN_ARROW_KEYPRESS&&_1!=isc.ListGrid.LEFT_ARROW_KEYPRESS&&_1!=isc.ListGrid.RIGHT_ARROW_KEYPRESS)delete this.$502
var _3=this.getEditRow(),_4=this.getEditCol();if(_3==null&&_4==null)return;if(arguments.length>=2){this.setEditValue(_3,_4,_2)}
var _5=this.getFieldName(_4),_6=this.shouldCancelEdit(_3,_4,_1);if(_6)return this.cancelEditing(_1);var _7=this.getNextEditCell(_3,_4,_1);this.clearNewEditCell();if(_7==null){this.logInfo("cellEditEnd: ending editing, completion event: "+_1,"gridEdit");this.$560(_1);return}
if(Array.isLoading(this.getCellRecord(_7[0],_7[1]))){return false}
this.$55b(_1,_3,_4,_7[0],_7[1])}
,isc.A.getUpdatedEditorValue=function isc_ListGrid_getUpdatedEditorValue(){if(!this.$49j)return;var _1=this.$6l.getItem(this.getEditFieldName()),_2=this.getEditField(),_3;if(_1){if(_1.$20g())_1.updateValue();_3=_1.getValue();return this.$561(_3,_2,this.getEditRow(),this.getEditCol())}else{return this.getEditDisplayValue(this.getEditRow(),this.getEditCol())}}
,isc.A.storeUpdatedEditorValue=function isc_ListGrid_storeUpdatedEditorValue(_1,_2){if(!this.isDrawn()||!this.$49j)return;var _3=this.getEditRow();if(_2==null)_2=this.getEditCol();var _4=this.getField(_2),_5=this.$6l,_6=this.getEditFormItem(_2),_7;if(_6){if(_6.$20g())_6.updateValue();_7=this.$561(_6.getValue(),_4,_3,_2);var _8;if(_7===_8)_7=null;var _9=this.$535(_3,_2),_10;if(_9===_10||!this.fieldValuesAreEqual(_4,_9,_7)){this.setEditValue(_3,_2,_7,true,_1)}}}
,isc.A.$55e=function isc_ListGrid__handleEditorExit(_1,_2,_3,_4){var _5=this.getCellRecord(_2,_3),_6=this.getField(_3),_7=this.getEditorName(_2,_6),_8=true;var _9=this.$6l.getItem(_7);if(isc.isA.PopUpTextAreaItem(_9))_9.hidePopUp();if(_6&&_6.editorExit!=null){isc.Func.replaceWithMethod(_6,"editorExit","editCompletionEvent,record,newValue,rowNum,colNum,grid");_8=(_6.editorExit(_1,_5,_4,_2,_3,this)
!=false)}
if(_8&&this.editorExit!=null)
_8=this.editorExit(_1,_5,_4,_2,_3)!=false;return _8}
,isc.A.$55f=function isc_ListGrid__handleRowEditorExit(_1,_2,_3){var _4=this.getRecord(_2);if(this.rowEditorExit!=null)
return(this.rowEditorExit(_1,_4,_3,_2)!=false);return true}
,isc.A.$508=function isc_ListGrid__handleEditorEnter(_1,_2,_3,_4){var _5=this.getCellRecord(_2,_3),_6=this.getFieldName(_3),_7=this.getField(_3),_8=true;if(isc.isA.PopUpTextAreaItem(_1)&&_1.popUpOnEnter){_1.showPopUp(true)}
if(_7&&_7.editorEnter!=null){isc.Func.replaceWithMethod(_7,"editorEnter","record,value,rowNum,colNum,grid");_8=_7.editorEnter(_5,_4,_2,_3,this)!=false}
if(_8&&this.editorEnter!=null)
_8=this.editorEnter(_5,_4,_2,_3)!=false;return _8}
,isc.A.$509=function isc_ListGrid__handleRowEditorEnter(_1,_2,_3){var _4=this.getRecord(_2);if(this.rowEditorEnter!=null)
return this.rowEditorEnter(_4,_3,_2)!=false;return true}
,isc.A.$55g=function isc_ListGrid__validationEnabled(){if(!isc.DS)return false;var _1=isc.DS.get(this.dataSource);return!this.neverValidate&&!(_1&&_1.useLocalValidators!=null&&_1.useLocalValidators==false)}
,isc.A.shouldSaveOnCellExit=function isc_ListGrid_shouldSaveOnCellExit(_1,_2){if(this.getCellRecord(_1,_2)==null)return false;return(this.saveByCell!=null?this.saveByCell:this.editByCell)}
,isc.A.shouldSaveOnRowExit=function isc_ListGrid_shouldSaveOnRowExit(_1,_2,_3){return true}
,isc.A.shouldValidateByCell=function isc_ListGrid_shouldValidateByCell(_1,_2,_3){var _4=this.getField(_2);if(_4&&_4.validateByCell!=null)return _4.validateByCell;return(this.validateByCell!=null?this.validateByCell:this.editByCell)}
,isc.A.shouldValidateByRow=function isc_ListGrid_shouldValidateByRow(_1,_2,_3){return this.autoValidate}
,isc.A.shouldCancelEdit=function isc_ListGrid_shouldCancelEdit(_1,_2,_3){if(_3==isc.ListGrid.ESCAPE_KEYPRESS){var _4=this.getField(_2),_5=_4.escapeKeyEditAction||this.escapeKeyEditAction;if(_5=="cancel")return true}
return false}
,isc.A.$562=function isc_ListGrid__killEdit(_1,_2,_3){if(this.$55l!=_1)return;if(this.confirmCancelEditing){if(_3==null){var _4=this.getID()+".$562('"+_1+"','"+_2+"',value);";isc.ask(this.cancelEditingConfirmationMessage,_4);var _5=isc.Dialog.Warn.toolbar.getButton(0);if(_5.isDrawn())_5.focus()
else isc.Timer.setTimeout({target:_5,methodName:"focus"},0);return}else if(_3==false)return}
var _6=this.getEditRow(),_7=this.getEditCol(),_8=this.getUpdatedEditorValue(),_9=this.getEditValues(_6,_7);if(!this.$55e(_2,_6,_7,_8)||!this.$55f(_2,_6,_9))
{return}
var _10,_11=[],_12=this.$471,_13=this.getEditValues(_12,_7),_14=this.body?this.body.getDrawArea():[null,null,1,0],_15=_14[2],_16=_14[3],_17;if(_13!=null){if(this.isNewEditRecord(_12,_7)){if(this.isDrawn())this.body.markForRedraw("clearing extra edit row");_10=true}else{for(var i=_15;i<=_16;i++){if(_13[this.getFieldName(i)]!==_17&&((this.editByCell&&this.$504!=i)||!this.canEditCell(_12,i)))
{_11.add(i)}}}
this.$55x(this.$471,_7,true);if(this.isGrouped&&this.$55i(_12)){_10=true}}
if(this.body){this.hideInlineEditor(true);if(this.isDrawn()){if(!_10){for(var i=0;i<_11.length;i++){this.refreshCell(_12,_11[i])}}}}}
,isc.A.isNewEditRecord=function isc_ListGrid_isNewEditRecord(_1){var _2=this.getEditSession(_1);return _2&&_2.$56i}
,isc.A.endEditing=function isc_ListGrid_endEditing(){if(this.getEditRow()!=null)this.$560(isc.ListGrid.PROGRAMMATIC)}
,isc.A.$560=function isc_ListGrid__saveAndHideEditor(_1){if(this.$52t()){this.saveEdits(_1);return}
var _2=this.getEditRow(),_3=this.getEditCol(),_4=this.getEditValue(_2,_3),_5=this.getEditValues(_2,_3);if(!this.$55e(_1,_2,_3,_4)||!this.$55f(_1,_2,_5))return;var _6=(this.autoSaveEdits&&this.shouldWaitForSave()),_7=(this.autoSaveEdits&&this.stopOnErrors),_8;if(_6)_8="if((success||!this.stopOnErrors)&&"+"(this.$55l=="+this.$55l+"))"+"this.hideInlineEditor(true);";var _9=_1==isc.ListGrid.ESCAPE_KEYPRESS,_10=this.autoSaveEdits&&!_9;if(_10){this.saveEdits(_1,_8)}else{var _11=this.getFieldName(_3);var _12=!_9&&this.$55g()&&(this.shouldValidateByCell(_2,_3,_1)||this.shouldValidateByRow(_2,_3,_1));if(_12){var _13;if(this.useCellRecords){_13=!this.validateCell(_2,_3)}else{_13=!this.validateRow(_2)}
if(this.stopOnErrors&&_13)return false}
if(!this.rowHasChanges(_2,false)){this.logInfo("editor hiding at "+[_2,_3]+", no actual changes, dropping editSession","gridEdit");this.$55x(_2,_3)}}
if(!_6)this.hideInlineEditor(true);if(this.isGrouped&&(!_10||!this.shouldSaveLocally())){this.$55i(_2)}}
,isc.A.saveAndEditNewCell=function isc_ListGrid_saveAndEditNewCell(_1,_2,_3){if(_3==null)_3=isc.ListGrid.PROGRAMMATIC;this.$55h(_1,_2,_3)}
,isc.A.$55h=function isc_ListGrid__saveAndStartEditing(_1,_2,_3){var _4=this.shouldWaitForSave(),_5;if(_4){_5="if((success||!this.stopOnErrors)&&"+"(this.$55l=="+this.$55l+"))"+"this.$53j("+_1+","+_2+");"}
this.saveEdits(_3,_5);if(!_4){this.$53j(_1,_2)}}
,isc.A.$563=function isc_ListGrid__saveAndEditNextCell(_1,_2){var _3=this.getEditRow(),_4=this.getEditCol(),_5=isc.ListGrid.PROGRAMMATIC,_6=this.findNextEditCell(_3,_4,_1,_2,false);this.$55h(_6[0],_6[1],_5)}
,isc.A.saveAndEditNextCell=function isc_ListGrid_saveAndEditNextCell(){this.$563(1,true)}
,isc.A.saveAndEditPreviousCell=function isc_ListGrid_saveAndEditPreviousCell(){this.$563(-1,true)}
,isc.A.saveAndEditNextRow=function isc_ListGrid_saveAndEditNextRow(){this.$563(1,false)}
,isc.A.saveAndEditPreviousRow=function isc_ListGrid_saveAndEditPreviousRow(){this.$563(-1,false)}
,isc.A.getNextEditCell=function isc_ListGrid_getNextEditCell(_1,_2,_3){switch(_3){case isc.ListGrid.CLICK_OUTSIDE:case isc.ListGrid.ESCAPE_KEYPRESS:return null;case isc.ListGrid.ENTER_KEYPRESS:var _4=this.getField(_2),_5=_4.enterKeyEditAction||this.enterKeyEditAction;if(_5=="done")return null;else if(_5=="nextCell")
return this.findNextEditCell(_1,_2,1,true,false);else if(_5=="nextRow")
return this.findNextEditCell(_1,_2,1,false,false);else if(_5=="nextRowStart"){var _6=this.listEndEditAction||"done";if(_1+1<this.getTotalRows()||_6=="next"){return this.findNextEditCell(_1+1,0,1,true,true)}else{if(_6=="stop")return[_1,_2];return null}}
case isc.ListGrid.EDIT_FIELD_CHANGE:case isc.ListGrid.PROGRAMMATIC:return this.getNewEditCell();case isc.ListGrid.UP_ARROW_KEYPRESS:return this.findNextEditCell(_1,_2,-1,false,false);case isc.ListGrid.DOWN_ARROW_KEYPRESS:return this.findNextEditCell(_1,_2,1,false,false);case isc.ListGrid.TAB_KEYPRESS:case isc.ListGrid.RIGHT_ARROW_KEYPRESS:var _4=this.getField(_2);if(_4.nextTabColNum!=null){return this.findNextEditCell(_1+1,_4.nextTabColNum,1,true,true)}else{return this.findNextEditCell(_1,_2,1,true,false)}
case isc.ListGrid.SHIFT_TAB_KEYPRESS:case isc.ListGrid.LEFT_ARROW_KEYPRESS:var _4=this.getField(_2)
if(_4.previousTabColNum!=null){return this.findNextEditCell(_1-1,_4.previousTabColNum,-1,true,true)}else
return this.findNextEditCell(_1,_2,-1,true,false)}
this.logWarn("getNextEditCell(): Passed unrecognized editCompletionEvent type:"+_3+", returning null");return null}
,isc.A.findNextEditCell=function isc_ListGrid_findNextEditCell(_1,_2,_3,_4,_5,_6,_7,_8){var _9=_1,_10=_2,_11,_12=this.getFields().length;if(_12==0)return;_11=(_7?_9:(this.listEndEditAction=="next"||_6)?this.getTotalRows():this.getTotalRows()-1);if(_9>this.getTotalRows())_11=_9;if((_5!=false||_3==0)&&_9<=_11&&_9>=0&&_10<_12&&_10>=0&&this.canEditCell(_9,_10)&&(_8||this.$53i(_9,_10)))
{return[_9,_10]}
if(_3==0)return null;var _13=this.canEditCell(_9,_10)&&this.$53i(_9,_10),_14=_7?_9:0;if(_4){var _15=this.rowEndEditAction||"next";while(_9<(_11+1)&&_9>=_14){if(isc.ResultSet&&isc.isA.ResultSet(this.data)){if(_9<this.data.getLength()&&!this.data.rowIsLoaded(_9))return null}
_10+=_3;if(_15=="same"&&_2==_10){return _13?[_1,_2]:null}
if(_10<0||_10>=_12){if(_15=="done"||isc.isA.RecordEditor(this))return null;if(_15=="stop")return(_13?[_1,_2]:null)
else{_10=(_3>0?-1:_12);if(_15=="next")_9+=_3}}else if(this.canEditCell(_9,_10)&&(_8||this.$53i(_9,_10)))
{return[_9,_10]}}
if(this.listEndEditAction=="stop"||(this.listEndEditAction=="next"&&_9<_14))
{if(_13)return[_1,_2];else return null}else{return null}}else{_9+=_3
while(_9>=_14&&_9<=_11){if(this.canEditCell(_9,_10)&&(_8||this.$53i(_9,_10))){return[_9,_10]}
_9+=_3}
if(this.listEndEditAction=="stop"||(this.listEndEditAction=="next"&&_9<_14))
{return _13?[_1,_2]:null}
return null}}
,isc.A.$53i=function isc_ListGrid__canFocusInEditor(_1,_2){var _3=this.getFieldName(_2),_4=this.$6l,_5=_4?_4.getItem(_3):null;if(_5)return _5.$mb();var _6=this.getField(_3);if(_6.canFocus!=null)return _6.canFocus;var _7=this.getEditorType(_6,this.getEditedRecord(_1));_7=isc.FormItemFactory.getItemClassName(_6,_7,null,true);var _8=_7=isc.FormItemFactory.getItemClass(_7);return _8?_8.getPrototype().$mb():false}
,isc.A.setNewEditCell=function isc_ListGrid_setNewEditCell(_1,_2){this.$564=[_1,_2]}
,isc.A.getNewEditCell=function isc_ListGrid_getNewEditCell(){if(this.canEditCell(this.$564[0],this.$564[1]))return this.$564;return null}
,isc.A.clearNewEditCell=function isc_ListGrid_clearNewEditCell(){this.$564=null}
,isc.A.discardAllEdits=function isc_ListGrid_discardAllEdits(_1,_2){if(_1==null)_1=this.getAllEditRows(true);else{for(var i=0;i<_1.length;i++){if(isc.isA.Array(_1[i]))_1[i]=this.getEditValuesID(_1[i][0],_1[i][1]);if(isc.isA.Number(_1[i]))_1[i]=this.getEditValuesID(_1[i])}}
if(_1==null)return;for(var i=0;i<_1.length;i++)this.discardEdits(_1[i],null,_2)}
,isc.A.discardEdits=function isc_ListGrid_discardEdits(_1,_2,_3,_4){if(_1==null)return;var _5;if(isc.isA.Number(_1)){_5=this.getEditValuesID(_1,_2)}else{_5=_1;_1=this.getEditSessionRowNum(_5,_2)}
var _6=this.recordMarkedAsRemoved(_1);if(!_3&&this.$52t()&&(!(this.getTotalRows()==1&&_1==0)))
{this.logInfo("Refusing to hide editor on 'discardEdits' [editCompletionEvent:"+_4+"] due to alwaysShowEditors settings","inactiveEditorHTML");_3=true}
if(!_3&&this.$49j&&this.getEditRow()==_1){if(_4==null)_4=isc.ListGrid.PROGRAMMATIC;return this.$562(this.$55l,_4)}else{if(this.$49j&&this.getEditRow()==_1){this.storeUpdatedEditorValue(true)}
this.$55x(_5,_2)}
if(_6)this.refreshRow(_1)}
,isc.A.saveEdits=function isc_ListGrid_saveEdits(_1,_2,_3,_4,_5,_6){if(_1==null)_1=isc.ListGrid.PROGRAMMATIC;if(_3==null){_3=this.getEditRow()
_4=this.getEditCol()}else if(_4==null){_4=(_3==this.getEditRow()?this.getEditCol():this.getRowEditColNum(_3))}
if(_3==null)return false;if(this.recordMarkedAsRemoved(_3)){if(_5){return true}
var _7=this.isNewEditRecord(_3,_4);if(_7){this.discardEdits(_3);this.$565(_2,_3,_4,_1,true)}else{var _8=this;var _9=function(_18,_19,_20){_8.fireCallback(_2,"rowNum,colNum,editCompletionEvent,success",[_3,_4,_1,true])};this.removeData(this.getCellRecord(_3,_4),_9);return}}
var _10=this.getEditValues(_3,_4),_11=this.getCellRecord(_3,_4),_12=this.getEditValuesID(_3,_4);var _13;if(_11!=null&&!Array.isLoading(_11)){if(isc.isA.Tree(this.data)){_13=this.data.getCleanNodeData(_11,false)}else{_13=isc.addProperties({},_11)}
if(_13&&this.selection)delete _13[this.selection.selectionProperty]}
var _14={rowNum:_3,colNum:_4,oldValues:_13,editValuesID:_12,values:_10,editCompletionEvent:_1,newRecord:(_3>this.data.getLength())};if(this.logIsDebugEnabled("gridEdit")){this.logDebug("change detection: newValues: "+this.echo(_10)+", oldValues: "+this.echo(_13),"gridEdit")}
var _15=this.recordHasChanges(_3,_4,false);if(!_6&&this.$55g()){this.$566=(_5||_15);var _16;if(this.useCellRecords){_16=!this.validateCell(_3,_4)}else{_16=!this.validateRow(_3)}
if(_16){if(!_5)this.$567(_14,_2);return false}}
if(_5)return true;if(!_15){this.logInfo("saveEdits: no actual change, not saving","gridEdit");this.$55x(_12,_4);this.$565(_2,_3,_4,_1,true);return}
_10=this.$507(_3,_4);this.logInfo("Saving newValues '"+isc.echoAll(_10)+"'","gridEdit");this.rememberSubmittedEditValues(_3,_4);_10=isc.addProperties({},_10);var _17=this.saveEditedValues(_3,_4,_10,_13,_12,_1,_2);if(_17===false){this.$567(_14,_2);return false}
return true}
,isc.A.rowHasChanges=function isc_ListGrid_rowHasChanges(_1,_2){return this.recordHasChanges(_1,null,_2)}
,isc.A.recordHasChanges=function isc_ListGrid_recordHasChanges(_1,_2,_3){if(_3==null)_3=true;var _4=false,_5=(_3?this.getEditValues(_1,_2):this.$507(_1,_2)),_6=this.getCellRecord(_1,_2);if(!_6)return true;var _7=isc.addProperties({},_6,this.getSubmittedEditValues(_1,_2));for(var _8 in _5){if(_8==this.removeRecordProperty)continue;var _9=_7[_8],_10=_5[_8];if(!this.fieldValuesAreEqual(this.getField(_8),_9,_10)){_4=true;break}}
return _4}
,isc.A.hasChanges=function isc_ListGrid_hasChanges(_1){var _2=this.getAllEditRows();if(_2!=null){for(var i=0;i<_2.length;i++){if(this.recordMarkedAsRemoved(_2[i]))return true;if(this.rowHasChanges(_2[i],_1))return true}}
return false}
,isc.A.cellHasChanges=function isc_ListGrid_cellHasChanges(_1,_2,_3){if(_1==null||_2==null)return false;var _4=this.getField(_2),_5=_4?_4.dataPath:null,_6=this.getEditorName(_1,_2),_7=(_3?this.getEditValues(_1,_2):this.$507(_1,_2));if(!_7)return false;var _8=this.getCellRecord(_1,_2);if(!_8)return true;var _9;if(_5){_9=isc.Canvas.$3e(this.$536(_5),_7)}else{_9=_7[_6]}
var _10;if(_9===_10)return false;var _11=this.getSubmittedEditValues(_1,_2),_12;if(_5){if(_11){_12=isc.Canvas.$3e(this.$536(_5),_11)}
if(_12==null){_12=isc.Canvas.$3e(this.$536(_5),_8)}
if(_12==null){_12=isc.Canvas.$3e(this.$536(_5),_11)}}else{_12=_11!=null?this.$ef(_11[_6],_8[_6]):_8[_6]}
return!this.fieldValuesAreEqual(_4,_12,_9)}
,isc.A.saveAllEdits=function isc_ListGrid_saveAllEdits(_1,_2){this.$568=this.$568||{};this.$569=this.$569||{};this.$57a=this.$57a||{};if(_1==null)_1=this.getAllEditCells();if(_1==null)return false;if(!isc.isAn.Array(_1))_1=[_1];if(this.$55g()){for(var i=0;i<_1.length;i++){var _4=isc.isAn.Array(_1[i])?_1[i][0]:_1[i],_5=isc.isAn.Array(_1[i])?_1[i][1]:null;var _6=this.recordMarkedAsRemoved(_4)||this.saveEdits(null,null,_4,_5,true);if(!_6){if(this.stopOnErrors)return false;else{_1[i]=null;_2=null}}}}
var _7=(this.dataSource!=null&&!this.shouldSaveLocally()),_8=false;if(_7)_8=!isc.RPCManager.startQueue();var _9=this.$52b++,_10=this.$568[_9]=[],_11=this.$569[_9]=[];this.$57a[_9]=_2;var _12=false;var _13=["this.$57b('",,"',rowNum,"+_9+",colNum,editCompletionEvent,success)"];for(var i=0;i<_1.length;i++){if(_1[i]==null)continue;var _4=_1[i],_5;if(isc.isAn.Array(_4)){_5=_4[1];_4=_4[0]}
var _14=this.recordMarkedAsRemoved(_4);if(!this.recordHasChanges(_4,_5)&&!_14){continue}
_12=true;var _15=this.getEditValuesID(_4);_13[1]=_15;if(_14){_11[_11.length]=_15}else{_10[_10.length]=_15}
this.saveEdits(null,_13.join(""),_4,_5,false,true)}
if(_8){isc.RPCManager.sendQueue(null,null,null,true)}
return _12}
,isc.A.$57b=function isc_ListGrid__saveAllEditsRowCallback(_1,_2,_3,_4,_5,_6){if(!_6){delete this.$57a[_3];delete this.$568[_3];delete this.$569[_3];return}
var _7=this.$568[_3],_8=this.$569[_3];if(_7==null&&_8==null)return;var _9,_10,_11;for(_9=0;_9<_7.length;_9++){var _12=_7[_9];if(_12==_1){_10=true;break}}
if(!_10){for(_9=0;_9<_8.length;_9++){var _12=_8[_9];if(_12==_1){_10=true;break}}
if(_10)_11=true}
if(_11){_8.removeAt(_9)}else{_7.removeAt(_9)}
if(_7.length==0&&_8.length==0){this.$565(this.$57a[_3],_2,_4,_5);delete this.$57a[_3];delete this.$568[_3];delete this.$569[_3]}}
,isc.A.$561=function isc_ListGrid__parseEditorValue(_1,_2,_3,_4,_5){var _6;if(_5===_6)_5=this.getCellRecord(_3,_4);if(_2&&_2.parseEditorValue!=null){isc.Func.replaceWithMethod(_2,"parseEditorValue","value,record,rowNum,colNum,grid");_1=_2.parseEditorValue(_1,_5,_3,_4,this)}else if(this.parseEditorValue!=null){_1=this.parseEditorValue(_1,_5,_3,_4)}
return _1}
,isc.A.saveEditedValues=function isc_ListGrid_saveEditedValues(_1,_2,_3,_4,_5,_6,_7){var _8={editValuesID:_5,rowNum:_1,colNum:_2,values:_3,oldValues:_4,editCompletionEvent:_6};if(this.shouldSaveLocally()){if(this.useRemoteValidators==true){var _9=false,_10=this.getDataSource(),_11=_10?_10.getFieldNames():null;if(_11!=null){for(var i=0;i<_11.length;i++){var _13=_10.getField(_11[i]),_14=_13.validators;if(_14==null)continue;if(!isc.isAn.Array(_14))_14=[_14];for(var _15=0;_15<_14.length;_15++){if(isc.Validator.isServerValidator(_14[_15])){_9=true;break}}
if(_9)break}}
if(_9){var _16={validationMode:"partial",prompt:isc.RPCManager.validateDataPrompt,showPrompt:this.shouldWaitForSave(),clientContext:{editInfo:_8,saveCallback:_7},componentID:this.ID};_10.validateData(_8.values,{target:this,methodName:"remoteValidationForLocalSaveComplete"},_16);if(this.$525==null)this.$525={};this.$525[_8.editValuesID]=true;return}}
return this.$57c(_8,_7)}
if(isc.Offline&&isc.Offline.isOffline()&&!this.dataSource.clientOnly){isc.warn(this.offlineSaveMessage);return false}
var _17=this.getID()+".$57d(dsResponse, dsRequest)",_18={operation:(_4==null?this.addOperation:this.updateOperation)||this.saveOperation,application:this.application,willHandleError:true,showPrompt:this.shouldWaitForSave(),oldValues:_4,$57e:this.getRecord(_1,_2),clientContext:{saveCallback:_7,newValues:_3,editInfo:_8},componentId:this.ID};if(this.saveRequestProperties){isc.addProperties(_18,this.saveRequestProperties)}
this.$57f(_3,_4);if(this.$525==null)this.$525={};this.$525[_8.editValuesID]=true;var _10=isc.DS.get(this.dataSource);if(_4==null){var _19=isc.addProperties({},_4,_3);_10.addData(_19,_17,_18)}else{var _20=isc.DS.get(this.dataSource).filterPrimaryKeyFields(_4),_21=isc.addProperties({},_20,_3);_10.updateData(_21,_17,_18)}}
,isc.A.$57c=function isc_ListGrid__saveLocally(_1,_2){var _3=_1.rowNum,_4=_1.colNum,_5=_1.oldValues,_6=_1.values;var _7=this.getCellRecord(_3,_4),_8=(_7==null);if(_8){var _9=this.getData();var _10=this.getEditSession(_3);_10.$56d=this.getPrimaryKeys(_6);delete _10.$56i;_9.add(_6);_9.dataChanged()}else{var _11,_12;var _13={};for(var i=0;i<this.completeFields.length;i++){var _15=this.completeFields[i],_16=this.getEditorName(_3,_15,true);_11=isc.Canvas.$3e(this.$536(_16),_6,this,true);if(_11!==_12){var _17=this.fields.indexOf(_15);if(_17!=-1){this.setRawCellValue(_7,_3,_17,_11)}else{if(_15.dataPath){isc.Canvas.$3d(this.$536(_15.dataPath),_11,_7,this,true)}else{_7[_15[this.fieldIdProperty]]=_11}}}
var _18=_16;if(_16.indexOf("/")!=null){_18=_16.substring(0,_16.indexOf("/"))}
_13[_18]=true}
for(var _19 in _6){if(_13[_19]==true)continue;_7[_19]=_6[_19]}}
this.$57f(_6,_5);this.$57g(_1,_2);if(this.valuesManager!=null){this.valuesManager.$41(_3,null,_7,this)}}
,isc.A.remoteValidationForLocalSaveComplete=function isc_ListGrid_remoteValidationForLocalSaveComplete(_1,_2,_3){var _4=_1.clientContext,_5=_4.editInfo;if(this.$525&&this.$525[_5.editValuesID]){delete this.$525[_5.editValuesID];if(isc.isAn.emptyObject(this.$525))delete this.$525}
if(_1.status<0&&_1.status!=isc.RPCResponse.STATUS_VALIDATION_ERROR){isc.logWarn("Server-side validation failed: "+_1.data);isc.RPCManager.handleError(_1,_3);return}
var _6=this.$507(_5.editValuesID);if(_1.errors){var _7=isc.DynamicForm.getSimpleErrors(_1.errors),_8=false;for(var _9 in _7){_8=true;if(_6==null){this.logWarn("Asynchronous remote validation failed for attempted save of edits "+"for row:"+_5.rowNum+" but edit values have subsequently been "+"discarded. Ignoring validation failure.  Validation failure details:\n"+this.echoFull(_7));break}
var _10=_7[_9],_11=this.getField(_9);if(_10!=null&&_11!=null){var _12=_5.values[_9],_13=_6[_9];if(_12!=_13&&!(isc.isA.Date(_13)&&isc.isA.Date(_12)&&(_13.getTime()==_12.getTime())))
{this.logWarn("Asynchronous remote validation failed for attempted save of edits "+"for row:"+_5.rowNum+". Submitted value for field "+_9+" was "+_12+", but the edit value has subsequently been modified "+"and is now:"+_13+". Not showing the following validation "+"error[s] for this field:\n"+this.echoAll(_10));continue}
if(!isc.isAn.Array(_10))_10=[_10];_10=_10.getProperty("errorMessage");this.setFieldError(_5.rowNum,_9,_10,false)}}
if(_8)return}
this.$57c(_5,_4.saveCallback)}
,isc.A.$57f=function isc_ListGrid__unsortOnChange(_1,_2){var _3=this.$4h();if(_3==null)return;var _4=this.fields[_3];if(_4==null)return;var _5=_4[this.fieldIdProperty];var _6;if(_5!=null&&_1[_5]!==_6&&(_2==null||_1[_5]!=_2[_5]))
{this.unsort()}}
,isc.A.$57d=function isc_ListGrid__updateRecordReply(_1,_2){var _3=_1.data,_4=_1.clientContext,_5=_4.editInfo,_6=_5.colNum,_7=this.fields[_6];if(this.$525&&this.$525[_5.editValuesID]){delete this.$525[_5.editValuesID];if(isc.isAn.emptyObject(this.$525))delete this.$525}
var _8=this.getEditSession(_5.editValuesID);if(_8!=null){_5.rowNum=_8.$46t}else{if(_3&&_3[0]){_5.rowNum=this.findRowNum(_3[0]);if(this.useCellRecords)_5.colNum=this.findColNum(_3[0])}
else _5.rowNum=-1}
var _9=_5.rowNum,_6=_5.colNum;if(_1.status<0){var _10=this.parseServerErrors(_1.errors);this.logInfo("error on save, status: "+_1.status+", errors: "+this.echo(_1.errors),"gridEdit");if(isc.isAn.Array(_10)){if(_10.length!=1)
this.logWarn("Server reports validation errors for multiple records - only "+"displaying errors for the first record.","gridEdit");_10=_10[0]}
if(this.useCellRecords)this.setCellErrors(_9,_6,_10[this.getEditorName(_9,_6)]);else this.setRowErrors(_9,_10);this.$567(_5,_4.saveCallback,_1,_2);return false}
this.$57g(_5,_4.saveCallback,_1,_2)}
,isc.A.parseServerErrors=function isc_ListGrid_parseServerErrors(_1){if(isc.isAn.Array(_1)){if(_1.length>1){this.logWarn("server returned errors for multiple records - dropping all but the "+"first record returned")}
_1=_1[0]}
if(_1&&_1.recordPath)delete _1.recordPath;for(var _2 in _1){var _3=_1[_2];if(_3==null)_3="Unspecified error";if(isc.isAn.Array(_3)){for(var i=0;i<_3.length;i++){_3[i]=_3[i].errorMessage}}else if(_3.errorMessage){_1[_2]=[_3.errorMessage]}else{_1[_2]=[_3]}}
return _1}
);isc.evalBoundary;isc.B.push(isc.A.$57g=function isc_ListGrid__editCompleteCallback(_1,_2,_3,_4){if(this.$49j&&this.getEditForm().hasFocus)this.storeUpdatedEditorValue(true);var _5=_1.rowNum,_6=_1.colNum,_7=_1.editValuesID,_8=this.getEditSession(_7);var _9;if(_3!=null){var _10=isc.DataSource.getUpdatedData(_4,_3,true),_9=isc.isAn.Array(_10)?_10[0]:_10;if(_9==null){return}
if(_8&&_8.$56d==null){this.newRecordForEditValues(_7,_9)}}else{_9=this.getCellRecord(_5,_6)}
var _11=_1.values,_12=this.$507(_7),_13=this.isEditingRecord(_5,_6),_14=this.dataSource?isc.DS.get(this.dataSource).getPrimaryKeyFieldNames():null;var _15=false;for(var _16 in _12){var _17=_14&&_14.contains(_16);if(this.fieldValuesAreEqual(this.getField(_16),_12[_16],_11[_16]))
{if(!(_13&&_17))this.clearEditValue(_7,_16,true,true)}else{if(!_17)_15=true}}
if(!_13&&!_15){this.$55x(_7,_6)}
if(_3!=null||_1.newRecord){this.displayUpdatedDSRecord(_5,_6,_9)}
var _18=_1.values,_19=_1.oldValues,_20=_1.editCompletionEvent;if(_5==-1)_5=_1.rowNum;if(this.convertToMethod("editComplete")){this.editComplete(_5,_6,_18,_19,_20,_3)}
this.$57h(_9,_5,_6,_18,_19,_4);if(_2)this.$565(_2,_5,_6,_20,true)}
,isc.A.displayUpdatedDSRecord=function isc_ListGrid_displayUpdatedDSRecord(_1,_2,_3){if(this.$52s()){this.suppressEditRowRemap=true;this.dataChanged();this.suppressEditRowRemap=false}else if(this.isGrouped){this.regroup()}}
,isc.A.isEditingRecord=function isc_ListGrid_isEditingRecord(_1,_2){return this.getEditRow()==_1}
,isc.A.$565=function isc_ListGrid__fireSaveCallback(_1,_2,_3,_4,_5){if(_1!=null){this.fireCallback(_1,this.$52c,[_2,_3,_4,_5])}}
,isc.A.$57h=function isc_ListGrid__fireCellChanged(_1,_2,_3,_4,_5,_6){var _7,_8;for(var i in _4){var _10=(_5==null?null:_5[i]);if(_4[i]==_10)continue;var _11=this.fields.find(this.fieldIdProperty,i);if(!_11)continue;if(this.isGrouped&&_11){var _12=this.getGroupByFields();if(_12.contains(_11.name))_7=true}
if(this.shouldAutoFitField(_11)){_8=true}
this.$57i(_1,_11,_4[i],_10,_2,this.fields.indexOf(_11))}
if(_8){if(this.shouldSaveLocally()){this.updateFieldWidthsForAutoFitValue("Local edit")}}
if(_7){if(this.shouldSaveLocally()){this.$57j(_1,_1,_2,_4)}
this.$495=false;this.$523()}}
,isc.A.$57i=function isc_ListGrid__cellChanged(_1,_2,_3,_4,_5,_6){if(_2&&_2.cellChanged){if(!isc.isA.Function(_2.cellChanged)){isc.Func.replaceWithMethod(_2,"cellChanged","record,newValue,oldValue,rowNum,colNum,grid,recordNum,fieldNum")}
_2.cellChanged.call(this,_1,_3,_4,_5,_6,this,_5,_6)}else if(this.cellChanged){this.cellChanged(_1,_3,_4,_5,_6,this,_5,_6)}}
,isc.A.$567=function isc_ListGrid__editFailedCallback(_1,_2,_3,_4){var _5=_1.rowNum,_6=_1.colNum,_7=_1.values,_8=_1.oldValues,_9=_1.editCompletionEvent;if(this.convertToMethod("editFailed")){this.editFailed(_5,_6,_7,_8,_9,_3,_4)}
if(_2!=null)this.$565(_2,_5,_6,_9,false);this.clearSubmittedEditValues(_1.editValuesID,_7)}
,isc.A.editFailed=function isc_ListGrid_editFailed(_1,_2,_3,_4,_5,_6,_7){if(_6!=null&&_6.errors==null){isc.RPCManager.$a2(_6,_7)}}
,isc.A.validateRow=function isc_ListGrid_validateRow(_1,_2){return this.validateRecord(_1,_2)}
,isc.A.validateRecord=function isc_ListGrid_validateRecord(_1,_2){var _3,_4;if(isc.isAn.Array(_1)){_3=_1[0];_4=_1[1]}else{_3=_1}
var _5=this.getEditValues(_3,_4);if(_5==null)return true;var _6=this.getCellRecord(_3,_4),_7=this.getFields().getProperty(this.fieldIdProperty);var _8=isc.rpc.startQueue();var _9=this.validateRowValues(_5,_6,_3,_7);if(!_8)isc.rpc.sendQueue();this.setRowErrors(_3,_9,_2);return(_9==null)}
,isc.A.validateRowValues=function isc_ListGrid_validateRowValues(_1,_2,_3,_4){var _5=true,_6={};if(!isc.isAn.Array(_4))_4=[_4];var _7=false;if(_2==null){_7=true;_2={}}
for(var i=0;i<_4.length;i++){var _9,_10;if(isc.isA.Number(_4[i])){_10=_4[i];_9=_4[i]=this.getFieldName(_10)}else{_9=_4[i];_10=this.getFields().findIndex(this.fieldIdProperty,_9)}
if(_10<0)continue;var _11=((_7&&this.canEditCell(_3,_10))||isc.propertyDefined(_1,_9));if(!_11)continue;var _12=_1[_9],_13=_2[_9],_14=this.validateCellValue(_3,_10,_12,_13);if(_14!=null){_5=false;_6[_9]=_14}}
if(!_5)return _6;return null}
,isc.A.validateCell=function isc_ListGrid_validateCell(_1,_2,_3,_4){var _5;if(isc.isA.String(_2))_5=this.getColNum(_2);else{_5=_2;_2=this.getEditorName(_1,_5)}
if(this.$49j&&(this.getEditRow()==_1)&&this.getEditForm().getItem(_2)!=null)
{this.storeUpdatedEditorValue(null,_5)}
var _6=this.getEditValues(_1,_5),_7=this.cellHasErrors(_1,_2),_8=_6?_6[_2]:null,_9=this.getCellRecord(_1,_5),_10=_9?_9[_2]:null;if(_9!=null&&(!_6||!isc.propertyDefined(_6,_2))){return true}
var _11=this.validateCellValue(_1,_5,_8,_10,_4);if(_11!=null){this.setFieldError(_1,_2,_11);return false}
if(_7){this.setFieldError(_1,_2,null)}
return true}
,isc.A.validateCellValue=function isc_ListGrid_validateCellValue(_1,_2,_3,_4,_5){var _6=this.getCellRecord(_1,_2);var _7=this.getField(_2);return this.validateFieldValue(_3,_4,_6,_7,_1,_2,_5)}
,isc.A.getRequiredFieldMessage=function isc_ListGrid_getRequiredFieldMessage(_1,_2){return isc.Validator.requiredField}
,isc.A.validateFieldValue=function isc_ListGrid_validateFieldValue(_1,_2,_3,_4,_5,_6,_7){var _8=[],_9=true;var _10=this.cellIsRequired(_5,_6);if(_10&&(_1==null||isc.isAn.emptyString(_1))){var _11=this.getRequiredFieldMessage(_4,_3);_8.add(_11);_9=false}
var _12=this.getCellValidators(_5,_6);if(_12){if(this.logIsDebugEnabled("gridEdit")){this.logDebug((this.useCellRecords?"At col:"+_6:"At field: "+_4.name)+" applying validators: "+this.echoAll(_12)+" to value:"+_1,"gridEdit")}
var _13,_14=this.getEditedRecord(_5,_6),_15=null,_16={rowNum:_5};if(this.$566==true){_16.skipServerValidation=true;this.$566=null}
if(_7){_13=this.validateFieldAndDependencies(_4,_12,_1,_14,_16)}else{_13=this.validateField(_4,_12,_1,_14,_16)}
if(_13!=null){if(_13.valid!=true){_9=false;if(_7)
_8=_13.errors[_4.name]||[];else
_8=_13.errors||[]}else{if(_13.resultingValue!=null){this.setEditValue(_5,_6,_13.resultingValue);_1=_13.resultingValue}}
_15=_13.errors;if(this.logIsInfoEnabled("gridEdit")){this.logInfo("validateFieldValue, newValue: "+this.echo(_1)+", passed validation: "+_9+", resultingValue: "+this.echo(_13.resultingValue),"gridEdit")}}}
if(_7){for(var _17 in _15){if(_17!=_4.name){this.setFieldError(_5,_17,_15[_17],true)}}}
if(_9)return null
else return _8}
,isc.A.cellIsRequired=function isc_ListGrid_cellIsRequired(_1,_2){var _3=this.getField(_2);return _3&&(_3.required||this.isXMLRequired(_3))}
,isc.A.getCellValidators=function isc_ListGrid_getCellValidators(_1,_2){var _3=this.getField(_2);return _3?_3.validators:null}
,isc.A.hasErrors=function isc_ListGrid_hasErrors(){var _1=this.getAllEditRows(true);for(var i=0;i<_1.length;i++){if(this.rowHasErrors(_1[i]))return true}
return false}
,isc.A.rowHasErrors=function isc_ListGrid_rowHasErrors(_1,_2){var _3=this.getEditSession(_1,_2);return(_3&&_3.$56p&&!isc.isA.emptyObject(_3.$56p))}
,isc.A.cellHasErrors=function isc_ListGrid_cellHasErrors(_1,_2){var _3=this.getEditorName(_1,_2),_4=this.getEditSession(_1,_2),_5=_4?_4.$56p:null;return(_5!=null&&_5[_3]!=null)}
,isc.A.getRowValidationErrors=function isc_ListGrid_getRowValidationErrors(_1){return this.getRowErrors(_1)}
,isc.A.getRowErrors=function isc_ListGrid_getRowErrors(_1,_2){var _3=this.getEditSession(_1,_2);return(_3!=null?_3.$56p:null)}
,isc.A.getCellErrors=function isc_ListGrid_getCellErrors(_1,_2){var _3=this.getEditSession(_1,_2);if(_3==null)return null;if(isc.isA.Number(_2))_2=this.getEditorName(_1,_2);var _4=_3.$56p;return(_4==null?null:_4[_2])}
,isc.A.setCellErrors=function isc_ListGrid_setCellErrors(_1,_2,_3,_4){return this.setFieldError(_1,_2,_3,_4)}
,isc.A.setFieldError=function isc_ListGrid_setFieldError(_1,_2,_3,_4){var _5=_2;if(isc.isA.Number(_5))_5=this.getEditorName(_1,_5);if(_3==null||(isc.isAn.Array(_3)&&_3.length==0)){if(!this.cellHasErrors(_1,_2))return;var _6=this.getEditSession(_1,_2);delete _6.$56p[_5];if(isc.isAn.emptyObject(_6.$56p))delete _6.$56p}else{if(_5==null){this.logWarn("setFieldError() passed field identifier '"+_2+"'. "+"Unable to find corresponding field in this grid - not setting error.");return}
var _7=this.getFields().getProperty(this.fieldIdProperty);if(!_7.contains(_5))
{this.logWarn("setFieldError() passed field identifier '"+_2+"'. This "+"is not a visible field in the grid - error may not be visible to user.")}
var _8;if(isc.isAn.Array(_3))_8=_3
else _8=[_3];var _6=this.getEditSession(_1,_2);if(_6==null){var _9=this.getColNum(_2),_10=this.getCellRecord(_1,_9);_6=this.createEditSession(_1,_9,_10)}
if(_6.$56p==null)_6.$56p={};_6.$56p[_5]=_8}
if(!_4){if(this.useCellRecords)this.showCellErrors(_1,_2)
else this.showErrors(_1,[_5])}}
,isc.A.addFieldErrors=function isc_ListGrid_addFieldErrors(_1,_2,_3,_4){this.setFieldError(_4,_1,_2)}
,isc.A.setRowErrors=function isc_ListGrid_setRowErrors(_1,_2,_3){if(_1==null||_1==-1)return;var _4=this.getRowErrors(_1);if(!_2&&!_4)return;var _5=this.getEditSession(_1);if(_5==null)_5=this.createEditSession(_1,null,this.getRecord(_1));if(_2){var _6=this.getFields().getProperty(this.fieldIdProperty);for(var _7 in _2){if(!_3&&!_6.contains(_7)){this.logWarn("setRowErrors() passed error for non-visible field: '"+_7+"'")}
if(!isc.isAn.Array(_2[_7]))_2[_7]=[_2[_7]]}}
_5.$56p=_2;if(!_3){var _8=_2?isc.getKeys(_2):[];for(var _7 in _4){var _9=this.getColNum(_7);if(!_8.contains(_7))_8.add(_7)}
this.showErrors(_1,_8)}}
,isc.A.clearFieldError=function isc_ListGrid_clearFieldError(_1,_2,_3){return this.setFieldError(_1,_2,null,_3)}
,isc.A.clearRowErrors=function isc_ListGrid_clearRowErrors(_1,_2){this.setRowErrors(_1,null,_2)}
,isc.A.showErrors=function isc_ListGrid_showErrors(_1,_2){if(_2==null)_2=this.getFields().getProperty(this.fieldIdProperty);var _3=this.getRowErrors(_1);if(_3==null)_3={};var _4;for(var i=0;i<_2.length;i++){var _6=_2[i];this.showCellErrors(_1,_6);if(_4==null&&_3[_6])_4=_6}
var _7=this.assembleErrorMessage(_3,_2);if(_7){this.displayRowErrorMessages(_7)}
if(this.stopOnErrors&&_4!=null){var _8=this.getFields().findIndex(this.fieldIdProperty,_4);this.showInlineEditor(_1,_8)}}
,isc.A.showCellErrors=function isc_ListGrid_showCellErrors(_1,_2){var _3;if(isc.isA.Number(_2)){this.getEditorName(_1,_2)}else{_3=_2;_2=this.getColNum(_3)}
if(_2==-1)return;if(this.showErrorIcons){this.refreshCell(_1,_2,null,true)}else{this.body.$48f(null,_1,_2)}}
,isc.A.assembleErrorMessage=function isc_ListGrid_assembleErrorMessage(_1,_2){var _3=false,_4="Validation Errors occurred:\r\n";for(var _5 in _1){if(_2&&!_2.contains(_1))continue;var _6=this.$57k(_5,_1[_5]);if(_6!=null){_3=true;_4+=_6}}
return(_3?_4:null)}
,isc.A.$57k=function isc_ListGrid__createFieldErrorString(_1,_2){var _3,_4,_5=this.getField(_1),_6=_5&&_5.title?_5.title:_1;for(var i=0;i<_2.length;i++){var _8=_2[i];if(_8!=null){if(_4==null){_4="<br>Field '"+_6+"':"}
_4+="<br>* "+_8}}
return _4}
,isc.A.displayRowErrorMessages=function isc_ListGrid_displayRowErrorMessages(_1){if(this.stopOnErrors)isc.warn(_1,"var LG="+this.getID()+";if(LG.$6l)LG.$6l.focus()")}
,isc.A.removeData=function isc_ListGrid_removeData(_1,_2,_3){_3=isc.addProperties({},_3);if(_3.clientContext==null){_3.clientContext={}}
_3.clientContext.removeDataCallback=_2;if(this.getDataSource()!=null&&!this.shouldSaveLocally()){return this.Super("removeData",[_1,{target:this,methodName:"removeDataComplete"},_3],arguments)}
if(this.data){var _4=this.getEditValuesID(_1);if(_4!=null)this.discardEdits(_4);this.getOriginalData().remove(_1);this.updateFieldWidthsForAutoFitValue("removeData");this.regroup()}
this.fireCallback({target:this,methodName:"removeDataComplete"},"dsResponse,data,dsRequest",[null,_1,_3])}
,isc.A.removeDataComplete=function isc_ListGrid_removeDataComplete(_1,_2,_3){if(_1&&_1.status!=0){}else{if(!isc.isAn.Array(_2))_2=[_2];for(var i=0;i<_2.length;i++){var _5=this.getEditValuesID(_2[i]);if(_5!=null){this.discardEdits(_5)}}}
if(_3&&_3.clientContext&&_3.clientContext.removeDataCallback){this.fireCallback(_3.clientContext.removeDataCallback,"dsResponse,data,dsRequest",[_1,_2,_3])}}
,isc.A.removeRecordClick=function isc_ListGrid_removeRecordClick(_1,_2){var _3=this.warnOnRemoval,_4=this.shouldDeferRemoval();if(_3&&_4&&this.recordMarkedAsRemoved(_1))_3=false;if(_3){isc.ask(this.warnOnRemovalMessage,this.getID()+".completeRemoveRecordClick(value, "+_1+","+_2+")")}else{this.completeRemoveRecordClick(true,_1,_2)}}
,isc.A.completeRemoveRecordClick=function isc_ListGrid_completeRemoveRecordClick(_1,_2,_3){if(!_1)return;if(this.shouldDeferRemoval()){if(this.recordMarkedAsRemoved(_2)){this.unmarkRecordRemoved(_2)}else{this.markRecordRemoved(_2)}}else{var _4=this.getAllEditRows(),_5=_4.contains(_2);if(_5)this.discardEdits(_2,_3);var _6=this.getCellRecord(_2,_3);if(_6==null)return;this.delayCall("removeRecord",[_2,_6])}}
,isc.A.removeRecord=function isc_ListGrid_removeRecord(_1,_2){if(_2==null)_2=this.data.get(_1);else _1=(this.data?this.data.indexOf(_2):null)
if(_2==null||_1==null||_1==-1||!this.data)return;if(isc.Offline&&isc.Offline.isOffline()&&this.dataSource&&!this.dataSource.clientOnly){isc.warn(this.offlineSaveMessage);return}
var _3=this.animateRemoveRecord&&this.isDrawn()&&this.isVisible()&&this.body&&_1>=this.body.$6i&&_1<=this.body.$459;if(_2.$4p&&_2.$4p.length>0){_3=false}
if(_3){this.$528=true;var _4=1;if(isc.isA.Tree(this.data)&&this.data.isFolder(_2)&&this.data.isOpen(_2)){var _5=this.data.getChildren(_2);if(_5)_4+=_5.getLength()}
this.$57l=_4;this.body.$46j(false,_1,_1+_4)}
var _6=this.getDataSource(),_7=(_6==null||this.shouldSaveLocally());if(!_7){if(!_6.getPrimaryKeyField){this.logWarn("DataSource:"+_6+" has no primary key field - unable to remove records");return}}
var _8;if(_3){_8=this.getID()+".showRemoveCompleteAnimation("+_1+", data,dsResponse);"}
this.removeData(_2,_8,{showPrompt:false})}
,isc.A.showRemoveCompleteAnimation=function isc_ListGrid_showRemoveCompleteAnimation(_1,_2,_3){if(_3&&_3.status<0){this.logWarn("Removal of record failed");_2=null}
if(!_2){delete this.$528;if(this.body){delete this.body.$46k;delete this.body.$46m;delete this.body.$46p;delete this.body.$46q}
this.markForRedraw()}else{this.startRowAnimation(false,_1,_1+this.$57l,{target:this,methodName:"$57m"},this.animateRemoveSpeed,this.animateRemoveTime);delete this.$57l}}
,isc.A.$57m=function isc_ListGrid__removeDataAnimationComplete(){delete this.$528;this.redraw()}
,isc.A.startRowAnimation=function isc_ListGrid_startRowAnimation(_1,_2,_3,_4,_5,_6,_7,_8,_9){if(!this.body)return;this.finishRowAnimation();this.$57n=_4;if(this.frozenBody){this.frozenBody.startRowAnimation(_1,_2,_3,{target:this,methodName:"rowAnimationComplete",argNames:"body,hasFrozenBody",args:[this.frozenBody,true]},_5,_6,_7,_8,true,_9)}
this.body.startRowAnimation(_1,_2,_3,{target:this,methodName:"rowAnimationComplete",argNames:"body,hasFrozenBody",args:[this.body,(this.frozenBody!=null)]},_5,_6,_7,_8,true,_9)}
,isc.A.rowAnimationComplete=function isc_ListGrid_rowAnimationComplete(_1,_2){var _3=!_2||this.$57o;if(!_3){this.$57o=true}else{delete this.$57o;var _4=this.$57n;delete this.$57n;if(_4!=null)this.fireCallback(_4)}}
,isc.A.finishRowAnimation=function isc_ListGrid_finishRowAnimation(){if(this.body)this.body.finishRowAnimation();if(this.frozenBody)this.frozenBody.finishRowAnimation()}
,isc.A.animateRowHeight=function isc_ListGrid_animateRowHeight(_1,_2,_3,_4,_5,_6,_7){if(!this.body)return;return this.body.animateRowHeight(_1,_2,_3,_4,_5,_6,_7,true)}
,isc.A.setCellHeight=function isc_ListGrid_setCellHeight(_1){var _2=this.cellHeight;this.cellHeight=_1;if(this.body)this.body.cellHeight=_1;if(_2!=_1&&this.isDrawn()){this.body.markForRedraw("Cell height changed");if(this.frozenBody)this.frozenBody.markForRedraw("Cell height changed")}}
,isc.A.setRowHeight=function isc_ListGrid_setRowHeight(_1,_2){if(!this.body)return;if(this.frozenBody!=null)this.frozenBody.setRowHeight(_1,_2);return this.body.setRowHeight(_1,_2)}
,isc.A.getDragTrackerIcon=function isc_ListGrid_getDragTrackerIcon(_1){var _2=_1?_1[0]:null,_3;if(_2){var _4=this.getTitleField();_3=this.getValueIcon(this.getField(_4),_2[_4],_2)}
if(_3==null)_3=this.trackerImage;return _3}
,isc.A.getDragTrackerTitle=function isc_ListGrid_getDragTrackerTitle(_1,_2){var _3=this.getTitleField(),_4=this.getColNum(_3),_5=this.getCellValue(_1,_2,_4);return"<nobr>"+_5+"</nobr>"}
,isc.A.dragStart=function isc_ListGrid_dragStart(){if(this.canDragSelect)return true;var _1=this.selection.getSelection();if(!_1||_1.length==0)return false;for(var i=0;i<_1.length;i++){if(_1[i].canDrag==false)return false}
this.$4q=_1;return true}
,isc.A.dragMove=function isc_ListGrid_dragMove(){var _1=isc.EH.dropTarget;if(!this.canDragRecordsOut&&_1!=null&&_1!=this&&!this.contains(_1))
{return false}}
,isc.A.dropMove=function isc_ListGrid_dropMove(){if(!this.canAcceptDroppedRecords&&isc.EH.dragTarget!=this)return true;if(!this.willAcceptDrop()){this.body.setNoDropIndicator()}else{this.body.clearNoDropIndicator()}
if(!this.canReorderRecords)return true;this.showDragLineForRecord()}
,isc.A.dropOut=function isc_ListGrid_dropOut(){this.body.clearNoDropIndicator();this.hideDragLine()}
,isc.A.dragStop=function isc_ListGrid_dragStop(){this.body.clearNoDropIndicator();this.hideDragLine();this.$4q=null}
,isc.A.willAcceptDrop=function isc_ListGrid_willAcceptDrop(){var _1=this.ns.EH;if(!this.Super("willAcceptDrop",arguments)&&_1.dragTarget!=this.body)return false;if(_1.dragTarget==this){if(!this.$57p())return false}else{if(!this.canAcceptDroppedRecords)return false}
if(!isc.isAn.Object(_1.dragTarget.getDragData()))return false;isc.$ny=true;var _2=this.getEventRecordNum(),_3=this.getEventFieldNum();var _4=this.getReorderPosition(_2);if(_2!=-2&&_4==isc.ListGrid.AFTER)_2+=1;isc.$ny=false;if(_2<-1)return true;if(_2==-1)return false;var _5=this.getRecord(_2,_3);if(!this.recordIsEnabled(_2,_3)||(_5!=null&&_5.canAcceptDrop==false))return false;return true}
,isc.A.$57p=function isc_ListGrid__canDragRecordsToSelf(){return this.canReorderRecords}
,isc.A.drop=function isc_ListGrid_drop(){if(this.willAcceptDrop()==false)return false;var _1=this.ns.EH.dragTarget;var _2=this.getDropIndex();var _3=_1.cloneDragData();var _4=(_2!=-2?this.data.get(_2):null);if(this.onRecordDrop!=null&&(this.onRecordDrop(_3,_4,_2,_1)==false))
{return false}
return this.recordDrop(_3,_4,_2,_1)}
,isc.A.recordDrop=function isc_ListGrid_recordDrop(_1,_2,_3,_4){this.transferRecords(_1,_2,(this.canReorderRecords?_3:null),_4);if(this.recordsDropped)this.recordsDropped(_1,_3,this,_4);return false}
,isc.A.viewRow=function isc_ListGrid_viewRow(_1){this.getDataSource().viewFile(this.getRecord(_1))}
,isc.A.downloadRow=function isc_ListGrid_downloadRow(_1){this.getDataSource().downloadFile(this.getRecord(_1))}
,isc.A.autoSizeColumn=function isc_ListGrid_autoSizeColumn(_1){var _2=this.body.getColumnAutoSize(_1);if(_2!=null)this.resizeField(_1,_2)}
,isc.A.getRecord=function isc_ListGrid_getRecord(_1,_2){return this.getCellRecord(_1,_2)}
,isc.A.getCellRecord=function isc_ListGrid_getCellRecord(_1,_2){if(!isc.isA.Number(_1)){this.logWarn("getCellRecord called with bad rowNum: "+this.echo(_1));return}
if(!this.data||_1>=this.data.getLength())return null;var _3=this.data.get(_1);if(this.$56s)this.$57q(_3,_1);return _3}
,isc.A.$57q=function isc_ListGrid__testRowEditData(_1,_2){if(_1==null||_1[this.editValuesTestedProperty])return;if(this.$56c==null)this.$56c={};var _3=true;for(var i in this.$53n){var _5=this.$53n[i];if(_5!=null&&_5.$46t==null){if(this.comparePrimaryKeys(_1,_5.$56d)){_5.$46t=_2;this.$56c[_2]=i}else{_3=false}}}
if(_3)delete this.$56s;_1[this.editValuesTestedProperty]=true}
,isc.A.comparePrimaryKeys=function isc_ListGrid_comparePrimaryKeys(_1,_2){var _3=this.getPrimaryKeys(_1);if(!this.dataSource||this.shouldSaveLocally())return(_3==_2);for(var _4 in _3){if(_3[_4]!=_2[_4])return false}
return true}
,isc.A.getPrimaryKeys=function isc_ListGrid_getPrimaryKeys(_1){if(this.shouldSaveLocally())return _1;var _2=this.getDataSource(),_3=_2.getPrimaryKeyFieldNames(),_4={};if(!isc.isAn.Array(_3))_3=[_3];for(var i=0;i<_3.length;i++){_4[_3[i]]=_1[_3[i]]}
return _4}
,isc.A.getTotalRows=function isc_ListGrid_getTotalRows(){if(this.data==null)return 0;if(this.data.getLength==null){this.logWarn("GetTotalRows: ListGrid has invalid data object:"+this.echo(this.data));return 0}
var _1=this.data.getLength(),_2=this.$56q();if(_2!=null&&_2+1>_1)_1=_2+1;if(this.showNewRecordRow)_1+=1;return _1}
,isc.A.$56q=function isc_ListGrid__getLastEditRow(){if(this.$56j==null){var _1=-1,_2=(this.$56c?isc.getKeys(this.$56c):[]);for(var i=0;i<_2.length;i++){var _4=parseInt(_2[i]);if(_4>_1)_1=_4}
this.$56j=_1}
return this.$56j}
,isc.A.recordIsEnabled=function isc_ListGrid_recordIsEnabled(_1,_2){var _3=this.getCellRecord(_1,_2);if(_3==null)return true;if(this.recordMarkedAsRemoved(_1))return false;return(_3[this.recordEnabledProperty]!=false&&_3[this.isSeparatorProperty]!=true)}
,isc.A.cellIsEnabled=function isc_ListGrid_cellIsEnabled(_1,_2){return this.recordIsEnabled(_1,_2)}
,isc.A.getCellField=function isc_ListGrid_getCellField(_1,_2){return this.getField(_2)}
,isc.A.getFields=function isc_ListGrid_getFields(){return this.fields}
,isc.A.getAllFields=function isc_ListGrid_getAllFields(){return this.completeFields||this.fields}
,isc.A.getSpecifiedField=function isc_ListGrid_getSpecifiedField(_1){var _2=this.completeFields;if(_2==null)_2=this.fields;if(_2==null)return;if(isc.isAn.Object(_1)){if(_2.contains(_1))return _1;return null}else if(isc.isA.String(_1))return _2.find(this.fieldIdProperty,_1);else return(_2[_1])}
,isc.A.getFieldName=function isc_ListGrid_getFieldName(_1){var _2=this.getField(_1);return _2?_2[this.fieldIdProperty]:null}
,isc.A.getField=function isc_ListGrid_getField(_1){if(this.fields==null||_1==null)return null;var _2;if(this.$53q){_2=this.fields[_1];if(_2!=null)return _2}else{if(isc.isA.Number(_1))return this.fields[_1]}
if(_1.$53p||isc.isAn.Object(_1))return _1;if(isc.isA.String(_1)){var _3=_1.contains(isc.Canvas.$3a);if(!_3){return this.fields.find(this.fieldIdProperty,_1)}else{var _4,_5,_6,_7=_1.trim(isc.Canvas.$3a),_8,_9,_10,_11;if(_1.startsWith(isc.Canvas.$3a)){_5=_1.substring(1);if(_1.endsWith(isc.Canvas.$3a)){_6=_1.substring(0,_1.length)}else{_4=_1+"/"}}else{if(_1.endsWith(isc.Canvas.$3a)){_6=_1.substring(0,_1.length);_4="/"+_1}else{_4="/"+_1+"/"}
_8="/"+_1}
var _12=_7.lastIndexOf(isc.Canvas.$3a);if(_12!=-1){_9=_7.substring(_12+1);_10=_9+"/"}
for(var i=0;i<this.fields.length;i++){var _14=this.fields[i].dataPath;if(!_14)continue;if(_14==_1||(_5&&_14==_5)||(_6&&_14==_6)||(_7&&_14==_7)||(_8&&_14==_8)||(_4&&_14==_4)||(_9&&_14==_9)||(_10&&_14==_10))
{return this.fields[i]}}}}
return null}
,isc.A.getColNum=function isc_ListGrid_getColNum(_1){return this.getFieldNum(_1)}
,isc.A.getFieldTitle=function isc_ListGrid_getFieldTitle(_1){var _2;if(isc.isAn.Object(_1))_2=_1;else _2=this.fields[_1];if(!_2)return"Unknown field";if(_2.getFieldTitle){isc.Func.replaceWithMethod(_2,"getFieldTitle","viewer,fieldNum");return _2.getFieldTitle(this,_1)}
return _2.title||_2.name}
,isc.A.getSummaryTitle=function isc_ListGrid_getSummaryTitle(_1){var _2;if(_1.getSummaryTitle!=null){isc.Func.replaceWithMethod(_1,"getSummaryTitle","viewer,field");_2=_1.getSummaryTitle(this,_1)}else if(_1.summaryTitle!=null){_2=_1.summaryTitle}else{_2=this.getFieldTitle(_1)}
return _2}
,isc.A.setValueMap=function isc_ListGrid_setValueMap(_1,_2){this.Super("setValueMap",arguments);if(this.$49j){var _3,_4;var _5=this.getColNum(_1);if(_5!=-1&&_5<this.fields.length){var _4=this.getField(_1),_3=_4[this.fieldIdProperty];this.$6l.setValueMap(_3,this.getEditorValueMap(_4,this.getEditedRecord(this.getEditRow(),_5)))}}
if(this.summaryRow){this.summaryRow.setValueMap(_1,_2)}
if(this.isDrawn()&&this.isVisible()){this.$52w("setValueMap")}}
,isc.A.getDisplayValue=function isc_ListGrid_getDisplayValue(_1,_2){var _3=this.getField(_1),_4=_3?_3.valueMap:null;if(isc.isAn.Object(_4)&&!isc.isAn.Array(_4)&&isc.propertyDefined(_4,_2))
{return _4[_2]}
return _2}
,isc.A.getData=function isc_ListGrid_getData(){return this.data}
,isc.A.getRecordIndex=function isc_ListGrid_getRecordIndex(_1){var _2=-1;if(this.body&&this.body.isDrawn()){_2=this.data.indexOf(_1,this.body.$6i,this.body.$459)}
if(_2==-1)_2=this.data.indexOf(_1);return _2}
,isc.A.getRecordCellIndex=function isc_ListGrid_getRecordCellIndex(_1){return[this.getRecordIndex(),0]}
,isc.A.getEventRow=function isc_ListGrid_getEventRow(_1){if(this.header&&this.header.containsPoint(this.ns.EH.getX(),this.ns.EH.getY())){return-1}
return this.body.getEventRow(_1)}
,isc.A.getEventColumn=function isc_ListGrid_getEventColumn(_1){return this.body.getEventColumn(_1)}
,isc.A.getEventRecordNum=function isc_ListGrid_getEventRecordNum(_1){return this.getEventRow(_1)}
,isc.A.getEventFieldNum=function isc_ListGrid_getEventFieldNum(_1){return this.getEventColumn(_1)}
,isc.A.getReorderPosition=function isc_ListGrid_getReorderPosition(_1,_2){if(_2==null)_2=this.body.getOffsetY();if(_1==null)_1=this.getEventRow(_2);if(_1==-2)return isc.ListGrid.AFTER;if(_1>=this.getTotalRows())return isc.ListGrid.BEFORE;var _3=this.body.getRowTop(_1),_4=this.body.getRowSize(_1);if(_2>Math.round(_3+_4/ 2)){return isc.ListGrid.AFTER}else{return isc.ListGrid.BEFORE}}
,isc.A.getDropIndex=function isc_ListGrid_getDropIndex(_1,_2){if(this.data.getLength()==0){return 0}
if(_1==null)_1=this.getEventRow();if(_2==null)_2=this.getReorderPosition(_1);if(this.canReorderRecords){if(_1==-2)_1=this.data.getLength()-1;if(_2==isc.ListGrid.AFTER)_1++}
return _1}
,isc.A.selectionChanged=function isc_ListGrid_selectionChanged(){}
,isc.A.getSerializeableFields=function isc_ListGrid_getSerializeableFields(_1,_2){_1.addList(["header","selection"]);return this.Super("getSerializeableFields",arguments)}
,isc.A.deriveFrozenFields=function isc_ListGrid_deriveFrozenFields(){var _1=this.frozenFields=this.fields.findAll("frozen",true),_2=this.fields.findAll("autoFreeze",true),_3=this.fields.findAll("$57r",true);if(_1&&_3&&_1.length==_3.length){for(var i=0;i<_3.length;i++){var _5=_3[i];_5.frozen=false;delete _5.$57r}
_1=this.frozenFields=null}
var _6=this.completeFields.findAll("frozen",true);if(_1&&_1.length==this.fields.length){_1=this.frozenFields=null;this.$52x=true}else this.$52x=false;if(_1){if(_2){for(var i=0;i<_2.length;i++){var _5=_2[i];if(!_5.frozen){_5.frozen=true;_5.$57r=true;var _7=_5.getAutoFreezePosition?_5.getAutoFreezePosition():this.freezeLeft()?0:this.fields.length,_8=_5.getAutoFreezePosition?_5.getAutoFreezePosition():this.freezeLeft()?0:this.completeFields.length;_1.addAt(_5,_7);_6.addAt(_5,_8)}}}
this.fields.slideList(_1,this.freezeLeft()?0:this.fields.length);this.completeFields.slideList(_6,this.freezeLeft()?0:this.completeFields.length);this.resizeFieldsInRealTime=false}
for(var i=0;i<this.fields.length;i++){var _5=this.fields[i];_5.masterIndex=i}
if(_1){var _9=this.normalFields=[];for(var i=0;i<this.fields.length;i++){var _5=this.fields[i];if(!_5.frozen)_9.add(_5)}}else this.normalFields=null}
,isc.A.rebuildForFreeze=function isc_ListGrid_rebuildForFreeze(_1){if(!this.body)return;if(this.filterEditor){this.filterEditor.deriveVisibleFields();this.filterEditor.updateBody(_1);this.filterEditor.remapEditFieldsForFreeze();this.filterEditor.layoutChildren(this.$52d)}
this.endEditing();this.deriveVisibleFields();this.updateHeader();this.updateBody(_1);this.layoutChildren(this.$52d);if(this.frozenBody){this.frozenBody.markForRedraw("Recalculating draw area from initial sizing of body")}
if(this.summaryRow&&this.showGridSummary){this.summaryRow.setFields(this.completeFields.duplicate());this.summaryRow.rebuildForFreeze(_1);this.summaryRow.$52u()}}
,isc.A.setCanFreezeFields=function isc_ListGrid_setCanFreezeFields(_1){this.canFreezeFields=_1;delete this.$520}
,isc.A.refreshMasterIndex=function isc_ListGrid_refreshMasterIndex(){if(!this.body)return;if(this.header)this.$57s(this.header.getMembers());if(this.body)this.$57s(this.body.fields);if(!this.frozenFields)return;if(this.frozenHeader)this.$57s(this.frozenHeader.getMembers());if(this.frozenBody)this.$57s(this.frozenBody.fields)}
,isc.A.$57s=function isc_ListGrid__refreshMasterIndexForFields(_1){for(var i=0;i<_1.length;i++){_1[i].masterIndex=this.fields.findIndex("name",_1[i].name)}}
,isc.A.getFreezeOn=function isc_ListGrid_getFreezeOn(){return(this.freezeOn!=null?this.freezeOn:this.isRTL()?"right":"left")}
,isc.A.freezeLeft=function isc_ListGrid_freezeLeft(){return this.getFreezeOn()=="left"}
,isc.A.getLeftBody=function isc_ListGrid_getLeftBody(){return this.frozenFields&&this.freezeLeft()?this.frozenBody:this.body}
,isc.A.getRightBody=function isc_ListGrid_getRightBody(){return this.frozenFields&&!this.freezeLeft()?this.frozenBody:this.body}
,isc.A.setFreezeOn=function isc_ListGrid_setFreezeOn(_1){var _2=this.get
this.freezeOn=_1;if(_1!=_2)this.rebuildForFreeze(true)}
,isc.A.getFrozenSlots=function isc_ListGrid_getFrozenSlots(_1){var _2=this.frozenFields;if(!_2)return _1;return this.freezeLeft()?_1.slice(0,_2.length):_1.slice(_1.length-_2.length)}
,isc.A.getUnfrozenSlots=function isc_ListGrid_getUnfrozenSlots(_1){var _2=this.frozenFields;if(!_2)return _1;return this.freezeLeft()?_1.slice(_2.length):_1.slice(0,_1.length-_2.length)}
,isc.A.fieldIsFrozen=function isc_ListGrid_fieldIsFrozen(_1){if(!this.frozenFields)return false;var _2=this.getField(_1);return _2&&_2.frozen}
,isc.A.getFieldBody=function isc_ListGrid_getFieldBody(_1){if(!this.frozenFields)return this.body;return this.fieldIsFrozen(_1)?this.frozenBody:this.body}
,isc.A.getFieldHeader=function isc_ListGrid_getFieldHeader(_1){if(!this.frozenFields)return this.header;return this.fieldIsFrozen(_1)?this.frozenHeader:this.header}
,isc.A.getFieldHeaderButton=function isc_ListGrid_getFieldHeaderButton(_1){var _2=this.getFieldHeader(_1);return _2?_2.getMember(this.getLocalFieldNum(_1)):null}
,isc.A.getLocalFieldNum=function isc_ListGrid_getLocalFieldNum(_1){if(this.body&&this.body.isPrinting){var _2=this.fields[_1];if(!this.$52x&&_2.frozen&&this.frozenBody){return this.frozenBody.fields.indexOf(_2)}else{return this.body.fields.indexOf(_2)}}
if(!this.frozenFields)return _1;var _3;_3=this.frozenFields.length;if(!this.fieldIsFrozen(_1))return _1-_3;return this.freezeLeft()?_1:_1-(this.fields.length-this.frozenFields.length)}
,isc.A.getFieldNumFromLocal=function isc_ListGrid_getFieldNumFromLocal(_1,_2){if(this.isPrinting){var _3=this.frozenFields!=null?(_2==this.frozenBody?this.frozenFields:this.unfrozenFields):this.fields;var _4=_2.fields[_1];for(var i=0;i<_3.length;i++){if(_3[i].name==_4.name)break;if(_3[i].shouldPrint==false)_1++}}
if(!this.frozenFields)return _1;if(_2==this.frozenBody){if(this.freezeLeft())return _1;else{var _6=this.fields.length-this.frozenFields.length;return _1+_6}}else{var _7=this.freezeLeft()?this.frozenFields.length:0;return _1+_7}}
,isc.A.getColumnLeft=function isc_ListGrid_getColumnLeft(_1){var _2=this.getFieldBody(_1);return _2.getColumnLeft(this.getLocalFieldNum(_1))}
,isc.A.getColumnPageLeft=function isc_ListGrid_getColumnPageLeft(_1){var _2=this.getFieldBody(_1);return _2.getColumnPageLeft(this.getLocalFieldNum(_1))}
,isc.A.getColumnWidth=function isc_ListGrid_getColumnWidth(_1){var _2=this.getFieldBody(_1);return _2.getColumnWidth(this.getLocalFieldNum(_1))}
,isc.A.refreshCellStyle=function isc_ListGrid_refreshCellStyle(_1,_2,_3){var _4=this.getFieldBody(_2);return _4.refreshCellStyle(_1,this.getLocalFieldNum(_2),_3)}
,isc.A.freezeField=function isc_ListGrid_freezeField(_1){return this.toggleFrozen(_1,true)}
,isc.A.unfreezeField=function isc_ListGrid_unfreezeField(_1){return this.toggleFrozen(_1,false)}
,isc.A.toggleFrozen=function isc_ListGrid_toggleFrozen(_1,_2){if(!isc.isAn.Array(_1))_1=[_1];var _3=false;for(var i=0;i<_1.length;i++){var _5=this.getField(_1[i]);if(!_5||!this.fieldIsVisible(_5)||this.isCheckboxField(_5))
{continue}
var _6=_2;if(_6==null)_6=!_5.frozen;if(_5.frozen==_2)continue;_3=true;_5.frozen=_6}
if(!_3)return false;this.rebuildForFreeze();this.handleViewStateChanged();return true}
,isc.A.updateBody=function isc_ListGrid_updateBody(_1){if(_1||(this.frozenFields&&!this.frozenBody)||(this.frozenBody&&!this.frozenFields))
{_1=true;this.dropRecordComponents();if(this.frozenFields){this.$57t=this.showRollOverCanvas;this.$57u=this.showSelectionCanvas;this.showRollOverCanvas=false;this.showSelectionCanvas=false;if(this.$57t)this.updateRollOverCanvas();if(this.$57u)this.updateSelectionCanvas()}else{if(this.$57t)this.showRollOverCanvas=true;if(this.$57u)this.showSelectionCanvas=true;delete this.$57t;delete this.$57u}
if(this.body){this.$57v()}
if(this.bodyLayout)this.bodyLayout.destroy();if(this.body)this.body.destroy();this.body=this.bodyLayout=this.frozenBody=null;this.createBodies()}
if(this.isDrawn()){this.updateGridComponents()}
if(this.body){this.body.fields=this.normalFields||this.fields;if(!_1)this.$53h(this.body);this.body.markForRedraw("fields change")}
if(this.frozenBody){this.frozenBody.fields=this.frozenFields;if(!_1)this.$53h(this.frozenBody);this.frozenBody.markForRedraw("fields change")}}
,isc.A.shouldDestroyOnUnembed=function isc_ListGrid_shouldDestroyOnUnembed(_1,_2){if(_1.dontAutoDestroy)return false;if(_1.isBackgroundComponent){if(_2==this.$49s&&_1.creator==this)return true;return false}
if(_1.isRecordComponent){if(_2==this.$49s)return true;return false}
if(_1.destroyOnUnembed!=null)return _1.destroyOnUnembed;if(_2==this.$52a)return false;return true}
,isc.A.$57v=function isc_ListGrid__destroyEmbeddedComponentsForRebuild(){var _1=this.body.$4p;if(this.frozenBody){var _2=[];if(_1==null)_1=_2;else _1=_2.addList(_1);_1.addList(this.frozenBody.$4p||[])}
if(_1&&_1.length>0){for(var i=0;i<_1.length;i++){var _4=_1[i].embeddedRecord,_5=false;if(_4.$4p){for(var j=0;j<_4.$4p.length;j++){var _7=_4.$4p[j];_5=_5||_7.isExpansionComponent;if(this.shouldDestroyOnUnembed(_7,this.$52e)){_7.markForDestroy()}else{_7.deparent()}}
_4.$4p=null;if(_5)_4.expanded=false;delete _4.$4p}}}}
,isc.A.createBodies=function isc_ListGrid_createBodies(){if(this.body!=null)return;this.$54b();this.body=this.createBody(this.ID+"_body",this.normalFields||this.fields);this.body.resizeTo("100%","100%");this.bodies=[this.body];this.dragScrollTarget=this.body;var _1=this.frozenFields;if(_1==null||_1.length==0){return this.body}
this.frozenBody=this.createBody(this.ID+"$57w",_1,true);if(this.body.hscrollOn&&!this.shrinkForFreeze){this.frozenBody.setEndSpace(this.body.getScrollbarSize())}
if(this.freezeLeft()){this.bodies.unshift(this.frozenBody)}else{this.bodies.add(this.frozenBody)}
this.bodyLayout=this.createAutoChild("bodyLayout",{width:"100%",height:"100%",autoDraw:false,members:this.bodies},isc.HLayout)}
,isc.A.bodyOverflowed=function isc_ListGrid_bodyOverflowed(){if(this.summaryRow!=null&&this.summaryRow.body){this.summaryRow.body.setRightSpace(this.body.vscrollOn?this.body.getScrollbarSize():0)}
if(!this.frozenBody)return;this.frozenBody.setEndSpace(this.body.hscrollOn&&!this.shrinkForFreeze?this.body.getScrollbarSize():0)}
,isc.A.createBody=function isc_ListGrid_createBody(_1,_2,_3){var _4=isc.ClassFactory.getClass(this.bodyConstructor).createRaw();_4.ID=_1;_4.autoDraw=false;_4.grid=this;_4.fields=_2;_4.frozen=_3;_4.overflow=_3?"hidden":this.bodyOverflow;_4.backgroundColor=this.bodyBackgroundColor;var _5=this.bodyStyleName;if(this.alternateBodyStyleName!=null&&this.alternateRecordStyles){_5=this.alternateBodyStyleName}
_4.styleName=_5;_4.allowContentAndChildren=true;_4.fixedRowHeights=this.fixedRecordHeights;_4.fixedColumnWidths=this.fixedFieldWidths;_4.alternateRowStyles=this.alternateRecordStyles;_4.alternateRowFrequency=this.alternateRecordFrequency;_4.canSelectText=this.canDragSelectText;_4.showAllRows=this.showAllRecords;if(this.virtualScrolling!=null)_4.virtualScrolling=this.virtualScrolling;if(_4.virtualScrolling)_4.fixedRowHeights=false;_4.dragTarget=this.canDragSelect?null:this;_4.dragAppearance=isc.EventHandler.NONE;_4.locatorParent=this;_4.selectionAppearance=this.selectionAppearance;var _6=isc.ListGrid.$496;for(var i=0;i<_6.length;i++){var _8=_6[i],_9=this[_8];if(_9!=null)_4[_8]=_9}
isc.addProperties(_4,this.bodyDefaults,this.bodyProperties);this.$57x(_4);if(_3)_4.showEmptyMessage=false;_4.completeCreation();return _4}
);isc.evalBoundary;isc.B.push(isc.A.getRowHeight=function isc_ListGrid_getRowHeight(_1,_2){var _3=this.cellHeight;if(this.frozenFields&&this.getEditRow()==_2){var _4=this.getEditForm(),_5=_4?_4.getItems():[];for(var i=0;i<_5.length;i++){var _7=(_5[i].getHeight()+2*this.cellPadding);if(_7>_3)_3=_7}}
return this.body.updateHeightForEmbeddedComponents(_1,_2,_3)}
,isc.A.makeSelectionCanvas=function isc_ListGrid_makeSelectionCanvas(_1){this.selectionCanvas=this.createAutoChild("selectionCanvas",{eventProxy:this.body,snapTo:"TL",width:"100%",height:"100%",bubbleMouseEvents:true,destroyOnUnEmbed:false,percentSource:this,percentBox:"custom",grid:this,autoDraw:false});this.selectionUnderCanvas=this.createAutoChild("selectionUnderCanvas",{eventProxy:this.body,snapTo:"TL",width:"100%",height:"100%",bubbleMouseEvents:true,destroyOnUnEmbed:false,percentSource:this,percentBox:"custom",grid:this,autoDraw:false})}
,isc.A.getSelectionCanvas=function isc_ListGrid_getSelectionCanvas(){if(!this.selectionCanvas)this.makeSelectionCanvas();return this.selectionCanvas}
,isc.A.getSelectionUnderCanvas=function isc_ListGrid_getSelectionUnderCanvas(){if(!this.selectionUnderCanvas)this.makeSelectionCanvas();return this.selectionUnderCanvas}
,isc.A.updateSelectionCanvas=function isc_ListGrid_updateSelectionCanvas(){var _1=this.selection,_2,_3,_4;if(isc.isA.CellSelection(_1)){var _5=_1.getSelectedCells()[0];_2=_5?_5[0]:-1,_3=_5?_5[1]:-1;_4=_5?this.getCellRecord(_2,_3):null}else{var _4=this.getSelectedRecord();_2=this.data?this.data.indexOf(_4):-1}
var _6=this.selectionCanvas,_7=_6?_6.embeddedRecord:null,_8=_6?_6.$475:-1,_9=_6?_6.$47r:-1;if(this.selectionCanvas==null){if(_4==null||!this.showSelectionCanvas)return}else if(this.showSelectionCanvas&&_7==_4&&_8==_2&&_9==_3)
{return}
if(this.showSelectionCanvas&&this.selectionType!=isc.Selection.SINGLE&&this.selectionType!=isc.Selection.NONE)
{this.logWarn("showSelectionCanvas is set to true, but not supported for selectionType "+this.selectionType);this.showSelectionCanvas=false}
if(!_4||_2==-1||!this.showSelectionCanvas){if(_7){this.removeEmbeddedComponent(_7,_6);var _10=this.selectionUnderCanvas;this.removeEmbeddedComponent(_7,_10)}
return}
var _6=this.getSelectionCanvas(),_10=this.getSelectionUnderCanvas();_6.record=_4;_10.record=_4;this.addEmbeddedComponent(_6,_4,_2,_3,"within");this.addEmbeddedComponent(_10,_4,_2,_3,"within")}
,isc.A.makeRollOverCanvas=function isc_ListGrid_makeRollOverCanvas(){this.rollOverCanvas=this.createAutoChild("rollOverCanvas",{eventProxy:this.body,percentSource:this,percentBox:"custom",bubbleMouseEvents:true,destroyOnUnEmbed:false,grid:this,autoDraw:false});this.rollUnderCanvas=this.createAutoChild("rollUnderCanvas",{eventProxy:this.body,percentSource:this,percentBox:"custom",bubbleMouseEvents:true,destroyOnUnEmbed:false,grid:this,autoDraw:false})}
,isc.A.getRollOverCanvas=function isc_ListGrid_getRollOverCanvas(_1,_2){if(!this.rollOverCanvas)this.makeRollOverCanvas();return this.rollOverCanvas}
,isc.A.getRollUnderCanvas=function isc_ListGrid_getRollUnderCanvas(_1,_2){if(!this.rollUnderCanvas)this.makeRollOverCanvas();return this.rollUnderCanvas}
,isc.A.updateRollOverCanvas=function isc_ListGrid_updateRollOverCanvas(_1,_2,_3){var _4=false,_5;if(_3||!this.showRollOverCanvas||_1==-1){_4=true}else{var _5=this.getCellRecord(_1,_2);if(_5==null||Array.isLoading(_5)){_4=true}}
if(_4){if(this.rollOverCanvas){this.removeEmbeddedComponent(this.rollOverCanvas.embeddedRecord,this.rollOverCanvas);this.removeEmbeddedComponent(this.rollUnderCanvas.embeddedRecord,this.rollUnderCanvas)}
return}
if(!this.useCellRollOvers)_2=null;var _6=this.rollOverCanvas=this.getRollOverCanvas(_1,_2),_7=this.rollUnderCanvas=this.getRollUnderCanvas(_1,_2);_6.record=_5;_7.record=_5;this.addEmbeddedComponent(_6,_5,_1,_2,"within");this.addEmbeddedComponent(_7,_5,_1,_2,"within")}
,isc.A.updateEmbeddedComponentZIndex=function isc_ListGrid_updateEmbeddedComponentZIndex(_1){if(!_1)return;if(this.selectionCanvas==_1){var _2=this.body.getTableZIndex();this.selectionCanvas.setZIndex(_2+50)}
if(this.selectionUnderCanvas==_1){var _2=this.body.getTableZIndex();this.selectionUnderCanvas.setZIndex(_2-100)}
if(this.rollOverCanvas==_1){var _2=this.body.getTableZIndex();_1.setZIndex(_2+100)}
if(this.rollUnderCanvas==_1){var _2=this.body.getTableZIndex();_1.setZIndex(_2-50)}}
,isc.A.$477=function isc_ListGrid__handleEmbeddedComponentResize(_1,_2,_3,_4){if(this.frozenBody!=null&&_4!=null&&_4!=0){var _5=(_1==this.frozenBody)?this.body:this.frozenBody;_5.markForRedraw("Embedded component requires row resizing")}}
,isc.A.getButtonProperties=function isc_ListGrid_getButtonProperties(){var _1={align:null};if(this.headerTitleStyle!=null)_1.titleStyle=this.headerTitleStyle;if(this.headerBaseStyle!=null)_1.baseStyle=this.headerBaseStyle;if(this.headerButtonSrc!=null)_1.src=this.headerButtonSrc;if(this.frozenHeaderBaseStyle!=null)
_1.frozenBaseStyle=this.frozenHeaderBaseStyle;if(this.frozenHeaderTitleStyle!=null)
_1.frozenTitleStyle=this.frozenHeaderTitleStyle;if(this.frozenHeaderButtonSrc!=null)
_1.frozenSrc=this.frozenHeaderButtonSrc;var _2=isc.addProperties({},this.headerButtonDefaults,_1,this.headerButtonProperties);return _2}
,isc.A.getHeaderButtonProperties=function isc_ListGrid_getHeaderButtonProperties(_1){var _2=this.getButtonProperties();if(this.buttonTitleFunction==null){this.buttonTitleFunction=new Function("return this.parentElement.grid.getHeaderButtonTitle(this)")}
isc.addProperties(_2,{defaultRadioGroup:this.getID()+"$57y",getActionType:function(_9,_10,_11,_12){var _3=this.parentElement,_4;if(_3)_4=_3.grid;if(_4&&isc.isA.ListGrid(_4)){var _5=_4.fields[_3.getButtonNumber(this)];var _6=(_4.canSort!=false);if(_6&&_5!=null)_6=(_4.$50j(_5)!=false);if(_6)return isc.Button.RADIO}
return this.invokeSuper(null,"getActionType",_9,_10,_11,_12)},getTitle:this.buttonTitleFunction,mouseOver:function(){var _4=this.parentElement.grid;if(_4.shouldShowHeaderMenuButton(this)){var _7=_4.getHeaderMenuButton(this);this.addPeer(_7);_7.addProperties({dragTarget:this});if(this.zIndex>_7.zIndex)_7.bringToFront();if(!_7.isVisible()){_7.show()}}
return this.Super("mouseOver",arguments)},mouseOut:function(){var _4=this.parentElement.grid,_8=_4.headerMenuButton;if(_8&&_8.isVisible()&&_8.masterElement==this&&isc.EH.getTarget()!=_8)
{_4.headerMenuButton.hide()}
return this.Super("mouseOut",arguments)}},_1);return _2}
,isc.A.makeHeader=function isc_ListGrid_makeHeader(){if(this.header!=null)return;if(this.headerSpans){this.spanMap={};for(var i=0;i<this.headerSpans.length;i++){var _2=this.headerSpans[i];for(var j=0;j<_2.fields.length;j++){this.spanMap[_2.fields[j]]=_2}}}
var _4=this.header=this.makeHeaderForFields(this.normalFields||this.fields);_4.locatorParent=this;if(this.sorter==null)this.makeCornerSortButton();this.headers=[_4];if(this.frozenFields){var _5=this.frozenHeader=this.makeHeaderForFields(this.frozenFields,"visible",this.getID()+"$57z");if(this.freezeLeft()){this.headers.unshift(this.frozenHeader)}else{this.headers.add(this.frozenHeader)}
var _6=this.headers.duplicate();this.headerLayout=this.createAutoChild("headerLayout",{autoDraw:false,overflow:"hidden",height:this.headerHeight,members:_6},isc.HLayout)}
var _7=this.headerLayout||this.header;this.observe(_7,"moved","observer.updateSorter()");this.observe(_7,"resized","observer.updateSorter()")}
,isc.A.updateSorter=function isc_ListGrid_updateSorter(){if(!this.sorter)return;if(this.$v3||this.$u9){this.delayCall("updateSorter");return}
var _1=(this.header||this.headerLayout)&&this.$570();if(_1){this.sorter.moveTo(this.getSorterLeft(),this.getSorterTop())}else{this.sorter.moveTo(0,0)}
this.sorter.setVisibility(_1?isc.Canvas.INHERIT:isc.Canvas.HIDDEN)}
,isc.A.makeHeaderForFields=function isc_ListGrid_makeHeaderForFields(_1,_2,_3){var _4=this.createHeader({ID:_3,grid:this,buttons:_1.duplicate(),reorderOnDrop:false,height:null,minMemberSize:this.minFieldWidth,tabWithinToolbar:false,overflow:_2||"hidden",_redrawWithParent:!this.fixedFieldWidths})
this.observe(_4,"scrollTo","observer.headerScrolled()");return _4}
,isc.A.createHeader=function isc_ListGrid_createHeader(_1){var _2=this;var _3=this.canTabToHeader||(this.canTabToHeader==null&&isc.screenReader);var _4=isc.addProperties({makeButton:function(_6,_26,_27,_28,_29){_6=isc.addProperties({},_6);if(_6.headerTitleStyle)_6.titleStyle=_6.headerTitleStyle;else if(_6.frozen&&this.buttonProperties.frozenTitleStyle)
_6.titleStyle=_6.frozenTitleStyle;if(_6.frozen&&this.buttonProperties.frozenSrc)
_6.src=this.buttonProperties.frozenSrc;_6.showIf=null;_6.grid=_2;if(_2.shouldAutoFitField(_6)){var _5=_2.getAutoFitWidthApproach(_6);if(_5=="title"||_5=="both"){_6.overflow="visible";_6.resized=function(){if(this.isDrawn()&&this.grid){this.grid.headerButtonResized(this)}}}}
if(_6.$521){_6.width=_6.$521}else if(_6.width==null&&_2.shouldAutoFitField(_6)){_6.width=this.minFieldWidth||1}
_6.sizeImageToFitOverflow=true;var _6=this.Super("makeButton",[_6,_26,_27,_28,_29]);var _7;if(_6.headerBaseStyle){_6.baseStyle=_6.headerBaseStyle;_7=true}else if(!_7){if(_6.frozen&&this.buttonProperties.frozenBaseStyle)
_6.setBaseStyle(this.buttonProperties.frozenBaseStyle);else if(this.buttonProperties.baseStyle!=null)
_6.setBaseStyle(this.buttonProperties.baseStyle)}
return _6},buttonProperties:this.getHeaderButtonProperties(),border:this.headerBorder,styleName:this.headerBarStyle,backgroundColor:this.headerBackgroundColor,backgroundImage:this.headerBackgroundImage,canResizeItems:this.canResizeFields,canReorderItems:this.canReorderFields,dontObserve:false,childVisibilityChanged:function(){this.Super("childVisibilityChanged",arguments);this.$571()},$10r:function(){this.Super("$10r",arguments);this.$571()},spannedFields:Array.create({sortUnique:true}),addSpan:function(_18,_26){if(!this.$572)this.$572=[];this.$572.add(_18);this.spannedFields.addList(_26);if(this.isDrawn()){this.$573(_18);this.addChild(_18)}},$571:function(){if(!this.$572||!this.isDrawn())return;var _8=this.instantRelayout;this.instantRelayout=false;for(var i=0;i<this.$572.length;i++)this.$573(this.$572[i]);this.instantRelayout=_8},$574:function(_18){if(_18.$575!=null){return _18.$575}
var _10=[],_11=_18.fields.length,_12=true;for(var i=0;i<this.members.length;i++){if(_18.$576[this.members[i].name]==true){_12=false;_10[_10.length]=this.members[i];_11--}
if(_11==0)break}
_18.$575=_10;return _12?null:_10},$573:function(_18){if(!this.isDrawn()){return}
if(_18.$577){_18.setHeight(Math.floor(this.getHeight()/2))}
var _13=this.$574(_18);if(!_13){if(this.isVisible()){_18.hide()}
return}
var _14,_15=0;for(var i=0;i<_13.length;i++){var _16=_13[i];_16.canReorder=false;_16.layoutAlign="bottom";if(_16.visibility!=isc.Canvas.HIDDEN){_15+=_16.getVisibleWidth();if(!_14||this.isRTL())_14=_16}}
if(_14){_18.setVisibility(isc.Canvas.INHERIT);_18.setLeft(_14.getLeft());_18.setWidth(_15)}},$578:function(){if(!this.$572)return;this.$571();var _17=this.getVisibleHeight();for(var i=0;i<this.$572.length;i++){var _18=this.$572[i];this.addChild(_18);var _13=this.$574(_18);if(!_13)continue;for(var j=0;j<_13.length;j++){var _20=_13[j],_21=_20.getVisibleHeight(),_22=_18.getVisibleHeight();if(_22+_21>_17)_20.setHeight(_21-_22)}
_18.bringToFront()}},removeButtons:function(_26){if(_2.headerSpans)_2.$579(_26,this);this.Super("removeButtons",arguments)},addButtons:function(_26,_27){this.Super("addButtons",arguments);if(_2.headerSpans)_2.$58a(_26,this);this.$578()},draw:function(){if(this.$572&&_2.unspannedHeaderVAlign){for(var i=0;i<this.buttons.length;i++){var _6=this.buttons[i];if(!this.spannedFields.contains(_6.name))_6.valign=_2.unspannedHeaderVAlign}}
this.Super("draw",arguments);this.$578()},getStandardChildLocator:function(_26){var _23=_26.masterIndex,_2=this.grid;if(_23!=null&&_2!=null){var _24=_2.getFieldName(_23);return isc.AutoTest.createLocatorFallbackPath("headerButton",{fieldName:_24})}
return this.Super("getStandardChildLocator",_26)},getChildFromLocatorSubstring:function(_26){if(isc.isA.Canvas(this[_26]))return this[_26];var _25=isc.AutoTest.parseLocatorFallbackPath(_26);if(this.grid&&_25&&_25.name=="headerButton"){var _24=_25.config.fieldName,_23=this.grid.getFieldNum(_24),_20=this.grid.getFieldHeader(_23);if(_23==-1||_20==null){this.logWarn("fieldName:"+_24+", gave fieldNum:"+_23+" unable to find header button for this field.");return}
if(_20!=this){this.logWarn("AutoTest locator string specified this header for field "+_24+" but that header button is contained in header "+_20+". This can happen if headers have been frozen / "+"unfrozen since the locator string was created. Redirecting to that widget.");return _20.getChildFromLocatorSubstring(_26)}
return this.members[this.grid.getLocalFieldNum(_23)]}
return this.Super("getChildFromLocatorSubstring",arguments)}},_1);if(!_3){_4.tabIndex=-1}
if(this.headerButtonConstructor!=null)
_4.buttonConstructor=this.headerButtonConstructor;var _20=this.createAutoChild("header",_4,isc.Toolbar);if(_20.canReorderItems&&!_20.dontObserve){this.observe(_20,"itemDragReordered","observer.headerDragReordered(itemNum,newPosition,this)")}
if(_20.canResizeItems&&!_20.dontObserve)this.$58b(_20);if(this.headerSpans)this.$58a(_1.buttons,_20);return _20}
,isc.A.headerButtonResized=function isc_ListGrid_headerButtonResized(_1){this.$49c("header button resized")}
,isc.A.$58a=function isc_ListGrid__addHeaderSpans(_1,_2){var _3=[],_4=_1.getProperty(this.fieldIdProperty);for(var i=0;i<this.headerSpans.length;i++){var _6=this.headerSpans[i];if(_4.intersect(_6.fields).length){_3.add(_6)}}
for(var i=0;i<_3.length;i++){var _6=_3[i];var _7=this,_8=_6.liveObject;if(_8==null||_8.destroyed||_8.destroying||_8.$wa){var _9=isc.addProperties(this.getButtonProperties(),{_constructor:_2.buttonConstructor,height:this.headerSpanHeight,align:"center",valign:this.headerSpanVAlign,showRollOver:false,showContextMenu:function(){return _7.headerSpanContextClick(this)}},_6);_8=_6.liveObject=this.createAutoChild("headerSpan",_9);_8.$577=this.headerSpanHeight==null;if(_8.$577){var _10=_6.height||(this.headerSpanProperties?this.headerSpanProperties.height:null)||(this.headerSpanDefaults?this.headerSpanDefaults:null);if(_10!=null)_8.$577=false}}
_8.$576={};for(var _11=0;_11<_6.fields.length;_11++){_8.$576[_6.fields[_11]]=true}
_8.$575=null;_2.addSpan(_8,_8.fields)}}
,isc.A.$579=function isc_ListGrid__removeHeaderSpans(_1,_2){var _3=[],_4=_1.getProperty(this.fieldIdProperty);for(var i=0;i<this.headerSpans.length;i++){var _6=this.headerSpans[i];if(_4.intersect(_6.fields).length){var _7=_6.liveObject;if(_7)_7.$575=null}}}
,isc.A.dirtyHeader=function isc_ListGrid_dirtyHeader(){if(this.header)this.header.markForRedraw()}
,isc.A.updateHeader=function isc_ListGrid_updateHeader(){var _1=this.frozenHeader?this.headerLayout:this.header;if(_1){_1.markForDestroy();_1.clear();if(this.showHeader){var _2=[this.frozenHeader,this.header];for(var i=0;i<_2.length;i++){if(_2[i]==null)continue;if(_2[i].$572!=null){for(var _4=0;_4<_2[i].$572.length;_4++){var _5=_2[i].$572[_4];if(_5)_5.deparent()}}}}
_1.clearIDs()}
this.header=this.frozenHeader=this.headerLayout=null;if(this.headerMenuButton&&this.headerMenuButton.destroyed){this.headerMenuButton=null}
var _6=this.showHeader,_7;if(_6){_7=this.gridComponents.indexOf(this.$51m);if(_7==-1){_6=false;this.logWarn("showHeader set to true, but header not included in grid header components."+" The header will not be shown.")}}
if(_6){this.makeHeader();if(this.isDrawn()){this.updateGridComponents()}}else if(this.sorter){this.sorter.hide()}}
,isc.A.setHeaderHeight=function isc_ListGrid_setHeaderHeight(_1){var _2=this.headerHeight;if(_2==_1)return;this.headerHeight=_1;if(this.showHeader==false)return;if(!this.header&&_1>0){var _3=this.gridComponents.indexOf("header");if(_3==-1){return}
if(this.isDrawn()){this.updateGridComponents()}}else{if(_2==0)this.updateHeader();this.layoutChildren(this.$52f)}}
,isc.A.setShowHeader=function isc_ListGrid_setShowHeader(_1){if(_1==this.showHeader)return;this.showHeader=_1;this.updateHeader();this.layoutChildren(this.$52g)}
,isc.A.shouldShowHeaderMenuButton=function isc_ListGrid_shouldShowHeaderMenuButton(_1){var _2;if(_1.showDefaultContextMenu==false){_2=false}else if(_1.showHeaderContextMenuButton!=null){_2=_1.showHeaderContextMenuButton}else{_2=this.showHeaderMenuButton}
if(this.showHeaderContextMenu&&_2){var _3=this.getHeaderContextMenuItems(_1.masterIndex);return(_3&&_3.length>0)}
return false}
,isc.A.getHeaderMenuButton=function isc_ListGrid_getHeaderMenuButton(_1){if(!this.headerMenuButton||this.headerMenuButton.destroyed){var _2={};if(this.headerMenuButtonHeight)_2.height=this.headerMenuButtonHeight;if(this.headerMenuButtonWidth)_2.width=this.headerMenuButtonWidth;if(this.headerMenuButtonIcon)_2.icon=this.headerMenuButtonIcon;if(this.headerMenuButtonIconHeight){_2.iconHeight=this.headerMenuButtonIconHeight}
if(this.headerMenuButtonIconWidth){_2.iconWidth=this.headerMenuButtonIconWidth}
if(this.headerMenuButtonBaseStyle){_2.baseStyle=this.headerMenuButtonBaseStyle}
if(this.headerMenuButtonTitleStyle){_2.titleStyle=this.headerMenuButtonTitleStyle}
if(this.headerMenuButtonSrc)_2.src=this.headerMenuButtonSrc;this.createAutoChild("headerMenuButton",_2,"Button",true)}
this.headerMenuButton.canDragResize=(_1&&_1.canDragResize!=null?_1.canDragResize&&this.canResizeFields:this.canResizeFields);this.headerMenuButton.addProperties({doubleClick:function(){this.creator.headerMenuButtonDoubleClick()}});return this.headerMenuButton}
,isc.A.headerMenuButtonClick=function isc_ListGrid_headerMenuButtonClick(){var _1=this.header,_2=_1.containsEvent()?_1.getMouseOverButtonIndex():-1;if(_2<0&&this.frozenHeader){_1=this.frozenHeader;_2=_1.getMouseOverButtonIndex()}
if(_2==-1)return;var _3=_1.getMember(_2);var _4=_3.getPageBottom(),_5=this.$531?this.$531.getVisibleWidth():isc.Menu.getPrototype().defaultWidth,_6=Math.max(_3.getPageLeft(),(_3.getPageRight()-_5));this.displayHeaderContextMenu(_3,[_6,_4]);this.headerMenuButton.bringToFront()}
,isc.A.headerMenuButtonDoubleClick=function isc_ListGrid_headerMenuButtonDoubleClick(){var _1=this.header,_2=_1.getMouseOverButtonIndex();if(_2==-1&&this.frozenHeader){_1=this.frozenHeader;_2=_1.getMouseOverButtonIndex()}
if(_2==-1)return;var _3=_1.getMember(_2);this.headerDoubleClick(_2,_1);isc.Menu.hideAllMenus()}
,isc.A.getHeaderButtonTitle=function isc_ListGrid_getHeaderButtonTitle(_1){var _2=_1;if(isc.isA.Object(_1))_2=_1.masterIndex;var _3=this.fields[_2];if(!_3)return"";var _4=_3[this.fieldIdProperty],_5=this.isSortField(_4),_6=(_5)&&(!this.showSortArrow||this.showSortArrow==isc.ListGrid.FIELD||this.showSortArrow==isc.ListGrid.BOTH),_7=(this.showSortNumerals==false?false:_5&&this.getSortFieldCount()>1),_8=this.getFieldTitle(_2);var _9;if(_7){var _10=this.getSortSpecifier(_4);_9=_10.sortIndex}
var _11=_8+(_6?"&nbsp;"+this.getSortArrowImage(_2):"")+(_7?"&nbsp;"+this.getSortNumeralHTML(_4,_9):"");if(this.leaveHeaderMenuButtonSpace&&this.shouldShowHeaderMenuButton(_1)){_11+=isc.Canvas.spacerHTML(this.headerMenuButtonWidth,1)}
return _3.showTitle!=false?_11:isc.nbsp}
,isc.A.$492=function isc_ListGrid__setCheckboxHeaderState(_1){var _2=this.getCheckboxFieldPosition(),_3=this.fields[_2];if(!this.isCheckboxField(_3))return;var _4=_1?(this.checkboxFieldTrueImage||this.booleanTrueImage):(this.checkboxFieldFalseImage||this.booleanFalseImage),_5=(this.canSelectAll==false||this.selectionType=="single")?"&nbsp;":this.getValueIconHTML(_4,_3);this.setFieldTitle(_2,_5);_3.$58c=_1}
,isc.A.$50h=function isc_ListGrid__headerClick(_1,_2){var _3=_2.getMember(_1).masterIndex;return this.headerClick(_3,_2)}
,isc.A.headerClick=function isc_ListGrid_headerClick(_1,_2){if(this.onHeaderClick&&(this.onHeaderClick(_1,_2)==false)){return}
var _3=this.fields[_1];if(this.isCheckboxField(_3)&&this.selectionType!="single"&&this.canSelectAll!=false){if(_3.$58c){this.deselectAllRecords();this.$492(false)}else{this.selectAllRecords();if(!isc.ResultSet||!isc.isA.ResultSet(this.data)||this.data.allMatchingRowsCached())
{this.$492(true)}}
return false}
if(this.canAutoFitFields&&this.headerAutoFitEvent=="click"){this.autoFitField(_1)}
if(this.canSort==false||!_3||this.$50j(_3)==false)return false;var _4=_3[this.fieldIdProperty],_5=this.isSortField(_4)?this.getSortSpecifier(_4):null,_6=isc.EventHandler,_7=_6.getKey(),_8=_6.shiftKeyDown(),_9=this.$52o,_10=_3.sortDirection;if(_8&&this.canMultiSort){if(this.isSortField(_4)){if(_10==_9||this.canUnsort==false){this.toggleSort(_4)}else{this.toggleSort(_4,"unsort")}}else{this.$58d(_4)}}else{if(this.isSortField(_4)&&this.getSortFieldCount()==1){if(_10==_9||this.canUnsort==false){this.toggleSort(_4)}else{this.toggleSort(_4,"unsort")}}else{this.setSort([{property:_4,direction:_9?"ascending":"descending"}])}}
return false}
,isc.A.$50i=function isc_ListGrid__headerDoubleClick(_1,_2){var _3=_2.getMember(_1).masterIndex;return this.headerDoubleClick(_3,_2)}
,isc.A.headerDoubleClick=function isc_ListGrid_headerDoubleClick(_1,_2){var _3=this.fields[_1];if(this.isCheckboxField(_3))return;if(this.canAutoFitFields&&this.headerAutoFitEvent=="doubleClick"){this.autoFitField(_1)}
return false}
,isc.A.fieldDragResizeStart=function isc_ListGrid_fieldDragResizeStart(){this.makeDragLine();this._dragLine.resizeTo(2,this.getVisibleHeight()-(this.body.hscrollOn?this.body.getScrollbarSize():0));var _1=this.ns.EH.dragTarget;var _2=this.getField(_1.masterIndex);if(_2)_2.autoFitWidth=false;_1.setWidth(_1.getVisibleWidth());_1.setOverflow("hidden")}
,isc.A.fieldDragResizeMove=function isc_ListGrid_fieldDragResizeMove(_1){var _2=this.ns.EH,_3=_2.dragTarget;var _4=this.header.$13s;this.showDragLineForField(_4);if(this.resizeFieldsInRealTime){var _5=_2.dragResizeWidth;this.$45p[_4]=_5;this.$530=true;this.setBodyFieldWidths(this.$45p);this.body.useQuickDrawAheadRatio=true;this.body.redraw("fieldDragResize");delete this.body.useQuickDrawAheadRatio;delete this.body.$530;var _6=this.body.getScrollLeft();if(_6>0)this.header.scrollTo(_6,null,"headerResize")}}
,isc.A.fieldDragResizeStop=function isc_ListGrid_fieldDragResizeStop(){this.hideDragLine()}
,isc.A.headerDragReordered=function isc_ListGrid_headerDragReordered(_1,_2,_3){var _4=_3.getMember(_1).masterIndex,_5=_3.getMember(_2).masterIndex;this.reorderField(_4,_5)}
,isc.A.reorderField=function isc_ListGrid_reorderField(_1,_2){this.reorderFields(_1,_1+1,_2-_1)}
,isc.A.reorderFields=function isc_ListGrid_reorderFields(_1,_2,_3){if(this.showFilterEditor&&this.filterEditor!=null){this.filterEditor.reorderFields(_1,_2,_3)}
if(this.summaryRow&&this.showGridSummary){this.summaryRow.reorderFields(_1,_2,_3)}
var _4=this.fields[_1],_5=this.fields[_2-1],_6=_1+_3,_7=this.fields[_6];var _8=this.$4h(),_9=this.fields[_8];if(this.$45p!=null)this.$45p.slideRange(_1,_2,_1+_3);if(this.completeFields==null)this.setFields(this.fields);if(this.completeFields.contains(_7)){var _10=this.completeFields.indexOf(_4),_11=this.completeFields.indexOf(_5)+1,_12=this.completeFields.indexOf(_7);this.completeFields.slideRange(_10,_11,_12)}
this.deriveVisibleFields();if(_8!=null){_8=this.fields.indexOf(_9);if(_8==-1)_8=null;this.$53e(_8)}
var _13;if(!this.frozenFields){_13=this.getFieldHeader(_1);if(_13){_13.reorderItems(this.getLocalFieldNum(_1),this.getLocalFieldNum(_2),this.getLocalFieldNum(_1+_3))}}else{this.rebuildForFreeze();_13=this.getFieldHeader(_1)}
if(_8!=null&&_13)
_13.selectButton(this.getLocalFieldNum(_8));var _14=this.$49j;if(_14){var _15=this.$6l,_16=this.getEditRow(),_17=this.getEditField(),_18=[]
for(var i=0;i<_15.items.length;i++){var _20=_15.items[i];_20.colNum=this.fields.findIndex(this.fieldIdProperty,_20.getFieldName());for(var j=0;j<=_18.length;j++){if(_18[j]==null||_18[j].colNum>_20.colNum){_18.addAt(_20,j);break}}}
_15.items.setArray(_18);this.$504=this.fields.indexOf(_17)}
var _22=this.body;if(this.frozenFields&&_1<this.frozenFields.length){_22=this.frozenBody}
if(_22){this.body.fields=this.normalFields||this.fields;if(this.frozenBody)this.frozenBody.fields=this.frozenFields;this.setBodyFieldWidths(this.$45p);if(_22.$4p!=null){this.$53h(_22)}
if(!_22.isDirty())this.$52w("reorderFields")}
this.handleFieldStateChanged()}
,isc.A.remapEditFieldsForFreeze=function isc_ListGrid_remapEditFieldsForFreeze(){if(this.$49j){var _1=this.getEditForm(),_2=_1.getItems();for(var i=0;i<_2.length;i++){var _4=_2[i];_4.colNum=this.fields.findIndex(this.fieldIdProperty,_4.getFieldName());var _5=this.fields[_4.colNum];if(!this.$52x&&_5.frozen)_4.containerWidget=this.frozenBody;else _4.containerWidget=this.body}}}
,isc.A.headerDragResized=function isc_ListGrid_headerDragResized(_1,_2,_3){var _4=_3.getMember(_1).masterIndex;this.resizeField(_4,_2,true)}
,isc.A.setCanResizeFields=function isc_ListGrid_setCanResizeFields(_1){if(this.canResizeFields==_1)return;this.canResizeFields=_1;if(this.header)this.header.setCanResizeItems(_1);if(this.frozenHeader)this.frozenHeader.setCanResizeItems(_1);if(this.headerMenuButton)this.headerMenuButton.canDragResize=_1;if(_1){if(this.header&&!this.header.dontObserve)this.$58b(this.header);if(this.frozenHeader&&!this.frozenHeader.dontObserve)
this.$58b(this.frozenHeader)}else{if(this.header)this.$58e(this.header);if(this.frozenHeader)this.$58e(this.frozenHeader)}}
,isc.A.$58b=function isc_ListGrid__observeHeaderResize(_1){this.observe(_1,"itemDragResized","observer.headerDragResized(itemNum,newSize,this)");this.observe(_1,"dragResizeMemberStart","observer.fieldDragResizeStart()");this.observe(_1,"dragResizeMemberMove","observer.fieldDragResizeMove()");this.observe(_1,"dragResizeMemberStop","observer.fieldDragResizeStop()")}
,isc.A.$58e=function isc_ListGrid__ignoreHeaderResize(_1){if(this.isObserving(_1,"itemDragResized"))
this.ignore(_1,"itemDragResized");if(this.isObserving(_1,"dragResizeMemberStart"))
this.ignore(_1,"dragResizeMemberStart");if(this.isObserving(_1,"dragResizeMemberMove"))
this.ignore(_1,"dragResizeMemberMove");if(this.isObserving(_1,"dragResizeMemberStop"))
this.ignore(_1,"dragResizeMemberStop")}
,isc.A.resizeField=function isc_ListGrid_resizeField(_1,_2,_3){if(!isc.isA.Number(_1))_1=this.getFieldNum(_1);if(_1==-1)return;if(this.header&&this.header.isDrawn()){var _4=this.getFieldHeader(_1),_5=this.getLocalFieldNum(_1);_4.getMember(_5).setWidth(_2)}
if(_3==null||_3){this.fields[_1].width=_2;this.fields[_1].autoFitWidth=false}
if(!this.isDrawn())return;this.$45p[_1]=_2;this.$530=true;this.setBodyFieldWidths(this.$45p);if(this.body.isDirty())this.body.redraw("setting body field widths");delete this.$530;if(this.showFilterEditor&&this.filterEditor){this.filterEditor.resizeField(_1,_2,_3)}
if(this.summaryRow&&this.showGridSummary){this.summaryRow.resizeField(_1,_2,_3)}
this.handleFieldStateChanged()}
,isc.A.$570=function isc_ListGrid__showSortButton(){var _1=this.showSortArrow;if(_1!=null){return(_1==isc.ListGrid.CORNER||_1==isc.ListGrid.BOTH)}
return this.$53w()}
,isc.A.$53w=function isc_ListGrid__shouldLeaveScrollbarGap(){if(this.leaveScrollbarGap)return true;if(!this.body||this.isEmpty())return false;if(this.body.vscrollOn)return true;var _1=this.body,_2=this.headerHeight,_3=this.getInnerHeight()-this.body.getVMarginBorder()-
(this.showHeader?_2:0);if(this.autoFitData==isc.Canvas.VERTICAL||this.autoFitData==isc.Canvas.BOTH){_3=(this.getTotalRows()*this.cellHeight);var _4=this.getAutoFitMaxBodyHeight();if(_4&&_4<_3){_3=_4}
if(this.autoFitMaxRows&&(this.autoFitMaxRows*this.cellHeight)>_3){_3=(this.autoFitMaxRows*this.cellHeight)}}
if(_3<=0)return false;return this.predictScrollbarGap&&this.bodyOverflow==isc.Canvas.AUTO&&(this.getTotalRows()*this.cellHeight>_3)}
,isc.A.getSorterTop=function isc_ListGrid_getSorterTop(){return(this.headerLayout||this.header).getTop()}
,isc.A.getSorterLeft=function isc_ListGrid_getSorterLeft(){if(this.isRTL()){return this.getLeftMargin()+this.getLeftBorderSize()+this.getLeftPadding();}else{var _1=(this.headerLayout||this.header);return _1.getWidth()+_1.getLeft()}}
,isc.A.makeCornerSortButton=function isc_ListGrid_makeCornerSortButton(){this.sorter=this.createAutoChild("sorter",isc.addProperties(this.getButtonProperties(),this.sorterDefaults,{ID:this.getID()+"_sorter",width:this.getScrollbarSize(),height:this.headerHeight,backgroundColor:this.headerBackgroundColor,imgDir:this.widgetImgDir,visibility:(this.$570()?isc.Canvas.INHERIT:isc.Canvas.HIDDEN)}));this.addChild(this.sorter,"sorter",false)}
,isc.A.sorterContextClick=function isc_ListGrid_sorterContextClick(){var _1=this.showHeaderContextMenu;if(this.showCornerContextMenu!=null)_1=this.showCornerContextMenu;if(_1)return this.displayHeaderContextMenu(this.sorter)}
,isc.A.headerBarContextClick=function isc_ListGrid_headerBarContextClick(_1){var _2=this.showHeaderContextMenu;if(this.showCornerContextMenu!=null)_2=this.showCornerContextMenu;if(_2){return this.displayHeaderContextMenu(_1.getMember(_1.getMouseOverButtonIndex()))}}
,isc.A.headerSpanContextClick=function isc_ListGrid_headerSpanContextClick(_1){var _2=this.getHeaderSpanContextMenuItems(_1);if(!_2||_2.length==0)return false;if(!this.$532){this.$532=this.getMenuConstructor().create({items:_2})}else{this.$532.setItems(_2)}
this.$532.showContextMenu();return false}
,isc.A.displayHeaderContextMenu=function isc_ListGrid_displayHeaderContextMenu(_1,_2){if(!_1)return;var _3=this.fields[_1.masterIndex];if(_3&&_3.showDefaultContextMenu==false)return false;var _4=this.getHeaderContextMenuItems(_1.masterIndex);if(_4.length==0)return;if(!this.$531)this.$531=this.getHeaderContextMenu(_1);this.$531.setData(_4);if(_2!=null&&_2.length>0){this.$531.moveTo(0,0);this.$531.setVisibility("hidden");if(!this.$531.isDrawn())this.$531.draw();else this.$531.redraw();this.$531.placeNear(_2[0],_2[1]);this.$531.show();return false}else{return this.$531.showContextMenu(_1)}}
,isc.A.getToggleFreezeText=function isc_ListGrid_getToggleFreezeText(_1){var _2={field:_1,viewer:this,title:this.getSummaryTitle(_1)}
return(!this.$52x&&_1.frozen)?this.unfreezeFieldText.evalDynamicString(this,_2):this.freezeFieldText.evalDynamicString(this,_2)}
,isc.A.getGroupByText=function isc_ListGrid_getGroupByText(_1){var _2={field:_1,title:this.getSummaryTitle(_1),viewer:this}
return this.groupByText.evalDynamicString(this,_2)}
,isc.A.$58f=function isc_ListGrid__shouldGroupByField(_1){var _1=this.getField(_1);return!!(_1&&this.$58g(_1)&&this.data.getLength()<=this.groupByMaxRecords)}
,isc.A.$58g=function isc_ListGrid__canGroupByField(_1){var _1=this.getField(_1);return!!(_1&&((this.canGroupBy==true&&_1.canGroupBy!=false)||(this.canGroupBy!=false&&_1.canGroupBy==true)))}
,isc.A.getHeaderSpanContextMenuItems=function isc_ListGrid_getHeaderSpanContextMenuItems(_1){if(!this.showTreeColumnPicker&&!this.showHeaderSpanContextMenu)return false;var _2=[{title:this.fieldVisibilitySubmenuTitle,submenu:this.getColumnPickerItems(),icon:"[SKINIMG]actions/column_preferences.png"}];var _3=this,_4;for(var i=0;i<_1.fields.length;i++){var _6=_1.fields[i],_7=this.getField(_6);if(_7&&!this.$52x&&_7.frozen){_4=true;break}}
if(this.canFreezeFields&&this.fields.length>1&&(_4||this.frozenFields==null||this.normalFields.length>_1.fields.length))
{_2.add({isSeparator:true});_2.add({title:(_4?"Unfreeze ":"Freeze ")+_1.title,grid:this,spanFields:_1.fields,frozen:_4,icon:(_4?"[SKINIMG]actions/unfreeze.png":"[SKINIMG]actions/freezeLeft.png"),click:function(){for(var i=0;i<this.spanFields.length;i++){_3.completeFields.find(_3.fieldIdProperty,this.spanFields[i]).frozen=!this.frozen}
_3.rebuildForFreeze()}})}
return _2}
,isc.A.shouldShowColumnPicker=function isc_ListGrid_shouldShowColumnPicker(_1){if(_1.treeField||_1.canHide===false)return false;var _2=this.getSummaryTitle(_1);if(_2==null||isc.isAn.emptyString(_2))return false;return true}
,isc.A.getColumnPickerItems=function isc_ListGrid_getColumnPickerItems(){var _1=[],_2=this;for(var i=0;i<this.completeFields.length;i++){var _4=this.completeFields[i];if(!this.shouldShowColumnPicker(_4))continue;var _5=this.getSummaryTitle(_4);var _6=this.spanMap?this.spanMap[_4.name]:null;if(this.showTreeColumnPicker&&_6){if(!_1.find("spanConfig",_6)){_1.add({title:_6.title,autoDismiss:false,spanConfig:_6,click:function(_16,_9,_17){var _7=this.spanConfig.fields;var _8=this.anySpannedFieldsVisible();if(_8)_2.hideFields(_7)
else _2.showFields(_7);_17.body.delayCall("markForRedraw")},anySpannedFieldsVisible:function(){var _7=this.spanConfig.fields;for(var i=0;i<_7.length;i++){if(_2.fieldIsVisible(_7[i]))return true}
return false},enableIf:function(_16,_17,_9){var _6=this.spanConfig;for(var i=0;i<_17.data.length;i++){var _9=_17.data[i];if(_2.fieldIsVisible(_9.fieldName)&&!_6.fields.contains(_9.fieldName))return true}
return false},checkIf:function(_16,_17,_9){return this.anySpannedFieldsVisible()}})}
_5="&nbsp;&nbsp;&nbsp;&nbsp;"+_5}
_1.add({title:_5,fieldName:_4.name,prompt:_4.prompt,autoDismiss:false,checkIf:function(_16,_17,_9){return _2.fieldIsVisible(this.fieldName)},enableIf:function(_16,_17,_9){return!(_2.fields.length==1&&_2.fieldIsVisible(this.fieldName))},click:function(_16,_9,_17){var _10=_17.$58h,_11=_10?_10.data.findIndex("groupItem",true):null,_12=_10?_10.data[_11]:null,_13=(_12&&_12.fieldName==_9.fieldName);if(_2.fieldIsVisible(this.fieldName)){var _14=_2.fields.length;if(_2.getCurrentCheckboxField()!=null)_14-=1;if(_14>1){_2.hideField(_9.fieldName)}
if(_14==1){var _15=_17.data.find("fieldName",_2.fields[0].name);_17.setItemEnabled(_15,false);this.$58i=_15}}else{_2.showField(_9.fieldName);if(this.$58i){_17.setItemEnabled(this.$58i,true);this.$58i=null}}
if(_13){_10.setItemEnabled(_11,_2.$58f(_12.fieldName))}
_17.body.delayCall("markForRedraw")}})}
return _1}
,isc.A.getColumnPickerMenu=function isc_ListGrid_getColumnPickerMenu(_1){var _2={canHover:true,showIcons:true,showHover:true,cellHoverHTML:function(_3){return _3.prompt},items:_1};return{title:this.fieldVisibilitySubmenuTitle,submenu:_2,icon:"[SKINIMG]actions/column_preferences.png"}}
,isc.A.getHeaderContextMenuItems=function isc_ListGrid_getHeaderContextMenuItems(_1){var _2=this.getField(_1);var _3=[],_4=false;var _5=this.canSort&&((_2&&this.$50j(_2)!=false)||(!_2&&this.$4h()!=null));if(_5||!_2){if(_2){var _6=_1!=null?_1:this.$4h();_3[0]={title:this.sortFieldAscendingText,icon:"[SKINIMG]actions/sort_ascending.png",click:"menu.doSort("+_6+", 'ascending')"};_3[1]={title:this.sortFieldDescendingText,icon:"[SKINIMG]actions/sort_descending.png",click:"menu.doSort("+_6+", 'descending')"};_4=true}
if(this.canMultiSort){_3.add({title:this.configureSortText,click:"menu.grid.askForSort();"});_4=true;if(!_2||this.isSortField(_2[this.fieldIdProperty])){_3.add({title:_2?this.clearSortFieldText:this.clearAllSortingText,field:_2,enableIf:function(_14,_15,_16){return(_2||(!_2&&_15.grid.getSortFieldCount()>0))},click:_2?"menu.doSort("+_6+", 'unsort')":"menu.grid.clearSort();"});_4=true}}}
var _7=this.canAutoFitFields&&_2;if(_7){_3.add({title:this.autoFitAllText,click:"menu.grid.autoFitFields()"});_3.add({title:this.autoFitFieldText,click:"menu.grid.autoFitField("+_1+",true);"})}
if(!_2&&this.showFilterEditor){if(_4)_3.add({isSeparator:true});_3.add({title:this.clearFilterText,click:"menu.grid.setFilterEditorCriteria(null); menu.grid.filterByEditor();"});_4=true}
if(this.canPickFields&&this.completeFields.length>1){var _8=this.getColumnPickerItems();if(_8.length>1){if(_4)_3.add({isSeparator:true});_3.add(this.getColumnPickerMenu(_8));_4=true}}
var _9=_2&&this.$58g(_2),_10=(this.canGroupBy!=false)&&_2&&_2.canGroupBy!=false&&this.isGrouped;if(_4&&(_10||_9)){_3.add({isSeparator:true})}
if(_9){var _11=(!_2.getGroupValue?_2.groupingModes?_2.groupingModes:(_2.$14v?_2.$14v.groupingModes:false):false);var _12=null;if(_11){_12=[];for(var _13 in _11){_12.add({title:_11[_13],groupType:_13,targetField:_2,fieldName:_2.name,prompt:_2.prompt,checked:(_2.groupingMode==_13),click:function(_14,_15,_16){this.targetField.groupingMode=this.groupType;_16.$58h.groupField(_15)}})}}
_3.add({groupItem:true,title:this.getGroupByText(_2),fieldName:_2.name,targetField:_2,prompt:_2.prompt,icon:"[SKINIMG]actions/groupby.png",click:function(_14,_15,_16){this.targetField.groupingMode=this.targetField.defaultGroupingMode||null;_16.groupField(_15)},enabled:this.$58f(_2),canSelectParent:true,submenu:_12});_4=true}
if(_10){_3.add({title:this.ungroupText,click:"menu.ungroup()",icon:"[SKINIMG]actions/ungroup.png"});_4=true}
if(_2){if(this.canFreezeFields&&this.fields.length>1&&(_2.frozen||(this.frozenFields==null||this.normalFields.length>1))&&!(this.spanMap&&this.spanMap[_2.name])&&_2.canFreeze!=false)
{if(_4)_3.add({isSeparator:true});_3.add({title:this.getToggleFreezeText(_2),grid:this,field:_2,icon:(_2.frozen?"[SKINIMG]actions/unfreeze.png":"[SKINIMG]actions/freezeLeft.png"),click:"item.grid.toggleFrozen(this.field,!this.field.frozen)"});_4=true}}
if(this.canAddFormulaFields){if(_4)_3.add({isSeparator:true});if(_2&&_2.userFormula){_3.add({title:this.editFormulaFieldText,grid:this,field:_2,click:"item.grid.editFormulaField(item.field)",icon:"[SKINIMG]ListGrid/formula_menuItem.png"});_3.add({title:this.removeFormulaFieldText,grid:this,field:_2,click:"item.grid.removeField(item.field.name)",icon:"[SKINIMG]ListGrid/formula_menuItem.png"})}
_3.add({title:this.addFormulaFieldText,grid:this,click:"item.grid.addFormulaField()",icon:"[SKINIMG]ListGrid/formula_menuItem.png"});_4=true}
if(this.canAddSummaryFields){if(_4)_3.add({isSeparator:true});if(_2&&_2.userSummary){_3.add({title:this.editSummaryFieldText,grid:this,field:_2,click:"item.grid.editSummaryField(item.field)",icon:"[SKINIMG]ListGrid/formula_menuItem.png"});_3.add({title:this.removeSummaryFieldText,grid:this,field:_2,click:"item.grid.removeField(item.field.name)",icon:"[SKINIMG]ListGrid/formula_menuItem.png"})}
_3.add({title:this.addSummaryFieldText,grid:this,click:"item.grid.addSummaryField()",icon:"[SKINIMG]ListGrid/formula_menuItem.png"})}
return _3}
);isc.evalBoundary;isc.B.push(isc.A.getHeaderContextMenu=function isc_ListGrid_getHeaderContextMenu(){return this.createAutoChild("headerContextMenu",{ID:this.getID()+"$531",grid:this})}
,isc.A.getSortArrowImage=function isc_ListGrid_getSortArrowImage(_1){var _2;if(_1==null||_1==_2)_1=this.$4h();if(this.canSort&&_1!=null){return this.imgHTML((Array.shouldSortAscending(this.getField(_1).sortDirection)?this.sortAscendingImage:this.sortDescendingImage),null,null,null,null,this.widgetImgDir)}else{return isc.Canvas.spacerHTML(1,1)}}
,isc.A.sorterClick=function isc_ListGrid_sorterClick(){if(!this.canSort)return false;var _1=this.$4h();if(_1!=null){var _2=this.getField(_1),_3=_2[this.fieldIdProperty],_4=this.$52o,_5=_2.sortDirection;if(this.isSortField(_3)){if(_4==_5||this.canUnsort==false){this.toggleSort(_3)}else{this.toggleSort(_3,"unsort")}}else{this.setSort({property:_3,direction:_4})}}else{this.sort(_1,(_1!=null?!Array.shouldSortAscending(this.getField(_1).sortDirection):null))}}
,isc.A.showDragLineForRecord=function isc_ListGrid_showDragLineForRecord(_1,_2){var _3=this.body;if(_1==null)_1=this.getEventRecordNum();var _4=_3.getVisibleRows()[1];if(_1==-2)_1=_4;if(this._dragLine&&!this.recordIsEnabled(_1)&&_1!=_4)return this._dragLine.hide();if(_2==null)_2=this.getReorderPosition(_1);var _5=this.getLeftBody();var _6=_5.getPageLeft()+(this.isRTL()&&_5.vscrollOn?_5.getScrollbarSize():0);if(_2==isc.ListGrid.BEFORE){this.showHDragLine(_6,_3.getRowPageTop(_1));this._dragLine.afterRow=_1-1}else if(_2==isc.ListGrid.AFTER){this.showHDragLine(_6,(_3.getRowPageTop(_1)+_3.getRowSize(_1)));this._dragLine.afterRow=_1}else{return this._dragLine.hide()}
if(this.recordDropMove){this.fireCallback("recordDropMove","viewer,recordNum,record,position",[this,_1,this.getRecord(_1),_2])}}
,isc.A.showHDragLine=function isc_ListGrid_showHDragLine(_1,_2){this.makeDragLine();this._dragLine.resizeTo(this.getViewportWidth()-(this.body.vscrollOn?this.body.getScrollbarSize():0),2);var _3=this.body.getPageTop(),_4=_3+this.body.getVisibleHeight();if(_2<_3)_2=_3;else if(_2>_4)_2=_4;this._dragLine.setPageRect(_1,_2);this._dragLine.show();this._dragLine.bringToFront()}
,isc.A.showDragLineForField=function isc_ListGrid_showDragLineForField(_1,_2){this.makeDragLine();if(_2==true){this._dragLine.resizeTo(2,this.headerHeight)}else{this._dragLine.resizeTo(2,this.getOuterViewportHeight())}
if(_1==null)_1=this.getEventFieldNum();if(_1<0){this._dragLine.hide();return}
var _3=this.ns.EH.dragTarget,_4=(!this.isRTL()?_3.getPageLeft()+this.ns.EH.dragResizeWidth:_3.getPageRight()-this.ns.EH.dragResizeWidth)-1;this._dragLine.setPageRect(_4,this.getPageTop()+(this.showFilterEditor?this.filterEditorHeight:0));this._dragLine.bringToFront();this._dragLine.show()}
,isc.A.unsort=function isc_ListGrid_unsort(){this.setSort(null)}
,isc.A.resort=function isc_ListGrid_resort(){if(this.$32)return this.setSort(isc.shallowClone(this.$32));var _1=this.$4h();if(_1!=null){this.sort(_1,this.sortDirection)}}
,isc.A.sort=function isc_ListGrid_sort(_1,_2){if(this.canSort==false)return false;if(isc.isA.String(_1))_1=this.getFieldNum(_1);var _3=this.$4h(),_4=(_3!=null?Array.shouldSortAscending(this.getField(_3).sortDirection):null);if(_1==null){if(_3!=null){_1=_3}else{for(var i=0;i<this.fields.length;i++){if(this.$50j(this.fields[i])!=false){_1=i;break}}}}
if(_1==null)return false;var _6=this.getField(_1);if(_6==null||this.$50j(_6)==false){return}
if(_2==null){_2=(_6.sortDirection!=null?_6.sortDirection:this.$52o)}
if(_3==_1&&_2==_6.sortDirection)return;var _7={property:_6[this.fieldIdProperty],direction:Array.shouldSortAscending(_2)?"ascending":"descending"};return this.setSort([_7])}
,isc.A.$4h=function isc_ListGrid__getSortFieldNum(){if(this.sortFieldNum!=null)return this.sortFieldNum;if(this.sortField!=null){var _1=this.getFieldNum(this.sortField);if(_1==-1)_1=null;this.sortFieldNum=_1;var _2=this.getFieldName(this.sortFieldNum);if(_2)this.sortField=_2;return _1}
return null}
,isc.A.$53e=function isc_ListGrid__setSortFieldNum(_1){this.sortFieldNum=_1;var _2=this.getFieldName(_1);this.sortField=_2}
,isc.A.getSortField=function isc_ListGrid_getSortField(){return this.sortField}
,isc.A.getUnderlyingField=function isc_ListGrid_getUnderlyingField(_1){if(!this.fields&&!this.completeFields&&!this.dataSource){this.logWarn("fields and completeFields are null and there is no DataSource");return null}
var _2=null;if(this.fields){_2=isc.Class.getArrayItem(_1,this.fields,this.fieldIdProperty)}
if(!_2&&this.completeFields){_2=isc.Class.getArrayItem(_1,this.completeFields,this.fieldIdProperty)}
if(!_2&&this.dataSource){if(!isc.isA.DataSource(this.dataSource))this.dataSource=this.getDataSource(this.dataSource);_2=this.dataSource.getField(_1)}
return _2}
,isc.A.toggleSort=function isc_ListGrid_toggleSort(_1,_2){var _3=this.getSpecifiedField(_1),_4=this.$32?isc.shallowClone(this.$32):[],_5=_4.find("property",_1);if(!_5&&_3.displayField){_5=_4.find("property",_3.displayField);if(!_5)return}
if(!_2)
_2=Array.shouldSortAscending(_5.direction)?"descending":"ascending";if(_2=="unsort")_4.remove(_5);else _5.direction=_2;this.setSort(_4)}
,isc.A.$58d=function isc_ListGrid__addSort(_1,_2){var _3=_2!=null?_2:this.$52o;this.addSort({property:_1,direction:_3?"ascending":"descending"})}
,isc.A.addSort=function isc_ListGrid_addSort(_1){var _2=this.$32?isc.shallowClone(this.$32):[];_2.add(_1);this.setSort(_2)}
,isc.A.getSort=function isc_ListGrid_getSort(){if(this.$32){return isc.shallowClone(this.$32)}else if(this.initialSort){return isc.shallowClone(this.initialSort)}else{var _1;if(this.sortFieldNum!=null)_1=this.getField(this.sortFieldNum);if(!_1&&this.sortField!=null){_1=this.getUnderlyingField(this.sortField)}
if(_1){var _2=this.$52z(_1);return[{property:_1[this.fieldIdProperty],direction:_2}]}}}
,isc.A.getSortSpecifier=function isc_ListGrid_getSortSpecifier(_1){var _2=null;if(this.$32&&this.$32.length>0){_2=this.$32.find("property",_1);if(!_2){var _3=this.getSpecifiedField(_1);if(_3&&_3.displayField&&!_3.optionDataSource){_2=this.$32.find("property",_3.displayField)}}}
return _2}
,isc.A.getSortFieldCount=function isc_ListGrid_getSortFieldCount(){return this.$32?this.$32.length:0}
,isc.A.isSortField=function isc_ListGrid_isSortField(_1){var _2=this.getSortSpecifier(_1)?true:false;if(!_2){var _3=this.getSpecifiedField(_1);if(_3&&_3.displayField&&!_3.optionDataSource){_2=this.getSortSpecifier(_3.displayField)?true:false}}
return _2}
,isc.A.getSortNumeralHTML=function isc_ListGrid_getSortNumeralHTML(_1,_2){return"<span class="+this.sortNumeralStyle+">"+(_2+1)+"</span>"}
,isc.A.clearSort=function isc_ListGrid_clearSort(){this.setSort(null)}
,isc.A.setSort=function isc_ListGrid_setSort(_1){if(this.logIsInfoEnabled("sorting"))this.logInfo("Entering setSort","sorting");if(!this.fields&&!this.getDataSource()){if(this.logIsInfoEnabled("sorting")){this.logInfo("setSort() not performing sort - no fields","sorting")}
return false}
if(this.confirmDiscardEdits&&this.dataSource!=null){if(this.hasChanges()&&isc.ResultSet&&isc.isA.ResultSet(this.data)&&!this.data.allMatchingRowsCached())
{this.showLostEditsConfirmation({target:this,methodName:"$58j"},{target:this,methodName:"$58k"});this.$58l=_1
return}}
var _2=[],_3;if(this.$32){for(var i=0;i<this.$32.length;i++){var _5=this.$32[i],_6=_1?_1.find("property",_5.property):null;if(!_6){_2.add(isc.shallowClone(_5));if(this.logIsInfoEnabled("sorting")){this.logInfo("In setSort - marking field "+_5.property+" for removal","sorting")}}}}
this.$32=_1||[];if(!this.$32||this.$32.length==0){if(this.logIsInfoEnabled("sorting"))
this.logInfo("null or zero-length sortSpecifiers - unsorting only","sorting")}
for(var i=0;i<this.$32.length;i++){var _7=this.$32[i],_8=this.getUnderlyingField(_7.property),_9=this.getField(_7.property),_10=null;if(_8){if(_9){if(!_3)_3=_9}
if(!_7.normalizer){if(_8.sortNormalizer){_10=_8.sortNormalizer}else if(_8.valueMap){_10=isc.isA.String(_8.valueMap)?this.getGlobalReference(_8.valueMap):_8.valueMap}else if(_8.type!=null){_10=_8.type}
_7.normalizer=_10}
if(_7.direction==null)_7.direction="ascending";if(_3&&_3==_9)_7.primarySort=true;if(_7.primarySort){this.sortDirection=Array.shouldSortAscending(_7.direction)}
if(!_7.context)_7.context=this;_7.sortIndex=i}}
var _11=this.$32[0],_12=_11?_11.property:null,_13=_12?this.getFieldNum(_12):null,_14=_12?this.getUnderlyingField(_12):null,_15=_11?Array.shouldSortAscending(_11.direction):null;if(!_14){if(_11)this.logWarn("Field does not exist: "+_11.property)}
if(_13>=0){this.$53e(_13)}else{this.$53e(null)}
if(this.logIsInfoEnabled("sorting")){this.logInfo("In setSort - ready to sort on specifiers:\n"+isc.echoAll(this.$32),"sorting")}
if(this.$32&&this.$32.length>0){if(this.data&&(this.data.setSort||this.data.length>0||isc.isA.ResultTree(this.data)||isc.isA.Tree(this.data)))
{if(this.data.setSort){if(this.logIsInfoEnabled("sorting")){this.logInfo("In setSort -  Calling data.setSort with specifiers:\n"+isc.echoAll(this.$32),"sorting")}
this.data.setSort(this.$32)}else if(this.data.sortByProperty){if(this.logIsInfoEnabled("sorting")){this.logInfo("In setSort - Calling data.sortByProperty with specifier:\n"+isc.echoAll(_11),"sorting")}
this.data.sortByProperty(_11.property,Array.shouldSortAscending(_11.direction),_11.normalizer,_11.context)}}else{if(this.logIsInfoEnabled("sorting")){this.logInfo("In setSort - not sorting:\nthis.data is"+this.echoAll(this.data),"sorting")}}}else{if(this.data){if(this.data.setSort!=null)this.data.setSort([]);else if(this.data.unsort)this.data.unsort()}
if(this.invalidateCacheOnUnsort){this.invalidateCache()}}
var _16=(this.header&&isc.isA.Toolbar(this.header));if(_2){if(_2.length>0){if(this.logIsInfoEnabled("sorting")){this.logInfo("In setSort - Removing sort-media from now unsorted fields:\n"+isc.echoAll(_2),"sorting")}}
for(var i=0;i<_2.length;i++){var _17=_2[i],_8=this.getSpecifiedField(_17.property),_18=[];if(_8){_18.add(_8);if(this.logIsInfoEnabled("sorting")){this.logInfo("In setSort, removing sort-media for fieldName '"+_8.name+"'","sorting")}}
var _19=this.getFields();if(_19){var _20=_19.findAll("displayField",_17.property);if(_20){_20.removeUnless("optionDataSource",null);if(_20.length>0){_18.addList(_20);if(this.logIsInfoEnabled("sorting")){this.logInfo("In setSort, removing sort-media for fields with displayField "+"'"+_8.name+"' - these are: "+isc.echoFull(_20.getProperty("name").join(", ")),"sorting")}}}}
for(var j=0;j<_18.length;j++){var _22=_18[j],_23=this.getFieldNum(_22.name);if(_22)delete _22.sortDirection;if(_23>=0&&_16){var _24=this.getFieldHeaderButton(_23);if(_24)_24.setTitle(_24.getTitle())}}}}
if(this.$32&&this.$32.length>0){this.logInfo("In setSort - Setting sort-media for sorted fields:\n"+isc.echoAll(this.$32),"sorting");var _25=false;for(var i=0;i<this.$32.length;i++){var _17=this.$32[i],_8=this.getSpecifiedField(_17.property),_18=[];if(_8){_18.add(_8);if(this.logIsInfoEnabled("sorting")){this.logInfo("In setSort, adding sort-media for fieldName '"+_8.name+"'","sorting")}}
var _19=this.getFields();if(_19){var _20=_19.findAll("displayField",_17.property);if(_20){_18.addList(_20);if(this.logIsInfoEnabled("sorting")){this.logInfo("In setSort, adding sort-media for fields with displayField "+"'"+_8.name+"' - these are: "+isc.echoFull(_20.getProperty("name").join(", ")),"sorting")}}}
for(var j=0;j<_18.length;j++){var _22=_18[j],_23=this.getFieldNum(_22.name);if(_22)_22.sortDirection=Array.shouldSortAscending(_17.direction);if(_16&&_23>=0){var _26=this.getFieldHeader(_23),_24=this.getFieldHeaderButton(_23);if(_24){if(!_25){_26.selectButton(_24);_25=true}
_24.setTitle(this.getHeaderButtonTitle(_24))}}}}}else if(_16){var _27;if(this.header){_27=this.header.getSelectedButton();if(_27)_27.deselect()}
if(this.frozenHeader){_27=this.frozenHeader.getSelectedButton();if(_27)_27.deselect()}}
if(_13>=0){if(this.logIsInfoEnabled("sorting")){this.logInfo("In setSort - $53e called with fieldNum: "+_13+" - sortField is now: "+this.sortField+"\ngetSortState() now returns: "+isc.echoAll(this.getSortState()),"sorting")}}
if(this.sorter){this.sorter.setTitle(this.sorter.getTitle())}
if(this.body&&this.showRecordComponents)delete this.body.$49q;this.$52w(this.$52i);if(this.logIsInfoEnabled("sorting"))this.logInfo("Leaving setSort","sorting");this.handleSortChanged(this.$32);return true}
,isc.A.handleSortChanged=function isc_ListGrid_handleSortChanged(_1){this.sortChanged(_1);this.handleViewStateChanged()}
,isc.A.sortChanged=function isc_ListGrid_sortChanged(_1){}
,isc.A.$58j=function isc_ListGrid__continueSort(){var _1=this.$58l;delete this.$58l;this.setSort(_1)}
,isc.A.$58k=function isc_ListGrid__cancelSort(){var _1;var _1=this.$58l?this.$58l[0]:null;delete this.$58l;if(_1!=null){var _2=this.getFieldNum(_1.property);if(_2!=-1&&this.sortFieldNum!=_2){this.header.deselectButton(_2)}}}
,isc.A.sortData=function isc_ListGrid_sortData(){if(!this.data||!this.fields)return;var _1=this.fields[this.$4h()],_2=null;if(_1==null){this.$53e(0);_1=this.fields[0]}
if(_1.sortNormalizer){_2=_1.sortNormalizer}else if(_1.valueMap){_2=_1.valueMap;if(isc.isA.String(_1.valueMap))_2=this.getGlobalReference(_1.valueMap)}else if(_1.type!=null){_2=_1.type}
this.data.sortByProperty(_1[this.fieldIdProperty],_1.sortDirection,_2,this)}
,isc.A.getEmbeddedComponentCount=function isc_ListGrid_getEmbeddedComponentCount(_1){var _2=this.body?this.body.$4p:null;if(this.frozenBody&&this.frozenBody.$4p!=null){_2=(_2?_2.duplicate():[]).addList(this.frozenBody.$4p)}
if(!_2)return 0;if(_1=="recordComponent"){_2=_2.findAll("isRecordComponent",true)}else if(_1=="backgroundComponent"){_2=_2.findAll("isBackgroundComponent",true)}
return _2==null?0:_2.length}
,isc.A.addEmbeddedComponent=function isc_ListGrid_addEmbeddedComponent(_1,_2,_3,_4,_5){var _6=this.getFieldBody(_4),_4=this.getLocalFieldNum(_4),_3=(_3!=null?_3:this.getRecordIndex(_2));_6.addEmbeddedComponent(_1,_2,_3,_4,_5);if(this.frozenBody!=null){var _7=_6==this.frozenBody?this.body:this.frozenBody;if(_3>=0&&_7.isDrawn()&&!_7.isDirty()){var _8=_7.getRowHeight(_2,_3);if(_8!=_6.getRowSize(_3)){_7.markForRedraw()}}}}
,isc.A.removeEmbeddedComponent=function isc_ListGrid_removeEmbeddedComponent(_1,_2,_3){var _4;if(isc.isA.Canvas(_1)){_2=_1;_1=_2.embeddedRecord}
if(isc.isA.Number(_2)){_4=this.getFieldBody(_2);_2=this.getLocalFieldNum(_2)}else{if(!_2){if(!_1.$4p||_1.$4p.length==0)return;_2=_1.$4p[0]}
_4=isc.Canvas.getById(_2.$476)}
if(_4==null){return}
_4.removeEmbeddedComponent(_1,_2,_3)}
,isc.A.getEmbeddedComponent=function isc_ListGrid_getEmbeddedComponent(_1,_2){var _3;if(isc.isA.Number(_2)){_3=this.getFieldBody(_2);_2=this.getLocalFieldNum(_2)}else{_3=isc.Canvas.getById(_2.$476)}
return _3.getEmbeddedComponent(_1,_2)}
,isc.A.getRecordDataSource=function isc_ListGrid_getRecordDataSource(_1){return this.dataSource}
,isc.A.openRecordEditor=function isc_ListGrid_openRecordEditor(_1){if(this.$54z!=null)this.closeRecord();var _2=isc.addProperties({},_1);var _3=this.ns.DynamicForm.create(this.recordEditorProperties,{autoDraw:false,dataSource:this.getRecordDataSource(_1),numCols:4,values:_2,$58m:_1});var _4=this.ns.VStack.create({autoDraw:false,width:this.getAvailableFieldWidth()-this.embeddedComponentIndent,left:this.embeddedComponentIndent,destroyOnUnEmbed:true,members:[_3,this.ns.Toolbar.create({autoDraw:false,width:200,buttons:[{title:this.recordEditorSaveButtonTitle,click:this.getID()+".embeddedSaveRecord("+_3.getID()+")",extraSpace:10},{title:this.recordEditorCancelButtonTitle,record:_1,grid:this,click:function(){var _5=this.parentElement.parentElement;this.grid.closeRecord(this.record,_5)}}]})]});this.addEmbeddedComponent(_4,_1,this.data.indexOf(_1));this.$54z=_1;this.$58n=_4}
,isc.A.embeddedSaveRecord=function isc_ListGrid_embeddedSaveRecord(_1){_1.saveData({target:this,methodName:"embeddedEditComplete"},{$58m:_1.$58m,$58o:_1})}
,isc.A.embeddedEditComplete=function isc_ListGrid_embeddedEditComplete(_1,_2,_3){if(_1.status==0){this.removeEmbeddedComponent(_3.$58m,_3.$58o)}}
,isc.A.closeRecord=function isc_ListGrid_closeRecord(_1,_2){if(!_1)_1=this.$54z;if(!_2)_2=this.$58n;this.removeEmbeddedComponent(_1,_2);this.$54z=null;this.$58n=null}
,isc.A.openRecordDetailGrid=function isc_ListGrid_openRecordDetailGrid(_1,_2){if(this.$54z!=null)this.closeRecord();var _3=this.getRecordDetailGrid(_1,_2);var _4=isc.VLayout.create({autoDraw:false,destroyOnUnEmbed:true,height:this.cellHeight,left:this.embeddedComponentIndent,width:this.getAvailableFieldWidth()-this.embeddedComponentIndent,resizeBarSize:4,members:[_3]});this.addEmbeddedComponent(_4,_1,this.data.indexOf(_1));var _2=isc.DataSource.getDataSource(_3.dataSource);_3.fetchRelatedData(_1,this.getRecordDataSource(_1));this.$54z=_1;this.$58n=_4}
,isc.A.getRecordDetailGrid=function isc_ListGrid_getRecordDetailGrid(_1,_2){_2=isc.DataSource.getDataSource(_2);var _3=isc.ListGrid.create(this.recordDetailGridProperties,{autoDraw:false,dataSource:_2,showResizeBar:true,showCellContextMenus:this.showCellContextMenus},this.nestedGridDefaults);return _3}
,isc.A.chartData=function isc_ListGrid_chartData(_1,_2,_3,_4,_5){this.checkChartConstructor();if(_1)_1=this.getField(_1);if(_2)_2=this.map("getField",_2);else{_2=this.fields.duplicate();_2.remove(_1)}
if(!_3){if(!isc.ResultSet||!isc.isA.ResultSet(this.data)){_3=this.data}else if(this.data.allMatchingRowsCached()){_3=this.data.getAllRows()}else{var _6=this.getVisibleRows();_3=this.data.getRange(_6[0],_6[1])}}
var _7,_8;if(_2.length>=1){for(var i=0;i<_2.length;i++){var _10=_2[i];_2[i]={id:_10.name,title:this.htmlUnescapeExportFieldValue(_10.title),type:_10.type}}
_8={id:"columnFacet",title:this.valueTitle,values:_2,inlinedValues:true}}
if(_3.length>=1&&_1){_7={id:_1.name}}
var _11=[_8,_7];_11.removeAll([null]);if(_5)_11.reverse();var _12=isc.addProperties({data:_3,facets:_11,title:this.chartTitle,chartType:this.chartType},_4);if(_7&&!_8)_12.valueProperty=_2[0].name;return this.createAutoChild("chart",_12,this.chartConstructor)}
,isc.A.checkChartConstructor=function isc_ListGrid_checkChartConstructor(){var _1=this.chartConstructor;if(isc.isA.String(_1)){_1=window.isc[this.chartConstructor]}
if(_1==null){this.logWarn('Component chartConstructor attribute set to "'+this.chartConstructor+'" - this cannot be resolved to a valid SmartClient class. Verify that all '+'appropriate modules are loaded and that this class exists.')}else if(_1.invalidClass){var _2='Component chartConstructor attribute set to "'+this.chartConstructor+'". ';if(_1.invalidErrorMessage)_2+=_1.invalidErrorMessage;else _2+=".This class is invalid - verify all appropriate modules are loaded.";this.logWarn(_2)}}
,isc.A.chartRow=function isc_ListGrid_chartRow(_1,_2,_3){return this.chartData(null,_2,[this.getRecord(_1)],_3)}
,isc.A.chartColumn=function isc_ListGrid_chartColumn(_1,_2,_3){return this.chartData(_2,[_1],null,_3)}
,isc.A.regroup=function isc_ListGrid_regroup(_1){if(!this.isGrouped)return;var _2=this.getGroupByFields();if(_2==null||_2.length==0)return;if(this.inhibitRegroup||!(this.originalData||this.data))
{return}
var _3;if(this.data.isGroupedOutput&&this.originalData)_3=this.originalData;else this.originalData=_3=this.data;var _4=_3.getLength();if(isc.ResultSet&&isc.isA.ResultSet(_3)&&(!_3.lengthIsKnown()||!_3.rangeIsLoaded(0,_4-1)))
{_3.getRange(0,_4-1);this.$53d=true;this.logInfo("postponing grouping until data is loaded","grouping");return}
if(_4>=this.groupByMaxRecords){this.logInfo("Results too numerous - disabling grouping.","grouping");this.clearGroupBy();return}
var _5;if(this.groupTree){if(!_1&&this.canCollapseGroup&&this.retainOpenStateOnRegroup&&(this.groupTree.getLength()>0)&&(this.groupTree.$44t==this.groupByField))
{_5=[];this.$53c(this.groupTree,this.groupTree.getRoot(),_5,true)}
this.ignore(this.groupTree,"changeDataVisibility");this.groupTree.destroy()}
this.groupTree=this.createAutoChild("groupTree",{idField:this.groupIdField,parentIdField:this.groupParentIdField,titleProperty:"groupValue",childrenProperty:"groupMembers",parentProperty:"_groupTree_"+this.ID,showRoot:false,isGroupedOutput:true,alwaysSortGroupHeaders:(this.showGroupSummary&&this.showGroupSummaryInHeader),$44t:this.groupByField,criteria:this.data.criteria,separateFolders:true,getCriteria:function(){return this.criteria},getChildren:function(_30,_31,_32,_33,_34,_35,_36){var _6=this.Super("getChildren",arguments);if(_36||_31==isc.Tree.FOLDERS_ONLY||_30==null||_30==this.getRoot())
{return _6}
var _7=this.creator;if(_7&&_7.showGroupSummary&&!_7.showGroupSummaryInHeader){var _8=_30.groupName;if(_7.groupByFieldSummaries!=null&&!_7.groupByFieldSummaries.contains(_8))
{return _6}
var _9=_6?_6.duplicate():[],_10=this.getRecordsInGroup(_30);_10=this.combineWithEditVals(_10);var _11=_7.getGroupSummaryData(_10,_30)
if(!_11!=null){if(!isc.isAn.Array(_11))_11=[_11];_9.addList(_11)}
return _9}else{return _6}},getRecordsInGroup:function(_30){var _7=this.creator,_12=_7.getGroupByFields(),_8=_30.groupName;var _6=this.getChildren(_30,null,null,null,null,null,true);var _13=[];if(_8==_12[_12.length-1]){_13.addList(_6)}else{if(_6!=null){for(var i=0;i<_6.length;i++){var _15=this.getRecordsInGroup(_6[i]);if(_15!=null&&_15.length>0)_13.addList(_15)}}}
return _13},combineWithEditVals:function(_6){var _7=this.creator,_10=[];_10.addList(_6);var _16=_7.getAllEditRows();if(_16.length>0){var _17;for(var i=0;i<_10.length;i++){var _18=_7.getEditSession(_10[i]);if(_18){var _19=isc.addProperties({},_10[i],_18.$53o);_10[i]=_19}}}
return _10},primaryKeyFields:this.dataSource?this.getDataSource().getPrimaryKeyFieldNames():null,indexOf:function(_30){var _20=this.Super("indexOf",arguments);if(_20==-1&&this.creator.getDataSource()!=null){var _21=this.$44z();_20=_21.findByKeys(_30,this.creator.getDataSource())}
return _20}},isc.Tree);this.logInfo("Adding "+_3.getLength()+" records to groups","grouping");for(var i=0;i<_3.getLength();i++){this.$4g(_3.get(i),false,_5)}
var _22=this.groupTree.getRoot(),_23=0,_24=0,_25=this.groupTree,_2=this.getGroupByFields();while(_22!=null&&_24<_2.length){var _26=_25.getChildren(_22);var _27=(this.completeFields||this.fields).find(this.fieldIdProperty,_2[_24]);for(var i=0;i<_26.length;i++){var _28=_26[i],_29=this.getGroupTitle(_28,_27);if(!this.singleCellGroupHeaders()){_28.groupTitle=_29}else _28[this.singleCellValueProperty]=_29;if(this.showGroupSummary&&this.showGroupSummaryInHeader){this.applyGroupSummaryToHeader(_28)}}
if(_25.getParent(_22)&&_25.getChildren(_25.getParent(_22))[_23+1]){_23++;_22=_25.getChildren(_25.getParent(_22))[_23]}else{_22=_25.getChildren(_22).first();_23=0;_24++}}
if(!_5)this.openInitialGroups();this.data=_25;if(!_1){this.$529(this.data)}else{this.observe(_25,"changeDataVisibility","observer.$523()")}
this.createSelectionModel();if(this.$52v){this.setSelectedState(this.$52v);delete this.$52v}
if(this.fields.find("$50r",true)!=null){this.updateFieldWidthsForAutoFitValue("regroup with group title column showing")}
this.markForRedraw("regroup")}
,isc.A.$57j=function isc_ListGrid__incrementalRegroup(_1,_2,_3,_4){this.$495=true;var _5=this.data.getParents(_2);var _6=this.data.remove(_2);var _7=this.getGroupByFields();for(var i=0,j=_7.length-1;i<_5.length-1;i++){var _10=_5[i];if(_10.groupMembers.getLength()==0)this.data.remove(_10);else{var _11=(this.completeFields||this.fields).find(this.fieldIdProperty,_10);if(this.singleCellGroupHeaders()){_10[this.singleCellValueProperty]=this.getGroupTitle(_10,_11)}else{_10.groupTitle=this.getGroupTitle(_10,_11)}}
j--}
if(!(_2&&!_6)&&(_1||_4))
this.$4g(_1||_4,true)}
,isc.A.openInitialGroups=function isc_ListGrid_openInitialGroups(){var _1=this.groupTree;if(this.groupStartOpen=="all"){_1.openAll()}else if(this.groupStartOpen=="first"){_1.openAll(_1.getChildren(_1.getRoot()).first())}else if(isc.isAn.Array(this.groupStartOpen)){var _2=_1.getChildren(_1.getRoot());for(var i=0;i<this.groupStartOpen.length;i++){var _4=this.groupStartOpen[i];var _5=_2.find("groupValue",_4);if(_5)_1.openFolder(_5)}}}
,isc.A.groupBy=function isc_ListGrid_groupBy(_1){var _2=[];if(isc.isAn.Array(_1)){_2=_1}else{for(var i=0;i<arguments.length;i++){_2[i]=arguments[i]}}
for(var i=0;i<_2.length;i++){var _4=this.completeFields||this.fields||[];if(_2[i]!=null&&(_4.find("name",_2[i])==null)){this.logWarn("groupBy() passed field:"+_2[i]+" - this is not a valid field"+" within this grid - ignoring","grouping");return}}
if(this.handleGroupBy!=null&&this.handleGroupBy(_2)==false){return}
this.handleViewStateChanged();if(_2.length==0||_2[0]==null){this.clearGroupBy();return}
if(isc.isAn.Array(this.groupByField))this.groupByField.setLength(0);else this.groupByField=[];for(var i=0;i<_2.length;i++){if(this.isCheckboxField(_2[i]))continue;var _5=this.getSpecifiedField(_2[i]);if(_5&&_5.displayField!=null&&this.getField(_5.displayField)&&_5.optionDataSource==null){this.groupByField.add(_5.displayField)}else{this.groupByField.add(_2[i])}}
this.isGrouped=true;if(this.showGroupSummary&&this.showGroupSummaryInHeader&&this.getGroupTitleField()==null&&this.showGroupTitleColumn)
{if(this.groupTitleColumn==null){this.groupTitleColumn=this.getGroupTitleColumn()}
if(this.fields.indexOf(this.groupTitleColumn)==-1){this.addField(this.groupTitleColumn,this.getGroupTitleColumnPosition())}}
this.logInfo("groupBy: "+this.groupByField,"grouping");this.$53d=true;this.dataChanged()}
,isc.A.clearGroupBy=function isc_ListGrid_clearGroupBy(){this.logInfo("ungrouping","grouping");this.isGrouped=false;if(this.originalData){var _1=this.preserveEditsOnSetData;this.preserveEditsOnSetData=true;this.setData(this.originalData);this.preserveEditsOnSetData=_1;delete this.originalData;if(this.groupTree&&this.groupTree.criteria){this.data.setCriteria(this.groupTree.criteria)}
delete this.groupTree;if(this.groupByField)this.groupByField=null}
if(this.groupTitleColumn!=null){var _2=this.completeFields||this.fields,_3=_2.indexOf(this.groupTitleColumn);if(_3!=-1){var _4=[];for(var i=0;i<_2.length;i++){if(i==_3)continue;_4[_4.length]=_2[i]}
this.setFields(_4)}}}
,isc.A.getGroupTitle=function isc_ListGrid_getGroupTitle(_1,_2){if(!_2){var _3=this.data.getLevel(_1)-1;var _4=this.completeFields||this.fields,_5=this.getGroupByFields();_2=_4.find(this.fieldIdProperty,_5[_3])}
if(_2==null){return}
if(_2.getGroupTitle){return _2.getGroupTitle(_1.groupValue,_1,_2,_2.name,this)}
else if(!_2.getGroupValue&&_2.$14v&&_2.$14v.getGroupTitle){return _2.$14v.getGroupTitle(_1.groupValue,_1,_2,_2.name,this)}else if(!this.singleCellGroupHeaders()){return _1.groupTitle}else{return _1[this.singleCellValueProperty]}}
,isc.A.getGroupNodeHTML=function isc_ListGrid_getGroupNodeHTML(_1,_2){var _3=this.frozenBody===_2;if(this.frozenBody&&!_3)return this.emptyCellValue;var _4=this.data.isOpen(_1)?"opened":"closed",_5=isc.Img.urlForState(this.groupIcon,null,null,_4),_6=isc.Canvas.spacerHTML(this.groupIconPadding,1),_7=isc.Canvas.spacerHTML((this.data.getLevel(_1)-1)*this.groupIndentSize+this.groupLeadingIndent,1);var _8=this.imgHTML(_5,this.groupIconSize,this.groupIconSize);var _9=(this.canCollapseGroup?_7+_8+_6+this.getGroupTitle(_1):_7+_6+this.getGroupTitle(_1));return _9}
,isc.A.$4g=function isc_ListGrid__addRecordToGroup(_1,_2,_3){var _4,_5=this.groupTree.getRoot(),_6=this.getGroupByFields();for(var i=0;i<_6.length;i++){var _8=_6[i],_9=(this.completeFields||this.fields).findIndex(this.fieldIdProperty,_8),_10=(this.completeFields||this.fields).get(_9),_11=this.data.indexOf(_1);var _12=this.getRawCellValue(_1,_11,_8,true);if(_10.getGroupValue){_12=_10.getGroupValue(_12,_1,_10,_8,this)}else if(_10.userFormula){_12=this.getFormulaFieldValue(_10,_1)}else if(_10.userSummary){_12=this.getSummaryFieldValue(_10,_1)}else if(_10.$14v&&_10.$14v.getGroupValue){_12=_10.$14v.getGroupValue(_12,_1,_10,_8,this)}
if(_10.valueMap&&_10.valueMap[_12]){_12=_10.valueMap[_12]}
if(_12==null||isc.isAn.emptyString(_12)){_12=this.nullGroupTitle}
var _13=this.groupTree.getChildren(_5);if(_13==null)_4=null;else _4=_13.find('groupValue',_12);if(!_4){_4={groupName:_8,groupValue:_12,$35:true,canDrag:false,canSelect:false};if(this.singleCellGroupHeaders())_4.singleCellValue=_12;else _4.groupTitle=_12;_4[_8]=_12;_4[this.recordCustomStyleProperty]=this.groupNodeStyle
_4[this.recordEditProperty]=false
if(_3!=null){_4[this.groupTree.openProperty]=_3.find(_8,_12)?true:false}
this.groupTree.add(_4,_5)}
_5=_4}
this.groupTree.add(_1,_5);if(_2){var _14=this.groupTree.getParents(_1);for(var i=_14.length-2,j=0;i>=0;i--,j++){var _16=this.getGroupTitle(_14[i]);if(!this.singleCellGroupHeaders())_14[i].groupTitle=_16;else _14[i][this.singleCellValueProperty]=_16}}}
,isc.A.applyGroupSummaryToHeader=function isc_ListGrid_applyGroupSummaryToHeader(_1){var _2=_1.groupName;if(this.groupByFieldSummaries!=null&&!this.groupByFieldSummaries.contains(_2)){return}
var _3=this.groupTree,_4=_3.combineWithEditVals(_3.getRecordsInGroup(_1));var _5=this.getGroupSummaryData(_4,_1);if(isc.isAn.Array(_5))_5=_5[0];var _6=this.completeFields||this.fields;for(var i=0;i<_6.length;i++){var _8=_6[i].name;if(_8=="groupTitle")continue;_1[_8]=_5[_8]}
_1.isGroupSummary=true}
,isc.A.setShowGroupSummaryInHeader=function isc_ListGrid_setShowGroupSummaryInHeader(_1){if(this.showGroupSummaryInHeader==_1)return;this.showGroupSummaryInHeader=_1;var _2=this.getGroupByFields();if(_2!=null&&_2.length>0){this.ungroup();this.groupBy(_2)}}
,isc.A.ungroup=function isc_ListGrid_ungroup(){this.$52v=this.getSelectedState(true);this.groupBy(null)}
,isc.A.setHeaderSpans=function isc_ListGrid_setHeaderSpans(_1){this.headerSpans=_1;this.updateHeader();this.layoutChildren("headerSpans changed")}
,isc.A.setHeaderSpanTitle=function isc_ListGrid_setHeaderSpanTitle(_1,_2){var _3=this.headerSpans.find("name",_1);if(!_3){this.logWarn("setHeaderSpanTitle() - unable to locate span named: "+_1+this.getStackTrace());return}
_3.title=_2;if(_3.liveObject)_3.liveObject.setTitle(_2)}
,isc.A.refreshGroupSummary=function isc_ListGrid_refreshGroupSummary(_1){if(!this.isGrouped||!this.showGroupSummary||!this.groupTree)return;if(this.showGroupSummaryInHeader){if(_1!=null){var _2=this.groupTree.getParent(this.groupTree.get(_1));this.applyGroupSummaryToHeader(_2);this.refreshRow(this.groupTree.indexOf(_2))}else{var _3=this.groupTree.findAll("$35",true);for(var i=0;i<_3.length;i++){this.applyGroupSummaryToHeader(_3[i])}
this.markForRedraw("refresh group summaries")}}else{var _5;if(_1!=null)_5=this.groupTree.getLength();this.groupTree.$44j();if(_1==null||(_5!=this.groupTree.getLength())){this.body.markForRedraw("refresh group summaries")}else{var _6=this.groupTree.getParent(this.groupTree.get(_1));if(_6){var _7=this.groupTree.indexOf(_6),_8=this.groupTree.getChildren(_6);for(var i=0;i<_8.length;i++){if(_8[i].isGroupSummary)this.refreshRow(_7+1+i)}}}}}
,isc.A.addBodyPassthroughMethods=function isc_ListGrid_addBodyPassthroughMethods(_1){if(this.body)this.$57x(this.body);if(this.frozenBody)this.$57x(this.frozenBody)}
,isc.A.$57x=function isc_ListGrid__addBodyPassthroughMethods(_1){var _2={},_3=isc.getKeys(isc.ListGrid.$50a);for(var i=0;i<_3.length;i++){var _5=_3[i],_6=this[_5];if(_5=="cellContextClick")continue;if(_6==null){_2[_5]=_1.getClass().getPrototype()[_5]}else if(_6!=isc.ListGrid.$50b[_5]){_2[_5]=isc.ListGrid.$50a[_5]}}
_1.addMethods(_2)}
,isc.A.addProperties=function isc_ListGrid_addProperties(_1){this.Super("addProperties",arguments);this.addBodyPassthroughMethods()}
,isc.A.addMethods=function isc_ListGrid_addMethods(_1){this.Super("addMethods",arguments);this.addBodyPassthroughMethods()}
,isc.A.propertyChanged=function isc_ListGrid_propertyChanged(_1,_2){this.invokeSuper(isc.ListGrid,"propertyChanged",_1,_2);if(this.$52j[_1])this.$58p=true;if(this.body==null)return;if(isc.ListGrid.$496.contains(_1)){this.body[_1]=_2}
if(this.$52k[_1]!=null){this.body.setProperty(this.$52k[_1],_2);this.markForRedraw()}}
,isc.A.doneSettingProperties=function isc_ListGrid_doneSettingProperties(){if(this.$58p)this.$52p()}
,isc.A.setChildEditableProperties=function isc_ListGrid_setChildEditableProperties(_1,_2,_3,_4){var _5=isc.DS.get(_3.type);if(_5&&_5.inheritsSchema("ListGridField")){isc.addProperties(_1,_2);this.setFields(this.completeFields||this.fields)}else{this.Super("setChildEditableProperties",arguments)}}
,isc.A.getExportFieldValue=function isc_ListGrid_getExportFieldValue(_1,_2,_3){var _4=this.getSpecifiedField(_2);if(this.isCheckboxField(_4)||_4.valueIcons||this.$53g(_4))
{var _5=(_4.displayField!=null&&!_4.valueMap&&!_4.getCellValue&&this.$4t(_4));var _6=this.getRecordIndex(_1),_7;_7=this.getRawCellValue(_1,_6,_5?_4.displayField:_2,true);_7=this.$32v(_7,_1,_4,_6,_3);var _8=this.getFieldHilites(_1,_4);if(_8)_7=this.applyHiliteHTML(_8,_7);return this.htmlUnescapeExportFieldTitle(_7.toString())}
return this.Super("getExportFieldValue",arguments)}
,isc.A.getOriginalData=function isc_ListGrid_getOriginalData(){return(this.isGrouped&&this.originalData)?this.originalData:this.getData()}
);isc.B._maxIndex=isc.C+684;isc.ListGrid.registerStringMethods(isc.GridRenderer.$488);isc.ListGrid.registerStringMethods({recordClick:"viewer,record,recordNum,field,fieldNum,value,rawValue",recordDoubleClick:"viewer,record,recordNum,field,fieldNum,value,rawValue",recordsDropped:"records,rowNum,viewer,sourceWidget",recordDropMove:"viewer,recordNum,record,position",editValueChanged:"rowNum,fieldName,newValue,oldValue",editorChange:"record,newValue,oldValue,rowNum,colNum",cellChanged:"record,newValue,oldValue,rowNum,colNum,grid,recordNum,fieldNum",editComplete:"rowNum,colNum,newValues,oldValues,editCompletionEvent,dsResponse",editFailed:"rowNum,colNum,newValues,oldValues,editCompletionEvent,dsResponse,dsRequest",editorEnter:"record,value,rowNum,colNum",rowEditorEnter:"record,editValues,rowNum",editorExit:"editCompletionEvent,record,newValue,rowNum,colNum",rowEditorExit:"editCompletionEvent,record,newValues,rowNum",validateCellValue:"rowNum,colNum,newValue,oldValue",validateFieldValue:"newValue,oldValue,record,field,rowNum,colNum",formatCellValue:"value,record,rowNum,colNum",formatEditorValue:"value,record,rowNum,colNum",parseEditorValue:"value,record,rowNum,colNum",sortChanged:"sortSpecifiers",fieldStateChanged:"",viewStateChanged:"",dataArrived:"startRow,endRow",headerClick:"fieldNum",onHeaderClick:"fieldNum",onRecordDrop:"dropRecords,targetRecord,index,sourceWidget",onExpandRecord:"record",onCollapseRecord:"record",drawAreaChanged:"oldStartRow,oldEndRow,oldStartCol,oldEndCol",showRecordComponent:"record,colNum",createRecordComponent:"record,colNum",updateRecordComponent:"record,colNum,component,recordChanged",updateFilterEditorValues:"criteria",filterEditorSubmit:"criteria",handleGroupBy:"fields"});isc.ListGrid.$58q=isc.addProperties({},isc.ListGrid.getInstanceProperty("headerDefaults"));isc.ListGrid.$58r=isc.addProperties({},isc.ListGrid.getInstanceProperty("headerButtonDefaults"));isc.ListGrid.classInit();isc.defineClass("LineEditor",isc.ListGrid);isc.A=isc.LineEditor.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.canEdit=true;isc.A.editEvent="click";isc.A.editOnFocus=true;isc.A.modalEditing=true;isc.A.enterKeyEditAction="nextRowStart";isc.A.listEndEditAction="next";isc.A.height=50;isc.A.emptyMessage="Click to add data";isc.A.emptyMessageStyle="normal";isc.A.autoFitData="vertical";isc.A.leaveScrollbarGap=false;isc.B.push(isc.A.click=function isc_LineEditor_click(){var _1=this.getRecord(this.getEventRow());this.Super("click",arguments);if(_1==null&&isc.EH.lastEvent.target==this.body)this.startEditingNew()}
);isc.B._maxIndex=isc.C+1;isc.ClassFactory.defineClass("TreeGrid","ListGrid");isc.addGlobal("TreeViewer",isc.TreeGrid);isc.defineClass("TreeGridBody",isc.GridBody);isc.A=isc.TreeGridBody.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$58s="TABLE";isc.A.$58t="padding:0px;border:0px;";isc.B.push(isc.A.$48f=function isc_TreeGridBody__updateCellStyle(_1,_2,_3,_4,_5){if(_4==null)_4=this.getTableElement(_2,_3);if(_4==null)return;if(!this.showHiliteInCells&&_3==this.grid.getLocalFieldNum(this.grid.getTreeFieldNum()))
{if(_1==null)_1=this.getCellRecord(_2,_3);if(_5==null)_5=this.getCellStyle(_1,_2,_3);var _6=_4.childNodes[0];while(_6&&_6.tagName!=this.$58s)_6=_6.childNodes[0];if(_6){var _7;if(this.getCellCSSText){_7=this.getCellCSSText(_1,_2,_3);if(_7!=null&&!isc.isAn.emptyString(_7)){_7+=isc.Canvas.$rr}else _7=null}
_6.className=_5;if(_7!=null)_6.cssText=_7;var _8=_6.rows,_9=_8[0].cells;if(_9&&_9.length>0){for(var i=0;i<_9.length;i++){_9[i].className=_5;if(_7){if(i==_9.length-1){_7+="paddingLeft:"+this.iconPadding}
_9[i].cssText=_7}}}}}
return isc.GridRenderer.getPrototype().$48f.apply(this,[_1,_2,_3,_4,_5])}
,isc.A.click=function isc_TreeGridBody_click(_1,_2){if(!this.$6k()){var _3=this.grid,_4=_3.getEventRecordNum(),_5=_3.getRecord(_4);if(_3.data.isFolder(_5)&&_3.clickInOpenArea(_5)){if(isc.screenReader){this.$47u(_4)}
_3.toggleFolder(_5);_3.clearLastHilite();_3.$527=null;return isc.EH.STOP_BUBBLING}}
return this.Super("click",arguments)}
,isc.A.mouseDown=function isc_TreeGridBody_mouseDown(){var _1=this.getEventRow(),_2=this.grid.data.get(_1);if(_2!=null&&this.grid.clickInOpenArea(_2)){return isc.EH.STOP_BUBBLING}else if(this.grid.clickInCheckboxArea(_2)&&this.canSelectRecord(_2)){var _3=this.grid.selectionType;if(_3==isc.Selection.SINGLE){this.deselectAllRecords();this.selectRecord(_2)}else if(_3==isc.Selection.SIMPLE||_3==isc.Selection.MULTIPLE){if(this.selection.isSelected(_2))this.deselectRecord(_2);else this.selectRecord(_2)}
return isc.EH.STOP_BUBBLING}else{return this.Super("mouseDown",arguments)}}
,isc.A.mouseUp=function isc_TreeGridBody_mouseUp(){var _1=this.getEventRow(),_2=this.grid.data.get(_1);if(_2!=null&&(this.grid.clickInOpenArea(_2)||this.grid.clickInCheckboxArea(_2)))
{return isc.EH.STOP_BUBBLING}else{return this.Super("mouseUp",arguments)}}
,isc.A.placeEmbeddedComponent=function isc_TreeGridBody_placeEmbeddedComponent(_1){if(this.grid.indentRecordComponents){var _2=_1.$47r;if(_2==this.grid.getTreeFieldNum()&&!_1.snapOffsetLeft){var _3=_1.embeddedRecord;if(_3!=null){_1.snapOffsetLeft=this.grid.getOpenAreaWidth(_3)+this.grid.iconPadding}}}
return this.Super("placeEmbeddedComponent",arguments)}
);isc.B._maxIndex=isc.C+5;isc.A=isc.TreeGrid;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.TREE_FIELD={name:"nodeTitle",treeField:true,getCellValue:function(_1,_2,_3,_4){if(!_1.getNodeTitle){var _5=_4==null?null:_1.getFieldName(_4);return _2==null||_5==null?null:_2[_5]}
return _1.getNodeTitle(_2,_3,this)},canFilter:false,getFieldTitle:function(_1,_2){var _3=_1.getField(_2);if(_3.name=="nodeTitle")return _1.treeFieldTitle;return _3.title||_3.name}};isc.B.push(isc.A.$58u=function isc_c_TreeGrid__getTreeCellTemplate(){if(!this.$221){isc.Canvas.$rk.add({target:this,methodName:"$222"});this.$221=true}
if(this.$58v==null){this.$58v=["<table role='presentation' cellpadding=0 cellspacing=0 class='",,"' style='",,isc.Canvas.$rr+"'><tr><td style='",,isc.Canvas.$rr+"' class='",,"'>",,"</td>"]}
return this.$58v}
,isc.A.$58w=function isc_c_TreeGrid__getTreeCellTitleTemplate(){if(!this.$221){isc.Canvas.$rk.add({target:this,methodName:"$222"});this.$221=true}
if(this.$58x==null){this.$58x=["<td style='",,";"+isc.Canvas.$rr+"' class='",,"'>"+(isc.Browser.isSafari||isc.Browser.isIE?"<nobr>":""),,,,(isc.Browser.isSafari?"</nobr>":"")+"</td><td style='",,";"+isc.Canvas.$rr+"padding-left:",,"px;' class='",,"'>",,,"</td>"]}
return this.$58x}
,isc.A.$222=function isc_c_TreeGrid__doublingStringsChanged(){this.$58v=null;this.$58x=null}
);isc.B._maxIndex=isc.C+3;isc.A=isc.TreeGrid.getPrototype();isc.A.autoFetchTextMatchStyle="exact";isc.A.cascadeSelection=false;isc.A.showPartialSelection=false;isc.A.treeFieldTitle="Name";isc.A.autoAssignTreeField=true;isc.A.showRoot=false;isc.A.displayNodeType=isc.Tree.FOLDERS_AND_LEAVES;isc.A.canDragRecordsOut=false;isc.A.dragDataAction=isc.ListGrid.MOVE;isc.A.openDropFolderDelay=600;isc.A.parentAlreadyContainsChildMessage="This item already contains a child item with that name.";isc.A.cantDragIntoSelfMessage="You can't drag an item into itself.";isc.A.cantDragIntoChildMessage="You can't drag an item into one of it's children.";isc.A.fixedFieldWidths=true;isc.A.wrapCells=false;isc.A.showHiliteInCells=false;isc.A.indentSize=20;isc.A.extraIconGap=2;isc.A.iconSize=16;isc.A.skinImgDir="images/TreeGrid/";isc.A.folderIcon="[SKIN]/folder.gif";isc.A.dropIconSuffix="drop";isc.A.openIconSuffix="open";isc.A.closedIconSuffix="closed";isc.A.nodeIcon="[SKIN]/file.gif";isc.A.showOpenIcons=true;isc.A.showDropIcons=true;isc.A.customIconProperty="icon";isc.A.customIconOpenProperty="showOpenIcon";isc.A.customIconDropProperty="showDropIcon";isc.A.showCustomIconOpen=false;isc.A.showCustomIconDrop=false;isc.A.manyItemsImage="[SKIN]folder_file.gif";isc.A.showConnectors=false;isc.A.showFullConnectors=true;isc.A.showOpener=true;isc.A.openerImage="[SKIN]opener.gif";isc.A.connectorImage="[SKIN]connector.gif";isc.A.offlineNodeMessage="This data not available while offline";isc.A.indentRecordComponents=true;isc.A.canGroupBy=false;isc.A.ignoreEmptyCriteria=false;isc.A.drawAllMaxCells=50;isc.A.drawAheadRatio=1.0;isc.A.$6m="open_icon_";isc.A.$6n="extra_icon_";isc.A.$58y="icon_";isc.A.$58z="nodeTitle";isc.A=isc.TreeGrid.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.bodyConstructor="TreeGridBody";isc.A.iconPadding=3;isc.A.$580="</tr></table>";isc.A.$px=";";isc.A.$22d="checkbox";isc.A.$581="absmiddle";isc.A.$582=["style='margin-right:",,"px;'"];isc.A.$11z={};isc.B.push(isc.A.initWidget=function isc_TreeGrid_initWidget(){this.invokeSuper(isc.TreeGrid,this.$sb);if(!this.dataSource&&this.data!=null&&this.data.dataSource){this.dataSource=this.data.dataSource}
if(!this.fields||this.fields.length==0){this.fields=[isc.TreeGrid.TREE_FIELD]}}
,isc.A.setDataSource=function isc_TreeGrid_setDataSource(_1,_2){if(_2==null||_2.length==0){_2=[isc.TreeGrid.TREE_FIELD]}
return this.Super("setDataSource",[_1,_2])}
,isc.A.$583=function isc_TreeGrid__initTreeField(){if(!this.fields||this.fields.length==0){this.fields=[isc.TreeGrid.TREE_FIELD]}else{var _1=this.completeFields,_2=this.fields,_3;for(var i=0;i<_1.length;i++){if(_1[i].treeField){_3=_2.indexOf(_1[i]);break}}
if(_3==null){if(!this.autoAssignTreeField)return;var _5=this.data.titleProperty,_6=_2.findIndex(this.fieldIdProperty,_5);if(_6!=-1)_3=_6}
if(_3==null)_3=0;if(this.isCheckboxField(this.fields[_3]))_3+=1;this.$584=_3;var _7=_2[_3],_8=isc.TreeGrid.TREE_FIELD;for(var _9 in _8){if(_7[_9]==null){_7[_9]=_8[_9]}}}}
,isc.A.deriveVisibleFields=function isc_TreeGrid_deriveVisibleFields(_1,_2,_3,_4){this.invokeSuper(isc.TreeGrid,"deriveVisibleFields",_1,_2,_3,_4);this.$583()}
,isc.A.getEmptyMessage=function isc_TreeGrid_getEmptyMessage(){if(this.isOffline()){return this.offlineMessage}
if(isc.isA.Tree(this.data)&&this.data.getLoadState(this.data.getRoot())==isc.Tree.LOADING)
return this.loadingDataMessage==null?"&nbsp;":this.loadingDataMessage.evalDynamicString(this,{loadingImage:this.imgHTML(isc.Canvas.loadingImageSrc,isc.Canvas.loadingImageSize,isc.Canvas.loadingImageSize)});return this.emptyMessage.evalDynamicString(this,{loadingImage:this.imgHTML(isc.Canvas.loadingImageSrc,isc.Canvas.loadingImageSize,isc.Canvas.loadingImageSize)})}
,isc.A.isEmpty=function isc_TreeGrid_isEmpty(){if(!isc.isA.Tree(this.data))return true;var _1=this.data.getRoot();if(_1==null)return true;var _2=this.data.hasChildren(_1);if(_2||this.showRoot||this.data.showRoot)return false;return true}
,isc.A.getOpenState=function isc_TreeGrid_getOpenState(){var _1=this.data;if(_1==null){this.logWarn("getOpenState() called for a treeGrid with no data");return[]}
if(_1.getOpenState)return _1.getOpenState();var _2=_1.getRoot(),_3=[];this.$53c(_1,_2,_3);return isc.Comm.serialize(_3)}
,isc.A.setOpenState=function isc_TreeGrid_setOpenState(_1){if(this.data&&this.data.setOpenState){this.data.setOpenState(_1);return}
_1=this.evalViewState(_1,"openState")
if(!_1)return;if(!this.data){this.logWarn("unable to set open state for this treeGrid as this.data is unset");return}
this.data.closeAll();this.data.openFolders(_1)}
,isc.A.getSelectedPaths=function isc_TreeGrid_getSelectedPaths(){if(!this.selection)return null;var _1=this.selection.getSelection()||[],_2=[];for(var i=0;i<_1.length;i++){_2[i]=this.data.getPath(_1[i])}
return isc.Comm.serialize(_2)}
,isc.A.showActionInPanel=function isc_TreeGrid_showActionInPanel(_1){return this.Super("showActionInPanel",arguments)}
,isc.A.setSelectedPaths=function isc_TreeGrid_setSelectedPaths(_1){_1=this.evalViewState(_1,"selectedPaths")
if(!_1)return;var _2=this.selection,_3=this.data;if(_3&&_2){_2.deselectAll();var _4=[];for(var i=0;i<_1.length;i++){var _6=_3.find(_1[i]);if(_6)_4.add(_6)}
this.selection.selectList(_4)}}
,isc.A.getViewState=function isc_TreeGrid_getViewState(){var _1=this.Super("getViewState",[true]);_1.open=this.getOpenState();return"("+isc.Comm.serialize(_1)+")"}
,isc.A.setViewState=function isc_TreeGrid_setViewState(_1){this.Super("setViewState",arguments);_1=this.evalViewState(_1,"viewState",true)
if(!_1)return;if(_1.open)this.setOpenState(_1.open);if(_1.selected)this.setSelectedState(_1.selected)}
,isc.A.getDefaultData=function isc_TreeGrid_getDefaultData(){return isc.Tree.create({$393:true})}
,isc.A.setData=function isc_TreeGrid_setData(_1,_2,_3,_4){this.invokeSuper(isc.TreeGrid,"setData",_1,_2,_3,_4);if(!this.data)return;if(this.separateFolders!=null)this.data.separateFolders=this.separateFolders;if(this.sortFoldersBeforeLeaves!=null)
this.data.sortFoldersBeforeLeaves=this.sortFoldersBeforeLeaves;if(this.showRoot&&isc.ResultTree&&isc.isA.ResultTree(this.data)){this.logWarn("showRoot may not be set with a databound treeGrid, unexpected "+"results may occur")}
this.data.showRoot=this.showRoot;this.data.openDisplayNodeType=this.displayNodeType}
,isc.A.draw=function isc_TreeGrid_draw(_1,_2,_3,_4){if(this.initialData&&(!isc.ResultSet||!isc.isA.ResultSet(this.data))){this.setData(this.createResultTree())}
this.invokeSuper(isc.TreeGrid,"draw",_1,_2,_3,_4)}
,isc.A.bodyKeyPress=function isc_TreeGrid_bodyKeyPress(_1){var _2=this.selection;if(this.selectionType!=isc.Selection.NONE&&this.data.getLength()>0&&_2.anySelected()&&!_2.multipleSelected())
{var _3=this.selection.getSelectedRecord();if(_1.keyName=="Arrow_Left"){if(this.data.isFolder(_3)&&this.data.isOpen(_3)){this.closeFolder(_3)}else{this.$543(this.data.getParent(_3),true)}
return false}else if(_1.keyName=="Arrow_Right"){if(this.data.isFolder(_3)){if(!this.data.isOpen(_3)){this.openFolder(_3);return false}else{var _4=this.getRecord(this.data.indexOf(_3)+1);if(_4!=null&&this.data.getParent(_4)==_3){this.$543(_4,true);return false}}}}}
return this.Super("bodyKeyPress",arguments)}
,isc.A.$49d=function isc_TreeGrid__cellContextClick(_1,_2,_3){if(_2<0||_3<0)return true;var _4=this.data.isFolder(_1);if(this.nodeContextClick&&this.nodeContextClick(this,_1,_2)==false){return false}
if(_4){if(this.folderContextClick&&this.folderContextClick(this,_1,_2)==false){return false}}else{if(this.leafContextClick&&this.leafContextClick(this,_1,_2)==false){return false}}
return this.Super("$49d",arguments)}
,isc.A.handleEditCellEvent=function isc_TreeGrid_handleEditCellEvent(_1,_2){var _3=this.getRecord(_1);if(this.clickInOpenArea(_3)||this.clickInCheckboxArea(_3))return false;return this.Super("handleEditCellEvent",arguments)}
,isc.A.canEditCell=function isc_TreeGrid_canEditCell(_1,_2){if(this.Super("canEditCell",arguments)==false)return false;if(this.getField(_2)[this.fieldIdProperty]==this.data.nameProperty)return false;if(this.getField(_2)[this.fieldIdProperty]==this.$58z)return false;return true}
,isc.A.getEditFormItemFieldWidths=function isc_TreeGrid_getEditFormItemFieldWidths(_1){var _2=this.data.getLevel(_1);if(!this.showRoot)_2--;var _3=this.getOpenerIconSize(_1),_4=_2*(this.showConnectors?_3:this.indentSize);_4+=this.iconSize+_3;if(this.$585(_1)){_4+=(this.$53r()+this.extraIconGap)}else if(this.getExtraIcon(_1)){_4+=(this.iconSize+this.extraIconGap)}
var _5=this.Super("getEditFormItemFieldWidths",arguments),_6=this.getTreeFieldNum();_5[_6]-=_4;return _5}
,isc.A.getRecordDataSource=function isc_TreeGrid_getRecordDataSource(_1){return this.data.getNodeDataSource(_1)}
,isc.A.rowClick=function isc_TreeGrid_rowClick(_1,_2,_3){var _4=_1;if(this.clickInOpenArea(_4)||this.clickInCheckboxArea(_4))return false;this.$527=_2;if(_2<0||_3<0)return false;var _4=this.getRecord(_2),_5=this.data.isFolder(_4);if(this.nodeClick)this.nodeClick(this,_4,_2);if(_5){if(this.folderClick)this.folderClick(this,_4,_2)}else{if(this.leafClick)this.leafClick(this,_4,_2)}
return this.Super("rowClick",arguments)}
,isc.A.recordDoubleClick=function isc_TreeGrid_recordDoubleClick(_1,_2,_3,_4,_5,_6,_7){if(this.clickInOpenArea(_2)||this.clickInCheckboxArea(_2))return false;if(this.isEditable()&&this.editEvent==isc.EH.DOUBLE_CLICK&&this.canEditCell(_3,_5))
{return true}
if(this.data.isFolder(_2)){return this.toggleFolder(_2)}else
return this.openLeaf(_2)}
,isc.A.dataChanged=function isc_TreeGrid_dataChanged(){this.Super("dataChanged",arguments);var _1=this.$53a;if(_1&&this.data.isOpen(_1)&&this.data.getLoadState(_1)==isc.Tree.LOADED)
{this.$53b(_1);this.$53a=null}}
,isc.A.openLeaf=function isc_TreeGrid_openLeaf(_1){}
,isc.A.getDragTrackerIcon=function isc_TreeGrid_getDragTrackerIcon(_1){var _2;if(_1&&_1.length>1&&this.manyItemsImage!=null)
_2=this.manyItemsImage;else if(_1&&_1[0])_2=this.getIcon(_1[0],true);return _2}
,isc.A.getDragTrackerTitle=function isc_TreeGrid_getDragTrackerTitle(_1,_2,_3,_4,_5,_6){var _7=this.getFieldNum(this.getTitleField());if(_7!=this.getTreeFieldNum())
return this.invokeSuper(isc.TreeGrid,"getDragTrackerTitle",_1,_2,_3,_4,_5,_6);var _8=this.getCellStyle(_1,_2,_7),_9=this.getCellCSSText(_1,_2,_7);if(this.selection.isSelected(_1)){var _10=this.body.getCellStyleIndex(_1,_2,_7),_11=this.body.getCellStyleName(_10,_1,_2,_7);if(_11==_8){_10-=2;_8=this.body.getCellStyleName(_10,_1,_2,_7)}}
var _12=this.invokeSuper(isc.TreeGrid,"getCellValue",_1,_2,_7);var _13=this.$586(_12,_1,_2,_7,false,_8,_9).join(isc.emptyString);return["<table class='",_8,"' style='",_9,"'><tr>",_13,"</tr></table>"].join(isc.emptyString)}
,isc.A.willAcceptDrop=function isc_TreeGrid_willAcceptDrop(){if(!this.Super("willAcceptDrop",arguments))return false;isc.$ny=true;var _1=this.getEventRecordNum(),_2=this.data.get(_1);isc.$ny=false;if(_2==null)_2=this.data.getRoot();if(!_2||_2.canAcceptDrop==false)return false;var _3=this.data.isFolder(_2);if(!_3&&!(this.canReorderRecords||this.canDropOnLeaves))return false;var _4=isc.EH.dragTarget.getDragData();if(!isc.isAn.Object(_4)||this.getDropError(_4,_2)!=null){return false}
if(!_3){_2=this.data.getParent(_2);if(_2.canAcceptDrop==false)return false}
if(isc.EH.dragTarget!=this)return true;var _5=this.canReparentNodes;if(_5==null&&this.canAcceptDroppedRecords)_5=true;if(!_5){if(!isc.isAn.Array(_4))_4=[_4];var _6;_6=this.data.getParent(_4[0]);if(_6!=_2)return false;for(var i=1;i<_4.length;i++){if(_6!=this.data.getParent(_4[i]))return false}}
return true}
,isc.A.$52p=function isc_TreeGrid__setUpDragProperties(){this.canDrag=(this.canDrag||this.canDragRecordsOut||this.$57p()||this.canDragSelect);this.canDrop=(this.canDrop||this.canDragRecordsOut||this.$57p());this.canAcceptDrop=(this.canAcceptDrop||this.canAcceptDroppedRecords||this.$57p())}
,isc.A.$57p=function isc_TreeGrid__canDragRecordsToSelf(){var _1=this.canReparentNodes;if(_1==null&&this.canAcceptDroppedRecords){if(!this.$587){this.logInfo("'canReparentNodes' is unset. Allowing node reparenting as "+"'canAcceptDroppedRecords' is set to true. For explicit control, "+"use 'canReparentNodes' instead.","dragDrop");this.$587=true}
_1=this.canAcceptDroppedRecords}
return this.canReorderRecords||_1}
,isc.A.getDropError=function isc_TreeGrid_getDropError(_1,_2){for(var i=0,_4=_1.length;i<_4;i++){if(this.data.isDescendantOf(_2,_1[i])){return this.cantDragIntoChildMessage}}
var _5=this.data.isFolder(_2);if(_5){for(i=0;i<_4;i++){if(_1[i]==_2){return this.cantDragIntoSelfMessage}}}
return null}
,isc.A.dropMove=function isc_TreeGrid_dropMove(){var _1=this.getEventRow();if(_1==-1)return;var _2=(_1==-2?this.data.getRoot():this.data.get(_1)),_3=this.getDropFolder(),_4=(this.canReorderRecords?this.getReorderPosition(_1):null);if(_3!=this.lastDropFolder||_2!=this.$588||_4!=this.$589){if(!this.$59a){this.$59a=this.getID()+".openDropFolder()"}
if(this.openDropFolderTimer)isc.Timer.clear(this.openDropFolderTimer);if(!this.data.isOpen(_3)){this.openDropFolderTimer=isc.Timer.setTimeout(this.$59a,this.openDropFolderDelay)}
this.updateDropFolder(_3)}
if(!this.willAcceptDrop()){this.body.setNoDropIndicator()}else{this.body.clearNoDropIndicator()}
if(this.canReorderRecords){if(this.data.isOpen(_3))this.showDragLineForRecord(_1,_4);else this.hideDragLine()}
this.$588=_2;this.$589=_4}
,isc.A.getDropFolder=function isc_TreeGrid_getDropFolder(){var _1=this.getEventRow(),_2=this.data,_3=(_1<0?_2.getRoot():_2.get(_1));if(_2.isRoot(_3))return _2.getRoot();var _4=_2.isFolder(_3);if(!this.canReorderRecords)return(_4?_3:_2.getParent(_3));var _5=this.getReorderPosition(_3);if(!_4||_5==isc.ListGrid.BEFORE||(_5==isc.ListGrid.AFTER&&(!_2.isOpen(_3)||!_2.hasChildren(_3))))
{return _2.getParent(_3)}else{return _3}}
,isc.A.openDropFolder=function isc_TreeGrid_openDropFolder(){var _1=this.lastDropFolder;if(!_1||!this.data.isFolder(_1)||this.data.isOpen(_1))return false;this.openFolder(_1);if(this.canReorderRecords)
this.showDragLineForRecord(this.data.indexOf(_1),isc.ListGrid.OVER)}
,isc.A.getReorderPosition=function isc_TreeGrid_getReorderPosition(_1,_2,_3,_4,_5){if(_2==null)_2=this.body.getOffsetY();if(_1==null)_1=this.getEventRow(_2);var _6=this.data;if(!isc.isA.Number(_1))_1=_6.indexOf(_1);var _7=_6.get(_1);if(_7&&_6.isFolder(_7)){var _8=_2-this.body.getRowTop(_1),_9=this.body.getRowSize(_1);if(_8<(_9/ 4)){return isc.ListGrid.BEFORE}else if(_8>(3*_9/ 4)){return isc.ListGrid.AFTER}else{return isc.ListGrid.OVER}}
return this.invokeSuper(isc.TreeGrid,"getReorderPosition",_1,_2,_3,_4,_5)}
,isc.A.showDragLineForRecord=function isc_TreeGrid_showDragLineForRecord(_1,_2,_3,_4,_5){if(_1==null)_1=this.getEventRecordNum();if(_2==null)_2=this.getReorderPosition(_1);if(_2==isc.ListGrid.OVER){var _6=this.getRecord(_1),_7=this.data;if(_7.isFolder(_6)&&_7.isOpen(_6))_2=isc.ListGrid.AFTER}
return this.invokeSuper(isc.TreeGrid,"showDragLineForRecord",_1,_2,_3,_4,_5)}
,isc.A.dropOut=function isc_TreeGrid_dropOut(){this.hideDragLine();this.body.clearNoDropIndicator();this.$588=null;this.updateDropFolder();if(this.openDropFolderTimer)isc.Timer.clear(this.openDropFolderTimer)}
,isc.A.updateDropFolder=function isc_TreeGrid_updateDropFolder(_1){var _2=this.lastDropFolder;this.lastDropFolder=_1;if(_1){_1.$59b=this.body.willAcceptDrop(_1)
this.setRowIcon(_1,this.getIcon(_1))}
if(_2&&_2!=_1){delete _2.$59b;this.setRowIcon(_2,this.getIcon(_2))}}
,isc.A.transferSelectedData=function isc_TreeGrid_transferSelectedData(_1,_2,_3,_4){if(!this.isValidTransferSource(_1)){if(_4)this.fireCallback(_4);return}
if(_3==null)_3=0;if(_2==null)_2=this.data.getRoot();var _5=_1.cloneDragData();this.transferNodes(_5,_2,_3,_1,_4)}
,isc.A.drop=function isc_TreeGrid_drop(){if(!this.willAcceptDrop())return false;var _1=isc.EH.dragTarget.cloneDragData(),_2=this.getEventRecordNum(),_3=this.getReorderPosition(_2),_4=this.data.get(_2)||this.data.getRoot(),_5=this.getDropFolder();var _6=isc.EH.dragTarget.getData(),_7=(isc.isA.Tree(_6)&&isc.isA.Tree(this.data)&&_6.getRoot()==this.data.getRoot());for(var i=0;i<_1.length;i++){var _9=_1[i];var _10=(this.data.findChildNum(_5,this.data.getName(_9))!=-1);var _11=_7&&this.canReorderRecords&&_5==this.data.getParent(_9);if(_10&&!_11){this.logInfo("already a child named: "+this.data.getName(_9)+" under parent: "+this.data.getPath(_5));isc.warn(this.parentAlreadyContainsChildMessage);return false}}
var _12=null;if(this.canReorderRecords){if(_2<0){_5=_4;_12=this.data.getChildren(_5).getLength()}else if(_4==_5){_12=0}else{_12=(_3==isc.ListGrid.AFTER?1:0)+this.data.getChildren(_5).indexOf(_4)}}
if(this.onFolderDrop!=null&&(this.onFolderDrop(_1,_5,_12,isc.EH.dragTarget)==false))return false;this.folderDrop(_1,_5,_12,isc.EH.dragTarget);this.data.openFolder(_5);return false}
,isc.A.folderDrop=function isc_TreeGrid_folderDrop(_1,_2,_3,_4,_5){this.transferNodes(_1,_2,_3,_4,_5)}
,isc.A.transferNodes=function isc_TreeGrid_transferNodes(_1,_2,_3,_4,_5){if(!this.$4c("transferNodes",_1,_2,_3,_4,_5)){return}
_2=_2||this.data.root;var _6=_4.getData(),_7=(isc.isA.Tree(_6)&&isc.isA.Tree(this.data)&&_6.getRoot()==this.data.getRoot());var _8=this.getDataSource(),_9=_4.getDataSource();if(_7&&(this.dragDataAction!=isc.TreeGrid.COPY&&this.dragDataAction!=isc.TreeGrid.CLONE))
{if(_8!=null&&this.data!=null&&isc.ResultTree&&isc.isA.ResultTree(this.data))
{this.$4l[0].noRemove=true;var _10=isc.rpc.startQueue();var _11=_6.getChildren(_2);var _12,_13;if(_3!=null){if(_3<_11.length){_12=_11[_3]}}
if(_12==_13){_12=_11[_11.length-1]}
for(var i=0;i<_1.length;i++){var _15=_1[i];if(this.shouldSaveLocally()||_15[this.data.parentIdField]==_2[this.data.idField])
{if(_3!=null){_11=_6.getChildren(_2);_6.move(_15,_2,_11.indexOf(_12))}}else{var _16=_1[i]["_isOpen_"+this.data.ID];var _15=isc.addProperties({},this.data.getCleanNodeData(_1[i],true,false)),_17=isc.addProperties({},_15);if(_16!=null)_15["_isOpen_"+this.data.ID]=_16;_15[this.data.parentIdField]=_2[this.data.idField];var _18=null,_19=this.data.getChildren(_2);if(_3==null){_18=_19.get(_19.length-1)}else if(_3>0){_18=_19.get(_3-1)}
this.updateDataViaDataSource(_15,_8,{oldValues:_17,parentNode:this.data.getParent(_1[i]),newParentNode:_2,dragTree:_6,draggedNode:_15,draggedNodeList:_1,dropNeighbor:_18,dropIndex:_3},_4)}}}else{_6.moveList(_1,_2,_3)}}else if(_8!=null){var _20;if(this.dragRecategorize||(_9!=null&&_9!=_8&&this.data!=null&&isc.ResultTree&&isc.isA.ResultTree(this.data)&&_4.dragDataAction==isc.TreeGrid.MOVE))
{var _21=_9.getTreeRelationship(_8);if(_21!=null&&_21.parentIdField){var _22=false,_23=_9.getPrimaryKeyFields();for(var _24 in _23){if(_24==_21.parentIdField){this.logWarn("dragRecategorize: data source has dataSource:"+_9.getID()+". foreignKey relationship with "+"target dataSource "+_8.getID()+" is based on primary key which cannot be modified.");_22=true}}
if(!_22)_20=true;this.logInfo("Recategorizing dropped nodes in dataSource:"+_9.getID())}
this.$4l[0].noRemove=true;var _10=isc.rpc.startQueue();for(var i=0;i<_1.length;i++){var _15={};var _25=_9.getPrimaryKeyFieldNames();for(var j=0;j<_25.length;j++){_15[_25[j]]=_1[i][_25[j]]}
if(_20){_15[_21.parentIdField]=_2[_21.idField]}
isc.addProperties(_15,this.getDropValues(_15,_9,_2,_3,_4));this.updateDataViaDataSource(_15,_9,null,_4)}}else{var _10=isc.rpc.startQueue();for(var i=0;i<_1.length;i++){var _27=_1[i],_28=this.data;if(_28){_27[_28.parentIdField]=_2[_28.idField]}
isc.addProperties(_27,this.getDropValues(_27,_9,_2,_3,_4));this.$4e(_27,_9,_4,null,_3,_2)}}}else{for(var i=0;i<_1.length;i++){this.$4e(_1[i],_9,_4,null,_3,_2)}}
if(!this.$4i){isc.Log.logDebug("Invoking transferDragData from inside transferNodes - no server "+"queries needed?","dragDrop");_4.transferDragData(this.$4j,this);if(_8){if(!this.$4d)isc.rpc.sendQueue()}}
this.$4k=false}
,isc.A.$4n=function isc_TreeGrid__updateComplete(_1,_2,_3){if(!_3.dragTree)return;if(_3.newParentNode!=this.data.root&&_3.dragTree.getParent(_3.newParentNode)==null)
{isc.logWarn("Target folder is no longer in the Tree in TreeGrid cache sync");return}
var _4=_3.dropNeighbor,_5=_3.dragTree,_6=_5.getChildren(_3.newParentNode),_7=_3.draggedNodeList,_8=_5.idField,_9=_7.findIndex(_8,_3.draggedNode[_8]),_10,_11;if(_4==null){_10=0}else{for(var i=0;i<_6.length;i++){var _13=_6[i];if(_13==_4){_10=i+1;break}}}
if(_10!==_11){while(_10<_6.length){var _14=_7.findIndex(_8,_6[_10][_8]);if(_14==-1||_14>_9)break;_10++}}
if(_10===_11){isc.logWarn("Could not order dropped node by reference to neighbor; trying absolute index");_10=_3.dropIndex}
if(_10===_11){isc.logWarn("Unable to determine drop location in TreeGrid cache sync");return}
var _15=this.data.find(_8,_3.draggedNode[_8]);_5.move(_15,this.data.getParent(_15),_10);this.Super("$4n",arguments)}
,isc.A.getTreeCellValue=function isc_TreeGrid_getTreeCellValue(_1,_2,_3,_4){if(_2==null){return _1}
var _5=this.data.getLevel(_2),_6=isc.TreeGrid.$58u(),_7=this.getCellCSSText(_2,_3,_4),_8=this.getCellStyle(_2,_3,_4);_6[1]=_8
_6[3]=_7
if(_6[3]!=null&&!_6[3].endsWith(this.$px))_6[3]+=this.$px;_6[5]=_7;_6[7]=_8;_6[9]=this.getIndentHTML(_5,_2);var _9=this.$586(_1,_2,_3,_4,true,_8,_7);for(var i=0,j=11;i<_9.length;i++){_6[j]=_9[i];j++}
_6[j]=this.$580
return _6.join(isc.emptyString)}
,isc.A.$586=function isc_TreeGrid__getTreeCellTitleArray(_1,_2,_3,_4,_5,_6,_7){if(_7==null)_7=this.getCellCSSText(_2,_3,_4);if(_6==null)_6=this.getCellStyle(_2,_3,_4);var _8=isc.TreeGrid.$58w();_8[1]=_7;_8[3]=_6;if(_5){var _9=this.getOpenIcon(_2),_10=this.openerIconSize||(this.showConnectors?this.cellHeight:null),_11=(_3!=null?this.$6m+_3:null);if(_9){_8[5]=this.getIconHTML(_9,_11,_10)}else{_8[5]=this.$59c(_10||this.iconSize)}}else _8[5]=null;var _12=this.$585(_2),_13=_12||this.getExtraIcon(_2),_14=(_3!=null?this.$6n+_3:null),_15=(_12!=null?this.$53r():this.iconSize),_16=this.extraIconGap,_17=this.getIcon(_2),_18=(_3!=null?this.$58y+_3:null);_8[6]=(_13?this.getIconHTML(_13,_14,_15,_16):null);_8[7]=this.getIconHTML(_17,_18,_2.iconSize);_8[9]=_7;_8[11]=this.iconPadding;_8[13]=_6;_8[15]=this.wrapCells?null:"<NOBR>"
_8[16]=_1;return _8}
,isc.A.getCellAlign=function isc_TreeGrid_getCellAlign(_1,_2,_3){var _4=this.getField(_3);if(_4&&_4.treeField){return this.isRTL()?"right":"left"}
return this.Super("getCellAlign",arguments)}
,isc.A.getCellValue=function isc_TreeGrid_getCellValue(_1,_2,_3,_4,_5,_6,_7){var _8=this.invokeSuper(isc.TreeGrid,"getCellValue",_1,_2,_3,_4,_5,_6,_7);if(_3==this.getTreeFieldNum()){_8=this.getTreeCellValue(_8,_1,_2,_3)}
return _8}
,isc.A.bodyDrawing=function isc_TreeGrid_bodyDrawing(_1,_2,_3,_4,_5){this.$59d={};return this.invokeSuper(isc.TreeGrid,"bodyDrawing",_2,_3,_4,_5)}
,isc.A.getNodeTitle=function isc_TreeGrid_getNodeTitle(_1,_2,_3){if(_3.name&&_3.name!=this.$58z){if(_2==-1)return _1[_3.name];return this.getEditedRecord(_2)[_3.name]}
return this.data.getTitle(_1)}
,isc.A.getTitleField=function isc_TreeGrid_getTitleField(){if(this.titleField!=null)return this.titleField;return this.getFieldName(this.getTreeFieldNum())}
,isc.A.getTreeFieldNum=function isc_TreeGrid_getTreeFieldNum(){return this.$584}
,isc.A.getOpenAreaWidth=function isc_TreeGrid_getOpenAreaWidth(_1){var _2=this.getOpenerIconSize(_1),_3=(this.showConnectors?_2:this.indentSize);return((this.data.getLevel(_1)-(this.showRoot?0:1))*_3)+_2}
,isc.A.getOpenerIconSize=function isc_TreeGrid_getOpenerIconSize(_1){return(this.openerIconSize||(this.showConnectors?this.cellHeight:this.iconSize))}
,isc.A.clickInOpenArea=function isc_TreeGrid_clickInOpenArea(_1){if(!this.data.isFolder(_1))return false;var _2=this.getTreeFieldNum(),_3=this.getFieldBody(_2),_4=this.getLocalFieldNum(_2),_5=_3.getColumnLeft(_4),_6=_3.getColumnWidth(_4),_7=this.getOpenAreaWidth(_1),x=_3.getOffsetX();if(this.isRTL()){var _9=_5+_6;return x>=(_9-_7)&&x<=_9}else{return x>=_5&&x<_5+_7}}
,isc.A.clickInCheckboxArea=function isc_TreeGrid_clickInCheckboxArea(_1){if(this.selectionAppearance!=this.$22d)return false;var _2=this.getTreeFieldNum(),_3=this.getFieldBody(_2),_4=this.getLocalFieldNum(_2),_5=_3.getColumnLeft(_4),_6=_3.getColumnWidth(_4),_7=this.getOpenAreaWidth(_1),_8=this.$53r(),x=_3.getOffsetX();if(this.isRTL()){var _10=_5+_6;return(x>=(_10-_7-_8)&&x<=(_10-_7))}else{return(x>=(_5+_7)&&x<(_5+_7+_8))}}
,isc.A.getIndentHTML=function isc_TreeGrid_getIndentHTML(_1,_2){var _3=_1;if(!this.showRoot)_3--;var _4=(this.showConnectors?this.getOpenerIconSize(_2):this.indentSize);if(this.showConnectors&&this.showFullConnectors){var _5=this.data.$44p(_2);_5.remove(_1);if(!this.showRoot)_5.remove(0);if(_5.length!=0){if(!this.$59e){var _6=isc.Img.urlForState(this.connectorImage,null,null,"ancestor"),_7=this.getIconHTML(_6,null,this.cellHeight);this.$59f=_7}
var _8=this.$59c(_4),_9=isc.StringBuffer.create(isc.emptyString);_9.append("<NOBR>");for(var i=(this.showRoot?0:1);i<_1;i++){if(_5.contains(i))_9.append(this.$59f);else _9.append(_8)}
_9=_9.release();return _9}}
var _11=this.$59c(_3*_4);if(isc.Browser.isIE9||(isc.Browser.isStrict&&(isc.Browser.isIE7||isc.Browser.isIE8))){_11="<NOBR>"+_11+"</NOBR>"}
return _11}
,isc.A.$59c=function isc_TreeGrid__indentHTML(_1){if(_1==0)return isc.emptyString;var _2=isc.TreeGrid.$59g;if(_2==null)_2=isc.TreeGrid.$59g={};if(_2[_1]==null)_2[_1]=isc.Canvas.spacerHTML(_1,1);return _2[_1]}
,isc.A.getOpenIcon=function isc_TreeGrid_getOpenIcon(_1){if(this.showOpener==false)return null;if(!this.data)return null;if(isc.isA.Number(_1))_1=this.data.get(_1);if(_1==null)return null;if(_1.openIcon){return _1.openIcon}else{var _2=this.data.isFolder(_1),_3=_2,_4=_2,_5,_6;if(_2){var _7=this.data.getLoadState(_1);if(_7==isc.Tree.UNLOADED||(_7==isc.Tree.FOLDERS_LOADED&&this.displayNodeType!=isc.Tree.FOLDERS_ONLY))
{_3=true;_4=false}else{_3=this.data.hasChildren(_1,this.displayNodeType);_4=_3&&this.data.isOpen(_1)}}
if(_4&&!this.showFullConnectors)_6=true
else{_6=!this.$59h(_1)}
_5=!this.$59i(_1);return this.getOpenerImageURL(_3,_4,_5,_6)}}
,isc.A.$59i=function isc_TreeGrid__shouldShowPreviousLine(_1){var _2=this.data.indexOf(_1);if(_2==0)return false;if(this.showFullConnectors)return true;var _3=this.getRecord(_2-1),_4=this.data.getParent(_1);if(_3==null)return false;return(_4==_3||_4==this.data.getParent(_3))}
,isc.A.$59h=function isc_TreeGrid__shouldShowNextLine(_1){if(this.showFullConnectors){var _2=this.data,_3=_2.getParent(_1),_4=_2.getChildren(_3);return _4.indexOf(_1)!=_4.length-1}
var _5=this.data.indexOf(_1),_6=this.getRecord(_5+1);if(_6==null)return false;return(this.data.getParent(_1)==this.data.getParent(_6))}
,isc.A.getOpenerImageURL=function isc_TreeGrid_getOpenerImageURL(_1,_2,_3,_4){if(!this.$59j){var _5=this.openerImage;this.$59j={opened:isc.Img.urlForState(_5,null,null,"opened"),closed:isc.Img.urlForState(_5,null,null,(this.isRTL()?"closed_rtl":"closed")),opening:isc.Img.urlForState(_5,null,null,"opening")}}
if(this.showConnectors&&!this.$59k){var _5=this.connectorImage,_6=["single","start","end","middle","opened_single","opened_start","opened_middle","opened_end","closed_single","closed_start","closed_middle","closed_end"],_7={},_8=this.isRTL(),_9="$59l";for(var i=0;i<_6.length;i++){var _11=_6[i],_12=_11;if(_8)_12+=_9;_7[_11]=isc.Img.urlForState(_5,null,null,_12)}
this.$59k=_7}
if(this.showConnectors){var _13=this.$59k;if(_1){if(_2){if(!this.showFullConnectors){if(_3)return _13.opened_single;return _13.opened_end}
if(_3&&_4)return _13.opened_single;else if(_3)return _13.opened_start;else if(_4)return _13.opened_end;else return _13.opened_middle}else{if(_3&&_4)return _13.closed_single;if(_3)return _13.closed_start;if(_4)return _13.closed_end;return _13.closed_middle}}else{if(_3&&_4)return _13.single;if(_3)return _13.start;if(_4)return _13.end;return _13.middle}}else{var _13=this.$59j;if(!_1)return null;if(_2)return _13.opened;return _13.closed}}
,isc.A.$585=function isc_TreeGrid__getCheckboxIcon(_1){var _2=null;if(this.selectionAppearance==this.$22d){var _3=this.selection.isSelected(_1)?true:false;var _4=(_3&&this.showPartialSelection&&this.selection.isPartiallySelected(_1))?true:false;_2=_4?(this.checkboxFieldPartialImage||this.booleanPartialImage):_3?(this.checkboxFieldTrueImage||this.booleanTrueImage):(this.checkboxFieldFalseImage||this.booleanFalseImage);if(!this.body.canSelectRecord(_1)){if(this.showDisabledSelectionCheckbox){_2=isc.Img.urlForState(_2,null,null,"Disabled")}else{_2="[SKINIMG]/blank.gif"}}}
return _2}
,isc.A.getExtraIcon=function isc_TreeGrid_getExtraIcon(_1){return null}
,isc.A.getIcon=function isc_TreeGrid_getIcon(_1,_2){if(isc.isA.Number(_1))_1=this.data.get(_1);if(!_1)return null;var _3=_1[this.customIconProperty],_4=(_3!=null),_5=this.data.isFolder(_1);if(!_4){if(_5)_3=this.folderIcon;else _3=this.nodeIcon}
var _6;if(_5){var _7=_2?false:(this.lastDropFolder==_1&&_1.$59b),_8=_2?false:!!this.data.isOpen(_1);if(_7){if(_1.dropIcon!=null)_3=_1.dropIcon;else if(!_4&&this.folderDropImage!=null)_3=this.folderDropImage;else{var _9;if(_4){_9=_1[this.customIconDropProperty];if(_9==null)_9=this.showCustomIconDrop}else{_9=this.showDropIcons}
if(_9)_6=this.dropIconSuffix}}else if(_8){if(_1.openedIcon!=null)_3=_1.openedIcon;else if(!_4&&this.folderOpenImage!=null)_3=this.folderOpenImage;else{var _10;if(_4){_10=_1[this.customIconOpenProperty];if(_10==null)_10=this.showCustomIconOpen}else{_10=this.showOpenIcons}
if(_10)_6=this.openIconSuffix;else if(!_4)_6=this.closedIconSuffix}}else{if(!_4){if(this.folderClosedImage)_3=this.folderClosedImage;else _6=this.closedIconSuffix}}}else{if(!_4&&this.fileImage)_3=this.fileImage}
return isc.Img.urlForState(_3,false,false,_6)}
,isc.A.getIconHTML=function isc_TreeGrid_getIconHTML(_1,_2,_3,_4){if(_1==null)return isc.emptyString;if(_3==null)_3=this.iconSize;var _5=this.$59d.iconHTML;if(_5==null)_5=this.$59d.iconHTML={};if(_5[_1]==null){var _6;if(_4){var _7=this.$582;_7[1]=_4;_6=_7.join(isc.emptyString)}
var _8=this.$11z;_8.src=_1;_8.width=_8.height=_3;_8.name=_2;_8.align=this.$581;_8.extraStuff=_6;_5[_1]=this.$1i(_8)}
var _9=_5[_1];_9[14]=_2;return _9.join(isc.$ah)}
,isc.A.setRowIcon=function isc_TreeGrid_setRowIcon(_1,_2){if(!isc.isA.Number(_1))_1=this.data.indexOf(_1);if(_1!=-1&&this.getIcon(_1)!=null){this.setImage(this.$58y+_1,_2)}}
,isc.A.setNodeIcon=function isc_TreeGrid_setNodeIcon(_1,_2){_1[this.customIconProperty]=_2;this.setImage(this.$58y+this.getRecordIndex(_1),_2)}
,isc.A.getPrintHTML=function isc_TreeGrid_getPrintHTML(_1,_2){var _3=this.printExpandTree;if(_3==null)_3=_1?_1.expandTrees:null;if(_3&&this.data){if(isc.ResultTree&&isc.isA.ResultTree(this.data)&&this.data.loadDataOnDemand){this.logWarn("Printing TreeGrid with option to expand folders on print not supported "+"for load on demand trees.")}else{this.data.openAll()}}
return this.Super("getPrintHTML",arguments)}
,isc.A.getExportFieldValue=function isc_TreeGrid_getExportFieldValue(_1,_2,_3){var _4=this.Super("getExportFieldValue",arguments);if(_3==this.getTreeFieldNum()&&this.exportIndentString){var _5=this.data.getLevel(_1);while(--_5>0)_4=this.exportIndentString+_4}
return _4}
);isc.B._maxIndex=isc.C+70;isc.TreeGrid.registerStringMethods({folderOpened:"node",folderClosed:"node",folderClick:"viewer,folder,recordNum",leafClick:"viewer,leaf,recordNum",nodeClick:"viewer,node,recordNum",folderContextClick:"viewer,folder,recordNum",leafContextClick:"viewer,leaf,recordNum",nodeContextClick:"viewer,node,recordNum",dataArrived:"parentNode",onFolderDrop:"nodes,folder,index,sourceWidget"});isc.defineInterface("Observer").addInterfaceProperties({registerObserved:isc.ClassFactory.TARGET_IMPLEMENTS,unregisterObserved:isc.ClassFactory.TARGET_IMPLEMENTS});isc.defineInterface("AutoObserver","Observer").addInterfaceProperties({observedName:"observed",observations:{},registerObserved:function(_1){this[this.observedName]=_1;for(var _2 in this.observations){this.observe(_1,_2,this.observations[_2])}
if(isc.isA.Canvas(_1)){this.observe(_1,"destroy","observer.unregisterObserved(observed)")}},unregisterObserved:function(_1){this[this.observedName]=null;for(var _2 in this.observations){this.ignore(_1,_2)}}});isc.defineInterface("GridAutoObserver","AutoObserver").addInterfaceProperties({observedName:"grid"});isc.defineClass("GridTotalRowsIndicator","Label","GridAutoObserver");isc.A=isc.GridTotalRowsIndicator.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.height=1;isc.A.overflow="visible";isc.A.valign="center";isc.A.observations={"dataArrived":"observer.gridDataChanged()","setData":"observer.gridDataChanged()"};isc.A.dynamicContents=true;isc.A.contents="Total Rows: ${this.rowCount}";isc.A.rowCount="N/A";isc.B.push(isc.A.gridDataChanged=function isc_GridTotalRowsIndicator_gridDataChanged(){var _1=this.grid.data;if(!_1)this.rowCount="N/A";if(isc.isA.ResultSet(_1)){if(_1.lengthIsKnown())this.rowCount=_1.getLength();else this.rowCount="N/A"}else if(isc.isAn.Array(_1)){this.rowCount=_1.getLength()}
this.markForRedraw()}
);isc.B._maxIndex=isc.C+1;isc.defineClass("ObserverToolStrip","ToolStrip");isc.A=isc.ObserverToolStrip.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.initWidget=function isc_ObserverToolStrip_initWidget(){this.Super("initWidget",arguments);for(var i=0;i<this.members.length;i++){var m=this.members[i];if(isc.isAn.Observer(m)){m.registerObserved(this.grid)}}}
);isc.B._maxIndex=isc.C+1;isc.defineClass("GridToolStrip","ObserverToolStrip");isc.A=isc.GridToolStrip.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.membersMargin=5;isc.A.addButtonDefaults={_constructor:"Img",size:16,layoutAlign:"center",src:"[SKIN]/actions/add.png",click:"this.creator.grid.startEditingNew()"};isc.A.removeButtonDefaults={_constructor:"Img",size:16,layoutAlign:"center",src:"[SKIN]/actions/remove.png",click:"this.creator.grid.removeSelectedData()"};isc.A.refreshButtonDefaults={_constructor:"Img",size:16,layoutAlign:"center",src:"[SKIN]/actions/refresh.png",click:"this.creator.grid.invalidateCache()"};isc.A.exportButtonDefaults={_constructor:"IButton",title:"Export to CSV",layoutAlign:"center",click:"this.creator.grid.exportData()"};isc.A.totalRowsIndicatorDefaults={_constructor:"GridTotalRowsIndicator",layoutAlign:"center"};isc.A.members=["autoChild:removeButton","autoChild:addButton","autoChild:exportButton","starSpacer","autoChild:refreshButton","autoChild:totalRowsIndicator"];isc.B.push(isc.A.initWidget=function isc_GridToolStrip_initWidget(){this.Super("initWidget",arguments)}
);isc.B._maxIndex=isc.C+1;isc.ClassFactory.defineClass("RecordEditor","ListGrid");isc.A=isc.RecordEditor.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.autoDraw=false;isc.A.cellSpacing=0;isc.A.cellPadding=0;isc.A.selectionType="none";isc.A.showRollOver=false;isc.A.baseStyle="recordEditorCell";isc.A.showHeader=false;isc.A.showEmptyMessage=false;isc.A.bodyOverflow="hidden";isc.A.fixedRecordHeights=true;isc.A.drawAllMaxCells=0;isc.A.skinImgDir="images/RecordEditor/";isc.A.saveImg={src:"[SKIN]add.png",width:16,height:16,showOver:true};isc.A.filterImg={src:"[SKIN]filter.png",width:16,height:16,showOver:true};isc.A.actionButtonStyle="normal";isc.A.listEndEditAction="next";isc.A.canEdit=true;isc.A.editByCell=false;isc.A.$k6=false;isc.A.bodyStyleName="normal";isc.A.styleName="normal";isc.B.push(isc.A.shouldAutoFitField=function isc_RecordEditor_shouldAutoFitField(){return false}
,isc.A.$55s=function isc_RecordEditor__showEditClickMask(){}
);isc.B._maxIndex=isc.C+2;isc.A=isc.RecordEditor.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$59m="filter";isc.B.push(isc.A.initWidget=function isc_RecordEditor_initWidget(){if(this.sourceWidget!=null){var _1=this.sourceWidget;this.setWidth(_1.getInnerContentWidth());this.observe(_1,"resized","observer.sourceWidgetResized(observed)");this.leaveScrollbarGap=this.sourceWidget.leaveScrollbarGap;if(this.isAFilterEditor()){this.actOnCellChange=this.sourceWidget.filterByCell;this.actOnKeypress=this.sourceWidget.filterOnKeypress}else{this.actOnCellChange=this.sourceWidget.saveByCell}
isc.addProperties(this.editFormDefaults,{autoFocus:false});this.fieldIDProperty=this.sourceWidget.fieldIDProperty;this.fields=this.sourceWidget.completeFields.duplicate();this.cellHeight=this.getInnerHeight()}else{this.logWarn("RecordEditor initialized without a sourceWidget property. "+"This widget is not supported as a standalone component.")}
return this.Super(this.$sb)}
,isc.A.destroy=function isc_RecordEditor_destroy(){this.ignore(this.sourceWidget,"resized");this.Super("destroy",arguments)}
,isc.A.sourceWidgetResized=function isc_RecordEditor_sourceWidgetResized(_1){this.setWidth(_1.getInnerContentWidth())}
,isc.A.isAFilterEditor=function isc_RecordEditor_isAFilterEditor(){return(this.actionType==this.$59m)}
,isc.A.$32v=function isc_RecordEditor__formatCellValue(_1,_2,_3,_4,_5){if(_3.isRemoveField)return"&nbsp;"
return this.Super("$32v",arguments)}
,isc.A.$4t=function isc_RecordEditor__useDisplayFieldValue(_1){return false}
,isc.A.draw=function isc_RecordEditor_draw(){var _1=this.sourceWidget;this.setWidth(_1.getInnerContentWidth());var _2=this.findNextEditCell(0,0,1,true,true,false,true,true);if(_2==null){this.logWarn("No editable fields in this record editor."+(this.isAFilterEditor()?" Check the 'canFilter' property for each field in ":" Check the 'canEdit' property for each field in ")+this.sourceWidget.getID())}else{var _3=this.isAFilterEditor(),_4;if(_3){_4=this.sourceWidget.$55a()}else{var _5;_4={};for(var i=0;i<this.fields.length;i++){var _7=this.fields[i];if(_7.defaultValue!==_5){_4[_7[this.fieldIdProperty]]=_7.defaultValue}}}
if(this.isAFilterEditor())this.setValuesAsCriteria(_4);else this.setEditValues(0,_4);var _8=_2[1];this.$53j(0,_8)}
this.Super("draw",arguments);this.$59n.bringToFront()}
,isc.A.setFields=function isc_RecordEditor_setFields(){this.Super("setFields",arguments);var _1=this.findNextEditCell(0,0,1,true,true);if(_1==null)_1=[0,0];if(this.isDrawn()&&!this.$49j)this.$53j(0,_1[1])}
,isc.A.createChildren=function isc_RecordEditor_createChildren(){this.Super("createChildren",arguments);var _1=this.getEditForm();if(_1!=null&&this.$59o){_1.setValuesAsCriteria(this.$59o);delete this.$59o;if(this.isAFilterEditor())_1.isSearchForm=true}
if(!this.$59n)this.makeActionButton()}
,isc.A.makeActionButton=function isc_RecordEditor_makeActionButton(){var _1;if(this.isAFilterEditor()){_1=this.filterImg}else{_1=this.saveImg}
this.$59n=isc.Button.create({recordEditor:this,left:this.getInnerWidth()-this.getScrollbarSize(),width:this.getScrollbarSize(),autoDraw:false,baseStyle:this.actionButtonStyle,skinImgDir:this.skinImgDir,icon:_1.src,showRollOverIcon:_1.showOver,showFocusedIcon:_1.showFocused,showFocusedAsOver:false,prompt:this.actionButtonPrompt,click:function(){this.recordEditor.performAction()}},this.actionButtonDefaults,this.actionButtonProperties);this.addChild(this.$59n)}
,isc.A.performAction=function isc_RecordEditor_performAction(_1){if(this.isAFilterEditor())this.performFilter(_1);else this.performSave(_1)}
,isc.A.setValuesAsCriteria=function isc_RecordEditor_setValuesAsCriteria(_1,_2){var _3=this.getEditForm();if(_3==null){this.$59o=_1;return}
_3.setValuesAsCriteria(_1);if(_2)this.refreshRow(0)}
,isc.A.makeEditForm=function isc_RecordEditor_makeEditForm(){var _1=this.editFormProperties=this.editFormProperties||{};if(_1.allowExpressions==null&&this.allowFilterExpressions!=null)
_1.allowExpressions=this.allowFilterExpressions;_1.expressionDataSource=this.expressionDataSource;var _2=this.getEditForm(),_3;if(_2==null){_3=this.$59o}else{_3=_2.getValuesAsCriteria();_2.expressionDataSource=this.expressionDataSource}
this.Super("makeEditForm",arguments);_2=this.getEditForm();if(_2!=null){_2.setValuesAsCriteria(_3);delete this.$59o;if(this.isAFilterEditor())_2.isSearchForm=true}}
,isc.A.updateDataSource=function isc_RecordEditor_updateDataSource(_1){this.expressionDataSource=_1;var _2=this.getEditForm();if(_2)_2.expressionDataSource=this.expressionDataSource}
,isc.A.getEditDisplayValue=function isc_RecordEditor_getEditDisplayValue(_1,_2,_3){if(this.isAFilterEditor()&&this.$6l!=null){var _4=this.getFieldName(_2);return this.$6l.getValue(_4)}
return this.Super("getEditDisplayValue",arguments)}
,isc.A.performFilter=function isc_RecordEditor_performFilter(_1){var _2=this.$59p(this.sourceWidget.autoFetchTextMatchStyle);var _3={};if(_1)_3.showPrompt=false;_3.textMatchStyle=this.sourceWidget.autoFetchTextMatchStyle;var _4=this.sourceWidget.data;if((isc.isA.ResultSet(_4)&&_4.willFetchData(_2,_3.textMatchStyle))||(isc.isA.Tree(_4)&&this.sourceWidget.dataSource!=null))
{this.fireOnPause("performFilter",{target:this.sourceWidget,methodName:"handleFilterEditorSubmit",args:[_2,_3]},this.fetchDelay)}else{this.sourceWidget.handleFilterEditorSubmit(_2,_3)}}
,isc.A.$59p=function isc_RecordEditor__getFilterCriteria(_1){var _2=this.getEditForm();if(_2==null){return this.$59o}
return _2.getValuesAsCriteria(null,_1)}
,isc.A.performSave=function isc_RecordEditor_performSave(_1){var _2=this.getEditRow(),_3=this.getEditCol(),_4=this.getFieldName(_3),_5=this.$6l.getValue(_4);this.setEditValue(_2,_3,_5);var _6=this.getEditValues(0),_7=this.getFields().getProperty(this.fieldIdProperty);if(!this.validateRowValues(_6,{},0,_7))return;this.$55x(0);this.$53j(0,0);for(var _4 in _6){this.refreshCell(0,_3)}}
,isc.A.getValues=function isc_RecordEditor_getValues(){var _1=this.getEditCol(),_2=this.getEditFieldName();this.setEditValue(0,_1,this.$6l.getValue(_2));var _3=this.$59p();return _3}
,isc.A.getValuesAsCriteria=function isc_RecordEditor_getValuesAsCriteria(_1){var _2=this.getEditCol(),_3=this.getEditFieldName();var _4=this.$59p(_1);return _4}
,isc.A.canEditCell=function isc_RecordEditor_canEditCell(_1,_2){if(this.isAFilterEditor()){var _3=this.getField(_2);if(_3==null)return false;return(_3.canFilter!=false)}else{return this.sourceWidget.canEditCell(_1,_2)}}
,isc.A.getEditorValueMap=function isc_RecordEditor_getEditorValueMap(_1,_2){if(this.isAFilterEditor()){return this.sourceWidget.getFilterEditorValueMap(_1)}else{return this.sourceWidget.getEditorValueMap(_1,_2)}}
,isc.A.getEditorType=function isc_RecordEditor_getEditorType(_1,_2){if(this.isAFilterEditor()){return this.sourceWidget.getFilterEditorType(_1)}else{return this.sourceWidget.getEditorType(_1,_2)}}
,isc.A.$59q=function isc_RecordEditor__editorChanged(){this.form.grid.editorChanged(this)}
,isc.A.editorChanged=function isc_RecordEditor_editorChanged(_1){var _2=_1.actOnKeypress!=null?_1.actOnKeypress:this.actOnKeypress;if(_2){this.performAction(true)}}
,isc.A.getEditorProperties=function isc_RecordEditor_getEditorProperties(_1){var _2={height:this.cellHeight};if(_1.displayField&&!_1.optionDataSource)
_2.optionDataSource=this.sourceWidget.dataSource;if(this.isAFilterEditor()){_2.allowEmptyValue=true;_2.changed=this.$59q;_2.actOnKeypress=_1.filterOnKeypress;return isc.addProperties(_2,this.sourceWidget.getFilterEditorProperties(_1))}else{return isc.addProperties(_2,this.sourceWidget.getEditorProperties(_1))}}
,isc.A.getEditItem=function isc_RecordEditor_getEditItem(_1,_2,_3,_4,_5,_6){var _7=this.Super("getEditItem",arguments);if(!this.isAFilterEditor())return _7;var _8=this.sourceWidget.getFilterEditorProperties(_1),_9;if(!_8)_8={};if(_1.defaultValue!=null&&_8.defaultValue===_9){delete _7.defaultValue}
if(_1.change!=null&&_8.change===_9){delete _7.change}
if(_1.defaultDynamicValue!=null&&_8.defaultDynamicValue!=null)
{delete _7.defaultDynamicValue}
if(_1.icons!=null&&_8.icons===_9){delete _7.icons}
if(_1.showPickerIcon!=null&&_8.showPickerIcon===_9){delete _7.showPickerIcon}
return _7}
,isc.A.getDefaultEditValue=function isc_RecordEditor_getDefaultEditValue(_1,_2){if(this.isAFilterEditor())return null;return this.Super("getDefaultEditValue",arguments)}
,isc.A.cellEditEnd=function isc_RecordEditor_cellEditEnd(_1,_2){if(_1!=isc.ListGrid.ENTER_KEYPRESS&&_1!=isc.ListGrid.TAB_KEYPRESS&&_1!=isc.ListGrid.SHIFT_TAB_KEYPRESS&&_1!=isc.ListGrid.EDIT_FIELD_CHANGE)return true;var _3;if(_2===_3)_2=this.getUpdatedEditorValue();var _4=this.getEditRow(),_5=this.getEditCol();this.setEditValue(_4,_5,_2);if(_1==isc.ListGrid.ENTER_KEYPRESS||this.actOnCellChange){this.performAction();if(_1==isc.ListGrid.ENTER_KEYPRESS)return}
var _6=this.getNextEditCell(_4,_5,_1);if(_6==null||_6[0]!=_4){if(_1==isc.ListGrid.TAB_KEYPRESS){this.$59n.focus()}else{this.$l5(false)}
return}
return this.Super("cellEditEnd",arguments)}
,isc.A.clearEditValue=function isc_RecordEditor_clearEditValue(_1,_2){return this.Super("clearEditValue",[_1,_2,null,true])}
,isc.A.layoutChildren=function isc_RecordEditor_layoutChildren(){this.Super("layoutChildren",arguments);if(this.$59n)
this.$59n.setLeft(this.getInnerWidth()-this.getScrollbarSize())
if(this.body){var _1=this.body;if(this.bodyLayout)_1=this.bodyLayout;_1.setWidth(this.getInnerWidth()-this.getScrollbarSize())}
if(this.$59n&&this.body)this.$59n.$xc(this.body)}
,isc.A.adjustOverflow=function isc_RecordEditor_adjustOverflow(){this.Super("adjustOverflow",arguments);if(this.$59n){this.$59n.setHeight(this.body.getVisibleHeight())}}
,isc.A.bodyScrolled=function isc_RecordEditor_bodyScrolled(_1,_2){this.Super("bodyScrolled",arguments);if(this.$59r!=null){isc.Timer.clear(this.$59r)}
this.$59r=this.delayCall("syncSourceScrolling",[],0)}
,isc.A.syncSourceScrolling=function isc_RecordEditor_syncSourceScrolling(){var _1=this.body.getScrollLeft();if(this.sourceWidget&&this.sourceWidget.body&&this.sourceWidget.body.getScrollLeft()!=_1){this.sourceWidget.body.scrollTo(_1)}}
,isc.A.getFieldWidths=function isc_RecordEditor_getFieldWidths(){var _1=this.sourceWidget.getFieldWidths();if(isc.isA.Array(_1))_1=_1.duplicate();return _1}
,isc.A.$59s=function isc_RecordEditor__correctForActionButtonClipping(){return(this.sourceWidget&&this.sourceWidget.body&&!this.sourceWidget.body.vscrollOn)}
,isc.A.getEditFormItemFieldWidths=function isc_RecordEditor_getEditFormItemFieldWidths(_1){var _2=this.Super("getEditFormItemFieldWidths",arguments);if(this.$59s()){var _3=_2.sum(),_4=this.getInnerWidth()-this.getScrollbarSize();if(_3>_4){_2[_2.length-1]-=Math.min(this.getScrollbarSize(),(_3-_4))}}
return _2}
,isc.A.getCellAlign=function isc_RecordEditor_getCellAlign(_1,_2,_3,_4){if(!_4&&_3==this.fields.getLength()-1){return isc.Browser.isRTL?"right":"left"}
return this.Super("getCellAlign",arguments)}
,isc.A.getCellValue=function isc_RecordEditor_getCellValue(_1,_2,_3,_4){var _5=this.fields[_3];if(_5&&(this.isCheckboxField(_5)||this.isExpansionField(_5)||this.isRowNumberField(_5)))
return"&nbsp;"
var _6=this.Super("getCellValue",arguments);if(_3==this.fields.getLength()-1){var _5=this.getField(_3),_7=this.getEditForm(),_8=_7?_7.getItem(_5.name):null,_9=(_8!=null)?_8.getWidth():null;if(_9!=null){_6="<div style='text-align:"+this.getCellAlign(_1,_2,_3,true)+";width:"+_9+"px;'>"+_6+"</div>"}}
return _6}
,isc.A.rebuildForFreeze=function isc_RecordEditor_rebuildForFreeze(){}
,isc.A.rowClick=function isc_RecordEditor_rowClick(){}
,isc.A.rowDoubleClick=function isc_RecordEditor_rowDoubleClick(){}
);isc.B._maxIndex=isc.C+42;isc.ClassFactory.defineClass("Menu","ListGrid");isc.A=isc.Menu;isc.A.standardFields={icon:"ICON_FIELD",title:"TITLE_FIELD",key:"KEY_FIELD",subMenu:"SUBMENU_FIELD"};isc.A.ICON_FIELD={name:"icon",width:25,getCellValue:function(_1,_2){return _1.getIcon(_2)}};isc.A.TITLE_FIELD={name:"title",width:"*",getCellValue:function(_1,_2){return _1.getItemTitle(_2)}};isc.A.KEY_FIELD={name:"keys",width:35,getCellValue:function(_1,_2){return _1.getKeyTitle(_2)}};isc.A.SUBMENU_FIELD={name:"submenu",width:18,getCellValue:function(_1,_2){return _1.getSubmenuImage(_2)}};isc.A.$l2=[];isc.A.SHIFT=(isc.Browser.isWin?"Shift+":"shift-");isc.A.CTRL=(isc.Browser.isWin?"Ctrl+":"ctrl-");isc.A.ALT=(isc.Browser.isWin?"Alt+":"option-");isc.A.META=(isc.Browser.isWin?"Windows+":"command-");isc.A=isc.Menu.getPrototype();isc.A.fixedIconWidth=true;isc.A.styleName="normal";isc.A.bodyStyleName="normal";isc.A.submenuDelay=200;isc.A.submenuOffset=-4;isc.A.defaultWidth=150;isc.A.defaultHeight=20;isc.A.enforceMaxHeight=true;isc.A.maxHeight=null;isc.A.backgroundColor=null;isc.A.overflow=isc.Canvas.VISIBLE;isc.A.bodyOverflow=isc.Canvas.VISIBLE;isc.A.arrowKeyAction="focus";isc.A.selectionType=isc.Selection.NONE;isc.A.autoDraw=false;isc.A.tableStyle="menuTable";isc.A.showRollOver=true;isc.A.showFocusOutline=false;isc.A.showAllRecords=true;isc.A.fixedFieldWidths=false;isc.A.fixedRecordHeights=false;isc.A.leaveScrollbarGap=false;isc.A.baseStyle="menu";isc.A.alternateRecordStyles=false;isc.A.showHeader=false;isc.A.showSortArrow=isc.ListGrid.NONE;isc.A.canDrag=false;isc.A.canAcceptDrop=false;isc.A.canReorderRecords=false;isc.A.useKeys=true;isc.A.showKeys=true;isc.A.showIcons=true;isc.A.showSubmenus=true;isc.A.submenuDirection="right";isc.A.showFieldsSeparately=false;isc.A.emptyMessage="[Empty menu]";isc.A.cellSpacing=0;isc.A.cellPadding=2;isc.A.iconWidth=16;isc.A.iconHeight=16;isc.A.autoSetDynamicItems=true;isc.A.skinImgDir="images/Menu/";isc.A.submenuImage={src:"[SKIN]submenu.gif",width:7,height:7};isc.A.submenuDisabledImage={src:"[SKIN]submenu_disabled.gif",width:7,height:7};isc.A.checkmarkImage={src:"[SKIN]check.gif",width:9,height:9};isc.A.checkmarkDisabledImage={src:"[SKIN]check_disabled.gif",width:9,height:9};isc.A.useBackMask=true;isc.A.submenuInheritanceMask=["submenuConstructor","$59t","className","submenuDelay","submenuOffset","defaultWidth","backgroundColor","tableStyle","showRollOver","baseStyle","emptyMessage","canDrag","canAcceptDrop","canReorderRecords","useKeys","showKeys","showIcons","showSubmenus","submenuDirection","cellPadding","iconWidth","iconHeight","autoSetDynamicItems","skinImgDir","submenuImage","submenuDisabledImage","checkmarkImage","checkmarkDisabledImage","bodyDefaults","itemClick","canSelectParentItems","childrenProperty","inheritedProperties"];isc.A.mergeSingleParent=true;isc.A.autoDismiss=true;isc.A.cascadeAutoDismiss=true;isc.A.autoDismissOnBlur=true;isc.A.fetchSubmenus=true;isc.A=isc.Menu.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$s0="left";isc.A.$s2="right";isc.B.push(isc.A.initWidget=function isc_Menu_initWidget(){this.$59u();this.position=isc.Canvas.ABSOLUTE;if(this.dataSource!=null&&!this.hasFlatDataSource()){var _1=this.initialCriteria||this.criteria;var _2=this.createResultTree(_1,null,{showPrompt:false,dataProperties:{autoOpenRoot:false}});if(this.items)_2.addList(this.items,_2.getRoot());if(this.loadDataOnDemand==false){_2.loadChildren(_2.getRoot(),{caller:this,methodName:"treeDataLoaded"})}
this.data=_2}else if(this.dataSource!=null){var _3=isc.DataSource.get(this.dataSource);_3.fetchData(null,{caller:this,methodName:"flatDataLoaded"})}
if(this.data==null&&this.items!=null)this.data=this.items;if(isc.Tree&&isc.isA.Tree(this.data)){this.$59t=this.data;this.childrenProperty=this.data.childrenProperty;var _4=this.data.getChildren(),_5=this.mergeSingleParent&&!isc.isA.ResultTree(this.$59t)&&_4.length==1&&this.data.hasChildren(_4[0]);if(_5){var _6=_4[0];this.data=this.data.getChildren(_6).duplicate();this.data.add({isSeparator:true});var _7={};isc.addProperties(_7,_6);_7[this.childrenProperty]=null;this.data.add(_7)}else{this.data=null}}
this.Super(this.$sb);if(!this.fields){this.$59v=true;this.fields=[];var _8=(this.submenuDirection==this.$s0);if(_8&&this.showSubmenus)this.fields.add(isc.Menu.SUBMENU_FIELD);if(this.showIcons)this.fields.add(isc.Menu.ICON_FIELD);this.fields.add(isc.Menu.TITLE_FIELD);if(this.showKeys)this.fields.add(isc.Menu.KEY_FIELD);if(!_8&&this.showSubmenus)this.fields.add(isc.Menu.SUBMENU_FIELD)}else{this.$59v=false;for(var i=0;i<this.fields.length;i++){var _10=this.fields[i];if(isc.isA.String(_10)){if(isc.Menu.standardFields[_10]!=null){this.fields[i]=isc.Menu[isc.Menu.standardFields[_10]]}else{this.logWarn("Menu field specified as :"+_10+". This is not a recognized standard field name");this.fields.removeAt(i);i-=1}}}}
if(this.iconBodyStyleName!=null&&this.fields!=null){for(var i=0;i<this.fields.length;i++){if(this.fields[i]=="icon"||this.fields[i]==isc.Menu.ICON_FIELD){this.bodyStyleName=this.iconBodyStyleName;break}}}
if(isc.Browser.isSafari){isc.addProperties(this.submenuImage,{align:"BOTTOM"});isc.addProperties(this.submenuDisabledImage,{align:"BOTTOM"})}
if(this.useKeys)this.setUpKeyListening();if(this.$59t){if(!this.treeParentNode)this.treeParentNode=this.$59t.getRoot();this.setTreeNode(this.treeParentNode)}}
,isc.A.hasFlatDataSource=function isc_Menu_hasFlatDataSource(){var _1=isc.DataSource.get(this.dataSource);var _2=_1.getFieldNames();var _3=false,_4=false;for(var i=0;i<_2.length;i++){var _6=_1.getField(_2[i]);if(_6.primaryKey)_3=true;if(_6.foreignKey)_4=true}
return!(_3&&_4)}
,isc.A.setFields=function isc_Menu_setFields(_1,_2,_3,_4,_5){if(_1&&(_1!=this.fields)){this.$59v=false}
this.invokeSuper(isc.Menu,"setFields",_1,_2,_3,_4,_5)}
,isc.A.treeDataLoaded=function isc_Menu_treeDataLoaded(){this.treeDataArrived(this.$59w)}
,isc.A.setTreeNode=function isc_Menu_setTreeNode(_1){var _2=this.$59t.getLoadState(_1);this.$59w=_1;if(_2==isc.Tree.LOADED){this.treeDataArrived(_1)}else if(_2!=isc.Tree.LOADING){this.$59t.loadChildren(_1,this.getID()+".treeDataArrived(node)");this.$59x=true;this.setData(null)}}
,isc.A.treeDataArrived=function isc_Menu_treeDataArrived(_1){delete this.$59x;if(_1==this.$59w){this.setData(this.$59t.getChildren(_1));if(this.masterMenu&&this.masterMenu.isVisible())
this.masterMenu.placeSubmenu(_1,this)}}
,isc.A.flatDataLoaded=function isc_Menu_flatDataLoaded(_1,_2){this.setData(_2)}
,isc.A.getEmptyMessage=function isc_Menu_getEmptyMessage(){if(this.$59x){return this.loadingDataMessage==null?"&nbsp;":this.loadingDataMessage.evalDynamicString(this,{loadingImage:this.imgHTML(isc.Canvas.loadingImageSrc,isc.Canvas.loadingImageSize,isc.Canvas.loadingImageSize)})}
return this.Super("getEmptyMessage",arguments)}
,isc.A.isEmpty=function isc_Menu_isEmpty(){if(this.$59x)return true;return this.Super("isEmpty",arguments)}
,isc.A.$59u=function isc_Menu__setUpEmptyMessage(){isc.addProperties(this,{emptyMessageTableStyle:this.tableStyle,emptyMessageStyle:this.baseStyle+isc.GridRenderer.standardStyleSuffixes[4]})}
,isc.A.$52r=function isc_Menu__observeData(_1,_2,_3,_4,_5){var _6;if(this.$59t){_6=this.isObserving(this.$59t,"dataChanged");_1=this.$59t}
if(!_6)this.invokeSuper(isc.Menu,"$52r",_1,_2,_3,_4,_5);if(this.autoSetDynamicItems){this.$59y()}}
,isc.A.$52q=function isc_Menu__ignoreData(_1){this.Super("$52q",arguments);if(this.autoSetDynamicItems){delete this.setDynamicItems}}
,isc.A.rowClick=function isc_Menu_rowClick(_1,_2,_3){this.Super("rowClick",arguments);this.selectMenuItem(_2,_3)}
,isc.A.selectMenuItem=function isc_Menu_selectMenuItem(_1,_2){if(_1==null)_1=this.getEventRecordNum();_1=this.getItem(_1);var _3=true;if(_1==null||!this.itemIsEnabled(_1)){isc.Menu.hideAllMenus("itemClick");return false}
if(this.hasSubmenu(_1)&&!this.canSelectParentItems&&!_1.canSelectParent){if(this.submenuTimer)this.submenuTimer=isc.Timer.clear(this.submenuTimer);if(this.$59z!=_1)this.hideSubmenu();this.showSubmenu(_1);return false}
var _4=this;while(_4.$58h){_4=_4.$58h}
if(this.autoDismiss&&(_1.autoDismiss||_1.autoDismiss==null)){isc.Menu.hideAllMenus("itemClick")}
if(_1.action){if(!isc.isA.Function(_1.action)){isc.Func.replaceWithMethod(_1,"action","")}
if(_1.action()==false)return false}
if(_1.click){if(!isc.isA.Function(_1.click)){isc.Func.replaceWithMethod(_1,"click","target,item,menu,colNum")}
var _5=(this.target?this.target:this);_3=_1.click(_5,_1,this,_2)}
if(_3!=false){_3=this.itemClick(_1,_2)}
if(!(this.autoDismiss&&(_1.autoDismiss||_1.autoDismiss==null))){this.refreshRow(this.getRecordIndex(_1))}
return _3}
,isc.A.mouseOver=function isc_Menu_mouseOver(){var _1=this.$58h;if(_1&&_1.body.lastOverRow!=this.$590){if(_1.submenuTimer)_1.submenuTimer=isc.Timer.clear(_1.submenuTimer);_1.$18m(this.$590)}}
,isc.A.rowOver=function isc_Menu_rowOver(_1,_2){if(this.submenuTimer)this.submenuTimer=isc.Timer.clear(this.submenuTimer);this.submenuTimer=isc.Timer.setTimeout({target:this,method:this.changeSubmenu},this.submenuDelay)}
,isc.A.itemClick=function isc_Menu_itemClick(_1,_2){}
,isc.A.getShowSubmenuKey=function isc_Menu_getShowSubmenuKey(){return this.submenuDirection=="right"?"Arrow_Right":"Arrow_Left"}
,isc.A.getHideSubmenuKey=function isc_Menu_getHideSubmenuKey(){return this.submenuDirection=="right"?"Arrow_Left":"Arrow_Right"}
,isc.A.bodyKeyPress=function isc_Menu_bodyKeyPress(_1,_2){var _3=isc.EventHandler.lastEvent.keyName;if(_3==this.getHideSubmenuKey()){if(this.$58h!=null){this.$58h.hideSubmenu();this.$58h.focus();return false}}else if(_3==this.getShowSubmenuKey()){var _4=this.getItem(this.getFocusRow());if(this.hasSubmenu(_4)){this.changeSubmenu();this.$591.$18j(1);return false}}else if(_3=="Escape"&&this.autoDismissOnBlur!=false){if(this.$58h!=null){this.$58h.hideSubmenu();this.$58h.focus()}else{isc.Menu.hideAllMenus("outsideClick")}
return false}else if(_3=="Enter"){return this.$18k()}
return this.Super("bodyKeyPress",arguments)}
,isc.A.$18j=function isc_Menu__navigateToNextRecord(_1){var _2=this.getFocusRow();if(_2==null)_2=0;if(_1==-1){do{_2+=_1;if(_2<0){this.hide();return false}}while(!this.itemIsEnabled(_2))}
return this.Super("$18j",arguments)}
,isc.A.show=function isc_Menu_show(_1){if(this.$592){this.moveTo(this.$593[0],this.$593[1]);this.$592=null}
if(_1==null)_1=this.showAnimationEffect;var _2=!this.$594&&(_1!=null)&&(_1!="none");if(_2){this.$594=true;this.animateShow(_1,"this.$595()")
return}
if(this.setDynamicItems)this.setDynamicItems()
if(!this.isDrawn()){this.draw(true)}
this.body.focusOnHide=isc.EH.getFocusCanvas();isc.Menu.$l2.add(this);if(this.autoDismissOnBlur)isc.Menu.$596();this.bringToFront();this.Super("show",arguments);if(!this.$594)this.$595()}
,isc.A.$595=function isc_Menu__showComplete(){if(this.$594)delete this.$594;if(isc.Browser.isMoz){this.getClipHandle().offsetLeft}
this.body.focus()}
,isc.A.hide=function isc_Menu_hide(){if(this.visibility==isc.Canvas.HIDDEN)return;this.Super("hide",arguments);this.$95();this.clearLastHilite();this.$527=null;if(this.$59z)delete this.$59z;if(this.submenuTimer)isc.Timer.clearTimeout(this.submenuTimer)}
,isc.A.showContextMenu=function isc_Menu_showContextMenu(_1){if(_1&&(_1.target==this||(this.body&&_1.target==this.body))){if(this.body){if(isc.Browser.isSafari){this.body.$48t=this.getEventRow();this.body.$48u=this.getEventColumn()}
this.body.click()}
return false}
var _2;if(isc.isA.Canvas(_1))_2=_1;if(_1!=null&&_1.target!=null)_2=_1.target;if(_2!=null)this.target=_2;this.positionContextMenu();this.show();return false}
,isc.A.getMaxHeight=function isc_Menu_getMaxHeight(){if(this.maxHeight!=null)return this.maxHeight;return isc.Page.getHeight()-this.getScrollbarSize()}
,isc.A.$13y=function isc_Menu__showOffscreen(){if(!this.isDrawn()){this.setVisibility(isc.Canvas.HIDDEN);this.draw()}
this.setVisibility(isc.Canvas.VISIBLE);this.$95();if(this.isDirty()||this.body.isDirty())this.redraw();if(this.$v3)this.adjustOverflow();if(!this.$597&&this.enforceMaxHeight){if(this.overflow!=isc.Canvas.VISIBLE){this.leaveScrollbarGap=false;this.setOverflow(isc.Canvas.VISIBLE);this.setHeight(this.defaultHeight);this.setWidth(this.$598||this.defaultWidth);this.adjustOverflow()}
var _1=this.getVisibleHeight(),_2=this.getVisibleWidth(),_3=this.getMaxHeight();if(this.overflow==isc.Canvas.VISIBLE&&_1>_3){this.leaveScrollbarGap=true;this.setHeight(_3);this.$598=this.getWidth();this.setWidth(this.getVisibleWidth()+this.getScrollbarSize())
this.setOverflow(isc.Canvas.AUTO);this.adjustOverflow()}
this.$597=true}
this.setVisibility(isc.Canvas.HIDDEN)}
,isc.A.dataChanged=function isc_Menu_dataChanged(_1,_2,_3,_4){if(this.$59t&&this.$59w!=null){if(!this.$599(this.$59w)){if(this.$58h==null){this.setTreeNode(this.$59t.getRoot())}else{this.destroy(true);return}}}
var _5=this.invokeSuper(isc.Menu,"dataChanged",_1,_2,_3,_4);delete this.$597;if(this.autoSetDynamicItems){this.$60a()}
return _5}
,isc.A.$599=function isc_Menu__treeContains(_1){while(_1){if(this.$59t.isRoot(_1))return true;_1=this.$59t.getParent(_1)}
return false}
,isc.A.setData=function isc_Menu_setData(_1,_2,_3,_4){var _5=this.invokeSuper(isc.Menu,"setData",_1,_2,_3,_4);delete this.$597;return _5}
,isc.A.setItems=function isc_Menu_setItems(_1,_2,_3,_4){return this.setData(_1,_2,_3,_4)}
,isc.A.getMenuItem=function isc_Menu_getMenuItem(_1){return isc.Class.getArrayItem(_1,this.data,"name")}
,isc.A.$95=function isc_Menu__moveOffscreen(){if(this.parentElement!=null)return;if(this.$592)return;this.$593=[this.getLeft(),this.getTop()];this.moveTo(null,-9999);this.$592=true}
,isc.A.moveBy=function isc_Menu_moveBy(){var _1=this.Super("moveBy",arguments);if(this.$592)this.$592=false;return _1}
,isc.A.resizeBy=function isc_Menu_resizeBy(_1,_2,_3,_4,_5,_6){if((_1!=null&&_1!=0)||(_2!=null&&_2!=0))delete this.$597;return this.invokeSuper(isc.Menu,"resizeBy",_1,_2,_3,_4,_5,_6)}
,isc.A.hideContextMenu=function isc_Menu_hideContextMenu(){this.hide()}
,isc.A.positionContextMenu=function isc_Menu_positionContextMenu(){this.$13y();var _1=isc.EH.getLastEvent();this.placeNear(_1.x,_1.y)}
,isc.A.getItem=function isc_Menu_getItem(_1){return isc.Class.getArrayItem(_1,this.data,"name")}
,isc.A.setItemProperties=function isc_Menu_setItemProperties(_1,_2){var _1=this.getItem(_1);if(_1!=null){isc.addProperties(_1,_2)}
if(this.isVisible())this.redraw()}
,isc.A.getItemNum=function isc_Menu_getItemNum(_1){return isc.Class.getArrayItemIndex(_1,this.data,"name")}
,isc.A.getItems=function isc_Menu_getItems(){return this.getData()}
,isc.A.addItem=function isc_Menu_addItem(_1,_2){if(_2==null)_2=this.data.getLength();this.data.addAt(_1,_2);this.markForRedraw()}
,isc.A.removeItem=function isc_Menu_removeItem(_1){this.data.remove(_1);this.markForRedraw()}
,isc.A.changeSubmenu=function isc_Menu_changeSubmenu(){var _1=this.getItem(this.body.lastOverRow);if(_1&&this.$59z==_1)return;this.hideSubmenu();if(_1!=null)this.showSubmenu(_1)}
,isc.A.hasSubmenu=function isc_Menu_hasSubmenu(_1){if(!_1)return false;if(_1.submenu)return true;if(this.fetchSubmenus==false||_1.fetchSubmenus==false)return false;if(isc.isA.Tree(this.$59t)){return(this.$59t.isFolder(_1)&&((isc.ResultTree&&isc.isA.ResultTree(this.$59t))||this.$59t.hasChildren(_1)))}
return false}
,isc.A.showSubmenu=function isc_Menu_showSubmenu(_1){var _2=this.getSubmenu(_1);if(!_2)return;this.placeSubmenu(_1,_2)}
,isc.A.getSubmenu=function isc_Menu_getSubmenu(_1){_1=this.getItem(_1);if(!this.hasSubmenu(_1))return;var _2=isc.applyMask(this,this.submenuInheritanceMask);if(!this.$59v){var _3=[];for(var i=0;i<this.fields.length;i++){_3[i]=isc.addProperties({},this.fields[i])}
_2.fields=_3}
if(this.cascadeAutoDismiss){_2.autoDismiss=this.autoDismiss}
var _5=_1.submenu;if(!_5){var _6=(this.$60b||this),_7=(_6==this?0:this.$60c);if(!_6.$60d)_6.$60d=[];this.logDebug("RootMenu:"+_6.getID()+", submenus:"+_6.$60d+", Level:"+_7);_5=_6.$60d[_7];if(!_5){isc.addProperties(_2,{ID:(_6.getID()+"_childrenSubMenu_"+_7),$60b:_6,$60c:_7+1,autoDraw:false,treeParentNode:this.$59t?_1:null,masterMenu:this});var _8=this.submenuConstructor||isc.Menu;_5=_8.create(_2);_6.$60d[_7]=_5;_6.observe(_5,"destroy","observer.submenuDestroyed("+_7+");")}}else if(!isc.isA.Menu(_5)){if(!this.$60d)this.$60d=[];if(isc.isA.String(_5)){_5=window[_5]}else if(isc.isAn.Array(_5)){_5=this.getMenuConstructor().create({autoDraw:false,data:_5},_2);this.$60d.add(_5)}else if(isc.isAn.Object(_5)){_5=this.getMenuConstructor().create(isc.addProperties({autoDraw:false},_2,_5));this.$60d.add(_5)}
_1.submenu=_5}
if(this.$59t)_5.setTreeNode(_1);return _5}
,isc.A.submenuDestroyed=function isc_Menu_submenuDestroyed(_1){delete this.$60d[_1]}
,isc.A.placeSubmenu=function isc_Menu_placeSubmenu(_1,_2){if(this.$59z==_1&&this.$591==_2)return;this.$59z=_1;this.$591=_2;_2.$13y();var _3=this.getItemNum(_1),_4=_2.getPeerRect(),_5=isc.Canvas.$yh(_4[2],_4[3],{left:this.getPageLeft()-this.submenuOffset,width:this.getVisibleWidth()+this.submenuOffset,top:this.body.getRowPageTop(_3)},this.submenuDirection==this.$s0?this.$s0:this.$s2,false)
_2.setPageRect(_5[0],_5[1]);if(this.target!=this&&_2.target!=_2){_2.target=this.target}
_2.show();_2.$58h=this;_2.$590=_3;if(isc.Menu.$60e)delete isc.Menu.$60e[this.getID()]}
,isc.A.hideMenuTree=function isc_Menu_hideMenuTree(){this.hideSubmenu();this.hide()}
,isc.A.hideSubmenu=function isc_Menu_hideSubmenu(){if(this.$591){this.$591.hideSubmenu();this.$591.hide();delete this.$591;delete this.$59z}}
,isc.A.getSubmenuImage=function isc_Menu_getSubmenuImage(_1){if(!this.hasSubmenu(_1))return"&nbsp;";if(!this.$60f){var _2=(this.submenuDirection==this.$s0),_3=isc.addProperties({},this.submenuImage),_4=isc.addProperties({},this.submenuDisabledImage);_3.src=isc.Img.urlForState(_3.src,null,null,(_2?this.$s0:null));_4.src=isc.Img.urlForState(_4.src,null,null,(_2?this.$s0:null));this.$60f=this.imgHTML(_3);this.$60g=this.imgHTML(_4)}
return(this.itemIsEnabled(_1)?this.$60f:this.$60g)}
,isc.A.itemIsEnabled=function isc_Menu_itemIsEnabled(_1){_1=this.getItem(_1);return(_1&&_1.enabled!=false&&_1.isSeparator!=true)}
,isc.A.$59y=function isc_Menu__makeDynamicItemsFunction(){var _1=isc.SB.create();if(this.enableIf)_1.append(this.enableIf,";");_1.append("var menu=this;\r");for(var i=0;i<this.data.length;i++){var _3=this.getItem(i);if(!_3)continue;_1.append("var item=this.data["+i+"];\r");if(_3.enableIf){if(isc.isA.String(_3.enableIf)){_1.append("changed|=this.$60h(",i,",",_3.enableIf,");\r")}else if(isc.isA.Function(_3.enableIf)){_1.append("changed|=this.$60h(",i,",this.data[",i,"].enableIf(target,menu,item));\r")}}
if(_3.checkIf){if(isc.isA.String(_3.checkIf)){_1.append("changed|=this.$60i(",i,",",_3.checkIf,");\r")}else if(isc.isA.Function(_3.checkIf)){_1.append("changed|=this.$60i(",i,",this.data[",i,"].checkIf(target,menu,item));\r")}}
if(_3.dynamicTitle){if(isc.isA.String(_3.dynamicTitle)){_1.append("changed|=this.setItemTitle(",i,",",_3.dynamicTitle,");\r")}else if(isc.isA.Function(_3.dynamicTitle)){_1.append("changed|=this.setItemTitle(",i,",this.data[",i,"].dynamicTitle(target,menu,item));\r")}}
if(_3.dynamicIcon){if(isc.isA.String(_3.dynamicIcon)){_1.append("changed|=this.setItemIcon(",i,",",_3.dynamicIcon,");\r")}else if(isc.isA.Function(_3.dynamicIcon)){_1.append("changed|=this.setItemIcon(",i,",this.data[",i,"].dynamicIcon(target,menu,item));\r")}}}
var _4=_1.toString();if(_4=="")return;_1=isc.SB.create();_1.append("var target=this.target, changed=false;\r",_4,"if(changed&&this.isDrawn()){\r","this.redraw('dynamic item change');\r",(isc.Browser.isIE?"this.body.setRowStyle(0);\r":""),"}");this.addMethods({setDynamicItems:new Function(_1.toString())});this.setDynamicItems.$60j=this.data.duplicate()}
,isc.A.$60a=function isc_Menu__refreshDynamicItemsFunction(){if(this.setDynamicItems==null||!this.setDynamicItems.$60j.equals(this.data)){this.$59y()}}
,isc.A.refreshRow=function isc_Menu_refreshRow(){if(this.setDynamicItems)this.setDynamicItems();return this.Super("refreshRow",arguments)}
,isc.A.$60h=function isc_Menu__setItemEnabled(_1,_2){return this.setItemEnabled(_1,!!_2)}
,isc.A.$60i=function isc_Menu__setItemChecked(_1,_2){return this.setItemChecked(_1,!!_2)}
,isc.A.setItemEnabled=function isc_Menu_setItemEnabled(_1,_2){if(_2==null)_2=true;_1=this.getItem(_1);if(!_1)return;if(_1.enabled!=_2){_1.enabled=_2;this.markForRedraw("itemEnabled");return true}
return false}
,isc.A.setItemChecked=function isc_Menu_setItemChecked(_1,_2){if(_2==null)_2=true;_1=this.getItem(_1);if(!_1)return;if(_1.checked!=_2){_1.checked=_2;this.markForRedraw("itemChecked");return true}
return false}
,isc.A.setItemTitle=function isc_Menu_setItemTitle(_1,_2){_1=this.getItem(_1);if(!_1)return;if(_1.title!=_2){_1.title=_2;this.markForRedraw("item title change");return true}
return false}
,isc.A.setItemIcon=function isc_Menu_setItemIcon(_1,_2,_3){_1=this.getItem(_1);if(!_1)return;if(_1.icon!=_2){_1.icon=_2;if(_3)_1.disabledIcon=_3;this.markForRedraw("item icon change");return true}
return false}
,isc.A.getIcon=function isc_Menu_getIcon(_1){var _2=this.fixedIconWidth&&this.getRecordIndex(_1)==0,_3=_2?this.iconWidth:null;var _4;if(_1.icon){var _5=(this.itemIsEnabled(_1)||!_1.disabledIcon?_1.icon:_1.disabledIcon);_4=this.imgHTML(_5,(_1.iconWidth?_1.iconWidth:this.iconWidth),(_1.iconHeight?_1.iconHeight:this.iconHeight));if(_2&&(_1.iconWidth==null||_1.iconWidth>=_3)){_2=false}else{_3-=_1.iconWidth}}
if(_1.checked){_4=this.getCheckmarkImage(_1);if(_2){var _6=this.checkmarkImage?this.checkmarkImage.width:this.iconWidth;if(_6<_3){_3-=_6}else{_2=false}}}
if(_2){if(_4)return _4+this.imgHTML("[SKIN]/../blank.gif",_3,1);else return this.imgHTML("[SKIN]/../blank.gif",_3,1)}
return _4||"&nbsp;"}
,isc.A.getItemTitle=function isc_Menu_getItemTitle(_1){var _2;if(this.$59t){_2=this.$59t.getTitle(_1)}else{_2=_1.title||_1.name||_1.id}
_2=_2||"&nbsp;";return _2}
,isc.A.getKeyTitle=function isc_Menu_getKeyTitle(_1){if(_1.keyTitle)return _1.keyTitle;return"&nbsp;"}
,isc.A.getCheckmarkImage=function isc_Menu_getCheckmarkImage(_1){if(!this.$60k){this.$60k=this.imgHTML(this.checkmarkImage);this.$60l=this.imgHTML(this.checkmarkDisabledImage)}
return(this.itemIsEnabled(_1)?this.$60k:this.$60l)}
,isc.A.setUpKeyListening=function isc_Menu_setUpKeyListening(){var _1="";var _2,_3,_4=this.data.length;for(var i=0;i<_4;i++){_2=this.getItem(i);if(!_2)continue;_3=_2.keys;if(!_3)continue;if(!isc.isAn.Array(_3))_3=[_3];for(var _6,k=0,_8=_3.length;k<_8;k++){_6=_3[k];if(_6==null)continue;isc.Page.registerKey(_6,"target.menuKey("+i+");",this);if(!this.registeredKeys)this.registeredKeys=[];this.registeredKeys.add(_6)}
if(!_2.keyTitle)this.setItemKeyTitle(_2,_3[0])}}
,isc.A.destroy=function isc_Menu_destroy(_1){if(this.registeredKeys){for(var i=0;i<this.registeredKeys.length;i++){isc.Page.unregisterKey(this.registeredKeys[i],this)}}
if(this.$60d)this.$60d.map("destroy");this.Super("destroy",arguments);if(this.$59t){var _3=this.$59t;if(!_1&&_3.$393&&isc.isA.Function(_3.destroy)&&(_3.componentId==this.ID))
{_3.destroy()}else{this.$52q(_3)}
delete this.$59t}}
,isc.A.menuKey=function isc_Menu_menuKey(_1){if(this.setDynamicItems)this.setDynamicItems();return this.selectMenuItem(_1)}
,isc.A.setItemKeyTitle=function isc_Menu_setItemKeyTitle(_1,_2){var _3;if(isc.isA.String(_2))_3=_2
else if(isc.isAn.Object(_2)){if(_2.title)_3=_2.title
else _3=_2.keyName}
_1.keyTitle=_3}
);isc.B._maxIndex=isc.C+70;isc.A=isc.Menu;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.hideAllMenus=function isc_c_Menu_hideAllMenus(_1){var _2=_1=="itemClick",_3=_1=="outsideClick";var _4=true;if(isc.Menu.$l2.length>0){var _5=isc.Menu.$l2,_6=false,_7,_8=isc.EH.getFocusCanvas();isc.Menu.$l2=[];for(var i=_5.length-1;i>=0;i--){var _10=_5[i];if(!_10.isVisible()){continue}
if(_2&&_10.autoDismiss==false){isc.Menu.$l2.addAt(_10,0);_4=false;continue}
if(_3&&_10.autoDismissOnBlur==false){isc.Menu.$l2.addAt(_10,0);_4=false;continue}
if(_10.$xd(_8)){if(_7==null)_7=_10;_6=true}
_10.hide()}
if(_6&&isc.isA.Canvas(_7.body.focusOnHide)){_7.body.focusOnHide.focus()}}
isc.Menu.$60e={};if(_4){if(isc.Menu.$60m){isc.EH.hideClickMask(isc.Menu.$60m);isc.Menu.$60m=null}}else{if(!isc.EH.clickMaskUp(isc.Menu.$60m)){this.$596()}}
if(isc.Menu.$60n!=null){var _11=isc.Menu.$60n;delete isc.Menu.$60n;if(isc.EH.lastEvent.eventType==isc.EH.MOUSE_DOWN){_11.$60o=true;isc.Page.setEvent(isc.EH.CLICK,_11,isc.Page.FIRE_ONCE,"$60p")}}}
,isc.A.$60q=function isc_c_Menu__getAutoDismissOnBlurMenus(){if(this.$l2==null||this.$l2.length==0)return[];var _1=[];for(var i=0;i<this.$l2.length;i++){if(this.$l2[i].autoDismissOnBlur!=false)_1.add(this.$l2[i])}
return _1}
,isc.A.$596=function isc_c_Menu__showMenuClickMask(){if(isc.Menu.$60q().length>0&&(isc.Menu.$60m==null||!isc.EH.clickMaskUp(isc.Menu.$60m)))
{isc.Menu.$60m=isc.EH.showClickMask("isc.Menu.hideAllMenus('outsideClick')",true)}}
,isc.A.menuForValueMap=function isc_c_Menu_menuForValueMap(_1,_2){var _3=[];if(isc.isA.String(_1))_1=this.getPrototype().getGlobalReference(_1);if(isc.isAn.Array(_1)){for(var i=0;i<_1.length;i++){_3[i]={value:_1[i],title:_1[i]}}}else{for(var _5 in _1){_3.add({value:_5,title:_1[_5]})}}
var _6=isc.Menu.$60r;if(_2==false||isc.Menu.$60r==null){_6=isc.Menu.newInstance({autoDraw:false,itemClick:function(_7){if(this.target.valueMapMenuSelected){this.target.valueMapMenuSelected(_7.value)}}})}
if(_2!=false&&isc.Menu.$60r==null)isc.Menu.$60r=_6;_6.setData(_3);return _6}
);isc.B._maxIndex=isc.C+4;isc.Menu.registerStringMethods({itemClick:"item"})
isc.A=isc.ListGrid.getPrototype();isc.A.showHeaderContextMenu=true;isc.A=isc.ListGrid.getPrototype();isc.A.showHeaderMenuButton=false;isc.$60s={title:"Show Menu",height:22,showMenuButtonImage:true,menuButtonImage:"[SKIN]menu_button.gif",menuButtonImageUp:"[SKIN]menu_button_up.gif",hiliteAccessKey:true,iconWidth:7,iconHeight:7,iconOrientation:"right",iconAlign:"right",align:"left",showMenuBelow:true,alignMenuLeft:true,menu:null,initWidget:function(_1,_2,_3,_4){if(this.showMenuButtonImage)this.$27v();return this.invokeSuper(isc.MenuButton,"initWidget",_1,_2,_3,_4)},autoDestroyMenu:true,destroy:function(_1,_2,_3,_4,_5){if(this.menu!=null&&this.autoDestroyMenu&&this.menu.destroy!=null&&!this.menu.destroyed&&!this.menu.destroying)
{this.menu.destroy();this.menu=null}
return this.invokeSuper(isc.MenuButton,"destroy",_1,_2,_3,_4,_5)},setShowMenuButtonImage:function(_1){if(_1==this.showMenuButtonImage)return;this.showMenuButtonImage=_1;if(_1)this.$27v();else this.icon=null;if(this.isDrawn())this.markForRedraw()},setShowMenuBelow:function(_1){if(_1!=this.showMenuBelow){this.showMenuBelow=_1;if(this.showMenuButtonImage){this.$27v();if(this.isDrawn())this.markForRedraw()}}},$27v:function(){var _1=this.showMenuBelow?this.menuButtonImage:this.menuButtonImageUp;if(isc.isAn.Object(_1)){if(_1.width)this.iconWidth=_1.width;if(_1.height)this.iconHeight=_1.height;this.icon=_1.src}else{this.icon=_1}},handleClick:function(){if(this.Super("handleClick",arguments)==false)return;if(this.$60o)return;this.showMenu()},$60p:function(){if(this.$60o){var _1=this;isc.Page.setEvent(isc.EH.IDLE,function(){_1.$60o=null},isc.Page.FIRE_ONCE)}},keyPress:function(){if(isc.EventHandler.lastEvent.keyName=="Arrow_Down"){this.showMenu();return false}
return this.Super("keyPress",arguments)},$zn:function(){this.Super("$zn",arguments);this.markForRedraw()},$60t:function(_1,_2){if(!_1||!_2)return;_1.rootMenuButton=_2;_1.autoDismissOnBlur=false;_1.mouseOver=function(){var _3=this.rootMenuButton;if(!_3.showMenuOnRollOver)return this.Super("mouseOver",arguments);_3.checkRollOverMouseOver(_3,this)};_1.mouseOut=function(){var _3=this.rootMenuButton;if(!_3.showMenuOnRollOver)return this.Super("mouseOut",arguments);_3.checkRollOverMouseOut(_3,this)}},checkRollOverMouseOver:function(_1,_2){if(_1.$60u!=null){isc.Timer.clear(_1.$60u)}
if(_1.menu!=null&&!(_1.menu.isVisible()&&_1.menu.isDrawn())){isc.Menu.hideAllMenus();_1.showMenu()}},checkRollOverMouseOut:function(_1,_2){if(_1.$60u!=null){_1.$60u=null}
if(_1.menu!=null&&_1.menu.isVisible()&&_1.menu.isDrawn()){_1.$60u=isc.Timer.setTimeout(function(){if(_1.menu!=null&&_1.menu.isVisible()&&_1.menu.isDrawn()){isc.Menu.hideAllMenus();_1.$60u=null}},_1.rollOverMenuHideDelay)}},showMenu:function(){if(isc.isA.String(this.menu))this.menu=window[this.menu];if(!isc.isA.Menu(this.menu))this.$13x(this.menu);if(!isc.isA.Menu(this.menu))return;isc.Menu.$60n=this;var _1=this.menu;if(this.showMenuOnRollOver){var _2=this;this.$60t(_1,this);_1.getSubmenu=function(_6){var _3=this.Super("getSubmenu",arguments);if(_3)_2.$60t(_3,_2);return _3}}
_1.$13y();var _4=this.getPageLeft();if(!this.alignMenuLeft){_4=_4-(_1.getVisibleWidth()-this.getVisibleWidth())}
var _5=this.showMenuBelow?this.getPageTop()+this.getVisibleHeight()+1:this.getPageTop()-_1.getVisibleHeight()+2;_1.placeNear(_4,_5);_1.show(this.menuAnimationEffect)},$13x:function(_1){if(!_1)return;_1.autoDraw=false;if(this.showMenuOnRollOver){var _2=this;_1.getSubmenu=function(_5){var _3=this.Super("getSubmenu",arguments);_2.$60t(_3,_2);return _3}}
var _4=this.menuConstructor||isc.Menu;this.menu=_4.create(_1)},rollOverMenuHideDelay:250,mouseMove:function(){if(this.showMenuOnRollOver)this.mouseOver()},mouseOver:function(){if(!this.showMenuOnRollOver)return this.Super("mouseOver",arguments);this.checkRollOverMouseOver(this,this)},mouseOut:function(){if(!this.showMenuOnRollOver)return this.Super("mouseOut",arguments);this.checkRollOverMouseOut(this,this)}};isc.ClassFactory.defineClass("MenuButton","Button");isc.MenuButton.addProperties(isc.$60s)
isc.A=isc.MenuButton.getPrototype();isc.A.skinImgDir="images/Menu/";isc.A.baseStyle="menuButton";isc.ClassFactory.defineClass("IMenuButton","StretchImgButton");isc.IMenuButton.addProperties(isc.$60s)
isc.A=isc.IMenuButton.getPrototype();isc.A.labelSkinImgDir="images/Menu/";isc.defineClass("ToolStripMenuButton","IMenuButton");isc.A=isc.ToolStripMenuButton.getPrototype();isc.A.showTitle=true;isc.A.showRollOver=true;isc.A.showDown=true;isc.A.labelVPad=0;isc.A.labelHPad=7;isc.A.autoFit=true;isc.A.src="[SKIN]/ToolStrip/button/button.png";isc.A.capSize=3;isc.A.height=22;isc.ClassFactory.defineClass("SelectionTreeMenu","Menu")
isc.A=isc.SelectionTreeMenu.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.itemClick=function isc_SelectionTreeMenu_itemClick(_1){this.inheritedProperties.button.$36i(_1)}
,isc.A.getBaseStyle=function isc_SelectionTreeMenu_getBaseStyle(_1,_2,_3){var _4=this.inheritedProperties.button;if(_4.$60v(_1))return _4.selectedBaseStyle;return this.Super("getBaseStyle",arguments)}
,isc.A.show=function isc_SelectionTreeMenu_show(){if(this.body){for(var i=0;i<this.getTotalRows();i++){this.body.setRowStyle(i)}}
return this.Super("show",arguments)}
,isc.A.getItemTitle=function isc_SelectionTreeMenu_getItemTitle(_1,_2,_3,_4,_5){var _6=this.inheritedProperties.button;if(_6.displayField)return _1[_6.displayField];return this.invokeSuper(isc.SelectionTreeMenu,"getItemTitle",_1,_2,_3,_4,_5)}
);isc.B._maxIndex=isc.C+4;isc.ClassFactory.defineClass("TreeMenuButton","MenuButton");isc.ClassFactory.defineClass("ITreeMenuButton","IMenuButton");isc.$60w={title:null,unselectedTitle:"Choose a value",showPath:false,pathSeparatorString:"&nbsp;&gt;&nbsp;",selectedBaseStyle:"treeMenuSelected",overflow:isc.Canvas.VISIBLE,menuConstructor:isc.SelectionTreeMenu,getTitle:function(){if(this.title)return this.title;var _1=this.getSelectedItem();if(_1){if(!this.showPath){if(!isc.isA.Menu(this.menu))this.$13x(this.menu);return this.menu.getItemTitle(_1)}else{var _2=this.getTree();var _3=_2.getParents(_1),_4=[];for(var i=_3.length-1;i>=0;i--){if(!_2.showRoot&&i==_3.length-1)continue;_4.add(this.menu.getItemTitle(_3[i]))}
_4.add(this.menu.getItemTitle(_1));return _4.join(this.pathSeparatorString)}}else{return this.unselectedTitle}},$13x:function(_1){_1=isc.addProperties(this.menuDefaults||{},_1,{inheritedProperties:{button:this},canSelectParentItems:this.canSelectParentItems,submenuConstructor:isc.SelectionTreeMenu,dataSource:this.dataSource,criteria:this.criteria,data:this.data});if(this.emptyMenuMessage)_1.emptyMessage=this.emptyMenuMessage;if(this.loadDataOnDemand!=null)_1.loadDataOnDemand=this.loadDataOnDemand;var _2=this.Super("$13x",[_1]);this.observe(this.menu,"treeDataLoaded","observer.$36k()");return _2},$36k:function(){if(this.treeDataLoaded)this.treeDataLoaded()},getTree:function(){if(!isc.isA.Menu(this.menu))this.$13x(this.menu);return this.menu.$59t},$36i:function(_1){if(this.itemSelected&&this.itemSelected(_1,this.$60x)==false)
return;this.setSelectedItem(_1)},setSelectedItem:function(_1){this.$60x=_1;this.setTitle()},getSelectedItem:function(){return this.$60x},$60v:function(_1){var _2=this.getSelectedItem(),_3=this.getTree();while(_2){if(_1==_2)return true;_2=_3.getParent(_2)}
return false}};isc.TreeMenuButton.addProperties(isc.$60w)
isc.ITreeMenuButton.addProperties(isc.$60w)
isc.TreeMenuButton.registerStringMethods({itemSelected:"item, oldItem"});isc.ITreeMenuButton.registerStringMethods({itemSelected:"item, oldItem"});isc.ClassFactory.defineClass("TileLayout","Canvas");isc.A=isc.TileLayout.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.layoutPolicy="fit";isc.A.tileSize=50;isc.A.tileMargin=10;isc.A.layoutMargin=5;isc.A.paddingAsLayoutMargin=true;isc.A.animateTileChange=true;isc.A.orientation="horizontal";isc.A.overflow="auto";isc.A.canFocus=true;isc.A.expandMargins=true;isc.A.autoWrapLines=true;isc.A.dragLineDefaults={overflow:"hidden",styleName:"layoutDropLine"};isc.A.$60y=true;isc.A.relayoutProperties=["tilesPerLine","orientation","tileWidth","tileHeight","expandMargins"];isc.A.canDrop=true;isc.A.$60z=2;isc.B.push(isc.A.initWidget=function isc_TileLayout_initWidget(){this.$600();this.invokeSuper(isc.TileLayout,"initWidget");if(!this.tiles)this.tiles=[];this.data=this.tiles}
,isc.A.draw=function isc_TileLayout_draw(_1,_2,_3,_4){this.invokeSuper(isc.TileLayout,"draw",_1,_2,_3,_4);this.$601();this.logDebug('calling layoutTiles from draw',"TileLayout");this.layoutTiles()}
,isc.A.resized=function isc_TileLayout_resized(){this.Super("resized",arguments);this.logDebug('calling layoutTiles from resized',"TileLayout");this.layoutTiles()}
,isc.A.childResized=function isc_TileLayout_childResized(_1,_2,_3,_4){this.invokeSuper(isc.TileLayout,"childResized",_1,_2,_3,_4);this.logDebug('calling layoutTiles from childResized',"TileLayout");isc.Timer.setTimeout(this.ID+".layoutTiles()",100)}
,isc.A.$600=function isc_TileLayout__enforceLegalLayoutPolicy(){if(this.layoutPolicy!="fit"&&this.layoutPolicy!="flow"){this.layoutPolicy="fit"}}
,isc.A.willScroll=function isc_TileLayout_willScroll(){var _1=this.orientation=="horizontal";var _2=this.$xy;var _3=this.$xz;var _4=this.$xw;var _5=this.$xx;var _6,_7;if(this.$60y){_7=this.getUserVisibleTiles();_6=_7.getLength()}else{_6=this.getLength()}
if(this.layoutPolicy=="flow"){if(this.overflow!="auto"&&this.overflow!="scroll")return false;var _8=_1?this.getInnerWidth()-_5:this.getInnerHeight()-_3;var _9=_1?this.getInnerHeight()-_3:this.getInnerWidth()-_4;var _10=_1?_4:_2;var _11=_1?_2:_4;var _12=0;var _13=0;var _14=_1?this.getTileHMargin():this.getTileVMargin();var _15=_1?this.getTileVMargin():this.getTileHMargin();for(var i=0;i<_6;i++){var _17=_7?_7[i]:this.getTile(i);if(!this.autoWrapLines&&_17.startLine)_10=0;var _18=_1?_17.getVisibleWidth():_17.getVisibleHeight();if((this.autoWrapLines&&_10+_18>_8)||(!this.autoWrapLines&&_17.endLine)){_10=_1?_4:_2;;_11+=_12+_15;_12=0;_13++}
var _19=_1?_17.getVisibleHeight():_17.getVisibleWidth();if(_11+_19>_9)return true;if(_12<_19){_12=_19}
_10+=_18+_14}
return false}else{var _9=_1?this.getVisibleHeight()-(_2+_3):this.getVisibleWidth()-(_5+_4);var _19=_1?this.getTileHeight():this.getTileWidth();var _15=_1?this.getTileVMargin():this.getTileHMargin();var _20=this.getTilesPerLine();var _21=Math.floor(_9/(_19+_15));var _22=_20*_21;return(_6>_22)}}
,isc.A.layoutTiles=function isc_TileLayout_layoutTiles(_1){if(!this.isDrawn()||this.getLength()==0)return;this.$602=[];this.$603=0;this.$604=[this.getScrollLeft(),this.getScrollTop(),this.getInnerWidth(),this.getInnerHeight()];var _2,_3,_4,_5;if(this.paddingAsLayoutMargin){var _6=this.$q6();_4=_6.left;_5=_6.right;_2=_6.top;_3=_6.bottom}
var _7=this.$xy=this.$ef(this.layoutMargin,_2,0);var _8=this.$xz=this.$ef(this.layoutMargin,_3,0);var _9=this.$xw=this.$ef(this.layoutMargin,_4,0);var _10=this.$xx=this.$ef(this.layoutMargin,_5,0);var _11,_12;if(this.$60y){_12=this.getUserVisibleTiles();_11=_12.getLength()}else{_11=this.getLength()}
if(this.layoutPolicy=="flow"){this.logDebug("starting flow layout","TileLayout");var _13=this.orientation=="horizontal";var _14=_13?this.getInnerWidth()-_10:this.getInnerHeight()-_8;if(this.willScroll())_14-=this.getScrollbarSize();var _15=_13?_9:_7;var _16=_13?_7:_9;var _17=0;var _18=0;var _19=_13?this.getTileHMargin():this.getTileVMargin();var _20=_13?this.getTileVMargin():this.getTileHMargin();for(var i=0;i<_11;i++){var _22=_12?_12[i]:this.getTile(i);if(!this.autoWrapLines&&_22.startLine)_15=0;var _23=_13?_22.getVisibleWidth():_22.getVisibleHeight();if((this.autoWrapLines&&_15+_23>_14)||(!this.autoWrapLines&&_22.endLine)){_15=_13?_9:_7;;_16+=_17+(i==0?0:_20);_17=0;_18++}
var _24=_13?_16:_15;var _25=_13?_15:_16;var _26=_12?_12[i]:i;this.processTile(_26,_24,_25);var _27=_13?_22.getVisibleHeight():_22.getVisibleWidth();if(_17<_27){_17=_27}
_15+=_23+_19}}else{this.logDebug("starting fit layout:"+this.$594,"TileLayout");var _28=this.getTilesPerLine();var _29=this.getTileHeight();var _30=this.getTileWidth();var _13=this.orientation=="horizontal";var _31,_32,_33;var _34,_35,_36,_37,_38,_39;if(this.shouldUseIncrRendering()){this.logDebug("fit layout, using incremental rendering","TileLayout");var _40=this.getVisibleTiles();this.$605=_40;_35=_40[0];_34=_11;var _41=this.getVisibleLines();_38=_41[0];_39=_41[0]+_41[1];if(!this.hasAllVisibleTiles(_40,true))return}else{this.logDebug("fit layout, rendering all tiles","TileLayout");_34=_11;_35=0;_38=0;_39=Math.ceil(_34/ _28);_31=this.getTileHMargin();_32=this.getTileVMargin()}
var _42=Math.ceil(_11/ _28);var _32=this.getTileVMargin();var _31=this.getTileHMargin();var _27=_13?_29:_30;var _43=_13?_32:_31;var _44=((_27+_43)*_42)-_43+(_13?_7+_8:_9+_10);_37=this.$606();_13?_37.setHeight(_44):_37.setWidth(_44);_37.sendToBack();_33=this.getExtraMarginPixels(_28,_29,_30,_31,_32);for(var i=_38;i<_39;i++){var _45=_33;for(var j=0;j<_28;j++){var _47=_13?i:j;var _48=_13?j:i;var _24=(_47*_29)+(_47*_32)+_7;if(!_13&&j+1<=_33)_24+=j+1;var _25=(_48*_30)+(_48*_31)+_9;if(_13&&j+1<=_33)_25+=j+1;var _26=_12?_12[_35]:_35
var _22=this.processTile(_26,_24,_25,this.getTileHeight(),this.getTileWidth());_35++;if(_35>=_34)break}
if(_35>=_34)break}}}
,isc.A.hasAllVisibleTiles=function isc_TileLayout_hasAllVisibleTiles(){return true}
,isc.A.getUserVisibleTiles=function isc_TileLayout_getUserVisibleTiles(){var _1=[];for(var i=0;i<this.getLength();i++){var _3=this.getTile(i);if(!_3.$607)_1.add(_3)}
return _1}
,isc.A.processTile=function isc_TileLayout_processTile(_1,_2,_3,_4,_5){var _6;if(this.$594){if(isc.isA.Canvas(_1))_6=_1;else _6=this.getRecordTile(_1);if(!_6)return;if(this.$608!=null&&this.$608.findIndex("ID",_6.ID)==-1)return;if(!_6)return;if(_4)_6.setHeight(_4);if(_5)_6.setWidth(_5);if(_6.getTop()!=_2||_6.getLeft()!=_3){_6.$609=_2;_6.$61a=_3;this.$61b.add(_6)}
var _7=[_3,_2,_6.getVisibleWidth(),_6.getVisibleHeight()];if(isc.Canvas.rectsIntersect(this.$604,_7)){this.$602.add(_6)}}else{if(isc.isA.Canvas(_1))_6=_1;else _6=this.getTile(_1);if(!_6)return;if(_6.isDirty())_6.redraw();if(_4)_6.setHeight(_4);if(_5)_6.setWidth(_5);_6.setTop(_2);_6.setLeft(_3);this.addChild(_6);_6.$61c=true;_6.show();_6.$61c=null;var _7=[_6.getLeft(),_6.getTop(),_6.getVisibleWidth(),_6.getVisibleHeight()];if(isc.Canvas.rectsIntersect(this.$604,_7)){this.$602.add(_6)}
this.$603+=1;return _6}}
,isc.A.$606=function isc_TileLayout__getSpacerCanvas(){if(!this.$61d){this.$61d=isc.Canvas.create({autoDraw:false});this.addChild(this.$61d)}
return this.$61d}
,isc.A.getDrawnStartIndex=function isc_TileLayout_getDrawnStartIndex(){if(this.$605)return this.$605[0];else return null}
,isc.A.getDrawnEndIndex=function isc_TileLayout_getDrawnEndIndex(){if(this.$605)return this.$605[1];else return null}
,isc.A.shouldUseIncrRendering=function isc_TileLayout_shouldUseIncrRendering(){if(this.$594){return true}else if(!this.showAllRecords&&this.layoutPolicy=="fit"&&(this.overflow=="auto"||this.overflow=="hidden")){return true}else{return false}}
,isc.A.getLength=function isc_TileLayout_getLength(){if(!this.tiles)return 0;else return this.tiles.getLength()}
,isc.A.getTilesPerLine=function isc_TileLayout_getTilesPerLine(){if(this.tilesPerLine)return this.tilesPerLine;else{var _1=this.orientation=="horizontal"?this.getTileWidth():this.getTileHeight();var _2=this.orientation=="horizontal"?(this.tileHMargin||this.tileMargin):(this.tileVMargin||this.tileMargin);var _3=this.orientation=="horizontal"?this.getInnerWidth():this.getInnerHeight();var _4=Math.floor(_3/ _1);var _5=this.orientation=="horizontal"?this.$xw+this.$xx:this.$xy+this.$xz;var _6=_2*(_4-1)+(_4*_1)+_5;if(_6>_3){var _7=Math.ceil((_6-_3)/_1);_4-=_7}
if(_4<1)_4=1;return _4}}
,isc.A.$601=function isc_TileLayout__setTileSize(){var _1=this.orientation=="horizontal";if(this.layoutPolicy!="fit"||this.expandMargins||!this.tilesPerLine||(_1&&this.tileWidth)||(!_1&&this.tileHeight))return;var _2=_1?(this.tileHMargin||this.tileMargin):(this.tileVMargin||this.tileMargin);var _3=_1?this.getInnerWidth():this.getInnerHeight();var _4=this.layoutMargin*2;var _5=_3-_4;if(!this.tilesPerLine){this.tileSize=50}else{var _6=Math.floor(_5/ this.tilesPerLine);_6-=_2;isc.logWarn('setTileSize:'+[_6,_3,this.layoutMargin]);if(this.orientation=="horizontal")this.tileWidth=_6;else this.tileHeight=_6}}
,isc.A.getVisibleLines=function isc_TileLayout_getVisibleLines(){var _1=(this.orientation=="horizontal");var _2=_1?this.getScrollTop():this.getScrollLeft();var _3=_1?this.getTileHeight()+this.getTileVMargin():this.getTileWidth()+this.getTileHMargin();var _4=_1?this.getInnerHeight():this.getInnerWidth();var _5=Math.floor(_2/ _3);if(_5>0)_5--;var _6=Math.ceil(_4/ _3)+2;return[_5,_6]}
,isc.A.getVisibleTiles=function isc_TileLayout_getVisibleTiles(){var _1=this.getVisibleLines();var _2=this.getTilesPerLine();var _3=_1[0]*_2;var _4=(_1[0]+_1[1])*_2;return[_3,_4]}
,isc.A.scrolled=function isc_TileLayout_scrolled(){if(this.shouldUseIncrRendering()){if(this.$41s)isc.Timer.clear(this.$41s);this.$41s=isc.Timer.setTimeout(this.ID+".layoutAfterScroll()")}}
,isc.A.layoutAfterScroll=function isc_TileLayout_layoutAfterScroll(){this.logDebug('layoutAfterScroll',"TileLayout");if(this.shouldLayoutTiles()){this.logDebug('calling layoutTiles from layoutAfterScroll',"TileLayout");this.layoutTiles()}}
,isc.A.shouldLayoutTiles=function isc_TileLayout_shouldLayoutTiles(){var _1=this.getVisibleTiles();if(_1[0]==this.getDrawnStartIndex()&&_1[1]==this.getDrawnEndIndex()){return false}else{return true}}
,isc.A.getTileWidth=function isc_TileLayout_getTileWidth(){if(this.tileWidth){if(isc.isA.String(this.tileWidth)){this.tileWidth=parseInt(this.tileWidth);if(!isc.isA.Number(this.tileWidth))this.tileWidth=this.tileSize}
return this.tileWidth}
else return this.tileSize}
,isc.A.getTileHeight=function isc_TileLayout_getTileHeight(){if(this.tileHeight){if(isc.isA.String(this.tileHeight)){this.tileHeight=parseInt(this.tileHeight);if(!isc.isA.Number(this.tileHeight))this.tileHeight=this.tileSize}
return this.tileHeight}
else return this.tileSize}
,isc.A.getInnerBreadth=function isc_TileLayout_getInnerBreadth(){var _1=this.orientation=="horizontal"?this.getInnerWidth():this.getInnerHeight();return _1}
,isc.A.getTileHMargin=function isc_TileLayout_getTileHMargin(){var _1;if(this.tileHMargin){_1=this.tileHMargin}else{_1=this.tileMargin}
if(this.layoutPolicy=="fit"&&this.expandMargins&&this.orientation=="horizontal"){var _2=this.getTilesPerLine();var _3=this.$xw+this.$xx;var _4=_2-1;if(_4==0)_4=1;var _5=Math.floor((this.getInnerBreadth()-(_2*this.getTileWidth())-_3)/_4);if(_5<_1)return _1;else return _5}else{return _1}}
,isc.A.getTileVMargin=function isc_TileLayout_getTileVMargin(){var _1;if(this.tileHMargin){_1=this.tileVMargin}else{_1=this.tileMargin}
if(this.layoutPolicy=="fit"&&this.expandMargins&&this.orientation=="vertical"){var _2=this.getTilesPerLine();var _3=this.$xy+this.$xz;var _4=_2-1;if(_4==0)_4=1;var _5=Math.floor((this.getInnerBreadth()-(_2*this.getTileHeight())-_3)/_4);if(_5<_1)return _1;else return _5}else{return _1}}
,isc.A.getExtraMarginPixels=function isc_TileLayout_getExtraMarginPixels(_1,_2,_3,_4,_5){if(this.expandMargins&&this.orientation=="horizontal"){var _6=(_1*_3)+((_1-1)*_4)+(this.$xw+this.$xx);return this.getInnerBreadth()-_6}else if(this.expandMargins&&this.orientation=="vertical"){var _6=(_1*_2)+((_1-1)*_5)+(this.$xy+this.$xz);return this.getInnerBreadth()-_6}else{return 0}}
,isc.A.getTile=function isc_TileLayout_getTile(_1){return isc.Class.getArrayItem(_1,this.tiles)}
,isc.A.addTile=function isc_TileLayout_addTile(_1,_2){if(!this.tiles)return;if(!_2)_2=this.tiles.getLength();this.tiles.addAt(_1,_2);this.reLayout()}
,isc.A.removeTile=function isc_TileLayout_removeTile(_1){if(!this.tiles)return;if(!isc.isA.Canvas(_1)){var _2=this.tiles.findIndex("ID",_1);if(_2==-1)_1=this.tiles.get(_1);else _1=this.tiles.get(_2)}
this.removeChild(_1)}
,isc.A.removeChild=function isc_TileLayout_removeChild(_1,_2){var _3=this.tiles.findIndex("ID",_1.ID);if(_3>-1)this.tiles.removeAt(_3);this.invokeSuper(isc.TileLayout,"removeChild",_1,_2);this.reLayout()}
,isc.A.getRecordTile=function isc_TileLayout_getRecordTile(_1){if(_1==null)return null;return this.tiles.get(_1)}
,isc.A.childVisibilityChanged=function isc_TileLayout_childVisibilityChanged(_1,_2){if(!this.$594&&!_1.$61e){if(_2=="hidden")_1.$607=true;else _1.$607=null;this.reLayout()}}
,isc.A.propertyChanged=function isc_TileLayout_propertyChanged(_1,_2){this.invokeSuper(isc.TileLayout,"propertyChanged",_1,_2);if(isc.endsWith(_1,"Margin")||this.relayoutProperties.contains(_1))
{this.layoutTiles()}}
,isc.A.showDragLineForRecord=function isc_TileLayout_showDragLineForRecord(){if(isc.isAn.Array(this.data)||(isc.isA.ResultSet(this.data))){var x=this.getOffsetX(),y=this.getOffsetY(),_3=this.getPageLeft(),_4=this.getPageTop();if(this.data.getLength()==0){return}
var _5=this.findIndexForCoord(x,y);if(this.$61f==null)this.$61f=_5;var _6,_7,_8=this.getRecordTile(_5);if(_8!=null){if((this.orientation=="horizontal"&&x>_8.getLeft()+(_8.getVisibleWidth()/2))||(this.orientation=="vertical"&&y>_8.getTop()+(_8.getVisibleHeight()/2))){_5++;_8=this.getRecordTile(_5)}}
if(_5==null||_8==null){_5=this.data.getLength();_8=this.getRecordTile(_5-1);if(this.orientation=="horizontal"){_6=_8.getLeft()+_8.getVisibleWidth()-this.$60z;_7=_8.getTop()}else{_6=_8.getLeft();_7=_8.getTop()+_8.getVisibleHeight()-this.$60z}}else{_6=_8.getLeft();_7=_8.getTop()}
this.$61g=_5;var _9,_10;if(this.orientation=="horizontal"){_10=_8.getVisibleHeight();_9=this.$60z}
else{_9=_8.getVisibleWidth();_10=this.$60z}
this.showDragLine(_3+(_6-this.getScrollLeft()),_4+(_7-this.getScrollTop()),_9,_10)}}
,isc.A.showDragLine=function isc_TileLayout_showDragLine(_1,_2,_3,_4){this.makeDragLine();var _5=this.getVisibleHeight();var _6=Math.round((_5-this.getInnerHeight())/2);var _7=this.getPageTop();var _8=_7+_6;var _9=_7+_5-_6;if(_2<_8){_4=_4-(_8-_2);_2=_8}else if(_2+_4>_9){if(_2>=_9){_4=0}else{_4=_4-((_2+_4)-_9)}}
this._dragLine.moveTo(_1,_2);this._dragLine.resizeTo(_3,_4);this._dragLine.show()}
,isc.A.dropOut=function isc_TileLayout_dropOut(){this.hideDragLine()}
,isc.A.dropMove=function isc_TileLayout_dropMove(){this.showDragLineForRecord()}
,isc.A.findIndexForCoord=function isc_TileLayout_findIndexForCoord(_1,_2){var _3=this.getDrawnStartIndex(),_4=this.getDrawnEndIndex();if(_3==null||_4==null){_3=0;_4=this.data.getLength()}
var _5=this.ns.EH;var _6=_5.dragTarget,_7;if(_6)_7=_6.ID;for(var i=_3;i<_4;i++){var _9=this.getRecordTile(i);if(!_9)continue;if(_9.ID==_7)continue;if(_9.getLeft()+_9.getVisibleWidth()>_1&&_9.getTop()+_9.getVisibleHeight()>_2)return i}
return null}
,isc.A.drop=function isc_TileLayout_drop(){var _1=this.$61g||0;var _2=this.ns.EH.dragTarget;var _3=this.$61f;this.$61f=null;if(!isc.isAn.Array(_2))_2=[_2];var _4=_2[0].parentElement;var _5=this.data.get(_1);this.transferRecords(_2,_5,_1,_4);this.reLayout()}
,isc.A.reLayout=function isc_TileLayout_reLayout(){if(this.destroying)return;if(this.animateTileChange){isc.Timer.setTimeout(this.ID+".$61h()",200)}else{this.logDebug('calling layoutTiles from reLayout',"TileLayout");this.layoutTiles()}}
,isc.A.$61h=function isc_TileLayout__animateChange(){this.logDebug("starting $61h()","TileLayout");this.$594=true;this.$62=[];var _1=this.$608=this.$602;this.$61b=[];this.layoutTiles();if(_1!=null){for(var i=0;i<_1.length;i++){if(!this.$602.contains(_1[i])){_1[i].hide()}}}
var _3=this.$61b.length;if(_3==0){this.$61i();return}
for(var i=0;i<_3;i++){var _4=this.$61b[i];_4.show();var _5;if(i==_3-1){_5=_4.animateMove(_4.$61a,_4.$609,this.getID()+".$61i()")}else{_5=_4.animateMove(_4.$61a,_4.$609)}
this.$62.add({ID:_5,tile:_4})}}
,isc.A.$61i=function isc_TileLayout__finishAnimating(){this.$594=false;this.$608=null;delete this.$608;this.$61b=null;delete this.$61b;this.logDebug('calling layoutTiles from $61i',"TileLayout");this.layoutTiles()}
,isc.A.setTileSize=function isc_TileLayout_setTileSize(_1){this.tileSize=_1;this.layoutTiles()}
,isc.A.setTileWidth=function isc_TileLayout_setTileWidth(_1){this.tileWidth=_1;this.layoutTiles()}
,isc.A.setTileHeight=function isc_TileLayout_setTileHeight(_1){this.tileHeight=_1;this.layoutTiles()}
,isc.A.setTileMargin=function isc_TileLayout_setTileMargin(_1){this.tileMargin=_1;this.layoutTiles()}
,isc.A.setTileHMargin=function isc_TileLayout_setTileHMargin(_1){this.tileHMargin=_1;this.layoutTiles()}
,isc.A.setTileVMargin=function isc_TileLayout_setTileVMargin(_1){this.tileVMargin=_1;this.layoutTiles()}
);isc.B._maxIndex=isc.C+50;isc.ClassFactory.defineClass("FlowLayout","TileLayout");isc.A=isc.FlowLayout.getPrototype();isc.A.layoutPolicy="flow";isc.ClassFactory.defineClass("TileGrid","TileLayout","DataBoundComponent");isc.A=isc.TileGrid.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.tileValueStyle="tileValue";isc.A.valuesShowRollOver=false;isc.A.valuesShowSelected=true;isc.A.valuesShowDown=false;isc.A.tileValueAlign="center";isc.A.showLabels=false;isc.A.tileLabelStyle="tileLabel";isc.A.wrapValues=false;isc.A.canSortFields=true;isc.A.autoFetchTextMatchStyle="substring";isc.A.selectionType=isc.Selection.MULTIPLE;isc.A.tileConstructor="SimpleTile";isc.A.detailViewerConstructor="DetailViewer";isc.A.recycleTiles=true;isc.A.animateTileChange=true;isc.A.styleName="tileGrid";isc.A.$60y=false;isc.A.$61j=[];isc.A.$61k=10;isc.A.$35a="Arrow_Up";isc.A.$35b="Arrow_Down";isc.A.$61l="Arrow_Left";isc.A.$61m="Arrow_Right";isc.A.dragAppearance=isc.EH.TRACKER;isc.A.dragTrackerMode="title";isc.A.tileDragAppearance=isc.EH.TRACKER;isc.B.push(isc.A.initWidget=function isc_TileGrid_initWidget(){this.$600();if(isc.FormulaBuilder==null)this.canAddFormulaFields=false;if(isc.SummaryBuilder==null)this.canAddSummaryFields=false;if(this.layoutPolicy=="flow"){isc.logWarn("TileGrid does not support layoutPolicy 'flow'; there may by unexpected behavior."+"Use a TileLayout instead for flow layout.")}
this.$52p();this.invokeSuper(isc.TileLayout,"initWidget");if(!this.tiles)this.tiles=[];if(this.showAllRecords)this.recycleTiles=false;if(this.getDataSource()){this.$61n={};if(this.getDataSource().getPrimaryKeyFieldNames().length==0){this.animateTileChange=false}}
this.detailViewer=this.createAutoChild("detailViewer",{tileGrid:this,showLabel:this.showLabels,showBorder:false,cellStyle:this.tileValueStyle,labelStyle:this.tileLabelStyle,blockStyle:"normal",wrapValues:this.wrapValues,cellPadding:0,valueAlign:this.tileValueAlign,useInnerWidth:false,clipValues:true,width:10,height:10,data:[],dataSource:this.getDataSource(),getCellStyle:function(_2,_3,_4,_5){var _1=(_3.cellStyle||this.cellStyle);if(this.tileGrid.valuesShowRollOver&&this.currentTile.state==isc.StatefulCanvas.STATE_OVER){_1+=this.currentTile.getStateSuffix()}else if(this.tileGrid.valuesShowDown&&this.currentTile.state==isc.StatefulCanvas.STATE_DOWN){_1+=this.currentTile.getStateSuffix()}else if(this.tileGrid.valuesShowSelected&&this.currentTile.isSelected()){_1+=this.currentTile.getStateSuffix()}
return _1}});if(this.fieldState!=null)this.setFieldState(this.fieldState);else this.setFields(this.fields,true);this.membersMargin=this.tileMargin;this.setData(this.data)}
,isc.A.setDataSource=function isc_TileGrid_setDataSource(_1,_2){this.Super("setDataSource",arguments);if(this.getDataSource()){this.$61n={};if(this.getDataSource().getPrimaryKeyFieldNames().length==0){this.animateTileChange=false}}}
,isc.A.shouldUseField=function isc_TileGrid_shouldUseField(_1,_2){if(this.Super("shouldUseField",arguments))return true;if(_2){var _3=isc.DS.get(_2).getIconField();if(_1==_3||_1.name==_3||(_3&&_1.name==_3.name))
{return true}}
return false}
,isc.A.setFields=function isc_TileGrid_setFields(_1,_2){if(!_1&&this.getDataSource()){var _3=this.getDataSource().getIconField();if(_3){_1=[];_1.add({name:_3,type:_3.type});_1.add({name:this.getDataSource().getTitleField()})}}
if(this.completeFields==null)this.fields=[];this.completeFields=this.bindToDataSource(_1);if(this.completeFields==null)this.completeFields=[];if(!this.completeFields)return;this.deriveVisibleFields();this.detailViewer.fields=this.completeFields.duplicate();if(!_2){this.logDebug('calling layoutTiles from setFields',"TileGrid");this.layoutTiles()}}
,isc.A.deriveVisibleFields=function isc_TileGrid_deriveVisibleFields(){this.fields.setArray(this.getVisibleFields(this.completeFields))}
,isc.A.getVisibleFields=function isc_TileGrid_getVisibleFields(_1){var _2=_1.duplicate();for(var i=0;i<_1.length;i++){var _4=_1.get(i);if(!this.fieldShouldBeVisible(_4)||_4.visible==false)_2.remove(_4)}
return _2}
,isc.A.computeTileDimensions=function isc_TileGrid_computeTileDimensions(_1){if(((this.tileHeight&&this.tileWidth)||(this.tileSize))&&!_1)return;if(!((isc.ResultSet&&isc.isA.ResultSet(this.data)&&this.data.resultSize>=this.data.getLength())||isc.isAn.Array(this.data))){return}
if(this.layoutPolicy!="fit")return;var _2=0,_3=0;this.detailViewer.clipValues=false;for(var i=0;i<this.data.getLength();i++){var t=this.getTile(i);var _6=t.overflow;t.setOverflow("visible");t.redraw();t.show();var _7=t.getVisibleHeight();var _8=t.getVisibleWidth();if(_7>_2)_2=_7;if(_8>_3)_3=_8;t.setOverflow(_6);t.hide()}
this.detailViewer.clipValues=true;if(!this.tileHeight&&_2>0)this.tileHeight=_2;if(!this.tileWidth&&_3>0)this.tileWidth=_3}
,isc.A.getTileID=function isc_TileGrid_getTileID(_1){if(!_1)return null;var _2=this.getDataSource();if(_2&&_2.getPrimaryKeyFieldNames().length>0){var _3=_2.getPrimaryKeyFields();var _4="";for(var _5 in _3){_4+=_1[_5]}
return this.$61n[_4]}else{return _1.$61o}}
,isc.A.setTileID=function isc_TileGrid_setTileID(_1,_2){var _3=this.getDataSource();if(_3&&_3.getPrimaryKeyFieldNames().length>0){var _4=_3.getPrimaryKeyFields();var _5="";for(var _6 in _4){_5+=_1[_6]}
this.$61n[_5]=_2}else{_1.$61o=_2}}
,isc.A.getTileRecord=function isc_TileGrid_getTileRecord(_1){var _2=this;var _3=_2.data;var _4,_5;if(isc.isA.ResultSet(this.data)&&!this.data.lengthIsKnown())return null;if(this.showAllRecords||_2.getDrawnStartIndex()==null||_2.getDrawnEndIndex()==null){_4=0;_5=_3.getLength()}else{_4=_2.getDrawnStartIndex();_5=_2.getDrawnEndIndex()+1;if(_5>_3.getLength())_5=_3.getLength()}
for(var i=_4;i<_5;i++){var _7=_3.get(i);if(_2.getTileID(_7)==_1.ID)return _7}
return null}
,isc.A.setTileRecord=function isc_TileGrid_setTileRecord(_1,_2){return null}
,isc.A.setData=function isc_TileGrid_setData(_1){if(this.$594){return false}
if(!_1)return;if(this.data){this.ignore(this.data,"dataChanged");this.ignore(this.data,"dataArrived")}
if(_1)this.data=_1;if(!this.data)return;if(this.data){if(isc.ResultSet&&isc.isA.ResultSet(this.data)){this.observe(this.data,"dataArrived","observer.dataArrived(arguments[0],arguments[1])");this.observe(this.data,"dataChanged","observer.dataChanged(operationType, originalRecord, rowNum, updateData)")}else{this.observe(this.data,"dataChanged","observer.dataChanged()")}}
if(!this.selection||(this.data!=this.selection.data)){this.createSelectionModel()}
this.dataChanged()}
,isc.A.getData=function isc_TileGrid_getData(){return this.data}
,isc.A.getPrimaryKeys=function isc_TileGrid_getPrimaryKeys(_1){var _2=this.data;if(!isc.ResultSet||!isc.isA.ResultSet(_2))return _1;var _3=this.getDataSource(),_4=_3.getPrimaryKeyFieldNames(),_5={};if(!isc.isAn.Array(_4))_4=[_4];for(var i=0;i<_4.length;i++){_5[_4[i]]=_1[_4[i]]}
return _5}
,isc.A.setRecordValues=function isc_TileGrid_setRecordValues(_1,_2){if(!this.data)return;var _3=this.data.indexOf(_1);if(_3==-1)return;var _4=this.data.get(_3);isc.combineObjects(_4,_2);if(this.valuesManager!=null){this.valuesManager.$41(_3,null,_4,this)}
this.logDebug('calling layoutTiles from setRecordValues',"TileGrid");this.layoutTiles()}
,isc.A.dataArrived=function isc_TileGrid_dataArrived(_1,_2){}
,isc.A.dataChanged=function isc_TileGrid_dataChanged(_1,_2,_3,_4){if(!this.data||(isc.ResultSet&&isc.isA.ResultSet(this.data)&&!this.data.lengthIsKnown()))
{this.logDebug("dataChanged: returning due to no data yet","TileGrid");return}
this.computeTileDimensions();if(!this.$61p)this.$61p=0;if(_1=="add"){this.logDebug("add","TileGrid");this.layoutTiles()}else if(_1=="remove"){this.logDebug("remove","TileGrid");if((this.recycleTiles&&this.data.getLength()<this.getDrawnEndIndex()-this.getDrawnStartIndex()+1)||!this.recycleTiles){var _5=this.tiles[_3];this.tiles.remove(_5);_5.destroy()}
this.layoutTiles()}else if(_1=="update"){this.logDebug("update","TileGrid");this.layoutTiles()}else if(this.data.getLength()>=this.$61p){this.logDebug("filter or sort, new data same or longer","TileGrid");if(this.$61p>0)this.$61q();else this.layoutTiles()}else{this.logDebug("filter or sort, new data shorter","TileGrid");this.selection.deselectAll();var _6=this.getDrawnEndIndex()+1;var _7=_6>this.data.getLength()?this.data.getLength():_6;var _8=this.getTilesPerLine();if(Math.floor(_6/ _8)>Math.floor(_7/ _8)&&this.getScrollTop()!=0&&this.recycleTiles){this.scrollToTop();this.layoutTiles()}else{this.$61q()}}
this.$61p=this.data.getLength();if(this.data.getLength()==0){this.cleanupExtraTiles(0)}}
,isc.A.$61q=function isc_TileGrid__layoutAfterDataChange(){if(this.destroying)return;if(this.animateTileChange){if(this.$594){var _1=this.$62;for(var i=0;i<_1.length;i++){this.finishAnimation(_1[i].ID);_1[i].tile.hide()}
return}
this.fireOnPause("tileGridAnimate",this.$61h)}else{this.logDebug('calling layoutTiles from layoutAfterDataChange',"TileGrid");this.layoutTiles()}}
,isc.A.cleanupExtraTiles=function isc_TileGrid_cleanupExtraTiles(_1){var _2=this.tiles;for(var i=_1;i<_2.length;i++){var _4=_2[i];_4.hide();_4.moveTo(0,0)}}
,isc.A.destroy=function isc_TileGrid_destroy(){if(this.data){this.ignore(this.data,"dataChanged");this.ignore(this.data,"dataArrived");if(this.data.$393&&isc.isA.Function(this.data.destroy))
this.data.destroy()}
this.Super("destroy",arguments)}
,isc.A.$61r=function isc_TileGrid__getTileID(_1){return this.ID+"_tile_"+_1}
,isc.A.getLength=function isc_TileGrid_getLength(){if(!this.data||(isc.ResultSet&&isc.isA.ResultSet(this.data)&&!this.data.lengthIsKnown()))return 0;else return this.data.getLength()}
,isc.A.makeTile=function isc_TileGrid_makeTile(_1,_2){var _3={ID:this.$61r(_2),tileNum:_2,canHover:true,handleHover:function(){if(this.creator.itemHover)this.creator.fireCallback("itemHover","item",[this])},mouseDown:function(){this.creator.$61s(this);this.creator.focus()},rightMouseDown:function(){var _4=this.creator.$61t(this);if(_4==false)return false;this.creator.focus();return _4},mouseUp:function(){this.creator.$61u(this)},doubleClick:function(){var _5=this.creator.getTileRecord(this);return this.creator.recordDoubleClick(this.creator,this,_5)}};if(_1.tileProperties)isc.addProperties(_3,_1.tileProperties);var _6=_1.tileConstructor?_1.tileConstructor:this.tileConstructor;var _7=this.createAutoChild("tile",_3,_6);this.detailViewer.setWidth(_7.getInnerWidth());this.detailViewer.setHeight(_7.getInnerHeight());return _7}
,isc.A.getTileHTML=function isc_TileGrid_getTileHTML(_1){return this.detailViewer.getBlockHTML([_1])}
,isc.A.getTile=function isc_TileGrid_getTile(_1){var _2,_3,_4;if(isc.isAn.Object(_1)){_3=_1;_4=this.data.indexOf(_1);_2=this.getTileID(_1)}else{_3=this.data.get(_1);if(!_3)return null;_2=this.$61r(_1);_4=_1}
if(!_2)_2=this.$61r(_4);if(this.canReclaimTile(_4)&&!_3.tileConstructor){var _5=this.$61v(_4);_5.redraw();if(this.selection.isSelected(_3)){_5.setSelected(true)}else{_5.setSelected(false)}
return _5}else if(_2&&window[_2]){var _5=this.$61v(_4,window[_2]);_5.redraw();if(this.selection.isSelected(_3)){_5.setSelected(true)}else{_5.setSelected(false)}
return _5}else{var _6=this.$61r(_4),_7;this.setTileID(_3,_6);_7=this.makeTile(_3,_4);if(!this.tiles)this.tiles=[];this.tiles.add(_7);return _7}}
,isc.A.$61w=function isc_TileGrid__clearLogs(){this.$61j=[]}
,isc.A.$61x=function isc_TileGrid__limitLog(_1,_2){if(!this.$61j.find("key",_2)){this.$61j.add({key:_2,logs:this.$61k})}
if(this.$61j.find("key",_2).logs>0){isc.logWarn(_1);this.$61j.find("key",_2).logs-=1}}
,isc.A.layoutTiles=function isc_TileGrid_layoutTiles(){this.computeTileDimensions();this.invokeSuper(isc.TileGrid,"layoutTiles");var _1=this.tiles?this.tiles.length:0;var _2=this.$603;if(!this.$594&&_2<_1)this.cleanupExtraTiles(_2)}
,isc.A.$61v=function isc_TileGrid__reclaimTile(_1,_2){var _3=this.data.get(_1),_4;if(!_2){var _5=_1-this.getDrawnStartIndex();_4=this.tiles[_5]}else{_4=_2}
var _6=this.getTileRecord(_4);if(_6)this.setTileID(_6,null);this.setTileID(_3,_4.ID);_4.tileNum=_1;return _4}
,isc.A.canReclaimTile=function isc_TileGrid_canReclaimTile(_1){var _2=this.getDrawnStartIndex()||0;if(this.recycleTiles&&this.tiles&&this.tiles.length>_1-_2){return true}else{return false}}
,isc.A.$61t=function isc_TileGrid__tileRightMouseDown(_1){var _2=this.getTileRecord(_1);if(this.recordContextClick!=null){if(this.recordContextClick(this,_1,_2)==false)return false}
return this.$61s(_1)}
,isc.A.$61s=function isc_TileGrid__tileMouseDown(_1){var _2=this.getTileRecord(_1);if(_2)this.selection.selectOnMouseDown(this,_1.tileNum);this.recordClick(this,_1,_2);var _3,_4;if(_1.getTop()<this.getScrollTop()){_4="top"}else if(_1.getTop()+_1.getVisibleHeight()>this.getScrollTop()+this.getInnerHeight()){_4="bottom"}
if(_1.getLeft()<this.getScrollLeft()){_3="left"}else if(_1.getLeft()+_1.getVisibleWidth()>this.getScrollLeft()+this.getInnerWidth()){_3="right"}
if(_3||_4){this.scrollIntoView(_1.getLeft(),_1.getTop(),_1.getVisibleWidth(),_1.getVisibleHeight(),_3,_4,true)}}
,isc.A.$61u=function isc_TileGrid__tileMouseUp(_1){this.selection.selectOnMouseUp(this,_1.tileNum)}
,isc.A.recordClick=function isc_TileGrid_recordClick(){return true}
,isc.A.recordDoubleClick=function isc_TileGrid_recordDoubleClick(){return true}
,isc.A.recordContextClick=function isc_TileGrid_recordContextClick(){}
,isc.A.selectionChange=function isc_TileGrid_selectionChange(_1,_2){if(this.selectionChanged&&(this.selectionChanged(_1,_2)==false))return false;var _3=this.selection,_4=_3.lastSelectionItem;var _5=window[this.getTileID(_4)];if(_5&&_5.setSelected){_5.setSelected(_2)}}
,isc.A.keyPress=function isc_TileGrid_keyPress(_1,_2){if(this.isAnimating("scroll"))return false;var _3=this.selection.lastSelectionItem;if(!_3)return;var _4=_1.keyName,_5=this.selection.data.indexOf(_3),_6=this.orientation=="horizontal",_7;if(_4==this.$35a){_7=_6?this.$61y(_5,"above"):_5-1}else if(_4==this.$35b){_7=_6?this.$61y(_5,"below"):_5+1}else if(_4==this.$61l){_7=_6?_5-1:this.$61y(_5,"above")}else if(_4==this.$61m){_7=_6?_5+1:this.$61y(_5,"below")}else{return}
if(_7==-1||_7>this.data.getLength()-1)return;if(_7==null)return false;var _8=this.selection.data.get(_7),_9=window[this.getTileID(_8)];if(_9){this.$61s(_9)}
return false}
,isc.A.$61y=function isc_TileGrid__adjacentTileIndex(_1,_2){var _3=this.selection.data,_4=_1,_5=window[this.getTileID(_3.get(_4))],_6=this.orientation=="horizontal",_7=_6?_5.getTop():_5.getLeft(),_8=_6?_5.getLeft():_5.getTop(),_9=_6?_5.getVisibleWidth():_5.getVisibleHeight(),_10=_5;while(_7==(_6?_10.getTop():_10.getLeft())){_4=_2=="above"?_4-1:_4+1;if(_4<0||_4>_3.getLength()-1){return-1}
_10=window[this.getTileID(_3.get(_4))];if(!_10)return-1}
var _11=_6?_10.getTop():_10.getLeft();var _12=-1,_13=0;while((_6?_10.getTop():_10.getLeft())==_11){var _14=_6?_10.getLeft():_10.getTop(),_15=_6?_10.getVisibleWidth():_10.getVisibleHeight(),_16=this.$61z([_8,_8+_9],[_14,_14+_15]);if(_16>_13){_12=_4;_13=_16}
_4=_2=="above"?_4-1:_4+1;if(_4<0||_4>_3.getLength()-1)break;_10=window[this.getTileID(_3.get(_4))];if(!_10)break}
return _12}
,isc.A.$61z=function isc_TileGrid__getCommonRange(_1,_2){if((_2[0]>=_1[0]&&_2[0]<=_1[1])||(_2[1]>=_1[0]&&_2[1]<=_1[1])||(_2[0]<=_1[0]&&_2[1]>=_1[1]))
{var _3=_2[0]>_1[0]?_2[0]:_1[0];var _4=_2[1]>_1[1]?_1[1]:_2[1];return _4-_3}
return 0}
,isc.A.addTile=function isc_TileGrid_addTile(){return false}
,isc.A.removeTile=function isc_TileGrid_removeTile(){return false}
,isc.A.getRecordTile=function isc_TileGrid_getRecordTile(_1){if(_1==null)return null;if(_1>=this.data.getLength())return null;var _2=this.getTileID(this.data.get(_1));if(!_2)return null;else return window[_2]}
,isc.A.childVisibilityChanged=function isc_TileGrid_childVisibilityChanged(_1,_2){this.invokeSuper(isc.TileLayout,"childVisibilityChanged",_1,_2)}
,isc.A.hasAllVisibleTiles=function isc_TileGrid_hasAllVisibleTiles(_1,_2){if(isc.isA.ResultSet(this.data)){if(!this.data.lengthIsKnown())return false;var _3=_1[1]+1;if(_3>this.data.getLength())_3=this.data.getLength();if(this.data.rangeIsLoaded(_1[0],_3)){return true}else{if(_2){this.logDebug("in hasAllVisibleTiles, fetching range: "+_1[0]+" to "+_3+", total length: "+this.data.getLength(),"TileGrid");this.data.getRange(_1[0],_3)}
return false}}else{return true}}
,isc.A.$52p=function isc_TileGrid__setUpDragProperties(){this.canReorderTiles=(this.canDrag||this.canReorderTiles);this.canDragTilesOut=(this.canDrag||this.canDragTilesOut);this.canAcceptDroppedRecords=(this.canAcceptDrop||this.canAcceptDroppedRecords)
this.canDrag=(this.canDrag||this.canDragTilesOut||this.canReorderTiles);this.canAcceptDrop=(this.canAcceptDrop||this.canAcceptDroppedRecords||this.canReorderTiles)}
,isc.A.getDragTrackerTitle=function isc_TileGrid_getDragTrackerTitle(_1){var _2=this.getTitleField(),_3=_1[_2];return"<nobr>"+_3+"</nobr>"}
,isc.A.drop=function isc_TileGrid_drop(){var _1=this.$61g||0;if(_1>this.data.getLength())_1=0;var _2=this.ns.EH.dragTarget;var _3=this.$61f;this.$61f=null;var _4=_2.getDataSource(),_5=_2.cloneDragData();var _6=this.data.get(_1);this.transferRecords(_5,_6,_1,_2)}
,isc.A.dropMove=function isc_TileGrid_dropMove(){if(!this.canReorderTiles)return true;if(!this.canAcceptDroppedRecords&&isc.EH.dragTarget!=this)return true;this.showDragLineForRecord()}
,isc.A.dragMove=function isc_TileGrid_dragMove(){var _1=isc.EH.dropTarget;if(!this.canDragTilesOut&&_1!=null&&_1!=this)
{return false}
var _2=this.getSelectedRecord();if(this.tileDragAppearance=="outline"){var _3=this.ns.EH;var _4=this.getTileID(_2);var _5=window[_4];var _6="<div style='width:"+_5.getVisibleWidth()+";height:"+_5.getVisibleHeight()+"'>"+_3.getDragOutline(_5).getInnerHTML()+"</div>";_3.setDragTracker(_6)}else if(this.tileDragAppearance=="target"){var _3=this.ns.EH;var _4=this.getTileID(_2);var _5=window[_4];var _6="<div style='width:"+_5.getVisibleWidth()+";height:"+_5.getVisibleHeight()+"'>"+_5.getInnerHTML()+"</div>";_3.setDragTracker(_6);_5.hide();this.$610=_5;if(!_3.dragMoveAction)_3.dragMoveAction=_3.$nh;if(_3.dragTarget.showDragShadow)_3.$ni();if(_3.dragTarget.dragOpacity!=null)_3.$nj()}}
,isc.A.willAcceptDrop=function isc_TileGrid_willAcceptDrop(){var _1=this.ns.EH;if(!this.Super("willAcceptDrop",arguments))return false;var _2=_1.dragTarget;if(_2==this){if(!this.canReorderTiles)return false}else{if(!this.canAcceptDroppedRecords)return false}
if(!isc.isAn.Object(_2.getDragData()))return false;return true}
,isc.A.dragStop=function isc_TileGrid_dragStop(){this.Super("dropOut",arguments);if(this.$610){this.$610.show();this.$610=null}}
,isc.A.getCellValue=function isc_TileGrid_getCellValue(_1,_2){return this.detailViewer.getStandaloneFieldValue(_1,_2[this.fieldIdProperty])}
,isc.A.getStandaloneFieldValue=function isc_TileGrid_getStandaloneFieldValue(_1,_2){var _3=this.getCellValue(_1,this.getField(_2));return _3}
,isc.A.getTitleFieldValue=function isc_TileGrid_getTitleFieldValue(_1){var _2=this.getDataSource().getTitleField(),_3=this.getCellValue(_1,this.getDataSource().getField(_2));return _3}
,isc.A.hideField=function isc_TileGrid_hideField(_1){this.getField(_1).showIf="false";this.getField(_1).hidden=true;this.fieldStateChanged()}
,isc.A.showField=function isc_TileGrid_showField(_1){this.getField(_1).showIf="true";this.getField(_1).hidden=false;this.fieldStateChanged()}
,isc.A.getField=function isc_TileGrid_getField(_1){if(!this.fields)return null;return isc.Class.getArrayItem(_1,this.fields,this.fieldIdProperty)}
,isc.A.getFields=function isc_TileGrid_getFields(){return this.fields}
,isc.A.getAllFields=function isc_TileGrid_getAllFields(){return this.fields}
,isc.A.setFieldState=function isc_TileGrid_setFieldState(_1){if(isc.isA.String(_1))_1=this.evalViewState(_1,"fieldState")
if(_1){this.completeFields=this.$3w(_1);this.setFields(_1);this.markForRedraw();this.fieldStateChanged()}}
,isc.A.showActionInPanel=function isc_TileGrid_showActionInPanel(_1){if(_1.name=="sort")return true;return this.Super("showActionInPanel",arguments)}
,isc.A.getPrintHTML=function isc_TileGrid_getPrintHTML(){if(!this.data.lengthIsKnown()){isc.logWarn("Attempt to print TileGrid "+this.ID+" while data is loading will be ignored");return""}
var _1=this.data.getLength();if(!this.data.rangeIsLoaded(0,_1)){isc.logWarn("Make sure all data is loaded before attempting to print "+"TileGrid: "+this.ID);return""}
var _2;var _3=this.printTilesPerLine?this.printTilesPerLine:this.getTilesPerLine();if(this.orientation=="horizontal"){var _4=this.getInnerWidth();_2="<table width='"+_4+"'>";for(var i=0;i<_1;i++){var _6=this.getTile(i);if(i%_3==0){if(i==0)_2+="<tr>";else if(i<_1-1)_2+="</tr><tr>"}
_2+="<td>"+_6.getPrintHTML()+"</td>"}
_2+="</tr></table>"}else{_2="<table>";for(var i=0;i<_3;i++){_2+="<tr>";for(var j=i;j<_1;j+=_3){var _6=this.getTile(j);_2+="<td>"+_6.getPrintHTML()+"</td>"}
_2+="</tr>"}
_2+="</table>"}
return _2}
);isc.B._maxIndex=isc.C+63;isc.ClassFactory.defineClass("SimpleTile","StatefulCanvas");isc.A=isc.SimpleTile.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.baseStyle="simpleTile";isc.A.overflow="hidden";isc.A.showRollOver=true;isc.A.redrawOnStateChange=true;isc.A._redrawWithParent=false;isc.B.push(isc.A.initWidget=function isc_SimpleTile_initWidget(){this.invokeSuper(isc.SimpleTile,"initWidget",arguments);this.showDown=this.creator.valuesShowDown}
,isc.A.getInnerHTML=function isc_SimpleTile_getInnerHTML(){this.creator.detailViewer.currentTile=this;var _1=this.creator.getTileRecord(this);if(!_1)return null;return this.creator.getTileHTML(_1)}
,isc.A.getRecord=function isc_SimpleTile_getRecord(){return this.creator.getTileRecord(this)}
);isc.B._maxIndex=isc.C+3;isc.TileGrid.registerStringMethods({dataArrived:"startRecord,endRecord",selectionChanged:"record,state",itemHover:"item",itemClick:"item",recordClick:"viewer,tile,record",recordDoubleClick:"viewer,tile,record",recordContextClick:"viewer,tile,record",fieldStateChanged:""});isc.ClassFactory.defineClass("ColumnTree","Layout","DataBoundComponent");isc.A=isc.ColumnTree;isc.A.TREE_FIELD={name:"treeField",width:"*",getCellValue:function(_1,_2,_3,_4){return _1.creator.getCellValue(_1,_2,_3,_4)}};isc.A=isc.ColumnTree.getPrototype();isc.A.orientation="horizontal";isc.A.animateMemberEffect={effect:"slide",startFrom:"L",endAt:"R"};isc.A.folderIcon="[SKIN]/folder.gif";isc.A.customIconProperty="icon";isc.A.skinImgDir="images/TreeGrid/";isc.A.nodeIcon="[SKIN]file.gif";isc.A.openIconSuffix="open";isc.A.closedIconSuffix="closed";isc.A.showOpenIcons=true;isc.A.showCustomIconOpen=false;isc.A.customIconOpenProperty="showOpenIcon";isc.A.showColumn=true;isc.A.columnConstructor="ListGrid";isc.A.columnDefaults={animateTime:100,animateEffect:"slide",canAddFormulaFields:false,canAddSummaryFields:false,canSort:false,canGroupBy:false,showHeaderMenuButton:false,selectionChanged:function(_1,_2){if(_2){this.creator.nodeSelected(this,_1)}},bodyProperties:{$48f:function(_1,_2,_3,_4,_5){var _6="padding:0px;border:0px;";if(_4==null)_4=this.getTableElement(_2,_3);if(_4==null)return;if(!this.showHiliteInCells)
{if(_1==null)_1=this.getCellRecord(_2,_3);if(_5==null)_5=this.getCellStyle(_1,_2,_3);var _7=_4.childNodes[0];while(_7&&_7.tagName!="TABLE")_7=_7.childNodes[0];if(_7){_7.className=_5;if(this.getCellCSSText){_4.style.cssText=isc.StringBuffer.concat(this.$48g(_1,_2,_3,_5),this.zeroBorderPadding)}}}
return isc.GridRenderer.getPrototype().$48f.apply(this,[_1,_2,_3,_4,_5])}}};isc.A.showHeaders=false;isc.A.firstColumnTitle="&nbsp;";isc.A.showNodeCount=false;isc.A.wrapCells=false;isc.A.iconPadding=3;isc.A.ignoreEmptyCriteria=false;isc.A.backButtonTitle="Back";isc.A.backButtonDefaults={_constructor:"IButton",snapTo:"TR",left:5,top:5,autoFit:true,click:function(){this.creator.navigateBack()}};isc.A.overflow="hidden";isc.A=isc.ColumnTree.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$58v=["<table cellpadding=0 cellspacing=0 class='",,"' style='",,"border:0px;padding:0px;'><tr><td>",,"</td>","<td>"+(isc.Browser.isSafari||isc.Browser.isIE?"<nobr>":""),,,,(isc.Browser.isSafari?"</nobr>":"")+"</td><td style='padding-left:",,"px;'>",,,"</td>","</tr></table>"];isc.A.$58x=["<td>"+(isc.Browser.isSafari||isc.Browser.isIE?"<nobr>":""),,,,(isc.Browser.isSafari?"</nobr>":"")+"</td><td style='padding-left:",,"px;'>",,,"</td>"];isc.A.$581="absmiddle";isc.A.$11z={};isc.B.push(isc.A.getDynamicDefaults=function isc_ColumnTree_getDynamicDefaults(_1){if(_1=="column"){return{autoDraw:false,showHiliteInCells:true,leaveScrollbarGap:false,selectionType:"single",showHeader:false,fields:isc.clone(this.fields)}}}
,isc.A.initWidget=function isc_ColumnTree_initWidget(){this.Super("initWidget",arguments);if(this.showMultipleColumns==null)
this.showMultipleColumns=!isc.Browser.isHandset;this.columns=[];if(!this.dataSource&&this.data!=null&&this.data.dataSource){this.dataSource=this.data.dataSource}
if(!this.fields||this.fields.length==0){this.fields=[isc.ColumnTree.TREE_FIELD]}
if(this.showMultipleColumns==false&&this.showHeaders&&this.showBackButton!=false){this.backButton=this.createAutoChild("backButton",{title:this.backButtonTitle,disabled:true});this.addChild(this.backButton);this.backButton.bringToFront()}
this.columns[0]=this.createAutoChild("column",this.getColumnProperties(this.data?this.data.getRoot():null,0),null,false);this.addColumn(this.columns[0],0);this.currentColumn=0;if(this.data)this.populateFirstColumn()}
,isc.A.populateFirstColumn=function isc_ColumnTree_populateFirstColumn(){if(this.data.showRoot){this.columns[0].setData([this.data.getRoot()])}else{this.columns[0].setData(this.data.getChildren(this.data.getRoot()))}
if(this.shouldShowHeader(null,0)){this.columns[0].setShowHeader(true);this.columns[0].setFieldProperties(0,{title:this.getColumnTitle(null,0)})}}
,isc.A.treeIsTied=function isc_ColumnTree_treeIsTied(_1,_2){return false}
,isc.A.getSelectedRecord=function isc_ColumnTree_getSelectedRecord(){if(this.currentColumn<=0)return this.data.getRoot();var _1=this.getColumn(this.currentColumn-1);return _1.getSelectedRecord()}
,isc.A.navigateBack=function isc_ColumnTree_navigateBack(){if(this.currentColumn<=0)return;var _1,_2;if(this.currentColumn>=2){_1=this.getColumn(this.currentColumn-2);_2=_1.getSelectedRecord()}else{_1=this.getColumn(0);_2=this.data.getRoot()}
this.logInfo("navigating to column: "+(this.currentColumn-1)+" to node: "+this.data.getTitle(_2));this.nodeSelected(_1,_2,true)}
,isc.A.slideTransition=function isc_ColumnTree_slideTransition(_1,_2,_3,_4){if(!isc.Browser.isWebKit){if(_4){_2.deselectAllRecords();_2.animateShow()}else{_1.animateHide();_2.show()}
_2.bringToFront();return}
this.logInfo((_4?"right":"left")+" slideTransition from: "+_1+" to "+_2+" within "+_3);_2.hide();_3.addChild(_2);if(!_2.isDrawn())_2.draw();var _5=_1.getStyleHandle();var _6=_2.getStyleHandle();_6.setProperty("-webkit-transition","none");var _7="translate3d("+(_4?"-":"")+_3.getViewportWidth()+"px, 0%, 0%)";_6.setProperty("-webkit-transform",_7);var _8=_3.overflow;_3.setOverflow("hidden");_2.show();isc.Timer.setTimeout(function(){_5.setProperty("-webkit-transition","-webkit-transform 0.3s ease-in-out");_6.setProperty("-webkit-transition","-webkit-transform 0.3s ease-in-out");_7="translate3d("+(_4?"":"-")+_3.getViewportWidth()+"px, 0%, 0%)";_5.setProperty("-webkit-transform",_7);_6.setProperty("-webkit-transform","translate3d(0px, 0%, 0%)");isc.Timer.setTimeout(function(){_1.hide();_3.setOverflow(_8)},350)},0)}
,isc.A.nodeSelected=function isc_ColumnTree_nodeSelected(_1,_2,_3){if(this.onNodeSelected!=null&&(this.onNodeSelected(_1,_2)==false)){return}
var _4=this.getColumnIndex(_2),_5=this.data.isFolder(_2);var _6=_4+1;if(!_5)_6-=1;var _7=this.columns[_6];if(!this.treeIsTied(_1,_2)){if(this.showMultipleColumns!=false)this.hideColumnsToRight(_6);if(!_5)return;this.data.openFolder(_2);if(isc.isA.ListGrid(_7)){_7.deselectAllRecords();_7.setData(this.data.getChildren(_2));this.addColumn(_7,_6)}else{_7=this.columns[_6]=this.createAutoChild("column",this.getColumnProperties(_2,_4+1),null,false);_7.setData(this.data.getChildren(_2));this.addColumn(_7,_6)}
if(this.shouldShowHeader(_2,_6)){_7.setShowHeader(true);var _8=this.getColumnTitle(_2,_6);_7.setFieldProperties(0,{title:_8})}
if(_7.data.getLength()>0){this.updateHeadingNodeCount(_2)}}
var _9=(_3?this.columns[this.currentColumn]:_1);var _10=_7;if(this.showMultipleColumns==false){this.slideTransition(_9,_10,this,_3?true:false)}else{_10.show()}
this.currentColumn=(_6<0?0:_6);this.logInfo("currentColumn is now: "+this.currentColumn);if(this.backButton){this.backButton.bringToFront();this.backButton.setDisabled(this.currentColumn<=0)}}
,isc.A.addColumn=function isc_ColumnTree_addColumn(_1,_2){if(this.showMultipleColumns==false){_1.resizeTo("100%","100%");this.addChild(_1,_2)}else{this.addMember(_1,_2)}}
,isc.A.getCurrentTitle=function isc_ColumnTree_getCurrentTitle(){return this.columns[this.currentColumn].getFieldTitle(0)}
,isc.A.getPreviousTitle=function isc_ColumnTree_getPreviousTitle(){if(this.currentColumn<=0)return"";return this.columns[this.currentColumn-1].getFieldTitle(0)}
,isc.A.updateHeadingNodeCount=function isc_ColumnTree_updateHeadingNodeCount(_1){var _2=this.getColumnIndex(_1);if(!this.shouldShowHeader(_1,_2)||!this.showNodeCount)return;if(_2<0)return;if(this.columns[_2+1].data.getLength()==0)return;var _3=this.data.getTitle(_1);if(this.showNodeCount){_3=_3+" ("+this.columns[_2+1].data.getLength()+")"}
this.columns[_2+1].setFieldProperties(0,{title:_3})}
,isc.A.getColumnIndex=function isc_ColumnTree_getColumnIndex(_1){if(this.data.showRoot){return this.data.getLevel(_1)}else{var _2=this.data.getLevel(_1);return _2-1}}
,isc.A.hideColumnsToRight=function isc_ColumnTree_hideColumnsToRight(_1){for(var i=_1+1;i<this.columns.length;i++){this.columns[i].hide();this.columns[i].deselectAllRecords()}}
,isc.A.shouldShowHeader=function isc_ColumnTree_shouldShowHeader(_1,_2){return this.showHeaders}
,isc.A.getColumnTitle=function isc_ColumnTree_getColumnTitle(_1,_2){if(_2==0){return this.firstColumnTitle}else{return this.data.getTitle(_1)}}
,isc.A.getRecord=function isc_ColumnTree_getRecord(_1,_2){if(_1==null||_1<0)return null;if(_2!=null){if(_2<0||_2>this.columns.length){return null}
if(_1>this.columns[_2].data.length||!this.columns[_2].isVisible()){return null}
return this.columns[_2].data[_1]}
var _3=0;for(var _4=0;_4<this.columns.length;_4++){if(!this.columns[_4].isVisible())continue;if(_3+this.columns[_4].data.length>_1){return this.columns[_4].data[_1-_3]}
_3+=this.columns[_4].data.length}
return null}
,isc.A.getTreeCellValue=function isc_ColumnTree_getTreeCellValue(_1,_2,_3,_4,_5){if(_3==null){return _1}
var _6=this.$58v;_6[1]=_2.getCellStyle(_3,_4,_5);_6[3]=_2.getCellCSSText(_3,_4,_5);var _7=this.$586(_1,_3,_4,true);for(var i=0;i<10;i++){_6[6+i]=_7[i]}
return _6.join(isc.emptyString)}
,isc.A.$586=function isc_ColumnTree__getTreeCellTitleArray(_1,_2,_3,_4){var _5=this.$58x;_5[1]=null;var _6=this.getIcon(_2),_7=(_3!=null?this.$58y+_3:null);_5[2]=null;_5[3]=this.getIconHTML(_6,_7,_2.iconSize);_5[5]=this.iconPadding;_5[7]=this.wrapCells?null:"<NOBR>"
_5[8]=_1;return _5}
,isc.A.getCellValue=function isc_ColumnTree_getCellValue(_1,_2,_3,_4,_5,_6,_7,_8){var _9=this.getNodeTitle(_2,_3);_9=this.getTreeCellValue(_9,_1,_2,_3,_4);return _9}
,isc.A.getIcon=function isc_ColumnTree_getIcon(_1,_2){if(isc.isA.Number(_1))_1=this.data.get(_1);if(!_1)return null;var _3=_1[this.customIconProperty],_4=(_3!=null),_5=this.data.isFolder(_1);if(!_4){if(_5)_3=this.folderIcon;else _3=this.nodeIcon}
var _6;if(_5){var _7=_2?false:(this.lastDropFolder==_1&&_1.$59b),_8=_2?false:!!this.data.isOpen(_1);if(_7){if(_1.dropIcon!=null)_3=_1.dropIcon;else if(!_4&&this.folderDropImage!=null)_3=this.folderDropImage;else{var _9;if(_4){_9=_1[this.customIconDropProperty];if(_9==null)_9=this.showCustomIconDrop}else{_9=this.showDropIcons}
if(_9)_6=this.dropIconSuffix}}else if(_8){if(_1.openedIcon!=null)_3=_1.openedIcon;else if(!_4&&this.folderOpenImage!=null)_3=this.folderOpenImage;else{var _10;if(_4){_10=_1[this.customIconOpenProperty];if(_10==null)_10=this.showCustomIconOpen}else{_10=this.showOpenIcons}
if(_10)_6=this.openIconSuffix;else if(!_4)_6=this.closedIconSuffix}}else{if(!_4){if(this.folderClosedImage)_3=this.folderClosedImage;else _6=this.closedIconSuffix}}}else{if(!_4&&this.fileImage)_3=this.fileImage}
return isc.Img.urlForState(_3,false,false,_6)}
,isc.A.getIconHTML=function isc_ColumnTree_getIconHTML(_1,_2,_3){if(_1==null)return isc.emptyString;if(_3==null)_3=this.iconSize;var _4=this.$11z;_4.src=_1;_4.width=_4.height=_3;_4.name=_2;_4.align=this.$581;var _5=this.$1i(_4);_5[14]=_2;return _5.join(isc.$ah)}
,isc.A.getNodeTitle=function isc_ColumnTree_getNodeTitle(_1,_2,_3){return this.data.getTitle(_1)}
,isc.A.getData=function isc_ColumnTree_getData(){return this.data}
,isc.A.setData=function isc_ColumnTree_setData(_1,_2,_3,_4){if(!isc.isA.Tree(_1))return;this.data=_1;this.data.columnTree=this;this.data.dataArrived="this.columnTree.updateHeadingNodeCount(parentNode);";this.data.separateFolders=this.separateFolders;if(this.showRoot&&isc.isA.ResultTree(this.data)){this.logWarn("showRoot may not be set with a databound columnTree, unexpected "+"results may occur")}
this.data.showRoot=this.showRoot;this.data.openDisplayNodeType=this.displayNodeType;this.data.openFolder(this.data.root);this.populateFirstColumn()}
,isc.A.useExistingDataModel=function isc_ColumnTree_useExistingDataModel(_1,_2,_3){return false}
,isc.A.createDataModel=function isc_ColumnTree_createDataModel(_1,_2,_3){return this.createResultTree(_1,_3.afterFlowCallback,_3,null)}
,isc.A.updateDataModel=function isc_ColumnTree_updateDataModel(_1,_2,_3){}
,isc.A.getColumn=function isc_ColumnTree_getColumn(_1){if(isc.isAn.Object(_1)){var _2=this.getColumnIndex(_1)+1;if(this.columns[_2]&&this.columns[_2].isVisible())return this.columns[_2]}else{if(this.columns[_1]&&_1<=this.currentColumn)return this.columns[_1]}
return null}
,isc.A.getColumnProperties=function isc_ColumnTree_getColumnProperties(_1,_2){}
,isc.A.selectAllRecords=function isc_ColumnTree_selectAllRecords(_1){if(_1==null)_1=0;if(!this.columns[_1])return;this.columns[_1].selectAllRecords()}
,isc.A.deselectAllRecords=function isc_ColumnTree_deselectAllRecords(_1){if(_1==null)_1=0;if(!this.columns[_1])return;this.columns[_1].deselectAllRecords()}
,isc.A.anySelected=function isc_ColumnTree_anySelected(_1){if(_1==null)_1=0;if(!this.columns[_1])return false;return this.columns[_1].anySelected()}
,isc.A.getSelection=function isc_ColumnTree_getSelection(_1){if(_1==null)_1=0;if(!this.columns[_1])return[];return this.columns[_1].getSelection()}
,isc.A.getSelectionObject=function isc_ColumnTree_getSelectionObject(_1){if(_1==null)_1=0;if(!this.columns[_1])return null;return this.columns[_1].selection}
);isc.B._maxIndex=isc.C+35;isc.ColumnTree.registerStringMethods({nodeSelected:"column, node",onNodeSelected:"column,node"})
isc.ClassFactory.defineClass("TableView","ListGrid");isc.A=isc.TableView;isc.A.PLAIN="plain";isc.A.GROUPED="grouped";isc.A.TITLE_ONLY="titleOnly";isc.A.TITLE_DESCRIPTION="titleAndDescription";isc.A.SUMMARY_INFO="summaryInfo";isc.A.SUMMARY_DATA="summaryData";isc.A.SUMMARY_FULL="summaryFull";isc.A.WHOLE_RECORD="wholeRecord";isc.A.NAVICON_ONLY="navIconOnly";isc.A=isc.TableView.getPrototype();isc.A.iconField="icon";isc.A.showIconField=true;isc.A.titleField="title";isc.A.infoField="info";isc.A.dataField="data";isc.A.descriptionField="description";isc.A.recordNavigationProperty="$611";isc.A.tableMode=isc.TableView.PLAIN;isc.A.recordLayout=isc.TableView.TITLE_ONLY;isc.A.navIcon="[SKINIMG]/iOS/listArrow_button.png";isc.A.wholeRecordNavIcon="[SKINIMG]/iOS/listArrow.png";isc.A.navigationMode=isc.TableView.WHOLE_RECORD;isc.A.recordTitleStyle="recordTitle";isc.A.recordDescriptionStyle="recordDescription";isc.A.recordDataStyle="recordData";isc.A.recordInfoStyle="recordInfo";isc.A.iconFieldDefaults={width:50,imageSize:30,align:"center",type:"image"};isc.A.titleFieldDefaults={name:"TVtitleField",width:"*",type:"text",formatCellValue:function(_1,_2,_3,_4,_5){if(_5.formatRecord!=null){return _5.formatRecord(_2)}
var _6=_5.$612(_2,_5.titleField),_7=_5.$612(_2,_5.descriptionField),_8=_5.$612(_2,_5.infoField),_9=_5.$612(_2,_5.dataField),_10="";if(_5.recordLayout==isc.TableView.SUMMARY_INFO||_5.recordLayout==isc.TableView.SUMMARY_FULL)
{_10+="<span class='"+_5.recordInfoStyle+"'>"+_8+"</span>"}
_10+="<span class='"+_5.recordTitleStyle+"'>"+_6+"</span>";if(_5.recordLayout!=isc.TableView.TITLE_ONLY){_10+="<span class='"+_5.recordDescriptionStyle+"'>"+_7+"</span>"}
if(_5.recordLayout==isc.TableView.SUMMARY_DATA||_5.recordLayout==isc.TableView.SUMMARY_FULL)
{_10+="<span class='"+_5.recordDataStyle+"'>"+_9+"</span>"}
return _10}};isc.A.navigationFieldDefaults={name:"TVnavigationField",width:54,align:"right",formatCellValue:function(_1,_2,_3,_4,_5){if(_5.getShowNavigation(_2)){var _6=isc.Img.create({autoDraw:false,autoFit:true,imageType:"normal",src:_5.getNavigationIcon(_2)});return _6.getInnerHTML()}
return _5.$sd}};isc.A.groupByFieldDefaults={showIf:"false"};isc.A.canAddFormulaFields=false;isc.A.canAddSummaryFields=false;isc.A.showHeader=false;isc.A.selectionType="none";isc.A.skinImgDir="images/iOS/";isc.A.baseStyle="tableCell";isc.A.border="0px";isc.A.wrapCells=false;isc.A.cellHeight=44;isc.A.alternateRecordStyles=false;isc.A.canCollapseGroup=false;isc.A.groupStartOpen="all";isc.A.ignoreEmptyCriteria=false;isc.A=isc.TableView.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.initWidget=function isc_TableView_initWidget(){this.Super("initWidget",arguments);this.$613={};if(this.formatRecord!=null&&!isc.isA.Function(this.formatRecord))
isc.Func.replaceWithMethod(this,"formatRecord","record")}
,isc.A.setFields=function isc_TableView_setFields(_1){this.invokeSuper(isc.TableView,"setFields",this.$614(_1))}
,isc.A.$614=function isc_TableView__defineTableFields(_1){var _2=_1||[];for(var i=0;i<_2.length;i++){_2[i].showIf="false"}
if(this.showIconField){var _4=_2.find(this.fieldIdProperty,this.iconField);if(_4)_2.remove(_4);this.$615=_2.length;_2[_2.length]=isc.addProperties({name:this.iconField},this.iconFieldDefaults,this.iconFieldProperties)}
var _4=_2.find(this.fieldIdProperty,this.titleFieldDefaults.name);if(_4)_2.remove(_4);_2[_2.length]=isc.addProperties({},this.titleFieldDefaults,this.titleFieldProperties);var _4=_2.find(this.fieldIdProperty,this.navigationFieldDefaults.name);if(_4)_2.remove(_4);this.$616=_2.length;_2[_2.length]=isc.addProperties({},this.navigationFieldDefaults,this.navigationFieldProperties);if(this.groupByField){var _5;if(isc.isA.Array(this.groupByField)){_5=this.groupByField}else{_5=[this.groupByField]}
for(var i=0;i<_5.length;i++){var _6=_2.find(this.fieldIdProperty,_5[i]);if(_6){isc.addProperties(_6,this.groupByFieldDefaults,this.groupByFieldProperties)}else{_2[_2.length]=isc.addProperties({name:_5[i]},this.groupByFieldDefaults,this.groupByFieldProperties)}}}
return _2}
,isc.A.$612=function isc_TableView__getFormattedFieldValue(_1,_2){var _3=_1[_2]||this.$sd,_4=this.$613[_2],_5;if(_4==null||_4==_5){_4=isc.Class.getArrayItemIndex(_2,this.getAllFields(),this.fieldIdProperty);this.$613[_2]=_4}
if(_4>=0){_3=this.getFormattedValue(_1,_2,_3)}
return _3}
,isc.A.getNavigationIcon=function isc_TableView_getNavigationIcon(_1){return(this.navigationMode==isc.TableView.NAVICON_ONLY?this.navIcon:this.wholeRecordNavIcon)}
,isc.A.getShowNavigation=function isc_TableView_getShowNavigation(_1){if(_1&&_1[this.recordNavigationProperty]!=null){return _1[this.recordNavigationProperty]}
return this.showNavigation}
,isc.A.canSelectRecord=function isc_TableView_canSelectRecord(_1){return this.body.canSelectRecord(_1)}
,isc.A.recordClick=function isc_TableView_recordClick(_1,_2,_3,_4,_5,_6,_7){if(_5!=this.$615&&_5!=this.$616&&this.canSelectRecord(_2))
{this.selectSingleRecord(_2)}
if(_5==this.$616||this.navigationMode==isc.TableView.WHOLE_RECORD){if(this.recordNavigationClick){isc.Func.replaceWithMethod(this,"recordNavigationClick","record");this.recordNavigationClick(_2)}}else if(_5==this.$615){if(this.imageClick){isc.Func.replaceWithMethod(this,"imageClick","record");this.imageClick(_2)}}}
,isc.A.getBaseStyle=function isc_TableView_getBaseStyle(_1,_2,_3){if(this.isGrouped){var _4=this.data.get(_2),_5=this.data.isFirst(_4),_6=this.data.isLast(_4);if(_5&&_6){return(_3==0?"cellOnlyLeft":(_3==this.fields.length-1?"cellOnlyRight":"cellOnly"))}else if(_5){return(_3==0?"cellTopLeft":(_3==this.fields.length-1?"cellTopRight":"cellTop"))}else if(_6){return(_3==0?"cellBottomLeft":(_3==this.fields.length-1?"cellBottomRight":"cellBottom"))}}
return this.Super("getBaseStyle",arguments)}
);isc.B._maxIndex=isc.C+9;isc.TableView.registerStringMethods({recordNavigationClick:"record",imageClick:"record",formatRecord:"record"});isc.ClassFactory.defineClass("DOMTree","Tree");isc.A=isc.DOMTree.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.tagNameProperty="tagName";isc.A.elementProperty="$18q";isc.A.loadOnInit=true;isc.A.loadDataOnDemand=true;isc.A.hideTextNodes=true;isc.B.push(isc.A.makeRoot=function isc_DOMTree_makeRoot(){var _1=this.Super("makeRoot",arguments);_1[this.elementProperty]=this.rootElement;return _1}
,isc.A.getElement=function isc_DOMTree_getElement(_1){return _1[this.elementProperty]}
,isc.A.getElementTitle=function isc_DOMTree_getElementTitle(_1){var _2=_1.getAttribute(this.titleProperty);if(!this.valueIsEmpty(_2))return _2;_2=_1.getAttribute(this.nameProperty);if(!this.valueIsEmpty(_2))return _2;if(!isc.xml.hasElementChildren(_1)){_2=isc.xml.getElementText(_1);if(!this.valueIsEmpty(_2))return _2}
return _1.tagName||_1.nodeName}
,isc.A.valueIsEmpty=function isc_DOMTree_valueIsEmpty(_1){return _1==null||isc.isAn.emptyString(_1)}
,isc.A.isFolder=function isc_DOMTree_isFolder(_1){if(_1==this.root||_1.children!=null)return true;var _2=_1[this.elementProperty];if(!_2||!_2.childNodes||_2.childNodes.length==0)return false;if(!this.hideTextNodes)return true;return isc.xml.hasElementChildren(_2)}
,isc.A.moveList=function isc_DOMTree_moveList(_1,_2,_3){var _4=_1[0],_5=this.getElement(_4);this.logWarn("moveList: "+this.echoAll(_1)+", newParent: "+this.echo(_2)+", index: "+_3);this.$617(_5,_2,_3);this.Super("moveList",arguments)}
,isc.A.remove=function isc_DOMTree_remove(_1){var _2=this.getElement(_1);_2.parentNode.removeChild(_2);return this.Super("remove",arguments)}
,isc.A.addElement=function isc_DOMTree_addElement(_1,_2,_3){this.$617(_1,_2,_3);if(this.isLoaded(_2)){var _4=this.nodeForElement(_1);this.add(_4,_2,_3)}else{this.dataChanged()}}
,isc.A.$617=function isc_DOMTree__addToDOM(_1,_2,_3){var _4=this.getElement(_2);if(_3==null){this.logWarn("appending: "+this.echoLeaf(_1)+" to: "+this.echoLeaf(_4));_4.appendChild(_1)}else{var _5=this.getChildren(_2)[_3],_6=this.getElement(_5);this.logWarn("inserting into: "+this.echoLeaf(_4)+", before: "+this.echoLeaf(_6));_4.insertBefore(_1,_6)}}
,isc.A.nodeForElement=function isc_DOMTree_nodeForElement(_1){var _2={};_2[this.elementProperty]=_1;_2[this.titleProperty]=this.getElementTitle(_1);if(this.tagNameProperty){_2[this.tagNameProperty]=_1.tagName||_1.nodeName}
if(this.copyAttributes){for(var j=0;j<this.copyAttributes.length;j++){var _4=this.copyAttributes[j];_2[_4]=_1.getAttribute(_4)}}
return _2}
,isc.A.loadChildren=function isc_DOMTree_loadChildren(_1){if(this.isLoaded(_1))return;try{var _2=_1.$18q;if(_2==null)return;var _3=_2.childNodes;if(isc.Browser.isMoz&&_2.contentDocument){_3=[_2.contentDocument.documentElement]}else{if(this.loadingBatch()&&!isc.xml.hasElementChildren(_2))return}
_1[this.openProperty]=true;if(_3!=null){for(var i=0;i<_3.length;i++){var _5=_3[i];if(this.hideTextNodes&&_5.nodeName.startsWith("#"))continue;var _6=this.nodeForElement(_5);this.add(_6,_1)}}
this.setLoadState(_1,isc.Tree.LOADED)}catch(e){this.logWarn("parent node: "+this.echo(_1)+", at path: "+this.getPath(_1)+", error: "+this.echo(e)+this.getStackTrace())}}
);isc.B._maxIndex=isc.C+11;isc.defineClass("DOMGrid","TreeGrid");isc.A=isc.DOMGrid.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.canDragRecordsOut=true;isc.A.canAcceptDroppedRecords=true;isc.A.canReorderRecords=true;isc.A.showRoot=true;isc.A.$618="&lt;";isc.A.$619="&gt;";isc.A.$62a=[" ",,'="',,'"'];isc.A.colorMap={table:"#009900",tr:"#333399",td:"#663366",form:"#CC6600",input:"#3333FF",textarea:"#3333FF",div:"#663300",span:"#663300"};isc.A.colorPrefix="color:";isc.A.$62b={};isc.B.push(isc.A.initWidget=function isc_DOMGrid_initWidget(){this.Super(this.$sb);if(this.url){isc.xml.loadXML(this.url,this.getID()+".setRootElement(xmlDoc.documentElement)")}}
,isc.A.getDefaultData=function isc_DOMGrid_getDefaultData(){return null}
,isc.A.getElement=function isc_DOMGrid_getElement(_1){return this.data.getElement(_1)}
,isc.A.setRootElement=function isc_DOMGrid_setRootElement(_1){this.rootElement=_1;var _2=isc.DOMTree.create({rootElement:_1});this.setData(_2)}
,isc.A.getIcon=function isc_DOMGrid_getIcon(){}
,isc.A.getNodeTitle=function isc_DOMGrid_getNodeTitle(_1,_2,_3){if(_1==null)return null;if(_1.$18v)return _1.$18v;var _4=this.htmlMode?this.$62c(_1):this.$62d(_1);return(_1.$18v=_4)}
,isc.A.dataChanged=function isc_DOMGrid_dataChanged(){this.Super("dataChanged",arguments);this.data.getOpenList().setProperty("$18v",null)}
,isc.A.$62d=function isc_DOMGrid__getXMLNodeTitle(_1){if(_1.$18q==null){this.logWarn("no element for node: "+this.echo(_1))}
var _2=_1.$18q,_3=isc.emptyString,_4=this.$62e;if(_4==null)_4=this.$62e=isc.SB.create();else _4.clear();_4.append(this.$618,(_2.tagName||_2.nodeName));var _5=_2.attributes;if(_5!=null){var _6=this.$62a;for(var i=0;i<_5.length;i++){var _8=_5[i];_6[1]=_8.name;_6[3]=_8.value;_4.append(_6)}}
if(!isc.xml.hasElementChildren(_2)){_4.append(this.$619,isc.xml.getElementText(_2),"&lt;/",(_2.tagName||_2.nodeName),this.$619)}else if(_2.childNodes.length>0){_4.append(this.$619)}else{_4.append("/&gt;")}
return _4.toString()}
,isc.A.$62c=function isc_DOMGrid__getHTMLNodeTitle(_1){var _2=_1.$18q,_3=isc.emptyString,_4,_5;if(isc.Browser.isIE&&_2.scopeName=="VML"){_4=(_2.style?_2.style.width:null);_5=(_2.style?_2.style.height:null)}else{_4=_2.width||(_2.style?_2.style.width:null);_5=_2.height||(_2.style?_2.style.height:null)}
var _6=(_2.tagName&&_2.tagName.toLowerCase()=="td");return isc.SB.concat(this.$618,(_2.tagName||_2.nodeName),(_2.id?" ID="+_2.id:_3),(!this.valueIsEmpty(_4)?" WIDTH="+_4:_3),(!this.valueIsEmpty(_5)?" HEIGHT="+_5:_3),(_6&&_2.rowSpan>1?" ROWSPAN="+_2.rowSpan:_3),(_6&&_2.colSpan>1?" COLSPAN="+_2.colSpan:_3),this.$619)}
,isc.A.valueIsEmpty=function isc_DOMGrid_valueIsEmpty(_1){return _1==null||isc.isAn.emptyString(_1)}
,isc.A.getCellCSSText=function isc_DOMGrid_getCellCSSText(_1,_2,_3){var _4=this.data.getElement(_1);if(_4==null)return null;var _5=_4.tagName;if(_5==null)return null;if(this.$62b[_5]){_5=this.$62b[_5]}else{this.$62b=_5=_5.toLowerCase()}
if(this.colorMap[_5]!=null){return isc.SB.concat(this.colorPrefix,this.colorMap[_5],isc.semi)}}
);isc.B._maxIndex=isc.C+11;isc.ClassFactory.defineClass("MenuBar","Toolbar");isc.addGlobal("Menubar",isc.MenuBar);isc.A=isc.MenuBar.getPrototype();isc.A.overflow=isc.Canvas.VISIBLE;isc.A.defaultHeight=22;isc.A.menuConstructor="Menu";isc.A.buttonConstructor="MenuBarButton";isc.A.tabIndex=-1;isc.A.tabWithinToolbar=false;isc.A.buttonDefaults={showDown:false,showRollOver:true,showFocused:true,showFocusedAsOver:true};isc.A=isc.MenuBar.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.initWidget=function isc_MenuBar_initWidget(){this.Super("initWidget",arguments)}
,isc.A.setButtons=function isc_MenuBar_setButtons(){var _1=[];if(this.menus){for(var i=0;i<this.menus.length;i++){var _3=this.menus[i];_1[i]=this.$62f(_3,i)}}
return this.Super("setButtons",[_1],arguments)}
,isc.A.$62f=function isc_MenuBar__getButtonProperties(_1,_2){return{title:_1.title,width:(_1.menuButtonWidth?_1.menuButtonWidth:_1.width),menuNum:_2,focusChanged:function(_3){if(isc.Browser.isMoz&&_3)this.bringToFront()}}}
,isc.A.setMenus=function isc_MenuBar_setMenus(_1){if(!isc.isAn.Array(_1))_1=[_1];for(var i=0;i<this.members.length;i++){var _3=this.members[i],_4=this.menus[_3.menuNum];if(_3.isObserving(_4,"hide")){_3.ignore(_4,"hide")}}
this.menus=_1;this.setButtons()}
,isc.A.$62g=function isc_MenuBar__remapButton(_1,_2){if(!_1)return;if(_2==-1){var _3=_1.menuNum,_4=this.menus[_1.menuNum];if(_1.isObserving(_4,"hide")){_1.ignore(_4,"hide")}}
_1.menuNum=_2}
,isc.A.addMenus=function isc_MenuBar_addMenus(_1,_2){if(!_1)return;if(!isc.isAn.Array(_1))_1=[_1];if(!this.menus)this.menus=[];if(_2==null)_2=this.menus.length;if(!this.$13k){this.menus.addListAt(_1,_2)}else{for(var i=_2;i<this.members.length;i++){this.$62g(this.members[i],(i+_1.length))}
this.menus.addListAt(_1,_2);var _4=[];for(var i=0;i<_1.length;i++){var _5=this.menus.indexOf(_1[i]);_4[i]=this.$62f(_1[i],_5)}
this.addButtons(_4,_2)}}
,isc.A.removeMenus=function isc_MenuBar_removeMenus(_1){if(_1==null)return;if(!isc.isAn.Array(_1))_1=[_1];var _2=[],_3=this.menus.duplicate();for(var i=0;i<_1.length;i++){var _5=_1[i];if(isc.isA.Number(_5))_5=this.menus[_5];else if(!this.menus.contains(_5))continue;_3.remove(_5);var _6=this.menus.indexOf(_5);if(this.$13k)_2.add(this.members[_6])}
if(!this.$13k){this.menus=_3;return}
for(var i=0;i<this.menus.length;i++){if(this.menus[i]==_3[i])continue;this.$62g(this.members[i],_3.indexOf(this.menus[i]))}
this.menus=_3;this.removeButtons(_2)}
,isc.A.showMenu=function isc_MenuBar_showMenu(_1){var _2;if(isc.isA.Number(_1))_2=this.menus[_1];else{_2=_1;_1=this.menus.indexOf(_2)}
if(!_2){this.logWarn("showMenu() called with invalid menu number: "+_1+".  No effect.");return}
var _3;for(var i=0;i<this.members.length;i++){if(this.members[i].menuNum==_1){_3=this.members[i]}}
if(!isc.isA.Canvas(_2)){if(_2.ID==null)_2.ID=this.getID()+"_menu"+_1;_2.autoDraw=false;_2=this.menus[_1]=isc.ClassFactory.newInstance(this.menuConstructor,_2,this.menuDefaults)}
if(this.activeMenu!=null){this.menus[this.activeMenu].hideMenuTree()}
_2.keyEventParent=this;_2.moveTo(_3.getPageLeft(),_3.getPageBottom());_2.show();_3.$62h=_3.showRollOver;_3.showRollOver=false;_3.setState(isc.StatefulCanvas.STATE_DOWN);this.activeMenu=_1;if(!_3.isObserving(_2,"hide")){_3.observe(_2,"hide","observer.menuHidden(observed)")}
var _5=isc.EH;if(_5.targetIsMasked(this))this.bringToFront();var _6=_5.clickMaskRegistry.last(),_7=_5.getMaskedFocusCanvas(_6);if(this.members.contains(_7))_5.setMaskedFocusCanvas(null,_6);_2.body.focusOnHide=_3}
,isc.A.$13l=function isc_MenuBar__focusInNextButton(_1){if(!this.activeMenu==null)return this.Super("$13l",arguments);if(_1==null)_1=true;var _2=this.activeMenu,_3=_1?1:-1,_4=_2+_3,_5=this.getMembers();while(_2!=_4){if(_4<0)_4=_5.length-1;else if(_4>=this.members.length)_4=0;var _6=_5[_4];if(!_6.isDisabled()){_6.showMenu();break}
_4+=_3}}
,isc.A.getFocusButtonIndex=function isc_MenuBar_getFocusButtonIndex(){if(this.activeMenu!=null)return this.activeMenu;return this.Super("getFocusButtonIndex",arguments)}
);isc.B._maxIndex=isc.C+10;isc.ClassFactory.defineClass("MenuBarButton","MenuButton");isc.A=isc.MenuBarButton.getPrototype();isc.A.showMenuButtonImage=false;isc.A.showDown=false;isc.A.autoDraw=false;isc.A.align="center";isc.A=isc.MenuBarButton.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.mouseOver=function isc_MenuBarButton_mouseOver(){this.Super("mouseOver",arguments);var _1=this.parentElement.activeMenu;if(_1!=null&&_1!=this.menuNum){this.showMenu()}}
,isc.A.mouseDown=function isc_MenuBarButton_mouseDown(){if(this.parentElement.activeMenu==this.menuNum){isc.Menu.hideAllMenus()}else{this.showMenu()}}
,isc.A.mouseUp=function isc_MenuBarButton_mouseUp(){}
,isc.A.click=function isc_MenuBarButton_click(){}
,isc.A.mouseOut=function isc_MenuBarButton_mouseOut(){if(this.parentElement.activeMenu!=this.menuNum){this.Super("mouseOut",arguments)}}
,isc.A.handleKeyPress=function isc_MenuBarButton_handleKeyPress(_1,_2){if(_1.keyName=="Space"||_1.keyName=="Enter")return this.showMenu();if(this.keyPress){this.convertToMethod("keyPress");return this.keyPress(_1,_2)}}
,isc.A.showMenu=function isc_MenuBarButton_showMenu(){this.parentElement.showMenu(this.menuNum)}
,isc.A.menuHidden=function isc_MenuBarButton_menuHidden(_1){if(isc.$dd)arguments.$de=this;if(this.state==isc.StatefulCanvas.STATE_DOWN){if(this.hasFocus&&this.showFocused)this.setState(isc.StatefulCanvas.STATE_OVER);else this.setState(isc.StatefulCanvas.STATE_UP)}
this.showRollOver=this.$62h;delete this.$62h;this.menuIsDown=false;if(this.parentElement.activeMenu==this.menuNum){this.parentElement.activeMenu=null}
delete _1.eventParent;this.ignore(_1,"hide")}
);isc.B._maxIndex=isc.C+8;isc.ClassFactory.defineClass("CellSelection");isc.A=isc.CellSelection;isc.A.$445=0;isc.A.COL_SELECTION_FLAGS=null;isc.A=isc.CellSelection;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.generateFlagTable=function isc_c_CellSelection_generateFlagTable(){isc.CellSelection.COL_SELECTION_FLAGS=[];for(var i=0;i<32;i++)
isc.CellSelection.COL_SELECTION_FLAGS[i]=Math.pow(2,i)}
);isc.B._maxIndex=isc.C+1;isc.A=isc.CellSelection.getPrototype();isc.A.data=null;isc.A.numCols=0;isc.A.selectionProperty=null;isc.A.$vi=true;isc.A.$62i=[];isc.A.lastSelectedCell=[];isc.A.changedCells=[];isc.A=isc.CellSelection.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.init=function isc_CellSelection_init(){if(!isc.CellSelection.COL_SELECTION_FLAGS)isc.CellSelection.generateFlagTable();if(!this.selectionProperty)this.selectionProperty="_cellSelection_"+isc.CellSelection.$445++;this.setData((this.data?this.data:[]))}
,isc.A.setData=function isc_CellSelection_setData(_1){if(this.data!=null)this.ignoreData(this.data);this.data=_1;if(this.data!=null)this.observeData(this.data)}
,isc.A.observeData=function isc_CellSelection_observeData(_1){this.observe(_1,"dataChanged","observer.$vi = true")}
,isc.A.ignoreData=function isc_CellSelection_ignoreData(_1){this.ignore(_1,"dataChanged")}
,isc.A.cellIsSelected=function isc_CellSelection_cellIsSelected(_1,_2){var _3=this.data[_1],_4=(_3?_3[this.selectionProperty]:null),_5=(_4?_4[Math.floor(_2/ 32)]:null),_6=isc.CellSelection.COL_SELECTION_FLAGS[_2%32];return(_5!=null&&((_5&_6)!=0))}
,isc.A.rowHasSelection=function isc_CellSelection_rowHasSelection(_1){var _2=this.data[_1],_3=(_2?_2[this.selectionProperty]:null),_4=Math.ceil(this.numCols/ 32);if(!_2||!_2[this.selectionProperty])return false;for(var i=0;i<_4;i++){if(_3[i])return true}
return false}
,isc.A.colHasSelection=function isc_CellSelection_colHasSelection(_1){if(_1>this.numCols-1)return false;var _2=isc.CellSelection.COL_SELECTION_FLAGS[_1%32],_3=Math.floor(_1/ 32);var _4=this.data,_5=_4.length;for(var i=0;i<_5;i++){var _7=_4[i][this.selectionProperty];if(_7&&_7[_3]&&((_7[_3]&_2)!=0))
return true}
return false}
,isc.A.anySelected=function isc_CellSelection_anySelected(){var _1=Math.ceil(this.numCols/ 32);var _2=this.data,_3=_2.length;for(var i=0;i<_3;i++){var _5=_2[i][this.selectionProperty];if(!_5)continue;for(var j=0;j<_1;j++){if(_5[j])return true}}
return false}
,isc.A.getSelectedCells=function isc_CellSelection_getSelectedCells(){if(!this.$vi)return this.$62i;var _1=[],_2=isc.CellSelection.COL_SELECTION_FLAGS,_3=Math.ceil(this.numCols/ 32),_4=this.data,_5=_4.length,_6;for(var i=0;i<_5;i++){_6=_4[i][this.selectionProperty];if(!_6)continue;for(var j=0,_9,_10;j<_3;j++){_9=_6[j];if(!_9)continue;_10=(j==_3-1&&this.numCols%32!=0)?this.numCols%32:32;for(var k=0;k<_10;k++){if((_9&_2[k])!=0){_1[_1.length]=[i,j*32+k]}}}}
this.$62i=_1;this.$vi=false;return _1}
,isc.A.getSelectionRowNums=function isc_CellSelection_getSelectionRowNums(){var _1=[],_2=Math.ceil(this.numCols/ 32),_3=this.data,_4=_3.length,_5;for(var i=0;i<_4;i++){_5=_3[i][this.selectionProperty];if(!_5)continue;for(var j=0,_8;j<_2;j++){if(_5[j]){_1[_1.length]=i;break}}}
return _1}
,isc.A.getSelectionColNums=function isc_CellSelection_getSelectionColNums(){var _1=[],_2=[],_3=isc.CellSelection.COL_SELECTION_FLAGS,_4=Math.ceil(this.numCols/ 32),_5=this.data,_6=_5.length,_7;for(var i=0;i<_6;i++){_7=_5[i][this.selectionProperty];if(!_7)continue;for(var j=0,_10;j<_4;j++){if(_7[j]){_2[j]=_2[j]|_7[j]}}}
if(_2.length==0)return _1;for(var i=0,_11=this.numCols;i<_11;i++){if((_2[Math.floor(i/ 32)]&_3[i%32])!=0)
_1[_1.length]=i}
return _1}
,isc.A.getSelectionBounds=function isc_CellSelection_getSelectionBounds(){var _1=this.getSelectionRowNums(),_2=this.getSelectionColNums();return[_1.first(),_2.first(),_1.last(),_2.last()]}
,isc.A.$62j=function isc_CellSelection__setCellSelection(_1,_2,_3){var _4=this.data[_1],_5=(_4?_4[this.selectionProperty]:null),_6=Math.floor(_2/ 32),_7=(_5?_5[Math.floor(_2/ 32)]:0),_8=isc.CellSelection.COL_SELECTION_FLAGS[_2%32];if(!_4||_2>this.numCols-1)return false;if(_4.enabled==false)return false;if(_5==null){_5=_4[this.selectionProperty]=[];for(var i=0,_10=Math.ceil(this.numCols/ 32);i<_10;i++)_5[i]=0}
else if(_7==null){_5[_6]=0}
if(((_7&_8)!=0)==_3)return false;_5[_6]=_7^_8;if(_3)this.lastSelectedCell=[_1,_2];this.$vi=true;return true}
,isc.A.setCellRangeSelection=function isc_CellSelection_setCellRangeSelection(_1,_2,_3,_4,_5){this.changedCells=this.$62k(_1,_2,_3,_4,_5);return this.$62l()}
,isc.A.$62k=function isc_CellSelection__setCellRangeSelection(_1,_2,_3,_4,_5){var _6=[],_7,_8,_9,_10;if(_1<=_3){_7=_1;_8=_3}else{_7=_3;_8=_1}
if(_2<=_4){_9=_2;_10=_4}else{_9=_4;_10=_2}
if(this.logIsDebugEnabled()){this.logDebug((_5?"selecting ":"deselecting ")+[_7,_9]+" through "+[_8,_10])}
for(var _11=_7;_11<=_8;_11++){for(var _12=_9;_12<=_10;_12++){if(this.$62j(_11,_12,_5)){_6[_6.length]=[_11,_12]}}}
return _6}
,isc.A.setCellListSelection=function isc_CellSelection_setCellListSelection(_1,_2){if(!_1)return false;var _3=[];for(var i=0,_5=_1.length,_6,_7;i<_5;i++){_6=_1[i][0];_7=_1[i][1];if(this.$62j(_6,_7,_2))
_3[_3.length]=[_6,_7]}
this.changedCells=_3;return this.$62l()}
,isc.A.$62l=function isc_CellSelection__cellSelectionsChanged(){if(this.changedCells.length>0){this.selectionChanged();return true}else
return false}
,isc.A.selectionChanged=function isc_CellSelection_selectionChanged(){}
,isc.A.setCellSelection=function isc_CellSelection_setCellSelection(_1,_2,_3){if(this.$62j(_1,_2,_3)){this.changedCells=[[_1,_2]];this.selectionChanged();return true}else
return false}
,isc.A.selectCell=function isc_CellSelection_selectCell(_1,_2){return this.setCellSelection(_1,_2,true)}
,isc.A.deselectCell=function isc_CellSelection_deselectCell(_1,_2){return this.setCellSelection(_1,_2,false)}
,isc.A.selectCellRange=function isc_CellSelection_selectCellRange(_1,_2,_3,_4){this.changedCells=this.$62k(_1,_2,_3,_4,true);return this.$62l()}
,isc.A.deselectCellRange=function isc_CellSelection_deselectCellRange(_1,_2,_3,_4){this.changedCells=this.$62k(_1,_2,_3,_4,false);return this.$62l()}
,isc.A.selectRow=function isc_CellSelection_selectRow(_1){return this.selectCellRange(_1,0,_1,this.numCols-1)}
,isc.A.deselectRow=function isc_CellSelection_deselectRow(_1){return this.deselectCellRange(_1,0,_1,this.numCols-1)}
,isc.A.selectCol=function isc_CellSelection_selectCol(_1){return this.selectCellRange(0,_1,this.data.length-1,_1)}
,isc.A.deselectCol=function isc_CellSelection_deselectCol(_1){return this.deselectCellRange(0,_1,this.data.length-1,_1)}
,isc.A.selectAll=function isc_CellSelection_selectAll(){return this.selectCellRange(0,0,this.data.length-1,this.numCols-1)}
,isc.A.deselectAll=function isc_CellSelection_deselectAll(){return this.deselectCellRange(0,0,this.data.length-1,this.numCols-1)}
,isc.A.selectCellList=function isc_CellSelection_selectCellList(_1){return this.setCellListSelection(_1,true)}
,isc.A.deselectCellList=function isc_CellSelection_deselectCellList(_1){return this.setCellListSelection(_1,false)}
,isc.A.selectSingleCell=function isc_CellSelection_selectSingleCell(_1,_2){var _3=this.cellIsSelected(_1,_2);this.changedCells=this.$62k(0,0,this.data.length-1,this.numCols-1,false);this.$62j(_1,_2,true);if(!_3)
this.changedCells[this.changedCells.length]=[_1,_2];return this.$62l()}
,isc.A.selectSingleRow=function isc_CellSelection_selectSingleRow(_1){var _2=[];if(_1>0)
_2=this.$62k(0,0,_1-1,this.numCols-1,false);_2=_2.concat(this.$62k(_1,0,_1,this.numCols-1,true));if(_1<this.data.length-1)
_2=_2.concat(this.$62k(_1+1,0,this.data.length-1,this.numCols-1,false));this.changedCells=_2;return this.$62l()}
,isc.A.selectSingleCol=function isc_CellSelection_selectSingleCol(_1){var _2=[];if(_1>0)
_2=this.$62k(0,0,this.data.length-1,_1-1,false);_2=_2.concat(this.$62k(0,_1,this.data.length-1,_1,true));if(_1<this.numCols-1)
_2=_2.concat(this.$62k(0,_1+1,this.data.length-1,this.numCols-1,false));this.changedCells=_2;return this.$62l()}
,isc.A.selectOnMouseDown=function isc_CellSelection_selectOnMouseDown(_1,_2,_3){if(_1.selectionType==isc.Selection.NONE)return false;this.startRow=this.lastRow=_2;this.startCol=this.lastCol=_3;var _4=this.cellIsSelected(_2,_3),_5=this.getSelectedCells(),_6=this.getSelectionBounds();this.deselectCellOnMouseUp=false;this.deselectOthersOnMouseUp=false;if(_1.selectionType==isc.Selection.SINGLE){this.selectSingleCell(_2,_3);return true}else if(isc.EventHandler.shiftKeyDown()){if(_5.length==0){this.selectCell(_2,_3);return true}else{var _7=_6[0],_8=_6[1],_9=_6[2],_10=_6[3];if(_2<_7)
_7=_2;else if(_2>=_9)
_9=_2;else{this.deselectCellRange(_2+1,_8,_9,_10);_9=_2}
if(_3<_8)
_8=_3;else if(_3>=_10)
_10=_3;else{this.deselectCellRange(_7,_3+1,_9,_10);_10=_3}
this.selectCellRange(_7,_8,_9,_10);return true}}else if(_1.selectionType==isc.Selection.SIMPLE){if(!_4){this.selectCell(_2,_3);return true}else{this.deselectCellOnMouseUp=true;return false}}else if(isc.Browser.isMac?isc.EventHandler.metaKeyDown():isc.EventHandler.ctrlKeyDown()){this.setCellSelection(_2,_3,!_4);return true}else{if(!_4){this.selectSingleCell(_2,_3);return true}else if(isc.EventHandler.rightButtonDown()){this.deselectOnDragMove=true;return false}else{if(this.dragSelection){if(this.simpleDeselect){this.deselectAll();this.selectOriginOnDragMove=true;return true}
this.selectSingleCell(_2,_3);return true}else{if(this.simpleDeselect){this.deselectAllOnMouseUp=true}else{this.deselectOthersOnMouseUp=(_5.length>1)}
return false}}}}
,isc.A.selectOnDragMove=function isc_CellSelection_selectOnDragMove(_1,_2,_3){var _4=this.startRow,_5=this.startCol,_6=this.lastRow,_7=this.lastCol;if(_2<0||_3<0){this.logWarn("selectOnDragMove: aborting due to negative coordinate: "+[_2,_3]);return}
if(_2==_6&&_3==_7)return;if(_1.selectionType==isc.Selection.SINGLE){this.selectSingleCell(_2,_3);return}
var _8=[];if(this.selectOriginOnDragMove){this.$62j(_4,_5);_8.add([_4,_5]);this.selectOriginOnDragMove=false}else if(this.deselectOnDragMove||this.deselectAllOnMouseUp||this.deselectOthersOnMouseUp)
{this.selectSingleCell(_4,_5);this.deselectAllOnMouseUp=this.deselectOthersOnMouseUp=this.deselectOnDragMove=false}
if((_2!=_6&&((_6>=_4&&_4>=_2)||(_2>=_4&&_4>=_6)))||(_3!=_7&&((_7>=_5&&_5>=_3)||(_3>=_5&&_5>=_7))))
{this.$62j(_4,_5,false);_8.addList(this.$62k(_4,_5,_6,_7,false));this.$62j(_4,_5,true);_8.addList(this.$62k(_4,_5,_2,_3,true));this.changedCells=_8;this.$62l();this.lastRow=_2;this.lastCol=_3;return}
if(_2>=0&&_2!=_6){if(_4>=_6&&_6>_2){_8.addList(this.$62k(_2,_5,_6-1,_7,true))}else if(_4>=_2&&_2>_6){_8.addList(this.$62k(_6,_5,_2-1,_7,false))}else if(_4<=_2&&_2<_6){_8.addList(this.$62k(_2+1,_5,_6,_7,false))}else if(_4<=_6&&_6<_2){_8.addList(this.$62k(_6+1,_5,_2,_7,true))}
_6=this.lastRow=_2}
if(_3>=0&&_3!=_7){if(_5>=_7&&_7>_3){_8.addList(this.$62k(_4,_3,_6,_7-1,true))}else if(_5>=_3&&_3>_7){_8.addList(this.$62k(_4,_7,_6,_3-1,false))}else if(_5<=_3&&_3<_7){_8.addList(this.$62k(_4,_3+1,_6,_7,false))}else if(_5<=_7&&_7<_3){_8.addList(this.$62k(_4,_7+1,_6,_3,true))}
this.lastCol=_3}
this.changedCells=_8;this.$62l()}
,isc.A.selectOnMouseUp=function isc_CellSelection_selectOnMouseUp(_1,_2,_3){if(_1.selectionType==isc.Selection.NONE)return false;if(this.deselectOthersOnMouseUp){this.selectSingleCell(_2,_3);this.deselectOthersOnMouseUp=false;return true}else if(this.deselectRecordOnMouseUp){this.deselectCell(_2,_3);this.deselectRecordOnMouseUp=false;return true}else if(this.deselectAllOnMouseUp){this.deselectAll();this.deselectAllOnMouseUp=false}else
return false}
);isc.B._maxIndex=isc.C+37;if(isc.Window){isc.ClassFactory.defineClass("FieldEditor","Window");isc.A=isc.FieldEditor.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.isModal=true;isc.A.showMinimizeButton=false;isc.A.autoCenter=true;isc.A.autoSize=true;isc.A.defaultWidth=475;isc.A.visibleFieldsConstructor="ListGrid";isc.A.hiddenFieldsConstructor="ListGrid";isc.A.showFooter=false;isc.A.title="Customize Fields";isc.A.showInstructionsPane=true;isc.A.bodyProperties={layoutMargin:5};isc.A.instructionsPaneDefaults={_constructor:isc.HTMLFlow,padding:5,height:1};isc.A.instructions="Drag fields between grids to control which fields are visible "+"and the order in which fields are displayed";isc.B.push(isc.A.initWidget=function isc_FieldEditor_initWidget(){this.invokeSuper(isc.FieldEditor,"initWidget");if(!this.fields){isc.logWarn('FieldEditor can not be created because no fields were provided');return}
this.addItem(this.addAutoChild("instructionsPane",{contents:this.instructions}));this.visibleFieldsDefaults=this.hiddenFieldsDefaults={height:200,width:200,leaveScrollbarGap:false,canDragRecordsOut:true,canAcceptDroppedRecords:true,canReorderRecords:true,dragDataAction:"move"};var _1=this.visibleFieldsGrid=this.createAutoChild("visibleFields",{fields:[{name:"title",title:"Visible Fields",formatCellValue:"value || record.name"}]});var _2=this.fields;var _3=_2.findAll("visible",null);var _4=_2.findAll("visible",false);_1.setData(_3);var _5=this.hiddenFieldsGrid=this.createAutoChild("hiddenFields",{canReorderRecords:false,fields:[{name:"title",title:"Hidden Fields",formatCellValue:"value || record.name"}]});_5.setData(_4);var _6=isc.HLayout.create({membersMargin:10,layoutMargin:5,height:1,overflow:"visible",members:[_1,isc.VStack.create({width:32,height:74,layoutAlign:"center",membersMargin:10,members:[isc.Img.create({src:"[SKINIMG]actions/back.png",width:16,height:16,visFieldsGrid:_1,hidFieldsGrid:_5,layoutAlign:"center",click:"this.visFieldsGrid.transferSelectedData(this.hidFieldsGrid)"}),isc.Img.create({src:"[SKINIMG]actions/forward.png",width:16,height:16,layoutAlign:"center",visFieldsGrid:_1,hidFieldsGrid:_5,click:"this.hidFieldsGrid.transferSelectedData(this.visFieldsGrid)"})]}),_5]});this.addItem(_6);var _7=this.createAutoChild("okButton",{autoDraw:false,title:"Done",fieldEditor:this,click:function(){this.creator.okClick()},layoutAlign:"center"},isc.IButton);this.addItem(_7)}
,isc.A.okClick=function isc_FieldEditor_okClick(){var _1=isc.clone(this.visibleFieldsGrid.data);var _2=isc.clone(this.hiddenFieldsGrid.data);_1.setProperty("visible",null);_2.setProperty("visible",false);_1.addList(_2);var _3=_1.getProperties(["name","visible"]);this.done(_1,_3);this.hide();this.destroy()}
,isc.A.done=function isc_FieldEditor_done(_1,_2){}
);isc.B._maxIndex=isc.C+3}
isc.ClassFactory.defineClass("FormulaBuilder","VLayout");isc.A=isc.FormulaBuilder.getPrototype();isc.A.vertical=true;isc.A.padding=10;isc.A.showFormulaField=true;isc.A.formulaFieldDefaults={type:"text",formItemType:"AutoFitTextAreaItem",height:20,width:"*",hoverWidth:300,keyPress:function(){if(this.form.creator.autoTest){this.fireOnPause("autoTest",{target:this.form.creator,methodName:"testFunction"},this.form.creator.autoTestDelay)}}};isc.A.showTitleField=true;isc.A.titleFieldDefaults={type:"text",width:"*"};isc.A.showHelpIcon=true;isc.A.helpIconDefaults={src:"[SKIN]actions/help.png"};isc.A.autoHideCheckBoxLabel="Auto hide fields used in formula";isc.A.showAutoHideCheckBox=true;isc.A.autoHideCheckBoxDefaults={type:"boolean"};isc.A.builderTypeText="Formula";isc.A.helpTextIntro="For basic arithmetic, type in symbols (+-/%) directly.<P>The following functions are also available:";isc.A.fieldKeyDefaults={_constructor:"ListGrid",leaveScrollbarGap:false,showResizeBar:true,autoFitData:"vertical",autoFitMaxRecords:6,autoFetchData:true,showRollOver:false,selectionType:"none",defaultFields:[{name:"mappingKey",width:40},{name:"title"},{name:"sourceDS",showIf:"list.creator.dataSources != null"},{name:"name",showIf:"false"},{name:"type",showIf:"false"},{name:"length",showIf:"false"}],recordClick:function(_1,_2){var _3=this.creator.formulaField;if(_3){var _4=_3.getEnteredValue()||"";var _5=_3.getSelectionRange();if(_5!=null){_4=_4.substring(0,_5[0])+_2.mappingKey+_4.substring(_5[1])}else{_4+=_2.mappingKey}
_3.setValue(_4);_3.focusInItem();if(this.creator.autoTest){this.fireOnPause("autoTest",{target:this.creator,methodName:"testFunction"},this.creator.autoTestDelay)}}}};isc.A.instructionsTextStart="The following fields are available for use in this \${builderType}";isc.A.instructionsDefaults={_constructor:"Label",height:1,extraSpace:10,overflow:"visible"};isc.A.titleFormDefaults={_constructor:"DynamicForm",extraSpace:5};isc.A.formulaFormDefaults={_constructor:"DynamicForm",extraSpace:5};isc.A.hideFieldsFormDefaults={_constructor:"DynamicForm",extraSpace:20};isc.A.sampleHeaderDefaults={_constructor:"Label",height:15,extraSpace:5};isc.A.sampleLabelDefaults={_constructor:"Canvas",height:40,width:"100%",align:"center",valign:"top",extraSpace:10,showHover:true,overflow:"hidden",styleName:"sampleOutput"};isc.A.messageLabelDefaults={_constructor:"Label",height:20,width:"100%",align:"right",valign:"center",overflow:"hidden",showHover:true};isc.A.buttonLayoutDefaults={_constructor:"HLayout",width:"100%",align:"right"};isc.A.cancelButtonDefaults={_constructor:"IButton",autoParent:"buttonLayout",autoFit:true,extraSpace:10,click:function(){this.creator.completeEditing(true)}};isc.A.testButtonDefaults={_constructor:"IButton",autoParent:"buttonLayout",autoFit:true,extraSpace:10,click:function(){this.creator.testFunction()}};isc.A.saveAddAnotherButtonDefaults={_constructor:"IButton",autoParent:"buttonLayout",autoFit:true,extraSpace:10,click:function(){if(!this.creator.showTitleForm||this.creator.titleForm.validate())this.creator.saveAddAnother()}};isc.A.saveButtonDefaults={_constructor:"IButton",autoParent:"buttonLayout",autoFit:true,click:function(){if(!this.creator.showTitleForm||this.creator.titleForm.validate())this.creator.save()}};isc.A.fieldType="float";isc.A.allowEscapedKeys=false;isc.A.invalidBuilderPrompt="Invalid \${builderType}: \${errorText}";isc.A.defaultErrorText="[No Explicit Error]";isc.A.invalidBlankPrompt="Invalid blank \${builderType}";isc.A.validBuilderPrompt="Valid \${builderType}";isc.A.helpWindowTitle="\${builderType} Help";isc.A.titleFieldTitle="Title";isc.A.defaultNewFieldTitle="New Field";isc.A.keyColumnTitle="Key";isc.A.sourceFieldColumnTitle="Source Field";isc.A.sourceDSColumnTitle="Source DataSource";isc.A.cancelButtonTitle="Cancel";isc.A.saveAddAnotherButtonTitle="Save & Add Another";isc.A.saveButtonTitle="Save";isc.A.saveConfirmationPrompt="Save changes to this \${builderType}?";isc.A.invalidGeneratedFunctionPrompt="The generated function is invalid - Check your \${builderType} and retry.";isc.A.sampleHeaderTitle="Sample:";isc.A.testButtonTitle="Test";isc.A=isc.FormulaBuilder.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.warnDuplicateTitlesMessage="Another field already has the title '${fieldTitle}'.  Continue anyway?";isc.A.autoTest=true;isc.A.autoTestDelay=200;isc.A.samplePrompt="<nobr>For Record: ${title}</nobr><br><nobr>Output: ${output}</nobr>";isc.B.push(isc.A.getValue=function isc_FormulaBuilder_getValue(){return this.formulaField?this.formulaField.getValue():null}
,isc.A.setValue=function isc_FormulaBuilder_setValue(_1){if(this.formulaField){this.formulaField.setValue(_1)}}
,isc.A.setFormula=function isc_FormulaBuilder_setFormula(_1){this.setValue(_1)}
,isc.A.getFieldIdProperty=function isc_FormulaBuilder_getFieldIdProperty(){return this.getClass().getFieldIdProperty(this.component)}
,isc.A.getTitle=function isc_FormulaBuilder_getTitle(){return this.titleField?this.titleField.getValue():null}
,isc.A.setTitle=function isc_FormulaBuilder_setTitle(_1){if(this.titleField){this.titleField.setValue(_1)}}
,isc.A.getFieldFromMappingKey=function isc_FormulaBuilder_getFieldFromMappingKey(_1){var _2=this.getAvailableFields();for(var i=0;i<_2.length;i++){var _4=_2.get(i);if(_4.mappingKey==_1)return _4}
return null}
,isc.A.getFields=function isc_FormulaBuilder_getFields(){if(this.fields)return this.fields;if(this.component)return this.component.getAllFields();var _1;if(this.dataSources){_1=[];for(var i=0;i<this.dataSources.length;i++){var _3=this.dataSources[i],_4=_3.getFields();for(var _5 in _4){var _6=isc.addProperties({},_4[_5],{name:_3.getID()+"."+_5,sourceDS:_3.getID()});_1.add(_6)}}}else{_1=isc.getValues(this.dataSource.getFields())}
return _1}
,isc.A.shouldHideUsedFields=function isc_FormulaBuilder_shouldHideUsedFields(){if(this.showAutoHideCheckBox&&this.autoHideCheckBox&&this.autoHideCheckBox.getValue()){return this.autoHideCheckBox.getValue()}else return false}
,isc.A.getHelpText=function isc_FormulaBuilder_getHelpText(){return this.getHoverText()}
,isc.A.initWidget=function isc_FormulaBuilder_initWidget(){this.Super("initWidget",arguments);if(this.dataSource)this.dataSource=isc.DataSource.get(this.dataSource);if(this.dataSources){var _1=[];for(var i=0;i<this.dataSources.length;i++){_1[i]=isc.DataSource.get(this.dataSources[i])}}
var _3=this.getAvailableFields();if(!this.field){this.field={name:this.getUniqueFieldName(),title:this.defaultNewFieldTitle,type:this.fieldType,width:"50",canFilter:false,canSortClientOnly:true,originalOrder:this.availableFields.length}}
this.instructions=this.createAutoChild("instructions",{contents:this.instructionsTextStart.evalDynamicString(this,{builderType:this.builderTypeText})});this.addMember(this.instructions);this.fieldKeyDS=isc.DataSource.create({ID:this.getID()+"DS",clientOnly:true,testData:_3,fields:[{name:"mappingKey",title:this.keyColumnTitle,width:40},{name:"title",title:this.sourceFieldColumnTitle},{name:"sourceDS",title:this.sourceDSColumnTitle,showIf:"false"},{name:"name",showIf:"false",primaryKey:true},{name:"type",showIf:"false"},{name:"length",showIf:"false"}]});this.fieldKey=this.createAutoChild("fieldKey",{dataSource:this.fieldKeyDS});if(this.fieldKey.showFilterEditor!==false&&this.fieldKey.autoFitMaxRecords&&_3.length>this.fieldKey.autoFitMaxRecords)
{this.fieldKey.setShowFilterEditor(true)}
this.addMember(this.fieldKey);if(this.showTitleField){this.addAutoChild("titleForm",{fields:[isc.addProperties(this.titleFieldDefaults,this.titleFieldProperties,{title:this.titleFieldTitle,name:"titleField"})]});this.titleField=this.titleForm.getField("titleField");this.setTitle(this.field.title||isc.DataSource.getAutoTitle(this.field.name))}
if(this.showFormulaField){this.addAutoChild("formulaForm",{fields:[isc.addProperties({title:this.builderTypeText},this.formulaFieldDefaults,this.formulaFieldProperties,this.showHelpIcon?{icons:[isc.addProperties({prompt:this.getHelpText()},this.helpIconDefaults,this.helpIconProperties,{click:"form.creator.showHelpWindow();"})]}:{},{name:"formulaField"})]});this.formulaField=this.formulaForm.getField("formulaField");if(this.showHelpIcon)this.helpIcon=this.formulaField.icons[0]}
this.addAutoChild("messageLabel");this.addAutoChild("sampleHeader",{contents:this.sampleHeaderTitle});this.addAutoChild("sampleLabel");if(this.showAutoHideCheckBox){this.addAutoChild("hideFieldsForm",{fields:[isc.addProperties({title:this.autoHideCheckBoxLabel},this.autoHideCheckBoxDefaults,this.autoHideCheckBoxProperties,{name:"autoHide"})]});this.autoHideCheckBox=this.hideFieldsForm.getField("autoHide")}
this.addAutoChild("buttonLayout");this.addAutoChild("cancelButton",{title:this.cancelButtonTitle});if(!this.autoTest)this.addAutoChild("testButton",{title:this.testButtonTitle});this.addAutoChild("saveAddAnotherButton",{title:this.saveAddAnotherButtonTitle});this.addAutoChild("saveButton",{title:this.saveButtonTitle});if(this.showTitleField)this.titleForm.focusInItem(this.titleField);else this.formulaForm.focusInItem(this.formulaField);this.setInitialValue();if(this.editMode&&this.autoTest)this.testFunction()}
,isc.A.getUniqueFieldName=function isc_FormulaBuilder_getUniqueFieldName(){return this.getNewUniqueFieldName("formulaField")}
,isc.A.getNewUniqueFieldName=function isc_FormulaBuilder_getNewUniqueFieldName(_1){if(!_1||_1=="")_1="field";var _2=this.getFields(),_3=1,_4=_1.length;for(var i=0;i<_2.length;i++){var _6=_2.get(i);if(_6.name.startsWith(_1)){var _7=_6.name.substr(_4),_8=new Number(_7);if(_8&&_8>=_3)_3=_8+1}}
return _1+_3}
,isc.A.destroy=function isc_FormulaBuilder_destroy(){if(this.fieldKeyDS)this.fieldKeyDS.destroy();this.Super("destroy",arguments)}
,isc.A.setInitialValue=function isc_FormulaBuilder_setInitialValue(){if(this.editMode&&this.field.userFormula){this.initialValue=this.field.userFormula.text;if(this.field.userFormula.allowEscapedKeys)
this.allowEscapedKeys=this.field.userFormula.allowEscapedKeys}
this.initialValue=this.initialValue||"";this.setValue(this.initialValue)}
,isc.A.showHelpWindow=function isc_FormulaBuilder_showHelpWindow(){var _1=this.locatorParent,_2=_1?_1.getTop():this.top,_3=_1?_1.getLeft():this.left,_4=_1?_1.getWidth():this.width,_5=_1?_1.getVisibleHeight():this.getVisibleHeight();if(_1)_1.centerInPage();if(this.helpWindow&&this.helpWindow!=null){this.hideHelpWindow()}else{this.helpIcon.prompt=null;this.formulaField.stopHover();_3-=(_4/ 2);if(_1)_1.setLeft(_3);this.helpWindow=isc.Window.create({autoDraw:true,title:this.helpWindowTitle.evalDynamicString(this,{builderType:this.builderTypeText}),showMinimizeButton:false,showMaximizeButton:false,showCloseButton:false,isModal:false,headerIconProperties:{src:"[SKIN]actions/help.png"},items:[isc.Label.create({contents:this.getHelpText(),padding:10})]});this.helpWindow.moveTo(_3+_4,_2);this.helpWindow.resizeTo(_4,_5)}}
,isc.A.hideHelpWindow=function isc_FormulaBuilder_hideHelpWindow(){if(this.helpWindow){this.helpWindow.destroy();this.helpWindow=null}
this.helpIcon.prompt=this.getHelpText();this.formulaField.stopHover()}
,isc.A.getHoverText=function isc_FormulaBuilder_getHoverText(){var _1=isc.SB.create();_1.append("<b>",this.helpTextIntro,"</b> <P>");_1.append("<ul>");var _2=isc.MathFunction.getRegisteredFunctionIndex(),_3=this.mathFunctions;if(_3&&_3.length>0){for(var i=0;i<_3.length;i++){var _5=_2[_3[i]];_1.append("<li> <b>",_5.name,": </b> ",_5.description,"<p>");_1.append("<i>usage: ",_5.usage,"</i> </li>")}}
_1.append("</ul>");return _1.toString()}
,isc.A.getAvailableFields=function isc_FormulaBuilder_getAvailableFields(){if(!this.availableFields){this.availableFields=this.getClass().getAvailableFields(this.getFields(),this.field)}
return this.availableFields}
,isc.A.getUsedFields=function isc_FormulaBuilder_getUsedFields(){return this.getClass().getUsedFields(this.getValue(),this.getAvailableFields(),this.field)}
,isc.A.getCompleteValueObject=function isc_FormulaBuilder_getCompleteValueObject(){var _1=this.getUsedFields(),_2=this.generateFunction(),_3={sortNormalizer:_2,$4s:_2,type:this.fieldType,userFormula:{text:this.getValue(),formulaVars:{}}},_4=this.getFieldIdProperty();if(this.allowEscapedKeys)_3.userFormula.allowEscapedKeys=true;for(var i=0;i<_1.length;i++){var _6=_1.get(i);_3.userFormula.formulaVars[_6.mappingKey]=_6[_4]}
return _3}
,isc.A.getBasicValueObject=function isc_FormulaBuilder_getBasicValueObject(){var _1=this.getUsedFields(),_2={text:this.getValue(),formulaVars:{}},_3=this.getFieldIdProperty();if(this.allowEscapedKeys)_2.allowEscapedKeys=true;for(var i=0;i<_1.length;i++){var _5=_1.get(i);_2.formulaVars[_5.mappingKey]=_5[_3]}
return _2}
,isc.A.getUpdatedFieldObject=function isc_FormulaBuilder_getUpdatedFieldObject(){return isc.addProperties(this.field,{title:this.getTitle()},this.getCompleteValueObject())}
,isc.A.testFunction=function isc_FormulaBuilder_testFunction(){var _1=this.getClass().testFunction(this.field,this.getBasicValueObject(),this.component,this.getFields());var _2="",_3=_1.errorText||this.defaultErrorText;if(_1.failedGeneration||_1.failedExecution){_2=this.invalidBuilderPrompt.evalDynamicString(this,{builderType:this.builderTypeText,errorText:_3})}else if(_1.emptyTestValue){_2=this.invalidBlankPrompt.evalDynamicString(this,{builderType:this.builderTypeText})}else{_2=this.validBuilderPrompt.evalDynamicString(this,{builderType:this.builderTypeText})}
this.setTestMessage(_2);this.setSamplePrompt(this.getSamplePrompt(_1));return _1}
,isc.A.getTestRecord=function isc_FormulaBuilder_getTestRecord(){if(this.testRecord)return this.testRecord;return this.getClass().getTestRecord(this.component,this.getAvailableFields())}
,isc.A.setTestMessage=function isc_FormulaBuilder_setTestMessage(_1){this.messageLabel.setContents(_1)}
,isc.A.setSamplePrompt=function isc_FormulaBuilder_setSamplePrompt(_1){this.sampleLabel.setContents("<center>"+_1+"</center>")}
,isc.A.generateFunction=function isc_FormulaBuilder_generateFunction(){return this.getClass().generateFunction(this.getBasicValueObject(),this.getUsedFields(),this.component)}
,isc.A.saveAddAnother=function isc_FormulaBuilder_saveAddAnother(){this.restartBuilder=true;this.save()}
,isc.A.fieldTitleIsUnique=function isc_FormulaBuilder_fieldTitleIsUnique(_1){var _2=this.component?this.component.getAllFields():null,_3=_2?_2.findAll({"title":_1}):null,_4=true;if(_3&&_3.length>0){for(var i=0;i<_3.length;i++){if(_3[i].name!=this.field.name){_4=false;break}}}
return _4}
,isc.A.save=function isc_FormulaBuilder_save(){var _1=this.testFunction();if(this.warnDuplicateTitles&&!this.duplicateTitleAccepted){var _2=this.getTitle();if(!this.fieldTitleIsUnique(_2)){var _3=this.warnDuplicateTitlesMessage.evalDynamicString(this,{fieldTitle:_2})
var _4=this;isc.confirm(_3,function(_5){if(_5){_4.duplicateTitleAccepted=true;_4.delayCall("save")}else{_4.restartBuilder=false}});return null}}
delete this.duplicateTitleAccepted;if(_1.emptyTestValue){isc.warn(this.invalidBlankPrompt.evalDynamicString(this,{builderType:this.builderTypeText}));return}else if(_1.failedGeneration||_1.failedExecution){isc.warn(this.invalidGeneratedFunctionPrompt.evalDynamicString(this,{builderType:this.builderTypeText}));return}
this.completeEditing(false)}
,isc.A.completeEditing=function isc_FormulaBuilder_completeEditing(_1,_2){this.cancelled=_1;if(_1){if(this.editMode&&!_2){if(this.getValue()!=this.initialValue){var _3=this;var _4=this.saveConfirmationPrompt.evalDynamicForm(this,{builderType:this.builderTypeText});isc.confirm(_4,function(_5){if(_5){_3.save()}else{_3.completeEditing(true,true)}});return}}}
if(this.helpWindow)this.hideHelpWindow();if(this.availableFields){this.availableFields=this.availableFields.sortByProperty("originalOrder",true);this.availableFields.clearProperty("originalOrder")}
this.fireOnClose()}
,isc.A.fireOnClose=function isc_FormulaBuilder_fireOnClose(){}
,isc.A.getSamplePrompt=function isc_FormulaBuilder_getSamplePrompt(_1){var _2=this.dataSource?this.dataSource.getTitleField():isc.firstKey(_1.record),_3=_1.result!=null?_1.result:this.invalidBuilderPrompt.evalDynamicString(this,{builderType:this.builderTypeText,errorText:_1.errorText||this.defaultErrorText}),_4=_1.record[_2];return this.samplePrompt.evalDynamicString(this,{title:_4,output:_3})}
);isc.B._maxIndex=isc.C+34;isc.A=isc.FormulaBuilder;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.mappingKeyForIndex=function isc_c_FormulaBuilder_mappingKeyForIndex(_1){var _2="",_3=Math.floor(_1/(26*27)),_4=_1%(26*26),_5=Math.floor(_4/ 26);if(_3>=1)_2+=String.fromCharCode(65+(_3-1));if(_5>=1){if(_3>=1){_2+=String.fromCharCode(65+(_5-1));_2+=String.fromCharCode(65+(_1-(26*27))%26)}else{_2+=String.fromCharCode(65+(_5-1));_2+=String.fromCharCode(65+_1%26)}}else{if(_3>=1){_2+=String.fromCharCode(65)}
_2+=String.fromCharCode(65+_1%26)}
return _2}
,isc.A.getFieldIdProperty=function isc_c_FormulaBuilder_getFieldIdProperty(_1){return _1?_1.fieldIdProperty:"name"}
,isc.A.getAvailableFields=function isc_c_FormulaBuilder_getAvailableFields(_1,_2){var _3=[],j=0;if(!_1)return _3;for(var i=0;i<_1.getLength();i++){var _6=_1.get(i),_7=_6.type;_6.originalOrder=i;if(_2&&_2.name==_6.name)continue;if(_6.userFormula||isc.SimpleType.inheritsFrom(_7,"integer")||isc.SimpleType.inheritsFrom(_7,"float"))
{_6.mappingKey=isc.FormulaBuilder.mappingKeyForIndex(j++);if(!_6.title)_6.title=isc.DataSource.getAutoTitle(_6.name);_3.add(_6)}}
var _8=_2&&_2.userFormula?_2.userFormula.formulaVars:{};for(var _9 in _8){var _10=_3.find("mappingKey",_9),_11=_3.find("name",_8[_9]),_12=_11.mappingKey;_11.mappingKey=_10.mappingKey;_10.mappingKey=_12}
_3=_3.sortByProperties(["mappingKey"],[true],[function(_6,_14,_15){var _13=_6[_14];if(_13.length==1)_13='  '+_13;else if(_13.length==2)_13=' '+_13;return _13}]);return _3}
,isc.A.getUsedFields=function isc_c_FormulaBuilder_getUsedFields(_1,_2,_3){var _4=this.getAvailableFields(_2,_3),_5=[];if(!_4||!_1)return _5;_4=_4.sortByProperties(["mappingKey"],[false],[function(_8,_9,_10){var _6=_8[_9];if(_6.length==1)_6='  '+_6;else if(_6.length==2)_6=' '+_6;return _6}]);for(var i=0;i<_4.length;i++){var _8=_4.get(i);if(this.allowEscapedKeys){if(_1.indexOf("#"+_8.mappingKey)>=0||_1.indexOf("#{"+_8.mappingKey+"}")>=0)
{_5.add(_8)}}else if(_1.indexOf(_8.mappingKey)>=0){_5.add(_8)}}
return _5}
,isc.A.getFieldDetailsFromValue=function isc_c_FormulaBuilder_getFieldDetailsFromValue(_1,_2,_3){var _4=_1,_5=this.getFieldIdProperty(_3),_6={usedFields:[],missingFields:[]};for(var _7 in _4){var _8=_4[_7],_9=_2.findIndex(_5,_8);if(!_2[_9]){isc.logWarn("Field "+_8+" is not in the list of available-fields");_6.missingFields.add(_8)}else{var _10=isc.addProperties({},_2[_9]);_10.mappingKey=_7;_6.usedFields.add(_10)}}
return _6}
,isc.A.testFunction=function isc_c_FormulaBuilder_testFunction(_1,_2,_3,_4){var _5={};try{_5.component=_3;_5.record=this.getTestRecord(_3,_4);if(_2.text==""){_5.emptyTestValue=true;return _5}
_5.jsFunction=this.generateFunction(_2,_4,_3);_5.result=_5.jsFunction(_5.record,_3)}catch(err){if(!_5.jsFunction)_5.failedGeneration=true;_5.failedExecution=true;_5.errorText=err.message}
return _5}
,isc.A.getTestRecord=function isc_c_FormulaBuilder_getTestRecord(_1,_2){var _3=this.getFieldIdProperty(_1),_4;if(_1){_4=_1.getSelectedRecord();if(!_4){if(_1.body){var _5=_1.body.getVisibleRows();_4=_5?_1.getRecord(_5[0]):_1.data.get(0)}else{_4=_1.data.get(0)}}}
if(!_4&&_2){_4={};for(var i=0;i<_2.length;i++){var _7=_2.get(i);if(_7.userFormula){_7.$4s=_7.sortNormalizer=isc.FormulaBuilder.generateFunction(_7.userFormula,_2,_1)}
if(_7.$4s){isc.DataSource.setPathValue(_4,_7[_3],_7.$4s(_4,_1))}else if(_7.type)
if(isc.SimpleType.inheritsFrom(_7.type,"integer")||isc.SimpleType.inheritsFrom(_7.type,"float"))
{isc.DataSource.setPathValue(_4,_7[_3],1)}else{isc.DataSource.setPathValue(_4,_7[_3],_7[_3])}
else{isc.DataSource.setPathValue(_4,_7[_3],_7[_3])}}}
return _4}
,isc.A.generateFunction=function isc_c_FormulaBuilder_generateFunction(_1,_2,_3,_4){var _5=isc.SB.create(),_6=_1.text,_7=this.getFieldIdProperty(_3),_8=this.getFieldDetailsFromValue(_1.formulaVars,_2,_3),_9=_8.usedFields,_10=_8.missingFields;_9=_9.sortByProperties(["mappingKey"],[false],[function(_13,_18,_19){var _11=_13[_18];if(_11.length==1)_11='  '+_11;else if(_11.length==2)_11=' '+_11;return _11}]);if(_10.length==0){if(_9.length>0){for(var i=0;i<_9.length;i++){var _13=_9.get(i);var _14=_13[_7];_5.append("var ");_5.append(_13.mappingKey,"=isc.DataSource.getPathValue(record,'",_14,"');","\nif(",_13.mappingKey,"==null)",_13.mappingKey,"= (component ? component.getStandaloneFieldValue(record, '",_14,"', true) : "+(_4?"null)":"0)"));_5.append(";\n");if(_1.allowEscapedKeys){_6=_6.replaceAll("#"+_13.mappingKey,_13.mappingKey);_6=_6.replaceAll("#{"+_13.mappingKey+"}",_13.mappingKey)}}
_5.append("\n")}
var _15=isc.MathFunction.getRegisteredFunctions();if(_15&&_15.length>0){_5.append("var functions=isc.MathFunction.getRegisteredFunctionIndex(),\n");for(var i=0;i<_15.length;i++){var _13=_15.get(i);_5.append("        ");_5.append(_13.name,"=","functions.",_13.name,".jsFunction");_5.append(i==_15.length-1?";":",","\n")}
_5.append("\n")}
_5.append("var value=",_6,";",(_4?null:"if (isNaN(value)) return (component && component.badFormulaResultValue) || '.'; "),"return value;")}else{this.logWarn("Formula failed due to missing fields: "+_10.join(", ")+".");var _11=(_3&&_3.badFormulaResultValue)||".";if(_11)_11="'"+_11+"'";_5.append("return ",_11,";")}
var _16=_5.toString();var _17=new Function("record,component",_16);return _17}
);isc.B._maxIndex=isc.C+8;isc.ClassFactory.defineClass("SummaryBuilder","FormulaBuilder");isc.A=isc.SummaryBuilder.getPrototype();isc.A.builderTypeText="Summary";isc.A.fieldType="text";isc.A.autoHideCheckBoxLabel="Auto hide fields used in Summary";isc.A.helpTextIntro="Building Summary Columns";isc.A.allowBasicMultiCharKeys=false;isc.A=isc.SummaryBuilder.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.setSummary=function isc_SummaryBuilder_setSummary(_1){this.setValue(_1)}
,isc.A.setInitialValue=function isc_SummaryBuilder_setInitialValue(){if(this.editMode&&this.field.userSummary){this.initialValue=this.field.userSummary.text}
this.initialValue=this.initialValue||"";this.setValue(this.initialValue)}
,isc.A.getUniqueFieldName=function isc_SummaryBuilder_getUniqueFieldName(){return this.getNewUniqueFieldName("summaryField")}
,isc.A.getHoverText=function isc_SummaryBuilder_getHoverText(){var _1=isc.SB.create(),_2=this.getTestRecord(),_3=this.getFieldIdProperty(),_4=this.getFieldFromMappingKey("A"),_5=_4[_3],_6=_4?_4.title||_4.name:null,_7=this.getFieldFromMappingKey("B"),_8=_7?_7[_3]:null,_9=_7?_7.title||_7.name:null;_1.append("<b>",this.helpTextIntro,"</b> <P>");_1.append("Summary columns are user-created fields that combine dynamic-values "+"from other fields in the current record with static text specified by the user.<P>");_1.append("Dynamic-values are specified by prefixing a mapping-key from the table "+"opposite with #");if(this.getFields().length>26)_1.append(", or by using #{key} when the key "+"is 2 or more characters long,");_1.append(" and everything else is copied directly into the output.<P>");if(this.dataSource){_1.append("For example, in the current DataSource, key <b>A</b> maps to field <i>",_6,"</i> and <b>B</b> is <i>",!_7?"missing":_9,"</i>.<P>");_1.append("So, if we enter the Summary format-string as:<P>","<i>#A is relative to #B</i><P>","then example output using the current data would look like:<P>");if(_2){_1.append("<i>",_2[_5]," is relative to ",!_7?"{missing}":_2[_8],"</i><P>")}}
return _1.toString()}
,isc.A.getAvailableFields=function isc_SummaryBuilder_getAvailableFields(){if(!this.availableFields){this.availableFields=this.getClass().getAvailableFields(this.getFields(),this.field)}
return this.availableFields}
,isc.A.getUsedFields=function isc_SummaryBuilder_getUsedFields(){return this.getClass().getUsedFields(this.getValue(),this.getAvailableFields(),this.allowBasicMultiCharKeys,this.field)}
,isc.A.getCompleteValueObject=function isc_SummaryBuilder_getCompleteValueObject(){var _1=this.getUsedFields(),_2=this.generateFunction(),_3=this.getFieldIdProperty(),_4={sortNormalizer:_2,$39:_2,type:this.fieldType,userSummary:{text:this.getValue()}};if(_1&&_1.length>0){_4.userSummary.summaryVars={};for(var i=0;i<_1.length;i++){var _6=_1.get(i);_4.userSummary.summaryVars[_6.mappingKey]=_6[_3]}}
return _4}
,isc.A.getBasicValueObject=function isc_SummaryBuilder_getBasicValueObject(){var _1=this.getUsedFields(),_2=this.getFieldIdProperty(),_3={text:this.getValue(),summaryVars:{}};for(var i=0;i<_1.length;i++){var _5=_1.get(i);_3.summaryVars[_5.mappingKey]=_5[_2]}
return _3}
,isc.A.generateFunction=function isc_SummaryBuilder_generateFunction(){return this.getClass().generateFunction(this.getBasicValueObject(),this.getUsedFields(),this.component)}
,isc.A.initWidget=function isc_SummaryBuilder_initWidget(){this.Super("initWidget",arguments)}
);isc.B._maxIndex=isc.C+10;isc.A=isc.SummaryBuilder;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.getAvailableFields=function isc_c_SummaryBuilder_getAvailableFields(_1,_2){var _3=[];if(!_1)return _3;for(var i=0,j=0;i<_1.getLength();i++){var _6=_1.get(i);if(_2&&_2.name==_6.name)continue;_6.originalOrder=i;_6.mappingKey=isc.FormulaBuilder.mappingKeyForIndex(j++);if(!_6.title)_6.title=isc.DataSource.getAutoTitle(_6.name);_3.add(_6)}
var _7=_2&&_2.userSummary?_2.userSummary.summaryVars:{};for(var _8 in _7){var _9=_3.find("mappingKey",_8),_10=_3.find("name",_7[_8]),_11=_10.mappingKey;_10.mappingKey=_9.mappingKey;_9.mappingKey=_11}
_3=_3.sortByProperties(["mappingKey"],[true],[function(_6,_13,_14){var _12=_6[_13];if(_12.length==1)_12='  '+_12;else if(_12.length==2)_12=' '+_12;return _12}]);return _3}
,isc.A.getUsedFields=function isc_c_SummaryBuilder_getUsedFields(_1,_2,_3,_4){var _5=this.getAvailableFields(_2,_4),_6=[];if(!_5||!_1)return _6;_5=_5.sortByProperties(["mappingKey"],[false],[function(_9,_10,_11){var _7=_9[_10];if(_7.length==1)_7='  '+_7;else if(_7.length==2)_7=' '+_7;return _7}]);for(var i=0;i<_5.length;i++){var _9=_5.get(i);if(_1.indexOf("#{"+_9.mappingKey+"}")>=0)
_6.add(_9);else if((_9.mappingKey.length==1||_3)&&_1.indexOf("#"+_9.mappingKey)>=0)_6.add(_9)}
return _6}
,isc.A.testFunction=function isc_c_SummaryBuilder_testFunction(_1,_2,_3,_4){var _5={},_6=this.getFieldIdProperty(_3);try{_5.component=_3;_5.record=this.getTestRecord(_3,_4);if(_2.text==""){_5.emptyTestValue=true;return _5}
_5.jsFunction=this.generateFunction(_2,_4,_3);_5.result=_5.jsFunction(_5.record,_1[_6],_3)}catch(err){if(!_5.jsFunction)_5.failedGeneration=true;_5.failedExecution=true;_5.errorText=err.message}
return _5}
,isc.A.generateFunction=function isc_c_SummaryBuilder_generateFunction(_1,_2,_3){var _4=isc.SB.create(),_5=_1.text,_6=this.getFieldIdProperty(_3),_7=this.getFieldDetailsFromValue(_1.summaryVars,_2,_3),_8=_7.usedFields,_9=_7.missingFields;_8=_8.sortByProperties(["mappingKey"],[false],[function(_12,_15,_16){var _10=_12[_15];if(_10.length==1)_10='  '+_10;else if(_10.length==2)_10=' '+_10;return _10}]);if(_8.length>0){_4.append("var ");for(var i=0;i<_8.length;i++){var _12=_8.get(i);if(i>0)_4.append("        ");_4.append(_12.mappingKey,"=(component ? component.getStandaloneFieldValue(record,'",_12[_6],"') : record['",_12[_6],"']");_4.append(i==_8.length-1?");":"),","\n");_5=_5.replaceAll("#{"+_12.mappingKey+"}","'+"+_12.mappingKey+"+'");_5=_5.replaceAll("#"+_12.mappingKey,"'+"+_12.mappingKey+"+'")}
_4.append("\n")}
_5=_5.replace(/(#({[A-Z][A-Z]?}|[A-Z][A-Z]?))/g,(_3&&_3.missingSummaryFieldValue)||"-");if(_5.substr(0,2)=="'+"){_5=_5.substr(2)}else if(_5.substr(0,1)!="'"){_5="'"+_5}
if(_5.substr(_5.length-2)=="+'"){_5=_5.substr(0,_5.length-2)}else if(_5.substr(_5.length-1)!="'"){_5=_5+"'"}
_4.append("return ",_5,";");var _13=_4.toString(),_14=new Function("record,fieldName,component",_13);return _14}
);isc.B._maxIndex=isc.C+4;isc.defineClass("HiliteRule","HLayout");isc.A=isc.HiliteRule.getPrototype();isc.A.height=1;isc.A.width="100%";isc.A.overflow="visible";isc.A.clauseConstructor="FilterClause";isc.A.clauseProperties={width:"100%",fieldPickerWidth:"*",clauseDefaults:{canEditField:function(){return true}},operatorPickerWidth:140,valueItemWidth:130,excludeNonFilterableFields:false};isc.A.hiliteFormDefaults={_constructor:"DynamicForm",numCols:3,colWidths:[90,40,80],width:210,items:[{name:"colorType",type:"SelectItem",showTitle:false,valign:"center",valueMap:{foreground:"Foreground",background:"Background"},defaultValue:"foreground",width:"*"},{name:"color",title:"Color",type:"ColorItem",width:"*"}]};isc.A.advancedClauseLayoutDefaults={_constructor:"HLayout",height:1,width:"100%"};isc.A.advancedClauseLabelDefaults={_constructor:"Label",autoParent:"advancedClauseLayout",width:"*",overflow:"hidden",height:18,valign:"center",wrap:false,padding:1};isc.A.advancedClauseEditButtonDefaults={_constructor:"ImgButton",autoParent:"advancedClauseLayout",width:18,height:18,layoutAlign:"center",src:"[SKINIMG]/actions/edit.png",showRollOver:false,showDown:false,showDisabled:false,click:function(){this.creator.editAdvancedRule()}};isc.A.showRemoveButton=true;isc.A.removeButtonPrompt="Remove";isc.A.removeButtonDefaults={_constructor:isc.ImgButton,width:18,height:18,layoutAlign:"center",src:"[SKIN]/actions/remove.png",showRollOver:false,showDown:false,showDisabled:false,hoverWidth:80,click:function(){this.creator.remove()}};isc.A=isc.HiliteRule.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.initWidget=function isc_HiliteRule_initWidget(){if(!this.isAdvanced&&this.hilite){var _1=this.hilite.criteria;if(_1&&_1.criteria&&isc.isAn.Array(_1.criteria))
this.isAdvanced=true}
if(isc.isA.String(this.dataSource))this.dataSource=isc.DS.getDataSource(this.dataSource);if(this.hilite)this.checkHiliteProperties(this.hilite);if(this.isAdvanced){var _2=isc.FilterBuilder.getFilterDescription(this.hilite.criteria,this.dataSource);var _3=(_2.indexOf(isc.FilterBuilder.missingFieldPrompt)>=0);this.membersMargin=2;this.addAutoChild("advancedClauseLayout");this.addAutoChild("removeButton",{disabled:_3?true:false,autoParent:"advancedClauseLayout"});this.addAutoChild("advancedClauseLabel",{contents:_2,prompt:_2,disabled:_3?true:false});this.addAutoChild("advancedClauseEditButton",{disabled:_3?true:false})}else{var _3=(this.dataSource.getField(this.fieldName)==null);this.addAutoChild("clause",{dataSource:this.dataSource,field:this.dataSource.getField(this.fieldName),fieldName:this.fieldName,criterion:this.hilite?this.hilite.criteria:null,showRemoveButton:this.showRemoveButton,disabled:_3?true:false,remove:function(){this.creator.remove()}});this.addMember(this.clause);this.addAutoChild("hiliteForm",{disabled:_3?true:false});if(this.hilite){this.hiliteForm.setValues({colorType:(this.hilite.textColor?"foreground":"background"),color:(this.hilite.textColor?this.hilite.textColor:this.hilite.backgroundColor)})}
this.addMember(this.hiliteForm)}}
,isc.A.checkHiliteProperties=function isc_HiliteRule_checkHiliteProperties(_1){if(!_1)return;if(_1.cssText){var _2=_1.cssText.split(";");for(var i=0;i<_2.length;i++){var _4=_2[i],_5=_4.split(":");if(_5[0]=="textColor"&&!_1.textColor)
_1.textColor=_5[1];else if(_5[0]=="backgroundColor"&&!_1.backgroundColor)
_1.backgroundColor=_5[1]}}else if(_1.textColor||_1.backgroundColor){_1.cssText="";if(_1.textColor)
_1.cssText+="color:"+_1.textColor+";";if(_1.backgroundColor)
_1.cssText+="background-color:"+_1.backgroundColor+";"}}
,isc.A.remove=function isc_HiliteRule_remove(){this.markForDestroy()}
,isc.A.getHilite=function isc_HiliteRule_getHilite(){if(this.isAdvanced){return this.hilite}
var _1=this.hilite=isc.addProperties(this.hilite||{},{fieldName:this.fieldName}),_2=this.hiliteForm.getValue("colorType"),_3=this.hiliteForm.getValue("color"),_4=this.clause.getCriterion();_1.criteria=_4;if(_2=="foreground"){_1.textColor=_3;_1.cssText="color:"+_3+";"}else{_1.backgroundColor=_3;_1.cssText="background-color:"+_3+";"}
if(this.hilite&&this.hilite.id)_1.id=this.hilite.id;return _1}
,isc.A.editAdvancedRule=function isc_HiliteRule_editAdvancedRule(){var _1=this.getID()+".editAdvancedRuleReply(hilite)";this.advancedHiliteDialog=isc.Window.create({title:"Advanced Highlight Editor",width:Math.round(isc.Page.getWidth()/2),height:1,isModal:true,showModalMask:true,showResizer:true,autoSize:true,autoCenter:true,items:[isc.AdvancedHiliteEditor.create({width:"100%",height:"100%",dataSource:this.fieldDataSource?null:this.dataSource,fieldDataSource:this.fieldDataSource,hilite:this.hilite,callback:_1})]});this.advancedHiliteDialog.show()}
,isc.A.editAdvancedRuleReply=function isc_HiliteRule_editAdvancedRuleReply(_1){this.advancedHiliteDialog.hide();this.advancedHiliteDialog.markForDestroy();if(_1){this.hilite=_1;var _2=isc.FilterBuilder.getFilterDescription(this.hilite.criteria,this.dataSource);this.advancedClauseLabel.setContents(_2);this.advancedClauseLabel.setPrompt(_2)}}
);isc.B._maxIndex=isc.C+6;isc.defineClass("HiliteEditor","VLayout");isc.A=isc.HiliteEditor.getPrototype();isc.A.mainLayoutDefaults={_constructor:"HLayout",width:"100%",extraSpace:5};isc.A.fieldLayoutDefaults={_constructor:"VLayout",width:180,autoParent:"mainLayout",showResizeBar:true};isc.A.addAdvancedHiliteButtonDefaults={_constructor:"IButton",title:"Add Advanced Rule",align:"center",width:"100%",height:22,autoParent:"fieldLayout",click:function(){this.creator.addAdvancedHilite()}};isc.A.fieldListDefaults={_constructor:"ListGrid",width:"100%",height:"*",autoParent:"fieldLayout",fields:[{name:"name",showIf:"false"},{name:"title",title:"Available Fields"}],recordClick:function(_1,_2){this.creator.addHilite(_2)}};isc.A.ruleLayoutDefaults={_constructor:"VLayout",top:22,membersMargin:1,padding:1,overflow:"auto",autoParent:"mainLayout",border:"1px solid grey",width:"100%",height:"100%"};isc.A.hiliteRuleDefaults={_constructor:"HiliteRule"};isc.A.hiliteButtonsDefaults={_constructor:"HLayout",layoutMargin:5,membersMargin:8,height:1};isc.A.saveButtonDefaults={_constructor:"IButton",autoParent:"hiliteButtons",title:"Save",click:function(){this.creator.saveHilites()}};isc.A.cancelButtonDefaults={_constructor:"IButton",autoParent:"hiliteButtons",title:"Cancel",click:function(){this.creator.completeEditing()}};isc.A.defaultWidth=800;isc.A.defaultHeight=300;isc.A=isc.HiliteEditor.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.initWidget=function isc_HiliteEditor_initWidget(){this.Super("initWidget",arguments);this.addAutoChildren(["mainLayout","fieldLayout","addAdvancedHiliteButton","fieldList","ruleLayout","hiliteButtons","saveButton","cancelButton"]);this.setDataSource(this.dataSource);this.setHilites(this.hilites)}
,isc.A.setDataSource=function isc_HiliteEditor_setDataSource(_1){this.dataSource=_1;if(this.fieldDataSource&&!this.fieldDataSource.$62m){this.setupFieldList()}else if(this.dataSource){this.getClientOnlyFieldDS()}else{this.logWarn("No DataSource present, can't edit hilites")}
this.fieldList.markForRedraw()}
,isc.A.setFieldDataSource=function isc_HiliteEditor_setFieldDataSource(_1){this.fieldDataSource=_1;this.setupFieldList()}
,isc.A.setupFieldList=function isc_HiliteEditor_setupFieldList(){this.fieldList.showFilterEditor=true;this.fieldList.setDataSource(this.fieldDataSource);this.fieldList.setFields([{name:"name",showIf:"false"},{name:"title",title:"Available Fields"},{name:"type",showIf:"false"}]);this.fieldList.fetchData()}
,isc.A.getClientOnlyFieldDS=function isc_HiliteEditor_getClientOnlyFieldDS(){var _1=isc.getValues(this.dataSource.getFields());var _2=[];for(var i=0;i<_1.length;i++){var _4=_1[i];if(!_4.hidden)_2.add(_4)}
this.fieldDataSource=isc.DataSource.create({$62m:true,fields:[{name:"name",showIf:"false"},{name:"title",title:"Available Fields"},{name:"type",showIf:"false"}],cacheData:_2,clientOnly:true});this.setupFieldList()}
,isc.A.addHilite=function isc_HiliteEditor_addHilite(_1){var _2=this.createAutoChild("hiliteRule",{width:"100%",fieldName:_1.name,dataSource:this.dataSource});this.showNewHilite(_2)}
,isc.A.removeHilite=function isc_HiliteEditor_removeHilite(_1){this.ruleLayout.members.remove(_1);_1.destroy()}
,isc.A.showNewHilite=function isc_HiliteEditor_showNewHilite(_1){this.ruleLayout.addMember(_1)}
,isc.A.addAdvancedHilite=function isc_HiliteEditor_addAdvancedHilite(){var _1=this.getID()+".addAdvancedHiliteReply(hilite)";this.advancedHiliteDialog=isc.Window.create({title:"Advanced Hilite Editor",width:Math.round(isc.Page.getWidth()/2),height:1,isModal:true,showModalMask:true,showResizer:true,canDragResize:true,autoSize:true,autoCenter:true,items:[isc.AdvancedHiliteEditor.create({width:"100%",height:"100%",dataSource:this.fieldDataSource?null:this.dataSource,fieldDataSource:this.fieldDataSource,callback:_1})]});this.advancedHiliteDialog.show()}
,isc.A.addAdvancedHiliteReply=function isc_HiliteEditor_addAdvancedHiliteReply(_1){this.advancedHiliteDialog.hide();this.advancedHiliteDialog.markForDestroy();if(!_1)return;var _2=this.createAutoChild("hiliteRule",{width:"100%",isAdvanced:true,dataSource:this.dataSource,fieldDataSource:this.fieldDataSource,fieldName:_1.fieldName,hilite:_1});this.showNewHilite(_2)}
,isc.A.clearHilites=function isc_HiliteEditor_clearHilites(){for(var i=this.ruleLayout.members.length-1;i>=0;i--)
this.removeHilite(this.ruleLayout.getMember(i))}
,isc.A.setHilites=function isc_HiliteEditor_setHilites(_1){_1=this.hilites=_1||[];for(var i=0;i<_1.length;i++){var _3=_1[i],_4=this.createAutoChild("hiliteRule",{fieldName:_3.fieldName,hilite:_3,dataSource:this.dataSource});this.showNewHilite(_4)}}
,isc.A.saveHilites=function isc_HiliteEditor_saveHilites(_1){var _2=this.getHilites();this.completeEditing(_2)}
,isc.A.getHilites=function isc_HiliteEditor_getHilites(){var _1=this.ruleLayout.members,_2=[];for(var i=0;i<_1.length;i++){var _4=_1[i],_5=_4.getHilite();_2.add(_5)}
return _2}
,isc.A.getHiliteState=function isc_HiliteEditor_getHiliteState(){var _1=this.getHilites();if(_1==null)return null;return"("+isc.JSON.encode(_1,{dateFormat:"dateConstructor"})+")"}
,isc.A.completeEditing=function isc_HiliteEditor_completeEditing(_1){if(this.logIsInfoEnabled())this.logInfo("returning hilites: "+isc.echoFull(_1));if(this.callback)this.fireCallback(this.callback,"hilites",[_1])}
);isc.B._maxIndex=isc.C+16;isc.defineClass("AdvancedHiliteEditor","VStack");isc.A=isc.AdvancedHiliteEditor.getPrototype();isc.A.padding=10;isc.A.membersMargin=10;isc.A.filterBuilderDefaults={_constructor:"FilterBuilder",isGroup:true,groupTitle:"Filter",padding:8,maxHeight:200,overflow:"visible"};isc.A.hiliteFormDefaults={_constructor:"DynamicForm",isGroup:true,groupTitle:"Appearance",extraSpace:4,padding:8,width:"100%",numCols:6,colWidths:[200,150,100,150,100,150]};isc.A.hiliteButtonsDefaults={_constructor:isc.HLayout,membersMargin:8,height:1};isc.A.saveButtonDefaults={_constructor:"IButton",autoParent:"hiliteButtons",title:"Save",click:function(){this.creator.saveHilite()}};isc.A.cancelButtonDefaults={_constructor:"IButton",autoParent:"hiliteButtons",title:"Cancel",click:function(){this.creator.cancelEditing()}};isc.A.defaultWidth=800;isc.A.defaultHeight=600;isc.A.visibilityMode="multiple";isc.A.invalidHilitePrompt="Enter at least one rule, a color and a target field, or press 'Cancel' to abandon changes.";isc.A=isc.AdvancedHiliteEditor.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.initWidget=function isc_AdvancedHiliteEditor_initWidget(){this.Super("initWidget",arguments);var _1=this.getDataSource(),_2=this;this.addAutoChild("filterBuilder",{dataSource:_1,fieldDataSource:this.fieldDataSource,fieldNameChanged:function(_10){this.Super("fieldNameChanged",arguments);_2.fieldChosen(_10.getFieldName())}});var _3=[{title:"Target Field(s)",name:"fieldName",multiple:true,allowMultiSelect:true,type:"select"},{title:"Text",name:"textColor",type:"color"},{title:"Background",name:"backgroundColor",type:"color"}];this.addAutoChild("hiliteForm");if(this.fieldDataSource){_3[0]=isc.addProperties({},_3[0],{valueField:"name",displayField:"title",optionDataSource:this.fieldDataSource});delete _3[0].defaultDynamicValue;this.hiliteForm.addItems(_3)}else{var _4=this.fieldNames||_1.getFieldNames(),_5=this.fieldMap={};for(var i=0;i<_4.length;i++){var _7=_4[i],_8=_1.getField(_7),_9=_8.title;if(_8.hidden)continue;_9=_9?_9:_7;_5[_7]=_9}
this.fieldMap=_5;_3[0].valueMap=_5;this.hiliteForm.addItems(_3)}
this.addAutoChildren(["hiliteButtons","saveButton","cancelButton"]);this.addMembers([this.filterBuilder,this.hiliteForm,this.hiliteButtons]);if(this.hilite!=null){this.filterBuilder.setCriteria(this.hilite.criteria);this.hiliteForm.editRecord(this.hilite)}}
,isc.A.fieldChosen=function isc_AdvancedHiliteEditor_fieldChosen(_1){if(_1&&this.hiliteForm.getValue("fieldName")==null){this.hiliteForm.setValue("fieldName",_1)}}
,isc.A.saveHilite=function isc_AdvancedHiliteEditor_saveHilite(){this.hiliteForm.setValue("criteria",this.filterBuilder.getCriteria());var _1=this.hiliteForm.getValues();if(_1.criteria.criteria==null||_1.criteria.criteria.length==0||(!_1.textColor&&!_1.backgroundColor)||_1.fieldName==null)
{isc.say(this.invalidHilitePrompt);return}
var _2="";if(_1.textColor&&_1.textColor!=""){_2+="color:"+_1.textColor+";"}
if(_1.backgroundColor&&_1.backgroundColor!=""){_2+="background-color:"+_1.backgroundColor+";"}
_1.cssText=_2;if(this.hilite&&this.hilite.id)_1.id=this.hilite.id;this.completeEditing(_1)}
,isc.A.cancelEditing=function isc_AdvancedHiliteEditor_cancelEditing(){this.completeEditing(null)}
,isc.A.completeEditing=function isc_AdvancedHiliteEditor_completeEditing(_1){if(this.callback)this.fireCallback(this.callback,["hilite"],[_1])}
);isc.B._maxIndex=isc.C+5;isc.ClassFactory.defineClass("ReportBuilder","VLayout");isc.A=isc.ReportBuilder;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.reportIdField="reportId";isc.A.reportNameField="reportName";isc.A.reportCategoryField="reportCategory";isc.A.reportCreatorField="reportCreator";isc.A.reportIsSharedField="reportIsShared";isc.A.reportIsDefaultField="reportIsDefault";isc.A.reportViewStateField="reportViewState";isc.A.defaultDataSource="ReportChooserDS";isc.B.push(isc.A.showReportBuilder=function isc_c_ReportBuilder_showReportBuilder(_1,_2,_3,_4,_5){var _6=isc.Window.create(isc.addProperties({},{isModal:true,width:"90%",height:"90%",title:"Report Builder",vertical:true,autoSize:true,autoCenter:true,visible:false,callback:_2,showMinimizeButton:false,closeClick:function(){if(this.callback)this.fireCallback(this.callback);return this.Super("closeClick")}},_4));var _7=isc.ReportBuilder.create(isc.addProperties({},{width:"100%",height:"100%",dataSource:_5||isc.DS.get(this.defaultDataSource),grid:_1,callback:_2},_3));_6.body.addChild(_7);_6.show()}
,isc.A.setLinkedGridState=function isc_c_ReportBuilder_setLinkedGridState(_1,_2){_2=_2||this.previewGrid;if(!_2||!_1)return;var _3=_1["reportViewState"],_4=isc.ReportBuilder.getObjectFromState(_3);_2.setCriteria(_4&&_4.criteria?isc.ReportBuilder.getObjectFromState(_4.criteria):null);var _5=isc.ReportBuilder.getObjectFromState(_4.field);_2.setFieldState(_5?_5.field:null);_2.setSortState(_5?_5.sort:null);_2.ungroup();if(_5.group&&_5.group.length>0)
_2.groupBy(_5?_5.group:null);_2.setHiliteState(_4.hilite)}
,isc.A.getObjectFromState=function isc_c_ReportBuilder_getObjectFromState(_1){if(_1==null)return null;var _2=eval(_1);return _2}
,isc.A.getStateForObject=function isc_c_ReportBuilder_getStateForObject(_1){if(_1==null)return null;return"("+isc.JSON.encode(_1,{dateFormat:"dateConstructor"})+")"}
);isc.B._maxIndex=isc.C+4;isc.A=isc.ReportBuilder.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.vertical=true;isc.A.padding=10;isc.A.width=830;isc.A.height=600;isc.A.layoutDefaults={_constructor:"VLayout",width:"100%",height:1};isc.A.reportGridDefaults={_constructor:"ListGrid",width:"100%",height:122,autoParent:"layout",canEdit:false,autoFetchData:true,recordClick:function(_1,_2){this.creator.showRecord(_2)},dataArrived:function(_1,_2){var _3=this.getSelection();if(_3&&_3.length==0){this.selectSingleRecord(0);this.recordClick(this,this.getRecord(0))}},fields:[{name:"reportName",title:"Report Name"},{name:"reportDescription",title:"Description"},{name:"reportCreator",title:"Created By"},{name:"reportIsDefault",type:"boolean",title:"Default"},{name:"reportIsShared",type:"boolean",title:"Shared"},{name:"reportCategory",title:"Category"}]};isc.A.reportGridButtonLayoutDefaults={_constructor:"HLayout",width:"100%",height:1,overflow:"visible",autoParent:"layout",align:"right"};isc.A.cloneSelectedButtonTitle="Clone";isc.A.cloneSelectedButtonDefaults={_constructor:"IButton",autoFit:true,click:"this.creator.cloneSelected();",autoParent:"reportGridButtonLayout"};isc.A.addNewButtonTitle="+";isc.A.addNewButtonDefaults={_constructor:"IButton",width:18,click:"this.creator.addNew();",autoParent:"reportGridButtonLayout"};isc.A.removeSelectedButtonTitle="-";isc.A.removeSelectedButtonDefaults={_constructor:"IButton",width:18,click:"this.creator.removeSelected();",autoParent:"reportGridButtonLayout"};isc.A.editorLayoutDefaults={_constructor:"VLayout",width:"100%",height:1,overflow:"visible",layoutTopMargin:15,autoParent:"layout"};isc.A.editorFormDefaults={_constructor:"DynamicForm",width:"100%",numCols:7,colWidths:[100,"*","*",120,60,60,80],fields:[{name:"reportName",type:"text",colSpan:2,width:"*",title:"Report Name"},{name:"reportIsDefault",type:"boolean",colSpan:1,width:"*",showTitle:false},{name:"reportIsShared",type:"boolean",colSpan:1,width:"*",title:"Shared",showTitle:false},{name:"reportCategory",type:"select",width:"*",title:"Category",endRow:true,valueMap:["Financial","HR"]},{name:"reportDescription",type:"textArea",colSpan:"*",width:"*",height:66,title:"Description",endRow:true}],autoParent:"editorLayout"};isc.A.editorTabSetDefaults={_constructor:"TabSet",width:"100%",height:200,autoParent:"editorLayout"};isc.A.editorButtonLayoutDefaults={_constructor:"HLayout",width:"100%",height:1,overflow:"visible",autoParent:"editorLayout",backgroundColor:"lightblue",membersMargin:5,padding:3,align:"right"};isc.A.editorPreviewLabelTitle="Preview";isc.A.editorPreviewLabelDefaults={_constructor:"Label",width:"*",height:22,layoutAlign:"right",autoParent:"editorButtonLayout"};isc.A.editorTryItButtonTitle="Apply";isc.A.editorTryItButtonDefaults={_constructor:"IButton",autoFit:true,click:"this.creator.tryIt();",layoutAlign:"right",autoParent:"editorButtonLayout"};isc.A.editorSaveButtonTitle="Save";isc.A.editorSaveButtonDefaults={_constructor:"IButton",autoFit:true,click:"this.creator.saveEditor();",layoutAlign:"right",autoParent:"editorButtonLayout"};isc.A.editorCancelButtonTitle="Revert";isc.A.editorCancelButtonDefaults={_constructor:"IButton",autoFit:true,click:"this.creator.cancelEditor();",layoutAlign:"right",autoParent:"editorButtonLayout"};isc.A.criteriaPaneDefaults={_constructor:"VLayout",width:"100%",height:"100%",formDefaults:{_constructor:"DynamicForm",width:160,height:22,fields:[{name:"criteriaType",type:"radioGroup",colSpan:"*",width:"*",showTitle:false,vertical:false,valueMap:["Basic","Advanced"],defaultValue:"Advanced",changed:function(_1,_2,_3){_1.creator.showFilterBuilder(_3)}}],extraSpace:10},filterBuilderDefaults:{_constructor:"FilterBuilder",width:"100%",height:"100%"},initWidget:function(){this.Super("initWidget",arguments);if(this.criteriaState){this.initialCriteria=this.getState(this.criteriaState)}
this.addAutoChild("form");this.addAutoChild("filterBuilder",{dataSource:this.dataSource,criteria:this.initialCriteria});this.addMember(this.filterBuilder)},showFilterBuilder:function(_1){var _2=this.filterBuilder.getCriteria();this.removeMember(this.filterBuilder);this.filterBuilder.destroy();this.filterBuilder=null;var _3={dataSource:this.dataSource,criteria:_2};if(_1=="Basic")_3.topOperatorAppearance="radio";this.addAutoChild("filterBuilder",_3);this.addMember(this.filterBuilder)},getCriteria:function(){var _1=this.filterBuilder.getCriteria();if(!_1||_1.criteria.length==0)return null;return _1},setCriteria:function(_1){return this.filterBuilder.setCriteria(_1)},getState:function(_1){_1=_1||this.getCriteria();if(_1==null)return null;return"("+isc.JSON.encode(_1,{dateFormat:"dateConstructor"})+")"},setState:function(state){if(state==null)this.setCriteria(null);var crit=eval(state);this.setCriteria(crit)}};isc.A.columnPaneDefaults={_constructor:"VLayout",width:"100%",height:"100%",layoutDefaults:{_constructor:"VLayout",width:"100%",height:"100%"},layoutLabelDefaults:{_constructor:"Label",width:"100%",height:"30",contents:"Use the arrows or drag and drop column-names to configure visible columns."+"  You may also directly manipulate the Preview grid below.",autoParent:"layout"},childLayoutDefaults:{_constructor:"HLayout",width:"100%",height:"100%",autoParent:"layout"},fieldGridDefaults:{_constructor:"ListGrid",width:"30%",height:"100%",autoParent:"childLayout",fields:[{name:"title",title:"Title"},{name:"type",title:"Type"}],canDragRecordsOut:true,canAcceptDroppedRecords:true,canReorderRecords:true,dragDataAction:"move",transferRecords:function(_1,_2,_3,_4,_5){_1.setProperty("isInGrid",false);this.Super("transferRecords",arguments)}},buttonLayoutDefaults:{_constructor:"VLayout",width:1,height:"100%",autoParent:"childLayout",padding:10,align:"center",layoutAlign:"center"},moveLeftButtonDefaults:{_constructor:"IButton",width:30,height:30,title:"<<",autoParent:"buttonLayout",layoutAlign:"center",disabled:true,click:"this.creator.moveSelectionLeft()"},moveRightButtonDefaults:{_constructor:"IButton",width:30,height:30,title:">>",autoParent:"buttonLayout",layoutAlign:"center",disabled:true,click:"this.creator.moveSelectionRight()"},configGridDefaults:{_constructor:"ListGrid",width:"*",height:"100%",autoParent:"childLayout",fields:[{name:"name",showIf:"false",title:"Name"},{name:"title",title:"Title"},{name:"width",title:"Width"},{name:"frozen",canToggle:true,type:"boolean",title:"Frozen"},{name:"sortIndex",title:"Sort Order"},{name:"sortDirection",type:"select",title:"Sort Direction",valueMap:{ascending:"Ascending",descending:"Descending"}},{name:"groupIndex",title:"Group By Order"},{name:"masterIndex",showIf:"false"}],initialCriteria:{_constructor:"AdvancedCriteria",operator:"and",criteria:[{fieldName:"isInGrid",operator:"equal",value:true}]},initialSort:[{property:"masterIndex",direction:"ascending"}],canEdit:true,autoSaveEdits:true,canDragRecordsOut:true,canAcceptDroppedRecords:true,canReorderRecords:true,dragDataAction:"move",transferRecords:function(_1,_2,_3,_4,_5){_1.setProperty("isInGrid",true);this.Super("transferRecords",arguments)}},getPreviewGrid:function(){if(!this.previewGrid){this.previewGrid=this.creator.previewGrid;var _1=this;this.observe(this.previewGrid,"viewStateChanged","observer.setData(observer.previewGrid);")}
return this.previewGrid},initWidget:function(){this.Super("initWidget",arguments);this.addAutoChild("layout");this.addAutoChild("layoutLabel");this.addAutoChild("childLayout");this.addAutoChild("fieldGrid");this.addAutoChildren(["buttonLayout","moveLeftButton","moveRightButton"]);this.addAutoChild("configGrid",{previewGrid:this.getPreviewGrid()});if(this.columnState){this.setState(this.columnState)}else{this.setInitialData()}},moveSelectionLeft:function(){var _1=this.configGrid.getSelection();_1.setProperty("isInGrid",false);if(_1)this.fieldGrid.transferRecords(_1,null,null,this.configGrid)},moveSelectionRight:function(){var _1=this.fieldGrid.getSelection();if(_1)this.configGrid.transferRecords(_1,this.configGrid.data.length-1,this.configGrid.data.length,this.fieldGrid)},getState:function(){return isc.ReportBuilder.getStateForObject(this.getData())},setState:function(_1){if(!_1){this.setInitialData();return}
var _2=isc.ReportBuilder.getObjectFromState(_1),_3=this.getPreviewGrid(),_4=this.linkedGrid;_3.setFieldState(_2.field);_3.setSortState(_2.sort);if(_2&&_2.group=="")_2.group=null;_3.groupByFields=_2.group;_3.groupBy(_2.group);this.setData(_3)},setInitialData:function(){this.setData(this.linkedGrid)},getData:function(){var _1=this.getPreviewGrid();var _2={field:isc.ReportBuilder.getObjectFromState(_1.getFieldState()),sort:{}};var _3=this.configGrid.data,_4=[],_5=[]
for(var i=0;i<_2.field.length;i++){var _7=_2.field[i],_8=_3.find("name",_7.name);if(_8){delete _7.visible;_7.frozen=_8.frozen;delete _7.$521;if(_8.width=="AutoFit"){_7.autoFitWidth=true;delete _7.width}else{var _9=_8.width!=null?_8.width:"*";delete _7.autoFitWidth;var _10=parseInt(_9);_7.width=!isNaN(_10)?_10:_9}
if(_8.groupIndex!=null){_4.add({name:_8.name,groupIndex:_8.groupIndex})}
if(_8.sortIndex!=null){_5.add({name:_8.name,sortDirection:_8.sortDirection,sortIndex:_8.sortIndex})}}else{_7.visible=false}}
if(_5){_5=_5.sortByProperty("sortIndex",true);for(var i=0;i<_5.length;i++){if(!_2.sort.sortSpecifiers)_2.sort.sortSpecifiers=[];_2.sort.sortSpecifiers.add({property:_5[i].name,direction:_5[i].sortDirection})}}
if(_4){_4=_4.sortByProperty("groupIndex",true);_2.group=_4.getProperty("name").join(",")}
return _2},setData:function(_1){var _2=_1.getAllFields();var _3=_1.getFields(),_4=_1.getSort(),_5=_1.getGroupByFields(),_6=[];for(var i=0;i<_2.length;i++){var _8=isc.shallowClone(_2[i]),_9=_3.find("name",_8.name),_10=_1.getSortSpecifier(_8.name),_11=_10?_4.indexOf(_4.find("property",_8.name)):0,_12=_5?_5.contains(_8.name):false,_13=_12?_5.indexOf(_8.name):0;if(_9&&_9.visible!=false){_8.isInGrid=true}else{_8.isInGrid=false}
if(_8.autoFitWidth)_8.width="AutoFit";if(!_8.width)_8.width="*";if(_10){_8.sortIndex=_11+1;_8.sortDirection=_10.direction}else{delete _8.sortIndex;delete _8.sortDirection}
if(_12){_8.groupIndex=_13+1}else{delete _8.groupIndex}
_6.add(_8)}
var _14;if(this.fieldGrid){_14=_6.findAll("isInGrid",false);this.fieldGrid.setData(_14?_14.duplicate():[])}
if(this.configGrid){_14=_6.findAll("isInGrid",true);this.configGrid.setData(_14?_14.duplicate():[])}}};isc.A.hilitePaneDefaults={_constructor:"VLayout",width:"100%",height:"100%",overflow:"auto",hiliteEditorDefaults:{_constructor:"HiliteEditor",width:"100%",height:"100%",showHiliteButtons:false},initWidget:function(){this.Super("initWidget",arguments);if(this.hiliteState){this.hilites=this.getState(this.hiliteState)}
this.addAutoChild("hiliteEditor",{dataSource:this.dataSource,hilites:this.hilites});this.addMember(this.hiliteEditor)},getState:function(_1){var _2=_1||this.hiliteEditor.getHiliteState();return _2},setState:function(state){this.hiliteEditor.clearHilites();if(state==null){this.hiliteEditor.setHilites(null);return}
var hilites=eval(state);this.hiliteEditor.setHilites(hilites)}};isc.A.formulaPaneDefaults={_constructor:"VLayout",width:"100%",height:"100%",layoutDefaults:{_constructor:"VLayout",width:"100%",height:"100%"},childLayoutDefaults:{_constructor:"HLayout",width:"100%",height:"100%",autoParent:"layout"},fieldGridDefaults:{_constructor:"ListGrid",width:"30%",height:"100%",autoParent:"childLayout",fields:[{name:"title",title:"Title"},{name:"type",title:"Type"}]},builderDefaults:{_constructor:"FormulaBuilder",width:"*",height:"100%",autoParent:"childLayout"},initWidget:function(){this.Super("initWidget",arguments);if(this.formulaState){}
this.addAutoChild("layout");this.addAutoChild("layoutLabel");this.addAutoChild("childLayout");this.addAutoChild("fieldGrid");this.addAutoChild("builder",{dataSource:this.dataSource});this.addMember(this.layout)},getState:function(_1){},setState:function(state){}};isc.A.summaryPaneDefaults={_constructor:"VLayout",width:"100%",height:"100%",layoutDefaults:{_constructor:"VLayout",width:"100%",height:"100%"},childLayoutDefaults:{_constructor:"HLayout",width:"100%",height:"100%",autoParent:"layout"},fieldGridDefaults:{_constructor:"ListGrid",width:"30%",height:"100%",autoParent:"childLayout",fields:[{name:"title",title:"Title"},{name:"type",title:"Type"}]},builderDefaults:{_constructor:"SummaryBuilder",width:"*",height:"100%",autoParent:"childLayout"},initWidget:function(){this.Super("initWidget",arguments);if(this.summaryState){}
this.addAutoChild("layout");this.addAutoChild("layoutLabel");this.addAutoChild("childLayout");this.addAutoChild("fieldGrid");this.addAutoChild("builder",{dataSource:this.dataSource});this.addMember(this.layout)},getState:function(_1){},setState:function(state){}};isc.A.previewGridDefaults={_constructor:"ListGrid",width:"100%",height:122,autoParent:"layout",canEdit:false,autoFetchData:true};isc.A.normalAutoChildren=["layout","reportGrid","reportGridButtonLayout","cloneSelectedButton","adddNewButton","removeSelectedButton"];isc.A.editorAutoChildren=["editorLayout","editorForm","editorTabSet","editorButtonLayout","editorPreviewLabel","editorSaveButton","editorCancelButton"];isc.A.otherAutoChildren=["previewGrid"];isc.B.push(isc.A.initWidget=function isc_ReportBuilder_initWidget(){this.Super("initWidget",arguments);this.reportIdField=this.reportIdField||this.getClass().reportIdField;this.reportNameField=this.reportNameField||this.getClass().reportNameField;this.reportCategoryField=this.reportCategoryField||this.getClass().reportCategoryField;this.reportCreatorField=this.reportCreatorField||this.getClass().reportCreatorField;this.reportIsSharedField=this.reportSharedField||this.getClass().reportIsSharedField;this.reportIsDefaultField=this.reportIsDefaultField||this.getClass().reportIsDefaultField;this.reportViewStateField=this.reportViewStateField||this.getClass().reportViewStateField;this.dataSource=isc.DS.get(this.dataSource);if(!isc.isA.DataSource(this.dataSource))
this.dataSource=isc.DS.get(this.getClass().defaultDataSource);this.addAutoChild("layout");var _1={dataSource:this.dataSource};var _2=null;this.addAutoChild("reportGrid",{dataSource:this.dataSource,initialCriteria:_2});this.addAutoChild("reportGridButtonLayout");this.addAutoChild("cloneSelectedButton",{title:this.cloneSelectedButtonTitle});this.addAutoChild("addNewButton",{title:this.addNewButtonTitle});this.addAutoChild("removeSelectedButton",{title:this.removeSelectedButtonTitle});this.createEditorLayout();var _3=this.getGrid();this.addAutoChild("previewGrid",{dataSource:_3.dataSource});var _4=_3.getViewState();this.previewGrid.setViewState(_4)}
,isc.A.createEditorLayout=function isc_ReportBuilder_createEditorLayout(){if(!this.editorLayout){this.addAutoChild("editorLayout");this.addAutoChild("editorForm",{dataSource:this.dataSource,extraSpace:10});this.addMember(isc.LayoutSpacer.create({width:"100%",height:10}));this.addAutoChild("editorTabSet");this.editorTabSet.addTabs([{ID:"criteria_tab",name:"criteriaPane",title:"Criteria",selected:true},{ID:"columns_tab",name:"columnsPane",title:"Columns"},{ID:"hilites_tab",name:"hilitesPane",title:"Hilites"},{ID:"formula_tab",name:"formulaPane",title:"Calculated Columns"},{ID:"summary_tab",name:"summaryPane",title:"Summary Columns"}]);this.editorTabSet.selectTab(0);this.addAutoChild("criteriaPane",{dataSource:this.getGridDataSource()});this.editorTabSet.setTabPane(0,this.criteriaPane);this.addAutoChild("columnPane",{dataSource:this.getGridDataSource(),linkedGrid:this.getGrid()});this.editorTabSet.setTabPane(1,this.columnPane);this.addAutoChild("hilitePane",{dataSource:this.getGridDataSource()});this.editorTabSet.setTabPane(2,this.hilitePane);this.addAutoChild("formulaPane",{dataSource:this.getGridDataSource()});this.editorTabSet.setTabPane(3,this.formulaPane);this.addAutoChild("summaryPane",{dataSource:this.getGridDataSource()});this.editorTabSet.setTabPane(4,this.summaryPane);this.addAutoChild("editorButtonLayout");this.addAutoChild("editorPreviewLabel",{contents:this.editorPreviewLabelTitle});this.addAutoChild("editorTryItButton",{title:this.editorTryItButtonTitle});this.addAutoChild("editorSaveButton",{title:this.editorSaveButtonTitle});this.addAutoChild("editorCancelButton",{title:this.editorCancelButtonTitle})}else{this.editorLayout.show()}}
,isc.A.getUserID=function isc_ReportBuilder_getUserID(){return"testUser"}
,isc.A.getSelected=function isc_ReportBuilder_getSelected(){var _1=this.reportGrid.getSelection();if(isc.isAn.Array(_1)&&_1.length>0)return _1[0];return null}
,isc.A.cloneSelected=function isc_ReportBuilder_cloneSelected(){var _1=this.getSelected();if(_1){var _2=isc.addProperties({},_1);_2[this.dataSource.getPrimaryKeyFieldNames()[0]]=null;this.showEditor(_2,"add")}}
,isc.A.showRecord=function isc_ReportBuilder_showRecord(_1){if(_1)this.showEditor(_1)}
,isc.A.addNew=function isc_ReportBuilder_addNew(){this.showEditor(null,"add")}
,isc.A.removeSelected=function isc_ReportBuilder_removeSelected(){var _1=this.getSelected();if(_1){this.reportGrid.removeRecord(null,_1)}}
,isc.A.getGrid=function isc_ReportBuilder_getGrid(){return this.grid}
,isc.A.setGrid=function isc_ReportBuilder_setGrid(_1){this.grid=_1}
,isc.A.getGridDataSource=function isc_ReportBuilder_getGridDataSource(){var _1=this.getGrid();if(!_1)return this.dataSource;return _1.getDataSource()}
,isc.A.showEditor=function isc_ReportBuilder_showEditor(_1,_2){this.setEditorData(_1,_2)}
);isc.B._maxIndex=isc.C+12;isc.A=isc.ReportBuilder.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.getEditorCriteriaState=function isc_ReportBuilder_getEditorCriteriaState(){return this.criteriaPane?this.criteriaPane.getState():null}
,isc.A.setEditorCriteriaState=function isc_ReportBuilder_setEditorCriteriaState(_1){if(this.criteriaPane)
this.criteriaPane.setState(_1?_1.criteria:null)}
,isc.A.getEditorColumnState=function isc_ReportBuilder_getEditorColumnState(){return this.columnPane?this.columnPane.getState():null}
,isc.A.setEditorColumnState=function isc_ReportBuilder_setEditorColumnState(_1){if(this.columnPane)this.columnPane.setState(_1?_1.field:null)}
,isc.A.getEditorHiliteState=function isc_ReportBuilder_getEditorHiliteState(){return this.hilitePane?this.hilitePane.getState():null}
,isc.A.setEditorHiliteState=function isc_ReportBuilder_setEditorHiliteState(_1){if(this.hilitePane)this.hilitePane.setState(_1?_1.hilite:null)}
,isc.A.getEditorSummaryState=function isc_ReportBuilder_getEditorSummaryState(){return this.summaryPane?this.summaryPane.getState():null}
,isc.A.setEditorSummaryState=function isc_ReportBuilder_setEditorSummaryState(_1){if(this.summaryPane)this.summaryPane.setState(_1?_1.summary:null)}
,isc.A.getEditorFormulaState=function isc_ReportBuilder_getEditorFormulaState(){return this.formulaPane?this.formulaPane.getState():null}
,isc.A.setEditorFormulaState=function isc_ReportBuilder_setEditorFormulaState(_1){if(this.formulaPane)this.formulaPane.setState(_1?_1.formula:null)}
,isc.A.setEditorData=function isc_ReportBuilder_setEditorData(_1,_2){var _3=this.editorForm;if(!_1||_2=="add"){_3.editNewRecord(isc.addProperties({},_1,{reportCreator:this.getUserID()}))}else _3.editRecord(_1);var _4=_1?isc.ReportBuilder.getObjectFromState(_1[this.reportViewStateField]):null;this.setEditorCriteriaState(_4);this.setEditorColumnState(_4);this.setEditorHiliteState(_4);this.setEditorFormulaState(_4);this.setEditorSummaryState(_4);this.tryIt()}
,isc.A.clearEditorData=function isc_ReportBuilder_clearEditorData(){this.setEditorData(null,null)}
,isc.A.hideEditor=function isc_ReportBuilder_hideEditor(){this.editorLayout.hide();var _1=this.getSelected();this.cloneSelectedButton.setDisabled(!_1);this.addNewButton.enable();this.removeSelectedButton.setDisabled(!_1);this.reportGrid.enable()}
,isc.A.updateFormRecord=function isc_ReportBuilder_updateFormRecord(){var _1=this.editorForm;var _2={criteria:this.getEditorCriteriaState(),field:this.getEditorColumnState(),hilite:this.getEditorHiliteState(),formula:this.getEditorFormulaState(),summary:this.getEditorSummaryState()};_1.setValue(this.reportViewStateField,isc.ReportBuilder.getStateForObject(_2))}
,isc.A.saveEditor=function isc_ReportBuilder_saveEditor(){this.updateFormRecord();this.editorForm.saveData()}
,isc.A.cancelEditor=function isc_ReportBuilder_cancelEditor(){var _1=this.getSelected(),_2=this.editorForm.formOperationType,_3=0;if(_2!="add"){_3=this.reportGrid.getRecordIndex(_1)}
this.clearEditorData();this.reportGrid.deselectAllRecords();this.reportGrid.selectSingleRecord(_3);this.showEditor(this.reportGrid.getRecord(_3))}
,isc.A.tryIt=function isc_ReportBuilder_tryIt(){if(this.columnPane&&this.columnPane.configGrid)this.columnPane.configGrid.endEditing();this.updateFormRecord();isc.ReportBuilder.setLinkedGridState(this.editorForm.getData(),this.previewGrid)}
);isc.B._maxIndex=isc.C+17;isc._moduleEnd=isc._Grids_end=(isc.timestamp?isc.timestamp():new Date().getTime());if(isc.Log&&isc.Log.logIsInfoEnabled('loadTime'))isc.Log.logInfo('Grids module init time: ' + (isc._moduleEnd-isc._moduleStart) + 'ms','loadTime');delete isc.definingFramework;}else{if(window.isc && isc.Log && isc.Log.logWarn)isc.Log.logWarn("Duplicate load of module 'Grids'.");}
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

