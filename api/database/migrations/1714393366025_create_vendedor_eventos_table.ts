import { BaseSchema } from '@adonisjs/lucid/schema'

export default class extends BaseSchema {
  protected tableName = 'vendedor_eventos'

  async up() {
    this.schema.createTable(this.tableName, (table) => {
      table.increments('id_vendedor_evento')
      table.bigInteger('id_vendedor')
        .unsigned()
        .notNullable()
        .references('id_vendedor')
        .inTable('vendedores')
      table.bigInteger('id_evento')
        .unsigned()
        .notNullable()
        .references('id_evento')
        .inTable('eventos')
      table
        .bigInteger('responsavel')
        .unsigned()
        .notNullable()
        .references('id_administrador')
        .inTable('administradores')
      table.boolean('ativo').defaultTo(true)   
      table.timestamp('ativado_em').defaultTo(this.now())  
      table.timestamp('criado_em').defaultTo(this.now())
      table.timestamp('desativado_em').nullable()
    })
  }

  async down() {
    this.schema.dropTable(this.tableName)
  }
}