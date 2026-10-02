import { DateTime } from 'luxon'
import { BaseModel, column, belongsTo, hasMany } from '@adonisjs/lucid/orm'
import type { BelongsTo, HasMany } from '@adonisjs/lucid/types/relations'
import Evento from '#models/Evento'
import Vendedor from '#models/Vendedor'
import Administrador from '#models/Administrador'
import Transacao from '#models/Transacao'

export default class VendedorEvento extends BaseModel {
  @column({ isPrimary: true })
  public id_vendedor_evento!: number

  @column()
  public id_vendedor!: number

  @column()
  public id_evento!: number

  @column()
  public responsavel!: number

  @column.dateTime({ autoCreate: true })
  public criado_em!: DateTime

  @column()
  public ativo!: Boolean

  @belongsTo(() => Evento, { foreignKey: 'id_evento' })
  public evento!: BelongsTo<typeof Evento>

  @belongsTo(() => Vendedor, { foreignKey: 'id_vendedor' })
  public vendedor!: BelongsTo<typeof Vendedor>

  @belongsTo(() => Administrador, { foreignKey: 'responsavel' })
  public administrador!: BelongsTo<typeof Administrador>

  @hasMany(() => Transacao, { foreignKey: 'id_vendedor_evento' })
  public transacao!: HasMany<typeof Transacao>
}
