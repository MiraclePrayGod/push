package com.example.mscliente.util;

public class StringUtils {
    public static String capitalizarNombre(String nombre){
        if (nombre==null||nombre.isEmpty()){
            return  nombre;
        }
        String[] palabras = nombre.split(" ");
        StringBuilder nombreCapitalizado = new StringBuilder();
        for (String palabra: palabras){
            if(palabra.length() > 0 ){
                nombreCapitalizado.append(palabra.substring(0,1).toUpperCase())
                        .append(palabra.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return nombreCapitalizado.toString().trim();
    }
    public static String esCorreoValido(String email) {
        if(email== null|| !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            throw new IllegalArgumentException("Ingrese su correo con su formato completo: usuario@dominio.com");
        }
        return email;
    }
}
