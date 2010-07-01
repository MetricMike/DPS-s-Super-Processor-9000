
package dpsssuperprocessor9000;

/**
 * @desc An implementing object contains instructions on how to process a single
 * file given to it.
 * @author Michael Weigle <michael.weigle@gmail.com>
 */
public interface Job
{
    /**
     * Contains instructions for how to manipulate a single file. Assumes TXT
     * files are windows-1252 encoded, XML files are UTF-8 encoded, and other
     * file types are binary encoded.
     * 
     * @param handler the FileManipulator instance containing token information
     * @return true if success, false is failure
     */
    boolean process( FileManipulator handler );

    /**
     * Initializes an input stream according to what's needed. Assumes TXT files
     * are windows-1252 encoded, XML files are UTF-8 encoded, and other file
     * types are binary encoded.
     *
     * @param handler the FileManipulator instance containing token information
     * @return true if success, false is failure
     */
    boolean initInStream( FileManipulator handler );
    
    /**
     * Initializes an output stream according to what's needed. Assumes TXT
     * files are windows-1252 encoded, XML files are UTF-8 encoded, and other
     * file types are binary encoded.
     * 
     * @param handler the FileManipulator instance containing token information
     * @return true if success, false is failure
     */
    boolean initOutStream( FileManipulator handler );
}
