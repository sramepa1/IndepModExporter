/*
 * @(#)FeedTableDemo.java 8/23/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.rss.FeedItemTableModel;
import com.jidesoft.rss.FeedTable;
import de.nava.informa.core.ParseException;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.parsers.FeedParser;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Demoed Component: {@link com.jidesoft.editor.CodeEditor} <br> Required jar files:
 * jide-common.jar, jide-editor.jar, jide-components.jar, jide-shortcut.jar, jide-editor.jar <br>
 * Required L&F: any L&F
 */
public class FeedTableDemo extends AbstractDemo {
    public FeedTable _table;

    public FeedTableDemo() {
    }

    public String getName() {
        return "FeedReader Demo (Table Only)";
    }

    public String getProduct() {
        return PRODUCT_NAME_FEEDREADER;
    }

    @Override
    public String getDescription() {
        return "This is an example of how to use FeedReader.\n\n" +
                "Demoed classes:\n" +
                "com.jidesoft.rss.FeedReader";
    }

    public Component getDemoPanel() {
        FeedItemTableModel model = new FeedItemTableModel(10);
        try {
            model.openChannel(FeedParser.parse(new ChannelBuilder(), "http://www.curious-creature.org/feed/"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        _table = new FeedTable(model);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(_table));
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "F1.FeedReader";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeel();
        showAsFrame(new FeedTableDemo());
    }
}
