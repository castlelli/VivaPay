import type { HttpContext } from '@adonisjs/core/http'
import type { NextFn } from '@adonisjs/core/types/http'
import type { Authenticators } from '@adonisjs/auth/types'
import { Exception } from '@adonisjs/core/exceptions'


/**
 * Auth middleware is used authenticate HTTP requests and deny
 * access to unauthenticated users.
 */

export default class AuthMiddleware {
  /**
   * The URL to redirect to, when authentication fails
   */
  redirectTo = '/login'

  async handle(
    ctx: HttpContext,
    next: NextFn,
    options: {
      guards?: (keyof Authenticators)[]
      perfil?: string
    } = {},
  ) {
    let a = await ctx.auth.authenticateUsing(options.guards, { loginRoute: this.redirectTo })
    switch(options.perfil){
      case 'cliente':
        await a.load('cliente')
        if(!a.cliente)
          throw new Exception('Aborting request')
        break
      case 'admin':
        await a.load('administrador')
        await a.load('vendedor')
        if(!a.administrador)
          throw new Exception('Aborting request')
        break
      case 'vendedor':
        await a.load('vendedor')
        if(!a.vendedor)
          throw new Exception('Aborting request')
        break
    }
    await next()
  }
}