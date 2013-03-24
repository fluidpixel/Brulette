//
//  bruletteLogin.h
//  Brulette
//
//  Created by Stuart Varrall on 22/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface bruletteLogin : UITableViewController <UITableViewDataSource, UITableViewDelegate>
{
	NSMutableArray *items;
	UITableView *teamTableView;
}

@property(retain)UITableView *teamTableView;

-(void)registerUser;
-(void)deleteUser;
-(void)getTeams;

-(void)processRequest:(NSString *)postBody HTTPMethod:(NSString *)HTTPMethod selector:(NSString*)selector url:(NSURL*)url;

-(void)processUser:(NSDictionary*)response;
-(void)processDelete:(NSDictionary*)response;
-(void)processGetTeams:(NSDictionary*)response;

@end
