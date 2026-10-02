import { DateTime } from 'luxon'
import { BaseModel, column, belongsTo } from '@adonisjs/lucid/orm'
import type { BelongsTo } from '@adonisjs/lucid/types/relations'
import VendedorEvento from '#models/VendedorEvento'
import Administrador from '#models/Administrador'

export default class OperacaoVendedorEvento extends BaseModel {
  public static table = "operacoes_vendedor_eventos"
  @column({ isPrimary: true })
  public id_operacao_vendedor_evento!: number

  @column()
  public responsavel!: number

  @column.dateTime()
  public data_operacao!: DateTime

  @column()
  public tipo !: string

  @column()
  public id_vendedor_evento!: number

  @belongsTo(() => Administrador, { foreignKey: 'responsavel' })
  public administrador!: BelongsTo<typeof Administrador>

  @belongsTo(() => VendedorEvento, { foreignKey: 'id_vendedor_evento' })
  public vendedorEvento!: BelongsTo<typeof VendedorEvento>
}
