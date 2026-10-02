import { DateTime } from 'luxon'
import type { BelongsTo, HasMany } from '@adonisjs/lucid/types/relations'
import { BaseModel, belongsTo, column, hasMany } from '@adonisjs/lucid/orm'
import Transacao from '#models/Transacao'
import Cliente from '#models/Cliente'
import Evento from '#models/Evento'

export default class ClienteEvento extends BaseModel {
  @column({ isPrimary: true })
  public id_cliente_evento!: number

  @column({})
  public id_cliente!: number

  @column({})
  public voucher!: string

  @column({})
  public id_evento!: number

  @column.dateTime({ autoCreate: true })
  public criado_em!: DateTime

  @belongsTo(() => Cliente, { foreignKey: 'id_cliente' })
  public cliente!: BelongsTo<typeof Cliente>

  @belongsTo(() => Evento, { foreignKey: 'id_evento' })
  public evento!: BelongsTo<typeof Evento>

  @hasMany(() => Transacao, { foreignKey: 'id_cliente_evento' })
  public transacao!: HasMany<typeof Transacao>

  get saldo() {
    let s = 0

    this.transacao.forEach(t => {
      if (t.tipo === 'D' || t.tipo === 'E')
        s -= t.valor
      else
        s += t.valor
    })

    return s
  }
}