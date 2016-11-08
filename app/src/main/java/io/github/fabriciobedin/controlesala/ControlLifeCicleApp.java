package io.github.fabriciobedin.controlesala;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by fabricio on 18/10/2016.
 */

public class ControlLifeCicleApp extends Application {

    public static DatabaseReference sensorLuz;
    public static DatabaseReference ligaLuz;
    public static DatabaseReference sensorPorta;
    public static DatabaseReference horarioPortaAbriu;
    public static DatabaseReference horarioPortaFechou;


    @Override
    public void onCreate() {
        //quando ele entra aqui, iniciou o ciclo de vida da aplicação toda e não só da tela
        super.onCreate();

        //referencia pro path do banco

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        sensorLuz = database.getReference("sensor");

        ligaLuz = database.getReference("ligaLuz");

        sensorPorta = database.getReference("sensorPorta");
        horarioPortaAbriu = database.getReference("horarioPortaAbriu");
        horarioPortaFechou = database.getReference("horarioPortaFechou");


        //pega o path tod mais o sensor que esta dentro da raiz

    }
}
