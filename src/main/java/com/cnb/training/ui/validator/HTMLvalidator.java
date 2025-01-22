package com.cnb.training.ui.validator;


import com.cnb.training.util.HTMLparser;
import com.cnb.training.util.StringUtil;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator("com.cnb.training.ui.validator.HTMLvalidator")
public class HTMLvalidator implements Validator {

    private String errMessage;

    public HTMLvalidator() {
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        errMessage = HTMLparser.parseHTMLString(value.toString());

        if (!errMessage.equalsIgnoreCase("Valid")) {

            FacesMessage msg = new FacesMessage("ERROR! Invalid HTML", errMessage);
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);

        } else {

            if (!StringUtil.checkParentheses(value.toString())) {

                FacesMessage msg = new FacesMessage("ERROR!", "Parentheses not opened/closed correctly. " + value.toString());
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);

            } else if (!StringUtil.checkSqBrackets(value.toString())) {

                FacesMessage msg = new FacesMessage("ERROR!", "Square brackets not opened/closed correctly. " + value.toString());
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }

        }


    }
}
