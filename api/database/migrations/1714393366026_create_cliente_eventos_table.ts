import { BaseSchema } from '@adonisjs/lucid/schema'

export default class extends BaseSchema {
  protected tableName = 'cliente_eventos'

  async up() {
    this.schema.createTable(this.tableName, (table) => {
      table.increments('id_cliente_evento')
      table.string('voucher', 255).notNullable().unique()
      table
        .bigInteger('id_cliente')
        .unsigned()
        .notNullable()
        .references('id_cliente')
        .inTable('clientes')
      table
        .bigInteger('id_evento')
        .unsigned()
        .notNullable()
        .references('id_evento')
        .inTable('eventos')
      table.timestamp('criado_em').defaultTo(this.now())
    })
  }

  async down() {
    this.schema.dropTable(this.tableName)
  }
}