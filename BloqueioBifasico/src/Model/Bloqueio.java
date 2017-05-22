/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author geovani
 */
public enum Bloqueio {
    COMPARTILHADO ("S"),
    EXCLUSIVO ("X");
    String texto;

    private Bloqueio(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return "Bloqueio{" + texto + '}';
    }
    
    
    
    
}
