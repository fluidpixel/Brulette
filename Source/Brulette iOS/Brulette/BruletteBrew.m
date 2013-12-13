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
		NSArray* tagArray = [brew objectForKey:@"brew_tags"];
		
		for (NSDictionary* tag in tagArray)
		{
			NSString* category = [tag objectForKey:@"category"];
			if ([category isEqualToString:@"drink"])
				self.drink = [tag objectForKey:@"name"];
			else if ([category isEqualToString:@"method"])
				self.method = [tag objectForKey:@"name"];
			else if ([category isEqualToString:@"smooth"])
				self.smooth = [tag objectForKey:@"name"];
			else if ([category isEqualToString:@"size"])
				self.size = [tag objectForKey:@"name"];
			else if ([category isEqualToString:@"sweet"])
				self.sweet = [tag objectForKey:@"name"];
			else if ([category isEqualToString:@"time"])
				self.time = [tag objectForKey:@"name"];
		}
		
		self.name = [brew objectForKey:@"name"];
		self.brew_id = [brew objectForKey:@"id"];
    }
    return self;
}

+(id)bruletteBrewWithBrew:(NSDictionary *)brew
{
    return [[BruletteBrew alloc] initWithBrew:brew];
}

@end
