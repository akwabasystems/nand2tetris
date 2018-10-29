
package com.akwabasystems.parsing;

import com.akwabasystems.model.OutputType;
import java.io.File;


/**
 * An interface that defines the methods necessary for analyzing Jack files. The API operates on a given source, where
 * that source is either a Jack file or a directory containing one or more such files. It also sets the output type,
 * which affects the final contents and format of the output file.
 */
public interface Analyzer {
    
    
    /**
     * Analyzes the given file
     * 
     * @param inputFile     the file to analyze
     * @return a reference to the current instance of the class that implements this interface
     */
    Analyzer parse(File inputFile);
    
    
    /**
     * Sets the output type for this analyzer, which specifies whether to output XML tokens, the XML tree of the code,
     * or the generate code.
     * 
     * @param outputType        the output type to set for this analyzer
     * @return a reference to the current instance of the class that implements this interface
     */
    Analyzer setOutputType(OutputType outputType);

}
