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

import java.util.Arrays;

import javax.swing.DefaultListModel;

import junit.framework.TestCase;

import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.tests.event.ListDataReport;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * Tests the {@link ComboBoxAdapter}.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.27 $
 *
 * @see ComboBoxAdapter
 */
public final class ComboBoxAdapterTest extends TestCase {

    private static final String[] AN_ARRAY = {"one", "two", "three"};

    private DefaultListModel listModel;
    private SelectionInList<String> selectionInList;
    private ValueModel selectionHolder;

    private ComboBoxAdapter<String> combo;
    private ListDataReport report;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        initComboWithListModel();
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        listModel = null;
        combo = null;
        selectionInList = null;
        report = null;
    }


    /**
     * Checks that the constructors reject null selectionHolders with a NPE.
     */
    public void testConstructorRejectsNullValues() {
        final String message = "Constructor must throw NPE on null selectionHolder.";
        try {
            new ComboBoxAdapter<String>(listModel, null);
            fail(message);
        } catch (NullPointerException e) {
            // the expected behaviour
        }
        try {
            new ComboBoxAdapter<String>(AN_ARRAY, null);
            fail(message);
        } catch (NullPointerException e) {
            // the expected behaviour
        }
        try {
            new ComboBoxAdapter<String>(Arrays.asList(AN_ARRAY), null);
            fail(message);
        } catch (NullPointerException e) {
            // the expected behaviour
        }
        try {
            new ComboBoxAdapter<String>((SelectionInList<String>)null);
            fail("Constructor must reject a null selectionInList.");
        } catch (NullPointerException e) {
            // the expected behaviour
        }
    }


    /**
     * Tests that the event source for all events fired by
     * ComboBoxModel operations is the ComboBoxModel.
     */
    public void testEventSource() {
        final String message = "The event source must be the combo box model.";
        assertEquals("Initial size must be " + AN_ARRAY.length,
                AN_ARRAY.length, combo.getSize());
        listModel.addElement("some");
        assertEquals(message, combo, report.lastEvent().getSource());
        listModel.remove(0);
        assertEquals(message, combo, report.lastEvent().getSource());
        listModel.setElementAt("newElement", 0);
        assertEquals(message, combo, report.lastEvent().getSource());
        combo.setSelectedItem("three");
        assertEquals(message, combo, report.lastEvent().getSource());
    }


    /**
     * Checks that manipulations on the underlying ListModel
     * throw one event only, or in other words: avoid duplicate events.
     */
    public void testOneEventOnListModelManipulation() {
        assertOneEventOnListModelManipulation();
        listModel = createListModel(AN_ARRAY);
        selectionInList = new SelectionInList<String>(listModel);
        initComboWithSelectionInList();
        assertOneEventOnListModelManipulation();
    }


    private void assertOneEventOnListModelManipulation() {
        listModel.addElement("some");
        assertEquals("event count", 1, report.eventCount());
        listModel.removeElementAt(0);
        assertEquals("event count", 2, report.eventCount());
        listModel.setElementAt("newElement", 0);
        assertEquals("event count", 3, report.eventCount());
    }


    //    public void testInitialSelectionInList() {
    //        selectionHolder.setValue(listModel.getElementAt(1));
    //        SelectionInList sil = new SelectionInList(listModel);
    //        combo = new ComboBoxAdapter(sil, selectionHolder);
    //        assertEquals("Selection index shall reflect the initial selection.",
    //                1,
    //                sil.getSelectionIndex());
    //    }
    //
    //    public void testInitialSelectionInCombo() {
    //        selectionHolder.setValue("notInList");
    //        SelectionInList sil = new SelectionInList(listModel);
    //        sil.setSelectionIndex(1);
    //        combo = new ComboBoxAdapter(sil, selectionHolder);
    //        assertEquals("Selection index shall reflect the initial selection.",
    //                -1,
    //                sil.getSelectionIndex());
    //    }

    /**
     * Checks that a selection action throws one event only.
     */
    public void testOneEventOnComboSelection() {
        assertOneEventOnComboSelection();
        initComboWithArray();
        assertOneEventOnComboSelection();
        initComboWithList();
        assertOneEventOnComboSelection();
        initComboWithSelectionInList();
        assertOneEventOnComboSelection();
    }


    private void assertOneEventOnComboSelection() {
        combo.setSelectedItem("three");
        assertEquals("event count", 1, report.eventCount());
    }


    /**
     * Checks that a change in the selection holder value throws one event only.
     */
    public void testOneEventOnHolderSelection() {
        assertOneEventOnHolderSelection();
        initComboWithArray();
        assertOneEventOnHolderSelection();
        initComboWithList();
        assertOneEventOnHolderSelection();
    }


    private void assertOneEventOnHolderSelection() {
        selectionHolder.setValue("three");
        assertEquals("event count", 1, report.eventCount());
    }


    public void testAllowSelectionNotInList() {
        combo.setSelectedItem("notInList");
        assertEquals("combo must accept any selection",
                "notInList",
                combo.getSelectedItem());
    }


    /**
     * Checks that an absent element should be rejected
     * if the adapter is constructed with a selectionInList.
     */
    public void testRejectSelectionNotInList() {
        initComboWithSelectionInList();
        combo.setSelectedItem("notInList");
        assertNull("combo must reject not contained elements",
                combo.getSelectedItem());
    }


    public void testChangeListSameSelection() {
        initComboWithSelectionInList();
        String[] anotherArray = new String[]{"one", "two", "three", "four"};
        selectionInList.setListModel(createListModel(anotherArray));
        assertTrue("combo must fire at least one event, either 1 content change or a combination of add/remove or content/add or content/remove",
                report.eventCount() >= 1);
    }


    // Changing the Selection *************************************************

    /**
     * Verifies that a ComboBoxAdapter built with a SelectionInList
     * keeps the selection in synch with an underlying ListModel.
     */
    public void testSelectionAfterSetElement() {
        initComboWithSelectionInList();
        combo.setSelectedItem("two");
        listModel.set(1, "other");
        assertEquals("combo selection must be changed",
                "other",
                combo.getSelectedItem());
    }

    /**
     * Verifies that a ComboBoxAdapter built with a selection holder
     * won't update the selection if the underlying ListModel changes.
     */
    public void testSelectionAfterSetElementDecoupled() {
        combo.setSelectedItem("two");
        listModel.set(1, "other");
        assertEquals("combo selection must be changed",
                "two",
                combo.getSelectedItem());
    }

    // Removing Items, Requires an Underlying ListModel ***********************

    public void testSelectionAfterRemoveBefore() {
        testSelectionAfterRemove(AN_ARRAY[1], 0);
    }


    public void testSelectionAfterRemoveAfter() {
        testSelectionAfterRemove(AN_ARRAY[1], 2);
    }


    /**
     * Verifies that the selection will be cleared if
     * it has been removed from the underlying list.
     */
    public void testSelectionAfterRemoveOn() {
        initComboWithSelectionInList();
        combo.setSelectedItem("two");
        listModel.remove(1);
        assertEquals("Combo's selection must be empty",
                null,
                combo.getSelectedItem());
    }


    /**
     * Verifies that the selection holder is unchanged
     * if the selection is removed from the underlying list.
     */
    public void testSelectionAfterRemoveOnDecoupled() {
        combo.setSelectedItem("two");
        listModel.remove(1);
        assertEquals("Combo's selection must be empty",
                "two",
                combo.getSelectedItem());
    }


    public void testSelectionAfterRemoveUnrelated() {
        testSelectionAfterRemove("notInList", 1);
    }


    private void testSelectionAfterRemove(Object initialSelection, int index) {
        combo.setSelectedItem(initialSelection);
        listModel.remove(index);
        assertEquals("Combo's selection must be unchanged after remove",
                initialSelection, combo.getSelectedItem());
    }


    /**
     * This is for comparison only:
     *  taking DefaultComboBoxModel as reference.
     */
    //  public void testDefaultComboSelectionAfterRemoveOn() {
    //    DefaultComboBoxModel combo = new DefaultComboBoxModel(someArray);
    //    combo.setSelectedItem(someArray[1]);
    //    combo.removeElementAt(1);
    //    // PENDING: how independent is the combo selection? remove
    //    // if removed from list?
    //    // DefaultComboBoxModel moves selection to previous item
    //    assertEquals("pathological behaviour of DefaultComboBoxModel ", "two", combo.getSelectedItem());
    //
    //  }
    // Inserting Items, Requires an underlying ListModel **********************
    public void testSelectionAfterInsertBefore() {
        testSelectionAfterInsert(AN_ARRAY[1], 0);
    }


    public void testSelectionAfterInsertAfter() {
        testSelectionAfterInsert(AN_ARRAY[1], 2);
    }


    public void testSelectionAfterInsertOn() {
        testSelectionAfterInsert(AN_ARRAY[1], 1);
    }


    public void testSelectionAfterInsertUnrelated() {
        testSelectionAfterInsert("unrelated", 1);
    }


    private void testSelectionAfterInsert(Object initialSelection, int index) {
        combo.setSelectedItem(initialSelection);
        updateReport();
        int oldSize = combo.getSize();
        listModel.insertElementAt("another", index);
        assertEquals("Combo's selection must be unchanged after insert",
                initialSelection, combo.getSelectedItem());
        assertEquals("size", oldSize + 1, combo.getSize());
    }


    // List Changes, Requires an Underlying SelectionInList *******************

    /**
     * Tests effects of setting new List when
     * selectionInList controls selection.
     */
    public void testNewListWithSelectedItem() {
        // selection is controlled by SelectionInList!
        initComboWithSelectionInList();
        combo.setSelectedItem("two");
        updateReport();
        Object[] array = {"totally", "different", "two", "content"};
        selectionInList.setListModel(createListModel(array));
        assertEquals("selection must be unchanged",
                "two",
                combo.getSelectedItem());

    }


    /**
     * Tests effects of setting new List when
     * selectionInList controls selection.
     */
    public void testNewListWithoutSelectedItem() {
        // The selection is controlled by SelectionInList!
        initComboWithSelectionInList();
        combo.setSelectedItem("two");
        updateReport();
        Object[] array = {"totally", "different", "content"};
        selectionInList.setListModel(createListModel(array));
        assertEquals("combo selection must be null",
                null,
                combo.getSelectedItem());
    }


    /**
     * Tests effects of setting new List when
     * selectionInList does not control selection.
     */
    /*
     public void testNewListWithoutSelectedItemOldWasNotInList() {
     // The selection is _not_ controlled by SelectionInList!
     initComboWithSelectionInListAndSelection();
     combo.setSelectedItem("notInList");
     updateReport();
     Object[] array = { "totally", "different", "content" };
     selectionInList.setListModel(createListModel(array));
     assertEquals("combo selection must be unchanged",
     "notInList",
     combo.getSelectedItem());
     }
     */

    /**
     * Tests effects of setting new List when
     * selectionInList does not control selection.
     */
    //    public void testNewListWithoutSelectedItemOldWasInList() {
    //        // The selection is _not_ controlled by SelectionInList!
    //        initComboWithSelectionInListAndSelection();
    //        combo.setSelectedItem("one");
    //        updateReport();
    //        Object[] array = { "totally", "different", "content" };
    //        selectionInList.setListModel(createListModel(array));
    //        assertEquals("combo selection must be null", null, combo.getSelectedItem());
    //    }
    //
    //
    //    public void testChangeSelectionInList() {
    //        initComboWithSelectionInListAndSelection();
    //        selectionInList.setSelection(AN_ARRAY[0]);
    //        assertEquals(
    //                "combo selection must be updated to selectionInList selection",
    //                selectionInList.getSelection(),
    //                combo.getSelectedItem());
    //        assertEquals("combo must fire one contentsChanged",
    //                1,
    //                report.eventCountChange());
    //        updateReport();
    //        combo.setSelectedItem("notInList");
    //        assertNull("selectionInList selection must be empty",
    //                selectionInList.getSelection());
    //        assertEquals("combo must fire one contentsChanged",
    //                1,
    //                report.eventCountChange());
    //        selectionInList.setSelection(AN_ARRAY[1]);
    //        assertEquals("combo selection must (or not??) be synched",
    //                AN_ARRAY[1],
    //                combo.getSelectedItem());
    //    }

    // Setup Helper Code ******************************************************
    private void updateReport() {
        report = new ListDataReport();
        combo.addListDataListener(report);
    }


    private void initComboWithSelectionInList() {
        initBasicFields();
        combo = new ComboBoxAdapter<String>(selectionInList);
        updateReport();
    }


    private void initComboWithArray() {
        initBasicFields();
        combo = new ComboBoxAdapter<String>(AN_ARRAY, selectionHolder);
        updateReport();
    }


    private void initComboWithListModel() {
        initBasicFields();
        combo = new ComboBoxAdapter<String>(listModel, selectionHolder);
        updateReport();
    }


    private void initComboWithList() {
        initBasicFields();
        combo = new ComboBoxAdapter<String>(Arrays.asList(AN_ARRAY), selectionHolder);
        updateReport();
    }


    private void initBasicFields() {
        listModel = createListModel(AN_ARRAY);
        selectionInList = new SelectionInList<String>(listModel);
        selectionHolder = new ValueHolder(null);
    }


    private DefaultListModel createListModel(Object[] array) {
        DefaultListModel model = new DefaultListModel();
        for (Object element : array) {
            model.addElement(element);
        }
        return model;
    }

}
