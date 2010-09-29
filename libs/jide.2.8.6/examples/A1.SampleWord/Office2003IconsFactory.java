/*
 * @(#)Office2003IconsFactory.JAVA
 *
 * Copyright 2002 - 2004 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.icons.IconsFactory;

import javax.swing.*;

/**
 */
public class Office2003IconsFactory {
    public static class Standard {
        public static final String NEW = "office2003/standard/new.gif";
        public static final String OPEN = "office2003/standard/open.gif";
        public static final String SAVE = "office2003/standard/save.gif";
        public static final String PERMISSION = "office2003/standard/permission.gif";
        public static final String EMAIL = "office2003/standard/e-mail.gif";
        // ----
        public static final String PRINT = "office2003/standard/print.gif";
        public static final String PRINT_PREVIEW = "office2003/standard/print-preview.gif";
        // ----
        public static final String SPELLING_GRAMMAR = "office2003/standard/spelling-grammar.gif";
        public static final String RESEARCH = "office2003/standard/research.gif";
        // ----
        public static final String CUT = "office2003/standard/cut.gif";
        public static final String COPY = "office2003/standard/copy.gif";
        public static final String PASTE = "office2003/standard/paste.gif";
        public static final String FORMAT_PAINTER = "office2003/standard/format-painter.gif";
        // ----
        public static final String UNDO = "office2003/standard/undo.gif";
        public static final String REDO = "office2003/standard/redo.gif";
        // ----
        public static final String INSERT_HYPERLINK = "office2003/standard/insert-hyperlink.gif";
        public static final String TABLES_BORDERS = "office2003/standard/tables-borders.gif";
        public static final String INSERT_TABLE = "office2003/standard/insert-table.gif";
        public static final String INSERT_EXCEL = "office2003/standard/insert-excel.gif";
        public static final String COLUMNS = "office2003/standard/columns.gif";
        public static final String DRAWING = "office2003/standard/drawing.gif";
        // ----
        public static final String DOCUMENT_MAP = "office2003/standard/document-map.gif";
        public static final String SHOW_HIDE_SYMBOL = "office2003/standard/show-hide-symbol.gif";
        public static final String HELP = "office2003/standard/help.gif";
        // ----
    }

    public static class Formatting {
        public static final String STYLE_FORMATTING = "office2003/formatting/style-formatting.gif";
        public static final String FORMAT_FONT = "office2003/formatting/format-font.gif";
        // ----

        public static final String BOLD = "office2003/formatting/bold.gif";
        public static final String ITALIC = "office2003/formatting/italic.gif";
        // ----

        public static final String ALIGN_LEFT = "office2003/formatting/align-left.gif";
        public static final String ALIGN_CENTER = "office2003/formatting/align-center.gif";
        public static final String ALIGN_RIGHT = "office2003/formatting/align-right.gif";
        public static final String JUSTIFY = "office2003/formatting/justify.gif";
        public static final String DISTRIBUTED = "office2003/formatting/distributed.gif";
        public static final String LINE_SPACING = "office2003/formatting/line-spacing.gif";
        // ----

        public static final String NUMBERING = "office2003/formatting/numbering.gif";
        public static final String BULLETS = "office2003/formatting/bullets.gif";
        public static final String DECREASE_INDENT = "office2003/formatting/decrease-indent.gif";
        public static final String INCREASE_INDENT = "office2003/formatting/increase-indent.gif";
        // ----

        public static final String HIGHLIGHT = "office2003/formatting/highlight.gif";
        public static final String OUTSIDE_BORDER = "office2003/formatting/outside-border.gif";
        public static final String FONT_COLOR = "office2003/formatting/font-color.gif";
    }

    public static class Drawing {
        public static final String SELECT_OBJECT = "office2003/drawing/select-object.gif";
        // ----

        public static final String LINE = "office2003/drawing/line.gif";
        public static final String ARROW = "office2003/drawing/arrow.gif";
        public static final String RECTANGLE = "office2003/drawing/rectangle.gif";
        public static final String OVAL = "office2003/drawing/oval.gif";
        public static final String TEXTBOX = "office2003/drawing/textbox.gif";
        public static final String VERTICAL_TEXTBOX = "office2003/drawing/vertical-textbox.gif";
        public static final String INSERT_WORDART = "office2003/drawing/insert-wordart.gif";
        public static final String INSERT_DIAGRAM_ORGCHART = "office2003/drawing/insert-diagram-orgchart.gif";
        public static final String INSERT_CLIPART = "office2003/drawing/insert-clipart.gif";
        public static final String INSERT_PICTURE = "office2003/drawing/insert-picture.gif";
        // ----

        public static final String FILL_COLOR = "office2003/drawing/fill-color.gif";
        public static final String LINE_COLOR = "office2003/drawing/line-color.gif";
        public static final String FONT_COLOR = "office2003/drawing/font-color.gif";
        public static final String LINE_STYLE = "office2003/drawing/line-style.gif";
        public static final String DASH_STYLE = "office2003/drawing/dash-style.gif";
        public static final String ARROW_STYLE = "office2003/drawing/arrow-style.gif";
        public static final String SHADOW_STYLE = "office2003/drawing/shadow-style.gif";
        public static final String THREED_STYLE = "office2003/drawing/3d-style.gif";
    }

    public static class Status {
        public static final String ERROR = "office2003/status/error.gif";
    }

    public static ImageIcon getImageIcon(String name) {
        if (name != null)
            return IconsFactory.getImageIcon(Office2003IconsFactory.class, name);
        else
            return null;
    }

    public static void main(String[] argv) {
        IconsFactory.generateHTML(Office2003IconsFactory.class);
    }
}
