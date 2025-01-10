package fr.insalyon.creatis.vip.applicationimporter.client.view.applicationdisplay;

import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;

import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.applicationimporter.client.view.Constants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

public class TagsLayout extends AbstractFormLayout {

    private final HLayout hLayout;
    private final VLayout vLayoutLeft;
    private final ListGrid selectedTags;
    private final ListGrid suggestedTags;
    private final ListGrid existingsTags;
    private final LayoutSpacer spacer;
    private final DataSource dataSource;

    private DynamicForm entryFormBlock;
    private ComboBoxItem entryForm;
    
    private BoutiquesApplication boutiques;

    public TagsLayout(String width, String height) {
        super(width, height);

        hLayout = new HLayout(10);
        vLayoutLeft = new VLayout(10);
        selectedTags = createList("Selected Tags");

        dataSource = new DataSource().setClientOnly(true);
        dataSource.setFields(new DataSourceTextField("name").setPrimaryKey(true));

        suggestedTags = createBoutiquesList();
        existingsTags = createList("Existing Tags").setDataSource(dataSource);
        spacer = new LayoutSpacer();
        getDynamicFormInput();

        configure();
        loadDatabaseTags();
    }

    public List<String> getSelectedTags() {
        return getTags(selectedTags);
    }

    public void setBoutiques(BoutiquesApplication boutiquesApplication) {
        boutiques = boutiquesApplication;
        loadBoutiquesTags();
    }

    private void configure() {
        addTitle("Tags", Constants.ICON_INFORMATION);
        setOverflow(Overflow.AUTO);

        vLayoutLeft.addMember(selectedTags);
        vLayoutLeft.addMember(entryFormBlock);

        hLayout.addMember(vLayoutLeft);
        hLayout.addMember(spacer);
        hLayout.addMember(suggestedTags);
        hLayout.addMember(existingsTags);
        addMember(hLayout);
    }

    private ListGrid createList(String name) {
        ListGrid list = new ListGrid();

        list.setWidth(175);
        list.setHeight(250);
        list.setDragDataAction(DragDataAction.MOVE);
        list.setCanDragRecordsOut(true);
        list.setCanAcceptDroppedRecords(true);
        list.setCanReorderRecords(true);
        list.setFields(new ListGridField("name", name));

        return list;
    }

    private ListGrid createBoutiquesList() {
        ListGrid list = createList("Boutiques Tags Suggestion");

        list.setWidth(325);
        list.setCanDragRecordsOut(false);
        list.setCanAcceptDroppedRecords(false);
        return list;
    }


    private void addSelectedTag(String tagName) {
        Record record = new Record();

        record.setAttribute("name", tagName);
        selectedTags.addData(record);
    }

    private void addExistingTag(String tagName) {
        Record record = new Record();

        record.setAttribute("name", tagName);
        dataSource.addData(record);

        existingsTags.fetchData();
    }

    private List<String> getTags(ListGrid list) {
        List<String> tags = new ArrayList<>();

        for (Record record : list.getRecordList().toArray()) {
            tags.add(record.getAttribute("name"));
        }
        return tags;
    }

    private void loadDatabaseTags() {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        List<String> tags = getTags(suggestedTags);

        final AsyncCallback<List<Tag>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load tags:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Tag> result) {
                result.forEach((t) -> {
                    if ( ! tags.contains(t.getName())) {
                        addExistingTag(t.getName());
                    }
                });
            }
        };
        service.getTags(callback);
    }

    private void getDynamicFormInput() {
        entryFormBlock = new DynamicForm();

        entryForm = new ComboBoxItem("autoComplete", "Search/Add custom Tag");

        entryForm.setWidth(175);
        entryForm.setTitleOrientation(TitleOrientation.TOP);
        entryForm.setValueField("name");
        entryForm.setOptionDataSource(dataSource);

        entryForm.setAddUnknownValues(true);
        entryForm.setFetchMissingValues(false);

        entryForm.addKeyPressHandler(event -> {
            if ("Enter".equals(event.getKeyName())) {
                String inputValue = entryForm.getEnteredValue();
                Optional<String> matchedValue;
                Record record = new Record();
                
                if (inputValue != null && ! inputValue.trim().isEmpty()) {
                    matchedValue = getTags(existingsTags).stream().filter((t) -> t.equalsIgnoreCase(inputValue)).findFirst();
                    if (matchedValue.isPresent()) {
                        record.setAttribute("name", matchedValue.get());
                        dataSource.removeData(record);
                    }
                    if ( ! getTags(selectedTags).contains(inputValue)) {
                        addSelectedTag(inputValue);
                    }
                }
                entryForm.clearValue();
            }
        });

        entryFormBlock.setFields(entryForm);
    }

    /**
     * Must be run before loadDatabaseTags, because the other function filter
     * depending on the result of this one.
     */
    private void loadBoutiquesTags() {
        Record record;

        for (Map.Entry<String, String> entry : boutiques.getTags().entrySet()) {
            record = new Record();
            record.setAttribute("name", entry.getKey() + ":" + entry.getValue());

            suggestedTags.addData(record);
        }
    }
}
