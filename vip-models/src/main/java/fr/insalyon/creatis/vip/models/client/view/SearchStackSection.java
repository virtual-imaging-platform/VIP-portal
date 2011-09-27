/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModelLight;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;
import java.util.List;

/**
 *
 * @author glatard
 */
public class SearchStackSection extends SectionStackSection {

    private String tabID;
    private IButton submitButton;
    private DynamicForm form;
    private TextItem queryItem;
    protected ModalWindow modal;

    public SearchStackSection(String tabID) {

        this.tabID = tabID;
        queryItem = new TextItem("query", "Model part");
        queryItem.setWidth(350);
        queryItem.setRequired(true);

        this.setTitle("Search");
        this.setExpanded(false);

        configureForm();

        HLayout hLayout = new HLayout(5);
        hLayout.setMargin(5);
        hLayout.addMember(submitButton);

        VLayout vLayout = new VLayout(5);
        vLayout.addMember(form);
        vLayout.addMember(hLayout);
        this.addItem(vLayout);

    }

    private void configureForm() {
        form = new DynamicForm();
        modal = new ModalWindow(form);
        form.setMargin(5);
        form.setWidth(500);
        form.setNumCols(4);

        submitButton = new IButton("Search");
        submitButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                ModelServiceAsync ms = ModelService.Util.getInstance();
                final AsyncCallback<List<SimulationObjectModelLight>> callback = new AsyncCallback<List<SimulationObjectModelLight>>() {

                    public void onFailure(Throwable caught) {
                        SC.warn("Cannot list models");
                        modal.hide();
                    }

                    public void onSuccess(List<SimulationObjectModelLight> result) {
                        ModelListTab modelTab = (ModelListTab) Layout.getInstance().getTab(tabID);
                        modelTab.setModelList(result);
                        modal.hide();
                    }
                };
                modal.show("Searching Models...", true);
                ms.searchModels(queryItem.getEnteredValue(), callback);
            }
        });
        form.setFields(queryItem);
    }
}
