package cz.cvut.promod.gui.settings;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * This class is used as a storage of Setting Pages. Each Page has its name (can not be set to null), icon (can be set
 * to null) and SettingPagePanel (can be set to null).
 *
 * User: lucky
 * Date: 20.8.2010
 * Time: 18:26:05
 */
public class SettingPageData {
    private List<SettingPageData> children = new LinkedList<SettingPageData>();
    private String name = null;
    private Icon icon = null;
    private SettingPagePanel panel = null;

    /**
     * Creates new SettingPageData instance
     * @param name Name of the SettingPage. This name will be shown in the Setting Dialog in the list of single pages
     * @param icon Icon of the Setting Page.
     * @param panel Setting Panel. This panel will be shown when user select this SettingPage in the SettingDialog
     */
    public SettingPageData(String name, Icon icon, SettingPagePanel panel) {
        this.name = name;
        this.icon = icon;
        this.panel = panel;
    }

    /**
     * Creates new SettingPageData instance
     * @param name Name of the SettingPage. This name will be shown in the Setting Dialog in the list of single pages
     * @param panel Setting Panel. This panel will be shown when user select this SettingPage in the SettingDialog
     */
    public SettingPageData(String name, SettingPagePanel panel) {
        this.name = name;
        this.panel = panel;
    }

    /**
     * Returns the name of this setting page
     * @return the name of this setting page
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the icon of this setting page
     * @return the icon of this setting page
     */
    public Icon getIcon() {
        return this.icon;
    }

    /**
     * Returns List of children pages.
     * @return List of children pages. This children pages are supposed to be printed under this page (in the Setting
     * Dialog's list)
     */
    public List<SettingPageData> getChildren() {
        return new ArrayList<SettingPageData>(this.children);
    }

    /**
     * Append a child
     * @param pageInfo Insert new Children
     */
    public void addChildren(SettingPageData pageInfo) {
        if (!this.children.contains(pageInfo)) {
            this.children.add(pageInfo);
        }
    }

    /**
     * Returns SettingPagePanel of this SettingPage
     * @return SettingPagePanel of this SettingPage
     */
    public SettingPagePanel getSettingPage() {
        return this.panel;
    }
}
