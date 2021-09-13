package fr.insalyon.creatis.vip.application.client.view.launch;

import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.Source;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesInput;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesStringInput;
import sun.security.krb5.internal.crypto.Des;

/**
 * Class to parse Descriptor objects (used to represent gwendia descriptors) into BoutiquesApplication objects.
 */
public class DescriptorParser {
    /**
     * @param descriptor            Descriptor of the application
     * @param applicationName       String name of the application
     * @param applicationVersion    String version of the application
     * @return BoutiquesApplication representing the same application as provided Descriptor
     */
    public BoutiquesApplication descriptorToBoutiquesApplication(Descriptor descriptor, String applicationName,
                                                                 String applicationVersion){
        BoutiquesApplication application = new BoutiquesApplication(applicationName, descriptor.getDescription(),
                applicationVersion);
        for(Source input : descriptor.getSources()){
            BoutiquesInput.InputType type =
                    (input.getType().equals("URI")) ? BoutiquesInput.InputType.FILE : BoutiquesInput.InputType.STRING;
            application.addInput(new BoutiquesStringInput(input.getName(), input.getName(), input.getDescription(),
                    type, input.isOptional(), null, null, null,
                    null, null, input.getDefaultValue()));
        }
        return application;
    }
}
