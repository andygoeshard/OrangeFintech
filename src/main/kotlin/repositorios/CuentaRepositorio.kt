package repositorios

import entidades.Compra
import entidades.Cuenta

class CuentaRepositorio {
    val cuentas = mutableListOf<Cuenta>()

    fun agregar(cuenta: Cuenta) {
        cuentas.add(cuenta)
    }

    fun eliminar(cuenta: Cuenta) {
        cuentas.remove(cuenta)
    }

    fun obtenerPorCodigo(codigoCuenta:Int){
        //TODO: Completar
        for(elemento in cuentas){
            if(elemento.codigoCuenta.equals(codigoCuenta)){
                print("""
                   Codigo de Cuenta: ${elemento.codigoCuenta}
                   Nombre: ${elemento.nombre}
                   Apellido: ${elemento.apellido}
                   Dinero en cuenta: $${elemento.dineroEnCuenta}
                   Fecha de alta: ${elemento.fechaAlta}
                """.trimIndent())
            }
        }
    }
