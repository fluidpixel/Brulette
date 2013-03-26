//
//  bruletteBrewViewController.m
//  Brulette
//
//  Created by Stuart Varrall on 26/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import "bruletteBrewViewController.h"
#import "bruletteBrewCell.h"

@interface bruletteBrewViewController ()

@end

@implementation bruletteBrewViewController

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
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)backButtonAction:(id)sender
{
	[self.navigationController popViewControllerAnimated:TRUE];
}

#pragma mark - UITableViewDataSource protocol methods

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return brews.count + 1;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	
    NSString *ident = @"brewCell";
    // re-use or create a cell
    bruletteBrewCell *cell = [tableView dequeueReusableCellWithIdentifier:ident forIndexPath:indexPath];
    // find the to-do item for this index
    int index = [indexPath row];
    
	if (index < brews.count)
	{
		//bruletteTeam *item = teamMembers[index];
		NSDictionary *member = brews[index];
		
		// set the text
		cell.brewNameLabel.text = [member objectForKey:@"name"];

		[cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
		
	}
	else
	{
		cell.brewNameLabel.text = @"New Brew";
		[cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
	}
	
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	bruletteBrewCell *selectedCell = (bruletteBrewCell*) [tableView cellForRowAtIndexPath:indexPath];
	
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
	
	int index = [indexPath row];
	
	BruletteBrew *brew = brews[index];
	
	//goto new brew screen
	NSLog(@"brew: %@", brew.name);
	
}


@end
