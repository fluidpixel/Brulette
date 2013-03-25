//
//  bruletteViewController.m
//  Brulette
//
//  Created by Stuart Varrall on 22/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import "bruletteViewController.h"
#import "bruletteLogin.h"
#import "bruletteTeamCell.h"

#import "bruletteTeamViewController.h"

@interface bruletteViewController ()

@end

@implementation bruletteViewController
{
	// an array of to-do items
	bruletteTeam* teamDict;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

	// Do any additional setup after loading the view, typically from a nib.

	self.bruletteLogin = [[bruletteLogin alloc] init];

	[self.bruletteLogin setTeamTableView:self.teamTableView];

	[self.teamTableView setDataSource:self.bruletteLogin];
	//[self.teamTableView setDelegate:self.bruletteLogin];
	[self.teamTableView setDelegate:self];
	
    //self.teamTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    
	NSString *auth_token = [[NSUserDefaults standardUserDefaults] objectForKey:@"auth_token"];
	
	if (!auth_token)
	{
		[self.bruletteLogin registerUser];
		//[self.bruletteLogin deleteUser];
	}
	else
	{
		NSLog(@"auth_token: %@", auth_token);
		
		//on launch set the auth_token to be reused throughout
		//[self.bruletteLogin setAuthentificationToken];
		
		//populate the team table
		[self.bruletteLogin getTeams];
		
	}
	
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{

    if ([[segue identifier] isEqualToString:@"teamPush"])
	{
		bruletteTeamViewController *bruletteTeamViewController = [segue destinationViewController];
		
		bruletteTeamViewController.bruletteTeam = teamDict;
	}
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	bruletteTeamCell *selectedCell = (bruletteTeamCell*) [tableView cellForRowAtIndexPath:indexPath];
	
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
	
	NSLog(@"selected cell: %@", selectedCell.teamName.text);
	if ([selectedCell.teamName.text isEqualToString:@"New Team"])
	{
		[self.bruletteLogin addTeam];
	}
	else
	{
		teamDict = [self.bruletteLogin returnTeam:indexPath.row];
		[self performSegueWithIdentifier:@"teamPush" sender:teamDict];

	}
}

- (IBAction)joinTeam:(id)sender
{
	[self.bruletteLogin joinTeamWithSlug:self.slugTextField.text];
}

/*
-(UIColor*)colorForIndex:(NSInteger) index {
    NSUInteger itemCount = _toDoItems.count - 1;
    float val = ((float)index / (float)itemCount) * 0.2;
    return [UIColor colorWithRed: 0.2 green:val blue: 0.2 alpha:1.0];
}

#pragma mark - UITableViewDataDelegate protocol methods
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 64.0f;
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath {
    cell.backgroundColor = [self colorForIndex:indexPath.row];
}
*/


@end
