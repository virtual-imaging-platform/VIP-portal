package fr.insalyon.creatis.vip.application.client.view.launch;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.Source;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesFlagInput;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesInput;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesStringInput;

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
            BoutiquesInput boutiquesInput = null;
            // it is a flag when type is NOT "URI", and 'vip-type-restriction' is "flag"
            if ( ( ! "URI".equals(input.getType())) &&
                    input.getVipTypeRestriction() != null &&
                    "flag".equals(input.getVipTypeRestriction())) {
                boutiquesInput = handleFlags(input);
            } else {
                boutiquesInput = handleStringsAndFiles(input);
            }
            application.addInput(boutiquesInput);
        }
        return application;
    }

    private BoutiquesInput handleFlags(Source input) {
        BoutiquesInput res = new BoutiquesFlagInput(input.getName(), input.getName(), input.getDescription(),
                input.isOptional(), null, null, false);
        res.setCommandLineFlag(input.getDefaultValue());
        return res;
    }

    private BoutiquesInput handleStringsAndFiles(Source input) {
        BoutiquesInput.InputType type =
                (input.getType().equals("URI")) ? BoutiquesInput.InputType.FILE : BoutiquesInput.InputType.STRING;
        String defaultValue = input.getDefaultValue();
        if (input.isOptional() && ApplicationConstants.INPUT_WITHOUT_VALUE.equals(input.getDefaultValue())) {
            defaultValue = null;
        }
        return new BoutiquesStringInput(input.getName(), input.getName(), input.getDescription(),
                type, input.isOptional(), null, null, null,
                null, null, defaultValue);
    }
}
