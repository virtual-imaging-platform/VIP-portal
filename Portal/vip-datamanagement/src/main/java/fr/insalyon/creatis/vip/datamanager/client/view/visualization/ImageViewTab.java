/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.datamanager.client.view.image;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.LabelButton;
import fr.insalyon.creatis.vip.core.client.view.common.ToolstripLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.bean.Image;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerSection;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;

/**
 *
 * @author glatard
 */
public class ImageViewTab extends Tab {

    private Canvas imageCanvas;
    private Image image;
    private SpinnerItem spinner;
    private DynamicForm form;
    private ModalWindow modal;

    public ImageViewTab(final String imageLFN) {
        
        this.setTitle(imageLFN.substring(imageLFN.lastIndexOf('/') + 1));
        this.setCanClose(true);
        
        imageCanvas = new Canvas();      
        VLayout vLayout = new VLayout();
        modal = new ModalWindow(vLayout);
        form = new DynamicForm();
   
        Canvas canvas = new Canvas();
        canvas.addChild(imageCanvas);

        LabelButton download = new LabelButton("Download Image", DataManagerConstants.ICON_DOWNLOAD);
        download.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                downloadFile(imageLFN);
            }
        });
        
        ToolstripLayout toolstrip = new ToolstripLayout();
        toolstrip.addMember(download);
        toolstrip.addMember(form);
      
        spinner = new SpinnerItem();
        spinner.setName("Slice");
        spinner.setDefaultValue(0);
        spinner.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                updateImageDisplay();
            }
        });
        form.setWidth100();
        form.setHeight(10);
        form.setItems(spinner);
        form.disable();
        
        canvas.setHeight100();

       
        vLayout.addMember(WidgetUtil.getLabel("<u>Warning:</u> this is an experimental viewer to be used only for preview. It creates png slices from 3D images using <a href='http://www.itksnap.org/pmwiki/pmwiki.php?n=Convert3D.Documentation'>ITKSnap's convert3D</a>.", 10));
        vLayout.addMember(toolstrip);
        vLayout.addMember(canvas);
        
        

        loadImage(imageLFN);

        this.setPane(vLayout);
    }

    private void loadImage(String imageLFN) {
        DataManagerServiceAsync dmsa = DataManagerService.Util.getInstance();
        AsyncCallback<Image> ac = new AsyncCallback<Image>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Cannot load image: " + caught.getMessage());
            }

            @Override
            public void onSuccess(Image result) {
                modal.hide();
                image = result;

                String url = "<img src='" + getURL(result.getZdim()/2) + "' border='0'>";
                spinner.setValue(result.getZdim()/2);
               //  SC.say(getURL(0));
                imageCanvas.setContents(url);
                form.enable();
            }
        };
        modal.show("Loading image...", true);
        dmsa.getImageSlicesURL(imageLFN, ac);
    }

    private void updateImageDisplay() {
        int n = Integer.parseInt(spinner.getValueAsString());
        if (n < image.getZdim() && n >= 0) {
            String url = "<img src='" + getURL(n) + "' border='0'>";
            // SC.say(getURL(n));   
            imageCanvas.setContents(url);
        } else {
            if (n >= image.getZdim()) {
                spinner.setValue(image.getZdim() - 1);
            }
            if (n < 0) {
                spinner.setValue(0);
            }
        }
    }

    private String getURL(int sliceNumber) {
        return GWT.getModuleBaseURL() + ".." + image.getURLSlices() + "/slice" + sliceNumber + ".png";

    }

    public static boolean isSupported(String fileName) {
        return fileName.endsWith(".nii") || fileName.endsWith(".nii.gz");
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

            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to download file " + lfn + ":<br />" + caught.getMessage());
            }

            public void onSuccess(String result) {
                Layout.getInstance().setNoticeMessage("File " + lfn.substring(lfn.lastIndexOf("/") + 1) + " added to transfer queue.");
                OperationLayout.getInstance().addOperation(result);
                ((DataManagerSection) Layout.getInstance().getMainSection(DataManagerConstants.SECTION_FILE_TRANSFER)).expand();
            }
        };
        DataManagerService.Util.getInstance().downloadFile(lfn, callback);
    }
}
