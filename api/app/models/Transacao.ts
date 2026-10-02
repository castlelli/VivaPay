
import { DateTime } from 'luxon'
import type { BelongsTo } from '@adonisjs/lucid/types/relations'
import { BaseModel, belongsTo, column } from '@adonisjs/lucid/orm'
import ClienteEvento from '#models/ClienteEvento'
import VendedorEvento from './VendedorEvento.js'

export default class Transacao extends BaseModel {
  public static table = "transacoes"
  @column({ isPrimary: true })
  public id_transacao!: number

  @column()
  public valor!: number

  @column()
  public tipo!: string

  @column()
  public metodo!: string

  @column({})
  public id_cliente_evento!: number

  @column({})
  public id_vendedor_evento!: number

  @column.dateTime({ autoCreate: true })
  public criado_em!: DateTime

  @belongsTo(() => ClienteEvento, { foreignKey: 'id_cliente_evento' })
  public clienteEvento!: BelongsTo<typeof ClienteEvento>

  @belongsTo(() => VendedorEvento, { foreignKey: 'id_vendedor_evento' })
  public vendedorEvento!: BelongsTo<typeof VendedorEvento>
}
