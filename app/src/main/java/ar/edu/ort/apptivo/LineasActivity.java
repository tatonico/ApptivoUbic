package ar.edu.ort.apptivo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LineasActivity extends AppCompatActivity {
ListView lvwLineas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lineas);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        Linea j = new Linea();
        lvwLineas = (ListView) findViewById(R.id.lvwLineas);

        if(b!=null)
        {
         j =(Linea) b.get("list");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, j);
        lvwLineas.setAdapter(arrayAdapter);
    }
}
