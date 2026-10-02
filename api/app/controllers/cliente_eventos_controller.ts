import ClienteEvento from '#models/ClienteEvento'
import Administrador from '#models/Administrador'
import Evento from '#models/Evento'
import Cliente from '#models/Cliente'

export default class ClienteEventosController {
  /* Método para consultar o saldo dos clientes */
  public async consultarSaldo({ params }: any) {
    let clienteEvento = (
      await ClienteEvento.query()
        .whereHas('cliente', (q) => {
          q.where('voucher', params.voucher)
        })
        .whereHas('evento', (q) => {
          q.whereNull('desativado_em')
        })
    )[0]

    // Carregar as transacoes('banco de dados transacao') do cliente
    await clienteEvento.load('transacao')

    // Verificar se existe alguma transação desse cliente
    if (clienteEvento) {
      // Chamar o método-atributo de clienteEvento que calcula o saldo e carrega-lo em uma variavel
      let saldo = clienteEvento.saldo

      // Retornar o saldo por um JSON
      return {
        sucesso: true,
        saldo: saldo,
        mensagem: 'O seu saldo é de: ' + saldo

      }
    }

    // Retornar que nenhum cliente foi encontrado
    else {
      return 'Cliente não encontrado'
    }
  }

  // Método para fazer o depósito para o client

  //Métodos de seleção

  public async selecionarTodos({ auth }: any) {
    const clientesEvento = (await this.selecionarTodosInstituicao(auth.user)).clienteEventos
    return {
      sucesso: true,
      clienteEventos: clientesEvento,
    }
  }

  public async selecionarTodosInstituicao(user: any) {
    let admin = (await Administrador.query().where('id_usuario', user.id_usuario))[0]
    const clientesEvento = await ClienteEvento.query()
      .from('cliente_eventos as ce')
      .select('ce.*')
      .join('clientes as c', 'ce.id_cliente', 'c.id_cliente')
      .join('eventos as e', 'ce.id_evento', 'e.id_evento')
      .join('administradores as a', 'e.responsavel', 'a.id_administrador')
      .where('a.id_instituicao', admin.id_instituicao)
    return {
      sucesso: true,
      clienteEventos: clientesEvento,
    }
  }

  public async selecionarPorId({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.clienteEventos

      if (!todos) {
        throw new Error('Não foi possível recuperar os eventos de clientes.')
      }

      const clienteEvento = todos.filter(
        (clienteEvento) => clienteEvento.id_cliente_evento == params.id_cliente_evento
      )
      return {
        sucesso: true,
        clienteEvento: clienteEvento,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar evento de cliente por ID',
        error: error.message,
      }
    }
  }

  public async selecionarPorCliente({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.clienteEventos

      if (!todos) {
        throw new Error('Não foi possível recuperar os eventos de clientes.')
      }

      const clienteEventos = todos.filter(
        (clienteEvento) => clienteEvento.id_cliente == params.id_cliente
      )
      return {
        sucesso: true,
        clienteEventos: clienteEventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar evento de cliente por ID do cliente',
        error: error.message,
      }
    }
  }

  public async selecionarPorEvento({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.clienteEventos

      if (!todos) {
        throw new Error('Não foi possível recuperar os eventos de clientes.')
      }

      const clienteEventos = todos.filter(
        (clienteEvento) => clienteEvento.id_evento == params.id_evento
      )
      return {
        sucesso: true,
        clienteEventos: clienteEventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar evento de cliente por ID do evento',
        error: error.message,
      }
    }
  }

  public async selecionarComSaldo({ params }: any) {
    const clientes = (await this.selecionarPorEvento(params.id_evento)).clienteEventos

    if (!clientes) {
      return {
        sucesso: true,
        mensagem: 'Não há clientes ativos nesse evento.',
      }
    }
    const clientesComSaldo = await clientes?.filter((cliente) => cliente.saldo > 0)
    if (!clientesComSaldo) {
      return {
        sucesso: true,
        mensagem: 'Não há clientes com saldo nesse evento.',
      }
    }
    return {
      sucesso: true,
      clientesComSaldo: clientesComSaldo,
    }
  }

  public async selecionarPorVoucher({ params, auth }: any) {
    try {
      let clientes = (await this.selecionarTodosInstituicao(auth.user)).clienteEventos
      if (!clientes) {
        return {
          sucesso: false,
          mensagem: 'Erro ao encontrar clientes correspondentes.',
        }
      }
      const cliente = await clientes.filter((cliente) => cliente.voucher == params.voucher)
      if (!cliente) {
        throw new Error('Cliente não encontrado com este voucher.')
      }
      return {
        sucesso: true,
        cliente: cliente,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar cliente por voucher',
        error: error.message,
      }
    }
  }

  public async cadastrar({ request }: any) {
    try {

      let evento = (await Evento.query().where('id_evento', request.body().id_evento))[0]
      if (!evento) {
        return {
          sucesso: false,
          mensagem: 'Evento não encontrado.',
        }
      }

      let clienteExistente = (await Cliente.query().where('id_cliente', request.body().id_cliente))[0]
      if (!clienteExistente) {
        return {
          sucesso: false,
          mensagem: 'Cliente não encontrado.',
        }
      }

      let existeClienteEvento = (await ClienteEvento.query().where('id_cliente', request.body().id_cliente))[0]
      if (existeClienteEvento) {
        return {
          sucesso: false,
          mensagem: 'Esse cliente já existe.'
        }
      }



      let existeVoucher: boolean = true
      let codigo: any
      while (existeVoucher) {
        codigo = Math.floor(Math.random() * 99999)
        const resultado = await ClienteEvento.query().where('voucher', codigo).first()
        existeVoucher = resultado !== null
      }


      const clienteEvento = new ClienteEvento()
      clienteEvento.voucher = codigo?.toString()
      clienteEvento.id_evento = request.body().id_evento
      clienteEvento.id_cliente = request.body().id_cliente
      await clienteEvento.save()

      if (clienteEvento.$isPersisted) {
        return {
          sucesso: true,
          clienteEvento: clienteEvento,
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
