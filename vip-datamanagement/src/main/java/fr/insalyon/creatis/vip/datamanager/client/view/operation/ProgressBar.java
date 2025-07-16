package fr.insalyon.creatis.vip.datamanager.client.view.operation;

import com.smartgwt.client.widgets.layout.HStack;

/**
 *
 * @author Rafael Silva
 */
public class ProgressBar extends HStack {

    private HStack progressHStack;

    public ProgressBar(int height, String color, int percent) {

        this.setWidth("95%");
        this.setHeight(height);
        this.setBorder("1px solid #BFBFBF");

        progressHStack = new HStack();
        progressHStack.setBackgroundColor(color);
        progressHStack.setWidth(percent + "%");
        progressHStack.setPadding(1);
        this.addMember(progressHStack);
    }
}