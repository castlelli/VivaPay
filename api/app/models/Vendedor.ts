import { DateTime } from 'luxon'
import type { BelongsTo, HasMany } from '@adonisjs/lucid/types/relations'
import { BaseModel, column, belongsTo, hasMany, afterSave } from '@adonisjs/lucid/orm'
import Usuario from '#models/Usuario'
import VendedorEvento from '#models/VendedorEvento'
import Administrador from '#models/Administrador'

export default class Vendedor extends BaseModel {
  public static table = 'vendedores'
  @column({ isPrimary: true })
  public id_vendedor!: number

  @column()
  public loja!: string

  @column({})
  public id_usuario!: number

  @column()
  public responsavel!: number

  @column.dateTime({ autoCreate: true })
  public criado_em!: DateTime

  @column()
  public ativo!: Boolean

  @belongsTo(() => Usuario, { foreignKey: 'id_usuario' })
  public usuario!: BelongsTo<typeof Usuario>

  @belongsTo(() => Administrador, { foreignKey: 'responsavel' })
  public administrador!: BelongsTo<typeof Administrador>

  @hasMany(() => VendedorEvento, { foreignKey: 'id_vendedor' })
  public vendedor_evento!: HasMany<typeof VendedorEvento>

  @afterSave()
  public static async afterSaveHook(vendedor: Vendedor) {
    console.log('User saved', vendedor)
  }
}
