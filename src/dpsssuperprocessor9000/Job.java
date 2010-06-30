
package dpsssuperprocessor9000;

import java.io.*;

/**
 * @desc An implementing object contains instructions on how to process a single
 * file given to it.
 * @author Michael Weigle <michael.weigle@gmail.com>
 */
public interface Job
{
    boolean process( FileManipulator handler );
    boolean initInStream( FileManipulator handler );
    boolean initOutStream( FileManipulator handler );
}
