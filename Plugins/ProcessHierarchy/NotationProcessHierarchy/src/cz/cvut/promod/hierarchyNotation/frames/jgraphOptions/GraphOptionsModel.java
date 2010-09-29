package cz.cvut.promod.hierarchyNotation.frames.jgraphOptions;

import com.jgoodies.binding.beans.Model;
import cz.cvut.promod.hierarchyNotation.resources.Resources;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:29:57, 27.11.2009
 *
 * The Model component for the GraphOptions dockable frame.
 */
public class GraphOptionsModel extends Model {

    public static final String TITLE_LABEL = Resources.getResources().getString("hierarchy.frame.options.title");

    public static final int INIT_SPINNER_STEP = 1;

    public static final int MIN_SCALE = 20;
    public static final int MAX_SCALE = 100;
    public static final int EXTENT = 0;
    public static final int INIT_SCALE = 100;

    public static final int MIN_CELL_SIZE = 5;
    public static final int MAX_CELL_SIZE = 30;
    public static final int INIT_CELL_SIZE = 30;

    public static final int INIT_SELL_SIZE = 10;

    public static final String PROPERTY_GRID = "grid";
    private boolean grid = true; // is true because the initial settings sets this to false in GraphOptions, fire change

    public static final String PROPERTY_VIEW_GRID = "viewGrid";
    private boolean viewGrid;

    public static final String PROPERTY_CELL_SIZE = "cellSize";
    private int cellSize = 20;

    public static final String PROPERTY_SCALE = "scale";
    private int scale = INIT_SCALE;

    private final String name;


    public GraphOptionsModel(final String name) {
        this.name = name;        
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
        firePropertyChange(PROPERTY_VIEW_GRID, oldViewGrid, viewGrid);
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

    public String getName() {
        return name;
    }
}

