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
public enum Acesso {
    READ("R"),
    WRITE("W"),
    BEGIN("B"),
    END("E"),
    COMMIT("C"),
    ABORT("A");
    String texto;

     Acesso(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
    
   
}
