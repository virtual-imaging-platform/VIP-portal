package fr.insalyon.creatis.vip.application.client.bean.boutiquesTools;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BoutiquesOutputFile implements IsSerializable {

    private String id;
    private String name;
    private String description;
    private String valueKey;
    private String pathTemplate;
    private boolean list;
    private boolean optional;
    private String commandLineFlag;
    private String pathTemplateStrippedExtensionsString;
    
   
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getValueKey() {
        return valueKey;
    }

    public String getPathTemplate() {
        return pathTemplate;
    }

    public boolean isList() {
        return list;
    }

    public boolean isOptional() {
        return optional;
    }

    public String getCommandLineFlag() {
        return commandLineFlag;
    }

    public String getPathTemplateStrippedExtensionsString() {
	    return pathTemplateStrippedExtensionsString;
	}

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValueKey(String valueKey) {
        this.valueKey = valueKey;
    }

    public void setPathTemplate(String pathTemplate) {
        this.pathTemplate = pathTemplate;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public void setCommandLineFlag(String commandLineFlag) {
        this.commandLineFlag = commandLineFlag;
    }

        public void setPathTemplateStrippedExtensionsString(String pathTemplateStrippedExtensionsString) {
	    this.pathTemplateStrippedExtensionsString = pathTemplateStrippedExtensionsString;
	}
}