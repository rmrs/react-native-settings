
#import "RNSettings.h"

#import <CoreLocation/CoreLocation.h>

NSString *const LOCATION_SETTING = @"LOCATION_SETTING";
NSString *const DISABLED = @"DISABLED";
NSString *const ENABLED = @"ENABLED";

@implementation RNSettings

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

+ (BOOL)requiresMainQueueSetup
{
    return NO;
}

RCT_EXPORT_MODULE();

RCT_REMAP_METHOD(getSetting, getSetting:(NSString *)setting resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)
{
  if ([setting  isEqual: LOCATION_SETTING]) {
    if ([CLLocationManager locationServicesEnabled]) {
      resolve(ENABLED);
    } else {
      resolve(DISABLED);
    }
  } else {
    NSError *error = [NSError errorWithDomain:@"world" code:200 userInfo:nil];
    reject(@"getSetting", @"unhanded setting", error);
  }
}

- (NSDictionary *)constantsToExport
{
  return @{ LOCATION_SETTING: LOCATION_SETTING, DISABLED: DISABLED, ENABLED: ENABLED };
}

@end
