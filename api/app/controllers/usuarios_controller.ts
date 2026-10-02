import hash from '@adonisjs/core/services/hash'
import Usuario from "#models/Usuario";
import Administrador from '#models/Administrador';
import mail from '@adonisjs/mail/services/main'

export default class UsuariosController {

  //Método para Login de Usuarios(Vendedores por enquanto)
  public async login({ request }: any) {


    //Recebe o login do usuario pelo body
    let login = request.body().login

    //Recebe a senha do usuario pelo body
    let senha = request.body().senha

    //Procura um usuario com esse login
    let usuario = (await Usuario.query().where('login', login))[0]

    //Verifica se existe esse usuario
    if (!usuario) {

      //Retorna erro na busca
      return {
        sucesso: false,
        mensagem: 'Usuario não encontrado'
      }

    }

    //Verifica se a senha do usuario está certa
    if (!await hash.verify(usuario.senha, senha)) {

      //Retorna senha incorreta
      return {
        sucesso: false,
        mensagem: 'Senha incorreta'
      }

    }

    //Cria um token para o usuario
    let token = await Usuario.accessTokens.create(
      usuario,
      ['*'],
      {
        name: "VivaPay",
        expiresIn: '1 day'
      }
    )

    //Retorna sucesso no login
    return {
      success: true,
      obj: {
        usuario: {
          ...(usuario.serialize()),
          token: token
        }
      }
    }
  }


  public async cadastrar({ request }: any) {
    try {
      const usuario = new Usuario()
      usuario.login = request.body().login
      usuario.senha = request.body().senha
      usuario.nome_usuario = request.body().nome
      const infoMessage = {
        login: request.body().login,
        senha: request.body().senha,
        nome: request.body().nome
      }
      await usuario.save()
      if (request.body().login != '' || request.body().login != null) {
        try {
          await mail.send((message) => {
            message
              .to(request.body().login)
              .from('contato.cashflowbr@gmail.com')
              .subject('Test Email')
              .htmlView('emails/verify_email_html', { infoMessage })
          })
        } catch (error) {
          console.error('Error sending email:', error)
        }
      }
      if (usuario.$isPersisted) {
        return {
          sucesso: true,
          usuario: usuario,
          mensagem: 'Usuário cadastrado com sucesso!',
        }
      }
      return {
        sucesso: false,
        mensagem: 'Erro ao cadastrar usuário!',
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro no cadastro. Verifique as informações inseridas.'
      }
    }
  }

  public async selecionarPorId({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Autorização inválida.',
        }
      }
      const usuario = await Usuario.query()
        .from('usuarios as u')
        .where('u.id_usuario', params.id_usuario)
      return {
        sucesso: true,
        usuario: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todos os vendedores',
        error: error.message,
      }
    }
  }
}

