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
package fr.insalyon.creatis.vip.datamanager.client.view.visualization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.common.LabelButton;
import fr.insalyon.creatis.vip.core.client.view.common.ToolstripLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.bean.Image;
import fr.insalyon.creatis.vip.datamanager.client.bean.VisualizationItem;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerSection;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;

/**
 *
 * @author glatard
 */
public class ImageViewTab extends AbstractViewTab {

    private final Canvas imageCanvas;
    private Image image;
    private final SpinnerItem spinner;
    private final CheckboxItem checkX;
    private final CheckboxItem checkY;
    private final CheckboxItem checkZ;
    private final DynamicForm form;
    private final DynamicForm formx;
    private VisualizationItem visualizationItem;
    private  String direction;

    public ImageViewTab(final String imageLFN) {
        super(imageLFN);

        this.setID(tabId(imageLFN));
        imageCanvas = new Canvas();
        VLayout vLayout = new VLayout();
        form = new DynamicForm();
        formx = new DynamicForm();
        Canvas canvas = new Canvas();
        canvas.addChild(imageCanvas);
        direction ="z";
        LabelButton download = new LabelButton("Download Image", DataManagerConstants.ICON_DOWNLOAD);
        download.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                downloadFile(imageLFN);
            }
        });
        ToolstripLayout toolstrip = new ToolstripLayout();
        toolstrip.addMember(download);
     toolstrip.addMember(form);
        toolstrip.addMember(formx);

        spinner = new SpinnerItem();
        spinner.setName("Slice");
        spinner.setDefaultValue(0);
        spinner.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                updateImageDisplay();
            }
        });
         checkX = new CheckboxItem();
        checkX.setName("X");
        checkX.setValue(false);
        checkX.addChangedHandler(new ChangedHandler(){
            @Override
            public void onChanged(    ChangedEvent event){
                checkY.setValue(false);
                checkZ.setValue(false);
                updateDirectionDisplay("x");
            }});
        checkY = new CheckboxItem();
        checkY.setName("Y");
        checkY.setValue(false);
        checkY.addChangedHandler(new ChangedHandler(){
            @Override
            public void onChanged(    ChangedEvent event){
                checkX.setValue(false);
                checkZ.setValue(false);
                updateDirectionDisplay("y");
            }});

        checkZ = new CheckboxItem();
        checkZ.setName("Z");
        checkZ.setValue(true);
        checkZ.addChangedHandler(new ChangedHandler(){
            @Override
            public void onChanged(    ChangedEvent event){
                checkX.setValue(false);
                checkY.setValue(false);
                updateDirectionDisplay("z");
            }});

      //  form.setWidth100();
        form.setHeight(10);
        form.setItems(spinner);
        form.disable();

        //formx.setWidth100();
        formx.setHeight(20);
        formx.setItems(checkX,checkY,checkZ);
        formx.disable();

        canvas.setHeight100();

        vLayout.setWidth100();
        Label warning = WidgetUtil.getLabel("<u>Warning:</u> this is an experimental viewer to be used only for preview. It creates png slices from 3D images using <a href='http://www.itksnap.org/pmwiki/pmwiki.php?n=Convert3D.Documentation'>ITKSnap's convert3D</a>.", 10);
        warning.setWidth100();
        vLayout.addMember(warning);
        vLayout.addMember(toolstrip);
        vLayout.addMember(canvas);

        this.getPane().addChild(vLayout);
    }

    private void updateImageDisplay() {
        int n = Integer.parseInt(spinner.getValueAsString());
        if (n < image.getZdim() && n >= 0) {
            showSlice(visualizationItem.getLocalPath(), n);
        } else {
            if (n >= image.getZdim()) {
                spinner.setValue(image.getZdim() - 1);
            }
            if (n < 0) {
                spinner.setValue(0);
            }
        }
    }

    private void updateDirectionDisplay(String dir)
    {
        direction = dir;
        final  String localPath = visualizationItem.getLocalPath();
        DataManagerServiceAsync dmsa = DataManagerService.Util.getInstance();
            modal.show("Slicing image...", true);
            dmsa.getImageSlicesURL(localPath, direction, new AsyncCallback<Image>() {

                @Override
                public void onFailure(Throwable caught) {
                    modal.hide();
                    Layout.getInstance().setWarningMessage("Unable to slice image " + localPath + ":<br />" + caught.getMessage());
                }

                @Override
                public void onSuccess(Image result) {
                    modal.hide();
                    image = result;
                    spinner.setValue(image.getZdim()/2 );
                    showSliceURL(getURLofSlice(image, image.getZdim()/2 ));
                }
            });



    }

    private void showSliceURL(String url) {
        String tag = "<img src='" + url + "' border='0'>";
        imageCanvas.setContents(tag);
        form.enable();
    }

    private void showSlice(final String localPath, final int sliceNumber) {
        if (image == null) {
            DataManagerServiceAsync dmsa = DataManagerService.Util.getInstance();
            modal.show("Slicing image...", true);
            dmsa.getImageSlicesURL(localPath, direction , new AsyncCallback<Image>() {

                @Override
                public void onFailure(Throwable caught) {
                    modal.hide();
                    Layout.getInstance().setWarningMessage("Unable to slice image " + localPath + ":<br />" + caught.getMessage());
                }

                @Override
                public void onSuccess(Image result) {
                    modal.hide();
                    image = result;
                    formx.enable();
                     spinner.setValue(image.getZdim()/2 );
                    showSliceURL(getURLofSlice(image, image.getZdim()/2 ));
                }
            });
        } else {
            showSliceURL(getURLofSlice(image, sliceNumber));
        }
        spinner.setValue(sliceNumber);

    }

    private ImgButton getImgButton(String imgSrc, String prompt) {
        ImgButton button = new ImgButton();
        button.setShowDown(false);
        button.setShowRollOver(false);
        button.setLayoutAlign(Alignment.CENTER);
        button.setSrc(imgSrc);
        button.setPrompt(prompt);
        button.setHeight(16);
        button.setWidth(16);
        return button;
    }

    private void downloadFile(final String lfn) {
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to download file " + lfn + ":<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                Layout.getInstance().setNoticeMessage("File " + lfn.substring(lfn.lastIndexOf("/") + 1) + " added to transfer queue.");
                OperationLayout.getInstance().addOperation(result);
                ((DataManagerSection) Layout.getInstance().getMainSection(DataManagerConstants.SECTION_FILE_TRANSFER)).expand();
            }
        };
        DataManagerService.Util.getInstance().downloadFile(lfn, callback);
    }

    @Override
    public void displayFile(VisualizationItem item) {
        visualizationItem = item;
        showSlice(item.getLocalPath(), 0);
    }

    public static boolean isFileSupported(String fileName) {
        return fileName.endsWith(".nii") || fileName.endsWith(".nii.gz");
    }

    public static String fileTypeName() {
        return "image";
    }

    public static String tabId(String fileName) {
        return
            "image-view-" +
            fileName.replaceAll(" ", "-").toLowerCase() +
            "-tab";
    }

    private String getURLofSlice(Image image, int sliceNumber) {
        return GWT.getModuleBaseURL() + "../" + image.getRelativeURL() + "/slice" + sliceNumber + ".png";
    }
}
