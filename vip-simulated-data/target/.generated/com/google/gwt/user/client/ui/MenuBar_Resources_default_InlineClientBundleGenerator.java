package com.google.gwt.user.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class MenuBar_Resources_default_InlineClientBundleGenerator implements com.google.gwt.user.client.ui.MenuBar.Resources {
  public com.google.gwt.resources.client.ImageResource menuBarSubMenuIcon() {
    return menuBarSubMenuIcon;
  }
  private void _init0() {
    menuBarSubMenuIcon = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
    "menuBarSubMenuIcon",
    com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ?externalImage_rtl : externalImage,
    0, 0, 5, 9, false, false
  );
  }
  
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static final java.lang.String externalImage = "data:image/gif;base64,R0lGODlhBQAJAIAAAAAAAAAAACH5BAEAAAEALAAAAAAFAAkAAAIMRB5gp9v2YlJsJRQKADs=";
  private static final java.lang.String externalImage_rtl = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAJCAYAAAD6reaeAAAAIUlEQVR42mNgwAT/sQn8xybwH5vAf2wCuFXiNBOn7XAJAL1wGOgatBIBAAAAAElFTkSuQmCC";
  private static com.google.gwt.resources.client.ImageResource menuBarSubMenuIcon;
  
  static {
    new MenuBar_Resources_default_InlineClientBundleGenerator()._init0();
  }
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      menuBarSubMenuIcon(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("menuBarSubMenuIcon", menuBarSubMenuIcon());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'menuBarSubMenuIcon': return this.@com.google.gwt.user.client.ui.MenuBar.Resources::menuBarSubMenuIcon()();
    }
    return null;
  }-*/;
}
