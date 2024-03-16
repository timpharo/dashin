# Build frontend
FROM node:20-alpine as frontend
WORKDIR /usr/local/app

COPY ./client .

#RUN apt-get -qq update  \
#    && apt-get -y -qq install libgtk2.0-0 libgtk-3-0 libgbm-dev libnotify-dev libgconf-2-4 libnss3 libxss1 libasound2 libxtst6 xauth xvfb
RUN npm install
RUN npm run build

# Build backend
FROM azul/zulu-openjdk-alpine:19-latest as backend
WORKDIR /usr/local/app

COPY ./gradle/wrapper ./gradle/wrapper
COPY ./gradlew .
COPY settings.gradle.kts .
COPY . .

# Copy frontend dist into the backend app
COPY --from=frontend /usr/local/app/dist /usr/local/app/src/main/resources/static

RUN ./gradlew build # --info

#Create final image with jar that also includes frontend
FROM azul/zulu-openjdk-alpine:19-latest
COPY --from=backend /usr/local/app/build/libs/dashin.jar /usr/local/app/app.jar

CMD ["java", "-jar", "/usr/local/app/app.jar"]

