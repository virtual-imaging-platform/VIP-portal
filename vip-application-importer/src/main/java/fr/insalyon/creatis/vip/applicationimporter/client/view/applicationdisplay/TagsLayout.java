package fr.insalyon.creatis.vip.applicationimporter.client.view.applicationdisplay;

import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RemoveRecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RemoveRecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;

import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.client.bean.Tag.ValueType;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.applicationimporter.client.view.Constants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

public class TagsLayout extends AbstractFormLayout {

    private final HLayout hLayout;
    private final VLayout vLayoutLeft;
    private final ListGrid selectedTags;
    private final ListGrid existingsTags;
    private final LayoutSpacer spacer;

    private DynamicForm entryFormBlock;
    private ComboBoxItem entryForm;
    
    public TagsLayout(String width, String height) {
        super(width, height);

        hLayout = new HLayout(10);
        vLayoutLeft = new VLayout(10);
        selectedTags = createList(true);

        existingsTags = createList(false);
        spacer = new LayoutSpacer();
        setFormInput();

        configure();
        loadDatabaseNonBoutiquesTags();
    }

    public List<Tag> getSelectedTags(String application, String version) {
        return getTags(selectedTags, application, version);
    }

    public void setBoutiquesTags(List<Tag> boutiquesTags) {
        for (Tag tag : boutiquesTags) {
            selectedTags.addData(createTagRecord(tag.getKey(), tag.getValue(), tag.getType(), tag.isVisible(), tag.isBoutiques()));
        }
    }

    private void configure() {
        addTitle("Tags", Constants.ICON_INFORMATION);
        setOverflow(Overflow.AUTO);

        vLayoutLeft.addMember(addTitleToList("SelectedTags", selectedTags));
        vLayoutLeft.addMember(entryFormBlock);

        hLayout.addMember(vLayoutLeft);
        hLayout.addMember(spacer);
        hLayout.addMember(addTitleToList("VIP Existings Tags", existingsTags));
        addMember(hLayout);
        setDescription();
        selectedTags.setShowRollOver(true);
    }

    private void setDescription() {
        getMember(0).setPrompt("Please each tags must be an unique string clear for users. For boutiques suggestions you might transform them, for example fsl:true should become fsl as a unique tag.");
    }

    private VLayout addTitleToList(String name, ListGrid list) {
        VLayout vLayout = new VLayout();

        vLayout.addMember(WidgetUtil.getLabel("<b>" + name + "</b>", 15));
        vLayout.addMember(list);
        return vLayout;
    }

    private ListGrid createList(boolean removable) {
        ListGrid list = new ListGrid();

        list.addRemoveRecordClickHandler(new RemoveRecordClickHandler() {
            @Override
            public void onRemoveRecordClick(RemoveRecordClickEvent event) {
                ListGridRecord record = list.getRecord(event.getRowNum());

                existingsTags.addData(record);
                existingsTags.fetchData();
            }
        });
        list.setCanRemoveRecords(removable);

        list.setShowRollOver(false);
        list.setWidth(350);
        list.setHeight(250);
        list.setDragDataAction(DragDataAction.MOVE);
        list.setCanDragRecordsOut(true);
        list.setCanAcceptDroppedRecords(true);
        list.setCanReorderRecords(true);
        list.setFields(
            new ListGridField("key", "Key").setCanEdit(true),
            new ListGridField("value", "Value").setCanEdit(true),
            new ListGridField("visible", "Visible").setType(ListGridFieldType.BOOLEAN).setCanEdit(true),
            new ListGridField("boutiques", "Boutiques").setType(ListGridFieldType.BOOLEAN).setCanEdit(true)
        );
        list.setCanEdit(true);

        return list;
    }

    private void addSelectedTag(Tag tag) {
        selectedTags.addData(createTagRecord(tag.getKey(), tag.getValue(), tag.getType(), tag.isVisible(), tag.isBoutiques()));
    }

    private List<Tag> getTags(ListGrid list, String application, String version) {
        List<Tag> tags = new ArrayList<>();

        for (Record record : list.getRecords()) {
            tags.add(createTagFromRecord(record, application, version));
        }
        return tags;
    }

    private void setFormInput() {
        entryFormBlock = new DynamicForm();
        entryForm = new ComboBoxItem("autoComplete", "Add custom Tag");

        entryForm.setWidth(175);
        entryForm.setTitleOrientation(TitleOrientation.TOP);

        entryForm.addKeyPressHandler(event -> {
            if ("Enter".equals(event.getKeyName())) {
                String inputValue = entryForm.getEnteredValue();
                
                if (inputValue != null && ! inputValue.trim().isEmpty()) {
                    addSelectedTag(new Tag(inputValue, "default", Tag.ValueType.STRING, null, null, true, false));
                }
                entryForm.clearValue();
            }
        });

        entryFormBlock.setFields(entryForm);
    }

    private void loadDatabaseNonBoutiquesTags() {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();

        final AsyncCallback<List<Tag>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load tags:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Tag> result) {
                Record[] data = result.stream()
                    .map((t) -> {
                        return createTagRecord(t.getKey(), t.getValue(), t.getType(), false, t.isBoutiques());
                    })
                    .toArray(Record[]::new);
            
                existingsTags.setData(data);
            }
        };
        service.getNonBoutiquesTags(callback);
    }

    private Record createTagRecord(String key, String value, ValueType type, boolean visible, boolean boutiques) {
        Record record = new Record();

        record.setAttribute("key", key);
        record.setAttribute("value", value);
        record.setAttribute("type", ValueType.STRING);
        record.setAttribute("visible", visible);
        record.setAttribute("boutiques", boutiques);
        return record;
    }

    private Tag createTagFromRecord(Record record, String appName, String appVersion) {
        return new Tag(
            record.getAttribute("key"),
            record.getAttribute("value"),
            Tag.ValueType.valueOf(record.getAttribute("type")),
            appName,
            appVersion,
            record.getAttributeAsBoolean("visible"),
            record.getAttributeAsBoolean("boutiques"));
    }
}
