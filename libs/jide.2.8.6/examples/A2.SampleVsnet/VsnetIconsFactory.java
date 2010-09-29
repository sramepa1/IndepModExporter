/*
 * @(#)VsnetIconsFactory.java
 *
 * Copyright 2002 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.icons.IconsFactory;

import javax.swing.*;

/**
 * A helper class to contain icons from Visual Studio .NET Those icons are copyrighted by Microsoft.
 */
public class VsnetIconsFactory {

    public static class Standard {
        public static final String ADD_NEW_ITEMS = "vsnet/standard/add_new_items.gif";
        public static final String OPEN = "vsnet/standard/open.gif";
        public static final String SAVE = "vsnet/standard/save.gif";
        public static final String SAVE_ALL = "vsnet/standard/save_all.gif";
        // ----

        public static final String CUT = "vsnet/standard/cut.gif";
        public static final String COPY = "vsnet/standard/copy.gif";
        public static final String PASTE = "vsnet/standard/paste.gif";
        // ----

        public static final String UNDO = "vsnet/standard/undo.gif";
        public static final String REDO = "vsnet/standard/redo.gif";
        public static final String NAVIGATE_BACKWARD = "vsnet/standard/navigate_backward.gif";
        public static final String NAVIGATE_FORWARD = "vsnet/standard/navigate_forward.gif";
        // ----

        public static final String START = "vsnet/standard/start.gif";
        // ----

        public static final String FIND_IN_FILES = "vsnet/standard/find_in_files.gif";
        // ----

        public static final String SOLUTION = "vsnet/toolwindows/solution_explorer.gif";
        public static final String PROPERTY = "vsnet/toolwindows/property.gif";
        public static final String TOOLBOX = "vsnet/toolwindows/toolbox.gif";

        // ---- // ----
        public static final String CLASSVIEW = "vsnet/toolwindows/class_view.gif";
        public static final String SERVER = "vsnet/toolwindows/server_explorer.gif";
        public static final String RESOURCEVIEW = "vsnet/toolwindows/resource_view.gif";

        // ---- // ----
        public static final String MACRO = "vsnet/toolwindows/macro_explorer.gif";
        public static final String OBJECT = "vsnet/toolwindows/object_browser.gif";
        public static final String DOCUMENTOUTLINE = "vsnet/toolwindows/document_outline.gif";

        // ---- // ----
        public static final String TASKLIST = "vsnet/toolwindows/tasklist.gif";
        public static final String COMMAND = "vsnet/toolwindows/command.gif";
        public static final String OUTPUT = "vsnet/toolwindows/output.gif";

        // ---- // ----
        public static final String FINDRESULT1 = "vsnet/toolwindows/find_result_1.gif";
        public static final String FINDRESULT2 = "vsnet/toolwindows/find_result_2.gif";
        public static final String FINDSYMBOL = "vsnet/toolwindows/find_symbol_result.gif";

        // ---- // ----
        public static final String FAVORITES = "vsnet/toolwindows/favorites.gif";
    }

    public static class Build {
        public static final String BUILD_FILE = "vsnet/build/build_file.gif";
        public static final String BUILD_SOLUTION = "vsnet/build/build_solution.gif";
        public static final String CANCEL = "vsnet/build/cancel.gif";
    }

    public static class Layout {
        // ----

        public static final String ALIGN_TO_GRID = "vsnet/layout/align_to_grid.gif";
        // ----

        public static final String ALIGN_LEFTS = "vsnet/layout/align_lefts.gif";
        public static final String ALIGN_CENTERS = "vsnet/layout/align_centers.gif";
        public static final String ALIGN_RIGHTS = "vsnet/layout/align_rights.gif";
        // ----

        public static final String ALIGN_TOPS = "vsnet/layout/align_tops.gif";
        public static final String ALIGN_MIDDLES = "vsnet/layout/align_middles.gif";
        public static final String ALIGN_BOTTOMS = "vsnet/layout/align_bottoms.gif";
        // ----

        public static final String MAKE_SAME_WIDTH = "vsnet/layout/make_same_width.gif";
        public static final String SIZE_TO_GRID = "vsnet/layout/size_to_grid.gif";
        public static final String MAKE_SAME_HEIGHT = "vsnet/layout/make_same_height.gif";
        public static final String MAKE_SAME_SIZE = "vsnet/layout/make_same_size.gif";
        // ----

        public static final String MAKE_HORI_SPACING_EQUAL = "vsnet/layout/make_hori_spacing_equal.gif";
        public static final String INC_HORI_SPACING = "vsnet/layout/inc_hori_spacing.gif";
        public static final String DEC_HORI_SPACING = "vsnet/layout/dec_hori_spacing.gif";
        public static final String REMOVE_HORI_SPACING = "vsnet/layout/remove_hori_spacing.gif";
        // ----

        public static final String MAKE_VERT_SPACING_EQUAL = "vsnet/layout/make_vert_spacing_equal.gif";
        public static final String INC_VERT_SPACING = "vsnet/layout/inc_vert_spacing.gif";
        public static final String DEC_VERT_SPACING = "vsnet/layout/dec_vert_spacing.gif";
        public static final String REMOVE_VERT_SPACING = "vsnet/layout/remove_vert_spacing.gif";
        // ----

        public static final String CENTER_HORI = "vsnet/layout/center_hori.gif";
        public static final String CENTER_VERT = "vsnet/layout/center_vert.gif";
        // ----

        public static final String BRING_TO_FRONT = "vsnet/layout/bring_to_front.gif";
        public static final String SEND_TO_BACK = "vsnet/layout/send_to_back.gif";
    }

    public static class Formatting {
        // ----

        public static final String BOLD = "vsnet/formatting/bold.gif";
        public static final String ITALIC = "vsnet/formatting/italic.gif";
        public static final String UNDERLINE = "vsnet/formatting/underline.gif";
        // ----

        public static final String FOREGROUND = "vsnet/formatting/foreground.gif";
        public static final String BACKGROUND = "vsnet/formatting/background.gif";
        // ----

        public static final String ALIGN_LEFT = "vsnet/formatting/align-left.gif";
        public static final String ALIGN_CENTER = "vsnet/formatting/align-center.gif";
        public static final String ALIGN_RIGHT = "vsnet/formatting/align-right.gif";
        public static final String JUSTIFY = "vsnet/formatting/justify.gif";
        // ----

        public static final String NUMBERING = "vsnet/formatting/numbering.gif";
        public static final String BULLETS = "vsnet/formatting/bullets.gif";
        // ----

        public static final String DECREASE_INDENT = "vsnet/formatting/decrease-indent.gif";
        public static final String INCREASE_INDENT = "vsnet/formatting/increase-indent.gif";
    }

    public static ImageIcon getImageIcon(String name) {
        if (name != null)
            return IconsFactory.getImageIcon(VsnetIconsFactory.class, name);
        else
            return null;
    }

    public static void main(String[] argv) {
        IconsFactory.generateHTML(VsnetIconsFactory.class);
    }


}
