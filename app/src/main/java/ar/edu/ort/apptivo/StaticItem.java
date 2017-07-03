package ar.edu.ort.apptivo;

/**
 * Created by Tato on 3/7/2017.
 */

public class StaticItem{
    public StaticItem () { // private constructor
        String Nombre = "";
    }
    public static String Nombre;
    public static String Contrasena;
    public static String Apellido;
    public static String Sexo;
    public static String Mail;
    public static Integer Id;

    public static  void setObjeto(Usuario user) {
        Nombre = user.Nombre;
        Contrasena = user.Contrasena;
        Apellido = user.Apellido;
        Sexo= user.Sexo;
        Mail = user.Mail;
        Id=user.Id;
    }
    public static Usuario getUser()
    {
        Usuario user = new Usuario();
        user.Nombre=Nombre;
        user.Apellido=Apellido;
        user.Contrasena=Contrasena;
        user.Id=Id;
        user.Sexo=Sexo;
        return user;
    }
}