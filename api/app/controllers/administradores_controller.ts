
import Administrador from '#models/Administrador';

export default class AdministradoresController {
  /*public async cadastrar( { request, auth } ) {
      let metodos = new MetodosUteiProvider()
      let admin = (await Administrador.query().whereNull('cancelado_em').where('id_usuario', auth.user.id_usuario))[0]
      let nome = request.body().nomeUsuario
      let senha = request.body().senha
      let login = request.body().login
      let usuario = await metodos.cadastrarUsuario(login, senha, nome)
      let instituicao = metodos.selecionarPorId(admin.id_instituicao, Instituicao, 'id_instituicao')
      const adm = await metodos.cadastrarAdm(instituicao.id_instituicao, usuario.id_usuario)

      return {
          sucesso: true,
          mensagem: "Administrador cadastrado com sucesso.",
          adm: adm
      }*/

  public async selecionarPorId({ auth, params }: any) {
    try {
      let administrador = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      if (!administrador) {
        return {
          sucesso: false,
          mensagem: 'Autorização inválida.',
        }
      }
      const admin = await Administrador.query().where('id_administrador', params.id_administrador);
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador não encontrado.'
        }
      }
      return {
        sucesso: true,
        adm: admin
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar o administrador.',
        error: error.message,
      }
    }
  }
}