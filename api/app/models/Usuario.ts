import { DateTime } from 'luxon'
import hash from '@adonisjs/core/services/hash'
import type { HasMany } from '@adonisjs/lucid/types/relations'
import { BaseModel, column, hasMany, beforeSave } from '@adonisjs/lucid/orm'
import Vendedor from '#models/Vendedor'
import Cliente from '#models/Cliente'
import { DbAccessTokensProvider } from '@adonisjs/auth/access_tokens'
import Administrador from '#models/Administrador'


export default class Usuario extends BaseModel {

  static accessTokens = DbAccessTokensProvider.forModel(Usuario, {
    expiresIn: '30 days',
    prefix: 'oat_',
    table: 'auth_access_tokens',
    type: 'auth_token',
    tokenSecretLength: 40,
  })

  @column({ isPrimary: true })
  public id_usuario!: number

  @column()
  public login!: string

  @column({})
  public senha!: string

  @column()
  public nome_usuario!: string

  @column.dateTime({ autoCreate: true })
  public criado_em!: DateTime

  @hasMany(() => Vendedor, { foreignKey: 'id_usuario' })
  public vendedor!: HasMany<typeof Vendedor>

  @hasMany(() => Cliente, { foreignKey: 'id_usuario' })
  public cliente!: HasMany<typeof Cliente>

  @hasMany(() => Administrador, { foreignKey: 'id_usuario' })
  public administrador!: HasMany<typeof Administrador>

  @beforeSave()
  public static async hashPassword(usuario: Usuario) {
    if (usuario.$dirty.senha) {
      usuario.senha = await hash.make(usuario.senha)
    }
  }

}
