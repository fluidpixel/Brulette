//
//  BruletteBrew.m
//  Brulette
//
//  Created by Stuart Varrall on 25/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import "BruletteBrew.h"

@implementation BruletteBrew

-(id)initWithBrew:(NSDictionary*)brew {
    if (self = [super init]) {
		self.name = [brew objectForKey:@"name"];
		self.drink = [brew objectForKey:@"drink"];
		self.method = [brew objectForKey:@"method"];
		self.milk = [brew objectForKey:@"milk"];
		self.size = [brew objectForKey:@"size"];
		self.sugars = [brew objectForKey:@"sugars"];
		self.sweeteners = [brew objectForKey:@"sweeteners"];
		self.time = [brew objectForKey:@"time"];
    }
    return self;
}

+(id)bruletteBrewWithBrew:(NSDictionary *)brew
{
    return [[BruletteBrew alloc] initWithBrew:brew];
}

@end
