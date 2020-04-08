# Official MySabay SDK for Android

This is the official MySabay SDK for native Android application. To use this SDK, you can follow the guides below or download the test with the example project we have in this repository.

## Create your application

Create your MySabay application if you don't have one yet at [MySabay App Dashboard](https://kh.mysabay.com:8443/index.html) and copy your `appId` 
and `appSecret` for the integration. 

## Installation

Add Jitpack to your project build.gralde file

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Then add this dependency to your app build.gradle file.

```
dependencies {
    implementation 'kh.com.mysabay:sabaysdk:1.0.6-b06'
}
```

## Configuration

MySabaySDK has default configuration with dark theme and sandbox url.

```
final SdkConfiguration configuration = new SdkConfiguration.Builder(
                BuildConfig.APPLICATION_ID, // mysabay app Id
                "SDK sample", //mysabay  app name
                "{MySabayAppSecret}", //MySabay App Secret
                "{LicenseKey}", // license key and required in production
                "{MerchantID}") // merchant id and required in production
                .setSdkTheme(SdkTheme.Light) // optional configure
                .setToUseSandBox(true) // optional configure
                .build();
        MySabaySDK.Impl.setDefaultInstanceConfiguration(configuration);
```
> NOTE: MySabaySdk is need configuration

## Integration

> Note that in order to use the store and checkout function, the user must login first.
> Follow the guide below for each functions provided by the SDK:

*  **Login**

Call 

```
    MySabaySDK.getInstance().showLoginView(new LoginListener() {
                        @Override
                        public void loginSuccess(String accessToken) {
                            MessageUtil.displayToast(v.getContext(), "accessToken = " + accessToken);
                        }
    
                        @Override
                        public void loginFailed(Object error) {
                            MessageUtil.displayToast(v.getContext(), "error = " + error);
                        }
                    });
``` 
to open login screen

* **Show user profile**

```android

    MySabaySDK.getInstance().getUserProfile(new UserInfoListener() {
                                    @Override
                                    public void userInfo(String info) {
                                        // handle user infomation here
                                    }
                                });
```

* **Store and checkout**

```
MySabaySDK.getInstance().showStoreView(new PaymentListener() {
                    @Override
                    public void purchaseMySabaySuccess(Object dataMySabay) {
                        // handle mysabay payment success here
                    }

                    @Override
                    public void purchaseIAPSuccess(Object dataIAP) {
                        // handle android in app purchase here
                    }

                    @Override
                    public void purchaseFailed(Object dataError) {
                        // hanlde error
                    }
                });
```

* **Refresh token** 

```
        MySabaySDK.getInstance().refreshToken(new RefreshTokenListener() {
                        @Override
                        public void refreshSuccess(String token) {
                            // work with token
                        }

                        @Override
                        public void refreshFailed(Throwable error) {
                            //handle error here
                        }
                    });
```

* **Get current token**

```
    MySabaySDK.getInstance().currentToken();
```

* **Check valid token**

```
       MySabaySDK.getInstance().isTokenValid();

```

* **Logout**

Call ```MySabaySDK.getInstance().logout();```

## mySabay API
### Server side validation
In order for the CP server to validate the user access token received from the client as valid, mySabay also hosts pulic user api for fetching user profile and validating token. The API document is available [here](https://api-reference.mysabay.com/).
