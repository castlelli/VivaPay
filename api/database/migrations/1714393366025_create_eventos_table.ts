import { BaseSchema } from '@adonisjs/lucid/schema'


export default class extends BaseSchema {
  protected tableName = 'eventos'

  async up() {
    this.schema.createTable(this.tableName, (table) => {
      table.increments('id_evento')
      table
        .bigInteger('id_instituicao')
        .unsigned()
        .notNullable()
        .references('id_instituicao')
        .inTable('instituicoes')
      table
        .bigInteger('responsavel')
        .unsigned()
        .notNullable()
        .references('id_administrador')
        .inTable('administradores')
      table.boolean('ativo').defaultTo(true)
      table.string('nome_evento', 255).notNullable()
      table.string('abreviacao_evento', 255)
      table.text('descricao_evento').nullable()
      table.timestamp('hora_inicio').notNullable()
      table.timestamp('hora_final').notNullable()
      table.timestamp('finalizado_em').nullable()
      table.timestamp('desativado_em').nullable()
      table.timestamp('criado_em').defaultTo(this.now())
    })
  }

  async down() {
    this.schema.dropTable(this.tableName)
  }
}