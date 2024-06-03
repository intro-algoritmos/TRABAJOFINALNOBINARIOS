
/**
 * Clase CodificadorMensajes: representa una componente capaz de cifrar
 * un mensaje en formato texto. El mensaje a cifrar debe ser un objeto
 * de tipo Mensaje (básicamente, una lista de strings, donde cada string
 * representa una línea). Se asume que el mensaje es ASCII, es decir, todos
 * los caracteres del mensaje tienen códigos en el rango [0, 127].
 * Si el mensaje a codificar tiene al menos una línea, el
 * mecanismo de codificación/cifrado calcula un código de cifrado a partir
 * de la misma. Caso contrario, el código de cifrado es irrelevante.
 * 
 * La codificación utiliza una variante de Cifrado Cesar, en el cual el 
 * desplazamiento se basa en una código de encripción múltiple. Véase
 * Cifrado de Vigenère para más detalles.
 * 
 * @author N. Aguirre
 * @version 0.1
 */
public class CodificadorMensajes
{
    /**
     * Mensaje a codificar
     */
    private Mensaje mensajeACodificar;
    
    /**
     * Mensaje codificado
     */
    private Mensaje mensajeCodificado;
    
    /**
     * Codigo para encripcion
     */
    private int[] codigoEncripcion;
    
    /**
     * Constructor de la clase CodificadorMensajes.
     * Inicializa el mensaje a encriptar/codificar con el parámetro pasado, e
     * inicializa el mensaje codificado y el codigo de encripción en null.
     * Precondición: el mensaje msg no puede ser nulo
     * @param msg es el mensaje a encriptar.
     */
    public CodificadorMensajes(Mensaje msg)
    {
        if (msg == null) {
            throw new IllegalArgumentException("Mensaje es nulo");
        }
        mensajeACodificar = msg;
        mensajeCodificado = null;
        codigoEncripcion = null;
    }

    /**
     * Encripta el mensaje. El mensaje no debe estar encriptado.
     * Precondición: El mensaje aún no fue cifrado (i.e., el campo mensajeCodificado es null).
     */
    public void codificarMensaje() 
    {
        if (mensajeCodificado != null) {
            // mensaje ya codificado
            throw new IllegalStateException("El mensaje ya está codificado");
        }
        if (mensajeACodificar.cantLineas() == 0) {
            mensajeCodificado = new Mensaje();
            codigoEncripcion = new int[0];
        }
        else {
            mensajeCodificado = new Mensaje();
            codigoEncripcion = generarCodigoEncripcion(mensajeACodificar.obtenerLinea(0));
            for (int i = 0; i < mensajeACodificar.cantLineas(); i++) {
                String curr = mensajeACodificar.obtenerLinea(i);
                String currCodificada = encriptarCadena(curr, codigoEncripcion);
                mensajeCodificado.agregarLinea(currCodificada);
            }
        }
    }
    
    /**
     * Cambia el mensaje a codificar.
     * Precondición: el nuevo mensaje no puede ser null.
     * Postcondición: el mensaje a codificar se actualiza, y se vuelve el objeto
     * a un estado de "aún no codificado".
     * @param msg es el mensaje a codificar.
     */
    public void cambiarMensaje(Mensaje msg)
    {
        if (msg == null)
            throw new IllegalArgumentException("Mensaje es nulo");
        mensajeACodificar = msg;
        mensajeCodificado = null;
        codigoEncripcion = null;
    }
    
    /**
     * Retorna el mensaje ya codificado/cifrado.
     * Precondición: el mensaje debe haber sido codificado previamente (i.e., se debe haber llamado a codificarMensaje()).
     * Postcondicion: se retorna el mensaje cifrado/codificado.
     * @return el mensaje cifrado.
     */
    public Mensaje obtenerMensajeCodificado() {
        if (mensajeCodificado == null)
            throw new IllegalStateException("Mensaje aún no codificado");
        return mensajeCodificado;
    }
    
    /**
     * Retorna el código de cifrado.
     * Precondición: el mensaje debe haber sido codificado previamente (i.e., se debe haber llamado a codificarMensaje()).
     * Postcondicion: se retorna el código obtenido para el cifrado.
     * @return el código de cifrado.
     */
    public int[] obtenerCodigoEncripcion() {
        if (mensajeCodificado == null)
            throw new IllegalStateException("Mensaje aún no codificado");
        return codigoEncripcion;
    }
    
    /**
     * Computa el código de encripción correspondiente a una cadena str.
     * Para calcular el código de encripción se suman los códigos ASCII de str, 
     * y se divide por 99991 (el número primo de 5 dígitos más grande). Los 
     * dígitos del resto de la división constituyen el código de encripción.
     * Ej: para la cadena "hola", los códigos ascii de los caracteres son
     * 104, 111, 108 y 97, respectivamente. Su suma es 420, y 420 % 99991 es
     * 420. Luego, el código de inscripción es el arreglo {4, 2, 0}.
     */
    private int[] generarCodigoEncripcion(String str) 
    {
        if(str == null) 
        //la cadena no tiene que ser NULL
        throw new IllegalArgumentException("la cadena esta vacia"); 
        int num = 0;
        int resul = 0;
        int suma = 0;
        
        //el for saca las letras dependiendo las posiciones de la palabra para poder guardarlas y luego sumarlas 
            for(int i = 0; i<str.length() ;i++)
            {
                char letra = str.charAt(i);
                num = (int) letra;
                suma = suma + num;
                num = 0;
             }
        
        // aqui la suma la divido por el numero para sacar el resto y luego creo variables locales para el siguiente for
        resul = suma%99991;
        int div = 0;
        // la cadena "cant" funciona para pasar el resultado de la suma a String para poder sacar asi la cantidad de letras que hay
        // para poder guardarlo en el arreglo
        String cant = resul + "";
        int codigofinal [] = new int [cant.length()];
        // en este for hice que comience desde la posicion de la cantidad de numeros que tiene cant para poder 
        // iniciar desde ahi hasta 0, le reste uno ya que el ArrayList comienza desde la posicion 0
            for(int i = cant.length()-1 ; i>=0 ; i--)
            {
                //el "div" funciona para dividir el numero y guardar el resto
                div = resul % 10;
                //el "resul" se divide en 10 para poder asi pasar al siguiente numero y guardar solo el resto
                resul = resul/10;
                //el resto se guarda en la posicion donde comienza i, lo hice de abajo asi arriba porque si no guardaba de 
                // arriba hacia abajo
                codigofinal[i]=div;
            } 
        return codigofinal;
    }
    
    /**
     * Encripta una cadena, dado un código numérico. Se usan los dígitos del código
     * para reemplazar cada caracter de la cadena por el caracter correspondiente a 
     * "trasladar" el mismo el número de lugares que indica el código. El código tiene
     * múltiples valores: se usa el primero para el primer caracter, el segundo para el segundo,
     * y así sucesivamente. Si se agota el código, se vuelve al comienzo del mismo, hasta
     * encriptar toda la cadena.
     * Precondición: tanto str como codigo no deben ser nulos.
     * @param str es la cadena a encriptar
     * @param codigo es el código a utilizar para la encripción
     */
    private String encriptarCadena(String str, int[] codigo) {
        if (str == null) throw new IllegalArgumentException("Cadena nula");
        if (codigo == null) throw new IllegalArgumentException("Código inválido");
        String result = "";
        int indiceCodigo = 0;
        for (int i = 0; i < str.length(); i++) {
            char curr = str.charAt(i);
            char currEncriptado = (char) ((curr + codigo[indiceCodigo]) % 128);
            result = result + currEncriptado;
            indiceCodigo = (indiceCodigo + 1) % (codigo.length);
        }
        return result;
    }
    
}
