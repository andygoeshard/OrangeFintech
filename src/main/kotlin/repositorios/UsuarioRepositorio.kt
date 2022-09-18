package repositorios

import entidades.Usuario
import java.time.LocalDate

object UsuarioRepositorio {

    val usuarios = mutableListOf<Usuario>()

    init{

        usuarios.add(Usuario("Andy", "And1!", 0,"Andres", "Aquino", 2000.0, 0.0, LocalDate.of(2020,1,1)))

    }

    fun agregar(usuario: Usuario) {
        if(existe(usuario.nickname)){
            //TODO fallar
        }
        usuarios.add(usuario)
    }

    fun eliminar(usuario: Usuario) {
        usuarios.remove(usuario)
    }

    fun existe(nickname: String): Boolean {

        for(elemento in usuarios){
            if(elemento.nickname.equals(nickname)) {
                return true
            }
        }
        return false

    }

    fun iniciar(nickname: String, password: String): Usuario {
        //TODO: Completar
        val usuarioIniciado: Usuario
        for(elemento in usuarios){
            if(elemento.nickname.equals(nickname) && elemento.password.equals(password)){
                usuarioIniciado = elemento
            }
        }
        return usuarioIniciado
    }

    fun obtenerPorCodigo(codigoCuenta:Int){
        //TODO: Completar
        for(elemento in usuarios){
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

    fun editarPorCodigo(codigoCuenta: Int, usuario: Usuario){

        usuarios[codigoCuenta] = usuario

    }
}