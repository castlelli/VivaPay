/*
|--------------------------------------------------------------------------
| Routes file
|--------------------------------------------------------------------------
|
| The routes file is used for defining the HTTP routes.
|
*/
/*import UsuariosController from '#controllers/usuarios_controller'
import ClientesController from '#controllers/clientes_controller'
import VendedoresController from '#controllers/vendedores_controller'
import EventosController from '#controllers/eventos_controller'
import VendedorEventosController from '#controllers/vendedor_eventos_controller'
import ClienteEventosController from '#controllers/cliente_eventos_controller'
import TransacoesController from '#controllers/transacoes_controller'*/
const UsuariosController = () => import('#controllers/usuarios_controller')
const ClientesController = () => import('#controllers/clientes_controller')
const VendedoresController = () => import('#controllers/vendedores_controller')
const EventosController = () => import('#controllers/eventos_controller')
const VendedorEventosController = () => import('#controllers/vendedor_eventos_controller')
const ClienteEventosController = () => import('#controllers/cliente_eventos_controller')
const TransacoesController = () => import('#controllers/transacoes_controller')
const OperacoesController = () => import('#controllers/operacoes_controller')
const AdministradoresController = () => import("#controllers/administradores_controller")
const PdfsController = () => import('#controllers/pdfs_controller')
import router from '@adonisjs/core/services/router'
import { middleware } from '#start/kernel'

import AutoSwagger from 'adonis-autoswagger'
import swagger from '#config/swagger'

// returns swagger in YAML
router.get('/swagger', async () => {
  return AutoSwagger.default.docs(router.toJSON(), swagger)
})

// Renders Swagger-UI and passes YAML-output of /swagger
router.get('/docs', async () => {
  return AutoSwagger.default.ui('/swagger', swagger)
  // return AutoSwagger.default.scalar("/swagger", swagger); to use Scalar instead
  // return AutoSwagger.default.rapidoc("/swagger", swagger); to use RapiDoc instead
})

router
  .group(() => {
    router.get('/saldo/total/:voucher/:data', [TransacoesController, 'selecionarSaldoPorVoucherPorTipo'])
    router.get('/saldo/:voucher', [ClienteEventosController, 'consultarSaldo'])
    router.put('/deposito', [TransacoesController, 'deposito'])
    router.put('/extorno', [TransacoesController, 'estorno'])
    router.put('/pagamento', [TransacoesController, 'pagamento'])
  })
  .prefix('/caixa')
  .use(middleware.auth({ perfil: 'vendedor' }))

/*router
  .group(() => {
    router
      .group(() => {
        router.put('/alterarnome', [InstituicoesController, 'alterarNome'])
        router.put('/alterarcnpj', [InstituicoesController, 'alterarCNPJ'])
        router.put('/alterarabreviacao', [InstituicoesController, 'alterarAbreviacao'])
        router.put('/alterarlocal', [InstituicoesController, 'alterarLocal'])
      })
      .prefix('/alteracao')
  })
  .prefix('instituicao')
  .use(middleware.auth({ perfil: 'admin' }))*/

router
  .group(() => {
    router.put('/usuario', [UsuariosController, 'cadastrar'])
    router.put('/vendedor', [VendedoresController, 'cadastrar'])
    router.put('/cliente', [ClientesController, 'cadastrar'])
    router.put('/evento', [EventosController, 'cadastrar'])
    //router.put('/instituicao', [InstituicoesController, 'cadastrar'])
    router.put('/vendedorevento', [VendedorEventosController, 'cadastrar'])
    router.put('/clienteevento', [ClienteEventosController, 'cadastrar'])
    //router.put('/administrador', [AdministradoresController, 'cadastrar'])
  })
  .prefix('/cadastro')
  .use(middleware.auth({ perfil: 'admin' }))

router
  .group(() => {
    router
      .group(() => {
        router.get('/porId/:id_administrador', [AdministradoresController, 'selecionarPorId'])
      })
      .prefix('/selecao')
  })
  .prefix('adms').use(middleware.auth({ perfil: 'admin' }))

router.group(() => {
  router.group(() => {
    router.get('/porid/:id_usuario', [UsuariosController, 'selecionarPorId'])
  }).prefix('/selecao')
}).prefix('usuarios').use(middleware.auth({ perfil: 'admin' }))

router
  .group(() => {
    router
      .group(() => {
        router.get('/todos', [VendedoresController, 'selecionarTodos'])
        router.get('/porid/:id_vendedor', [VendedoresController, 'selecionarPorId'])
        router.get('/poridproprio', [VendedoresController, 'selecionarPorIdProprio'])
        router.get('/pornome/:nomeVendedor', [VendedoresController, 'selecionarPorNome'])
        router.get('/porcriador/:responsavel', [VendedoresController, 'selecionarPorCriador'])
        router.get('/pordatacriacao/:dataInicial/:dataFinal', [
          VendedoresController,
          'selecionarPorDataCriacao',
        ])
        router.get('/porativo/:estado', [VendedoresController, 'selecionarPorAtivo'])

        router.group(() => {
          router.get('/:quantidade', [VendedoresController, 'selecionarPorQuantidade'])
          router.get('/porNome/:nomeVendedor/:quantidade', [VendedoresController, 'selecionarPorNomePorQuantidade'])
          router.get('/porCriador/:responsavel/:quantidade', [VendedoresController, 'selecionarPorCriadorPorQuantidade'])
          router.get('/porDataCriacao/:dataInicial/:dataFinal/:quantidade', [
            VendedoresController,
            'selecionarPorDataCriacaoPorQuantidade',
          ])
          router.get('/porAtivo/:estado/:quantidade', [VendedoresController, 'selecionarPorAtivoPorQuantidade'])
        }).prefix('porQuantidade')

      })
      .prefix('/selecao')

    router.put('/alterarnome/:id_vendedor', [VendedoresController, 'alterarNome'])
    router.put('/alternarativo/:id_vendedor', [VendedoresController, 'alternarAtivo'])
  })
  .prefix('/vendedor')
  .use(middleware.auth({ perfil: 'admin' }))
router
  .group(() => {
    router
      .group(() => {
        router.get('/todos', [VendedorEventosController, 'selecionarTodos'])
        router.get('/porQuantidade/:quantidade', [VendedorEventosController, 'selecionarTodosPorQuantidade'])
        router.get('/porid/:id_vendedor_evento', [VendedorEventosController, 'selecionarPorId'])
        router.get('/porvendedor/:id_vendedor', [VendedorEventosController, 'selecionarPorVendedor'])
        router.get('/porloja/:loja', [VendedorEventosController, 'selecionarPorLoja'])
        router.get('/porquantidade/porloja/:loja/:quantidade', [VendedorEventosController, 'selecionarPorLojaPorQuantidade'])
        router.get('/porevento/:id_evento', [VendedorEventosController, 'selecionarPorEvento'])
        router.get('/pordatacriacao/:dataInicial/:dataFinal', [VendedorEventosController, 'selecionarPorDataCriacao'])
        router.get('/porativo/:estado', [VendedorEventosController, 'selecionarPorAtivo'])
      })
      .prefix('/selecao')

    router.put('/alternarativo/:id_vendedor_evento', [VendedorEventosController, 'alternarAtivo'])
  })
  .prefix('/vendedorevento')
  .use(middleware.auth({ perfil: 'admin' }))

router
  .group(() => {
    router
      .group(() => {
        router.get('/todos', [ClienteEventosController, 'selecionarTodos'])
        router.get('/porid/:id_cliente_evento', [ClienteEventosController, 'selecionarPorId'])
        router.get('/porcliente/:id_cliente', [ClienteEventosController, 'selecionarPorCliente'])
        router.get('/porevento/:id_evento', [ClienteEventosController, 'selecionarPorEvento'])
        //router.get('/porvoucher/:voucher', [ClientesController, 'selecionarPorVoucher'])
        // router.get('/pornomevento/:nomeEvento', [ClienteEventosController,'selecionarPorNomeEvento'])
      })
      .prefix('/selecao')
  })
  .prefix('/clienteevento')
  .use(middleware.auth({ perfil: 'admin' }))

router
  .group(() => {
    router
      .group(() => {
        //router.get('/todos', [ClientesController, 'selecionarTodos'])
        //router.get('/porid/:id_cliente', [ClientesController, 'selecionarPorId'])
        //router.get('/porvoucher/:voucher', [ClientesController, 'selecionarPorVoucher'])
        //router.get('/pordatacriacao/', [ClientesController, 'selecionarPorDataCriacao'])
      })
      .prefix('/selecao')
  })
  .prefix('/cliente')
  .use(middleware.auth({ perfil: 'admin' }))

router
  .group(() => {
    router
      .group(() => {
        router.get('/todos', [EventosController, 'selecionarTodos']);
        router.get('/porquantidade/:quantidade', [EventosController, 'selecionarPorQuantidade']);
        router.get('/porId/:id_evento', [EventosController, 'selecionarPorId']);
        router.get('/pornome/:nomeEvento', [EventosController, 'selecionarPorNome']);
        // router.get('/pordatacriacao', [EventosController, 'selecionarPorDataCriacao']);
        router.get('/porcriador/:responsavel', [EventosController, 'selecionarPorCriador']);
        router.get('/porativo/:estado', [EventosController, 'selecionarPorAtivo']);
        router.get('/porinicioefim/:horaInicio/:horaFinal', [EventosController, 'selecionarPorInicioeFim']);
        // router.get('/comsaldo/:id_evento', [EventosController, 'selecionarComSaldo']);

        router.group(() => {
          router.get('/todos', [EventosController, 'selecionarTodosPorQuantidade']);
          router.get('/porresponsavel/:responsavel/:quantidade', [EventosController, 'selecionarPorResponsavelPorQuantidade']);
          router.get('/porNome/:nome/:quantidade', [EventosController, 'selecionarPorNomePorQuantidade']);
        }).prefix('/porquantidade');

      }).prefix('/selecao');

    router.put('/alterarnome/:id_evento', [EventosController, 'alterarNome']);
    router.put('/alterardesc/:id_evento', [EventosController, 'alterarDesc']);
    router.put('/alterarabrev/:id_evento', [EventosController, 'alterarAbrev']);
    router.put('/alterarinicioefim/:id_evento', [EventosController, 'alterarInicioeFim']);
    router.put('/alternarativo/:id_evento', [EventosController, 'alternarAtivo']);
  })
  .prefix('/evento')
  .use(middleware.auth({ perfil: 'admin' }));

router
  .group(() => {
    router.post('/deposito', [TransacoesController, 'deposito'])
    router.post('/estorno', [TransacoesController, 'estorno'])

    router.group(() => {
      router.get('/tudo/:datainicio/:datafinal', [TransacoesController, 'selecionarTudo'])
      router.get('/portipo/:tipo', [TransacoesController, 'selecionarPorTipo'])
      router.get('/portipo/pordata/:tipo/:datainicio/:datafinal', [TransacoesController, 'selecionarPorTipoPorData'])
      router.get('/pormetodo/:metodo', [TransacoesController, 'selecionarPorMetodo'])
      router.get('/porloja', [TransacoesController, 'selecionarPorVendedorEvento'])
      router.get('/porcliente', [TransacoesController, 'selecionarPorCliente'])
      router.get('/porclienteEvento', [TransacoesController, 'selecionarPorClienteEvento'])
      router.get('/porvoucherportipo/:voucher/:tipo', [TransacoesController, 'selecionarPorVoucherPorTipo'])

      router.group(() => {
        router.get('/:quantidade', [TransacoesController, 'selecionarPorQuantidade'])
        router.get('/portipoporquantidade/:tipo/:quantidade', [TransacoesController, 'selecionarPorTipoPorQuantidade'])
        router.get('/porvendedorevento/:id_vendedorEvento/:quantidade', [TransacoesController, 'selecionarPorVendedorEventoPorQuantidade'])
        router.get('/porvendedor/:id/:quantidade', [TransacoesController, 'selecionarPorVendedorPorQuantidade'])
        router.get('/porclienteevento/:id_clienteEvento/:quantidade', [TransacoesController, 'selecionarPorClienteEventoPorQuantidade'])
        router.get('/porvoucherportipo/:voucher/:tipo/:quantidade', [TransacoesController, 'selecionarPorVoucherPorTipoPorQuantidade'])
        router.get('/porcliente/:id_cliente/:quantidade', [TransacoesController, 'selecionarPorClientePorQuantidade'])
      }).prefix('/porquantidade')

    }).prefix('/selecao')
  }).prefix('/transacao')
  .use(middleware.auth({ perfil: 'admin' }))


router
  .group(() => {
    router.group(() => {
      router.get('/todos/:quantidade', [VendedorEventosController, 'selecionarTodosPorQuantidadeVendedor']);
      router.get('/porvendedor/:id_vendedor/:quantidade', [VendedorEventosController, 'selecionarPorVendedorPorQuantidadeVendedor']);
      router.get('/porevento/:id_evento/:quantidade', [VendedorEventosController, 'selecionarPorEventoPorQuantidadeVendedor']);
      router.get('/pordatacriacao/:dataInicial/:dataFinal/:quantidade', [VendedorEventosController, 'selecionarPorDataCriacaoPorQuantidadeVendedor']);
      router.get('/porativo/:estado/:quantidade', [VendedorEventosController, 'selecionarPorAtivoPorQuantidadeVendedor']);
      router.get('/porvendedorproprio/', [VendedorEventosController, 'selecionarParaVendedor']);
      router.get('/buscarsaldo', [VendedorEventosController, 'buscarSaldoLoja'])
    }).prefix('/selecao')

  })
  .prefix('/vendedorevento')
  .use(middleware.auth({ perfil: 'vendedor' }));


router
  .group(() => {
    router.group(() => {
      router.get('/porquantidade/:quantidade', [TransacoesController, 'selecionarPorQuantidadeVendedor']);
      router.get('/portipo/:tipo/:quantidade', [TransacoesController, 'selecionarPorTipoPorQuantidadeVendedor']);
      router.get('/porvendedorevento/:idVendedorEvento/:quantidade', [TransacoesController, 'selecionarPorVendedorEventoPorQuantidadeVendedor']);
      router.get('/porvendedor/:id/:quantidade', [TransacoesController, 'selecionarPorVendedorPorQuantidadeVendedor']);
      router.get('/porclienteevento/:idClienteEvento/:quantidade', [TransacoesController, 'selecionarPorClienteEventoPorQuantidadeVendedor']);
      router.get('/porcliente/:idCliente/:quantidade', [TransacoesController, 'selecionarPorClientePorQuantidadeVendedor']);
    }).prefix('/selecao')
  })
  .prefix('/transacoes')
  .use(middleware.auth({ perfil: 'vendedor' }));

router.group(() => {
  router
    .group(() => {
      router
        .group(() => {
          //router.get('/todos', [OperacoesController, 'selecionarTudo'])
          router.get('/vendedor/porquantidade/:quantidade', [OperacoesController, 'selecionarTudoVendedorPorQuantidade'])
          router.get('/vendedorevento/porQuantidade/:quantidade', [OperacoesController, 'selecionarTudoVendedorEventoPorQuantidade'])
          router.get('/evento/porquantidade/:quantidade', [OperacoesController, 'selecionarTudoEventoPorQuantidade'])
          router.get('/vendedor', [OperacoesController, 'selecionarTudoVendedor'])
          router.get('/vendedorevento', [OperacoesController, 'selecionarTudoVendedorEvento'])
          router.get('/evento', [OperacoesController, 'selecionarTudoEvento'])
        }).prefix('/tudo')

      router.get('/vendedor/porquantidade/:quantidade', [OperacoesController, 'selecionarTudoVendedorPorQuantidade'])
      router.get('/vendedorevento/porquantidade/:quantidade', [OperacoesController, 'selecionarTudoVendedorEventoPorQuantidade'])
      router.get('/evento/porQuantidade/:quantidade', [OperacoesController, 'selecionarTudoEventoPorQuantidade'])

      router
        .group(() => {
          router.get('/vendedor/:idVendedor', [OperacoesController, 'selecionarPorVendedor'])
          router.get('/vendedor-evento/:idVendedorEvento', [OperacoesController, 'selecionarPorVendedorEvento'])
          router.get('/evento/:idEvento', [OperacoesController, 'selecionarPorEvento'])
        }).prefix('/porid')

      router
        .group(() => {
          router.get('/vendedor/:nomeVendedor', [OperacoesController, 'selecionarPorNomeVendedor'])
          router.get('/evento/:nomeEvento', [OperacoesController, 'selecionarPorNomeEvento'])
          router.get('/vendedorevento/:nomeVendedor', [OperacoesController, 'selecionarPorNomeVendedorEvento'])
          router.get('/porquantidade/vendedor/:nomeVendedor/:quantidade', [OperacoesController, 'selecionarPorNomeVendedorPorQuantidade'])
          router.get('/porquantidade/evento/:nomeEvento/:quantidade', [OperacoesController, 'selecionarPorNomeEventoPorQuantidade'])
          router.get('/porquantidade/vendedorevento/:nomeVendedor/:quantidade', [OperacoesController, 'selecionarPorNomeVendedorEventoPorQuantidade'])
        }).prefix('/pornome')

      router
        .group(() => {
          router.get('/vendedor/:idAdm', [OperacoesController, 'selecionarVendedorPorADM'])
          router.get('/evento/:idAdm', [OperacoesController, 'selecionarEventoPorADM'])
          router.get('/vendedor-evento/:idAdm', [OperacoesController, 'selecionarVendedorEventoPorADM'])
        }).prefix('/poradm')
    }).prefix('/selecao')
}).prefix('/operacoes')
  .use(middleware.auth({ perfil: 'admin' }))

router.post('login', [UsuariosController, 'login'])

router.group(() => {
  router.group(() => {
    router.get('/comprovante', [PdfsController, 'generateReceipt'])
  }).prefix('/criacao')
}).prefix('/pdf')
