package cl.bancochile.monitor.tx.utils;

import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatosUtils {
    private final static Logger logger = LoggerFactory.getLogger(DatosUtils.class);

    private static String toHexDec(byte[] digest) {
        String hash = "";
        for (byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) {
                hash += "0";
            }
            hash += Integer.toHexString(b);
        }
        return hash;
    }

    public static String codigoUnico() {
        String unico = "";

        try {
            unico = UUID.randomUUID().toString().replaceAll("-", "").trim();
        } catch (Exception e) {
            unico = "";
            logger.error("Error en código Único", e);
        }

        return unico;
    }

    public static String randomStr() {
        String hash = StringUtils.EMPTY;

        try {
            Date ahora = new Date();

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(ahora.toString().getBytes());
            byte[] digest = md.digest();
            String hashMd5 = toHexDec(digest);
            hash = hashMd5.substring(0, 15);
        } catch (Exception e) {
            logger.error("Error al generar Random MD5", e);
        }

        return hash;
    }

    public static String getCodigoRetorno(String codigo) {
        String resultado = "";

        try {
            if (!StringUtils.isEmpty(codigo)) {
                resultado = codigo.toUpperCase().trim();
            } else {
                logger.debug("Código de Retorno NULO");
            }
        } catch (Exception e) {
            resultado = "";
            logger.error("Error en obtencio de codigo de retorno", e);
        }

        return resultado;
    }

    public static int obtenerEnteroStr(String numero) {
        int resultado = 0;
        try {
            resultado = Integer.parseInt(numero);
        } catch (Exception e) {
            resultado = 0;
            logger.error("Error en obtencio de entero", e);
        }
        return resultado;
    }

    public static int obtenerEntero(Integer numero) {
        int resultado = 0;
        try {
            resultado = numero.intValue();
        } catch (Exception e) {
            resultado = 0;
            logger.error("Error en obtencio de entero", e);
        }
        return resultado;
    }

    public static String obtenerString(String texto) {
        String resultado = "";
        try {
            resultado = texto.trim();
        } catch (Exception e) {
            resultado = "";
            logger.error("Error en obtencio de string", e);
        }
        return resultado;
    }

    public static boolean isProductoActivo(String estado) {
        boolean resultado = false;

        try {
            if (!StringUtils.isEmpty(estado)) {
                String descripcion = estado.toUpperCase().trim();
                if (descripcion.contains("VIGENTE") || descripcion.contains("ACTIVO")) {
                    resultado = true;
                }
            }
        } catch (Exception e) {
            logger.error("Error en verificacion de producto activo", e);
            resultado = false;
        }

        return resultado;
    }

    public static Long objecto2long(Object objeto) {
        Long resultado = null;

        try {
            String numero = objeto.toString();
            resultado = Long.parseLong(numero);
        } catch (Exception e) {
            logger.error("Error en parsing de objeto a long", e);
        }

        return resultado;
    }

    public static boolean isDebito(String tbkTipoPago) {
        boolean resultado = false;

        try {
            if (!StringUtils.isEmpty(tbkTipoPago)) {
                if ("VD".equalsIgnoreCase(tbkTipoPago.trim())) {
                    resultado = true;
                }
                logger.debug("TBK Tipo Pago reportado: " + tbkTipoPago);
            } else {
                logger.debug("TBK Tipo de Pago inválido, valor obtenido: " + tbkTipoPago);
            }
        } catch (Exception e) {
            logger.error("Error en verificacion de debito", e);
        }

        return resultado;
    }

    public static String parseoString(String texto, String relleno, int largo, boolean izquierda) {
        String resultado = "";

        try {
            if (StringUtils.isEmpty(texto)) {
                resultado = StringUtils.leftPad("", largo, relleno);
            } else {
                String cadena = StringUtils.substring(texto, 0, largo);

                if (izquierda) {
                    resultado = StringUtils.leftPad(cadena, largo, relleno);
                } else {
                    resultado = StringUtils.rightPad(cadena, largo, relleno);
                }
            }
        } catch (Exception e) {
            logger.error("Error en parsing de string", e);
        }

        return resultado;
    }

    public static String getHostName() {
        String resultado = "";

        try {
            InetAddress address = Inet4Address.getLocalHost();
            resultado = address.getHostName();
        } catch (Exception e) {
            logger.error("Error en obtencion de nombre de host", e);
        }

        return resultado;
    }

    public static String getNumeroTarjetaPCI(String numeroTarjeta) {
        String numero = "";
        try {
            int largo = numeroTarjeta.length();
            String finalTarjeta = numeroTarjeta.substring(largo - 4, largo);
            numero = "XXXX-XXXX-XXXX-" + finalTarjeta;
        } catch (Exception e) {
            numero = "";
            logger.error("Error en obtencion de numero de tarjeta PCI", e);
        }
        return numero;
    }

    public static Long getLong(String valor) {
        Long resultado = null;
        try {
            BigDecimal decimal = new BigDecimal(valor);
            resultado = decimal.longValue();
        } catch (Exception e) {
            logger.error("Error en parsing de bigdecimal", e);
        }
        return resultado;
    }
}
