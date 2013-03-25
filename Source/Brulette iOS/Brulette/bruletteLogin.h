//
//  bruletteLogin.h
//  Brulette
//
//  Created by Stuart Varrall on 22/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "bruletteTeam.h"

@protocol bruletteDataDelegate <NSObject>

-(void)returnTeamMembersWithTeamId:(NSArray*)teamMemberArray;
-(void)returnMemberId:(int)membershipId;

@end

@interface bruletteLogin : UITableViewController <UITableViewDataSource, UITableViewDelegate>
{
	NSMutableArray *items;
	UITableView *teamTableView;
	NSString *auth_token;

	//id<bruletteDataDelegate> delegate;
}

@property (nonatomic, assign) id delegate;
-(void)returnTeamMembersWithTeamId:(NSArray*)teamMemberArray;

@property(retain)UITableView *teamTableView;

-(void)registerUser;
-(void)loginUser;
-(void)deleteUser;

-(void)getTeams;
-(void)addTeam;
-(void)deleteTeamWithId:(NSString *)teamId;
-(void)joinTeamWithSlug:(NSString *)slug;
-(void)joinTeamWithId:(int)teamId password:(NSString*)password;

-(void)leaveTeamWithMembershipId:(NSString*)membershipId;

-(void)getTeamBySlug:(NSString *)slug;

-(void)updateTeamMembershipWithId:(NSString *)membershipId active:(NSString*)active;

-(void)startRoundWithTeamId:(NSString *)teamId;

-(NSArray*)returnTeams;
-(bruletteTeam*)returnTeam:(int)teamId;

-(void)processRequest:(NSString *)postBody HTTPMethod:(NSString *)HTTPMethod selector:(NSString*)selector url:(NSURL*)url;

-(void)processUser:(NSDictionary*)response;
-(void)processDelete:(NSDictionary*)response;
-(void)processGetTeams:(NSDictionary*)response;
-(void)processJoinTeamWithSlug:(NSDictionary*)response;
-(void)processJoinTeamWithId:(NSDictionary*)response;
-(void)processLeaveTeam:(NSDictionary*)response;
-(void)processGetTeamBySlug:(NSDictionary*)response;

-(void)processStartRound:(NSDictionary*)response;

@end
