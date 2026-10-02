import { BaseSchema } from '@adonisjs/lucid/schema'

export default class extends BaseSchema {
  protected tableName = 'operacoes_vendedores'

  async up() {
    this.schema.createTable(this.tableName, (table) => {
      table.increments('id_operacao_vendedor')
      table
        .bigInteger('responsavel')
        .unsigned()
        .references('id_administrador')
        .inTable('administradores')
        .nullable()
      table.timestamp('data_operacao').defaultTo(this.now())
      table
        .bigInteger('id_vendedor')
        .unsigned()
        .references('id_vendedor')
        .inTable('vendedores')
      table.string("tipo", 1).notNullable()
    })
  }

  async down() {
    this.schema.dropTable(this.tableName)
  }
}
