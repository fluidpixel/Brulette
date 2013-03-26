//
//  bruletteTeamViewController.m
//  Brulette
//
//  Created by Stuart Varrall on 24/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import "bruletteTeamViewController.h"
#import "BruletteData.h"
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
	
	bruletteDataClass = [[BruletteData alloc] init];
	
	[bruletteDataClass setDelegate:self];
	 
	self.teamNameLabel.text = self.bruletteTeam.name;
	NSLog(@"id: %@", self.bruletteTeam.teamId);
	
	[bruletteDataClass getTeamBySlug:self.bruletteTeam.slug];
	
}

-(void)returnTeamMembersWithTeamId:(NSArray*)teamMemberArray
{
	teamMembers = [[NSMutableArray alloc] init];
	teamMembers = [NSMutableArray arrayWithArray:teamMemberArray];
	
	[self.memberTable reloadData];
}

-(void)returnMemberId:(int)membershipId
{
	membership_id = membershipId;
}

-(void)returnRound:(NSDictionary*)round
{
	currentRound = [[round objectForKey:@"id"] intValue];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)leaveTeamAction:(id)sender
{

	[bruletteDataClass leaveTeamWithMembershipId:[NSString stringWithFormat:@"%i", membership_id]];
	
	//[self performSegueWithIdentifier:@"returnSegue" sender:self];
}

- (IBAction)deleteTeamAction:(id)sender
{
	[bruletteDataClass deleteTeamWithId:self.bruletteTeam.teamId];
	
	//[self performSegueWithIdentifier:@"returnSegue" sender:self];
}

- (IBAction)backAction:(id)sender
{
	[self.navigationController popViewControllerAnimated:TRUE];
}

- (IBAction)joinRoundAction:(id)sender
{
	[bruletteDataClass newBrewWithRound:currentRound];
}

- (IBAction)startRoundAction:(id)sender
{
	[bruletteDataClass startRoundWithTeamId:self.bruletteTeam.teamId];
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
		//bruletteTeam *item = teamMembers[index];
		NSDictionary *member = teamMembers[index];
		
		// set the text
		cell.teamMemberLabel.text = [member objectForKey:@"user_name"];
		if ([member objectForKey:@"active"])
		{
			[cell setAccessoryType:UITableViewCellAccessoryCheckmark];
		}
		else
		{
			[cell setAccessoryType:UITableViewCellAccessoryNone];
		}

	}
	else
	{
		cell.teamMemberLabel.text = @"New Member";
		[cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
	}
	
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	bruletteMemberCell *selectedCell = (bruletteMemberCell*) [tableView cellForRowAtIndexPath:indexPath];
	
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
	
	int index = [indexPath row];
	
	NSDictionary *member = teamMembers[index];
	
	if ([selectedCell.teamMemberLabel.text isEqualToString:@"New Member"])
	{
		//[self.bruletteLogin addTeam];
	}
	else
	{
		if(selectedCell.accessoryType == UITableViewCellAccessoryCheckmark)
		{
			[selectedCell setAccessoryType:UITableViewCellAccessoryNone];
			[bruletteDataClass updateTeamMembershipWithId:[[member objectForKey:@"id"] stringValue] active:@"false"];
		}
		else
		{
			[selectedCell setAccessoryType:UITableViewCellAccessoryCheckmark];
			[bruletteDataClass updateTeamMembershipWithId:[[member objectForKey:@"id"] stringValue] active:@"true"];
		}
	}
}


@end
