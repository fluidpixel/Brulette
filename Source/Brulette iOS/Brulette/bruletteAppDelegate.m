//
//  bruletteAppDelegate.m
//  Brulette
//
//  Created by Stuart Varrall on 22/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import "bruletteAppDelegate.h"
#import "UAirship.h"
#import "UAPush.h"

@implementation bruletteAppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    // Override point for customization after application launch.

	//Create Airship options dictionary and add the required UIApplication launchOptions
	NSMutableDictionary *takeOffOptions = [NSMutableDictionary dictionary];
	[takeOffOptions setValue:launchOptions forKey:UAirshipTakeOffOptionsLaunchOptionsKey];
	
	// Call takeOff (which creates the UAirship singleton), passing in the launch options so the
	// library can properly record when the app is launched from a push notification. This call is
	// required.
	//
	// Populate AirshipConfig.plist with your app's info from https://go.urbanairship.com
	[UAirship takeOff:takeOffOptions];
	
	// Set the icon badge to zero on startup (optional)
	[[UAPush shared] resetBadge];
	
	// Register for remote notfications with the UA Library. This call is required.
	[[UAPush shared] registerForRemoteNotificationTypes:(UIRemoteNotificationTypeBadge |
														 UIRemoteNotificationTypeSound |
														 UIRemoteNotificationTypeAlert)];
	
	// Handle any incoming incoming push notifications.
	// This will invoke `handleBackgroundNotification` on your UAPushNotificationDelegate.
	[[UAPush shared] handleNotification:[launchOptions valueForKey:UIApplicationLaunchOptionsRemoteNotificationKey]
					   applicationState:application.applicationState];
	
    return YES;
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    // Updates the device token and registers the token with UA.
    [[UAPush shared] registerDeviceToken:deviceToken];
	
	[[NSUserDefaults standardUserDefaults] setObject:deviceToken forKey:@"deviceToken"];
}

- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error{
    NSLog(@"FAILED TO REGISTER FOR PUSH NOTIFICATIONS");
    NSLog(@"%@", error.userInfo);
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    if ( application.applicationState == UIApplicationStateActive )
	{
		NSLog(@"alert in foreground");
        // app was already in the foreground
		NSString *message = [[userInfo valueForKey:@"aps"] valueForKey:@"alert"];
		UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Want a Brew?" message:message delegate:self cancelButtonTitle: @"OK" otherButtonTitles:nil];
			[alert show];
	}
	else
	{
		NSLog(@"alert in background");

		[application setApplicationIconBadgeNumber:0];
	}
}

- (void)applicationWillResignActive:(UIApplication *)application
{
	// Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
	// Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
	// Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
	// If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
	// Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
	// Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
	 [UAirship land];
	// Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
