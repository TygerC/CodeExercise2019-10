import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Main class
 */
public class CodeTest
{
    
    /**
     * Runs the process of fitting blocks to the grid.
     * 
     * @param args Text file containing the coordinates of the blocks
     */
    public static void main( String[] args )
    {
        if ( args.length != 1 )
        {
            System.err.println( "Missing input file." );
            return;
        }
        
        List<Block> blocks = null;
        
        try
        {
            blocks = parseInputFile( args[0] );
        }
        catch ( Exception e )
        {
            System.err.println( "Failed to read input file: " + e.getMessage() );
            return;
        }
        
        try
        {
            FitToGrid fitToGrid = new FitToGrid( 4 );
            FittingResult fittingResult = fitToGrid.fitBlocks( blocks );
            
            if ( fittingResult == null )
            {
                System.err.println( "The blocks does not fit the grid." );
            }
            else
            {
                // This looks nice
                fittingResult.printGrid();
                // The result in the same format as the input
                fittingResult.printCoordinates();
            }
        }
        catch ( Exception e )
        {
            System.err.println( "Got an exception while processing: " + e.getMessage() );
        }
    }

    /**
     * Parse input file and create the block list from it.
     * 
     * @param filename 
     * @return list of blocks
     * @throws Exception if any problems in the parsing or creation of the blocks
     */
    private static List<Block> parseInputFile( String filename ) throws Exception
    {
        Path path = Paths.get( filename );
        List<String> inputLines = Files.readAllLines( path );
        List<Block> blocks = new ArrayList<>();
        
        for ( String line: inputLines )
        {
            String[] values = line.split( ":" );
            String[] points = values[1].split( ";" );
            Set<Point> pointSet = new HashSet<>();
                    
            for ( String pointStr : points )
            {
                String[] coordinates = pointStr.split( "," );
                Point point = new Point( 
                        Integer.parseInt( coordinates[0] ), 
                        Integer.parseInt( coordinates[1] ) );
                pointSet.add( point );
            }
            
            Block block = new Block( values[0].charAt( 0 ), pointSet );
            blocks.add( block );
        }
    
        return blocks;
    }
}
