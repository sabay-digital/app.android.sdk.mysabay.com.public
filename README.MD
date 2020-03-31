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
    implementation 'kh.com.mysabay:sabaysdk:1.0.6-b04'
}
```

## Configuration

MySabaySDK has default configurations with dark theme and sandbox mode to true.

```
final SdkConfiguration configuration = new SdkConfiguration.Builder("APPLICATION_ID",
                "{MySabayAppSecret}", "{LicenseKey}",
                "{MerchantID}")
                .setSdkTheme(SdkTheme.Dark) //default dark
                .setToUseSandBox(true) //default to sandbox
                .build();
        MySabaySDK.Impl.setDefaultInstanceConfiguration(configuration);
```
> NOTE: MySabaySdk requires configuration before usage.

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

    MySabaySDK.getInstance().getUserInfo(new UserInfoListener() {
                                    @Override
                                    public void userInfo(String info) {
                                        // handle user infomation here
                                    }
                                });
```

* **Store and checkout**

```
MySabaySDK.getInstance().showShopView(new PaymentListener() {
                    @Override
                    public void purchaseMySabaySuccess(Object dataMySabay) {
                        // handle mysabay payemtn success herer
                    }

                    @Override
                    public void purchaseIAPSuccess(Object dataIAP) {
                        // handle android app in purchase here
                    }

                    @Override
                    public void purchaseFailed(Object dataError) {
                        // hanlde error
                    }
                });
```

* **Logout**

Call ```MySabaySDK.getInstance().logout();```
