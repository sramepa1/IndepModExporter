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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A PropertyChangeListener that stores the received PropertyChangeEvents.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.9 $
 * @see     PropertyChangeListener
 */
public final class PropertyChangeReport
    extends AbstractChangeReport<PropertyChangeEvent>
    implements PropertyChangeListener {

    /**
     * The observed property has been changed.
     * Adds the event to the list of stored events.
     *
     * @param evt   the property change event to be handled
     */
    public void propertyChange(PropertyChangeEvent evt) {
        addEvent(evt);
        if (isLogActive()) {
            boolean same = evt.getOldValue() == evt.getNewValue();
            System.out.println();
            System.out.println("Source:" + evt.getSource());
            System.out.println("Property:" + evt.getPropertyName());
            System.out.println("Old value:" + evt.getOldValue());
            System.out.println("New value:" + evt.getNewValue());
            System.out.println("Old==new: " + same);
        }
    }

    public int multicastEventCount() {
        int count = 0;
        for (PropertyChangeEvent event : events) {
            if (event.getPropertyName() == null)
                count++;
        }
        return 0;
    }

    public int namedEventCount() {
        return eventCount() - multicastEventCount();
    }

    public Object lastOldValue() {
        return getLastEvent().getOldValue();
    }

    public Object lastNewValue() {
        return getLastEvent().getNewValue();
    }

    public boolean lastOldBooleanValue() {
        return ((Boolean) lastOldValue()).booleanValue();
    }

    public boolean lastNewBooleanValue() {
        return ((Boolean) lastNewValue()).booleanValue();
    }

}
