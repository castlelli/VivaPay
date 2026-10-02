import Cliente from '#models/Cliente'
import Administrador from '#models/Administrador'
import Usuario from '#models/Usuario'

export default class ClienteController {
  public async selecionarTodos({ auth }: any) {
    try {
      const clientes = (await this.selecionarTodosInstituicao(auth.user)).clientes
      return {
        sucesso: true,
        clientes: clientes,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todos os clientes',
        error: error.message,
      }
    }
  }

  public async selecionarTodosInstituicao(user: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', user.id_usuario))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const clientes = await Cliente.query()
        .join('administradores as a', 'a.id_administrador', 'clientes.criado_por')
        .where('a.id_instituicao', admin.id_instituicao)
        .select('clientes.*')
      return {
        sucesso: true,
        clientes: clientes,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todos os clientes',
        error: error.message,
      }
    }
  }

  /* public async selecionarPorId({ params, auth }: any) {
    try {
      const cliente = await Cliente.query().findById(params.id_cliente)
      if (!cliente) {
        throw new Error('Cliente não encontrado.')
      }
      return {
        sucesso: true,
        cliente: cliente,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar cliente por ID',
        error: error.message,
      }
    }
  }*/

  /*
  public async selecionarPorDataCriacao({ request, auth }: any) {
    try {
      const { dataInicial, dataFinal } = request.body()
      const clientes = await Cliente.query().whereBetween('criado_em', [dataInicial, dataFinal])
      return {
        sucesso: true,
        clientes: clientes,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar clientes por criado_em',
        error: error.message,
      }
    }
  }

  public async selecionarPorLogin({ params, auth }: any) {
    try {
      const cliente = await Cliente.query().findOne({ login: params.login })
      if (!cliente) {
        throw new Error('Cliente não encontrado com este login.')
      }
      return {
        sucesso: true,
        cliente: cliente,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar cliente por login',
        error: error.message,
      }
    }
  }
}*/

  public async cadastrar({request }: any) {
    try {
      // Verifica se o usuário é válido
      let usuario = (await Usuario.query().where('id_usuario', request.body().id_usuario))[0]
      if (!usuario) {
        return {
          sucesso: false,
          mensagem: 'Usuário não encontrado.',
        }
      }

      let clienteExistente = (await Cliente.query().where('id_usuario', request.body().id_usuario))[0]
      if (clienteExistente) {
        return {
          sucesso: false,
          mensagem: 'Cliente já cadastrado.',
        }
      }

      let existeUsuarioCliente = (await Cliente.query().where('id_usuario', request.body().id_usuario))[0]
      if (existeUsuarioCliente) {
        return {
          sucesso: false,
          mensagem: 'Já existe um cliente nesse usuário.'

        }
      }

      const cliente = new Cliente()

      cliente.id_usuario = request.body().id_usuario

      await cliente.save()

      if (cliente.$isPersisted) {
        return {
          sucesso: true,
          cliente: cliente,
          mensagem: 'Cliente cadastrado com sucesso!',
        }
      } else {
        return {
          sucesso: false,
          mensagem: 'Erro ao cadastrar o cliente.',
        }
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro no cadastro do cliente.',
        error: error.message,
      }
    }
  }
}
