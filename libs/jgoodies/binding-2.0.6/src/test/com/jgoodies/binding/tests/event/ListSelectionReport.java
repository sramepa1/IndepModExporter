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

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A ListDataListener that stores the received ListDataEvents.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.9 $
 */
public final class ListSelectionReport implements ListSelectionListener {

    /**
     * Holds a list of all received ListSelectionEvent.
     */
    private final List<ListSelectionEvent> allEvents = new LinkedList<ListSelectionEvent>();


    // Implementing the ListSelectionListener Interface ***************************

    /**
     * Called whenever the value of the selection changes.
     *
     * @param evt the event that characterizes the change.
     */
    public void valueChanged(ListSelectionEvent evt) {
        allEvents.add(0, evt);
    }


    // Public Report API *****************************************************

    public ListSelectionEvent lastEvent() {
        return allEvents.isEmpty()
            ? null
            : allEvents.get(0);
    }

    public List<ListSelectionEvent> eventList() {
        return allEvents;
    }

    public int eventCount() {
        return allEvents.size();
    }

    public boolean hasEvents() {
        return !allEvents.isEmpty();
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
        if (!(o instanceof ListSelectionReport))
            return false;

        ListIterator<ListSelectionEvent> e1 = eventList().listIterator();
        ListIterator<ListSelectionEvent> e2 = ((ListSelectionReport) o).eventList().listIterator();
        while (e1.hasNext() && e2.hasNext()) {
            ListSelectionEvent evt1 = e1.next();
            ListSelectionEvent evt2 = e2.next();
            if (!equalListDataEvents(evt1, evt2))
                return false;
        }
        return !(e1.hasNext() || e2.hasNext());
    }

    /**
     * Poor but valid implementation. Won't be used.
     *
     * @return this report's hash code
     */
    @Override
    public int hashCode() {
        return eventList().size();
    }


    /**
     * Checks and answers whether the two given ListSelectionEvents have
     * the same first and last index. The source and adjusting state may differ.
     */
    private boolean equalListDataEvents(ListSelectionEvent evt1, ListSelectionEvent evt2) {
        return evt1.getFirstIndex() == evt2.getFirstIndex()
             && evt1.getLastIndex() == evt2.getLastIndex();
    }


}
