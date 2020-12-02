import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class FitToGridTest
{

    private static final Class<FitToGrid> fitToGridClass = FitToGrid.class;
    private static final char EMPTY_VALUE = '-';

    private static final Set<Point> pointsA = new HashSet<>();
    private static final Set<Point> pointsB = new HashSet<>();
    private static final Set<Point> pointsC = new HashSet<>();
    private static final Set<Point> pointsD = new HashSet<>();

    private static final int sideLength = 4;
    
    static
    {
        pointsA.add( new Point( 0, 0 ) );
        pointsA.add( new Point( 1, 0 ) );
        pointsA.add( new Point( 2, 0 ) );
        pointsA.add( new Point( 2, 1 ) );
        
        pointsB.add( new Point( 0, 0 ) );
        pointsB.add( new Point( 0, 1 ) );
        pointsB.add( new Point( 1, 1 ) );
        pointsB.add( new Point( 1, 2 ) );
        
        pointsC.add( new Point( 0, 0 ) );
        pointsC.add( new Point( 0, 1 ) );
        pointsC.add( new Point( 1, 1 ) );
        pointsC.add( new Point( 1, 2 ) );

        pointsD.add( new Point( 0, 0 ) );
        pointsD.add( new Point( 1, 0 ) );
        pointsD.add( new Point( 1, 1 ) );
        pointsD.add( new Point( 2, 1 ) );
        
    }
    
    private final FitToGrid m_fitToGrid = new FitToGrid( 4 );
    private Block m_blockA;
    private Block m_blockB;
    private Block m_blockC;
    private Block m_blockD;

    @Before
    public void setUp()
    {
        m_blockA = new Block( 'A', pointsA );
        m_blockB = new Block( 'B', pointsB );
        m_blockC = new Block( 'C', pointsC );
        m_blockD = new Block( 'D', pointsD );
    }
    
    @Test
    public void testNotTriedAll_AllInGrid() throws Exception
    {
        Method method = fitToGridClass.getDeclaredMethod( "notTriedAll", List.class, Point.class ); 
        method.setAccessible( true );
        
        LinkedList<Block> blockList = new LinkedList<>();
        blockList.add( m_blockA );
        blockList.add( m_blockB );
        blockList.add( m_blockC );
        blockList.add( m_blockD );
        
        m_blockA.setInGrid( true );
        m_blockB.setInGrid( true );
        m_blockC.setInGrid( true );
        m_blockD.setInGrid( true );
        
        Point point = new Point( 1,2 );
        m_blockA.addTriedStartPoint( point );
        m_blockB.addTriedStartPoint( point );
        m_blockC.addTriedStartPoint( point );
        m_blockD.addTriedStartPoint( point );
        
        boolean result = (boolean)method.invoke( m_fitToGrid, blockList, point );
        
        assertFalse( result );
    }

    @Test
    public void testNotTriedAll_TriedAllNothingInGrid() throws Exception
    {
        Method method = fitToGridClass.getDeclaredMethod( "notTriedAll", List.class, Point.class ); 
        method.setAccessible( true );
        
        LinkedList<Block> blockList = new LinkedList<>();
        blockList.add( m_blockA );
        blockList.add( m_blockB );
        blockList.add( m_blockC );
        blockList.add( m_blockD );
        
        Point point = new Point( 1,2 );
        m_blockA.addTriedStartPoint( point );
        m_blockB.addTriedStartPoint( point );
        m_blockC.addTriedStartPoint( point );
        m_blockD.addTriedStartPoint( point );
        
        boolean result = (boolean)method.invoke( m_fitToGrid, blockList, point );
        
        assertFalse( result );
    }

    @Test
    public void testNotTriedAll_TriedSomeNothingInGrid() throws Exception
    {
        Method method = fitToGridClass.getDeclaredMethod( "notTriedAll", List.class, Point.class ); 
        method.setAccessible( true );
        
        LinkedList<Block> blockList = new LinkedList<>();
        blockList.add( m_blockA );
        blockList.add( m_blockB );
        blockList.add( m_blockC );
        blockList.add( m_blockD );
        
        Point point = new Point( 1,2 );
        m_blockA.addTriedStartPoint( point );
        m_blockB.addTriedStartPoint( point );
        m_blockD.addTriedStartPoint( point );
        
        boolean result = (boolean)method.invoke( m_fitToGrid, blockList, point );
        
        assertTrue( result );
    }

    @Test
    public void testNotTriedAll_TriedNoneSomeInGrid() throws Exception
    {
        Method method = fitToGridClass.getDeclaredMethod( "notTriedAll", List.class, Point.class ); 
        method.setAccessible( true );
        
        LinkedList<Block> blockList = new LinkedList<>();
        blockList.add( m_blockA );
        blockList.add( m_blockB );
        blockList.add( m_blockC );
        blockList.add( m_blockD );
        
        Point point = new Point( 1,2 );

        m_blockB.setInGrid( true );
        m_blockC.setInGrid( true );
        
        boolean result = (boolean)method.invoke( m_fitToGrid, blockList, point );
        
        assertTrue( result );
    }

    @Test
    public void testNotTriedAll_TriedSomeSomeInGrid() throws Exception
    {
        Method method = fitToGridClass.getDeclaredMethod( "notTriedAll", List.class, Point.class ); 
        method.setAccessible( true );
        
        LinkedList<Block> blockList = new LinkedList<>();
        blockList.add( m_blockA );
        blockList.add( m_blockB );
        blockList.add( m_blockC );
        blockList.add( m_blockD );
        
        Point point = new Point( 1,2 );
        m_blockB.addTriedStartPoint( point );
        m_blockD.addTriedStartPoint( point );

        m_blockB.setInGrid( true );
        
        boolean result = (boolean)method.invoke( m_fitToGrid, blockList, point );
        
        assertTrue( result );
    }

    @Test
        public void testGetAvailableStartingPoints1() throws Exception
        {
            Method method = fitToGridClass.getDeclaredMethod( "getAvailableStartingPoints", Block.class ); 
            method.setAccessible( true );
            
            List<Point> resultPoints = (List<Point>)method.invoke( m_fitToGrid, m_blockA );
    
            assertEquals( 2, resultPoints.size() );
            
            Point expected = new Point( 0, 0 );
            assertEquals( expected, resultPoints.get( 0 ) );
    
            expected = new Point( 1, 0 );
            assertEquals( expected, resultPoints.get( 1 ) );
        }

    @Test
        public void testGetAvailableStartingPoints2() throws Exception
        {
            Method method = fitToGridClass.getDeclaredMethod( "getAvailableStartingPoints", Block.class ); 
            method.setAccessible( true );
            
            List<Point> resultPoints = (List<Point>)method.invoke( m_fitToGrid, m_blockB );
    
            assertEquals( 4, resultPoints.size() );
            
            Point expected = new Point( 0, 0 );
            assertEquals( expected, resultPoints.get( 0 ) );
    
            expected = new Point( 1, 0 );
            assertEquals( expected, resultPoints.get( 1 ) );
    
            expected = new Point( 2, 0 );
            assertEquals( expected, resultPoints.get( 2 ) );
    
            expected = new Point( 3, 0 );
            assertEquals( expected, resultPoints.get( 3 ) );
        }

    @Test
        public void testGetAvailableStartingPoints3() throws Exception
        {
            Method method = fitToGridClass.getDeclaredMethod( "getAvailableStartingPoints", Block.class ); 
            method.setAccessible( true );
            
            List<Point> resultPoints = (List<Point>)method.invoke( m_fitToGrid, m_blockD );
    
            assertEquals( 3, resultPoints.size() );
            
            Point expected = new Point( 0, 0 );
            assertEquals( expected, resultPoints.get( 0 ) );
    
            expected = new Point( 1, 0 );
            assertEquals( expected, resultPoints.get( 1 ) );
    
            expected = new Point( 2, 0 );
            assertEquals( expected, resultPoints.get( 2 ) );
        }

    @Test
    public void testFitWithStartingBlock() throws Exception
    {
        Method method = fitToGridClass.getDeclaredMethod( "fitWithStartingBlock", Block.class, Point.class, List.class ); 
        method.setAccessible( true );

        Set<Point> pointsB = new HashSet<>();
        pointsB.add( new Point( 0, 0 ) );
        pointsB.add( new Point( 0, 1 ) );
        pointsB.add( new Point( 0, 1 ) );
        pointsB.add( new Point( 1, 2 ) );

        Block blockB = new Block( 'B', pointsB );

        Set<Point> pointsD = new HashSet<>();
        pointsD.add( new Point( 0, 0 ) );
        pointsD.add( new Point( 0, 1 ) );
        pointsD.add( new Point( 0, 2 ) );
        pointsD.add( new Point( 0, 3 ) );

        Block blockD = new Block( 'D', pointsD );
        
        
        LinkedList<Block> blockList = new LinkedList<>();
        blockList.add( m_blockA );
        blockList.add( blockB );
        blockList.add( m_blockC );
        blockList.add( blockD );

        Block startBlock = m_blockA;
        
        Point point = new Point( 0, 0 );
        
        boolean result = (boolean)method.invoke( m_fitToGrid, startBlock, point, blockList );
        
        assertTrue( result );
    
    }

    @Test
    public void testFitWithStartingBlock_BlockDontFit() throws Exception
    {
        Method method = fitToGridClass.getDeclaredMethod( "fitWithStartingBlock", Block.class, Point.class, List.class ); 
        method.setAccessible( true );

        LinkedList<Block> blockList = new LinkedList<>();
        blockList.add( m_blockA );
        blockList.add( m_blockB );
        blockList.add( m_blockC );
        blockList.add( m_blockD );

        Block startBlock = m_blockA;
        
        Point point = new Point( 0, 0 );
        
        //test
        boolean result = (boolean)method.invoke( m_fitToGrid, startBlock, point, blockList );
        
        //assert
        assertFalse( result );
    
    }

    @Test
    public void testFitBlocks_NoFit() throws Exception
    {
        LinkedList<Block> blockList = new LinkedList<>();
        blockList.add( m_blockA );
        blockList.add( m_blockB );
        blockList.add( m_blockC );
        blockList.add( m_blockD );

        //test
        FittingResult result = m_fitToGrid.fitBlocks( blockList );
        
        //assert
        assertNull( result );
        
    }

    @Test
    public void testFitBlocks_Fits() throws Exception
    {
        LinkedList<Block> blockList = createTestData();

        //test
        FittingResult result = m_fitToGrid.fitBlocks( blockList );
        
        //assert
        char[][] grid = result.getGrid();
        for ( int y=0; y < sideLength; y++ )
        {
            for ( int x=0; x < sideLength; x++ )
            {
                assertNotEquals( EMPTY_VALUE, grid[x][y] );
            }
        }
    }
    
    @Test
    public void testGetNextFreePoint1() throws Exception
    {
        Method method = fitToGridClass.getDeclaredMethod( "getNextFreePoint" ); 
        method.setAccessible( true );
        
        //test
        Point result = (Point)method.invoke( m_fitToGrid );
        
        //assert
        Point expected = new Point( 0, 0 );
        assertEquals( expected, result );
    }

    @Test
    public void testGetNextFreePoint2() throws Exception
    {
        Method method = fitToGridClass.getDeclaredMethod( "getNextFreePoint" ); 
        method.setAccessible( true );
        
        Field field = fitToGridClass.getDeclaredField( "m_grid" );
        field.setAccessible( true );
        
        char[][] grid = createEmptyGrid();
        
        grid[0][0] = 'X';
        grid[1][0] = 'X';
        grid[2][0] = 'X';
        grid[3][0] = 'X';
        
        field.set( m_fitToGrid, grid );

        //test
        Point result = (Point)method.invoke( m_fitToGrid );
        
        //assert
        Point expected = new Point( 0, 1 );
        assertEquals( expected, result );
    }

    @Test
    public void testGetNextFreePoint3() throws Exception
    {
        Method method = fitToGridClass.getDeclaredMethod( "getNextFreePoint" ); 
        method.setAccessible( true );
        
        Field field = fitToGridClass.getDeclaredField( "m_grid" );
        field.setAccessible( true );
        
        char[][] grid = createEmptyGrid();
        
        grid[0][0] = 'X';
        grid[1][0] = 'X';
        grid[3][0] = 'X';
        
        field.set( m_fitToGrid, grid );

        //test
        Point result = (Point)method.invoke( m_fitToGrid );
        
        //assert
        Point expected = new Point( 2, 0 );
        assertEquals( expected, result );
    }

    @Test
    public void testGetNextFreePoint4() throws Exception
    {
        Method method = fitToGridClass.getDeclaredMethod( "getNextFreePoint" ); 
        method.setAccessible( true );
        
        Field field = fitToGridClass.getDeclaredField( "m_grid" );
        field.setAccessible( true );
        
        char[][] grid = createEmptyGrid();
        
        grid[0][0] = 'X';
        grid[1][0] = 'X';
        grid[2][0] = 'X';
        grid[3][0] = 'X';

        grid[0][1] = 'X';
        grid[1][1] = 'X';
        grid[2][1] = 'X';
        grid[3][1] = 'X';

        grid[0][2] = 'X';
        //grid[1][2] = 'X';
        grid[2][2] = 'X';
        grid[3][2] = 'X';
        
        field.set( m_fitToGrid, grid );

        //test
        Point result = (Point)method.invoke( m_fitToGrid );
        
        //assert
        Point expected = new Point( 1, 2 );
        assertEquals( expected, result );
    }

    @Test
    public void testGetNextFreePoint5() throws Exception
    {
        Method method = fitToGridClass.getDeclaredMethod( "getNextFreePoint" ); 
        method.setAccessible( true );
        
        Field field = fitToGridClass.getDeclaredField( "m_grid" );
        field.setAccessible( true );
        
        char[][] grid = createEmptyGrid();
        
        grid[0][0] = 'X';
        grid[1][0] = 'X';
        grid[2][0] = 'X';
        grid[3][0] = 'X';

        grid[0][1] = 'X';
        grid[1][1] = 'X';
        //grid[2][1] = 'X';
        //grid[3][1] = 'X';

        grid[0][2] = 'X';
        grid[1][2] = 'X';
        //grid[2][2] = 'X';
        grid[3][2] = 'X';

        grid[0][3] = 'X';
        grid[1][3] = 'X';
        //grid[2][3] = 'X';
        grid[3][3] = 'X';
        
        field.set( m_fitToGrid, grid );

        //test
        Point result = (Point)method.invoke( m_fitToGrid );
        
        //assert
        Point expected = new Point( 2, 1 );
        assertEquals( expected, result );
    }

    //################################################
    // NOT TESTS
    //################################################
    
    private static char[][] createEmptyGrid()
    {
        char[][] grid = new char[sideLength][sideLength];
        
        for ( int i=0; i < sideLength; i++ )
        {
            Arrays.fill( grid[i], EMPTY_VALUE );
        }
        
        return grid;
    }
    
    private static LinkedList<Block> createTestData()
    {
        LinkedList<Block> blockList = new LinkedList<>();

        Set<Point> pointsA = new HashSet<>();
        pointsA.add( new Point( 0, 0 ) );
        pointsA.add( new Point( 1, 0 ) );
        pointsA.add( new Point( 1, 1 ) );
        pointsA.add( new Point( 2, 1 ) );

        Set<Point> pointsB = new HashSet<>();
        pointsB.add( new Point( 0, 0 ) );
        pointsB.add( new Point( 0, 1 ) );
        pointsB.add( new Point( 0, 2 ) );
        pointsB.add( new Point( 1, 2 ) );

        Set<Point> pointsC = new HashSet<>();
        pointsC.add( new Point( 0, 0 ) );
        pointsC.add( new Point( 1, 0 ) );
        pointsC.add( new Point( 2, 0 ) );
        pointsC.add( new Point( 1, 1 ) );

        Set<Point> pointsD = new HashSet<>();
        pointsD.add( new Point( 0, 0 ) );
        pointsD.add( new Point( 1, 0 ) );
        pointsD.add( new Point( 1, 1 ) );
        pointsD.add( new Point( 1, -1 ) );

        Block blockA = new Block( 'A', pointsA );
        Block blockB = new Block( 'B', pointsB );
        Block blockC = new Block( 'C', pointsC );
        Block blockD = new Block( 'D', pointsD );

        blockList.add( blockA );
        blockList.add( blockB );
        blockList.add( blockC );
        blockList.add( blockD );

        return blockList;
    }

}
