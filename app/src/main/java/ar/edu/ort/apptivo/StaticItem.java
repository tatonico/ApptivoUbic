package ar.edu.ort.apptivo;

/**
 * Created by Tato on 3/7/2017.
 */

public class StaticItem{
    public StaticItem () { // private constructor
        String Nombre = "";
    }
    public static String Nombre;
    public static String Contraseña;
    public static String Apellido;
    public static String Sexo;
    public static String Email;
    public static Integer Id;

    public static  void setObjeto(Usuario user) {
        Nombre = user.Nombre;
        Contraseña = user.Contraseña;
        Apellido = user.Apellido;
        Sexo= user.Sexo;
        Email = user.Email;
        Id=user.Id;
    }
    public static Usuario getUser()
    {
        Usuario user = new Usuario();
        user.Nombre=Nombre;
        user.Apellido=Apellido;
        user.Contraseña=Contraseña;
        user.Id=Id;
        user.Sexo=Sexo;
        return user;
    }
}