//
//  bruletteSettingsViewController.m
//  Brulette
//
//  Created by Stuart Varrall on 04/09/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import "bruletteSettingsViewController.h"
#import "BruletteData.h"

@interface bruletteSettingsViewController ()

@end

@implementation bruletteSettingsViewController

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
	
	[bruletteDataClass updateUser:nil];
	
}

-(void)returnUser:(NSDictionary*)user
{
	NSLog(@"user: %@", user);
	self.nameTextField.text = [user objectForKey:@"name"];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)saveAction:(id)sender
{
	if (self.nameTextField.text.length > 0)
	{
		[bruletteDataClass updateUser:self.nameTextField.text];
	}
	[self.navigationController popViewControllerAnimated:TRUE];
	
}
@end
