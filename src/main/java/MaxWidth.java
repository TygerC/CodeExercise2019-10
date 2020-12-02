import java.util.Comparator;

public class MaxWidth implements Comparator<Block>
{

    @Override
    public int compare( Block block1, Block block2 )
    {
        return Integer.compare( block1.getBaseWidth(), block2.getBaseWidth() );
    }

}
