package net.azarquiel.blackjack

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import net.azarquiel.blackjack.model.Carta
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var random: Random
    private lateinit var  llcartas: LinearLayout
    val mazo = Array(40){i -> Carta() }
    var imazo=0
    val palos = arrayOf("clubs","diamonds","hearts","spades")
    var jugador = 0
    var puntos= Array(2){0}
    val handler = Handler()
    val delayMillis = 2000
    private var titulo="BLACKJACK"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titulo= title.toString()

        random = Random(System.currentTimeMillis())
        llcartas=findViewById<LinearLayout>(R.id.llcartas)
        var ivmazo = findViewById<ImageView>(R.id.ivmazo)
        var btnparar = findViewById<Button>(R.id.btnparar)

        ivmazo.setOnClickListener {ivmazoOnClick()}
        btnparar.setOnClickListener {btnpararOnClick()}

        newgame()
    }



    private fun newgame() {

       var c=0
       for (p in 0..3){
           for (n in 1..10){
               mazo[c]= Carta(n,p)
               c++
           }
       }
        mazo.shuffle(random)
       /*for (carta in mazo){carta.palo
           Log.d("paco", "${carta.numero} de ${palos[carta.palo]} ")
       }*/
        sacaCarta()
        sacaCarta()
    }

    private fun ivmazoOnClick() {
        sacaCarta()
        comprobar()
    }
    private fun btnpararOnClick() {
        llcartas.removeAllViews()
        if (jugador==0){
            jugador=1
            newgame()
        }else if (jugador==1){
            showalert()

        }


    }
    private fun sacaCarta(){
        val cartaJuego = mazo[imazo]
        var n = if (cartaJuego.numero>7)10 else cartaJuego.numero

        puntos[jugador]+=n
        imazo++
        val ivCarta= ImageView(this)
        val idimage= resources.getIdentifier("${palos[cartaJuego.palo]}${cartaJuego.numero}","drawable",packageName)
        ivCarta.setImageResource(idimage)
        val lp=LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.setMargins(8,0,8,0)
        ivCarta.layoutParams=lp

        llcartas.addView(ivCarta,0)

        title="${titulo}-${puntos[jugador]} puntos"



    }
    private fun reinicio(){
        puntos[0]=0
        puntos[1]=0
        jugador=0
        llcartas.removeAllViews()
    }
    private fun comprobar(){
        if (puntos[0]>21&&jugador==0){
            llcartas.removeAllViews()
            handler.postDelayed({
                jugador=1
                newgame()
            }, delayMillis.toLong())
            showalert2()

        }else if(puntos[1]>21&&jugador==1) {
            showalert()

        }
    }

    private lateinit var alerta2: AlertDialog
    private fun showalert2() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Has perdido, le toca al jugador 2")
        builder.setMessage("  ")
        builder.setMessage("  ")
        builder.setMessage("  ")
        builder.setPositiveButton("Continuar") { _, _ ->

            alerta2.dismiss()

        }


        alerta2 = builder.create()
        alerta2.show()
    }

    private fun showalert(){
        val builder = AlertDialog.Builder(this)
        var ptnju1= 21-puntos.get(0)
        var ptnju2= 21-puntos.get(1)
        builder.setTitle("RESULTADO")
        if (ptnju1>ptnju2&&puntos[0]<=21){
            builder.setMessage("Jugador 1: ${puntos[0]}puntos \nJugador 2: ${puntos[1]}puntos \nEl ganador es el jugador 1 ")
        }else if (ptnju2>ptnju1&&puntos[1]<=21){
            builder.setMessage("Jugador 1: ${puntos[0]}puntos \nJugador 2: ${puntos[1]}puntos \nEl ganador es el jugador 2 ")
        }else if (ptnju2==ptnju1&&puntos[0]<=21&&puntos[1]<=21){
            builder.setMessage("Jugador 1: ${puntos[0]}puntos \nJugador 2: ${puntos[1]}puntos \nHabeis empatado")
        }else if (puntos[0]>21&&puntos[1]>21){
            builder.setMessage("Jugador 1: ${puntos[0]}puntos \nJugador 2: ${puntos[1]}puntos \nHabeis perdido")
        }

        builder.setPositiveButton("nueva partida") {_,_->
            reinicio()
            newgame()

        }
        builder.setNegativeButton("Fin") {_,_->
            finish()
        }
        builder.show()

    }

}
