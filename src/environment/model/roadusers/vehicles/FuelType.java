package environment.model.roadusers.vehicles;

import java.util.Random;

/**
 * The {@link FuelType} enumeration is a list of the types of fuel that can be
 * used by a {@link Vehicle} to model the different costs of fuel.
 * 
 * @author John_Berg
 * @version 31/03/2017
 * @since 31/03/2017
 */
public enum FuelType {

	/**
	 * The Petrol {@link FuelType} entry.
	 */
	PETROL(1.20);

	/**
	 * The cost representation of the {@link FuelType} per gallon.
	 */
	public final double price;

	/**
	 * Initialise the {@link FuelType} with a specified price.
	 * 
	 * <p>
	 * This constructor is only used to initialise the enumerations.
	 * </p>
	 * 
	 * @param price
	 *            The price per gallon of the {@link FuelType}.
	 */
	private FuelType(final double price) {

		this.price = price;
	}

	/**
	 * Select a {@link FuelType} enumeration at random.
	 * 
	 * <p>
	 * Randomly select a listed {@link FuelType}, a {@link Random} object must
	 * be provided to select the {@link FuelType}; however, if the provided
	 * {@link Random} is null, then <code>null</code> is returned as the
	 * {@link FuelType} cannot be selected without a {@link Random} object.
	 * </p>
	 * 
	 * @param rng
	 *            The {@link Random} object used to select the {@link FuelType}.
	 * @return A randomly selected {@link FuelType} unless the provided argument
	 *         is null, in that case return null.
	 * @see Random
	 */
	public static final FuelType generateFuelType(final Random rng) {

		/*
		 * Check that the rng is not null, if so then select a FuelType using
		 * the rng, otherwise, return null.
		 */

		return rng != null ? FuelType.values()[rng.nextInt(FuelType.values().length)] : null;
	}
}