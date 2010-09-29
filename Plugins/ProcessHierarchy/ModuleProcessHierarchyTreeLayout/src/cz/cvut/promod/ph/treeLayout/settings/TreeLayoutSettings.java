package cz.cvut.promod.ph.treeLayout.settings;

import cz.cvut.promod.gui.settings.SettingPageData;
import cz.cvut.promod.ph.treeLayout.settings.pages.GeneralPage;

import java.util.LinkedList;
import java.util.List;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:47:22, 28.1.2010
 *
 * TreeLayoutSettings represents a setting page fpr the common Settings Dialog.
 */
public class TreeLayoutSettings {

    private final List<SettingPageData> settingPages;

    private static TreeLayoutSettings instance;

    private final TreeLayoutSettingsModel model;

    public TreeLayoutSettings(){
        settingPages = new LinkedList<SettingPageData>();

        model = new TreeLayoutSettingsModel();

        initPages();
    }

    private void initPages() {
        GeneralPage genPage = new GeneralPage();
        SettingPageData setPageData = new SettingPageData("Process Hierarchy Tree Layout", null, genPage);
        settingPages.add(setPageData);
    }

    public List<SettingPageData> getSettingPages() {
        return settingPages;
    }

    public TreeLayoutSettingsModel.VerticalLayout getVerticalLayout(){
        return model.getVerticalLayout();
    }

    public void setVerticalLayout(final TreeLayoutSettingsModel.VerticalLayout verticalLayout){
        model.setVerticalLayout(verticalLayout);
    }

    public static TreeLayoutSettings getInstance() {
        return instance;
    }

    public static void setInstance(final TreeLayoutSettings instance) {
        TreeLayoutSettings.instance = instance;
    }

}