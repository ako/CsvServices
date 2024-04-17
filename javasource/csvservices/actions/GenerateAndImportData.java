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
import csvservices.impl.DataGenerator;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import static java.lang.Math.min;

public class GenerateAndImportData extends CustomJavaAction<java.lang.Long>
{
	private java.lang.String Header;
	private java.lang.String ColumnExpressions;
	private java.lang.String Delimiter;
	private java.lang.Long RecordCount;
	private java.lang.String DataPattern;
	private java.lang.String Entity;

	public GenerateAndImportData(IContext context, java.lang.String Header, java.lang.String ColumnExpressions, java.lang.String Delimiter, java.lang.Long RecordCount, java.lang.String DataPattern, java.lang.String Entity)
	{
		super(context);
		this.Header = Header;
		this.ColumnExpressions = ColumnExpressions;
		this.Delimiter = Delimiter;
		this.RecordCount = RecordCount;
		this.DataPattern = DataPattern;
		this.Entity = Entity;
	}

	@java.lang.Override
	public java.lang.Long executeAction() throws Exception
	{
		// BEGIN USER CODE
		ILogNode logger = Core.getLogger(GenerateData.class.getName());
		logger.info(String.format("Pars: %s, %s, %s, %d",this.Header,this.ColumnExpressions, this.Delimiter,this.RecordCount));
		DataGenerator dg = new DataGenerator();

		Long objectsCreated = 0l;
		CsvImporter csvImporter = new CsvImporter();
		StringWriter outputWriter = new StringWriter();
		String moduleName = this.Entity.split("\\.")[0];
		String entityName = this.Entity.split("\\.")[1];

		long importCount = 0;
		while(importCount < this.RecordCount) {
			// generate csv data
			String fakeBatchCsv =  dg.generate(this.Header,this.ColumnExpressions.replaceAll("\"","''"),this.Delimiter,min(this.RecordCount - importCount,100));
			// import csv data
			objectsCreated += Long.valueOf(csvImporter.csvToEntities(getContext(), outputWriter, moduleName, entityName, new ByteArrayInputStream(fakeBatchCsv.getBytes(StandardCharsets.UTF_8)), false, -1, true, null, this.Delimiter));
			logger.info(String.format("Imported %d of %d",objectsCreated,this.RecordCount));
			importCount+=100;
		}
		outputWriter.close();
		return objectsCreated;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "GenerateAndImportData";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
