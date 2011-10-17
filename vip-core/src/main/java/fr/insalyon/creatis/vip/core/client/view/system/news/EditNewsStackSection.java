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
package fr.insalyon.creatis.vip.core.client.view.system.news;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.bean.News;
import fr.insalyon.creatis.vip.core.client.rpc.NewsService;
import fr.insalyon.creatis.vip.core.client.rpc.NewsServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Rafael Silva
 */
public class EditNewsStackSection extends SectionStackSection {

    private ModalWindow modal;
    private boolean newNews = true;
    private VLayout vLayout;
    private DynamicForm form;
    private TextItem titleItem;
    private RichTextEditor richTextEditor;

    public EditNewsStackSection() {

        this.setTitle("Add News");
        this.setCanCollapse(true);
        this.setExpanded(true);
        this.setResizeable(true);

        vLayout = new VLayout(15);
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setMargin(5);
        configureForm();

        modal = new ModalWindow(vLayout);

        this.addItem(vLayout);
    }

    private void configureForm() {

        form = new DynamicForm();
        form.setWidth(500);

        titleItem = new TextItem("name", "Title");
        titleItem.setWidth(350);
        titleItem.setRequired(true);

        form.setFields(titleItem);
        vLayout.addMember(form);

        richTextEditor = new RichTextEditor();
        richTextEditor.setHeight(250);
        richTextEditor.setOverflow(Overflow.HIDDEN);
        richTextEditor.setCanDragResize(true);
        richTextEditor.setShowEdges(true);
        richTextEditor.setControlGroups("fontControls", "formatControls",
                "styleControls", "editControls", "colorControls", "insertControls");
        vLayout.addMember(richTextEditor);

        IButton saveButton = new IButton("Save");
        saveButton.setWidth(50);
        saveButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (form.validate()) {
                    String title = titleItem.getValueAsString().trim();
                    String message = richTextEditor.getValue();

                    save(new News(title, message));
                }
            }
        });
        vLayout.addMember(saveButton);
    }

    private void save(News news) {

        NewsServiceAsync service = NewsService.Util.getInstance();

        if (newNews) {
            final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                public void onFailure(Throwable caught) {
                    modal.hide();
                    SC.warn("Unable to add news:<br />" + caught.getMessage());
                }

                public void onSuccess(Void result) {
                    modal.hide();
                    ManageNewsTab newsTab = (ManageNewsTab) Layout.getInstance().
                            getTab(CoreConstants.TAB_MANAGE_NEWS);
                    newsTab.loadNews();
                    setNews(null);
                }
            };
            modal.show("Adding news...", true);
            service.add(news, callback);

        } else {
            final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                public void onFailure(Throwable caught) {
                    modal.hide();
                    SC.warn("Unable to update news:<br />" + caught.getMessage());
                }

                public void onSuccess(Void result) {
                    modal.hide();
                    ManageNewsTab newsTab = (ManageNewsTab) Layout.getInstance().
                            getTab(CoreConstants.TAB_MANAGE_NEWS);
                    newsTab.loadNews();
                    setNews(null);
                }
            };
            modal.show("Updating news...", true);
            service.update(news, callback);
        }
    }

    public void setNews(News news) {
        if (news != null) {
            this.setTitle("Editing News: " + news.getTitle());
            this.titleItem.setValue(news.getTitle());
            this.titleItem.setDisabled(true);
            this.richTextEditor.setValue(news.getMessage());
            this.newNews = false;
        } else {
            this.setTitle("Add News");
            this.titleItem.setValue("");
            this.titleItem.setDisabled(false);
            this.richTextEditor.setValue("");
            this.newNews = true;
        }
    }
}
