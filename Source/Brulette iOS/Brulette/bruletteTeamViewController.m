//
//  bruletteTeamViewController.m
//  Brulette
//
//  Created by Stuart Varrall on 24/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import "bruletteTeamViewController.h"
#import "bruletteLogin.h"
#import "bruletteMemberCell.h"

@interface bruletteTeamViewController ()

@end

@implementation bruletteTeamViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
	
	bruletteDataClass = [[bruletteLogin alloc] init];
	
	[bruletteDataClass setDelegate:self];
	 
	self.teamNameLabel.text = self.bruletteTeam.name;
	NSLog(@"id: %@", self.bruletteTeam.teamId);
	
	[bruletteDataClass getTeamBySlug:self.bruletteTeam.slug];
	
}

-(void)returnTeamMembersWithTeamId:(NSArray*)teamMemberArray;
{
	NSLog(@"teamMembers: %@", teamMemberArray);
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)leaveTeamAction:(id)sender
{
	[self.bruletteLogin leaveTeamWithMembershipId:self.bruletteTeam.teamId];
	
	//[self performSegueWithIdentifier:@"returnSegue" sender:self];
}

- (IBAction)deleteTeamAction:(id)sender
{
	[self.bruletteLogin deleteTeamWithId:self.bruletteTeam.teamId];
	
	//[self performSegueWithIdentifier:@"returnSegue" sender:self];
}

- (IBAction)startRoundAction:(id)sender
{
	[self.bruletteLogin startRoundWithTeamId:self.bruletteTeam.teamId];
}

#pragma mark - UITableViewDataSource protocol methods

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return teamMembers.count + 1;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	
    NSString *ident = @"memberCell";
    // re-use or create a cell
    bruletteMemberCell *cell = [tableView dequeueReusableCellWithIdentifier:ident forIndexPath:indexPath];
    // find the to-do item for this index
    int index = [indexPath row];
    
	if (index < teamMembers.count)
	{
		bruletteTeam *item = teamMembers[index];
		
		// set the text
		cell.teamMemberLabel.text = item.name;

		[cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
	}
	else
	{
		cell.teamMemberLabel.text = @"New Member";
		[cell setAccessoryType:UITableViewCellAccessoryNone];
	}
	
    return cell;
}

@end
