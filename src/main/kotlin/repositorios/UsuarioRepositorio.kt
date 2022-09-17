package repositorios

import entidades.Cuenta
import entidades.Usuario

class UsuarioRepositorio {
    val usuarios = mutableListOf<Usuario>()

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

    fun iniciar(nickname: String, password: String): List<Usuario> {
        //TODO: Completar
        val usuariosIniciados = mutableListOf<Usuario>()
        for(elemento in usuarios){
            if(elemento.nickname.equals(nickname) && elemento.password.equals(password)){
                usuariosIniciados.add(elemento)
            }
        }
        return usuariosIniciados
    }
}