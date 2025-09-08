package fr.insalyon.creatis.vip.social.client.view.common;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.social.client.SocialConstants;

/**
 *
 * @author Rafael Silva
 */
public class AbstractComposeWindow extends Window {

    protected ModalWindow modal;
    protected VLayout vLayout;
    protected DynamicForm form;
    protected RichTextEditor richTextEditor;

    public AbstractComposeWindow(String title) {

        this.setTitle(Canvas.imgHTML(SocialConstants.ICON_COMPOSE) + " " + title);
        this.setCanDragReposition(true);
        this.setCanDragResize(true);
        this.setWidth(700);
        this.setHeight(450);
        this.centerInPage();
        this.setBackgroundColor("#F2F2F2");
        this.setPadding(5);

        vLayout = new VLayout(5);
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setPadding(5);

        modal = new ModalWindow(vLayout);
        
        configureRichTextEditor();

        this.addItem(vLayout);
    }

    private void configureRichTextEditor() {

        richTextEditor = new RichTextEditor();
        richTextEditor.setHeight100();
        richTextEditor.setOverflow(Overflow.HIDDEN);
        richTextEditor.setCanDragResize(true);
        richTextEditor.setShowEdges(true);
        richTextEditor.setControlGroups("fontControls", "formatControls",
                "styleControls", "editControls", "colorControls", "insertControls");
    }
}
