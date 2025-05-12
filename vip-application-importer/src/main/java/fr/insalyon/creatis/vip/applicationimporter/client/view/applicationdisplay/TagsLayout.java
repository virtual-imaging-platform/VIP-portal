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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;

import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.applicationimporter.client.view.Constants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

public class TagsLayout extends AbstractFormLayout {

    private final HLayout hLayout;
    private final VLayout vLayoutLeft;
    private final ListGrid selectedTags;
    private final ListGrid existingsTags;
    private final LayoutSpacer spacer;
    private final DataSource dataSource;

    private DynamicForm entryFormBlock;
    private ComboBoxItem entryForm;
    private Set<String> manualTags;
    
    private Map<String, String> boutiquesTags;

    public TagsLayout(String width, String height) {
        super(width, height);

        hLayout = new HLayout(10);
        vLayoutLeft = new VLayout(10);
        selectedTags = createList("Selected Tags", true);

        dataSource = new DataSource().setClientOnly(true);
        dataSource.setFields(new DataSourceTextField("name").setPrimaryKey(true));

        existingsTags = createList("VIP Existings Tags", false).setDataSource(dataSource);
        spacer = new LayoutSpacer();
        getDynamicFormInput();

        configure();
        loadDatabaseNonBoutiquesTags();
        manualTags = new HashSet<>();
    }

    public List<Tag> getSelectedTags(String application, String version) {
        return getTags(selectedTags, application, version);
    }

    public void setBoutiquesTags(Map<String,String> tags) {
        boutiquesTags = tags;
        loadBoutiquesTags();
    }

    private void configure() {
        addTitle("Tags", Constants.ICON_INFORMATION);
        setOverflow(Overflow.AUTO);

        vLayoutLeft.addMember(selectedTags);
        vLayoutLeft.addMember(entryFormBlock);

        hLayout.addMember(vLayoutLeft);
        hLayout.addMember(spacer);
        hLayout.addMember(existingsTags);
        addMember(hLayout);
        setDescription();
        selectedTags.setShowRollOver(true);
    }

    private void setDescription() {
        getMember(0).setPrompt("Please each tags must be an unique string clear for users. For boutiques suggestions you might transform them, for example fsl:true should become fsl as a unique tag.");
    }

    private ListGrid createList(String name, boolean removable) {
        ListGrid list = new ListGrid();

        list.addRemoveRecordClickHandler(new RemoveRecordClickHandler() {
            @Override
            public void onRemoveRecordClick(RemoveRecordClickEvent event) {
                ListGridRecord record = list.getRecord(event.getRowNum());

                final String name = record.getAttribute("name");
                if (manualTags.contains(name)) {
                    addExistingTag(new Tag(name, 
                        record.getAttributeAsBoolean("visible"), 
                        record.getAttributeAsBoolean("boutiques")));
                }
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
            new ListGridField("name", name).setCanEdit(false),
            new ListGridField("visible", "Visible").setType(ListGridFieldType.BOOLEAN).setCanEdit(true),
            new ListGridField("boutiques", "Boutiques").setType(ListGridFieldType.BOOLEAN).setCanEdit(true)
        );
        list.setCanEdit(true);

        list.setCellFormatter((value, record, rowNum, colNum) -> {
            if (manualTags.contains(value)) {
                return "<span style='font-weight:bold;'>" + (String) value + "</span>";
            }
            return (String) value;
        });
        return list;
    }

    private void addSelectedTag(Tag tag) {
        Record record = new Record();

        record.setAttribute("name", tag.getName());
        record.setAttribute("visible", tag.isVisible());
        record.setAttribute("boutiques", tag.isBoutiques());
        selectedTags.addData(record);
    }

    private Record addExistingTag(Tag tag) {
        Record record = new Record();

        record.setAttribute("name", tag.getName());
        record.setAttribute("visible", tag.isVisible());
        record.setAttribute("boutiques", tag.isBoutiques());

        existingsTags.fetchData();

        return record;
    }

    private List<Tag> getTags(ListGrid list, String application, String version) {
        List<Tag> tags = new ArrayList<>();

        for (Record record : list.getRecordList().toArray()) {
            tags.add(new Tag(
                record.getAttribute("name"),
                application,
                version,
                record.getAttributeAsBoolean("visible"),
                record.getAttributeAsBoolean("boutiques")
            ));
        }
        return tags;
    }

    private void getDynamicFormInput() {
        entryFormBlock = new DynamicForm();

        entryForm = new ComboBoxItem("autoComplete", "Add/Search custom Tag");

        entryForm.setWidth(175);
        entryForm.setTitleOrientation(TitleOrientation.TOP);
        entryForm.setValueField("name");
        entryForm.setOptionDataSource(dataSource);

        entryForm.setAddUnknownValues(true);
        entryForm.setFetchMissingValues(false);

        entryForm.addKeyPressHandler(event -> {
            if ("Enter".equals(event.getKeyName())) {
                String inputValue = entryForm.getEnteredValue();
                Optional<Tag> matchedValue;
                Record record = new Record();
                
                if (inputValue != null && ! inputValue.trim().isEmpty()) {
                    matchedValue = getTags(existingsTags, "", "")
                            .stream().filter((t) -> t.getName().equalsIgnoreCase(inputValue)).findFirst();
                    if (matchedValue.isPresent()) {
                        record.setAttribute("name", matchedValue.get().getName());
                        dataSource.removeData(record);
                        existingsTags.fetchData();
                    } else {
                        manualTags.add(inputValue);
                    }
                    if ( ! getTags(selectedTags, "", "").stream().anyMatch((t) -> t.getName().equals(inputValue))) {
                        addSelectedTag(new Tag(inputValue, true, false));
                    }
                }
                entryForm.clearValue();
            }
        });

        entryFormBlock.setFields(entryForm);
    }

    private void loadDatabaseNonBoutiquesTags() {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        List<Tag> tags = getTags(selectedTags, "", "");

        final AsyncCallback<List<Tag>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load tags:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Tag> result) {
                Record[] data = result.stream()
                    .filter((t) -> ! tags.stream().anyMatch((e) -> e.getName().equals(t.getName())))
                    .map((t) -> {
                        manualTags.add(t.getName());
                        return addExistingTag(t);
                    })
                    .toArray(Record[]::new);

                dataSource.setCacheData(data);
            }
        };
        service.getNonBoutiquesTags(callback);
    }

    /**
     * Must be run before loadDatabaseTags, because the other function filter
     * depending on the result of this one.
     */
    private void loadBoutiquesTags() {
        Record record;

        for (Map.Entry<String, String> entry : boutiquesTags.entrySet()) {
            record = new Record();
            record.setAttribute("name", entry.getKey() + ":" + entry.getValue());
            record.setAttribute("visible", true);
            record.setAttribute("boutiques", true);

            selectedTags.addData(record);
        }
    }
}
