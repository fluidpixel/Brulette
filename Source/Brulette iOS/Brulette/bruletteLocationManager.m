//
//  bruletteLocationManager.m
//  Brulette
//
//  Created by Stuart Varrall on 25/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import "bruletteLocationManager.h"

@implementation bruletteLocationManager

- (id)init {
    self = [super init];
	
    if(self) {
        self.locationManager = [CLLocationManager new];
        [self.locationManager setDelegate:self];
        [self.locationManager setDistanceFilter:kCLDistanceFilterNone];
        [self.locationManager setHeadingFilter:kCLHeadingFilterNone];
        [self.locationManager startUpdatingLocation];
        //do any more customization to your location manager
    }
	
    return self;
}

+ (bruletteLocationManager*)sharedSingleton {
    static bruletteLocationManager* sharedSingleton;
    if(!sharedSingleton) {
        @synchronized(sharedSingleton) {
            sharedSingleton = [bruletteLocationManager new];
        }
    }
	
    return sharedSingleton;
}

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation {
    //handle your location updates here
}

- (float) distanceToPharmacyLatitude:(NSString *)pharmacyLatitude distanceToPharmacyLongitude:(NSString *)pharmacyLongitude postCodeLocation:(CLLocation *)postCodeLocation
{
	
	float pharmacyDistance = 0.f;
	
	if ([pharmacyLatitude class] != [NSNull class])
	{
		CLLocation *pharmacyLocation = [[CLLocation alloc] initWithLatitude:[pharmacyLatitude floatValue] longitude:[pharmacyLongitude floatValue]];
		
		if (postCodeLocation)
		{
			pharmacyDistance = [pharmacyLocation distanceFromLocation:postCodeLocation] / 1000; //m into km
		}
		else if (self.mapError)
		{
			//pharmacyDistance = [pharmacyLocation distanceFromLocation:[bLocationManager sharedSingleton].locationManager.location] / 1000; //m into km
			pharmacyDistance = -1.f;
		}
		else
		{
			pharmacyDistance = [pharmacyLocation distanceFromLocation:self.locationManager.location] / 1000; //m into km
		}
	}
	
	return pharmacyDistance;
}

- (CLLocation *) pharmacyLocationLatitude:(NSString *)pharmacyLatitude pharmacyLocationLongitude:(NSString *)pharmacyLongitude
{
	if ([pharmacyLatitude class] != [NSNull class])
	{
		CLLocation *pharmacyLocation = [[CLLocation alloc] initWithLatitude:[pharmacyLatitude floatValue] longitude:[pharmacyLongitude floatValue]];
		
		return pharmacyLocation;
	}
	else{
		return  nil;
	}
	
}

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
	NSLog(@"map error: %@", error);
	
	self.mapError = TRUE;
	
}

- (void)locationManager:(CLLocationManager *)manager didUpdateHeading:(CLHeading *)newHeading {
    //handle your heading updates here- I would suggest only handling the nth update, because they
    //come in fast and furious and it takes a lot of processing power to handle all of them
}


@end
