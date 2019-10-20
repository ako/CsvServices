package csvservices.impl;

import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.logging.ILogNode;
import com.mendix.systemwideinterfaces.MendixRuntimeException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixIdentifier;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.systemwideinterfaces.core.UserException;
import com.mendix.systemwideinterfaces.core.meta.IMetaAssociation;
import com.mendix.systemwideinterfaces.core.meta.IMetaPrimitive;
import com.mendix.thirdparty.org.json.JSONArray;
import com.mendix.thirdparty.org.json.JSONObject;

import java.io.*;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ako on 2/10/2015.
 */
public class CsvImporter {
    private static ILogNode logger = Core.getLogger(CsvServicesImpl.LOG_NORE);
    public static final String UTF8_BOM = "\uFEFF";

    private boolean containsUnfinishedString(String s) {
        String s2 = new String(s);
        boolean unfinishedString = (s2.replaceAll("[^\"]", "").length() % 2) == 1;
        logger.debug(String.format("containsUnfinishedString: %b, for %s", unfinishedString, s));
        return unfinishedString;
    }

    /*
     * Create new entities for uploaded stuff
     */
    public int csvToEntities(IContext context, Writer writer, String moduleName, String entityName, InputStream inputStream, Boolean strict, int maxRecords, Boolean hasHeader, String alternativeHeader) throws IOException {
        IContext ctx = null;
        logger.info(String.format("csvToEntities: %s.%s", moduleName, entityName));

        String csvSplitter = "(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        int lineNo = 0;
        int objNo = 0;
        String[] attributeNames = null;
        String[] attributeFormats = null;
        Format[] attributeFormatters = null;
        Boolean[] attributeIsPK = null;
        Boolean[] attributeIsFKS = null;
        String[] attributeFKSSeparator = null;
        Boolean entityHasPk = false;
        JSONArray errorMessages = new JSONArray();
        String nextLine = null;

        context.endTransaction();
        ctx = context.createClone();
        ctx.startTransaction();
        while ((nextLine = bufferedReader.readLine()) != null) {
            line += nextLine;
            logger.debug("Processing line " + line);
            if (maxRecords != -1 && objNo > maxRecords) {
                break;
            }
            if (lineNo == 0 && line.startsWith(UTF8_BOM)) {
                line = line.substring(1);
            }
            if (line.startsWith("#") || line.length() == 0) {
                continue;
            }
            if (lineNo == 0) {
                // Header line
                String headerLine = null;
                if (hasHeader) {
                    headerLine = line;
                }
                if (alternativeHeader != null) {
                    headerLine = alternativeHeader;
                }
                logger.info("Using headerline: " + headerLine);
                // Determine what delimiter is used
                if (headerLine.contains("\t")) {
                    csvSplitter = "\t" + csvSplitter;
                    logger.info("Using tabs as field separator");
                } else if (headerLine.contains(";")) {
                    csvSplitter = ";" + csvSplitter;
                    logger.info("Using ; as field separator");
                } else {
                    csvSplitter = "," + csvSplitter;
                    logger.info("Using , as field separator");
                }
                // Determine attribute names
                attributeNames = headerLine.split(csvSplitter);
                attributeFormats = new String[attributeNames.length];
                attributeFormatters = new Format[attributeNames.length];
                attributeIsPK = new Boolean[attributeNames.length];
                attributeIsFKS = new Boolean[attributeNames.length];
                attributeFKSSeparator = new String[attributeNames.length];
                for (int i = 0; i < attributeNames.length; i++) {
                    attributeNames[i] = attributeNames[i].trim();
                    // check if attribute is part of primary key
                    attributeIsPK[i] = attributeNames[i].endsWith("*");
                    if (attributeIsPK[i]) {
                        entityHasPk = true;
                        attributeNames[i] = attributeNames[i].replace("*", "");
                    }
                    // check if attribute is set of foreign keys
                    attributeFKSSeparator[i] = ";";
                    attributeIsFKS[i] = attributeNames[i].startsWith("[");
                    if (attributeIsFKS[i]) {
                        attributeFKSSeparator[i] = attributeNames[i].substring(attributeNames[i].length() - 2, attributeNames[i].length() - 1);
                        attributeNames[i] = attributeNames[i].substring(1, attributeNames[i].length() - 2);
                    } else {
                        // in case it's an implicit set (as specified by the data)
                        attributeFKSSeparator[i] = ";";
                    }
                    logger.debug(String.format("Is key set: %s. %b, separator: %s", attributeNames[i], attributeIsFKS[i], attributeFKSSeparator[i]));
                    // remove double quotes at start and end
                    attributeNames[i] = attributeNames[i].replaceAll("^\"|\"$", "");
                    // check if has formatting specified
                    if (attributeNames[i].indexOf("(") >= 0) {
                        // format included in braces
                        attributeFormats[i] = attributeNames[i].substring(attributeNames[i].indexOf("(") + 1, attributeNames[i].length() - 1);
                        attributeNames[i] = attributeNames[i].substring(0, attributeNames[i].indexOf("("));
                        attributeFormatters[i] = new SimpleDateFormat(attributeFormats[i]);
                    }
                    logger.debug("attribute: " + attributeNames[i] + ", format: " + attributeFormats[i]);
                }
                if (hasHeader) {
                    logger.debug("skipping first line for processing, was header line");
                    lineNo++;
                    line = "";
                    continue;
                }
            }

            if (containsUnfinishedString(line)) {
                // add next line to current line
                lineNo++;
                continue;
            }
            IMendixObject object = null;
            try {
                String[] values = line.split(csvSplitter);
                String objectConstraint = "";
                if (entityHasPk) {
                    // test if object already exists, get object

                    for (int i = 0; i < attributeNames.length; i++) {
                        if (attributeIsPK[i]) {
                            IMetaPrimitive metaPrimitive = Core.getMetaObject(moduleName + "." + entityName).getMetaPrimitive(attributeNames[i]);
                            IMetaPrimitive.PrimitiveType type = metaPrimitive.getType();
                            if (type.equals(IMetaPrimitive.PrimitiveType.String)
                                    || type.equals(IMetaPrimitive.PrimitiveType.HashString)
                            ) {
                                objectConstraint += "(" + attributeNames[i] + " = '" + values[i] + "') and ";
                            } else if (type.equals(IMetaPrimitive.PrimitiveType.DateTime)) {
                                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                                String internalDateString = dateTimeFormat.format(toDateValue(values[i], attributeFormatters[i]));
                                //pkVals.add(internalDateString);
                                objectConstraint += "(" + attributeNames[i] + " = '" + internalDateString + "') and ";
                            } else {
                                objectConstraint += "(" + attributeNames[i] + " = " + values[i] + ") and ";
                            }
                        }
                    }
                    String findByPkXpath = "//" + moduleName + "." + entityName + "[" + objectConstraint + " (1 = 1)]";
                    logger.debug("find by pk constraint: " + findByPkXpath);
                    try {
                        // TODO replace by createXpathQuery
                        List<IMendixObject> objects = Core.retrieveXPathQuery(ctx, findByPkXpath);
                        if (objects.size() > 0) {
                            object = objects.get(0);
                        }
                    } catch (Exception e) {
                        logger.info(e);
                        logger.warn("Failed to retrieve object by pk");
                    }
                }
                if (object == null) {
                    try {
                        object = Core.instantiate(ctx, moduleName + "." + entityName);
                    } catch (Exception e) {
                        logger.info(e);
                        logger.warn("Failed to create a new object " + moduleName + "." + entityName + " due to: " + e.getMessage());
                        errorMessages.put("Failed to create a new object " + moduleName + "." + entityName + " due to: " + e.getMessage());
                    }
                }
                for (int i = 0; i < values.length; i++) {
                    try {
                        values[i] = values[i].trim();
                        logger.debug(String.format("value: %s = %s", attributeNames[i], values[i]));
                        /*
                         * check if reference
                         */
                        if (attributeNames[i].contains(".")) {
                            String[] refInfo = attributeNames[i].split("\\.");
                            logger.debug("Assoc: " + refInfo[0]);

                            IMetaAssociation assoc = Core.getMetaAssociation(moduleName + "." + refInfo[0]);
                            logger.debug("Assoc: " + assoc.getName() + ", " + assoc.getParent().getName() + " - " + assoc.getChild().getName());
                            /*
                             * check if reference set
                             */

                            if (attributeIsFKS[i]
                                    || (values[i].startsWith("[") && values[i].endsWith("]"))) {
                                // reference set
                                String[] refs = values[i].replaceAll("^\\[|\\]$", "").split(attributeFKSSeparator[i]);
                                // find referenced objects
                                List<IMendixIdentifier> ids = new ArrayList();
                                for (int ri = 0; ri < refs.length; ri++) {
                                    logger.debug("ref: " + refs[ri]);
                                    String xpath = String.format("//%s[%s=$param]", assoc.getChild().getName(), refInfo[1]);
                                    logger.debug("xpath = " + xpath);
                                    List<IMendixObject> refObjectList = Core.createXPathQuery(xpath)
                                            .setVariable("param", refs[ri])
                                            .execute(context);
                                    if (refObjectList.size() == 1) {
                                        logger.debug("ref object uuid: " + refObjectList.get(0).getId().toLong());
                                        ids.add(refObjectList.get(0).getId());
                                    } else {
                                        logger.warn("found more than one object on ref");
                                        errorMessages.put("found more than one object on ref");
                                    }
                                }
                                // add reference to object
                                object.setValue(ctx, assoc.getName(), ids);
                            } else {
                                // reference
                                if (values[i] != null && !values[i].equals("")) {
                                    String xpath = String.format("//%s[%s=$param]", assoc.getChild().getName(), refInfo[1]);
                                    logger.debug("reference xpath = " + xpath);
                                    //String xpath = String.format("//%s[%s=%s]", assoc.getChild().getName(), refInfo[1], values[i]);
                                    //logger.debug("reference xpath: " + xpath);
                                    try {
                                        List<IMendixObject> refObjectList = Core.createXPathQuery(xpath)
                                                .setVariable("param", values[i])
                                                .execute(context);
//                                        List<IMendixObject> refObjectList = Core.retrieveXPathQuery(ctx, xpath);
                                        if (refObjectList.size() != 0) {
                                            logger.debug("references obj id: " + refObjectList.get(0).getId().toLong());
                                            object.setValue(ctx, assoc.getName(), refObjectList.get(0).getId());
                                        } else {
                                            String errorMsg = String.format("Associated object %s for %s where %s=%s not found",
                                                    assoc.getChild().getName(), assoc.getName(), refInfo[1], values[i]);
                                            logger.warn(errorMsg);
                                            errorMessages.put(errorMsg);
                                        }
                                    } catch (Exception e) {
                                        logger.warn("Failed to set reference: " + e.getMessage());
                                        errorMessages.put("Failed to set reference: " + e.getMessage());
                                    }
                                }
                            }

                        } else {
                            /*
                             * set attribute value
                             */
                            IMetaPrimitive.PrimitiveType type = null;
                            try {
                                IMetaPrimitive primitive = null;
                                primitive = object.getMetaObject().getMetaPrimitive(attributeNames[i]);
                                type = primitive.getType();
                            } catch (Exception e) {
                                logger.error("Failed to get primitive for attribute: " + attributeNames[i]);
                                type = IMetaPrimitive.PrimitiveType.String;
                            }
                            logger.debug("attribute type: " + type.name());
                            values[i] = values[i].trim();
                            try {
                                if (type.equals(IMetaPrimitive.PrimitiveType.DateTime)) {
                                    object.setValue(ctx, attributeNames[i], toDateValue(values[i], attributeFormatters[i]));
                                } else {
                                    object.setValue(ctx, attributeNames[i], values[i].replaceAll("^\"|\"$", ""));
                                }
                            } catch (Exception e) {
                                logger.debug(String.format("Attribute %s cannot be set", attributeNames[i]));
                                errorMessages.put(String.format("Attribute %s cannot be set", attributeNames[i]));
                                if (strict) {
                                    throw e;
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.debug(String.format("Attribute %d cannot be set for row %d", i, lineNo));
                        errorMessages.put(String.format("Attribute %d cannot be set for row %d", i, lineNo));
                        if (strict) {
                            throw e;
                        }
                    }
                }
                logger.debug("commiting object: " + object);
                Core.commit(ctx, object);
            } catch (CoreException e) {
                logger.warn(e);
                logger.warn("failed to create object: " + object);
                errorMessages.put("failed to create object: " + object);
            } catch (UserException e2) {
                logger.warn(e2);
                logger.warn("failed to create object: " + e2.getMessage());
                errorMessages.put("failed to create object: " + object);
            } catch (MendixRuntimeException e3) {
                logger.warn(e3);
                logger.warn("failed to create object: " + e3.getMessage());
                errorMessages.put("failed to create object: " + object);
            }

            lineNo++;
            line = "";
            objNo++;
            if (objNo % 100 == 0) {
                logger.info("Commiting up to object: " + objNo);
                ctx.endTransaction();

                ctx = context.createClone();
                ctx.startTransaction();
            }
        }
        ctx.endTransaction();
        context.startTransaction();

        logger.info("objects created: " + objNo);
        JSONObject response = new JSONObject();
        response.put("lines_processed", lineNo);
        response.put("objects_created", objNo);
        if (errorMessages.length() == 0) {
            response.put("status", "successfully created objects");
        } else {
            logger.warn("# Errors during import: " + errorMessages.length());
            logger.warn(errorMessages.get(0));
            response.put("status", "Failure during object creation");
            response.put("numberOfErrors", errorMessages.length());
            //response.put("errors",errorMessages.subList(0,Math.min(10,errorMessages.length())).toArray());
            response.put("errors", errorMessages);
        }
        inputStream.close();
        String responseString = "";
        try {
            responseString = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        writer.write(responseString);
        return objNo;
    }

    private Object toDateValue(String value, Format attributeFormatter) {
        Object val = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (attributeFormatter != null) {
                val = attributeFormatter.parseObject(value.replaceAll("^\"|\"$", ""));
            } else {
                val = dateTimeFormat.parse(value.replaceAll("^\"|\"$", ""));
            }
        } catch (ParseException e) {
            try {
                val = dateFormat.parse(value.replaceAll("^\"|\"$", ""));
            } catch (ParseException e1) {
                try {
                    val = dateFormat2.parse(value.replaceAll("^\"|\"$", ""));
                } catch (ParseException e2) {
                    logger.warn("failed to parse date: " + value);
                }
            }
        }
        return val;
    }

}
