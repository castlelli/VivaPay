import { BaseSchema } from '@adonisjs/lucid/schema'

export default class extends BaseSchema {
  protected tableName = 'usuarios'

  async up() {
    this.schema.createTable(this.tableName, (table) => {
      table.increments('id_usuario')
      table.string('login', 255).nullable().unique()
      table.string('senha', 255).nullable()
      table.string('nome_usuario', 255).nullable()
      table.timestamp('criado_em').defaultTo(this.now())
    })
  }

  async down() {
    this.schema.dropTable(this.tableName)
  }
}
