//
//  bruletteViewController.h
//  Brulette
//
//  Created by Stuart Varrall on 22/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "bruletteLogin.h"

@interface bruletteViewController : UIViewController <UITableViewDelegate>

@property(nonatomic, strong) bruletteLogin* bruletteLogin;
@property (weak, nonatomic) IBOutlet UITableView *teamTableView;

@end
