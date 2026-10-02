import VendedorEvento from '#models/VendedorEvento'
import Administrador from '#models/Administrador'
import { isAfter, isBefore, isEqual } from 'date-fns'
import OperacaoVendedorEvento from '#models/OperacaoVendedorEvento'
import { DateTime } from 'luxon'
import Vendedor from '#models/Vendedor'
import Evento from '#models/Evento'
import Usuario from '#models/Usuario'
import TransacaoController from './transacoes_controller.js'
import Transacao from '#models/Transacao'

export default class VendedorEventoController {
  public async cadastrar({ auth, request }: any) {
    let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
    try {
      let id_vendedor = request.body().id_vendedor
      let id_evento = request.body().id_evento
      let jaExiste = (
        await VendedorEvento.query()
          .where('id_vendedor', id_vendedor)
          .andWhere('id_evento', id_evento)
      )[0]

      if (jaExiste) {
        return {
          sucesso: false,
          mensagem: 'Esse vendedor já está nesse evento.',
        }
      }

      const vendedorEvento = new VendedorEvento()
      vendedorEvento.id_vendedor = id_vendedor
      vendedorEvento.id_evento = id_evento
      vendedorEvento.responsavel = admin.id_administrador
      await vendedorEvento.save()
      if (vendedorEvento.$isPersisted) {
        return {
          sucesso: true,
          mensagem: 'Vendedor cadastrado com sucesso no evento.',
        }
      } else {
        return {
          sucesso: false,
          mensagem: 'Erro no cadastro do vendedor no evento.',
        }
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro no cadastro do vendedor no evento.',
      }
    }
  }
  public async selecionarTodos({ auth }: any) {
    try {
      const vendedorEventos = (await this.selecionarTodosInstituicao(auth.user)).vendedorEventos
      return {
        sucesso: true,
        vendedorEventos: vendedorEventos
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todos os eventos de vendedores',
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
      const vendedorEventos = await VendedorEvento.query()
        .from('vendedor_eventos as ve')
        .join('vendedores as v', 've.id_vendedor', 'v.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('a.id_instituicao', admin.id_instituicao)
        .select('ve.*');

      return {
        sucesso: true,
        vendedorEventos: vendedorEventos
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todos os eventos de vendedores',
        error: error.message,
      }
    }
  }

  public async selecionarPorId({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.vendedorEventos

      if (!todos) {
        throw new Error('Não foi possível recuperar os eventos de vendedores.')
      }

      const vendedorEvento = todos.filter(
        (vendedorEvento) => vendedorEvento.id_vendedor_evento == params.id_vendedor_evento
      )
      return {
        sucesso: true,
        vendedorEvento: vendedorEvento,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar evento de vendedor por ID',
        error: error.message,
      }
    }
  }

  public async selecionarPorVendedor({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.vendedorEventos

      if (!todos) {
        throw new Error('Não foi possível recuperar os eventos de vendedores.')
      }

      const vendedorEventos = todos.filter(
        (vendedorEvento) => vendedorEvento.id_vendedor == params.id_vendedor
      )
      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar evento de vendedor por ID do vendedor',
        error: error.message,
      }
    }
  }

  public async buscarSaldoLoja({ auth }: any) {
    let resultado = (await Transacao.query()
      .from('transacoes as t')
      .join('vendedor_eventos as ve', 't.id_vendedor_evento', 've.id_vendedor_evento')
      .join('vendedores as v', 'v.id_vendedor', 've.id_vendedor').where('ve.id_vendedor', auth.user.$preloaded.vendedor[0].id_vendedor)
      .select('v.loja')
      .sum('t.valor as saldo')
      .groupBy('v.loja'));
    return {
      sucesso: true,
      saldo: resultado.map(transacao => transacao.$extras)
    }
  }

  public async selecionarParaVendedor({ params, auth }: any) {
    console.log(auth.user)
    try {
      let admin = (await Administrador.query().where('id_administrador', auth.user.$preloaded.vendedor[0].responsavel))[0]
      let resultado = await this.selecionarTodosInstituicao(admin)
      let todos = resultado.vendedorEventos
      let usuario = (await Usuario.query().where('id_usuario', auth.user.$preloaded.vendedor[0].id_usuario))
      if (!todos) {
        throw new Error('Não foi possível recuperar os eventos de vendedores.')
      }

      const vendedorEventos = todos.filter(
        (vendedorEvento) => vendedorEvento.id_vendedor == auth.user.$preloaded.vendedor[0].id_vendedor
      )
      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
        usuarioAtual: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar evento de vendedor por ID do vendedor',
        error: error.message,
      }
    }
  }

  public async selecionarPorEvento({ params, auth }: any) {
    try {

      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.vendedorEventos

      if (!todos) {
        throw new Error('Não foi possível recuperar os eventos de vendedores.')
      }

      const vendedorEventos = todos.filter(
        (vendedorEvento) => vendedorEvento.id_evento == params.id_evento
      )
      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar evento de vendedor por ID do evento',
        error: error.message,
      }
    }
  }

  public async selecionarPorDataCriacao({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.vendedorEventos
      let dataInicial = new Date(params.dataInicial)
      let dataFinal = new Date(params.dataFinal)

      if (!todos) {
        throw new Error('Não foi possível recuperar os eventos de vendedores.')
      }

      const vendedorEventos = todos.filter(
        (vendedorEvento) =>
          (isEqual(vendedorEvento.criado_em.toJSDate(), dataInicial) ||
            isAfter(vendedorEvento.criado_em.toJSDate(), dataInicial)) &&
          (isEqual(vendedorEvento.criado_em.toJSDate(), dataFinal) ||
            isBefore(vendedorEvento.criado_em.toJSDate(), dataFinal))
      )
      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar evento de vendedor por data de criação',
        error: error.message,
      }
    }
  }

  public async selecionarPorAtivo({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.vendedorEventos

      if (!todos) {
        throw new Error('Não foi possível recuperar os eventos de vendedores.')
      }

      const estadoAtivo =
        params.estado === 'true' ? true : params.estado === 'false' ? false : params.estado
      const vendedorEventos = todos.filter((vendedorEvento) => vendedorEvento.ativo === estadoAtivo)
      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar evento de vendedor por atividade',
        error: error.message,
      }
    }
  }

  //Métodos de seleção por quantidade.

  public async selecionarTodosPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const vendedorEventos = await VendedorEvento.query()
        .from('vendedor_eventos as ve')
        .join('vendedores as v', 've.id_vendedor', 'v.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .orderBy('ve.id_vendedor_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('ve.*');

      const loja = await Vendedor.query()
        .whereIn('id_vendedor', vendedorEventos.map(ve => ve.id_vendedor))
        .select('id_vendedor', 'loja');

      const evento = await Evento.query()
        .whereIn('id_evento', vendedorEventos.map(ve => ve.id_evento))
        .select('id_evento', 'nome_evento');

      const usuario = await Usuario.query()
        .whereIn('id_usuario', vendedorEventos.map(ve => ve.responsavel))
        .select('id_usuario', 'nome_usuario');

      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
        vendedor: loja,
        evento: evento,
        usuario: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar eventos de vendedores por quantidade',
        error: error.message,
      }
    }
  }

  public async selecionarPorVendedorPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const vendedorEventos = await VendedorEvento.query()
        .from('vendedor_eventos as ve')
        .join('vendedores as v', 've.id_vendedor', 'v.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('ve.id_vendedor', params.id_vendedor)
        .orderBy('ve.id_vendedor_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('ve.*');

      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar eventos de vendedor por ID do vendedor e quantidade',
        error: error.message,
      }
    }
  }

  public async selecionarPorLojaPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }

      const vendedor = await Vendedor.query()
        .where('loja', params.loja)
        .select('id_vendedor', 'loja');

      const vendedorEventos = await VendedorEvento.query()
        .from('vendedor_eventos as ve')
        .join('vendedores as v', 've.id_vendedor', 'v.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .whereIn('ve.id_vendedor', vendedor.map(v => v.id_vendedor))
        .orderBy('ve.id_vendedor_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('ve.*');

      const evento = await Evento.query()
        .whereIn('id_evento', vendedorEventos.map(ve => ve.id_evento))
        .select('id_evento', 'nome_evento');

      const usuario = await Usuario.query()
        .whereIn('id_usuario', vendedorEventos.map(ve => ve.responsavel))
        .select('id_usuario', 'nome_usuario');

      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
        vendedor: vendedor,
        evento: evento,
        usuario: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar vendedor evento por loja por quantidade',
        error: error.message,
      }
    }
  }

  public async selecionarPorLoja({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }

      const vendedor = await Vendedor.query()
        .where('loja', params.loja)
        .select('id_vendedor');

      const vendedorEventos = await VendedorEvento.query()
        .from('vendedor_eventos as ve')
        .join('vendedores as v', 've.id_vendedor', 'v.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .whereIn('ve.id_vendedor', vendedor.map(v => v.id_vendedor))
        .orderBy('ve.id_vendedor_evento')
        .select('ve.*');

      const usuario = await Usuario.query()
        .whereIn('id_usuario', vendedorEventos.map(ve => ve.responsavel))
        .select('id_usuario', 'nome_usuario');

      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
        usuario: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar vendedor evento por loja por quantidade',
        error: error.message,
      }
    }
  }

  public async selecionarParaVendedorPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_administrador', auth.user.$preloaded.vendedor[0].responsavel))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const vendedorEventos = await VendedorEvento.query()
        .from('vendedor_eventos as ve')
        .join('vendedores as v', 've.id_vendedor', 'v.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('ve.id_vendedor', auth.user.$preloaded.vendedor[0].id_vendedor)
        .orderBy('ve.id_vendedor_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('ve.*');

      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar eventos de vendedor para o vendedor autenticado por quantidade',
        error: error.message,
      }
    }
  }

  public async selecionarPorEventoPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const vendedorEventos = await VendedorEvento.query()
        .from('vendedor_eventos as ve')
        .join('vendedores as v', 've.id_vendedor', 'v.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('ve.id_evento', params.id_evento)
        .orderBy('ve.id_vendedor_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('ve.*');

      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar eventos de vendedor por ID do evento e quantidade',
        error: error.message,
      }
    }
  }

  public async selecionarPorDataCriacaoPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const vendedorEventos = await VendedorEvento.query()
        .from('vendedor_eventos as ve')
        .join('vendedores as v', 've.id_vendedor', 'v.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .whereBetween('ve.criado_em', [params.dataInicial, params.dataFinal])
        .orderBy('ve.id_vendedor_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('ve.*');

      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar eventos de vendedor por data de criação e quantidade',
        error: error.message,
      }
    }
  }

  public async selecionarPorAtivoPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const estadoAtivo = params.estado === 'true' ? true : params.estado === 'false' ? false : params.estado
      const vendedorEventos = await VendedorEvento.query()
        .from('vendedor_eventos as ve')
        .join('vendedores as v', 've.id_vendedor', 'v.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('ve.ativo', estadoAtivo)
        .orderBy('ve.id_vendedor_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('ve.*');

      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar eventos de vendedor por atividade e quantidade',
        error: error.message,
      }
    }
  }

  //Rotas especificamente para uso no aplicativo, usando o preload de vendedor.

  public async selecionarTodosPorQuantidadeVendedor({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_administrador', auth.user.$preloaded.vendedor[0].responsavel))[0];
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const vendedorEventos = await VendedorEvento.query()
        .from('vendedor_eventos as ve')
        .join('vendedores as v', 've.id_vendedor', 'v.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .orderBy('ve.id_vendedor_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('ve.*');

      return {
        sucesso: true,
        vendedorEventos: vendedorEventos
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar eventos de vendedores por quantidade',
        error: error.message,
      }
    }
  }

  public async selecionarPorVendedorPorQuantidadeVendedor({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_administrador', auth.user.$preloaded.vendedor[0].responsavel))[0];
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const vendedorEventos = await VendedorEvento.query()
        .from('vendedor_eventos as ve')
        .join('vendedores as v', 've.id_vendedor', 'v.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('ve.id_vendedor', params.id_vendedor)
        .orderBy('ve.id_vendedor_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('ve.*');

      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar eventos de vendedor por ID do vendedor e quantidade',
        error: error.message,
      }
    }
  }

  public async selecionarParaVendedorPorQuantidadeVendedor({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_administrador', auth.user.$preloaded.vendedor[0].responsavel))[0];
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const vendedorEventos = await VendedorEvento.query()
        .from('vendedor_eventos as ve')
        .join('vendedores as v', 've.id_vendedor', 'v.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('ve.id_vendedor', auth.user.$preloaded.vendedor[0].id_vendedor)
        .orderBy('ve.id_vendedor_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('ve.*');

      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar eventos de vendedor para o vendedor autenticado por quantidade',
        error: error.message,
      }
    }
  }

  public async selecionarPorEventoPorQuantidadeVendedor({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_administrador', auth.user.$preloaded.vendedor[0].responsavel))[0];
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const vendedorEventos = await VendedorEvento.query()
        .from('vendedor_eventos as ve')
        .join('vendedores as v', 've.id_vendedor', 'v.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('ve.id_evento', params.id_evento)
        .orderBy('ve.id_vendedor_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('ve.*');

      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar eventos de vendedor por ID do evento e quantidade',
        error: error.message,
      }
    }
  }

  public async selecionarPorDataCriacaoPorQuantidadeVendedor({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_administrador', auth.user.$preloaded.vendedor[0].responsavel))[0];
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const vendedorEventos = await VendedorEvento.query()
        .from('vendedor_eventos as ve')
        .join('vendedores as v', 've.id_vendedor', 'v.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .whereBetween('ve.criado_em', [params.dataInicial, params.dataFinal])
        .orderBy('ve.id_vendedor_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('ve.*');

      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar eventos de vendedor por data de criação e quantidade',
        error: error.message,
      }
    }
  }

  public async selecionarPorAtivoPorQuantidadeVendedor({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_administrador', auth.user.$preloaded.vendedor[0].responsavel))[0];
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const estadoAtivo = params.estado === 'true' ? true : params.estado === 'false' ? false : params.estado;
      const vendedorEventos = await VendedorEvento.query()
        .from('vendedor_eventos as ve')
        .join('vendedores as v', 've.id_vendedor', 'v.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('ve.ativo', estadoAtivo)
        .orderBy('ve.id_vendedor_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('ve.*');

      return {
        sucesso: true,
        vendedorEventos: vendedorEventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar eventos de vendedor por atividade e quantidade',
        error: error.message,
      }
    }
  }

  public async alternarAtivo({ params, auth }: any) {
    try {

      const vendedor = await VendedorEvento.findOrFail(params.id_vendedor_evento)
      vendedor.ativo = !vendedor.ativo
      vendedor.save()
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      let operacao = new OperacaoVendedorEvento()
      operacao.tipo = vendedor.ativo ? "A" : "D"
      operacao.id_vendedor_evento = vendedor.id_vendedor
      operacao.responsavel = admin.id_administrador
      operacao.data_operacao = DateTime.now()
      await operacao.save()
      if (operacao.$isPersisted) {
        return {
          sucesso: true,
        }
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao alternar estado ativo do evento de vendedor',
        error: error.message,
      }
    }
  }

}
