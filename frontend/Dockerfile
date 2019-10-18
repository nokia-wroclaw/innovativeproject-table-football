# Stage 0
FROM tiangolo/node-frontend:10 as build-stage
WORKDIR /app
COPY package*.json /app/
RUN npm install
COPY ./ /app/
ARG configuration=production
RUN npm run build -- --output-path=./dist/out --configuration $configuration

# Stage 1
FROM nginx:1.15
COPY --from=build-stage /app/dist/out/ /usr/share/nginx/html
#COPY --from=build-stage /nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build-stage /app/cert/ /etc/nginx/ssl/
COPY --from=build-stage /app/nginx.conf /etc/nginx/conf.d/ngix.conf
