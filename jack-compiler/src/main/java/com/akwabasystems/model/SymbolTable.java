
package com.akwabasystems.model;

import org.json.JSONObject;

/**
 * A class that maintains a mapping between symbols found in a program and the identifier properties needed for
 * their compilation: type, kind, and running index.
 */
public class SymbolTable {
    private ClassScope classScope;
    private SubroutineScope subroutineScope;
    private Scope currentScope;
    
    
    /**
     * Starts the uppermost class scope and sets it as the current scope
     * 
     * @param name      the name of the class 
     */
    public void startClass(String name) {
        classScope = new ClassScope(name);
        currentScope = classScope;
    }
    
    
    /**
     * Starts a subroutine scope and sets it as the current scope
     * 
     * @param name      the name of the subroutine
     */
    public void startSubroutine(String name) {
        subroutineScope = new SubroutineScope(name);
        currentScope = subroutineScope;
        
        if (classScope != null) {
            subroutineScope.setParentScope(classScope);
        }
    }
    

    /**
     * Defines a symbol in the respective scope based on its type
     * 
     * @param name          the name of the symbol to define
     * @param type          the type of symbol to define
     * @param kind          the kind of symbol of define
     */
    public void define(String name, String type, IdentifierKind kind) {
        define(name, type, kind, null);
    }


    /**
     * Defines a symbol in the respective scope based on its type
     * 
     * @param name          the name of the symbol to define
     * @param type          the type of symbol to define
     * @param kind          the kind of symbol of define
     */
    public void define(String name, String type, IdentifierKind kind, JSONObject attributes) {
        boolean hasClassScope = (kind == IdentifierKind.STATIC || kind == IdentifierKind.FIELD ||
            kind == IdentifierKind.METHOD);
        
        /**
         * Check whether we're defining a class method symbol, which stores attributes such as the
         * argument length and the return type
         */
        if (hasClassScope && attributes != null) {
            classScope.define(name, type, kind, attributes);
        } else if(hasClassScope) {
            classScope.define(name, type, kind);
        } else {
            subroutineScope.define(name, type, kind);
        }
    }


    /**
     * Returns the total variable count for symbols of the given kind
     * 
     * @param kind          the variable kind for which to return the count
     * @return the total variable count for symbols of the given kind
     */
    public int varCount(IdentifierKind kind) {
        return currentScope.varCount(kind);
    } 
    
    
    /**
     * Returns the kind for the given variable name
     * 
     * @param name          the name of the variable for which to retrieve the kind
     * @return the kind for the given variable name
     */
    public IdentifierKind kindOf(String name) {
        return currentScope.kindOf(name);
    }
    
    
    /**
     * Returns the type for the given variable name
     * 
     * @param name          the name of the variable for which to retrieve the type
     * @return the type for the given variable name
     */
    public String TypeOf(String name) {
        return currentScope.typeOf(name);
    }
    
    
    /**
     * Returns the index for the given variable name
     * 
     * @param name          the name of the variable for which to retrieve the index
     * @return the index for the given variable name
     */
    public int IndexOf(String name) {
        return currentScope.indexOf(name);
    }
    
    
    /**
     * Returns the current class scope
     * 
     * @return the current class scope
     */
    public Scope currentClassScope() {
        return classScope;
    }
    
    
    /**
     * Returns the current subroutine scope
     * 
     * @return the current subroutine scope
     */
    public Scope currentSubroutineScope() {
        return subroutineScope;
    }

    
    /**
     * Outputs the description of the current symbol table
     */
    public void describe() {
        if (classScope != null) {
            System.out.printf("%s\n", classScope.describe());
        }
        
        if (subroutineScope != null) {
            System.out.printf("%s\n", subroutineScope.describe());
        }
    }
    
}
