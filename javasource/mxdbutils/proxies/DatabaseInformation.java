// This file was generated by Mendix Modeler.
//
// WARNING: Code you write here will be lost the next time you deploy the project.

package mxdbutils.proxies;

public class DatabaseInformation
{
	private final com.mendix.systemwideinterfaces.core.IMendixObject databaseInformationMendixObject;

	private final com.mendix.systemwideinterfaces.core.IContext context;

	/**
	 * Internal name of this entity
	 */
	public static final java.lang.String entityName = "MxDbUtils.DatabaseInformation";

	/**
	 * Enum describing members of this entity
	 */
	public enum MemberNames
	{
		ProductName("ProductName"),
		ProductVersion("ProductVersion"),
		Implementation("Implementation");

		private java.lang.String metaName;

		MemberNames(java.lang.String s)
		{
			metaName = s;
		}

		@java.lang.Override
		public java.lang.String toString()
		{
			return metaName;
		}
	}

	public DatabaseInformation(com.mendix.systemwideinterfaces.core.IContext context)
	{
		this(context, com.mendix.core.Core.instantiate(context, "MxDbUtils.DatabaseInformation"));
	}

	protected DatabaseInformation(com.mendix.systemwideinterfaces.core.IContext context, com.mendix.systemwideinterfaces.core.IMendixObject databaseInformationMendixObject)
	{
		if (databaseInformationMendixObject == null)
			throw new java.lang.IllegalArgumentException("The given object cannot be null.");
		if (!com.mendix.core.Core.isSubClassOf("MxDbUtils.DatabaseInformation", databaseInformationMendixObject.getType()))
			throw new java.lang.IllegalArgumentException("The given object is not a MxDbUtils.DatabaseInformation");

		this.databaseInformationMendixObject = databaseInformationMendixObject;
		this.context = context;
	}

	/**
	 * @deprecated Use 'DatabaseInformation.load(IContext, IMendixIdentifier)' instead.
	 */
	@java.lang.Deprecated
	public static mxdbutils.proxies.DatabaseInformation initialize(com.mendix.systemwideinterfaces.core.IContext context, com.mendix.systemwideinterfaces.core.IMendixIdentifier mendixIdentifier) throws com.mendix.core.CoreException
	{
		return mxdbutils.proxies.DatabaseInformation.load(context, mendixIdentifier);
	}

	/**
	 * Initialize a proxy using context (recommended). This context will be used for security checking when the get- and set-methods without context parameters are called.
	 * The get- and set-methods with context parameter should be used when for instance sudo access is necessary (IContext.createSudoClone() can be used to obtain sudo access).
	 */
	public static mxdbutils.proxies.DatabaseInformation initialize(com.mendix.systemwideinterfaces.core.IContext context, com.mendix.systemwideinterfaces.core.IMendixObject mendixObject)
	{
		return new mxdbutils.proxies.DatabaseInformation(context, mendixObject);
	}

	public static mxdbutils.proxies.DatabaseInformation load(com.mendix.systemwideinterfaces.core.IContext context, com.mendix.systemwideinterfaces.core.IMendixIdentifier mendixIdentifier) throws com.mendix.core.CoreException
	{
		com.mendix.systemwideinterfaces.core.IMendixObject mendixObject = com.mendix.core.Core.retrieveId(context, mendixIdentifier);
		return mxdbutils.proxies.DatabaseInformation.initialize(context, mendixObject);
	}

	/**
	 * Commit the changes made on this proxy object.
	 */
	public final void commit() throws com.mendix.core.CoreException
	{
		com.mendix.core.Core.commit(context, getMendixObject());
	}

	/**
	 * Commit the changes made on this proxy object using the specified context.
	 */
	public final void commit(com.mendix.systemwideinterfaces.core.IContext context) throws com.mendix.core.CoreException
	{
		com.mendix.core.Core.commit(context, getMendixObject());
	}

	/**
	 * Delete the object.
	 */
	public final void delete()
	{
		com.mendix.core.Core.delete(context, getMendixObject());
	}

	/**
	 * Delete the object using the specified context.
	 */
	public final void delete(com.mendix.systemwideinterfaces.core.IContext context)
	{
		com.mendix.core.Core.delete(context, getMendixObject());
	}
	/**
	 * @return value of ProductName
	 */
	public final java.lang.String getProductName()
	{
		return getProductName(getContext());
	}

	/**
	 * @param context
	 * @return value of ProductName
	 */
	public final java.lang.String getProductName(com.mendix.systemwideinterfaces.core.IContext context)
	{
		return (java.lang.String) getMendixObject().getValue(context, MemberNames.ProductName.toString());
	}

	/**
	 * Set value of ProductName
	 * @param productname
	 */
	public final void setProductName(java.lang.String productname)
	{
		setProductName(getContext(), productname);
	}

	/**
	 * Set value of ProductName
	 * @param context
	 * @param productname
	 */
	public final void setProductName(com.mendix.systemwideinterfaces.core.IContext context, java.lang.String productname)
	{
		getMendixObject().setValue(context, MemberNames.ProductName.toString(), productname);
	}

	/**
	 * @return value of ProductVersion
	 */
	public final java.lang.String getProductVersion()
	{
		return getProductVersion(getContext());
	}

	/**
	 * @param context
	 * @return value of ProductVersion
	 */
	public final java.lang.String getProductVersion(com.mendix.systemwideinterfaces.core.IContext context)
	{
		return (java.lang.String) getMendixObject().getValue(context, MemberNames.ProductVersion.toString());
	}

	/**
	 * Set value of ProductVersion
	 * @param productversion
	 */
	public final void setProductVersion(java.lang.String productversion)
	{
		setProductVersion(getContext(), productversion);
	}

	/**
	 * Set value of ProductVersion
	 * @param context
	 * @param productversion
	 */
	public final void setProductVersion(com.mendix.systemwideinterfaces.core.IContext context, java.lang.String productversion)
	{
		getMendixObject().setValue(context, MemberNames.ProductVersion.toString(), productversion);
	}

	/**
	 * Set value of Implementation
	 * @param implementation
	 */
	public final mxdbutils.proxies.DatabaseImplementation getImplementation()
	{
		return getImplementation(getContext());
	}

	/**
	 * @param context
	 * @return value of Implementation
	 */
	public final mxdbutils.proxies.DatabaseImplementation getImplementation(com.mendix.systemwideinterfaces.core.IContext context)
	{
		Object obj = getMendixObject().getValue(context, MemberNames.Implementation.toString());
		if (obj == null)
			return null;

		return mxdbutils.proxies.DatabaseImplementation.valueOf((java.lang.String) obj);
	}

	/**
	 * Set value of Implementation
	 * @param implementation
	 */
	public final void setImplementation(mxdbutils.proxies.DatabaseImplementation implementation)
	{
		setImplementation(getContext(), implementation);
	}

	/**
	 * Set value of Implementation
	 * @param context
	 * @param implementation
	 */
	public final void setImplementation(com.mendix.systemwideinterfaces.core.IContext context, mxdbutils.proxies.DatabaseImplementation implementation)
	{
		if (implementation != null)
			getMendixObject().setValue(context, MemberNames.Implementation.toString(), implementation.toString());
		else
			getMendixObject().setValue(context, MemberNames.Implementation.toString(), null);
	}

	/**
	 * @return the IMendixObject instance of this proxy for use in the Core interface.
	 */
	public final com.mendix.systemwideinterfaces.core.IMendixObject getMendixObject()
	{
		return databaseInformationMendixObject;
	}

	/**
	 * @return the IContext instance of this proxy, or null if no IContext instance was specified at initialization.
	 */
	public final com.mendix.systemwideinterfaces.core.IContext getContext()
	{
		return context;
	}

	@java.lang.Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;

		if (obj != null && getClass().equals(obj.getClass()))
		{
			final mxdbutils.proxies.DatabaseInformation that = (mxdbutils.proxies.DatabaseInformation) obj;
			return getMendixObject().equals(that.getMendixObject());
		}
		return false;
	}

	@java.lang.Override
	public int hashCode()
	{
		return getMendixObject().hashCode();
	}

	/**
	 * @return String name of this class
	 */
	public static java.lang.String getType()
	{
		return "MxDbUtils.DatabaseInformation";
	}

	/**
	 * @return String GUID from this object, format: ID_0000000000
	 * @deprecated Use getMendixObject().getId().toLong() to get a unique identifier for this object.
	 */
	@java.lang.Deprecated
	public java.lang.String getGUID()
	{
		return "ID_" + getMendixObject().getId().toLong();
	}
}