// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package csvservices.actions;

import com.mendix.core.Core;
import com.mendix.logging.ILogNode;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;
import csvservices.impl.CsvImporter;
import csvservices.impl.CsvLoader;
import csvservices.impl.CsvServicesImpl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ImportCsvUrlDataToLoaderRow extends CustomJavaAction<java.lang.Long> {
    private final java.lang.String Entity;
    private final java.lang.String CsvUrl;
    private final java.lang.Long MaxRecords;
    private final java.lang.Boolean HasHeader;
    private final java.lang.String AlternativeHeader;
    private final java.lang.String Delimiter;
    private final java.lang.Boolean ConvertToJson;

    public ImportCsvUrlDataToLoaderRow(IContext context, java.lang.String Entity, java.lang.String CsvUrl, java.lang.Long MaxRecords, java.lang.Boolean HasHeader, java.lang.String AlternativeHeader, java.lang.String Delimiter, java.lang.Boolean ConvertToJson) {
        super(context);
        this.Entity = Entity;
        this.CsvUrl = CsvUrl;
        this.MaxRecords = MaxRecords;
        this.HasHeader = HasHeader;
        this.AlternativeHeader = AlternativeHeader;
        this.Delimiter = Delimiter;
        this.ConvertToJson = ConvertToJson;
    }

    @java.lang.Override
    public java.lang.Long executeAction() throws Exception {
        // BEGIN USER CODE
        logger.info("executeAction - toLoaderTable: " + this.Entity + ", " + Arrays.toString(this.Entity.split("\\.")));
        Long objectsCreated = 0L;
        CsvImporter csvImporter = new CsvImporter();
        String moduleName = this.Entity.split("\\.")[0];
        String entityName = this.Entity.split("\\.")[1];
        int maxRecords = (this.MaxRecords == null) ? -1 : this.MaxRecords.intValue();
        URL csvUrl = new URL(this.CsvUrl);
        InputStream is = null;
        try {
            if (this.CsvUrl.endsWith(".gz") || this.CsvUrl.endsWith(".gzip")) {
                is = new GZIPInputStream(csvUrl.openStream());
            } else if (this.CsvUrl.contains(".zip#")) {
                // reading specific file from zip
                String fileName = this.CsvUrl.substring(this.CsvUrl.indexOf(".zip#") + 5);
                ZipInputStream zis = new ZipInputStream(csvUrl.openStream());
                ZipEntry ze = zis.getNextEntry();
                while (ze != null) {
                    String name = ze.getName();
                    System.out.printf("entry: %s%n", name);
                    if (name.equals(fileName)) {
                        System.out.println("found entry");
                        is = zis;
                        break;
                    }
                    ze = zis.getNextEntry();
                }
                if (is == null) {
                    throw new FileNotFoundException(String.format("Zipfile not found: %s in %s", fileName, csvUrl));
                }
            } else {
                is = csvUrl.openStream();
            }
            try (StringWriter outputWriter = new StringWriter()) {
                // create loader table
                CsvLoader csvLoader = new CsvLoader();
                csvLoader.loadCsvFile(this.Entity, is, this.getContext());

                logger.info("Done importing: " + outputWriter);
            }
        } catch (Exception e) {
            logger.warn("csv import failed: " + e.getMessage());
            logger.error(e);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return objectsCreated;
        // END USER CODE
    }

    /**
     * Returns a string representation of this action
     *
     * @return a string representation of this action
     */
    @java.lang.Override
    public java.lang.String toString() {
        return "ImportCsvUrlDataToLoaderRow";
    }

    // BEGIN EXTRA CODE
    private static final ILogNode logger = Core.getLogger(CsvServicesImpl.LOG_NORE);

    // END EXTRA CODE
}
