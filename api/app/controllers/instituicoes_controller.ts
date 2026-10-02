
import Instituicao from "#models/Instituicao"

export default class InstituicoesController {
    public async cadastrar( { request }:any ){

        let nomeInstituicao = request.body().nome
        
        let abreviacaoInstituicao = request.body().abreviacao

        let cidadeEstado = request.body().cidadeEstado

        let cnpj = request.body().cnpj

        let instituicao = new Instituicao()
    
        instituicao.nome_instituicao = nomeInstituicao
    
        instituicao.abreviacao_instituicao = abreviacaoInstituicao
    
        instituicao.cidade_estado = cidadeEstado
    
        instituicao.cnpj = cnpj
    
        await instituicao.save()
    
    }
/*
    public async selecionarPorId(id:number)
    {
        let metodos = new MetodosUteiProvider()
        const instituicao =  await metodos.selecionarPorId(id, Instituicao, 'id_instituicao')
        return{
            sucesso: true,
            instituicao: instituicao
        }
    }

    public async alterarNome({ request }:any )
    {
        let metodos = new MetodosUteiProvider()
        const instituicao = (await this.selecionarPorId(request.body().id_instituicao)).instituicao
        await metodos.alterarString(instituicao, 'nome_instituicao', request.body().nome_instituicao)
        instituicao.save()
    }

    public async alterarAbreviacao({ request }:any )
    {
        let metodos = new MetodosUteiProvider()
        const instituicao = (await this.selecionarPorId(request.body().id_instituicao)).instituicao
        await metodos.alterarString(instituicao, 'abreviacao_instituicao', request.body().abreviacao_instituicao)
        instituicao.save()
    }

    public async alterarCNPJ({ request }:any )
    {
        let metodos = new MetodosUteiProvider()
        const instituicao = (await this.selecionarPorId(request.body().id_instituicao)).instituicao
        await metodos.alterarString(instituicao, 'cnpj', request.body().cnpj)
        instituicao.save()
    }

    public async alterarLocal({ request }:any )
    {
        let metodos = new MetodosUteiProvider()
        const instituicao = (await this.selecionarPorId(request.body().id_instituicao)).instituicao
        await metodos.alterarString(instituicao, 'cidade_estado', request.body().cidade_estado)
        instituicao.save()
    }*/

}