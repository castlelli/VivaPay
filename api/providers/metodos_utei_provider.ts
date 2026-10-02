/*import type { ApplicationService } from '@adonisjs/core/types'
import Usuario from '#models/Usuario'
import Vendedor from '#models/Vendedor'
import Administrador from '#models/Administrador'
import Instituicao from '#models/Instituicao'
import Cliente from '#models/Cliente'
import ClienteEvento from '#models/ClienteEvento'
import Evento from '#models/Evento'
import VendedorEvento from '#models/VendedorEvento'
import { DateTime } from 'luxon'
//import OperacaoEvento from '#models/OperacaoEvento'
//import OperacaoVendedor from '#models/OperacaoVendedor'
//import { ModelAttributes } from '@adonisjs/lucid/build/src/types/model'
//import OperacaoVendedorEvento from '#models/OperacaoVendedorEvento'
//import VendedoresController from '#controllers/vendedores_controller'

//OBSERVAÇÃO -> Otimizar métodos de verificar
export default class MetodosUteiProvider {
  constructor(protected app: ApplicationService) {}

  public async verificarExiste(id: number, Class: any, atributo: string)
  {
    const objeto = this.selecionarPorId(id, Class, atributo)
    return (objeto !=  null && objeto != undefined)
  }
  

  public async selecionarPorADM(ADM: number, Classe: any, atributo: string){
    try {
      const objeto = await Classe.query().where(atributo, ADM)
    return objeto
    } catch (error) {
        return "Erro: "+error
    }
    
  }

  public async selecionarPorData(data_inicial: Date, data_final: Date, Classe: any, atributo: string)
  {
    try {

      const objeto = await Classe
      .where(atributo, '>=', data_inicial)
      .andWhere(atributo, '<=', data_final)
      return objeto
    } catch (error) {
      return "Erro:" +error
    }
    
  }

  public async selecionarPorId(id: number, Classe: any, atributo: string)
  {
    let objeto = null
    try{
      objeto = await Classe.findBy(atributo, id)
      return objeto
    } catch(error){
        return "Erro:"+error
    }

    
  }
  
  public async selecionarPorBooleano(atributo: string, Classe: any, estado: boolean)
  {
    try {
      const objeto = await Classe.findBy(atributo, estado)
      return objeto
    } catch (error) {
      return "Erro:"
    }
    
  }

  public async alternarBooleano(objeto: any)
  {
    try{
      objeto.ativo = !objeto.ativo
      await objeto.save()
    } catch(error)
    {
      return "Erro: "+error
    }
    
  }

  /*public async selecionarTudo(Classe: any)
  {
    let tudo = (await Classe.query().whereHas('relacao1', u =>{
      //     u.where('id_usuario', auth.user.id_usuario)
       }).whereHas('re', q=>{
           q.where("id_instituicao", admin.id_instituicao)
       }))
    return tudo
  }

  public async selecionarAssociativaPorString(classePai: any, associativa: any, atributo: any, parametro: any, classePai)
    {
      //Nesse método usamos SQL para procurar uma classe associativa por nome.
      //Para isso, procuramos Vendedores que tem algum VendedorEvento correspondente. Depois, filtramos esses por nome.

      //Variáveis para facilitar
      let id_classePai = classePai+'.id_'+classePai.toLowerCase()
      let id_associativa = associativa+'.id_'+associativa.toLowerCase()
      let atributo_pai = classePai+'.'+atributo

      const objetos = await classePai
        .from(classePai) //Selecionar da Classe Pai
        .innerJoin(classePai, id_classePai, id_associativa) //Aqueles com id corresponde à associativa.
        .where(atributo_pai, 'LIKE', `%${parametro}%`) //E cujo o atributo x corresponde ao parâmetro de pesquisa.
        .select(associativa+'*.'); //Seleciona todas as colunas.
      
      return objetos
    }

  //É bom a gente separar esses que cadastram uma classe específica.
  //MÉTODOS DE CADASTRO
  public async cadastrarUsuario(login: string, senha: string, nome: string)
  {
    try {
      let usuario =  new Usuario()
      usuario.login = login 
      usuario.senha = senha 

      usuario.nome_usuario = nome

      await usuario.save()
      
      return usuario
    } catch (error) {
      return "Erro:"+error
    }
      

  }

  public async cadastrarVendedor(id_usuario: number, id_criador: number, nome: string)
  {
      try{
      const usuario = this.selecionarPorId(id_usuario, Usuario, 'id_usuario')
      if(usuario == null || usuario == undefined)
      {
        return{
          sucesso: false,
          mensagem: "Usuário enviado inválido."
        }
      }

      const criador = this.selecionarPorId(id_criador, Administrador, 'id_administrador')
      if(criador == null || criador == undefined)
        {
          return{
            sucesso: false,
            mensagem: 'Criador inválido'
          }
        }
      
      let vendedor = new Vendedor()

      vendedor.id_usuario = id_usuario,
      
      vendedor.responsavel = id_criador,
      
      vendedor.loja = nome
      
      await vendedor.save()
      
      return vendedor
    } catch(error){
      return "Erro:"+ error
    }
  }

  public async cadastrarCliente(id_usuario: number, voucher: string)
  {
    try{
      if(voucher==null || voucher =="")
      {
        do{
          let voucherTeste = Math.floor(Math.random() * 99999999)
          var validacaoVoucher = (await Cliente.query().where("voucher", voucherTeste))[0]
          voucher = voucherTeste.toString()
        }while(validacaoVoucher)
      }
        
      
        let cliente = new Cliente()
        
        cliente.id_usuario = id_usuario
                
        await cliente.save()


        return cliente
    } catch(error){
      return "Erro:"+error
    }
  }

  public async cadastrarClienteEvento(id_usuario: number, voucher: string, id_evento: number)
  {
    try{
      let cliente = await this.cadastrarCliente(id_usuario, voucher)
      let existe = this.verificarExiste(cliente.id_cliente, Cliente, 'id_cliente')
      if(!existe)
      {
        return{
          sucesso: false,
          mensagem: 'Cliente inválido.'
        }
      }
      const evento = this.verificarExiste(id_evento, Evento, 'id_evento')
      if(!evento)
      {
        return{
          sucesso: false,
          mensagem: 'Evento inválido.'
        }
      }
      
        let clienteEvento = new ClienteEvento()
      
      clienteEvento.id_cliente = cliente.id_cliente
      
      clienteEvento.id_evento = id_evento
      
      await clienteEvento.save()

      return clienteEvento
    } catch(error){
      return "Erro: "+error
    }
  }

  public async cadastrarVendedorEvento(id_vendedor: number, id_evento: number, id_adm: number)
  {
    const vendedorExiste = this.verificarExiste(id_vendedor, Vendedor, 'id_vendedor')
    if(!vendedorExiste)
    {
      return{
        sucesso: false,
        mensagem: 'Vendedor inválido.'
      }
    }
    /*const eventoExiste = await this.verificarExiste(id_evento, Evento, 'id_evento')
    if(!eventoExiste)
    {
      return{
        sucesso: false,
        mensagem: 'Evento inválido.'
      }
    }
    let vendedorEvento = new VendedorEvento()
    vendedorEvento.criado_por = id_adm

    vendedorEvento.id_evento = id_evento
    
    vendedorEvento.id_vendedor = id_vendedor
    
    await vendedorEvento.save()

    return vendedorEvento
  }

  public async cadastrarAdm(id_instituicao: number, id_usuario: number)
  {
    const instituicao = this.verificarExiste(id_instituicao, Instituicao, 'id_instituicao')
    if(!instituicao)
    {
      return{
        sucesso: false,
        mensagem: 'Instituição inválida.'
      }
    }
    
    let adm = new Administrador()
    
    adm.id_instituicao = id_instituicao

    adm.id_usuario = id_usuario
    
    await adm.save()

    return adm
    
  }

  public async cadastrarEvento(id_instituicao: number, 
    id_administrador: number, 
    nome: string, 
    abrev: string, 
    hora_inicio: DateTime, 
    hora_final: DateTime)
  {
    const instituicao = this.verificarExiste(id_instituicao, Instituicao, 'id_instituicao')
    if(!instituicao)
      {
        return{
          sucesso: false,
          mensagem: 'Instituição inválida.'
        }
      }
    if(!instituicao)
      {
        return{
          sucesso: false,
          mensagem: 'Criador inválido'
        }
      }
    let evento = new Evento()
    evento.responsavel = id_administrador
    evento.id_instituicao = id_instituicao
    evento.abreviacao_evento = abrev
    evento.nome_evento = nome
    evento.hora_inicio = hora_inicio
    evento.hora_final = hora_final
    evento.save()

    return evento

  }
/*
  public async operacaoEvento(id_evento: number, id_adm: number)
  { 
    const evento = this.selecionarPorId(id_evento, Evento, 'id_evento')
    const tipo = evento.ativo ? "A" : "C"
    let operacao = new OperacaoEvento()
    operacao.tipo = tipo
    operacao.responsavel = id_adm
    operacao.evento = id_evento
    await operacao.save()
  }

  public async operacaoVendedor(id_vendedor: number, id_adm: number)
  { 
    let VC: VendedoresController = VendedoresController
    const vendedor = VC.selecionarPorId(id_vendedor)
    const tipo = vendedor.ativo ? "A" : "C"
    let operacao = new OperacaoVendedor()
    operacao.tipo = tipo
    operacao.responsavel = id_adm
    operacao.id_vendedor = id_vendedor
    await operacao.save()
  }

  public async operacaoVendedorEvento(id_vendedor_evento: number, id_adm: number)
  { 
    const vendedor = this.selecionarPorId(id_vendedor_evento, VendedorEvento, 'id_vendedor_evento')
    const tipo = vendedor.ativo ? "A" : "C"
    let operacao = new OperacaoVendedorEvento()
    operacao.tipo = tipo
    operacao.responsavel = id_adm
    operacao.id_vendedor_evento = id_vendedor_evento
    await operacao.save()
  }




}*/
