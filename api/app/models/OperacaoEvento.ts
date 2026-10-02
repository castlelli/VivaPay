import { DateTime } from 'luxon'
import { BaseModel, column, belongsTo } from '@adonisjs/lucid/orm'
import type { BelongsTo} from '@adonisjs/lucid/types/relations'
import Evento from '#models/Evento'
import Administrador from '#models/Administrador'

export default class OperacaoEvento extends BaseModel {
  public static table = "operacoes_eventos"
  @column({ isPrimary: true })
  public id_operacao_evento!: number

  @column()
  public responsavel!: number

  @column.dateTime()
  public data_operacao!: DateTime

  @column()
  public id_evento!: number

  @column()
  public tipo !: string

  @belongsTo(() => Administrador, { foreignKey: 'responsavel' })
  public responsavelPor!: BelongsTo<typeof Administrador>

  @belongsTo(() => Evento, { foreignKey: 'id_evento' })
  public evento!: BelongsTo<typeof Evento>
}
