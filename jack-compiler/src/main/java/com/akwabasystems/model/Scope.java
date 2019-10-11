
package com.akwabasystems.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONObject;


/**
 * A class that defines a scope (either a class or a subroutine scope) in which to store the different symbols in a
 * program. It provides an API for defining symbols with their identifiers. It also provides an API for resolving
 * those symbols, which involves looking up the symbol in its scope chain.
 */
public abstract class Scope {
    protected String name;
    protected ScopeType type;
    protected Scope parentScope;
    protected ConcurrentMap<String,Identifier> symbolTable;
    protected ConcurrentMap<String,AtomicLong> varCount;


    /**
     * @constructor
     * 
     * @param name      the name of this scope
     * @param type      the type of this scope
     */
    public Scope(String name, ScopeType type) {
        this.name = name;
        this.type = type;
        symbolTable = new ConcurrentHashMap<>();
        varCount = new ConcurrentHashMap<>();
    }


    /**
     * Returns the name for this scope
     * 
     * @return the name for this scope
     */
    public String getName() {
        return name;
    }


    /**
     * Defines a symbol of the specified type and kind
     * 
     * @param varName       the name of the symbol to define
     * @param type          the type of symbol to define
     * @param kind          the kind of symbol of define
     */
    public void define(String varName, String type, IdentifierKind kind) {
        define(varName, type, kind, null);
    }
    

    /**
     * Defines a symbol of the specified type, kind, and attributes
     * 
     * @param varName         the name of the symbol to define
     * @param type            the type of symbol to define
     * @param kind            the kind of symbol of define
     * @param attributes      the extra attributes for this symbol
     */
    protected void define(String varName, String type, IdentifierKind kind, JSONObject attributes) {
        AtomicLong index = varCount.get(kind.text());

          if(index == null) {
              index = new AtomicLong(0);
              varCount.put(kind.text(), index);
          }

          Identifier identifier = new Identifier(varName, type, kind);
          identifier.setIndex(((Long)index.getAndIncrement()).intValue());

          if (attributes != null) {
              identifier.setAttributes(attributes);
          }
          
          symbolTable.putIfAbsent(varName, identifier);
    }
    
    /**
     * Returns the total variable count for variables of the given kind
     * 
     * @param kind          the kind of variables for which to return the count
     * @return the total variable count for variables of the given kind
     */
    public int varCount(IdentifierKind kind) {
        return (varCount.containsKey(kind.text()))? ((AtomicLong)varCount.get(kind.text())).intValue() : 0;
    }


    /**
     * Returns the kind for the given variable name
     * 
     * @param varName       the name of the variable for which to retrieve the kind
     * @return the kind for the given variable name
     */
    public IdentifierKind kindOf(String varName) {
        if(!symbolTable.containsKey(varName)) {
            return IdentifierKind.NONE;
        }
        
        Identifier identifier = symbolTable.get(varName);
        return identifier.getKind();
    }
    
    
    /**
     * Returns the type for the given variable name
     * 
     * @param varName       the name of the variable for which to retrieve the type
     * @return the type for the given variable name
     */
    public String typeOf(String varName) {
        if(!symbolTable.containsKey(varName)) {
            return null;
        }
        
        Identifier identifier = symbolTable.get(varName);
        return identifier.getType();
    }
    
    
    /**
     * Returns the index for the given variable name
     * 
     * @param varName          the name of the variable for which to retrieve the index
     * @return the index for the given variable name
     */
    public int indexOf(String varName) {
        if(!symbolTable.containsKey(varName)) {
            return -1;
        }
        
        Identifier identifier = symbolTable.get(varName);
        return identifier.getIndex();
    }
    
    
    /**
     * Sets the parent scope for the given scope
     * 
     * @param parentScope       the parent scope to set for this scope 
     */
    protected void setParentScope(Scope parentScope) {
        this.parentScope = parentScope;
    }
    
    
    /**
     * Returns the parent scope for the given scope
     * 
     * @return the parent scope for this scope 
     */
    protected Scope getParentScope() {
        return parentScope;
    }
    
    
    /**
     * Resolves the given symbol in its scope chain
     * 
     * @param name      the name of the symbol to resolve
     * @return the identifier for the symbol with the given name
     */
    public Identifier resolve(String name) {
        Identifier identifier = symbolTable.get(name);
        
        if (identifier != null) {
            return identifier;
        }
        
        return (parentScope != null)? parentScope.resolve(name) : null;
    }
    
    
    /**
     * Returns a string representation of this scope
     * 
     * @return a string representation of this scope
     */
    @Override
    public String toString() {
        return String.format("Scope { type: %s, name: %s }", type, name);
    }
    
    
    /**
     * Outputs the description of the current scope
     */
    protected String describe() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Scope: Name: %s, Type: %s\n", this.name, this.type));
        
        symbolTable.keySet().stream().forEach((key) -> {
            Identifier identifier = symbolTable.get(key);
            builder.append(String.format("VarName: %s: %s", key, identifier))
                   .append("\n");
        });
        
        return builder.toString();
    }
    
}
