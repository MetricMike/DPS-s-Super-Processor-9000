package dpsssuperprocessor9000;

import java.io.*;

/**
 * @desc   Contains some common code to make file handling easier.
 * @author Michael Weigle <michael.weigle@gmail.com>
 */
public class FileManipulator
{
    private String firstAbsName;
    private String lastAbsName;

    private String absPath;
    private String numWidth;
    private String fileType;

    public FileManipulator()
    {
        firstAbsName = getFileName( "first" );
        lastAbsName = getFileName( "last" );

        Tokenize();
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

    /**
     * Splits a given file up to fill the absPath, numWidth, and fileType variables.
     */
    private void Tokenize()
    {
        throw new UnsupportedOperationException( "Not yet implemented" );
    }
}
