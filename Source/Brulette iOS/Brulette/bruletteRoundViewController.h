//
//  bruletteRoundViewController.h
//  Brulette
//
//  Created by Stuart Varrall on 06/09/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BruletteData.h"

@interface bruletteRoundViewController : UIViewController <UITableViewDelegate, UITableViewDataSource, bruletteDataDelegate>
{
	NSMutableArray* roundArray;
	NSMutableArray* closedRoundArray;
	
	BruletteData* bruletteDataClass;
}
@property (weak, nonatomic) IBOutlet UITableView *roundTableView;

@property (nonatomic, strong) NSString* team_id;

@end
