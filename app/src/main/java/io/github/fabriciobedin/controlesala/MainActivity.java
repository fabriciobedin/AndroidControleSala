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
    ToggleButton btSensor1Luz;
    float lightValue = 0;
    int btnValue;
    boolean retornoLuz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSensorLuz = (TextView) findViewById(R.id.txt_sensor_luz);
        txtSensorTemperatura = (TextView) findViewById(R.id.txt_sensor_temperatura);
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

//                        if (lightValue > 26){
//                            mudaValorLuz(1);
//                        }else{
//                            mudaValorLuz(0);
//                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        if(verificaSeLuzEstaLigada()){
            btSensor1Luz.setChecked(true);
        } else {
            btSensor1Luz.setChecked(false);
        }

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
    }

    public boolean verificaSeLuzEstaLigada (){

        ControlLifeCicleApp.ligaLuz.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()>=0){
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        btnValue = Integer.parseInt(""+data.getValue());
                        if(btnValue == 1){
                            retornoLuz = true;
                        }else {
                            retornoLuz = false;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
        return retornoLuz;
    }

    public void mudaValorLuz (final int valor){
        ControlLifeCicleApp.ligaLuz.child("ligaLuz").setValue(valor, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null){
                    Log.e("FIREBASE", "msg error: " + databaseError.getMessage());
                } else {
                    if (valor == 1){
                        Toast.makeText(getApplicationContext(), "Ligando a Luz...", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Desligando a Luz...", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

}
