package environment;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Represents a statistic about the station. This also encapsulates the
 * behaviour of the retrieving the value of a statistic using
 * {@link #get(Class)} and collating the value of the total statistic using
 * {@link #sum()}.
 * 
 * @param <SuperType>
 *            The super type that all the entries of the {@link Map} must be
 *            subtype of in order to be stored in this {@link Statistic}.
 * 
 * @author Joshua_Eddy
 * @version 27/04/2017
 * 
 * @see environment.model.Station
 * @see environment.GUI.views.Graph
 *
 */
public class Statistic<SuperType> implements Cloneable {

	// Instance Fields --------------------------------------------------------

	/**
	 * The
	 * <code>{@link Map}&lt;{@link Class}&lt;? extends {@link SuperType}&gt;, {@link Double}&gt;</code>
	 * which stores the {@link Double} value of a statistic. The {@link Class}
	 * of a sub-class of {@link SuperType} is the key of this {@link Map}. The
	 * {@link Double} value of this {@link Map} denotes combined value that type
	 * of the key {@link SuperType} it is assigned to.
	 * 
	 * @see #sum(Map)
	 * @see #update(Map, Class, double)
	 */
	private Map<Class<? extends SuperType>, Double> statMap;

	// Constructor ------------------------------------------------------------

	/**
	 * Constructs a new Statistic.
	 */
	public Statistic() {

		this.statMap = new HashMap<Class<? extends SuperType>, Double>();

	}

	// Public Methods ---------------------------------------------------------

	/**
	 * Changes the value (<code>double</code>) of a statistic assigned to a
	 * specific {@link SuperType} type
	 * (<code>Class&lt;? extends {@link SuperType}&gt;</code>) by a specified
	 * amount in the the parameter statistic map (<code>{@link Map}</code>). If
	 * the parameter {@link SuperType} type and assigned value are not currently
	 * stored in the statistic map then they will be added.
	 * 
	 * @param stat
	 *            <code>{@link Map}&lt;Class&lt;? extends {@link SuperType}&gt;, {@link Double}&gt;</code>
	 *            that denotes some statistic. NOT NULL
	 * @param type
	 *            <code>Class&lt;? extends {@link SuperType}&gt;</code> that
	 *            denotes the type of {@link SuperType} that is assigned to a to
	 *            the value that is to be updated.
	 * @param amount
	 *            <code>double</code> that denotes the change in the value.
	 */
	public final void update(Class<? extends SuperType> type, double amount) {

		// If the specified statistic map is not null.
		if (statMap != null) {

			// If the specified statistic map has no statistics on road users of
			// the specified type. Then initialise the statistic of the
			// specified type.
			if (!statMap.keySet().contains(type)) {
				statMap.put(type, 0.0);
			}

			// Iterate through all the road user types in the specified
			// statistic map
			for (Class<? extends SuperType> currentType : statMap.keySet()) {

				// If the current type is the same as that of the specified
				// type.
				if (currentType == type) {

					// Initialise a Double variable to hold the current value of
					// the statistic assigned to the specified sub type.
					Double currentStat = statMap.get(type);

					// Add the specified amount to that value.
					currentStat += amount;

					// Update the value of the statistic in the specified
					// statistic map.
					statMap.replace(currentType, currentStat);
				}

			}
		}

	}

	/**
	 * Retrieves the value of <code>this</code> {@link Statistic} by
	 * {@link SuperType} sub type. If the {@link SuperType} sub type type does
	 * not have a value stored in the {@link Statistic} return zero. If the
	 * specified key is NULL return the {@link #sum()}.
	 * 
	 * @param key
	 *            <code>Class&lt;? extends {@link SuperType}&gt;</code>
	 * @return The <code>double</code> value assigned to that specified key.
	 */
	public final double get(Class<? extends SuperType> key) {

		// If the key is null assume that they want the sum of all the elements
		// in the statMap.
		// Otherwise, if the key is present in the statMap then retrieve its
		// assigned value.
		// Otherwise, return zero as that key is not present in the statMap.
		if (key == null) {
			return sum();
		} else if (statMap.containsKey(key)) {
			return statMap.get(key);
		} else {
			return 0.0;
		}
	}

	@Override
	public final Statistic<SuperType> clone() {

		// Create a new Statistic that will act as a clone of this.
		Statistic<SuperType> clone = new Statistic<SuperType>();

		// Iterate through all the elements in the statMap and put them into the
		// statMap of the clone.
		for (Class<? extends SuperType> key : this.statMap.keySet()) {
			clone.statMap.put(key, this.statMap.get(key).doubleValue());
		}

		return clone;

	}

	/**
	 * Sums all the values ({@link Double}) of this statistic {@link Map}
	 * {@link SuperType} type groups to yield one total value.
	 * 
	 * @return The <code>double</code> sum of all the values in this statistic
	 *         map.
	 */
	public final double sum() {

		// Initialise an aggregation variable to store the sum of the statistic
		// maps values.
		Double total = 0.0;

		// If the statistic map is not equal to null then iterate through all
		// the values in the map and sum them.
		if (statMap != null) {
			for (Class<? extends SuperType> currentType : statMap.keySet()) {
				total += statMap.get(currentType);

			}
		}
		return total;

	}
}
