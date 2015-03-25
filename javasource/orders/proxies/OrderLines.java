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
public class OrderLines
{
	private final IMendixObject orderLinesMendixObject;

	private final IContext context;

	/**
	 * Internal name of this entity
	 */
	public static final String entityName = "Orders.OrderLines";

	/**
	 * Enum describing members of this entity
	 */
	public enum MemberNames
	{
		LineId("LineId"),
		LineNo("LineNo"),
		Quantity("Quantity"),
		Line_Product("Orders.Line_Product"),
		Order_Lines("Orders.Order_Lines");

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

	public OrderLines(IContext context)
	{
		this(context, Core.instantiate(context, "Orders.OrderLines"));
	}

	protected OrderLines(IContext context, IMendixObject orderLinesMendixObject)
	{
		if (orderLinesMendixObject == null)
			throw new IllegalArgumentException("The given object cannot be null.");
		if (!Core.isSubClassOf("Orders.OrderLines", orderLinesMendixObject.getType()))
			throw new IllegalArgumentException("The given object is not a Orders.OrderLines");

		this.orderLinesMendixObject = orderLinesMendixObject;
		this.context = context;
	}

	/**
	 * @deprecated Use 'OrderLines.load(IContext, IMendixIdentifier)' instead.
	 */
	@Deprecated
	public static orders.proxies.OrderLines initialize(IContext context, IMendixIdentifier mendixIdentifier) throws CoreException
	{
		return orders.proxies.OrderLines.load(context, mendixIdentifier);
	}

	/**
	 * Initialize a proxy using context (recommended). This context will be used for security checking when the get- and set-methods without context parameters are called.
	 * The get- and set-methods with context parameter should be used when for instance sudo access is necessary (IContext.getSudoContext() can be used to obtain sudo access).
	 */
	public static orders.proxies.OrderLines initialize(IContext context, IMendixObject mendixObject)
	{
		return new orders.proxies.OrderLines(context, mendixObject);
	}

	public static orders.proxies.OrderLines load(IContext context, IMendixIdentifier mendixIdentifier) throws CoreException
	{
		IMendixObject mendixObject = Core.retrieveId(context, mendixIdentifier);
		return orders.proxies.OrderLines.initialize(context, mendixObject);
	}

	public static java.util.List<orders.proxies.OrderLines> load(IContext context, String xpathConstraint) throws CoreException
	{
		java.util.List<orders.proxies.OrderLines> result = new java.util.ArrayList<orders.proxies.OrderLines>();
		for (IMendixObject obj : Core.retrieveXPathQuery(context, "//Orders.OrderLines" + xpathConstraint))
			result.add(orders.proxies.OrderLines.initialize(context, obj));
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
	 * @return value of LineId
	 */
	public final Long getLineId()
	{
		return getLineId(getContext());
	}

	/**
	 * @param context
	 * @return value of LineId
	 */
	public final Long getLineId(IContext context)
	{
		return (Long) getMendixObject().getValue(context, MemberNames.LineId.toString());
	}

	/**
	 * Set value of LineId
	 * @param lineid
	 */
	public final void setLineId(Long lineid)
	{
		setLineId(getContext(), lineid);
	}

	/**
	 * Set value of LineId
	 * @param context
	 * @param lineid
	 */
	public final void setLineId(IContext context, Long lineid)
	{
		getMendixObject().setValue(context, MemberNames.LineId.toString(), lineid);
	}

	/**
	 * @return value of LineNo
	 */
	public final Long getLineNo()
	{
		return getLineNo(getContext());
	}

	/**
	 * @param context
	 * @return value of LineNo
	 */
	public final Long getLineNo(IContext context)
	{
		return (Long) getMendixObject().getValue(context, MemberNames.LineNo.toString());
	}

	/**
	 * Set value of LineNo
	 * @param lineno
	 */
	public final void setLineNo(Long lineno)
	{
		setLineNo(getContext(), lineno);
	}

	/**
	 * Set value of LineNo
	 * @param context
	 * @param lineno
	 */
	public final void setLineNo(IContext context, Long lineno)
	{
		getMendixObject().setValue(context, MemberNames.LineNo.toString(), lineno);
	}

	/**
	 * @return value of Quantity
	 */
	public final Long getQuantity()
	{
		return getQuantity(getContext());
	}

	/**
	 * @param context
	 * @return value of Quantity
	 */
	public final Long getQuantity(IContext context)
	{
		return (Long) getMendixObject().getValue(context, MemberNames.Quantity.toString());
	}

	/**
	 * Set value of Quantity
	 * @param quantity
	 */
	public final void setQuantity(Long quantity)
	{
		setQuantity(getContext(), quantity);
	}

	/**
	 * Set value of Quantity
	 * @param context
	 * @param quantity
	 */
	public final void setQuantity(IContext context, Long quantity)
	{
		getMendixObject().setValue(context, MemberNames.Quantity.toString(), quantity);
	}

	/**
	 * @return value of Line_Product
	 */
	public final orders.proxies.Products getLine_Product() throws CoreException
	{
		return getLine_Product(getContext());
	}

	/**
	 * @param context
	 * @return value of Line_Product
	 */
	public final orders.proxies.Products getLine_Product(IContext context) throws CoreException
	{
		orders.proxies.Products result = null;
		IMendixIdentifier identifier = getMendixObject().getValue(context, MemberNames.Line_Product.toString());
		if (identifier != null)
			result = orders.proxies.Products.load(context, identifier);
		return result;
	}

	/**
	 * Set value of Line_Product
	 * @param line_product
	 */
	public final void setLine_Product(orders.proxies.Products line_product)
	{
		setLine_Product(getContext(), line_product);
	}

	/**
	 * Set value of Line_Product
	 * @param context
	 * @param line_product
	 */
	public final void setLine_Product(IContext context, orders.proxies.Products line_product)
	{
		if (line_product == null)
			getMendixObject().setValue(context, MemberNames.Line_Product.toString(), null);
		else
			getMendixObject().setValue(context, MemberNames.Line_Product.toString(), line_product.getMendixObject().getId());
	}

	/**
	 * @return value of Order_Lines
	 */
	public final orders.proxies.Orders getOrder_Lines() throws CoreException
	{
		return getOrder_Lines(getContext());
	}

	/**
	 * @param context
	 * @return value of Order_Lines
	 */
	public final orders.proxies.Orders getOrder_Lines(IContext context) throws CoreException
	{
		orders.proxies.Orders result = null;
		IMendixIdentifier identifier = getMendixObject().getValue(context, MemberNames.Order_Lines.toString());
		if (identifier != null)
			result = orders.proxies.Orders.load(context, identifier);
		return result;
	}

	/**
	 * Set value of Order_Lines
	 * @param order_lines
	 */
	public final void setOrder_Lines(orders.proxies.Orders order_lines)
	{
		setOrder_Lines(getContext(), order_lines);
	}

	/**
	 * Set value of Order_Lines
	 * @param context
	 * @param order_lines
	 */
	public final void setOrder_Lines(IContext context, orders.proxies.Orders order_lines)
	{
		if (order_lines == null)
			getMendixObject().setValue(context, MemberNames.Order_Lines.toString(), null);
		else
			getMendixObject().setValue(context, MemberNames.Order_Lines.toString(), order_lines.getMendixObject().getId());
	}

	/**
	 * @return the IMendixObject instance of this proxy for use in the Core interface.
	 */
	public final IMendixObject getMendixObject()
	{
		return orderLinesMendixObject;
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
			final orders.proxies.OrderLines that = (orders.proxies.OrderLines) obj;
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
		return "Orders.OrderLines";
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
