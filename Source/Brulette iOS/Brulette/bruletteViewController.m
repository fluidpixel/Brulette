//
//  bruletteViewController.m
//  Brulette
//
//  Created by Stuart Varrall on 22/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import "bruletteViewController.h"
#import "BruletteData.h"
#import "bruletteTeamCell.h"

#import "bruletteTeamViewController.h"

@interface bruletteViewController ()

@end

@implementation bruletteViewController
{
	// an array of to-do items
	BruletteTeam* teamDict;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

	bruletteDataClass = [[BruletteData alloc] init];
	
	[bruletteDataClass setDelegate:self];
	
	//[self.navigationController.navigationBar setBarStyle:UIBarStyleBlack];
	//[self.navigationController.navigationBar setTranslucent:YES];
	
	// Do any additional setup after loading the view, typically from a nib.
	
	[self.teamTableView setDataSource:self];
	[self.teamTableView setDelegate:self];
	
    //self.teamTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    
	NSString *auth_token = [[NSUserDefaults standardUserDefaults] objectForKey:@"auth_token"];
	
	if (!auth_token)
	{
		[bruletteDataClass registerUser];
		//[bruletteDataClass deleteUser];
	}
	else
	{
		NSLog(@"auth_token: %@", auth_token);
		
		//on launch set the auth_token to be reused throughout
		//[bruletteDataClass setAuthentificationToken];
		
		//populate the team table
		[bruletteDataClass getTeams];
		
	}
	
	[self.slugTextField setDelegate:self];
	
	self.screenHeight = self.view.frame.size.height;
	
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
	
	[self.slugTextField resignFirstResponder];
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
	
	NSLog(@"selected cell: %@", selectedCell.teamName.text);
	if ([selectedCell.teamName.text isEqualToString:@"New Team"])
	{
		[bruletteDataClass addTeam];
	}
	else
	{
		teamDict = [bruletteDataClass returnTeam:indexPath.row];
		[self performSegueWithIdentifier:@"teamPush" sender:teamDict];

	}
}

#pragma mark text view responders
- (void)textFieldDidBeginEditing:(UITextField *)textField
{

	[UIView animateWithDuration:0.3f animations:^ {
		//[self.view setNeedsUpdateConstraints];
		//[self.view layoutIfNeeded];
        self.view.frame = CGRectMake(0, -220, self.view.frame.size.width, self.view.frame.size.height);
		//self.view.frame = CGRectMake(0, -120, 320, 480);
    }];
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
	
    // Animate the current view back to its original position
    [UIView animateWithDuration:0.3f animations:^ {
		//[self.view setNeedsUpdateConstraints];
		//[self.view layoutIfNeeded];
        self.view.frame = CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height);
    }];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
	
	[textField resignFirstResponder];
	return YES;
}

- (void)keyboardWasShown:(NSNotification*)aNotification
{
    NSDictionary* info = [aNotification userInfo];
    CGSize kbSize = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
	NSLog(@"keyboardsize: %f", kbSize.height);
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    UITouch * touch = [touches anyObject];
    if(touch.phase == UITouchPhaseBegan) {
        [self.slugTextField resignFirstResponder];
    }
}

#pragma mark Button Actions
- (IBAction)joinTeam:(id)sender
{
	[bruletteDataClass joinTeamWithSlug:self.slugTextField.text];
}

- (IBAction)newBrew:(id)sender {
}

-(void)returnTeamMembersWithTeamId:(NSArray*)teamArray;
{
	items = [[NSMutableArray alloc] init];
	items = [NSMutableArray arrayWithArray:teamArray];
	
	[self.teamTableView reloadData];
	
}

#pragma mark - UITableViewDataSource protocol methods

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return items.count + 1;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	
    NSString *ident = @"bruletteTeamCell";
    // re-use or create a cell
    bruletteTeamCell *cell = [tableView dequeueReusableCellWithIdentifier:ident forIndexPath:indexPath];
    // find the to-do item for this index
    int index = [indexPath row];
    
	if (index < items.count)
	{
		BruletteTeam *item = items[index];
		
		// set the text
		cell.teamName.text = item.name;
		cell.teamSlug.text = item.slug;
		[cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
	}
	else
	{
		cell.teamName.text = @"New Team";
		cell.teamSlug.text = @"";
		[cell setAccessoryType:UITableViewCellAccessoryNone];
	}
	
    return cell;
}


#pragma mark Deleting of Teams
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return YES if you want the specified item to be editable.
    return YES;
}

// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
	BruletteTeam *item = items[[indexPath row]];
	
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        //add code here for when you hit delete
		NSLog(@"delete id: %@", item.teamId);
		
		[bruletteDataClass deleteTeamWithId:item.teamId];
		
    }
}

@end
