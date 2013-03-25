//
//  bruletteLocationManager.h
//  Brulette
//
//  Created by Stuart Varrall on 25/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>

@interface bruletteLocationManager : NSObject <CLLocationManagerDelegate>

@property (nonatomic, strong) CLLocationManager* locationManager;
@property (nonatomic) BOOL mapError;


+ (bruletteLocationManager*)sharedSingleton;

- (float) distanceToPharmacyLatitude:(NSString *)pharmacyLatitude distanceToPharmacyLongitude:(NSString *)pharmacyLongitude postCodeLocation:(CLLocation *)postCodeLocation;
- (CLLocation *)pharmacyLocationLatitude:(NSString*)pharmacyLatitude pharmacyLocationLongitude:(NSString*)pharmacyLongitude;


- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error;
- (void)locationManager:(CLLocationManager *)manager didUpdateHeading:(CLHeading *)newHeading;

@end
