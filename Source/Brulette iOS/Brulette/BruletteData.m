//
//  bruletteLogin.m
//  Brulette
//
//  Created by Stuart Varrall on 22/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import "BruletteData.h"
#import "bruletteTeamCell.h"
#import "BruletteTeam.h"
#import "bruletteLocationManager.h"
#import "BruletteBrew.h"

#define BgQueue dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)
#define usersURL [NSURL URLWithString:@"https://salty-plains-8447.herokuapp.com/api/users"]
#define teamsURL [NSURL URLWithString:@"https://salty-plains-8447.herokuapp.com/api/teams"]
#define membershipURL [NSURL URLWithString:@"https://salty-plains-8447.herokuapp.com/api/memberships"]
#define roundsURL [NSURL URLWithString:@"https://salty-plains-8447.herokuapp.com/api/rounds"]
#define ordersURL [NSURL URLWithString:@"https://salty-plains-8447.herokuapp.com/api/orders"]
#define brewUrl [NSURL URLWithString:@"https://salty-plains-8447.herokuapp.com/api/brews"]

@implementation BruletteData
@synthesize teamTableView;
//@synthesize delegate;

- (id)init {
    self = [super init];
	
    if(self) {
        auth_token = [[NSUserDefaults standardUserDefaults] objectForKey:@"auth_token"];
		user_id = [[NSUserDefaults standardUserDefaults] objectForKey:@"user_id"];
    }
    return self;
}

#pragma mark Delegate Calls
-(void)returnTeamMembersWithTeamId:(NSArray*)teamMemberArray
{
	if([self delegate] != nil)
		[[self delegate] returnTeamMembersWithTeamId:teamMemberArray];
}

-(void)returnMemberId:(int)membershipId
{
	if([self delegate] != nil)
		[[self delegate] returnMemberId:membershipId];
}

-(void)returnTeams:(NSArray *)teamArray
{
	if([self delegate] != nil)
		[[self delegate] returnTeamMembersWithTeamId:teamArray];

}

-(void)returnRound:(NSDictionary *)round
{
	if([self delegate] != nil)
		[[self delegate] returnRound:round];
}

-(void)returnRounds:(NSArray *)roundArray
{
	if([self delegate] != nil)
		[[self delegate] returnRounds:roundArray];
}

-(void)returnOpenRounds:(NSArray *)openRoundArray
{
	if([self delegate] != nil)
		[[self delegate] returnOpenRounds:openRoundArray];
}

-(void)returnBrews:(NSArray *)brewArray
{
	if([self delegate] != nil)
		[[self delegate] returnBrews:brewArray];
	
}

-(void)returnUser:(NSDictionary *)user
{
	if([self delegate] != nil)
		[[self delegate] returnUser:user];

}

-(BruletteTeam*)returnTeam:(int)teamID
{
	return [items objectAtIndex:teamID];
}

-(NSString*)returnDeviceToken
{
	NSString *deviceToken = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
	
	deviceToken = [deviceToken stringByTrimmingCharactersInSet:[NSCharacterSet characterSetWithCharactersInString:@"<>"]];
	deviceToken = [deviceToken stringByReplacingOccurrencesOfString:@" " withString:@""];
	deviceToken = [deviceToken stringByReplacingOccurrencesOfString:@"-" withString:@""];
	
	return deviceToken;
}


#pragma mark requests
-(void)processRequest:(NSString *)postBody HTTPMethod:(NSString *)HTTPMethod selector:(NSString*)selector url:(NSURL*)url
{
	NSData *postData = [postBody dataUsingEncoding:NSUTF8StringEncoding allowLossyConversion:YES];
	NSString *postLength = [NSString stringWithFormat:@"%d", [postData length]];
	
	NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
	
	if ([HTTPMethod isEqualToString:@"GET"])
	{
		url = [NSURL URLWithString:[NSString stringWithFormat:@"%@?%@", url, postBody]];
	}
	else
	{
		[request setHTTPBody:postData];
		[request setHTTPMethod:HTTPMethod];
		[request setValue:postLength forHTTPHeaderField:@"Content-Length"];
		[request setValue:@"application/x-www-form-urlencoded;charset=UTF-8" forHTTPHeaderField:@"Content-Type"];

	}
	
	NSLog(@"call: %@", selector);
	NSLog(@"HTTPMethod: %@", HTTPMethod);
	NSLog(@"url: %@", url);
	NSLog(@"postData: %@", postBody);
	
	[request setURL:url];
	
	dispatch_async(BgQueue, ^{
		
		NSError *requestError;
		NSURLResponse *urlResponse = nil;
		NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:&urlResponse error:&requestError];
		
		//Return Value
		//The downloaded data for the URL request. Returns nil if a connection could not be created or if the download fails.
		if (response == nil) {
			// Check for problems
			if (requestError != nil) {
				NSLog(@"url error: %@", requestError);
			}
			
			//return nil;
		}
		else
		{
			
			NSDictionary* data = [NSJSONSerialization JSONObjectWithData:response
																 options:kNilOptions
																   error:&requestError];
			if (data)
			{
				NSLog(@"response: %@", data);
				SEL theSelector = NSSelectorFromString(selector);
				
				[self performSelectorOnMainThread:theSelector withObject:data waitUntilDone:YES];
			}
			else //not a json array therefore somethings probably gone wrong so extract error
			{
				NSString *errorString = [[NSString alloc] initWithData:response encoding:NSASCIIStringEncoding];
				NSLog(@"response: %@", errorString);
			}
		}
		
	});

}

#pragma mark User Requests
-(void)registerUser
{
//http://salty-plains-8447.herokuapp.com/api/users -X POST --data "user[email]=jm@ideonic.com&user[password]=password&user[password_confirmation]=password&user[name]=jerrymiah"

	NSString *deviceToken = [self returnDeviceToken];
	
	if (!deviceToken)
	{
		NSLog(@"deviceToken: null");
		
		UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"No Notifications" message:@"We can't send you brew alerts without push notifications" delegate:self cancelButtonTitle: @"OK" otherButtonTitles:nil];
		[alert show];
	}
	else
	{
		//NSString *deviceToken = @"BD1995566B198494F889D8071596078136CD7E8E468157CB3769C81408030A2D";
		
		NSString *email = [NSString stringWithFormat:@"%@@fpstudios.com", deviceToken];
		
		NSString *postBody = [NSString stringWithFormat:@"user[name]=%@&user[email]=%@&user[password]=%@&user[password_confirmation]=%@&user[device_provider]=%@&user[device_token]=%@",
							  [deviceToken substringToIndex:6],
							  email,
							  @"password",
							  @"password",
							  @"ios",
							  deviceToken];
		
		[self processRequest:postBody HTTPMethod:@"POST" selector:@"processUser:" url:usersURL];
	}
}
-(void)loginUser
{
	//curl https://salty-plains-8447.herokuapp.com/api/users/login --data "email=jeremiah@ideonic.com&password=password"
	
	NSString *deviceToken = [self returnDeviceToken];
	
//	NSString *email = [NSString stringWithFormat:@"%@@fpstudios.com", deviceToken];
	NSString *email = @"169d8dc790fb4fd1b0fe1255ddaf90f7@fpstudios.com";
	NSString *postBody = [NSString stringWithFormat:@"email=%@&password=%@", email, @"password"];
	NSURL* url = [usersURL URLByAppendingPathComponent:@"login"];
	
	[self processRequest:postBody HTTPMethod:@"POST" selector:@"processLoginUser:" url:url];
}

-(void)deleteUser
{
	//http://salty-plains-8447.herokuapp.com/api/users -X DELETE --data "auth_token=@"gzMtCP7zL1evbyQTTGFN""
	
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@", auth_token];
	
	[self processRequest:postBody HTTPMethod:@"DELETE" selector:@"processDelete:" url:usersURL];
	
}

-(void)getUser
{
	//curl -X GET https://salty-plains-8447.herokuapp.com/api/users --data "auth_token=LGGPPzt5Kr9RxVjdnegi&user[name]=jeremiahAlex&user[current_password]=password"
	
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@", auth_token];
	
	NSURL* url = [usersURL URLByAppendingPathComponent:[NSString stringWithFormat:@"/%@", user_id]];
	
	[self processRequest:postBody HTTPMethod:@"GET" selector:@"processGetUser:" url:url];
	
}

-(void)updateUser:(NSString*)name
{
	//curl -X PUT https://salty-plains-8447.herokuapp.com/api/users --data "auth_token=LGGPPzt5Kr9RxVjdnegi&user[name]=jeremiahAlex&user[current_password]=password"
	
	NSString *postBody = nil;
	if(name)
		postBody = [NSString stringWithFormat:@"auth_token=%@&user[current_password]=password&user[name]=%@", auth_token, name];
	else
		postBody = [NSString stringWithFormat:@"auth_token=%@&user[current_password]=password", auth_token];
	
	[self processRequest:postBody HTTPMethod:@"PUT" selector:@"processUpdate:" url:usersURL];
	
}

#pragma mark Team Requests
-(void)getTeams
{
//	curl -X GET https://salty-plains-8447.herokuapp.com/api/teams --data "auth_token=MjpgZsA5npo1s3tsq6jn"
	
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@", auth_token];
	
	[self processRequest:postBody HTTPMethod:@"GET" selector:@"processGetTeams:" url:teamsURL];
}

-(void)addTeam
{
	//	curl -X POST https://salty-plains-8447.herokuapp.com/api/teams --data "auth_token=MjpgZsA5npo1s3tsq6jn&team[name]=myTeam"
	
	NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
	[dateFormatter setDateFormat:@"HH"];
	int hour = [[dateFormatter stringFromDate:[NSDate date]] intValue];
	
	NSString* teamName = [NSString stringWithFormat:@"Best Team %i", hour];
	CLLocation *userLocation = [bruletteLocationManager sharedSingleton].locationManager.location;
	
	NSString* locationString = @"";
	NSLog(@"userLocation: %@", userLocation);
	
	if (userLocation)
	{
		locationString = [NSString stringWithFormat:@"&team[latitude]=%g&team[longitude]=%g", userLocation.coordinate.latitude, userLocation.coordinate.longitude];
	}
	
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@&team[name]=%@%@", auth_token, teamName, locationString];
	
	[self processRequest:postBody HTTPMethod:@"POST" selector:@"processAddTeam:" url:teamsURL];
}

-(void)leaveTeamWithMembershipId:(NSString *)membershipId
{
	//curl -X DELETE https://salty-plains-8447.herokuapp.com/api/memberships/2 --data "auth_token=MjpgZsA5npo1s3tsq6jn"
	
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@", auth_token];
	NSURL* url = [membershipURL URLByAppendingPathComponent:membershipId];
	
	[self processRequest:postBody HTTPMethod:@"DELETE" selector:@"processLeaveTeam:" url:url];
}


-(void)deleteTeamWithId:(NSString*)teamId
{
	//curl -X DELETE https://salty-plains-8447.herokuapp.com/api/teams/5 --data "auth_token=VZpRUA4yq6FkoT68Hx9y"
	
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@", auth_token];
	NSURL* url = [teamsURL URLByAppendingPathComponent:teamId];
	
	[self processRequest:postBody HTTPMethod:@"DELETE" selector:@"processDeleteTeam:" url:url];
}

-(void)joinTeamWithSlug:(NSString *)slug
{
	//GET https://salty-plains-8447.herokuapp.com/api/teams/R9DvlhWz
	
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@", auth_token];
	
	NSURL* url = [teamsURL URLByAppendingPathComponent:slug];
	[self processRequest:postBody HTTPMethod:@"GET" selector:@"processJoinTeamWithSlug:" url:url];
	
}

-(void)joinTeamWithId:(int)teamId password:(NSString *)password
{
//	curl https://salty-plains-8447.herokuapp.com/api/memberships --data "auth_token=MjpgZsA5npo1s3tsq6jn&team_id=2&password=pass"
//curl https://salty-plains-8447.herokuapp.com/api/memberships --data "auth_token=RzHBzUYgEnvKEFzHytsP&team_id=12&password="
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@&team_id=%i&password=%@", auth_token, teamId, @""];
	//NSString *postBody = [NSString stringWithFormat:@"auth_token=%@&team_id=%i", auth_token, teamId];
	
	[self processRequest:postBody HTTPMethod:@"POST" selector:@"processJoinTeamWithId:" url:membershipURL];
}

-(void)getTeamBySlug:(NSString *)slug
{
	//curl -X GET https://salty-plains-8447.herokuapp.com/api/teams/kSZlQfwT --data "auth_token=MjpgZsA5npo1s3tsq6jn"
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@", auth_token];
	NSURL* url = [teamsURL URLByAppendingPathComponent:slug];
	
	[self processRequest:postBody HTTPMethod:@"GET" selector:@"processGetTeamBySlug:" url:url];
}

-(void)updateTeamMembershipWithId:(NSString *)membershipId active:(NSString *)active
{
	//curl -X PUT https://salty-plains-8447.herokuapp.com/api/memberships/2 --data "auth_token=MjpgZsA5npo1s3tsq6jn&membership[active]=false"
	
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@&membership[active]=%@", auth_token, active];
	NSURL* url = [membershipURL URLByAppendingPathComponent:membershipId];
	
	[self processRequest:postBody HTTPMethod:@"PUT" selector:@"processUpdateTeamMembership:" url:url];
	
}

-(void)getUsersBrews:(NSString *)brewType
{
//	curl -X GET https://salty-plains-8447.herokuapp.com/api/brews --data "auth_token=MjpgZsA5npo1s3tsq6jn&drink=Coffee"
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@&drink=%@", auth_token, brewType];
	
	[self processRequest:postBody HTTPMethod:@"GET" selector:@"processGetUsersBrews:" url:brewUrl];
}

-(void)newBrewWithBrew:(BruletteBrew *)brew
{
	//curl https://salty-plains-8447.herokuapp.com/api/brews --data "auth_token=hwKT1x5jYpFwxtnpusB3&brew_tags=tea:drink,sugar:sweet"
	NSString *brewString = [NSString stringWithFormat:@"brew[name]=%@&brew_tags=%@:drink,%@:method,%@:sweet,%@:smooth,%@:time", brew.name, brew.drink, brew.method, brew.sweet, brew.smooth, brew.time];
	
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@&%@", auth_token, brewString];
	
	[self processRequest:postBody HTTPMethod:@"POST" selector:@"processNewBrew:" url:brewUrl];
}

-(void)newBrewWithRound:(int)round
{
	//curl https://localhost:3000/api/brews --data "auth_token=MjpgZsA5npo1s3tsq6jn&round_id=12&brew[drink]=Coffee&brew[method]=No preference&brew[milk]=Skimmed&brew[name]=jBrew&brew[size]=S&brew[sugars]=1&brew[sweeteners]=1&brew[time]=100"
	
	NSString *brewString = [NSString stringWithFormat:@"round_id=%i%@", round,  @"&brew[drink]=Coffee&brew[method]=No preference&brew[milk]=Skimmed&brew[name]=jBrew&brew[size]=S&brew[sugars]=1&brew[sweeteners]=1&brew[time]=100"];
	
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@&%@", auth_token, brewString];
	
	[self processRequest:postBody HTTPMethod:@"POST" selector:@"processNewBrew:" url:brewUrl];
}

-(void)updateBrewWithBrew:(BruletteBrew *)brew
{
	//curl -X PUT https://salty-plains-8447.herokuapp.com/api/brews/10 --data "auth_token=LGGPPzt5Kr9RxVjdnegi&brew_tags=tea:drink,sugar:sweet"
	NSString *brewString = [NSString stringWithFormat:@"brew[name]=%@&brew_tags=%@:drink,%@:method,%@:sweet,%@:smooth,%@:time", brew.name, brew.drink, brew.method, brew.sweet, brew.smooth, brew.time];
	
	NSURL* url = [brewUrl URLByAppendingPathComponent:[brew.brew_id stringValue]];
	
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@&%@", auth_token, brewString];
	
	[self processRequest:postBody HTTPMethod:@"PUT" selector:@"processNewBrew:" url:url];
}
-(void)deleteBrew:(NSString*)brew_id
{
	//curl -X DELETE https://salty-plains-8447.herokuapp.com/api/teams/5 --data "auth_token=VZpRUA4yq6FkoT68Hx9y"
	
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@", auth_token];
	NSURL* url = [brewUrl URLByAppendingPathComponent:brew_id];
	
	[self processRequest:postBody HTTPMethod:@"DELETE" selector:@"processDeleteBrew:" url:url];
}

#pragma mark Round Requests

-(void)getRounds
{
	//curl -X GET https://salty-plains-8447.herokuapp.com/api/rounds --data "auth_token=MjpgZsA5npo1s3tsq6jn&status=Open"

	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@", auth_token];
	
	[self processRequest:postBody HTTPMethod:@"GET" selector:@"processGetRounds:" url:roundsURL];
}

-(void)getOpenRounds
{
	//curl -X GET https://salty-plains-8447.herokuapp.com/api/rounds --data "auth_token=MjpgZsA5npo1s3tsq6jn&status=Open"
	
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@&status=open", auth_token];
	
	[self processRequest:postBody HTTPMethod:@"GET" selector:@"processGetOpenRounds:" url:roundsURL];
}

-(void)joinRound:(NSString*)round_id
{
	//curl https://salty-plains-8447.herokuapp.com/api/orders --data "auth_token=hwKT1x5jYpFwxtnpusB3&round_id=1&brew_id=20"
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@&round_id=%@&brew_id=86", auth_token, round_id];
	
	[self processRequest:postBody HTTPMethod:@"POST" selector:@"processJoinedRound:" url:ordersURL];
	
}
-(void)startRoundWithTeamId:(NSString *)teamId
{
	//curl POST https://localhost:3000/api/rounds --data "auth_token=MjpgZsA5npo1s3tsq6jn&team_id=1&volunteer=false&time=100"
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@&team_id=%@&volunteer=false&time=100", auth_token, teamId];
	
	[self processRequest:postBody HTTPMethod:@"POST" selector:@"processStartRound:" url:roundsURL];
}

#pragma mark Process Responses
-(void)processUser:(NSDictionary*)response
{
	NSString* responseString = [[response objectForKey:@"email"] objectAtIndex:0];
	
	if([responseString isEqualToString:@"has already been taken"])
	{
		[self loginUser];
	}
	else
	{
		auth_token = [response objectForKey:@"auth_token"];
		NSDictionary* user = [response objectForKey:@"user"];
		user_id = [user objectForKey:@"id"];
		
		[[NSUserDefaults standardUserDefaults] setObject:auth_token forKey:@"auth_token"];
		[[NSUserDefaults standardUserDefaults] setObject:user_id forKey:@"user_id"];
	}
}

-(void)processLoginUser:(NSDictionary*)response
{
	NSLog(@"process login user");
	auth_token = [response objectForKey:@"auth_token"];
	user_id = [response objectForKey:@"user_id"];
	
	[[NSUserDefaults standardUserDefaults] setObject:auth_token forKey:@"auth_token"];
	[[NSUserDefaults standardUserDefaults] setObject:user_id forKey:@"user_id"];
	
	[self getTeams];
}

-(void)processGetUser:(NSDictionary *)response
{
	NSLog(@"get user");
	NSDictionary* user = [response objectForKey:@"user"];
	[self returnUser:user];
}

-(void)processDelete:(NSDictionary*)response
{
	NSLog(@"user deleted");
}

-(void)processUpdate:(NSDictionary*)response
{
	NSLog(@"user updated");
	NSDictionary* user = [response objectForKey:@"entry"];
	[self returnUser:user];
}

-(void)processGetTeams:(NSDictionary*)response
{
	NSLog(@"process Get team");
	items = nil;
	items = [[NSMutableArray alloc] init];
	
	NSArray *teamArray = [response objectForKey:@"entries"];
	
	for(int i = 0; i < [teamArray count]; i++)
	{
		NSDictionary* teamDict = [teamArray objectAtIndex:i];
		[items addObject:[BruletteTeam bruletteTeamWithName:teamDict]];
	}
	
	[self returnTeams:items];
}

-(void)processGetRounds:(NSDictionary*)response
{
	NSLog(@"process Get rounds");
	items = nil;
	items = [[NSMutableArray alloc] init];
	
	NSArray *roundArray = [response objectForKey:@"entries"];
	for (NSDictionary* round in roundArray)
	{
		[items addObject:round];
	}
	
	[self returnRounds:items];
}

-(void)processGetOpenRounds:(NSDictionary*)response
{
	NSLog(@"process Get Open rounds");
	items = nil;
	items = [[NSMutableArray alloc] init];
	
	NSArray *roundArray = [response objectForKey:@"entries"];
	for (NSDictionary* round in roundArray)
	{
		[items addObject:round];
	}
	
	[self returnOpenRounds:items];
}

-(void)processJoinedRound:(NSDictionary*)response
{
	NSLog(@"process Join round");
	items = nil;
	items = [[NSMutableArray alloc] init];
	
	NSArray *roundArray = [response objectForKey:@"entries"];
	for (NSDictionary* round in roundArray)
	{
		[items addObject:round];
	}
	
	//[self returnRounds:items];
}


#pragma mark Process Return Results

-(void)processAddTeam:(NSDictionary*)response
{
	NSLog(@"process Add Team");
	//NSArray *teamArray = [response objectForKey:@"entry"];

	NSDictionary* teamDict = [response objectForKey:@"entry"];
	[items addObject:[BruletteTeam bruletteTeamWithName:teamDict]];
	[self getTeams];
}

-(void)processDeleteTeam:(NSDictionary*)response
{
	NSLog(@"Team deleted");

	[self getTeams];
}

-(void)processGetTeamBySlug:(NSDictionary*)response
{
	NSArray *memberArray = [response objectForKey:@"memberships"];
	
	for(NSDictionary* member in memberArray)
	{
		NSLog(@"member_id: %@", [member objectForKey:@"user_id"]);
	}
	
	NSDictionary *user_membership = [response objectForKey:@"user_membership"];
	
	[self returnTeamMembersWithTeamId:memberArray];
	[self returnMemberId:[[user_membership objectForKey:@"id"] intValue]];
}

-(void)processJoinTeamWithSlug:(NSDictionary *)response
{

	NSDictionary* teamDict = [response objectForKey:@"entry"];
	
	[self joinTeamWithId:[[teamDict objectForKey:@"id"] intValue] password:[teamDict objectForKey:@"password"]];
}

-(void)processJoinTeamWithId:(NSDictionary *)response
{
	[self getTeams];
	
	[teamTableView reloadData];
}

-(void)processLeaveTeam:(NSDictionary *)response
{
	NSLog(@"processLeaveTeam");
	
}
-(void)processUpdateTeamMembership:(NSDictionary *)response
{
	NSLog(@"updatedTeamMembership");
}

-(void)processStartRound:(NSDictionary *)response
{
	NSLog(@"round started");
	NSDictionary* round = [response objectForKey:@"entry"];
	[self returnRound:round];
}

-(void)processGetUsersBrews:(NSDictionary *)response
{
	NSLog(@"process Get Brews");

	NSMutableArray* brews = [[NSMutableArray alloc] init];

	NSArray *responseArray = [response objectForKey:@"entries"];
	
	for(NSDictionary* brew in responseArray)
	{
		[brews addObject:[BruletteBrew bruletteBrewWithBrew:brew]];
	}
	
	[self returnBrews:brews];

}

-(void)processNewBrew:(NSDictionary *)response
{
	NSLog(@"brew Added");
}

-(void)processDeleteBrew:(NSDictionary*)response
{
	NSLog(@"Brew deleted");
	
	[self getUsersBrews:nil];
}

@end
