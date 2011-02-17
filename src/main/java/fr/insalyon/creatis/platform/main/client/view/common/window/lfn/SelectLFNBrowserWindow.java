/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.platform.main.client.view.common.window.lfn;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;

/**
 *
 * @author Rafael Silva
 */
public class SelectLFNBrowserWindow extends AbstractLFNBrowserWindow {

    private TextField selectedLFN;

    /**
     * Creates a LFN browser window that allows file selection in order to
     * fill a TextField with its address.
     *
     * @param rootPath Root path
     * @param refID TextField identification
     * @param fieldSet FieldSet that contains the TextField
     */
    public SelectLFNBrowserWindow(String rootPath, final String refID, final FieldSet fieldSet) {

        super("Grid Browser", rootPath);

        ToolbarTextItem textItem = new ToolbarTextItem("Selected Grid File: ");
        topToolbar.addItem(textItem);
        selectedLFN = new TextField();
        selectedLFN.setWidth(410);
        selectedLFN.setReadOnly(true);
        topToolbar.addField(selectedLFN);

        ToolbarButton okButton = new ToolbarButton("Ok", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                if (selectedLFN.getValueAsString().length() > 0) {
                    TextField tf = (TextField) fieldSet.findByID(refID);
                    tf.setValue(selectedLFN.getValueAsString());
                    close();
                } else {
                    MessageBox.alert("Error", "No Grid File Selected.");
                }
            }
        });
        topToolbar.addButton(okButton);
        topToolbar.addSeparator();
        ToolbarButton cancelButton = new ToolbarButton("Cancel", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                close();
            }
        });
        topToolbar.addButton(cancelButton);

        grid.addGridRowListener(new GridRowListenerAdapter() {

            @Override
            public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
                Record record = grid.getStore().getRecordAt(rowIndex);
                String clickedFileName = record.getAsString("file-name");
                String clickedDir = record.getAsString("file-dir");
                selectedLFN.setValue("lfn://" + HOST + clickedDir + "/" + clickedFileName);
            }
        });
        
        display();
    }
}
