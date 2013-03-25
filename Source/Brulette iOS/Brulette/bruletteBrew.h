//
//  bruletteBrew.h
//  Brulette
//
//  Created by Stuart Varrall on 25/03/2013.
//  Copyright (c) 2013 Fluid Pixel Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface bruletteBrew : NSObject

@property (nonatomic, copy) NSString *drink;
@property (nonatomic, copy) NSString *method;
@property (nonatomic, copy) NSString *milk;
@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *size;
@property (nonatomic, copy) NSString *sugars;
@property (nonatomic, copy) NSString *sweeteners;
@property (nonatomic, copy) NSString *time;

-(id)initWithBrew:(NSDictionary*)brew;
+(id)bruletteBrewWithBrew:(NSDictionary *)brew;

@end
