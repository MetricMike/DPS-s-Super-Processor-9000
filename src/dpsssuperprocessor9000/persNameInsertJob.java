package dpsssuperprocessor9000;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @desc   Takes an XML file and inserts <persName> tags where applicable.
 * @author Michael Weigle <michael.weigle@gmail.com>
 */
public class persNameInsertJob implements Job
{
    @Override
    public boolean process( FileManipulator handler )
    {
        StringBuilder fileAsString = new StringBuilder();
        String currLine = "";

        try
        {
            while( (currLine = handler.currReader.readLine()) != null )
            {
                currLine = findMister( currLine );
                currLine = findMessrs( currLine );
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
     * Calls findMister() at the beginning of the String.
     *
     * @param currLine the String to search
     * @return the modified (if necessary) searched String
     */
    private String findMister( String currLine )
    {
        return findMister( 0, currLine );
    }

    /**
     * Searches a line for instances of "Mr. " and places <persName> tags around
     * "Mr." and the accompanying name.
     *
     * @param startIndex an int representing where to begin
     * @param currLine the String to search
     * @return the modified (if necessary) searched String
     */
    private String findMister( int startIndex, String currLine )
    {
        if( (startIndex >= 0) && (startIndex < currLine.length()) )
        {
            int nameStart = currLine.indexOf( "Mr. ", startIndex );

            if( nameStart != -1 )
            {
                int nameEnd = nameStart + 4;

                boolean moreToName = true;

                while( moreToName )
                {
                    if( currLine.charAt( nameEnd ) == '.' )
                    {
                        try
                        {
                            if( Character.isUpperCase( currLine.charAt( nameEnd + 2 ) ) )
                            {
                                if( currLine.charAt( nameEnd + 3 ) == '.' || Character.isUpperCase( currLine.charAt( nameEnd + 3 ) ) )
                                    nameEnd = nameEnd + 2;
                                else // Reached end of sentence
                                    moreToName = false;
                            }
                            else // Probably end of sentence
                                moreToName = false;
                        }
                        catch( StringIndexOutOfBoundsException ex ) // End of line reached
                        {
                            moreToName = false;
                        }
                    }
                    else if( Character.isLowerCase( currLine.charAt( nameEnd ) ) )
                    {
                        if( Character.isUpperCase( currLine.charAt( nameEnd + 1 ) ) )
                            nameEnd++;
                        else
                        {
                            nameEnd--;
                            moreToName = false;
                        }
                    }
                    else if( Character.isUpperCase( currLine.charAt( nameEnd ) ) )
                        nameEnd++;
                    else if( currLine.charAt( nameEnd ) == ' ' )
                    {
                        if( Character.isUpperCase( currLine.charAt( nameEnd + 1 ) ) )
                            nameEnd++;
                        else
                            moreToName = false;
                    }
                    else // Is anything other than a letter or '.'
                        moreToName = false;
                }

                if( nameEnd > nameStart + 4 ) // If nameEnd has been extended
                {
                    StringBuilder tmpString = new StringBuilder( currLine );

                    tmpString.insert( nameStart, "<persName>" );
                    nameEnd = nameEnd + 10;

                    tmpString.insert( nameEnd, "</persName>" );
                    nameEnd = nameEnd + 11;

                    currLine = tmpString.toString();
                }

                currLine = findMister( nameEnd, currLine );
            }

        }

        return currLine;
    }

    /**
     * Calls findMessrs() at the beginning of the String.
     *
     * @param currLine the String to search
     * @return the modified (if necessary) searched String
     */
    private String findMessrs( String currLine )
    {
        return findMessrs( 0, currLine );
    }

    /**
     * Searches a line for instances of "Messrs. " and places <persName> tags
     * around the accompanying names.
     *
     * @param startIndex an int representing where to begin
     * @param currLine the String to search
     * @return the modified (if necessary) searched String
     */
    private String findMessrs( int startIndex, String currLine )
    {
        if( (startIndex >= 0) && (startIndex < currLine.length()) )
        {
            int nameStart = currLine.indexOf( "Messrs. ", startIndex );

            if( nameStart != -1 )
            {
                nameStart = nameStart + 8;
                int nameEnd = nameStart;

                boolean moreToName = true;

                while( moreToName )
                {
                    if( currLine.charAt( nameEnd ) == '.' )
                    {
                        try
                        {
                            if( Character.isUpperCase( currLine.charAt( nameEnd + 2 ) ) )
                            {
                                if( currLine.charAt( nameEnd + 3 ) == '.' || Character.isUpperCase( currLine.charAt( nameEnd + 3 ) ) )
                                    nameEnd = nameEnd + 2;
                                else // Reached end of sentence
                                    moreToName = false;
                            }
                            else // Probably end of sentence
                                moreToName = false;
                        }
                        catch( StringIndexOutOfBoundsException ex ) // End of line reached
                        {
                            moreToName = false;
                        }
                    }
                    else if( currLine.charAt( nameEnd ) == ',' )
                    {
                        if( Character.isUpperCase( currLine.charAt( nameEnd + 2 ) ) )
                        {
                            currLine = insertTag( currLine, nameStart, nameEnd );
                            nameStart = nameEnd + 21 + 2;
                            nameEnd = nameStart;
                        }
                        else if( ( nameEnd + 1 ) == currLine.indexOf( " and ", nameStart ) )
                        {
                            currLine = insertTag( currLine, nameStart, nameEnd );
                            nameStart = nameEnd + 21 + 5;
                            nameEnd = nameStart;
                        }
                        else
                            moreToName = false;
                    }
                    else if( Character.isLowerCase( currLine.charAt( nameEnd ) ) )
                    {
                        if( Character.isUpperCase( currLine.charAt( nameEnd + 1 ) ) )
                            nameEnd++;
                        else
                        {
                            nameEnd--;
                            moreToName = false;
                        }
                    }
                    else if( Character.isUpperCase( currLine.charAt( nameEnd ) ) )
                        nameEnd++;
                    else if( currLine.charAt( nameEnd ) == ' ' )
                    {
                        if( nameEnd == currLine.indexOf( " and ", nameStart ) )
                        {
                            currLine = insertTag( currLine, nameStart, nameEnd );
                            nameStart = nameEnd + 21 + 5;
                            nameEnd = nameStart;
                        }
                        else if( nameEnd == currLine.indexOf( " of ", nameStart ) )
                        {
                            currLine = insertTag( currLine, nameStart, nameEnd );
                            nameStart = currLine.indexOf( ", ", nameEnd ) + 2;

                            if( nameStart == currLine.indexOf( "and ", nameEnd ) )
                                nameStart = nameStart + 4;

                            nameEnd = nameStart;
                        }
                        else if( Character.isUpperCase( currLine.charAt( nameEnd + 1 ) ) )
                            nameEnd++;
                        else
                            moreToName = false;
                    }
                    else // Is anything other than a letter, ' ', ',', or '.'
                        moreToName = false;
                }

                if( nameEnd > nameStart ) // If nameEnd has been extended
                {
                    currLine = insertTag( currLine, nameStart, nameEnd );
                    nameEnd = nameEnd + 21;
                }
                
                currLine = findMessrs( nameEnd, currLine );
            }

        }

        return currLine;
    }

    private String insertTag( String currLine, int nameStart, int nameEnd )
    {
        StringBuilder tmpString = new StringBuilder( currLine );

        tmpString.insert( nameStart, "<persName>" );
        nameEnd = nameEnd + 10;

        tmpString.insert( nameEnd, "</persName>" );
        nameEnd = nameEnd + 11;

        return tmpString.toString();
    }

}
