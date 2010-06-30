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
    private String filePrefix = null;

    private int numStart = 0;
    private int numEnd = 0;
    private int numWidth = 0;

    private String realNum = null;
    private String absCurrFile = null;
    private String relCurrFile = null;

    private File inFile = null;
    private Reader currReader = null;
    private File outFile = null;
    private Writer currWriter = null;

    public FileManipulator()
    {
        firstAbsName = requestInfo( "the absolute filename for the first file" );
        absPath = firstAbsName.substring( 0, firstAbsName.lastIndexOf( '\\' ) );
        tokenize();
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
     * Detects filePrefix, fileType, numStart, numEnd, and numWidth if applicable.
     *
     * @throws UnsupportedOperationException if called on a filetype other than TIF, PSD, JPG, JP2, TXT, or XML
     */
    private void tokenize()
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
                System.err.println( "Number Format Exception - a non-number was entered." );
            }
        }

        if( numWidth == 0 )
            numWidth = typeDot - numDot - 1;

        filePrefix = firstAbsName.substring( firstAbsName.lastIndexOf( "\\" ) + 1, numDot );
    }

    /**
     * Runs a loop around the files indicated by the user according to the passed Job.
     * 
     * @param j an object conforming to the Job Interface which manipulates a single file
     */
    public void processLoop( Job j )
    {
        for( int i = numStart; i <= numEnd; i++ )
        {
            if( i != -1 )
            {
                realNum = String.format( "%0" + numWidth + "d", i );
                absCurrFile = absPath + filePrefix + "." + realNum + fileType;
                relCurrFile = filePrefix + "." + realNum + fileType;
            }
            else
            {
                absCurrFile = null;
                relCurrFile = null;
            }

            if( !j.initInStream( this ) )
            {
                System.err.println( "Error initializing inputstream for " + relCurrFile );
                continue;
            }

            if( !j.initOutStream( this ) )
            {
                System.err.println( "Error initializing outputstream for " + relCurrFile );
                continue;
            }

            if( !j.process( this ) )
            {
                System.err.println( "Error processing " + relCurrFile );
                continue;
            }

        }

        System.out.println( "Job finished" );
    }
}
