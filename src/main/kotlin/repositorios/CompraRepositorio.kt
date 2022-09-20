package repositorios

import entidades.Compra
import entidades.Criptomonedas
import java.time.LocalDate
import java.time.LocalTime

object CompraRepositorio {
    val compra = mutableListOf<Compra>()

    init {
        CompraRepositorio.agregar(Compra("Andy", 0, LocalDate.of(2021,3,1), LocalTime.of(17,10,1), Criptomonedas.CRIPTODIA, 200.0, 10000.0, "2%"))
    }

    fun agregar(compra: Compra) {
        this.compra.add(compra)
    }

    fun eliminar(compra: Compra) {
        this.compra.remove(compra)
    }

    // No se usa.
    fun obtenerPorCodigo(codigoCompra: Int) {
        //TODO: Completar
        for (elemento in compra) {
            if (elemento.codigoCompra == codigoCompra) {
                val nombreCriptomoneda: String = stringuearTipoCripto(elemento.criptomoneda)
                println(
                    """
                     ---------------------------------------------   
                     Codigo de Compra: ${elemento.codigoCompra}
                     Fecha de Compra: ${elemento.fechaCompra}
                     Hora de Compra: ${elemento.horaCompra}
                     Criptomoneda: ${nombreCriptomoneda}
                     Valor Adquirido: $${elemento.valorAdquirido}
                     ValorPagado: $${elemento.valorPagado}
                     --------------------------------------------- 
                """.trimIndent()
                )
            }
        }
    }

    fun obtenerListaCompraPorUsuario(nickname: String){

        for (elemento in compra) {

            if(elemento.usuario.equals(nickname)){
                val nombreCriptomoneda: String = stringuearTipoCripto(elemento.criptomoneda)
                println(
                    """
                     ---------------------------------------------   
                     Codigo de Compra: ${elemento.codigoCompra}
                     Fecha de Compra: ${elemento.fechaCompra}
                     Hora de Compra: ${elemento.horaCompra}
                     Criptomoneda: ${nombreCriptomoneda}
                     Valor Adquirido: $${elemento.valorAdquirido}
                     Valor Pagado: $${elemento.valorPagado}
                     Valor comision: ${elemento.comision} 
                     --------------------------------------------- 
                """.trimIndent()
                )


            }

        }

    }

    private fun stringuearTipoCripto(criptomoneda: Criptomonedas): String {
        when(criptomoneda){

            Criptomonedas.CRIPTOMAS -> return "Criptomas"
            Criptomonedas.CRIPTODIA -> return "Criptodia"
            Criptomonedas.CARRECRIPTO -> return "Carrecripto"
        }

    }


}