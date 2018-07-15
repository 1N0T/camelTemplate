// mvn compile exec:java -Dexec.mainClass=ToniS.ProcesoCamel -Dexec.args="Mi parámetro"

package ToniS;

import ToniS.*;

import org.apache.camel.*;
import org.apache.camel.main.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;


public class ProcesoCamel {
 
    private org.apache.camel.main.Main instanciaCamel;
    public static void main(String[] args) throws Exception {
        String miArgumento = args[0];	
    	ProcesoCamel proceso = new ProcesoCamel();
        proceso.inicia(miArgumento);
    }
 
    public void inicia(String miArgumento) 
                       throws Exception {
		
        // Creamos instancia de Main (permanece viva hasta se cancela la JVM)
        instanciaCamel = new org.apache.camel.main.Main();

        // Añadimos rutas
       	instanciaCamel.addRouteBuilder(new MiRouteBuilder(miArgumento));

        // Añadimos listener de eventos
        instanciaCamel.addMainListener(new Events());
 
        // Se ejecuta hasta que termine la JVM
        System.out.println("=====================================================");
        System.out.println("Iniciando Camel. Usa ctrl + c para terminar la JVM.  ");
        System.out.println("=====================================================");
        instanciaCamel.run();
    }
 
    private static class MiRouteBuilder extends RouteBuilder {
        // Extendemos la clase RouteBuilder para recibir parámetros, por lo que
        // tenemos que definir un constructor que coincida con la firma
		private final String miPropiedad;

        // Este constructor no es necesario si no necesitamos pasarle parámetros
		private MiRouteBuilder(String miArgumento) {
		    super();
		    this.miPropiedad = miArgumento;
		}        

        @Override
        public void configure() throws Exception {

            System.out.println("He recibido el parámetro: " + this.miPropiedad);
            System.out.println("==================================================================================");

            from("timer://temporizador?period=100")
            .process(new Processor() {
                public void process(Exchange msg) throws Exception {
                    System.out.println("Procesando la propiedad:" + msg.getContext().resolvePropertyPlaceholders("{{mi.propiedad}}"));
                }
            })
            .transform().simple("Esta es mi.propiedad ${properties:mi.propiedad}")
            .to("log:ToniS.ProcesoCamel?level=INFO");
        }
    }


	// No utilizamos los eventos pero lo dejamos a modo de plantilla por si en un futuro se necesitara.
    public static class Events extends MainListenerSupport {
 
        @Override
        public void afterStart(MainSupport main) {

            System.out.println("=====================");
            System.out.println("Camel se ha iniciado!");
            System.out.println("=====================");

            // Recuperamos el contexto de main para modificar configuración
            CamelContext ctx = main.getCamelContexts().get(0); 

            // Es obligatorio registrarlo en el contexto con el nombre properties
            PropertiesComponent pc = new PropertiesComponent();
            pc.setLocation("classpath:/misPropiedades.properties");
            ctx.addComponent("properties", pc);

        }
 
        @Override
        public void beforeStop(MainSupport main) {
            System.out.println("====================");
            System.out.println("Camel se va a parar!");
            System.out.println("====================");
        }
    }


}


