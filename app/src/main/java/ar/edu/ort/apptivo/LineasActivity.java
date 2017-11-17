package ar.edu.ort.apptivo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class LineasActivity extends AppCompatActivity {
    ListView lvwLineas;
    ArrayList<Linea> j;
    ArrayList<Linea> jnew;
    ArrayList<String> d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lineas);
        final Intent intent= getIntent();
        Bundle b = intent.getExtras();
        Linea line = new Linea();
        j = new ArrayList<>();
        jnew = new ArrayList<>();
        d = new ArrayList<>();
        lvwLineas = (ListView) findViewById(R.id.lvwLineas);

        if(b!=null)
        {
            j =(ArrayList)b.get("list");
        }
//tatonico luciana321321 tato_izraelski@hotmail.com



        for(int i=0; i < j.size(); i++){
            line = j.get(i);
            if (!d.contains(line.nombre)) {
                jnew.add(line);
                d.add(line.nombre +"\n" +"En destino aproximadamente a las: " + line.tiempollegada);
            }
        }
        ArrayAdapter<String> ListAdapter = new ArrayAdapter<String> ( this,android.R.layout.simple_list_item_1,d);
        lvwLineas.setAdapter(ListAdapter);
        lvwLineas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Linea lineaselected = jnew.get(position);
                Intent intent = new Intent(getApplicationContext(),NavigationDrawerActivity.class);
                intent.putExtra("linea", lineaselected);
                startActivity(intent);
                finish();
            } });
    }

}
