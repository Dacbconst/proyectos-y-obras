package com.luckyecuador.app.PintucoAPP;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAgotados;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AgotadosActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String id_pdv,user, marca, categoria, tipo, venta;
    DatabaseHelper handler;

    TextView empty;
    ListView listview;
    private Spinner spMarca, spCategoria, spTipo;

    List<String> listProductos;

    CustomAdapterPresenciaMinima dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agotados);
        setToolbar();
        //setActionBar();
        LoadData();
        //startService(new Intent(getApplicationContext(), MyService.class));
        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);
        spMarca = (Spinner)findViewById(R.id.spMarca);
        spCategoria = (Spinner)findViewById(R.id.spSubcategoria);
        spTipo=(Spinner) findViewById(R.id.spTipo);

        empty = (TextView) findViewById(R.id.recyclerview_data_empty);
        listview = (ListView)findViewById(android.R.id.list);
        //GUILLERMO 03/09/2018
    /*
        filtrarMarca();
*/
    }


/*
    public void filtrarMarca() {
        List<String> operadores = handler.getMarcaFlooring(user);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMarca.setAdapter(dataAdapter);
        spMarca.setOnItemSelectedListener(this);
    }
*/
//GUILLERMO 03/09/2018
    /*
    public void filtrarMarca(String status) {

        List<String> operadores = handler.getSubcategoriaFlooring(status,user);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
    }

    public void filtrarMarca(String status, String subcategoria) {
        List<String> operadores = handler.getSubcategoriaFlooring(status,subcategoria,user);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(dataAdapter);
        spTipo.setOnItemSelectedListener(this);
    }
*/

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV, Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {

        if (adapterView == spMarca) {
            try {
                marca = adapterView.getItemAtPosition(i).toString();


                //GUILLERMO 03/09/2018
    /*
                filtrarMarca(status);
                */
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView == spCategoria) {
            try {
                categoria = adapterView.getItemAtPosition(i).toString();
                /*
                filtrarMarca(status, subcategoria);
                */
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView == spTipo) {
            try {
                tipo = adapterView.getItemAtPosition(i).toString();
                showListView(marca, categoria, tipo);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


        }

    }



    public void showListView(String marca, String categoria,String tipo) {
        /*GUILLERMO
        listProductos = handler.filtrarListProductos(status,subcategoria,canal);
        */
        dataAdapter = new CustomAdapterPresenciaMinima(this,listProductos);
        if (!dataAdapter.isEmpty()) {
            listview.setAdapter(dataAdapter);
        }else{
            empty.setVisibility(View.VISIBLE);
        }
    }

    public class CustomAdapterPresenciaMinima extends ArrayAdapter<String> {

        public List<String> values;
        public Context context;
        boolean[] checkBoxState;

        public CustomAdapterPresenciaMinima(Context context, List<String> values) {
            super(context, 0, values);
            this.values = values;

            checkBoxState=new boolean[values.size()];
        }

        public class ViewHolder{
            TextView lblSku;
            CheckBox check; // agregado GT
            //EditText txtunidad;
            EditText txtventa;
            // Spinner spMotivo;
        }

        @Override
        public int getViewTypeCount() {
            // TODO Auto-generated method stub
            return values.size();
        }

        @Override
        public int getItemViewType(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //Obtener Instancia Inflater
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           ViewHolder vHolder = null;
            //Comprobar si el View existe
            //Si no existe inflarlo
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.list_row_option, parent, false); // Modificacion (list_row_option) GT

                //Obtener instancias de los elementos
                vHolder = new ViewHolder();
                vHolder.lblSku = (TextView) convertView.findViewById(R.id.lblSku);
                vHolder.check = (CheckBox) convertView.findViewById(R.id.checkPresencia);
                //   vHolder.txtunidad = (EditText) convertView.findViewById(R.id.txtunidad);
                vHolder.txtventa = (EditText) convertView.findViewById(R.id.txtventa);

                //cargarMotivos(vHolder);
                convertView.setTag(vHolder);

                //checkGuardar.setEnabled(true);
            } else { vHolder = (ViewHolder) convertView.getTag(); }

            if (values.size() > 0) {
                //set the data to be displayed
                vHolder.lblSku.setText(values.get(position));

                // sku = vHolder.lblSku.getText().toString();
                // checkGuardar.setEnabled(false);
                final ViewHolder finalv =vHolder;

                vHolder.check.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //  unidad= finalv.txtunidad.getText().toString();
                        venta=finalv.txtventa.getText().toString();

                        if (((CheckBox)v).isChecked()) {

                            if (((CheckBox)v).isChecked()) {

                                if (venta.equals(" ") || venta.isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "Verifica el Ingreso en Unidades", Toast.LENGTH_LONG).show();

                                }else
                                {
                                    String valor="1";
                                    insertData(finalv.lblSku.getText().toString(), valor, venta);
                                }
                            }
                        }
                    }
                });
            }
            //Devolver al ListView la fila creada
            return convertView;
        }
    }



    public void insertData(String producto, String target, String total) {

        //Almacenar Datos
        ContentValues values = new ContentValues();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fechaser = date.format(currentLocalTime);

        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String horaser = hour.format(currentLocalTime);

        String brand=spMarca.getSelectedItem().toString();
        String category=spCategoria.getSelectedItem().toString();
        String subcategory=spTipo.getSelectedItem().toString();
        /*
        values.put(ContractInsertAgotados.Columnas.IDPDV, id_pdv);
        values.put(ContractInsertAgotados.Columnas.CATEGORIA,subcategoria);
        values.put(ContractInsertAgotados.Columnas.CATEGORIA, subcategory);
        values.put(ContractInsertAgotados.Columnas.MARCA, contenido);
        values.put(ContractInsertAgotados.Columnas.PRODUCTO, producto);
        values.put(ContractInsertAgotados.Columnas.TOTAL,target);
        values.put(ContractInsertAgotados.Columnas.TARGET,total);
        */
        values.put(ContractInsertAgotados.Columnas.FECHA, fechaser);
        values.put(ContractInsertAgotados.Columnas.HORA, horaser);
        values.put(Constantes.PENDIENTE_INSERCION, 1);

        getContentResolver().insert(ContractInsertAgotados.CONTENT_URI, values);

        if (VerificarNet.hayConexion(getApplicationContext())) {
            SyncAdapter.sincronizarAhora(this, true, Constantes.insertAgotados, null);
            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
