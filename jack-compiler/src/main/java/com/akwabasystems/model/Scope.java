/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akwabasystems.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;


/**
 *
 * @author vn0gxkl
 */
public abstract class Scope {
    protected String name;
    protected ScopeType type;
    protected Scope parentScope;
    protected ConcurrentMap<String,Identifier> symbolTable;
    protected ConcurrentMap<String,AtomicLong> varCount;


    public Scope(String name, ScopeType type) {
        this.name = name;
        this.type = type;
        symbolTable = new ConcurrentHashMap<String,Identifier>();
        varCount = new ConcurrentHashMap<String, AtomicLong>();
    }


    public String getName() {
        return name;
    }


    public void define(String varName, String type, IdentifierKind kind) {
        AtomicLong index = varCount.get(kind.text());

        if(index == null) {
            index = new AtomicLong(0);
            varCount.put(kind.text(), index);
        }

        Identifier identifier = new Identifier(varName, type, kind);
        identifier.setIndex(((Long)index.getAndIncrement()).intValue());
        
        symbolTable.putIfAbsent(varName, identifier);
        System.out.printf("--- Defined symbol: %s - type: %s - kind: %s - index: %s\n", 
                varName, type, kind, identifier.getIndex());
    }
    
    
    public int varCount(IdentifierKind kind) {
        return (varCount.containsKey(kind.text()))? ((AtomicLong)varCount.get(kind.text())).intValue() : 0;
    }


    public IdentifierKind kindOf(String varName) {
        if(!symbolTable.containsKey(varName)) {
            return IdentifierKind.NONE;
        }
        
        Identifier identifier = symbolTable.get(varName);
        return identifier.getKind();
    }
    
    
    public String typeOf(String varName) {
        if(!symbolTable.containsKey(varName)) {
            return null;
        }
        
        Identifier identifier = symbolTable.get(varName);
        return identifier.getType();
    }
    
    
    public int indexOf(String varName) {
        if(!symbolTable.containsKey(varName)) {
            return -1;
        }
        
        Identifier identifier = symbolTable.get(varName);
        return identifier.getIndex();
    }
    
    
    protected void setParentScope(Scope parentScope) {
        this.parentScope = parentScope;
    }
    
    
    protected Scope getParentScope() {
        return parentScope;
    }
    
    
    public Identifier resolve(String name) {
        Identifier identifier = symbolTable.get(name);
        
        if (identifier != null) {
            return identifier;
        }
        
        return (parentScope != null)? parentScope.resolve(name) : null;
    }
    
    
    
    @Override
    public String toString() {
        return String.format("Scope { type: %s, name: %s }", type, name);
    }
    
    
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
