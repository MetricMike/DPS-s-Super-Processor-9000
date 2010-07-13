package dpsssuperprocessor9000;

/**
 * Digital Production Services' Super Processor 9000
 * @version 2010.0713
 * @author Michael Weigle <michael.weigle@gmail.com>
 */
public class Main
{
    /**
     * @param args the command line arguments (none currently supported)
     */
    public static void main( String[] args )
    {
        FileManipulator handler = new FileManipulator();
        handler.processLoop( new persNameInsertJob() );

        System.out.println( "Job completed." );
    }

}
