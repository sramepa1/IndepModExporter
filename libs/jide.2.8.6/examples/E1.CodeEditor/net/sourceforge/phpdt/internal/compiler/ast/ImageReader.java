package net.sourceforge.phpdt.internal.compiler.ast;

import javax.swing.*;

class ImageReader {
    static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ImageReader.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        }
        else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
