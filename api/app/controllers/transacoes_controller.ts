import ClienteEvento from '#models/ClienteEvento'
import Vendedor from '#models/Vendedor'
import VendedorEvento from '#models/VendedorEvento'
import Transacao from '#models/Transacao'
import Administrador from '#models/Administrador'
import ClienteEventosController from './cliente_eventos_controller.js'
import { DateTime } from 'luxon';

export default class TransacaoController {
  public async deposito({ request }: any) {
    try {
      let objclienteEvento
      if (request.body().valor <= 0) {
        return {
          sucesso: false,
          mensagem: "Valor Inválido."
        }
      }
      if (request.body().voucher) {
        objclienteEvento = (await ClienteEvento.query()
          .whereHas('cliente', q => {
            q.where('voucher', request.body().voucher)
          })
          .whereHas('evento', q => {
            q.andWhere('ativo', true)
          }))[0]

        if (!objclienteEvento) {
          return {
            sucesso: false,
            mensagem: "Cliente não encontrado ou evento não está ativo."
          }
        }
      } else {
        return {
          sucesso: false,
          mensagem: "Voucher não fornecido."
        }
      }

      //deposito nao deve usar vendedor, por isso ta comentado
      /*// Procura no BD um vendedor que corresponda ao usuário autenticado
      vendedor = (await Vendedor.query().where('id_usuario', auth.user.id_usuario))[0]
      if (!vendedor) {
        return {
          sucesso: false,
          mensagem: "Vendedor não encontrado."
        }
      }

      /// Verifica se o vendedor está associado a um evento ativo
      let objvendedorEvento = (await VendedorEvento.query()
        .whereHas('vendedor', v => {
          v.where('id_vendedor', vendedor.id_vendedor)
        })
        .whereHas('evento', e => {
          e.andWhere('ativo', true)
        }))[0]

      if (!objvendedorEvento) {
        return {
          sucesso: false,
          mensagem: "Vendedor não está associado a um evento ativo."
        }
      }*/

      // Cria uma nova transação
      let transacao = new Transacao()
      //transacao.id_vendedor_evento = objvendedorEvento.id_vendedor_evento
      transacao.id_cliente_evento = objclienteEvento.id_cliente_evento
      transacao.valor = request.body().valor
      transacao.metodo = request.body().metodo
      transacao.tipo = "C" // Crédito, ou seja, depósito

      await transacao.save()

      const valor = transacao.valor.toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL',
      });

      return {
        sucesso: true,
        mensagem: "Transação realizada com sucesso!\nTotal depositado: " + valor,
        transacao: transacao
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao realizar depósito",
        error: error.message,
      }
    }
  }

  public async pagamento({ request, auth }: any) {
    try {
      if (request.body().valor <= 0) {
        return {
          sucesso: false,
          mensagem: "Valor Inválido."
        }
      }
      if (!request.body().voucher) {
        return {
          sucesso: false,
          mensagem: "Erro na busca de cliente"
        }
      }


      let objclienteEvento = (await ClienteEvento.query().whereHas('cliente', q => {
        q.where('voucher', request.body().voucher)
      }).whereHas('evento', q => {
        q.andWhere('ativo', true)
      }))[0]

      if (!objclienteEvento) {
        return {
          sucesso: false,
          mensagem: "Cliente não encontrado ou evento não está ativo."
        }
      }

      let vendedor = (await Vendedor.query().where('id_usuario', auth.user.id_usuario))[0]

      let objvendedorEvento = (await VendedorEvento.query().whereHas('vendedor', v => {
        v.where('id_vendedor', vendedor.id_vendedor)
      }).whereHas('evento', e => {
        e.andWhere('ativo', true)
      }))[0]

      if (!objvendedorEvento) {
        return {
          sucesso: false,
          mensagem: "Vendedor não encontrado ou evento não está ativo."
        }
      }

      await objclienteEvento.load('transacao')

      if (objclienteEvento.saldo < request.body().valor) {
        return {
          sucesso: false,
          mensagem: "Saldo insuficiente"
        }
      }

      let transacao = new Transacao()
      transacao.id_vendedor_evento = objvendedorEvento.id_vendedor_evento
      transacao.id_cliente_evento = objclienteEvento.id_cliente_evento
      transacao.valor = request.body().valor
      transacao.tipo = "D" // Débito, ou seja, pagamento.
      transacao.metodo = "V"

      await transacao.save()

      if (transacao.$isPersisted) {
        return {
          sucesso: true,
          mensagem: "Total pago " + request.body().valor,
          transacao: transacao
        }
      }

      return {
        sucesso: false,
        mensagem: "Erro no pagamento"
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao realizar pagamento",
        error: error.message,
      }
    }
  }

  public async estorno({ request }: any) {
    try {
      if (request.body().valor <= 0) {
        return {
          sucesso: false,
          mensagem: "Valor Inválido."
        }
      }
      let objclienteEvento = (await ClienteEvento.query().whereHas('cliente', q => {
        q.where('voucher', request.body().voucher)
      }).whereHas('evento', q => {
        q.andWhere('ativo', true)
      }))[0]

      if (!objclienteEvento) {
        return {
          sucesso: false,
          mensagem: "Cliente não encontrado ou evento não está ativo."
        }
      }

      //let vendedor = (await Vendedor.query().where('id_usuario', auth.user.id_usuario))[0]

      //let vendedorEvento = await VendedorEvento.findByOrFail('id_vendedor', vendedor.id_vendedor)

      await objclienteEvento.load('transacao')

      if (objclienteEvento.saldo == 0) {
        return {
          sucesso: false,
          mensagem: "Sem saldo no voucher"
        }
      }
      let transacao = new Transacao()
      //transacao.id_vendedor_evento = vendedorEvento.id_vendedor_evento
      transacao.id_cliente_evento = objclienteEvento.id_cliente_evento
      transacao.valor = objclienteEvento.saldo
      transacao.metodo = "E" // E -> Estorno
      transacao.tipo = "E" // E -> Estorno

      const saldo = objclienteEvento.saldo.toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL',
      });

      await transacao.save()

      if (transacao.$isPersisted) {
        return {
          sucesso: true,
          mensagem: "Total retornado " + saldo,
          transacao: transacao
        }
      }

      return {
        sucesso: false,
        mensagem: "Erro no retorno"
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao realizar estorno",
        error: error.message,
      }
    }
  }

  public async selecionarPorTipo({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.id_usuario))[0]

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('cliente_eventos as ce', 'ce.id_cliente_evento', 't.id_cliente_evento')
        .join('eventos as e', "e.id_evento", "ce.id_evento")
        .join('administradores as a', 'a.id_administrador', 'e.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.tipo', params.tipo)
        .select('t.*'));

      const vouchers = await ClienteEvento.query()
        .whereIn('id_cliente_evento', transacoes.map(t => t.id_cliente_evento))
        .select('id_cliente_evento', 'voucher');

      return {
        sucesso: true,
        transacoes: transacoes,
        vouchers: vouchers
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por tipo",
        error: error.message,
      }
    }
  }

  public async selecionarPorMetodo({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.id_usuario))[0]

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('cliente_eventos as ce', 'ce.id_cliente_evento', 't.id_cliente_evento')
        .join('eventos as e', "e.id_evento", "ce.id_evento")
        .join('administradores as a', 'a.id_administrador', 'e.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.metodo', params.metodo)
        .select('t.*'));

      return {
        sucesso: true,
        transacoes: transacoes
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por tipo",
        error: error.message,
      }
    }
  }


  public async selecionarPorVendedorEvento({ request, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.id_usuario))[0];

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('vendedor_eventos as ve', 've.id_vendedor_evento', 't.id_vendedor_evento')
        .join('administradores as a', 'a.id_administrador', 've.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.id_vendedor_evento', request.body().id_vendedorEvento)
        .select('t.*'));

      return {
        sucesso: true,
        transacoes: transacoes
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por vendedor evento",
        error: error.message,
      };
    }
  }

  public async selecionarPorVendedor({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_administrador", auth.user.$preloaded.vendedor[0].responsavel))[0];

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('vendedor_eventos as ve', 've.id_vendedor_evento', 't.id_vendedor_evento')
        .join('vendedores as v', 'v.id_vendedor', 've.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.id_vendedor_evento', params.id)
        .select('t.*'))

      return {
        sucesso: true,
        transacoes: transacoes
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por vendedor",
        error: error.message,
      };
    }
  }

  public async selecionarPorClienteEvento({ request, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.id_usuario))[0];

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('vendedor_eventos as ve', 've.id_vendedor_evento', 't.id_vendedor_evento')
        .join('administradores as a', 'a.id_administrador', 've.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.id_cliente_evento', request.body().id_clienteEvento)
        .select('t.*'));

      return {
        sucesso: true,
        transacoes: transacoes
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por cliente evento",
        error: error.message,
      };
    }
  }

  public async selecionarPorCliente({ request, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.id_usuario))[0];

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('vendedor_eventos as ve', 've.id_vendedor_evento', 't.id_vendedor_evento')
        .join('administradores as a', 'a.id_administrador', 've.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.id_cliente', request.body().id_cliente)
        .select('t.*'));

      return {
        sucesso: true,
        transacoes: transacoes
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por cliente",
        error: error.message,
      };
    }
  }

  //Métodos com limite para preenchimento de tabelas

  public async selecionarPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.id_usuario))[0]

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('cliente_eventos as ce', 'ce.id_cliente_evento', 't.id_cliente_evento')
        .join('eventos as e', "e.id_evento", "ce.id_evento")
        .join('administradores as a', 'a.id_administrador', 'e.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .orderBy('t.criado_em', 'desc')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('t.*'));

      return {
        sucesso: true,
        transacoes: transacoes
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por quantidade",
        error: error.message,
      }
    }
  }

  public async selecionarPorTipoPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.id_usuario))[0];

      const transacoes = await Transacao.query()
        .from('transacoes as t')
        .join('cliente_eventos as ce', 'ce.id_cliente_evento', 't.id_cliente_evento')
        .join('eventos as e', "e.id_evento", "ce.id_evento")
        .join('administradores as a', 'a.id_administrador', 'e.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.tipo', params.tipo)
        .orderBy('t.criado_em', 'desc')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('t.*');

      const vouchers = await ClienteEvento.query()
        .whereIn('id_cliente_evento', transacoes.map(t => t.id_cliente_evento))
        .select('id_cliente_evento', 'voucher');

      return {
        sucesso: true,
        transacoes: transacoes,
        vouchers: vouchers
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por tipo",
        error: error.message,
      };
    }
  }



  public async selecionarPorVendedorEventoPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.$preloaded.vendedor[0].responsavel))[0];
      const transacoes = await Transacao.query()
        .from('transacoes as t')
        .join('vendedor_eventos as ve', 've.id_vendedor_evento', 't.id_vendedor_evento')
        .join('administradores as a', 'a.id_administrador', 've.responsavel')
        .join('cliente_eventos as ce', 'ce.id_cliente_evento', 't.id_cliente_evento')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.id_vendedor_evento', params.id_vendedorEvento)
        .orderBy('t.criado_em', 'desc')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('t.*', 'ce.voucher'); // Aqui selecionamos o voucher


      const transacoesComVoucher = transacoes.map(transacao => {
        return {
          ...transacao.$attributes,
          $extras: {
            voucher: transacao.$extras.voucher
          }
        };
      });

      return {
        sucesso: true,
        transacoes: transacoesComVoucher,
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por vendedor evento",
        error: error.message,
      };
    }
  }

  public async selecionarPorVendedorPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_administrador", auth.user.$preloaded.vendedor[0].responsavel))[0];

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('vendedor_eventos as ve', 've.id_vendedor_evento', 't.id_vendedor_evento')
        .join('vendedores as v', 'v.id_vendedor', 've.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.id_vendedor_evento', params.id)
        .orderBy('t.criado_em', 'desc')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('t.*'));

      return {
        sucesso: true,
        transacoes: transacoes
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por vendedor",
        error: error.message,
      };
    }
  }

  public async selecionarPorClienteEventoPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.id_usuario))[0];

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('vendedor_eventos as ve', 've.id_vendedor_evento', 't.id_vendedor_evento')
        .join('administradores as a', 'a.id_administrador', 've.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.id_cliente_evento', params.id_clienteEvento)
        .orderBy('t.criado_em', 'desc')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('t.*'));

      return {
        sucesso: true,
        transacoes: transacoes
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por cliente evento",
        error: error.message,
      };
    }
  }

  public async selecionarPorVoucherPorTipo({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.id_usuario))[0];

      const vouchers = await ClienteEvento.query()
        .where('voucher', params.voucher)
        .select('id_cliente_evento', 'voucher');

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('cliente_eventos as ce', 'ce.id_cliente_evento', 't.id_cliente_evento')
        .join('eventos as e', "e.id_evento", "ce.id_evento")
        .join('administradores as a', 'a.id_administrador', 'e.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.tipo', params.tipo)
        .whereIn('t.id_cliente_evento', vouchers.map(v => v.id_cliente_evento))
        .orderBy('t.criado_em', 'desc')
        .select('t.*'));

      return {
        sucesso: true,
        transacoes: transacoes,
        vouchers: vouchers
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por voucher",
        error: error.message,
      };
    }
  }

  public async selecionarPorVoucherPorTipoPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.id_usuario))[0];

      const vouchers = await ClienteEvento.query()
        .where('voucher', params.voucher)
        .select('id_cliente_evento', 'voucher');

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('cliente_eventos as ce', 'ce.id_cliente_evento', 't.id_cliente_evento')
        .join('eventos as e', "e.id_evento", "ce.id_evento")
        .join('administradores as a', 'a.id_administrador', 'e.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.tipo', params.tipo)
        .whereIn('t.id_cliente_evento', vouchers.map(v => v.id_cliente_evento))
        .orderBy('t.criado_em', 'desc')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('t.*'));

      return {
        sucesso: true,
        transacoes: transacoes,
        vouchers: vouchers
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por voucher",
        error: error.message,
      };
    }
  }

  public async selecionarPorClientePorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.id_usuario))[0];

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('vendedor_eventos as ve', 've.id_vendedor_evento', 't.id_vendedor_evento')
        .join('administradores as a', 'a.id_administrador', 've.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.id_cliente', params.id_cliente)
        .orderBy('t.criado_em', 'desc')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('t.*'));

      return {
        sucesso: true,
        transacoes: transacoes
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por cliente",
        error: error.message,
      };
    }
  }

  //Métodos para uso especifico no aplicativo (preload de vendedor)

  public async selecionarPorQuantidadeVendedor({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.$preloaded.vendedor[0].responsavel))[0];

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('cliente_eventos as ce', 'ce.id_cliente_evento', 't.id_cliente_evento')
        .join('eventos as e', "e.id_evento", "ce.id_evento")
        .join('administradores as a', 'a.id_administrador', 'e.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .orderBy('t.criado_em', 'desc')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('t.*'));

      return {
        sucesso: true,
        transacoes: transacoes
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por quantidade",
        error: error.message,
      };
    }
  }

  public async selecionarPorTipoPorQuantidadeVendedor({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.$preloaded.vendedor[0].responsavel))[0];

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('cliente_eventos as ce', 'ce.id_cliente_evento', 't.id_cliente_evento')
        .join('eventos as e', "e.id_evento", "ce.id_evento")
        .join('administradores as a', 'a.id_administrador', 'e.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.tipo', params.tipo)
        .orderBy('t.criado_em', 'desc')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('t.*'));

      return {
        sucesso: true,
        transacoes: transacoes
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por tipo",
        error: error.message,
      };
    }
  }

  public async selecionarPorVendedorEventoPorQuantidadeVendedor({ params, auth, response }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.$preloaded.vendedor[0].responsavel))[0];
      const transacoes = await Transacao.query()
        .from('transacoes as t')
        .join('vendedor_eventos as ve', 've.id_vendedor_evento', 't.id_vendedor_evento')
        .join('administradores as a', 'a.id_administrador', 've.responsavel')
        .join('cliente_eventos as ce', 'ce.id_cliente_evento', 't.id_cliente_evento') // Join com cliente_eventos
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.id_vendedor_evento', params.idVendedorEvento)
        .orderBy('t.criado_em', 'desc')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('t.*', 'ce.voucher');
      const formattedTransacoes = transacoes.map(transacao => {
        const transacaoJson = transacao.toJSON()
        transacaoJson.voucher = transacao.$extras.voucher
        return transacaoJson
      })

      return {
        sucesso: true,
        transacoes: formattedTransacoes
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por vendedor evento",
        error: error.message,
      };
    }
  }

  public async selecionarPorVendedorPorQuantidadeVendedor({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_administrador", auth.user.$preloaded.vendedor[0].responsavel))[0];

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('vendedor_eventos as ve', 've.id_vendedor_evento', 't.id_vendedor_evento')
        .join('vendedores as v', 'v.id_vendedor', 've.id_vendedor')
        .join('administradores as a', 'a.id_administrador', 'v.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.id_vendedor_evento', params.id)
        .orderBy('t.criado_em', 'desc')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('t.*'));

      return {
        sucesso: true,
        transacoes: transacoes
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por vendedor",
        error: error.message,
      };
    }
  }

  public async selecionarPorClienteEventoPorQuantidadeVendedor({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.$preloaded.vendedor[0].responsavel))[0];

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('vendedor_eventos as ve', 've.id_vendedor_evento', 't.id_vendedor_evento')
        .join('administradores as a', 'a.id_administrador', 've.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.id_cliente_evento', params.id_clienteEvento)
        .orderBy('t.criado_em', 'desc')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('t.*'));

      return {
        sucesso: true,
        transacoes: transacoes
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por cliente evento",
        error: error.message,
      };
    }
  }

  public async selecionarPorClientePorQuantidadeVendedor({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.$preloaded.vendedor[0].responsavel))[0];

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('vendedor_eventos as ve', 've.id_vendedor_evento', 't.id_vendedor_evento')
        .join('administradores as a', 'a.id_administrador', 've.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.id_cliente', params.id_cliente)
        .orderBy('t.criado_em', 'desc')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('t.*'));

      return {
        sucesso: true,
        transacoes: transacoes
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por cliente",
        error: error.message,
      };
    }
  }

  public async selecionarTudo({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.id_usuario))[0];

      const datainicio = DateTime.fromISO(params.datainicio);
      const datafinal = DateTime.fromISO(params.datafinal);

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('cliente_eventos as ce', 'ce.id_cliente_evento', 't.id_cliente_evento')
        .join('eventos as e', "e.id_evento", "ce.id_evento")
        .join('administradores as a', 'a.id_administrador', 'e.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .orderBy('t.criado_em')
        .whereRaw('DATE(t.criado_em) BETWEEN ? AND ?', [
          datainicio.toFormat('yyyy-MM-dd'),
          datafinal.toFormat('yyyy-MM-dd')
        ])
        .select('t.*'));

      const vouchers = await ClienteEvento.query()
        .whereIn('id_cliente_evento', transacoes.map(t => t.id_cliente_evento))
        .select('id_cliente_evento', 'voucher');

      return {
        sucesso: true,
        transacoes: transacoes,
        vouchers: vouchers
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar todas as transações",
        error: error.message,
      };
    }
  }

  public async selecionarSaldoPorVoucherPorTipo({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.id_usuario))[0];

      const data = DateTime.fromISO(params.data);

      const vouchers = await ClienteEvento.query()
        .where('voucher', params.voucher)
        .select('id_cliente_evento', 'voucher', 'criado_em');

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('cliente_eventos as ce', 'ce.id_cliente_evento', 't.id_cliente_evento')
        .join('eventos as e', "e.id_evento", "ce.id_evento")
        .join('administradores as a', 'a.id_administrador', 'e.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .whereIn('t.id_cliente_evento', vouchers.map(v => v.id_cliente_evento))
        .whereRaw('DATE(t.criado_em) = ?', [data.toFormat('yyyy-MM-dd')])
        .select('t.*'));

      const transacoesDepositos = await transacoes.filter(t => t.tipo == 'C')
      let valorDepositos: number = 0;
      for (const transacao of transacoesDepositos) {
        valorDepositos += transacao.valor;
      }

      const TransacoesEstorno = await transacoes.filter(t => t.tipo == 'E')
      let valorEstorno: number = 0;
      for (const transacao of TransacoesEstorno) {
        valorEstorno += transacao.valor;
      }

      const TransacoesPagamento = await transacoes.filter(t => t.tipo == 'D')
      let valorPagamento: number = 0;
      for (const transacao of TransacoesPagamento) {
        valorPagamento += transacao.valor;
      }

      const valorDepositosString = valorDepositos.toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL',
      });

      const valorSaidaString = valorEstorno.toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL',
      });

      const valorPagamentoString = valorPagamento.toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL',
      });

      return {
        sucesso: true,
        transacoes: transacoes,
        vouchers: vouchers,
        valorDepositos: valorDepositos,
        valorEstorno: valorEstorno,
        valorPagamento: valorPagamento,
        valorDepositosString: valorDepositosString,
        valorSaidaString: valorSaidaString,
        valorPagamentoString: valorPagamentoString
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao gerar saldo do voucher",
        error: error.message,
      };
    }
  }

  public async selecionarPorTipoPorData({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where("id_usuario", auth.user.id_usuario))[0];

      const datainicio = DateTime.fromISO(params.datainicio);
      const datafinal = DateTime.fromISO(params.datafinal);

      const transacoes = (await Transacao.query()
        .from('transacoes as t')
        .join('cliente_eventos as ce', 'ce.id_cliente_evento', 't.id_cliente_evento')
        .join('eventos as e', "e.id_evento", "ce.id_evento")
        .join('administradores as a', 'a.id_administrador', 'e.responsavel')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('t.tipo', params.tipo)
        .whereRaw('DATE(t.criado_em) BETWEEN ? AND ?', [
          datainicio.toFormat('yyyy-MM-dd'),
          datafinal.toFormat('yyyy-MM-dd')
        ])
        .orderBy('t.criado_em')
        .select('t.*'));

      const vouchers = await ClienteEvento.query()
        .whereIn('id_cliente_evento', transacoes.map(t => t.id_cliente_evento))
        .select('id_cliente_evento', 'voucher');

      const TransacoesTipo = await transacoes.filter(t => t.tipo == params.tipo)
      let valorTipo: number = 0;
      for (const transacao of TransacoesTipo) {
        valorTipo += transacao.valor;
      }

      const valorTipoString = valorTipo.toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL',
      });

      return {
        sucesso: true,
        transacoes: transacoes,
        vouchers: vouchers,
        valorTipo: valorTipo,
        valorTipoString: valorTipoString
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: "Erro ao selecionar transações por tipo",
        error: error.message,
      }
    }
  }



}