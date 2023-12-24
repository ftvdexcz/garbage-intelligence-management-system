# stage 1
FROM node:18.15.0 as build
ENV NODE_OPTIONS=--max-old-space-size=8192
WORKDIR /app
COPY . . 
RUN npm install --force
RUN npm run build

# stage 2
FROM nginx:alpine as prod
ENV TZ="Asia/Ho_Chi_Minh"
COPY --from=build /app/dist /usr/share/nginx/html
COPY default.conf /etc/nginx/conf.d/default.conf
EXPOSE 7777
CMD ["nginx", "-g", "daemon off;"]

