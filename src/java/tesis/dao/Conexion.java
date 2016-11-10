/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesis.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Julian
 */
public class Conexion {
    String url = "jdbc:mysql://eu-cdbr-west-01.cleardb.com:3306/heroku_65386730131d08e?user=bb4ea9654c62d9&password=2641de9c";    
    //String url="jdbc:mysql://localhost:3306/actifit?user=root&password=root";
    

    public Connection getConexion() {
        java.sql.Connection con = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            try {
                con = DriverManager.getConnection(url);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return con;
    }
    
    public String ranking(){
        Connection cn;
        Conexion con = new Conexion();
        ResultSet rs;
        PreparedStatement pr;
        JSONObject ob = new JSONObject();
        JSONArray ja = new JSONArray();
        try {
            cn = con.getConexion();
            //String sql = "SELECT * FROM actividades where puntuacion>=" + puntuacion+" and fecha>=current_date()";
            String sql = "";

            sql = "SELECT * FROM jugador order by puntaje desc limit 5";
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();

            while (rs.next()) {
                JSONObject o = new JSONObject();
                o.put("nombre", rs.getString("nombres"));
                o.put("puntaje", rs.getFloat("puntaje"));
                ja.add(o);

            }
            ob.put("rank", ja);
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return ob.toString();
    }
    
    public String getDatos(String correo){
        Connection cn;
        Conexion con = new Conexion();
        ResultSet rs;
        PreparedStatement pr;
        JSONObject o = new JSONObject();
        
        try {
            cn = con.getConexion();
            //String sql = "SELECT * FROM actividades where puntuacion>=" + puntuacion+" and fecha>=current_date()";
            String sql = "";

            sql = "select nombre,nombres,peso,altura,imc,n.imagen,email,max_puntaje,puntaje,n.id_nivel"
                    + " from jugador j join nivel n on id_nivel="+getNivel(getPuntaje(correo))+" "
                    + "where j.email='"+correo+"'";
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();

            while (rs.next()) {                
                o.put("nombre", rs.getString("nombres"));
                o.put("puntaje", rs.getInt("puntaje"));
                o.put("nivel",rs.getString("nombre"));
                o.put("peso",rs.getFloat("peso"));
                o.put("alt",rs.getFloat("altura"));
                o.put("imc",rs.getFloat("imc"));
                o.put("max",rs.getInt("max_puntaje"));
                o.put("img",rs.getString("imagen"));
                
            }
            
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return o.toString();
    }
    
    public String validarUsuario(String correo) {
        
        String co = "fail";
        try {
            Connection con =getConexion();
            String strsql = "SELECT * FROM jugador where email='" + correo + "'";
            PreparedStatement pstm = con.prepareStatement(strsql);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                co = rs.getString("email");
            }
            System.out.println("**"+co+"**");
            if (co == "" && co == null) {
                co = "fail";
            }

            pstm.close();
            con.close();

        } catch (Exception e) { 
            co="fail";
            System.out.println(e);
        }
        return co;
    }
    
    public String login(String correo,String psw){
        String rpta="fail";
        Connection cn;
        Conexion con = new Conexion();
        ResultSet rs;
        PreparedStatement pr;
        JSONObject o = new JSONObject();
        
        try {
            cn = con.getConexion();
            //String sql = "SELECT * FROM actividades where puntuacion>=" + puntuacion+" and fecha>=current_date()";
            String sql = "";

            sql = "SELECT * FROM jugador where email='"+correo+"' and contrasena='"+psw+"'";
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();

            while (rs.next()) {
                rpta=rs.getString("email");
            }                                       
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            rpta="fail";
            System.out.println(ex);
        }
        return rpta;
    }
    
    public String getActividades(int nivel,String correo){
        Connection cn;
        Conexion con = new Conexion();
        ResultSet rs;
        PreparedStatement pr;
        JSONObject ob = new JSONObject();
        JSONArray ja = new JSONArray();
        try {
            cn = con.getConexion();
            //String sql = "SELECT * FROM actividades where puntuacion>=" + puntuacion+" and fecha>=current_date()";
            String sql = "";

            sql = "SELECT * FROM actividad where id_actividad not in (select id_actividad from actividad_realizada where id_jugador='"+correo+"') and id_nivel="+nivel;
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();

            while (rs.next()) {
                JSONObject o = new JSONObject();
                o.put("id",rs.getInt("id_actividad"));
                o.put("nombre", rs.getString("nombre"));
                o.put("descripcion", rs.getString("descripcion"));
                o.put("repeticiones",rs.getString("repeticiones"));
                o.put("imagen", rs.getString("imagen"));
                o.put("tiempo", rs.getInt("tiempo"));          
                o.put("punt",rs.getInt("puntaje"));
                ja.add(o);
            }
            ob.put("act", ja);
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return ob.toString();
    }
    
    public int getPuntaje(String correo){
        int p=0;
        Connection cn;
        Conexion con = new Conexion();
        ResultSet rs;
        PreparedStatement pr;
        try {
            cn = con.getConexion();
            //String sql = "SELECT * FROM actividades where puntuacion>=" + puntuacion+" and fecha>=current_date()";
            String sql = "";

            
                sql = "SELECT puntaje FROM jugador where email='"+correo+"'";
            
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();

            while (rs.next()) {
                p=rs.getInt(1);
            }
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return p;
    }
    
    public boolean registrar(String nombre,String email,String psw,float peso,int altura){
        boolean ok=false;
        Connection cn;
        Conexion con = new Conexion();
        PreparedStatement pr;
        try{
            cn=con.getConexion();
            String sql="insert into jugador "
                    + "(nombres,email,contrasena,peso,altura,imc,fecha_inicio,nivel_actual,puntaje) "
                    + "values(?,?,?,?,?,?,current_date(),1,0)";
            pr=cn.prepareStatement(sql);
            pr.setString(1, nombre);
            pr.setString(2, email);
            pr.setString(3, psw);
            pr.setFloat(4, peso);
            pr.setInt(5, altura);
            pr.setFloat(6, (Float.parseFloat(String.valueOf(peso/(Math.pow(altura/100, 2))))));
            pr.executeUpdate();
        }catch(Exception e){
            ok=false;
        }
        return ok;
    }
    
    public int getNivel(int puntaje){
        int p=0;
        Connection cn;
        Conexion con = new Conexion();
        ResultSet rs;
        PreparedStatement pr;
        try {
            cn = con.getConexion();
            //String sql = "SELECT * FROM actividades where puntuacion>=" + puntuacion+" and fecha>=current_date()";
            String sql = "";

            if(puntaje>=0 && puntaje<=271){
                sql = "SELECT id_nivel FROM nivel where min_puntaje=0 and max_puntaje=271";
            }else if(puntaje>=272 && puntaje<=466){
                sql = "SELECT id_nivel FROM nivel where min_puntaje=272 and max_puntaje=466";
            }else if(puntaje>=467 && puntaje<=856){
                sql = "SELECT id_nivel FROM nivel where min_puntaje=467 and max_puntaje=856";
            }else if(puntaje>=857 && puntaje<=1051){
                sql = "SELECT id_nivel FROM nivel where min_puntaje=857 and max_puntaje=1051";
            }else if(puntaje>=1052 && puntaje<=1521){
                sql = "SELECT id_nivel FROM nivel where min_puntaje=1052 and max_puntaje=1521";
            }else if(puntaje>=1522 && puntaje<=1776){
                sql = "SELECT id_nivel FROM nivel where min_puntaje=1522 and max_puntaje=1776";
            }else if(puntaje>=1777 && puntaje<=2302){
                sql = "SELECT id_nivel FROM nivel where min_puntaje=1777 and max_puntaje=2302";
            }else if(puntaje>=2303 && puntaje<=2587){
                sql = "SELECT id_nivel FROM nivel where min_puntaje=2303 and max_puntaje=2587";
            }
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();

            while (rs.next()) {
                p=rs.getInt(1);
            }
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return p;
    }
    
    public boolean sumarPuntos(String correo, int idAct){
        boolean ok=false;
        Connection cn;
        Conexion con = new Conexion();
        ResultSet rs;
        PreparedStatement pr;
        
        PreparedStatement pr2;
        
        int punt=getPuntaje(correo);
        int punt2=0;
        int nueva_punt=0;
        try{
            cn = con.getConexion();
                       
            String sql="Select puntaje from actividad where id_actividad="+idAct;
            pr=cn.prepareStatement(sql);
            rs=pr.executeQuery();
            while(rs.next()){
                punt2=rs.getInt(1);
            }
            rs.close();
            pr.close();
            nueva_punt=punt+punt2;
            
            String sql2="update jugador set puntaje="+nueva_punt+" where email='"+correo+"'";
            pr2=cn.prepareStatement(sql2);
            pr2.executeUpdate();
            pr2.close();
            cn.close();
            insertAct(correo, idAct);
            ok=true;
            
        }catch(Exception e){
            ok=false;
        }
        return ok;
    }
    
    public void insertAct(String correo,int idAct){
        Connection cn;
        Conexion con = new Conexion();
        PreparedStatement pr;
        try{
            cn=con.getConexion();
            String sql="insert into actividad_realizada (id_jugador,id_actividad,fecha) values('"+correo+"',"+idAct+",current_date())";
            pr=cn.prepareStatement(sql);
            pr.executeUpdate();
        }catch(Exception e){
        }
    }
    
    public String progreso(String correo){
        Connection cn;
        Conexion con = new Conexion();
        ResultSet rs;
        PreparedStatement pr;
        JSONObject ob = new JSONObject();
        JSONArray ja = new JSONArray();
        try {
            cn = con.getConexion();
            //String sql = "SELECT * FROM actividades where puntuacion>=" + puntuacion+" and fecha>=current_date()";
            String sql = "";

            sql = "select count(*) as cant,day(fecha) as dia from actividad_realizada where id_jugador='"+correo+"' group by fecha";
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();

            while (rs.next()) {
                JSONObject o = new JSONObject();
                o.put("cant",rs.getInt("cant"));
                o.put("dia", rs.getString("dia"));
                      
                ja.add(o);
            }
            ob.put("progreso", ja);
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return ob.toString();
    }

}
