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
import csvservices.impl.DataGenerator;
import java.util.StringTokenizer;

public class GenerateData extends CustomJavaAction<java.lang.String>
{
	private java.lang.String Header;
	private java.lang.String ColumnExpressions;
	private java.lang.String Delimiter;
	private java.lang.Long RecordCount;
	private java.lang.String DataPattern;

	public GenerateData(IContext context, java.lang.String Header, java.lang.String ColumnExpressions, java.lang.String Delimiter, java.lang.Long RecordCount, java.lang.String DataPattern)
	{
		super(context);
		this.Header = Header;
		this.ColumnExpressions = ColumnExpressions;
		this.Delimiter = Delimiter;
		this.RecordCount = RecordCount;
		this.DataPattern = DataPattern;
	}

	@java.lang.Override
	public java.lang.String executeAction() throws Exception
	{
		// BEGIN USER CODE
		//throw new com.mendix.systemwideinterfaces.MendixRuntimeException("Java action was not implemented");
		//String data = "";
		ILogNode logger = Core.getLogger(GenerateData.class.getName());
		logger.info(String.format("Pars: %s, %s, %s, %d",this.Header,this.ColumnExpressions, this.Delimiter,this.RecordCount));
		DataGenerator dg = new DataGenerator();
		if(this.DataPattern!=null) {
			return dg.generate(this.DataPattern);
		}else{
			return dg.generate(this.Header,this.ColumnExpressions,this.Delimiter,this.RecordCount);
		}
		//return data;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "GenerateData";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}