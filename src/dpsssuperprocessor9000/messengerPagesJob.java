package dpsssuperprocessor9000;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @desc   Takes a series of TXT files and joins them into a single XML file
 * with proper headers for page breaks.
 * @author Michael Weigle <michael.weigle@gmail.com>
 */
public class messengerPagesJob implements Job
{
    @Override
    public boolean process( FileManipulator handler )
    {
        StringBuilder fileAsString = new StringBuilder();
        String header = String.format( "<EPB/>\n"
            + "<PB REF=\"%08d.jp2\" SEQ=\"%08d\" RES=\"72dpi\" FMT=\"JP2\" FTR=\"unspec\" N=\"%s\"/>\n"
            + "<DIV1 TYPE=\"\">\n", handler.numCurr, handler.numCurr );

        String currLine = "";

        try
        {
            while( ( currLine = handler.currReader.readLine() ) != null )
            {
                //do something?!
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
        if( !handler.fileType.equalsIgnoreCase( "txt" ) )
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
            new InputStreamReader( fis, Charset.forName( "windows-1252" ) ) );

        return true;
    }

    @Override
    public boolean initOutStream( FileManipulator handler )
    {
        if( handler.numCurr == handler.numStart )
        {
            handler.outFile = new File( handler.absPath + "\\" + handler.filePrefix + ".xml" );
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
        else // handler.numCurr > handler.numStart
        {
            if( handler.currWriter != null )
                return true;
            else
                return false;
        }
    }

    @Override
    public boolean print( String fileAsString, FileManipulator handler )
    {
        try
        {
            handler.currWriter.write( fileAsString );
            handler.currWriter.flush();

            if( handler.numCurr == handler.numEnd )
            {
                handler.currWriter.close();
            }

            handler.currReader.close();
        }
        catch( IOException ex )
        {
            System.err.println( "Error printing " + handler.relCurrFile );
            return false;
        }

        return true;
    }

}
