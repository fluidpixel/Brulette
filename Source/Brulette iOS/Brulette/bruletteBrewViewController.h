//
//  bruletteBrewViewController.h
//  Brulette
//
//  Created by Stuart Varrall on 26/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BruletteData.h"
#import "BruletteBrew.h"

@interface bruletteBrewViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>
{
	NSMutableArray* brews;
	BruletteData* bruletteDataClass;
}


@property (nonatomic, strong) BruletteBrew* bruletteBrew;

- (IBAction)backButtonAction:(id)sender;
@property (weak, nonatomic) IBOutlet UITableView *brewTable;

@end
