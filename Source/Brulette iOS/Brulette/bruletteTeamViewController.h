//
//  bruletteTeamViewController.h
//  Brulette
//
//  Created by Stuart Varrall on 24/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "bruletteTeam.h"
#import "bruletteLogin.h"

@interface bruletteTeamViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>
{
	NSArray* teamMembers;
	
	bruletteLogin* bruletteDataClass;
}

@property (weak, nonatomic) IBOutlet UILabel *teamNameLabel;

@property(nonatomic, strong) bruletteLogin* bruletteLogin;
@property (nonatomic, strong) bruletteTeam *bruletteTeam;

- (IBAction)startRoundAction:(id)sender;
- (IBAction)leaveTeamAction:(id)sender;
- (IBAction)deleteTeamAction:(id)sender;


@property (weak, nonatomic) IBOutlet UIButton *leaveTeamButton;
@property (weak, nonatomic) IBOutlet UIButton *deleteTeamButton;
@end
