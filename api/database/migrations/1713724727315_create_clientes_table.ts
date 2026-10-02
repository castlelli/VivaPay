import { BaseSchema } from '@adonisjs/lucid/schema'

export default class extends BaseSchema {
  protected tableName = 'clientes'

  async up() {
    this.schema.createTable(this.tableName, (table) => {
      table.increments('id_cliente')
      table
        .bigInteger('id_usuario')
        .unsigned()
        .notNullable()
        .references('id_usuario')
        .inTable('usuarios')
      table.timestamp('criado_em').defaultTo(this.now())
    })
  }

  async down() {
    this.schema.dropTable(this.tableName)
  }
}
