/*
 * @(#)FeedReaderDemo.java 8/20/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.rss.FeedReader;
import com.jidesoft.status.ProgressStatusBarItem;
import com.jidesoft.status.StatusBar;
import de.nava.informa.core.ItemIF;

import javax.swing.*;
import java.awt.*;

/**
 * Demoed Component: {@link com.jidesoft.editor.CodeEditor} <br> Required jar files: jide-common.jar, jide-editor.jar,
 * jide-components.jar, jide-shortcut.jar, jide-editor.jar <br> Required L&F: any L&F
 */
public class FeedReaderDemo extends AbstractDemo {
    public FeedReader _reader;

    public FeedReaderDemo() {
    }

    public String getName() {
        return "FeedReader Demo";
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
        _reader = new FeedReader(
                new String[]{
                        "http://www.jidesoft.com/blog/rss",
                        "http://www.curious-creature.org/feed/"
                }
                , "http://www.jidesoft.com/blog/rss"
        ) {
            @Override
            protected void displayHtmlBrowser(String content, ItemIF item) {
                content += "<p><a href=\"" + item.getLink().toExternalForm() + "\">Read more ...</a>";
                super.displayHtmlBrowser(content, item);
            }
        };
        StatusBar statusBar = new StatusBar();
        ProgressStatusBarItem barItem = new ProgressStatusBarItem();
        barItem.setMaxNumberOfHistoryItems(-1);
        statusBar.add(barItem);

        DemoFeedsListener demoRssFeedsListener = new DemoFeedsListener(barItem);
        _reader.addFeedEventListener(demoRssFeedsListener);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(_reader);
        panel.add(statusBar, BorderLayout.AFTER_LAST_LINE);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "F1.FeedReader";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeel();
        showAsFrame(new FeedReaderDemo());
    }
}
