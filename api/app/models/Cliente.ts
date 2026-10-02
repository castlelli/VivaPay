import { DateTime } from 'luxon'
import type { BelongsTo, HasMany } from '@adonisjs/lucid/types/relations'
import { BaseModel, belongsTo, column, hasMany } from '@adonisjs/lucid/orm'
import Usuario from '#models/Usuario'
import ClienteEvento from '#models/ClienteEvento'

export default class Cliente extends BaseModel {
  @column({ isPrimary: true })
  public id_cliente!: number

  @column()
  public id_usuario!: number

  @column.dateTime({ autoCreate: true })
  public criado_em!: DateTime

  @belongsTo(() => Usuario, { foreignKey: 'id_usuario' })
  public usuario!: BelongsTo<typeof Usuario>

  @hasMany(() => ClienteEvento, { foreignKey: 'id_cliente' })
  public transacao!: HasMany<typeof ClienteEvento>

}
