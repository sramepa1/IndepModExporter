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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import junit.framework.TestCase;

import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.LinkedListModel;
import com.jgoodies.binding.list.ObservableList;
import com.jgoodies.binding.tests.event.ListDataReport;

/**
 * A test case for classes {@link ArrayListModel} and {@link LinkedListModel}.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.18 $
 */
public final class ObservableListTest extends TestCase {

    /**
     * Tests the constructor that accepts a collection as initial content.
     */
    public void testListConstructor() {
        new ArrayListModel<String>(createEmptyArrayListModel());
        new LinkedListModel<String>(createEmptyArrayListModel());

        new ArrayListModel<String>(createSingleElementArrayListModel());
        new LinkedListModel<String>(createSingleElementLinkedListModel());

        new ArrayListModel<String>(createFilledArrayListModel());
        new LinkedListModel<String>(createFilledLinkedListModel());
    }

    /**
     * Tests a single add operation on different observable lists.
     */
    public void testAdd() {
        testAdd(createEmptyArrayListModel());
        testAdd(createEmptyLinkedListModel());

        testAdd(createSingleElementArrayListModel());
        testAdd(createSingleElementLinkedListModel());

        testAdd(createFilledArrayListModel());
        testAdd(createFilledLinkedListModel());
    }


    /**
     * Tests a single indexed add operation on different observable lists.
     */
    public void testAddIndexed() {
        testAddIndexed(createEmptyArrayListModel(), 0);
        testAddIndexed(createEmptyLinkedListModel(), 0);

        testAddIndexed(createSingleElementArrayListModel(), 0);
        testAddIndexed(createSingleElementLinkedListModel(), 0);
        testAddIndexed(createSingleElementArrayListModel(), 1);
        testAddIndexed(createSingleElementLinkedListModel(), 1);

        int size1 = createFilledArrayListModel().size();
        for (int i = 0; i <= size1; i++) {
            testAddIndexed(createFilledArrayListModel(), i);
        }
        int size2 = createFilledLinkedListModel().size();
        for (int i = 0; i <= size2; i++) {
            testAddIndexed(createFilledLinkedListModel(), i);
        }
    }


    /**
     * Tests a multiple add operation on different combinations of observable lists.
     */
    public void testAddAll() {
        testAddAll(createEmptyArrayListModel(),  createEmptyArrayListModel());
        testAddAll(createEmptyLinkedListModel(), createEmptyArrayListModel());
        testAddAll(createEmptyArrayListModel(),  createSingleElementArrayListModel());
        testAddAll(createEmptyLinkedListModel(), createSingleElementArrayListModel());
        testAddAll(createEmptyArrayListModel(),  createFilledArrayListModel());
        testAddAll(createEmptyLinkedListModel(), createFilledArrayListModel());

        testAddAll(createSingleElementArrayListModel(),  createEmptyArrayListModel());
        testAddAll(createSingleElementLinkedListModel(), createEmptyArrayListModel());
        testAddAll(createSingleElementArrayListModel(),  createSingleElementArrayListModel());
        testAddAll(createSingleElementLinkedListModel(), createSingleElementArrayListModel());
        testAddAll(createSingleElementArrayListModel(),  createFilledArrayListModel());
        testAddAll(createSingleElementLinkedListModel(), createFilledArrayListModel());

        testAddAll(createFilledArrayListModel(),  createEmptyArrayListModel());
        testAddAll(createFilledLinkedListModel(), createEmptyArrayListModel());
        testAddAll(createFilledArrayListModel(),  createSingleElementArrayListModel());
        testAddAll(createFilledLinkedListModel(), createSingleElementArrayListModel());
        testAddAll(createFilledArrayListModel(),  createFilledArrayListModel());
        testAddAll(createFilledLinkedListModel(), createFilledLinkedListModel());
    }


    /**
     * Tests indexed multiple add operations on different combinations of
     * observable lists.
     */
    public void testAddAllIndexed() {
        testAddAllIndexed(createEmptyArrayListModel(), createFilledArrayListModel(), 0);
        testAddAllIndexed(createEmptyLinkedListModel(), createFilledArrayListModel(), 0);

        testAddAllIndexed(createSingleElementArrayListModel(), createFilledArrayListModel(), 0);
        testAddAllIndexed(createSingleElementLinkedListModel(), createFilledArrayListModel(), 0);
        testAddAllIndexed(createSingleElementArrayListModel(), createFilledArrayListModel(), 1);
        testAddAllIndexed(createSingleElementLinkedListModel(), createFilledArrayListModel(), 1);

        int size1 = createFilledArrayListModel().size();
        for (int i = 0; i <= size1; i++) {
            testAddAllIndexed(createFilledArrayListModel(), createFilledArrayListModel(), i);
        }
        int size2 = createFilledLinkedListModel().size();
        for (int i = 0; i <= size2; i++) {
            testAddAllIndexed(createFilledLinkedListModel(), createFilledArrayListModel(),  i);
        }
    }


    /**
     * Tests the #clear operation on different observable lists.
     */
    public void testClear() {
        testClear(createEmptyArrayListModel());
        testClear(createEmptyLinkedListModel());

        testClear(createSingleElementArrayListModel());
        testClear(createSingleElementLinkedListModel());

        testClear(createFilledArrayListModel());
        testClear(createFilledLinkedListModel());
    }


    /**
     * Tests the #remove(int) operation on different observable lists.
     */
    public void testRemoveIndex() {
        testRemoveIndex(createEmptyArrayListModel(),  -1);
        testRemoveIndex(createEmptyLinkedListModel(), -1);
        testRemoveIndex(createEmptyArrayListModel(),   0);
        testRemoveIndex(createEmptyLinkedListModel(),  0);
        testRemoveIndex(createEmptyArrayListModel(),   1);
        testRemoveIndex(createEmptyLinkedListModel(),  1);

        testRemoveIndex(createSingleElementArrayListModel(),  -1);
        testRemoveIndex(createSingleElementLinkedListModel(), -1);
        testRemoveIndex(createSingleElementArrayListModel(),   0);
        testRemoveIndex(createSingleElementLinkedListModel(),  0);
        testRemoveIndex(createSingleElementArrayListModel(),   1);
        testRemoveIndex(createSingleElementLinkedListModel(),  1);

        int size1 = createFilledArrayListModel().size();
        for (int i = -1; i <= size1; i++) {
            testRemoveIndex(createFilledArrayListModel(), i);
        }
        int size2 = createFilledLinkedListModel().size();
        for (int i = -1; i <= size2; i++) {
            testRemoveIndex(createFilledLinkedListModel(), i);
        }
    }


    /**
     * Tests the #remove(Object) operation on different observable lists.
     */
    public void testRemoveObject() {
        testRemoveObject(createEmptyArrayListModel(),  -1);
        testRemoveObject(createEmptyLinkedListModel(), -1);
        testRemoveObject(createEmptyArrayListModel(),   0);
        testRemoveObject(createEmptyLinkedListModel(),  0);
        testRemoveObject(createEmptyArrayListModel(),   1);
        testRemoveObject(createEmptyLinkedListModel(),  1);

        testRemoveObject(createSingleElementArrayListModel(),  -1);
        testRemoveObject(createSingleElementLinkedListModel(), -1);
        testRemoveObject(createSingleElementArrayListModel(),   0);
        testRemoveObject(createSingleElementLinkedListModel(),  0);
        testRemoveObject(createSingleElementArrayListModel(),   1);
        testRemoveObject(createSingleElementLinkedListModel(),  1);

        int size1 = createFilledArrayListModel().size();
        for (int i = -1; i <= size1; i++) {
            testRemoveObject(createFilledArrayListModel(), i);
        }
        int size2 = createFilledLinkedListModel().size();
        for (int i = -1; i <= size2; i++) {
            testRemoveObject(createFilledLinkedListModel(), i);
        }
    }


    /**
     * Tests #removeAll on different combinations of observable lists.
     */
    public void testRemoveAll() {
        testRemoveAll(createEmptyArrayListModel(),  createEmptyArrayListModel());
        testRemoveAll(createEmptyLinkedListModel(), createEmptyArrayListModel());
        testRemoveAll(createEmptyArrayListModel(),  createSingleElementArrayListModel());
        testRemoveAll(createEmptyLinkedListModel(), createSingleElementArrayListModel());
        testRemoveAll(createEmptyArrayListModel(),  createFilledArrayListModel());
        testRemoveAll(createEmptyLinkedListModel(), createFilledArrayListModel());

        testRemoveAll(createSingleElementArrayListModel(),  createEmptyArrayListModel());
        testRemoveAll(createSingleElementLinkedListModel(), createEmptyArrayListModel());
        testRemoveAll(createSingleElementArrayListModel(),  createSingleElementArrayListModel());
        testRemoveAll(createSingleElementLinkedListModel(), createSingleElementArrayListModel());
        testRemoveAll(createSingleElementArrayListModel(),  createFilledArrayListModel());
        testRemoveAll(createSingleElementLinkedListModel(), createFilledArrayListModel());

        testRemoveAll(createFilledArrayListModel(),  createEmptyArrayListModel());
        testRemoveAll(createFilledLinkedListModel(), createEmptyArrayListModel());
        testRemoveAll(createFilledArrayListModel(),  createSingleElementArrayListModel());
        testRemoveAll(createFilledLinkedListModel(), createSingleElementArrayListModel());
        testRemoveAll(createFilledArrayListModel(),  createFilledArrayListModel());
        testRemoveAll(createFilledLinkedListModel(), createFilledLinkedListModel());
    }


    /**
     * Tests #retainAll on different combinations of observable lists.
     */
    public void testRetainAll() {
        testRetainAll(createEmptyArrayListModel(),  createEmptyArrayListModel());
        testRetainAll(createEmptyLinkedListModel(), createEmptyArrayListModel());
        testRetainAll(createEmptyArrayListModel(),  createSingleElementArrayListModel());
        testRetainAll(createEmptyLinkedListModel(), createSingleElementArrayListModel());
        testRetainAll(createEmptyArrayListModel(),  createFilledArrayListModel());
        testRetainAll(createEmptyLinkedListModel(), createFilledArrayListModel());

        testRetainAll(createSingleElementArrayListModel(),  createEmptyArrayListModel());
        testRetainAll(createSingleElementLinkedListModel(), createEmptyArrayListModel());
        testRetainAll(createSingleElementArrayListModel(),  createSingleElementArrayListModel());
        testRetainAll(createSingleElementLinkedListModel(), createSingleElementArrayListModel());
        testRetainAll(createSingleElementArrayListModel(),  createFilledArrayListModel());
        testRetainAll(createSingleElementLinkedListModel(), createFilledArrayListModel());

        testRetainAll(createFilledArrayListModel(),  createEmptyArrayListModel());
        testRetainAll(createFilledLinkedListModel(), createEmptyArrayListModel());
        testRetainAll(createFilledArrayListModel(),  createSingleElementArrayListModel());
        testRetainAll(createFilledLinkedListModel(), createSingleElementArrayListModel());
        testRetainAll(createFilledArrayListModel(),  createFilledArrayListModel());
        testRetainAll(createFilledLinkedListModel(), createFilledLinkedListModel());
    }


    /**
     * Tests the #set operation on different observable lists.
     */
    public void testSet() {
        testSet(createEmptyArrayListModel(), 0);
        testSet(createEmptyLinkedListModel(), 0);

        testSet(createSingleElementArrayListModel(), 0);
        testSet(createSingleElementLinkedListModel(), 0);
        testSet(createSingleElementArrayListModel(), 1);
        testSet(createSingleElementLinkedListModel(), 1);

        int size1 = createFilledArrayListModel().size();
        for (int i = 0; i < size1; i++) {
            testSet(createFilledArrayListModel(), i);
        }
        int size2 = createFilledLinkedListModel().size();
        for (int i = 0; i < size2; i++) {
            testSet(createFilledLinkedListModel(), i);
        }
    }


    /**
     * Tests the list iterator's add operation on different observable lists.
     */
    public void testListIteratorAdd() {
        testListIteratorAdd(createEmptyArrayListModel(),  0, 0);
        testListIteratorAdd(createEmptyLinkedListModel(), 0, 0);

        testListIteratorAdd(createSingleElementArrayListModel(),  0, 0);
        testListIteratorAdd(createSingleElementArrayListModel(),  0, 1);
        testListIteratorAdd(createSingleElementArrayListModel(),  1, 1);
        testListIteratorAdd(createSingleElementLinkedListModel(), 0, 0);
        testListIteratorAdd(createSingleElementLinkedListModel(), 0, 1);
        testListIteratorAdd(createSingleElementLinkedListModel(), 1, 1);

        int size1 = createFilledArrayListModel().size();
        for (int i = 0; i <= size1; i++) {
            for (int j = i; j <= size1; j++) {
                testListIteratorAdd(createFilledArrayListModel(), i, j);
            }
        }
        int size2 = createFilledLinkedListModel().size();
        for (int i = 0; i <= size2; i++) {
            for (int j = i; j <= size2; j++) {
                testListIteratorAdd(createFilledLinkedListModel(), i, j);
            }
        }
    }

    /**
     * Tests the list iterator's remove operation on different observable lists.
     */
    public void testListIteratorRemove() {
        testListIteratorRemove(createEmptyArrayListModel(), 0, 0);
        testListIteratorRemove(createEmptyLinkedListModel(), 0, 0);

        testListIteratorRemove(createSingleElementArrayListModel(),  0, 0);
        testListIteratorRemove(createSingleElementArrayListModel(),  0, 1);
        testListIteratorRemove(createSingleElementArrayListModel(),  1, 1);
        testListIteratorRemove(createSingleElementLinkedListModel(), 0, 0);
        testListIteratorRemove(createSingleElementLinkedListModel(), 0, 1);
        testListIteratorRemove(createSingleElementLinkedListModel(), 1, 1);

        int size1 = createFilledArrayListModel().size();
        for (int i = 0; i < size1; i++) {
            for (int j = i; j < size1; j++) {
                testListIteratorRemove(createFilledArrayListModel(), i, j);
            }
        }
        int size2 = createFilledLinkedListModel().size();
        for (int i = 0; i < size2; i++) {
            for (int j = i; j < size2; j++) {
                testListIteratorRemove(createFilledLinkedListModel(), i, j);
            }
        }
    }


    /**
     * Tests the iterator's remove operation on different observable lists.
     */
    public void testIteratorRemove() {
        testIteratorRemove(createEmptyArrayListModel(), 0);
        testIteratorRemove(createEmptyLinkedListModel(), 0);

        testIteratorRemove(createSingleElementArrayListModel(),  0);
        testIteratorRemove(createSingleElementArrayListModel(),  1);
        testIteratorRemove(createSingleElementLinkedListModel(), 0);
        testIteratorRemove(createSingleElementLinkedListModel(), 1);

        int size1 = createFilledArrayListModel().size();
        for (int i = 0; i < size1; i++) {
            testIteratorRemove(createFilledArrayListModel(), i);
        }
        int size2 = createFilledLinkedListModel().size();
        for (int i = 0; i < size2; i++) {
            testIteratorRemove(createFilledLinkedListModel(), i);
        }
    }


    // Test the Event Listener List ******************************************

    public void testGetListDataListenersWithoutListeners() {
        ListDataListener[] listeners =
            createEmptyArrayListModel().getListDataListeners();
        assertEquals("The list of ArrayListModel's ListDataListeners is empty.",
                0, listeners.length);
        listeners = createEmptyLinkedListModel().getListDataListeners();
        assertEquals("The list of LinkedListModel's ListDataListeners is empty.",
                0, listeners.length);
    }



    // Test Implementations **************************************************

    private void testAdd(ObservableList<String> list) {
        ListDataReport listDataReport = new ListDataReport();
        list.addListDataListener(listDataReport);

        list.add("OneAdded");
        assertEquals("An element has been added.",
                1, listDataReport.eventCountAdd());
        assertEquals("One event has been fired.",
                1, listDataReport.eventCount());

        list.add("TwoAdded");
        assertEquals("Another element has been added.",
                2, listDataReport.eventCountAdd());
        assertEquals("Another event has been fired.",
                2, listDataReport.eventCountAdd());
    }

    private void testAddIndexed(ObservableList<String> list, int index) {
        ListDataReport listDataReport = new ListDataReport();
        list.addListDataListener(listDataReport);

        list.add(index, "OneAdded");
        assertEquals("An element has been added.",
                1, listDataReport.eventCountAdd());
        assertEquals("One event has been fired.",
                1, listDataReport.eventCount());
        assertEquals("Proper index0 (first add).",
                index, listDataReport.lastEvent().getIndex0());
        assertEquals("Proper index1 (first add).",
                index, listDataReport.lastEvent().getIndex1());

        list.add(index, "TwoAdded");
        assertEquals("Another element has been added.",
                2, listDataReport.eventCountAdd());
        assertEquals("Another event has been fired.",
                2, listDataReport.eventCount());
        assertEquals("Proper index0 (second add).",
                index, listDataReport.lastEvent().getIndex0());
        assertEquals("Proper index1 (second add).",
                index, listDataReport.lastEvent().getIndex1());
    }


    private <E> void testAddAll(ObservableList<E> list, List<E> additions) {
        ListDataReport listDataReport = new ListDataReport();
        list.addListDataListener(listDataReport);

        int addCount =  additions.size();
        int eventCount = additions.isEmpty() ? 0 : 1;
        int addIndex0 = list.size();
        int addIndex1 = addIndex0 + addCount - 1;
        list.addAll(additions);
        assertEquals("Elements have been added.",
                eventCount, listDataReport.eventCountAdd());
        assertEquals("One event has been fired.",
                eventCount, listDataReport.eventCount());
        if (!additions.isEmpty()) {
            assertEquals("Proper index0 (first addAll).",
                    addIndex0, listDataReport.lastEvent().getIndex0());
            assertEquals("Proper index1 (first addAll).",
                    addIndex1, listDataReport.lastEvent().getIndex1());
        }

        addIndex0 = list.size();
        addIndex1 = addIndex0 + addCount - 1;
        list.addAll(additions);
        assertEquals("More elements have been added.",
                2 * eventCount, listDataReport.eventCountAdd());
        assertEquals("Another event has been fired.",
                2 * eventCount, listDataReport.eventCountAdd());
        if (!additions.isEmpty()) {
            assertEquals("Proper index0 (second addAll).",
                    addIndex0, listDataReport.lastEvent().getIndex0());
            assertEquals("Proper index1 (second addAll).",
                    addIndex1, listDataReport.lastEvent().getIndex1());
        }
    }

    private <E> void testAddAllIndexed(ObservableList<E> list, List<E> additions, int index) {
        ListDataReport listDataReport = new ListDataReport();
        list.addListDataListener(listDataReport);

        int addCount =  additions.size();
        int eventCount = additions.isEmpty() ? 0 : 1;
        int addIndex0 = index;
        int addIndex1 = addIndex0 + addCount - 1;
        list.addAll(index, additions);
        assertEquals("Elements have been added.",
                eventCount, listDataReport.eventCountAdd());
        assertEquals("One event has been fired.",
                eventCount, listDataReport.eventCount());
        if (!additions.isEmpty()) {
            assertEquals("Proper index0 (first addAll).",
                    addIndex0, listDataReport.lastEvent().getIndex0());
            assertEquals("Proper index1 (first addAll).",
                    addIndex1, listDataReport.lastEvent().getIndex1());
        }

        addIndex0 = index;
        addIndex1 = addIndex0 + addCount - 1;
        list.addAll(index, additions);
        assertEquals("More elements have been added.",
                2 * eventCount, listDataReport.eventCountAdd());
        assertEquals("Another event has been fired.",
                2 * eventCount, listDataReport.eventCountAdd());
        if (!additions.isEmpty()) {
            assertEquals("Proper index0 (second addAll).",
                    addIndex0, listDataReport.lastEvent().getIndex0());
            assertEquals("Proper index1 (second addAll).",
                    addIndex1, listDataReport.lastEvent().getIndex1());
        }
    }


    private <E> void testClear(ObservableList<E> list) {
        ListDataReport listDataReport = new ListDataReport();
        list.addListDataListener(listDataReport);

        int eventCount = list.isEmpty() ? 0 : 1;
        int index0 = 0;
        int index1 = list.size() - 1;
        list.clear();
        assertEquals("All elements have been removed.",
                eventCount, listDataReport.eventCountRemove());
        assertEquals("One event has been fired.",
                eventCount, listDataReport.eventCount());
        if (!list.isEmpty()) {
            assertEquals("Proper index0 (first clear).",
                    index0, listDataReport.lastEvent().getIndex0());
            assertEquals("Proper index1 (first clear).",
                    index1, listDataReport.lastEvent().getIndex1());
        }

        list.clear();
        assertEquals("No further elements have been removed.",
                eventCount, listDataReport.eventCount());
    }


    private <E> void testRemoveIndex(ObservableList<E> list, int index) {
        ListDataReport listDataReport = new ListDataReport();
        list.addListDataListener(listDataReport);

        if (index < 0 || index >= list.size()) {
            try {
                list.remove(index);
                fail("IndexOutOfBoundsException expected when removing index " + index);
            } catch (IndexOutOfBoundsException e) {
                // Do nothing; this is the expected behavior.
            }
        } else {
            list.remove(index);
            assertEquals("An element has been removed.",
                    1, listDataReport.eventCountRemove());
            assertEquals("One event has been fired.",
                    1, listDataReport.eventCount());
            assertEquals("Proper index0 (first remove).",
                    index, listDataReport.lastEvent().getIndex0());
            assertEquals("Proper index1 (first remove).",
                    index, listDataReport.lastEvent().getIndex1());
        }

        if (index < 0 || index >= list.size()) {
            try {
                list.remove(index);
                fail("IndexOutOfBoundsException expected when removing index " + index);
            } catch (IndexOutOfBoundsException e) {
                // Do nothing; this is the expected behavior.
            }
        } else {
            list.remove(index);
            assertEquals("Another element has been removed.",
                    2, listDataReport.eventCountRemove());
            assertEquals("Another event has been fired.",
                    2, listDataReport.eventCount());
            assertEquals("Proper index0 (second remove).",
                    index, listDataReport.lastEvent().getIndex0());
            assertEquals("Proper index1 (second remove).",
                    index, listDataReport.lastEvent().getIndex1());
        }
    }


    private <E> void testRemoveObject(ObservableList<E> list, int index) {
        ListDataReport listDataReport = new ListDataReport();
        list.addListDataListener(listDataReport);

        boolean contained = (index >= 0) && (index < list.size());
        Object object = contained
                ? list.get(index)
                : "AnObjectNotContainedInTheCollection";

        boolean removed = list.remove(object);
        assertEquals("Index != -1 iff the list changed.",
                contained, removed);
        if (removed) {
            assertEquals("An element has been removed.",
                    1, listDataReport.eventCountRemove());
            assertEquals("One event has been fired.",
                    1, listDataReport.eventCount());
            assertEquals("Proper index0 (first remove).",
                    index, listDataReport.lastEvent().getIndex0());
            assertEquals("Proper index1 (first remove).",
                    index, listDataReport.lastEvent().getIndex1());
        }
    }


    private <E> void testRemoveAll(ObservableList<E> list, List<E> removals) {
        ListDataReport listDataReport = new ListDataReport();
        list.addListDataListener(listDataReport);

        List<Integer> indicesToRemove = new LinkedList<Integer>();
        List<E> testRemoveList  = new LinkedList<E>(list);
        for (E e : removals) {
            int index = testRemoveList.indexOf(e);
            if (index != -1) {
                indicesToRemove.add(Integer.valueOf(index));
                testRemoveList.remove(index);
            }
        }

        int oldSize =  list.size();
        list.removeAll(removals);
        int newSize = list.size();
        int removedCount = oldSize - newSize;

        assertEquals("Removed element count and number of removal indices.",
                removedCount, indicesToRemove.size());
        assertEquals("Elements have been removed.",
                removedCount, listDataReport.eventCountRemove());
        assertEquals("One event has been fired.",
                removedCount, listDataReport.eventCount());

        // Check the indices fired by the ObservableList
        Iterator<Integer> indexIterator = indicesToRemove.iterator();
        for (ListDataEvent event : listDataReport.eventList()) {
            int index = indexIterator.next().intValue();
            assertEquals("Proper index0 (removeAll).",
                    index, event.getIndex0());
            assertEquals("Proper index1 (removeAll).",
                    index, event.getIndex1());
        }
    }


    private <E> void testRetainAll(ObservableList<E> list, List<E> retains) {
        ListDataReport listDataReport = new ListDataReport();
        list.addListDataListener(listDataReport);

        int oldSize =  list.size();
        list.retainAll(retains);
        int newSize = list.size();
        int eventCount = oldSize - newSize;

        assertEquals("Elements have been removed.",
                eventCount, listDataReport.eventCountRemove());
        assertEquals("One event has been fired.",
                eventCount, listDataReport.eventCount());
    }


    private void testSet(ObservableList<String> list, int index) {
        ListDataReport listDataReport = new ListDataReport();
        list.addListDataListener(listDataReport);

        if (index >= list.size()) {
            try {
                list.set(index, "newValue1");
                fail("IndexOutOfBoundsException expected when removing index " + index);
            } catch (IndexOutOfBoundsException e) {
                // Do nothing; this is the expected behavior.
            }
        } else {
            list.set(index, "newValue1");
            assertEquals("An element has been changed.",
                    1, listDataReport.eventCountChange());
            assertEquals("One event has been fired.",
                    1, listDataReport.eventCount());
            assertEquals("Proper index0 (first change).",
                    index, listDataReport.lastEvent().getIndex0());
            assertEquals("Proper index1 (first change).",
                    index, listDataReport.lastEvent().getIndex1());
        }

        if (index >= list.size()) {
            try {
                list.set(index, "newValue2");
                fail("IndexOutOfBoundsException expected when removing index " + index);
            } catch (IndexOutOfBoundsException e) {
                // Do nothing; this is the expected behavior.
            }
        } else {
            list.set(index, "newValue2");
            assertEquals("Another element has been changed.",
                    2, listDataReport.eventCountChange());
            assertEquals("Another event has been fired.",
                    2, listDataReport.eventCount());
            assertEquals("Proper index0 (second change).",
                    index, listDataReport.lastEvent().getIndex0());
            assertEquals("Proper index1 (second change).",
                    index, listDataReport.lastEvent().getIndex1());
        }
    }


    private void testListIteratorAdd(ObservableList<String> list, int iteratorIndex, int elementIndex) {
        ListDataReport listDataReport = new ListDataReport();
        list.addListDataListener(listDataReport);

        ListIterator<String> listIterator = list.listIterator(iteratorIndex);
        int relativeElementIndex = elementIndex - iteratorIndex;
        for (int i = 0; i < relativeElementIndex; i++) {
            listIterator.next();
        }
        listIterator.add("Added by ListIterator");

        assertEquals("An element has been added.",
                1, listDataReport.eventCountAdd());
        assertEquals("One event has been fired.",
                1, listDataReport.eventCount());
        assertEquals("Proper index0.",
                elementIndex, listDataReport.lastEvent().getIndex0());
        assertEquals("Proper index1.",
                elementIndex, listDataReport.lastEvent().getIndex1());
    }


    private <E> void testListIteratorRemove(
            ObservableList<E> list,
            int iteratorIndex,
            int elementIndex) {
        ListDataReport listDataReport = new ListDataReport();
        list.addListDataListener(listDataReport);

        ListIterator<E> listIterator = list.listIterator(iteratorIndex);
        int relativeElementIndex = elementIndex - iteratorIndex;
        for (int i = 0; i <= relativeElementIndex && listIterator.hasNext(); i++) {
            listIterator.next();
        }
        if (elementIndex < list.size()) {
            listIterator.remove();
            assertEquals("An element has been removed.",
                    1, listDataReport.eventCountRemove());
            assertEquals("One event has been fired.",
                    1, listDataReport.eventCount());
            assertEquals("Proper index0.",
                    elementIndex, listDataReport.lastEvent().getIndex0());
            assertEquals("Proper index1.",
                    elementIndex, listDataReport.lastEvent().getIndex1());
        }
    }


    private <E> void testIteratorRemove(
            ObservableList<E> list,
            int elementIndex) {
        ListDataReport listDataReport = new ListDataReport();
        list.addListDataListener(listDataReport);

        Iterator<E> iterator = list.iterator();
        for (int i = 0; i <= elementIndex && iterator.hasNext(); i++) {
            iterator.next();
        }
        if (elementIndex < list.size()) {
            iterator.remove();
            assertEquals("An element has been removed.",
                    1, listDataReport.eventCountRemove());
            assertEquals("One event has been fired.",
                    1, listDataReport.eventCount());
            assertEquals("Proper index0.",
                    elementIndex, listDataReport.lastEvent().getIndex0());
            assertEquals("Proper index1.",
                    elementIndex, listDataReport.lastEvent().getIndex1());
        }
    }

    // Creating Observable Lists *********************************************

    private ArrayListModel<String> createEmptyArrayListModel() {
        return new ArrayListModel<String>();
    }

    private LinkedListModel<String> createEmptyLinkedListModel() {
        return new LinkedListModel<String>();
    }

    private ArrayListModel<String> createSingleElementArrayListModel() {
        ArrayListModel<String> list = new ArrayListModel<String>();
        list.add("One");
        return list;
    }

    private LinkedListModel<String> createSingleElementLinkedListModel() {
        LinkedListModel<String> list = new LinkedListModel<String>();
        list.add("One");
        return list;
    }

    private ArrayListModel<String> createFilledArrayListModel() {
        ArrayListModel<String> list = new ArrayListModel<String>();
        list.add("One");
        list.add("Two");
        list.add("Three");
        list.add("Four");
        list.add("Five");
        list.add("Six");
        return list;
    }

    private LinkedListModel<String> createFilledLinkedListModel() {
        LinkedListModel<String> list = new LinkedListModel<String>();
        list.add("One");
        list.add("Two");
        list.add("Three");
        list.add("Four");
        list.add("Five");
        list.add("Six");
        return list;
    }


}
