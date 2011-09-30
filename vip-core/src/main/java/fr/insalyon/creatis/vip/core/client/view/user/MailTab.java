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
package fr.insalyon.creatis.vip.core.client.view.user;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RowMouseDownEvent;
import com.smartgwt.client.widgets.grid.events.RowMouseDownHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.bean.News;
import fr.insalyon.creatis.vip.core.client.rpc.NewsService;
import fr.insalyon.creatis.vip.core.client.rpc.NewsServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.user.mail.NewsViewerWindow;
import fr.insalyon.creatis.vip.core.client.view.system.news.NewsRecord;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class MailTab extends Tab {

    private ModalWindow modal;
    private ListGrid grid;

    public MailTab() {

        this.setTitle(Canvas.imgHTML(CoreConstants.ICON_MAIL) + " "
                + CoreConstants.APP_MAIL);
        this.setID(CoreConstants.TAB_MAIL);
        this.setCanClose(true);

        configureGrid();
        modal = new ModalWindow(grid);
        
        VLayout vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        
        vLayout.addMember(grid);
        
        this.setPane(vLayout);
        loadData();
    }

    private void configureGrid() {

        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowRollOverCanvas(true);
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField titleField = new ListGridField("title", "Title");
        ListGridField dataField = new ListGridField("posted", "Date", 110);
        ListGridField authorField = new ListGridField("author", "Author", 160);

        grid.setFields(titleField, dataField, authorField);
        grid.addRowMouseDownHandler(new RowMouseDownHandler() {

            public void onRowMouseDown(RowMouseDownEvent event) {
                ListGridRecord record = event.getRecord();
                new NewsViewerWindow((NewsRecord) record).show();
            }
        });
    }

    private void loadData() {

        NewsServiceAsync service = NewsService.Util.getInstance();
        final AsyncCallback<List<News>> callback = new AsyncCallback<List<News>>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get list of messages:<br />" + caught.getMessage());
            }

            public void onSuccess(List<News> result) {
                List<NewsRecord> dataList = new ArrayList<NewsRecord>();

                for (News n : result) {
                    dataList.add(new NewsRecord(n.getTitle(), n.getMessage(),
                            n.getPosted(), n.getAuthor()));
                }
                grid.setData(dataList.toArray(new NewsRecord[]{}));
                modal.hide();
            }
        };
        modal.show("Loading messages...", true);
        service.getNews(callback);
    }
}
