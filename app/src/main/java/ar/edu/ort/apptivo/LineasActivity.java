package ar.edu.ort.apptivo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class LineasActivity extends AppCompatActivity {
    ListView lvwLineas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lineas);
        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        Linea line = new Linea();
        ArrayList<Linea> j = new ArrayList<>();
        ArrayList<String> d = new ArrayList<>();
        lvwLineas = (ListView) findViewById(R.id.lvwLineas);

        if(b!=null)
        {
            j =(ArrayList) b.get("list");
        }


        for(int i=0; i < j.size(); i++){
            line = j.get(i);
            d.add(line.nombre);
        }




        ArrayAdapter<Linea> ListAdapter = new ArrayAdapter<Linea> ( this,android.R.layout.simple_list_item_1,j);
        lvwLineas.setAdapter(ListAdapter);
    }
}
