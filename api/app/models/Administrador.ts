import { DateTime } from 'luxon'
import type { BelongsTo, HasMany } from '@adonisjs/lucid/types/relations'
import { BaseModel, column, hasMany, belongsTo } from '@adonisjs/lucid/orm'
import Vendedor from '#models/Vendedor'
import Usuario from '#models/Usuario'
import VendedorEvento from '#models/VendedorEvento'
import Evento from '#models/Evento'
import Instituicao from '#models/Instituicao'

export default class Administrador extends BaseModel {
  public static table = "administradores"
  @column({ isPrimary: true })
  public id_administrador!: number

  @column({ })
  public id_usuario!: number

  @column({ serializeAs: null })
  public id_instituicao!: number

  @column.dateTime({ autoCreate: true })
  public criado_em!: DateTime

  @column.dateTime()
  public cancelado_em!: DateTime

  @belongsTo(() => Usuario, { foreignKey: 'id_usuario' })
  public usuario!: BelongsTo<typeof Usuario>

  @belongsTo(() => Instituicao, { foreignKey: 'id_instituicao' })
  public instituicao!: BelongsTo<typeof Instituicao>

  @hasMany(() => VendedorEvento, { foreignKey: 'responsavel' })
  public vendedores_eventos!: HasMany<typeof VendedorEvento>

  @hasMany(() => Vendedor, { foreignKey: 'responsavel' })
  public vendedores_criados!: HasMany<typeof Vendedor>

  @hasMany(() => Evento, { foreignKey: 'responsavel' })
  public eventos_criados!: HasMany<typeof Evento>
}