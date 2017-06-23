package ar.edu.ort.apptivo;

/**
 * Created by 42252968 on 9/6/2017.
 */

public class ApiMock {

    public static String getLogin(){
        String strREturnValue= "";
        strREturnValue += " {";
        strREturnValue += "         idUsuario : 10,";
        strREturnValue += "         Nombre : 'Martin',";
        strREturnValue += "         Apellido: 'Izra' ,";
        strREturnValue += "         Sexo: 'Masculino'";
        strREturnValue += "         Mail: 'tatonico@gmail.com'";
        strREturnValue += "         Contrasena: 'xxxxx'";
        strREturnValue += "}";
        return strREturnValue;
    }

}
