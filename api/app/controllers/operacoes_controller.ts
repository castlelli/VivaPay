import OperacaoEvento from '#models/OperacaoEvento'
import OperacaoVendedor from '#models/OperacaoVendedor'
import OperacaoVendedorEvento from '#models/OperacaoVendedorEvento'
import Administrador from '#models/Administrador'
import Usuario from '#models/Usuario'
import Evento from '#models/Evento'
import VendedorEvento from '#models/VendedorEvento'
import Vendedor from '#models/Vendedor'

export default class OperacoesController {
  /* public async selecionarTudo(user: any) {
     try {
       let admin = (await Administrador.query().where('id_usuario', user.id_usuario))[0]

       const operacoesVendedor = await OperacaoVendedor
         .query()
         .join('administradores as a', 'operacoes_vendedores.responsavel', 'a.id_administrador')
         .where('a.id_instituicao', admin.id_instituicao)
         .select('operacoes_vendedores.*')



       const operacoesVendedorEvento = await OperacaoVendedor
         .query()
         .join('administradores as a', 'operacoes_vendedor_eventos.responsavel', 'a.id_administrador')
         .where('a.id_instituicao', admin.id_instituicao)
         .select('operacoes_vendedor_eventos.*')

       return {
         sucesso: true,
         operacoesEvento: operacoesEvento,
         operacoesVendedor: operacoesVendedor,
         operacoesVendedorEvento: operacoesVendedorEvento,
       }
     } catch (error) {
       return {
         sucesso: false,
         mensagem: 'Erro ao selecionar todas as operações',
         error: error.message,
       }
     }
   }*/

  public async selecionarTudoPorVendedor({ auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.$preloaded.vendedor[0].responsavel))[0]
      const operacoesVendedor = await OperacaoVendedor
        .query()
        .join('administradores as a', 'operacoes_vendedores.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .select('operacoes_vendedores.*')

      return {
        sucesso: true,
        operacoes: operacoesVendedor
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todas as operações de vendedores',
        error: error.message,
      }
    }
  }

  public async selecionarTudoVendedor({ auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      const operacoesVendedor = await OperacaoVendedor
        .query()
        .join('administradores as a', 'operacoes_vendedores.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .select('operacoes_vendedores.*')

      return {
        sucesso: true,
        operacoes: operacoesVendedor
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todas as operações de vendedores',
        error: error.message,
      }
    }
  }


  public async selecionarTudoVendedorEvento({ auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      const operacoesVendedorEvento = await OperacaoVendedorEvento
        .query()
        .join('administradores as a', 'operacoes_vendedor_eventos.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .select('operacoes_vendedor_eventos.*')

      return {
        sucesso: true,
        operacoes: operacoesVendedorEvento,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todas as operações de vendedores em eventos',
        error: error.message,
      }
    }
  }

  public async selecionarTudoEvento({ auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      const operacoesEvento = await OperacaoEvento
        .query()
        .join('administradores as a', 'operacoes_eventos.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .select('operacoes_eventos.*')

      return {
        sucesso: true,
        operacoes: operacoesEvento,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todas as operações de eventos',
        error: error.message,
      }
    }
  }


  //Métodos de seleção com quantidade

  public async selecionarTudoVendedorPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      const operacoesVendedor = await OperacaoVendedor
        .query()
        .join('administradores as a', 'operacoes_vendedores.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .select('operacoes_vendedores.*')
        .orderBy('operacoes_vendedores.data_operacao', 'desc')
        .limit(10)
        .offset((params.quantidade - 1) * 10)

      const usuario = await Usuario.query()
        .whereIn('id_usuario', operacoesVendedor.map(op => op.responsavel))
        .select('id_usuario', 'nome_usuario');

      const vendedor = await Vendedor.query()
        .whereIn('id_vendedor', operacoesVendedor.map(ve => ve.id_vendedor))
        .select('id_vendedor', 'loja');

      return {
        sucesso: true,
        operacoes: operacoesVendedor,
        vendedor: vendedor,
        usuario: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todas as operações de vendedores por quantidade',
        error: error.message,
      }
    }
  }


  public async selecionarTudoVendedorEventoPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      const operacoesVendedorEvento = await OperacaoVendedorEvento.query()
        .join('administradores as a', 'operacoes_vendedor_eventos.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .orderBy('operacoes_vendedor_eventos.data_operacao', 'desc')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('operacoes_vendedor_eventos.*')

      const vendedorEvento = await VendedorEvento.query()
        .whereIn('id_vendedor_evento', operacoesVendedorEvento.map(op => op.id_vendedor_evento))
        .select('id_vendedor_evento', 'id_vendedor');

      const vendedor = await Vendedor.query()
        .whereIn('id_vendedor', vendedorEvento.map(ve => ve.id_vendedor))
        .select('id_vendedor', 'loja');

      const usuario = await Usuario.query()
        .whereIn('id_usuario', operacoesVendedorEvento.map(op => op.responsavel))
        .select('id_usuario', 'nome_usuario');

      return {
        sucesso: true,
        operacoes: operacoesVendedorEvento,
        vendedorEvento: vendedorEvento,
        vendedor: vendedor,
        usuario: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todas as operações de vendedores em eventos por quantidade',
        error: error.message,
      }
    }
  }

  public async selecionarTudoEventoPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      const operacoesEvento = await OperacaoEvento
        .query()
        .join('administradores as a', 'operacoes_eventos.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .select('operacoes_eventos.*')
        .orderBy('operacoes_eventos.data_operacao', 'desc')
        .limit(10)
        .offset((params.quantidade - 1) * 10)

      const usuario = await Usuario.query()
        .whereIn('id_usuario', operacoesEvento.map(op => op.responsavel))
        .select('id_usuario', 'nome_usuario');

      const evento = await Evento.query()
        .whereIn('id_evento', operacoesEvento.map(op => op.id_evento))
        .select('id_evento', 'nome_evento');

      return {
        sucesso: true,
        operacoes: operacoesEvento,
        usuario: usuario,
        evento: evento
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todas as operações de eventos por quantidade',
        error: error.message,
      }
    }
  }


  public async selecionarPorNomeEvento({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]

      const evento = await Evento.query()
        .where('nome_evento', params.nomeEvento)
        .select('id_evento', 'nome_evento');


      const operacoesEvento = await OperacaoEvento.query()
        .from('operacoes_eventos as oe')
        .join('administradores as a', 'a.id_administrador', 'oe.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .whereIn('oe.id_evento', evento.map(e => e.id_evento))
        .orderBy('oe.id_operacao_evento')
        .select('oe.*')

      return {
        sucesso: true,
        operacoes: operacoesEvento,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar as operações de eventos por nome',
        error: error.message,
      }
    }
  }

  public async selecionarPorNomeVendedor({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]

      const vendedor = await Vendedor.query()
        .where('loja', params.nomeVendedor)
        .select('id_vendedor', 'loja');

      const operacoesVendedor = await OperacaoVendedor.query()
        .from('operacoes_vendedores as ov')
        .join('administradores as a', 'a.id_administrador', 'ov.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .whereIn('ov.id_vendedor', vendedor.map(v => v.id_vendedor))
        .orderBy('ov.id_operacao_vendedor')
        .select('ov.*')

      return {
        sucesso: true,
        operacoes: operacoesVendedor
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar as operações de vendedores por nome',
        error: error.message,
      }
    }
  }

  public async selecionarPorNomeVendedorEvento({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]

      const vendedor = await Vendedor.query()
        .where('loja', params.nomeVendedor)
        .select('id_vendedor', 'loja');

      const vendedorevento = await VendedorEvento.query()
        .whereIn('id_vendedor', vendedor.map(v => v.id_vendedor))
        .select('id_vendedor_evento', 'id_vendedor', 'id_evento');

      const operacoesVendedorEvento = await OperacaoVendedorEvento.query()
        .from('operacoes_vendedor_eventos as ove')
        .join('administradores as a', 'a.id_administrador', 'ove.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .whereIn('ove.id_vendedor_evento', vendedorevento.map(v => v.id_vendedor_evento))
        .orderBy('ove.id_operacao_vendedor_evento')
        .select('ove.*')

      const usuario = await Usuario.query()
        .whereIn('id_usuario', operacoesVendedorEvento.map(ove => ove.responsavel))
        .select('id_usuario', 'nome_usuario');


      return {
        sucesso: true,
        operacoes: operacoesVendedorEvento,
        vendedor: vendedor,
        vendedorEvento: vendedorevento,
        usuario: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar as operações de vendedores em eventos por nome',
        error: error.message,
      }
    }
  }

  public async selecionarPorNomeEventoPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]

      const evento = await Evento.query()
        .where('nome_evento', params.nomeEvento)
        .select('id_evento', 'nome_evento');


      const operacoesEvento = await OperacaoEvento.query()
        .from('operacoes_eventos as oe')
        .join('administradores as a', 'a.id_administrador', 'oe.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .whereIn('oe.id_evento', evento.map(e => e.id_evento))
        .orderBy('oe.id_operacao_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('oe.*')

      const usuario = await Usuario.query()
        .whereIn('id_usuario', operacoesEvento.map(oe => oe.responsavel))
        .select('id_usuario', 'nome_usuario');

      return {
        sucesso: true,
        operacoes: operacoesEvento,
        evento: evento,
        usuario: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar as operações de eventos por nome',
        error: error.message,
      }
    }
  }

  public async selecionarPorNomeVendedorPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]

      const vendedor = await Vendedor.query()
        .where('loja', params.nomeVendedor)
        .select('id_vendedor', 'loja');

      const operacoesVendedor = await OperacaoVendedor.query()
        .from('operacoes_vendedores as ov')
        .join('administradores as a', 'a.id_administrador', 'ov.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .whereIn('ov.id_vendedor', vendedor.map(v => v.id_vendedor))
        .orderBy('ov.id_operacao_vendedor')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('ov.*')

      const usuario = await Usuario.query()
        .whereIn('id_usuario', operacoesVendedor.map(ov => ov.responsavel))
        .select('id_usuario', 'nome_usuario');

      return {
        sucesso: true,
        operacoes: operacoesVendedor,
        vendedor: vendedor,
        usuario: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar as operações de vendedores por nome',
        error: error.message,
      }
    }
  }

  public async selecionarPorNomeVendedorEventoPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]

      const vendedor = await Vendedor.query()
        .where('loja', params.nomeVendedor)
        .select('id_vendedor', 'loja');

      const vendedorevento = await VendedorEvento.query()
        .whereIn('id_vendedor', vendedor.map(v => v.id_vendedor))
        .select('id_vendedor_evento', 'id_vendedor', 'id_evento');

      const operacoesVendedorEvento = await OperacaoVendedorEvento.query()
        .from('operacoes_vendedor_eventos as ove')
        .join('administradores as a', 'a.id_administrador', 'ove.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .whereIn('ove.id_vendedor_evento', vendedorevento.map(v => v.id_vendedor_evento))
        .orderBy('ove.id_operacao_vendedor_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('ove.*')

      const usuario = await Usuario.query()
        .whereIn('id_usuario', operacoesVendedorEvento.map(ove => ove.responsavel))
        .select('id_usuario', 'nome_usuario');


      return {
        sucesso: true,
        operacoes: operacoesVendedorEvento,
        vendedor: vendedor,
        vendedorEvento: vendedorevento,
        usuario: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar as operações de vendedores em eventos por nome',
        error: error.message,
      }
    }
  }

  /*public async selecionarPorVendedor({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTudoVendedor(auth)
      let operacoes = resultado.operacoes

      if (!operacoes) {
        throw new Error('Não foi possível recuperar as operações de vendedores.')
      }

      const operacao = operacoes.filter((op: { id_vendedor: any }) => op.id_vendedor == params.id_vendedor)
      return {
        sucesso: true,
        operacao: operacao,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar operação de vendedor por ID',
        error: error.message,
      }
    }
  }

  public async selecionarPorVendedorEvento({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTudoVendedorEvento(auth)
      let operacoes = resultado.operacoes

      if (!operacoes) {
        throw new Error('Não foi possível recuperar as operações de vendedores em eventos.')
      }

      const operacao = operacoes.filter((op: { id_vendedor_evento: any }) => op.id_vendedor_evento == params.id_vendedor_evento)
      return {
        sucesso: true,
        operacao: operacao,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar operação de vendedor em evento por ID',
        error: error.message,
      }
    }
  }

  public async selecionarPorEvento({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTudoEvento(auth)
      let operacoes = resultado.operacoes

      if (!operacoes) {
        throw new Error('Não foi possível recuperar as operações de eventos.')
      }

      const operacao = operacoes.filter((op: { id_evento: any }) => op.id_evento == params.id_evento)
      return {
        sucesso: true,
        operacao: operacao,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar operação de evento por ID',
        error: error.message,
      }
    }
  }

  public async selecionarVendedorEventoPorADM({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTudoVendedorEvento(auth)
      let operacoes = resultado.operacoes

      if (!operacoes) {
        throw new Error('Não foi possível recuperar as operações de vendedores em eventos.')
      }

      const operacoesFiltradas = operacoes.filter((op: { responsavel: any }) => op.responsavel == params.id_adm)
      return {
        sucesso: true,
        operacoes: operacoesFiltradas,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar operações de vendedor em evento por ADM',
        error: error.message,
      }
    }
  }

  public async selecionarEventoPorADM({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTudoEvento(auth)
      let operacoes = resultado.operacoes

      if (!operacoes) {
        throw new Error('Não foi possível recuperar as operações de eventos.')
      }

      const operacoesFiltradas = operacoes.filter((op: { responsavel: any }) => op.responsavel == params.id_adm)
      return {
        sucesso: true,
        operacoes: operacoesFiltradas,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar operações de evento por ADM',
        error: error.message,
      }
    }
  }

  public async selecionarVendedorPorADM({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTudoVendedor(auth)
      let operacoes = resultado.operacoes

      if (!operacoes) {
        throw new Error('Não foi possível recuperar as operações de vendedores.')
      }

      const operacoesFiltradas = operacoes.filter((op: { responsavel: any }) => op.responsavel == params.id_adm)
      return {
        sucesso: true,
        operacoes: operacoesFiltradas,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar operações de vendedor por ADM',
        error: error.message,
      }
    }
  }*/

  public async operacaoVendedor(id_vendedor: number, id_adm: number, estadoAtual: boolean) {
    let tipo = estadoAtual ? 'A' : 'D'
    let operacao = new OperacaoVendedor()
    operacao.tipo = tipo
    operacao.id_vendedor = id_vendedor
    operacao.responsavel = id_adm
    await operacao.save()
  }

  public async operacaoEvento(id_evento: number, id_adm: number, estadoAtual: boolean) {
    let tipo = estadoAtual ? 'A' : 'D'
    let operacao = new OperacaoEvento()
    operacao.tipo = tipo
    operacao.id_evento = id_evento
    operacao.responsavel = id_adm
    await operacao.save()
  }

  public async operacaoVendedorEvento(id_vendedor_evento: number, id_adm: number, estadoAtual: boolean) {
    let tipo = estadoAtual ? 'A' : 'D'
    let operacao = new OperacaoVendedorEvento()
    operacao.tipo = tipo
    operacao.id_vendedor_evento = id_vendedor_evento
    operacao.responsavel = id_adm
    await operacao.save()
  }
}
