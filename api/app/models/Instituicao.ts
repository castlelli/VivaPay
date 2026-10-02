//Erros nessa parte do código são normais e característicos de TypeScript. 
import { DateTime } from 'luxon'
import { BaseModel, column, hasMany } from '@adonisjs/lucid/orm'
import type { HasMany } from '@adonisjs/lucid/types/relations'
import Evento from '#models/Evento'

export default class Instituicao extends BaseModel {
  public static table = "instituicoes"
  //Cada declaração dessa é um atributo da classe.
  @column({ isPrimary: true }) //isPrimary define qual é chave primária
  public id_instituicao!: number

  @column()
  public nome_instituicao!: string

  @column()
  public abreviacao_instituicao!: string

  //serializeAs é um método de proteção de dados. Usamos para informações mais sensíveis.
  @column({})
  public cnpj!: string

  @column({})
  public cidade_estado!: string

  /*AutoCreate cria automáticamente essa informação.
  No caso da data de criação, ela é obrigatória (NOT NULL), 
  então automaticamente ela cria na data de hoje.*/
  @column.dateTime({ autoCreate: true }) //******VER SE ESSE DATE TIME É NECESSÁRIO POR SER AUTOCREATE *******/
  public criado_em!: DateTime

  @column.dateTime({})
  public cancelado_em!: DateTime

  //BelongsTo quer dizer há um atributo nessa classe
  //que referencia outra. 

  //Has many quer dizer que essa classe está em outras.
  @hasMany(() => Evento, { foreignKey: 'id_instituicao' })
  public instituicao!: HasMany<typeof Evento>

}