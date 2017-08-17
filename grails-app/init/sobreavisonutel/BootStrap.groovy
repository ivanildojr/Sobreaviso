package sobreavisonutel

import sobreavisonutel.seguranca.Perfil
import sobreavisonutel.seguranca.Usuario
import sobreavisonutel.seguranca.UsuarioPerfil

class BootStrap {

    def init = { servletContext ->

//        def adminRole = new Perfil(authority: 'ROLE_ADMIN').save()
//        def userRole = new Perfil(authority: 'ROLE_USER').save()
//
//        def testUser = new Usuario(username: 'ivanildo', password: 'senha').save()
//
//        UsuarioPerfil.create testUser, adminRole
//
//        UsuarioPerfil.withSession {
//            it.flush()
//            it.clear()
//        }
//
//        assert Usuario.count() == 1
//        assert Perfil.count() == 2
//        assert UsuarioPerfil.count() == 1
//
//
//        for (int i = 1; i < 8; i++) {
//            for (int j = 0; j < 24; j++) {
//                Escala escala = new Escala()
//                escala.atendentes = null
//                escala.dia = i
//                escala.hora = j
//                escala.save(flush:true)
//            }
//        }
//
//        Atendentes atendente = new Atendentes()
//        atendente.nome = "Ivanildo"
//        atendente.telefone = "991122659"
//        atendente.save(flush:true)
//        Atendentes atendente2 = new Atendentes()
//        atendente2.nome = "Torres"
//        atendente2.telefone = "991121755"
//        atendente2.save(flush:true)
//        Atendentes atendente3 = new Atendentes()
//        atendente3.nome = "Rudsom"
//        atendente3.telefone = "996477123"
//        atendente3.save(flush:true)


    }
    def destroy = {
    }
}
