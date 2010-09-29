package cz.cvut.promod.gui.listeners;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import cz.cvut.promod.gui.support.utils.NotationGuiHolder;
import cz.cvut.promod.gui.projectNavigation.ProjectNavigation;
import cz.cvut.promod.gui.ModelerModel;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.LinkedList;

import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockingManager;

import javax.swing.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 18:32:19, 19.10.2009
 *
 * Action that controls the visibility of high level navigation.
 */
public  class ProjectNavigationActionTarget extends ProModAction {

    private final DockingManager dockingManager;
    private final ProjectNavigation projectNavigationDockableFrame;
    private final ModelerModel model;

    public ProjectNavigationActionTarget(final String displayName, 
                                         final Icon icon,
                                         final KeyStroke keyStroke,
                                         final DockingManager dockingManager,
                                         final ProjectNavigation projectNavigationDockableFrame,
                                         final ModelerModel model){

        super(displayName, icon, keyStroke);

        this.dockingManager = dockingManager;
        this.projectNavigationDockableFrame = projectNavigationDockableFrame;
        this.model = model;
    }

    public void actionPerformed(ActionEvent event) {
        if( projectNavigationDockableFrame.isAvailable() ){
            dockingManager.setFrameUnavailable(projectNavigationDockableFrame.getName());
        } else{
            final String currentNotation = ModelerSession.getProjectService().getSelectedProjectNotationIndentifier();

            final NotationGuiHolder specification = model.getNotationGuiHolder(currentNotation);

            if( specification != null ){
                final List<DockableFrame> visibleDockableFrames = new LinkedList<DockableFrame>();

                for( final DockableFrame dockableFrame : specification.getListOfFrames(NotationGuiHolder.Position.LEFT) ){
                    if( dockableFrame.isAvailable() ){
                        visibleDockableFrames.add((dockableFrame));
                        specification.getButton(dockableFrame.getName(), NotationGuiHolder.Position.LEFT).doClick();
                    }
                }

                dockingManager.setFrameAvailable(projectNavigationDockableFrame.getName());

                for( final DockableFrame dockableFrame : visibleDockableFrames ){
                    specification.getButton(dockableFrame.getName(), NotationGuiHolder.Position.LEFT).doClick();
                }
            } else{
                dockingManager.setFrameAvailable(projectNavigationDockableFrame.getName());
            }
        }
    }

}