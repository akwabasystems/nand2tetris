/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akwabasystems.model;


public class SymbolTable {
    private ClassScope classScope;
    private SubroutineScope subroutineScope;
    private Scope currentScope;
    
    
    public void startClass(String name) {
        classScope = new ClassScope(name);
        currentScope = classScope;
        System.out.printf("\n>>> SymbolTable: started class scope: %s\n", currentScope);
    }
    
    
    public void startSubroutine(String name) {
        subroutineScope = new SubroutineScope(name);
        currentScope = subroutineScope;
        System.out.printf("\n>>> SymbolTable: started subroutine scope: %s\n", currentScope);
    }


    public void define(String name, String type, IdentifierKind kind) {
        boolean hasClassScope = (kind == IdentifierKind.STATIC || kind == IdentifierKind.FIELD);
        
        if(hasClassScope) {
            classScope.define(name, type, kind);
        } else {
            subroutineScope.define(name, type, kind);
        }
    }
    

    public int varCount(IdentifierKind kind) {
        return currentScope.varCount(kind);
    } 
    
    
    public IdentifierKind kindOf(String name) {
        return currentScope.kindOf(name);
    }
    
    
    public String TypeOf(String name) {
        return currentScope.typeOf(name);
    }
    
    
    public int IndexOf(String name) {
        return currentScope.indexOf(name);
    }

}
