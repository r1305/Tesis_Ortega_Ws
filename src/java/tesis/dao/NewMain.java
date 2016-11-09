/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesis.dao;

/**
 *
 * @author Julian
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Conexion c=new Conexion();
//        System.out.println(c.login("eortega@gmail.com", "123"));
//        System.out.println("nivel: "+c.getNivel(c.getPuntaje("eortega@gmail.com")));
//        System.out.println(c.getActividades(c.getNivel(c.getPuntaje("eortega@gmail.com"))));
//        System.out.println(78/Math.pow(1.7, 2));
//        System.out.println(c.sumarPuntos("eortega@gmail.com", 1));
        System.out.println(c.validarUsuario("ortega@gmail.com"));
    }
    
}
