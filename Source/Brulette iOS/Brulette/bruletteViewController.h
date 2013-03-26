//
//  bruletteViewController.h
//  Brulette
//
//  Created by Stuart Varrall on 22/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BruletteData.h"

@interface bruletteViewController : UIViewController <UITableViewDelegate, UITableViewDataSource,  UITextFieldDelegate>
{
	NSArray *items;
	BruletteData* bruletteDataClass;
}

@property (weak, nonatomic) IBOutlet UITableView *teamTableView;
@property (weak, nonatomic) IBOutlet UITextField *slugTextField;
- (IBAction)joinTeam:(id)sender;
- (IBAction)newBrew:(id)sender;

@property (nonatomic) float screenHeight;

@end
