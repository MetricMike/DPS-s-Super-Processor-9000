package dpsssuperprocessor9000;

import java.io.*;

/**
 * Digital Production Services' Super Processor 9000
 * @version 2010.0623
 * @author Michael Weigle <michael.weigle@gmail.com>
 */
public class Main
{
    /**
     * @param args the command line arguments (none currently supported)
     */
    public static void main( String[] args )
    {
        String firstFileName = getFileName( "first" );
        String lastFileName = getFileName( "last" );
        // Tokenize into relevant fields
    }

    /**
     * Requests an absolute filename from the user via the console.
     *
     * @param target description of the type of file being requested (e.g. "first" or "last")
     * @return a String containing the absolute path indicated by the user
     */
    private static String getFileName( String target )
    {
        System.out.println( "Please enter the absolute filename for the " + target + " file" );

        BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
        String targetName = null;

        try
        {
            targetName = br.readLine();
        }
        catch( IOException ioe )
        {
            System.out.println( "IO Error when reading " + target + "file." );
            targetName = null;
        }

        return targetName;
    }

}
