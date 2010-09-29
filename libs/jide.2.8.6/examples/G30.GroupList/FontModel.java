/*
 * FontModel.java
 *
 * Created on Oct 16, 2007, 11:59:02 AM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jidesoft.list.AbstractGroupableListModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class FontModel extends AbstractGroupableListModel {

    private static final String[] FONT_NAMES =
            GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    private static String[] GROUP_NAMES = {"Recently Used Fonts", "All Fonts"};

    private List<Font> _fonts;
    private int _limit;
    private List<Font> _recentlyUsed;

    public FontModel() {
        this(8);
    }

    public FontModel(int limit) {
        _limit = limit;
        _recentlyUsed = new LinkedList();
        _fonts = new ArrayList();
        for (String fontName : FONT_NAMES) {
            _fonts.add(Font.decode(fontName));
        }
    }

    @Override
    public Object getGroupAt(int index) {
        if (index < _recentlyUsed.size()) {
            return GROUP_NAMES[0];
        }
        else {
            return GROUP_NAMES[1];
        }
    }

    public int getSize() {
        return _recentlyUsed.size() + _fonts.size();
    }

    public Object getElementAt(int index) {
        int usedSize = _recentlyUsed.size();
        if (index < usedSize) {
            return _recentlyUsed.get(index);
        }
        return _fonts.get(index - usedSize);
    }

    public List<Font> getRecentlyUsedFont() {
        return Collections.unmodifiableList(_recentlyUsed);
    }

    public void putFont(Font font) {
        if (_recentlyUsed.contains(font)) {
            _recentlyUsed.remove(font);
        }
        _recentlyUsed.add(0, font);
        if (_recentlyUsed.size() > _limit) {
            _recentlyUsed.remove(_recentlyUsed.size() - 1);
        }
        super.fireGroupChanged(this);
    }

    @Override
    public Object[] getGroups() {
        return GROUP_NAMES;
    }

    public void remove(int index) {
        int usedSize = _recentlyUsed.size();
        if (index < usedSize) {
            _recentlyUsed.remove(index);
        }
        else {
            _fonts.remove(index - usedSize);
        }
        fireGroupChanged(this);
    }

    public void shuffle() {
        Collections.shuffle(_recentlyUsed);
        Collections.shuffle(_fonts);
        fireGroupChanged(this);
    }
}

