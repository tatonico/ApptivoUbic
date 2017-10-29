package ar.edu.ort.apptivo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegistrarActivity extends AppCompatActivity {

    Button btnRegistrar;
    TextView txtNombre;
    TextView txtApellido;
    TextView txtMail;
    TextView txtContraseña;
    TextView txtRepetirContraseña;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
    }
    public void SetearReferencias(){
        btnRegistrar = (Button)findViewById(R.id.btnRegistrar);
        txtNombre = (TextView)findViewById(R.id.txtNombre);
        txtApellido = (TextView)findViewById(R.id.txtNombre);
        txtMail = (TextView)findViewById(R.id.txtNombre);
        txtContraseña = (TextView)findViewById(R.id.txtNombre);
        txtRepetirContraseña = (TextView)findViewById(R.id.txtRepetirContraseña);

    }
    public void SetearListener(){
        btnRegistrar.setOnClickListener(Registrar_Click);
    }
    View.OnClickListener Registrar_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (txtApellido.getText().toString() != "" && txtMail.getText().toString() != ""
                    && txtContraseña.getText().toString() != "" && txtNombre.getText().toString() != "" &&
                    txtRepetirContraseña.getText().toString() != "") {
                if (isEmailValid(txtMail.getText().toString())) {
                    if (txtContraseña.length() > 7) {
                        if (txtContraseña.getText().toString() == txtRepetirContraseña.getText().toString()) {
                            new RegistrarTask().execute(txtNombre.getText().toString(), txtApellido.getText().toString(),
                                    "http://apptivodatabase.azurewebsites.net/api/api/Login/",
                                    txtMail.getText().toString(), txtContraseña.getText().toString());
                        } else {
                            Toast.makeText(RegistrarActivity.this, "Los campos de contraseña y repetir contraseña no coinciden.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegistrarActivity.this, "Su password debe ocupar 8 o mas caracteres.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegistrarActivity.this, "Su email no es valido.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

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


    private class RegistrarTask extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String devolvio) {

            if(devolvio!= "Error") {
                Toast msg = Toast.makeText(getApplicationContext(),"Registracion correcta." , Toast.LENGTH_SHORT);
                msg.show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast msg = Toast.makeText(getApplicationContext(),"Hubo error en la registracion." , Toast.LENGTH_SHORT);
                msg.show();
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        }
        @Override
        protected String doInBackground(String... parametros) {
            OkHttpClient client = new OkHttpClient();
            String Url= parametros[2]+ "/" + parametros[0] +"/"+parametros[1]+"/"+parametros[3]+"/"+parametros[4];
            Request request = new Request.Builder()
                    .url(Url)
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamo al API Rest servicio1 en localhost
                String resultado = response.body().string();
                return resultado;
            } catch (IOException e) {
                Log.d("Error",e.getMessage());             // Error de Network
                return "Error";
            }
        }
    }
}
