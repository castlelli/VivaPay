import { BaseSchema } from '@adonisjs/lucid/schema'

export default class extends BaseSchema {
  protected tableName = 'vendedores'

  async up() {
    this.schema.createTable(this.tableName, (table) => {
      table.increments('id_vendedor')
      table.string('loja', 255).notNullable()
      table
        .bigInteger('id_usuario')
        .unsigned()
        .notNullable()
        .references('id_usuario')
        .inTable('usuarios')
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
