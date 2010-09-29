package cz.cvut.promod.ph.treeLayout.settings;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:51:17, 28.1.2010
 *
 * The model for the TreeLayoutSettings.
 */
public class TreeLayoutSettingsModel {

    public static enum VerticalLayout{
        TOP,
        CENTER,
        BOTTOM
    }

    private VerticalLayout verticalLayout = VerticalLayout.CENTER; //initial value

    
    public VerticalLayout getVerticalLayout() {
        return verticalLayout;
    }

    public void setVerticalLayout(VerticalLayout verticalLayout) {
        this.verticalLayout = verticalLayout;
    }
}
