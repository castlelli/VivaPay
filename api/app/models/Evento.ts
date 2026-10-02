import { DateTime } from 'luxon'
import { BaseModel, belongsTo, column, hasMany } from '@adonisjs/lucid/orm'
import type { BelongsTo, HasMany } from '@adonisjs/lucid/types/relations'
import Instituicao from '#models/Instituicao'
import VendedorEvento from '#models/VendedorEvento'
import Administrador from '#models/Administrador'
import ClienteEvento from './ClienteEvento.js'


export default class Evento extends BaseModel {
  @column({ isPrimary: true })
  public id_evento!: number

  @column()
  public nome_evento!: string

  @column()
  public responsavel!: number

  @column({})
  public descricao_evento !: string

  @column()
  public abreviacao_evento!: string

  @column.dateTime({ autoCreate: true })
  public criado_em !: DateTime

  @column.dateTime({})
  public hora_inicio !: DateTime

  @column.dateTime({})
  public hora_final !: DateTime

  @column({})
  public id_instituicao !: number

  @column()
  public ativo!: Boolean

  @belongsTo(() => Instituicao, { foreignKey: 'id_instituicao' })
  public instituicao!: BelongsTo<typeof Instituicao>

  @belongsTo(() => Administrador, { foreignKey: 'criado_por' })
  public administrador!: BelongsTo<typeof Administrador>

  @hasMany(() => VendedorEvento, { foreignKey: 'id_evento' })
  public vendedor_evento!: HasMany<typeof VendedorEvento>

  @hasMany(() => ClienteEvento, { foreignKey: 'id_evento' })
  public cliente_evento!: HasMany<typeof ClienteEvento>

}