/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akwabasystems.model;

/**
 *
 * @author vn0gxkl
 */
public class Identifier {
    private String name;
    private String type;
    private int index;
    private IdentifierKind kind = IdentifierKind.VAR;
   
   
    public Identifier(String name, String type, IdentifierKind kind) {
        this.name = name;
        this.type = type;
        this.kind = kind;
    }


    public void setIndex(int index) {
        this.index = index;
    }
    
    
    public int getIndex() {
        return index;
    }
    
    
    public String getName() {
        return name;
    }
    
    
    public String getType() {
        return type;
    }
    
    
    public IdentifierKind getKind() {
        return kind;
    }


    @Override
    public String toString() {
        return String.format("Identifier { name: %s, type: %s, kind: %s, index: %s }", name, type, kind, index);
    } 
   
}
