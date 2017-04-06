package environment.model;

import java.util.HashMap;
import java.util.Map;

import environment.model.roadusers.RoadUser;

/**
 * 
 * Represents a statistic about the station. This also encapsulates the
 * behaviour of the retrieving the value of a statistic using
 * {@link #get(Class)} and collating the value of the total statistic using
 * {@link #sum()}.
 * 
 * @author Joshua_Eddy
 * @version 06/04/2017
 * 
 * @see environment.model.Station
 *
 */
public class Statistic implements Cloneable {

	// Instance Fields --------------------------------------------------------

	/**
	 * The
	 * <code>{@link Map}&lt;{@link Class}&lt;? extends {@link RoadUser}&gt;, {@link Double}&gt;</code>
	 * which stores the {@link Double} value of a statistic of the station. The
	 * {@link Class} of a sub-class of {@link RoadUser} is the key of this
	 * {@link Map}. The {@link Double} value of this {@link Map} denotes
	 * combined value that type of the key {@link RoadUser} it is assigned to.
	 * 
	 * @see environment.model.roadusers.vehicles.Vehicle
	 * @see environment.model.roadusers.RoadUser
	 * @see #sum(Map)
	 * @see #update(Map, Class, double)
	 */
	private Map<Class<? extends RoadUser>, Double> statMap;

	// Constructor ------------------------------------------------------------

	/**
	 * Constructs a new Statistic.
	 */
	public Statistic() {

		this.statMap = new HashMap<Class<? extends RoadUser>, Double>();

	}

	// Public Methods ---------------------------------------------------------

	/**
	 * Changes the value (<code>double</code>) of a statistic assigned to a
	 * specific {@link RoadUser} type
	 * (<code>Class&lt;? extends {@link RoadUser}&gt;</code>) by a specified
	 * amount in the the parameter statistic map (<code>{@link Map}</code>). If
	 * the parameter {@link RoadUser} type and assigned value are not currently
	 * stored in the statistic map then they will be added.
	 * 
	 * @param stat
	 *            <code>{@link Map}&lt;Class&lt;? extends {@link RoadUser}&gt;, {@link Double}&gt;</code>
	 *            that denotes some statistic about the {@link Station}. For
	 *            example the amount of profit made from fuel sales grouped by
	 *            {@link RoadUser} type. NOT NULL
	 * @param type
	 *            <code>Class&lt;? extends {@link RoadUser}&gt;</code> that
	 *            denotes the type of {@link RoadUser} that is assigned to a to
	 *            the value that is to be updated.
	 * @param amount
	 *            <code>double</code> that denotes the change in the value.
	 */
	public final void update(Class<? extends RoadUser> type, double amount) {

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
			for (Class<? extends RoadUser> currentType : statMap.keySet()) {

				// If the current type is the same as that of the specified
				// type.
				if (currentType == type) {

					// Initialise a Double variable to hold the current value of
					// the statistic assigned to the specified road user type.
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
	 * {@link RoadUser} type. If the {@link RoadUser} type does not have a value
	 * stored in the {@link Statistic} return zero.
	 * 
	 * @param key
	 *            <code>Class&lt;? extends {@link RoadUser}&gt;</code>
	 * @return The <code>double</code> value assigned to that specified key.
	 */
	public final double get(Class<? extends RoadUser> key) {
		if (statMap.containsKey(key)) {
			return statMap.get(key);
		} else {
			return 0.0;
		}
	}

	@Override
	public final Statistic clone() {

		// Create a new Statistic that will act as a clone of this.
		Statistic clone = new Statistic();

		// Iterate through all the elements in the statMap and put them into the
		// statMap of the clone.
		for (Class<? extends RoadUser> key : this.statMap.keySet()) {
			clone.statMap.put(key, this.statMap.get(key).doubleValue());
		}

		return clone;

	}

	/**
	 * Sums all the values ({@link Double}) of this statistic {@link Map}
	 * {@link RoadUser} type groups to yield one total value.
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
			for (Class<? extends RoadUser> currentType : statMap.keySet()) {
				total += statMap.get(currentType);

			}
		}
		return total;

	}
}
