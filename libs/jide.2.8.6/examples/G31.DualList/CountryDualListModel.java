/*
 * CountryDualListModel.java
 *
 * Created on Oct 18, 2007, 1:01:13 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jidesoft.list.AbstractDualListModel;

public class CountryDualListModel extends AbstractDualListModel {

    private static final String[] COUNTRY_TABLE = {
            "Argentina", "Australia",
            "Brazil", "Canada", "China", "France", "Germany", "India", "Ireland", "Italy",
            "Japan", "Portugal", "Russia", "Saudi Arabia", "Singapore",
            "South Korea", "Spain", "Sweden", "United Arab Emirates", "United Kingdom",
            "United States"
    };

    public int getSize() {
        return COUNTRY_TABLE.length;
    }

    public Object getElementAt(int index) {
        return COUNTRY_TABLE[index];
    }

}
