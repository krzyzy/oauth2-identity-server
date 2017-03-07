FROM registry.accuratus-software.pl:5000/accuratus/java:8jre


RUN mkdir -p /opt/oauth-identity-server
ADD oauth-identity-server /opt/oauth-identity-server/
RUN unzip /opt/oauth-identity-server/account-ui.jar

WORKDIR /opt/oauth-identity-server

CMD ["java", "-jar", "./oauth-identity-server.jar", "com.solidify.oauth2.IdentityServer", "--spring.profiles.active=INT" ]