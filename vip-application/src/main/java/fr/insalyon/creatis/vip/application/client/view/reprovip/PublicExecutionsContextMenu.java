package fr.insalyon.creatis.vip.application.client.view.reprovip;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.ReproVipService;
import fr.insalyon.creatis.vip.application.client.rpc.ReproVipServiceAsync;
import fr.insalyon.creatis.vip.core.client.bean.PublicExecution;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

public class PublicExecutionsContextMenu extends Menu {

    private ModalWindow modal;
    private String executionID;
    private ReproVipServiceAsync reproVipServiceAsync = ReproVipService.Util.getInstance();

    public PublicExecutionsContextMenu(
            ModalWindow modal, String executionID, PublicExecution.PublicExecutionStatus status) {

        this.modal = modal;
        this.executionID = executionID;

        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem OptionPublicExecutionItem = new MenuItem("Create directory");
        OptionPublicExecutionItem.setIcon(CoreConstants.ICON_SUCCESS);
        OptionPublicExecutionItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                createReproVipDirectory(executionID);
            }
        });

        MenuItem DeleteExecutionItem = new MenuItem("Delete ReproVIP directory");
        DeleteExecutionItem.setIcon(CoreConstants.ICON_CLEAR);
        DeleteExecutionItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                deleteReproVipDirectory(executionID);
            }
        });

        switch (status) {
            case REQUESTED:
                this.setItems(OptionPublicExecutionItem);
                break;
            case DIRECTORY_CREATED:
                this.setItems(DeleteExecutionItem);
                break;
            case PUBLISHED:
                this.setItems();
                break;
        }
    }

    public void createReproVipDirectory(String executionID) {
        reproVipServiceAsync.createReproVipDirectory(executionID, new AsyncCallback<PublicExecution.PublicExecutionStatus>() {
            public void onFailure(Throwable caught) {
                SC.warn("Error creating ReproVip directory: " + caught.getMessage());
            }

            @Override
            public void onSuccess(PublicExecution.PublicExecutionStatus s) {
                SC.say("ReproVip directory successfully created");
                refreshPublicExecutions();
            }
        });
    }

    public void deleteReproVipDirectory(String executionID) {
        reproVipServiceAsync.deleteReproVipDirectory(executionID, new AsyncCallback<PublicExecution.PublicExecutionStatus>() {
            @Override
            public void onFailure(Throwable caught) {
                SC.warn("Error deleting ReproVip directory: " + caught.getMessage());
            }

            @Override
            public void onSuccess(PublicExecution.PublicExecutionStatus s) {
                SC.say("ReproVip directory successfully deleted");
                refreshPublicExecutions();
            }
        });
    }

    private void refreshPublicExecutions() {
        ReproVipTab reproVipTab = (ReproVipTab) Layout.getInstance().
                getTab(ApplicationConstants.TAB_REPROVIP);
        reproVipTab.loadPublicExecutions();
    }
}
