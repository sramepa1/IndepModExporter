/*
 * @(#)SampleIconsFactory.java
 *
 * Copyright 2002-2003 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.icons.IconsFactory;

import javax.swing.*;

/**
 * Demoed Component: {@link IconsFactory} <br> Required jar files: jide-common.jar <br> Required L&F: any L&F
 */
public class IconsFactoryDemo {

    public static class CollapsiblePane {
        public static final String CONTROL_PANEL = "icons/e_control_panel.png";
        public static final String OTHER = "icons/e_other.png";
        public static final String RENAME = "icons/e_rename.png";
        public static final String MOVE = "icons/e_move.png";
        public static final String COPY = "icons/e_copy.png";
        public static final String PUBLISH = "icons/e_publish.png";
        public static final String EMAIL = "icons/e_email.png";
        public static final String DELET = "icons/e_delete.png";
        public static final String LOCALDISK = "icons/e_localdisk.png";
        public static final String PICTURES = "icons/e_pictures.png";
        public static final String COMPUTER = "icons/e_computer.png";
        public static final String NETWORK = "icons/e_network.png";
    }

    public static class OutlookShortcuts {
        public static final String TODAY = "icons/o_today.png";
        public static final String INBOX = "icons/o_inbox.png";
        public static final String CONTACTS = "icons/o_contacts.png";
        public static final String TASKS = "icons/o_tasks.png";
        public static final String CALENDAR = "icons/o_calendar.png";
        public static final String NOTES = "icons/o_notes.png";
        public static final String DELETED_ITEMS = "icons/o_deleted_items.png";

        public static final String DRAFTS = "icons/o_drafts.png";
        public static final String OUTBOX = "icons/o_outbox.png";
        public static final String SEND_ITEMS = "icons/o_send_items.png";
        public static final String JOURNAL = "icons/o_journal.png";

        public static final String COMPUTER = "icons/o_my_computer.png";
        public static final String NETWORK = "icons/o_my_network.png";
        public static final String DOCUMENTS = "icons/o_my_documents.png";
    }

    public static class PropertiesWindow {
        public static final String CATEGORIED = "icons/t_category.png";
        public static final String SORT = "icons/t_sort.png";
        public static final String DESCRIPTION = "icons/t_description.png";
    }

    public static ImageIcon getImageIcon(String name) {
        if (name != null)
            return IconsFactory.getImageIcon(IconsFactoryDemo.class, name);
        else
            return null;
    }

    public static void main(String[] argv) {
        IconsFactory.generateHTML(IconsFactoryDemo.class);
    }


}
