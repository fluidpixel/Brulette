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

	//[self.navigationController.navigationBar setBarStyle:UIBarStyleBlack];
	//[self.navigationController.navigationBar setTranslucent:YES];
	
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
		[self.bruletteLogin addTeam];
	}
	else
	{
		teamDict = [self.bruletteLogin returnTeam:indexPath.row];
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
