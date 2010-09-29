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

package com.jgoodies.binding.tests.event;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * A ListDataListener that stores the received ListDataEvents.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.10 $
 */
public final class ListDataReport implements ListDataListener {

    /**
     * Holds a list of all received ListDataEvents.
     */
    private final List<ListDataEvent> allEvents = new LinkedList<ListDataEvent>();

    /**
     * Holds a list of all received add ListDataEvents.
     */
    private final List<ListDataEvent> addEvents = new LinkedList<ListDataEvent>();

    /**
     * Holds a list of all received remove ListDataEvents.
     */
    private final List<ListDataEvent> removeEvents = new LinkedList<ListDataEvent>();

    /**
     * Holds a list of all received change ListDataEvents.
     */
    private final List<ListDataEvent> changeEvents = new LinkedList<ListDataEvent>();


    // Implementing the ListDataListener Interface ***************************

    /**
     * Sent after the indices in the index0,index1
     * interval have been inserted in the data model.
     * The new interval includes both index0 and index1.
     *
     * @param evt  a <code>ListDataEvent</code> encapsulating the
     *    event information
     */
    public void intervalAdded(ListDataEvent evt) {
        allEvents.add(0, evt);
        addEvents.add(0, evt);
    }


    /**
     * Sent after the indices in the index0,index1 interval
     * have been removed from the data model.  The interval
     * includes both index0 and index1.
     *
     * @param evt  a <code>ListDataEvent</code> encapsulating the
     *    event information
     */
    public void intervalRemoved(ListDataEvent evt) {
        allEvents.add(0, evt);
        removeEvents.add(0, evt);
    }


    /**
     * Sent when the contents of the list has changed in a way
     * that's too complex to characterize with the previous
     * methods. For example, this is sent when an item has been
     * replaced. Index0 and index1 bracket the change.
     *
     * @param evt  a <code>ListDataEvent</code> encapsulating the
     *    event information
     */
    public void contentsChanged(ListDataEvent evt) {
        allEvents.add(0, evt);
        changeEvents.add(0, evt);
    }


    // Public Report API *****************************************************

    private ListDataEvent getEvent(int index) {
        return allEvents.get(index);
    }

    public ListDataEvent lastEvent() {
        return eventCount() > 0
            ? getEvent(0)
            : null;
    }

    public ListDataEvent previousEvent() {
        return eventCount() > 1
            ? getEvent(1)
            : null;
    }

    public List<ListDataEvent> eventList() {
        return allEvents;
    }

    public int eventCount() {
        return allEvents.size();
    }

    public int eventCountAdd() {
        return addEvents.size();
    }

    public int eventCountRemove() {
        return removeEvents.size();
    }

    public int eventCountChange() {
        return changeEvents.size();
    }

    public boolean hasEvents() {
        return !allEvents.isEmpty();
    }

    public void clearEventList() {
       allEvents.clear();
    }


    // ************************************************************************

    /**
     * Compares this report's event list with the event list
     * of the given object and ignores the source of the contained
     * ListDataEvents that may differ.
     *
     * @param o   the object to compare with
     * @return true if equal, false if not
     *
     * @see java.util.AbstractList#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ListDataReport))
            return false;

        ListIterator<ListDataEvent> e1 = eventList().listIterator();
        ListIterator<ListDataEvent> e2 = ((ListDataReport) o).eventList().listIterator();
        while (e1.hasNext() && e2.hasNext()) {
            ListDataEvent evt1 = e1.next();
            ListDataEvent evt2 = e2.next();
            if (!equalListDataEvents(evt1, evt2))
                return false;
        }
        return !(e1.hasNext() || e2.hasNext());
    }

    /**
     * Poor but valid implementation. Won't be used.
     *
     * @return this reports hash code
     */
    @Override
    public int hashCode() {
        return eventList().size();
    }


    /**
     * Checks and answers whether the two given ListDataEvents
     * have the same type and indices. The source may differ.
     */
    private boolean equalListDataEvents(ListDataEvent evt1, ListDataEvent evt2) {
        return evt1.getType() == evt2.getType()
             && evt1.getIndex0() == evt2.getIndex0()
             && evt1.getIndex1() == evt2.getIndex1();
    }


}
