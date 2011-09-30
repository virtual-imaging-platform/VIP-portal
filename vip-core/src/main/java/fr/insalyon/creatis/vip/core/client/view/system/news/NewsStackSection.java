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
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.bean.News;
import fr.insalyon.creatis.vip.core.client.rpc.NewsService;
import fr.insalyon.creatis.vip.core.client.rpc.NewsServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class NewsStackSection extends SectionStackSection {

    private ModalWindow modal;
    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;

    public NewsStackSection() {

        this.setTitle("News");
        this.setCanCollapse(true);
        this.setExpanded(true);
        this.setResizeable(true);

        configureGrid();

        VLayout vLayout = new VLayout();
        vLayout.setMaxHeight(400);
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.addMember(grid);
        
        modal = new ModalWindow(grid);

        this.addItem(vLayout);
        loadData();
    }

    private void configureGrid() {
        
        grid = new ListGrid() {

            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
                rollOverRecord = this.getRecord(rowNum);

                if (rollOverCanvas == null) {
                    rollOverCanvas = new HLayout(3);
                    rollOverCanvas.setSnapTo("TR");
                    rollOverCanvas.setWidth(50);
                    rollOverCanvas.setHeight(22);

                    ImgButton loadImg = getImgButton(CoreConstants.ICON_EDIT, "Edit");
                    loadImg.addClickHandler(new ClickHandler() {

                        public void onClick(ClickEvent event) {
                            edit(new News(rollOverRecord.getAttribute("title"),
                                    rollOverRecord.getAttribute("message"),
                                    rollOverRecord.getAttribute("posted"),
                                    rollOverRecord.getAttribute("author")));
                        }
                    });
                    ImgButton deleteImg = getImgButton(CoreConstants.ICON_DELETE, "Delete");
                    deleteImg.addClickHandler(new ClickHandler() {

                        public void onClick(ClickEvent event) {
                            SC.confirm("Do you really want to remove this news?", new BooleanCallback() {

                                public void execute(Boolean value) {
                                    if (value != null && value) {
                                        remove(new News(rollOverRecord.getAttribute("title"),
                                                rollOverRecord.getAttribute("message"),
                                                rollOverRecord.getAttribute("posted"),
                                                rollOverRecord.getAttribute("author")));
                                    }
                                }
                            });
                        }
                    });
                    rollOverCanvas.addMember(loadImg);
                    rollOverCanvas.addMember(deleteImg);
                }
                return rollOverCanvas;
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
        };
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowRollOverCanvas(true);
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setShowRowNumbers(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField titleField = new ListGridField("title", "Title");
        ListGridField dataField = new ListGridField("posted", "Date");
        ListGridField authorField = new ListGridField("author", "Author");

        grid.setFields(titleField, dataField, authorField);
        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {

            public void onCellDoubleClick(CellDoubleClickEvent event) {
                edit(new News(event.getRecord().getAttribute("title"),
                        event.getRecord().getAttribute("message"),
                        event.getRecord().getAttribute("posted"),
                        event.getRecord().getAttribute("author")));
            }
        });
    }

    public void loadData() {
        NewsServiceAsync service = NewsService.Util.getInstance();
        final AsyncCallback<List<News>> callback = new AsyncCallback<List<News>>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get news list:<br />" + caught.getMessage());
            }

            public void onSuccess(List<News> result) {
                modal.hide();
                List<NewsRecord> dataList = new ArrayList<NewsRecord>();

                for (News n : result) {
                    dataList.add(new NewsRecord(n.getTitle(), n.getMessage(),
                            n.getPosted(), n.getAuthor()));
                }
                grid.setData(dataList.toArray(new NewsRecord[]{}));
            }
        };
        modal.show("Loading news...", true);
        service.getNews(callback);
    }

    private void remove(News news) {

        NewsServiceAsync service = NewsService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to delete news:<br />" + caught.getMessage());
            }

            public void onSuccess(Void result) {
                modal.hide();
                SC.say("News successfully deleted.");
                loadData();
            }
        };
        modal.show("Deleting news...", true);
        service.remove(news, callback);
    }

    private void edit(News news) {
        ManageNewsTab newsTab = (ManageNewsTab) Layout.getInstance().getTab(CoreConstants.TAB_MANAGE_NEWS);
        newsTab.setNews(news);
    }
}
