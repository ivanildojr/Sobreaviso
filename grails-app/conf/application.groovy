// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'sobreavisonutel.seguranca.Usuario'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'sobreavisonutel.seguranca.UsuarioPerfil'
grails.plugin.springsecurity.authority.className = 'sobreavisonutel.seguranca.Perfil'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/escala/agenda',  access: ['ROLE_ADMIN']],
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['ROLE_ADMIN']],
	[pattern: '/index.gsp',      access: ['ROLE_ADMIN']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]
grails.plugin.springsecurity.logout.postOnly = false