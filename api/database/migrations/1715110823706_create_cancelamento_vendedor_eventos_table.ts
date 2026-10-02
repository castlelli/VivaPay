import { BaseSchema } from '@adonisjs/lucid/schema'

export default class extends BaseSchema {
  protected tableName = 'operacoes_vendedor_eventos'

  async up() {
    this.schema.createTable(this.tableName, (table) => {
      table.increments('id_operacao_vendedor_evento')
      table
        .bigInteger('responsavel')
        .unsigned()
        .references('id_administrador')
        .inTable('administradores')
        .nullable()
      table.timestamp('data_operacao').defaultTo(this.now())
      table
        .bigInteger('id_vendedor_evento')
        .unsigned()
        .references('id_vendedor_evento')
        .inTable('vendedor_eventos')
      table.string("tipo", 1).notNullable()
    })
  }

  async down() {
    this.schema.dropTable(this.tableName)
  }
}
