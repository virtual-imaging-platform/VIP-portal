package fr.insalyon.creatis.vip.core.client.view.util;

import com.smartgwt.client.widgets.form.validator.MatchesFieldValidator;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;

/**
 *
 * @author Rafael Silva
 */
public class ValidatorUtil {

    /**
     * 
     * @return 
     */
    public static RegExpValidator getEmailValidator() {
        
        RegExpValidator validator = new RegExpValidator();
        validator.setErrorMessage("Invalid e-mail address");
        validator.setExpression("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$");
        return validator;
    }
    
    /**
     * 
     * @param otherField
     * @param errorMessage
     * @return 
     */
    public static MatchesFieldValidator getMatchesValidator(String otherField, 
            String errorMessage) {
        
        MatchesFieldValidator validator = new MatchesFieldValidator();
        validator.setOtherField(otherField);
        validator.setErrorMessage(errorMessage);
        return validator;
    }
    
    /**
     * 
     * @return 
     */
    public static RegExpValidator getStringValidator() {
        
        RegExpValidator validator = new RegExpValidator();  
        validator.setErrorMessage("Invalid string");  
        validator.setExpression("^([0-9.,A-Za-z-+@/_(): ])+$");
        return validator;
    }
    
     public static RegExpValidator getStringValidator(String validatorExpression) {
        
        RegExpValidator validator = new RegExpValidator();  
        validator.setErrorMessage("Invalid string");  
        validator.setExpression(validatorExpression); 
        return validator;
    }
}
