package fr.insalyon.creatis.vip.application.client.view.reprovip;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.PublicExecution;
import fr.insalyon.creatis.vip.application.client.bean.PublicExecution.PublicExecutionStatus;
import fr.insalyon.creatis.vip.application.client.rpc.ReproVipService;
import fr.insalyon.creatis.vip.application.client.rpc.ReproVipServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

public class PublicExecutionsContextMenu extends Menu {

    private ModalWindow modal;
    private String experienceName;
    private ReproVipServiceAsync reproVipServiceAsync = ReproVipService.Util.getInstance();

    public PublicExecutionsContextMenu(
            ModalWindow modal, String experienceName, PublicExecution.PublicExecutionStatus status) {

        this.modal = modal;
        this.experienceName = experienceName;

        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem createDirectoryItem = new MenuItem("Create directory");
        createDirectoryItem.setIcon(CoreConstants.ICON_SUCCESS);
        createDirectoryItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                createReproVipDirectory(experienceName);
            }
        });

        MenuItem deleteExecutionItem = new MenuItem("Delete ReproVIP directory");
        deleteExecutionItem.setIcon(CoreConstants.ICON_CLEAR);
        deleteExecutionItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                deleteReproVipDirectory(experienceName);
            }
        });

        MenuItem publish = new MenuItem("Edit DOI");
        publish.setIcon(CoreConstants.ICON_EDIT);
        publish.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                publishExperience(experienceName);
            }
        });

        switch (status) {
            case REQUESTED:
                this.setItems(createDirectoryItem);
                break;
            case DIRECTORY_CREATED:
                this.setItems(deleteExecutionItem, publish);
                break;
            case PUBLISHED:
                this.clear();
                break;
        }
    }

    public void createReproVipDirectory(String experienceName) {
        reproVipServiceAsync.createReproVipDirectory(experienceName, new AsyncCallback<>() {
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

    public void deleteReproVipDirectory(String experienceName) {
        reproVipServiceAsync.deleteReproVipDirectory(experienceName, new AsyncCallback<>() {
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

    public void publishExperience(String experienceName) {
        SC.askforValue("Publish Experience", "Please enter the DOI of the experience " + experienceName, new ValueCallback() {
            @Override
            public void execute(String doi) {
                if (doi.length() > 0) {
                    SC.ask("Are you sure that you want to define the DOI of " + experienceName + " as " + doi + ".\nThis action is irreversible!", new BooleanCallback() {
                        @Override
                        public void execute(Boolean value) {
                            if (value) {
                                setExecutionPublished(experienceName, doi);
                            }
                        }
                    });
                } else {
                    SC.warn("DOI can't be empty!");
                }
            }
        });
    }

    private void setExecutionPublished(String experienceName, String doi) {
        reproVipServiceAsync.setExecutionPublished(experienceName, doi, new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                SC.warn("Error while setting doi: " + caught.getMessage());
            }

            @Override
            public void onSuccess(PublicExecutionStatus s) {
                SC.say("Doi successfully defined!");
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
