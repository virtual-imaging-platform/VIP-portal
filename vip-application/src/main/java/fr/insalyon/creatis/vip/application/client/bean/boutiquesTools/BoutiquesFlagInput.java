package fr.insalyon.creatis.vip.application.client.bean.boutiquesTools;

import java.util.List;
import java.util.Map;

/**
 * Representation of an Flag input in an application Boutiques descriptor
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class BoutiquesFlagInput extends BoutiquesInput {
    final private boolean defaultValue;

    /**
     * @param id String
     * @param name String
     * @param description String
     * @param isOptional boolean
     * @param disablesInputsId List of String IDs of inputs disabled when this is non-empty
     * @param requiresInputsId List of String IDs of inputs requiring this to be non-empty
     * @param defaultValue boolean
     */
    public BoutiquesFlagInput(String id, String name, String description, boolean isOptional,
                              List<String> disablesInputsId, List<String> requiresInputsId, boolean defaultValue){
        super(id, name, description, InputType.FLAG, isOptional, disablesInputsId, requiresInputsId, null,
                null, null);
        this.defaultValue = defaultValue;
    }

    /**
     * @return boolean: always true (Flag input is always optional
     */
    @Override
    public boolean isOptional() {
        return true;
    }

    /**
     * @return Always null: Flag input cannot have 'value-disables' dependencies
     */
    @Override
    public Map<String, List<String>> getValueDisablesInputsId() {
        return null;
    }

    /**
     * @return Always null: Flag input cannot have 'value-requires' dependencies
     */
    @Override
    public Map<String, List<String>> getValueRequiresInputsId() {
        return null;
    }

    /**
     * @return Always null: Flag cannot have value choices other than true and false
     */
    @Override
    public List<String> getPossibleValues() {
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
