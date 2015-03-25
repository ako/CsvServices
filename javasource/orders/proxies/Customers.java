// This file was generated by Mendix Business Modeler.
//
// WARNING: Code you write here will be lost the next time you deploy the project.

package orders.proxies;

import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixIdentifier;
import com.mendix.systemwideinterfaces.core.IMendixObject;

/**
 * 
 */
public class Customers
{
	private final IMendixObject customersMendixObject;

	private final IContext context;

	/**
	 * Internal name of this entity
	 */
	public static final String entityName = "Orders.Customers";

	/**
	 * Enum describing members of this entity
	 */
	public enum MemberNames
	{
		CustomerId("CustomerId"),
		Firstname("Firstname"),
		Lastname("Lastname"),
		DateOfBirth("DateOfBirth"),
		Billing_Address("Orders.Billing_Address"),
		Delivery_Address("Orders.Delivery_Address");

		private String metaName;

		MemberNames(String s)
		{
			metaName = s;
		}

		@Override
		public String toString()
		{
			return metaName;
		}
	}

	public Customers(IContext context)
	{
		this(context, Core.instantiate(context, "Orders.Customers"));
	}

	protected Customers(IContext context, IMendixObject customersMendixObject)
	{
		if (customersMendixObject == null)
			throw new IllegalArgumentException("The given object cannot be null.");
		if (!Core.isSubClassOf("Orders.Customers", customersMendixObject.getType()))
			throw new IllegalArgumentException("The given object is not a Orders.Customers");

		this.customersMendixObject = customersMendixObject;
		this.context = context;
	}

	/**
	 * @deprecated Use 'Customers.load(IContext, IMendixIdentifier)' instead.
	 */
	@Deprecated
	public static orders.proxies.Customers initialize(IContext context, IMendixIdentifier mendixIdentifier) throws CoreException
	{
		return orders.proxies.Customers.load(context, mendixIdentifier);
	}

	/**
	 * Initialize a proxy using context (recommended). This context will be used for security checking when the get- and set-methods without context parameters are called.
	 * The get- and set-methods with context parameter should be used when for instance sudo access is necessary (IContext.getSudoContext() can be used to obtain sudo access).
	 */
	public static orders.proxies.Customers initialize(IContext context, IMendixObject mendixObject)
	{
		return new orders.proxies.Customers(context, mendixObject);
	}

	public static orders.proxies.Customers load(IContext context, IMendixIdentifier mendixIdentifier) throws CoreException
	{
		IMendixObject mendixObject = Core.retrieveId(context, mendixIdentifier);
		return orders.proxies.Customers.initialize(context, mendixObject);
	}

	public static java.util.List<orders.proxies.Customers> load(IContext context, String xpathConstraint) throws CoreException
	{
		java.util.List<orders.proxies.Customers> result = new java.util.ArrayList<orders.proxies.Customers>();
		for (IMendixObject obj : Core.retrieveXPathQuery(context, "//Orders.Customers" + xpathConstraint))
			result.add(orders.proxies.Customers.initialize(context, obj));
		return result;
	}

	/**
	 * Commit the changes made on this proxy object.
	 */
	public final void commit() throws CoreException
	{
		Core.commit(context, getMendixObject());
	}

	/**
	 * Commit the changes made on this proxy object using the specified context.
	 */
	public final void commit(IContext context) throws CoreException
	{
		Core.commit(context, getMendixObject());
	}

	/**
	 * Delete the object.
	 */
	public final void delete()
	{
		Core.delete(context, getMendixObject());
	}

	/**
	 * Delete the object using the specified context.
	 */
	public final void delete(IContext context)
	{
		Core.delete(context, getMendixObject());
	}
	/**
	 * @return value of CustomerId
	 */
	public final Long getCustomerId()
	{
		return getCustomerId(getContext());
	}

	/**
	 * @param context
	 * @return value of CustomerId
	 */
	public final Long getCustomerId(IContext context)
	{
		return (Long) getMendixObject().getValue(context, MemberNames.CustomerId.toString());
	}

	/**
	 * Set value of CustomerId
	 * @param customerid
	 */
	public final void setCustomerId(Long customerid)
	{
		setCustomerId(getContext(), customerid);
	}

	/**
	 * Set value of CustomerId
	 * @param context
	 * @param customerid
	 */
	public final void setCustomerId(IContext context, Long customerid)
	{
		getMendixObject().setValue(context, MemberNames.CustomerId.toString(), customerid);
	}

	/**
	 * @return value of Firstname
	 */
	public final String getFirstname()
	{
		return getFirstname(getContext());
	}

	/**
	 * @param context
	 * @return value of Firstname
	 */
	public final String getFirstname(IContext context)
	{
		return (String) getMendixObject().getValue(context, MemberNames.Firstname.toString());
	}

	/**
	 * Set value of Firstname
	 * @param firstname
	 */
	public final void setFirstname(String firstname)
	{
		setFirstname(getContext(), firstname);
	}

	/**
	 * Set value of Firstname
	 * @param context
	 * @param firstname
	 */
	public final void setFirstname(IContext context, String firstname)
	{
		getMendixObject().setValue(context, MemberNames.Firstname.toString(), firstname);
	}

	/**
	 * @return value of Lastname
	 */
	public final String getLastname()
	{
		return getLastname(getContext());
	}

	/**
	 * @param context
	 * @return value of Lastname
	 */
	public final String getLastname(IContext context)
	{
		return (String) getMendixObject().getValue(context, MemberNames.Lastname.toString());
	}

	/**
	 * Set value of Lastname
	 * @param lastname
	 */
	public final void setLastname(String lastname)
	{
		setLastname(getContext(), lastname);
	}

	/**
	 * Set value of Lastname
	 * @param context
	 * @param lastname
	 */
	public final void setLastname(IContext context, String lastname)
	{
		getMendixObject().setValue(context, MemberNames.Lastname.toString(), lastname);
	}

	/**
	 * @return value of DateOfBirth
	 */
	public final java.util.Date getDateOfBirth()
	{
		return getDateOfBirth(getContext());
	}

	/**
	 * @param context
	 * @return value of DateOfBirth
	 */
	public final java.util.Date getDateOfBirth(IContext context)
	{
		return (java.util.Date) getMendixObject().getValue(context, MemberNames.DateOfBirth.toString());
	}

	/**
	 * Set value of DateOfBirth
	 * @param dateofbirth
	 */
	public final void setDateOfBirth(java.util.Date dateofbirth)
	{
		setDateOfBirth(getContext(), dateofbirth);
	}

	/**
	 * Set value of DateOfBirth
	 * @param context
	 * @param dateofbirth
	 */
	public final void setDateOfBirth(IContext context, java.util.Date dateofbirth)
	{
		getMendixObject().setValue(context, MemberNames.DateOfBirth.toString(), dateofbirth);
	}

	/**
	 * @return value of Billing_Address
	 */
	public final orders.proxies.Address getBilling_Address() throws CoreException
	{
		return getBilling_Address(getContext());
	}

	/**
	 * @param context
	 * @return value of Billing_Address
	 */
	public final orders.proxies.Address getBilling_Address(IContext context) throws CoreException
	{
		orders.proxies.Address result = null;
		IMendixIdentifier identifier = getMendixObject().getValue(context, MemberNames.Billing_Address.toString());
		if (identifier != null)
			result = orders.proxies.Address.load(context, identifier);
		return result;
	}

	/**
	 * Set value of Billing_Address
	 * @param billing_address
	 */
	public final void setBilling_Address(orders.proxies.Address billing_address)
	{
		setBilling_Address(getContext(), billing_address);
	}

	/**
	 * Set value of Billing_Address
	 * @param context
	 * @param billing_address
	 */
	public final void setBilling_Address(IContext context, orders.proxies.Address billing_address)
	{
		if (billing_address == null)
			getMendixObject().setValue(context, MemberNames.Billing_Address.toString(), null);
		else
			getMendixObject().setValue(context, MemberNames.Billing_Address.toString(), billing_address.getMendixObject().getId());
	}

	/**
	 * @return value of Delivery_Address
	 */
	public final orders.proxies.Address getDelivery_Address() throws CoreException
	{
		return getDelivery_Address(getContext());
	}

	/**
	 * @param context
	 * @return value of Delivery_Address
	 */
	public final orders.proxies.Address getDelivery_Address(IContext context) throws CoreException
	{
		orders.proxies.Address result = null;
		IMendixIdentifier identifier = getMendixObject().getValue(context, MemberNames.Delivery_Address.toString());
		if (identifier != null)
			result = orders.proxies.Address.load(context, identifier);
		return result;
	}

	/**
	 * Set value of Delivery_Address
	 * @param delivery_address
	 */
	public final void setDelivery_Address(orders.proxies.Address delivery_address)
	{
		setDelivery_Address(getContext(), delivery_address);
	}

	/**
	 * Set value of Delivery_Address
	 * @param context
	 * @param delivery_address
	 */
	public final void setDelivery_Address(IContext context, orders.proxies.Address delivery_address)
	{
		if (delivery_address == null)
			getMendixObject().setValue(context, MemberNames.Delivery_Address.toString(), null);
		else
			getMendixObject().setValue(context, MemberNames.Delivery_Address.toString(), delivery_address.getMendixObject().getId());
	}

	/**
	 * @return the IMendixObject instance of this proxy for use in the Core interface.
	 */
	public final IMendixObject getMendixObject()
	{
		return customersMendixObject;
	}

	/**
	 * @return the IContext instance of this proxy, or null if no IContext instance was specified at initialization.
	 */
	public final IContext getContext()
	{
		return context;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;

		if (obj != null && getClass().equals(obj.getClass()))
		{
			final orders.proxies.Customers that = (orders.proxies.Customers) obj;
			return getMendixObject().equals(that.getMendixObject());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return getMendixObject().hashCode();
	}

	/**
	 * @return String name of this class
	 */
	public static String getType()
	{
		return "Orders.Customers";
	}

	/**
	 * @return String GUID from this object, format: ID_0000000000
	 * @deprecated Use getMendixObject().getId().toLong() to get a unique identifier for this object.
	 */
	@Deprecated
	public String getGUID()
	{
		return "ID_" + getMendixObject().getId().toLong();
	}
}
