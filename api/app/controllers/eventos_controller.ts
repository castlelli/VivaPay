import Evento from '#models/Evento'
import Administrador from '#models/Administrador'
import OperacaoEvento from '#models/OperacaoEvento'
import { DateTime } from 'luxon'
import Usuario from '#models/Usuario'

export default class EventosController {
  public async selecionarTodos({ auth }: any) {
    try {
      const eventos = (await this.selecionarTodosInstituicao(auth.user)).eventos

      const usuario = await Usuario.query()
        .whereIn('id_usuario', eventos.map(e => e.responsavel))
        .select('id_usuario', 'nome_usuario');

      return {
        sucesso: true,
        eventos: eventos,
        usuario: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todos os eventos',
        error: error.message,
      }
    }
  }


  public async selecionarPorAtivo({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const eventos = await Evento.query()
        .from('eventos as e')
        .select('e.*')
        .join('administradores as a', 'e.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('ativo', params.estado)

      const usuario = await Usuario.query()
        .whereIn('id_usuario', eventos.map(e => e.responsavel))
        .select('id_usuario', 'nome_usuario');

      return {
        sucesso: true,
        eventos: eventos,
        usuario: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todos os eventos',
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
      const eventos = await Evento.query()
        .from('eventos as e')
        .select('e.*')
        .join('administradores as a', 'e.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
      return {
        sucesso: true,
        eventos: eventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todos os eventos',
        error: error.message,
      }
    }
  }

  public async selecionarPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const eventos = await Evento.query()
        .from('eventos as e')
        .select('e.*')
        .join('administradores as a', 'e.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .orderBy('e.id_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10)

      const usuario = await Usuario.query()
        .whereIn('id_usuario', eventos.map(e => e.responsavel))
        .select('id_usuario', 'nome_usuario');

      return {
        sucesso: true,
        eventos: eventos,
        usuario: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar eventos por quantidade',
        error: error.message,
      }
    }
  }

  public async selecionarPorId({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.eventos

      if (!todos) {
        throw new Error('Não foi possível recuperar os eventos.')
      }

      const evento = todos.filter((evento) => evento.id_evento == params.id_evento)
      return {
        sucesso: true,
        evento: evento,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar evento por ID',
        error: error.message,
      }
    }
  }

  public async selecionarPorNome({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.eventos

      if (!todos) {
        throw new Error('Não foi possível recuperar os eventos.')
      }

      const eventos = todos.filter((evento) => evento.nome_evento.includes(params.nomeEvento))
      return {
        sucesso: true,
        eventos: eventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar evento por nome',
        error: error.message,
      }
    }
  }

  public async selecionarPorCriador({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.eventos

      if (!todos) {
        throw new Error('Não foi possível recuperar os eventos.')
      }

      const eventos = todos.filter((evento) => evento.responsavel == params.responsavel)
      return {
        sucesso: true,
        eventos: eventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar evento por criador',
        error: error.message,
      }
    }
  }

  /* public async selecionarPorDataCriacao({ request, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.eventos
      let dataInicial = new Date(request.body().dataInicial)
      let dataFinal = new Date(request.body().dataFinal)

      if (!todos) {
        throw new Error('Não foi possível recuperar os eventos.')
      }

      const eventos = todos.filter(
        (evento) =>
          (isEqual(evento.criado_em.toJSDate(), dataInicial) ||
            isAfter(evento.criado_em.toJSDate(), dataInicial)) &&
          (isEqual(evento.criado_em.toJSDate(), dataFinal) ||
            isBefore(evento.criado_em.toJSDate(), dataFinal))
      )
      return {
        sucesso: true,
        eventos: eventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar evento por data de criação',
        error: error.message,
      }
    }
  }*/

  public async selecionarPorInicioeFim({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.eventos

      if (!todos) {
        throw new Error('Não foi possível recuperar os eventos.')
      }

      const eventos = todos.filter(
        (evento) =>
          evento.hora_inicio >= params.dataInicio &&
          evento.hora_final <= params.dataFinal
      )
      return {
        sucesso: true,
        eventos: eventos,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar evento por data de realização',
        error: error.message,
      }
    }
  }

  /* public async selecionarPorAtivo({ params, auth }: any) {
        try {
            let resultado = await this.selecionarTodosInstituicao(auth.user);
            let todos = resultado.eventos;
    
            if (!todos) {
                throw new Error('Não foi possível recuperar os eventos.');
            }
    
            const eventos = todos.filter(evento => evento.ativo == params.estado);
            return {
                sucesso: true,
                eventos: eventos
            };
        } catch (error) {
            return {
                sucesso: false,
                mensagem: 'Erro ao selecionar evento por estado ativo',
                error: error.message
            };
        }
    } */

  //Métodos com quantidade para preencher tabelas

  public async selecionarTodosPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0];
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        };
      }
      const eventos = await Evento.query()
        .from('eventos as e')
        .select('e.*')
        .join('administradores as a', 'e.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .orderBy('e.id_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10);

      return {
        sucesso: true,
        eventos: eventos,
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todos os eventos por quantidade',
        error: error.message,
      };
    }
  }

  public async selecionarPorResponsavelPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0];
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        };
      }
      const eventos = await Evento.query()
        .from('eventos as e')
        .select('e.*')
        .join('administradores as a', 'e.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('e.responsavel', params.responsavel)
        .orderBy('e.id_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10);

      return {
        sucesso: true,
        eventos: eventos,
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar eventos por responsável e quantidade',
        error: error.message,
      };
    }
  }

  public async selecionarPorNomePorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0];
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        };
      }
      const eventos = await Evento.query()
        .from('eventos as e')
        .select('e.*')
        .join('administradores as a', 'e.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('e.nome_evento', 'like', `%${params.nome}%`)
        .orderBy('e.id_evento')
        .limit(10)
        .offset((params.quantidade - 1) * 10);

      const usuario = await Usuario.query()
        .whereIn('id_usuario', eventos.map(e => e.responsavel))
        .select('id_usuario', 'nome_usuario');

      return {
        sucesso: true,
        eventos: eventos,
        usuario: usuario
      };
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar eventos por nome e quantidade',
        error: error.message,
      };
    }
  }

  public async alternarAtivo({ params, auth }: any) {
    try {
      const evento = await Evento.findOrFail(params.id_evento)
      evento.ativo = !evento.ativo
      evento.save()
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      let operacao = new OperacaoEvento()
      operacao.tipo = evento.ativo ? "A" : "D"
      operacao.id_evento = evento.id_evento
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
        mensagem: 'Erro ao alternar estado ativo do evento',
        error: error.message,
      }
    }
  }

  public async alterarNome({ params, request }: any) {
    try {
      await Evento.query()
        .where('id_evento', params.id_evento)
        .update({ nome_evento: request.body().nomeEvento })
      return {
        sucesso: true,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao alterar nome do evento',
        error: error.message,
      }
    }
  }

  public async alterarDesc({ params, request }: any) {
    try {
      await Evento.query()
        .where('id_evento', params.id_evento)
        .update({ nome_evento: request.body().descEvento })
      return {
        sucesso: true,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao alterar descrição do evento',
        error: error.message,
      }
    }
  }

  public async alterarAbrev({ params, request }: any) {
    try {
      await Evento.query()
        .where('id_evento', params.id_evento)
        .update({ nome_evento: request.body().abrevEvento })
      return {
        sucesso: true,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao alterar abreviação do evento',
        error: error.message,
      }
    }
  }

  public async alterarInicioeFim({ params, request }: any) {
    try {
      await Evento.query().where('id_evento', params.id_evento).update({
        hora_inicio: request.body().horaInicio,
        hora_final: request.body().horaFinal,
      })
      return {
        sucesso: true,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao alterar horários de início e fim do evento',
        error: error.message,
      }
    }
  }
  public async cadastrar({ auth, request }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]

      const evento = new Evento()
      evento.nome_evento = request.body().nomeEvento
      evento.descricao_evento = request.body().descricaoEvento
      evento.abreviacao_evento = request.body().abreviacaoEvento
      evento.hora_inicio = request.body().horaInicio
      evento.hora_final = request.body().horaFinal
      evento.responsavel = admin.id_administrador
      evento.id_instituicao = admin.id_instituicao
      await evento.save()

      if (evento.$isPersisted) {
        return {
          sucesso: true,
          mensagem: 'Evento cadastrado com sucesso!',
        }
      } else {
        return {
          sucesso: false,
          mensagem: 'Erro ao cadastrar o evento.',
        }
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro no cadastro do evento.',
      }
    }
  }
}
