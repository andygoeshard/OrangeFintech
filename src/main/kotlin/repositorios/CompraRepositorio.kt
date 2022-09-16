package repositorios

import entidades.Compra
import java.time.LocalDate

class CompraRepositorio {
    val compra = mutableListOf<Compra>()

    fun agregar(compra: Compra) {
        this.compra.add(compra)
    }

    fun eliminar(compra: Compra) {
        this.compra.remove(compra)
    }

    fun obtenerPorCodigo(codigoCompra:Int){
        //TODO: Completar
        for(elemento in compra){
            if(elemento.codigoCompra == codigoCompra){
                println("""
                     Codigo de Compra: ${elemento.codigoCompra}
                     Fecha de Compra: ${elemento.fechaCompra}
                     Hora de Compra: ${elemento.horaCompra}
                     Criptomoneda: ${elemento.criptomoneda}
                     Valor Adquirido: $${elemento.valorAdquirido}
                     ValorPagado: $${elemento.valorPagado}
                """.trimIndent())
            }
        }
    }