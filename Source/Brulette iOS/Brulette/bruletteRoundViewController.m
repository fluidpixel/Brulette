//
//  bruletteRoundViewController.m
//  Brulette
//
//  Created by Stuart Varrall on 06/09/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import "bruletteRoundViewController.h"
#import "bruletteRoundCell.h"

@interface bruletteRoundViewController ()

@end

@implementation bruletteRoundViewController

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

	bruletteDataClass = [[BruletteData alloc] init];
	[bruletteDataClass setDelegate:self];
	
	[self.roundTableView setDataSource:self];
	[self.roundTableView setDelegate:self];

	[bruletteDataClass getRounds];
	[bruletteDataClass getOpenRounds];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - UITableViewDataSource protocol methods

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	if (section == 0) {
		return roundArray.count + 1;
	}
	else
		return closedRoundArray.count;
    
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
	return 2;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    if(section == 0)
        return @"Open Rounds";
    else
        return @"Closed Rounds";
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	
    NSString *ident = @"roundCell";
    // re-use or create a cell
    bruletteRoundCell *cell = [tableView dequeueReusableCellWithIdentifier:ident forIndexPath:indexPath];
    // find the to-do item for this index
    int index = [indexPath row];
    
	if (indexPath.section==0)
	{
		if (index < roundArray.count)
		{
			//bruletteTeam *item = teamMembers[index];
			NSDictionary *round = roundArray[index];
			
			// set the text
			cell.textLabel.text = [[round objectForKey:@"id"] stringValue];
			cell.textLabel.textColor = [UIColor blackColor];
			
			cell.detailTextLabel.text = [round objectForKey:@"closes_at"];
			if ([round objectForKey:@"user_id"] == [[NSUserDefaults standardUserDefaults] objectForKey:@"user_id"])
			{
				[cell setAccessoryType:UITableViewCellAccessoryCheckmark];
			}
			else
			{
				[cell setAccessoryType:UITableViewCellAccessoryNone];
				cell.detailTextLabel.text = @"tap to join";
			}
		}
		else
		{
			cell.textLabel.text = @"Create New Round";
			cell.textLabel.textColor = [[UIColor alloc] initWithRed:107.00 / 255 green:142.00 / 255 blue:35.00 / 255 alpha:1.0];
			cell.detailTextLabel.text = @"";
			[cell setAccessoryType:UITableViewCellAccessoryNone];
	//		[cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
		}
	}else //closed rounds
	{
		//bruletteTeam *item = teamMembers[index];
		NSDictionary *round = closedRoundArray[index];
		
		// set the text
		cell.textLabel.text = [[round objectForKey:@"id"] stringValue];
		cell.detailTextLabel.text = [round objectForKey:@"closes_at"];
		
		if ([round objectForKey:@"user_id"] == [[NSUserDefaults standardUserDefaults] objectForKey:@"user_id"])
		{
			[cell setAccessoryType:UITableViewCellAccessoryCheckmark];
		}
		else
		{
			[cell setAccessoryType:UITableViewCellAccessoryNone];
		}
	}
	
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	bruletteRoundCell *selectedCell = (bruletteRoundCell*) [tableView cellForRowAtIndexPath:indexPath];
	
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
	
	int index = [indexPath row];
	
	if ([selectedCell.textLabel.text isEqualToString:@"Create New Round"])
	{
		[bruletteDataClass startRoundWithTeamId:self.team_id];
	}
	else
	{
		
		NSDictionary *round = roundArray[index];
		[bruletteDataClass joinRound:[[round objectForKey:@"id"] stringValue]];
		
		/*
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
		 */
	}
}


#pragma mark Delegate Methods
-(void)returnRound:(NSDictionary *)round
{
	[bruletteDataClass getRounds];
	[bruletteDataClass getOpenRounds];

	[self.roundTableView reloadData];
	NSLog(@"round: %@", round);
}

-(void)returnRounds:(NSArray *)closedRounds
{
	closedRoundArray = [[NSMutableArray alloc] init];
	closedRoundArray = [NSMutableArray arrayWithArray:closedRounds];
	
	[self.roundTableView reloadData];
}

-(void)returnOpenRounds:(NSArray *)openRoundArray
{
	roundArray = [[NSMutableArray alloc] init];
	roundArray = [NSMutableArray arrayWithArray:openRoundArray];
	[self.roundTableView reloadData];
}


@end
