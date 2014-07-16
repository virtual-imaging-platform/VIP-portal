package fr.insalyon.creatis.vip.core.client.view.user.publication;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.bean.Publication;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.LabelButton;
import fr.insalyon.creatis.vip.core.client.view.common.ToolstripLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.user.PublicationTab;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public class PublicationLayout extends VLayout {

    PublicationGrid publicationGrid;
    private final ModalWindow modal;
    private ListGrid grid;
    private static PublicationLayout instance;
    boolean state = true;

    public static PublicationLayout getInstance() {
        if (instance == null) {
            instance = new PublicationLayout();
        }
        return instance;
    }

    public PublicationLayout() {

        this.setWidth100();
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);

        configureActions();
        configureGrid();
        modal = new ModalWindow(grid);
        loadData();
    }

    private void configureActions() {

        ToolstripLayout toolstrip = new ToolstripLayout();
        LabelButton searchButton = new LabelButton("Search", CoreConstants.ICON_SEARCH);
        searchButton.setWidth(150);
        searchButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                setFilter();
            }
        });

        toolstrip.addMember(WidgetUtil.getSpaceLabel(15));
        toolstrip.addMember(searchButton);

        LabelButton addButton = new LabelButton("Add Publication", CoreConstants.ICON_ADD);
        addButton.setWidth(150);
        addButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                edit(null, null, null, null, null, null, null);
            }
        });
        toolstrip.addMember(addButton);

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



        publicationGrid = new PublicationGrid();
        grid = publicationGrid.grid;

        this.addMember(grid);
    }

    public void loadData() {
        final AsyncCallback<List<Publication>> callback = new AsyncCallback<List<Publication>>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of publications:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Publication> result) {
                modal.hide();
                List<PublicationRecord> dataList = new ArrayList<PublicationRecord>();
                for (Publication pub : result) {
                    dataList.add(new PublicationRecord(pub.getId(), pub.getTitle(), pub.getType(), pub.getTypeName(), pub.getDate(), pub.getAuthors(), pub.getDoi(), pub.getVipAuthor()));
                }
                grid.setData(dataList.toArray(new PublicationRecord[]{}));
                publicationGrid.ds.setTestData(dataList.toArray(new PublicationRecord[]{}));
            }
        };
        modal.show("Loading Publications...", true);
        ConfigurationService.Util.getInstance().getPublications(callback);
    }

    protected static void edit(String id, String title, String type, String typeName, String authors, String date, String doi) {

        PublicationTab pubTab = (PublicationTab) Layout.getInstance().
                getTab(CoreConstants.TAB_PUBLICATION);
        pubTab.setPublication(id, title, type, typeName, authors, date, doi);
    }

    private void remove(Long id) {

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to remove publication:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                Layout.getInstance().setNoticeMessage("The publication was successfully removed!");
                loadData();
            }
        };
        modal.show("Removing publication '" + "'...", true);
        ConfigurationService.Util.getInstance().removePublication(id, callback);
    }

    public void setFilter() {

        if (state == false) {
            grid.setShowFilterEditor(false);
            state = true;
        } else {
            grid.setShowFilterEditor(true);
            state = false;
        }

    }
}
