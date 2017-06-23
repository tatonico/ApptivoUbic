package ar.edu.ort.apptivo;
/*
	        String method = "register";
            BackgroundTask backgroundTask = new BackgroundTask(this);
            backgroundTask.execute(method, parametros);
            finish();
*/
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.StringTokenizer;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.framed.Variant;


/**
 * Created by 42374778 on 2/6/2017.
 */

public class BackgroundTask extends AsyncTask<String, Void, String>
{

    Context ctx;
    BackgroundTask(Context ctx)
    {
        this.ctx= ctx;
    }

    public String registrar(String[] params)
    {
        OkHttpClient client = new OkHttpClient();


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("metodo", params[0])
                .addFormDataPart("usuario", params[1])
                .addFormDataPart("password", params[2])
                .addFormDataPart("email", params[3])
                .build();


        Request request = new Request.Builder()
                .url("")
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();  // Llamo al API Rest servicio1 en ejemplo.com
            String resultado = response.body().string();

            return resultado;
        } catch (IOException e) {
            Log.d("Debug", e.getMessage());
            return "error";
        }
    }

    public String login(String[] params)
    {
        OkHttpClient client = new OkHttpClient();


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("metodo", params[0])
                .addFormDataPart("usuario", params[1])
                .addFormDataPart("Password", params[2])
                .build();


        Request request = new Request.Builder()
                .url("")
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();  // Llamo al API Rest servicio1 en ejemplo.com
            String resultado = response.body().string();

            return resultado;

        } catch (IOException e) {
            Log.d("Debug", e.getMessage());
            //mostrarError(e.getMessage()); // Error de Network
            return "error";
        }
    }

    public String chequearUseryMail(String[] params)
    {
        OkHttpClient client = new OkHttpClient();


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("metodo", params[0])
                .addFormDataPart("usuario", params[1])
                .addFormDataPart("Mail", params[2])
                .build();


        Request request = new Request.Builder()
                .url("")
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();  // Llamo al API Rest servicio1 en ejemplo.com
            String resultado = response.body().string();

            String[] errores = resultado.split("/");

            for (int i=0; i > errores.length; i++)
            {
                switch (errores[i])
                {
                    case "MailMal":
                        break;
                    case "UserMail":
                        break;
                    case "LosDosMal":
                        break;
                }
            }

            return resultado;

        } catch (IOException e) {
            Log.d("Debug", e.getMessage());
            //mostrarError(e.getMessage()); // Error de Network
            return "error";
        }
    }

    public String error(String metodo)
    {
        OkHttpClient client = new OkHttpClient();


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("metodo", metodo)
                .build();


        Request request = new Request.Builder()
                .url("")
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();  // Llamo al API Rest servicio1 en ejemplo.com
            String resultado = response.body().string();

            return resultado;
        } catch (IOException e) {
            Log.d("Debug", e.getMessage());
            //mostrarError(e.getMessage()); // Error de Network
            return "error";
        }
    }



    @Override
    protected String doInBackground (String...params)
    {
        switch (params[0])
        {
            case "register":
                return registrar(params);
            case "login":
                return login(params);
            case "chequearUserYMail":
                return chequearUseryMail(params);
            default:
                //return "Hubo un error";
                return error(params[0]);
        }

    }

    @Override
    protected void onProgressUpdate(Void... values)
    {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result)
    {
        try{
            Gson gson = new Gson();
            Usuario miUsuario = gson.fromJson(result, Usuario.class);
        }
        catch (Exception e)        {
        }
        Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
    }
}
