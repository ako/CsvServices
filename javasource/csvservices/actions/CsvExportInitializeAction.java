// This file was generated by Mendix Modeler.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package csvservices.actions;

import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;
import csvservices.impl.CsvRestHandler;

public class CsvExportInitializeAction extends CustomJavaAction<java.lang.Boolean>
{
	private java.lang.String RequiredUserRole;

	public CsvExportInitializeAction(IContext context, java.lang.String RequiredUserRole)
	{
		super(context);
		this.RequiredUserRole = RequiredUserRole;
	}

	@java.lang.Override
	public java.lang.Boolean executeAction() throws Exception
	{
		// BEGIN USER CODE
        Core.addRequestHandler(CsvRestHandler.serviceRoot, new CsvRestHandler(this.getContext(), this.RequiredUserRole));
        return true;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "CsvExportInitializeAction";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
