//
//  bruletteTeam.h
//  Brulette
//
//  Created by Stuart Varrall on 23/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface bruletteTeam : NSObject

// A text description of this item.
@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *slug;
@property (nonatomic, copy) NSString *password;
@property (nonatomic, copy) NSString *updated_at;
@property (nonatomic, copy) NSString *created_at;
@property (nonatomic, copy) NSString *latitude;
@property (nonatomic, copy) NSString *longitude;
@property (nonatomic, copy) NSString *teamId;

// Returns an SHCToDoItem item initialized with the given text.
-(id)initWithName:(NSDictionary*)name;

// Returns an SHCToDoItem item initialized with the given text.
+(id)toDoItemWithName:(NSDictionary*)name;

@end
