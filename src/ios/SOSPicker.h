//
//  SOSPicker.h
//  SyncOnSet
//
//  Created by Christopher Sullivan on 10/25/13.
//
//

#import <Cordova/CDVPlugin.h>
#import "ELCAlbumPickerController.h"
#import "ELCImagePickerController.h"

@interface SOSPicker : CDVPlugin <ELCImagePickerControllerDelegate, UINavigationControllerDelegate, UIScrollViewDelegate>

@property (copy)   NSString* callbackId;

- (void) getPictures:(CDVInvokedUrlCommand *)command;
- (UIImage*)imageByScalingNotCroppingForSize:(UIImage*)anImage toSize:(CGSize)frameSize;

@property (nonatomic, assign) NSInteger width;
@property (nonatomic, assign) NSInteger height;
@property (nonatomic, assign) NSInteger quality;

@property (nonatomic, assign) NSInteger maxWidthOrHeight;
@property (nonatomic, assign) NSInteger compressQuality ;
@property (nonatomic, assign) NSInteger maxImageByteSize ; // 5mb
@property (nonatomic, assign) NSInteger minNeedcompressByteSize; // 512kb
@property (nonatomic, assign) BOOL autoCrop;

@end
