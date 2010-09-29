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

package com.jgoodies.binding.list;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;


/**
 * Represents a selection in a list of objects. Provides bound bean properties
 * for the list, the selection, the selection index, and the selection empty
 * state. The SelectionInList implements ValueModel with the selection as value.
 * Selection changes fire an event only if the old and new value are not equal.
 * If you need to compare the identity you can use and observe the selection
 * index instead of the selection or value.<p>
 *
 * The SelectionInList uses three ValueModels to hold the list, the selection
 * and selection index and provides bound bean properties for these models.
 * You can access, observe and replace these ValueModels. This is useful
 * to connect a SelectionInList with other ValueModels; for example you can
 * use the SelectionInList's selection holder as bean channel for a
 * PresentationModel. Since the SelectionInList is a ValueModel, it is often
 * used as bean channel. See the Binding tutorial classes for examples on how
 * to connect a SelectionInList with a PresentationModel.<p>
 *
 * This class also implements the {@link ListModel} interface that allows
 * API users to observe fine grained changes in the structure and contents
 * of the list. Hence instances of this class can be used directly as model of
 * a JList. If you want to use a SelectionInList with a JComboBox or JTable,
 * you can convert the SelectionInList to the associated component model
 * interfaces using the adapter classes
 * {@link com.jgoodies.binding.adapter.ComboBoxAdapter}
 * and {@link com.jgoodies.binding.adapter.AbstractTableAdapter} respectively.
 * These classes are part of the Binding library too.<p>
 *
 * The SelectionInList supports two list types as content of its list holder:
 * <code>List</code> and <code>ListModel</code>. The two modes differ in how
 * precise this class can fire events about changes to the content and structure
 * of the list. If you use a List, this class can only report
 * that the list changes completely; this is done by firing
 * a PropertyChangeEvent for the <em>list</em> property.
 * Also, a <code>ListDataEvent</code> is fired that reports a complete change.
 * In contrast, if you use a ListModel it will report the same
 * PropertyChangeEvent. But fine grained changes in the list
 * model will be fired by this class to notify observes about changes in
 * the content, added and removed elements.<p>
 *
 * If the list content doesn't change at all, or if it always changes
 * completely, you can work well with both List content and ListModel content.
 * But if the list structure or content changes, the ListModel reports more
 * fine grained events to registered ListDataListeners, which in turn allows
 * list views to chooser better user interface gestures: for example, a table
 * with scroll pane may retain the current selection and scroll offset.<p>
 *
 * An example for using a ListModel in a SelectionInList is the asynchronous
 * transport of list elements from a server to a client. Let's say you transport
 * the list elements in portions of 10 elements to improve the application's
 * responsiveness. The user can then select and work with the SelectionInList
 * as soon as the ListModel gets populated. If at a later time more elements
 * are added to the list model, the SelectionInList can retain the selection
 * index (and selection) and will just report a ListDataEvent about
 * the interval added. JList, JTable and JComboBox will then just add
 * the new elements at the end of the list presentation.<p>
 *
 * If you want to combine List operations and the ListModel change reports,
 * you may consider using an implementation that combines these two interfaces,
 * for example {@link ArrayListModel} or {@link LinkedListModel}.<p>
 *
 * <strong>Important Note:</strong> If you change the ListModel instance,
 * either by calling <code>#setListModel(ListModel)</code> or by setting
 * a new value to the underlying list holder, you must ensure that
 * the list holder throws a PropertyChangeEvent whenever the instance changes.
 * This event is used to remove a ListDataListener from the old ListModel
 * instance and is later used to add it to the new ListModel instance.
 * It is easy to violate this constraint, just because Java's standard
 * PropertyChangeSupport helper class that is used by many beans, checks
 * a changed property value via <code>#equals</code>, not <code>==</code>.
 * For example, if you change the SelectionInList's list model from an empty
 * list <code>L1</code> to another empty list instance <code>L2</code>,
 * the PropertyChangeSupport won't generate a PropertyChangeEvent,
 * and so, the SelectionInList won't know about the change, which
 * may lead to unexpected behavior.<p>
 *
 * This binding library provides some help for firing PropertyChangeEvents
 * if the old ListModel and new ListModel are equal but not the same.
 * Class {@link com.jgoodies.binding.beans.ExtendedPropertyChangeSupport}
 * allows to permanently or individually check the identity (using
 * <code>==</code>) instead of checking the equity (using <code>#equals</code>).
 * Class {@link com.jgoodies.binding.beans.Model} uses this extended
 * property change support. And class {@link ValueHolder} uses it too
 * and can be configured to always test the identity.<p>
 *
 * Since version 1.0.2 this class provides public convenience methods
 * for firing ListDataEvents, see the methods <code>#fireContentsChanged</code>,
 * <code>#fireIntervalAdded</code>, and <code>#fireIntervalRemoved</code>.
 * These are automatically invoked if the list holder holds a ListModel
 * that fires these events. If on the other hand the underlying List or
 * ListModel does not fire a required ListDataEvent, you can use these
 * methods to notify presentations about a change. It is recommended
 * to avoid sending duplicate ListDataEvents; hence check if the underlying
 * ListModel fires the necessary events or not. Typically an underlying
 * ListModel will fire the add and remove events; but often it'll lack
 * an event if the (selected) contents has changed. A convenient way to
 * indicate that change is <code>#fireSelectedContentsChanged</code>. See
 * the tutorial's AlbumManagerModel for an example how to use this feature.<p>
 *
 * The SelectionInList is partially defined for Lists and ListModels
 * that contain {@code null}. Setting the selection to {@code null}
 * on a SelectionInList that contains {@code null} won't set the selection index
 * to the index of the first {@code null} element. For details see the
 * {@link #setSelection(Object)} JavaDocs. This is because the current
 * implementation interprets a {@code null} selection as <em>unspecified</em>,
 * which maps better to a cleared selection than to a concrete selection index.
 * Anyway, as long as you work with the selection index and selection index
 * holder, such a SelectionInList will work fine. This is the case if you bind
 * a SelectionInList to a JList or JTable. Binding such a SelectionInList
 * to a JComboBox won't synchronize the selection index if {@code null}
 * is selected.<p>
 *
 * <strong>Constraints:</strong> The list holder holds instances of {@link List}
 * or {@link ListModel}, the selection holder values of type {@code E}
 * and the selection index holder of type {@code Integer}. The selection
 * index holder must hold non-null index values; however, when firing
 * an index value change event, both the old and new value may be null.
 * If the ListModel changes, the underlying ValueModel must fire
 * a PropertyChangeEvent.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.38 $
 *
 * @see     ValueModel
 * @see     List
 * @see     ListModel
 * @see     com.jgoodies.binding.adapter.ComboBoxAdapter
 * @see     com.jgoodies.binding.adapter.AbstractTableAdapter
 * @see     com.jgoodies.binding.beans.ExtendedPropertyChangeSupport
 * @see     com.jgoodies.binding.beans.Model
 * @see     com.jgoodies.binding.value.ValueHolder
 *
 * @param <E>  the type of the list elements and the selection
 */
public final class SelectionInList<E> extends IndirectListModel<E>
    implements ValueModel {


    // Constant Names for Bound Properties ************************************

    /**
     * The name of the bound read-write <em>selection</em> property.
     */
    public static final String PROPERTYNAME_SELECTION = "selection";

    /**
     * The name of the bound read-only <em>selectionEmpty</em> property.
     */
    public static final String PROPERTYNAME_SELECTION_EMPTY = "selectionEmpty";

    /**
     * The name of the bound read-write <em>selection holder</em> property.
     */
    public static final String PROPERTYNAME_SELECTION_HOLDER = "selectionHolder";

    /**
     * The name of the bound read-write <em>selectionIndex</em> property.
     */
    public static final String PROPERTYNAME_SELECTION_INDEX = "selectionIndex";

    /**
     * The name of the bound read-write <em>selection index holder</em> property.
     */
    public static final String PROPERTYNAME_SELECTION_INDEX_HOLDER = "selectionIndexHolder";

    /**
     * The name of the bound read-write <em>value</em> property.
     */
    public static final String PROPERTYNAME_VALUE = "value";


    // ************************************************************************

    /**
     * A special index that indicates that we have no selection.
     */
    private static final int NO_SELECTION_INDEX = -1;


    // Instance Fields ********************************************************

    /**
     * Holds the selection, an instance of <code>Object</code>.
     */
    private ValueModel selectionHolder;

    /**
     * Holds the selection index, an <code>Integer</code>.
     */
    private ValueModel selectionIndexHolder;

    /**
     * The <code>PropertyChangeListener</code> used to handle
     * changes of the selection.
     */
    private final PropertyChangeListener selectionChangeHandler;

    /**
     * The <code>PropertyChangeListener</code> used to handle
     * changes of the selection index.
     */
    private final PropertyChangeListener selectionIndexChangeHandler;

    /**
     * Duplicates the value of the selectionHolder.
     * Used to provide better old values in PropertyChangeEvents
     * fired after selectionIndex changes.
     */
    private E oldSelection;

    /**
     * Duplicates the value of the selectionIndexHolder.
     * Used to provide better old values in PropertyChangeEvents
     * fired after selectionIndex changes and selection changes.
     */
    private int oldSelectionIndex;


    // Instance creation ****************************************************

    /**
     * Constructs a SelectionInList with an empty initial
     * <code>ArrayListModel</code> using defaults for the selection holder
     * and selection index holder.
     */
    public SelectionInList() {
        this((ListModel) new ArrayListModel<E>());
    }


    /**
     * Constructs a SelectionInList on the given item array
     * using defaults for the selection holder and selection index holder.
     * The specified array will be converted to a List.<p>
     *
     * Changes to the list "write through" to the array, and changes
     * to the array contents will be reflected in the list.
     *
     * @param listItems        the array of initial items
     *
     * @throws NullPointerException if <code>listItems</code> is {@code null}
     */
    public SelectionInList(E[] listItems) {
        this(Arrays.asList(listItems));
    }


    /**
     * Constructs a SelectionInList on the given item array and
     * selection holder using a default selection index holder.
     * The specified array will be converted to a List.<p>
     *
     * Changes to the list "write through" to the array, and changes
     * to the array contents will be reflected in the list.
     *
     * @param listItems        the array of initial items
     * @param selectionHolder  holds the selection
     *
     * @throws NullPointerException if <code>listItems</code> or
     *     <code>selectionHolder</code> is {@code null}
     */
    public SelectionInList(E[] listItems, ValueModel selectionHolder) {
        this(Arrays.asList(listItems), selectionHolder);
    }


    /**
     * Constructs a SelectionInList on the given item array and
     * selection holder using a default selection index holder.
     * The specified array will be converted to a List.<p>
     *
     * Changes to the list "write through" to the array, and changes
     * to the array contents will be reflected in the list.
     *
     * @param listItems        the array of initial items
     * @param selectionHolder  holds the selection
     * @param selectionIndexHolder  holds the selection index
     *
     * @throws NullPointerException if <code>listItems</code>,
     *     <code>selectionHolder</code>, or <code>selectionIndexHolder</code>
     *     is {@code null}
     */
    public SelectionInList(
            E[] listItems,
            ValueModel selectionHolder,
            ValueModel selectionIndexHolder) {
        this(Arrays.asList(listItems), selectionHolder, selectionIndexHolder);
    }


    /**
     * Constructs a SelectionInList on the given list
     * using defaults for the selection holder and selection index holder.<p>
     *
     * <strong>Note:</strong> Favor <code>ListModel</code> over
     * <code>List</code> when working with the SelectionInList.
     * Why? The SelectionInList can work with both types. What's the
     * difference? ListModel provides all list access features
     * required by the SelectionInList's. In addition it reports more
     * fine grained change events, instances of <code>ListDataEvents</code>.
     * In contrast developer often create Lists and operate on them
     * and the ListModel may be inconvenient for these operations.<p>
     *
     * A convenient solution for this situation is to use the
     * <code>ArrayListModel</code> and <code>LinkedListModel</code> classes.
     * These implement both List and ListModel, offer the standard List
     * operations and report the fine grained ListDataEvents.
     *
     * @param list        the initial list
     */
    public SelectionInList(List<E> list) {
        this(new ValueHolder(list, true));
    }


    /**
     * Constructs a SelectionInList on the given list and
     * selection holder using a default selection index holder.<p>
     *
     * <strong>Note:</strong> Favor <code>ListModel</code> over
     * <code>List</code> when working with the SelectionInList.
     * Why? The SelectionInList can work with both types. What's the
     * difference? ListModel provides all list access features
     * required by the SelectionInList's. In addition it reports more
     * fine grained change events, instances of <code>ListDataEvents</code>.
     * In contrast developer often create Lists and operate on them
     * and the ListModel may be inconvenient for these operations.<p>
     *
     * A convenient solution for this situation is to use the
     * <code>ArrayListModel</code> and <code>LinkedListModel</code> classes.
     * These implement both List and ListModel, offer the standard List
     * operations and report the fine grained ListDataEvents.
     *
     * @param list             the initial list
     * @param selectionHolder  holds the selection
     *
     * @throws NullPointerException
     *     if <code>selectionHolder</code> is {@code null}
     */
    public SelectionInList(List<E> list, ValueModel selectionHolder) {
        this(new ValueHolder(list, true), selectionHolder);
    }


    /**
     * Constructs a SelectionInList on the given list,
     * selection holder, and selection index holder.<p>
     *
     * <strong>Note:</strong> Favor <code>ListModel</code> over
     * <code>List</code> when working with the SelectionInList.
     * Why? The SelectionInList can work with both types. What's the
     * difference? ListModel provides all list access features
     * required by the SelectionInList's. In addition it reports more
     * fine grained change events, instances of <code>ListDataEvents</code>.
     * In contrast developer often create Lists and operate on them
     * and the ListModel may be inconvenient for these operations.<p>
     *
     * A convenient solution for this situation is to use the
     * <code>ArrayListModel</code> and <code>LinkedListModel</code> classes.
     * These implement both List and ListModel, offer the standard List
     * operations and report the fine grained ListDataEvents.
     *
     * @param list                  the initial list
     * @param selectionHolder       holds the selection
     * @param selectionIndexHolder  holds the selection index
     *
     * @throws NullPointerException if <code>selectionHolder</code>,
     *     or <code>selectionIndexHolder</code> is {@code null}
     */
    public SelectionInList(
            List<E> list,
            ValueModel selectionHolder,
            ValueModel selectionIndexHolder) {
        this(new ValueHolder(list, true),
             selectionHolder,
             selectionIndexHolder);
    }


    /**
     * Constructs a SelectionInList on the given list model
     * using defaults for the selection holder and selection index holder.
     *
     * @param listModel        the initial list model
     */
    public SelectionInList(ListModel listModel) {
        this(new ValueHolder(listModel, true));
    }


    /**
     * Constructs a SelectionInList on the given list model
     * and selection holder using a default selection index holder.
     *
     * @param listModel        the initial list model
     * @param selectionHolder  holds the selection
     *
     * @throws NullPointerException
     *     if <code>selectionHolder</code> is {@code null}
     */
    public SelectionInList(ListModel listModel, ValueModel selectionHolder) {
        this(new ValueHolder(listModel, true), selectionHolder);
    }


    /**
     * Constructs a SelectionInList on the given list model,
     * selection holder, and selection index holder.
     *
     * @param listModel             the initial list model
     * @param selectionHolder       holds the selection
     * @param selectionIndexHolder  holds the selection index
     *
     * @throws NullPointerException if <code>selectionHolder</code>,
     *     or <code>selectionIndexHolder</code> is {@code null}
     */
    public SelectionInList(
            ListModel listModel,
            ValueModel selectionHolder,
            ValueModel selectionIndexHolder) {
        this(new ValueHolder(listModel, true),
             selectionHolder,
             selectionIndexHolder);
    }


    /**
     * Constructs a SelectionInList on the given list holder
     * using defaults for the selection holder and selection index holder.<p>
     *
     * <strong>Constraints:</strong>
     * 1) The listHolder must hold instances of List or ListModel and
     * 2) must report a value change whenever the value's identity changes.
     * Note that many bean properties don't fire a PropertyChangeEvent
     * if the old and new value are equal - and so would break this constraint.
     * If you provide a ValueHolder, enable its identityCheck feature
     * during construction. If you provide an adapted bean property from
     * a bean that extends the JGoodies <code>Model</code> class,
     * you can enable the identity check feature in the methods
     * <code>#firePropertyChange</code> by setting the trailing boolean
     * parameter to {@code true}.
     *
     * @param listHolder          holds the list or list model
     *
     * @throws NullPointerException
     *     if <code>listHolder</code> is {@code null}
     */
    public SelectionInList(ValueModel listHolder) {
        this(listHolder, new ValueHolder(null, true));
    }


    /**
     * Constructs a SelectionInList on the given list holder,
     * selection holder and selection index holder.<p>
     *
     * <strong>Constraints:</strong>
     * 1) The listHolder must hold instances of List or ListModel and
     * 2) must report a value change whenever the value's identity changes.
     * Note that many bean properties don't fire a PropertyChangeEvent
     * if the old and new value are equal - and so would break this constraint.
     * If you provide a ValueHolder, enable its identityCheck feature
     * during construction. If you provide an adapted bean property from
     * a bean that extends the JGoodies <code>Model</code> class,
     * you can enable the identity check feature in the methods
     * <code>#firePropertyChange</code> by setting the trailing boolean
     * parameter to {@code true}.
     *
     * @param listHolder             holds the list or list model
     * @param selectionHolder        holds the selection
     * @throws NullPointerException  if <code>listHolder</code>
     *     or <code>selectionHolder</code> is {@code null}
     */
    public SelectionInList(ValueModel listHolder, ValueModel selectionHolder) {
        this(
            listHolder,
            selectionHolder,
            new ValueHolder(Integer.valueOf(NO_SELECTION_INDEX)));
    }


    /**
     * Constructs a SelectionInList on the given list holder,
     * selection holder and selection index holder.<p>
     *
     * <strong>Constraints:</strong>
     * 1) The listHolder must hold instances of List or ListModel and
     * 2) must report a value change whenever the value's identity changes.
     * Note that many bean properties don't fire a PropertyChangeEvent
     * if the old and new value are equal - and so would break this constraint.
     * If you provide a ValueHolder, enable its identityCheck feature
     * during construction. If you provide an adapted bean property from
     * a bean that extends the JGoodies <code>Model</code> class,
     * you can enable the identity check feature in the methods
     * <code>#firePropertyChange</code> by setting the trailing boolean
     * parameter to {@code true}.
     *
     * @param listHolder               holds the list or list model
     * @param selectionHolder          holds the selection
     * @param selectionIndexHolder     holds the selection index
     *
     * @throws NullPointerException    if the <code>listModelHolder</code>,
     *     <code>selectionHolder</code>, or <code>selectionIndexHolder</code>
     *     is {@code null}
     * @throws IllegalArgumentException if the listHolder is a ValueHolder
     *     that doesn't check the identity when changing its value
     * @throws ClassCastException if the listModelHolder contents
     *     is neither a List nor a ListModel
     */
    public SelectionInList(
        ValueModel listHolder,
        ValueModel selectionHolder,
        ValueModel selectionIndexHolder) {
        super(listHolder);
        if (selectionHolder == null)
            throw new NullPointerException("The selection holder must not be null.");
        if (selectionIndexHolder == null)
            throw new NullPointerException("The selection index holder must not be null.");

        selectionChangeHandler      = new SelectionChangeHandler();
        selectionIndexChangeHandler = new SelectionIndexChangeHandler();

        this.selectionHolder = selectionHolder;
        this.selectionIndexHolder = selectionIndexHolder;
        initializeSelectionIndex();

        this.selectionHolder.addValueChangeListener(selectionChangeHandler);
        this.selectionIndexHolder.addValueChangeListener(selectionIndexChangeHandler);
    }


    // ListModel Helper Code **************************************************

    /**
     * Notifies all registered ListDataListeners that the contents
     * of the selected list item - if any - has changed.
     * Useful to update a presentation after editing the selection.
     * See the tutorial's AlbumManagerModel for an example how to use
     * this feature.<p>
     *
     * If the list holder holds a ListModel, this SelectionInList listens
     * to ListDataEvents fired by that ListModel, and forwards these events
     * by invoking the associated <code>#fireXXX</code> method, which in turn
     * notifies all registered ListDataListeners. Therefore if you fire
     * ListDataEvents in an underlying ListModel, you don't need this method
     * and should not use it to avoid sending duplicate ListDataEvents.
     *
     * @see ListModel
     * @see ListDataListener
     * @see ListDataEvent
     *
     * @since 1.0.2
     */
    public void fireSelectedContentsChanged() {
        if (hasSelection()) {
            int selectionIndex = getSelectionIndex();
            fireContentsChanged(selectionIndex, selectionIndex);
        }
    }


    // Accessing the List, Selection and Index ********************************

    /**
     * Looks up and returns the current selection using
     * the current selection index. Returns {@code null} if
     * no object is selected or if the list has no elements.
     *
     * @return the current selection, {@code null} if none is selected
     */
    public E getSelection() {
        return getSafeElementAt(getSelectionIndex());
    }


    /**
     * Sets the selection index to the index of the first list element
     * that equals {@code newSelection}. If {@code newSelection}
     * is {@code null}, it is interpreted as <em>unspecified</em>
     * and the selection index is set to -1, and this SelectionInList
     * has no selection. Does nothing if the list is empty or {@code null}.
     *
     * @param newSelection   the object to be set as new selection,
     *     or {@code null} to set the selection index to -1
     */
    public void setSelection(E newSelection) {
        if (!isEmpty()) {
            setSelectionIndex(indexOf(newSelection));
        }
    }


    /**
     * Checks and answers if an element is selected.
     *
     * @return true if an element is selected, false otherwise
     */
    public boolean hasSelection() {
        return getSelectionIndex() != NO_SELECTION_INDEX;
    }


    /**
     * Checks and answers whether the selection is empty or not.
     * Unlike #hasSelection, the underlying property #selectionEmpty
     * for this method is bound. I.e. you can observe this property
     * using a PropertyChangeListener to update UI state.
     *
     * @return true if nothing is selected, false if there's a selection
     * @see #clearSelection
     * @see #hasSelection
     */
    public boolean isSelectionEmpty() {
        return !hasSelection();
    }


    /**
     * Clears the selection of this SelectionInList - if any.
     */
    public void clearSelection() {
        setSelectionIndex(NO_SELECTION_INDEX);
    }


    /**
     * Returns the selection index.
     *
     * @return the selection index
     *
     * @throws NullPointerException if the selection index holder
     *     has a null Object set
     */
    public int getSelectionIndex() {
        return ((Integer) getSelectionIndexHolder().getValue()).intValue();
    }


    /**
     * Sets a new selection index. Does nothing if it is the same as before.
     *
     * @param newSelectionIndex   the selection index to be set
     * @throws IndexOutOfBoundsException if the new selection index
     *    is outside the bounds of the list
     */
    public void setSelectionIndex(int newSelectionIndex) {
        int upperBound = getSize() - 1;
        if (newSelectionIndex < NO_SELECTION_INDEX || newSelectionIndex > upperBound)
            throw new IndexOutOfBoundsException(
                    "The selection index " + newSelectionIndex + " must be in [-1, " + upperBound + "]");

        oldSelectionIndex = getSelectionIndex();
        if (oldSelectionIndex == newSelectionIndex)
            return;

        getSelectionIndexHolder().setValue(Integer.valueOf(newSelectionIndex));
    }


    // Accessing the Holders for: List, Selection and Index *******************

    /**
     * Returns the selection holder.
     *
     * @return the selection holder
     */
    public ValueModel getSelectionHolder() {
        return selectionHolder;
    }


    /**
     * Sets a new selection holder.
     * Does nothing if the new is the same as before.
     * The selection remains unchanged and is still driven
     * by the selection index holder. It's just that future
     * index changes will update the new selection holder
     * and that future selection holder changes affect the
     * selection index.
     *
     * @param newSelectionHolder   the selection holder to set
     *
     * @throws NullPointerException if the new selection holder is null
     */
    public void setSelectionHolder(ValueModel newSelectionHolder) {
        if (newSelectionHolder == null)
            throw new NullPointerException("The new selection holder must not be null.");

        ValueModel oldSelectionHolder = getSelectionHolder();
        oldSelectionHolder.removeValueChangeListener(selectionChangeHandler);
        selectionHolder = newSelectionHolder;
        oldSelection = (E) newSelectionHolder.getValue();
        newSelectionHolder.addValueChangeListener(selectionChangeHandler);
        firePropertyChange(PROPERTYNAME_SELECTION_HOLDER,
                           oldSelectionHolder,
                           newSelectionHolder);
    }


    /**
     * Returns the selection index holder.
     *
     * @return the selection index holder
     */
    public ValueModel getSelectionIndexHolder() {
        return selectionIndexHolder;
    }


    /**
     * Sets a new selection index holder.
     * Does nothing if the new is the same as before.
     *
     * @param newSelectionIndexHolder   the selection index holder to set
     *
     * @throws NullPointerException if the new selection index holder is null
     * @throws IllegalArgumentException if the value of the new selection index
     *     holder is null
     */
    public void setSelectionIndexHolder(ValueModel newSelectionIndexHolder) {
        if (newSelectionIndexHolder == null)
            throw new NullPointerException("The new selection index holder must not be null.");

        if (newSelectionIndexHolder.getValue() == null)
            throw new IllegalArgumentException("The value of the new selection index holder must not be null.");

        ValueModel oldSelectionIndexHolder = getSelectionIndexHolder();
        if (equals(oldSelectionIndexHolder, newSelectionIndexHolder))
            return;

        oldSelectionIndexHolder.removeValueChangeListener(selectionIndexChangeHandler);
        selectionIndexHolder = newSelectionIndexHolder;
        newSelectionIndexHolder.addValueChangeListener(selectionIndexChangeHandler);
        oldSelectionIndex = getSelectionIndex();
        oldSelection = getSafeElementAt(oldSelectionIndex);
        firePropertyChange(PROPERTYNAME_SELECTION_INDEX_HOLDER,
                           oldSelectionIndexHolder,
                           newSelectionIndexHolder);
    }


    // ValueModel Implementation ********************************************

    /**
     * Returns the current selection, {@code null} if the selection index
     * does not represent a selection in the list.
     *
     * @return the selected element - if any
     */
    public E getValue() {
        return getSelection();
    }


    /**
     * Sets the selection index to the index of the first list element
     * that equals {@code newValue}. If {@code newValue}
     * is {@code null}, it is interpreted as <em>unspecified</em>
     * and the selection index is set to -1, and this SelectionInList
     * has no selection. Does nothing if the list is empty or {@code null}.
     *
     * @param newValue   the object to be set as new selection,
     *     or {@code null} to set the selection index to -1
     */
    public void setValue(Object newValue) {
        setSelection((E) newValue);
    }


    /**
     * Registers the given PropertyChangeListener with this model.
     * The listener will be notified if the value has changed.<p>
     *
     * The PropertyChangeEvents delivered to the listener have the name
     * set to "value". In other words, the listeners won't get notified
     * when a PropertyChangeEvent is fired that has a null object as
     * the name to indicate an arbitrary set of the event source's
     * properties have changed.<p>
     *
     * In the rare case, where you want to notify a PropertyChangeListener
     * even with PropertyChangeEvents that have no property name set,
     * you can register the listener with #addPropertyChangeListener,
     * not #addValueChangeListener.
     *
     * @param l the listener to add
     *
     * @see ValueModel
     */
    public void addValueChangeListener(PropertyChangeListener l) {
        addPropertyChangeListener(PROPERTYNAME_VALUE, l);
    }


    /**
     * Removes the given PropertyChangeListener from the model.
     *
     * @param l the listener to remove
     */
    public void removeValueChangeListener(PropertyChangeListener l) {
        removePropertyChangeListener(PROPERTYNAME_VALUE, l);
    }


    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param oldValue   the value before the change
     * @param newValue   the value after the change
     *
     * @see java.beans.PropertyChangeSupport
     */
    void fireValueChange(Object oldValue, Object newValue) {
        firePropertyChange(PROPERTYNAME_VALUE, oldValue, newValue);
    }


    // Misc ******************************************************************

    /**
     * Removes the internal listeners from the list holder, selection holder,
     * selection index holder. If the current list is a ListModel, the internal
     * ListDataListener is removed from the list model. This SelectionInList
     * must not be used after calling <code>#release</code>.<p>
     *
     * To avoid memory leaks it is recommended to invoke this method,
     * if the list holder, selection holder, or selection index holder
     * live much longer than this SelectionInList.
     * Instead of releasing the SelectionInList, you typically make
     * the list holder, selection holder, and selection index holder
     * obsolete by releasing the PresentationModel or BeanAdapter that has
     * created them before.<p>
     *
     * As an alternative you may use ValueModels that in turn use
     * event listener lists implemented using <code>WeakReference</code>.<p>
     *
     * Basically this release method performs the reverse operation
     * performed during the SelectionInList construction.
     *
     * @see PresentationModel#release()
     * @see BeanAdapter#release()
     * @see java.lang.ref.WeakReference
     *
     * @since 1.2
     */
    @Override
    public void release() {
        super.release();
        selectionHolder.removeValueChangeListener(selectionChangeHandler);
        selectionIndexHolder.removeValueChangeListener(selectionIndexChangeHandler);
        selectionHolder = null;
        selectionIndexHolder = null;
        oldSelection = null;
    }


    // Helper Code ***********************************************************

    private E getSafeElementAt(int index) {
        return (index < 0 || index >= getSize())
            ? null
            : getElementAt(index);
    }


    /**
     * Returns the index in the list of the first occurrence of the specified
     * element, or -1 if the element is {@code null} or the list does not
     * contain this element.<p>
     *
     * {@code null} is mapped to -1, because the current implementation
     * interprets a null selection as <em>unspecified</em>.
     *
     * @param element  the element to search for
     * @return the index in the list of the first occurrence of the
     *     given element, or -1 if the element is {@code null} or
     *     the list does not contain this element.
     */
    private int indexOf(Object element) {
        return indexOf(getListHolder().getValue(), element);
    }


    /**
     * Returns the index in the list of the first occurrence of the specified
     * element, or -1 if the element is {@code null} or the list does not
     * contain this element.<p>
     *
     * {@code null} is mapped to -1, because the current implementation
     * interprets a null selection as <em>unspecified</em>.
     *
     * @param aList    the List or ListModel used to look up the element
     * @param element  the element to search for
     * @return the index in the list of the first occurrence of the
     *     given element, or -1 if the element is {@code null} or
     *     the list does not contain this element.
     */
    private int indexOf(Object aList, Object element) {
        if (element == null) {
            return NO_SELECTION_INDEX;
        } else if (getSize(aList) == 0) {
            return NO_SELECTION_INDEX;
        }
        if (aList instanceof List) {
            return ((List<?>) aList).indexOf(element);
        }

        // Search the first occurrence of element in the list model.
        ListModel listModel = (ListModel) aList;
        int size = listModel.getSize();
        for (int index = 0; index < size; index++) {
            if (element.equals(listModel.getElementAt(index)))
                return index;
        }
        return NO_SELECTION_INDEX;
    }


    /**
     * Sets the index according to the selection, unless the selection
     * is {@code null}.
     * Also initializes the copied selection and selection index.
     * This method is invoked by the constructors to synchronize
     * the selection and index. No listeners are installed yet.<p>
     *
     * An initial selection of {@code null} may indicate that the selection
     * is unspecified. This happens for example, if the selection holder
     * adapts a bean property via a PresentationModel, but the bean
     * is {@code null}. In this case, the current semantics decides to not
     * set the selection index - even if null is a list element.<p>
     *
     * This leads to an inconsistency. If we construct a SelectionInList
     * with {1, 2, 3} and initial selection 1, the selection index is set.
     * If we construct {null, 2, 3} and initial selection null, the selection
     * index is not set.<p>
     *
     * TODO: Discuss whether we want to set the selection index if the
     * initial selection is {@code null}.
     */
    private void initializeSelectionIndex() {
        E selectionValue = (E) selectionHolder.getValue();
        if (selectionValue != null) {
            setSelectionIndex(indexOf(selectionValue));
        }
        oldSelection      = selectionValue;
        oldSelectionIndex = getSelectionIndex();
    }


    // Overriding Superclass Behavior *****************************************

    /**
     * Creates and returns the ListDataListener used to observe
     * changes in the underlying ListModel. It is re-registered
     * in <code>#updateListModel</code>.
     *
     * @return the ListDataListener that handles changes
     *     in the underlying ListModel
     */
    @Override
    protected ListDataListener createListDataChangeHandler() {
        return new ListDataChangeHandler();
    }


    /**
     * Removes the list data change handler from the old list in case
     * it is a <code>ListModel</code> and adds it to new one in case
     * it is a <code>ListModel</code>.
     * It then fires a property change for the list and a contents change event
     * for the list content. Finally it tries to restore the previous selection
     * - if any.<p>
     *
     * Since version 1.1 the selection will be restored after
     * the list content change has been indicated. This is because some
     * listeners may clear the selection in a side-effect.
     * For example a JTable that is bound to this SelectionInList
     * via an AbstractTableAdapter and a SingleSelectionAdapter
     * will clear the selection if the new list has a size other
     * than the old list.
     *
     * @param oldList   the old list content
     * @param oldSize   the size of the old List content
     * @param newList   the new list content
     *
     * @see javax.swing.JTable#tableChanged(javax.swing.event.TableModelEvent)
     */
    @Override
    protected void updateList(Object oldList, int oldSize, Object newList) {
        boolean hadSelection = hasSelection();
        Object oldSelectionHolderValue = hadSelection
            ? getSelectionHolder().getValue()
            : null;
        super.updateList(oldList, oldSize, newList);
        if (hadSelection) {
            setSelectionIndex(indexOf(newList, oldSelectionHolderValue));
        }
    }


    // Event Handlers *********************************************************

    /**
     * Handles ListDataEvents in the list model.
     * In addition to the ListDataChangeHandler in IndirectListModel,
     * this class also updates the selection index.
     */
    private final class ListDataChangeHandler implements ListDataListener {

        /**
         * Sent after the indices in the index0, index1
         * interval have been inserted in the data model.
         * The new interval includes both index0 and index1.
         *
         * @param evt  a <code>ListDataEvent</code> encapsulating the
         *    event information
         */
        public void intervalAdded(ListDataEvent evt) {
            int index0 = evt.getIndex0();
            int index1 = evt.getIndex1();
            int index  = getSelectionIndex();
            fireIntervalAdded(index0, index1);
            // If the added elements are after the index; do nothing.
            if (index >= index0) {
                setSelectionIndex(index + (index1 - index0 + 1));
            }
        }


        /**
         * Sent after the indices in the index0, index1 interval
         * have been removed from the data model.  The interval
         * includes both index0 and index1.
         *
         * @param evt  a <code>ListDataEvent</code> encapsulating the
         *    event information
         */
        public void intervalRemoved(ListDataEvent evt) {
            int index0 = evt.getIndex0();
            int index1 = evt.getIndex1();
            int index  = getSelectionIndex();
            fireIntervalRemoved(index0, index1);
            if (index < index0) {
                // The removed elements are after the index; do nothing.
            } else if (index <= index1) {
                setSelectionIndex(NO_SELECTION_INDEX);
            } else {
                setSelectionIndex(index - (index1 - index0 + 1));
            }
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
            fireContentsChanged(evt.getIndex0(), evt.getIndex1());
            updateSelectionContentsChanged(evt.getIndex0(), evt.getIndex1());
        }

        private void updateSelectionContentsChanged(int first, int last) {
            if (first < 0) return;
            int selectionIndex = getSelectionIndex();
            if (first <= selectionIndex && (selectionIndex <= last)) {
            // need to synch directly on the holder because the
            // usual methods for setting selection/-index check for
            // equality
               getSelectionHolder().setValue(getElementAt(selectionIndex));
            }
        }

    }

    /**
     * Listens to changes of the selection.
     */
    private final class SelectionChangeHandler implements PropertyChangeListener {

        /**
         * The selection has been changed. Updates the selection index holder's
         * value and notifies registered listeners about the changes - if any -
         * in the selection index, selection empty, selection, and value.<p>
         *
         * Adjusts the selection holder's value and the old selection index
         * before any event is fired. This ensures that the event old and
         * new values are consistent with the SelectionInList's state.<p>
         *
         * The current implementation assumes that the event sources
         * provides a non-{@code null} new value. An arbitrary selection holder
         * may fire change events where the new and/or old value is
         * {@code null} to indicate that it is unknown, unspecified,
         * or difficult to compute (now).<p>
         *
         * TODO: Consider getting the new selection safely from the selection
         * holder in case the new value is {@code null}. See the commented
         * code section below.
         *
         * @param evt   the property change event to be handled
         */
        public void propertyChange(PropertyChangeEvent evt) {
            E oldValue     = (E) evt.getOldValue();
            E newSelection = (E) evt.getNewValue();
//            if (newSelection == null) {
//                newSelection = (E) selectionHolder.getValue();
//            }
            int newSelectionIndex = indexOf(newSelection);
            if (newSelectionIndex != oldSelectionIndex) {
                selectionIndexHolder.removeValueChangeListener(selectionIndexChangeHandler);
                selectionIndexHolder.setValue(Integer.valueOf(newSelectionIndex));
                selectionIndexHolder.addValueChangeListener(selectionIndexChangeHandler);
            }
            int theOldSelectionIndex = oldSelectionIndex;
            oldSelectionIndex = newSelectionIndex;
            oldSelection      = newSelection;
            firePropertyChange(PROPERTYNAME_SELECTION_INDEX,
                    theOldSelectionIndex,
                    newSelectionIndex);
            firePropertyChange(PROPERTYNAME_SELECTION_EMPTY,
                    theOldSelectionIndex == NO_SELECTION_INDEX,
                    newSelectionIndex    == NO_SELECTION_INDEX);
            /*
             * Implementation Note: The following two lines fire the
             * PropertyChangeEvents for the 'selection' and 'value' properties.
             * If the old and new value are equal, no event is fired.
             *
             * TODO: Consider using ==, not equals to check for changes.
             * That would enable API users to use the selection holder with
             * beans that must be checked with ==, not equals.
             * However, the SelectionInList's List would still use equals
             * to find the index of an element.
             */
            firePropertyChange(PROPERTYNAME_SELECTION, oldValue, newSelection);
            fireValueChange(oldValue, newSelection);
        }
    }

    /**
     * Listens to changes of the selection index.
     */
    private final class SelectionIndexChangeHandler implements PropertyChangeListener {

        /**
         * The selection index has been changed. Updates the selection holder
         * value and notifies registered listeners about changes - if any -
         * in the selection index, selection empty, selection, and value.<p>
         *
         * Handles null old values in the index PropertyChangeEvent.
         * Ignores null new values in this events, because the selection
         * index value must always be a non-null value.<p>
         *
         * Adjusts the selection holder's value and the old selection index
         * before any event is fired. This ensures that the event old and
         * new values are consistent with the SelectionInList's state.
         *
         * @param evt   the property change event to be handled
         */
        public void propertyChange(PropertyChangeEvent evt) {
            int newSelectionIndex = getSelectionIndex();
            E theOldSelection = oldSelection;
            //E oldSelection = getSafeElementAt(oldSelectionIndex);
            E newSelection = getSafeElementAt(newSelectionIndex);
            /*
             * Implementation Note: The following conditional suppresses
             * value change events if the old and new selection are equal.
             *
             * TODO: Consider using ==, not equals to check for changes.
             * That would enable API users to use the selection holder with
             * beans that must be checked with ==, not equals.
             * However, the SelectionInList's List would still use equals
             * to find the index of an element.
             */
            if (!SelectionInList.this.equals(theOldSelection, newSelection)) {
                selectionHolder.removeValueChangeListener(selectionChangeHandler);
                selectionHolder.setValue(newSelection);
                selectionHolder.addValueChangeListener(selectionChangeHandler);
            }
            int theOldSelectionIndex = oldSelectionIndex;
            oldSelectionIndex = newSelectionIndex;
            oldSelection      = newSelection;
            firePropertyChange(PROPERTYNAME_SELECTION_INDEX,
                    theOldSelectionIndex,
                    newSelectionIndex);
            firePropertyChange(PROPERTYNAME_SELECTION_EMPTY,
                    theOldSelectionIndex == NO_SELECTION_INDEX,
                    newSelectionIndex    == NO_SELECTION_INDEX);
            firePropertyChange(PROPERTYNAME_SELECTION, theOldSelection, newSelection);
            fireValueChange(theOldSelection, newSelection);
        }
    }


}
