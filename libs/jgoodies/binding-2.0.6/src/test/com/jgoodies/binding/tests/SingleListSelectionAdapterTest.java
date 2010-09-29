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

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import junit.framework.TestCase;

import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.tests.event.ListSelectionReport;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * A test case for class
 * {@link com.jgoodies.binding.adapter.SingleListSelectionAdapter}.
 *
 * @author Karsten Lentzsch
 * @author Jeanette Winzenburg
 * @version $Revision: 1.13 $
 */
public final class SingleListSelectionAdapterTest extends TestCase {

    /**
     * Holds a sequence of indices used to test index changes.
     */
    private static final int[] INDICES =
        {0, -1, -1, 3, 2, -1, 1, 2, 3, 4, 0, -1};

    /**
     * Checks that changes to the underlying ValueModel update the adapter.
     */
    public void testAdapterReflectsModelChanges() {
        ValueModel model = new ValueHolder();
        SingleListSelectionAdapter adapter =
            new SingleListSelectionAdapter(model);
        for (int index : INDICES) {
            model.setValue(new Integer(index));
            assertEquals("New adapter index",
                    index,
                    adapter.getMinSelectionIndex());
        }
    }

    /**
     * Checks that changes to the adapter update the underlying ValueModel.
     */
    public void testModelReflectsAdapterChanges() {
        ValueModel model = new ValueHolder();
        SingleListSelectionAdapter adapter =
            new SingleListSelectionAdapter(model);
        for (int index : INDICES) {
            adapter.setLeadSelectionIndex(index);
            assertEquals("New model index",
                    new Integer(index),
                    model.getValue());
        }
    }

    /**
     * Checks that index changes of the underlying ValueModel are observed and
     * reported by the adapter.<p>
     *
     * Wrong test: changeReport.getNew/OldValue() !=
     * listReport.getFirst/LastIndex() if switching between selected/unselected
     *
     */
//  public void testAdapterFiresModelEvents() {
//
//        ValueModel model = new ValueHolder(-1);
//        SingleListSelectionAdapter adapter = new SingleListSelectionAdapter(
//                model);
//        PropertyChangeReport changeReport = new PropertyChangeReport();
//        ListSelectionReport listReport = new ListSelectionReport();
//        model.addValueChangeListener(changeReport);
//        adapter.addListSelectionListener(listReport);
//        for (int i = 0; i < INDICES.length; i++) {
//            int index = INDICES[i];
//            model.setValue(new Integer(index));
//            if (changeReport.hasEvents()) {
//                Object oldValue = changeReport.lastEvent().getOldValue();
//                Object newValue = changeReport.lastEvent().getNewValue();
//                int oldIndex = (oldValue == null) ? -1 : ((Integer) oldValue)
//                        .intValue();
//                int newIndex = (newValue == null) ? -1 : ((Integer) newValue)
//                        .intValue();
//                int firstIndex = Math.min(oldIndex, newIndex);
//                int lastIndex = Math.max(oldIndex, newIndex);
//                assertEquals("Last model event index", new Integer(index),
//                        newValue);
//                if (listReport.hasEvents()) {
//                    assertEquals("Wrong test...Last adapter event first index",
//                            firstIndex, listReport.lastEvent().getFirstIndex());
//                    assertEquals("Wrong test...Last adapter event last index",
//                            lastIndex, listReport.lastEvent().getLastIndex());
//                }
//            }
//        }
//
//        // Check that the model and adapter throw the same number of events.
//        assertEquals("Event count", changeReport.eventCount(), listReport
//                .eventCount());
//    }



    /**
     * Checks for correct SelectionEvent on empty --> selected. (an index of -1
     * is invalid because it does not represent a position in the list that
     * might have changed selection state).
     */
    public void testSelectEvent() {
        ValueModel model = new ValueHolder(-1);
        SingleListSelectionAdapter adapter =
            new SingleListSelectionAdapter(model);
        ListSelectionReport report = new ListSelectionReport();
        adapter.addListSelectionListener(report);
        adapter.setSelectionInterval(2, 2);
        ListSelectionEvent e = report.lastEvent();
        assertEquals("first must be index of changed selection ",
                2,
                e.getFirstIndex());
    }

    /**
     * Checks for correct SelectionEvent on selected --> empty.
     */
    public void testDeSelectEvent() {
        ValueModel model = new ValueHolder(2);
        SingleListSelectionAdapter adapter =
            new SingleListSelectionAdapter(model);
        ListSelectionReport report = new ListSelectionReport();
        adapter.addListSelectionListener(report);
        adapter.clearSelection();
        ListSelectionEvent e = report.lastEvent();
        assertEquals("first must be index of changed selection ",
                2,
                e.getFirstIndex());
    }

    public void testChangeSelectEvent() {
        ValueModel model = new ValueHolder(2);
        SingleListSelectionAdapter adapter =
            new SingleListSelectionAdapter(model);
        ListSelectionReport report = new ListSelectionReport();
        adapter.addListSelectionListener(report);
        adapter.setSelectionInterval(3, 3);
        ListSelectionEvent e = report.lastEvent();
        assertEquals("first must be lower index of changed selection ",
                2,
                e.getFirstIndex());
        assertEquals("last must be upper index of changed selection ",
                3,
                e.getLastIndex());

    }

    public void testIsSelectedIndex() {
        ListSelectionModel model = getSelectionAdapter(-1);
        assertTrue("selection must return empty", model.isSelectionEmpty());
        assertFalse("isSelectedIndex(-1) must return false",
                model.isSelectedIndex(-1));
    }

    public void testRemoveSelectionInterval() {
        ListSelectionModel adapter = getSelectionAdapter(1);
        adapter.removeSelectionInterval(0, 2);
        assertTrue("selection must be empty ", adapter.isSelectionEmpty());
    }

    public void testAddSelectionInterval() {
        ListSelectionModel adapter = getSelectionAdapter(-1);
        try {
            adapter.addSelectionInterval(0, 2);
        } catch (UnsupportedOperationException ex) {
            fail("adapter must not fire on legal operation" + ex);
        }

    }
    //--------------------- insert/removeIndexInterval

    public void testInsertIndexIntervalBefore() {
        ListSelectionModel adapter = getSelectionAdapter(2);
        adapter.insertIndexInterval(0, 1, false);
        assertEquals("selectionindex must be increased by 1",
                3,
                adapter.getMinSelectionIndex());

    }
    public void testInsertIndexIntervalAfter() {
        ListSelectionModel adapter = getSelectionAdapter(2);
        adapter.insertIndexInterval(3, 1, false);
        assertEquals("selectionindex must be unchanged",
                2,
                adapter.getMinSelectionIndex());
    }

    public void testRemoveIndexIntervalAfter() {
        ListSelectionModel adapter = getSelectionAdapter(2);
        adapter.removeIndexInterval(3, 3);
        assertEquals("selectionindex must be unchanged",
                2,
                adapter.getMinSelectionIndex());

    }

    public void testRemoveIndexIntervalBefore() {
        ListSelectionModel adapter = getSelectionAdapter(2);
        adapter.removeIndexInterval(0, 0);
        assertEquals("selectionindex must be decreased by 1",
                1,
                adapter.getMinSelectionIndex());

    }

    public void testRemoveIndexIntervalOn() {
        ListSelectionModel model = getSelectionAdapter(2);
        model.removeIndexInterval(2, 2);
        assertTrue("selection must be empty", model.isSelectionEmpty());

    }

    public void testLead() {
        ListSelectionModel model = getSelectionAdapter(-1);
        model.setSelectionInterval(0, 2);
        assertEquals("second parameter must be lead",
                2,
                model.getLeadSelectionIndex());
    }

    private ListSelectionModel getSelectionAdapter(int index) {
        ValueModel model = new ValueHolder(index);
        SingleListSelectionAdapter adapter =
            new SingleListSelectionAdapter(model);
        return adapter;
    }

    //---------------- testing reference implementation

    public void testReferenceInsertIndexIntervalBefore() {
        ListSelectionModel model = createReferenceSelection(2);
        model.insertIndexInterval(0, 1, false);
        assertEquals("selectionindex must be increased by 1",
                3,
                model.getMinSelectionIndex());
    }
    public void testReferenceInsertIndexIntervalAfter() {
        ListSelectionModel model = createReferenceSelection(2);
        model.insertIndexInterval(3, 1, false);
        assertEquals("selectionindex must be unchanged",
                2,
                model.getMinSelectionIndex());
    }

    public void testReferenceRemoveIndexIntervalBefore() {
        ListSelectionModel model = createReferenceSelection(2);
        model.removeIndexInterval(0, 0);
        assertEquals("selectionindex must be decreased by 1",
                1,
                model.getMinSelectionIndex());
    }

    public void testReferenceRemoveIndexIntervalAfter() {
        ListSelectionModel model = createReferenceSelection(2);
        model.removeIndexInterval(3, 3);
        assertEquals("selectionindex must be unchanged",
                2,
                model.getMinSelectionIndex());

    }

    public void testReferenceRemoveIndexIntervalOn() {
        ListSelectionModel model = createReferenceSelection(2);
        model.removeIndexInterval(2, 2);
        assertTrue("selection must be empty", model.isSelectionEmpty());

    }

    public void testReferenceIsSelectedIndex() {
        ListSelectionModel model = createReferenceSelection(-1);
        assertTrue("selection must be empty", model.isSelectionEmpty());
        assertFalse("isSelectedIndex(-1) must return false",
                model.isSelectedIndex(-1));
    }

    public void testReferenceRemoveSelectionInterval() {
        ListSelectionModel adapter = createReferenceSelection(1);
        adapter.removeSelectionInterval(0, 2);
        assertTrue("selection must be empty ", adapter.isSelectionEmpty());
    }

    public void testReferenceAddSelectionInterval() {
        ListSelectionModel adapter = createReferenceSelection(-1);
        try {
            adapter.addSelectionInterval(0, 2);
        } catch (UnsupportedOperationException ex) {
            fail("adapter must not fire on legal operation" + ex);
        }
    }

    public void testReferenceLead() {
        ListSelectionModel model = createReferenceSelection(-1);
        model.setSelectionInterval(0, 2);
        assertEquals("second parameter must be lead",
                2,
                model.getLeadSelectionIndex());
    }


    private ListSelectionModel createReferenceSelection(int i) {
        ListSelectionModel model = new DefaultListSelectionModel();
        model.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        model.setSelectionInterval(i, i);
        return model;
    }


}
