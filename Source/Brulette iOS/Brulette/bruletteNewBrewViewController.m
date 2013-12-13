//
//  bruletteNewBrewViewController.m
//  Brulette
//
//  Created by Stuart Varrall on 26/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import "bruletteNewBrewViewController.h"

@interface bruletteNewBrewViewController ()

@end

@implementation bruletteNewBrewViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.brew = [[BruletteBrew alloc] init];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
	
	self.screenHeight = self.view.frame.size.height;
	
	bruletteDataClass = [[BruletteData alloc] init];
	[bruletteDataClass setDelegate:self];
	
	

	for (id subView in self.view.subviews)
    {
        if ([subView isKindOfClass:[UITextField class]]) {
            [subView setDelegate:self];
        }
    }
	
	// keyboard notification
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(keyboardWillHide)
												 name:UIKeyboardWillHideNotification
											   object:nil];
	
	if(self.brew)
	{
		[self updateTextFields];
		[self.actionBarButton setTitle:@"Update"];
	}
	else
	{
		self.brew = [[BruletteBrew alloc] init];
		[self.actionBarButton setTitle:@"Create"];
	}
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark keyboard events
- (void)textFieldDidBeginEditing:(UITextField *)textField
{
	NSLog(@"diff: %f", (self.view.frame.size.height - textField.frame.origin.y));
	//self.currentTextField = textField;
	
	if ((self.view.frame.size.height - textField.frame.origin.y) < 255.0)
	{
		[self keyboardWillShow];
	}
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
	[self updateBrew];
	//[self keyboardWillHide];
}


- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch * touch = [touches anyObject];
    if(touch.phase == UITouchPhaseBegan)
	{
        [self.nameField resignFirstResponder];
		[self.drinkField resignFirstResponder];
		[self.methodField resignFirstResponder];
		[self.milkField resignFirstResponder];
		[self.sizeField resignFirstResponder];
		[self.sugarsField resignFirstResponder];
		[self.timeField resignFirstResponder];
		
    }
}

-(void)keyboardWillShow {
    // Animate the current view out of the way
    [UIView animateWithDuration:0.3f animations:^ {
		//[self.view setNeedsUpdateConstraints];
		//[self.view layoutIfNeeded];
        //self.view.frame = CGRectMake(0, -140, 320, self.view.frame.size.height);
		self.view.frame = CGRectMake(0, -180, 320, 480);
    }];
	
}

-(void)keyboardWillHide {
    // Animate the current view back to its original position
    [UIView animateWithDuration:0.3f animations:^ {
		//[self.view setNeedsUpdateConstraints];
		//[self.view layoutIfNeeded];
        self.view.frame = CGRectMake(0, 0, 320, self.screenHeight);
    }];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
	
	[textField resignFirstResponder];
	return YES;
}

#pragma mark actions
-(void)updateBrew
{
	self.brew.name = self.nameField.text;
	self.brew.drink = self.drinkField.text;
	self.brew.method = self.methodField.text;
	self.brew.smooth = self.milkField.text;
	self.brew.size = self.sizeField.text;
	self.brew.sweet = self.sugarsField.text;
	self.brew.time = self.timeField.text;

	NSLog(@"brew: %@", self.brew);
}

-(void)updateTextFields
{
	self.nameField.text = self.brew.name;
	self.drinkField.text = self.brew.drink;
	self.methodField.text = self.brew.method;
	self.milkField.text = self.brew.smooth;
	self.sizeField.text = self.brew.size;
	//self.sugarsField.text = self.brew.sugars;
	//self.sweetenersField.text = self.brew.sweeteners;
	//self.timeField.text = self.brew.time;
}

- (IBAction)backButtonAction:(id)sender
{
	[self.navigationController popViewControllerAnimated:TRUE];
}

- (IBAction)saveButtonAction:(id)sender
{
	[self updateBrew];
	if(self.brew.brew_id)
		[bruletteDataClass updateBrewWithBrew:self.brew];
	else
		[bruletteDataClass newBrewWithBrew:self.brew];
	
	[self performSelector:@selector(pop) withObject:self afterDelay:1.0 ];
	[self.activityIndicator setHidden:NO];
	
}

-(void)pop
{
	[self.activityIndicator setHidden:YES];
	[self.navigationController popViewControllerAnimated:TRUE];
}

@end
