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

import javax.swing.JFrame;

import com.jgoodies.binding.tutorial.Album;
import com.jgoodies.binding.tutorial.TutorialApplication;

/**
 * This is just a launcher class for the Album Manager example.
 * It creates an AlbumManagerView for a list of example Albums,
 * creates a JFrame and add the view as the frame's content.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.14 $
 *
 * @see AlbumManagerView
 * @see AlbumManagerModel
 * @see com.jgoodies.binding.PresentationModel
 */
public final class AlbumManagerExample extends TutorialApplication {


    // Launching **************************************************************

    public static void main(String[] args) {
        TutorialApplication.launch(AlbumManagerExample.class, args);
    }


    @Override
    protected void startup(String[] args) {
        JFrame frame = createFrame("Binding Tutorial :: Album Manager");
        List<Album> exampleAlbums = Album.ALBUMS;
        AlbumManager albumManager = new AlbumManager(exampleAlbums);
        AlbumManagerModel model = new AlbumManagerModel(albumManager);
        AlbumManagerView view = new AlbumManagerView(model);
        frame.add(view.buildPanel());
        packAndShowOnScreenCenter(frame);
    }


}
