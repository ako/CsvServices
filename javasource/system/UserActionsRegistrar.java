package system;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import com.mendix.core.actionmanagement.IActionRegistrator;

@Component(immediate = true)
public class UserActionsRegistrar
{
  @Reference
  public void registerActions(IActionRegistrator registrator)
  {
    registrator.bundleComponentLoaded();
    registrator.registerUserAction(csvservices.actions.CsvExportInitializeAction.class);
    registrator.registerUserAction(csvservices.actions.ImportCsvData.class);
    registrator.registerUserAction(csvservices.actions.ImportCsvResourceData.class);
    registrator.registerUserAction(system.actions.VerifyPassword.class);
  }
}
