package fr.insalyon.creatis.vip.application.client.view.system.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.models.Tag;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.LabelButton;
import fr.insalyon.creatis.vip.core.client.view.common.ToolstripLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.core.models.Pair;


public class TagLayout extends VLayout {
    private ModalWindow modal;
    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;

    public TagLayout() {
        this.setWidth100();
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);

        configureToolStrip();
        configureGrid();
        modal = new ModalWindow(grid);

        loadData();
    }

    private void configureToolStrip() {
        ToolstripLayout toolstrip = new ToolstripLayout();
        toolstrip.addMember(WidgetUtil.getSpaceLabel(15));

        LabelButton refreshButton = new LabelButton("Refresh", CoreConstants.ICON_REFRESH);
        refreshButton.setWidth(150);
        refreshButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadData();
            }
        });
        toolstrip.addMember(refreshButton);

        this.addMember(toolstrip);
    }

    private void configureGrid() {

        grid = new ListGrid() {
            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
                rollOverRecord = this.getRecord(rowNum);

                if (rollOverCanvas == null) {
                    rollOverCanvas = new HLayout(3);
                    rollOverCanvas.setSnapTo("TR");
                    rollOverCanvas.setWidth(50);
                    rollOverCanvas.setHeight(22);

                    ImgButton loadImg = WidgetUtil.getImgButton(CoreConstants.ICON_EDIT, "Edit");
                    loadImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            edit(rollOverRecord);
                        }
                    });
                    rollOverCanvas.addMember(loadImg);
                }
                return rollOverCanvas;
            }
        };
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowRollOverCanvas(true);
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setShowRowNumbers(true);
        grid.setEmptyMessage("<br>No data available.");
        grid.setFields(
                new ListGridField("key", "Key"),
                new ListGridField("value", "Value"),
                new ListGridField("type", "Type"),
                new ListGridField("appVersions", "AppVersions"),
                new ListGridField("visible", "Visible").setType(ListGridFieldType.BOOLEAN),
                new ListGridField("boutiques", "Boutiques").setType(ListGridFieldType.BOOLEAN));
        grid.setSortField("name");
        grid.setSortDirection(SortDirection.ASCENDING);
        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
            @Override
            public void onCellDoubleClick(CellDoubleClickEvent event) {
                edit(event.getRecord());
            }
        });
        this.addMember(grid);
    }

    public void loadData() {
        final AsyncCallback<List<Tag>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of tags:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Tag> result) {
                modal.hide();
                // this is to organize tags by unique key value mapped on all AppVersions associated
                Map<Pair<String, String>, Pair<Tag, Set<String>>> tagsAppsVersions = new HashMap<>();
                List<TagRecord> dataList = new ArrayList<>();

                for (Tag tag : result) {
                    Pair<String, String> keyValue = new Pair<>(tag.getKey(), tag.getValue());
                    String appVersion = tag.getApplication() + " (" + tag.getVersion() + ")";

                    if ( ! tagsAppsVersions.containsKey(keyValue)) {
                        tagsAppsVersions.put(keyValue, new Pair<>(tag, new HashSet<>()));
                    }
                    tagsAppsVersions.get(keyValue).getSecond().add(appVersion);
                }
                tagsAppsVersions.forEach((k, v) -> {
                    dataList.add(new TagRecord(v.getFirst(), v.getSecond()));
                });
                grid.setData(dataList.toArray(new TagRecord[]{}));
            }
        };
        modal.show("Loading tags...", true);
        ApplicationService.Util.getInstance().getTags(callback);
    }

    private void edit(ListGridRecord record) {
        ManageTagsTab tagsTab = (ManageTagsTab) Layout.getInstance().
            getTab(ApplicationConstants.TAB_MANAGE_TAG);
        Pair<Tag, String[]> data = tagFromRecord(record);

        tagsTab.setTag(data.getFirst(), data.getSecond());
    }

    private Pair<Tag, String[]> tagFromRecord(ListGridRecord record) {
        return new Pair<>(new Tag(
                record.getAttributeAsString("key"),
                record.getAttributeAsString("value"),
                Tag.ValueType.valueOf(record.getAttributeAsString("type").toUpperCase()),
                null,
                null,
                record.getAttributeAsBoolean("visible"),
                record.getAttributeAsBoolean("boutiques")),
                record.getAttributeAsStringArray("appVersions"));
    }
}
