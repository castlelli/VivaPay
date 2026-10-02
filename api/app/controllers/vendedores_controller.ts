import Vendedor from '#models/Vendedor'
import Administrador from '#models/Administrador'
import { isAfter, isBefore, isEqual } from 'date-fns'
import Usuario from '#models/Usuario'
import OperacaoVendedor from '#models/OperacaoVendedor'
import { DateTime } from 'luxon'

export default class VendedoresController {
  public async cadastrar({ auth, request, response }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      let usuarioVendedor = (await Usuario.query().where('login', request.body().login))[0]
      if (usuarioVendedor) {
        let jaExisteUsuario = (
          await Vendedor.query().where('id_usuario', usuarioVendedor.id_usuario)
        )[0]
        if (jaExisteUsuario) {
          return {
            sucesso: false,
            mensagem: 'Já existe um vendedor correspondente a esse usuário.',
          }
        }
      } else {
        return {
          sucesso: false,
          mensagem: 'Erro ao encontrar o usuário criado.',
        }
      }

      const vendedor = new Vendedor()
      vendedor.loja = request.body().nome
      vendedor.responsavel = admin.id_administrador
      vendedor.id_usuario = usuarioVendedor.id_usuario
      await vendedor.save()
      if (vendedor.$isPersisted) {
        return {
          sucesso: true,
          mensagem: 'Vendedor cadastrado com sucesso!',
        }
      } else {
        return {
          sucesso: false,
          mensagem: 'Erro ao cadastrar o vendedor.',
        }
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro no cadastro do vendedor: ' + error,
      }
    }
  }

  public async selecionarTodos({ auth }: any) {
    try {
      const vendedores = (await this.selecionarTodosInstituicao(auth.user)).vendedores
      const usuario = await Usuario.query()
        .whereIn('id_usuario', vendedores.map(v => v.responsavel))
        .select('id_usuario', 'nome_usuario');

      return {
        sucesso: true,
        vendedores: vendedores,
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

  public async selecionarTodosInstituicao(user: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', user.id_usuario))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const vendedores = await Vendedor.query()
        .from('vendedores as v')
        .select('v.*')
        .join('administradores as a', 'v.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
      return {
        sucesso: true,
        vendedores: vendedores
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todos os vendedores',
        error: error.message,
      }
    }
  }

  public async selecionarTodosInstituicaoPorVendedor(user: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', user.responsavel))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const vendedores = await Vendedor.query()
        .from('vendedores as v')
        .select('v.*')
        .join('administradores as a', 'v.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
      return {
        sucesso: true,
        vendedores: vendedores
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar todos os vendedores',
        error: error.message,
      }
    }
  }

  public async selecionarPorId({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.vendedores

      if (!todos) {
        throw new Error('Não foi possível recuperar os vendedores.')
      }

      const vendedor = todos.filter((vendedor) => vendedor.id_vendedor == params.id_vendedor)
      return {
        sucesso: true,
        vendedor: vendedor,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar vendedor por ID',
        error: error.message,
      }
    }
  }
  public async selecionarPorIdProprio({ auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicaoPorVendedor(auth.user.$preloaded.vendedor[0])
      let todos = resultado.vendedores

      if (!todos) {
        throw new Error('Não foi possível recuperar os vendedores.')
      }

      const vendedor = todos.filter((vendedor) => vendedor.id_vendedor == auth.user.$preloaded.vendedor[0].id_vendedor)
      return {
        sucesso: true,
        vendedor: vendedor,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar vendedor por ID',
        error: error.message,
      }
    }
  }

  public async selecionarPorNome({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.vendedores

      if (!todos) {
        throw new Error('Não foi possível recuperar os vendedores.')
      }

      const vendedores = todos.filter((vendedor) => vendedor.loja.includes(params.nomeVendedor))

      const usuario = await Usuario.query()
        .whereIn('id_usuario', vendedores.map(v => v.responsavel))
        .select('id_usuario', 'nome_usuario');

      return {
        sucesso: true,
        vendedores: vendedores,
        usuario: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar vendedor por nome da loja',
        error: error.message,
      }
    }
  }

  public async selecionarPorCriador({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.vendedores

      if (!todos) {
        throw new Error('Não foi possível recuperar os vendedores.')
      }

      const vendedores = todos.filter((vendedor) => vendedor.responsavel == params.responsavel)
      return {
        sucesso: true,
        vendedores: vendedores,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar vendedor por criador',
        error: error.message,
      }
    }
  }

  public async selecionarPorDataCriacao({ request, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.vendedores
      let dataInicial = new Date(request.body().dataInicial)
      let dataFinal = new Date(request.body().dataFinal)

      if (!todos) {
        throw new Error('Não foi possível recuperar os vendedores.')
      }

      const vendedores = todos.filter(
        (vendedor) =>
          (isEqual(vendedor.criado_em.toJSDate(), dataInicial) ||
            isAfter(vendedor.criado_em.toJSDate(), dataInicial)) &&
          (isEqual(vendedor.criado_em.toJSDate(), dataFinal) ||
            isBefore(vendedor.criado_em.toJSDate(), dataFinal))
      )
      return {
        sucesso: true,
        vendedores: vendedores,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar vendedor por data de criação',
        error: error.message,
      }
    }
  }

  public async selecionarPorAtivo({ params, auth }: any) {
    try {
      let resultado = await this.selecionarTodosInstituicao(auth.user)
      let todos = resultado.vendedores

      if (!todos) {
        throw new Error('Não foi possível recuperar os vendedores.')
      }

      const estadoAtivo =
        params.estado === 'true' ? true : params.estado === 'false' ? false : params.estado
      const vendedores = todos.filter((vendedor) => vendedor.ativo === estadoAtivo)
      return {
        sucesso: true,
        vendedores: vendedores,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar vendedor por atividade',
        error: error.message,
      }
    }
  }

  //Métodos com quantidade

  public async selecionarPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const vendedores = await Vendedor.query()
        .from('vendedores as v')
        .join('administradores as a', 'v.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .orderBy('v.id_vendedor')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('v.*');

      const usuario = await Usuario.query()
        .whereIn('id_usuario', vendedores.map(v => v.responsavel))
        .select('id_usuario', 'nome_usuario');

      return {
        sucesso: true,
        vendedores: vendedores,
        usuario: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar vendedor por nome da loja e quantidade',
        error: error.message,
      }
    }
  }

  public async selecionarPorNomePorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const vendedores = await Vendedor.query()
        .from('vendedores as v')
        .join('administradores as a', 'v.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .whereRaw('LOWER(v.loja) LIKE ?', [`%${params.nomeVendedor.toLowerCase()}%`])
        .orderBy('v.id_vendedor')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('v.*');

      const usuario = await Usuario.query()
        .whereIn('id_usuario', vendedores.map(v => v.responsavel))
        .select('id_usuario', 'nome_usuario');

      return {
        sucesso: true,
        vendedores: vendedores,
        usuario: usuario
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar vendedor por nome da loja e quantidade',
        error: error.message,
      }
    }
  }

  public async selecionarPorCriadorPorQuantidade({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: 'Administrador inválido.',
        }
      }
      const vendedores = await Vendedor.query()
        .from('vendedores as v')
        .join('administradores as a', 'v.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('v.responsavel', params.responsavel)
        .orderBy('v.id_vendedor')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('v.*');

      return {
        sucesso: true,
        vendedores: vendedores,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar vendedor por criador e quantidade',
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
      const vendedores = await Vendedor.query()
        .from('vendedores as v')
        .join('administradores as a', 'v.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .whereBetween('v.criado_em', [params.dataInicial, params.dataFinal])
        .orderBy('v.id_vendedor')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('v.*');

      return {
        sucesso: true,
        vendedores: vendedores,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar vendedor por data de criação e quantidade',
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
      const estadoAtivo = params.estado === 'true'
      const vendedores = await Vendedor.query()
        .from('vendedores as v')
        .join('administradores as a', 'v.responsavel', 'a.id_administrador')
        .where('a.id_instituicao', admin.id_instituicao)
        .where('v.ativo', estadoAtivo)
        .orderBy('v.id_vendedor')
        .limit(10)
        .offset((params.quantidade - 1) * 10)
        .select('v.*');

      return {
        sucesso: true,
        vendedores: vendedores,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao selecionar vendedor por atividade e quantidade',
        error: error.message,
      }
    }
  }



  public async alternarAtivo({ params, auth }: any) {
    try {
      let admin = (await Administrador.query().where('id_usuario', auth.user.id_usuario))[0]
      if (!admin) {
        return {
          sucesso: false,
          mensagem: "Não autorizado"
        }
      }
      const vendedor = await Vendedor.findOrFail(params.id_vendedor)
      vendedor.ativo = !vendedor.ativo
      vendedor.save()

      let operacao = new OperacaoVendedor()
      operacao.tipo = vendedor.ativo ? "A" : "D"
      operacao.id_vendedor = vendedor.id_vendedor
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
        mensagem: 'Erro ao alternar a atividade do vendedor',
        error: error.message,
      }
    }
  }

  public async alterarNome({ params, request }: any) {
    try {
      await Vendedor.query()
        .where('id_vendedor', params.id_vendedor)
        .update({ loja: request.body().nomeLoja })
      return {
        sucesso: true,
      }
    } catch (error) {
      return {
        sucesso: false,
        mensagem: 'Erro ao alterar nome da loja do vendedor',
        error: error.message,
      }
    }
  }
}
