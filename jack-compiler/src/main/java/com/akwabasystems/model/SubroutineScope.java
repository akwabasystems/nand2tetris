
package com.akwabasystems.model;


/**
 * A class that represents a subroutine scope. Its parent scope is set to the scope of its enclosing class. 
 */
public final class SubroutineScope extends Scope { 
    
    public SubroutineScope(String name) {
        super(name, ScopeType.SUBROUTINE);
    }

}
