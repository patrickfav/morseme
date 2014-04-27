package at.favre.app.morseme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
public class MessageFragment extends Fragment {

	private EditText etMessage;
	private TextSwitcher textSwitcher;
	private FlowLayout flowLayout;

	private Typeface myTypeface;
	private AsyncTask<Void,Void,Void> currentTask;
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
				it.putExtra("sms_body", SmsReceiver.MORSE_PREFIX+etMessage.getText().toString().trim());
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
				if(flowLayout != null) {
					if (textBefore.length() > charSequence.length() && flowLayout.getChildCount() > 0){ //deleted
						flowLayout.removeViewAt(flowLayout.getChildCount() - 1);
					}else{
						TextView tv = createMorseTextLetter();
						tv.setText(charSequence.toString().substring(charSequence.length()-1,charSequence.length()).toUpperCase());
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


		rootView.findViewById(R.id.btn_sine).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				if(view.isActivated()) {
					checkCurrentAndCancelTask();
					view.setActivated(false);
				} else {
					view.setActivated(true);
					checkCurrentAndCancelTask();
					currentTask = new SineMorseTranslator(MorseUtil.generateMorseSequenze(etMessage.getText().toString()), new ITranslatorListener() {
						@Override
						public void onMorseComplete(boolean canceld) {
							view.setActivated(false);
						}
					},pref.getSineMorseLength(),pref.getSinePauseLength());
					currentTask.execute();
				}
			}
		});

		rootView.findViewById(R.id.btn_vibrate).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				if (view.isActivated()) {
					checkCurrentAndCancelTask();
					view.setActivated(false);
				} else {
					view.setActivated(true);
					checkCurrentAndCancelTask();
					currentTask = new VibrateMorseTranslator(MorseUtil.generateMorseSequenze(etMessage.getText().toString()), new ITranslatorListener() {
						@Override
						public void onMorseComplete(boolean canceld) {
							view.setActivated(false);
						}
					},pref.getVibratorMorseLength(),pref.getVibratorPauseLength(),(Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE));
					currentTask.execute();
				}
			}
		});

		rootView.findViewById(R.id.btn_torch).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				if(view.isActivated()) {
					checkCurrentAndCancelTask();
					view.setActivated(false);
				} else {
					view.setActivated(true);
					checkCurrentAndCancelTask();
					currentTask = new FlashlightMorseTranslator(MorseUtil.generateMorseSequenze(etMessage.getText().toString()), new ITranslatorListener() {
						@Override
						public void onMorseComplete(boolean canceld) {
							view.setActivated(false);
						}
					},pref.getTorchMorseLength(),pref.getTorchPauseLength());
					currentTask.execute();
				}
			}
		});

		return rootView;
	}

	private TextView createMorseTextLetter() {
		TextView tv = new TextView(getActivity());
		tv.setTypeface(myTypeface,Typeface.BOLD);
		tv.setTextSize(getResources().getDimensionPixelSize(R.dimen.morseTextSize));
		tv.setTextColor(getResources().getColor(R.color.dirtyWhiteBgTextColor));
		tv.setPadding(0,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()),0,0);
		return tv;
	}

	private void checkCurrentAndCancelTask() {
		if(currentTask != null && !currentTask.isCancelled()) {
			currentTask.cancel(true);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((ActionBarActivity) getActivity()).getSupportActionBar().hide();
	}
}
