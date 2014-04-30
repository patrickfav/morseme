package at.favre.app.morseme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;

import at.favre.app.morseme.application.MorseApplication;
import at.favre.app.morseme.application.PreferenceHandler;
import at.favre.app.morseme.morse.MorseUtil;
import at.favre.app.morseme.morse.translators.FlashlightMorseTranslator;
import at.favre.app.morseme.morse.translators.ITranslatorListener;
import at.favre.app.morseme.morse.translators.SineMorseTranslator;
import at.favre.app.morseme.morse.translators.VibrateMorseTranslator;
import at.favre.app.morseme.views.FlowLayout;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageFragment extends Fragment implements View.OnClickListener {

    private EditText etMessage;
    private TextSwitcher textSwitcher;
    private FlowLayout flowLayout;

    private Typeface myTypeface;
    private AsyncTask<Void, Integer, Void> currentTask;
    private PreferenceHandler pref;

    public MessageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = ((MorseApplication) getActivity().getApplication()).getPreferenceHandler();
        myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "morse.ttf");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);

        flowLayout = (FlowLayout) rootView.findViewById(R.id.flowlayout);


        etMessage = (EditText) rootView.findViewById(R.id.et_message);
        rootView.findViewById(R.id.bt_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:");
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", SmsReceiver.MORSE_PREFIX + etMessage.getText().toString().trim());
                startActivity(it);
            }
        });

        etMessage.addTextChangedListener(new TextWatcher() {
            String textBefore;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (flowLayout != null) {
                    switchMorseTextAndButtons(charSequence.length() == 0);
                    if (textBefore.length() > charSequence.length() && flowLayout.getChildCount() > 0) { //deleted
                        checkDeleteMorse(charSequence.toString().toUpperCase());
                    } else {
                        TextView tv = createMorseTextLetter();
                        tv.setText(charSequence.toString().substring(charSequence.length() - 1, charSequence.length()).toUpperCase());
                        flowLayout.addView(tv);
                        final Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_top);
                        tv.startAnimation(anim);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        rootView.findViewById(R.id.btn_sine).setOnClickListener(this);
        rootView.findViewById(R.id.btn_vibrate).setOnClickListener(this);
        rootView.findViewById(R.id.btn_torch).setOnClickListener(this);

        rootView.findViewById(R.id.morseTextWrapper).setVisibility(View.INVISIBLE);

        return rootView;
    }


    private void switchMorseTextAndButtons(boolean newTextEmpty) {
        final Animation anim;
        View wrapper = getView().findViewById(R.id.morseTextWrapper);
        if (newTextEmpty && wrapper.getVisibility() == View.VISIBLE) {
            anim = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_out);
            wrapper.startAnimation(anim);
            wrapper.setVisibility(View.INVISIBLE);
        } else if (!newTextEmpty && wrapper.getVisibility() == View.INVISIBLE) {
            anim = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_fade_in);
            wrapper.startAnimation(anim);
            wrapper.setVisibility(View.VISIBLE);
        }

    }

    private void checkDeleteMorse(String newText) {
        flowLayout.removeAllViews();

        for (int i = 0; i < newText.length(); i++) {
            TextView tv = createMorseTextLetter();
            tv.setText(String.valueOf(newText.charAt(i)));
            flowLayout.addView(tv);
        }
    }


    private TextView createMorseTextLetter() {
        TextView tv = new TextView(getActivity());
        tv.setTypeface(myTypeface, Typeface.BOLD);
        tv.setTextSize(getResources().getDimensionPixelSize(R.dimen.morseTextSize));
        tv.setTextColor(getResources().getColor(R.color.dirtyWhiteBgTextColor));
        tv.setPadding(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()), 0, 0);
        return tv;
    }

    private void checkCurrentAndCancelTask() {
        if (currentTask != null && !currentTask.isCancelled()) {
            currentTask.cancel(true);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ActionBarActivity) getActivity()).getSupportActionBar().hide();
    }

	@Override
	public void onResume() {
		super.onResume();
		prepareStartAnimation(getView());
	}


	private void prepareStartAnimation(final View rootView) {
//		getView().findViewById(R.id.textView).setTranslationY(100);
		final Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_out_long);
		anim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				rootView.findViewById(R.id.darkWrapper).setVisibility(View.GONE);
				final Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_top);
				getView().findViewById(R.id.textView).startAnimation(anim);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				rootView.findViewById(R.id.darkWrapper).startAnimation(anim);

			}
		},900);
	}

	@Override
    public void onPause() {
        super.onPause();
        checkCurrentAndCancelTask();
    }

    @Override
    public void onClick(final View view) {
        if (view.isActivated()) {
            checkCurrentAndCancelTask();
            view.setActivated(false);
            cancelMorseHighlight();
            etMessage.setEnabled(true);
        } else {
            etMessage.setEnabled(false);
            etMessage.clearFocus();
            view.setActivated(true);
            checkCurrentAndCancelTask();
            ITranslatorListener listener = new ITranslatorListener() {
                @Override
                public void onMorseComplete(boolean canceld) {
                    view.setActivated(false);
                    etMessage.setEnabled(true);
                }

                @Override
                public void currentPlayedLetter(int letterIndex) {
                    showActiveLetter(letterIndex);
                }
            };

            switch (view.getId()) {
                case R.id.btn_sine:
                    currentTask = new SineMorseTranslator(MorseUtil.generateMorseSequenze(etMessage.getText().toString()), listener, pref.getSineMorseLength(), pref.getSinePauseLength());
                    break;
                case R.id.btn_torch:
                    currentTask = new FlashlightMorseTranslator(MorseUtil.generateMorseSequenze(etMessage.getText().toString()), listener, pref.getTorchMorseLength(), pref.getTorchPauseLength());
                    break;
                case R.id.btn_vibrate:
                    currentTask = new VibrateMorseTranslator(MorseUtil.generateMorseSequenze(etMessage.getText().toString()), listener, pref.getVibratorMorseLength(), pref.getVibratorPauseLength(), (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE));
                    break;
            }

            currentTask.execute();
        }
    }

    private void showActiveLetter(int letterIndex) {
        if (flowLayout.getChildAt(letterIndex) != null) {
            ((TextView) flowLayout.getChildAt(letterIndex)).setTextColor(getResources().getColor(R.color.dirtyWhiteBgTextColorHighlighted));
        } else {
            cancelMorseHighlight();
        }
    }

    private void cancelMorseHighlight() {
        for (int i = 0; i < flowLayout.getChildCount(); i++) {
            ((TextView) flowLayout.getChildAt(i)).setTextColor(getResources().getColor(R.color.dirtyWhiteBgTextColor));
        }
    }
}
