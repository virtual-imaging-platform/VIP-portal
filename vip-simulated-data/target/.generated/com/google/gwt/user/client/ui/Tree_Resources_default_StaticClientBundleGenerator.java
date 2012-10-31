package com.google.gwt.user.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class Tree_Resources_default_StaticClientBundleGenerator implements com.google.gwt.user.client.ui.Tree.Resources {
  public com.google.gwt.resources.client.ImageResource treeClosed() {
    return treeClosed;
  }
  public com.google.gwt.resources.client.ImageResource treeLeaf() {
    return treeLeaf;
  }
  public com.google.gwt.resources.client.ImageResource treeOpen() {
    return treeOpen;
  }
  private void _init0() {
    treeClosed = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
    "treeClosed",
    bundledImage_None,
    32, 0, 16, 16, false, false
  );
    treeLeaf = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
    "treeLeaf",
    bundledImage_None,
    16, 0, 16, 16, false, false
  );
    treeOpen = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
    "treeOpen",
    bundledImage_None,
    0, 0, 16, 16, false, false
  );
  }
  
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static final java.lang.String bundledImage_None = GWT.getModuleBaseURL() + "EDC7827FEEA59EE44AD790B1C6430C45.cache.png";
  private static com.google.gwt.resources.client.ImageResource treeClosed;
  private static com.google.gwt.resources.client.ImageResource treeLeaf;
  private static com.google.gwt.resources.client.ImageResource treeOpen;
  
  static {
    new Tree_Resources_default_StaticClientBundleGenerator()._init0();
  }
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      treeClosed(), 
      treeLeaf(), 
      treeOpen(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("treeClosed", treeClosed());
        resourceMap.put("treeLeaf", treeLeaf());
        resourceMap.put("treeOpen", treeOpen());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'treeClosed': return this.@com.google.gwt.user.client.ui.Tree.Resources::treeClosed()();
      case 'treeLeaf': return this.@com.google.gwt.user.client.ui.Tree.Resources::treeLeaf()();
      case 'treeOpen': return this.@com.google.gwt.user.client.ui.Tree.Resources::treeOpen()();
    }
    return null;
  }-*/;
}
