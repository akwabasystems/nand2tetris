
package com.akwabasystems.model;


/**
 * A class that represents a class scope. It is set as the parent scope for all child scopes defined
 * for its subroutines. 
 */
public final class ClassScope extends Scope { 
    
    public ClassScope(String name) {
        super(name, ScopeType.CLASS);
    }

}
