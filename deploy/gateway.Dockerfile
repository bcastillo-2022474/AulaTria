# The gateway image. The CI assembles the build context (see .github/workflows/deploy.yml and `mage deploy:bundle`):
#   gateway/Dockerfile  gateway/Caddyfile  gateway/teacher/  gateway/student/
FROM caddy:2-alpine
COPY Caddyfile /etc/caddy/Caddyfile
COPY teacher /srv/teacher
COPY student /srv/student
