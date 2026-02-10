package fr.insalyon.creatis.vip.gatelab.client.view.launch;

import com.smartgwt.client.widgets.HTMLPane;

/**
 *
 * @author camarasu
 */
public class LoadMacHTMLPane extends HTMLPane {

    public LoadMacHTMLPane(String title, String code, String parentFolderId,
            String macId, String path, String target, boolean usePool, boolean doUnzip) {

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
                + "  <label for=\"folder_upload\">Choose parent folder (contains mac and data)</label>\n"
                + "  <input type=\"file\" id=\"" + parentFolderId
                //Note:  "webkitdirectory  multiple" works with Chrome, Firefox and Edge, but is unsupported in Internet Explorer.
                + "\" name=\"parent_folder\" webkitdirectory  multiple>\n"
                + "</div>\n"
                + "<div>\n"
                + "  <label for=\"mac_file\">Choose main macro file (inside mac folder) &nbsp;&nbsp;&nbsp</label>\n"
                + "  <input type=\"file\" id=\"" + macId + "\" name=\"mac_file\">\n"
                + "</div>\n"
                + "<div>\n"
                + "  <input type=\"button\" onclick=\"parseAndUploadMac(\'"
                + parentFolderId + "\',\'" + macId + "\',\'" + code + "\', \'"
                + path + "\', \'" + target + "\', \'" + usePool + "\', \'" + doUnzip
                + "\')\" value=\"Parse macro file\"/>\n"
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
