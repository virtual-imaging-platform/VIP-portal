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

if(window.isc&&window.isc.module_Core&&!window.isc.module_Foundation){isc.module_Foundation=1;isc._moduleStart=isc._Foundation_start=(isc.timestamp?isc.timestamp():new Date().getTime());if(isc._moduleEnd&&(!isc.Log||(isc.Log && isc.Log.logIsDebugEnabled('loadTime')))){isc._pTM={ message:'Foundation load/parse time: ' + (isc._moduleStart-isc._moduleEnd) + 'ms', category:'loadTime'};
if(isc.Log && isc.Log.logDebug)isc.Log.logDebug(isc._pTM.message,'loadTime')
else if(isc._preLog)isc._preLog[isc._preLog.length]=isc._pTM
else isc._preLog=[isc._pTM]}isc.definingFramework=true;isc.ClassFactory.defineClass("Animation");isc.A=isc.Animation;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.interval=40;isc.A.registry=[];isc.A.animateTime=1000;isc.B.push(isc.A.smoothStart=function isc_c_Animation_smoothStart(_1){return Math.pow(_1,2)}
,isc.A.smoothEnd=function isc_c_Animation_smoothEnd(_1){return 1-Math.abs(Math.pow(_1-1,2))}
,isc.A.smoothStartEnd=function isc_c_Animation_smoothStartEnd(_1){return(-Math.cos(_1*Math.PI)+1)/2.0}
);isc.B._maxIndex=isc.C+3;isc.A=isc.Animation;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$6v="ratio,ID,earlyFinish";isc.B.push(isc.A.generateAnimationID=function isc_c_Animation_generateAnimationID(){if(!this.$6w)this.$6w=0;return"_"+(this.$6w++)}
,isc.A.timeoutAction=function isc_c_Animation_timeoutAction(){if(isc.Animation)isc.Animation.fireTimer()}
,isc.A.registerAnimation=function isc_c_Animation_registerAnimation(_1,_2,_3,_4){if(!this.$6x){this.$6x=isc.Timer.setTimeout(this.timeoutAction,this.interval);this.$6y=isc.timeStamp()}
if(!_4)_4=this;if(!_2)_2=this.animateTime;if(isc.isA.String(_3)){if(!isc.Animation.accelerationMap){isc.Animation.accelerationMap={smoothStart:isc.Animation.smoothStart,smoothEnd:isc.Animation.smoothEnd,smoothStartEnd:isc.Animation.smoothStartEnd}}
_3=isc.Animation.accelerationMap[_3]}
var _5=this.generateAnimationID();this.registry.add({ID:_5,target:_4,callback:_1,duration:_2,elapsed:0,totalFrames:Math.round(_2/ this.interval),currentFrame:0,maxDuration:_2*3,acceleration:_3});return _5}
,isc.A.clearAnimation=function isc_c_Animation_clearAnimation(_1){for(var i=0;i<this.registry.length;i++){if(this.registry[i]&&this.registry[i].ID==_1){this.registry.removeAt(i);break}}}
,isc.A.finishAnimation=function isc_c_Animation_finishAnimation(_1){for(var i=0;i<this.registry.length;i++){if(this.registry[i]&&this.registry[i].ID==_1){var _3=this.registry[i];break}}
this.clearAnimation(_1);if(_3)this.fireAction(_3,1,true)}
,isc.A.fireTimer=function isc_c_Animation_fireTimer(){var _1=isc.timeStamp(),_2=(_1-this.$6y),_3=Math.max(0,this.interval-(_2-this.interval));this.$6x=isc.Timer.setTimeout(this.timeoutAction,_3);this.$6y=_1;for(var i=0;i<this.registry.length;i++){var _5=this.registry[i];if(_5==null)continue;_5.elapsed+=_2;var _6=_5.currentFrame+1;if(!isc.Animation.timeBased&&((_5.elapsed/ _5.maxDuration)>(_6/ _5.totalFrames)))
{_6=Math.min(_5.totalFrames,Math.ceil((_5.elapsed/ _5.maxDuration)*_5.totalFrames))}
_5.currentFrame=_6;var _7=isc.Animation.timeBased?_5.elapsed/ _5.duration:_5.currentFrame/ _5.totalFrames;var _8=_7,_9=_5.acceleration;if(_9&&isc.isA.Function(_9)){try{_8=_5.acceleration(_8)}catch(e){this.logWarn("Custom ratio function for animation:"+isc.Log.echoAll(_5)+"\nCaused an error:"+(e.message?e.message:e));_5.acceleration=null}}
if(_7>=1){_8=1;this.registry[i]=null}
var _10=null;try{_10=this.fireAction(_5,_8)}catch(e){_10=e}
if(_10!=null){this.logWarn("Attempt to fire registered animation:"+isc.Log.echoAll(_5)+"\nCaused an error:"+(_10.message?_10.message:_10));this.registry[i]=null}
if(_7>=1){this.logDebug("animation "+_5.ID+" completed","animation")}}
this.registry.removeEmpty();if(this.registry.length==0){isc.Timer.clearTimeout(this.$6x);this.$6x=null}}
,isc.A.fireAction=function isc_c_Animation_fireAction(_1,_2,_3){var _4=_1.target;if(!_4||_4.destroyed){return"No valid target. Target may have been destroyed since animation commenced"}
_4.fireCallback(_1.callback,this.$6v,[_2,_1.ID,_3])}
,isc.A.isActive=function isc_c_Animation_isActive(){return(this.registry&&this.registry.length>0)}
);isc.B._maxIndex=isc.C+8;isc.A=isc.Canvas.getPrototype();isc.A.animateTime=300;isc.A.animateAcceleration="smoothEnd";isc.A.$6z=["rect","fade","scroll","show","hide"];isc.A.animateShowEffect="wipe";isc.A.animateHideEffect="wipe";isc.A=isc.Canvas.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$60={};isc.A.$61={};isc.A.$62={};isc.A.$63="Animation";isc.A.$64={};isc.A.$65={};isc.A.$66=0;isc.A.$67="move";isc.A.$ox="resize";isc.A.$68="rect";isc.A.$d9="show";isc.A.$69="slide";isc.A.$7a="wipe";isc.A.$7b="fade";isc.A.$7c="fly";isc.A.$7d="T";isc.A.$7e="L";isc.A.$7f={slide:"show",wipe:"show",fly:"move",fade:"fade"};isc.A.$x4="hide";isc.A.$7g={slide:"hide",wipe:"hide",fly:"move",fade:"fade"};isc.B.push(isc.A.registerAnimation=function isc_Canvas_registerAnimation(_1,_2,_3){if(!_3)_3=this.animationAcceleration;if(!_2)_2=this.animateTime;return isc.Animation.registerAnimation(_1,_2,_3,this)}
,isc.A.cancelAnimation=function isc_Canvas_cancelAnimation(_1){isc.Animation.clearAnimation(_1)}
,isc.A.getAnimateTime=function isc_Canvas_getAnimateTime(_1){if(!isc.isA.String(_1)||isc.isAn.emptyString(_1))return this.animateTime;if(!this.$60[_1]){this.$60[_1]="animate"+_1.substring(0,1).toUpperCase()+_1.substring(1)+"Time"}
return this[this.$60[_1]]||this.animateTime}
,isc.A.getAnimateAcceleration=function isc_Canvas_getAnimateAcceleration(_1){if(!isc.isA.String(_1)||isc.isAn.emptyString(_1))return this.animateAcceleration;if(!this.$61[_1]){this.$61[_1]="animate"+_1.substring(0,1).toUpperCase()+_1.substring(1)+"Acceleration"}
return this[this.$61[_1]]||this.animateAcceleration}
,isc.A.$7h=function isc_Canvas__getAnimationID(_1){if(!this.$62[_1]){this.$62[_1]=_1+this.$63}
return this.$62[_1]}
,isc.A.$7i=function isc_Canvas__getAnimationMethodName(_1){if(!this.$64[_1]){this.$64[_1]="fireAnimation"+_1.substring(0,1).toUpperCase()+_1.substring(1)}
return this.$64[_1]}
,isc.A.$7j=function isc_Canvas__startAnimation(_1,_2,_3,_4){var _5=this.$7h(_1);if(this[_5])this.finishAnimation(_1);if(!this.$65[_1]){this.$65[_1]="$"+_1+"AnimationInfo"}
this[this.$65[_1]]=_2;if(_3==null)_3=this.getAnimateTime(_1);if(_4==null)_4=this.getAnimateAcceleration(_1);var _6=this[_5]=this.registerAnimation(this[this.$7i(_1)],_3,_4);if(this.logIsInfoEnabled("animation")){this.logInfo("starting animation "+_6+" of type: "+_1+", duration: "+_3+", acceleration: "+this.echoLeaf(_4),"animation")}
this.$66++;return _6}
,isc.A.$7k=function isc_Canvas__clearAnimationInfo(_1){var _2=this.$7h(_1);if(!this[_2]){return}
delete this[_2];delete this[this.$65[_1]];this.$66--}
,isc.A.animationComplete=function isc_Canvas_animationComplete(_1){}
,isc.A.$7l=function isc_Canvas__fireAnimationCompletionCallback(_1,_2,_3){if(!_1)return;var _4=this;var _5=function(){_4.fireCallback(_1,"earlyFinish",[_2]);_4.animationComplete(_2)}
if(_2||_3){_5()}else{isc.Timer.setTimeout(_5,0)}}
,isc.A.finishAnimation=function isc_Canvas_finishAnimation(_1){if(_1==null){for(var i=0;i<this.$6z.length;i++){this.finishAnimation(this.$6z[i])}
return}
var _3=this.$7h(_1);if(!this[_3])return;if(this.logIsInfoEnabled("animation")){this.logInfo("manual finish for animations: "+this.echoAll(this[_3])+(this.logIsDebugEnabled("animation")?this.getStackTrace():""),"animation")}
isc.Animation.finishAnimation(this[_3])}
,isc.A.animateMove=function isc_Canvas_animateMove(_1,_2,_3,_4,_5){return this.animateRect(_1,_2,null,null,_3,_4,_5,this.$67)}
,isc.A.fireAnimationMove=function isc_Canvas_fireAnimationMove(_1,_2,_3){return this.fireAnimationRect(_1,_2,_3,this.$67)}
,isc.A.animateResize=function isc_Canvas_animateResize(_1,_2,_3,_4,_5){return this.animateRect(null,null,_1,_2,_3,_4,_5,this.$ox)}
,isc.A.fireAnimationResize=function isc_Canvas_fireAnimationResize(_1,_2,_3){return this.fireAnimationRect(_1,_2,_3,this.$ox)}
,isc.A.animateRect=function isc_Canvas_animateRect(_1,_2,_3,_4,_5,_6,_7,_8){if(_8==null){_8=this.$68;if(this.resizeAnimation!=null)this.finishAnimation(this.$ox);if(this.moveAnimation!=null)this.finishAnimation(this.$67)}
var _9={$7m:this.getRect(),_left:_1,_top:_2,$7n:_3,$s9:_4,$7o:_5};return this.$7j(_8,_9,_6,_7)}
,isc.A.fireAnimationRect=function isc_Canvas_fireAnimationRect(_1,_2,_3,_4){var _5=(_4==this.$ox?this.$resizeAnimationInfo:(_4==this.$67?this.$moveAnimationInfo:this.$rectAnimationInfo)),_6=_5.$7m,_7=_5._left,_8=_5._top,_9=_5.$7n,_10=_5.$s9,_11=_7!=null?this.$7p(_6[0],_7,_1):null,_12=_8!=null?this.$7p(_6[1],_8,_1):null;var _13,_14;if(_9!=null&&_11!=null&&(_7-_6[0]!=0)){var _15=(_9-_6[2])/(_7-_6[0]);if(Math.floor(_15)==_15){_13=_6[2]+(_15*(_11-_6[0]))}}
if(_10!=null&&_12!=null&&(_8-_6[1]!=0)){var _15=(_10-_6[3])/(_8-_6[1]);if(Math.floor(_15)==_15){_14=_6[3]+(_15*(_12-_6[1]))}}
if(_13==null&&_9!=null){_13=this.$7p(_6[2],_9,_1)}
if(_14==null&&_10!=null){_14=this.$7p(_6[3],_10,_1)}
if(_1==1){if(_4==null)_4="rect";this.$7k(_4)}
this.setRect(_11,_12,_13,_14,(_1<1));if(this.isDirty())this.redraw("animated resize");if(_1==1){this.$7l(_5.$7o,_3)}}
,isc.A.$7p=function isc_Canvas__getRatioTargetValue(_1,_2,_3){if(_2==null)return _1;return(_1+Math.floor(_3*(_2-_1)))}
,isc.A.animateFade=function isc_Canvas_animateFade(_1,_2,_3,_4){if(!this.isDrawn()){this.setOpacity(_1);this.$7l(_2,true);return}
if(this.visibility==isc.Canvas.HIDDEN){this.setOpacity(0);this.show()}
if(_1==null)_1=100;var _5={$7q:this.opacity!=null?this.opacity:100,$7r:_1,$7o:_2};return this.$7j("fade",_5,_3,_4)}
,isc.A.fireAnimationFade=function isc_Canvas_fireAnimationFade(_1,_2,_3){var _4=this.$fadeAnimationInfo,_5=_4.$7q,_6=_4.$7r;var _7=this.$7p(_5,_6,_1);if(isc.Browser.isIE&&_7>0&&!_4.$7s){var _8=this.getStyleHandle();if(_8){_8.visibility=isc.Canvas.VISIBLE;_8.visibility=isc.Canvas.INHERIT}
var _9=this.peers;if(_9&&_9.length>0){for(var i=0;i<_9.length;i++){if(_9[i].$rw){var _8=_9[i].getStyleHandle();if(_8){_8.visibility=isc.Canvas.VISIBLE;_8.visibility=isc.Canvas.INHERIT}}}}
_4.$7s=true}
if(_1==1){this.$7k("fade")}
this.setOpacity(_7,(_1<1));if(_1==1)this.$7l(_4.$7o,_3)}
,isc.A.animateScroll=function isc_Canvas_animateScroll(_1,_2,_3,_4,_5){var _6=this.overflow;if(this.overflow==isc.Canvas.VISIBLE)return;var _7={$7t:this.getScrollLeft(),$7u:this.getScrollTop(),$7v:_1,$7w:_2,$7o:_3};return this.$7j("scroll",_7,_4,_5)}
,isc.A.fireAnimationScroll=function isc_Canvas_fireAnimationScroll(_1,_2,_3){var _4=this.$scrollAnimationInfo,_5=_4.$7t,_6=_4.$7v,_7=_4.$7u,_8=_4.$7w,_9=this.$7p(_5,_6,_1),_10=this.$7p(_7,_8,_1);if(_1==1){this.$7k("scroll")}
this.scrollTo(_9,_10,null,(_1<1));if(_1==1&&_4.$7o){this.$7l(_4.$7o,_3)}}
,isc.A.animateShow=function isc_Canvas_animateShow(_1,_2,_3,_4){if(_1==null)_1=this.animateShowEffect;var _5;if(isc.isAn.Object(_1)){_5=_1;_1=_1.effect}
if(this.$zj!=null)this.finishAnimation(this.$zj);if(this.isDrawn()&&this.isVisible()){return}
if(this.$zl!=null){return}
if(!this.isDrawn()){if(this.parentElement&&!this.parentElement.isDrawn()){this.show();this.logInfo("not animating show, component not drawn","animation");this.animateShowComplete(true);return}else{this.draw()}}
this.$zl=this.$7f[_1]||this.$d9;this.$7x=_2;if(!this.$7y)
this.$7y={target:this,methodName:"animateShowComplete"}
if(_1==this.$7b){var _6=this.opacity;this.$7z=_2;this.setOpacity(0);this.show();if(_3==null)_3=this.animateShowTime;if(_4==null)_4=this.animateShowAcceleration;return this.animateFade(_6,this.$7y,_3,_4)}else if(_1==this.$7c){if(this.parentElement!=null){this.logInfo("animateShow() called with 'fly' effect - not supported for child widgets"+" defaulting to standard 'wipe' animation instead.","animation");_1=this.$7a}else{if(_3==null)_3=this.animateShowTime;if(_4==null)_4=this.animateShowAcceleration;var _7=this.isRTL(),_8=this.getLeft(),_9=_7?isc.Page.getWidth()+isc.Page.getScrollLeft():0-this.getVisibleWidth();this.$70=this._percent_left,this.setLeft(_9);this.show();return this.animateMove(_8,null,this.$7y,_3,_4)}}
if(!this.$71(_1)){this.logInfo("not animating show, can't do clip animations","animation");this.show();this.animateShowComplete(true);return}
if(this.isVisible())this.hide();var _10=this.getVisibleHeight(),_11=this.getVisibleWidth(),_12=_5?_5.startFrom==this.$7d:true,_13=(_12?this.getScrollTop():this.getScrollLeft()),_14=(_1=="slide"),_15={$tt:this.$tt,$72:this.getHeight(),$73:_10,$ts:this.$ts,$74:this.getWidth(),$75:_11,$76:this._percent_width,$77:this._percent_height,$78:this.overflow,$x5:_12,$79:_13,$8a:_14,$7o:this.$7y};if(_12){if(this.vscrollOn&&this.vscrollbar){_15.$8b=this.vscrollbar.thumb.getTop();_15.$8c=this.vscrollbar.thumb.getHeight();if(this.vscrollbar.thumb){this.vscrollbar.thumb.$k8=false;this.vscrollbar.thumb.$8d=true}
this.vscrollbar.$8e=true;this.vscrollbar.$8d=true;this.vscrollbar.setHeight(1)}
if(this.hscrollOn&&this.hscrollbar){this.hscrollbar.$8d=true;if(this.hscrollbar.thumb)this.hscrollbar.thumb.$8d=true;if(!_15.$8a){this.hscrollbar.$k8=false}else{this.hscrollbar.setTop(this.getTop());this.hscrollbar.setHeight(1)}}}else{if(this.hscrollOn&&this.hscrollbar){_15.$8b=this.hscrollbar.thumb.getLeft();_15.$8c=this.hscrollbar.thumb.getWidth();this.hscrollbar.$8e=true;this.hscrollbar.$8d=true;if(this.hscrollbar.thumb){this.hscrollbar.thumb.$k8=false;this.hscrollbar.thumb.$8d=true}
this.hscrollbar.setWidth(1)}
if(this.vscrollOn&&this.vscrollbar){this.vscrollbar.$8d=true;if(this.vscrollbar.thumb)this.vscrollbar.thumb.$8d=true;if(!_15.$8a){this.vscrollbar.$k8=false}else{this.vscrollbar.setLeft(this.getLeft());this.vscrollbar.setWidth(1)}}}
if(this.showEdges&&this.$nw){this.$nw.$5m=true}
if(this.overflow==isc.Canvas.VISIBLE){this.setOverflow(isc.Canvas.HIDDEN)}
if(this.overflow==isc.Canvas.AUTO||this.overflow==isc.Canvas.SCROLL){this.$vt=true}
this.resizeTo((_12?_11:1),(_12?1:_10),true);if(_14)this.scrollTo((_12?null:_13+(_11-1)),(_12?_13+(_10-1):null));if(this.showEdges&&this.$nw){if(_12)
this.$z6(this.$nw.getHandle().firstChild.style,"height",_10);else
this.$z6(this.$nw.getHandle().firstChild.style,"width",_11);this.$nw.setOverflow(isc.Canvas.HIDDEN);if(_14){if(_12){var _16=this.$nw.$xy;this.$z6(this.getStyleHandle(),"marginTop",(this.getTopMargin()-_16))}else{var _16=this.$nw.$xw;this.$z6(this.getStyleHandle(),"marginLeft",(this.getLeftMargin()-_16))}}
this.$nw.show()}else{var _17=_12?(this.hscrollOn?this.hscrollbar:null):(this.vscrollOn?this.vscrollbar:null),_18=_12?(this.vscrollOn?this.vscrollbar:null):(this.hscrollOn?this.hscrollbar:null);if(_17&&_15.$8a){_17.show();if(_18)_18.show()}else{this.show()}}
return this.$7j(this.$d9,_15,_3,_4)}
,isc.A.fireAnimationShow=function isc_Canvas_fireAnimationShow(_1,_2,_3){var _4=this.$showAnimationInfo,_5=_4.$x5;if(_1<1){var _6=(_5?_4.$73:_4.$75),_7=this.$7p(1,_6,_1),_8=_6-_7,_9=(this.showEdges&&this.$nw),_10,_11;if(_9){_10=(_4.$8a?(_5?this.$nw.$xz:this.$nw.$xx):(_5?this.$nw.$xy:this.$nw.$xw)),_11=(_4.$8a?(_5?this.$nw.$xy:this.$nw.$xw):(_5?this.$nw.$xz:this.$nw.$xx));this.$nw.resizeTo((_5?null:_7),(_5?_7:null),true);if(_4.$8a){if(_5)this.$nw.scrollToBottom();else this.$nw.scrollToRight()}
if(_7<_10)return;if(_8<=_11){if(_4.$8a){var _12=(_5?"marginTop":"marginLeft"),_13=(_5?this.getTopMargin()-_8:this.getLeftMargin()-_8);this.$z6(this.getStyleHandle(),_12,_13);this.scrollTo((_5?null:_4.$79),(_5?_4.$8f:null),null,true)}
return}
if(!this.isVisible()){this.$zm=true;this.show();delete this.$zm}}
var _14=_5?this.vscrollOn:this.hscrollOn,_15=_5?this.hscrollOn:this.vscrollOn;if(_14){var _16;if(_5){_16=this.vscrollbar;if(_16)_16.resizeTo(null,_7)}else{_16=this.hscrollbar;var _17=_7;if(this.vscrollOn){if(_4.$8a){_17-=this.scrollbarSize}else{_17=Math.min(_7,_6-this.scrollbarSize)}}
if(_17>0){if(_16)_16.resizeTo(_17,null)}}
if(_4.$8a&&_16){if(_5)_16.scrollToBottom();else _16.scrollToRight()}
if(_16&&_16.thumb){var _18=_16.thumb;if(_4.$8a){var _19=_4.$8b-_8,_20=_19+Math.min(_7,_4.$8c),_21=_5?this.getTop():this.getLeft();if(_20<=_21){}else{_19=Math.max(_21,_19);var _22=Math.min(_20-_19,_7);_18.resizeTo(_5?null:_22,_5?_22:null);if(_5)_18.scrollToBottom()
else _18.scrollToRight();_18.moveTo(_5?null:_19,_5?_19:null);if(!_18.isVisible())_18.show()}}else{var _19=_4.$8b,_20=Math.min((_19+_4.$8c),(_5?this.getTop()+_7:this.getLeft()+_7));var _23=(_5?this.getTop():this.getLeft())+_7
if(_23<=_19){}else{if(_5)_18.setHeight(_20-_19);else _18.setWidth(_20-_19);if(!_18.isVisible())_18.show()}}}}
var _24=0;if(_15&&_25){var _25=_5?this.hscrollbar:this.vscrollbar;if(_4.$8a){var _26=_5?(this.getTop()+Math.max(0,(_7-this.scrollbarSize))):(this.getLeft()+Math.max(0,(_7-this.scrollbarSize)))
_25.moveTo(_5?null:_26,_5?_26:null);var _27=Math.min(_7,this.scrollbarSize);_25.resizeTo(_5?null:_27,_5?_27:null);if(_5){_25.scrollToBottom();if(_25.thumb)_25.thumb.scrollToBottom()}else{_25.scrollToRight();if(_25.thumb)_25.thumb.scrollToRight()}
if(_7>this.scrollbarSize&&!this.isVisible()){this.$zm=true;this.show();delete this.$zm}}else{if(_8<=this.scrollbarSize){if(!_25.isVisible())_25.show();_25.resizeTo(_5?null:this.scrollbarSize-_8,_5?this.scrollbarSize-_8:null)}}
if(_25.isVisible()){_24=this.scrollbarSize-
(_5?_25.getHeight():_25.getWidth())}else{_24=this.scrollbarSize}}
var _28=_7;if(_9)_28+=_11;if(_24)_28+=_24
if(!this.resizeTo((_5?null:_28),(_5?_28:null),true))
{this.$ym()}
if(_4.$8a){this.scrollTo((_5?null:_4.$79+_8),(_5?_4.$79+_8:null),null,true)}}else{if(!this.isVisible())this.show();this.$7k("show");this.setOverflow(_4.$78);if(this.overflow==isc.Canvas.AUTO||this.overflow==isc.Canvas.SCROLL){delete this.$vt;if(this.vscrollOn&&this.vscrollbar){if(this.vscrollbar.visibility==isc.Canvas.HIDDEN)this.vscrollbar.show();if(_5)delete this.vscrollbar.$8e;delete this.vscrollbar.$8d;this.vscrollbar.$k8=true;if(_4.$8a)this.vscrollbar.scrollTo(0,0);if(this.vscrollbar.thumb){delete this.vscrollbar.thumb.$8d;this.vscrollbar.thumb.$k8=true;if(_4.$8g)this.vscrollbar.thumb.scrollTo(0,0)}
if(!_5){this.vscrollbar.setWidth(this.getScrollbarSize());this.vscrollbar.setThumb()}}
if(this.hscrollOn&&this.hscrollbar){if(this.hscrollbar.visibility==isc.Canvas.HIDDEN)this.hscrollbar.show();if(!_5){delete this.hscrollbar.$8e}else{this.hscrollbar.setHeight(this.getScrollbarSize());this.hscrollbar.setThumb()}
delete this.hscrollbar.$8d;this.hscrollbar.$k8=true;if(_4.$8a)this.hscrollbar.scrollTo(0,0);if(this.hscrollbar.thumb){delete this.hscrollbar.thumb.$8d;this.hscrollbar.thumb.$k8=true;if(_4.$8a)this.hscrollbar.thumb.scrollTo(0,0)}}}
if(this.showEdges&&this.$nw){if(_4.$8a){var _12=(_5?"marginTop":"marginLeft"),_13=(_5?this.getTopMargin():this.getLeftMargin());this.$z6(this.getStyleHandle(),_12,_13);this.$nw.scrollTo((_5?null:0),(_5?0:null))}
if(_5)
this.$nw.getHandle().firstChild.style.height="100%";else
this.$nw.getHandle().firstChild.style.width="100%";this.$nw.setOverflow(isc.Canvas.VISIBLE);delete this.$nw.$5m}
if(!this.resizeTo(_4.$74,_4.$72)){this.$ym()}
this.$ts=_4.$ts;this.$tt=_4.$tt;this._percent_width=_4.$76;this._percent_height=_4.$77;if(_4.$8a)this.scrollTo((_5?null:_4.$79),(_5?_4.$79:null));if(_4.$7o){this.$7l(_4.$7o,_3)}}}
,isc.A.animateShowComplete=function isc_Canvas_animateShowComplete(_1){if(this.$70!=null){this._percent_left=this.$70;delete this.$70}
this.$zl=null;var _2=this.$7x;this.$7x=null;if(_2)this.$7l(_2,_1,true)}
,isc.A.$71=function isc_Canvas__canAnimateClip(_1){if(this.canAnimateClip!=null)return this.canAnimateClip;return(this.scrollTo==isc.Canvas.getInstanceProperty("scrollTo"))}
,isc.A.animateHide=function isc_Canvas_animateHide(_1,_2,_3,_4,_5){if(_1==null)_1=this.animateHideEffect;var _6;if(isc.isAn.Object(_1)){_6=_1;_1=_6.effect}
if(this.$zl!=null){this.finishAnimation(this.$zl)}
if(!this.isVisible())return;if(this.$zj!=null)return;if(!this.isDrawn()&&!isc.isA.LayoutSpacer(this)){this.hide();if(_2)this.$7l(_2,true);return}
this.$zj=this.$7g[_1]||this.$x4;this.$8h=_2;if(!this.$8i)
this.$8i={target:this,methodName:"$8j"}
if(_1==this.$7b){this.$8k=this.opacity;this.$8l=true;if(_3==null)_3=this.animateHideTime;if(_4==null)_4=this.animateHideAcceleration;return this.animateFade(0,this.$8i,_3,_4,_5)}else if(_1==this.$7c){this.$8m=this.getLeft();this.$8n=this._percent_left;if(this.parentElement!=null){this.logInfo("animateHide() called with 'fly' effect - not supported for child widgets"+" defaulting to standard 'wipe' animation instead.","animation");_1=this.$7a}else{if(_3==null)_3=this.animateShowTime;if(_4==null)_4=this.animateShowAcceleration;var _7=this.isRTL(),_8=_7?isc.Page.getWidth()+isc.Page.getScrollLeft():0-this.getVisibleWidth();return this.animateMove(_8,null,this.$8i,_3,_4,_5)}}
if((!this.$71(_1)||!this.isDrawn())&&!this.isA(isc.LayoutSpacer))
{this.logInfo("not animating hide, can't do clip animations","animation");this.hide();this.$8j(true);return}
var _9=this.getVisibleHeight(),_10=this.getVisibleWidth(),_11=(_6?_6.endAt==this.$7d:true),_12={$tt:this.$tt,$72:this.getHeight(),$73:_9,$ts:this.$ts,$74:this.getWidth(),$75:_10,$79:(_11?this.getScrollTop():this.getScrollLeft()),$x5:_11,$8o:_1=="slide",$78:this.overflow,$7o:this.$8i,$8p:_5};if(_12.$8o){if(_11&&this.vscrollOn&&this.vscrollbar){_12.$8b=this.vscrollbar.thumb.getTop();_12.$8c=this.vscrollbar.thumb.getHeight()}else if(!_11&&this.hscrollOn&&this.hscrollbar){_12.$8b=this.hscrollbar.thumb.getLeft();_12.$8c=this.hscrollbar.thumb.getWidth()}}
this.resizeTo(_10,_9,true);if(this.overflow==isc.Canvas.VISIBLE)this.setOverflow(isc.Canvas.HIDDEN);if(this.overflow==isc.Canvas.AUTO||this.overflow==isc.Canvas.SCROLL){this.$vt=true;if(this.vscrollOn&&this.vscrollbar){this.vscrollbar.$k8=false;if(_11)this.vscrollbar.$8e=true;this.vscrollbar.$8d=true;if(this.vscrollbar.thumb){this.vscrollbar.thumb.$8d=true}}
if(this.hscrollOn&&this.hscrollbar){this.hscrollbar.$k8=false;if(!_11)this.hscrollbar.$8e=true;this.hscrollbar.$8d=true;if(this.hscrollbar.thumb){this.hscrollbar.thumb.$8d=true}}}
if(this.showEdges){this.$nw.setOverflow("hidden");this.$nw.$5m=true;this.$z6(this.$nw.getHandle().firstChild.style,(_11?"height":"width"),(_11?this.$nw.getHeight():this.$nw.getWidth()))}
return this.$7j(this.$x4,_12,_3,_4)}
,isc.A.fireAnimationHide=function isc_Canvas_fireAnimationHide(_1,_2,_3){var _4=this.$hideAnimationInfo,_5=_4.$x5;if(_1<1){var _6=(_5?_4.$73:_4.$75),_7=this.$7p(_6,1,_1),_8=_6-_7,_9=(this.showEdges&&this.$nw),_10,_11,_12=this.hscrollOn&&this.hscrollbar,_13=this.vscrollOn&&this.vscrollbar;if(_9){_10=(_4.$8o?(_5?this.$nw.$xy:this.$nw.$xw):(_5?this.$nw.$xz:this.$nw.$xx));_11=(_4.$8o?(_5?this.$nw.$xz:this.$8q.$xx):(_5?this.$nw.$xy:this.$nw.$xw));this.$nw.resizeTo((_5?null:_7),(_5?_7:null),true);if(_4.$8o){if(_5)this.$nw.scrollToBottom();else this.$nw.scrollToRight()}
if(_8<_10){if(_4.$8o){var _14=(_5?"marginTop":"marginLeft"),_15=(_5?this.getTopMargin():this.getLeftMargin())
this.$z6(this.getStyleHandle(),_14,(_15-_8))}
this.$ym();return}
if(_4.$8o&&!this.$8r){var _14=(_5?"marginTop":"marginLeft"),_15=(_5?this.getTopMargin():this.getLeftMargin())
this.$z6(this.getStyleHandle(),_14,(_15-_10));this.$8r=true}
if(_9&&_7<=_11){this.$zk=true;this.getStyleHandle().visibility=isc.Canvas.HIDDEN;delete this.$zk}}
var _16=_5?(_13?this.vscrollbar:null):(_12?this.hscrollbar:null);if(_16){if(_5)_16.setHeight(_7);else{var _17=_7;if(this.vscrollOn){if(_4.$8o){_17-=this.scrollbarSize}else{_17=Math.min(_7,_6-this.scrollbarSize)}}
if(_17>0)_16.setWidth(_17);else _16.hide()}
if(_4.$8o){if(_5)_16.scrollToBottom();else _16.scrollToRight()}
if(_16.thumb&&_16.thumb.isVisible()){if(_4.$8o){var _18=_4.$8b-_8,_19=_5?this.getTop():this.getLeft();if(_18>=_19){_16.thumb.moveTo(_5?null:_18,_5?_18:null)}else{_16.thumb.moveTo(_5?null:this.getLeft(),_5?this.getTop():null);var _20=_4.$8c+(_18-_19);if(_20>0){_16.thumb.resizeTo(_5?null:_20,_5?_20:null);_16.thumb.scrollTo(_5?null:_19-_18,_5?_19-_18:null)}else{_16.thumb.hide()}}}else{if(_5){var _21=(this.getTop()+_7)
if(_16.thumb.getBottom()>_21){var _22=_21-_16.thumb.getTop();if(_22>0)_16.thumb.setHeight(_22);else _16.thumb.hide()}}else{var _23=(this.getLeft()+_7)
if(_16.thumb.getRight()>_23){var _24=_23-_16.thumb.getLeft();if(_24>0)_16.thumb.setWidth(_24);else _16.thumb.hide()}}}}}
var _25=_5?(_12?this.hscrollbar:null):(_13?this.vscrollbar:null),_26=0;if(_25){var _27=this.scrollbarSize;if(_4.$8o){if(_7>=_27){var _28=(_5?this.getTop():this.getLeft())+_7-_27;_25.moveTo(_5?null:_28,_5?_28:null)}else{_25.moveTo(_5?null:this.getLeft(),_5?this.getTop():null);_25.resizeTo(_5?null:_7,_5?_7:null);if(_5)_25.scrollToBottom();else _25.scrollToRight();var _29=_25.thumb
if(_29){_29.resizeTo(_5?null:_7,_5?_7:null);if(_5)_29.scrollToBottom();else _29.scrollToRight()}}
if(_7<=_27){this.$zk=true;if(this.isVisible())this.hide();delete this.$zk;return}}else{if(_8<=_27){_25.resizeTo(_5?null:_27-_8,_5?_27-_8:null);if(_25.thumb){_25.thumb.resizeTo(_5?null:_27-_8,_5?_27-_8:null)}}else{if(_25.isVisible())_25.hide()}}
if(_25.isVisible()){_26=this.scrollbarSize-
(_5?_25.getHeight():_25.getWidth())}else{_26=this.scrollbarSize}}
var _30=_7;if(_9)_30+=_10;if(_26)_30+=_26;if(!this.resizeTo((_5?null:_30),(_5?_30:null),true))
{this.$ym()}
var _31;if(_4.$8o){this.scrollTo((_5?null:_4.$79+_8),(_5?_4.$79+_8:null),null,true)}}else{this.$7k("hide");if(this.isVisible())this.hide();if(_4.$78)this.setOverflow(_4.$78);if(this.showEdges&&this.$nw){delete this.$8r;this.$nw.setOverflow(isc.Canvas.VISIBLE);delete this.$nw.$5m;if(_5)this.$nw.getHandle().firstChild.style.height="100%";else this.$nw.getHandle().firstChild.style.width="100%"
if(_4.$8o){var _32=this.$qx(),_14=(_5?"marginTop":"marginLeft"),_15=(_5?_32.top:_32.left)
this.$z6(this.getStyleHandle(),_14,_15)}}
if(this.overflow==isc.Canvas.AUTO||this.overflow==isc.Canvas.SCROLL){delete this.$vt;if(_5){if(this.vscrollOn&&this.vscrollbar){if(this.vscrollbar.isVisible())this.vscrollbar.hide();delete this.vscrollbar.$8e;delete this.vscrollbar.$8d;this.vscrollbar.$k8=true;if(this.vscrollbar.thumb){delete this.vscrollbar.thumb.suppressImageResize}
if(_4.$8o){this.vscrollbar.scrollTo(0,0);this.vscrollbar.setHeight(this.getHeight());if(this.vscrollbar.thumb)this.vscrollbar.thumb.scrollTo(0,0)}}
if(this.hscrollOn&&this.hscrollbar){if(this.hscrollbar.isVisible())this.hscrollbar.hide();this.hscrollbar.$k8=true;delete this.hscrollbar.$8d;if(_4.$8o)this.hscrollbar.scrollTo(0,0);if(this.hscrollbar.thumb){delete this.hscrollbar.thumb.$8d;if(_4.$8o)this.hscrollbar.thumb.scrollTo(0,0)}}}else{if(this.hscrollOn&&this.hscrollbar){if(this.hscrollbar.isVisible())this.hscrollbar.hide();delete this.hscrollbar.$8e;delete this.hscrollbar.$8d;this.hscrollbar.$k8=true;if(this.hscrollbar.thumb)
delete this.hscrollbar.$8d;if(_4.$8o){this.hscrollbar.scrollTo(0,0);this.hscrollbar.setWidth(this.getWidth());if(this.hscrollbar.thumb)this.hscrollbar.thumb.scrollTo(0,0)}}
if(this.vscrollOn&&this.vscrollbar){if(this.vscrollbar.isVisible())this.vscrollbar.hide();this.vscrollbar.$k8=true;delete this.vscrollbar.$8d;if(_4.$8o)this.vscrollbar.scrollTo(0,0);if(this.vscrollbar.thumb){if(_4.$8o)this.vscrollbar.thumb.scrollTo(0,0);delete this.vscrollbar.$8d}}}}
this.resizeTo(_4.$74,_4.$72);this.$tt=_4.$tt;this.$ts=_4.$ts;if(_4.$8o)this.scrollTo((_5?null:_4.$79),(_5?_4.$79:null));if(_4.$7o){this.$7l(_4.$7o,_3,true)}}}
,isc.A.$8j=function isc_Canvas__animateHideComplete(_1){delete this.$zj;var _2=this.$8h;delete this.$8h;if(this.isVisible())this.hide();if(this.$8l){this.setOpacity(this.$8k);delete this.$8k;delete this.$8l}
if(this.$8m!=null){this.setLeft(this.$8m);delete this.$8m}
if(this.$8n!=null){this._percent_left=this.$8n;delete this.$8n}
if(_2){this.$7l(_2,_1,true)}}
,isc.A.isAnimating=function isc_Canvas_isAnimating(_1){if(_1==null)return this.$66>0;if(_1&&!isc.isAn.Array(_1)){if(!this.$8s)this.$8s=[];this.$8s[0]=_1;_1=this.$8s}
if(!_1)_1=this.$6z;for(var i=0;i<_1.length;i++){if(this[this.$7h(_1[i])]!=null){return true}}
return false}
);isc.B._maxIndex=isc.C+30;isc.ClassFactory.defineClass("StatefulCanvas","Canvas");isc.A=isc.StatefulCanvas;isc.A.STATE_UP="";isc.A.STATE_DOWN="Down";isc.A.STATE_OVER="Over";isc.A.STATE_DISABLED="Disabled";isc.A.BUTTON="button";isc.A.CHECKBOX="checkbox";isc.A.RADIO="radio";isc.A.FOCUSED="Focused";isc.A.SELECTED="Selected";isc.A.UNSELECTED="";isc.A.$8t={};isc.A=isc.StatefulCanvas.getPrototype();isc.A.state="";isc.A.showFocusedAsOver=true;isc.A.showDisabled=true;isc.A.actionType="button";isc.A.cursor=isc.Canvas.ARROW;isc.A.capSize=0;isc.A.align=isc.Canvas.CENTER;isc.A.valign=isc.Canvas.CENTER;isc.A.autoFitDirection=isc.Canvas.BOTH;isc.A.iconSize=16;isc.A.iconOrientation="left";isc.A.iconSpacing=6;isc.A.showIconState=true;isc.A.showDisabledIcon=true;isc.A.gripImgSuffix="grip";isc.A.overCanvasConstructor="Canvas";isc.A.overCanvasDefaults={mouseOut:function(){if(isc.EH.getTarget()!=this.creator)this.clear();return this.Super("mouseOut",arguments)}};isc.A=isc.StatefulCanvas.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$8u="visualState";isc.A.$8v="SelectedFocused";isc.A.labelDefaults={$mb:function(){return this.masterElement.$mb()},focusChanged:function(_1){if(this.hasFocus)this.eventProxy.focus()},getContents:function(){return this.masterElement.getTitleHTML()},adjustOverflow:function(_1,_2,_3,_4){this.invokeSuper(null,"adjustOverflow",_1,_2,_3,_4);if(this.masterElement)this.masterElement.$8w()}};isc.A.$8x="label";isc.A.$8y="Label overflowed.";isc.A.$8z="$ym";isc.B.push(isc.A.initWidget=function isc_StatefulCanvas_initWidget(){if(this.src==null)this.src=this.vertical?this.vSrc:this.hSrc;var _1=!this.isDisabled();if(!_1){this.$80=this.state;if(this.showDisabled)this.state=isc.StatefulCanvas.STATE_DISABLED}
this.baseStyle=this.baseStyle||this.className;this.styleName=(this.suppressClassName?null:this.getStateName());this.className=this.styleName;if(this.radioGroup!=null){var _2=this.radioGroup;this.radioGroup=null;this.addToRadioGroup(_2)}
this.setAutoFit(this.autoFit,true);if(this.showGrip){this.showTitle=true;this.labelVPad=0;this.labelHPad=0;this.iconSpacing=0;this.align=isc.Canvas.CENTER;this.icon=this.getImgURL(this.getURL(this.gripImgSuffix));this.iconSize=this.gripSize;this.iconWidth=this.vertical?this.gripBreadth:this.gripLength;this.iconHeight=this.vertical?this.gripLength:this.gripBreadth;this.showRollOverIcon=this.showRollOverGrip;this.showDownIcon=this.showDownGrip}
var _3=this.shouldShowLabel();if(_3)this.makeLabel();if(isc.screenReader&&!_3&&!this.showGrip&&(this.prompt||this.title)){var _4=this.prompt||this.title;if(isc.Button.getInstanceProperty("title")!=_4){this.ariaState={label:_4}}}}
,isc.A.getURL=function isc_StatefulCanvas_getURL(_1,_2,_3,_4){return isc.Img.urlForState(this.src,_3!=null?_3:this.selected,_4!=null?_4:this.getFocusedState(),_2!=null?_2:this.state,_1,this.getCustomState())}
,isc.A.shouldShowLabel=function isc_StatefulCanvas_shouldShowLabel(){return this.showTitle}
,isc.A.stateChanged=function isc_StatefulCanvas_stateChanged(){if(this.destroyed)return;if(this.logIsDebugEnabled(this.$8u)){this.logDebug("state changed to: "+this.getStateName(),"visualState")}
if(this.redrawOnStateChange){this.markForRedraw("state change")}
if(!this.suppressClassName){this.setClassName(this.getStateName())}
var _1=this.label;if(_1!=null){_1.setState(this.getState());_1.setSelected(this.isSelected());_1.setCustomState(this.getCustomState())}}
,isc.A.setBaseStyle=function isc_StatefulCanvas_setBaseStyle(_1){this.baseStyle=_1;if(this.label&&this.titleStyle==null)this.label.setBaseStyle(_1);this.stateChanged()}
,isc.A.setTitleStyle=function isc_StatefulCanvas_setTitleStyle(_1){this.titleStyle=_1;if(this.label){this.label.setBaseStyle(_1||this.baseStyle)}
this.stateChanged()}
,isc.A.setState=function isc_StatefulCanvas_setState(_1){if(this.state==_1)return;this.state=_1;this.stateChanged()}
,isc.A.$w9=function isc_StatefulCanvas__updateChildrenTopElement(){this.Super("$w9",arguments);this.setHandleDisabled(this.isDisabled())}
,isc.A.getState=function isc_StatefulCanvas_getState(){return this.state}
,isc.A.setSelected=function isc_StatefulCanvas_setSelected(_1){if(this.selected==_1)return;if(_1&&this.radioGroup!=null){var _2=isc.StatefulCanvas.$8t[this.radioGroup];if(_2==null){this.logWarn("'radioGroup' property set for this widget, but no corresponding group "+"exists. To set up a new radioGroup containing this widget, or add this "+" widget to an existing radioGroup at runtime, call 'addToRadioGroup(groupID)'")}else{for(var i=0;i<_2.length;i++){if(_2[i]!=this&&_2[i].isSelected())
_2[i].setSelected(false)}}}
this.selected=_1;if(this.label)this.label.setSelected(this.isSelected());this.stateChanged()}
,isc.A.select=function isc_StatefulCanvas_select(){this.setSelected(true)}
,isc.A.deselect=function isc_StatefulCanvas_deselect(){this.setSelected(false)}
,isc.A.isSelected=function isc_StatefulCanvas_isSelected(){return this.selected}
,isc.A.getActionType=function isc_StatefulCanvas_getActionType(){return this.actionType}
,isc.A.setActionType=function isc_StatefulCanvas_setActionType(_1){if(_1==isc.StatefulCanvas.BUTTON&&this.isSelected()){this.setSelected(false)}
this.actionType=_1}
,isc.A.addToRadioGroup=function isc_StatefulCanvas_addToRadioGroup(_1){if(_1==null||this.radioGroup==_1)return;if(this.radioGroup!=null)this.removeFromRadioGroup();this.radioGroup=_1;if(isc.StatefulCanvas.$8t[this.radioGroup]==null){isc.StatefulCanvas.$8t[this.radioGroup]=[this]}else{isc.StatefulCanvas.$8t[this.radioGroup].add(this)}}
,isc.A.removeFromRadioGroup=function isc_StatefulCanvas_removeFromRadioGroup(_1){if(this.radioGroup==null||(_1!=null&&_1!=this.radioGroup))return;var _2=isc.StatefulCanvas.$8t[this.radioGroup];_2.remove(this);delete this.radioGroup}
,isc.A.setHandleDisabled=function isc_StatefulCanvas_setHandleDisabled(_1,_2,_3,_4){this.invokeSuper(isc.StatefulCanvas,"setHandleDisabled",_1,_2,_3,_4);if(!this.showDisabled)return;var _5=(this.state==isc.StatefulCanvas.STATE_DISABLED);if(_5==_1)return;if(_1==false){if(this.$81)this.setCursor(this.$81);var _6=this.$80||isc.StatefulCanvas.STATE_UP;this.setState(_6)}else{this.$81=this.cursor;this.setCursor(isc.StatefulCanvas.ARROW);this.$80=this.state;this.setState(isc.StatefulCanvas.STATE_DISABLED)}}
,isc.A.getStateName=function isc_StatefulCanvas_getStateName(){var _1=this.getStateSuffix();if(_1)return this.baseStyle+_1;return this.baseStyle}
,isc.A.getStateSuffix=function isc_StatefulCanvas_getStateSuffix(){var _1=this.getState(),_2=this.isSelected()?isc.StatefulCanvas.SELECTED:null,_3=this.getFocusedState()?isc.StatefulCanvas.FOCUSED:null,_4=this.getCustomState();return this.$82(_1,_2,_3,_4)}
,isc.A.$82=function isc_StatefulCanvas__getStateSuffix(_1,_2,_3,_4){var _5;if(_2||_3){_5=(_2&&_3)?this.$8v:_2?_2:_3}
if(!_4){if(_5)return _1?_5+_1:_5;else return _1}else if(_5){return _1?_5+_1+_4:_5+_4}else{return _1?_1+_4:_4}}
,isc.A.setCustomState=function isc_StatefulCanvas_setCustomState(_1){if(_1==this.customState)return;this.customState=_1;this.stateChanged()}
,isc.A.getCustomState=function isc_StatefulCanvas_getCustomState(){return this.customState}
,isc.A.getPrintStyleName=function isc_StatefulCanvas_getPrintStyleName(){return this.printStyleName||this.getStateName()}
,isc.A.makeLabel=function isc_StatefulCanvas_makeLabel(){var _1=this.getAutoChildClass(this.$8x,null,isc.Label);var _2=this.label=_1.createRaw();_2.align=this.align;_2.valign=this.valign;_2.$k6=false;_2.$k7=false;_2._redrawWithParent=false;_2.containedPeer=true;_2.icon=this.icon;_2.iconWidth=this.iconWidth;_2.iconHeight=this.iconHeight;_2.iconSize=this.iconSize;_2.iconOrientation=this.iconOrientation;_2.iconAlign=this.iconAlign;_2.iconSpacing=this.iconSpacing;_2.showDownIcon=this.showDownIcon;_2.showSelectedIcon=this.showSelectedIcon;_2.showRollOverIcon=this.showRollOverIcon;_2.showFocusedIcon=this.showFocusedIcon;_2.showDisabledIcon=this.showDisabledIcon;if(this.showIconState!=null)_2.showIconState=this.showIconState;_2.getFocusedState=function(){var _3=this.masterElement;if(_3&&_3.getFocusedState)return _3.getFocusedState()}
_2.skinImgDir=this.labelSkinImgDir||this.skinImgDir;_2.baseStyle=this.titleStyle||this.baseStyle;_2.state=this.getState();_2.customState=this.getCustomState();_2.getPrintStyleName=function(){return this.masterElement.getPrintStyleName()}
_2.overflow=this.overflow;_2.width=this.$83();_2.height=this.$84();_2.left=this.$85();_2.top=this.$86();_2.wrap=this.wrap!=null?this.wrap:this.vertical;_2.eventProxy=this;_2.isMouseTransparent=true;_2.zIndex=this.getZIndex(true)+1;_2.tabIndex=-1;this.$eq(this.$8x,_2);this.label.setSelected(this.isSelected());this.addPeer(this.label,null,null,true)}
,isc.A.setLabelSkinImgDir=function isc_StatefulCanvas_setLabelSkinImgDir(_1){this.labelSkinImgDir=_1;if(this.label!=null)this.label.setSkinImgDir(_1)}
,isc.A.setSkinImgDir=function isc_StatefulCanvas_setSkinImgDir(_1){this.Super("setSkinImgDir",arguments);if(this.labelSkinImgDir==null&&this.label!=null)this.label.setSkinImgDir(_1)}
,isc.A.setIconOrientation=function isc_StatefulCanvas_setIconOrientation(_1){this.iconOrientation=_1;if(this.label){this.label.iconOrientation=_1;this.label.markForRedraw()}else{this.markForRedraw()}}
,isc.A.setAutoFit=function isc_StatefulCanvas_setAutoFit(_1,_2){if(_2){this.$87=true;if(!_1)return}
if(!this.$87)return;_1=!!_1;if(!_2&&(!!this.autoFit==_1))return;this.$88=true;this.autoFit=_1;var _3=(this.autoFitDirection==isc.Canvas.BOTH)||(this.autoFitDirection==isc.Canvas.HORIZONTAL),_4=(this.autoFitDirection==isc.Canvas.BOTH)||(this.autoFitDirection==isc.Canvas.VERTICAL);this.inherentWidth=_1&&_3;this.inherentHeight=_1&&_4;if(_1){this.$89=this.overflow;this.setOverflow(isc.Canvas.VISIBLE);if(_3){this.$9a=this.width;this.setWidth(1)}
if(_4){this.$9b=this.height;this.setHeight(1)}}else{var _5=this.$9a||this.defaultWidth,_6=this.$9b||this.defaultHeight;if(_3)this.setWidth(_5);if(_4)this.setHeight(_6);if(this.parentElement&&isc.isA.Layout(this.parentElement)){if(_3&&!this.$9a)this.$ts=null;if(_4&&!this.$9b)this.$tt=null}
this.$9a=null;this.$9b=null;if(this.$89)this.setOverflow(this.$89);this.$89=null}
delete this.$88}
,isc.A.resizeBy=function isc_StatefulCanvas_resizeBy(_1,_2,_3,_4,_5,_6){if(this.autoFit&&this.$87&&!this.$88){var _7=false;if(_1!=null&&(this.autoFitDirection==isc.Canvas.BOTH||this.autoFitDirection==isc.Canvas.HORIZONTAL))
{this.$9a=(1+_1);_7=true;_1=null}
if(_2!=null&&(this.autoFitDirection==isc.Canvas.BOTH||this.autoFitDirection==isc.Canvas.VERTICAL))
{this.$9b=(1+_2);_7=true;_2=null}
if(_7)this.setAutoFit(false)}
return this.invokeSuper(isc.StatefulCanvas,"resizeBy",_1,_2,_3,_4,_5,_6)}
,isc.A.getLabelHPad=function isc_StatefulCanvas_getLabelHPad(){if(this.labelHPad!=null)return this.labelHPad;if(this.vertical){return this.labelBreadthPad!=null?this.labelBreadthPad:0}else{return this.labelLengthPad!=null?this.labelLengthPad:this.capSize}}
,isc.A.getLabelVPad=function isc_StatefulCanvas_getLabelVPad(){if(this.labelVPad!=null)return this.labelVPad;if(!this.vertical){return this.labelBreadthPad!=null?this.labelBreadthPad:0}else{return this.labelLengthPad!=null?this.labelLengthPad:this.capSize}}
,isc.A.$85=function isc_StatefulCanvas__getLabelLeft(){var _1;if(this.isDrawn()){_1=(this.position==isc.Canvas.RELATIVE&&this.parentElement==null?this.getPageLeft():this.getOffsetLeft())}else{_1=this.getLeft()}
_1+=this.getLabelHPad();return _1}
,isc.A.$86=function isc_StatefulCanvas__getLabelTop(){var _1;if(this.isDrawn()){_1=(this.position==isc.Canvas.RELATIVE&&this.parentElement==null?this.getPageTop():this.getOffsetTop())}else{_1=this.getTop()}
_1+=this.getLabelVPad();return _1}
,isc.A.$83=function isc_StatefulCanvas__getLabelSpecifiedWidth(){var _1=this.getInnerWidth();_1-=2*this.getLabelHPad();return Math.max(_1,1)}
,isc.A.$84=function isc_StatefulCanvas__getLabelSpecifiedHeight(){var _1=this.getInnerHeight();_1-=2*this.getLabelVPad();return Math.max(_1,1)}
,isc.A.getImgBreadth=function isc_StatefulCanvas_getImgBreadth(){if(this.overflow==isc.Canvas.VISIBLE&&isc.isA.Canvas(this.label))
{return this.vertical?this.$9c():this.$9d()}
return(this.vertical?this.getInnerWidth():this.getInnerHeight())}
,isc.A.getImgLength=function isc_StatefulCanvas_getImgLength(){if(this.overflow==isc.Canvas.VISIBLE&&isc.isA.Canvas(this.label))
{return this.vertical?this.$9d():this.$9c()}
return(this.vertical?this.getInnerHeight():this.getInnerWidth())}
,isc.A.$9d=function isc_StatefulCanvas__getAutoInnerHeight(){var _1=this.getInnerHeight();if(!isc.isA.Canvas(this.label))return _1;var _2=this.getLabelVPad();var _3=this.label.getVisibleHeight()+2*_2;return Math.max(_3,_1)}
,isc.A.$9c=function isc_StatefulCanvas__getAutoInnerWidth(){var _1=this.getInnerWidth();if(!isc.isA.Canvas(this.label))return _1;var _2=this.getLabelHPad();var _3=this.label.getVisibleWidth()+2*_2;return Math.max(_3,_1)}
,isc.A.$8w=function isc_StatefulCanvas__labelAdjustOverflow(){if(this.overflow!=isc.Canvas.VISIBLE)return;this.adjustOverflow(this.$8y)}
,isc.A.getScrollWidth=function isc_StatefulCanvas_getScrollWidth(_1,_2,_3,_4){if(this.overflow!=isc.Canvas.VISIBLE||!isc.isA.Canvas(this.label))
return this.invokeSuper(isc.StatefulCanvas,"getScrollWidth",_1,_2,_3,_4);if(this.$u9){this.$u9=null;this.adjustOverflow("widthCheckWhileDeferred")}
if(!_1&&this.$xg!=null)return this.$xg;var _5=this.$9c()
return(this.$xg=_5)}
,isc.A.getScrollHeight=function isc_StatefulCanvas_getScrollHeight(_1,_2,_3,_4){if(this.overflow!=isc.Canvas.VISIBLE||!isc.isA.Canvas(this.label))
return this.invokeSuper(isc.StatefulCanvas,"getScrollHeight",_1,_2,_3,_4);if(this.$u9){this.$u9=null;this.adjustOverflow("heightCheckWhileDeferred")}
if(!_1&&this.$xk!=null)return this.$xk;var _5=this.$9d()
return(this.$xk=_5)}
,isc.A.setOverflow=function isc_StatefulCanvas_setOverflow(_1,_2,_3,_4,_5){if(this.autoFit&&this.$87&&!this.$88&&_1!=isc.Canvas.VISIBLE){this.$89=_1;this.setAutoFit(false);return}
this.invokeSuper(isc.StatefulCanvas,"setOverflow",_1,_2,_3,_4,_5);if(isc.isA.Canvas(this.label))this.label.setOverflow(_1,_2,_3,_4,_5)}
,isc.A.$ym=function isc_StatefulCanvas__resized(_1,_2,_3,_4,_5){this.invokeSuper(isc.StatefulCanvas,this.$8z,_1,_2,_3,_4,_5);if(this.label)this.label.resizeTo(this.$83(),this.$84())}
,isc.A.draw=function isc_StatefulCanvas_draw(_1,_2,_3){if(isc.$dd)arguments.$de=this;var _4=isc.Canvas.$ce.draw.call(this,_1,_2,_3);if(this.position!=isc.Canvas.ABSOLUTE&&isc.isA.Canvas(this.label)){if(isc.Page.isLoaded())this.$9e();else isc.Page.setEvent("load",this.getID()+".$9e()")}
if(this.label!=null&&isc.Canvas.ariaEnabled()){this.setAriaState("label",this.title)}
return _4}
,isc.A.$9e=function isc_StatefulCanvas__positionLabel(){if(!this.isDrawn())return;this.label.moveTo(this.$85(),this.$86())}
,isc.A.setAlign=function isc_StatefulCanvas_setAlign(_1){this.align=_1;if(this.isDrawn())this.markForRedraw();if(this.label)this.label.setAlign(_1)}
,isc.A.setVAlign=function isc_StatefulCanvas_setVAlign(_1){this.valign=_1;if(this.isDrawn())this.markForRedraw();if(this.label)this.label.setVAlign(_1)}
,isc.A.getPrintHTML=function isc_StatefulCanvas_getPrintHTML(){var _1=this.shouldShowLabel();if(_1){if(this.label==null){this.makeLabel()}
return this.label.getPrintHTML()}
return this.Super("getPrintHTML",arguments)}
,isc.A.shouldHiliteAccessKey=function isc_StatefulCanvas_shouldHiliteAccessKey(){return this.hiliteAccessKey}
,isc.A.getTitleHTML=function isc_StatefulCanvas_getTitleHTML(){var _1=this.getTitle();if(!this.shouldHiliteAccessKey()||!isc.isA.String(_1)||this.accessKey==null)
return _1;return isc.Canvas.hiliteCharacter(_1,this.accessKey)}
,isc.A.getTitle=function isc_StatefulCanvas_getTitle(){return this.title}
,isc.A.setTitle=function isc_StatefulCanvas_setTitle(_1){this.title=_1;var _1=this.getTitleHTML();if(this.label){this.label.setContents(_1);this.label.setState(this.getState());this.label.setSelected(this.isSelected())}else if(this.title!=null&&this.shouldShowLabel()){this.makeLabel()}
this.markForRedraw()}
,isc.A.setZIndex=function isc_StatefulCanvas_setZIndex(_1,_2,_3){isc.Canvas.$ce.setZIndex.call(this,_1,_2,_3);if(isc.isA.Canvas(this.label))this.label.moveAbove(this)}
,isc.A.$zn=function isc_StatefulCanvas__updateCanFocus(){this.Super("$zn",arguments);if(this.label!=null)this.label.$zn()}
,isc.A.setIcon=function isc_StatefulCanvas_setIcon(_1){this.icon=_1;if(this.label)this.label.setIcon(_1);else if(_1&&this.shouldShowLabel())this.makeLabel()}
,isc.A.mouseOver=function isc_StatefulCanvas_mouseOver(){if(this.showDown&&this.ns.EH.mouseIsDown()){this.setState(isc.StatefulCanvas.STATE_DOWN)}else{if(this.showRollOver){this.setState(isc.StatefulCanvas.STATE_OVER)}
if(this.showOverCanvas){if(!this.overCanvas){this.addAutoChild("overCanvas",{autoDraw:false})}
if(!this.overCanvas.isDrawn())this.overCanvas.draw()}}}
,isc.A.mouseOut=function isc_StatefulCanvas_mouseOut(){if(this.showRollOver){this.setState(isc.StatefulCanvas.STATE_UP)}else if(this.showDown&&this.ns.EH.mouseIsDown()){this.setState(isc.StatefulCanvas.STATE_UP)}
if(this.showOverCanvas&&this.overCanvas&&this.overCanvas.isVisible()&&(isc.EH.getTarget()!=this.overCanvas))
{this.overCanvas.clear()}}
,isc.A.$nb=function isc_StatefulCanvas__focusChanged(_1,_2,_3,_4){var _5=this.invokeSuper(isc.StatefulCanvas,"$nb",_1,_2,_3,_4);if(!(_1&&isc.Browser.isIE&&(this.getFocusHandle()!=this.getDocument().activeElement)))
{this.updateStateForFocus(_1)}
return _5}
,isc.A.updateStateForFocus=function isc_StatefulCanvas_updateStateForFocus(_1){if(!this.showFocused)return;if(this.showFocusedAsOver){if(!this.showRollOver)return;var _2=this.getState();if(_1&&!this.isDisabled()){if(_2==isc.StatefulCanvas.STATE_UP)this.setState(isc.StatefulCanvas.STATE_OVER)}else{if(_2==isc.StatefulCanvas.STATE_OVER)this.setState(isc.StatefulCanvas.STATE_UP)}}else{this.stateChanged();if(this.label)this.label.stateChanged()}}
,isc.A.getFocusedState=function isc_StatefulCanvas_getFocusedState(){if(!this.showFocused||this.showFocusedAsOver||this.isDisabled())return false;return this.hasFocus}
,isc.A.handleMouseDown=function isc_StatefulCanvas_handleMouseDown(_1,_2){if(_1.target==this&&this.useEventParts){if(this.firePartEvent(_1,isc.EH.MOUSE_DOWN)==false)return false}
if(this.showDown)this.setState(isc.StatefulCanvas.STATE_DOWN);if(this.mouseDown)return this.mouseDown(_1,_2)}
,isc.A.handleMouseUp=function isc_StatefulCanvas_handleMouseUp(_1,_2){if(_1.target==this&&this.useEventParts){if(this.firePartEvent(_1,isc.EH.MOUSE_UP)==false)return false}
if(this.showDown){this.setState(this.showRollOver?isc.StatefulCanvas.STATE_OVER:isc.StatefulCanvas.STATE_UP)}
if(this.mouseUp)return this.mouseUp(_1,_2)}
,isc.A.handleActivate=function isc_StatefulCanvas_handleActivate(_1,_2){var _3=this.getActionType();if(_3==isc.StatefulCanvas.RADIO){this.select()}else if(_3==isc.StatefulCanvas.CHECKBOX){this.setSelected(!this.isSelected())}
if(this.activate)return this.activate(_1,_2);if(this.action)return this.action();if(this.click)return this.click(_1,_2)}
,isc.A.handleClick=function isc_StatefulCanvas_handleClick(_1,_2){if(isc.$dd)arguments.$de=this;if(_1.target==this&&this.useEventParts){if(this.firePartEvent(_1,isc.EH.CLICK)==false)return false}
return this.handleActivate(_1,_2)}
,isc.A.handleKeyPress=function isc_StatefulCanvas_handleKeyPress(_1,_2){if(isc.$dd)arguments.$de=this;if(this.keyPress&&(this.keyPress(_1,_2)==false))return false;if(_1.keyName=="Space"||_1.keyName=="Enter"){if(this.handleActivate(_1,_2)==false)return false}
return true}
,isc.A.destroy=function isc_StatefulCanvas_destroy(){this.removeFromRadioGroup();return this.Super("destroy",arguments)}
);isc.B._maxIndex=isc.C+68;isc.StatefulCanvas.registerStringMethods({activate:isc.EH.$ki,action:""});isc.ClassFactory.defineClass("Layout","Canvas");isc.A=isc.Layout;isc.A.FILL="fill";isc.A=isc.Layout.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.orientation="horizontal";isc.A.vPolicy=isc.Layout.FILL;isc.A.hPolicy=isc.Layout.FILL;isc.A.minMemberSize=1;isc.A.enforcePolicy=true;isc.A.paddingAsLayoutMargin=true;isc.A.$wo=true;isc.A.membersMargin=0;isc.A.defaultResizeBars="marked";isc.A.resizeBarClass="Splitbar";isc.A.resizeBarSize=7;isc.A.animateMemberEffect="slide";isc.A.canDropComponents=true;isc.A.dropLineThickness=2;isc.A.membersAreChildren=true;isc.B.push(isc.A.setDefaultResizeBars=function isc_Layout_setDefaultResizeBars(_1){if(this.defaultResizeBars==_1)return;this.defaultResizeBars=_1;this.$9f()}
);isc.B._maxIndex=isc.C+1;isc.A=isc.Canvas.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.setShowResizeBar=function isc_Canvas_setShowResizeBar(_1){if(this.showResizeBar==_1)return;this.showResizeBar=_1;var _2=this.parentElement;if(_2==null||!isc.isA.Layout(_2))return;_2.$9f()}
);isc.B._maxIndex=isc.C+1;isc.A=isc.Layout.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.managePercentBreadth=true;isc.A.$9g="layout";isc.A.$9h=["show","hide","rect"];isc.A.$9i=["rect","move"];isc.A.$9j=[];isc.A.$9k="membersAdded";isc.A.$9l="membersRemoved";isc.A.placeHolderDefaults={styleName:"layoutPlaceHolder",overflow:isc.Canvas.HIDDEN};isc.A.dropLineDefaults={styleName:"layoutDropLine",overflow:"hidden",isMouseTransparent:true};isc.B.push(isc.A.getMemberLength=function isc_Layout_getMemberLength(_1){return this.vertical?_1.getVisibleHeight():_1.getVisibleWidth()}
,isc.A.getMemberBreadth=function isc_Layout_getMemberBreadth(_1){return this.vertical?_1.getVisibleWidth():_1.getVisibleHeight()}
,isc.A.setMemberBreadth=function isc_Layout_setMemberBreadth(_1,_2){if(this.logIsDebugEnabled(this.$9g))this.$9m(_1,_2);this.vertical?_1.setWidth(_2):_1.setHeight(_2)}
,isc.A.getLength=function isc_Layout_getLength(){if(this.vertical)return this.getInnerHeight();var _1=this.getInnerWidth();if(this.leaveScrollbarGap&&!this.vscrollOn)_1-=this.getScrollbarSize();return _1}
,isc.A.getBreadth=function isc_Layout_getBreadth(){if(!this.vertical)return this.getInnerHeight();var _1=this.getInnerWidth();if(this.leaveScrollbarGap&&!this.vscrollOn)_1-=this.getScrollbarSize();return _1}
,isc.A.getLengthPolicy=function isc_Layout_getLengthPolicy(){return this.vertical?this.vPolicy:this.hPolicy}
,isc.A.getBreadthPolicy=function isc_Layout_getBreadthPolicy(){return this.vertical?this.hPolicy:this.vPolicy}
,isc.A.memberHasInherentLength=function isc_Layout_memberHasInherentLength(_1){if(!(this.vertical?_1.hasInherentHeight():_1.hasInherentWidth())){return false}
var _2=this.$9n(_1);if(isc.isA.String(_2)&&(_2.endsWith(this.$ta)||_2==this.$tb))
{return false}
return true}
,isc.A.memberHasInherentBreadth=function isc_Layout_memberHasInherentBreadth(_1){return(this.vertical?_1.hasInherentWidth():_1.hasInherentHeight())}
,isc.A.$9o=function isc_Layout__overflowsLength(_1){return((this.vertical&&_1.canOverflowHeight())||(!this.vertical&&_1.canOverflowWidth()))}
,isc.A.$9n=function isc_Layout__explicitLength(_1){return this.vertical?_1.$tt:_1.$ts}
,isc.A.$9p=function isc_Layout__explicitBreadth(_1){return this.vertical?_1.$ts:_1.$tt}
,isc.A.$9q=function isc_Layout__memberPercentLength(_1){return this.vertical?_1._percent_height:_1._percent_width}
,isc.A.scrollingOnLength=function isc_Layout_scrollingOnLength(){return this.vertical?this.vscrollOn:this.hscrollOn}
,isc.A.getMemberGap=function isc_Layout_getMemberGap(_1){return(_1.extraSpace||0)-(this.memberOverlap||0)+(_1.$9r||0)}
,isc.A.initWidget=function isc_Layout_initWidget(){if(isc.$dd)arguments.$de=this;var _1=isc.Layout;if(this.vertical==null){this.vertical=(this.orientation==_1.VERTICAL)}else{this.orientation=(this.vertical?_1.VERTICAL:_1.HORIZONTAL)}
if(this.isRTL()&&!this.vertical)this.reverseOrder=!this.reverseOrder;if(this.members==null)this.members=[];else if(!isc.isA.Array(this.members))this.members=[this.members];if(this.membersAreChildren){if(this.members.length==0&&this.children!=null&&!this.$9s())
{this.members=this.children=this.createMemberCanvii(this.children)}else{this.members=this.createMemberCanvii(this.members);if(this.children==null)this.children=[];this.children.addList(this.members)}}else{this.logInfo("members are peers","layout");this.addMethods({draw:this.$9t});this.members=this.createMemberCanvii(this.members);if(this.peers==null)this.peers=[];this.peers.addList(this.members)}
this.setLayoutMargin();if(this.members&&this.members.length>0)this.$9u()}
,isc.A.createMemberCanvii=function isc_Layout_createMemberCanvii(_1){_1=this.createCanvii(_1);for(var i=_1.length-1;i>=0;i--){if(_1[i]==null)continue;if(!isc.isA.Canvas(_1[i])){this.logWarn("Layout unable to resolve member:"+this.echo(_1[i])+" to a Canvas - ignoring this member");_1.removeAt(i)}}
return _1}
,isc.A.$9s=function isc_Layout__allGeneratedChildren(){for(var i=0;i<this.children.length;i++){var _2=this.children[i];if(_2!=null&&!_2._generated)return false}
return true}
,isc.A.setLayoutMargin=function isc_Layout_setLayoutMargin(_1){if(_1!=null)this.layoutMargin=_1;var _2=this.layoutHMargin,_3=this.layoutVMargin,_4=this.layoutMargin,_5=this.reverseOrder?this.layoutEndMargin:this.layoutStartMargin,_6=this.reverseOrder?this.layoutStartMargin:this.layoutEndMargin;var _7,_8,_9,_10;if(this.paddingAsLayoutMargin){var _11=this.$q6();_7=_11.left;_8=_11.right;_9=_11.top;_10=_11.bottom}
this.$xw=this.$ef(this.layoutLeftMargin,(!this.vertical?_5:null),_2,_4,_7,0);this.$xx=this.$ef(this.layoutRightMargin,(!this.vertical?_6:null),_2,_4,_8,0);this.$xy=this.$ef(this.layoutTopMargin,(this.vertical?_5:null),_3,_4,_9,0);this.$xz=this.$ef(this.layoutBottomMargin,(this.vertical?_6:null),_3,_4,_10,0);this.$9v=true;this.reflow()}
,isc.A.$9w=function isc_Layout__getSideMargin(_1){if(this.$xw==null)this.setLayoutMargin();if(_1)return this.$xw+this.$xx;else return this.$xy+this.$xz}
,isc.A.$9x=function isc_Layout__getBreadthMargin(){return this.$9w(this.vertical)}
,isc.A.$9y=function isc_Layout__getLengthMargin(){return this.$9w(!this.vertical)}
,isc.A.$9t=function isc_Layout__drawOverride(){if(isc.$dd)arguments.$de=this;if(!this.membersAreChildren){this.$9z();this.layoutChildren(this.$se);this.drawPeers();this.$t1=true;return}
isc.Canvas.$ce.draw.apply(this,arguments)}
,isc.A.resizePeersBy=function isc_Layout_resizePeersBy(_1,_2,_3){if(!this.membersAreChildren)return;isc.Canvas.$ce.resizePeersBy.call(this,_1,_2,_3)}
,isc.A.markForRedraw=function isc_Layout_markForRedraw(){if(this.membersAreChildren)return this.Super("markForRedraw",arguments);this.reflow("markedForRedraw")}
,isc.A.drawChildren=function isc_Layout_drawChildren(){if(this.membersAreChildren){this.$9z();this.layoutChildren(this.$se);this.$90()}
return}
,isc.A.$91=function isc_Layout__memberCanFocus(_1){return true}
,isc.A.$9z=function isc_Layout__setupMembers(){if(!this.members)return;for(var i=0;i<this.members.length;i++){var _2=this.members[i];if(_2==null){this.logWarn("members array: "+this.members+" includes null entry at position "+i+". Removing");this.members.removeAt(i);i-=1;continue}
if(this.$91(_2)&&(_2.$xb||_2.tabIndex==null))
{this.updateMemberTabIndex(_2)}
this.autoSetBreadth(_2)}}
,isc.A.childCanFocusChanged=function isc_Layout_childCanFocusChanged(_1){if(!this.members.contains(_1))return;this.updateMemberTabIndex(_1)}
,isc.A.$90=function isc_Layout__drawNonMemberChildren(){if(!this.membersAreChildren||!this.children)return;for(var i=0;i<this.children.length;i++){var _2=this.children[i];if(this.members.contains(_2))continue;if(!isc.isA.Canvas(_2)){_2.autoDraw=false;_2=isc.Canvas.create(_2)}
if(!_2.isDrawn())_2.draw()}}
,isc.A.$92=function isc_Layout__getMemberDefaultBreadth(_1){var _2=this.$9p(_1),_3=isc.isA.String(_2)&&isc.endsWith(_2,this.$ta)?_2:null,_4=Math.max(this.getBreadth()-this.$9x(),1);if(this.$93&&!this.leaveScrollbarGap){_4-=this.getScrollbarSize()}
var _5=(_3==null?_4:Math.floor(_4*(parseInt(_3)/100)));if(this.getMemberDefaultBreadth==null)return _5;return this.getMemberDefaultBreadth(_1,_5)}
,isc.A.autoSetBreadth=function isc_Layout_autoSetBreadth(_1){if(!this.shouldAlterBreadth(_1))return false;var _2=this.$94;this.$94=true;this.setMemberBreadth(_1,this.$92(_1));this.$94=_2;return true}
,isc.A.shouldAlterBreadth=function isc_Layout_shouldAlterBreadth(_1){var _2=this.$9p(_1);if(_2!=null){return(this.managePercentBreadth&&this.getBreadthPolicy()==isc.Layout.FILL&&isc.isA.String(_2)&&isc.endsWith(_2,this.$ta))}
if(this.vertical&&_1.inherentWidth)return false;if(this.getBreadthPolicy()==isc.Layout.FILL)return true;return false}
,isc.A.$95=function isc_Layout__moveOffscreen(_1){return isc.Canvas.moveOffscreen(_1)}
,isc.A.getMarginSpace=function isc_Layout_getMarginSpace(){var _1=this.$9y();for(var i=0;i<this.members.length;i++){var _3=this.members[i];if(_3.$96){_1+=this.resizeBarSize}else if(i<this.members.length-1&&!this.$97(this.members[i+1])){_1+=this.membersMargin}
_1+=this.getMemberGap(_3)}
if(this.members.length!=0&&this.$97(this.members[0])){_1-=this.membersMargin}
if(this.memberOverlap!=null)_1+=this.memberOverlap
return _1}
,isc.A.getTotalMemberSpace=function isc_Layout_getTotalMemberSpace(){return this.getLength()-this.getMarginSpace()}
,isc.A.$98=function isc_Layout__getTotalMemberLength(){var _1=0;for(var i=0;i<this.members.length;i++){var _3=this.members[i];if(this.$97(_3))continue;_1+=this.getMemberLength(_3)}
return _1+this.getMarginSpace()}
,isc.A.ignoreMember=function isc_Layout_ignoreMember(_1){if(!_1||!this.members||this.members.indexOf(_1)==-1)return;_1.$99=true}
,isc.A.stopIgnoringMember=function isc_Layout_stopIgnoringMember(_1){_1.$99=false;this.reflow()}
,isc.A.isIgnoringMember=function isc_Layout_isIgnoringMember(_1){if(_1.$99)
return _1.$99;return false}
,isc.A.$97=function isc_Layout__shouldIgnoreMember(_1){if(_1.visibility==isc.Canvas.HIDDEN&&!(_1.$nw&&_1.$nw.isVisible()))return true;if(this.isIgnoringMember(_1))return true;return false}
,isc.A.ignoreMemberZIndex=function isc_Layout_ignoreMemberZIndex(_1){if(!_1||!this.members||this.members.indexOf(_1)==-1)return;_1.$10a=true;this.reflow()}
,isc.A.stopIgnoringMemberZIndex=function isc_Layout_stopIgnoringMemberZIndex(_1){_1.$10a=false;this.reflow()}
,isc.A.$10b=function isc_Layout__isIgnoringMemberZIndex(_1){if(this.isIgnoringMember(_1))
return true;else if(_1.$10a)
return _1.$10a;return false}
,isc.A.gatherSizes=function isc_Layout_gatherSizes(_1,_2,_3){if(!_2){_2=this.$10c;if(_2==null){_2=this.$10c=[]}else{_2.length=0}}
var _4=this.getLengthPolicy();var _5=this.logIsInfoEnabled(this.$9g);for(var i=0;i<this.members.length;i++){var _7=this.members[i];var _8=_2[i];if(_8==null){_8=_2[i]={}}
if(this.$97(_7)&&!_7.$10d){_8.$10e=0;if(_5)_8.$10f="hidden";continue}
if(this.memberHasInherentLength(_7)||_4==isc.Layout.NONE){_8.$10e=this.getMemberLength(_7);if(_5){_8.$10f=(_4==isc.Layout.NONE?"no length policy":"inherent size")}
continue}
if(_1&&this.$9o(_7)){var _9=this.getMemberLength(_7);if(_9!=_3[i]){if(_5){this.logInfo("member: "+_7+" overflowed.  set length: "+_3[i]+" got length: "+_9,"layout")}
_8.$10g=true;_8.$10e=_9}
continue}
if(this.$9n(_7)!=null){_8.$10e=this.vertical?_7.$tt:_7.$ts;if(_5)_8.$10f="explicit size";continue}
if(this.respectSizeLimits){var _10=this.vertical?_7.minHeight:_7.minWidth,_11=this.vertical?_7.maxHeight:_7.maxWidth;if(_10!=null&&_3[i]!=null&&_10>_3[i]){_8.$10e=_10;if(_5)_8.$10f="minimum size";continue}
if(_11!=null&&_3[i]!=null&&_11<_3[i]){_8.$10e=_11;if(_5)_8.$10f="maximum size";continue}}
if(_8.$10e==null){_8.$10e=this.$tb;if(_5)_8.$10f="no length specified"}}
return _2}
,isc.A.resizeMembers=function isc_Layout_resizeMembers(_1,_2,_3){var _4=this.logIsInfoEnabled(this.$9g);for(var i=0;i<this.members.length;i++){var _6=this.members[i],_7=_2[i];if(this.$97(_6))continue;if(_3&&!this.$9o(_6))continue;var _8=null;if(this.shouldAlterBreadth(_6)){if(_4)
_7.$10h="breadth policy: "+this.getBreadthPolicy();_8=_7.$10i=this.$92(_6)}else{_7.$10i=this.getMemberBreadth(_6);if(_4){_7.$10h=(this.getBreadthPolicy()==isc.Layout.NONE?"no breadth policy":"explicit size")}}
var _9=null;if(this.getLengthPolicy()!=isc.Layout.NONE&&(!this.memberHasInherentLength(_6)&&!_7.$10g))
{_9=_7.$10j=_1[i]}
if(_9!=null&&this.$9o(_6)&&!_6.isDirty()){var _10=(this.vertical?_6.getHeight():_6.getWidth()),_11=this.getMemberLength(_6);if(_11>_10&&_9<=_11&&(_8==null||_8<=this.getMemberBreadth(_6)))
{if(_4)this.logInfo("not applying "+this.getLengthAxis()+": "+_9+" to overflowed member: "+_6+" w/"+this.getLengthAxis()+": "+_11,"layout");_9=null}}
if(this.logIsDebugEnabled(this.$9g))this.$9m(_6,_8,_9);if(!_6.isAnimating(this.$9h)){if(this.vertical){_6.resizeTo(_8,_9)}else{_6.resizeTo(_9,_8)}}
if(_6.isDrawn()){if(_6.isDirty())_6.redraw("Layout getting new size")}else{if(!_6.isDrawn())_6.$10k=true}}}
,isc.A.$10l=function isc_Layout__enforceStackZIndex(){if(!this.stackZIndex||this.members.length<2)return;for(var _1=0;_1<this.members.length;_1++)
if(!this.$10b(this.members[_1]))break;var _2=this.members[_1],_3=_2.getZIndex();var _4,_5;for(var i=_1+1;i<this.members.length;i++){if(this.$10b(this.members[i]))continue;_4=_2;_5=_4.getZIndex();_2=this.members[i];_3=_2.getZIndex();if((_3<=_5)&&this.stackZIndex=="lastOnTop")
_2.moveAbove(_4);else if((_3>=_5)&&this.stackZIndex=="firstOnTop")
_2.moveBelow(_4)}}
,isc.A.stackMembers=function isc_Layout_stackMembers(_1,_2,_3){if(_3==null)_3=true;var _4=(this.membersAreChildren?0:this.getOffsetLeft()),_5=(this.membersAreChildren?0:this.getOffsetTop()),_6=this.reverseOrder,_7=(_6?-1:1);var _8=(this.vertical?this.getInnerWidth():this.getInnerHeight())
-this.$9x();if((this.vertical&&this.canOverflowWidth())||(!this.vertical&&this.canOverflowHeight()))
{for(var i=0;i<this.members.length;i++){var _10=this.members[i];if(this.$97(_10))continue;var _11=this.getMemberBreadth(_10);if(_11>_8)_8=_11}}
if(this.logIsDebugEnabled(this.$9g)){this.logDebug("centering wrt visible breadth: "+_8,this.$9g)}
var _12;if(_6){if(this.isRTL()&&!this.vertical){_12=this.getLength()}else{_12=Math.max(this.getLength(),this.$98())}}
var _13=(this.vertical?(!_6?_5:_5+_12):(!_6?_4:_4+_12));if(this.align!=null){var _14=this.$98(),_15=Math.max(this.getLength(),_14),_16=_15-_14;if(((!_6&&(this.align==isc.Canvas.BOTTOM||this.align==isc.Canvas.RIGHT))||(_6&&(this.align==isc.Canvas.LEFT||this.align==isc.Canvas.TOP))))
{_13+=(_7*_16)}else if(this.align==isc.Canvas.CENTER){_13+=Math.round(_16/ 2)}}
var _17=(this.vertical?_4+this.$xw:_5+this.$xy),_18=false,_19=false,_20=0;for(var i=0;i<_1.length;i++){var _10=_1[i],_21=_2?_2[i]:null;if(i==0){var _22;if(this.vertical)_22=(_6?this.$xz:this.$xy);else _22=(_6?this.$xx:this.$xw);_13+=(_7*_22)}else{if(_18){_13+=(_7*this.resizeBarSize)}else if(!_19){_13+=(_7*this.membersMargin)}}
var _23=_10.isAnimating(this.$9i);if(this.$97(_10)){if(!this.isIgnoringMember(_10)&&!_23){_10.moveTo(_4+this.$xw,_5+this.$xy)}
if(_10.$96){var _24=this.getBreadth()-this.$9x();this.makeResizeBar(_10,_17,_13,_24);_18=true}else{if(_10.$10m!=null)_10.$10m.hide();_18=false}
_19=true;_20++;continue}else{_19=false}
var _25=_17,_26=isc.Canvas,_27=this.getLayoutAlign(_10);if(_27==_26.RIGHT||_27==_26.BOTTOM){_25=_8-this.getMemberBreadth(_10)+(this.vertical?this.$xw:this.$xy)}else if(_27==_26.CENTER){_25=Math.floor((_8-this.getMemberBreadth(_10))/2)+(this.vertical?this.$xw:this.$xy)}
if(this.getMemberOffset!=null)
_25=this.getMemberOffset(_10,_25,_27);var _28=this.getMemberLength(_10);if(!_23){if(this.vertical){if(!_6)_10.moveTo(_25,_13);else _10.moveTo(_25,_13-_28)}else{if(!_6)_10.moveTo(_13,_25);else _10.moveTo(_13-_28,_25)}}
_13+=(_7*_28);_13+=(_7*this.getMemberGap(_10));if(_10.$96){var _24=this.getBreadth()-this.$9x();this.makeResizeBar(_10,_17,_13,_24)}else{if(_10.$10m!=null)_10.$10m.hide()}
_18=_10.$96;if(_3)this.memberSizes[i-_20]=_28;if(_2)_21.$10n=_28}
if(_3)this.memberSizes.length=(i-_20);if(this.overflow!=isc.Canvas.VISIBLE)this.$10o();this.$10l()}
,isc.A.getLayoutAlign=function isc_Layout_getLayoutAlign(_1){if(_1.layoutAlign!=null)return _1.layoutAlign;if(this.defaultLayoutAlign!=null)return this.defaultLayoutAlign;return this.vertical?(this.isRTL()?isc.Canvas.RIGHT:isc.Canvas.LEFT):isc.Canvas.TOP}
,isc.A.$10o=function isc_Layout__enforceScrollSize(){var _1,_2,_3=false,_4=false,_5,_6,_7,_8,_9=this.vertical;if(_9){_2=this.$xz||0;_1=this.$xx||0}else{_2=this.$xx||0;_1=this.$xz||0}
if(_2>0||_1>0)_3=true;var _10=this.getInnerWidth(),_11=this.getInnerHeight();if(_3){for(var i=this.members.length-1;i>=0;i--){_6=this.members[i];if(!_6.isVisible())continue;if(_9){if(_5==null){_5=_6;_7=_6.getTop()+_6.getVisibleHeight()}
var _13=_6.getLeft()+_6.getVisibleWidth();if(_8==null||_8<_13)_8=_13}else{if(_5==null){_5=_6;_8=_6.getLeft()+_6.getVisibleWidth()}
var _14=_6.getTop()+_6.getVisibleHeight();if(_7==null||_7<_14)_7=_14}}
if(_7==null)_7=0;if(_8==null)_8=0}else{var _15=false;for(var i=this.members.length-1;i>=0;i--){var _6=this.members[i];if(isc.isA.LayoutSpacer(_6)&&_6.isVisible()){var _16=_6.getWidth(),_17=_6.getHeight();if(i==this.members.length-1){_4=true;if(_9)_7=_6.getTop()+_17;else _8=_6.getLeft()+_16}
if(_9){if(_16>_10&&(_8==null||_16>_8)){_15=true;_8=_16}}else if(_17>_11&&(_7==null||_17>_7)){_15=true;_7=_17}}}
if(_15&&!_4){for(var i=this.members.length-1;i>=0;i--){var _6=this.members[i];if(isc.isA.LayoutSpacer(_6))continue;if(this.vertical){var _16=_6.getVisibleWidth();if(_16>=_8){_15=false;break}}else{var _17=_6.getVisibleHeight();if(_17>=_7){_15=false;break}}}
if(_15)_4=true}
if(_4){if(_8==null)_8=1;if(_7==null)_7=1}}
if(_4||_3){if(this.vertical){_8+=_1;_7+=_2}else{_8+=_2;_7+=_1}
this.enforceScrollSize(_8,_7)}
else this.stopEnforcingScrollSize()}
,isc.A.setOverflow=function isc_Layout_setOverflow(_1,_2,_3,_4,_5){var _6=this.overflow;if(_6==isc.Canvas.VISIBLE&&_1!=isc.Canvas.VISIBLE){this.$10o()}else if(_6!=isc.Canvas.VISIBLE&&_1==isc.Canvas.VISIBLE){this.stopEnforcingScrollSize()}
return this.invokeSuper(isc.Layout,"setOverflow",_1,_2,_3,_4,_5)}
,isc.A.layoutChildren=function isc_Layout_layoutChildren(_1,_2,_3){if(isc.$dd)arguments.$de=this;if(this.destroying)return;if(this.$10p==null)this.$10p=1;else this.$10p++;if(!this.members)this.members=[];if(this.children&&this.children.length){for(var i=0;i<this.children.length;i++){this.$10q(this.children[i])}}
if(!this.isDrawn()&&_1!=this.$se)return;this.$94=true;if(_2!=null||_3!=null){if((this.vertical&&isc.isA.Number(_2))||(!this.vertical&&isc.isA.Number(_3)))
{this.$9v=true}}
if(this.isDrawn()&&this.getLengthPolicy()==isc.Layout.NONE&&!this.$9v){if(this.logIsInfoEnabled(this.$9g)){this.logInfo("Restacking, reason: "+_1,this.$9g)}
this.stackMembers(this.members);this.$9v=false;this.$10r(_1);return}
this.$9v=false;var _5=this.getTotalMemberSpace();var _6=this.$10s(_5),_7=this.$10c;if(!this.scrollingOnLength()&&this.overflow==isc.Canvas.AUTO&&_6.sum()>this.getLength())
{this.logInfo("scrolling will be required on length axis",this.$9g);this.$93=true}
this.resizeMembers(_6,_7,true);if(this.manageChildOverflow)this.$u6=true;for(var i=0;i<this.members.length;i++){var _8=this.members[i],_9=this.$91(_8);if(_8.$10k){this.$95(_8);_8.draw();_8.$10k=null;if(!_9&&this.$91(_8)){this.updateMemberTabIndex(_8)}}}
if(this.manageChildOverflow)this.$u7(this.members);var _10=this.memberSizes=this.$10s(_5,true,_6,_7);if(!this.$93&&!this.scrollingOnLength()&&this.overflow==isc.Canvas.AUTO&&_10.sum()>this.getLength())
{this.logInfo("scrolling will be required on length axis, after overflow",this.$9g);this.$93=true}
this.resizeMembers(_10,_7,false);if(this.manageChildOverflow)this.$u6=true;for(var i=0;i<this.members.length;i++){var _8=this.members[i];if(_8.$10k){this.$95(_8);_8.draw();_8.$10k=null}}
if(this.manageChildOverflow)this.$u7(this.members);this.stackMembers(this.members,_7);this.reportSizes(_7,_1);this.$10r(_1)}
,isc.A.$10q=function isc_Layout__resolvePercentageSizeForChild(_1){var _2=_1._percent_height,_3=_1._percent_width;if(_1.snapTo){_1.$vb();return}
var _4=(this.getLengthPolicy()==isc.Layout.FILL);if(!(_1._percent_left||_1._percent_top||_2||_3))return;if(_4&&this.vertical){if(_2!=null&&this.members.contains(_1))_2=null}else if(_4&&!this.vertical){if(_3!=null&&this.members.contains(_1))_3=null}
_1.setRect(_1._percent_left,_1._percent_top,_3,_2)}
,isc.A.$10s=function isc_Layout__getMemberSizes(_1,_2,_3,_4){if(!_3){_3=this.$10t;if(_3==null)_3=this.$10t=[];else _3.length=this.members.length}
_4=this.gatherSizes(_2,_4,_3);this.$10u(_3,_4);return this.getClass().applyStretchResizePolicy(_3,_1,this.minMemberSize,true,this)}
,isc.A.$10r=function isc_Layout__layoutChildrenDone(_1){this.$93=false;this.$10v=false;this.$94=false;if(this.$v3&&this.isDrawn()&&!this.$zs&&(_1!="resized"||this.shouldRedrawOnResize()))
{this.adjustOverflow()}
if(!this.enforcePolicy){this.vertical?this.vPolicy=isc.Layout.NONE:this.hPolicy=isc.Layout.NONE}}
,isc.A.$10u=function isc_Layout__getPolicyLengths(_1,_2){for(var i=0;i<_2.length;i++){_1[i]=_2[i].$10e}}
,isc.A.getMemberSizes=function isc_Layout_getMemberSizes(){if(this.memberSizes)return this.memberSizes.duplicate();return this.memberSizes}
,isc.A.getScrollWidth=function isc_Layout_getScrollWidth(_1){if(isc.$dd)arguments.$de=this;if(this.$u9){this.$u9=null;this.adjustOverflow("widthCheckWhileDeferred")}
if(!_1&&this.$xg!=null)return this.$xg;var _2=this.children?this.$xi(this.children,true):0,_3=this.members?this.$xi(this.members,true):0,_4=Math.max(_2,_3+this.$xx);return(this.$xg=_4)}
,isc.A.getScrollHeight=function isc_Layout_getScrollHeight(_1){if(isc.$dd)arguments.$de=this;if(this.$u9){this.$u9=null;this.adjustOverflow("heightCheckWhileDeferred")}
if(!_1&&this.$xk!=null)return this.$xk;var _2=this.children?this.$xm(this.children,true):0,_3=this.children?this.$xm(this.members,true):0,_4=Math.max(_2,_3+this.$xz);return(this.$xk=_4)}
,isc.A.layoutIsDirty=function isc_Layout_layoutIsDirty(){return this.$10v==true}
,isc.A.reflow=function isc_Layout_reflow(_1){if(this.$10v)return;if(this.isDrawn()){this.$10v=true;if(this.instantRelayout){this.layoutChildren(_1)}else{var _2=this,_3=this.$10p;isc.EH.$or(function(){if(!_2.destroyed)_2.reflowNow(_1,_3)})}}}
,isc.A.reflowNow=function isc_Layout_reflowNow(_1,_2){if(_2!=null&&_2<this.$10p)return;this.layoutChildren(_1)}
,isc.A.childResized=function isc_Layout_childResized(_1,_2,_3,_4){if(isc.$dd)arguments.$de=this;if(this.suppressMemberAnimations){var _5=false;if(_1.isAnimating(this.$d9)){_5=true;_1.finishAnimation(this.$d9)}
if(_1.isAnimating(this.$x4)){_5=true;_1.finishAnimation(this.$x4)}
if(_1.isAnimating(this.$10w)){_5=true;_1.finishAnimation(this.$10w)}
if(_5)return}
this.$yg("child resize");if(this.$94)return;if(_1.$tv)return;if(!this.members.contains(_1))return;var _6=_1;if(_4!="overflow"&&_4!="overflow changed"){if(_2!=null&&_2!=0){var _7=_6.$ts;_6.$ts=_6._percent_width||_6.getWidth();this.$10x(_7,_6,_4,true)}
if(_3!=null&&_3!=0){var _8=_6.$tt;_6.$tt=_6._percent_height||_6.getHeight();this.$10x(_8,_6,_4)}}
var _9=isc.SB.concat("memberResized: (",_2,",",_3,"): ",_6.getID());if(_5)this.reflowNow(_9);else
this.reflow(_9)}
,isc.A.$10x=function isc_Layout__reportNewSize(_1,_2,_3,_4){if(!this.logIsDebugEnabled(this.$9g))return;var _5=_4?_2.$ts:_2.$tt;if(_5!=_1){this.logDebug("new user "+(_4?"width: ":"height: ")+_5+" for member "+_2+", oldSize: "+_1+" reason: "+_3+(this.logIsDebugEnabled("userSize")?this.getStackTrace():""),"layout")}}
,isc.A.childVisibilityChanged=function isc_Layout_childVisibilityChanged(_1,_2){if(!this.members.contains(_1))return;if(!_1.isDrawn())this.$9v=true;this.reflow("member changed visibility: "+_1);if(_1.$10m&&_1.$10m.showGrip&&_1.$10m.showClosedGrip){if(_1.$10m.label)_1.$10m.label.stateChanged()}
this.$yg("child visibility changed")}
,isc.A.pageResize=function isc_Layout_pageResize(){var _1=this.$10p;this.Super("pageResize",arguments);if(this.isDrawn()&&(this.$10p==null||_1==this.$10p))
{this.reflow("pageResize")}}
,isc.A.sectionHeaderClick=function isc_Layout_sectionHeaderClick(_1){var _2=_1.section;if(_2==null)return;if(!isc.isAn.Array(_2))_2=[_2];var _3=false;for(var i=0;i<_2.length;i++){if(isc.isA.String(_2[i]))_2[i]=window[_2[i]];if(_2[i].visibility!="hidden")_3=true}
if(_3){_2.map("hide");_1.setExpanded(false)}else{_2.map("show");_1.setExpanded(true)}}
,isc.A.getMember=function isc_Layout_getMember(_1){var _2=this.getMemberNumber(_1);if(_2==-1)return null;return this.members[_2]}
,isc.A.getMemberNumber=function isc_Layout_getMemberNumber(_1){if(isc.isA.String(_1)){_1=window[_1];return this.members.indexOf(_1)}else if(isc.isA.Canvas(_1)){return this.members.indexOf(_1)}
if(isc.isA.Number(_1))return _1;return-1}
,isc.A.hasMember=function isc_Layout_hasMember(_1){return this.members.contains(_1)}
,isc.A.getMembers=function isc_Layout_getMembers(_1){return this.members}
,isc.A.getPrintChildren=function isc_Layout_getPrintChildren(){var _1=this.members;if(!_1||_1.length==0)return;var _2=[];for(var i=0;i<_1.length;i++){if(this.shouldPrintChild(_1[i]))_2.add(_1[i])}
return(_2.length>0)?_2:null}
,isc.A.getChildPrintHTML=function isc_Layout_getChildPrintHTML(_1,_2,_3){if(!_2)_2={};if(_2.inline==null&&!this.vertical&&!this.printVertical){_2.inline=true}
return this.Super("getChildPrintHTML",[_1,_2,_3])}
,isc.A.getCompletePrintHTMLFunction=function isc_Layout_getCompletePrintHTMLFunction(_1,_2){var _3=this;return function(_5){_3.isPrinting=false;var _4=_3.vertical||_3.printVertical;if(isc.isAn.Array(_5)&&_5.length>0){if(_4)_5=_5.join(isc.emptyString);else{_5="<TABLE WIDTH=100%><TR><TD valign=top>"+_5.join("</TD><TD valign=top>")+"</TD></TR></TABLE>"}}
if(_5)_1[2]=_5;_1=_1.join(isc.emptyString);delete _3.currentPrintProperties;if(_2){_3.fireCallback(_2,"html, callback",[_1,_2]);return null}else{return _1}}}
,isc.A.addMember=function isc_Layout_addMember(_1,_2,_3){this.addMembers(_1,_2,_3);return this}
,isc.A.addMembers=function isc_Layout_addMembers(_1,_2,_3){if(!_1)return;if(isc.$dd)arguments.$de=this;this.$10y();if(this.logIsInfoEnabled(this.$9g)){this.logInfo("adding newMembers: "+_1+(_2!=null?" at position: "+_2:""),"layout")}
if(!isc.isAn.Array(_1)){this.$9j[0]=_1;_1=this.$9j}
if(this.members==null)this.members=[];if(_2>this.members.length)_2=this.members.length;var _4=this.isDrawn();for(var i=0;i<_1.length;i++){var _6=_1[i];if(!_6)continue;if(!isc.isAn.Instance(_6)){_6=this.createCanvas(_6)}
if(!isc.isA.Canvas(_6)){this.logWarn("addMembers() unable to resolve member:"+this.echo(_6)+" to a Canvas - ignoring");continue}
if(this.members.contains(_6)){if(_2!=null){this.members.slide(this.members.indexOf(_6),_2+i)}
continue}
if(_6.addAsPeer||_6.snapEdge){this.addPeer(_6,null,false);continue}else if(_6.addAsChild||_6.snapTo){this.addChild(_6,null,false);continue}
if(_6.parentElement)_6.deparent();if(_6.isDrawn())_6.clear();if(_2!=null){this.members.addAt(_6,_2+i)}else{this.members.add(_6)}
this.$10z(_6);this.autoSetBreadth(_6);var _7=_4&&this.animateMembers&&!_3&&_1.length==1&&_6.visibility!=isc.Canvas.HIDDEN;if(_7)_6.hide();var _8=(_4&&this.getLengthPolicy()==isc.Layout.NONE);if(this.membersAreChildren){this.addChild(_6,null,_8)}else{this.addPeer(_6,null,_8)}
_6.moveTo(0,0);if(this.isDrawn())this.updateMemberTabIndex(_6);if(this.isDrawn()&&this.memberHasInherentLength(_6)){this.$95(_6);if(!_6.isDrawn())_6.draw()}}
this.$9j[0]=null;if(_7){this.$100(_6)}else
this.reflow(this.$9k);this.$9u()}
,isc.A.$10z=function isc_Layout__getUserSizes(_1){if(_1._percent_height)_1.$tt=_1._percent_height;if(_1._percent_width)_1.$ts=_1._percent_width;if(this.memberHasInherentLength(_1)){if(!_1.$tt&&!_1.$yj){_1.restoreDefaultSize(true)}
if(!_1.$ts&&!_1.$yi){_1.restoreDefaultSize()}}}
,isc.A.$101=function isc_Layout__animateMargin(_1,_2){var _3=this;var _4=_1;var _5=this.getMemberNumber(_1);if(_5==this.members.length-1)_1=this.getMember(_5-1);if(!_1)return;var _6=this.membersMargin+this.getMemberGap(_1);if(_2)_1.$9r=-(_6+1);this.registerAnimation(function(_8){var _7=Math.floor(_8*_6);if(_2)_7=_6-_7;_1.$9r=-_7;if(_8==1)_1.$9r=null},this.animateMemberTime)}
,isc.A.removeChild=function isc_Layout_removeChild(_1,_2){isc.Canvas.$ce.removeChild.call(this,_1,_2);if(this.membersAreChildren&&this.members.contains(_1)){this.removeMember(_1)}}
,isc.A.removeMember=function isc_Layout_removeMember(_1,_2){this.removeMembers(_1,_2)}
,isc.A.removeMembers=function isc_Layout_removeMembers(_1,_2){if(!_1)return;this.$10y();if(!isc.isAn.Array(_1)){this.$9j[0]=_1;_1=this.$9j}
if(_1===this.members)_1=_1.duplicate();for(var i=0;i<_1.length;i++){var _4=_1[i];if(isc.isA.Canvas(_6))continue;_1[i]=this.getMember(_4);if(_1[i]==null){this.logWarn("couldn't find member to remove: "+this.echoLeaf(_4));_1.removeAt(i);i-=1}}
var _5=(this.animateMembers&&_1.length==1&&!_2),_6=(_5?_1[0]:null);if(_5){if(_6.parentElement!=this||_6.destroying||!_6.isVisible())
{_5=false}}
if(_5){var _7=this,_8=_1.duplicate(),_9=function(){_7.$102(_8)};this.$103(_6,_9)}else{this.$102(_1)}
this.$9j[0]=null;this.$9u()}
,isc.A.$102=function isc_Layout__completeRemoveMembers(_1){if(!_1)return;for(var i=0;i<_1.length;i++){var _3=_1[i];this.members.remove(_3);if(this.membersAreChildren&&_3.parentElement==this)_3.deparent();_3.$yj=_3.$yi=null;if(_3.$10m){_3.$10m.destroy();_3.$10m=null}
if(_3.showTarget==this)delete _3.showTarget;if(_3.$104)_3.destroy()}
this.reflow(this.$9l)}
,isc.A.setMembers=function isc_Layout_setMembers(_1){if(_1==this.members||!isc.isAn.Array(_1))return;var _2=[];for(var i=0;i<this.members.length;i++){if(!_1.contains(this.members[i]))_2.add(this.members[i])}
var _4=this.instantRelayout;this.instantRelayout=false;this.removeMembers(_2,true);this.addMembers(_1,0,true);this.instantRelayout=_4;if(_4)this.reflow("set members")}
,isc.A.showMember=function isc_Layout_showMember(_1,_2){return this.showMembers([_1],_2)}
,isc.A.showMembers=function isc_Layout_showMembers(_1,_2){if(this.isDrawn()&&this.animateMembers&&_1.length==1){this.$100(_1[0],_2)}else{for(var i=0;i<_1.length;i++){var _4=this.getMember(_1[i]);_4.show()}
if(_2)this.fireCallback(_2)}}
,isc.A.$100=function isc_Layout__animateMemberShow(_1,_2){_1=this.getMember(_1);this.setNewMemberLength(_1);_1.animateShow(this.animateMemberEffect,_2,this.animateMemberTime);if(_1.isAnimating())this.$101(_1,true)}
,isc.A.setNewMemberLength=function isc_Layout_setNewMemberLength(_1){_1.$10d=true;var _2=this.$10s(this.getTotalMemberSpace());delete _1.$10d;var _3=_2[this.members.indexOf(_1)];var _4=this.$94;this.$94=true;this.vertical?_1.setHeight(_3):_1.setWidth(_3);this.$94=_4}
,isc.A.hideMember=function isc_Layout_hideMember(_1,_2){return this.hideMembers([_1],_2)}
,isc.A.hideMembers=function isc_Layout_hideMembers(_1,_2){this.$105=_2;if(this.animateMembers&&_1.length==1){this.$103(_1[0],_2)}else{for(var i=0;i<_1.length;i++){var _4=this.getMember(_1[i]);_4.hide()}
this.fireCallback(_2)}}
,isc.A.$103=function isc_Layout__animateMemberHide(_1,_2){_1=this.getMember(_1);_1.animateHide(this.animateMemberEffect,_2,this.animateMemberTime);if(_1.isAnimating())this.$101(_1)}
,isc.A.setVisibleMember=function isc_Layout_setVisibleMember(_1){var _2=this.getMember(_1);if(_2==null)return;this.hideMembers(this.members);this.showMember(_2)}
,isc.A.reorderMember=function isc_Layout_reorderMember(_1,_2){this.reorderMembers(_1,_1+1,_2)}
,isc.A.reorderMembers=function isc_Layout_reorderMembers(_1,_2,_3){this.members.slideRange(_1,_2,_3);this.$106("membersReordered")}
,isc.A.$106=function isc_Layout__membersReordered(_1){this.layoutChildren(_1);this.$9u()}
,isc.A.replaceMember=function isc_Layout_replaceMember(_1,_2){var _3=this.instantRelayout;this.instantRelayout=false;var _4=this.getMemberNumber(_1);this.removeMember(_1,true);this.addMember(_2,_4,true);this.instantRelayout=_3;if(_3)this.reflowNow()}
,isc.A.$9u=function isc_Layout__membersChanged(){if(!this.destroying){this.$9f()}
if(this.membersChanged)this.membersChanged()}
,isc.A.$9f=function isc_Layout__computeShowResizeBarsForMembers(){var _1=this.defaultResizeBars;for(var i=this.members.length-1;i>=0;i--){var _3=this.members[i];if(_3==null)continue;var _4=false;if(_1==isc.Canvas.MARKED){_4=_3.showResizeBar}else if(_1==isc.Canvas.MIDDLE){_4=(i<this.members.length-1)&&(_3.showResizeBar!=false)}else if(_1==isc.Canvas.ALL){_4=_3.showResizeBar!=false}
var _5=_3.$96;_3.$96=_4;if(_5!=_4)this.reflow("$96 changed")}}
,isc.A.updateMemberTabIndex=function isc_Layout_updateMemberTabIndex(_1){if(!this.$91(_1)||(_1.tabIndex!=null&&!_1.$xb))return;var _2,_3=this.members.indexOf(_1);while(_3>0&&_2==null){_3-=1
_2=this.members[_3].$07()}
if(_2==null&&(this.tabIndex==null||this.$xb))
_2=this;if(_2){_1.$xc(_2)}}
,isc.A.dragRepositionStart=function isc_Layout_dragRepositionStart(){var _1=isc.EH.dragTarget;if(!this.hasMember(_1)||_1.getDragAppearance(isc.EH.DRAG_REPOSITION)!="target")return;var _2=_1.getPageLeft(),_3=_1.getPageTop();this.$107(_1,_2,_3)}
,isc.A.$107=function isc_Layout__popOutDraggingMember(_1,_2,_3){this.$108=_1;var _4=this.$109(_1,"$11a",this.showDragPlaceHolder)
_1.$11b=_4;var _5=this.instantRelayout;this.instantRelayout=false;this.addMember(_4,this.getMemberNumber(_1),true);_1.deparent();_1.eventParent=this;this.instantRelayout=_5;_1.moveTo(_2,_3);_1.draw()}
,isc.A.dragRepositionStop=function isc_Layout_dragRepositionStop(){var _1=isc.EH.dragTarget;if(!this.members.contains(_1)&&_1!=this.$108)return;var _2=_1.getDragAppearance(isc.EH.DRAG_REPOSITION),_3=_2==isc.EH.TARGET;if(!_3&&(_2!=isc.EH.OUTLINE))return false;var _4=_3?isc.EH.STOP_BUBBLING:false;this.$108=null;if(_1.eventParent==this)_1.eventParent=null;if(_1.dropSucceeded)return _4;var _5=_1.$11b;if(_5!=null){if(_1.parentElement!=null||_1.destroyed){this.$11c(_1)}else{_1.$11b=null;var _6=this.getMemberNumber(_5),_7=_5.getPageRect(),_8=this,_9=function(){_8.replaceMember(_5,_1)};if(this.animateMembers){_1.animateRect(_7[0],_7[1],_7[2],_7[3],_9)}else
_9(true)}}
return _4}
,isc.A.$109=function isc_Layout__createSpacer(_1,_2,_3){var _4,_5;if(_3){_4=this.createAutoChild("placeHolder",_5,isc.Canvas)}else{_4=isc.LayoutSpacer.create(_5)}
_4.setRect(_1.getRect());_4.$ts=_4.getWidth();_4.$tt=_4.getHeight();_4.layoutAlign=_1.layoutAlign;_4.extraSpace=(_1.extraSpace||0);_4.$104=true;return _4}
,isc.A.removePlaceHolder=function isc_Layout_removePlaceHolder(_1){if(this.animateMembers&&!isc.isA.LayoutSpacer(_1)){var _2=this.$109(_1);this.replaceMember(_1,_2);_1.destroy();_1=_2}
this.removeMember(_1)}
,isc.A.willAcceptDrop=function isc_Layout_willAcceptDrop(){if(!this.canAcceptDrop||!this.canDropComponents)return false;return this.invokeSuper(isc.Layout,"willAcceptDrop")}
,isc.A.dropOver=function isc_Layout_dropOver(){if(!this.willAcceptDrop())return;this.showDropLine();isc.EventHandler.dragTarget.bringToFront();return true}
);isc.evalBoundary;isc.B.push(isc.A.dropMove=function isc_Layout_dropMove(){if(!this.willAcceptDrop())return;this.showDropLine()}
,isc.A.dropOut=function isc_Layout_dropOut(){this.hideDropLine()}
,isc.A.dropStop=function isc_Layout_dropStop(){this.hideDropLine()}
,isc.A.getDropComponent=function isc_Layout_getDropComponent(_1,_2){if(!isc.isA.Palette(_1))return _1;var _3=_1.transferDragData(),_4=(isc.isAn.Array(_3)?_3[0]:_3);return _4.liveObject}
,isc.A.drop=function isc_Layout_drop(){if(!this.willAcceptDrop()||this.$11d)return;var _1=this.getDropPosition();var _2=this.getDropComponent(isc.EventHandler.getDragTarget(),_1);if(!_2)return;var _3=this.members.indexOf(_2);if(_3==-1&&_2.$11b)
_3=this.members.indexOf(_2.$11b)
if(_3!=-1&&(_3==_1||_3+1==_1))
{return false}
_2.dropSucceeded=true;if(isc.Browser.isMoz){this.delayCall("$11e",[_2,_1])}else{this.$11e(_2,_1)}
return isc.EH.STOP_BUBBLING}
,isc.A.$11e=function isc_Layout__completeDrop(_1,_2){this.hideDropLine();var _3=_1.parentElement;if(_3&&_1.getDragAppearance(isc.EH.dragOperation)==isc.EH.OUTLINE&&this.animateMembers&&isc.isA.Layout(_3)&&_3.hasMember(_1))
{_3.$107(_1,isc.EH.dragOutline.getPageLeft(),isc.EH.dragOutline.getPageTop())}
var _4=false;if(this.members.contains(_1)){var _5=this.members.indexOf(_1);if(_5<_2)_4=true;this.removeMember(_1,true)}else{var _6=_1.$11b;if(_6!=null){var _7=this.getMemberNumber(_6)
if((_7>=0)&&(_7<_2)){_4=true}
_6.parentElement.$11c(_1)}}
var _8=_2-(_4?1:0);if(!this.animateMembers||(_1.dragAppearance!="target"&&_1.dragAppearance!="outline")){this.addMember(_1,_8);delete _1.dropSucceeded;return}
var _9=this.$109(_1,"$11f");this.addMember(_9,_2);this.reflowNow();this.$11g=_1;var _10=this,_11=_9.getPageLeft(),_12=_9.getPageTop();if(_4){var _13=this.membersMargin+this.getMemberGap(_1);if(this.vertical)_12-=(_1.getVisibleHeight()+_13);else _11-=(_1.getVisibleWidth()+_13)}
if(_9==this.members.last()&&this.members.length>1){var _14=(this.members[this.members.length-2].$9r||0);if(this.vertical)_12-=_14;else _11-=_14}
_1.animateMove(_11,_12,function(){_10.$11g=null;var _15=_10.instantRelayout;_10.instantRelayout=false;_9.destroy();_1.dropSucceeded=null;_10.addMember(_1,_8,true);_10.instantRelayout=_15;if(_15)_10.reflowNow()},this.animateMemberTime)}
,isc.A.$11c=function isc_Layout__cleanUpPlaceHolder(_1){var _2=_1.$11b;if(this.hasMember(_2)){_1.$11b=null;this.removePlaceHolder(_2)}}
,isc.A.$10y=function isc_Layout__finishDropAnimation(){if(this.$11g!=null){this.$11g.finishAnimation("move")}}
,isc.A.getDropPosition=function isc_Layout_getDropPosition(){var _1=this.vertical?this.getOffsetY():this.getOffsetX();if(_1<0)return 0;var _2=this.vertical?this.$xy:this.$xw;for(var i=0;i<this.memberSizes.length;i++){var _4=this.memberSizes[i],_5=this.members[i];if(_1<(_2+(_4/ 2))){if(_5.canDropBefore===false)return false;return i}
_2+=_4+this.membersMargin+this.getMemberGap(_5)}
return this.members.length}
,isc.A.$11h=function isc_Layout__getChildInset(_1){return(_1?this.getTopMargin()+this.getTopBorderSize():this.getLeftMargin()+this.getLeftBorderSize())}
,isc.A.getPositionOffset=function isc_Layout_getPositionOffset(_1){if(this.members.length==0){return this.vertical?this.getPageTop()+this.$11h(true)+this.$xy:this.getPageLeft()+this.$11h()+this.$xw}
if(_1<this.members.length){var _2=this.members[_1];return(this.vertical?_2.getPageTop():_2.getPageLeft())}else{var _2=this.members[_1-1];return(this.vertical?_2.getPageBottom():_2.getPageRight())}}
,isc.A.showDropLine=function isc_Layout_showDropLine(){if(this.$11d)return;var _1=this.getDropPosition();if(!isc.isA.Number(_1)){this.hideDropLine();return}
if(_1<0)return;if(this.$10v)this.reflowNow();if(!this._dropLine)this._dropLine=this.makeDropLine();var _2=this.dropLineThickness,_3=this.getPositionOffset(_1);var _4;if(this.$xw==null)this.setLayoutMargin();if(_1==0){_4=this.vertical?this.$xy:this.$xw}else if(_1==this.members.length){_4=-(this.vertical?this.$xz:this.$xx)}else{_4=this.membersMargin}
_3=_3-Math.round((_4+_2)/2);var _5=this.vertical?this.$xw+this.$11h():this.$xy+this.$11h(true);var _6=this.vertical?this.getVisibleWidth()-this.getVMarginBorder()-this.$9x():this.getVisibleHeight()-this.getHMarginBorder()-this.$9y();var _7=_6+this.$9y();this._dropLine.setPageRect((this.vertical?this.getPageLeft()+_5:_3),(this.vertical?_3:this.getPageTop()+_5),(this.vertical?_6:_2),(this.vertical?_2:_7));var _8=this.topElement||this;if(this._dropLine.getZIndex()<_8.getZIndex())this._dropLine.moveAbove(_8);this._dropLine.show()}
,isc.A.hideDropLine=function isc_Layout_hideDropLine(){if(this._dropLine)this._dropLine.hide()}
,isc.A.makeDropLine=function isc_Layout_makeDropLine(){var _1=this.createAutoChild("dropLine",null,isc.Canvas);_1.dropTarget=this;return _1}
,isc.A.createResizeBar=function isc_Layout_createResizeBar(_1,_2,_3,_4){var _5=isc.ClassFactory.getClass(this.resizeBarClass).createRaw();_5.autoDraw=false;_5.target=_1;_5.targetAfter=_3;_5.hideTarget=_4;_5.layout=this;_5.vertical=!this.vertical;_5.dragScrollDirection=this.vertical?isc.Canvas.VERTICAL:isc.Canvas.HORIZONTAL;_5.dragScrollType="parentsOnly";_5.init();return _5}
,isc.A.makeResizeBar=function isc_Layout_makeResizeBar(_1,_2,_3,_4){var _5=_1.$10m;if(_5==null){var _6=_1,_7,_8,_9=this.getMember(this.getMemberNumber(_1)+1)||_1;if(_1.resizeBarTarget=="next"){_6=_9;_7=true}
if(_1.resizeBarHideTarget!=null){if(_1.resizeBarHideTarget=="next")_8=_9;else _8=_1}else{_8=_6}
_5=this.createResizeBar(_6,_3,_7,_8);_1.$10m=_5}
if(this.vertical){_5.setRect(_2,_3,_4,this.resizeBarSize)}else{if(this.isRTL())_3-=this.resizeBarSize;_5.setRect(_3,_2,this.resizeBarSize,_4)}
if(this.membersAreChildren){this.addChild(_5)}else{this.addPeer(_5)}
if(!_5.isDrawn())_5.draw();if(!_5.isVisible())_5.show();return _5}
,isc.A.propertyChanged=function isc_Layout_propertyChanged(_1,_2){this.invokeSuper(isc.Layout,"propertyChanged",_1,_2);if(isc.endsWith(_1,"Margin"))this.setLayoutMargin()}
,isc.A.getLengthAxis=function isc_Layout_getLengthAxis(){return this.vertical?"height":"width"}
,isc.A.$9m=function isc_Layout__reportResize(_1,_2,_3){var _4=this.vertical?_2:_3,_5=this.vertical?_3:_2,_6=_1.getDelta("width",_4,_1.getWidth()),_7=_1.getDelta("height",_5,_1.getHeight());if((_6!=null&&_6!=0)||(_7!=null&&_7!=0)){this.logDebug("resizing "+_1+(_1.isDrawn()?" (drawn): ":": ")+(_2!=null?_2+(this.vertical?"w ":"h "):"")+(_3!=null?_3+(this.vertical?"h":"w"):""),"layout")}}
,isc.A.reportSizes=function isc_Layout_reportSizes(_1,_2){if(!this.logIsInfoEnabled(this.$9g))return;var _3="layoutChildren (reason: "+_2+"):\nlayout specified size: "+this.getWidth()+"w x "+this.getHeight()+"h\n"+"drawn size: "+this.getVisibleWidth(true)+"w x "+this.getVisibleHeight(true)+"h\n"+"available size: "+this.getInnerWidth()+(!this.vertical?"w (length) x ":"w x ")+this.getInnerHeight()+(this.vertical?"h (length)\n":"h\n");for(var i=0;i<_1.length;i++){var _5=_1[i];_3+="   "+this.members[i]+"\n";_3+="      "+_5.$10n+" drawn length"+(_5.$10j?" (resizeLength: "+_5.$10j+")":"")+" (policyLength: "+_5.$10e+")"+" ("+_5.$10f+")\n";_3+="      "+_5.$10i+" drawn breadth ("+_5.$10h+")\n"}
if(_1.length==0)_3+="[No members]";this.logInfo(_3,"layout")}
);isc.B._maxIndex=isc.C+125;isc.defineClass("HLayout","Layout");isc.A=isc.HLayout.getPrototype();isc.A.orientation="horizontal";isc.A.animateMemberEffect={effect:"slide",startFrom:"L",endAt:"L"};isc.defineClass("VLayout","Layout");isc.A=isc.VLayout.getPrototype();isc.A.orientation="vertical";isc.defineClass("HStack","Layout");isc.A=isc.HStack.getPrototype();isc.A.orientation="horizontal";isc.A.hPolicy=isc.Layout.NONE;isc.A.animateMemberEffect={effect:"slide",startFrom:"L",endAt:"L"};isc.A.defaultWidth=20;isc.defineClass("VStack","Layout");isc.A=isc.VStack.getPrototype();isc.A.orientation="vertical";isc.A.vPolicy=isc.Layout.NONE;isc.A.defaultHeight=20;isc.defineClass("LayoutSpacer","Canvas");isc.A=isc.LayoutSpacer.getPrototype();isc.A.overflow="hidden";isc.A.draw=isc.Canvas.NO_OP;isc.A.redraw=isc.Canvas.NO_OP;isc.A.$xj=true;isc.Layout.registerDupProperties("members");isc.defineClass("Button","StatefulCanvas");isc.A=isc.Button.getPrototype();isc.A.title="Untitled Button";isc.A.suppressClassName=true;isc.A.useEventParts=true;isc.A.autoFitDirection="horizontal";isc.A.baseStyle="button";isc.A.showDown=true;isc.A.showFocused=true;isc.A.showRollOver=true;isc.A.mozOutlineOffset="0px";isc.A.wrap=false;isc.A.height=20;isc.A.width=100;isc.A.overflow=isc.Canvas.HIDDEN;isc.A.redrawOnDisable=false;isc.A.redrawOnStateChange=false;isc.A.cursor=isc.Canvas.HAND;isc.A.className=null;isc.A.canFocus=true;isc.A=isc.Button.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$11i="px;";isc.A.$px=";";isc.A.$11j="border:";isc.A.$11k="padding-top:0px;padding-bottom:0px;";isc.A.$11l="padding:";isc.A.$11m="background-color:";isc.A.$11n="margin:0px;";isc.A.$11o=["' style='",,,,,,,,,,,null];isc.A.$11p="</td></tr></tbody></table>";isc.A.$11q="<table cellspacing='0' cellpadding='0'><tbody><tr><td ";isc.A.$11r="<table width='100%' cellspacing='0' cellpadding='0'><tbody><tr><td ";isc.A.$11s="font-size:1px;padding-right:";isc.A.$11t="font-size:1px;padding-left:";isc.A.$11u="px'>";isc.A.$11v="</td><td ";isc.A.$49="class='";isc.A.$11w="'>";isc.A.$11x="' nowrap='true'>";isc.A.$11y="</td></tr></tbody></table>";isc.A.$s2="right";isc.A.$11z={align:"absmiddle",extraStuff:" style='vertical-align:middle' eventpart='icon'"};isc.A.$110=[null,"$111"];isc.B.push(isc.A.initWidget=function isc_Button_initWidget(){if(this.border!=null){this.$112=this.border;this.border=null}
if(this.padding!=null){this.$113=this.padding;this.padding=null}
if(this.backgroundColor!=null){this.$114=this.backgroundColor;this.backgroundColor=null}
var _1=isc.Button.$115;if(_1==null){_1=isc.Button.$115=(isc.Browser.isIE||(isc.Browser.isMoz&&!isc.Browser.isStrict&&isc.Canvas.getInstanceProperty("$mi"))?false:true)}
this.redrawOnResize=_1;return isc.StatefulCanvas.$ce.initWidget.call(this)}
,isc.A.getInnerHTML=function isc_Button_getInnerHTML(){var _1=isc.Button;if(!_1.$116){_1._100Size=" width='100%' height='100%";_1._100Width=" width='100%";_1.$2d="width='";_1.$2e="' height='";_1.$117="' style='table-layout:fixed;overflow:hidden;";_1.$118="'><tbody><tr><td class='";_1.$119="'><tbody><tr><td nowrap='true' class='";var _2=_1.$116=[];_2[0]="<table cellspacing='0' cellpadding='0' ";_2[9]="' align='";_1.$12a="' valign='center";_1.$12b="' valign='top";_1.$12c="' valign='bottom";_1.$12d="' tabindex='-1' onfocus='";_1.$12e=".$12f()'>";_1.$12g="'>"}
var _2=_1.$116;if(this.isPrinting||this.redrawOnResize==false){_2[1]=(this.isPrinting?_1._100Width:_1._100Size);_2[2]=null;_2[3]=null;_2[4]=null}else{_2[1]=_1.$2d;_2[2]=this.getInnerWidth();_2[3]=_1.$2e;_2[4]=this.getInnerHeight()}
if(this.overflow==isc.Canvas.VISIBLE){_2[5]=null}else{_2[5]=_1.$117}
_2[6]=(this.wrap?_1.$118:_1.$119);_2[7]=this.isPrinting?this.getPrintStyleName():this.getStateName();var _3=(this.cssText||this.$112||this.$113||this.$114||this.margin||this.$12h());if(_3)_2[8]=this.$12i();else _2[8]=null;_2[10]=(this.$12j()?isc.Canvas.CENTER:this.align);_2[11]=(this.valign==isc.Canvas.TOP?_1.$12b:(this.valign==isc.Canvas.BOTTOM?_1.$12c:_1.$12a));if(this.$mb()&&this._useNativeTabIndex){_2[12]=_1.$12d;_2[13]=this.getID();_2[14]=_1.$12e}else{_2[12]=_1.$12g;_2[13]=_2[14]=null}
this.fillInCell(_2,15)
return _2.join(isc.emptyString)}
,isc.A.setOverflow=function isc_Button_setOverflow(){var _1=this.isDirty();this.Super("setOverflow",arguments);if(!_1)this.redraw()}
,isc.A.getPrintTagStart=function isc_Button_getPrintTagStart(){var _1=this.currentPrintProperties,_2=_1.topLevelCanvas==this,_3=!_2&&_1.inline;return[(_3?"<span ":"<div "),">"].join(isc.emptyString)}
,isc.A.$12i=function isc_Button__getCellStyleHTML(){var _1=this.$11o;_1[1]=(this.cssText?this.cssText:null);if(this.$112!=null){_1[2]=this.$11j;_1[3]=this.$112;_1[4]=this.$px}else{_1[2]=null;_1[3]=null;_1[4]=null}
var _2=this.$113;if(_2!=null){_1[5]=this.$11l;_1[6]=_2;_1[7]=this.$px}else{_1[5]=null;_1[6]=null;_1[7]=null}
if(this.$12h()){_1[7]=(_1[7]||isc.emptyString)+this.$11k}
if(this.$114!=null){_1[8]=this.$11m;_1[9]=this.$114;_1[10]=this.$px}else{_1[8]=null;_1[9]=null;_1[10]=null}
if(this.margin!=null)_1[11]=this.$11n;else _1[11]=null;return _1.join(isc.emptyString)}
,isc.A.$12h=function isc_Button__writeZeroVPadding(){return this.overflow==isc.Canvas.HIDDEN&&!this.isAnimating()&&(isc.Browser.isMoz||isc.Browser.isSafari||isc.Browser.isIE)}
,isc.A.setBorder=function isc_Button_setBorder(_1){this.$112=_1;this.markForRedraw()}
,isc.A.setPadding=function isc_Button_setPadding(_1){this.$113=_1;this.markForRedraw()}
,isc.A.setBackgroundColor=function isc_Button_setBackgroundColor(_1){this.$114=_1;this.markForRedraw()}
,isc.A.$12k=function isc_Button__endTemplate(_1,_2){_1[_2]=this.$11p;_1.length=_2+1;return _1}
,isc.A.$12j=function isc_Button__iconAtEdge(){return this.icon!=null&&this.iconAlign!=null&&(this.iconAlign==this.iconOrientation)&&(this.iconAlign!=this.align)}
,isc.A.fillInCell=function isc_Button_fillInCell(_1,_2){var _3=this.isRTL();var _4=this.getTitleHTML();if(!this.icon){if(isc.Browser.isMoz){var _5=this.reliableMinHeight;_1[_2]=(_5?"<div>":null);_1[_2+1]=_4;_1[_2+2]=(_5?"</div>":null);this.$12k(_1,_2+3)}else{_1[_2]=_4;this.$12k(_1,_2+1)}
return}
var _6=this.iconOrientation!=this.$s2,_7=this.$12l();if(this.noIconSubtable){var _8=isc.Canvas.spacerHTML(this.iconSpacing,1);_1[_2]=(_6?isc.SB.concat(_7,_8,_4):isc.SB.concat(_4,_8,_7));this.$12k(_1,_2+1)
return}
var _9=this.$12j(),_10;if(_9){_10=(this.iconWidth?this.iconWidth:this.iconSize)+(isc.Browser.isBorderBox?this.iconSpacing:0)}
_1[_2]=(_9?this.$11r:this.$11q);var _11=this.isPrinting?this.getPrintStyleName():(this.titleStyle?this.titleStyle+(this.isDisabled()?isc.StatefulCanvas.STATE_DISABLED:isc.emptyString):this.getStateName());if(_6){_1[++_2]=this.$49;_1[++_2]=_11;_1[++_2]=this.$rs;_1[++_2]=!_3?this.$11s:this.$11t;_1[++_2]=this.iconSpacing;if(_9){_1[++_2]="px;width:";_1[++_2]=_10}
_1[++_2]=this.$11u;_1[++_2]=_7;_1[++_2]=this.$11v;_1[++_2]=this.$49;_1[++_2]=_11;_1[++_2]=this.$rs;if(_9){_1[++_2]="' align='"
_1[++_2]=this.align}
_1[++_2]=(this.wrap?this.$11w:this.$11x)
_1[++_2]=_4}else{_1[++_2]=this.$49;_1[++_2]=_11;_1[++_2]=this.$rs;if(_9){_1[++_2]="' align='";_1[++_2]=this.align}
_1[++_2]=(this.wrap?this.$11w:this.$11x)
_1[++_2]=_4;_1[++_2]=this.$11v;_1[++_2]=this.$49;_1[++_2]=_11;_1[++_2]=this.$rs;_1[++_2]=!_3?this.$11t:this.$11s;_1[++_2]=this.iconSpacing;if(_9){_1[++_2]="px;width:";_1[++_2]=_10}
_1[++_2]=this.$11u;_1[++_2]=_7}
_1[++_2]=this.$11y;this.$12k(_1,_2+1)}
,isc.A.$12l=function isc_Button__generateIconImgHTML(){var _1=this.$11z;if(this.$12m==null){this.$110[0]=this.getID();this.$12m=this.$110.join(isc.emptyString)}
_1.name=this.$12m;_1.width=this.iconWidth||this.iconSize;_1.height=this.iconHeight||this.iconSize;_1.src=this.$12n();return this.imgHTML(_1)}
,isc.A.$12n=function isc_Button__getIconURL(){var _1=this.state,_2=this.selected,_3=this.getCustomState(),_4=isc.StatefulCanvas;if(_1==_4.STATE_DISABLED&&!this.showDisabledIcon)_1=null;else if(_1==_4.STATE_DOWN&&!this.showDownIcon)_1=null;else if(_1==_4.STATE_OVER&&!this.showRollOverIcon)_1=null;if(!this.showIconState){_1=null;_3=null}
if(_2&&!this.showSelectedIcon)_2=false;var _5=this.showFocusedIcon?this.getFocusedState():null;var _6=this.icon;if(isc.isAn.Object(_6))_6=_6.src;return isc.Img.urlForState(_6,_2,_5,_1,null,_3)}
,isc.A.getTitleHTML=function isc_Button_getTitleHTML(_1,_2,_3,_4){var _5=this.invokeSuper(isc.Button,"getTitleHTML",_1,_2,_3,_4);if(!this.padTitle||this.align==isc.Canvas.CENTER)return _5;if(this.align==isc.Canvas.RIGHT)return _5+isc.nbsp;else if(this.align==isc.Canvas.LEFT)return isc.nbsp+_5}
,isc.A.setWrap=function isc_Button_setWrap(_1){if(this.wrap!=_1){this.wrap=_1;this.markForRedraw("wrapChanged")}}
,isc.A.getTitleCell=function isc_Button_getTitleCell(){if(!this.getHandle())return null;var _1=this.getHandle().firstChild.rows[0].cells[0];return _1}
,isc.A.getButtonMinHeight=function isc_Button_getButtonMinHeight(){var _1=this.getTitleCell();if(!isc.Browser.isMoz){return _1.scrollHeight+isc.Element.$q3(this.getStateName())}
return _1.firstChild.offsetHeight+isc.Element.$q3(this.getStateName())}
,isc.A.getPreferredWidth=function isc_Button_getPreferredWidth(){var _1=this.wrap,_2=this.overflow,_3=this.width;this.setWrap(false);this.overflow=isc.Canvas.VISIBLE;this.setWidth(1);this.redrawIfDirty("getPreferredWidth");var _4=this.getScrollWidth();this.setWrap(_1);this.overflow=_2;this.setWidth(_3);return _4}
,isc.A.getTitle=function isc_Button_getTitle(){if(this.useContents)return this.getContents();return this.title}
,isc.A.setTitle=function isc_Button_setTitle(_1){this.title=_1;this.markForRedraw("setTitle")}
,isc.A.stateChanged=function isc_Button_stateChanged(){if(this.redrawOnStateChange||!this.isDrawn()){return this.Super("stateChanged")}else{var _1=this.isPrinting?this.getPrintStyleName():this.getStateName();if(!this.suppressClassName)this.setClassName(_1);else this.setTableClassName(_1);if(this.icon){this.setImage(this.$12m,this.$12n())}}}
,isc.A.setTableClassName=function isc_Button_setTableClassName(_1){var _2=this.getTitleCell();if(!_2)return;if(_2.className!=_1)_2.className=_1;if(this.icon&&!this.noIconSubtable&&!this.titleStyle){var _3=_2.firstChild.rows[0].cells[(this.iconOrientation==this.$s2?0:1)];if(_3&&_3.className!=_1)_3.className=_1}
if(this.overflow==isc.Canvas.VISIBLE)this.adjustOverflow()}
,isc.A.setTitleStyle=function isc_Button_setTitleStyle(_1){this.titleStyle=_1;if(!this.isDrawn()||!this.icon||this.noIconSubtable)return
var _2=this.getTitleCell();if(!_2)return;var _3=_2.firstChild.rows[0].cells[1];if(_3&&_3.className!=_1)_3.className=_1}
,isc.A.setIcon=function isc_Button_setIcon(_1){var _2=this.icon!=null;this.icon=_1;if(_2&&(_1!=null))this.setImage(this.$12m,this.$12n());else this.redraw()}
,isc.A.$12f=function isc_Button__cellFocus(){isc.EH.$jp("cFCS");this.focus();isc.EH.$jq()}
,isc.A.$zn=function isc_Button__updateCanFocus(){this.Super("$zn",arguments);if(this._useNativeTabIndex)this.markForRedraw()}
);isc.B._maxIndex=isc.C+27;isc.Button.registerStringMethods({getTitle:null});isc.ClassFactory.defineClass("AutoFitButton","Button");isc.A=isc.AutoFitButton.getPrototype();isc.A.autoFit=true;isc.Button.registerStringMethods({iconClick:"element,ID,event"});isc.addGlobal("IButton",isc.Button);isc.defineClass("Img","StatefulCanvas");isc.A=isc.Img;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$12o=[];isc.B.push(isc.A.urlForState=function isc_c_Img_urlForState(_1,_2,_3,_4,_5,_6){if(!_1)return _1;if(!_4&&!_5&&!_2&&!_3&&!_6)return _1;var _7=_1.lastIndexOf(isc.dot),_8=_1.substring(0,_7),_9=_1.substring(_7+1),_10=this.$12o;_10.length=1;_10[0]=_8;if(_2){_10[1]=isc.$ak;_10[2]=isc.StatefulCanvas.SELECTED}
if(_3){_10[3]=isc.$ak;_10[4]=isc.StatefulCanvas.FOCUSED}
if(_4){_10[5]=isc.$ak;_10[6]=_4}
if(_6){_10[7]=isc.$ak;_10[8]=_6}
if(_5){_10[9]=isc.$ak;_10[10]=_5}
_10[11]=isc.dot;_10[12]=_9;var _11=_10.join(isc.$ah);return _11}
);isc.B._maxIndex=isc.C+1;isc.A=isc.Img.getPrototype();isc.A.name="main";isc.A.src="blank.gif";isc.A.imageType=isc.Img.STRETCH;isc.A.suppressClassName=false;isc.A.mozOutlineOffset="0px";isc.A.showTitle=false;isc.A.usePNGFix=true;isc.A=isc.Img.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$12p="<TABLE WIDTH=";isc.A.$12q=" HEIGHT=";isc.A.$12r=" BORDER=0 CELLSPACING=0 CELLPADDING=0><TR>";isc.A.$12s="<TD VALIGN=center ALIGN=center>";isc.A.$12t="<TD BACKGROUND=";isc.A.$12u="</TD></TR></TABLE>";isc.A.sizeImageToFitOverflow=false;isc.B.push(isc.A.initWidget=function isc_Img_initWidget(){isc.StatefulCanvas.$ce.initWidget.call(this);this.redrawOnResize=(this.imageType!=isc.Img.STRETCH)}
,isc.A.setImageType=function isc_Img_setImageType(_1){if(this.imageType==_1)return;this.imageType=_1;this.markForRedraw();this.redrawOnResize=(this.imageType!=isc.Img.STRETCH)}
,isc.A.getInnerHTML=function isc_Img_getInnerHTML(){var _1=this.sizeImageToFitOverflow?this.getOverflowedInnerWidth():this.getInnerWidth(),_2=this.sizeImageToFitOverflow?this.getOverflowedInnerHeight():this.getInnerHeight(),_3=this.imageType;var _4=this.extraStuff;if(this.altText!=null){var _5=this.altText;_5=" alt='"+_5.replace("'","&apos;")+"'"
if(_4==null)_4=_5;else _4+=" "+_5}
if(_3==isc.Img.STRETCH||_3==isc.Img.NORMAL){if(_3==isc.Img.NORMAL){_1=this.imageWidth;_2=this.imageHeight}
return this.imgHTML(this.getURL(),_1,_2,this.name,_4,null,this.activeAreaHTML)}
var _6=isc.SB.create();_6.append(this.$12p,_1,this.$12q,_2,this.$12r);if(_3==isc.Img.TILE){_6.append(this.$12t,this.getImgURL(this.getURL()),this.$r8,isc.Canvas.spacerHTML(_1,_2))}else{_6.append(this.$12s,this.imgHTML(this.getURL(),this.imageWidth,this.imageHeight,this.name,_4,null,this.activeAreaHTML))}
_6.append(this.$12u);return _6.toString()}
,isc.A.getOverflowedInnerWidth=function isc_Img_getOverflowedInnerWidth(){return this.getVisibleWidth()-this.getHMarginBorder()}
,isc.A.getOverflowedInnerHeight=function isc_Img_getOverflowedInnerHeight(){return this.getVisibleHeight()-this.getVMarginBorder()}
,isc.A.$yl=function isc_Img__handleResized(_1,_2){if(this.redrawOnResize!=false||!this.isDrawn())return;var _3=this.getImage(this.name).style;var _4=this.sizeImageToFitOverflow?this.getOverflowedInnerWidth():this.getInnerWidth(),_5=this.sizeImageToFitOverflow?this.getOverflowedInnerHeight():this.getInnerHeight();this.$z6(_3,this.$s7,_4);this.$z6(_3,this.$s6,_5)}
,isc.A.$8w=function isc_Img__labelAdjustOverflow(){this.Super("$8w",arguments);if(this.overflow!=isc.Canvas.VISIBLE||!this.sizeImageToFitOverflow)return;var _1=this.getImage(this.name),_2=_1?_1.style:null;if(_2==null)return;var _3=this.getOverflowedInnerWidth(),_4=this.getOverflowedInnerHeight();this.$z6(_2,this.$s7,_3);this.$z6(_2,this.$s6,_4)}
,isc.A.setSrc=function isc_Img_setSrc(_1){if(_1==null||this.src==_1)return;this.src=_1;this.resetSrc()}
,isc.A.resetSrc=function isc_Img_resetSrc(){if(!this.isDrawn())return;if(this.imageType!=isc.Img.TILE){this.setImage(this.name,this.getURL())}else{this.markForRedraw("setSrc on tiled image")}}
,isc.A.stateChanged=function isc_Img_stateChanged(){this.Super("stateChanged");if(!this.statelessImage)this.resetSrc()}
,isc.A.getHoverHTML=function isc_Img_getHoverHTML(){if(this.altText){if(isc.Browser.isIE)return null;if(this.prompt&&this.prompt!=this.altText){this.logWarn("Img component specified with altText:"+this.altText+" and prompt:"+this.prompt+". Value for 'prompt' attribute will be ignored in favor of 'altText' value.")}
return this.altText}
return this.Super("getHoverHTML",arguments)}
);isc.B._maxIndex=isc.C+11;isc.ClassFactory.defineClass("StretchImg","StatefulCanvas");isc.A=isc.StretchImg.getPrototype();isc.A.vertical=true;isc.A.capSize=2;isc.A.overflow=isc.Canvas.HIDDEN;isc.A.imageType=isc.Img.STRETCH;isc.A.items=[{name:"start",width:"capSize",height:"capSize"},{name:"stretch",width:"*",height:"*"},{name:"end",width:"capSize",height:"capSize"}];isc.A.autoCalculateSizes=true;isc.A.cacheImageSizes=true;isc.A.suppressClassName=false;isc.A.mozOutlineOffset="0px";isc.A.showTitle=false;isc.A=isc.StretchImg.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$12v="<NOBR>";isc.A.$12w="</NOBR>";isc.A.$12x="<BR>";isc.A.$12y=" STYLE='display:block'";isc.A.$12p="<TABLE style='font-size:1px;' CELLPADDING=0 CELLSPACING=0 BORDER=0>";isc.A.$12u="</TABLE>";isc.A.$12z="<TR><TD class='";isc.A.$120="</TD></TR>";isc.A.$121="<TD class='";isc.A.$122="'>";isc.A.$123="</TD>";isc.A.renderStretchImgInTable=isc.Browser.isMoz||isc.Browser.isIE8Strict;isc.A.oversizeStretchImg=isc.Browser.isMoz&&isc.Browser.isUnix;isc.A.$124="blank";isc.B.push(isc.A.initWidget=function isc_StretchImg_initWidget(){isc.StatefulCanvas.$ce.initWidget.call(this);this.redrawOnResize=(this.imageType!=isc.Img.STRETCH)}
,isc.A.shouldShowLabel=function isc_StretchImg_shouldShowLabel(){if(this.showGrip)return true;return this.Super("shouldShowLabel",arguments)}
,isc.A.getPart=function isc_StretchImg_getPart(_1){for(var i=0,_3=this.items.length,_4;i<_3;i++){_4=this.items[i];if(_4.name==_1)return _4}
return null}
,isc.A.getPartNum=function isc_StretchImg_getPartNum(_1){for(var i=0,_3=this.items.length,_4;i<_3;i++){_4=this.items[i];if(_4.name==_1)return i}
return null}
,isc.A.getSize=function isc_StretchImg_getSize(_1){if(!this.$125||this.$126)this.resizeImages();return this.$125[_1]}
,isc.A.$8w=function isc_StretchImg__labelAdjustOverflow(_1,_2,_3,_4){if(this.overflow==isc.Canvas.VISIBLE)this.$yl(null,null,true);this.invokeSuper(isc.StretchImg,"$8w",_1,_2,_3,_4)}
,isc.A.setOverflow=function isc_StretchImg_setOverflow(_1,_2,_3,_4){var _5=false;if(this.overflow==isc.Canvas.VISIBLE&&((this.getScrollWidth()>this.getWidth())||(this.getScrollHeight()>this.getHeight())))
{_5=true}
this.invokeSuper(isc.StretchImg,"setOverflow",_1,_2,_3,_4);if(_5)this.$yl(null,null,true)}
,isc.A.$yl=function isc_StretchImg__handleResized(_1,_2,_3){if(this.redrawOnResize!=false||!this.isDrawn()){this.$126=true;return}
if(this.$8d)return;this.resizeImages();var _4=this.items,_5=_3||(isc.isA.Number(_1)&&_1!=0),_6=_3||(isc.isA.Number(_2)&&_2!=0),_7=(this.vertical&&_5)||(!this.vertical&&_6),_8=(this.vertical&&_6)||(!this.vertical&&_5);for(var i=0;i<_4.length;i++){var _10=this.getImage(_4[i].name);if(_10==null)continue;var _11=this.oversizeStretchImg&&(this.vertical?_4[i].height==isc.star:_4[i].width==isc.star),_12=_11?_10.parentNode:null;if(_7){var _13=this.vertical?this.getWidth():this.getHeight();this.$z6(_10.style,this.vertical?this.$s7:this.$s6,_13);if(_11&&_12!=null){this.$z6(_12.style,this.vertical?this.$s7:this.$s6,_13)}}
if(_8){var _13=this.$125[i];if(_11&&_12!=null){this.$z6(_12.style,this.vertical?this.$s6:this.$s7,_13);_13+=2}
this.$z6(_10.style,this.vertical?this.$s6:this.$s7,_13)}}}
,isc.A.resizeImages=function isc_StretchImg_resizeImages(){if(this.$8d)return;var _1=(this.vertical?this.$s6:this.$s7),_2=this.items,_3=this.$125;if(_3==null)_3=this.$125=[];_3.length=_2.length;for(var i=0;i<_2.length;i++){_3[i]=_2[i][_1]}
isc.Canvas.applyStretchResizePolicy(_3,this.getImgLength(),1,true,this)}
,isc.A.getInnerHTML=function isc_StretchImg_getInnerHTML(){var _1=this.items,_2=_1.length,_3=this.vertical;if(this.$126||!this.$125||(this.autoCalculateSizes&&!this.cacheImageSizes))this.resizeImages();delete this.$126;var _4=this.$125,_5=(_3?this.getImgBreadth():this.getImgLength()),_6=(_3?this.getImgLength():this.getImgBreadth()),_7=isc.SB.create();if(this.logIsDebugEnabled(this.$r5)){this.logDebug("drawing with imageType: '"+this.imageType+"' and sizes "+this.$125,"drawing")}
var _8=!_3&&this.isRTL();if(this.imageType==isc.Img.TILE){_7.append("<TABLE CELLSPACING=0 CELLPADDING=0 BORDER=0 WIDTH=",_5," HEIGHT=",_6,"><TBODY>",(_3?"":"<TR>"));for(var j=0;j<_2;j++){var i=_8?_2-j-1:j;var _11=_4[i];if(_11>0){var _12=_1[i],_13=this.getImgURL(this.$127(_12));if(_3){_7.append("<TR><TD WIDTH=",_5," HEIGHT=",_11,_12.name?(" NAME=\""+this.getCanvasName()+_12.name+"\""):null," BACKGROUND=\"",_13,"\" class=\"",this.getItemStyleName(_12),"\">",isc.Canvas.spacerHTML(1,_11),"</TD></TR>")}else{_7.append("<TD WIDTH=",_11," HEIGHT=",_6,_12.name?(" NAME=\""+this.getCanvasName()+_12.name+"\""):null," BACKGROUND=\"",_13,"\" class=\"",this.getItemStyleName(_12),"\">",isc.Canvas.spacerHTML(_11,1),"</TD>")}}}
_7.append((_3?"":"</TR>"),"</TABLE>")}else if(this.imageType==isc.Img.CENTER){_7.append("<TABLE CELLSPACING=0 CELLPADDING=0 BORDER=0 WIDTH=",_5," HEIGHT=",_6,"><TBODY>",(_3?"":"<TR VALIGN=center>"));for(var j=0;j<_2;j++){var i=_8?_2-j-1:j;var _11=_4[i];if(_11>0){var _12=_1[i],_13=this.$127(_12);if(_3){_7.append("<TR VALIGN=center><TD WIDTH=",_5," HEIGHT=",_11," ALIGN=center"," class=\"",this.getItemStyleName(_12),"\">",this.imgHTML(_13,null,null,_12.name),"</TD></TR>")}else{_7.append("<TD WIDTH=",_11," HEIGHT=",_6," ALIGN=center"," class=\"",this.getItemStyleName(_12),"\">",this.imgHTML(_13,null,null,_12.name),"</TD>")}}}
_7.append((_3?"":"</TR>"),"</TABLE>")}else{var _14=this.renderStretchImgInTable;if(_14)_7.append(this.$12p);else if(!_3)_7.append(this.$12v);var _15=[" class=",null," "];for(var j=0;j<_2;j++){var i=_8?_2-j-1:j;var _16=(j==0);var _17=(j==_2-1);var _11=_4[i];if(_11>0){var _12=_1[i],_13=this.$127(_12),_18;var _18;if(!_14){var _19=this.getItemStyleName(_12);if(_19){_15[1]=_19;_18=_15.join(isc.emptyString)}else{_18=isc.emptyString}}
if(!_3){if(_14){_7.append(_16?this.$12z:this.$121);_7.append(this.getItemStyleName(_12));_7.append(this.$122)}
var _20=_11,_21=(this.oversizeStretchImg&&(_12.width==isc.star));if(_21){_7.append("<div style='overflow:hidden;width:",_11,"px;height:",_6,"px;'>")
_20=_11+2}
_7.append(this.imgHTML(_13,_20,_6,_12.name,_18));if(_21){_7.append("</div>")}
if(_14)_7.append(_17?this.$120:this.$123)}else{if(_14){_7.append(this.$12z);_7.append(this.getItemStyleName(_12));_7.append(this.$122)}
var _22=_11,_21=(this.oversizeStretchImg&&(_12.width==isc.star));if(_21){_7.append("<div style='overflow:hidden;height:",_11,"px;width:",_5,"px;'>")
_22=_11+2}
_7.append(this.imgHTML(_13,_5,_22,_12.name,isc.Browser.isDOM?((_18?_18:"")+this.$12y):_18));if(_21){_7.append("</div>")}
if(_14)_7.append(this.$120);else if(!isc.Browser.isDOM&&i<_2-1)_7.append(this.$12x)}}}
if(_14)_7.append(this.$12u)
else if(!_3)_7.append(this.$12w)}
return _7.toString()}
,isc.A.getItemStyleName=function isc_StretchImg_getItemStyleName(_1){var _2=_1.baseStyle||this.itemBaseStyle;if(!_2)return null;var _3=_1.state?_1.state:this.getState(),_4=_1.selected!=null?_1.selected:this.selected,_5=this.showFocused&&!this.showFocusedAsOver&&!this.isDisabled()?(_1.focused!=null?_1.focused:this.focused):false;return _2+this.$82(_3,_4?isc.StatefulCanvas.SELECTED:null,_5?isc.StatefulCanvas.FOCUSED:null)}
,isc.A.$127=function isc_StretchImg__getItemURL(_1){if(_1.src)return _1.src;if(_1.name==this.$124)return isc.Canvas.$11;return this.getURL(_1.name,(_1.state?_1.state:this.getState()),(_1.selected!=null?_1.selected:this.selected),(this.showFocused&&!this.showFocusedAsOver&&!this.isDisabled()?(_1.focused!=null?_1.focused:this.focused):false))}
,isc.A.setState=function isc_StretchImg_setState(_1,_2){if(_2==null){var _3=this.items.clearProperty("state"),_4=this.state!=_1;this.Super("setState",[_1]);if(_3&&!_4)this.stateChanged()}else{var _5=this.getPart(_2);if(_5){if(_5.state==_1)return;_5.state=_1}
this.stateChanged()}}
,isc.A.stateChanged=function isc_StretchImg_stateChanged(_1){this.Super("stateChanged");if(!this.isDrawn())return;if(this.imageType==isc.Img.TILE||this.$125==null){this.markForRedraw("setState (tiled images)")}else{if(isc.Browser.isWin2k&&isc.Browser.isIE){this.markForRedraw("Win2k IE image state change");return}
var _2=0;for(var i=0;i<this.items.length;i++){if(this.$125[i]>0){var _4=this.items[i];if((!_1||_4.name==_1)&&_4.name!=this.$124){this.setImage(_4.name,this.$127(_4));var _5=this.getImage(_4.name);if(_5){if(isc.Browser.isMoz){_5=_5.parentNode}
_5.className=this.getItemStyleName(_4)}}}else{_2++}}}}
,isc.A.setSrc=function isc_StretchImg_setSrc(_1){if(_1==null||this.src==_1)return;this.src=_1;this.markForRedraw()}
,isc.A.inWhichPart=function isc_StretchImg_inWhichPart(){if(this.vertical){var _1=this.inWhichPosition(this.$125,this.getOffsetY())}else{var _1=this.inWhichPosition(this.$125,this.getOffsetX(),this.getTextDirection())}
var _2=this.items[_1];return(_2?_2.name:null)}
);isc.B._maxIndex=isc.C+16;isc.defineClass("Label","Button");isc.A=isc.Label.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.align=isc.Canvas.LEFT;isc.A.wrap=true;isc.A.showTitle=false;isc.A.height=null;isc.A.width=null;isc.A.overflow="visible";isc.A.canFocus=false;isc.A.styleName="normal";isc.A.baseStyle=null;isc.A.cursor="default";isc.A.showRollOver=false;isc.A.showFocus=false;isc.A.showDown=false;isc.A.showDisabled=false;isc.A.useContents=true;isc.B.push(isc.A.setStyleName=function isc_Label_setStyleName(_1){this.setBaseStyle(_1)}
);isc.B._maxIndex=isc.C+1;isc.ClassFactory.defineClass("Progressbar","StretchImg");isc.A=isc.Progressbar.getPrototype();isc.A.percentDone=0;isc.A.length=100;isc.A.breadth=20;isc.A.vertical=false;isc.A.skinImgDir="images/Progressbar/";isc.A.src="[SKIN]progressbar.gif";isc.A.cacheImageSizes=false;isc.A.backgroundColor="CCCCCC";isc.A.verticalItems=[{name:"v_empty_end",size:3},{name:"v_empty_stretch",size:0},{name:"v_empty_start",size:3},{name:"v_end",size:3},{name:"v_stretch",size:0},{name:"v_start",size:3}];isc.A.horizontalItems=[{name:"h_start",size:3},{name:"h_stretch",size:0},{name:"h_end",size:3},{name:"h_empty_start",size:3},{name:"h_empty_stretch",size:0},{name:"h_empty_end",size:3}];isc.A=isc.Progressbar.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.initWidget=function isc_Progressbar_initWidget(){if(this.vertical){this.setWidth(this.breadth);this.setHeight(this.length);this.items=this.verticalItems}else{this.setWidth(this.length);this.setHeight(this.breadth);this.items=this.horizontalItems}
this.Super(this.$sb)}
,isc.A.resizeImages=function isc_Progressbar_resizeImages(){var _1=this.getLength(),_2=this.items,_3=this.$125=[],_4=this.percentDone;if(this.vertical){_3[0]=(_4<100?_2[0].size:0);_3[2]=(_4<100?_2[2].size:0);_3[3]=(_4>0?_2[3].size:0);_3[5]=(_4>0?_2[5].size:0)}else{_3[0]=(_4>0?_2[0].size:0);_3[2]=(_4>0?_2[2].size:0);_3[3]=(_4<100?_2[3].size:0);_3[5]=(_4<100?_2[5].size:0)}
_1-=_3[0]+_3[2]+_3[3]+_3[5];if(this.vertical){_3[4]=Math.ceil(_1*_4/ 100);_3[1]=Math.floor(_1*(100-_4)/100)}else{_3[1]=Math.ceil(_1*_4/ 100);_3[4]=Math.floor(_1*(100-_4)/100)}}
,isc.A.setPercentDone=function isc_Progressbar_setPercentDone(_1){if(this.percentDone==_1)return;_1=Math.min(100,(Math.max(0,_1)));this.percentDone=_1;if(this.isDrawn())this.markForRedraw("percentDone updated");this.percentChanged()}
,isc.A.percentChanged=function isc_Progressbar_percentChanged(){}
,isc.A.getLength=function isc_Progressbar_getLength(){return this.vertical?this.getHeight():this.getWidth()}
,isc.A.getBreadth=function isc_Progressbar_getBreadth(){return this.vertical?this.getWidth():this.getHeight()}
,isc.A.setLength=function isc_Progressbar_setLength(_1){this.length=_1;this.vertical?this.setHeight(_1):this.setWidth(_1)}
,isc.A.setBreadth=function isc_Progressbar_setBreadth(_1){this.breadth=_1;this.vertical?this.setWidth(_1):this.setHeight(_1)}
);isc.B._maxIndex=isc.C+8;isc.ClassFactory.defineClass("Rangebar","Progressbar");isc.A=isc.Rangebar.getPrototype();isc.A.value=0;isc.A.minValue=0;isc.A.maxValue=99;isc.A.title="";isc.A.vertical=true;isc.A.showTitle=true;isc.A.showRange=true;isc.A.showValue=true;isc.A.allLabelDefaults={width:50,height:20,spacing:5};isc.A.titleLabelDefaults={width:100,className:"rangebarTitle"};isc.A.rangeLabelDefaults={className:"rangebarRange"};isc.A.valueLabelDefaults={className:"rangebarValue"};isc.A.forceOverrides={$k6:false,autoDraw:false};isc.A.flipValues=false;isc.A=isc.Rangebar.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.initWidget=function isc_Rangebar_initWidget(){this.Super(this.$sb);this.titleLabelDefaults=isc.addProperties({},this.allLabelDefaults,this.titleLabelDefaults);this.valueLabelDefaults=isc.addProperties({},this.allLabelDefaults,this.valueLabelDefaults);this.rangeLabelDefaults=isc.addProperties({},this.allLabelDefaults,this.rangeLabelDefaults);if(this.showRange){this.$128=this.addPeer(this.$129("min"));this.$13a=this.addPeer(this.$129("max"))}
if(this.showValue)this._valueLabel=this.addPeer(this.$13b());if(this.showTitle)this._titleLabel=this.addPeer(this.$13c());this.setValue(this.value)}
,isc.A.resized=function isc_Rangebar_resized(_1,_2){this.$13d()}
,isc.A.$13d=function isc_Rangebar__adjustPeerPositions(){if(this.showRange&&this.$128&&this.$13a){var _1=this.$13e("min");var _2=this.$13e("max");this.$128.moveTo(_1.left,_1.top);this.$13a.moveTo(_2.left,_2.top)}
if(this.showValue&&this._valueLabel){var _3=this.$13f();this._valueLabel.moveTo(_3.left,_3.top)}
if(this.showTitle&&this._titleLabel){var _3=this.$13g();this._titleLabel.moveTo(_3.left,_3.top)}}
,isc.A.$129=function isc_Rangebar__createRangeLabel(_1){var _2=this.$13e(_1);return isc.Label.newInstance({ID:this.getID()+"_"+_1+"Label",contents:(_1=="min"?(this.minValueLabel?this.minValueLabel:this.minValue):(this.maxValueLabel?this.maxValueLabel:this.maxValue))},this.rangeLabelDefaults,_2,this.forceOverrides)}
,isc.A.$13e=function isc_Rangebar__computeRangeLabelProperties(_1){var _2={},_3=this.rangeLabelDefaults,_4=((_1=="min"&&!this.flipValues)||(_1="max"&&this.flipValues));if(this.vertical){_2.left=this.left+this.width+_3.spacing,_2.align=isc.Canvas.LEFT;if(_4){_2.top=this.getTop()+this.getHeight()-_3.height;_2.valign=isc.Canvas.BOTTOM}else{_2.top=this.getTop();_2.valign=isc.Canvas.TOP}}else{_2.top=this.getTop()+this.getHeight()+_3.spacing,_2.valign=isc.Canvas.TOP;if(_4){_2.left=this.getLeft();_2.align=isc.Canvas.LEFT}else{_2.left=this.getLeft()+this.getWidth()-_3.width;_2.align=isc.Canvas.RIGHT}}
return _2}
,isc.A.$13c=function isc_Rangebar__createTitleLabel(){var _1=this.$13g();return isc.Label.newInstance({ID:this.getID()+"_titleLabel",contents:this.title},this.titleLabelDefaults,_1,this.forceOverrides)}
,isc.A.$13g=function isc_Rangebar__computeTitleLabelProperties(){var _1={};var _2=this.titleLabelDefaults;if(this.vertical){_1.left=this.left+this.width/ 2-_2.width/ 2;_1.top=this.top-_2.height-_2.spacing;_1.align=isc.Canvas.CENTER}else{_1.left=this.left-_2.width-_2.spacing;_1.top=this.top+this.getHeight()/2-_2.height/ 2;_1.align=isc.Canvas.RIGHT}
return _1}
,isc.A.$13b=function isc_Rangebar__createValueLabel(){var _1=this.$13f();return isc.Label.newInstance({ID:this.getID()+"_valueLabel",contents:this.value,mouseUp:"return false;",observes:[{source:this,message:"valueChanged",action:"observer.setContents(this.getValue())"}]},this.valueLabelDefaults,_1,this.forceOverrides)}
,isc.A.$13f=function isc_Rangebar__computeValueLabelProperties(){var _1={};var _2=this.valueLabelDefaults;if(this.vertical){_1.left=this.left-_2.width-_2.spacing;_1.top=this.top+this.getHeight()/2-_2.height/ 2;_1.align=isc.Canvas.RIGHT;_1.valign=isc.Canvas.CENTER}else{_1.left=this.left+this.width/ 2-_2.width/ 2;_1.top=this.top-_2.height-_2.spacing;_1.align=isc.Canvas.CENTER;_1.valign=isc.Canvas.BOTTOM}
return _1}
,isc.A.getValue=function isc_Rangebar_getValue(){return this.value}
,isc.A.setValue=function isc_Rangebar_setValue(_1){if(this.value==_1)return;if(_1>this.maxValue)_1=this.maxValue;else if(_1<this.minValue)_1=this.minValue;this.value=_1;this.percentDone=100*(this.value-this.minValue)/(this.maxValue-this.minValue);this.markForRedraw();this.valueChanged()}
,isc.A.valueChanged=function isc_Rangebar_valueChanged(){}
);isc.B._maxIndex=isc.C+12;isc.ClassFactory.defineClass("Toolbar","Layout");isc.A=isc.Toolbar.getPrototype();isc.A.vertical=false;isc.A.overflow=isc.Canvas.HIDDEN;isc.A.height=20;isc.A.buttonConstructor="Button";isc.A.canReorderItems=false;isc.A.canResizeItems=false;isc.A.canRemoveItems=false;isc.A.reorderOnDrop=true;isc.A.tabWithinToolbar=true;isc.A.allowButtonReselect=false;isc.A.buttonDefaults={click:function(){this.Super("click",arguments);this.parentElement.itemClick(this,this.parentElement.getButtonNumber(this))},doubleClick:function(){this.Super("doubleClick",arguments);this.parentElement.itemDoubleClick(this,this.parentElement.getButtonNumber(this))},setSelected:function(){var _1=this.isSelected();this.Super("setSelected",arguments);if(this.parentElement.allowButtonReselect||_1!=this.isSelected()){if(this.isSelected())this.parentElement.buttonSelected(this);else this.parentElement.buttonDeselected(this)}},dragAppearance:isc.EventHandler.NONE,setTabIndex:function(_1){this.Super("setTabIndex",arguments);this.$13h=null},setAccessKey:function(_1,_2){if(!_2)this.$13i=null;this.Super("setAccessKey",[_1])},focusChanged:function(_1){if(this.hasFocus&&this.parentElement.$13j){this.parentElement.$13j(this)}},$l5:function(_1,_2){if(this.parentElement.$l5){this.parentElement.$l5(_1,_2,this)}}};isc.A=isc.Toolbar.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.draw=function isc_Toolbar_draw(_1,_2,_3,_4){if(isc.$dd)arguments.$de=this;if(!this.readyToDraw())return this;if(!this.$13k)this.setButtons();this.invokeSuper(isc.Toolbar,"draw",_1,_2,_3,_4)}
,isc.A.keyPress=function isc_Toolbar_keyPress(){var _1=this.ns.EH.lastEvent.keyName;if(!this.tabWithinToolbar){if((this.vertical&&_1=="Arrow_Up")||(!this.vertical&&_1=="Arrow_Left")){this.$13l(false);return false}else if((this.vertical&&_1=="Arrow_Down")||(!this.vertical&&_1=="Arrow_Right")){this.$13l();return false}}
return this.Super("keyPress",arguments)}
,isc.A.$13l=function isc_Toolbar__focusInNextButton(_1,_2){_1=(_1!=false);var _3=(_2!=null?_2:this.getFocusButtonIndex());if(_3==null)_3=(_1?-1:this.buttons.length);_3+=_1?1:-1;while(_3>=0&&_3<this.buttons.length){var _4=this.getMembers()[_3];if(_4.$mb()){_4.focus();return true}
_3+=_1?1:-1}
return false}
,isc.A.getFocusButtonIndex=function isc_Toolbar_getFocusButtonIndex(){var _1=this.getButtons(),_2;for(var i=0;i<_1.length;i++){if(_1[i].hasFocus){_2=i;break}}
return _2}
,isc.A.$l5=function isc_Toolbar__focusInNextTabElement(_1,_2,_3){if(!isc.EH.targetIsMasked(this,_2)){var _4=_3?this.members.indexOf(_3):null;if(!this.tabWithinToolbar){if(_1&&_4==null){var _5=this.$13m;if(_5!=null)return this.fb.focus()}}else if(this.$13l(_1,_4))return}
return this.Super("$l5",arguments)}
,isc.A.$mb=function isc_Toolbar__canFocus(_1,_2,_3,_4){var _5=this.members;if(_5&&_5.length>0){for(var i=0;i<_5.length;i++){if(_5[i].$mb())return true}}
return this.invokeSuper(isc.Toolbar,"$mb",_1,_2,_3,_4)}
,isc.A.setFocus=function isc_Toolbar_setFocus(_1){if(!this.$0n())return;var _2=this.getFocusButtonIndex();if(!_1){if(_2!=null&&this.members)this.members[_2].setFocus(false)}else{if(_2!=null)return;if(this.$13m)this.$13m.setFocus(true);else this.$13l()}}
,isc.A.focusAtEnd=function isc_Toolbar_focusAtEnd(_1){_1=!!_1;var _2=(_1?-1:this.buttons.length);this.$13l(_1,_2)}
,isc.A.$13n=function isc_Toolbar__setButtonTabIndex(_1,_2){if(!_1.$13h&&(_1.$00()!=null||_1.$01()!=null))
{_1.$v7()}
_1.$13h=true;if(_1.tabIndex!=_2)_1.$0m(_2,false)}
,isc.A.updateMemberTabIndex=function isc_Toolbar_updateMemberTabIndex(){}
,isc.A.$04=function isc_Toolbar__slotChildrenIntoTabOrder(){}
,isc.A.$13o=function isc_Toolbar__setButtonAccessKey(_1,_2){_1.$13i=true;_1.setAccessKey(_2,true)}
,isc.A.setupButtonFocusProperties=function isc_Toolbar_setupButtonFocusProperties(){var _1=this.$13m;if((!_1||!isc.isA.Canvas(_1)||_1.visibility==isc.Canvas.HIDDEN)&&this.buttons.length>0)
{var _2;for(var i=0;i<this.members.length;i++){if(isc.isA.Canvas(this.members[i])&&this.members[i].visibility!=isc.Canvas.HIDDEN)
{_2=this.members[i];break}}
this.$13j(_2)
_1=this.$13m}
var _4;if(this.tabWithinToolbar){_4=this.getTabIndex()}else{_4=-1}
var _5=this.getButtons();for(var i=0;i<_5.length;i++){var _6=_5[i];if(_6!=_1&&(_6.tabIndex==null||_6.$xb))
{this.$13n(_6,_4)}}}
,isc.A.$13j=function isc_Toolbar__updateFocusButton(_1){if(this.$13m==_1){return}
if(_1.accessKey!=this.accessKey&&(_1.accessKey==null||_1.$13i))
{this.$13o(_1,this.accessKey)}
if(_1.tabIndex==null||_1.$xb||_1.$13h)
{this.$13n(_1,this.getTabIndex())}
var _2=this.$13m;if(_2!=null&&(_2.tabIndex==null||_2.$xb||_2.$13h))
{if(!this.tabWithinToolbar)this.$13n(_2,-1);if(_2.accessKey!=null&&_2.$13i)
{this.$13o(_2,null)}}
this.$13m=_1}
,isc.A.$0m=function isc_Toolbar__setTabIndex(_1,_2,_3,_4){this.invokeSuper(isc.Toolbar,"$0m",_1,_2,_3,_4);if(this.tabWithinToolbar){var _5=this.getButtons();for(var i=0;i<_5.length;i++){if(_5[i].tabIndex==null||_5[i].$xb||_5[i].$13h)
this.$13n(_5[i],this.getTabIndex())}}else{var _7=this.$13m;if(_7!=null){this.$13m=null;this.$13j(_7)}}}
,isc.A.setAccessKey=function isc_Toolbar_setAccessKey(_1){this.Super("setAccessKey",arguments);var _2=this.$13m;if(_2!=null){this.$13m=null;this.$13j(_2)}}
,isc.A.getLength=function isc_Toolbar_getLength(_1,_2,_3,_4){if(this.innerWidth!=null)return this.innerWidth;return this.invokeSuper(isc.Toolbar,"getLength",_1,_2,_3,_4)}
,isc.A.setButtons=function isc_Toolbar_setButtons(_1){this.$13k=true;if(_1)this.buttons=_1;if(this.members==null)this.members=[];var _2=this.members.duplicate();for(var i=0;i<_2.length;i++){var _4=_2[i];if(!this.buttons.contains(_4)){_2[i].destroy()}}
if(this.buttons==null)this.buttons=[];var _5=[];for(var i=0;i<this.buttons.length;i++){var _6=this.buttons[i];if(!isc.isA.Canvas(_6))_6=this.makeButton(_6);_5[_5.length]=_6;if(isc.isA.StatefulCanvas(_6)){var _7=_6.getActionType();if(_7==isc.StatefulCanvas.RADIO){if(_6.selected)this.lastSelectedButton=_6}}}
this.addMembers(_5,0);if(this.canResizeItems)this.setResizeRules();this.setupButtonFocusProperties()}
,isc.A.buttonShouldHiliteAccessKey=function isc_Toolbar_buttonShouldHiliteAccessKey(){if(this.$13i)return false;return this.hiliteAccessKey}
,isc.A.makeButton=function isc_Toolbar_makeButton(_1){_1.width=_1.width||null;_1.height=_1.height||null;_1.canDrag=this.canReorderItems||this.canDragSelectItems||this.canRemoveItems;_1.canDragResize=(_1.canDragResize!=null?_1.canDragResize&&this.canResizeItems:this.canResizeItems);_1.canAcceptDrop=this.canAcceptDrop;_1.canDrop=this.canRemoveItems;_1.shouldHiliteAccessKey=this.buttonShouldHiliteAccessKey;return this.$13p(_1,null)}
,isc.A.$13p=function isc_Toolbar__makeItem(_1,_2){var _3=(_1.buttonConstructor?_1.buttonConstructor:this.buttonConstructor);_3=this.ns.ClassFactory.getClass(_3);var _4=_3.newInstance({autoDraw:false},this.buttonDefaults,this.buttonProperties,_1,_2);if(!isc.isA.StatefulCanvas(_4))return _4;var _5;if((_4.getActionType()==isc.StatefulCanvas.RADIO&&_4.radioGroup===_5)||_4.defaultRadioGroup!=null){var _6=_4.defaultRadioGroup!=null?_4.defaultRadioGroup:this.getID();_4.addToRadioGroup(_6)}
return _4}
,isc.A.addButtons=function isc_Toolbar_addButtons(_1,_2){if(_1==null)return;if(!isc.isAn.Array(_1))_1=[_1];if(!this.$13k)this.setButtons();_1.removeEvery(null);var _3;if(isc.isAn.Array(_2)){if(_2.length!=_1.length){this.logWarn("addButtons passed "+_1.length+" buttons with "+_2.length+" discrete positions specified. Ignoring.");return}
var _4={};for(var i=0;i<_2.length;i++){_4[_2[i]]=_1[i]}
_2.sort();_3=[];var _6={buttons:[],position:_2[0]},_7=0;for(var i=0;i<_2.length;i++){var _8=_2[i],_9=_4[_8];_6.buttons.add(_9);var _10=_2[i+1]
if(_10==null||_10!=_8+1){_3[_7]=_6;_7++
if(_10!=null)_6={buttons:[],position:_10}}}
for(var i=0;i<_3.length;i++){this.buttons.addListAt(_3[i].buttons,_3[i].position)}}else{this.buttons.addListAt(_1,_2)}
var _11=this.instantRelayout;this.instantRelayout=false;var _12;if(_3==null){_12=this.$13q(_1);this.addMembers(_12,_2)}else{for(var i=0;i<_3.length;i++){var _13=this.$13q(_3[i].buttons);this.addMembers(_13,_3[i].position);if(_12==null)_12=_13;else _12.addList(_13)}}
if(_11){this.instantRelayout=true;if(this.$10v)this.$10v=false;this.reflow("addButtons")}
if(this.canResizeItems)this.setResizeRules();_12.map("show")}
,isc.A.$13q=function isc_Toolbar__createButtonInstances(_1){var _2=[];for(var i=0;i<_1.length;i++){var _4=_1[i],_5=isc.isA.Canvas(_4)?_4:this.makeButton(_4);_2[i]=_5}
return _2}
,isc.A.removeButtons=function isc_Toolbar_removeButtons(_1){if(_1==null)return;if(!isc.isAn.Array(_1))_1=[_1];var _2=[];for(var i=0;i<_1.length;i++){_1[i]=this.buttons[this.getButtonNumber(_1[i])];if(_1[i]==null){this.logWarn("removeButtons(): unable to find button for item number "+i+" in the array passed in.  Skipping this item.");_1.removeItem(i);i-=1;continue}
_2[i]=this.getButton(this.buttons.indexOf(_1[i]))}
var _4=this.buttons;_4.removeList(_1);this.removeMembers(_2)}
,isc.A.getButton=function isc_Toolbar_getButton(_1){_1=this.getButtonNumber(_1);return this.getMember(_1)}
,isc.A.getButtonNumber=function isc_Toolbar_getButtonNumber(_1){if(isc.isAn.Object(_1)&&!isc.isA.Canvas(_1))return this.buttons.indexOf(_1);return this.getMemberNumber(_1)}
,isc.A.getButtons=function isc_Toolbar_getButtons(){return this.members}
,isc.A.setCanResizeItems=function isc_Toolbar_setCanResizeItems(_1){if(this.canResizeItems==_1)return;this.canResizeItems=_1;var _2=this.getButtons();if(!_2)return;for(var i=0;i<_2.length;i++){var _4=_2[i];_4.canDragResize=(_4.canDragResize!=null?_4.canDragResize&&_1:_1)}
this.setResizeRules()}
,isc.A.setResizeRules=function isc_Toolbar_setResizeRules(){if(!this.members)return;var _1=this.isRTL();var _2,_3,_4;if(this.vertical){_2={"T":isc.Canvas.ROW_RESIZE,"B":isc.Canvas.ROW_RESIZE};_3=["T","B"];_4=["B"]}else{_2={"L":isc.Canvas.COL_RESIZE,"R":isc.Canvas.COL_RESIZE};_3=["L","R"];if(!_1){_4=["R"]}else{_4=["L"]}}
var _5=false;for(var i=0;i<this.members.length;i++){var _7=this.members[i];if(!_7.canDragResize){_7.resizeFrom=_7.edgeCursorMap=null;_5=true}else{if(_5||i==0)
{_7.resizeFrom=_4}else{_7.resizeFrom=_3}
_7.edgeCursorMap=_2;_5=false}}}
,isc.A.getSelectedButton=function isc_Toolbar_getSelectedButton(){return this.lastSelectedButton}
,isc.A.selectButton=function isc_Toolbar_selectButton(_1){if(!this.members)return;var _2=this.getButton(_1);if(_2&&isc.isA.StatefulCanvas(_2))_2.select()}
,isc.A.deselectButton=function isc_Toolbar_deselectButton(_1){var _2=this.getButton(_1);if(_2)_2.deselect()}
,isc.A.buttonSelected=function isc_Toolbar_buttonSelected(_1){if(_1.getActionType()==isc.Button.RADIO){this.lastSelectedButton=_1}}
,isc.A.buttonDeselected=function isc_Toolbar_buttonDeselected(_1){}
,isc.A.itemClick=function isc_Toolbar_itemClick(_1,_2){}
,isc.A.itemDoubleClick=function isc_Toolbar_itemDoubleClick(_1,_2){}
,isc.A.getMouseOverButtonIndex=function isc_Toolbar_getMouseOverButtonIndex(){var _1=this.vertical?this.getOffsetY():this.getOffsetX();return this.inWhichPosition(this.memberSizes,_1,this.getTextDirection())}
,isc.A.prepareForDragging=function isc_Toolbar_prepareForDragging(){var _1=this.ns.EH;var _2=_1.lastEvent.target;while(_2.dragTarget){_2=_2.dragTarget}
var _3=_1.dragOperation;if(((this.canResizeItems&&_3=="dragResize")||(this.canReorderItems&&_3=="drag"))&&this.members.contains(_2))
{if(_3=="dragResize"){if((this.vertical&&["T","B"].contains(_1.resizeEdge))||(!this.vertical&&["L","R"].contains(_1.resizeEdge)))
{_1.dragOperation="dragResizeMember";return}}else if(_3=="drag"){_1.dragOperation="dragReorder";return}}
return this.Super("prepareForDragging",arguments)}
,isc.A.getDropPosition=function isc_Toolbar_getDropPosition(){var _1=this.getMouseOverButtonIndex();var _2=this.ns.EH,_3=(this.reorderStyle=="explorer"||(_2.dropTarget&&_2.dropTarget.parentElement==this));if(_3&&_1>=0){var _4=this.memberSizes[_1],_5=(this.vertical?this.getOffsetY():this.getOffsetX());_5-=this.memberSizes.slice(0,_1).sum();var _6=_1;if(_5>_4/ 2)_1++}
var _7=this.members.length,_8=(_3?_7:_7-1);if(_1==-2&&this.containsEvent())return _8;var _9=this.dragStartPosition||0;if(_1<0||_1>_8)_1=_9;else if(_2.dragTarget&&_2.dragTarget.parentElement==this&&(this.members[_1]&&this.members[_1].canReorder==false))
{_1=_9}
return _1}
,isc.A.dragReorderStart=function isc_Toolbar_dragReorderStart(){var _1=this.ns.EH,_2=_1.dragTarget;if(_2.canReorder==false)return false;if(_2.showDown)_2.setState(isc.StatefulCanvas.STATE_DOWN);this.dragStartPosition=this.getButtonNumber(_2);return _1.STOP_BUBBLING}
,isc.A.dragReorderMove=function isc_Toolbar_dragReorderMove(){var _1=this.ns.EH,_2=_1.dragTarget,_3=this.dragStartPosition,_4=this.getDropPosition();this.dragCurrentPosition=_4;var _5=this.members.duplicate();_5.slide(_3,_4);this.stackMembers(_5,null,false);return _1.STOP_BUBBLING}
,isc.A.dragReorderStop=function isc_Toolbar_dragReorderStop(){var _1=this.ns.EH,_2=_1.dragTarget,_3=this.dragStartPosition,_4=this.dragCurrentPosition;_2.setState(isc.StatefulCanvas.STATE_UP);if(_4==_3)return false;if(this.reorderOnDrop)this.reorderItem(_4,_3);if(this.itemDragReordered)this.itemDragReordered(_3,_4);return _1.STOP_BUBBLING}
,isc.A.dragStop=function isc_Toolbar_dragStop(){var _1=this.ns.EH,_2=_1.dragTarget,_3=this.dragStartPosition;_2.setState(isc.StatefulCanvas.STATE_UP);this.hideDropLine();return _1.STOP_BUBBLING}
,isc.A.reorderItem=function isc_Toolbar_reorderItem(_1,_2){this.reorderItems(_1,_1+1,_2)}
,isc.A.reorderItems=function isc_Toolbar_reorderItems(_1,_2,_3){this.buttons.slideRange(_1,_2,_3);this.reorderMembers(_1,_2,_3);this.setResizeRules()}
,isc.A.dragResizeMemberStart=function isc_Toolbar_dragResizeMemberStart(){var _1=this.ns.EH,_2=_1.dragTarget,_3=this.getButtonNumber(_2),_4=this.isRTL();var _5=false;if((!_4&&_1.resizeEdge=="L")||(_4&&_1.resizeEdge=="R")){_5=true;_3--;_1.resizeEdge=(_4?"L":"R")}else if(_1.resizeEdge=="T"){_5=true;_3--;_1.resizeEdge="B"}
if(_3<0||_3>=this.members.length||_2==null)return false;_1.dragTarget=_2=this.members[_3];_2.$13r=_2.canDrop;_2.canDrop=false;this.$13s=_3;if(_2.showDown)_2.setState(isc.StatefulCanvas.STATE_DOWN);if(_5){var _6=this.members[_3+1];if(_6)_6.setState(isc.StatefulCanvas.STATE_UP)}
return _1.STOP_BUBBLING}
,isc.A.dragResizeMemberMove=function isc_Toolbar_dragResizeMemberMove(){var _1=this.ns.EH,_2=_1.dragTarget;_2.resizeToEvent();_2.redrawIfDirty("dragResize");return _1.STOP_BUBBLING}
,isc.A.dragResizeMemberStop=function isc_Toolbar_dragResizeMemberStop(){var _1=this.ns.EH,_2=_1.dragTarget;_2.canDrop=_2.$13r;_2.setState(isc.StatefulCanvas.STATE_UP);_2.resizeToEvent();var _3=(this.vertical?_2.getHeight():_2.getWidth());this.resizeItem(this.$13s,_3);if(this.itemDragResized)this.itemDragResized(this.$13s,_3);return _1.STOP_BUBBLING}
,isc.A.resizeItem=function isc_Toolbar_resizeItem(_1,_2){var _3=this.members[_1];if(this.vertical)_3.setHeight(_2);else _3.setWidth(_2)}
);isc.B._maxIndex=isc.C+49;isc.Toolbar.registerStringMethods({itemClick:"item,itemNum",itemDragResized:"itemNum,newSize",itemDragReordered:"itemNum,newPosition"});isc.defineClass("ImgButton","Img");isc.A=isc.ImgButton.getPrototype();isc.A.baseStyle="imgButton";isc.A.showDown=true;isc.A.showFocused=true;isc.A.showRollOver=true;isc.A.showTitle=false;isc.A.cursor=isc.Button.$ce.cursor;isc.A.src="[SKIN]/ImgButton/button.png";isc.A.canFocus=true;isc.A.overflow=isc.Canvas.HIDDEN;isc.defineClass("StretchImgButton","StretchImg");isc.A=isc.StretchImgButton.getPrototype();isc.A.useEventParts=true;isc.A.baseStyle="stretchImgButton";isc.A.showDown=true;isc.A.showFocused=true;isc.A.showRollOver=true;isc.A.showTitle=true;isc.A.hiliteAccessKey=true;isc.A.src="[SKIN]/button/button.png";isc.A.vertical=false;isc.A.capSize=12;isc.A.autoFitDirection="horizontal";isc.A.cursor=isc.Button.$ce.cursor;isc.A.canFocus=true;isc.StretchImgButton.registerStringMethods({iconClick:""})
isc.defineClass("ToolStrip","Layout");isc.A=isc.ToolStrip.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.height=20;isc.A.defaultWidth=250;isc.A.styleName="toolStrip";isc.A.vertical=false;isc.A.resizeBarClass="ToolStripResizer";isc.A.resizeBarSize=14;isc.A.separatorClass="ToolStripSeparator";isc.A.separatorSize=8;isc.A.showGroupTitle=true;isc.A.groupTitleAlign="center";isc.A.groupTitleOrientation="top";isc.A.groupConstructor="ToolStripGroup";isc.A.formWrapperConstructor="DynamicForm";isc.A.formWrapperDefaults={showTitle:false,numCols:1,overflow:"visible",width:1,height:1};isc.B.push(isc.A.initWidget=function isc_ToolStrip_initWidget(_1,_2,_3,_4,_5,_6){this.members=this.$13t(this.members);this.invokeSuper(isc.ToolStrip,this.$sb,_1,_2,_3,_4,_5,_6);if(this.vertical&&this.verticalStyleName!=null){this.setStyleName(this.verticalStyleName)}}
,isc.A.$13t=function isc_ToolStrip__convertMembers(_1){var _2=isc.ClassFactory.getClass(this.separatorClass);if(_1==null)return null;var _3=[];for(var i=0;i<_1.length;i++){var m=_1[i];if(m=="separator"){var _6=_2.createRaw();_6.autoDraw=false;_6.vertical=!this.vertical;if(this.vertical){_6.height=this.separatorSize}else{_6.width=this.separatorSize}
_6.completeCreation();_3.add(_6)}else if(m=="resizer"&&i>0){_1[i-1].showResizeBar=true}else if(m=="starSpacer"){_3.add(isc.LayoutSpacer.create({width:"*"}))}else if(isc.isA.ToolStripResizer(m)&&i>0){_1[i-1].showResizeBar=true;m.destroy()}else{if(isc.isA.ToolStripSeparator(m)){var _6=m;_6.vertical=!this.vertical;_6.setSrc(this.vertical?_6.hSrc:_6.vSrc);if(this.vertical){_6.setHeight(this.separatorSize)}else{_6.setWidth(this.separatorSize)}
_6.markForRedraw()}else if(isc.isA.ToolStripGroup(m)){if(!m.showTitle)m.setShowTitle(this.showGroupTitle);if(!m.titleAlign)m.setTitleAlign(this.groupTitleAlign);if(!m.titleOrientation)m.setTitleOrientation(this.groupTitleOrientation)}
_3.add(m)}}
return _3}
,isc.A.addMembers=function isc_ToolStrip_addMembers(_1,_2,_3,_4,_5){if(!_1)return;if(!isc.isAn.Array(_1))_1=[_1];var _6=_1[0],_7=isc.isA.ToolStripResizer(_6);if(_6=="resizer"||_7){_2=_2||this.members.length;var _8=Math.min(_2,this.members.length)-1;if(_8>0){var _9=this.getMember(_8);if(_9!=null){_9.showResizeBar=true;this.reflow()}}
var _10=_1.shift();if(_7)_10.destroy()}
_1=this.$13t(_1);return this.invokeSuper(isc.ToolStrip,"addMembers",_1,_2,_3,_4,_5)}
,isc.A.addToolStripGroup=function isc_ToolStrip_addToolStripGroup(_1,_2){if(!_1)return null;if(!isc.isA.Class(_1)){var _3=this.groupConstructor;if(isc.isA.String(_3))_3=isc.ClassFactory.getClass(this.groupConstructor);_1=_3.create(_1)}
if(!_1||!isc.isA.ToolStripGroup(_1))return null;if(_1.showTitle==null)_1.setShowTitle(this.showGroupTitle);if(!_1.titleAlign)_1.setTitleAlign(this.groupTitleAlign);if(!_1.titleOrientation)_1.setTitleOrientation(this.groupTitleOrientation);this.addMember(_1,_2);return _1}
,isc.A.addFormItem=function isc_ToolStrip_addFormItem(_1,_2,_3){if(isc.isA.Canvas(_1)){this.addMember(_1,_3);return _1}
var _4=this.createAutoChild("formWrapper",_2);_4.setItems([_1]);this.addMember(_4,_3);return _4}
);isc.B._maxIndex=isc.C+5;isc.defineClass("ToolStripSeparator","Img");isc.A=isc.ToolStripSeparator.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.skinImgDir="images/ToolStrip/";isc.A.vSrc="[SKIN]separator.png";isc.A.hSrc="[SKIN]hseparator.png";isc.A.layoutAlign="center";isc.B.push(isc.A.initWidget=function isc_ToolStripSeparator_initWidget(){if(isc.isA.Img(this))this.src=this.vertical?this.vSrc:this.hSrc;this.Super("initWidget",arguments)}
);isc.B._maxIndex=isc.C+1;isc.defineClass("ToolStripButton","StretchImgButton");isc.A=isc.ToolStripButton.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.showTitle=true;isc.A.showRollOver=true;isc.A.showDown=true;isc.A.labelHPad=6;isc.A.labelVPad=0;isc.A.autoFit=true;isc.A.src="[SKIN]/ToolStrip/button/button.png";isc.A.capSize=3;isc.A.height=22;isc.B.push(isc.A.initWidget=function isc_ToolStripButton_initWidget(){if(!this.title)this.iconSpacing=0;this.Super("initWidget",arguments)}
,isc.A.setTitle=function isc_ToolStripButton_setTitle(_1){if(!_1){this.iconSpacing=0;if(this.label)this.label.iconSpacing=0}
this.Super("setTitle",arguments)}
);isc.B._maxIndex=isc.C+2;isc.defineClass("ToolStripGroup","VLayout");isc.A=isc.ToolStripGroup.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.styleName="toolStripGroup";isc.A.layoutMargin=2;isc.A.membersMargin=1;isc.A.layoutAlign="top";isc.A.autoDraw=false;isc.A.height=1;isc.A.width=1;isc.A.overflow="visible";isc.A.labelLayoutDefaults={_constructor:"HLayout",width:"100%",height:22};isc.A.labelConstructor="Label";isc.A.labelDefaults={width:"100%",height:22,autoDraw:true,wrap:false,overflow:"hidden",border:"none"};isc.A.titleStyle="toolStripGroupTitle";isc.A.bodyConstructor="HLayout";isc.A.bodyDefaults={width:"*",height:"*",overflow:"visible",membersMargin:2,autoDraw:false};isc.A.columnLayoutDefaults={_constructor:"VLayout",width:1,membersMargin:2,height:"100%",overflow:"visible",autoDraw:false,numRows:0,addMember:function(_1,_2){this.Super("addMember",arguments);if(_1.rowSpan==null)_1.rowSpan=1;var _3=_1.rowSpan*this.creator.rowHeight+((_1.rowSpan-1)*this.membersMargin);if(_1.orientation=="vertical"){_1.rowSpan=this.maxRows;_3=(_1.rowSpan*this.creator.rowHeight)+((this.maxRows-1)*this.membersMargin)}
_1.setHeight(_3);this.numRows+=_1.rowSpan;if(this.numRows>this.maxRows)this.numRows=this.maxRows},removeMember:function(_1){this.Super("removeMember",arguments);if(_1.rowSpan==null)_1.rowSpan=1;this.numRows-=_1.rowSpan;_1.markForDestroy();_1=null}};isc.A.numRows=1;isc.A.rowHeight=20;isc.A.defaultColWidth="*";isc.A.titleHeight=18;isc.A.autoHideOnLastRemove=false;isc.B.push(isc.A.initWidget=function isc_ToolStripGroup_initWidget(){this.Super("initWidget",arguments);var _1=this.showTitle!=false&&this.showLabel!=false;if(_1){this.addAutoChild("labelLayout",{height:this.titleHeight});this.addAutoChild("label",isc.addProperties({},this.titleProperties||{},{styleName:this.titleStyle,height:this.titleHeight,align:this.titleAlign,contents:this.title,autoDraw:false}));this.labelLayout.addMember(this.label);if(this.showTitle==false)this.labelLayout.hide();this.addMember(this.labelLayout)}
this.addAutoChild("body",{_constructor:this.bodyConstructor,height:this.numRows*this.rowHeight});this.addMember(this.body,_1?(this.titleOrientation=="bottom"?0:1):0);if(this.controls){this.addControls(this.controls,false)}}
,isc.A.setTitle=function isc_ToolStripGroup_setTitle(_1){if(this.label)this.label.setContents(_1)}
,isc.A.setShowTitle=function isc_ToolStripGroup_setShowTitle(_1){this.showTitle=_1;if(!_1&&this.labelLayout.isVisible())this.labelLayout.hide();else if(_1&&!this.labelLayout.isVisible())this.labelLayout.show()}
,isc.A.setTitleAlign=function isc_ToolStripGroup_setTitleAlign(_1){this.titleAlign=_1;if(this.label)this.label.setAlign(this.titleAlign)}
,isc.A.setTitleOrientation=function isc_ToolStripGroup_setTitleOrientation(_1){this.titleOrientation=_1;if(this.label&&this.labelLayout){if(this.titleOrientation=="top"){this.removeMember(this.labelLayout);this.addMember(this.labelLayout,0)}else if(this.titleOrientation=="bottom"){this.removeMember(this.labelLayout);this.addMember(this.labelLayout,1)}}}
,isc.A.addColumn=function isc_ToolStripGroup_addColumn(_1,_2){var _3;if(_1===null||_1===_3){_1=this.body.members.length}
var _4=this.defaultColWidth;if(this.colWidths&&this.colWidths[_1]!=null)_4=this.colWidths[_1];var _5=this.createAutoChild("columnLayout",{maxRows:this.numRows,numRows:0,width:_4,height:this.body.getVisibleHeight()-1});this.body.addMember(_5,_1);if(_2)_5.addMembers(_2);return _5}
,isc.A.getAvailableColumn=function isc_ToolStripGroup_getAvailableColumn(_1){var _2=this.body.members;if(_2&&_2.length>0){for(var i=0;i<_2.length;i++){var _4=_2[i];if(_4.numRows<_4.maxRows)return _4}}
if(_1!=false)return this.addColumn();return null}
,isc.A.getControlColumn=function isc_ToolStripGroup_getControlColumn(_1){var _2=this.body.members;if(_2&&_2.length>0){for(var i=_2.length-1;i>=0;i--){if(_2[i].members.contains(_1))return _2[i]}}
return null}
,isc.A.setControls=function isc_ToolStripGroup_setControls(_1,_2){if(this.controls){this.removeAllControls()}
this.addControls(_1,_2)}
,isc.A.addControls=function isc_ToolStripGroup_addControls(_1,_2){if(!_1)return;if(!isc.isAn.Array(_1))_1=[_1];for(var i=0;i<_1.length;i++){this.addControl(_1[i],null,_2)}}
,isc.A.addControl=function isc_ToolStripGroup_addControl(_1,_2,_3){if(!_1)return null;var _4;if(_2===null||_2===_4||_2>=this.numRows)_2=this.numRows-1;var _5=this.getAvailableColumn(true);if(!this.controls)this.controls=[];if(_3!=false)this.controls.add(_1);_5.addMember(_1,_2);_5.reflowNow()}
,isc.A.removeControl=function isc_ToolStripGroup_removeControl(_1){_1=isc.isAn.Object(_1)?_1:this.getMember(_1);if(!_1)return null;var _2=this.getControlColumn(_1);if(_2){_2.removeMember(_1);this.controls.remove(_1);if(_2.members.length<=1){_2.hide();this.body.removeMember(_2);_2.markForDestroy();_2=null}}
if(this.body.members.length==0&&this.autoHideOnLastRemove){this.hide()}}
,isc.A.removeAllControls=function isc_ToolStripGroup_removeAllControls(){if(!this.controls||this.controls.length==0)return null;for(var i=this.controls.length-1;i>=0;i--){var _2=this.controls[i];_2.hide();this.removeControl(_2);_2.markForDestroy();_2=null}}
,isc.A.resized=function isc_ToolStripGroup_resized(){this.$13u()}
,isc.A.draw=function isc_ToolStripGroup_draw(){this.Super("draw",arguments);this.$13u()}
,isc.A.redraw=function isc_ToolStripGroup_redraw(){this.Super("redraw",arguments);this.$13u()}
,isc.A.$13u=function isc_ToolStripGroup__updateLabel(){var _1=this.getVisibleWidth(),_2=this.layoutMargin,_3=this.getVisibleWidth()-(this.layoutMargin*3);this.labelLayout.setWidth(_3);this.label.setWidth(_3)}
);isc.B._maxIndex=isc.C+17;isc.defineClass("IconButton","Button");isc.A=isc.IconButton.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.width=1;isc.A.overflow="visible";isc.A.height=1;isc.A.padding=3;isc.A.autoDraw=false;isc.A.usePartEvents=true;isc.A.orientation="horizontal";isc.A.baseStyle="iconButton";isc.A.showMenuIcon=false;isc.A.menuIconSrc="[SKINIMG]/Menu/submenu_down.png";isc.A.menuIconOverSrc="[SKINIMG]/Menu/submenu_downOver.png";isc.A.menuIconWidth=14;isc.A.menuIconHeight=13;isc.A.menuIconStyleCSS="vertical-align:middle; border:1px solid transparent; -moz-border-radius: 3px; "+"-webkit-border-radius: 3px; -khtml-border-radius: 3px; border-radius: 3px;";isc.A.menuIconOverBorderCSS="1px solid #A7ABB4";isc.A.menuConstructor=isc.Menu;isc.A.iconSize=16;isc.A.largeIconSize=32;isc.A.showMenuIconOver=false;isc.B.push(isc.A.initWidget=function isc_IconButton_initWidget(){if(this.orientation=="vertical"){this.align="center";this.valign="top"}else{this.align="left";this.valign="center"}
this.$13v=this.title;this.$13w=this.icon;this.Super("initWidget",arguments)}
,isc.A.getTitle=function isc_IconButton_getTitle(){var _1=this.orientation=="vertical",_2=(_1?this.largeIcon||this.$13w:this.$13w),_3=(_1?this.largeIconSize:this.iconSize),_4=this.$13v;if(this.showDisabledIcon&&this.disabled){var _5=_2.lastIndexOf("."),_6=_2.substring(0,_5)+"_Disabled"+_2.substring(_5);_2=_6}
var _7="vertical-align:middle;"+(_1?"margin-bottom:5px;":""),_8=this.menuIconStyleCSS+(_1?"margin-top:4px;":""),_9=this.imgHTML(_2,_3,_3,null," style='"+_7+"' eventpart='icon'"),_10=this.showMenuIcon?this.imgHTML(this.menuIconSrc,this.menuIconWidth,this.menuIconHeight,"menuIcon"," style='"+_8+"' eventpart='menuIcon' "):null;;if(this.orientation=="vertical"){this.icon=null;var _11="<span style='font-size:4px'><br></span>";_4=_9+"<br>"+_4;if(_10)_4+="<br>"+_10}else{if(this.showMenuIcon&&_10)_4+="&nbsp;"+_10}
this.title=_4;return _4}
,isc.A.mouseOut=function isc_IconButton_mouseOut(){this.Super("mouseOut",arguments);if(this.showingMenuButtonOver)this.menuIconMouseOut()}
,isc.A.menuIconClick=function isc_IconButton_menuIconClick(){}
,isc.A.menuIconMouseMove=function isc_IconButton_menuIconMouseMove(){if(!this.showMenuIconOver||this.showingMenuButtonOver)return;var _1=this.getImage("menuIcon");if(_1){this.showingMenuButtonOver=true;_1.style.border=this.menuIconOverBorderCSS}}
,isc.A.menuIconMouseOut=function isc_IconButton_menuIconMouseOut(){if(!this.showMenuIconOver)return;var _1=this.getImage("menuIcon");if(_1){this.showingMenuButtonOver=false;_1.style.border="1px solid transparent"}}
);isc.B._maxIndex=isc.C+6;isc.defineClass("IconMenuButton","IconButton");isc.A=isc.IconMenuButton.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.usePartEvents=true;isc.A.showMenuIcon=true;isc.A.menu=null;isc.B.push(isc.A.menuIconClick=function isc_IconMenuButton_menuIconClick(){this.showMenu()}
,isc.A.showMenu=function isc_IconMenuButton_showMenu(){if(isc.isA.String(this.menu))this.menu=window[this.menu];if(!isc.isA.Menu(this.menu))this.$13x(this.menu);if(!isc.isA.Menu(this.menu))return;var _1=this.menu;_1.$13y();var _2=this.getPageLeft();var _3=this.getPageTop()+this.getVisibleHeight()+1;_1.placeNear(_2,_3);_1.show(this.menuAnimationEffect)}
,isc.A.$13x=function isc_IconMenuButton__createMenu(_1){if(!_1)return;_1.autoDraw=false;var _2=this.menuConstructor||isc.Menu;this.menu=_2.create(_1)}
);isc.B._maxIndex=isc.C+3;isc.defineClass("RibbonBar","ToolStrip");isc.A=isc.RibbonBar.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.groupConstructor="RibbonGroup";isc.B.push(isc.A.addGroup=function isc_RibbonBar_addGroup(_1,_2){return this.addToolStripGroup(_1,_2)}
);isc.B._maxIndex=isc.C+1;isc.defineClass("RibbonGroup","ToolStripGroup");isc.A=isc.RibbonGroup.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.newControlConstructor="IconButton";isc.A.newControlDefaults={};isc.B.push(isc.A.createControl=function isc_RibbonGroup_createControl(_1,_2){var _3=this.createAutoChild("newControl",_1);return this.addControl(_3,_2)}
);isc.B._maxIndex=isc.C+1;isc.defineClass("SectionStack","VLayout");isc.addGlobal("ListBar",isc.SectionStack);isc.A=isc.SectionStack.getPrototype();isc.A.overflow="hidden";isc.A.styleName="sectionStack";isc.A.sectionHeaderClass="SectionHeader";isc.A.headerHeight=20;isc.A.printHeaderStyleName="printHeader";isc.A.canResizeSections=true;isc.A.canResizeStack=true;isc.A.canReorderSections=false;isc.A.scrollSectionIntoView=true;isc.A.useGlobalSectionIDs=false;isc.A.animateMemberEffect="wipe";isc.A.visibilityMode="mutex";isc.A.forceFill=true;isc.A.itemIndent=0;isc.A.showExpandControls=true;isc.A=isc.SectionStack.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.sectionNameIndex=0;isc.B.push(isc.A.initWidget=function isc_SectionStack_initWidget(){this.Super(this.$sb);if(this.canReorderSections)this.canAcceptDrop=true;if(this.visibilityMode=="mutex")this.animateSections=false;if(this.animateSections!=null)this.animateMembers=this.animateSections
if(this.groups!=null&&this.sections==null)this.sections=this.groups;var _1=this.sections;this.sections=[];this.addSections(_1,null,true)}
,isc.A.$13z=function isc_SectionStack__dragIsSectionReorder(){if(this.canReorderSections){var _1=this.ns.EH.dragTarget;return(this.sections!=null&&this.sections.contains(_1))}
return false}
,isc.A.willAcceptDrop=function isc_SectionStack_willAcceptDrop(){if(this.$13z()){var _1=this.ns.EH.dragTarget;return(_1.canReorder!=false)}
return this.Super("willAcceptDrop",arguments)}
,isc.A.getDropPosition=function isc_SectionStack_getDropPosition(_1){if(!this.$13z()){return this.getEditModeDropPosition(_1)}
var _2=this.vertical?this.getOffsetY():this.getOffsetX();if(_2<0)return 0;var _3=this.vertical?this.$xy:this.$xw,_4=this.sections[0],_5=0,_6=0,_7=0;while(_7<this.members.length){var _8=0,_9=this.members[_7],_10=0;while(_9!=null&&(_9==_4||(_4.items&&_4.items.contains(_9)))){if(_9.isVisible()){_8+=this.memberSizes[_6];_10=this.membersMargin+this.getMemberGap(_9);_8+=_10;_6++}
_7+=1
_9=this.members[_7]}
if(_2<(_3+((_8-_10)/2))){if(_4&&_4.canDropBefore===false)return false;return this.members.indexOf(_4)}
_3+=_8;_5+=1;_4=this.sections[_5]}
return this.members.length}
,isc.A.drop=function isc_SectionStack_drop(){if(!this.willAcceptDrop())return;var _1=this.getDropPosition(),_2=this.getDropComponent(isc.EventHandler.getDragTarget(),_1);if(this.canReorderSections&&_2!=null&&this.sections.contains(_2)){var _3=this.sections.indexOf(_2),_4;var _5=this.members[_1];if(_5==null){_4=this.sections.length}else{for(var i=0;i<this.sections.length;i++){if(_5==this.sections[i]||(this.sections[i].items&&this.sections[i].items.contains(_5)))
{_4=i}}}
var _7=_4>_3;if(_7){_4-=1}
if(_4==_3){return}
this.sections.slide(_3,_4);var _8=this.members.indexOf(_2),_9=_8+1,_10=_2.items||[];for(var i=0;i<_10.length;i++){if(this.members.contains(_10[i]))_9+=1}
if(_7)_1-=(_9-_8);this.logInfo("Drag reorder of sections - section:"+_2+" moved to:"+_4+" - reordering members from "+_8+" to "+_9+" target position:"+_1);this.reorderMembers(_8,_9,_1)}}
,isc.A.addItem=function isc_SectionStack_addItem(_1,_2,_3){var _4=this.createCanvas(_2);if(!isc.isA.Canvas(_4)){this.logWarn("addItem passed:"+this.echo(_2)+" cannot be resolved to a Canvas - ignoring");return}
var _5=this.getSection(_1);if(_3==null)_3=0;if(_3>=_5.items.length)_3=_5.items.length;_5.items.addAt(_4,_3);if(this.isDrawn()&&_5.expanded){var _6=1+this.members.indexOf(_5)+_3;this.addMember(_4,_6)}else if(_4.isDrawn()){_4.clear();_4.deparent()}}
,isc.A.removeItem=function isc_SectionStack_removeItem(_1,_2){var _3=this.getSection(_1);_3.items.remove(_2);if(this.members.contains(_2))this.removeMember(_2)}
,isc.A.setSectionProperties=function isc_SectionStack_setSectionProperties(_1,_2){var _1=this.getSection(_1);if(_1!=null){if(isc.isA.Canvas(_1)){_1.setProperties(_2)}else{isc.addProperties(_1,_2)}}}
,isc.A.removeChild=function isc_SectionStack_removeChild(_1,_2){isc.Layout.$ce.removeChild.call(this,_1,_2);var _3=this.sections;if(_3){for(var i=0;i<_3.length;i++){var _5=_3[i];if(_1==_5){this.removeSection(_1);break}else if(_5.items&&_5.items.contains(_1)){this.removeItem(_5,_1);break}}}}
,isc.A.addSections=function isc_SectionStack_addSections(_1,_2,_3){if(_1==null)return;if(!isc.isAn.Array(_1))_1=[_1];if(_2==null||_2>this.sections.length){_2=this.sections.length}
for(var i=0;i<_1.length;i++){var _5=_1[i];if(!_5)continue;if(_5.showHeader==null)_5.showHeader=true;if(_5.canHide==null)_5.canHide=true;var _6=_5.expanded!=null?_5.expanded:_5.autoShow||_5.showHeader==false;if(_5.hidden==null)_5.hidden=false;if(_5.items==null)_5.items=[];else if(!isc.isA.Array(_5.items))_5.items=[_5.items];for(var j=0;j<_5.items.length;j++){if(isc.isAn.Object(_5.items[j]))_5.items[j].$0j=this.ID};var _8=isc.ClassFactory.getClass(this.sectionHeaderClass),_9=_8.createRaw();_9.autoDraw=false;_9._generated=true;_9.expanded=_6;_9.isSectionHeader=true;if(!this.editingOn)
_9.noDoubleClicks=true;_9.visibility=(_5.hidden||_5.showHeader==false)?isc.Canvas.HIDDEN:isc.Canvas.INHERIT;_9.dragScrollType="parentsOnly";_9.dragScrollDirection=this.vertical?isc.Canvas.VERTICAL:isc.Canvas.HORIZONTAL;_9.layout=this;if(this.vertical)_9.height=this.headerHeight;else _9.width=this.headerHeight;var _10=null,_11=null;if(_5.name!=null)_10=_5.name;if(_5.ID!=null){if(_10==null)_10=_5.ID;if(!this.useGlobalSectionIDs){_11=_5.ID;delete _5.ID}else{var _12=window[_5.ID];if(_12!=null){this.logWarn("Note: Section Stack Section has ID specified as '"+_5.ID+"'. This collides with an existing "+(isc.isA.Canvas(_12)?"SmartClient component ID.":"object reference.")+" The existing object will be replaced by the generated header"+" for this section. To avoid applying section IDs to their"+" corresponding section headers, you can set"+" sectionStack.useGlobalSectionIDs to false")}}}
if(_10==null){_10="section"+this.sectionNameIndex++}
var _13=_10,_14=this.sections.find("name",_10);while(_14!=_5&&_14!=null){_10="section"+this.sectionNameIndex++;_14=this.sections.find("name",_10)}
if(_13!=_10){this.logWarn("Specified name for section:"+_13+" collided with name for "+"existing section in this stack. Replacing with auto-generated name:"+_10)}
_5.name=_10;isc.addProperties(_9,_5);_9.__ref=null;_9.$130=_5;_5.$131=_9;_9.getSectionConfig=function(){return this.$130}
_5.getSectionHeader=function(){return this.$131}
if(_11!=null){_5.ID=_11}
if(this.canReorderSections&&_9.canReorder!=false){_9.canDragReposition=true;_9.canDrop=true}
_9.completeCreation();_5=_9;this.sections.addAt(_5,_2+i);this.addMember(_5,this.$132(_5));if(_6&&!_5.hidden){this.expandSection(_5);this.$133=_5}else{for(var _15=0;_15<_5.items.length;_15++){var _16=_5.items[_15];if(_16.parentElement&&_16.parentElement!=this)_16.deparent();if(isc.isA.Canvas(_16)&&_16.isDrawn())_16.clear()}}
if(_5.items){if(!this.canResizeSections)_5.items.setProperty("resizeable",false);else if(_5.resizeable!=null){_5.items.setProperty("resizeable",_5.resizeable)}}}
if(_3&&this.$133==null){var _17=_1.first();if(!(_17.expanded==false)){var _18=this.sections.first();this.expandSection(_18);this.$133=_18}}}
,isc.A.addSection=function isc_SectionStack_addSection(_1,_2){this.addSections(_1,_2)}
,isc.A.removeSection=function isc_SectionStack_removeSection(_1){if(!isc.isAn.Array(_1))_1=[_1];for(var i=0;i<_1.length;i++){var _3=_1[i];_3=this.getSectionHeader(_3);for(var _4=0;_4<_3.items.length;_4++){var _5=_3.items[_4];if(this.members.contains(_5))this.removeMember(_5)}
this.sections.remove(_3);if(!_3.destroying&&!_3.destroyed)_3.destroy()}}
,isc.A.getSections=function isc_SectionStack_getSections(){return this.sections.getProperty("name")}
,isc.A.reorderSection=function isc_SectionStack_reorderSection(_1,_2){this.moveSection(_1,_2)}
,isc.A.moveSection=function isc_SectionStack_moveSection(_1,_2){if(_2==null)return;if(!isc.isAn.Array(_1))_1=[_1];for(var i=0;i<_1.length;i++){var _4=this.getSectionHeader(_1[i]);if(_4==null){this.logInfo("moveSection(): Unable to find header for specified section:"+_1[i]+", skipping");i--;_1.removeAt(i)}else{_1[i]=_4;this.sections[this.sections.indexOf(_4)]=null}}
this.sections.removeEmpty();this.sections.addListAt(_1,_2);var _5=0;for(var i=0;i<this.sections.length;i++){var _6=this.getSectionHeader(i),_7=this.members.indexOf(_6),_8=_7+1;var _9=_6.items;if(_9!=null&&_9.length!=0&&this.members.contains(_9[0])){if(_7==-1){_7=this.members.indexOf(_9[0]);_8=_7}
_8+=_9.length}
if(_7==-1)continue;this.members.slideRange(_7,_8,_5);_5+=(_8-_7)}
this.$106("moveSection() called")}
,isc.A.showSection=function isc_SectionStack_showSection(_1,_2){this.$134(_1,true,false,_2)}
,isc.A.expandSection=function isc_SectionStack_expandSection(_1,_2){if(!isc.isAn.Array(_1))_1=[_1];if(this.visibilityMode=="mutex"){this.collapseSection(this.$133)}
this.$134(_1,false,true,_2)}
,isc.A.$134=function isc_SectionStack__showSection(_1,_2,_3,_4){if(_1==null)return;if(!isc.isAn.Array(_1))_1=[_1];var _5=[];for(var i=0;i<_1.length;i++){var _7=this.getSectionHeader(_1[i]);if(_7==null){this.logWarn("showSection(): no such section ["+i+"]: "+this.echo(_1[i]));continue}
if(_7.showHeader&&_7.hidden&&(_2||_3)){_5.add(_7);_7.hidden=false}
if(_3||_7.expanded){if(_7.setExpanded&&!_7.setOpen)_7.setExpanded(true);else if(_7.setOpen)_7.setOpen(true);this.$133=_7;if(_7.items){for(var _8=_7.items.length-1;_8>=0;_8--){var _9=this.createCanvas(_7.items[_8]);if(!isc.isA.Canvas(_9)){this.logWarn("Section with title:"+_7.title+" contains invalid item:"+_7.items[_8]+" - ignoring this item.");_7.items.removeAt(_8);continue}
_7.items[_8]=_9}
var _10=this.$132(_7)+1;this.addMembers(_7.items,_10,true);_5.addList(_7.items)}}}
var _11=this;this.showMembers(_5,function(){_11.$135(_1,_4)})}
,isc.A.$135=function isc_SectionStack__completeShowOrExpandSection(_1,_2){if(_1.length==0)return;if(this.isDrawn()){var _3=this.getSectionHeader(_1[0]);if(this.vscrollOn&&this.scrollSectionIntoView){var _4=(_3.showHeader?_3:_3.items.first()),_5=_3.items.last();this.delayCall("scrollIntoView",[_4.getLeft(),_4.getTop(),_4.getVisibleWidth(),_5.getVisibleHeight(),"left","top"],0)}}
if(_2!=null)this.fireCallback(_2)}
,isc.A.sectionForItem=function isc_SectionStack_sectionForItem(_1){if(this.sections){for(var i=0;i<this.sections.length;i++){for(var j=0;j<this.sections[i].items.length;i++){if(this.sections[i].items[j]==_1){return this.sections[i]}}}}}
,isc.A.hideSection=function isc_SectionStack_hideSection(_1,_2){this.$136(_1,true,false,_2)}
,isc.A.collapseSection=function isc_SectionStack_collapseSection(_1,_2){this.$136(_1,false,true,_2)}
,isc.A.$136=function isc_SectionStack__hideSection(_1,_2,_3,_4){if(_1==null)return;if(!isc.isAn.Array(_1))_1=[_1];var _5=[];for(var i=0;i<_1.length;i++){var _7=this.getSectionHeader(_1[i]);if(_7==null){this.logWarn("hideSection(): no such section ["+i+"]: "+this.echo(_1[i]));continue}
if(_2&&!_7.hidden){_7.hidden=true;_5.add(_7)}
if(_3||_7.expanded){if(_3){if(_7.setExpanded&&!_7.setOpen)_7.setExpanded(false);else if(_7.setOpen)_7.setOpen(false)}
if(_7.items){for(var j=0;j<_7.items.length;j++){if(this.members.contains(_7.items[j]))_5.add(_7.items[j])}}}}
if(this.forceFill&&this.getVisibleHeight()<=this.getHeight()){var _9=this.getMemberNumber(this.getSectionHeader(_1[0]));var _10;var _11=false;for(var i=_9-1;i>=0;i--){var _12=this.members[i];if(_5.contains(_12))continue;if(this.memberIsDragResizeable(_12)){if(this.memberHasAutoResizeableHeight(_12)){_11=true;break}else if(_10==null){_10=_12}}}
if(!_11){for(var i=_9+1;i<this.members.length;i++){var _12=this.members[i];if(_5.contains(_12))continue;if(this.memberIsDragResizeable(_12)){if(this.memberHasAutoResizeableHeight(_12)){_11=true;break}else if(_10==null){_10=_12}}}}
if(!_11&&_10!=null){_10.$tt=null}}
this.hideMembers(_5,_4)}
,isc.A.sectionIsVisible=function isc_SectionStack_sectionIsVisible(_1){_1=this.getSectionHeader(_1);if(!_1)return false;if(_1.showHeader&&_1.isVisible())return true;var _2=_1.items.first();if(_2==null||!isc.isA.Canvas(_2)||!_2.isDrawn()||_2.visibility==isc.Canvas.HIDDEN)return false;return true}
,isc.A.getVisibleSections=function isc_SectionStack_getVisibleSections(){var _1=[];for(var i=0;i<this.sections.length;i++)
if(this.sectionIsVisible(this.sections[i]))_1.add(this.sections[i].name);return _1}
,isc.A.sectionIsExpanded=function isc_SectionStack_sectionIsExpanded(_1){return this.getSectionHeader(_1).expanded}
,isc.A.getExpandedSections=function isc_SectionStack_getExpandedSections(){var _1=this.sections.findAll("expanded",true);return _1==null?[]:_1.getProperty("name")}
,isc.A.setSectionTitle=function isc_SectionStack_setSectionTitle(_1,_2){var _3=this.getSectionHeader(_1);if(_3)_3.setTitle(_2)}
,isc.A.getSectionHeader=function isc_SectionStack_getSectionHeader(_1){return isc.Class.getArrayItem(_1,this.sections,"name")}
,isc.A.getSection=function isc_SectionStack_getSection(_1){return this.getSectionHeader(_1)}
,isc.A.getSectionConfig=function isc_SectionStack_getSectionConfig(_1){var _2=this.getSectionHeader(_1);if(!isc.isA.Canvas(_2))return _2;return _2.$130}
,isc.A.getSectionNumber=function isc_SectionStack_getSectionNumber(_1){if(isc.isA.String(_1)){return this.sections.findIndex("name",_1)}else{return this.sections.indexOf(_1)}}
,isc.A.$132=function isc_SectionStack__getSectionPosition(_1){var _2=this.getMemberNumber(_1);if(_2!=-1)return _2;var _3=this.sections.indexOf(_1);if(_3<=0)return _3;var _4=this.sections[_3-1],_5=_4.items?_4.items.last():null;if(this.hasMember(_5)){return this.getMemberNumber(_5)+1}else{return this.getMemberNumber(_4)+1}}
,isc.A.sectionHeaderClick=function isc_SectionStack_sectionHeaderClick(_1){if(this.onSectionHeaderClick&&(this.onSectionHeaderClick(_1)==false)){return false}
if(this.visibilityMode=="mutex"){if(this.sectionIsExpanded(_1)){var _2=this.sections.indexOf(_1);if(_2==this.sections.getLength()-1)_2=0;else _2+=1;var _3=this.sections[_2];this.collapseSection(_1);this.expandSection(_3);this.$133=_3;return false}
this.collapseSection(this.$133);this.expandSection(_1)}else{if(!this.sectionIsExpanded(_1)){this.expandSection(_1)}else{this.collapseSection(_1)}}
this.$133=_1;return false}
,isc.A.getSectionCursor=function isc_SectionStack_getSectionCursor(_1){var _2;var _3=this.getSectionConfig(_1);if(_3==null)_2=isc.Canvas.DEFAULT;else if(_3.cursor)_2=_3.cursor;else{if(_3.canCollapse!=false){_2=isc.Canvas.HAND}else if(this.canRorderSections&&_3.canReorder!=false){_2="move"}else{_2=isc.Canvas.DEFAULT}}
return _2}
,isc.A.getDragResizeTarget=function isc_SectionStack_getDragResizeTarget(_1){var _2=this.getMemberNumber(_1);var _3;this.$137=0;for(var i=_2-1;i>=0;i--){var _1=this.getMember(i);if(this.memberIsDragResizeable(_1)){_3=_1;break}
if(_1.isSectionHeader||(!_1.resizeable&&_1.isVisible()))
this.$137+=_1.getVisibleHeight()}
if(!_3)return null;if(this.canResizeStack)return _3;var _5=this.getMembers().length;for(var i=_2+1;i<_5;i++){var _1=this.getMember(i);if(this.memberIsDragResizeable(_1))return _3}
return null}
,isc.A.memberIsDragResizeable=function isc_SectionStack_memberIsDragResizeable(_1){if(!_1.isSectionHeader&&_1.resizeable!==false&&_1.isVisible()&&(!this.memberHasInherentLength(_1)||_1.resizeable))return true}
,isc.A.memberHasAutoResizeableHeight=function isc_SectionStack_memberHasAutoResizeableHeight(_1){var _2=_1.$tt;return _2==null||(isc.isA.String(_2)&&(_2=="*"||isc.endsWith(_2,"%")))}
,isc.A.getMemberDefaultBreadth=function isc_SectionStack_getMemberDefaultBreadth(_1,_2){var _3=_2;if(!_1.isSectionHeader){if(this.itemStartIndent!=null||this.itemEndIndent!=null)
_3-=this.itemStartIndent+this.itemEndIndent;else _3-=this.itemIndent}
return _3}
,isc.A.getMemberOffset=function isc_SectionStack_getMemberOffset(_1,_2,_3){var _4=this.itemIndent;if(_1.isSectionHeader)return _2;if(this.itemStartIndent!=null)_4=this.itemStartIndent;if(_3==isc.Canvas.RIGHT||_3==isc.Canvas.BOTTOM)
_4*=-1;return _2+_4}
);isc.B._maxIndex=isc.C+40;isc.$138={icon:"[SKIN]SectionHeader/opener.gif",overflow:"hidden",baseStyle:"sectionHeader",showDisabled:true,expanded:false,setExpanded:function(_1){this.expanded=_1;this.stateChanged()},setOpen:function(_1){this.setExpanded(_1)},getCustomState:function(){return this.expanded?"opened":"closed"}};isc.$139={overflow:"hidden",wrap:false,height:20,expanded:false,canCollapse:true,getSectionStack:function(){var _1=this.layout;if(_1)return isc.isA.String(_1)?window[_1]:_1;else return null},keyPress:function(){var _1=this.getSectionStack();if(_1==null)return;var _2=isc.EH.getKey();if(_2=="Enter"||_2=="Space"){if(this.canCollapse)return _1.sectionHeaderClick(this)}else if(_2=="Arrow_Up"||_2=="Arrow_Down"){var _3=_1.getDragResizeTarget(this);if(_3==null)return false;var _4=(_2=="Arrow_Up"?-5:5);this.bringToFront();this.resizeTarget(_3,true,this.resizeInRealTime,0,0,(this.getPageTop()+_4))
this.$14a=_3}},keyUp:function(){if(this.$14a){var _1=isc.EH.getKey();if(_1=="Arrow_Up"||_1=="Arrow_Down"){this.finishTargetResize(this.$14a,true,this.resizeInRealTime);this.$14a=null}}},$mb:function(){if(this.canTabToHeader!=null)return this.canTabToHeader;var _1=this.getSectionStack();if(_1){if(_1.canTabToHeaders!=null)return _1.canTabToHeaders;if(_1.canTabToHeader!=null)return _1.canTabToHeader;if(isc.SectionItem&&isc.isA.SectionItem(_1)){var _2=_1.form;if(_2&&_2.canTabToSectionHeaders!=null)return _2.canTabToSectionHeaders}
return!!isc.screenReader}
else return true},$14b:function(){var _1=this.getSectionStack();return _1?true:false},schemaName:"SectionStackSection",addItem:function(_1,_2){if(!this.$14b())return;var _3=this.getSectionStack();_3.addItem(this,_1,_2);_3.expandSection(this)},removeItem:function(_1){if(!this.$14b())return;this.getSectionStack().removeItem(this,_1)},canDrag:true,dragAppearance:"none",isSectionHeader:true,dragStart:function(){if(!this.$14b())return;var _1=this.getSectionStack().getDragResizeTarget(this);this.$14c=_1;if(_1==null)return false;this.bringToFront()},dragMove:function(){if(!this.$14b())return;var _1=this.getSectionStack().$137;var _2=0-isc.EH.dragOffsetY;this.resizeTarget(this.$14c,true,this.resizeInRealTime,_2,_1)},dragStop:function(){this.finishTargetResize(this.$14c,true,this.resizeInRealTime)},destroy:function(){if(!this.expanded&&this.items){var _1=this.items;for(var i=0;i<_1.length;i++){if(isc.isA.Canvas(_1[i])&&_1[i].parentElement!=this.parentElement){_1[i].destroy()}}}
var _3=this.controls,_4=this.controlsLayout;if(_3){if(!isc.isAn.Array(_3))_3=[_3];for(var _5=0;_5<_3.length;_5++){if(_3[_5].destroy&&!_3[_5].destroyed&&(_4==null||_3[_5].parentElement!=_4))
{_3[_5].destroy()}}}
return this.Super("destroy",arguments)},controlsLayoutDefaults:{_constructor:isc.HStack,defaultLayoutAlign:"center",snapTo:"R",membersMargin:5,layoutEndMargin:5,addAsChild:true},addControls:function(){if(!this.controls)return;this.addAutoChild("controlsLayout",{height:this.getInnerHeight(),align:this.isRTL()?"left":"right",members:this.controls});this.allowContentAndChildren=true},refreshControls:function(){if(!this.controls)return;if(!this.controlsLayout)this.addControls();var _1=this.controlsLayout;_1.addMembers(this.controls);this.allowContentAndChildren=true},getPrintStyleName:function(){var _1=this.parentElement;if(_1&&_1.printHeaderStyleName!=null){this.printStyleName=_1.printHeaderStyleName}
return this.Super("getPrintStyleName",arguments)},shouldPrint:true};isc.defineClass("SectionHeader","Label");isc.A=isc.SectionHeader.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.useContents=false;isc.B.push(isc.A.click=function isc_SectionHeader_click(){if(this.contains(isc.EH.lastTarget))return;if(!this.canCollapse||!this.$14b())return;return this.getSectionStack().sectionHeaderClick(this)}
,isc.A.draw=function isc_SectionHeader_draw(_1,_2,_3,_4){if(isc.$dd)arguments.$de=this;if(!this.readyToDraw())return;this.align=this.isRTL()?"right":"left";if(!this.canCollapse||(this.$14b()&&this.getSectionStack()&&this.getSectionStack().showExpandControls==false))
{this.icon=null;this.showIconState=false}
this.setCursor(this.getCurrentCursor());this.invokeSuper(isc.SectionHeader,"draw",_1,_2,_3,_4);this.addControls();if(this.headerControls!=null){this.headerLayout=isc.HLayout.create({autoDraw:false,width:this.getInnerWidth(),height:this.getInnerHeight(),members:this.headerControls});this.addChild(this.headerLayout);this.allowContentAndChildren=true}}
,isc.A.getCurrentCursor=function isc_SectionHeader_getCurrentCursor(){var _1=this.cursor;if(this.getSectionStack()&&this.getSectionStack().getSectionCursor!=null){_1=this.getSectionStack().getSectionCursor(this)}
return _1}
);isc.B._maxIndex=isc.C+3;isc.SectionHeader.addMethods(isc.$139, isc.$138);isc.defineClass("ImgSectionHeader","HLayout");isc.A=isc.ImgSectionHeader.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.backgroundDefaults=isc.addProperties({titleStyle:"sectionHeaderTitle",src:"[SKIN]SectionStack/header.gif",backgroundColor:"#a0a0a0",click:function(){if(this.parentElement&&this.parentElement.editingOn){return this.Super("click",arguments)}
if(this.parentElement.canCollapse){if(this.parentElement.getSectionStack())
return this.parentElement.getSectionStack().sectionHeaderClick(this.parentElement)}},width:"100%",height:"100%",addAsChild:true,getPrintStyleName:function(){if(this.parentElement)return this.parentElement.getPrintStyleName();return this.Super("getPrintStyleName",arguments)}},isc.$138);isc.B.push(isc.A.setExpanded=function isc_ImgSectionHeader_setExpanded(_1){this.expanded=_1;if(this.background)this.background.setExpanded(_1)}
,isc.A.setOpen=function isc_ImgSectionHeader_setOpen(_1){this.setExpanded(_1)}
,isc.A.setTitle=function isc_ImgSectionHeader_setTitle(_1){this.title=_1;if(this.background)this.background.setTitle(_1)}
,isc.A.draw=function isc_ImgSectionHeader_draw(_1,_2,_3,_4){if(isc.$dd)arguments.$de=this;if(!this.readyToDraw())return;this.setupBackground();this.addControls();this.addAutoChildren(this.headerControls);this.background.sendToBack();this.invokeSuper(isc.ImgSectionHeader,"draw",_1,_2,_3,_4)}
,isc.A.setupBackground=function isc_ImgSectionHeader_setupBackground(){var _1={title:this.title,expanded:this.expanded,canFocus:false};if(this.icon)_1.icon=this.icon;if(!this.canCollapse||(this.$14b()&&this.getSectionStack()&&this.getSectionStack().showExpandControls==false))
{_1.icon=null;_1.showIconState=false}
_1.align=this.isRTL()?"right":"left";_1.canDragReposition=this.canDragReposition;_1.canDrop=this.canDrop;_1.dragTarget=this;var _2=this.getCurrentCursor();this.setCursor(_2);_1.cursor=_2;if(this.background==null){this.addAutoChild("background",_1,isc.StretchImgButton)}else{this.background.setProperties(_1)}}
,isc.A.getCurrentCursor=function isc_ImgSectionHeader_getCurrentCursor(){var _1=this.cursor;if(this.getSectionStack()&&this.getSectionStack().getSectionCursor!=null){_1=this.getSectionStack().getSectionCursor(this)}
return _1}
,isc.A.getPrintHTML=function isc_ImgSectionHeader_getPrintHTML(_1){if(this.background==null)this.setupBackground();return this.background.getPrintHTML(_1)}
);isc.B._maxIndex=isc.C+7;isc.ImgSectionHeader.addMethods(isc.$139)
isc.SectionStack.registerStringMethods({onSectionHeaderClick:"sectionHeader"});isc.SectionStack.registerDupProperties("sections",["items"]);isc.ClassFactory.defineClass("Scrollbar","StretchImg");isc.$14d={autoDraw:false,_generated:true,$k7:false,$k6:false,_redrawWithParent:false,containedPeer:true,showDisabled:false,skinImgDir:"images/Scrollbar/",canDrag:true,dragAppearance:isc.EventHandler.NONE,dragStartDistance:0,dragScrollType:"parentsOnly",click:isc.EventHandler.stopBubbling,doubleClick:isc.EventHandler.stopBubbling,mouseMove:isc.EventHandler.stopBubbling,mouseOver:function(){return this.scrollbar.thumbOver()},mouseOut:function(){return this.scrollbar.thumbOut()},mouseDown:function(){return this.scrollbar.thumbDown()},dragStart:function(){return this.scrollbar.thumbDragStart()},dragMove:function(){return this.scrollbar.thumbMove()},dragStop:function(){return this.scrollbar.thumbDragStop()},mouseUp:function(){return this.scrollbar.thumbUp()},keyPress:function(){return this.ns.EH.bubbleEvent(this.scrollbar,this.ns.EH.eventTypes.KEY_PRESS)},keyDown:function(){return this.ns.EH.bubbleEvent(this.scrollbar,this.ns.EH.eventTypes.KEY_DOWN)},keyUp:function(){return this.ns.EH.bubbleEvent(this.scrollbar,this.ns.EH.eventTypes.KEY_UP)},mouseWheel:function(){return this.ns.EH.bubbleEvent(this.scrollbar,this.ns.EH.eventTypes.MOUSE_WHEEL)},masterMoved:function(){var _1=this.masterElement;if(_1&&_1.$xe)return;this.Super("masterMoved",arguments)}};isc.defineClass("ScrollThumb","StretchImg").addProperties(isc.$14d)
isc.A=isc.ScrollThumb.getPrototype();isc.A.hSrc="[SKIN]hthumb.gif";isc.A.vSrc="[SKIN]vthumb.gif";isc.A.backgroundColor="#EEEEEE";isc.A.textDirection="ltr";isc.A.capSize=2;isc.defineClass("HScrollThumb",isc.ScrollThumb);isc.A=isc.HScrollThumb.getPrototype();isc.A.vertical=false;isc.defineClass("VScrollThumb",isc.ScrollThumb);isc.defineClass("SimpleScrollThumb","Img").addProperties(isc.$14d)
isc.A=isc.SimpleScrollThumb.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.title="&nbsp;";isc.A.titleStyle="normal";isc.A.overflow="hidden";isc.A.vBaseStyle="vScrollThumb";isc.A.hBaseStyle="hScrollThumb";isc.A.imageType="center";isc.A.hSrc="[SKIN]hthumb_grip.gif";isc.A.vSrc="[SKIN]vthumb_grip.gif";isc.A.showRollOver=true;isc.A.statelessImage=true;isc.B.push(isc.A.initWidget=function isc_SimpleScrollThumb_initWidget(){if(this.vertical){this.src=this.vSrc||this.src;this.baseStyle=this.vBaseStyle||this.baseStyle}else{this.src=this.hSrc||this.src;this.baseStyle=this.hBaseStyle||this.baseStyle}
this.Super("initWidget",arguments)}
);isc.B._maxIndex=isc.C+1;isc.defineClass("HSimpleScrollThumb",isc.SimpleScrollThumb);isc.A=isc.HSimpleScrollThumb.getPrototype();isc.A.vertical=false;isc.defineClass("VSimpleScrollThumb",isc.SimpleScrollThumb);isc.A=isc.VSimpleScrollThumb.getPrototype();isc.A.vertical=true;isc.A=isc.Scrollbar.getPrototype();isc.A.btnSize=16;isc.A.state=isc.StatefulCanvas.STATE_UP;isc.A.autoEnable=true;isc.A.allowThumbDownState=false;isc.A.allowThumbOverState=false;isc.A.showTrackEnds=false;isc.A.thumbMinSize=12;isc.A.trackEndWidth=12;isc.A.trackEndHeight=12;isc.A.thumbOverlap=1;isc.A.thumbInset=0;isc.A.overflow=isc.Canvas.HIDDEN;isc.A.skinImgDir="images/Scrollbar/";isc.A.cornerSrc="[SKIN]corner.gif";isc.A.hSrc="[SKIN]hscroll.gif";isc.A.vSrc="[SKIN]vscroll.gif";isc.A.hThumbClass=isc.HScrollThumb;isc.A.vThumbClass=isc.VScrollThumb;isc.A.startImg={name:"start",width:"btnSize",height:"btnSize"};isc.A.trackStartImg={name:"track_start",width:"trackStartSize",height:"trackStartSize"};isc.A.trackImg={name:"track",width:"*",height:"*"};isc.A.trackEndImg={name:"track_end",width:"trackEndSize",height:"trackEndSize"};isc.A.endImg={name:"end",width:"btnSize",height:"btnSize"};isc.A.textDirection="ltr";isc.A.$z0=true;isc.A.showThumb=true;isc.A=isc.Scrollbar.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$14e="start";isc.A.$14f="end";isc.A.$14g="track";isc.A.$14h="track_start";isc.A.$14i="track_end";isc.A.$14j="thumb";isc.A.$14k="corner";isc.A.$14k="corner";isc.A.click=isc.EventHandler.stopBubbling;isc.A.mouseOver=isc.EH.stopBubbling;isc.B.push(isc.A.initWidget=function isc_Scrollbar_initWidget(){this.invokeSuper(isc.Scrollbar,"initWidget");var _1=this.cornerSize||this.getID()+".btnSize-1";this.$14l={name:"corner",width:_1,height:_1};if(null==this.startThumbOverlap)this.startThumbOverlap=this.thumbOverlap;if(null==this.endThumbOverlap)this.endThumbOverlap=this.thumbOverlap;this.setItems();if(this.vertical)this.setWidth(this.btnSize);else this.setHeight(this.btnSize);this.makeThumb();this.addPeer(this.thumb);this.setScrollTarget();this.setThumb()}
,isc.A.setItems=function isc_Scrollbar_setItems(){if(this.showTrackEnds==true){this.items=[this.startImg,this.trackStartImg,this.trackImg,this.trackEndImg,this.endImg]}else{this.items=[this.startImg,this.trackImg,this.endImg]}
if(this.showCorner)this.items.add(this.$14l)}
,isc.A.setShowCorner=function isc_Scrollbar_setShowCorner(_1){_1=_1!=false;if(this.showCorner!=_1){this.showCorner=_1;this.setItems();this.resizeImages();this.markForRedraw("showCorner")}
return _1}
,isc.A.setScrollTarget=function isc_Scrollbar_setScrollTarget(_1){if(this.$z0&&this.scrollTarget!=null&&this.isObserving(this.scrollTarget,"scrollTo"))
{this.ignore(this.scrollTarget,"scrollTo")}
if(_1!=null)this.scrollTarget=_1;if(this.scrollTarget==null)this.scrollTarget=this;if(this.$z0&&this.scrollTarget!=this&&this.scrollTarget!=_1){this.observe(this.scrollTarget,"scrollTo","observer.setThumb()")}}
,isc.A.setHandleDisabled=function isc_Scrollbar_setHandleDisabled(_1){this.Super("setHandleDisabled",arguments);if(this.thumb){if(this.scrollTarget&&this.scrollTarget.$14m){if(_1)this.thumb.delayCall("setVisibility",[isc.Canvas.HIDDEN]);else this.thumb.delayCall("setVisibility",[this.visibility])}else{if(_1)this.thumb.setVisibility(isc.Canvas.HIDDEN);else this.thumb.setVisibility(this.visibility)}
this.thumb.$k8=!_1}
if(_1==(this.state==isc.StatefulCanvas.STATE_UP)){this.setState(_1?isc.StatefulCanvas.STATE_DISABLED:isc.StatefulCanvas.STATE_UP)}}
,isc.A.setVisibility=function isc_Scrollbar_setVisibility(_1,_2,_3,_4){this.invokeSuper(isc.Scrollbar,"setVisibility",_1,_2,_3,_4);if(this.isVisible())this.setThumb()}
,isc.A.parentVisibilityChanged=function isc_Scrollbar_parentVisibilityChanged(_1,_2,_3,_4){this.invokeSuper(isc.Scrollbar,"parentVisibilityChanged",_1,_2,_3,_4);if(this.isVisible())this.setThumb()}
,isc.A.drawPeers=function isc_Scrollbar_drawPeers(_1,_2,_3,_4){this.setThumb();this.invokeSuper(isc.Scrollbar,"drawPeers",_1,_2,_3,_4)}
,isc.A.resizePeersBy=function isc_Scrollbar_resizePeersBy(_1,_2){this.setThumb()}
,isc.A.makeThumb=function isc_Scrollbar_makeThumb(){if(!this.showThumb)return;var _1=this.vertical?this.vThumbClass:this.hThumbClass;this.thumb=_1.create({ID:this.getID()+"_thumb",scrollbar:this,state:this.state,visibility:this.visibility,width:this.vertical?this.getWidth():1,height:!this.vertical?this.getHeight():1,dragScrollDirection:this.vertical?isc.Canvas.VERTICAL:isc.Canvas.HORIZONTAL});if(this.thumb.showRollOver){this.allowThumbOverState=true
this.thumb.showRollOver=false}
if(this.thumb.showDown){this.allowThumbDownState=true;this.thumb.showDown=false}}
,isc.A.updateButtonsOnEdges=function isc_Scrollbar_updateButtonsOnEdges(){if(this.disableButtonsOnEdges){var _1=this.scrollTarget.getScrollRatio(this.vertical);var _2=this.scrollTarget.getViewportRatio(this.vertical);if(_1==0){this.setState(isc.StatefulCanvas.STATE_DISABLED,this.$14e)}else{this.setState(isc.StatefulCanvas.STATE_UP,this.$14e)}
if(_1==1||_2>=1){this.setState(isc.StatefulCanvas.STATE_DISABLED,this.$14f)}else{this.setState(isc.StatefulCanvas.STATE_UP,this.$14f)}}}
,isc.A.setThumb=function isc_Scrollbar_setThumb(){this.updateButtonsOnEdges();if(this.thumb==null||this.$8e)return;var _1=this.thumb,_2=this.trackSize();if(this.isDrawn()&&_1.isDrawn())_1.moveAbove(this);var _3=Math.round(this.scrollTarget.getViewportRatio(this.vertical)*_2);if(!isc.isA.Number(_3)||_3<this.thumbMinSize)_3=this.thumbMinSize;if(_3>_2)_3=_2;var _4=Math.max(1,(this.vertical?this.getWidth():this.getHeight())
-(2*this.thumbInset));this.vertical?_1.resizeTo(_4,_3):_1.resizeTo(_3,_4);this.moveThumb()}
,isc.A.setZIndex=function isc_Scrollbar_setZIndex(_1){this.Super("setZIndex",arguments);if(this.thumb)this.thumb.moveAbove(this)}
,isc.A.moveThumbTo=function isc_Scrollbar_moveThumbTo(_1){if(!this.thumb)return;if(this.vertical)
return this.thumb.moveTo(this.getLeft()+this.thumbInset,_1);else
return this.thumb.moveTo(_1,this.getTop()+this.thumbInset)}
,isc.A.thumbSize=function isc_Scrollbar_thumbSize(){if(!this.thumb)return;return(this.vertical?this.thumb.getHeight():this.thumb.getWidth())}
,isc.A.moveThumb=function isc_Scrollbar_moveThumb(){var _1=(this.$z0||this.scrollTarget.canScroll(this.vertical));if(!_1){if(this.autoEnable)this.disable();this.moveThumbTo(this.trackStart());return}
if(this.autoEnable&&!this.scrollTarget.isDisabled())this.enable();var _2=this.scrollTarget.getScrollRatio(this.vertical),_3=this.trackSize()-this.thumbSize(),_4=Math.round(_2*_3);this.moveThumbTo(_4+this.trackStart());var _5=isc.EH;if(_5.mouseIsDown()&&(_5.mouseDownTarget()==this)&&this.thumb.containsEvent())
this.doneTrackScrolling()}
,isc.A.trackSize=function isc_Scrollbar_trackSize(){if(this.showTrackEnds==true)
return this.getSize(this.getPartNum(this.$14g))+this.getSize(this.getPartNum(this.$14h))+this.getSize(this.getPartNum(this.$14i))+this.startThumbOverlap+this.endThumbOverlap;else return this.getSize(this.getPartNum(this.$14g))+this.startThumbOverlap+this.endThumbOverlap}
,isc.A.trackStart=function isc_Scrollbar_trackStart(){if(this.vertical)
return this.getTop()+this.btnSize-this.startThumbOverlap;else
return this.getLeft()+this.btnSize-this.startThumbOverlap}
,isc.A.directionRelativeToThumb=function isc_Scrollbar_directionRelativeToThumb(){if(!this.thumb){if(this.clickPart==this.$14e)return-1;else return 1}
var _1,_2=this.thumb,_3,_4;if(this.vertical){_1=isc.EH.getY();_3=_2.getPageTop();_4=_2.getHeight()}else{_1=isc.EH.getX();_3=_2.getPageLeft();_4=_2.getWidth()}
if(_1<_3)return-1;else if(_1>_3+_4)return 1;return 0}
,isc.A.mouseDown=function isc_Scrollbar_mouseDown(){this.clickPart=this.inWhichPart();if(this.clickPart==this.$14k){this.clickPart=null;return isc.EH.STOP_BUBBLING}
this.$14n(isc.StatefulCanvas.STATE_DOWN,this.clickPart);this.startDirection=this.directionRelativeToThumb();return isc.EH.STOP_BUBBLING}
,isc.A.mouseStillDown=function isc_Scrollbar_mouseStillDown(){if(this.clickPart==this.$14g||this.showTrackEnds==true&&(this.clickPart==this.$14h||this.clickPart==this.$14i)){var _1=this.directionRelativeToThumb();if(_1!=0&&_1==this.startDirection){if(this.$14o){delete this.$14o;this.$14p=true}else if(!this.$14p)
this.$14o=true;this.scrollTarget.scrollByPage(this.vertical,this.startDirection,"trackClick")}}else{this.scrollTarget.scrollByDelta(this.vertical,this.startDirection,"trackButtonClick")}
return true}
,isc.A.doubleClick=function isc_Scrollbar_doubleClick(){if(isc.Browser.isIE)return this.mouseStillDown();return isc.EH.STOP_BUBBLING}
,isc.A.$14n=function isc_Scrollbar__updateItemStates(_1,_2){if(_2==null)return this.setState(_1);var _3=isc.StatefulCanvas.STATE_UP,_4=(_2==this.$14g||_2==this.$14h||_2==this.$14i),_5=!_4&&_2==this.$14e,_6=!_4&&!_5&&_2==this.$14f,_7=!_4&&!_5&&!_6,_8=_4?_1:_3;this.setState(_5?_1:_3,this.$14e);this.setState(_8,this.$14g);if(this.showTrackEnds)this.setState(_8,this.$14h);if(this.showTrackEnds)this.setState(_8,this.$14i);this.setState(_6?_1:_3,this.$14f);if(this.showCorner)this.setState(_7?_1:_3,this.$14k)}
,isc.A.mouseUp=function isc_Scrollbar_mouseUp(){if(this.clickPart){var _1=this.showRollOver?isc.StatefulCanvas.STATE_OVER:isc.StatefulCanvas.STATE_UP;this.$14n(_1,this.clickPart)}
this.clickPart=null;this.doneTrackScrolling();this.updateButtonsOnEdges();return isc.EventHandler.STOP_BUBBLING}
,isc.A.mouseMove=function isc_Scrollbar_mouseMove(){if(this.ns.EH.mouseIsDown()&&this.clickPart){}else if(this.showRollOver){this.$14n(isc.StatefulCanvas.STATE_OVER,this.inWhichPart())}
return isc.EH.STOP_BUBBLING}
,isc.A.mouseOut=function isc_Scrollbar_mouseOut(){if(this.ns.EH.mouseIsDown())return isc.EH.STOP_BUBBLING;if(this.showRollOver){this.setState(isc.StatefulCanvas.STATE_UP)}
return isc.EH.STOP_BUBBLING}
,isc.A.prepareForDragging=function isc_Scrollbar_prepareForDragging(){return false}
,isc.A.isDragScrolling=function isc_Scrollbar_isDragScrolling(){return this.$14q}
,isc.A.isRepeatTrackScrolling=function isc_Scrollbar_isRepeatTrackScrolling(){return this.$14p}
,isc.A.doneTrackScrolling=function isc_Scrollbar_doneTrackScrolling(){delete this.$14o;if(this.isRepeatTrackScrolling()){delete this.$14p;if(this.scrollTarget&&this.scrollTarget.doneFastScrolling)this.scrollTarget.doneFastScrolling()}}
,isc.A.thumbOver=function isc_Scrollbar_thumbOver(){if(this.allowThumbOverState){this.thumb.setState(isc.StatefulCanvas.STATE_OVER)}}
,isc.A.thumbOut=function isc_Scrollbar_thumbOut(){if(!isc.EH.mouseIsDown()){this.thumb.setState(isc.StatefulCanvas.STATE_UP)}}
,isc.A.thumbDown=function isc_Scrollbar_thumbDown(){this.clickPart=this.$14j;if(this.allowThumbDownState){this.thumb.setState(isc.StatefulCanvas.STATE_DOWN)}
return isc.EventHandler.STOP_BUBBLING}
,isc.A.thumbDragStart=function isc_Scrollbar_thumbDragStart(){var _1=isc.EH;_1.dragOffsetX=this.thumb.getOffsetX(_1.mouseDownEvent);_1.dragOffsetY=this.thumb.getOffsetY(_1.mouseDownEvent);this.$14q=true;return _1.STOP_BUBBLING}
,isc.A.getEventCoord=function isc_Scrollbar_getEventCoord(){var _1=isc.EH;if(this.vertical)
return _1.getY()-this.getPageTop()-this.btnSize+this.startThumbOverlap-_1.dragOffsetY;else
return _1.getX()-this.getPageLeft()-this.btnSize+this.startThumbOverlap-_1.dragOffsetX}
,isc.A.masterMoved=function isc_Scrollbar_masterMoved(_1,_2,_3,_4,_5,_6){if(this.masterElement.$xe)return;return this.invokeSuper(isc.Scrollbar,"masterMoved",_1,_2,_3,_4,_5,_6)}
,isc.A.thumbMove=function isc_Scrollbar_thumbMove(){var _1=this.trackSize()-this.thumbSize(),_2=this.getEventCoord(),_3=_2/ _1;_3=Math.max(0,Math.min(_3,1));this.scrollTarget.scrollToRatio(this.vertical,_3,"thumbMove");return isc.EventHandler.STOP_BUBBLING}
,isc.A.thumbUp=function isc_Scrollbar_thumbUp(){if(this.clickPart!=this.$14j)
return this.mouseUp();var _1=this.allowThumbOverState&&this.thumb.containsEvent()?isc.StatefulCanvas.STATE_OVER:isc.StatefulCanvas.STATE_UP;this.thumb.setState(_1);return isc.EventHandler.STOP_BUBBLING}
,isc.A.thumbDragStop=function isc_Scrollbar_thumbDragStop(){delete this.$14q;if(this.scrollTarget&&this.scrollTarget.doneFastScrolling)this.scrollTarget.doneFastScrolling();return this.thumbUp()}
,isc.A.keyPress=function isc_Scrollbar_keyPress(){return this.ns.EH.bubbleEvent(this.scrollTarget,this.ns.EH.eventTypes.KEY_PRESS)}
,isc.A.keyDown=function isc_Scrollbar_keyDown(){return this.ns.EH.bubbleEvent(this.scrollTarget,this.ns.EH.eventTypes.KEY_DOWN)}
,isc.A.keyUp=function isc_Scrollbar_keyUp(){return this.ns.EH.bubbleEvent(this.scrollTarget,this.ns.EH.eventTypes.KEY_UP)}
,isc.A.mouseWheel=function isc_Scrollbar_mouseWheel(){return this.ns.EH.bubbleEvent(this.scrollTarget,this.ns.EH.eventTypes.MOUSE_WHEEL)}
,isc.A.hide=function isc_Scrollbar_hide(_1,_2,_3,_4){this.invokeSuper("Scrollbar","hide",_1,_2,_3,_4);if(!this.$z0&&this.scrollTarget!=null){this.moveTo(this.scrollTarget.getLeft(),this.scrollTarget.getTop());this.resizeTo(1,1)}}
);isc.B._maxIndex=isc.C+44;isc.ClassFactory.defineClass("NativeScrollbar","Canvas");isc.A=isc.NativeScrollbar;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.getScrollbarSize=function isc_c_NativeScrollbar_getScrollbarSize(){return isc.Element.getNativeScrollbarSize()}
);isc.B._maxIndex=isc.C+1;isc.A=isc.NativeScrollbar.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.vertical=true;isc.A.showCustomScrollbars=false;isc.A.overflow="hidden";isc.A.autoEnable=true;isc.A.scrollbarCanvasDefaults={overflow:"scroll",showCustomScrollbars:false,$nd:function(_1,_2){this.Super("$nd",arguments);if(isc.Browser.isMoz&&!_1&&(_2||isc.Browser.geckoVersion<20030312))
{return}
if(this.$z4)return;this.creator.scrollbarCanvasScrolled()},parentResized:function(){this.creator.sizeScrollbarCanvas();this.creator.adjustOverflow()}};isc.A.scrollbarCanvasThickness=100;isc.B.push(isc.A.initWidget=function isc_NativeScrollbar_initWidget(){var _1=isc.NativeScrollbar.getScrollbarSize();if(this.vertical){this.setWidth(_1)}else{this.setHeight(_1)}
this.setOverflow(isc.Canvas.HIDDEN);this.addAutoChild("scrollbarCanvas");this.sizeScrollbarCanvas();this.setScrollTarget();this.setThumb()}
,isc.A.sizeScrollbarCanvas=function isc_NativeScrollbar_sizeScrollbarCanvas(){var _1=isc.Element.getNativeScrollbarSize();var _2=this.vertical?this.scrollbarCanvasThickness:this.getInnerWidth()+(this.showCorner?0:_1),_3=!this.vertical?this.scrollbarCanvasThickness:this.getInnerHeight()+(this.showCorner?0:_1);this.scrollbarCanvas.resizeTo(_2,_3)}
,isc.A.$zp=function isc_NativeScrollbar__adjustOverflow(){this.Super("$zp",arguments);if(this.vertical){this.scrollToTop();this.scrollToRight()}else{this.scrollToLeft();this.scrollToBottom()}}
,isc.A.setScrollTarget=function isc_NativeScrollbar_setScrollTarget(_1){if(this.$z0&&this.scrollTarget!=null&&this.isObserving(this.scrollTarget,"scrollTo")){this.ignore(this.scrollTarget,"scrollTo")}
if(_1!=null)this.scrollTarget=_1;if(this.scrollTarget==null)this.scrollTarget=this;if(this.$z0&&this.scrollTarget!=this&&this.scrollTarget!=_1){this.observe(this.scrollTarget,"scrollTo","observer.setThumb()")}}
,isc.A.setThumb=function isc_NativeScrollbar_setThumb(){if(this.$8e)return;var _1=this.scrollbarCanvas,_2=(this.$z0||this.scrollTarget.canScroll(this.vertical)),_3=1,_4=1;if(_2){var _5=this.scrollTarget.getViewportRatio(this.vertical);var _6=(this.vertical?_1.getViewportHeight():_1.getViewportWidth()),_7=Math.round(_6/ _5);if(this.vertical)_4=_7;else _3=_7}
if(_1.spacerLength!=_7){_1.setContents(isc.Canvas.spacerHTML(_3,_4));_1.spacerLength=_7}
this.moveThumb()}
,isc.A.setVisibility=function isc_NativeScrollbar_setVisibility(_1,_2,_3,_4){this.invokeSuper(isc.Scrollbar,"setVisibility",_1,_2,_3,_4);if(this.isVisible())this.setThumb()}
,isc.A.parentVisibilityChanged=function isc_NativeScrollbar_parentVisibilityChanged(_1,_2,_3,_4){this.invokeSuper(isc.Scrollbar,"parentVisibilityChanged",_1,_2,_3,_4);if(this.isVisible())this.setThumb()}
,isc.A.moveThumb=function isc_NativeScrollbar_moveThumb(){var _1=this.scrollTarget.getScrollRatio(this.vertical);var _2=this.scrollbarCanvas;var _3=this.vertical?_2.getScrollHeight()-_2.getViewportHeight():_2.getScrollWidth()-_2.getViewportWidth(),_4=Math.round(_1*_3);_2.scrollTo(this.vertical?0:_4,this.vertical?_4:0)}
,isc.A.scrollbarCanvasScrolled=function isc_NativeScrollbar_scrollbarCanvasScrolled(){var _1=this.scrollbarCanvas,_2=this.vertical?_1.getScrollTop()/(_1.getScrollHeight()-_1.getViewportHeight()):_1.getScrollLeft()/(_1.getScrollWidth()-_1.getViewportWidth());this.scrollTarget.scrollToRatio(this.vertical,_2)}
,isc.A.setShowCorner=function isc_NativeScrollbar_setShowCorner(_1){this.showCorner=_1;this.sizeScrollbarCanvas()}
);isc.B._maxIndex=isc.C+10;isc.$14r={canDrag:true,dragAppearance:"none",dragStartDistance:1,canCollapse:true,cursor:"hand",vResizeCursor:"row-resize",hResizeCursor:"col-resize",resizeInRealTime:false,$k7:false,$k6:false,overflow:"hidden",isMouseTransparent:true};isc.$14s={initWidget:function(){if(isc.isA.Img(this))this.src=this.vertical?this.vSrc:this.hSrc;if(this.vertical){this.defaultWidth=this.defaultWidth||10;this.cursor=this.hResizeCursor;this.baseStyle=this.vBaseStyle||this.baseStyle}else{this.defaultHeight=this.defaultHeight||10;this.cursor=this.vResizeCursor;this.baseStyle=this.hBaseStyle||this.baseStyle}
this.Super("initWidget",arguments);if(isc.Browser.isMoz)this.bringToFront()},makeLabel:function(){this.Super("makeLabel",arguments);this.label.addMethods({getCustomState:function(){var _1=this.masterElement;if(!_1.showClosedGrip)return
var _2=_1.target,_3=_2.visibility==isc.Canvas.HIDDEN;if((!_1.targetAfter&&_3)||(_1.targetAfter&&!_3)){return"closed"}}})},dragStart:function(){if(this.showDown)this.setState("Down");this.bringToFront()},dragMove:function(){var _1=this.vertical?(0-isc.EH.dragOffsetX):(0-isc.EH.dragOffsetY);this.resizeTarget(this.target,!this.vertical,this.resizeInRealTime,_1,null,null,this.targetAfter)},dragStop:function(){if(this.showDown)this.setState("");this.finishTargetResize(this.target,!this.vertical,this.resizeInRealTime)},click:function(){if(this.canCollapse!=true)return;var _1=this.hideTarget||this.target;if(!this.target)return;if(_1.visibility=='hidden'){if(isc.isA.Layout(_1.parentElement))_1.parentElement.showMember(_1);else _1.show();if(this.$14t!=null){this.canDrag=this.$14t;this.$14t=null}}else{if(isc.isA.Layout(_1.parentElement))_1.parentElement.hideMember(_1);else _1.hide();this.$14t=this.canDrag;this.canDrag=false}
this.setState("")}};isc.defineClass("Splitbar","StretchImg");isc.A=isc.Splitbar.getPrototype();isc.A.skinImgDir="images/Splitbar/";isc.A.imageType="stretch";isc.A.capSize=3;isc.A.vSrc="[SKIN]vsplit.gif";isc.A.hSrc="[SKIN]hsplit.gif";isc.Splitbar.addMethods(isc.$14r,isc.$14s)
isc.defineClass("ImgSplitbar","Img");isc.A=isc.ImgSplitbar.getPrototype();isc.A.skinImgDir="images/Splitbar/";isc.A.imageType="center";isc.A.hSrc="[SKIN]hgrip.png";isc.A.vSrc="[SKIN]vgrip.png";isc.A.styleName="splitbar";isc.A.showDown=true;isc.ImgSplitbar.addMethods(isc.$14r,isc.$14s)
isc.addGlobal("StretchImgSplitbar",isc.Splitbar);isc.addGlobal("LayoutResizeBar",isc.Splitbar);isc.defineClass("HSplitbar","Splitbar");isc.A=isc.HSplitbar.getPrototype();isc.A.vertical=false;isc.defineClass("VSplitbar","Splitbar");isc.defineClass("Stretchbar","Splitbar");isc.A=isc.Stretchbar.getPrototype();isc.A.canResize=false;isc.A.skinImgDir="images/Stretchbar/";isc.A.showRollOver=true;isc.defineClass("HStretchbar","Stretchbar");isc.A=isc.HStretchbar.getPrototype();isc.A.vertical=false;isc.A.src="[SKIN]hsplit.gif";isc.A.defaultHeight=10;isc.defineClass("VStretchbar","Stretchbar");isc.A=isc.VStretchbar.getPrototype();isc.A.src="[SKIN]vsplit.gif";isc.A.defaultWidth=10;isc.defineClass("Snapbar","Splitbar");isc.A=isc.Snapbar.getPrototype();isc.A.showRollOver=true;isc.A.showDown=true;isc.A.showGrip=true;isc.A.showDownGrip=true;isc.A.showRollOverGrip=true;isc.A.showClosedGrip=true;isc.A.gripImgSuffix="snap";isc.defineClass("ToolStripResizer","ImgSplitbar");isc.A=isc.ToolStripResizer.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.skinImgDir="images/ToolStrip/";isc.A.vSrc="[SKIN]resizer.png";isc.A.hSrc="[SKIN]hresizer.png";isc.A.layoutAlign="center";isc.A.resizeInRealTime=true;isc.A.showDown=false;isc.A.imageLength=20;isc.A.imageBreadth=14;isc.A.imageType="center";isc.B.push(isc.A.initWidget=function isc_ToolStripResizer_initWidget(){this.imageWidth=this.vertical?this.imageBreadth:this.imageLength;this.imageHeight=this.vertical?this.imageLength:this.imageBreadth;this.Super("initWidget",arguments)}
);isc.B._maxIndex=isc.C+1;isc.A=isc.Canvas;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$ta="%";isc.A.$14u="listPolicy";isc.B.push(isc.A.applyStretchResizePolicy=function isc_c_Canvas_applyStretchResizePolicy(_1,_2,_3,_4,_5){if(!_1)return;var _6=0,_7=0,_8=0,_9=0,_10=(_4?_1:[]),_11=this.logIsDebugEnabled(this.$14u),_3=(_3||1);if(_11&&_4)_1=_1.duplicate();for(var i=0;i<_1.length;i++){_9=_1[i];if(_9==null||isc.is.emptyString(_9))_1[i]=_9=isc.star;if(isc.isA.Number(_9)){_10[i]=_9}else{if(_9==isc.star){_7++;_9=0}else if(_9.indexOf(this.$ta)>=0){if(_5!=null&&_5.fixedPercents){var _13=parseInt(_9);_9=_10[i]=Math.round((_13/ 100)*_2)}else{_6+=parseInt(_9);_9=0}}else{if(_5&&isc.isA.Number(_5[_9])){_9=_10[i]=_5[_9]}else{var _14=parseInt(_9);if(isc.isA.Number(_14)&&_14>=0){_10[i]=_9=_14}else{try{_9=isc.eval(_9)}catch(e){var _15=_5&&_5.logWarn?_5:this;_15.logWarn("StretchResizePolicy: "+" unable to convert size:"+_9+" to a valid size - reported error: '"+e+"'\n Complete set of sizes:"+_1);_9=null}
if(!isc.isA.Number(_9))_9=0;_10[i]=_9}}}}
_9=Math.max(_9,0);_8+=_9}
var _16=0;if(_7){if(_6>=100){_8+=(_7*_3)}else{_16=(100-_6)/_7;_6=100}}
if(_6>0){var _17=_2-_8,_18=Math.max(0,_17/ _6),_19=null;for(i=0;i<_1.length;i++){_9=_1[i];if(isc.isA.String(_9)){var _20;if(_9==isc.star){_20=_16*_18}else if(_9.indexOf(this.$ta)>=0){_20=parseInt(_9)*_18}else{continue}
_20=Math.max(Math.floor(_20),_3);_17-=_20;_19=i;_10[i]=_20}}
if(_17>0)_10[_19]+=_17}
if(_11){this.logDebug("stretchResize"+(_5?" for "+_5.ID:"")+" with totalSize: "+_2+",  desired sizes: "+_1+",  calculated sizes: "+_10,"listPolicy")}
return _10}
);isc.B._maxIndex=isc.C+1;isc.ClassFactory.defineClass("GroupingMessages");isc.A=isc.GroupingMessages;isc.A.upcomingTodayTitle="Today";isc.A.upcomingTomorrowTitle="Tomorrow";isc.A.upcomingThisWeekTitle="This Week";isc.A.upcomingNextWeekTitle="Next Week";isc.A.upcomingNextMonthTitle="Next Month";isc.A.upcomingBeforeTitle="Before";isc.A.upcomingLaterTitle="Later";isc.A.byDayTitle="by Day";isc.A.byWeekTitle="by Week";isc.A.byMonthTitle="by Month";isc.A.byQuarterTitle="by Quarter";isc.A.byYearTitle="by Year";isc.A.byDayOfMonthTitle="by Day of Month";isc.A.byUpcomingTitle="by Upcoming";isc.A.byHoursTitle="by Hours";isc.A.byMinutesTitle="by Minutes";isc.A.bySecondsTitle="by Seconds";isc.A.byMillisecondsTitle="by Milliseconds";isc.builtinTypes={text:{validators:{type:"isString",typeCastValidator:true}},"boolean":{validators:{type:"isBoolean",typeCastValidator:true}},integer:{validators:{type:"isInteger",typeCastValidator:true},normalDisplayFormatter:function(_1,_2){if(isc.isA.Number(_1))return _1.toFormattedString();return _1},getGroupValue:function(_1,_2,_3,_4,_5){var g=_3.groupGranularity;return g?Math.ceil(_1/ g):_1},getGroupTitle:function(_1,_2,_3,_4,_5){var g=_3.groupGranularity;return g?((_1-1)*g)+" - "+(_1*g):_1}},"float":{validators:{type:"isFloat",typeCastValidator:true},normalDisplayFormatter:function(_1,_2){if(isc.isA.Number(_1))return _1.toFormattedString();return _1},getGroupValue:function(_1,_2,_3,_4,_5){_3.groupPrecision=parseInt(_3.groupPrecision);if(_3.groupPrecision<0)_3.groupPrecision=_3.groupPrecision*-1;var p=_3.groupPrecision?Math.pow(10,_3.groupPrecision):null;return p?Math.floor(_1*p)/p:_1},getGroupTitle:function(_1,_2,_3,_4,_5){return _3.groupPrecision?_1+"*":_1}},date:{validators:{type:"isDate",typeCastValidator:true},normalDisplayFormatter:function(_1,_2){if(isc.isA.Date(_1))return _1.toNormalDate();return _1},groupingModes:{day:isc.GroupingMessages.byDayTitle,week:isc.GroupingMessages.byWeekTitle,month:isc.GroupingMessages.byMonthTitle,quarter:isc.GroupingMessages.byQuarterTitle,year:isc.GroupingMessages.byYearTitle,dayOfMonth:isc.GroupingMessages.byDayOfMonthTitle,upcoming:isc.GroupingMessages.byUpcomingTitle},defaultGroupingMode:"day",groupingMode:this.defaultGroupingMode,getGroupValue:function(_1,_2,_3,_4,_5){var _6=_1;var _7=_3.groupingMode=(_3.groupingMode||_3.$14v.defaultGroupingMode||null);if(isc.isA.Date(_1)&&_7){switch(_7){case"year":_6=_1.getFullYear();break;case"quarter":_6=Math.floor(_1.getMonth()/3)+1;break;case"month":_6=_1.getMonth();break;case"week":_6=_1.getWeek();break;case"day":case"dayOfWeek":_6=_1.getDay();break;case"dayOfMonth":_6=_1.getDate();break;case"timezoneHours":_6=_1.getTimezoneOffset()/60;break;case"timezoneMinutes":_6=_1.getTimezoneOffset();break;case"timezoneSeconds":_6=_1.getTimezoneOffset()*60;break;case"upcoming":var _8=new Date();if(_8.isToday(_1))return 1;else if(_8.isTomorrow(_1))return 2;else if(_8.isThisWeek(_1))return 3;else if(_8.isNextWeek(_1))return 4;else if(_8.isNextMonth(_1))return 5;else if(_8.isBeforeToday(_1))return 7;else return 6;break}}
return _6},getGroupTitle:function(_1,_2,_3,_4,_5){var _6=_1;var _7=_3.groupingMode=(_3.groupingMode||_3.$14v.defaultGroupingMode||null);if(_7&&_1!="-none-"){switch(_7){case"month":_6=Date.getShortMonthNames()[_1];break;case"quarter":_6="Q"+_1;break;case"week":_6="Week #"+_1;break;case"day":case"dayOfWeek":_6=Date.getShortDayNames()[_1];break;case"dayOfMonth":_6=_1;break;case"timezoneHours":_6="GMT+"+_1;break;case"timezoneMinutes":_6="GMT+"+_1+" minutes";break;case"timezoneSeconds":_6="GMT+"+_1+" seconds";break;case"upcoming":var _8=new Date();if(_1==1)return isc.GroupingMessages.upcomingTodayTitle;else if(_1==2)return isc.GroupingMessages.upcomingTomorrowTitle;else if(_1==3)return isc.GroupingMessages.upcomingThisWeekTitle;else if(_1==4)return isc.GroupingMessages.upcomingNextWeekTitle;else if(_1==5)return isc.GroupingMessages.upcomingNextMonthTitle;else if(_1==7)return isc.GroupingMessages.upcomingBeforeTitle;else return isc.GroupingMessages.upcomingLaterTitle;break}}
return _6}},time:{validators:{type:"isTime",typeCastValidator:true},normalDisplayFormatter:function(_1,_2){if(isc.isA.Date(_1))return isc.Time.toTime(_1,null,true);return _1},groupingModes:{hours:isc.GroupingMessages.byHoursTitle,minutes:isc.GroupingMessages.byMinutesTitle,seconds:isc.GroupingMessages.bySecondsTitle,milliseconds:isc.GroupingMessages.byMillisecondsTitle},defaultGroupingMode:"hours",groupingMode:this.defaultGroupingMode,getGroupValue:function(_1,_2,_3,_4,_5){var _6=_1;var _7=_3.groupingMode=(_3.groupingMode||_3.$14v.defaultGroupingMode||null);if(isc.isA.Date(_1)&&_7){switch(_7){case"hours":_6=_1.getHours();break;case"minutes":_6=_1.getMinutes();break;case"seconds":_6=_1.getSeconds();break;case"milliseconds":_6=_1.getMilliseconds();break}}
return _6},getGroupTitle:function(_1,_2,_3,_4,_5){var _6=_1;var _7=_3.groupingMode||_3.$14v.defaultGroupingMode||null;if(_7&&_1!="-none-"){switch(_7){case"hours":case"minutes":case"seconds":case"milliseconds":_6=_1;break}}
return _6}},string:{inheritsFrom:"text"},"int":{inheritsFrom:"integer"},"long":{inheritsFrom:"integer"},number:{inheritsFrom:"integer"},decimal:{inheritsFrom:"float"},"double":{inheritsFrom:"float"},datetime:{inheritsFrom:"date",normalDisplayFormatter:function(_1,_2){if(isc.isA.Date(_1))return _1.toShortDateTime(null,true);return _1}},dateTime:{inheritsFrom:"datetime"},positiveInteger:{inheritsFrom:"integer",validators:{type:"integerRange",min:0}},integerPercent:{inheritsFrom:"integer",validators:{type:"integerRange",min:0,max:100}},percent:{inheritsFrom:"integerPercent"},sequence:{inheritsFrom:"integer"},"enum":{validators:"isOneOf"},"intEnum":{inheritsFrom:"integer",validators:"isOneOf"},regexp:{inheritsFrom:"text",validators:"isRegexp"},identifier:{inheritsFrom:"text",validators:"isIdentifier"},URL:{inheritsFrom:"text"},image:{inheritsFrom:"text"},HTML:{inheritsFrom:"text"},measure:{validators:"isMeasure"},integerOrAuto:{validators:"integerOrAuto"},expression:{inheritsFrom:"text"},method:{inheritsFrom:"text"},"function":{inheritsFrom:"text"},alignEnum:{inheritsFrom:"enum",valueMap:{left:"left",center:"center",right:"right"}},valignEnum:{inheritsFrom:"enum",valueMap:{top:"top",bottom:"bottom",center:"center"}},sideEnum:{inheritsFrom:"enum",valueMap:{left:"left",right:"right",top:"top",bottom:"bottom"}},color:{inheritsFrom:"string",validators:"isColor"},modifier:{inheritsFrom:"text",hidden:true,canEdit:false},modifierTimestamp:{inheritsFrom:"datetime",hidden:true,canEdit:false},creator:{inheritsFrom:"text",hidden:true,canEdit:false},creatorTimestamp:{inheritsFrom:"datetime",hidden:true,canEdit:false},password:{inheritsFrom:"text",normalDisplayFormatter:function(_1,_2){return new Array((_1&&_1.length>0?_1.length+1:0)).join("*")},shortDisplayFormatter:function(_1,_2){return new Array((_1&&_1.length>0?_1.length+1:0)).join("*")}}};(function(){for(var _1 in isc.builtinTypes){isc.builtinTypes[_1].name=_1}})();isc.defineClass("SimpleType");isc.A=isc.SimpleType;isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.$14w="typeCastValidator";isc.A.$14x={title:function(_1,_2){if(_2.summaryValueTitle!=null)return _2.summaryValueTitle;return _2.title},sum:function(_1,_2){var _3=_2.name;if(!_3)return;var _4=0;for(var i=0;i<_1.length;i++){var _6=_1[i][_3],_7=parseFloat(_6);if(_6==null||_6==isc.emptyString)continue;if(isc.isA.Number(_7)&&(_7==_6))_4+=_7;else return null}
return _4},avg:function(_1,_2){var _3=_2.name;if(!_3)return;var _4=0,_5=0;for(var i=0;i<_1.length;i++){var _7=_1[i][_3],_8=parseFloat(_7);if(_7==null||_7==isc.emptyString)continue;if(isc.isA.Number(_8)&&(_8==_7)){_5+=1;_4+=_8}else{return null}}
return _5>0?_4/ _5:null},max:function(_1,_2){var _3=_2?_2.name:null;if(!_3)return;var _4=(_2&&(_2.type=="date"));var _5;for(var i=0;i<_1.length;i++){var _7=_1[i][_3];if(_7==null||_7==isc.emptyString)continue;if(_4){if(!isc.isA.Date(_7))return null;if(_5==null||_7.getTime()>_5.getTime())_5=_7.duplicate()}else{var _8=parseFloat(_7);if(isc.isA.Number(_8)&&(_8==_7)){if(_5==null)_5=_8;else if(_5<_7)_5=_8}else{return null}}}
return _5},min:function(_1,_2){var _3=_2?_2.name:null;if(!_3)return;var _4=(_2.type=="date")
var _5;for(var i=0;i<_1.length;i++){var _7=_1[i][_3];if(_7==null||_7==isc.emptyString)continue;if(_4){if(!isc.isA.Date(_7))return null;if(_5==null||_7.getTime()<_5.getTime())_5=_7.duplicate()}else{var _8=parseFloat(_7);if(isc.isA.Number(_8)&&(_8==_7)){if(_5==null)_5=_8;else if(_5>_7)_5=_8}else{return null}}}
return _5},multiplier:function(_1,_2){var _3=_2?_2.name:null;if(!_3)return;var _4=0;for(var i=0;i<_1.length;i++){var _6=_1[i][_3];var _7=parseFloat(_6);if(isc.isA.Number(_7)&&(_7==_6)){if(i==0)_4=_7;else _4=(_4*_7)}else{return null}}
return _4},count:function(_1,_2){return _1.length}};isc.B.push(isc.A.getType=function isc_c_SimpleType_getType(_1,_2){if(_2)return _2.getType(_1);var _3=isc.builtinTypes[_1];return _3}
,isc.A.getBaseType=function isc_c_SimpleType_getBaseType(_1,_2){if(isc.isA.String(_1))_1=this.getType(_1,_2);if(_1==null)return null;while(_1.inheritsFrom){var _3=this.getType(_1.inheritsFrom,_2);if(_3==null)return null;_1=_3}
return _1.name}
,isc.A.inheritsFrom=function isc_c_SimpleType_inheritsFrom(_1,_2,_3){if(_2==null){this.logWarn("inheritsFrom passed null type");return false}
if(isc.isA.String(_1))_1=this.getType(_1,_3);if(_1==null)return false;if(_1.name==_2)return true;while(_1.inheritsFrom){var _4=this.getType(_1.inheritsFrom,_3);if(_4==null)return null;if(_4.name==_2)return true;_1=_4}
return false}
,isc.A.validateValue=function isc_c_SimpleType_validateValue(_1,_2,_3){var _4={name:"$14y",type:_1};isc.SimpleType.addTypeDefaults(_4);var _3=_3||isc.DS.get("Object");return _3.validateFieldValue(_4,_2)}
,isc.A.addTypeDefaults=function isc_c_SimpleType_addTypeDefaults(_1,_2){if(_1==null||_1.$14z)return;_1.$14z=true;var _3=this.getType(_1.type,_2);if(_3==null)return;_1.$14v=_3;if(_1.valueMap==null){var _4=this.getInheritedProperty(_3,"valueMap",_2);if(_4!=null)_3.valueMap=_1.valueMap=_4}
if(_1.editorType==null){var _5=this.getInheritedProperty(_3,"editorType",_2);if(_5!=null)_3.editorType=_1.editorType=_5}
if(_1.readOnlyEditorType==null){var _5=this.getInheritedProperty(_3,"readOnlyEditorType",_2);if(_5!=null)_3.readOnlyEditorType=_1.readOnlyEditorType=_5}
var _6=this.getInheritedProperty(_3,"editorProperties",_2);if(_6!=null){if(_1.editorProperties!=null){isc.addProperties(_6,_1.editorProperties)}
_1.editorProperties=_6}
var _7=this.getInheritedProperty(_3,"readOnlyEditorProperties",_2);if(_7!=null){if(_1.readOnlyEditorProperties!=null){isc.addProperties(_7,_1.readOnlyEditorProperties)}
_1.readOnlyEditorProperties=_7}
var _8=this.getInheritedProperty(_3,"shortDisplayFormatter",_2)
if(_8!=null)_3.shortDisplayFormatter=_1.$140=_8;var _8=this.getInheritedProperty(_3,"normalDisplayFormatter",_2)
if(_8!=null)_3.normalDisplayFormatter=_1.$141=_8;var _8=this.getInheritedProperty(_3,"editFormatter",_2)
if(_8!=null)_3.editFormatter=_1.$142=_8;var _9=this.getInheritedProperty(_3,"parseInput",_2)
if(_9!=null)_3.parseInput=_1.$143=_9;var _10=this.getValidators(_3,_2);if(_10==null)return;if(!_1.validators){_1.validators=_10}else{if(!isc.isAn.Array(_1.validators))_1.validators=[_1.validators];_1.validators.addAsList(_10);this.$144(_1.validators)}}
,isc.A.getInheritedProperty=function isc_c_SimpleType_getInheritedProperty(_1,_2,_3){while(_1!=null){if(_1[_2]!=null)return _1[_2]
_1=this.getType(_1.inheritsFrom,_3)}}
,isc.A.getValidators=function isc_c_SimpleType_getValidators(_1,_2){if(isc.isA.String(_1))_1=this.getType(_1,_2);if(_1.$145)return _1.validators;var _3=_1.validators;if(_3!=null){if(!isc.isAn.Array(_3))_3=[_3];var _4=[];for(var i=0;i<_3.length;i++){var _6=_3[i];if(isc.isA.String(_6)){_6={"type":_6}}else if(_6.type==null&&isc.isAn.emptyObject(_6)){continue}
_6._generated=true;_4.add(_6)}
_3=_4}
var _7=_1.inheritsFrom;if(_7!=null){var _8=this.getType(_7,_2);if(_8!=null){var _9=this.getValidators(_8,_2);if(_9!=null){_3=_3||[];_3.addAsList(_9);this.$144(_3)}}}
if(_3)_3.$3v=true;_1.validators=_3;_1.$145=true;return _3}
,isc.A.$144=function isc_c_SimpleType__reorderTypeValidator(_1){var _2=_1.find(this.$14w,true);if(_2){var _3=_2.type;for(var i=0;i<_1.length;i++){if(_1[i].type==_3)break}
if(i!=0)_1.unshift(_1[i]);_1[0].stopIfFalse=true}}
,isc.A.registerSummaryFunction=function isc_c_SimpleType_registerSummaryFunction(_1,_2){if(_1==null)return;if(isc.isA.String(_2)){_2=isc.Func.expressionToFunction("records,field",_1)}
this.$14x[_1]=_2}
,isc.A.setDefaultSummaryFunction=function isc_c_SimpleType_setDefaultSummaryFunction(_1,_2){var _3=this.getType(_1);if(_3)_3.$146=_2}
,isc.A.getDefaultSummaryFunction=function isc_c_SimpleType_getDefaultSummaryFunction(_1){var _2=this.getType(_1);if(_2){if(_2.$146!=null){return _2.$146}
if(_2.inheritsFrom!=null&&_2.inheritsFrom!=_1){return this.getDefaultSummaryFunction(_2.inheritsFrom)}}}
,isc.A.applySummaryFunction=function isc_c_SimpleType_applySummaryFunction(_1,_2,_3){if(!_3||!_2||!_1)return;if(isc.isA.String(_3)){if(this.$14x[_3]){_3=this.$14x[_3]}else{_3=isc.Func.expressionToFunction("records,field",_3)}}
if(isc.isA.Function(_3)){return _3(_1,_2)}}
);isc.B._maxIndex=isc.C+12;isc.SimpleType.setDefaultSummaryFunction("integer","sum");isc.SimpleType.setDefaultSummaryFunction("float","sum");isc.A=isc.SimpleType.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.B.push(isc.A.init=function isc_SimpleType_init(){if(!this.name)this.name=isc.ClassFactory.getNextGlobalID(this);if(isc.builtinTypes[this.name]!=null){if(!this.xmlSource){this.logWarn("SimpleType '"+this.name+"' defined twice: "+this.getStackTrace());isc.builtinTypes[this.name]=this}}else{isc.builtinTypes[this.name]=this}
if(this.validOperators!=null){isc.DataSource.setTypeOperators(this.name,this.validOperators)}}
);isc.B._maxIndex=isc.C+1;isc.SimpleType.getPrototype().toString=function(){return"["+this.Class+" name="+this.name+(this.inheritsFrom?" inheritsFrom="+this.inheritsFrom:"")+"]"};isc.defineClass("NavigationButton","Button");isc.A=isc.NavigationButton.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.height=30;isc.A.autoFit=true;isc.A.baseStyle="navButton";isc.A.backBaseStyle="navBackButton";isc.A.forwardBaseStyle="navForwardButton";isc.A.direction="none";isc.B.push(isc.A.initWidget=function isc_NavigationButton_initWidget(){this.setBaseStyle(this.getBaseStyleName())}
,isc.A.setNavigationDirection=function isc_NavigationButton_setNavigationDirection(_1){this.direction=_1;this.setBaseStyle(this.getBaseStyleName())}
,isc.A.getNavigationDirection=function isc_NavigationButton_getNavigationDirection(){return this.direction}
,isc.A.getBaseStyleName=function isc_NavigationButton_getBaseStyleName(){if(this.direction=="back"){return this.backBaseStyle}
if(this.direction=="forward"){return this.forwardBaseStyle}
return this.baseStyle}
);isc.B._maxIndex=isc.C+4;isc.defineClass("NavigationBar","HLayout");isc.A=isc.NavigationBar.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.width="100%";isc.A.height=44;isc.A.styleName="navToolbar";isc.A.leftButtonTitle="&nbsp;";isc.A.leftButtonDefaults={_constructor:"NavigationButton",direction:"back",layoutAlign:"center"};isc.A.titleLabelDefaults={_constructor:"Label",width:"*",styleName:"navBarHeader",align:"center",valign:"center"};isc.A.rightButtonTitle="&nbsp;";isc.A.rightButtonDefaults={_constructor:"NavigationButton",direction:"forward",layoutAlign:"center"};isc.A.showRightButton=false;isc.A.autoChildren=["leftButton","titleLabel","rightButton"];isc.A.controls=["leftButton","titleLabel","rightButton"];isc.B.push(isc.A.setControls=function isc_NavigationBar_setControls(_1){this.controls=_1;var _2=[];for(var i=0;i<_1.length;i++){var _4=_1[i];if(isc.isA.String(_4))_4=this[_4];_2[i]=_4}
this.setMembers(_2)}
,isc.A.initWidget=function isc_NavigationBar_initWidget(){this.Super("initWidget",arguments);var _1={click:function(){if(this.creator.navigationClick)this.creator.navigationClick(this.direction)}};if(this.leftButtonTitle!=null)_1.title=this.leftButtonTitle;if(this.leftButtonIcon!=null)_1.icon=this.leftButtonIcon;this.leftButton=this.createAutoChild("leftButton",_1);this.titleLabel=this.createAutoChild("titleLabel",{contents:this.title});var _2={click:function(){if(this.creator.navigationClick)this.creator.navigationClick(this.direction)}};if(this.rightButtonTitle!=null)_2.title=this.rightButtonTitle;if(this.rightButtonIcon!=null)_2.icon=this.rightButtonIcon;this.rightButton=this.createAutoChild("rightButton",_2);this.setControls(this.controls)}
,isc.A.setTitle=function isc_NavigationBar_setTitle(_1){this.title=_1;this.titleLabel.setContents(this.title)}
,isc.A.setLeftButtonTitle=function isc_NavigationBar_setLeftButtonTitle(_1){this.leftButtonTitle=_1;if(this.leftButton)this.leftButton.setTitle(_1)}
,isc.A.setLeftButtonIcon=function isc_NavigationBar_setLeftButtonIcon(_1){this.leftButtonIcon=_1;if(this.leftButton)this.leftButton.setIcon(_1)}
,isc.A.setShowLeftButton=function isc_NavigationBar_setShowLeftButton(_1){if(this.leftButton==null)return;var _2=(this.leftButton.visibility!=isc.Canvas.HIDDEN);if(_1==_2)return;this.leftButton.setVisibility(_1?isc.Canvas.INHERIT:isc.Canvas.HIDDEN)}
,isc.A.setRightButtonTitle=function isc_NavigationBar_setRightButtonTitle(_1){if(this.rightButton)this.rightButton.setTitle(_1)}
,isc.A.setRightButtonIcon=function isc_NavigationBar_setRightButtonIcon(_1){this.rightButtonIcon=_1;if(this.rightButton)this.rightButton.setIcon(_1)}
,isc.A.setShowRightButton=function isc_NavigationBar_setShowRightButton(_1){if(this.rightButton==null)return;var _2=(this.rightButton.visibility!=isc.Canvas.HIDDEN);if(_1==_2)return;this.rightButton.setVisibility(_1?isc.Canvas.INHERIT:isc.Canvas.HIDDEN)}
);isc.B._maxIndex=isc.C+9;isc.NavigationBar.registerStringMethods({navigationClick:"direction"});isc.defineClass("SplitPane","VLayout");isc.A=isc.SplitPane.getPrototype();isc.B=isc._allFuncs;isc.C=isc.B._maxIndex;isc.D=isc._funcClasses;isc.D[isc.C]=isc.A.Class;isc.A.mainLayoutDefaults={_constructor:"HLayout",width:"100%",height:"100%"};isc.A.leftLayoutDefaults={_constructor:"VLayout",width:320,height:"100%"};isc.A.rightLayoutDefaults={_constructor:"VLayout",width:"*",height:"100%"};isc.A.navigationLayoutDefaults={_constructor:"VLayout",width:"100%",height:"100%"};isc.A.listLayoutDefaults={_constructor:"VLayout",width:"100%",height:"100%"};isc.A.detailLayoutDefaults={_constructor:"VLayout",width:"100%",height:"100%"};isc.A.navigationBarDefaults={_constructor:"NavigationBar",hieght:44,navigationClick:function(_1){this.creator.navigationClick(_1)}};isc.A.detailToolStripDefaults={_constructor:"NavigationBar",hieght:44};isc.A.autoChildren=["mainLayout","leftLayout","rightLayout","navigationLayout","navigationBar","listLayout","detailLayout","detailToolStrip"];isc.A.showLeftButton=true;isc.A.showRightButton=true;isc.B.push(isc.A.setDetailToolButtons=function isc_SplitPane_setDetailToolButtons(_1){this.detailToolButtons=_1;this.updateDetailToolStrip()}
,isc.A.isHandset=function isc_SplitPane_isHandset(){return this.layoutMode=="handset"||isc.Browser.isHandset}
,isc.A.isTablet=function isc_SplitPane_isTablet(){return this.layoutMode=="tablet"||isc.Browser.isTablet}
,isc.A.getPageOrientation=function isc_SplitPane_getPageOrientation(){return this.pageOrientation||isc.Page.getOrientation()}
,isc.A.initWidget=function isc_SplitPane_initWidget(){this.Super("initWidget",arguments);this.addAutoChildren(this.autoChildren,"none");this.addMember(this.mainLayout);if(this.navigationPane!=null)this.$147(this.navigationPane);if(this.detailPane!=null)this.$148(this.detailPane);isc.Page.setEvent("orientationChange",this.getID()+".pageOrientationChanged()");if(this.initialPane!=null){this.currentPane=this.initialPane}else{if(this.isHandset()){if(this.listPane!=null){this.currentPane="list"}else{this.currentPane="navigation"}}else{this.currentPane="detail"}}
this.pageOrientationChanged()}
,isc.A.pageOrientationChanged=function isc_SplitPane_pageOrientationChanged(){this.updateUI()}
,isc.A.updateUI=function isc_SplitPane_updateUI(_1){var _2=this.currentUIConfig,_3=this.$149;var _4=this.currentUIConfig=this.getUIConfiguration(),_5=this.$149=this.currentPane;if(!_1&&_4==_2&&_5==_3)return;if(_4=="handset"){this.updateNavigationBar();if(_5=="navigation"){this.navigationLayout.setMembers([this.navigationBar,this.navigationPane]);this.mainLayout.setMembers([this.navigationLayout])}else if(_5=="detail"){var _6=[this.navigationBar,this.detailPane];if(this.detailToolButtons!=null){this.updateDetailToolStrip();_6.add(this.detailToolStrip)}
this.detailLayout.setMembers(_6);this.mainLayout.setMembers([this.detailLayout])}else{var _6=[this.navigationBar,this.listPane];this.listLayout.setMembers(_6);this.mainLayout.setMembers([this.listLayout])}}else if(_4=="portrait"){this.updateDetailToolStrip();this.detailLayout.setMembers([this.detailToolStrip,this.detailPane]);this.mainLayout.setMembers([this.detailLayout]);if(this.currentPane=="navigation"){if(this.listPopUp!=null&&this.listPopUp.isVisible()){this.listPopUp.hide()}
this.updateNavigationBar();if(this.navigationPopUp==null){var _7=isc.PopupWindow||isc.Window;this.navigationPopUp=this.createAutoChild("navigationPopUp",{_constructor:_7,headerControls:[this.navigationBar],items:[this.navigationPane],width:"50%",height:"80%",isModal:true,showModalMask:true,dismissOnOutsideClick:true,closeClick:function(){this.creator.showDetailPane()}})}else{this.navigationPopUp.setHeaderControls([this.navigationBar]);if(!this.navigationPopUp.items||!this.navigationPopUp.items.contains(this.navigationPane))
{this.navigationPopUp.addItem(this.navigationPane)}}
this.delayCall("$15a")}else if(this.currentPane=="list"){if(this.navigationPopUp!=null&&this.navigationPopUp.isVisible()){this.navigationPopUp.hide()}
if(this.listPopUp==null){var _7=isc.PopupWindow||isc.Window;this.listPopUp=this.createAutoChild("listPopUp",{_constructor:_7,headerControls:[this.navigationBar],items:[this.listPane],width:"50%",height:"80%",isModal:true,showModalMask:true,dismissOnOutsideClick:true,closeClick:function(){this.creator.showDetailPane()}})}else{this.listPopUp.setHeaderControls([this.navigationBar]);if(!this.listPopUp.items||!this.listPopUp.items.contains(this.listPane))
{this.listPopUp.addItem(this.listPane)}}
this.delayCall("$15b")}else{if(this.navigationPopUp!=null&&this.navigationPopUp.isVisible()){this.navigationPopUp.hide()}
if(this.listPopUp!=null&&this.listPopUp.isVisible()){this.listPopUp.hide()}}}else if(_4=="landscape"){if(this.navigationPopUp!=null){if(this.navigationPopUp.isVisible())this.navigationPopUp.hide();if(this.navigationPopUp.items&&this.navigationPopUp.items.contains(this.navigationPane))
{this.navigationPopUp.removeItem(this.navigationPane)}}
this.updateNavigationBar();this.navigationLayout.setMembers([this.navigationBar,this.navigationPane]);this.updateDetailToolStrip();this.detailLayout.setMembers([this.detailToolStrip,this.detailPane]);if(this.listPane!=null){if(_5!="navigation"){this.listLayout.setMembers([this.navigationBar,this.listPane]);this.leftLayout.setMembers([this.listLayout])}else{this.leftLayout.setMembers([this.navigationLayout])}}else{this.leftLayout.setMembers([this.navigationLayout])}
this.rightLayout.setMembers([this.detailLayout]);if(this.spacer==null){this.spacer=isc.Canvas.create({backgroundColor:"black",overflow:"hidden",height:"100%",width:1,autoDraw:false})}
this.mainLayout.setMembers([this.leftLayout,this.spacer,this.rightLayout])}else{this.updateNavigationBar();this.navigationLayout.setMembers([this.navigationBar,this.navigationPane]);this.updateDetailToolStrip();if(this.listPane!=null){this.listLayout.setMembers([this.detailToolStrip,this.listPane]);this.detailLayout.setMembers([this.detailPane])}else{this.detailLayout.setMembers([this.detailToolStrip,this.detailPane])}
this.leftLayout.setMembers([this.navigationLayout]);var _6=(this.listPane!=null?[this.listLayout]:[]);_6.add(this.detailLayout);this.rightLayout.setMembers(_6);this.mainLayout.setMembers([this.leftLayout,this.rightLayout])}}
,isc.A.$15a=function isc_SplitPane__showNavPopUp(){if(!this.navigationPopUp||(this.navigationPopUp.isVisible()&&this.navigationPopUp.isDrawn()))return;this.navigationPopUp.setPageTop(this.navigationPopUpButton.getPageBottom());this.navigationPopUp.setPageLeft(this.navigationPopUpButton.getPageLeft());this.navigationPopUp.show()}
,isc.A.$15b=function isc_SplitPane__showListPopUp(){if(!this.listPopUp||(this.listPopUp.isVisible()&&this.listPopUp.isDrawn()))return;this.listPopUp.setPageTop(this.listPopUpButton.getPageBottom());this.listPopUp.setPageLeft(this.listPopUpButton.getPageLeft());this.listPopUp.show()}
,isc.A.updateDetailToolStrip=function isc_SplitPane_updateDetailToolStrip(){if(this.currentUIConfig=="handset"){var _1=[isc.LayoutSpacer.create({width:"*"})];_1.addList(this.detailToolButtons);_1.add(isc.LayoutSpacer.create({width:"*"}));if(this.detailTitleLabel&&this.detailTitleLabel.isDrawn()){this.detailTitleLabel.deparent()}
this.detailToolStrip.setMembers(_1)}else if(this.currentUIConfig=="portrait"){if(this.navigationPopUpButton==null){this.navigationPopUpButton=this.createAutoChild("navigationPopUpButton",{_constructor:"IButton",title:this.navigationTitle,click:function(){this.creator.showNavigationPane()}})}else{this.navigationPopUpButton.setTitle(this.navigationTitle)}
if(this.listPopUpButton==null){this.listPopUpButton=this.createAutoChild("listPopUpButton",{_constructor:"IButton",title:this.listTitle,click:function(){this.creator.showListPane()}})}else{this.listPopUpButton.setTitle(this.listTitle)}
this.updateDetailTitleLabel();var _1=[(this.currentPane!="navigation"?this.listPopUpButton:this.navigationPopUpButton),this.detailNavigationControl,this.detailTitleLabel];if(this.detailToolButtons!=null){_1.addList(this.detailToolButtons)}
_1.removeEmpty();this.detailToolStrip.setMembers(_1)}else{this.updateDetailTitleLabel();var _1=[this.detailTitleLabel];if(this.detailToolButtons!=null){_1.addList(this.detailToolButtons)}
this.detailToolStrip.setMembers(_1)}}
,isc.A.updateDetailTitleLabel=function isc_SplitPane_updateDetailTitleLabel(){if(this.detailTitleLabel==null){this.detailTitleLabel=isc.Label.create({autoDraw:false,align:"center",valign:"center",width:"*",height:this.detailToolStrip.getHeight()})}
this.detailTitleLabel.setContents(this.detailTitle)}
,isc.A.updateNavigationBar=function isc_SplitPane_updateNavigationBar(){this.logInfo("updateNavigationBar, currentPane: "+this.currentPane+", currentUI: "+this.currentUIConfig);if((this.currentUIConfig=="handset"&&this.currentPane!="navigation")||(this.currentUIConfig=="portrait"&&this.currentPane=="list")||(this.currentUIConfig=="landscape"&&this.currentPane!="navigation"))
{var _1;if(this.currentUIConfig=="landscape"){_1=this.listPane!=null&&this.listPane.isVisible()?this.listTitle:this.navigationTitle}else{_1=(this.currentPane=="detail"?this.detailTitle:(this.currentPane=="list"?this.listTitle:this.navigationTitle))}
if(_1==null)_1="&nbsp;";this.navigationBar.setTitle(_1);if(this.showNavigationPaneButton==null){this.showNavigationPaneButton=this.createAutoChild("showNavigationPaneButton",{_constructor:isc.NavigationButton,direction:"back",title:(this.currentPane=="detail"&&this.listPane!=null?this.listTitle:this.navigationTitle),click:function(){if(this.creator.currentPane=="detail"&&this.creator.listPane!=null&&!(this.creator.listPane.isVisible()&&this.creator.listPane.isDrawn()))
{this.creator.showListPane()}else{this.creator.showNavigationPane()}}})}else{this.showNavigationPaneButton.setTitle(this.currentPane=="detail"&&this.listPane!=null&&!(this.listPane.isVisible()&&this.listPane.isDrawn())?this.listTitle:this.navigationTitle)}
var _2=[this.showNavigationPaneButton,"titleLabel"];if(this.detailNavigationControl!=null){_2.add(this.detailNavigationControl)}
this.navigationBar.setControls(_2)}else{this.navigationBar.setTitle(this.navigationTitle);this.navigationBar.setLeftButtonTitle(this.leftButtonTitle);this.navigationBar.setRightButtonTitle(this.rightButtonTitle);this.navigationBar.setControls(["leftButton","titleLabel","rightButton"]);this.navigationBar.setShowLeftButton(this.showLeftButton);this.navigationBar.setShowRightButton(this.showRightButton)}}
,isc.A.$15c=function isc_SplitPane__getShowNavigationPaneButton(_1){if(this.showNavigationPaneButton==null){this.showNavigationPaneButton=this.createAutoChild("showNavigationPaneButton",{_constructor:isc.NavigationButton,direction:"back",title:_1,click:function(){this.creator.showNavigationPane()}})}else{this.showNavigationPaneButton.setTitle(_1)}
return this.showNavigationPaneButton}
,isc.A.getUIConfiguration=function isc_SplitPane_getUIConfiguration(){if(this.uiConfiguration!=null)return this.uiConfiguration;if(this.isHandset())return"handset";else if(this.isTablet()&&this.getPageOrientation()=="portrait")return"portrait";else if(this.isTablet()&&this.getPageOrientation()=="landscape")return"landscape";else return"desktop"}
,isc.A.setShowLeftButton=function isc_SplitPane_setShowLeftButton(_1){this.showLeftButton=_1;this.updateNavigationBar()}
,isc.A.setLeftButtonTitle=function isc_SplitPane_setLeftButtonTitle(_1){this.leftButtonTitle=_1;this.updateNavigationBar()}
,isc.A.setLeftButtonIcon=function isc_SplitPane_setLeftButtonIcon(_1){this.leftButtonIcon=_1;this.updateNavigationBar()}
,isc.A.setShowRightButton=function isc_SplitPane_setShowRightButton(_1){this.showRightButton=_1;this.updateNavigationBar()}
,isc.A.setRightButtonTitle=function isc_SplitPane_setRightButtonTitle(_1){this.rightButtonTitle=_1;this.updateNavigationBar()}
,isc.A.setRightButtonIcon=function isc_SplitPane_setRightButtonIcon(_1){this.rightButtonIcon=_1;this.updateNavigationBar()}
,isc.A.$147=function isc_SplitPane__setNavigationPane(_1){this.navigationPane=_1;this.navigationPane.setWidth("100%");this.navigationPane.setHeight("100%");this.navigationPane.splitPane=this}
,isc.A.setNavigationPane=function isc_SplitPane_setNavigationPane(_1){this.$147(_1);if(this.currentView=="navigation"){this.updateUI(true)}}
,isc.A.setNavigationTitle=function isc_SplitPane_setNavigationTitle(_1){this.navigationTitle=_1;this.updateNavigationBar()}
,isc.A.showNavigationPane=function isc_SplitPane_showNavigationPane(){var _1=this.currentPane!=null&&this.currentPane!="navigation";this.currentPane="navigation";this.updateUI();if(_1&&this.paneChanged!=null)this.paneChanged("navigation")}
,isc.A.$15d=function isc_SplitPane__setListPane(_1){this.listPane=_1;this.listPane.setWidth("100%");this.listPane.setHeight("100%");this.listPane.splitPane=this}
,isc.A.setListPane=function isc_SplitPane_setListPane(_1){this.$15d(_1);this.updateUI(true)}
,isc.A.showListPane=function isc_SplitPane_showListPane(){var _1=(this.listPane!=null?"list":"detail");var _2=(_1!=this.currentPane);this.currentPane=_1
this.updateUI();if(_2&&this.paneChanged!=null)this.paneChanged(_1)}
,isc.A.setListTitle=function isc_SplitPane_setListTitle(_1){this.listTitle=_1;this.updateNavigationBar();this.updateDetailToolStrip()}
,isc.A.$148=function isc_SplitPane__setDetailPane(_1){this.detailPane=_1;this.detailPane.setWidth("100%");this.detailPane.setHeight("100%");this.detailPane.splitPane=this}
,isc.A.setDetailPane=function isc_SplitPane_setDetailPane(_1){this.$148(_1);this.updateUI(true)}
,isc.A.showDetailPane=function isc_SplitPane_showDetailPane(){var _1=(this.currentPane!="detail");this.currentPane="detail";this.updateUI();if(_1&&this.paneChanged!=null)this.paneChanged("detail")}
,isc.A.setDetailTitle=function isc_SplitPane_setDetailTitle(_1){this.detailTitle=_1;if(this.currentUIConfig=="handset"){if(this.currentPane=="detail")this.updateNavigationBar()}else{this.updateDetailToolStrip()}}
,isc.A.navigationClick=function isc_SplitPane_navigationClick(_1){}
,isc.A.setDetailNavigationControl=function isc_SplitPane_setDetailNavigationControl(_1){this.detailNavigationControl=_1;var _2=this.currentUIConfig!="landscape"&&this.currentPane=="detail";if(_2)this.updateUI(true)}
);isc.B._maxIndex=isc.C+34;isc.SplitPane.registerStringMethods({paneChanged:"pane"});isc._moduleEnd=isc._Foundation_end=(isc.timestamp?isc.timestamp():new Date().getTime());if(isc.Log&&isc.Log.logIsInfoEnabled('loadTime'))isc.Log.logInfo('Foundation module init time: ' + (isc._moduleEnd-isc._moduleStart) + 'ms','loadTime');delete isc.definingFramework;}else{if(window.isc && isc.Log && isc.Log.logWarn)isc.Log.logWarn("Duplicate load of module 'Foundation'.");}
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

