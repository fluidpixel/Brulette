//
//  bruletteTeam.m
//  Brulette
//
//  Created by Stuart Varrall on 23/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import "BruletteTeam.h"

@implementation BruletteTeam

-(id)initWithName:(NSDictionary*)team {
    if (self = [super init]) {
		self.name = [team objectForKey:@"name"];
		self.slug = [team objectForKey:@"slug"];
		self.teamId = [[team objectForKey:@"id"] stringValue];
		self.password = [team objectForKey:@"password"];
    }
    return self;
}

+(id)bruletteTeamWithName:(NSDictionary *)team {
    return [[BruletteTeam alloc] initWithName:team];
}

@end

