package dpsssuperprocessor9000;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @desc   Takes an XML file and inserts <speaker>, <sp>, and <p> tags where
 * applicable.
 * @author Michael Weigle <michael.weigle@gmail.com>
 */
public class speakerInsertJob implements Job
{
    private String[] delimiters =
    {
        "\t", "said:", "said :", "said,", "said ,", "-", " -", "&#x2014;"
    };

    @Override
    public boolean process( FileManipulator handler )
    {
        StringBuilder fileAsString = new StringBuilder();
        String currLine = "";

        try
        {
            while( (currLine = handler.currReader.readLine()) != null )
            {
                currLine = findSpeakers( currLine );
                fileAsString.append( currLine ).append( "\n" );
            }
        }
        catch( IOException ioe )
        {
            System.err.println( "Error reading from: " + handler.relCurrFile );
            return false;
        }

        return print( fileAsString.toString(), handler );
    }

    @Override
    public boolean initInStream( FileManipulator handler )
    {
        if( !handler.fileType.equalsIgnoreCase( "xml" ) )
            return false;

        handler.inFile = new File( handler.absCurrFile );
        FileInputStream fis = null;

        try
        {
            fis = new FileInputStream( handler.inFile );
        }
        catch( FileNotFoundException ex )
        {
            return false;
        }

        handler.currReader = new BufferedReader(
            new InputStreamReader( fis, Charset.forName( "UTF-8" ) ) );

        return true;
    }

    @Override
    public boolean initOutStream( FileManipulator handler )
    {
        handler.outFile = new File( "$$$$$.tmp" );
        FileOutputStream fos = null;

        try
        {
            fos = new FileOutputStream( handler.outFile );
        }
        catch( Exception ex )
        {
            return false;
        }

        handler.currWriter = new BufferedWriter(
            new OutputStreamWriter( fos, Charset.forName( "UTF-8" ) ) );

        return true;
    }

    @Override
    public boolean print( String fileAsString, FileManipulator handler )
    {
        try
        {
            handler.currWriter.write( fileAsString );
            handler.currWriter.close();

            handler.currReader.close();
            handler.inFile.delete();
            handler.outFile.renameTo( handler.inFile );
        }
        catch( IOException ex )
        {
            System.err.println( "Error printing " + handler.relCurrFile );
            return false;
        }

        return true;
    }

    /**
     * Calls findSpeakers() at the beginning of the STring.
     * 
     * @param currLine the String to search
     * @return the modified (if necessary) searched String
     */
    private String findSpeakers( String currLine )
    {
        return findSpeakers( 0, currLine );
    }

    /**
     * Searches a line for patterns matching "</persName>" + delimiters and
     * surrounds with <sp>, <p>, and <speaker> tags.
     *
     * @param startIndex an int representing where to begin
     * @param currLine the String to search
     * @return the modified (if necessary) searched String
     */
    private String findSpeakers( int startIndex, String currLine )
    {
        if( (startIndex >= 0) && (startIndex < currLine.length()) )
        {
            int[] positions = new int[3];
            positions = findIndices( currLine, positions );

            if( positions[2] != -1 )
            {
                StringBuilder tmpString = new StringBuilder( currLine );

                tmpString.insert( positions[0], "</p></sp><sp><speaker>" );
                positions = incrementPositions( positions, 22 );

                tmpString.insert( positions[1], "</speaker>" );
                positions = incrementPositions( positions, 10 );

                tmpString.insert( positions[2], "<p>" );
                positions = incrementPositions( positions, 3 );

                currLine = tmpString.toString();
            }
        }

        return currLine;
    }

    /**
     * Increments every int in an array by a certain amount.
     *
     * I feel bad for making this into its own method, but I think I'm going to
     * end up calling it a lot.
     *
     * @param positions an int array describing important line positions
     * @param increment an int describing how much to increment by
     * @return a modified (if necessary) positions array
     */
    private int[] incrementPositions( int[] positions, int increment )
    {
        for( int i = 0; i < positions.length; i++ )
        {
            positions[i] += increment;
        }

        return positions;
    }

    /**
     * Searches the currentLine for indices describing the start and end of
     * <persName> tags and delimiters (if present).
     * 
     * @param currline the String to search
     * @param nameEnd an int describing the index directly following a
     * </persName> tag
     * @return an int describing the index of a 
     */
    private int[] findIndices( String currLine, int[] positions )
    {
        positions[0] = currLine.indexOf( "<persName>" );

        if( positions[0] == -1 )
        {
            positions[1] = -1;
            positions[2] = -1;
        }
        else // positions[0] != -1
        {
            positions[1] = currLine.indexOf( "</persName>" ) + 11;
            int tmpUpBound = currLine.indexOf( "<persName>", positions[1] );

            for( String delimiter : delimiters )
            {
                int tmpInt = currLine.indexOf( delimiter, positions[1] );

                if( tmpInt != -1 )
                {
                    if( tmpUpBound != -1 && tmpInt < tmpUpBound )
                    {
                        positions[2] = tmpInt + delimiter.length();
                        break;
                    }
                    else if( tmpUpBound == -1 )
                    {
                        positions[2] = tmpInt + delimiter.length();
                        break;
                    }
                    else // tmpUpBound != -1 && tmpInt >= tmpUpBound)
                    {
                        positions[2] = -1;
                        break;
                    }
                }
                else
                    positions[2] = -1;
            }

        }

        return positions;
    }

}
