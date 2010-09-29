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

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import junit.framework.Assert;

/**
 * Compares the size reported by the ListDataEvents with the ListModel size.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.5 $
 */
public final class ListSizeConstraintChecker implements ListDataListener {

    private int size;

    public ListSizeConstraintChecker(int initialSize) {
        this.size = initialSize;
    }

    public void contentsChanged(ListDataEvent e) {
        assertSize(e);
    }

    public void intervalAdded(ListDataEvent e) {
        int addedElements = e.getIndex1() - e.getIndex0() + 1;
        size += addedElements;
        assertSize(e);
    }

    public void intervalRemoved(ListDataEvent e) {
        int removedElements = e.getIndex1() - e.getIndex0() + 1;
        size -= removedElements;
        assertSize(e);
    }

    private void assertSize(ListDataEvent e) {
        Assert.assertEquals(
                "The ListModel size equals the size reported by ListDataEvents.",
                ((ListModel) e.getSource()).getSize(),
                size);
    }

}
