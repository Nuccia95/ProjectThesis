package shared.thesiscommon.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Classe di utilità per la gestione del testo.
 *
 */
public final class TextUtils {
    private static final Charset CHARSET_NAME = StandardCharsets.UTF_16LE; // $NON-NLS-1$

    /**
     * Decodifica un array di byte in una stringa utilizzando la codifica UTF-16.
     *
     * @param bytes
     *            L'array da decodificare.
     *
     * @return la stringa decodificata.
     */
    public static String decode(final byte[] bytes) {
	return new String(bytes, CHARSET_NAME);
    }

    /**
     * Codifica una string in un array di byte utilizzando la codifica UTF-16.
     *
     * @param text
     *            La stringa da codificare.
     *
     * @return l'array di byte codificato.
     */
    public static byte[] encode(final String text) {
	// try {
	return text.getBytes(CHARSET_NAME);
	// } catch (final UnsupportedEncodingException e) {
	// throw new MirageException(MessageFormat.format("Encoding non
	// supportato: ''{0}''", CHARSET_NAME), e);
	// }
    }

    /**
     * Verifica se una stringa è vuota (nulla o di lunghezza 0).
     *
     * @param string
     *            La stringa da testare.
     *
     * @return <code>true</code> se è vuota, <code>false</code> altrimenti.
     */
    public static boolean isEmpty(final String string) {
	return string == null || string.isEmpty();
    }

    private TextUtils() {
    }
}
