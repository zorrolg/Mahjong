package com.citywar.type;

public enum DistanceType {
	
	DISTANCE_100KM(1,100000),DISTANCE_1000KM(2,1000000),WORLD(3,Double.MAX_VALUE);
	
	private DistanceType (int id,double distance){
		this.id = id;
		this.distance = distance;
	}
	int id;
	
	double distance;
	
    public static DistanceType valueOf(int id)
	{
		for (DistanceType function : values())
		{
			if (function.id == id) { return function; }
		}

		throw new IllegalArgumentException(" illegal DistanceType id:" + id);
	}
    
    public static DistanceType getValueByDistance(double distance)
  	{
  		for (DistanceType function : values())
  		{
  			if (function.distance == distance) { return function; }
  		}
  		return WORLD;
  	}

	public double getDistance() {
		return distance;
	}
}
