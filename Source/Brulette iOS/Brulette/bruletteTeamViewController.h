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
	NSMutableArray* teamMembers;
	int membership_id;
	bruletteLogin* bruletteDataClass;
	int currentRound;
}

@property (weak, nonatomic) IBOutlet UILabel *teamNameLabel;

@property (nonatomic, strong) bruletteTeam *bruletteTeam;

@property (weak, nonatomic) IBOutlet UITableView *memberTable;

- (IBAction)backAction:(id)sender;

- (IBAction)joinRoundAction:(id)sender;
- (IBAction)startRoundAction:(id)sender;
- (IBAction)leaveTeamAction:(id)sender;
- (IBAction)deleteTeamAction:(id)sender;


@property (weak, nonatomic) IBOutlet UIButton *joinRoundButton;

@property (weak, nonatomic) IBOutlet UIButton *leaveTeamButton;
@property (weak, nonatomic) IBOutlet UIButton *deleteTeamButton;
@end
