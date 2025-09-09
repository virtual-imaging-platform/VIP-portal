package fr.insalyon.creatis.vip.core.client.view;

import jsinterop.annotations.JsType;
import jsinterop.annotations.JsPackage;

@JsType(isNative = true, name = "console", namespace = JsPackage.GLOBAL)
public class Console {
    public static native void log(Object message);
}