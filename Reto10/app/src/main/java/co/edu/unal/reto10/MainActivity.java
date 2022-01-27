package co.edu.unal.reto10;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Spinner municipios_spinner;
    private ArrayList<String> municipios;
    private ArrayAdapter<String> municipios_adapter;
    private ListView listadoGeneral;
    private ArrayList<String> datos;
    private ArrayAdapter<String> datos_adapter;
    private final Context context = this;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        municipios = new ArrayList<>();
        datos = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        String url = "https://www.datos.gov.co/resource/pejt-qp6n.json?$select=distinct%20municipio&$order=municipio%20ASC";
        municipios_spinner = (Spinner) findViewById(R.id.municipios);
        listadoGeneral = findViewById(R.id.list);
        JsonArrayRequest centros = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject tmp = null;
                            try {
                                tmp = response.getJSONObject(i);
                                municipios.add(tmp.getString("municipio"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        municipios_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, municipios);
                        municipios_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        municipios_spinner.setAdapter(municipios_adapter);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        municipios_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                datos.clear();
                final String tmp = (String) parent.getItemAtPosition(pos);
                String url = "https://www.datos.gov.co/resource/pejt-qp6n.json?municipio=" + tmp;
                JsonArrayRequest codes = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject tmp = null;
                                    try {
                                        tmp = response.getJSONObject(i);
                                        String tmp2 = "Centro poblado: " + tmp.getString("centro_poblado") + "\n";
                                        tmp2 += "Numero DANE: " + tmp.getString("dane_centro_poblado") + "\n";
                                        tmp2 += "Tipo de energia: " + tmp.getString("tipo_de_energia") + "\n";
                                        tmp2 += "DDA: " + tmp.getString("dda") + "\n";
                                        tmp2 += "Longitud: " + tmp.getString("longitud") + "\n";
                                        tmp2 += "Latitud: " + tmp.getString("latitud") + "\n";
                                        datos.add(tmp2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                datos_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, datos);
                                listadoGeneral.setAdapter(datos_adapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("REQ", "bad");
                            }
                        });
                queue.add(codes);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        queue.add(centros);
    }
}