/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.client.view.launch;

import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class InputsLayout extends VLayout {

    private AbstractInputsLayout mainLayout;
    private String tabID;
    private String applicationName;

    public InputsLayout(String tabID, String applicationName, boolean showExamples) {

        this.tabID = tabID;
        this.applicationName = applicationName;
        this.setMembersMargin(10);

        configureButtons(showExamples);
    }

    private void configureButtons(boolean showExamples) {

        HLayout buttonsLayout = new HLayout(5);
        buttonsLayout.setWidth100();

        Label savedInputsLabel = WidgetUtil.getLabel("Saved Inputs", CoreConstants.ICON_SAVED, 15, Cursor.HAND);
        savedInputsLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                if (mainLayout != null) {
                    mainLayout.destroy();
                }
                mainLayout = new SavedInputsLayout(tabID, applicationName);
                addMember(mainLayout);
            }
        });
        buttonsLayout.addMember(savedInputsLabel);

        if (showExamples) {
            Label examplesLabel = WidgetUtil.getLabel("Examples", CoreConstants.ICON_EXAMPLE, 15, Cursor.HAND);
            examplesLabel.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (mainLayout != null) {
                        mainLayout.destroy();
                    }
                    mainLayout = new ExampleInputsLayout(tabID, applicationName);
                    addMember(mainLayout);
                }
            });
            buttonsLayout.addMember(examplesLabel);
        }

        this.addMember(buttonsLayout);
    }

    public void loadData() {

        if (mainLayout != null) {
            mainLayout.loadData();
        }
    }
}
