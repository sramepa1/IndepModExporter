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
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;


/**
 * A ListModel implementation that holds a List or ListModel in a ValueModel.
 * If you hold a List, this class can only report that the List has been
 * replaced; this is done by firing a PropertyChangeEvent for the <em>list</em>
 * property. Also, a <code>ListDataEvent</code> is fired that reports
 * a complete change. In contrast, if you use a ListModel it will report
 * the same PropertyChangeEvent. But fine grained changes in the ListModel
 * will be fired by this class to notify observes about changes in the content,
 * added and removed elements.<p>
 *
 * If the list content doesn't change at all, or if it always changes
 * completely, you can work well with both List content and ListModel content.
 * But if the list structure or content changes, the ListModel reports more
 * fine grained events to registered ListDataListeners, which in turn allows
 * list views to chooser better user interface gestures: for example, a table
 * with scroll pane may retain the current selection and scroll offset.<p>
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
 * For example, if you change the IndirectListModel's list model from an empty
 * list <code>L1</code> to another empty list instance <code>L2</code>,
 * the PropertyChangeSupport won't generate a PropertyChangeEvent,
 * and so, the IndirectListModel won't know about the change, which
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
 * This class provides public convenience methods for firing ListDataEvents,
 * see the methods <code>#fireContentsChanged</code>,
 * <code>#fireIntervalAdded</code>, and <code>#fireIntervalRemoved</code>.
 * These are automatically invoked if the list holder holds a ListModel
 * that fires these events. If on the other hand the underlying List or
 * ListModel does not fire a required ListDataEvent, you can use these
 * methods to notify presentations about a change. It is recommended
 * to avoid sending duplicate ListDataEvents; hence check if the underlying
 * ListModel fires the necessary events or not.<p>
 *
 * <strong>Constraints:</strong> The list holder holds instances of {@link List}
 * or {@link ListModel}. If the ListModel changes, the underlying ValueModel
 * must fire a PropertyChangeEvent.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.9 $
 *
 * @see     List
 * @see     ListModel
 * @see     SelectionInList
 * @see     ValueModel
 * @see     com.jgoodies.binding.adapter.ComboBoxAdapter
 * @see     com.jgoodies.binding.adapter.AbstractTableAdapter
 * @see     com.jgoodies.binding.beans.ExtendedPropertyChangeSupport
 * @see     com.jgoodies.binding.beans.Model
 * @see     com.jgoodies.binding.value.ValueHolder
 *
 * @param <E>  the type of the list elements
 *
 * @since 2.0
 */
public class IndirectListModel<E> extends Model implements ListModel {


    // Constant Names for Bound Properties ************************************

    /**
     * The name of the bound write-only <em>list</em> property.
     */
    public static final String PROPERTYNAME_LIST = "list";

    /**
     * The name of the bound read-write <em>listHolder</em> property.
     */
    public static final String PROPERTYNAME_LIST_HOLDER = "listHolder";


    // ************************************************************************

    /**
     * An empty <code>ListModel</code> that is used if the list holder's
     * content is null.
     *
     * @see #getListModel()
     */
    private static final ListModel EMPTY_LIST_MODEL =
        new EmptyListModel();


    // Instance Fields ********************************************************

    /**
     * Holds a <code>List</code> or <code>ListModel</code> that in turn
     * holds the elements.
     */
    private ValueModel listHolder;

    /**
     * Holds a copy of the listHolder's value. Used as the old list
     * when the listHolder's value changes. Required because a ValueModel
     * may use {@code null} as old value, but the IndirectListModel
     * must know about the old and the new list.
     */
    private Object list;

    /**
     * The size of the current list. Used during changes from an old
     * to a new list to check for shorter or longer lists, which in turn
     * leads to different ListDataEvents.
     * Required only if the old and new list are the same instance.
     */
    private int listSize;

    /**
     * Handles changes of the list.
     */
    private final PropertyChangeListener listChangeHandler;

    /**
     * Handles structural and content changes of the list model.
     */
    private final ListDataListener listDataChangeHandler;

    /**
     * Refers to the list of list data listeners that is used
     * to notify registered listeners if the ListModel changes.
     */
    private final EventListenerList listenerList = new EventListenerList();


    // Instance creation ****************************************************

    /**
     * Constructs an IndirectListModel with an empty initial
     * {@code ArrayListModel}.
     */
    public IndirectListModel() {
        this((ListModel) new ArrayListModel<E>());
    }


    /**
     * Constructs an IndirectListModel on the given item array.
     * The specified array will be converted to a List.<p>
     *
     * Changes to the list "write through" to the array, and changes
     * to the array contents will be reflected in the list.
     *
     * @param listItems        the array of initial items
     *
     * @throws NullPointerException if <code>listItems</code> is {@code null}
     */
    public IndirectListModel(E[] listItems) {
        this(Arrays.asList(listItems));
    }


    /**
     * Constructs an IndirectListModel on the given list.<p>
     *
     * <strong>Note:</strong> Favor <code>ListModel</code> over
     * <code>List</code> when working with an IndirectListModel.
     * Why? The IndirectListModel can work with both types. What's the
     * difference? ListModel provides all list access features
     * required by the IndirectListModel's. In addition it reports more
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
    public IndirectListModel(List<E> list) {
        this(new ValueHolder(list, true));
    }


    /**
     * Constructs an IndirectListModel on the given list model
     * using a default list holder.
     *
     * @param listModel        the initial list model
     */
    public IndirectListModel(ListModel listModel) {
        this(new ValueHolder(listModel, true));
    }


    /**
     * Constructs an IndirectListModel on the given list holder.<p>
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
    public IndirectListModel(ValueModel listHolder) {
        if (listHolder == null)
            throw new NullPointerException("The list holder must not be null.");
        checkListHolderIdentityCheck(listHolder);

        listChangeHandler     = new ListChangeHandler();
        listDataChangeHandler = createListDataChangeHandler();

        this.listHolder = listHolder;

        this.listHolder.addValueChangeListener(listChangeHandler);

        // If the ValueModel holds a ListModel observe list data changes too.
        list = listHolder.getValue();
        listSize = getSize(list);
        if (list != null) {
            if (list instanceof ListModel) {
                ((ListModel) list).addListDataListener(listDataChangeHandler);
            } else if (!(list instanceof List)) {
                throw new ClassCastException("The listHolder's value must be a List or ListModel.");
            }
        }
    }


    // Accessing the List/ListModel *******************************************

    /**
     * Returns the list holder's List or an empty List, if it
     * holds {@code null}. Throws an exception if the list holder holds
     * any other type, including ListModels.
     *
     * @return the List content or an empty List if the content is {@code null}
     *
     * @throws ClassCastException if the list holder is neither
     *     {@code null} nor a List
     *
     * @see #setList(List)
     * @see #getListModel()
     * @see #setListModel(ListModel)
     *
     * @since 2.0
     */
    public final List<E> getList() {
        Object aList = getListHolder().getValue();
        if (aList == null)
            return Collections.<E>emptyList();
        if (aList instanceof List)
            return (List<E>) aList;
        throw new ClassCastException(
                "#getList assumes that the list holder holds a List");
    }


    /**
     * Sets the given list as value of the list holder.<p>
     *
     * <strong>Note:</strong> Favor <code>ListModel</code> over
     * <code>List</code> when working with an IndirectListModel.
     * Why? The IndirectListModel can work with both types. What's the
     * difference? ListModel provides all list access features
     * required by the IndirectListModel's. In addition it reports more
     * fine grained change events, instances of <code>ListDataEvents</code>.
     * In contrast developer often create Lists and operate on them
     * and the ListModel may be inconvenient for these operations.<p>
     *
     * A convenient solution for this situation is to use the
     * <code>ArrayListModel</code> and <code>LinkedListModel</code> classes.
     * These implement both List and ListModel, offer the standard List
     * operations and report the fine grained ListDataEvents.
     *
     * @param newList   the list to be set as new list content
     *
     * @see #getList()
     * @see #getListModel()
     * @see #setListModel(ListModel)
     */
    public final void setList(List<E> newList) {
        getListHolder().setValue(newList);
    }


    /**
     * Returns the list holder's ListModel or an empty ListModel, if it
     * holds {@code null}. Throws an exception if the list holder holds
     * any other type, including Lists.
     *
     * @return the ListModel content or an empty ListModel
     *     if the content is {@code null}
     *
     * @throws ClassCastException if the list holder is neither
     *     {@code null} nor a ListModel
     *
     * @see #setListModel(ListModel)
     * @see #setList(List)
     */
    public final ListModel getListModel() {
        Object aListModel = getListHolder().getValue();
        if (aListModel == null)
            return EMPTY_LIST_MODEL;
        if (aListModel instanceof ListModel)
            return (ListModel) aListModel;
        throw new ClassCastException(
                "#getListModel assumes that the list holder holds a ListModel");
    }


    /**
     * Sets the given list model as value of the list holder.
     *
     * @param newListModel   the list model to be set as new list content
     *
     * @see #getListModel()
     * @see #setList(List)
     */
    public final void setListModel(ListModel newListModel) {
        getListHolder().setValue(newListModel);
    }


    // Accessing the List/ListModel Holder ************************************

    /**
     * Returns the model that holds the List/ListModel.
     *
     * @return the model that holds the List/ListModel
     */
    public final ValueModel getListHolder() {
        return listHolder;
    }

    /**
     * Sets a new list holder. Does nothing if old and new holder are equal.
     * Removes the list change handler from the old holder and adds
     * it to the new one. In case the list holder contents is a ListModel,
     * the list data change handler is updated too by invoking
     * <code>#updateListDataRegistration</code> in the same way as done in the
     * list change handler.<p>
     *
     * TODO: Check and verify whether the list data registration update
     * can be performed in one step <em>after</em> the listHolder has been
     * changed - instead of remove the list data change handler, then
     * changing the listHolder, and finally adding the list data change handler.
     *
     * @param newListHolder   the list holder to be set
     *
     * @throws NullPointerException if the new list holder is {@code null}
     * @throws IllegalArgumentException if the listHolder is a ValueHolder
     *     that doesn't check the identity when changing its value
     */
    public final void setListHolder(ValueModel newListHolder) {
        if (newListHolder == null)
            throw new NullPointerException("The new list holder must not be null.");
        checkListHolderIdentityCheck(newListHolder);

        ValueModel oldListHolder = getListHolder();
        if (oldListHolder == newListHolder)
            return;

        Object oldList = list;
        int    oldSize = listSize;
        Object newList = newListHolder.getValue();

        oldListHolder.removeValueChangeListener(listChangeHandler);
        listHolder = newListHolder;
        newListHolder.addValueChangeListener(listChangeHandler);

        updateList(oldList, oldSize, newList);
        firePropertyChange(PROPERTYNAME_LIST_HOLDER,
                           oldListHolder,
                           newListHolder);
    }


    // ListModel Implementation ***********************************************

    /**
     * Checks and answers if the list is empty or {@code null}.
     *
     * @return true if the list is empty or {@code null}, false otherwise
     */
    public final boolean isEmpty() {
        return getSize() == 0;
    }


    /**
     * Returns the length of the list, <code>0</code> if the list model
     * is {@code null}.
     *
     * @return the size of the list, <code>0</code> if the list model is
     *     {@code null}
     */
    public final int getSize() {
        return getSize(getListHolder().getValue());
    }


    /**
     * Returns the value at the specified index, {@code null}
     * if the list model is {@code null}.
     *
     * @param index  the requested index
     * @return the value at <code>index</code>, {@code null}
     *      if the list model is {@code null}
     *
     * @throws NullPointerException if the list holder's content is null
     */
    public final E getElementAt(int index) {
        return getElementAt(getListHolder().getValue(), index);
    }


    /**
     * Adds a listener to the list that's notified each time a change
     * to the data model occurs.
     *
     * @param l the <code>ListDataListener</code> to be added
     */
    public final void addListDataListener(ListDataListener l) {
        listenerList.add(ListDataListener.class, l);
    }


    /**
     * Removes a listener from the list that's notified each time a
     * change to the data model occurs.
     *
     * @param l the <code>ListDataListener</code> to be removed
     */
    public final void removeListDataListener(ListDataListener l) {
        listenerList.remove(ListDataListener.class, l);
    }


    /**
     * Returns an array of all the list data listeners
     * registered on this <code>IndirectListModel</code>.
     *
     * @return all of this model's <code>ListDataListener</code>s,
     *         or an empty array if no list data listeners
     *         are currently registered
     *
     * @see #addListDataListener(ListDataListener)
     * @see #removeListDataListener(ListDataListener)
     */
    public final ListDataListener[] getListDataListeners() {
        return listenerList.getListeners(ListDataListener.class);
    }


    // ListModel Helper Code **************************************************

    /**
     * Notifies all registered ListDataListeners that the contents
     * of one or more list elements has changed.
     * The changed elements are specified by the closed interval index0, index1
     * -- the end points are included. Note that index0 need not be less than
     * or equal to index1.<p>
     *
     * If the list holder holds a ListModel, this IndirectListModel listens
     * to ListDataEvents fired by that ListModel, and forwards these events
     * by invoking the associated <code>#fireXXX</code> method, which in turn
     * notifies all registered ListDataListeners. Therefore if you fire
     * ListDataEvents in an underlying ListModel, you don't need this method
     * and should not use it to avoid sending duplicate ListDataEvents.
     *
     * @param index0 one end of the new interval
     * @param index1 the other end of the new interval
     *
     * @see ListModel
     * @see ListDataListener
     * @see ListDataEvent
     *
     * @since 1.0.2
     */
    public final void fireContentsChanged(int index0, int index1) {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(this,
                            ListDataEvent.CONTENTS_CHANGED, index0, index1);
                }
                ((ListDataListener) listeners[i + 1]).contentsChanged(e);
            }
        }
    }


    /**
     * Notifies all registered ListDataListeners that one or more elements
     * have been added to this IndirectListModel's List/ListModel.
     * The new elements are specified by a closed interval index0, index1
     * -- the end points are included. Note that index0 need not be less than
     * or equal to index1.<p>
     *
     * If the list holder holds a ListModel, this IndirectListModel listens
     * to ListDataEvents fired by that ListModel, and forwards these events
     * by invoking the associated <code>#fireXXX</code> method, which in turn
     * notifies all registered ListDataListeners. Therefore if you fire
     * ListDataEvents in an underlying ListModel, you don't need this method
     * and should not use it to avoid sending duplicate ListDataEvents.
     *
     * @param index0 one end of the new interval
     * @param index1 the other end of the new interval
     *
     * @see ListModel
     * @see ListDataListener
     * @see ListDataEvent
     *
     * @since 1.0.2
     */
    public final void fireIntervalAdded(int index0, int index1) {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;
        listSize = getSize();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index0, index1);
                }
                ((ListDataListener) listeners[i + 1]).intervalAdded(e);
            }
        }
    }


    /**
     * Notifies all registered ListDataListeners that one or more elements
     * have been removed from this IndirectListModel's List/ListModel.
     * <code>index0</code> and <code>index1</code> are the end points
     * of the interval that's been removed.  Note that <code>index0</code>
     * need not be less than or equal to <code>index1</code>.<p>
     *
     * If the list holder holds a ListModel, this IndirectListModel listens
     * to ListDataEvents fired by that ListModel, and forwards these events
     * by invoking the associated <code>#fireXXX</code> method, which in turn
     * notifies all registered ListDataListeners. Therefore if you fire
     * ListDataEvents in an underlying ListModel, you don't need this method
     * and should not use it to avoid sending duplicate ListDataEvents.
     *
     * @param index0 one end of the removed interval,
     *               including <code>index0</code>
     * @param index1 the other end of the removed interval,
     *               including <code>index1</code>
     *
     * @see ListModel
     * @see ListDataListener
     * @see ListDataEvent
     *
     * @since 1.0.2
     */
    public final void fireIntervalRemoved(int index0, int index1) {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;
        listSize = getSize();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index0, index1);
                }
                ((ListDataListener) listeners[i + 1]).intervalRemoved(e);
            }
        }
    }


    // Misc ******************************************************************

    /**
     * Removes the internal listeners from the list holder. If the current list
     * is a ListModel, the internal ListDataListener is removed from it.
     * This IndirectListModel must not be used after calling
     * <code>#release</code>.<p>
     *
     * To avoid memory leaks it is recommended to invoke this method,
     * if the list holder, selection holder, or selection index holder
     * live much longer than this IndirectListModel.
     * Instead of releasing the IndirectListModel, you typically make the
     * list holder obsolete by releasing the PresentationModel or BeanAdapter
     * that has created them before.<p>
     *
     * As an alternative you may use ValueModels that in turn use
     * event listener lists implemented using <code>WeakReference</code>.<p>
     *
     * Basically this release method performs the reverse operation
     * performed during the IndirectListModel construction.
     *
     * @see PresentationModel#release()
     * @see BeanAdapter#release()
     * @see java.lang.ref.WeakReference
     *
     * @since 1.2
     */
    public void release() {
        listHolder.removeValueChangeListener(listChangeHandler);
        if (list != null && (list instanceof ListModel)) {
            ((ListModel) list).removeListDataListener(listDataChangeHandler);
        }
        listHolder = null;
        list = null;
    }


    // Default Behavior *******************************************************

    /**
     * Creates and returns the ListDataListener used to observe
     * changes in the underlying ListModel. It is re-registered
     * in <code>#updateListModel</code>.
     *
     * @return the ListDataListener that handles changes
     *     in the underlying ListModel
     */
    protected ListDataListener createListDataChangeHandler() {
        return new ListDataChangeHandler();
    }


    /**
     * Removes the list data change handler from the old list in case
     * it is a <code>ListModel</code> and adds it to new one in case
     * it is a <code>ListModel</code>.
     * It then fires a property change for the list and a contents change event
     * for the list content.
     *
     * @param oldList   the old list content
     * @param oldSize   the size of the old List content
     * @param newList   the new list content
     *
     * @see javax.swing.JTable#tableChanged(javax.swing.event.TableModelEvent)
     */
    protected void updateList(Object oldList, int oldSize, Object newList) {
        if (oldList != null && (oldList instanceof ListModel)) {
            ((ListModel) oldList).removeListDataListener(listDataChangeHandler);
        }
        if (newList != null && (newList instanceof ListModel)) {
            ((ListModel) newList).addListDataListener(listDataChangeHandler);
        }
        int newSize = getSize(newList);
        list = newList;
        listSize = getSize(newList);
        firePropertyChange(PROPERTYNAME_LIST, oldList, newList);
        fireListChanged(oldSize - 1, newSize - 1);
    }


    /**
     * Notifies all registered ListDataListeners that this ListModel
     * has changed from an old list to a new list content.
     * If the old and new list size differ, a remove or add event for
     * the removed or added interval is fired. A content change
     * is reported for the interval common to the old and new list.<p>
     *
     * This method is invoked by #updateList during the transition
     * from an old List(Model) to a new List(Model).<p>
     *
     * <strong>Note:</strong>
     * The order of the events fired ensures that after each event
     * the size described by the ListDataEvents equals the ListModel size.
     *
     * @param oldLastIndex   the last index of the old list
     * @param newLastIndex   the last index of the new list
     */
    protected final void fireListChanged(int oldLastIndex, int newLastIndex) {
        if (newLastIndex < oldLastIndex) {
            fireIntervalRemoved(newLastIndex + 1, oldLastIndex);
        } else if (oldLastIndex < newLastIndex) {
            fireIntervalAdded(oldLastIndex + 1, newLastIndex);
        }
        int lastCommonIndex = Math.min(oldLastIndex, newLastIndex);
        if (lastCommonIndex >= 0) {
            fireContentsChanged(0, lastCommonIndex);
        }
    }


    // Helper Code ************************************************************

    /**
     * Returns the length of the given list, <code>0</code> if the list model
     * is {@code null}.
     *
     * @param aListListModelOrNull  a List, ListModel or null
     * @return the size of the given list, <code>0</code> if the list model is
     *     {@code null}
     */
    protected final int getSize(Object aListListModelOrNull) {
        if (aListListModelOrNull == null)
            return 0;
        else if (aListListModelOrNull instanceof ListModel)
            return ((ListModel) aListListModelOrNull).getSize();
        else
            return ((List<?>) aListListModelOrNull).size();
    }


    private E getElementAt(Object aList, int index) {
        if (aList == null)
            throw new NullPointerException("The list contents is null.");
        else if (aList instanceof ListModel)
            return (E) ((ListModel) aList).getElementAt(index);
        else
            return ((List<E>) aList).get(index);
    }


    /**
     * Throws an IllegalArgumentException if the given ValueModel
     * is a ValueHolder that has the identityCheck feature disabled.
     */
    private void checkListHolderIdentityCheck(ValueModel aListHolder) {
        if (!(aListHolder instanceof ValueHolder))
            return;

        ValueHolder valueHolder = (ValueHolder) aListHolder;
        if (!valueHolder.isIdentityCheckEnabled())
            throw new IllegalArgumentException(
                 "The list holder must have the identity check enabled.");
    }


    // Helper Classes *********************************************************

    /**
     * A ListModel that has no elements, a size of 0, and never fires an event.
     */
    private static final class EmptyListModel implements ListModel, Serializable {

        /**
         * Returns zero to indicate an empty list.
         */
        public int getSize() { return 0; }

        /**
         * Returns {@code null} because this model has no elements.
         */
        public Object getElementAt(int index) { return null; }

        /**
         * Does nothing, because the empty list will never fire an event.
         *
         * @param l the <code>ListDataListener</code> to be ignored
         */
        public void addListDataListener(ListDataListener l) {
            // Do nothing.
        }

        /**
         * Does nothing, because the empty list will never fire an event.
         *
         * @param l the <code>ListDataListener</code> to be ignored
         */
        public void removeListDataListener(ListDataListener l) {
            // Do nothing.
        }

    }


    // Event Handlers *********************************************************

    /**
     * Handles changes of the List or ListModel.
     */
    private final class ListChangeHandler implements PropertyChangeListener {

        /**
         * The list has been changed.
         * Notifies all registered listeners about the change.
         *
         * @param evt   the property change event to be handled
         */
        public void propertyChange(PropertyChangeEvent evt) {
            Object oldList = list;
            int    oldSize = listSize;
            Object newList = evt.getNewValue();
            updateList(oldList, oldSize, newList);
        }
    }


    /**
     * Handles ListDataEvents in the list model.
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
            fireIntervalAdded(index0, index1);
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
            fireIntervalRemoved(index0, index1);
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
        }

    }


}
