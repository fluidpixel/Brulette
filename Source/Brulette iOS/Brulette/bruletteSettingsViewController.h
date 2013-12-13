//
//  bruletteSettingsViewController.h
//  Brulette
//
//  Created by Stuart Varrall on 04/09/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BruletteData.h"

@interface bruletteSettingsViewController : UIViewController
{
	BruletteData* bruletteDataClass;	
}

- (IBAction)saveAction:(id)sender;

@property (weak, nonatomic) IBOutlet UITextField *nameTextField;
@end
