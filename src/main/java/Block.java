import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a block.
 */
public class Block implements Comparable<Block>
{
    private Set<Point> m_points;
    private char m_name;
    private int m_baseWidth;
    private List<Point> m_triedStartPoints = new ArrayList<>();
    private boolean m_isInGrid = false;
    private boolean m_hasBeenFirst = false;
    
    /**
     * Creates a new block with the given name and points.
     * 
     * @param name
     * @param points
     */
    public Block( char name, Set<Point> points )
    {
        m_name = name;
        m_points = points;
        findBaseWidth();
    }
    
    /**
     * Creates a new instance of block by copying values from the given block.  
     * 
     * @param block
     */
    public Block( Block block )
    {
        m_name = block.m_name;
        m_baseWidth = block.m_baseWidth;
        m_points = new HashSet<>();
        m_points.addAll( block.m_points );
    }

    public int getSize()
    {
        return m_points.size();
    }
    
    public boolean isInGrid()
    {
        return m_isInGrid;
    }

    public void setInGrid( boolean isInGrid )
    {
        this.m_isInGrid = isInGrid;
    }

    public boolean hasBeenFirst()
    {
        return m_hasBeenFirst;
    }

    public void setHasBeenFirst( boolean hasBeenFirst )
    {
        this.m_hasBeenFirst = hasBeenFirst;
    }

    /**
     * Calculates the width of the block at its lowest point (i.e. smallest value of y).
     */
    private void findBaseWidth()
    {
        m_baseWidth = (int)(long)m_points
                .stream()
                .collect( Collectors.groupingBy( p -> p.getY(), Collectors.counting() ) )
                .entrySet()
                .stream()
                .min( Comparator.comparing( Map.Entry::getKey ) )
                .get()
                .getValue();
    }

    
    public Set<Point> getPoints()
    {
        return m_points;
    }

    public int getBaseWidth()
    {
        return m_baseWidth;
    }

    /**
     * Get the smallest value of x among the points of the block.
     * 
     * @return
     */
    public int getMinXvalue()
    {
        return m_points.stream().map( p -> p.getX() ).min( Comparator.naturalOrder() ).get();
    }
    
    /**
     * Gets the point with smallest value of y. If there are several, returns the one with
     * smallest value of x.
     * 
     * @return
     */
    public Point getLowestLeftPoint()
    {
        Comparator<Point> minX = Comparator.comparing( Point::getX );
        
        return m_points
                .stream().
                filter( p -> p.getY() == getMinYvalue() )
                .sorted( minX )
                .findFirst()
                .get();
    }
    
    /**
     * Get the smallest value of y among the points of the block.
     * 
     * @return
     */
    private int getMinYvalue()
    {
        return m_points.stream().map( p -> p.getY() ).min( Comparator.naturalOrder() ).get();
    }

    /* *
     * Compares blocks based on the width of the smallest y.
     * 
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo( Block other )
    {
        return Integer.compare( other.getBaseWidth(), this.getBaseWidth() );
    }

    /**
     * Shifts the value of x-coordinates on all points by the given parameter.
     * 
     * @param x
     */
    public void shiftXvalues( int x )
    {
        m_points.forEach( p -> p.setX( p.getX() + x ) );
    }

    /**
     * Shifts the value of y-coordinates on all points by the given parameter.
     * 
     * @param y
     */
    public void shiftYvalues( int y )
    {
        m_points.forEach( p -> p.setY( p.getY() + y ) );
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + m_name;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Block other = (Block)obj;
        if ( m_name != other.m_name )
            return false;
        return true;
    }

    public char getName()
    {
        return m_name;
    }

    /**
     * Add a new point to the list of starting points tried on this block.
     * 
     * @param point
     */
    public void addTriedStartPoint( Point point )
    {
        m_triedStartPoints.add( point );
    }

    /**
     * Checks if this block has been tried to fit to the given point. 
     * 
     * @param point
     * @return true is block has been tried to the given point, false otherwise
     */
    public boolean hasTriedStartPointBefore( Point point )
    {
        return m_triedStartPoints.contains( point );
    }

    /**
     * Clears tried starting points and sets block not the be in the grid.
     */
    public void cleanTried()
    {
        setInGrid( false );
        m_triedStartPoints.clear();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Block [m_points=" + m_points 
                + ", m_name=" + m_name 
                + ", m_baseWidth=" + m_baseWidth 
                + ", m_triedStartPoints=" + m_triedStartPoints 
                + ", m_isInGrid=" + m_isInGrid 
                + ", m_hasBeenFirst=" + m_hasBeenFirst + "]";
    }

    /**
     * Create a string representation of the block.
     * 
     * @return
     */
    public String getBlockAsString()
    {
        List<Point> orderedPoints = m_points.stream().sorted( new Comparator<Point>()
        {
            @Override
            public int compare( Point p1, Point p2 )
            {
                int result = Integer.compare( p1.getX(), p2.getX() );
                if ( result == 0 )
                {
                    result = Integer.compare( p1.getY(), p2.getY() );
                }
                return result;
            }
        } ).collect( Collectors.toList() );
        
        StringBuilder sb = new StringBuilder( this.m_name + ":");
        
        orderedPoints.forEach( p -> sb.append( p.getX() + "," + p.getY() + ";" ) );
        
        sb.deleteCharAt( sb.length() - 1 );
        
        return sb.toString();
    }
}
