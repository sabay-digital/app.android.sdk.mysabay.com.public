package kh.com.mysabay.sample;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import kh.com.mysabay.sample.databinding.ActivityMainBinding;
import kh.com.mysabay.sdk.Globals;
import kh.com.mysabay.sdk.MySabaySDK;
import kh.com.mysabay.sdk.SdkConfiguration;
import kh.com.mysabay.sdk.callback.LoginListener;
import kh.com.mysabay.sdk.callback.PaymentListener;
import kh.com.mysabay.sdk.callback.RefreshTokenListener;
import kh.com.mysabay.sdk.pojo.payment.SubscribePayment;
import kh.com.mysabay.sdk.utils.LogUtil;
import kh.com.mysabay.sdk.utils.MessageUtil;
import kh.com.mysabay.sdk.utils.SdkTheme;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewBinding.viewPb.setVisibility(View.GONE);
        findViewById(R.id.show_login_screen).setOnClickListener(v -> {
            mViewBinding.viewPb.setVisibility(View.VISIBLE);
            if (MySabaySDK.getInstance().isLogIn()) {
                MessageUtil.displayDialog(v.getContext(), "User already login", "choose option below",
                        "Logout", "Get user information",
                        (dialog, which) -> {
                            MySabaySDK.getInstance().logout();
                        }, (dialog, which) -> MySabaySDK.getInstance().getUserProfile(info -> {
                            MessageUtil.displayDialog(v.getContext(), info);
                        }));
                mViewBinding.viewPb.setVisibility(View.GONE);
            } else
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
        });

        mViewBinding.showPaymentPreAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySabaySDK.getInstance().showStoreView(new PaymentListener() {
                    @Override
                    public void purchaseSuccess(SubscribePayment data) {
                        if (data.getType().equals(Globals.APP_IN_PURCHASE)) {
                            LogUtil.info(data.getType(), data.data.toString());
                            MessageUtil.displayToast(v.getContext(), data.getType() + " Payment Completed");
                        } else if (data.getType().equals(Globals.MY_SABAY)) {
                            LogUtil.info(data.getType(), data.data.toString());
                            MessageUtil.displayToast(v.getContext(), data.getType() + " Payment Completed");
                        } else {
                            LogUtil.info(data.getType(), data.data.toString());
                            MessageUtil.displayToast(v.getContext(), data.getType() + " Payment Completed");
                        }
                    }

                    @Override
                    public void purchaseFailed(Object dataError) {
                        MessageUtil.displayToast(v.getContext(), "error = " + dataError);
                    }
                });
            }
        });

        mViewBinding.btnGetToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MySabaySDK.getInstance().isLogIn())
                    MessageUtil.displayToast(v.getContext(), "current token =" + MySabaySDK.getInstance().currentToken());
                else
                    MessageUtil.displayToast(v.getContext(), "Need user login");
            }
        });

        mViewBinding.btnRefreshToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MySabaySDK.getInstance().isLogIn()) {
                    MySabaySDK.getInstance().refreshToken(new RefreshTokenListener() {
                        @Override
                        public void refreshSuccess(String token) {
                            MessageUtil.displayToast(v.getContext(), "refresh token = " + token);
                        }

                        @Override
                        public void refreshFailed(Throwable error) {
                            MessageUtil.displayToast(v.getContext(), "error in refresh token " + error.getLocalizedMessage());
                        }
                    });
                } else
                    MessageUtil.displayToast(v.getContext(), "Need user login");
            }
        });

        mViewBinding.btnValidateTokenExpired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MySabaySDK.getInstance().isLogIn())
                    MessageUtil.displayToast(v.getContext(), "Token is valid =" + MySabaySDK.getInstance().isTokenValid());
                else
                    MessageUtil.displayToast(v.getContext(), "Need user login");
            }
        });
    }

    @Override
    protected void onStop() {
        mViewBinding.viewPb.setVisibility(View.GONE);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MySabaySDK.getInstance().destroy();
    }

    @Override
    public void onBackPressed() {
        mViewBinding.viewPb.setVisibility(View.GONE);
        super.onBackPressed();
    }
}
