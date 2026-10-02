import { BaseSchema } from '@adonisjs/lucid/schema'

export default class extends BaseSchema {
  protected tableName = 'administradores'

  async up() {
    this.schema.createTable(this.tableName, (table) => {
      table.increments('id_administrador')
      table
        .bigInteger('id_usuario')
        .unsigned()
        .notNullable()
        .references('id_usuario')
        .inTable('usuarios')
      table
        .bigInteger('id_instituicao')
        .unsigned()
        .notNullable()
        .references('id_instituicao')
        .inTable('instituicoes')
      table.timestamp('criado_em').defaultTo(this.now())
      table.timestamp('cancelado_em')
    })
  }

  async down() {
    this.schema.dropTable(this.tableName)
  }
}