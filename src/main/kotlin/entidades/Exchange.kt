package entidades

import java.time.LocalTime
import java.util.*

abstract class Exchange{
    abstract fun calcularComision(compra: Compra): Double
}

object Criptomas: Exchange(){
    override fun calcularComision(compra: Compra): Double {
        return 0.02 // Comision del 2%
    }
}

object Criptodia: Exchange(){
    override fun calcularComision(compra: Compra): Double {
        if(compra.horaCompra in LocalTime.of(20,0,0,0)..LocalTime.of(23,59,0,0)){
            return 0.01 // Comision del 1% entre las 20:00 y las 23:59
        } else{
            return 0.03 // Comision del 3%
        }
    }
}

object Carrecripto: Exchange(){
    override fun calcularComision(compra: Compra): Double {

        val diaDeHoy = Calendar.getInstance(); // Obtiene el dia de hoy
        if(diaDeHoy.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || diaDeHoy.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) return 0.3
        // Si es sabado o domingo retorna 3% de comision
        else return 0.075
        // Cualquier otro dia retorna comision de 0.75%
    }
}

fun cobrarComision(exchange: Exchange, compra: Compra): Double{
    return exchange.calcularComision(compra)
}