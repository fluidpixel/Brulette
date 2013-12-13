//
//  bruletteBrewViewController.m
//  Brulette
//
//  Created by Stuart Varrall on 26/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import "bruletteBrewViewController.h"
#import "bruletteBrewCell.h"
#import "bruletteNewBrewViewController.h"

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
	
	[bruletteDataClass getUsersBrews:@""];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
	
	[bruletteDataClass getUsersBrews:@""];
    [self.brewTable reloadData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)selectedBrew
{
	
    if ([[segue identifier] isEqualToString:@"newBrewSegue"])
	{
		bruletteNewBrewViewController *bruletteNewBrewViewController = [segue destinationViewController];
		
		bruletteNewBrewViewController.brew = selectedBrew;
	}
}

-(void)returnBrews:(NSArray*)brewArray
{
	brews = [[NSMutableArray alloc] init];
	brews = [NSMutableArray arrayWithArray:brewArray];
	
	[self.brewTable reloadData];
}

#pragma mark Button Actions
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
		BruletteBrew *brew = brews[index];
		
		// set the text
		cell.brewNameLabel.text = brew.name;
		cell.brewNameLabel.textColor = [UIColor blackColor];
		[cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
		
	}
	else
	{
		cell.brewNameLabel.text = @"New Brew";
		cell.brewNameLabel.textColor = [[UIColor alloc] initWithRed:107.00 / 255 green:142.00 / 255 blue:35.00 / 255 alpha:1.0];
		[cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
	}
	
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	//bruletteBrewCell *selectedCsell = (bruletteBrewCell*) [tableView cellForRowAtIndexPath:indexPath];
	
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
	
	int index = [indexPath row];
	
	if (brews.count > index)
	{
		BruletteBrew *brew = brews[index];
		NSLog(@"brew: %@", brew.name);
		[self performSegueWithIdentifier:@"newBrewSegue" sender:brew];
	}
	else
		[self performSegueWithIdentifier:@"newBrewSegue" sender:nil];
	
	//goto new brew screen
	
	
	
	
}

#pragma mark Deleting of Brews
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath;
{
	return YES;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
	BruletteBrew *brew = brews[[indexPath row]];
	
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        //add code here for when you hit delete
		NSLog(@"delete id: %@", brew.name);
		
		[bruletteDataClass deleteBrew:[brew.brew_id stringValue]];
		
    }
}

@end
