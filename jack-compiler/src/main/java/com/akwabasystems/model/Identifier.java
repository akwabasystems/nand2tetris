
package com.akwabasystems.model;


/**
 * A class that encapsultes the details of a symbol found it a program: name, type, type, index, and kind
 */
public class Identifier {
    private final String name;
    private final String type;
    private int index;
    private IdentifierKind kind = IdentifierKind.VAR;
   
   
    /**
     * @constructor
     * 
     * @param name      the name of this identifier
     * @param type      the type of this identifier
     * @param kind      the kind of this identifier
     */
    public Identifier(String name, String type, IdentifierKind kind) {
        this.name = name;
        this.type = type;
        this.kind = kind;
    }


    /**
     * Sets the index for this identifier
     * 
     * @param index     the index to set for this identifier 
     */
    public void setIndex(int index) {
        this.index = index;
    }
    

    /**
     * Returns the type for this identifier
     * 
     * @return the type for this identifier 
     */
    public int getIndex() {
        return index;
    }
    
    
    /**
     * Returns the name for this identifier
     * 
     * @return the name for this identifier 
     */
    public String getName() {
        return name;
    }
    
    
    /**
     * Returns the type for this identifier
     * 
     * @return the type for this identifier 
     */
    public String getType() {
        return type;
    }
    
    
    /**
     * Returns the kind for this identifier
     * 
     * @return the kind for this identifier 
     */
    public IdentifierKind getKind() {
        return kind;
    }


    /**
     * Returns a string representation of this identifier
     * 
     * @return a string representation of this identifier
     */
    @Override
    public String toString() {
        return String.format("Identifier { name: %s, type: %s, kind: %s, index: %s }", name, type, kind, index);
    } 
   
}
