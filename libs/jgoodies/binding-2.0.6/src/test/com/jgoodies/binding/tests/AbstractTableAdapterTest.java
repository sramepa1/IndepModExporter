/*
 * Copyright (c) 2002-2008 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jgoodies.binding.tests;

import javax.swing.ListModel;

import junit.framework.TestCase;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.list.ArrayListModel;

/**
 * A test case for class {@link AbstractTableAdapter}.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.9 $
 */
public final class AbstractTableAdapterTest extends TestCase {


    private static final String[] COLUMN_NAMES = {"Title", "Artist"};


    // Constructor Tests ******************************************************

    public void testConstructorRejectsNullListModel() {
        try {
            new ExampleTableModel(null);
        } catch (NullPointerException e) {
            // The expected behavior.
        }
        try {
            new ExampleTableModel(null, COLUMN_NAMES);
        } catch (NullPointerException e) {
            // The expected behavior.
        }
    }


    public void testConstructorAcceptsNullColumnNames() {
        try {
            new ExampleTableModel(new ArrayListModel<Object>());
        } catch (NullPointerException e) {
            fail("AbstractTableAdapter(ListModel) is correct if the ListModel is not null.");
        }
        try {
            new ExampleTableModel(new ArrayListModel<Object>(), null);
        } catch (NullPointerException e) {
            fail("AbstractTableAdapter(ListModel, String[]) must accept a null columnName argument.");
        }
    }


    // Helper Code ************************************************************

    /**
     * An example TableModel that presents an Album's title and artist.
     */
    private static final class ExampleTableModel extends AbstractTableAdapter<Object> {

        private ExampleTableModel(ListModel listModel) {
            super(listModel);
        }

        private ExampleTableModel(ListModel listModel, String[] columnNames) {
            super(listModel, columnNames);
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Object row = getRow(rowIndex);
            switch (columnIndex) {
                case 0 : return "Title of " + row;
                case 1 : return "Artist of " + row;
                default :
                    throw new IllegalStateException("Unknown column");
            }
        }

    }

}

