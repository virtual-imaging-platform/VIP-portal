package fr.insalyon.creatis.vip.application.client.view.boutiquesParsing;

import com.google.gwt.json.client.JSONObject;
import fr.insalyon.creatis.vip.application.client.view.launch.FlagInputLayout;
import fr.insalyon.creatis.vip.application.client.view.launch.LaunchFormLayout;

import java.util.Map;

/**
 * Representation of an Flag input in an application Boutiques descriptor
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class BoutiquesInputFlag extends BoutiquesInput{
    final private boolean defaultValue;

    /**
     * Initializes input information from its JSON description
     *
     * @param descriptor JSONObject describing this input
     * @throws RuntimeException if descriptor is invalid
     */
    public BoutiquesInputFlag(JSONObject descriptor){
        super(descriptor);
        this.defaultValue = BoutiquesUtil.getBooleanValue(descriptor, "default-value", true);
    }

    /**
     * @return boolean: always true (Flag input is always optional
     */
    @Override
    public boolean isOptional() {
        return true;
    }

    /**
     * Create a FlagInputLayout representing this input for user interaction
     *
     * @param layout LaunchFormLayout representing application launch form, used as InputLayout's parentLayout
     * @return       FlagInputLayout representing this
     */
    @Override
    public FlagInputLayout getLayout(LaunchFormLayout layout) {
        return new FlagInputLayout(this, layout);
    }

    /**
     * @return Always null: Flag input cannot have 'value-disables' dependencies
     */
    @Override
    public Map<String, String[]> getValueDisablesInputsId() {
        return null;
    }

    /**
     * @return Always null: Flag input cannot have 'value-requires' dependencies
     */
    @Override
    public Map<String, String[]> getValueRequiresInputsId() {
        return null;
    }

    /**
     * @return Always null: Flag cannot have value choices other than true and false
     */
    @Override
    public String[] getPossibleValues() {
        return null;
    }

    /**
     * @return Boolean representing this input's default value
     */
    @Override
    public Boolean getDefaultValue() {
        return this.defaultValue;
    }
}
