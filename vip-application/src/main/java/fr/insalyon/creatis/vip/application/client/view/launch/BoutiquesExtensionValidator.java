package fr.insalyon.creatis.vip.application.client.view.launch;

import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplicationExtensions;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesInput;

import java.util.Optional;

public class BoutiquesExtensionValidator {

    private final BoutiquesApplication boutiquesApplication;
    private final BoutiquesApplicationExtensions extensions;

    public BoutiquesExtensionValidator(BoutiquesApplication boutiquesApplication) {
        this.boutiquesApplication = boutiquesApplication;
        this.extensions = boutiquesApplication.getBoutiquesExtensions();
    }

    public void validate() {
        this.validateUnmodifiableInputs();
    }

    private void validateUnmodifiableInputs() {
        extensions.getUnmodifiableInputs().stream()
                .map(boutiquesApplication::getInput)
                .filter(Optional::isPresent)
                .forEach(input -> validateInput(input.get(), "unmodifiable"));

        extensions.getUnmodifiableInputsByValue().keySet().stream()
                .map(boutiquesApplication::getInput)
                .filter(Optional::isPresent)
                .forEach(input -> validateUnmodifiableInputByValue(input.get()));

        extensions.getHiddenInputs().stream()
                .map(boutiquesApplication::getInput)
                .filter(Optional::isPresent)
                .forEach(input -> validateInput(input.get(), "hidden"));
    }

    private void validateUnmodifiableInputByValue(BoutiquesInput input) {
        validateInput(input, "unmodifiable");
        LaunchFormLayout.assertCondition(
                input.getType().equals(BoutiquesInput.InputType.STRING),
                "{" + input.getId() + "} has unmodifiable values but is not a string input");

        LaunchFormLayout.assertCondition(
                extensions.getNonListInputs().contains(input.getId()),
                "{" + input.getId() + "} has unmodifiable values and must not be a list");

    }

    private void validateInput(BoutiquesInput input, String constraint) {
        // no other input should disable it
        boutiquesApplication.getInputs().forEach(i -> {
            LaunchFormLayout.assertCondition(
                    ( ! i.getDisablesInputsId().contains(input.getId())),
                    "{" + input.getId() + "} is " + constraint + " but can be disabled by : " + i.getId());
            LaunchFormLayout.assertCondition(
                    (i.getValueDisablesInputsId().values().stream().noneMatch(set -> set.contains(input.getId()))),
                    "{" + input.getId() + "} is " + constraint + " but be disabled by value by : " + i.getId());
        });
        // it should not require any other input
        LaunchFormLayout.assertCondition(
                input.getRequiresInputsId() == null,
                "{" + input.getId() + "} is " + constraint + " and must not require other input");
    }
}
