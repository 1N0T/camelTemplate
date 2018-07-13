![logo](https://raw.github.com/1N0T/images/master/global/1N0T.png)

# camelTemplate
Plantilla mínima camel con Main y recepción de parámetros. Este tipo de contexto, permanece en ejecución hasta que se cancela la **JVM**

Para ejecutar:

```bash
mvn compile exec:java -Dexec.mainClass=ToniS.ProcesoCamel -Dexec.args="mi_argumento"
```

Para empaquetar y ejecutar, podemos optar por alguna de las siguientes opciones:

```bash
java -jar ./target/plantillaCamel.jar "Mi parametro"
java -cp ./target/plantillaCamel.jar ToniS.ProcesoCamel "Mi parametro"

```

