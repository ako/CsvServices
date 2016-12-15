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
    private static ILogNode logger = Core.getLogger(CsvImporter.class.getName());

    /*
     * Create new entities for uploaded stuff
     */
    public void csvToEntities(IContext context, Writer writer, String moduleName, String entityName, InputStream inputStream) throws IOException {
        logger.info(String.format("csvToEntities: %s.%s",moduleName,entityName));
        final String csvSplitter = (",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        int lineNo = 0;
        String[] attributeNames = null;
        String[] attributeFormats = null;
        Format[] attributeFormatters = null;
        Boolean[] attributeIsPK = null;
        Boolean entityHasPk = false;
        JSONArray errorMessages = new JSONArray();

        context.startTransaction();
        while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith("#") || line.length() == 0) {
                continue;
            }
            if (lineNo == 0) {
                // Header line
                attributeNames = line.split(csvSplitter);
                attributeFormats = new String[attributeNames.length];
                attributeFormatters = new Format[attributeNames.length];
                attributeIsPK = new Boolean[attributeNames.length];
                for (int i = 0; i < attributeNames.length; i++) {
                    attributeNames[i] = attributeNames[i].trim();
                    // check if attribute is part of primary key
                    attributeIsPK[i] = attributeNames[i].endsWith("*");
                    if (attributeIsPK[i]) {
                        entityHasPk = true;
                        attributeNames[i] = attributeNames[i].replace("*", "");
                    }
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
            } else {
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
                            List<IMendixObject> objects = Core.retrieveXPathQuery(context, findByPkXpath);
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
                            object = Core.instantiate(context, moduleName + "." + entityName);
                        } catch (Exception e) {
                            logger.info(e);
                            logger.warn("Failed to create a new object " + moduleName + "." + entityName + " due to: " + e.getMessage());
                            errorMessages.put("Failed to create a new object " + moduleName + "." + entityName + " due to: " + e.getMessage());
                        }
                    }
                    for (int i = 0; i < values.length; i++) {
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

                            if (values[i].startsWith("[") && values[i].endsWith("]")) {
                                // reference set
                                String[] refs = values[i].replaceAll("^\\[|\\]$", "").split(";");
                                // find referenced objects
                                List<IMendixIdentifier> ids = new ArrayList();
                                for (int ri = 0; ri < refs.length; ri++) {
                                    logger.debug("ref: " + refs[ri]);
                                    String xpath = String.format("//%s[%s=%s]", assoc.getChild().getName(), refInfo[1], refs[ri]);
                                    logger.debug("xpath = " + xpath);
                                    List<IMendixObject> refObjectList = Core.retrieveXPathQuery(context, xpath);
                                    if (refObjectList.size() == 1) {
                                        logger.debug("ref object uuid: " + refObjectList.get(0).getId().toLong());
                                        ids.add(refObjectList.get(0).getId());
                                    } else {
                                        logger.warn("found more than one object on ref");
                                        errorMessages.put("found more than one object on ref");
                                    }
                                }
                                // add reference to object
                                object.setValue(context, assoc.getName(), ids);
                            } else {
                                // reference
                                if (values[i] != null && !values[i].equals("")) {
                                    String xpath = String.format("//%s[%s=%s]", assoc.getChild().getName(), refInfo[1], values[i]);
                                    logger.debug("reference xpath: " + xpath);
                                    try {
                                        List<IMendixObject> refObjectList = Core.retrieveXPathQuery(context, xpath);
                                        if(refObjectList.size() != 0) {
                                            logger.debug("references obj id: " + refObjectList.get(0).getId().toLong());
                                            object.setValue(context, assoc.getName(), refObjectList.get(0).getId());
                                        }else{
                                            String errorMsg = String.format("Associated object %s for %s where %s=%s not found",
                                                    assoc.getChild().getName(), assoc.getName(), refInfo[1],values[i]);
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
                            if (type.equals(IMetaPrimitive.PrimitiveType.DateTime)) {
                                object.setValue(context, attributeNames[i], toDateValue(values[i], attributeFormatters[i]));
                            } else {
                                object.setValue(context, attributeNames[i], values[i].replaceAll("^\"|\"$", ""));
                            }
                        }
                    }
                    logger.debug("commiting object: " + object);
                    Core.commit(context, object);
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
            }
            lineNo++;
            if(lineNo % 100 == 0){
                context.endTransaction();
                context.startTransaction();
            }
        }
        context.endTransaction();

        logger.info("objects created: " + lineNo);
        JSONObject response = new JSONObject();
        response.put("lines_processed", lineNo);
        if (errorMessages.length() == 0) {
            response.put("status", "successfully created objects");
        } else {
            logger.warn("# Errors during import: " + errorMessages.length());
            logger.warn(errorMessages.get(0));
            response.put("status", "Failure during object creation");
            response.put("numberOfErrors",errorMessages.length());
            //response.put("errors",errorMessages.subList(0,Math.min(10,errorMessages.length())).toArray());
            response.put("errors", errorMessages);
        }
        inputStream.close();
        String responseString = "";
        try{
            responseString = response.toString();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        writer.write(responseString);
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
