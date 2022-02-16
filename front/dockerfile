FROM node:14.14
WORKDIR /app

COPY package.json .

ADD . .
RUN npm install

COPY . .

RUN npm run build

FROM nginx:alpine as production-build
RUN rm -rf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/nginx/default.conf /etc/nginx/conf.d/default.conf

RUN rm -rf /usr/share/nginx/html/*
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]