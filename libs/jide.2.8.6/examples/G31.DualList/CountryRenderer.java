/*
 * CountryRenderer.java
 *
 * Created on 2007-10-21, 13:08:32
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jidesoft.icons.IconsFactory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CountryRenderer extends DefaultListCellRenderer {

    private static Map<Object, Icon> ICON_CACHE;

    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);

        if (ICON_CACHE == null) {
            ICON_CACHE = new HashMap();
        }
        Icon icon = ICON_CACHE.get(value);
        if (icon == null) {
            icon = IconsFactory.getImageIcon(CountryRenderer.class, "icons/" + value + ".png");
            ICON_CACHE.put(value, icon);
        }

        label.setIcon(icon);
        return label;
    }

}
