package system;

import com.mendix.core.actionmanagement.IActionRegistrator;

public class UserActionsRegistrar
{
  public void registerActions(IActionRegistrator registrator)
  {
    registrator.bundleComponentLoaded();
    registrator.registerUserAction(csvservices.actions.CsvExportInitializeAction.class);
    registrator.registerUserAction(csvservices.actions.ExportCsvData.class);
    registrator.registerUserAction(csvservices.actions.GenerateAndImportData.class);
    registrator.registerUserAction(csvservices.actions.GenerateData.class);
    registrator.registerUserAction(csvservices.actions.ImportCsvData.class);
    registrator.registerUserAction(csvservices.actions.ImportCsvResourceData.class);
    registrator.registerUserAction(csvservices.actions.ImportCsvUrlData.class);
    registrator.registerUserAction(csvservices.actions.ImportCsvUrlDataToLoaderRow.class);
    registrator.registerUserAction(mxdbutils.actions.DeleteEntitiesDirectly.class);
    registrator.registerUserAction(mxdbutils.actions.ExecuteDatabaseUpdateStatement.class);
    registrator.registerUserAction(mxdbutils.actions.ExecuteQuery.class);
    registrator.registerUserAction(mxdbutils.actions.GetDatabaseInformation.class);
    registrator.registerUserAction(mxdbutils.actions.LockObject.class);
    registrator.registerUserAction(mxdbutils.actions.TruncateEntity.class);
    registrator.registerUserAction(mxdbutils.actions.Vacuum.class);
    registrator.registerUserAction(system.actions.VerifyPassword.class);
  }
}
