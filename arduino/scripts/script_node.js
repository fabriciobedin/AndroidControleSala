var firebase = require('firebase');

//essa parte, é copiada do firebase
//após criar o projeto, abra a opção projeto Web e copie o código pra cá
//apenas o que está entre os traços abaixo
//------------------------------------------------------------------------------------
var config = {
    apiKey: "AIzaSyBNyAjPEu584bMTqRwb2kZ-BcMBtBs4inU",
    authDomain: "controle-sala.firebaseapp.com",
    databaseURL: "https://controle-sala.firebaseio.com",
    storageBucket: "controle-sala.appspot.com",
    messagingSenderId: "587803273779"
  };
  firebase.initializeApp(config);
//------------------------------------------------------------------------------------

//path do banco
var rootRef = firebase.database().ref();

var db = firebase.database();


//jhonny five
var five = require("johnny-five");
var board = new five.Board();
var pin = "A1";


board.on("ready", function() {
  var light = new five.Light("A0");
  var valueLight = 0;
  var led = new five.Led(13);
  

  light.on("change", function() {
		if(valueLight != this.level){
			valueLight = this.level*100;
      valueLight = parseInt(valueLight);
      if (valueLight != (valueLight+1) || valueLight != (valueLight-1)) {
        console.log(valueLight);
        firebase.database().ref('sensor').set({
          light : valueLight
        })  
      }
		}
 	});

  var ref = db.ref("ligaLuz");
  ref.orderByKey().on("child_changed", function(snapshot) {
    if (snapshot.val() == 1) {
      led.on();
      console.log("Luz Ligada");
    } else{
      led.off();
      console.log("Luz Desligada");
    }
  });


  var button = new five.Button({
    pin: 2, 
    invert: true
  });


  button.on("press", function() {
    var data = new Date();
    var horarioCompleto = data.getDate() + "/" + (data.getMonth()+1) +"/"+ data.getFullYear() + "  " + 
                          data.getHours() + ":" + data.getMinutes() + ":" + data.getSeconds();
    
    console.log( "Porta Aberta" );
    console.log( horarioCompleto );
    firebase.database().ref('sensorPorta').set({
          porta : 1         
    })

    firebase.database().ref('horarioPortaAbriu').set({
          dia : horarioCompleto          
    })
  });

  button.on("release", function() {
    var data = new Date();
    var horarioCompleto = data.getDate() + "/" + (data.getMonth()+1) +"/"+ data.getFullYear() + "  " + 
                          data.getHours() + ":" + data.getMinutes() + ":" + data.getSeconds();

    console.log( "Porta Fechada" );
    console.log( horarioCompleto );
    firebase.database().ref('sensorPorta').set({
          porta : 0
    })

    firebase.database().ref('horarioPortaFechou').set({
          dia : horarioCompleto         
    })
  });

  new five.Thermometer({
    pin: "A1",
    toCelsius: function(raw) { // optional
      return (raw / sensivity) + offset;
    }
  });

 });