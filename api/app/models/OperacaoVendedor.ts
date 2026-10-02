import { DateTime } from 'luxon'
import { BaseModel, column, belongsTo } from '@adonisjs/lucid/orm'
import type { BelongsTo} from '@adonisjs/lucid/types/relations'
import Vendedor from '#models/Vendedor'
import Administrador from '#models/Administrador'

export default class OperacaoVendedor extends BaseModel {
  public static table = "operacoes_vendedores"
  @column({ isPrimary: true })
  public id_operacao_vendedor!: number

  @column()
  public responsavel!: number

  @column.dateTime()
  public data_operacao!: DateTime

  @column()
  public tipo !: string

  @column()
  public id_vendedor!: number

  @belongsTo(() => Administrador, { foreignKey: 'responsavel' })
  public administrador!: BelongsTo<typeof Administrador>

  @belongsTo(() => Vendedor, { foreignKey: 'id_vendedor' })
  public vendedor!: BelongsTo<typeof Vendedor>
}
