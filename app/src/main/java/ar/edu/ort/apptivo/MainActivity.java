package ar.edu.ort.apptivo;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText edtMail;
    EditText edtPass;
    TextView lblRegistrar;
    Button btnIngreso, btnNoAccount, btnRegistrar;
    String strUser= "tato@gmail.com";
    String strPass= "12345678";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObtenerReferencias();
        SetearListeners();

    }

    private void ObtenerReferencias(){
        edtMail=(EditText) findViewById(R.id.edtMail);
        edtPass=(EditText) findViewById(R.id.edtPass);
        btnIngreso = (Button) findViewById(R.id.btnIngreso);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        btnNoAccount=(Button)findViewById(R.id.btnInvitado);
    }


    private void SetearListeners(){
        btnNoAccount.setOnClickListener(ClickInv);
        btnIngreso.setOnClickListener(Click);
        btnRegistrar.setOnClickListener(ClickReg);
    }
    View.OnClickListener ClickInv = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(getApplicationContext(),NavigationDrawerActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener ClickReg = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {

            Intent myIntent = new Intent(MainActivity.this, RegistrarActivity.class);
            MainActivity.this.startActivity(myIntent);
        }
    };

    View.OnClickListener Click = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            IniciarSegundaActividad();
        }
    };

    private void IniciarSegundaActividad() {
        String sError="";
        Boolean Error=true;
        if (!isEmailValid(edtMail.getText().toString())) {
            Error=false;
            sError+="El Email no es valido." + "\n";
        }
        if (edtPass.getText().toString().length()<8) {
            Error=false;
            sError+="La password contiene mas de 8 caracteres."+ "\n";
        }
        if (Error)
        {
            new LoginTask().execute(edtMail.getText().toString(), edtPass.getText().toString(), "http://apptivodatabase.azurewebsites.net/api/api/Login");
        }
        else
        {
            Toast msg = Toast.makeText(getApplicationContext(),sError , Toast.LENGTH_SHORT);
            msg.show();
        }
    }
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    private class LoginTask extends AsyncTask<String, Void, Usuario> {

        protected void onPostExecute(Usuario us) {
            super.onPostExecute(us);
    
            try {

                if (us.Nombre != null) {
                    Intent intent = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
                    StaticItem.setObjeto(us);
                    startActivity(intent);
                    finish();
                } else {
                    Toast msg = Toast.makeText(getApplicationContext(), "Usuario y/o password incorrecto.", Toast.LENGTH_SHORT);
                    msg.show();
                }
            }catch(Exception e)
            {
                Toast.makeText(MainActivity.this, us.Mail, Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected Usuario doInBackground(String... parametros) {
            OkHttpClient client = new OkHttpClient();
            String Url= parametros[2]+ "/" + parametros[0] +"/"+parametros[1];
            Request request = new Request.Builder()
                    .url(Url)
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamo al API Rest servicio1 en localhost
                String resultado = response.body().string();
                Log.d("resultado", resultado);
                if (resultado.length()==4)
                {   Log.d("resultado", resultado);
                    return new Usuario();
                }                else{
                    Gson gson = new Gson();
                    Usuario miUsuario = gson.fromJson(resultado, Usuario.class);
                    return miUsuario;
                }

            } catch (IOException e) {
                Log.d("usuario",e.getMessage());             // Error de Network
                return new Usuario();
            }
        }
    }
}
