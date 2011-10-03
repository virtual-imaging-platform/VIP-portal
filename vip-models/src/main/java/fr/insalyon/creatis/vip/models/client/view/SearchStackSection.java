/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.models.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModelLight;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;
import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class SearchStackSection extends SectionStackSection {

    private String tabID;
    private IButton submitButton;
    private DynamicForm form;
    private TextItem queryItem;
    protected ModalWindow modal;
    private SelectItem typesPickList;
    private SelectItem timePickList;

    public SearchStackSection(String tabID) {

        this.tabID = tabID;
        queryItem = new TextItem("query", "Model part");
        queryItem.setWidth(350);
        queryItem.setRequired(true);

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_SEARCH) + " Search");
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

        typesPickList = new SelectItem();
        typesPickList.setTitle("Model layers");
        typesPickList.setMultiple(true);
        typesPickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        typesPickList.setWidth(350);
        String[] values = {"Geometrical", "Anatomical", "Pathological", "Foreign object", "External agent"};
        typesPickList.setValueMap(values);

        timePickList = new SelectItem();
        timePickList.setTitle("Time");
        timePickList.setMultiple(true);
        timePickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        timePickList.setWidth(350);
        String[] values1 = {"Longitudinal follow-up", "Movement"};
        timePickList.setValueMap(values1);

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


                ms.searchModels(queryItem.getEnteredValue(), typesPickList.getValues(), timePickList.getValues(), callback);
            }
        });
        form.setFields(queryItem, typesPickList, timePickList);
    }
}
