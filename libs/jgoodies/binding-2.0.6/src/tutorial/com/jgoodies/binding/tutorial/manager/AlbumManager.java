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

package com.jgoodies.binding.tutorial.manager;

import java.util.List;

import javax.swing.ListModel;

import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.tutorial.Album;

/**
 * Holds a List of Albums and provides operations to add, delete and
 * change a Album. Such a manager is often part of the domain layer.<p>
 *
 * This manager holds the Albums in an ArrayListModel, so we can
 * operate on a List and can expose it as a ListModel.
 * As an alternative, a higher-level presentation model, such as
 * the AlbumManagerModel could turn the List into a ListModel.
 * In the latter case, you would then need to fire the required
 * ListDataEvents.<p>
 *
 * The AlbumManagerModel turns the List of Albums and the operations
 * into a form that can be used in a user interface to display,
 * select, and edit Albums.<p>
 *
 * TODO: Demonstrate how to sort the albums.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.10 $
 *
 * @see AlbumManagerModel
 */

public final class AlbumManager {

    /**
     * Holds the List of Albums. Albums are added and removed from this List.
     * The ObservableList implements ListModel, and so, we can directly
     * use this List for the UI and can observe changes.<p>
     *
     * In a real world application this List may be kept
     * in synch with a database.
     */
    private final ArrayListModel<Album> managedAlbums;


    // Instance Creation ******************************************************

    /**
     * Constructs a AlbumManager for the given list of Albums.
     *
     * @param albums   the list of Albums to manage
     */
    public AlbumManager(List<Album> albums) {
        this.managedAlbums = new ArrayListModel<Album>(albums);
    }


    // Exposing the ListModel of Albums ****************************************

    public ListModel getManagedAlbums() {
        return managedAlbums;
    }


    // Managing Albums *********************************************************

    /**
     * Creates and return a new Album.
     *
     * @return the new Album
     */
    public Album createItem() {
        return new Album();
    }


    /**
     * Adds the given Album to the List of managed Albums
     * and notifies observers of the managed Albums ListModel
     * about the change.
     *
     * @param albumToAdd   the Album to add
     */
    public void addItem(Album albumToAdd) {
        managedAlbums.add(albumToAdd);
    }


    /**
     * Removes the given Album from the List of managed Albums
     * and notifies observers of the managed Albums ListModel
     * about the change.
     *
     * @param albumToRemove    the Album to remove
     */
    public void removeItem(Album albumToRemove) {
        managedAlbums.remove(albumToRemove);
    }


}
