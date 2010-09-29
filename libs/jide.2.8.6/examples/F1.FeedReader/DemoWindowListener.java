/*
 * @(#)DemoWindowListener.java 5/25/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.rss.FeedReader;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class DemoWindowListener implements WindowListener {

    private FeedReader _feedReader = null;

    DemoWindowListener(FeedReader demo) {
        _feedReader = demo;
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        _feedReader.dispose();
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

}
