package cz.cvut.promod.gui;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.gui.projectNavigation.ProjectNavigation;
import cz.cvut.promod.gui.support.utils.NotationGuiHolder;
import cz.cvut.promod.plugin.notationSpecificPlugIn.DockableFrameData;

import com.jidesoft.docking.*;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.action.CommandBar;
import com.jidesoft.status.StatusBar;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.status.ButtonStatusBarItem;
import com.jgoodies.forms.factories.Borders;

import javax.swing.*;
import java.util.List;
import java.awt.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:10:00, 10.10.2009
 */

/**
 * GUI definition for the Modeler class.
 */
public class ModelerView extends JFrame {

    /** Dimension for layout of side buttons. */
    private final Dimension BOX_VERTICAL_GAP = new Dimension(0, 5);
    private final Dimension BOX_HORIZONTAL_GAP = new Dimension(5, 0);

    /** NO other DockableFrame is allowed to have this index */
    private static final int PROJECT_NAVIGATION_INDEX = 0;

    /** Each instance of DockableFrame is supposed to have unique index. */
    public static int dockingFrameCounter = PROJECT_NAVIGATION_INDEX + 1;

    /** Represents the dockable frame ID counter. */
    private static int dockableFrameIDcounter = 1;

    protected ProjectNavigation projectNavigationDockableFrame;

    protected DefaultDockingManager dockingManager;

    private JPanel dockableArea;

    protected final CardLayout cardLayoutRightSidePane = new CardLayout();
    protected final JPanel rightSidePane = new JPanel(cardLayoutRightSidePane);

    private final JPanel leftSideBottomLayerPane = new JPanel(new BorderLayout());
    protected final JButton projectNavigatorButton = ModelerSession.getComponentFactoryService().createJideButton(
            ModelerSession.getCommonResourceBundle().getString("modeler.action.navigation"), null, 1);

    protected final CardLayout cardLayoutLeftSidePane = new CardLayout();
    protected final JPanel leftSidePane = new JPanel(cardLayoutLeftSidePane);

    protected final CardLayout cardLayoutBottomSidePane = new CardLayout();
    protected final JPanel bottomSidePane = new JPanel(cardLayoutBottomSidePane);

    protected final CardLayout cardLayoutTopSidePane = new CardLayout();
    protected final JPanel topSidePane = new JPanel(cardLayoutTopSidePane);    

    protected final CardLayout cardLayoutWorkspacePane = new CardLayout();
    protected final JPanel workspacePane = new JPanel(cardLayoutWorkspacePane);

    private Workspace workspace;

    protected final StatusBar statusBar = ModelerSession.getComponentFactoryService().createStatusBar();
    protected final LabelStatusBarItem statusUserTitleLabel = ModelerSession.getComponentFactoryService().createLabelStatusBarItem();
    protected final LabelStatusBarItem loggedUserLabel = ModelerSession.getComponentFactoryService().createLabelStatusBarItem();
    protected final ButtonStatusBarItem switchUserButton = ModelerSession.getComponentFactoryService().createButtonStatusBarItem();

    protected final CardLayout cardLayoutToolBar = new CardLayout();
    protected final JPanel toolBarPane = new JPanel(cardLayoutToolBar);

    private static final Dimension DEFAULT_SIZE = new Dimension(1024, 768);

    private static final Dimension DEFAULT_SIZE_LOW_LEVEL = new Dimension(300, 500);

    private static final Dimension DEFAULT_SIZE_CONSOLE = new Dimension(500, 200);

    private static final Point DEFAULT_UNDOCKED_FRAME_POSITION = new Point(-1, -1);

    private static final String USER_TITLE_LABEL = ModelerSession.getCommonResourceBundle().getString("modeler.user.title");
    private static final String USER_SWITCH_LABEL = ModelerSession.getCommonResourceBundle().getString("modeler.user.switch");

    protected final CardLayout cardLayoutStatusBar = new CardLayout();
    protected final JPanel statusBarPane = new JPanel(cardLayoutStatusBar);


    public ModelerView(){
        setTitle(ModelerModel.APPLICATION_NAME);

        initializeDockingManager();

        initLayout();

        initStatusBar();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);        
        setPreferredSize(DEFAULT_SIZE);
        setSize(DEFAULT_SIZE);
        setLocationRelativeTo(null);
    }

    /**
     * Initialize status bar items.
     */
    private void initStatusBar() {
        statusUserTitleLabel.setAlignment(JLabel.LEFT);
        statusUserTitleLabel.setText(USER_TITLE_LABEL);
        statusBar.add(statusUserTitleLabel, JideBoxLayout.FIX);

        loggedUserLabel.setAlignment(JLabel.LEFT);
        loggedUserLabel.setText(ModelerSession.getUserService().getUser());
        statusBar.add(loggedUserLabel, JideBoxLayout.FIX);

        switchUserButton.setText(USER_SWITCH_LABEL);
        statusBar.add(switchUserButton, JideBoxLayout.FIX);

        statusBar.add(statusBarPane, JideBoxLayout.VARY);
    }

    private void initLayout(){
        workspace.add(workspacePane); // add panel with CardLayout on the workspace

        final JPanel basisPanel = ModelerSession.getComponentFactoryService().createPanel();
        basisPanel.setLayout(new BorderLayout());

        basisPanel.add(dockableArea, BorderLayout.CENTER);
        basisPanel.add(rightSidePane, BorderLayout.EAST);
        basisPanel.add(leftSideBottomLayerPane, BorderLayout.WEST);
        basisPanel.add(topSidePane, BorderLayout.NORTH);
        basisPanel.add(bottomSidePane, BorderLayout.SOUTH);

        add(basisPanel, BorderLayout.CENTER);
        add(toolBarPane, BorderLayout.PAGE_START);
        add(statusBar, BorderLayout.PAGE_END);

        //init high level navigation hide/show button
        leftSideBottomLayerPane.add(leftSidePane, BorderLayout.CENTER);

        final JPanel panelProjectNavigationButton = new JPanel(new BorderLayout());
        panelProjectNavigationButton.setBorder(Borders.DLU2_BORDER);
        panelProjectNavigationButton.setOpaque(false);
        panelProjectNavigationButton.add(projectNavigatorButton, BorderLayout.CENTER);

        leftSideBottomLayerPane.add(panelProjectNavigationButton, BorderLayout.NORTH);

        //ads empty panels to all of card layouts with special key defined by ModelerModel.MODELER_IDENTIFIER
        initEmptyModelerView();
    }

    /**
     * Initialize buttons panels, workspace and tool bar that are visible if no notation is selected.
     */
    private void initEmptyModelerView() {
        rightSidePane.add(ModelerSession.getComponentFactoryService().createPanel(), ModelerModel.MODELER_IDENTIFIER);
        leftSidePane.add(ModelerSession.getComponentFactoryService().createPanel(), ModelerModel.MODELER_IDENTIFIER);
        bottomSidePane.add(ModelerSession.getComponentFactoryService().createPanel(), ModelerModel.MODELER_IDENTIFIER);
        topSidePane.add(ModelerSession.getComponentFactoryService().createPanel(), ModelerModel.MODELER_IDENTIFIER);

        workspacePane.add(ModelerSession.getComponentFactoryService().createPanel(), ModelerModel.MODELER_IDENTIFIER);

        toolBarPane.add(ModelerSession.getToolBarControlService().getCommandBar(ModelerModel.MODELER_IDENTIFIER), ModelerModel.MODELER_IDENTIFIER);
        statusBarPane.add(ModelerSession.getStatusBarControlService().getStatusBar(ModelerModel.MODELER_IDENTIFIER), ModelerModel.MODELER_IDENTIFIER);
    }

    /**
     * Initialization project navigation.
     */
    protected void initProjectNavigation(){
        projectNavigationDockableFrame = new ProjectNavigation(PROJECT_NAVIGATION_INDEX);
        dockingManager.addFrame(projectNavigationDockableFrame);
    }

    private void initializeDockingManager(){
      dockableArea = new JPanel();

      dockingManager = new DefaultDockingManager(this, dockableArea);

      workspace = dockingManager.getWorkspace();

      dockingManager.setSideDockAllowed(false);

      dockingManager.setInitSplitPriority(DockingManager.SPLIT_EAST_WEST_SOUTH_NORTH);

      dockingManager.setTabbedPaneCustomizer(new
          DockingManager.TabbedPaneCustomizer(){
            public void customize( JideTabbedPane tabbedPane ){
              tabbedPane.setHideOneTab(true);
            }
          });
    }

    /**
     * Adds a new main windows to the workplace card layout.
     *
     * @param key is a key for the card layout
     * @param component is the component to be displayed
     */
    protected void addMainWindowPane( final String key, final Component component ){
        workspacePane.add(key, component);
    }

    /**
     * Adds a new tool bar to the tool bar card layout.
     *
     * @param notationIdentifier is a key for the card layout
     * @param commandBar is the component to be displayed
     */
    protected void addToolBarPane(final String notationIdentifier, final CommandBar commandBar){
        toolBarPane.add(commandBar, notationIdentifier);
    }

    /**
     * Adds a new status bar.
     *
     * @param notationIdentifier is a key for the card layout
     * @param statusBar is the notation specific status bar
     */
    protected void addStatusBar(final String notationIdentifier, final StatusBar statusBar){
        statusBarPane.add(statusBar, notationIdentifier);
    }

    /**
     * Adds a new side panel onto the specified base layer using CardLayout.
     *
     * @param key        to be used by the CardLayout.
     * @param baseLayer  is base layer onto that should be the new panel added.
     * @param buttonList is list of buttons to be shown on the new layer.
     * @param isVertical true = vertical (for low level navigation), false = horizontal (consoles).
     */
    protected void addSidePane( final String key,
                                final JPanel baseLayer,
                                final List<JideButton> buttonList,
                                final boolean isVertical ){
      final JPanel panel = new JPanel();
      panel.setBorder(Borders.DLU2_BORDER);

      final Dimension dimension;
      if( isVertical ){
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        dimension = BOX_VERTICAL_GAP;
      } else{
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        dimension = BOX_HORIZONTAL_GAP;
      }

      for( JideButton but : buttonList ){
        panel.add(but);
        panel.add(Box.createRigidArea(dimension));
      }

      baseLayer.add(panel, key);
    }

    /**
     * Creates the dockable frame.
     *
     * @param component is the component to be added on the dockable frame.
     * @param position is the position specification.
     * @param title is the dockable frame id
     * @param isMaximizable true if the frame is supposed to be maximizable
     * @param initialState is the initial state of the frame
     * @return the instance of required dockable frame
     */
    protected DockableFrame createDockableFrame(final JComponent component,
                                                final NotationGuiHolder.Position position,
                                                final String title,
                                                final boolean isMaximizable,
                                                final DockableFrameData.InitialState initialState){

        final DockableFrame dockableFrame = new DockableFrame(String.valueOf(dockableFrameIDcounter++));
        dockableFrame.setTitle(title);

        dockableFrame.setShowContextMenu(false); 

        if(DockableFrameData.InitialState.FLOATED.equals(initialState)){
            dockableFrame.getContext().setInitMode(DockContext.STATE_FLOATING);
        } else {
            dockableFrame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        }
        
        dockableFrame.getContext().setInitIndex(dockingFrameCounter++);
        dockableFrame.setAutohidable(false);
        dockableFrame.setDockable(true);
        dockableFrame.setSideDockAllowed(false);
        dockableFrame.setRearrangable(false);
        dockableFrame.setTabDockAllowed(false);

        // make this dockable frame maximizable or not
        int availableButtons = DockableFrame.BUTTON_FLOATING | DockableFrame.BUTTON_CLOSE;
        if(isMaximizable){
             availableButtons |= DockableFrame.BUTTON_MAXIMIZE;
        }
        dockableFrame.setAvailableButtons(availableButtons);

        switch( position ){
            case RIGHT:
                dockableFrame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
                dockableFrame.setUndockedBounds(new Rectangle(DEFAULT_UNDOCKED_FRAME_POSITION, DEFAULT_SIZE_LOW_LEVEL));
                dockableFrame.setPreferredSize(DEFAULT_SIZE_LOW_LEVEL);
                break;
            case LEFT:
                dockableFrame.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
                dockableFrame.setUndockedBounds(new Rectangle(DEFAULT_UNDOCKED_FRAME_POSITION, DEFAULT_SIZE_LOW_LEVEL));
                dockableFrame.setPreferredSize(DEFAULT_SIZE_LOW_LEVEL);
                break;
            case BOTTOM:
                dockableFrame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
                dockableFrame.setUndockedBounds(new Rectangle(DEFAULT_UNDOCKED_FRAME_POSITION, DEFAULT_SIZE_CONSOLE));
                dockableFrame.setPreferredSize(DEFAULT_SIZE_CONSOLE);
                break;
            case TOP:
                dockableFrame.getContext().setInitSide(DockContext.DOCK_SIDE_NORTH);
                dockableFrame.setUndockedBounds(new Rectangle(DEFAULT_UNDOCKED_FRAME_POSITION, DEFAULT_SIZE_CONSOLE));
                dockableFrame.setPreferredSize(DEFAULT_SIZE_CONSOLE);
                break;
            default:
                dockableFrame.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
        }

        dockableFrame.add(component);
        
        return dockableFrame;
    }

}
