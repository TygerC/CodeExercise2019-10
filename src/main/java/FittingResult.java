import java.util.List;
import java.util.stream.Collectors;

public class FittingResult
{
    private final char[][] m_grid;
    private final List<Block> m_blocks;
    
    public FittingResult( char[][] grid, List<Block> blocks )
    {
        this.m_grid = grid;
        this.m_blocks = blocks;
    }
    
    public char[][] getGrid()
    {
        return m_grid;
    }

    public List<Block> getBlocks()
    {
        return m_blocks;
    }

    /**
     * Print the grid to output stream in human readable form.
     */
    public void printGrid()
    {
        System.out.println( "---- Blocks in the grid ----" );
        
        for ( int i = m_grid.length - 1; i >= 0; i-- )
        {
            for ( int k = 0 ; k < m_grid.length; k++ )
            {
                System.out.print( "(" + m_grid[k][i] + ") " );
            }
            System.out.println();
        }
    }
    
    /**
     * Print the coordinates of the blocks to the output stream. 
     */
    public void printCoordinates()
    {
        System.out.println( "---- Coordinates of the blocks ----" );
        
        // Order by name
        List<Block> orderedByName = m_blocks
                                    .stream()
                                    .sorted( ( b1, b2 ) -> Character.compare( b1.getName(), b2.getName() ) )
                                    .collect( Collectors.toList() );
        
        
        for ( Block block: orderedByName )
        {
            System.out.println( block.getBlockAsString() );
        }
    }

    
}
