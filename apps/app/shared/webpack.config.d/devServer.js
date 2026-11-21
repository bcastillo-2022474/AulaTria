// The browser build uses real paths (/courses, /lessons/<id>, ...): the dev server must answer index.html for them, so reload and
// deep links work. Production hosts need the same fallback (see apps/app/README.md).
// AULA_DEV_PORT overrides the port ONLY when set (the default stays 8080).
config.devServer = Object.assign(config.devServer || {}, { historyApiFallback: true });
if (process.env.AULA_DEV_PORT) config.devServer.port = Number(process.env.AULA_DEV_PORT);
