package csvservices.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Opens a csv file from an http(s) url, optionally gzipped or as an entry of a zip file (url.zip#entry).
 */
public class CsvUrlSource {
    private static final int CONNECT_TIMEOUT_MS = 30_000;
    private static final int READ_TIMEOUT_MS = 300_000;

    public static InputStream open(String csvUrl) throws IOException {
        URI uri;
        try {
            uri = new URI(csvUrl);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid csv url: " + csvUrl, e);
        }
        // only http(s): file:, jar: and other schemes would read files from the server itself
        String scheme = uri.getScheme() == null ? "" : uri.getScheme().toLowerCase();
        if (!scheme.equals("http") && !scheme.equals("https")) {
            throw new IllegalArgumentException("Only http and https csv urls are supported: " + csvUrl);
        }
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
        connection.setReadTimeout(READ_TIMEOUT_MS);
        int status = connection.getResponseCode();
        if (status < 200 || status >= 300) {
            connection.disconnect();
            throw new IOException(String.format("Downloading %s failed with http status %d", csvUrl, status));
        }
        InputStream is = connection.getInputStream();
        if (csvUrl.endsWith(".gz") || csvUrl.endsWith(".gzip")) {
            return new GZIPInputStream(is);
        }
        if (csvUrl.contains(".zip#")) {
            String fileName = csvUrl.substring(csvUrl.indexOf(".zip#") + 5);
            ZipInputStream zis = new ZipInputStream(is);
            for (ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()) {
                if (ze.getName().equals(fileName)) {
                    return zis;
                }
            }
            zis.close();
            throw new FileNotFoundException(String.format("File %s not found in %s", fileName, csvUrl));
        }
        return is;
    }
}
