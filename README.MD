# Official MySabay SDK for Android

This is the official MySabay SDK for native Android application. To use this SDK, you can follow the guides below or download the test with the example project we have in this repository.

## Version
Sabay currently publishes four versions of android SDK:
- Latest: 1.0.5
    - Android-X: 'com.github.sabay-digital:sdk.android-x.mysabay.com:1.0.5'
    - Old Android: 'com.github.sabay-digital:sdk.android-old.mysabay.com:1.0.5-o'
    - Changes
        - Add packageId filed into payment response
- 1.0.4
    - Android-X: 'com.github.sabay-digital:sdk.android-x.mysabay.com:1.0.4'
    - Old Android: 'com.github.sabay-digital:sdk.android-old.mysabay.com:1.0.4-o'
    - Changes
        - Callback after payment completed
        - Handle payment success by type
- 1.0.3
    - Android-X: 'com.github.sabay-digital:sdk.android-x.mysabay.com:1.0.3'
    - Old Android: 'com.github.sabay-digital:sdk.android-old.mysabay.com:1.0.3-o'
    - Changes
        - Subscribe login to pass accessToken
- 1.0.2
    - Android-X: 'com.github.sabay-digital:sdk.android-x.mysabay.com:1.0.2'
    - Old Android: 'com.github.sabay-digital:sdk.android-old.mysabay.com:1.0.2-o'
    - Changes:
        - Received accessToken, refreshToken, expire when webview loaded
        - Received UUID when get userprofile completed

- 1.0.0
    - Android-X: 'com.github.sabay-digital:sdk.android-x.mysabay.com:1.0.0'
    - Old Android: 'com.github.sabay-digital:sdk.android-old.mysabay.com:1.0.0-o'
    - Changes:
        - Received accessToken, refreshToken and expire when verify success
        - Remove refreshToken and expire from UserProfile
        - Create new one by using refreshToken to get new accessToken and expire

## Create your application

Create your MySabay application if you don't have one yet at [MySabay App Dashboard](https://kh.mysabay.com:8443/index.html) and copy your `appId` 
and `appSecret` for the integration. 

## Workflow
The login and payment workflow is described with the following diagram for communication between CP app, server, mySabay SDK and mySabay API.

### Login flow
<img src="Images/user-login-flow.png">

### Payment flow
There are 2 different payment flows in the SDK: 1). payment with google In App Billing 2). payment with mySabay Wallet which includes different payment options such as Telco, Sabay Coin, and the list will continue as we are working to add more payment service providers such as banks. You will have to implement the SDK payment following the 2 flows below

#### 1). Payment with In-App Billing:
<img src="Images/payment-flow-iap.png">

#### 2). Payment with mySabay Wallet:
<img src="Images/payment-flow-ssn.png">

Refer to the API document below for payment receipt validation of both payments.

## Installation

1. Add Jitpack to your project build.gralde file

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. Then add this dependency to your app build.gradle file.

```gradle
dependencies {
    implementation 'com.github.sabay-digital:sdk.android-x.mysabay.com:1.0.4'
}
```

Add dataBinding to gradle
```gradle
android {
    ...
    dataBinding {
        enabled = true
    }
}
```

Add compileOptions
```grale    
android {
    ...
    compileOptions {
        targetCompatibility JavaVersion.VERSION_1_8
        sourceCompatibility JavaVersion.VERSION_1_8
}
```

Add thease dependencies if you use android appcompat
```gradle
dependencies {
    implementation 'com.android.support:design:28.0.0'
    implementation 'com.afollestad.material-dialogs:core:0.9.6.0'
}
```

3. Initialize SDK & declare Permissions:

MysabaySdk needs to be initialized. You should only do this 1 time, so placing the initialization in your Application is a good idea. An example for this would be:

```java
[MyApplication.java]
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //MySabaySDK has default configuration with dark theme and sandbox url.
        final SdkConfiguration configuration = new SdkConfiguration.Builder(
                        "55", // mysabay app Id
                        "SDK sample", //mysabay  app name
                        "9c85c50a4362f687cd4507771ba81db5cf50eaa0b3008f4f943f77ba3ac6386b", //MySabay App Secret
                        "", // license key
                        "") // merchant id
                        .setSdkTheme(SdkTheme.Light)
                        .setToUseSandBox(true).build();
                MySabaySDK.Impl.setDefaultInstanceConfiguration(this, configuration);
    }
}
```
> NOTE: MySabaySdk is need configuration

## Integration

> Note that in order to use the store and checkout function, the user must login first.
> Follow the guide below for each functions provided by the SDK:

*  **Login**

Call 

```java
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

```java

    MySabaySDK.getInstance().getUserProfile(new UserInfoListener() {
        @Override
        public void userInfo(String info) {
            // handle user infomation here
        }
    });
```

* **Store and checkout**

```java
    MySabaySDK.getInstance().showStoreView(new PaymentListener() {
        @Override
        public void purchaseSuccess(SubscribePayment data) {
            if (data.getType().equals(Globals.APP_IN_PURCHASE)) {
                // handle android in app purchase here
            } else if (data.getType().equals(Globals.MY_SABAY)) {
                // handle mysabay payment success here
            } else {
                // handle onetime payment here
            }
        }
        @Override
        public void purchaseFailed(Object dataError) {
            // hanlde error
        }
    });
```

* **Refresh token** 

```java
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

```java
    MySabaySDK.getInstance().currentToken();
```

* **Check valid token**

```java
    MySabaySDK.getInstance().isTokenValid();
```

* **Logout**

To logout user session from the app use the following method:

``` java
    MySabaySDK.getInstance().logout();
```

## mySabay API
### Server side validation
- In order for the CP server to validate the user access token received from the client as valid, mySabay also hosts pulic user api for fetching user profile and validating token. The API document is available [here](https://api-reference.mysabay.com/).

- In order for the CP server to validate the payment receipt from In-App Billing or mySabay wallet, mySabay also hosts public store api available [here](https://api-reference.mysabay.com/?urls.primaryName=mySabay%20SDK%20-%20Store%20API).

### Note 
* This SDK supports Android with  minSdkVersion 21 only.


