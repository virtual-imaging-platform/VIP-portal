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

if(window.isc&&window.isc.module_Core&&!window.isc.module_Forms){isc.module_Forms=1;isc._moduleStart=isc._Forms_start=(isc.timestamp?isc.timestamp():new Date().getTime());if(isc._moduleEnd&&(!isc.Log||(isc.Log && isc.Log.logIsDebugEnabled('loadTime')))){isc._pTM={ message:'Forms load/parse time: ' + (isc._moduleStart-isc._moduleEnd) + 'ms', category:'loadTime'};
if(isc.Log && isc.Log.logDebug)isc.Log.logDebug(isc._pTM.message,'loadTime')
else if(isc._preLog)isc._preLog[isc._preLog.length]=isc._pTM
else isc._preLog=[isc._pTM]}isc.definingFramework=true;isc.A=isc.Canvas;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.applyTableResizePolicy=function isc_c_Canvas_applyTableResizePolicy(_1,_2,_3,_4,_5,_6,_7){var _8=this.logIsDebugEnabled("tablePolicy"),_9=this.logIsInfoEnabled("tablePolicy"),_10=this.logIsDebugEnabled("tablePlacement");var _11=_1.$17s;if(!this.$17t(_1)){_11=_1.$17s=[];var _12=0,_13=0;for(var _14=0;_14<_1.length;_14++){var _15=_1[_14];if(!_15.alwaysTakeSpace&&!_15.visible)continue;var _16=_15.getColSpan(),_17=_15.getRowSpan();if(_17==0||_16==0)continue;if(_16==null)_16=1;if(_17==null)_17=1;var _18=_16;if(_16=="*")_18=1;var _19=_15.getTitleOrientation();if(_15.showTitle&&(_19==isc.Canvas.LEFT||_19==isc.Canvas.RIGHT))
{_18+=(_15.getTitleColSpan()||1);if(_16!="*")_16+=(_15.getTitleColSpan()||1)}
var _20=(_15.isStartRow?_15.isStartRow():_15.startRow),_21=(_15.isEndRow?_15.isEndRow():_15.endRow);if(_10){this.logDebug("at: "+["row"+_12,"col"+_13]+", item: "+(_15.name||_15.Class)+(_16=="*"?", colSpan:'*'":"")+", required cols:"+_18+(_17>1?", rowSpan:"+_17:"")+(_20?", startRow:true":"")+(_21?", endRow:true":""),"tablePlacement")}
var _22=null,_23=null;if(_13>=_4||(_20&&_13!=0)){_12++;_13=0;_15.$17u=true}else{_15.$17u=false}
if(_12<_11.length){for(;_12<_11.length;_12++){var _24=_11[_12];if(_24==null)break;for(;_13<_4;_13++){if(_24[_13]!=null)continue;for(var j=_13;j<_4;j++){if(_24[j]!=null)break;if((j-_13)+1>=_18){_22=_12;_23=_13;break}}
if(_23!=null)break}
if(_23!=null)break;_13=0;_15.$17u=true}}
if(_23==null){_22=_12;_23=0;_15.$17u=true}
_13=_23;if(_16=="*")_16=_4-_13;if(!isc.isA.Number(_17))_17=1;for(var r=_12;r<_12+_17;r++){if(!_11[r])_11[r]=[];for(var c=_13;c<_13+_16;c++){_11[r][c]=_14}}
_15.$17v=[_23,_22,_23+_16,_22+_17];_13+=_16;if(_21)_13=_4;if(_10){this.logDebug("item: "+(_15.name||_15.Class)+" placed at: "+["row"+_22,"col"+_23]+(_15.$17u?", marked startRow ":"")+", rowTable: "+this.echoAll(_11),"tablePlacement")}}
var _28=0;for(var r=0;r<_11.length;r++){var _29=_11[r];if(_29==null)break;var _30=0,_31=null;for(var c=0;c<_29.length;c++){if(_29[c]==null){_30++;continue}
if(r>0&&_11[r-1]!=null&&_29[c]==_11[r-1][c])continue;var _14=_29[c],_15=_1[_14];if(_15==_31||_15==null)continue;_15.$17w=_28;_15.$17x=_30;if(_10&&(_30>0||_28>0)){this.logDebug("itemNum:"+_14+" ("+(_15.name||_15.Class)+") at: "+["row"+_22,"col"+_23]+" preceded by "+(_30>0?_30+" empty cells":"")+(_28>0?" "+_28+" empty rows":""),"tablePlacement")}
_30=_28=0;_31=_15}
if(_31==null){_28++;_30=0}}}
if(!_5||!isc.isAn.Array(_5)){if(!isc.isAn.Array(_5)){this.logWarn(" 'colWidths' not an array - Ignoring.","tableResizePolicy")}
_5=[]}
_5=_5.duplicate();for(var c=0;c<_5.length;c++){var _32=_5[c];if(isc.isA.String(_32)){if(_32=="*")_5[c]=[0,10000,0,1];else if(_32.contains("*"))_5[c]=[0,10000,0,parseInt(_32)];else if(_32.contains("%"))_5[c]=[0,10000,parseInt(_32),0];else{var _33=parseInt(_32);if(_33==_32){_5[c]=_33}else{this.logWarn("Failed to understand specified colWidth:"+_32);_5[c]=[0,10000,0,1]}}}}
_1.colWidths=_5;if(!_6){_6=[];for(var r=0;r<_11.length;r++){var _29=_11[r],_34=null,_35=100000,_36=0,_37=0;if(!_29)continue;for(var c=0;c<_29.length;c++){var _15=_1[_29[c]];if(!_15)continue;var _38=_15.getCellHeight(_7);var _17=(_15.$17v[3]-_15.$17v[1]);if(_8)this.logWarn("item at: "+[r,c]+" has height: "+_38+", item is: "+_15);_15.$17y=false;if(isc.isA.Number(_38)){_38=Math.floor(_38/ _17);if(_8)this.logWarn("item: "+_15+" has pixel size: "+_38);if(_34==null||_38>_34){_34=_38}
if(_38>_35)_35=_38}else if(isc.isA.String(_38)){if(_38.contains("*")){_15.$17y=true;var _39=(_38=="*"?1:parseFloat(_38))
/ _17;                        if (_8) this.logWarn("item: " + _15 + " has star size: " + _39);						_37 = Math.max(_37, _39);					// else if height is a percentage
					} else {                        _15.$17y = true;						// get the percentage as a number
						// NOTE: if the item takes up more than one row, split it evenly across
                        // its rows
						var _40 = parseFloat(_38) /_17;if(_8)this.logWarn("item: "+_15+" has percent size: "+_40);if(_40>_36)_36=_40}
if(_15.minHeight>_34){_34=_15.minHeight}
if(_15.minHeight>_35){_35=_15.minHeight}
if(_15.maxHeight<_35&&_34<_15.maxHeight)
{_35=_15.maxHeight}}
if(_36>0||_37>0){if(_34==null)_34=0;_6[r]=[_34,_35,_36,_37]}else{if(_34==null){_34=_1.$17z||22}
_6[r]=_34}}}}
_1.rowHeights=_6;if(_9)this.logInfo("\ntotalWidth: "+_2+", totalHeight: "+_3+"\nspecified sizes:\n"+"cols:"+this.echoAll(_1.colWidths)+", rows: "+this.echoAll(_1.rowHeights),"tablePolicy");_1.$170=_5=isc.Canvas.stretchResizeList(_1.colWidths,_2);_1.$171=_6=isc.Canvas.stretchResizeList(_1.rowHeights,_3);if(_9)this.logInfo("\nderived sizes:\n"+"cols:"+this.echoAll(_1.$170)+", rows: "+this.echoAll(_1.$171),"tablePolicy");for(_14=0;_14<_1.length;_14++){_15=_1[_14];if(!_15.visible)continue;var _41=isc.isA.Canvas(_15),_32=_41?_15.getWidth():_15.width,_42=_41?_15.getHeight():_15.getCellHeight(_7),_19=_15.getTitleOrientation(),_43=_15.$17v,_44=0;if(_15.showTitle){if(_19==isc.Canvas.LEFT){_44=_5[_43[0]]}else{_44=_5[_43[2]]}}
if(_32=="*"){_32=0;var _45=(_15.showTitle&&_19==isc.Canvas.LEFT)?1:0,_46=(_15.showTitle&&_19==isc.Canvas.RIGHT)?1:0,_47=_43[0]+_45,_48=Math.min(_5.length,_43[2]-_46);for(var c=_47;c<_48;c++){_32+=_5[c]}}
if(_15.$17y){_42=0;var _20=_43[1],_21=_43[3];for(var c=_20;c<_21;c++){_42+=_6[c]}}
_15.$172=[_32,_42];_15.$173=_44}}
,isc.A.$17t=function isc_c_Canvas__tableResizePolicyIsValid(_1){if(!_1.$17s)return false;return true}
,isc.A.invalidateTableResizePolicy=function isc_c_Canvas_invalidateTableResizePolicy(_1){delete _1.$17s;delete _1.$171;delete _1.$170}
,isc.A.stretchResizeList=function isc_c_Canvas_stretchResizeList(_1,_2){var _3=0,_4=0,_5=0,_6=_1.duplicate();for(var i=0;i<_1.length;i++){var _8=_6[i];if(isc.isA.Number(_8)){_8=Math.max(_8,1);_5+=_8;_6[i]=_8}else{var _9=_8[2],_10=_8[3];if(_10==0){_3+=_9}
_4+=_10}}
if(_4){var _11=0;if(_3<100){_11=(100-_3)/_4}
for(var r=0;r<_1.length;r++){var _8=_6[r];if(isc.isA.Number(_8))continue;var _9=_8[2],_10=_8[3],_13=_10*_11;if(_9<_13){_8[2]=_13}
if(_10>0)_3+=_8[2]}}
if(_3<=0)return _6;var _14=Math.max(0,_2-_5);for(var r=0;r<_1.length;r++){var _15=Math.max(0,_14/ _3),_8=_6[r];if(isc.isA.Number(_8))continue;var _16=_8[0];if(_16==0)continue;var _17=_8[2],_18=_15*_17;if(_18<_16){_6[r]=_16;_14-=_16;_3-=_17;r=0}}
for(var r=0;r<_1.length;r++){var _15=Math.max(0,_14/ _3),_8=_6[r];if(isc.isA.Number(_8))continue;var _19=_8[1],_17=_8[2],_18=_15*_17;if(_18>_19){_6[r]=_19;_14-=_19;_3-=_17;r=0}}
_15=Math.max(0,_14/ _3);for(var r=0;r<_1.length;r++){_8=_6[r];if(isc.isA.Number(_8))continue;var _17=_8[2];_6[r]=Math.floor(_17*_15)}
return _6}
);isc.B._maxIndex=isc.C+4;isc.ClassFactory.defineClass("ButtonTable",isc.Canvas);isc.A=isc.ButtonTable.getPrototype();isc.A.cellSpacing=0;isc.A.cellPadding=2;isc.A.cellBorder=0;isc.A.tableStyle="menuTable";isc.A.baseButtonStyle="button";isc.A.backgroundColor="CCCCCC";isc.A=isc.ButtonTable.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.getInnerHTML=function isc_ButtonTable_getInnerHTML(){var _1=isc.SB.newInstance();_1.append("<TABLE"," CLASS=",this.tableStyle," WIDTH=",this.getWidth()-(this.overflow==isc.Canvas.SCROLL||this.overflow==isc.Canvas.AUTO?this.getScrollbarSize():0)," HEIGHT=",this.getHeight()," CELLSPACING=",this.cellSpacing," CELLPADDING=",this.cellPadding," BORDER=",this.cellBorder,"><TR>");for(var r=0;r<this.items.length;r++){var _3=this.items[r];_1.append("<TR>");if(!isc.isAn.Array(_3))_3=[_3];for(var i=0;i<_3.length;i++){var _5=_3[i];if(_5.action){_1.append(this.getCellButtonHTML(_5.contents,_5.action,_5.style,_5.disabled,_5.selected,_5.align,_5.extraTagStuff))}else{_1.append(this.getCellHTML(_5.contents,_5.style,_5.align,_5.extraTagStuff))}}
_1.append("</TR>")}
_1.append("</TABLE>");return _1.toString()}
,isc.A.showModal=function isc_ButtonTable_showModal(){this.showClickMask(this.getID()+".hide()");this.show();this.unmask();this.bringToFront()}
,isc.A.hide=function isc_ButtonTable_hide(){this.Super("hide",arguments);this.hideClickMask();this.$174=null}
,isc.A.getButtonBaseStyle=function isc_ButtonTable_getButtonBaseStyle(_1){var _2;if(_1)_2=_1.getAttribute("basestyle");if(!_2)_2=this.baseButtonStyle;return _2}
,isc.A.getMouseOutStyle=function isc_ButtonTable_getMouseOutStyle(_1){var _2=this.getButtonBaseStyle(_1);if(this.buttonIsSelected(_1)){_2+="Selected"}
if(this.buttonIsDisabled(_1)){_2+="Disabled"}
return _2}
,isc.A.buttonIsSelected=function isc_ButtonTable_buttonIsSelected(_1){return _1&&_1.getAttribute("buttonselected")}
,isc.A.buttonIsDisabled=function isc_ButtonTable_buttonIsDisabled(_1){return _1&&_1.getAttribute("buttondisabled")}
,isc.A.cellButtonOver=function isc_ButtonTable_cellButtonOver(_1){var _2=this.getButtonBaseStyle(_1);if(this.buttonIsSelected(_1))_2+="Selected";if(_1)_1.className=_2+"Over"}
,isc.A.cellButtonOut=function isc_ButtonTable_cellButtonOut(_1){if(!_1)return;_1.className=this.getMouseOutStyle(_1)}
,isc.A.cellButtonDown=function isc_ButtonTable_cellButtonDown(_1){if(_1){var _2=this.getButtonBaseStyle(_1);if(this.buttonIsSelected(_1))_2+="Selected";_2+="Down"
_1.className=_2}}
,isc.A.getCellHTML=function isc_ButtonTable_getCellHTML(_1,_2,_3,_4){return isc.StringBuffer.concat("<TD ALIGN=",(_3||isc.Canvas.CENTER)," CLASS=",(_2||this.baseButtonStyle+"Disabled"),(_4||_4),">",_1,"</TD>")}
,isc.A.getCellButtonHTML=function isc_ButtonTable_getCellButtonHTML(_1,_2,_3,_4,_5,_6,_7){if(_3==null)_3=this.baseButtonStyle;var _8=_3;if(_4)_8+="Selected";if(_5)_8+="Disabled";return isc.StringBuffer.concat("<TD ALIGN=",(_6||isc.Canvas.CENTER)," CLASS=",_8," ONMOUSEOVER='",this.getID(),".cellButtonOver(this);return false;' "," ONMOUSEOUT='",this.getID(),".cellButtonOut(this);return true;'"," ONMOUSEDOWN='",this.getID(),".cellButtonDown(this);return true;'"," ONMOUSEUP='",this.getID(),".cellButtonOut(this)';return true;"," basestyle='",_3,"'",(_4?" buttonselected='true'":null),(_5?" buttondisabled='true'":null),(_7?" "+_7:null)," ONCLICK=\""+_2+"\">",_1,"</TD>")}
);isc.B._maxIndex=isc.C+12;isc.ClassFactory.defineClass("DateChooser","ButtonTable");isc.A=isc.DateChooser.getPrototype();isc.A.defaultWidth=150;isc.A.defaultHeight=171;isc.A.showHeader=true;isc.A.headerHeight=20;isc.A.showYearButtons=true;isc.A.showYearChooser=true;isc.A.showMonthButtons=true;isc.A.showMonthChooser=true;isc.A.skinImgDir="images/common/";isc.A.prevYearIcon="[SKIN]doubleArrow_left.gif";isc.A.prevYearIconWidth=14;isc.A.prevYearIconHeight=7;isc.A.prevMonthIcon="[SKIN]arrow_left.gif";isc.A.prevMonthIconWidth=7;isc.A.prevMonthIconHeight=7;isc.A.nextYearIcon="[SKIN]doubleArrow_right.gif";isc.A.nextYearIconWidth=14;isc.A.nextYearIconHeight=7;isc.A.nextMonthIcon="[SKIN]arrow_right.gif";isc.A.nextMonthIconWidth=7;isc.A.nextMonthIconHeight=7;isc.A.showDoubleYearIcon=true;isc.A.yearMenuStyle="dateChooserYearMenu";isc.A.startYear=1995;isc.A.endYear=2015;isc.A.monthMenuStyle="dateChooserMonthMenu";isc.A.showTodayButton=true;isc.A.showCancelButton=false;isc.A.todayButtonTitle="Today";isc.A.cancelButtonTitle="Cancel";isc.A.disableWeekends=false;isc.A.showWeekends=true;isc.A.firstDayOfWeek=0;isc.A.year=new Date().getFullYear();isc.A.month=new Date().getMonth();isc.A.chosenDate=new Date();isc.A.baseButtonStyle="dateChooserButton";isc.A.alternateStyleSuffix="Dark";isc.A.headerStyle="dateChooserButtonDisabled";isc.A.useBackMask=true;isc.A.canFocus=true;isc.A=isc.DateChooser.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.dayNameLength=2;isc.B.push(isc.A.show=function isc_DateChooser_show(){var _1=this.Super("show",arguments);if(this.autoClose){this.showClickMask(this.getID()+".close();",true,this);this.bringToFront()}}
,isc.A.setData=function isc_DateChooser_setData(_1){if(!isc.isA.Date(_1))_1=new Date();this.year=_1.getFullYear();this.month=_1.getMonth();this.chosenDate=_1;this.markForRedraw()}
,isc.A.getData=function isc_DateChooser_getData(){return this.chosenDate}
,isc.A.getInnerHTML=function isc_DateChooser_getInnerHTML(){if(!this.showHeader)this.headerHeight=0;this.baseWeekdayStyle=this.baseWeekdayStyle||this.baseButtonStyle;this.baseWeekendStyle=this.baseWeekendStyle||this.baseWeekdayStyle;var _1=this.baseNavButtonStyle||this.baseButtonStyle;var d=Date.createLogicalDate(this.year,this.month,1),_3=Date.createLogicalDate(this.year,this.month,1),_4=isc.SB.create();if(this.showHeader){_4.append("<TABLE WIDTH=100%"," HEIGHT=",(this.headerHeight+1)," CELLSPACING=",this.cellSpacing," CELLPADDING=",this.cellPadding," BORDER=",this.cellBorder,"><TR>");if(this.showYearButtons){var _5;if(this.showDoubleYearIcon){var _6=this.imgHTML(this.prevMonthIcon,this.prevMonthIconWidth,this.prevMonthIconHeight);_5=_8?"&nbsp;":"<NOBR>"+_6+_6+"<\/NOBR>"}else{_5=_8?"&nbsp;":this.imgHTML(this.prevYearIcon,this.prevYearIconWidth,this.prevYearIconHeight)}
_4.append(this.getCellButtonHTML(_5,this.getID()+".showPrevYear()",_1,null,null,isc.Canvas.CENTER," WIDTH=15"))}
if(this.showMonthButtons){_4.append(this.getCellButtonHTML(this.imgHTML(this.prevMonthIcon,this.prevMonthIconWidth,this.prevMonthIconHeight),this.getID()+".showPrevMonth()",_1,null,null,isc.Canvas.CENTER," WIDTH=15"))}
if(this.showMonthChooser){_4.append(this.getCellButtonHTML(_3.getShortMonthName(),this.getID()+".showMonthMenu()",_1,null,null,isc.Canvas.RIGHT," WIDTH=50%"))}else{_4.append(this.getCellHTML(_3.getShortMonthName(),_1+"Disabled"))}
if(this.showYearChooser){_4.append(this.getCellButtonHTML(_3.getFullYear(),this.getID()+".showYearMenu()",_1,null,null,isc.Canvas.LEFT," WIDTH=50%"))}else{_4.append(this.getCellHTML(_3.getShortFullName(),_1+"Disabled"))}
if(this.showMonthButtons){var _7=(_3.getFullYear()==9999&&_3.getMonth()==11);_4.append(this.getCellButtonHTML(_7?"&nbsp;":this.imgHTML(this.nextMonthIcon,this.nextMonthIconWidth,this.nextMonthIconHeight),_7?"":this.getID()+".showNextMonth()",_1,null,_7?true:null,isc.Canvas.CENTER," WIDTH=15"))}
if(this.showYearButtons){var _8=_3.getFullYear()==9999;var _9;if(this.showDoubleYearIcon){var _6=this.imgHTML(this.nextMonthIcon,this.nextMonthIconWidth,this.nextMonthIconHeight);_9=_8?"&nbsp;":"<NOBR>"+_6+_6+"<\/NOBR>"}else{_9=_8?"&nbsp;":this.imgHTML(this.nextYearIcon,this.nextYearIconWidth,this.nextYearIconHeight)}
_4.append(this.getCellButtonHTML(_9,_8?"":this.getID()+".showNextYear()",_1,null,_8?true:null,isc.Canvas.CENTER," WIDTH=15"))}
_4.append("<\/TR><\/TABLE>")}
_4.append("<TABLE WIDTH=100% HEIGHT=",(this.getHeight()-this.headerHeight)," CELLSPACING=0 CELLPADDING=2 BORDER=",this.cellBorder,">");_4.append("<TR HEIGHT=15>");var _10=this.getDayNames();var _11=Date.getWeekendDays();for(var i=0;i<_10.length;i++){var _13=_11.contains((i+this.firstDayOfWeek)%7)
if(_13&&!this.showWeekends)continue;var _14=(_13&&this.weekendHeaderStyle)?this.weekendHeaderStyle:this.headerStyle;_4.append(this.getCellHTML("<B>"+_10[(i+this.firstDayOfWeek)%7]+"</B>",_14))}
_4.append("<\/TR>");_3.setDate(_3.getDate()
-_3.getDay()+this.firstDayOfWeek
-((_3.getDay()<this.firstDayOfWeek)?7:0));var _15;var _16=false;while(true){if(this.alternateWeekStyles)_16=!_16;_4.append("<TR>");for(var i=0;i<7;i++){var _17=_3.getDay(),_18=_11.contains(_17);if(!(!this.showWeekends&&_18)){var _19=!_18?this.baseWeekdayStyle:this.baseWeekendStyle;if(_16)_19+=this.alternateStyleSuffix;_4.append(this.getDayCellButtonHTML((_15?null:_3),_19))}
if(this.year==9999&&this.month==11&&_3.getDate()==31){_15=true}else{var _20=_3.getDate();_3.setDate(_3.getDate()+1);if(_20==_3.getDate())_3.setDate(_3.getDate()+1)}}
_4.append("<\/TR>");if(_3.getMonth()!=this.month||_15)break}
if(this.showTodayButton||this.showCancelButton){_4.append("<TR");if(this.todayButtonHeight!=null)_4.append(" HEIGHT=",this.todayButtonHeight);_4.append(">");if(this.showTodayButton){var _21=!this.showCancelButton?(this.showWeekends?7:5):(!this.showWeekends?3:4);_4.append(this.getCellButtonHTML(this.todayButtonTitle,this.getID()+".todayClick()",this.baseBottomButtonStyle||this.baseButtonStyle,null,null,isc.Canvas.CENTER," COLSPAN="+_21))}
if(this.showCancelButton){var _21=!this.showTodayButton?(this.showWeekends?7:5):(!this.showWeekends?2:4);_4.append(this.getCellButtonHTML(this.cancelButtonTitle,this.getID()+".cancelClick()",this.baseBottomButtonStyle||this.baseButtonStyle,null,null,isc.Canvas.CENTER," COLSPAN="+_21))}
_4.append("<\/TR>")}
_4.append("<\/TABLE>");return _4.toString()}
,isc.A.getDayNames=function isc_DateChooser_getDayNames(){if(isc.DateChooser.$175==null){isc.DateChooser.$175=[Date.getShortDayNames(1),Date.getShortDayNames(2),Date.getShortDayNames(3)]}
return isc.DateChooser.$175[this.dayNameLength-1]}
,isc.A.getDayCellButtonHTML=function isc_DateChooser_getDayCellButtonHTML(_1,_2,_3){if(_1==null)
return this.getCellButtonHTML("&nbsp;",null,_2,false,false,isc.Canvas.CENTER);var _4=(this.chosenDate&&(Date.compareLogicalDates(_1,this.chosenDate)==0)),_5=(_1.getMonth()!=this.month);var _6=this.getID()+".dateClick("+_1.getFullYear()+","+_1.getMonth()+","+_1.getDate()+");";if(this.disableWeekends&&Date.getWeekendDays().contains(_1.getDay())){_5=true;_6="return false;"}
return this.getCellButtonHTML(_1.getDate(),_6,_2,_4,_5,isc.Canvas.CENTER)}
,isc.A.dateIsSelected=function isc_DateChooser_dateIsSelected(_1){return null}
,isc.A.showPrevMonth=function isc_DateChooser_showPrevMonth(){if(--this.month==-1){this.month=11;this.year--}
this.markForRedraw()}
,isc.A.showNextMonth=function isc_DateChooser_showNextMonth(){if(++this.month==12){this.month=0;this.year++}
this.markForRedraw()}
,isc.A.showMonth=function isc_DateChooser_showMonth(_1){this.month=_1;if(this.monthMenu)this.monthMenu.hide();this.bringToFront();this.markForRedraw()}
,isc.A.showMonthMenu=function isc_DateChooser_showMonthMenu(){if(!this.monthMenu){var _1=[[]],_2=Date.createLogicalDate(2001,0,1);for(var i=0;i<12;i++){_2.setMonth(i);_1[_1.length-1].add({contents:_2.getShortMonthName(),action:this.getID()+".showMonth("+i+")"});if((i+1)%3==0)_1.add([])}
this.monthMenu=isc.ButtonTable.newInstance({styleName:this.monthMenuStyle,left:this.getPageLeft()+5,top:this.getPageTop()+this.headerHeight,width:Math.min(this.getWidth(),120),height:Math.min(this.getHeight()-this.headerHeight,80),items:_1,visibility:isc.Canvas.HIDDEN,baseButtonStyle:this.baseButtonStyle});this.monthMenu.setPageLeft(this.getPageLeft()+((this.width-this.monthMenu.width)/2))}else{var _4=this.getPageTop()+this.headerHeight,_5=Math.min(this.getWidth(),120),_6=Math.min(this.getHeight()-this.headerHeight,80),_7=this.getPageLeft()+((this.width-_5)/2)
this.monthMenu.setPageRect(_7,_4,_5,_6)}
this.monthMenu.showModal()}
,isc.A.showPrevYear=function isc_DateChooser_showPrevYear(){this.year--;this.markForRedraw()}
,isc.A.showNextYear=function isc_DateChooser_showNextYear(){this.year++;this.markForRedraw()}
,isc.A.showYear=function isc_DateChooser_showYear(_1){this.year=_1;if(this.yearMenu)this.yearMenu.hide();this.markForRedraw()}
,isc.A.showYearMenu=function isc_DateChooser_showYearMenu(){var _1=(this.endYear-this.startYear),_2=Math.round(_1/ 10)>3?Math.round(_1/ 10):3;var _3=[[]];for(var i=0;i<=(this.endYear-this.startYear);i++){var _5=i+this.startYear;_3[_3.length-1].add({contents:_5,action:this.getID()+".showYear("+_5+")"});if((i+1)%_2==0)_3.add([])}
if(!this.yearMenu){this.yearMenu=isc.ButtonTable.newInstance({styleName:this.yearMenuStyle,top:this.getPageTop()+this.headerHeight,width:Math.min(this.getWidth(),(40*_2)),height:Math.min(this.getHeight()-this.headerHeight,80),items:_3,visibility:isc.Canvas.HIDDEN,baseButtonStyle:this.baseButtonStyle});this.yearMenu.setPageLeft(this.getPageLeft()+((this.width-this.yearMenu.width)/2))}else{var _6=this.getPageTop()+this.headerHeight,_7=Math.min(this.getWidth(),(40*_2)),_8=Math.min(this.getHeight()-this.headerHeight,80),_9=this.getPageLeft()+((this.width-_7)/2);this.yearMenu.items=_3;this.yearMenu.setPageRect(_9,_6,_7,_8)}
this.yearMenu.showModal()}
,isc.A.dateClick=function isc_DateChooser_dateClick(_1,_2,_3){var _4=this.chosenDate=Date.createLogicalDate(_1,_2,_3);this.month=_2;this.year=_1;this.dataChanged();if(window.dateClickCallback){if(isc.isA.String(window.dateClickCallback)){window.dateClickCallback=new Function("date",window.dateClickCallback)}
window.dateClickCallback(_4)}
if(this.autoHide)this.hide();if(this.autoClose)this.close();if(this.isDrawn())this.markForRedraw();return _4}
,isc.A.dataChanged=function isc_DateChooser_dataChanged(){}
,isc.A.cancelClick=function isc_DateChooser_cancelClick(){this.close()}
,isc.A.todayClick=function isc_DateChooser_todayClick(){this.dateClick(new Date().getFullYear(),new Date().getMonth(),new Date().getDate())}
,isc.A.close=function isc_DateChooser_close(){this.hideClickMask();if(this.yearMenu&&this.yearMenu.isVisible())this.yearMenu.hide();if(this.monthMenu&&this.monthMenu.isVisible())this.monthMenu.hide();if(this.isDrawn())this.clear()}
);isc.B._maxIndex=isc.C+20;isc.A=isc.DateChooser;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.getSharedDateChooser=function isc_c_DateChooser_getSharedDateChooser(_1){if(!this.$176){this.$176=this.create(_1,{_generated:true,autoHide:true,showCancelButton:true});return this.$176}
isc.addProperties(this.$176,_1);return this.$176}
);isc.B._maxIndex=isc.C+1;isc.ClassFactory.defineClass("Slider",isc.Canvas);isc.A=isc.Slider;isc.A.DOWN="down";isc.A.UP="";isc.A.EVENTNAME="sliderMove";isc.A=isc.Slider.getPrototype();isc.A.title="Set Value";isc.A.length=200;isc.A.vertical=true;isc.A.thumbThickWidth=23;isc.A.thumbThinWidth=17;isc.A.trackWidth=7;isc.A.skinImgDir="images/Slider/";isc.A.thumbSrc="thumb.gif";isc.A.trackSrc="track.gif";isc.A.trackCapSize=6;isc.A.trackImageType=isc.Img.STRETCH;isc.A.showTitle=true;isc.A.showRange=true;isc.A.showValue=true;isc.A.labelWidth=50;isc.A.labelHeight=20;isc.A.labelSpacing=5;isc.A.titleStyle="sliderTitle";isc.A.rangeStyle="sliderRange";isc.A.valueStyle="sliderValue";isc.A.value=1;isc.A.minValue=1;isc.A.maxValue=100;isc.A.roundValues=true;isc.A.roundPrecision=1;isc.A.flipValues=false;isc.A.canFocus=true;isc.A.stepPercent=5;isc.A.animateThumbTime=250;isc.A.animateThumbAcceleration="slowStartandEnd";isc.A.valueChangedOnDrag=true;isc.A.valueChangedOnRelease=true;isc.A.valueChangedOnClick=true;isc.A=isc.Slider.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.hValueLabelWidth=5;isc.A.trackConstructor="StretchImg";isc.A.thumbConstructor="Img";isc.B.push(isc.A.initWidget=function isc_Slider_initWidget(){this.Super("initWidget",arguments);if(!(this.minValue<=this.maxValue)){this.logWarn("Slider specified with minValue:"+this.minValue+", greater than maxValue:"+this.maxValue+" - reversing max and min value.");var _1=this.minValue;this.minValue=this.maxValue;this.maxValue=_1}
if(this.minValue!=null)this.minValue=this.$177(this.minValue);if(this.maxValue!=null)this.maxValue=this.$177(this.maxValue);this.setUpSize();this.$178();if(this.showTitle)this._titleLabel=this.addChild(this.$13c());if(this.showRange){this.$128=this.addChild(this.$129("min"));this.$13a=this.addChild(this.$129("max"))}
if(this.showValue){this._valueLabel=this._thumb.addPeer(this.$13b());this._valueLabel.sendToBack();this.$179()}
this.setValue(this.value,!(this.animateThumbInit==true))}
,isc.A.setUpSize=function isc_Slider_setUpSize(){var _1=this.$ts,_2=this.$tt;if(this.vertical){if(_1==null){var _3=Math.max(this.thumbThickWidth,this.trackWidth);if(this.showValue)_3+=this.labelWidth+this.labelSpacing;if(this.showRange)_3+=this.labelWidth+this.labelSpacing;this.logInfo("defaulting width to "+_3+"px");this.setWidth(_3)}
if(_2==null){var _4=this.length;if(this.showTitle)_4+=this.labelHeight+this.labelSpacing;if(this.showValue&&(this.labelHeight>this.thumbThinWidth)){_4+=(this.labelHeight-this.thumbThinWidth)}
this.logInfo("no specified height on vertical Slider - defaulting to:"+_4+" based on slider.length of "+this.length);this.setHeight(_4)}else{this.length=this.getHeight();if(this.showTitle)this.length-=(this.labelHeight+this.labelSpacing);if(this.showValue&&(this.labelHeight>this.thumbThinWidth)){this.length-=(this.labelHeight-this.thumbThinWidth)}
this.logInfo("setting slider track length to:"+this.length+", based on specified height")}}else{if(_2==null){var _4=Math.max(this.thumbThickWidth,this.trackWidth);if(this.showValue)_4+=this.labelHeight+this.labelSpacing;if(this.showRange)_4+=this.labelHeight+this.labelSpacing;this.logInfo("defaulting height to "+_4+"px");this.setHeight(_4)}
if(_1==null){var _3=(this.length+(this.showTitle?this.labelWidth+this.labelSpacing:0));if(this.showValue&&(this.labelWidth>this.thumbThinWidth)){_3+=(this.labelWidth-this.thumbThinWidth)}
this.logInfo("no specified width on horizontal Slider - defaulting to:"+_3+" based on slider.length of "+this.length);this.setWidth(_3)}else{this.length=this.getWidth();if(this.showTitle)this.length-=(this.labelWidth+this.labelSpacing);if(this.showValue&&(this.hValueLabelWidth>this.thumbThinWidth)){this.length-=(this.hValueLabelWidth-this.thumbThinWidth)}
this.logInfo("setting slider track length to:"+this.length+", based on specified width")}}
this.$18a=this.length-this.thumbThinWidth;if(this.numValues&&this.numValues>1){this.$18b=this.$18a/(this.numValues-1)}}
,isc.A.resizeBy=function isc_Slider_resizeBy(_1,_2){this.Super("resizeBy",arguments);if(!this._track)return;var _3=this.vertical;if((_3&&_2!=0)||(!_3&&_1!=0)){this.length+=_3?_2:_1;this.$18a=this.length-this.thumbThinWidth;if(_3)this._track.resizeBy(0,_2)
else this._track.resizeBy(_1,0);if(this.numValues&&this.numValues>1){this.$18b=this.$18a/(this.numValues-1)}
this.setValue(this.value,true,true);if(this.showRange){if(this.vertical){var _4=this.flipValues?this.$13a:this.$128;_4.moveBy(0,_2)}else{var _4=this.flipValues?this.$128:this.$13a;_4.moveBy(_1,0)}}}}
,isc.A.$129=function isc_Slider__createRangeLabel(_1){var _2,_3,_4,_5,_6=(this.vertical?_1=="max":_1=="min");if(this.flipValues)_6=!_6;if(this.vertical){_2=Math.max(this.thumbThickWidth,this.trackWidth)+this.labelSpacing+(this.showValue?this.labelWidth+this.labelSpacing:0);_4=isc.Canvas.LEFT;if(_6){_3=(this.showTitle?this.labelHeight+this.labelSpacing:0);_5=isc.Canvas.TOP}else{_3=(this.showTitle?this.labelHeight+this.labelSpacing:0)+(this.length-this.labelHeight);_5=isc.Canvas.BOTTOM}}else{_3=Math.max(this.thumbThickWidth,this.trackWidth)+this.labelSpacing+(this.showValue?this.labelHeight+this.labelSpacing:0);_5=isc.Canvas.TOP;if(_6){_2=(this.showTitle?this.labelWidth+this.labelSpacing:0);_4=isc.Canvas.LEFT}else{_2=(this.showTitle?this.labelWidth+this.labelSpacing:0)+this.length-this.labelWidth;_4=isc.Canvas.RIGHT}}
return isc.Label.create({ID:this.getID()+"_"+_1+"Label",autoDraw:false,left:_2,top:_3,width:this.labelWidth,height:this.labelHeight,wrap:false,align:_4,valign:_5,className:this.rangeStyle,contents:(_1=="min"?(this.minValueLabel?this.minValueLabel:this.minValue):(this.maxValueLabel?this.maxValueLabel:this.maxValue))})}
,isc.A.$13c=function isc_Slider__createTitleLabel(){var _1=(this.vertical?isc.Canvas.CENTER:isc.Canvas.RIGHT);return isc.Label.create({ID:this.getID()+"_titleLabel",autoDraw:false,left:0,top:0,width:(this.vertical?this.getWidth():this.labelWidth),height:(this.vertical?this.labelHeight:this.getHeight()),align:_1,className:this.titleStyle,contents:this.title})}
,isc.A.$13b=function isc_Slider__createValueLabel(){var _1,_2,_3,_4,_5;if(this.vertical){_1=this._thumb.getLeft()-this.labelWidth-this.labelSpacing;_2=this._thumb.getTop()+parseInt(this._thumb.getHeight()/2-this.labelHeight/ 2);_4=isc.Canvas.RIGHT;_5=isc.Canvas.CENTER;_3=this.labelWidth}else{_1=this._thumb.getLeft()+parseInt(this._thumb.getWidth()/2-this.labelWidth/ 2);_2=this._thumb.getTop()-this.labelHeight-this.labelSpacing;_4=isc.Canvas.CENTER;_5=isc.Canvas.BOTTOM;_3=this.hValueLabelWidth}
var _6=isc.Label.create({ID:this.getID()+"_valueLabel",autoDraw:false,left:_1,top:_2,width:_3,height:this.labelHeight,wrap:false,align:_4,className:this.valueStyle,contents:this.value,mouseUp:function(){return false},moveWithMaster:false,observes:[{source:this,message:"valueChanged",action:"this.$179();"}]});if(!this.vertical){isc.addMethods(_6,{draw:function(){var _7=this.visibility
this.hide();this.Super("draw",arguments);this.parentElement.$179();this.setVisibility(this.prevVis)}})};return _6}
,isc.A.$178=function isc_Slider__createTrackLayout(){var _1=this.$18c(),_2,_3,_4=(this.vertical?this.trackWidth:this.length),_5=(this.vertical?this.length:this.trackWidth),_6,_7,_8=(this.vertical?this.thumbThickWidth:this.thumbThinWidth),_9=(this.vertical?this.thumbThinWidth:this.thumbThickWidth);var _10=this.thumbThickWidth>this.trackWidth;if(_10){if(this.vertical){_6=_1[0];_2=_6+parseInt(this.thumbThickWidth/ 2-this.trackWidth/ 2);_3=_1[1];_7=_1[1]}else{_7=_1[1];_3=_7+parseInt(this.thumbThickWidth/ 2-this.trackWidth/ 2);_2=_1[0];_6=_1[0]}}else{if(this.vertical){_2=_1[0];_6=_2+parseInt(this.trackWidth/ 2-this.thumbThinWidth/ 2);_3=_1[1];_7=_1[1]}else{_3=_1[1];_7=_3+parseInt(this.trackWidth/ 2-this.thumbThinWidth/ 2);_2=_1[0];_6=_1[0]}}
this.logDebug("calculated coords for track:"+[_2,_3,_4,_5]);this.logDebug("calculated coords for thumb:"+[_6,_7,_8,_9]);this._track=this.addChild(this.$18d(_3,_2,_4,_5));this._thumb=this._track.addPeer(this.$18e(_7,_6,_8,_9))}
,isc.A.$18c=function isc_Slider__getTrackLayoutPos(){var _1=this.vertical?(this.showValue?this.labelWidth+this.labelSpacing:0):(this.showTitle?this.labelWidth+this.labelSpacing:0),_2=this.vertical?(this.showTitle?this.labelHeight+this.labelSpacing:0):(this.showValue?this.labelHeight+this.labelSpacing:0);if(this.showValue){if(this.vertical&&(this.labelHeight>this.thumbThinWidth))
_2+=Math.round((this.labelHeight-this.thumbThinWidth)/2);if(this.horizontal&&(this.labelWidth>this.thumbThinWidth))
_1+=Math.round((this.labelWidth-this.thumbThinWidth)/2)}
return[_1,_2]}
,isc.A.$18d=function isc_Slider__createTrack(_1,_2,_3,_4){return this.createAutoChild("track",{left:_2,top:_1,width:_3,height:_4,vertical:this.vertical,capSize:this.trackCapSize,src:"[SKIN]"+(this.vertical?"v":"h")+this.trackSrc,skinImgDir:this.skinImgDir,imageType:this.trackImageType,styleName:this[(this.vertical?"v":"h")+"TrackStyle"],overflow:"hidden",showDisabled:true,canFocus:true,tabIndex:-1,cacheImageSizes:false})}
,isc.A.$18e=function isc_Slider__createThumb(_1,_2,_3,_4){var _5
return this.createAutoChild("thumb",{left:_2,top:_1,width:_3,height:_4,src:"[SKIN]"+(this.vertical?"v":"h")+this.thumbSrc,skinImgDir:this.skinImgDir,overflow:"hidden",showDisabled:true,styleName:this[(this.vertical?"v":"h")+"ThumbStyle"],canDrag:true,dragAppearance:isc.EventHandler.NONE,cursor:isc.Canvas.HAND,dragMove:function(){this.parentElement.$18f();return false},$k6:false,dragStart:function(){var _6=isc.EventHandler;_6.dragOffsetX=-1*(this.getPageLeft()-_6.mouseDownEvent.x);_6.dragOffsetY=-1*(this.getPageTop()-_6.mouseDownEvent.y);this.parentElement.$18g=true;return _6.STOP_BUBBLING},dragStop:function(){this.parentElement.$18g=false;this.setState(isc.Slider.UP);if(this.parentElement.valueChangedOnRelease){this.parentElement.valueChanged(this.parentElement.value)}
return false},mouseDown:function(){this.setState(isc.Slider.DOWN)},mouseUp:function(){this.setState(isc.Slider.UP);return false},canFocus:true,tabIndex:-1})}
,isc.A.$18f=function isc_Slider__thumbMove(_1){var _2,_3;if(this.vertical){var _4=this._track.getTop(),_5=this.$18a+_4;_2=isc.EventHandler.getY()-isc.EventHandler.dragOffsetY-this.getPageTop();_2=Math.max(_4,Math.min(_5,_2));var _6=_2-_4;if(this.numValues){_6=Math.round(_6/ this.$18b)*this.$18b;_2=Math.round(_6)+_4}
if(_2==this._thumb.getTop())return;this.logDebug("drag-moving thumb to:"+_2)
if(_1&&this.animateThumb){this.$18h=this._thumb.animateMove(this._thumb.getLeft(),_2,null,this.animateThumbTime,this.animateThumbAcceleration)}else{this._thumb.setTop(_2)}
_3=(this.flipValues?_6/ this.$18a:1-_6/ this.$18a)}else{var _7=this._track.getLeft(),_5=this.$18a+_7;_2=isc.EventHandler.getX()-isc.EventHandler.dragOffsetX-this.getPageLeft();_2=Math.max(_7,Math.min(_5,_2));var _6=_2-_7;if(this.numValues){_6=Math.round(_6/ this.$18b)*this.$18b;_2=Math.round(_6)+_7}
if(_2==this._thumb.getLeft())return;this.logDebug("drag-moving thumb to:"+_2)
if(_1&&this.animateThumb){this.$18h=this._thumb.animateMove(_2,this._thumb.getTop(),null,this.animateThumbTime,this.animateThumbAcceleration)}else{this._thumb.setLeft(_2)}
_3=(this.flipValues?1-_6/ this.$18a:_6/ this.$18a)}
if(this.maxValue==this.minValue){this.value=this.minValue}else{var _8=_3*(this.maxValue-this.minValue)+this.minValue
_8=this.$177(_8);this.value=_8}
this.logDebug("slider value from drag-move:"+this.value);if(this.valueChangedOnDrag||!this.$18g){this.valueChanged(this.value)}
if(this.sliderTarget)isc.EventHandler.handleEvent(this.sliderTarget,isc.Slider.EVENTNAME,this)}
,isc.A.$177=function isc_Slider__getRoundedValue(_1){if(this.roundValues)_1=Math.round(_1);else if(this.roundPrecision!=null){var _2=Math.pow(10,this.roundPrecision);_1=(Math.round(_1*_2))/_2}
return _1}
,isc.A.$179=function isc_Slider__updateValueLabel(){var _1=this._valueLabel;if(_1==null)return;_1.setContents(this.getValue());var _2=this._thumb;if(this.vertical){_1.setTop(parseInt((_2.getTop()+_2.getHeight()/2)-_1.getHeight()/2))}else{if(_1.isDrawn())_1.redraw("sizing label");var _3=_1.getVisibleWidth(),_4=parseInt((_2.getLeft()+_2.getWidth()/2)-_3/ 2);if(_4+_3>this.getWidth()){_4=this.getWidth()-_3}
if(_4<0)_4=0;_1.setLeft(_4)}}
,isc.A.mouseUp=function isc_Slider_mouseUp(){isc.EventHandler.dragOffsetX=isc.EventHandler.dragOffsetY=Math.floor(this.thumbThinWidth/ 2);if(this.valueChangedOnClick)this.$18f(true)}
,isc.A.setValue=function isc_Slider_setValue(_1,_2,_3){var _4,_5;if(!isc.isA.Number(_1))return;_1=Math.max(this.minValue,(Math.min(_1,this.maxValue)));_1=this.$177(_1);this.value=_1;if(this.minValue==this.maxValue)_4=1;else _4=(this.value-this.minValue)/(this.maxValue-this.minValue);_5=_4*this.$18a;var _6;if(this.vertical){_6=this._track.getTop()+parseInt(this.flipValues?_5:this.$18a-_5);if(this.animateThumb&&!_2){this.$18h=this._thumb.animateMove(this._thumb.getLeft(),_6,null,this.animateThumbTime,this.animateThumbAcceleration)}else{this._thumb.setTop(_6)}}else{_6=this._track.getLeft()+parseInt(this.flipValues?this.$18a-_5:_5);if(this.animateThumb&&!_2){this.$18h=this._thumb.animateMove(_6,this._thumb.getTop(),null,this.animateThumbTime,this.animateThumbAcceleration)}else{this._thumb.setLeft(_6)}}
if(!_3)this.valueChanged(this.value);if(this.sliderTarget)isc.EventHandler.handleEvent(this.sliderTarget,isc.Slider.EVENTNAME,this)}
,isc.A.getValue=function isc_Slider_getValue(){return this.value}
,isc.A.valueChanged=function isc_Slider_valueChanged(_1){}
,isc.A.valueIsChanging=function isc_Slider_valueIsChanging(){return(this.$18g==true)}
,isc.A.handleKeyPress=function isc_Slider_handleKeyPress(_1,_2){var _3=_1.keyName;if(_3=="Home"){this.setValue(this.minValue,true);return false}
if(_3=="End"){this.setValue(this.maxValue,true);return false}
var _4=(this.maxValue-this.minValue)*this.stepPercent/ 100;if(this.roundValues&&_4<1)_4=1;if(this.vertical){if((this.flipValues&&_3=="Arrow_Up")||(!this.flipValues&&_3=="Arrow_Down"))
{this.setValue(this.getValue()-_4,true);return false}else if((this.flipValues&&_3=="Arrow_Down")||(!this.flipValues&&_3=="Arrow_Up"))
{this.setValue(this.getValue()+_4,true);return false}}else{if((this.flipValues&&_3=="Arrow_Left")||(!this.flipValues&&_3=="Arrow_Right"))
{this.setValue(this.getValue()+_4,true)
return false}else if((this.flipValues&&_3=="Arrow_Right")||(!this.flipValues&&_3=="Arrow_Left"))
{this.setValue(this.getValue()-_4,true)
return false}}
if(this.keyPress){this.convertToMethod("keyPress");return this.keyPress(_1,_2)}}
,isc.A.setCanFocus=function isc_Slider_setCanFocus(_1){this.Super("canFocus",arguments);if(this._thumb!=null)this._thumb.setCanFocus(_1);if(this._track!=null)this._track.setCanFocus(_1)}
,isc.A.setMinValue=function isc_Slider_setMinValue(_1){_1=this.$177(_1);this.minValue=_1;if(this.$128)this.$128.setContents(_1);this.setValue(this.minValue)}
,isc.A.setMaxValue=function isc_Slider_setMaxValue(_1){_1=this.$177(_1);this.maxValue=_1;if(this.$13a)this.$13a.setContents(_1);this.setValue(this.minValue)}
,isc.A.setNumValues=function isc_Slider_setNumValues(_1){this.numValues=_1;this.$18b=this.$18a/(this.numValues-1);this.setValue(this.minValue)}
,isc.A.setTitle=function isc_Slider_setTitle(_1){this._titleLabel.setContents(_1)}
,isc.A.setLength=function isc_Slider_setLength(_1){this.length=_1;this.setUpSize()}
,isc.A.$18i=function isc_Slider__refreshChildren(){this._titleLabel.destroy();this._track.destroy();this._thumb.destroy();this._valueLabel.destroy();this.$128.destroy();this.$13a.destroy();this.initWidget()}
,isc.A.setVertical=function isc_Slider_setVertical(_1){this.vertical=_1;this.$18i()}
,isc.A.setThumbThickWidth=function isc_Slider_setThumbThickWidth(_1){this.thumbThickWidth=_1;this.$18i()}
,isc.A.setThumbThinWidth=function isc_Slider_setThumbThinWidth(_1){this.thumbThinWidth=_1;this.$18i()}
,isc.A.setTrackWidth=function isc_Slider_setTrackWidth(_1){this.trackWidth=_1;this.$18i()}
,isc.A.setThumbSrc=function isc_Slider_setThumbSrc(_1){this.thumbSrc=_1;this.$18i()}
,isc.A.setTrackSrc=function isc_Slider_setTrackSrc(_1){this.trackSrc=_1;this.$18i()}
,isc.A.setTrackCapSize=function isc_Slider_setTrackCapSize(_1){this.trackCapSize=_1;this.$18i()}
,isc.A.setTrackImageType=function isc_Slider_setTrackImageType(_1){this.trackImageType=_1;this.$18i()}
,isc.A.setShowTitle=function isc_Slider_setShowTitle(_1){this.showTitle=_1;this.$18i()}
,isc.A.setShowRange=function isc_Slider_setShowRange(_1){this.showRange=_1;this.$18i()}
,isc.A.setShowValue=function isc_Slider_setShowValue(_1){this.showValue=_1;this.$18i()}
,isc.A.setLabelWidth=function isc_Slider_setLabelWidth(_1){this.labelWidth=_1;this.$18i()}
,isc.A.setLabelHeight=function isc_Slider_setLabelHeight(_1){this.labelHeight=_1;this.$18i()}
,isc.A.setLabelSpacing=function isc_Slider_setLabelSpacing(_1){this.labelSpacing=_1;this.$18i()}
,isc.A.setMaxValueLabel=function isc_Slider_setMaxValueLabel(_1){this.$13a.setContents(_1)}
,isc.A.setRoundValues=function isc_Slider_setRoundValues(_1){this.roundValues=_1;this.$18i()}
,isc.A.setRoundPrecision=function isc_Slider_setRoundPrecision(_1){this.roundPrecision=_1;this.$18i()}
,isc.A.setFlipValues=function isc_Slider_setFlipValues(_1){this.flipValues=_1;this.$18i()}
,isc.A.setStepPercent=function isc_Slider_setStepPercent(_1){this.stepPercent=_1;this.$18i()}
);isc.B._maxIndex=isc.C+45;isc.Slider.registerStringMethods({valueChanged:"value"})
if(isc.ListGrid){isc.ClassFactory.defineClass("ScrollingMenu","ListGrid");isc.A=isc.ScrollingMenu.getPrototype();isc.A.useBackMask=true;isc.A.canFocus=true;isc.A.showHeader=false;isc.A.showEdges=false;isc.A.autoDraw=false;isc.A.className="scrollingMenu";isc.A.bodyStyleName="scrollingMenuBody";isc.A.selectionType="single";isc.A.showRollOver=false;isc.A.leaveScrollbarGap=false;isc.A.generateClickOnSpace=false;isc.A.generateDoubleClickOnEnter=false;isc.A.generateClickOnEnter=true;isc.A.showModal=true;isc.A.arrowKeyAction="focus";isc.A.enableSelectOnRowOver=true;isc.A.filterOnKeypress=true;isc.ScrollingMenu.changeDefaults("filterEditorDefaults",{backgroundColor:"white",editorKeyPress:function(_1,_2,_3){if(_2=="Arrow_Down"){this.sourceWidget.$18j(1);return false}
if(_2=="Arrow_Up"){this.sourceWidget.$18j(-1);return false}
if(_2=="Enter"){this.sourceWidget.$18k();return}
return this.Super("editorKeyPress",arguments)}});isc.A=isc.ScrollingMenu.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$18l=true;isc.B.push(isc.A.show=function isc_ScrollingMenu_show(){if(this.showModal)this.showClickMask({target:this,methodName:"cancel"},false,[this]);this.Super("show",arguments);if(this.showModal)this.body.focus()}
,isc.A.recordClick=function isc_ScrollingMenu_recordClick(_1,_2,_3,_4,_5,_6,_7){this.hide();if(_2!=null)this.itemClick(_2)}
,isc.A.itemClick=function isc_ScrollingMenu_itemClick(_1){}
,isc.A.rowOver=function isc_ScrollingMenu_rowOver(_1,_2,_3){if(this.enableSelectOnRowOver)this.selection.selectOnRowOver(_1)}
,isc.A.createSelectionModel=function isc_ScrollingMenu_createSelectionModel(_1,_2,_3,_4,_5){var _6=this.invokeSuper("ScrollingMenu","createSelectionModel",_1,_2,_3,_4,_5);this.selection.addProperties({selectOnRowOver:function(_7){this.selectSingle(_7);this.selectionFromMouse=true},setSelected:function(_7,_8){this.selectionFromMouse=false;return this.Super("setSelected",arguments)}});return _6}
,isc.A.$18m=function isc_ScrollingMenu__hiliteRecord(_1){this.Super("$18m",arguments);this.selection.selectSingle(this.getRecord(_1))}
,isc.A.bodyKeyPress=function isc_ScrollingMenu_bodyKeyPress(_1,_2){var _3=_1.keyName;if(_3==this.$18n){var _4=this.selection;if(_4&&_4.selectionFromMouse){this.cancel();return false}}
if(_3=="Escape"){this.cancel();return false}
return this.Super("bodyKeyPress",arguments)}
,isc.A.cancel=function isc_ScrollingMenu_cancel(){this.hide()}
,isc.A.hide=function isc_ScrollingMenu_hide(){this.hideClickMask();return this.Super("hide",arguments)}
,isc.A.dataChanged=function isc_ScrollingMenu_dataChanged(){var _1=this.Super("dataChanged",arguments);if(!this.$18l)return;if(this.data&&this.data.getLength()>0&&this.selection&&!this.selection.anySelected()&&(isc.isA.ResultSet==null||!isc.isA.ResultSet(this.data)||this.data.rowIsLoaded(0)))
{this.selection.selectItem(0)}
return _1}
);isc.B._maxIndex=isc.C+10}
isc.ClassFactory.defineClass("DynamicForm","Canvas","DataBoundComponent");isc.addGlobal("FormLayout",isc.DynamicForm);isc.A=isc.DynamicForm;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.GET="GET";isc.A.POST="POST";isc.A.NORMAL="normal";isc.A.MULTIPART="multipart";isc.A.NORMAL_ENCODING="application/x-www-form-urlencoded";isc.A.MULTIPART_ENCODING="multipart/form-data";isc.A.$18o="$18o";isc.A.$18p="$18p";isc.A.$18q="$18q";isc.A.$18r="$18s";isc.A.$18t="$18u";isc.A.$18v="$18v";isc.B.push(isc.A.buildOperatorIndex=function isc_c_DynamicForm_buildOperatorIndex(){if(isc.DataSource==null)return;var _1=isc.getValues(isc.DataSource.getSearchOperators());_1=_1.sortByProperties(["symbol"],[false],[function(_4,_5,_6){var _2=_4[_5],_3=isc.isA.String(_2)?_2.length:0;return _3}]);this.$18w=_1.makeIndex("symbol",true)}
,isc.A.getOperatorIndex=function isc_c_DynamicForm_getOperatorIndex(){return this.$18w}
);isc.B._maxIndex=isc.C+2;isc.A=isc.DynamicForm.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.flattenItems=false;isc.A.numCols=2;isc.A.fixedColWidths=false;isc.A.fixedRowHeights=false;isc.A.colWidths=null;isc.A.minColWidth=20;isc.A.cellSpacing=0;isc.A.cellPadding=2;isc.A.cellBorder=0;isc.A.defaultRowHeight=22;isc.A.sectionVisibilityMode="multiple";isc.A.allowContentAndChildren=true;isc.A.separateContentInsertion=true;isc.A.$ww=true;isc.A.fieldIdProperty="name";isc.A.titleField="title";isc.A.showDetailFields=true;isc.A.longTextEditorThreshold=255;isc.A.longTextEditorType="textArea";isc.A.titlePrefix="";isc.A.rightTitlePrefix=":&nbsp;";isc.A.titleSuffix="&nbsp;:";isc.A.rightTitleSuffix="";isc.A.titleWidth=100;isc.A.showInlineErrors=true;isc.A.showErrorIcons=true;isc.A.showErrorText=false;isc.A.showErrorStyle=true;isc.A.errorOrientation="left";isc.A.errorItemDefaults={type:"blurb",wrap:true,showIf:function(){return!this.form.showInlineErrors&&this.form.hasErrors()},defaultDynamicValue:function(_1,_2,_3){return _2.getErrorsHTML(_2.getErrors())}};isc.A.errorItemCellStyle="formCellError";isc.A.errorsPreamble="The following errors were found.";isc.A.hiliteRequiredFields=true;isc.A.requiredTitlePrefix="<b>";isc.A.requiredRightTitlePrefix="<b>:&nbsp;";isc.A.requiredTitleSuffix="&nbsp;:</b>";isc.A.requiredRightTitleSuffix="</b>";isc.A.canHover=false;isc.A.itemHoverDelay=500;isc.A.itemHoverStyle="formHover";isc.A.overflow=isc.Canvas.VISIBLE;isc.A.defaultHeight=20;isc.A.validateOnChange=false;isc.A.implicitSaveDelay=2000;isc.A.synchronousValidation=false;isc.A.autoFocus=false;isc.A.selectOnFocus=false;isc.A.canFocus=true;isc.A._useNativeTabIndex=false;isc.A._useFocusProxy=false;isc.A.uniqueMatch=true;isc.A.browserSpellCheck=true;isc.A.cancelParamName="org.apache.struts.taglib.html.CANCEL";isc.A.cancelParamValue="cancel";isc.A.action="#";isc.A.method=isc.DynamicForm.POST;isc.A.encoding=isc.DynamicForm.NORMAL_ENCODING;isc.A.writeFormTag=true;isc.A.autoSendTargetFieldName="__target__";isc.A.useNativeSelectItems=false;isc.A.hideUsingDisplayNone=isc.Browser.isMoz&&isc.Browser.isMac;isc.A.operator="and";isc.A.nestedEditorType="NestedEditorItem";isc.A.nestedListEditorType="NestedListEditorItem";isc.A.canDropItems=false;isc.A.canAddColumns=true;isc.B.push(isc.A.hasInherentHeight=function isc_DynamicForm_hasInherentHeight(){if(this.inherentHeight!=null)return this.inherentHeight;return(this.overflow==isc.Canvas.VISIBLE||this.overflow==isc.Canvas.CLIP_H)}
);isc.B._maxIndex=isc.C+1;isc.A=isc.DynamicForm.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$18x="upload";isc.A.$18y="mutex";isc.A.$18z=["name","editorType","readOnlyEditorType","type","valueMap","defaultValue","showTitle","left","top","width","height"];isc.A.dataArity="single";isc.A.$refPropName="__ref";isc.A.$3a="/";isc.A.$180="form";isc.A.$181="$182";isc.A.$183="$184";isc.A.$185="absolute";isc.A.$186="showIf";isc.A.$187="item,value,form,values";isc.A.$188="</FORM>";isc.A.$189="tablePolicy";isc.A.$19a="<COL WIDTH=";isc.A.$19b=(isc.Browser.isIE?"<TR STYLE='position:absolute'>":"<TR>");isc.A.$19c=(isc.Browser.isSafari?"</div></TD>":"</TD>");isc.A.$121="<TD>";isc.A.$123="</TD>";isc.A.$12z="<TR>";isc.A.$120="</TR>";isc.A.$19d="<br>";isc.A.$19e="</TABLE></FORM>";isc.A.$19f=["<INPUT TYPE=HIDDEN NAME='",,"' VALUE='",,"'>"];isc.A.titleHeight=15;isc.A.$19g="height:";isc.A.$19h="width:";isc.A.$19i="max-width:";isc.A.$19j="max-height:";isc.A.$19g="height:";isc.A.$19k="<NOBR>";isc.A.$19l="</td></tr></TABLE>";isc.A.$19m="</DIV>";isc.A.$19n="</TD>";isc.A.$19o=["<TD ",," CLASS='",,"' ALIGN='",,"' VALIGN='",,"'",,,">"];isc.A.$19p=["<DIV style='overflow:hidden;text-overflow:ellipsis;",,,,"'>"];isc.A.$19q="$19r";isc.A.$19s=["<FORM ","ID","=",,," METHOD=",," ACTION='",,"' ENCTYPE=",,,,," ONSUBMIT='return ",,".$19t()' ONRESET='",,".resetValues(); return false;'",(isc.Browser.isSafari?" STYLE='display:inline;'":null)," STYLE='margin-bottom:0px;'>"];isc.A.$19u=" STYLE='position:absolute;left:0px;top:0px;'";isc.A.$19v=" TARGET='";isc.A.$19w=["<TABLE role='presentation' ID='",,"' ","WIDTH=",," CELLSPACING=",," CELLPADDING=",," BORDER=",,(isc.Browser.isMoz?"><TBODY>":">")];isc.A.$19x="table";isc.A.formSubmitFailedWarning="Form was unable to be submitted. The most likely cause for this is an "+"invalid value in an upload field.";isc.A.$19y="requiredIf";isc.A.$19z="required";isc.A.$18n="Enter";isc.A.$190={"Arrow_Up":true,"Arrow_Down":true,"Arrow_Right":true,"Arrow_Left":true,"Page_Up":true,"Page_Down":true,"Home":true,"End":true,"Backspace":true,"Delete":true,"Tab":true};isc.B.push(isc.A.initWidget=function isc_DynamicForm_initWidget(){if(isc.$dd)arguments.$de=this;if(!isc.DynamicForm.$18w)isc.DynamicForm.buildOperatorIndex();this.setColWidths(this.colWidths);this.Super("initWidget",arguments);if(this.showComplexFieldsRecursively)this.showComplexFields=true;if(this.fields&&this.items==null)this.items=this.fields;if(this.defaultItems!=null&&this.items==null){this.items=[];for(var i=0;i<this.defaultItems.length;i++){this.items[i]=isc.addProperties({},this.defaultItems[i])}}
if(this.values==null)this.values={};if(this.action!=isc.DynamicForm.getPrototype().action&&this.action!=null&&!isc.isA.emptyString(this.action))
{this.setAction(this.action)}
if(!this.dataSource){var _2=this.items||[];for(var i=0;i<_2.length;i++){if(_2[i]==null)continue;if(this.dataPath||_2[i].dataPath){this.$3s=isc.shallowClone(_2);break}}}
this.setItems(this.items?this.items:[],true);if(this.isDisabled()){this.setDisabled(true)}
this.setErrors(this.errors?this.errors:{});this.setValues(this.values,true);if(this.selectionComponent!=null)this.setSelectionComponent(this.selectionComponent,true);if(isc.isA.String(this.valuesManager))this.valuesManager=window[this.valuesManager]}
,isc.A.$191=function isc_DynamicForm__destroyItems(_1){if(!_1)return;if(!isc.isA.FormItem(_1[0]))return;_1.map("destroy");this.destroyOrphanedItems("containing form destroyed")}
,isc.A.destroy=function isc_DynamicForm_destroy(){if(this.valuesManager&&this.valuesManager.removeMember){this.valuesManager.removeMember(this)}
this.$191(this.items);this.Super("destroy",arguments)}
,isc.A.setHandleDisabled=function isc_DynamicForm_setHandleDisabled(_1){if(this.isDrawn()){if(this.redrawOnDisable)this.markForRedraw("setDisabled");this.disableKeyboardEvents(_1,null,true)}
var _2=this.getItems();for(var i=0;i<_2.length;i++){_2[i].updateDisabled()}}
,isc.A.disableKeyboardEvents=function isc_DynamicForm_disableKeyboardEvents(_1,_2,_3){this.Super("disableKeyboardEvents",arguments);if(!_3){if(_1){this.$192=this.getTabIndex();this.$0m(-1)}else{this.$0m(this.$192)}}}
,isc.A.applyFieldDefaults=function isc_DynamicForm_applyFieldDefaults(_1){if(_1==null)return;for(var i=0;i<_1.length;i++){var _3=_1[i];if(_3==null)return}}
,isc.A.getEditorType=function isc_DynamicForm_getEditorType(_1){return this.getClass().getEditorType(_1,this)}
,isc.A.setItems=function isc_DynamicForm_setItems(_1,_2){if(_1!=null){for(var i=0;i<_1.length;i++){var _4=false;if(_1[i]==null){this.logWarn("Encountered empty entry in items array - removing this entry.")
_4=true}
if(isc.isA.Canvas(_1[i])){this.logWarn("Encountered a Canvas instance:"+_1[i]+" in the items "+"array - the DynamicForm items array should contain only FormItem "+"definitions. Removing this entry.");_4=true}
if(_4){_1.removeAt(i);i-=1}}}
_1=this.bindToDataSource(_1);if(!_1)_1=[];else if(_1==this.items)_1=_1.duplicate();if(this.items!=null&&this.items.length>0&&!_2)this.removeItems(this.items);this.$193(_1,null,true,_2)}
,isc.A.setFields=function isc_DynamicForm_setFields(_1){this.setItems(_1)}
,isc.A.getFields=function isc_DynamicForm_getFields(){return this.items}
,isc.A.getItems=function isc_DynamicForm_getItems(){return this.items}
,isc.A.visibleAtPoint=function isc_DynamicForm_visibleAtPoint(_1,_2,_3,_4){if(this.invokeSuper(isc.DynamicForm,"visibleAtPoint",_1,_2,_3,_4))
return true;var _5=this.items||[],_6={},_7=_5.indexOf(this.getFocusSubItem());for(var i=-1;i<_5.length;i++){var _9=i;if(i==-1){_9=_7}else if(_9==_7)continue;if(_9==-1)continue;var _10=_5[_9],_11=_10.containerWidget;if(_11==this||!_10.isDrawn()||!_10.isVisible())continue;var _12=_11.getID();if(_6[_12]==null){_6[_12]=_11.visibleAtPoint(_1,_2,_3,_4)}
if(!_6[_12])continue;var _13=_10.getPageLeft(),_14=_10.getPageTop();if(_13<=_1&&(_13+_10.getVisibleWidth())>=_1&&_14<=_2&&(_14+_10.getVisibleHeight())>=_2){return true}}
return false}
,isc.A.addItems=function isc_DynamicForm_addItems(_1,_2){if(!isc.isAn.Array(_1))_1=[_1];if(this.dataSource){var _3=isc.DS.get(this.dataSource);for(var i=0;i<_1.length;i++){_1[i]=this.combineFieldData(_1[i]);var _5=_1[i].name;if(_5&&this.getItem(_5)){this.removeItem(_5)}}}
this.addFieldValidators(_1);if(_2==null||_2>this.items.length)_2=this.items.length;this.$193(_1,_2)}
,isc.A.$193=function isc_DynamicForm__addItems(_1,_2,_3,_4){var _5=this.isDrawn(),_6=_5?this.getTabIndexSpan():null;this.applyFieldDefaults(_1);var _7=[];var _8=false,_9=false,_10=(this.sectionVisibilityMode==this.$18y);for(var _11=0;_11<_1.length;_11++){var _12=_1[_11];if(!_12){_1.removeItem(_11);_11--;continue}
var _13=isc.DynamicForm.canEditField(_12,this);if(!_13&&_12.readOnlyEditorProperties){_12=isc.addProperties({},_12,_12.readOnlyEditorProperties)}else if(_12.editorProperties){_12=isc.addProperties({},_12,_12.editorProperties)}
var _14=this.getEditorType(_12);_1[_11]=_12=this.createItem(_12,_14);if(_14==this.$18x)_8=true;if(isc.FileItem&&isc.isA.FileItem(_12)&&_9){this.logWarn("Attempting to creating a form with multiple FileItems. This is "+"not currently supported - only the first file type field value will "+"be committed on submission of this form.")}
if(isc.isA.SectionItem(_12)){_7.add(_12);if(_12.sectionExpanded&&_10)
this.$133=_12}}
if(_3)this.items=_1
else this.items.addListAt(_1,_2);if(!_4){this.setItemValues(this.getValues(),false,true,_1)}
if(_8)this.encoding=isc.DynamicForm.MULTIPART_ENCODING;for(var i=0;i<_7.length;i++){var _16=_7[i],_17=_16.sectionExpanded;if(_17&&(!_10||(this.$133==_16))){_16.expandSection()}else{_16.collapseSection()}}
this.$194=true;var _18=this.getTabIndex();if(_5&&_18!=-1){this.$195();var _19=this.getTabIndexSpan();if(_19>_6){var _20=this.$00();if(_20){var _21=_20.getTabIndex();if(_21<(_18+_19)){_20.$05((_18+_19)-_21)}}}}
this.markForRedraw("Form items added")}
,isc.A.copyKnownProperties=function isc_DynamicForm_copyKnownProperties(_1,_2,_3){var _4;for(var i=0;i<_3.length;i++){var _6=_3[i],_7=_2[_6];if(_7!==_4){_1[_6]=_7;delete _2[_6]}}}
,isc.A.createItem=function isc_DynamicForm_createItem(_1,_2){if(_1.form!=null&&!(_1.form==this.getID()||_1.form!=this)){this.logWarn("Unsupported 'form' property ["+_1.form+"] set on item:"+_1+".  Ignoring.")}
if(_1.destroyed&&isc.isA.FormItem(_1)){this.logWarn("destroyed FormItem passed to setItems()/addItem(): FormItems cannot be "+"re-used with different DynamicForms")}
var _3=isc.FormItemFactory.getItemClassName(_1,_2,this),_4=isc.FormItemFactory.getItemClass(_3);if(!_4){this.logWarn("Problem initializing item: "+isc.Log.echo(_1)+" - derived FormItem class is: "+_3+".  Please make sure the relevant module is loaded");return}
var _5=_1;_1=_4.createRaw();_1.form=_1.containerWidget=_1.eventParent=this;var _6=null;if(_1["validators"]!=null&&_5["validators"]!=null){_6=_1.validators}
if(isc.Browser.isIE&&this.canAlterItems){this.copyKnownProperties(_1,_5,this.$18z)}
if(this.autoChildItems){if(_1.ID==null)_1.ID=null;this.$eq(_4.Class,_1,_5)}else{_1.completeCreation(_5);if(_6!=null){if(!_1.validators){_1.validators=_6}else{if(!isc.isAn.Array(_1.validators)){_1.validators=[_1.validators]}
if(_1.validators.$3v){_1.validators=_1.validators.duplicate()}
_1.validators.addList(_6)}}}
_1.form=this;if(_1.destroyed)_1.destroyed=false;if(_1.shouldSaveValue&&(_1[this.fieldIdProperty]==null||isc.isAn.emptyString(_1[this.fieldIdProperty]))&&(_1.dataPath==null||isc.isAn.emptyString(_1.dataPath)))
{this.logWarn(_1.getClass()+" form item defined with no '"+this.fieldIdProperty+"' property - Value will not be saved."+" To explicitly exclude a form item from the set of values to "+"be saved, set 'shouldSaveValue' to false for this item.")
_1.shouldSaveValue=false}
return _1}
,isc.A.removeItems=function isc_DynamicForm_removeItems(_1){if(_1==null)return;if(!isc.isAn.Array(_1))_1=[_1];if(_1==this.items)_1=this.items.duplicate();_1=this.map("getItem",_1);this.items.removeList(_1);if(this.$196==null){this.$196=[]}
for(var i=0;i<_1.length;i++){var _3=_1[i];if(_3==null)continue;if(_3.items!=null){_1.addList(_3.items,i+1)}
if(this.$197==_3)delete this.$197;if(!this.items.contains(_3)&&isc.isA.FormItem(_3)){if(this.isDrawn()){this.$196.add(_3)}else{_3.destroy()}}}
this.$194=true;this.markForRedraw("Form items removed")}
,isc.A.addField=function isc_DynamicForm_addField(_1,_2){this.addItems(_1,_2)}
,isc.A.removeField=function isc_DynamicForm_removeField(_1){this.removeItems(_1)}
,isc.A.addItem=function isc_DynamicForm_addItem(_1,_2){this.addItems(_1,_2)}
,isc.A.removeItem=function isc_DynamicForm_removeItem(_1){this.removeItems(_1)}
,isc.A.addFields=function isc_DynamicForm_addFields(_1,_2){return this.addItems(_1,_2)}
,isc.A.removeFields=function isc_DynamicForm_removeFields(_1){return this.removeItems(_1)}
,isc.A.$mb=function isc_DynamicForm__canFocus(_1,_2,_3,_4){if(this.canFocus==true)return true;var _5=this.getItems();for(var i=0;i<_5.length;i++){if(_5[i].$mb())return true}
return this.invokeSuper(isc.DynamicForm,"$mb",_1,_2,_3,_4)}
,isc.A.$195=function isc_DynamicForm__assignTabIndices(){var _1=this.items;if(!_1||_1.length==0)return;var _2=[],_3={};for(var i=0;i<_1.length;i++){var _5=_1[i],_6=_5.tabIndex;if(_6!=null&&_6!=-1){if(_2[_6]!=null&&!_3[_6]){this.logWarn("More than one item in this form have an explicitly specified tabIndex of '"+_6+"'. Tab order cannot be guaranteed within this form.");_3[_6]=true}
_2[_6]=_5}}
var _7=1;for(var i=0;i<_1.length;i++){var _5=_1[i];if(!isc.isA.FormItem(_5)){if(this.logIsDebugEnabled())
this.logDebug("$195() fired before all form items have been initialized"+this.getStackTrace());continue}
if(!_5.$mb()||_5.tabIndex!=null||_5.globalTabIndex!=null){continue}
_7+=1;while(_2[_7]!=null){_7+=1}
_5.$198=_7;if(isc.isA.CanvasItem(_5)){var _8=_5.canvas;if(_8&&_8.getTabIndexSpan){_7+=_8.getTabIndexSpan()}}}}
,isc.A.$04=function isc_DynamicForm__slotChildrenIntoTabOrder(){return}
,isc.A.getTabIndexSpan=function isc_DynamicForm_getTabIndexSpan(){var _1=this.items;var _2=1;if(!_1){return _2}
for(var i=0;i<_1.length;i++){var _4=_1[i];if(!isc.isA.FormItem(_4)){return _1.length}
if(!_4.$mb()||_4.globalTabIndex!=null){continue}
var _5=_4.tabIndex||_4.$198;if(_5==null){this.$195();_5=_4.$198}
if(isc.isA.CanvasItem(_4)){var _6=_4.canvas,_7=0;if(_6&&_6.getTabIndexSpan)_7=_6.getTabIndexSpan();if(_7>1){_5+=_7-1}}
if(_5!=null&&_5>_2)_2=_5}
return _2}
,isc.A.$0m=function isc_DynamicForm__setTabIndex(){this.Super("$0m",arguments);if(this.isDrawn()&&this.isVisible()&&this.items){for(var i=0;i<this.items.length;i++){this.items[i].updateTabIndex()}}}
,isc.A.handleMoved=function isc_DynamicForm_handleMoved(_1,_2,_3,_4){this.invokeSuper(isc.DynamicForm,"handleMoved",_1,_2,_3,_4);this.itemsMoved()}
,isc.A.handleParentMoved=function isc_DynamicForm_handleParentMoved(_1,_2,_3,_4){this.invokeSuper(isc.DynamicForm,"handleParentMoved",_1,_2,_3,_4);this.itemsMoved()}
,isc.A.zIndexChanged=function isc_DynamicForm_zIndexChanged(_1,_2,_3,_4){this.invokeSuper(isc.DynamicForm,"zIndexChanged",_1,_2,_3,_4);this.itemsZIndexChanged()}
,isc.A.parentZIndexChanged=function isc_DynamicForm_parentZIndexChanged(_1,_2,_3,_4){this.invokeSuper(isc.DynamicForm,"parentZIndexChanged",_1,_2,_3,_4);this.itemsZIndexChanged()}
,isc.A.itemsMoved=function isc_DynamicForm_itemsMoved(){var _1=this.getItems();if(!_1)return;for(var i=0;i<_1.length;i++){if(_1[i].isVisible)_1[i].moved()}}
,isc.A.itemsVisibilityChanged=function isc_DynamicForm_itemsVisibilityChanged(){var _1=this.getItems();if(!_1)return;for(var i=0;i<_1.length;i++){if(_1[i].visibilityChanged)_1[i].visibilityChanged()}}
,isc.A.itemsZIndexChanged=function isc_DynamicForm_itemsZIndexChanged(){var _1=this.getItems();if(!_1)return;for(var i=0;i<_1.length;i++){_1[i].zIndexChanged()}}
,isc.A.scrollTo=function isc_DynamicForm_scrollTo(_1,_2,_3){var _4=this.getScrollLeft(),_5=this.getScrollTop();this.Super("scrollTo",arguments);if(_4!=this.getScrollLeft()||_5!=this.getScrollTop())this.itemsMoved()}
,isc.A.$71=function isc_DynamicForm__canAnimateClip(){if(this.canAnimateClip!=null)return this.canAnimateClip;return true}
,isc.A.setTitleOrientation=function isc_DynamicForm_setTitleOrientation(_1){this.titleOrientation=_1;this.$194=true;this.markForRedraw()}
,isc.A.setNumCols=function isc_DynamicForm_setNumCols(_1){this.numCols=_1;this.$194=true;this.markForRedraw()}
,isc.A.setAutoComplete=function isc_DynamicForm_setAutoComplete(_1){this.autoComplete=_1;for(var i=0;i<this.items.length;i++){this.items[i].$199()}}
,isc.A.setValues=function isc_DynamicForm_setValues(_1,_2){delete this.$20a;if(isc.isAn.Array(_1)){var _3=isc.isA.Object(_1[0]);this.logWarn("values specified as an array."+(_3?" Treating the first item in the array as intended values.":" Ignoring specified values (resetting to defaults)."));if(_3)_1=_1[0];else _1=null}
if(_1==null)_1={};this.$20b(_1);var _4=this.items;for(var i=0;i<_4.length;i++){if(_4[i].shouldSaveValue&&this.$4t(_4[i])){_4[i].$20c()}}
this.setItemValues(_1,null,_2);this.rememberValues();if(this.rulesEngine!=null){this.rulesEngine.processEditStart(_1)}
this.markForRedraw("setValues")}
,isc.A.$4t=function isc_DynamicForm__useDisplayFieldValue(_1){if(!_1||!_1.displayField)return false;if(_1.optionDataSource!=null)return false;if(_1.getValueFieldName()!=_1.getFieldName())return false;return true}
,isc.A.setData=function isc_DynamicForm_setData(_1){this.setValues(_1)}
,isc.A.setDataSource=function isc_DynamicForm_setDataSource(_1,_2){this.Super("setDataSource",arguments);this.clearErrors()}
,isc.A.rememberValues=function isc_DynamicForm_rememberValues(){var _1=this.getValues(),_2=this.$20d={};this.$20e=[];var _3="__ref";var _4=[],_5={},_6=this.getItems()||[];for(var i=0;i<_6.length;i++){if(_6[i].dataPath!=null){_4[_4.length]=_6[i].dataPath.split("/")}}
if(_4.length>0){for(var i=0;i<_4.length;i++){var _8=_1,_9=_2,_10=_4[i];_5[_10[0]]=true;for(var _11=0;_11<_10.length;_11++){var _12=_10[_11];if(_8[_12]!=null){var _13=_8[_12],_14=_9[_12],_15=isc.isAn.Object(_13);if(_15&&(_14==null||_14==_13)){if(isc.isA.Date(_14)){_9[_12]=new Date(_14.getTime())}else if(isc.isAn.Array(_14)){_9[_12]=_14.duplicate()}else{_9[_12]=isc.addProperties({},_13)}}else if(_14==null){_9[_12]=_13}
_8=_8[_12];_9=_9[_12]}else{break}}}}
for(var _16 in _1){if(isc.isA.Function(_1[_16]))continue;if(_16==_3)continue;if(_5[_16]==true)continue;if(isc.isA.Date(_1[_16])){_2[_16]=new Date();_2[_16].setTime(_1[_16].getTime())}else{_2[_16]=_1[_16]}
var _17=this.getItem(_16);if(_17&&_17.isSetToDefaultValue())this.$20e.add(_16)}
return _2}
,isc.A.resetValues=function isc_DynamicForm_resetValues(){this.clearErrors();var _1={};for(var _2 in this.$20d){if(this.$20e.contains(_2))continue;if(isc.isA.Date(_1[_2])&&isc.isA.Date(this.$20d[_2]))
_1[_2].setTime(this.$20d[_2].getTime());else
_1[_2]=this.$20d[_2]}
this.setValues(_1)}
,isc.A.clearValues=function isc_DynamicForm_clearValues(){this.setValues();var _1=this.getItems();for(var i=0;i<_1.length;i++){if(_1[i].shouldSaveValue==false)_1[i].setValue(null)}
this.clearErrors();this.rememberValues();this.markForRedraw("clearValues")}
,isc.A.valuesHaveChanged=function isc_DynamicForm_valuesHaveChanged(_1,_2,_3){if(_2==null)_2=this.getValues();if(_3==null)_3=this.$20d||{};return isc.DynamicForm.valuesHaveChanged(this,_1,_2,_3)}
,isc.A.getOldValues=function isc_DynamicForm_getOldValues(){var _1={};isc.addProperties(_1,this.$20d);return _1}
,isc.A.getOldValue=function isc_DynamicForm_getOldValue(_1){return this.getOldValues()[_1]}
,isc.A.getChangedValues=function isc_DynamicForm_getChangedValues(){return this.valuesHaveChanged(true)}
,isc.A.getValues=function isc_DynamicForm_getValues(){this.updateFocusItemValue();return this.values}
,isc.A.updateFocusItemValue=function isc_DynamicForm_updateFocusItemValue(){var _1=this.getFocusSubItem();if(!this.$20f&&_1!=null&&_1.$20g()){_1.updateValue()}}
,isc.A.getData=function isc_DynamicForm_getData(){return this.getValues()}
,isc.A.getValuesAsCriteria=function isc_DynamicForm_getValuesAsCriteria(_1,_2,_3){if(_1==null){_1=(this.operator!="and")||this.getItems().map("hasAdvancedCriteria").contains(true)||this.allowExpressions||(this.$20a!=null)}
if(!_1){var _4=this.$20h();if(_3)return _4;return isc.DataSource.filterCriteriaForFormValues(_4)}
var _5=this.$20a?isc.clone(this.$20a):{operator:this.operator,_constructor:"AdvancedCriteria",criteria:[]};var _6=this.$20h(true,_2);_6.removeEmpty();if(_6&&_6.length>0)_5.criteria.addList(_6);return isc.DS.checkEmptyCriteria(_5)}
,isc.A.$20h=function isc_DynamicForm__getMappedCriteriaValues(_1,_2){var _3=isc.addProperties({},this.getValues()),_4={},_5=[];var _6=this.getFields();for(var i=0;i<_6.length;i++){if(!_6[i].shouldSaveValue)continue;var _8=_6[i],_9=_6[i].getFieldName(),_10=_6[i].getCriteriaFieldName();delete _3[_9];if(!_1){if(_10!=null){_4[_10]=_6[i].getCriteriaValue()}}else{var _11=_8.getCriterion(_2);if(_11!=null)_5.add(_11)}}
if(!_1){return isc.addProperties(_3,_4)}else{for(var _12 in _3){if(_5.find("fieldName",_12))continue;if(_3[_12]==null)continue;_5.add({operator:isc.DataSource.getCriteriaOperator(_3[_12],_2),fieldName:_12,value:_3[_12]})}
return _5}}
,isc.A.getFilterCriteria=function isc_DynamicForm_getFilterCriteria(){return this.getValuesAsCriteria()}
,isc.A.setValuesAsCriteria=function isc_DynamicForm_setValuesAsCriteria(_1,_2){this.setValues({});if(!_2&&!isc.DataSource.isAdvancedCriteria(_1)){this.$20b(_1);var _3=this.items||[];for(var i=0;i<_3.length;i++){for(var _5 in _1){if(_3[i].canEditSimpleCriterion(_5)){_3[i].setSimpleCriterion(_1[_5],_5);break}}}
this.rememberValues()}else{_1=isc.clone(_1);var _6=_1.operator;if(_6!=this.operator){this.logWarn("Dynamic Form editing advanced criteria object:"+isc.Comm.serialize(_1)+". Form level operator specified as '"+this.operator+"' - Criteria returned from this form will be nested in an outer "+this.operator+" clause.");_1._constructor=null;_1={_constructor:"AdvancedCriteria",operator:this.operator,criteria:[_1]}}
var _3=this.getItems(),_7=_1.criteria,_8={};for(var i=0;i<_7.length;i++){for(var _9=0;_9<_3.length;_9++){if(!_3[_9].shouldSaveValue)continue;var _10=_3[_9];if(this.shouldApplyCriterionToItem(_3[_9],_7[i])){var _11=_3[_9].getID();if(_8[_11]==null){_8[_11]=_7[i]}else{var _12=_8[_11];var _13=isc.DataSource.combineCriteria(_12,_7[i],this.operator,null,true);if(!_10.canEditCriterion(_13)){this.logInfo("setValuesAsCriteria(): criteria include:"+this.echoFull(_12)+" and "+this.echoFull(_7[i])+". Both of these "+"could be applied to item:"+_10+". However, the item is unable to edit a composite criterion "+"resulting from combining these criteria. Therefore "+this.echoFull(_7[i])+" will not be applied to this item","AdvancedCriteria");continue}else{this.logDebug("setValuesAsCriteria(): Combined multiple criteria into "+"composite criterion:"+this.echoFull(_13)+" and assigned to item:"+_10,"AdvancedCriteria");_8[_11]=_13}}
_7[i]=null;break}}}
_7.removeEmpty();for(var _11 in _8){var _10=window[_11];_10.setCriterion(_8[_11])}
this.$20a=_1}}
,isc.A.shouldApplyCriterionToItem=function isc_DynamicForm_shouldApplyCriterionToItem(_1,_2){if(_1.canEditCriterion(_2))return true;if(_2.fieldName!=null&&_2.fieldName==_1.getCriteriaFieldName()){this.logInfo("Editing AdvancedCriteria in a dynamicForm. Criteria "+"includes a value for field:"+_2.fieldName+". This form includes an item "+_1+" with the same fieldName"+" but the specified operator '"+_2.operator+"' does not match the operator for this form item:"+_1.getOperator()+". Original criterion will be retained and combined with any "+"criterion returned from this item.","AdvancedCriteria")}
return false}
,isc.A.getValuesAsAdvancedCriteria=function isc_DynamicForm_getValuesAsAdvancedCriteria(_1,_2){return this.getValuesAsCriteria(true,_1,_2)}
,isc.A.getItem=function isc_DynamicForm_getItem(_1){if(isc.isA.FormItem(_1))return _1;var _2=isc.Class.getArrayItem(_1,this.items,this.fieldIdProperty);if(_2!=null)return _2;for(var i=0;i<this.items.length;i++){var _4=this.items[i].dataPath;if(_4==_1)return this.items[i];if(this.dataPath){if(this.dataPath.endsWith("/")){if(this.dataPath+_4==_1)return this.items[i]}else if(this.dataPath+"/"+_4==_1)return this.items[i]}}
if(isc.isA.Number(_1-1)){return this.items[parseInt(_1)]}
return null}
,isc.A.getField=function isc_DynamicForm_getField(_1){return this.getItem(_1)}
,isc.A.getSubItem=function isc_DynamicForm_getSubItem(_1){return this.getItem(_1)}
,isc.A.getItemById=function isc_DynamicForm_getItemById(_1){var _2;if(isc.isA.String(_1)){_2=window[_1]}else _2=_1;if(isc.isA.FormItem(_2))return _2;return null}
,isc.A.getValue=function isc_DynamicForm_getValue(_1){var _2=this.getItem(_1);if(_2&&isc.isA.Function(_2.getValue))return _2.getValue();return this.$20i(_1)}
,isc.A.$20i=function isc_DynamicForm__getValue(_1){return isc.DynamicForm.$3e(_1,this.values)}
,isc.A.setValue=function isc_DynamicForm_setValue(_1,_2){var _3=this.getItem(_1);if(_3!=null)return _3.setValue(_2);else if(this.values!=null){this.$20j(_1,_2);return _2}}
,isc.A.clearValue=function isc_DynamicForm_clearValue(_1){var _2=this.getItem(_1);if(_2!=null)_2.clearValue();else if(this.values)isc.DynamicForm.$3c(_1,this.values)}
,isc.A.showItem=function isc_DynamicForm_showItem(_1){var _2=this.getItem(_1);if(_2!=null)return _2.show()}
,isc.A.hideItem=function isc_DynamicForm_hideItem(_1){var _2=this.getItem(_1);if(_2!=null)return _2.hide()}
,isc.A.saveItemValue=function isc_DynamicForm_saveItemValue(_1,_2){if(_1.shouldSaveValue==false)return;var _3=isc.DynamicForm.$3f(this.dataPath,_1.getDataPath()||_1.getFieldName());if(_3!=null){this.$20j(_3,_2)}else{return}
if(this.$4t(_1)&&(_1.displayField!=_3)){var _4=_1.mapValueToDisplay(_2);this.setValue(_1.displayField,_4)}
_1.$20k()}
,isc.A.$20j=function isc_DynamicForm__saveValue(_1,_2){var _3;if(isc.ValuesManager&&isc.isA.ValuesManager(this.valuesManager)){var _4=this.valuesManager.getField(_1);if(_4)_3=_4.name}
isc.DynamicForm.$3d(_1,_2,this.values,this,true,false,_3);var _5=this.selectionComponent;if(!_5&&this.valuesManager!=null){if(isc.isA.ValuesManager(this.valuesManager)&&this.valuesManager.members&&this.valuesManager.members.contains(this))
{this.valuesManager.$20l(_1,_2,this)}}}
,isc.A.clearItemValue=function isc_DynamicForm_clearItemValue(_1){var _2=isc.DynamicForm.$3f(this.dataPath,_1.getDataPath()||_1.getFieldName());isc.DynamicForm.$3c(_2,this.values);if(!this.selectionComponent&&isc.isA.ValuesManager(this.valuesManager)&&this.valuesManager.members&&this.valuesManager.members.contains(this))
{this.valuesManager.$20m(_2,this)}}
,isc.A.$20b=function isc_DynamicForm__saveValues(_1){this.values=_1;if(!this.selectionComponent&&isc.isA.ValuesManager(this.valuesManager)&&this.valuesManager.members&&this.valuesManager.members.contains(this))
{var _2=isc.getKeys(this.values);for(var i in _1){this.valuesManager.$20l(i,_1[i],this);_2.remove(i)}
for(var i=0;i<_2.length;i++){this.valuesManager.$20m(_2[i],this)}}}
,isc.A.getSavedItemValue=function isc_DynamicForm_getSavedItemValue(_1){if(_1.shouldSaveValue==false)return null;var _2=isc.DynamicForm.$3f(this.dataPath,_1.getDataPath())||_1.getFieldName();return this.$20i(_2)}
,isc.A.resetValue=function isc_DynamicForm_resetValue(_1){var _2=this.getItem(_1);return(_2?_2.resetValue():null)}
,isc.A.getValueMap=function isc_DynamicForm_getValueMap(_1){var _2=this.getItem(_1);return(_2?_2.getValueMap():null)}
,isc.A.setValueMap=function isc_DynamicForm_setValueMap(_1,_2){var _3=this.getItem(_1);return(_3?_3.setValueMap(_2):null)}
,isc.A.getOptions=function isc_DynamicForm_getOptions(_1){return this.getValueMap(_1)}
,isc.A.setOptions=function isc_DynamicForm_setOptions(_1,_2){return this.setValueMap(_1,_2)}
,isc.A.getForm=function isc_DynamicForm_getForm(_1){var _2=(_1==null?[this.getFormID()]:arguments);return this.Super("getForm",_2)}
,isc.A.getFormID=function isc_DynamicForm_getFormID(){return this.$t4(this.$180)}
,isc.A.getSerializeableFields=function isc_DynamicForm_getSerializeableFields(_1,_2){_1.addList(["items"]);return this.Super("getSerializeableFields",arguments)}
,isc.A.expandSection=function isc_DynamicForm_expandSection(_1){var _2=this.getItem(_1);if(isc.isA.SectionItem(_2))_2.expandSection()}
,isc.A.collapseSection=function isc_DynamicForm_collapseSection(_1){var _2=this.getItem(_1);if(isc.isA.SectionItem(_2))_2.collapseSection()}
,isc.A.$20n=function isc_DynamicForm__sectionExpanding(_1){if(this.isDrawn()){this.blur();this.$20o=this.notifyAncestorsOnReflow;this.notifyAncestorsOnReflow=true}
if(this.sectionVisibilityMode=="mutex"&&this.$133&&this.$133!=_1)
{this.$133.collapseSection()}
this.$133=_1}
,isc.A.$20p=function isc_DynamicForm__sectionCollapsing(_1){if(this.isDrawn()){this.blur();this.$20o=this.notifyAncestorsOnReflow;this.notifyAncestorsOnReflow=true}}
,isc.A.getErrors=function isc_DynamicForm_getErrors(){return this.errors}
,isc.A.getFieldErrors=function isc_DynamicForm_getFieldErrors(_1){if(!this.errors)return null;var _2;if(isc.isA.FormItem(_1)){var _3=_1;_1=_3.getFieldName();_2=this.buildFieldDataPath(this.getFullDataPath(),_3)}
var _4=this.errors[_1];if(isc.isA.String(_4)||isc.isAn.Array(_4)){return _4}
if(_2!=null){if(isc.isA.String(_4)||isc.isAn.Array(_4))return _4}
return null}
,isc.A.getDataPathErrors=function isc_DynamicForm_getDataPathErrors(_1){var _2=_1.split("/");var _3=this.errors;for(var i=0;i<_2.length;i++){_3=_3[_2[i]];if(!_3)return null}
return _3}
,isc.A.setErrors=function isc_DynamicForm_setErrors(_1,_2){this.errors=isc.DynamicForm.formatValidationErrors(_1);var _3=false,_4={};for(var _5 in this.errors){var _6=this.getItem(_5);if(!_6||!_6.visible){_4[_5]=this.errors[_5];_3=true}}
if(_2)this.showErrors(this.errors,_4)}
,isc.A.setError=function isc_DynamicForm_setError(_1,_2){var _3=this.errors[_1];if(!_3)this.errors[_1]=_2;else{if(isc.isA.String(_3))this.errors[_1]=[_3,_2];else this.errors[_1].add(_2)}}
,isc.A.addFieldErrors=function isc_DynamicForm_addFieldErrors(_1,_2,_3){if(!this.errors)this.errors={};this.addValidationError(this.errors,_1,_2);if(_3)this.showFieldErrors(_1)}
,isc.A.setFieldErrors=function isc_DynamicForm_setFieldErrors(_1,_2,_3){if(this.errors==null)this.errors={};this.errors[_1]=_2;if(_3)this.showFieldErrors(_1)}
,isc.A.clearFieldErrors=function isc_DynamicForm_clearFieldErrors(_1,_2,_3){if(this.errors==null)return;if(!this.errors[_1])return;delete this.errors[_1];if(_2){this.showFieldErrors(_1)}}
,isc.A.clearErrors=function isc_DynamicForm_clearErrors(_1){this.setErrors({},_1)}
,isc.A.hasErrors=function isc_DynamicForm_hasErrors(){var _1=this.errors;if(!_1)return false;for(var _2 in _1){if(_1[_2]!=null)return true}
return false}
,isc.A.hasFieldErrors=function isc_DynamicForm_hasFieldErrors(_1){var _2=this.errors;return(_2&&_2[_1]!=null)}
,isc.A.draw=function isc_DynamicForm_draw(_1,_2,_3,_4){if(isc.$dd)arguments.$de=this;if(!this.readyToDraw())return this;this.invokeSuper(isc.DynamicForm,this.$r0,_1,_2,_3,_4);this.$20q();var _5=this.autoFocus,_6=(!_5?this.$181:this.$183);this.$20f=true;isc.Page.setEvent(isc.EH.IDLE,this,isc.Page.FIRE_ONCE,_6);if(this.position==isc.Canvas.RELATIVE){isc.Page.setEvent(isc.EH.LOAD,this,isc.Page.FIRE_ONCE,"$20r")}
return this}
,isc.A.$zo=function isc_DynamicForm__adjustOverflowForPageLoad(){if(isc.Browser.isSafari){var _1=this.getItems();if(this.isDrawn()&&_1){for(var i=0;i<_1.length;i++){_1[i].$20s();if(this.isDirty())break}}}
return this.Super("$zo",arguments)}
,isc.A.$182=function isc_DynamicForm__delayedSetValues(){this.setItemValues(null,true);this.rememberValues();delete this.$20f}
,isc.A.$184=function isc_DynamicForm__delayedSetValuesFocus(){this.$182();this.focus()}
,isc.A.redraw=function isc_DynamicForm_redraw(){this.$20t();this.$20u();if(this.$20v!=null)delete this.$20v;this.Super("redraw",arguments);this.$20w();this.setItemValues(null,true);var _1,_2,_3;if(isc.Browser.isMoz){_3=this.getClipHandle();if(_3){_1=_3.scrollLeft;_2=_3.scrollTop}}
if(isc.Browser.isMoz){if(_1!=null&&_3.scrollLeft!=_1)
_3.scrollLeft=_1;if(_2!=null&&_3.scrollTop!=_2)
_3.scrollTop=_2}
this.itemsMoved();if(this.$20o!=null){this.notifyAncestorsOnReflow=this.$20o;this.$20o=null}}
,isc.A.$20q=function isc_DynamicForm__itemsDrawn(){var _1=this.items;for(var i=0;i<_1.length;i++){if(_1[i]&&_1[i].visible)_1[i].drawn()}}
,isc.A.$20w=function isc_DynamicForm__itemsRedrawn(){var _1=this.items;for(var i=0;i<_1.length;i++){var _3=_1[i];if(!_3)continue;if(_3.visible){_3.isDrawn()?_3.redrawn():_3.drawn()}else if(_3.isDrawn()){_3.cleared()}}
this.destroyOrphanedItems("Delayed destroy of removed items on form redraw")}
,isc.A.$20x=function isc_DynamicForm__itemsCleared(){var _1=this.items;for(var i=0;i<_1.length;i++){if(_1[i].isDrawn&&_1[i].isDrawn())_1[i].cleared()}
this.destroyOrphanedItems("Delayed destroy of removed items on clear")}
,isc.A.destroyOrphanedItems=function isc_DynamicForm_destroyOrphanedItems(_1){if(this.$196!=null){this.$196.map("destroy",[_1]);delete this.$196}}
,isc.A.$20t=function isc_DynamicForm__itemsRedrawing(){var _1=this.items;for(var i=0;i<_1.length;i++){var _3=_1[i];if(!_3)continue;if(_3.visible&&_3.isDrawn())_3.redrawing()}}
,isc.A.modifyContent=function isc_DynamicForm_modifyContent(){this.$20r()}
,isc.A.$20r=function isc_DynamicForm__placeCanvasItems(){return this.$20y("placeCanvas")}
,isc.A.$20y=function isc_DynamicForm__notifyCanvasItems(_1){if(!isc.CanvasItem)return;for(var i=0;i<this.items.length;i++){var _3=this.items[i];if(_3&&isc.isA.CanvasItem(_3))_3[_1]()}}
,isc.A.redrawFormItem=function isc_DynamicForm_redrawFormItem(_1,_2){var _3=this.getItems();if(!_1)return;while(_1.parentItem)_1=_1.parentItem;if(!_3.contains(_1))return;this.$194=true;this.markForRedraw(_1.ID+": "+(_2?_2:"redrawFormItem"))}
,isc.A.getElementValues=function isc_DynamicForm_getElementValues(){var _1={};for(var i=0;i<this.items.length;i++){var _3=this.items[i],_4=_3.getDataElement()?_3.getDataElement().value:"[no element]";_1[_3[this.fieldIdProperty]]=_4}
return _1}
,isc.A.setItemValues=function isc_DynamicForm_setItemValues(_1,_2,_3,_4){var _5=(_1==null);if(_5)_1=this.getValues();if(_1==null)_1={};var _6;if(_3){_6=this.$20a?this.$20a.criteria:null}
_4=_4||this.items;for(var _7=0;_7<_4.length;_7++){var _8=_4[_7],_9=_8.getFieldName(),_10=_8.getFullDataPath(),_11=_8.isSetToDefaultValue(),_12;if(_10){_12=isc.DynamicForm.$3e(_10,_1,this,true)}else if(_9)_12=_1[_9];if(_2&&isc.CanvasItem&&isc.isA.CanvasItem(_8)&&!_8.$20z())
{continue}
var _13,_14=((!_9&&!_10)||_12===_13);var _15=null;if(_3&&_14&&_8.value!=null){_15=_8.value;if(_15!=_8._value)_11=false}
var _16=null;if(_14&&_6!=null){for(var i=0;i<_6.length;i++){if(_8.canEditCriterion(_6[i])){_14=false;if(_16==null){_16=_6[i]}else{var _18=isc.DataSource.combineCriteria(_16,_6[i],this.operator,null,true);if(!_8.canEditCriterion(_18)){this.logInfo("setItemValues(): current values include multiple extra criteria "+"that could be applied to form item:"+_8+". Criteria include:"+this.echoFull(_16)+" and "+this.echoFull(_6[i])+". However, the item is unable to edit a composite criterion "+"resulting from combining these criteria. Therefore "+this.echoFull(_6[i])+" will not be applied to this item","AdvancedCriteria");continue}else{this.logInfo("setItemValues(): Combined multiple 'extra' criteria into "+"composite criterion:"+this.echoFull(_18)+" and assigned to item:"+_8,"AdvancedCriteria");_16=_18}}
_6.removeAt(i);if(_6.length==0){delete this.$20a}else{i--}}}}
if(_8.shouldSaveValue==false){if(!_14){this.logInfo("DynamicForm.setValues() passed a value for '"+_8[this.fieldIdProperty]+"'."+" The corresponding form item was declared with 'shouldSaveValue' set to "+" false to exclude its value from the form's values object."+" Setting 'shouldSaveValue' to true for this item."+"\n[To avoid seeing this message in the future, set 'shouldSaveValue'"+" to true for any form items whose values are to be managed via "+" form.setValues() / form.getValues().]")
_8.shouldSaveValue=true}else{var _19=(_11?null:_8._value);if(_15!=null)_19=_15;_8.setValue(_19,(_11?false:_2));continue}}
if(_15!=null){_14=false;_12=_15}
if(_14||(_16==null&&_5&&_11)){var _20;if(!_3)_8.clearValue();else if(_3&&_11&&_8._value!==_20){_8.saveValue(_8._value,true)}}else{if(_16!=null){_8.setCriterion(_16)}else{_8.setValue(_12,true)}}}}
,isc.A.$200=function isc_DynamicForm__absPos(){return this.itemLayout==this.$185}
,isc.A.setColWidths=function isc_DynamicForm_setColWidths(_1){if(_1==null)return;if(isc.isA.String(_1)){var _2=_1.split(/[, ]+/);if(_2==null||_2.length==0){this.logWarn("ignoring invalid colWidths string: "+_1);if(_1==this.colWidths)this.colWidths=null;return}
_1=_2}else if(isc.isAn.Array(_1)&&_1.length==1&&isc.isA.String(_1[0]))
{var _2=_1[0].split(/[, ]+/);if(_2!=null||_2.length>1){_1=_2}}
this.colWidths=_1;if(this.isDrawn())this.markForRedraw()}
,isc.A.getInnerHTML=function isc_DynamicForm_getInnerHTML(){if(this.autoDupMethods)this.duplicateMethod("getInnerHTML");var _1=isc.StringBuffer.create();if(this.writeFormTag&&!this.isPrinting)_1.append(this.getFormTagStartHTML());var _2=this.values,_3=this.items;var _4=false;for(var _5=0;_5<_3.length;_5++){var _6=_3[_5],_7=_6.visible;if(_6.showIf){isc.Func.replaceWithMethod(_6,this.$186,this.$187);var _8=_6.getValue();_7=(_6.showIf(_6,_8,this,_2)==true)}
if(_7&&this.isPrinting){if(_6.shouldPrint!=null){_7=_6.shouldPrint}else if(_7&&this.currentPrintProperties.omitControls){var _9=this.currentPrintProperties.omitControls;for(var i=0;i<_9.length;i++){var _11=_9[i];if(isc.isA[_11]&&isc.isA[_11](_6)){_7=false}}}}
if(_7!=_6.visible){_6.visible=_7;if(!_6.alwaysTakeSpace)_4=true}}
if(_4||this.$194)isc.Canvas.invalidateTableResizePolicy(_3);this.$194=false;this.setRequiredIf();if(this.flattenItems){var _12=null;for(var _5=0;_5<_3.length;_5++){var _6=_3[_5];if(_6.visible||_6.alwaysTakeSpace)_12++;if(_6.showTitle&&_6.titleOrientation!="top")
_12++;_6.$201=_6.colSpan||null;_6.colSpan=null}
if(_12){this.numCols=_12;this.$194=true;this.markForRedraw()}}
if(this.$200()){_1.append(this.getAbsPosHTML());_1.append(this.$188);return _1.release()}
_1.append(this.getTableStartHTML());if(this.titleWidth==this.$tb&&!this.colWidths){this.colWidths=[];for(var i=0;i<this.numCols;i++)this.colWidths[i]=this.$tb}
var _13;if(this.colWidths){_13=this.colWidths;if(_13.length>this.numCols){if(!this.$202){this.logWarn("colWidths Array longer than numCols, using only first "+this.numCols+" column widths")}
_13=_13.slice(0,this.numCols)}else if(_13.length<this.numCols){if(!this.$202){this.logWarn("colWidths Array shorter than numCols, remaining columns get '*' size")}
for(var i=_13.length;i<this.numCols;i++)_13[i]=isc.star}}else{_13=[];var _14=this.getInnerContentWidth();_14-=(this.cellBorder!=null?this.cellBorder:0);var _15=Math.floor(this.numCols/ 2),_16=_14-(_15*this.titleWidth),_17;if(this.isPrinting){_17="*"}else{_17=Math.floor(_16/(this.numCols-_15));_17=Math.max(this.minColWidth,_17)}
for(var i=0;i<_15;i++){_13.add(this.titleWidth);_13.add(_17)}
if((this.numCols%2)!=0)_13.add(_17);if(this.logIsInfoEnabled(this.$189)){this.logInfo("totalWidth: "+_14+", generated colWidths: "+_13,this.$189)}}
var _18=this.getInnerContentWidth(),_19=this.getInnerContentHeight();if(this.cellSpacing!=0){if(isc.Browser.isMoz)_19-=2*this.cellSpacing;else if(isc.Browser.isSafari)_19-=this.cellSpacing}
_3.$17z=this.defaultRowHeight;isc.Canvas.applyTableResizePolicy(_3,_18,_19,this.numCols,_13);var _20=false;if(isc.CanvasItem){for(var i=0;i<_3.length;i++){var _6=_3[i];if(isc.isA.CanvasItem(_6)&&_6.checkCanvasOverflow()){if(!_20&&this.logIsInfoEnabled(this.$189)){this.logInfo("CanvasItem: "+_6+" overflowed, rerunning policy",this.$189)}
_20=true}}}
if(_20){isc.Canvas.applyTableResizePolicy(_3,_18,_19,this.numCols,_13,null,true)}
if(!this.isPrinting){_13=_3.$170}
for(var _21=0;_21<_13.length;_21++){var _22=_13[_21];if(_22=="*"){_1.append("<COL>")}else{_1.append(this.$19a,_22,this.$r8)}}
if(this.isPrinting){_1.append("<tr>")}else{_1.append(this.$19b)}
var _23=isc.DynamicForm.$203();for(var _21=0;_21<_13.length;_21++){if(!isc.isA.Number(_13[_21])){_1.append(_23.join(isc.emptyString),this.$19c)}else{var _18=_13[_21];_18-=(this.cellSpacing!=null?(2*this.cellSpacing):0);_23[3]=(isc.FormItem?isc.FormItem.getPrototype().baseStyle:null);var _24=isc.Browser.isIE?1:0,_25=_23.join(isc.emptyString);_1.append(_25,this.fixedColWidths?isc.Canvas.spacerHTML(_18,_24):null,this.$19c)}}
_1.append(this.$120);if(this.autoSendTarget&&this.target)_1.append(this.$204());var _26=[];for(var _5=0,_27=_3.length;_5<_27;_5++){var _6=_3[_5];if(!_6)continue;var _7=_6.visible;_6.$20k();if(this.logIsDebugEnabled())this.logDebug("Drawing FormItem: "+_6);if(!_6.alwaysTakeSpace&&!_7)continue;if((_6.rowSpan==0||_6.colSpan==0)&&_5<_27-1){_26.add(_6);continue}
var _28=_6.getFieldName(),_29=_6.getErrors(),_8=_6.getValue(),_30=this.getTitleOrientation(_6);if(isc.is.emptyString(_29))_29=null;if(_6.$17u||_5==0){if(_5!=0)_1.append(this.$120);if(_6.$17w>0){for(var i=0;i<_6.$17w;i++)_1.append(this.$12z,this.$120)}
_1.append(this.$12z);if(_6.$17x>0){for(var i=0;i<_6.$17x;i++)_1.append(this.$121,this.$123)}}
if(_30==isc.Canvas.LEFT){_1.append(this.getTitleCellHTML(_6,_29))}
_1.append(this.getCellStartHTML(_6,_29));if(_7&&_30==isc.Canvas.TOP){_1.append(this.getTitleSpanHTML(_6,_29),this.$19d)}
var _31=(_7&&_29&&this.showInlineErrors);if(_31&&_6.getErrorOrientation()==isc.Canvas.TOP){_1.append(this.getItemErrorHTML(_6,_29))}
if(_26.length>0){for(var m=0;m<_26.length;m++){if(!_26[m].visible)continue;_1.append(_26[m].getInnerHTML(_26[m].getValue()))}
_26.length=0}
if(_7){_1.append(_6.getInnerHTML(_8,true,this.showInlineErrors))}else _1.append(isc.Canvas.spacerHTML(_6.width,_6.height));if(_31&&_6.getErrorOrientation()==isc.Canvas.BOTTOM){_1.append(this.getItemErrorHTML(_6,_29))}
_1.append(this.getCellEndHTML(_6,_29));if(_30==isc.Canvas.RIGHT){_1.append(this.getTitleCellHTML(_6,_29))}}
if(_3.length>0)_1.append(this.$120);if(this.writeFormTag&&!this.isPrinting)_1.append(this.$19e);else _1.append("</TABLE>");return _1.release()}
);isc.evalBoundary;isc.B.push(isc.A.getPrintChildren=function isc_DynamicForm_getPrintChildren(){return null}
,isc.A.createErrorItem=function isc_DynamicForm_createErrorItem(){var _1=isc.addProperties({cellStyle:this.errorItemCellStyle},this.errorItemDefaults,this.errorItemProperties);this.addItems([_1],0);this.$205=this.getItem(0)}
,isc.A.getErrorsHTML=function isc_DynamicForm_getErrorsHTML(_1){if(!_1||isc.isAn.emptyObject(_1))return isc.emptyString;var _2=this.errorsPreamble;_2+="<UL>";for(var _3 in _1){var _4=this.getItem(_3),_5;if(_4)_5=_4.getErrorMessage(_1[_3])
else{_5=_1[_3];if(isc.isAn.Array(_5))
_5="<ul><li>"+_5.join("</li><li>")+"</li></ul>"}
_2+="<LI>"+(_4?_4.getTitle():_3)+" : "+_5+"</LI>"}
_2+="</UL>";return _2}
,isc.A.getItemErrorHTML=function isc_DynamicForm_getItemErrorHTML(_1,_2){return _1.getErrorHTML(_2)}
,isc.A.$204=function isc_DynamicForm__getAutoSendTargetHTML(){this.$19f[1]=this.autoSendTargetFieldName;this.$19f[3]=this.target;return this.$19f.join(isc.emptyString)}
,isc.A.getCellStartHTML=function isc_DynamicForm_getCellStartHTML(_1,_2){var _3=_1.getColSpan(),_4=_1.getRowSpan();if(_3==0)_3=1;if(_4==0)_4=1;if(_3=="*"){var _5=(_1.$17v?_1.$17v[0]:0);_3=(this.numCols-_5)}
var _6=_1.getCellStyle();var _7=this.fixedRowHeights||_1.shouldFixRowHeight();var _8=_1.$172?_1.$172[1]:null;if(isc.isA.Number(_8)&&this.cellSpacing!=0)_8-=2*this.cellSpacing;return this.$206((_1.align?_1.align:((this.form?this.form.isRTL():this.isRTL())?isc.Canvas.RIGHT:isc.Canvas.LEFT)),_1.vAlign,_6,_4,_3,null,(_7?_8:null),null,_1.cssText,(this.form?this.form.getID():this.getID()),_1.getItemID(),_1.getFormCellID())}
,isc.A.$206=function isc_DynamicForm__getCellStartHTML(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13){var _14=isc.StringBuffer.create(),_15=isc.$ah;_14.append("<TD ALIGN=",_1,(_2==null?_15:" VALIGN="+_2),(_3!=null?" CLASS='"+_3+"'":_15)," STYLE='",(_9!=null?_9:_15),"'",(_4>1?" ROWSPAN="+_4:_15),(_5>1?" COLSPAN="+_5:_15),(_6!=null?" WIDTH="+_6:_15),(_7!=null?" HEIGHT="+_7:_15),(_8!=null?_8:_15));if(_12){_14.append(" ID=",_12," ")}
if(_11&&_10){_14.append(isc.DynamicForm.$18o,"='",_11,"'")}
_14.append(_13?"><NOBR>":">");return _14.release()}
,isc.A.getCellEndHTML=function isc_DynamicForm_getCellEndHTML(_1,_2){return this.$207()}
,isc.A.$207=function isc_DynamicForm__getCellEndHTML(_1){return _1?"</NOBR></TD>":"</TD>"}
,isc.A.getTitleOrientation=function isc_DynamicForm_getTitleOrientation(_1){if(_1&&!_1.shouldShowTitle())return null;return(_1?_1.titleOrientation:null)||this.titleOrientation||isc.Canvas.LEFT}
,isc.A.getTitleAlign=function isc_DynamicForm_getTitleAlign(_1){var _2=this.form||this;return(_1.titleAlign?_1.titleAlign:this.titleAlign?this.titleAlign:this.isRTL()?isc.Canvas.LEFT:isc.Canvas.RIGHT)}
,isc.A.getTitleVAlign=function isc_DynamicForm_getTitleVAlign(_1){var _2=(_1.titleVAlign?_1.titleVAlign:this.titleVAlign?this.titleVAlign:isc.Canvas.CENTER);return(_2==isc.Canvas.CENTER?isc.Canvas.MIDDLE:_2)}
,isc.A.getTitleHeight=function isc_DynamicForm_getTitleHeight(_1){var _2=this.form||this;return(_1.titleHeight!=null?_1.titleHeight:this.titleHeight)}
,isc.A.getTitleSpanHTML=function isc_DynamicForm_getTitleSpanHTML(_1,_2){var _3=isc.StringBuffer.create();_3.append("<SPAN ",this.$208(_1)," CLASS='",_1.getTitleStyle(),"' ALIGN=",this.getTitleAlign(_1),">");_3.append(this.getTitleHTML(_1,_2));_3.append("</SPAN>");return _3.release()}
,isc.A.shouldClipTitle=function isc_DynamicForm_shouldClipTitle(_1){if(!_1||!_1.form==this)return false;return(_1.clipTitle!=null?_1.clipTitle:this.clipItemTitles)}
,isc.A.getTitleCellHTML=function isc_DynamicForm_getTitleCellHTML(_1,_2){var _3=isc.StringBuffer.create(),_4=_1.getTitleStyle(),_5=this.getTitleAlign(_1),_6=this.getTitleVAlign(_1);var _7=this.$19o;_7[1]=this.$208(_1);_7[3]=_4;_7[5]=_5;_7[7]=_6;if(_1.getRowSpan()>1)_7[9]=" ROWSPAN="+_1.getRowSpan();else _7[9]=null;if(_1.getTitleColSpan()>1)_7[10]=" COLSPAN="+_1.getTitleColSpan();else _7[10]=null;_3.append(_7.join(isc.emptyString));_3.append(this.getTitleCellInnerHTML(_1,_2));_3.append(this.$19n);return _3.release()}
,isc.A.getTitleCellInnerHTML=function isc_DynamicForm_getTitleCellInnerHTML(_1,_2){var _3=isc.StringBuffer.create(),_4=_1.getTitleStyle(),_5=this.getTitleAlign(_1),_6=_1.$173||null,_7=_1.$172?_1.$172[1]:null,_8=this.shouldClipTitle(_1),_9=(_1.wrapTitle!=null?_1.wrapTitle:(this.wrapItemTitles!=null?this.wrapItemTitles:!_8));if(_7){if(this.cellSpacing)_7-=2*this.cellSpacing;var _10,_11;if(_4){_10=isc.Element.$q5(_4,true);_11=isc.Element.$q7(_4,true)}
if(_10==null)_10=this.cellPadding||0;if(_11==null)_11=this.cellPadding||0;_7-=(_10+_11)
if(_4)_7-=isc.Element.$q3(_4)}
if(_6){if(this.cellSpacing)_6-=2*this.cellSpacing;var _12,_13;if(_4){_12=isc.Element.$q8(_4,true);_13=isc.Element.$q9(_4,true)}
if(_12==null)_12=this.cellPadding||0;if(_13==null)_13=this.cellPadding||0;_6-=(_12+_13)
_6-=isc.Element.$q4(_4)}
var _14=isc.Browser.isMoz?this.$19j:this.$19g,_15=isc.Browser.isMoz?this.$19i:this.$19h;if(_8){var _16=this.$19p;if(_9||!isc.Browser.isMoz)_16[1]=null;else _16[1]="white-space:nowrap;";if(_6!=null)_16[2]=_15+_6+"px;"
else _16[2]=null;if(_7!=null)_16[3]=_14+_7+"px;"
else _16[3]=null;_3.append(_16.join(isc.emptyString));if(!isc.Browser.isMoz){var _17=isc.DynamicForm.$209();_17[1]=_7;_17[3]=_4;_17[5]=_5;if(!_9)_17[7]=this.$19k
else _17[7]=null;_3.append(_17.join(isc.emptyString))}}else if(!_9){_3.append(this.$19k)}
_3.append(this.getTitleHTML(_1,_2));if(_8){if(!isc.Browser.isMoz)_3.append(this.$19l);_3.append(this.$19m)}
return _3.release()}
,isc.A.$208=function isc_DynamicForm__containsItemTitleAttrHTML(_1){if(!isc.DynamicForm.$21a){isc.DynamicForm.$21b=[" ",isc.DynamicForm.$18o,"='",null,"' ",isc.DynamicForm.$18p,"='",isc.DynamicForm.$18v,"' ","ID="]}
isc.DynamicForm.$21b[3]=_1.getItemID();isc.DynamicForm.$21b[10]=this.$21c(_1);return isc.DynamicForm.$21b.join(isc.emptyString)}
,isc.A.$21c=function isc_DynamicForm__getTitleCellID(_1){return this.$t4(_1.getID()+this.$19q)}
,isc.A.getTitleCell=function isc_DynamicForm_getTitleCell(_1){if(!this.isDrawn())return null;_1=this.getItem(_1);if(!_1)return null;return isc.Element.get(this.$21c(_1))}
,isc.A.updateTitleCellState=function isc_DynamicForm_updateTitleCellState(_1){var _2=this.getTitleCell(_1);if(_2==null)return;_1=this.getItem(_1);_2.className=_1.getTitleStyle();_2.innerHTML=this.getTitleCellInnerHTML(_1,_1.getErrors())}
,isc.A.getTitleHTML=function isc_DynamicForm_getTitleHTML(_1,_2){var _3=isc.StringBuffer.create();var _4=_1.visible?_1.getTitleHTML():null;if(_4){var _5=this.isRequired(_1),_6=this.getTitleOrientation(_1),_7=(_6==isc.Canvas.LEFT||_6==isc.Canvas.TOP);_3.append((_5&&this.hiliteRequiredFields?(_7?this.requiredTitlePrefix:this.requiredRightTitlePrefix):(_7?this.titlePrefix:this.rightTitlePrefix)),_4,(_5&&this.hiliteRequiredFields?(_7?this.requiredTitleSuffix:this.requiredRightTitleSuffix):(_7?this.titleSuffix:this.rightTitleSuffix)))}else{_3.append("&nbsp;")}
return _3.release()}
,isc.A.getFormTagStartHTML=function isc_DynamicForm_getFormTagStartHTML(){var _1=this.$19s,_2=this.getFormID(),_3=this.getID();_1[3]=_2;if(this.$200())_1[4]=this.$19u;else _1[4]=null;_1[6]=this.method;_1[8]=this.action;if(this.isMultipart())_1[10]=isc.DynamicForm.MULTIPART_ENCODING;else _1[10]=isc.DynamicForm.NORMAL_ENCODING;if(this.target!=null){_1[11]=this.$19v;_1[12]=this.target;_1[13]=this.$r9}else{_1[11]=null;_1[12]=null;_1[13]=null}
_1[15]=_3;_1[17]=_3;return _1.join(isc.emptyString)}
,isc.A.getTableStartHTML=function isc_DynamicForm_getTableStartHTML(){var _1=isc.isA.DynamicForm(this)?this.$19w:isc.DynamicForm.getPrototype().$19w;_1[1]=this.$21d();_1[4]=this.isPrinting?"100%":(this.getInnerContentWidth!=null?this.getInnerContentWidth():this.getInnerWidth());_1[6]=this.cellSpacing;_1[8]=this.cellPadding;_1[10]=this.cellBorder;return _1.join(isc.emptyString)}
,isc.A.$21d=function isc_DynamicForm__getTableElementID(){return this.$t4(this.$19x)}
,isc.A.$21e=function isc_DynamicForm__getTableElement(){return isc.Element.get(this.$21d())}
,isc.A.layoutChildren=function isc_DynamicForm_layoutChildren(_1,_2,_3,_4){this.invokeSuper(isc.DynamicForm,"layoutChildren",_1,_2,_3,_4);var _5=this.getItems();if(!_5)return;for(var i=0;i<_5.length;i++){var _7=_5[i].width,_8=_5[i].height;if((isc.isA.String(_7)&&(_7.contains("%")||_7.contains("*")))||(isc.isA.String(_8)&&(_8.contains("%")||_8.contains("*"))))
{this.markForRedraw("size change with dynamic size children");break}}}
,isc.A.getAbsPosHTML=function isc_DynamicForm_getAbsPosHTML(){var _1=isc.SB.create();for(var _2=0,_3=this.items.length;_2<_3;_2++){var _4=this.items[_2];if(!_4)continue;_4.$20k();if(!_4.visible)continue;var _5=!_4.$21f(),_6=this.showInlineErrors;_1.append(_4.getStandaloneItemHTML(_4.getValue(),_5,_6))}
return _1.release()}
,isc.A.getScrollWidth=function isc_DynamicForm_getScrollWidth(_1){if(this.$u9){this.$u9=null;this.adjustOverflow("widthCheckWhileDeferred")}
if(!_1&&this.$xg!=null)return this.$xg;var _2;if(!isc.Browser.isIE||!this.$200()||!(this.isDrawn()||this.handleDrawn())||this.items==null)
{_2=isc.Canvas.$ce.getScrollWidth.call(this,_1)}else{_2=0;for(var i=0;i<this.items.length;i++){var _4=this.items[i];if(_4.visible==false||!_4.isDrawn())continue;var _5=_4.getAbsDiv();if(_5){var _6=_5.scrollWidth+_4.$21g(_4.left);if(_6>_2)_2=_6}}}
this.$xg=_2;return _2}
,isc.A.getScrollHeight=function isc_DynamicForm_getScrollHeight(_1){if(this.$u9){this.$u9=null;this.adjustOverflow("heightCheckWhileDeferred")}
if(!_1&&this.$xk!=null)return this.$xk;var _2;if(!isc.Browser.isIE||!this.$200()||!(this.isDrawn()||this.handleDrawn())||this.items==null)
{_2=isc.Canvas.$ce.getScrollHeight.call(this,_1)}else{_2=0;for(var i=0;i<this.items.length;i++){var _4=this.items[i];if(_4.visible==false||!_4.isDrawn())continue;var _5=_4.getAbsDiv();if(_5){var _6=_5.scrollHeight+_4.$21g(_4.top,true);if(_6>_2)_2=_6}}}
this.$xk=_2;return _2}
,isc.A.$21h=function isc_DynamicForm__formWillSubmit(){return this.canSubmit||this.isMultipart()||(this.action!=isc.DynamicForm.getPrototype().action)}
,isc.A.submitForm=function isc_DynamicForm_submitForm(){if(!this.$21h()){this.logWarn("Attempt to perform direct submission on DynamicForm where this.canSubmit "+"is false. Please set this property to true, or use the standard databinding "+"interfaces to send data to the server.")}
if(this.getFileItemForm()!=null){this.logWarn("Performing a direct submission on a DynamicForm containing a FileItem. "+"Note: This item's value will not be submitted to the server.  FileItems "+"are intended for use with databound forms backed by the SmartClient server "+"only.  If you are not using the SmartClient Databinding subsystem, "+"use an UploadItem rather than a FileItem to submit a file as part of a raw "+"HTTP request. Otherwise use saveData() rather than a direct call to "+"submitForm() to save the full set of values for the form.")}
var _1=this.getForm();if(!_1)return;if(_1.action!=this.action)_1.action=this.action;try{return _1.submit()}catch(e){this.logWarn("Form submission was unsuccessful. In some browsers this can occur when "+"an upload item is present and has an invalid value.\n"+e.message);this.formSubmitFailed()}}
,isc.A.performImplicitSave=function isc_DynamicForm_performImplicitSave(_1,_2){this.implicitSaveInProgress=true;if(_1){if(_1.$21i){_1.parentItem.updateValue()}
if(_1.awaitingImplicitSave)delete _1.awaitingImplicitSave;if(_1.$du!=null)isc.Timer.clear(_1.$du)}
if(this.awaitingImplicitSave)delete this.awaitingImplicitSave;this.logInfo("implicitSave called "+(!_2?"by editorExit()":"after implicitSaveDelay ("+this.implicitSaveDelay+"ms)")+" for item "+_1.name+".");this.saveData(this.getID()+".$21j(data)",{showPrompt:false})}
,isc.A.$21j=function isc_DynamicForm__implicitSaveCallback(_1){delete this.implicitSaveInProgress;this.implicitSaveCallback(_1)}
,isc.A.implicitSaveCallback=function isc_DynamicForm_implicitSaveCallback(_1){}
,isc.A.formSubmitFailed=function isc_DynamicForm_formSubmitFailed(){isc.warn(this.formSubmitFailedWarning);var _1=this.getValues()._transaction;if(_1!=null&&isc.RPCManager&&isc.XMLTools){var _2=isc.XMLTools.parseXML(this.getValues()._transaction),_3;if(_2)_3=isc.XMLTools.selectNumber(_2,"//transactionNum");if(_3!=null){isc.RPCManager.doClearPrompt(_3);isc.RPCManager.clearTransaction(_3)}
var _4=this.getItem("_transaction");if(_4&&isc.isA.HiddenItem(_4)){this.clearValue("_transaction")}}}
,isc.A.setAction=function isc_DynamicForm_setAction(_1,_2){this.action=_1;var _3=this.getForm();if(_3)_3.action=_1;this.$21k=!_2}
,isc.A.setTarget=function isc_DynamicForm_setTarget(_1){this.target=_1;var _2=this.getForm();if(_2)_2.target=_1}
,isc.A.setMethod=function isc_DynamicForm_setMethod(_1){this.method=_1;var _2=this.getForm();if(_2)_2.method=_1}
,isc.A.getFileItemForm=function isc_DynamicForm_getFileItemForm(){if(!isc.FileItem)return null;var _1=this.getItems()||[];for(var i=0;i<_1.length;i++){if(isc.isA.FileItem(_1[i]))return _1[i].canvas}
return null}
,isc.A.$19t=function isc_DynamicForm__handleNativeSubmit(){return false}
,isc.A.validate=function isc_DynamicForm_validate(_1,_2,_3,_4,_5){if(this.disableValidation)return true;if(this.dataSource&&this.dataSource.useLocalValidators!=null&&this.useLocalValidators==false)return true;var _6=this.hasErrors(),_7=false,_8=this.getForm(),_9=false;var _10={},_11={},_12=this.getValues(),_13=(_1&&!_2&&this.dataSource)?isc.addProperties({},this.getDataSource().getFields()):null;var _14={unknownErrorMessage:this.unknownErrorMessage,serverValidationMode:"full"};if(_3)
_14.typeValidationsOnly=_3;if(_5)
_14.skipServerValidation=_5;var _15=isc.rpc?isc.rpc.startQueue():false;for(var _16=0;_16<this.items.length;_16++){var _17=false,_18=this.items[_16],_19=_18.getFieldName(),_20=isc.DynamicForm.$3f(this.dataPath,_18.getDataPath()),_21=_18.getValue(),_22=!_18.visible||isc.isA.HiddenItem(_18);if(_22&&!_1)continue;if(_18.validators!=null){if(!isc.isAn.Array(_18.validators)){_18.validators=[_18.validators]}
var _23=this.validateField(_18,_18.validators,_21,_12,_14);if(_23!=null){if(_23.errors!=null){_17=this.addValidationError(_10,_19||_20,_23.errors);if(_17)_7=true}
if(_23.resultingValue!=null){_21=_23.resultingValue;if(_20){isc.DynamicForm.$3d(_20,_21,_12)}else if(_19){_12[_19]=_21}
_9=true}}}
if(_22&&_17)_11[_19||_20]=_10[_19||_20];if(_13)delete _13[_19]}
if(_13){_14.dontValidateNullValues=true;delete _14.typeValidationsOnly;for(var i in _13){var _25=_13[i],_26=i,_27=_25.validators,_21=_12[_26];if(_27!=null){var _21=_12[_26];var _23=this.validateField(_25,_27,_21,_12,_14);if(_23!=null&&_23.errors!=null){this.addValidationError(_10,_26,_23.errors)}}
if(_10[_26]!=null)_11[_26]=_10[_26]}}
if(!_15&&isc.rpc)isc.rpc.sendQueue();if(_7)this.logInfo("Validation errors: "+isc.Log.echoAll(_10));if(_4)return(_7?_10:true);this.setErrors(_10);if(_9){this.setItemValues(_12);for(var _28 in _12){if(this.getItem(_28)==null)this.$20j(_28,_12[_28])}}
if(_7||_6)this.showErrors(_10,_11);return!_7}
,isc.A.valuesAreValid=function isc_DynamicForm_valuesAreValid(_1,_2){var _3=this.validate(_1,null,null,true);if(_3===true){return(_2?null:true)}else{return(_2?_3:false)}}
,isc.A.getValidatedValues=function isc_DynamicForm_getValidatedValues(){if(!this.validate())return null;return this.getValues()}
,isc.A.showErrors=function isc_DynamicForm_showErrors(_1,_2,_3){var _4;if(_2===_4)_2=this.getHiddenErrors();if(_1===_4)_1=this.getErrors();if(_1&&!this.showInlineErrors&&(!this.$205||this.$205.destroyed||!this.items.contains(this.$205)))
{this.createErrorItem()}
this.markForRedraw("Validation Errors Changed");if(_1&&!isc.isAn.emptyObject(_1)&&!_3){for(var _5 in _1){var _6=this.getItem(_5);if(_6&&_6.isVisible()&&_6.isDrawn()){this.$21l(_6);break}}}
if(!this.showInlineErrors){this.delayCall("scrollIntoView",[0,0],100)}
if(_2){this.$21m(_2)}}
,isc.A.getHiddenErrors=function isc_DynamicForm_getHiddenErrors(){if(!this.errors)return null;var _1=false,_2={};for(var _3 in this.errors){var _4=this.getItem(_3);if(!_4||!_4.visible){_1=true;_2[_3]=this.errors[_3]}}
return(_1?_2:null)}
,isc.A.showFieldErrors=function isc_DynamicForm_showFieldErrors(_1,_2){var _3;return this.showErrors(_3,_3,_2)}
,isc.A.$21m=function isc_DynamicForm__handleHiddenValidationErrors(_1){if(_1==null||isc.isAn.emptyObject(_1))return;var _2;if(this.handleHiddenValidationErrors){_2=this.handleHiddenValidationErrors(_1)}
if(_2==false)return;var _3="Validation errors occurred for the following fields "+"with no visible form items:";for(var _4 in _1){var _5=_1[_4];if(!isc.isAn.Array(_5))_5=[_5];if(_5.length==0)continue;_3+="\n"+_4+":";for(var i=0;i<_5.length;i++){_3+=(i==0?"- ":"\n - ")+_5[i]}}
this.logWarn(_3,"validation")}
,isc.A.isRequired=function isc_DynamicForm_isRequired(_1){return(isc.DynamicForm.canEditField(_1,this)&&(_1.required||_1.$21n||this.isXMLRequired(_1)))}
,isc.A.setRequiredIf=function isc_DynamicForm_setRequiredIf(){var _1=this.getValues();for(var _2=0;_2<this.items.length;_2++){var _3=this.items[_2],_4=_3.validators;delete _3.$21n;if(!_3.visible||!_4||_4.length==0)continue;for(var v=0;v<_4.length;v++){var _6=_4[v];if(!_6)continue;var _7=isc.Validator.getValidatorType(_6);if(_7==this.$19y){var _8=_3.getValue();if(_6.expression!=null&&!isc.isA.Function(_6.expression)){isc.Func.replaceWithMethod(_6,"expression","item,validator,value")}
_3.$21n=_6.expression.apply(this,[_3,_6,_8])}else if(_7==this.$19z){_3.$21n=true}}}}
,isc.A.setFocusItem=function isc_DynamicForm_setFocusItem(_1){_1=this.getItem(_1);this.$197=_1}
,isc.A.getFocusItem=function isc_DynamicForm_getFocusItem(){var _1=this.getFocusSubItem();while(_1&&_1.parentItem!=null){_1=_1.parentItem}
return _1}
,isc.A.getFocusSubItem=function isc_DynamicForm_getFocusSubItem(){return this.$197}
,isc.A.$0n=function isc_DynamicForm__readyToSetFocus(){return!this.isDisabled()}
,isc.A.setFocus=function isc_DynamicForm_setFocus(_1){if(!this.$0n())return;var _2=this.isVisible();if(_1){var _3=this.getFocusSubItem();if(_3==null){var _4=this.getItems();if(_4!=null){for(var i=0;i<_4.length;i++){var _6=_4[i];if(_6.$mb()&&_6.isDrawn()&&_6.isVisible()&&!_6.isDisabled())
{_3=_6;break}}}}
var _7=isc.EH.lastEvent;if(_3!=null&&!(_7.target==this&&_7.eventType==isc.EH.MOUSE_DOWN)){return this.focusInItem(_3)}}
this.Super("setFocus",arguments);if(!_1){this.$21o(this.getFocusSubItem())}}
,isc.A.$l5=function isc_DynamicForm__focusInNextTabElement(_1,_2,_3){if(_3||!this.items||this.items.length==0||(_2&&isc.EH.targetIsMasked(this,_2)))
{return this.Super("$l5",arguments)}
var _4=this.items,_5=this.getFocusSubItem();if(_5==null){this.focusAtEnd(_1);return}
while(_5.parentItem){if(_5.$21p(_1))return;_5=_5.parentItem}
if(_5.$21p(_1))return;_5=this.$21q(_5,_1);if(_5!=null){this.focusInItem(_5)}else{if(isc.EH.$ma==this&&isc.EH.$mc==this){this.focusAtEnd(_1)}else{return this.Super("$l5",arguments)}}}
,isc.A.$21q=function isc_DynamicForm__getNextFocusItem(_1,_2){var _3=this.items,_4=_1,_5=_1.getGlobalTabIndex(),_6,_7,_8=_3.indexOf(_1);for(var i=0;i<_3.length;i++){var _10=_3[i];if(_10==_1)continue;var _11=_10.getGlobalTabIndex();if(_11<0){continue}
if(!this.$21r(_10,true))continue;if(_2){if(_11==_5&&i>_8){_6=_10;break}
if(_11>_5&&(_7==null||_7>_11))
{_6=_10;_7=_11}}else{if((_11<_5||(_11==_5&&_8>i))&&(_7==null||_7<=_11))
{_6=_10;_7=_11}}}
return _6}
,isc.A.focusAtEnd=function isc_DynamicForm_focusAtEnd(_1){if(!this.items)return;var _2,_3,_4=this.items;for(var i=0;i<_4.length;i++){var _6=_4[i],_7=_6.getGlobalTabIndex();if(_7<0||!this.$21r(_6,true))continue;if((_3==null)||(_1&&_7<_3)||(!_1&&_7>=_3))
{_2=_6;_3=_7}}
if(_2&&this.$21r(_2,true))this.focusInItem(_2,!!_1);else{var _8,_9=isc.EH.clickMaskRegistry;if(_9){for(var i=_9.length-1;i>=0;i--){if(isc.EH.isHardMask(_9[i])){_8=_9[i];break}}}
this.$l5(_1,_8,true)}}
,isc.A.$21r=function isc_DynamicForm__canFocusInItem(_1,_2){if(isc.isA.String(_1))_1=this.getItem(_1);return _1&&_1.$mb()&&_1.isDrawn()&&_1.isVisible()&&!_1.isDisabled()&&(!_2||_1.tabIndex!=-1)}
,isc.A.focusInItem=function isc_DynamicForm_focusInItem(_1,_2){if(_1!=null){var _3=this.getItem(_1)}else{var _3=this.getFocusSubItem()}
if(!_3){if(_1!=null)this.logWarn("couldn't find focus item: "+_1);return}
if(_3.$mb()){_3.focusInItem(_2);this.setFocusItem(_3);if(this.$20f){var _4=this;isc.Page.setEvent("idle",function(){if(!_4.destroyed)_4.focusInItem()},isc.Page.FIRE_ONCE)}}else{this.logWarn("focusInItem: item cannot accept focus: "+_3)}}
,isc.A.clearFocusItem=function isc_DynamicForm_clearFocusItem(){delete this.$197}
,isc.A.blurFocusItem=function isc_DynamicForm_blurFocusItem(){var _1=this.getFocusSubItem();if(_1!=null){this.$21o(_1);this.clearFocusItem()}}
,isc.A.$21o=function isc_DynamicForm__blurItem(_1){if(_1!=null)_1.blurItem()}
,isc.A.$20u=function isc_DynamicForm__blurFocusItemWithoutHandler(){var _1=this.getFocusSubItem();if(_1!=null&&_1.hasFocus){if(this.$20v==null)this.$20v=0;else this.$20v+=1;this.$21o(_1)}else{this.logDebug("blur w/o handler: no item to blur")}}
,isc.A.$21l=function isc_DynamicForm__focusInItemWithoutHandler(_1){if(!_1||!this.$21r(_1)){var _2;if(_1&&_1.parentItem){this.$21l(_1.parentItem);_2=true}
this.logInfo("$21l("+_1+"): not calling focus as item not focusable or item already has focus"+(_2?". Putting focus into containerItem instead.":""),"nativeFocus")
return}
var _3=_1.hasFocus;if(isc.Browser.isIE){var _4=isc.DynamicForm.$ot(document.activeElement);_3=(_4&&_4.item==_1)}
if(_3)return;this.$21s(_1);this.focusInItem(_1)}
,isc.A.$21s=function isc_DynamicForm__suppressFocusHandlerForItem(_1){if(this.$21t==null)this.$21t=0;else this.$21t+=1;this.$21u=_1}
,isc.A.setOpacity=function isc_DynamicForm_setOpacity(_1,_2,_3,_4,_5,_6){var _7=this.opacity;this.invokeSuper(isc.DynamicForm,"setOpacity",_1,_2,_3,_4,_5,_6);_1=this.opacity;if(isc.Browser.isMoz&&this.hasFocus&&(_1!=_7)&&(_1==null||_1==100||_7==null||_7==100))
{var _8=this.getFocusSubItem();if(_8&&_8.$21v()){this.$20u();this.$21l(_8)}}}
,isc.A.clearingElement=function isc_DynamicForm_clearingElement(_1){if(this.$21t!=null&&this.$21u==_1){delete this.$21t;delete this.$21u}
if(this.$20v!=null&&(this.getFocusSubItem()==_1)){delete this.$20v}}
,isc.A.hide=function isc_DynamicForm_hide(){if(isc.Browser.isMoz)this.$21o(this.getFocusSubItem());this.Super("hide",arguments)}
,isc.A.setVisibility=function isc_DynamicForm_setVisibility(_1,_2,_3,_4){this.invokeSuper(isc.DynamicForm,"setVisibility",_1,_2,_3,_4);this.itemsVisibilityChanged();if(this.isVisible()&&this.isDrawn()&&this.autoFocus)this.focus()}
,isc.A.clear=function isc_DynamicForm_clear(){this.Super("clear",arguments);this.itemsVisibilityChanged()
this.$20x()}
,isc.A.$nb=function isc_DynamicForm__focusChanged(_1){this.Super("$nb",arguments);if(!this.hasFocus)this.$21o(this.getFocusSubItem())}
,isc.A.parentVisibilityChanged=function isc_DynamicForm_parentVisibilityChanged(_1){if(!this.isVisible()&&isc.Browser.isMoz)this.$21o(this.getFocusSubItem());this.Super("parentVisibilityChanged",arguments);this.itemsVisibilityChanged();if(this.isVisible()&&this.autoFocus)this.focus()}
,isc.A.$mh=function isc_DynamicForm__allowNativeTextSelection(_1){var _2=this.$pf(_1);if(_2.item){var _3=_2.item.$mh(_1,_2);if(_3!=null)return _3}
return this.Super("$mh",arguments)}
,isc.A.prepareForDragging=function isc_DynamicForm_prepareForDragging(_1,_2,_3,_4){var _5=this.ns.EH;if(_5.dragTarget)return;var _6=_5.lastEvent,_7=this.$pf(_6);if(_7.item&&(_7.overElement||_7.overTextBox||_7.overControlTable))return false;return this.invokeSuper(isc.DynamicForm,"prepareForDragging",_1,_2,_3,_4)}
,isc.A.$pf=function isc_DynamicForm__getEventTargetItemInfo(_1){if(!_1)_1=isc.EH.lastEvent;var _2=isc.EH.isMouseEvent(_1.eventType)?_1.nativeTarget:_1.nativeKeyTarget;var _3=isc.DynamicForm.$ot(_2,this);_1.itemInfo=_3;return _3}
,isc.A.getEventItem=function isc_DynamicForm_getEventItem(){var _1=isc.EH.lastEvent.itemInfo;if(_1!=null&&!_1.inactiveContext&&!_1.overTitle)return _1.item;return null}
,isc.A.getEventItemInfo=function isc_DynamicForm_getEventItemInfo(){var _1=this.$pf();if(_1==null||_1.inactiveContext)return null;return{item:_1.item,overItem:(_1.overElement||_1.overTextBox||_1.overControlTable),overTitle:_1.overTitle,icon:_1.overIcon}}
,isc.A.handleMouseStillDown=function isc_DynamicForm_handleMouseStillDown(_1,_2){if(isc.$dd)arguments.$de=this;var _3=this.$pf(_1),_4=((_3.overTitle||_3.inactiveContext)?null:_3.item);if(_4!=null){if(_4.form!=this)return;if(_4.mouseStillDown){if(_4.handleMouseStillDown(_1)==false)return false}}}
,isc.A.handleMouseDown=function isc_DynamicForm_handleMouseDown(_1,_2){var _3=this.$pf(_1),_4=(_3.overTitle?null:_3.item);if(_4!=null){if(_4.form!=this)return;_4.handleMouseDown(_1);if(isc.Browser.isSafari&&!_3.inactiveContext&&_3.overElement&&isc.isA.CheckboxItem(_4))
{_4.focusInItem()}}}
,isc.A.$21w=function isc_DynamicForm__itemMouseEvent(_1,_2){var _3=this.$21x,_4=this.$21y,_5=this.$21z,_6=_1.item,_7=_1.overTitle,_8=_1.overIcon;if(_1.inactiveContext!=null){_6=null;_7=null;_8=null}
if(_3&&_3.destroyed){_3=null;this.$21x=null;this.$21z=null;this.$21y=null}
if(_6&&_6.destroyed){_6=null;_7=null;_8=null}
this.$21x=_6;this.$21y=_7;this.$21z=_8;if(_2==isc.EH.MOUSE_OVER){if(_6){if(_7)_6.handleTitleOver();else{if(_8)this.$21z=null;_6.handleMouseOver()}}}else if(_2==isc.EH.MOUSE_OUT){if(_3){if(_4)_3.handleTitleOut();else{if(_5)_3.$210(_5);_3.handleMouseOut()}}}else{var _9=(_3!=_6||_4!=_7);if(_9){if(_3){if(_4)_3.handleTitleOut();else{if(_5)_3.$210(_5);_3.handleMouseOut()}}
if(_6){if(_7)_6.handleTitleOver();else{if(_8)_6.$211(_8);_6.handleMouseOver()}}}else{if(_7)_6.handleTitleMove();else{if(_5!=_8){if(_5)_6.$210(_5);if(_8)_6.$211(_8)}else if(_6){if(_8)_6.$212(_8);_6.handleMouseMove()}}}}}
,isc.A.handleMouseOver=function isc_DynamicForm_handleMouseOver(_1,_2){if(this.mouseOver&&this.mouseOver(_1,_2)==false)return false;this.$21w(this.$pf(_1),isc.EH.MOUSE_OVER)}
,isc.A.handleMouseMove=function isc_DynamicForm_handleMouseMove(_1,_2){if(this.mouseMove&&this.mouseMove(_1,_2)==false)return false;var _3=this.$pf(_1);this.$21w(_3,isc.EH.MOUSE_MOVE)}
,isc.A.handleMouseOut=function isc_DynamicForm_handleMouseOut(_1,_2){this.$21w({},isc.EH.MOUSE_OUT);if(this.mouseOut&&this.mouseOut(_1,_2)==false)return false}
,isc.A.bubbleItemHandler=function isc_DynamicForm_bubbleItemHandler(_1,_2,_3,_4,_5,_6){var _7=this.getItemById(_1),_8=null;for(;_7!=null;_7=_7.parentItem){if(_7.form!=this)continue;if(_7[_2]!=null&&!isc.isA.Function(_7[_2])){isc.Func.replaceWithMethod(_7,_2,"arg1,arg2,arg3,arg4")}
if(_7[_2]==null){this.logWarn("handler:"+_2+" is not present on itemID "+_1);return false}
_8=_7[_2](_3,_4,_5,_6);if(_8==false)return _8}
return _8}
,isc.A.bubbleInactiveEditorEvent=function isc_DynamicForm_bubbleInactiveEditorEvent(_1,_2,_3){return this.bubbleItemHandler(_1,"$213",_2,_3.inactiveContext,_3)}
,isc.A.elementChanged=function isc_DynamicForm_elementChanged(_1){var _2=this.bubbleItemHandler(_1,"elementChanged",_1);return(_2!=false)}
,isc.A.handleClick=function isc_DynamicForm_handleClick(_1,_2){var _3=this.$pf(_1);if(this.editingOn){if(!_3||!_3.item||(!_3.inactiveContext&&!_3.overTitle&&!_3.overIcon&&!_3.overElement&&!_3.overTextBox&&!_3.overControlTable)){var _4=false;if(_3&&_3.item&&_3.item.isA("SpacerItem")){_4=true}
this.logWarn("No item clicked upon, passing the click to the DF","EventHandler");if(!_4)return this.Super("handleClick",arguments)}}
var _5;if(_3&&_3.item){var _6=_3.item;if(_6.isReadOnly())return this.Super("handleClick",arguments);if(_3.inactiveContext){this.logInfo("Bubbling inactive editor event for "+_6.ID,"EventHandler");_5=this.bubbleInactiveEditorEvent("click",_6,_3)}else{if(_3.overTitle){this.logInfo("Bubbling handleTitleClick event for "+_6.ID,"EventHandler");_5=this.bubbleItemHandler(_6,"handleTitleClick",_6)}else{var _7=(_3.overElement||_3.overTextBox||_3.overControlTable);_7=_7||_6.isA("SpacerItem");if(_3.overIcon&&(_6.form==this)){if(_6.$214(_3.overIcon)==false)
return false;var _8=_6.getIcon(_3.overIcon);if(_8&&_8.writeIntoItem)
_7=true}
if(_7)this.logInfo("Bubbling handleClick event for "+_6.ID,"EventHandler");if(_7&&this.bubbleItemHandler(_6,"handleClick",_6)==false){_5=false}else{if(_7)this.logInfo("Bubbling handleCellClick event for "+_6.ID,"EventHandler");_5=this.bubbleItemHandler(_6,"handleCellClick",_6)}}}}
if(_5==false||_5==isc.EH.STOP_BUBBLING)return _5;return this.Super("handleClick",arguments)}
,isc.A.handleDoubleClick=function isc_DynamicForm_handleDoubleClick(_1,_2){var _3=this.$pf(_1);var _4;if(_3&&_3.item){var _5=_3.item;if(_3.inactiveContext){_4=this.bubbleInactiveEditorEvent(_5,"doubleClick",_3)}else if(_3.overTitle){_4=this.bubbleItemHandler(_5,"handleTitleDoubleClick",_5)}else{var _6=(_3.overElement||_3.overTextBox||_3.overControlTable);if(_3.overIcon){if(_5.$214(_3.overIcon)==false)return false;var _7=_5.getIcon(_3.overIcon);if(_7&&_7.writeIntoItem)_6=true}
if(_6&&this.bubbleItemHandler(_5,"handleDoubleClick",_5)==false){_4=false}else{_4=this.bubbleItemHandler(_5,"handleCellDoubleClick",_5)}}}
if(_4==false||_4==isc.EH.STOP_BUBBLING)return _4;return this.Super("handleDoubleClick",arguments)}
,isc.A.elementFocus=function isc_DynamicForm_elementFocus(_1,_2){if(!this.hasFocus)isc.EventHandler.focusInCanvas(this);var _3=this.getItemById(_2);this.setFocusItem(_3);var _4=true,_5=false;if(this.$21t!=null){if(this.$21u!=_3){delete this.$21t;delete this.$21u}else{_5=true;this.$21t-=1;if(this.$21t<0){delete this.$21t;delete this.$21u}}}
_4=this.bubbleItemHandler(_2,"elementFocus",_5);return(_4!=false)}
,isc.A.elementBlur=function isc_DynamicForm_elementBlur(_1,_2){if(!isc.isA.FormItem(this.getItemById(_2)))return;var _3=true;if(this.$20v==null)_3=this.bubbleItemHandler(_2,"elementBlur");else{this.$20v-=1;if(this.$20v<0)delete this.$20v}
this.clearPrompt();return(_3!=false)}
,isc.A.handleKeyPress=function isc_DynamicForm_handleKeyPress(_1,_2){if(_1.keyName==this.$18n){if(this.saveOnEnter){var _3=this.getFocusSubItem();if(isc.isA.TextItem(_3))this.submit()}}
if(_1.characterValue!=null&&_1.characterValue!=0&&(_1.keyName!="Escape"))
{return isc.EventHandler.STOP_BUBBLING}
if(this.$190[_1.keyName]&&_1.keyTarget!=this){return isc.EventHandler.STOP_BUBBLING}
return this.Super("handleKeyPress",arguments)}
);isc.evalBoundary;isc.B.push(isc.A.handleKeyDown=function isc_DynamicForm_handleKeyDown(_1,_2){var _3
if(this.convertToMethod("keyDown")){_3=this.keyDown(_1,_2)}
var _4=isc.EH.getKey();if(_3!=false&&this.$190[_4]){return isc.EH.STOP_BUBBLING}
return _3}
,isc.A.itemHoverHTML=function isc_DynamicForm_itemHoverHTML(_1){if(_1.implementsPromptNatively)return null;var _2=_1.prompt;if(!_2&&_1.parentItem)_2=this.itemHoverHTML(_1.parentItem)
return _2}
,isc.A.titleHoverHTML=function isc_DynamicForm_titleHoverHTML(_1){if(_1.prompt)return _1.prompt;if(this.shouldClipTitle(_1))return _1.getTitle()}
,isc.A.$215=function isc_DynamicForm__showItemHover(_1,_2){if(_2&&!isc.is.emptyString(_2)){var _3=this.$1e(_1);isc.Hover.show(_2,_3,(_1.hoverRect||this.itemHoverRect))}else isc.Hover.clear()}
,isc.A.$1e=function isc_DynamicForm__getHoverProperties(_1){if(!isc.isA.FormItem(_1))_1=this.getItem(_1);var _2={};if(_1){_2=isc.addProperties({},{width:(_1.hoverWidth!=null?_1.hoverWidth:this.itemHoverWidth),height:(_1.hoverHeight!=null?_1.hoverHeight:this.itemHoverHeight),align:(_1.hoverAlign!=null?_1.hoverAlign:this.itemHoverVAlign),baseStyle:(_1.hoverStyle!=null?_1.hoverStyle:this.itemHoverStyle),opacity:(_1.hoverOpacity!=null?_1.hoverOpacity:this.itemHoverOpacity)})}else{_2=isc.addProperties({},{width:this.hoverWidth,height:this.hoverHeight,align:this.hoverAlign,baseStyle:this.hoverStyle,opacity:this.hoverOpacity})}
_2.moveWithMouse=this.hoverMoveWithMouse;return _2}
,isc.A.showPrompt=function isc_DynamicForm_showPrompt(_1){window.status=_1}
,isc.A.clearPrompt=function isc_DynamicForm_clearPrompt(){window.status=""}
,isc.A.isMultipart=function isc_DynamicForm_isMultipart(){return!(this.encoding==isc.DynamicForm.NORMAL||this.encoding==isc.DynamicForm.NORMAL_ENCODING)}
,isc.A.itemIsLastInRow=function isc_DynamicForm_itemIsLastInRow(_1,_2){var _3=this.items.$17s,_4=_3[_2],_5=this.getItems().indexOf(_1);if(!_4||_5<0)return false;if(_4[this.numCols-1]==_5)return true;return false}
,isc.A.getColumnWidths=function isc_DynamicForm_getColumnWidths(){var _1=this.items.$17s,_2=[];_2.length=this.numCols;for(var j=0;j<_2.length;j++)_2[j]=0;for(var _4=0;_4<_1.length;_4++){var _5=_1[_4];for(var i=0;i<_5.length;i++){var _7=this.items.get(_5[i]);if(_7.colSpan&&_7.colSpan>1)continue;if(_7.showTitle&&(this.titleOrientation=="left"||!this.titleOrientation)){if(_7.getVisibleTitleWidth()>_2[i]){_2[i]=_7.getVisibleTitleWidth()}
i++}
if(_7.width>_2[i])_2[i]=_7.width;if(_7.showTitle&&_7.titleOrientation=="right"&&_7.getVisibleTitleWidth()>_2[i+1]){_2[++i]=_7.getVisibleTitleWidth()}}}
return _2}
,isc.A.getItemTableOffsets=function isc_DynamicForm_getItemTableOffsets(_1,_2){var _3=_2||this.items.$17s,_4=this.getItems().indexOf(_1),_5={};_5.itemIndex=_4
for(var _6=0;_6<_3.length;_6++){var _7=_3[_6],_8=_7.indexOf(_4),_9=_7.lastIndexOf(_4);if(_8>-1&&_9>-1){if(!_5.left||_8<_5.left)_5.left=_8;if(!_5.width||_5.width<_9-_8)_5.width=_9-_8+1;if(!_5.top||_6<_5.top)_5.top=_6;if(!_5.height||_5.height<_6-_5.top){_5.height=_6-_5.top+1}}}
return _5}
,isc.A.getItemDropIndex=function isc_DynamicForm_getItemDropIndex(_1,_2){if(!_1)return;if(!_2)_2="L";var _3=this.getItemTableOffsets(_1),_4=this.items.$17s;if(_2=="L")return _3.itemIndex;if(_2=="R"){if(this.itemIsLastInRow(_1)&&this.canAddColumns!=true){return _3.itemIndex+1}
return _3.itemIndex+1}
if(_2=="T"){return this.getItemIndexAtTableLocation(_3.top-(_3.top==0?0:1),_3.left)}
if(_2=="B"){var _5=_3.top+_3.height-1;var _6=this.getItemIndexAtTableLocation(_5+1,_3.left);if(_6==null){_6=this.items.length}
return _6}}
,isc.A.getItemIndexAtTableLocation=function isc_DynamicForm_getItemIndexAtTableLocation(_1,_2){var _3=this.items.$17s;if(!_3[_1])return;return _3[_1][_2]}
,isc.A.getItemAtPageOffset=function isc_DynamicForm_getItemAtPageOffset(_1,_2){this.items.$216=this.getColumnWidths();var _3=this.items.$17s,_4=this.items.$216,_5=this.items.$171;var _6=this.inWhichPosition(_4,_1-this.getPageLeft()),_7=this.inWhichPosition(_5,_2-this.getPageTop());_6=_6==-1?0:_6==-2?_4.length:_6;_7=_7==-1?0:_7==-2?_5.length:_7;if(!_3[_7])return null;var _8=_3[_7][_6],_9=this.getItem(_8);if(_9!=null){_9.$217=_7;_9.$218=_6;_9.$219=_8}
return _9}
,isc.A.getNearestItem=function isc_DynamicForm_getNearestItem(_1,_2){var _3=9999999999,_4;this.logDebug("Computing nearest item to ("+_1+","+_2+")","formItemDragDrop");for(var i=0;i<this.items.length;i++){var _6=this.items[i];var _7=_6.getPageRect(true),_8=_7[0],_9=_7[1],_10=_7[2],_11=_7[3],_12=0,_13=0;if(_1>=_8&&_1<=_8+_10&&_2>=_9&&_2<=_9+_11)
{return _6}
if(_1>_8){if(_1>_8+_10){_12=_1-(_8+_10)}}else{_12=_8-_1}
if(_2>_9){if(_2>_9+_11){_13=_2-(_9+_11)}}else{_13=_9-_2}
var _14=Math.sqrt(_12*_12+_13*_13);this.logDebug("Item "+_6.name+": (l,t,w,h) = "+_7,"formItemDragDrop");this.logDebug("XDelta: "+_12+", yDelta: "+_13+", straight line distance: "+_14,"formItemDragDrop");if(_14<_3){this.logDebug("Item "+_6.name+": distance is shorter than "+_3+", it is now the nearest item","formItemDragDrop");_3=_14;_4=_6}}
return _4}
,isc.A.showDragLineForItem=function isc_DynamicForm_showDragLineForItem(_1,_2,_3){this.makeDragLine();if(!_1){this._dragLine.hide();return}
var _4=_1.getPageRect(),_5=_4[0],_6=_4[1],_7=_4[2],_8=_1.getVisibleHeight(),_9=this.titleOrientation||"left";if(_1.showTitle!=false){if(_9=="left"||_9=="right")_7+=_1.getVisibleTitleWidth();if(_9=="left")_5-=_1.getVisibleTitleWidth()}
var _10;if(_2<=_5)_2=_5+1;else if(_2>=_5+_7){_2=_5+_7-1;_10=true}
var _11=_7/ 4;if(_11>20)_11=20;if(_3<=_6)_3=_6+1;else if(_3>=_6+_8)_3=_6+_8-1;var _12=_2-_5,_13=Math.round(_7/ _12),_14=_3-_6,_15=Math.round(_8/ _14),_16=(_5+_7)-_2,_17=Math.round(_7/ _16),_18=(_6+_8)-_3,_19=Math.round(_8/ _18),_20="R",_21,_22,_23,_24;_5--;_6--;if(_10||(Math.min(_13,_17)<Math.min(_15,_19)&&((_13>_17&&_12<_11)||(_17>_13&&_16<_11)))){_20=_10?"R":_13>_17?"L":"R";_22=3;_21=_8;_23=_20=="L"?_5:_5+_7-1;_24=_6}else{_20=_15>_19?"T":"B";_22=_7;_23=_5;_21=3;_24=_20=="T"?_6:_6+_8-1}
_1.dropSide=_20;if(this.itemIsLastInRow(_1,_1.$217)&&!this.canAddColumns&&_1.dropSide=="R"){this.hideDragLine();this.setNoDropIndicator();this.$22a=this.currentCursor;this.setCursor("not-allowed")}
else{if(this.$y9){this.clearNoDropIndicator()
this.setCursor(this.$22a)}
var _25={left:_23,top:_24};this.adjustDragLinePosition(_25,_1,_20);_23=_25.left;_24=_25.top;this._dragLine.resizeTo(_22,_21);this._dragLine.setPageRect(_23,_24);this._dragLine.bringToFront();this._dragLine.show()}}
,isc.A.adjustDragLinePosition=function isc_DynamicForm_adjustDragLinePosition(_1,_2,_3){var _4=this.items.$17s,_5=this.items.indexOf(_2),_6,_7,_8;for(var i=0;i<_4.length;i++){if(_4[i].indexOf(_5)!=-1){_6=i;_7=_4[i].indexOf(_5);_8=_4[i].lastIndexOf(_5);break}}
if(_6==null||_7==null||_8==null)return;if(_3=="T"){if(_6==0)return;if(_4[_6-1][_7]==_4[_6-1][_8]&&_4[_6-1][_7-1]!=_4[_6-1][_7]&&_4[_6-1][_8+1]!=_4[_6-1][_7])
{var _10=this.items[_4[_6-1][_7]].getPageRect(true);var _11=_10[1]+_10[3];_1.top-=Math.round((_1.top-_11)/2)}}
if(_3=="B"){if(_6==_4.length-1)return;if(_4[_6+1][_7]==_4[_6+1][_8]&&_4[_6+1][_7-1]!=_4[_6+1][_7]&&_4[_6+1][_8+1]!=_4[_6+1][_7])
{var _10=this.items[_4[_6+1][_7]].getPageRect(true);var _11=_10[1];_1.top+=Math.round((_11-_1.top)/2)}}
if(_3=="L"){if(_7==0)return;var _10=this.items[_4[_6][_7-1]].getPageRect(true);var _12=_10[0]+_10[2];_1.left-=Math.round((_1.left-_12)/2)}
if(_3=="R"){if(_8==_4[_6].length-1)return;var _10=this.items[_4[_6][_8+1]].getPageRect(true);var _12=_10[0];_1.left+=Math.round((_12-_1.left)/2)}}
,isc.A.showDragLineForForm=function isc_DynamicForm_showDragLineForForm(){this.makeDragLine();this._dragLine.resizeTo(3,this.getHeight());this._dragLine.setPageRect(this.getPageLeft(),this.getPageTop());this._dragLine.bringToFront();this._dragLine.show()}
,isc.A.enableField=function isc_DynamicForm_enableField(_1){if(_1==null||isc.isAn.emptyString(_1))return;var _2=this.getItem(_1);if(_2)_2.enable()}
,isc.A.disableField=function isc_DynamicForm_disableField(_1){if(_1==null||isc.isAn.emptyString(_1))return;var _2=this.getItem(_1);if(_2)_2.disable()}
,isc.A.showField=function isc_DynamicForm_showField(_1){if(_1==null||isc.isAn.emptyString(_1))return;var _2=this.getItem(_1);if(_2)_2.show()}
,isc.A.hideField=function isc_DynamicForm_hideField(_1){if(_1==null||isc.isAn.emptyString(_1))return;var _2=this.getItem(_1);if(_2)_2.hide()}
,isc.A.getSelectionChain=function isc_DynamicForm_getSelectionChain(){if(!this.selectionComponent)return[];var _1=[];var _2=this;while(_2.selectionComponent){_1.add(_2.selectionComponent);_2=_2.selectionComponent}
var _3=[];for(var i=_1.length-1;i>=0;i--){_3.add(_1[i].getRecordIndex(_1[i].getSelectedRecord()))}
return _3}
,isc.A.setCanEdit=function isc_DynamicForm_setCanEdit(_1){this.canEdit=_1;if(this.isDrawn())this.markForRedraw("setCanEdit")}
,isc.A.setFieldCanEdit=function isc_DynamicForm_setFieldCanEdit(_1,_2){if(_1==null||isc.isAn.emptyString(_1))return;var _3=this.getField(_1);if(_3){if(_3.setCanEdit)_3.setCanEdit(_2);else{_3.canEdit=_2;this.redraw()}}}
);isc.B._maxIndex=isc.C+234;isc.A=isc.DynamicForm;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.defaultFieldType="text";isc.A.$22b="link";isc.A.$ia="text";isc.A.$22c="select";isc.A.$22d="checkbox";isc.A.$22e="staticText";isc.A.$if="boolean";isc.A.$22f="binary";isc.A.$22g="blob";isc.A.$22h="multifile";isc.A.$22i="multiupload";isc.A.$18x="upload";isc.A.$22j="file";isc.A.$22k="base64Binary";isc.A.$22l="enum";isc.A.$22m="CycleItem";isc.A.$22n="selectOther";isc.A.$22o="relation";isc.A.$22p="NestedEditorItem";isc.A.$22q="NestedListEditorItem";isc.A.$22r="imageFile";isc.A.$22s="ViewFileItem";isc.A.$22t="section";isc.A.$22u="SectionItem";isc.A.$22v="button";isc.A.$22w="ButtonItem";isc.A.$22x="id";isc.A.$refPropName="__ref";isc.B.push(isc.A.getEditorType=function isc_c_DynamicForm_getEditorType(_1,_2){if(_1._constructor==isc.FormItem.Class)_1._constructor=null;var _3=_2.getDataSource();var _4=this.canEditField(_1,_2),_5=this.defaultFieldType,_6=(_4==false&&_1.readOnlyEditorType)||_1.editorType||_1.formItemType||_1._constructor||_1.type||_5;if((_4==false&&_1.readOnlyEditorType)||_1.editorType||_1.formItemType||_1._constructor)
{return _6}
if(_6==this.$22b){if(!this.canEditField(_1,_2))_6=this.$22b;else _6=this.$ia}else if(!_4){if(_6==this.$22f||_6==this.$22j||_6==this.$22r)
_6=this.$22s;else if(_6!=this.$22t&&_6!=this.$22u&&_6!=this.$22v&&_6!=this.$22w)
{_6=this.$22e}}else if(_6==this.$if){var _7=_1.valueMap;if(!isc.isAn.Array(_7)&&isc.isAn.Object(_7))_6=this.$22c;else _6=this.$22d}else if(_6==this.$22f||_6==this.$22g||_6==this.$22j||_6==this.$22r)
{if(_1.dataSource)_6=this.$22h
else _6=this.$22j}else if(_6==this.$22i){_6=this.$22h}else if(_6==this.$22k){_6=this.$22k}else if(_6==this.$22l){if(_1.showValueIconOnly)_6=this.$22m
else _6=this.$22c}else if(isc.DataSource&&isc.isA.DataSource(_3)&&_3.fieldIsComplexType(_1.name)){_6=_1.multiple?_2.nestedListEditorType:_2.nestedEditorType}else{if(!_1.type||(_1.type==_5)||(_1.type!=this.$22n&&(isc.FormItemFactory.getItemClass(_1.type)==null)))
{if(_1.dataSource){_6=this.$22o}else if(_1.valueMap||_1.optionDataSource||_1.displayField){_6=(_1.showValueIconOnly?this.$22m:this.$22c)}else if(_2&&(_1.length&&_1.length>_2.longTextEditorThreshold))
{_6=_2.longTextEditorType}else{_6=_5}}}
return _6}
,isc.A.canEditField=function isc_c_DynamicForm_canEditField(_1,_2){if(_2&&_2.canEditField)return _2.canEditField();var _3=(_2&&_2.canEditFieldAttribute)||"canEdit";if(_1[_3]!=null)return _1[_3];if(_2.canEdit!=null)return _2.canEdit;if(_1.canSave==false&&(!_2||!_2.$22y)){return false}
return true}
,isc.A.$ot=function isc_c_DynamicForm__getItemInfoFromElement(_1,_2){var _3=_2?_2.getClipHandle():document,_4={},_5=isc.DynamicForm.$18o,_6=isc.DynamicForm.$18p,_7=isc.DynamicForm.$18q,_8=isc.DynamicForm.$18r,_9=isc.DynamicForm.$18t,_10=isc.DynamicForm.$18v;while(_1&&_1!=_3&&_1!=document){var _11=_1.getAttribute?_1.getAttribute(_5):null;if(_11!=null&&!isc.isAn.emptyString(_11)){var _12=window[_11];if(_12&&!_12.destroyed){_4.item=_12;var _13=_12.$22z(_1);if(_13!=null){if(this.logIsDebugEnabled("inactiveEditorHTML")){this.logDebug("Event occurred over inactive HTML for item:"+_12+" inactiveContext:"+this.echo(_13),"inactiveEditorHTML")}
_4.inactiveContext=_13}
var _14=_1.getAttribute(_6);if(_14==_7)_4.overElement=true;else if(_14==_10)_4.overTitle=true;else if(_14==_8)_4.overTextBox=true;else if(_14==_9)_4.overControlTable=true;else if(_14&&!isc.isAn.emptyString(_14))
_4.overIcon=_14;break}}
_1=_1.parentNode}
return _4}
,isc.A.getSimpleErrors=function isc_c_DynamicForm_getSimpleErrors(_1){var _2={};if(isc.isAn.Array(_1))_1=_1[0];for(var _3 in _1){var _4=_1[_3];if(_3=="recordPath"&&!isc.isAn.Object(_4))continue;if(isc.isAn.Array(_4)){_2[_3]=[];for(var i=0;i<_4.length;i++){var _6=_4[i];_2[_3][i]=isc.isAn.Object(_6)?isc.shallowClone(_6):{errorMessage:_6}}}else{_2[_3]=isc.isAn.Object(_4)?isc.shallowClone(_4):{errorMessage:_4}}}
return _2}
,isc.A.formatValidationErrors=function isc_c_DynamicForm_formatValidationErrors(_1){var _2={};if(isc.isAn.Array(_1))_1=_1[0];for(var _3 in _1){var _4=_1[_3];if(_3=="recordPath"&&!isc.isAn.Object(_4))continue;if(isc.isAn.Array(_4)){_2[_3]=[];for(var i=0;i<_4.length;i++){var _6=_4[i];if(isc.isAn.Object(_6))_6=_6.errorMessage;_2[_3][i]=_6}}else{_2[_3]=isc.isAn.Object(_4)?_4.errorMessage:_4}}
return _2}
,isc.A.compareValues=function isc_c_DynamicForm_compareValues(_1,_2){if(_1==_2)return true;if(isc.isA.Date(_1)&&isc.isA.Date(_2))
return(Date.compareDates(_1,_2)==0);else if(isc.isAn.Array(_1)&&isc.isAn.Array(_2)){return _1.equals(_2)}else{if(isc.isA.Number(_1)||isc.isA.String(_1)||isc.isA.Boolean(_1)){_1=_1.valueOf()}
if(isc.isA.Number(_2)||isc.isA.String(_2)||isc.isA.Boolean(_2)){_2=_2.valueOf()}
if(_1==_2)return true;if(isc.isAn.Object(_1)&&isc.isAn.Object(_2)){var _3=isc.addProperties({},_2);for(var _4 in _1){if(_2[_4]!=_1[_4])return false;delete _3[_4]}
for(var _4 in _3){return false}
return true}}
return false}
,isc.A.valuesHaveChanged=function isc_c_DynamicForm_valuesHaveChanged(_1,_2,_3,_4,_5){var _6=false,_7={};for(var _8 in _3){if(isc.isA.Function(_3[_8]))continue;if(_8==this.$220)continue;if(isc.isAn.Instance(_3[_8])||isc.isA.Class(_3[_8]))continue;var _9=_5==null?_8:_5+"/"+_8;var _10=_1.getItem(_9);if(_10!=null){_6=!_10.compareValues(_3[_8],_4[_8]);if(_6&&_2)_7[_8]=_3[_8]}else{var _11=_3[_8],_12=_4[_8];var _13=isc.isA.Object(_11),_14=isc.isAn.Object(_12);if(_13&&(isc.isA.Number(_11)||isc.isA.String(_11)||isc.isA.Boolean(_11)))
{_11=_11.valueOf();_13=false}
if(_14&&(isc.isA.Number(_12)||isc.isA.String(_12)||isc.isA.Boolean(_12)))
{_12=_12.valueOf();_14=false}
if(_13&&!isc.isAn.Array(_11)&&!isc.isA.Date(_11)&&_14&&!isc.isAn.Array(_12)&&!isc.isA.Date(_12))
{var _15=this.valuesHaveChanged(_1,_2,_3[_8],_4[_8],_9);if(!_2&&_15){_6=true;break}else if(!isc.isAn.emptyObject(_15)){if(_7[_8]==null)_7[_8]={};isc.addProperties(_7[_8],_15)}}else{_6=!isc.DynamicForm.compareValues(_11,_12);if(_6&&_2)_7[_8]=_11}}
if(_6&&!_2){return true}}
return(_2?_7:_6)}
,isc.A.getFilterCriteria=function isc_c_DynamicForm_getFilterCriteria(){var _1={};for(var i=0;i<arguments.length;i++){var _3=arguments[i];if(_3==null)continue;isc.addProperties(_1,_3.getFilterCriteria())}
return _1}
,isc.A.$203=function isc_c_DynamicForm__getTopRowCellStart(){if(!this.$221){isc.Canvas.$rk.add({target:this,methodName:"$222"});this.$221=true}
if(this.$223==null){this.$223=["<TD style='",isc.Canvas.$rr,"height:0px;overflow:hidden;padding:0px;' class='",null,"'>",(isc.Browser.isSafari?"<div style='overflow:hidden;height:0px'>":"")]}
return this.$223}
,isc.A.$209=function isc_c_DynamicForm__getTitleInnerTableTemplate(){if(!this.$221){isc.Canvas.$rk.add({target:this,methodName:"$222"});this.$221=true}
if(this.$224==null){this.$224=["<TABLE height=",," border=0 cellspacing=0 cellpadding=0><tr><td class='",,"' style='"+isc.Canvas.$rr+"' ALIGN='",,"'>",null]}
return this.$224}
,isc.A.$222=function isc_c_DynamicForm__doublingStringsChanged(){this.$223=null;this.$224=null}
);isc.B._maxIndex=isc.C+11;isc.defineClass("InlineFormItem","DynamicForm");isc.A=isc.InlineFormItem.getPrototype();isc.A.position="relative";isc.A.writeFormTag=false;isc.A.canSubmit=true;isc.A.numCols=1;isc.A.autoDraw=true;isc.A=isc.InlineFormItem;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.create=function isc_c_InlineFormItem_create(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13){var _14=isc.addProperties({showTitle:false,validate:function(){this.form.validate()},destroy:function(){this.form.destroy()}},_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13);var _15=this.createRaw().completeCreation({fields:[_14],valuesManager:_14.valuesManager},_14.formProperties);return _15.getItem(0)}
);isc.B._maxIndex=isc.C+1;isc.A=isc.DynamicForm;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.makeInlineItem=function isc_c_DynamicForm_makeInlineItem(_1,_2,_3,_4){return isc.InlineFormItem.create({name:_1,type:_2,formProperties:_4},_3)}
,isc.A.getFormValues=function isc_c_DynamicForm_getFormValues(_1){return isc.Canvas.getFormValues(_1)}
);isc.B._maxIndex=isc.C+2;isc.DynamicForm.registerStringMethods({itemChanged:"item,newValue",itemChange:"item,newValue,oldValue",itemKeyPress:"item,keyName,characterValue",submitValues:"values,form",handleHiddenValidationErrors:"errors"});isc.ClassFactory.defineClass("FormItem");isc.A=isc.FormItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$225=[null,"_inactiveContext",null];isc.A.$wf=isc.Canvas.getPrototype().$wf;isc.A.$v9=isc.Canvas.getPrototype().$v9;isc.A.reuseDOMIDs=false;isc.B.push(isc.A.$t4=function isc_FormItem__getDOMID(_1,_2,_3,_4){if(_4==null&&this.isInactiveHTML()){_4=this.$226}
if(_4!=null){this.$225[0]=_1;this.$225[2]=_4;_1=this.$225.join(isc.emptyString);if(this.logIsDebugEnabled("inactiveEditorHTML")){this.logDebug("$t4 called for inactive HTML -- generated partName:"+_1,"inactiveEditorHTML")}
_2=false}
return isc.Canvas.getPrototype().$t4.apply(this,[_1,_2,_3])}
);isc.B._maxIndex=isc.C+1;isc.A=isc.FormItem;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.create=function isc_c_FormItem_create(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13){this.logWarn("Unsupported call to "+this.getClassName()+".create(). FormItems must be created "+"by their containing form. To create form items, use the 'items' property of a DynamicForm "+"instance. See documentation for more details.");return isc.addProperties({},_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13)}
,isc.A.getNewTagID=function isc_c_FormItem_getNewTagID(){if(this.$227==null)this.$227=0;this.$227+=1;return"isc_FormItemElement_ID_"+this.$227}
,isc.A.setElementTabIndex=function isc_c_FormItem_setElementTabIndex(_1,_2){_1.tabIndex=_2;if(isc.Browser.isMoz){_1.style.MozUserFocus=(_2<0?"ignore":"normal")}}
,isc.A.$228=function isc_c_FormItem__aboutToFireNativeElementFocus(_1){if(!isc.Browser.isIE)return;var _2=this.getActiveElement();if(_2&&_2.tagName==null)_2=null;if(_2&&((_2.tagName.toLowerCase()==this.$229&&_2.type.toLowerCase()==this.$23a)||_2.tagName.toLowerCase()==this.$23b))
{var _3=_2.createTextRange();_3.execCommand("Unselect")}}
,isc.A.$23c=function isc_c_FormItem__textBasedItem(_1,_2){if(isc.isA.FormItem(_1))_1=_1.getClassName();if(!this.$23d){this.$23d={text:true,TextItem:true,textItem:true,textArea:true,TextAreaItem:true,textAreaItem:true}
this.$23e={popUpTextArea:true,PopUpTextAreaItem:true,popUpTextAreaItem:true}}
return this.$23d[_1]||(!_2||this.$23e[_1])}
,isc.A.$23f=function isc_c_FormItem__nativeFocusHandler(){if(!window.isc||!isc.DynamicForm)return;isc.EH.$jp("IFCS");var _1;if(isc.Log.supportsOnError){_1=isc.FormItem.$23g(this)}else{try{_1=isc.FormItem.$23g(this)}catch(e){isc.Log.$a3(e)}}
isc.EH.$jq();return _1}
,isc.A.$23g=function isc_c_FormItem___nativeFocusHandler(_1){var _2=isc.DynamicForm.$ot(_1),_3=_2.item;if(_3&&_3.isDisabled()){_1.blur();return}
if(_3){return _3.$23h(_1,_3)}
isc.EH.$jq()}
,isc.A.$23i=function isc_c_FormItem__nativeBlurHandler(){if(!window.isc||!isc.DynamicForm)return;isc.EH.$jp("IBLR");var _1;if(isc.Log.supportsOnError){_1=isc.FormItem.$23j(this)}else{try{_1=isc.FormItem.$23j(this)}catch(e){isc.Log.$a3(e)}}
isc.EH.$jq();return _1}
,isc.A.$23j=function isc_c_FormItem___nativeBlurHandler(_1){var _2=isc.DynamicForm.$ot(_1),_3=_2.item;if(_3&&_3.hasFocus){return _3.$23k(_1,_3)}}
,isc.A.$23l=function isc_c_FormItem__nativeCutPaste(){if(!window.isc)return;var _1=this,_2=isc.DynamicForm.$ot(_1),_3=_2.item;if(_3&&_3.hasFocus){return _3.$23l(_1,_3)}}
,isc.A.$23m=function isc_c_FormItem__nativeChangeHandler(){if(!window.isc||!isc.DynamicForm)return;var _1=this,_2=isc.DynamicForm.$ot(_1),_3=_2.item;if(_3)return _3.$23n()}
,isc.A.$23o=function isc_c_FormItem__nativeIconFocus(){var _1=this,_2=isc.DynamicForm.$ot(_1),_3=_2.item,_4=_2.overIcon;if(_3){if(_3.iconIsDisabled(_4))_1.blur();else return _3.$23p(_4,_1)}}
,isc.A.$23q=function isc_c_FormItem__nativeIconBlur(){if(!window.isc)return;var _1=this,_2=isc.DynamicForm.$ot(_1),_3=_2.item,_4=_2.overIcon;if(_3&&!_3.iconIsDisabled(_4))return _3.$23r(_4,_1)}
,isc.A.$23s=function isc_c_FormItem__nativeIconClick(){return false}
,isc.A.getErrorPromptString=function isc_c_FormItem_getErrorPromptString(_1){var _2="";if(!isc.isAn.Array(_1))_1=[_1];for(var i=0;i<_1.length;i++){_2+=(i>0?"<br>":"")+_1[i].asHTML()};return _2}
,isc.A.$23t=function isc_c_FormItem__getOuterTableStartTemplate(){if(!this.$221){isc.Canvas.$rk.add({target:this,methodName:"$222"});this.$221=true}
if(this.$23u==null){this.$23u=["<TABLE role='presentation' CELLSPACING=0 CELLPADDING=0 BORDER=0 ID='",,"' STYLE='"+isc.Canvas.$rr,,"' CLASS='",,"'><TR>",,"<TD style='",,"' VALIGN=",,">"]}
return this.$23u}
,isc.A.$23v=function isc_c_FormItem__getIconsCellTemplate(){if(!this.$221){isc.Canvas.$rk.add({target:this,methodName:"$222"});this.$221=true}
if(this.$23w==null){this.$23w=["</TD><TD VALIGN=",," WIDTH=",," style='"+isc.Canvas.$rr+"' class='",,"'>",null]}
return this.$23w}
,isc.A.$222=function isc_c_FormItem__doublingStringsChanged(){this.$23u=null;this.$23w=null}
);isc.B._maxIndex=isc.C+18;isc.A=isc.FormItem;isc.A.$229="input";isc.A.$23a="text";isc.A.$23b="textarea";isc.A=isc.FormItem.getPrototype();isc.A.emptyDisplayValue="";isc.A.multipleValueSeparator=", ";isc.A.fetchMissingValues=true;isc.A.alwaysFetchMissingValues=false;isc.A.useShortDateFormat=true;isc.A.valueIconSize=16;isc.A.valueIconLeftPadding=0;isc.A.valueIconRightPadding=3;isc.A.showFocusedPickerIcon=false;isc.A.pickerIconHSpace=0;isc.A.pickerIconName="picker";isc.A.pickerIconSrc="";isc.A.visible=true;isc.A.accessKey=null;isc.A.changeOnKeypress=true;isc.A.maintainSelectionOnTransform=true;isc.A.dirtyOnKeyDown=true;isc.A.showTitle=true;isc.A.width="*";isc.A.height=20;isc.A.titleColSpan=1;isc.A.colSpan=1;isc.A.rowSpan=1;isc.A.browserInputTypeMap={"text":"text","email":"email","url":"url","tel":"tel","phone":"tel","number":"[0-9]*","zip":"[0-9]*"};isc.A.defaultIconSrc="[SKIN]/DynamicForm/default_formItem_icon.gif";isc.A.iconHSpace=3;isc.A.iconVAlign=isc.Canvas.BOTTOM;isc.A.iconHeight=20;isc.A.iconWidth=20;isc.A.iconPrompt="";isc.A.showIcons=true;isc.A.redrawOnShowIcon=true;isc.A.errorIconHeight=16;isc.A.errorIconWidth=16;isc.A.errorIconSrc="[SKIN]/DynamicForm/validation_error_icon.png";isc.A.showHint=true;isc.A.showFocused=false;isc.A.showDisabled=true;isc.A.cellStyle="formCell";isc.A.hintStyle="formHint";isc.A.titleStyle="formTitle";isc.A.$23x=false;isc.A=isc.FormItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$s6="height";isc.A.$s7="width";isc.A.$23y="colSpan";isc.A.$23z="rowSpan";isc.A.$tb="*";isc.A.$b2="_";isc.A.$230="value";isc.A.$231="dataElement";isc.A.$ta="%";isc.A.errorMessageWidth=80;isc.A.$232=1;isc.A.$233=new RegExp(".*_inactiveContext(.*)$");isc.A.$234="<DIV STYLE='position:absolute;left:";isc.A.$235="px;top:";isc.A.$236="px;width:";isc.A.$237="px;height:";isc.A.$238="px;' ID='";isc.A.$239="'>";isc.A.$24a="</DIV>";isc.A.$24b=["<SPAN style='white-space:nowrap;' eventProxy=",," "+isc.DynamicForm.$18o+"='",,"' ID='",,"'>"];isc.A.$24c="</SPAN>";isc.A.$24d="$24e";isc.A.$24f="$24g";isc.A.$24h=["<DIV isDisabledEventMask='true' style='overflow:hidden;position:absolute;width:",null,"px;height:",null,"px' "+isc.DynamicForm.$18o+"='",null,"' "+isc.DynamicForm.$18p+"='"+isc.DynamicForm.$18q+"' ID='",,"'>",(isc.Browser.isIE?isc.Canvas.spacerHTML(1600,100):"&nbsp;"),"</DIV>"];isc.A.$24i="hiddenDataElement";isc.A.$24j="control";isc.A.$24k="textBox";isc.A.$24l="pickerIconCell";isc.A.$24m="hintCell";isc.A.$24n="FormItemStyling";isc.A.$24o="deprecated";isc.A.$24p="Over";isc.A.$24q="Down";isc.A.$24r="Disabled";isc.A.$24s="valueIcon";isc.A.$24t="</TD></TR></TABLE>";isc.A.$24u=["<TABLE role='presentation' ID='",,"' "+isc.DynamicForm.$18o+"='",,"' "+isc.DynamicForm.$18p+"='"+isc.DynamicForm.$18t,"' CELLPADDING=0 CELLSPACING=0 STYLE='",,"' CLASS='",,"'><TR><TD style='",,"'>",,"</TD><TD ID='",,"' CLASS='",,"' STYLE='",,"'>",,"</TD></TR></TABLE>"];isc.A.$24v=["</TD><TD ID='",,"' CLASS='",,"'>"];isc.A.$24w="$24x";isc.A.$24y="white-space:normal;";isc.A.$24z="white-space:nowrap;";isc.A.$240="min-width:";isc.A.$241="min-height:";isc.A.$19h="width:";isc.A.$19g="height:";isc.A.$11i="px;";isc.A.$px=";";isc.A.$242={};isc.A.$243="cursor:default;";isc.A.$244="overflow:hidden;";isc.A.$245="text-align:";isc.A.$246="font-size:";isc.A.$247=" ACCESSKEY='";isc.A.$248=" TABINDEX='";isc.A.$r9="'";isc.A.$249=["<DIV ID='",,"' "+isc.DynamicForm.$18o+"='",,"' "+isc.DynamicForm.$18p+"='"+isc.DynamicForm.$18r,"' CLASS='",,"' STYLE='",,"'",,">",,,"</DIV>"];isc.A.$r5="drawing";isc.A.$25a="#";isc.A.$25b="<table role='presentation' cellpadding=0 cellspacing=0 margin=0><tr>";isc.A.$25c="vertical-align:";isc.A.$25d={};isc.A._$_iLink_="_iLink_";isc.A._$_iImg_="_iImg_";isc.A.$25e="cell";isc.A.applyStaticTypeFormat=true;isc.A.$25f="smart";isc.A.$25g={"Tab":true,"Arrow_Left":true,"Arrow_Right":true,"Arrow_Up":true,"Arrow_Down":true,"Home":true,"End":true,"Page_Up":true,"Page_Down":true,"Enter":true};isc.A.shouldSaveValue=true;isc.A.$25h="character";isc.A.$25i="EndToEnd";isc.A.$25j="EndToStart";isc.A.$25h="character";isc.A.$25k={};isc.A.$212=isc.Class.NO_OP;isc.A.$18n="Enter";isc.A.$25l="img";isc.A.$kw="Tab";isc.A.$25m="$25n";isc.A.$25o={colSpan:true,rowSpan:true,startRow:true,endRow:true,showTitle:true,showHint:true};isc.A.$25p={baseStyle:true,showErrorStyle:true,showFocused:true,showErrorStyle:true,controlStyle:true,pickerIconStyle:true,textBoxStyle:true};isc.A.$25q="itemCellStyle";isc.A.useWildCardsByDefault=true;isc.A.$25r="ZZZZZZZZZZ";isc.B.push(isc.A.init=function isc_FormItem_init(){if(isc.$dd)arguments.$de=this;if(this.ID==null||window[this.ID]!=this){isc.ClassFactory.addGlobalID(this)}
if(this.options&&!this.valueMap){this.valueMap=this.options;delete this.options}
this.$25s(this.$s6);this.$25s(this.$s7);this.$25s(this.$23y);this.$25s(this.$23z);this._value=this.getDefaultValue();this.$25t=true;this.$25u();if((!this.validateOnExit||!this.synchronousValidation)&&this.validators&&this.validators.length>0)
{for(var i=0;i<this.validators.length;i++){if(this.validators[i].stopOnError){this.validateOnExit=true;this.synchronousValidation=true;break}}}
if((!this.validateOnExit||!this.synchronousValidation)&&((this.stopOnError==null&&this.form&&this.form.stopOnError)||this.stopOnError))
{this.validateOnExit=true;this.synchronousValidation=true}
this.onInit(this)}
,isc.A.onInit=function isc_FormItem_onInit(_1){}
,isc.A.$25s=function isc_FormItem__convertRawToMeasure(_1){var _2=this[_1];if(_2==null||isc.isA.Number(_2)||_2==this.$tb)return _2;var _3=parseInt(_2);if(_3==_2){this[_1]=_3;return _2}
return _2}
,isc.A.destroy=function isc_FormItem_destroy(){this.invalidateDisplayValueCache(true);if(this.isDrawn())this.cleared();var _1=this.pickList;this.pickList=null;if(_1!=null){if(_1.formItem==this)delete _1.formItem;if(_1.isVisible())_1.hide();if(!this.reusePickList())_1.destroy()}
this.destroyed=true;this.form=null;this.$25v=null;var _2;isc.ClassFactory.dereferenceGlobalID(this);this.$v9();if(isc.EH.$25w==this)isc.EH.$25w=null}
,isc.A.clear=function isc_FormItem_clear(){if(this.picker)this.picker.clear()}
,isc.A.toString=function isc_FormItem_toString(){var _1=this.getFieldName(),_2=this.ID,_3="["+this.Class+" instance "+(_1!=null?" name ='"+_1+"', ":"")+"global ID="+_2+"]";return _3}
,isc.A.getDataSource=function isc_FormItem_getDataSource(){if(isc.isA.String(this.dataSource))return isc.DS.get(this.dataSource);return this.dataSource}
,isc.A.registerWithDataView=function isc_FormItem_registerWithDataView(_1){if(!this.inputDataPath)return;if(!_1){_1=this.form;while(_1&&!isc.isA.DataView(_1))_1=_1.parentElement}
if(!_1){this.logWarn("Component initialized with an inputDataPath property, but no DataView "+"was found in the parent hierarchy. inputDataPath is only applicable to "+"DataBoundComponents and FormItems being managed by a DataView");return}
_1.registerItem(this)}
,isc.A.getFieldName=function isc_FormItem_getFieldName(){return this.name}
,isc.A.getDataPath=function isc_FormItem_getDataPath(){return this.dataPath}
,isc.A.getFullDataPath=function isc_FormItem_getFullDataPath(){var _1=this.getDataPath()||this.getFieldName();if(!_1){if(this.shouldSaveValue){this.logWarn("Encountered field with neither name nor dataPath: "+this.echo(this))}
_1=""}
if(!isc.isA.String(_1))_1=_1+"";if(_1.startsWith(isc.Canvas.$3a))return _1;var _2=this.form.getFullDataPath();if(_2&&_2!=isc.Canvas.$3a){return _2+isc.Canvas.$3a+_1}
return _1}
,isc.A.getItemName=function isc_FormItem_getItemName(){return this.getFieldName()}
,isc.A.getElementName=function isc_FormItem_getElementName(){if(this.isInactiveHTML())return"";var _1=this.getFieldName();if(this.parentItem){var _2=this.parentItem.getElementName();if(_1==isc.emptyString)_1=_2;else _1=[_2,this.$b2,_1].join(isc.emptyString)}
if(_1==null||_1==this.getID()||_1==isc.emptyString){_1=this.$t4(this.$230)}
return _1}
,isc.A.getDataElementId=function isc_FormItem_getDataElementId(){if(this.isInactiveHTML())return this.$t4(this.$231);if(this.$25x==null){this.$25x=this.$t4(this.$231,true)}
return this.$25x}
,isc.A.getItemID=function isc_FormItem_getItemID(){return this.getID()}
,isc.A.getID=function isc_FormItem_getID(){if(this.ID==null){isc.ClassFactory.addGlobalID(this)}
return this.ID}
,isc.A.shouldShowTitle=function isc_FormItem_shouldShowTitle(){return this.showTitle}
,isc.A.getTitleHTML=function isc_FormItem_getTitleHTML(){var _1=this.getTitle();if(!this.$mb())return _1;if(this.accessKey!=null){_1=isc.Canvas.hiliteCharacter(_1,this.accessKey)}
var _2;if(this.hasDataElement())_2=this.getDataElementId();if(!_2)return _1;return isc.SB.concat("<LABEL FOR=",_2,(this.accessKey!=null?" ACCESSKEY="+this.accessKey:isc.$ah),">",_1,"</LABEL>")}
,isc.A.getTitle=function isc_FormItem_getTitle(){var _1;if(this[this.form.titleField]!==_1)return this[this.form.titleField];return this[this.form.fieldIdProperty]}
,isc.A.getTitleOrientation=function isc_FormItem_getTitleOrientation(){return this.form.getTitleOrientation(this)}
,isc.A.isVisible=function isc_FormItem_isVisible(){if(!this.containerWidget.isVisible())return false;if(this.visible==false)return false;if(this.parentItem&&!this.parentItem.isVisible())return false;return true}
,isc.A.getRowSpan=function isc_FormItem_getRowSpan(){return this.rowSpan}
,isc.A.getColSpan=function isc_FormItem_getColSpan(){if(this.colSpan==0)this.colSpan=1;return this.colSpan}
,isc.A.getTitleColSpan=function isc_FormItem_getTitleColSpan(){if(this.titleColSpan==0)this.titleColSpan=1;return this.titleColSpan}
,isc.A.isStartRow=function isc_FormItem_isStartRow(){return this.startRow}
,isc.A.isEndRow=function isc_FormItem_isEndRow(){return this.endRow}
,isc.A.getRect=function isc_FormItem_getRect(){return[this.getLeft(),this.getTop(),this.getVisibleWidth(),this.getVisibleHeight()]}
,isc.A.getPageRect=function isc_FormItem_getPageRect(_1){if(_1)return this.getPageRectIncludingTitle();return[this.getPageLeft(),this.getPageTop(),this.getVisibleWidth(),this.getVisibleHeight()]}
,isc.A.getPeerRect=function isc_FormItem_getPeerRect(){return this.getPageRect()}
,isc.A.getPageRectIncludingTitle=function isc_FormItem_getPageRectIncludingTitle(){var _1=this.getPageLeft(),_2=this.getPageTop(),_3=this.getVisibleWidth(),_4=this.getVisibleHeight();if(this.showTitle){var _5=this.getTitlePageLeft(),_6=this.getTitlePageTop(),_7=this.getVisibleTitleWidth(),_8=this.form.getTitleHeight(this);;if(this.titleOrientation=="left"||this.titleOrientation=="left"||this.titleOrientation==null)
{_1=_1<_5?_1:_5;_3+=_7}else{_1=_1<_5?_1:_5;_3=_3>_7?_3:_7;if(isc.isA.Number(_8))_4+=_8}}
return[_1,_2,_3,_4]}
,isc.A.getCellHeight=function isc_FormItem_getCellHeight(_1){if(isc.$dd)arguments.$de=this;if(this.cellHeight!=null)return this.cellHeight;var _2=this.getHeight(_1);if(!isc.isA.Number(_2))return _2;var _3=this.getIconsHeight();if(_2<_3){_2=_3}
if(this.showPickerIcon&&this.pickerIconHeight){var _4=this.pickerIconHeight+this.$25y();if(_4>_2)_2=_4}
var _5=this.containerWidget;if(this.$200()||!isc.isA.DynamicForm(_5))return _2;_2+=this.$25z();if(this.showTitle&&this.form.getTitleOrientation(this)==isc.Canvas.TOP){_2+=this.form.getTitleHeight(this)}
return _2}
,isc.A.shouldFixRowHeight=function isc_FormItem_shouldFixRowHeight(){return this.cellHeight!=null}
,isc.A.$25z=function isc_FormItem__getCellVBorderPadSpacing(){var _1=0,_2=this.form,_3=this.getCellStyle();if(this.parentItem)_2=this.parentItem;_1+=2*_2.cellSpacing;var _4=isc.isA.Number(_2.cellPadding)?_2.cellPadding:0,_5=isc.Element.$q5(_3,true);if(_5==null)_5=_4
var _6=isc.Element.$q7(_3,true);if(_6==null)_6=_4;_1+=_5;_1+=_6;_1+=isc.Element.$q3(_3);return _1}
,isc.A.$250=function isc_FormItem__getCellHBorderPadSpacing(){var _1=0,_2=this.form,_3=this.getCellStyle();if(this.parentItem)_2=this.parentItem;if(isc.isA.Number(_2.cellSpacing))_1+=2*_2.cellSpacing;var _4=isc.isA.Number(_2.cellPadding)?_2.cellPadding:0,_5=isc.Element.$q8(_3,true);if(_5==null)_5=_4;var _6=isc.Element.$q9(_3,true);if(_6==null)_6=_4;_1+=_5;_1+=_6;_1+=isc.Element.$q4(_3);return _1}
,isc.A.getInnerHeight=function isc_FormItem_getInnerHeight(){var _1=this.containerWidget;if(this.$200())return this.$21g(this.height,true);if(this.$172==null&&this.height!=null&&isc.isA.String(this.height)&&this.containerWidget&&!isc.isA.DynamicForm(this.containerWidget)&&this.containerWidget.sizeFormItem!=null)
{this.containerWidget.sizeFormItem(this)}
if(this.$172){var _2=this.$172[1];if(!isc.isA.Number(_2))return _2;if(this.$251()){_2-=this.$25z()}
return _2}
return this.getHeight()}
,isc.A.getInnerWidth=function isc_FormItem_getInnerWidth(_1){var _2=this.containerWidget;if(this.$200())return this.$21g(this.width);if(this.$172==null&&this.width!=null&&isc.isA.String(this.width)&&this.containerWidget&&!isc.isA.DynamicForm(this.containerWidget)&&this.containerWidget.sizeFormItem!=null)
{this.containerWidget.sizeFormItem(this)}
var _3=this.$172?this.$172[0]:this.width;if(!isc.isA.Number(_3)){return _3}
if(this.$251()){_3-=this.$250()}
return _3}
,isc.A.getColWidth=function isc_FormItem_getColWidth(){var _1=this.form?this.form.items:null;if(_1&&_1.$170!=null&&this.$17v!=null){var _2=this.$17v[0],_3=this.$17v[2];if(this.showTitle){var _4=this.getTitleOrientation();if(_4==isc.Canvas.LEFT)_2+=1;else if(_4==isc.Canvas.RIGHT)_3-=1}
var _5=0;for(var c=_2;c<_3;c++){_5+=_1.$170[c]}
return _5}
return null}
,isc.A.$200=function isc_FormItem__absPos(){return(this.containerWidget.$200&&this.containerWidget.$200())}
,isc.A.$251=function isc_FormItem__writtenIntoCell(){return(this.containerItem!=null||(this.form==this.containerWidget&&!this.$200()))}
,isc.A.$21g=function isc_FormItem__getPercentCoord(_1,_2){if(isc.isA.String(_1)&&isc.endsWith(_1,this.$ta)){var _3=this.containerWidget,_4=_2?_3.getInnerHeight():_3.getInnerWidth();return Math.round((parseInt(_1,10)/100)*_4)}
return _1}
,isc.A.getElementWidth=function isc_FormItem_getElementWidth(){var _1=this.getInnerWidth();if(!isc.isA.Number(_1))return null;_1-=this.getTotalIconsWidth();return(isc.isA.Number(_1)?Math.max(_1,1):null)}
,isc.A.getTextBoxWidth=function isc_FormItem_getTextBoxWidth(_1){var _2=this.getElementWidth();if(!isc.isA.Number(_2))return _2;if(this.textBoxStyle){var _3=this.getTextBoxStyle();_2-=(isc.Element.$p5(_3)+isc.Element.$p6(_3));if(this.$252()){_2-=isc.Element.$rd(_3)}}
if(this.showPickerIcon){_2-=this.getPickerIconWidth();var _4=this.getPickerIcon();if(_4.hspace!=null)_2-=_4.hspace;if(this.pickerIconStyle)
_2-=isc.Element.$rd(this.getPickerIconStyle());if(this.controlStyle)
_2-=isc.Element.$rd(this.getControlStyle())}
if(this.hasDataElement()&&this.$253(_1)){_2-=((this.getValueIconWidth()||0)+(this.valueIconLeftPadding+this.valueIconRightPadding))}
return _2-this.$254()}
,isc.A.getErrorWidth=function isc_FormItem_getErrorWidth(){var _1=0;if(this.form.showInlineErrors&&this.hasErrors()){var _2=this.getErrorOrientation();if(_2==isc.Canvas.LEFT||_2==isc.Canvas.RIGHT){if(this.shouldShowErrorText()){_1+=this.errorMessageWidth}else if(this.shouldShowErrorIcon()){_1+=this.errorIconWidth+this.iconHSpace}}}
return _1}
,isc.A.$254=function isc_FormItem__getErrorWidthAdjustment(){var _1=this.getErrorWidth();if(_1!=0&&this.expandHintAndErrors&&(this.getColWidth()!=null)){var _2=this.getColWidth()-this.getInnerWidth();if(_2>0)_1-=_2;if(_1<0)_1=0}
return _1}
,isc.A.getValueIconHeight=function isc_FormItem_getValueIconHeight(){var _1=this.valueIconHeight;if(_1==null)_1=this.valueIconSize;return _1}
,isc.A.getValueIconWidth=function isc_FormItem_getValueIconWidth(){var _1=this.valueIconWidth;if(_1==null)_1=this.valueIconSize;return _1}
,isc.A.getTextBoxHeight=function isc_FormItem_getTextBoxHeight(){var _1=(isc.isA.Number(this.getHeight())?this.getHeight():this.getInnerHeight());if(!isc.isA.Number(_1))return _1;if(this.valueIcons!=null||this.getValueIcon!=null){var _2=this.getValueIconHeight();if(_2>_1)_1=_2}
if(this.textBoxStyle){var _3=this.getTextBoxStyle();_1-=(isc.Element.$p3(_3)+isc.Element.$p4(_3));if(this.$252()){_1-=isc.Element.$rc(_3)}}
if(this.showPickerIcon&&this.controlStyle){_1-=isc.Element.$rc(this.getControlStyle())}
if(this.showTitle&&this.form.getTitleOrientation(this)==isc.Canvas.TOP&&!isc.isA.Number(this.getCellHeight()))
{_1-=this.form.getTitleHeight(this)}
return _1}
,isc.A.$252=function isc_FormItem__sizeTextBoxAsContentBox(){return!isc.Browser.isBorderBox}
,isc.A.getPickerIconWidth=function isc_FormItem_getPickerIconWidth(){return(this.pickerIconWidth!=null?this.pickerIconWidth:this.getPickerIconHeight())}
,isc.A.getPickerIconHeight=function isc_FormItem_getPickerIconHeight(){if(this.pickerIconHeight!=null)return this.pickerIconHeight;else{var _1=(isc.isA.Number(this.getHeight())?this.getHeight():this.getInnerHeight());if(!isc.isA.Number(_1))return null;_1-=this.$25y();this.pickerIconHeight=_1;return _1}}
,isc.A.$25y=function isc_FormItem__getPickerIconVPad(){var _1=0;if(this.controlStyle){_1+=isc.Element.$rc(this.controlStyle)}
if(this.pickerIconStyle){_1+=isc.Element.$rc(this.pickerIconStyle)}
return _1}
,isc.A.getHeight=function isc_FormItem_getHeight(){return this.height}
,isc.A.getVisibleHeight=function isc_FormItem_getVisibleHeight(){var _1=this.isDrawn()?this.getOuterElement():null;if(_1==null){this.logInfo("getVisibleHeight() - unable to determine drawn height for this item -"+" returning pixel height from specified height","sizing");if(isc.isA.Number(this.height)){return this.height}
this.logWarn("getVisibleHeight() unable to determine height - returning zero","sizing");return 0}
return _1.offsetHeight}
,isc.A.getIconHeight=function isc_FormItem_getIconHeight(_1){if(_1==null&&this.icons!=null&&this.icons.getLength()>0)_1=this.icons[0];else if(!this.$255(_1)){this.logWarn("getIconHeight() passed invalid icon:"+isc.Log.echoAll(_1));return null}
return(_1.height!=null?_1.height:this.iconHeight)}
,isc.A.getTitleVisibleHeight=function isc_FormItem_getTitleVisibleHeight(){var _1=this.isDrawn()&&this.form?isc.Element.get(this.form.$21c(this)):null;if(_1==null){var _2="getTitleHeight() Unable to determine position for "+(this.name==null?"this item ":this.name)+". ";if(this.isDrawn()){_2+="This method is not supported by items of type "+this.getClass()}else{_2+="Position cannot be determined before the element is drawn"}
_2+=" - returning zero.";this.form.logWarn(_2);return 0}
return isc.Element.getVisibleHeight(_1)}
,isc.A.getWidth=function isc_FormItem_getWidth(){return this.width}
,isc.A.getVisibleWidth=function isc_FormItem_getVisibleWidth(){var _1=this.isDrawn()?this.getOuterElement():null;if(_1==null){this.logInfo("getVisibleWidth() - unable to determine drawn width for this item -"+" returning pixel width from specified width","sizing");if(isc.isA.Number(this.width)){return this.width}else if(this.form&&this.form.items.$170!=null){return this.form.items.$170[this.form.getItems().indexOf(this)]}
this.logWarn("getVisibleWidth() unable to determine width - returning zero","sizing");return 0}
return _1.offsetWidth}
,isc.A.getVisibleTitleWidth=function isc_FormItem_getVisibleTitleWidth(){var _1=this.isDrawn()&&this.form?isc.Element.get(this.form.$21c(this)):null;if(_1==null){this.logInfo("getVisibleTitleWidth() - unable to determine drawn width for this "+"item - returning 0","sizing");return 0}
return _1.offsetWidth}
,isc.A.getIconWidth=function isc_FormItem_getIconWidth(_1){if(_1==null&&this.icons!=null&&this.icons.getLength()>0)_1=this.icons[0];else if(!this.$255(_1)){this.logWarn("getIconWidth() passed invalid icon:"+isc.Log.echoAll(_1));return null}
return(_1.width!=null?_1.width:this.iconWidth)}
,isc.A.setHeight=function isc_FormItem_setHeight(_1){this.height=_1;this.redraw()}
,isc.A.setWidth=function isc_FormItem_setWidth(_1){this.width=_1;this.redraw()}
,isc.A.setLeft=function isc_FormItem_setLeft(_1){this.left=_1;this.redraw()}
,isc.A.setTop=function isc_FormItem_setTop(_1){this.top=_1;this.redraw()}
,isc.A.moved=function isc_FormItem_moved(){}
,isc.A.visibilityChanged=function isc_FormItem_visibilityChanged(){}
,isc.A.zIndexChanged=function isc_FormItem_zIndexChanged(){}
,isc.A.getInactiveEditorHTML=function isc_FormItem_getInactiveEditorHTML(_1,_2,_3,_4){this.$256=true;this.$226=this.setupInactiveContext(_4);if(this.logIsDebugEnabled("inactiveEditorHTML")){this.logDebug("getInactiveEditorHTML() called - context passed in:"+this.echo(_4)+" generated context ID:"+this.$226,"inactiveEditorHTML")}
var _5=this.getStandaloneItemHTML(_1,_2,_3);delete this.$226;delete this.$256;return _5}
,isc.A.setupInactiveContext=function isc_FormItem_setupInactiveContext(_1){if(_1==null)_1={};if(this.$257())_1.isPrintHTML=true;var _2=this.$232++;_1.inactiveContextID=_2;_1.formItem=this;if(!this.$258)this.$258={};this.$258[_2]=_1;return _2}
,isc.A.clearAllInactiveEditorContexts=function isc_FormItem_clearAllInactiveEditorContexts(){delete this.$258}
,isc.A.clearInactiveEditorContext=function isc_FormItem_clearInactiveEditorContext(_1){if(isc.isAn.Object(_1))_1=_1.inactiveContextID;if(this.$258)delete this.$258[_1]}
,isc.A.$22z=function isc_FormItem__getInactiveContextFromElement(_1){if(_1&&_1.id!=null&&this.$258!=null){var _2=_1.id,_3=this.$wf(_2);if(_3){var _4=_3.match(this.$233);if(_4){return this.$258[_4[1]]}}}
return null}
,isc.A.isInactiveHTML=function isc_FormItem_isInactiveHTML(){if(this.parentItem&&this.parentItem.isInactiveHTML())return true;return this.$257()||this.$256}
,isc.A.$257=function isc_FormItem__isPrinting(){return this.containerWidget&&this.containerWidget.isPrinting}
,isc.A.getStandaloneItemHTML=function isc_FormItem_getStandaloneItemHTML(_1,_2,_3){var _4=isc.SB.create(),_5=this.form;if(_5){if(this.$200()){var _6=this.$21g(this.left),_7=this.$21g(this.top,true),_8=this.getInnerWidth(),_9=this.getInnerHeight();if(!isc.isA.Number(_6))_6=0;if(!isc.isA.Number(_7))_7=0;_4.append(this.$234);_4.appendNumber(_6);_4.append(this.$235);_4.appendNumber(_7);if(isc.isA.Number(_8)){_4.append(this.$236);_4.appendNumber(_8)}
if(isc.isA.Number(_9)){_4.append(this.$237);_4.appendNumber(_9)}
_4.append(this.$238,this.$259(),this.$239)}
var _10=this.$24b,_11=_5.getID(),_12=this.getID();_10[1]=_11;_10[3]=_12;_10[5]=this.$t4(this.$24d);_4.append(_10);_4.append(this.getInnerHTML(_1,_2,_3,true));_4.append(this.$24c);if(this.$200()){_4.append(this.$24a)}}
return _4.release()}
,isc.A.$259=function isc_FormItem__getAbsDivID(){return this.$t4(this.$24f)}
,isc.A.getAbsDiv=function isc_FormItem_getAbsDiv(){if(this.$24g)return this.$24g;if(!this.isDrawn())return;this.$24g=isc.Element.get(this.$259());return this.$24g}
,isc.A.$26a=function isc_FormItem__hasExternalIcons(){var _1=this.icons;if(!_1)return false;for(var i=0;i<_1.length;i++){if(!_1[i].writeIntoItem)return true}
return false}
,isc.A.useDisabledEventMask=function isc_FormItem_useDisabledEventMask(){return(isc.Browser.isMoz&&this.hasDataElement())||(isc.Browser.isIE&&isc.isA.TextItem(this))}
,isc.A.$26b=function isc_FormItem__getEventMaskHTML(){var _1=this.$24h;_1[1]=this.$26c();_1[3]=this.getHeight();_1[5]=this.getItemID();_1[7]=this.$t4("eventMask");return _1.join(isc.emptyString)}
,isc.A.$26d=function isc_FormItem__getEventMaskElement(){return isc.Element.get(this.$t4("eventMask"))}
,isc.A.$26c=function isc_FormItem__getEventMaskWidth(){return this.getElementWidth()}
,isc.A.getBrowserSpellCheck=function isc_FormItem_getBrowserSpellCheck(){if(this.browserSpellCheck!=null)return this.browserSpellCheck;return this.form.browserSpellCheck}
,isc.A.$20z=function isc_FormItem__useHiddenDataElement(){return(this.shouldSaveValue&&!this.hasDataElement()&&this.shouldSubmitValue())}
,isc.A.$26e=function isc_FormItem__getHiddenDataElementID(){return this.$t4(this.$24i)}
,isc.A.$26f=function isc_FormItem__getHiddenDataElement(){return this.$26g(this.$24i)}
,isc.A.$26g=function isc_FormItem__getHTMLPartHandle(_1){if(!this.isDrawn())return null;if(!this.$26h)this.$26h={};var _2=this.$26h[_1];if(_2==null){_2=isc.Element.get(this.$t4(_1));if(_2!=null)this.$26h[_1]=_2}
return _2}
,isc.A.$26i=function isc_FormItem__getControlTableID(){return this.$t4(this.$24j)}
,isc.A.$26j=function isc_FormItem__getControlTableElement(){return this.$26g(this.$24j)}
,isc.A.$26k=function isc_FormItem__getTextBoxID(){return this.$t4(this.$24k)}
,isc.A.$6f=function isc_FormItem__getTextBoxElement(){if(this.hasDataElement()&&this.$26l)return this.getDataElement();return this.$26g(this.$24k)}
,isc.A.$26m=function isc_FormItem__getPickerIconCellID(){return this.$t4(this.$24l)}
,isc.A.$26n=function isc_FormItem__getPickerIconCellElement(){return this.$26g(this.$24l)}
,isc.A.$26o=function isc_FormItem__getHiddenDataElementHTML(){return"<INPUT type='hidden' name='"+this.getFieldName()+"' ID='"+this.$26e()+"'>"}
,isc.A.$26p=function isc_FormItem__getHintCellID(){return this.$t4(this.$24m)}
,isc.A.$26q=function isc_FormItem__getHintCellElement(){return this.$26g(this.$24m)}
,isc.A.updateState=function isc_FormItem_updateState(){if(!this.isDrawn())return;var _1=this.logIsDebugEnabled(this.$24n);if(this.containerWidget==this.form&&!this.$200()){var _2=this.getCellStyle();if(_1)this.logDebug("About to apply basic cell style:"+_2,"FormItemStyling");var _3=this.getFormCell();if(_3)_3.className=_2;var _4=this.getOuterTableElement();if(_4)_4.className=_2;if(this.showTitle)this.form.updateTitleCellState(this)}
if(this.showPickerIcon){var _5=this.getControlStyle(),_6=this.getPickerIconStyle();if(_1){this.logDebug("About to apply cell styles to control box and picker icon cell:"+[_5,_6],"FormItemStyling")}
var _7=this.$26j();if(_7)_7.className=_5;var _8=this.$26n();if(_8)_8.className=_6}
var _9=this.getTextBoxStyle();if(_1)this.logDebug("About to apply text box style:"+_9,"FormItemStyling");var _10=this.$6f();if(_10){_10.className=_9}
if(this.$26r()&&_10){if(!this.$26s){var _11=this.getTextBoxWidth(),_12=this.getTextBoxHeight();_11+=isc.Element.getHBorderSize(_10)-2;_12+=isc.Element.getVBorderSize(_10)-2;var _13=this.$t4("focusOutline");isc.Element.insertAdjacentHTML(_10,"beforeBegin","<DIV ID='"+_13+(this.textBoxStyle?"' CLASS='"+this.textBoxStyle+"Focused'":"'")+" STYLE='background-image:none;background-color:transparent;position:absolute;width:"+_11+"px;height:"+_12+"px;visibility:hidden;border:1px dotted white;z-index:100;'>&nbsp;</DIV>");this.$26s=isc.Element.get(_13)}
if(this.hasFocus)this.$26s.style.visibility="inherit";else this.$26s.style.visibility="hidden"}}
,isc.A.$26t=function isc_FormItem__warnDeprecated(_1,_2,_3){if(!this.logIsInfoEnabled(this.$24o))return;if(!this.$26u)this.$26u={};if(this.$26u[_1]==true)return;if(_3==null)_3="5.5";var _4=isc.SB.create();_4.append("Using '",_1,"': ",this[_1]," to style this form item.  This property is deprecated as of SmartClient Version ",_3," - we recommend removing this property and using '",_2,"' instead.");this.logInfo(_4.release(),"deprecated");this.$26u[_1]=true}
,isc.A.getInnerHTML=function isc_FormItem_getInnerHTML(_1,_2,_3,_4){var _5,_6;if(this.isInactiveHTML()&&this.$226==null){_5=true;var _7,_8=this.parentItem;if(_8!=null&&_8.isInactiveHTML()){if(_8.$226==null){_8.setupInactiveContext();_6=true}
_7=_8.$258[_8.$226]}
this.$226=this.setupInactiveContext(_7);if(this.logIsDebugEnabled("inactiveEditorHTML")){this.logDebug("getInnerHTML(): Item is marked as inactive - set up "+"new inactive context ID:"+this.$226,"inactiveEditorHTML")}}
this.$26v=_2&&!this.$21f();var _9;if((this.isInactiveHTML()||this.isDisabled())&&this.useDisabledEventMask()){_9=isc.SB.create();_9.append(this.$26b())}
if(this.$20z()){if(!_9)_9=isc.SB.create();_9.append(this.$26o())}
if(this.$21f())_2=false;var _10=this.$26w(_1,_2,_3);var _11;if(_9!=null){_9.append(_10);if(_4)_11=_9.getArray();_11=_9.release()}else{_11=(_4?_10:_10.join(isc.emptyString))}
if(_5)delete this.$226;if(this.parentItem&&_6)
delete this.parentItem.$226;return _11}
,isc.A.$26x=function isc_FormItem__writeOuterTable(_1,_2){if(_2)return true;if(_1&&this.getHint()!=null)return true;if(this.icons&&this.icons.length>0)return true}
,isc.A.$253=function isc_FormItem__getValueIcon(_1){if(this.suppressValueIcon)return null;var _2,_3;if(_1===_3)_1=this.getValue();if(this.getValueIcon)_2=this.getValueIcon(_1);else{if(_1==null)_2=this.emptyValueIcon;else if(this.valueIcons!=null)_2=this.valueIcons[_1]}
if(_2==null)return null;var _4=((this.isDisabled()||this.isReadOnly())&&this.showValueIconDisabled?this.$24r:this.$26y);if(_4!=null){if(!isc.CheckboxItem.$26z)isc.CheckboxItem.$26z={};var _5=isc.CheckboxItem.$26z[_2];if(!_5){_5={};_5.Over=isc.Img.urlForState(_2,false,false,this.$24p);_5.Down=isc.Img.urlForState(_2,false,false,this.$24q);_5.Disabled=isc.Img.urlForState(_2,false,false,this.$24r);isc.CheckboxItem.$26z[_2]=_5}
_2=_5[_4]}
return _2}
,isc.A.$2n=function isc_FormItem__getValueIconHTML(_1){var _2=this.$253(_1);if(_2==null){return isc.emptyString}
var _3=this.imageURLPrefix||this.baseURL||this.imgDir,_4=this.imageURLSuffix;if(_4)_2=_2+_4;var _5=this.getValueIconWidth();var _6=this.getValueIconHeight();return isc.Canvas.$2n(_2,_3,_5,_6,this.valueIconLeftPadding,this.valueIconRightPadding,this.$t4(this.$24s))}
,isc.A.$260=function isc_FormItem__getValueIconHandle(){if(!this.isDrawn())return null;var _1=isc.Element.get(this.$t4(this.$24s));return _1}
,isc.A.$26w=function isc_FormItem__getTableHTML(_1,_2,_3){var _4=this.getErrorOrientation(),_5,_6=_4==isc.Canvas.LEFT,_7,_8=this.isReadOnly();if(_3&&(_6||_4==isc.Canvas.RIGHT))
{var _9=this.getErrors();if(_9){_5=true;_7=this.getErrorHTML(_9)}}
var _10=this.iconVAlign,_11=this.mapValueToDisplay(_1),_12=this.$26x(_2,_5),_13=this.showPickerIcon;;var _14=_12?isc.FormItem.$23t():[];if(_12){_14.length=13;_14[1]=this.$261();_14[3]=this.getOuterTableCSS();if(this.containerWidget==this.form&&!this.$200()){_14[5]=this.getCellStyle()}else{_14[5]=null}
if(_5&&_6){_14[7]=isc.StringBuffer.concat("<TD STYLE='",isc.Canvas.$rr,"' CLASS='",this.getCellStyle(),"'>",_7,"</TD>")}else _14[7]=null;if(!_13)_14[9]=this.getTextBoxCellCSS();else _14[9]=isc.Canvas.$rr;_14[11]=_10}
if(!_13){_14[_14.length]=(_8?this.getReadOnlyHTML(_11,_1):this.getElementHTML(_11,_1))}else{var _15=this.getPickerIconStyle(),_16=this.getID(),_17=this.getControlStyle(),_18=this.$24u,_19=this.$26i(),_20=this.$26k(),_21=this.$26m();_18[1]=_19;_18[3]=_16;_18[6]=this.getControlTableCSS();if(_17==null&&this.containerWidget==this.form&&!this.$200()){_18[8]=this.getCellStyle();_18[6]+=isc.Canvas.$rr}else{_18[8]=_17}
_18[10]=this.getTextBoxCellCSS();_18[12]=(_8?this.getReadOnlyHTML(_11,_1):this.getElementHTML(_11,_1));_18[14]=_21;_18[16]=_15;_18[18]=this.getPickerIconCellCSS();var _22=this.getPickerIcon(),_23=_22&&this.hasFocus&&this.showFocusedPickerIcon&&(_22.showFocusedWithItem!=false);_18[20]=this.getIconHTML(this.getPickerIcon(),_23);for(var i=0;i<_18.length;i++){_14[_14.length]=_18[i]}}
if(_12){if(this.$26a()){var _25=isc.FormItem.$23v();_25[1]=_10;_25[3]=this.getTotalIconsWidth();_25[5]=this.getCellStyle();_25[7]=this.getIconsHTML();for(var i=0;i<_25.length;i++){_14[_14.length]=_25[i]}}
var _26=(_5&&!_6);var _27;if(_2){_27=this.getHint();if(isc.isA.emptyString(_27))_27=null}
if(_27||_26){var _28=this.$24v;_28[1]=this.$26p();_28[3]=_27?this.getHintStyle():null;_28[5]=(_27||"")+(_26?_7||"":"");for(var i=0;i<_28.length;i++){_14[_14.length]=this.$24v[i]}}
_14[_14.length]=this.$24t}
return _14}
,isc.A.$261=function isc_FormItem__getOuterTableID(){return this.$t4(this.$24w)}
,isc.A.$262=function isc_FormItem__getCellStyle(_1){var _2=this.hasErrors();if(!isc.FormItem.$263)isc.FormItem.$263={};var _3=isc.FormItem.$263[_1];if(!_3){_3={};_3.Error=_1+"Error";_3.Focused=_1+"Focused"
_3.Disabled=_1+"Disabled"
isc.FormItem.$263[_1]=_3}
if(_2){return(this.shouldShowErrorStyle()&&this.form.showInlineErrors?_3.Error:_1)}else{if(this.showFocused&&this.hasFocus&&!this.isInactiveHTML())
return _3.Focused;if(this.showDisabled&&this.isDisabled())return _3.Disabled;return _1}}
,isc.A.getCellStyle=function isc_FormItem_getCellStyle(){if(this.parentItem!=null){if(this.parentItem.itemCellStyle)return this.$262(this.parentItem.itemCellStyle)}
var _1=this.$262(this.cellStyle);if(!this.hasErrors()){if(this.cellClassName!=null){this.$26t("cellClassName","cellStyle");_1=this.cellClassName}}else{if(this.errorCellClassName!=null){this.$26t("errorCellClassname","cellStyle");_1=this.errorCellClassName}}
return _1}
,isc.A.getTitleStyle=function isc_FormItem_getTitleStyle(){if(this.$257()&&this.printTitleStyle){return this.$262(this.printTitleStyle)}
var _1=this.getErrors();if(_1==isc.emptyString)_1=null;var _2=this.$262(this.titleStyle);if(!_1){if(this.titleClassName!=null){this.$26t("titleClassName","titleStyle");_2=this.titleClassName}}else{if(this.titleErrorClassName!=null){this.$26t("titleErrorClassName","titleStyle");_2=this.titleErrorClassName}}
return _2}
,isc.A.getHintStyle=function isc_FormItem_getHintStyle(){if(this.hintClassName!=null){this.$26t("hintClassName","hintStyle");return this.hintClassName}
if(this.hintStyle!=null)return this.hintStyle}
,isc.A.getTextBoxStyle=function isc_FormItem_getTextBoxStyle(){if(this.$257()&&this.printTextBoxStyle){return this.$262(this.printTextBoxStyle)}
var _1=this.textBoxStyle?this.$262(this.textBoxStyle):null;if(this.elementClassName!=null){this.$26t("elementClassName","textBoxStyle");_1=this.elementClassName}
return _1}
,isc.A.getPickerIconStyle=function isc_FormItem_getPickerIconStyle(){if(this.pickerIconStyle!=null)return this.$262(this.pickerIconStyle);return null}
,isc.A.getControlStyle=function isc_FormItem_getControlStyle(){if(this.controlStyle!=null)return this.$262(this.controlStyle);return null}
,isc.A.getOuterTableCSS=function isc_FormItem_getOuterTableCSS(){var _1=this.$242;var _2,_3=this.expandHintAndErrors;if(!this.$257()||isc.isA.Number(this.width)){if(_3&&this.getHint()==null){var _4=this.getErrorOrientation();_3=(_4==isc.Canvas.LEFT||_4==isc.Canvas.RIGHT)}
if(_3&&(this.getColWidth()!=null)){_2=Math.max(this.getInnerWidth(),this.getColWidth())}else{_2=this.getInnerWidth()}}
if(!isc.isA.Number(_2)){if(!this.$264)
this.$264=this.$24y;return this.$264}else if(_1[_2]!=null){return _1[_2]}
var _5=isc.SB.create();_5.append(this.$24y);_5.append(this.$19h);_5.appendNumber(_2,5);_5.append(isc.semi);_1[_2]=_5.release();return _1[_2]}
,isc.A.getControlTableCSS=function isc_FormItem_getControlTableCSS(){var _1=isc.SB.create();_1.append(this.$243);var _2=this.getElementWidth()-this.$254();if(isc.isA.Number(_2))_1.append(this.$19h,_2,this.$11i);return _1.release()}
,isc.A.getTextBoxCellCSS=function isc_FormItem_getTextBoxCellCSS(){return this.textBoxCellCSS!=null?this.textBoxCellCSS:isc.Canvas.$rr}
,isc.A.getTextBoxCSS=function isc_FormItem_getTextBoxCSS(){var _1=isc.SB.create();if(!this.$257()||isc.isA.Number(this.width)){var _2=this.getTextBoxWidth();if(isc.isA.Number(_2)){if((isc.Browser.isOpera||isc.Browser.isMoz||isc.Browser.isSafari)&&!this.clipValue){_1.append(this.$240,_2,this.$11i)}else{_1.append(this.$19h,_2,this.$11i)}}}
var _3=this.getTextBoxHeight();if(isc.isA.Number(_3)){if(isc.Browser.isMoz&&!this.clipValue){_1.append(this.$241,_3,this.$11i)}else{_1.append(this.$19g,_3,this.$11i)}}
if(this.clipValue)_1.append(this.$244);if(this.wrap)_1.append(this.$24y)
else _1.append(this.$24z);if(this.textAlign!=null){_1.append(this.$245,this.textAlign,this.$px)}
return _1.release()}
);isc.evalBoundary;isc.B.push(isc.A.getPickerIconCellCSS=function isc_FormItem_getPickerIconCellCSS(){if(isc.Browser.isIE)return isc.emptyString;var _1=this.getPickerIconHeight();if(isc.isA.Number(_1)&&_1<this.getInnerHeight()){return this.$246+_1+this.$11i}
return isc.emptyString}
,isc.A.getPickerIcon=function isc_FormItem_getPickerIcon(){if(this.$265==null){var _1=this.getPickerIconWidth(),_2=this.getPickerIconHeight();var _3={pickerIcon:true,writeIntoItem:true,showOver:this.showOver,showFocused:this.showFocusedPickerIcon,hspace:this.pickerIconHSpace,width:_1,height:_2,src:this.pickerIconSrc,click:function(_4,_5,_6){_5.showPicker()}};isc.addProperties(_3,this.pickerIconDefaults,this.pickerIconProperties);this.$266(_3,this.pickerIconName);this.$265=_3;if(this.iconIsDisabled(_3))_3.$267=true}
return this.$265}
,isc.A.getElementHTML=function isc_FormItem_getElementHTML(_1,_2){var _3=isc.SB.create(),_4=this.$26r();var _5=this.$268(),_6,_7;if(_5){var _8=this.$269(),_9=this.isDisabled()?null:this.accessKey;if(_4){_7=isc.Canvas.getFocusProxyString(this.getID(),false,0,0,this.getTextBoxWidth(),this.getTextBoxHeight(),this.isVisible(),!this.isDisabled(),_8,_9,false)}else{var _10=isc.SB.create();if(_9!=null)_10.append(this.$247,_9,this.$r9);_10.append(this.$248,_8,this.$r9);_6=_10.release()}}
if(_7!=null)_3.append(_7);var _11=this.$249;_11[1]=this.$26k();_11[3]=this.getID();_11[6]=this.getTextBoxStyle();_11[8]=this.getTextBoxCSS();_11[10]=_6;_11[12]=this.$2n(_2);_11[13]=(this.showValueIconOnly?null:_1);_3.append(_11);return _3.release()}
,isc.A.getReadOnlyHTML=function isc_FormItem_getReadOnlyHTML(_1,_2){return this.getElementHTML()}
,isc.A.$26r=function isc_FormItem__writeOutFocusProxy(){if(this.useFocusProxy!=null)return this.useFocusProxy;return(isc.Browser.isMoz&&isc.Browser.geckoVersion<20051111)&&this.$mb()&&!this.hasDataElement()}
,isc.A.$27a=function isc_FormItem__getItemElementAttributeHTML(){if(!isc.FormItem.$27b){isc.FormItem.$27b=[" ",isc.DynamicForm.$18o,"='",null,"' ",isc.DynamicForm.$18p,"='",isc.DynamicForm.$18q,"'"]}
isc.FormItem.$27b[3]=this.getItemID();return isc.FormItem.$27b.join(isc.emptyString)}
,isc.A.getErrors=function isc_FormItem_getErrors(){if(this.form)return this.form.getFieldErrors(this)}
,isc.A.getError=function isc_FormItem_getError(){this.logWarn("call to deprecated method FormItem.getError()."+" Use FormItem.getErrors() instead.");return this.getErrors()}
,isc.A.getErrorMessage=function isc_FormItem_getErrorMessage(_1){return(isc.isAn.Array(_1)?"<UL><LI>"+_1.join("</LI><LI>")+"</LI></UL>":_1)}
,isc.A.shouldShowErrorIcon=function isc_FormItem_shouldShowErrorIcon(){return this.showErrorIcon!=null?this.showErrorIcon:this.form.showErrorIcons}
,isc.A.shouldShowErrorText=function isc_FormItem_shouldShowErrorText(){return this.showErrorText!=null?this.showErrorText:this.form.showErrorText}
,isc.A.shouldShowErrorStyle=function isc_FormItem_shouldShowErrorStyle(){return this.showErrorStyle!=null?this.showErrorStyle:this.form.showErrorStyle}
,isc.A.shouldShowErrorIconPrompt=function isc_FormItem_shouldShowErrorIconPrompt(){return this.shouldShowErrorIcon&&!this.shouldShowErrorText()}
,isc.A.getErrorOrientation=function isc_FormItem_getErrorOrientation(){return this.errorOrientation!=null?this.errorOrientation:this.form.errorOrientation}
,isc.A.getErrorHTML=function isc_FormItem_getErrorHTML(_1){var _2=this.shouldShowErrorText(),_3=this.shouldShowErrorIcon();if(!_2&&!_3)return isc.emptyString;var _4=this.form,_5=_3&&_2,_6=!_5&&_3&&((this.getErrorOrientation()==isc.Canvas.LEFT)||(this.getErrorOrientation()==isc.Canvas.RIGHT)),_7=(_2&&this.form.showTitlesWithErrorMessages&&this.getTitle()!=null?this.getTitle()+": ":null),_8,_9=_2?this.getErrorMessage(_1):null;if(!_5){_8=isc.SB.concat("<DIV ",(_6?"style='display:inline;'":null)," CLASS='",this.getCellStyle(),"'>",(_3?this.getErrorIconHTML(_1)+"&nbsp;":null),_7,_9,"</DIV>")}else{_8=isc.SB.concat("<TABLE role='presentation' WIDTH=100% CELLSPACING=0 CELLPADDING=0><TR>","<TD WIDTH=",this.errorIconWidth,">",this.getErrorIconHTML(_1),"</TD><TD STYLE='",isc.Canvas.$rr,"' CLASS='",this.getCellStyle(),"'>&nbsp;",_7,_9,"</TD></TR></TABLE>")}
return _8}
,isc.A.getErrorIconHTML=function isc_FormItem_getErrorIconHTML(_1){this.$27c=_1;var _2=this.getErrorIconId();var _3="";if(_1!=null&&isc.Canvas.ariaEnabled()&&!isc.Canvas.useLiteAria()){if(isc.isAn.Array(_1))_1=_1.join(",");_3=' aria-label="'+_1.replace("\"","&quot;")+'"'}
return this.$27d(_2,this.errorIconWidth,this.errorIconHeight,"top",0,null,null,this.form.getImgURL(this.errorIconSrc),this.getID(),_2,null,true,isc.DynamicForm.$18o+"='"+this.getID()+"' "+isc.DynamicForm.$18p+"='"+_2+"'"+_3)}
,isc.A.getErrorIconId=function isc_FormItem_getErrorIconId(){return this.$t4("error")}
,isc.A.getHint=function isc_FormItem_getHint(){if(!this.showHint||!this.hint)return null
return this.hint}
,isc.A.drawn=function isc_FormItem_drawn(){if(this.logIsInfoEnabled(this.$r5)){this.logInfo("Form item drawn "+(this.containerWidget==this.form?"in form "+this.form.getID():"in container widget "+this.containerWidget.getID())+(this.logIsDebugEnabled("drawing")?this.getStackTrace():""),"drawing")}
this.$t1=true;if(this.$26v)this.$27e=true;this.$26v=null;this.$27f();if(isc.screenReader)this.addContentRoles()}
,isc.A.redrawing=function isc_FormItem_redrawing(){if(this.$27g(true)){this.$27h()}
this.form.clearingElement(this);this.$24g=null}
,isc.A.redrawn=function isc_FormItem_redrawn(){if(this.logIsInfoEnabled("drawing")){this.logInfo("Form item redrawn "+(this.containerWidget==this.form?"in form "+this.form.getID():"in container widget "+this.containerWidget.getID())+(this.logIsDebugEnabled("drawing")?this.getStackTrace():""),"drawing")}
this.$27i();this.$27f();if(isc.screenReader)this.addContentRoles();if(this.$27g(true)){if(isc.Browser.isIE){this.delayCall("$27j",[],100)}else{this.$27j()}}}
,isc.A.$27h=function isc_FormItem__storeFocusForRedraw(){this.$27k=true;this.rememberSelection();if(this.items){for(var i=0;i<this.items.length;i++){if(this.items[i].hasFocus){return this.items[i].$27h()}}}
var _2=this.$27l();if(_2!=null&&_2!=this.getFocusElement()){var _3=this.getPickerIcon();if(_3!=null&&this.$27m(_3)==_2){this.$27n=_3}else if(this.icons){for(var i=0;i<this.icons.length;i++){if(this.$27m(this.icons[i])==_2){this.$27n=this.icons[i];break}}}}}
,isc.A.$27j=function isc_FormItem__refocusAfterRedraw(){var _1=this.isDrawn()&&this.isVisible();if(_1){var _2=isc.EH.getFocusCanvas();if(_2!=null&&_2!=this.form){_1=false}else{var _3=this.form.getFocusSubItem();if(_3!=this&&_3!=this.parentItem&&(!this.items||!this.items.contains(_3)))
{_1=false}}}
delete this.$27k;if(_1)this.resetToLastSelection();if(this.items){for(var i=0;i<this.items.length;i++){if(this.items[i].$27g()){return this.items[i].$27j()}}}
if(_1)this.form.$21s(this);if(this.$27n){var _5=this.getIcon(this.$27n);delete this.$27n;if(_5){if(_1){this.focusInIcon(_5)}
return}}
if(_1)this.focusInItem()}
,isc.A.$27f=function isc_FormItem__applyHandlersToElement(){if(this.$mb()){var _1=this.getFocusElement();if(!_1){this.logWarn("Attempting to apply event handlers to this item. "+"Unable to get a pointer to this item's focus element");return}
_1.onfocus=isc.FormItem.$23f;_1.onblur=isc.FormItem.$23i;if(isc.Browser.isIE){_1.onpaste=isc.FormItem.$23l;_1.oncut=isc.FormItem.$23l}
if(this.$27o){for(var _2 in this.$27o){_1[_2]=this.$27o[_2]}}}
this.$27p()}
,isc.A.$27p=function isc_FormItem__setUpIconEventHandlers(){if(this.showPickerIcon)this.$27q(this.getPickerIcon());if(this.showIcons&&this.icons&&this.icons.length>0){for(var i=0;i<this.icons.length;i++){var _2=this.icons[i];if(_2&&(this.$27r(_2)||this.$27s(_2)))
this.$27q(_2)}}}
,isc.A.$27q=function isc_FormItem__iconDrawn(_1){if(!_1.imgOnly){var _2=this.$27m(_1);if(_2){_2.onfocus=isc.FormItem.$23o
_2.onblur=isc.FormItem.$23q
_2.href=this.$25a;_2.onclick=isc.FormItem.$23s;if(isc.Browser.isSafari){if(!isc.FormItem.$27t){isc.FormItem.$27t=function(){var _3=isc.DynamicForm.$ot(this),_4=_3.item,_5=_3.overIcon;if(_4)return _4.focusInIcon(_5)}}
_2.onmousedown=isc.FormItem.$27t}}}}
,isc.A.cleared=function isc_FormItem_cleared(){if(this.logIsInfoEnabled("drawing")){this.logInfo("Form item cleared "+(this.containerWidget==this.form?"from within form "+this.form.getID():"from within container widget "+this.containerWidget.getID())+(this.logIsDebugEnabled("drawing")?this.getStackTrace():""),"drawing")}
this.form.clearingElement(this);this.$27i();this.$27e=false;this.$26v=false;this.$t1=false}
,isc.A.$27i=function isc_FormItem__clearCachedHandles(){this.$25v=null;this.$24g=null;this.$27u=null;this.$26h={}}
,isc.A.isDrawn=function isc_FormItem_isDrawn(){return this.$t1}
,isc.A.$25u=function isc_FormItem__setUpIcons(){var _1=this.icons;if(_1==null)return;for(var i=0;i<_1.length;i++){var _3=_1[i];this.$27v(_3)}}
,isc.A.$27v=function isc_FormItem__setUpIcon(_1){this.$266(_1);if(this.iconIsDisabled(_1))_1.$267=true}
,isc.A.getIconsHTML=function isc_FormItem_getIconsHTML(_1){if(!this.showIcons||(this.icons==null&&(!_1||!this.showPickerIcon))){return""}
var _2=this.$27g(true);if(this.showIconsOnFocus&&!_2){this.hideAllIcons();return""}
var _3=isc.SB.create(),_4=false;var _5=this.icons;if(_1&&this.showPickerIcon){_5=[this.getPickerIcon()];_5.addList(this.icons)}
for(var i=0;i<_5.length;i++){var _7=_5[i];if(!this.$27s(_7)||this.$27r(_7))continue;if(_4==false){_4=true;_3.append(this.$25b)}
_3.append("<td>");var _8=_2&&this.$27w(_7,true);_3.append(this.getIconHTML(_7,null,this.iconIsDisabled(_7),!!_8));_3.append("</td>")}
if(_4)_3.append("</table>");return _3.release()}
,isc.A.$27g=function isc_FormItem__hasRedrawFocus(_1){var _2=this.hasFocus||this.$27k;if(_1&&!_2&&this.items!=null){for(var i=0;i<this.items.length;i++){if(this.items[i].hasFocus||this.items[i].$27k)_2=true;break}}
return _2}
,isc.A.$266=function isc_FormItem__setupIconName(_1,_2){if(_2==null)_2=_1.name;if(_2==null&&_1.$27x!=null){this.logWarn("Attempting to use '$27x' property as icon name - this property has been deprecated in favor of 'name'");_2=_1.$27x}
if(_2!=null){var _3=this.icons?this.icons.findAll("name",_2):[];if(_3!=null&&_3.length>0&&(_3.length>1||_3[0]!=_1))
{this.logWarn("This form item has more than one icon with the same specified name:"+_2+". Ignoring this name and using an auto-generated one instead.");_2=null}else{_1.name=_2;return _1}}
if(this.$27y==null)this.$27y=0;_1.name="_"+this.$27y++;return _1}
,isc.A.$27z=function isc_FormItem__getIconVAlign(_1){if(this.$265&&(_1==this.$265))return null;var _2=this.iconVAlign;if(_2==isc.Canvas.TOP){return"top"}else if(_2==isc.Canvas.BOTTOM){return(isc.Browser.isSafari?"bottom":"text-bottom")}else if(_2==isc.Canvas.CENTER){return"middle"}
return _2}
,isc.A.$270=function isc_FormItem__getIconVMargin(){return 0}
,isc.A.getIconPrompt=function isc_FormItem_getIconPrompt(_1){if(this.iconIsDisabled(_1))return null;return _1.prompt||this.iconPrompt}
,isc.A.getIconURL=function isc_FormItem_getIconURL(_1,_2,_3,_4){var _5=_1.src||this.defaultIconSrc,_6=(this.showDisabled&&(_3||this.iconIsDisabled(_1)))?isc.StatefulCanvas.STATE_DISABLED:_2?isc.StatefulCanvas.STATE_OVER:null;_5=isc.Img.urlForState(_5,false,_4,_6);return _5}
,isc.A.getIconHTML=function isc_FormItem_getIconHTML(_1,_2,_3,_4){var _5=this.getIconURL(_1,_2,_3,_4),_6=this.getIconWidth(_1),_7=this.getIconHeight(_1),_8=(_1.hspace!=null?_1.hspace:this.iconHSpace),_9=_1.backgroundColor,_10=this.form.getID(),_11=this.getItemID(),_12=_1.name;if(_1.imgOnly){return this.$27d(this.$271(_12),_6,_7,this.$27z(_1),this.$270(_1),_8,_9,_5,_11,_12)}else{if(isc.FormItem.$272==null){isc.FormItem.$272=["<a role='button' ID='",,"'"," style='margin-left:",,"px;"+(isc.Browser.isMoz?"-moz-user-focus:":""),,,"' tabIndex=",," ",isc.DynamicForm.$18o,"='",,"' ",isc.DynamicForm.$18p,"='",,"' handleNativeEvents=false>",,"</a>"]}
var _13=isc.FormItem.$272;var _3=this.iconIsDisabled(_1),_14=(_3||this.canTabToIcons==false)?-1:this.$273(_1);_13[1]=this.$274(_12);var _15=this.$275(_1);if(_15)_13[4]=_8;else _13[4]="0"
if(isc.Browser.isMoz)_13[6]=(_14<0?"ignore;":"normal;");_13[7]=_3?"cursor:default;":null;_13[9]=_14;if(isc.Canvas.ariaEnabled()&&!isc.Canvas.useLiteAria()){_13[10]=" ";if(_1.prompt){_13[10]=" aria-label='"+_1.prompt.replace("'","&apos;")+"' "}
if(_3)_13[10]+=" aria-disabled='true' "}
_13[13]=_11;_13[17]=_12;_13[19]=this.$27d(this.$271(_12),_6,_7,this.$27z(_1),this.$270(_1),(!_15?_8:null),_9,_5,_11,_12,_8);return _13.join(isc.emptyString)}}
,isc.A.$275=function isc_FormItem__applyIconHSpaceToLink(_1){return(!isc.Browser.isIE&&!isc.Browser.isSafari&&!_1.imgOnly&&!isc.Browser.isStrict)}
,isc.A.$27d=function isc_FormItem__getIconImgHTML(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,_11,_12,_13){if(isc.FormItem.$276==null){isc.FormItem.$276=["ID='",,"' style='",,,";margin-top:",,"px;margin-bottom:",,"px;",,,,"'"]}
var _14=isc.FormItem.$276;_14[1]=_1
if(_4!=null){_14[3]=this.$25c;_14[4]=_4}else{_14[3]=null;_14[4]=null}
_14[6]=_5;_14[8]=_5;if(_6!=null){_14[10]="margin-left:"+_6+"px;"}else{_14[10]=null}
_14[11]=(_7!=null?"background-color:"+_7+";":null);if(isc.Browser.isStrict&&!isc.Browser.isTransitional&&!_12)
_14[12]="display:block;"
else _14[12]=null;if(_13)_14[14]=_13;var _13=_14.join(isc.emptyString);_14.length=14;var _15=isc.FormItem.$11z=isc.FormItem.$11z||{align:isc.Browser.isSafari?"absmiddle":"TEXTTOP"};_15.src=_8;_15.width=_2;_15.height=_3;_15.extraStuff=_13;return isc.Canvas.imgHTML(_15)}
,isc.A.$274=function isc_FormItem__getIconLinkId(_1){if(this.isInactiveHTML()){return this.$t4(this._$_iLink_+_1)}
if(!this.$277)this.$277={};var _2=this.$277;if(!_2[_1]){_2[_1]=this.$t4(this._$_iLink_+_1,true)}
return _2[_1]}
,isc.A.$271=function isc_FormItem__getIconImgId(_1){if(this.isInactiveHTML()){return this.$t4(this._$_iImg_+_1)}
if(!this.$278)this.$278={};var _2=this.$278;if(!_2[_1]){_2[_1]=this.$t4(this._$_iImg_+_1,true)}
return _2[_1]}
,isc.A.$27m=function isc_FormItem__getIconLinkElement(_1){_1=this.getIcon(_1);if(_1==null||_1.imgOnly)return null;var _2=this.$274(_1.name);return isc.Element.get(_2)}
,isc.A.$6h=function isc_FormItem__getIconImgElement(_1){_1=this.getIcon(_1);if(_1==null)return null;var _2=this.$271(_1.name);return isc.Element.get(_2)}
,isc.A.$279=function isc_FormItem__getTargetIcon(_1){if(!_1||!this.icons)return null;var _2=isc.DynamicForm.$ot(_1);if(!_2||_2.item!=this)return null;return _2.icon}
,isc.A.$27s=function isc_FormItem__shouldShowIcon(_1){if(_1.showIf==null)return true;isc.Func.replaceWithMethod(_1,"showIf","form,item");return!!_1.showIf(this.form,this)}
,isc.A.$27r=function isc_FormItem__writeIconIntoItem(_1){if(_1.writeIntoItem)return true;return false}
,isc.A.$28a=function isc_FormItem__mayShowIcons(){if(!this.showIcons||this.icons==null||(this.showIconsOnFocus&&!this.hasFocus))return false;return true}
,isc.A.getTotalIconsWidth=function isc_FormItem_getTotalIconsWidth(){if(!this.$28a())return 0;var _1=0;for(var i=0;i<this.icons.length;i++){var _3=this.icons[i];if(!this.$27s(_3)||this.$27r(_3))continue;_1+=(_3.width!=null?_3.width:this.iconWidth)+(_3.hspace!=null?_3.hspace:this.iconHSpace)}
return _1}
,isc.A.getIconsHeight=function isc_FormItem_getIconsHeight(){if(!this.$28a())return 0;var _1=0;for(var i=0;i<this.icons.length;i++){var _3=this.icons[i];if(!this.$27s(_3)||this.$27r(_3))continue;var _4=(_3.height!=null?_3.height:this.iconHeight);_4+=this.$270()*2;if(_4>_1)_1=_4}
return _1}
,isc.A.setIcons=function isc_FormItem_setIcons(_1){this.icons=_1;this.$25u();this.redraw()}
,isc.A.addIcon=function isc_FormItem_addIcon(_1){if(!this.icons)this.icons=[];this.icons.add(_1);this.setIcons(this.icons);return _1}
,isc.A.getIconByProperty=function isc_FormItem_getIconByProperty(_1,_2){if(this.icons)return this.icons.find(_1,_2)}
,isc.A.setIconEnabled=function isc_FormItem_setIconEnabled(_1){_1=this.getIcon(_1);if(!_1)return;var _2=!this.iconIsDisabled(_1);if(!!_1.$267!=_2)return;if(!_2)_1.$267=true;else delete _1.$267;if(!this.isDrawn())return;var _3=this.$27m(_1),_4=this.$6h(_1);if(_3){if(!_2){isc.FormItem.setElementTabIndex(_3,-1);_3.style.cursor="default"}else{isc.FormItem.setElementTabIndex(_3,this.$273(_1))
_3.style.cursor=""}}
if(_4){var _5=this.getIconURL(_1,null,!_2);isc.Canvas.$1j(_4,_5)}}
,isc.A.showIcon=function isc_FormItem_showIcon(_1,_2){delete this.$28b;if(!isc.isAn.Object(_1))return;if(_1.name==null){this.$266(_1)}
var _3=this.$27s(_1);_1.showIf=function(){return true}
if(!_3&&this.showIcons&&this.containerWidget.isDrawn()&&this.isVisible())
{if(this.redrawOnShowIcon||_1.writeIntoItem){this.redraw()}else{var _4;for(var i=(this.icons.indexOf(_1)-1);i>=0;i--){var _6=this.icons[i];if(!_6.writeIntoItem&&this.$27s(_6)){_4=_6;break}}
var _7=true;if(_4!=null){var _8;_8=_4.imgOnly?this.$6h(_4):this.$27m(_4);if(_8!=null){var _9=this.getIconHTML(_1,null,this.isDisabled(),_2);var _10="<td>"+_9+"</td>",_11=_8.parentNode;isc.Element.insertAdjacentHTML(_11,"afterEnd",_10);this.$28c();_7=false}}
if(_7){this.logInfo("showIcon(): Unable to dynamically update icon visibility - "+"redrawing the form");return this.redraw()}else{this.$27q(_1)}}}}
,isc.A.hideIcon=function isc_FormItem_hideIcon(_1){if(!isc.isAn.Object(_1))return;var _2=this.$27s(_1);_1.showIf=function(){return false}
if(_2&&this.showIcons&&this.containerWidget.isDrawn()&&this.isVisible())
{if(this.redrawOnShowIcon||_1.writeIntoItem){this.redraw()}
else{var _3=_1.imgOnly?this.$6h(_1):this.$27m(_1);if(_3==null){this.logInfo("hideIcon(): Unable to dynamically update icon visibility - "+"redrawing the form");return this.redraw()}
var _4=_3.parentNode,_5=_4.parentNode;if(_4.tagName.toLowerCase()!="td"){isc.Element.clear(_3)}else{if(_5.cells.length==1){isc.Element.clear(_3.parentNode)}else{isc.Element.clear(_4)}}
this.$28c()}}}
,isc.A.$28c=function isc_FormItem__iconVisibilityChanged(){this.$28d()}
,isc.A.showAllIcons=function isc_FormItem_showAllIcons(_1){if(this.$28e!=null){isc.Timer.clear(this.$28e);delete this.$28e}
this.$28f(this.icons,_1)}
,isc.A.hideAllIcons=function isc_FormItem_hideAllIcons(){if(this.$28e!=null)delete this.$28e;this.$28g(this.icons);this.$28b=true}
,isc.A.$28f=function isc_FormItem__showIcons(_1,_2){if(_1==null||_1.length==0)return;for(var i=0;i<_1.length;i++){_2=_2&&this.$27w(_1[i],true);this.showIcon(_1[i],_2)}}
,isc.A.$28g=function isc_FormItem__hideIcons(_1){if(_1==null||_1.length==0)return;for(var i=0;i<_1.length;i++){this.hideIcon(_1[i])}}
,isc.A.getIcon=function isc_FormItem_getIcon(_1){if(_1==null)return;var _2;if(this.icons){for(var i=0;i<this.icons.length;i++){if(this.icons[i]==_1||this.icons[i].name==_1)_2=this.icons[i]}}
if(!_2&&this.showPickerIcon){if(isc.isAn.Object(_1))_1=_1.name;var _4=this.getPickerIcon();if(_4&&_4.name==_1)_2=_4}
if(!_2){this.logInfo("FormItem unable to get pointer to icon with name:"+_1+" - Invalid name, or icons array has been inappropriately modified."+" To update icon[s] for some form item, use the method 'setIcons()'.")}
return _2}
,isc.A.$28h=function isc_FormItem__setIconImgState(_1,_2,_3){if(this.isDisabled())return;if(_3==null)_3=this.hasFocus&&this.$27w(_1,true)
var _4=this.$6h(_1);if(_4!=null){var _5=this.getIconURL(_1,_2,null,_3);isc.Canvas.$1j(_4,_5)}}
,isc.A.$28i=function isc_FormItem__iconShouldShowOver(_1){if(this.isDisabled())return false;if(_1.showOver!=null)return _1.showOver;return this.showOverIcons}
,isc.A.$27w=function isc_FormItem__iconShouldShowFocused(_1,_2){if(!_1||this.isDisabled())return false;if(_2&&_1.showFocusedWithItem==false)return false;if(_1.showFocused!=null)return _1.showFocused;return this.showFocusedIcons}
,isc.A.setIconBackgroundColor=function isc_FormItem_setIconBackgroundColor(_1,_2){_1.backgroundColor=_2;var _3=this.$6h(_1);if(_3!=null){try{_3.style.backgroundColor=_2}catch(e){}}}
,isc.A.showPicker=function isc_FormItem_showPicker(_1,_2,_3,_4){var _5=this.picker;_3=isc.addProperties(_3||{},{callingForm:this.form,callingFormItem:this});if(isc.isA.String(_5)&&isc.isA.Canvas(window[_5])){_5=this.picker=window[_5]}
if(!_5){_5=this.picker=this.createPicker(_3);if(!isc.isA.Function(_5.dataChanged)){_5.dataChanged=new Function()}
_5.observe(_5,"resized","observed.placeNear(observed.lastShowRect)");if(this.pickerDataChanged&&_5.dataChanged){this.observe(_5,"dataChanged","observer.pickerDataChanged(observed)")}}else{isc.addProperties(_5,_3)}
var _6=_5.getID();if(!_4){if(this.getPickerRect){_4=this.getPickerRect()}else if(_2){var _7=this.getIconPageRect(_2);_4=[_7[0],_7[1]]}
else _4=[isc.EH.getX(),isc.EH.getY()]}
_5.lastShowRect=_4;_5.setRect(_4);if(!_5.isDrawn()){_5.moveTo(null,-9999);_5.draw()}
this.picker.placeNear(_4);if(isc.isA.Function(_5.setData)){if(isc.isA.Function(this.getPickerData)){_5.setData(this.getPickerData(_5))}else _5.setData(this.getValue(_5))}
var _8=_1?null:_6+".hide()";if(_1&&isc.isA.Function(_5.clickMaskClicked))
_8=_6+".clickMaskClicked()";_5.showClickMask(_8,!_1,_5);if(_1!=null&&_5.isModal==null)_5.isModal=_1;_5.show();_5.bringToFront();_5.focus();return false}
,isc.A.createPicker=function isc_FormItem_createPicker(_1){return this.createAutoChild("picker",_1)}
,isc.A.hidePicker=function isc_FormItem_hidePicker(){if(!this.picker)return;this.picker.hideClickMask();this.picker.hide()}
,isc.A.redraw=function isc_FormItem_redraw(_1){if(!this.isDrawn())return;if(this.hasFocus)this.$27k=true;if(!this.hasFocus&&this.items!=null){for(var i=0;i<this.items.length;i++){if(this.items[i].hasFocus)this.$27k=true}}
if(this.containerWidget.redrawFormItem){this.containerWidget.redrawFormItem(this,_1)}else{this.containerWidget.markForRedraw("Form item redrawn"+(_1?": "+_1:isc.emptyString))}}
,isc.A.adjustOverflow=function isc_FormItem_adjustOverflow(_1){if(!this.$28j){this.$28j=[this.getID(),"  overflow changed: "]}
if(_1==null)this.$28j[2]="No Reason Specified.";else this.$28j[2]=_1;if(isc.isA.DynamicForm(this.containerWidget))
this.containerWidget.adjustOverflow(this.$28j.join(isc.emptyString))}
,isc.A.show=function isc_FormItem_show(_1){if(this.visible==true)return;this.visible=true;if(!_1)this.showIf=null;if(this.containerWidget.redrawFormItem)this.containerWidget.redrawFormItem(this,"showing form item");else this.containerWidget.markForRedraw("showing form item");this.visibilityChanged(true)}
,isc.A.hide=function isc_FormItem_hide(_1){if(this.visible==false)return;this.visible=false;if(!_1)this.showIf=null;if(this.containerWidget.redrawFormItem)this.containerWidget.redrawFormItem(this,"hiding form item");else this.containerWidget.markForRedraw("hiding form item");this.visibilityChanged(true)}
,isc.A.$20s=function isc_FormItem__updateHTMLForPageLoad(){if(!isc.Browser.isSafari||!this.isDrawn())return;this.$28d()}
,isc.A.$28d=function isc_FormItem__resetWidths(){if(!this.isDrawn())return;var _1=this.clipValue;var _2=this.getOuterTableElement();if(_2)_2.style.width=this.getInnerWidth();if(this.showPickerIcon){var _3=this.$26j();if(_3)_3.style.width=this.getElementWidth();var _4=this.getPickerIcon(),_5=this.$6h(_4);if(_5){_5.style.height=this.getPickerIconHeight();_5.style.width=this.getPickerIconWidth()}}
var _6=this.getTextBoxWidth(),_7=this.getTextBoxHeight(),_8=this.$6f();if(_8){if(_1)_8.style.width=_6+"px";else _8.style.minWidth=_6+"px";_8.style.height=_7+"px"}
if(this.$26r()){var _9=this.getFocusElement()
if(_9){_9.style.width=_6;_9.style.height=_7}}}
,isc.A.hasElement=function isc_FormItem_hasElement(){return this.hasDataElement()}
,isc.A.hasDataElement=function isc_FormItem_hasDataElement(){return this.$23x}
,isc.A.getElement=function isc_FormItem_getElement(_1){return this.getDataElement(_1)}
,isc.A.getFocusElement=function isc_FormItem_getFocusElement(){if(!this.isDrawn()||!this.$mb())return null;if(this.hasDataElement())return this.getDataElement();if(this.$26r()){if(!this.$27u){this.$27u=isc.Element.get(this.getID()+"__focusProxy")}
return this.$27u}
return this.$6f()}
,isc.A.$27l=function isc_FormItem__getCurrentFocusElement(){if(this.hasFocus==null&&!isc.EH.$0o==this){return null}
var _1=this.$28k;if(isc.Browser.isIE&&_1!=this.getActiveElement()){this.logInfo("not returning focus element "+this.echoLeaf(_1)+" since it's not active"+isc.EH.$m8(),"nativeFocus");if(this.hasFocus){this.hasFocus=false;this.elementBlur()}
this.$28k=null;return null}
return _1}
,isc.A.getDataElement=function isc_FormItem_getDataElement(_1){if(_1==null){var _2=this}else{var _2=this.form.getItem(_1)}
if(!_2.hasDataElement())return null;if(!this.isDrawn())return;var _3=this.$25v;if(_3==null){_3=(this.$25v=isc.Element.get(this.getDataElementId()))}
return _3}
,isc.A.getOuterElement=function isc_FormItem_getOuterElement(){if(!this.isDrawn())return null;var _1=this.$27e;if(this.$26x(_1)){return this.getOuterTableElement()}
if(this.showPickerIcon){return this.$26j()}
var _2=this.$6f();if(_2==null){_2=this.getHandle()}
return _2}
,isc.A.getHandle=function isc_FormItem_getHandle(){if(!this.isDrawn())return null;if(this.$200())return this.getAbsDiv();if(this.containerWidget==this.form)return this.getFormCell();return isc.Element.get(this.$t4(this.$24d))}
,isc.A.getOuterTableElement=function isc_FormItem_getOuterTableElement(){return this.$26g(this.$24w)}
,isc.A.$28l=function isc_FormItem__overElement(_1){if(!_1)_1=isc.EH.lastEvent;var _2=_1.itemInfo;return(_2&&_2.overElement)}
,isc.A.$28m=function isc_FormItem__overTextBox(_1){if(!_1)_1=isc.EH.lastEvent;var _2=_1.itemInfo;return(_2&&(_2.overTextBox||_2.overElement))}
,isc.A.$28n=function isc_FormItem__overControlTable(_1){if(!_1)_1=isc.EH.lastEvent;var _2=_1.itemInfo;return(_2&&(_2.overControlTable||this.$28m(_1)||(_2.overIcon&&this.getIcon(_2.overIcon)==this.getPickerIcon())))}
,isc.A.getFormCellID=function isc_FormItem_getFormCellID(){return this.$t4(this.$25e)}
,isc.A.getFormCell=function isc_FormItem_getFormCell(){return isc.Element.get(this.getFormCellID())}
,isc.A.getDisplayValue=function isc_FormItem_getDisplayValue(_1){var _2;return this.mapValueToDisplay(_1!==_2?_1:this.getValue())}
,isc.A.mapValueToDisplay=function isc_FormItem_mapValueToDisplay(_1){var _2=this.$28o(_1);_1=this.$28p(_2);return _1}
,isc.A.$28p=function isc_FormItem__formatDataType(_1){if(this.applyStaticTypeFormat){if(this.formatValue!=null){var _2=this.form,_3=this.form?this.form.values:{};return this.formatValue(_1,_3,_2,this)}}else if(this.formatEditorValue!=null){var _2=this.form,_3=this.form?this.form.values:{};return this.formatEditorValue(_1,_3,_2,this)}else if(this.$142!=null){var _2=this.form,_3=this.form?this.form.values:{};if(this.$14v&&this.$14v.editFormatter){return this.$14v.editFormatter(_1,this,_2,_3)}else{return this.$142(_1,this,_2,_3)}}
if(isc.isA.Date(_1)){if(this.$28q()){var _4=this.$28r();var _5=isc.SimpleType.inheritsFrom(this.getType(),"time");return isc.Time.toTime(_1,_4,_5)}else{var _4=this.$28s();var _6=this.getType(),_7=isc.SimpleType.inheritsFrom(_6,"date"),_8=isc.SimpleType.inheritsFrom(_6,"datetime");if(_7&&!_8){return _1.toShortDate(_4,false)}else{if(this.useShortDateFormat){return _8?_1.toShortDatetime(_4,true):_1.toShortDate(_4,true)}else{return _1.toNormalDate(_4)}}}}
if(this.$14v&&isc.isA.Function(this.$14v.normalDisplayFormatter)&&this.applyStaticTypeFormat)
{return this.$14v.normalDisplayFormatter(_1,this,this.form,this.form.values)}
if(this.$141&&this.applyStaticTypeFormat){return this.$141(_1,this,this.form,this.form.values)}
if(_1==null)_1=this.emptyDisplayValue;else{_1=isc.iscToLocaleString(_1);if(this.emptyDisplayValue!=isc.emptyString&&_1==isc.emptyString)
_1=this.emptyDisplayValue}
return _1}
,isc.A.$28q=function isc_FormItem__formatAsTime(){var _1=this.getType(),_2=isc.SimpleType.inheritsFrom(_1,"time"),_3=_2;if(this.timeFormatter==null&&this.dateFormatter!=null)_3=false;if(this.dateFormatter==null&&this.timeFormatter!=null)_3=true;return _3}
,isc.A.$28s=function isc_FormItem__getDateFormatter(){if(this.dateFormatter!=null)return this.dateFormatter;var _1=this.getType(),_2=isc.SimpleType.inheritsFrom(_1,"date"),_3=isc.SimpleType.inheritsFrom(_1,"datetime");if(_2&&this.displayFormat!=null)return this.displayFormat;if(_3&&this.form.datetimeFormatter!=null)return this.form.datetimeFormatter;return this.form.dateFormatter}
,isc.A.$28r=function isc_FormItem__getTimeFormatter(){if(this.timeFormatter!=null)return this.timeFormatter;if(this.displayFormat!=null&&isc.SimpleType.inheritsFrom(this.type,"time")){return this.displayFormat}
return this.form.timeFormatter}
,isc.A.mapDisplayToValue=function isc_FormItem_mapDisplayToValue(_1){_1=this.$28t(_1);return this.$28u(_1)}
,isc.A.$28t=function isc_FormItem__parseDisplayValue(_1){if(!this.applyStaticTypeFormat){if(this.parseEditorValue!=null){_1=this.parseEditorValue(_1,this.form,this)}else if(this.$143!=null){var _2=this.form,_3=_2?_2.values:{};if(this.$14v&&this.$14v.parseInput){_1=this.$14v.parseInput(_1,this,_2,_3)}else{_1=this.$143(_1,this,_2,_3)}}
if(_1!=null&&isc.isA.String(_1)){var _4=this.getType();var _5=isc.SimpleType.inheritsFrom(_4,"date"),_6=isc.SimpleType.inheritsFrom(_4,"time"),_7=_5&&isc.SimpleType.inheritsFrom(_4,"datetime"),_8=(_1=="");if(_5||_6){if(this.$28q()){if(_8&&this.allowEmptyValue){_1=null}else{var _9;if(!_6&&isc.isA.Date(this._value)){_9=this._value}
var _10=isc.Time.parseInput(_1,false,false,!_6,_9);if(isc.isA.Date(_10))_1=_10}}else{var _11=this.inputFormat;if(_11==null){_11=Date.mapDisplayFormatToInputFormat(this.$28s())}
var _12=_5&&!_7;var _13=Date.parseInput(_1,_11,this.centuryThreshold,false,!_12);if(isc.isA.Date(_13))_1=_13}}}}
return _1}
,isc.A.getType=function isc_FormItem_getType(){if(this.type!=null)return this.type;if(this.criteriaField&&this.form&&this.form.dataSource){var _1=isc.DataSource.get(this.form.dataSource);var _2=_1.getField(this.criteriaField);if(_2)return _2.type}
return null}
,isc.A.setToZeroTime=function isc_FormItem_setToZeroTime(_1){Date.setToZeroTime(_1)}
,isc.A.$28o=function isc_FormItem__mapKey(_1,_2){var _3=_2?null:_1;var _4=this.getValueMap();if(!_4)return _3;if(isc.isA.String(_4))_4=this.getGlobalReference(_4);if(isc.isAn.Array(_4)&&!isc.isAn.Array(_1))return _3;var _5;if(isc.isAn.Array(_1)){_5="";for(var i=0;i<_1.length;i++){var _7=isc.getValueForKey(_1[i],_4,_1[i]);var _8=this.$2n(_1[i]);if(_8!=null&&_1.length>1)_5+=_8;_5+=_7;if(i!=_1.length-1)_5+=this.multipleValueSeparator}}else{_5=isc.getValueForKey(_1,_4,_3)}
return _5}
,isc.A.$28u=function isc_FormItem__unmapKey(_1){var _2=this.getValueMap();if(!_2)return _1;if(isc.isA.String(_2))_2=this.getGlobalReference(_2);if(isc.isAn.Array(_2))return _1;var _3=isc.getKeyForValue(_1,_2);if(_3==_1&&_3==this.emptyDisplayValue)_3="";return _3}
,isc.A.setValueMap=function isc_FormItem_setValueMap(_1){this.valueMap=_1;this.updateValueMap()}
,isc.A.setValueIcons=function isc_FormItem_setValueIcons(_1){this.valueIcons=_1;if(this.isDrawn())this.redraw()}
,isc.A.setOptions=function isc_FormItem_setOptions(_1){return this.setValueMap(_1)}
,isc.A.updateValueMap=function isc_FormItem_updateValueMap(_1){if(_1!=false){this.setElementValue(this.mapValueToDisplay(this.getValue()))}
if(this.hasElement())this.setElementValueMap(this.getValueMap())}
,isc.A.setElementValueMap=function isc_FormItem_setElementValueMap(_1){}
,isc.A.getValueMap=function isc_FormItem_getValueMap(){var _1=this.valueMap;if(isc.isA.String(_1)){_1=this.getGlobalReference(_1)}
var _2=this.$28v;if(_2!=null){if(_1==null)_1=_2;else{if(isc.isAn.Array(_1)){var _3=_1;_1={};for(var i=0;i<_3.length;i++){_1[_3[i]]=_3[i]}}
isc.addProperties(_1,_2)}}
return _1}
,isc.A.getValueFieldName=function isc_FormItem_getValueFieldName(){if(this.valueField)return this.valueField;if(this.form.dataSource&&this.foreignKey)
return isc.DS.getForeignFieldName(this,this.form.dataSource);var _1=this.getFieldName();return _1||"name"}
,isc.A.getDisplayFieldName=function isc_FormItem_getDisplayFieldName(){if(this.displayField)return this.displayField;var _1=this.getOptionDataSource();var _2=this.getValueFieldName();if(_1&&_1!=isc.DataSource.getDataSource(this.form.dataSource)&&_1.getField(_2)&&_1.getField(_2).hidden==true){return _1.getTitleField()}}
,isc.A.$20c=function isc_FormItem__displayFieldValueFromFormValues(){if(this.displayField!=null){var _1=this.form.getValues(),_2=_1[this.getFieldName()],_3=_1[this.displayField];if(_3!=null){var _4={};_4[_2]=_3}
this.$28v=_4}}
,isc.A.getOptions=function isc_FormItem_getOptions(){return this.getValueMap()}
,isc.A.getOptionDataSource=function isc_FormItem_getOptionDataSource(){var _1=this.optionDataSource;if(_1==null&&this.form&&this.form.dataSource){if(this.foreignKey)_1=isc.DS.getForeignDSName(this,this.form.dataSource);else _1=this.form.dataSource}
if(isc.isA.String(_1))_1=isc.DataSource.getDataSource(_1);return _1}
,isc.A.getValueMapTitle=function isc_FormItem_getValueMapTitle(_1){var _2=this.getValueMap();if(isc.isAn.Array(_2))return(_2.contains(_1)?_1:"");return _2[_1]}
,isc.A.saveValue=function isc_FormItem_saveValue(_1,_2){var _3;this._value=_1;this.$25t=_2;if(this.isDrawn()){if(this.$20z())this.$28w(_1)}
if(this.form==null)return;if(_1==_3&&this.$28x){this.form.clearItemValue(this)}else{this.form.saveItemValue(this,_1)}}
,isc.A.$28w=function isc_FormItem__setHiddenDataElementValue(_1){var _2=this.$26f();if(_2)_2.value=_1}
,isc.A.setValue=function isc_FormItem_setValue(_1,_2){this.$28y=true;var _3=(this.maintainSelectionOnTransform&&this.hasFocus&&(this.$28z()!=this.$25f));if(_3)this.rememberSelection();if(this.$280!=null){isc.Timer.clearTimeout(this.$280);this.$280=null}
var _4;if(_1==null&&!_2){var _5=this.getDefaultValue();if(_5!=null){_4=true;_1=_5}}
if(this.length!=null&&_1!=null&&isc.isA.String(_1)&&_1.length>this.length)
{_1=_1.substring(0,this.length)}
this.saveValue(_1,_4);if(_1!=null&&this.shouldFetchMissingValue(_1)){this.$281();this.$282(_1)}else{if(this.$283==null||!this.compareValues(this.$283,this._value))
{this.$284()}}
var _6=this.mapValueToDisplay(_1);this.setElementValue(_6,_1);if(_3)this.resetToLastSelection(true);return _1}
);isc.evalBoundary;isc.B.push(isc.A.shouldFetchMissingValue=function isc_FormItem_shouldFetchMissingValue(_1){if(this.fetchMissingValues==false)return false;if(this.getOptionDataSource()==null)return false;var _2=false;if(this.$285!=null&&(this.$286||this.$285.find(this.getValueFieldName(),_1)!=null))
{_2=true}
if(_2)return false;if(this.alwaysFetchMissingValues)return true;if(this.getDisplayFieldName()==null)return false;var _3=(this.$28o(_1,true)!=null);return!_3}
,isc.A.setDefaultValue=function isc_FormItem_setDefaultValue(_1){var _2=this.defaultValue,_3;this.defaultValue=_1;if(this.isSetToDefaultValue()||(this._value==null&&_2===_3))
this.clearValue()}
,isc.A.$282=function isc_FormItem__checkForDisplayFieldValue(_1){if(this.$287==null||!this.$287[_1]){if(!this.$287)this.$287={};this.$287[_1]=true;var _2=isc.addProperties({},this.optionCriteria);if(!this.filterLocally){var _3={};_3[this.getValueFieldName()]=_1;_2=isc.DataSource.combineCriteria(_2,_3)}
var _4=isc.addProperties({},this.optionFilterContext,{showPrompt:false,clientContext:{dataValue:_1,filterLocally:this.filterLocally},componentId:this.containerWidget.getID(),componentContext:this.getFieldName()});var _5;if(this.optionOperationId!==_5){_4.operationId=this.optionOperationId}
this.getOptionDataSource().fetchData(_2,{target:this,methodName:"fetchMissingValueReply"},_4)}}
,isc.A.fetchMissingValueReply=function isc_FormItem_fetchMissingValueReply(_1,_2,_3){var _4,_5=_1.clientContext.dataValue,_6=_1.clientContext.filterLocally,_7=this.getDisplayFieldName(),_8=this.getValueFieldName();delete this.$287[_5];if(_2)_4=_2.find(_8,_5);if(!_4){this.logInfo("Unable to retrieve display value for data value:"+_5+" from dataSource "+this.getOptionDataSource());if(!_6){if(_2!=null&&_2.getLength()>0){this.logWarn("FetchMissingValues - filterLocally is false yet optionDataSource "+"fetch included records that do not match our current data value. Ignoring "+"these values.","fetchMissingValues");this.logDebug("Data returned:"+this.echoAll(_2),"fetchMissingValues")}
return}}
this.$288(_2);if(_6)this.$286=true;var _9=(this._value==_5)&&this.$289();this.updateDisplayValueMap(_9)}
,isc.A.$288=function isc_FormItem__addDataToDisplayFieldCache(_1,_2){if(this.$285==null){this.$285=[]}
if(_2){var _3=this.$285,_4=_3.length,_5=this.getValueFieldName();for(var i=0;i<_1.length;i++){var _7=true,_8=_1[i];for(var j=0;j<_4;j++){if(_3[j][_5]==_8[_5]){_7=false;break}}
if(_7==false)continue;_3[_3.length]=_8}}else{this.$285.addList(_1)}
var _10=this.getOptionDataSource();if(!this.isObserving(_10,"dataChanged")){this.observe(_10,"dataChanged","observer.dataSourceDataChanged(observed,dsRequest,dsResponse)")}}
,isc.A.$289=function isc_FormItem__refreshForDisplayValueChange(){return true}
,isc.A.updateDisplayValueMap=function isc_FormItem_updateDisplayValueMap(_1){this.$284();var _2=this.$285,_3=this.getDisplayFieldName(),_4=this.getValueFieldName();var _5=this.$28v={};var _6;for(var i=0;i<_2.length;i++){var _8=_2[i];var _9=_8[_4],_10=_8[_3];if(_5[_9]!==_6){if(_5[_9]!=_10){this.logWarn("Deriving valueMap for '"+_4+"' from dataSource based on displayField '"+_3+"'. This dataSource contains more than one record with "+_4+" set to "+_9+" with differing "+_3+" values."+" Derived valueMap is therefore unpredictable.","fetchMissingValues")}
continue}
_5[_8[_4]]=_8[_3]}
this.updateValueMap(_1)}
,isc.A.invalidateDisplayValueCache=function isc_FormItem_invalidateDisplayValueCache(_1){this.$28v=null;this.$285=null;this.$281();this.$286=false;var _2=this.getOptionDataSource();if(_2!=null&&this.isObserving(_2,"dataChanged")){this.ignore(_2,"dataChanged")}
if(_1)return;if(this.form.$4t(this)){this.$20c()}else if(this._value!=null&&this.shouldFetchMissingValue(this._value)){this.$281();this.$282(this._value)}
this.updateValueMap()}
,isc.A.dataSourceDataChanged=function isc_FormItem_dataSourceDataChanged(_1,_2,_3){var _4=this.logIsDebugEnabled("fetchMissingValues");if(_4){this.logDebug("dataSourceDataChanged is firing for request:"+this.echo(_2),"fetchMissingValues")}
var _5=this.$285;if(_5==null)return;if(_3.invalidateCache){if(_4){this.logDebug("Request had invalidateCache set, dropping cached display values","fetchMissingValues")}
this.invalidateDisplayValueCache()}else{var _6=this.getDisplayFieldName(),_7=this.getValueFieldName();var _8=_1.getUpdatedData(_2,_3,true),_9=_2.operationType=="add",_10=_2.operationType=="update",_11=_2.operationType=="remove";if(_4){this.logDebug("Operation type:"+_2.operationType+", updateData:"+this.echoAll(_8),"fetchMissingValues")}
if(_8==null||(!_9&&!_11&&!_10))return;if(!isc.isAn.Array(_8)){_8=[_8]}
var _12=false,_7=this.getValueFieldName();if(_9){_5.addList(_8);_12=_8.find(_7,this._value)!=null}else{var _13=_1.getPrimaryKeyFields();for(var i=0;i<_8.length;i++){var _15=_8[i],_16=isc.applyMask(_15,_13);var _17=_1.findByKeys(_16,_5);if(_17==-1){if(_11)continue;_5.add(_15)}else{if(_5[_17][_7]==this._value){_12=true}
if(_11){_5.removeAt(_17)}else{_5[_17]=_15}}}}
this.updateDisplayValueMap(_12&&this.$289())}}
,isc.A.getSelectedRecord=function isc_FormItem_getSelectedRecord(){if(this.$283!=null){if(!this.compareValues(this.$283,this._value)){this.logInfo("getSelectedRecord - cached record doesn't match new value - dropping","fetchMissingValues");this.$281()}}
return this.$29a}
,isc.A.$284=function isc_FormItem__updateSelectedRecord(){if(this._value==null||this.$285==null){this.$281()}else{var _1=this.getValueFieldName();this.$29a=this.$285.find(_1,this._value);this.$283=this._value}}
,isc.A.$281=function isc_FormItem__clearSelectedRecord(){delete this.$29a;delete this.$283}
,isc.A.clearValue=function isc_FormItem_clearValue(){this.$28x=true;this.setValue();delete this.$28x}
,isc.A.setElementValue=function isc_FormItem_setElementValue(_1,_2){if(!this.isDrawn())return;var _3;if(_2===_3){_2=this._value}
if(this.hasDataElement()){var _4=this.getDataElement();if(_4!=null){this.$29b(_2);return _4.value=_1}}
var _5=this.$6f();if(_5!=null){if(this.showValueIconOnly)_1=isc.emptyString;var _6=this.$2n(_2);if(_6!=null)
_1=_6+(_1!=null?_1:isc.emptyString);if(isc.Browser.isIE){if(_1&&_1.startsWith("<nobr>"))
_1=_1.substring(6);if(_1&&_1.endsWith("</nobr>"))
_1=_1.substring(0,_1.length-7);try{_5.innerHTML=_1}catch(e){var _7=document.createElement("span");_7.innerHTML=_1;_5.innerHTML="";_5.appendChild(_7)}}else{_5.innerHTML=_1}
if(!this.clipValue||this.height==null||this.width==null){this.adjustOverflow("textBox value changed")}}}
,isc.A.$29b=function isc_FormItem__updateValueIcon(_1){if(this.suppressValueIcon||!this.isDrawn())return;var _2=this.$253(_1),_3=this.$260();if(_2!=null){if(this.imageURLSuffix!=null)_2+=this.imageURLSuffix;_2=isc.Canvas.getImgURL(_2,this.imageURLPrefix||this.baseURL||this.imgDir);if(_3!=null){_3.src=_2}else{var _4=false;if(this.hasDataElement()){var _5=this.getDataElement();if(_5!=null){isc.Element.insertAdjacentHTML(_5,"beforeBegin",this.$2n(_1));_5.style.width=this.getTextBoxWidth(_1);_4=true}}else{var _6=this.$6f();if(_6!=null){isc.Element.insertAdjacentHTML(_6,"afterBegin",this.$2n(_1));_4=true}}
if(!_4)this.redraw()}}else if(_3!=null&&!(isc.isAn.Array(_1)&&_1.length>1)){isc.Element.clear(_3);if(this.hasDataElement()){var _5=this.getDataElement();_5.style.width=this.getTextBoxWidth(_1)}}}
,isc.A.setPrompt=function isc_FormItem_setPrompt(_1){this.prompt=_1}
,isc.A.setHint=function isc_FormItem_setHint(_1){this.hint=_1;if(this.showHint)this.redraw()}
,isc.A.setHintStyle=function isc_FormItem_setHintStyle(_1){if(!this.$21f()&&this.getHint()){var _2=this.$26q();if(_2)_2.className=_1}}
,isc.A.$29c=function isc_FormItem__showInFieldHint(){if(!this.$29d){var _1=this.getDataElement();if(_1){_1.className=this.$29e();if(this.isA.TextItem){this.$29f=_1.type;_1.type="text"}}else{var _2=this.$6f();if(_2!=null){_2.className=this.$29e()}}
var _3=this.getHint();if(_3)_3=_3.unescapeHTML();this.setElementValue(_3);this.$29d=true}}
,isc.A.$29g=function isc_FormItem__hideInFieldHint(){if(this.$29d){var _1=this.getDataElement();if(_1){_1.className=this.getTextBoxStyle();if(this.$29f){_1.type=this.$29f;delete this.$29f}}else{var _2=this.$6f();if(_2!=null){_2.className=this.getTextBoxStyle()}}
this.setElementValue(isc.emptyString);this.$29d=false}}
,isc.A.$29e=function isc_FormItem__getInFieldHintStyle(){return this.textBoxStyle+"Hint"}
,isc.A.$21f=function isc_FormItem__getShowHintInField(){if(this.showHint&&this.showHintInField){if(isc.isA.TextItem(this)||isc.isA.TextAreaItem(this)||isc.isA.SelectItem(this))
{return true}}
return false}
,isc.A.getDefaultValue=function isc_FormItem_getDefaultValue(){if(this.defaultDynamicValue){this.convertToMethod("defaultDynamicValue");var _1=this,_2=this.form,_3=this.form.getValues();return this.defaultDynamicValue(_1,_2,_3)}
return this.defaultValue}
,isc.A.setToDefaultValue=function isc_FormItem_setToDefaultValue(){return this.clearValue()}
,isc.A.isSetToDefaultValue=function isc_FormItem_isSetToDefaultValue(){return(this.$25t==true)}
,isc.A.updateValue=function isc_FormItem_updateValue(){if(!this.hasElement()||this.getDataElement()==null)return;var _1=this.getElementValue();return this.$20l(_1)}
,isc.A.$20l=function isc_FormItem__updateValue(_1){if(this.$29h){_1=this.$29i(_1)}
_1=this.mapDisplayToValue(_1);return this.storeValue(_1)}
,isc.A.storeValue=function isc_FormItem_storeValue(_1){if(this.compareValues(_1,this._value)){return true}
if(this.$29j){if(this.compareValues(_1,this.$29k)){return true}}
var _2=this.handleChange(_1,this._value);if(this.destroyed)return;_1=this.$29k;this.updateAppearance(_1);if(!this.compareValues(_1,this._value))this.saveValue(_1);delete this.$29k;this.handleChanged(this._value);return _2}
,isc.A.handleChanged=function isc_FormItem_handleChanged(_1){if(this.form.rulesEngine!=null){this.form.rulesEngine.processChanged(this.form,this)}
if(this.changed)this.changed(this.form,this,_1);if(this.form){if(!this.suppressItemChanged&&this.form.itemChanged!=null)
this.form.itemChanged(this,_1);this.checkForImplicitSave()}}
,isc.A.checkForImplicitSave=function isc_FormItem_checkForImplicitSave(){if(this.getImplicitSave()){var _1=this;this.form.awaitingImplicitSave=true;this.form.fireOnPause("fiImplicitSave",function(){if(_1.form.awaitingImplicitSave){_1.form.performImplicitSave(_1,true)}},this.form.implicitSaveDelay)}}
,isc.A.updateAppearance=function isc_FormItem_updateAppearance(_1){if(this.valueIcons||this.getValueIcon){this.$29b(_1)}}
,isc.A.getValue=function isc_FormItem_getValue(){var _1;if(this._value!==_1){return this._value}
return this.form.getSavedItemValue(this)}
,isc.A.getElementValue=function isc_FormItem_getElementValue(){if(this.$29d)return null;var _1=this.getDataElement();if(!_1)return null;return _1.value}
,isc.A.resetValue=function isc_FormItem_resetValue(){var _1=this.form.$20d[this.getFieldName()];this.setValue(_1)}
,isc.A.shouldSubmitValue=function isc_FormItem_shouldSubmitValue(){return this.form.$21h()}
,isc.A.setCanEdit=function isc_FormItem_setCanEdit(_1){var _2=!this.isReadOnly();this.canEdit=_1;var _3=!this.isReadOnly();if(_2!=_3)this.updateCanEdit()}
,isc.A.updateCanEdit=function isc_FormItem_updateCanEdit(){if(this.isDisabled())return;var _1=this.isReadOnly();this.setElementReadOnly(_1);this.$29l();this.$29b();this.updateState()}
,isc.A.setElementReadOnly=function isc_FormItem_setElementReadOnly(_1){if(this.hasDataElement())this.redraw()}
,isc.A.$29m=function isc_FormItem__setElementReadOnly(_1){if(this.hasDataElement()){var _2=this.getDataElement();if(_2){if(!_1&&!_2.readOnly){_2.disabled=_1}else{_2.readOnly=_1}
_2.tabIndex=this.$269()}}else if(this.$mb()){var _2=this.getFocusElement();if(_2)_2.tabIndex=this.$269()}}
,isc.A.isReadOnly=function isc_FormItem_isReadOnly(){if(this.canEdit!=null)return!this.canEdit;var _1=this;while(_1.parentItem){if(_1.parentItem.canEdit!=null)return!_1.parentItem.canEdit;_1=_1.parentItem}
if(this.form&&this.form.canEdit!=null)return!this.form.canEdit;return false}
,isc.A.isEditable=function isc_FormItem_isEditable(){return true}
,isc.A.getCriteriaFieldName=function isc_FormItem_getCriteriaFieldName(){return this.criteriaField||this.includeFrom||this.getFieldName()}
,isc.A.getCriteriaValue=function isc_FormItem_getCriteriaValue(){return this.getValue()}
,isc.A.hasAdvancedCriteria=function isc_FormItem_hasAdvancedCriteria(){return this._value!=null&&this.operator!=null||this.$29n()}
,isc.A.$29n=function isc_FormItem__shouldAllowExpressions(){var _1=isc.isA.TextItem(this)||isc.isA.TextAreaItem(this)||isc.isA.CanvasItem(this)||isc.isA.ContainerItem(this)||isc.isA.DateItem(this);if(!_1)return false;_1=this.allowExpressions;if(_1==null)_1=this.form.allowExpressions;return _1}
,isc.A.getOperator=function isc_FormItem_getOperator(_1,_2){var _3;if(this.operator){_3=this.operator}else if(_2){_3="inSet"}else{var _4=this.getType();if(this.valueMap||this.optionDataSource||isc.SimpleType.inheritsFrom(_4,"enum")||isc.SimpleType.inheritsFrom(_4,"boolean")||isc.SimpleType.inheritsFrom(_4,"float")||isc.SimpleType.inheritsFrom(_4,"integer"))
{_3="equals"}else{if(_1==null)_1="substring";_3=isc.DataSource.getCriteriaOperator(null,_1)}}
return _3}
,isc.A.canEditCriterion=function isc_FormItem_canEditCriterion(_1,_2){if(_1.fieldName!=null&&_1.fieldName==this.getCriteriaFieldName()&&_1.operator==this.getOperator(null,isc.isAn.Array(_1.value)))
{return true}
if(this.$29n()){var _3=isc.DS.getCriteriaFields(_1,this.form.expressionDataSource||this.form.dataSource,true);return _3.contains(this.getCriteriaFieldName())}
return false}
,isc.A.canEditSimpleCriterion=function isc_FormItem_canEditSimpleCriterion(_1){var _2=this.getCriteriaFieldName();return _2==_1}
,isc.A.setSimpleCriterion=function isc_FormItem_setSimpleCriterion(_1,_2){this.setValue(_1)}
,isc.A.getCriterion=function isc_FormItem_getCriterion(_1){var _2=this.getCriteriaValue();if(_2==null||isc.is.emptyString(_2))return;if(isc.isAn.Array(_2)){var _3=_2.getUniqueItems();if(_2.length==0||(_3.length==1&&isc.isA.String(_2[0])&&isc.is.emptyString(_2[0])))return}
var _4=this.getOperator(_1,isc.isAn.Array(_2)),_5=this.getCriteriaFieldName();var _6={fieldName:_5,operator:_4,value:_2};if(this.$29n()){var _7=this.parseValueExpressions(_2,_5,_4);if(_7!=null)_6=_7}
return _6}
,isc.A.setCriterion=function isc_FormItem_setCriterion(_1){var _2=this.$29n(),_3=_1?_1.value:null;if(_2){_3=this.buildValueExpressions(_1)}
this.setValue(_3)}
,isc.A.clearErrors=function isc_FormItem_clearErrors(){var _1=this.getFieldName();if(_1)this.form.clearFieldErrors(_1,true)}
,isc.A.setError=function isc_FormItem_setError(_1){var _2=this.getFieldName();if(_2)this.form.setError(_2,_1)}
,isc.A.hasErrors=function isc_FormItem_hasErrors(){if(this.parentItem!=null)return this.parentItem.hasErrors();var _1=this.getFieldName();if(_1&&this.form)return this.form.hasFieldErrors(_1);var _2=this.getDataPath();if(_2&&this.form)return this.form.hasFieldErrors(_2);return false}
,isc.A.validate=function isc_FormItem_validate(){var _1=this.hasErrors(),_2=[],_3=null,_4=false;var _5=isc.rpc.startQueue();var _6=this.getValue(),_7=isc.addProperties({},this.form.getValues()),_8={unknownErrorMessage:this.form.unknownErrorMessage};var _9=this.form.validateFieldAndDependencies(this,this.validators,_6,_7,_8);var _10=this.name;if(_10==null)_10=this.getDataPath();if(_10==null){this.logWarn("item has no specified name or dataPath - "+"unable to meaningfully store validation errors.")}
if(!_5)isc.rpc.sendQueue();if(_9!=null){if(_9.resultingValue!=null){this.setValue(_9.resultingValue)}
if(!_9.valid){_2=_9.errors[_10];if(_2==null)_2=[]}
_4=_9.stopOnError;_3=_9.errors}
var _11=false;if(_2.length>0||_1){if(_2.length>0){this.form.setFieldErrors(_10,_2,false)}else{this.form.clearFieldErrors(_10,false)}
_11=true;if(_4)this.focusInItem()}
for(var _12 in _3){if(_12!=_10){var _13=_3[_12];if((_13!=null&&!isc.isAn.emptyObject(_13))||this.form.hasFieldErrors(_12))
{this.form.setFieldErrors(_12,_13,false);_11=true}}}
if(_11){this.redraw()}
return(_2.length==0)}
,isc.A.setRequired=function isc_FormItem_setRequired(_1){if(_1==this.required)return;this.required=_1;if(this.form==null)return;if(_1){var _2=this.form.getRequiredValidator(this);this.addValidator(_2)}else{this.removeValidator({type:"required"})}
this.redraw()}
,isc.A.addValidator=function isc_FormItem_addValidator(_1){if(this.validators==null)this.validators=[];else if(!isc.isAn.Array(this.validators))this.validators=[this.validators];if(this.validators.$3v){this.validators=this.validators.duplicate()}
this.validators.add(_1)}
,isc.A.removeValidator=function isc_FormItem_removeValidator(_1){if(this.validators==null)return;if(!isc.isAn.Array(this.validators))this.validators=[this.validators];if(this.validators.$3v){this.validators=this.validators.duplicate()}
var _2=this.validators.find(_1);this.validators.remove(_2)}
,isc.A.setAutoComplete=function isc_FormItem_setAutoComplete(_1){this.autoComplete=_1;this.$199()}
,isc.A.$199=function isc_FormItem__handleAutoCompleteChange(){var _1=this.$28z();if(isc.Browser.isIE&&this.hasDataElement()){var _2=this.getDataElement();if(_2)_2.autoComplete=(_1=="native"?"":"off")}}
,isc.A.$28z=function isc_FormItem__getAutoCompleteSetting(){if(this.autoComplete!=null)return this.autoComplete;return this.form.autoComplete}
,isc.A.autoCompleteEnabled=function isc_FormItem_autoCompleteEnabled(){if(isc.Browser.isSafari)return false;return this.$28z()=="smart"}
,isc.A.uniqueMatchOnly=function isc_FormItem_uniqueMatchOnly(){if(this.uniqueMatch!=null)return this.uniqueMatch;return this.form.uniqueMatch}
,isc.A.getCandidates=function isc_FormItem_getCandidates(){var _1=this.autoCompleteCandidates;if(_1==null){var _2=this.getValueMap();if(_2!=null){if(isc.isAn.Array(_2))_1=_2;else _1=isc.getValues(_2)}else if(this.form.grid){var _3=this.form.grid.data;if(isc.isA.ResultSet!=null&&isc.isA.ResultSet(_3))_1=_3.getValuesList(this.name);else _1=_3.getProperty(this.name)}}
if(_1!=null)_1=_1.getUniqueItems();return _1}
,isc.A.getCompletion=function isc_FormItem_getCompletion(_1){if(_1==null)return;var _2=this.getCandidates();if(_2==null||_2.length==0)return;var _3=_1.toUpperCase(),_4=this.uniqueMatchOnly(),_5;for(var i=0;i<_2.length;i++){var _7=_2[i],_8=_7!=null?_7.toUpperCase():null;if(_8==_3)return null;if(isc.startsWith(_8,_3)){if(!_4)return _7;if(_5!=null)return null;_5=_7}}
return _5}
,isc.A.showCompletion=function isc_FormItem_showCompletion(_1){this.clearCompletion();if(!this.canAutoComplete||!this.hasDataElement()||!this.autoCompleteEnabled())return;var _2=isc.EH.lastEvent.keyName;if(_2=="Backspace"||_2=="Delete")return;var _3=this.getCompletion(_1);if(_3==null){return}
this.form.$20u();this.form.$21l(this);this.setElementValue(_1+_3.substring(_1.length));this.$29o=_1;this.$29h=_3;this.setSelectionRange(_1.length,_3.length)}
,isc.A.$29i=function isc_FormItem__handleChangeWithCompletion(_1){var _2=this.$29h,_3=isc.EH.lastEvent.keyName;if(this.$25g[_3]==true){this.acceptCompletion(_3==this.$18n);return _2}
var _4=_2.substring(this.$29o.length);if(!_1.endsWith(_4)){this.clearCompletion();return _1}
if(this.getSelectedText()==_4){return this.$29o}
this.clearCompletion();return _1}
,isc.A.clearCompletion=function isc_FormItem_clearCompletion(){delete this.$29h;delete this.$29o}
,isc.A.acceptCompletion=function isc_FormItem_acceptCompletion(_1){var _2=this.$29h;if(!_2)return;if(this.autoCompleteEnabled()){var _3=_1?[_2.length,_2.length]:null;if(this.getElementValue()!=_2){if(!_1)_3=this.getSelectionRange();this.setElementValue(_2)}
if(this.hasFocus&&_3)
this.setSelectionRange(_3[0],_3[1])}
this.clearCompletion()}
,isc.A.setSelectionRange=function isc_FormItem_setSelectionRange(_1,_2){if(!isc.isA.TextItem(this)&&!isc.isA.TextAreaItem(this))return;if(!this.isDrawn())return;if(!isc.isA.Number(_1))_1=0;if(!isc.isA.Number(_2))_2=0;if(_1>_2){var _3=_2;_2=_1;_1=_3}
var _4=this.getDataElement();if(_4==null)return;if(this.logIsInfoEnabled("nativeFocus")&&!this.$29p()){this.logInfo("setSelectionRange() about to change focus "+isc.EH.$m8()+(this.logIsDebugEnabled("traceFocus")?this.getStackTrace():""),"nativeFocus")}
if(isc.Browser.isIE){var _5=_4.createTextRange();_5.collapse(true);_5.moveStart(this.$25h,_1);_5.moveEnd(this.$25h,(_2-_1));_5.select()}else{_4.focus();_4.setSelectionRange(_1,_2)}}
,isc.A.selectValue=function isc_FormItem_selectValue(){var _1=this.getElementValue(),_2=isc.isA.String(_1)?_1.length:0;this.setSelectionRange(0,_2)}
,isc.A.deselectValue=function isc_FormItem_deselectValue(_1){if(!this.hasFocus)return;if(_1)this.setSelectionRange(0,0);else{var _2=this.getElementValue(),_3=isc.isA.String(_2)?_2.length:0;this.setSelectionRange(_3,_3)}}
,isc.A.getSelectionRange=function isc_FormItem_getSelectionRange(_1){if(!isc.isA.TextItem(this)&&!isc.isA.TextAreaItem(this))return;if(isc.isA.UploadItem(this))return;if(!this.$29p())return;var _2=this.getDataElement();if(_2==null)return;if(isc.Browser.isIE){var _3=[],_4=this.$29q();var _5=_2.createTextRange();if(_5==null||_4==null)return;if(isc.isA.TextAreaItem(this)){if(!this.supportsSelectionRange)return null;var _6=_5.text.length;if(_6==_4.text.length){return[0,_6]}else{if(_1){if(_5.offsetLeft==_4.offsetLeft&&_5.offsetTop==_4.offsetTop)
{return[0,_4.text.length]}else{_5.collapse(false);if(_5.offsetLeft==_4.offsetLeft&&_5.offsetTop==_4.offsetTop)
{return[_6,_6]}}}else{for(var i=0;i<=_6;i++){if(_5.offsetLeft==_4.offsetLeft&&_5.offsetTop==_4.offsetTop)
{return[i,i+_4.text.length]}
_5.moveStart(this.$25h)}}}
return null}
if(_5.compareEndPoints(this.$25i,_4)==0){_3[1]=_5.text.length}else{_5.setEndPoint(this.$25i,_4);_3[1]=_5.text.length}
_5.setEndPoint(this.$25j,_4);_3[0]=_5.text.length;return _3}else if(isc.Browser.isMoz||isc.Browser.isSafari||isc.Browser.isOpera){return[_2.selectionStart,_2.selectionEnd]}}
,isc.A.$29p=function isc_FormItem__hasNativeFocus(){var _1=this.getFocusElement(),_2=this.getActiveElement();return(_1==_2)}
,isc.A.getSelectedText=function isc_FormItem_getSelectedText(){if(!isc.isA.TextItem(this)&&!isc.isA.TextAreaItem(this)){return}
if(isc.Browser.isIE){var _1=this.$29q();if(_1)return _1.text}else if(isc.Browser.isMoz||isc.Browser.isSafari){var _2=this.getElement();if(_2!=null){return _2.value.substring(_2.selectionStart,_2.selectionEnd)}}}
,isc.A.$29q=function isc_FormItem__getIESelectionRange(){if(!isc.Browser.isIE)return;if(isc.isA.TextAreaItem(this)&&!this.supportsSelectionRange)return null;var _1=this.getDocument().selection,_2=(_1!=null?_1.createRange():null);if(_2!=null&&_2.parentElement().id==this.getDataElementId())return _2;return null}
,isc.A.rememberSelection=function isc_FormItem_rememberSelection(){if(!this.isDrawn())return;if(!isc.isA.TextItem(this)&&!isc.isA.TextAreaItem(this))return;var _1=this.getElementValue();if(_1==isc.emptyString)return;this.$29r=_1;var _2=this.getSelectionRange();if(_2){this.$29s=_2[0];this.$29t=_2[1]}}
,isc.A.resetToLastSelection=function isc_FormItem_resetToLastSelection(_1){if(!this.isDrawn()||this.$29s==null)return;var _2,_3=this.getElementValue(),_4=this.$29r;if(!_1)_2=(_3==_4);else{if(this.$29s==0&&this.$29t==_4.length){_2=true;this.$29t=_3.length}else{_2=(_3.toLowerCase()==_4.toLowerCase())}}
if(_2)this.setSelectionRange(this.$29s,this.$29t);delete this.$29s;delete this.$29t;delete this.$29r}
,isc.A.handleChange=function isc_FormItem_handleChange(_1,_2){if(this.$29j&&this.compareValues(_1,this.$29k))return true;this.$29j=true;this.$29k=_1;var _3=_1;if(isc.isA.Date(_3))_3=_3.duplicate();else if(isc.isAn.Array(_3))_3=_3.duplicate();else if(isc.isAn.Object(_3))_3=isc.addProperties({},_3);if(this.transformInput){_1=this.transformInput(this.form,this,_1,_2)}
var _4=this.hasErrors(),_5=false,_6=[],_7=null,_8;if(this.length!=null&&isc.isA.String(_1)&&_1.length>this.length){_1=_1.substring(0,this.length)}
var _9=isc.rpc.startQueue();var _10=isc.addProperties({},this.form.values),_11={unknownErrorMessage:this.form.unknownErrorMessage,changing:true},_12=this.form.validateFieldAndDependencies(this,this.validators,_1,_10,_11);if(!_9)isc.rpc.sendQueue();if(_12!=null){_5=!_12.valid;if(_12.resultingValue!=null){_8=_12.resultingValue}
if(!_12.valid){_6=_12.errors[this.name];if(_6==null)_6=[]}
_7=_12.errors}
var _13;if(_5&&_8===_13&&this.$29u())
{_8=_2;if(_2==null)_8=null}
if(_8!==_13)_1=_8;if(_5){this.clearErrors();this.setError(_6)}else if(_12!=null&&_4){this.clearErrors()}
this.$28y=false;if((!_5||this.changeOnError)){if(this.change!=null){if(this.change(this.form,this,_1,_2)==false){_1=_2;_5=true}}
if(this.destroyed)return;if(!_5&&this.form&&this.form.itemChange!=null){if(this.form.itemChange(this,_1,_2)==false){_1=_2;_5=true}}}
var _14=this.$28y;var _15=!this.compareValues(_1,_3);if((_5||_15)&&!_14)
{this.setValue(_1);if(_5&&this.maintainSelectionOnTransform){this.$29v()}else if(this.hasFocus){this.delayCall("setSelectionRange",[_1.length,_1.length])}}
if(this.$28y)this.$29k=this._value;if((this.redrawOnChange||_6.length>0||(_6.length==0&&_12!=null&&_4)))
{this.redraw()}
for(var _16 in _7){if(_16!=this.name){this.form.setFieldErrors(_16,_7[_16],true)}}
if(!_5&&this.hasFocus)this.showCompletion(_1);delete this.$29j;return(!_5)}
,isc.A.$29u=function isc_FormItem__rejectInvalidValueOnChange(){return(this.rejectInvalidValueOnChange!=null)?this.rejectInvalidValueOnChange:this.form.rejectInvalidValueOnChange}
,isc.A.compareValues=function isc_FormItem_compareValues(_1,_2){return isc.DynamicForm.compareValues(_1,_2)}
,isc.A.elementChanged=function isc_FormItem_elementChanged(){var _1=(isc.EH.$g4!=null);if(!_1)isc.EH.$jp("ICHG");this.logDebug("native change");if(isc.Log.supportsOnError){this.updateValue()}else{try{this.updateValue()}catch(e){isc.Log.$a3(e)}}
if(!_1)isc.EH.$jq();return true}
,isc.A.$213=function isc_FormItem__handleInactiveEditorEvent(_1,_2,_3){if(this.logIsDebugEnabled("inactiveEditorHTML")){this.logDebug("handling inactive editor event:"+_1+", inactive context:"+this.echo(_2),"inactiveEditorHTML")}
var _4=this.$25k[_1];if(_4==null){_4=this.$25k[_1]="inactiveEditor"+_1.substring(0,1).toUpperCase()+_1.substring(1)}
if(this[_4]!=null){return this[_4](_2,_3)}}
,isc.A.$29w=function isc_FormItem__fireStandardHandler(_1){this.convertToMethod(_1);return this[_1](this.form,this,isc.EH.lastEvent)}
,isc.A.handleTitleClick=function isc_FormItem_handleTitleClick(){if(this.isDisabled())return;if(this.editingOn){this.editClick();this.handleClick();return false}
return this.$29w("titleClick")}
,isc.A.handleTitleDoubleClick=function isc_FormItem_handleTitleDoubleClick(){if(this.isDisabled())return;return this.$29w("titleDoubleClick")}
,isc.A.handleClick=function isc_FormItem_handleClick(){if(this.editingOn){isc.EditContext.selectCanvasOrFormItem(this,true);return false}
if(this.isDisabled())return;return this.$29w("click")}
,isc.A.handleDoubleClick=function isc_FormItem_handleDoubleClick(){if(this.isDisabled())return;return this.$29w("doubleClick")}
,isc.A.handleCellClick=function isc_FormItem_handleCellClick(){if(this.isDisabled())return;return this.$29w("cellClick")}
,isc.A.handleCellDoubleClick=function isc_FormItem_handleCellDoubleClick(){if(this.isDisabled())return;return this.$29w("cellDoubleClick")}
,isc.A.$23n=function isc_FormItem__handleElementChanged(){return this.form.elementChanged(this.getID())}
,isc.A.handleMouseMove=function isc_FormItem_handleMouseMove(){if(!this.isDisabled()&&(this.showValueIconOver||this.showValueIconDown)){var _1=isc.EH.lastEvent.itemInfo,_2=(_1.overElement||_1.overTextBox||_1.overControlTable),_3=this.$26y;if(_2){if(this.$lr&&this.showValueIconDown){if(_3!=this.$24q){this.$26y=this.$24q;this.$29b()}}else if(this.showValueIconOver&&_3!=this.$24p){this.$26y=this.$24p;this.$29b()}}else{var _4=(this.showValueIconFocused&&this.showValueIconOver&&this.hasFocus)?this.$24p:null;if(_3!=_4){this.$26y=_4;this.$29b()}}}
if(this.$29w("mouseMove")==false)return false}
,isc.A.handleMouseOver=function isc_FormItem_handleMouseOver(){isc.Hover.setAction(this,this.$1d,null,this.$29x());return this.$29w("mouseOver")}
,isc.A.handleMouseOut=function isc_FormItem_handleMouseOut(){var _1=(this.showValueIconFocused&&this.showValueIconOver&&this.hasFocus)?this.$24p:null;if(this.$26y!=_1){this.$26y=_1;this.$29b()}
this.stopHover();return this.$29w("mouseOut")}
,isc.A.handleMouseDown=function isc_FormItem_handleMouseDown(){var _1=isc.EH.lastEvent.itemInfo,_2=_1.inactiveContext;if(_2!=null){return this.form.bubbleInactiveEditorEvent(this,"mouseDown",_1)}
if(!this.isDisabled()&&this.showValueIconDown){var _3=(_1.overElement||_1.overTextBox||_1.overControlTable);if(_3){this.$26y=this.$24q;this.$lr=true;isc.Page.setEvent(isc.EH.MOUSE_UP,this,isc.Page.FIRE_ONCE,"$29y");this.$29b()}}
if(this.mouseDown)return this.$29w("mouseDown")}
,isc.A.stopHover=function isc_FormItem_stopHover(){isc.Hover.clear()}
,isc.A.$29y=function isc_FormItem__clearMouseDown(){this.$lr=null;if(this.$26y==this.$24q){this.$26y=this.showValueIconOver?this.$24p:null;this.$29b()}}
,isc.A.handleMouseStillDown=function isc_FormItem_handleMouseStillDown(_1){if(this.mouseStillDown){return this.$29w("mouseStillDown")}}
,isc.A.$29x=function isc_FormItem__getHoverDelay(){return this.hoverDelay!=null?this.hoverDelay:this.form.itemHoverDelay}
,isc.A.handleTitleMove=function isc_FormItem_handleTitleMove(){return this.$29w("titleMove")}
,isc.A.handleTitleOver=function isc_FormItem_handleTitleOver(){isc.Hover.setAction(this,this.$29z,null,this.$29x());return this.$29w("titleOver")}
,isc.A.handleTitleOut=function isc_FormItem_handleTitleOut(){this.stopHover();return this.$29w("titleOut")}
,isc.A.$23p=function isc_FormItem__iconFocus(_1,_2){var _3=this.getIcon(_1);if(_3!=null){var _4=(_3.prompt!=null?_3.prompt:this.iconPrompt)
window.status=_4;if(this.$27w(_3)){this.$28h(_3,false,true)}
else if(this.$28i(_3))this.$28h(_3,true)}
return this.$23h(_2,this)}
,isc.A.$23r=function isc_FormItem__iconBlur(_1,_2){var _3=this.getIcon(_1);if(_3!=null){window.status="";var _4=this.$27w(_3),_5=_3.showFocusedWithItem!=false,_6=this.showIconsOnFocus;if(_4&&(!_6||!_5)){this.$28h(_3,false,false)}
if(this.$28i(_3))this.$28h(_3,false,false)}
return this.$23k(_2,this)}
,isc.A.$211=function isc_FormItem__iconMouseOver(_1){if(_1==this.getErrorIconId())return this.$290();var _2=this.getIcon(_1);if(_2!=null){if(this.$28i(_2))this.$28h(_2,true);this.$291=_2;isc.Hover.setAction(this,this.$292,null,this.$29x());var _3=(_2.prompt!=null?_2.prompt:this.iconPrompt)
window.status=_3;return true}}
,isc.A.$210=function isc_FormItem__iconMouseOut(_1){if(_1==this.getErrorIconId())return this.$293();var _2=this.getIcon(_1);if(_2!=null){window.status="";if(this.$28i(_2))this.$28h(_2,false);delete this.$291;isc.Hover.setAction(this,this.$1d,null,this.$29x());return true}}
,isc.A.$214=function isc_FormItem__iconClick(_1){var _2=this.getIcon(_1);if(_2==null)return;if(this.iconIsDisabled(_2))return;if(_2.click!=null){if(!isc.isA.Function(_2.click)){isc.Func.replaceWithMethod(_2,"click","form,item,icon")}
if(_2.click(this.form,this,_2)==false)return false}
if(this.iconClick)this.iconClick(this.form,this,_2)}
,isc.A.$294=function isc_FormItem__iconKeyPress(_1){var _2=this.getIcon(_1);if(_2){var _3=isc.EH.getKey(),_4=isc.EH.getKeyEventCharacter();if(_2.keyPress){if(!isc.isA.Function(_2.keyPress)){isc.Func.replaceWithMethod(_2,"keyPress","keyName, character,form,item,icon")}
if(_2.keyPress(_3,_4,this.form,this,_2)==false)
return false}
if(this.iconKeyPress)this.iconKeyPress(_3,_4,this.form,this,_2);if(_3==this.$18n){if(this.$214(_2)==false)return false}}}
,isc.A.$290=function isc_FormItem__handleErrorIconMouseOver(){isc.Hover.setAction(this,this.$295,null,this.$29x())}
,isc.A.$293=function isc_FormItem__handleErrorIconMouseOut(){isc.Hover.setAction(this,this.$1d,null,this.$29x())}
,isc.A.$295=function isc_FormItem__handleErrorIconHover(){if(this.itemHover&&this.itemHover(this,this.form)==false)return false;var _1=this.shouldShowErrorIconPrompt()?isc.FormItem.getErrorPromptString(this.$27c):isc.emptyString;if(_1&&!isc.is.emptyString(_1))
isc.Hover.show(_1,this.form.$1e(this));else isc.Hover.setAction(this,this.$1d,null,this.$29x())}
,isc.A.$1d=function isc_FormItem__handleHover(_1){if(this.itemHover&&this.itemHover(this,this.form)==false)return false;var _2;if(this.itemHoverHTML)_2=this.itemHoverHTML(this,this.form);else _2=this.form.itemHoverHTML(this,this.form);this.form.$215(this,_2)}
,isc.A.$29z=function isc_FormItem__handleTitleHover(_1){if(this.titleHover&&this.titleHover(this,this.form)==false)return false;var _2;if(this.titleHoverHTML)_2=this.titleHoverHTML(this,this.form);else _2=this.form.titleHoverHTML(this,this.form);this.form.$215(this,_2)}
,isc.A.$292=function isc_FormItem__handleIconHover(){if(this.itemHover&&this.itemHover(this,this.form)==false)return false;var _1=this.$291,_2=this.getIconPrompt(_1);if(_2&&!isc.is.emptyString(_2))
isc.Hover.show(_2,this.form.$1e(this));else isc.Hover.setAction(this,this.$1d,null,this.$29x())}
,isc.A.getGlobalTabIndex=function isc_FormItem_getGlobalTabIndex(){if(this.globalTabIndex==null){if(this.tabIndex==-1)this.globalTabIndex=-1;else{var _1=this.form.getTabIndex(),_2=this.getTabIndex();if(_1==-1)return-1;return(_1+_2)}}
return this.globalTabIndex}
,isc.A.getTabIndex=function isc_FormItem_getTabIndex(){if(this.tabIndex!=null)return this.tabIndex;if(this.globalTabIndex||!this.$mb())return null;if(this.$198==null){this.form.$195()}
return this.$198}
,isc.A.setGlobalTabIndex=function isc_FormItem_setGlobalTabIndex(_1){this.globalTabIndex=_1;this.$296(_1)}
,isc.A.setTabIndex=function isc_FormItem_setTabIndex(_1){this.globalTabIndex=null;this.tabIndex=_1;this.$296(_1)}
);isc.evalBoundary;isc.B.push(isc.A.$269=function isc_FormItem__getElementTabIndex(_1){if(this.isInactiveHTML()||(!_1&&this.isDisabled()))
{return-1}
if(this.$297!=null)return this.$297;return this.getGlobalTabIndex()}
,isc.A.$296=function isc_FormItem__setElementTabIndex(_1){this.$297=_1;if(!this.$mb()||!this.isVisible()||!this.isDrawn())return;if(this.getFocusElement()!=null){isc.FormItem.setElementTabIndex(this.getFocusElement(),_1);this.$298()}else{this.redraw("set tab index")}}
,isc.A.updateTabIndex=function isc_FormItem_updateTabIndex(){if(!this.$mb()||!this.isVisible()||!this.isDrawn()||this.isDisabled())return;var _1=this.getGlobalTabIndex();if(this.$297!=_1)this.$296(_1)}
,isc.A.$273=function isc_FormItem__getIconTabIndex(_1){if(_1.tabIndex==-1||this.iconIsDisabled(_1))return-1;return this.$269(true)}
,isc.A.$298=function isc_FormItem__updateIconTabIndices(){var _1=[];_1.addList(this.icons);if(this.showPickerIcon)_1.add(this.getPickerIcon());for(var i=0;i<_1.length;i++){var _3=_1[i];if(!_3||_3.imgOnly)continue;var _4=this.$27m(_3);if(_4!=null){isc.FormItem.setElementTabIndex(_4,this.$273(_3))}}}
,isc.A.setDisabled=function isc_FormItem_setDisabled(_1){var _2=this.isDisabled();this.disabled=_1;var _3=this.isDisabled();if(_2!=_3)this.updateDisabled()}
,isc.A.setShowDisabled=function isc_FormItem_setShowDisabled(_1){this.showDisabled=_1;this.updateDisabled()}
,isc.A.updateDisabled=function isc_FormItem_updateDisabled(){var _1=this.isDisabled();this.$299(!_1);this.$29l();this.$29b();if(this.showDisabled)this.updateState()}
,isc.A.setEnabled=function isc_FormItem_setEnabled(_1){return this.setDisabled(!_1)}
,isc.A.isDisabled=function isc_FormItem_isDisabled(){var _1=this.disabled;if(!_1){if(this.parentItem!=null)_1=this.parentItem.isDisabled();else{_1=this.form.isDisabled();if(!_1&&this.containerWidget!=this.form)_1=this.containerWidget.isDisabled()}}
return _1}
,isc.A.enable=function isc_FormItem_enable(){this.setDisabled(false)}
,isc.A.disable=function isc_FormItem_disable(){this.setDisabled(true)}
,isc.A.$299=function isc_FormItem__setElementEnabled(_1){if(this.hasDataElement()){var _2=this.getDataElement();if(_2){_2.disabled=!_1;_2.tabIndex=this.$269();if(this.useDisabledEventMask()){var _3=this.$26d();if(_3&&(!_3.getAttribute||_3.getAttribute("isDisabledEventMask")!="true"))
{_3=null}
if(_1&&_3){isc.Element.clear(_3)}else if(!_1&&!_3){isc.Element.insertAdjacentHTML(_2,"beforeBegin",this.$26b())}}}}else if(this.$mb()){var _2=this.getFocusElement();if(_2)_2.tabIndex=this.$269()}}
,isc.A.$29l=function isc_FormItem__setIconsEnabled(){if(this.showPickerIcon){var _1=this.getPickerIcon();this.setIconEnabled(_1)}
if(!this.icons||this.icons.length<1)return;for(var i=0;i<this.icons.length;i++){this.setIconEnabled(this.icons[i])}}
,isc.A.iconIsDisabled=function isc_FormItem_iconIsDisabled(_1,_2){_1=this.getIcon(_1);if(!_1)return;if(this.containerWidget&&this.containerWidget.isDisabled())return true;if(_1.neverDisable)return false;return this.isDisabled()||(!_2&&this.isReadOnly())}
,isc.A.$mb=function isc_FormItem__canFocus(){if(this.canFocus!=null)return this.canFocus;return this.hasDataElement()}
,isc.A.$268=function isc_FormItem__canFocusInTextBox(){return this.$mb()}
,isc.A.getCanFocus=function isc_FormItem_getCanFocus(){return this.$mb()}
,isc.A.focusInItem=function isc_FormItem_focusInItem(){var _1=this.isVisible()&&this.$mb()&&!this.isDisabled(),_2=_1?this.getFocusElement():null;if(!_1||!_2){return}
if(_2.focus){var _3=this.getActiveElement();if(!isc.Browser.isIE||_3!=_2){this.logInfo("about to call element.focus() "+isc.EH.$m8()+(this.logIsDebugEnabled("traceFocus")?this.getStackTrace():""),"nativeFocus");isc.FormItem.$228(this);isc.EventHandler.$nc=this;_2.focus()}else{this.logInfo("element already focused, not focus()ing","nativeFocus")}
if(isc.Browser.isIE){isc.EH.$0o=this;this.$28k=_2}}else{this.logInfo("can't call element focus, no element","nativeFocus")}
var _4=this.selectOnFocus;if(_4==null&&this.form)_4=this.form.selectOnFocus;if(_4&&_2.select)_2.select()}
,isc.A.blurItem=function isc_FormItem_blurItem(){if(!this.isVisible()||!(this.hasFocus||isc.EH.$0o==this))return;var _1=this.$27l();if(_1&&_1.blur){this.logInfo("about to call element blur"+isc.EH.$m8()+(this.logIsDebugEnabled("traceBlur")?this.getStackTrace():""),"nativeFocus");isc.EH.$m9=this;if(isc.Browser.isIE){try{_1.blur()}catch(e){}}else{_1.blur()}}else{this.logInfo("can't call element blur, no element","nativeFocus")}}
,isc.A.focusInIcon=function isc_FormItem_focusInIcon(_1){_1=this.getIcon(_1);if(_1==null||_1.imgOnly)return;var _2=this.$27m(_1);if(_2!=null)_2.focus()}
,isc.A.blurIcon=function isc_FormItem_blurIcon(_1){if(isc.isA.String(_1))_1=this.getIcon(_1);if(_1==null||!this.icons||!this.icons.contains(_1)||_1.imgOnly)return;var _2=this.$27m(_1);if(_2!=null)_2.blur()}
,isc.A.$23h=function isc_FormItem__nativeElementFocus(_1,_2){if(isc.EH.$nc==this)delete isc.EH.$nc
isc.EH.$m7(this,true);if(isc.Browser.isMoz&&!this.isVisible()){this.logWarn("calling element.blur() to correct focus in hidden item: "+this,"nativeFocus");_1.blur();return}
this.hasFocus=true;this.$28k=_1;var _3=this.form.elementFocus(_1,_2);return _3}
,isc.A.$23k=function isc_FormItem__nativeElementBlur(_1,_2){if(isc.EH.$m9==this)delete isc.EH.$m9
if(this.$280!=null){isc.Timer.clearTimeout(this.$280);this.$25n()}
isc.EH.$m7(this);this.hasFocus=false;delete this.$28k;var _3=this.form.elementBlur(_1,_2);return _3}
,isc.A.elementFocus=function isc_FormItem_elementFocus(_1){if(this.prompt)this.form.showPrompt(this.prompt);if(this.showIconsOnFocus&&this.showIcons){this.showAllIcons(true)}else{if(this.icons)this.updateIconsForFocus(this.icons,true)}
if(this.showFocusedPickerIcon&&this.showPickerIcon){var _2=this.getPickerIcon();if(_2)this.updateIconsForFocus(_2,true)}
if(this.showFocused)this.updateState();if(this.showValueIconFocused&&this.showValueIconOver&&this.$26y==null){this.$26y=this.$24p;this.$29b()}
if(_1)return;if((this.grid&&this.grid.blockOnFieldBusy(this))||(!this.grid&&this.form.blockOnFieldBusy(this)))
{return false}
this.handleEditorEnter()
if(this.focus){this.convertToMethod("focus");return this.focus(this.form,this)}
return true}
,isc.A.updateIconsForFocus=function isc_FormItem_updateIconsForFocus(_1,_2){if(_1==null)return;_2=!!_2;if(!isc.isAn.Array(_1))_1=[_1];for(var i=0;i<_1.length;i++){if(this.$27w(_1[i],true)){var _4=this.$6h(_1[i]);if(_4!=null){isc.Canvas.$1j(_4,this.getIconURL(_1[i],false,null,_2))}}}}
,isc.A.elementBlur=function isc_FormItem_elementBlur(){if(this.prompt)this.form.clearPrompt();if(this.showIconsOnFocus&&this.showIcons){if(this.$28e==null){this.$28e=this.delayCall("hideAllIcons",[],0)}}else if(this.hideIconsOnKeypress&&this.showIcons){this.showAllIcons()}else{if(this.icons)this.updateIconsForFocus(this.icons,false)}
if(this.showFocusedPickerIcon&&this.showPickerIcon){var _1=this.getPickerIcon();if(_1)this.updateIconsForFocus(_1,false)}
if(this.showFocused){this.updateState()}
if(this.$26y==this.$24p){this.$26y=null
this.$29b()}
this.acceptCompletion();this.handleEditorExit();if(this.blur){this.convertToMethod("blur");return this.blur(this.form,this)}
return true}
,isc.A.$21p=function isc_FormItem__moveFocusWithinItem(_1){var _2=this.items,_3=this.icons;if(this.$265!=null){_3=[this.$265];_3.addList(this.icons)}
if((_2==null||_2.length==0)&&(_3==null||_3.length==0)){return false}
var _4=this.getFocusIconIndex(true),_5;if(_4==null){var _6=isc.EventHandler.lastEvent.keyTarget;if(_6==this)_5=0;else if(_2){_5=_2.indexOf(_6)}}
if((_5==null||_5==-1)&&_4==null){return false}
if(_1){if(_5!=null&&_2!=null){while(_5<_2.length-1){_5+=1
var _7=_2[_5];if(_7.$mb()){_7.focusInItem();return true}}}
if(_4==null)_4=-1;if(this.canTabToIcons!=false&&this.showIcons&&_3!=null){while(_4<_3.length-1){_4+=1;var _8=_3[_4];if(this.$27s(_8)&&!_8.imgOnly&&_8.tabIndex!=-1){this.focusInIcon(_8);return true}}}
return false}else{if(this.canTabToIcons!=false&&_4!=null){while(_4>0){_4-=1;var _8=_3[_4]
if(this.$27s(_8)&&!this.imgOnly&&_8.tabIndex!=-1){this.focusInIcon(_8);return true}}}
if(_5==null)
_5=_2!=null?_2.length:1;while(_5>0){var _7;_5-=1
if(_2==null){_7=this}else{_7=_2[_5]}
if(_7.$mb()){_7.focusInItem();return true}}
return false}}
,isc.A.getFocusIconIndex=function isc_FormItem_getFocusIconIndex(_1){var _2=this.$27l();var _3;if(_1&&this.$265!=null){_3=[this.$265];_3.addList(this.icons)}else{_3=this.icons}
if(_2==null||_3==null||_3.length==0)return null;for(var i=0;i<_3.length;i++){if(this.$27m(_3[i])==_2)return i}
return null}
,isc.A.$mh=function isc_FormItem__allowNativeTextSelection(_1,_2){if(_2.overTitle)return;if(_2.overIcon)return false;if(_1==null)_1=isc.EH.lastEvent;if(_1.nativeTarget&&(_1.nativeTarget.tagName.toLowerCase()==this.$25l))
return false;return this.canSelectText!=false}
,isc.A.handleEditorExit=function isc_FormItem_handleEditorExit(){if(!this.$30a)return;this.$30a=null;var _1=this.getValue();if(!this.$30b)this.$30c(_1);if(this.getImplicitSave()&&this.form&&this.form.awaitingImplicitSave&&!this.form.implicitSaveInProgress&&this.$20g()&&this.getImplicitSaveOnBlur()!=false)
{this.form.performImplicitSave(this,false)}
if(this.editorExit)this.editorExit(this.form,this,_1)}
,isc.A.getImplicitSave=function isc_FormItem_getImplicitSave(){return(this.implicitSave!=null?this.implicitSave:this.form.implicitSave)}
,isc.A.getImplicitSaveOnBlur=function isc_FormItem_getImplicitSaveOnBlur(){if(this.getImplicitSave()==false)return false;return(this.implicitSaveOnBlur!=null?this.implicitSaveOnBlur:this.form.implicitSaveOnBlur)}
,isc.A.$30c=function isc_FormItem__performValidateOnEditorExit(_1){if(this.validateOnExit||this.form.validateOnExit){if(this.$30d||(this.form.isNewRecord&&this.form.isNewRecord())||!this.compareValues(_1,this.$30e))
{this.validate()}
this.$30d=null;this.$30e=null}
var _2=this.form.rulesEngine;if(_2!=null){_2.processEditorExit(this.form,this)}}
,isc.A.handleEditorEnter=function isc_FormItem_handleEditorEnter(){if(this.$30a)return;this.$30a=true;var _1=this.getValue();if(this.validateOnExit||this.form.validateOnExit)this.$30e=_1;if(this.editorEnter)this.editorEnter(this.form,this,_1);if(this.form.rulesEngine!=null){this.form.rulesEngine.processEditorEnter(this.form,this)}}
,isc.A.$30f=function isc_FormItem__setupFocusCheck(){var _1=this;this.$30g=isc.Page.setEvent(isc.EH.MOUSE_UP,function(){if(!_1.destroyed)_1.$30h()})}
,isc.A.$30h=function isc_FormItem__checkNativeFocus(){isc.Page.clearEvent(isc.EH.MOUSE_UP,this.$30g);delete this.$30g;if(this.getActiveElement()==document.body){this.focusInItem()}}
,isc.A.$21v=function isc_FormItem__willHandleInput(){return false}
,isc.A.$30i=function isc_FormItem__handleInput(){isc.EH.$jp("INP");if(!this.mask){if(this.changeOnKeypress){if(isc.Log.supportsOnError){this.updateValue()}else{try{this.updateValue()}catch(e){isc.Log.$a3(e)}}}else if(this.hasDataElement()&&this.length!=null){var _1=this.getElementValue();if(isc.isA.String(_1)&&_1.length>this.length){this.setElementValue(_1.substring(0,this.length))}}}
isc.EH.$jq()}
,isc.A.$23l=function isc_FormItem__nativeCutPaste(_1,_2){if(this.changeOnKeypress)this.$30j()}
,isc.A.handleKeyPress=function isc_FormItem_handleKeyPress(_1,_2){if(!this.form)return;var _3=this.form.$pf(_1);if(_3.overIcon){if(this.$294(_3.overIcon)==false)return false}else{if(!this.$21v()&&this.changeOnKeypress)this.$30j();var _4=_1.keyName;if(this.hideIconsOnKeypress&&!this.$28b&&_4!=this.$kw){this.hideAllIcons()}}
return this.$30k(this,this.form,_4,_1.characterValue)}
,isc.A.$30k=function isc_FormItem__fireKeyPressHandlers(_1,_2,_3,_4){if(this.keyPress!=null&&this.keyPress(_1,_2,_3,_4)==false){return false}
if(!this.form)return false;if(this.$30l!=null&&this.$30l(_1,_2,_3,_4)==false)
{return false}
if(this.parentItem==null&&this.form.itemKeyPress!=null){return this.form.itemKeyPress(_1,_3,_4)}}
,isc.A.$30j=function isc_FormItem__queueForUpdate(){if(this.$280!=null){isc.Timer.clearTimeout(this.$280);this.$25n()}
if(this.maintainSelectionOnTransform&&(this.$28z()!=this.$25f))
{this.$30m()}
this.$280=isc.Timer.setTimeout({target:this,methodName:this.$25m},0)}
,isc.A.$25n=function isc_FormItem__delayedUpdate(){delete this.$280;this.updateValue();this.$30n()}
,isc.A.$30m=function isc_FormItem__rememberPreChangeSelection(){if(this.$30o!=null)return;var _1=this.getSelectionRange(true);if(_1){this.$30o=_1[0];this.$30p=_1[1]}}
,isc.A.$29v=function isc_FormItem__revertToPreChangeSelection(){if(this.$30o==null)return;this.setSelectionRange(this.$30o,this.$30p)}
,isc.A.$30n=function isc_FormItem__clearPreChangeSelection(){delete this.$30o;delete this.$30p}
,isc.A.handleKeyDown=function isc_FormItem_handleKeyDown(_1,_2){if(this.dirtyOnKeyDown)this.$30q();var _3=this,_4=this.form,_5=_1.keyName;if(this.keyDown!=null&&this.keyDown(_3,_4,_5)==false)return false}
,isc.A.$20g=function isc_FormItem__itemValueIsDirty(){return this.$30r==true}
,isc.A.$30q=function isc_FormItem__markValueAsDirty(){this.$30r=true}
,isc.A.$20k=function isc_FormItem__markValueAsNotDirty(){this.$30r=false}
,isc.A.handleKeyUp=function isc_FormItem_handleKeyUp(_1,_2){if(!this.form)return;var _3=this,_4=this.form,_5=_1.keyName;if(this.keyUp!=null&&this.keyUp(_3,_4,_5)==false)return false}
,isc.A.getSerializeableFields=function isc_FormItem_getSerializeableFields(_1,_2){_1.addList(["form"]);return this.Super("getSerializeableFields",arguments)}
,isc.A.getLeft=function isc_FormItem_getLeft(){var _1=this.isDrawn()?this.getOuterElement():null;if(_1==null){var _2="getLeft() Unable to determine position for "+(this.name==null?"this item ":this.name)+". ";if(this.isDrawn()){_2+="This method is not supported by items of type "+this.getClass()}else{_2+="Position cannot be determined before the element is drawn"}
_2+=" - returning zero.";this.form.logWarn(_2);return 0}
return this.$30s(_1)}
,isc.A.getTitleLeft=function isc_FormItem_getTitleLeft(){var _1=this.isDrawn()&&this.form?isc.Element.get(this.form.$21c(this)):null;if(_1==null){var _2="getTitleLeft() Unable to determine position for "+(this.name==null?"this item ":this.name)+". ";if(this.isDrawn()){_2+="This method is not supported by items of type "+this.getClass()}else{_2+="Position cannot be determined before the element is drawn"}
_2+=" - returning zero.";this.form.logWarn(_2);return 0}
return this.$30s(_1)}
,isc.A.$30s=function isc_FormItem__getElementLeft(_1){var _2=_1.offsetParent,_3=this.containerWidget.getHandle(),_4=_3.offsetParent,_5=isc.Element.getOffsetLeft(_1);while(_2&&_2!=_3&&_2!=_4){_5+=isc.Element.getOffsetLeft(_2)
_5-=(_2.scrollLeft||0);var _6=(isc.Browser.isIE?parseInt(_2.currentStyle.borderLeftWidth):parseInt(isc.Element.getComputedStyleAttribute(_2,"borderLeftWidth")));if(isc.isA.Number(_6))_5+=_6;var _7=(isc.Browser.isIE?parseInt(_2.currentStyle.marginLeft):parseInt(isc.Element.getComputedStyleAttribute(_2,"marginLeft")));if(isc.isA.Number(_7))_5+=_7;_2=_2.offsetParent}
if(_2==_4){_5-=isc.Element.getOffsetLeft(_3)}
return _5}
,isc.A.$255=function isc_FormItem__isValidIcon(_1){return(_1!=null&&(this.icons&&this.icons.contains(_1)||this.showPickerIcon&&this.getPickerIcon()==_1))}
,isc.A.getIconLeft=function isc_FormItem_getIconLeft(_1){if(_1==null&&this.icons!=null&&this.icons.getLength()>0)_1=this.icons[0];else if(!this.$255(_1)){this.logWarn("getIconLeft() passed invalid icon:"+isc.Log.echoAll(_1));return null}
var _2=this.$6h(_1);if(_2==null){this.logWarn("getIconLeft() unable to return position of icon - "+"this icon is not currently drawn into the page. Returning null");return null}
return isc.Element.getLeftOffset(_2,this.containerWidget.getClipHandle())}
,isc.A.getTop=function isc_FormItem_getTop(){var _1=this.isDrawn()?this.getOuterElement():null;if(_1==null){var _2="getTop() Unable to determine position for "+(this.name==null?"this item ":this.name)+". ";if(this.isDrawn()){_2+="This method is not supported by items of type "+this.getClass()}else{_2+="Position cannot be determined before the element is drawn"}
_2+=" - returning zero.";this.form.logWarn(_2);return 0}
var _3=this.$30t(_1);return _3}
,isc.A.getTitleTop=function isc_FormItem_getTitleTop(){var _1=this.isDrawn()&&this.form?isc.Element.get(this.form.$21c(this)):null;if(_1==null){var _2="getTitleTop() Unable to determine position for "+(this.name==null?"this item ":this.name)+". ";if(this.isDrawn()){_2+="This method is not supported by items of type "+this.getClass()}else{_2+="Position cannot be determined before the element is drawn"}
_2+=" - returning zero.";this.form.logWarn(_2);return 0}
return this.$30t(_1)}
,isc.A.$30t=function isc_FormItem__getElementTop(_1){var _2=this.containerWidget.getHandle(),_3=_2.offsetParent,_4=_1.offsetParent,_5=isc.Element.getOffsetTop(_1);while(_4&&_4!=_2&&_4!=_3){_5+=isc.Element.getOffsetTop(_4)
_5-=(_4.scrollTop||0);var _6=(isc.Browser.isMoz?0:(isc.Browser.isIE?parseInt(_4.currentStyle.borderTopWidth):parseInt(isc.Element.getComputedStyleAttribute(_4,"borderTopWidth"))));if(isc.isA.Number(_6))_5+=_6;var _7=(isc.Browser.isIE?parseInt(_4.currentStyle.marginTop):parseInt(isc.Element.getComputedStyleAttribute(_4,"marginTop")));if(isc.isA.Number(_7))_5+=_7;_4=_4.offsetParent}
if(_4==_3){_5-=isc.Element.getOffsetTop(_2)}
return _5}
,isc.A.getIconTop=function isc_FormItem_getIconTop(_1){if(_1==null&&this.icons!=null&&this.icons.getLength()>0)_1=this.icons[0];else if(!this.$255(_1)){this.logWarn("getIconTop() passed invalid icon:"+isc.Log.echoAll(_1));return null}
var _2=this.$6h(_1);if(_2==null){this.logWarn("getIconTop() unable to return position of icon - "+"this icon is not currently drawn into the page. Returning null");return null}
return isc.Element.getTopOffset(_2,this.containerWidget.getClipHandle())}
,isc.A.getPageLeft=function isc_FormItem_getPageLeft(){return this.getLeft()+((this.containerWidget.getPageLeft()+this.containerWidget.getLeftMargin()+this.containerWidget.getLeftBorderSize())
-this.containerWidget.getScrollLeft())}
,isc.A.getPageTop=function isc_FormItem_getPageTop(){return this.getTop()+((this.containerWidget.getPageTop()+this.containerWidget.getTopMargin()+this.containerWidget.getTopBorderSize())
-this.containerWidget.getScrollTop())}
,isc.A.getTitlePageLeft=function isc_FormItem_getTitlePageLeft(){return this.getTitleLeft()+((this.containerWidget.getPageLeft()+this.containerWidget.getLeftMargin()+this.containerWidget.getLeftBorderSize())
-this.containerWidget.getScrollLeft())}
,isc.A.getTitlePageTop=function isc_FormItem_getTitlePageTop(){return this.getTitleTop()+((this.containerWidget.getPageTop()+this.containerWidget.getTopMargin()+this.containerWidget.getTopBorderSize())
-this.containerWidget.getScrollTop())}
,isc.A.getIconRect=function isc_FormItem_getIconRect(_1){return[this.getIconLeft(_1),this.getIconTop(_1),this.getIconWidth(_1),this.getIconHeight(_1)]}
,isc.A.getIconPageRect=function isc_FormItem_getIconPageRect(_1){var _2=this.getIconRect(_1);_2[0]+=this.containerWidget.getPageLeft();_2[1]+=this.containerWidget.getPageTop();return _2}
,isc.A.propertyChanged=function isc_FormItem_propertyChanged(_1,_2){if(this.$25o[_1]==true)this.$30u=true;if(this.$25p[_1]==true)this.updateState();if(_1==this.$25q&&this.items){for(var i=0;i<this.items.length;i++){this.items[i].updateState()}}}
,isc.A.doneSettingProperties=function isc_FormItem_doneSettingProperties(){if(this.$30u){var _1=this.form,_2=_1.items;_2.$17s=null;_1.markForRedraw()}
delete this.$30u}
,isc.A.setEditMode=function isc_FormItem_setEditMode(_1,_2,_3){if(_1==null)_1=true;if(this.editingOn==_1)return;this.editingOn=_1;if(this.editingOn){this.editContext=_2}
this.editNode=_3}
,isc.A.editClick=function isc_FormItem_editClick(){var _1=this.getTitlePageLeft(),_2=this.getVisibleTitleWidth(),_3,_4,_5,_6;_4=this.getTitlePageTop();_6=this.getTitleVisibleHeight();_5=this.getVisibleHeight();_3=(_6==_5)?_4:_4+((_6-_5)/2);isc.EditContext.manageTitleEditor(this,_1,_2,_3,null)}
,isc.A.parseValueExpressions=function isc_FormItem_parseValueExpressions(_1,_2){var _3=this.getType(),_4=(isc.SimpleType.inheritsFrom(_3,"integer")||isc.SimpleType.inheritsFrom(_3,"float")||isc.SimpleType.inheritsFrom(_3,"date")),_5=isc.DynamicForm.getOperatorIndex(),_6=isc.getKeys(_5),_7={operator:"and",criteria:[]},_8=_7.criteria,_9=[],_10=this.$29n(),_11=isc.DS.get(this.form.expressionDataSource||this.form.dataSource);if(!_1)_1=this.getValue();if(!_1)return;if(!isc.isA.String(_1))_1+="";var _12=this.getOperator();if(_12)_6.add(_12);var _13=_11?_11.getSearchOperator(_12):{id:_12};var _14=_13.caseInsensitive;if(_4&&_1.contains(" and ")){_9=_1.split(" and ")}else if(_4&&_1.contains(" or ")){_9=_1.split(" or ");_7.operator="or"}else if(_1.contains("...")){_9=_1.split("...");if(_9.length==2){var _15=_5["..."],_16;if(_15)_16=(_14?_15.find("caseInsensitive",true):_15[0]);var _17=_11?_11.getField(_2):null;if(_17&&isc.SimpleType.inheritsFrom(_17.type,"date")){_9[0]=new Date(Date.parse(_9[0]));_9[0].logicalDate=true;_9[1]=new Date(Date.parse(_9[1]));_9[1].logicalDate=true}else if(_17&&_17.type=="text"){if(!_9[1].endsWith(this.$25r)){_9[1]+=this.$25r}}
return{fieldName:_2,operator:_16.ID,start:_9[0],end:_9[1]}}}else{_9=[_1]}
var _18=[" and "," or "];for(var i=0;i<_9.length;i++){var _20=_9[i],_21={fieldName:_2}
_17=_11?_11.getField(_2):null,_22=(_17?_17&&isc.SimpleType.inheritsFrom(_17.type,"date"):false),_23=false;for(var _24 in _5){if(!_24)continue;var _25=_5[_24],_26=false,_27;if(_24=="=="&&isc.isA.String(_20)&&_20.startsWith("=")&&!_20.startsWith("==")&&!_20.startsWith("=("))
{_26=true}
if(_25&&_25.length){var _28=_25.findAll("caseInsensitive",_14);if(_28==null||_28.length==0)_28=_25;if(_28.length>1){}
_27=_28[0]}
if(!_27||!_27.symbol||_18.contains(_27.symbol)){continue}
if(_6.contains(_27.symbol)&&((isc.isA.String(_20)&&(_20.startsWith(_27.symbol)||(_27.symbol=="..."&&_20.contains(_27.symbol))))||_26))
{_23=true;if(_20.startsWith(_27.symbol)){_20=_20.substring(_27.symbol.length-(_26?1:0))}
if(_27.closingSymbol){if(_20.endsWith(_27.closingSymbol)){_20=_20.substring(0,_20.length-_27.closingSymbol.length)}}
if(_20.contains("...")){var _29=_20.split("...");if(_29.length==2){var _15=_5["..."],_16;if(_15)_16=(_14?_15.find("caseInsensitive",true):_15[0]);var _17=_11?_11.getField(_2):null;if(_17&&isc.SimpleType.inheritsFrom(_17.type,"date")){_29[0]=new Date(Date.parse(_29[0]));_29[0].logicalDate=true;_29[1]=new Date(Date.parse(_29[1]));_29[1].logicalDate=true}else if(_17&&_17.type=="text"){if(!_29[1].endsWith(this.$25r)){_29[1]+=this.$25r}}
_7.criteria.add({fieldName:_2,operator:_16.ID,start:_29[0],end:_29[1]});continue}}
if(_22){_20=new Date(Date.parse(_20));_20.logicalDate=true}
_21.operator=_27.ID;if(_27.processValue){_20=_27.processValue(_20,_11)}
if(_27.wildCard&&isc.isA.String(_20)&&_20.contains(_27.wildCard)){var _30=_20.split(_27.wildCard),_31;if(_30.length>1){for(var _32=0;_32<_30.length;_32++){var _33=_30[_32];if(!_33||_33.length==0)continue;_31={fieldName:_2,value:_33};var _34=_32>0,_35=_30.length-1>_32;if(_34&&_35){_31.operator=_14?"iContains":"contains"}else if(_34){_31.operator=_14?"iEndsWith":"endsWith"}else if(_35){_31.operator=_14?"iStartsWith":"startsWith"}
_7.criteria.add(_31)}
this.$30v=true;_21.operator=null}}else{if(_27.valueType!="none")_21.value=_20}
break}}
if(!_23){_21.operator=_12;_21.value=_20}
if(_21.operator)_7.criteria.add(_21)}
if(_7.criteria.length==1)_7=_7.criteria[0];if(_7.criteria&&_7.criteria.length==0)_7=null;return _7}
,isc.A.flattenExpressionCriteria=function isc_FormItem_flattenExpressionCriteria(_1){var _2=[];for(var i=0;i<_1.length;i++){var _4=_1[i];if(!_4.criteria){_2.add(_4)}else{_2.addList(this.flattenExpressionCriteria(_4.criteria))}}
return _2}
,isc.A.buildValueExpressions=function isc_FormItem_buildValueExpressions(_1){var _2=_1,_3=isc.shallowClone(_2),_4=" "+_3.operator+" ",_5=[_4],_6=[],_7="",_8=isc.DS.get(this.form.expressionDataSource||this.form.dataSource);if(_2.criteria){_3.criteria=this.flattenExpressionCriteria(_2.criteria)}
var _9=isc.DynamicForm.getOperatorIndex(),_10=isc.getKeys(_9),_11=this.validOperators;if(!_11){_11=[];for(var j=0;j<_10.length;j++){var _13=_9[_10[j]];_11.addList(_13.getProperty("ID"))}}
var _14=this.getOperator();if(_14)_11.add(_14);var _15=_8?_8.getSearchOperator(_14):{id:_14};var _16=_15.caseInsensitive,_17=false,_18;if(!_3.criteria){var _19=[_3];_3={criteria:_19}}
var _20=["startsWith","iStartsWith","contains","iContains","endsWith","iEndsWith"];if(this.useWildCardsByDefault&&this.type=="text"&&(_3.criteria.length>1||(_3.criteria.length==1&&_20.contains(_3.criteria[0].operator)&&_3.criteria[0].value&&_3.criteria[0].value.startsWith("=")&&!_3.criteria[0].value.startsWith("==")&&!_3.criteria[0].value.startsWith("=("))||this.$30v))
{_17=true;var _13=_9["=="];var _21=_13.find({"ID":"equals"});_18=_21.wildCard;_5[0]=""}
var _22=0;for(var i=0;i<_3.criteria.length;i++){var _24=_3.criteria[i],_25=_24.operator,_26=_24.value,_27=_8?_8.getField(_24.fieldName):null;for(var j=0;j<_10.length;j++){var _13=_9[_10[j]];var _28=_13.find({"ID":_25});if(_28){_25=_28;break}}
if(i>0){_5.add(_4)}
if(isc.isA.String(_25)){this.logWarn("Unknown filter-expression operator: '"+_25+"'")}else if(_17){if(_25.ID=="contains"||_25.ID=="iContains"){if(_6[_6.length-1]!=_18)_6.add(_18);_6.add(_24.value);_6.add(_18)}else if(_25.ID=="startsWith"||_25.ID=="iStartsWith"){_6.add(_24.value);_6.add(_18)}else if(_25.ID=="endsWith"||_25.ID=="iEndsWith"){if(_6[_6.length-1]!=_18)_6.add(_18);_6.add(_24.value)}}else if(_25.ID==_14){_6.add(_24.value)}else if(_25.ID=="betweenInclusive"||_25.ID=="iBetweenInclusive"){if(_3.criteria.length>1)_5.addAt(_25.symbol,_22);else _5[_22]=_25.symbol
_22++;var _29=_24.end;if(_27&&_27.type=="text"){if(_29&&_29.endsWith(this.$25r)){_29=_29.replace(this.$25r,"")}}
_6.addList([_24.start,_29])}else if(_25.ID=="isNull"||_25.ID=="notNull"){_6.add(_25.symbol)}else if(_11.contains(_25.ID)){var _30=_25;if(isc.isAn.Array(_26))_26=_26.join(_25.valueSeparator);if(_30.ID!=_15){_26=(_30&&_30.symbol?_30.symbol:"")+_26;if(_30.closingSymbol)_26+=_30.closingSymbol}
_6.add(_26)}else if(_25.ID.startsWith("i")){var _31=_25.ID.substring(1),_32=_31.charAt(0);_31=_32.toLowerCase()+_31.substring(1)
if(_11.contains(_31)){var _33=_10.find("ID",_31);if(_33.ID!=_15){_26=(_30&&_30.symbol?_30.symbol:"")+_26;if(_30.closingSymbol)_26+=_30.closingSymbol}
_6.add(_26)}}
_22++}
if(_17)_6.addAt("=",0);if(_5.length>1){for(var i=0;i<_6.length;i++){_7+=_6[i];if(i<_6.length-1)_7+=_5[i]}}else{_7=_6.join((_6.length>1?_5[0]:""))}
delete this.$30v;return _7.length>0?_7:null}
);isc.B._maxIndex=isc.C+425;isc.FormItem.registerStringMethods({showIf:"item,value,form,values",defaultDynamicValue:"item,form,values",focus:"form,item",blur:"form,item",editorEnter:"form,item,value",editorExit:"form,item,value",click:"form,item",doubleClick:"form,item",iconClick:"form,item,icon",iconKeyPress:"keyName,character,form,item,icon",change:"form,item,value,oldValue",changed:"form,item,value",transformInput:"form,item,value,oldValue",cellClick:"form,item",cellDoubleClick:"form,item",titleClick:"form,item",titleDoubleClick:"form,item",mouseMove:"form,item",mouseOver:"form,item",mouseOut:"form,item",titleMove:"form,item",titleOver:"form,item",titleOut:"form,item",itemHover:"item,form",titleHover:"item,form",keyPress:"item, form, keyName, characterValue",keyDown:"item,form,keyName",keyUp:"item,form,keyName",getValueIcon:"value",formSaved:"request,response,data",formatValue:"value,record,form,item",formatEditorValue:"value,record,form,item",parseEditorValue:"value,form,item"});isc.FormItem.getPrototype().toString=function(){return"["+this.Class+" ID:"+this.ID+(this.name!=null?" name:"+this.name:"")+"]"};isc.ClassFactory.defineClass("FormItemFactory");isc.A=isc.FormItemFactory;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$ia="text";isc.A.$30w="Item";isc.A.$30x="TextareaItem";isc.A.$30y="TextAreaItem";isc.A.$30z="DatetimeItem";isc.A.$300="DateTimeItem";isc.A.$301={};isc.B.push(isc.A.getItemClassName=function isc_c_FormItemFactory_getItemClassName(_1,_2,_3,_4){if(_2==null)_2=_1.editorType||_1.formItemType||_1.type;if(isc.isA.String(_2)){var _5=_2.toLowerCase();if(_5.contains(isc.DynamicForm.$22c)){if(_5=="selectother"||_5=="selectotheritem"){_5="select";if(!_4)_1.isSelectOther=true}
if(_5=="select"||_5=="selectitem"){_3=_1.form||_3;var _6=(!isc.ListGrid||(_1.multiple&&_1.multipleAppearance=="grid")||(_3?_3.useNativeSelectItems:false));if(_6)_2="NativeSelectItem"
else _2="SelectItem"}}
if(_5==isc.DynamicForm.$22h)_2="MultiFileItem";else if(_5==isc.DynamicForm.$22i)_2="MultiUploadItem";else if(_5==isc.DynamicForm.$22k.toLowerCase())_2="SOAPUploadItem"}
return _2}
,isc.A.getItemClass=function isc_c_FormItemFactory_getItemClass(_1){var _2=isc.ClassFactory.getClass(_1);if(!_2||!isc.isA.FormItem(_2)){if(_1!=null&&_1.startsWith("T")){var _3=_1.substring(1),_2=isc.ClassFactory.getClass(_3);if(isc.isA.FormItem(_2))return _2}
if(_1==null)_1=this.$ia;var _4=this.$301,_5=_4[_1];if(!_5){_5=_4[_1]=_1.substring(0,1).toUpperCase()+_1.substring(1)+this.$30w}
if(_5==this.$30x)_5=this.$30y;if(_5==this.$30z)_5=this.$300;_2=isc.ClassFactory.getClass(_5)}
return _2}
,isc.A.makeItem=function isc_c_FormItemFactory_makeItem(_1){if(_1==null)return null;if(isc.isA.FormItem(_1)){return _1}
var _2=this.getItemClassName(_1),_3=this.getItemClass(_2);if(!_3){this.logWarn("makeItem(): type "+_1.type+" not recognized, using TextItem");_3=isc.TextItem}
return isc.ClassFactory.newInstance(_3,_1)}
);isc.B._maxIndex=isc.C+3;isc.ClassFactory.defineClass("Validator");isc.Validator.addProperties({})
isc.A=isc.Validator;isc.A.notABoolean="Must be a true/false value";isc.A.notAString="Must be a string.";isc.A.notAnInteger="Must be a whole number.";isc.A.notADecimal="Must be a valid decimal.";isc.A.notADate="Must be a date.";isc.A.notATime="Must be a time.";isc.A.notAnIdentifier="Identifiers must start with a letter, underscore or $ character, "+"and may contain only letters, numbers, underscores or $ characters.";isc.A.notARegex="Must be a valid regular expression.";isc.A.notAColor="Must be a CSS color identifier.";isc.A.mustBeLessThan="Must be no more than ${max}";isc.A.mustBeGreaterThan="Must be at least ${min}";isc.A.mustBeLaterThan="Must be later than ${min.toShortDate()}";isc.A.mustBeLaterThanTime="Must be later than ${isc.Time.toShortTime(min)}";isc.A.mustBeEarlierThan="Must be earlier than ${max.toShortDate()}";isc.A.mustBeEarlierThanTime="Must be earlier than ${isc.Time.toShortTime(max)}";isc.A.mustBeShorterThan="Must be no more than ${max} characters";isc.A.mustBeLongerThan="Must be at least ${min} characters";isc.A.mustBeExactLength="Must be exactly ${max} characters";isc.A.notAMeasure='Must be a whole number, percentage, "*" or "auto"';isc.A.requiredField="Field is required";isc.A.notOneOf="Not a valid option";isc.A.notAFunction='Must be a function.';isc.A.$302="true";isc.A.$3i="false";isc.A.$303=".";isc.A.READONLY="readOnly";isc.A.HIDDEN="hidden";isc.A.DISABLED="disabled";isc.A.$304={isBoolean:{type:"isBoolean",title:"Value is boolean",valueType:"none",dataType:"none",condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3))return true;if(isc.isA.Boolean(_3))return true;if(!_2.errorMessage){_2.defaultErrorMessage=isc.Validator.notABoolean}
if(isc.isA.String(_3)){var _4=isc.Validator;_2.resultingValue=(_3==_4.$302);return(_3==_4.$302||_3==_4.$3i)}else if(isc.isA.Number(_3)){_2.resultingValue=(_3!=0);return(_3==0||_3==1)}
_2.resultingValue=!!_3;return false}},isString:{type:"isString",title:"Value is a string",valueType:"none",dataType:"none",condition:function(_1,_2,_3){if(_3==null||isc.isA.String(_3))return true;if(!_2.errorMessage)_2.defaultErrorMessage=isc.Validator.notAString;_2.resultingValue=isc.iscToLocaleString(_3);return true}},isInteger:{type:"isInteger",title:"Value is a whole number",valueType:"none",dataType:"none",condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3))return true;if(!_2.errorMessage)_2.defaultErrorMessage=isc.Validator.notAnInteger;if(isNaN(_3))return false;var _4=parseInt(_3,10),_5=(_3==_4);if(_2.convertToInteger){var _6=parseFloat(_3),_4=Math.round(_6);_2.resultingValue=_4;return true}else{if(_5){_2.resultingValue=_4;return true}else return false}}},isFloat:{type:"isFloat",title:"Value is a floating point number",valueType:"none",dataType:"none",condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3))return true;if(!_2.errorMessage)_2.defaultErrorMessage=isc.Validator.notADecimal;var _4;if(_3==isc.Validator.$303){_4="0."}else{_4=parseFloat(_3);if(isNaN(_4)||_4!=_3)return false}
_2.resultingValue=_4;return true}},isDate:{type:"isDate",title:"Value is a date",valueType:"none",dataType:"none",condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3)||isc.isA.Date(_3))return true;if(!_2.errorMessage)_2.defaultErrorMessage=isc.Validator.notADate;var _4=Date.parseSchemaDate(_3);if(_4==null)return false;_2.resultingValue=_4;return true}},isTime:{type:"isTime",title:"Value is a logical Time value",valueType:"none",dataType:"none",condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3)||isc.isA.Date(_3))return true;if(!_2.errorMessage)_2.defaultErrorMessage=isc.Validator.notATime;var _4=isc.Time.parseInput(_3,true);if(_4==null){_4=Date.parseSchemaDate(_3)}
if(_4!=null){_2.resultingValue=_4;return true}
return false}},isIdentifier:{type:"isIdentifier",valueType:"none",dataType:"none",condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3))return true;if(!_2.errorMessage){_2.defaultErrorMessage=isc.Validator.notAnIdentifier}
return _3.match(/^[a-zA-Z_\$][\w\$]*$/)!=null}},isRegexp:{type:"isRegexp",valueType:"none",dataType:"none",condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3))return true;if(!_2.errorMessage)_2.defaultErrorMessage=isc.Validator.notARegex;if(typeof _3=='object'&&_3.constructor==RegExp)return true;if(isc.Browser.isDOM){if(!isc.Validator.$305){isc.Validator.$305=new Function("value","try{var regex=new RegExp(value)}catch(e){return false}return true")}
return isc.Validator.$305(_3)}else{var _4=new RegExp(_3);return true}}},isFunction:{type:"isFunction",valueType:"none",dataType:"none",condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3)||_3==isc.Class.NO_OP||isc.isA.StringMethod(_3))
{return true}
if(!_2.errorMessage)_2.defaultErrorMessage=isc.Validator.notAFunction;try{isc.Func.expressionToFunction("",_3)}catch(e){return false}
if(_3.iscAction)_3=_3.iscAction;_2.resultingValue=isc.StringMethod.create({value:_3});return true}},isColor:{type:"isColor",valueType:"none",dataType:"text",isColor:function(_1,_2,_3){if(!_2.errorMessage)_2.defaultErrorMessage=isc.Validator.notAColor;if(!_3)return true;return isc.isA.color(_3)}},isMeasure:{type:"isMeasure",valueType:"none",dataType:["integer","string"],condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3)||_3=="*")return true;if(!_2.errorMessage)_2.defaultErrorMessage=isc.Validator.notAMeasure;if(isc.isA.String(_3)&&_3.charAt(_3.length-1)=='%'){_3=_3.slice(0,-1);return _3.match(/\d+\.?\d*/)!=null}
return isc.Validator.processValidator(_1,_2,_3,"integerOrAuto")}},integerOrAuto:{type:"integerOrAuto",valueType:"none",dataType:["integer","string"],condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3)||(isc.isA.String(_3)&&_3.toLowerCase()=="auto"))return true;return isc.Validator.processValidator(_1,_2,_3,"isInteger")}},integerRange:{type:"integerRange",title:"Value is an integer within the specified range",valueType:"valueRange",dataType:"integer",rangeStartAttribute:"min",rangeEndAttribute:"max",condition:function(_1,_2,_3){var _4=_3;if(!isc.isA.String(_3))_3=parseInt(_3,10);if(isNaN(_3)||_3!=_4)return true;_2.dynamicErrorMessageArguments={validator:_2,max:_2.max,min:_2.min}
if(isc.isA.Number(_2.max)&&((!_2.exclusive&&_3>_2.max)||(_2.exclusive&&_3>=_2.max)))
{if(!_2.errorMessage){_2.defaultErrorMessage=isc.Validator.mustBeLessThan}
return false}
if(isc.isA.Number(_2.min)&&((!_2.exclusive&&_3<_2.min)||(_2.exclusive&&_3<=_2.min)))
{if(!_2.errorMessage){_2.defaultErrorMessage=isc.Validator.mustBeGreaterThan}
return false}
return true}},lengthRange:{type:"lengthRange",title:"Value is a string whose length falls within the specified range",valueType:"valueRange",dataType:"text",rangeStartAttribute:"min",rangeEndAttribute:"max",condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3))return true;if(!isc.isA.String(_3))return true;_2.dynamicErrorMessageArguments={validator:_2,max:_2.max,min:_2.min}
var _4=_3.length,_5=_2.max!=null?parseInt(_2.max,10):null,_6=_2.min!=null?parseInt(_2.min,10):null;if(!isc.isA.Number(_5))_5=null;if(!isc.isA.Number(_6))_6=null;if(_5!=null&&_4>_5){_2.defaultErrorMessage=(_5==_6?isc.Validator.mustBeExactLength:isc.Validator.mustBeShorterThan);return false}
if(_6!=null&&_4<_6){_2.defaultErrorMessage=(_5==_6?isc.Validator.mustBeExactLength:isc.Validator.mustBeLongerThan);return false}
return true}},contains:{type:"contains",title:"Value contains the specified substring",valueType:"fieldType",dataType:"text",valueAttribute:"substring",condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3))return true;if(!isc.isA.String(_3))_3=isc.iscToLocaleString(_3);return _3.indexOf(_2.substring)>-1}},doesntContain:{type:"doesntContain",title:"Value does not contain the specified substring",valueType:"fieldType",dataType:"text",valueAttribute:"substring",condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3))return true;if(!isc.isA.String(_3))_3=isc.iscToLocaleString(_3);return _3.indexOf(_2.substring)==-1}},substringCount:{type:"substringCount",title:"Value contains a specified substring multiple times",valueType:"custom",dataType:"text",editorType:"SubstringCountEditor",getAttributesFromEditor:function(_1,_2){var _3=_2.canvas;return _3.getValues()},setEditorAttributes:function(_1,_2,_3){var _4=_2.canvas;if(_3==null){_4.clearValues();return}
_4.setValue("substring",_3.substring);_4.setValue("count",_3.count);_4.setValue("operator",_3.operator)},condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3))return true;var _4=_2.substring;for(var _5=0,_6=0;_5<_3.length;_5++){_5=_3.indexOf(_4,_5);if(_5>-1)_6++;else break}
var _7=_2.operator,_8=_2.count;if(!_7)_7="==";if(!_8)_8=0;switch(_7){case"==":return _6==_8;case"!=":return _6!=_8;case"<":return _6<_8;case"<=":return _6<=_8;case">":return _6>_8;case">=":return _6>=_8}
return false}},regexp:{type:"regexp",title:"Value matches a regular expression",valueType:"fieldType",dataType:"text",valueAttribute:"expression",condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3))return true;var _4=_2.expression;if(isc.isA.String(_4)){_4=new RegExp(_4)}
return _4.test(_3)}},mask:{type:"mask",title:"Value matches a regular expression mask",valueType:"custom",dataType:"text",editorType:"MaskRuleEditor",getAttributesFromEditor:function(_1,_2){var _3=_2.canvas;return _3.getValues()},setEditorAttributes:function(_1,_2,_3){var _4=_2.canvas;if(_3==null){_4.clearValues();return}
_4.setValue("mask",_3.mask);_4.setValue("transformTo",_3.transformTo)},condition:function(_1,_2,_3){if(_3==null)return true;if(!isc.isA.String(_3)&&isc.isA.Function(_3.toString))
_3=_3.toString();if(isc.is.emptyString(_3))return true;var _4=_2.mask;if(isc.isA.String(_4))_4=_2.mask=new RegExp(_4);if(!_4.test(_3)){return false}else{if(_2.transformTo){_2.resultingValue=_3.replace(_4,_2.transformTo)}}
return true}},dateRange:{type:"dateRange",title:"Value is a date within the specified range",valueType:"valueRange",dataType:"date",rangeStartAttribute:"min",rangeEndAttribute:"max",editorType:"RelativeDateItem",condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3))return true;if(!isc.isA.Date(_3))return false;var _4=_2.min,_5=_2.max;if(_4!=null&&!isc.isA.Date(_4))_4=_2.min=Date.parseSchemaDate(_4);if(_5!=null&&!isc.isA.Date(_5))_5=_2.max=Date.parseSchemaDate(_5);_2.dynamicErrorMessageArguments={validator:_2,max:_5,min:_4}
if(isc.isA.Date(_4)&&((!_2.exclusive&&_3.getTime()<_4.getTime())||(_2.exclusive&&_3.getTime()<=_4.getTime())))
{if(!_2.errorMessage){_2.defaultErrorMessage=isc.Validator.mustBeLaterThan}
return false}
if(isc.isA.Date(_5)&&((!_2.exclusive&&_3.getTime()>_5.getTime())||(_2.exclusive&&_3.getTime()>=_5.getTime())))
{if(!_2.errorMessage){_2.defaultErrorMessage=isc.Validator.mustBeEarlierThan}
return false}
return true}},timeRange:{type:"timeRange",description:"Value is a logical time value within the specified range",valueType:"valueRange",dataType:"time",rangeStartAttribute:"min",rangeEndAttribute:"max",condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3))return true;if(!isc.isA.Date(_3))return false;var _4=_2.min,_5=_2.max;if(_4!=null&&!isc.isA.Date(_4)){if(isc.isA.String(_4)&&_4.contains(":")){var _6=_4.split(":"),_7=_6[0]||0,_8=_6[1]||0,_9=_6[2]||0,_10=_6[3]||0;_4=_2.min=new Date(0,0,0,_7,_8,_9,_10)}else{_4=_2.min=Date.parseSchemaDate(_4)}}
if(_5!=null&&!isc.isA.Date(_5)){if(isc.isA.String(_5)&&_5.contains(":")){var _6=_5.split(":"),_7=_6[0]||0,_8=_6[1]||0,_9=_6[2]||0,_10=_6[3]||0;_5=_2.max=new Date(0,0,0,_7,_8,_9,_10)}else{_5=_2.max=Date.parseSchemaDate(_5)}}
_2.dynamicErrorMessageArguments={validator:_2,max:_5,min:_4};_4.setFullYear(_3.getFullYear());_4.setMonth(_3.getMonth());_4.setDate(_3.getDate());_5.setFullYear(_3.getFullYear());_5.setMonth(_3.getMonth());_5.setDate(_3.getDate());if(isc.isA.Date(_4)&&((!_2.exclusive&&_3<_4)||(_2.exclusive&&_3<=_4)))
{if(!_2.errorMessage){_2.defaultErrorMessage=isc.Validator.mustBeLaterThanTime}
return false}
if(isc.isA.Date(_5)&&((!_2.exclusive&&_3>_5)||(_2.exclusive&&_3>=_5)))
{if(!_2.errorMessage){_2.defaultErrorMessage=isc.Validator.mustBeEarlierThanTime}
return false}
return true}},floatLimit:{type:"floatLimit",valueType:"custom",dataType:"float",condition:function(_1,_2,_3){var _4;if(_2.precision!=null){if(_2.roundToPrecision==null)_2.roundToPrecision=true;if(!isc.Validator.processValidator(_1,_2,_3,"floatPrecision"))
return false;if(_2.resultingValue!=null)
_3=_4=_2.resultingValue}
if(_2.min!=null||_2.max!=null){if(!isc.Validator.processValidator(_1,_2,_3,"floatRange")){return false}else{if(_4!=null&&_2.resultingValue==null&&_2.roundToPrecision)
_2.resultingValue=_4}}
return true}},floatRange:{type:"floatRange",title:"Value is a floating point number within the specified range",valueType:"custom",dataType:"float",editorType:"FloatRangeEditor",getAttributesFromEditor:function(_1,_2){var _3=_2.canvas;return _3.getValues()},setEditorAttributes:function(_1,_2,_3){var _4=_2.canvas;if(_3==null){_4.clearValues();return}
_4.setValue("min",_3.min);_4.setValue("max",_3.max);_4.setValue("exclusive",!!_3.exclusive)},condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3))return true;var _4=_3;if(!isc.isA.String(_3))_4=parseFloat(_4);if(isNaN(_4)||_4!=_3)return true;_2.dynamicErrorMessageArguments={validator:_2,max:_2.max,min:_2.min}
var _5,_6;if(_2.max!=null){_5=isc.isA.Number(_2.max)?_2.max:parseFloat(_2.max)}
if(_2.min!=null){_6=isc.isA.Number(_2.min)?_2.min:parseFloat(_2.min)}
if(isc.isA.Number(_5)&&((!_2.exclusive&&_4>_5)||(_2.exclusive&&_4>=_5)))
{if(!_2.errorMessage){_2.defaultErrorMessage=isc.Validator.mustBeLessThan}
return false}
if(isc.isA.Number(_6)&&((!_2.exclusive&&_4<_6)||(_2.exclusive&&_4<=_6)))
{if(!_2.errorMessage){_2.defaultErrorMessage=isc.Validator.mustBeGreaterThan}
return false}
return true}},floatPrecision:{type:"floatPrecision",title:"Value is a floating point number specified to the appropriate precision",valueType:"custom",dataType:"float",editorType:"FloatPrecisionEditor",getAttributesFromEditor:function(_1,_2){var _3=_2.canvas;return _3.getValues()},setEditorAttributes:function(_1,_2,_3){var _4=_2.canvas;if(_3==null){_4.clearValues();return}
_4.setValue("precision",_3.precision);_4.setValue("roundToPrecision",_3.roundToPrecision)},condition:function(_1,_2,_3){if(_3==null||isc.is.emptyString(_3))return true;var _4=parseFloat(_3);if(isNaN(_4)||_4!=_3)return false;if(isc.isA.Number(_2.precision)){var _5=Math.pow(10,_2.precision);var _6=(Math.round(_4*_5))/_5;if(_2.roundToPrecision){_2.resultingValue=_6;return true}else{return(_4==_6)}}}},requiredIf:{type:"requiredIf",title:"Conditionally required field",valueType:"fieldType",dataType:"none",valueAttribute:"expression",editorType:"TextAreaItem",condition:function(_1,_2,_3,_4){if(_2.expression!=null&&!isc.isA.Function(_2.expression)){isc.Func.replaceWithMethod(_2,"expression","item,validator,value,record")}
var _5=_2.expression(_1,_2,_3,_4);if(_2.errorMessage==null)
_2.errorMessage=isc.Validator.requiredField;return!_5||(_3!=null&&!isc.is.emptyString(_3))}},isOneOf:{type:"isOneOf",title:"Is one of list",valueType:"valueSet",dataType:"none",valueAttribute:"list",condition:function(_1,_2,_3,_4){if(_3==null||isc.is.emptyString(_3))return true;var _5=_2.list||(_1?(_1.getValueMap?_1.getValueMap():_1.valueMap):null),_6=_5;if(!isc.isAn.Array(_5)&&isc.isAn.Object(_5)){_6=isc.getKeys(_5)}
if(_6!=null){for(var i=0,_8=_6.length;i<_8;i++){if(_6[i]==_3)return true}}else{isc.Log.logWarn("isOneOf validator specified with no specified list of options "+"or valueMap - validator will always fail. "+"Field definition:"+isc.Log.echo(_1),"validation")}
if(!_2.errorMessage){_2.defaultErrorMessage=isc.Validator.notOneOf}
return false}},required:{type:"required",title:"Required field",valueType:"none",dataType:"none",condition:function(_1,_2,_3,_4){if(_2.errorMessage==null)
_2.errorMessage=isc.Validator.requiredField;return(_3!=null&&!isc.is.emptyString(_3))},action:function(_1,_2,_3,_4,_5){if(!_2.required){_2.$21n=(_1!=null)}}},readOnly:{type:"readOnly",valueType:"custom",dataType:"none",editorType:"ReadOnlyRuleEditor",valueAttribute:"fieldAppearance",title:"Set field read-only state/appearance",condition:function(_1,_2,_3,_4){return true},action:function(_1,_2,_3,_4,_5){if(_3.readOnlyComponent!=null){var _5=window[_3.readOnlyComponent];if(_5==null){this.logWarn("ReadOnly validator has specified component:"+_3.readOnlyComponent+" - unable to find a component with this "+" ID. Ignoring.");return}
if(_3.fieldAppearance==isc.Validator.HIDDEN){if(_1==true)_5.setVisibility("hidden");else _5.setVisibility("inherit")}else{if(_3.fieldAppearance!=isc.Validator.DISABLED){this.logWarn("ReadOnly type rule has field appearance set to:"+_3.fieldAppearance+" and has a specified readOnlyComponent"+". This is not supported - using appearance disabled instead.")}
if(_1==true)_5.setDisabled(true);else _5.setDisabled(false)}
return}
var _6=_2.name;if(_3.fieldAppearance==isc.Validator.HIDDEN){if(_1==true)_5.hideField(_6);else _5.showField(_6)}else if(_3.fieldAppearance==isc.Validator.DISABLED){if(_1==true)_5.disableField(_6);else _5.enableField(_6)}else{if(_1==true)_5.setFieldCanEdit(_6,false);else _5.setFieldCanEdit(_6,true)}}},matchesField:{type:"matchesField",title:"Matches another field value",valueType:"fieldName",dataType:"none",valueAttribute:"otherField",condition:function(_1,_2,_3,_4){if(_2.otherField==null){isc.logWarn("matchesField validator is missing 'otherField' definition. "+"Validator forced false.");return false}
return(_3==_4[_2.otherField])}},isUnique:{type:"isUnique",valueType:"none",dataType:"none",title:"Validate field value is unique on DataSource",requiresServer:true},hasRelatedRecord:{type:"hasRelatedRecord",valueType:"none",dataType:"none",title:"Validate field value exists on a related DataSource",requiresServer:true},serverCustom:{type:"serverCustom",valueType:"none",dataType:"none",title:"Validate field value using a custom server expression",requiresServer:true},message:{type:"message",title:"displays an informational or warning message",valueType:"none",dataType:"none",condition:function(_1,_2,_3,_4){return true},action:function(_1,_2,_3,_4,_5){var _6=_3.displayMode,_7=(_3.message==null?"errorMessage":"message"),_8=isc.Validator.getErrorMessage(_3,_7);if(_6=="form"){if(!_5.addFieldErrors){return}
if(_1==true){_5.addFieldErrors(_2.name,_8,true)}else{_5.setFieldErrors(_2.name,null,true)}}else if(_6=="transient"){if(_1==true){var _9=function(){if(_5&&_5.hasFocus)_5.blur();isc.showFadingPrompt(_8,_3.duration)};isc.Page.setEvent("idle",_9,isc.Page.FIRE_ONCE)}}else{var _10=_3.severity;if(_1==true){var _9=function(){if(_5&&_5.hasFocus)_5.blur();if(_10=="warning"){isc.warn(_8)}else{isc.say(_8)}};isc.Page.setEvent("idle",_9,isc.Page.FIRE_ONCE)}}}},populate:{type:"populate",title:"Populates a target field with a value calculated based on values in other fields",valueType:"custom",dataType:"none",editorType:"PopulateRuleEditor",getAttributesFromEditor:function(_1,_2){return _2.getValue()},setEditorAttributes:function(_1,_2,_3){if(_3!=null&&_3.formula!=null){_2.setValue({formula:_3.formula,formulaVars:_3.formulaVars})}else{_2.clearValue()}},condition:function(_1,_2,_3,_4){return true},action:function(_1,_2,_3,_4,_5){if(_1!=true)return;if(_3.$306==null){var _6={};var _7=_6.formulaVars=_3.formulaVars;_6.text=_3.formula;var _8=[];for(var _9 in _7){var _10={};_10.mappingKey=_9;_10.name=_7[_9];_8.add(_10)}
_3.$306=isc.FormulaBuilder.generateFunction(_6,_8,null,true)}
var _11=_3.$306(_4,_5);if(_2.setValue){_2.setValue(_11)}else if(_5&&_5.setValue){var _12=_2.fieldName;if(_12==null)_12=_2.dataPath;_5.setValue(_12,_11)}}},setRequired:{type:"setRequired",valueType:"none",dataType:"none",condition:function(_1,_2,_3,_4){return true},action:function(_1,_2,_3,_4,_5){var _6=!!_1;var _7=_2;if(!_7.setRequired&&_5.getItem){_7=_5.getItem(_2.name||_2.dataPath)}
if(_7&&_7.setRequired){_7.setRequired(_6)}else{_2.required=_6}}}};isc.A=isc.Validator;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.getValidatorType=function isc_c_Validator_getValidatorType(_1){var _2=_1.type;if(_2==null&&_1._constructor)_2=_1._constructor;return _2}
,isc.A.isServerValidator=function isc_c_Validator_isServerValidator(_1){if(_1.serverOnly)return true;var _2=this.$304[this.getValidatorType(_1)];if(_2!=null&&_2.requiresServer)return true;return false}
,isc.A.processValidator=function isc_c_Validator_processValidator(_1,_2,_3,_4,_5){if(_2.serverOnly)return true;if(_4==null)_4=isc.Validator.getValidatorType(_2);var _6=true;var _7;if(_4!=null)_7=this.$304[_4];var _8;if(_7==null){if(_2.condition){if(!isc.isA.Function(_2.condition)){this.logDebug("Creating function for validation condition:\r"+_2.condition);isc.Func.replaceWithMethod(_2,"condition","item,validator,value,record")}
_8=_2.condition}}else{if(_7.requiresServer==true){return true}
_8=_7.condition;if(!_2.errorMessage){_2.defaultErrorMessage=_7.defaultErrorMessage}}
if(_8!=null){var _9=_2.validateEachItem;if(_9==null)_9=_1.validateEachItem;if(_1&&_1.multiple&&_9&&isc.isAn.Array(_3)){var _10=[];for(var i=0;i<_3.length;i++){delete _2.resultingValue;_6=_6&&_8(_1,_2,_3[i],_5);_10[i]=(_2.resultingValue!=null?_2.resultingValue:_3[i])}
_2.resultingValue=_10}else{delete _2.resultingValue;_6=_8(_1,_2,_3,_5)}}else{this.logWarn("validator not understood on item: "+isc.echo(_1)+":\r"+isc.echoFull(_2))}
return _6}
,isc.A.performAction=function isc_c_Validator_performAction(_1,_2,_3,_4,_5){var _6=this.getValidatorType(_3);var _7;if(_6!=null)_7=this.$304[_6];var _8;if(_7!=null){_8=_7.action}
if(_8==null&&_3.action){if(!isc.isA.Function(_3.action)){this.logDebug("Creating function for validation action:\r"+_3.action);isc.Func.replaceWithMethod(_3,"action","result,item,validator,record,component")}
_8=_3.action}
if(_8!=null){_8(_1,_2,_3,_4,_5)}}
,isc.A.getErrorMessage=function isc_c_Validator_getErrorMessage(_1,_2){var _3=_2?_1[_2]:_1.errorMessage;if(_3==null)_3=_1.defaultErrorMessage;if(_3&&_1.dynamicErrorMessageArguments){_3=_3.evalDynamicString(null,_1.dynamicErrorMessageArguments)}
return _3}
,isc.A.addValidator=function isc_c_Validator_addValidator(_1,_2){if(isc.isA.String(_1)){var _3={};_3[_1]=_2;return this.addValidators(_3)}}
,isc.A.addValidators=function isc_c_Validator_addValidators(_1){for(var _2 in _1){var _3={};_3.type=_2;_3.condition=_1[_2];if(!isc.isA.Function(_3.condition)){isc.Func.replaceWithMethod(_3,"condition","item,validator,value")}
_1[_2]=_3}
this.addValidatorDefinitions(_1)}
,isc.A.addValidatorDefinition=function isc_c_Validator_addValidatorDefinition(_1,_2){if(!isc.isAn.Object(_2)){isc.logWarn("Invalid validator in call to addValidatorDefinition. Ignored.")}
var _3={};_3[_1]=_2;return this.addValidatorDefinitions(_3)}
,isc.A.addValidatorDefinitions=function isc_c_Validator_addValidatorDefinitions(_1){if(!_1||!isc.isAn.Object(_1))return;for(var _2 in _1){if(this.$304[_2]){isc.logWarn("addValidatorDefinitions: Validator definition already exists "+"for type "+_2+". Replacing.")}}
isc.addProperties(this.$304,_1)}
);isc.B._maxIndex=isc.C+9;isc.ClassFactory.defineClass("ContainerItem","FormItem");isc.A=isc.ContainerItem.getPrototype();isc.A.cellSpacing=0;isc.A.cellPadding=2;isc.A.cellBorder=0;isc.A.recalculateItemsOnRedraw=false;isc.A.$23x=false;isc.A.changeOnKeypress=false;isc.ContainerItem.addMethods(isc.applyMask(isc.DynamicForm.getPrototype(),["getTableStartHTML","$21d","$21e","getCellStartHTML","$206","getCellEndHTML","$207","getTitleAlign","getItemPromptHTML","getItem","fieldIdProperty"]))
isc.A=isc.ContainerItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.writeOutLabelTag=true;isc.B.push(isc.A.init=function isc_ContainerItem_init(){this.Super("init",arguments);this.setItems(this.items?this.items:null)}
,isc.A.destroy=function isc_ContainerItem_destroy(){this.Super("destroy",arguments);if(this.items){for(var i=0;i<this.items.length;i++){this.items[i].destroy()}}}
,isc.A.setItems=function isc_ContainerItem_setItems(_1){var _2=this.items?this.items:null;if(_1)this.items=_1;else _1=this.items;if(!this.items)return null;this.logDebug("Creating "+this.items.length+" contained items");var _3=false;for(var _4=0;_4<_1.length;_4++){var _5=_1[_4];if(!_5){_1.removeItem(_4--);continue}
isc.addMethods(_5,{$269:function(){return this.parentItem.$269()}});_5.containerWidget=this.containerWidget;_5.parentItem=this;_5.eventParent=this;_5.form=this.form;_5.showTitle=false;if(!isc.isA.FormItem(_5))_1[_4]=_5=isc.FormItemFactory.makeItem(_5);if(this.accessKey!=null&&!_3&&_5.$mb()){_5.accessKey=this.accessKey;_3=true}
if(_5.name!=null)this[_5.name]=_5}
if(this.isDrawn()){if(_2&&_2!=this.items){this.$307={};for(var i=0;i<_2.length;i++){var _7=_2[i];if(!_1.contains(_2[i])){this.$307[_2[i].getID()]=true}}}}
this.redraw()}
,isc.A.getItems=function isc_ContainerItem_getItems(){return this.items}
,isc.A.getTitleHTML=function isc_ContainerItem_getTitleHTML(){var _1,_2;var _3=this.getTitle();if(!this.writeOutLabelTag||!this.getCanFocus()){return _3}
if(this.accessKey!=null){_3=isc.Canvas.hiliteCharacter(_3,this.accessKey)}
for(var i=0;i<this.items.length;i++){if(this.items[i].getCanFocus()&&this.items[i].hasDataElement()){_2=this.items[i];break}}
if(!_2){return _3}
return isc.SB.concat("<LABEL FOR=",_2.getDataElementId(),">",_3,"</LABEL>")}
,isc.A.$296=function isc_ContainerItem__setElementTabIndex(_1){if(!this.isVisible()||!this.containerWidget.isDrawn())return;this.$297=_1;for(var i=0;i<this.items.length;i++){if(this.items[i].$mb())this.items[i].$296(_1)}
if(!this.form.isDirty()){this.$298()}}
,isc.A.isEditable=function isc_ContainerItem_isEditable(){return false}
,isc.A.$mb=function isc_ContainerItem__canFocus(){if(!this.items)return false;for(var i=0;i<this.items.length;i++){if(this.items[i].$mb())return true}
return false}
,isc.A.focusInItem=function isc_ContainerItem_focusInItem(){if(!this.isVisible()||!this.$mb())return;for(var i=0;i<this.items.length;i++){if(this.items[i].$mb()){this.items[i].focusInItem();break}}}
,isc.A.blurItem=function isc_ContainerItem_blurItem(){for(var i=0;i<this.items.length;i++){if(this.items[i].hasFocus){this.items[i].blurItem();break}}}
,isc.A.$27f=function isc_ContainerItem__applyHandlersToElement(){this.$27p()}
,isc.A.drawn=function isc_ContainerItem_drawn(){var _1=this.items;if(!_1)return;for(var i=0;i<_1.length;i++){if(_1[i].visible!=false)_1[i].drawn()}
return this.Super("drawn",arguments)}
,isc.A.redrawn=function isc_ContainerItem_redrawn(){var _1=this.items;if(!_1)return;for(var i=0;i<_1.length;i++){var _3=_1[i];if(_3.visible!=false){if(!_3.isDrawn())_3.drawn();else _3.redrawn()}else{if(_3.isDrawn())_3.cleared()}}
if(this.$307){for(var _4 in this.$307){if(window[_4]!=null)window[_4].cleared()}
delete this.$307}
return this.Super("redrawn",arguments)}
,isc.A.cleared=function isc_ContainerItem_cleared(){var _1=this.items;if(!_1)return;for(var i=0;i<_1.length;i++){if(_1[i].isDrawn())_1[i].cleared()}
if(this.$307){for(var _3 in this.$307){if(window[_3]!=null)window[_3].cleared()}
delete this.$307}
return this.Super("cleared",arguments)}
,isc.A.makeNamedItem=function isc_ContainerItem_makeNamedItem(_1,_2){if(!this.itemCache)this.itemCache={};var _3=this.itemCache[_1];if(!_3){_3=(this[_1]||this.getClass()[_1]);if(_2!=null){_3=isc.addProperties({},_3,_2)}
_3=this.itemCache[_1]=isc.FormItemFactory.makeItem(_3)}
return _3}
,isc.A.getInnerHTML=function isc_ContainerItem_getInnerHTML(_1,_2,_3,_4){if(!_1)_1={};if(!this.items||this.recalculateItemsOnRedraw||!isc.isA.FormItem(this.items[0]))this.setItems();if(!this.items)return"No items set for containerItem "+this;var _5;if(this.isInactiveHTML()&&this.$226==null){_5=true;this.$226=this.setupInactiveContext(null);if(this.logIsDebugEnabled("inactiveEditorHTML")){this.logDebug("getInnerHTML(): Item is marked as inactive - set up "+"new inactive context ID:"+this.$226,"inactiveEditorHTML")}}
var _6=this.getErrorOrientation(),_7,_8=_6==isc.Canvas.LEFT,_9;if(_3&&(_8||_6==isc.Canvas.RIGHT))
{var _10=this.getErrors();if(_10){_7=true;_9=this.getErrorHTML(_10)}}
var _11=isc.StringBuffer.newInstance();if(this.$20z()){_11.append(this.$26o())}
_11.append(this.getTableStartHTML());var _12=this.items;for(var _13=0;_13<_12.length;_13++){var _14=_12[_13];_14.$20k()
_14.form=this.form;if(_14.showIf){if(!isc.isA.Function(_14.showIf)){isc.Func.replaceWithMethod(_14,"showIf","item,value,form")}
var _15=this.getItemValue(_14,_1);var _16=(_14.showIf(_14,_15,this.form)!=false);if(_16!=_14.visible){_14.visible=_16}}}
for(var _13=0,_17=this.items.length;_13<_17;_13++){var _14=this.items[_13];if(!_14)continue;if(!_14.visible)continue;var _15=this.getItemValue(_14,_1);if(_14.$17u||_13==0){if(_13!=0)_11.append("</TR>");_11.append("<TR>")}
if(_13==0&&_7&&_8){var _18=1;for(var _19=1;_19<this.items.length;_19++){if(this.items[_19].$17u)_18++}
_11.append("<TD ROWSPAN=",_18,">",_9,"</TD>")}
_11.append(this.getCellStartHTML(_14));_11.append(_14.getInnerHTML(_15,true));_11.append(this.getCellEndHTML(_14))}
if(this.showPickerIcon||(this.showIcons&&this.icons!=null)){var _20=this.getTotalIconsWidth();if(this.showPickerIcon)_20+=this.getPickerIconWidth();_11.append(this.$206((this.form.isRTL()?isc.Canvas.RIGHT:isc.Canvas.LEFT),null,this.getCellStyle(),1,1,_20,null,null,isc.Canvas.$rr,null,null,null,(this.icons&&(this.showPickerIcon||this.icons.length>1))));_11.append(this.getIconsHTML(true));_11.append(this.$207(true))}
if(_7&&!_8)_2=true;if(_2){var _21=this.getHint(),_22=!_8?_9:null,_23=(_21&&_22)?_21+_22:(_21||_22);if(_23&&!isc.isA.emptyString(_23)){this.$24v[1]=this.$26p();this.$24v[3]=this.getHintStyle();this.$24v[5]=_23;_11.append(this.$24v)}}
_11.append("</TR></TABLE>");if(_5)delete this.$226;return _11.toString()}
,isc.A.getPickerIcon=function isc_ContainerItem_getPickerIcon(){var _1=this.Super("getPickerIcon",arguments);_1.writeIntoItem=false;return _1}
,isc.A.getItemValue=function isc_ContainerItem_getItemValue(_1,_2){if(_2==null)_2={};if(!isc.isA.FormItem(_1))_1=this.getItem(_1);if(!_1)return null;var _3=_1.getFieldName(),_4=null;if(_1.value!=null)_4=_1.value;if(_4==null&&_3){_4=_2[_3]}
if(_4==null){_4=_1.getDefaultValue();if(_4==null&&this.form&&this.form.values)_4=this.form.values[_3]}
return _4}
,isc.A.$20g=function isc_ContainerItem__itemValueIsDirty(){if(this.items==null)return false;for(var i=0;i<this.items.length;i++){if(this.items[i].$20g())return true}
return this.$30r}
,isc.A.$20k=function isc_ContainerItem__markValueAsNotDirty(){this.$30r=false;for(var i=0;i<this.items.length;i++){this.items[i].$20k()}}
,isc.A.updateDisabled=function isc_ContainerItem_updateDisabled(){this.Super("updateDisabled",arguments);if(this.items){for(var i=0;i<this.items.length;i++)this.items[i].updateDisabled()}}
,isc.A.updateCanEdit=function isc_ContainerItem_updateCanEdit(){this.Super("updateCanEdit",arguments);if(this.items){for(var i=0;i<this.items.length;i++)this.items[i].updateCanEdit()}}
,isc.A.getTextDirection=function isc_ContainerItem_getTextDirection(){return this.form.getTextDirection()}
,isc.A.getLeft=function isc_ContainerItem_getLeft(){var _1=this.$21e();if(_1==null){this.logWarn("getLeft() Unable to determine position for "+(this.name==null?"this item ":this.name)+". Position cannot be determined before the item is drawn "+"- returning zero");return 0}
return this.$30s(_1)}
,isc.A.getTop=function isc_ContainerItem_getTop(){var _1=this.$21e();if(_1==null){this.logWarn("getTop() Unable to determine position for "+(this.name==null?"this item ":this.name)+". Position cannot be determined before the item is drawn "+"- returning zero");return 0}
return this.$30t(_1)}
,isc.A.getVisibleWidth=function isc_ContainerItem_getVisibleWidth(){var _1=this.$21e();if(_1==null)return this.Super("getVisibleWidth",arguments);return _1.offsetWidth}
,isc.A.getVisibleHeight=function isc_ContainerItem_getVisibleHeight(){var _1=this.$21e();if(_1==null)return this.Super("getVisibleHeight",arguments);return _1.offsetHeight}
);isc.B._maxIndex=isc.C+27;isc.ClassFactory.defineClass("CanvasItem","FormItem");isc.A=isc.CanvasItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.width="*";isc.A.height=null;isc.A.shouldSaveValue=false;isc.B.push(isc.A.setElementValue=function isc_CanvasItem_setElementValue(_1,_2){var _3;if(_2===_3){_2=this._value}
this.showValue(_1,_2,this.form,this)}
,isc.A.showValue=function isc_CanvasItem_showValue(_1,_2){}
,isc.A.$308=function isc_CanvasItem__canvas_resized(_1,_2,_3){this.Super("resized",arguments);if(!this.dragResizing()){this.canvasItem.canvasResized(_1,_2,_3)}}
,isc.A.$309=function isc_CanvasItem__canvas_dragResized(){this.canvasItem.canvasResized(1,1);return this.Super("dragResized",arguments)}
,isc.A.$31a=function isc_CanvasItem__canvas_focusInNextTabElement(_1,_2){if(isc.isA.DynamicForm(this)){return this.Super("$l5",arguments)}else
return this.canvasItem.form.$l5(_1,_2)}
,isc.A.$31b=function isc_CanvasItem__canvas_getTabIndexSpan(){if(isc.isA.DynamicForm(this)){return this.Super("getTabIndexSpan",arguments)}
var _1=[];this.canvasItem.$31c(this,_1);var _2=0;for(var i=0;i<_1.length;i++){if(_1[i]==this)_2+=1
else _2+=_1[i].getTabIndexSpan()}
return _2}
);isc.B._maxIndex=isc.C+6;isc.A=isc.CanvasItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.editCriteriaInInnerForm=true;isc.A.applyPromptToCanvas=true;isc.B.push(isc.A.init=function isc_CanvasItem_init(){this.Super("init",arguments);this.$31d(this.form,this)}
,isc.A.isEditable=function isc_CanvasItem_isEditable(){return false}
,isc.A.$mb=function isc_CanvasItem__canFocus(){if(this.canFocus==true)return this.canFocus;var _1=this.canvas;if(_1&&_1.$mb)return _1.$mb();return false}
,isc.A.$31d=function isc_CanvasItem__createCanvas(){if(this.createCanvas!=null){var _1=this.canvas;this.canvas=this.fireCallback("createCanvas");if(this.canvas==null)this.canvas=_1}
if(!isc.isAn.Object(this.canvas)&&!this.canvasProperties&&!window[this.canvas]){if(isc.designTime)return;this.logWarn("CanvasItem: "+(this.getFieldName()?this.getFieldName():this.getID())+" defined with no canvas property - creating a default "+"canvas for this item.")}
if(!isc.isAn.Object(this.canvas)&&isc.isA.Canvas(window[this.canvas])){this.canvas=window[this.canvas]}
var _2={_redrawWithParent:false,$xa:true,tabIndex:-1,canvasItem:this,disabled:this.shouldDisableCanvas()};_2.resized=this.$308;_2.dragResized=this.$309;_2.handleClick=function(){this.Super("handleClick",arguments);if(!isc.isA.StatefulCanvas(this)&&this.canvasItem){this.canvasItem.handleClick()}}
_2.handleActivate=function(){this.Super("handleActivate",arguments);if(this.canvasItem)this.canvasItem.handleClick()}
_2.handleDoubleClick=function(){this.Super("handleDoubleClick",arguments);if(this.canvasItem)this.canvasItem.handleDoubleClick()}
_2.$l5=this.$31a;_2.getTabIndexSpan=this.$31b;if(this.dataSource)_2.dataSource=this.dataSource;if(this.prompt&&this.applyPromptToCanvas)_2.prompt=this.prompt;if(this.overflow!=null)_2.overflow=this.overflow;if(this.accessKey!=null)_2.accessKey=this.accessKey;if(this.showFocused!=null)_2.showFocused=this.showFocused;if(this.showFocusedAsOver!=null)_2.showFocusedAsOver=this.showFocusedAsOver;if(isc.isA.String(this.canvas)&&window[this.canvas])this.canvas=window[this.canvas];if(!isc.isA.Canvas(this.canvas)){isc.addProperties(_2,this.canvas);if(_2.ID==null)_2.ID=null;this.autoDestroy=true;this.addAutoChild("canvas",_2,isc.Canvas,this.containerWidget)}else{this.canvas.setTabIndex(-1);if(this.applyPromptToCanvas)this.canvas.setPrompt(this.prompt);this.canvas.setAccessKey(this.accessKey);isc.addProperties(this.canvas,_2);if(_2.dataSource)this.canvas.bindToDataSource();this.canvas.setDisabled(this.shouldDisableCanvas());this.containerWidget.addChild(this.canvas)}
if(this.containerWidget!=this.form){if(isc.EH.clickMaskUp()){var _3=isc.EH.getAllClickMaskIDs();for(var i=_3.length-1;i>=0;i--){var _5=isc.EH.targetIsMasked(this.containerWidget,_3[i]);if(!_5){isc.EH.addUnmaskedTarget(this.canvas,_3[i]);break}}}}
if(this.canvas)this.observe(this.canvas,"$nb","observer.canvasFocusChanged()");if(isc.isA.DynamicForm(this.canvas)){this.observe(this.canvas,"setFocusItem","observer.nestedFormSetFocusItem()")}}
,isc.A.setCanvas=function isc_CanvasItem_setCanvas(_1){if(isc.isA.Canvas(this.canvas)&&this.canvas!=_1){this.ignore(this.canvas,"$nb");if(isc.isA.DynamicForm(this.canvas))this.ignore(this.canvas,"setFocusItem")}
if(_1)this.canvas=_1;this.$31d()}
,isc.A.redrawn=function isc_CanvasItem_redrawn(_1){this.Super("redrawn",arguments);var _2=this.shouldDisableCanvas();if(this.canvas.isDisabled()!=_2)this.canvas.setDisabled(_2)}
,isc.A.destroy=function isc_CanvasItem_destroy(){if(this.canvas){delete this.canvas.canvasItem;if(this.autoDestroy)this.canvas.destroy(true);else if(this.canvas.visibility!=isc.Canvas.HIDDEN)this.canvas.hide()}
return this.Super("destroy",arguments)}
,isc.A.placeCanvas=function isc_CanvasItem_placeCanvas(_1){var _2=this.canvas;if(this.visible==false){_2.hide();_2.moveTo(0,0);return}
if(this.form&&!this.form.isDrawn()&&this.form.position==isc.Canvas.RELATIVE){_2.hide();return}
var _3=this.containerWidget.getClipHandle(),_4=isc.Element.get(this.getID()+"$31e");var _5=isc.Element.getLeftOffset(_4,_3),_6=isc.Element.getTopOffset(_4,_3);_2.moveTo(_5,_6);if(_2.visibility==isc.Canvas.HIDDEN){_2.show()}
if(!_1&&isc.Browser.isMac&&isc.Browser.isMoz&&_5==0&&_6==0){isc.Timer.setTimeout({target:this,methodName:"$31f"},0)}}
,isc.A.$31f=function isc_CanvasItem__delayedPlaceCanvas(){this.placeCanvas(true)}
,isc.A.cleared=function isc_CanvasItem_cleared(){this.Super("cleared",arguments);if(this.canvas&&this.canvas.isDrawn())this.canvas.clear()}
,isc.A.moved=function isc_CanvasItem_moved(){if(this.isDrawn())this.placeCanvas()}
,isc.A.checkCanvasOverflow=function isc_CanvasItem_checkCanvasOverflow(){return this.sizeCanvas(true)}
,isc.A.sizeCanvas=function isc_CanvasItem_sizeCanvas(_1){var _2=this.canvas;if(_1&&!(_2.overflow==isc.Canvas.VISIBLE||_2.overflow==isc.Canvas.CLIP_H))
{this.logDebug("ignoring first pass, can't overflow","canvasItemSizing");return}
var _3=this.getInnerWidth(),_4=this.getInnerHeight(),_5,_6;if(this.showTitle&&this.getTitleOrientation()==isc.Canvas.TOP){_4-=this.form.getTitleHeight(this)}
_6=_4;var _7=_2.$ts||this.width;_5=(_7==null||_7=="*"?_3:_7);if(!_2.isDirty()&&(_5==null||_5<=_2.getVisibleWidth())&&_2.getHeight()<_2.getVisibleHeight()&&_6<=_2.getVisibleHeight())
{this.logDebug("not applying height: "+_6+" to overflowed Canvas with height: "+_2.getVisibleHeight(),"canvasItemSizing");_6=null}
if(!isc.isA.Number(_5))_5=null;if(!isc.isA.Number(_6))_6=null;this.$31g(_5,_6);this.logDebug("this.$172: "+this.$172+", policy size: "+[_3,_4]+", specifiedSize: "+[_7,_2.$tt||this.height]+", Resized Canvas to: "+[_5,_6],"canvasItemSizing");if(!_2.isDrawn()){var _8=this.containerWidget.getDrawnState();if(_8==isc.Canvas.COMPLETE||_8==isc.Canvas.HANDLE_DRAWN){_2.draw()}}else _2.redrawIfDirty("CanvasItem getting new size");var _9=_2.getVisibleWidth(),_10=_2.getVisibleHeight();this.logDebug("visible size of embedded Canvas: "+[_9,_10],"canvasItemSizing");if(!_1)this.minHeight=null;else this.minHeight=_10>_2.getHeight()?_10:null;if(_10>_4)return true}
,isc.A.$31g=function isc_CanvasItem__setCanvasSize(_1,_2){this.$31h=true;this.canvas.resizeTo(_1,_2);this.$31h=false}
,isc.A.getElementHTML=function isc_CanvasItem_getElementHTML(_1){var _2=this.canvas;if(_2&&this.$257()){return _2.getPrintHTML(this.containerWidget.currentPrintProperties)}
this.sizeCanvas();this.$296(this.getGlobalTabIndex());return"<SPAN style='padding:0px;margin:0px;' ID='"+this.getID()+"$31e'>"+isc.Canvas.spacerHTML(_2.getVisibleWidth(),_2.getVisibleHeight())+"</SPAN>"}
,isc.A.$27f=function isc_CanvasItem__applyHandlersToElement(){this.$27p()}
,isc.A.getHeight=function isc_CanvasItem_getHeight(_1){var _2=this.canvas;if(_1){var _3=_2.getVisibleHeight();if(_3>_2.getHeight())return _3}
return this.canvas.$tt||this.height||this.canvas.defaultHeight}
,isc.A.getWidth=function isc_CanvasItem_getWidth(){return this.canvas.$ts||this.width||this.canvas.defaultWidth}
,isc.A.canvasResized=function isc_CanvasItem_canvasResized(_1,_2,_3){if(this.$31h)return;var _4=this.canvas,_5=_4.getWidth(),_6=_4.getHeight();if(!_4.isDrawn())return;if(_3!="overflow"){if(_1!=null&&_1!=0)_4.$ts=_5;if(_2!=null&&_2!=0)_4.$tt=_6}
this.logDebug("canvas resized: new specified sizes: "+[_5,_6],"canvasItemSizing");this.redraw()}
,isc.A.shouldDisableCanvas=function isc_CanvasItem_shouldDisableCanvas(){return this.disabled==true||this.isReadOnly()}
,isc.A.updateDisabled=function isc_CanvasItem_updateDisabled(){this.Super("updateDisabled",arguments);this.canvas.setDisabled(this.shouldDisableCanvas())}
,isc.A.updateCanEdit=function isc_CanvasItem_updateCanEdit(){this.Super("updateCanEdit",arguments);this.canvas.setDisabled(this.shouldDisableCanvas())}
,isc.A.setElementReadOnly=function isc_CanvasItem_setElementReadOnly(){this.$299(!this.isReadOnly()&&!this.isDisabled())}
,isc.A.$296=function isc_CanvasItem__setElementTabIndex(_1){this.$31i(_1)}
,isc.A.$31c=function isc_CanvasItem__getCanvasTabDescendents(_1,_2){if(_1.canvasItem!=null&&_1.canvasItem!=this)return;_2.add(_1);var _3=_1.members||_1.children||{};for(var i=0;i<_3.length;i++){this.$31c(_3[i],_2)}}
,isc.A.$31i=function isc_CanvasItem__setCanvasTabIndex(_1){var _2=this.canvas,_3=[];if(_2)this.$31c(_2,_3);for(var i=0;i<_3.length;i++){_2=_3[i];_2.$v7();_2.$0m(_1,false);_1+=_2==this.canvas?1:_2.getTabIndexSpan()}}
,isc.A.focusInItem=function isc_CanvasItem_focusInItem(_1){if(this.canvas){var _2=[],_3;this.$31c(this,_2);var _4=_1==false?_2.length-1:0,_5=_1==false?0:_2.length-1,_6=_1==false?-1:1;for(var i=_4;_1?i>=_5:i<=_5;i+=_6){if(_2[i].$mb()){_3=_2[i];break}}
if(_3){if(_1!=null){this.canvas.focusAtEnd(_1)}else{this.canvas.focus()}}}
return this.Super("focusInItem",arguments)}
,isc.A.blurItem=function isc_CanvasItem_blurItem(){if(this.canvas)this.canvas.blur();return this.Super("blurItem",arguments)}
,isc.A.canvasFocusChanged=function isc_CanvasItem_canvasFocusChanged(){if(this.canvas.hasFocus){this.form.setFocusItem(this);this.elementFocus()}else this.elementBlur()}
,isc.A.nestedFormSetFocusItem=function isc_CanvasItem_nestedFormSetFocusItem(){this.form.setFocusItem(this)}
,isc.A.hasAdvancedCriteria=function isc_CanvasItem_hasAdvancedCriteria(){if(this.editCriteriaInInnerForm&&isc.isA.DynamicForm(this.canvas))return true;return this.Super("hasAdvancedCriteria",arguments)}
,isc.A.canEditCriterion=function isc_CanvasItem_canEditCriterion(_1){if(this.editCriteriaInInnerForm&&isc.isA.DynamicForm(this.canvas)){if(_1.operator!=this.canvas.operator)return false;for(var i=0;i<_1.criteria;i++){var _3=this.canvas.getItems(),_4;for(var _5=0;_5<_3.length;_5++){if(_3[_5].canEditCriterion(_1.criteria[i])){_4=true;break}}
if(!_4)return false}
return true}
return this.Super("canEditCriterion",arguments)}
,isc.A.getCriterion=function isc_CanvasItem_getCriterion(){if(this.editCriteriaInInnerForm&&isc.isA.DynamicForm(this.canvas)){return this.canvas.getValuesAsAdvancedCriteria()}else return this.Super("getCriterion",arguments)}
,isc.A.setCriterion=function isc_CanvasItem_setCriterion(_1){if(this.editCriteriaInInnerForm&&isc.isA.DynamicForm(this.canvas)){this.canvas.setValuesAsCriteria(_1,true)}else return this.Super("setCriterion",arguments)}
,isc.A.setPrompt=function isc_CanvasItem_setPrompt(_1){this.Super("setPrompt",_1);if(this.applyPromptToCanvas&&this.canvas){if(isc.isA.Canvas(this.canavs))this.canvas.setPrompt(_1);else this.canvas.prompt=_1}}
);isc.B._maxIndex=isc.C+35;isc.CanvasItem.registerStringMethods({createCanvas:"form,item",showValue:"displayValue,dataValue,form,item"});isc.ClassFactory.defineClass("TextItem","FormItem");isc.A=isc.TextItem;isc.A.DEFAULT="default";isc.A.UPPER="upper";isc.A.LOWER="lower";isc.A.$31j={'0':{charFilter:"[0-9+\\-]"},'#':{charFilter:"[0-9]"},'9':{charFilter:"[0-9 ]"},'L':{charFilter:"[A-Za-z]"},'?':{charFilter:"[A-Za-z ]"},'a':{charFilter:"[0-9A-Za-z]"},'A':{charFilter:"[0-9A-Za-z]"},'C':{charFilter:"."}};isc.A=isc.TextItem.getPrototype();isc.A.width=150;isc.A.height=isc.Browser.isSafari?22:19;isc.A.textBoxStyle="textItem";isc.A.length=null;isc.A.canAutoComplete=true;isc.A.$31k="TEXT";isc.A.$23x=true;isc.A.$26l=true;isc.A.emptyStringValue=null;isc.A.redrawOnShowIcon=false;isc.A.clipValue=true;isc.A.$27o={onmousedown:(isc.Browser.isIE?function(){var _1=this,_2=isc.DynamicForm.$ot(_1),_3=_2.item;if(_3)_3.$30f()}:null)};isc.A.printFullText=false;isc.A=isc.TextItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$31l=[,"<INPUT TYPE=",," NAME='",,"' ID='",,"' handleNativeEvents=false"];isc.A.$248=" TABINDEX=";isc.A.$r8=">";isc.A.$tm=" DISABLED ";isc.A.$31m="native";isc.A.$31n=" AUTOCOMPLETE=OFF ";isc.A.$247=" ACCESSKEY=";isc.A.$31o=[" CLASS='",,"' STYLE='",,,,,,,,,,,,,,,,(isc.Browser.isMoz?"-moz-user-focus:":null),,"' "];isc.A.$19h="WIDTH:";isc.A.$11i="px;";isc.A.$19g="HEIGHT:";isc.A.$245="text-align:";isc.A.$px=";";isc.A.$31p="normal;";isc.A.$31q="ignore;";isc.A.$31r="margin-top:-1px;margin-bottom:-1px;";isc.A.applyStaticTypeFormat=false;isc.A.characterCasing=isc.TextItem.DEFAULT;isc.A.formatOnFocusChange=false;isc.A.maskPadChar=" ";isc.A.maskPromptChar="_";isc.B.push(isc.A.$31s=function isc_TextItem__handleCutPaste(){if(this.changeOnKeypress)this.$30j()}
,isc.A.$21v=function isc_TextItem__willHandleInput(){return!isc.Browser.isIE}
,isc.A.getTextBoxCellCSS=function isc_TextItem_getTextBoxCellCSS(){return this.$24z}
,isc.A.setElementReadOnly=function isc_TextItem_setElementReadOnly(_1){this.$29m(_1)}
,isc.A.getElementHTML=function isc_TextItem_getElementHTML(_1,_2){var _3=this.$2n(_2);if(this.showValueIconOnly)return _3;var _4;if(this.$257()&&this.printFullText){_4=isc.StringBuffer.concat("<SPAN ",this.getElementStyleHTML(),">",_2==null?"&nbsp;":_2.asHTML(),"</SPAN>")}else{var _5=this.$31l,_6=this.form,_7=_6.getID(),_8=this.getItemID();_5[0]=_3;_5[2]=this.$31k;_5[4]=this.getElementName();_5[6]=this.getDataElementId();_5[8]=this.$27a();if(isc.Browser.isMoz||isc.Browser.isSafari){if(this.getBrowserSpellCheck())_5[_5.length]=" spellcheck=true";else _5[_5.length]=" spellcheck=false"}
if(isc.Browser.isSafari){if(this.browserAutoCapitalize==false){_5[_5.length]=" autocapitalize=off"}
if(this.browserAutoCorrect==false){_5[_5.length]=" autocorrect=off"}
if(this.browserInputType!=null){var _9=this.browserInputTypeMap[this.browserInputType];if(_9==null)_9=this.browserInputType;_5[_5.length]=" type='"+_9+"'"}}
if(this.$21v){_5[_5.length]=" ONINPUT='"
_5[_5.length]=this.getID()
_5[_5.length]=".$30i()'"}
if(this.isDisabled()||(this.$31k=="FILE"&&this.isReadOnly())){_5[_5.length]=this.$tm}
if(this.isInactiveHTML()||this.isReadOnly()){_5[_5.length]=" READONLY=TRUE"}
if(this.isInactiveHTML()&&_1!=null&&_1!=isc.emptyString){_5[_5.length]=" value='"+isc.makeXMLSafe(_1)+"'"}
if(this.$28z()!=this.$31m){_5[_5.length]=this.$31n}
_5[_5.length]=this.getElementStyleHTML();var _10=this.$269();if(_10!=null){var _11=_5.length;_5[_11]=this.$248;isc.$bp(_5,_10,_11+1,5)}
if(this.showTitle==false&&this.accessKey!=null){_5[_5.length]=this.$247;_5[_5.length]=this.accessKey}
_5[_5.length]=this.$r8;_4=_5.join(isc.emptyString);_5.length=8}
return _4}
,isc.A.$252=function isc_TextItem__sizeTextBoxAsContentBox(){return isc.Browser.isStrict}
,isc.A.$23k=function isc_TextItem__nativeElementBlur(_1,_2){if(this.form&&!this.form.$20f){this.form.elementChanged(this)}
var _3=this.Super("$23k",arguments);if(this.formatOnFocusChange||this.mask!=null||this.$31t==null||this.$31t!=this.getEnteredValue())
{this.refreshDisplayValue()}
if(this.showHintInField){var _4;var _5=this.getElementValue();if(_5===_4||_5==null||isc.is.emptyString(_5)){this.$29c()}}
if(this.$31u!=null){isc.Timer.clear(this.$31u);this.$31u=null}
return _3}
,isc.A.refreshDisplayValue=function isc_TextItem_refreshDisplayValue(){var _1=this.getValue();if(this.mapValueToDisplay){_1=this.mapValueToDisplay(_1)}
if(!this.hasFocus&&this.showHintInField&&(_1==null||_1=="")){this.$29c()}else{this.setElementValue(_1)}}
,isc.A.getElementStyleHTML=function isc_TextItem_getElementStyleHTML(){var _1=this.$257()&&this.printFullText;var _2=this.$31o,_3=this.getTextBoxWidth(),_4=this.getTextBoxHeight(),_5=this.getTextBoxStyle();_2[1]=_5;if(isc.isA.Number(_3)){_2[3]=this.$19h;isc.$bp(_2,_3,4,4);_2[8]=this.$11i}else{_2[3]=_2[4]=_2[5]=_2[6]=_2[7]=_2[8]=null}
if(isc.isA.Number(_4)){_2[9]=this.$19g;isc.$bp(_2,_4,10,4);_2[14]=this.$11i}else{_2[9]=_2[10]=_2[11]=_2[12]=_2[13]=_2[14]=null}
if(this.textAlign){_2[15]=this.$245;_2[16]=this.textAlign;_2[17]=this.$px}else{_2[15]=_2[16]=_2[17]=null}
if(isc.Browser.isIE){_2[18]=_1?null:this.$31r}
if(isc.Browser.isMoz&&!_1){_2[19]=(this.$269()>0?this.$31p:this.$31q)}
return _2.join(isc.emptyString)}
,isc.A.getEnteredValue=function isc_TextItem_getEnteredValue(){return this.getElementValue()}
,isc.A.mapValueToDisplay=function isc_TextItem_mapValueToDisplay(_1){if(this.mask){var x=this.$31v();if(!this.hasFocus)
x=this.$31w(_1);return x}
var _3=isc.FormItem.$ce.mapValueToDisplay.call(this,_1);if(_3==null)return isc.emptyString;return _3}
,isc.A.mapDisplayToValue=function isc_TextItem_mapDisplayToValue(_1){var _2;if(this.mask){_2=this.$31x(_1)}else{_2=this.$28u(_1)}
_2=this.$28t(_2);if(isc.is.emptyString(_2))_2=this.emptyStringValue;return _2}
,isc.A.saveValue=function isc_TextItem_saveValue(_1,_2){if(this.mask)this.$31w(_1);this.Super("saveValue",arguments)}
,isc.A.setValue=function isc_TextItem_setValue(_1,_2,_3,_4){this.$29g();var _5;if(_1!==_5&&(_1==null||isc.is.emptyString(_1)))
this.emptyStringValue=_1;if(_1!==_5&&_1!=null&&this.characterCasing!=isc.TextItem.DEFAULT){if(this.characterCasing==isc.TextItem.UPPER){_1=_1.toUpperCase()}else if(this.characterCasing==isc.TextItem.LOWER){_1=_1.toLowerCase()}}
_1=this.invokeSuper(isc.TextItem,"setValue",_1,_2,_3,_4);if(!this.hasFocus&&this.showHint&&this.showHintInField&&this.getHint()){if(_1===_5||_1==null||isc.is.emptyString(_1)){this.$29c()}}
return _1}
,isc.A.getCriteriaFieldName=function isc_TextItem_getCriteriaFieldName(){if(this.criteriaField)return this.criteriaField;if(this.displayField)return this.displayField;return this.Super("getCriteriaFieldName",arguments)}
,isc.A.$31y=function isc_TextItem__shouldSelectOnFocus(){var _1=this.selectOnFocus;if(_1==null&&this.form)_1=this.form.selectOnFocus;if(_1){var _2=isc.EH.isMouseEvent();if(_2)_1=false}
return _1}
,isc.A.$23h=function isc_TextItem__nativeElementFocus(_1,_2){var _3=this.Super("$23h",arguments);this.$29g();if(this.formatOnFocusChange)this.refreshDisplayValue()
if(this.mask){this.$31z(false);var _4=0;var _5=this.$310;var _6=this.$31y();if(!_6){_4=this.$311();_5=_4}
this.$31u=this.delayCall("$312",[_4,_5])}else{var _6=this.$31y();var _7=this.getEnteredValue();if(_6){if(_7!=null){this.$31u=this.delayCall("$313")}}
this.$31t=_7}
return _3}
,isc.A.$312=function isc_TextItem__delayed_setSelection(_1,_2){this.$31u=null;if(!this.$29p())return;this.$314(_1,_2)}
,isc.A.$313=function isc_TextItem__delayed_selectValue(){this.$31u=null;if(!this.$29p())return;this.selectValue()}
,isc.A.setKeyPressFilter=function isc_TextItem_setKeyPressFilter(_1){if(this.mask){this.logWarn("setKeyPressFilter() ignored because mask is enabled");return}
this.keyPressFilter=_1;this.$315=null;if(this.keyPressFilter){this.$315=new RegExp(this.keyPressFilter)}}
,isc.A.init=function isc_TextItem_init(){this.Super("init",arguments);if(this.mask){if((isc.ComboBoxItem&&isc.isA.ComboBoxItem(this))||(isc.SpinnerItem&&isc.isA.SpinnerItem(this)))
{this.logWarn("item.mask is unsupported for this FormItem type. "+"This item has mask specified as '"+this.mask+"' - ignoring.");this.mask=null}else{this.$316();if(this.keyPressFilter){this.logWarn("init: keyPressFilter ignored because mask is enabled")}}}else if(this.keyPressFilter){this.$315=new RegExp(this.keyPressFilter)}}
,isc.A.handleKeyPress=function isc_TextItem_handleKeyPress(_1,_2){if(this.Super("handleKeyPress",arguments)==false)
return false;if(this.isReadOnly())return true;if(isc.EventHandler.ctrlKeyDown()||isc.EventHandler.altKeyDown())return true;if((!this.characterCasing||this.characterCasing==isc.TextItem.DEFAULT)&&!this.$315&&!this.mask)
{return true}
var _3=_1.keyName,_4=_1.characterValue;if(this.mask){var _5=this.$317();var _6=isc.Browser.isSafari;var _7=_5.begin;if(_3=="Backspace"||_3=="Delete"){if((_5.begin-_5.end)!=0||(_6&&this.$318))
{if(isc.Browser.isSafari&&this.$318){_5=this.$318;this.$318=null}
if(this.maskOverwriteMode){this.$319(_5.begin,_5.end)}else{var _8=_5.end-_5.begin;this.$32a(_5.begin,_8)}
this.$31z(true);this.$32b(_5.begin,0)}else{if(_3=="Backspace"){var _9=((_6&&_5.begin==_5.end)?_7:_7-1);if(_9>=0){if(this.maskOverwriteMode){while(!this.$32c[_9]&&_9>=0)_9--;this.$32d[_9]=this.maskPromptChar}else{this.$32a(_9)}
this.$31z(true);this.$32b(_9,-1)}}else{if(this.maskOverwriteMode){if(_7==this.$32e(_7-1)){this.$32d[_7]=this.maskPromptChar}}else{this.$32a(_7)}
this.$31z(true);this.$32b(_7,0)}}
return false}
if(this.$32f(_4)&&((_5.begin-_5.end)!=0||(_6&&this.$318)))
{if(isc.Browser.isSafari&&this.$318){_5=this.$318;this.$318=null}
if(this.maskOverwriteMode){this.$319(_5.begin,_5.end)}else{var _8=_5.end-_5.begin;this.$32a(_5.begin,_8)}}
if(_6&&(_5.begin-_5.end)!=0&&!this.$32f(_4))
{this.$318=_5}else{this.$318=null}
if(_3=="Escape"){this.$319(0,this.$310)
this.$31z(true);this.$314(this.$32g);return false}}
if((this.mask&&!this.$32f(_4))||(!this.mask&&((!this.$315&&!this.$32h(_4))||(this.$315&&!this.$32f(_4)))))
{return true}
var c=String.fromCharCode(_4);if(this.mask){var p=this.$32e(_7-1);if(p<this.$310){var _12=this.$32c[p];if(_12){if(_12.casing){c=this.$32i(c,_12.casing)}
if(_12.filter.test(c)){if(!this.maskOverwriteMode)this.$32j(p);this.$32d[p]=c;var _13=p;if(this.$31z(true)){_13=this.$32e(p)}
this.$314(_13)}}}
return false}
var _14=c;if(!this.mask)_14=this.$32i(c,this.characterCasing);if(c==_14&&!this.$315)return true;if(this.$315){if(this.$32f(_4)&&!this.$315.test(_14)){return false}}
if(c==_14)return true;var _15=this.getValue()||"";var _5=this.getSelectionRange();if((_5[0]-_5[1])!=0){_15=_15.substring(0,_5[0])+_14+_15.substring(_5[1]+1)}else{_15=_15.substring(0,_5[0])+_14+_15.substring(_5[1])}
this.setValue(_15);this.setSelectionRange(_5[0]+1,_5[0]+1);return false}
,isc.A.$32f=function isc_TextItem__isTypableCharacter(_1){return((_1>=32&&_1<=126)||_1>127)}
,isc.A.$32h=function isc_TextItem__isAlphaCharacter(_1){return(_1>=65&&_1<=90)||(_1>=97&&_1<=122)}
,isc.A.$32i=function isc_TextItem__mapCharacterCase(_1,_2){if(_2==isc.TextItem.UPPER){_1=_1.toUpperCase()}else if(_2==isc.TextItem.LOWER){_1=_1.toLowerCase()}
return _1}
,isc.A.setMask=function isc_TextItem_setMask(_1){if(isc.isA.ComboBoxItem(this)||isc.isA.SpinnerItem(this)){return}
this.mask=_1;this.$316();if(this.keyPressFilter){this.$315=null;this.logWarn("setMask: keyPressFilter ignored because mask is enabled")}
this.setValue("")}
,isc.A.$316=function isc_TextItem__parseMask(){this.$32c=[];this.$32d=[];this.$310=0;var _1=null;var _2=false;var _3=false;var _4="";var _5=this.mask.split("");for(var i=0;i<_5.length;i++){var c=_5[i];if(c=="<"){_1=(_1==isc.TextItem.LOWER?null:isc.TextItem.LOWER)}else if(c==">"){_1=(_1==isc.TextItem.UPPER?null:isc.TextItem.UPPER)}else{if(!_2&&c=="\\"){_2=true}else if(_2){this.$32k(c,_1);_2=false}else{if(!_3&&c=="["){_3=true;_4+=c}else if(_3&&c=="]"){_3=false;_4+=c;this.$32c.push({filter:new RegExp(_4),casing:_1});if(this.$32g==null){this.$32g=this.$32c.length-1}
this.$32d.push(this.maskPromptChar);this.$310++;_4=""}else if(_3){_4+=c}else{this.$32l(c,_1)}}}}}
,isc.A.$32k=function isc_TextItem__addLiteralToMask(_1,_2){this.$32c.push(null);this.$32d.push(_1);this.$310++}
,isc.A.$32l=function isc_TextItem__addUnknownToMask(_1,_2){var _3=isc.TextItem.$31j[_1];if(_3){this.$32c.push({filter:new RegExp(_3.charFilter),casing:_2});if(this.$32g==null){this.$32g=this.$32c.length-1}
this.$32d.push(this.maskPromptChar)}else{this.$32c.push(null);this.$32d.push(_1)}
this.$310++}
,isc.A.$317=function isc_TextItem__getSelection(){var _1=this.getSelectionRange();if(_1==null)_1=[0,0];return{begin:_1[0],end:_1[1]}}
,isc.A.$314=function isc_TextItem__setSelection(_1,_2){if(this.hasFocus){_2=(isc.isA.Number(_2)?_2:_1);this.setSelectionRange(_1,_2)}}
,isc.A.$32e=function isc_TextItem__getNextEntryPosition(_1){while(++_1<this.$310){if(this.$32c[_1])return _1}
return this.$310}
,isc.A.$311=function isc_TextItem__getEndPosition(){var _1=0;for(var i=this.$310-1;i>=0;i--){if(this.$32c[i]){if(this.$32d[i]==this.maskPromptChar)
_1=i;else
break}}
return _1}
,isc.A.$31w=function isc_TextItem__maskValue(_1){if(_1==null)_1="";if(!isc.isA.String(_1))_1=_1.toString();this.$319(0,this.$310);var _2=-1;if(this.maskSaveLiterals){for(var i=0,_4=0;i<_1.length;i++){if(this.$32c[i]){var c=_1.charAt(i);if(c==" "){if(!this.hasFocus)
this.$32d[i]=c}else if(this.$32c[i].filter.test(c)){this.$32d[i]=c;_2=i}}}}else{for(var i=0,_4=0;i<this.$310;i++){if(this.$32c[i]){while(_4<_1.length){var c=_1.charAt(_4++);if(c==" "){if(!this.hasFocus)this.$32d[i]=c;break}else if(this.$32c[i].filter.test(c)){this.$32d[i]=c;_2=i;break}}
if(_4>_1.length)break}}}
_1=this.$31v();if(!this.hasFocus){if(_2>=0){for(var i=_2+1;i<this.$310;i++){if(this.$32c[i])break;_2++}}
_1=_1.substring(0,_2+1)}
return _1}
,isc.A.$31x=function isc_TextItem__unmaskValue(_1){if(_1==null)_1="";var _2=false;var _3=-1;var _4="";for(var i=0,_6=0;i<_1.length;i++){var c=_1.charAt(i);if(this.$32c[i]){if(c!=this.maskPromptChar&&this.$32c[i].filter.test(c)){_4+=c;_2=true;_3=_6++}else{_4+=this.maskPadChar;_6++}}else if(this.maskSaveLiterals){_4+=c;_3=_6++}}
if(!_2){_4=""}else{_4=_4.substring(0,_3+1)}
return _4}
,isc.A.$31v=function isc_TextItem__getMaskBuffer(){if(this.$32d==null)return"";return this.$32d.join('')}
,isc.A.$319=function isc_TextItem__clearMaskBuffer(_1,_2){for(var i=_1;i<_2&&i<this.$310;i++){if(this.$32c[i])this.$32d[i]=this.maskPromptChar}}
,isc.A.$31z=function isc_TextItem__saveMaskBuffer(_1){var _2=this.$31v();this.setElementValue(_2);if(_1&&this.changeOnKeypress){var _3=this.$31x(_2);var _4=this.handleChange(_3,this._value);if(this.destroyed)return;_3=this.$29k;this.updateAppearance(_3);this.saveValue(_3);this.handleChanged(_3);return _4}
return true}
,isc.A.$32b=function isc_TextItem__positionCaret(_1,_2){if(_2<0){while(!this.$32c[_1]&&_1>=0)_1--}else{while(!this.$32c[_1]&&_1<this.$310)_1++}
this.$314(_1)}
,isc.A.$32a=function isc_TextItem__shiftMaskBufferLeft(_1,_2){if(!_2)_2=1;while(!this.$32c[_1]&&_1>=0)_1--;for(var i=_1,_4=i+_2-1;i<this.$310;i++,_4=j){if(this.$32c[i]){this.$32d[i]=this.maskPromptChar;var j=this.$32e(_4++);var _6=this.$32c[i];var c=this.$32d[j];if(j<this.$310&&_6.filter.test(c)){if(_6.casing){c=this.$32i(c,_6.casing)}
this.$32d[i]=c}else{while(i<j){if(this.$32c[i])this.$32d[i]=this.maskPromptChar;i++}
break}}}}
,isc.A.$32j=function isc_TextItem__shiftMaskBufferRight(_1){for(var i=_1,c=this.maskPromptChar;i<this.$310;i++){var _4=this.$32c[i];if(_4){if(_4.casing){c=this.$32i(c,_4.casing)}
var j=this.$32e(i);var t=this.$32d[i];this.$32d[i]=c;if(j<this.$310&&this.$32c[j].filter.test(t)){c=t}else{break}}}}
);isc.B._maxIndex=isc.C+41;isc.ClassFactory.defineClass("BlurbItem","FormItem");isc.A=isc.BlurbItem.getPrototype();isc.A.shouldSaveValue=false;isc.A.height=null;isc.A.showTitle=false;isc.A.colSpan="*";isc.A.startRow=true;isc.A.endRow=true;isc.A.textBoxStyle="staticTextItem";isc.A.emptyDisplayValue="&nbsp;";isc.ClassFactory.defineClass("ButtonItem","CanvasItem");isc.A=isc.ButtonItem.getPrototype();isc.A.canFocus=true;isc.A.shouldSaveValue=false;isc.A.height=null;isc.A.width=null;isc.A.titleStyle=null;isc.A.showTitle=false;isc.A.startRow=true;isc.A.endRow=true;isc.A.buttonConstructor=isc.Button;isc.A.autoFit=true;isc.A.buttonDefaults={getTitle:function(){return this.canvasItem.getTitle()}};isc.A.autoDestroy=true;isc.A=isc.ButtonItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$32m={width:true,height:true,icon:true};isc.B.push(isc.A.getTitleHTML=function isc_ButtonItem_getTitleHTML(){return this.getTitle()}
,isc.A.setTitle=function isc_ButtonItem_setTitle(_1){this.title=_1;if(this.canvas)this.canvas.setTitle(_1)}
,isc.A.$31d=function isc_ButtonItem__createCanvas(){var _1={canFocus:this.$mb(),disabled:this.isDisabled(),width:this.width};if(this.height!=null)_1.height=this.height;if(this.icon)_1.icon=this.icon;if(this.titleStyle)_1.titleStyle=this.titleStyle;if(this.baseStyle)_1.baseStyle=this.baseStyle;if(this.autoFit!=null)_1.autoFit=this.autoFit;this.canvas=this.createAutoChild("button",_1,this.buttonConstructor);this.Super("$31d",arguments)}
,isc.A.$31g=function isc_ButtonItem__setCanvasSize(_1,_2,_3,_4){if(_1==null&&_2==null)return;return this.invokeSuper(isc.ButtonItem,"$31g",_1,_2,_3,_4)}
,isc.A.propertyChanged=function isc_ButtonItem_propertyChanged(_1,_2){if(this.canvas!=null&&this.$32m[_1]){this.canvas.setProperty(_1,_2)}}
,isc.A.handleClick=function isc_ButtonItem_handleClick(){if(this.editingOn){if(isc.VisualBuilder&&isc.VisualBuilder.titleEditEvent=="click")this.editClick();return false}
return this.Super("handleClick",arguments)}
,isc.A.handleDoubleClick=function isc_ButtonItem_handleDoubleClick(){if(this.editingOn){if(isc.VisualBuilder&&isc.VisualBuilder.titleEditEvent=="doubleClick")this.editClick();return false}
return this.Super("handleDoubleClick",arguments)}
,isc.A.$29n=function isc_ButtonItem__shouldAllowExpressions(){return false}
);isc.B._maxIndex=isc.C+8;if(isc.ListGrid){isc.ClassFactory.defineInterface("PickList");isc.ClassFactory.defineClass("PickListMenu","ScrollingMenu");isc.A=isc.PickListMenu;isc.A.$32n={};isc.A.pickListCacheLimit=50;isc.A=isc.PickListMenu.getPrototype();isc.A.useAllDataSourceFields=false;isc.A.tabIndex=-1;isc.A.canResizeFields=false;isc.A.canFreezeFields=false;isc.A.styleName="pickListMenu";isc.A.bodyStyleName="pickListMenuBody";isc.A.normalCellHeight=16;isc.A=isc.PickListMenu.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$32o="background-color:";isc.A.$32p="color:";isc.B.push(isc.A.getValueIcon=function isc_PickListMenu_getValueIcon(_1,_2,_3){var _4=this.formItem;var _5=_4&&!_4.suppressValueIcons&&(_4.valueIcons!=null||_4.getValueIcon!=null);if(_5){var _6=_4.getValueFieldName(),_7=_4.valueIconField||_4.getDisplayFieldName()||_6;if(this.getFieldName(_1)==_7){return _4.$253(_3[_6])}}
return this.Super("getValueIcon",arguments)}
,isc.A.recordClick=function isc_PickListMenu_recordClick(_1,_2,_3,_4,_5,_6,_7){if(!this.allowMultiSelect)this.hide();if(_2!=null)this.itemClick(_2)}
,isc.A.headerClick=function isc_PickListMenu_headerClick(_1,_2){var _3=this.Super("headerClick",arguments);var _4=this.getField(_1);if(this.isCheckboxField(_4)&&this.allowMultiSelect){this.multiSelectChanged()}
return _3}
,isc.A.multiSelectChanged=function isc_PickListMenu_multiSelectChanged(){var _1=this.formItem,_2=_1.getValueFieldName(),_3=this.getSelection(),_4=true,_5=[];for(var i=0;i<_3.length;i++){_4=false;var _7=_3[i];_5.add(_7[_2])}
_1.pickValue(_4?null:_5)}
,isc.A.itemClick=function isc_PickListMenu_itemClick(_1){if(this.allowMultiSelect){this.multiSelectChanged()}else{var _2=this.formItem,_3=_2.getValueFieldName();var _4=_1[_3];_2.pickValue(_4)}}
,isc.A.hide=function isc_PickListMenu_hide(_1,_2,_3,_4){var _5=this.isVisible()&&this.isDrawn();this.invokeSuper(isc.PickListMenu,"hide",_1,_2,_3,_4);if(!this.formItem)return;if(_5&&this.showModal)this.formItem.focusInItem();this.formItem.$32q=null;if(_5)this.formItem.$32r();delete this.formItem.$32s}
,isc.A.show=function isc_PickListMenu_show(){var _1=this.isVisible()&&this.isDrawn();this.bringToFront();this.Super("show",arguments);if(!_1)this.formItem.$32t()}
,isc.A.showClickMask=function isc_PickListMenu_showClickMask(){if(!this.clickMaskUp(this.getID())){var _1=this.Super("showClickMask",arguments);if(this.formItem){var _2=this.formItem.form,_3=isc.EH.clickMaskRegistry.find("ID",_1);if(_3.$ne)_3.$ne=null}}}
,isc.A.getCellCSSText=function isc_PickListMenu_getCellCSSText(_1,_2,_3){if(_1==this.selection.getSelectedRecord()){var _4=[];if(this.hiliteColor!=null)
_4[0]=this.$32o
_4[1]=this.hiliteColor
_4[2]=isc.$32u;if(this.hiliteTextColor!=null)
_4[3]=this.$32p;_4[4]=this.hiliteTextColor;_4[5]=isc.semi;return _4.join(isc.emptyString)}}
,isc.A.keyDown=function isc_PickListMenu_keyDown(){var _1=isc.EH.lastEvent.keyName;if(_1=="Tab"){this.hide();return false}}
,isc.A.$32v=function isc_PickListMenu__formatCellValue(_1,_2,_3,_4,_5){if(this.formItem==null)return this.Super("$32v",arguments);var _6=this.getFieldName(_5);_1=this.formItem.formatPickListValue(_1,_6,_2);return this.Super("$32v",[_1,_2,_3,_4,_5])}
,isc.A.bodyKeyPress=function isc_PickListMenu_bodyKeyPress(_1,_2){var _3=isc.EH.lastEvent.keyName;if(isc.Browser.isSafari){if(_3=="Tab"){this.hide();return false}}
var _4=isc.EH.getKeyEventCharacterValue();if(_4!=null){var _5=this.formItem.getAllLocalOptions();if(isc.isAn.Array(_5)&&_5.length>1){var _6=String.fromCharCode(_4),_6=_6.toLowerCase(),_7=this.formItem,_8=_7.getValueFieldName(),_9=_5.indexOf(this.getSelectedRecord()),_10=_9<(_5.length-1)?_9+1:0;while(_10!=_9){if(_9<0)_9=0;var _11=_5[_10][_8];_11=_7.mapValueToDisplay(_11);if(isc.isA.String(_11)&&_11.length>0&&_11.charAt(0).toLowerCase()==_6){this.scrollRecordIntoView(_10);this.$18m(_10);return}
_10+=1;if(_10>=_5.length)_10=0}}}
if(this.getFocusRow()==null&&_3=="Enter"){this.cancel();return false}
return this.Super("bodyKeyPress",arguments)}
,isc.A.dataChanged=function isc_PickListMenu_dataChanged(_1,_2,_3,_4){var _5=this.data;if(!_5)return;var _5=this.requestVisibleRows();if(_5&&Array.isLoading(_5[0])){return}
this.Super("dataChanged",arguments);var _6=this.formItem;if(_2&&this.getSelectedRecord()==_2&&_6){var _7=this.data.indexOf(_2),_8=_7==-1?null:this.data.get(_7);if(_8){var _9=_6.getValueFieldName();_6.setValue(_8[_9])}else{_6.clearValue()}}}
);isc.B._maxIndex=isc.C+13;isc.PickList.addInterfaceProperties({pickListHeight:300,emptyPickListHeight:100,pickListMaxWidth:400,pickListBaseStyle:"pickListCell",pickListAnimationTime:200,fetchDelay:200,pickListCellHeight:16,pickListHeaderHeight:22,allowMultiSelect:true});isc.PickList.addInterfaceMethods({showPickList:function(_1,_2){this.$32q=true;if(!this.pickList)this.makePickList(_1,null,_2);else this.setUpPickList(_1,_2);if(!_1&&(!this.pickList.isDrawn()||!this.pickList.isVisible())){this.$32w()}},$32w:function(){var _1=this.pickList;if(!this.isDrawn()||(this.shouldHideEmptyPickList()&&_1.getTotalRows()<1)){return}
this.placePickList();if(!_1.isDrawn()||!_1.isVisible()){if(this.animatePickList)this.pickList.animateShow("wipe",null,this.pickListAnimationTime);else
this.pickList.show()}},fetchData:function(_1,_2,_3){if(this.getOptionDataSource()==null){this.logWarn("fetchData() called for a non-databound pickList. Ignoring");return}
if(_2==null)_2={};if(_1!=null){if(_2.clientContext==null)_2.clientContext={};_2.clientContext.$7o=_1}
_2.componentContext=this.form.ID+"."+this.name;if(!this.pickList){this.makePickList(false,_2,false,true)}else{this.filterDataBoundPickList(_2,!_3)}},mapValueToDisplay:function(_1,_2,_3,_4){if(this.isSelectOther){if(_1==this.otherValue)return this.otherTitle;if(_1==this.separatorValue)return this.separatorTitle}
return this.invokeSuper(isc.SelectItem,"mapValueToDisplay",_1,_2,_3,_4)},makePickList:function(_1,_2,_3,_4){var _5=isc.timeStamp();var _6=this.reusePickList();if(_6){this.pickList=this.getSharedPickList()}
if(!this.pickList){var _7=this.pickListProperties||{};if(this.multiple){if(_7.noDoubleClicks==null){_7.noDoubleClicks=true}}else{if(_7.noDoubleClicks==null){_7.noDoubleClicks=false}}
if(this.sortField!=null)_7.sortField=this.sortField;if(this.sortFieldNum!=null)_7.sortFieldNum=this.sortFieldNum;if(this.sortDirection!=null)_7.sortDirection=this.sortDirection;this.pickList=isc.PickListMenu.create({headerHeight:this.pickListHeaderHeight},_7);var _8=this.pickList.dataProperties||{};if(this.filterLocally)_8.fetchMode="local";this.pickList.dataProperties=_8;if(_6)this.storeSharedPickList()}
if(this.fetchDisplayedFieldsOnly&&this.optionDataSource&&(!this.optionFilterContext||!this.optionFilterContext.outputs))
{var _9=this.pickListFields||[];if(this.valueField)_9.add(this.valueField);if(this.displayField)_9.add(this.displayField);if(_9.length>0){if(!this.optionFilterContext)this.optionFilterContext={};this.optionFilterContext.outputs=_9.getUniqueItems().join(',')}}
this.setUpPickList(_1,_3,_2,_4);if(this.logIsInfoEnabled("timing"))
this.logInfo("Time to initially create pickList:"+(isc.timeStamp()-_5),"timing")},cachePickListResults:true,reusePickList:function(){return this.pickListProperties==null&&this.cachePickListResults},getSharedPickList:function(){if(this.$32x()){var _1=this.getOptionDataSource().getID(),_2=isc.PickListMenu.$32n[_1];if(_2){for(var i=0;i<_2.length;i++){if(_2[i].$32y==this.pickListFields){_2[i].$32z=isc.timeStamp();var _4=_2[i].$320;if(_4.destroyed){_2.removeAt(i);this.$321(_4);i--;continue}
return _4}}}
return null}else{if(isc.PickList.$322&&isc.PickList.$322.destroyed){this.$321(isc.PickList.$322);isc.PickList.$322=null;return null}
return isc.PickList.$322}},$321:function(_1){if(_1.$323!=null){for(var _2 in _1.$323){if(window[_2]&&window[_2].pickList==_1){delete window[_2].pickList}}}},storeSharedPickList:function(){if(this.$32x()){var _1=this.getOptionDataSource().getID(),_2=isc.PickListMenu.$32n;if(!_2[_1])_2[_1]=[];var _3={$320:this.pickList,$32y:this.pickListFields,$32z:isc.timeStamp()}
_2[_1].add(_3);if(isc.PickListMenu.$324==null){isc.PickListMenu.$324=1}else{isc.PickListMenu.$324+=1;if(isc.PickListMenu.$324>isc.PickListMenu.pickListCacheLimit){var _4,_5=isc.timeStamp();for(var _1 in _2){var _6=_2[_1];for(var i=0;i<_6.length;i++){var _8=_6[i];if(_8.$32z<=_5&&(_8!=_3)){_4=_8;_5=_8.$32z}}}
if(_4){isc.PickListMenu.$324-=1;var _9=_4.$320;var _6=_2[_9.getDataSource().getID()];_6.remove(_4);if(_9.$323!=null){for(var _10 in _9.$323){if(window[_10]&&window[_10].pickList==_9)
delete window[_10].pickList}}
_4.$320.delayCall("destroy")}}}}else{isc.PickList.$322=this.pickList}},getPickListCellHeight:function(){var _1=this.pickListCellHeight;if(this.pickListProperties&&this.pickListProperties.cellHeight!=null){_1=this.pickListProperties.cellHeight}
if(this.valueIcons!=null||this.getValueIcon!=null){var _2=this.getValueIconHeight();if(_2>_1)_1=_2}
return _1},setUpPickList:function(_1,_2,_3,_4){var _5=this.pickList;var _6=this.getPickListCellHeight();_5.setCellHeight(_6);this.$325();this.setUpPickListFields();if(!_5.originalEmptyMessage)_5.originalEmptyMessage=_5.emptyMessage;_5.emptyMessage=this.emptyPickListMessage||_5.originalEmptyMessage;this.pickList.emptyMessageHeight=this.emptyPickListHeight;this.pickList.setWidth(Math.max(1,this.pickListWidth||this.getElementWidth()));var _7=this.autoSizePickList&&!this.pickList.showHeader;this.pickList.autoFitFieldWidths=_7;this.pickList.setAutoFitData(_7?"both":"vertical");var _8=1;if(this.pickList.showHeader)_8+=this.pickList.headerHeight;if(this.pickList.showFilterEditor)_8+=this.pickList.filterEditorHeight;this.pickList.setHeight(_8);this.pickList.setAutoFitMaxHeight(this.pickListHeight);this.pickList.setAutoFitMaxWidth(this.pickListMaxWidth);this.filterPickList(_1,_2,_3,_4)},$325:function(){var _1=this.pickList.formItem;if(_1==this)return;var _2={};isc.addProperties(_2,{formItem:this,normalBaseStyle:this.pickListBaseStyle,tallBaseStyle:(this.pickListTallBaseStyle||this.pickListBaseStyle),hiliteColor:this.pickListHiliteColor,hiliteTextColor:this.pickListHiliteTextColor,showModal:this.modalPickList,dateFormatter:this.dateFormatter,dataArrived:function(_3,_4){if(isc.$dd)arguments.$de=this;this.Super("dataArrived",arguments);if(this.formItem)this.formItem.handleDataArrived(_3,_4,this.data)}});if(this.multiple&&this.multipleAppearance=="picklist"&&this.allowMultiSelect)
{_2.selectionAppearance="checkbox";_2.allowMultiSelect=true;_2.enableSelectOnRowOver=false;_2.selectionType="simple";_2.$18l=false;_2.className="listGrid";_2.bodyStyleName="gridBody"}else{_2.selectionAppearance="rowStyle";_2.allowMultiSelect=false;_2.enableSelectOnRowOver=true;_2.selectionType="single";_2.$18l=true;_2.className="scrollingMenu",_2.bodyStyleName="pickListMenuBody"}
if(this.pickListProperties){isc.addProperties(_2,this.pickListProperties)}
this.pickList.setProperties(_2);if(!this.pickList.$323)this.pickList.$323={};this.pickList.$323[this.getID()]=true;if(_1){if(this.pickList.isObserving(_1.containerWidget,"hide")){this.pickList.ignore(_1.containerWidget,"hide")}
if(this.pickList.isObserving(_1.containerWidget,"clear")){this.pickList.ignore(_1.containerWidget,"clear")}}
if(!this.pickList.isObserving(this.containerWidget,"hide")){this.pickList.observe(this.containerWidget,"hide","observer.hide();")}
if(!this.pickList.isObserving(this.containerWidget,"clear")){this.pickList.observe(this.containerWidget,"clear","if(observer.isDrawn())observer.clear();")}
this.pickList.markForRedraw()},getPickListFields:function(){if(this.pickListFields)return this.pickListFields;var _1=this.getDisplayFieldName(),_2;if(_1!=null){_2={width:"*",name:_1}
_2.formatCellValue=this.$326}else{_2={width:"*",name:this.getValueFieldName(),valueMap:this.getValueMap()}}
if(this.emptyDisplayValue!=null)_2.emptyCellValue=this.emptyDisplayValue;if(this.dateFormatter!=null){_2.type="date"}
_2.$327=true;return[_2]},$326:function(_1,_2,_3,_4,_5){if(_1!=null)return _1;var _6=_5.formItem,_7=_6.getValueFieldName()
if(_2[_7]==null)return _6.emptyCellValue;return _1},formatPickListValue:function(_1,_2,_3){if(this.pickList.getField(_2).$327){return this.$28p(_1)}
return _1},getPickListFilterCriteria:function(){var _1=isc.addProperties({},this.optionCriteria);return isc.DataSource.combineCriteria(_1,this.pickListCriteria,null,this.textMatchStyle)},getAllLocalOptions:function(){return this.$32x()?null:this.getClientPickListData()},$328:function(_1,_2,_3){var _4=this.getOptionDataSource();if(_4==null){return}
var _5=this.pickList&&!this.pickList.destroyed?(this.pickList.originalData||this.pickList.data):null;if(!_5||!(_5.localData||_5.allRows))return;var _6=(_2?this.getValueFieldName():this.getDisplayFieldName()||this.getValueFieldName()),_7=(_2?this.getDisplayFieldName()||this.getValueFieldName():this.getValueFieldName());if(_6==_7&&!_3)return _1;var _8=_5.allRows||_5.localData;var _9;if(isc.isAn.Array(_1)){_9="";var _10=isc.shallowClone(_1);for(var i=0;i<_1.length;i++){var _12=_10[i];var _13=_8.find(_7,_1[i]);if(_13!=null){_9+=_13[_6]}else continue;if(i!=_1.length-1)_9+=this.multipleValueSeparator}}else{var _13=_8.find(_7,_1);if(_13!=null)_9=_13[_6]}
return _9},$302:"true",setUpPickListFields:function(){var _1=this.getPickListFields(),_2=this.pickList.fields;var _3=!_2||(_2.length!=_1.length);if(!_3){for(var i=0;i<_1.length;i++){var _5=_1[i],_6=_2[i];for(var _7 in _5){if(_5[_7]!=_6[_7]){_3=true;break}}
if(_3)break}}
if(!_3)return;for(var i=0;i<_1.length;i++){if(_1[i].showIf==null){_1[i].showIf=this.$302}}
if(this.valueIcons!=null||this.getValueIcon!=null){for(var i=0;i<_1.length;i++){var _5=_1[i];if(_5[this.form.fieldIdProperty]==this.getValueFieldName()){if(_5.valueIconHeight==null)
_5.valueIconHeight=this.valueIconHeight;if(_5.valueIconWidth==null)
_5.valueIconWidth=this.valueIconWidth;if(_5.valueIconSize==null)
_5.valueIconSize=this.valueIconSize;if(_5.imageURLPrefix==null)
_5.imageURLPrefix=this.imageURLPrefix||this.baseURL||this.imgDir;if(_5.imageURLSuffix==null)
_5.imageURLSuffix=this.imageURLSuffix}}}
this.pickList.setFields(_1);var _8;if(this.pickListHeaderHeight==0)_8=false;else if(this.pickListProperties){if(this.pickListProperties.showHeader!=null){_8=this.pickListProperties.showHeader}else if(this.pickListProperties.headerHeight==0){_8=false}};if(_8==null){var _9=this.pickList.getFields();var _10=(this.multiple&&this.multipleAppearance=="picklist"&&this.allowMultiSelect==true)?2:1;_8=(_9.length>_10)}
if(_8){this.pickList.setHeaderHeight(this.pickListHeaderHeight);this.pickList.setShowHeader(true)}else{this.pickList.setShowHeader(false)}},$32x:function(){if(this.optionDataSource)return true;if((this.showOptionsFromDataSource||!this.valueMap)&&this.getOptionDataSource()!=null)return true;return false},filterPickList:function(_1,_2,_3,_4){if(!_2)
this.$329(_1,_3,null,_4);else{this.$33a=true;this.$33b=_1;this.fireOnPause("fetch",{target:this,methodName:"$329",args:[null,_3,true,_4]},this.fetchDelay)}},$329:function(_1,_2,_3,_4){this.$33a=null;if(_3)_1=this.$33b;delete this.$33b;this.$32s=_1;var _5=this.$32x();if(_5){var _6=this.getOptionDataSource();if(this.pickList.getDataSource()!=_6){this.pickList.setDataSource(_6,(this.pickList.completeFields||this.pickList.fields))}
this.filterDataBoundPickList(_2,_4)}else{var _7=this.filterClientPickListData();if(this.pickList.data!=_7)this.pickList.setData(_7);this.filterComplete()}},$33c:function(_1){if(!this.$32x()||!_1)return;var _2=this.getOptionDataSource();if(_1.getDataSource()==_2&&_1.data){var _3=_1.data.context,_4=_1.data.criteria;if(_3.textMatchStyle!=this.textMatchStyle)return true;if(this.optionFilterContext!=null){for(var _5 in this.optionFilterContext){if(this.optionFilterContext[_5]!=_3[_5])return true}}
if(_2.compareCriteria(_4,this.getPickListFilterCriteria(),_3)==0){return false}}
return true},getFirstOptionValue:function(){var _1;if(this.$32x()){var _2=this.pickList||(this.reusePickList()?this.getSharedPickList():null);if(_2&&!this.$33c(_2)){var _3=_2.data.get(0);if(_3==null||Array.isLoading(_3)){_1=null}else{_1=_3[this.getValueFieldName()]}}else{this.fetchData(null,null,true)}}else{var _4=this.valueMap;if(isc.isAn.Array(_4))_1=_4[0];else if(isc.isAn.Object(_4)){for(var _5 in _4){_1=_5;break}}}
return _1},getClientPickListData:function(){return isc.PickList.optionsFromValueMap(this)},$32r:function(){if(isc.Canvas.ariaEnabled()){this.setAriaState("expanded",false);this.clearAriaState("owns")}
if(this.pickListHidden)this.pickListHidden()},$32t:function(){if(isc.Canvas.ariaEnabled()){this.setAriaState("expanded",true);this.setAriaState("owns",this.pickList.getCanvasName())}
if(this.pickListShown)this.pickListShown()},selectDefaultItem:function(){return this.selectItemFromValue(this.getValue())},selectItemFromValue:function(_1){if(!isc.isAn.Array(_1))_1=[_1];var _2=this.pickList.getSelection(),_3=this.getValueFieldName(),_4=true,_5;for(var i=0;i<_1.length;i++){var _7=_1[i],_8;if(_2.find(_3,_7))continue;var _9=this.pickList.getData();if(isc.ResultSet&&isc.isA.ResultSet(_9)){var _10=_9.localData;if(_10)_8=_10.find(_3,_7)}else{_8=_9.find(_3,_7)}
if(_8&&_8!=Array.LOADING){if(this.pickList.allowMultiSelect)this.pickList.selectRecord(_8);else this.pickList.selection.selectSingle(_8);_5=_9.indexOf(_8)}else{_4=false}}
if(_5!=null)this.pickList.scrollRecordIntoView(_5);return _4},filterComplete:function(_1,_2,_3,_4){if(!_4&&_3!=null&&_3.clientContext!=null){var _5=this.$33d,_6=_3.clientContext.fetchID;if(_5==null||_5<_6){this.$33d=_6}else{this.logWarn("Server returned out of order responses for databound fetch requests."+" Ignoring superceded request results");return}}
this.$33e=false;this.$33f(_1,_2,_3);this.$33g(_1,_2,_3);var _7=(_3&&_3.clientContext?_3.clientContext.$7o:null);if(_7){this.fireCallback(_7,"item,dsResponse,data,dsRequest",[this,_1,_2,_3])}},$33f:function(_1,_2,_3){var _4=this.pickList;if(!_4||_4.destroyed)return;var _5=_4.hasFocus||(_4.body&&_4.body.hasFocus);var _2=_4.getData();if(_2.getLength()==0&&_4.isVisible()&&_4.isDrawn()){if(this.hideEmptyPickList){_4.hide();if(_5)this.focusInItem()}else{var _6=this.getPickListPosition();if(this.allowPickListToClip)_4.setRect([_6[0],_6[1]]);else _4.placeNear(_6[0],_6[1])}}else{if(this.$32s)this.$32w();else if(_4.isVisible()&&_4.isDrawn())this.placePickList();delete this.$32s}},$33g:function(_1,_2,_3){this.selectDefaultItem();if(_1!=null&&_2!=null){this.$288(_2,true);this.updateDisplayValueMap(false)}
if(this.$33h){delete this.$33h;this.$282(this._value)}
this.$33i()},$33i:function(){if(this.isDrawn()&&this.getValueFieldName()!=null&&this.$32x())
{if(this.isA("ComboBoxItem")){if(this.addUnknownValues==true){this.updateValue()}
if(this.hasFocus)return}
var _1;if(!this.$20g())_1=this.getValue();else{if(this.isA("SelectItem"))_1=this.$33j;else _1=this.mapDisplayToValue(this.getElementValue())}
var _2=this.getSelectedRecord();if(_2){var _3=this.mapValueToDisplay(_1);if(this.$33k!=_3){this.setElementValue(_3)}}}},$33l:0,filterDataBoundPickList:function(_1,_2){if(isc.$dd)arguments.$de=this;var _3=this.getPickListFilterCriteria(),_4={textMatchStyle:this.textMatchStyle,showPrompt:false};if(this.optionFilterContext!=null)isc.addProperties(_4,this.optionFilterContext);if(this.optionOperationId!=null)_4.operationId=this.optionOperationId;if(_1!=null){isc.addProperties(_4,_1)}
var _5=false,_6=false;var _7=this.pickList.originalData||this.pickList.data;if(_7&&isc.ResultSet&&isc.isA.ResultSet(_7)){if(_2){_7.$33m()}else{if(!_7.willFetchData(_3,this.textMatchStyle)){if(!_7.lengthIsKnown()||(_7.getLength()!=0&&!_7.rowIsLoaded(0))){_5=true;if(this.pickList.$33n!=this.getID()){if(!this.isObserving(_7,"fetchRemoteDataReply")){this.observe(_7,"fetchRemoteDataReply","observer.$33o(observed, dsResponse, data, request)");this.$33p=_7.$33q}}}else{_6=true}}}
if(!_5){_4.fetchID=this.$33l++;if(this.isObserving(_7,"fetchRemoteDataReply")){this.ignore(_7,"fetchRemoteDataReply")}}}
this.pickList.filterData(_3,{target:this,methodName:"filterComplete"},_4);if(_6&&this.pickList.data.getLength()>0&&!this.pickList.data.rowIsLoaded(0))
{this.logInfo("filterData with new criteria caused async fetch even though "+"data.willFetchData() returned false.","pickListFilter");_6=false}
if(_6)this.filterComplete();else{this.$33e=true;if(!_5){this.pickList.$33n=this.getID()}}},$33o:function(_1,_2,_3,_4){if(this.$33p!=_4.clientContext.requestIndex){return}
this.ignore(_1,"fetchRemoteDataReply");this.filterComplete(_2,_3,_4,true)},handleDataArrived:function(_1,_2,_3){if(this.defaultToFirstOption&&this.getValue()==null&&_1==0){this.setToDefaultValue()}
this.$33i();if(this.dataArrived)this.dataArrived(_1,_2,_3)},dataArrived:function(_1,_2,_3){},textMatchStyle:"startsWith",$33r:"substring",separatorRows:[{isSeparator:true}],filterClientPickListData:function(){var _1=this.getClientPickListData();var _2=this.getPickListFilterCriteria();if(_2==null||isc.isA.emptyObject(_2))return _1;var _3=[],_4;if(this.showAllOptions)_4=this.separatorRows.duplicate();var _5=false;for(var _6 in _2){var _7=_2[_6];if(!_7||isc.isA.emptyString(_7))continue;_5=true;if(!isc.isAn.Array(_7))_7=[_7];for(var _8=0;_8<_7.length;_8++){var _9=_7[_8];if(!isc.isA.String(_9))_9+=isc.emptyString;_9=_9.toLowerCase();var _10=_1.getLength(),_11=this.getValueFieldName();for(var i=0;i<_10;i++){var _13=_1[i][_6];if(this.filterDisplayValue&&_6==_11){_13=this.mapValueToDisplay(_13)}
if(!isc.isA.String(_13))_13+="";_13=_13.toLowerCase();if((this.textMatchStyle==this.$33r&&!_13.contains(_9))||(this.textMatchStyle!=this.$33r&&!isc.startsWith(_13,_9)))
{if(this.showAllOptions)_4.add(_1[i])}else{_3.add(_1[i])}}}}
if(!_5)_3=_1.duplicate();if(this.showAllOptions&&_4.length>1)_3.addList(_4);return _3},shouldHideEmptyPickList:function(){if(this.hideEmptyPickList!=null)return this.hideEmptyPickList;return!this.$32x()},getPickListPosition:function(){return[this.getPageLeft(),this.getPageTop()+this.getHeight()]},placePickList:function(){var _1=this.pickList;if(_1.isDirty()||(_1.body&&_1.body.isDirty())){_1.redraw("Refreshing stale pickList content before positioning")}else if(!_1.isDrawn()){isc.Canvas.moveOffscreen(_1);_1.draw()}
var _2=this.getPickListPosition(),_3=_2[0],_4=_2[1];if(this.allowPickListToClip)_1.setRect([_3,_4]);else _1.placeNear(_3,_4)},pickValue:function(_1){}});isc.A=isc.PickList;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.optionsFromValueMap=function isc_c_PickList_optionsFromValueMap(_1){var _2=_1.getValueMap(),_3=[];if(_2==null)_2=[];var _4=_1.getValueFieldName(),_5=_1.getDisplayFieldName();if(isc.isAn.Array(_2)){for(var i=0;i<_2.length;i++){_3[i]={}
_3[i][_4]=_2[i];if(_5!=null)_3[i][_5]=_2[i]}}else if(isc.isAn.Object(_2)){var i=0;var _7=_1.getType(),_8,_9,_10;if(_7!=null){if(isc.SimpleType.inheritsFrom(_7,"integer"))
{_8=true}else if(isc.SimpleType.inheritsFrom(_7,"float")){_9=true}else if(isc.SimpleType.inheritsFrom(_7,"boolean")){_10=true}}
for(var j in _2){_3[i]={};var _12=j;if(_8){var _13=parseInt(_12);if(_13==_12)_12=_13}else if(_9){var _14=parseFloat(_12);if(_14==_12)_12=_14}else if(_10){var _15=(_12=="true"?true:(_12=="false"?false:null));if(_15!=null)_12=_15}
_3[i][_4]=_12;if(_5!=null)_3[i][_5]=_2[j];i++}
_3.$33s=true}
return _3}
);isc.B._maxIndex=isc.C+1}
isc.ClassFactory.defineClass("NativeSelectItem","FormItem");isc.A=isc.NativeSelectItem;isc.A.DEFAULT_ROW_COUNT=6;isc.A.instances=[];isc.NativeSelectItem.addProperties(isc.$33t)
isc.A=isc.NativeSelectItem.getPrototype();isc.A.height=null;isc.A.$23x=true;isc.A.$26l=true;isc.A.$27o={onchange:isc.FormItem.$23m};isc.A=isc.NativeSelectItem;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.getOptionsHTML=function isc_c_NativeSelectItem_getOptionsHTML(_1,_2){var _3=isc.SB.create();if(isc.isAn.Array(_1)){for(var i=0,_5=_1.length;i<_5;i++){var _6=_1[i];_3.append(this.$33u(_6,_6,_2))}}else{for(var _6 in _1){var _7=_1[_6];_3.append(this.$33u(_6,_7,_2))}}
return _3.toString()}
,isc.A.$33u=function isc_c_NativeSelectItem__getOptionHTML(_1,_2,_3){var _4=this.$33v;if(!_4){this.$33w=" SELECTED ";_4=this.$33v=[];_4[0]="<OPTION ";_4[2]=' VALUE="';_4[4]='">';_4[6]="</OPTION>"}
_4[1]=(_1==_3?this.$33w:null);_4[3]=_1;_4[5]=_2;return _4.join(isc.$ah)}
,isc.A.getOptionCount=function isc_c_NativeSelectItem_getOptionCount(_1){if(isc.isAn.Array(_1)){return _1.length}else{var _2=0;for(var _3 in _1){_2++}
return _2}}
);isc.B._maxIndex=isc.C+3;isc.A=isc.NativeSelectItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.textMatchStyle="startsWith";isc.B.push(isc.A.getPickListFilterCriteria=function isc_NativeSelectItem_getPickListFilterCriteria(){var _1=this.optionCriteria||{};return isc.addProperties(_1,this.pickListCriteria)}
,isc.A.init=function isc_NativeSelectItem_init(){this.Super("init",arguments);isc.NativeSelectItem.instances.add(this);if(this.optionDataSource){var _1=this.getOptionDataSource();var _2=this.getValueFieldName();var _3=this.getDisplayFieldName();var _4=this;var _5={textMatchStyle:this.textMatchStyle,showPrompt:false};if(this.optionFilterContext!=null)isc.addProperties(_5,this.optionFilterContext);if(this.optionOperationId!=null)_5.operationId=this.optionOperationId;var _6=this.getPickListFilterCriteria();_1.fetchData(_6,function(_10,_11){var _7;if(!_3)_7=[];else _7={};for(var i=0;i<_11.getLength();i++){var _9=_11[i];if(!_3){_7.add(_9[_2])}else{_7[_9[_2]]=_9[_3]}}
_4.setValueMap(_7)},_5)}}
,isc.A.destroy=function isc_NativeSelectItem_destroy(){isc.NativeSelectItem.instances.remove(this);this.Super("destroy",arguments)}
,isc.A.getTextBoxCellCSS=function isc_NativeSelectItem_getTextBoxCellCSS(){return this.$24z}
,isc.A.setElementReadOnly=function isc_NativeSelectItem_setElementReadOnly(_1){this.$299(!_1&&!this.isDisabled())}
,isc.A.getElementHTML=function isc_NativeSelectItem_getElementHTML(_1,_2){this.$33x=false;var _3=this.form,_4=_3.getID(),_5=isc.StringBuffer.newInstance(),_6=this.getItemID();var _7=isc.$ah;var _8=this.$2n(_2);if(_8!=null)_5.append(_8);if(!this.showValueIconOnly){_5.append("<SELECT"," NAME=",this.getElementName()," ID=",this.getDataElementId(),this.$27a(),(!this.showTitle&&this.accessKey!=null?" ACCESSKEY="+this.accessKey:_7),((this.isReadOnly()||this.isDisabled())?" DISABLED ":_7),this.getElementStyleHTML(),(this.multiple?" MULTIPLE":_7)," TABINDEX=",this.$269()," handleNativeEvents=false>");_5.append(this.getOptionsHTML(this.getValueMap()));_5.append("</SELECT>")}
return _5.toString()}
,isc.A.$23n=function isc_NativeSelectItem__handleElementChanged(_1){if(isc.Browser.isIE&&!_1){isc.Timer.setTimeout(this.getID()+".$23n(true)",10);return true}
return this.form.elementChanged(this.getID())}
,isc.A.$23k=function isc_NativeSelectItem__nativeElementBlur(_1,_2){var _3=this.Super("$23k",arguments);if(this.changeOnBlur)this.form.elementChanged(this)}
,isc.A.getOptionsHTML=function isc_NativeSelectItem_getOptionsHTML(_1){var _2=isc.NativeSelectItem.getOptionsHTML(_1?_1:this.getValueMap());if(this.isSelectOther){_2+="<OPTION VALUE=\""+this.separatorValue+"\">"+this.separatorTitle+"<OPTION VALUE=\""+this.otherValue+"\">"+this.otherTitle}
return _2}
,isc.A.getOptionCount=function isc_NativeSelectItem_getOptionCount(_1){return isc.NativeSelectItem.getOptionCount(_1?_1:this.getValueMap())}
,isc.A.getElementStyleHTML=function isc_NativeSelectItem_getElementStyleHTML(){var _1=isc.SB.create(),_2=isc.SB.create();if(this.textBoxStyle!=null)_1.append(" CLASS='",this.getTextBoxStyle(),"' ");if(this.multiple||this.rows){var _3=this.rows;if(!isc.isA.Number(_3)||_3<1)
_3=Math.min(isc.NativeSelectItem.DEFAULT_ROW_COUNT,this.getOptionCount());if(this.height){if(isc.isA.Number(this.height))_2.append("HEIGHT:",this.height,"px;")}
_1.append(" SIZE=",_3)}
if(isc.Browser.isDOM){var _4=this.getElementWidth();if(isc.isA.Number(_4)){_4=Math.max(_4,1);_2.append("WIDTH:",_4,"px;")}
if(isc.Browser.isMoz){_2.append("-moz-user-focus:",(this.$269()>0?"normal;":"ignore;"))}
_2.append("margin-top:0px;margin-bottom:0px;");_2=_2.toString();if(_2.length>0)_1.append(" STYLE='",_2,"'")}
return _1.toString()}
,isc.A.$28c=function isc_NativeSelectItem__iconVisibilityChanged(){if(!isc.isA.Number(this.width))return;return this.Super("$28c",arguments)}
,isc.A.$270=function isc_NativeSelectItem__getIconVMargin(){return 0}
,isc.A.mapValueToDisplay=function isc_NativeSelectItem_mapValueToDisplay(_1){if(isc.isAn.Array(_1)){var _2=[];for(var i=0;i<_1.length;i++){_2[i]=this.mapValueToDisplay(_1[i])}
return _2}
return this.Super("mapValueToDisplay",arguments)}
,isc.A.setElementValue=function isc_NativeSelectItem_setElementValue(_1,_2){if(arguments.length==1)_2=_1;var _3=this.getDataElement();if(!_3)return null;var _4=_3.options;if(!_4){this.logDebug("setElementValue(): element.options is null. ???");return null}
this.$29b(_2);if(!this.multiple){if(_2==null)_2="";for(var i=0;i<_4.length;i++){if(_4[i].value==_2){if(_3.selectedIndex!=i){_3.selectedIndex=i}
return _3.selectedIndex}}
for(var i=0;i<_4.length;i++){if(_4[i].text==_2){if(_3.selectedIndex!=i){_3.selectedIndex=i}
_3.selectedIndex=i;return _3.selectedIndex}}
if(this.addUnknownValues){if(isc.Browser.isIE){var _6=0;if(this.$33x){_4[_6].text=_1;_4[_6].value=_2}else{_4.add(new Option(_2,_1),_6);this.$33x=true}}else{if(this.$33x){var _6=_4.length-1;_4[_6].value=_2;_4[_6].text=_1}else{var _6=_4.length;_4[_6]=new Option(_2,_1);this.$33x=true}}
if(_3.selectedIndex!=_6){_3.selectedIndex=_6}
return _3.selectedIndex}else{return null}}else{if(_2==null){_2=[]}else if(isc.isA.String(_2)&&_2.contains(",")){_2=_2.split(",")}else if(!isc.isAn.Array(_2)){_2=[_2]}else{_2=_2.duplicate()}
if(_1==null){_1=[]}else if(isc.isA.String(_1)&&_1.contains(",")){_1=_1.split(",")}else if(!isc.isAn.Array(_1)){_1=[_1]}else{_1=_1.duplicate()}
for(var i=0;i<_4.length;i++){var _7=_3.options[i];var _8=_2.indexOf(_7.value);if(_8>-1){if(_7.selected!=true)_7.selected=true;_2.removeItem(_8)}else{if(_7.selected!=false)_7.selected=false}}
if(_2.length!=0){for(var i=0;i<_4.length;i++){var _7=_3.options[i];var _8=_2.indexOf(_7.text);if(_8>-1){if(_7.selected!=true)_7.selected=true;_2.removeItem(_8)}}}
if(_2.length!=0&&this.addUnknownValues){for(var i=0;i<_2.length;i++){var _9=_4[_4.length]=new Option(_2[i],_1[i]);_9.selected=true}}
return _2}}
,isc.A.getElementValue=function isc_NativeSelectItem_getElementValue(){var _1=this.getDataElement();if(!_1)return null;var _2=_1.options;if(!_2||_2.length==0)return null;if(!this.multiple){var _3=_2[_1.selectedIndex];if(!_3)return null;return(_3.value!=null?_3.value:_3.text)}else{var _4=[];for(var i=0;i<_2.length;i++){var _3=_2[i];if(_3.selected){_4.add(_3.value!=null?_3.value:_3.text)}}
if(_4.length<2)return _4[0];return _4}}
,isc.A.setElementValueMap=function isc_NativeSelectItem_setElementValueMap(_1){this.$33x=false;this.Super("setElementValueMap",arguments);var _2=this.getDataElement();if(_2==null)return;var _3=_2.options;_3.length=0;if(isc.isAn.Array(_1)){for(var i=0;i<_1.length;i++){_3[i]=new Option(_1[i],_1[i])}}else{for(var _5 in _1){_3[_3.length]=new Option(_1[_5],_5)}}
if(this.isSelectOther){_3[_3.length]=new Option(this.separatorTitle,this.separatorValue);_3[_3.length]=new Option(this.otherTitle,this.otherValue)}}
,isc.A.updateValue=function isc_NativeSelectItem_updateValue(){if(this.isSelectOther){if(!this.hasElement()||this.getDataElement()==null)return;var _1=this._value,_2=this.getElementValue();if(_2==this.separatorValue){this.setValue(_1);return false}
if(_2==this.otherValue){var _3=this.getValueMapTitle(_1);_2=prompt("Other value for \r'"+this.getTitle()+"'?",(_3?_3:""));if(_2==null){this.setValue(_1);return false}
this.setElementValue(_2)}}
return this.Super("updateValue",arguments)}
);isc.B._maxIndex=isc.C+18;if(isc.ListGrid){isc.ClassFactory.defineClass("SelectItem","FormItem");isc.$33t={textBoxStyle:"selectItemText",height:19,width:150,dirtyOnKeyDown:false,changeOnKeypress:false,redrawOnShowIcon:false,addUnknownValues:true,autoSizePickList:true,multipleAppearance:"picklist",separatorTitle:"--------------------",separatorValue:"----",otherTitle:"Other...",otherValue:"***other***"};isc.SelectItem.addProperties(isc.$33t)
isc.A=isc.SelectItem.getPrototype();isc.A.showPickerIcon=true;isc.A.emptyDisplayValue="&nbsp;";isc.A.controlStyle="selectItemControl";isc.A.pickerIconStyle="selectItemPickerIcon";isc.A.canFocus=true;isc.A.showFocused=true;isc.A.pickerIconSrc="[SKIN]/DynamicForm/SelectItem_PickButton_icon.gif";isc.A.pickerIconDefaults={tabIndex:-1,click:function(){}};isc.A.clipValue=true;isc.A.showOver=true;isc.A.modalPickList=true;isc.A.changeOnValueChange=true;isc.A.changeOnKeyboardNavigation=true;isc.A.canSelectText=false;isc.A.allowEmptyValue=false;isc.A.autoFetchData=true;isc.A=isc.SelectItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.selectOtherPrompt="Other value for <br>${item.getTitle()}?";isc.A.dialogWidth=250;isc.B.push(isc.A.init=function isc_SelectItem_init(){if(this.hiliteOnFocus!=null){this.$26t("hiliteOnFocus","showFocused");this.showFocused=this.hiliteOnFocus}
if(this.pickButtonWidth!=null){this.$26t("pickButtonWidth","pickerIconWidth");this.pickerIconWidth=this.pickButtonWidth}
if(this.pickButtonHeight!=null){this.$26t("pickButtonHeight","pickerIconHeight");this.pickerIconHeight=this.pickButtonHeight}
if(this.pickButtonSrc!=null){this.$26t("pickButtonSrc","pickerIconSrc");this.pickerIconSrc=this.pickButtonSrc}
return this.Super("init",arguments)}
,isc.A.drawn=function isc_SelectItem_drawn(_1,_2,_3,_4){this.invokeSuper(isc.SelectItem,"drawn",_1,_2,_3,_4);if(this.autoFetchData&&this.$32x()){this.fetchData(null,null,true)}}
,isc.A.$270=function isc_SelectItem__getIconVMargin(){return 0}
,isc.A.$23p=function isc_SelectItem__iconFocus(_1,_2){var _3=this.getIcon(_1);if(_3==this.getPickerIcon()){_2.blur();this.focusInItem();return}
return this.Super("$23p",arguments)}
,isc.A.setElementReadOnly=function isc_SelectItem_setElementReadOnly(_1){}
,isc.A.handleMouseMove=function isc_SelectItem_handleMouseMove(){if(this.showOver&&!this.isDisabled()){if(this.$28n())this.$28h(this.getPickerIcon(),true);else this.$28h(this.getPickerIcon(),false)}
return this.Super("handleMouseMove",arguments)}
,isc.A.handleMouseOut=function isc_SelectItem_handleMouseOut(){if(this.showOver&&!this.isDisabled()){this.$28h(this.getPickerIcon(),false)}
return this.Super("handleMouseOut",arguments)}
,isc.A.$210=function isc_SelectItem__iconMouseOut(_1,_2,_3,_4,_5){if(this.getIcon(_1)==this.getPickerIcon()&&this.$28n())return;return this.invokeSuper("SelectItem","$210",_1,_2,_3,_4,_5)}
,isc.A.handleClick=function isc_SelectItem_handleClick(){if(!this.isDisabled()){this.focusInItem();this.showPickList()}
return this.Super("handleClick",arguments)}
,isc.A.handleKeyPress=function isc_SelectItem_handleKeyPress(_1,_2){var _3=this.Super("handleKeyPress",arguments);if(_3==false)return false;if(this.isReadOnly())return _3;var _4=_1.keyName;if(_4=="Enter"&&this.isSelectOther){if(this.$33y!=null)this.updateValue()}else if(_4=="Arrow_Down"){if(isc.EH.altKeyDown())this.showPickList();else this.moveToNextValue(1);_3=false}else if(_4=="Arrow_Up"){if(isc.EH.altKeyDown())this.showPickList();else this.moveToNextValue(-1);_3=false}else if(_4=="Home"){this.moveToFirstValue();_3=false}else if(_4=="End"){this.moveToLastValue();_3=false}else{var _5=_1.characterValue;if(_5!=null){this.moveToChar(_5)}}
return _3}
,isc.A.$23k=function isc_SelectItem__nativeElementBlur(_1,_2){var _3=this.Super("$23k",arguments);if(this.changeOnBlur||this.$20g()||this.$33y){if(isc.Browser.isMoz&&this.$33y==this.otherValue)
this.form.$21t=true;this.updateValue()}
return _3}
,isc.A.showPickList=function isc_SelectItem_showPickList(_1,_2){var _3=isc.PickList.getPrototype().showPickList;_3.apply(this,arguments);if(this.pickList){this.pickList.deselectAllRecords();if(this.getValue())this.selectItemFromValue(this.getValue())}}
,isc.A.handleEditorExit=function isc_SelectItem_handleEditorExit(){if(this.$32q)return;return this.Super("handleEditorExit",arguments)}
,isc.A.editorEnter=function isc_SelectItem_editorEnter(_1,_2,_3){this.$29g()}
,isc.A.editorExit=function isc_SelectItem_editorExit(_1,_2,_3){var _4;if(this.showHintInField&&(_3===_4||_3==null||isc.is.emptyString(_3)))
{this.$29c()}}
,isc.A.$32t=function isc_SelectItem__pickListShown(){this.handleEditorEnter();if(this.pickListShown)this.pickListShown()}
,isc.A.$32r=function isc_SelectItem__pickListHidden(){if(this.fireChangeOnSelect==false)this.updateValue();if(this.pickListHidden)this.pickListHidden()}
,isc.A.getAllLocalOptions=function isc_SelectItem_getAllLocalOptions(){var _1;if(this.$32x()){if(!this.pickList||this.pickList.destroyed)return;var _2=this.pickList.data;if(!_2||!_2.lengthIsKnown()||!_2.allMatchingRowsCached())return;var _3=this.getPickListFilterCriteria();if(_2.compareCriteria(_3,_2.criteria)!=0){if(!_2.allRowsCached()||!_2.useClientFiltering)return;this.filterPickList(false,false)}
_1=_2.getAllRows()}else{_1=this.getClientPickListData()}
return _1}
,isc.A.moveToChar=function isc_SelectItem_moveToChar(_1){var _2=this.getAllLocalOptions();if(!_2||_2.length<2)return;var _3=String.fromCharCode(_1);if(_3==null)return;_3=_3.toLowerCase();var _4=(this.isSelectOther&&this.$33y!=null)?this.$33y:(this.$20g()?this.$33j:this.getValue()),_5=this.getValueFieldName(),_6=_2.findIndex(_5,_4),i=(_6==_2.length-1?0:_6+1);while(i!=_6){if(_6<0)_6=0;var _8=_2[i][this.getValueFieldName()],_9=this.mapValueToDisplay(_8);if(isc.isA.String(_9)){var _10=_9.charAt(0).toLowerCase();if(_10==_3){var _11=_8;this.changeToValue(_11,(this.changeOnValueChange&&this.changeOnKeyboardNavigation));return}}
i+=1;if(i>=_2.length)i=0}}
,isc.A.moveToNextValue=function isc_SelectItem_moveToNextValue(_1){var _2=this.getAllLocalOptions();if(!_2||_2.length<1)return;var _3;if(this.isSelectOther&&this.$33y!=null)_3=this.$33y;else _3=(this.$20g()?this.$33j:this.getValue());var _4=this.getValueFieldName(),_5=_2.findIndex(_4,_3);_5+=_1;if(_5>=_2.length||_5<0)return;var _6=_2[_5][_4];this.changeToValue(_6,(this.changeOnValueChange&&this.changeOnKeyboardNavigation))}
,isc.A.moveToFirstValue=function isc_SelectItem_moveToFirstValue(){if(this.optionDataSource)return;var _1=this.getClientPickListData(),_2=this.getValueFieldName(),_3=_1[0][_2];this.changeToValue(_3,(this.changeOnValueChange&&this.changeOnKeyboardNavigation))}
,isc.A.moveToLastValue=function isc_SelectItem_moveToLastValue(){if(this.optionDataSource)return;var _1=this.getClientPickListData(),_2=this.getValueFieldName(),_3=_1[_1.length-1][_2]
this.changeToValue(_3,(this.changeOnValueChange&&this.changeOnKeyboardNavigation))}
,isc.A.$mb=function isc_SelectItem__canFocus(){return true}
,isc.A.$33z=function isc_SelectItem__getIconMouseDownFunction(){if(!this.$330){this.$330=new Function("if(window."+this.getID()+")window."+this.getID()+".$32q=true;")}
return this.$330}
,isc.A.$27f=function isc_SelectItem__applyHandlersToElement(_1,_2,_3,_4){this.invokeSuper(isc.SelectItem,"$27f",_1,_2,_3,_4);if(isc.Browser.isIE){var _5=this.$6h(this.getPickerIcon());if(_5){_5.onmousedown=this.$33z()}}}
,isc.A.makePickList=function isc_SelectItem_makePickList(_1){if(!this.filterLocally&&this.allowEmptyValue&&this.$32x()){if(this.pickListProperties==null)
this.pickListProperties={};if(this.pickListProperties.dataProperties==null)
this.pickListProperties.dataProperties={};this.pickListProperties.dataProperties.fetchMode="basic"}
var _2=isc.PickList.getPrototype().makePickList;return _2.apply(this,arguments)}
,isc.A.changeToValue=function isc_SelectItem_changeToValue(_1,_2){var _3=(this.$33y||this.$33j||this.getValue());if(_3==_1)return;if(this.isSelectOther&&(_1==this.separatorValue||_1==this.otherValue))
{this.setElementValue(this.mapValueToDisplay(_1));this.$33y=_1;return}else{delete this.$33y}
this.setLocalValue(_1);if(_2)this.updateValue()}
,isc.A.setLocalValue=function isc_SelectItem_setLocalValue(_1){this.$33j=_1;if(this.isVisible()&&this.containerWidget.isDrawn()){if(_1==null)_1=null;this.setElementValue(this.mapValueToDisplay(_1),_1)}
this.$30q()}
,isc.A.setElementValue=function isc_SelectItem_setElementValue(_1,_2,_3,_4,_5){this.$33k=_1;if(this.showHintInField&&this.getHint()){var _6;if(_1===_6||_1==null||isc.is.emptyString(_1))
{if(this.hasDataElement()){var _7=this.getDataElement();_7.className=this.$29e()}else{var _8=this.$6f();if(_8!=null)
_8.className=this.$29e()}
var _9=this.getHint();if(_9)_9=_9.unescapeHTML();_1=_9;this.$29d=true}}
return this.invokeSuper(isc.SelectItem,"setElementValue",_1,_2,_3,_4,_5)}
,isc.A.updateValue=function isc_SelectItem_updateValue(){if(this.isSelectOther&&this.$33y!=null){var _1=this.getSelectOtherValue(this.$33y);delete this.$33y;this.setLocalValue(_1)}
if(!this.$20g())return;var _2=this.$33j;this.$20l(_2)}
,isc.A.mapDisplayToValue=function isc_SelectItem_mapDisplayToValue(_1){return _1}
,isc.A.getSelectOtherValue=function isc_SelectItem_getSelectOtherValue(_1){if(_1==this.separatorValue)return(this.$33j||this.getValue());if(_1==this.otherValue){var _2=this.$33j||this.getValue(),_3=(_2==null?"":this.mapValueToDisplay(_2)),_4=this.selectOtherPrompt.evalDynamicString(null,{item:this,value:_2}),_5=isc.addProperties({width:this.dialogWidth},this.dialogDefaults,this.dialogProperties);isc.askForValue(_4,this.getID()+".getSelectOtherValueCallback(value)",_5);return true}}
,isc.A.getSelectOtherValueCallback=function isc_SelectItem_getSelectOtherValueCallback(_1){if(_1!=null){_1=this.mapDisplayToValue(_1);this.changeToValue(_1,this.changeOnValueChange)}}
,isc.A.setValue=function isc_SelectItem_setValue(_1,_2,_3,_4){_1=this.$331(_1);var _5,_6=this.$33j;if(_6===_5)_6=this._value;this.invokeSuper(isc.SelectItem,"setValue",_1,_2,_3,_4);_1=this.getValue();if(_1!=_6)this.setLocalValue(_1);if(this.pickList&&this.pickList.isDrawn()&&this.pickListVisible()){this.setUpPickList(true)}
if(!this.hasFocus&&this.showHint&&this.showHintInField&&this.getHint()){if(_1===_5||_1==null||isc.is.emptyString(_1)){this.$29c()}}
return _1}
,isc.A.saveValue=function isc_SelectItem_saveValue(_1,_2,_3,_4,_5){var _6=this._value;if(this.$332(_6,_1))delete this.$333;return this.invokeSuper(isc.SelectItem,"saveValue",_1,_2,_3,_4,_5)}
,isc.A.$332=function isc_SelectItem__dropCacheOnValueChange(_1,_2){return(this.addUnknownValues&&this.$333&&((_1!=null&&!this.$334(_1))||(_2!=null&&!this.$334(_2))))}
,isc.A.$20k=function isc_SelectItem__markValueAsNotDirty(_1,_2,_3,_4){this.invokeSuper(isc.SelectItem,"$20k",_1,_2,_3,_4);delete this.$33j}
,isc.A.getDefaultValue=function isc_SelectItem_getDefaultValue(){var _1=this.Super("getDefaultValue",arguments);if(_1==null&&this.defaultToFirstOption)_1=this.getFirstOptionValue();return this.$331(_1)}
,isc.A.$331=function isc_SelectItem__getValidValue(_1){if(!this.$335(_1)){var _2=this.$336;this.$336=true;var _3;if(_2)_3=_1;else _3=this.$33j||this.getValue();if(_1==_3||!this.$335(_3)){_3=null}
_1=_3}
return _1}
,isc.A.$335=function isc_SelectItem__valueIsValid(_1){if(this.addUnknownValues||this.optionDataSource)return true;if(_1==null)return true;if(isc.isAn.Array(_1)){for(var i=0;i<_1.length;i++){if(!this.$334(_1[i]))return false}
return true}else{return this.$334(_1)}}
,isc.A.$334=function isc_SelectItem__valueInValueMap(_1){var _2=this.getValueMap(),_3;if(isc.isAn.Array(_2)){return _2.contains(_1)}else if(isc.isAn.Object(_2)){return(_2[_1]!==_3)}
return false}
,isc.A.mapValueToDisplay=function isc_SelectItem_mapValueToDisplay(_1,_2,_3,_4){if(this.isSelectOther){if(_1==this.otherValue)return this.otherTitle;if(_1==this.separatorValue)return this.separatorTitle}
return this.invokeSuper(isc.SelectItem,"mapValueToDisplay",_1,_2,_3,_4)}
,isc.A.getSelectedRecord=function isc_SelectItem_getSelectedRecord(){if(this.pickList==null||this.pickList.destroyed)this.makePickList(false);var _1,_2=this.$33j;if(_2===_1)_2=this.getValue();if(this.selectItemFromValue(_2)){return this.pickList.getSelectedRecord()}
return this.Super("getSelectedRecord",arguments)}
,isc.A.getSelectedRecords=function isc_SelectItem_getSelectedRecords(){var _1=this.$33j;this.selectItemFromValue(_1);var _2=this.pickList.getSelection();if(_2.length>0)return _2;else return null}
,isc.A.$28o=function isc_SelectItem__mapKey(_1,_2,_3,_4,_5,_6){var _7=this.invokeSuper(isc.SelectItem,"$28o",_1,true,_3,_4,_5,_6);if(_7==null&&this.getDisplayFieldName()!=null)
_7=this.$328(_1,false);if(_7==null&&!_2)_7=_1;return _7}
,isc.A.$282=function isc_SelectItem__checkForDisplayFieldValue(_1,_2){var _3=(this.$28o(_1,true)!=null);if(_3)return;if(this.$33e){this.$33h=true;return}
this.invokeSuper(isc.SelectItem,"$282",_1)}
,isc.A.getClientPickListData=function isc_SelectItem_getClientPickListData(){if(this.$333)return this.$333;var _1=isc.PickList.optionsFromValueMap(this),_2=this.getValueFieldName();if(this.allowEmptyValue&&!this.multiple&&(_1.find(_2,null)==null)){var _3={};_3[_2]=null;_1.addAt(_3,0)}
var _4=this.getValue();if(_4!=null&&!isc.isAn.Array(_4)){if(_1.find(_2,_4)==null){var _5={};_5[_2]=_4;_1.addAt(_5,0)}}
if(this.isSelectOther){var _6={},_7={};_6[_2]=this.separatorValue;_7[_2]=this.otherValue;_1.addListAt([_6,_7],_1.length)}
this.$333=_1;return _1}
,isc.A.formatPickListValue=function isc_SelectItem_formatPickListValue(_1,_2,_3){if(this.isSelectOther&&(_2==this.getValueFieldName())){if(_1==this.otherValue)return this.otherTitle;if(_1==this.separatorValue)return this.separatorTitle}
if(this.pickList.getField(_2).$327){return this.$28p(_1)}
return _1}
,isc.A.pickValue=function isc_SelectItem_pickValue(_1){if(this.isSelectOther){if(this.getSelectOtherValue(_1))return}
this.changeToValue(_1,(this.changeOnValueChange&&this.fireChangeOnSelect!=false))}
,isc.A.getPickListPosition=function isc_SelectItem_getPickListPosition(){var _1=this.getPageTop(),_2=_1+this.getHeight(),_3=this.getPageLeft(),_4=isc.Page.getScrollTop(),_5=isc.Page.getHeight()+_4;var _6;if(!this.pickList.isDrawn()){var _7=this.pickList.data;if(_7==null||_7.getLength()==0){_6=this.emptyPickListHeight}else{_6=Math.min((_7.getLength()*this.pickList.cellHeight)+(this.pickList.showHeader?this.pickList.headerHeight:0)+(this.pickList.showFilterEditor?this.pickList.filterEditorHeight:0),this.pickListHeight)}}else{_6=this.pickList.getVisibleHeight()}
if(_2+_6>_5){_2=Math.max(_4,(_1-_6))}
return[_3,_2]}
,isc.A.setValueMap=function isc_SelectItem_setValueMap(){this.Super("setValueMap",arguments);if(this.$333)delete this.$333;if(this.hasPickList()){if(this.pickList.isVisible()&&this.pickList.isDrawn()){this.pickList.hide()}
delete this.pickList.formItem}
var _1=this.getValue(),_2=this.$331(_1);if(_1!=_2){this.setValue(_2)}else{this.setElementValue(this.mapValueToDisplay(_2))}}
,isc.A.hasPickList=function isc_SelectItem_hasPickList(){return(this.pickList&&!this.pickList.destroyed&&this.pickList.formItem==this)}
,isc.A.pickListVisible=function isc_SelectItem_pickListVisible(){return(this.hasPickList()&&this.pickList.isDrawn()&&this.pickList.isVisible())}
,isc.A.cleared=function isc_SelectItem_cleared(){if(this.pickListVisible())this.pickList.hide();return this.Super("cleared",arguments)}
,isc.A.filterComplete=function isc_SelectItem_filterComplete(){if(this.allowEmptyValue&&!this.multiple&&this.$32x()){var _1=this.pickList.data,_2=_1.isLocal()?_1.allRows:_1.localData,_3=this.getValueFieldName();if(_2&&!_2.find(_3,null)){var _4={};_4[_3]=null;_1.insertCacheData({},0)}}
var _5=isc.PickList.getPrototype().filterComplete;_5.apply(this,arguments)}
);isc.B._maxIndex=isc.C+55;isc.ClassFactory.mixInInterface("SelectItem","PickList");isc.SelectItem.registerStringMethods({dataArrived:"startRow,endRow,data",getPickListFilterCriteria:""})}else{isc.ClassFactory.defineClass("SelectItem","NativeSelectItem")}
isc.defineClass("CycleItem","FormItem");isc.A=isc.CycleItem.getPrototype();isc.A.canSelectText=false;isc.A.canFocus=true;isc.A.iconVAlign="middle";isc.A=isc.CycleItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.handleKeyPress=function isc_CycleItem_handleKeyPress(){var _1=isc.EH.getKey();var _2=this.isReadOnly();if(!_2&&_1=="Space"){this.advanceValue();return false}
return this.Super("handleKeyPress",arguments)}
,isc.A.handleClick=function isc_CycleItem_handleClick(){if(this.isDisabled()||this.isReadOnly())return;if(!this.hasFocus)this.focusInItem();this.advanceValue()}
,isc.A.handleDoubleClick=function isc_CycleItem_handleDoubleClick(){if(this.isDisabled()||this.isReadOnly())return;this.advanceValue()}
,isc.A.advanceValue=function isc_CycleItem_advanceValue(){var _1=this.getValueMap();if(isc.isA.Object(_1)&&!isc.isA.Array(_1)){_1=isc.getKeys(_1)}
if(_1==null||_1.length<2){this.logInfo("CycleItem is non interactive as there are no options for this item.");return}
var _2=this.getValue(),_3=_1.indexOf(_2);if(_3==_1.length-1)_3=-1;var _4=_1[_3+1];if(!this.compareValues(_4,this._value)){var _5=this.mapValueToDisplay(_4);this.setElementValue(_5,_4);if(isc.Canvas.ariaEnabled())this.setAriaState("checked",!!_4);this.$20l(_4)}}
,isc.A.setElementReadOnly=function isc_CycleItem_setElementReadOnly(_1){this.$299(!_1&&!this.isDisabled())}
);isc.B._maxIndex=isc.C+5;isc.defineClass("CheckboxItem","CycleItem");isc.A=isc.CheckboxItem;isc.A.trueFalseValueMap=[true,false];isc.A.trueFalseNullValueMap=[true,false,null];isc.A=isc.CheckboxItem.getPrototype();isc.A.textBoxStyle="labelAnchor";isc.A.showLabel=true;isc.A.height=20;isc.A.requiredTitlePrefix="<b>";isc.A.requiredTitleSuffix="</b>";isc.A.valueIconLeftPadding=4;isc.A.valueIconRightPadding=3;isc.A.showValueIconOver=true;isc.A.showValueIconFocused=true;isc.A.showValueIconDown=true;isc.A.showValueIconDisabled=true;isc.A.checkedImage="[SKINIMG]/DynamicForm/checked.gif";isc.A.uncheckedImage="[SKINIMG]/DynamicForm/unchecked.gif";isc.A.partialSelectedImage="[SKINIMG]/DynamicForm/partialcheck.gif";isc.A.unsetImage="[SKINIMG]/DynamicForm/unsetcheck.gif";isc.A.valueIconWidth=13;isc.A.valueIconHeight=13;isc.A=isc.CheckboxItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.allowEmptyValue=false;isc.A.$302="true";isc.A.$3i="false";isc.A.emptyValueKey="**NULL**";isc.A.$24p="Over";isc.A.$24q="Down";isc.A.$24r="Disabled";isc.A.$337="height:";isc.A.$tk="px";isc.B.push(isc.A.getValueMap=function isc_CheckboxItem_getValueMap(){if(this.$338)return this.$338;var _1=this.Super("getValueMap",arguments);if(_1!=null){var _2=false,_3=isc.isAn.Object(_1);if(isc.isAn.Array(_1)){_3=false;if(_1.length!=2){_1=null}else{var _4=_1.indexOf(this.$3i);if(_4!=-1)_1[_4]=false;var _5=_1.indexOf(this.$302);if(_5!=-1)_1[_5]=true;if(!((_1[0]&&!_1[1])||(!_1[0]&&_1[1]))){this.logInfo("Checkbox item created with valueMap:"+_1+"which has no explicit true/false display values. Mapping the first value to true and the second to false.");var _6={};_6[_1[0]]=true;_6[_1[1]]=false;this.valueMap=_1=_6;_3=true}else{_2=true}}}
if(_3){var _7=[],_8=[],_9;for(var _10 in _1){if(_7.length==2){_9=true;break}
var _11=_1[_10];if(_10==this.$3i)_10=false;else if(_10==this.$302)_10=true;if(_11==this.$3i){_11=_1[_10]=false}else if(_11==this.$302){_11=_1[_10]=true}
_8[_8.length]=_10;_7[_7.length]=_11}
if(_7.length!=2)_9=true;if(!_9){if((_7[0]&&!_7[1])||(!_7[0]&&_7[1])){}else if((_8[0]&&!_8[1])||(!_8[0]&&_8[1])){_1=_8}else _9=true}
if(_9)_1=null}else if(!_2){_1=null}}
if(_1&&this.allowEmptyValue){if(_2)_1.add(null);else _1[this.emptyValueKey]=null}
return(this.$338=_1||(this.allowEmptyValue?isc.CheckboxItem.trueFalseNullValueMap:isc.CheckboxItem.trueFalseValueMap))}
,isc.A.$28u=function isc_CheckboxItem__unmapKey(){var _1=this.Super("$28u",arguments);if(_1==this.emptyValueKey)_1=null;return _1}
,isc.A.setValueMap=function isc_CheckboxItem_setValueMap(){this.$338=null;return this.Super("setValueMap",arguments)}
,isc.A.init=function isc_CheckboxItem_init(_1,_2,_3,_4){this.invokeSuper(isc.CheckboxItem,"init",_1,_2,_3,_4);if(this.showValueIconOnly==null)this.showValueIconOnly=!this.showLabel;if(this.textAlign==null&&this.align!=null){this.textAlign=this.align}}
,isc.A.setShowLabel=function isc_CheckboxItem_setShowLabel(_1){this.showLabel=_1;this.showValueIconOnly=!_1;if(this.isDrawn())this.redraw()}
,isc.A.mapValueToDisplay=function isc_CheckboxItem_mapValueToDisplay(_1,_2,_3,_4){if(this.labelAsTitle)return isc.emptyString;var _5=this.invokeSuper(isc.CheckboxItem,"getTitleHTML",_1,_2,_3,_4);var _6=this.form;if((this.required||this.$21n)&&_6&&_6.hiliteRequiredFields){_5=this.requiredTitlePrefix+_5+this.requiredTitleSuffix}
return _5}
,isc.A.getValueIcon=function isc_CheckboxItem_getValueIcon(_1){var _2=this.getValueMap();if(!isc.isAn.Array(_2)&&isc.isAn.Object(_2))_1=_2[_1];if(_1)return this.checkedImage;else if(_1===false)return this.uncheckedImage;else{if(this.showUnsetImage!=null){return this.showUnsetImage?this.unsetImage:this.uncheckedImage}
return this.allowEmptyValue?this.unsetImage:this.uncheckedImage}}
,isc.A.getTitleHTML=function isc_CheckboxItem_getTitleHTML(_1,_2,_3){if(this.labelAsTitle)return this.invokeSuper(isc.CheckboxItem,"getTitleHTML",_1,_2,_3);return isc.emptyString}
,isc.A.$26x=function isc_CheckboxItem__writeOuterTable(){return true}
,isc.A.getTextBoxHeight=function isc_CheckboxItem_getTextBoxHeight(){return null}
,isc.A.getTextBoxCellCSS=function isc_CheckboxItem_getTextBoxCellCSS(){var _1=isc.Canvas.$rr;var _2=this.invokeSuper(isc.CheckboxItem,"getTextBoxHeight");if(_2&&isc.isA.Number(_2))
_1+=this.$337+_2+this.$tk;return _1}
,isc.A.$29n=function isc_CheckboxItem__shouldAllowExpressions(){return false}
);isc.B._maxIndex=isc.C+12;isc.ClassFactory.defineClass("NativeCheckboxItem","FormItem");isc.A=isc.NativeCheckboxItem.getPrototype();isc.A.textBoxStyle="labelAnchor";isc.A.implementsPromptNatively=isc.screenReader;isc.A.$31k="CHECKBOX";isc.A.$23x=true;isc.A.showLabel=true;isc.A.$27o={onclick:isc.FormItem.$23m};isc.A=isc.NativeCheckboxItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$339=["<TABLE role='presentation' CELLSPACING=0 CELLPADDING=0 BORDER=0><TR>","<TD WIDTH=20><INPUT TYPE=",," NAME=",," ID=",,,,,,," handleNativeEvents=false ",,,," TABINDEX=",,(isc.Browser.isMoz?" STYLE='-moz-user-focus:":null),,"></TD>",,"</TR></TABLE>"];isc.A.$34a=["<TD CLASS='",,"' ALIGN=LEFT",,,,," style='",,"'>",,"</TD>"];isc.A.$34b=["<A HREF='javascript:void ",,".boxTitleClick()' ONMOUSEOVER='window.status = \"",,"\"; return true' ONMOUSEOUT='window.status = \"\"; return true' CLASS='",,"' title=\"",,"\">",,"</A>"];isc.B.push(isc.A.getInnerWidth=function isc_NativeCheckboxItem_getInnerWidth(_1,_2,_3,_4){if(!this.showLabel||this.showValueIconOnly){return 20}
return this.invokeSuper(isc.NativeCheckboxItem,"getInnerWidth",_1,_2,_3,_4)}
,isc.A.getElementHTML=function isc_NativeCheckboxItem_getElementHTML(_1){var _2=this.form.getID(),_3=this.getItemID(),_4=this.$339,_5=this.getAnchorTitle();_4[2]=this.$31k;_4[4]=this.getElementName();_4[6]=this.getDataElementId();_4[7]=this.$27a();if(this.value!=null){_4[8]=" VALUE='";_4[9]=this.value;_4[10]="'";if(this.containerWidget&&this.containerWidget.isPrinting){if(_1==this.value)_4[10]+=" CHECKED='true'"}}else{_4[8]=null;_4[9]=null;_4[10]=null}
if(this.isDisabled()||this.isReadOnly())_4[11]=" DISABLED";else _4[11]=null;if(this.implementsPromptNatively){if(this.prompt!=null){_4[13]=" TITLE='";_4[14]=this.prompt;_4[15]="'"}else{_4[13]=_4[14]=_4[15]=null}}
var _6=this.$269();_4[17]=_6;if(isc.Browser.isMoz){_4[19]=(_6>0?"normal;'":"ignore;'")}
if(this.showLabel&&!this.showValueIconOnly){var _7=this.getElementWidth(),_8=this.getInnerHeight();if(isc.isA.Number(_7))_7=Math.max(20,_7-20);if(isc.Browser.isSafari&&!this.isDisabled()&&(isc.Browser.isChrome?isc.Browser.safariVersion<535:isc.Browser.safariVersion<534.5))
{var _9=this.$34b;_9[1]=_3;_9[3]=this.prompt;_9[5]=this.getTextBoxStyle();_9[7]=this.prompt;_9[9]=_5;_5=_9.join(isc.emptyString)}
var _10=this.$34a;_10[1]=this.getTextBoxStyle();if(_7!=null){_10[3]=" WIDTH=";_10[4]=_7}else{_10[3]=null;_10[4]=null}
if(_8!=null){_10[5]=" HEIGHT=";_10[6]=_8}else{_10[5]=null;_10[6]=null}
if(this.wrap==false){_10[8]=this.$24z}else{_10[8]=null}
_10[10]=_5;_4[21]=_10.join(isc.emptyString)}else{_4[21]=null}
return _4.join(isc.emptyString)}
,isc.A.getOuterElement=function isc_NativeCheckboxItem_getOuterElement(_1,_2,_3){if(!this.isDrawn())return null;var _4;if(!this.$26x(this.$27e)&&!this.showPickerIcon){_4=this.getDataElement()}
return this.invokeSuper(isc.NativeCheckboxItem,"getOuterElement",_1,_2,_3)}
,isc.A.getTitleHTML=function isc_NativeCheckboxItem_getTitleHTML(){return""}
,isc.A.getAnchorTitle=function isc_NativeCheckboxItem_getAnchorTitle(_1,_2,_3,_4){return this.invokeSuper(isc.NativeCheckboxItem,"getTitleHTML",_1,_2,_3,_4)}
,isc.A.setElementValue=function isc_NativeCheckboxItem_setElementValue(_1){var _2=this.getDataElement();if(!_2)return null;return _2.checked=(_1&&_1!="false")}
,isc.A.getElementValue=function isc_NativeCheckboxItem_getElementValue(){var _1=this.getDataElement();if(!_1)return null;return(_1.checked==true)}
,isc.A.boxTitleClick=function isc_NativeCheckboxItem_boxTitleClick(){var _1=this.getDataElement();if(_1)_1.checked=!_1.checked;this.form.elementChanged(this.getItemID())}
,isc.A.setElementReadOnly=function isc_NativeCheckboxItem_setElementReadOnly(_1){this.$34c(_1)}
,isc.A.updateDisabled=function isc_NativeCheckboxItem_updateDisabled(){if(this.isDrawn())this.redraw()}
);isc.B._maxIndex=isc.C+10;isc.ClassFactory.defineClass("HeaderItem","FormItem");isc.A=isc.HeaderItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.shouldSaveValue=false;isc.A.defaultValue="Header";isc.A.height=20;isc.A.showTitle=false;isc.A.textBoxStyle="headerItem";isc.A.colSpan="*";isc.A.startRow=true;isc.A.endRow=true;isc.A.emptyDisplayValue="&nbsp;";isc.B.push(isc.A.isEditable=function isc_HeaderItem_isEditable(){return false}
);isc.B._maxIndex=isc.C+1;isc.defineClass("SectionItem","CanvasItem");isc.A=isc.SectionItem.getPrototype();isc.A.shouldSaveValue=false;isc.A.defaultValue="Section Header";isc.A.sectionVisible=true;isc.A.sectionExpanded=true;isc.A.sectionHeaderClass="SectionHeader";isc.A.canCollapse=true;isc.A.autoDestroy=true;isc.A.showTitle=false;isc.A.startRow=true;isc.A.endRow=true;isc.A.colSpan="*";isc.A.width="*";isc.A.height=20;isc.addGlobal("GroupItem",isc.SectionItem);isc.A=isc.SectionItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.init=function isc_SectionItem_init(){if(this.sectionVisible==false)this.sectionExpanded=false;this.Super("init",arguments)}
,isc.A.$31d=function isc_SectionItem__createCanvas(){var _1=isc.ClassFactory.getClass(this.sectionHeaderClass),_2={autoDraw:false,section:this,title:this.defaultValue,expanded:this.sectionExpanded,layout:this,height:this.height,canCollapse:this.canCollapse,canDrag:false,getCurrentCursor:function(){if(this.canvasItem&&this.canvasItem.cursor!=null)return this.canvasItem.cursor;return this.canCollapse==false?isc.Canvas.DEFAULT:isc.Canvas.HAND}};if(this.baseStyle!=null)_2.baseStyle=this.baseStyle;if(this.printStyleName!=null)_2.printStyleName=this.printStyleName;isc.addProperties(_2,this.canvasDefaults,this.canvasProperties);var _3=_1.create(_2);this.canvas=_3;this.Super("$31d",arguments)}
,isc.A.isEditable=function isc_SectionItem_isEditable(){return false}
,isc.A.isExpanded=function isc_SectionItem_isExpanded(){return this.sectionExpanded==true?true:false}
,isc.A.setValue=function isc_SectionItem_setValue(_1){this.Super("setValue",arguments);if(this.canvas)this.canvas.setTitle(this.getValue())}
,isc.A.sectionHeaderClick=function isc_SectionItem_sectionHeaderClick(){this.cellClick()}
,isc.A.cellClick=function isc_SectionItem_cellClick(){if(this.sectionExpanded){this.collapseSection()}else{this.expandSection()}}
,isc.A.expandSection=function isc_SectionItem_expandSection(){this.form.$20n(this);this.$34d();if(this.itemIds==null){this.logWarn("sectionItem defined with no items or itemIds");return}
for(var i=0;i<this.itemIds.length;i++){var _2=this.itemIds[i],_3=this.form.getItem(_2);if(_3==null){this.logWarn("expandSection: no such item: "+_2);continue}
if(_3.showIf==null&&_3.$34e!=null)_3.showIf=_3.$34e;_3.show(true)}
this.canvas.setExpanded(true);this.sectionExpanded=true;this.form.$194=true}
,isc.A.$34d=function isc_SectionItem__createItems(){if(this.items!=null&&!this.$34f){this.form.addItems(this.items,this.form.items.indexOf(this)+1);this.itemIds=[];for(var i=0;i<this.items.length;i++){this.itemIds[i]=this.items[i].getFieldName();if(this.itemIds[i]==null){this.logWarn("unable to include item:"+this.items[i]+" with no name in section")}}
this.$34f=true}}
,isc.A.addItem=function isc_SectionItem_addItem(_1,_2){this.form.addItems(_1,this.form.items.indexOf(this)+1+(_2||0));this.itemIds=this.itemIds||[];this.itemIds.add(_1.name)}
,isc.A.removeItem=function isc_SectionItem_removeItem(_1){var _2=(isc.isA.Object(_1)?_1.name:_1);this.itemIds.remove(_2);this.form.removeItems(_1)}
,isc.A.getItem=function isc_SectionItem_getItem(_1){return this.form.getItem(_1)}
,isc.A.collapseSection=function isc_SectionItem_collapseSection(){this.form.$20p(this);if(this.itemIds==null){if(this.items==null||this.$34f){this.logWarn("collapseSection with no sectionItem.itemIds");return}}else{for(var i=0;i<this.itemIds.length;i++){var _2=this.itemIds[i],_3=this.form.getItem(_2);if(_3==null){this.logWarn("collapseSection: no such item: "+_2);continue}
if(_3.showIf!=null)_3.$34e=_3.showIf;_3.hide()}}
this.canvas.setExpanded(false);this.sectionExpanded=false;this.form.$194=true}
,isc.A.$29n=function isc_SectionItem__shouldAllowExpressions(){return false}
);isc.B._maxIndex=isc.C+14;isc.ClassFactory.defineClass("HiddenItem","FormItem");isc.A=isc.HiddenItem.getPrototype();isc.A.showTitle=false;isc.A.cellStyle=null;isc.A.width=0;isc.A.height=0;isc.A.colSpan=0;isc.A.rowSpan=0;isc.A.$23x=true;isc.A.canFocus=false;isc.A=isc.HiddenItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.isEditable=function isc_HiddenItem_isEditable(){return false}
,isc.A.getInnerHTML=function isc_HiddenItem_getInnerHTML(_1){var _2=isc.StringBuffer.concat("<INPUT ID='",this.getDataElementId(),"' TYPE=HIDDEN NAME=",this.getElementName(),">");return _2.toString()}
,isc.A.getRowSpan=function isc_HiddenItem_getRowSpan(){return 0}
,isc.A.getColSpan=function isc_HiddenItem_getColSpan(){return 0}
,isc.A.shouldShowTitle=function isc_HiddenItem_shouldShowTitle(){return false}
,isc.A.getErrorHTML=function isc_HiddenItem_getErrorHTML(_1){this.logError("Error in hidden field '"+this.getFieldName()+"':\r  "+_1);return null}
,isc.A.isStartRow=function isc_HiddenItem_isStartRow(){return false}
,isc.A.isEndRow=function isc_HiddenItem_isEndRow(){return false}
);isc.B._maxIndex=isc.C+8;isc.ClassFactory.defineClass("StaticTextItem","FormItem");isc.A=isc.StaticTextItem.getPrototype();isc.A.height=null;isc.A.width=null;isc.A.wrap=true;isc.A.textBoxStyle="staticTextItem";isc.A.redrawOnShowIcon=false;isc.A.useShortDateFormat=false;isc.A.escapeHTML=null;isc.A.emptyDisplayValue="&nbsp;";isc.A=isc.StaticTextItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$sd="&nbsp;";isc.B.push(isc.A.mapValueToDisplay=function isc_StaticTextItem_mapValueToDisplay(_1,_2,_3,_4,_5){var _6=this.invokeSuper(isc.StaticTextItem,"mapValueToDisplay",_1,_2,_3,_4,_5);var _7=this.escapeHTML||this.outputAsHTML||this.asHTML;if(_7&&(_1==null||_1==isc.emptyString)&&_6==this.$sd)
{_7=false}
if(isc.isA.String(_6)&&_7){_6=_6.asHTML()}
return _6}
,isc.A.isEditable=function isc_StaticTextItem_isEditable(){return false}
,isc.A.$mb=function isc_StaticTextItem__canFocus(){if(this.canFocus!=null)return this.canFocus;return isc.screenReader}
);isc.B._maxIndex=isc.C+3;isc.ClassFactory.defineClass("LinkItem","TextItem");isc.A=isc.LinkItem.getPrototype();isc.A.wrap=false;isc.A.canEdit=false;isc.A.readOnlyTextBoxStyle="staticTextItem";isc.A=isc.LinkItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$249=["<DIV ID='",,"' "+isc.DynamicForm.$18o+"='",,,"' CLASS='",,"' STYLE='",,"'>",,"</DIV>"];isc.B.push(isc.A.$26r=function isc_LinkItem__writeOutFocusProxy(){return(this.isReadOnly()?false:this.Super("$26r",arguments))}
,isc.A.$34g=function isc_LinkItem__getLinkElement(){if(!this.isReadOnly())return this.Super("$34g",arguments);if(!this.isDrawn())return null;return(isc.Element.get(this.getID()+"$34h"))}
,isc.A.getFocusElement=function isc_LinkItem_getFocusElement(){return(this.isReadOnly()?this.$34g():this.Super("getFocusElement",arguments))}
,isc.A.hasDataElement=function isc_LinkItem_hasDataElement(){return!this.isReadOnly()}
,isc.A.$mb=function isc_LinkItem__canFocus(){return(this.isReadOnly()?true:this.Super("$mb",arguments))}
,isc.A.getTextBoxStyle=function isc_LinkItem_getTextBoxStyle(){if(!this.isReadOnly())return this.Super("getTextBoxStyle",arguments);if(this.$257()&&this.printTextBoxStyle){return this.$262(this.printTextBoxStyle)}
return(this.readOnlyTextBoxStyle?this.$262(this.readOnlyTextBoxStyle):null)}
,isc.A.$34i=function isc_LinkItem__linkClicked(_1){var _2=(this.destroyed||!this.isDrawn()||!this.isVisible()||this.isDisabled());if(!_2){_2=isc.EH.targetIsMasked(this.containerWidget);if(_2&&(this.form!=this.containerWidget)){_2=isc.EH.targetIsMasked(this.form)}}
if(!_2&&this.target=="javascript"){_2=true;this.handleClick()}
if(_2){if(!isc.Browser.isIE){_1.preventDefault()}
return false}
return true}
,isc.A.getReadOnlyHTML=function isc_LinkItem_getReadOnlyHTML(_1){var _2=this.getLinkHTML(_1);var _3=this.$249;_3[1]=this.$26k();_3[3]=this.getID();_3[6]=this.getTextBoxStyle();_3[8]=this.getTextBoxCSS();_3[10]=_2;return _3.join(isc.emptyString)}
,isc.A.getLinkHTML=function isc_LinkItem_getLinkHTML(_1){var _2=this.$2n(this._value);if(this.showValueIconOnly)return _2;if(_1!=null)_1=isc.iscToLocaleString(_1);if(_1==null)_1=isc.emptyString;var _3=this.linkTitle;if(_3==null)_3=_1;var _4=this.target;if(_4=="javascript"){_1="javascript:void"}
_1=isc.Canvas.linkHTML(_1,_3,_4,(this.getID()+"$34h"),this.getGlobalTabIndex(),this.accessKey," onclick='if(window."+this.getID()+") return "+this.getID()+".$34i(event);' "+isc.DynamicForm.$18p+"='"+isc.DynamicForm.$18r+"'");if(_2!=null)_1=_2+_1;return _1}
,isc.A.setElementValue=function isc_LinkItem_setElementValue(_1){if(!this.isReadOnly())return this.Super("setElementValue",arguments);if(this.isDrawn()){var _2=this.$6f();if(_2)_2.innerHTML=this.getLinkHTML(_1);this.$27f()}}
,isc.A.setLinkTitle=function isc_LinkItem_setLinkTitle(_1){this.linkTitle=_1;this.redraw()}
,isc.A.setElementReadOnly=function isc_LinkItem_setElementReadOnly(_1){this.redraw()}
);isc.B._maxIndex=isc.C+12;isc.ClassFactory.defineClass("PasswordItem","TextItem");isc.A=isc.PasswordItem.getPrototype();isc.A.$31k="PASSWORD";isc.ClassFactory.defineClass("RadioGroupItem","ContainerItem");isc.A=isc.RadioGroupItem.getPrototype();isc.A.itemHeight=20;isc.A.vertical=true;isc.A.prompt=null;isc.A.textBoxStyle="labelAnchor";isc.A.writeOutLabelTag=false;isc.A=isc.RadioGroupItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.init=function isc_RadioGroupItem_init(){if(this.disabledValues!=null){this.$34j={};for(var i=0;i<this.disabledValues.length;i++){var _2=this.disabledValues[i];this.$34j[_2]=true}}
return this.Super("init",arguments)}
,isc.A.$20z=function isc_RadioGroupItem__useHiddenDataElement(){return false}
,isc.A.setItems=function isc_RadioGroupItem_setItems(){var _1=this.getValueMap();if(!this.itemCache)this.itemCache={};var _2=[];if(isc.isAn.Array(_1)){for(var i=0;i<_1.length;i++){var _4=_1[i];_2.add(this.$34k(_4,_4))}}else{for(var _4 in _1){var _5=_1[_4];_2.add(this.$34k(_5,_4))}}
return this.Super("setItems",[_2])}
,isc.A.$34k=function isc_RadioGroupItem__getRadioItem(_1,_2){var _3=this.itemCache[_2+"|"+_1];if(_3){delete _3._value;delete _3.hasFocus;delete _3.disabled}else{var _4;if(this.itemPrompt){var _5=new RegExp("\\*","g");_4=this.itemPrompt.replace(_5,_1)}
var _6={type:"radio",name:"$34l"+_2,value:_2,getElementName:function(){return this.parentItem.getElementName()},title:_1,prompt:_4,height:this.itemHeight,$17u:this.vertical,textBoxStyle:this.textBoxStyle,wrap:this.wrap,updateValue:function(){this.parentItem.updatePreviousSelection(this.value);return this.Super("updateValue",arguments)},setDisabled:function(_7){this.parentItem.$34m(this.value,_7);return this.Super("setDisabled",arguments)},shouldSaveValue:false,suppressItemChanged:true};isc.addProperties(_6,this.itemProperties);_3=this.itemCache[_2+"|"+_1]=isc.FormItemFactory.makeItem(_6)}
if(this.$34j!=null&&this.$34j[_2]!=null){_3.disabled=this.$34j[_2]}
return _3}
,isc.A.getItemValue=function isc_RadioGroupItem_getItemValue(_1){var _2=_1.value;if(_2==this.getValue())return _2;return _1.unselectedValue}
,isc.A.itemForValue=function isc_RadioGroupItem_itemForValue(_1){return this["$34l"+_1]}
,isc.A.setValueDisabled=function isc_RadioGroupItem_setValueDisabled(_1,_2){if(this.$34j!=null&&this.$34j[_1]==_2)return;var _3=this.itemForValue(_1);if(_3&&this.items.contains(_3)){_3.setDisabled(_2)}else{this.$34j[_1]=_2}}
,isc.A.$34m=function isc_RadioGroupItem__itemDisabled(_1,_2){if(this.$34j==null)this.$34j={};this.$34j[_1]=_2}
,isc.A.getInnerHTML=function isc_RadioGroupItem_getInnerHTML(_1){this.setItems();return this.Super("getInnerHTML",arguments)}
,isc.A.isEditable=function isc_RadioGroupItem_isEditable(){return true}
,isc.A.setElementReadOnly=function isc_RadioGroupItem_setElementReadOnly(_1){}
,isc.A.setValue=function isc_RadioGroupItem_setValue(_1){this.$28y=true;var _2=(this.valueMap==null)||(isc.isAn.Array(this.valueMap)?!this.valueMap.contains(_1):!isc.propertyDefined(this.valueMap,_1));if(_1==null||_2){_1=this.getDefaultValue()}
if(this.items!=null){var _3=this.itemForValue(_1);if(_3!=null){this.itemForValue(_1).setValue(_1)}
if(this._value!=null&&this._value!=_1){var _4=this.itemForValue(this._value);if(_4)_4.setValue(null)}}
this.saveValue(_1)}
,isc.A.updatePreviousSelection=function isc_RadioGroupItem_updatePreviousSelection(_1){var _2=this.getValue();if(isc.isA.String(_1))_2=_2+"";if(_2==null||_1==_2||this.itemForValue(_2)==null)
{return}
this.itemForValue(_2).updateValue()}
,isc.A.updateValue=function isc_RadioGroupItem_updateValue(){var _1;for(var i=0;i<this.items.length;i++){_1=this.items[i].getValue();if(_1!=null)break}
if(_1==this._value)return;if(this.handleChange(_1,this._value)==false)return;_1=this.$29k;this.saveValue(_1);this.handleChanged(_1)}
,isc.A.setValueMap=function isc_RadioGroupItem_setValueMap(_1){this.Super("setValueMap",arguments);this.redraw()}
,isc.A.getHeight=function isc_RadioGroupItem_getHeight(){var _1=this.getValueMap(),_2=0;if(isc.isAn.Array(_1)){_2=_1.length}else{for(var _3 in _1){_2++}}
return _2*this.itemHeight}
);isc.B._maxIndex=isc.C+16;isc.ClassFactory.defineClass("RadioItem","NativeCheckboxItem");isc.A=isc.RadioItem.getPrototype();isc.A.$31k="RADIO";isc.A.value=true;isc.A.defaultValue=null;isc.A=isc.RadioItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.setElementValue=function isc_RadioItem_setElementValue(_1){var _2=this.getDataElement();if(!_2)return null;if(isc.isA.String(this.value))_1=(_1+"");return _2.checked=(this.value==_1)}
,isc.A.getElementValue=function isc_RadioItem_getElementValue(){var _1=this.getDataElement(),_2=this.value,_3=this.unselectedValue;if(!_1)return _3;return(_1.checked?_2:_3)}
,isc.A.boxTitleClick=function isc_RadioItem_boxTitleClick(){var _1=this.getDataElement();if(_1&&!_1.checked){_1.checked=true;this.form.elementChanged(this.getItemID())}}
,isc.A.mapValueToDisplay=function isc_RadioItem_mapValueToDisplay(_1){return _1}
,isc.A.mapDisplayToValue=function isc_RadioItem_mapDisplayToValue(_1){return _1}
,isc.A.setElementReadOnly=function isc_RadioItem_setElementReadOnly(_1){this.$299(!_1&&!this.isDisabled())}
);isc.B._maxIndex=isc.C+6;isc.ClassFactory.defineClass("ResetItem","ButtonItem");isc.A=isc.ResetItem.getPrototype();isc.A.title="Reset";isc.A=isc.ResetItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.handleClick=function isc_ResetItem_handleClick(){if(this.Super("handleClick",arguments)==false)return false;this.form.resetValues()}
);isc.B._maxIndex=isc.C+1;if(isc.ListGrid){isc.defineClass("DateItem","ContainerItem");isc.A=isc.DateItem;isc.A.mapCache={};isc.A.DAY_MONTH_YEAR="DMY";isc.A.MONTH_DAY_YEAR="MDY";isc.A.YEAR_MONTH_DAY="YMD";isc.A.DAY_MONTH="DM";isc.A.MONTH_DAY="MD";isc.A.YEAR_MONTH="YM";isc.A.MONTH_YEAR="MY";isc.A.DEFAULT_START_DATE=Date.createLogicalDate(1995,0,1);isc.A.DEFAULT_END_DATE=Date.createLogicalDate(2015,11,31);isc.A.DEFAULT_CENTURY_THRESHOLD=25;isc.A.chooserWidth=150;isc.A.chooserHeight=171;isc.A=isc.DateItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.textFieldDefaults={name:"dateTextField",type:"text",changeOnBlur:true,changeOnKeypress:true,changed:function(){this.isDirty=true},blur:function(){this.isDirty=false;if(this.parentItem)this.parentItem.updateValue()},shouldSaveValue:false,getTextBoxWidth:function(){if(this.parentItem)return this.parentItem.getTextBoxWidth();return this.Super("getTextBoxWidth",arguments)},$21i:true};isc.A.daySelectorDefaults={name:"daySelector",title:"Day",prompt:"Choose a day",type:"select",valueMap:"this.parentItem.getDayOptions()",shouldSaveValue:false,saveValue:function(){this.Super("saveValue",arguments);this.parentItem.updateValue()},getErrorWidth:function(){return 0},cssText:"padding-left:3px;",width:45,suppressItemChanged:true};isc.A.monthSelectorDefaults={name:"monthSelector",title:"Month",prompt:"Choose a month",type:"select",valueMap:"this.parentItem.getMonthOptions()",shouldSaveValue:false,saveValue:function(){this.Super("saveValue",arguments);this.parentItem.updateValue()},getErrorWidth:function(){return 0},width:55,suppressItemChanged:true};isc.A.yearSelectorDefaults={name:"yearSelector",title:"Year",prompt:"Choose a year",type:"select",valueMap:"this.parentItem.getYearOptions()",shouldSaveValue:false,saveValue:function(){this.Super("saveValue",arguments);this.parentItem.updateValue()},cssText:"padding-left:3px;",getErrorWidth:function(){return 0},width:60,suppressItemChanged:true};isc.A.width=150;isc.A.cellPadding=0;isc.A.useSharedPicker=true;isc.A.pickerConstructor="DateChooser";isc.A.pickerDefaults={width:isc.DateItem.chooserWidth,height:isc.DateItem.chooserHeight,border:"1px solid black;",showCancelButton:true,autoHide:true};isc.A.textAlign=isc.Canvas.RIGHT;isc.A.enforceDate=false;isc.A.invalidDateStringMessage="Invalid date";isc.A.showPickerIcon=true;isc.A.pickerIconWidth=20;isc.A.pickerIconHeight=20;isc.A.pickerIconSrc="[SKIN]/DynamicForm/DatePicker_icon.gif";isc.A.pickerIconHSpace=3;isc.A.pickerIconPrompt="Show Date Chooser";isc.A.pickerIconProperties={};isc.A.startDate=isc.DateItem.DEFAULT_START_DATE;isc.A.endDate=isc.DateItem.DEFAULT_END_DATE;isc.A.centuryThreshold=isc.DateItem.DEFAULT_CENTURY_THRESHOLD;isc.B.push(isc.A.getOperator=function isc_DateItem_getOperator(_1){if(!this.operator)return"equals";return this.operator}
);isc.B._maxIndex=isc.C+1;isc.A=isc.DateItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$34n="[01][0-9]";isc.A.$34o="[0-3]#";isc.A.$34p="####";isc.A.$34q="[0-2][0-9]:[0-6][0-9]";isc.A.$34r={"MDY":"toUSShortDate","DMY":"toEuropeanShortDate","YMD":"toJapanShortDate"};isc.A.selectorPadding=2;isc.B.push(isc.A.init=function isc_DateItem_init(){if(this.useTextField==null)this.useTextField=this.useMask||false;return this.Super("init",arguments)}
,isc.A.getSelectorFormat=function isc_DateItem_getSelectorFormat(){if(this.selectorFormat){return this.selectorFormat}else if(this.inputFormat&&isc.isA.String(this.inputFormat)){return this.inputFormat}else{var _1=Date.getInputFormat();if(isc.isA.String(_1))return _1;this.logInfo("DateItem selectorFormat unspecified - assuming US format");return"MDY"}}
,isc.A.getInputFormatMask=function isc_DateItem_getInputFormatMask(_1){var _2=this.maskDateSeparator||this.$34s();var _3;if(_1=="YMD"){_3=[this.$34p,_2,this.$34n,_2,this.$34o]}else if(_1=="DMY"){_3=[this.$34o,_2,this.$34n,_2,this.$34p]}else{_3=[this.$34n,_2,this.$34o,_2,this.$34p]}
if(isc.isA.DateTimeItem(this)){_3.addList([" ",this.$34q])}
return _3.join("")}
,isc.A.$34s=function isc_DateItem__getDefaultDateSeparator(){return Date.getDefaultDateSeparator()}
,isc.A.$34t=function isc_DateItem__getDefaultDateSeparatorRegex(){var _1=this.$34s();return new RegExp(_1,"/g")}
,isc.A.setItems=function isc_DateItem_setItems(_1){var _2=isc.DateItem,_3=this.getSelectorFormat();if(_1!=null&&_1.length!=0){this.logWarn("setItems() called for dateItem with itemList:"+_1+" - ignoring, and making use of default date fields")}
_1=this.items=[];if(this.useTextField){var _4={textAlign:this.textAlign,emptyDisplayValue:this.emptyDisplayValue,operator:this.operator,title:this.title};if(this.showHintInField){_4.showHintInField=this.showHintInField;_4.hint=this.hint;this.hint=null}
var _5={};if(this.useMask){var _6=this.getInputFormat();if(!_6)_6="MDY";var _7=this.getInputFormatMask(_6);_5.mask=_7;_5.maskSaveLiterals=true;_5.maskOverwriteMode=true;if(this.inputFormat){this.dateFormatter=this.$34r[_6]}}
var _8=isc.addProperties(_4,this.textFieldDefaults,_2.TEXT_FIELD,this.textFieldProperties,_5);_8.name="dateTextField";if(this.height&&(!this.textFieldProperties||!this.textFieldProperties.height))
{_8.height=this.getTextBoxHeight()}
_1.add(_8);var _9;this.daySelector=this.yearSelector=this.monthSelector=_9}else{for(var i=0;i<_3.length;i++){var _11=_3.charAt(i);var _12,_13,_14;if(_11=="D"){var _12;if(this.daySelectorProperties!=null){_12=isc.addProperties({},this.daySelectorDefaults,_2.DAY_SELECTOR,this.daySelectorProperties)}else{_12=isc.addProperties({},this.daySelectorDefaults,_2.DAY_SELECTOR)}
_12.name="daySelector";_1.add(_12)}else if(_11=="M"){var _13;if(this.monthSelectorProperties!=null){_13=isc.addProperties({},this.monthSelectorDefaults,_2.MONTH_SELECTOR,this.monthSelectorProperties)}else{_13=isc.addProperties({},this.monthSelectorDefaults,_2.MONTH_SELECTOR)}
_13.name="monthSelector";_1.add(_13)}else if(_11=="Y"){var _14;if(this.yearSelectorProperties!=null){_14=isc.addProperties({},this.yearSelectorDefaults,_2.YEAR_SELECTOR,this.yearSelectorProperties)}else{_14=isc.addProperties({},this.yearSelectorDefaults,_2.YEAR_SELECTOR)}
_14.name="yearSelector";_1.add(_14)}}}
this.Super("setItems",[_1]);if(this.useTextField){this.textField=this.dateTextField}}
,isc.A.getInnerWidth=function isc_DateItem_getInnerWidth(){if(this.useTextField){return this.Super("getInnerWidth",arguments)}
var _1=0,_2=0;if(this.daySelector){_2+=1;_1+=this.daySelector.width}
if(this.monthSelector){_2+=1;_1+=this.monthSelector.width}
if(this.yearSelector){_2+=1;_1+=this.yearSelector.width}
if(this.showPickerIcon)_1+=this.getPickerIconWidth();if(_2>0)_1+=(_2-1)*this.selectorPadding;return _1}
,isc.A.isEditable=function isc_DateItem_isEditable(){return true}
,isc.A.getEnteredValue=function isc_DateItem_getEnteredValue(){if(this.useTextField&&this.textField!=null){return this.textField.getEnteredValue()}
return this.getValue()}
,isc.A.setElementReadOnly=function isc_DateItem_setElementReadOnly(_1){}
,isc.A.setValue=function isc_DateItem_setValue(_1){this.$28y=true;var _2=false;if(_1==null){var _3=this.getDefaultValue();var _4;if(_3!==_4){_1=_3;_2=true}}
var _5=(isc.isA.Date(_1)&&isc.isA.Date(this._value)?(this.useLogicalDates()?(Date.compareLogicalDates(_1,this._value)==0):(Date.compareDates(_1,this._value)==0)):_1==this._value);var _6,_7;if(isc.is.emptyString(_1))_1=null;if(_1==null){_7=true;_6=_1}else{_6=this.parseDate(_1);if(_6==null){_7=true;_6=_1}}
if(_7){var _8;if(!this.useTextField){_8=true}else if(this.enforceDate&&_1!=null){var _9=this.dateTextField;_8=!this.$34u||!_9||(_9.getValue()!=_1)}
if(_8){this.logInfo("dateItem.setValue(): invalid date passed: '"+_1+"'.  Ignoring this value. Non date values are only supported "+" for dateItems where useTextField is true and enforceDate is false.");return false}}
if(!_7&&this.$34u){delete this.$34u;this.clearErrors();this.redraw()}
this.saveValue(_6,_2);this.$34v=true;if(this.useTextField){if(this.dateTextField){if(_5&&this.dateTextField.isDirty){this.dateTextField.setValue(this.dateTextField._value)}else{var _10=_7?_6:this.formatDate(_6);this.dateTextField.setValue(_10);delete this.dateTextField.isDirty}}}
if(this.daySelector)this.daySelector.setValue(_6.getDate());if(this.monthSelector)this.monthSelector.setValue(_6.getMonth());if(this.yearSelector)this.yearSelector.setValue(_6.getFullYear());delete this.$34v;return true}
,isc.A.$28w=function isc_DateItem__setHiddenDataElementValue(_1){var _2=this.$26f();if(_2!=null){if(isc.isA.Date(_1))_2.value=_1.toDBDate();else _2.value=_1}}
,isc.A.getCellHeight=function isc_DateItem_getCellHeight(){var _1=this.Super("getCellHeight",arguments);if(isc.Browser.isIE&&this.useTextField&&isc.isA.Number(_1))_1+=2;return _1}
,isc.A.elementChanged=function isc_DateItem_elementChanged(){return}
,isc.A.getCriteriaValue=function isc_DateItem_getCriteriaValue(){return this.parseDate(this.getValue())}
,isc.A.updateValue=function isc_DateItem_updateValue(){if(this.$34v)return;this.$34v=true;var _1;if(this.useTextField){this.dateTextField.updateValue();var _2=this.dateTextField.getValue(),_3;var _4=_2;if(_2==isc.emptyString||_2==null)_1=null;else{_1=this.parseDate(_2);if(_1==null){_3=true;_1=_2}else{_4=this.formatDate(_1);if(_2!=_4){this.dateTextField.setValue(_4)}}}
if(this._value==_1||(isc.isA.Date(this._value)&&(this.formatDate(this._value)==_4)))
{delete this.$34v;return}
if(this.enforceDate){if(this.$34u&&!_3){delete this.$34u;this.clearErrors();this.redraw()}else if(_3){this.logWarn("Invalid date string entered in date text field :"+_1);if(!this.$34u){this.$34u=true;this.setError(this.invalidDateStringMessage);this.redraw()}}}}else{_1=(this._value||this.getDefaultValue());_1=_1.duplicate();var _5,_6,_7;_5=(this.daySelector?this.daySelector.getValue():_1.getDate());_1.setDate(1);if(this.yearSelector){_7=this.yearSelector.getValue()
_1.setYear(_7)}
if(this.monthSelector){_6=this.monthSelector.getValue();_1.setMonth(_6)}
_1.setDate(_5);if(_6!=_1.getMonth()){_5=_5-_1.getDate();if(this.daySelector)this.daySelector.setValue(_5);_1.setMonth(_6);_1.setDate(_5)}}
delete this.$34v;if(this.compareValues(_1,this._value)==true)return false;if(this.handleChange(_1,this._value)==false)return;_1=this.$29k;this.saveValue(_1);this.handleChanged(_1)}
,isc.A.saveValue=function isc_DateItem_saveValue(_1){if(isc.isA.Date(_1)&&_1.logicalDate==null&&_1.logicalTime==null){if(this.useLogicalDates())_1.logicalDate=true}
return this.Super("saveValue",arguments)}
,isc.A.resetValue=function isc_DateItem_resetValue(){var _1=this.form.$20d[this.getFieldName()];if(isc.isA.Date(_1)&&isc.isA.Date(this._value))
_1=this._value.setTime(_1.getTime());this.setValue(_1)}
,isc.A.getItemValue=function isc_DateItem_getItemValue(_1,_2){if(isc.isAn.emptyObject(_2))_2=null;var _3=isc.isA.Date(_2),_4=isc.isA.Date(this._value);if(_2==this._value||(_3&&_4&&(Date.compareDates(_2,this._value)==0)))
{return _1.getValue()}
if(_1==this.dateTextField)return _3?this.formatDate(_2):_2;else if(_1==this.daySelector)return _3?_2.getDate():null;else if(_1==this.monthSelector)return _3?_2.getMonth():null;else if(_1==this.yearSelector)return _3?_2.getFullYear():null}
,isc.A.getDisplayValue=function isc_DateItem_getDisplayValue(){var _1=this.getValue();if(!isc.isA.Date(_1))return this.Super("getDisplayValue",arguments);if(this.useTextField||!this.items){return this.formatDate(_1)}else{if(!this.isDrawn()){if(this.yearSelector)this.yearSelector.setValue(_1.getFullYear());if(this.monthSelector)this.monthSelector.setValue(_1.getMonth());if(this.daySelector)this.daySelector.setValue(_1.getDate())}
return this.items.map("getDisplayValue").join(" ")}}
,isc.A.getDefaultValue=function isc_DateItem_getDefaultValue(){var _1=this.Super("getDefaultValue");if(!isc.isA.Date(_1)){var _2=this.parseDate(_1);if(isc.isA.Date(_2))_1=_2;else if(!this.useTextField||this.enforceDate){var _3;if(_1!=null){this.logWarn("Default DateItem value provided as:"+_1+". This is not recognized as a valid date - defaulting to a new date");_3=this.defaultValue==_1}
if(!this.useTextField)_1=this.$34w();if(_3)this.defaultValue=_1}}
return _1}
,isc.A.$34w=function isc_DateItem__getEmptyDate(){var _1=Date.createLogicalDate();return _1}
,isc.A.useLogicalDates=function isc_DateItem_useLogicalDates(){var _1=this.getType(),_2=isc.SimpleType.inheritsFrom(_1,"date"),_3=isc.SimpleType.inheritsFrom(_1,"datetime");return _1!=null&&_2&&!_3}
,isc.A.getStartDate=function isc_DateItem_getStartDate(){var _1=this.startDate;if(isc.isA.String(_1))_1=this.parseDate(this.startDate);if(!isc.isA.Date(_1)){this.logWarn("startDate was not in valid date format - using default start date");_1=isc.DateItem.DEFAULT_START_DATE}
return _1}
,isc.A.getEndDate=function isc_DateItem_getEndDate(){var _1=this.endDate;if(isc.isA.String(_1))_1=this.parseDate(this.endDate);if(!isc.isA.Date(_1)){this.logWarn("endDate was not in valid date format - using default end date");_1=isc.DateItem.DEFAULT_END_DATE}
return _1}
,isc.A.$mb=function isc_DateItem__canFocus(){if(this.canFocus!=null)return this.canFocus;return true}
,isc.A.focusInItem=function isc_DateItem_focusInItem(){if(!this.isVisible())return;if(this.useTextField){if(this.dateTextField)this.dateTextField.focusInItem()}else{var _1=this.getSelectorFormat(),_2=_1.charAt(0);if(_2=="D"&&this.daySelector)this.daySelector.focusInItem();if(_2=="M"&&this.monthSelector)this.monthSelector.focusInItem();if(_2=="Y"&&this.yearSelector)this.yearSelector.focusInItem()}}
,isc.A.setSelectionRange=function isc_DateItem_setSelectionRange(_1,_2){if(this.dateTextField)return this.dateTextField.setSelectionRange(_1,_2)}
,isc.A.getSelectionRange=function isc_DateItem_getSelectionRange(){if(this.dateTextField)return this.dateTextField.getSelectionRange()}
,isc.A.selectValue=function isc_DateItem_selectValue(){if(this.dateTextField)return this.dateTextField.selectValue()}
,isc.A.deselectValue=function isc_DateItem_deselectValue(_1){if(this.dateTextField)return this.dateTextField.deselectValue()}
,isc.A.getDayOptions=function isc_DateItem_getDayOptions(){var _1=this.getStartDate(),_2=this.getEndDate();var _3=1,_4=31;if(_1.getYear()==_2.getYear()&&_1.getMonth()==_2.getMonth())
{_3=_1.getDate()
_4=_2.getDate()}
var _5="day."+_3+"."+_4;if(isc.DateItem.mapCache[_5])return isc.DateItem.mapCache[_5];var _6=isc.DateItem.mapCache[_5]=[];for(var i=_3;i<=_4;i++)_6[i-_3]=i;return _6}
,isc.A.getMonthOptions=function isc_DateItem_getMonthOptions(){var _1=this.getStartDate(),_2=this.getEndDate();var _3=0,_4=11;if(_1.getYear()==_2.getYear()){_3=_1.getMonth()
_4=_2.getMonth()}
var _5="month."+_3+"."+_4;if(isc.DateItem.mapCache[_5])return isc.DateItem.mapCache[_5];var _6=isc.DateItem.mapCache[_5]={};var _7=Date.getShortMonthNames();for(;_3<=_4;_3++){_6[_3]=_7[_3]}
return _6}
,isc.A.getYearOptions=function isc_DateItem_getYearOptions(){var _1=this.getStartDate().getFullYear(),_2=this.getEndDate().getFullYear();var _3="year."+_1+"."+_2;if(isc.DateItem.mapCache[_3])return isc.DateItem.mapCache[_3];var _4=isc.DateItem.mapCache[_3]=[];for(var i=_1;i<=_2;i++){_4[i-_1]=i}
return _4}
,isc.A.parseDate=function isc_DateItem_parseDate(_1,_2){if(isc.isA.Date(_1))return _1;if(this.parseEditorValue!=null){var _3=this.parseEditorValue(_1,this.form,this);return _3}
if(_2==null)_2=this.getInputFormat();var _4=this.useLogicalDates();var _5=Date.parseInput(_1,_2,this.centuryThreshold,true,!_4);return _5}
,isc.A.formatDate=function isc_DateItem_formatDate(_1){if(this.formatEditorValue!=null){var _2=this.form?this.form.values:{};return this.formatEditorValue(_1,_2,this.form,this)}
if(!isc.isA.Date(_1))return _1;var _3=this.$28s(),_4=this.getType(),_5=isc.SimpleType.inheritsFrom(_4,"date"),_6=isc.SimpleType.inheritsFrom(_4,"datetime");return this.showTime?_1.toShortDatetime(_3,_6||!_5):_1.toShortDate(_3,_6||!_5)}
,isc.A.getInputFormat=function isc_DateItem_getInputFormat(){if(this.inputFormat)return this.inputFormat;var _1=this.$28s();return Date.mapDisplayFormatToInputFormat(_1)}
,isc.A.getPickerIcon=function isc_DateItem_getPickerIcon(_1,_2,_3,_4){var _5=this.invokeSuper(isc.DateItem,"getPickerIcon",_1,_2,_3,_4);if(_5.prompt==null)_5.prompt=this.pickerIconPrompt;return _5}
,isc.A.showPicker=function isc_DateItem_showPicker(){if(!this.form.$20f)this.updateValue();if(!this.picker){if(this.useSharedPicker)this.picker=isc.DateChooser.getSharedDateChooser();else{this.picker=isc[this.pickerConstructor].create(isc.addProperties({},this.pickerDefaults,this.pickerProperties,{border:"none",_generated:true,autoHide:true,showCancelButton:true}))}}
var _1=this.picker;var _2=_1.callingFormItem;if(_2!=this){if(_2)_2.ignore(_1,"dataChanged");this.observe(_1,"dataChanged","observer.pickerDataChanged(observed)");_1.callingFormItem=this;_1.callingForm=this.form;_1.locatorParent=this.form}
_1.startYear=this.getStartDate().getFullYear();_1.endYear=this.getEndDate().getFullYear();return this.Super("showPicker",arguments)}
,isc.A.getPickerRect=function isc_DateItem_getPickerRect(){var _1=this.getPageLeft(),_2=this.getPageTop(),_3=isc.DateItem.chooserWidth+3,_4=isc.DateItem.chooserHeight+3;_1+=Math.round((this.getVisibleWidth()-(this.getPickerIconWidth()/2))-
(_3/ 2));_2+=Math.round((this.getPickerIconHeight()/2)-(_4/ 2));return[_1,_2]}
,isc.A.pickerDataChanged=function isc_DateItem_pickerDataChanged(_1){var _2=_1.getData();var _3=_2.getFullYear(),_4=_2.getMonth(),_5=_2.getDate(),_6=_2.getTime();var _7=this.useLogicalDates();if(!_7){this.setToZeroTime(_2)}
this.$34v=true;if(this.useTextField){var _8=this.formatDate(_2);this.dateTextField.setValue(_8)}else{var _2=this._value||this.getDefaultValue(),_9;if(this.yearSelector)this.yearSelector.setValue(_3);else{_2.setFullYear(_3);_9=true}
if(this.monthSelector)this.monthSelector.setValue(_4);else{_2.setMonth(_4-1);_9=true}
if(this.daySelector)this.daySelector.setValue(_5);else{_2.setDate(_5);_9=true}
if(_9){this._value=_2}}
this.$34v=false;this.updateValue();if(!this.hasFocus)this.focusInItem();var _10=this.getErrors();if(_10&&_10.length>0)this.validate();if(this.validateOnExit||this.form.validateOnExit){this.$30d=true}}
,isc.A.setHint=function isc_DateItem_setHint(_1){if(this.useTextField&&this.showHintInField){this.dateTextField.setHint(_1)}else{this.Super("setHint",arguments)}}
,isc.A.getPickerData=function isc_DateItem_getPickerData(){var _1=this.getValue();if(_1!=null&&isc.isA.Date(_1))return _1;return this.getDefaultChooserDate()}
,isc.A.getDefaultChooserDate=function isc_DateItem_getDefaultChooserDate(){return this.defaultChooserDate}
,isc.A.$29n=function isc_DateItem__shouldAllowExpressions(){if(this.useTextField){return this.Super("$29n",arguments)}else{return false}}
,isc.A.propertyChanged=function isc_DateItem_propertyChanged(_1){if(_1=="useTextField"||_1=="useMask")this.setItems()}
);isc.B._maxIndex=isc.C+46}
if(isc.ListGrid){isc.defineClass("DateTimeItem","DateItem");isc.A=isc.DateTimeItem.getPrototype();isc.A.useTextField=true;isc.A.showTime=true}
isc.ClassFactory.defineClass("SpacerItem","FormItem");isc.A=isc.SpacerItem.getPrototype();isc.A.shouldSaveValue=false;isc.A.showTitle=false;isc.A.width=20;isc.A.height=20;isc.A.showHint=false;isc.A.showIcons=false;isc.A=isc.SpacerItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.isEditable=function isc_SpacerItem_isEditable(){return false}
,isc.A.getElementHTML=function isc_SpacerItem_getElementHTML(_1){return isc.Canvas.spacerHTML(this.width,this.height)}
,isc.A.shouldShowTitle=function isc_SpacerItem_shouldShowTitle(){return false}
);isc.B._maxIndex=isc.C+3;isc.ClassFactory.defineClass("RowSpacerItem","SpacerItem");isc.A=isc.RowSpacerItem.getPrototype();isc.A.showTitle=false;isc.A.colSpan="*";isc.A.startRow=true;isc.A.endRow=true;isc.A.width=20;isc.A.height=20;isc.ClassFactory.defineClass("SubmitItem","ButtonItem");isc.A=isc.SubmitItem.getPrototype();isc.A.title="Submit";isc.A=isc.SubmitItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.handleClick=function isc_SubmitItem_handleClick(){if(this.Super("handleClick",arguments)==false)return false;this.form.submit();this.form.completeEditing()}
);isc.B._maxIndex=isc.C+1;isc.ClassFactory.defineClass("CancelItem","ButtonItem");isc.A=isc.CancelItem.getPrototype();isc.A.title="Cancel";isc.A=isc.CancelItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.handleClick=function isc_CancelItem_handleClick(){if(this.Super("handleClick",arguments)==false)return false;this.form.cancelEditing()}
);isc.B._maxIndex=isc.C+1;isc.ClassFactory.defineClass("TextAreaItem","FormItem");isc.A=isc.TextAreaItem;isc.A.OFF="OFF";isc.A.SOFT="SOFT";isc.A.VIRTUAL="SOFT";isc.A.ON="HARD";isc.A.HARD="HARD";isc.A.PHYSICAL="HARD";isc.A=isc.TextAreaItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.wrap=isc.TextAreaItem.VIRTUAL;isc.A.width=150;isc.A.height=100;isc.A.textBoxStyle="textItem";isc.A.redrawOnShowIcon=false;isc.A.clipValue=true;isc.A.$23x=true;isc.A.$26l=true;isc.A.emptyStringValue=null;isc.A.lineBreakValue="\n";isc.A.iconVAlign=isc.Canvas.TOP;isc.A.$27o={onmousedown:(isc.Browser.isIE?function(){var _1=this,_2=isc.DynamicForm.$ot(_1),_3=_2.item;if(_3)_3.$30f()}:null)};isc.A.supportsSelectionRange=true;isc.A.printFullText=true;isc.B.push(isc.A.getEnteredValue=function isc_TextAreaItem_getEnteredValue(){return this.getElementValue()}
);isc.B._maxIndex=isc.C+1;isc.A=isc.TextAreaItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.allowNativeResize=false;isc.A.minHeight=16;isc.A.applyStaticTypeFormat=false;isc.B.push(isc.A.getTextBoxCellCSS=function isc_TextAreaItem_getTextBoxCellCSS(){return this.$24z}
,isc.A.$252=function isc_TextAreaItem__sizeTextBoxAsContentBox(){return isc.Browser.isStrict}
,isc.A.$21v=function isc_TextAreaItem__willHandleInput(){return!isc.Browser.isIE}
,isc.A.setElementReadOnly=function isc_TextAreaItem_setElementReadOnly(_1){this.$29m(_1)}
,isc.A.getElementHTML=function isc_TextAreaItem_getElementHTML(_1,_2){var _3=this.form,_4=_3.getID(),_5=this.getItemID(),_6=isc.StringBuffer.create(),_7=this.$2n(_2);if(_7!=null)_6.append(_7);if(!this.showValueIconOnly){if(!this.printFullText||!this.$257()){_6.append("<TEXTAREA NAME=",this.getElementName()," ID=",this.getDataElementId(),this.$27a(),this.getElementStyleHTML(),(this.isDisabled()?" DISABLED ":""),(this.$28z()!="native"?" AUTOCOMPLETE=OFF ":""),((isc.Browser.isMoz||isc.Browser.isSafari)?(this.getBrowserSpellCheck()?" spellcheck=true":" spellcheck=false"):null)," WRAP=",this.wrap," TABINDEX=",this.$269(),(this.showTitle==false&&this.accessKey!=null?" ACCESSKEY="+this.accessKey:""),(this.$21v?" ONINPUT='"+this.getID()+".$30i()'":null),(this.readOnly||this.isInactiveHTML()?" READONLY=TRUE":null)," handleNativeEvents=false>",(this.isInactiveHTML()?_1:null),"</TEXTAREA>")}else{if(_1==null)_1="";_6.append("<DIV style='",(isc.isA.Number(this.width)?"width:"+this.width+"px;":null),"' class='",this.getTextBoxStyle(),"'>",_1.asHTML(),"</DIV>")}}
return _6.release()}
,isc.A.handleMouseMove=function isc_TextAreaItem_handleMouseMove(){var _1=this.Super("handleMouseMove",arguments);if(_1==false||!this.allowNativeResize)return false;if(isc.EH.mouseIsDown()&&this.$34x==null){this.$34x=isc.Page.setEvent("idle",this.getID()+".$34y()")}}
,isc.A.$34y=function isc_TextAreaItem__checkForElementResize(){var _1=false;var _2=this.getDataElement();if(_2){if(_2.offsetWidth!=this.getTextBoxWidth())_1=true;if(_2.offsetHeight!=this.getTextBoxHeight())_1=true}
if(_1)this.$34z();if(!isc.EH.mouseIsDown()){isc.Page.clearEvent("idle",this.$34x);this.$34x=null}}
,isc.A.$34z=function isc_TextAreaItem__nativeElementResize(){var _1=this.containerWidget;if(_1)_1.$yg("Native textarea resize")}
,isc.A.$23h=function isc_TextAreaItem__nativeElementFocus(_1,_2){var _3=this.Super("$23h",arguments);this.$29g();this.$31t=this.getEnteredValue();return _3}
,isc.A.$23k=function isc_TextAreaItem__nativeElementBlur(_1,_2){var _3=this.Super("$23k",arguments);this.form.elementChanged(this);if(this.$31t==null||this.$31t!=this.getEnteredValue())
{var _4=this.getValue();if(this.mapValueToDisplay){_4=this.mapValueToDisplay(_4)}
this.setElementValue(_4)}
if(this.showHintInField){var _5;var _4=this.getElementValue();if(_4===_5||_4==null||isc.is.emptyString(_4)){this.$29c()}}
return _3}
,isc.A.getElementStyleHTML=function isc_TextAreaItem_getElementStyleHTML(){var _1=this.getTextBoxWidth(),_2=this.getTextBoxHeight();return isc.StringBuffer.concat(" CLASS='"+this.getTextBoxStyle(),(isc.Browser.isMoz&&isc.isA.String(this.wrap)&&this.wrap.toLowerCase()!="off"?"' ROWS=10 COLS=10":"'")," STYLE='",this.getElementCSSText(_1,_2),"' ")}
,isc.A.getElementCSSText=function isc_TextAreaItem_getElementCSSText(_1,_2){if(isc.isA.Number(_1)&&_1<=0)_1=1;if(isc.isA.Number(_2)&&_2<this.minHeight)_2=this.minHeight;return isc.StringBuffer.concat(this.allowNativeResize?null:"resize:none;",(isc.Browser.isIE?"margin-top:-1px;margin-bottom:-1px;margin-left:0px;margin-right:0px;":"margin:0px;"),(isc.isA.Number(_1)?"WIDTH:"+_1+"px;":""),(isc.isA.Number(_2)?"HEIGHT:"+_2+"px;":""),(this.textAlign?"text-align:"+this.textAlign+";":""),(isc.Browser.isMoz?"-moz-user-focus:"+(this.$269()>0?"normal;":"ignore;"):""))}
,isc.A.mapValueToDisplay=function isc_TextAreaItem_mapValueToDisplay(_1,_2,_3,_4,_5){var _6=this.invokeSuper(isc.TextAreaItem,"mapValueToDisplay",_1,_2,_3,_4,_5);if(_6==null)_6=isc.emptyString;return _6}
,isc.A.mapDisplayToValue=function isc_TextAreaItem_mapDisplayToValue(_1){if(!this.applyStaticTypeFormat&&this.parseEditorValue!=null){return this.parseEditorValue(_1,this.form,this)}
var _2=this.$28u(_1);if(isc.is.emptyString(_2))_2=this.emptyStringValue;return _2}
,isc.A.setValue=function isc_TextAreaItem_setValue(_1){this.$29g();var _2;if(_1!==_2&&(_1==null||isc.is.emptyString(_1)))
this.emptyStringValue=_1;delete this.$340;_1=this.Super("setValue",arguments);if(!this.hasFocus&&this.showHint&&this.showHintInField&&this.getHint()){if(_1===_2||_1==null||isc.is.emptyString(_1)){this.$29c()}}
return _1}
,isc.A.updateValue=function isc_TextAreaItem_updateValue(){this.$340=true;return this.Super("updateValue",arguments)}
,isc.A.getValue=function isc_TextAreaItem_getValue(){var _1=this.Super("getValue",arguments);if(this.$340&&isc.isA.String(_1)){if(!this.$341)
this.$341=new RegExp("(\\r\\n|[\\r\\n])","g");_1=""+_1;_1=_1.replace(this.$341,this.lineBreakValue)}
return _1}
,isc.A.getScrollHeight=function isc_TextAreaItem_getScrollHeight(){var _1=this.$6f();if(_1==null)return this.getHeight();return _1.scrollHeight}
,isc.A.getScrollWidth=function isc_TextAreaItem_getScrollWidth(){var _1=this.$6f();if(_1==null)return this.getWidth();return _1.scrollWidth}
,isc.A.$342=function isc_TextAreaItem__hscrollOn(){var _1=this.$6f();return _1&&_1.scrollWidth>_1.clientWidth}
,isc.A.$343=function isc_TextAreaItem__vscrollOn(){var _1=this.$6f();return _1&&_1.scrollHeight>_1.clientHeight}
,isc.A.getScrollTop=function isc_TextAreaItem_getScrollTop(){var _1=this.$6f();if(_1==null)return 0;return _1.scrollTop}
,isc.A.getScrollLeft=function isc_TextAreaItem_getScrollLeft(){var _1=this.$6f();if(_1==null)return 0;return _1.scrollLeft}
,isc.A.scrollTo=function isc_TextAreaItem_scrollTo(_1,_2){var _3=this.$6f();if(_3==null)return;if(_1!=null)_3.scrollLeft=_1;if(_2!=null)_3.scrollTop=_2}
,isc.A.scrollToTop=function isc_TextAreaItem_scrollToTop(){this.scrollTo(null,0)}
,isc.A.scrollToBottom=function isc_TextAreaItem_scrollToBottom(){var _1=this.getScrollHeight()-this.getInnerHeight();if(_1>=0){if(this.$342())_1+=this.form.getScrollbarSize();this.scrollTo(null,_1)}}
);isc.B._maxIndex=isc.C+26;isc.ClassFactory.defineClass("AutoFitTextAreaItem","TextAreaItem");isc.A=isc.AutoFitTextAreaItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.supportsSelectionRange=true;isc.B.push(isc.A.getTestBox=function isc_AutoFitTextAreaItem_getTestBox(_1){var _2=this.mapValueToDisplay(this.getValue());var _3=isc.AutoFitTextAreaItem;if(!_3.$344){_3.$344=isc.Canvas.create({autoDraw:true,overflow:"hidden",left:0,top:-100,contents:["<textarea ID='isc_autoFitTextArea_sizeTester'","style='overflow:hidden;",(isc.Browser.isIE?"margin-top:-1px;margin-bottom:-1px;margin-left:0px;margin-right:0px;":"margin:0px;"),"'></textarea>"].join("")})}
var _4=isc.Element.get("isc_autoFitTextArea_sizeTester");if(_3.currentItem!=this||_1){_4.className=this.getTextBoxStyle();if(isc.Browser.isMoz){if(isc.isA.String(this.wrap)&&this.wrap.toLowerCase()!="off"){_4.rows=10;_4.cols=10}else{_4.rows="";_4.cols=""}}
_4.setAttribute("wrap",this.wrap);_4.style.width=this.getTextBoxWidth();_4.style.height=this.getTextBoxHeight();_4.style.textAlign=this.textAlign||"";_4.cssText=this.getElementCSSText(this.getTextBoxWidth(),this.getTextBoxHeight());_3.currentItem=this}
_4.value=_2;var _5=_4.scrollHeight;return _4}
,isc.A.getScrollHeight=function isc_AutoFitTextAreaItem_getScrollHeight(_1){var _2=this.getTestBox(_1);return _2.scrollHeight}
,isc.A.getScrollWidth=function isc_AutoFitTextAreaItem_getScrollWidth(_1){var _2=this.getTestBox(_1);return _2.scrollWidth}
,isc.A.getElementCSSText=function isc_AutoFitTextAreaItem_getElementCSSText(_1,_2){var _3=this.Super("getElementCSSText",arguments);_3+="overflow:hidden;"
return _3}
,isc.A.$345=function isc_AutoFitTextAreaItem__getTextBoxHPadding(){if(this.$346!=null)return this.$346;var _1=this.getDataElement();if(!_1)return 0;var _2=parseInt(isc.Element.getComputedStyleAttribute(_1,"paddingLeft")),_3=parseInt(isc.Element.getComputedStyleAttribute(_1,"paddingRight")),_4=(isc.isA.Number(_2)?_2:0)+(isc.isA.Number(_3)?_3:0);this.$346=_4;return _4}
,isc.A.$347=function isc_AutoFitTextAreaItem__getTextBoxVPadding(){if(this.$348!=null)return this.$348;var _1=this.getDataElement();if(!_1)return 0;if(isc.Browser.isIE&&_1.currentStyle==null)return 0;var _2=parseInt(isc.Element.getComputedStyleAttribute(_1,"paddingTop")),_3=parseInt(isc.Element.getComputedStyleAttribute(_1,"paddingBottom")),_4=(isc.isA.Number(_2)?_2:0)+(isc.isA.Number(_3)?_3:0);this.$348=_4;return _4}
,isc.A.updateSize=function isc_AutoFitTextAreaItem_updateSize(_1){var _2=this.getDataElement();if(!_2)return;var _3,_4;var _5=this.getTextBoxHeight(),_6=this.$347(),_7=this.getScrollHeight(_1),_8=_2.offsetHeight;if((_7+_6)>_8){_2.style.height=_7+_6;_4=true}else if((_7+_6)<_8&&_8>_5){if((_7+_6)<_8){_3=true;_2.style.height=Math.max(_7+_6,_5)}
_4=true}
var _9=this.getTextBoxWidth(),_10=isc.Browser.isIE?0:this.$345(),_11=this.getScrollWidth(_1),_12=_2.offsetWidth;if((_11+_10)>_12){_2.style.width=(_11+_10);_4=true}else if((_11+_10)<_12&&_12>_9){_2.style.width=Math.max(_9,_11+_10);_3=true
_4=true}
if(_3)this.containerWidget.$yf=true;if(_4)this.adjustOverflow("Updated size to fit content")}
,isc.A.handleChanged=function isc_AutoFitTextAreaItem_handleChanged(){this.updateSize();return this.Super("handleChanged",arguments)}
,isc.A.drawn=function isc_AutoFitTextAreaItem_drawn(){this.Super("drawn",arguments);delete this.$346;delete this.$348;this.updateSize(true)}
,isc.A.redrawn=function isc_AutoFitTextAreaItem_redrawn(){this.Super("redrawn",arguments);delete this.$346;delete this.$348;this.updateSize(true)}
);isc.B._maxIndex=isc.C+10;isc.ClassFactory.defineClass("TimeItem","TextItem");isc.A=isc.TimeItem;isc.A.DEFAULT_TIME="00:00:00";isc.A=isc.TimeItem.getPrototype();isc.A.changeOnBlur=true;isc.A.changeOnKeypress=false;isc.A.width=100;isc.A.timeFormatter="toShort24HourTime";isc.A.allowEmptyValue=true;isc.A=isc.TimeItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.short24TimeFormat="HH:MM";isc.A.shortTimeFormat="HH:MM [am|pm]";isc.A.long24TimeFormat="HH:MM:SS";isc.A.longTimeFormat="HH:MM:SS [am|pm]";isc.A.formatterMap={toTime:{mask:"[0-1]#:[0-6]#:[0-6]# [ap]m",formatter:"toPaddedTime"},to24HourTime:{mask:"[0-2]#:[0-6]#:[0-6]#",formatter:"toPadded24HourTime"},toPaddedTime:{mask:"[0-1]#:[0-6]#:[0-6]# [ap]m"},toPadded24HourTime:{mask:"[0-2]#:[0-6]#:[0-6]#"},toShortTime:{mask:"[0-1]#:[0-6]# [ap]m",formatter:"toShortPaddedTime"},toShort24HourTime:{mask:"[0-2]#:[0-6]#",formatter:"toShortPadded24HourTime"},toShortPaddedTime:{mask:"[0-1]#:[0-6]# [ap]m"},toShortPadded24HourTime:{mask:"[0-2]#:[0-6]#"}};isc.B.push(isc.A.getHint=function isc_TimeItem_getHint(){if(!this.showHint)return"";if(this.hint!=null)return this.hint;var _1=this.$28r();switch(_1){case"to24HourTime":case"toPadded24HourTime":return this.long24TimeFormat;case"toTime":case"toPaddedTime":return this.longTimeFormat;case"toShort24HourTime":case"toShortPadded24HourTime":return this.short24TimeFormat;case"toShortTime":case"toShortPaddedTime":return this.shortTimeFormat}
return""}
,isc.A.getDefaultValue=function isc_TimeItem_getDefaultValue(){var _1=this.defaultValue;if(!_1&&!this.allowEmptyValue)_1=isc.TimeItem.DEFAULT_TIME;if(_1&&!isc.isA.Date(_1))
_1=isc.Time.parseInput(_1);return _1}
,isc.A.mapValueToDisplay=function isc_TimeItem_mapValueToDisplay(_1){if(this.allowEmptyValue&&_1==null)return isc.emptyString;if(!isc.isA.Date(_1))_1=isc.Time.parseInput(_1);var _2=isc.Time.format(_1,this.$28r());_2=this.Super("mapValueToDisplay",_2);return _2}
,isc.A.mapDisplayToValue=function isc_TimeItem_mapDisplayToValue(_1){var _2=this.Super("mapDisplayToValue",arguments);if((_2==null||isc.isAn.emptyString(_2))&&this.allowEmptyValue){_2=null}else{_2=isc.Time.parseInput(_2)}
return _2}
,isc.A.updateValue=function isc_TimeItem_updateValue(){this.Super("updateValue",arguments);if(!this.mask){this.setElementValue(this.mapValueToDisplay(this.getValue()))}}
,isc.A.setValue=function isc_TimeItem_setValue(_1){if(isc.isA.String(_1))_1=isc.Time.parseInput(_1);return this.Super("setValue",[_1])}
,isc.A.compareValues=function isc_TimeItem_compareValues(_1,_2){if(_1==_2)return true;if(isc.isA.Date(_1)&&isc.isA.Date(_2)&&isc.Time.compareTimes(_1,_2))return true;return false}
,isc.A.init=function isc_TimeItem_init(){if(this.useMask){var _1=this.formatterMap[this.$28r()];if(!_1){this.useMask=false;this.mask=null;this.logWarn("Mask will not be used because timeFormatter "+this.$28r()+" is not recognized")}else{if(_1.formatter)this.timeFormatter=_1.formatter;this.mask=_1.mask}
if(this.mask){this.maskSaveLiterals=true;this.maskOverwriteMode=true}}else if(this.mask){this.mask=null}
this.Super("init",arguments)}
,isc.A.setMask=function isc_TimeItem_setMask(_1){this.logWarn("setMask: custom mask ignored")}
);isc.B._maxIndex=isc.C+9;isc.ClassFactory.defineClass("ToolbarItem","CanvasItem");isc.A=isc.ToolbarItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.canFocus=true;isc.A.shouldSaveValue=false;isc.A.showTitle=false;isc.A.buttonSpace=4;isc.A.startRow=true;isc.A.endRow=true;isc.A.colSpan="*";isc.A.canvasConstructor=isc.Toolbar;isc.A.vertical=false;isc.A.buttonConstructor=isc.AutoFitButton;isc.B.push(isc.A.isEditable=function isc_ToolbarItem_isEditable(){return false}
);isc.B._maxIndex=isc.C+1;isc.A=isc.ToolbarItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.$31d=function isc_ToolbarItem__createCanvas(){var _1=(this.buttons||this.items||[]),_2=this.buttonProperties||{};isc.addProperties(_2,{handleActivate:function(){var _3=this.parentElement.canvasItem,_4=_3.form;if(this.click!=null)return this.click(_4,_3)}});if(this.buttonBaseStyle&&!_2.baseStyle){_2.baseStyle=this.buttonBaseStyle}
if(this.buttonTitleStyle&&!_2.titleStyle){_2.titleStyle=this.buttonTitleStyle}
this.$349(_1);this.canvas={overflow:isc.Canvas.VISIBLE,buttons:_1,membersMargin:this.buttonSpace,vertical:this.vertical,buttonProperties:_2};var _5=this.height,_6=this.width;if(!isc.isA.Number(_6)&&this.$172)
_6=isc.isA.Number(this.$172[0])?this.$172[0]:null;if(!isc.isA.Number(_5)&&this.$172)
_5=isc.isA.Number(this.$172[1])?this.$172[1]:null;if(_5)this.canvas.height=_5;if(_6)this.canvas.width=_6;if(this.buttonConstructor!=null)
this.canvas.buttonConstructor=this.buttonConstructor;return this.Super("$31d",arguments)}
,isc.A.$349=function isc_ToolbarItem__updateButtons(_1){if(!_1||_1.length==0)return;for(var i=0;i<_1.length;i++){if(_1[i].click&&isc.isA.String(_1[i].click)){_1[i].click=isc.Func.expressionToFunction("form,item",_1[i].click)}}}
,isc.A.getButton=function isc_ToolbarItem_getButton(_1){return isc.Class.getArrayItem(_1,this.buttons,"name")}
,isc.A.addButton=function isc_ToolbarItem_addButton(_1,_2){this.buttons=this.buttons||[];this.buttons.addAt(_1,_2);this.setButtons(this.buttons)}
,isc.A.removeButton=function isc_ToolbarItem_removeButton(_1){var _2=isc.Class.getArrayItemIndex(_1,this.buttons,"name");if(_2!=-1){this.buttons.removeAt(_2);this.setButtons(this.buttons)}}
,isc.A.setButtons=function isc_ToolbarItem_setButtons(_1){this.$349(_1);this.buttons=_1;if(!this.canvas)return;this.canvas.setButtons(_1)}
,isc.A.setItems=function isc_ToolbarItem_setItems(_1){return this.setButtons(_1)}
);isc.B._maxIndex=isc.C+7;isc.ClassFactory.defineClass("UploadItem","TextItem");isc.A=isc.UploadItem.getPrototype();isc.A.$31k="FILE";isc.A.$27o={onchange:isc.FormItem.$23m};isc.A=isc.UploadItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.elementHeight=(isc.Browser.isMoz?18:null);isc.B.push(isc.A.getTextBoxWidth=function isc_UploadItem_getTextBoxWidth(){if(isc.Browser.isMoz)return null;return this.Super("getTextBoxWidth",arguments)}
,isc.A.getTextBoxHeight=function isc_UploadItem_getTextBoxHeight(){if(this.elementHeight)return this.elementHeight;return this.Super("getTextBoxHeight",arguments)}
,isc.A.$26c=function isc_UploadItem__getEventMaskWidth(){var _1=this.getElementWidth();if(!isc.isA.Number(_1))_1=185;return _1}
,isc.A.$20l=function isc_UploadItem__updateValue(_1){_1=this.mapDisplayToValue(_1);if(_1==this._value)return true;var _2=this.handleChange(_1,this._value);if(this.$29k!=_1){this.logWarn("Upload Items do not support programmatically modifying the value entered "+"by the user. Ignoring attempt to update from change handler")}
this.saveValue(this.mapDisplayToValue(this.getElementValue()));return _2}
,isc.A.redrawn=function isc_UploadItem_redrawn(){this.Super("redrawn",arguments);this.updateValue(this.getElementValue())}
,isc.A.setElementReadOnly=function isc_UploadItem_setElementReadOnly(_1){this.$299(!_1&&!this.isDisabled())}
,isc.A.setValue=function isc_UploadItem_setValue(_1){var _2=this.getValue();if(_1==null||isc.isAn.emptyString(_1)){if(_2==null||isc.isAn.emptyString(_2))return;return this.Super("setValue",arguments)}
if(_1==_2){this.logInfo("Attempting to set the value for an upload form item to:"+_1+" This is the current value for the item so no action to take, but setting "+"UploadItems to a new value is always disabled.");return}
this.logWarn("Attempting to set the value for an upload form item. This is disallowed "+"for security reasons - returning the current value of the form item")}
,isc.A.$23n=function isc_UploadItem__handleElementChanged(){this.Super("$23n",arguments);this.checkForImplicitSave()}
,isc.A.setElementValue=function isc_UploadItem_setElementValue(_1){if(_1==null||isc.isAn.emptyString(_1)){if(isc.Browser.isIE){this.redraw();return}
return this.Super("setElementValue",arguments)}
this.logInfo("Attempting to set the value for an upload form item. This is disallowed "+"for security reasons - returning the current value of the form item");return this.getElementValue()}
,isc.A.refreshDisplayValue=function isc_UploadItem_refreshDisplayValue(){}
,isc.A.$29n=function isc_UploadItem__shouldAllowExpressions(){return false}
);isc.B._maxIndex=isc.C+11;if(isc.Browser.isSafari){isc.A=isc.UploadItem.getPrototype();isc.A.colSpan="*";isc.A.startRow=true;isc.A.endRow=true;isc.A.width=300}
isc.defineClass("ComboBoxItem","TextItem","PickList");isc.A=isc.ComboBoxItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.autoSizePickList=true;isc.A.showPickerIcon=true;isc.A.pickerIconWidth=15;isc.A.pickerIconSrc="[SKIN]/DynamicForm/ComboBoxItem_PickButton_icon.gif";isc.A.pickerIconProperties={tabIndex:-1,showOver:true};isc.A.modalPickList=false;isc.A.showPickListOnKeypress=true;isc.A.$35a="Arrow_Up";isc.A.$35b="Arrow_Down";isc.A.$35c="Page_Up";isc.A.$35d="Page_Down";isc.A.$kt="Escape";isc.A.$18n="Enter";isc.A.$kw="Tab";isc.A.addUnknownValues=true;isc.A.filterDisplayValue=true;isc.A.autoFetchData=false;isc.B.push(isc.A.drawn=function isc_ComboBoxItem_drawn(_1,_2,_3,_4){this.invokeSuper(isc.ComboBoxItem,"drawn",_1,_2,_3,_4);if(this.autoFetchData&&this.$32x()){this.filterWithValue=false;this.fetchData(null,null,true)}}
,isc.A.pickListShown=function isc_ComboBoxItem_pickListShown(){if(this.pickList.isVisible()){this.$35e=this.ns.Page.setEvent("mouseDown",this,null,"$35f")}}
,isc.A.$35f=function isc_ComboBoxItem__clickOutside(){var _1=this.pickList;if(!_1||!_1.isVisible())return;var _2=isc.EH.lastEvent.target;if(!_1.contains(_2,true))_1.hide();if(!this.$35g){this.$35g=this.ns.Page.setEvent("mouseUp",this,isc.Page.FIRE_ONCE,"$35h")}
if(!this.$35i){this.$35i=this.ns.Page.setEvent("dragStop",this,isc.Page.FIRE_ONCE,"$35j")}}
,isc.A.$35h=function isc_ComboBoxItem__dismissPickListClick(){if(this.form.$pf().item==this){this.ns.Page.clearEvent("dragStop",this.$35i);delete this.$35i;delete this.$35g;return false}
this.$35j(true)}
,isc.A.$35j=function isc_ComboBoxItem__refocusFromPLMouseUp(_1){if(_1==true){this.ns.Page.clearEvent("dragStop",this.$35i)}else{this.ns.Page.clearEvent("mouseUp",this.$35g)}
delete this.$35i;delete this.$35g;if(this.pickList&&this.pickList.isVisible()&&this.pickList.contains(isc.EH.getTarget()))
{this.focusInItem()}}
,isc.A.pickListHidden=function isc_ComboBoxItem_pickListHidden(){if(this.$35e)this.ns.Page.clearEvent("mouseDown",this.$35e);delete this.$35e}
,isc.A.handleKeyPress=function isc_ComboBoxItem_handleKeyPress(){if(!this.hasFocus||this.isReadOnly())return this.Super("handleKeyPress",arguments);var _1=isc.EH.lastEvent.keyName,_2=this.pickList,_3=(_2?(_2.isDrawn()&&_2.isVisible()):false);if(_3&&(_1==this.$35d||_1==this.$35c)){return _2.body.handleKeyPress(isc.EH.lastEvent)}
var _4=this.getEnteredValue(),_5=this.getValue(),_6=(!_4||_4==isc.emptyString);if(_1==this.$35b&&isc.EH.altKeyDown()){this.$35k=_4;this.$35l=_6;this.filterWithValue=false;this.showPickList();return false}
if(_1==this.$kt&&this.addUnknownValues==false){var _7=this.mapValueToDisplay(_5);this.setElementValue(_7)}
var _8=_1==this.$18n,_9=false;if(_8&&_3){if(this.addUnknownValues==false)_9=true;else if(this.$35m)_9=true;else if(this.completeOnEnter!=null)_9=this.completeOnEnter;else{_9=!this.form.isSearchForm||!this.$32x()}}
var _10=_3&&(_1==this.$35b||_1==this.$35a);this.$35m=_10;if(_3){if(_10||(_8&&_9)||_1==this.$kt)
{_2.bodyKeyPress(isc.EH.lastEvent);return false}
if(_8)this.hidePicker()}
this.$35n();return this.Super("handleKeyPress",arguments)}
,isc.A.shouldCompleteOnTab=function isc_ComboBoxItem_shouldCompleteOnTab(){return this.completeOnTab||(this.addUnknownValues==false)}
,isc.A.handleKeyDown=function isc_ComboBoxItem_handleKeyDown(){if(!this.hasFocus||this.isReadOnly())return this.Super("handleKeyDown",arguments);var _1=isc.EH.lastEvent.keyName,_2=this.pickList,_3=(_2?(_2.isDrawn()&&_2.isVisible()):false);this.$35o=null;if(_1==this.$kw&&(this.shouldCompleteOnTab()||this.$28z()==this.$25f))
{if(this.$35p()){this.$35o=this.getEnteredValue()}else if(_3){this.$35q()}}
return this.Super("handleKeyDown",arguments)}
,isc.A.$35p=function isc_ComboBoxItem__loadingData(){return(this.$33a||this.$33e)}
,isc.A.$35q=function isc_ComboBoxItem__fireTabCompletion(){var _1=this.pickList;var _2=_1.getSelectedRecord();if(_2!=null)_1.itemClick(_2)}
,isc.A.refreshDisplayValue=function isc_ComboBoxItem_refreshDisplayValue(){if(this.$35p())return;return this.Super("refreshDisplayValue",arguments)}
,isc.A.isUnknownValue=function isc_ComboBoxItem_isUnknownValue(_1){var _2=this.getValueMap();if(_2!=null){if(isc.isAn.Array(_2)){if(_2.contains(_1))return false}else if(isc.isAn.Object(_2)){for(var _3 in _2){if(_2[_3]==_1)return false}}}
var _4=this.getOptionDataSource();if(_4!=null){return this.$328(_1,true,true)==null}
return true}
,isc.A.handleEditorExit=function isc_ComboBoxItem_handleEditorExit(){if(this.$35r())return;this.$35m=false;var _1=this.getEnteredValue(),_2=this.getValue();if(this.$35o!=null&&this.$35o==_1&&!this.$35p())
{this.$35q();this.$35o=null;_1=this.getEnteredValue()}
if(this.$35o==null){if(this.addUnknownValues){var _3=this.getOptionDataSource();if(_2==_1&&(_3==null||this.getDisplayFieldName!=null)){if(_2!=null&&this.shouldFetchMissingValue(_2)){this.$282(_2)}
var _4=this.mapValueToDisplay(_2);if(_4!=_2)this.setElementValue(_4)}}else{if(!this.$35p()){if(this.isUnknownValue(_1)){this.setElementValue("");_1=""}
this.$20l(_1,true)}}}
this.$30b=(this.$35o!=null);var _5=this.Super("handleEditorExit",arguments);this.$30b=null;return _5}
,isc.A.$33g=function isc_ComboBoxItem__updateValueForFilterComplete(_1,_2,_3){this.selectDefaultItem();if(!this.hasFocus){if(this.$35o!=null&&this.$35o==this.getEnteredValue())
{this.$35q();this.$35o=null}
if(!this.addUnknownValues){var _4=this.getEnteredValue();if(this.isUnknownValue(_4)){this.setElementValue("");_4=""}
this.$20l(_4,true);_4=this.getEnteredValue();if(this.showHintInField&&(_4==null||isc.is.emptyString(_4))){this.$29c()}}
this.$30c(this.getValue())}
this.$33i()}
,isc.A.$35n=function isc_ComboBoxItem__markPending(){var _1=this.$35s;this.$35s=true;this.$35t=this.getEnteredValue();if(!_1)this.updateState()}
,isc.A.$35u=function isc_ComboBoxItem__markNotPending(){if(!this.$35s)return;this.$35s=null;this.$35t=null;this.updateState()}
,isc.A.getTextBoxStyle=function isc_ComboBoxItem_getTextBoxStyle(){if(this.pendingTextBoxStyle==null||!this.$35s||this.$257()){return this.Super("getTextBoxStyle",arguments)}
return this.pendingTextBoxStyle}
,isc.A.$20l=function isc_ComboBoxItem__updateValue(_1,_2){var _3=!_2&&!this.$35v&&this.addUnknownValues==false;if(!_3){this.$35u();var _4=this.mapDisplayToValue(_1);this.explicitChoice=this.$35v;if(this.compareValues(_4,this._value)){return true}
return this.Super("$20l",arguments)}else{if(this.changeOnKeypress&&this.length!=null&&isc.isA.String(_1)&&_1.length>this.length){_1=_1.substring(0,this.length);this.setElementValue(_1)}
if(this.$35s==null)return;this.refreshPickList(_1)}}
,isc.A.handleChanged=function isc_ComboBoxItem_handleChanged(_1,_2){var _3=this.Super("handleChanged",arguments);this.refreshPickList(this.getEnteredValue());return _3}
,isc.A.refreshPickList=function isc_ComboBoxItem_refreshPickList(_1){var _2=(!_1||_1==isc.emptyString);if(!_2)delete this.$35l;var _3=this.pickList,_4=(_3?_3.isVisible():false);if(_2&&!this.$35l){if(_4)_3.hide()}else if(this.showPickListOnKeypress||_4){if(!this.$35v&&this.hasFocus){if(!this.filterWithValue&&(this.$35k!=_1)){this.filterWithValue=true;delete this.$35k}
this.showPickList(true,true)}}}
,isc.A.selectDefaultItem=function isc_ComboBoxItem_selectDefaultItem(){if(this.pickList==null||this.pickList.destroyed)return;var _1=this.pickList.selection;if(this.pickList.selection.anySelected()){if(this.optionDataSource){var _2=this.getSelectedRecord();if(_2){this.pickList.clearLastHilite();this.delayCall("selectItemFromValue",[_2[this.valueField]])}}
return}
var _2=this.pickList.getRecord(0);if(_2==null||Array.isLoading(_2)||_2[this.pickList.isSeparatorProperty])return;_1.selectSingle(_2);this.pickList.clearLastHilite();this.pickList.scrollRecordIntoView(0)}
,isc.A.getSelectedRecord=function isc_ComboBoxItem_getSelectedRecord(){var _1=this.Super("getSelectedRecord",arguments);if(_1==null&&this._value!=null&&this.getOptionDataSource()){if(this.pickList==null||this.pickList.destroyed)this.makePickList(false);if(this.pickList&&this.pickList.data){_1=this.pickList.data.find(this.getValueFieldName(),this._value);if(_1!=null){this.$288([_1]);this.$284()}}}
return _1}
,isc.A.getPickListFilterOperator=function isc_ComboBoxItem_getPickListFilterOperator(){return(this.textMatchStyle=="startsWith"?"iStartsWith":this.textMatchStyle=="exact"?"iEquals":"iContains")}
,isc.A.getPickListFilterCriteria=function isc_ComboBoxItem_getPickListFilterCriteria(){var _1=this.optionCriteria,_2=this.pickListCriteria;if(_2!=null){if(_1==null)_1=_2;else{_1=isc.DataSource.combineCriteria(_1,_2)}}
if(this.alwaysFilterWithValue||this.filterWithValue){var _3=this.filterFields;if(_3==null){_3=[this.getDisplayFieldName()||this.getValueFieldName()]}
var _4=this.getEnteredValue();var _5;if(_3.length==1){_5={};_5[_3[0]]=_4}else{_5={_constructor:"AdvancedCriteria",operator:"or",criteria:[]}
for(var i=0;i<_3.length;i++){_5.criteria.add({fieldName:_3[i],value:_4,operator:this.getPickListFilterOperator()})}}
if(_1==null)_1=_5;else _1=isc.DataSource.combineCriteria(_1,_5)}
return _1||{}}
,isc.A.$289=function isc_ComboBoxItem__refreshForDisplayValueChange(){if(!this.hasFocus)return true;if(!this.addUnknownValues)return!this.$35s;return!!this.explicitChoice}
,isc.A.elementBlur=function isc_ComboBoxItem_elementBlur(){this.Super("elementBlur",arguments);if(isc.Browser.isMobile){this.delayCall("hidePickListOnBlur",[true],100)}else{this.hidePickListOnBlur()}}
,isc.A.hidePickListOnBlur=function isc_ComboBoxItem_hidePickListOnBlur(_1){if(_1&&((this.hasFocus&&this.containerWidget.hasFocus)||(this.pickList&&this.pickList.body.hasFocus)))return;var _2=this.pickList,_3=isc.EH,_4=_3.lastEvent;if(this.$35r()||(isc.Browser.isIE&&this.getActiveElement()==this.getDataElement()))
{return}
delete this.$32s;delete this.$33b;if(!_2||!_2.isVisible()||_2.formItem!=this)return;_2.hide()}
,isc.A.$35r=function isc_ComboBoxItem__mouseDownInPickList(){var _1=this.pickList;if(!_1||!_1.isVisible()||!_1.isDrawn())return false;var _2=isc.EH,_3=isc.EH.lastEvent;if((_3.eventType=="selectionChange"&&_1.contains(isc.EH.mouseDownTarget()))||((_3.eventType==_2.MOUSE_DOWN||_3.eventType==_2.CLICK)&&_1.contains(_3.target,true)))
{return true}
return false}
,isc.A.editorEnter=function isc_ComboBoxItem_editorEnter(_1,_2,_3){this.$29g()}
,isc.A.editorExit=function isc_ComboBoxItem_editorExit(_1,_2,_3){var _4;if(this.showHintInField&&(_3===_4||_3==null||isc.is.emptyString(_3)))
{this.$29c()}}
,isc.A.showPicker=function isc_ComboBoxItem_showPicker(){this.focusInItem();this.filterWithValue=false;var _1=this.getEnteredValue(),_2=(!_1||_1==isc.emptyString);this.$35k=_1;this.$35l=_2;return this.showPickList()}
,isc.A.pickValue=function isc_ComboBoxItem_pickValue(_1){this.$29g();var _2=this.mapValueToDisplay(_1);this.setElementValue(_2);if(this.hasFocus)this.selectValue();this.$35v=true;this.$35w={};this.$35w[_2]=_1;this.updateValue();delete this.$35v}
,isc.A.setValueMap=function isc_ComboBoxItem_setValueMap(){this.Super("setValueMap",arguments);if(this.pickList){if(this.pickList.isVisible())this.pickList.hide();delete this.pickList.formItem
this.setUpPickList(this.pickList.isVisible())}
if(this.addUnknownValues){this.setElementValue(this.mapValueToDisplay(this.getValue()))}}
,isc.A.setValue=function isc_ComboBoxItem_setValue(_1,_2,_3,_4,_5){var _6=this._value!=_1;if(!_6){if(this.$35s){this.setElementValue(this.$35t);return}}
this.$35u();this.$35x=true;this.invokeSuper(isc.ComboBoxItem,"setValue",_1,_2,_3,_4,_5);delete this.$35x;if(!this.hasFocus&&this.showHint&&this.showHintInField&&this.getHint()){var _7;if(_1===_7||_1==null||isc.is.emptyString(_1)){this.$29c()}}
if(_3||_1==null){this.explicitChoice=false}else if(_6){this.explicitChoice=true}}
,isc.A.getDefaultValue=function isc_ComboBoxItem_getDefaultValue(){var _1=this.Super("getDefaultValue",arguments);if(_1==null&&this.defaultToFirstOption){_1=this.getFirstOptionValue()}
return _1}
,isc.A.shouldGenerateExactMatchCriteria=function isc_ComboBoxItem_shouldGenerateExactMatchCriteria(){if(this.generateExactMatchCriteria!=null)return this.generateExactMatchCriteria;var _1=this.form.getDataSource();if(_1&&_1.supportsAdvancedCriteria())return true;return false}
,isc.A.hasAdvancedCriteria=function isc_ComboBoxItem_hasAdvancedCriteria(){if(this.Super("hasAdvancedCriteria",arguments))return true;if(this.addUnknownValues&&this.explicitChoice&&this.shouldGenerateExactMatchCriteria())
return true;return false}
,isc.A.getCriteriaFieldName=function isc_ComboBoxItem_getCriteriaFieldName(){if(this.criteriaField!=null)return this.criteriaField;if(this.displayField!=null&&this.addUnknownValues&&!this.explicitChoice){return this.displayField}
return this.getFieldName()}
,isc.A.getCriteriaValue=function isc_ComboBoxItem_getCriteriaValue(){if(this.displayField!=null&&this.addUnknownValues&&!this.explicitChoice){return this.getEnteredValue()}
return this.Super("getCriteriaValue",arguments)}
,isc.A.getOperator=function isc_ComboBoxItem_getOperator(_1){var _2;if(this.addUnknownValues&&!this.explicitChoice){_2=this.getPickListFilterOperator()}else{_2=this.Super("getOperator",arguments)}
return _2}
,isc.A.canEditSimpleCriterion=function isc_ComboBoxItem_canEditSimpleCriterion(_1){if(this.criterionField)return _1==this.criterionField;if(this.displayField&&_1==this.displayField)return true;return this.getFieldName()==_1}
,isc.A.setSimpleCriterion=function isc_ComboBoxItem_setSimpleCriterion(_1,_2){if(this.criterionField==null&&this.displayField!=null&&_2==this.displayField){this.setValue(_1,null,true)}else{this.setValue(_1)}}
,isc.A.canEditCriterion=function isc_ComboBoxItem_canEditCriterion(_1,_2){if(!this.addUnknownValues){return this.Super("canEditCriterion",arguments)}
var _3=_1.fieldName,_4=this.criterionField||this.getFieldName(),_5=this.Super("getOperator",[]);if(_3!=null){if(this.displayField==null){if(_3==_4){return _1.operator==_5||_1.operator==this.getPickListFilterOperator()}}else{if(_3==_4){return _1.operator==_5}else if(_3==this.displayField){return _1.operator==this.getPickListFilterOperator()}}}
return false}
,isc.A.setCriterion=function isc_ComboBoxItem_setCriterion(_1){if(!this.addUnknownValues||this.displayField==null||_1.fieldName!=this.displayField)
{return this.Super("setCriterion",arguments)}
var _2=_1.value;this.setElementValue(_2);this.$282(_2);var _3=this.mapDisplayToValue(_2);if(!this.compareValues(_3,this._value))this.saveValue(_3)}
,isc.A.$282=function isc_ComboBoxItem__checkForDisplayFieldValue(_1,_2){var _3=(this.$28o(_1,true)!=null);if(_3){return}
if(this.$33e){this.$33h=true;return}
if(this.pickList!=null&&this.pickList.formItem==this&&isc.isA.ResultSet(this.pickList.data)&&this.pickList.data.allRowsCached())
{return}
this.invokeSuper(isc.ComboBoxItem,"$282",_1)}
,isc.A.$28o=function isc_ComboBoxItem__mapKey(_1,_2,_3,_4,_5,_6){var _7=this.invokeSuper(isc.ComboBoxItem,"$28o",_1,true,_3,_4,_5,_6);if(_7==null&&this.getDisplayFieldName()!=null)
_7=this.$328(_1,false);if(_7==null&&!_2)_7=_1;return _7}
,isc.A.mapDisplayToValue=function isc_ComboBoxItem_mapDisplayToValue(_1,_2,_3,_4){if(this.$35w){for(var i in this.$35w){if(i==_1){return this.$35w[i]}}
delete this.$35w}
if(this.getDisplayFieldName()!=null){var _6;_6=this.$328(_1,true);if(_6!=null)_1=_6}
return this.invokeSuper(isc.ComboBoxItem,"mapDisplayToValue",_1,_2,_3,_4)}
,isc.A.$29n=function isc_ComboBoxItem__shouldAllowExpressions(){return false}
);isc.B._maxIndex=isc.C+49;isc.ComboBoxItem.registerStringMethods({dataArrived:"startRow,endRow,data",getPickListFilterCriteria:""});isc.ClassFactory.defineClass("FileItem","CanvasItem");isc.A=isc.FileItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.shouldSaveValue=true;isc.A.$22g="blob";isc.B.push(isc.A.$31d=function isc_FileItem__createCanvas(){if(!isc.isA.Canvas(this.canvas)){this.$35y=this.isReadOnly();this.canvas=(this.$35y?this.$35z():this.$350())}
this.containerWidget.addChild(this.canvas)}
,isc.A.$35z=function isc_FileItem__createReadOnlyCanvas(){var _1;if(this.type==this.$22g){_1=isc.DynamicForm.create({autoDraw:false,_redrawWithParent:false,redrawOnResize:false,canSubmit:true,action:this.action,targetItem:this,items:[{type:"text",editorType:"StaticText",width:this.width,height:this.height,name:this.getFieldName(),showTitle:false}]})}else{_1=isc.Canvas.create({height:10})}
return _1}
,isc.A.$350=function isc_FileItem__createEditableCanvas(){return isc.DynamicForm.create({autoDraw:false,_redrawWithParent:false,redrawOnResize:false,canSubmit:true,action:this.action,targetItem:this,getSaveOperationType:function(){if(this.targetItem&&this.targetItem.form)
return this.targetItem.form.getSaveOperationType();return this.Super("getSaveOperationType",arguments)},items:[{targetItem:this,type:"upload",width:this.width,height:this.height,name:this.getFieldName(),showTitle:false,saveValue:function(_1,_2,_3,_4){this.Super("saveValue",arguments);this.targetItem.saveValue(_1,_2,_3,_4)}},{name:"_transaction",type:"HiddenItem"}]})}
,isc.A.setElementReadOnly=function isc_FileItem_setElementReadOnly(_1){this.redraw()}
,isc.A.redraw=function isc_FileItem_redraw(){if(this.$35y!=this.isReadOnly()){var _1=this.getValue();if(this.canvas){delete this.canvas.canvasItem;this.canvas.destroy(true)}
this.$35y=this.isReadOnly();this.setCanvas(this.$35y?this.$35z():this.$350());this.setValue(_1)}
this.Super("redraw",arguments)}
,isc.A.getValue=function isc_FileItem_getValue(){return(this.$35y?this.Super("getValue",arguments):this.canvas.getValue(this.getFieldName()))}
,isc.A.setValue=function isc_FileItem_setValue(_1){if(this.isReadOnly()){var _2=this.form,_3=_2.getValues();if(this.type=="blob"){this.canvas.getItem(this.getFieldName()).setValue(_1)}else if(this.type=="imageFile"&&this.showFileInline!=false){this.canvas.setHeight("*");this.canvas.setWidth("*");this.canvas.setContents(this.getImageHTML())}else{if(this.showFileInline==true){this.logWarn("setValue(): Unsupported field-type for showFileInline: "+this.type)}
this.canvas.setHeight(20);this.canvas.setWidth("100%");this.canvas.setContents(this.getViewDownloadHTML(_1,_3))}
return this.Super("setValue",arguments)}else{if(_1==null||isc.isA.emptyString(_1)){this.canvas.getItem(this.getFieldName()).setValue(_1);return this.Super("setValue",arguments)}else{this.logWarn("Cannot programatically set the value of an upload field due to security restraints");return}}}
,isc.A.setWidth=function isc_FileItem_setWidth(_1){if(this.canvas&&!this.isReadOnly()){this.canvas.items[0].setWidth(_1)}
this.Super("setWidth",arguments)}
,isc.A.setHeight=function isc_FileItem_setHeight(_1){if(this.canvas&&!this.isReadOnly()){this.canvas.items[0].setHeight(_1)}
this.Super("setHeight",arguments)}
,isc.A.getViewDownloadHTML=function isc_FileItem_getViewDownloadHTML(_1,_2){if(isc.isA.String(_1))return _1;if(_2==null)return null;var _3=_2[this.name+"_filename"];if(_3==null||isc.isA.emptyString(_3))return this.emptyCellValue;var _4=isc.Canvas.imgHTML("[SKIN]actions/view.png",16,16,null,"style='cursor:"+isc.Canvas.HAND+"' onclick='"+this.getID()+".viewFile()'");var _5=isc.Canvas.imgHTML("[SKIN]actions/download.png",16,16,null,"style='cursor:"+isc.Canvas.HAND+"' onclick='"+this.getID()+".downloadFile()'");return"<nobr>"+_4+"&nbsp;"+_5+"&nbsp;"+_3+"</nobr>"}
,isc.A.getImageHTML=function isc_FileItem_getImageHTML(){var _1=this.form.getValues(),_2=this.form.getField(this.name),_3=this.name+"$351",_4;if(!_1[this.name])return" ";if(!_1[_3]){var _5=isc.Canvas.getFieldImageDimensions(_2,_1);_4=_1[_3]=isc.Canvas.imgHTML(this.form.getDataSource().streamFile(_1,_2.name),_5.width,_5.height)}else
_4=_1[_3];return _4}
,isc.A.viewFile=function isc_FileItem_viewFile(){isc.DS.get(this.form.dataSource).viewFile(this.form.getValues(),this.name)}
,isc.A.downloadFile=function isc_FileItem_downloadFile(){isc.DS.get(this.form.dataSource).downloadFile(this.form.getValues(),this.name)}
,isc.A.$29n=function isc_FileItem__shouldAllowExpressions(){return false}
);isc.B._maxIndex=isc.C+14;if(isc.ListGrid){isc.ClassFactory.defineClass("RelationItem","CanvasItem");isc.A=isc.RelationItem.getPrototype();isc.A.canvasConstructor="ListGrid";isc.A.canvasDefaults={canEdit:true};isc.A.pickerConstructor="RelationPicker";isc.A.showEditButton=true;isc.A.editButtonDefaults={click:"item.showPicker(!form.saveOperationIsAdd(), icon)",prompt:"Edit new/selected item"};isc.A.showRemoveButton=true;isc.A.removeButtonDefaults={src:"[SKIN]DynamicForm/Remove_icon.gif",click:"item.removeSelectedData()",prompt:"Remove selected item"};isc.A.canEditWithNoMasterRecord=false;isc.A=isc.RelationItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.init=function isc_RelationItem_init(){this.disabled=!this.canEditWithNoMasterRecord;this.Super("init",arguments);if(!this.pickerDefaults)this.pickerDefaults={};isc.addProperties(this.pickerDefaults,{dataSource:this.dataSource});if(this.showEditButton)this.editButton=this.addIcon(this.editButtonDefaults);if(this.showRemoveButton)this.removeButton=this.addIcon(this.removeButtonDefaults)}
,isc.A.getPickerData=function isc_RelationItem_getPickerData(){var _1=this.canvas.getSelectedRecord();if(_1)return _1;return this.getDataSource().getForeignKeysByRelation(this.form.getValues(),this.form.dataSource)}
,isc.A.showPicker=function isc_RelationItem_showPicker(_1,_2,_3,_4){this.Super("showPicker",arguments);var _5={};if(!this.form.saveOperationIsAdd())
_5=this.getDataSource().getForeignKeysByRelation(this.form.getValues(),this.form.dataSource);this.picker.setForeignKeyValues(_5)}
,isc.A.getValue=function isc_RelationItem_getValue(){return}
,isc.A.removeSelectedData=function isc_RelationItem_removeSelectedData(){this.canvas.removeSelectedData()}
,isc.A.setValue=function isc_RelationItem_setValue(){this.delayCall("filterRelation")}
,isc.A.filterRelation=function isc_RelationItem_filterRelation(){var _1=this.form.getValues();if(this.form.saveOperationIsAdd()){this.canvas.setData([]);this.setDisabled(!this.canEditWithNoMasterRecord)}else{this.canvas.filterData(this.getDataSource().getForeignKeysByRelation(_1,this.form.dataSource));this.enable()}
if(this.picker)this.picker.clearData()}
,isc.A.$29n=function isc_RelationItem__shouldAllowExpressions(){return false}
);isc.B._maxIndex=isc.C+8;isc.defineClass("RelationPicker","VLayout");isc.A=isc.RelationPicker.getPrototype();isc.A.className="dialogBackground";isc.A=isc.RelationPicker.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.creatorName="picker";isc.B.push(isc.A.initWidget=function isc_RelationPicker_initWidget(){this.Super("initWidget",arguments);this.addAutoChild("editor",{dataSource:this.dataSource},"DynamicForm");this.addAutoChild("toolbar",{membersMargin:2},"HLayout");this.addAutoChild("saveButton",{title:"Save",click:"this.picker.editor.saveData(this.picker.getID()+'.hide()')"},"AutoFitButton",this.toolbar);this.addAutoChild("clearButton",{title:"Clear",click:"this.picker.clearData();"},"AutoFitButton",this.toolbar);this.addAutoChild("cancelButton",{title:"Cancel",click:"this.picker.hide();this.picker.clearData()"},"AutoFitButton",this.toolbar)}
,isc.A.hide=function isc_RelationPicker_hide(){this.Super("hide",arguments);this.hideClickMask()}
,isc.A.setData=function isc_RelationPicker_setData(_1){this.editor.setData(_1)}
,isc.A.getData=function isc_RelationPicker_getData(){return this.editor.getValues()}
,isc.A.clearData=function isc_RelationPicker_clearData(){this.editor.clearValues();this.setData(this.foreignKeyValues)}
,isc.A.dataChanged=function isc_RelationPicker_dataChanged(){}
,isc.A.setForeignKeyValues=function isc_RelationPicker_setForeignKeyValues(_1){this.foreignKeyValues=_1}
);isc.B._maxIndex=isc.C+7;}
if(isc.ListGrid){isc.ClassFactory.defineClass("MultiFileItem","RelationItem");isc.A=isc.MultiFileItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.pickerConstructor="MultiFilePicker";isc.A.canvasDefaults={showHeader:false,canHover:true,cellHoverHTML:function(_1,_2,_3){if(this.canvasItem.form.saveOperationIsAdd())
return _1[this.getFieldName(_3)]},getCellValue:function(_1,_2,_3){var _1=this.Super("getCellValue",arguments);if(this.canvasItem.form.saveOperationIsAdd()){if(!this.displayShortName||!_1)return _1;_1=_1.replace(/.*(\\|\/)/g,isc.emptyString)}
return _1}};isc.A.emptyMessage="Click icon to add...";isc.A.displayShortName=true;isc.A.iconWidth=16;isc.A.iconHeight=16;isc.A.editButtonDefaults=isc.addProperties({},isc.RelationItem.getInstanceProperty('editButtonDefaults'),{prompt:"Add files",src:"[SKIN]MultiUploadItem/icon_add_files.png",showOver:false});isc.A.removeButtonDefaults=isc.addProperties({},isc.RelationItem.getInstanceProperty('removeButtonDefaults'),{src:"[SKIN]MultiUploadItem/icon_remove_files.png",showOver:false,prompt:"Remove selected files"});isc.A.canEditWithNoMasterRecord=true;isc.B.push(isc.A.getDynamicDefaults=function isc_MultiFileItem_getDynamicDefaults(_1){if(_1=="canvas"){var _2={};if(this.emptyMessage!=null)_2.emptyMessage=this.emptyMessage;if(this.displayShortName!=null)_2.displayShortName=this.displayShortName;return _2}
return this.Super("getDynamicDefaults",arguments)}
);isc.B._maxIndex=isc.C+1;isc.A=isc.MultiFileItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.removeSelectedData=function isc_MultiFileItem_removeSelectedData(){if(!this.form.saveOperationIsAdd())return this.Super("removeSelectedData",arguments);var _1=this.canvas.getSelection();for(var i=0;i<_1.length;i++)this.picker.removeUploadField(_1[i]._form,true)}
,isc.A.formSaved=function isc_MultiFileItem_formSaved(_1,_2,_3){if(this.picker){this.showPicker(true,this.editButton);this.picker.setForeignKeyValues(this.getDataSource().getForeignKeysByRelation(_3,this.form.dataSource));this.picker.saveData(this.getID()+".saveDataCallback()");return false}else{this.saveDataCallback()}}
,isc.A.saveDataCallback=function isc_MultiFileItem_saveDataCallback(){if(this.picker)this.picker.hide();this.form.formSavedComplete()}
,isc.A.pickerDataChanged=function isc_MultiFileItem_pickerDataChanged(_1){if(!this.form.saveOperationIsAdd())return;this.canvas.setData(this.picker.getData())}
,isc.A.destroy=function isc_MultiFileItem_destroy(){this.Super("destroy");if(this.picker)this.picker.destroy()}
);isc.B._maxIndex=isc.C+5;isc.defineClass("MultiFilePicker","VStack");isc.A=isc.MultiFilePicker.getPrototype();isc.A.height=1;isc.A.layoutMargin=10;isc.A.styleName="dialogBackground";isc.A.minUploadFields=1;isc.A.minFileSizeForProgressBar=204800;isc.A.progressCheckFrequency=1000;isc.A.progressMeterConstructor="MultiFileProgressMeter";isc.A.uploadLayoutConstructor="VStack";isc.A.uploadWithPKButtonName="Save";isc.A.uploadWithoutPKButtonName="OK";isc.A.cancelButtonName="Cancel";isc.A.showUploadRemoveButton=true;isc.A.uploadWithoutPK=false;isc.A=isc.MultiFilePicker.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.creatorName="picker";isc.B.push(isc.A.initWidget=function isc_MultiFilePicker_initWidget(){this.Super("initWidget",arguments);this.addAutoChild("uploadLayout",{height:1});this.addAutoChild("addAnotherFileButton",{width:75,height:20,align:"left",cursor:isc.Canvas.HAND,icon:"[SKIN]MultiUploadItem/icon_add_files.png",contents:"<u>Add&nbsp;another</u>",click:"this.picker.addUploadField()"},"Label");this.addAutoChild("toolbar",{width:1,height:1,membersMargin:10,layoutMargin:10,layoutAlign:"right"},"HStack");this.addAutoChild("saveButton",{title:this.hasKeys()?this.uploadWithPKButtonName:this.uploadWithoutPKButtonName,width:80,updateTitle:function(){var _1=this.parentElement.picker;var _2=_1.hasKeys()?_1.uploadWithPKButtonName:_1.uploadWithoutPKButtonName;if(_2!=this.title)this.setTitle(_2)},click:function(){var _1=this.parentElement.picker;if(!_1.hasKeys()&&!_1.uploadWithoutPK){_1.hide();_1.dataChanged()}else{_1.saveData()}},observes:[{source:this,message:"setForeignKeyValues",action:"observer.updateTitle()"}]},"IButton",this.toolbar);this.addAutoChild("cancelButton",{title:this.cancelButtonName,width:80,click:function(){this.picker.hide();if(this.picker.creator.form.saveOperationIsAdd())this.picker.clearData()}},"IButton",this.toolbar);this.clearData()}
,isc.A.hasKeys=function isc_MultiFilePicker_hasKeys(){return(this.foreignKeyValues&&!isc.isAn.emptyObject(this.foreignKeyValues))}
,isc.A.setForeignKeyValues=function isc_MultiFilePicker_setForeignKeyValues(_1){this.foreignKeyValues=_1}
,isc.A.clearData=function isc_MultiFilePicker_clearData(){var _1=this.getForms();for(var i=0;i<_1.length;i++)this.removeUploadField(_1[i]);for(var i=0;i<this.minUploadFields;i++)this.addUploadField()}
,isc.A.addUploadField=function isc_MultiFilePicker_addUploadField(){if(this.dataSource==null){isc.logWarn("A datasource must be defined on a MultiFileItem ("+this.id+") or it will both not function and elements will not render properly.")}
var _1=this.createAutoChild("uploadForm",{dataSource:this.dataSource,cellPadding:0,numCols:2,colWidths:['*','*'],width:250,elementChanged:function(){this.Super("elementChanged",arguments);this.picker.dataChanged()}},isc.DynamicForm);var _2=this.createAutoChild("uploadFormLayout",{members:[_1],height:21},isc.HLayout);this.uploadLayout.addMember(_2);if(this.showUploadRemoveButton){var _3=this.createAutoChild("uploadRemoveButton",{form:_1,picker:this,contents:isc.emptyString,cursor:isc.Canvas.HAND,icon:"[SKIN]MultiUploadItem/icon_remove_files.png",click:"this.picker.removeUploadField(this.form, true)",iconSpacing:6,width:22,height:20},isc.Label);_2.addMember(_3,0)}
if(this.maxUploadFields&&this.maxUploadFields<=this.uploadLayout.getMembers().length)
this.addAnotherFileButton.hide()}
,isc.A.removeUploadField=function isc_MultiFilePicker_removeUploadField(_1,_2){if(_1.$352){isc.rpc.cancelQueue(_1.$352);this.transactionNum=null;if(this.progressMeter)this.progressMeter.hide()}
_1.parentElement.destroy();if(_2&&this.uploadLayout.getMembers().length<this.minUploadFields){this.addUploadField()}
if(this.maxUploadFields&&this.maxUploadFields>this.uploadLayout.getMembers().length){this.addAnotherFileButton.show()}
this.dataChanged()}
,isc.A.getForms=function isc_MultiFilePicker_getForms(){return this.uploadLayout.getMembers().map("getMember",this.showUploadRemoveButton?1:0)}
,isc.A.dataChanged=function isc_MultiFilePicker_dataChanged(){}
,isc.A.hide=function isc_MultiFilePicker_hide(){this.Super("hide",arguments);this.hideClickMask()}
,isc.A.getData=function isc_MultiFilePicker_getData(){var _1=[];var _2=this.getForms();for(var i=0;i<_2.length;i++){var _4=_2[i];var _5=_4.getValues();if(isc.isAn.emptyObject(_5))continue;_5._form=_4;_1[_1.length]=_5}
return _1}
,isc.A.saveData=function isc_MultiFilePicker_saveData(_1){if(!_1)_1=this.saveCallback;if(!_1)_1=this.getID()+".hide()";this.saveCallback=_1;var _2=this.getForms();this.saveButton.setTitle(this.uploadWithPKButtonName);var _3;var _4;while(_2.length>0){_3=_2[0];_4=_3.getFields()[0].getValue();if(!_4){this.removeUploadField(_3);_2.remove(_3)}
else break}
if(_2.length==0){if(this.progressMeter){this.progressMeter.hide()}
this.transactionNum=null;delete this.saveCallback;this.fireCallback(_1);this.clearData();return}
if(!_3.validate())return;if(!this.progressMeter){this.progressMeter=this.createAutoChild("progressMeter",{progressCheckFrequency:this.progressCheckFrequency});this.addMember(this.progressMeter,0)}
_4=_4.replace(/.*(\\|\/)/g,isc.emptyString);this.progressMeter.setFileName(_4);this.progressMeter.hideProgressBar();this.progressMeter.show();if(this.hasKeys()){for(var _5 in this.foreignKeyValues)_3.setValue(_5,this.foreignKeyValues[_5])}
var _6=_3.saveData(this.getID()+".saveDataCallback(dsRequest, dsResponse, data)",{params:{formID:_3.getID()},willHandleError:true,form:_3,showPrompt:false,saveDataCallback:_1,timeout:0});this.transactionNum=_6.transactionNum;_3.$352=this.transactionNum;this.progressCheck(_3.getID(),this.transactionNum)}
,isc.A.saveDataCallback=function isc_MultiFilePicker_saveDataCallback(_1,_2,_3){var _4=_1.form;if(_2.status!=isc.RPCResponse.STATUS_SUCCESS){this.progressMeter.hide();this.transactionNum=null;if(_2.status==isc.RPCResponse.STATUS_VALIDATION_ERROR){_4.setErrors(_2.errors,true)}else{isc.warn(_3)}
return}
_4.$352=null;this.removeUploadField(_4);this.saveData(_1.saveDataCallback);if(this.callingFormItem.fileUploaded){this.callingFormItem.fileUploaded(_1,_2)}}
,isc.A.progressCheck=function isc_MultiFilePicker_progressCheck(_1,_2){this.lastProgressCheckTime=new Date().getTime();isc.DMI.callBuiltin({methodName:"uploadProgressCheck",callback:this.getID()+".progressCallback(rpcRequest, rpcResponse, data, "+_2+")",arguments:_1,requestParams:{willHandleError:true,showPrompt:false,formID:_1}})}
,isc.A.progressCallback=function isc_MultiFilePicker_progressCallback(_1,_2,_3,_4){var _5=_1.formID;var _6=window[_5];if(!_6||this.transactionNum!==_4)return;if(_2.status!=isc.RPCResponse.STATUS_SUCCESS)this.progressCheck(_5);if(_3.errors){isc.rpc.cancelQueue(this.transactionNum);_6.setErrors(_3.errors,true);this.saveButton.show();this.transactionNum=null;this.progressMeter.hide();return}
this.progressMeter.setFileSize(_3.totalBytes);if(_3.totalBytes<this.minFileSizeForProgressBar){this.progressMeter.hideProgressBar();return}
this.progressMeter.setBytesReceived(_3.bytesSoFar);this.progressMeter.showProgressBar();this.progressMeter.setPercentDone(100*_3.bytesSoFar/ _3.totalBytes);var _7=this.progressCheckFrequency-(new Date().getTime()-this.lastProgressCheckTime);if(_7<0)_7=0;this.delayCall("progressCheck",[_5,_4],_7)}
);isc.B._maxIndex=isc.C+14;isc.defineClass("MultiFileProgressMeter","VStack");isc.A=isc.MultiFileProgressMeter;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.formatBytes=function isc_c_MultiFileProgressMeter_formatBytes(_1){var _2;if(_1<1024){_1=Math.round(_1/ 1024);_2="B"}else if(_1<(1024*1024)){_1=Math.round(_1/ 1024);_2="KB"}else{_1=Math.round(_1/(1024*1024)*100)/100;_2="MB"}
return _1+"&nbsp;"+_2}
);isc.B._maxIndex=isc.C+1;isc.A=isc.MultiFileProgressMeter.getPrototype();isc.A.height=50;isc.A=isc.MultiFileProgressMeter.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.initWidget=function isc_MultiFileProgressMeter_initWidget(){this.Super("initWidget",arguments);this.addAutoChild("progressLabel",{height:1,dynamicContentsVars:{progressMeter:this},dynamicContents:true,contents:"<b><nobr>Saving ${progressMeter.fileName} ${progressMeter.getFormattedFileSize()}</nobr></b>"},"Canvas")}
,isc.A.setFileName=function isc_MultiFileProgressMeter_setFileName(_1){this.fileName=_1;delete this.fileSize;this.bytesSoFar=0;this.bytesReceived=0;this.progressLabel.markForRedraw();if(this.progressBar)this.setPercentDone(0)}
,isc.A.setFileSize=function isc_MultiFileProgressMeter_setFileSize(_1){this.fileSize=_1;this.progressLabel.markForRedraw()}
,isc.A.setBytesReceived=function isc_MultiFileProgressMeter_setBytesReceived(_1){this.bytesSoFar=this.bytesReceived;;this.bytesReceived=_1;this.progressLabel.markForRedraw()}
,isc.A.getFormattedFileSize=function isc_MultiFileProgressMeter_getFormattedFileSize(){if(!this.fileSize)return isc.emptyString;var _1="<br>";if(this.bytesReceived){_1+=isc.MultiFileProgressMeter.formatBytes(this.bytesReceived)+" of "}
_1+=isc.MultiFileProgressMeter.formatBytes(this.fileSize);if(this.bytesSoFar&&this.progressCheckFrequency){var _2=this.bytesReceived-this.bytesSoFar;_2=isc.MultiFileProgressMeter.formatBytes(_2*1000/this.progressCheckFrequency);_1+=" ("+_2+"/sec)"}
return _1}
,isc.A.showProgressBar=function isc_MultiFileProgressMeter_showProgressBar(){this.addAutoChild("progressBar",{overflow:"visible"},"Progressbar");this.progressBar.show()}
,isc.A.hideProgressBar=function isc_MultiFileProgressMeter_hideProgressBar(){if(this.progressBar)this.progressBar.hide()}
,isc.A.setPercentDone=function isc_MultiFileProgressMeter_setPercentDone(_1){this.progressBar.setPercentDone(_1)}
);isc.B._maxIndex=isc.C+8;}
isc.addGlobal("MultiUploadItem",isc.MultiFileItem);isc.addGlobal("MultiUploadPicker",isc.MultiFilePicker);if(isc.ListGrid){isc.defineClass("DialogUploadItem","StaticTextItem");isc.A=isc.DialogUploadItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.iconHeight=16;isc.A.iconWidth=16;isc.A.icons=[{src:"[SKIN]MultiUploadItem/icon_add_files.png",name:"upload",prompt:"Upload File",click:"item.showPicker(true)"},{src:"[SKIN]MultiUploadItem/icon_remove_files.png",name:"remove",prompt:"Remove File",click:"item.removeFile()"}];isc.A.pickerConstructor="DialogUploadPicker";isc.A.noFileString="[NONE]";isc.B.push(isc.A.init=function isc_DialogUploadItem_init(){this.Super("init",arguments);if(!this.pickerDefaults)this.pickerDefaults={};isc.addProperties(this.pickerDefaults,{dataSource:this.dataSource})}
,isc.A.mapValueToDisplay=function isc_DialogUploadItem_mapValueToDisplay(_1){return _1==null?this.noFileString:this.Super("mapValueToDisplay",arguments)}
,isc.A.showPicker=function isc_DialogUploadItem_showPicker(){this.Super("showPicker",arguments);var _1=this.getValue('primaryKey');this.picker.foreignKeyValues={primaryKey:_1}}
,isc.A.removeFile=function isc_DialogUploadItem_removeFile(){var _1=this.getValue();if(_1!=this.defaultValue){var _2=isc.DataSource.get(this.dataSource);_2.removeData({primaryKey:_1},this.getID()+".removeFileCallback(dsResponse)")}}
,isc.A.removeFileCallback=function isc_DialogUploadItem_removeFileCallback(_1){if(_1.status!=isc.DSResponse.STATUS_SUCCESS){isc.warn("Unable to remove file: "+_1.data);return}
this.setValue(this.defaultValue)}
,isc.A.fileUploaded=function isc_DialogUploadItem_fileUploaded(_1,_2){var _3=_2.data;var _4={};_4[_3.primaryKey]=_3.file_filename;this.setValueMap(_4);this.setValue(_3.primaryKey)}
,isc.A.destroy=function isc_DialogUploadItem_destroy(){this.Super("destroy");if(this.picker)this.picker.destroy()}
,isc.A.$29n=function isc_DialogUploadItem__shouldAllowExpressions(){return false}
);isc.B._maxIndex=isc.C+8;isc.defineClass("DialogUploadPicker","MultiFilePicker");isc.A=isc.DialogUploadPicker.getPrototype();isc.A.maxUploadFields=1;isc.A.uploadWithoutPKButtonName="Upload";isc.A.uploadWithPKButtonName="Upload";isc.A.showUploadRemoveButton=false;isc.A.uploadWithoutPK=true}
if(isc.ListGrid){isc.ClassFactory.defineClass("SOAPUploadItem","DialogUploadItem");isc.A=isc.SOAPUploadItem.getPrototype();isc.A.dataSource="sessionFiles"}isc.ClassFactory.defineClass("SpinnerItem","TextItem");isc.A=isc.SpinnerItem;isc.A.INCREASE_ICON={width:16,height:9,src:"[SKIN]/DynamicForm/Spinner_increase_icon.png",name:"increase",showOver:true,imgOnly:true,hspace:0};isc.A.DECREASE_ICON={width:16,height:9,src:"[SKIN]/DynamicForm/Spinner_decrease_icon.png",name:"decrease",showOver:true,imgOnly:true,hspace:0};isc.A=isc.SpinnerItem.getPrototype();isc.A.changeOnKeypress=false;isc.A.canTabToIcons=false;isc.A.height=18;isc.A.step=1;isc.A=isc.SpinnerItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.init=function isc_SpinnerItem_init(){this.Super("init",arguments);var _1=this.step;if(_1!=null&&!isc.isA.Number(_1)){_1=parseFloat(_1);if(!isc.isA.Number(_1))_1=1;this.step=_1}}
,isc.A.$25u=function isc_SpinnerItem__setUpIcons(){if(this.icons==null)this.icons=[];var _1=isc.addProperties({},isc.SpinnerItem.INCREASE_ICON),_2=isc.addProperties({},isc.SpinnerItem.DECREASE_ICON);this.icons.addListAt([_1,_2],0);this.Super("$25u",arguments)}
,isc.A.getIconsHTML=function isc_SpinnerItem_getIconsHTML(){if(!this.showIcons)return"";var _1=isc.SB.create();if(!this.$353){var _2="<TD tabIndex=-1"+(isc.Browser.isIE?" style='font-size:0px'":"")+">",_3={},_4=this.$27z(_3),_5=this.$270(_3);this.$353=["<TABLE role='presentation' STYLE='vertical-align:",_4,";margin-top:",_5,";margin-bottom:",_5,";display:inline;' BORDER=0 CELLPADDING=0 CELLSPACING=0><TR>",_2,this.getIconHTML(this.icons[0]),"<TD></TR><TR>",_2,this.getIconHTML(this.icons[1]),"</TD></TR></TABLE>"]}else{this.$353[8]=this.getIconHTML(this.icons[0]);this.$353[11]=this.getIconHTML(this.icons[1])}
_1.append(this.$353);for(var i=2;i<this.icons.length;i++){var _7=this.icons[i];if(!this.$27s(_7)||this.$27r(_7))continue;_1.append(this.getIconHTML(_7))}
return _1.toString()}
,isc.A.$270=function isc_SpinnerItem__getIconVMargin(_1){if(_1==this.icons[0]||_1==this.icons[1])return 0;return this.Super("$270",arguments)}
,isc.A.getTotalIconsWidth=function isc_SpinnerItem_getTotalIconsWidth(){var _1=this.Super("getTotalIconsWidth",arguments);if(_1>0){var _2=Math.max(this.icons[0].width,this.icons[1].width);_1-=_2}
return _1}
,isc.A.mouseStillDown=function isc_SpinnerItem_mouseStillDown(_1,_2,_3){if(this.isDisabled()||this.isReadOnly())return;this.$354++;if(this.$30r)this.updateValue();var _4=_3.nativeTarget;if(_4==this.$6h(this.icons[0])){this.increaseValue()}else if(_4==this.$6h(this.icons[1])){this.decreaseValue()}}
,isc.A.mouseDown=function isc_SpinnerItem_mouseDown(_1,_2,_3){if(this.isDisabled()||this.isReadOnly())return;if(!this.hasFocus)this.focusInItem();this.$354=0}
,isc.A.handleKeyPress=function isc_SpinnerItem_handleKeyPress(_1,_2){if(this.Super("handleKeyPress",arguments)==false)return false;var _3=_1.keyName,_4=this.isReadOnly();if(!_4&&_3=="Arrow_Up"){this.increaseValue();return false}
if(!_4&&_3=="Arrow_Down"){this.decreaseValue();return false}}
,isc.A.increaseValue=function isc_SpinnerItem_increaseValue(){this.updateValue();var _1=this.getValue();if(_1!=null&&this.max==_1)return;var _2=this.$354;var _3=this.step*(_2!=null?Math.pow(2,Math.floor(this.$354/(2000/isc.EH.STILL_DOWN_DELAY))):1);return this.$355(_1,_3)}
,isc.A.decreaseValue=function isc_SpinnerItem_decreaseValue(){this.updateValue();var _1=this.getValue();if(_1!=null&&this.min==_1)return;var _2=this.$354,_3=(0-this.step)*(_2!=null?Math.pow(2,Math.floor(this.$354/(2000/isc.EH.STILL_DOWN_DELAY))):1);return this.$355(_1,_3)}
,isc.A.$355=function isc_SpinnerItem__increaseValue(_1,_2){var _3=this.min,_4=this.max;if(!isc.isA.Number(_1)){_1=0;if((_3!=null&&_1<_3)||(_4!=null&&_1>_4)){_1=(_3!=null?_3:_4)}}else{var _5,_6;if(Math.round(_2)==_2){_5=0}else{var _7=_2+"";_5=_7.length-(_7.indexOf(".")+1)}
if(Math.round(_1)==_1){_6=0}else{var _8=_1+"";_6=_8.length-(_8.indexOf(".")+1)}
_1+=_2;var _9=Math.max(_5,_6);if(_9>0){_1=parseFloat(_1.toFixed(_9))}
if(_2>0&&_4!=null&&_1>_4)_1=_4;else if(_2<0&&_3!=null&&_1<_3)_1=_3}
this.setElementValue(_1);this.updateValue()}
,isc.A.mapDisplayToValue=function isc_SpinnerItem_mapDisplayToValue(_1){_1=this.Super("mapDisplayToValue",arguments);if(isc.isA.String(_1)){var _2=parseFloat(_1);if(_2==_1)_1=_2}
return _1}
,isc.A.updateValue=function isc_SpinnerItem_updateValue(){var _1=this.getElementValue();_1=this.mapDisplayToValue(_1);if(_1==this._value)return;if(_1!=null&&(!isc.isA.Number(_1)||(this.max!=null&&_1>this.max)||(this.min!=null&&_1<this.min)))
{var _2=this.mapValueToDisplay(this._value);this.setElementValue(_2);return}
this.Super("updateValue",arguments)}
,isc.A.setValue=function isc_SpinnerItem_setValue(_1,_2,_3,_4,_5,_6){if(_1!=null&&!isc.isA.Number(_1)){var _7=parseFloat(_1);if(_7==_1)_1=_7;else{this.logWarn("setValue(): passed '"+_1+"'. This is not a valid number - rejecting this value");_1=null}}
if(_1!=null){if(this.max!=null&&_1>this.max){this.logWarn("setValue passed "+_1+" - exceeds specified maximum. Clamping to this.max.");_1=this.max}
if(this.min!=null&&_1<this.min){this.logWarn("setValue passed "+_1+" - less than specified minimum. Clamping to this.min.");_1=this.min}}
return this.invokeSuper(isc.SpinnerItem,"setValue",_1,_2,_3,_4,_5,_6)}
);isc.B._maxIndex=isc.C+14;isc.ClassFactory.defineClass("SliderItem","CanvasItem");isc.A=isc.SliderItem.getPrototype();isc.A.vertical=false;isc.A.minValue=1;isc.A.maxValue=100;isc.A.roundValues=true;isc.A.roundPrecision=1;isc.A.defaultValue=1;isc.A.shouldSaveValue=true;isc.A.sliderDefaults={autoDraw:false,showTitle:false,valueChanged:function(){if(this.canvasItem)this.canvasItem.sliderChange()}};isc.A.autoDestroy=true;isc.A=isc.SliderItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.changeOnDrag=true;isc.B.push(isc.A.init=function isc_SliderItem_init(){this.Super("init",arguments);this.$356=this.getDefaultValue()}
,isc.A.$31d=function isc_SliderItem__createCanvas(){var _1=this.sliderDefaults;var _2=isc.addProperties({},_1,{vertical:this.vertical,minValue:this.minValue,maxValue:this.maxValue,value:this.defaultValue,numValues:this.numValues,roundValues:this.roundValues,roundPrecision:this.roundPrecision,tabIndex:this.getGlobalTabIndex()},this.sliderProperties);this.canvas=isc.Slider.create(_2);this.Super("$31d",arguments)}
,isc.A.isEditable=function isc_SliderItem_isEditable(){return true}
,isc.A.setValue=function isc_SliderItem_setValue(_1){this.$28y=true;var _2;if(_1==null){_2=this.getDefaultValue();if(_2!=null)_1=_2}
this.$356=_1;this.canvas.setValue(_1,(_2!=null))}
,isc.A.getValue=function isc_SliderItem_getValue(){return this.canvas.getValue()}
,isc.A.sliderChange=function isc_SliderItem_sliderChange(){var _1=this.canvas.getValue();if(this.$356!=_1){if(this.changeOnDrag||!this.canvas.valueIsChanging()){this.$20l(_1);this.$356=_1}}else{this.saveValue(_1)}}
,isc.A.setMinValue=function isc_SliderItem_setMinValue(_1){this.canvas.setMinValue(_1)}
,isc.A.setMaxValue=function isc_SliderItem_setMaxValue(_1){this.canvas.setMaxValue(_1)}
,isc.A.setNumValues=function isc_SliderItem_setNumValues(_1){this.canvas.setNumValues(_1)}
,isc.A.$29n=function isc_SliderItem__shouldAllowExpressions(){return false}
);isc.B._maxIndex=isc.C+10;isc.ClassFactory.defineClass("ColorItem","TextItem");isc.A=isc.ColorItem.getPrototype();isc.A.changeOnBlur=true;isc.A.changeOnKeypress=false;isc.A.pickerConstructor="ColorPicker";isc.A.pickerDefaults={colorSelected:function(_1,_2){this.callingFormItem.pickerColorSelected(_1,_2)}};isc.A.showPickerIcon=true;isc.A.pickerIconWidth=18;isc.A.pickerIconHeight=18;isc.A.pickerIconSrc="[SKIN]/DynamicForm/ColorPicker_icon.png";isc.A.pickerIconProperties={prompt:"Click to select a new color",showOver:false};isc.A.defaultPickerMode="simple";isc.A.allowComplexMode=true;isc.A.supportsTransparency=false;isc.A.browserSpellCheck=false;isc.A=isc.ColorItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.init=function isc_ColorItem_init(){this.pickerDefaults.defaultPickMode=this.defaultPickerMode;this.pickerDefaults.allowComplexMode=this.allowComplexMode;this.pickerDefaults.supportsTransparency=this.supportsTransparency;this.Super("init",arguments)}
,isc.A.updateValue=function isc_ColorItem_updateValue(){var _1=this._value,_2=this.getElementValue();_2=this.mapDisplayToValue(_2);if(_2==this._value)return;if(_2!=null&&!isc.isA.color(_2)){this.setElementValue(_1);return}
this.Super("updateValue",arguments);if(this.showPickerIcon&&this._value!=_1){this.setIconBackgroundColor(this.getPickerIcon(),this._value)}}
,isc.A.getDefaultValue=function isc_ColorItem_getDefaultValue(){var _1=this.Super("getDefaultValue",arguments);if(_1&&!isc.isA.color(_1)){this.logWarn("Default value:"+_1+" is not a valid color identifier."+" Ignoring this default.");_1=this.defaultValue=null}
return _1}
,isc.A.showPicker=function isc_ColorItem_showPicker(){this.picker=isc.ColorChooser.getSharedColorPicker(this.pickerDefaults);var _1=this.picker;var _2=_1.callingFormItem;if(_2!=this){_1.callingFormItem=this;_1.callingForm=this.form;_1.setSupportsTransparency(this.supportsTransparency)}
_1.setHtmlColor(this._value||"");return this.Super("showPicker",arguments)}
,isc.A.pickerColorSelected=function isc_ColorItem_pickerColorSelected(_1,_2){if(!this.mask){_1=this.mapValueToDisplay(_1)}
this.setElementValue(_1);this.updateValue()}
,isc.A.setValue=function isc_ColorItem_setValue(_1){this.Super("setValue",arguments);this.setIconBackgroundColor(this.getPickerIcon(),this._value)}
);isc.B._maxIndex=isc.C+6;if(isc.ListGrid){isc.ClassFactory.defineClass("ValueMapEditor","VLayout");isc.A=isc.ValueMapEditor.getPrototype();isc.A.mapTypeConstructor=isc.Button;isc.A.mapTypeDefaults={autoDraw:false,width:"100%",click:function(){this.creator.canvasItem.toggleObjectArray()},showIf:function(){return this.creator.canvasItem.showMapTypeButton},getTitle:function(){var _1=this.creator.canvasItem,_2=_1.saveAsObject;if(_2)return _1.saveAsObjectTitle;else return _1.saveAsArrayTitle}};isc.A.selectorConstructor=isc.ListGrid;isc.A.selectorDefaults={height:1,overflow:"visible",bodyOverflow:"visible",inherentHeight:true,showNewRecordRow:true,listEndEditAction:"next",canEdit:true,editEvent:isc.EH.CLICK,cellHeight:22,selectionType:isc.Selection.NONE,selectOnEdit:false,leaveScrollbarGap:false,showSortArrow:isc.ListGrid.NONE,canSort:false,canResizeFields:false,dataChanged:function(){this.Super("dataChanged",arguments);if(this.creator&&this.creator.canvasItem)
this.creator.canvasItem.updateValue()},stopOnErrors:true,validateCellValue:function(_1,_2,_3,_4){var _5=this.getFieldName(_2),_6=this.creator.canvasItem,_7=_6.$357(_5,_1,_3);if(_7)return _7;return this.Super("validateCellValue",arguments)},cellContextClick:function(_1,_2,_3){var _4=this.creator.canvasItem.getSelectorContextMenu(_1);_4.showContextMenu();return false},getHeaderContextMenu:function(){return this.creator.canvasItem.getSelectorContextMenu()}};isc.A=isc.ValueMapEditor.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.initWidget=function isc_ValueMapEditor_initWidget(){if(!this.canvasItem){this.logWarn("ValueMapEditors are not supported as standalone widgets at this time.");return}
this.Super("initWidget",arguments);this.addAutoChild("mapType",{});this.addAutoChild("selector",{newRecordRowMessage:this.canvasItem.newOptionRowMessage,showHeader:this.showHeader,fields:this.canvasItem.$358(),data:this.canvasItem.$359()})}
);isc.B._maxIndex=isc.C+1;isc.ClassFactory.defineClass("ValueMapItem","CanvasItem");isc.A=isc.ValueMapItem.getPrototype();isc.A.height=1;isc.A.autoDestroy=true;isc.A.shouldSaveValue=true;isc.A.showMapTypeButton=true;isc.A.showHeader=true;isc.A.newOptionRowMessage="Click to add a new option";isc.A.displayTitle="Display";isc.A.valueTitle="Value";isc.A.undefinedKeyErrorMessage="Each valueMap option must have a defined value";isc.A.duplicateValueErrorMessage="Please enter a unique value for this option";isc.A.saveAsArrayTitle="Stored == Displayed";isc.A.saveAsObjectTitle="Stored != Displayed";isc.A=isc.ValueMapItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.$31d=function isc_ValueMapItem__createCanvas(){if(this.canvas)return;if(this._value==null)this._value=[];this.canvas=isc.ValueMapEditor.create({autoDraw:false,_generated:true,ID:this.getID()+"$36a",canvasItem:this,showMapType:this.showMapTypeButton,showHeader:this.showHeader,height:this.height,overflow:isc.Canvas.VISIBLE});this.Super("$31d",arguments)}
,isc.A.$36b=function isc_ValueMapItem__getMapTypeButton(){return this.canvas.mapType}
,isc.A.$36c=function isc_ValueMapItem__getSelectorGrid(){return this.canvas.selector}
,isc.A.$359=function isc_ValueMapItem__getSelectorData(_1){var _1=_1||this.getValue();if(!_1)_1=this.saveAsObject?{}:[];var _2=[];if(isc.isAn.Array(_1)){for(var i=0;i<_1.length;i++){_2[i]={value:_1[i]}}}else{var i=0;for(var _4 in _1){_2[i]={value:_4,display:_1[_4]}
i++}}
return _2}
,isc.A.$358=function isc_ValueMapItem__getSelectorFields(){if(!this.$36d||!this.$36e){this.$36d={name:"display",title:this.displayTitle};this.$36e={name:"value",title:this.valueTitle}}
var _1=[this.$36e];if(this.saveAsObject)_1.add(this.$36d);return _1}
,isc.A.$357=function isc_ValueMapItem__validateSelectorCellValue(_1,_2,_3){if(_1!="value")return;if(!this.saveAsObject&&this.allowDuplicates)return;var _4=this.$36c(),_5=_2>_4.data.getLength(),_6=this._value,_7;if(this.saveAsObject){if(_3==null||_3==""){return[this.undefinedKeyErrorMessage]}
_7=isc.getKeys(_6)}else{_7=_6||[]}
var _8;if(_5){if(_7.contains(_3))_8=true}else{for(var i=0;i<_7.length;i++){if(_2==i)continue;if(_3==_7[i]){_8=true;break}}}
if(_8)return[this.duplicateValueErrorMessage];return null}
,isc.A.getSelectorContextMenu=function isc_ValueMapItem_getSelectorContextMenu(_1){if(!this.$36f){var _2=[{title:"Add new option",click:"menu.canvasItem.addOption()"},{title:"Delete option",enableIf:"this.record != null",click:"menu.canvasItem.removeOption(menu.record)"}];if(this.showMapTypeButton){_2.add({dynamicTitle:"this.canvasItem.$36g()",click:"menu.canvasItem.toggleObjectArray()"})}
this.$36f=this.ns.Menu.create({canvasItem:this,ID:this.getID()+"$36h",data:_2})}
this.$36f.record=_1;return this.$36f}
,isc.A.addOption=function isc_ValueMapItem_addOption(){this.$36c().startEditingNew()}
,isc.A.removeOption=function isc_ValueMapItem_removeOption(_1){var _2=this.$36c();_2.data.remove(_1);_2.data.dataChanged()}
,isc.A.$36g=function isc_ValueMapItem__getToggleObjectArrayTitle(){var _1=this.saveAsObject;return _1?this.saveAsArrayTitle:this.saveAsObjectTitle}
,isc.A.toggleObjectArray=function isc_ValueMapItem_toggleObjectArray(){this.setSaveAsObject(!this.saveAsObject)}
,isc.A.setSaveAsObject=function isc_ValueMapItem_setSaveAsObject(_1){if(this.saveAsObject==_1)return;var _2,_3=this.getValue();if(_1){_2={};if(_3!=null){for(var i=0;i<_3.length;i++){_2[_3[i]]=_3[i]}}}else{_2=[];for(var _5 in _3){_2.add(_5)}}
this._value=_2;this.saveAsObject=_1;var _6=this.$36c(),_7=this.$359();if(!_6)return;_6.setData(_7);_6.setFields(this.$358());if(this.showMapTypeButton)this.$36b().markForRedraw()}
,isc.A.isEditable=function isc_ValueMapItem_isEditable(){return true}
,isc.A.updateValue=function isc_ValueMapItem_updateValue(){var _1=this.$36c(),_2=_1.data,_3=this.saveAsObject,_4=_3?{}:[],_5;if(_3){var _6={};if(isc.isAn.Array(this._value)){_5=true}else{isc.addProperties(_6,this._value)}
var _7=isc.getKeys(_6);for(var i=0;i<_2.length;i++){var _9=_2[i],_10=_9.display;_4[_9.value]=_9.display;if(!_7.contains(_10)||(_6[_9.value]!=_10))
_5=true;delete _6[_9.value]}
if(isc.getKeys(_6).length!=0)_5=true}else{if(!isc.isAn.Array(this._value)||(this._value.length!=_2.length)){_5=true}
for(var i=0;i<_2.length;i++){var _9=_2[i],_10=_9.value;_4[i]=_10;if(!_5&&(this._value[i]!=_10))_5=true}}
if(!_5)return;if(this.handleChange(_4,this._value)==false)return;_4=this.$29k;delete this.$29k;this.saveValue(_4)}
,isc.A.setValue=function isc_ValueMapItem_setValue(_1){this.$28y=true;if(_1!=null&&!isc.isAn.Object(_1)){this.logWarn("setValue() passed an invalid object. Must be a valueMap (either "+"specified as an array or a raw JS object.");return}
var _2=isc.isAn.Array(_1),_3=(!!this.saveAsObject);if(_1!=null&&_2==_3){this.logInfo("setValue() passed a valueMap of type "+(_2?"Array":"Object")+".  Updating this.saveAsObject to match this data type.");this.setSaveAsObject(!_2);_3=this.saveAsObject}
this._value=_1;this.$36c().setData(this.$359())}
);isc.B._maxIndex=isc.C+15}
isc.defineClass("ArrayItem",isc.ValueMapItem);isc.A=isc.ArrayItem.getPrototype();isc.A.showMapTypeButton=false;isc.A.showHeader=false;isc.A.saveAsObject=false;isc.A.allowDuplicates=true;isc.A.newOptionRowMessage="Click to add values";isc.defineClass("MappingItem",isc.ValueMapItem);isc.A=isc.MappingItem.getPrototype();isc.A.showMapTypeButton=false;isc.A.saveAsObject=true;if(isc.ListGrid){isc.ClassFactory.defineClass("PickTreeItem","CanvasItem");isc.A=isc.PickTreeItem;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.$36i=function isc_c_PickTreeItem__itemSelected(_1){return this.canvasItem.$36i(_1)}
,isc.A.$36j=function isc_c_PickTreeItem__getButtonTitle(){var _1=this.getSelectedItem();if(_1==null){var _2=this.canvasItem,_3=this.canvasItem.getValue();if(_3!=null)return _2.mapValueToDisplay(_3)}
return this.Super("getTitle",arguments)}
,isc.A.$36k=function isc_c_PickTreeItem__treeDataLoaded(){var _1=this.canvasItem;_1.setValue(_1.getValue())}
);isc.B._maxIndex=isc.C+3;isc.A=isc.PickTreeItem.getPrototype();isc.A.canFocus=true;isc.A.shouldSaveValue=true;isc.A.buttonDefaults={height:19};isc.A.emptyMenuMessage="No items to display";isc.A=isc.PickTreeItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.buttonConstructor="TreeMenuButton";isc.B.push(isc.A.init=function isc_PickTreeItem_init(){this.Super("init",arguments);if(this.dataSource==null&&this.valueTree==null)
this.logWarn("This form item requires a 'valueTree'.")}
,isc.A.getOptionDataSource=function isc_PickTreeItem_getOptionDataSource(){var _1=this.optionDataSource||this.dataSource;if(_1!=null)_1=isc.DataSource.get(_1);return _1}
,isc.A.getDisplayValue=function isc_PickTreeItem_getDisplayValue(){return this.canvas.getTitle()}
,isc.A.$31d=function isc_PickTreeItem__createCanvas(){var _1={getTitle:isc.PickTreeItem.$36j,canFocus:this.$mb(),disabled:this.isDisabled(),dataSource:this.getOptionDataSource(),criteria:this.optionCriteria,data:this.valueTree,canSelectParentItems:this.canSelectParentItems,itemSelected:isc.PickTreeItem.$36i,emptyMenuMessage:this.emptyMenuMessage,loadDataOnDemand:this.loadDataOnDemand,treeDataLoaded:isc.PickTreeItem.$36k,displayField:this.displayField};this.canvas=this.addAutoChild("button",_1,this.buttonConstructor,this.container);this.autoDestroy=true;this.Super("$31d",arguments);if(this._value!=null)this.setValue(this._value)}
,isc.A.$36i=function isc_PickTreeItem__itemSelected(_1){var _2=this.$36l(_1);if(this.$20l(_2))this.$36m=_1}
,isc.A.$36l=function isc_PickTreeItem__mapNodeToValue(_1){if(this.$36n())return this.valueTree.getPath(_1);return _1[this.$36o()]}
,isc.A.$36n=function isc_PickTreeItem__usePathAsId(){return(!this.valueField&&this.valueTree&&(this.valueTree.modelType!="parent"))}
,isc.A.$36o=function isc_PickTreeItem__getValueFieldName(){var _1=this.valueField;if(!_1){_1=this.valueTree?this.valueTree.idField:this.$36p()}
return _1}
,isc.A.$36p=function isc_PickTreeItem__getPrimaryKeyFieldName(){if(!this.getOptionDataSource())return null;if(!this.$36q){var _1=isc.DataSource.getDataSource(this.getOptionDataSource()),_2=_1.getPrimaryKeyFieldNames(),_3=isc.isAn.Array(_2)?_2[0]:_2;if(isc.isAn.Array(_2)&&_2.length>1){this.logWarn("Multiple primary key fields not supported by PickTreeItem - using '"+_3+"' as single primary key field")}
this.$36q=_3}
return this.$36q}
,isc.A.getSelectedNode=function isc_PickTreeItem_getSelectedNode(){return this.$36m}
,isc.A.setValue=function isc_PickTreeItem_setValue(_1){this.Super("setValue",arguments);var _2=this.$36r(this.getValue());this.$36m=_2;if(this.canvas){this.canvas.setSelectedItem(_2)}}
,isc.A.updateValueMap=function isc_PickTreeItem_updateValueMap(_1){this.Super("updateValueMap",arguments);if(_1)this.canvas.markForRedraw()}
,isc.A.$36r=function isc_PickTreeItem__getNode(_1){if(!_1)return null;var _2=(this.getOptionDataSource()?this.canvas.getTree():this.valueTree);if(this.$36n())return this.valueTree.find(_1);return _2.find(this.$36o(),_1)}
,isc.A.$29n=function isc_PickTreeItem__shouldAllowExpressions(){return false}
,isc.A.fetchData=function isc_PickTreeItem_fetchData(){var _1=this.getOptionDataSource();if(_1==null){this.logWarn("fetchData() called on pickTree item with no option data source. Ignoring.");return}
var _2=this.canvas.getTree();if(!_2||!_2.invalidateCache){return}
_2.invalidateCache()}
);isc.B._maxIndex=isc.C+15;isc.defineClass("IPickTreeItem","PickTreeItem");isc.A=isc.IPickTreeItem.getPrototype();isc.A.buttonConstructor="ITreeMenuButton"}
isc.ClassFactory.defineClass("PopUpTextAreaItem","StaticTextItem");isc.A=isc.PopUpTextAreaItem.getPrototype();isc.A.canFocus=true;isc.A.wrap=false;isc.A.width=150;isc.A.clipValue=true;isc.A.popUpOnEnter=false;isc.A.popUpOnAnyClick=true;isc.A.textAreaWidth=100;isc.A.textAreaHeight=100;isc.A.iconOnly=false;isc.A.popUpIconSrc="[SKIN]/DynamicForm/PopUpTextAreaEditor_icon.gif";isc.A.popUpIconWidth=20;isc.A.popUpIconHeight=20;isc.A.iconVAlign=isc.Canvas.CENTER;isc.A=isc.PopUpTextAreaItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.$25u=function isc_PopUpTextAreaItem__setUpIcons(){if(this.icons==null)this.icons=[];var _1={name:"popUpIcon",src:this.popUpIconSrc,showOver:false,width:this.popUpIconWidth,height:this.popUpIconHeight,click:this.$36s};this.icons.addAt(_1,0);this.Super("$25u",arguments)}
,isc.A.$36s=function isc_PopUpTextAreaItem__popUpIconClick(_1,_2,_3){if(_2.popUpOnAnyClick||_2.isDisabled())return;_2.showPopUp(true)}
,isc.A.handleCellClick=function isc_PopUpTextAreaItem_handleCellClick(){if(this.Super("handleCellClick")==false)return false;if(this.popUpOnAnyClick&&!this.isDisabled())this.showPopUp(true)}
,isc.A.showPopUp=function isc_PopUpTextAreaItem_showPopUp(_1){var _2=this.getValue();if(!this.$36t)this.setupPopUpForm();this.placePopUp();var _3=this.$36t.getItem("textArea");_3.setValue(_2);this.$36t.bringToFront();this.$36t.show();if(_1)this.$36t.focusInItem("textArea");this.$36t.showClickMask({target:this,methodName:"hidePopUp"},true,[this.$36t])}
,isc.A.visibilityChanged=function isc_PopUpTextAreaItem_visibilityChanged(){if(!this.isVisible())this.$36u()}
,isc.A.$36u=function isc_PopUpTextAreaItem__hiddenObservation(){var _1=this.$36t;if(!_1||!(_1.isVisible()&&_1.isDrawn()))return;_1.hide()}
,isc.A.moved=function isc_PopUpTextAreaItem_moved(){this.$36v()}
,isc.A.$36v=function isc_PopUpTextAreaItem__movedObservation(){var _1=this.$36t;if(!_1||!(_1.isVisible()&&_1.isDrawn()))return;var _2=this.getTop(),_3=this.getLeft(),_4=this.getInnerWidth(),_5=this.getInnerHeight(),_6=this.containerWidget,_7=_6.getScrollTop(),_8=_6.getScrollLeft(),_9=_6.getViewportWidth(),_10=_6.getViewportHeight();if(_2<_7||(_2+_5)>(_7+_10)||_3<_8||(_3+_4)>(_8+_9))
{_1.hide()}else{this.placePopUp()}}
,isc.A.zIndexChanged=function isc_PopUpTextAreaItem_zIndexChanged(){var _1=this.$36t;if(!_1||!(_1.isVisible()&&_1.isDrawn()))return;_1.bringToFront()}
,isc.A.placePopUp=function isc_PopUpTextAreaItem_placePopUp(){var _1=this.getTextAreaTop(),_2=this.getTextAreaLeft(),_3=this.getTextAreaWidth(),_4=this.getTextAreaHeight();this.$36t.moveTo(_2,_1);this.$36t.resizeTo(_3,_4);var _5=this.$36t.getItem("textArea");_5.setWidth(_3);_5.setHeight(_4)}
,isc.A.setupPopUpForm=function isc_PopUpTextAreaItem_setupPopUpForm(){if(this.$36t!=null)return;var _1=isc.DynamicForm.create({autoDraw:false,ID:this.getID()+"$36t",_generated:true,separateContentInsertion:false,cellPadding:0,targetItem:this,values:{textArea:this.getValue()},items:[{name:"textArea",showTitle:false,type:"textArea",selectOnFocus:true,targetItem:this,focus:function(_4){this.targetItem.textAreaFocus()},keyDown:function(_4,_5,_6,_7){this.targetItem.$30q();return this.targetItem.textAreaKeyDown(_4,_6,_7)},keyPress:function(_4,_5,_6,_7){return this.targetItem.textAreaKeyPress(_4,_6,_7)},blur:function(){this.targetItem.textAreaBlur()}}],hide:function(_4,_5,_6,_7){var _2=this.invokeSuper(isc.DynamicForm,"hide",_4,_5,_6,_7);this.hideClickMask();return _2}});this.$36t=_1;var _3=this.containerWidget;_1.observe(_3,"destroy","observer.hide();observer.destroy()")}
,isc.A.hidePopUp=function isc_PopUpTextAreaItem_hidePopUp(){if(this.$36t){this.updateValue();this.$36t.hide()}}
,isc.A.destroy=function isc_PopUpTextAreaItem_destroy(){if(this.$36t){this.$36t.destroy();delete this.$36t}
return this.Super("destroy",arguments)}
,isc.A.getTextAreaTop=function isc_PopUpTextAreaItem_getTextAreaTop(){var _1=this.getPageTop();if(isc.Browser.isIE)_1-=1;return _1}
,isc.A.getTextAreaLeft=function isc_PopUpTextAreaItem_getTextAreaLeft(){return this.getPageLeft()}
,isc.A.getTextAreaWidth=function isc_PopUpTextAreaItem_getTextAreaWidth(){return Math.max(this.textAreaWidth,this.getInnerWidth())}
,isc.A.getTextAreaHeight=function isc_PopUpTextAreaItem_getTextAreaHeight(){return this.textAreaHeight}
,isc.A.mapValueToDisplay=function isc_PopUpTextAreaItem_mapValueToDisplay(){if(this.iconOnly)return"";return this.Super("mapValueToDisplay",arguments)}
,isc.A.textAreaBlur=function isc_PopUpTextAreaItem_textAreaBlur(){this.hidePopUp()}
,isc.A.textAreaFocus=function isc_PopUpTextAreaItem_textAreaFocus(){}
,isc.A.textAreaKeyPress=function isc_PopUpTextAreaItem_textAreaKeyPress(_1,_2,_3){}
,isc.A.textAreaKeyDown=function isc_PopUpTextAreaItem_textAreaKeyDown(_1,_2,_3){}
,isc.A.setValue=function isc_PopUpTextAreaItem_setValue(_1){var _2=this.mapValueToDisplay(this.getValue());this.Super("setValue",arguments);var _3=this.mapValueToDisplay(this.getValue());if(_2!=_3){this.setElementValue(_3)
if(this.$36t&&this.$36t.isVisible()){this.$36t.setValue("textArea",_3)}}}
,isc.A.updateValue=function isc_PopUpTextAreaItem_updateValue(){if(this.$36t&&this.$36t.isVisible()&&!this.$36t.$20f)
{var _1=this.$36t.getItem("textArea");_1.updateValue();var _2=this.$36t.getValue("textArea");this.setElementValue(this.mapValueToDisplay(_2));this.$20l(_2)}else{return this.Super("updateValue",arguments)}}
,isc.A.setElementValue=function isc_PopUpTextAreaItem_setElementValue(_1){if(this.iconOnly)return;return this.Super("setElementValue",arguments)}
,isc.A.focusInItem=function isc_PopUpTextAreaItem_focusInItem(){if(this.$36t&&this.$36t.isVisible()){this.$36t.focusInItem('textArea')}else if(this.showIcons){this.focusInIcon(this.icons[0])}else{this.showPopUp(true)}}
,isc.A.$296=function isc_PopUpTextAreaItem__setElementTabIndex(_1){this.$297=_1;if(!this.isVisible()||!this.containerWidget.isDrawn())return;this.$298()}
,isc.A.setElementReadOnly=function isc_PopUpTextAreaItem_setElementReadOnly(_1){}
);isc.B._maxIndex=isc.C+28;isc.defineClass("ExpressionItem","PopUpTextAreaItem");isc.A=isc.ExpressionItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.textAreaWidth=400;isc.A.showActionIcon=true;isc.A.actionIconSrc="[SKIN]/actions/add.png";isc.A.actionIconWidth=20;isc.A.actionIconHeight=20;isc.A.actionIconPosition=1;isc.B.push(isc.A.mapValueToDisplay=function isc_ExpressionItem_mapValueToDisplay(_1){if(isc.isA.StringMethod(_1))return _1.getDisplayValue();else if(isc.isA.Function(_1)){if(_1.iscAction){return"["+_1.iscAction.title+"]"}
return isc.Func.getBody(_1)}
else return this.Super("mapValueToDisplay",arguments)}
,isc.A.getValue=function isc_ExpressionItem_getValue(){var _1=this.Super("getValue");if(isc.isA.Function(_1))return isc.Func.getBody(_1);else return _1}
,isc.A.$25u=function isc_ExpressionItem__setUpIcons(){this.Super("$25u",arguments);if(this.showActionIcon){if(this.icons==null)this.icons=[];var _1=this.actionIconPosition;this.icons.addAt({name:"action",src:this.actionIconSrc,showOver:false,width:this.actionIconWidth,height:this.actionIconHeight,click:this.getID()+".showActionMenu();return false;"},_1);this.$27v(this.icons[_1])}}
,isc.A.updateAppearance=function isc_ExpressionItem_updateAppearance(_1){this.setElementValue(this.mapValueToDisplay(_1))}
,isc.A.showActionMenu=function isc_ExpressionItem_showActionMenu(){var _1=this,_2=isc.ActionMenu.create({sourceComponent:this.form.currentComponent,sourceMethod:this.name,components:this.form.allComponents,bindingComplete:function(_4){_1.$20l(_4)}});_2.show();var _3=this.getIconPageRect(this.icons[1]);_2.placeNear(_3[0]+this.actionIconWidth,_3[1]+this.actionIconHeight-this.containerWidget.getScrollTop())}
);isc.B._maxIndex=isc.C+5;isc.ClassFactory.defineClass("SearchForm","DynamicForm");isc.A=isc.SearchForm.getPrototype();isc.A.canEditFieldAttribute="canFilter";isc.A.isSearchForm=true;isc.A.hiliteRequiredFields=false;isc.A.operationType="fetch";isc.A.$22y=true;isc.A=isc.SearchForm.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$36w="DateItem";isc.B.push(isc.A.createItem=function isc_SearchForm_createItem(_1,_2,_3,_4,_5){var _6=this.getDataSource(),_7=_6?_6.getField(_1[this.fieldIdProperty])!=null:false;if(_7){var _8=isc.FormItemFactory.getItemClassName(_1,_2,this),_9=isc.FormItemFactory.getItemClass(_8);if(_9==isc.DateItem&&_1&&(_1.useTextField==null))
_1.useTextField=true;if(_1.allowEmptyValue==null)_1.allowEmptyValue=true}
return this.invokeSuper(isc.SearchForm,"createItem",_1,_2,_3,_4,_5)}
,isc.A.submitValues=function isc_SearchForm_submitValues(_1,_2){if(this.search!=null){return this.search(this.getValuesAsCriteria(),this)}}
,isc.A.validate=function isc_SearchForm_validate(_1,_2,_3){if(this.validateTypeOnly){return this.invokeSuper(isc.SearchForm,"validate",_1,_2,true)}else{return this.invokeSuper(isc.SearchForm,"validate",_1,_2,_3)}}
,isc.A.getEditorType=function isc_SearchForm_getEditorType(_1){var _2=_1.type;if(_2=="date"&&_1.editorType==null)return"DateRangeItem";return this.Super("getEditorType",arguments)}
);isc.B._maxIndex=isc.C+4;isc.A=isc.SearchForm.getPrototype();isc.A.showFilterFieldsOnly=true;isc.A.validateTypeOnly=true;isc.SearchForm.registerStringMethods({search:"criteria,form"});isc.ClassFactory.defineClass("ValuesManager");isc.A=isc.ValuesManager.getPrototype();isc.A.unknownErrorMessage=null;isc.A=isc.ValuesManager.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.init=function isc_ValuesManager_init(){this.ns.ClassFactory.addGlobalID(this);if(this.unknownErrorMessage==null){this.unknownErrorMessage=isc.Canvas.getPrototype().unknownErrorMessage}
if(this.dataSource)this.bindToDataSource(this.dataSource);this.values=isc.addProperties({},this.values);if(this.members!=null){var _1=this.members;this.members=null;if(!isc.isAn.Array(_1))_1=[_1];for(var i=0;i<_1.length;i++){this.addMember(_1[i])}}
this.rememberValues()}
,isc.A.destroy=function isc_ValuesManager_destroy(){var _1=this.members;if(_1){for(var i=_1.length-1;i>=0;i--){this.removeMember(_1[i])}}
window[this.getID()]=null}
,isc.A.$36x=function isc_ValuesManager__saveDataReply(_1,_2,_3){if(!this.suppressServerDataSync&&_2&&_2.status>=0&&_3!=null){if(isc.isAn.Array(_3))_3=_3[0];if(_1.data)_1.data=isc.shallowClone(_1.data);this.setValues(_3)}
this.$36y={request:_1,response:_2,data:_3};this.formSavedComplete()}
,isc.A.$41=function isc_ValuesManager__updateMultipleMemberValue(_1,_2,_3,_4){_2=(_2!=null)?this.$3f(_1,_2):_1;return this.$20l(_2,_3,_4)}
,isc.A.$20l=function isc_ValuesManager__updateValue(_1,_2,_3){if(this.$36z)return;if(isc.isA.DynamicForm(_3)&&_3.getItem(_1)==null){this.$360(_3,_1);return}
var _4;var _5=_3.getFullDataPath();if(isc.isA.String(_1)&&_1.startsWith(isc.Canvas.$3a)){_4=true}else if(_5){_1=(_1!=null)?this.$3f(_5,_1):_5;_4=true}else{_4=_1.contains(isc.Canvas.$3a)}
if(!_4){this.values[_1]=_2}else{isc.DynamicForm.$3d(_1,_2,this.values,_3,true)}
if(_4&&this.autoSynchronize!==false){var _6=_1.split(isc.Canvas.$3a);if(parseInt(_6[_6.length-1])==_6[_6.length-1]){this.synchronizeMembers(_3)}else{var _7=this.getFieldsForDataPath(_1);this.$36z=true;for(var i=0;i<_7.length;i++){if(_7[i].form==_3)continue;_7[i].saveValue(_2)}
delete this.$36z}}}
,isc.A.synchronizeMembers=function isc_ValuesManager_synchronizeMembers(_1){if(!this.members)return;this.$36z=true;for(var i=0;i<this.members.length;i++){if(!_1||this.members[i].selectionComponent==_1){this.$361(this.members[i])}}
delete this.$36z}
,isc.A.$3f=function isc_ValuesManager__combineDataPaths(_1,_2){return isc.DynamicForm.$3f(_1,_2)}
,isc.A.$360=function isc_ValuesManager__itemlessValueWarning(_1,_2){this.logWarn("Member Form: "+_1+" has explicitly specified value for field[s] '"+_2+"', but has"+" no item associated with this fieldName. Ignoring this value. "+"Values may be set for fields with no associated form item directly "+"on the valuesManager via valuesManager.setValues(), but not on "+"member forms. See ValuesManager documentation for more info.")}
,isc.A.$20m=function isc_ValuesManager__clearValue(_1,_2){var _3=_2.getFullDataPath();if(_3)_1=this.$3f(_3,_1);return isc.DynamicForm.$3c(_1,this.values)}
,isc.A.bindToDataSource=function isc_ValuesManager_bindToDataSource(_1){if(!isc.isA.DataSource(_1))_1=isc.DataSource.getDataSource(_1);if(_1!=null)this.dataSource=_1}
,isc.A.setDataSource=function isc_ValuesManager_setDataSource(_1,_2){this.bindToDataSource(_1)}
,isc.A.getDataSource=function isc_ValuesManager_getDataSource(){if(isc.isA.String(this.dataSource)){if(this.serviceNamespace||this.serviceName){this.dataSource=this.lookupSchema()}else{var _1=isc.DS.get(this.dataSource);if(_1!=null)return _1;_1=this.getWindow()[this.dataSource];if(_1&&isc.isA.DataSource(_1))return(this.dataSource=_1)}}
return this.dataSource}
,isc.A.lookupSchema=function isc_ValuesManager_lookupSchema(){var _1;if(this.serviceName)_1=isc.WebService.getByName(this.serviceName,this.serviceNamespace);else _1=isc.WebService.get(this.serviceNamespace);if((this.serviceNamespace||this.serviceName)&&_1==null){this.logWarn("Could not find WebService definition: "+(this.serviceName?"serviceName: "+this.serviceName:"")+(this.serviceNamespace?"   serviceNamespace: "+this.serviceNamespace:"")+this.getStackTrace())}
if(!isc.isA.String(this.dataSource)){this.logWarn("this.dataSource was not a String in lookupSchema");return}
if(_1)return _1.getSchema(this.dataSource)}
,isc.A.getDataSourceField=function isc_ValuesManager_getDataSourceField(_1){var _2=this.getDataSource();if(!_2||!_1)return null;_1=_1.trim("/");var _3=this.getDataSource(),_4=_1.split("/"),_5;for(var i=0;i<_4.length;i++){if(isc.isAn.emptyString(_4[i]))continue;var _7=_4[i];_5=_3.getField(_7);_3=_5?isc.DataSource.getDataSource(_5.type):_3}
return _5}
,isc.A.getItems=function isc_ValuesManager_getItems(){if(!this.members)return;var _1=[];for(var i=0;i<this.members.length;i++){var _3=this.members[i];if(!_3.getItems)continue;_1.addList(_3.getItems())}
return _1}
,isc.A.getFields=function isc_ValuesManager_getFields(){return this.getItems()}
,isc.A.getItem=function isc_ValuesManager_getItem(_1,_2){return this.$362(_1,true,_2)}
,isc.A.getField=function isc_ValuesManager_getField(_1){return this.getItem(_1)}
,isc.A.getFieldsForDataPath=function isc_ValuesManager_getFieldsForDataPath(_1){return this.getItem(_1,true)}
,isc.A.getMembers=function isc_ValuesManager_getMembers(){return this.members}
,isc.A.getMember=function isc_ValuesManager_getMember(_1){var _2=window[_1];if(this.members&&this.members.contains(_2))return _2;return null}
,isc.A.getMemberForField=function isc_ValuesManager_getMemberForField(_1,_2){return this.$362(_1,false,_2)}
,isc.A.$362=function isc_ValuesManager__findMemberByField(_1,_2,_3){if(!this.members||_1==null||isc.isAn.emptyString(_1))return null;var _4=_1.trim(isc.Canvas.$3a);var _5=_4.split(isc.Canvas.$3a);var _6=_3?[]:null;for(var i=0;i<this.members.length;i++){var _8=this.members[i],_9=_8.getFullDataPath();if(_9==isc.Canvas.$3a||isc.isAn.emptyString(_9)){_9=null}else if(_9!=null){_9=_9.trim(isc.Canvas.$3a)}
if(_5&&_5.length>0&&_9!=null){var _10=null;for(var _11=0;_11<_5.length;_11++){_10=!_10?_5[_11]:(_10+isc.Canvas.$3a+_5[_11]);if(_9.endsWith(isc.Canvas.$3a)){_9=_9.substring(0,_9.length-1)}
if(_9==_10){if(!_2&&(_11==_5.length-1)){if(!_3)return _8;_6.add(_8);break}
if(_8.getField){var _12=_5.slice(_11+1).join(isc.Canvas.$3a);var _13=_8.getField(_12);if(_13){if(_2){if(!isc.isA.FormItem(_13))_13=null;if(_3){if(_13)_6.add(_13)}else{return _13}}else{if(_3)_6.add(_8);else return _8}}}}}}else{if(this.members[i].getItem){var _14=this.members[i].getField(_1);if(!_14){if(_1.startsWith(isc.Canvas.$3a)){_14=this.members[i].getField(_1.substring(1))}}
if(_14){if(_2){if(!isc.isA.FormItem(_14))_14=null;if(_3){if(_14)_6.add(_14)}else{return _14}}else{if(_3)_6.add(_8);else return _8}}}}}
return _3?_6:null}
,isc.A.getFileItemForm=function isc_ValuesManager_getFileItemForm(){if(!this.members)return;var _1=false,_2;for(var i=0;i<this.members.length;i++){if(this.members[i].getFileItemForm==null)continue;var _4=this.members[i].getFileItemForm();if(_4){if(_1){this.logWarn("ValuesManager defined with more than one member form "+" containing a FileItem. This is not supported - binary data may "+"only be uploaded from one FileItem when saving ValuesManager data")}else{_2=_4;_1=true}}}
return _2}
,isc.A.validate=function isc_ValuesManager_validate(){if(this.disableValidation)return true;if(this.dataSource&&this.dataSource.useLocalValidators!=null&&this.useLocalValidators==false)return true;this.clearHiddenErrors();var _1=true,_2=this.dataSource?isc.addProperties({},this.getDataSource().getFields()):null,_3={},_4={},_5=false;this.buildDataPathsRecursively(_4,"",this.getDataSource());if(this.members){for(var i=0;i<this.members.length;i++){if(!isc.isA.DynamicForm(this.members[i]))continue;var _7=this.members[i],_8=_7.disableValidation,_9=this.members[i].getItems();if(!_8){if(_7.handleHiddenValidationErrors!=null){this.logInfo("form level 'handleHiddenValidationErrors' method suppressed "+"in favor of valuesManager level handler","validation");_7.$363=_7.handleHiddenValidationErrors}
_7.handleHiddenValidationErrors=this.$364}
for(var j=0;j<_9.length;j++){var _11=_9[j].getFullDataPath()||_9[j].getFieldName();_5=_5||(_11&&_11.contains(isc.Canvas.$3a));if(_2&&this.members[i].getDataSource()==this.getDataSource()){delete _2[_11]}
if(_4&&_11){delete _4[_11.trim(isc.Canvas.$3a)]}}
var _12=_8?true:_7.validate(true,true)
_1=(_1&&_12);if(!_8){if(_7.$365)_7.handleHiddenValidationErrors=_7.$365;else delete _7.handleHiddenValidationErrors}
if(!_12&&!(_7.isDrawn()&&_7.isVisible())){this.addHiddenErrors(_7.errors,_7)}}}
var _13=this.getValues(),_14={},_15=_5?_4:_2;for(var _11 in _15){var _16=_15[_11],_3=_16.validators,_17=isc.DynamicForm.$3e(_11,_13);if(_3!=null){for(var i=0;i<_3.length;i++){var _18=_3[i];if(!_18)continue;if(_17==null&&_18.type!='required'&&_18.type!="requiredIf")
{continue}
if(!this.processValidator(_16,_18,_17,null,_13)){if(_14[_11]==null)_14[_11]=[];var _19=_18.errorMessage||this.unknownErrorMessage;_14[_11].add(_19)}}}
if(_14[_11]&&_14[_11].length==1)_14[_11]=_14[_11][0]}
this.addHiddenErrors(_14);this.$21m(true);if(isc.getKeys(_14).length>0)_1=false;return _1}
,isc.A.buildDataPathsRecursively=function isc_ValuesManager_buildDataPathsRecursively(_1,_2,_3){if(!isc.isA.DataSource(_3))return;if(_3.$366){this.logWarn("detected ds loop at: "+_2+", refusing to recurse further");return}
_3.$366=true;var _4=_3.getFields();for(var _5 in _4){_1[_2+_5]=_4[_5];if(_3.fieldIsComplexType(_5)){var _6=_3.getSchema(_4[_5].type);this.buildDataPathsRecursively(_1,_2+_5+isc.Canvas.$3a,_6)}}
delete _3.$366}
,isc.A.getValidatedValues=function isc_ValuesManager_getValidatedValues(){if(!this.validate())return null;return this.getValues()}
,isc.A.$364=function isc_ValuesManager__handleHiddenFormErrors(_1){var _2=this.valuesManager;_2.addHiddenErrors(_1,this);return false}
,isc.A.clearHiddenErrors=function isc_ValuesManager_clearHiddenErrors(){delete this.hiddenErrors}
,isc.A.addHiddenErrors=function isc_ValuesManager_addHiddenErrors(_1,_2){if(_1==null||isc.isAn.emptyObject(_1))return;if(!this.hiddenErrors)this.hiddenErrors={};if(_2){if(isc.isA.Canvas(_2))_2=_2.getID()}else _2=this.getID();if(!this.hiddenErrors[_2])this.hiddenErrors[_2]={};for(var _3 in _1){this.hiddenErrors[_2][_3]=this.$367(this.hiddenErrors[_2][_3],_1[_3])}}
,isc.A.getHiddenErrors=function isc_ValuesManager_getHiddenErrors(_1){if(!_1){this.synchHiddenErrors()}
if(!this.hiddenErrors)return null;var _2={};for(var _3 in this.hiddenErrors){this.assembleHiddenErrorsRecursively(_2,this.hiddenErrors[_3])}
return _2}
,isc.A.assembleHiddenErrorsRecursively=function isc_ValuesManager_assembleHiddenErrorsRecursively(_1,_2,_3,_4){if(_3==null)_3="";var _5=_3;if(_4!=null)_5+="["+_4+"]";if(isc.isA.List(_2)){for(var i=0;i<_2.length;i++){if(_2[i]!==null){if(isc.isAn.Object(_2[i])){this.assembleHiddenErrorsRecursively(_1,_2[i],_5,i)}else{if(_1[_5]==null)_1[_5]=[];_1[_5][i]=_2[i]}}}}else if(isc.isAn.Object(_2)){for(var _7 in _2){if(isc.isAn.Object(_2[_7])){if(_5==""){this.assembleHiddenErrorsRecursively(_1,_2[_7],_7)}else{this.assembleHiddenErrorsRecursively(_1,_2[_7],_5+isc.Canvas.$3a+_7)}}else{if(_5==""){_1[_7]=_2[_7]}else{_1[_5+isc.Canvas.$3a+_7]=_2[_7]}}}}else{_1[_5]=_2}
return _1}
,isc.A.synchHiddenErrors=function isc_ValuesManager_synchHiddenErrors(){var _1=this.hiddenErrors,_2=this.getID();if(_1&&_1[_2]){for(var _3 in _1[_2]){var _4=_1[_2][_3],_5=this.getItem(_3),_6=_5?_5.form:null;if(_5){_6.addFieldErrors(_3,_4);delete _1[_2][_3]}}}
var _7=_1[_2];_1=this.hiddenErrors={};if(_7)_1[_2]=_7;if(this.members){for(var i=0;i<this.members.length;i++){if(!isc.isA.DynamicForm(this.members[i]))continue;var _9=this.members[i],_10=_9.getID(),_11=_9.errors;if(!_11||isc.isAn.emptyObject(_11))continue;if(!_9.isVisible()||!_9.isDrawn()){_11=isc.addProperties({},_11);_1[_10]=_11}else{for(var _3 in _11){var _5=_9.getItem(_3);if(!_5){if(!_1[_2])_1[_2]={};_1[_2][_3]=_11[_3];delete _11[_3]}else if(!_5.visible){if(!_1[_10])_1[_10]={};_1[_10][_3]=_11[_3]}}}}}}
,isc.A.processValidator=function isc_ValuesManager_processValidator(_1,_2,_3,_4,_5){return isc.Validator.processValidator(_1,_2,_3,_4,_5)}
,isc.A.$21m=function isc_ValuesManager__handleHiddenValidationErrors(_1){var _2=this.getHiddenErrors(_1);if(_2==null||isc.getKeys(_2).length==0)return;var _3;if(this.handleHiddenValidationErrors){_3=this.handleHiddenValidationErrors(_2)}
if(_3==false)return;var _4="Validation failed with the following errors:";var _5=isc.isAn.Array(_2)?_2:[_2];for(var i=0;i<_5.length;i++){var _7=_5[i];for(var _8 in _7){var _9=_2[_8];if(!isc.isAn.Array(_9))_9=[_9];if(_9.length==0)continue;_4+="\n"+_8+":";for(var i=0;i<_9.length;i++){_4+=(i==0?"- ":"\n - ")+_9[i]}}}
this.logWarn(_4,"validation")}
,isc.A.setErrors=function isc_ValuesManager_setErrors(_1,_2){this.clearHiddenErrors();if(isc.isA.List(_1))_1=_1[0];var _3=(this.members?this.members.duplicate():[]);for(var i=0;i<_3.length;i++){if(!isc.isA.DynamicForm(_3[i]))continue;var _5=_3[i],_6=!_5.isVisible()||!_5.isDrawn(),_7=_5.getItems(),_8={},_9={},_10=_5.getSelectionChain();for(var j=0;j<_7.getLength();j++){var _12=_7[j],_13=_12.getFullDataPath(),_14=_12.getFieldName(),_15=this.getItemErrors(_1,_13,_10);if(_15!=null){_8[_14]=_15;if(_6||!_12.visible){_9[_14]=_15}
this.deleteItemErrors(_1,_13,_10)}}
_5.setErrors(_8,false);if(!isc.isAn.emptyObject(_9))
this.addHiddenErrors(_9,_5)}
this.addHiddenErrors(_1);if(_2)this.showErrors(true)}
,isc.A.getItemErrors=function isc_ValuesManager_getItemErrors(_1,_2,_3){var _4=_2.trim(isc.Canvas.$3a),_5=_4.contains(isc.Canvas.$3a);if(isc.isAn.Array(_1))_1=_1[0];if(!_5){var _6=_1[_2]}else{var _7=_4.split(isc.Canvas.$3a),_6=_1,_8=0;for(var i=0;i<_7.length;i++){_6=_6[_7[i]];if(isc.isAn.Array(_6)){if(_3.length>_8){_6=_6[_3[_8++]]}else{_6=_6[0]}}
if(!_6)break}}
if(_6){if(!isc.isAn.Array(_6))_6=[_6];var _10=[];for(var i=0;i<_6.length;i++){if(_6[i].errorMessage){_10.add(_6[i].errorMessage)}else{_10.add(_6[i])}}
return _10.length>1?_10:_10[0]}}
,isc.A.deleteItemErrors=function isc_ValuesManager_deleteItemErrors(_1,_2,_3){var _4=_2.trim(isc.Canvas.$3a),_5=_4.contains(isc.Canvas.$3a);if(isc.isAn.Array(_1))_1=_1[0];if(!_5){delete _1[_2]}else{var _6=_4.split(isc.Canvas.$3a);var _7=_1,_8=[],_9=0;for(var i=0;i<_6.length;i++){_8.add(_7);_7=_7[_6[i]];if(isc.isAn.Array(_7)){if(_3.length>_9){_7=_7[_3[_9++]]}else{_7=_7[0]}}
if(!_7)break}
if(_7)delete _7;for(var i=_8.length-1;i>=0;i--){if(isc.isAn.emptyObject(_8[i])){delete _8[i]}}}}
,isc.A.$367=function isc_ValuesManager__addFieldErrors(_1,_2){if(!_1)return _2;if(!_2)return _1;if(!isc.isAn.Array(_1))_1=[_1];if(isc.isA.String(_2))_1.add(_2);else _1.addList(_2);return _1}
,isc.A.addFieldErrors=function isc_ValuesManager_addFieldErrors(_1,_2,_3){var _4=true;var _5=this.getMemberForField(_1);if(_5!=null&&isc.isA.DynamicForm(_5)){_5.addFieldErrors(_1,_2,false);var _6=_5.getItem();if(_5.isVisible()&&_5.isDrawn()&&_6&&_6.visible){_4=false}}
if(_4){if(!this.hiddenErrors)this.hiddenErrors={};var _7=_5?_5.getID():this.getID();if(!this.hiddenErrors[_7])this.hiddenErrors[_7]={};this.hiddenErrors[_7][_1]=this.$367(this.hiddenErrors[_7][_1],_2)}
if(_3)this.showFieldErrors(_1)}
,isc.A.setFieldErrors=function isc_ValuesManager_setFieldErrors(_1,_2,_3){var _4=true;var _5=this.getMemberForField(_1);if(_5!=null&&isc.isA.DynamicForm(_5)){_5.setFieldErrors(_1,_2,false);var _6=_5.getItem();if(_5.isVisible()&&_5.isDrawn()&&_6&&_6.visible){_4=false}}
if(_4){if(!this.hiddenErrors)this.hiddenErrors={};this.hiddenErrors[_1]=_2}
if(_3)this.showFieldErrors(_1)}
,isc.A.clearErrors=function isc_ValuesManager_clearErrors(_1){this.setErrors({},_1)}
,isc.A.clearFieldErrors=function isc_ValuesManager_clearFieldErrors(_1,_2){var _3=this.getMemberForField(_1);if(_3&&isc.isA.DynamicForm(_3))_3.clearFieldErrors(_1,_2);if(this.hiddenErrors)delete this.hiddenErrors[_1]}
,isc.A.getErrors=function isc_ValuesManager_getErrors(){var _1=isc.addProperties({},this.getHiddenErrors(true));if(this.members){for(var i=0;i<this.members.length;i++){if(!isc.isA.DynamicForm(this.members[i]))continue;isc.addProperties(_1,this.members[i].getErrors())}}
if(!isc.isA.emptyObject(_1))return _1
return null}
,isc.A.getFieldErrors=function isc_ValuesManager_getFieldErrors(_1){var _2=this.getMemberForField(_1)
if(_2&&isc.isA.DynamicForm(_2))return _2.getFieldErrors(_1);if(this.hiddenErrors&&this.hiddenErrors[this.getID()])
return this.hiddenErrors[this.getID()][_1]}
,isc.A.hasErrors=function isc_ValuesManager_hasErrors(){if(this.hiddenErrors&&!isc.isA.emptyObject(this.hiddenErrors)){for(var _1 in this.hiddenErrors){if(this.hiddenErrors[_1]&&!isc.isAn.emptyObject(this.hiddenErrors[_1]))
return true}}
if(this.members==null)return false;for(var i=0;i<this.members.length;i++){if(isc.isA.DynamicForm(this.members[i])&&this.members[i].hasErrors())return true}
return false}
,isc.A.hasFieldErrors=function isc_ValuesManager_hasFieldErrors(_1){var _2=this.getMemberForField(_1);if(_2&&isc.isA.DynamicForm(_2)&&_2.hasFieldErrors(_1))return true;var _3=this.getHiddenErrors(true);if(_3&&_3[_1]!=null)return true;return false}
,isc.A.showErrors=function isc_ValuesManager_showErrors(_1){if(this.members){for(var i=0;i<this.members.length;i++){if(!isc.isA.DynamicForm(this.members[i]))continue;if(!this.members[i].isDrawn()||!this.members[i].isVisible())continue;this.members[i].markForRedraw("ValuesManager validation errors")}}
if(this.hiddenErrors!=null){this.$21m(_1)}}
,isc.A.showFieldErrors=function isc_ValuesManager_showFieldErrors(_1){var _2=this.getMemberForField(_1);if(_2&&isc.isA.DynamicForm(_2)&&_2.isVisible()&&_2.isDrawn()){var _3=_2.getItem(_1);if(_3&&_3.visible){_3.redraw("Validation errors modified");return}}
this.$21m()}
,isc.A.getFilterCriteria=function isc_ValuesManager_getFilterCriteria(){var _1={};if(this.members){for(var i=0;i<this.members.length;i++){isc.addProperties(_1,this.members[i].getFilterCriteria())}}
var _3=this.getValues(),_4;for(var _5 in _3){if(_1[_5]!==_4)delete _3[_5]}
isc.addProperties(_1,isc.DataSource.filterCriteriaForFormValues(_3));return _1}
,isc.A.getValues=function isc_ValuesManager_getValues(){if(this.members!=null){var _1=isc.EH.getFocusCanvas();if(this.members.contains(_1)&&_1.updateFocusItemValue)_1.updateFocusItemValue()}
return isc.addProperties({},this.values)}
,isc.A.setValues=function isc_ValuesManager_setValues(_1){if(isc.isAn.Array(_1)){var _2=isc.isA.Object(_1[0]);this.logWarn("values specified as an array."+(_2?" Treating the first item in the array as intended values.":" Ignoring specified values."));if(_2)_1=_1[0];else _1=null}
var _3={};this.$368(_3,_1,this.getDataSource());_1=_3;this.values=_1;if(this.members){for(var i=0;i<this.members.length;i++){this.$361(this.members[i])}}
this.rememberValues()}
,isc.A.setData=function isc_ValuesManager_setData(_1){return this.setValues(_1)}
,isc.A.clearValues=function isc_ValuesManager_clearValues(){this.setValues({})}
,isc.A.getMemberValues=function isc_ValuesManager_getMemberValues(_1){var _2=this.getMember(_1);if(_2!=null)return _2.getValues()}
,isc.A.setMemberValues=function isc_ValuesManager_setMemberValues(_1,_2){var _3=this.getMember(_1);if(_3!=null)return _3.setValues(_2)}
,isc.A.rememberValues=function isc_ValuesManager_rememberValues(){var _1=this.getValues();this.$20d={}
this.$20e=[]
this.$368(this.$20d,_1,this.getDataSource(),null,true);return this.$20d}
,isc.A.getOldValues=function isc_ValuesManager_getOldValues(){var _1={};isc.addProperties(_1,this.$20d);return _1}
,isc.A.getChangedValues=function isc_ValuesManager_getChangedValues(){return this.valuesHaveChanged(true)}
,isc.A.$368=function isc_ValuesManager__cloneValues(_1,_2,_3,_4,_5,_6){if(_2==null)return;var _7=_3?_3.deepCloneOnEdit:this.deepCloneOnEdit,_8=_7==null?isc.DataSource.deepCloneOnEdit:_7;if(isc.isAn.Array(_2)){for(var i=0;i<_2.length;i++){var _10=_2[i];if(isc.isA.Function(_10))continue;if(isc.isAn.Instance(_2[_13])||isc.isA.Class(_2[_13]))continue;if(_10==null||isc.isA.String(_10)||isc.isA.Boolean(_10)||isc.isA.Number(_10))
{_1[_1.length]=_10}else if(isc.isA.Date(_10)){_1[_1.length]=new Date(_10.getTime())}else if(isc.isAn.Object(_10)){var _11;if(isc.isAn.Array(_10)){_11=_1[_1.length]=[]}else{_11=_1[_1.length]={}}
this.$368(_11,_10,_3,(_5?_4:null),_5)}}
return}
if(_2.$5z!=null){_2=isc.JSONEncoder.$5y(_2)}
var _12={__ref:true,$4o:true,$4p:true};if(isc.DataSource.cloneValuesSafely){if(!_6)_6=[];if(_6.contains(_2)){_1=_2;return}
_6.add(_2)}
for(var _13 in _2){if(isc.isA.Function(_2[_13]))continue;if(_12[_13]==true)continue;if(isc.isAn.Instance(_2[_13])||isc.isA.Class(_2[_13]))continue;var _14;if(_5){if(_4){_14=_4+_13}else{_14=_13}
var _15=this.getItem(_14);if(_15&&_15.isSetToDefaultValue()){this.$20e.add(_14)}}
var _16=_2[_13];if(isc.isA.Date(_16)){_1[_13]=_16.duplicate()}else if(isc.isAn.Object(_16)&&!isc.isAn.Array(_16)){var _17=_3?_3.getField(_13):null;if(!_17){_1[_13]=_2[_13]}else{if(_17.deepCloneOnEdit==true||(_17.deepCloneOnEdit==null&&_8))
{if(isc.DataSource.cloneValuesSafely){if(_6.contains(_16)){_1[_13]=_2[_13];continue}
_6.add(_16)}
_1[_13]={};this.$368(_1[_13],_16,isc.DataSource.get(_17.type),_5?(_14+isc.Canvas.$3a):null,_5,_6)}else{_1[_13]=_2[_13]}}}else if(isc.isAn.Array(_16)){var _17=_3?_3.getField(_13):null;if(!_17){_1[_13]=_2[_13]}else{if(_17.deepCloneOnEdit==true||(_17.deepCloneOnEdit==null&&_8))
{if(isc.DataSource.cloneValuesSafely){if(_6.contains(_16)){_1[_13]=_2[_13];continue}
_6.add(_16)}
_1[_13]=[];this.$368(_1[_13],_16,isc.DataSource.get(_17.type),_5?(_14+isc.Canvas.$3a):null,_5,_6)}else{_1[_13]=_2[_13]}}}else{_1[_13]=_2[_13]}}}
,isc.A.resetValues=function isc_ValuesManager_resetValues(){var _1={};for(var _2 in this.$20d){if(this.$20e.contains(_2))continue;if(isc.isA.Date(this.$20d[_2])){var _3=this.getValue(_2);if(isc.isA.Date(_3)){_3.setTime(this.$20d[_2].getTime())
_1[_2]=_3}else{_1[_2]=this.$20d[_2].duplicate()}}else{_1[_2]=this.$20d[_2]}}
this.setValues(_1)}
,isc.A.valuesHaveChanged=function isc_ValuesManager_valuesHaveChanged(_1){var _2=this.getValues();var _3=this.$20d||{};return isc.DynamicForm.valuesHaveChanged(this,_1,_2,_3)}
,isc.A.getValue=function isc_ValuesManager_getValue(_1,_2){return isc.DynamicForm.$3e(_1,this.values,_2)}
,isc.A.setValue=function isc_ValuesManager_setValue(_1,_2){var _3=false,_4,_5;if(this.members){var _6=this.getItem(_1,true);for(var i=0;i<_6.length;i++){var _8=_6[i];if(_8&&_8.setValue){if(_2===_5)_8.clearValue();else _8.setValue(_2);_3=true}}}
if(!_3){if(_2===_5){isc.DynamicForm.$3c(_1,this.values)}else{isc.DynamicForm.$3d(_1,_2,this.values)}}
var _9=this.$362(_1,false,true);if(_9){for(var i=0;i<_9.length;i++){if(_4&&_4.setData){var _10=_1;if(_1.indexOf(isc.Canvas.$3a)!=-1){_10=_1.substring(0,_1.lastIndexOf(isc.Canvas.$3a));_4.setData(isc.DynamicForm.$3e(_10,this.values))}}}}}
,isc.A.clearValue=function isc_ValuesManager_clearValue(_1){this.setValue(_1)}
,isc.A.addMember=function isc_ValuesManager_addMember(_1,_2){if(isc.isA.String(_1))_1=window[_1];if(!isc.isA.Canvas(_1)){this.logWarn("addMember() passed invalid object: "+this.echo(_1)+" - this should be a Canvas instance");return}
if(_1.valuesManager!=null)_1.valuesManager.removeMember(_1);if(this.members==null)this.members=[];var _3=_1.getDataSource();if(_3!=null&&!_2&&_3!=this.getDataSource()){this.logWarn("addMember(): mismatched DataSources; new member form "+_1+" has dataSource: '"+_3.ID+"', valuesManager has DataSource "+(this.getDataSource()!=null?"'"+this.getDataSource().ID+"'":"[NONE]"))}
if(this.getDataSource()!=null&&_1.isMultipart&&_1.isMultipart()&&_1.isMultipart())
{this.logWarn("addMember(): new member form "+_1+" is flagged as using multipart encoding. Multipart forms require direct form "+"submission to transfer uploaded files to the server - any uploaded files from "+"this member form will be dropped when saving values from this ValuesManager to "+"the server.")}
if(_1.dataArity=="single"&&_1.autoTrackSelection){if(_1.selectionComponent==null||_1.$369){var _4=_1.getFullDataPath(),_5=_4?this.getDataSourceField(_4):null,_6=isc.DynamicForm.$3e(_4,this.values),_7=isc.isAn.Array(_6)||(_5&&_5.multiple);if(_7){var _8=this.getMemberForField(_4,true);if(_8&&_8.length>0){for(var i=0;i<_8.length;i++){var _10=_8[i];if(_10.dataArity=="multiple"){_1.setSelectionComponent(_10);_1.$369=true;break}}}}}}else{var _4=_1.getFullDataPath(),_11=this.getMemberForField(_4,true);if(_11&&_11.length>0){for(var i=0;i<_11.length;i++){if(_11[i].dataArity=="single"&&_11[i].autoTrackSelection&&(_11[i].selectionComponent==null||_11[i].$369==true))
{_11[i].setSelectionComponent(_1);_11[i].$369=true}}}}
if(_1.dataArity=="multiple"&&_1.autoTrackSelection){var _4=_1.getFullDataPath(),_12=_4&&_4.contains(isc.Canvas.$3a);if(_12){var _13=_4.split(isc.Canvas.$3a);_4="/";for(var i=_13.length-2;i>=0;i--){for(var j=0;j<=i;j++){_4+=_13[j];if(j!=i)_4+="/"}
var _5=this.getDataSourceField(_4),_6=isc.DynamicForm.$3e(_4,this.values),_7=isc.isAn.Array(_6)||(_5&&_5.multiple);if(_7)break}}
if(_7){var _8=this.getMemberForField(_4,true);if(_8&&_8.length>0){for(var i=0;i<_8.length;i++){var _10=_8[i];if(_10.dataArity=="multiple"){_1.setSelectionComponent(_10);_1.$369=true;break}}}}
var _4=_1.getFullDataPath();if(_4&&_4!=""){var _15=this.members;for(var i=0;i<_15.length;i++){if(_15[i]==_1)continue;if(_15[i].dataArity=="single")continue;var _16=_15[i].getFullDataPath();if(_16&&_16!=_4&&_16.startsWith(_4))
{if(_15[i].selectionComponent!=null){if(_15[i].$369){var _17=_15[i].selectionComponent.getFullDataPath();if(_4.length>_17.length){_15[i].setSelectionComponent(_1);_15[i].$369=true}}}}}}}
this.members.add(_1);if(_1.dataSource==null&&this.dataSource!=null&&_1.getFields){var _18=isc.isA.DynamicForm(_1)?_1.$3s:_1.getFields();_18=_18||_1.getFields();_1.setDataSource(this.dataSource,_18)}
this.$361(_1,true);_1.valuesManager=this;_1.$3r=_2;this.rememberValues()}
,isc.A.$361=function isc_ValuesManager__setMemberValues(_1,_2){if(_1.getFields==null)return;var _3=_1.getFullDataPath(),_4=this.getField(_3),_5=isc.DynamicForm.$3e(_3,this.values,_1,true),_6=isc.isAn.Array(_5)||(_4&&_4.multiple),_7=_1.selectionComponent;if(_6){if(_1.dataArity=="single"){if(_7!=null){_5=_5[_7.getRecordIndex(_7.getSelectedRecord())]}else{if(isc.isAn.Array(_5))_5=_5[0]}}}else{if(_5!=null&&_1.dataArity=="multiple")_5=[_5]}
if(!isc.isA.DynamicForm(_1)){if(!_1.setData)return;var _8=_1.getFullDataPath(),_9=_2?_1.getData():null;if(_5==null){if(_2)isc.DynamicForm.$3d(_8,_9)}else{if(_2&&_9!=null&&!isc.isAn.emptyObject(_9)&&!isc.isAn.emptyArray(_9))
{this.logInfo("ValuesManager member:"+_1.getID()+" has existing values:"+this.echo(_9)+", replacing with values from this valuesManager:"+this.echo(_5))}
_1.setData(_5)}}else{var _10=_1.getItems(),_11;for(var i=0;i<_10.getLength();i++){var _13=_10[i];var _14=_13.getDataPath()||_13.getFieldName();if(!_14)continue;var _15=_14,_3=_1.getFullDataPath();if(_3&&!_15.startsWith(isc.Canvas.$3a)){_15=this.$3f(_3,_15)}
var _16=isc.DynamicForm.$3e(_15,_1.values,_1,true);var _17=isc.DynamicForm.$3e(_15,this.values,_1,true);if(_16!==_11){if(_17!==_11){this.logInfo("Member form "+_1+" has specified value for field '"+_15+"' which collides with an already specified value in this "+"ValuesManager. Resetting the value on the member form.");_1.setValue(_15,_17)}else{if(!_2)_1.clearValue(_15)}}else if(_17!==_11){_1.setValue(_15,_17)}
var _18=_1.getValue(_15);if(_18===_11){isc.DynamicForm.$3c(_15,this.values)}else{isc.DynamicForm.$3d(_15,_1.getValue(_15),this.values,_1)}}
if(_2){this.$37a(_1)}}}
,isc.A.$37a=function isc_ValuesManager__findItemlessFormValues(_1,_2,_3,_4,_5){if(_2==null)_2=_1.getValues();if(_4==null)_4=[];for(var _6 in _2){var _7=_3?this.$3f(_3,_6):_6;if(!_1.getItem(_7)){var _8=_2[_6];if(!isc.isAn.Object(_8)||isc.isA.Date(_8)||isc.isAn.Array(_8)){_4.add(_7);_1.clearValue(_7)}else{this.$37a(_1,_8,_3,_4,true)}}}
if(!_5&&_4.length>0){this.$360(_1,_4)}}
,isc.A.addMembers=function isc_ValuesManager_addMembers(_1){if(!isc.isAn.Array(_1))this.addMember(_1);else{for(var i=0;i<_1.length;i++){this.addMember(_1[i])}}}
,isc.A.removeMember=function isc_ValuesManager_removeMember(_1){if(isc.isA.String(_1)){_1=isc.Class.getArrayItem(_1,this.members);if(_1==null)return}else if(this.members&&!this.members.contains(_1))return;if(this.members)this.members.remove(_1);delete _1.valuesManager}
,isc.A.removeMembers=function isc_ValuesManager_removeMembers(_1){if(!isc.isAn.Array(_1))this.removeMember(_1);else{for(var i=0;i<_1.length;i++){this.removeMember(_1[i])}}}
,isc.A.getPrintHTML=function isc_ValuesManager_getPrintHTML(){var _1=this.getValues(),_2=isc.StringBuffer.create();_2.append("<TABLE border=1><TR><TD align='center' style='font-weight:bold;'>Field</TD>","<TD align='center' style='font-weight:bold;'>Value</TD>");for(var _3 in _1){_2.append("<TR><TD>",_3,"</TD><TD>",_1[_3],"</TD></TR>")}
_2.append("</TABLE>");return _2.toString()}
,isc.A.getAllDBCs=function isc_ValuesManager_getAllDBCs(_1){var _2=[];if(_1==null){for(var i=0;i<this.members.length;i++){_2.addAll(this.getAllDBCs(this.members[i]))}
var _4=[];for(var i=0;i<_2.length;i++){if(!_4.contains(_2[i]))_4.add(_2[i])}
return _4}
if(isc.isA.DataBoundComponent(_1))_2.add(_1);var _5=_1.children;if(!_5)return _2;for(var i=0;i<_5.length;i++){var _1=_5[i];if(isc.isA.DataBoundComponent(_1))_2.add(_1);_2.addAll(this.getAllDBCs(_1))}
return _2}
);isc.B._maxIndex=isc.C+73;isc.ValuesManager.registerStringMethods({handleHiddenValidationErrors:"errors",submitValues:"values,valuesManager"});if(isc.Window){isc.ClassFactory.defineClass("ColorPicker",isc.Window);isc.A=isc.ColorPicker;isc.A.MORE_BUTTON_TITLE="More >>";isc.A.LESS_BUTTON_TITLE="<< Less";isc.A=isc.ColorPicker;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.getSharedColorPicker=function isc_c_ColorPicker_getSharedColorPicker(_1,_2){_1=_1||{};if(!isc.isA.ColorPicker(this.$37b)){this.$37b=isc.ColorPicker.create(_1)}else{if(_1.colorSelected==null)delete this.$37b.colorSelected;if(_1.colorChanged==null)delete this.$37b.colorChanged;this.$37b.setProperties(_1)}
if(!_2){var _3=this.$37b;if(_3.$37c!=_3.defaultPickMode){_3.$37c=_3.defaultPickMode;if(_3.$37c=='simple'){_3.removeComplexElements();if(_3.allowComplexMode){_3.modeToggleButton.setTitle(isc.ColorPicker.MORE_BUTTON_TITLE)}}else{if(!_3.$37d){_3.createComplexElements()}
_3.addComplexElements();_3.modeToggleButton.setTitle(isc.ColorPicker.LESS_BUTTON_TITLE)}}
_3.setHtmlColor(_3.defaultColor);_3.$37e=this.defaultOpacity}
return this.$37b}
);isc.B._maxIndex=isc.C+1;isc.addGlobal("ColorChooser",isc.ColorPicker);isc.A=isc.ColorPicker.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.title="Select a Color";isc.A.autoSize=true;isc.A.isModal=true;isc.A.autoCenter=true;isc.A.autoDraw=false;isc.A.showMinimizeButton=false;isc.A.layoutMargin=2;isc.A.canFocus=false;isc.A.showOkButton=true;isc.A.okButtonConstructor=isc.IButton;isc.A.okButtonDefaults={title:"OK",width:80,autoParent:"buttonLayout",click:function(){if(this.creator.colorSelected){this.creator.colorSelected(this.creator.getHtmlColor(),this.creator.getOpacity())}
this.creator.hide()}};isc.A.showCancelButton=true;isc.A.cancelButtonConstructor=isc.IButton;isc.A.cancelButtonDefaults={title:"Cancel",width:80,autoParent:"buttonLayout",click:function(){this.creator.hide()}};isc.A.showModeToggleButton=true;isc.A.modeToggleButtonConstructor=isc.IButton;isc.A.modeToggleButtonDefaults={title:isc.ColorPicker.MORE_BUTTON_TITLE,width:80,autoParent:"buttonLayout",click:function(){this.creator.$37f()}};isc.A.showButtonLayout=true;isc.A.buttonLayoutConstructor="HLayout";isc.A.buttonLayoutDefaults={autoParent:"contentLayout"};isc.A.defaultColor="#808080";isc.A.colorButtonSize=20;isc.A.colorButtonBaseStyle="colorChooserCell";isc.A.colorArray=["#000000","#996100","#636300","#006300","#006366","#000080","#636399","#636363","#800000","#FF6600","#808000","#8000FF","#008080","#0000FF","#666699","#808080","#FF0000","#FF9900","#99CC00","#639966","#63CCCC","#6366FF","#800080","#999999","#FF00FF","#FFCC00","#FFFF00","#00FF00","#00FFFF","#00CCFF","#996366","#C0C0C0","#FF99CC","#FFCC99","#FFFF99","#CCFFCC","#CCFFFF","#99CCFF","#CC99FF","#FFFFFF"];isc.A.swatchWidth=170;isc.A.swatchHeight=170;isc.A.lumStep=4;isc.A.lumWidth=15;isc.A.supportsTransparency=true;isc.A.opacityText="Lorem ipsum dolor sit amet, consectetuer adipiscing elit.";isc.A.swatchImageURL="[SKIN]ColorPicker/spectrum.png";isc.A.crosshairImageURL="[SKIN]ColorPicker/crosshair.png";isc.A.basicColorLabel="Basic Colors:";isc.A.selectedColorLabel="Selected Color:";isc.A.opacitySliderLabel="Opacity:";isc.A.defaultOpacity=100;isc.A.autoPosition=true;isc.A.autoCenterOnShow=true;isc.A.defaultPickMode="simple";isc.A.allowComplexMode=true;isc.A.$37g=true;isc.B.push(isc.A.closeClick=function isc_ColorPicker_closeClick(){this.hide()}
);isc.B._maxIndex=isc.C+1;isc.A=isc.ColorPicker.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.show=function isc_ColorPicker_show(){if(this.autoPosition){this.autoCenter=false;var _1=isc.EH.getLastEvent();this.placeNear(_1.x,_1.y)}else{if(this.autoCenterOnShow)this.autoCenter=true}
this.Super("show",arguments)}
,isc.A.initWidget=function isc_ColorPicker_initWidget(){this.$37c=this.defaultPickMode;this.basicColorLayout=isc.VLayout.create({autoDraw:false});for(var i=0;i<5;i++){var _2=isc.HLayout.create({autoDraw:false,layoutBottomMargin:1,membersMargin:1,height:this.colorButtonSize});for(var j=0;j<8;j++){var _4=isc.StatefulCanvas.create({autoDraw:false,width:this.colorButtonSize,height:this.colorButtonSize,overflow:"hidden",title:"",backgroundColor:this.colorArray[i*8+j],baseStyle:this.colorButtonBaseStyle,showRollOver:true,picker:this,click:function(){this.picker.setHtmlColor(this.backgroundColor);if(this.picker.$37c=="simple"){this.picker.$37h(this.backgroundColor)}}});_2.addMember(_4)}
this.basicColorLayout.addMember(_2)}
this.leftHandLayout=isc.VLayout.create({autoDraw:false});this.leftHandLayout.addMember(this.basicColorLayout);this.innerContentLayout=isc.HLayout.create({autoDraw:false,align:"center",members:[this.leftHandLayout]});this.contentLayout=isc.VLayout.create({autoDraw:false,members:[this.innerContentLayout]});this.addItem(this.contentLayout);if(this.$37c=="simple"){this.showOkButton=false;if(!this.allowComplexMode){this.showModeToggleButton=false}}else{this.showModeToggleButton=false}
this.addAutoChildren(["buttonLayout","okButton","cancelButton","modeToggleButton"]);if(this.$37c=="complex"){this.createComplexElements();this.addComplexElements()}
this.setHtmlColor(this.defaultColor);this.$37i();this.setOpacity(this.defaultOpacity);this.Super("initWidget",arguments)}
,isc.A.createComplexElements=function isc_ColorPicker_createComplexElements(){if(this.$37c!='complex'){return}
this.$37d=isc.DynamicForm.create({autoDraw:false,cellPadding:1,padding:10,width:65,fields:[{name:"pickerRedVal",title:"Red",type:"text",width:"40",defaultValue:this.$37j,prompt:"The Red component of the selected color",picker:this,changed:function(_2,_3,_4){this.picker.setRed(_4)}},{name:"pickerGrnVal",title:"Green",type:"text",width:"40",defaultValue:this.$37k,prompt:"The Green component of the selected color",picker:this,changed:function(_2,_3,_4){this.picker.setGreen(_4)}},{name:"pickerBluVal",title:"Blue",type:"text",width:"40",defaultValue:this.$37l,prompt:"The Blue component of the selected color",picker:this,changed:function(_2,_3,_4){this.picker.setBlue(_4)}},{name:"pickerHtmlVal",title:"HTML",type:"text",width:"65",defaultValue:this.$37m,prompt:"The selected color's HTML coding",picker:this,changed:function(_2,_3,_4){this.picker.setHtmlColor(_4)}}]});this.$37n=isc.DynamicForm.create({autoDraw:false,cellPadding:1,padding:10,width:65,fields:[{name:"pickerHueVal",title:"Hue",type:"text",width:"40",defaultValue:this.$37o,prompt:"The Hue (base tone) of the selected color",picker:this,changed:function(_2,_3,_4){this.picker.setHue(_4)}},{name:"pickerSatVal",title:"Sat",type:"text",width:"40",defaultValue:this.$37p,prompt:"The Saturation (color purity) of the selected color",picker:this,changed:function(_2,_3,_4){this.picker.setSaturation(_4)}},{name:"pickerLumVal",title:"Lum",type:"text",width:"40",defaultValue:this.$37q,prompt:"The Luminosity (brightness) of the selected color",picker:this,changed:function(_2,_3,_4){this.picker.setLuminosity(_4)}}]});this.$37r=isc.Img.create({autoDraw:false,imageWidth:16,imageHeight:16,src:this.crosshairImageURL,width:16,height:16,imageType:"normal",canDrag:true,canDrop:true,dragAppearance:"target",picker:this,dragMove:function(){this.picker.$37s=true;this.picker.$37t(this.parentElement.getOffsetX(),this.parentElement.getOffsetY())}});this.$37u=isc.Canvas.create({autoDraw:false,width:100,height:40,backgroundColor:this.getHtmlColor()});this.$37v=isc.Canvas.create({autoDraw:false,width:60,height:40,overflow:"hidden",border:"1px black solid",contents:this.opacityText,children:[this.$37u]});this.$37w=isc.VStack.create({lumWidth:15,height:this.swatchHeight,margin:5,border:"1px solid black"});for(var i=0;i<this.swatchHeight/ this.lumStep;i++){this.$37w.addMember(isc.Canvas.create({width:this.lumWidth,height:this.lumStep,margin:0,padding:0,overflow:"hidden"}))}
this.$37x=isc.Slider.create({minValue:0,maxValue:240,numValues:240,margin:5,length:this.swatchHeight,width:10,showTitle:false,showValue:false,showRange:false});if(this.supportsTransparency){this.$37y=isc.Slider.create({autoDraw:false,vertical:false,margin:5,minValue:0,maxValue:100,numValues:100,length:100,height:12,width:100,thumbThickWidth:15,thumbThinWidth:10,showTitle:false,showValue:false,showRange:false,value:100});this.$37z=isc.HLayout.create({autoDraw:false,layoutLeftMargin:5,layoutRightMargin:5,membersMargin:5,members:[isc.Label.create({autoDraw:false,margin:5,contents:this.opacitySliderLabel,width:this.swatchWidth-105,height:10}),this.$37y]})}
this.$370=isc.VLayout.create({autoDraw:false,layoutLeftMargin:5,layoutRightMargin:5,membersMargin:5,members:[isc.HLayout.create({autoDraw:false,height:this.swatchHeight,members:[isc.Img.create({autoDraw:false,margin:5,width:this.swatchWidth+12,height:this.swatchHeight+12,src:this.swatchImageURL,overflow:"hidden",border:"1px black solid",picker:this,click:function(){this.picker.$37t(this.getOffsetX(),this.getOffsetY())},children:[this.$37r]}),this.$37w,this.$37x]}),isc.HLayout.create({autoDraw:false,layoutLeftMargin:5,layoutRightMargin:5,membersMargin:5,members:[isc.Label.create({autoDraw:false,margin:5,contents:this.selectedColorLabel,width:this.swatchWidth-63,height:15}),this.$37v]})]});if(this.$37x)this.observe(this.$37x,"valueChanged","observer.$371()");if(this.$37y)this.observe(this.$37y,"valueChanged","observer.$372()")}
,isc.A.initComplexElements=function isc_ColorPicker_initComplexElements(){this.$37x.setValue(this.$37q);this.$37i();this.$373(this.$37o,this.$37p);this.setHtmlColor(this.$37m);this.$37u.setBackgroundColor(isc.ColorUtils.hslToHtml(this.$37o,this.$37p,this.$37q));if(this.supportsTransparency){this.$37u.setOpacity(this.$37e);this.$37y.setValue(this.$37e)}}
,isc.A.addComplexElements=function isc_ColorPicker_addComplexElements(){if(this.$37c!='complex'){return}
this.showOkButton=true;this.setAutoChild("okButton");this.basicLabel=isc.Label.create({autoDraw:false,margin:5,contents:this.basicColorLabel,width:100,height:15});this.formLayout=isc.HLayout.create({autoDraw:false,members:[this.$37d,this.$37n]});this.leftHandLayout.addMember(this.basicLabel,0);this.leftHandLayout.addMember(this.formLayout);if(this.supportsTransparency){this.$370.addMember(this.$37z)}
this.innerContentLayout.addMember(this.$370);this.initComplexElements()}
,isc.A.removeComplexElements=function isc_ColorPicker_removeComplexElements(){if(this.$37c=='complex'){return}
this.showOkButton=false;this.setAutoChild("okButton");if(this.formLayout){this.leftHandLayout.removeMembers([this.basicLabel,this.formLayout]);this.innerContentLayout.removeMember(this.$370)}}
,isc.A.setSupportsTransparency=function isc_ColorPicker_setSupportsTransparency(_1){this.supportsTransparency=_1;if(this.$37c=='complex'){if(this.supportsTransparency){this.$370.addMember(this.$37z)}else{this.$370.removeMember(this.$37z)}}}
,isc.A.getRed=function isc_ColorPicker_getRed(){return this.$37j}
,isc.A.getGreen=function isc_ColorPicker_getGreen(){return this.$37k}
,isc.A.getBlue=function isc_ColorPicker_getBlue(){return this.$37l}
,isc.A.getHue=function isc_ColorPicker_getHue(){return this.$37o}
,isc.A.getSaturation=function isc_ColorPicker_getSaturation(){return this.$37p}
,isc.A.getLuminosity=function isc_ColorPicker_getLuminosity(){return this.$37q}
,isc.A.getHtmlColor=function isc_ColorPicker_getHtmlColor(){return this.$37m}
,isc.A.getOpacity=function isc_ColorPicker_getOpacity(){return this.$37e}
,isc.A.setRed=function isc_ColorPicker_setRed(_1){if(_1<0)this.$37j=0;else if(_1>255)this.$37j=255;else this.$37j=_1/ 1;if(this.$37c=='complex'){this.$37d.setValue("pickerRedVal",this.$37j)}
if(this.$37g===true)
this.$374('rgb')}
,isc.A.setGreen=function isc_ColorPicker_setGreen(_1){if(_1<0)this.$37k=0;else if(_1>255)this.$37k=255;else this.$37k=_1/ 1;if(this.$37c=='complex'){this.$37d.setValue("pickerGrnVal",this.$37k)}
if(this.$37g===true)
this.$374('rgb')}
,isc.A.setBlue=function isc_ColorPicker_setBlue(_1){if(_1<0)this.$37l=0;else if(_1>255)this.$37l=255;else this.$37l=_1/ 1;if(this.$37c=='complex'){this.$37d.setValue("pickerBluVal",this.$37l)}
if(this.$37g===true)
this.$374('rgb')}
,isc.A.setHue=function isc_ColorPicker_setHue(_1){if(_1<0)this.$37o=0;else if(_1>239)this.$37o=239;else this.$37o=_1/ 1;if(this.$37c=='complex'){this.$37n.setValue("pickerHueVal",this.$37o)}
if(this.$37g===true)
this.$374('hsl')}
,isc.A.setSaturation=function isc_ColorPicker_setSaturation(_1){if(_1<0)this.$37p=0;else if(_1>240)this.$37p=240;else this.$37p=_1/ 1;if(this.$37c=='complex'){this.$37n.setValue("pickerSatVal",this.$37p)}
if(this.$37g===true)
this.$374('hsl')}
,isc.A.setLuminosity=function isc_ColorPicker_setLuminosity(_1){if(_1<0)this.$37q=0;else if(_1>240)this.$37q=240;else this.$37q=_1/ 1;if(this.$37c=='complex'){this.$37n.setValue("pickerLumVal",this.$37q)}
if(this.$37g===true)
this.$374('hsl')}
,isc.A.setHtmlColor=function isc_ColorPicker_setHtmlColor(_1){if(isc.ColorUtils.encodingIsValid(_1)===true){this.$37m=_1.toUpperCase();if(this.$37c=='complex'){this.$37d.setValue("pickerHtmlVal",this.$37m)}
if(this.$37g===true)
this.$374('html')}}
,isc.A.setOpacity=function isc_ColorPicker_setOpacity(_1){if(this.$37c=='complex'&&this.supportsTransparency){if(_1<0)this.$37e=0;else if(_1>100)this.$37e=100;else this.$37e=_1/ 1;if(this.$37g===true)
this.$374('opacity')}}
,isc.A.$374=function isc_ColorPicker__changeColor(_1){if(_1=='rgb'){var _2=isc.ColorUtils.rgbToHsl(this.$37j,this.$37k,this.$37l);this.$37g=false;this.setHue(_2.h);this.setSaturation(_2.s);this.setLuminosity(_2.l);this.setHtmlColor(isc.ColorUtils.rgbToHtml(this.$37j,this.$37k,this.$37l));this.$37g=true;this.$373(this.$37o,this.$37p)}else if(_1=='hsl'){var _3=isc.ColorUtils.hslToRgb(this.$37o,this.$37p,this.$37q);this.$37g=false;this.setRed(_3.r);this.setGreen(_3.g);this.setBlue(_3.b);this.setHtmlColor(isc.ColorUtils.rgbToHtml(this.$37j,this.$37k,this.$37l));this.$37g=true;if(this.$37o!=this.$375||this.$37p!=this.$376){this.$373(this.$37o,this.$37p)}}else if(_1=='html'){var _3=isc.ColorUtils.htmlToRgb(this.$37m);this.$37g=false;this.setRed(_3.r);this.setGreen(_3.g);this.setBlue(_3.b);var _2=isc.ColorUtils.rgbToHsl(this.$37j,this.$37k,this.$37l);this.setHue(_2.h);this.setSaturation(_2.s);this.setLuminosity(_2.l);this.$37g=true;this.$373(this.$37o,this.$37p)}
if(this.$37c=='complex'){this.$37x.setValue(this.$37q)}
if(this.$37c=='complex'){this.$37u.setBackgroundColor(isc.ColorUtils.hslToHtml(this.$37o,this.$37p,this.$37q))}
if(this.$37o!=this.$375||this.$37p!=this.$376){this.$37i()}
if(this.$37c=='complex')
this.$37u.setOpacity(this.$37e);this.$375=this.$37o;this.$376=this.$37p;if(this.colorChanged)this.colorChanged()}
,isc.A.$37h=function isc_ColorPicker__oneClickColorSelected(_1){this.hide();if(this.colorSelected)this.colorSelected(_1)}
,isc.A.$373=function isc_ColorPicker__positionCrossHair(_1,_2){if(this.$37c!='complex'){return}
if(this.$37s===true){this.$37s=false;return}
var _3=_1/ 239.0;var _4=_2/ 240.0;_3*=this.swatchWidth;_4=this.swatchHeight-(_4*this.swatchHeight);_3=parseInt(_3)-8;_4=parseInt(_4)-8;this.$37r.setLeft(_3);this.$37r.setTop(_4)}
,isc.A.$37t=function isc_ColorPicker__crosshairMoved(_1,_2){_1-=5;_2-=5;_1/=this.swatchWidth;_2=1.0-_2/ this.swatchHeight;this.$37g=false;this.setHue(Math.floor(_1*239.0+0.5));this.$37g=true;this.setSaturation(Math.floor(_2*240.0+0.5))}
,isc.A.$37i=function isc_ColorPicker__setLumVals(){if(this.$37c!='complex'){return}
for(var i=0;i<this.swatchHeight/ this.lumStep;i++){this.$37w.members[i].setBackgroundColor(isc.ColorUtils.hslToHtml(this.$37o,this.$37p,240-(i*240/(this.swatchHeight/ this.lumStep))))}}
,isc.A.$371=function isc_ColorPicker__lumSliderChanged(){var _1=this.$37x.getValue();if(this.$37q!=_1){this.setLuminosity(_1)}}
,isc.A.$372=function isc_ColorPicker__opSliderChanged(){this.setOpacity(this.$37y.getValue())}
,isc.A.$37f=function isc_ColorPicker__togglePickMode(){if(this.$37c=="simple"){this.$37c="complex";if(!this.$370){this.createComplexElements()}
this.addComplexElements();this.modeToggleButton.setTitle(isc.ColorPicker.LESS_BUTTON_TITLE)}else{this.$37c="simple";this.removeComplexElements();this.modeToggleButton.setTitle(isc.ColorPicker.MORE_BUTTON_TITLE)}
this.modeToggleButton.setState("")}
);isc.B._maxIndex=isc.C+31;isc.ColorPicker.registerStringMethods({colorChanged:"",colorSelected:"color,opacity"});isc.defineClass("ColorUtils",isc.Class);isc.A=isc.ColorUtils;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.hexToDec=function isc_c_ColorUtils_hexToDec(_1){return parseInt(_1,16)}
,isc.A.decToHex=function isc_c_ColorUtils_decToHex(_1){var d=_1/ 1;var h=d.toString(16);if(h.length==1){h="0"+h}
return h}
,isc.A.brightness=function isc_c_ColorUtils_brightness(_1,_2,_3){var _4=isc.ColorUtils.rgbToHsl(_1,_2,_3);return(_4.l/ 240.0)}
,isc.A.encodingIsValid=function isc_c_ColorUtils_encodingIsValid(_1){return(_1.substring(0,1)=='#'&&isc.isA.color(_1))}
,isc.A.rgbToHtml=function isc_c_ColorUtils_rgbToHtml(_1,_2,_3){var _4='#'+isc.ColorUtils.decToHex(_1)+isc.ColorUtils.decToHex(_2)+isc.ColorUtils.decToHex(_3);return _4}
,isc.A.hslToHtml=function isc_c_ColorUtils_hslToHtml(_1,_2,_3){var _4=isc.ColorUtils.hslToRgb(_1,_2,_3);var _5='#'+isc.ColorUtils.decToHex(_4.r)+isc.ColorUtils.decToHex(_4.g)+isc.ColorUtils.decToHex(_4.b);return _5}
,isc.A.htmlToRgb=function isc_c_ColorUtils_htmlToRgb(_1){var r=_1.substring(1,3);var g=_1.substring(3,5);var b=_1.substring(5,7);return{r:isc.ColorUtils.hexToDec(r),g:isc.ColorUtils.hexToDec(g),b:isc.ColorUtils.hexToDec(b)}}
,isc.A.htmlToHsl=function isc_c_ColorUtils_htmlToHsl(_1){var r=_1.substring(1,3);var g=_1.substring(3,5);var b=_1.substring(5,7);return isc.ColorUtils.rgbToHsl(isc.ColorUtils.hexToDec(r),isc.ColorUtils.hexToDec(g),isc.ColorUtils.hexToDec(b))}
,isc.A.rgbToHsl=function isc_c_ColorUtils_rgbToHsl(_1,_2,_3){var _4=_1/ 255.0;var _5=_2/ 255.0;var _6=_3/ 255.0;var _7=Math.min(Math.min(_4,_5),_6);var _8=Math.max(Math.max(_4,_5),_6);var _9=_8-_7;var h=0,s=0,l=0;l=(_8+_7)/2.0;if(_8==_7){s=0;h=0}else{if(l<0.5){s=(_8-_7)/(_8+_7)}else{s=(_8-_7)/(2.0-_8-_7)}
if(_4==_8)
h=(_5-_6)/_9;else if(_5==_8)
h=2+(_6-_4)/_9;else
h=4+(_4-_5)/_9}
h=Math.floor(h*40+0.5);if(h<0)h+=240;s=Math.floor(s*240+0.5);l=Math.floor(l*240+0.5);return{h:h,s:s,l:l}}
,isc.A.hslToRgb=function isc_c_ColorUtils_hslToRgb(_1,_2,_3){var _4=_1/ 239.0;var _5=_2/ 240.0;var _6=_3/ 240.0;var _7,_8,_9,_10,_11;var r=0,g=0,b=0;if(_5==0){r=_6;g=_6;b=_6}else{if(_6<0.5){_8=_6*(1.0+_5)}else{_8=(_6+_5)-(_6*_5)}
_7=(2.0*_6)-_8;_9=_4+0.3333;_10=_4;_11=_4-0.3333;if(_9<0)_9+=1.0;if(_10<0)_10+=1.0;if(_11<0)_11+=1.0;if(_9>1)_9-=1.0;if(_10>1)_10-=1.0;if(_11>1)_11-=1.0;if(_9*6.0<1)
r=_7+(_8-_7)*6.0*_9;else if(_9*2.0<1)
r=_8;else if(_9*3.0<2)
r=_7+(_8-_7)*(0.6667-_9)*6.0;else
r=_7;if(_10*6.0<1)
g=_7+(_8-_7)*6.0*_10;else if(_10*2.0<1)
g=_8;else if(_10*3.0<2)
g=_7+(_8-_7)*(0.6667-_10)*6.0;else
g=_7;if(_11*6.0<1)
b=_7+(_8-_7)*6.0*_11;else if(_11*2.0<1)
b=_8;else if(_11*3.0<2)
b=_7+(_8-_7)*(0.6667-_11)*6.0;else
b=_7}
r=Math.floor(r*255.0+0.5);g=Math.floor(g*255.0+0.5);b=Math.floor(b*255.0+0.5);return{r:r,g:g,b:b}}
);isc.B._maxIndex=isc.C+10}
isc.ClassFactory.defineClass("NestedEditorItem","CanvasItem");isc.A=isc.NestedEditorItem.getPrototype();isc.A.shouldSaveValue=true;isc.A.editorConstructor="DynamicForm";isc.A.editorDefaults={itemChanged:function(_1,_2){this.creator.updateValue(this.getValuesAsCriteria())}};isc.A=isc.NestedEditorItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.init=function isc_NestedEditorItem_init(){this.$377();this.Super("init",arguments)}
,isc.A.isEditable=function isc_NestedEditorItem_isEditable(){return true}
,isc.A.$377=function isc_NestedEditorItem__createEditor(){var _1;var _2={};if(this.form.dataSource){_1=isc.DataSource.getDataSource(this.form.dataSource);var _3=_1.getField(this.name);if(_3){_2.dataSource=_1.getFieldDataSource(_3)}}
if(this.form&&this.form.showComplexFieldsRecursively){_2.showComplexFields=true;_2.showComplexFieldsRecursively=true}else{_2.showComplexFields=false}
this.addAutoChild("editor",_2);this.canvas=this.editor}
,isc.A.setValue=function isc_NestedEditorItem_setValue(_1){this.editor.setValues(_1)}
,isc.A.updateValue=function isc_NestedEditorItem_updateValue(_1){this.$20l(_1)}
,isc.A.$29n=function isc_NestedEditorItem__shouldAllowExpressions(){return false}
);isc.B._maxIndex=isc.C+6;isc.ClassFactory.defineClass("NestedListEditorItem","CanvasItem");isc.A=isc.NestedListEditorItem.getPrototype();isc.A.shouldSaveValue=true;isc.A.editorConstructor="ListEditor";isc.A.editorDefaults={inlineEdit:false,height:155,saveRecord:function(){if(!this.form.validate())return false;var _1=this.form.getValues();this.showList();if(this.inlineEdit){this.grid.setEditValues(this.grid.getEditRow(),_1)}else{if(this.form.saveOperationType=="add"){this.grid.addData(_1)}else{isc.addProperties(this.currentRecord,_1);this.grid.markForRedraw()}
if(!this.inlineEdit){this.form.clearValues()}
this.creator.updateValue(this.grid.data)}
return true}};isc.A=isc.NestedListEditorItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.init=function isc_NestedListEditorItem_init(){this.$377();this.Super("init",arguments)}
,isc.A.isEditable=function isc_NestedListEditorItem_isEditable(){return true}
,isc.A.$377=function isc_NestedListEditorItem__createEditor(){var _1;var _2={};if(this.form.dataSource){_1=isc.DataSource.getDataSource(this.form.dataSource);var _3=_1.getField(this.name);if(_3){_2.dataSource=_1.getFieldDataSource(_3)}}
if(this.form&&this.form.showComplexFieldsRecursively){_2.formProperties={showComplexFields:true,showComplexFieldsRecursively:true};_2.gridProperties={showComplexFields:true,showComplexFieldsRecursively:true,canRemoveRecords:true,saveLocally:true,data:[]}}else{_2.formProperties={showComplexFields:false};_2.gridProperties={showComplexFields:false,canRemoveRecords:true,saveLocally:true}}
this.addAutoChild("editor",_2);this.canvas=this.editor}
,isc.A.updateValue=function isc_NestedListEditorItem_updateValue(_1){this.editor.setData(_1);this.$20l(_1)}
,isc.A.setValue=function isc_NestedListEditorItem_setValue(_1){this.editor.setData(_1);this.Super("setValue",arguments)}
,isc.A.$29n=function isc_NestedListEditorItem__shouldAllowExpressions(){return false}
);isc.B._maxIndex=isc.C+6;isc.ClassFactory.defineClass("ViewFileItem","CanvasItem");isc.A=isc.ViewFileItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.shouldSaveValue=false;isc.A.colSpan="*";isc.A.height=20;isc.A.width="*";isc.A.overflow="visible";isc.A.canvasDefaults={_constructor:"Canvas",height:10,width:"100%"};isc.B.push(isc.A.isEditable=function isc_ViewFileItem_isEditable(){return false}
,isc.A.init=function isc_ViewFileItem_init(){this.addAutoChild("canvas");this.Super('init',arguments)}
,isc.A.setValue=function isc_ViewFileItem_setValue(_1){var _2=this.form,_3=_2.getValues();if(this.type=="imageFile"&&this.showFileInline!=false){this.canvas.setHeight("*");this.canvas.setWidth("*");this.canvas.setContents(this.getImageHTML()||"&nbsp;")}else{if(this.showFileInline==true){this.logWarn("setValue(): Unsupported field-type for showFileInline: "+this.type)}
this.canvas.setHeight(20);this.canvas.setWidth("*");this.canvas.setContents(this.getViewDownloadHTML(_1,_3)||"&nbsp;")}
this.Super("setValue",arguments)}
,isc.A.getViewDownloadHTML=function isc_ViewFileItem_getViewDownloadHTML(_1,_2){if(isc.isA.String(_1))return _1;if(_2==null)return null;var _3=this.nativeName||this.name,_4=_2[_3+"_filename"];if(_4==null||isc.isA.emptyString(_4))return this.emptyCellValue;var _5=isc.Canvas.imgHTML("[SKIN]actions/view.png",16,16,null,"style='cursor:"+isc.Canvas.HAND+"' onclick='"+this.getID()+".viewFile()'");var _6=isc.Canvas.imgHTML("[SKIN]actions/download.png",16,16,null,"style='cursor:"+isc.Canvas.HAND+"' onclick='"+this.getID()+".downloadFile()'");return"<nobr>"+_5+"&nbsp;"+_6+"&nbsp;"+_4+"</nobr>"}
,isc.A.getImageHTML=function isc_ViewFileItem_getImageHTML(){var _1=this.form.getValues(),_2=this.form.getField(this.name),_3=this.name+"$351",_4;if(!_1[_3]){var _5=isc.Canvas.getFieldImageDimensions(_2,_1);_4=_1[_3]=isc.Canvas.imgHTML(this.form.getDataSource().streamFile(_1,_2.name),_5.width,_5.height)}else
_4=_1[_3];return _4}
,isc.A.viewFile=function isc_ViewFileItem_viewFile(){isc.DS.get(this.form.dataSource).viewFile(this.form.getValues(),this.name)}
,isc.A.downloadFile=function isc_ViewFileItem_downloadFile(){isc.DS.get(this.form.dataSource).downloadFile(this.form.getValues(),this.name)}
,isc.A.$29n=function isc_ViewFileItem__shouldAllowExpressions(){return false}
);isc.B._maxIndex=isc.C+8;isc.defineClass("PanelHeader","ImgSectionHeader");isc.A=isc.PanelHeader.getPrototype();isc.A.width="100%";isc.A.height=22;isc.A.baseStyle="sectionHeader";isc.A=isc.PanelHeader.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.showSelectedIcon=false;isc.A.showRollOverIcon=false;isc.A.showDisabledIcon=false;isc.A.showDownIcon=false;isc.A.showFocusedIcon=false;isc.B.push(isc.A.initWidget=function isc_PanelHeader_initWidget(){this.setPanelTitleFromCanvas()}
,isc.A.setPanelTitleFromCanvas=function isc_PanelHeader_setPanelTitleFromCanvas(){if(this.canvas){if(this.canvas.title)this.title=this.canvas.title;if(this.canvas.icon)this.icon=this.canvas.icon}}
);isc.B._maxIndex=isc.C+2;isc.PanelHeader.registerStringMethods({iconClick:""});isc.A=isc.Canvas.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.panelHeaderDefaults={_constructor:"PanelHeader"};isc.A.panelHeaderProperties={};isc.A.panelHeaderPlacement="peer";isc.B.push(isc.A.setupPanelHeader=function isc_Canvas_setupPanelHeader(){if(!this.showPanelHeader)return;this.panelHeader=this.createAutoChild("panelHeader",{canvas:this,snapTo:"T"});if(isc.isA.Layout(this))this.panelHeaderPlacement="member";else this.panelHeaderPlacement="peer";this.panelHeaderPlacement="peer";if(this.panelHeaderPlacement=="member"){this.addMember(this.panelHeader,0)}
else if(this.panelHeaderPlacement=="peer"){this.addPeer(this.panelHeader);this.panelHeader.moveAbove(this);this.$1l(this.panelHeader,isc.Canvas.TOP)}
else if(this.panelHeaderPlacement=="custom"){}}
);isc.B._maxIndex=isc.C+1;isc.A=isc.Canvas.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.panelActionControls=[];isc.B.push(isc.A.refreshPanelControls=function isc_Canvas_refreshPanelControls(){var _1=this.panelControls||[];if(!this.panelHeader.controls)this.panelHeader.controls=[];else this.panelHeader.controls.setLength(0);for(var i=0;i<_1.length;i++){var _3=_1.get(i),_4=null;if(isc.isAn.Object(_3)){_4=isc.addProperties({},_3)}else if(_3.startsWith("action:")){var _5=_3.substring(7,_3.length),_6=isc.Canvas.getRegisteredAction(_5);if(this.canPerformAction(_6)){if(this.showActionInPanel(_6))
_4=this.getPanelActionControl(_6)}}else{_4=this.createAutoChild(_3)}
if(_4){this.panelHeader.controls.add(_4)}else{}}
var _7=this.panelHeader;_7.addControls()}
,isc.A.canPerformAction=function isc_Canvas_canPerformAction(_1){var _2=_1.name,_3=_1.enableProperty||"can"+_2.substring(0,1).toUpperCase()+_2.substring(1,_2.length);return this[_3]&&this[_3]==true?true:false}
,isc.A.showActionInPanel=function isc_Canvas_showActionInPanel(_1){return _1.showInPanel}
,isc.A.getPanelActionControl=function isc_Canvas_getPanelActionControl(_1){var _2=this.panelActionControls[_1.name],_3=_1.controlConstructor||"ImgButton";_2=isc.ClassFactory.newInstance(_3,{ID:this.getID()+"_"+_1.name,width:18,height:18,src:_1.icon,showRollOver:false,showDown:false,showDisabled:false,showFocused:false,actionTarget:this,actionObject:_1,prompt:_1.tooltip,click:function(){this.actionTarget[this.actionObject.methodName]()}});this.panelActionControls[_1.name]=_2;return this.panelActionControls[_1.name]}
,isc.A.showPrintPreview=function isc_Canvas_showPrintPreview(_1,_2,_3,_4){isc.Canvas.showPrintPreview(this,_1,_2,_3,_4)}
);isc.B._maxIndex=isc.C+5;isc.A=isc.Canvas;isc.A.$378={};isc.A=isc.Canvas;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.registerAction=function isc_c_Canvas_registerAction(_1){if(!this.$378[_1.name]){this.$378[_1.name]=_1}}
,isc.A.getRegisteredActionNames=function isc_c_Canvas_getRegisteredActionNames(){return isc.getKeys(this.$378)}
,isc.A.getRegisteredActions=function isc_c_Canvas_getRegisteredActions(){return isc.getValues(this.$378)}
,isc.A.getRegisteredActionIndex=function isc_c_Canvas_getRegisteredActionIndex(){var _1=this.getRegisteredActions(),_2=_1.makeIndex("name",false);return _2}
,isc.A.getRegisteredAction=function isc_c_Canvas_getRegisteredAction(_1){return this.$378[_1]}
,isc.A.isActionRegistered=function isc_c_Canvas_isActionRegistered(_1){return!this.$378[_1]?false:true}
);isc.B._maxIndex=isc.C+6;isc.defineClass("Action","Class");isc.A=isc.Action.getPrototype();isc.A.name=null;isc.A.title=null;isc.A.icon=null;isc.A.tooltip=null;isc.A.methodName=null;isc.A.controlConstructor="ImgButton";isc.A.enableProperty=null;isc.A.showInPanel=true;isc.Canvas.registerAction(isc.Action.create({name:"edit",title:"Edit",icon:"[SKINIMG]/actions/edit.png",tooltip:"Put the component into Edit mode",methodName:"startEditing",showInPanel:false}));isc.Canvas.registerAction(isc.Action.create({name:"editNew",title:"Edit New",icon:"[SKINIMG]/SectionHeader/opener_closed.png",tooltip:"Add a new Record to the component",methodName:"startEditingNew",showInPanel:false}));isc.Canvas.registerAction(isc.Action.create({name:"sort",title:"Sort",icon:"[SKINIMG]/actions/sort_ascending.png",tooltip:"Sort the records in the component",methodName:"sort",controlConstructor:"SortActionSelector",enableProperty:"canSortFields",showInPanel:false}));isc.Canvas.registerAction(isc.Action.create({name:"export",title:"Export",icon:"[SKINIMG]/actions/redo.png",tooltip:"Export the data in the component",methodName:"exportData",showInPanel:true}));isc.Canvas.registerAction(isc.Action.create({name:"print",title:"Print",icon:"[SKINIMG]/actions/print.png",tooltip:"Print the data in the component",methodName:"showPrintPreview",showInPanel:true}));isc.defineClass("SortActionSelector","DynamicForm");isc.A=isc.SortActionSelector.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.width=100;isc.A.height=20;isc.A.numCols=4;isc.A.fields=[{name:"sortField",showTitle:false,colSpan:2,type:"select",width:80,prompt:"Sort Field",startRow:false,endRow:false,changed:function(_1,_2,_3){var _4=_1.getField("sortDirection").getValue(),_5=_4?"ascending":"descending";if(_1.actionTarget.sort)_1.actionTarget.sort(_3,_5);else _1.actionTarget.data.sortByProperty(_3,_4)}},{name:"sortDirection",showTitle:true,showLabel:false,type:"checkbox",width:20,prompt:"Sort Direction: Checked is Ascending",startRow:false,endRow:false,changed:function(_1,_2,_3){var _4=_3,_5=_4?"ascending":"descending",_6=_1.getField("sortField").getValue();if(_1.actionTarget.sort)_1.actionTarget.sort(_6,_5);else _1.actionTarget.data.sortByProperty(_6,_4)}}];isc.B.push(isc.A.initWidget=function isc_SortActionSelector_initWidget(){this.Super("initWidget",arguments)}
,isc.A.draw=function isc_SortActionSelector_draw(){this.Super("draw",arguments);var _1=this.actionTarget.getDataSource(),_2=_1?isc.getValues(_1.getFields()):[],_3={};for(var i=0;i<_2.length;i++){var _5=_2.get(i);_3[_5.name]=_5.title}
this.getField("sortField").setValueMap(_3)}
);isc.B._maxIndex=isc.C+2;isc.defineClass("DataPathItem","TextItem");isc.A=isc.DataPathItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.operationsTreeDefaults={_constructor:"TTreeGrid",autoDraw:false,recordDoubleClick:function(){this.creator.operationSelected()},getIcon:function(_1){var _2=this.creator.form.creator,_3=(_2&&_2.getServiceElementIcon)?_2.getServiceElementIcon(_1):null;if(_3)return _3;return this.Super("getIcon",arguments)}};isc.A.operationsTreeSelectButtonDefaults={_constructor:"TButton",autoDraw:false,title:"Select",click:function(){if(this.creator.operationsTree.anySelected())
this.creator.operationSelected()}};isc.A.defaultIcons=[{src:"[SKINIMG]/actions/edit.png",click:"item.showOperationsTreeData()",width:16,height:16},{src:"[SKINIMG]/actions/remove.png",click:"item.clearFormValues()",width:16,height:16}];isc.A.baseManagedProperties=["dataPath","schemaDataSource","serviceName","serviceNamespace"];isc.B.push(isc.A.getPropertyName=function isc_DataPathItem_getPropertyName(_1){if(this.isInput)
return"input"+_1.substring(0,1).toUpperCase()+_1.substring(1);else return _1}
,isc.A.initManagedProperties=function isc_DataPathItem_initManagedProperties(){this.managedProperties=[];var _1=this.baseManagedProperties;for(var i=0;i<_1.length;i++){this.managedProperties.add(this.getPropertyName(_1[i]))}}
,isc.A.keyPress=function isc_DataPathItem_keyPress(_1,_2,_3){if(_3!="Arrow_Left"&&_3!="Arrow_Right"&&_3!="Home"&&_3!="End")return false;this.Super("keyPress",arguments)}
,isc.A.init=function isc_DataPathItem_init(){this.icons=isc.clone(this.defaultIcons);this.initManagedProperties();this.Super("init",arguments);if(this.operationsTreeData){this.addAutoChildren(["operationsTree","operationsTreeSelectButton"])}}
,isc.A.showOperationsTreeData=function isc_DataPathItem_showOperationsTreeData(){if(!this.operationsTreeData)return;if(!this.operationsTree)
this.addAutoChildren(["operationsTree","operationsTreeSelectButton"]);var _1;if(isc.isA.Tree(this.operationsTreeData)){_1=this.operationsTreeData}else{_1=isc.Tree.create({modelType:"children",root:{children:this.operationsTreeData},nameProperty:"name",childrenProperty:"children"});_1.openAll()}
this.operationsTree.setData(_1);var _2=this.isInput?"formInputs":"formOutputs";var _3=_2+"/"+this.getValue(),_4=_1.find(_3);if(_4)this.operationsTree.selectRecord(_4);if(!this.schemaDialog){this.schemaDialog=isc.TWindow.create({title:"Select element from message",autoCenter:true,height:"90%",width:"60%",isModal:true,showModalMask:true,items:[isc.VLayout.create({width:"100%",height:"100%",members:[this.operationsTree,this.operationsTreeSelectButton]})]})}else this.schemaDialog.show()}
,isc.A.operationSelected=function isc_DataPathItem_operationSelected(){var _1=this.operationsTree,_2=_1.data,_3=_1.getSelectedRecord();this.schemaDialog.hide();this.setDataPathProperties(_3)}
,isc.A.setDataPathProperties=function isc_DataPathItem_setDataPathProperties(_1){var _2=_1.initData||_1.defaults;this.dataPathProps=isc.applyMask(_2,this.managedProperties);if(this.logIsInfoEnabled()){this.logInfo("setDPProps, editNode: "+this.echoAll(_1)+" defaults: "+this.echo(_2)+", managedProps: "+this.managedProperties+", props: "+this.echo(this.dataPathProps))}
this.saveFormValues()}
,isc.A.saveFormValues=function isc_DataPathItem_saveFormValues(){for(var i=0;i<this.managedProperties.length;i++){var _2=this.managedProperties[i],_3=this.dataPathProps[_2];this.form.setValue(_2,_3)}
if(this.form.saveProperties){this.form.saveProperties(this.dataPathProps,this.form.currentComponent)}}
,isc.A.clearFormValues=function isc_DataPathItem_clearFormValues(){for(var i=0;i<this.managedProperties.length;i++){this.form.setValue(this.managedProperties[i],null)}
if(this.form.saveProperties){this.form.saveProperties(this.dataPathProps,this.form.currentComponent)}}
);isc.B._maxIndex=isc.C+9;if(isc.ListGrid){isc.defineClass("RelativeDateItem","CanvasItem");isc.A=isc.RelativeDateItem;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$379={ms:"millisecond",s:"second",mn:"minute",h:"hour",d:"day",w:"week",m:"month",q:"quarter",y:"year",dc:"decade",c:"century"};isc.B.push(isc.A.getAbsoluteDate=function isc_c_RelativeDateItem_getAbsoluteDate(_1,_2,_3,_4){var _5=false;if(isc.SimpleType.inheritsFrom(_3,"date")&&!isc.SimpleType.inheritsFrom(_3,"datetime"))
{_5=true}
var _6=isc.DateUtil.getAbsoluteDate(_1,_2,_4,_5);return _6}
,isc.A.getPeriodName=function isc_c_RelativeDateItem_getPeriodName(_1){var _2=_1.toLowerCase();return this.$379[_2]||_2}
,isc.A.getPeriodKey=function isc_c_RelativeDateItem_getPeriodKey(_1){if(this.$38a==null){this.$38a=isc.makeReverseMap(this.$379)}
_1=_1.toLowerCase();return this.$38a[_1]||_1}
,isc.A.getRelativeDateParts=function isc_c_RelativeDateItem_getRelativeDateParts(_1){return isc.DateUtil.getRelativeDateParts(_1)}
,isc.A.isRelativeDate=function isc_c_RelativeDateItem_isRelativeDate(_1){return isc.DateUtil.isRelativeDate(_1)}
);isc.B._maxIndex=isc.C+5;isc.A=isc.RelativeDateItem.getPrototype();isc.A.height=20;isc.A.cellHeight=20;isc.A.canFocus=true;isc.A.timeUnitOptions=["day","week","month"];isc.A.showPastOptions=true;isc.A.showFutureOptions=true;isc.A.rangeRoundingGranularity={"year":"day","month":"day","week":"day","day":"day","hour":"minute","minute":"second","second":"second"};isc.A.millisecondsAgoTitle="N milliseconds ago";isc.A.secondsAgoTitle="N seconds ago";isc.A.minutesAgoTitle="N minutes ago";isc.A.hoursAgoTitle="N hours ago";isc.A.daysAgoTitle="N days ago";isc.A.weeksAgoTitle="N weeks ago";isc.A.monthsAgoTitle="N months ago";isc.A.yearsAgoTitle="N years ago";isc.A.millisecondsFromNowTitle="N milliseconds from now";isc.A.secondsFromNowTitle="N seconds from now";isc.A.minutesFromNowTitle="N minutes from now";isc.A.hoursFromNowTitle="N hours from now";isc.A.daysFromNowTitle="N days from now";isc.A.weeksFromNowTitle="N weeks from now";isc.A.monthsFromNowTitle="N months from now";isc.A.yearsFromNowTitle="N years from now";isc.A.defaultValue="$today";isc.A.operator="greaterThan";isc.A.presetOptions={"$today":"Today","$yesterday":"Yesterday","$tomorrow":"Tomorrow","$weekAgo":"Current day of last week","$weekFromNow":"Current day of next week","$monthAgo":"Current day of last month","$monthFromNow":"Current day of next month"};isc.A.valueFieldDefaults={type:"ComboBoxItem",name:"valueField",showTitle:false,shouldSaveValue:false,validateOnChange:false,getInnerWidth:function(_1){var _2=this.Super("getInnerWidth",arguments);var _3=this.form.canvasItem;if(_3==null||this.$200()||!_3.$251()||!isc.isA.Number(_2)){return _2}
return _2-_3.$250()}};isc.A.defaultQuantity=1;isc.A.quantityFieldDefaults={type:"SpinnerItem",name:"quantityField",width:60,min:0,step:1,showTitle:false,shouldSaveValue:false,selectOnFocus:true};isc.A.showChooserIcon=true;isc.A.pickerIconDefaults={name:"chooserIcon",showOver:false,showFocused:false,showFocusedWithItem:false,neverDisable:true,width:16,height:16,src:"[SKIN]/DynamicForm/DatePicker_icon.gif"};isc.A.iconVAlign="center";isc.A.pickerIconPrompt="Show Date Chooser";isc.A.pickerConstructor="DateChooser";isc.A.showCalculatedDateField=true;isc.A.calculatedDateFieldDefaults={type:"BlurbItem",name:"calculatedDateField",border:"1px solid black;",width:"*",setValue:function(_1){if(_1==null||_1=="")this.hide();else if(!this.isVisible())this.show();return this.Super("setValue",arguments)},startRow:false,showTitle:false,shouldSaveValue:false};isc.A.startDate=isc.DateItem.DEFAULT_START_DATE;isc.A.endDate=isc.DateItem.DEFAULT_END_DATE;isc.A.centuryThreshold=isc.DateItem.DEFAULT_CENTURY_THRESHOLD;isc.A.shouldSaveValue=true;isc.A.editCriteriaInInnerForm=false;isc.A.editorConstructor="DynamicForm";isc.A.editorDefaults={numCols:4,cellPadding:0,colWidths:[130,"*","*"],itemChanged:function(_1,_2){this.creator.updateValue()},itemKeyPress:function(_1,_2,_3){var _1=this.canvasItem;if(_1&&_1.form){return _1.$30k(_1,_1.form,_2,_3)}}};isc.A.useSharedPicker=false;isc.A.pickerDefaults={width:isc.DateItem.chooserWidth,height:isc.DateItem.chooserHeight,showCancelButton:true,autoHide:true};isc.A=isc.RelativeDateItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.validators=[{type:"isDate"}];isc.B.push(isc.A.init=function isc_RelativeDateItem_init(){this.$377();this.Super("init",arguments)}
,isc.A.isEditable=function isc_RelativeDateItem_isEditable(){return true}
,isc.A.$377=function isc_RelativeDateItem__createEditor(){var _1;var _2={$202:true};this.addAutoChild("editor",_2);this.canvas=this.editor;var _3=this,_4=[],_5=2;_4[0]=isc.addProperties({},this.valueFieldDefaults,this.valueFieldProperties,{valueMap:this.getValueFieldOptions()});_4[1]=isc.addProperties({},this.quantityFieldDefaults,this.quantityFieldProperties,{defaultValue:this.defaultQuantity});if(this.showChooserIcon){_5=3;_4[2]={name:"iconPlaceholder",type:"staticText",width:1,showTitle:false,iconVAlign:"center",icons:[isc.addProperties({prompt:this.pickerIconPrompt},this.pickerIconDefaults,this.pickerIconProperties,{click:function(){_3.showPicker()}})]}}
var _6=this.getType(),_7=false;if(isc.SimpleType.inheritsFrom(_6,"date")&&!isc.SimpleType.inheritsFrom(_6,"datetime"))
{_7=true}
this.baseDate=this.baseDate||(_7?isc.Date.createLogicalDate():new Date());if(this.showCalculatedDateField){_4[_5]=isc.addProperties({},this.calculatedDateFieldDefaults,this.calculatedDateFieldProperties,{cellStyle:this.getHintStyle()})}
this.canvas.setFields(_4);this.valueField=this.canvas.getField("valueField");this.quantityField=this.canvas.getField("quantityField");if(this.showCalculatedDateField)
this.calculatedDateField=this.canvas.getField("calculatedDateField");if(this.showChooserIcon){this.iconPlaceholder=this.canvas.getField("iconPlaceholder");this.pickerIcon=this.iconPlaceholder.icons.find("name","chooserIcon")}
this.setValue(this.value||this.defaultValue)}
,isc.A.updateEditor=function isc_RelativeDateItem_updateEditor(){if(!this.valueField||!this.quantityField)return;var _1,_2,_3=false;if(this.valueField.hasFocus){_1=this.valueField;_2=this.valueField.getSelectionRange()}else if(this.quantityField.hasFocus){_1=this.quantityField;_2=this.quantityField.getSelectionRange()}
var _4=this.valueField.getValue(),_5=this.quantityField.getValue();var _6=(_4&&isc.isA.String(_4)&&this.relativePresets[_4]);if(!_6){if(this.quantityField.isVisible()){_3=true;this.quantityField.hide()}}else{if(!this.quantityField.isVisible()){_3=true;this.quantityField.show()}}
if(this.calculatedDateField){var _4=this.getAbsoluteDate();this.calculatedDateField.setValue(!_4?"":"("+this.formatDate(_4)+")")}
if(_3&&_1!=null){if(!_6&&_1==this.quantityField){this.valueField.focusInItem()}else{if(_2){_1.delayCall("setSelectionRange",[_2[0],_2[1]])}}}}
,isc.A.getValueFieldOptions=function isc_RelativeDateItem_getValueFieldOptions(){var _1=isc.addProperties({},this.presetOptions);this.relativePresets={};for(var i=0;i<this.timeUnitOptions.length;i++){var _3=this.timeUnitOptions[i];if(this.showPastOptions){_1[_3+"_ago"]=this[_3+"sAgoTitle"];this.relativePresets[_3+"_ago"]=true}
if(this.showFutureOptions){_1[_3+"_fromNow"]=this[_3+"sFromNowTitle"];this.relativePresets[_3+"_fromNow"]=true}}
return _1}
,isc.A.setValue=function isc_RelativeDateItem_setValue(_1,_2){if(!this.valueField)return this.Super("setValue",arguments);if(_1==null&&!_2){var _3=this.getDefaultValue();if(_3!=null){_1=_3}}
if(isc.isA.Date(_1)&&_1.$38b!=null&&this.compareValues(this.$38c,_1.$38b))
{if(_1.getTime()==_1.$38d){_1=_1.$38b}}
var _4=false;if(_1==null){this.valueField.setValue(null)}else if(isc.isA.Date(_1)||this.valueField.valueMap[_1]||(_1.value&&this.valueField.valueMap[_1.value]))
{var _5=isc.isA.Date(_1);_4=!_5;this.valueField.setValue(_5?this.formatDate(_1):_1.value?_1.value:_1)}else if(this.timeUnitOptions.contains(_1)){_4=true;_1+="_fromNow";this.valueField.setValue(_1)}else{var _6,_4=isc.RelativeDateItem.isRelativeDate(_1);if(_4||isc.isA.String(_1)){_6=isc.RelativeDateItem.getAbsoluteDate(_1,this.baseDate,this.getType(),this.rangePosition)}
if(!isc.isA.Date(_6)){_4=false;this.valueField.setValue(null)}else{var _7=_4?_1.value:_1;_7=isc.DateUtil.mapRelativeDateShortcut(_7,this.rangePosition);var _8,_9,_10=isc.RelativeDateItem.getRelativeDateParts(_7),_11=isc.RelativeDateItem.getPeriodName(_10.period),_12=(_10.direction=="+"?"fromNow":"ago");_9=_10?_10.countValue:null;_8=_11?_11.toLowerCase()+"_"+_12:null;if(_8&&this.valueField.valueMap[_8]){this.valueField.setValue(_8);this.quantityField.setValue(_9);_4=true}else{_4=false;this.valueField.setValue(this.formatDate(_6))}}}
if(_4){this.$38c=this.getDataValue();_1=this.$38e(this.$38c)}else{this.$38c=null}
this.Super("setValue",[_1,_2],arguments);this.updateEditor()}
,isc.A.$38e=function isc_RelativeDateItem__convertToAbsoluteDate(_1){var _2=_1.value?_1.value:_1;var _3=isc.RelativeDateItem.getAbsoluteDate(_2,this.baseDate,this.getType(),this.rangePosition);if(isc.isA.Date(_3)){_3.$38b=_1;_3.$38d=_3.getTime()}
return _3}
,isc.A.getAbsoluteDate=function isc_RelativeDateItem_getAbsoluteDate(){return this.getDataValue(true)}
,isc.A.getRelativeDate=function isc_RelativeDateItem_getRelativeDate(){var _1=this.valueField.getValue(),_2=this.quantityField.getValue();if(!_1||!isc.isA.String(_1))return null;var _3=_1.substring(0,1);if(_3=="+"||_3=="-"||_3=="$"){return this.getRelativeDateObject(_1)}
var _4=_1.indexOf("_");if(_4>=0){var _5=_1.substring(0,_4),_6=(_1.substring(_4+1)=="ago"),_7=isc.RelativeDateItem.getPeriodKey(_5);if(_7){var _8=(_6?"-":"+")+_2+_7;var _9=this.rangePosition=="end"?"+":"-",_10=this.rangeRoundingGranularity[_5];if(_10!=null&&_10.toLowerCase()!="millisecond"){_10=isc.RelativeDateItem.getPeriodKey(_10).toUpperCase();_8+="["+_9+"0"+_10+"]"}
return this.getRelativeDateObject(_8)}}
return null}
,isc.A.getRelativeDateObject=function isc_RelativeDateItem_getRelativeDateObject(_1){var _2={_constructor:"RelativeDate",value:_1};if(this.rangePosition)_2.rangePosition=this.rangePosition;return _2}
,isc.A.getDataValue=function isc_RelativeDateItem_getDataValue(_1){var _2=this.valueField.getValue(),_3;if(_2==null||isc.isAn.emptyString(_2)){_3=null}else{var _4=this.getRelativeDate();if(_4){if(_1){_3=this.$38e(_4)}else{_3=_4}}else{_3=this.parseDate(_2,this.getInputFormat())}
if(isc.isA.Date(_3)){var _5=this.getType();if(_5==null||(!isc.SimpleType.inheritsFrom(_5,"datetime")&&!isc.SimpleType.inheritsFrom(_5,"time")))
{_3.logicalDate=true}}}
return _3}
,isc.A.updateValue=function isc_RelativeDateItem_updateValue(){if(!this.valueField||!this.quantityField)return;var _1=this._value,_2=this.$38c,_3=this.getDataValue(false),_4=(_3==null||isc.isA.Date(_3))?_3:this.$38e(_3);if(_3!=_4){this.$38c=_3}else{this.$38c=null}
if(this.compareValues(_1,_4)&&this.compareValues(_2,this.$38c))return;this.$20l(_4);this.updateEditor()}
,isc.A.hasAdvancedCriteria=function isc_RelativeDateItem_hasAdvancedCriteria(){return(this.valueField&&this.valueField.getValue()!=null)}
,isc.A.getCriterion=function isc_RelativeDateItem_getCriterion(_1){var _2=this.getDataValue(_1);if(_2==null)return null;var _3=this.getCriteriaFieldName();return{operator:this.operator,value:_2,fieldName:_3}}
,isc.A.getCellHeight=function isc_RelativeDateItem_getCellHeight(){var _1=this.Super("getCellHeight",arguments);if(isc.Browser.isIE&&this.useTextField&&isc.isA.Number(_1))_1+=2;return _1}
,isc.A.getPickerIcon=function isc_RelativeDateItem_getPickerIcon(_1,_2,_3,_4){var _5=this.invokeSuper(isc.DateItem,"getPickerIcon",_1,_2,_3,_4);if(_5.prompt==null)_5.prompt=this.pickerIconPrompt;return _5}
,isc.A.showPicker=function isc_RelativeDateItem_showPicker(){if(!this.picker){if(this.useSharedPicker)this.picker=isc.DateChooser.getSharedDateChooser();else{this.picker=isc[this.pickerConstructor].create(isc.addProperties({},this.pickerDefaults,this.pickerProperties,{_generated:true,autoHide:true,showCancelButton:true}))}}
var _1=this.picker;var _2=_1.callingFormItem;if(_2!=this){if(_2)_2.ignore(_1,"dataChanged");this.observe(_1,"dataChanged","observer.pickerDataChanged(observed)");_1.callingFormItem=this;_1.callingForm=this.canvas;_1.locatorParent=this.canvas}
_1.startYear=this.getStartDate().getFullYear();_1.endYear=this.getEndDate().getFullYear();return this.Super("showPicker",arguments)}
,isc.A.getPickerRect=function isc_RelativeDateItem_getPickerRect(){var _1=this.getPageLeft(),_2=this.getPageTop(),_3=isc.DateItem.chooserWidth+3,_4=isc.DateItem.chooserHeight+3,_5=this.canvas,_6;_6=_5.getItem("iconPlaceholder");_1+=_6.getLeft();_1+=Math.round((_6.getVisibleWidth()-(this.getPickerIconWidth()/2))-
(_3/ 2));_2+=Math.round((this.getPickerIconHeight()/2)-(_4/ 2));return[_1,_2]}
,isc.A.pickerDataChanged=function isc_RelativeDateItem_pickerDataChanged(_1){var _2=_1.getData();var _3=this.getType(),_4=isc.SimpleType.inheritsFrom(_3,"date"),_5=isc.SimpleType.inheritsFrom(_3,"datetime");if(!_4||_5){this.setToZeroTime(_2);if(this.rangePosition=="end")_2=isc.DateUtil.getEndOf(_2,"D")}
this.$34v=true;this.valueField.setValue(this.formatDate(_2));this.$34v=false;this.updateValue();if(!this.hasFocus)this.focusInItem()}
,isc.A.getStartDate=function isc_RelativeDateItem_getStartDate(){var _1=this.parseDate(this.startDate);if(!isc.isA.Date(_1)){this.logWarn("startDate was not in valid date format - using default start date");_1=isc.DateItem.DEFAULT_START_DATE}
return _1}
,isc.A.getEndDate=function isc_RelativeDateItem_getEndDate(){var _1=this.parseDate(this.endDate);if(!isc.isA.Date(_1)){this.logWarn("endDate was not in valid date format - using default end date");_1=isc.DateItem.DEFAULT_END_DATE}
return _1}
,isc.A.parseDate=function isc_RelativeDateItem_parseDate(_1,_2){if(_2==null)_2=this.getInputFormat();var _3=this.getType(),_4=isc.SimpleType.inheritsFrom(_3,"date")&&!isc.SimpleType.inheritsFrom(_3,"datetime");return Date.parseInput(_1,_2,this.centuryThreshold,true,!_4)}
,isc.A.formatDate=function isc_RelativeDateItem_formatDate(_1){if(!isc.isA.Date(_1))return _1;var _2=this.$28s();var _3=this.getType(),_4=false,_5=isc.SimpleType.inheritsFrom(_3,"date");if(!_5||isc.SimpleType.inheritsFrom(_3,"datetime"))_4=true;if(_4){return _1.toShortDatetime(_2,true)}else{return _1.toShortDate(_2,!_5)}}
,isc.A.getInputFormat=function isc_RelativeDateItem_getInputFormat(){if(this.inputFormat)return this.inputFormat;var _1=this.$28s();if(_1){return Date.mapDisplayFormatToInputFormat(_1)}
return null}
,isc.A.getEnteredValue=function isc_RelativeDateItem_getEnteredValue(){if(this.valueField)return this.valueField.getValue();return this.getValue()}
,isc.A.getValue=function isc_RelativeDateItem_getValue(){var _1=this.Super("getValue",arguments);return _1||(this.valueField&&this.valueField.getValue())}
);isc.B._maxIndex=isc.C+26}
if(isc.ListGrid){isc.defineClass("DateRangeItem","CanvasItem");isc.A=isc.DateRangeItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.shouldSaveValue=true;isc.A.fromTitle="From";isc.A.toTitle="To";isc.A.allowRelativeDates=false;isc.A.dateRangeFormDefaults={_constructor:"DynamicForm",margin:0,padding:0,itemChanged:function(_1,_2){var _3=this.getValues(),_4={_constructor:"DateRange"};if(_3.fromField!=null)_4.start=_3.fromField;if(_3.toField!=null)_4.end=_3.toField;this.creator.$20l(_4)}};isc.B.push(isc.A.setFromDate=function isc_DateRangeItem_setFromDate(_1){this.fromDate=_1;if(this.fromField)this.fromField.setValue(this.fromDate)}
,isc.A.setToDate=function isc_DateRangeItem_setToDate(_1){this.toDate=_1;if(this.toField)this.toField.setValue(this.toDate)}
,isc.A.hasAdvancedCriteria=function isc_DateRangeItem_hasAdvancedCriteria(){return this.fromField&&this.toField&&(this.fromField.getValue()!=null||this.toField.getValue()!=null)}
,isc.A.getCriterion=function isc_DateRangeItem_getCriterion(_1){_1=_1||!this.allowRelativeDates;var _2=_1?this.fromField.getValue():this.fromField.getRelativeDate()||this.fromField.getValue(),_3=_2!=null,_4=_1?this.toField.getValue():this.toField.getRelativeDate()||this.toField.getValue(),_5=_4!=null,_6=null;if(_3||_5){_6={_constructor:"AdvancedCriteria",operator:"and",criteria:[]};if(_3){if(isc.isA.RelativeDateItem(_2)){_2.rangePosition="start"}
_6.criteria.add({fieldName:this.getCriteriaFieldName(),operator:"greaterOrEqual",value:_2})}
if(_5){if(isc.isA.RelativeDateItem(_4)){_4.rangePosition="end"}
_6.criteria.add({fieldName:this.getCriteriaFieldName(),operator:"lessOrEqual",value:_4})}}
return _6}
,isc.A.canEditCriterion=function isc_DateRangeItem_canEditCriterion(_1){if(_1==null)return false;var _2=this.getCriteriaFieldName();if(_1.operator=="and"){var _3=_1.criteria;if(_3.length==0||_3.length>2){return false}else if(_3.length==1){var _4=_3[0];if(_4.fieldName!=_2)return false;if(_4.operator=="equals"){this.logWarn("DynamicForm editing Advanced criteria. Includes criterion for "+"field "+_2+". A dateRange editor is showing for this field and "+"the existing criteria has operator: "+_4.operator+". DateRange "+"items can only edit criteria greaterThan/greaterOrEqual or lessThan/lessOrEqual. "+"However, for the 'equals' operator, a dateRange will be constructed for you, "+"as greaterOrEqual to [value] and lessOrEqual to [value], ie, one day.");return true}}
for(var i=0;i<_3.length;i++){var _6=_3[i];if(_6.fieldName!=_2)return false;if(_6.operator!="greaterThan"&&_6.operator!="greaterOrEqual"&&_6.operator!="lessThan"&&_6.operator!="lessOrEqual")
{this.logWarn("DynamicForm editing Advanced criteria. Includes criterion for "+"field "+_2+". A dateRange editor is showing for this field but "+"the existing criteria has operator:"+_6.operator+". DateRange "+"items can only edit criteria greaterThan/greaterOrEqual or lessThan/lessOrEqual "+"so leaving this unaltered.");return false}}
return true}else if(_1.fieldName==_2){var _7="DynamicForm editing Advanced criteria. Includes criterion for "+"field "+_2+". A dateRange editor is showing for this field and "+"the existing criteria has operator:"+_1.operator+". DateRange "+"items can only edit criteria greaterThan/greaterOrEqual or lessThan/lessOrEqual";if(_1.operator=="equals"){this.logWarn(_7+". However, for the 'equals' operator, a dateRange will be "+"constructed for you, as greaterOrEqual to [value] and lessOrEqual to [value], "+"ie, one day.");return true}
if(_1.operator!="greaterThan"&&_1.operator!="greaterOrEqual"&&_1.operator!="lessThan"&&_1.operator!="lessOrEqual")
{this.logWarn(_7+" so leaving this unaltered.");return false}
return true}
return false}
,isc.A.setCriterion=function isc_DateRangeItem_setCriterion(_1){if(!_1)return;if(_1.operator=="equals"){var _2={_constructor:"AdvancedCriteria",operator:"and",criteria:[{fieldName:_1.fieldName,operator:"greaterOrEqual",value:_1.value},{fieldName:_1.fieldName,operator:"lessOrEqual",value:_1.value}]};_1=_2}
var _3,_4;if(_1.operator=="and"){_3=_1.criteria.find("operator","greaterThan");if(!_3)_3=_1.criteria.find("operator","greaterOrEqual");_4=_1.criteria.find("operator","lessThan");if(!_4)_4=_1.criteria.find("operator","lessOrEqual")}else{if(_1.operator=="greaterThan")_3=_1;else if(_1.operator=="greaterOrEqual")_3=_1;else if(_1.operator=="lessThan")_4=_1;else if(_1.operator=="lessOrEqual")_4=_1}
if(_3!=null){this.fromField.setValue(_3.value)}
if(_4!=null){this.toField.setValue(_4.value)}}
);isc.B._maxIndex=isc.C+6;isc.A=isc.DateRangeItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.init=function isc_DateRangeItem_init(){this.$377();this.Super("init",arguments)}
,isc.A.isEditable=function isc_DateRangeItem_isEditable(){return true}
,isc.A.$377=function isc_DateRangeItem__createEditor(){var _1;var _2={$202:true};if(this.form.dataSource){_1=isc.DataSource.getDataSource(this.form.dataSource);var _3=_1.getField(this.name);if(_3){_2.dataSource=_1.getFieldDataSource(_3)}}
if(this.form&&this.form.showComplexFieldsRecursively){_2.showComplexFields=true;_2.showComplexFieldsRecursively=true}else{_2.showComplexFields=false}
_2.height=22;var _4=this.innerTitleOrientation||this.titleOrientation||this.form.titleOrientation;_2.titleOrientation=_4;if(_4=="left"||_4=="right"){_2.numCols=2;_2.colWidths=[50,"*"]}else{_2.numCols=1;_2.colWidths=["*"]}
this.addAutoChild("dateRangeForm",_2);this.canvas=this.dateRangeForm;var _5=this.getType(),_6=false;if(isc.SimpleType.inheritsFrom(_5,"date")&&!isc.SimpleType.inheritsFrom(_5,"datetime"))
{_6=true}
this.baseDate=this.baseDate||(_6?isc.Date.createLogicalDate():new Date());var _7=this,_8=this.allowRelativeDates?"RelativeDateItem":"DateItem",_9=[];_9[0]=isc.addProperties({},this.fromFieldDefaults,this.fromFieldProperties,{name:"fromField",_constructor:_8,baseDate:this.baseDate,dateFormatter:(this.dateDisplayFormat||this.dateFormatter),type:this.getType(),inputFormat:(this.dateInputFormat||this.inputFormat),rangePosition:"start",title:this.fromTitle,defaultValue:this.fromValue,useTextField:(_8=="DateItem"?true:null)});_9[1]=isc.addProperties({},this.toFieldDefaults,this.toFieldProperties,{name:"toField",_constructor:_8,baseDate:this.baseDate,dateFormatter:(this.dateDisplayFormat||this.dateFormatter),type:this.getType(),inputFormat:(this.dateInputFormat||this.inputFormat),rangePosition:"end",title:this.toTitle,defaultValue:this.toValue,useTextField:(_8=="DateItem"?true:null)});this.canvas.setFields(_9);this.toField=this.canvas.getField("toField");this.fromField=this.canvas.getField("fromField");if(this.allowRelativeDates){this.fromField.canvas.$06=this.toField.canvas;this.toField.canvas.$0y=this.fromField.canvas}
if(this.defaultValue){this.setValue(this.defaultValue)}else{if(this.fromDate)this.setFromDate(this.fromDate);if(this.toDate)this.setToDate(this.toDate)}}
,isc.A.fieldChanged=function isc_DateRangeItem_fieldChanged(){}
,isc.A.setValue=function isc_DateRangeItem_setValue(_1){var _2=_1?_1.start:null,_3=_1?_1.end:null,_4=isc.RelativeDateItem;if(!this.allowRelativeDates&&_4.isRelativeDate(_2))this.setFromDate(null);else this.setFromDate(_2);if(!this.allowRelativeDates&&_4.isRelativeDate(_3))this.setToDate(null);else this.setToDate(_3);this.Super("setValue",arguments)}
,isc.A.getValue=function isc_DateRangeItem_getValue(){if(!this.fromField||!this.toField)return;var _1=this.allowRelativeDates,_2=_1&&this.fromField.getRelativeDate()?this.fromField.getRelativeDate():this.fromField.getValue(),_3=_1&&this.toField.getRelativeDate()?this.toField.getRelativeDate():this.toField.getValue(),_4={_constructor:"DateRange"};if(_2==null&&_3==null)return null;if(_2!=null)_4.start=_2;if(_3!=null)_4.end=_3;return _4}
,isc.A.destroy=function isc_DateRangeItem_destroy(){if(this.dateRangeForm)this.dateRangeForm.destroy();this.Super("destroy",arguments)}
);isc.B._maxIndex=isc.C+7;if(isc.Window){isc.defineClass("DateRangeDialog","Window");isc.A=isc.DateRangeDialog;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.askForRange=function isc_c_DateRangeDialog_askForRange(_1,_2,_3,_4){var _5=isc.DateRangeDialog.create({allowRelativeDates:_1!=null?_1:true,rangeItemProperties:_2,callback:_4},_3);_5.show()}
);isc.B._maxIndex=isc.C+1;isc.A=isc.DateRangeDialog.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.isModal=true;isc.A.showModalMask=true;isc.A.dismissOnEscape=true;isc.A.autoCenter=true;isc.A.width=380;isc.A.height=140;isc.A.vertical="true";isc.A.showMinimizeButton=false;isc.A.headerIconProperties={src:"[SKIN]/DynamicForm/DatePicker_icon.gif"};isc.A.returnCriterion=false;isc.A.headerTitle="Select Date Range";isc.A.mainLayoutDefaults={_constructor:"VLayout",width:"100%",height:"100%",layoutMargin:5};isc.A.rangeFormDefaults={_constructor:"DynamicForm",numCols:1,width:"100%",height:"100%",autoParent:"mainLayout"};isc.A.rangeItemDefaults={_constructor:"DateRangeItem",allowRelativeDates:true,showTitle:false};isc.A.buttonLayoutDefaults={_constructor:"HLayout",width:"100%",height:22,align:"right",membersMargin:5,autoParent:"mainLayout"};isc.A.clearButtonTitle="Clear";isc.A.clearButtonDefaults={_constructor:"IButton",height:22,width:80,canFocus:true,autoParent:"buttonLayout",click:function(){this.creator.clearValues()}};isc.A.okButtonTitle="OK";isc.A.okButtonDefaults={_constructor:"IButton",height:22,width:80,canFocus:true,autoParent:"buttonLayout",click:function(){this.creator.accept()}};isc.A.cancelButtonTitle="Cancel";isc.A.cancelButtonDefaults={_constructor:"IButton",height:22,width:80,canFocus:true,autoParent:"buttonLayout",click:function(){this.creator.cancel()}};isc.A.destroyOnClose=true;isc.B.push(isc.A.destroy=function isc_DateRangeDialog_destroy(){if(this.rangeForm){this.rangeForm.markForDestroy()}
this.Super("destroy",arguments)}
);isc.B._maxIndex=isc.C+1;isc.A=isc.DateRangeDialog.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.initWidget=function isc_DateRangeDialog_initWidget(){this.title=this.headerTitle;this.Super("initWidget",arguments);this.addAutoChild("mainLayout");this.addAutoChild("rangeForm",{$202:true,items:[isc.addProperties({},this.rangeItemDefaults,this.rangeItemProperties,{name:"rangeItem",fromDate:this.fromDate,toDate:this.toDate,dateDisplayFormat:this.dateDisplayFormat})]});this.rangeItem=this.rangeForm.getField("rangeItem");this.rangeItem.canvas.numCols=1;this.addAutoChild("buttonLayout");this.addAutoChild("clearButton",{canFocus:true,title:this.clearButtonTitle});this.addAutoChild("okButton",{canFocus:true,title:this.okButtonTitle});this.addAutoChild("cancelButton",{canFocus:true,title:this.cancelButtonTitle});this.addItem(this.mainLayout)}
,isc.A.clearValues=function isc_DateRangeDialog_clearValues(){if(this.rangeItem)this.rangeItem.setValue(null)}
,isc.A.accept=function isc_DateRangeDialog_accept(){this.finished(this.rangeItem.returnCriterion?this.rangeItem.getCriterion():this.rangeItem.getValue())}
,isc.A.cancel=function isc_DateRangeDialog_cancel(){this.hide();if(this.destroyOnClose)this.markForDestroy()}
,isc.A.finished=function isc_DateRangeDialog_finished(_1){if(this.callback)this.fireCallback(this.callback,"value",[_1]);this.hide();if(this.destroyOnClose)this.markForDestroy()}
);isc.B._maxIndex=isc.C+5;isc.defineClass("MiniDateRangeItem","StaticTextItem");isc.A=isc.MiniDateRangeItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.textBoxStyle="textItem";isc.A.clipValue=true;isc.A.wrap=false;isc.A.iconVAlign="top";isc.A.height=20;isc.A.width=100;isc.A.shouldSaveValue=true;isc.A.rangeDialogDefaults={_constructor:"DateRangeDialog",autoDraw:false,destroyOnClose:false};isc.A.canFocus=true;isc.A.canTabToIcons=true;isc.A.fromDateOnlyPrefix="Since";isc.A.toDateOnlyPrefix="Before";isc.A.pickerIconPrompt="Show Date Chooser";isc.A.pickerIconDefaults={name:"showDateRange",src:"[SKIN]/DynamicForm/DatePicker_icon.gif",width:16,height:16,showOver:false,showFocused:false,showFocusedWithItem:false,hspace:0,click:function(_1,_2,_3){if(!_2.disabled)_2.showRangeDialog()}};isc.A.iconVAlign="center";isc.A.allowRelativeDates=true;isc.B.push(isc.A.getFocusElement=function isc_MiniDateRangeItem_getFocusElement(){return this.$27m(this.icons[0])}
,isc.A.$268=function isc_MiniDateRangeItem__canFocusInTextBox(){return false}
,isc.A.handleClick=function isc_MiniDateRangeItem_handleClick(){if(!this.disabled)this.showRangeDialog()}
);isc.B._maxIndex=isc.C+3;isc.A=isc.MiniDateRangeItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.init=function isc_MiniDateRangeItem_init(){this.addAutoChild("rangeDialog",{fromDate:this.fromDate,toDate:this.toDate,rangeItemProperties:{allowRelativeDates:this.allowRelativeDates,type:this.getType()},dateDisplayFormat:this.dateDisplayFormat,callback:this.getID()+".rangeDialogCallback(value)"});this.icons=[isc.addProperties({prompt:this.pickerIconPrompt},this.pickerIconDefaults,this.pickerIconProperties)];this.canTabToIcons=true;this.rangeItem=this.rangeDialog.rangeItem;this.rangeItem.name=this.name}
,isc.A.showRangeDialog=function isc_MiniDateRangeItem_showRangeDialog(){this.rangeDialog.rangeItem.setFromDate(this.fromDate);this.rangeDialog.rangeItem.setToDate(this.toDate);this.rangeDialog.show()}
,isc.A.rangeDialogCallback=function isc_MiniDateRangeItem_rangeDialogCallback(_1){if(!this.$20l(_1))return;this.displayValue(_1)}
,isc.A.hasAdvancedCriteria=function isc_MiniDateRangeItem_hasAdvancedCriteria(){return this.rangeItem!=null&&this.rangeItem.hasAdvancedCriteria()}
,isc.A.getCriterion=function isc_MiniDateRangeItem_getCriterion(){var _1=this.rangeItem?this.rangeItem.getCriterion():null;return _1}
,isc.A.setCriterion=function isc_MiniDateRangeItem_setCriterion(_1){if(this.rangeItem){this.rangeItem.setCriterion(_1);var _2=this.rangeItem.getValue();this.setValue(_2,null,true)}}
,isc.A.canEditCriterion=function isc_MiniDateRangeItem_canEditCriterion(_1){return this.rangeItem?this.rangeItem.canEditCriterion(_1):false}
,isc.A.setValue=function isc_MiniDateRangeItem_setValue(_1,_2,_3){this.updateStoredDates(_1);if(!_3){this.rangeItem.setFromDate(this.fromDate);this.rangeItem.setToDate(this.toDate)}
var _4=[this.getValue()];this.Super("setValue",_4,arguments)}
,isc.A.updateStoredDates=function isc_MiniDateRangeItem_updateStoredDates(_1){if(_1!=null){if(isc.DataSource.isAdvancedCriteria(_1)){var _2={};for(var i=0;i<_1.criteria.length;i++){var _4=_1.criteria[i];if(_4.operator=="greaterThan"||_4.operator=="greaterOrEqual")
_2.start=_4.value;else if(_4.operator=="lessThan"||_4.operator=="lessOrEqual")
_2.end=_4.value}
_1=_2}
this.fromDate=_1.start;this.toDate=_1.end}else{this.fromDate=null;this.toDate=null}}
,isc.A.saveValue=function isc_MiniDateRangeItem_saveValue(){this.Super("saveValue",arguments);this.updateStoredDates(this._value)}
,isc.A.displayValue=function isc_MiniDateRangeItem_displayValue(_1){var _2=this.mapValueToDisplay(_1)||"";this.setElementValue(_2,_1)}
,isc.A.mapValueToDisplay=function isc_MiniDateRangeItem_mapValueToDisplay(_1){if(_1==null)return"";var _2=_1.start,_3=_1.end,_4=isc.RelativeDateItem,_5=(_4.isRelativeDate(_2)?_4.getAbsoluteDate(_2.value,null,null,"start"):_2),_6=(_4.isRelativeDate(_3)?_4.getAbsoluteDate(_3.value,null,null,"end"):_3);var _7;if(_5||_6){if(this.dateDisplayFormat){if(_5)_7=this.formatDate(_5);if(_6){if(_7)_7+=" - "+this.formatDate(_6);else _7=this.formatDate(_6)}}else _7=Date.getFormattedDateRangeString(_5,_6);if(!_5)_7=this.toDateOnlyPrefix+" "+_7;else if(!_6)_7=this.fromDateOnlyPrefix+" "+_7}
this.prompt=_7||"";return this.prompt}
,isc.A.getValue=function isc_MiniDateRangeItem_getValue(){if(!this.rangeItem)return;return this.rangeItem.getValue()}
,isc.A.formatDate=function isc_MiniDateRangeItem_formatDate(_1){if(!isc.isA.Date(_1))return _1;var _2=this.getType(),_3=isc.SimpleType.inheritsFrom(_2,"date")&&!isc.SimpleType.inheritsFrom(_2,"datetime");return _1.toShortDate(this.$28s(),!_3)}
,isc.A.getCriteriaValue=function isc_MiniDateRangeItem_getCriteriaValue(){return this.getCriterion()}
,isc.A.destroy=function isc_MiniDateRangeItem_destroy(){if(this.rangeDialog)this.rangeDialog.markForDestroy();this.Super("destroy",arguments)}
);isc.B._maxIndex=isc.C+16}}
if(isc.Portal){isc.defineClass("EntityEditorHeader","VLayout");isc.A=isc.EntityEditorHeader.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.height=1;isc.A.padding=10;isc.A.border="2px solid black";isc.A.headerLayoutDefaults={_constructor:"VLayout",width:"100%",height:1,membersMargin:5};isc.A.headerLabelTitle="<B><H2>Editing ${entityType}</H2><br>"+"<H3>This UI lets you edit the entire data-structure for this Entity-type</H3></B>";isc.A.headerLabelDefaults={_constructor:"Label",width:"100%",height:30,autoParent:"headerLayout"};isc.A.showDetailLabel=false;isc.A.defaultDetailLabelTitle="<B><H3>This UI lets you edit the entire data-structure for this Entity-type</H3></B>";isc.A.detailLabelTitle="<B><H3>$entityComment</H3></B>";isc.A.detailLabelDefaults={_constructor:"Label",width:"100%",height:20,autoParent:"headerLayout"};isc.A.unknownEntityTitle="[Unknown Entity-type]";isc.B.push(isc.A.initWidget=function isc_EntityEditorHeader_initWidget(){var _1=this.headerLabelTitle;var _2=this.detailLabelTitle;if(this.dataSource)this.getDataSource(this.dataSource);if(!this.entityName)this.entityName=this.getEntityName(this.dataSource);if(!this.entityComment)this.entityComment=this.getEntityComment(this.dataSource);if(this.entityName)
_1=_1.evalDynamicString(this,{entityType:this.entityName});if(this.entityComment)
_2=_2.evalDynamicString(this,{entityType:this.entityComment});this.headerLayout=this.addAutoChild("headerLayout");this.headerLabel=this.addAutoChild("headerLabel",{contents:_1});this.detailLabel=this.addAutoChild("detailLabel",{contents:_2});this.headerLayout.addMembers([this.headerLabel,this.detailLabel]);this.addMember(this.headerLayout)}
,isc.A.getEntityName=function isc_EntityEditorHeader_getEntityName(_1){var _2=this.unknownEntityTitle;if(_1){if(isc.isA.Function(_1.getEntityName))
_2=_1.getEntityName();else _2=_1.ID}
return _2}
,isc.A.getEntityComment=function isc_EntityEditorHeader_getEntityComment(_1){var _2=this.defaultDetailLabelTitle;if(_1){if(isc.isA.Function(_1.getEntityComment))
_2=_1.getEntityComment();else _2="Allows hierarchical editing of data in "+_1.ID+" DataSource"}
return _2}
);isc.B._maxIndex=isc.C+3;isc.defineClass("EntityEditorForm","Portlet");isc.A=isc.EntityEditorForm.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.addButtonDefaults={_constructor:"IButton",title:"Add",autoFit:true,layoutAlign:"right",click:function(){this.creator.addRecord()}};isc.A.formDefaults={_constructor:"DynamicForm",numCols:6,colWidths:["*","*","*","*","*","*"],width:"100%",implicitSave:true,initWidget:function(){this.Super("initWidget",arguments);if(this.record&&this.relation){var _1={};if(!this.relation.direction){_1[this.relation.baseFieldName]=this.record[this.relation.baseFieldName]}else{_1[this.relation.baseFieldName]=this.record[this.relation.relatedFieldName]}
this.fetchData(_1)}},implicitSaveCallback:function(_1){if(!this.record||!this.record[this.relation.baseFieldName]){this.creator.setRecord(_1);this.setValues(_1);this.saveOperationType="update"}}};isc.B.push(isc.A.initWidget=function isc_EntityEditorForm_initWidget(){this.Super("initWidget",arguments);this.addAutoChild("addButton");this.addAutoChild("form",isc.addProperties({},this.formProperties,{dataSource:this.dataSource,title:this.title,record:this.record,relation:this.relation}));if(isc.isA.Portlet(this))this.addItems([this.addButton,this.form]);else this.addMembers([this.addButton,this.form])}
,isc.A.fetchData=function isc_EntityEditorForm_fetchData(){var _1={};if(!this.relation.direction){if(this.isTopLevel())
_1[this.relation.baseFieldName]=this.record[this.relation.baseFieldName]}else{_1[this.relation.baseFieldName]=this.record[this.relation.relatedFieldName]}
this.form.fetchData(_1)}
,isc.A.addRecord=function isc_EntityEditorForm_addRecord(){var _1={};if(!this.relation.direction){if(!this.isTopLevel())
_1[this.relation.baseFieldName]=this.record[this.relation.baseFieldName]}else{_1[this.relation.baseFieldName]=this.record[this.relation.relatedFieldName]}
this.record=_1;this.form.editNewRecord(_1)}
,isc.A.isTopLevel=function isc_EntityEditorForm_isTopLevel(){return this.relation.relatedDS==null}
,isc.A.setRecord=function isc_EntityEditorForm_setRecord(_1){if(this.isTopLevel()){this.creator.updateTopLevel()}else{this.fetchData()}
this.record=_1;this.form.record=_1;if(this.addButton)this.addButton.setDisabled(this.record==null)}
,isc.A.getData=function isc_EntityEditorForm_getData(){return null}
,isc.A.getCriteria=function isc_EntityEditorForm_getCriteria(){return this.form.getValuesAsCriteria()}
,isc.A.enterSearchMode=function isc_EntityEditorForm_enterSearchMode(_1){this.addButton.setDisabled(true);this.record=this.form.record=null;this.form.implicitSave=false;this.form.setData([]);if(_1)this.form.setValues(_1)}
,isc.A.exitSearchMode=function isc_EntityEditorForm_exitSearchMode(){}
);isc.B._maxIndex=isc.C+9;isc.defineClass("EntityEditorGrid","Portlet");isc.A=isc.EntityEditorGrid.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.addButtonDefaults={_constructor:"IButton",title:"Add",autoFit:true,layoutAlign:"right",click:function(){this.creator.addRecord()}};isc.A.gridDefaults={_constructor:"ListGrid",width:"100%",height:"100%",autoFitMaxRecords:4,autoFetchData:false,canEdit:true,autoSaveEdits:true,initWidget:function(){if(this.record&&this.relation){this.initialCriteria={};this.initialCriteria[this.relation.baseFieldName]=this.record[this.relation.relatedFieldName]}
this.Super("initWidget",arguments)}};isc.B.push(isc.A.initWidget=function isc_EntityEditorGrid_initWidget(){this.addAutoChild("addButton");if(!isc.isA.DataSource(this.dataSource))
this.dataSource=isc.DS.get(this.dataSource);this.addAutoChild("grid",isc.addProperties({},this.gridProperties,{dataSource:this.dataSource,title:this.title,record:this.record,relation:this.relation}));if(this.record)this.fetchData();if(this.addButton)this.addButton.setDisabled(this.record==null);if(isc.isA.Portlet(this))this.addItems([this.addButton,this.grid]);else this.addMembers([this.addButton,this.grid]);this.Super("initWidget",arguments)}
,isc.A.setData=function isc_EntityEditorGrid_setData(_1){}
,isc.A.fetchData=function isc_EntityEditorGrid_fetchData(){var _1={};if(this.record){_1[this.relation.baseFieldName]=this.record[this.relation.relatedFieldName]}
this.grid.fetchData(_1)}
,isc.A.addRecord=function isc_EntityEditorGrid_addRecord(){var _1={};_1[this.relation.baseFieldName]=this.record[this.relation.relatedFieldName];this.grid.startEditingNew(_1)}
,isc.A.setRecord=function isc_EntityEditorGrid_setRecord(_1){this.record=_1;this.fetchData();if(this.addButton)this.addButton.setDisabled(this.record==null)}
,isc.A.getData=function isc_EntityEditorGrid_getData(){return null}
,isc.A.getCriteria=function isc_EntityEditorGrid_getCriteria(){return this.grid.getFilterEditorCriteria()}
,isc.A.enterSearchMode=function isc_EntityEditorGrid_enterSearchMode(_1){this.addButton.setDisabled(true);this.record=this.grid.record=null;this.grid.setData([]);this.grid.setShowFilterEditor(true);if(_1)this.grid.setCriteria(_1)}
,isc.A.exitSearchMode=function isc_EntityEditorGrid_exitSearchMode(){}
);isc.B._maxIndex=isc.C+9;isc.defineClass("EntityEditor","VLayout");isc.A=isc.EntityEditor.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.membersMargin=10;isc.A.padding=10;isc.A.dataSource="";isc.A.modeFormDefaults={_constructor:"DynamicForm",height:1,width:"100%",fields:[{name:"edit",title:"options",defaultToFirstOption:true,valueMap:["Edit","Search"],changed:function(_1,_2,_3){_1.creator.setMode(_3)}}]};isc.A.formEntityDefaults={_constructor:"EntityEditorForm",height:1,width:"100%"};isc.A.gridEntityDefaults={_constructor:"EntityEditorGrid",height:1,width:"100%"};isc.A.showTabset=false;isc.A.tabsetDefaults={_constructor:"TabSet",width:"100%",height:"100%"};isc.A.portalDefaults={_constructor:"PortalLayout",width:"100%",height:"100%",showColumnMenus:false,numColumns:1};isc.B.push(isc.A.getDataSourceHierarchy=function isc_EntityEditor_getDataSourceHierarchy(_1,_2){var _3=[];if(!isc.isA.DataSource(_1))
_1=isc.DS.getDataSource(_1);if(!isc.isA.DataSource(_1)){this.logWarn("No datasource provided..");return _3}
if(!_2)_2=[];if(_2.contains(_1.ID)){return null}
var _4=isc.getValues(_1.getFields()),_5=[];for(var i=0;i<_4.length;i++){if(_4[i].foreignKey!=null){_5.add(_4[i])}}
var _7=_1.ID,_8=[],_9=[];isc.DS.registerDataSource(_1);var _10=_1.getChildDataSources();if(_10){for(var i=0;i<_10.length;i++){var _11=_10[i],_12=isc.getValues(_11.getFields());for(var j=0;j<_12.length;j++){var _14=_12[j];if(!_14.foreignKey||isc.DS.getForeignDSName(_14,_11)!=_1.ID)continue;var _15=isc.DS.getForeignFieldName(_14,_11);var _16=_4.find("name",_15);if(_16&&_14.entityEditMode!="picker"){var _17=_7,_18=_16.name,_19=_11.ID,_20=_14.name,_21=_16.relationArity,_22="out";if(!_2.contains(_19)){_3.add({baseDS:_19,baseFieldName:_20,relatedDS:_17,relatedFieldName:_18,relationArity:_16.relationArity})}}}}}
for(var j=0;j<_5.length;j++){var _16=_5[j];if(!_16.foreignKey)continue;var _23=isc.DS.getForeignFieldName(_16,_1),_11=isc.DS.getDataSource(isc.DS.getForeignDSName(_16,_1)),_12=isc.getValues(_11.getFields());var _14=_12.find("name",_23);if(_14&&_16.entityEditMode!="picker"){var _17=_7,_18=_16.name,_19=this.getDSName(_11),_20=_14.name,_21=_16.relationArity,_22="out";if(!_2.contains(_19)){_3.add({baseDS:_19,baseFieldName:_20,relatedDS:_17,relatedFieldName:_18,relationArity:_21,direction:_22})}}}
return _3}
,isc.A.getDSName=function isc_EntityEditor_getDSName(_1){if(isc.isA.String(_1))return _1;if(isc.isA.DataSource(_1))return _1.ID;return null}
);isc.B._maxIndex=isc.C+2;isc.A=isc.EntityEditor.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.initWidget=function isc_EntityEditor_initWidget(){this.vertical=true;if(!isc.isA.DataSource(this.dataSource))
this.dataSource=isc.DS.getDataSource(this.dataSource);if(!this.dataSource)this.logWarn("No dataSource provided - no entity to edit");else this.entityTree=this.getEntityTree();this.addAutoChild("modeForm");this.addAutoChild("tabset");this.addAutoChild("portal");this.addMember(this.modeForm);if(this.tabset)this.addMember(this.tabset);if(this.portal)this.addMember(this.portal)}
,isc.A.getEntityTree=function isc_EntityEditor_getEntityTree(){if(!isc.isA.DataSource(this.dataSource))
return{baseDS:"NoDSProvided",relations:[]};var _1={baseDS:this.dataSource.ID,baseFieldName:this.dataSource.getPrimaryKeyFieldNames()[0],relations:[]};_1.relations=this.getDataSourceHierarchy(this.dataSource);return _1}
,isc.A.fetchDataByPK=function isc_EntityEditor_fetchDataByPK(_1){if(!this.dataSource)return;var _2=this;this.dataSource.fetchData(_1,function(_3,_4){_2.fetchDataReply(_4)})}
,isc.A.fetchDataReply=function isc_EntityEditor_fetchDataReply(_1){this.clearEntity();this.record=_1[0];this.showEntity()}
,isc.A.clearEntity=function isc_EntityEditor_clearEntity(){if(this.entities&&this.entities.length>0){if(this.showTabset){for(var i=this.tabset.tabs.length-1;i>=0;i--){this.entities[i].markForDestroy();this.tabset.removeTab(i)}}else if(this.portal){this.portal.members.removeAll();for(var i=this.entities.length-1;i>=0;i--){this.entities[i].markForDestroy()}}else{for(var i=this.members.length-1;i>=1;i--){this.removeMember(i);this.entities[i-1].markForDestroy()}}}
this.entities=[];this.record=null}
,isc.A.showEntity=function isc_EntityEditor_showEntity(){var _1=this.entityTree;if(!this.entities)this.entities=[];if(!this.entityTree)return;this.addEditor(_1);this.topLevelComponent=this.entities[0];if(_1&&_1.relations&&_1.relations.length>0){for(var i=0;i<_1.relations.length;i++){var _3=_1.relations[i];if(this.shouldShowEntity(_3.baseDS)){if(_3.relationArity=="one"){this.addEditor(_3)}else{this.addGrid(_3)}}}}}
,isc.A.updateTopLevel=function isc_EntityEditor_updateTopLevel(){for(var i=1;i<this.entities.length;i++){this.entities[i].setRecord(this.topLevelComponent.record)}}
,isc.A.getData=function isc_EntityEditor_getData(){return[]}
,isc.A.getDataSourceSpec=function isc_EntityEditor_getDataSourceSpec(_1,_2){if(this.dataSources){var _3=this.dataSources[_1];if(!_3)_3=this.dataSources[_1+"!"+_2];return _3}
return null}
,isc.A.getRelatedEditorProperties=function isc_EntityEditor_getRelatedEditorProperties(_1,_2){var _3=this.getDataSourceSpec(_1,_2);if(_3)return _3.editorProperties;return null}
,isc.A.shouldShowEntity=function isc_EntityEditor_shouldShowEntity(_1){if(this.dataSources){var _2=this.dataSources[_1],_3;if(_2===_3)return true;return(_2!==null)}
return true}
,isc.A.addEditor=function isc_EntityEditor_addEditor(_1){var _2=this.createAutoChild("formEntity",{height:"100%",width:"100%",dataSource:_1.baseDS,title:this.getEntityTitle(_1),record:this.record,relation:_1,formProperties:this.getRelatedEditorProperties(_1.baseDS,_1.relatedFieldName)});this.addEntityLink(_2);this.logWarn("adding linked single-record entity")}
,isc.A.addGrid=function isc_EntityEditor_addGrid(_1){var _2=this.createAutoChild("gridEntity",{height:"100%",width:"100%",dataSource:_1.baseDS,title:this.getEntityTitle(_1),record:this.record,relation:_1,gridProperties:this.getRelatedEditorProperties(_1.baseDS,_1.relatedFieldName)});this.addEntityLink(_2);this.logWarn("added linked multiple-record entity")}
,isc.A.addEntityLink=function isc_EntityEditor_addEntityLink(_1){this.entities.add(_1);if(this.showTabset){this.addEntityTab(_1)}else{var _2=_1.relation,_3=this.getDataSourceSpec(_2.baseDS,_2.relatedFieldName),_4=(_3&&_3.rowNum!=null?_3.rowNum:-1),_5=(_3&&_3.offsetInRow!=null?_3.offsetInRow:-1);if(this.portal){if(_3&&_3.userHeight!=null)_1.$tt=_3.userHeight;if(_4!=-1){this.portal.getColumn(0).addPortletToExistingRow(_1,_4,_5)}else{this.portal.getColumn(0).addPortlet(_1)}}
else this.addMember(_1)}}
,isc.A.getEntityName=function isc_EntityEditor_getEntityName(_1){var _2=this.getDSName(_1.baseDS),_3=this.getDSName(_1.relatedDS),_4=_1.relatedFieldName||_1.baseFieldName;return _2+"_"+_4}
,isc.A.getEntityTitle=function isc_EntityEditor_getEntityTitle(_1){var _2=this.getDataSourceSpec(_1.baseDS,_1.baseFieldName),_3=this.getDSName(_1.baseDS),_4=this.getDSName(_1.relatedDS),_5=_1.relatedFieldName||_1.baseFieldName,_6;if(_2&&_2.entityTitle){_6=_2.entityTitle}else if(!_4){_6=isc.DS.getAutoTitle(_3)}else if(_3!=_4){_6=isc.DS.getAutoTitle(_3)}else{_6=isc.DS.getAutoTitle(_5)}
return _6}
,isc.A.addEntityTab=function isc_EntityEditor_addEntityTab(_1){var _2=this.getEntityName(_1.relation),_3=this.getEntityTitle(_1.relation);if(_1){_1.setWidth("100%");_1.setHeight("100%")}
this.tabset.addTab({name:_2,title:_3,pane:_1,relation:_1.relation})}
,isc.A.setMode=function isc_EntityEditor_setMode(_1){var i,_3;if(_1=="Search"){if(this.entities&&this.entities.length>0){for(i=0;i<this.entities.length;i++){_3=this.entities[i];if(isc.isA.EntityEditorGrid(_3)){_3.enterSearchMode(this.getEntityCriteria(_3));_3.markForRedraw()}else if(isc.isA.EntityEditorForm(_3)){_3.enterSearchMode(this.getEntityCriteria(_3));_3.markForRedraw()}}}}else{if(!this.entityCriteria)this.entityCriteria={};if(this.entities&&this.entities.length>0){for(i=0;i<this.entities.length;i++){_3=this.entities[i];var _4=_3.getCriteria();if(_4){this.entityCriteria[_3.getID()]={relation:_3.relation,criteria:_4}}}}}}
,isc.A.getEntityCriteria=function isc_EntityEditor_getEntityCriteria(_1){if(this.entityCriteria){return this.entityCriteria[_1.getID()]}
return null}
);isc.B._maxIndex=isc.C+19}
if(isc.ReportBuilder){isc.defineClass("ReportChooserItem","ComboBoxItem");isc.A=isc.ReportChooserItem.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.title="Report";isc.A.optionDataSource=isc.ReportBuilder.defaultDataSource;isc.A.valueField=isc.ReportBuilder.reportIdField;isc.A.displayField=isc.ReportBuilder.reportNameField;isc.A.categoryField=isc.ReportBuilder.reportCategoryField;isc.A.pickListWidth=250;isc.A.pickListProperties={showHeader:false,overFlow:"visible",groupStartOpen:"all",retainOpenStateOnRegroup:false,dataFetchMode:"local",dataProperties:{dataArrived:function(_1,_2){var _3=this.localData,_4={reportId:-299,reportName:"Configure...",reportCategory:"Configuration"};_3.add(_4);var _5=window[this.componentId];if(_5&&_5.regroup){_5.regroup()}}}};isc.B.push(isc.A.init=function isc_ReportChooserItem_init(){this.pickListFields=[{name:this.valueField,showIf:"false"},{name:this.categoryField,showIf:"false"},{name:this.displayField,title:"Report Name",width:"100%"}];this.pickListProperties.autoFitFieldWidths=false;this.pickListProperties.groupByField=this.categoryField}
,isc.A.change=function isc_ReportChooserItem_change(_1,_2,_3,_4){if(this.grid&&_3==-299){isc.ReportBuilder.showReportBuilder(this.grid,this.getID()+".reportBuilderReply()");if(this.pickList)this.pickList.invalidateCache();return false}else return true}
,isc.A.changed=function isc_ReportChooserItem_changed(_1,_2,_3){var _4=this.getSelectedRecord();if(this.grid&&_4&&_4.reportId!=-299){isc.ReportBuilder.setLinkedGridState(_4,this.grid)}}
,isc.A.reportBuilderReply=function isc_ReportChooserItem_reportBuilderReply(){this.pickList.invalidateCache();this.setValue(null)}
);isc.B._maxIndex=isc.C+4}
isc._moduleEnd=isc._Forms_end=(isc.timestamp?isc.timestamp():new Date().getTime());if(isc.Log&&isc.Log.logIsInfoEnabled('loadTime'))isc.Log.logInfo('Forms module init time: ' + (isc._moduleEnd-isc._moduleStart) + 'ms','loadTime');delete isc.definingFramework;}else{if(window.isc && isc.Log && isc.Log.logWarn)isc.Log.logWarn("Duplicate load of module 'Forms'.");}
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

