//
//  bruletteViewController.m
//  Brulette
//
//  Created by Stuart Varrall on 22/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import "bruletteViewController.h"
#import "bruletteLogin.h"

@interface bruletteViewController ()

@end

@implementation bruletteViewController
{
	// an array of to-do items
	NSMutableArray* _toDoItems;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.

	self.bruletteLogin = [[bruletteLogin alloc] init];

	[self.bruletteLogin setTeamTableView:self.teamTableView];

	[self.teamTableView setDataSource:self.bruletteLogin];
	[self.teamTableView setDelegate:self.bruletteLogin];
	
	NSString *auth_token = [[NSUserDefaults standardUserDefaults] objectForKey:@"auth_token"];
	
	if (!auth_token)
	{
		[self.bruletteLogin registerUser];
		//[self.bruletteLogin deleteUser];
	}
	else
	{
		NSLog(@"auth_token: %@", auth_token);
		[self.bruletteLogin getTeams];
		
		
	}
	
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
