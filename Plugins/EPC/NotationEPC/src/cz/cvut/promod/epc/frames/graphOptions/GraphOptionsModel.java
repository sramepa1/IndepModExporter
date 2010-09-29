package cz.cvut.promod.epc.frames.graphOptions;

import com.jgoodies.binding.beans.Model;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:29:57, 27.11.2009
 *
 * The Model component of the GraphOptions dockable frame. 
 */
public class GraphOptionsModel extends Model {

    public static final String FRAME_TITLE_RES = "epc.frame.options.title";
    public static final String BELOW_ZERO_RES = "epc.frame.options.below.zero";
    public static final String USE_GRID_RES = "epc.frame.options.grid";
    public static final String CELL_SIZE_RES = "epc.frame.options.cel.size";
    public static final String VIEW_GRID_RES = "epc.frame.options.grid.view";
    public static final String GRAPH_SCALE_RES = "epc.frame.options.scale";
    public static final String GRAPH_SCALE_100_RES = "epc.frame.options.scale.100";

    public static final int INIT_SPINNER_STEP = 1;

    public static final int MIN_SCALE = 20;
    public static final int MAX_SCALE = 300;
    public static final int EXTENT = 0;
    public static final int INIT_SCALE = 100;

    public static final int MIN_CELL_SIZE = 5;
    public static final int MAX_CELL_SIZE = 30;
    public static final int INIT_CELL_SIZE = 30;

    public static final int INIT_SELL_SIZE = 10;

    public static final String PROPERTY_LOCK = "lock";
    private boolean lock;

    public static final String PROPERTY_ANTI_ALIASING = "antiAliasing";
    private boolean antiAliasing;

    public static final String PROPERTY_GRID = "grid";
    private boolean grid = true; // is true because the initial settings sets this to false in GraphOptions, fire change

    public static final String PROPERTY_VIEW_GRID = "viewGrid";
    private boolean viewGrid;

    public static final String PROPERTY_CELL_SIZE = "cellSize";
    private int cellSize = 20;

    public static final String PROPERTY_SCALE = "scale";
    private int scale = INIT_SCALE;

    public boolean isLock() {
        return lock;
    }

    public void setLock(final boolean lock) {
        final boolean oldLock = this.lock;
        this.lock = lock;
        firePropertyChange(PROPERTY_LOCK, oldLock, lock);
    }

    public boolean isGrid() {
        return grid;
    }

    public void setGrid(final boolean grid) {
        final boolean oldGrid = this.grid;
        this.grid = grid;
        firePropertyChange(PROPERTY_GRID, oldGrid, grid);
    }

    public boolean isViewGrid() {
        return viewGrid;
    }

    public int getScale() {
        return scale;
    }

    public boolean isAntiAliasing() {
        return antiAliasing;
    }

    public void setAntiAliasing(final boolean antiAliasing) {
        final boolean oldValue = this.antiAliasing;
        this.antiAliasing = antiAliasing;
        firePropertyChange(PROPERTY_ANTI_ALIASING, oldValue, antiAliasing);
    }

    public void setScale(final int scale) {
        if(!isAllowedCellSize(scale, MIN_SCALE, MAX_SCALE)){
            return;            
        }

        final int oldValue = this.scale;
        this.scale = scale;
        firePropertyChange(PROPERTY_SCALE, oldValue, scale);
    }

    public void setViewGrid(final boolean viewGrid) {
        final boolean oldViewGrid = this.viewGrid;
        this.viewGrid = viewGrid;
        firePropertyChange(GraphOptionsModel.PROPERTY_VIEW_GRID, oldViewGrid, viewGrid);
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(final int cellSize) {
        if(!isAllowedCellSize(cellSize, MIN_CELL_SIZE, MAX_CELL_SIZE)){
            return;
        }

        final int oldCellSize = this.cellSize;
        this.cellSize = cellSize;
        firePropertyChange(PROPERTY_CELL_SIZE, oldCellSize, cellSize);
    }

    private boolean isAllowedCellSize(final double cellSize, int min, int max){
        return !(cellSize < min || cellSize > max);

    }
}