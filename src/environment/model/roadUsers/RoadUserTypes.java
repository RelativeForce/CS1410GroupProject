package environment.model.roadUsers;

import java.util.Random;
import java.util.function.Supplier;

public enum RoadUserTypes {

	SMALL_CAR(0.33, SmallCar::new),

	MOTORBIKE(0.33, Motorbike::new),

	FAMILY_SEDAN(0.33, FamilySedan::new);

	/**
	 * The probability of creating a <code>RoadUser</code>.
	 */
	public final double probability;

	private final Supplier<RoadUser> roadUser;

	private RoadUserTypes(final double Probability, final Supplier<RoadUser> roadUser) {

		this.probability = Probability;
		this.roadUser = roadUser;
	}

	public final RoadUser create() {

		return roadUser.get();
	}

	public final boolean exists() {
		
		double chance = 1.0 / probability;
		
		Random gen = new Random();
		
		
		return gen.nextInt((int)chance) == 0;
	}

}
