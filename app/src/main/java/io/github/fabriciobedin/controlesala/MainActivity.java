package io.github.fabriciobedin.controlesala;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    TextView txtSensorLuz;
    TextView txtSensorTemperatura;
    TextView txtSensorPortaSala;
    ToggleButton btSensor1Luz;
    float lightValue = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSensorLuz = (TextView) findViewById(R.id.txt_sensor_luz);
        txtSensorTemperatura = (TextView) findViewById(R.id.txt_sensor_temperatura);
        txtSensorPortaSala = (TextView) findViewById(R.id.txt_sensor_porta_sala);
        btSensor1Luz = (ToggleButton) findViewById(R.id.bt_1_luz);
        btSensor1Luz.setTextOn("Ligada");
        btSensor1Luz.setTextOff("Desligada");

        ControlLifeCicleApp.sensorLuz.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()>0){
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        lightValue = Float.parseFloat(""+data.getValue());
                        lightValue = lightValue*100;
                        txtSensorLuz.setText(""+lightValue);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        //entra apenas uma vez
        ControlLifeCicleApp.sensorLuz.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()>0){
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        lightValue = Float.parseFloat(""+data.getValue());
                        if (lightValue > 26){
                            btSensor1Luz.setChecked(true);
                        }else{
                            btSensor1Luz.setChecked(false);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btSensor1Luz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mudaValorLuz(1);
                } else {
                   mudaValorLuz(0);
                }
            }
        });


        //sensor porta sala --------------------------------------------------------------------------------------------------
        ControlLifeCicleApp.sensorPorta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()>0){
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        Integer valorSensorPorta = 0;
                        valorSensorPorta = Integer.parseInt(""+data.getValue());
                        if(valorSensorPorta == 1){
                            txtSensorPortaSala.setText("Porta Aberta!");
                        } else{
                            txtSensorPortaSala.setText("Porta Fechada!");
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }



    public void mudaValorLuz (final int valor){
        ControlLifeCicleApp.ligaLuz.child("ligaLuz").setValue(valor, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null){
                    Log.e("FIREBASE", "msg error: " + databaseError.getMessage());
                } else {
//                    if (valor == 1){
//                        Toast.makeText(getApplicationContext(), "Ligando a Luz...", Toast.LENGTH_SHORT).show();
//                    }else {
//                        Toast.makeText(getApplicationContext(), "Desligando a Luz...", Toast.LENGTH_SHORT).show();
//                    }

                }
            }
        });
    }

}
