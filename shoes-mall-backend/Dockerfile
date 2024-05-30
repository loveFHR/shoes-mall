FROM docker.io/openjdk:17
ENV active=dev
WORKDIR /home
COPY ./target/shoes-mall-backend-0.0.1-SNAPSHOT.jar /home/app.jar


EXPOSE 2913
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
ENTRYPOINT ["java","--add-opens", "java.base/java.lang=ALL-UNNAMED","--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED","--add-opens", "java.base/java.util=ALL-UNNAMED"]
CMD ["-jar","-Dspring.profiles.active=${active}", "app.jar"]
