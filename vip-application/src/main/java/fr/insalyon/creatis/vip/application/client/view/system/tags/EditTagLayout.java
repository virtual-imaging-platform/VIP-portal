package fr.insalyon.creatis.vip.application.client.view.system.tags;

import java.util.Arrays;

import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.widgets.form.fields.BooleanItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.models.Tag;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;

public class EditTagLayout extends AbstractFormLayout {
    private TextItem keyField;
    private TextItem valueField;
    private SelectItem typeField;
    private SelectItem appVersionField;
    private CheckboxItem visibleField;
    private CheckboxItem boutiquesField;

    public EditTagLayout() {
        super(480, 200);
        addTitle("Edit tag", ApplicationConstants.ICON_TAG);

        configure();
    }

    private void configure() {
        keyField = FieldUtil.getTextItem(350, null);
        valueField = FieldUtil.getTextItem(350, null);
        visibleField = new BooleanItem().setShowTitle(false).setWidth(350);
        boutiquesField = new BooleanItem().setShowTitle(false).setWidth(350);

        typeField = new SelectItem();
        typeField.setShowTitle(false);
        typeField.setMultipleAppearance(MultipleAppearance.PICKLIST);
        typeField.setValueMap(Arrays.stream(Tag.ValueType.values())
            .map(v -> v.toString())
            .toArray(String[]::new));
        typeField.setWidth(350);

        appVersionField = new SelectItem();
        appVersionField.setShowTitle(false);
        appVersionField.setMultiple(true);
        appVersionField.setMultipleAppearance(MultipleAppearance.GRID);
        appVersionField.setWidth(350);

        addField("Key", keyField);
        addField("Value", valueField);
        addField("Type", typeField);
        addField("AppVersions", appVersionField);
        addField("Visible", visibleField);
        addField("Boutiques", boutiquesField);
    }

    public void setTag(Tag tag, String[] appVersions) {
        if (tag != null) {
            this.keyField.setValue(tag.getKey());
            this.valueField.setValue(tag.getValue());
            this.typeField.setValue(tag.getType().toString());
            this.appVersionField.setValueMap(appVersions);
            this.visibleField.setValue(tag.isVisible());
            this.boutiquesField.setValue(tag.isBoutiques());
        }
    }
}
