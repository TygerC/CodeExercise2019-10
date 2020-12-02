import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FitToGrid
{
    private static final char EMPTY_VALUE = '-';
    private int m_sideLength;
    private char[][] m_grid;
    
    public FitToGrid( int sideLength )
    {
        m_sideLength = sideLength;
        m_grid = new char[sideLength][sideLength];
        //fill the grid with empty values
        emptyGrid();
    }
    
    /**
     * Try to fit the given blocks to the grid.
     * 
     * @param blocks
     * @return the result grid if all blocks fit, null otherwise
     * @throws Exception if blocks list is empty or it's not possible to fit the blocks to the grid
     */
    public FittingResult fitBlocks( List<Block> blocks ) throws Exception
    {
        FittingResult result = null;
                
        if ( blocks == null || blocks.isEmpty() )
        {
            throw new Exception( "No blocks were given." );
        }
        
        //Check that the blocks fit to the grid, i.e. there aren't more points than
        //places in the grid.
        int pointCount = blocks.stream().mapToInt( b -> b.getSize() ).sum();
        
        if ( pointCount > ( m_sideLength * m_sideLength ) )
        {
            throw new Exception( "The blocks are too big to fit the grid." );
        }
        
        // Order blocks based on the width (on x-axis)
        LinkedList<Block> orderedBlocks = orderByMaxWidth( blocks );
        
        Iterator<Block> iterator = orderedBlocks.iterator();
        boolean allFit = false;

        do
        {
            // Take the first block and try to fit the other blocks around it.
            // The first block always starts from the point (0,0) and fitting goes
            // first to left on x-axis and then to top on y-axis.
            Block block = iterator.next();
            if ( !block.hasBeenFirst() )
            {
                block.setHasBeenFirst( true );
                
                // How many points the block can start from and still stay inside the grid.
                List<Point> firstBlockStartPoints = getAvailableStartingPoints( block );
                
                Iterator<Point> pointIter = firstBlockStartPoints.iterator();
                
                // Try to fit other blocks. (Well the first one also, but easier not to remove it.)
                while ( !allFit && pointIter.hasNext() )
                {
                    Point firstStartPoint = pointIter.next();
                    // Check that the first block fits, no point continuing if it doesn't.
                    if ( doesBlockFit( block, firstStartPoint ) )
                    {
                        // Try to fit the other blocks.
                        allFit = fitWithStartingBlock( block, firstStartPoint, orderedBlocks );
                    }
                    
                    if ( !allFit )
                    {
                        // Empty grid if blocks didn't fit.
                        emptyGrid();
                        // And clean try-related info from the blocks.
                        cleanBlocks( orderedBlocks );
                    }
                }
                
                if ( !allFit )
                {
                    emptyGrid();
                    cleanBlocks( orderedBlocks );
                }
                
            }
        }
        while ( notAllBlocksStarted( orderedBlocks ) && iterator.hasNext() && !allFit );
        
        
        if ( allFit )
        {
            result = new FittingResult( m_grid, orderedBlocks );
        }
        return result;
    }

    /**
     * Clean the tried values from the blocks.
     * 
     * @param blocks
     */
    private void cleanBlocks( LinkedList<Block> blocks )
    {
        blocks.forEach( b -> b.cleanTried() );
    }

    /**
     * Fill the grid with empty values.
     */
    private void emptyGrid()
    {
        for ( int i=0; i < m_sideLength; i++ )
        {
            Arrays.fill( m_grid[i], EMPTY_VALUE );
        }
    }

    /**
     * Try to fit the other blocks to the grid around the starting block.
     * 
     * @param startBlock the block to start all tries with
     * @param startPoint The point where to put the start block.
     * @param blocks all blocks
     * @return true if all blocks fit the grid, false otherwise
     */
    private boolean fitWithStartingBlock( Block startBlock, Point startPoint, List<Block> blocks )
    {
        Block block = startBlock;
        boolean allFit = false;
        
        do
        {
            try
            {
                Iterator<Block> iterator = blocks.iterator();
                if ( block == null )
                {
                    block = iterator.next();
                }
                
                do
                {
                    // Check if the block is already in the grid or this starting point has been tried 
                    // to the block already 
                    if ( !( block.isInGrid() || block.hasTriedStartPointBefore( startPoint ) ) )
                    {
                        // Add the point to list of tried starting points for this block.
                        block.addTriedStartPoint( startPoint );
                        
                        if ( putBlockToGrid( block, startPoint ) )
                        {
                            block.setInGrid( true );
                            startPoint = getNextFreePoint();
                        }
                        // else == block doesn't fit
                    }
                    // else == been there, done that, don't try again
                    
                    if ( iterator.hasNext() )
                    {
                        block = iterator.next();
                    }
                    else
                    {
                        // Inner loop has gone over all blocks, break out of it
                        block = null;
                    }
                    
                }
                while ( block != null );
            }
            catch ( NoFreePointFoundException e )
            {
                // No more free points in the grid.
                if ( areAllBlocksInTheGrid( blocks ) )
                {
                    allFit = true;
                }
            }
        }
        // Loop until all combinations has been tried of all blocks fit
        while ( notTriedAll( blocks, startPoint ) && !allFit );
        
        return areAllBlocksInTheGrid( blocks );
    }
    
    /**
     * Check if all of the blocks are in the grid.
     * 
     * @param blocks
     * @return true is all block are in the grid, false otherwise
     */
    private boolean areAllBlocksInTheGrid( List<Block> blocks )
    {
        return !blocks.stream().anyMatch( b -> !b.isInGrid() );
    }

    /**
     * Gets possible starting points for the given block starting from point (0,0)
     * and going right on the x-axis (to larger values).
     * 
     * @param block
     * @return list of available starting points for the given block. Empty list if there aren't any.
     */
    private List<Point> getAvailableStartingPoints( Block block )
    {
        List<Point> points = new ArrayList<>();
        
        int width = block.getBaseWidth();

        for ( int i=0; i <= ( m_sideLength - width ); i++ )
        {
            points.add( new Point( i, 0 ) );
        }
        
        return points;
    }

    /**
     * Checks if all blocks are either in the grid or have tried the given
     * starting point before.
     * 
     * @param blocks
     * @param point Used starting point
     * @return true if any block has not tried the given starting point and is not in the grid, false otherwise
     */
    private boolean notTriedAll( List<Block> blocks, Point point )
    {
        return blocks.stream().anyMatch( b -> !( b.isInGrid() || b.hasTriedStartPointBefore( point ) ) );
    }

    /**
     * Checks if all blocks have been as the starting block.
     * 
     * @param blocks
     * @return true if any block has not been as the starting block, false otherwise
     */
    private boolean notAllBlocksStarted( LinkedList<Block> blocks )
    {
        return blocks.stream().anyMatch( b -> !b.hasBeenFirst() );
    }

    
    /**
     * Puts the block to the grid starting from the given point if it fits.
     * 
     * @param block
     * @param startPoint
     * @return true if the block fit to the grid, false otherwise 
     */
    private boolean putBlockToGrid( Block block, Point startPoint )
    {
        boolean doesBlockFit = true;
        
        if ( block.getBaseWidth() > ( m_sideLength - startPoint.getX() ) )
        {
            doesBlockFit = false;
        }
        else
        {
            Block siftedBlock = shiftBlockToPoint( block, startPoint );
            if ( doesBlockFit( siftedBlock, startPoint ) )
            {
                for ( Point point: siftedBlock.getPoints() )
                {
                    m_grid[point.getX()][point.getY()] = siftedBlock.getName();
                }
            }
            else
            {
                doesBlockFit = false;
            }
        }
        
        return doesBlockFit;
    }

    /**
     * Check if the given block fits the grid starting from the given point.
     * 
     * @param block
     * @param startPoint
     * @return true is the block fits, false otherwise
     */
    private boolean doesBlockFit( Block block, Point startPoint )
    {
        boolean doesBlockFit = true;
        
        try
        {
            for ( Point point: block.getPoints() )
            {
                if ( EMPTY_VALUE != m_grid[point.getX()][point.getY()] )
                {
                    doesBlockFit = false;
                    break;
                }
            }
        }
        catch ( ArrayIndexOutOfBoundsException e )
        {
            // If the coordinate is outside of the grid, it's not fitting
            doesBlockFit = false;
        }
        
        return doesBlockFit;
    }

    /**
     * Shifts the coordinates of the point to be based on the given point, i.e.
     * to have all points of the blocks to be on the right side and/or above of 
     * the given point.
     * 
     * @param block the block to shift
     * @param point the point to base the shift
     * @return the shifted block
     */
    private Block shiftBlockToPoint( Block block, Point point )
    {
        Block returnBlock = block;
                
        //Find the lowest left point of the block
        Point lowestLeft = returnBlock.getLowestLeftPoint();
        
        int shiftX = point.getX() - lowestLeft.getX();
        int shiftY = point.getY() - lowestLeft.getY();

        // Shift if the value(s) are non-null
        if ( shiftX != 0 || shiftY != 0 )
        {
            returnBlock = new Block( block );
            if ( shiftX != 0 )
            {
                returnBlock.shiftXvalues( shiftX );
            }
            
            if ( shiftY != 0 )
            {
                returnBlock.shiftYvalues( shiftY );
            }
        }
        
        return returnBlock;
    }

    /**
     * Order the given list based on the width of the blocks (ascending).
     * 
     * @param blocks 
     * @return a new ordered LinkedList
     */
    private LinkedList<Block> orderByMaxWidth( List<Block> blocks )
    {
        LinkedList<Block> orderedList = blocks
                                    .stream()
                                    .sorted()
                                    .collect( Collectors.toCollection( LinkedList::new ) );
        
        return orderedList;
    }

    /**
     * Finds the next free point in the grid from the given starting point.
     * 
     * @return
     * @throws NoFreePointFoundException if the grid doesn't any free points
     */
    
    private Point getNextFreePoint() throws NoFreePointFoundException
    {
        Point nextPoint = null;

        // Search secondly from bottom to top
        for ( int y = 0 ; y < m_sideLength; y++ )
        {
            // Search first from left to right
            for ( int x = 0; x < m_sideLength; x++ )
            {
                if ( isGridPointEmpty( x, y ) )
                {
                    nextPoint = new Point( x, y );
                    break;
                }
            }
            
            if ( nextPoint != null )
            {
                break;
            }
        }

        if ( nextPoint == null )
        {
            throw new NoFreePointFoundException();
        }
        
        return nextPoint;
    }
    
    /**
     * Checks if the grid is empty in the given coordinates.
     * 
     * @param x
     * @param y
     * @return true if the point in the grid is empty, false otherwise
     */
    private boolean isGridPointEmpty( int x, int y )
    {
        char value = m_grid[x][y];
        
        return EMPTY_VALUE == value;
    }
}
