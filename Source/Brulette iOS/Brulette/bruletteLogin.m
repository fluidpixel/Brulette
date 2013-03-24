//
//  bruletteLogin.m
//  Brulette
//
//  Created by Stuart Varrall on 22/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import "bruletteLogin.h"
#import "bruletteTeamCell.h"
#import "bruletteTeam.h"

#define BgQueue dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)
#define usersURL [NSURL URLWithString:@"http://salty-plains-8447.herokuapp.com/api/users"]
#define teamsURL [NSURL URLWithString:@"http://salty-plains-8447.herokuapp.com/api/teams"]

@implementation bruletteLogin
@synthesize teamTableView;

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
			NSLog(@"response: %@", data);
			SEL theSelector = NSSelectorFromString(selector);
			
			[self performSelectorOnMainThread:theSelector withObject:data waitUntilDone:YES];

		}
		
	});

}

#pragma mark User Requests
-(void)registerUser
{
	
//http://salty-plains-8447.herokuapp.com/api/users -X POST --data "user[email]=jm@ideonic.com&user[password]=password&user[password_confirmation]=password&user[name]=jerrymiah"

	NSString *deviceToken = [[[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"] description];
	deviceToken = [deviceToken stringByTrimmingCharactersInSet:[NSCharacterSet characterSetWithCharactersInString:@"<>"]];
	deviceToken = [deviceToken stringByReplacingOccurrencesOfString:@" " withString:@""];
	
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
							  deviceToken,
							  email,
							  @"password",
							  @"password",
							  @"ios",
							  deviceToken];
		
		[self processRequest:postBody HTTPMethod:@"POST" selector:@"processUser:" url:usersURL];
	}
}
-(void)deleteUser
{
	//http://salty-plains-8447.herokuapp.com/api/users -X DELETE --data "auth_token=@"gzMtCP7zL1evbyQTTGFN""
	
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@", @"P4ajzpzU4z4bnfCG86Ys"];
	
	[self processRequest:postBody HTTPMethod:@"DELETE" selector:@"processDelete:" url:usersURL];
	
}

#pragma mark Team Requests
-(void)getTeams
{
//	curl -X GET https://salty-plains-8447.herokuapp.com/api/teams --data "auth_token=MjpgZsA5npo1s3tsq6jn"
	
	NSString *auth_token = [[NSUserDefaults standardUserDefaults] objectForKey:@"auth_token"];
	
	NSString *postBody = [NSString stringWithFormat:@"auth_token=%@", auth_token];
	
	[self processRequest:postBody HTTPMethod:@"GET" selector:@"processGetTeams:" url:teamsURL];
}


#pragma mark Process Responses
-(void)processUser:(NSDictionary*)response
{
	//P4ajzpzU4z4bnfCG86Ys
	NSString* auth_token = [response objectForKey:@"auth_token"];
	[[NSUserDefaults standardUserDefaults] setObject:auth_token forKey:@"auth_token"];
}

-(void)processDelete:(NSDictionary*)response
{
	NSLog(@"user deleted");
}

-(void)processGetTeams:(NSDictionary*)response
{
	NSLog(@"process team response");
	items = nil;
	items = [[NSMutableArray alloc] init];
	
	NSArray *teamArray = [response objectForKey:@"entries"];
	
	for(int i = 0; i < [teamArray count]; i++)
	{
		NSDictionary* teamDict = [teamArray objectAtIndex:i];
		[items addObject:[bruletteTeam toDoItemWithName:teamDict]];
	}
	
	//reload the table in main view with data
	[teamTableView reloadData];

}

#warning these shouldn't be here. Move to view controller
#pragma mark - UITableViewDataSource protocol methods

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return items.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	
    NSString *ident = @"bruletteTeamCell";
    // re-use or create a cell
    bruletteTeamCell *cell = [tableView dequeueReusableCellWithIdentifier:ident forIndexPath:indexPath];
    // find the to-do item for this index
    int index = [indexPath row];
    bruletteTeam *item = items[index];
	
    // set the text
	cell.teamName.text = item.name;
	cell.teamSlug.text = item.slug;
	
    return cell;
}

@end
