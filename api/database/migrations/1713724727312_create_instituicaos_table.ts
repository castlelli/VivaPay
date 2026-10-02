import { BaseSchema } from '@adonisjs/lucid/schema'

export default class extends BaseSchema {
  protected tableName = 'instituicoes'

  async up() {
    this.schema.createTable(this.tableName, (table) => {
      table.increments('id_instituicao')
      table.string('nome_instituicao', 255).notNullable().unique()
      table.string('abreviacao_instituicao', 255).nullable()
      table.string('cidade_estado', 255).notNullable()
      table.string('cnpj', 14).notNullable()
      table.timestamp('criado_em').defaultTo(this.now())
      table.timestamp('cancelado_em').nullable()
    })
  }

  async down() {
    this.schema.dropTable(this.tableName)
  }
}