# Replicación de sesiones en REDIS para TOMCAT 9

Autor: Roshka S.A.

Fecha de creación: 2023-02-06

Última revisión: 2023-02-14

## Pre requisitos: TOMCAT 9

Asumiendo que el TOMCAT reside en el `${catalina.base}` entonces, se necesitan los siguientes cambios:

### Librerías

1. Copiar al directorio `${catalina.base}/libs` las siguientes librerías:

* [redisson-all-3.16.1.jar](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=org.redisson&a=redisson-all&v=3.16.1&e=jar)
* [edisson-tomcat-9-3.16.1.jar](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=org.redisson&a=redisson-tomcat-9&v=3.16.1&e=jar)

En [este URL](https://redisson.org/articles/redis-based-tomcat-session-management.html) se peude chequear si hay versiones nuevas de estas librerías. Al momento de la escritura de este documento, estas mencionadas acá son las versiones actuales.

### Archivos de configuración

1. En el archivo `${catalina.base}/conf/context.xml` agregar el siguiente TAG:

```xml
<Manager className="org.redisson.tomcat.RedissonSessionManager"
        configPath="${catalina.base}/conf/redisson.conf"
        readMode="MEMORY"
        updateMode="AFTER_REQUEST"
    />
```

2. Crear el archivo `${catalina.base}/conf/redisson.conf` con el siguiente contenido:

```json
{
  "singleServerConfig": {
    "idleConnectionTimeout": 10000,
    "connectTimeout": 10000,
    "timeout": 3000,
    "retryAttempts": 3,
    "retryInterval": 1500,
    "password": null,
    "subscriptionsPerConnection": 5,
    "clientName": null,
    "address": "redis://127.0.0.1:6379",
    "subscriptionConnectionMinimumIdleSize": 1,
    "subscriptionConnectionPoolSize": 50,
    "connectionMinimumIdleSize": 10,
    "connectionPoolSize": 64,
    "database": 0,
    "dnsMonitoringInterval": 5000
  },
  "threads": 0,
  "nettyThreads": 0,
  "codec": null
}
```

Este contenido tiene que ser ajustado a la configuración correspondiente del servidor/cluster de REDIS.

## Pre requisitos: PROYECTO

Las sesiones se usan normalmente, solamente que si se van a guardar objetos de java, estos (así como sus propiedades) tienen que implementar la interfaz `java.io.Serializable`

Ejemplo:

```java
import java.io.Serializable;

public class SessionObject implements Serializable  {

    private String status;
    private int counter;

    private SessionObjectChild sessionObjectChild; // esta clase también tiene que implementar java.io.Serializable

    public SessionObject()
    {
        sessionObjectChild = new SessionObjectChild();
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public SessionObjectChild getSessionObjectChild() {
        return sessionObjectChild;
    }

    public void setSessionObjectChild(SessionObjectChild sessionObjectChild) {
        this.sessionObjectChild = sessionObjectChild;
    }
}
```

## Ejemplo

Este proyecto se puede compilar y correr para probar las sesiones compartidas con un balanceador de carga. Para eso, llevar adelante los siguientes pasos:

1. Configurar y poner en funcionamiento un servidor REDIS
2. Configurar dos servidores TOMCAT (pueden ser en el mismo SERVIDOR) copiando las librerías y configuraciones descritas en este documento, apuntando al servidor REDIS descrito en el paso 1
3. Configurar un balanceador de carga con NGINX como se puede ver en el archivo `docker/nginx.conf`

Acá es importante que en esta sección:

```
    # List of application servers
    upstream apiservers {
        server 192.168.67.20:7081;
        server 192.168.67.20:7082;
    }
```

Se pongan las combinaciones de IP+PUERTOS de los servidores TOMCAT configurados en el paso 2

4. Generar el WAR con el script gradle y renombrar a `test-tomcat-redis-session.war`
5. Copiar el WAR al directorio `webapps` del `${catalina.base}` de cada servidor tomcat configurado en el paso 2
6. Apuntar un navegador al url `http://192.168.67.20:5100/test-tomcat-redis-session/test`

Acá se va a balancear la carga entre los dos servidores, y las sesiones van a ser correctamente compartidas entre ambos.
