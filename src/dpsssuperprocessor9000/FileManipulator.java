package dpsssuperprocessor9000;

import java.io.*;

/**
 * @desc   Contains some common code to make file handling easier.
 * @author Michael Weigle <michael.weigle@gmail.com>
 */
public class FileManipulator
{
    private String firstAbsName = null;
    private String absPath = null;
    private String fileType = null;
    private int numStart = 0;
    private int numEnd = 0;
    private int numWidth = 0;

    public FileManipulator()
    {
        firstAbsName = requestInfo( "the absolute filename for the first file" );
        absPath = firstAbsName.substring( 0, firstAbsName.lastIndexOf( '\\' ) );
        getNumAndType();
    }

    /**
     * Requests an absolute filename from the user via the console.
     *
     * @param target description of the information being requested (e.g. "the absolute filename for the first file")
     * @return a String containing the absolute path indicated by the user
     */
    private static String requestInfo( String target )
    {
        System.out.println( "Please enter " + target + " :" );

        BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
        String targetName = null;

        try
        {
            targetName = br.readLine();
        }
        catch( IOException ioe )
        {
            System.out.println( "IO Exception when reading " + target + "file." );
            targetName = null;
        }

        return targetName;
    }

    /**
     * Detects fileType, numStart, numEnd, and numWidth if applicable.
     *
     * @throws UnsupportedOperationException if called on a filetype other than TIF, PSD, JPG, JP2, TXT, or XML
     */
    private void getNumAndType()
    {
        int typeDot = firstAbsName.lastIndexOf( '.' );

        fileType = firstAbsName.substring( typeDot + 1 ).toLowerCase();

        if( !fileType.equalsIgnoreCase( "tif" ) && !fileType.equalsIgnoreCase( "psd" )
            && !fileType.equalsIgnoreCase( "jpg" )
            && !fileType.equalsIgnoreCase( "jp2" )
            && !fileType.equalsIgnoreCase( "txt" )
            && !fileType.equalsIgnoreCase( "xml" ) )
            throw new UnsupportedOperationException( "File Type \"" + fileType + "\" is unsupported." );

        int numDot = firstAbsName.lastIndexOf( '.', typeDot - 1 );

        try
        {
            numStart = Integer.parseInt( firstAbsName.substring( numDot + 1, typeDot ) );
        }
        catch( NumberFormatException nfe )
        {
            numStart = -1;
            numEnd = -1;
            numWidth = -1;
        }

        while( numEnd == 0 )
        {
            try
            {
                numEnd = Integer.parseInt( requestInfo( "the item number of the last file" ) );
            }
            catch( NumberFormatException nfe )
            {
                System.out.println( "Number Format Exception - a non-number was entered." );
            }
        }

        if( numWidth == 0 )
            numWidth = typeDot - numDot - 1;
    }

}
