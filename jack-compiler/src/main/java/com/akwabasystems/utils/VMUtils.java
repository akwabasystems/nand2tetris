
package com.akwabasystems.utils;

import java.io.File;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;


/**
 * A class that contains a collection of utility methods used for various use cases.
 */
public class VMUtils {
    
    
    /**
     * Removes all comments from the given command
     * 
     * @param command           the command from which to remove the comments
     * @return a version of the given command stripped of all comments
     */
    public static String stripComments(String command) {
        Pattern pattern = Pattern.compile("\\/\\/[^\n\r]*|/\\*([^*]|[\n\r]|(\\*+([^*/]|[\\r\\n])))*\\*+/", 
                Pattern.MULTILINE);
        return command.replaceAll(pattern.pattern(), "").trim();
    }
    
    
    /**
     * Returns true if the given file has the specified extension; otherwise, returns false
     * 
     * @param file          the file to check
     * @param extension     the extension to match
     * @return true if the given file has the specified extension; otherwise, returns false
     */
    public static boolean hasExtension(File file, String extension) {
        String[] parts = StringUtils.split(file.getName(), ".");
        return (parts[parts.length - 1].equals(extension));
    }

}
