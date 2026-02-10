package fr.insalyon.creatis.vip.datamanager.client.view.browser;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.HTMLPane;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;

/**
 *
 * @author camarasu
 */
public class DataUploadHTMLPane extends HTMLPane {

    public DataUploadHTMLPane(String title, String code, String inputId,
            String path, String target, boolean usePool, boolean doUnzip) {

        this.setWidth100();
        this.setHeight100();
        this.setShowEdges(true);

        this.setContents(
                "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>" + title + "</title>"
                + "</head>"
                + "<body>"
                + "<form>\n"
                + "<div>\n"
                + "  <label for=\"folder_upload\">Choose folder for upload</label>\n"
                //Note:  "webkitdirectory  multiple" works with Chrome, Firefox and Edge, but is unsupported in Internet Explorer.
                + "  <input type=\"file\" id=\"" + inputId + "\" name=\"folder_upload\" webkitdirectory  multiple>\n"
                + "</div>\n"
                + "<div>\n"
                + "  <input type=\"button\" onclick=\"zipAndUploadFiles(\'"
                + inputId + "\',\'" + code + "\', \'" + path + "\', \'" + target
                + "\', \'" + usePool + "\', \'" + doUnzip + "\')\" value=\"Upload\"/>\n"
                + "</div>\n"
                + "</form>\n"
                + "\n"
                + "<div id=\"progressNumber\"></div>\n"
                + "<pre id=\"serverResponse\"></pre>\n"
                + "\n"
                + "</body>"
                + "</html>");
    }

}
