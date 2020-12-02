/**
 * Represent a point in x-y coordinate plane.
 */
public class Point
{
    private int m_x;
    private int m_y;
    
    /**
     * Creates a new point with the given coordinates.
     * 
     * @param x
     * @param y
     */
    public Point( int x, int y )
    {
        this.m_x = x;
        this.m_y = y;
    }
    
    public int getX()
    {
        return m_x;
    }
    
    public int getY()
    {
        return m_y;
    }

    public void setX( int x )
    {
        m_x = x ;
    }
    
    public void setY( int y )
    {
        m_y = y;
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + m_x;
        result = prime * result + m_y;
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
        Point other = (Point)obj;
        if ( m_x != other.m_x )
            return false;
        if ( m_y != other.m_y )
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "(" + m_x + "," + m_y + ")";
    }
}
