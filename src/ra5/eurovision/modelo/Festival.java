package ra5.eurovision.modelo;

import com.sun.media.jfxmediaimpl.HostUtils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Un objeto de esta clase guarda en un map (festival) las puntuaciones obtenidas
 * en el festival de Eurovisión por una serie de países
 * Las puntuaciones se leen de un fichero de texto (puntuaciones.txt)
 *
 * El map festival asocia nombre del país con la puntuación total obtenida
 * Importa el orden de las claves
 */
public class Festival {

    private static final String SALIDA = "resultados.txt";
    private Map<String, Integer> festival;

    /**
     * Constructor de la clase FestivalEurovision
     */
    public Festival() {
       festival = new TreeMap();

    }

    /**
     * Añade al map los puntos dados a un país
     * El nombre del país siempre se añade en mayúsculas
     * Si el país no existe se crea una nueva entrada en el map,
     * si existe el país se añaden los puntos
     */
    public void addPuntos(String pais, int puntos) {

        pais = pais.toUpperCase();
        if (festival.containsKey(pais)){
            int puntosActuales = festival.get(pais);
            festival.put(pais, puntosActuales + puntos);
        }
        else {
            festival.put(pais, puntos);
        }
    }

    /**
     * Lee del fichero de texto ENTRADA las puntuaciones dadas
     * a los países
     * Se capturarán todas las posibles excepciones
     * Si al leer el fichero hay alguna línea errónea se
     * contabiliza pero se sigue leyendo
     * Se devuelve el nº de líneas incorrectas
     * Se hará uso del método tratarLinea()
     *
     * Usar try-with-resources
     */
    public int leerPuntuaciones(String nombre) {
        File f = new File(nombre);
        int errores = 0;
        try (BufferedReader entrada = new BufferedReader(new FileReader(f))) {
            String linea = entrada.readLine();
            while (linea != null){
                try {
                    tratarLinea(linea);
                } catch (IllegalArgumentException e) {
                    errores++;
                }
                linea = entrada.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("El fichero no existe o no se ha encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error en la lectura del fichero: " + e.getMessage());
        }
        return errores;
    }


    /**
     * A partir de una línea extrae los puntos dados a cada uno de los países indicados
     * y añade al map esas puntuaciones
     *
     * El formato de la línea es
     * pais:puntos:pais:puntos:pais:puntos:pais:puntos
     *
     * Se propagan las posibles excepciones
     */
    private void tratarLinea(String linea) throws NumberFormatException, IllegalArgumentException {
        String[] trozos = linea.split(":");
        String nomPais;
        int puntosPais;
        for (int i = 0; i < trozos.length - 1; i+=2) {
            addPuntos(trozos[i].trim(), Integer.parseInt(trozos[i+1].trim()));
        }
    }


    /**
     * Dado un país devuelve su puntuación
     * Si el país no existe se lanza la excepción personalizada
     * PaisExcepcion con el mensaje "País XXX no existe"
     *
     * Se propagan las posibles excepciones
     */
    public int puntuacionDe(String pais) throws PaisExcepcion {
        int puntuacion = 0;
        if (festival.containsKey(pais.toUpperCase())){
            puntuacion = festival.get(pais);
        } else {
            throw new PaisExcepcion("El país " + pais + " no existe");
        }
        return puntuacion;
    }

    /**
     * Devuelve el nombre del pais ganador
     * (el primero encontrado)
     */
    public String ganador() {
        int max = 0;
        String nomGanador = "";
        for (String pais : festival.keySet()) {
            if (festival.get(pais) > max)
                max = festival.get(pais);
                nomGanador = pais;
        }
        return nomGanador;
    }

    /**
     * Guarda en el fichero SALIDA el nombre de cada país y la puntuación
     * total obtenida
     * Se propagan todas las excepciones
     * Usar try-with-resources
     */
    public void guardarResultados() throws IOException {
        File f = new File(SALIDA);
        try (PrintWriter salida = new PrintWriter(new BufferedWriter(new FileWriter(f)))) {
            for (String pais : festival.keySet()) {
                salida.println(pais + " --> " + festival.get(pais) + " puntos");
            }
        }
    }

}
