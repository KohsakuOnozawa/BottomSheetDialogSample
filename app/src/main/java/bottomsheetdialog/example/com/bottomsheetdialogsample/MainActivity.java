package bottomsheetdialog.example.com.bottomsheetdialogsample;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showDialogButton = (Button) findViewById(R.id.show_dialog_button);
        showDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SampleBottomSheetDialog sampleBottomSheetDialog = SampleBottomSheetDialog.newInstance();
                sampleBottomSheetDialog.show(getSupportFragmentManager(), SampleBottomSheetDialog.TAG);
            }
        });
    }

    public static class SampleBottomSheetDialog extends BottomSheetDialogFragment {

        public static final String TAG = SampleBottomSheetDialog.class.getSimpleName();
        private ViewTreeObserver.OnGlobalLayoutListener mListener;
        private View mContentView;

        public SampleBottomSheetDialog() {
        }

        public static SampleBottomSheetDialog newInstance() {
            final SampleBottomSheetDialog dialog = new SampleBottomSheetDialog();
            Bundle args = dialog.getArguments();
            if (args == null) {
                args = new Bundle();
            }
            dialog.setArguments(args);
            return dialog;
        }

        @Override
        public void setupDialog(Dialog dialog, int style) {
            super.setupDialog(dialog, style);
            mContentView = View.inflate(getContext(), R.layout.dialog_content, null);
            dialog.setContentView(mContentView);

            mListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // BottomSheetの高さなどを設定
                    View parent = (View) mContentView.getParent();
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
                    behavior.setPeekHeight(mContentView.getMeasuredHeight());
                    CoordinatorLayout.LayoutParams layoutParams =
                        (CoordinatorLayout.LayoutParams) ((View) mContentView.getParent()).getLayoutParams();
                    layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

                    // OnGlobalLayoutListenerを外す
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        removeGlobalLayoutListener();
                    } else {
                        removeGlobalLayoutListenerCompat();
                    }
                }
            };
            mContentView.getViewTreeObserver().addOnGlobalLayoutListener(mListener);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private void removeGlobalLayoutListener() {
            mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(mListener);
        }

        @SuppressWarnings("deprecation")
        private void removeGlobalLayoutListenerCompat() {
            mContentView.getViewTreeObserver().removeGlobalOnLayoutListener(mListener);
        }
    }
}
