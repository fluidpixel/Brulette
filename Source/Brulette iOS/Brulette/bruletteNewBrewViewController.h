//
//  bruletteNewBrewViewController.h
//  Brulette
//
//  Created by Stuart Varrall on 26/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BruletteData.h"
#import "BruletteBrew.h"

@interface bruletteNewBrewViewController : UIViewController <UITextFieldDelegate>
{
	BruletteData* bruletteDataClass;
}

@property (nonatomic, strong) BruletteBrew* brew;

@property (nonatomic) float screenHeight;

@property (weak, nonatomic) IBOutlet UITextField *nameField;
@property (weak, nonatomic) IBOutlet UITextField *drinkField;
@property (weak, nonatomic) IBOutlet UITextField *methodField;
@property (weak, nonatomic) IBOutlet UITextField *milkField;
@property (weak, nonatomic) IBOutlet UITextField *sizeField;
@property (weak, nonatomic) IBOutlet UITextField *sugarsField;
@property (weak, nonatomic) IBOutlet UITextField *sweetenersField;
@property (weak, nonatomic) IBOutlet UITextField *timeField;

@property (weak, nonatomic) IBOutlet UIBarButtonItem *actionBarButton;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *activityIndicator;

- (IBAction)backButtonAction:(id)sender;
- (IBAction)saveButtonAction:(id)sender;

@end
