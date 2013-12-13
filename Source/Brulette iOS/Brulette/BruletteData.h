//
//  bruletteLogin.h
//  Brulette
//
//  Created by Stuart Varrall on 22/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BruletteTeam.h"
#import "bruletteBrew.h"

@protocol bruletteDataDelegate <NSObject>

-(void)returnTeamMembersWithTeamId:(NSArray*)teamMemberArray;
-(void)returnMemberId:(int)membershipId;
-(void)returnTeams:(NSArray*)teamArray;
-(void)returnRound:(NSDictionary*)round;
-(void)returnRounds:(NSArray*)roundArray;
-(void)returnOpenRounds:(NSArray*)roundArray;
-(void)returnBrews:(NSArray*)brewArray;

@end

@interface BruletteData : NSObject
{
	NSMutableArray *items;
	UITableView *teamTableView;
	NSString *auth_token;
	NSString *user_id;

	//id<bruletteDataDelegate> delegate;
}


@property (nonatomic, assign) id delegate;
-(void)returnTeamMembersWithTeamId:(NSArray*)teamMemberArray;
-(void)returnMemberId:(int)membershipId;
-(void)returnRound:(NSDictionary *)round;
-(void)returnBrews:(NSArray *)brews;
-(void)returnUser:(NSDictionary *)user;

@property(retain)UITableView *teamTableView;

-(void)registerUser;
-(void)loginUser;
-(void)deleteUser;
-(void)getUser;
-(void)updateUser:(NSString*)name;

-(void)getTeams;
-(void)addTeam;
-(void)deleteTeamWithId:(NSString *)teamId;
-(void)joinTeamWithSlug:(NSString *)slug;
-(void)joinTeamWithId:(int)teamId password:(NSString*)password;

-(void)leaveTeamWithMembershipId:(NSString*)membershipId;

-(void)getTeamBySlug:(NSString *)slug;

-(void)updateTeamMembershipWithId:(NSString *)membershipId active:(NSString*)active;

-(void)getRounds;
-(void)getOpenRounds;
-(void)joinRound:(NSString*)round_id;
-(void)startRoundWithTeamId:(NSString *)teamId;

-(void)getUsersBrews:(NSString *)brewType;
-(void)newBrewWithBrew:(BruletteBrew *)brew;
-(void)newBrewWithRound:(int)round;
-(void)updateBrewWithBrew:(BruletteBrew *)brew;
-(void)deleteBrew:(NSString*)brew_id;

-(BruletteTeam*)returnTeam:(int)teamId;

-(void)processRequest:(NSString *)postBody HTTPMethod:(NSString *)HTTPMethod selector:(NSString*)selector url:(NSURL*)url;

-(void)processUser:(NSDictionary*)response;
-(void)processDelete:(NSDictionary*)response;
-(void)processGetTeams:(NSDictionary*)response;
-(void)processJoinTeamWithSlug:(NSDictionary*)response;
-(void)processJoinTeamWithId:(NSDictionary*)response;
-(void)processLeaveTeam:(NSDictionary*)response;
-(void)processGetTeamBySlug:(NSDictionary*)response;

-(void)processStartRound:(NSDictionary*)response;
-(void)processJoinedRound:(NSDictionary*)response;
-(void)processGetRounds:(NSDictionary*)response;
-(void)processGetOpenRounds:(NSDictionary*)response;

@end
