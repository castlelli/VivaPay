import { BaseSchema } from '@adonisjs/lucid/schema'

export default class extends BaseSchema {
  protected tableName = 'transacoes'

  async up() {
    this.schema.createTable(this.tableName, (table) => {
      table.increments('id_transacao')
      table.double('valor').notNullable()
      table.string('tipo', 1).notNullable()
      table.string('metodo', 1).notNullable()
      table
        .bigInteger('id_cliente_evento')
        .unsigned()
        .notNullable()
        .references('id_cliente_evento')
        .inTable('cliente_eventos')
      table
        .bigInteger('id_vendedor_evento')
        .unsigned()
        .nullable()
        .references('id_vendedor_evento')
        .inTable('vendedor_eventos')
      table.timestamp('criado_em').defaultTo(this.now())
    })
  }

  async down() {
    this.schema.dropTable(this.tableName)
  }
}
